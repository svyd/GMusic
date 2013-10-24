package com.google.android.music.cast;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.support.v7.media.MediaItemStatus;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.ControlRequestCallback;
import android.support.v7.media.MediaRouter.ProviderInfo;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.text.TextUtils;
import com.google.android.common.http.GoogleHttpClient;
import com.google.android.gsf.Gservices;
import com.google.android.music.cloudclient.MusicRequest;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.DownloadUtils;
import com.google.android.music.download.RequestSigningUtil;
import com.google.android.music.log.Log;
import com.google.android.music.playback.AsyncMediaPlayer;
import com.google.android.music.playback.AsyncMediaPlayer.AsyncCommandCallback;
import com.google.android.music.playback.LocalDevicePlayback.ServiceHooks;
import com.google.android.music.playback.StopWatch;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.DataNotFoundException;
import com.google.android.music.store.MusicFile;
import com.google.android.music.store.Store;
import com.google.android.music.utils.AlbumArtUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.PlaybackUtils;
import com.google.android.music.utils.RouteChecker;
import com.google.android.music.utils.async.AsyncWorkers;
import java.util.UUID;

public class RemoteAsyncMediaPlayer
  implements AsyncMediaPlayer
{
  private static final Intent ACTION_ENQUEUE_BASE_INTENT = new Intent();
  private int mAudioSessionId = -1;
  private volatile AsyncMediaPlayer.AsyncCommandCallback mCallback;
  private final CastTokenClient mCastTokenClient;
  private final Context mContext;
  private volatile ContentIdentifier mCurrentSongId = null;
  private volatile int mDownloadError;
  private volatile boolean mFetchedCastToken = false;
  private boolean mFromUserAction = false;
  private final Handler mHandler;
  private boolean mIsCurrent = false;
  private volatile boolean mIsPlaybackRequested;
  private volatile String mItemId = null;
  private final String mItemStatusCategory;
  private ItemStatusReceiver mItemStatusReceiver = null;
  private long mKnownPositionMillis;
  private final Object mLock;
  private MediaRouter mMediaRouter;
  private volatile MusicFile mMusicFile = null;
  private final MusicPreferences mMusicPreferences;
  private RemoteAsyncMediaPlayer mNextPlayer = null;
  private volatile int mNumberOfRetries;
  private StopWatch mPositionDeltaStopWatch;
  private final boolean mPrequeueItems;
  private MediaRouter.RouteInfo mRoute = null;
  private final RouteChecker mRouteChecker;
  private final LocalDevicePlayback.ServiceHooks mServiceHooks;
  private volatile String mSessionId = null;
  private volatile State mState;
  private volatile boolean mTryRefreshCastToken;
  private String mUrl = null;
  private PowerManager.WakeLock mWakeLock;

  static
  {
    Intent localIntent1 = ACTION_ENQUEUE_BASE_INTENT.addCategory("android.media.intent.category.REMOTE_PLAYBACK");
    Intent localIntent2 = ACTION_ENQUEUE_BASE_INTENT;
    Uri localUri = Uri.parse("https://android.clients.google.com/music/mplay");
    Intent localIntent3 = localIntent2.setDataAndType(localUri, "audio/mpeg");
    Intent localIntent4 = ACTION_ENQUEUE_BASE_INTENT.setAction("android.media.intent.action.ENQUEUE");
  }

  public RemoteAsyncMediaPlayer(Context paramContext, CastTokenClient paramCastTokenClient, LocalDevicePlayback.ServiceHooks paramServiceHooks, boolean paramBoolean, String paramString)
  {
    StopWatch localStopWatch = new StopWatch();
    this.mPositionDeltaStopWatch = localStopWatch;
    this.mKnownPositionMillis = 0L;
    State localState = State.NONE;
    this.mState = localState;
    this.mDownloadError = 1;
    this.mTryRefreshCastToken = false;
    this.mNumberOfRetries = 0;
    Object localObject = new Object();
    this.mLock = localObject;
    this.mIsPlaybackRequested = false;
    this.mContext = paramContext;
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, this);
    this.mMusicPreferences = localMusicPreferences;
    this.mCastTokenClient = paramCastTokenClient;
    MyHandler localMyHandler = new MyHandler();
    this.mHandler = localMyHandler;
    String str1 = UUID.randomUUID().toString();
    this.mItemStatusCategory = str1;
    this.mServiceHooks = paramServiceHooks;
    this.mPrequeueItems = paramBoolean;
    RouteChecker localRouteChecker = new RouteChecker(paramContext);
    this.mRouteChecker = localRouteChecker;
    this.mSessionId = paramString;
    if (isLogVerbose())
      logv("Created RemoteAsyncMediaPlayer.");
    PowerManager localPowerManager = (PowerManager)this.mContext.getSystemService("power");
    StringBuilder localStringBuilder = new StringBuilder();
    String str2 = getClass().getName();
    String str3 = str2 + ".mWakeLock";
    PowerManager.WakeLock localWakeLock = localPowerManager.newWakeLock(1, str3);
    this.mWakeLock = localWakeLock;
  }

  private void clear(boolean paramBoolean)
  {
    this.mItemId = null;
    this.mSessionId = null;
    this.mIsPlaybackRequested = false;
    if (!paramBoolean)
      return;
    this.mFetchedCastToken = false;
    if (this.mRoute == null)
      return;
    CastTokenClient localCastTokenClient = this.mCastTokenClient;
    String str = this.mRoute.getId();
    localCastTokenClient.clearCachedCastToken(str);
  }

  private String generateMplayUrl(boolean paramBoolean, MusicFile paramMusicFile)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str1;
    label51: StringBuilder localStringBuilder3;
    StringBuilder localStringBuilder4;
    switch (paramMusicFile.getSourceType())
    {
    default:
      str1 = null;
      return str1;
    case 2:
      StringBuilder localStringBuilder2 = localStringBuilder1.append("https://android.clients.google.com/music/mplay?songid=");
      String str2 = paramMusicFile.getSourceId();
      localStringBuilder3 = localStringBuilder1.append(str2);
      localStringBuilder4 = new StringBuilder().append("&pt=");
      if (!paramBoolean)
        break;
    case 3:
    }
    for (String str3 = "e"; ; str3 = "a")
    {
      String str4 = str3;
      StringBuilder localStringBuilder5 = localStringBuilder3.append(str4);
      RequestSigningUtil.appendMplayUrlSignatureParams(paramMusicFile.getSourceId(), localStringBuilder1);
      String str5 = localStringBuilder1.toString();
      str1 = MusicRequest.getSharedHttpClient(this.mContext).rewriteURI(str5);
      if (str1 != null)
        break;
      str1 = null;
      break;
      StringBuilder localStringBuilder6 = localStringBuilder1.append("https://android.clients.google.com/music/mplay?mjck=");
      break label51;
    }
  }

  private MediaRouter.RouteInfo getSelectedRouteOnMainThread()
  {
    MediaRouter.RouteInfo localRouteInfo;
    if (this.mRoute != null)
      localRouteInfo = this.mRoute;
    while (true)
    {
      return localRouteInfo;
      if (this.mMediaRouter == null)
      {
        MediaRouter localMediaRouter = MediaRouter.getInstance(this.mContext);
        this.mMediaRouter = localMediaRouter;
      }
      localRouteInfo = this.mMediaRouter.getSelectedRoute();
      if (localRouteInfo == null)
      {
        logw("Selected route is null -- should not happen!");
        notifyFailure(false);
        localRouteInfo = null;
      }
      else if (localRouteInfo.getPlaybackType() != 1)
      {
        logw("Selected route is no longer remote -- bailing!");
        notifyFailure(false);
        localRouteInfo = null;
      }
      else if (!this.mRouteChecker.isAcceptableRoute(localRouteInfo))
      {
        logw("Selected route is not acceptable -- bailing!");
        notifyFailure(false);
        localRouteInfo = null;
      }
      else
      {
        this.mRoute = localRouteInfo;
      }
    }
  }

  private ErrorHandlingAction handleRemoteHttpError(int paramInt, Bundle paramBundle)
  {
    String str1 = null;
    Object[] arrayOfObject1 = new Object[2];
    Integer localInteger = Integer.valueOf(paramInt);
    arrayOfObject1[0] = localInteger;
    String str2 = DebugUtils.bundleToString(paramBundle);
    arrayOfObject1[1] = str2;
    String str3 = String.format("handleRemoteHttpError: statusCode=%s headers=%s", arrayOfObject1);
    logd(str3);
    Object localObject = ErrorHandlingAction.REPORT_ERROR;
    if (paramInt == 401)
      if (!this.mTryRefreshCastToken)
      {
        logd("Refreshing the cast token");
        this.mTryRefreshCastToken = true;
        localObject = ErrorHandlingAction.RETRY;
        clear(true);
        State localState = State.WAITING_FOR_CAST_TOKEN;
        setState(localState);
        Message localMessage1 = this.mHandler.obtainMessage(1);
        boolean bool1 = this.mHandler.sendMessage(localMessage1);
        Message localMessage2 = this.mHandler.obtainMessage(2);
        boolean bool2 = this.mHandler.sendMessageDelayed(localMessage2, 10000L);
      }
    while (true)
    {
      String str4 = "Error handling action: " + localObject;
      logd(str4);
      return localObject;
      logd("Already tried to refresh the cast token once - bailing");
      this.mDownloadError = 4;
      continue;
      if (paramInt == 403)
      {
        str1 = paramBundle.getString("X-Rejected-Reason");
        if (!TextUtils.isEmpty(str1))
          if ("DEVICE_NOT_AUTHORIZED".equalsIgnoreCase(str1))
          {
            this.mDownloadError = 5;
          }
          else if ("ANOTHER_STREAM_BEING_PLAYED".equalsIgnoreCase(str1))
          {
            this.mDownloadError = 6;
          }
          else if ("STREAM_RATE_LIMIT_REACHED".equalsIgnoreCase(str1))
          {
            this.mDownloadError = 7;
          }
          else if ("TRACK_NOT_IN_SUBSCRIPTION".equalsIgnoreCase(str1))
          {
            this.mDownloadError = 13;
            ContentIdentifier localContentIdentifier = this.mCurrentSongId;
            if (localContentIdentifier != null)
              DownloadUtils.purgeNautilusTrackByLocalId(this.mContext, localContentIdentifier);
            localObject = ErrorHandlingAction.REPORT_ERROR_AND_SKIP;
          }
      }
      else if (paramInt == 404)
      {
        localObject = ErrorHandlingAction.REPORT_ERROR_AND_SKIP;
      }
      else if (paramInt == 503)
      {
        if (this.mNumberOfRetries < 5)
        {
          int i = this.mNumberOfRetries + 1;
          this.mNumberOfRetries = i;
          ErrorHandlingAction localErrorHandlingAction = ErrorHandlingAction.RETRY;
          long l1 = 0L;
          String str5 = paramBundle.getString("Retry-After");
          if (!TextUtils.isEmpty(str5));
          while (true)
          {
            try
            {
              l1 = Long.valueOf(str5).longValue();
              Object[] arrayOfObject2 = new Object[1];
              Long localLong = Long.valueOf(l1);
              arrayOfObject2[0] = localLong;
              String str6 = String.format("Server said to retry after %s sec", arrayOfObject2);
              logw(str6);
              if (l1 == 0L)
                break label484;
              l2 = l1 * 1000L;
              retryPlaybackRequest(l2);
              localObject = localErrorHandlingAction;
            }
            catch (NumberFormatException localNumberFormatException)
            {
              logw("Received 503 with invalid Retry-After header", localNumberFormatException);
              continue;
            }
            logw("Received 503 with no Retry-After header");
            continue;
            label484: long l2 = 1000L;
          }
        }
      }
      else if ((paramInt == 502) && (this.mNumberOfRetries < 5))
      {
        int j = this.mNumberOfRetries + 1;
        this.mNumberOfRetries = j;
        localObject = ErrorHandlingAction.RETRY;
        retryPlaybackRequest(1000L);
      }
    }
  }

  public static boolean isActionEnqueueSupported(MediaRouter.RouteInfo paramRouteInfo)
  {
    if (paramRouteInfo == null)
      Log.w("MusicCast", "isActionEnqueueSupported: null route");
    Intent localIntent;
    for (boolean bool = false; ; bool = paramRouteInfo.supportsControlRequest(localIntent))
    {
      return bool;
      localIntent = ACTION_ENQUEUE_BASE_INTENT;
    }
  }

  private boolean isLogVerbose()
  {
    return this.mMusicPreferences.isLogFilesEnabled();
  }

  private void logd(String paramString)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str1 = this.mItemStatusCategory;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append("[");
    State localState = this.mState;
    StringBuilder localStringBuilder3 = localStringBuilder2.append(localState).append("(");
    String str2 = this.mSessionId;
    StringBuilder localStringBuilder4 = localStringBuilder3.append(str2).append("/");
    String str3 = this.mItemId;
    String str4 = str3 + ")]: " + paramString;
    Log.d("MusicCast", str4);
  }

  private void logv(String paramString)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str1 = this.mItemStatusCategory;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append("[");
    State localState = this.mState;
    StringBuilder localStringBuilder3 = localStringBuilder2.append(localState).append("(");
    String str2 = this.mSessionId;
    StringBuilder localStringBuilder4 = localStringBuilder3.append(str2).append("/");
    String str3 = this.mItemId;
    String str4 = str3 + ")]: " + paramString;
    Log.d("MusicCast", str4);
  }

  private void logw(String paramString)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str1 = this.mItemStatusCategory;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append("[");
    State localState = this.mState;
    StringBuilder localStringBuilder3 = localStringBuilder2.append(localState).append("(");
    String str2 = this.mSessionId;
    StringBuilder localStringBuilder4 = localStringBuilder3.append(str2).append("/");
    String str3 = this.mItemId;
    String str4 = str3 + ")]: " + paramString;
    Log.w("MusicCast", str4);
  }

  private void logw(String paramString, Exception paramException)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str1 = this.mItemStatusCategory;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append("[");
    State localState = this.mState;
    StringBuilder localStringBuilder3 = localStringBuilder2.append(localState).append("(");
    String str2 = this.mSessionId;
    StringBuilder localStringBuilder4 = localStringBuilder3.append(str2).append("/");
    String str3 = this.mItemId;
    String str4 = str3 + ")]: " + paramString;
    Log.w("MusicCast", str4, paramException);
  }

  private void notifyFailure(boolean paramBoolean)
  {
    AsyncMediaPlayer.AsyncCommandCallback localAsyncCommandCallback = this.mCallback;
    if (localAsyncCommandCallback == null)
      return;
    if (!this.mIsCurrent)
      return;
    localAsyncCommandCallback.onFailure(paramBoolean);
  }

  private void notifyIfSongPlayed()
  {
    final ContentIdentifier localContentIdentifier = this.mCurrentSongId;
    if (localContentIdentifier == null)
      return;
    Context localContext = this.mContext;
    long l1 = duration();
    long l2 = position();
    if (!PlaybackUtils.isPlayed(localContext, l1, l2))
      return;
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local6 = new Runnable()
    {
      public void run()
      {
        try
        {
          LocalDevicePlayback.ServiceHooks localServiceHooks = RemoteAsyncMediaPlayer.this.mServiceHooks;
          ContentIdentifier localContentIdentifier = localContentIdentifier;
          localServiceHooks.markSongPlayed(localContentIdentifier);
          return;
        }
        catch (RemoteException localRemoteException)
        {
          RemoteAsyncMediaPlayer.this.logw("Could not mark song as played", localRemoteException);
        }
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local6);
  }

  private void populateSessionId(Intent paramIntent)
  {
    if (this.mSessionId == null)
      return;
    String str = this.mSessionId;
    Intent localIntent = paramIntent.putExtra("android.media.intent.extra.SESSION_ID", str);
  }

  private Intent preparePlayIntent(String paramString1, String paramString2, MusicFile paramMusicFile)
  {
    Intent localIntent1 = new Intent();
    Intent localIntent2 = localIntent1.addCategory("android.media.intent.category.REMOTE_PLAYBACK");
    Uri localUri = Uri.parse(paramString1);
    Intent localIntent3 = localIntent1.setDataAndType(localUri, "audio/mpeg");
    if ((!this.mFromUserAction) && (this.mPrequeueItems) && (this.mSessionId != null))
      Intent localIntent4 = localIntent1.setAction("android.media.intent.action.ENQUEUE");
    while (true)
    {
      populateSessionId(localIntent1);
      long l1 = position();
      if (l1 > 0L)
        Intent localIntent5 = localIntent1.putExtra("android.media.intent.extra.ITEM_POSITION", l1);
      Bundle localBundle1 = new Bundle();
      String str1 = "playon=" + paramString2;
      localBundle1.putString("Authorization", str1);
      Intent localIntent6 = localIntent1.putExtra("android.media.intent.extra.HTTP_HEADERS", localBundle1);
      Bundle localBundle2 = new Bundle();
      String str2 = paramMusicFile.getTitle();
      localBundle2.putString("android.media.metadata.TITLE", str2);
      String str3 = paramMusicFile.getAlbumArtist();
      localBundle2.putString("android.media.metadata.ALBUM_ARTIST", str3);
      String str4 = paramMusicFile.getTrackArtist();
      localBundle2.putString("android.media.metadata.ARTIST", str4);
      String str5 = paramMusicFile.getAlbumName();
      localBundle2.putString("android.media.metadata.ALBUM_TITLE", str5);
      String str6 = AlbumArtUtils.stripDimensionFromImageUrl(paramMusicFile.getAlbumArtLocation());
      localBundle2.putString("android.media.metadata.ARTWORK_URI", str6);
      long l2 = paramMusicFile.getDurationInMilliSec();
      localBundle2.putLong("android.media.metadata.DURATION", l2);
      if (isLogVerbose())
      {
        StringBuilder localStringBuilder1 = new StringBuilder();
        String str7 = this.mItemStatusCategory;
        StringBuilder localStringBuilder2 = localStringBuilder1.append(str7).append(": Metadata:");
        String str8 = DebugUtils.bundleToString(localBundle2);
        String str9 = str8;
        Log.v("MusicCast", str9);
      }
      Intent localIntent7 = localIntent1.putExtra("android.media.intent.extra.ITEM_METADATA", localBundle2);
      Intent localIntent8 = new Intent("com.google.android.music.cast.ACTION_STATUS_CHANGED");
      String str10 = this.mItemStatusCategory;
      Intent localIntent9 = localIntent8.addCategory(str10);
      PendingIntent localPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, localIntent8, 0);
      Intent localIntent10 = localIntent1.putExtra("android.media.intent.extra.ITEM_STATUS_UPDATE_RECEIVER", localPendingIntent);
      return localIntent1;
      Intent localIntent11 = localIntent1.setAction("android.media.intent.action.PLAY");
    }
  }

  private void processControlRequestResultBundle(Bundle paramBundle)
  {
    if (paramBundle == null)
    {
      processError("Result data is null!", paramBundle);
      return;
    }
    if (paramBundle.containsKey("android.media.intent.extra.ITEM_STATUS"))
    {
      MediaItemStatus localMediaItemStatus = MediaItemStatus.fromBundle(paramBundle.getBundle("android.media.intent.extra.ITEM_STATUS"));
      if (localMediaItemStatus == null)
      {
        processError("ITEM_STATUS is null!", paramBundle);
        return;
      }
      processMediaItemStatus(localMediaItemStatus);
      return;
    }
    processError("Result data contains no status!", paramBundle);
  }

  private void processError(String paramString, Bundle paramBundle)
  {
    StringBuilder localStringBuilder = new StringBuilder().append("Error encountered processing returned bundle: ").append(paramString).append(" Bundle: ");
    String str1 = DebugUtils.bundleToString(paramBundle);
    String str2 = str1;
    logw(str2);
    State localState = State.ERROR;
    setState(localState);
    notifyFailure(false);
  }

  private void processMediaItemStatus(MediaItemStatus paramMediaItemStatus)
  {
    int i = 0;
    long l1 = paramMediaItemStatus.getContentPosition();
    if (l1 > 65535L)
      updatePosition(l1);
    switch (paramMediaItemStatus.getPlaybackState())
    {
    default:
    case 3:
    case 6:
    case 5:
    case 7:
    case 2:
    case 1:
    case 0:
    case 4:
    }
    while (true)
    {
      if (i == 0)
        return;
      if (!isLogVerbose())
        return;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramMediaItemStatus;
      String str1 = DebugUtils.bundleToString(paramMediaItemStatus.getExtras());
      arrayOfObject[1] = str1;
      String str2 = String.format("Processing status status=%s status.extras=%s", arrayOfObject);
      logd(str2);
      return;
      State localState1 = this.mState;
      State localState2 = State.BUFFERING;
      if (localState1 != localState2)
      {
        i = 1;
        State localState3 = State.BUFFERING;
        setState(localState3);
        continue;
        State localState4 = this.mState;
        State localState5 = State.INVALIDATED;
        if (localState4 != localState5)
        {
          i = 1;
          State localState6 = State.INVALIDATED;
          setState(localState6);
          this.mServiceHooks.reportTrackPaused();
          this.mServiceHooks.notifyPlayStateChanged();
          this.mServiceHooks.notifyMediaRouteInvalidated();
          continue;
          State localState7 = this.mState;
          State localState8 = State.CANCELED;
          if (localState7 != localState8)
          {
            i = 1;
            State localState9 = State.CANCELED;
            setState(localState9);
            this.mServiceHooks.reportTrackPaused();
            this.mServiceHooks.notifyPlayStateChanged();
            continue;
            State localState10 = this.mState;
            State localState11 = State.ERROR;
            if (localState10 != localState11)
            {
              i = 1;
              Bundle localBundle1 = paramMediaItemStatus.getExtras();
              if (localBundle1 == null)
              {
                State localState12 = State.ERROR;
                setState(localState12);
                logw("statusExtra=null");
                notifyFailure(false);
              }
              else
              {
                Bundle localBundle2 = localBundle1.getBundle("android.media.status.extra.HTTP_RESPONSE_HEADERS");
                int j = localBundle1.getInt("android.media.status.extra.HTTP_STATUS_CODE");
                ErrorHandlingAction localErrorHandlingAction = handleRemoteHttpError(j, localBundle2);
                int[] arrayOfInt = 7.$SwitchMap$com$google$android$music$cast$RemoteAsyncMediaPlayer$ErrorHandlingAction;
                int k = localErrorHandlingAction.ordinal();
                switch (arrayOfInt[k])
                {
                default:
                  break;
                case 1:
                  State localState13 = State.ERROR;
                  setState(localState13);
                  if (this.mIsCurrent)
                    notifyFailure(false);
                  break;
                case 2:
                  State localState14 = State.ERROR;
                  setState(localState14);
                  if (this.mIsCurrent)
                  {
                    notifyFailure(true);
                    continue;
                    State localState15 = this.mState;
                    State localState16 = State.PAUSED;
                    if (localState15 != localState16)
                    {
                      i = 1;
                      this.mServiceHooks.reportTrackPaused();
                      State localState17 = State.PAUSED;
                      setState(localState17);
                      this.mServiceHooks.notifyPlayStateChanged();
                      continue;
                      State localState18 = this.mState;
                      State localState19 = State.PLAYING;
                      if (localState18 != localState19)
                      {
                        i = 1;
                        this.mTryRefreshCastToken = false;
                        this.mNumberOfRetries = 0;
                        State localState20 = State.PLAYING;
                        setState(localState20);
                        this.mServiceHooks.notifyOpenComplete();
                        this.mServiceHooks.reportTrackPlaying();
                        this.mServiceHooks.notifyPlayStateChanged();
                        continue;
                        State localState21 = this.mState;
                        State localState22 = State.PENDING;
                        if (localState21 != localState22)
                        {
                          i = 1;
                          State localState23 = State.PENDING;
                          setState(localState23);
                          continue;
                          State localState24 = this.mState;
                          State localState25 = State.STOPPED;
                          if (localState24 != localState25)
                          {
                            i = 1;
                            State localState26 = State.STOPPED;
                            setState(localState26);
                            this.mServiceHooks.reportTrackEnded();
                            long l2 = Gservices.getLong(this.mContext.getContentResolver(), "music_remote_player_temp_wake_lock_timeout_ms", 5000L);
                            this.mWakeLock.acquire(l2);
                          }
                        }
                      }
                    }
                  }
                  break;
                }
              }
            }
          }
        }
      }
    }
  }

  private void requestRemotePauseOnMainThread()
  {
    Intent localIntent1 = new Intent("android.media.intent.action.PAUSE");
    Intent localIntent2 = localIntent1.addCategory("android.media.intent.category.REMOTE_PLAYBACK");
    populateSessionId(localIntent1);
    MediaRouter.RouteInfo localRouteInfo = getSelectedRouteOnMainThread();
    if (localRouteInfo == null)
      return;
    if (Log.isLoggable("MusicCast", 3))
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = localIntent1;
      String str1 = DebugUtils.bundleToString(localIntent1.getExtras());
      arrayOfObject[1] = str1;
      String str2 = String.format("Sending intent=%s extras=%s", arrayOfObject);
      logd(str2);
    }
    MediaRouter.ControlRequestCallback local2 = new MediaRouter.ControlRequestCallback()
    {
      public void onError(String paramAnonymousString, Bundle paramAnonymousBundle)
      {
        RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer1 = RemoteAsyncMediaPlayer.this;
        StringBuilder localStringBuilder = new StringBuilder().append("Error encountered requesting remote pause: ").append(paramAnonymousString).append(": ");
        String str1 = DebugUtils.bundleToString(paramAnonymousBundle);
        String str2 = str1;
        localRemoteAsyncMediaPlayer1.logw(str2);
        RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer2 = RemoteAsyncMediaPlayer.this;
        RemoteAsyncMediaPlayer.State localState = RemoteAsyncMediaPlayer.State.ERROR;
        localRemoteAsyncMediaPlayer2.setState(localState);
        RemoteAsyncMediaPlayer.this.notifyFailure(false);
      }

      public void onResult(Bundle paramAnonymousBundle)
      {
        RemoteAsyncMediaPlayer.this.processControlRequestResultBundle(paramAnonymousBundle);
      }
    };
    localRouteInfo.sendControlRequest(localIntent1, local2);
  }

  private void requestRemotePlaybackOnMainThread()
  {
    MediaRouter.RouteInfo localRouteInfo = getSelectedRouteOnMainThread();
    if (localRouteInfo == null)
      return;
    String str1 = localRouteInfo.getId();
    boolean bool1 = this.mCastTokenClient.hasCachedCastToken(str1);
    this.mFetchedCastToken = bool1;
    if (!this.mFetchedCastToken)
    {
      State localState1 = State.WAITING_FOR_CAST_TOKEN;
      setState(localState1);
      Message localMessage1 = this.mHandler.obtainMessage(1);
      boolean bool2 = this.mHandler.sendMessage(localMessage1);
      Message localMessage2 = this.mHandler.obtainMessage(2);
      boolean bool3 = this.mHandler.sendMessageDelayed(localMessage2, 10000L);
      return;
    }
    String str2 = this.mCastTokenClient.getCachedCastToken(str1);
    if (str2 == null)
    {
      logw("Could not fetch cast token from server.");
      notifyFailure(false);
      return;
    }
    if (this.mItemStatusReceiver == null)
    {
      ItemStatusReceiver localItemStatusReceiver1 = new ItemStatusReceiver(null);
      this.mItemStatusReceiver = localItemStatusReceiver1;
      IntentFilter localIntentFilter = new IntentFilter("com.google.android.music.cast.ACTION_STATUS_CHANGED");
      String str3 = this.mItemStatusCategory;
      localIntentFilter.addCategory(str3);
      Context localContext = this.mContext;
      ItemStatusReceiver localItemStatusReceiver2 = this.mItemStatusReceiver;
      Intent localIntent1 = localContext.registerReceiver(localItemStatusReceiver2, localIntentFilter);
    }
    MusicFile localMusicFile;
    while (true)
    {
      localMusicFile = this.mMusicFile;
      if (localMusicFile != null)
        break;
      logw("Missing MusicFile object");
      notifyFailure(false);
      return;
      logw("ItemStatusReceiver already registered!");
    }
    String str4 = this.mUrl;
    Intent localIntent2 = preparePlayIntent(str4, str2, localMusicFile);
    if (Log.isLoggable("MusicCast", 3))
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = localIntent2;
      String str5 = DebugUtils.bundleToString(localIntent2.getExtras());
      arrayOfObject[1] = str5;
      String str6 = String.format("Sending intent=%s extras=%s", arrayOfObject);
      logd(str6);
    }
    State localState2 = State.PLAY_REQUEST_SENT;
    setState(localState2);
    MediaRouter.ControlRequestCallback local1 = new MediaRouter.ControlRequestCallback()
    {
      public void onError(String paramAnonymousString, Bundle paramAnonymousBundle)
      {
        RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer1 = RemoteAsyncMediaPlayer.this;
        StringBuilder localStringBuilder = new StringBuilder().append("Error encountered requesting remote playback: ").append(paramAnonymousString).append(": ");
        String str1 = DebugUtils.bundleToString(paramAnonymousBundle);
        String str2 = str1;
        localRemoteAsyncMediaPlayer1.logw(str2);
        RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer2 = RemoteAsyncMediaPlayer.this;
        RemoteAsyncMediaPlayer.State localState = RemoteAsyncMediaPlayer.State.ERROR;
        localRemoteAsyncMediaPlayer2.setState(localState);
        RemoteAsyncMediaPlayer.this.notifyFailure(false);
      }

      public void onResult(Bundle paramAnonymousBundle)
      {
        RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer1 = RemoteAsyncMediaPlayer.this;
        StringBuilder localStringBuilder = new StringBuilder().append("playIntent:onResult data=");
        String str1 = DebugUtils.bundleToString(paramAnonymousBundle);
        String str2 = str1;
        localRemoteAsyncMediaPlayer1.logd(str2);
        if (paramAnonymousBundle != null)
        {
          RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer2 = RemoteAsyncMediaPlayer.this;
          String str3 = paramAnonymousBundle.getString("android.media.intent.extra.SESSION_ID");
          String str4 = RemoteAsyncMediaPlayer.access$1502(localRemoteAsyncMediaPlayer2, str3);
          RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer3 = RemoteAsyncMediaPlayer.this;
          String str5 = paramAnonymousBundle.getString("android.media.intent.extra.ITEM_ID");
          String str6 = RemoteAsyncMediaPlayer.access$2202(localRemoteAsyncMediaPlayer3, str5);
          LocalDevicePlayback.ServiceHooks localServiceHooks = RemoteAsyncMediaPlayer.this.mServiceHooks;
          String str7 = RemoteAsyncMediaPlayer.this.mSessionId;
          localServiceHooks.setMediaRouteSessionId(str7);
        }
        if (RemoteAsyncMediaPlayer.this.mItemId == null)
        {
          RemoteAsyncMediaPlayer.this.processError("Item ID not initialized!", paramAnonymousBundle);
          return;
        }
        RemoteAsyncMediaPlayer.this.processControlRequestResultBundle(paramAnonymousBundle);
        if (!RemoteAsyncMediaPlayer.this.mPrequeueItems)
          return;
        if (RemoteAsyncMediaPlayer.this.mNextPlayer == null)
          return;
        Message localMessage = RemoteAsyncMediaPlayer.this.mHandler.obtainMessage(10);
        boolean bool = RemoteAsyncMediaPlayer.this.mHandler.sendMessage(localMessage);
      }
    };
    localRouteInfo.sendControlRequest(localIntent2, local1);
    this.mIsPlaybackRequested = true;
  }

  private void requestRemoteResumeOnMainThread()
  {
    Intent localIntent1 = new Intent("android.media.intent.action.RESUME");
    Intent localIntent2 = localIntent1.addCategory("android.media.intent.category.REMOTE_PLAYBACK");
    populateSessionId(localIntent1);
    MediaRouter.RouteInfo localRouteInfo = getSelectedRouteOnMainThread();
    if (localRouteInfo == null)
      return;
    if (isLogVerbose())
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = localIntent1;
      String str1 = DebugUtils.bundleToString(localIntent1.getExtras());
      arrayOfObject[1] = str1;
      String str2 = String.format("Sending intent=%s extras=%s", arrayOfObject);
      logd(str2);
    }
    MediaRouter.ControlRequestCallback local3 = new MediaRouter.ControlRequestCallback()
    {
      public void onError(String paramAnonymousString, Bundle paramAnonymousBundle)
      {
        RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer1 = RemoteAsyncMediaPlayer.this;
        StringBuilder localStringBuilder = new StringBuilder().append("Error encountered requesting remote resume: ").append(paramAnonymousString).append(": ");
        String str1 = DebugUtils.bundleToString(paramAnonymousBundle);
        String str2 = str1;
        localRemoteAsyncMediaPlayer1.logw(str2);
        RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer2 = RemoteAsyncMediaPlayer.this;
        RemoteAsyncMediaPlayer.State localState = RemoteAsyncMediaPlayer.State.ERROR;
        localRemoteAsyncMediaPlayer2.setState(localState);
        RemoteAsyncMediaPlayer.this.notifyFailure(false);
      }

      public void onResult(Bundle paramAnonymousBundle)
      {
        RemoteAsyncMediaPlayer.this.processControlRequestResultBundle(paramAnonymousBundle);
      }
    };
    localRouteInfo.sendControlRequest(localIntent1, local3);
  }

  private void requestRemoteSeekOnMainThread()
  {
    Intent localIntent1 = new Intent("android.media.intent.action.SEEK");
    Intent localIntent2 = localIntent1.addCategory("android.media.intent.category.REMOTE_PLAYBACK");
    populateSessionId(localIntent1);
    String str1 = this.mItemId;
    Intent localIntent3 = localIntent1.putExtra("android.media.intent.extra.ITEM_ID", str1);
    long l = this.mKnownPositionMillis;
    Intent localIntent4 = localIntent1.putExtra("android.media.intent.extra.ITEM_POSITION", l);
    MediaRouter.RouteInfo localRouteInfo = getSelectedRouteOnMainThread();
    if (localRouteInfo == null)
      return;
    if (isLogVerbose())
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = localIntent1;
      String str2 = DebugUtils.bundleToString(localIntent1.getExtras());
      arrayOfObject[1] = str2;
      String str3 = String.format("Sending intent=%s extras=%s", arrayOfObject);
      logd(str3);
    }
    MediaRouter.ControlRequestCallback local4 = new MediaRouter.ControlRequestCallback()
    {
      public void onError(String paramAnonymousString, Bundle paramAnonymousBundle)
      {
        RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer1 = RemoteAsyncMediaPlayer.this;
        StringBuilder localStringBuilder = new StringBuilder().append("Error encountered requesting remote seek: ").append(paramAnonymousString).append(": ");
        String str1 = DebugUtils.bundleToString(paramAnonymousBundle);
        String str2 = str1;
        localRemoteAsyncMediaPlayer1.logd(str2);
        RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer2 = RemoteAsyncMediaPlayer.this;
        RemoteAsyncMediaPlayer.State localState = RemoteAsyncMediaPlayer.State.ERROR;
        localRemoteAsyncMediaPlayer2.setState(localState);
        RemoteAsyncMediaPlayer.this.notifyFailure(false);
      }

      public void onResult(Bundle paramAnonymousBundle)
      {
        RemoteAsyncMediaPlayer.this.processControlRequestResultBundle(paramAnonymousBundle);
      }
    };
    localRouteInfo.sendControlRequest(localIntent1, local4);
  }

  private void requestRemoteSetVolumeOnMainThread(float paramFloat)
  {
    MediaRouter.RouteInfo localRouteInfo = getSelectedRouteOnMainThread();
    if (localRouteInfo == null)
      return;
    if (localRouteInfo.getVolumeHandling() != 1)
      return;
    int i = localRouteInfo.getVolumeMax();
    int j = (int)paramFloat * i;
    getSelectedRouteOnMainThread().requestSetVolume(j);
  }

  private void requestRemoveOnMainThread()
  {
    Intent localIntent1 = new Intent("android.media.intent.action.REMOVE");
    Intent localIntent2 = localIntent1.addCategory("android.media.intent.category.REMOTE_PLAYBACK");
    populateSessionId(localIntent1);
    String str1 = this.mItemId;
    Intent localIntent3 = localIntent1.putExtra("android.media.intent.extra.ITEM_ID", str1);
    MediaRouter.RouteInfo localRouteInfo = getSelectedRouteOnMainThread();
    if (localRouteInfo == null)
      return;
    if (Log.isLoggable("MusicCast", 3))
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = localIntent1;
      String str2 = DebugUtils.bundleToString(localIntent1.getExtras());
      arrayOfObject[1] = str2;
      String str3 = String.format("Sending intent=%s extras=%s", arrayOfObject);
      logd(str3);
    }
    MediaRouter.ControlRequestCallback local5 = new MediaRouter.ControlRequestCallback()
    {
      public void onError(String paramAnonymousString, Bundle paramAnonymousBundle)
      {
        RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer1 = RemoteAsyncMediaPlayer.this;
        StringBuilder localStringBuilder = new StringBuilder().append("Error encountered requesting remote remove: ").append(paramAnonymousString).append(": ");
        String str1 = DebugUtils.bundleToString(paramAnonymousBundle);
        String str2 = str1;
        localRemoteAsyncMediaPlayer1.logw(str2);
        RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer2 = RemoteAsyncMediaPlayer.this;
        RemoteAsyncMediaPlayer.State localState = RemoteAsyncMediaPlayer.State.ERROR;
        localRemoteAsyncMediaPlayer2.setState(localState);
        RemoteAsyncMediaPlayer.this.notifyFailure(false);
      }

      public void onResult(Bundle paramAnonymousBundle)
      {
        boolean bool = RemoteAsyncMediaPlayer.access$2702(RemoteAsyncMediaPlayer.this, false);
        RemoteAsyncMediaPlayer.this.processControlRequestResultBundle(paramAnonymousBundle);
      }
    };
    localRouteInfo.sendControlRequest(localIntent1, local5);
  }

  private void retryPlaybackRequest(long paramLong)
  {
    clear(false);
    Message localMessage = Message.obtain(this.mHandler, 3);
    boolean bool = this.mHandler.sendMessageDelayed(localMessage, paramLong);
  }

  private void setSessionId(String paramString)
  {
    if (this.mIsPlaybackRequested)
    {
      String str1 = "Ignoring sessionId " + paramString + " playback already requested";
      logw(str1);
      return;
    }
    this.mSessionId = paramString;
    if (!this.mPrequeueItems)
      return;
    if (isLogVerbose())
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Requesting queued playback for ");
      String str2 = this.mUrl;
      String str3 = str2;
      logv(str3);
    }
    Message localMessage = Message.obtain(this.mHandler, 3);
    boolean bool = this.mHandler.sendMessage(localMessage);
  }

  private void setState(State paramState)
  {
    if (isLogVerbose())
    {
      String str = "setState: state=" + paramState;
      logv(str);
    }
    while (true)
    {
      synchronized (this.mLock)
      {
        this.mState = paramState;
        int[] arrayOfInt = 7.$SwitchMap$com$google$android$music$cast$RemoteAsyncMediaPlayer$State;
        int i = paramState.ordinal();
        switch (arrayOfInt[i])
        {
        default:
          return;
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
          this.mPositionDeltaStopWatch.pause();
        case 7:
        }
      }
      this.mPositionDeltaStopWatch.start();
    }
  }

  private void updatePosition(long paramLong)
  {
    synchronized (this.mLock)
    {
      this.mKnownPositionMillis = paramLong;
      this.mPositionDeltaStopWatch.reset();
      return;
    }
  }

  public long duration()
  {
    MusicFile localMusicFile = this.mMusicFile;
    if (localMusicFile != null);
    for (long l = localMusicFile.getDurationInMilliSec(); ; l = 65535L)
      return l;
  }

  public int getAudioSessionId()
  {
    return this.mAudioSessionId;
  }

  public int getErrorType()
  {
    return this.mDownloadError;
  }

  public String getRemoteSongId()
  {
    MusicFile localMusicFile = this.mMusicFile;
    if (localMusicFile != null);
    for (String str = localMusicFile.getSourceId(); ; str = null)
      return str;
  }

  public boolean isInErrorState()
  {
    return this.mState.isError();
  }

  public boolean isInitialized()
  {
    return this.mState.isInitialized();
  }

  public boolean isPlaying()
  {
    return this.mState.isPlaying();
  }

  public boolean isPreparing()
  {
    return this.mState.isWaitingForPlayback();
  }

  public boolean isRenderingAudioLocally()
  {
    return false;
  }

  public boolean isStreaming()
  {
    return this.mState.isPlaying();
  }

  public void pause()
  {
    if (((!this.mState.canBePlayed()) && (!this.mState.isPlaying())) || (this.mItemId == null))
      logw("Not playable -- attempting to pause anyway.");
    Handler localHandler = this.mHandler;
    String str = this.mUrl;
    Message localMessage = Message.obtain(localHandler, 4, str);
    boolean bool = this.mHandler.sendMessage(localMessage);
  }

  public long position()
  {
    long l1 = this.mKnownPositionMillis;
    long l2 = this.mPositionDeltaStopWatch.getTime();
    long l3 = l1 + l2;
    MusicFile localMusicFile = this.mMusicFile;
    if (localMusicFile != null)
    {
      long l4 = localMusicFile.getDurationInMilliSec();
      l3 = Math.min(l3, l4);
    }
    return l3;
  }

  public void release()
  {
    if (Log.isLoggable("MusicCast", 3))
      logd("Releasing");
    if ((this.mIsPlaybackRequested) && (!this.mIsCurrent))
    {
      Message localMessage = Message.obtain(this.mHandler, 11);
      boolean bool = this.mHandler.sendMessage(localMessage);
    }
    State localState = State.CANCELED;
    setState(localState);
    if (this.mItemStatusReceiver != null)
    {
      Context localContext = this.mContext;
      ItemStatusReceiver localItemStatusReceiver = this.mItemStatusReceiver;
      localContext.unregisterReceiver(localItemStatusReceiver);
      this.mItemStatusReceiver = null;
    }
    notifyIfSongPlayed();
    MusicPreferences.releaseMusicPreferences(this);
  }

  public long seek(long paramLong)
  {
    if (isLogVerbose())
    {
      String str = "Seek to position " + paramLong + " requested.";
      logv(str);
    }
    if ((this.mState.canBePlayed()) || (this.mState.isPlaying()))
    {
      updatePosition(paramLong);
      if (this.mState.isPlaying())
      {
        State localState = State.PAUSED;
        setState(localState);
        this.mServiceHooks.notifyPlayStateChanged();
      }
      if (this.mIsPlaybackRequested)
      {
        Message localMessage = Message.obtain(this.mHandler, 8);
        boolean bool = this.mHandler.sendMessage(localMessage);
      }
    }
    return position();
  }

  public void setAsCurrentPlayer()
  {
    if (isLogVerbose())
      logv("Setting this player as the current one.");
    this.mIsCurrent = true;
  }

  public void setAudioSessionId(int paramInt)
  {
    this.mAudioSessionId = paramInt;
  }

  public void setDataSource(ContentIdentifier paramContentIdentifier, long paramLong1, long paramLong2, boolean paramBoolean1, boolean paramBoolean2, MusicFile paramMusicFile, AsyncMediaPlayer.AsyncCommandCallback paramAsyncCommandCallback)
  {
    if ((paramContentIdentifier != null) && (paramContentIdentifier.isSharedDomain()))
    {
      this.mDownloadError = 15;
      String str1 = "Shared track is not playable on remote device: songId=" + paramContentIdentifier;
      logd(str1);
      paramAsyncCommandCallback.onFailure(false);
      return;
    }
    if ((paramContentIdentifier == null) || (paramMusicFile == null))
    {
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = paramContentIdentifier;
      arrayOfObject1[1] = paramMusicFile;
      String str2 = String.format("Invalid arguments: songId=%s musicFile=%s", arrayOfObject1);
      logw(str2);
      paramAsyncCommandCallback.onFailure(true);
      return;
    }
    if ((paramContentIdentifier.isCacheable()) && (!paramMusicFile.getIsCloudFile()))
    {
      this.mDownloadError = 14;
      String str3 = "Sideloaded track is not playable on remote device: songId=" + paramContentIdentifier;
      logd(str3);
      paramAsyncCommandCallback.onFailure(false);
      return;
    }
    if ((!paramContentIdentifier.isCacheable()) || (!paramMusicFile.getIsCloudFile()))
    {
      String str4 = "Track is not playable on remote device: songId=" + paramContentIdentifier;
      logw(str4);
      paramAsyncCommandCallback.onFailure(true);
      return;
    }
    this.mCurrentSongId = paramContentIdentifier;
    updatePosition(paramLong1);
    this.mCallback = paramAsyncCommandCallback;
    this.mFromUserAction = paramBoolean2;
    this.mIsCurrent = paramBoolean1;
    Store localStore = Store.getInstance(this.mContext);
    SQLiteDatabase localSQLiteDatabase = localStore.beginRead();
    MusicFile localMusicFile1 = new MusicFile();
    this.mMusicFile = localMusicFile1;
    try
    {
      MusicFile localMusicFile2 = this.mMusicFile;
      long l = paramMusicFile.getLocalId();
      localMusicFile2.load(localSQLiteDatabase, l);
      localStore.endRead(localSQLiteDatabase);
      MusicFile localMusicFile3 = this.mMusicFile;
      String str5 = generateMplayUrl(paramBoolean2, localMusicFile3);
      this.mUrl = str5;
      if (this.mUrl == null)
      {
        String str6 = "Failed to generate URL for songId=" + paramContentIdentifier;
        logw(str6);
        paramAsyncCommandCallback.onFailure(true);
        return;
      }
    }
    catch (DataNotFoundException localDataNotFoundException)
    {
      String str7 = "Failed to load MusicFile for songId=" + paramContentIdentifier;
      logw(str7, localDataNotFoundException);
      paramAsyncCommandCallback.onFailure(true);
      return;
    }
    finally
    {
      localStore.endRead(localSQLiteDatabase);
    }
    State localState = State.DATA_SOURCE_SET;
    setState(localState);
    if (isLogVerbose())
    {
      Object[] arrayOfObject2 = new Object[3];
      String str8 = this.mUrl;
      arrayOfObject2[0] = str8;
      Boolean localBoolean = Boolean.valueOf(this.mPrequeueItems);
      arrayOfObject2[1] = localBoolean;
      String str9 = this.mSessionId;
      arrayOfObject2[2] = str9;
      String str10 = String.format("Setting data source.  Will play %s mPrequeueItems=%s mSessionId=%s", arrayOfObject2);
      logv(str10);
    }
    if ((this.mPrequeueItems) && (this.mSessionId != null))
    {
      if (isLogVerbose())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Requesting queued playback for ");
        String str11 = this.mUrl;
        String str12 = str11;
        logv(str12);
      }
      Message localMessage = Message.obtain(this.mHandler, 3);
      boolean bool = this.mHandler.sendMessage(localMessage);
    }
    paramAsyncCommandCallback.onSuccess();
  }

  public void setNextPlayer(AsyncMediaPlayer paramAsyncMediaPlayer)
  {
    if (!(paramAsyncMediaPlayer instanceof RemoteAsyncMediaPlayer))
      return;
    RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer = (RemoteAsyncMediaPlayer)paramAsyncMediaPlayer;
    this.mNextPlayer = localRemoteAsyncMediaPlayer;
    if (isLogVerbose())
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Next player set [");
      String str1 = this.mNextPlayer.mItemStatusCategory;
      String str2 = str1 + "]";
      logv(str2);
    }
    if (!this.mPrequeueItems)
      return;
    if (this.mSessionId == null)
      return;
    Message localMessage = this.mHandler.obtainMessage(10);
    boolean bool = this.mHandler.sendMessage(localMessage);
  }

  public void setVolume(float paramFloat)
  {
    Handler localHandler = this.mHandler;
    Float localFloat = Float.valueOf(paramFloat);
    Message localMessage = localHandler.obtainMessage(9, localFloat);
    boolean bool = this.mHandler.sendMessage(localMessage);
  }

  public void start()
  {
    if (this.mState.isPlaying())
    {
      logw("Ignoring request to start playback -- already playing.");
      return;
    }
    if (this.mState.isPausedOrStopped())
    {
      Message localMessage1 = this.mHandler.obtainMessage(5);
      boolean bool1 = this.mHandler.sendMessage(localMessage1);
      return;
    }
    if (!this.mState.canBePlayed())
    {
      logw("Attempting to start canceled or unplayable item.Clearing state and starting over.");
      State localState1 = State.NONE;
      setState(localState1);
      this.mItemStatusReceiver = null;
      this.mSessionId = null;
      this.mItemId = null;
      updatePosition(0L);
    }
    this.mServiceHooks.notifyOpenStarted();
    if (this.mMusicFile == null)
    {
      logw("No data source set!");
      State localState2 = State.ERROR;
      setState(localState2);
      notifyFailure(false);
      return;
    }
    if (this.mUrl == null)
    {
      logw("Could not generate url for remote playback.");
      State localState3 = State.ERROR;
      setState(localState3);
      notifyFailure(false);
      return;
    }
    if (this.mItemId == null)
    {
      if (isLogVerbose())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Requesting remote playback of ");
        String str1 = this.mUrl;
        String str2 = str1;
        logd(str2);
      }
      Message localMessage2 = Message.obtain(this.mHandler, 3);
      boolean bool2 = this.mHandler.sendMessage(localMessage2);
      return;
    }
    if (this.mState.isPlaying())
    {
      logd("start() request ignored -- already playing.");
      return;
    }
    logd("start() request ignored -- already queued.");
  }

  public void stop()
  {
    if (Log.isLoggable("MusicCast", 3))
      logd("Requesting remote stop");
    if (this.mItemId == null)
    {
      logw("Cannot stop -- have no queue id or have no item id.");
      return;
    }
    if ((!this.mState.canBePlayed()) && (!this.mState.isPlaying()))
    {
      logw("Cannot stop -- not playable.");
      return;
    }
    State localState = State.CANCELED;
    setState(localState);
    Handler localHandler = this.mHandler;
    String str = this.mUrl;
    Message localMessage = Message.obtain(localHandler, 6, str);
    boolean bool = this.mHandler.sendMessage(localMessage);
  }

  private class ItemStatusReceiver extends BroadcastReceiver
  {
    private ItemStatusReceiver()
    {
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      if (RemoteAsyncMediaPlayer.this.isLogVerbose())
      {
        RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer = RemoteAsyncMediaPlayer.this;
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = paramIntent;
        String str1 = DebugUtils.bundleToString(paramIntent.getExtras());
        arrayOfObject[1] = str1;
        String str2 = String.format("onReceive intent=%s extras=%s", arrayOfObject);
        localRemoteAsyncMediaPlayer.logv(str2);
      }
      if (paramIntent.hasExtra("android.media.intent.extra.ITEM_STATUS"))
      {
        MediaItemStatus localMediaItemStatus = MediaItemStatus.fromBundle(paramIntent.getBundleExtra("android.media.intent.extra.ITEM_STATUS"));
        RemoteAsyncMediaPlayer.this.processMediaItemStatus(localMediaItemStatus);
        return;
      }
      RemoteAsyncMediaPlayer.this.logw("Received update with no status!");
    }
  }

  private class MyHandler extends Handler
  {
    public MyHandler()
    {
      super();
    }

    public void handleMessage(Message paramMessage)
    {
      if (RemoteAsyncMediaPlayer.this.isLogVerbose())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("handleMessage: what=");
        int i = paramMessage.what;
        String str1 = i;
        Log.v("MusicCast", str1);
      }
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
        LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            CastTokenClient localCastTokenClient = RemoteAsyncMediaPlayer.this.mCastTokenClient;
            String str1 = RemoteAsyncMediaPlayer.this.mRoute.getProvider().getPackageName();
            String str2 = RemoteAsyncMediaPlayer.this.mRoute.getId();
            String str3 = localCastTokenClient.getCastToken(str1, str2);
            boolean bool1 = RemoteAsyncMediaPlayer.access$302(RemoteAsyncMediaPlayer.this, true);
            RemoteAsyncMediaPlayer.State localState1 = RemoteAsyncMediaPlayer.this.mState;
            RemoteAsyncMediaPlayer.State localState2 = RemoteAsyncMediaPlayer.State.WAITING_FOR_CAST_TOKEN;
            if (localState1 != localState2)
              return;
            RemoteAsyncMediaPlayer.this.mHandler.removeMessages(2);
            Message localMessage = RemoteAsyncMediaPlayer.this.mHandler.obtainMessage(3);
            boolean bool2 = RemoteAsyncMediaPlayer.this.mHandler.sendMessage(localMessage);
          }
        };
        AsyncWorkers.runAsync(localLoggableHandler, local1);
        return;
      case 2:
        RemoteAsyncMediaPlayer.this.logw("Timed out waiting for cast token.  Aborting playback.");
        RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer1 = RemoteAsyncMediaPlayer.this;
        RemoteAsyncMediaPlayer.State localState = RemoteAsyncMediaPlayer.State.ERROR;
        localRemoteAsyncMediaPlayer1.setState(localState);
        RemoteAsyncMediaPlayer.this.notifyFailure(false);
        return;
      case 3:
        RemoteAsyncMediaPlayer.this.requestRemotePlaybackOnMainThread();
        return;
      case 4:
        RemoteAsyncMediaPlayer.this.requestRemotePauseOnMainThread();
        return;
      case 5:
        RemoteAsyncMediaPlayer.this.requestRemoteResumeOnMainThread();
        return;
      case 6:
        RemoteAsyncMediaPlayer.this.requestRemotePauseOnMainThread();
        return;
      case 7:
        RemoteAsyncMediaPlayer.this.requestRemotePauseOnMainThread();
        return;
      case 8:
        RemoteAsyncMediaPlayer.this.requestRemoteSeekOnMainThread();
        return;
      case 9:
        RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer2 = RemoteAsyncMediaPlayer.this;
        float f = ((Float)paramMessage.obj).floatValue();
        localRemoteAsyncMediaPlayer2.requestRemoteSetVolumeOnMainThread(f);
        return;
      case 10:
        if (RemoteAsyncMediaPlayer.this.mNextPlayer == null)
          return;
        RemoteAsyncMediaPlayer localRemoteAsyncMediaPlayer3 = RemoteAsyncMediaPlayer.this.mNextPlayer;
        String str2 = RemoteAsyncMediaPlayer.this.mSessionId;
        localRemoteAsyncMediaPlayer3.setSessionId(str2);
        return;
      case 11:
      }
      RemoteAsyncMediaPlayer.this.requestRemoveOnMainThread();
    }
  }

  private static enum ErrorHandlingAction
  {
    static
    {
      ErrorHandlingAction[] arrayOfErrorHandlingAction = new ErrorHandlingAction[3];
      ErrorHandlingAction localErrorHandlingAction1 = REPORT_ERROR;
      arrayOfErrorHandlingAction[0] = localErrorHandlingAction1;
      ErrorHandlingAction localErrorHandlingAction2 = REPORT_ERROR_AND_SKIP;
      arrayOfErrorHandlingAction[1] = localErrorHandlingAction2;
      ErrorHandlingAction localErrorHandlingAction3 = RETRY;
      arrayOfErrorHandlingAction[2] = localErrorHandlingAction3;
    }
  }

  public static enum State
  {
    static
    {
      DATA_SOURCE_SET = new State("DATA_SOURCE_SET", 1);
      WAITING_FOR_CAST_TOKEN = new State("WAITING_FOR_CAST_TOKEN", 2);
      PLAY_REQUEST_SENT = new State("PLAY_REQUEST_SENT", 3);
      PENDING = new State("PENDING", 4);
      BUFFERING = new State("BUFFERING", 5);
      PLAYING = new State("PLAYING", 6);
      PAUSED = new State("PAUSED", 7);
      STOPPED = new State("STOPPED", 8);
      CANCELED = new State("CANCELED", 9);
      INVALIDATED = new State("INVALIDATED", 10);
      ERROR = new State("ERROR", 11);
      State[] arrayOfState = new State[12];
      State localState1 = NONE;
      arrayOfState[0] = localState1;
      State localState2 = DATA_SOURCE_SET;
      arrayOfState[1] = localState2;
      State localState3 = WAITING_FOR_CAST_TOKEN;
      arrayOfState[2] = localState3;
      State localState4 = PLAY_REQUEST_SENT;
      arrayOfState[3] = localState4;
      State localState5 = PENDING;
      arrayOfState[4] = localState5;
      State localState6 = BUFFERING;
      arrayOfState[5] = localState6;
      State localState7 = PLAYING;
      arrayOfState[6] = localState7;
      State localState8 = PAUSED;
      arrayOfState[7] = localState8;
      State localState9 = STOPPED;
      arrayOfState[8] = localState9;
      State localState10 = CANCELED;
      arrayOfState[9] = localState10;
      State localState11 = INVALIDATED;
      arrayOfState[10] = localState11;
      State localState12 = ERROR;
      arrayOfState[11] = localState12;
    }

    public boolean canBePlayed()
    {
      if ((isWaitingForPlayback()) || (isPausedOrStopped()));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean isError()
    {
      State localState = ERROR;
      if (this == localState);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean isInitialized()
    {
      if ((isWaitingForPlayback()) || (isPlaying()) || (isPausedOrStopped()));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean isPausedOrStopped()
    {
      State localState1 = PAUSED;
      if (this != localState1)
      {
        State localState2 = STOPPED;
        if (this != localState2)
          break label22;
      }
      label22: for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean isPlaying()
    {
      State localState = PLAYING;
      if (this == localState);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean isWaitingForPlayback()
    {
      State localState1 = DATA_SOURCE_SET;
      if (this != localState1)
      {
        State localState2 = WAITING_FOR_CAST_TOKEN;
        if (this != localState2)
        {
          State localState3 = PLAY_REQUEST_SENT;
          if (this != localState3)
          {
            State localState4 = PENDING;
            if (this != localState4)
            {
              State localState5 = BUFFERING;
              if (this != localState5)
                break label55;
            }
          }
        }
      }
      label55: for (boolean bool = true; ; bool = false)
        return bool;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cast.RemoteAsyncMediaPlayer
 * JD-Core Version:    0.6.2
 */