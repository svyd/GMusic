package com.google.android.music.download.ringtone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Pair;
import com.google.android.music.download.AbstractSchedulingService;
import com.google.android.music.download.AbstractSchedulingService.DisableReason;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.download.DownloadProgress;
import com.google.android.music.download.DownloadRequest;
import com.google.android.music.download.DownloadRequest.Owner;
import com.google.android.music.download.DownloadUtils;
import com.google.android.music.download.cache.FileLocation;
import com.google.android.music.download.cache.ICacheManager;
import com.google.android.music.download.cache.OutOfSpaceException;
import com.google.android.music.log.Log;
import com.google.android.music.store.DataNotFoundException;
import com.google.android.music.store.MusicFile;
import com.google.android.music.store.MusicRingtoneManager;
import com.google.android.music.store.Store;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RingtoneSchedulingService extends AbstractSchedulingService
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private final BroadcastReceiver mBroadcastReceiver;
  private volatile NotificationManager mNotificationManager;
  private volatile boolean mStartupReceiverEnabled;

  public RingtoneSchedulingService()
  {
    super("RingtoneService", localOwner, RingtoneStartupReceiver.class);
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        Bundle localBundle = paramAnonymousIntent.getExtras();
        if (localBundle == null)
          return;
        int i = localBundle.getInt("status");
        String str1 = localBundle.getString("name");
        long l = localBundle.getLong("musicId");
        String str2 = localBundle.getString("filepath");
        RingtoneSchedulingService localRingtoneSchedulingService = RingtoneSchedulingService.this;
        Context localContext = paramAnonymousContext;
        localRingtoneSchedulingService.showNotification(localContext, i, str1, l, str2);
      }
    };
    this.mBroadcastReceiver = local1;
  }

  /** @deprecated */
  private void showNotification(Context paramContext, int paramInt, String paramString1, long paramLong, String paramString2)
  {
    int i;
    int j;
    if (paramInt == 0)
    {
      i = 2131231147;
      j = 1;
    }
    try
    {
      Object[] arrayOfObject1 = new Object[j];
      arrayOfObject1[0] = paramString1;
      String str1 = getString(i, arrayOfObject1);
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = paramString1;
      String str3;
      for (Object localObject1 = getString(2131231148, arrayOfObject2); ; localObject1 = str3)
      {
        Notification localNotification = new Notification();
        long l = System.currentTimeMillis();
        localNotification.when = l;
        localNotification.flags = 24;
        localNotification.icon = 17301634;
        localNotification.tickerText = str1;
        localNotification.defaults = 0;
        Intent localIntent = MusicRingtoneManager.getEditRingtoneIntent(paramContext, paramString2, paramLong);
        PendingIntent localPendingIntent1 = PendingIntent.getActivity(this, 0, localIntent, 0);
        localNotification.contentIntent = localPendingIntent1;
        String str2 = getString(2131231149);
        PendingIntent localPendingIntent2 = localNotification.contentIntent;
        localNotification.setLatestEventInfo(this, (CharSequence)localObject1, str2, localPendingIntent2);
        this.mNotificationManager.notify(20, localNotification);
        return;
        i = 2131231150;
        str1 = getString(i);
        Object[] arrayOfObject3 = new Object[1];
        arrayOfObject3[0] = paramString1;
        str3 = getString(2131231148, arrayOfObject3);
      }
    }
    finally
    {
    }
  }

  protected List<DownloadRequest> getNextDownloads(ICacheManager paramICacheManager, Collection<Long> paramCollection)
    throws OutOfSpaceException
  {
    ArrayList localArrayList = new ArrayList();
    long[] arrayOfLong = Store.getInstance(this).getRingtoneManager().getRingtoneRequests(1);
    if (arrayOfLong == null)
    {
      if (LOGV)
        Log.d("RingtoneService", "getNextDownloads: ids=null");
      return localArrayList;
    }
    ContentIdentifier[] arrayOfContentIdentifier1 = new ContentIdentifier[arrayOfLong.length];
    int i = 0;
    while (true)
    {
      int j = arrayOfLong.length;
      if (i >= j)
        break;
      long l1 = arrayOfLong[i];
      ContentIdentifier.Domain localDomain = ContentIdentifier.Domain.DEFAULT;
      ContentIdentifier localContentIdentifier1 = new ContentIdentifier(l1, localDomain);
      arrayOfContentIdentifier1[i] = localContentIdentifier1;
      i += 1;
    }
    if (LOGV)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("getNextDownloads: trackIds.length=");
      int k = arrayOfContentIdentifier1.length;
      String str1 = k;
      Log.d("RingtoneService", str1);
    }
    ContentIdentifier[] arrayOfContentIdentifier2 = arrayOfContentIdentifier1;
    int m = arrayOfContentIdentifier2.length;
    int n = 0;
    while (true)
    {
      int i1 = n;
      int i2 = m;
      if (i1 >= i2)
        break;
      ContentIdentifier localContentIdentifier2 = arrayOfContentIdentifier2[n];
      Object localObject = null;
      try
      {
        Store localStore = Store.getInstance(this);
        long l2 = localContentIdentifier2.getId();
        MusicFile localMusicFile = MusicFile.getSummaryMusicFile(localStore, null, l2);
        localObject = localMusicFile;
        if (localObject == null)
          n += 1;
      }
      catch (DataNotFoundException localDataNotFoundException)
      {
        while (true)
        {
          Log.w("RingtoneService", "Failed to load track data: ", localDataNotFoundException);
          continue;
          FileLocation localFileLocation;
          try
          {
            int i3 = DownloadRequest.Owner.RINGTONE.ordinal();
            long l3 = localObject.getSize();
            localFileLocation = paramICacheManager.getTempFileLocation(localContentIdentifier2, i3, l3, 1);
            if (localFileLocation != null)
              break label313;
            String str2 = "Failed to get file location.";
            Log.w("RingtoneService", str2);
            String str3 = "Failed to get file location.";
            throw new OutOfSpaceException(str3);
          }
          catch (RemoteException localRemoteException)
          {
            Log.e("RingtoneService", "Failed to get temp file location");
          }
          continue;
          label313: String str4 = localObject.getTitle();
          String str5 = localObject.getSourceId();
          int i4 = localObject.getSourceAccount();
          int i5 = DownloadRequest.PRIORITY_RINGTONE;
          DownloadRequest.Owner localOwner = DownloadRequest.Owner.RINGTONE;
          DownloadRequest localDownloadRequest = new DownloadRequest(localContentIdentifier2, str4, str5, i4, i5, localOwner, 0L, false, localFileLocation, null, false);
          boolean bool = localArrayList.add(localDownloadRequest);
        }
      }
    }
  }

  protected long getTotalDownloadSize()
  {
    return ((Long)Store.getInstance(this).getRingtoneManager().getRingtoneRequestTotals(128).second).longValue();
  }

  protected boolean isDownloadingPaused()
  {
    return false;
  }

  protected void notifyAllWorkFinished()
  {
    Log.w("RingtoneService", "notifyAllWorkFinished: NOT IMPLEMENTED");
  }

  protected void notifyDisabled(AbstractSchedulingService.DisableReason paramDisableReason)
  {
    Log.w("RingtoneService", "notifyDisabled: NOT IMPLEMENTED");
    stopForeground(true);
  }

  protected void notifyDownloadCompleted(DownloadRequest paramDownloadRequest, DownloadProgress paramDownloadProgress)
  {
    long l = paramDownloadProgress.getDownloadByteLength();
    boolean bool = DownloadUtils.makeRingtoneFromDownloadRequest(this, paramDownloadRequest, l);
  }

  protected void notifyDownloadFailed(DownloadRequest paramDownloadRequest, DownloadProgress paramDownloadProgress)
  {
    Log.w("RingtoneService", "notifyDownloadFailed: NOT IMPLEMENTED");
  }

  protected void notifyDownloadProgress(float paramFloat, DownloadProgress paramDownloadProgress)
  {
    if (!LOGV)
      return;
    String str = "progressRatio: " + paramFloat;
    Log.d("RingtoneService", str);
  }

  protected void notifyDownloadStarting()
  {
    Log.w("RingtoneService", "notifyDownloadStarting: NOT IMPLEMENTED");
  }

  public IBinder onBind(Intent paramIntent)
  {
    return null;
  }

  public void onCreate()
  {
    super.onCreate();
    NotificationManager localNotificationManager = (NotificationManager)getSystemService("notification");
    this.mNotificationManager = localNotificationManager;
    PackageManager localPackageManager = getPackageManager();
    ComponentName localComponentName = new ComponentName(this, RingtoneStartupReceiver.class);
    if (localPackageManager.getComponentEnabledSetting(localComponentName) == 1);
    for (boolean bool = true; ; bool = false)
    {
      this.mStartupReceiverEnabled = bool;
      BroadcastReceiver localBroadcastReceiver = this.mBroadcastReceiver;
      IntentFilter localIntentFilter = new IntentFilter("com.google.android.music.RINGTONE_REQUEST_END");
      Intent localIntent = registerReceiver(localBroadcastReceiver, localIntentFilter);
      return;
    }
  }

  public void onDestroy()
  {
    BroadcastReceiver localBroadcastReceiver = this.mBroadcastReceiver;
    unregisterReceiver(localBroadcastReceiver);
    super.onDestroy();
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    if (LOGV)
    {
      String str1 = "intent=" + paramIntent;
      Log.d("RingtoneService", str1);
    }
    String str2;
    if (paramIntent != null)
    {
      str2 = paramIntent.getAction();
      if (!"com.google.android.music.download.ringtone.RingtoneSchedulingService.STOP_DOWNLOAD".equals(str2))
        break label69;
      stopSelf(paramInt2);
    }
    while (true)
    {
      return 3;
      str2 = null;
      break;
      label69: if ("com.google.android.music.download.ringtone.RingtoneSchedulingService.START_DOWNLOAD".equals(str2))
      {
        sendInitScheduleMessage(paramInt2);
      }
      else
      {
        String str3 = "Unknown action=" + str2;
        Log.w("RingtoneService", str3);
        stopSelf(paramInt2);
      }
    }
  }

  public static final class RingtoneStartupReceiver extends BroadcastReceiver
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (RingtoneSchedulingService.LOGV)
        Log.d("RingtoneService", "RingtoneStartupReceiver.onReceive");
      Intent localIntent1 = new Intent(paramContext, RingtoneSchedulingService.class);
      Intent localIntent2 = localIntent1.setAction("com.google.android.music.download.ringtone.RingtoneSchedulingService.START_DOWNLOAD");
      ComponentName localComponentName = paramContext.startService(localIntent1);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.ringtone.RingtoneSchedulingService
 * JD-Core Version:    0.6.2
 */