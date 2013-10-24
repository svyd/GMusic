package com.google.android.music.download.keepon;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Pair;
import android.widget.RemoteViews;
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
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.DataNotFoundException;
import com.google.android.music.store.MusicContent;
import com.google.android.music.store.MusicFile;
import com.google.android.music.store.Store;
import com.google.android.music.ui.AppNavigation;
import com.google.android.music.ui.HomeActivity;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.MusicUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KeeponSchedulingService extends AbstractSchedulingService
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private final KeeponCallbackManagerImpl mKeeponCallbackManager;
  private final int mNonClearableFlags = 106;
  private Notification mNotification;
  private volatile NotificationManager mNotificationManager;
  private SharedPreferences.OnSharedPreferenceChangeListener mPrefChangeListener;
  private volatile int mStartId;

  public KeeponSchedulingService()
  {
    super("KeeponService", localOwner, KeeponStartupReceiver.class);
    KeeponCallbackManagerImpl localKeeponCallbackManagerImpl = new KeeponCallbackManagerImpl();
    this.mKeeponCallbackManager = localKeeponCallbackManagerImpl;
    SharedPreferences.OnSharedPreferenceChangeListener local1 = new SharedPreferences.OnSharedPreferenceChangeListener()
    {
      public void onSharedPreferenceChanged(SharedPreferences paramAnonymousSharedPreferences, String paramAnonymousString)
      {
        boolean bool = KeeponSchedulingService.this.isDownloadingPaused();
        Context localContext = KeeponSchedulingService.this.getApplicationContext();
        Intent localIntent1 = new Intent(localContext, KeeponSchedulingService.class);
        if (bool)
        {
          KeeponSchedulingService localKeeponSchedulingService = KeeponSchedulingService.this;
          int i = KeeponSchedulingService.this.mStartId;
          localKeeponSchedulingService.sendCancelDownloadsMessage(i);
          return;
        }
        Intent localIntent2 = localIntent1.setAction("com.google.android.music.download.keepon.KeeponSchedulingService.START_DOWNLOAD");
        ComponentName localComponentName = localContext.startService(localIntent1);
      }
    };
    this.mPrefChangeListener = local1;
  }

  private Notification createNotification()
  {
    Notification localNotification = new Notification();
    long l = System.currentTimeMillis();
    localNotification.when = l;
    int i = localNotification.flags | 0x8;
    localNotification.flags = i;
    localNotification.icon = 2130837877;
    localNotification.defaults = 0;
    return localNotification;
  }

  private NotificationCompat.Builder createNotificationBuilder()
  {
    NotificationCompat.Builder localBuilder = new NotificationCompat.Builder(this);
    long l = System.currentTimeMillis();
    return localBuilder.setWhen(l).setDefaults(0).setAutoCancel(true).setOnlyAlertOnce(true).setSmallIcon(2130837877);
  }

  protected List<DownloadRequest> getNextDownloads(ICacheManager paramICacheManager, Collection<Long> paramCollection)
    throws OutOfSpaceException
  {
    ArrayList localArrayList = new ArrayList();
    Store localStore1 = Store.getInstance(this);
    Collection<Long> localCollection = paramCollection;
    ContentIdentifier[] arrayOfContentIdentifier1 = localStore1.getNextKeeponToDownload(1, localCollection);
    if (arrayOfContentIdentifier1 == null)
    {
      if (LOGV)
        Log.d("KeeponService", "getNextDownloads: trackIds=null");
      return localArrayList;
    }
    if (LOGV)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("getNextDownloads: trackIds.length=");
      int i = arrayOfContentIdentifier1.length;
      String str1 = i;
      Log.d("KeeponService", str1);
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
        Store localStore2 = Store.getInstance(this);
        long l1 = localContentIdentifier.getId();
        MusicFile localMusicFile = MusicFile.getSummaryMusicFile(localStore2, null, l1);
        localObject = localMusicFile;
        if (localObject == null)
          k += 1;
      }
      catch (DataNotFoundException localDataNotFoundException)
      {
        while (true)
        {
          Log.w("KeeponService", "Failed to load track data: ", localDataNotFoundException);
          continue;
          FileLocation localFileLocation;
          try
          {
            int i1 = DownloadRequest.Owner.KEEPON.ordinal();
            long l2 = localObject.getSize();
            localFileLocation = paramICacheManager.getTempFileLocation(localContentIdentifier, i1, l2, 3);
            if (localFileLocation != null)
              break label255;
            String str2 = "Failed to get file location.";
            Log.w("KeeponService", str2);
            String str3 = "Failed to get file location.";
            throw new OutOfSpaceException(str3);
          }
          catch (RemoteException localRemoteException)
          {
            Log.e("KeeponService", "Failed to get temp file location");
          }
          continue;
          label255: String str4 = localObject.getTitle();
          String str5 = localObject.getSourceId();
          int i2 = localObject.getSourceAccount();
          int i3 = DownloadRequest.PRIORITY_KEEPON;
          DownloadRequest.Owner localOwner = DownloadRequest.Owner.KEEPON;
          DownloadRequest localDownloadRequest = new DownloadRequest(localContentIdentifier, str4, str5, i2, i3, localOwner, 0L, false, localFileLocation, null, false);
          boolean bool = localArrayList.add(localDownloadRequest);
        }
      }
    }
  }

  protected long getTotalDownloadSize()
  {
    Pair localPair = Store.getInstance(this).getTotalNeedToKeepOn();
    if (localPair == null)
      Log.w("KeeponService", "Failed to retrieve the keepon download size");
    for (long l = 0L; ; l = ((Long)localPair.second).longValue())
      return l;
  }

  protected boolean isDownloadingPaused()
  {
    return getMusicPreferences().isKeepOnDownloadingPaused();
  }

  protected void notifyAllWorkFinished()
  {
    if (LOGV)
      Log.d("KeeponService", "notifyAllWorkFinished");
    stopForeground(true);
    Resources localResources = getResources();
    NotificationCompat.Builder localBuilder1 = createNotificationBuilder();
    String str1 = localResources.getString(2131231146);
    NotificationCompat.Builder localBuilder2 = localBuilder1.setTicker(str1);
    String str2 = localResources.getString(2131231146);
    NotificationCompat.Builder localBuilder3 = localBuilder2.setContentTitle(str2);
    PendingIntent localPendingIntent = AppNavigation.getIntentToOpenApp(this);
    Notification localNotification1 = localBuilder3.setContentIntent(localPendingIntent).getNotification();
    this.mNotification = localNotification1;
    NotificationManager localNotificationManager = this.mNotificationManager;
    Notification localNotification2 = this.mNotification;
    localNotificationManager.notify(10, localNotification2);
  }

  protected void notifyDisabled(AbstractSchedulingService.DisableReason paramDisableReason)
  {
    if (LOGV)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramDisableReason;
      String str1 = String.format("notifyDisabled: reason=%s", arrayOfObject);
      Log.d("KeeponService", str1);
    }
    stopForeground(true);
    Resources localResources = getResources();
    NotificationCompat.Builder localBuilder1 = createNotificationBuilder();
    String str2 = localResources.getString(2131231140);
    NotificationCompat.Builder localBuilder2 = localBuilder1.setTicker(str2);
    String str3 = localResources.getString(2131231140);
    NotificationCompat.Builder localBuilder3 = localBuilder2.setContentTitle(str3);
    int[] arrayOfInt = 2.$SwitchMap$com$google$android$music$download$AbstractSchedulingService$DisableReason;
    int i = paramDisableReason.ordinal();
    switch (arrayOfInt[i])
    {
    default:
      String str4 = "Unhandled disable reason: " + paramDisableReason;
      Log.w("KeeponService", str4);
      return;
    case 1:
      String str5 = localResources.getString(2131231145);
      Notification localNotification1 = localBuilder3.setContentText(str5).getNotification();
      this.mNotification = localNotification1;
      Notification localNotification2 = this.mNotification;
      PendingIntent localPendingIntent1 = AppNavigation.getIntentToOpenApp(this);
      localNotification2.contentIntent = localPendingIntent1;
      NotificationManager localNotificationManager1 = this.mNotificationManager;
      Notification localNotification3 = this.mNotification;
      localNotificationManager1.notify(10, localNotification3);
      return;
    case 2:
      String str6 = localResources.getString(2131231142);
      NotificationCompat.Builder localBuilder4 = localBuilder3.setContentText(str6);
      Intent localIntent1 = new Intent("android.net.wifi.PICK_WIFI_NETWORK");
      PendingIntent localPendingIntent2 = PendingIntent.getActivity(this, 0, localIntent1, 0);
      Notification localNotification4 = localBuilder4.setContentIntent(localPendingIntent2).getNotification();
      this.mNotification = localNotification4;
      NotificationManager localNotificationManager2 = this.mNotificationManager;
      Notification localNotification5 = this.mNotification;
      localNotificationManager2.notify(10, localNotification5);
      return;
    case 3:
      String str7 = localResources.getString(2131231143);
      NotificationCompat.Builder localBuilder5 = localBuilder3.setContentText(str7);
      PendingIntent localPendingIntent3 = AppNavigation.getIntentToOpenApp(this);
      Notification localNotification6 = localBuilder5.setContentIntent(localPendingIntent3).getNotification();
      this.mNotification = localNotification6;
      NotificationManager localNotificationManager3 = this.mNotificationManager;
      Notification localNotification7 = this.mNotification;
      localNotificationManager3.notify(10, localNotification7);
      return;
    case 4:
      String str8 = localResources.getString(2131231144);
      NotificationCompat.Builder localBuilder6 = localBuilder3.setContentText(str8);
      PendingIntent localPendingIntent4 = AppNavigation.getIntentToOpenApp(this);
      Notification localNotification8 = localBuilder6.setContentIntent(localPendingIntent4).getNotification();
      this.mNotification = localNotification8;
      NotificationManager localNotificationManager4 = this.mNotificationManager;
      Notification localNotification9 = this.mNotification;
      localNotificationManager4.notify(10, localNotification9);
      return;
    case 5:
      MusicPreferences localMusicPreferences = getMusicPreferences();
      String str9 = null;
      if (localMusicPreferences != null)
      {
        Account localAccount = localMusicPreferences.getSyncAccount();
        if (localAccount != null)
          str9 = localAccount.name;
      }
      while (true)
      {
        Uri localUri = MusicUtils.buildUriWithAccountName(localResources.getString(2131231067), str9);
        Intent localIntent2 = new Intent("android.intent.action.VIEW", localUri);
        String str10 = localResources.getString(2131231078);
        NotificationCompat.Builder localBuilder7 = localBuilder3.setContentText(str10);
        PendingIntent localPendingIntent5 = PendingIntent.getActivity(this, 0, localIntent2, 0);
        Notification localNotification10 = localBuilder7.setContentIntent(localPendingIntent5).getNotification();
        this.mNotification = localNotification10;
        NotificationManager localNotificationManager5 = this.mNotificationManager;
        Notification localNotification11 = this.mNotification;
        localNotificationManager5.notify(10, localNotification11);
        return;
        Log.w("KeeponService", "Failed to get music preferences");
      }
    case 6:
    }
    String str11 = localResources.getString(2131231139);
    NotificationCompat.Builder localBuilder8 = localBuilder3.setTicker(str11);
    String str12 = localResources.getString(2131231139);
    NotificationCompat.Builder localBuilder9 = localBuilder8.setContentTitle(str12);
    String str13 = localResources.getString(2131231141);
    Notification localNotification12 = localBuilder9.setContentText(str13).getNotification();
    this.mNotification = localNotification12;
    Notification localNotification13 = this.mNotification;
    PendingIntent localPendingIntent6 = AppNavigation.getIntentToOpenDownloadQueueActivity(this);
    localNotification13.contentIntent = localPendingIntent6;
    NotificationManager localNotificationManager6 = this.mNotificationManager;
    Notification localNotification14 = this.mNotification;
    localNotificationManager6.notify(10, localNotification14);
  }

  protected void notifyDownloadCompleted(DownloadRequest paramDownloadRequest, DownloadProgress paramDownloadProgress)
  {
    if (paramDownloadProgress.isSavable())
    {
      String str = paramDownloadProgress.getHttpContentType();
      long l = paramDownloadProgress.getDownloadByteLength();
      storeInCache(paramDownloadRequest, str, l);
      Store.getInstance(this).updateKeeponDownloadSongCounts();
    }
    while (true)
    {
      ContentResolver localContentResolver1 = getContentResolver();
      Uri localUri1 = MusicContent.DOWNLOAD_QUEUE_URI;
      localContentResolver1.notifyChange(localUri1, null, false);
      ContentResolver localContentResolver2 = getContentResolver();
      Uri localUri2 = MusicContent.KEEP_ON_URI;
      localContentResolver2.notifyChange(localUri2, null, false);
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
    if (LOGV)
    {
      String str1 = "progress: " + paramFloat;
      Log.d("KeeponService", str1);
    }
    this.mKeeponCallbackManager.notifyListeners(paramDownloadProgress);
    if (this.mNotification == null)
    {
      Notification localNotification1 = createNotification();
      this.mNotification = localNotification1;
    }
    Resources localResources = getResources();
    Notification localNotification2 = this.mNotification;
    String str2 = getPackageName();
    RemoteViews localRemoteViews1 = new RemoteViews(str2, 2130968613);
    localNotification2.contentView = localRemoteViews1;
    RemoteViews localRemoteViews2 = this.mNotification.contentView;
    String str3 = localResources.getString(2131231139);
    localRemoteViews2.setTextViewText(2131296391, str3);
    this.mNotification.flags = 106;
    int i = Math.round(100.0F * paramFloat);
    int j = Math.max(0, i);
    int k = Math.min(100, j);
    RemoteViews localRemoteViews3 = this.mNotification.contentView;
    String str4 = k + "%";
    localRemoteViews3.setTextViewText(2131296392, str4);
    this.mNotification.contentView.setInt(2131296393, "setMax", 100);
    this.mNotification.contentView.setInt(2131296393, "setProgress", k);
    Notification localNotification3 = this.mNotification;
    PendingIntent localPendingIntent = AppNavigation.getIntentToOpenDownloadQueueActivity(this);
    localNotification3.contentIntent = localPendingIntent;
    Notification localNotification4 = this.mNotification;
    startForeground(10, localNotification4);
  }

  protected void notifyDownloadStarting()
  {
    if (LOGV)
      Log.d("KeeponService", "notifyDownloadStarting");
    Resources localResources = getResources();
    NotificationCompat.Builder localBuilder1 = createNotificationBuilder();
    String str1 = localResources.getString(2131231139);
    NotificationCompat.Builder localBuilder2 = localBuilder1.setTicker(str1);
    String str2 = localResources.getString(2131231139);
    NotificationCompat.Builder localBuilder3 = localBuilder2.setContentTitle(str2);
    Intent localIntent = new Intent(this, HomeActivity.class);
    PendingIntent localPendingIntent = PendingIntent.getActivity(this, 0, localIntent, 0);
    Notification localNotification1 = localBuilder3.setContentIntent(localPendingIntent).getNotification();
    this.mNotification = localNotification1;
    NotificationManager localNotificationManager = this.mNotificationManager;
    Notification localNotification2 = this.mNotification;
    localNotificationManager.notify(10, localNotification2);
  }

  public IBinder onBind(Intent paramIntent)
  {
    return this.mKeeponCallbackManager;
  }

  public void onCreate()
  {
    super.onCreate();
    NotificationManager localNotificationManager = (NotificationManager)getSystemService("notification");
    this.mNotificationManager = localNotificationManager;
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

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    if (LOGV)
    {
      String str1 = "intent=" + paramIntent;
      Log.d("KeeponService", str1);
    }
    this.mStartId = paramInt2;
    String str2;
    if (paramIntent != null)
    {
      str2 = paramIntent.getAction();
      if (!"com.google.android.music.download.keepon.KeeponSchedulingService.STOP_DOWNLOAD".equals(str2))
        break label73;
      stopSelf();
    }
    while (true)
    {
      return 3;
      str2 = null;
      break;
      label73: if ("com.google.android.music.download.keepon.KeeponSchedulingService.START_DOWNLOAD".equals(str2))
      {
        sendInitScheduleMessage(paramInt2);
      }
      else
      {
        String str3 = "Unknown action=" + str2;
        Log.w("KeeponService", str3);
        stopSelf();
      }
    }
  }

  public static final class KeeponStartupReceiver extends BroadcastReceiver
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (KeeponSchedulingService.LOGV)
        Log.d("KeeponService", "KeeponStartupReceiver.onReceive");
      Intent localIntent1 = new Intent(paramContext, KeeponSchedulingService.class);
      Intent localIntent2 = localIntent1.setAction("com.google.android.music.download.keepon.KeeponSchedulingService.START_DOWNLOAD");
      ComponentName localComponentName = paramContext.startService(localIntent1);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.keepon.KeeponSchedulingService
 * JD-Core Version:    0.6.2
 */