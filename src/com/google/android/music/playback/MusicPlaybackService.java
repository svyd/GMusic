package com.google.android.music.playback;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import com.google.android.gsf.Gservices;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.download.IDownloadProgressListener;
import com.google.android.music.homewidgets.NowPlayingWidgetProvider;
import com.google.android.music.medialist.AllSongsList;
import com.google.android.music.medialist.ExternalSongList;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.mix.MixDescriptor;
import com.google.android.music.mix.MixGenerationState;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.RecentItemsManager;
import com.google.android.music.ui.AppNavigation;
import com.google.android.music.utils.AlbumArtUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncWorkers;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

public class MusicPlaybackService extends Service
  implements SharedPreferences.OnSharedPreferenceChangeListener, RemoteControlClientCompat.OnGetPlaybackPositionListener, RemoteControlClientCompat.OnPlaybackPositionUpdateListener
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.PLAYBACK_SERVICE);
  private AudioManager mAudioManager;
  private AudioManagerCompat mAudioManagerCompat;
  private BroadcastReceiver mAudioNoisyReceiver;
  private AtomicReference<DevicePlayback> mCurrentPlayback;
  private Handler mDelayedStopHandler;
  private Handler mDeviceSwitchHandler;
  private BroadcastReceiver mIntentReceiver;
  private boolean mIsLandscape;
  boolean mIsNotificationShowing;
  private LocalDevicePlayback mLocalPlayback;
  private ComponentName mMediaButtonReceiver;
  private MusicPreferences mMusicPreferences;
  private MusicPlaybackServiceImpl mMusicStub;
  Notification mNotification;
  private Handler mNotificationCanceller;
  boolean mNotificationHasButtons;
  private BroadcastReceiver mOrientationMonitor;
  private volatile RemoteControlClientCompat mRemoteControlClient;
  private boolean mRetrievedNotificationHasButtons;
  private boolean mServiceInUse;
  private int mServiceStartId;
  boolean mUIVisible;

  public MusicPlaybackService()
  {
    AtomicReference localAtomicReference = new AtomicReference();
    this.mCurrentPlayback = localAtomicReference;
    this.mServiceStartId = -1;
    this.mServiceInUse = false;
    this.mRetrievedNotificationHasButtons = false;
    this.mNotificationHasButtons = false;
    this.mIsNotificationShowing = false;
    this.mUIVisible = false;
    MusicPlaybackServiceImpl localMusicPlaybackServiceImpl = new MusicPlaybackServiceImpl(this);
    this.mMusicStub = localMusicPlaybackServiceImpl;
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        String str1 = paramAnonymousIntent.getAction();
        String str2 = paramAnonymousIntent.getStringExtra("command");
        MusicUtils.debugLog("mIntentReceiver.onReceive " + str1 + " / " + str2);
        if (MusicPlaybackService.this.shouldIgnoreCommand(paramAnonymousIntent, str1, str2))
          return;
        if (("next".equals(str2)) || ("com.android.music.musicservicecommand.next".equals(str1)))
        {
          MusicPlaybackService.this.next();
          return;
        }
        if (("previous".equals(str2)) || ("com.android.music.musicservicecommand.previous".equals(str1)))
        {
          MusicPlaybackService.this.prev();
          return;
        }
        if (("togglepause".equals(str2)) || ("com.android.music.musicservicecommand.togglepause".equals(str1)))
        {
          if (MusicPlaybackService.this.isPlaying())
          {
            MusicPlaybackService.this.pause();
            return;
          }
          MusicPlaybackService.this.play();
          return;
        }
        if (("pause".equals(str2)) || ("com.android.music.musicservicecommand.pause".equals(str1)))
        {
          MusicPlaybackService.this.pause();
          return;
        }
        if ("play".equals(str2))
        {
          MusicPlaybackService.this.play();
          return;
        }
        if ("stop".equals(str2))
        {
          MusicPlaybackService.this.pause();
          long l = MusicPlaybackService.this.seek(0L);
          return;
        }
        if ("appwidgetupdate".equals(str2))
        {
          if (MusicPlaybackService.LOGV)
            int i = Log.v("MusicPlaybackService", "onReceive() with appwidgetupdate");
          int[] arrayOfInt = paramAnonymousIntent.getIntArrayExtra("appWidgetIds");
          Intent localIntent1 = new Intent("com.android.music.metachanged");
          boolean bool = MusicPlaybackService.this.isPlaying();
          Intent localIntent2 = localIntent1.putExtra("playing", bool);
          Intent localIntent3 = MusicPlaybackService.populateExtrasFromSharedPreferences(MusicPlaybackService.this, localIntent1);
          NowPlayingWidgetProvider.notifyChange(MusicPlaybackService.this, localIntent1, "com.android.music.metachanged");
          return;
        }
        if (!"com.android.music.playstatusrequest".equals(str1))
          return;
        MusicPlaybackService localMusicPlaybackService = MusicPlaybackService.this;
        DevicePlayback localDevicePlayback = (DevicePlayback)MusicPlaybackService.this.mCurrentPlayback.get();
        localMusicPlaybackService.notifyChange("com.android.music.playstatusresponse", localDevicePlayback);
      }
    };
    this.mIntentReceiver = local1;
    BroadcastReceiver local2 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        Bundle localBundle = paramAnonymousIntent.getExtras();
        if (localBundle != null)
          int i = localBundle.size();
        MusicUtils.debugLog("Received: " + paramAnonymousIntent + "/" + localBundle);
        String str = paramAnonymousIntent.getAction();
        if (!"android.media.AUDIO_BECOMING_NOISY".equals(str))
          return;
        if (!((DevicePlayback)MusicPlaybackService.this.mCurrentPlayback.get()).isPlayingLocally())
          return;
        Intent localIntent1 = new Intent(paramAnonymousContext, MusicPlaybackService.class);
        Intent localIntent2 = localIntent1.setAction("com.android.music.musicservicecommand");
        Intent localIntent3 = localIntent1.putExtra("command", "pause");
        Intent localIntent4 = localIntent1.putExtra("device", "local");
        ComponentName localComponentName = MusicPlaybackService.this.startService(localIntent1);
      }
    };
    this.mAudioNoisyReceiver = local2;
    BroadcastReceiver local3 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        String str = paramAnonymousIntent.getAction();
        if (!"android.intent.action.CONFIGURATION_CHANGED".equals(str))
          return;
        if (MusicPlaybackService.this.getResources().getConfiguration().orientation == 2);
        for (boolean bool1 = true; ; bool1 = false)
        {
          boolean bool2 = MusicPlaybackService.this.mIsLandscape;
          if (bool1 != bool2)
            return;
          boolean bool3 = MusicPlaybackService.access$302(MusicPlaybackService.this, bool1);
          LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
          Runnable local1 = new Runnable()
          {
            public void run()
            {
              MusicPlaybackService.this.updateNotification();
            }
          };
          AsyncWorkers.runAsync(localLoggableHandler, local1);
          return;
        }
      }
    };
    this.mOrientationMonitor = local3;
    Handler local4 = new Handler()
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        long l1 = ((Long)paramAnonymousMessage.getData().get("cancel_notification")).longValue();
        long l2 = System.currentTimeMillis();
        long l3 = l1 / 86400000L;
        long l4 = l2 / 86400000L;
        if ((l3 == l4) && (l1 % 86400000L > 3600000L))
        {
          Message localMessage = obtainMessage(1);
          Bundle localBundle = paramAnonymousMessage.getData();
          localMessage.setData(localBundle);
          long l5 = (1L + l4) * 86400000L - l2;
          boolean bool = sendMessageDelayed(localMessage, l5);
          return;
        }
        MusicPlaybackService.this.stopForeground(true);
      }
    };
    this.mNotificationCanceller = local4;
    Handler localHandler = new Handler();
    this.mDeviceSwitchHandler = localHandler;
    Handler local6 = new Handler()
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        if (MusicPlaybackService.this.mServiceInUse)
          return;
        if (MusicPlaybackService.this.mIsNotificationShowing)
          return;
        if (((DevicePlayback)MusicPlaybackService.this.mCurrentPlayback.get()).getState().playingOrWillPlay())
          return;
        ((DevicePlayback)MusicPlaybackService.this.mCurrentPlayback.get()).saveState();
        MusicPlaybackService localMusicPlaybackService = MusicPlaybackService.this;
        int i = MusicPlaybackService.this.mServiceStartId;
        localMusicPlaybackService.stopSelf(i);
      }
    };
    this.mDelayedStopHandler = local6;
  }

  private void addToRecentAsync(final SongList paramSongList)
  {
    final MusicPlaybackService localMusicPlaybackService = this;
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local11 = new Runnable()
    {
      public void run()
      {
        Context localContext = localMusicPlaybackService;
        SongList localSongList = paramSongList;
        boolean bool = RecentItemsManager.addRecentlyPlayedItem(localContext, localSongList);
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local11);
  }

  private void alertFailureIfNecessary(int paramInt)
  {
    MusicPreferences localMusicPreferences = this.mMusicPreferences;
    ErrorInfo localErrorInfo = ErrorInfo.createErrorInfo(paramInt, localMusicPreferences, null);
    if (!localErrorInfo.canShowNotification())
    {
      if (!LOGV)
        return;
      String str = "Not showing notification for error type:" + paramInt;
      int i = Log.w("MusicPlaybackService", str);
      return;
    }
    if ((this.mUIVisible) && (localErrorInfo.canShowAlert()))
      return;
    Notification localNotification = localErrorInfo.createNotification(this);
    ((NotificationManager)getSystemService("notification")).notify(2, localNotification);
  }

  private static boolean doesIntentRequireSongInfo(String paramString)
  {
    boolean bool = false;
    if ("com.android.music.queuechanged".equals(paramString));
    while (true)
    {
      return bool;
      if ((!"com.android.music.playbackfailed".equals(paramString)) && (!"com.google.android.music.repeatmodechanged".equals(paramString)) && (!"com.google.android.music.shufflemodechanged".equals(paramString)) && (!"com.google.android.music.mix.generationfinished".equals(paramString)) && (!"com.google.android.music.mix.playbackmodechanged".equals(paramString)))
        bool = true;
    }
  }

  private void gotoIdleState()
  {
    this.mDelayedStopHandler.removeCallbacksAndMessages(null);
    Message localMessage1 = this.mDelayedStopHandler.obtainMessage();
    boolean bool1 = this.mDelayedStopHandler.sendMessageDelayed(localMessage1, 6000L);
    Message localMessage2 = this.mNotificationCanceller.obtainMessage(1);
    this.mNotificationCanceller.removeMessages(1);
    Bundle localBundle = new Bundle();
    long l = System.currentTimeMillis();
    localBundle.putLong("cancel_notification", l);
    localMessage2.setData(localBundle);
    boolean bool2 = this.mNotificationCanceller.sendMessageDelayed(localMessage2, 18000000L);
    if ((!this.mUIVisible) && (MusicPreferences.isHoneycombOrGreater()))
    {
      DevicePlayback.State localState1 = ((DevicePlayback)this.mCurrentPlayback.get()).getState();
      DevicePlayback.State localState2 = DevicePlayback.State.NO_PLAYLIST;
      if (localState1 != localState2)
        break label149;
    }
    label149: for (boolean bool3 = true; ; bool3 = false)
    {
      stopForeground(bool3);
      if (!bool3)
        return;
      this.mIsNotificationShowing = false;
      return;
    }
  }

  private boolean hasLocal()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).hasLocal();
  }

  private boolean hasRemote()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).hasRemote();
  }

  public static Intent populateExtras(Intent paramIntent, IMusicPlaybackService paramIMusicPlaybackService, Context paramContext)
  {
    while (true)
    {
      String str1;
      PlaybackState localPlaybackState;
      Object localObject;
      try
      {
        str1 = paramIntent.getAction();
        localPlaybackState = paramIMusicPlaybackService.getPlaybackState();
        if (str1.equals("com.android.music.queuechanged"))
        {
          boolean bool1 = localPlaybackState.isPlaylistLoading();
          Intent localIntent1 = paramIntent.putExtra("queueLoading", bool1);
          boolean bool2 = localPlaybackState.hasValidPlaylist();
          Intent localIntent2 = paramIntent.putExtra("hasValidPlaylist", bool2);
          int i = localPlaybackState.getQueueSize();
          Intent localIntent3 = paramIntent.putExtra("ListSize", i);
          localObject = localPlaybackState.getMediaList();
          if (localObject != null)
          {
            String str2 = ((MediaList)localObject).getStoreUrl();
            Intent localIntent4 = paramIntent.putExtra("queueStoreUrl", str2);
          }
          if (!"com.android.music.playstatechanged".equals(str1))
            break label707;
          boolean bool3 = localPlaybackState.isPlaying();
          Intent localIntent5 = paramIntent.putExtra("playstate", bool3);
          return paramIntent;
        }
        if ("com.google.android.music.repeatmodechanged".equals(str1))
        {
          int j = localPlaybackState.getRepeatMode();
          Intent localIntent6 = paramIntent.putExtra("repeat", j);
          continue;
        }
      }
      catch (RemoteException localRemoteException)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Error trying to get variables from the MusicPlaybackService: ");
        String str3 = localRemoteException.getMessage();
        String str4 = str3;
        throw new RuntimeException(str4, localRemoteException);
      }
      if ("com.google.android.music.shufflemodechanged".equals(str1))
      {
        int k = localPlaybackState.getShuffleMode();
        Intent localIntent7 = paramIntent.putExtra("shuffle", k);
      }
      else
      {
        TrackInfo localTrackInfo = paramIMusicPlaybackService.getCurrentTrackInfo();
        if (localTrackInfo == null)
        {
          localObject = null;
          label263: if (localObject != null)
          {
            Long localLong1 = Long.valueOf(((ContentIdentifier)localObject).getId());
            Intent localIntent8 = paramIntent.putExtra("id", localLong1);
            Integer localInteger = Integer.valueOf(((ContentIdentifier)localObject).getDomain().ordinal());
            Intent localIntent9 = paramIntent.putExtra("domain", localInteger);
            String str5 = localTrackInfo.getArtistName();
            Intent localIntent10 = paramIntent.putExtra("artist", str5);
            String str6 = localTrackInfo.getAlbumName();
            Intent localIntent11 = paramIntent.putExtra("album", str6);
            long l1 = localTrackInfo.getAlbumId();
            Intent localIntent12 = paramIntent.putExtra("albumId", l1);
            String str7 = localTrackInfo.getTrackName();
            Intent localIntent13 = paramIntent.putExtra("track", str7);
            int m = localTrackInfo.getRating();
            Intent localIntent14 = paramIntent.putExtra("rating", m);
            long l2 = localTrackInfo.getDuration();
            Intent localIntent15 = paramIntent.putExtra("duration", l2);
            int n = localTrackInfo.getPreviewPlayType();
            Intent localIntent16 = paramIntent.putExtra("previewPlayType", n);
            Boolean localBoolean1 = localTrackInfo.getAlbumArtIsFromService();
            Intent localIntent17 = paramIntent.putExtra("albumArtFromService", localBoolean1);
            boolean bool4 = localPlaybackState.isPlaying();
            Intent localIntent18 = paramIntent.putExtra("playing", bool4);
            boolean bool5 = localPlaybackState.isStreaming();
            Intent localIntent19 = paramIntent.putExtra("streaming", bool5);
            boolean bool6 = localPlaybackState.isPreparing();
            Intent localIntent20 = paramIntent.putExtra("preparing", bool6);
            boolean bool7 = localPlaybackState.isInErrorState();
            Intent localIntent21 = paramIntent.putExtra("inErrorState", bool7);
            long l3 = localPlaybackState.getPosition();
            Intent localIntent22 = paramIntent.putExtra("position", l3);
            Long localLong2 = Long.valueOf(localPlaybackState.getQueuePosition());
            Intent localIntent23 = paramIntent.putExtra("ListPosition", localLong2);
            Long localLong3 = Long.valueOf(localPlaybackState.getQueueSize());
            Intent localIntent24 = paramIntent.putExtra("ListSize", localLong3);
            boolean bool8 = localPlaybackState.isCurrentSongLoaded();
            Intent localIntent25 = paramIntent.putExtra("currentSongLoaded", bool8);
            if ((localPlaybackState.getMediaList() instanceof ExternalSongList))
            {
              String str8 = ((ExternalSongList)localPlaybackState.getMediaList()).getAlbumArtUrl(paramContext);
              Intent localIntent26 = paramIntent.putExtra("externalAlbumArtUrl", str8);
              Intent localIntent27 = paramIntent.putExtra("supportsRating", false);
            }
          }
        }
        else
        {
          while (true)
          {
            boolean bool9 = localPlaybackState.isLocalDevicePlayback();
            Intent localIntent28 = paramIntent.putExtra("local", bool9);
            break;
            localObject = localTrackInfo.getSongId();
            break label263;
            Boolean localBoolean2 = localTrackInfo.getSupportRating();
            Intent localIntent29 = paramIntent.putExtra("supportsRating", localBoolean2);
          }
          label707: if ("com.android.music.playbackfailed".equals(str1))
          {
            int i1 = localPlaybackState.getErrorType();
            Intent localIntent30 = paramIntent.putExtra("errorType", i1);
          }
        }
      }
    }
  }

  public static Intent populateExtrasFromSharedPreferences(Context paramContext, Intent paramIntent)
  {
    SharedPreferences localSharedPreferences = paramContext.getSharedPreferences("Music", 1);
    long l = localSharedPreferences.getLong("curalbumid", 65535L);
    if (l < 0L)
    {
      Intent localIntent1 = paramIntent.putExtra("albumArtResourceId", 2130837595);
      CharSequence localCharSequence = paramContext.getResources().getText(2131230913);
      Intent localIntent2 = paramIntent.putExtra("error", localCharSequence);
    }
    while (true)
    {
      return paramIntent;
      Intent localIntent3 = paramIntent.putExtra("supportsRating", true);
      String str1 = paramContext.getString(2131230890);
      String str2 = localSharedPreferences.getString("curartist", str1);
      Intent localIntent4 = paramIntent.putExtra("artist", str2);
      Intent localIntent5 = paramIntent.putExtra("albumId", l);
      String str3 = paramContext.getString(2131230891);
      String str4 = localSharedPreferences.getString("curalbum", str3);
      Intent localIntent6 = paramIntent.putExtra("album", str4);
      String str5 = localSharedPreferences.getString("curtitle", "");
      Intent localIntent7 = paramIntent.putExtra("track", str5);
      Intent localIntent8 = paramIntent.putExtra("albumArtFromService", true);
    }
  }

  private void removeFailureIfNecessary()
  {
    ((NotificationManager)getSystemService("notification")).cancel(2);
  }

  private void setNotificationBigContentView(Notification paramNotification, RemoteViews paramRemoteViews)
  {
    try
    {
      Field localField1 = Notification.class.getField("bigContentView");
      Field localField2 = localField1;
      try
      {
        localField2.set(paramNotification, paramRemoteViews);
        return;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
      }
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
    }
  }

  private void setUpNotificationLocked(RemoteViews paramRemoteViews, boolean paramBoolean)
  {
    Intent localIntent1 = new Intent("com.android.music.musicservicecommand.previous").setClass(this, MusicPlaybackService.class);
    PendingIntent localPendingIntent1 = PendingIntent.getService(this, 0, localIntent1, 0);
    paramRemoteViews.setOnClickPendingIntent(2131296488, localPendingIntent1);
    Intent localIntent2 = new Intent("com.android.music.musicservicecommand.togglepause").setClass(this, MusicPlaybackService.class);
    PendingIntent localPendingIntent2 = PendingIntent.getService(this, 0, localIntent2, 0);
    paramRemoteViews.setOnClickPendingIntent(2131296356, localPendingIntent2);
    Intent localIntent3 = new Intent("com.android.music.musicservicecommand.next").setClass(this, MusicPlaybackService.class);
    PendingIntent localPendingIntent3 = PendingIntent.getService(this, 0, localIntent3, 0);
    paramRemoteViews.setOnClickPendingIntent(2131296489, localPendingIntent3);
    Intent localIntent4 = new Intent("com.android.music.musicservicecommand.veto").setClass(this, MusicPlaybackService.class);
    PendingIntent localPendingIntent4 = PendingIntent.getService(this, 0, localIntent4, 0);
    paramRemoteViews.setOnClickPendingIntent(2131296538, localPendingIntent4);
    String str1 = getArtistName();
    String str2 = getTrackName();
    paramRemoteViews.setTextViewText(2131296442, str2);
    if (MusicUtils.isUnknown(str1))
      str1 = getString(2131230890);
    String str3 = getAlbumName();
    if (MusicUtils.isUnknown(str3))
      str3 = getString(2131230891);
    label290: label319: Object localObject1;
    int i;
    label344: int j;
    SongList localSongList;
    String str4;
    label420: Object localObject2;
    if (paramBoolean)
    {
      paramRemoteViews.setTextViewText(2131296400, str1);
      paramRemoteViews.setTextViewText(2131296401, str3);
      if (!this.mRetrievedNotificationHasButtons)
      {
        if (LayoutInflater.from(this).inflate(2130968685, null).findViewById(2131296489) != null)
          this.mNotificationHasButtons = true;
        this.mRetrievedNotificationHasButtons = true;
      }
      if (this.mNotificationHasButtons)
      {
        if (!isPlaying())
          break label504;
        paramRemoteViews.setImageViewResource(2131296356, 2130837644);
        if ((!paramBoolean) && (!this.mIsLandscape) && (!this.mMusicPreferences.isTabletMusicExperience()))
          break label517;
        paramRemoteViews.setViewVisibility(2131296488, 0);
      }
      if (!MusicPreferences.isHoneycombOrGreater())
        return;
      localObject1 = null;
      Resources localResources = getResources();
      if (!paramBoolean)
        break label529;
      i = 2131558428;
      j = localResources.getDimensionPixelSize(i);
      localSongList = ((DevicePlayback)this.mCurrentPlayback.get()).getMediaList();
      if ((!this.mMusicStub.isAlbumArtInService()) && (!(localSongList instanceof ExternalSongList)))
        break label551;
      if (!this.mMusicStub.isAlbumArtInService())
        break label537;
      MusicPlaybackServiceImpl localMusicPlaybackServiceImpl = this.mMusicStub;
      long l1 = this.mMusicStub.getAlbumId();
      str4 = localMusicPlaybackServiceImpl.getAlbumArtUrl(l1);
      if (str4 == null)
        break label600;
      MusicPlaybackService localMusicPlaybackService1 = this;
      int k = j;
      localObject2 = AlbumArtUtils.getExternalAlbumArtBitmap(localMusicPlaybackService1, str4, j, k, true, true, true);
    }
    while (true)
    {
      if (localObject2 != null)
      {
        paramRemoteViews.setImageViewBitmap(2131296536, (Bitmap)localObject2);
        return;
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = str1;
        arrayOfObject[1] = str3;
        String str5 = getString(2131231430, arrayOfObject);
        paramRemoteViews.setTextViewText(2131296537, str5);
        break;
        label504: paramRemoteViews.setImageViewResource(2131296356, 2130837645);
        break label290;
        label517: paramRemoteViews.setViewVisibility(2131296488, 8);
        break label319;
        label529: i = 2131558427;
        break label344;
        label537: str4 = ((ExternalSongList)localSongList).getAlbumArtUrl(this);
        break label420;
        label551: long l2 = getAlbumId();
        MusicPlaybackService localMusicPlaybackService2 = this;
        int m = j;
        int n = j;
        localObject2 = AlbumArtUtils.getArtwork(localMusicPlaybackService2, l2, m, n, true, null, null, null, true);
        continue;
      }
      int i1 = Log.w("MusicPlaybackService", "Failed to set album art for the notification");
      return;
      label600: localObject2 = localObject1;
    }
  }

  private boolean shouldIgnoreCommand(Intent paramIntent, String paramString1, String paramString2)
  {
    boolean bool = false;
    if (!((DevicePlayback)this.mCurrentPlayback.get()).isPlayingLocally())
    {
      String str1 = paramIntent.getStringExtra("device");
      if (!"any".equals(str1))
      {
        if (!"local".equals(str1))
          break label108;
        bool = true;
      }
    }
    if (bool)
    {
      String str2 = "Ignoring command " + paramString1 + " / " + paramString2 + " for non-local playback";
      int i = Log.w("MusicPlaybackService", str2);
    }
    return bool;
    label108: if (("pause".equals(paramString2)) || ("com.android.music.musicservicecommand.pause".equals(paramString1)));
    for (bool = true; ; bool = false)
      break;
  }

  /** @deprecated */
  private void updateNotification()
  {
    while (true)
    {
      try
      {
        MusicUtils.checkMainThread(this, "updateNotification called on UI thread");
        if (MusicPreferences.isHoneycombOrGreater())
        {
          if ((!this.mUIVisible) && (isPlaying()))
            this.mIsNotificationShowing = true;
          if (this.mIsNotificationShowing)
          {
            String str1 = getPackageName();
            RemoteViews localRemoteViews1 = new RemoteViews(str1, 2130968685);
            setUpNotificationLocked(localRemoteViews1, false);
            if (this.mNotification == null)
            {
              Notification localNotification1 = new Notification();
              this.mNotification = localNotification1;
              Notification localNotification2 = this.mNotification;
              int i = localNotification2.flags | 0x2;
              localNotification2.flags = i;
              this.mNotification.icon = 2130837878;
              Notification localNotification3 = this.mNotification;
              PendingIntent localPendingIntent = AppNavigation.getIntentToOpenAppWithPlaybackScreen(this);
              localNotification3.contentIntent = localPendingIntent;
            }
            this.mNotification.contentView = localRemoteViews1;
            if (MusicPreferences.isJellyBeanOrGreater())
            {
              String str2 = getPackageName();
              RemoteViews localRemoteViews2 = new RemoteViews(str2, 2130968686);
              setUpNotificationLocked(localRemoteViews2, true);
              Notification localNotification4 = this.mNotification;
              setNotificationBigContentView(localNotification4, localRemoteViews2);
            }
            Notification localNotification5 = this.mNotification;
            startForeground(1, localNotification5);
          }
          return;
        }
        if (isPlaying())
        {
          this.mIsNotificationShowing = true;
          continue;
        }
      }
      finally
      {
      }
      gotoIdleState();
    }
  }

  private void updateRemoteControlMetadata(Intent paramIntent)
  {
    MusicUtils.checkMainThread(this, "updateRemoteControlMetadata on main thread");
    if (!MusicPreferences.isICSOrGreater())
      return;
    RemoteControlClientCompat.MetadataEditorCompat localMetadataEditorCompat1 = this.mRemoteControlClient.editMetadata(true);
    String str1 = paramIntent.getStringExtra("album");
    String str2 = paramIntent.getStringExtra("artist");
    long l1 = paramIntent.getLongExtra("albumId", 65535L);
    RemoteControlClientCompat.MetadataEditorCompat localMetadataEditorCompat2 = localMetadataEditorCompat1.putString(1, str1);
    RemoteControlClientCompat.MetadataEditorCompat localMetadataEditorCompat3 = localMetadataEditorCompat1.putString(13, str2);
    RemoteControlClientCompat.MetadataEditorCompat localMetadataEditorCompat4 = localMetadataEditorCompat1.putString(2, str2);
    String str3 = paramIntent.getStringExtra("track");
    RemoteControlClientCompat.MetadataEditorCompat localMetadataEditorCompat5 = localMetadataEditorCompat1.putString(7, str3);
    long l2 = paramIntent.getLongExtra("duration", 0L);
    RemoteControlClientCompat.MetadataEditorCompat localMetadataEditorCompat6 = localMetadataEditorCompat1.putLong(9, l2);
    Object localObject1 = null;
    int i;
    int j;
    String str4;
    if (Build.VERSION.SDK_INT > 18)
    {
      i = 1024;
      j = 1024;
      localObject2 = ((DevicePlayback)this.mCurrentPlayback.get()).getMediaList();
      boolean bool = paramIntent.getBooleanExtra("albumArtFromService", false);
      if ((!bool) && (!(localObject2 instanceof ExternalSongList)))
        break label325;
      if (!bool)
        break label311;
      str4 = this.mMusicStub.getAlbumArtUrl(l1);
      label195: if (str4 == null)
        break label377;
    }
    label219: label370: label377: for (Object localObject2 = AlbumArtUtils.getExternalAlbumArtBitmap(this, str4, i, j, true, true, true); ; localObject2 = localObject1)
    {
      Object localObject3 = localObject2;
      Object localObject4;
      if (localObject3 != null)
      {
        if (((Bitmap)localObject3).getConfig() != null)
          break label370;
        int k = ((Bitmap)localObject3).getWidth();
        int m = ((Bitmap)localObject3).getHeight();
        Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
        localObject4 = Bitmap.createBitmap(k, m, localConfig);
        new Canvas((Bitmap)localObject4).drawBitmap((Bitmap)localObject3, 0.0F, 0.0F, null);
      }
      while (true)
      {
        RemoteControlClientCompat.MetadataEditorCompat localMetadataEditorCompat7 = localMetadataEditorCompat1.putBitmap(100, (Bitmap)localObject4);
        while (true)
        {
          localMetadataEditorCompat1.apply();
          return;
          i = 256;
          j = 256;
          break;
          str4 = ((ExternalSongList)localObject2).getAlbumArtUrl(this);
          break label195;
          MusicPlaybackService localMusicPlaybackService = this;
          int n = i;
          int i1 = j;
          localObject3 = AlbumArtUtils.getLockScreenArtwork(localMusicPlaybackService, l1, n, i1, true, str1, str2, true);
          break label219;
          int i2 = Log.w("MusicPlaybackService", "Failed to set album art for the lock screen");
        }
        localObject4 = localObject3;
      }
    }
  }

  private void updateRemoteControlPlaystate(boolean paramBoolean)
  {
    int i;
    if (paramBoolean)
      i = RemoteControlClientCompat.PLAYSTATE_ERROR;
    while (Gservices.getBoolean(getContentResolver(), "music_enable_rcc_playback_position", true))
    {
      RemoteControlClientCompat localRemoteControlClientCompat = this.mRemoteControlClient;
      long l = position();
      localRemoteControlClientCompat.setPlaybackState(i, l, 1.0F);
      return;
      if ((((DevicePlayback)this.mCurrentPlayback.get()).isPreparing()) && (((DevicePlayback)this.mCurrentPlayback.get()).isStreaming()))
        i = RemoteControlClientCompat.PLAYSTATE_BUFFERING;
      else if (isPlaying())
        i = RemoteControlClientCompat.PLAYSTATE_PLAYING;
      else
        i = RemoteControlClientCompat.PLAYSTATE_PAUSED;
    }
    this.mRemoteControlClient.setPlaybackState(i);
  }

  private void updateRemoteControlTransportControlFlags()
  {
    int i = RemoteControlClientCompat.FLAG_KEY_MEDIA_PREVIOUS;
    int j = RemoteControlClientCompat.FLAG_KEY_MEDIA_NEXT;
    int k = i | j;
    int m = RemoteControlClientCompat.FLAG_KEY_MEDIA_PLAY;
    int n = k | m;
    int i1 = RemoteControlClientCompat.FLAG_KEY_MEDIA_PAUSE;
    int i2 = n | i1;
    int i3 = RemoteControlClientCompat.FLAG_KEY_MEDIA_STOP;
    int i4 = i2 | i3;
    int i5 = RemoteControlClientCompat.FLAG_KEY_MEDIA_POSITION_UPDATE;
    int i6 = i4 | i5;
    this.mRemoteControlClient.setTransportControlFlags(i6);
  }

  public void cancelMix()
  {
    ((DevicePlayback)this.mCurrentPlayback.get()).cancelMix();
  }

  protected void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    StringBuilder localStringBuilder1 = new StringBuilder().append("currentPlayback:");
    Object localObject = this.mCurrentPlayback.get();
    String str1 = localObject;
    paramPrintWriter.println(str1);
    StringBuilder localStringBuilder2 = new StringBuilder().append("localPlayback:");
    LocalDevicePlayback localLocalDevicePlayback = this.mLocalPlayback;
    String str2 = localLocalDevicePlayback;
    paramPrintWriter.println(str2);
    DevicePlayback localDevicePlayback = (DevicePlayback)this.mCurrentPlayback.get();
    if (localDevicePlayback == null)
      return;
    StringBuilder localStringBuilder3 = new StringBuilder().append("Has playlist: ");
    boolean bool1 = localDevicePlayback.hasValidPlaylist();
    String str3 = bool1;
    paramPrintWriter.println(str3);
    SongList localSongList = localDevicePlayback.getMediaList();
    StringBuilder localStringBuilder4 = new StringBuilder().append("");
    int i = localDevicePlayback.getQueueSize();
    StringBuilder localStringBuilder5 = localStringBuilder4.append(i).append(" items in queue, currently at index ");
    int j = getQueuePosition();
    String str4 = j;
    paramPrintWriter.println(str4);
    if (localSongList == null)
      paramPrintWriter.println("MediaList: null");
    while (true)
    {
      paramPrintWriter.println("Currently loaded:");
      String str5 = getArtistName();
      paramPrintWriter.println(str5);
      String str6 = getAlbumName();
      paramPrintWriter.println(str6);
      String str7 = getTrackName();
      paramPrintWriter.println(str7);
      StringBuilder localStringBuilder6 = new StringBuilder().append("Playback State: ");
      DevicePlayback.State localState = localDevicePlayback.getState();
      String str8 = localState;
      paramPrintWriter.println(str8);
      StringBuilder localStringBuilder7 = new StringBuilder().append("Shuffle mode: ");
      int k = getShuffleMode();
      String str9 = k;
      paramPrintWriter.println(str9);
      StringBuilder localStringBuilder8 = new StringBuilder().append("Repeat mode: ");
      int m = getRepeatMode();
      String str10 = m;
      paramPrintWriter.println(str10);
      StringBuilder localStringBuilder9 = new StringBuilder().append("Infinite mix mode: ");
      boolean bool2 = isInfiniteMixMode();
      String str11 = bool2;
      paramPrintWriter.println(str11);
      StringBuilder localStringBuilder10 = new StringBuilder().append("Error state: ");
      boolean bool3 = localDevicePlayback.isInErrorState();
      String str12 = bool3;
      paramPrintWriter.println(str12);
      StringBuilder localStringBuilder11 = new StringBuilder().append("Error type: ");
      int n = localDevicePlayback.getErrorType();
      String str13 = n;
      paramPrintWriter.println(str13);
      StringBuilder localStringBuilder12 = new StringBuilder().append("Preparing: ");
      boolean bool4 = localDevicePlayback.isPreparing();
      String str14 = bool4;
      paramPrintWriter.println(str14);
      StringBuilder localStringBuilder13 = new StringBuilder().append("Streaming: ");
      boolean bool5 = localDevicePlayback.isStreaming();
      String str15 = bool5;
      paramPrintWriter.println(str15);
      StringBuilder localStringBuilder14 = new StringBuilder().append("Loading: ");
      boolean bool6 = localDevicePlayback.playlistLoading();
      String str16 = bool6;
      paramPrintWriter.println(str16);
      localDevicePlayback.dump(paramPrintWriter);
      MusicUtils.debugDump(paramPrintWriter);
      return;
      paramPrintWriter.println("MediaList:");
      localSongList.dump(paramPrintWriter);
    }
  }

  public long duration()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).duration();
  }

  public long getAlbumId()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getAlbumId();
  }

  public String getAlbumName()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getAlbumName();
  }

  public long getArtistId()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getArtistId();
  }

  public String getArtistName()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getArtistName();
  }

  public ContentIdentifier getAudioId()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getAudioId();
  }

  public int getAudioSessionId()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getAudioSessionId();
  }

  public TrackInfo getCurrentTrackInfo()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getCurrentTrackInfo();
  }

  public int getErrorType()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getErrorType();
  }

  public MixGenerationState getMixState()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getMixState();
  }

  public PlaybackState getPlaybackState()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getPlaybackState();
  }

  public int getPreviewPlayType()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getPreviewPlayType();
  }

  public int getQueuePosition()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getQueuePosition();
  }

  public int getQueueSize()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getQueueSize();
  }

  public int getRating()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getRating();
  }

  RemoteControlClientCompat getRemoteControlClient()
  {
    return this.mRemoteControlClient;
  }

  public int getRepeatMode()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getRepeatMode();
  }

  public String getSelectedMediaRouteId()
  {
    return this.mLocalPlayback.getSelectedMediaRouteId();
  }

  public int getShuffleMode()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getShuffleMode();
  }

  public String getSongStoreId()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getSongStoreId();
  }

  public String getSortableAlbumArtistName()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getSortableAlbumArtistName();
  }

  public String getTrackName()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).getTrackName();
  }

  public boolean isBuffering()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).isPreparing();
  }

  public boolean isCurrentSongLoaded()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).isCurrentSongLoaded();
  }

  public boolean isInfiniteMixMode()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).isInfiniteMixMode();
  }

  public boolean isPlaying()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).isPlaying();
  }

  public boolean isPlaylistLoading()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).playlistLoading();
  }

  public boolean isUIVisible()
  {
    return this.mUIVisible;
  }

  public void next()
  {
    ((DevicePlayback)this.mCurrentPlayback.get()).next();
  }

  void notifyChange(final String paramString, DevicePlayback paramDevicePlayback)
  {
    if (LOGV)
    {
      String str1 = "notifyChange: " + paramString;
      int i = Log.v("MusicPlaybackService", str1);
    }
    Object localObject = this.mCurrentPlayback.get();
    if (paramDevicePlayback != localObject)
    {
      if (!LOGV)
        return;
      int j = Log.w("MusicPlaybackService", "Received update from non-current device. Ignoring.");
      return;
    }
    if (this.mMusicStub.getAudioId() != null);
    for (int k = 1; (k == 0) && (doesIntentRequireSongInfo(paramString)); k = 0)
    {
      if (!LOGV)
        return;
      int m = Log.w("MusicPlaybackService", "Song is not loaded. Ignoring.");
      return;
    }
    final Intent localIntent1 = new Intent(paramString);
    MusicPlaybackServiceImpl localMusicPlaybackServiceImpl = this.mMusicStub;
    Intent localIntent2 = populateExtras(localIntent1, localMusicPlaybackServiceImpl, this);
    if ("com.android.music.asyncopenstart".equals(paramString))
      updateRemoteControlPlaystate(false);
    while (true)
    {
      if (LOGV)
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("Sending out broadcast: ");
        String str2 = localIntent1.getAction();
        StringBuilder localStringBuilder2 = localStringBuilder1.append(str2).append(" Extras: ");
        Bundle localBundle = localIntent1.getExtras();
        String str3 = localBundle;
        int n = Log.i("MusicPlaybackService", str3);
      }
      sendBroadcast(localIntent1);
      LoggableHandler localLoggableHandler1 = AsyncWorkers.sBackendServiceWorker;
      Runnable local8 = new Runnable()
      {
        public void run()
        {
          MusicPlaybackService localMusicPlaybackService = MusicPlaybackService.this;
          Intent localIntent = localIntent1;
          String str = paramString;
          NowPlayingWidgetProvider.notifyChange(localMusicPlaybackService, localIntent, str);
          MusicPlaybackService.this.updateNotification();
        }
      };
      AsyncWorkers.runAsync(localLoggableHandler1, local8);
      return;
      if ("com.android.music.asyncopencomplete".equals(paramString))
      {
        gotoIdleState();
        updateRemoteControlPlaystate(false);
      }
      else if ("com.android.music.playstatechanged".equals(paramString))
      {
        if (!isPlaying())
          gotoIdleState();
        updateRemoteControlPlaystate(false);
        if (!((DevicePlayback)this.mCurrentPlayback.get()).isInErrorState())
          removeFailureIfNecessary();
      }
      else if ("com.android.music.playbackfailed".equals(paramString))
      {
        updateRemoteControlPlaystate(true);
        int i1 = getErrorType();
        alertFailureIfNecessary(i1);
      }
      else if ("com.android.music.metachanged".equals(paramString))
      {
        LoggableHandler localLoggableHandler2 = AsyncWorkers.sBackendServiceWorker;
        Runnable local7 = new Runnable()
        {
          public void run()
          {
            MusicPlaybackService localMusicPlaybackService = MusicPlaybackService.this;
            Intent localIntent = localIntent1;
            localMusicPlaybackService.updateRemoteControlMetadata(localIntent);
            boolean bool = localIntent1.getBooleanExtra("inErrorState", false);
            MusicPlaybackService.this.updateRemoteControlPlaystate(bool);
          }
        };
        AsyncWorkers.runAsync(localLoggableHandler2, local7);
      }
    }
  }

  void notifyFailure(DevicePlayback paramDevicePlayback)
  {
    notifyChange("com.android.music.playbackfailed", paramDevicePlayback);
  }

  public IBinder onBind(Intent paramIntent)
  {
    this.mDelayedStopHandler.removeCallbacksAndMessages(null);
    this.mServiceInUse = true;
    return this.mMusicStub;
  }

  public void onCreate()
  {
    super.onCreate();
    String str1 = getPackageName();
    String str2 = MediaButtonIntentReceiver.class.getName();
    ComponentName localComponentName1 = new ComponentName(str1, str2);
    this.mMediaButtonReceiver = localComponentName1;
    IntentFilter localIntentFilter1 = new IntentFilter();
    localIntentFilter1.addAction("android.media.AUDIO_BECOMING_NOISY");
    BroadcastReceiver localBroadcastReceiver1 = this.mAudioNoisyReceiver;
    Intent localIntent1 = registerReceiver(localBroadcastReceiver1, localIntentFilter1);
    AudioManager localAudioManager = (AudioManager)getSystemService("audio");
    this.mAudioManager = localAudioManager;
    AudioManagerCompat localAudioManagerCompat = AudioManagerCompat.getAudioManagerCompat(this);
    this.mAudioManagerCompat = localAudioManagerCompat;
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this, this);
    this.mMusicPreferences = localMusicPreferences;
    if (getResources().getConfiguration().orientation == 2);
    for (boolean bool1 = true; ; bool1 = false)
    {
      this.mIsLandscape = bool1;
      Intent localIntent2 = new Intent("android.intent.action.MEDIA_BUTTON");
      ComponentName localComponentName2 = this.mMediaButtonReceiver;
      Intent localIntent3 = localIntent2.setComponent(localComponentName2);
      PendingIntent localPendingIntent = PendingIntent.getBroadcast(this, 0, localIntent2, 0);
      Looper localLooper = AsyncWorkers.sBackendServiceWorker.getLooper();
      RemoteControlClientCompat localRemoteControlClientCompat = new RemoteControlClientCompat(localPendingIntent, localLooper);
      this.mRemoteControlClient = localRemoteControlClientCompat;
      updateRemoteControlTransportControlFlags();
      IntentFilter localIntentFilter2 = new IntentFilter();
      localIntentFilter2.addAction("com.android.music.musicservicecommand");
      localIntentFilter2.addAction("com.android.music.musicservicecommand.togglepause");
      localIntentFilter2.addAction("com.android.music.musicservicecommand.pause");
      localIntentFilter2.addAction("com.android.music.musicservicecommand.next");
      localIntentFilter2.addAction("com.android.music.musicservicecommand.previous");
      localIntentFilter2.addAction("com.android.music.playstatusrequest");
      BroadcastReceiver localBroadcastReceiver2 = this.mIntentReceiver;
      Intent localIntent4 = registerReceiver(localBroadcastReceiver2, localIntentFilter2);
      IntentFilter localIntentFilter3 = new IntentFilter();
      IntentFilter localIntentFilter4 = new IntentFilter();
      localIntentFilter4.addAction("android.intent.action.CONFIGURATION_CHANGED");
      BroadcastReceiver localBroadcastReceiver3 = this.mOrientationMonitor;
      Intent localIntent5 = registerReceiver(localBroadcastReceiver3, localIntentFilter4);
      Message localMessage = this.mDelayedStopHandler.obtainMessage();
      boolean bool2 = this.mDelayedStopHandler.sendMessageDelayed(localMessage, 6000L);
      LocalDevicePlayback localLocalDevicePlayback1 = new LocalDevicePlayback(this);
      this.mLocalPlayback = localLocalDevicePlayback1;
      this.mLocalPlayback.onCreate();
      AtomicReference localAtomicReference = this.mCurrentPlayback;
      LocalDevicePlayback localLocalDevicePlayback2 = this.mLocalPlayback;
      localAtomicReference.set(localLocalDevicePlayback2);
      if (LOGV)
        int i = Log.d("MusicPlaybackService", "Playback service created");
      if (!Gservices.getBoolean(getContentResolver(), "music_enable_rcc_playback_position", true))
        return;
      this.mRemoteControlClient.setPlaybackPositionUpdateListener(this);
      this.mRemoteControlClient.setOnGetPlaybackPositionListener(this);
      return;
    }
  }

  public void onDestroy()
  {
    if (isPlaying())
      int i = Log.e("MusicPlaybackService", "Service being destroyed while still playing.");
    if (LOGV)
      int j = Log.d("MusicPlaybackService", "Playback service destroyed");
    this.mRemoteControlClient.setPlaybackPositionUpdateListener(null);
    this.mRemoteControlClient.setOnGetPlaybackPositionListener(null);
    AudioManagerCompat localAudioManagerCompat = this.mAudioManagerCompat;
    RemoteControlClientCompat localRemoteControlClientCompat = this.mRemoteControlClient;
    localAudioManagerCompat.unregisterRemoteControlClient(localRemoteControlClientCompat);
    this.mDelayedStopHandler.removeCallbacksAndMessages(null);
    this.mDeviceSwitchHandler.removeCallbacksAndMessages(null);
    BroadcastReceiver localBroadcastReceiver1 = this.mIntentReceiver;
    unregisterReceiver(localBroadcastReceiver1);
    BroadcastReceiver localBroadcastReceiver2 = this.mOrientationMonitor;
    unregisterReceiver(localBroadcastReceiver2);
    BroadcastReceiver localBroadcastReceiver3 = this.mAudioNoisyReceiver;
    unregisterReceiver(localBroadcastReceiver3);
    synchronized (this.mCurrentPlayback)
    {
      this.mLocalPlayback.onDestroy();
      this.mLocalPlayback = null;
      MusicPreferences.releaseMusicPreferences(this);
      super.onDestroy();
      return;
    }
  }

  public long onGetPlaybackPosition()
  {
    return position();
  }

  public void onPlaybackPositionUpdate(long paramLong)
  {
    long l = seek(paramLong);
  }

  public void onRebind(Intent paramIntent)
  {
    this.mDelayedStopHandler.removeCallbacksAndMessages(null);
    this.mServiceInUse = true;
  }

  /** @deprecated */
  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString)
  {
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    this.mServiceStartId = paramInt2;
    this.mDelayedStopHandler.removeCallbacksAndMessages(null);
    Intent localIntent = paramIntent;
    String str1;
    String str2;
    if (localIntent != null)
    {
      str1 = localIntent.getAction();
      str2 = localIntent.getStringExtra("command");
      MusicUtils.debugLog("onStartCommand " + str1 + " / " + str2);
      if (!shouldIgnoreCommand(localIntent, str1, str2))
      {
        if ((!"next".equals(str2)) && (!"com.android.music.musicservicecommand.next".equals(str1)))
          break label176;
        next();
      }
    }
    while (true)
    {
      this.mDelayedStopHandler.removeCallbacksAndMessages(null);
      Message localMessage = this.mDelayedStopHandler.obtainMessage();
      boolean bool = this.mDelayedStopHandler.sendMessageDelayed(localMessage, 6000L);
      if (paramIntent != null)
      {
        String str3 = MediaButtonIntentReceiver.RELEASE_RECEIVER_LOCK;
        if ((paramIntent.getBooleanExtra(str3, false)) && (MediaButtonIntentReceiver.sWakeLock != null))
          MediaButtonIntentReceiver.sWakeLock.release();
      }
      return 1;
      label176: if (("previous".equals(str2)) || ("com.android.music.musicservicecommand.previous".equals(str1)))
      {
        prev();
      }
      else if (("togglepause".equals(str2)) || ("com.android.music.musicservicecommand.togglepause".equals(str1)))
      {
        if (isPlaying())
        {
          pause();
          if (localIntent.getBooleanExtra("removeNotification", false))
          {
            stopForeground(true);
            this.mIsNotificationShowing = false;
          }
        }
        else if ((isPlaylistLoading()) || (((DevicePlayback)this.mCurrentPlayback.get()).getQueueSize() > 0))
        {
          play();
        }
        else
        {
          setShuffleMode(1);
          AllSongsList localAllSongsList = new AllSongsList(0);
          open(localAllSongsList, -1, true);
        }
      }
      else if (("pause".equals(str2)) || ("com.android.music.musicservicecommand.pause".equals(str1)))
      {
        pause();
        stopForeground(true);
        this.mIsNotificationShowing = false;
      }
      else if ("play".equals(str2))
      {
        play();
      }
      else if ("stop".equals(str2))
      {
        stop();
        stopForeground(true);
        this.mIsNotificationShowing = false;
      }
      else if ("com.android.music.musicservicecommand.veto".equals(str1))
      {
        pause();
        stopForeground(true);
        this.mIsNotificationShowing = false;
      }
      else if ("com.android.music.musicservicecommand.rating".equals(str1))
      {
        final int i = localIntent.getIntExtra("rating", -1);
        if (i >= 0)
        {
          LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
          Runnable local5 = new Runnable()
          {
            public void run()
            {
              MusicPlaybackService localMusicPlaybackService = MusicPlaybackService.this;
              int i = i;
              localMusicPlaybackService.setRating(i);
            }
          };
          AsyncWorkers.runAsync(localLoggableHandler, local5);
        }
      }
      else if ("com.google.android.music.playSongList".equals(str1))
      {
        SongList localSongList = (SongList)localIntent.getParcelableExtra("songlist");
        if (localSongList == null)
        {
          int j = Log.wtf("MusicPlaybackService", "Missing song list");
        }
        else
        {
          int k = localIntent.getIntExtra("position", -1);
          open(localSongList, k, true);
        }
      }
    }
  }

  public boolean onUnbind(Intent paramIntent)
  {
    this.mServiceInUse = false;
    ((DevicePlayback)this.mCurrentPlayback.get()).saveState();
    DevicePlayback.State localState = ((DevicePlayback)this.mCurrentPlayback.get()).getState();
    int[] arrayOfInt = 12.$SwitchMap$com$google$android$music$playback$DevicePlayback$State;
    int i = localState.ordinal();
    switch (arrayOfInt[i])
    {
    case 1:
    case 2:
    default:
    case 3:
    case 4:
    case 5:
    }
    while (true)
    {
      return true;
      Message localMessage = this.mDelayedStopHandler.obtainMessage();
      boolean bool = this.mDelayedStopHandler.sendMessageDelayed(localMessage, 6000L);
      continue;
      int j = this.mServiceStartId;
      stopSelf(j);
    }
  }

  public void open(SongList paramSongList, int paramInt, boolean paramBoolean)
  {
    AudioManager localAudioManager = this.mAudioManager;
    ComponentName localComponentName = this.mMediaButtonReceiver;
    localAudioManager.registerMediaButtonEventReceiver(localComponentName);
    AudioManagerCompat localAudioManagerCompat = this.mAudioManagerCompat;
    RemoteControlClientCompat localRemoteControlClientCompat = this.mRemoteControlClient;
    localAudioManagerCompat.registerRemoteControlClient(localRemoteControlClientCompat);
    addToRecentAsync(paramSongList);
    ((DevicePlayback)this.mCurrentPlayback.get()).open(paramSongList, paramInt, paramBoolean);
  }

  public void openAndQueue(SongList paramSongList, int paramInt)
  {
    AudioManager localAudioManager = this.mAudioManager;
    ComponentName localComponentName = this.mMediaButtonReceiver;
    localAudioManager.registerMediaButtonEventReceiver(localComponentName);
    AudioManagerCompat localAudioManagerCompat = this.mAudioManagerCompat;
    RemoteControlClientCompat localRemoteControlClientCompat = this.mRemoteControlClient;
    localAudioManagerCompat.registerRemoteControlClient(localRemoteControlClientCompat);
    addToRecentAsync(paramSongList);
    ((DevicePlayback)this.mCurrentPlayback.get()).openAndQueue(paramSongList, paramInt);
  }

  public void openMix(MixDescriptor paramMixDescriptor)
  {
    ((DevicePlayback)this.mCurrentPlayback.get()).openMix(paramMixDescriptor);
  }

  public void pause()
  {
    ((DevicePlayback)this.mCurrentPlayback.get()).pause();
  }

  public void play()
  {
    AudioManager localAudioManager = this.mAudioManager;
    ComponentName localComponentName = this.mMediaButtonReceiver;
    localAudioManager.registerMediaButtonEventReceiver(localComponentName);
    AudioManagerCompat localAudioManagerCompat = this.mAudioManagerCompat;
    RemoteControlClientCompat localRemoteControlClientCompat = this.mRemoteControlClient;
    localAudioManagerCompat.registerRemoteControlClient(localRemoteControlClientCompat);
    ((DevicePlayback)this.mCurrentPlayback.get()).play();
    this.mNotificationCanceller.removeMessages(1);
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local9 = new Runnable()
    {
      public void run()
      {
        MusicPlaybackService.this.updateNotification();
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local9);
  }

  public long position()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).position();
  }

  public void prev()
  {
    ((DevicePlayback)this.mCurrentPlayback.get()).prev();
  }

  public void refreshRadio()
  {
    ((DevicePlayback)this.mCurrentPlayback.get()).refreshRadio();
  }

  public void registerMusicCastMediaRouterCallback(IMusicCastMediaRouterCallback paramIMusicCastMediaRouterCallback)
  {
    this.mLocalPlayback.registerMusicCastMediaRouterCallback(paramIMusicCastMediaRouterCallback);
  }

  public long seek(long paramLong)
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).seek(paramLong);
  }

  public void setMediaRoute(boolean paramBoolean, String paramString)
  {
    this.mLocalPlayback.setMediaRoute(paramBoolean, paramString);
  }

  public void setMediaRouteVolume(String paramString, double paramDouble)
  {
    this.mLocalPlayback.setMediaRouteVolume(paramString, paramDouble);
  }

  public void setQueuePosition(int paramInt)
  {
    ((DevicePlayback)this.mCurrentPlayback.get()).setQueuePosition(paramInt);
  }

  public void setRating(int paramInt)
  {
    ((DevicePlayback)this.mCurrentPlayback.get()).setRating(paramInt);
  }

  public void setRepeatMode(int paramInt)
  {
    ((DevicePlayback)this.mCurrentPlayback.get()).setRepeatMode(paramInt);
  }

  public void setShuffleMode(int paramInt)
  {
    ((DevicePlayback)this.mCurrentPlayback.get()).setShuffleMode(paramInt);
  }

  void setUIVisible(boolean paramBoolean)
  {
    this.mUIVisible = paramBoolean;
    if (paramBoolean)
    {
      AudioManager localAudioManager = this.mAudioManager;
      ComponentName localComponentName = this.mMediaButtonReceiver;
      localAudioManager.registerMediaButtonEventReceiver(localComponentName);
      AudioManagerCompat localAudioManagerCompat = this.mAudioManagerCompat;
      RemoteControlClientCompat localRemoteControlClientCompat = this.mRemoteControlClient;
      localAudioManagerCompat.registerRemoteControlClient(localRemoteControlClientCompat);
    }
    if ((paramBoolean) && (MusicPreferences.isHoneycombOrGreater()))
    {
      stopForeground(true);
      this.mIsNotificationShowing = false;
      return;
    }
    if (paramBoolean)
      return;
    if ((!isBuffering()) && (!isPlaying()))
      return;
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local10 = new Runnable()
    {
      public void run()
      {
        MusicPlaybackService.this.updateNotification();
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local10);
  }

  public void shuffleAll()
  {
    AudioManager localAudioManager = this.mAudioManager;
    ComponentName localComponentName = this.mMediaButtonReceiver;
    localAudioManager.registerMediaButtonEventReceiver(localComponentName);
    AudioManagerCompat localAudioManagerCompat = this.mAudioManagerCompat;
    RemoteControlClientCompat localRemoteControlClientCompat = this.mRemoteControlClient;
    localAudioManagerCompat.registerRemoteControlClient(localRemoteControlClientCompat);
    ((DevicePlayback)this.mCurrentPlayback.get()).shuffleAll();
  }

  public void shuffleOnDevice()
  {
    AudioManager localAudioManager = this.mAudioManager;
    ComponentName localComponentName = this.mMediaButtonReceiver;
    localAudioManager.registerMediaButtonEventReceiver(localComponentName);
    AudioManagerCompat localAudioManagerCompat = this.mAudioManagerCompat;
    RemoteControlClientCompat localRemoteControlClientCompat = this.mRemoteControlClient;
    localAudioManagerCompat.registerRemoteControlClient(localRemoteControlClientCompat);
    ((DevicePlayback)this.mCurrentPlayback.get()).shuffleOnDevice();
  }

  public void shuffleSongs(SongList paramSongList)
  {
    AudioManager localAudioManager = this.mAudioManager;
    ComponentName localComponentName = this.mMediaButtonReceiver;
    localAudioManager.registerMediaButtonEventReceiver(localComponentName);
    AudioManagerCompat localAudioManagerCompat = this.mAudioManagerCompat;
    RemoteControlClientCompat localRemoteControlClientCompat = this.mRemoteControlClient;
    localAudioManagerCompat.registerRemoteControlClient(localRemoteControlClientCompat);
    addToRecentAsync(paramSongList);
    ((DevicePlayback)this.mCurrentPlayback.get()).shuffleSongs(paramSongList);
  }

  public void stop()
  {
    ((DevicePlayback)this.mCurrentPlayback.get()).stop();
    this.mNotificationCanceller.removeMessages(1);
    Message localMessage = this.mNotificationCanceller.obtainMessage(1);
    Bundle localBundle = new Bundle();
    long l = System.currentTimeMillis();
    localBundle.putLong("cancel_notification", l);
    localMessage.setData(localBundle);
    boolean bool = this.mNotificationCanceller.sendMessageDelayed(localMessage, 18000000L);
  }

  public boolean supportsRating()
  {
    return ((DevicePlayback)this.mCurrentPlayback.get()).supportsRating();
  }

  public void updateMediaRouteVolume(String paramString, double paramDouble)
  {
    this.mLocalPlayback.updateMediaRouteVolume(paramString, paramDouble);
  }

  public static class MusicPlaybackServiceImpl extends IMusicPlaybackService.Stub
  {
    AtomicReference<MusicPlaybackService> mService;

    MusicPlaybackServiceImpl(MusicPlaybackService paramMusicPlaybackService)
    {
      AtomicReference localAtomicReference = new AtomicReference(paramMusicPlaybackService);
      this.mService = localAtomicReference;
    }

    public boolean addDownloadProgressListener(ContentIdentifier paramContentIdentifier, IDownloadProgressListener paramIDownloadProgressListener)
    {
      return ((MusicPlaybackService)this.mService.get()).mLocalPlayback.addDownloadProgressListener(paramContentIdentifier, paramIDownloadProgressListener);
    }

    public void cancelMix()
      throws RemoteException
    {
      ((MusicPlaybackService)this.mService.get()).cancelMix();
    }

    public void clearQueue()
      throws RemoteException
    {
      ((DevicePlayback)((MusicPlaybackService)this.mService.get()).mCurrentPlayback.get()).clearQueue();
    }

    public void disableGroupPlay()
      throws RemoteException
    {
      ((DevicePlayback)((MusicPlaybackService)this.mService.get()).mCurrentPlayback.get()).disableGroupPlay();
    }

    public long duration()
    {
      return ((MusicPlaybackService)this.mService.get()).duration();
    }

    public String getAlbumArtUrl(long paramLong)
    {
      return ((DevicePlayback)((MusicPlaybackService)this.mService.get()).mCurrentPlayback.get()).getAlbumArtUrl(paramLong);
    }

    public long getAlbumId()
    {
      return ((MusicPlaybackService)this.mService.get()).getAlbumId();
    }

    public String getAlbumName()
    {
      return ((MusicPlaybackService)this.mService.get()).getAlbumName();
    }

    public long getArtistId()
    {
      return ((MusicPlaybackService)this.mService.get()).getArtistId();
    }

    public String getArtistName()
    {
      return ((MusicPlaybackService)this.mService.get()).getArtistName();
    }

    public ContentIdentifier getAudioId()
    {
      return ((MusicPlaybackService)this.mService.get()).getAudioId();
    }

    public int getAudioSessionId()
    {
      return ((MusicPlaybackService)this.mService.get()).getAudioSessionId();
    }

    public TrackInfo getCurrentTrackInfo()
      throws RemoteException
    {
      return ((MusicPlaybackService)this.mService.get()).getCurrentTrackInfo();
    }

    public int getErrorType()
    {
      return ((DevicePlayback)((MusicPlaybackService)this.mService.get()).mCurrentPlayback.get()).getErrorType();
    }

    public long getLastUserInteractionTime()
    {
      return ((DevicePlayback)((MusicPlaybackService)this.mService.get()).mCurrentPlayback.get()).getLastUserInteractionTime();
    }

    public SongList getMediaList()
    {
      return ((DevicePlayback)((MusicPlaybackService)this.mService.get()).mCurrentPlayback.get()).getMediaList();
    }

    public MixGenerationState getMixState()
      throws RemoteException
    {
      return ((MusicPlaybackService)this.mService.get()).getMixState();
    }

    public PlaybackState getPlaybackState()
      throws RemoteException
    {
      return ((MusicPlaybackService)this.mService.get()).getPlaybackState();
    }

    public int getPreviewPlayType()
      throws RemoteException
    {
      return ((MusicPlaybackService)this.mService.get()).getPreviewPlayType();
    }

    public int getQueuePosition()
    {
      return ((MusicPlaybackService)this.mService.get()).getQueuePosition();
    }

    public int getQueueSize()
    {
      return ((MusicPlaybackService)this.mService.get()).getQueueSize();
    }

    public int getRating()
    {
      return ((MusicPlaybackService)this.mService.get()).getRating();
    }

    public int getRepeatMode()
    {
      return ((MusicPlaybackService)this.mService.get()).getRepeatMode();
    }

    public String getSelectedMediaRouteId()
    {
      return ((MusicPlaybackService)this.mService.get()).getSelectedMediaRouteId();
    }

    public int getShuffleMode()
    {
      return ((MusicPlaybackService)this.mService.get()).getShuffleMode();
    }

    public String getSongStoreId()
    {
      return ((MusicPlaybackService)this.mService.get()).getSongStoreId();
    }

    public String getSortableAlbumArtistName()
    {
      return ((MusicPlaybackService)this.mService.get()).getSortableAlbumArtistName();
    }

    public String getTrackName()
    {
      return ((MusicPlaybackService)this.mService.get()).getTrackName();
    }

    public boolean hasLocal()
    {
      return ((MusicPlaybackService)this.mService.get()).hasLocal();
    }

    public boolean hasRemote()
    {
      return ((MusicPlaybackService)this.mService.get()).hasRemote();
    }

    public boolean hasValidPlaylist()
    {
      return ((DevicePlayback)((MusicPlaybackService)this.mService.get()).mCurrentPlayback.get()).hasValidPlaylist();
    }

    public boolean isAlbumArtInService()
    {
      return false;
    }

    public boolean isCurrentSongLoaded()
    {
      return ((MusicPlaybackService)this.mService.get()).isCurrentSongLoaded();
    }

    public boolean isInErrorState()
    {
      return ((DevicePlayback)((MusicPlaybackService)this.mService.get()).mCurrentPlayback.get()).isInErrorState();
    }

    public boolean isInFatalErrorState()
    {
      return ((DevicePlayback)((MusicPlaybackService)this.mService.get()).mCurrentPlayback.get()).isInFatalErrorState();
    }

    public boolean isInIniniteMixMode()
      throws RemoteException
    {
      return ((MusicPlaybackService)this.mService.get()).isInfiniteMixMode();
    }

    public boolean isPlaying()
    {
      return ((MusicPlaybackService)this.mService.get()).isPlaying();
    }

    public boolean isPlaylistLoading()
    {
      return ((DevicePlayback)((MusicPlaybackService)this.mService.get()).mCurrentPlayback.get()).playlistLoading();
    }

    public boolean isPreparing()
    {
      return ((DevicePlayback)((MusicPlaybackService)this.mService.get()).mCurrentPlayback.get()).isPreparing();
    }

    public boolean isStreaming()
    {
      return ((DevicePlayback)((MusicPlaybackService)this.mService.get()).mCurrentPlayback.get()).isStreaming();
    }

    public boolean isStreamingFullyBuffered()
      throws RemoteException
    {
      return ((DevicePlayback)((MusicPlaybackService)this.mService.get()).mCurrentPlayback.get()).isStreamingFullyBuffered();
    }

    public void next()
    {
      ((MusicPlaybackService)this.mService.get()).next();
    }

    public void open(SongList paramSongList, int paramInt, boolean paramBoolean)
    {
      ((MusicPlaybackService)this.mService.get()).open(paramSongList, paramInt, paramBoolean);
    }

    public void openAndQueue(SongList paramSongList, int paramInt)
    {
      ((MusicPlaybackService)this.mService.get()).openAndQueue(paramSongList, paramInt);
    }

    public void openMix(MixDescriptor paramMixDescriptor)
      throws RemoteException
    {
      ((MusicPlaybackService)this.mService.get()).openMix(paramMixDescriptor);
    }

    public void pause()
    {
      ((MusicPlaybackService)this.mService.get()).pause();
    }

    public void play()
    {
      ((MusicPlaybackService)this.mService.get()).play();
    }

    public long position()
    {
      return ((MusicPlaybackService)this.mService.get()).position();
    }

    public void prev()
    {
      ((MusicPlaybackService)this.mService.get()).prev();
    }

    public void refreshRadio()
    {
      ((MusicPlaybackService)this.mService.get()).refreshRadio();
    }

    public void registerMusicCastMediaRouterCallback(IMusicCastMediaRouterCallback paramIMusicCastMediaRouterCallback)
      throws RemoteException
    {
      ((MusicPlaybackService)this.mService.get()).registerMusicCastMediaRouterCallback(paramIMusicCastMediaRouterCallback);
    }

    public void removeDownloadProgressListener(IDownloadProgressListener paramIDownloadProgressListener)
    {
      ((MusicPlaybackService)this.mService.get()).mLocalPlayback.removeDownloadProgressListener(paramIDownloadProgressListener);
    }

    public long seek(long paramLong)
    {
      return ((MusicPlaybackService)this.mService.get()).seek(paramLong);
    }

    public void setMediaRoute(boolean paramBoolean, String paramString)
    {
      ((MusicPlaybackService)this.mService.get()).setMediaRoute(paramBoolean, paramString);
    }

    public void setMediaRouteVolume(String paramString, double paramDouble)
    {
      ((MusicPlaybackService)this.mService.get()).setMediaRouteVolume(paramString, paramDouble);
    }

    public void setQueuePosition(int paramInt)
    {
      ((MusicPlaybackService)this.mService.get()).setQueuePosition(paramInt);
    }

    public void setRating(int paramInt)
    {
      ((MusicPlaybackService)this.mService.get()).setRating(paramInt);
    }

    public void setRepeatMode(int paramInt)
    {
      ((MusicPlaybackService)this.mService.get()).setRepeatMode(paramInt);
    }

    public void setShuffleMode(int paramInt)
    {
      ((MusicPlaybackService)this.mService.get()).setShuffleMode(paramInt);
    }

    public void setUIVisible(boolean paramBoolean)
    {
      ((MusicPlaybackService)this.mService.get()).setUIVisible(paramBoolean);
    }

    public void shuffleAll()
    {
      ((MusicPlaybackService)this.mService.get()).shuffleAll();
    }

    public void shuffleOnDevice()
      throws RemoteException
    {
      ((MusicPlaybackService)this.mService.get()).shuffleOnDevice();
    }

    public void shuffleSongs(SongList paramSongList)
    {
      ((MusicPlaybackService)this.mService.get()).shuffleSongs(paramSongList);
    }

    public void stop()
    {
      ((MusicPlaybackService)this.mService.get()).stop();
    }

    public boolean supportsRating()
    {
      return ((MusicPlaybackService)this.mService.get()).supportsRating();
    }

    public void updateMediaRouteVolume(String paramString, double paramDouble)
    {
      ((MusicPlaybackService)this.mService.get()).updateMediaRouteVolume(paramString, paramDouble);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.playback.MusicPlaybackService
 * JD-Core Version:    0.6.2
 */