package com.google.android.music;

import android.os.RemoteException;
import android.util.Log;
import android.widget.ProgressBar;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.IDownloadProgressListener;
import com.google.android.music.download.keepon.IKeeponCallbackManager;
import com.google.android.music.download.keepon.KeeponSchedulingServiceConnection;

public class KeeponBufferProgressListener extends BufferProgressListener
{
  private final KeeponSchedulingServiceConnection mKeeponSchedulingServiceConnection;

  public KeeponBufferProgressListener(ProgressBar paramProgressBar, KeeponSchedulingServiceConnection paramKeeponSchedulingServiceConnection)
  {
    super(paramProgressBar);
    this.mKeeponSchedulingServiceConnection = paramKeeponSchedulingServiceConnection;
  }

  protected void connectListener(ContentIdentifier paramContentIdentifier)
  {
    IKeeponCallbackManager localIKeeponCallbackManager = this.mKeeponSchedulingServiceConnection.getKeeponCallbackManager();
    if (localIKeeponCallbackManager == null)
      return;
    try
    {
      IDownloadProgressListener localIDownloadProgressListener = this.mDownloadProgressListener;
      boolean bool = localIKeeponCallbackManager.addDownloadProgressListener(paramContentIdentifier, localIDownloadProgressListener);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      String str = localRemoteException.getMessage();
      int i = Log.e("KeeponBufferListener", str, localRemoteException);
    }
  }

  protected void disconnectListener()
  {
    IKeeponCallbackManager localIKeeponCallbackManager = this.mKeeponSchedulingServiceConnection.getKeeponCallbackManager();
    if (localIKeeponCallbackManager == null)
      return;
    try
    {
      IDownloadProgressListener localIDownloadProgressListener = this.mDownloadProgressListener;
      localIKeeponCallbackManager.removeDownloadProgressListener(localIDownloadProgressListener);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      String str = localRemoteException.getMessage();
      int i = Log.e("KeeponBufferListener", str, localRemoteException);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.KeeponBufferProgressListener
 * JD-Core Version:    0.6.2
 */