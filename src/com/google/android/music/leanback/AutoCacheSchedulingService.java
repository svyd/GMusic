package com.google.android.music.leanback;

import android.accounts.Account;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import com.google.android.gsf.Gservices;
import com.google.android.music.download.AbstractSchedulingService;
import com.google.android.music.download.AbstractSchedulingService.DisableReason;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.DownloadProgress;
import com.google.android.music.download.DownloadRequest;
import com.google.android.music.download.DownloadRequest.Owner;
import com.google.android.music.download.cache.FileLocation;
import com.google.android.music.download.cache.ICacheManager;
import com.google.android.music.download.cache.OutOfSpaceException;
import com.google.android.music.log.Log;
import com.google.android.music.net.INetworkMonitor;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.AutoCacheManager;
import com.google.android.music.store.DataNotFoundException;
import com.google.android.music.store.MusicContent;
import com.google.android.music.store.MusicFile;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.SystemUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public final class AutoCacheSchedulingService extends AbstractSchedulingService
{
  private static final boolean LOGV = LeanbackLog.LOGV;
  private static AtomicReference<PowerManager.WakeLock> sWakeLock = new AtomicReference();
  private long mAppUseTime = 0L;
  private long mConnectivityRecoveryTime = 0L;
  private boolean mCurrentUpdateRefreshedMixes = false;
  private long mCurrentUpdateTriggerTime = 0L;
  private DailyWindow mDailyWindow;
  private boolean mInitializedMixes = false;
  private long mLastAttemptTime = 0L;
  private long mLastUpdateTriggerTime = 0L;
  private SharedPreferences.OnSharedPreferenceChangeListener mPrefChangeListener;
  private long mScheduledTriggerTime = 0L;
  private int mSourceAccount = 0;
  private volatile int mStartId;

  public AutoCacheSchedulingService()
  {
    super("MusicLeanback", localOwner, null);
    SharedPreferences.OnSharedPreferenceChangeListener local2 = new SharedPreferences.OnSharedPreferenceChangeListener()
    {
      public void onSharedPreferenceChanged(SharedPreferences paramAnonymousSharedPreferences, String paramAnonymousString)
      {
        if (AutoCacheSchedulingService.this.getMusicPreferences().isAutoCachingEnabled())
          return;
        if (AutoCacheSchedulingService.LOGV)
          Log.d("MusicLeanback", "Autocaching is no longer enabled in the preferences");
        AutoCacheSchedulingService.this.stopAutoCaching();
      }
    };
    this.mPrefChangeListener = local2;
  }

  private static void acquireWakeLock(Context paramContext)
  {
    MusicUtils.assertMainProcess(paramContext, "AutoCaching WakeLock acquired in wrong process");
    MusicUtils.assertMainThread();
    synchronized (sWakeLock)
    {
      PowerManager.WakeLock localWakeLock = (PowerManager.WakeLock)sWakeLock.get();
      if (localWakeLock == null)
      {
        PowerManager localPowerManager = (PowerManager)paramContext.getSystemService("power");
        String str = AutoCacheSchedulingService.class.getName();
        localWakeLock = localPowerManager.newWakeLock(1, str);
        sWakeLock.set(localWakeLock);
      }
      localWakeLock.acquire();
      if (!LOGV)
        return;
      Log.d("MusicLeanback", "Acquired WakeLock");
      return;
    }
  }

  private void changeAlarmEnabledState(Long paramLong)
  {
    AlarmManager localAlarmManager = (AlarmManager)getSystemService("alarm");
    Intent localIntent = new Intent("com.google.android.music.leanback.AUTO_CACHE_ALARM");
    PendingIntent localPendingIntent = PendingIntent.getBroadcast(this, 0, localIntent, 0);
    if (paramLong != null)
    {
      if (LOGV)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Set alarm for: ");
        long l1 = paramLong.longValue();
        Date localDate = new Date(l1);
        String str = localDate;
        Log.i("MusicLeanback", str);
      }
      long l2 = paramLong.longValue();
      localAlarmManager.set(0, l2, localPendingIntent);
      return;
    }
    localAlarmManager.cancel(localPendingIntent);
  }

  private boolean checkAttemptFrequencyAndReschedule()
  {
    boolean bool = true;
    if (this.mLastAttemptTime <= 0L)
      if (LOGV)
        Log.i("MusicLeanback", "Attempt allowed: no previous");
    while (true)
    {
      return bool;
      long l1 = System.currentTimeMillis();
      long l2 = this.mLastAttemptTime;
      long l3 = l1 - l2;
      long l4 = this.mDailyWindow.geFrequencyInMillisec();
      if (l3 > l4)
      {
        if (LOGV)
        {
          String str1 = "Attempt allowed: " + l3 + " exceeds window frequency";
          Log.d("MusicLeanback", str1);
        }
      }
      else
      {
        long l5 = Gservices.getLong(getContentResolver(), "music_autocache_min_retry_delay_seconds", 3600L) * 1000L;
        if (l3 > l5)
        {
          if (LOGV)
          {
            String str2 = "Attempt allowed: " + l3 + " exceeds retry frequency of " + l5;
            Log.d("MusicLeanback", str2);
          }
        }
        else
        {
          if (LOGV)
          {
            StringBuilder localStringBuilder = new StringBuilder().append("Attempt dissallowed. Previous: ");
            long l6 = this.mLastAttemptTime;
            Date localDate = new Date(l6);
            String str3 = localDate + " Retry frequency: " + l5;
            Log.d("MusicLeanback", str3);
          }
          long l7 = this.mLastAttemptTime + l5 + 1000L;
          scheduleAlarm(l7);
          bool = false;
        }
      }
    }
  }

  private boolean checkAutoCacheConditions(boolean paramBoolean)
  {
    boolean bool1 = true;
    boolean bool2 = getMusicPreferences().isAutoCachingEnabled();
    boolean bool3 = SystemUtils.isBatteryCharging(this);
    if ((paramBoolean) && (!bool3))
    {
      if (LOGV)
        Log.d("MusicLeanback", "Power connected but stale charging state detected. Overriding.");
      bool3 = true;
    }
    boolean bool4 = hasHighSpeedConnection();
    boolean bool5 = checkAutoCacheWindow();
    if (LOGV)
    {
      Object[] arrayOfObject = new Object[4];
      Boolean localBoolean1 = Boolean.valueOf(bool2);
      arrayOfObject[0] = localBoolean1;
      Boolean localBoolean2 = Boolean.valueOf(bool3);
      arrayOfObject[1] = localBoolean2;
      Boolean localBoolean3 = Boolean.valueOf(bool4);
      arrayOfObject[2] = localBoolean3;
      Boolean localBoolean4 = Boolean.valueOf(bool5);
      arrayOfObject[3] = localBoolean4;
      String str = String.format("autoCacheConditions: enabled=%s, charging=%s, onWifi=%s, inWindow=%s", arrayOfObject);
      Log.d("MusicLeanback", str);
    }
    if ((bool2) && (bool3) && (bool4) && (bool5));
    while (true)
    {
      return bool1;
      bool1 = false;
    }
  }

  private boolean checkAutoCacheWindow()
  {
    if (this.mDailyWindow == null)
    {
      MusicPreferences localMusicPreferences = getMusicPreferences();
      DailyWindow localDailyWindow1 = createDailyWindow(this, localMusicPreferences);
      this.mDailyWindow = localDailyWindow1;
    }
    long l1 = System.currentTimeMillis();
    DailyWindow localDailyWindow2 = this.mDailyWindow;
    long l2 = this.mLastUpdateTriggerTime;
    if (!localDailyWindow2.isInWindow(l2))
      if (LOGV)
        Log.d("MusicLeanback", "Last update was outside the window");
    DailyWindow localDailyWindow3;
    long l3;
    for (boolean bool = this.mDailyWindow.isInWindow(l1); ; bool = localDailyWindow3.isInSameWindow(l3, l1))
    {
      return bool;
      localDailyWindow3 = this.mDailyWindow;
      l3 = this.mLastUpdateTriggerTime;
    }
  }

  private boolean checkIfAppIsUsedAndReschedule()
  {
    boolean bool = true;
    if (this.mLastUpdateTriggerTime <= 0L);
    while (true)
    {
      return bool;
      long l1 = this.mAppUseTime;
      long l2 = this.mLastUpdateTriggerTime;
      if (l1 < l2)
      {
        long l3 = Store.getInstance(this).getMostRecentPlayDate();
        long l4 = this.mAppUseTime;
        if (l3 > l4)
        {
          this.mAppUseTime = l3;
          saveState();
        }
        long l5 = this.mAppUseTime;
        long l6 = this.mLastUpdateTriggerTime;
        if (l5 < l6)
        {
          if (LOGV)
          {
            StringBuilder localStringBuilder1 = new StringBuilder().append("Not used since ");
            long l7 = this.mAppUseTime;
            Date localDate1 = new Date(l7);
            String str1 = localDate1;
            Log.i("MusicLeanback", str1);
          }
          if (LOGV)
          {
            StringBuilder localStringBuilder2 = new StringBuilder().append("Not played since ");
            Date localDate2 = new Date(l3);
            String str2 = localDate2;
            Log.i("MusicLeanback", str2);
          }
          DailyWindow localDailyWindow = this.mDailyWindow;
          long l8 = System.currentTimeMillis();
          scheduleDailyWindowAlarm(localDailyWindow, l8);
          bool = false;
        }
      }
    }
  }

  private static DailyWindow createDailyWindow(Context paramContext, MusicPreferences paramMusicPreferences)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    int i = paramMusicPreferences.getAutoCacheMinutesSinceMidnight();
    int j = Gservices.getInt(localContentResolver, "music_autocache_minutes_since_midnight", i);
    int k = paramMusicPreferences.getAutoCacheDurationInMinutes();
    int m = Gservices.getInt(localContentResolver, "music_autocache_duration_in_minutes", k);
    int n = Gservices.getInt(localContentResolver, "music_autocache_frequency_in_minutes", 1440);
    int i1 = Gservices.getInt(localContentResolver, "music_autocache_trigger_window_percentage", 40);
    DailyWindow.Builder localBuilder1 = DailyWindow.Builder.startBuildingInMinutes(j, m);
    DailyWindow.Builder localBuilder2 = localBuilder1.setFrequencyInMinutes(n);
    DailyWindow.Builder localBuilder3 = localBuilder1.setVariableTriggerPercentage(i1);
    DailyWindow localDailyWindow = localBuilder1.build();
    if (LOGV)
    {
      String str = "DailyWindow: " + localDailyWindow;
      Log.i("MusicLeanback", str);
    }
    return localDailyWindow;
  }

  private void disable()
  {
    SystemUtils.setComponentEnabled(this, ActionReceiver.class, false);
    changeAlarmEnabledState(null);
  }

  private void doUpdate()
  {
    long l1 = System.currentTimeMillis();
    if (this.mCurrentUpdateTriggerTime == 0L)
    {
      this.mCurrentUpdateTriggerTime = l1;
      this.mCurrentUpdateRefreshedMixes = false;
    }
    this.mLastAttemptTime = l1;
    saveState();
    if (!this.mCurrentUpdateRefreshedMixes)
    {
      MusicPreferences localMusicPreferences = getMusicPreferences();
      if (!new SuggestedMixUpdater(this, localMusicPreferences).updateMixes())
      {
        Log.w("MusicLeanback", "Suggested mix update failed");
        DailyWindow localDailyWindow = this.mDailyWindow;
        long l2 = System.currentTimeMillis();
        scheduleDailyWindowAlarm(localDailyWindow, l2);
        return;
      }
      this.mCurrentUpdateRefreshedMixes = true;
      this.mInitializedMixes = true;
      saveState();
    }
    finishUpdate(true);
  }

  private void enableActionReceiver()
  {
    SystemUtils.setComponentEnabled(this, ActionReceiver.class, true);
  }

  private void finishUpdate(boolean paramBoolean)
  {
    if (LOGV)
    {
      Object[] arrayOfObject = new Object[2];
      Boolean localBoolean = Boolean.valueOf(paramBoolean);
      arrayOfObject[0] = localBoolean;
      long l1 = this.mCurrentUpdateTriggerTime;
      Date localDate = new Date(l1);
      arrayOfObject[1] = localDate;
      String str = String.format("Finished update. Success: %b Started: %s", arrayOfObject);
      Log.i("MusicLeanback", str);
    }
    long l2 = this.mCurrentUpdateTriggerTime;
    this.mLastUpdateTriggerTime = l2;
    this.mCurrentUpdateTriggerTime = 0L;
    this.mCurrentUpdateRefreshedMixes = false;
    saveState();
    DailyWindow localDailyWindow = this.mDailyWindow;
    long l3 = this.mLastUpdateTriggerTime;
    scheduleDailyWindowAlarm(localDailyWindow, l3);
  }

  private AutoCacheManager getAutoCacheManager()
  {
    return Store.getInstance(this).getAutoCacheManager();
  }

  private boolean hasHighSpeedConnection()
  {
    INetworkMonitor localINetworkMonitor = getNetworkMonitor();
    boolean bool1 = false;
    if (localINetworkMonitor != null);
    try
    {
      boolean bool2 = localINetworkMonitor.hasHighSpeedConnection();
      bool1 = bool2;
      label20: return bool1;
    }
    catch (RemoteException localRemoteException)
    {
      break label20;
    }
  }

  private static boolean isWakeLockHeld()
  {
    synchronized (sWakeLock)
    {
      PowerManager.WakeLock localWakeLock = (PowerManager.WakeLock)sWakeLock.get();
      if (localWakeLock == null)
      {
        bool = false;
        return bool;
      }
      boolean bool = localWakeLock.isHeld();
    }
  }

  private void loadState()
  {
    SharedPreferences localSharedPreferences = getSharedPreferences("autocache.prefs", 0);
    long l1 = localSharedPreferences.getLong("last.update.time", 0L);
    this.mLastUpdateTriggerTime = l1;
    long l2 = localSharedPreferences.getLong("current.update.time", 0L);
    this.mCurrentUpdateTriggerTime = l2;
    long l3 = localSharedPreferences.getLong("scheduled.trigger.time", 0L);
    this.mScheduledTriggerTime = l3;
    boolean bool1 = localSharedPreferences.getBoolean("current.update.mixes.refreshed", false);
    this.mCurrentUpdateRefreshedMixes = bool1;
    boolean bool2 = localSharedPreferences.getBoolean("initialized", false);
    this.mInitializedMixes = bool2;
    int i = localSharedPreferences.getInt("account", 0);
    this.mSourceAccount = i;
    long l4 = localSharedPreferences.getLong("last.attempt.time", 0L);
    this.mLastAttemptTime = l4;
    long l5 = localSharedPreferences.getLong("app.use.time", 0L);
    this.mAppUseTime = l5;
    long l6 = localSharedPreferences.getLong("connectivity.recovery.time", 0L);
    this.mConnectivityRecoveryTime = l6;
    if (!LOGV)
      return;
    Object[] arrayOfObject = new Object[9];
    long l7 = this.mLastUpdateTriggerTime;
    Date localDate1 = new Date(l7);
    arrayOfObject[0] = localDate1;
    long l8 = this.mCurrentUpdateTriggerTime;
    Date localDate2 = new Date(l8);
    arrayOfObject[1] = localDate2;
    long l9 = this.mScheduledTriggerTime;
    Date localDate3 = new Date(l9);
    arrayOfObject[2] = localDate3;
    Boolean localBoolean1 = Boolean.valueOf(this.mCurrentUpdateRefreshedMixes);
    arrayOfObject[3] = localBoolean1;
    Boolean localBoolean2 = Boolean.valueOf(this.mInitializedMixes);
    arrayOfObject[4] = localBoolean2;
    Integer localInteger = Integer.valueOf(this.mSourceAccount);
    arrayOfObject[5] = localInteger;
    long l10 = this.mLastAttemptTime;
    Date localDate4 = new Date(l10);
    arrayOfObject[6] = localDate4;
    long l11 = this.mAppUseTime;
    Date localDate5 = new Date(l11);
    arrayOfObject[7] = localDate5;
    long l12 = this.mConnectivityRecoveryTime;
    Date localDate6 = new Date(l12);
    arrayOfObject[8] = localDate6;
    String str = String.format("State: lastUpdateTrigger=%s, currentUpdateTrigger=%s, scheduledTrigger=%s, currentUpdateRefreshedMixes=%s, initializedMixes=%s, sourceAccount=%s, lastAttempt=%s, appUseTime=%s, connectivityRecoveryTime=%s", arrayOfObject);
    Log.d("MusicLeanback", str);
  }

  private static boolean releaseWakeLock()
  {
    boolean bool = false;
    synchronized (sWakeLock)
    {
      PowerManager.WakeLock localWakeLock = (PowerManager.WakeLock)sWakeLock.get();
      if (localWakeLock != null)
      {
        if (localWakeLock.isHeld())
        {
          localWakeLock.release();
          bool = true;
        }
      }
      else
      {
        if ((bool) && (LOGV))
          Log.d("MusicLeanback", "Released WakeLock");
        return bool;
      }
      Exception localException = new Exception();
      Log.wtf("MusicLeanback", "Expecting to hold wakelock", localException);
    }
  }

  private void saveState()
  {
    SharedPreferences.Editor localEditor1 = getSharedPreferences("autocache.prefs", 0).edit();
    long l1 = this.mLastUpdateTriggerTime;
    SharedPreferences.Editor localEditor2 = localEditor1.putLong("last.update.time", l1);
    long l2 = this.mCurrentUpdateTriggerTime;
    SharedPreferences.Editor localEditor3 = localEditor1.putLong("current.update.time", l2);
    long l3 = this.mScheduledTriggerTime;
    SharedPreferences.Editor localEditor4 = localEditor1.putLong("scheduled.trigger.time", l3);
    boolean bool1 = this.mCurrentUpdateRefreshedMixes;
    SharedPreferences.Editor localEditor5 = localEditor1.putBoolean("current.update.mixes.refreshed", bool1);
    boolean bool2 = this.mInitializedMixes;
    SharedPreferences.Editor localEditor6 = localEditor1.putBoolean("initialized", bool2);
    int i = this.mSourceAccount;
    SharedPreferences.Editor localEditor7 = localEditor1.putInt("account", i);
    long l4 = this.mLastAttemptTime;
    SharedPreferences.Editor localEditor8 = localEditor1.putLong("last.attempt.time", l4);
    long l5 = this.mAppUseTime;
    SharedPreferences.Editor localEditor9 = localEditor1.putLong("app.use.time", l5);
    long l6 = this.mConnectivityRecoveryTime;
    SharedPreferences.Editor localEditor10 = localEditor1.putLong("connectivity.recovery.time", l6);
    boolean bool3 = localEditor1.commit();
  }

  private void scheduleAlarm(long paramLong)
  {
    Long localLong = Long.valueOf(paramLong);
    changeAlarmEnabledState(localLong);
  }

  private void scheduleDailyWindowAlarm(DailyWindow paramDailyWindow, long paramLong)
  {
    long l1 = paramDailyWindow.getTriggerTime(paramLong);
    this.mScheduledTriggerTime = l1;
    saveState();
    long l2 = this.mScheduledTriggerTime;
    scheduleAlarm(l2);
  }

  private long selectConnectivityRecoveryTime()
  {
    int i = Gservices.getInt(getContentResolver(), "music_max_connectivity_restore_delay_seconds", 180);
    long l1 = System.currentTimeMillis();
    long l2 = new Random().nextInt(i);
    long l3 = 1000L * l2;
    long l4 = l1 + l3;
    if (LOGV)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Delaying network activity until ");
      Date localDate = new Date(l4);
      String str = localDate;
      Log.d("MusicLeanback", str);
    }
    return l4;
  }

  private void startAutoCaching()
  {
    Log.i("MusicLeanback", "Start autocaching.");
    int i = this.mStartId;
    sendInitScheduleMessage(i);
  }

  private static void startService(Context paramContext, Intent paramIntent)
  {
    Object localObject1 = paramIntent.getAction();
    String str = "com.google.android.music.leanback.ACTIVATE_AUTO_CACHE";
    if ("com.google.android.music.START_DOWNLOAD_SCHEDULING".equals(localObject1))
      str = "com.google.android.music.leanback.APP_IN_USE";
    while (true)
    {
      localObject1 = new Intent(paramContext, AutoCacheSchedulingService.class);
      Intent localIntent = ((Intent)localObject1).setAction(str);
      acquireWakeLock(paramContext);
      try
      {
        if (paramContext.startService((Intent)localObject1) == null)
        {
          Log.e("MusicLeanback", "Failed to start AutoCaching service");
          i = 0;
          return;
          boolean bool1;
          return;
          if ("android.net.conn.CONNECTIVITY_CHANGE".equals(localObject1))
          {
            if (paramIntent.getBooleanExtra("noConnectivity", false))
            {
              return;
              return;
            }
            str = "com.google.android.music.leanback.CONNECTIVITY_CHANGE";
            continue;
          }
          if (!"android.intent.action.ACTION_POWER_CONNECTED".equals(localObject1))
            continue;
          str = "com.google.android.music.leanback.POWER_CONNECTED";
          continue;
        }
        int i = 1;
      }
      finally
      {
        boolean bool2 = releaseWakeLock();
      }
    }
  }

  private void stopAutoCaching()
  {
    Log.i("MusicLeanback", "Stop autocaching.");
    int i = this.mStartId;
    sendCancelDownloadsMessage(i);
  }

  private boolean updateSuggestedMixes(boolean paramBoolean1, boolean paramBoolean2)
  {
    MusicPreferences localMusicPreferences = getMusicPreferences();
    Account localAccount = localMusicPreferences.getStreamingAccount();
    boolean bool1;
    boolean bool2;
    if (localAccount == null)
    {
      if (LOGV)
        Log.w("MusicLeanback", "No valid streaming account configured");
      bool1 = true;
      if (!bool1)
        break label61;
      resetState();
      disable();
      bool2 = false;
    }
    while (true)
    {
      return bool2;
      bool1 = SuggestedMixUpdater.cleanupIfDisabled(this);
      break;
      label61: DailyWindow localDailyWindow1 = createDailyWindow(this, localMusicPreferences);
      this.mDailyWindow = localDailyWindow1;
      int i = Store.computeAccountHash(localAccount);
      int j = this.mSourceAccount;
      if (i != j)
      {
        Log.i("MusicLeanback", "New source account");
        resetState();
        this.mSourceAccount = i;
      }
      enableActionReceiver();
      if (paramBoolean1)
      {
        long l1 = System.currentTimeMillis();
        this.mAppUseTime = l1;
        saveState();
      }
      long l2 = System.currentTimeMillis();
      long l3 = this.mScheduledTriggerTime;
      if (l2 < l3)
      {
        DailyWindow localDailyWindow2 = this.mDailyWindow;
        long l4 = this.mLastAttemptTime;
        if (!localDailyWindow2.isInSameWindow(l4, l2))
        {
          if (LOGV)
            Log.d("MusicLeanback", "Still before the scheduled trigger. Bailing");
          bool2 = false;
        }
      }
      else if (paramBoolean2)
      {
        if (this.mConnectivityRecoveryTime == 0L)
        {
          long l5 = selectConnectivityRecoveryTime();
          this.mConnectivityRecoveryTime = l5;
          saveState();
        }
        long l6 = this.mConnectivityRecoveryTime;
        scheduleAlarm(l6);
        bool2 = false;
      }
      else
      {
        if (this.mConnectivityRecoveryTime != 0L)
        {
          if (LOGV)
            Log.d("MusicLeanback", "Recovered from connectivity change");
          this.mConnectivityRecoveryTime = 0L;
          saveState();
        }
        if (!checkIfAppIsUsedAndReschedule())
        {
          bool2 = false;
        }
        else if (!checkAttemptFrequencyAndReschedule())
        {
          bool2 = true;
        }
        else
        {
          bool2 = true;
          if (!this.mInitializedMixes)
          {
            if (LOGV)
              Log.i("MusicLeanback", "Initial update");
            doUpdate();
          }
          else if (this.mCurrentUpdateTriggerTime == 0L)
          {
            long l7 = this.mScheduledTriggerTime;
            if (l7 <= l2)
            {
              if (LOGV)
              {
                StringBuilder localStringBuilder1 = new StringBuilder().append("Starting new update. Last update was at: ");
                long l8 = this.mLastUpdateTriggerTime;
                Date localDate1 = new Date(l8);
                String str1 = localDate1;
                Log.i("MusicLeanback", str1);
              }
              doUpdate();
            }
            else if (LOGV)
            {
              StringBuilder localStringBuilder2 = new StringBuilder().append("Too early to update. Last update: ");
              long l9 = this.mLastUpdateTriggerTime;
              Date localDate2 = new Date(l9);
              StringBuilder localStringBuilder3 = localStringBuilder2.append(localDate2).append(". Will update: ");
              Date localDate3 = new Date(l7);
              String str2 = localDate3;
              Log.i("MusicLeanback", str2);
            }
          }
          else
          {
            DailyWindow localDailyWindow3 = this.mDailyWindow;
            long l10 = this.mCurrentUpdateTriggerTime;
            if (localDailyWindow3.isInSameWindow(l10, l2))
            {
              if (LOGV)
              {
                StringBuilder localStringBuilder4 = new StringBuilder().append("Continue update started at: ");
                long l11 = this.mCurrentUpdateTriggerTime;
                Date localDate4 = new Date(l11);
                String str3 = localDate4;
                Log.i("MusicLeanback", str3);
              }
              doUpdate();
            }
            else
            {
              finishUpdate(false);
              bool2 = false;
            }
          }
        }
      }
    }
  }

  protected List<DownloadRequest> getNextDownloads(ICacheManager paramICacheManager, Collection<Long> paramCollection)
    throws OutOfSpaceException
  {
    ArrayList localArrayList = new ArrayList();
    if (!checkAutoCacheConditions(false))
      if (LOGV)
        Log.i("MusicLeanback", "getNextDownloads: Conditions for autocache not met. Stopping.");
    ContentIdentifier[] arrayOfContentIdentifier1;
    while (true)
    {
      return localArrayList;
      AutoCacheManager localAutoCacheManager = getAutoCacheManager();
      Collection<Long> localCollection = paramCollection;
      arrayOfContentIdentifier1 = localAutoCacheManager.getNextAutoCacheDownloads(1, localCollection);
      if (arrayOfContentIdentifier1 != null)
        break;
      if (LOGV)
        Log.d("MusicLeanback", "getNextDownloads: trackIds=null");
    }
    if (LOGV)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("getNextDownloads: trackIds.length=");
      int i = arrayOfContentIdentifier1.length;
      String str1 = i;
      Log.d("MusicLeanback", str1);
    }
    ContentIdentifier[] arrayOfContentIdentifier2 = arrayOfContentIdentifier1;
    int j = arrayOfContentIdentifier2.length;
    int k = 0;
    while (true)
    {
      int m = k;
      int n = j;
      if (m >= n)
        break;
      ContentIdentifier localContentIdentifier = arrayOfContentIdentifier2[k];
      Object localObject = null;
      try
      {
        Store localStore = Store.getInstance(this);
        long l1 = localContentIdentifier.getId();
        MusicFile localMusicFile = MusicFile.getSummaryMusicFile(localStore, null, l1);
        localObject = localMusicFile;
        if (localObject == null)
          k += 1;
      }
      catch (DataNotFoundException localDataNotFoundException)
      {
        while (true)
        {
          Log.w("MusicLeanback", "Failed to load track data: ", localDataNotFoundException);
          continue;
          FileLocation localFileLocation;
          try
          {
            int i1 = DownloadRequest.Owner.AUTOCACHE.ordinal();
            long l2 = localObject.getSize();
            localFileLocation = paramICacheManager.getTempFileLocation(localContentIdentifier, i1, l2, 2);
            if (localFileLocation != null)
              break label286;
            String str2 = "Failed to get file location.";
            Log.w("MusicLeanback", str2);
            String str3 = "Failed to get file location.";
            throw new OutOfSpaceException(str3);
          }
          catch (RemoteException localRemoteException)
          {
            Log.e("MusicLeanback", "Failed to get temp file location");
          }
          continue;
          label286: String str4 = localObject.getTitle();
          String str5 = localObject.getSourceId();
          int i2 = localObject.getSourceAccount();
          int i3 = DownloadRequest.PRIORITY_AUTOCACHE;
          DownloadRequest.Owner localOwner = DownloadRequest.Owner.AUTOCACHE;
          DownloadRequest localDownloadRequest = new DownloadRequest(localContentIdentifier, str4, str5, i2, i3, localOwner, 0L, false, localFileLocation, null, false);
          boolean bool = localArrayList.add(localDownloadRequest);
        }
      }
    }
  }

  protected long getTotalDownloadSize()
  {
    return getAutoCacheManager().getTotalSizeToAutoCache();
  }

  protected boolean isDownloadingPaused()
  {
    return false;
  }

  protected void notifyAllWorkFinished()
  {
    Log.i("MusicLeanback", "All work finished.");
  }

  protected void notifyDisabled(AbstractSchedulingService.DisableReason paramDisableReason)
  {
    String str = "Disabled: " + paramDisableReason;
    Log.i("MusicLeanback", str);
  }

  protected void notifyDownloadCompleted(DownloadRequest paramDownloadRequest, DownloadProgress paramDownloadProgress)
  {
    if (paramDownloadProgress.isSavable())
    {
      String str = paramDownloadProgress.getHttpContentType();
      long l = paramDownloadProgress.getDownloadByteLength();
      storeInCache(paramDownloadRequest, str, l);
    }
    while (true)
    {
      ContentResolver localContentResolver = getContentResolver();
      Uri localUri = MusicContent.CONTENT_URI;
      localContentResolver.notifyChange(localUri, null, false);
      return;
      deleteFromStorage(paramDownloadRequest);
    }
  }

  protected void notifyDownloadFailed(DownloadRequest paramDownloadRequest, DownloadProgress paramDownloadProgress)
  {
    deleteFromStorage(paramDownloadRequest);
  }

  protected void notifyDownloadProgress(float paramFloat, DownloadProgress paramDownloadProgress)
  {
    if (!LOGV)
      return;
    Object[] arrayOfObject = new Object[3];
    ContentIdentifier localContentIdentifier = paramDownloadProgress.getId();
    arrayOfObject[0] = localContentIdentifier;
    String str1 = paramDownloadProgress.getTrackTitle();
    arrayOfObject[1] = str1;
    Float localFloat = Float.valueOf(paramFloat);
    arrayOfObject[2] = localFloat;
    String str2 = String.format("id=%s, title=%s, progressRatio=%f", arrayOfObject);
    Log.d("MusicLeanback", str2);
  }

  protected void notifyDownloadStarting()
  {
    Log.i("MusicLeanback", "Download starting.");
  }

  public void onCreate()
  {
    super.onCreate();
    MusicPreferences localMusicPreferences = getMusicPreferences();
    SharedPreferences.OnSharedPreferenceChangeListener localOnSharedPreferenceChangeListener = this.mPrefChangeListener;
    localMusicPreferences.registerOnSharedPreferenceChangeListener(localOnSharedPreferenceChangeListener);
  }

  public void onDestroy()
  {
    MusicPreferences localMusicPreferences = getMusicPreferences();
    SharedPreferences.OnSharedPreferenceChangeListener localOnSharedPreferenceChangeListener = this.mPrefChangeListener;
    localMusicPreferences.unregisterOnSharedPreferenceChangeListener(localOnSharedPreferenceChangeListener);
    super.onDestroy();
  }

  public int onStartCommand(final Intent paramIntent, int paramInt1, int paramInt2)
  {
    if ((paramInt1 & 0x1) != 0)
    {
      if (isWakeLockHeld())
      {
        String str1 = "WakeLock is held while processing redelivered intent: " + paramIntent;
        Log.w("MusicLeanback", str1);
      }
      acquireWakeLock(this);
    }
    while (true)
    {
      int i = super.onStartCommand(paramIntent, paramInt1, paramInt2);
      this.mStartId = paramInt2;
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          String str1 = paramIntent.getAction();
          if (AutoCacheSchedulingService.LOGV)
          {
            String str2 = "Service action:" + str1;
            Log.d("MusicLeanback", str2);
          }
          try
          {
            AutoCacheSchedulingService.this.loadState();
            AutoCacheSchedulingService localAutoCacheSchedulingService1 = AutoCacheSchedulingService.this;
            boolean bool1 = "com.google.android.music.leanback.APP_IN_USE".equals(str1);
            boolean bool2 = "com.google.android.music.leanback.CONNECTIVITY_CHANGE".equals(str1);
            boolean bool3 = localAutoCacheSchedulingService1.updateSuggestedMixes(bool1, bool2);
            boolean bool4 = bool3;
            boolean bool5 = AutoCacheSchedulingService.access$300();
            if (bool4)
            {
              AutoCacheSchedulingService localAutoCacheSchedulingService2 = AutoCacheSchedulingService.this;
              boolean bool6 = "com.google.android.music.leanback.POWER_CONNECTED".equals(str1);
              if (localAutoCacheSchedulingService2.checkAutoCacheConditions(bool6))
              {
                AutoCacheSchedulingService.this.startAutoCaching();
                return;
              }
            }
          }
          finally
          {
            boolean bool7 = AutoCacheSchedulingService.access$300();
          }
          Log.i("MusicLeanback", "Conditions not met for autocaching.");
          AutoCacheSchedulingService.this.stopAutoCaching();
        }
      };
      if (!postRunnable(local1))
      {
        String str2 = "postRunnable failed when processing intent: " + paramIntent;
        Log.w("MusicLeanback", str2);
        boolean bool = releaseWakeLock();
      }
      return 3;
      if (!isWakeLockHeld())
      {
        String str3 = "Wakelock is not held while processing Intent: " + paramIntent;
        Log.wtf("MusicLeanback", str3);
        acquireWakeLock(this);
      }
    }
  }

  void resetState()
  {
    this.mLastUpdateTriggerTime = 0L;
    this.mCurrentUpdateTriggerTime = 0L;
    this.mScheduledTriggerTime = 0L;
    this.mCurrentUpdateRefreshedMixes = false;
    this.mInitializedMixes = false;
    this.mSourceAccount = 0;
    this.mAppUseTime = 0L;
    this.mLastAttemptTime = 0L;
    this.mConnectivityRecoveryTime = 0L;
    saveState();
  }

  public static class ActionReceiver extends BroadcastReceiver
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (AutoCacheSchedulingService.LOGV)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("ActionReceiver action:");
        String str1 = paramIntent.getAction();
        String str2 = str1;
        Log.i("MusicLeanback", str2);
      }
      AutoCacheSchedulingService.startService(paramContext, paramIntent);
    }
  }

  public static class EnablingReceiver extends BroadcastReceiver
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (AutoCacheSchedulingService.LOGV)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("EnablingReceiver action:");
        String str1 = paramIntent.getAction();
        String str2 = str1;
        Log.i("MusicLeanback", str2);
      }
      AutoCacheSchedulingService.startService(paramContext, paramIntent);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.leanback.AutoCacheSchedulingService
 * JD-Core Version:    0.6.2
 */