package com.google.android.music.download;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.google.android.music.log.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class DownloadQueueService extends Service
{
  private volatile DownloadQueueManagerImpl mDownloadQueueManager;

  protected void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.println("DownloadQueueService:");
    Log.dump(paramPrintWriter);
  }

  public IBinder onBind(Intent paramIntent)
  {
    return this.mDownloadQueueManager;
  }

  public void onCreate()
  {
    DownloadQueueManagerImpl localDownloadQueueManagerImpl = new DownloadQueueManagerImpl(this);
    this.mDownloadQueueManager = localDownloadQueueManagerImpl;
  }

  public void onDestroy()
  {
    if (this.mDownloadQueueManager == null)
      return;
    this.mDownloadQueueManager.onDestroy();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.DownloadQueueService
 * JD-Core Version:    0.6.2
 */