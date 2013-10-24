package com.google.android.music.playback;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteException;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.MediaStore.Audio.Media;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouteSelector.Builder;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.Callback;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Pair;
import com.google.android.gsf.Gservices;
import com.google.android.music.StrictShuffler;
import com.google.android.music.cast.CastTokenClient;
import com.google.android.music.cast.RemoteAsyncMediaPlayer;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.download.DownloadProgress;
import com.google.android.music.download.DownloadQueueService;
import com.google.android.music.download.IDownloadProgressListener;
import com.google.android.music.download.IDownloadQueueManager;
import com.google.android.music.download.IDownloadQueueManager.Stub;
import com.google.android.music.download.cache.CacheService;
import com.google.android.music.download.cache.ICacheManager;
import com.google.android.music.download.cache.ICacheManager.Stub;
import com.google.android.music.download.cache.IDeleteFilter;
import com.google.android.music.download.cache.IDeleteFilter.Stub;
import com.google.android.music.download.cache.OutOfSpaceException;
import com.google.android.music.download.stream.DownloadRequestException;
import com.google.android.music.download.stream.StreamingClient;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.AllOnDeviceSongsList;
import com.google.android.music.medialist.AllSongsList;
import com.google.android.music.medialist.ExternalSongList;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.medialist.MediaList.MediaCursor;
import com.google.android.music.medialist.PlayQueueSongList;
import com.google.android.music.medialist.PlaylistSongList;
import com.google.android.music.medialist.SelectedSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.mix.MixDescriptor;
import com.google.android.music.mix.MixDescriptor.Type;
import com.google.android.music.mix.MixGenerationState;
import com.google.android.music.mix.MixGenerationState.State;
import com.google.android.music.mix.PlayQueueFeeder;
import com.google.android.music.mix.PlayQueueFeeder.PostProcessingAction;
import com.google.android.music.mix.PlayQueueFeederListener;
import com.google.android.music.net.IStreamabilityChangeListener;
import com.google.android.music.net.IStreamabilityChangeListener.Stub;
import com.google.android.music.net.NetworkMonitorServiceConnection;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent.XAudio;
import com.google.android.music.store.MusicFile;
import com.google.android.music.store.PlayQueueAddResult;
import com.google.android.music.store.PlayQueueInsertResult;
import com.google.android.music.store.Store;
import com.google.android.music.utils.DbUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.PostFroyoUtils.EnvironmentCompat;
import com.google.android.music.utils.SafeServiceConnection;
import com.google.android.music.utils.SystemUtils;
import com.google.android.music.utils.async.AsyncWorkers;
import com.google.common.collect.Lists;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class LocalDevicePlayback extends DevicePlayback
{
  private static volatile Boolean mDisableGaplessOverride = null;
  private final boolean LOGV;
  private PowerManager.WakeLock mAsyncWakeLock;
  private AudioManager.OnAudioFocusChangeListener mAudioFocusListener;
  private ContentIdentifier mAudioId;
  private AudioManager mAudioManager;
  private volatile ICacheManager mCacheManager;
  private final SafeServiceConnection mCacheServiceConnection;
  private final CastTokenClient mCastTokenClient;
  private AtomicReference<Cursor> mCurrentSongMetaDataCursor;
  String[] mCursorCols;
  private final IDeleteFilter mDeleteFilter;
  private SafeServiceConnection mDownloadManagerSafeConnection;
  private volatile IDownloadQueueManager mDownloadQueueManager;
  private MusicEventLogger mEventLogger;
  private LinkedList<Integer> mFuture;
  private LinkedList<Integer> mHistory;
  private volatile boolean mIsStreamingEnabled;
  private volatile boolean mIsSupposedToBePlaying;
  private long mLastPlayQueueGroupId;
  private int mLastPlayQueueGroupPosition;
  private volatile long mLastUserInteractionTime;
  private long mListItemId;
  private SongList mMediaList;
  private int mMediaMountedCount;
  private volatile String mMediaRouteSessionId;
  private MediaRouter mMediaRouter;
  private final MediaRouter.Callback mMediaRouterCallback;
  private Handler mMediaplayerHandler;
  private volatile MixGenerationState mMixGenerationState;
  private volatile IMusicCastMediaRouterCallback mMusicCastMediaRouterCallback;
  private final NetworkMonitorServiceConnection mNetworkMonitorServiceConnection;
  private ContentIdentifier mNextAudioId;
  private int mNextPlayPos;
  private AsyncMediaPlayer mNextPlayer;
  private volatile int mOpenFailedCounter;
  private volatile boolean mPausedByTransientLossOfFocus;
  private final MediaListWrapper mPlayList;
  private int mPlayPos;
  private volatile PlayQueueFeeder mPlayQueueFeeder;
  private final PlayQueueFeederListener mPlayQueueFeederListener;
  private AsyncMediaPlayer mPlayer;
  private volatile boolean mPrequeueItems;
  private volatile PreviewPlaybackInfo mPreviewPlaybackInfo;
  final RemoteCallbackList<IDownloadProgressListener> mProgressListeners;
  private boolean mQueueIsSaveable;
  private final StrictShuffler mRand;
  private long mReloadedQueueSeekPos;
  private int mRepeatMode;
  private ContentIdentifier mSavedAudioId;
  private volatile String mSelectedRouteId;
  ServiceHooks mServiceHooks;
  private BroadcastReceiver mSharedPreviewPlayListener;
  private int mShuffleMode;
  private final IStreamabilityChangeListener mStreamabilityChangeListener;
  private volatile StreamingClient mStreamingClient;
  private final Object mStreamingClientLock;
  private BroadcastReceiver mUnmountReceiver;
  private volatile boolean mUseLocalPlayer;
  private PowerManager.WakeLock mWakeLock;

  public LocalDevicePlayback(MusicPlaybackService paramMusicPlaybackService)
  {
    super(paramMusicPlaybackService);
    String[] arrayOfString = new String[19];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "artist";
    arrayOfString[2] = "album";
    arrayOfString[3] = "title";
    arrayOfString[4] = "album_id";
    arrayOfString[5] = "AlbumArtistId";
    arrayOfString[6] = "AlbumArtist";
    arrayOfString[7] = "artistSort";
    arrayOfString[8] = "is_podcast";
    arrayOfString[9] = "bookmark";
    arrayOfString[10] = "duration";
    arrayOfString[11] = "hasRemote";
    arrayOfString[12] = "hasLocal";
    arrayOfString[13] = "Rating";
    arrayOfString[14] = "SourceId";
    arrayOfString[15] = "SourceAccount";
    arrayOfString[16] = "StoreId";
    arrayOfString[17] = "domainParam";
    arrayOfString[18] = "Size";
    this.mCursorCols = arrayOfString;
    this.mShuffleMode = 0;
    this.mRepeatMode = 0;
    this.mMediaMountedCount = 0;
    this.mMediaList = null;
    LinkedList localLinkedList1 = Lists.newLinkedList();
    this.mFuture = localLinkedList1;
    LinkedList localLinkedList2 = Lists.newLinkedList();
    this.mHistory = localLinkedList2;
    AtomicReference localAtomicReference = new AtomicReference();
    this.mCurrentSongMetaDataCursor = localAtomicReference;
    this.mPlayPos = -1;
    this.mNextPlayPos = -1;
    this.mAudioId = null;
    this.mSavedAudioId = null;
    this.mNextAudioId = null;
    this.mListItemId = 0L;
    boolean bool = DebugUtils.isLoggable(DebugUtils.MusicTag.PLAYBACK_SERVICE);
    this.LOGV = bool;
    StrictShuffler localStrictShuffler = new StrictShuffler();
    this.mRand = localStrictShuffler;
    this.mOpenFailedCounter = 0;
    this.mPausedByTransientLossOfFocus = false;
    this.mUnmountReceiver = null;
    this.mIsSupposedToBePlaying = false;
    this.mQueueIsSaveable = true;
    this.mReloadedQueueSeekPos = 65535L;
    Object localObject = new Object();
    this.mStreamingClientLock = localObject;
    this.mLastUserInteractionTime = 0L;
    this.mIsStreamingEnabled = false;
    this.mUseLocalPlayer = true;
    this.mSelectedRouteId = null;
    this.mMediaRouteSessionId = null;
    this.mPrequeueItems = false;
    this.mMusicCastMediaRouterCallback = null;
    MediaRouter.Callback local1 = new MediaRouter.Callback()
    {
      public void onRouteVolumeChanged(MediaRouter paramAnonymousMediaRouter, MediaRouter.RouteInfo paramAnonymousRouteInfo)
      {
        if (LocalDevicePlayback.this.LOGV)
        {
          String str1 = " onRouteVolumeChanged: route=" + paramAnonymousRouteInfo;
          Log.d("LocalDevicePlayback", str1);
        }
        IMusicCastMediaRouterCallback localIMusicCastMediaRouterCallback = LocalDevicePlayback.this.mMusicCastMediaRouterCallback;
        if (localIMusicCastMediaRouterCallback != null)
          try
          {
            double d1 = paramAnonymousRouteInfo.getVolume();
            double d2 = paramAnonymousRouteInfo.getVolumeMax();
            double d3 = d1 / d2;
            String str2 = paramAnonymousRouteInfo.getId();
            localIMusicCastMediaRouterCallback.onRouteVolumeChanged(str2, d3);
            return;
          }
          catch (RemoteException localRemoteException)
          {
            Log.w("LocalDevicePlayback", "Remote call onRouteVolumeChanged failed", localRemoteException);
            return;
          }
        Log.w("LocalDevicePlayback", "No IMusicCastMediaRouterCallback is registered");
      }
    };
    this.mMediaRouterCallback = local1;
    IStreamabilityChangeListener.Stub local2 = new IStreamabilityChangeListener.Stub()
    {
      public void onStreamabilityChanged(boolean paramAnonymousBoolean)
        throws RemoteException
      {
        if (LocalDevicePlayback.this.mIsStreamingEnabled != paramAnonymousBoolean)
          return;
        boolean bool1 = LocalDevicePlayback.access$202(LocalDevicePlayback.this, paramAnonymousBoolean);
        if (LocalDevicePlayback.this.mNextPlayer == null)
          return;
        if (LocalDevicePlayback.this.LOGV)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("Streamability changed to :");
          boolean bool2 = LocalDevicePlayback.this.mIsStreamingEnabled;
          String str = bool2 + ". Updating the next track";
          Log.d("LocalDevicePlayback", str);
        }
        LocalDevicePlayback.this.setNextTrack();
      }
    };
    this.mStreamabilityChangeListener = local2;
    IDeleteFilter.Stub local3 = new IDeleteFilter.Stub()
    {
      public ContentIdentifier[] getFilteredIds()
        throws RemoteException
      {
        if (LocalDevicePlayback.this.mStreamingClient != null);
        for (ContentIdentifier[] arrayOfContentIdentifier = LocalDevicePlayback.this.mStreamingClient.getFilteredIds(); ; arrayOfContentIdentifier = null)
          return arrayOfContentIdentifier;
      }

      public boolean shouldFilter(String paramAnonymousString)
        throws RemoteException
      {
        if (LocalDevicePlayback.this.mStreamingClient != null);
        for (boolean bool = LocalDevicePlayback.this.mStreamingClient.shouldFilter(paramAnonymousString); ; bool = false)
          return bool;
      }
    };
    this.mDeleteFilter = local3;
    RemoteCallbackList localRemoteCallbackList = new RemoteCallbackList();
    this.mProgressListeners = localRemoteCallbackList;
    BroadcastReceiver local4 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        final String str = paramAnonymousIntent.getStringExtra("sharedurl");
        final int i = paramAnonymousIntent.getIntExtra("playtype", 1);
        final long l = paramAnonymousIntent.getIntExtra("duration", -1);
        if (l == 65535L)
          return;
        LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
        4 local4 = this;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            LocalDevicePlayback localLocalDevicePlayback = LocalDevicePlayback.this;
            String str = str;
            int i = i;
            long l = l;
            localLocalDevicePlayback.setPreviewValues(str, i, l);
            LocalDevicePlayback.this.onSongChanged();
          }
        };
        AsyncWorkers.runAsync(localLoggableHandler, local1);
      }
    };
    this.mSharedPreviewPlayListener = local4;
    PlayQueueFeederListener local5 = new PlayQueueFeederListener()
    {
      public void onFailure(MixDescriptor paramAnonymousMixDescriptor, PlayQueueFeeder.PostProcessingAction paramAnonymousPostProcessingAction)
      {
        int[] arrayOfInt = LocalDevicePlayback.37.$SwitchMap$com$google$android$music$mix$PlayQueueFeeder$PostProcessingAction;
        int i = paramAnonymousPostProcessingAction.ordinal();
        switch (arrayOfInt[i])
        {
        default:
          String str = "Unhandled listener action: " + paramAnonymousPostProcessingAction;
          throw new IllegalStateException(str);
        case 2:
          LocalDevicePlayback.this.mAsyncWakeLock.acquire();
          LoggableHandler localLoggableHandler1 = AsyncWorkers.sBackendServiceWorker;
          Runnable local5 = new Runnable()
          {
            public void run()
            {
              try
              {
                LocalDevicePlayback localLocalDevicePlayback = LocalDevicePlayback.this;
                int i = LocalDevicePlayback.this.mPlayPos;
                int j = localLocalDevicePlayback.getNextPlayPosition(true, false, i, false);
                LocalDevicePlayback.this.nextImplSync(true, j);
                return;
              }
              finally
              {
                LocalDevicePlayback.this.mAsyncWakeLock.release();
              }
            }
          };
          AsyncWorkers.runAsync(localLoggableHandler1, local5);
        case 1:
        case 3:
        case 4:
        case 5:
        case 6:
        }
        while (true)
        {
          MixGenerationState localMixGenerationState = LocalDevicePlayback.this.mMixGenerationState;
          if ((localMixGenerationState != null) && (localMixGenerationState.getMix().equals(paramAnonymousMixDescriptor)))
          {
            MixGenerationState.State localState = MixGenerationState.State.FAILED;
            localMixGenerationState.setState(localState);
          }
          LocalDevicePlayback.this.mAsyncWakeLock.acquire();
          LoggableHandler localLoggableHandler2 = AsyncWorkers.sBackendServiceWorker;
          Runnable local7 = new Runnable()
          {
            public void run()
            {
              try
              {
                LocalDevicePlayback.this.notifyChange("com.google.android.music.mix.generationfinished");
                return;
              }
              finally
              {
                LocalDevicePlayback.this.mAsyncWakeLock.release();
              }
            }
          };
          AsyncWorkers.runAsync(localLoggableHandler2, local7);
          return;
          LocalDevicePlayback.this.mAsyncWakeLock.acquire();
          LoggableHandler localLoggableHandler3 = AsyncWorkers.sBackendServiceWorker;
          Runnable local6 = new Runnable()
          {
            public void run()
            {
              try
              {
                LocalDevicePlayback.this.notifyChange("com.google.android.music.refreshfailed");
                return;
              }
              finally
              {
                LocalDevicePlayback.this.mAsyncWakeLock.release();
              }
            }
          };
          AsyncWorkers.runAsync(localLoggableHandler3, local6);
        }
      }

      public void onSuccess(MixDescriptor paramAnonymousMixDescriptor1, MixDescriptor paramAnonymousMixDescriptor2, final SongList paramAnonymousSongList, PlayQueueFeeder.PostProcessingAction paramAnonymousPostProcessingAction)
      {
        int[] arrayOfInt = LocalDevicePlayback.37.$SwitchMap$com$google$android$music$mix$PlayQueueFeeder$PostProcessingAction;
        int i = paramAnonymousPostProcessingAction.ordinal();
        MixGenerationState localMixGenerationState;
        switch (arrayOfInt[i])
        {
        default:
          String str = "Unhandled listener action: " + paramAnonymousPostProcessingAction;
          throw new IllegalStateException(str);
        case 1:
          LocalDevicePlayback.this.open(paramAnonymousSongList, -1, true);
        case 4:
          localMixGenerationState = LocalDevicePlayback.this.mMixGenerationState;
          if ((localMixGenerationState != null) && (localMixGenerationState.isMyMix(paramAnonymousMixDescriptor1)))
          {
            if (paramAnonymousMixDescriptor2 == null)
              break label367;
            localMixGenerationState.transitionToNewMix(paramAnonymousMixDescriptor2);
          }
          break;
        case 2:
        case 3:
        case 5:
        case 6:
        }
        while (true)
        {
          LocalDevicePlayback.this.mAsyncWakeLock.acquire();
          LoggableHandler localLoggableHandler1 = AsyncWorkers.sBackendServiceWorker;
          Runnable local4 = new Runnable()
          {
            public void run()
            {
              try
              {
                LocalDevicePlayback.this.notifyChange("com.google.android.music.mix.generationfinished");
                return;
              }
              finally
              {
                LocalDevicePlayback.this.mAsyncWakeLock.release();
              }
            }
          };
          AsyncWorkers.runAsync(localLoggableHandler1, local4);
          return;
          LocalDevicePlayback localLocalDevicePlayback1 = LocalDevicePlayback.this;
          long l1 = LocalDevicePlayback.this.mListItemId;
          PlayQueueAddResult localPlayQueueAddResult1 = localLocalDevicePlayback1.queue(paramAnonymousSongList, l1);
          LocalDevicePlayback.this.mAsyncWakeLock.acquire();
          LoggableHandler localLoggableHandler2 = AsyncWorkers.sBackendServiceWorker;
          Runnable local1 = new Runnable()
          {
            public void run()
            {
              try
              {
                LocalDevicePlayback localLocalDevicePlayback = LocalDevicePlayback.this;
                int i = LocalDevicePlayback.this.mPlayPos;
                int j = localLocalDevicePlayback.getNextPlayPosition(false, false, i, false);
                LocalDevicePlayback.this.nextImplSync(false, j);
                return;
              }
              finally
              {
                LocalDevicePlayback.this.mAsyncWakeLock.release();
              }
            }
          };
          AsyncWorkers.runAsync(localLoggableHandler2, local1);
          break;
          LocalDevicePlayback localLocalDevicePlayback2 = LocalDevicePlayback.this;
          long l2 = LocalDevicePlayback.this.mListItemId;
          PlayQueueAddResult localPlayQueueAddResult2 = localLocalDevicePlayback2.queue(paramAnonymousSongList, l2);
          break;
          LocalDevicePlayback.this.clearFutureQueueItems();
          LocalDevicePlayback.this.mAsyncWakeLock.acquire();
          LoggableHandler localLoggableHandler3 = AsyncWorkers.sBackendServiceWorker;
          Runnable local2 = new Runnable()
          {
            public void run()
            {
              try
              {
                LocalDevicePlayback localLocalDevicePlayback = LocalDevicePlayback.this;
                SongList localSongList = paramAnonymousSongList;
                long l = LocalDevicePlayback.this.mListItemId;
                PlayQueueAddResult localPlayQueueAddResult = localLocalDevicePlayback.queue(localSongList, l);
                LocalDevicePlayback.this.notifyChange("com.google.android.music.refreshcomplete");
                return;
              }
              finally
              {
                LocalDevicePlayback.this.mAsyncWakeLock.release();
              }
            }
          };
          AsyncWorkers.runAsync(localLoggableHandler3, local2);
          LocalDevicePlayback.this.next();
          return;
          if (LocalDevicePlayback.this.mMixGenerationState == null)
          {
            LocalDevicePlayback.this.notifyChange("com.google.android.music.refreshfailed");
            return;
          }
          LocalDevicePlayback.this.open(paramAnonymousSongList, -1, true);
          LocalDevicePlayback.this.mAsyncWakeLock.acquire();
          LoggableHandler localLoggableHandler4 = AsyncWorkers.sBackendServiceWorker;
          Runnable local3 = new Runnable()
          {
            public void run()
            {
              try
              {
                LocalDevicePlayback.this.notifyChange("com.google.android.music.refreshcomplete");
                return;
              }
              finally
              {
                LocalDevicePlayback.this.mAsyncWakeLock.release();
              }
            }
          };
          AsyncWorkers.runAsync(localLoggableHandler4, local3);
          break;
          label367: MixGenerationState.State localState = MixGenerationState.State.FINISHED;
          localMixGenerationState.setState(localState);
        }
      }
    };
    this.mPlayQueueFeederListener = local5;
    ServiceHooks local6 = new ServiceHooks()
    {
      public void cancelAllStreamingTracks()
      {
        LocalDevicePlayback.this.mStreamingClient.cancelAndPurgeAllStreamingTracks();
      }

      public void cancelTryNext()
      {
        if (LocalDevicePlayback.this.LOGV)
          Log.d("LocalDevicePlayback", "cancelTryNext");
        LocalDevicePlayback.this.mMediaplayerHandler.removeMessages(7);
      }

      public void markSongPlayed(ContentIdentifier paramAnonymousContentIdentifier)
        throws RemoteException
      {
        if ((!paramAnonymousContentIdentifier.isDefaultDomain()) && (!paramAnonymousContentIdentifier.isNautilusDomain()))
          return;
        Store localStore = Store.getInstance(LocalDevicePlayback.this.getContext());
        long l = paramAnonymousContentIdentifier.getId();
        localStore.markSongPlayed(l);
      }

      public void notifyMediaRouteInvalidated()
      {
        if (LocalDevicePlayback.this.LOGV)
          Log.d("LocalDevicePlayback", "notifyMediaRouteInvalidated");
        LocalDevicePlayback.this.onMediaRouteInvalidated();
      }

      public void notifyOpenComplete()
      {
        LocalDevicePlayback.this.onOpenComplete();
      }

      public void notifyOpenStarted()
      {
        LocalDevicePlayback.this.onOpenStarted();
      }

      public void notifyPlayStateChanged()
      {
        LocalDevicePlayback.this.onPlayStateChanged();
      }

      public void onDownloadProgress(final DownloadProgress paramAnonymousDownloadProgress)
      {
        LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            int i = LocalDevicePlayback.this.mProgressListeners.beginBroadcast();
            int j = 0;
            while (j < i)
              try
              {
                IDownloadProgressListener localIDownloadProgressListener = (IDownloadProgressListener)LocalDevicePlayback.this.mProgressListeners.getBroadcastItem(j);
                DownloadProgress localDownloadProgress1 = paramAnonymousDownloadProgress;
                localIDownloadProgressListener.onDownloadProgress(localDownloadProgress1);
                j += 1;
              }
              catch (RemoteException localRemoteException)
              {
                while (true)
                  Log.e("LocalDevicePlayback", "Failed to call the download progress", localRemoteException);
              }
              finally
              {
                LocalDevicePlayback.this.mProgressListeners.finishBroadcast();
              }
            LocalDevicePlayback.this.mProgressListeners.finishBroadcast();
            if (LocalDevicePlayback.this.mStreamingClient == null)
              return;
            StreamingClient localStreamingClient = LocalDevicePlayback.this.mStreamingClient;
            DownloadProgress localDownloadProgress2 = paramAnonymousDownloadProgress;
            localStreamingClient.handleDownloadProgress(localDownloadProgress2);
          }
        };
        AsyncWorkers.runAsync(localLoggableHandler, local1);
      }

      public void reportTrackEnded()
      {
        if (LocalDevicePlayback.this.mStreamingClient != null)
          LocalDevicePlayback.this.mStreamingClient.currenStreamingPlayEnded();
        if (LocalDevicePlayback.this.mNextPlayer != null)
        {
          LocalDevicePlayback.this.mPlayer.release();
          LocalDevicePlayback localLocalDevicePlayback = LocalDevicePlayback.this;
          AsyncMediaPlayer localAsyncMediaPlayer1 = LocalDevicePlayback.this.mNextPlayer;
          AsyncMediaPlayer localAsyncMediaPlayer2 = LocalDevicePlayback.access$2302(localLocalDevicePlayback, localAsyncMediaPlayer1);
          LocalDevicePlayback.this.mPlayer.setAsCurrentPlayer();
          AsyncMediaPlayer localAsyncMediaPlayer3 = LocalDevicePlayback.access$302(LocalDevicePlayback.this, null);
          boolean bool1 = LocalDevicePlayback.this.mMediaplayerHandler.sendEmptyMessage(8);
          return;
        }
        LocalDevicePlayback.this.mWakeLock.acquire(30000L);
        boolean bool2 = LocalDevicePlayback.this.mMediaplayerHandler.sendEmptyMessage(1);
        boolean bool3 = LocalDevicePlayback.this.mMediaplayerHandler.sendEmptyMessage(2);
      }

      public void reportTrackPaused()
      {
        if (LocalDevicePlayback.this.LOGV)
          Log.d("LocalDevicePlayback", "reportTrackPaused");
        boolean bool = LocalDevicePlayback.access$2202(LocalDevicePlayback.this, false);
      }

      public void reportTrackPlaying()
      {
        if (LocalDevicePlayback.this.LOGV)
          Log.d("LocalDevicePlayback", "reportTrackPlaying");
        boolean bool = LocalDevicePlayback.access$2202(LocalDevicePlayback.this, true);
      }

      public void setMediaRouteSessionId(String paramAnonymousString)
      {
        String str = LocalDevicePlayback.access$1802(LocalDevicePlayback.this, paramAnonymousString);
      }

      public String streamTrack(ContentIdentifier paramAnonymousContentIdentifier, long paramAnonymousLong, IDownloadProgressListener paramAnonymousIDownloadProgressListener, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2)
        throws DownloadRequestException, OutOfSpaceException
      {
        SongList localSongList = LocalDevicePlayback.this.mMediaList;
        String str;
        if (localSongList == null)
        {
          Log.w("LocalDevicePlayback", "MediaList is null");
          str = null;
        }
        while (true)
        {
          return str;
          if (LocalDevicePlayback.this.mStreamingClient != null)
          {
            ContentIdentifier[] arrayOfContentIdentifier = LocalDevicePlayback.this.getNextSongs();
            StreamingClient localStreamingClient = LocalDevicePlayback.this.mStreamingClient;
            String[] arrayOfString = LocalDevicePlayback.this.mCursorCols;
            ContentIdentifier localContentIdentifier = paramAnonymousContentIdentifier;
            long l = paramAnonymousLong;
            IDownloadProgressListener localIDownloadProgressListener = paramAnonymousIDownloadProgressListener;
            boolean bool1 = paramAnonymousBoolean1;
            boolean bool2 = paramAnonymousBoolean2;
            str = localStreamingClient.streamTrack(localContentIdentifier, l, localIDownloadProgressListener, bool1, bool2, localSongList, arrayOfString, arrayOfContentIdentifier);
          }
          else
          {
            Log.w("LocalDevicePlayback", "Streaming client is null");
            str = null;
          }
        }
      }
    };
    this.mServiceHooks = local6;
    Handler local7 = new Handler()
    {
      float mCurrentVolume = 1.0F;

      public void handleMessage(Message paramAnonymousMessage)
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("mMediaplayerHandler.handleMessage ");
        int i = paramAnonymousMessage.what;
        MusicUtils.debugLog(i);
        switch (paramAnonymousMessage.what)
        {
        default:
          return;
        case 5:
          float f1 = this.mCurrentVolume - 0.05F;
          this.mCurrentVolume = f1;
          if (this.mCurrentVolume > 0.2F)
            boolean bool1 = LocalDevicePlayback.this.mMediaplayerHandler.sendEmptyMessageDelayed(5, 10L);
          while (true)
          {
            AsyncMediaPlayer localAsyncMediaPlayer1 = LocalDevicePlayback.this.mPlayer;
            float f2 = this.mCurrentVolume;
            localAsyncMediaPlayer1.setVolume(f2);
            return;
            this.mCurrentVolume = 0.2F;
          }
        case 6:
          float f3 = this.mCurrentVolume + 0.01F;
          this.mCurrentVolume = f3;
          if (this.mCurrentVolume < 1.0F)
            boolean bool2 = LocalDevicePlayback.this.mMediaplayerHandler.sendEmptyMessageDelayed(6, 10L);
          while (true)
          {
            AsyncMediaPlayer localAsyncMediaPlayer2 = LocalDevicePlayback.this.mPlayer;
            float f4 = this.mCurrentVolume;
            localAsyncMediaPlayer2.setVolume(f4);
            return;
            this.mCurrentVolume = 1.0F;
          }
        case 3:
          if (LocalDevicePlayback.this.mIsSupposedToBePlaying)
          {
            LocalDevicePlayback.this.next(true);
            return;
          }
          LocalDevicePlayback localLocalDevicePlayback1 = LocalDevicePlayback.this;
          boolean bool3 = false;
          boolean bool4 = false;
          boolean bool5 = false;
          localLocalDevicePlayback1.openCurrentAndNext(false, bool3, bool4, bool5, 0L, null);
          return;
        case 8:
          if (LocalDevicePlayback.this.LOGV)
          {
            StringBuilder localStringBuilder2 = new StringBuilder().append("Handling: TRACK_WENT_TO_NEXT: mMixGenerationState=");
            MixGenerationState localMixGenerationState1 = LocalDevicePlayback.this.mMixGenerationState;
            String str1 = localMixGenerationState1;
            Log.d("LocalDevicePlayback", str1);
          }
          LocalDevicePlayback localLocalDevicePlayback2 = LocalDevicePlayback.this;
          int j = LocalDevicePlayback.this.mNextPlayPos;
          int k = LocalDevicePlayback.access$1102(localLocalDevicePlayback2, j);
          LoggableHandler localLoggableHandler1 = AsyncWorkers.sBackendServiceWorker;
          Runnable local1 = new Runnable()
          {
            public void run()
            {
              LocalDevicePlayback.MediaListWrapper localMediaListWrapper = LocalDevicePlayback.this.mPlayList;
              int i = LocalDevicePlayback.this.mPlayPos;
              Pair localPair = localMediaListWrapper.getAudioIdAndListItemId(i);
              if (localPair != null)
              {
                if (LocalDevicePlayback.this.mLastPlayQueueGroupId != 0L)
                {
                  int j = LocalDevicePlayback.this.mPlayPos;
                  int k = LocalDevicePlayback.this.mLastPlayQueueGroupPosition;
                  if (j < k)
                  {
                    long l1 = LocalDevicePlayback.access$2902(LocalDevicePlayback.this, 0L);
                    int m = LocalDevicePlayback.access$3002(LocalDevicePlayback.this, -1);
                  }
                }
                LocalDevicePlayback localLocalDevicePlayback1 = LocalDevicePlayback.this;
                ContentIdentifier localContentIdentifier = (ContentIdentifier)localPair.first;
                if (localLocalDevicePlayback1.refreshCursor(localContentIdentifier))
                {
                  LocalDevicePlayback localLocalDevicePlayback2 = LocalDevicePlayback.this;
                  long l2 = ((Long)localPair.second).longValue();
                  long l3 = LocalDevicePlayback.access$802(localLocalDevicePlayback2, l2);
                }
              }
              LocalDevicePlayback.this.mPlayer.start();
              LocalDevicePlayback.this.notifyChange("com.android.music.metachanged");
              LocalDevicePlayback.this.setNextTrack();
              LocalDevicePlayback localLocalDevicePlayback3 = LocalDevicePlayback.this;
              PlayQueueFeeder.PostProcessingAction localPostProcessingAction = PlayQueueFeeder.PostProcessingAction.FEED;
              boolean bool = localLocalDevicePlayback3.feedQueueIfNeeded(localPostProcessingAction);
            }
          };
          AsyncWorkers.runAsync(localLoggableHandler1, local1);
          return;
        case 1:
          if (LocalDevicePlayback.this.LOGV)
          {
            StringBuilder localStringBuilder3 = new StringBuilder().append("TRACK_ENDED: mMixGenerationState=");
            MixGenerationState localMixGenerationState2 = LocalDevicePlayback.this.mMixGenerationState;
            String str2 = localMixGenerationState2;
            Log.d("LocalDevicePlayback", str2);
          }
          if (LocalDevicePlayback.this.mRepeatMode == 1)
          {
            LocalDevicePlayback.this.openCurrentAndPlay(false);
            return;
          }
          LocalDevicePlayback.this.next(false);
          return;
        case 2:
          LocalDevicePlayback.this.mWakeLock.release();
          return;
        case 4:
          if (!LocalDevicePlayback.this.isPlayingLocally())
            return;
          switch (paramAnonymousMessage.arg1)
          {
          case 0:
          default:
            Log.e("LocalDevicePlayback", "Unknown audio focus change code");
            return;
          case -1:
            Log.v("LocalDevicePlayback", "AudioFocus: received AUDIOFOCUS_LOSS");
            LocalDevicePlayback.this.pause();
            return;
          case -3:
            LocalDevicePlayback.this.mMediaplayerHandler.removeMessages(6);
            boolean bool6 = LocalDevicePlayback.this.mMediaplayerHandler.sendEmptyMessage(5);
            return;
          case -2:
            Log.v("LocalDevicePlayback", "AudioFocus: received AUDIOFOCUS_LOSS_TRANSIENT");
            LocalDevicePlayback.this.pause(true);
            return;
          case 1:
          }
          Log.v("LocalDevicePlayback", "AudioFocus: received AUDIOFOCUS_GAIN");
          LocalDevicePlayback.this.mAsyncWakeLock.acquire();
          LoggableHandler localLoggableHandler2 = AsyncWorkers.sBackendServiceWorker;
          Runnable local2 = new Runnable()
          {
            public void run()
            {
              try
              {
                if ((!LocalDevicePlayback.this.isPlaying()) && (LocalDevicePlayback.this.mPausedByTransientLossOfFocus))
                  boolean bool1 = LocalDevicePlayback.this.mMediaplayerHandler.sendEmptyMessage(13);
                while (true)
                {
                  return;
                  LocalDevicePlayback.this.mMediaplayerHandler.removeMessages(6);
                  boolean bool2 = LocalDevicePlayback.this.mMediaplayerHandler.sendEmptyMessage(6);
                }
              }
              finally
              {
                LocalDevicePlayback.this.mAsyncWakeLock.release();
              }
            }
          };
          AsyncWorkers.runAsync(localLoggableHandler2, local2);
          return;
        case 7:
          if (LocalDevicePlayback.this.LOGV)
            Log.d("LocalDevicePlayback", "TRYNEXT");
          LocalDevicePlayback.this.next(false);
          return;
        case 9:
          if (LocalDevicePlayback.this.LOGV)
            Log.d("LocalDevicePlayback", "SET_MEDIA_ROUTE");
          LocalDevicePlayback.SelectedRoute localSelectedRoute = (LocalDevicePlayback.SelectedRoute)paramAnonymousMessage.obj;
          LocalDevicePlayback localLocalDevicePlayback3 = LocalDevicePlayback.this;
          boolean bool7 = localSelectedRoute.mLocalRoute;
          String str3 = localSelectedRoute.mRouteId;
          boolean bool8 = localSelectedRoute.mAutoPlay;
          long l = localSelectedRoute.mPosition;
          localLocalDevicePlayback3.setMediaRouteImpl(bool7, str3, bool8, l);
          return;
        case 10:
          LocalDevicePlayback.RouteVolume localRouteVolume1 = (LocalDevicePlayback.RouteVolume)paramAnonymousMessage.obj;
          LocalDevicePlayback localLocalDevicePlayback4 = LocalDevicePlayback.this;
          String str4 = localRouteVolume1.mRouteId;
          double d1 = localRouteVolume1.mValue;
          localLocalDevicePlayback4.setMediaRouteVolumeImpl(str4, d1);
          return;
        case 11:
          LocalDevicePlayback.RouteVolume localRouteVolume2 = (LocalDevicePlayback.RouteVolume)paramAnonymousMessage.obj;
          LocalDevicePlayback localLocalDevicePlayback5 = LocalDevicePlayback.this;
          String str5 = localRouteVolume2.mRouteId;
          double d2 = localRouteVolume2.mValue;
          localLocalDevicePlayback5.updateMediaRouteVolumeImpl(str5, d2);
          return;
        case 12:
          if (LocalDevicePlayback.this.LOGV)
            Log.d("LocalDevicePlayback", "SELECT_DEFAULT_MEDIA_ROUTE");
          LocalDevicePlayback.this.selectDefaultMediaRouteImpl();
          return;
        case 13:
        }
        if (LocalDevicePlayback.this.LOGV)
          Log.d("LocalDevicePlayback", "RESET_VOLUME_AND_PLAY");
        this.mCurrentVolume = 0.0F;
        AsyncMediaPlayer localAsyncMediaPlayer3 = LocalDevicePlayback.this.mPlayer;
        float f5 = this.mCurrentVolume;
        localAsyncMediaPlayer3.setVolume(f5);
        LocalDevicePlayback.this.mService.play();
      }
    };
    this.mMediaplayerHandler = local7;
    AudioManager.OnAudioFocusChangeListener local8 = new AudioManager.OnAudioFocusChangeListener()
    {
      public void onAudioFocusChange(int paramAnonymousInt)
      {
        LocalDevicePlayback.this.mMediaplayerHandler.obtainMessage(4, paramAnonymousInt, 0).sendToTarget();
      }
    };
    this.mAudioFocusListener = local8;
    SafeServiceConnection local9 = new SafeServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        if (LocalDevicePlayback.this.LOGV)
        {
          String str1 = "Service connected: " + paramAnonymousComponentName;
          Log.d("LocalDevicePlayback", str1);
        }
        String str2 = paramAnonymousComponentName.getClassName();
        if (DownloadQueueService.class.getName().equals(str2))
        {
          LocalDevicePlayback localLocalDevicePlayback = LocalDevicePlayback.this;
          IDownloadQueueManager localIDownloadQueueManager1 = IDownloadQueueManager.Stub.asInterface(paramAnonymousIBinder);
          IDownloadQueueManager localIDownloadQueueManager2 = LocalDevicePlayback.access$4302(localLocalDevicePlayback, localIDownloadQueueManager1);
        }
        while (true)
        {
          LocalDevicePlayback.this.tryCreatingStreamingSchedulingClient();
          return;
          String str3 = "Unknown connection to class" + paramAnonymousComponentName;
          Log.wtf("LocalDevicePlayback", str3);
        }
      }

      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
        String str1 = paramAnonymousComponentName.getClassName();
        if (DownloadQueueService.class.getName().equals(str1))
          return;
        String str2 = "Unknown disconnection from class" + paramAnonymousComponentName;
        Log.wtf("LocalDevicePlayback", str2);
      }
    };
    this.mDownloadManagerSafeConnection = local9;
    SafeServiceConnection local10 = new SafeServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        LocalDevicePlayback localLocalDevicePlayback = LocalDevicePlayback.this;
        ICacheManager localICacheManager1 = ICacheManager.Stub.asInterface(paramAnonymousIBinder);
        ICacheManager localICacheManager2 = LocalDevicePlayback.access$4502(localLocalDevicePlayback, localICacheManager1);
        try
        {
          ICacheManager localICacheManager3 = LocalDevicePlayback.this.mCacheManager;
          IDeleteFilter localIDeleteFilter = LocalDevicePlayback.this.mDeleteFilter;
          localICacheManager3.registerDeleteFilter(localIDeleteFilter);
          LocalDevicePlayback.this.tryCreatingStreamingSchedulingClient();
          return;
        }
        catch (RemoteException localRemoteException)
        {
          while (true)
            Log.w("LocalDevicePlayback", "Failed to register delete filter", localRemoteException);
        }
      }

      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
      }
    };
    this.mCacheServiceConnection = local10;
    MusicPlaybackService localMusicPlaybackService = this.mService;
    MediaListWrapper localMediaListWrapper = new MediaListWrapper(localMusicPlaybackService);
    this.mPlayList = localMediaListWrapper;
    IStreamabilityChangeListener localIStreamabilityChangeListener = this.mStreamabilityChangeListener;
    NetworkMonitorServiceConnection localNetworkMonitorServiceConnection = new NetworkMonitorServiceConnection(localIStreamabilityChangeListener);
    this.mNetworkMonitorServiceConnection = localNetworkMonitorServiceConnection;
    CastTokenClient localCastTokenClient = new CastTokenClient(paramMusicPlaybackService);
    this.mCastTokenClient = localCastTokenClient;
  }

  private SongList acquireAsyncLockAndClearMediaList()
  {
    SongList localSongList = this.mMediaList;
    clearCursor();
    this.mAsyncWakeLock.acquire();
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local24 = new Runnable()
    {
      public void run()
      {
        LocalDevicePlayback localLocalDevicePlayback = LocalDevicePlayback.this;
        ContentIdentifier localContentIdentifier1 = LocalDevicePlayback.this.mAudioId;
        ContentIdentifier localContentIdentifier2 = LocalDevicePlayback.access$6102(localLocalDevicePlayback, localContentIdentifier1);
        LocalDevicePlayback.this.mPlayList.setMediaList(null);
        SongList localSongList = LocalDevicePlayback.access$1902(LocalDevicePlayback.this, null);
        LocalDevicePlayback.this.onQueueChanged();
        LocalDevicePlayback.this.onSongChanged();
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local24);
    return localSongList;
  }

  private SongList addToDatabase(ExternalSongList paramExternalSongList)
  {
    SelectedSongList localSelectedSongList = null;
    Context localContext;
    if (paramExternalSongList.isAddToStoreSupported())
      localContext = getContext();
    for (long[] arrayOfLong = paramExternalSongList.addToStore(localContext, false); ; arrayOfLong = null)
    {
      if ((arrayOfLong != null) && (arrayOfLong.length > 0))
        localSelectedSongList = new SelectedSongList("", arrayOfLong);
      return localSelectedSongList;
      String str = "addToDatabase callled on a song list that doesn't support the operation: " + paramExternalSongList;
      Log.w("LocalDevicePlayback", str);
    }
  }

  private void clearCursor()
  {
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      final Cursor localCursor = (Cursor)this.mCurrentSongMetaDataCursor.get();
      if (localCursor != null)
      {
        this.mCurrentSongMetaDataCursor.set(null);
        LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
        Runnable local17 = new Runnable()
        {
          public void run()
          {
            localCursor.close();
          }
        };
        AsyncWorkers.runAsync(localLoggableHandler, local17);
      }
      return;
    }
  }

  private void clearFutureQueueItems()
  {
    SongList localSongList = acquireAsyncLockAndClearMediaList();
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local23 = new Runnable()
    {
      public void run()
      {
        try
        {
          int i = LocalDevicePlayback.this.mPlayPos;
          Store.getInstance(LocalDevicePlayback.this.getContext()).clearFuturePlayQueueItems(i);
          LocalDevicePlayback.this.reloadQueue(false);
          return;
        }
        finally
        {
          LocalDevicePlayback.this.mAsyncWakeLock.release();
        }
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local23);
  }

  private void closeExternalStorageFiles(String paramString)
  {
    stop(true);
    onQueueChanged();
    onSongChanged();
  }

  private SongList createPlayQueueSongList()
  {
    long l = Store.getInstance(getContext()).getPlayQueuePlaylistId();
    return new PlayQueueSongList(l);
  }

  private AsyncMediaPlayer createPlayer()
  {
    MusicPlaybackService localMusicPlaybackService1;
    ServiceHooks localServiceHooks1;
    if (this.mUseLocalPlayer)
    {
      localMusicPlaybackService1 = this.mService;
      localServiceHooks1 = this.mServiceHooks;
    }
    MusicPlaybackService localMusicPlaybackService2;
    CastTokenClient localCastTokenClient;
    ServiceHooks localServiceHooks2;
    boolean bool;
    String str;
    for (Object localObject = new LocalAsyncMediaPlayer(localMusicPlaybackService1, localServiceHooks1); ; localObject = new RemoteAsyncMediaPlayer(localMusicPlaybackService2, localCastTokenClient, localServiceHooks2, bool, str))
    {
      return localObject;
      localMusicPlaybackService2 = this.mService;
      localCastTokenClient = this.mCastTokenClient;
      localServiceHooks2 = this.mServiceHooks;
      bool = this.mPrequeueItems;
      str = this.mMediaRouteSessionId;
    }
  }

  private void disableInfiniteMixMode()
  {
    if (this.mMixGenerationState == null)
      return;
    this.mMixGenerationState = null;
    notifyChange("com.google.android.music.mix.playbackmodechanged");
  }

  private long doGetDuration(Cursor paramCursor)
  {
    String str;
    if ((this.mMediaList != null) && (this.mMediaList.isSharedDomain()))
    {
      int i = paramCursor.getColumnIndexOrThrow("SourceId");
      str = paramCursor.getString(i);
    }
    for (long l = getPreviewPlayDurationForUrl(str); ; l = paramCursor.getLong(10))
      return l;
  }

  private void dumpPastPresentAndFuture()
  {
  }

  private long duration(ContentIdentifier paramContentIdentifier)
  {
    long l1 = 65535L;
    if (this.mMediaList == null);
    while (true)
    {
      return l1;
      SongList localSongList = this.mMediaList;
      Context localContext = getContext();
      String[] arrayOfString = this.mCursorCols;
      Cursor localCursor = localSongList.getSongCursor(localContext, paramContentIdentifier, arrayOfString);
      MusicUtils.debugLog("Queried ID " + paramContentIdentifier);
      if (localCursor != null);
      try
      {
        if (localCursor.moveToFirst())
        {
          long l2 = doGetDuration(localCursor);
          l1 = l2;
        }
        Store.safeClose(localCursor);
      }
      finally
      {
        Store.safeClose(localCursor);
      }
    }
  }

  private void enableInfiniteMixMode(MixDescriptor paramMixDescriptor)
  {
    if ((this.mMixGenerationState != null) && (this.mMixGenerationState.getMix().equals(paramMixDescriptor)))
      return;
    this.mShuffleMode = 0;
    this.mRepeatMode = 0;
    MixGenerationState localMixGenerationState = new MixGenerationState(paramMixDescriptor);
    this.mMixGenerationState = localMixGenerationState;
    StringBuilder localStringBuilder = new StringBuilder().append("Enabled mix ");
    String str1 = paramMixDescriptor.toString();
    String str2 = str1;
    Log.i("LocalDevicePlayback", str2);
    notifyChange("com.google.android.music.mix.playbackmodechanged");
  }

  private void enableInfiniteMixModeForSuggestedMix(SongList paramSongList)
  {
    PlaylistSongList localPlaylistSongList = (PlaylistSongList)paramSongList;
    Store localStore = Store.getInstance(getContext());
    long l1 = localPlaylistSongList.getPlaylistId();
    ContentIdentifier localContentIdentifier = localStore.getFirstItemId(l1);
    if (localContentIdentifier == null)
      throw new IllegalStateException("Failed to get seed for suggested mix");
    long l2 = localContentIdentifier.getId();
    MixDescriptor.Type localType = MixDescriptor.Type.TRACK_SEED;
    Context localContext = getContext();
    String str = paramSongList.getName(localContext);
    MixDescriptor localMixDescriptor = new MixDescriptor(l2, localType, str, null);
    enableInfiniteMixMode(localMixDescriptor);
  }

  private boolean feedQueueIfNeeded(PlayQueueFeeder.PostProcessingAction paramPostProcessingAction)
  {
    boolean bool = true;
    if (this.LOGV)
    {
      String str1 = "feedQueueIfNeeded: action=" + paramPostProcessingAction;
      Log.d("LocalDevicePlayback", str1);
    }
    Looper localLooper1 = Looper.myLooper();
    Looper localLooper2 = AsyncWorkers.sBackendServiceWorker.getLooper();
    if (localLooper1 != localLooper2)
      throw new IllegalStateException("feedQueueIfNeeded must run on the AsyncWorkers.sBackendServiceWorker handler");
    if (this.mMixGenerationState != null)
    {
      int i = Gservices.getInt(getContext().getApplicationContext().getContentResolver(), "music_min_infinite_mix_tail_size", 5);
      PlayQueueFeeder.PostProcessingAction localPostProcessingAction = PlayQueueFeeder.PostProcessingAction.NEXT;
      if (paramPostProcessingAction == localPostProcessingAction)
        int j = i + 1;
      if (i < 0)
        throw new IllegalStateException("Negative queue tail size");
      if (this.LOGV)
      {
        Object[] arrayOfObject1 = new Object[3];
        Integer localInteger1 = Integer.valueOf(this.mPlayPos);
        arrayOfObject1[0] = localInteger1;
        Integer localInteger2 = Integer.valueOf(this.mPlayList.length());
        arrayOfObject1[1] = localInteger2;
        Integer localInteger3 = Integer.valueOf(i);
        arrayOfObject1[2] = localInteger3;
        String str2 = String.format("feedQueueIfNeeded: mPlayPos=%d mPlayList.length=%d minQueueTailSize=%d", arrayOfObject1);
        Log.d("LocalDevicePlayback", str2);
      }
      if (this.mPlayPos != -1)
      {
        int k = this.mPlayPos;
        int m = this.mPlayList.length() - i;
        if (k < m);
      }
      else
      {
        requestMoreContentSync(paramPostProcessingAction);
        if (this.LOGV)
        {
          Object[] arrayOfObject2 = new Object[2];
          Integer localInteger4 = Integer.valueOf(this.mPlayPos);
          arrayOfObject2[0] = localInteger4;
          Integer localInteger5 = Integer.valueOf(i);
          arrayOfObject2[1] = localInteger5;
          String str3 = String.format("feedQueueIfNeeded: updated the queue mPlayPos=%d minQueueTailSize=%d", arrayOfObject2);
          Log.d("LocalDevicePlayback", str3);
        }
      }
    }
    while (true)
    {
      return bool;
      if (this.LOGV)
      {
        Object[] arrayOfObject3 = new Object[1];
        Integer localInteger6 = Integer.valueOf(this.mPlayPos);
        arrayOfObject3[0] = localInteger6;
        String str4 = String.format("feedQueueIfNeeded: didn't update the queue mPlayPos=%d", arrayOfObject3);
        Log.d("LocalDevicePlayback", str4);
      }
      bool = false;
    }
  }

  private void fillShuffleList()
  {
    int i = this.mPlayList.length();
    int j;
    int k;
    if (i > 200)
    {
      j = 1;
      k = Math.min(200, i);
      if (this.mFuture.size() != 0)
      {
        int m = this.mFuture.size();
        int n = k - m;
      }
      if (this.mPlayPos == -1)
        break label365;
    }
    label150: label171: label220: label226: label365: for (int i1 = k + -1; ; i1 = k)
    {
      if (i1 <= 0)
      {
        return;
        j = 0;
        break;
      }
      LinkedList localLinkedList1 = Lists.newLinkedList();
      int i2 = 0;
      while (true)
        if (i2 < i1)
        {
          int i3 = this.mRand.nextInt(i);
          try
          {
            int i4 = this.mPlayList.get(i3);
            if (i4 != 0)
            {
              int i5;
              if (i4.isDefaultDomain())
              {
                Store localStore = Store.getInstance(getContext());
                long l = i4.getId();
                i5 = localStore.getRating(l);
                if ((i5 != 1) && (i5 != 1) && (i5 != 2))
                  break label220;
                i5 = 1;
                if (i5 == 0)
                  break label226;
                if (j == 0)
                {
                  Integer localInteger1 = Integer.valueOf(i3);
                  boolean bool1 = localLinkedList1.add(localInteger1);
                }
              }
              while (true)
              {
                int i6 = i2 + 1;
                break;
                Log.w("LocalDevicePlayback", "Only Default domain songs are valid for StoreService.getRating");
                i5 = 0;
                break label150;
                i5 = 0;
                break label171;
                LinkedList localLinkedList2 = this.mFuture;
                Integer localInteger2 = Integer.valueOf(i3);
                boolean bool2 = localLinkedList2.add(localInteger2);
              }
            }
          }
          catch (FileNotFoundException localFileNotFoundException)
          {
            while (true)
            {
              String str1 = localFileNotFoundException.getMessage();
              Log.e("LocalDevicePlayback", str1, localFileNotFoundException);
              continue;
              String str2 = "Could not get id for position: " + i3;
              Log.e("LocalDevicePlayback", str2);
            }
          }
        }
      if ((this.LOGV) && (localLinkedList1.size() != 0))
      {
        String str3 = "Rated down songs added to end of shuffle list: " + localLinkedList1;
        Log.v("LocalDevicePlayback", str3);
      }
      boolean bool3 = this.mFuture.addAll(localLinkedList1);
      return;
    }
  }

  // ERROR //
  private Pair<Integer, MusicFile> findPlayableSong(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    // Byte code:
    //   0: iload_1
    //   1: bipush 255
    //   3: if_icmpne +9 -> 12
    //   6: aconst_null
    //   7: astore 4
    //   9: aload 4
    //   11: areturn
    //   12: aload_0
    //   13: getfield 324	com/google/android/music/playback/LocalDevicePlayback:mIsStreamingEnabled	Z
    //   16: istore 5
    //   18: aconst_null
    //   19: astore 6
    //   21: iload_1
    //   22: istore 7
    //   24: ldc2_w 279
    //   27: lstore 8
    //   29: iconst_0
    //   30: istore 9
    //   32: aload_0
    //   33: getfield 380	com/google/android/music/playback/LocalDevicePlayback:mPlayList	Lcom/google/android/music/playback/LocalDevicePlayback$MediaListWrapper;
    //   36: iload 7
    //   38: invokevirtual 1080	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:getAudioIdAndListItemId	(I)Landroid/util/Pair;
    //   41: astore 11
    //   43: aload 11
    //   45: ifnonnull +47 -> 92
    //   48: aload_0
    //   49: getfield 296	com/google/android/music/playback/LocalDevicePlayback:LOGV	Z
    //   52: ifeq +34 -> 86
    //   55: new 785	java/lang/StringBuilder
    //   58: dup
    //   59: invokespecial 786	java/lang/StringBuilder:<init>	()V
    //   62: ldc_w 1082
    //   65: invokevirtual 792	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: iload 7
    //   70: invokevirtual 1063	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   73: invokevirtual 799	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   76: astore 12
    //   78: ldc_w 801
    //   81: aload 12
    //   83: invokestatic 957	com/google/android/music/log/Log:d	(Ljava/lang/String;Ljava/lang/String;)V
    //   86: aconst_null
    //   87: astore 4
    //   89: goto -80 -> 9
    //   92: aload 11
    //   94: getfield 1087	android/util/Pair:first	Ljava/lang/Object;
    //   97: checkcast 934	com/google/android/music/download/ContentIdentifier
    //   100: astore 13
    //   102: aload 13
    //   104: invokevirtual 1090	com/google/android/music/download/ContentIdentifier:isCacheable	()Z
    //   107: ifeq +350 -> 457
    //   110: aload_0
    //   111: invokevirtual 772	com/google/android/music/playback/LocalDevicePlayback:getContext	()Landroid/content/Context;
    //   114: invokestatic 829	com/google/android/music/store/Store:getInstance	(Landroid/content/Context;)Lcom/google/android/music/store/Store;
    //   117: astore 14
    //   119: aload_0
    //   120: invokevirtual 1093	com/google/android/music/playback/LocalDevicePlayback:isPlayingLocally	()Z
    //   123: ifeq +242 -> 365
    //   126: aconst_null
    //   127: astore 6
    //   129: aload 14
    //   131: aload 13
    //   133: aload 6
    //   135: invokevirtual 1097	com/google/android/music/store/Store:getPreferredMusicId	(Lcom/google/android/music/download/ContentIdentifier;I)Lcom/google/android/music/download/ContentIdentifier;
    //   138: astore 15
    //   140: aload 15
    //   142: astore 16
    //   144: aload_0
    //   145: invokevirtual 772	com/google/android/music/playback/LocalDevicePlayback:getContext	()Landroid/content/Context;
    //   148: invokestatic 829	com/google/android/music/store/Store:getInstance	(Landroid/content/Context;)Lcom/google/android/music/store/Store;
    //   151: astore 17
    //   153: aload 16
    //   155: invokevirtual 937	com/google/android/music/download/ContentIdentifier:getId	()J
    //   158: lstore 18
    //   160: aload 17
    //   162: aconst_null
    //   163: lload 18
    //   165: invokestatic 1103	com/google/android/music/store/MusicFile:getSummaryMusicFile	(Lcom/google/android/music/store/Store;Lcom/google/android/music/store/MusicFile;J)Lcom/google/android/music/store/MusicFile;
    //   168: astore 20
    //   170: aload 20
    //   172: astore 6
    //   174: aload 6
    //   176: invokevirtual 1106	com/google/android/music/store/MusicFile:getLocalCopyType	()I
    //   179: lookupswitch	default:+41->220, 0:+272->451, 100:+236->415, 200:+236->415, 300:+230->409
    //   221: lconst_0
    //   222: istore 19
    //   224: iload 19
    //   226: ifeq +13 -> 239
    //   229: iload 19
    //   231: ifeq +232 -> 463
    //   234: iload 5
    //   236: ifeq +227 -> 463
    //   239: aconst_null
    //   240: astore 14
    //   242: aload 14
    //   244: ifnull +225 -> 469
    //   247: aload 14
    //   249: ifnull +305 -> 554
    //   252: iload_1
    //   253: iload 7
    //   255: if_icmpeq +87 -> 342
    //   258: new 785	java/lang/StringBuilder
    //   261: dup
    //   262: invokespecial 786	java/lang/StringBuilder:<init>	()V
    //   265: ldc_w 1108
    //   268: invokevirtual 792	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   271: iload_1
    //   272: invokevirtual 1063	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   275: ldc_w 1110
    //   278: invokevirtual 792	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   281: iload 7
    //   283: invokevirtual 1063	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   286: invokevirtual 799	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   289: astore 22
    //   291: ldc_w 801
    //   294: aload 22
    //   296: invokestatic 917	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   299: iload_2
    //   300: ifeq +42 -> 342
    //   303: aload_0
    //   304: getfield 375	com/google/android/music/playback/LocalDevicePlayback:mService	Lcom/google/android/music/playback/MusicPlaybackService;
    //   307: invokevirtual 1115	com/google/android/music/playback/MusicPlaybackService:isUIVisible	()Z
    //   310: ifeq +32 -> 342
    //   313: aload_0
    //   314: getfield 375	com/google/android/music/playback/LocalDevicePlayback:mService	Lcom/google/android/music/playback/MusicPlaybackService;
    //   317: astore 23
    //   319: aload_0
    //   320: getfield 375	com/google/android/music/playback/LocalDevicePlayback:mService	Lcom/google/android/music/playback/MusicPlaybackService;
    //   323: ldc_w 1116
    //   326: invokevirtual 1117	com/google/android/music/playback/MusicPlaybackService:getString	(I)Ljava/lang/String;
    //   329: astore 24
    //   331: aload 23
    //   333: aload 24
    //   335: iconst_1
    //   336: invokestatic 1123	android/widget/Toast:makeText	(Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
    //   339: invokevirtual 1126	android/widget/Toast:show	()V
    //   342: iload 7
    //   344: invokestatic 1001	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   347: astore 25
    //   349: new 1084	android/util/Pair
    //   352: dup
    //   353: aload 25
    //   355: aload 6
    //   357: invokespecial 1129	android/util/Pair:<init>	(Ljava/lang/Object;Ljava/lang/Object;)V
    //   360: astore 4
    //   362: goto -353 -> 9
    //   365: aconst_null
    //   366: astore 6
    //   368: goto -239 -> 129
    //   371: astore 26
    //   373: ldc_w 801
    //   376: ldc_w 1131
    //   379: aload 26
    //   381: invokestatic 1058	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   384: aconst_null
    //   385: astore 4
    //   387: goto -378 -> 9
    //   390: astore 27
    //   392: ldc_w 801
    //   395: ldc_w 1133
    //   398: aload 27
    //   400: invokestatic 1135	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   403: aconst_null
    //   404: astore 4
    //   406: goto -397 -> 9
    //   409: iconst_0
    //   410: istore 19
    //   412: goto -188 -> 224
    //   415: aload_0
    //   416: invokevirtual 772	com/google/android/music/playback/LocalDevicePlayback:getContext	()Landroid/content/Context;
    //   419: aload 6
    //   421: invokestatic 1141	com/google/android/music/download/cache/CacheUtils:resolveMusicPath	(Landroid/content/Context;Lcom/google/android/music/store/MusicFile;)Ljava/io/File;
    //   424: astore 28
    //   426: aload 28
    //   428: ifnull +11 -> 439
    //   431: aload 28
    //   433: invokevirtual 1146	java/io/File:exists	()Z
    //   436: ifne +9 -> 445
    //   439: iconst_1
    //   440: istore 19
    //   442: goto -218 -> 224
    //   445: iconst_0
    //   446: istore 19
    //   448: goto -224 -> 224
    //   451: iconst_1
    //   452: istore 19
    //   454: goto -230 -> 224
    //   457: iconst_1
    //   458: istore 19
    //   460: goto -236 -> 224
    //   463: iconst_0
    //   464: istore 13
    //   466: goto -224 -> 242
    //   469: ldc2_w 1147
    //   472: lload 8
    //   474: ladd
    //   475: lstore 29
    //   477: aload_0
    //   478: getfield 380	com/google/android/music/playback/LocalDevicePlayback:mPlayList	Lcom/google/android/music/playback/LocalDevicePlayback$MediaListWrapper;
    //   481: invokevirtual 1005	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:length	()I
    //   484: i2l
    //   485: lstore 31
    //   487: lload 29
    //   489: lload 31
    //   491: lcmp
    //   492: iflt +10 -> 502
    //   495: aload_0
    //   496: invokespecial 715	com/google/android/music/playback/LocalDevicePlayback:onPlaybackFailure	()V
    //   499: goto -252 -> 247
    //   502: iload_2
    //   503: ifne +33 -> 536
    //   506: iload_3
    //   507: ifne +29 -> 536
    //   510: aload_0
    //   511: getfield 248	com/google/android/music/playback/LocalDevicePlayback:mRepeatMode	I
    //   514: ifne +22 -> 536
    //   517: aload_0
    //   518: getfield 380	com/google/android/music/playback/LocalDevicePlayback:mPlayList	Lcom/google/android/music/playback/LocalDevicePlayback$MediaListWrapper;
    //   521: invokevirtual 1005	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:length	()I
    //   524: bipush 255
    //   526: iadd
    //   527: istore 33
    //   529: iload 7
    //   531: iload 33
    //   533: if_icmpge -286 -> 247
    //   536: aload_0
    //   537: iconst_1
    //   538: iconst_0
    //   539: iload 7
    //   541: iload_3
    //   542: invokespecial 412	com/google/android/music/playback/LocalDevicePlayback:getNextPlayPosition	(ZZIZ)I
    //   545: istore 7
    //   547: lload 29
    //   549: lstore 8
    //   551: goto -522 -> 29
    //   554: aconst_null
    //   555: astore 4
    //   557: goto -548 -> 9
    //
    // Exception table:
    //   from	to	target	type
    //   110	140	371	java/io/FileNotFoundException
    //   144	170	390	com/google/android/music/store/DataNotFoundException
  }

  private int getNextPlayPosition(boolean paramBoolean1, boolean paramBoolean2, int paramInt, boolean paramBoolean3)
  {
    int i = 1;
    int j = 0;
    int m = -1;
    Looper localLooper1 = Looper.myLooper();
    Looper localLooper2 = AsyncWorkers.sBackendServiceWorker.getLooper();
    if (localLooper1 != localLooper2)
      throw new IllegalStateException("getNextPlayPosition must run on the AsyncWorkers.sBackendServiceWorker handler");
    if (this.mPlayList.length() <= 0)
      Log.e("LocalDevicePlayback", "No play queue");
    label60: int k;
    label147: int i1;
    while (true)
    {
      return m;
      if (shouldPlayInRandomOrder())
      {
        synchronized (this.mFuture)
        {
          if (this.mPlayList.length() > 200)
            fillShuffleList();
          if (this.mFuture.size() != 0)
            break label147;
          if ((this.mRepeatMode != 0) || (paramBoolean1));
        }
        if ((this.mRepeatMode == 2) || (paramBoolean1))
        {
          fillShuffleList();
          if (this.mFuture.size() == 0)
          {
            Throwable localThrowable1 = new Throwable();
            Log.e("LocalDevicePlayback", "Failed to fill future in getNextPlayPosition()", localThrowable1);
          }
        }
        else if (this.mRepeatMode == 1)
        {
          if (paramInt >= 0)
          {
            m = paramInt;
          }
          else
          {
            String str1 = "called getNext in repeat current mode, but mPlayPos wasn't valid: " + paramInt;
            Throwable localThrowable2 = new Throwable();
            Log.wtf("LocalDevicePlayback", str1, localThrowable2);
          }
        }
        else
        {
          StringBuilder localStringBuilder1 = new StringBuilder().append("Shouldn't be here, repeat mode is ");
          int i2 = this.mRepeatMode;
          String str2 = i2;
          Throwable localThrowable3 = new Throwable();
          Log.wtf("LocalDevicePlayback", str2, localThrowable3);
          continue;
          if (paramBoolean2)
            m = ((Integer)this.mFuture.getFirst()).intValue();
          else
            m = ((Integer)this.mFuture.removeFirst()).intValue();
        }
      }
      else if (!paramBoolean3)
      {
        int i3 = this.mPlayList.length() + -1;
        if (paramInt >= i3);
        while (true)
          switch (this.mRepeatMode)
          {
          default:
            StringBuilder localStringBuilder2 = new StringBuilder().append("Unknown repeat mode: ");
            int i4 = this.mRepeatMode;
            String str3 = i4;
            Throwable localThrowable4 = new Throwable();
            Log.wtf("LocalDevicePlayback", str3, localThrowable4);
            break label60;
            i = 0;
          case 1:
          case 0:
          case 2:
          }
        if (paramInt >= 0)
        {
          if (paramBoolean1)
          {
            if (i != 0);
            while (true)
            {
              int n = ???;
              break;
              k = paramInt + 1;
            }
          }
          i1 = paramInt;
        }
        else
        {
          String str4 = "called getNext in repeat current mode, but mPlayPos wasn't valid: " + paramInt;
          Throwable localThrowable5 = new Throwable();
          Log.wtf("LocalDevicePlayback", str4, localThrowable5);
          continue;
          if (i != 0)
          {
            if (paramBoolean1);
            while (true)
            {
              i1 = k;
              break;
              k = -1;
            }
          }
          i1 = paramInt + 1;
          continue;
          if (i != 0)
          {
            if ((this.mLastPlayQueueGroupId == 0L) || (this.mLastPlayQueueGroupPosition < 0))
              i1 = 0;
            else
              i1 = this.mLastPlayQueueGroupPosition;
          }
          else
            i1 = paramInt + 1;
        }
      }
      else
      {
        if (paramInt == 0);
        while (true)
        {
          k = this.mPlayList.length() + -1;
          switch (this.mRepeatMode)
          {
          default:
            StringBuilder localStringBuilder3 = new StringBuilder().append("Unknown repeat mode: ");
            int i5 = this.mRepeatMode;
            String str5 = i5;
            Throwable localThrowable6 = new Throwable();
            Log.wtf("LocalDevicePlayback", str5, localThrowable6);
            break label60;
            i = 0;
          case 1:
          case 2:
          case 0:
          }
        }
        if (paramInt >= 0)
        {
          if (paramBoolean1)
          {
            if (i != 0);
            for (i = k; ; i = paramInt + -1)
            {
              i1 = i;
              break;
            }
          }
          i1 = paramInt;
        }
        else
        {
          String str6 = "called getNext in repeat current mode, but mPlayPos wasn't valid: " + paramInt;
          Throwable localThrowable7 = new Throwable();
          Log.wtf("LocalDevicePlayback", str6, localThrowable7);
          continue;
          if ((this.mLastPlayQueueGroupId != 0L) && (this.mLastPlayQueueGroupPosition > 0))
          {
            int i6 = this.mLastPlayQueueGroupPosition;
            if (paramInt != i6)
              i1 = k;
          }
          else
          {
            if (paramInt <= 0)
              break;
            i1 = paramInt + -1;
          }
        }
      }
    }
    if (paramBoolean1);
    while (true)
    {
      i1 = k;
      break;
      k = -1;
    }
  }

  private ContentIdentifier[] getNextSongs()
  {
    int i = 0;
    int j = Gservices.getInt(getContext().getApplicationContext().getContentResolver(), "music_playlist_prefetch_count", 5);
    synchronized (this.mFuture)
    {
      int m = this.mFuture.size();
      ContentIdentifier[] arrayOfContentIdentifier1;
      if (m > 0)
      {
        int n = new ContentIdentifier[Math.min(m, j)];
        while (true)
        {
          int i2 = n.length;
          if (i >= i2)
            break;
          MediaListWrapper localMediaListWrapper = this.mPlayList;
          int i3 = ((Integer)this.mFuture.get(i)).intValue();
          ContentIdentifier localContentIdentifier1 = localMediaListWrapper.get(i3);
          n[i] = localContentIdentifier1;
          ContentIdentifier.Domain localDomain1 = n[i].getDomain();
          ContentIdentifier.Domain localDomain2 = ContentIdentifier.Domain.SHARED;
          if (localDomain1 == localDomain2)
            n[i] = null;
          i += 1;
        }
        arrayOfContentIdentifier1 = stripNullContentIdentifiers(n);
        return arrayOfContentIdentifier1;
      }
      if (this.mPlayList.length() < arrayOfContentIdentifier1);
      ContentIdentifier[] arrayOfContentIdentifier3;
      int k;
      Object localObject1;
      for (int i4 = this.mPlayList.length(); ; localObject1 = k)
      {
        arrayOfContentIdentifier3 = new ContentIdentifier[i4];
        k = this.mPlayPos;
        if (k < 0)
          k = 0;
        int i1 = 0;
        while (i1 < i4)
        {
          int i5 = this.mPlayList.length();
          if (k >= i5)
            k = 0;
          ContentIdentifier localContentIdentifier2 = this.mPlayList.get(k);
          arrayOfContentIdentifier3[i1] = localContentIdentifier2;
          int i6 = k + 1;
          i1 += 1;
          k = i6;
        }
      }
      ContentIdentifier[] arrayOfContentIdentifier2 = stripNullContentIdentifiers(arrayOfContentIdentifier3);
    }
  }

  private SharedPreferences getPreferences()
  {
    return getContext().getSharedPreferences("Music", 3);
  }

  private long getPreviewPlayDurationForUrl(String paramString)
  {
    PreviewPlaybackInfo localPreviewPlaybackInfo = this.mPreviewPlaybackInfo;
    if ((localPreviewPlaybackInfo != null) && (localPreviewPlaybackInfo.mPreviewUrl != null) && (localPreviewPlaybackInfo.mPreviewUrl.equals(paramString)));
    for (long l = localPreviewPlaybackInfo.mPreviewDuration; ; l = 65535L)
      return l;
  }

  private int getPreviewPlayTypeForUrl(String paramString)
  {
    PreviewPlaybackInfo localPreviewPlaybackInfo = this.mPreviewPlaybackInfo;
    if ((localPreviewPlaybackInfo != null) && (localPreviewPlaybackInfo.mPreviewUrl != null) && (localPreviewPlaybackInfo.mPreviewUrl.equals(paramString)))
      if (this.LOGV)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Use preview duration: ");
        long l = localPreviewPlaybackInfo.mPreviewDuration;
        String str = l;
        Log.d("LocalDevicePlayback", str);
      }
    for (int i = localPreviewPlaybackInfo.mPreviewPlayType; ; i = -1)
      return i;
  }

  private void invalidateNextPlayer()
  {
    try
    {
      if ((this.mPlayer != null) && (this.mPlayer.isInitialized()))
        this.mPlayer.setNextPlayer(null);
      label29: if (this.mNextPlayer == null)
        return;
      if (this.mStreamingClient != null)
        this.mStreamingClient.cancelNextStream();
      this.mNextPlayer.release();
      this.mNextPlayer = null;
      this.mNextPlayPos = -1;
      this.mNextAudioId = null;
      return;
    }
    catch (Exception localException)
    {
      break label29;
    }
  }

  private boolean isGaplessEnabled()
  {
    boolean bool = true;
    if (((mDisableGaplessOverride == null) || ((mDisableGaplessOverride != null) && (!mDisableGaplessOverride.booleanValue()))) && (Build.VERSION.SDK_INT >= 16) && (Gservices.getBoolean(getContext().getApplicationContext().getContentResolver(), "music_gapless_enabled", true)));
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  private static boolean isPlayQueue(SongList paramSongList)
  {
    if ((paramSongList != null) && ((paramSongList instanceof PlayQueueSongList)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private static boolean isQueueable(SongList paramSongList)
  {
    int[] arrayOfInt = 37.$SwitchMap$com$google$android$music$download$ContentIdentifier$Domain;
    int i = paramSongList.getDomain().ordinal();
    switch (arrayOfInt[i])
    {
    default:
    case 1:
    case 2:
    }
    for (boolean bool = false; ; bool = true)
      return bool;
  }

  private boolean isSuggestedMix(SongList paramSongList)
  {
    boolean bool = false;
    if (((paramSongList instanceof PlaylistSongList)) && (((PlaylistSongList)paramSongList).getPlaylistType() == 50))
      bool = true;
    return bool;
  }

  private boolean isUsingPlayQueue()
  {
    return isPlayQueue(this.mMediaList);
  }

  private boolean loadCurrent()
  {
    boolean bool;
    if (!this.mPlayList.isValid())
      bool = false;
    while (true)
    {
      return bool;
      MediaListWrapper localMediaListWrapper = this.mPlayList;
      int i = this.mPlayPos;
      Pair localPair = localMediaListWrapper.getAudioIdAndListItemId(i);
      if (localPair == null)
      {
        bool = false;
      }
      else
      {
        if (this.mLastPlayQueueGroupId != 0L)
        {
          int j = this.mPlayPos;
          int k = this.mLastPlayQueueGroupPosition;
          if (j < k)
          {
            this.mLastPlayQueueGroupId = 0L;
            this.mLastPlayQueueGroupPosition = -1;
          }
        }
        ContentIdentifier localContentIdentifier = (ContentIdentifier)localPair.first;
        if (refreshCursor(localContentIdentifier))
        {
          long l = ((Long)localPair.second).longValue();
          this.mListItemId = l;
          bool = true;
        }
        else
        {
          bool = false;
        }
      }
    }
  }

  private void next(final boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder().append("next: force=").append(paramBoolean).append(", currentPos=");
    int i = this.mPlayPos;
    String str = i;
    Log.i("LocalDevicePlayback", str);
    this.mAsyncWakeLock.acquire();
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local32 = new Runnable()
    {
      public void run()
      {
        try
        {
          LocalDevicePlayback localLocalDevicePlayback1 = LocalDevicePlayback.this;
          boolean bool1 = paramBoolean;
          int i = LocalDevicePlayback.this.mPlayPos;
          int j = localLocalDevicePlayback1.getNextPlayPosition(bool1, false, i, false);
          LocalDevicePlayback localLocalDevicePlayback2 = LocalDevicePlayback.this;
          PlayQueueFeeder.PostProcessingAction localPostProcessingAction = PlayQueueFeeder.PostProcessingAction.NEXT;
          if (!localLocalDevicePlayback2.feedQueueIfNeeded(localPostProcessingAction))
          {
            LocalDevicePlayback localLocalDevicePlayback3 = LocalDevicePlayback.this;
            boolean bool2 = paramBoolean;
            localLocalDevicePlayback3.nextImplSync(bool2, j);
          }
          return;
        }
        finally
        {
          LocalDevicePlayback.this.mAsyncWakeLock.release();
        }
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local32);
  }

  private void nextImplSync(boolean paramBoolean, int paramInt)
  {
    if (paramInt == -1)
    {
      this.mIsSupposedToBePlaying = false;
      onPlaybackComplete();
      onPlayStateChanged();
      return;
    }
    if (this.mPlayPos >= 0)
    {
      LinkedList localLinkedList = this.mHistory;
      Integer localInteger = Integer.valueOf(this.mPlayPos);
      boolean bool1 = localLinkedList.add(localInteger);
      while (this.mHistory.size() > 20)
        Object localObject = this.mHistory.removeFirst();
    }
    saveBookmarkIfNeeded();
    stop(true);
    this.mPlayPos = paramInt;
    LocalDevicePlayback localLocalDevicePlayback = this;
    boolean bool2 = paramBoolean;
    boolean bool3 = false;
    localLocalDevicePlayback.openCurrentAndPrepareToPlaySync(bool2, false, true, bool3, null);
  }

  private void onOpenStarted()
  {
    notifyChange("com.android.music.asyncopenstart");
  }

  private void onPlaybackFailure()
  {
    if (this.mStreamingClient != null)
      this.mStreamingClient.currenStreamingPlayEnded();
    this.mIsSupposedToBePlaying = false;
    notifyFailure();
    saveQueue(false);
  }

  private void onQueueChanged()
  {
    notifyChange("com.android.music.queuechanged");
    if (playlistLoading())
      return;
    saveQueue(true);
  }

  private void onSongChanged()
  {
    notifyChange("com.android.music.metachanged");
    saveQueue(false);
  }

  private void open(final ContentIdentifier paramContentIdentifier, boolean paramBoolean1, final boolean paramBoolean2, long paramLong, MusicFile paramMusicFile)
  {
    Object[] arrayOfObject = new Object[5];
    arrayOfObject[0] = paramContentIdentifier;
    Long localLong = Long.valueOf(paramLong);
    arrayOfObject[1] = localLong;
    Integer localInteger = Integer.valueOf(this.mPlayPos);
    arrayOfObject[2] = localInteger;
    Boolean localBoolean = Boolean.valueOf(paramBoolean1);
    arrayOfObject[3] = localBoolean;
    if (paramMusicFile != null);
    for (String str1 = paramMusicFile.getTitle(); ; str1 = "external")
    {
      arrayOfObject[4] = str1;
      String str2 = String.format("open: id=%s, pos=%s, playPos=%s, fromUser=%s, track=%s", arrayOfObject);
      Log.i("LocalDevicePlayback", str2);
      AsyncMediaPlayer localAsyncMediaPlayer1 = this.mPlayer;
      int i = localAsyncMediaPlayer1.getAudioSessionId();
      invalidateNextPlayer();
      AsyncMediaPlayer localAsyncMediaPlayer2 = createPlayer();
      this.mPlayer = localAsyncMediaPlayer2;
      this.mPlayer.setAudioSessionId(i);
      localAsyncMediaPlayer1.release();
      AsyncMediaPlayer localAsyncMediaPlayer3 = this.mPlayer;
      long l1 = duration();
      AsyncMediaPlayer.AsyncCommandCallback local27 = new AsyncMediaPlayer.AsyncCommandCallback()
      {
        public void onFailure(boolean paramAnonymousBoolean)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("Failed to open MusicId (");
          ContentIdentifier localContentIdentifier = paramContentIdentifier;
          String str = localContentIdentifier + ") for playback";
          Log.w("LocalDevicePlayback", str);
          int i = LocalDevicePlayback.access$6508(LocalDevicePlayback.this);
          if ((paramAnonymousBoolean) && (LocalDevicePlayback.this.mOpenFailedCounter < 10))
          {
            LocalDevicePlayback.this.tryNext();
            return;
          }
          LocalDevicePlayback.this.onPlaybackFailure();
        }

        public void onSuccess()
        {
          int i = LocalDevicePlayback.access$6502(LocalDevicePlayback.this, 0);
          if (LocalDevicePlayback.this.mPlayer.isStreaming())
            LocalDevicePlayback.this.onOpenComplete();
          if (!paramBoolean2)
            return;
          LocalDevicePlayback.this.mService.play();
        }
      };
      ContentIdentifier localContentIdentifier = paramContentIdentifier;
      long l2 = paramLong;
      boolean bool = paramBoolean1;
      MusicFile localMusicFile = paramMusicFile;
      localAsyncMediaPlayer3.setDataSource(localContentIdentifier, l2, l1, true, bool, localMusicFile, local27);
      return;
    }
  }

  private void open(SongList paramSongList, boolean paramBoolean1, int paramInt, boolean paramBoolean2)
  {
    final boolean bool1 = true;
    if (this.LOGV)
    {
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = paramSongList;
      Integer localInteger = Integer.valueOf(paramInt);
      arrayOfObject[1] = localInteger;
      Boolean localBoolean = Boolean.valueOf(paramBoolean2);
      arrayOfObject[2] = localBoolean;
      String str = String.format("open: medialist=%s position=%d play=%b ", arrayOfObject);
      Log.d("LocalDevicePlayback", str);
    }
    long l = System.currentTimeMillis();
    this.mLastUserInteractionTime = l;
    final SongList localSongList1 = this.mMediaList;
    boolean bool2 = paramSongList.equals(localSongList1);
    if (bool2)
    {
      int i = this.mPlayPos;
      if ((paramInt != i) && (isPlaying()))
        return;
    }
    final boolean bool3 = isPlayQueue(localSongList1);
    if ((!bool2) && (bool3) && (paramSongList.isDefaultDomain()))
      bool2 = true;
    if (!bool2)
    {
      if (!bool1)
        break label217;
      SongList localSongList2 = acquireAsyncLockAndClearMediaList();
    }
    while (true)
    {
      LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
      LocalDevicePlayback localLocalDevicePlayback = this;
      final SongList localSongList3 = paramSongList;
      final int j = paramInt;
      final boolean bool4 = paramBoolean1;
      final boolean bool5 = paramBoolean2;
      Runnable local20 = new Runnable()
      {
        boolean shouldClearQueue = false;

        // ERROR //
        public void run()
        {
          // Byte code:
          //   0: aload_0
          //   1: getfield 32	com/google/android/music/playback/LocalDevicePlayback$20:val$medialist	Lcom/google/android/music/medialist/SongList;
          //   4: astore_1
          //   5: aload_0
          //   6: getfield 34	com/google/android/music/playback/LocalDevicePlayback$20:val$position	I
          //   9: istore_2
          //   10: aload_0
          //   11: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   14: astore_3
          //   15: aload_0
          //   16: getfield 32	com/google/android/music/playback/LocalDevicePlayback$20:val$medialist	Lcom/google/android/music/medialist/SongList;
          //   19: astore 4
          //   21: aload_3
          //   22: aload 4
          //   24: invokestatic 54	com/google/android/music/playback/LocalDevicePlayback:access$5700	(Lcom/google/android/music/playback/LocalDevicePlayback;Lcom/google/android/music/medialist/SongList;)Z
          //   27: istore 5
          //   29: iload 5
          //   31: ifeq +22 -> 53
          //   34: aload_0
          //   35: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   38: astore 6
          //   40: aload_0
          //   41: getfield 32	com/google/android/music/playback/LocalDevicePlayback$20:val$medialist	Lcom/google/android/music/medialist/SongList;
          //   44: astore 7
          //   46: aload 6
          //   48: aload 7
          //   50: invokestatic 58	com/google/android/music/playback/LocalDevicePlayback:access$5800	(Lcom/google/android/music/playback/LocalDevicePlayback;Lcom/google/android/music/medialist/SongList;)V
          //   53: iload 5
          //   55: ifne +35 -> 90
          //   58: aload_0
          //   59: getfield 32	com/google/android/music/playback/LocalDevicePlayback$20:val$medialist	Lcom/google/android/music/medialist/SongList;
          //   62: invokestatic 62	com/google/android/music/playback/LocalDevicePlayback:access$5900	(Lcom/google/android/music/medialist/SongList;)Z
          //   65: ifne +25 -> 90
          //   68: aload_0
          //   69: getfield 32	com/google/android/music/playback/LocalDevicePlayback$20:val$medialist	Lcom/google/android/music/medialist/SongList;
          //   72: instanceof 64
          //   75: ifne +15 -> 90
          //   78: aload_0
          //   79: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   82: invokestatic 68	com/google/android/music/playback/LocalDevicePlayback:access$6000	(Lcom/google/android/music/playback/LocalDevicePlayback;)V
          //   85: aload_0
          //   86: iconst_1
          //   87: putfield 49	com/google/android/music/playback/LocalDevicePlayback$20:shouldClearQueue	Z
          //   90: aload_0
          //   91: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   94: invokestatic 72	com/google/android/music/playback/LocalDevicePlayback:access$000	(Lcom/google/android/music/playback/LocalDevicePlayback;)Z
          //   97: ifeq +45 -> 142
          //   100: new 74	java/lang/StringBuilder
          //   103: dup
          //   104: invokespecial 75	java/lang/StringBuilder:<init>	()V
          //   107: ldc 77
          //   109: invokevirtual 81	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   112: astore 8
          //   114: aload_0
          //   115: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   118: invokestatic 85	com/google/android/music/playback/LocalDevicePlayback:access$1500	(Lcom/google/android/music/playback/LocalDevicePlayback;)Lcom/google/android/music/mix/MixGenerationState;
          //   121: astore 9
          //   123: aload 8
          //   125: aload 9
          //   127: invokevirtual 88	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
          //   130: invokevirtual 92	java/lang/StringBuilder:toString	()Ljava/lang/String;
          //   133: astore 10
          //   135: ldc 94
          //   137: aload 10
          //   139: invokestatic 100	com/google/android/music/log/Log:d	(Ljava/lang/String;Ljava/lang/String;)V
          //   142: aconst_null
          //   143: astore 11
          //   145: aload_0
          //   146: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   149: invokestatic 85	com/google/android/music/playback/LocalDevicePlayback:access$1500	(Lcom/google/android/music/playback/LocalDevicePlayback;)Lcom/google/android/music/mix/MixGenerationState;
          //   152: ifnull +17 -> 169
          //   155: aload_0
          //   156: iconst_1
          //   157: putfield 49	com/google/android/music/playback/LocalDevicePlayback$20:shouldClearQueue	Z
          //   160: aload_0
          //   161: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   164: invokestatic 104	com/google/android/music/playback/LocalDevicePlayback:access$6100	(Lcom/google/android/music/playback/LocalDevicePlayback;)Lcom/google/android/music/download/ContentIdentifier;
          //   167: astore 11
          //   169: aload_0
          //   170: getfield 32	com/google/android/music/playback/LocalDevicePlayback$20:val$medialist	Lcom/google/android/music/medialist/SongList;
          //   173: invokestatic 62	com/google/android/music/playback/LocalDevicePlayback:access$5900	(Lcom/google/android/music/medialist/SongList;)Z
          //   176: ifne +387 -> 563
          //   179: aload_0
          //   180: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   183: astore 12
          //   185: aload_0
          //   186: getfield 32	com/google/android/music/playback/LocalDevicePlayback$20:val$medialist	Lcom/google/android/music/medialist/SongList;
          //   189: astore 13
          //   191: aload_0
          //   192: getfield 36	com/google/android/music/playback/LocalDevicePlayback$20:val$shuffleList	Z
          //   195: istore 14
          //   197: aload_0
          //   198: getfield 34	com/google/android/music/playback/LocalDevicePlayback$20:val$position	I
          //   201: istore 15
          //   203: aload_0
          //   204: getfield 49	com/google/android/music/playback/LocalDevicePlayback$20:shouldClearQueue	Z
          //   207: istore 16
          //   209: aload 12
          //   211: aload 13
          //   213: iload 14
          //   215: iload 15
          //   217: iload 16
          //   219: invokestatic 108	com/google/android/music/playback/LocalDevicePlayback:access$6200	(Lcom/google/android/music/playback/LocalDevicePlayback;Lcom/google/android/music/medialist/SongList;ZIZ)Lcom/google/android/music/store/PlayQueueInsertResult;
          //   222: astore 17
          //   224: aload 17
          //   226: ifnull +65 -> 291
          //   229: aload_0
          //   230: getfield 38	com/google/android/music/playback/LocalDevicePlayback$20:val$currentlyPlayingFromPlayQ	Z
          //   233: ifeq +319 -> 552
          //   236: aload_0
          //   237: getfield 40	com/google/android/music/playback/LocalDevicePlayback$20:val$currentList	Lcom/google/android/music/medialist/SongList;
          //   240: astore_1
          //   241: aload 17
          //   243: invokevirtual 114	com/google/android/music/store/PlayQueueInsertResult:getNewPlayPosition	()I
          //   246: istore_2
          //   247: aload_0
          //   248: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   251: astore 18
          //   253: aload 17
          //   255: invokevirtual 118	com/google/android/music/store/PlayQueueInsertResult:getGroupId	()J
          //   258: lstore 19
          //   260: aload 18
          //   262: lload 19
          //   264: invokestatic 122	com/google/android/music/playback/LocalDevicePlayback:access$2902	(Lcom/google/android/music/playback/LocalDevicePlayback;J)J
          //   267: lstore 21
          //   269: aload_0
          //   270: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   273: astore 23
          //   275: aload 17
          //   277: invokevirtual 125	com/google/android/music/store/PlayQueueInsertResult:getGroupPosition	()I
          //   280: istore 24
          //   282: aload 23
          //   284: iload 24
          //   286: invokestatic 129	com/google/android/music/playback/LocalDevicePlayback:access$3002	(Lcom/google/android/music/playback/LocalDevicePlayback;I)I
          //   289: istore 25
          //   291: aload_0
          //   292: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   295: aload_1
          //   296: invokestatic 132	com/google/android/music/playback/LocalDevicePlayback:access$6400	(Lcom/google/android/music/playback/LocalDevicePlayback;Lcom/google/android/music/medialist/SongList;)V
          //   299: aload_0
          //   300: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   303: invokestatic 136	com/google/android/music/playback/LocalDevicePlayback:access$2800	(Lcom/google/android/music/playback/LocalDevicePlayback;)Lcom/google/android/music/playback/LocalDevicePlayback$MediaListWrapper;
          //   306: aload_1
          //   307: invokevirtual 142	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:setMediaList	(Lcom/google/android/music/medialist/SongList;)V
          //   310: aload_0
          //   311: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   314: iload_2
          //   315: invokestatic 145	com/google/android/music/playback/LocalDevicePlayback:access$1102	(Lcom/google/android/music/playback/LocalDevicePlayback;I)I
          //   318: istore 26
          //   320: aload_0
          //   321: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   324: iconst_0
          //   325: invokestatic 148	com/google/android/music/playback/LocalDevicePlayback:access$6502	(Lcom/google/android/music/playback/LocalDevicePlayback;I)I
          //   328: istore 27
          //   330: aload_0
          //   331: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   334: ldc2_w 149
          //   337: invokestatic 153	com/google/android/music/playback/LocalDevicePlayback:access$6602	(Lcom/google/android/music/playback/LocalDevicePlayback;J)J
          //   340: lstore 28
          //   342: iload 5
          //   344: ifeq +10 -> 354
          //   347: aload_0
          //   348: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   351: invokestatic 156	com/google/android/music/playback/LocalDevicePlayback:access$6700	(Lcom/google/android/music/playback/LocalDevicePlayback;)V
          //   354: aload_0
          //   355: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   358: astore 30
          //   360: getstatic 162	com/google/android/music/mix/PlayQueueFeeder$PostProcessingAction:FEED	Lcom/google/android/music/mix/PlayQueueFeeder$PostProcessingAction;
          //   363: astore 31
          //   365: aload 30
          //   367: aload 31
          //   369: invokestatic 166	com/google/android/music/playback/LocalDevicePlayback:access$3200	(Lcom/google/android/music/playback/LocalDevicePlayback;Lcom/google/android/music/mix/PlayQueueFeeder$PostProcessingAction;)Z
          //   372: istore 32
          //   374: aload_0
          //   375: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   378: invokestatic 170	com/google/android/music/playback/LocalDevicePlayback:access$6800	(Lcom/google/android/music/playback/LocalDevicePlayback;)Ljava/util/LinkedList;
          //   381: astore 33
          //   383: aload 33
          //   385: monitorenter
          //   386: aload_0
          //   387: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   390: invokestatic 173	com/google/android/music/playback/LocalDevicePlayback:access$6900	(Lcom/google/android/music/playback/LocalDevicePlayback;)Ljava/util/LinkedList;
          //   393: invokevirtual 178	java/util/LinkedList:clear	()V
          //   396: aload_0
          //   397: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   400: invokestatic 170	com/google/android/music/playback/LocalDevicePlayback:access$6800	(Lcom/google/android/music/playback/LocalDevicePlayback;)Ljava/util/LinkedList;
          //   403: invokevirtual 178	java/util/LinkedList:clear	()V
          //   406: aload_0
          //   407: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   410: invokestatic 181	com/google/android/music/playback/LocalDevicePlayback:access$7000	(Lcom/google/android/music/playback/LocalDevicePlayback;)Z
          //   413: ifeq +80 -> 493
          //   416: aload_0
          //   417: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   420: invokestatic 185	com/google/android/music/playback/LocalDevicePlayback:access$7100	(Lcom/google/android/music/playback/LocalDevicePlayback;)Lcom/google/android/music/StrictShuffler;
          //   423: astore 34
          //   425: aload_0
          //   426: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   429: invokestatic 136	com/google/android/music/playback/LocalDevicePlayback:access$2800	(Lcom/google/android/music/playback/LocalDevicePlayback;)Lcom/google/android/music/playback/LocalDevicePlayback$MediaListWrapper;
          //   432: invokevirtual 188	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:length	()I
          //   435: istore 35
          //   437: aload 34
          //   439: iload 35
          //   441: invokevirtual 194	com/google/android/music/StrictShuffler:setHistorySize	(I)V
          //   444: aload_0
          //   445: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   448: invokestatic 198	com/google/android/music/playback/LocalDevicePlayback:access$1100	(Lcom/google/android/music/playback/LocalDevicePlayback;)I
          //   451: iflt +28 -> 479
          //   454: aload_0
          //   455: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   458: invokestatic 185	com/google/android/music/playback/LocalDevicePlayback:access$7100	(Lcom/google/android/music/playback/LocalDevicePlayback;)Lcom/google/android/music/StrictShuffler;
          //   461: astore 36
          //   463: aload_0
          //   464: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   467: invokestatic 198	com/google/android/music/playback/LocalDevicePlayback:access$1100	(Lcom/google/android/music/playback/LocalDevicePlayback;)I
          //   470: istore 37
          //   472: aload 36
          //   474: iload 37
          //   476: invokevirtual 201	com/google/android/music/StrictShuffler:injectHistoricalValue	(I)V
          //   479: aload_0
          //   480: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   483: invokestatic 204	com/google/android/music/playback/LocalDevicePlayback:access$7200	(Lcom/google/android/music/playback/LocalDevicePlayback;)V
          //   486: aload_0
          //   487: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   490: invokestatic 207	com/google/android/music/playback/LocalDevicePlayback:access$7300	(Lcom/google/android/music/playback/LocalDevicePlayback;)V
          //   493: aload 33
          //   495: monitorexit
          //   496: aload_0
          //   497: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   500: invokestatic 210	com/google/android/music/playback/LocalDevicePlayback:access$7400	(Lcom/google/android/music/playback/LocalDevicePlayback;)V
          //   503: aload_0
          //   504: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   507: astore 38
          //   509: aload_0
          //   510: getfield 42	com/google/android/music/playback/LocalDevicePlayback$20:val$play	Z
          //   513: istore 39
          //   515: aload 38
          //   517: iconst_1
          //   518: iconst_0
          //   519: iload 39
          //   521: iconst_0
          //   522: aload 11
          //   524: invokestatic 214	com/google/android/music/playback/LocalDevicePlayback:access$7500	(Lcom/google/android/music/playback/LocalDevicePlayback;ZZZZLcom/google/android/music/download/ContentIdentifier;)V
          //   527: aload_0
          //   528: getfield 44	com/google/android/music/playback/LocalDevicePlayback$20:val$listChanged	Z
          //   531: ifeq +10 -> 541
          //   534: aload_0
          //   535: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   538: invokestatic 217	com/google/android/music/playback/LocalDevicePlayback:access$1600	(Lcom/google/android/music/playback/LocalDevicePlayback;)V
          //   541: aload_0
          //   542: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   545: invokestatic 221	com/google/android/music/playback/LocalDevicePlayback:access$1000	(Lcom/google/android/music/playback/LocalDevicePlayback;)Landroid/os/PowerManager$WakeLock;
          //   548: invokevirtual 226	android/os/PowerManager$WakeLock:release	()V
          //   551: return
          //   552: aload_0
          //   553: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   556: invokestatic 230	com/google/android/music/playback/LocalDevicePlayback:access$6300	(Lcom/google/android/music/playback/LocalDevicePlayback;)Lcom/google/android/music/medialist/SongList;
          //   559: astore_1
          //   560: goto -319 -> 241
          //   563: aload_0
          //   564: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   567: ldc2_w 231
          //   570: invokestatic 122	com/google/android/music/playback/LocalDevicePlayback:access$2902	(Lcom/google/android/music/playback/LocalDevicePlayback;J)J
          //   573: lstore 40
          //   575: aload_0
          //   576: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   579: bipush 255
          //   581: invokestatic 129	com/google/android/music/playback/LocalDevicePlayback:access$3002	(Lcom/google/android/music/playback/LocalDevicePlayback;I)I
          //   584: istore 42
          //   586: goto -295 -> 291
          //   589: astore 43
          //   591: aload_0
          //   592: getfield 30	com/google/android/music/playback/LocalDevicePlayback$20:this$0	Lcom/google/android/music/playback/LocalDevicePlayback;
          //   595: invokestatic 221	com/google/android/music/playback/LocalDevicePlayback:access$1000	(Lcom/google/android/music/playback/LocalDevicePlayback;)Landroid/os/PowerManager$WakeLock;
          //   598: invokevirtual 226	android/os/PowerManager$WakeLock:release	()V
          //   601: aload 43
          //   603: athrow
          //   604: astore 44
          //   606: aload 33
          //   608: monitorexit
          //   609: aload 44
          //   611: athrow
          //
          // Exception table:
          //   from	to	target	type
          //   0	386	589	finally
          //   496	541	589	finally
          //   552	586	589	finally
          //   609	612	589	finally
          //   386	496	604	finally
          //   606	609	604	finally
        }
      };
      AsyncWorkers.runAsync(localLoggableHandler, local20);
      return;
      bool1 = false;
      break;
      label217: this.mAsyncWakeLock.acquire();
    }
  }

  private void openCurrentAndNext(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, long paramLong, ContentIdentifier paramContentIdentifier)
  {
    if (this.LOGV)
      Log.d("LocalDevicePlayback", "openCurrentAndNext");
    this.mMediaplayerHandler.removeMessages(7);
    resetCurrentSongMetaDataCursor();
    if (!this.mPlayList.isValid())
    {
      stop(false);
      onOpenComplete();
      return;
    }
    int i = this.mPlayPos;
    Pair localPair = findPlayableSong(i, paramBoolean1, paramBoolean4);
    if (localPair == null)
    {
      stop(false);
      this.mIsSupposedToBePlaying = false;
      onPlaybackComplete();
      onPlayStateChanged();
      return;
    }
    int j = ((Integer)localPair.first).intValue();
    this.mPlayPos = j;
    MusicFile localMusicFile = (MusicFile)localPair.second;
    if (!loadCurrent())
    {
      onOpenComplete();
      return;
    }
    if ((this.mAudioId.equals(paramContentIdentifier)) && (this.mIsSupposedToBePlaying));
    while (true)
    {
      setNextTrack();
      return;
      if (isPlayingLocally())
        this.mPlayer.stop();
      if ((!paramBoolean3) && (this.mIsSupposedToBePlaying))
      {
        this.mIsSupposedToBePlaying = false;
        onPlayStateChanged();
      }
      ContentIdentifier localContentIdentifier = this.mAudioId;
      LocalDevicePlayback localLocalDevicePlayback = this;
      boolean bool1 = paramBoolean1;
      boolean bool2 = paramBoolean2;
      long l1 = paramLong;
      localLocalDevicePlayback.open(localContentIdentifier, bool1, bool2, l1, localMusicFile);
      if (isPodcast())
      {
        long l2 = getBookmark() - 5000L;
        long l3 = seek(l2);
      }
    }
  }

  private void openCurrentAndPlay(final boolean paramBoolean)
  {
    this.mAsyncWakeLock.acquire();
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local25 = new Runnable()
    {
      public void run()
      {
        LocalDevicePlayback localLocalDevicePlayback = LocalDevicePlayback.this;
        boolean bool1 = paramBoolean;
        boolean bool2 = true;
        localLocalDevicePlayback.openCurrentAndPrepareToPlaySync(bool1, true, bool2, false, null);
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local25);
  }

  private void openCurrentAndPrepareToPlaySync(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, ContentIdentifier paramContentIdentifier)
  {
    PowerManager.WakeLock localWakeLock = null;
    while (true)
    {
      try
      {
        int j = this.mPlayList.length();
        if (j == 0)
        {
          if (!paramBoolean2)
            return;
          localWakeLock = this.mAsyncWakeLock;
          localWakeLock.release();
          return;
        }
        int i;
        if ((this.mPlayPos == -1) || (this.mPlayPos < 0))
          i = 1;
        if (shouldPlayInRandomOrder())
        {
          int k = this.mFuture;
          boolean bool1;
          try
          {
            fillShuffleList();
            if (i != null)
            {
              if (this.mFuture.size() == 0)
              {
                Log.d("LocalDevicePlayback", "Failed to fill future in openCurrentAndPlaySync()");
                if (!paramBoolean2)
                  return;
                localObject1 = this.mAsyncWakeLock;
                continue;
              }
              int m = ((Integer)this.mFuture.remove(0)).intValue();
              this.mPlayPos = m;
            }
            long l = 0L;
            Object localObject1 = this;
            bool1 = paramBoolean1;
            boolean bool2 = paramBoolean3;
            boolean bool3 = paramBoolean3;
            boolean bool4 = paramBoolean4;
            ContentIdentifier localContentIdentifier = paramContentIdentifier;
            ((LocalDevicePlayback)localObject1).openCurrentAndNext(bool1, bool2, bool3, bool4, l, localContentIdentifier);
            if (!paramBoolean2)
              return;
            localObject1 = this.mAsyncWakeLock;
            continue;
          }
          finally
          {
          }
        }
      }
      finally
      {
        if (paramBoolean2)
          this.mAsyncWakeLock.release();
      }
      if (localObject2 != null)
      {
        Object localObject3 = null;
        this.mPlayPos = localObject3;
      }
    }
  }

  private void pause(final boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder().append("pause: transient=").append(paramBoolean).append(", currentPos=");
    int i = this.mPlayPos;
    String str = i;
    Log.i("LocalDevicePlayback", str);
    this.mAsyncWakeLock.acquire();
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local30 = new Runnable()
    {
      public void run()
      {
        try
        {
          LocalDevicePlayback localLocalDevicePlayback = LocalDevicePlayback.this;
          boolean bool = paramBoolean;
          localLocalDevicePlayback.pauseSync(bool);
          return;
        }
        finally
        {
          LocalDevicePlayback.this.mAsyncWakeLock.release();
        }
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local30);
  }

  private void pauseSync(boolean paramBoolean)
  {
    ContentIdentifier localContentIdentifier = getAudioId();
    if (localContentIdentifier != null)
    {
      Object[] arrayOfObject = new Object[2];
      String str1 = localContentIdentifier.toString();
      arrayOfObject[0] = str1;
      String str2 = this.mPlayer.getRemoteSongId();
      arrayOfObject[1] = str2;
      int i = EventLog.writeEvent(74002, arrayOfObject);
      StringBuilder localStringBuilder1 = new StringBuilder().append("Event logging MUSIC_PAUSE_PLAYBACK_REQUESTED: ");
      String str3 = localContentIdentifier.toString();
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str3).append("/");
      String str4 = this.mPlayer.getRemoteSongId();
      String str5 = str4;
      Log.d("LocalDevicePlayback", str5);
    }
    while (true)
    {
      try
      {
        this.mMediaplayerHandler.removeMessages(6);
        this.mMediaplayerHandler.removeMessages(7);
        if (isPlaying())
        {
          this.mPlayer.pause();
          this.mIsSupposedToBePlaying = false;
          onPlayStateChanged();
          saveBookmarkIfNeeded();
          this.mPausedByTransientLossOfFocus = paramBoolean;
          return;
        }
        if (isPreparing())
        {
          stopSync();
          continue;
        }
      }
      finally
      {
      }
      if ((this.mPausedByTransientLossOfFocus) && (!paramBoolean))
        this.mPausedByTransientLossOfFocus = false;
    }
  }

  private PlayQueueAddResult queue(SongList paramSongList, long paramLong)
  {
    PlayQueueAddResult localPlayQueueAddResult = null;
    if (this.LOGV)
    {
      String str = "queue: currentListItemId=" + paramLong;
      Log.d("LocalDevicePlayback", str);
    }
    if (!isQueueable(paramSongList));
    while (true)
    {
      return localPlayQueueAddResult;
      if ((paramSongList instanceof ExternalSongList))
      {
        ExternalSongList localExternalSongList = (ExternalSongList)paramSongList;
        paramSongList = addToDatabase(localExternalSongList);
        if (paramSongList == null)
          continue;
      }
      Context localContext1 = getContext();
      String[] arrayOfString = new String[1];
      arrayOfString[0] = "audio_id";
      MediaList.MediaCursor localMediaCursor = paramSongList.getSyncMediaCursor(localContext1, arrayOfString, null);
      if (localMediaCursor == null)
        continue;
      try
      {
        Store localStore = Store.getInstance(getContext());
        if (this.mShuffleMode == 1)
        {
          localPlayQueueAddResult = null;
          localPlayQueueAddResult = localStore.addToPlayQueue(localMediaCursor, paramLong, localPlayQueueAddResult);
          if (localPlayQueueAddResult != null)
          {
            Context localContext2 = getContext();
            int i = localPlayQueueAddResult.getAddedSize();
            int j = localPlayQueueAddResult.getRequestedSize();
            MusicUtils.showSongsToAddToQueue(localContext2, i, j, 1000);
          }
          Store.safeClose(localMediaCursor);
          continue;
        }
        localPlayQueueAddResult = null;
      }
      finally
      {
        Store.safeClose(localMediaCursor);
      }
    }
  }

  private PlayQueueInsertResult queueAndPlay(SongList paramSongList, boolean paramBoolean1, int paramInt, boolean paramBoolean2)
  {
    Object localObject1;
    if (!isQueueable(paramSongList))
      localObject1 = null;
    SongList localSongList;
    MediaList.MediaCursor localMediaCursor1;
    while (true)
    {
      return localObject1;
      if ((paramSongList instanceof ExternalSongList))
      {
        ExternalSongList localExternalSongList = (ExternalSongList)paramSongList;
        localSongList = addToDatabase(localExternalSongList);
        if (localSongList == null)
          localObject1 = null;
      }
      else
      {
        localSongList = paramSongList;
        Context localContext = getContext();
        String[] arrayOfString = new String[1];
        arrayOfString[0] = "audio_id";
        localMediaCursor1 = localSongList.getSyncMediaCursor(localContext, arrayOfString, null);
        if (localMediaCursor1 != null)
          break;
        localObject1 = null;
      }
    }
    while (true)
    {
      long l2;
      try
      {
        Store localStore1 = Store.getInstance(getContext());
        if ((paramBoolean1) || (this.mShuffleMode == 1))
        {
          bool1 = true;
          if (paramInt == -1)
            continue;
          localStore2 = null;
          if (localStore2 != null)
            break label300;
          i = 0;
          l2 = 0;
          if ((!(localSongList instanceof PlaylistSongList)) || (((PlaylistSongList)localSongList).getPlaylistType() != 0))
            break label293;
          l1 = 1;
          l2 = 65535L;
          if ((this.mLastPlayQueueGroupPosition >= 0) && (this.mLastPlayQueueGroupId != 0L))
            l2 = this.mLastPlayQueueGroupId;
          if (paramBoolean2)
            localStore1.clearPlayQueue();
          if (localStore2 == null)
            continue;
          localPlayQueueInsertResult = localStore1.addSongsToPlay(localMediaCursor1, l1, bool1, i, l2);
          localObject1 = localPlayQueueInsertResult;
          Store.safeClose(localMediaCursor1);
          break;
        }
        boolean bool1 = false;
        continue;
        Store localStore2 = null;
        continue;
        localStore2 = localStore1;
        MediaList.MediaCursor localMediaCursor2 = localMediaCursor1;
        long l3 = l1;
        boolean bool2 = bool1;
        long l4 = l2;
        PlayQueueInsertResult localPlayQueueInsertResult = localStore2.addSongsToPlay(localMediaCursor2, l3, bool2, l4);
        localObject1 = localPlayQueueInsertResult;
        continue;
      }
      finally
      {
        Store.safeClose(localMediaCursor1);
      }
      label293: long l1 = l2;
      continue;
      label300: int i = paramInt;
    }
  }

  private boolean refreshCursor(ContentIdentifier paramContentIdentifier)
  {
    boolean bool;
    if (paramContentIdentifier == null)
    {
      MusicUtils.debugLog(new Exception());
      String str1 = "Invalid audio identifier:" + paramContentIdentifier;
      Log.wtf("LocalDevicePlayback", str1);
      bool = false;
    }
    while (true)
    {
      return bool;
      if (this.mMediaList == null)
      {
        bool = false;
        continue;
      }
      SongList localSongList = this.mMediaList;
      Context localContext = getContext();
      String[] arrayOfString = this.mCursorCols;
      Cursor localCursor = localSongList.getSongCursor(localContext, paramContentIdentifier, arrayOfString);
      MusicUtils.debugLog("Queried ID " + paramContentIdentifier);
      synchronized (this.mCurrentSongMetaDataCursor)
      {
        Store.safeClose((Cursor)this.mCurrentSongMetaDataCursor.get());
        this.mCurrentSongMetaDataCursor.set(null);
        if ((localCursor != null) && (localCursor.moveToFirst()))
        {
          this.mCurrentSongMetaDataCursor.set(localCursor);
          this.mAudioId = paramContentIdentifier;
          bool = true;
          onSongChanged();
          continue;
        }
        String str2 = "refreshCursor(" + paramContentIdentifier + ") failed";
        Log.e("LocalDevicePlayback", str2);
        Store.safeClose(localCursor);
        bool = false;
      }
    }
  }

  private final void registerExternalStorageListener()
  {
    if (this.mUnmountReceiver != null)
      return;
    BroadcastReceiver local16 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        String str1 = paramAnonymousIntent.getAction();
        if (str1.equals("android.intent.action.MEDIA_EJECT"))
        {
          LocalDevicePlayback.this.saveQueue(true);
          boolean bool = LocalDevicePlayback.access$5002(LocalDevicePlayback.this, false);
          LocalDevicePlayback localLocalDevicePlayback = LocalDevicePlayback.this;
          String str2 = paramAnonymousIntent.getData().getPath();
          localLocalDevicePlayback.closeExternalStorageFiles(str2);
          return;
        }
        if (!str1.equals("android.intent.action.MEDIA_MOUNTED"))
          return;
        int i = LocalDevicePlayback.access$5208(LocalDevicePlayback.this);
        LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            LocalDevicePlayback.this.reloadQueue();
            boolean bool = LocalDevicePlayback.access$5002(LocalDevicePlayback.this, true);
          }
        };
        AsyncWorkers.runAsync(localLoggableHandler, local1);
      }
    };
    this.mUnmountReceiver = local16;
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.intent.action.MEDIA_EJECT");
    localIntentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
    localIntentFilter.addDataScheme("file");
    Context localContext = getContext();
    BroadcastReceiver localBroadcastReceiver = this.mUnmountReceiver;
    Intent localIntent = localContext.registerReceiver(localBroadcastReceiver, localIntentFilter);
  }

  private void releasePlayers()
  {
    if (this.mPlayer != null)
      this.mPlayer.release();
    if (this.mStreamingClient != null)
      this.mStreamingClient.cancelNextStream();
    if (this.mNextPlayer == null)
      return;
    this.mNextPlayer.release();
  }

  private void reloadQueue()
  {
    reloadQueue(true);
  }

  private void reloadQueue(boolean paramBoolean)
  {
    this.mReloadedQueueSeekPos = 65535L;
    SharedPreferences localSharedPreferences = getPreferences();
    String str1 = localSharedPreferences.getString("medialist", null);
    MusicUtils.debugLog("reloading queue from: " + str1);
    if (str1 == null)
      return;
    MediaList localMediaList = MediaList.thaw(str1);
    if ((localMediaList == null) || (!(localMediaList instanceof SongList)))
    {
      String str2 = "LocalDevicePlayback";
      StringBuilder localStringBuilder1 = new StringBuilder().append("Saved media list must be a SongList, but got ");
      if (localMediaList == null);
      for (String str3 = "<null>"; ; str3 = localMediaList.getClass().getName())
      {
        String str4 = str3 + " instead";
        Log.e(str2, str4);
        MusicUtils.debugLog("error thawing: " + localMediaList);
        onQueueChanged();
        return;
      }
    }
    long l1 = localSharedPreferences.getLong("lastUserInteract", 0L);
    this.mLastUserInteractionTime = l1;
    int i = localSharedPreferences.getInt("repeatMode", 0);
    if ((i != 2) && (i != 1))
      i = 0;
    this.mRepeatMode = i;
    int j = localSharedPreferences.getInt("shufflemode", 0);
    if (j != 1)
      j = 0;
    this.mShuffleMode = j;
    String str5 = localSharedPreferences.getString("infiniteMixSeedId", null);
    String str6 = localSharedPreferences.getString("infiniteMixName", "");
    String str7 = localSharedPreferences.getString("infiniteMixArtLocation", null);
    int k = localSharedPreferences.getInt("infiniteMixType", -1);
    boolean bool = localSharedPreferences.getBoolean("infiniteMixSeedTypeLocal", false);
    Object localObject1 = null;
    Object localObject2;
    int m;
    if ((!TextUtils.isEmpty(str5)) && (k != -1))
    {
      localObject2 = MixDescriptor.Type.values()[k];
      if (bool)
      {
        m = Long.valueOf(str5).longValue();
        if (m == 65535L);
      }
    }
    while (true)
    {
      try
      {
        localObject3 = new MixDescriptor(m, (MixDescriptor.Type)localObject2, str6, str7);
        if (localObject3 != null)
        {
          if (this.LOGV)
          {
            String str8 = "Restored mix: " + localObject3;
            Log.d("LocalDevicePlayback", str8);
          }
          enableInfiniteMixMode((MixDescriptor)localObject3);
        }
        SongList localSongList1 = (SongList)localMediaList;
        setMediaList(localSongList1);
        MediaListWrapper localMediaListWrapper1 = this.mPlayList;
        SongList localSongList2 = this.mMediaList;
        localMediaListWrapper1.setMediaList(localSongList2);
        if (this.mPlayList.length() > 0)
          break label626;
        StringBuilder localStringBuilder2 = new StringBuilder().append("Empty playlist ");
        int n = this.mPlayList.length();
        MusicUtils.debugLog(n);
        this.mMediaList = null;
        this.mPlayList.setMediaList(null);
        onQueueChanged();
        return;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        String str9 = "Failed to parse out the long from local seed id: " + str5;
        Log.w("LocalDevicePlayback", str9);
        localObject3 = localObject1;
        continue;
      }
      Log.w("LocalDevicePlayback", "Failed to load mix");
      Object localObject3 = localObject1;
      continue;
      localObject3 = new MixDescriptor(str5, (MixDescriptor.Type)localObject2, str6, str7);
      continue;
      int i1 = MixDescriptor.Type.LUCKY_RADIO.ordinal();
      if (k != i1)
      {
        localObject3 = MixDescriptor.getLuckyRadio(getContext());
        continue;
        label626: int i2 = localSharedPreferences.getInt("curpos", 0);
        long l3 = localSharedPreferences.getLong("curAudioId", 0L);
        localObject2 = localSharedPreferences.getString("curDomainId", null);
        long l2 = localSharedPreferences.getLong("curListItemId", 0L);
        if ((l3 != 0L) && (localObject2 != null))
        {
          this.mPlayPos = i2;
          ContentIdentifier.Domain localDomain = ContentIdentifier.Domain.valueOf((String)localObject2);
          ContentIdentifier localContentIdentifier1 = new ContentIdentifier(l3, localDomain);
          this.mAudioId = localContentIdentifier1;
          this.mListItemId = l2;
          if (!this.mPlayList.refreshCurrentSongPosition())
            break label1269;
          int i3 = this.mPlayPos;
          if (i2 != i3)
          {
            StringBuilder localStringBuilder3 = new StringBuilder().append("Current song postion changed from ").append(i2).append(" to ");
            int i4 = this.mPlayPos;
            MusicUtils.debugLog(i4);
            i2 = this.mPlayPos;
          }
        }
        while (true)
        {
          long l4 = localSharedPreferences.getLong("playQGroupId", 0L);
          this.mLastPlayQueueGroupId = l4;
          int i5 = localSharedPreferences.getInt("playQGroupPos", -1);
          this.mLastPlayQueueGroupPosition = i5;
          this.mPlayList.refreshLastGroupPosition();
          int i6 = this.mPlayList.length();
          if ((i2 < 0) || (i2 >= i6))
          {
            MusicUtils.debugLog("position out of range: " + i2 + "/" + i6);
            if (this.mRepeatMode == 2)
              i2 = 0;
          }
          else
          {
            label917: if ((this.mMediaList != null) && (!isPlayQueue(this.mMediaList)) && (this.mMediaList.isDefaultDomain()))
            {
              Log.i("LocalDevicePlayback", "Upgrading to queue");
              SongList localSongList3 = this.mMediaList;
              PlayQueueInsertResult localPlayQueueInsertResult = queueAndPlay(localSongList3, false, i2, false);
              if ((localPlayQueueInsertResult == null) || (localPlayQueueInsertResult.getGroupId() == 0L) || (localPlayQueueInsertResult.getNewPlayPosition() < 0))
                break label1288;
              SongList localSongList4 = createPlayQueueSongList();
              setMediaList(localSongList4);
              MediaListWrapper localMediaListWrapper2 = this.mPlayList;
              SongList localSongList5 = this.mMediaList;
              localMediaListWrapper2.setMediaList(localSongList5);
              i2 = localPlayQueueInsertResult.getNewPlayPosition();
            }
            this.mPlayPos = i2;
            long l5 = localSharedPreferences.getLong("seekpos", 0L);
            this.mReloadedQueueSeekPos = l5;
            this.mOpenFailedCounter = 20;
            if (paramBoolean)
              stop(false);
            if (!loadCurrent())
              this.mReloadedQueueSeekPos = 0L;
            if (shouldPlayInRandomOrder())
            {
              StrictShuffler localStrictShuffler1 = this.mRand;
              int i7 = this.mPlayList.length();
              localStrictShuffler1.setHistorySize(i7);
              if (this.mPlayPos >= 0)
              {
                StrictShuffler localStrictShuffler2 = this.mRand;
                int i8 = this.mPlayPos;
                localStrictShuffler2.injectHistoricalValue(i8);
              }
            }
          }
          synchronized (this.mFuture)
          {
            fillShuffleList();
            dumpPastPresentAndFuture();
            onQueueChanged();
            StringBuilder localStringBuilder4 = new StringBuilder().append("queue reloaded with length ");
            int i9 = this.mPlayList.length();
            StringBuilder localStringBuilder5 = localStringBuilder4.append(i9).append(", shuffle mode ");
            int i10 = this.mShuffleMode;
            StringBuilder localStringBuilder6 = localStringBuilder5.append(i10).append(", playpos ");
            int i11 = this.mPlayPos;
            StringBuilder localStringBuilder7 = localStringBuilder6.append(i11).append(", id ");
            ContentIdentifier localContentIdentifier2 = this.mAudioId;
            MusicUtils.debugLog(localContentIdentifier2);
            return;
            label1269: MusicUtils.debugLog("Could not locate current song when restoring queue.");
            continue;
            i2 = i6 + -1;
            break label917;
            label1288: this.mMediaList = null;
            this.mPlayList.setMediaList(null);
          }
        }
      }
      else
      {
        localObject3 = localObject1;
      }
    }
  }

  private void requestCreateRadioStationSync()
  {
    if (this.mMixGenerationState == null)
    {
      Log.w("LocalDevicePlayback", "Missing mix state");
      return;
    }
    MixGenerationState localMixGenerationState = this.mMixGenerationState;
    MixGenerationState.State localState = MixGenerationState.State.NOT_STARTED;
    localMixGenerationState.setState(localState);
    PlayQueueFeeder localPlayQueueFeeder = this.mPlayQueueFeeder;
    MixDescriptor localMixDescriptor = this.mMixGenerationState.getMix();
    PlayQueueFeeder.PostProcessingAction localPostProcessingAction = PlayQueueFeeder.PostProcessingAction.NONE;
    localPlayQueueFeeder.requestCreateRadioStation(localMixDescriptor, localPostProcessingAction);
  }

  private void requestMoreContentSync(PlayQueueFeeder.PostProcessingAction paramPostProcessingAction)
  {
    if (this.mMixGenerationState == null)
    {
      Log.w("LocalDevicePlayback", "Missing mix state");
      return;
    }
    MixGenerationState localMixGenerationState = this.mMixGenerationState;
    MixGenerationState.State localState = MixGenerationState.State.NOT_STARTED;
    localMixGenerationState.setState(localState);
    int i = Gservices.getInt(getContext().getApplicationContext().getContentResolver(), "music_infinite_mix_recently_played_size", 50);
    List localList = this.mPlayList.getTailTracks(i);
    if (this.LOGV)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("requestMoreContentSync: recentlyPlayed=");
      String str1 = DebugUtils.listToString(localList);
      String str2 = str1;
      Log.d("LocalDevicePlayback", str2);
    }
    PlayQueueFeeder localPlayQueueFeeder = this.mPlayQueueFeeder;
    MixDescriptor localMixDescriptor = this.mMixGenerationState.getMix();
    localPlayQueueFeeder.requestContent(localMixDescriptor, localList, paramPostProcessingAction);
  }

  private void resetCurrentSongMetaDataCursor()
  {
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      if (this.mCurrentSongMetaDataCursor.get() != null)
      {
        ((Cursor)this.mCurrentSongMetaDataCursor.get()).close();
        this.mCurrentSongMetaDataCursor.set(null);
      }
      return;
    }
  }

  private void resumePlaybackAsync(final long paramLong)
  {
    this.mAsyncWakeLock.acquire();
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local35 = new Runnable()
    {
      public void run()
      {
        try
        {
          LocalDevicePlayback localLocalDevicePlayback = LocalDevicePlayback.this;
          long l = paramLong;
          localLocalDevicePlayback.openCurrentAndNext(true, true, true, false, l, null);
          return;
        }
        finally
        {
          LocalDevicePlayback.this.mAsyncWakeLock.release();
        }
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local35);
  }

  private void saveBookmarkIfNeeded()
  {
    try
    {
      if (!isPodcast())
        return;
      long l1 = position();
      long l2 = getBookmark();
      long l3 = duration();
      if ((l1 < l2) && (l1 + 10000L > l2))
        return;
      if ((l1 > l2) && (l1 - 10000L < l2))
        return;
      if ((l1 < 15000L) || (l1 + 10000L > l3))
        l1 = 0L;
      ContentValues localContentValues = new ContentValues();
      Long localLong = Long.valueOf(l1);
      localContentValues.put("bookmark", localLong);
      Uri localUri1 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
      long l4 = ((Cursor)this.mCurrentSongMetaDataCursor.get()).getLong(0);
      Uri localUri2 = ContentUris.withAppendedId(localUri1, l4);
      int i = getContext().getContentResolver().update(localUri2, localContentValues, null, null);
      return;
    }
    catch (SQLiteException localSQLiteException)
    {
    }
  }

  private void saveQueue(boolean paramBoolean)
  {
    if (!this.mQueueIsSaveable)
      return;
    if (this.mMediaList == null)
      return;
    final SongList localSongList = this.mMediaList;
    final long l1;
    final ContentIdentifier.Domain localDomain;
    label42: long l2;
    final long l3;
    String str1;
    final String str2;
    final String str3;
    final int i;
    int j;
    int k;
    final long l4;
    long l5;
    int m;
    MixDescriptor localMixDescriptor;
    Object localObject1;
    boolean bool1;
    String str4;
    String str5;
    int n;
    boolean bool2;
    Object localObject2;
    label193: final long l6;
    if (this.mAudioId == null)
    {
      l1 = 0L;
      if (this.mAudioId != null)
        break label298;
      localDomain = null;
      l2 = this.mListItemId;
      l3 = getAlbumId();
      str1 = getArtistName();
      str2 = getAlbumName();
      str3 = getTrackName();
      i = this.mPlayPos;
      j = this.mRepeatMode;
      k = this.mShuffleMode;
      l4 = this.mLastUserInteractionTime;
      l5 = this.mLastPlayQueueGroupId;
      m = this.mLastPlayQueueGroupPosition;
      if (this.mMixGenerationState == null)
        break label366;
      localMixDescriptor = this.mMixGenerationState.getMix();
      if (!localMixDescriptor.hasRemoteSeedId())
      {
        MixDescriptor.Type localType1 = localMixDescriptor.getType();
        MixDescriptor.Type localType2 = MixDescriptor.Type.LUCKY_RADIO;
        if (localType1 != localType2)
          break label310;
      }
      localObject1 = localMixDescriptor.getRemoteSeedId();
      bool1 = false;
      str4 = localMixDescriptor.getName();
      str5 = localMixDescriptor.getArtLocation();
      n = localMixDescriptor.getType().ordinal();
      bool2 = bool1;
      localObject2 = localObject1;
      if (this.mReloadedQueueSeekPos < 0L)
        break label390;
      l6 = this.mReloadedQueueSeekPos;
    }
    while (true)
    {
      this.mAsyncWakeLock.acquire();
      LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
      LocalDevicePlayback localLocalDevicePlayback = this;
      final boolean bool3 = paramBoolean;
      Runnable local18 = new Runnable()
      {
        public void run()
        {
          try
          {
            SharedPreferences.Editor localEditor1 = LocalDevicePlayback.this.getPreferences().edit();
            if (bool3)
            {
              String str1 = localSongList.freeze();
              SharedPreferences.Editor localEditor2 = localEditor1.putString("medialist", str1);
            }
            int i = i;
            SharedPreferences.Editor localEditor3 = localEditor1.putInt("curpos", i);
            long l1 = l3;
            SharedPreferences.Editor localEditor4 = localEditor1.putLong("curalbumid", l1);
            String str2 = str2;
            SharedPreferences.Editor localEditor5 = localEditor1.putString("curartist", str2);
            String str3 = str3;
            SharedPreferences.Editor localEditor6 = localEditor1.putString("curalbum", str3);
            String str4 = l1;
            SharedPreferences.Editor localEditor7 = localEditor1.putString("curtitle", str4);
            long l2 = localDomain;
            SharedPreferences.Editor localEditor8 = localEditor1.putLong("curAudioId", l2);
            String str5 = "curDomainId";
            if (l6 == null);
            String str9;
            for (Object localObject1 = null; ; localObject1 = str9)
            {
              SharedPreferences.Editor localEditor9 = localEditor1.putString(str5, (String)localObject1);
              long l3 = l4;
              SharedPreferences.Editor localEditor10 = localEditor1.putLong("curListItemId", l3);
              long l4 = this.val$playerPosition;
              SharedPreferences.Editor localEditor11 = localEditor1.putLong("seekpos", l4);
              long l5 = this.val$lastUserInteractionTime;
              SharedPreferences.Editor localEditor12 = localEditor1.putLong("lastUserInteract", l5);
              int j = this.val$repeatMode;
              SharedPreferences.Editor localEditor13 = localEditor1.putInt("repeatMode", j);
              int k = this.val$shuffleMode;
              SharedPreferences.Editor localEditor14 = localEditor1.putInt("shufflemode", k);
              String str6 = this.val$mixSeed;
              SharedPreferences.Editor localEditor15 = localEditor1.putString("infiniteMixSeedId", str6);
              boolean bool1 = this.val$isSeedIdLocal;
              SharedPreferences.Editor localEditor16 = localEditor1.putBoolean("infiniteMixSeedTypeLocal", bool1);
              String str7 = this.val$mixName;
              SharedPreferences.Editor localEditor17 = localEditor1.putString("infiniteMixName", str7);
              String str8 = this.val$mixArtLocation;
              SharedPreferences.Editor localEditor18 = localEditor1.putString("infiniteMixArtLocation", str8);
              int m = this.val$mixType;
              SharedPreferences.Editor localEditor19 = localEditor1.putInt("infiniteMixType", m);
              long l6 = this.val$groupId;
              SharedPreferences.Editor localEditor20 = localEditor1.putLong("playQGroupId", l6);
              int n = this.val$groupPosition;
              SharedPreferences.Editor localEditor21 = localEditor1.putInt("playQGroupPos", n);
              boolean bool2 = localEditor1.commit();
              return;
              str9 = l6.name();
            }
          }
          finally
          {
            LocalDevicePlayback.this.mAsyncWakeLock.release();
          }
        }
      };
      AsyncWorkers.runAsync(localLoggableHandler, local18);
      return;
      l1 = this.mAudioId.getId();
      break;
      label298: localDomain = this.mAudioId.getDomain();
      break label42;
      label310: MixDescriptor.Type localType3 = localMixDescriptor.getType();
      MixDescriptor.Type localType4 = MixDescriptor.Type.RADIO;
      if (localType3 == localType4);
      for (String str6 = Long.toString(localMixDescriptor.getLocalRadioId()); ; str6 = Long.toString(localMixDescriptor.getLocalSeedId()))
      {
        boolean bool4 = true;
        localObject1 = str6;
        bool1 = bool4;
        break;
      }
      label366: localObject2 = "";
      bool2 = false;
      str4 = "";
      str5 = null;
      n = 65535;
      break label193;
      label390: if ((this.mPlayer != null) && (this.mPlayer.isInitialized()))
        l6 = this.mPlayer.position();
      else
        l6 = 0L;
    }
  }

  private void selectDefaultMediaRouteImpl()
  {
    if (this.mMediaRouter == null)
    {
      Log.w("LocalDevicePlayback", "selectDefaultMediaRouteImpl called but the MediaRouter is null");
      return;
    }
    MediaRouter localMediaRouter = this.mMediaRouter;
    MediaRouter.RouteInfo localRouteInfo = this.mMediaRouter.getDefaultRoute();
    localMediaRouter.selectRoute(localRouteInfo);
  }

  private void setMediaList(SongList paramSongList)
  {
    if ((this.mMediaList != null) && (!this.mMediaList.isDefaultDomain()) && (this.mStreamingClient != null))
      this.mStreamingClient.clearPrefetchedCache();
    this.mMediaList = paramSongList;
  }

  private void setMediaRouteImpl(boolean paramBoolean1, String paramString, boolean paramBoolean2, long paramLong)
  {
    MediaRouteSelector localMediaRouteSelector = new MediaRouteSelector.Builder().addControlCategory("android.media.intent.category.LIVE_AUDIO").addControlCategory("android.media.intent.category.REMOTE_PLAYBACK").build();
    if (this.mMediaRouter == null)
    {
      Log.w("LocalDevicePlayback", "setMediaRouteImpl called but the MediaRouter is null");
      return;
    }
    final boolean bool1 = Gservices.getBoolean(getContext().getApplicationContext().getContentResolver(), "music_enable_cast_prequeue", false);
    MediaRouter localMediaRouter = this.mMediaRouter;
    MediaRouter.Callback localCallback = this.mMediaRouterCallback;
    localMediaRouter.addCallback(localMediaRouteSelector, localCallback);
    LocalDevicePlayback localLocalDevicePlayback = this;
    final String str1 = paramString;
    final boolean bool2 = paramBoolean1;
    final boolean bool3 = paramBoolean2;
    final long l = paramLong;
    MediaRouter.Callback local36 = new MediaRouter.Callback()
    {
      public void onRouteAdded(MediaRouter paramAnonymousMediaRouter, MediaRouter.RouteInfo paramAnonymousRouteInfo)
      {
        if (LocalDevicePlayback.this.LOGV)
        {
          String str1 = "onRouteAdded: route=" + paramAnonymousRouteInfo;
          Log.d("LocalDevicePlayback", str1);
        }
        String str2 = paramAnonymousRouteInfo.getId();
        String str3 = str1;
        if (!str2.equals(str3))
          return;
        LocalDevicePlayback.this.mMediaRouter.selectRoute(paramAnonymousRouteInfo);
        if (!bool2)
        {
          RemoteControlClientCompat localRemoteControlClientCompat = LocalDevicePlayback.this.mService.getRemoteControlClient();
          if (localRemoteControlClientCompat != null)
          {
            Object localObject = localRemoteControlClientCompat.getActualRemoteControlClientObject();
            if (localObject != null)
              LocalDevicePlayback.this.mMediaRouter.addRemoteControlClient(localObject);
          }
          if (!bool1)
            break label202;
          LocalDevicePlayback localLocalDevicePlayback1 = LocalDevicePlayback.this;
          boolean bool1 = RemoteAsyncMediaPlayer.isActionEnqueueSupported(paramAnonymousRouteInfo);
          boolean bool2 = LocalDevicePlayback.access$8902(localLocalDevicePlayback1, bool1);
        }
        while (true)
        {
          LocalDevicePlayback.this.mMediaRouter.removeCallback(this);
          if (bool3)
          {
            LocalDevicePlayback localLocalDevicePlayback2 = LocalDevicePlayback.this;
            long l = l;
            localLocalDevicePlayback2.resumePlaybackAsync(l);
          }
          if (!LocalDevicePlayback.this.LOGV)
            return;
          Log.d("LocalDevicePlayback", "onRouteAdded: selected route");
          return;
          label202: boolean bool3 = LocalDevicePlayback.access$8902(LocalDevicePlayback.this, false);
        }
      }
    };
    this.mMediaRouter.addCallback(localMediaRouteSelector, local36, 4);
    Iterator localIterator = this.mMediaRouter.getRoutes().iterator();
    label343: label349: 
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      MediaRouter.RouteInfo localRouteInfo = (MediaRouter.RouteInfo)localIterator.next();
      if (this.LOGV)
      {
        String str2 = "existing route= " + localRouteInfo;
        Log.d("LocalDevicePlayback", str2);
      }
      if (localRouteInfo.getId().equals(paramString))
      {
        this.mMediaRouter.removeCallback(local36);
        this.mMediaRouter.selectRoute(localRouteInfo);
        if (paramBoolean2)
          resumePlaybackAsync(paramLong);
        boolean bool4;
        if (!paramBoolean1)
        {
          RemoteControlClientCompat localRemoteControlClientCompat = this.mService.getRemoteControlClient();
          if (localRemoteControlClientCompat != null)
          {
            Object localObject = localRemoteControlClientCompat.getActualRemoteControlClientObject();
            if (localObject != null)
              this.mMediaRouter.addRemoteControlClient(localObject);
          }
          if (!bool1)
            break label343;
          bool4 = RemoteAsyncMediaPlayer.isActionEnqueueSupported(localRouteInfo);
        }
        for (this.mPrequeueItems = bool4; ; this.mPrequeueItems = false)
        {
          if (!this.LOGV)
            break label349;
          String str3 = "Selected route from existing routes: route=" + localRouteInfo;
          Log.d("LocalDevicePlayback", str3);
          break;
        }
      }
    }
  }

  private void setMediaRouteVolumeImpl(String paramString, double paramDouble)
  {
    if (this.LOGV)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramString;
      Double localDouble = Double.valueOf(paramDouble);
      arrayOfObject[1] = localDouble;
      String str = String.format("setMediaRouteVolumeImpl: routedId=%s volume=%s ", arrayOfObject);
      Log.d("LocalDevicePlayback", str);
    }
    if (this.mMediaRouter == null)
      return;
    MediaRouter.RouteInfo localRouteInfo = this.mMediaRouter.getSelectedRoute();
    if (localRouteInfo.getPlaybackType() != 1)
      return;
    if (!localRouteInfo.getId().equals(paramString))
      return;
    int i = (int)(localRouteInfo.getVolumeMax() * paramDouble);
    localRouteInfo.requestSetVolume(i);
  }

  private void setNextTrack()
  {
    if (isPlayingLocally())
    {
      if (isGaplessEnabled());
    }
    else if (!this.mPrequeueItems)
      return;
    final boolean bool = Gservices.getBoolean(getContext().getApplicationContext().getContentResolver(), "music_gapless_enabled_pinned", true);
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local26 = new Runnable()
    {
      public void run()
      {
        LocalDevicePlayback localLocalDevicePlayback1 = LocalDevicePlayback.this;
        LocalDevicePlayback localLocalDevicePlayback2 = LocalDevicePlayback.this;
        int i = LocalDevicePlayback.this.mPlayPos;
        int j = localLocalDevicePlayback2.getNextPlayPosition(false, false, i, false);
        Pair localPair1 = localLocalDevicePlayback1.findPlayableSong(j, false, false);
        if (localPair1 == null)
        {
          LocalDevicePlayback.this.invalidateNextPlayer();
          return;
        }
        int k = ((Integer)localPair1.first).intValue();
        MusicFile localMusicFile = (MusicFile)localPair1.second;
        if (k < 0)
          return;
        Pair localPair2 = LocalDevicePlayback.this.mPlayList.getAudioIdAndListItemId(k);
        if (localPair2 == null)
          return;
        final ContentIdentifier localContentIdentifier1 = (ContentIdentifier)localPair2.first;
        LocalDevicePlayback.this.invalidateNextPlayer();
        final AsyncMediaPlayer localAsyncMediaPlayer1 = LocalDevicePlayback.this.mPlayer;
        Store localStore = Store.getInstance(LocalDevicePlayback.this.getContext());
        if (LocalDevicePlayback.this.isPlayingLocally())
        {
          long[] arrayOfLong = null;
          long l1 = localContentIdentifier1.getId();
          arrayOfLong[0] = l1;
          if ((localStore.requiresDownloadManager(arrayOfLong) != null) && (!bool))
            return;
        }
        String str1 = "LocalDevicePlayback";
        String str2 = "setNextTrack: id=%s, playPos=%s, track=%s";
        Object[] arrayOfObject = new Object[3];
        arrayOfObject[0] = localContentIdentifier1;
        Integer localInteger = Integer.valueOf(k);
        arrayOfObject[1] = localInteger;
        int m = 2;
        if (localMusicFile != null);
        for (String str3 = localMusicFile.getTitle(); ; str3 = "external")
        {
          arrayOfObject[m] = str3;
          String str4 = String.format(str2, arrayOfObject);
          Log.i(str1, str4);
          ContentIdentifier localContentIdentifier2 = LocalDevicePlayback.access$8002(LocalDevicePlayback.this, localContentIdentifier1);
          int n = LocalDevicePlayback.access$2702(LocalDevicePlayback.this, k);
          LocalDevicePlayback localLocalDevicePlayback3 = LocalDevicePlayback.this;
          AsyncMediaPlayer localAsyncMediaPlayer2 = LocalDevicePlayback.this.createPlayer();
          AsyncMediaPlayer localAsyncMediaPlayer3 = LocalDevicePlayback.access$302(localLocalDevicePlayback3, localAsyncMediaPlayer2);
          AsyncMediaPlayer localAsyncMediaPlayer4 = LocalDevicePlayback.this.mNextPlayer;
          int i1 = LocalDevicePlayback.this.mPlayer.getAudioSessionId();
          localAsyncMediaPlayer4.setAudioSessionId(i1);
          AsyncMediaPlayer localAsyncMediaPlayer5 = LocalDevicePlayback.this.mNextPlayer;
          long l2 = LocalDevicePlayback.this.duration(localContentIdentifier1);
          AsyncMediaPlayer.AsyncCommandCallback local1 = new AsyncMediaPlayer.AsyncCommandCallback()
          {
            public void onFailure(boolean paramAnonymous2Boolean)
            {
              StringBuilder localStringBuilder = new StringBuilder().append("Failed to open MusicId (");
              ContentIdentifier localContentIdentifier = localContentIdentifier1;
              String str = localContentIdentifier + ") for playback";
              Log.e("LocalDevicePlayback", str);
              int i = LocalDevicePlayback.access$6508(LocalDevicePlayback.this);
              if ((paramAnonymous2Boolean) && (LocalDevicePlayback.this.mOpenFailedCounter < 10))
              {
                LocalDevicePlayback.this.tryNext();
                return;
              }
              LocalDevicePlayback.this.onPlaybackFailure();
            }

            public void onSuccess()
            {
              AsyncMediaPlayer localAsyncMediaPlayer1 = localAsyncMediaPlayer1;
              AsyncMediaPlayer localAsyncMediaPlayer2 = LocalDevicePlayback.this.mPlayer;
              if (localAsyncMediaPlayer1 != localAsyncMediaPlayer2)
                return;
              int i = LocalDevicePlayback.access$6502(LocalDevicePlayback.this, 0);
              try
              {
                AsyncMediaPlayer localAsyncMediaPlayer3 = LocalDevicePlayback.this.mPlayer;
                AsyncMediaPlayer localAsyncMediaPlayer4 = LocalDevicePlayback.this.mNextPlayer;
                localAsyncMediaPlayer3.setNextPlayer(localAsyncMediaPlayer4);
                return;
              }
              catch (Exception localException)
              {
                Object[] arrayOfObject = new Object[1];
                ContentIdentifier localContentIdentifier = localContentIdentifier1;
                arrayOfObject[0] = localContentIdentifier;
                String str = String.format("failed to set next: nextAudioId=%s", arrayOfObject);
                Log.e("LocalDevicePlayback", str, localException);
              }
            }
          };
          boolean bool = false;
          localAsyncMediaPlayer5.setDataSource(localContentIdentifier1, 0L, l2, false, bool, localMusicFile, local1);
          return;
        }
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local26);
  }

  private void setPreviewValues(String paramString, int paramInt, long paramLong)
  {
    PreviewPlaybackInfo localPreviewPlaybackInfo = new PreviewPlaybackInfo(paramString, paramInt, paramLong);
    this.mPreviewPlaybackInfo = localPreviewPlaybackInfo;
  }

  private boolean shouldPlayInRandomOrder()
  {
    int i = 1;
    if ((this.mShuffleMode != i) && (!isUsingPlayQueue()));
    while (true)
    {
      return i;
      int j = 0;
    }
  }

  private void stop(boolean paramBoolean)
  {
    StringBuilder localStringBuilder = new StringBuilder().append("stop: willPlay=").append(paramBoolean).append(", currentPos=");
    int i = this.mPlayPos;
    String str = i;
    Log.i("LocalDevicePlayback", str);
    if ((!paramBoolean) || ((paramBoolean) && (isPlayingLocally())))
      this.mPlayer.stop();
    resetCurrentSongMetaDataCursor();
    if (paramBoolean)
      return;
    if (!this.mIsSupposedToBePlaying)
      return;
    this.mIsSupposedToBePlaying = false;
    onPlayStateChanged();
  }

  private ContentIdentifier[] stripNullContentIdentifiers(ContentIdentifier[] paramArrayOfContentIdentifier)
  {
    int i = 0;
    int j = 0;
    while (true)
    {
      int k = paramArrayOfContentIdentifier.length;
      if (j >= k)
        break;
      if (paramArrayOfContentIdentifier[j] != null)
        i += 1;
      j += 1;
    }
    int m = paramArrayOfContentIdentifier.length;
    if (i != m);
    while (true)
    {
      return paramArrayOfContentIdentifier;
      if (i <= 0)
      {
        paramArrayOfContentIdentifier = null;
      }
      else
      {
        int n = 0;
        ContentIdentifier[] arrayOfContentIdentifier = new ContentIdentifier[i];
        j = 0;
        while (true)
        {
          int i1 = paramArrayOfContentIdentifier.length;
          if (j >= i1)
            break;
          if (paramArrayOfContentIdentifier[j] != null)
          {
            int i2 = n + 1;
            ContentIdentifier localContentIdentifier = paramArrayOfContentIdentifier[j];
            arrayOfContentIdentifier[n] = localContentIdentifier;
            int i3 = i2;
          }
          int i4 = j + 1;
        }
        paramArrayOfContentIdentifier = arrayOfContentIdentifier;
      }
    }
  }

  private void tryCreatingStreamingSchedulingClient()
  {
    synchronized (this.mStreamingClientLock)
    {
      if (this.LOGV)
      {
        if (this.mDownloadQueueManager == null)
          Log.d("LocalDevicePlayback", "Download queue manager is null");
        if (this.mCacheManager == null)
          Log.d("LocalDevicePlayback", "Cache manager is null");
      }
      if ((this.mStreamingClient == null) && (this.mDownloadQueueManager != null) && (this.mCacheManager != null))
      {
        Context localContext = getContext();
        IDownloadQueueManager localIDownloadQueueManager = this.mDownloadQueueManager;
        ICacheManager localICacheManager = this.mCacheManager;
        StreamingClient localStreamingClient = new StreamingClient(localContext, localIDownloadQueueManager, localICacheManager);
        this.mStreamingClient = localStreamingClient;
        Log.i("LocalDevicePlayback", "Streaming client created.");
      }
      return;
    }
  }

  private void tryNext()
  {
    if (this.LOGV)
      Log.d("LocalDevicePlayback", "tryNext");
    boolean bool = this.mMediaplayerHandler.sendEmptyMessageDelayed(7, 1000L);
  }

  private void updateMediaRouteVolumeImpl(String paramString, double paramDouble)
  {
    if (this.LOGV)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramString;
      Double localDouble = Double.valueOf(paramDouble);
      arrayOfObject[1] = localDouble;
      String str = String.format("updateMediaRouteVolumeImpl: routedId=%s deltaRatio=%s ", arrayOfObject);
      Log.d("LocalDevicePlayback", str);
    }
    if (this.mMediaRouter == null)
      return;
    MediaRouter.RouteInfo localRouteInfo = this.mMediaRouter.getSelectedRoute();
    if (localRouteInfo.getPlaybackType() != 1)
      return;
    if (!localRouteInfo.getId().equals(paramString))
      return;
    int i = (int)(localRouteInfo.getVolumeMax() * paramDouble);
    localRouteInfo.requestUpdateVolume(i);
  }

  public boolean addDownloadProgressListener(ContentIdentifier paramContentIdentifier, IDownloadProgressListener paramIDownloadProgressListener)
  {
    if (this.LOGV)
    {
      String str = "addDownloadProgressListener: " + paramContentIdentifier;
      Log.d("LocalDevicePlayback", str);
    }
    if (paramIDownloadProgressListener != null);
    for (boolean bool = this.mProgressListeners.register(paramIDownloadProgressListener); ; bool = false)
      return bool;
  }

  public void cancelMix()
  {
    disableInfiniteMixMode();
    this.mPlayQueueFeeder.cancelMix();
  }

  public void clearQueue()
  {
    disableInfiniteMixMode();
    SongList localSongList = acquireAsyncLockAndClearMediaList();
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local22 = new Runnable()
    {
      public void run()
      {
        try
        {
          LocalDevicePlayback.this.stopSync();
          Store.getInstance(LocalDevicePlayback.this.getContext()).clearPlayQueue();
          return;
        }
        finally
        {
          LocalDevicePlayback.this.mAsyncWakeLock.release();
        }
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local22);
  }

  public void disableGroupPlay()
  {
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local34 = new Runnable()
    {
      public void run()
      {
        if (LocalDevicePlayback.this.mLastPlayQueueGroupId == 0L)
          return;
        long l = LocalDevicePlayback.access$2902(LocalDevicePlayback.this, 0L);
        int i = LocalDevicePlayback.access$3002(LocalDevicePlayback.this, -1);
        LocalDevicePlayback.this.saveQueue(false);
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local34);
  }

  public void dump(PrintWriter paramPrintWriter)
  {
    StringBuilder localStringBuilder1 = new StringBuilder().append("IsPlayingLocally: ");
    boolean bool1 = isPlayingLocally();
    String str1 = bool1;
    paramPrintWriter.println(str1);
    StringBuilder localStringBuilder2 = new StringBuilder().append("AsyncPlayer IsPlaying: ");
    if (this.mPlayer != null);
    for (String str2 = String.valueOf(this.mPlayer.isPlaying()); ; str2 = "no async player")
    {
      String str3 = str2;
      paramPrintWriter.println(str3);
      StringBuilder localStringBuilder3 = new StringBuilder().append("GaplessEnabled: ");
      boolean bool2 = isGaplessEnabled();
      String str4 = bool2;
      paramPrintWriter.println(str4);
      return;
    }
  }

  public long duration()
  {
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      Cursor localCursor = (Cursor)this.mCurrentSongMetaDataCursor.get();
      if (localCursor == null)
      {
        l = 65535L;
        return l;
      }
      long l = doGetDuration(localCursor);
    }
  }

  public String getAlbumArtUrl(long paramLong)
  {
    return null;
  }

  public long getAlbumId()
  {
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      Cursor localCursor = (Cursor)this.mCurrentSongMetaDataCursor.get();
      if (localCursor == null)
      {
        l = 65535L;
        return l;
      }
      int i = localCursor.getColumnIndexOrThrow("album_id");
      long l = localCursor.getLong(i);
    }
  }

  public String getAlbumName()
  {
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      Cursor localCursor = (Cursor)this.mCurrentSongMetaDataCursor.get();
      if (localCursor == null)
      {
        str = null;
        return str;
      }
      int i = localCursor.getColumnIndexOrThrow("album");
      String str = localCursor.getString(i);
    }
  }

  public long getArtistId()
  {
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      Cursor localCursor = (Cursor)this.mCurrentSongMetaDataCursor.get();
      if (localCursor == null)
      {
        l = 65535L;
        return l;
      }
      int i = localCursor.getColumnIndexOrThrow("AlbumArtistId");
      long l = localCursor.getLong(i);
    }
  }

  public String getArtistName()
  {
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      Cursor localCursor = (Cursor)this.mCurrentSongMetaDataCursor.get();
      if (localCursor == null)
      {
        str = null;
        return str;
      }
      int i = localCursor.getColumnIndexOrThrow("artist");
      String str = localCursor.getString(i);
    }
  }

  public ContentIdentifier getAudioId()
  {
    if ((this.mPlayPos >= 0) && (this.mPlayList.isValid()));
    for (ContentIdentifier localContentIdentifier = this.mAudioId; ; localContentIdentifier = null)
      return localContentIdentifier;
  }

  public int getAudioSessionId()
  {
    try
    {
      if (this.mPlayer != null)
      {
        i = this.mPlayer.getAudioSessionId();
        return i;
      }
      int i = -1;
    }
    finally
    {
    }
  }

  public long getBookmark()
  {
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      Cursor localCursor = (Cursor)this.mCurrentSongMetaDataCursor.get();
      if (localCursor == null)
      {
        l = 0L;
        return l;
      }
      long l = localCursor.getLong(9);
    }
  }

  public TrackInfo getCurrentTrackInfo()
  {
    ContentIdentifier localContentIdentifier = getAudioId();
    TrackInfo localTrackInfo;
    if (localContentIdentifier == null)
      localTrackInfo = null;
    while (true)
    {
      return localTrackInfo;
      Cursor localCursor1;
      synchronized (this.mCurrentSongMetaDataCursor)
      {
        localCursor1 = (Cursor)this.mCurrentSongMetaDataCursor.get();
        if (localCursor1 == null)
          localTrackInfo = null;
      }
      int i = localCursor1.getColumnIndexOrThrow("title");
      String str1 = localCursor1.getString(i);
      int j = localCursor1.getColumnIndexOrThrow("artist");
      String str2 = localCursor1.getString(j);
      int k = localCursor1.getColumnIndexOrThrow("album");
      String str3 = localCursor1.getString(k);
      int m = localCursor1.getColumnIndexOrThrow("album_id");
      long l1 = localCursor1.getLong(m);
      LocalDevicePlayback localLocalDevicePlayback1 = this;
      Cursor localCursor2 = localCursor1;
      long l2 = localLocalDevicePlayback1.doGetDuration(localCursor2);
      boolean bool = true;
      String str4 = null;
      if ((getMediaList() instanceof ExternalSongList))
      {
        bool = false;
        ExternalSongList localExternalSongList = (ExternalSongList)getMediaList();
        Context localContext = getContext();
        str4 = localExternalSongList.getAlbumArtUrl(localContext);
      }
      int n = localCursor1.getColumnIndexOrThrow("Rating");
      int i1 = localCursor1.getInt(n);
      int i2 = localCursor1.getColumnIndexOrThrow("SourceId");
      String str5 = localCursor1.getString(i2);
      LocalDevicePlayback localLocalDevicePlayback2 = this;
      String str6 = str5;
      int i3 = localLocalDevicePlayback2.getPreviewPlayTypeForUrl(str6);
      localTrackInfo = new TrackInfo(localContentIdentifier, str1, str2, str3, l1, false, l2, str4, bool, i1, i3);
    }
  }

  public int getErrorType()
  {
    if (this.mPlayer == null);
    for (int i = 1; ; i = this.mPlayer.getErrorType())
      return i;
  }

  public long getLastUserInteractionTime()
  {
    return this.mLastUserInteractionTime;
  }

  public SongList getMediaList()
  {
    return this.mMediaList;
  }

  public MixGenerationState getMixState()
  {
    return this.mMixGenerationState;
  }

  public PlaybackState getPlaybackState()
  {
    try
    {
      int i = getQueuePosition();
      int j = getQueueSize();
      boolean bool1 = isPlaying();
      boolean bool2 = isCurrentSongLoaded();
      SongList localSongList = getMediaList();
      long l = position();
      int k = getShuffleMode();
      int m = getRepeatMode();
      boolean bool3 = isPreparing();
      boolean bool4 = isStreaming();
      boolean bool5 = isInErrorState();
      int n = getErrorType();
      boolean bool6 = isStreamingFullyBuffered();
      boolean bool7 = isInFatalErrorState();
      boolean bool8 = hasValidPlaylist();
      boolean bool9 = playlistLoading();
      boolean bool10 = isPlayingLocally();
      boolean bool11 = isInfiniteMixMode();
      PlaybackState localPlaybackState = new PlaybackState(i, j, bool1, bool2, localSongList, l, k, m, bool3, bool4, bool5, n, bool6, bool7, bool8, bool9, bool10, bool11);
      return localPlaybackState;
    }
    catch (Exception localException)
    {
      Log.e("LocalDevicePlayback", "Failed to create PlaybackState: ", localException);
      throw new RuntimeException(localException);
    }
  }

  public int getPreviewPlayType()
  {
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      Cursor localCursor = (Cursor)this.mCurrentSongMetaDataCursor.get();
      if (localCursor == null)
      {
        i = -1;
        return i;
      }
      int j = localCursor.getColumnIndexOrThrow("SourceId");
      String str = localCursor.getString(j);
      int i = getPreviewPlayTypeForUrl(str);
    }
  }

  public int getQueuePosition()
  {
    return this.mPlayPos;
  }

  public int getQueueSize()
  {
    if (this.mPlayList == null);
    for (int i = 0; ; i = this.mPlayList.length())
      return i;
  }

  public int getRating()
  {
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      Cursor localCursor = (Cursor)this.mCurrentSongMetaDataCursor.get();
      if (localCursor == null)
      {
        i = 0;
        return i;
      }
      int j = localCursor.getColumnIndexOrThrow("Rating");
      int i = localCursor.getInt(j);
    }
  }

  public int getRepeatMode()
  {
    return this.mRepeatMode;
  }

  public String getSelectedMediaRouteId()
  {
    return this.mSelectedRouteId;
  }

  public int getShuffleMode()
  {
    return this.mShuffleMode;
  }

  public String getSongStoreId()
  {
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      Cursor localCursor = (Cursor)this.mCurrentSongMetaDataCursor.get();
      if (localCursor == null)
      {
        str = null;
        return str;
      }
      int i = localCursor.getColumnIndexOrThrow("StoreId");
      String str = localCursor.getString(i);
    }
  }

  public String getSortableAlbumArtistName()
  {
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      Cursor localCursor = (Cursor)this.mCurrentSongMetaDataCursor.get();
      if (localCursor == null)
      {
        str = null;
        return str;
      }
      int i = localCursor.getColumnIndexOrThrow("artistSort");
      String str = localCursor.getString(i);
    }
  }

  public DevicePlayback.State getState()
  {
    DevicePlayback.State localState;
    if (this.mIsSupposedToBePlaying)
      localState = DevicePlayback.State.PLAYING;
    while (true)
    {
      return localState;
      if (this.mPausedByTransientLossOfFocus)
        localState = DevicePlayback.State.TRANSIENT_PAUSE;
      else if (this.mMediaplayerHandler.hasMessages(1))
        localState = DevicePlayback.State.SWITCHING_TRACKS;
      else if ((this.mPlayList != null) && (this.mPlayList.isValid()) && (this.mPlayList.length() > 0))
        localState = DevicePlayback.State.PAUSED;
      else
        localState = DevicePlayback.State.NO_PLAYLIST;
    }
  }

  public String getTrackName()
  {
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      Cursor localCursor = (Cursor)this.mCurrentSongMetaDataCursor.get();
      if (localCursor == null)
      {
        str = null;
        return str;
      }
      int i = localCursor.getColumnIndexOrThrow("title");
      String str = localCursor.getString(i);
    }
  }

  public boolean hasLocal()
  {
    boolean bool = false;
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      Cursor localCursor = (Cursor)this.mCurrentSongMetaDataCursor.get();
      if (localCursor == null)
        return bool;
      if (localCursor.getInt(12) > 0)
        bool = true;
    }
  }

  public boolean hasRemote()
  {
    boolean bool = false;
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      Cursor localCursor = (Cursor)this.mCurrentSongMetaDataCursor.get();
      if (localCursor == null)
        return bool;
      if (localCursor.getInt(11) > 0)
        bool = true;
    }
  }

  public boolean hasValidPlaylist()
  {
    if ((this.mPlayList != null) && (this.mPlayList.isValid()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isCurrentSongLoaded()
  {
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      if (this.mCurrentSongMetaDataCursor.get() != null)
      {
        bool = true;
        return bool;
      }
      boolean bool = false;
    }
  }

  public boolean isInErrorState()
  {
    if ((this.mPlayer != null) && (this.mPlayer.isInErrorState()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isInFatalErrorState()
  {
    return false;
  }

  public boolean isInfiniteMixMode()
  {
    if (this.mMixGenerationState != null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isPlaying()
  {
    return this.mIsSupposedToBePlaying;
  }

  public boolean isPlayingLocally()
  {
    return this.mUseLocalPlayer;
  }

  public boolean isPodcast()
  {
    boolean bool = false;
    synchronized (this.mCurrentSongMetaDataCursor)
    {
      Cursor localCursor = (Cursor)this.mCurrentSongMetaDataCursor.get();
      if (localCursor == null)
        return bool;
      if (localCursor.getInt(8) > 0)
        bool = true;
    }
  }

  public boolean isPreparing()
  {
    if ((this.mPlayer != null) && (this.mPlayer.isPreparing()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isStreaming()
  {
    if ((this.mPlayer != null) && (this.mPlayer.isStreaming()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isStreamingFullyBuffered()
  {
    if (this.mStreamingClient != null);
    for (boolean bool = this.mStreamingClient.isCurrentStreamingFullyBuffered(); ; bool = false)
      return bool;
  }

  public void next()
  {
    long l = System.currentTimeMillis();
    this.mLastUserInteractionTime = l;
    next(true);
  }

  public void onCreate()
  {
    super.onCreate();
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(getContext(), this);
    try
    {
      if (localMusicPreferences.isMediaRouteSupportEnabled())
      {
        MediaRouter localMediaRouter = MediaRouter.getInstance(this.mService);
        this.mMediaRouter = localMediaRouter;
      }
      MusicPreferences.releaseMusicPreferences(this);
      MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(getContext());
      this.mEventLogger = localMusicEventLogger;
      AudioManager localAudioManager = (AudioManager)getContext().getSystemService("audio");
      this.mAudioManager = localAudioManager;
      SafeServiceConnection localSafeServiceConnection1 = this.mCacheServiceConnection;
      Context localContext1 = getContext();
      Context localContext2 = getContext();
      Intent localIntent1 = new Intent(localContext2, CacheService.class);
      boolean bool1 = localSafeServiceConnection1.bindService(localContext1, localIntent1, 1);
      SafeServiceConnection localSafeServiceConnection2 = this.mDownloadManagerSafeConnection;
      Context localContext3 = getContext();
      Context localContext4 = getContext();
      Intent localIntent2 = new Intent(localContext4, DownloadQueueService.class);
      boolean bool2 = localSafeServiceConnection2.bindService(localContext3, localIntent2, 1);
      NetworkMonitorServiceConnection localNetworkMonitorServiceConnection = this.mNetworkMonitorServiceConnection;
      Context localContext5 = getContext();
      localNetworkMonitorServiceConnection.bindToService(localContext5);
      if ((mDisableGaplessOverride == null) && (isGaplessEnabled()) && (Gservices.getBoolean(getContext().getApplicationContext().getContentResolver(), "music_disable_gapless_lpaplayer", true)))
      {
        LoggableHandler localLoggableHandler1 = AsyncWorkers.sBackendServiceWorker;
        Runnable local11 = new Runnable()
        {
          public void run()
          {
            try
            {
              Boolean localBoolean = LocalDevicePlayback.access$4702(Boolean.valueOf(Boolean.parseBoolean(SystemUtils.getSystemProperty("lpa.decode"))));
              if (!LocalDevicePlayback.mDisableGaplessOverride.booleanValue())
                return;
              Log.d("LocalDevicePlayback", "LPAPlayer detected. Disabling gapless.");
              return;
            }
            catch (Exception localException)
            {
              String str = localException.getMessage();
              Log.w("LocalDevicePlayback", str, localException);
            }
          }
        };
        AsyncWorkers.runAsync(localLoggableHandler1, local11);
      }
      LoggableHandler localLoggableHandler2 = AsyncWorkers.sBackendServiceWorker;
      Runnable local12 = new Runnable()
      {
        public void run()
        {
          LocalDevicePlayback.this.tryCreatingStreamingSchedulingClient();
        }
      };
      AsyncWorkers.runAsync(localLoggableHandler2, local12);
      if (!PostFroyoUtils.EnvironmentCompat.isExternalStorageEmulated())
        registerExternalStorageListener();
      MusicPlaybackService localMusicPlaybackService = this.mService;
      Intent localIntent3 = new Intent(localMusicPlaybackService, CacheService.class);
      Intent localIntent4 = localIntent3.setAction("com.google.android.music.download.cache.CacheService.CLEAR_ORPHANED");
      ComponentName localComponentName = this.mService.startService(localIntent3);
      AsyncMediaPlayer localAsyncMediaPlayer = createPlayer();
      this.mPlayer = localAsyncMediaPlayer;
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("com.android.music.sharedpreviewmetadataupdate");
      Context localContext6 = getContext();
      BroadcastReceiver localBroadcastReceiver = this.mSharedPreviewPlayListener;
      Intent localIntent5 = localContext6.registerReceiver(localBroadcastReceiver, localIntentFilter);
      PowerManager localPowerManager = (PowerManager)this.mService.getSystemService("power");
      StringBuilder localStringBuilder1 = new StringBuilder();
      String str1 = getClass().getName();
      String str2 = str1 + ".mWakeLock";
      PowerManager.WakeLock localWakeLock1 = localPowerManager.newWakeLock(1, str2);
      this.mWakeLock = localWakeLock1;
      this.mWakeLock.setReferenceCounted(false);
      StringBuilder localStringBuilder2 = new StringBuilder();
      String str3 = getClass().getName();
      String str4 = str3 + ".mAsyncWakeLock";
      PowerManager.WakeLock localWakeLock2 = localPowerManager.newWakeLock(1, str4);
      this.mAsyncWakeLock = localWakeLock2;
      this.mAsyncWakeLock.acquire();
      this.mPlayList.setPlaylistLoading(true);
      LoggableHandler localLoggableHandler3 = AsyncWorkers.sBackendServiceWorker;
      Runnable local13 = new Runnable()
      {
        public void run()
        {
          try
          {
            LocalDevicePlayback.this.reloadQueue();
            return;
          }
          finally
          {
            LocalDevicePlayback.this.mPlayList.setPlaylistLoading(false);
            LocalDevicePlayback.this.mAsyncWakeLock.release();
          }
        }
      };
      AsyncWorkers.runAsync(localLoggableHandler3, local13);
      Context localContext7 = getContext();
      PlayQueueFeederListener localPlayQueueFeederListener = this.mPlayQueueFeederListener;
      PlayQueueFeeder localPlayQueueFeeder = new PlayQueueFeeder(localContext7, localPlayQueueFeederListener);
      this.mPlayQueueFeeder = localPlayQueueFeeder;
      return;
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(this);
    }
  }

  public void onDestroy()
  {
    this.mPlayQueueFeeder.onDestroy();
    AudioManager localAudioManager = this.mAudioManager;
    AudioManager.OnAudioFocusChangeListener localOnAudioFocusChangeListener = this.mAudioFocusListener;
    int i = localAudioManager.abandonAudioFocus(localOnAudioFocusChangeListener);
    this.mCastTokenClient.release();
    Intent localIntent1 = new Intent("android.media.action.CLOSE_AUDIO_EFFECT_CONTROL_SESSION");
    int j = getAudioSessionId();
    Intent localIntent2 = localIntent1.putExtra("android.media.extra.AUDIO_SESSION", j);
    String str = getContext().getPackageName();
    Intent localIntent3 = localIntent1.putExtra("android.media.extra.PACKAGE_NAME", str);
    getContext().sendBroadcast(localIntent1);
    releasePlayers();
    this.mMediaplayerHandler.removeCallbacksAndMessages(null);
    clearCursor();
    this.mAsyncWakeLock.acquire();
    LoggableHandler localLoggableHandler1 = AsyncWorkers.sBackendServiceWorker;
    Runnable local14 = new Runnable()
    {
      public void run()
      {
        try
        {
          LocalDevicePlayback.this.mPlayList.setMediaList(null);
          return;
        }
        finally
        {
          LocalDevicePlayback.this.mAsyncWakeLock.release();
        }
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler1, local14);
    Context localContext1 = getContext();
    BroadcastReceiver localBroadcastReceiver1 = this.mSharedPreviewPlayListener;
    localContext1.unregisterReceiver(localBroadcastReceiver1);
    if (this.mUnmountReceiver != null)
    {
      Context localContext2 = getContext();
      BroadcastReceiver localBroadcastReceiver2 = this.mUnmountReceiver;
      localContext2.unregisterReceiver(localBroadcastReceiver2);
      this.mUnmountReceiver = null;
    }
    this.mWakeLock.release();
    SafeServiceConnection localSafeServiceConnection1 = this.mDownloadManagerSafeConnection;
    Context localContext3 = getContext();
    localSafeServiceConnection1.unbindService(localContext3);
    SafeServiceConnection localSafeServiceConnection2 = this.mCacheServiceConnection;
    Context localContext4 = getContext();
    localSafeServiceConnection2.unbindService(localContext4);
    NetworkMonitorServiceConnection localNetworkMonitorServiceConnection = this.mNetworkMonitorServiceConnection;
    Context localContext5 = getContext();
    localNetworkMonitorServiceConnection.unbindFromService(localContext5);
    final StreamingClient localStreamingClient = this.mStreamingClient;
    if (localStreamingClient != null)
    {
      this.mAsyncWakeLock.acquire();
      LoggableHandler localLoggableHandler2 = AsyncWorkers.sBackendServiceWorker;
      Runnable local15 = new Runnable()
      {
        public void run()
        {
          try
          {
            localStreamingClient.destroy();
            return;
          }
          finally
          {
            LocalDevicePlayback.this.mAsyncWakeLock.release();
          }
        }
      };
      AsyncWorkers.runAsync(localLoggableHandler2, local15);
    }
    super.onDestroy();
  }

  protected void onMediaRouteInvalidated()
  {
    notifyChange("com.android.music.mediarouteinvalidated");
    saveQueue(false);
    selectDefaultMediaRoute();
  }

  protected void onOpenComplete()
  {
    notifyChange("com.android.music.asyncopencomplete");
  }

  protected void onPlayStateChanged()
  {
    notifyChange("com.android.music.playstatechanged");
    saveQueue(false);
  }

  protected void onPlaybackComplete()
  {
    notifyChange("com.android.music.playbackcomplete");
    saveQueue(false);
  }

  public void open(SongList paramSongList, int paramInt, boolean paramBoolean)
  {
    open(paramSongList, false, paramInt, paramBoolean);
  }

  public void openAndQueue(final SongList paramSongList, int paramInt)
  {
    if (this.LOGV)
      Log.d("LocalDevicePlayback", "openAndQueue");
    this.mAsyncWakeLock.acquire();
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local21 = new Runnable()
    {
      public void run()
      {
        while (true)
        {
          int i;
          try
          {
            long l1 = LocalDevicePlayback.access$2902(LocalDevicePlayback.this, 0L);
            i = LocalDevicePlayback.access$3002(LocalDevicePlayback.this, -1);
            i = 0;
            long l2 = 0L;
            PlayQueueAddResult localPlayQueueAddResult;
            if (LocalDevicePlayback.this.isUsingPlayQueue())
            {
              l2 = LocalDevicePlayback.this.mListItemId;
              if ((LocalDevicePlayback.this.mPlayList != null) && (LocalDevicePlayback.this.mPlayList.length() == 0))
                i = 1;
            }
            else
            {
              LocalDevicePlayback localLocalDevicePlayback1 = LocalDevicePlayback.this;
              SongList localSongList1 = paramSongList;
              if (localLocalDevicePlayback1.isSuggestedMix(localSongList1))
              {
                LocalDevicePlayback localLocalDevicePlayback2 = LocalDevicePlayback.this;
                SongList localSongList2 = paramSongList;
                localLocalDevicePlayback2.enableInfiniteMixModeForSuggestedMix(localSongList2);
                LocalDevicePlayback.this.requestCreateRadioStationSync();
              }
              LocalDevicePlayback localLocalDevicePlayback3 = LocalDevicePlayback.this;
              SongList localSongList3 = paramSongList;
              localPlayQueueAddResult = localLocalDevicePlayback3.queue(localSongList3, l2);
              if (localPlayQueueAddResult != null)
                continue;
              Log.e("LocalDevicePlayback", "Cannot add non-default domain to the queue");
              return;
            }
            i = 0;
            continue;
            if ((LocalDevicePlayback.this.mMediaList == null) && (localPlayQueueAddResult.getAddedSize() > 0))
            {
              LocalDevicePlayback localLocalDevicePlayback4 = LocalDevicePlayback.this;
              SongList localSongList4 = LocalDevicePlayback.this.createPlayQueueSongList();
              localLocalDevicePlayback4.setMediaList(localSongList4);
              LocalDevicePlayback.MediaListWrapper localMediaListWrapper = LocalDevicePlayback.this.mPlayList;
              SongList localSongList5 = LocalDevicePlayback.this.mMediaList;
              localMediaListWrapper.setMediaList(localSongList5);
              int j = LocalDevicePlayback.access$1102(LocalDevicePlayback.this, 0);
              LocalDevicePlayback.this.onQueueChanged();
              LocalDevicePlayback.this.openCurrentAndNext(true, false, false, false, 0L, null);
              continue;
            }
          }
          finally
          {
            LocalDevicePlayback.this.mAsyncWakeLock.release();
          }
          if (i != 0)
          {
            int k = LocalDevicePlayback.access$1102(LocalDevicePlayback.this, 0);
            LocalDevicePlayback.this.openCurrentAndNext(true, false, false, false, 0L, null);
          }
        }
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local21);
  }

  public void openMix(MixDescriptor paramMixDescriptor)
  {
    enableInfiniteMixMode(paramMixDescriptor);
    if (this.LOGV)
    {
      Object[] arrayOfObject = new Object[1];
      MixGenerationState localMixGenerationState = this.mMixGenerationState;
      arrayOfObject[0] = localMixGenerationState;
      String str = String.format("openMix: mMixGenerationState=%s", arrayOfObject);
      Log.d("LocalDevicePlayback", str);
    }
    PlayQueueFeeder localPlayQueueFeeder = this.mPlayQueueFeeder;
    PlayQueueFeeder.PostProcessingAction localPostProcessingAction = PlayQueueFeeder.PostProcessingAction.OPEN;
    localPlayQueueFeeder.requestContent(paramMixDescriptor, null, localPostProcessingAction);
  }

  public void pause()
  {
    long l = System.currentTimeMillis();
    this.mLastUserInteractionTime = l;
    pause(false);
  }

  public void play()
  {
    int i = 1;
    StringBuilder localStringBuilder = new StringBuilder().append("play: currentPos=");
    int j = this.mPlayPos;
    String str = j;
    Log.i("LocalDevicePlayback", str);
    long l = System.currentTimeMillis();
    this.mLastUserInteractionTime = l;
    AudioManager localAudioManager = this.mAudioManager;
    AudioManager.OnAudioFocusChangeListener localOnAudioFocusChangeListener = this.mAudioFocusListener;
    int k = localAudioManager.requestAudioFocus(localOnAudioFocusChangeListener, 3, i);
    if (i != k);
    while (i == 0)
    {
      Log.e("LocalDevicePlayback", "play() could not obtain audio focus.");
      return;
      i = 0;
    }
    this.mAsyncWakeLock.acquire();
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local28 = new Runnable()
    {
      public void run()
      {
        try
        {
          if (LocalDevicePlayback.this.mPlayer.isRenderingAudioLocally())
          {
            LocalDevicePlayback.this.mMediaplayerHandler.removeMessages(5);
            boolean bool1 = LocalDevicePlayback.this.mMediaplayerHandler.sendEmptyMessage(6);
          }
          if (!LocalDevicePlayback.this.mIsSupposedToBePlaying)
          {
            boolean bool2 = LocalDevicePlayback.access$2202(LocalDevicePlayback.this, true);
            boolean bool3 = LocalDevicePlayback.access$3602(LocalDevicePlayback.this, false);
            LocalDevicePlayback.this.onPlayStateChanged();
          }
          if (LocalDevicePlayback.this.mPlayList.length() <= 0)
          {
            LocalDevicePlayback.MediaListWrapper localMediaListWrapper = LocalDevicePlayback.this.mPlayList;
            AllSongsList localAllSongsList = new AllSongsList(0);
            localMediaListWrapper.setMediaList(localAllSongsList);
            LocalDevicePlayback.this.next(true);
          }
          for (PowerManager.WakeLock localWakeLock = LocalDevicePlayback.this.mAsyncWakeLock; ; localWakeLock = LocalDevicePlayback.this.mAsyncWakeLock)
          {
            localWakeLock.release();
            return;
            if (!LocalDevicePlayback.this.mPlayer.isInitialized())
            {
              LocalDevicePlayback localLocalDevicePlayback = LocalDevicePlayback.this;
              long l1 = LocalDevicePlayback.this.position();
              localLocalDevicePlayback.openCurrentAndNext(true, false, true, false, l1, null);
              if (LocalDevicePlayback.this.mReloadedQueueSeekPos != 65535L)
              {
                AsyncMediaPlayer localAsyncMediaPlayer = LocalDevicePlayback.this.mPlayer;
                long l2 = LocalDevicePlayback.this.mReloadedQueueSeekPos;
                long l3 = localAsyncMediaPlayer.seek(l2);
                long l4 = LocalDevicePlayback.access$6602(LocalDevicePlayback.this, 65535L);
              }
            }
            long l5 = LocalDevicePlayback.this.mPlayer.duration();
            if ((LocalDevicePlayback.this.mRepeatMode != 1) && (l5 > 2000L))
            {
              long l6 = LocalDevicePlayback.this.mPlayer.position();
              long l7 = l5 - 2000L;
              if (l6 >= l7)
                LocalDevicePlayback.this.next(true);
            }
            LocalDevicePlayback.this.mPlayer.start();
          }
        }
        finally
        {
          LocalDevicePlayback.this.mAsyncWakeLock.release();
        }
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local28);
  }

  public boolean playlistLoading()
  {
    if (this.mPlayList == null);
    for (boolean bool = false; ; bool = this.mPlayList.playlistLoading())
      return bool;
  }

  public long position()
  {
    if (this.mPlayer != null);
    for (long l = this.mPlayer.position(); ; l = this.mReloadedQueueSeekPos)
      return l;
  }

  public void prev()
  {
    StringBuilder localStringBuilder = new StringBuilder().append("prev: currentPos=");
    int i = this.mPlayPos;
    String str = i;
    Log.i("LocalDevicePlayback", str);
    long l = System.currentTimeMillis();
    this.mLastUserInteractionTime = l;
    this.mAsyncWakeLock.acquire();
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local31 = new Runnable()
    {
      public void run()
      {
        while (true)
        {
          try
          {
            PowerManager.WakeLock localWakeLock;
            if ((LocalDevicePlayback.this.mMediaList != null) && (LocalDevicePlayback.this.mMediaList.isFlagSet(32)) && (LocalDevicePlayback.this.position() > 3000L))
            {
              long l = LocalDevicePlayback.this.seek(0L);
              localWakeLock = LocalDevicePlayback.this.mAsyncWakeLock;
              localWakeLock.release();
              return;
            }
            if (LocalDevicePlayback.this.shouldPlayInRandomOrder())
              synchronized (LocalDevicePlayback.this.mFuture)
              {
                int i = LocalDevicePlayback.this.mHistory.size();
                if (i == 0)
                {
                  LocalDevicePlayback.this.fillShuffleList();
                  if (LocalDevicePlayback.this.mFuture.size() == 0)
                  {
                    localWakeLock = LocalDevicePlayback.this.mAsyncWakeLock;
                    continue;
                  }
                  LinkedList localLinkedList2 = LocalDevicePlayback.this.mHistory;
                  Object localObject1 = LocalDevicePlayback.this.mFuture.removeLast();
                  boolean bool = localLinkedList2.add(localObject1);
                  i = LocalDevicePlayback.this.mHistory.size();
                  if (i == 0)
                  {
                    localWakeLock = LocalDevicePlayback.this.mAsyncWakeLock;
                    continue;
                  }
                }
                if (LocalDevicePlayback.this.mPlayPos >= 0)
                {
                  LinkedList localLinkedList3 = LocalDevicePlayback.this.mFuture;
                  Integer localInteger = Integer.valueOf(LocalDevicePlayback.this.mPlayPos);
                  localLinkedList3.add(0, localInteger);
                }
                LinkedList localLinkedList4 = LocalDevicePlayback.this.mHistory;
                int j = i + -1;
                int k = ((Integer)localLinkedList4.remove(j)).intValue();
                int m = LocalDevicePlayback.access$1102(LocalDevicePlayback.this, k);
                LocalDevicePlayback.this.dumpPastPresentAndFuture();
                LocalDevicePlayback.this.saveBookmarkIfNeeded();
                LocalDevicePlayback.this.stop(true);
                LocalDevicePlayback.this.openCurrentAndPrepareToPlaySync(true, false, true, true, null);
                localWakeLock = LocalDevicePlayback.this.mAsyncWakeLock;
              }
          }
          finally
          {
            LocalDevicePlayback.this.mAsyncWakeLock.release();
          }
          LocalDevicePlayback localLocalDevicePlayback1 = LocalDevicePlayback.this;
          LocalDevicePlayback localLocalDevicePlayback2 = LocalDevicePlayback.this;
          int n = LocalDevicePlayback.this.mPlayPos;
          int i1 = localLocalDevicePlayback2.getNextPlayPosition(true, true, n, true);
          int i2 = LocalDevicePlayback.access$1102(localLocalDevicePlayback1, i1);
        }
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local31);
  }

  public void refreshRadio()
  {
    final MixGenerationState localMixGenerationState = this.mMixGenerationState;
    if (localMixGenerationState == null)
      return;
    this.mAsyncWakeLock.acquire();
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local19 = new Runnable()
    {
      public void run()
      {
        try
        {
          MixDescriptor localMixDescriptor = localMixGenerationState.getMix();
          PlayQueueFeeder localPlayQueueFeeder = LocalDevicePlayback.this.mPlayQueueFeeder;
          PlayQueueFeeder.PostProcessingAction localPostProcessingAction = PlayQueueFeeder.PostProcessingAction.CLEAR_REFRESH;
          localPlayQueueFeeder.requestContent(localMixDescriptor, null, localPostProcessingAction);
          return;
        }
        finally
        {
          LocalDevicePlayback.this.mAsyncWakeLock.release();
        }
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local19);
  }

  public void registerMusicCastMediaRouterCallback(IMusicCastMediaRouterCallback paramIMusicCastMediaRouterCallback)
  {
    if (this.LOGV)
      Log.d("LocalDevicePlayback", "registerMusicCastMediaRouterCallback");
    this.mMusicCastMediaRouterCallback = paramIMusicCastMediaRouterCallback;
  }

  public void removeDownloadProgressListener(IDownloadProgressListener paramIDownloadProgressListener)
  {
    if (paramIDownloadProgressListener == null)
      return;
    boolean bool = this.mProgressListeners.unregister(paramIDownloadProgressListener);
  }

  protected void saveState()
  {
    saveQueue(true);
  }

  public long seek(long paramLong)
  {
    String str = "seek: pos=" + paramLong;
    Log.i("LocalDevicePlayback", str);
    long l1 = System.currentTimeMillis();
    this.mLastUserInteractionTime = l1;
    long l3;
    if ((this.mPlayer != null) && (this.mPlayer.isInitialized()))
    {
      if (paramLong < 0L)
        paramLong = 0L;
      long l2 = this.mPlayer.duration();
      if (paramLong >= l2)
      {
        next(true);
        l3 = 65535L;
      }
    }
    while (true)
    {
      return l3;
      if (isPlayingLocally())
      {
        invalidateNextPlayer();
        l3 = this.mPlayer.seek(paramLong);
        setNextTrack();
      }
      else
      {
        l3 = this.mPlayer.seek(paramLong);
        continue;
        l3 = 65535L;
      }
    }
  }

  public void selectDefaultMediaRoute()
  {
    if (!this.mUseLocalPlayer)
      stop();
    this.mUseLocalPlayer = true;
    this.mSelectedRouteId = null;
    this.mMediaRouteSessionId = null;
    Message localMessage = Message.obtain(this.mMediaplayerHandler, 12);
    boolean bool = this.mMediaplayerHandler.sendMessage(localMessage);
  }

  public void setMediaRoute(boolean paramBoolean, String paramString)
  {
    if ((this.mUseLocalPlayer) && (paramBoolean))
      return;
    if ((this.mSelectedRouteId != null) && (this.mSelectedRouteId.equals(paramString)))
      return;
    long l = position();
    boolean bool1;
    if ((isPlaying()) && (!paramBoolean))
    {
      bool1 = true;
      if (!bool1)
      {
        saveQueue(false);
        if (this.mSelectedRouteId == null)
          break label156;
        this.mIsSupposedToBePlaying = false;
        onPlayStateChanged();
        label76: releasePlayers();
      }
      this.mUseLocalPlayer = paramBoolean;
      if (!paramBoolean)
        break label164;
      this.mSelectedRouteId = null;
    }
    for (this.mMediaRouteSessionId = null; ; this.mMediaRouteSessionId = null)
    {
      Handler localHandler = this.mMediaplayerHandler;
      boolean bool2 = paramBoolean;
      String str = paramString;
      SelectedRoute localSelectedRoute = new SelectedRoute(bool2, str, bool1, l);
      Message localMessage = Message.obtain(localHandler, 9, localSelectedRoute);
      boolean bool3 = this.mMediaplayerHandler.sendMessage(localMessage);
      return;
      bool1 = false;
      break;
      label156: stop(false);
      break label76;
      label164: this.mSelectedRouteId = paramString;
    }
  }

  public void setMediaRouteVolume(String paramString, double paramDouble)
  {
    Handler localHandler = this.mMediaplayerHandler;
    RouteVolume localRouteVolume = new RouteVolume(paramString, paramDouble);
    Message localMessage = Message.obtain(localHandler, 10, localRouteVolume);
    boolean bool = this.mMediaplayerHandler.sendMessage(localMessage);
  }

  public void setQueuePosition(int paramInt)
  {
    long l = System.currentTimeMillis();
    this.mLastUserInteractionTime = l;
    if (this.mPlayPos != paramInt)
      return;
    boolean bool = true;
    try
    {
      stop(bool);
      this.mPlayPos = paramInt;
      openCurrentAndPlay(true);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void setRating(int paramInt)
  {
    long l1 = System.currentTimeMillis();
    this.mLastUserInteractionTime = l1;
    ContentIdentifier localContentIdentifier1 = getAudioId();
    if ((localContentIdentifier1 != null) && ((localContentIdentifier1.isDefaultDomain()) || (localContentIdentifier1.isNautilusDomain())))
    {
      ContentResolver localContentResolver = getContext().getContentResolver();
      long l2 = localContentIdentifier1.getId();
      MusicContent.XAudio.setRating(localContentResolver, l2, paramInt);
      ContentIdentifier localContentIdentifier2 = this.mAudioId;
      if (localContentIdentifier1.equals(localContentIdentifier2))
        boolean bool = refreshCursor(localContentIdentifier1);
    }
    if (paramInt != 1)
      return;
    next(true);
  }

  public void setRepeatMode(int paramInt)
  {
    if (this.mMixGenerationState != null)
      return;
    long l = System.currentTimeMillis();
    this.mLastUserInteractionTime = l;
    try
    {
      int i = this.mRepeatMode;
      if (paramInt != i)
        return;
    }
    finally
    {
    }
    this.mRepeatMode = paramInt;
    saveQueue(false);
    if (this.mNextPlayer != null)
      setNextTrack();
    notifyChange("com.google.android.music.repeatmodechanged");
  }

  public void setShuffleMode(int paramInt)
  {
    if (this.mMixGenerationState != null)
      return;
    long l = System.currentTimeMillis();
    this.mLastUserInteractionTime = l;
    try
    {
      if ((this.mShuffleMode != paramInt) && (this.mPlayList.length() > 0))
        return;
    }
    finally
    {
    }
    this.mShuffleMode = paramInt;
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local33 = new Runnable()
    {
      public void run()
      {
        if ((LocalDevicePlayback.this.mShuffleMode == 1) && (LocalDevicePlayback.this.isUsingPlayQueue()))
        {
          Store localStore = Store.getInstance(LocalDevicePlayback.this.getContext());
          int i = LocalDevicePlayback.this.mPlayPos + 1;
          localStore.shufflePlayQueue(i);
        }
        synchronized (LocalDevicePlayback.this)
        {
          synchronized (LocalDevicePlayback.this.mFuture)
          {
            if (LocalDevicePlayback.this.shouldPlayInRandomOrder())
            {
              MusicUtils.debugLog(new Exception("shuffle enabled"));
              StrictShuffler localStrictShuffler = LocalDevicePlayback.this.mRand;
              int j = LocalDevicePlayback.this.mPlayList.length();
              localStrictShuffler.setHistorySize(j);
              LocalDevicePlayback.this.fillShuffleList();
              LocalDevicePlayback.this.saveQueue(false);
              if (LocalDevicePlayback.this.mNextPlayer != null)
                LocalDevicePlayback.this.setNextTrack();
              return;
            }
            MusicUtils.debugLog("shuffle disabled");
            LocalDevicePlayback.this.mFuture.clear();
          }
        }
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local33);
    notifyChange("com.google.android.music.shufflemodechanged");
  }

  public void shuffleAll()
  {
    AllSongsList localAllSongsList = new AllSongsList(-1);
    shuffleSongs(localAllSongsList);
  }

  public void shuffleOnDevice()
  {
    AllOnDeviceSongsList localAllOnDeviceSongsList = new AllOnDeviceSongsList(-1);
    shuffleSongs(localAllOnDeviceSongsList);
  }

  public void shuffleSongs(SongList paramSongList)
  {
    open(paramSongList, true, -1, true);
  }

  public void stop()
  {
    long l = System.currentTimeMillis();
    this.mLastUserInteractionTime = l;
    this.mAsyncWakeLock.acquire();
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local29 = new Runnable()
    {
      public void run()
      {
        try
        {
          LocalDevicePlayback.this.stopSync();
          return;
        }
        finally
        {
          LocalDevicePlayback.this.mAsyncWakeLock.release();
        }
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local29);
  }

  public void stopSync()
  {
    ContentIdentifier localContentIdentifier = getAudioId();
    if (localContentIdentifier != null)
    {
      Object[] arrayOfObject = new Object[2];
      String str1 = localContentIdentifier.toString();
      arrayOfObject[0] = str1;
      String str2 = this.mPlayer.getRemoteSongId();
      arrayOfObject[1] = str2;
      int i = EventLog.writeEvent(74001, arrayOfObject);
      StringBuilder localStringBuilder1 = new StringBuilder().append("Event logging MUSIC_STOP_PLAYBACK_REQUESTED: ");
      String str3 = localContentIdentifier.toString();
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str3).append("/");
      String str4 = this.mPlayer.getRemoteSongId();
      String str5 = str4;
      Log.d("LocalDevicePlayback", str5);
    }
    stop(false);
    releasePlayers();
    onPlayStateChanged();
    boolean bool = loadCurrent();
  }

  public boolean supportsRating()
  {
    return true;
  }

  public void updateMediaRouteVolume(String paramString, double paramDouble)
  {
    Handler localHandler = this.mMediaplayerHandler;
    RouteVolume localRouteVolume = new RouteVolume(paramString, paramDouble);
    Message localMessage = Message.obtain(localHandler, 11, localRouteVolume);
    boolean bool = this.mMediaplayerHandler.sendMessage(localMessage);
  }

  private static class RouteVolume
  {
    public final String mRouteId;
    public final double mValue;

    public RouteVolume(String paramString, double paramDouble)
    {
      this.mRouteId = paramString;
      this.mValue = paramDouble;
    }
  }

  private static class SelectedRoute
  {
    public boolean mAutoPlay;
    public boolean mLocalRoute;
    public long mPosition;
    public String mRouteId;

    public SelectedRoute(boolean paramBoolean1, String paramString, boolean paramBoolean2, long paramLong)
    {
      this.mLocalRoute = paramBoolean1;
      this.mRouteId = paramString;
      this.mAutoPlay = paramBoolean2;
      this.mPosition = paramLong;
    }
  }

  private static class PreviewPlaybackInfo
  {
    public long mPreviewDuration = 65535L;
    public int mPreviewPlayType = 1;
    public String mPreviewUrl;

    PreviewPlaybackInfo(String paramString, int paramInt, long paramLong)
    {
      this.mPreviewDuration = paramLong;
      this.mPreviewPlayType = paramInt;
      this.mPreviewUrl = paramString;
    }
  }

  class MediaListWrapper
  {
    int mColDomainIdx = -1;
    int mColListItemIdx = -1;
    int mColMusicIdx = -1;
    Context mContext;
    MediaList.MediaCursor mCursor;
    private ReadWriteLock mCursorLock;
    private SongList mCursorSongList = null;
    private boolean mPlaylistLoading = false;
    private int mRadiusToSearch;
    private final ContentObserver mRefreshContentObserver;

    public MediaListWrapper(Context arg2)
    {
      ReentrantReadWriteLock localReentrantReadWriteLock = new ReentrantReadWriteLock();
      this.mCursorLock = localReentrantReadWriteLock;
      LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
      ContentObserver local1 = new ContentObserver(localLoggableHandler)
      {
        public void onChange(boolean paramAnonymousBoolean)
        {
          LocalDevicePlayback.MediaListWrapper.this.processContentChange();
        }
      };
      this.mRefreshContentObserver = local1;
      Object localObject;
      this.mContext = localObject;
    }

    private ContentIdentifier.Domain getDomainLocked()
    {
      MediaList.MediaCursor localMediaCursor = this.mCursor;
      int i = this.mColDomainIdx;
      return ContentIdentifier.Domain.fromDBValue(localMediaCursor.getInt(i));
    }

    private boolean moveToPositionLocked(int paramInt, Lock paramLock)
    {
      boolean bool1 = false;
      MusicUtils.checkMainThread(this.mContext, "MediaListWrapper.moveToPositionLocked() on main thread");
      if (paramInt < 0)
        if (LocalDevicePlayback.this.LOGV)
        {
          String str1 = "invalid position requested: " + paramInt;
          Exception localException = new Exception();
          Log.e("LocalDevicePlayback", str1, localException);
        }
      label263: 
      while (true)
      {
        return bool1;
        String str2 = "invalid position requested: " + paramInt;
        Log.e("LocalDevicePlayback", str2);
        continue;
        try
        {
          if (this.mCursor == null)
            continue;
          int i = length();
          if (paramInt < i)
            break label162;
          String str3 = "invalid position requested: " + paramInt + ". List size: " + i;
          Log.e("LocalDevicePlayback", str3);
        }
        catch (CursorIndexOutOfBoundsException localCursorIndexOutOfBoundsException)
        {
        }
        continue;
        label162: if (!this.mCursor.moveToPosition(paramInt))
        {
          String str4 = "Failed to move cursor to position: " + paramInt;
          Log.e("LocalDevicePlayback", str4);
        }
        else
        {
          while (true)
          {
            MediaList.MediaCursor localMediaCursor = this.mCursor;
            int j = this.mColMusicIdx;
            if (!localMediaCursor.isNull(j))
              break label263;
            paramLock.unlock();
            SystemClock.sleep(200L);
            paramLock.lock();
            if (this.mCursor == null)
              break;
            boolean bool2 = this.mCursor.requery();
          }
          bool1 = true;
        }
      }
    }

    private void processContentChange()
    {
      int i = refresh();
      int j;
      boolean bool1;
      if (i == 2)
      {
        j = length();
        bool1 = LocalDevicePlayback.this.isPlaying();
        if ((LocalDevicePlayback.this.mPlayPos >= 0) && (LocalDevicePlayback.this.mPlayPos < j))
        {
          LocalDevicePlayback localLocalDevicePlayback1 = LocalDevicePlayback.this;
          boolean bool2 = false;
          boolean bool3 = false;
          localLocalDevicePlayback1.openCurrentAndNext(false, bool1, bool2, bool3, 0L, null);
        }
      }
      while (true)
      {
        LocalDevicePlayback localLocalDevicePlayback2 = LocalDevicePlayback.this;
        PlayQueueFeeder.PostProcessingAction localPostProcessingAction = PlayQueueFeeder.PostProcessingAction.FEED;
        boolean bool4 = localLocalDevicePlayback2.feedQueueIfNeeded(localPostProcessingAction);
        return;
        if (LocalDevicePlayback.this.mRepeatMode == 2)
        {
          int k = LocalDevicePlayback.access$1102(LocalDevicePlayback.this, 0);
          break;
        }
        if (j > 0)
        {
          LocalDevicePlayback localLocalDevicePlayback3 = LocalDevicePlayback.this;
          int m = j + -1;
          int n = LocalDevicePlayback.access$1102(localLocalDevicePlayback3, m);
          bool1 = false;
          break;
        }
        int i1 = LocalDevicePlayback.access$1102(LocalDevicePlayback.this, 0);
        bool1 = false;
        break;
        if ((i == 1) && (LocalDevicePlayback.this.mAudioId != null))
        {
          LocalDevicePlayback localLocalDevicePlayback4 = LocalDevicePlayback.this;
          ContentIdentifier localContentIdentifier = LocalDevicePlayback.this.mAudioId;
          boolean bool5 = localLocalDevicePlayback4.refreshCursor(localContentIdentifier);
          if (LocalDevicePlayback.this.mNextPlayer != null)
            LocalDevicePlayback.this.setNextTrack();
        }
      }
    }

    private int refresh()
    {
      Lock localLock = null;
      int i = 0;
      refreshLastGroupPosition();
      this.mCursorLock.writeLock().lock();
      try
      {
        SongList localSongList = this.mCursorSongList;
        resetCursorLocked(localSongList);
        int j;
        if (this.mCursor != null)
        {
          j = LocalDevicePlayback.this.mPlayPos;
          if (this.mCursor.getCountSync() == 0)
          {
            this.mCursor.close();
            this.mCursor = null;
            if (LocalDevicePlayback.this.isPlaying())
              LocalDevicePlayback.this.stopSync();
            LocalDevicePlayback.this.onQueueChanged();
            LocalDevicePlayback.this.onSongChanged();
            Log.w("LocalDevicePlayback", "New list returned an empty list");
            localLock = this.mCursorLock.writeLock();
          }
        }
        while (true)
        {
          localLock.unlock();
          while (true)
          {
            return i;
            if ((j != -1) && (LocalDevicePlayback.this.mAudioId != null))
            {
              boolean bool = updateSongPositionLocked();
              if (bool)
              {
                this.mCursorLock.writeLock().unlock();
                i = 1;
                continue;
              }
              StringBuilder localStringBuilder1 = new StringBuilder().append("Could not find old file: ");
              ContentIdentifier localContentIdentifier = LocalDevicePlayback.this.mAudioId;
              StringBuilder localStringBuilder2 = localStringBuilder1.append(localContentIdentifier).append(" in new list with search radius ");
              int k = this.mRadiusToSearch;
              String str = k;
              Log.w("LocalDevicePlayback", str);
              i = 2;
              localLock = this.mCursorLock.writeLock();
              break;
            }
            this.mCursorLock.writeLock().unlock();
            i = 1;
          }
          Log.w("LocalDevicePlayback", "Could not find old position... mCursor was null");
          localLock = this.mCursorLock.writeLock();
        }
      }
      finally
      {
        this.mCursorLock.writeLock().unlock();
      }
    }

    private boolean refreshCurrentSongPosition()
    {
      this.mCursorLock.writeLock().lock();
      try
      {
        boolean bool1 = updateSongPositionLocked();
        boolean bool2 = bool1;
        return bool2;
      }
      finally
      {
        this.mCursorLock.writeLock().unlock();
      }
    }

    private void refreshLastGroupPosition()
    {
      if (LocalDevicePlayback.this.mLastPlayQueueGroupId == 0L)
        return;
      Pair localPair = Store.getInstance(LocalDevicePlayback.this.getContext()).getPlayQueueLastGroupInfo();
      long l1 = ((Long)localPair.first).longValue();
      long l2 = LocalDevicePlayback.this.mLastPlayQueueGroupId;
      if (l1 != l2)
      {
        long l3 = LocalDevicePlayback.access$2902(LocalDevicePlayback.this, 0L);
        int i = LocalDevicePlayback.access$3002(LocalDevicePlayback.this, -1);
        return;
      }
      LocalDevicePlayback localLocalDevicePlayback = LocalDevicePlayback.this;
      int j = ((Integer)localPair.second).intValue();
      int k = LocalDevicePlayback.access$3002(localLocalDevicePlayback, j);
    }

    private void resetCursorLocked(SongList paramSongList)
    {
      if (this.mCursor != null)
      {
        MediaList.MediaCursor localMediaCursor1 = this.mCursor;
        ContentObserver localContentObserver1 = this.mRefreshContentObserver;
        localMediaCursor1.unregisterContentObserver(localContentObserver1);
        this.mCursor.close();
        this.mCursor = null;
      }
      if (paramSongList == null)
        return;
      String[] arrayOfString;
      if (paramSongList.hasUniqueAudioId())
      {
        arrayOfString = new String[2];
        arrayOfString[0] = "_id";
        arrayOfString[1] = "Domain";
        this.mColListItemIdx = -1;
      }
      for (this.mColDomainIdx = 1; ; this.mColDomainIdx = 2)
      {
        this.mColMusicIdx = 0;
        Context localContext = this.mContext;
        MediaList.MediaCursor localMediaCursor2 = paramSongList.getSyncMediaCursor(localContext, arrayOfString, null);
        this.mCursor = localMediaCursor2;
        if (this.mCursor == null)
          return;
        MediaList.MediaCursor localMediaCursor3 = this.mCursor;
        ContentObserver localContentObserver2 = this.mRefreshContentObserver;
        localMediaCursor3.registerContentObserver(localContentObserver2);
        return;
        arrayOfString = new String[3];
        arrayOfString[0] = "audio_id";
        arrayOfString[1] = "_id";
        arrayOfString[2] = "Domain";
        this.mColListItemIdx = 1;
      }
    }

    private boolean updateSongPositionLocked()
    {
      boolean bool;
      if (LocalDevicePlayback.this.mAudioId == null)
        bool = false;
      while (true)
      {
        return bool;
        int m;
        if (this.mColListItemIdx < 0)
        {
          long l1 = LocalDevicePlayback.this.mAudioId.getId();
          int i = LocalDevicePlayback.this.mPlayPos;
          MediaList.MediaCursor localMediaCursor1 = this.mCursor;
          int j = this.mColMusicIdx;
          int k = this.mRadiusToSearch;
          m = DbUtils.findItemInCursor(l1, i, localMediaCursor1, j, k);
          if (m < 0)
          {
            bool = false;
          }
          else
          {
            int n = LocalDevicePlayback.access$1102(LocalDevicePlayback.this, m);
            bool = true;
          }
        }
        else if (LocalDevicePlayback.this.mMediaList.hasStablePrimaryIds())
        {
          long l2 = LocalDevicePlayback.this.mListItemId;
          int i1 = LocalDevicePlayback.this.mPlayPos;
          MediaList.MediaCursor localMediaCursor2 = this.mCursor;
          int i2 = this.mColListItemIdx;
          int i3 = this.mRadiusToSearch;
          m = DbUtils.findItemInCursor(l2, i1, localMediaCursor2, i2, i3);
          if (m < 0)
          {
            bool = false;
          }
          else
          {
            int i4 = LocalDevicePlayback.access$1102(LocalDevicePlayback.this, m);
            bool = true;
          }
        }
        else
        {
          long l3 = LocalDevicePlayback.this.mListItemId;
          long l4 = LocalDevicePlayback.this.mAudioId.getId();
          int i5 = LocalDevicePlayback.this.mPlayPos;
          MediaList.MediaCursor localMediaCursor3 = this.mCursor;
          int i6 = this.mColListItemIdx;
          int i7 = this.mColMusicIdx;
          int i8 = this.mRadiusToSearch;
          m = DbUtils.findIndirectlyReferencedItem(l3, l4, i5, localMediaCursor3, i6, i7, i8);
          if (m < 0)
          {
            bool = false;
          }
          else
          {
            int i9 = LocalDevicePlayback.access$1102(LocalDevicePlayback.this, m);
            if (this.mCursor.moveToPosition(m))
            {
              MediaList.MediaCursor localMediaCursor4 = this.mCursor;
              int i10 = this.mColListItemIdx;
              long l5 = localMediaCursor4.getLong(i10);
              long l6 = LocalDevicePlayback.this.mListItemId;
              if (l5 != l6)
              {
                StringBuilder localStringBuilder1 = new StringBuilder().append("Now playing song (");
                long l7 = LocalDevicePlayback.this.mAudioId.getId();
                StringBuilder localStringBuilder2 = localStringBuilder1.append(l7).append(") list item id changed from ");
                long l8 = LocalDevicePlayback.this.mListItemId;
                String str = l8 + " to " + l5;
                Log.w("LocalDevicePlayback", str);
                long l9 = LocalDevicePlayback.access$802(LocalDevicePlayback.this, l5);
              }
            }
            bool = true;
          }
        }
      }
    }

    // ERROR //
    public ContentIdentifier get(int paramInt)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 51	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:mCursorLock	Ljava/util/concurrent/locks/ReadWriteLock;
      //   4: invokeinterface 230 1 0
      //   9: astore_2
      //   10: aload_2
      //   11: invokeinterface 172 1 0
      //   16: aload_0
      //   17: iload_1
      //   18: aload_2
      //   19: invokespecial 382	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:moveToPositionLocked	(ILjava/util/concurrent/locks/Lock;)Z
      //   22: ifeq +50 -> 72
      //   25: aload_0
      //   26: getfield 83	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:mCursor	Lcom/google/android/music/medialist/MediaList$MediaCursor;
      //   29: astore_3
      //   30: aload_0
      //   31: getfield 38	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:mColMusicIdx	I
      //   34: istore 4
      //   36: aload_3
      //   37: iload 4
      //   39: invokevirtual 366	com/google/android/music/medialist/MediaList$MediaCursor:getLong	(I)J
      //   42: lstore 5
      //   44: aload_0
      //   45: invokespecial 384	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:getDomainLocked	()Lcom/google/android/music/download/ContentIdentifier$Domain;
      //   48: astore 7
      //   50: new 339	com/google/android/music/download/ContentIdentifier
      //   53: dup
      //   54: lload 5
      //   56: aload 7
      //   58: invokespecial 387	com/google/android/music/download/ContentIdentifier:<init>	(JLcom/google/android/music/download/ContentIdentifier$Domain;)V
      //   61: astore 8
      //   63: aload_2
      //   64: invokeinterface 161 1 0
      //   69: aload 8
      //   71: areturn
      //   72: aload_2
      //   73: invokeinterface 161 1 0
      //   78: aconst_null
      //   79: astore 8
      //   81: goto -12 -> 69
      //   84: astore 9
      //   86: aload_2
      //   87: invokeinterface 161 1 0
      //   92: aconst_null
      //   93: astore 8
      //   95: goto -26 -> 69
      //   98: astore 10
      //   100: aload_2
      //   101: invokeinterface 161 1 0
      //   106: aload 10
      //   108: athrow
      //   109: astore 11
      //   111: goto -19 -> 92
      //   114: astore 12
      //   116: goto -10 -> 106
      //   119: astore 13
      //   121: goto -52 -> 69
      //   124: astore 14
      //   126: goto -48 -> 78
      //
      // Exception table:
      //   from	to	target	type
      //   10	63	84	android/database/CursorIndexOutOfBoundsException
      //   10	63	98	finally
      //   86	92	109	java/lang/Exception
      //   100	106	114	java/lang/Exception
      //   63	69	119	java/lang/Exception
      //   72	78	124	java/lang/Exception
    }

    // ERROR //
    public Pair<ContentIdentifier, Long> getAudioIdAndListItemId(int paramInt)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 51	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:mCursorLock	Ljava/util/concurrent/locks/ReadWriteLock;
      //   4: invokeinterface 230 1 0
      //   9: astore_2
      //   10: aload_2
      //   11: invokeinterface 172 1 0
      //   16: aload_0
      //   17: iload_1
      //   18: aload_2
      //   19: invokespecial 382	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:moveToPositionLocked	(ILjava/util/concurrent/locks/Lock;)Z
      //   22: ifeq +102 -> 124
      //   25: ldc2_w 186
      //   28: lstore_3
      //   29: aload_0
      //   30: getfield 40	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:mColListItemIdx	I
      //   33: iflt +23 -> 56
      //   36: aload_0
      //   37: getfield 83	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:mCursor	Lcom/google/android/music/medialist/MediaList$MediaCursor;
      //   40: astore 5
      //   42: aload_0
      //   43: getfield 40	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:mColListItemIdx	I
      //   46: istore 6
      //   48: aload 5
      //   50: iload 6
      //   52: invokevirtual 366	com/google/android/music/medialist/MediaList$MediaCursor:getLong	(I)J
      //   55: lstore_3
      //   56: aload_0
      //   57: getfield 83	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:mCursor	Lcom/google/android/music/medialist/MediaList$MediaCursor;
      //   60: astore 7
      //   62: aload_0
      //   63: getfield 38	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:mColMusicIdx	I
      //   66: istore 8
      //   68: aload 7
      //   70: iload 8
      //   72: invokevirtual 366	com/google/android/music/medialist/MediaList$MediaCursor:getLong	(I)J
      //   75: lstore 9
      //   77: aload_0
      //   78: invokespecial 384	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:getDomainLocked	()Lcom/google/android/music/download/ContentIdentifier$Domain;
      //   81: astore 11
      //   83: new 339	com/google/android/music/download/ContentIdentifier
      //   86: dup
      //   87: lload 9
      //   89: aload 11
      //   91: invokespecial 387	com/google/android/music/download/ContentIdentifier:<init>	(JLcom/google/android/music/download/ContentIdentifier$Domain;)V
      //   94: astore 12
      //   96: lload_3
      //   97: invokestatic 393	java/lang/Long:valueOf	(J)Ljava/lang/Long;
      //   100: astore 13
      //   102: new 288	android/util/Pair
      //   105: dup
      //   106: aload 12
      //   108: aload 13
      //   110: invokespecial 396	android/util/Pair:<init>	(Ljava/lang/Object;Ljava/lang/Object;)V
      //   113: astore 14
      //   115: aload_2
      //   116: invokeinterface 161 1 0
      //   121: aload 14
      //   123: areturn
      //   124: aload_2
      //   125: invokeinterface 161 1 0
      //   130: aconst_null
      //   131: astore 14
      //   133: goto -12 -> 121
      //   136: astore 15
      //   138: aload_2
      //   139: invokeinterface 161 1 0
      //   144: aconst_null
      //   145: astore 14
      //   147: goto -26 -> 121
      //   150: astore 16
      //   152: aload_2
      //   153: invokeinterface 161 1 0
      //   158: aload 16
      //   160: athrow
      //   161: astore 17
      //   163: goto -19 -> 144
      //   166: astore 18
      //   168: goto -10 -> 158
      //   171: astore 19
      //   173: goto -52 -> 121
      //   176: astore 20
      //   178: goto -48 -> 130
      //
      // Exception table:
      //   from	to	target	type
      //   10	115	136	android/database/CursorIndexOutOfBoundsException
      //   10	115	150	finally
      //   138	144	161	java/lang/Exception
      //   152	158	166	java/lang/Exception
      //   115	121	171	java/lang/Exception
      //   124	130	176	java/lang/Exception
    }

    // ERROR //
    public List<ContentIdentifier> getTailTracks(int paramInt)
    {
      // Byte code:
      //   0: new 400	java/util/LinkedList
      //   3: dup
      //   4: invokespecial 401	java/util/LinkedList:<init>	()V
      //   7: astore_2
      //   8: aload_0
      //   9: getfield 51	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:mCursorLock	Ljava/util/concurrent/locks/ReadWriteLock;
      //   12: invokeinterface 230 1 0
      //   17: astore_3
      //   18: aload_3
      //   19: invokeinterface 172 1 0
      //   24: aload_0
      //   25: invokevirtual 145	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:length	()I
      //   28: istore 4
      //   30: iconst_0
      //   31: istore 5
      //   33: iload_1
      //   34: iload 4
      //   36: if_icmpge +11 -> 47
      //   39: aload_0
      //   40: invokevirtual 145	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:length	()I
      //   43: iload_1
      //   44: isub
      //   45: istore 5
      //   47: iload 5
      //   49: istore 6
      //   51: iload 6
      //   53: iload 4
      //   55: if_icmpge +144 -> 199
      //   58: aload_0
      //   59: iload 6
      //   61: aload_3
      //   62: invokespecial 382	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:moveToPositionLocked	(ILjava/util/concurrent/locks/Lock;)Z
      //   65: ifeq +62 -> 127
      //   68: aload_0
      //   69: getfield 83	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:mCursor	Lcom/google/android/music/medialist/MediaList$MediaCursor;
      //   72: astore 7
      //   74: aload_0
      //   75: getfield 38	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:mColMusicIdx	I
      //   78: istore 8
      //   80: aload 7
      //   82: iload 8
      //   84: invokevirtual 366	com/google/android/music/medialist/MediaList$MediaCursor:getLong	(I)J
      //   87: lstore 9
      //   89: aload_0
      //   90: invokespecial 384	com/google/android/music/playback/LocalDevicePlayback$MediaListWrapper:getDomainLocked	()Lcom/google/android/music/download/ContentIdentifier$Domain;
      //   93: astore 11
      //   95: new 339	com/google/android/music/download/ContentIdentifier
      //   98: dup
      //   99: lload 9
      //   101: aload 11
      //   103: invokespecial 387	com/google/android/music/download/ContentIdentifier:<init>	(JLcom/google/android/music/download/ContentIdentifier$Domain;)V
      //   106: astore 12
      //   108: aload_2
      //   109: aload 12
      //   111: invokeinterface 407 2 0
      //   116: istore 13
      //   118: iload 6
      //   120: iconst_1
      //   121: iadd
      //   122: istore 6
      //   124: goto -73 -> 51
      //   127: new 113	java/lang/StringBuilder
      //   130: dup
      //   131: invokespecial 114	java/lang/StringBuilder:<init>	()V
      //   134: ldc_w 409
      //   137: invokevirtual 120	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   140: iload 5
      //   142: invokevirtual 123	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   145: invokevirtual 127	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   148: astore 14
      //   150: ldc 132
      //   152: aload 14
      //   154: invokestatic 254	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;)V
      //   157: aload_3
      //   158: invokeinterface 161 1 0
      //   163: aload_2
      //   164: areturn
      //   165: astore 15
      //   167: aconst_null
      //   168: astore_2
      //   169: aload_3
      //   170: invokeinterface 161 1 0
      //   175: goto -12 -> 163
      //   178: astore 16
      //   180: goto -17 -> 163
      //   183: astore 17
      //   185: aload_3
      //   186: invokeinterface 161 1 0
      //   191: aload 17
      //   193: athrow
      //   194: astore 18
      //   196: goto -5 -> 191
      //   199: aload_3
      //   200: invokeinterface 161 1 0
      //   205: goto -42 -> 163
      //
      // Exception table:
      //   from	to	target	type
      //   18	157	165	android/database/CursorIndexOutOfBoundsException
      //   157	175	178	java/lang/Exception
      //   199	205	178	java/lang/Exception
      //   18	157	183	finally
      //   185	191	194	java/lang/Exception
    }

    public boolean isValid()
    {
      if (this.mCursor != null);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public int length()
    {
      try
      {
        this.mCursorLock.readLock().lock();
        if (this.mCursor != null)
        {
          int i = this.mCursor.getCountSync();
          j = i;
          localLock = this.mCursorLock.readLock();
          return j;
        }
        int j = 0;
        Lock localLock = this.mCursorLock.readLock();
      }
      finally
      {
        this.mCursorLock.readLock().unlock();
      }
    }

    public boolean playlistLoading()
    {
      return this.mPlaylistLoading;
    }

    public void setMediaList(SongList paramSongList)
    {
      try
      {
        this.mPlaylistLoading = true;
        ContentIdentifier localContentIdentifier = LocalDevicePlayback.access$4102(LocalDevicePlayback.this, null);
        long l = LocalDevicePlayback.access$802(LocalDevicePlayback.this, 0L);
        this.mCursorLock.writeLock().lock();
        this.mCursorSongList = paramSongList;
        SongList localSongList = this.mCursorSongList;
        resetCursorLocked(localSongList);
        if (this.mCursorSongList != null)
        {
          int i = paramSongList.getSuggestedPositionSearchRadius();
          this.mRadiusToSearch = i;
        }
        return;
      }
      finally
      {
        this.mPlaylistLoading = false;
        this.mCursorLock.writeLock().unlock();
      }
    }

    public void setPlaylistLoading(boolean paramBoolean)
    {
      this.mPlaylistLoading = paramBoolean;
    }
  }

  public static abstract interface ServiceHooks
  {
    public abstract void cancelAllStreamingTracks();

    public abstract void cancelTryNext();

    public abstract void markSongPlayed(ContentIdentifier paramContentIdentifier)
      throws RemoteException;

    public abstract void notifyMediaRouteInvalidated();

    public abstract void notifyOpenComplete();

    public abstract void notifyOpenStarted();

    public abstract void notifyPlayStateChanged();

    public abstract void onDownloadProgress(DownloadProgress paramDownloadProgress);

    public abstract void reportTrackEnded();

    public abstract void reportTrackPaused();

    public abstract void reportTrackPlaying();

    public abstract void setMediaRouteSessionId(String paramString);

    public abstract String streamTrack(ContentIdentifier paramContentIdentifier, long paramLong, IDownloadProgressListener paramIDownloadProgressListener, boolean paramBoolean1, boolean paramBoolean2)
      throws DownloadRequestException, OutOfSpaceException;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.playback.LocalDevicePlayback
 * JD-Core Version:    0.6.2
 */