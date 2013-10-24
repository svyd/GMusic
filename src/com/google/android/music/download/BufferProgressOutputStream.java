package com.google.android.music.download;

import android.os.RemoteException;
import com.google.android.music.log.Log;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BufferProgressOutputStream extends OutputStream
{
  private final boolean LOGV;
  private final IDownloadProgressListener mDownloadProgressListener;
  private final DownloadRequest mDownloadRequest;
  private final DownloadState mDownloadState;
  private long mNextNotification;
  private long mNextNotificationBytes;
  private final long mNotificationByteGap;
  private final OutputStream mOut;

  public BufferProgressOutputStream(IDownloadProgressListener paramIDownloadProgressListener, OutputStream paramOutputStream, DownloadRequest paramDownloadRequest, DownloadState paramDownloadState)
  {
    boolean bool = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
    this.LOGV = bool;
    this.mDownloadProgressListener = paramIDownloadProgressListener;
    this.mOut = paramOutputStream;
    this.mDownloadRequest = paramDownloadRequest;
    this.mDownloadState = paramDownloadState;
    long l1 = System.currentTimeMillis() + 1000L;
    this.mNextNotification = l1;
    long l2 = ()((float)this.mDownloadState.getDownloadByteLength() * 0.1F);
    this.mNotificationByteGap = l2;
    long l3 = this.mNotificationByteGap;
    this.mNextNotificationBytes = l3;
  }

  public void close()
    throws IOException
  {
    if ((this.mOut instanceof FileOutputStream))
      ((FileOutputStream)this.mOut).getFD().sync();
    try
    {
      this.mOut.close();
      return;
    }
    catch (IOException localIOException)
    {
      if (!this.LOGV)
        return;
      StringBuilder localStringBuilder = new StringBuilder().append("Failed to close output stream:");
      String str1 = localIOException.getMessage();
      String str2 = str1;
      Log.w("BufferProgress", str2);
    }
  }

  public void flush()
    throws IOException
  {
    this.mOut.flush();
  }

  public void sendBufferProgress()
  {
    try
    {
      IDownloadProgressListener localIDownloadProgressListener = this.mDownloadProgressListener;
      DownloadRequest localDownloadRequest1 = this.mDownloadRequest;
      DownloadState localDownloadState = this.mDownloadState;
      DownloadProgress localDownloadProgress = new DownloadProgress(localDownloadRequest1, localDownloadState);
      localIDownloadProgressListener.onDownloadProgress(localDownloadProgress);
      long l1 = System.currentTimeMillis() + 1000L;
      this.mNextNotification = l1;
      long l2 = this.mDownloadState.getCompletedBytes();
      long l3 = this.mNotificationByteGap;
      long l4 = l2 + l3;
      this.mNextNotificationBytes = l4;
      return;
    }
    catch (RemoteException localRemoteException)
    {
      while (true)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Failed to update progress for request: ");
        DownloadRequest localDownloadRequest2 = this.mDownloadRequest;
        String str = localDownloadRequest2;
        Log.e("BufferProgress", str, localRemoteException);
      }
    }
  }

  public void write(int paramInt)
  {
    throw new UnsupportedOperationException();
  }

  public void write(byte[] paramArrayOfByte)
    throws IOException
  {
    int i = paramArrayOfByte.length;
    write(paramArrayOfByte, 0, i);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    this.mOut.write(paramArrayOfByte, paramInt1, paramInt2);
    DownloadState localDownloadState = this.mDownloadState;
    long l1 = paramInt2 - paramInt1;
    long l2 = localDownloadState.incrementCompletedBytes(l1);
    long l3 = System.currentTimeMillis();
    long l4 = this.mNextNotification;
    if (l3 <= l4)
    {
      long l5 = this.mNextNotificationBytes;
      if (l2 <= l5)
        return;
    }
    sendBufferProgress();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.BufferProgressOutputStream
 * JD-Core Version:    0.6.2
 */