package com.google.android.music.download.keepon;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.DownloadProgress;
import com.google.android.music.download.IDownloadProgressListener;
import com.google.android.music.log.Log;

public class KeeponCallbackManagerImpl extends IKeeponCallbackManager.Stub
{
  final RemoteCallbackList<IDownloadProgressListener> mProgressListeners;

  public KeeponCallbackManagerImpl()
  {
    RemoteCallbackList localRemoteCallbackList = new RemoteCallbackList();
    this.mProgressListeners = localRemoteCallbackList;
  }

  public boolean addDownloadProgressListener(ContentIdentifier paramContentIdentifier, IDownloadProgressListener paramIDownloadProgressListener)
    throws RemoteException
  {
    if (paramIDownloadProgressListener != null);
    for (boolean bool = this.mProgressListeners.register(paramIDownloadProgressListener); ; bool = false)
      return bool;
  }

  public void notifyListeners(DownloadProgress paramDownloadProgress)
  {
    int i = this.mProgressListeners.beginBroadcast();
    int j = 0;
    while (j < i)
      try
      {
        ((IDownloadProgressListener)this.mProgressListeners.getBroadcastItem(j)).onDownloadProgress(paramDownloadProgress);
        j += 1;
      }
      catch (RemoteException localRemoteException)
      {
        while (true)
          Log.e("KeeponCallback", "Failed to call the download probress", localRemoteException);
      }
      finally
      {
        this.mProgressListeners.finishBroadcast();
      }
    this.mProgressListeners.finishBroadcast();
  }

  public void removeDownloadProgressListener(IDownloadProgressListener paramIDownloadProgressListener)
    throws RemoteException
  {
    if (paramIDownloadProgressListener == null)
      return;
    boolean bool = this.mProgressListeners.unregister(paramIDownloadProgressListener);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.keepon.KeeponCallbackManagerImpl
 * JD-Core Version:    0.6.2
 */