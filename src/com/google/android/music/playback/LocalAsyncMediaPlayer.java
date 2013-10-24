package com.google.android.music.playback;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.provider.MediaStore.Audio.Media;
import android.util.EventLog;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.DownloadErrorType;
import com.google.android.music.download.DownloadProgress;
import com.google.android.music.download.DownloadState.State;
import com.google.android.music.download.IDownloadProgressListener;
import com.google.android.music.download.IDownloadProgressListener.Stub;
import com.google.android.music.download.cache.CacheUtils;
import com.google.android.music.download.cache.OutOfSpaceException;
import com.google.android.music.download.stream.DownloadRequestException;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MediaStoreImportService;
import com.google.android.music.store.MusicFile;
import com.google.android.music.store.Store;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.PlaybackUtils;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class LocalAsyncMediaPlayer extends LoggableHandler
  implements AsyncMediaPlayer
{
  private final boolean LOGV;
  private MediaPlayer.OnCompletionListener completionListener;
  private MediaPlayer.OnErrorListener errorListener;
  private MediaPlayer.OnInfoListener infoListener;
  private final boolean isPreGingerbread;
  private AsyncMediaPlayer.AsyncCommandCallback mCallback;
  private final Context mContext;
  private ContentIdentifier mCurrentSong;
  private long mCurrentSongSourceAccount;
  private String mCurrentSongSourceId;
  private String mCurrentUrl;
  private float mDownloadCompletedPercent;
  private int mDownloadError;
  private final IDownloadProgressListener mDownloadProgressListener;
  private DownloadState.State mDownloadState;
  private long mDurationMs;
  private final MusicEventLogger mEventLogger;
  private boolean mHasSentPlayEvent;
  private long mHttpSeek;
  private volatile boolean mIsCurrentPlayer;
  private CompatMediaPlayer mMediaPlayer;
  private MusicFile mMusicFile;
  private String mPlayEventKey;
  private String mPlayEventValue;
  private long mPositionMs;
  private final MusicPreferences mPrefs;
  private final LocalDevicePlayback.ServiceHooks mServiceHooks;
  private int mStatus;
  private StopWatch mStopWatch;
  private final PowerManager.WakeLock mWakeLock;
  private MediaPlayer.OnSeekCompleteListener seekListener;

  public LocalAsyncMediaPlayer(Context paramContext, LocalDevicePlayback.ServiceHooks paramServiceHooks)
  {
    super("LocalAsyncMediaPlayer", 5);
    boolean bool1 = DebugUtils.isLoggable(DebugUtils.MusicTag.PLAYBACK_SERVICE);
    this.LOGV = bool1;
    this.mDurationMs = 0L;
    this.mPositionMs = 0L;
    this.mHttpSeek = 0L;
    this.mIsCurrentPlayer = true;
    this.mHasSentPlayEvent = false;
    this.mStatus = 0;
    this.mDownloadCompletedPercent = 0.0F;
    DownloadState.State localState = DownloadState.State.NOT_STARTED;
    this.mDownloadState = localState;
    this.mDownloadError = 1;
    MediaPlayer.OnCompletionListener local2 = new MediaPlayer.OnCompletionListener()
    {
      public void onCompletion(MediaPlayer paramAnonymousMediaPlayer)
      {
        MusicUtils.debugLog("AsyncMediaPlayer OnCompletion " + this);
        LocalAsyncMediaPlayer.this.removeStatus(4);
        LocalAsyncMediaPlayer.this.mStopWatch.pause();
        if (!LocalAsyncMediaPlayer.this.mIsCurrentPlayer)
          return;
        LocalAsyncMediaPlayer.this.mServiceHooks.reportTrackEnded();
      }
    };
    this.completionListener = local2;
    MediaPlayer.OnErrorListener local3 = new MediaPlayer.OnErrorListener()
    {
      public boolean onError(MediaPlayer paramAnonymousMediaPlayer, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        boolean bool = true;
        StringBuilder localStringBuilder = new StringBuilder().append("AsyncMediaPlayer(");
        ContentIdentifier localContentIdentifier = LocalAsyncMediaPlayer.this.mCurrentSong;
        MusicUtils.debugLog(localContentIdentifier + ") OnError " + this + ":" + paramAnonymousInt1 + "," + paramAnonymousInt2);
        if (LocalAsyncMediaPlayer.this.mIsCurrentPlayer)
        {
          LocalAsyncMediaPlayer.this.mServiceHooks.notifyOpenComplete();
          if (LocalAsyncMediaPlayer.this.isPlaying())
            LocalAsyncMediaPlayer.this.notifyFailure(false);
        }
        LocalAsyncMediaPlayer.this.removeStatus(14);
        int i = LocalAsyncMediaPlayer.this.getAudioSessionId();
        LocalAsyncMediaPlayer.this.mMediaPlayer.release();
        LocalAsyncMediaPlayer localLocalAsyncMediaPlayer = LocalAsyncMediaPlayer.this;
        CompatMediaPlayer localCompatMediaPlayer1 = new CompatMediaPlayer();
        CompatMediaPlayer localCompatMediaPlayer2 = LocalAsyncMediaPlayer.access$002(localLocalAsyncMediaPlayer, localCompatMediaPlayer1);
        CompatMediaPlayer localCompatMediaPlayer3 = LocalAsyncMediaPlayer.this.mMediaPlayer;
        Context localContext = LocalAsyncMediaPlayer.this.mContext;
        localCompatMediaPlayer3.setWakeMode(localContext, 1);
        LocalAsyncMediaPlayer.this.setAudioSessionId(i);
        switch (paramAnonymousInt1)
        {
        default:
          String str = "Error: " + paramAnonymousInt1 + "," + paramAnonymousInt2;
          Log.d("MultiPlayer", str);
          bool = false;
        case 100:
        }
        return bool;
      }
    };
    this.errorListener = local3;
    MediaPlayer.OnSeekCompleteListener local4 = new MediaPlayer.OnSeekCompleteListener()
    {
      public void onSeekComplete(MediaPlayer paramAnonymousMediaPlayer)
      {
        LocalAsyncMediaPlayer localLocalAsyncMediaPlayer = LocalAsyncMediaPlayer.this;
        long l1 = LocalAsyncMediaPlayer.this.mMediaPlayer.getCurrentPosition();
        long l2 = LocalAsyncMediaPlayer.access$902(localLocalAsyncMediaPlayer, l1);
        LocalAsyncMediaPlayer.this.mStopWatch.reset();
      }
    };
    this.seekListener = local4;
    MediaPlayer.OnInfoListener local5 = new MediaPlayer.OnInfoListener()
    {
      public boolean onInfo(MediaPlayer paramAnonymousMediaPlayer, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        boolean bool = true;
        switch (paramAnonymousInt1)
        {
        default:
          bool = false;
        case 701:
        case 702:
        }
        while (true)
        {
          return bool;
          LocalAsyncMediaPlayer.this.onWaitForBuffer();
          continue;
          LocalAsyncMediaPlayer.this.onResumeFromBuffer();
        }
      }
    };
    this.infoListener = local5;
    IDownloadProgressListener.Stub local6 = new IDownloadProgressListener.Stub()
    {
      public void onDownloadProgress(DownloadProgress paramAnonymousDownloadProgress)
        throws RemoteException
      {
        if (LocalAsyncMediaPlayer.this.LOGV)
        {
          String str = paramAnonymousDownloadProgress.toString();
          Log.d("LocalAsyncMediaPlayer", str);
        }
        LocalAsyncMediaPlayer.this.mServiceHooks.onDownloadProgress(paramAnonymousDownloadProgress);
        if (LocalAsyncMediaPlayer.this.mCurrentSong == null)
          return;
        ContentIdentifier localContentIdentifier1 = LocalAsyncMediaPlayer.this.mCurrentSong;
        ContentIdentifier localContentIdentifier2 = paramAnonymousDownloadProgress.getId();
        if (!localContentIdentifier1.equals(localContentIdentifier2))
          return;
        LocalAsyncMediaPlayer localLocalAsyncMediaPlayer1 = LocalAsyncMediaPlayer.this;
        int i = paramAnonymousDownloadProgress.getError();
        int j = LocalAsyncMediaPlayer.access$1302(localLocalAsyncMediaPlayer1, i);
        LocalAsyncMediaPlayer localLocalAsyncMediaPlayer2 = LocalAsyncMediaPlayer.this;
        DownloadState.State localState1 = paramAnonymousDownloadProgress.getState();
        DownloadState.State localState2 = LocalAsyncMediaPlayer.access$1402(localLocalAsyncMediaPlayer2, localState1);
        LocalAsyncMediaPlayer.this.handleStreamingChanges();
        LocalAsyncMediaPlayer localLocalAsyncMediaPlayer3 = LocalAsyncMediaPlayer.this;
        float f1 = (float)paramAnonymousDownloadProgress.getCompletedBytes();
        float f2 = (float)paramAnonymousDownloadProgress.getDownloadByteLength();
        float f3 = f1 / f2;
        float f4 = LocalAsyncMediaPlayer.access$1602(localLocalAsyncMediaPlayer3, f3);
        if (LocalAsyncMediaPlayer.this.isPreGingerbread)
        {
          LocalAsyncMediaPlayer.this.updateDownloadStatus(0L);
          return;
        }
        DownloadState.State localState3 = paramAnonymousDownloadProgress.getState();
        DownloadState.State localState4 = DownloadState.State.FAILED;
        if (localState3 != localState4)
          return;
        LocalAsyncMediaPlayer.this.updateDownloadStatus(0L);
      }
    };
    this.mDownloadProgressListener = local6;
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(paramContext);
    this.mEventLogger = localMusicEventLogger;
    this.mContext = paramContext;
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, this);
    this.mPrefs = localMusicPreferences;
    this.mServiceHooks = paramServiceHooks;
    StopWatch localStopWatch = new StopWatch();
    this.mStopWatch = localStopWatch;
    boolean bool2 = MusicPreferences.isPreGingerbread();
    this.isPreGingerbread = bool2;
    PowerManager localPowerManager = (PowerManager)paramContext.getSystemService("power");
    String str = getClass().getName();
    PowerManager.WakeLock localWakeLock = localPowerManager.newWakeLock(1, str);
    this.mWakeLock = localWakeLock;
    this.mWakeLock.setReferenceCounted(false);
    final Object localObject1 = new Object();
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        LocalAsyncMediaPlayer localLocalAsyncMediaPlayer = LocalAsyncMediaPlayer.this;
        CompatMediaPlayer localCompatMediaPlayer1 = new CompatMediaPlayer();
        CompatMediaPlayer localCompatMediaPlayer2 = LocalAsyncMediaPlayer.access$002(localLocalAsyncMediaPlayer, localCompatMediaPlayer1);
        synchronized (localObject1)
        {
          localObject1.notifyAll();
          MessageQueue localMessageQueue = Looper.myQueue();
          MessageQueue.IdleHandler local1 = new MessageQueue.IdleHandler()
          {
            public boolean queueIdle()
            {
              LocalAsyncMediaPlayer.this.releaseWakeLock("idle");
              return true;
            }
          };
          localMessageQueue.addIdleHandler(local1);
          return;
        }
      }
    };
    boolean bool3 = post(local1);
    try
    {
      while (true)
      {
        CompatMediaPlayer localCompatMediaPlayer1 = this.mMediaPlayer;
        if (localCompatMediaPlayer1 != null)
          break;
        try
        {
          localObject1.wait();
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
      CompatMediaPlayer localCompatMediaPlayer2 = this.mMediaPlayer;
      Context localContext = this.mContext;
      localCompatMediaPlayer2.setWakeMode(localContext, 1);
      return;
    }
    finally
    {
    }
  }

  private void acquireWakeLockAndSendMsg(String paramString, Message paramMessage)
  {
    MusicUtils.debugLog("AsyncMediaPlayer.acquireWakeLockAndSendMsg(\"" + paramString + "\")");
    this.mWakeLock.acquire();
    if (sendMessage(paramMessage))
      return;
    String str = "Failed to send message: " + paramString;
    Log.w("LocalAsyncMediaPlayer", str);
    this.mWakeLock.release();
  }

  /** @deprecated */
  private void addStatus(int paramInt)
  {
    try
    {
      int i = this.mStatus | paramInt;
      this.mStatus = i;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private boolean handleFailedDownload()
  {
    boolean bool = false;
    if (!this.mIsCurrentPlayer);
    while (true)
    {
      return bool;
      removeStatus(8);
      addStatus(48);
      StringBuilder localStringBuilder = new StringBuilder().append("Failing playback because download failed: ");
      ContentIdentifier localContentIdentifier = this.mCurrentSong;
      String str = localContentIdentifier;
      Log.w("LocalAsyncMediaPlayer", str);
      if (!DownloadErrorType.isServerError(this.mDownloadError))
        bool = true;
      notifyFailure(bool);
    }
  }

  private void handleMessageImp(Message paramMessage)
  {
    boolean bool = true;
    AsyncMediaPlayer.AsyncCommandCallback localAsyncCommandCallback = (AsyncMediaPlayer.AsyncCommandCallback)paramMessage.obj;
    if ((isReleased()) && (paramMessage.what != 4))
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Command after release: ");
      int i = paramMessage.what;
      String str = i;
      Log.w("LocalAsyncMediaPlayer", str);
      return;
    }
    switch (paramMessage.what)
    {
    default:
      return;
    case 1:
      if (paramMessage.arg1 == 1);
      while (true)
      {
        setDataSourceImpl(bool, localAsyncCommandCallback);
        return;
        bool = false;
      }
    case 2:
      startFX();
      this.mMediaPlayer.start();
      long l1 = this.mMediaPlayer.getCurrentPosition();
      this.mPositionMs = l1;
      this.mStopWatch.reset();
      this.mStopWatch.start();
      return;
    case 3:
      this.mMediaPlayer.pause();
      long l2 = this.mMediaPlayer.getCurrentPosition();
      this.mPositionMs = l2;
      this.mStopWatch.pause();
      this.mStopWatch.reset();
      stopFX();
      return;
    case 4:
      MusicUtils.debugLog("AsyncMediaPlayer.RELEASE " + this);
      notifyIfSongPlayed();
      this.mMediaPlayer.release();
      this.mStopWatch.pause();
      this.mStopWatch.reset();
      MusicUtils.debugLog("AsyncMediaPlayer.RELEASE done " + this);
      this.mMediaPlayer = null;
      quit();
      releaseWakeLock("release");
      MusicPreferences.releaseMusicPreferences(this);
      return;
    case 6:
      this.mStopWatch.pause();
      int j = paramMessage.arg1;
      seekImpl(j);
      this.mStopWatch.reset();
      return;
    case 7:
      this.mMediaPlayer.reset();
      return;
    case 5:
      float f = paramMessage.arg1 / 1000.0F;
      this.mMediaPlayer.setVolume(f, f);
      return;
    case 8:
      updateDownloadStatusImpl();
      return;
    case 9:
    }
    logPlayEventImpl();
  }

  private void handleStreamingChanges()
  {
    if (this.LOGV)
    {
      Object[] arrayOfObject = new Object[3];
      Boolean localBoolean = Boolean.valueOf(this.mIsCurrentPlayer);
      arrayOfObject[0] = localBoolean;
      ContentIdentifier localContentIdentifier1 = this.mCurrentSong;
      arrayOfObject[1] = localContentIdentifier1;
      DownloadState.State localState1 = this.mDownloadState;
      arrayOfObject[2] = localState1;
      String str1 = String.format("handleStreamingChanges: mIsCurrentPlayer=%b mCurrentSong=%s mDownloadState=%s", arrayOfObject);
      Log.d("LocalAsyncMediaPlayer", str1);
    }
    DownloadState.State localState2 = this.mDownloadState;
    DownloadState.State localState3 = DownloadState.State.FAILED;
    if (localState2 == localState3)
    {
      boolean bool = handleFailedDownload();
      return;
    }
    DownloadState.State localState4 = this.mDownloadState;
    DownloadState.State localState5 = DownloadState.State.NOT_STARTED;
    if (localState4 != localState5)
    {
      DownloadState.State localState6 = this.mDownloadState;
      DownloadState.State localState7 = DownloadState.State.DOWNLOADING;
      if (localState6 != localState7)
      {
        DownloadState.State localState8 = this.mDownloadState;
        DownloadState.State localState9 = DownloadState.State.COMPLETED;
        if (localState8 != localState9)
          break label146;
      }
    }
    addStatus(32);
    return;
    label146: DownloadState.State localState10 = this.mDownloadState;
    DownloadState.State localState11 = DownloadState.State.CANCELED;
    if (localState10 == localState11)
      return;
    if (!this.mCurrentUrl.startsWith("http"))
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("Song is not downloading, but given http streamUrl: ");
    ContentIdentifier localContentIdentifier2 = this.mCurrentSong;
    String str2 = localContentIdentifier2;
    Log.wtf("LocalAsyncMediaPlayer", str2);
  }

  private boolean isReleased()
  {
    return isStatusOn(1);
  }

  /** @deprecated */
  private boolean isStatusOn(int paramInt)
  {
    try
    {
      int i = this.mStatus;
      if ((i & paramInt) != 0)
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

  private void logPlayEvent()
  {
    Message localMessage = obtainMessage(9);
    acquireWakeLockAndSendMsg("logPlayEvent", localMessage);
  }

  private void logPlayEventImpl()
  {
    if (this.mHasSentPlayEvent)
      return;
    if (this.mPlayEventKey != null)
    {
      MusicEventLogger localMusicEventLogger = this.mEventLogger;
      Object[] arrayOfObject = new Object[2];
      String str1 = this.mPlayEventKey;
      arrayOfObject[0] = str1;
      String str2 = this.mPlayEventValue;
      arrayOfObject[1] = str2;
      localMusicEventLogger.trackEvent("play", arrayOfObject);
      this.mHasSentPlayEvent = true;
      return;
    }
    StringBuilder localStringBuilder = new StringBuilder().append("Missing play event key for id: ");
    ContentIdentifier localContentIdentifier = this.mCurrentSong;
    String str3 = localContentIdentifier;
    Log.w("LocalAsyncMediaPlayer", str3);
  }

  private void notifyFailure(boolean paramBoolean)
  {
    if (this.mCallback == null)
      return;
    if (!this.mIsCurrentPlayer)
      return;
    this.mCallback.onFailure(paramBoolean);
  }

  private void notifyIfSongPlayed()
  {
    if (this.mMediaPlayer == null)
      return;
    if (this.mCurrentSong == null)
      return;
    Context localContext = this.mContext;
    long l1 = duration();
    long l2 = position();
    if (!PlaybackUtils.isPlayed(localContext, l1, l2))
      return;
    try
    {
      LocalDevicePlayback.ServiceHooks localServiceHooks = this.mServiceHooks;
      ContentIdentifier localContentIdentifier = this.mCurrentSong;
      localServiceHooks.markSongPlayed(localContentIdentifier);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("LocalAsyncMediaPlayer", "Could not mark song as played", localRemoteException);
    }
  }

  private void onResumeFromBuffer()
  {
    if (!isPlaying())
      return;
    if (!isPreparing())
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("resumeFromBuffer: id=");
    ContentIdentifier localContentIdentifier1 = this.mCurrentSong;
    String str = localContentIdentifier1;
    Log.i("LocalAsyncMediaPlayer", str);
    MusicEventLogger localMusicEventLogger = this.mEventLogger;
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = "id";
    ContentIdentifier localContentIdentifier2 = this.mCurrentSong;
    arrayOfObject[1] = localContentIdentifier2;
    arrayOfObject[2] = "bufferingEvent";
    arrayOfObject[3] = "resumeFromBuffering";
    localMusicEventLogger.trackEvent("playerBuffering", arrayOfObject);
    removeStatus(8);
    this.mStopWatch.start();
    if (!this.mIsCurrentPlayer)
      return;
    this.mServiceHooks.notifyPlayStateChanged();
  }

  private void onWaitForBuffer()
  {
    DownloadState.State localState1 = this.mDownloadState;
    DownloadState.State localState2 = DownloadState.State.FAILED;
    if (localState1 == localState2)
    {
      if (handleFailedDownload())
        return;
      stop();
      return;
    }
    if (isPreparing())
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("waitForBuffer: id=");
    ContentIdentifier localContentIdentifier1 = this.mCurrentSong;
    String str = localContentIdentifier1;
    Log.i("LocalAsyncMediaPlayer", str);
    MusicEventLogger localMusicEventLogger = this.mEventLogger;
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = "id";
    ContentIdentifier localContentIdentifier2 = this.mCurrentSong;
    arrayOfObject[1] = localContentIdentifier2;
    arrayOfObject[2] = "bufferingEvent";
    arrayOfObject[3] = "waitForBuffering";
    localMusicEventLogger.trackEvent("playerBuffering", arrayOfObject);
    addStatus(8);
    long l = this.mMediaPlayer.getCurrentPosition();
    this.mPositionMs = l;
    this.mStopWatch.pause();
    this.mStopWatch.reset();
    if (!this.mIsCurrentPlayer)
      return;
    this.mServiceHooks.notifyPlayStateChanged();
  }

  private void releaseWakeLock(String paramString)
  {
    MusicUtils.debugLog("AsyncMediaPlayer.releaseWakeLock(\"" + paramString + "\")");
    this.mWakeLock.release();
  }

  /** @deprecated */
  private void removeStatus(int paramInt)
  {
    try
    {
      int i = this.mStatus;
      int j = 0x7FFFFFFF ^ paramInt;
      int k = i & j;
      this.mStatus = k;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private void seekImpl(int paramInt)
  {
    if (isStreaming())
    {
      addStatus(8);
      MediaPlayer localMediaPlayer = this.mMediaPlayer.getNextPlayer();
      this.mMediaPlayer.stop();
      this.mMediaPlayer.reset();
      try
      {
        if (this.mIsCurrentPlayer)
          this.mServiceHooks.notifyOpenStarted();
        this.mMediaPlayer.setAudioStreamType(3);
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        try
        {
          LocalDevicePlayback.ServiceHooks localServiceHooks = this.mServiceHooks;
          ContentIdentifier localContentIdentifier = this.mCurrentSong;
          long l1 = paramInt;
          IDownloadProgressListener localIDownloadProgressListener = this.mDownloadProgressListener;
          String str1 = localServiceHooks.streamTrack(localContentIdentifier, l1, localIDownloadProgressListener, true, true);
          this.mCurrentUrl = str1;
          if (this.mCurrentUrl == null)
          {
            Log.e("LocalAsyncMediaPlayer", "Failed to resolve seek url");
            removeStatus(8);
            if (isReleased())
              return;
            if (!this.mIsCurrentPlayer)
              return;
            addStatus(16);
            notifyFailure(false);
            return;
          }
        }
        catch (OutOfSpaceException localOutOfSpaceException)
        {
          String str2 = localOutOfSpaceException.getMessage();
          Log.e("LocalAsyncMediaPlayer", str2, localOutOfSpaceException);
          this.mDownloadError = 2;
          removeStatus(8);
          addStatus(48);
          notifyFailure(false);
          return;
          localIllegalArgumentException = localIllegalArgumentException;
          String str3 = localIllegalArgumentException.getMessage();
          Log.e("LocalAsyncMediaPlayer", str3, localIllegalArgumentException);
          removeStatus(8);
          if (isReleased())
            return;
          if (!this.mIsCurrentPlayer)
            return;
          addStatus(16);
          notifyFailure(false);
          return;
        }
        catch (DownloadRequestException localDownloadRequestException)
        {
          Log.e("LocalAsyncMediaPlayer", "Failed to request seek in stream", localDownloadRequestException);
          removeStatus(8);
          addStatus(48);
          notifyFailure(false);
          return;
        }
      }
      catch (IOException localIOException)
      {
        String str4 = localIOException.getMessage();
        Log.e("LocalAsyncMediaPlayer", str4, localIOException);
        removeStatus(8);
        if (isReleased())
          return;
        if (!this.mIsCurrentPlayer)
          return;
        addStatus(16);
        notifyFailure(false);
        return;
      }
      String str5 = this.mCurrentUrl;
      setMediaPlayerDataSource(str5);
      if (localMediaPlayer != null)
        this.mMediaPlayer.setNextMediaPlayerCompat(localMediaPlayer);
      this.mMediaPlayer.prepare();
      removeStatus(8);
      long l2 = paramInt;
      this.mHttpSeek = l2;
      this.mPositionMs = 0L;
      if (this.mIsCurrentPlayer)
        this.mServiceHooks.notifyOpenComplete();
      if (isPlaying())
        this.mMediaPlayer.start();
    }
    while (true)
    {
      if (!isPlaying())
        return;
      this.mStopWatch.start();
      return;
      this.mMediaPlayer.seekTo(paramInt);
      this.mHttpSeek = 0L;
      long l3 = this.mMediaPlayer.getCurrentPosition();
      this.mPositionMs = l3;
      if (this.mIsCurrentPlayer)
        this.mServiceHooks.notifyPlayStateChanged();
    }
  }

  private void setDataSourceImpl(boolean paramBoolean, AsyncMediaPlayer.AsyncCommandCallback paramAsyncCommandCallback)
  {
    this.mCallback = paramAsyncCommandCallback;
    this.mCurrentUrl = null;
    this.mCurrentSongSourceAccount = 0L;
    this.mCurrentSongSourceId = "";
    if (this.LOGV)
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("setDataSourceImpl: ");
      ContentIdentifier localContentIdentifier1 = this.mCurrentSong;
      String str1 = localContentIdentifier1;
      Log.d("LocalAsyncMediaPlayer", str1);
    }
    addStatus(8);
    boolean bool1;
    if (this.mCurrentSong.isCacheable())
    {
      long l1 = this.mMusicFile.getSourceAccount();
      this.mCurrentSongSourceAccount = l1;
      String str2 = this.mMusicFile.getSourceId();
      this.mCurrentSongSourceId = str2;
      switch (this.mMusicFile.getLocalCopyType())
      {
      default:
        bool1 = false;
      case 300:
      case 100:
      case 200:
      case 0:
      }
    }
    boolean bool2;
    while (true)
    {
      if ((this.mMusicFile.getTrackType() == 4) || (this.mMusicFile.getTrackType() == 5))
      {
        this.mPlayEventKey = "nautilusId";
        String str3 = this.mMusicFile.getSourceId();
        this.mPlayEventValue = str3;
        bool2 = bool1;
        label215: if ((this.mIsCurrentPlayer) && (paramBoolean))
          this.mServiceHooks.cancelAllStreamingTracks();
        if (!bool2);
      }
      try
      {
        LocalDevicePlayback.ServiceHooks localServiceHooks = this.mServiceHooks;
        ContentIdentifier localContentIdentifier2 = this.mCurrentSong;
        long l2 = this.mPositionMs;
        IDownloadProgressListener localIDownloadProgressListener = this.mDownloadProgressListener;
        boolean bool3 = this.mIsCurrentPlayer;
        boolean bool4 = paramBoolean;
        String str4 = localServiceHooks.streamTrack(localContentIdentifier2, l2, localIDownloadProgressListener, bool3, bool4);
        this.mCurrentUrl = str4;
        addStatus(32);
        if (this.mPositionMs != 0L)
        {
          long l3 = this.mPositionMs;
          this.mHttpSeek = l3;
          this.mPositionMs = 0L;
        }
        if (this.mCurrentUrl == null)
        {
          Log.w("LocalAsyncMediaPlayer", "Failed to resolve track ");
          removeStatus(8);
          addStatus(16);
          notifyFailure(false);
          return;
          if (this.mCurrentSongSourceAccount == 0L);
          for (String str5 = ""; ; str5 = this.mCurrentSongSourceId)
          {
            Object[] arrayOfObject1 = new Object[3];
            String str6 = this.mCurrentSong.toString();
            arrayOfObject1[0] = str6;
            arrayOfObject1[1] = str5;
            Integer localInteger = Integer.valueOf(2);
            arrayOfObject1[2] = localInteger;
            int j = EventLog.writeEvent(74000, arrayOfObject1);
            StringBuilder localStringBuilder2 = new StringBuilder().append("Event logging MUSIC_START_PLAYBACK_REQUESTED: ");
            long l4 = this.mMusicFile.getLocalId();
            String str7 = l4 + "/" + str5 + ": local playback";
            Log.d("LocalAsyncMediaPlayer", str7);
            StringBuilder localStringBuilder3 = new StringBuilder();
            Uri localUri1 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            StringBuilder localStringBuilder4 = localStringBuilder3.append(localUri1).append("/");
            String str8 = this.mMusicFile.getSourceId();
            String str9 = str8;
            this.mCurrentUrl = str9;
            int i = 0;
            break;
          }
          Context localContext1 = this.mContext;
          MusicFile localMusicFile = this.mMusicFile;
          File localFile = CacheUtils.resolveMusicPath(localContext1, localMusicFile);
          if ((localFile != null) && (localFile.exists()))
          {
            String str10 = localFile.getAbsolutePath();
            this.mCurrentUrl = str10;
            Store localStore = Store.getInstance(this.mContext);
            long l5 = this.mMusicFile.getLocalId();
            bool1 = localStore.isLocalCopyCp(l5);
            if (!bool1)
              continue;
            if (!this.mMusicFile.isNautilus())
            {
              StringBuilder localStringBuilder5 = new StringBuilder().append("CP file is no longer nautilus: ");
              ContentIdentifier localContentIdentifier3 = this.mCurrentSong;
              String str11 = localContentIdentifier3;
              Log.i("LocalAsyncMediaPlayer", str11);
              continue;
            }
            if (!this.mPrefs.isNautilusStatusStale())
              continue;
            MusicEventLogger localMusicEventLogger1 = this.mEventLogger;
            Object[] arrayOfObject2 = new Object[0];
            localMusicEventLogger1.trackEvent("playerStaleNautilusError", arrayOfObject2);
            this.mDownloadError = 12;
            removeStatus(8);
            addStatus(16);
            notifyFailure(false);
            return;
          }
          bool1 = true;
          continue;
          bool1 = true;
          continue;
          if (this.mMusicFile.getLocalCopyType() == 300)
          {
            this.mPlayEventKey = "sideloadedId";
            this.mPlayEventValue = "";
            bool2 = bool1;
            break label215;
          }
          this.mPlayEventKey = "lockerId";
          String str12 = this.mMusicFile.getSourceId();
          this.mPlayEventValue = str12;
          bool2 = bool1;
          break label215;
          bool1 = true;
          if (this.mCurrentSong.isSharedDomain())
          {
            this.mPlayEventKey = "sharedPurchasedId";
            this.mPlayEventValue = "";
            bool2 = bool1;
            break label215;
          }
          StringBuilder localStringBuilder6 = new StringBuilder().append("Unable to identify the play type for id: ");
          ContentIdentifier localContentIdentifier4 = this.mCurrentSong;
          String str13 = localContentIdentifier4;
          Log.wtf("LocalAsyncMediaPlayer", str13);
          bool2 = bool1;
        }
      }
      catch (OutOfSpaceException localOutOfSpaceException)
      {
        String str14 = localOutOfSpaceException.getMessage();
        Log.e("LocalAsyncMediaPlayer", str14, localOutOfSpaceException);
        this.mDownloadError = 2;
        removeStatus(8);
        addStatus(16);
        notifyFailure(false);
        return;
      }
      catch (DownloadRequestException localDownloadRequestException)
      {
        String str15 = localDownloadRequestException.getMessage();
        Log.e("LocalAsyncMediaPlayer", str15, localDownloadRequestException);
        removeStatus(8);
        addStatus(16);
        notifyFailure(false);
        return;
      }
    }
    if ((isStreaming()) && (this.mIsCurrentPlayer))
      this.mServiceHooks.notifyOpenStarted();
    try
    {
      if (this.LOGV)
      {
        StringBuilder localStringBuilder7 = new StringBuilder().append("setDataSourceImpl: mCurrentUrl=");
        String str16 = this.mCurrentUrl;
        String str17 = str16;
        Log.d("LocalAsyncMediaPlayer", str17);
      }
      this.mMediaPlayer.reset();
      this.mMediaPlayer.setOnPreparedListener(null);
      if (this.mCurrentUrl.startsWith("content://"))
      {
        Uri localUri2 = Uri.parse(this.mCurrentUrl);
        CompatMediaPlayer localCompatMediaPlayer1 = this.mMediaPlayer;
        Context localContext2 = this.mContext;
        localCompatMediaPlayer1.setDataSource(localContext2, localUri2);
        removeStatus(32);
        this.mMediaPlayer.setAudioStreamType(3);
        CompatMediaPlayer localCompatMediaPlayer2 = this.mMediaPlayer;
        MediaPlayer.OnCompletionListener localOnCompletionListener = this.completionListener;
        localCompatMediaPlayer2.setOnCompletionListener(localOnCompletionListener);
        CompatMediaPlayer localCompatMediaPlayer3 = this.mMediaPlayer;
        MediaPlayer.OnErrorListener localOnErrorListener = this.errorListener;
        localCompatMediaPlayer3.setOnErrorListener(localOnErrorListener);
        CompatMediaPlayer localCompatMediaPlayer4 = this.mMediaPlayer;
        MediaPlayer.OnSeekCompleteListener localOnSeekCompleteListener = this.seekListener;
        localCompatMediaPlayer4.setOnSeekCompleteListener(localOnSeekCompleteListener);
        if (this.LOGV)
        {
          StringBuilder localStringBuilder8 = new StringBuilder().append("MediaPlayer.prepare: ");
          ContentIdentifier localContentIdentifier5 = this.mCurrentSong;
          String str18 = localContentIdentifier5;
          Log.d("LocalAsyncMediaPlayer", str18);
        }
        this.mMediaPlayer.prepare();
        if (this.LOGV)
        {
          StringBuilder localStringBuilder9 = new StringBuilder().append("MediaPlayer.prepare FINISHED: ");
          ContentIdentifier localContentIdentifier6 = this.mCurrentSong;
          String str19 = localContentIdentifier6;
          Log.d("LocalAsyncMediaPlayer", str19);
        }
        if (!isReleased())
          break label1652;
        removeStatus(8);
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        if (!isReleased())
        {
          StringBuilder localStringBuilder10 = new StringBuilder().append("Track: ");
          ContentIdentifier localContentIdentifier7 = this.mCurrentSong;
          StringBuilder localStringBuilder11 = localStringBuilder10.append(localContentIdentifier7).append(" : ");
          String str20 = localIOException.getMessage();
          String str21 = str20;
          Log.e("LocalAsyncMediaPlayer", str21, localIOException);
          MusicEventLogger localMusicEventLogger2 = this.mEventLogger;
          Object[] arrayOfObject3 = new Object[4];
          arrayOfObject3[0] = "failureMsg";
          String str22 = localIOException.getMessage();
          arrayOfObject3[1] = str22;
          arrayOfObject3[2] = "failureStack";
          String str23 = DebugUtils.getStackTrace(localIOException);
          arrayOfObject3[3] = str23;
          localMusicEventLogger2.trackEvent("playerError", arrayOfObject3);
        }
        removeStatus(8);
        if ((!isReleased()) && (!isStreaming()))
        {
          addStatus(16);
          notifyFailure(false);
        }
        if (this.mCurrentSongSourceAccount != 0L)
          return;
        Log.i("LocalAsyncMediaPlayer", "Requesting import of media store");
        Context localContext3 = this.mContext;
        Intent localIntent1 = new Intent(localContext3, MediaStoreImportService.class);
        Intent localIntent2 = localIntent1.setAction("MediaStoreImportService.import");
        if (this.mContext.startService(localIntent1) != null)
          return;
        Exception localException = new Exception();
        Log.wtf("LocalAsyncMediaPlayer", "Failed to start MediaStoreImportService", localException);
        return;
        String str24 = this.mCurrentUrl;
        setMediaPlayerDataSource(str24);
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      while (true)
        if (!isReleased())
        {
          String str25 = localIllegalArgumentException.getMessage();
          Log.e("LocalAsyncMediaPlayer", str25, localIllegalArgumentException);
          MusicEventLogger localMusicEventLogger3 = this.mEventLogger;
          Object[] arrayOfObject4 = new Object[4];
          arrayOfObject4[0] = "failureMsg";
          String str26 = localIllegalArgumentException.getMessage();
          arrayOfObject4[1] = str26;
          arrayOfObject4[2] = "failureStack";
          String str27 = DebugUtils.getStackTrace(localIllegalArgumentException);
          arrayOfObject4[3] = str27;
          localMusicEventLogger3.trackEvent("playerError", arrayOfObject4);
        }
      label1652: if ((this.mPositionMs != 0L) && (!bool2))
      {
        CompatMediaPlayer localCompatMediaPlayer5 = this.mMediaPlayer;
        int k = (int)this.mPositionMs;
        localCompatMediaPlayer5.seekTo(k);
      }
      if (this.mIsCurrentPlayer)
        logPlayEvent();
      startFX();
      if (!this.isPreGingerbread)
      {
        CompatMediaPlayer localCompatMediaPlayer6 = this.mMediaPlayer;
        MediaPlayer.OnInfoListener localOnInfoListener = this.infoListener;
        localCompatMediaPlayer6.setOnInfoListener(localOnInfoListener);
      }
      addStatus(2);
      removeStatus(8);
      paramAsyncCommandCallback.onSuccess();
    }
  }

  private void setMediaPlayerDataSource(String paramString)
    throws IOException
  {
    if (paramString.startsWith("http"))
    {
      this.mMediaPlayer.setDataSource(paramString);
      return;
    }
    FileInputStream localFileInputStream = new FileInputStream(paramString);
    FileDescriptor localFileDescriptor = localFileInputStream.getFD();
    this.mMediaPlayer.setDataSource(localFileDescriptor);
    localFileInputStream.close();
  }

  private void startFX()
  {
    Intent localIntent1 = new Intent("android.media.action.OPEN_AUDIO_EFFECT_CONTROL_SESSION");
    int i = getAudioSessionId();
    Intent localIntent2 = localIntent1.putExtra("android.media.extra.AUDIO_SESSION", i);
    String str = this.mContext.getPackageName();
    Intent localIntent3 = localIntent1.putExtra("android.media.extra.PACKAGE_NAME", str);
    this.mContext.sendBroadcast(localIntent1);
  }

  private void stopFX()
  {
    Intent localIntent1 = new Intent("android.media.action.CLOSE_AUDIO_EFFECT_CONTROL_SESSION");
    int i = getAudioSessionId();
    Intent localIntent2 = localIntent1.putExtra("android.media.extra.AUDIO_SESSION", i);
    String str = this.mContext.getPackageName();
    Intent localIntent3 = localIntent1.putExtra("android.media.extra.PACKAGE_NAME", str);
    this.mContext.sendBroadcast(localIntent1);
  }

  private void updateDownloadStatus(long paramLong)
  {
    if (isReleased())
      return;
    removeMessages(8);
    DownloadState.State localState1 = this.mDownloadState;
    DownloadState.State localState2 = DownloadState.State.COMPLETED;
    if (localState1 == localState2)
      return;
    boolean bool = sendEmptyMessageDelayed(8, paramLong);
  }

  private void updateDownloadStatusImpl()
  {
    long l1 = duration();
    long l2 = position() + 5000L;
    float f1 = (float)l2;
    float f2 = (float)l1;
    float f3 = f1 / f2;
    if (this.mDownloadCompletedPercent > f3)
    {
      onResumeFromBuffer();
      float f4 = this.mDownloadCompletedPercent;
      float f5 = (float)l1;
      long l3 = ()(f4 * f5) - l2;
      updateDownloadStatus(l3);
      return;
    }
    onWaitForBuffer();
  }

  public long duration()
  {
    return this.mDurationMs;
  }

  public int getAudioSessionId()
  {
    try
    {
      Class localClass = this.mMediaPlayer.getClass();
      Class[] arrayOfClass = new Class[0];
      Method localMethod = localClass.getMethod("getAudioSessionId", arrayOfClass);
      CompatMediaPlayer localCompatMediaPlayer = this.mMediaPlayer;
      Object[] arrayOfObject = new Object[0];
      int i = ((Integer)localMethod.invoke(localCompatMediaPlayer, arrayOfObject)).intValue();
      j = i;
      return j;
    }
    catch (Exception localException)
    {
      while (true)
        int j = -1;
    }
  }

  public int getErrorType()
  {
    if (this.mCurrentSong == null);
    for (int i = 1; ; i = this.mDownloadError)
      return i;
  }

  public String getRemoteSongId()
  {
    if (this.mCurrentSongSourceAccount == 0L);
    for (String str = ""; ; str = this.mCurrentSongSourceId)
      return str;
  }

  public void handleMessage(Message paramMessage)
  {
    try
    {
      handleMessageImp(paramMessage);
      return;
    }
    catch (Throwable localThrowable)
    {
      MusicUtils.debugLog(localThrowable);
      releaseWakeLock("handleMessage");
      throw new RuntimeException(localThrowable);
    }
  }

  public boolean isInErrorState()
  {
    return isStatusOn(16);
  }

  public boolean isInitialized()
  {
    return isStatusOn(2);
  }

  public boolean isPlaying()
  {
    return isStatusOn(4);
  }

  public boolean isPreparing()
  {
    return isStatusOn(8);
  }

  public boolean isRenderingAudioLocally()
  {
    return true;
  }

  public boolean isStreaming()
  {
    return isStatusOn(32);
  }

  public void pause()
  {
    if (isReleased())
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("AsyncMediaPlayer(");
    ContentIdentifier localContentIdentifier = this.mCurrentSong;
    MusicUtils.debugLog(localContentIdentifier + ").pause " + this);
    removeMessages(3);
    removeMessages(2);
    removeMessages(7);
    removeStatus(4);
    Message localMessage = obtainMessage(3);
    acquireWakeLockAndSendMsg("pause", localMessage);
  }

  public long position()
  {
    long l1 = this.mPositionMs;
    long l2 = this.mStopWatch.getTime();
    long l3 = l1 + l2;
    long l4 = this.mHttpSeek;
    return l3 + l4;
  }

  public void release()
  {
    if (isReleased())
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("AsyncMediaPlayer(");
    ContentIdentifier localContentIdentifier = this.mCurrentSong;
    MusicUtils.debugLog(localContentIdentifier + ").release " + this);
    this.mMediaPlayer.setOnCompletionListener(null);
    this.mMediaPlayer.setOnErrorListener(null);
    this.mMediaPlayer.setOnSeekCompleteListener(null);
    this.mMediaPlayer.setOnPreparedListener(null);
    this.mMediaPlayer.setOnInfoListener(null);
    addStatus(1);
    removeStatus(12);
    removeCallbacksAndMessages(null);
    Message localMessage = obtainMessage(4);
    acquireWakeLockAndSendMsg("release", localMessage);
  }

  public long seek(long paramLong)
  {
    if (isReleased())
      paramLong = 0L;
    while (true)
    {
      return paramLong;
      removeMessages(6);
      this.mPositionMs = paramLong;
      this.mHttpSeek = 0L;
      int i = (int)paramLong;
      Message localMessage = obtainMessage(6, i, 0);
      this.mStopWatch.reset();
      acquireWakeLockAndSendMsg("seek", localMessage);
    }
  }

  public void setAsCurrentPlayer()
  {
    this.mIsCurrentPlayer = true;
    logPlayEvent();
  }

  public void setAudioSessionId(int paramInt)
  {
    if (paramInt < 0)
      return;
    try
    {
      Class localClass1 = this.mMediaPlayer.getClass();
      Class[] arrayOfClass = new Class[1];
      Class localClass2 = Integer.TYPE;
      arrayOfClass[0] = localClass2;
      Method localMethod = localClass1.getMethod("setAudioSessionId", arrayOfClass);
      CompatMediaPlayer localCompatMediaPlayer = this.mMediaPlayer;
      Object[] arrayOfObject = new Object[1];
      Integer localInteger = Integer.valueOf(paramInt);
      arrayOfObject[0] = localInteger;
      Object localObject = localMethod.invoke(localCompatMediaPlayer, arrayOfObject);
      return;
    }
    catch (Exception localException)
    {
    }
  }

  public void setDataSource(ContentIdentifier paramContentIdentifier, long paramLong1, long paramLong2, boolean paramBoolean1, boolean paramBoolean2, MusicFile paramMusicFile, AsyncMediaPlayer.AsyncCommandCallback paramAsyncCommandCallback)
  {
    int i = 1;
    if (this.LOGV)
    {
      String str1 = "setDataSource: " + paramContentIdentifier;
      Log.d("LocalAsyncMediaPlayer", str1);
    }
    if (paramContentIdentifier == null)
      throw new IllegalArgumentException("Invalid ContentIdentifier");
    if (this.mCurrentSong != null)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Can't reuse. Previously used for ");
      ContentIdentifier localContentIdentifier = this.mCurrentSong;
      String str2 = localContentIdentifier + ", now for " + paramContentIdentifier;
      throw new IllegalStateException(str2);
    }
    MusicUtils.debugLog("AsyncMediaPlayer setDataSource " + this + ": " + paramContentIdentifier);
    this.mCurrentSong = paramContentIdentifier;
    this.mPositionMs = paramLong1;
    this.mDurationMs = paramLong2;
    this.mIsCurrentPlayer = paramBoolean1;
    this.mMusicFile = paramMusicFile;
    Message localMessage = obtainMessage(1, paramAsyncCommandCallback);
    if (paramBoolean2);
    while (true)
    {
      localMessage.arg1 = i;
      acquireWakeLockAndSendMsg("setDataSource", localMessage);
      return;
      i = 0;
    }
  }

  public void setNextPlayer(AsyncMediaPlayer paramAsyncMediaPlayer)
  {
    if (isReleased())
      return;
    LocalAsyncMediaPlayer localLocalAsyncMediaPlayer = null;
    if ((paramAsyncMediaPlayer instanceof LocalAsyncMediaPlayer))
      localLocalAsyncMediaPlayer = (LocalAsyncMediaPlayer)paramAsyncMediaPlayer;
    CompatMediaPlayer localCompatMediaPlayer1 = this.mMediaPlayer;
    if (localLocalAsyncMediaPlayer != null);
    for (CompatMediaPlayer localCompatMediaPlayer2 = localLocalAsyncMediaPlayer.mMediaPlayer; ; localCompatMediaPlayer2 = null)
    {
      localCompatMediaPlayer1.setNextMediaPlayerCompat(localCompatMediaPlayer2);
      return;
    }
  }

  public void setVolume(float paramFloat)
  {
    if (isReleased())
      return;
    removeMessages(5);
    int i = (int)(1000.0F * paramFloat);
    Message localMessage = obtainMessage(5, i, 0);
    acquireWakeLockAndSendMsg("setVolume", localMessage);
  }

  public void start()
  {
    if (isReleased())
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("AsyncMediaPlayer(");
    ContentIdentifier localContentIdentifier = this.mCurrentSong;
    MusicUtils.debugLog(localContentIdentifier + ").start " + this);
    removeMessages(3);
    removeMessages(2);
    Message localMessage = obtainMessage(2);
    acquireWakeLockAndSendMsg("start", localMessage);
    addStatus(4);
  }

  public void stop()
  {
    if (isReleased())
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("AsyncMediaPlayer(");
    ContentIdentifier localContentIdentifier = this.mCurrentSong;
    MusicUtils.debugLog(localContentIdentifier + ").stop " + this);
    if (this.mIsCurrentPlayer)
      this.mServiceHooks.cancelTryNext();
    removeMessages(3);
    removeMessages(2);
    removeMessages(7);
    removeStatus(14);
    Message localMessage = obtainMessage(7);
    acquireWakeLockAndSendMsg("stop", localMessage);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.playback.LocalAsyncMediaPlayer
 * JD-Core Version:    0.6.2
 */