package com.google.android.music;

import android.os.RemoteException;
import android.util.Log;
import android.widget.ProgressBar;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.DownloadProgress;
import com.google.android.music.download.DownloadState.State;
import com.google.android.music.download.IDownloadProgressListener;
import com.google.android.music.download.IDownloadProgressListener.Stub;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.MusicUtils;

public abstract class BufferProgressListener
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.UI);
  private ContentIdentifier mCurrentSong;
  protected IDownloadProgressListener mDownloadProgressListener;
  private ProgressBar mProgressBar;

  public BufferProgressListener(ProgressBar paramProgressBar)
  {
    IDownloadProgressListener.Stub local1 = new IDownloadProgressListener.Stub()
    {
      public void onDownloadProgress(DownloadProgress paramAnonymousDownloadProgress)
      {
        BufferProgressListener.this.processBufferBroadcast(paramAnonymousDownloadProgress);
      }
    };
    this.mDownloadProgressListener = local1;
    this.mProgressBar = paramProgressBar;
  }

  /** @deprecated */
  private void connectReceiver(ContentIdentifier paramContentIdentifier)
  {
    try
    {
      connectListener(paramContentIdentifier);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  private void processBufferBroadcast(DownloadProgress paramDownloadProgress)
  {
    while (true)
    {
      try
      {
        if (this.mCurrentSong != null)
        {
          ContentIdentifier localContentIdentifier1 = this.mCurrentSong;
          ContentIdentifier localContentIdentifier2 = paramDownloadProgress.getId();
          boolean bool = localContentIdentifier1.equals(localContentIdentifier2);
          if (bool);
        }
        else
        {
          return;
        }
        if (this.mProgressBar == null)
          continue;
        DownloadState.State localState1 = paramDownloadProgress.getState();
        DownloadState.State localState2 = DownloadState.State.COMPLETED;
        if (localState1 != localState2)
        {
          DownloadState.State localState3 = DownloadState.State.FAILED;
          if (localState1 != localState3)
          {
            DownloadState.State localState4 = DownloadState.State.CANCELED;
            if (localState1 != localState4)
              break label131;
          }
        }
        stopReceiver();
        DownloadState.State localState5 = DownloadState.State.COMPLETED;
        if (localState1 != localState5)
          continue;
        ProgressBar localProgressBar1 = this.mProgressBar;
        int i = this.mProgressBar.getMax();
        localProgressBar1.setSecondaryProgress(i);
        continue;
      }
      finally
      {
      }
      label131: float f1 = (float)paramDownloadProgress.getCompletedBytes();
      float f2 = (float)paramDownloadProgress.getDownloadByteLength();
      float f3 = f1 / f2;
      if (f3 >= 0.99F)
      {
        ProgressBar localProgressBar2 = this.mProgressBar;
        int j = this.mProgressBar.getMax();
        localProgressBar2.setSecondaryProgress(j);
      }
      else
      {
        ProgressBar localProgressBar3 = this.mProgressBar;
        int k = (int)(this.mProgressBar.getMax() * f3);
        localProgressBar3.setSecondaryProgress(k);
      }
    }
  }

  /** @deprecated */
  private void stopReceiver()
  {
    try
    {
      if ((this.mCurrentSong != null) && (MusicUtils.sService != null))
      {
        disconnectListener();
        this.mCurrentSong = null;
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  public void cleanup()
  {
    try
    {
      stopReceiver();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  protected abstract void connectListener(ContentIdentifier paramContentIdentifier);

  /** @deprecated */
  public void destroy()
  {
    try
    {
      stopReceiver();
      this.mProgressBar = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  protected abstract void disconnectListener();

  /** @deprecated */
  public void updateCurrentSong(ContentIdentifier paramContentIdentifier, boolean paramBoolean)
  {
    ContentIdentifier localContentIdentifier;
    do
    {
      do
        try
        {
          int i = this.mProgressBar;
          if (i == 0);
          while (true)
          {
            return;
            if (LOGV)
            {
              String str = "updateCurrentSong: " + paramContentIdentifier;
              int k = Log.d("BufferProgressListener", str);
            }
            if (paramContentIdentifier != null)
              break;
            stopReceiver();
            ProgressBar localProgressBar1 = this.mProgressBar;
            int m = this.mProgressBar.getMax();
            localProgressBar1.setSecondaryProgress(m);
          }
        }
        finally
        {
        }
      while (MusicUtils.sService == null);
      localContentIdentifier = this.mCurrentSong;
    }
    while (paramContentIdentifier.equals(localContentIdentifier));
    stopReceiver();
    int n;
    if (paramBoolean)
      n = 0;
    while (true)
    {
      try
      {
        boolean bool = MusicUtils.sService.isStreamingFullyBuffered();
        n = bool;
        ProgressBar localProgressBar2 = this.mProgressBar;
        if (n != 0)
        {
          j = this.mProgressBar.getMax();
          localProgressBar2.setSecondaryProgress(j);
          connectReceiver(paramContentIdentifier);
          this.mCurrentSong = paramContentIdentifier;
        }
      }
      catch (RemoteException localRemoteException)
      {
        if (!LOGV)
          continue;
        int i1 = Log.d("BufferProgressListener", "Failed to check if the current song is fully buffered", localRemoteException);
        continue;
        int j = 0;
        continue;
      }
      ProgressBar localProgressBar3 = this.mProgressBar;
      int i2 = this.mProgressBar.getMax();
      localProgressBar3.setSecondaryProgress(i2);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.BufferProgressListener
 * JD-Core Version:    0.6.2
 */