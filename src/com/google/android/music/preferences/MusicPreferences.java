package com.google.android.music.preferences;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.google.android.gsf.Gservices;
import com.google.android.music.NautilusStatus;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.log.Log;
import com.google.android.music.store.MusicContent;
import com.google.android.music.utils.ConfigUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.SafeServiceConnection;
import com.google.android.music.utils.async.AsyncWorkers;
import com.google.common.collect.Lists;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MusicPreferences
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.PREFERENCES);
  private static final int MSG_REFRESH_STREAMING_ENABLED = AsyncWorkers.getUniqueMessageType(AsyncWorkers.sBackendServiceWorker);
  private static MusicPreferences sMusicPreferences = null;
  private static LinkedList<WeakReference<Object>> sPreferenceReferences = Lists.newLinkedList();
  private Map<String, Object> mCachedPreferences;
  private final Context mContext;
  private final boolean mIsLargeScreen;
  private final boolean mIsTv;
  private final boolean mIsVoiceCapable;
  private final boolean mIsXLargeScreen;
  private IPreferenceChangeListener mPreferenceChangeListener;
  private List<SharedPreferences.OnSharedPreferenceChangeListener> mPreferenceChangeListeners;
  private IPreferenceService mPreferenceService;
  private boolean mPreferenceServiceBound;
  private boolean mPreferenceServiceConnected;
  private SafeServiceConnection mPreferenceServiceSafeConnection;
  private final Runnable mRefreshStreamingEnabledRunnable;
  private final List<Runnable> mRunOnceServiceConnected;

  private MusicPreferences(Context paramContext)
  {
    LinkedList localLinkedList1 = Lists.newLinkedList();
    this.mPreferenceChangeListeners = localLinkedList1;
    LinkedList localLinkedList2 = Lists.newLinkedList();
    this.mRunOnceServiceConnected = localLinkedList2;
    this.mPreferenceServiceBound = false;
    this.mPreferenceServiceConnected = false;
    SafeServiceConnection local1 = new SafeServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        MusicPreferences.this.onServiceConnectedImp(paramAnonymousComponentName, paramAnonymousIBinder);
      }

      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
        MusicPreferences.this.onServiceDisconnectedImp(paramAnonymousComponentName);
      }
    };
    this.mPreferenceServiceSafeConnection = local1;
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        MusicPreferences.this.refreshStreamingEnabledSync();
      }
    };
    this.mRefreshStreamingEnabledRunnable = local2;
    IPreferenceChangeListener.Stub local5 = new IPreferenceChangeListener.Stub()
    {
      public void onBooleanChanged(String paramAnonymousString, boolean paramAnonymousBoolean)
        throws RemoteException
      {
        synchronized (MusicPreferences.this)
        {
          Map localMap = MusicPreferences.this.mCachedPreferences;
          Boolean localBoolean = Boolean.valueOf(paramAnonymousBoolean);
          Object localObject1 = localMap.put(paramAnonymousString, localBoolean);
          MusicPreferences.this.notifyPreferenceChangeListeners(paramAnonymousString);
          return;
        }
      }

      public void onIntChanged(String paramAnonymousString, int paramAnonymousInt)
        throws RemoteException
      {
        synchronized (MusicPreferences.this)
        {
          Map localMap = MusicPreferences.this.mCachedPreferences;
          Integer localInteger = Integer.valueOf(paramAnonymousInt);
          Object localObject1 = localMap.put(paramAnonymousString, localInteger);
          MusicPreferences.this.notifyPreferenceChangeListeners(paramAnonymousString);
          return;
        }
      }

      public void onLongChanged(String paramAnonymousString, long paramAnonymousLong)
        throws RemoteException
      {
        synchronized (MusicPreferences.this)
        {
          Map localMap = MusicPreferences.this.mCachedPreferences;
          Long localLong = Long.valueOf(paramAnonymousLong);
          Object localObject1 = localMap.put(paramAnonymousString, localLong);
          MusicPreferences.this.notifyPreferenceChangeListeners(paramAnonymousString);
          return;
        }
      }

      public void onPreferenceRemoved(String paramAnonymousString)
        throws RemoteException
      {
        synchronized (MusicPreferences.this)
        {
          Object localObject1 = MusicPreferences.this.mCachedPreferences.remove(paramAnonymousString);
          MusicPreferences.this.notifyPreferenceChangeListeners(paramAnonymousString);
          return;
        }
      }

      public void onStringChanged(String paramAnonymousString1, String paramAnonymousString2)
        throws RemoteException
      {
        synchronized (MusicPreferences.this)
        {
          Object localObject1 = MusicPreferences.this.mCachedPreferences.put(paramAnonymousString1, paramAnonymousString2);
          MusicPreferences.this.notifyPreferenceChangeListeners(paramAnonymousString1);
          return;
        }
      }
    };
    this.mPreferenceChangeListener = local5;
    Context localContext = paramContext.getApplicationContext();
    this.mContext = localContext;
    boolean bool1 = getIsXLargeScreen();
    this.mIsXLargeScreen = bool1;
    boolean bool2 = getIsLargeScreen();
    this.mIsLargeScreen = bool2;
    boolean bool3 = getIsVoiceCapable();
    this.mIsVoiceCapable = bool3;
    boolean bool4 = getIsTv();
    this.mIsTv = bool4;
    Map localMap = this.mContext.getSharedPreferences("MusicPreferences", 0).getAll();
    this.mCachedPreferences = localMap;
  }

  /** @deprecated */
  private void bindToPreferenceService()
  {
    while (true)
    {
      try
      {
        bool = this.mPreferenceServiceBound;
        if (bool)
          return;
        SafeServiceConnection localSafeServiceConnection = this.mPreferenceServiceSafeConnection;
        Context localContext = this.mContext;
        Intent localIntent = new Intent("com.google.android.music.PREFERENCE_SERVICE");
        if (!localSafeServiceConnection.bindService(localContext, localIntent, 1))
          throw new RuntimeException("Could not connect to the preference service");
      }
      finally
      {
      }
      boolean bool = true;
      this.mPreferenceServiceBound = bool;
    }
  }

  private boolean getBooleanPreference(String paramString, boolean paramBoolean)
  {
    if (this.mCachedPreferences.containsKey(paramString))
      paramBoolean = ((Boolean)this.mCachedPreferences.get(paramString)).booleanValue();
    return paramBoolean;
  }

  private int getIntPreference(String paramString, int paramInt)
  {
    if (this.mCachedPreferences.containsKey(paramString))
      paramInt = ((Integer)this.mCachedPreferences.get(paramString)).intValue();
    return paramInt;
  }

  private boolean getIsLargeScreen()
  {
    if ((this.mContext.getResources().getConfiguration().screenLayout & 0x3) == 3);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private boolean getIsTv()
  {
    FeatureInfo[] arrayOfFeatureInfo1 = this.mContext.getPackageManager().getSystemAvailableFeatures();
    int j;
    if (arrayOfFeatureInfo1 != null)
    {
      FeatureInfo[] arrayOfFeatureInfo2 = arrayOfFeatureInfo1;
      int i = arrayOfFeatureInfo2.length;
      j = 0;
      if (j < i)
      {
        FeatureInfo localFeatureInfo = arrayOfFeatureInfo2[j];
        if (localFeatureInfo.name != null)
        {
          String str1 = localFeatureInfo.name;
          if (!"com.google.android.tv".equals(str1))
          {
            String str2 = localFeatureInfo.name;
            if (!"android.hardware.type.television".equals(str2));
          }
          else if (LOGV)
          {
            Log.d("MusicPreferences", "Found TV feature, using TV experience.");
          }
        }
      }
    }
    for (boolean bool = true; ; bool = false)
    {
      return bool;
      j += 1;
      break;
    }
  }

  private boolean getIsVoiceCapable()
  {
    int i = 0;
    TelephonyManager localTelephonyManager = (TelephonyManager)this.mContext.getSystemService("phone");
    boolean bool1;
    if (localTelephonyManager == null)
      bool1 = false;
    while (true)
    {
      return bool1;
      i = 0;
      try
      {
        Class[] arrayOfClass = new Class[i];
        Method localMethod = TelephonyManager.class.getMethod("isVoiceCapable", arrayOfClass);
        Object[] arrayOfObject = new Object[0];
        boolean bool2 = ((Boolean)localMethod.invoke(localTelephonyManager, arrayOfObject)).booleanValue();
        bool1 = bool2;
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        bool1 = true;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        break label75;
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        label75: break label75;
      }
    }
  }

  private boolean getIsXLargeScreen()
  {
    boolean bool = false;
    try
    {
      i = Configuration.class.getField("SCREENLAYOUT_SIZE_XLARGE");
      Configuration localConfiguration1 = this.mContext.getResources().getConfiguration();
      localConfiguration2 = localConfiguration1;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      try
      {
        int i;
        Configuration localConfiguration2;
        int j = i.getInt(localConfiguration2) & 0xF;
        int k = localConfiguration2.screenLayout;
        int m = k & 0xF;
        if (j != m)
          bool = true;
        while (true)
        {
          label57: return bool;
          localNoSuchFieldException = localNoSuchFieldException;
        }
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        break label57;
      }
    }
  }

  private int getLastUpgradeSyncVersion()
  {
    return getIntPreference("lastUpgradeSyncVersion", -1);
  }

  private long getLongPreference(String paramString, long paramLong)
  {
    if (this.mCachedPreferences.containsKey(paramString))
      paramLong = ((Long)this.mCachedPreferences.get(paramString)).longValue();
    return paramLong;
  }

  public static MusicPreferences getMusicPreferences(Context paramContext, Object paramObject)
  {
    synchronized (sPreferenceReferences)
    {
      boolean bool1 = sPreferenceReferences.isEmpty();
      LinkedList localLinkedList2 = sPreferenceReferences;
      WeakReference localWeakReference = new WeakReference(paramObject);
      boolean bool2 = localLinkedList2.add(localWeakReference);
      if (sMusicPreferences == null)
      {
        Context localContext = paramContext.getApplicationContext();
        sMusicPreferences = new MusicPreferences(localContext);
      }
      if (bool1)
        sMusicPreferences.bindToPreferenceService();
      MusicPreferences localMusicPreferences = sMusicPreferences;
      return localMusicPreferences;
    }
  }

  private String getStringPreference(String paramString1, String paramString2)
  {
    if (this.mCachedPreferences.containsKey(paramString1));
    for (String str = (String)this.mCachedPreferences.get(paramString1); ; str = paramString2)
      return str;
  }

  /** @deprecated */
  private boolean hasPreferenceService()
  {
    try
    {
      IPreferenceService localIPreferenceService = this.mPreferenceService;
      if (localIPreferenceService != null)
      {
        bool = true;
        return bool;
      }
      boolean bool = false;
    }
    finally
    {
    }
  }

  private boolean isConfiguredSyncAccount()
  {
    if (getStringPreference("SelectedAccount", null) != null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static boolean isGingerbreadOrGreater()
  {
    if (Build.VERSION.SDK_INT >= 9);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static boolean isGlass()
  {
    return Build.DEVICE.equals("glass-1");
  }

  public static boolean isHoneycombOrGreater()
  {
    if (Build.VERSION.SDK_INT >= 11);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static boolean isICSOrGreater()
  {
    if (Build.VERSION.SDK_INT >= 14);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static boolean isJellyBeanMR2OrGreater()
  {
    if (Build.VERSION.SDK_INT >= 18);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static boolean isJellyBeanOrGreater()
  {
    if (Build.VERSION.SDK_INT >= 16);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static boolean isPreGingerbread()
  {
    if (Build.VERSION.SDK_INT <= 8);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private void logValueRemoved(String paramString)
  {
    if (!LOGV)
      return;
    String str = "Removed pref key: " + paramString;
    Log.d("MusicPreferences", str);
  }

  private void logValueSet(String paramString, Object paramObject)
  {
    if (!LOGV)
      return;
    String str = "Set pref key " + paramString + "=" + paramObject;
    Log.d("MusicPreferences", str);
  }

  private void notifyPreferenceChangeListeners(String paramString)
  {
    synchronized (this.mPreferenceChangeListeners)
    {
      Iterator localIterator = this.mPreferenceChangeListeners.iterator();
      if (localIterator.hasNext())
        ((SharedPreferences.OnSharedPreferenceChangeListener)localIterator.next()).onSharedPreferenceChanged(null, paramString);
    }
  }

  // ERROR //
  private void onServiceConnectedImp(ComponentName paramComponentName, IBinder paramIBinder)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_2
    //   3: invokestatic 433	com/google/android/music/preferences/IPreferenceService$Stub:asInterface	(Landroid/os/IBinder;)Lcom/google/android/music/preferences/IPreferenceService;
    //   6: astore_3
    //   7: aload_0
    //   8: aload_3
    //   9: putfield 182	com/google/android/music/preferences/MusicPreferences:mPreferenceService	Lcom/google/android/music/preferences/IPreferenceService;
    //   12: aload_0
    //   13: iconst_1
    //   14: putfield 101	com/google/android/music/preferences/MusicPreferences:mPreferenceServiceConnected	Z
    //   17: aload_0
    //   18: invokevirtual 436	java/lang/Object:notifyAll	()V
    //   21: aload_0
    //   22: getfield 182	com/google/android/music/preferences/MusicPreferences:mPreferenceService	Lcom/google/android/music/preferences/IPreferenceService;
    //   25: astore 4
    //   27: aload_0
    //   28: getfield 112	com/google/android/music/preferences/MusicPreferences:mPreferenceChangeListener	Lcom/google/android/music/preferences/IPreferenceChangeListener;
    //   31: astore 5
    //   33: aload 4
    //   35: aload 5
    //   37: invokeinterface 442 2 0
    //   42: aload_0
    //   43: getfield 182	com/google/android/music/preferences/MusicPreferences:mPreferenceService	Lcom/google/android/music/preferences/IPreferenceService;
    //   46: invokeinterface 445 1 0
    //   51: invokestatic 451	com/google/common/collect/Maps:newLinkedHashMap	(Ljava/util/Map;)Ljava/util/LinkedHashMap;
    //   54: astore 6
    //   56: aload_0
    //   57: aload 6
    //   59: putfield 155	com/google/android/music/preferences/MusicPreferences:mCachedPreferences	Ljava/util/Map;
    //   62: aload_0
    //   63: monitorexit
    //   64: aload_0
    //   65: invokespecial 454	com/google/android/music/preferences/MusicPreferences:refreshStreamingEnabled	()V
    //   68: aload_0
    //   69: invokevirtual 457	com/google/android/music/preferences/MusicPreferences:clearSyncForUnselectedAccounts	()V
    //   72: aload_0
    //   73: invokespecial 460	com/google/android/music/preferences/MusicPreferences:runSavedServiceTasks	()V
    //   76: aload_0
    //   77: monitorenter
    //   78: aload_0
    //   79: invokevirtual 436	java/lang/Object:notifyAll	()V
    //   82: aload_0
    //   83: monitorexit
    //   84: aload_0
    //   85: aconst_null
    //   86: invokespecial 190	com/google/android/music/preferences/MusicPreferences:notifyPreferenceChangeListeners	(Ljava/lang/String;)V
    //   89: return
    //   90: astore 7
    //   92: ldc 143
    //   94: ldc_w 462
    //   97: invokestatic 465	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;)V
    //   100: aload_0
    //   101: monitorexit
    //   102: return
    //   103: astore 8
    //   105: aload_0
    //   106: monitorexit
    //   107: aload 8
    //   109: athrow
    //   110: astore 9
    //   112: aload_0
    //   113: monitorexit
    //   114: aload 9
    //   116: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   21	62	90	android/os/RemoteException
    //   2	21	103	finally
    //   21	62	103	finally
    //   62	64	103	finally
    //   92	103	103	finally
    //   78	84	110	finally
    //   112	114	110	finally
  }

  /** @deprecated */
  private void onServiceDisconnectedImp(ComponentName paramComponentName)
  {
    try
    {
      this.mPreferenceService = null;
      this.mPreferenceServiceConnected = false;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private void refreshNetworkUsageComponent()
  {
    if (isConfiguredSyncAccount());
    for (int i = 1; ; i = 2)
    {
      PackageManager localPackageManager = this.mContext.getPackageManager();
      Context localContext = this.mContext;
      ComponentName localComponentName = new ComponentName(localContext, "com.google.android.music.Settings.NetworkUsage");
      localPackageManager.setComponentEnabledSetting(localComponentName, i, 1);
      return;
    }
  }

  private void refreshStreamingEnabled()
  {
    if (!MusicUtils.isMainProcess(this.mContext))
      return;
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    int i = MSG_REFRESH_STREAMING_ENABLED;
    Runnable localRunnable = this.mRefreshStreamingEnabledRunnable;
    AsyncWorkers.runAsync(localLoggableHandler, i, localRunnable, true);
  }

  private void refreshStreamingEnabledSync()
  {
    Account localAccount;
    if (!upgradeLegacyInvalidAccount())
    {
      localAccount = getSyncAccount();
      if (localAccount != null)
        break label35;
      setStreamingAccount(null);
    }
    while (true)
    {
      refreshNetworkUsageComponent();
      notifyPreferenceChangeListeners(null);
      maybeEnableUpgradeSync();
      return;
      label35: if (LOGV)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Re-setting music sync state for account ");
        String str1 = localAccount.name;
        String str2 = str1;
        Log.d("MusicPreferences", str2);
      }
      ContentResolver.setIsSyncable(localAccount, "com.google.android.music.MusicContent", 1);
    }
  }

  public static void releaseMusicPreferences(Object paramObject)
  {
    while (true)
    {
      Object localObject1;
      synchronized (sPreferenceReferences)
      {
        Iterator localIterator = sPreferenceReferences.iterator();
        localObject1 = null;
        if (localIterator.hasNext())
        {
          localObject2 = ((WeakReference)localIterator.next()).get();
          if ((localObject2 != null) && (localObject2 != paramObject))
            break label150;
          if (localObject2 == null)
            break label144;
          localObject2 = null;
          localIterator.remove();
          localObject1 = localObject2;
          continue;
        }
        if (localObject1 == null)
        {
          String str = "Could not find reference holding on to MusicPreferences: " + paramObject;
          Throwable localThrowable = new Throwable();
          Log.w("MusicPreferences", str, localThrowable);
        }
        if ((sPreferenceReferences.isEmpty()) && (sMusicPreferences != null))
          sMusicPreferences.unbindFromPreferenceService();
        return;
      }
      label144: Object localObject2 = localObject1;
      continue;
      label150: localObject2 = localObject1;
    }
  }

  /** @deprecated */
  private void runSavedServiceTasks()
  {
    try
    {
      Iterator localIterator = this.mRunOnceServiceConnected.iterator();
      while (localIterator.hasNext())
      {
        Runnable localRunnable = (Runnable)localIterator.next();
        AsyncWorkers.runAsync(AsyncWorkers.sBackendServiceWorker, localRunnable);
      }
    }
    finally
    {
    }
    this.mRunOnceServiceConnected.clear();
  }

  private void setPreference(String paramString, Object paramObject)
  {
    changeValue(paramString, paramObject);
    AsyncPreferenceUpdate localAsyncPreferenceUpdate = new AsyncPreferenceUpdate(paramString, paramObject);
    runWithPreferenceService(localAsyncPreferenceUpdate);
  }

  private void setPreferenceWithExtraTask(String paramString, Object paramObject, Runnable paramRunnable)
  {
    changeValue(paramString, paramObject);
    AsyncPreferenceUpdate localAsyncPreferenceUpdate = new AsyncPreferenceUpdate(paramString, paramObject, paramRunnable);
    runWithPreferenceService(localAsyncPreferenceUpdate);
  }

  private void setStreamingAccount(Account paramAccount, boolean paramBoolean)
  {
    StringBuilder localStringBuilder1 = null;
    if (paramAccount != null)
    {
      String str1 = paramAccount.type;
      if (!"com.google".equals(str1))
      {
        StringBuilder localStringBuilder2 = new StringBuilder().append("Invalid account type: ");
        String str2 = paramAccount.type;
        String str3 = str2;
        throw new IllegalArgumentException(str3);
      }
    }
    setIsValidAccount(paramBoolean);
    Account localAccount = getSyncAccount();
    if ((localAccount != null) && (localAccount.equals(paramAccount)))
      return;
    if ((localAccount == null) && (paramAccount == null))
      return;
    clearTempNautilusStatus();
    setShowSyncNotification(true);
    String str4 = "SelectedAccount";
    Object localObject;
    String str5;
    if (paramAccount == null)
    {
      localObject = null;
      setPreference(str4, localObject);
      if (localAccount != null)
      {
        ContentResolver.cancelSync(localAccount, "com.google.android.music.MusicContent");
        ContentResolver.setSyncAutomatically(localAccount, "com.google.android.music.MusicContent", false);
        ContentResolver.setIsSyncable(localAccount, "com.google.android.music.MusicContent", 0);
      }
      if (LOGV)
      {
        str5 = "MusicPreferences";
        localStringBuilder1 = new StringBuilder().append("New Active account: ");
        if (paramAccount != null)
          break label234;
      }
    }
    label234: for (String str6 = "null"; ; str6 = paramAccount.name)
    {
      String str7 = str6;
      Log.d(str5, str7);
      switchAccountAsync(localAccount, paramAccount);
      refreshNetworkUsageComponent();
      return;
      localObject = paramAccount.name;
      break;
    }
  }

  private void switchAccountAsync(final Account paramAccount1, final Account paramAccount2)
  {
    MusicUtils.runAsync(new Runnable()
    {
      public void run()
      {
        Log.i("MusicPreferences", "Account switch: clearing database and downloaded content");
        MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(MusicPreferences.this.mContext);
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = "switchAccounts";
        arrayOfObject[1] = "Account switch: clearing database and downloaded content";
        localMusicEventLogger.trackEvent("switchAccounts", arrayOfObject);
        MusicContent.deleteAllRemoteContent(MusicPreferences.this.mContext.getContentResolver());
        ConfigUtils.deleteAllServerSettings();
        Intent localIntent1 = new Intent("com.google.android.music.accountchanged");
        if (paramAccount2 != null)
        {
          Account localAccount1 = paramAccount2;
          Intent localIntent2 = localIntent1.putExtra("com.google.android.music.newaccount", localAccount1);
        }
        if (paramAccount1 != null)
        {
          Account localAccount2 = paramAccount1;
          Intent localIntent3 = localIntent1.putExtra("com.google.android.music.oldaccount", localAccount2);
        }
        MusicPreferences.this.mContext.sendBroadcast(localIntent1);
        if (paramAccount2 == null)
          return;
        ContentResolver.setIsSyncable(paramAccount2, "com.google.android.music.MusicContent", 1);
        ContentResolver.setSyncAutomatically(paramAccount2, "com.google.android.music.MusicContent", true);
        Account localAccount3 = paramAccount2;
        Bundle localBundle = new Bundle();
        ContentResolver.requestSync(localAccount3, "com.google.android.music.MusicContent", localBundle);
      }
    });
  }

  /** @deprecated */
  private void unbindFromPreferenceService()
  {
    try
    {
      boolean bool = this.mPreferenceServiceBound;
      if (!bool);
      while (true)
      {
        return;
        SafeServiceConnection localSafeServiceConnection = this.mPreferenceServiceSafeConnection;
        Context localContext = this.mContext;
        localSafeServiceConnection.unbindService(localContext);
        this.mPreferenceServiceBound = false;
        this.mPreferenceServiceConnected = false;
        notifyAll();
      }
    }
    finally
    {
    }
  }

  private boolean upgradeLegacyInvalidAccount()
  {
    boolean bool1 = false;
    boolean bool2 = false;
    String str1 = getStringPreference("InvalidSelectedAccount", null);
    Account localAccount1;
    if (str1 != null)
    {
      String str2 = "Attempting to upgrade legacy account " + str1;
      Log.i("MusicPreferences", str2);
      setPreference("InvalidSelectedAccount", null);
      if (TextUtils.isEmpty(str1))
      {
        Log.w("MusicPreferences", "Skipping upgrade of empty of legacy invalid account");
        return bool1;
      }
      localAccount1 = getSyncAccount();
      if (localAccount1 == null)
        break label226;
      String str3 = localAccount1.name;
      if (!str1.equals(str3))
        break label183;
      String str4 = "Skipping upgrade of legacy invalid account " + str1 + ". It's aleady selected";
      Log.w("MusicPreferences", str4);
      label133: if (!bool2)
        break label281;
    }
    label281: for (String str5 = "invalidAccountUpgradeSuccess"; ; str5 = "invalidAccountUpgradeFailure")
    {
      MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this.mContext);
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = "invalidAccount";
      arrayOfObject[1] = str1;
      localMusicEventLogger.trackEvent(str5, arrayOfObject);
      bool1 = bool2;
      break;
      label183: String str6 = "Skipping upgrade of legacy invalid account " + str1 + ". Another account is aleady selected " + localAccount1;
      Log.w("MusicPreferences", str6);
      break label133;
      label226: Account localAccount2 = resolveAccount(str1);
      if (localAccount2 == null)
      {
        String str7 = "Failed to resolve legacy invalid account " + str1;
        Log.w("MusicPreferences", str7);
        break label133;
      }
      setInvalidStreamingAccount(localAccount2);
      bool2 = true;
      break label133;
    }
  }

  public static boolean usingNewDownloadUI(Context paramContext)
  {
    return Gservices.getBoolean(paramContext.getContentResolver(), "music_using_new_download_ui", true);
  }

  /** @deprecated */
  private boolean waitForServiceConnection()
  {
    try
    {
      Looper localLooper1 = Looper.getMainLooper();
      Looper localLooper2 = Looper.myLooper();
      if (localLooper1 == localLooper2)
        throw new RuntimeException("waitForServiceConnection() was called on the main thread");
    }
    finally
    {
    }
    while (this.mPreferenceServiceBound)
    {
      bool = this.mPreferenceServiceConnected;
      if (bool)
        break;
      try
      {
        wait();
      }
      catch (InterruptedException localInterruptedException)
      {
      }
    }
    boolean bool = this.mPreferenceServiceConnected;
    return bool;
  }

  public boolean autoSelectStreamingAccount(Account paramAccount)
  {
    boolean bool = false;
    if (isConfiguredSyncAccount());
    while (true)
    {
      return bool;
      if (isAccountAutoSelectEnabled())
      {
        setStreamingAccount(paramAccount);
        MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this.mContext);
        Object[] arrayOfObject = new Object[0];
        localMusicEventLogger.trackEvent("signUpAccountAutoSelect", arrayOfObject);
        Log.i("MusicPreferences", "Account auto-selected");
        bool = true;
      }
    }
  }

  public void changeValue(String paramString, Object paramObject)
  {
    if (paramObject == null)
    {
      Object localObject1 = this.mCachedPreferences.remove(paramString);
      logValueRemoved(paramString);
      return;
    }
    Object localObject2 = this.mCachedPreferences.put(paramString, paramObject);
    logValueSet(paramString, paramObject);
  }

  public void clearSyncForUnselectedAccounts()
  {
    Account[] arrayOfAccount1 = AccountManager.get(this.mContext).getAccountsByType("com.google");
    Account localAccount1 = getSyncAccount();
    Account[] arrayOfAccount2 = arrayOfAccount1;
    int i = arrayOfAccount2.length;
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      Account localAccount2 = arrayOfAccount2[j];
      if (!localAccount2.equals(localAccount1))
      {
        ContentResolver.cancelSync(localAccount2, "com.google.android.music.MusicContent");
        ContentResolver.setSyncAutomatically(localAccount2, "com.google.android.music.MusicContent", false);
        ContentResolver.setIsSyncable(localAccount2, "com.google.android.music.MusicContent", 0);
      }
      j += 1;
    }
  }

  public void clearTempNautilusStatus()
  {
    Long localLong = Long.valueOf(0L);
    setPreference("TempNautilusStatusTimestamp", localLong);
    Integer localInteger = Integer.valueOf(-1);
    setPreference("TempNautilusStatus", localInteger);
  }

  public void disableAutoSelect()
  {
    Boolean localBoolean = Boolean.valueOf(false);
    setPreference("AccountAutoSelectEnabled", localBoolean);
  }

  public int getAllSongsSortOrder()
  {
    return getIntPreference("AllSongsSortOrder", 1);
  }

  public int getArtistSongsSortOrder()
  {
    return getIntPreference("ArtistSongsSortOrder", 1);
  }

  public int getAutoCacheDurationInMinutes()
  {
    return getIntPreference("AutoCacheDurationInMinutes", 300);
  }

  public int getAutoCacheMinutesSinceMidnight()
  {
    return getIntPreference("AutoCacheMinutesSinceMidnight", 60);
  }

  public boolean getClearNautilusContent()
  {
    return getBooleanPreference("clearNautilusContent", false);
  }

  public int getContentFilter()
  {
    return getIntPreference("ContentFilter", 1);
  }

  public int getDatabaseVersion()
  {
    return getIntPreference("databaseVersion", -1);
  }

  public int getDisplayOptions()
  {
    int i = 0;
    if (!isOnDeviceMusicCapable());
    while (true)
    {
      return i;
      i = getIntPreference("DisplayOptions", 0);
    }
  }

  public String getLoggingId()
  {
    String str = getStringPreference("StringNonNegativeLoggingId", null);
    if (str == null)
    {
      str = Long.toHexString(Math.abs(new SecureRandom().nextLong()));
      setPreference("StringNonNegativeLoggingId", str);
    }
    return str;
  }

  public NautilusStatus getNautilusStatus()
  {
    long l1 = getLongPreference("TempNautilusStatusTimestamp", 0L);
    int i;
    NautilusStatus[] arrayOfNautilusStatus;
    if (l1 > 0L)
    {
      i = getIntPreference("TempNautilusStatus", -1);
      if (i > -1)
      {
        arrayOfNautilusStatus = NautilusStatus.values();
        int j = arrayOfNautilusStatus.length;
        if (i < j)
        {
          long l2 = Gservices.getLong(this.mContext.getContentResolver(), "music_temp_nautilus_status_exp_time_in_seconds", 86400L);
          if (l2 > 0L)
          {
            long l3 = 1000L * l2 + l1;
            long l4 = System.currentTimeMillis();
            if (l3 <= l4);
          }
        }
      }
    }
    for (NautilusStatus localNautilusStatus = arrayOfNautilusStatus[i]; ; localNautilusStatus = ConfigUtils.getNautilusStatus())
    {
      return localNautilusStatus;
      clearTempNautilusStatus();
    }
  }

  public int getRecentlyAddedSongsSortOrder()
  {
    return getIntPreference("RecentlyAddedSongsSortOrder", 4);
  }

  public boolean getResetSyncState()
  {
    return getBooleanPreference("resetSyncState", false);
  }

  public String getSecondaryExternalStorageMountPoint()
  {
    return getStringPreference("secondaryExternalMountPoint", null);
  }

  public String getSeletectedAccountForDisplay()
  {
    Account localAccount = getSyncAccount();
    if (localAccount != null);
    for (String str = localAccount.name; ; str = getStringPreference("InvalidSelectedAccount", null))
      return str;
  }

  public boolean getShowSyncNotification()
  {
    boolean bool = false;
    if (Gservices.getBoolean(this.mContext.getContentResolver(), "music_enable_sync_notifications", bool))
      bool = getBooleanPreference("showSyncNotification", true);
    return bool;
  }

  public boolean getStoreAvailable()
  {
    return getBooleanPreference("storeAvailable", false);
  }

  public long getStoreAvailableLastChecked()
  {
    return getLongPreference("storeAvailableLastChecked", 0L);
  }

  public int getStoreSongsSortOrder()
  {
    return getIntPreference("StoreSongsSortOrder", 4);
  }

  public int getStreamQuality()
  {
    if (isTvMusic());
    for (int i = 2; ; i = getIntPreference("StreamQuality", 1))
      return i;
  }

  public Account getStreamingAccount()
  {
    if (!isValidAccount());
    for (Account localAccount = null; ; localAccount = getSyncAccount())
      return localAccount;
  }

  public Account getSyncAccount()
  {
    Account localAccount = null;
    if (isConfiguredSyncAccount())
    {
      String str = getStringPreference("SelectedAccount", null);
      localAccount = resolveAccount(str);
    }
    return localAccount;
  }

  public int getThumbsUpSongsSortOrder()
  {
    return getIntPreference("ThumbsUpSongsSortOrder", 1);
  }

  public boolean getUpgradeSyncDone()
  {
    return getBooleanPreference("upgradeSyncDone", false);
  }

  public boolean hasStreamingAccount()
  {
    if (getStreamingAccount() != null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isAccountAutoSelectEnabled()
  {
    return getBooleanPreference("AccountAutoSelectEnabled", true);
  }

  public boolean isAutoCachingAvailable()
  {
    return Gservices.getBoolean(this.mContext.getContentResolver(), "music_enable_autocache", true);
  }

  public boolean isAutoCachingEnabled()
  {
    boolean bool = false;
    if (!isAutoCachingAvailable());
    while (true)
    {
      return bool;
      if (isCachedStreamingMusicEnabled())
        if (this.mCachedPreferences.containsKey("AutoCacheOn"))
          bool = ((Boolean)this.mCachedPreferences.get("AutoCacheOn")).booleanValue();
        else if (isNautilusEnabled())
          bool = Gservices.getBoolean(this.mContext.getContentResolver(), "music_autocache_opt_in_default_nautilus", true);
        else
          bool = Gservices.getBoolean(this.mContext.getContentResolver(), "music_autocache_opt_in_default", false);
    }
  }

  public boolean isCachedStreamingMusicEnabled()
  {
    boolean bool = true;
    if ((isCachingFeatureAvailable()) && (getBooleanPreference("CacheStreamed", true)));
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  public boolean isCachingFeatureAvailable()
  {
    if (!isTvMusic());
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isFindVideoEnabled()
  {
    boolean bool = false;
    if ((isNautilusEnabled()) && (Gservices.getBoolean(this.mContext.getApplicationContext().getContentResolver(), "music_enable_find_video", false)))
      bool = true;
    return bool;
  }

  public boolean isHighStreamQuality()
  {
    if (getStreamQuality() == 2);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isKeepOnDownloadingPaused()
  {
    return getBooleanPreference("isKeepOnDownloadPaused", false);
  }

  public boolean isLargeScreen()
  {
    return this.mIsLargeScreen;
  }

  public boolean isLogFilesEnabled()
  {
    return getBooleanPreference("LogFilesEnabled", false);
  }

  public boolean isLowStreamQuality()
  {
    if (getStreamQuality() == 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isMediaRouteSupportEnabled()
  {
    boolean bool1 = true;
    boolean bool2 = Gservices.getBoolean(this.mContext.getContentResolver(), "music_enable_cast_remote_playback", bool1);
    if ((!this.mIsTv) && (bool2));
    while (true)
    {
      return bool1;
      bool1 = false;
    }
  }

  public boolean isNautilusEnabled()
  {
    return getNautilusStatus().isNautilusEnabled();
  }

  public boolean isNautilusStatusStale()
  {
    boolean bool = false;
    long l1 = System.currentTimeMillis();
    long l2 = getLongPreference("NautilusTimestampMillisec", 0L);
    if (l2 != 0L)
    {
      long l3 = l1 + 86400000L;
      if (l2 <= l3);
    }
    else
    {
      updateNautilusTimestamp();
    }
    while (true)
    {
      return bool;
      long l4 = Gservices.getInt(this.mContext.getContentResolver(), "music_nautlus_offline_status_ttl_days", 30);
      long l5 = 86400000L * l4 + l2;
      if (l1 >= l5)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Stale nautilus state detected. Last updated: ");
        Date localDate = new Date(l2);
        String str = localDate + ". Exp: " + l4 + " days.";
        Log.e("MusicPreferences", str);
        bool = true;
      }
    }
  }

  public boolean isNormalStreamQuality()
  {
    int i = 1;
    if (getStreamQuality() != i);
    while (true)
    {
      return i;
      int j = 0;
    }
  }

  public boolean isOfflineDLOnlyOnWifi()
  {
    return getBooleanPreference("OfflineDLOnlyOnWifi", true);
  }

  public boolean isOfflineFeatureAvailable()
  {
    if ((hasStreamingAccount()) && (!isTvMusic()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isOnDeviceMusicCapable()
  {
    if (!isTvMusic());
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isRadioAnimationEnabled()
  {
    return Gservices.getBoolean(this.mContext.getContentResolver(), "music_show_radio_animation", true);
  }

  public boolean isStreamOnlyOnWifi()
  {
    return getBooleanPreference("StreamOnlyOnWifi", false);
  }

  public boolean isTabletMusicExperience()
  {
    if ((isLargeScreen()) || (isXLargeScreen()) || (isTvMusic()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isTvMusic()
  {
    return this.mIsTv;
  }

  public boolean isValidAccount()
  {
    boolean bool = false;
    if (!getBooleanPreference("isInvalidAccount", bool))
      bool = true;
    return bool;
  }

  public boolean isXLargeScreen()
  {
    return this.mIsXLargeScreen;
  }

  public void maybeEnableUpgradeSync()
  {
    try
    {
      PackageManager localPackageManager = this.mContext.getPackageManager();
      String str = this.mContext.getPackageName();
      int i = localPackageManager.getPackageInfo(str, 0).versionCode;
      int j = Gservices.getInt(this.mContext.getContentResolver(), "music_min_version_upgrade_sync", 1205);
      if ((j != -1) && (i >= j) && (getLastUpgradeSyncVersion() < j))
      {
        setUpgradeSyncDone(false);
        return;
      }
      setUpgradeSyncDone(true);
      return;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      Log.e("MusicPreferences", "Couldn't get current version code.", localNameNotFoundException);
    }
  }

  public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener)
  {
    synchronized (this.mPreferenceChangeListeners)
    {
      boolean bool = this.mPreferenceChangeListeners.add(paramOnSharedPreferenceChangeListener);
      return;
    }
  }

  public Account resolveAccount(String paramString)
  {
    if (paramString == null)
    {
      localAccount = null;
      return localAccount;
    }
    Account[] arrayOfAccount = AccountManager.get(this.mContext).getAccountsByType("com.google");
    Account localAccount = null;
    int i = 0;
    while (true)
    {
      int j = arrayOfAccount.length;
      if (i >= j)
        break;
      if (arrayOfAccount[i].name.equals(paramString))
      {
        localAccount = arrayOfAccount[i];
        break;
      }
      i += 1;
    }
  }

  /** @deprecated */
  public void runWithPreferenceService(Runnable paramRunnable)
  {
    try
    {
      if (!hasPreferenceService())
        boolean bool = this.mRunOnceServiceConnected.add(paramRunnable);
      while (true)
      {
        return;
        AsyncWorkers.runAsync(AsyncWorkers.sBackendServiceWorker, paramRunnable);
      }
    }
    finally
    {
    }
  }

  public void setAutoCachingEnabled(boolean paramBoolean)
  {
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference("AutoCacheOn", localBoolean);
  }

  public void setCachedStreamingMusicEnabled(boolean paramBoolean)
  {
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference("CacheStreamed", localBoolean);
  }

  public void setClearNautilusContent(boolean paramBoolean)
  {
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference("clearNautilusContent", localBoolean);
  }

  public void setContentFilter(int paramInt)
  {
    Integer localInteger = Integer.valueOf(paramInt);
    setPreference("ContentFilter", localInteger);
  }

  public void setDatabaseVersion(int paramInt)
  {
    Integer localInteger = Integer.valueOf(paramInt);
    setPreference("databaseVersion", localInteger);
  }

  public void setDisplayOptions(int paramInt)
  {
    if (!isOnDeviceMusicCapable())
      return;
    Integer localInteger = Integer.valueOf(paramInt);
    Runnable local4 = new Runnable()
    {
      public void run()
      {
        ContentResolver localContentResolver = MusicPreferences.this.mContext.getContentResolver();
        Uri localUri = MusicContent.CONTENT_URI;
        localContentResolver.notifyChange(localUri, null, false);
      }
    };
    setPreferenceWithExtraTask("DisplayOptions", localInteger, local4);
  }

  public void setInvalidStreamingAccount(Account paramAccount)
  {
    setStreamingAccount(paramAccount, false);
  }

  public void setIsValidAccount(boolean paramBoolean)
  {
    if (!paramBoolean);
    for (boolean bool = true; ; bool = false)
    {
      Boolean localBoolean = Boolean.valueOf(bool);
      setPreference("isInvalidAccount", localBoolean);
      return;
    }
  }

  public void setKeepOnDownloadPaused(boolean paramBoolean)
  {
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference("isKeepOnDownloadPaused", localBoolean);
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    Uri localUri = MusicContent.DOWNLOAD_QUEUE_URI;
    localContentResolver.notifyChange(localUri, null, false);
  }

  public void setLastUpgradeSyncVersion()
  {
    try
    {
      PackageManager localPackageManager = this.mContext.getPackageManager();
      String str = this.mContext.getPackageName();
      Integer localInteger = Integer.valueOf(localPackageManager.getPackageInfo(str, 0).versionCode);
      setPreference("lastUpgradeSyncVersion", localInteger);
      return;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      Log.e("MusicPreferences", "Couldn't get current version code.", localNameNotFoundException);
    }
  }

  public void setLogFilesEnable(boolean paramBoolean)
  {
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference("LogFilesEnabled", localBoolean);
  }

  public void setOffineDLOnlyOnWifi(boolean paramBoolean)
  {
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference("OfflineDLOnlyOnWifi", localBoolean);
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    Uri localUri = MusicContent.CONTENT_URI;
    localContentResolver.notifyChange(localUri, null, false);
  }

  public void setResetSyncState(boolean paramBoolean)
  {
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference("resetSyncState", localBoolean);
  }

  public void setSecondaryExternalStoragePath(String paramString)
  {
    setPreference("secondaryExternalMountPoint", paramString);
  }

  public void setShouldShowTutorialCard(String paramString, boolean paramBoolean)
  {
    String str = "ShowTutorialCard" + paramString;
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference(str, localBoolean);
  }

  public void setShowSyncNotification(boolean paramBoolean)
  {
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference("showSyncNotification", localBoolean);
  }

  public void setShowUnpinDialog(boolean paramBoolean)
  {
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference("showUnpinDialog", localBoolean);
  }

  public void setSidePannelWasClosedOnce(boolean paramBoolean)
  {
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference("SidePannelWasClosedOnce", localBoolean);
  }

  public void setStoreAvailable(boolean paramBoolean)
  {
    String str = "Setting store availability: " + paramBoolean;
    Log.i("MusicPreferences", str);
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference("storeAvailable", localBoolean);
  }

  public void setStoreAvailableLastChecked(long paramLong)
  {
    Long localLong = Long.valueOf(paramLong);
    setPreference("storeAvailableLastChecked", localLong);
  }

  public void setStreamOnlyOnWifi(boolean paramBoolean)
  {
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference("StreamOnlyOnWifi", localBoolean);
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    Uri localUri = MusicContent.CONTENT_URI;
    localContentResolver.notifyChange(localUri, null, false);
  }

  public void setStreamQuality(int paramInt)
  {
    Integer localInteger = Integer.valueOf(paramInt);
    setPreference("StreamQuality", localInteger);
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    Uri localUri = MusicContent.CONTENT_URI;
    localContentResolver.notifyChange(localUri, null, false);
  }

  public void setStreamingAccount(Account paramAccount)
  {
    setStreamingAccount(paramAccount, true);
  }

  public void setTempNautilusStatus(NautilusStatus paramNautilusStatus)
  {
    Integer localInteger = Integer.valueOf(paramNautilusStatus.ordinal());
    setPreference("TempNautilusStatus", localInteger);
    Long localLong = Long.valueOf(System.currentTimeMillis());
    setPreference("TempNautilusStatusTimestamp", localLong);
    updateNautilusTimestamp();
    StringBuilder localStringBuilder = new StringBuilder().append("Set temporary nautilus status to ");
    String str1 = paramNautilusStatus.name();
    String str2 = str1;
    Log.i("MusicPreferences", str2);
  }

  public void setTutorialViewed(boolean paramBoolean)
  {
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference("TutorialViewed2", localBoolean);
  }

  public void setUpgradeSyncDone(boolean paramBoolean)
  {
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference("upgradeSyncDone", localBoolean);
  }

  public boolean shouldShowTutorialCard(String paramString)
  {
    String str = "ShowTutorialCard" + paramString;
    return getBooleanPreference(str, true);
  }

  public boolean shouldShowUnpinDialog()
  {
    return getBooleanPreference("showUnpinDialog", true);
  }

  public boolean showStartRadioButtonsInActionBar()
  {
    return isTabletMusicExperience();
  }

  public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener paramOnSharedPreferenceChangeListener)
  {
    synchronized (this.mPreferenceChangeListeners)
    {
      boolean bool = this.mPreferenceChangeListeners.remove(paramOnSharedPreferenceChangeListener);
      return;
    }
  }

  public void updateNautilusTimestamp()
  {
    long l1 = System.currentTimeMillis();
    long l2 = getLongPreference("NautilusTimestampMillisec", 0L);
    if (Math.abs(l1 - l2) <= 43200000L)
      return;
    Long localLong = Long.valueOf(l1);
    setPreference("NautilusTimestampMillisec", localLong);
    if (LOGV)
      Log.i("MusicPreferences", "Nautlus status updated");
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this.mContext);
    Object[] arrayOfObject = new Object[0];
    localMusicEventLogger.trackEvent("nautilusStatusUpdated", arrayOfObject);
  }

  public void validateStreamingAccount()
  {
    if (!isConfiguredSyncAccount())
      return;
    if (getSyncAccount() != null)
      return;
    setStreamingAccount(null);
  }

  /** @deprecated */
  public boolean waitForFullyLoaded()
  {
    try
    {
      boolean bool1 = waitForServiceConnection();
      boolean bool2 = bool1;
      return bool2;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public boolean wasSidePannelClosedOnce()
  {
    return getBooleanPreference("SidePannelWasClosedOnce", false);
  }

  public boolean wasTutorialViewed()
  {
    return getBooleanPreference("TutorialViewed2", false);
  }

  private class AsyncPreferenceUpdate
    implements Runnable
  {
    private final Runnable mExtraTask;
    private final String mKey;
    private final Object mValue;

    public AsyncPreferenceUpdate(String paramObject, Object arg3)
    {
      this(paramObject, localObject, null);
    }

    public AsyncPreferenceUpdate(String paramObject, Object paramRunnable, Runnable arg4)
    {
      this.mKey = paramObject;
      this.mValue = paramRunnable;
      Object localObject;
      this.mExtraTask = localObject;
    }

    public void run()
    {
      if (!MusicPreferences.this.hasPreferenceService())
      {
        MusicPreferences.this.runWithPreferenceService(this);
        return;
      }
      try
      {
        if (this.mValue == null)
        {
          IPreferenceService localIPreferenceService1 = MusicPreferences.this.mPreferenceService;
          String str1 = this.mKey;
          localIPreferenceService1.remove(str1);
        }
        while (true)
        {
          if (this.mExtraTask == null)
            return;
          this.mExtraTask.run();
          return;
          if (!(this.mValue instanceof Integer))
            break;
          IPreferenceService localIPreferenceService2 = MusicPreferences.this.mPreferenceService;
          String str2 = this.mKey;
          int i = ((Integer)this.mValue).intValue();
          localIPreferenceService2.setIntPreference(str2, i);
        }
      }
      catch (RemoteException localRemoteException)
      {
        while (true)
        {
          StringBuilder localStringBuilder1 = new StringBuilder().append("Error communicating to preference service: ");
          String str3 = localRemoteException.getMessage();
          String str4 = str3;
          Log.e("MusicPreferences", str4, localRemoteException);
          continue;
          if ((this.mValue instanceof Long))
          {
            IPreferenceService localIPreferenceService3 = MusicPreferences.this.mPreferenceService;
            String str5 = this.mKey;
            long l = ((Long)this.mValue).longValue();
            localIPreferenceService3.setLongPreference(str5, l);
          }
          else if ((this.mValue instanceof String))
          {
            IPreferenceService localIPreferenceService4 = MusicPreferences.this.mPreferenceService;
            String str6 = this.mKey;
            String str7 = (String)this.mValue;
            localIPreferenceService4.setStringPreference(str6, str7);
          }
          else
          {
            if (!(this.mValue instanceof Boolean))
              break;
            IPreferenceService localIPreferenceService5 = MusicPreferences.this.mPreferenceService;
            String str8 = this.mKey;
            boolean bool = ((Boolean)this.mValue).booleanValue();
            localIPreferenceService5.setBooleanPreference(str8, bool);
          }
        }
        StringBuilder localStringBuilder2 = new StringBuilder().append("Unknown value type: ");
        Object localObject = this.mValue;
        String str9 = localObject;
        throw new IllegalArgumentException(str9);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.preferences.MusicPreferences
 * JD-Core Version:    0.6.2
 */