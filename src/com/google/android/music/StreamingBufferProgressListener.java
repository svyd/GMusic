package com.google.android.music;

import android.os.RemoteException;
import android.util.Log;
import android.widget.ProgressBar;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.IDownloadProgressListener;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.MusicUtils;

public class StreamingBufferProgressListener extends BufferProgressListener
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.UI);

  public StreamingBufferProgressListener(ProgressBar paramProgressBar)
  {
    super(paramProgressBar);
  }

  protected void connectListener(ContentIdentifier paramContentIdentifier)
  {
    try
    {
      if (MusicUtils.sService == null)
        return;
      IMusicPlaybackService localIMusicPlaybackService = MusicUtils.sService;
      IDownloadProgressListener localIDownloadProgressListener = this.mDownloadProgressListener;
      boolean bool = localIMusicPlaybackService.addDownloadProgressListener(paramContentIdentifier, localIDownloadProgressListener);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      String str = localRemoteException.getMessage();
      int i = Log.e("SBufferProgressListener", str, localRemoteException);
    }
  }

  protected void disconnectListener()
  {
    try
    {
      if (MusicUtils.sService == null)
        return;
      IMusicPlaybackService localIMusicPlaybackService = MusicUtils.sService;
      IDownloadProgressListener localIDownloadProgressListener = this.mDownloadProgressListener;
      localIMusicPlaybackService.removeDownloadProgressListener(localIDownloadProgressListener);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      String str = localRemoteException.getMessage();
      int i = Log.e("SBufferProgressListener", str, localRemoteException);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.StreamingBufferProgressListener
 * JD-Core Version:    0.6.2
 */