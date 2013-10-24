package com.google.android.music.download;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Build.VERSION;
import android.os.RemoteException;
import com.google.android.common.http.GoogleHttpClient;
import com.google.android.music.cloudclient.MusicRequest;
import com.google.android.music.log.Log;
import com.google.android.music.net.INetworkMonitor;
import com.google.android.music.net.NetworkMonitorServiceConnection;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DownloadQueueManagerImpl extends IDownloadQueueManager.Stub
{
  private final boolean LOGV;
  private final Context mContext;
  private final DownloadQueue mDownloadQueue;
  private final Thread mDownloadThread;
  private final GoogleHttpClient mHttpClient;
  private final MusicPreferences mMusicPreferences;
  private final NetworkMonitorServiceConnection mNetworkMonitorServiceConnection;
  private volatile boolean mShutdown;
  private WifiManager.WifiLock mWifiLock;
  private final WifiManager mWifiManager;

  public DownloadQueueManagerImpl(Context paramContext)
  {
    boolean bool = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
    this.LOGV = bool;
    this.mShutdown = false;
    NetworkMonitorServiceConnection localNetworkMonitorServiceConnection1 = new NetworkMonitorServiceConnection();
    this.mNetworkMonitorServiceConnection = localNetworkMonitorServiceConnection1;
    Thread local1 = new Thread("DownloadThread")
    {
      public void run()
      {
        while (true)
        {
          if (!DownloadQueueManagerImpl.this.mShutdown);
          Object localObject;
          while (true)
          {
            try
            {
              DownloadQueueManagerImpl.this.mDownloadQueue.clearCurrentTask();
              boolean bool1 = Thread.interrupted();
              localObject = null;
              try
              {
                DownloadTask localDownloadTask = DownloadQueueManagerImpl.this.mDownloadQueue.getNextTask();
                localObject = localDownloadTask;
                if (!DownloadQueueManagerImpl.this.mShutdown)
                  break label158;
                if (DownloadQueueManagerImpl.this.LOGV)
                  Log.d("DownloadQueueManager", "Shutting down");
                if (!DownloadQueueManagerImpl.this.LOGV)
                  return;
                StringBuilder localStringBuilder = new StringBuilder().append("Download thread finished: mShutdown=");
                boolean bool2 = DownloadQueueManagerImpl.this.mShutdown;
                String str = bool2;
                Log.d("DownloadQueueManager", str);
                return;
              }
              catch (InterruptedException localInterruptedException)
              {
                if (!DownloadQueueManagerImpl.this.LOGV)
                  continue;
                Log.d("DownloadQueueManager", "Interrupted:", localInterruptedException);
                continue;
              }
            }
            catch (Throwable localThrowable)
            {
              Log.w("DownloadQueueManager", "Unhandled exception: ", localThrowable);
            }
            break;
            label158: if (localObject == null)
              break;
            if (!Thread.interrupted())
              break label211;
            if (!DownloadQueueManagerImpl.this.mShutdown)
              break label198;
            if (DownloadQueueManagerImpl.this.LOGV)
              Log.d("DownloadQueueManager", "We have a task but need to shutdown");
          }
          label198: DownloadQueueManagerImpl.this.mDownloadQueue.clearCurrentTask();
          continue;
          label211: DownloadQueueManagerImpl.this.runTask(localObject);
        }
      }
    };
    this.mDownloadThread = local1;
    this.mContext = paramContext;
    Thread localThread = this.mDownloadThread;
    DownloadQueue localDownloadQueue = new DownloadQueue(localThread);
    this.mDownloadQueue = localDownloadQueue;
    GoogleHttpClient localGoogleHttpClient = MusicRequest.getSharedHttpClient(this.mContext);
    this.mHttpClient = localGoogleHttpClient;
    NetworkMonitorServiceConnection localNetworkMonitorServiceConnection2 = this.mNetworkMonitorServiceConnection;
    Context localContext = this.mContext;
    localNetworkMonitorServiceConnection2.bindToService(localContext);
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, this);
    this.mMusicPreferences = localMusicPreferences;
    WifiManager localWifiManager = (WifiManager)paramContext.getSystemService("wifi");
    this.mWifiManager = localWifiManager;
    this.mDownloadThread.start();
  }

  private void acquireWifiLock()
  {
    try
    {
      INetworkMonitor localINetworkMonitor = this.mNetworkMonitorServiceConnection.getNetworkMonitor();
      if (this.mWifiLock != null)
        return;
      if (localINetworkMonitor == null)
        return;
      if (!localINetworkMonitor.hasWifiConnection())
        return;
      if (Build.VERSION.SDK_INT >= 12);
      for (int i = 3; ; i = 1)
      {
        WifiManager.WifiLock localWifiLock = this.mWifiManager.createWifiLock(i, "DownloadQueueManager");
        this.mWifiLock = localWifiLock;
        this.mWifiLock.setReferenceCounted(false);
        this.mWifiLock.acquire();
        return;
      }
    }
    catch (RemoteException localRemoteException)
    {
      String str = localRemoteException.getMessage();
      Log.e("DownloadQueueManager", str, localRemoteException);
    }
  }

  private void afterExecute(DownloadTask paramDownloadTask, boolean paramBoolean)
  {
    releaseWiFiLock();
  }

  private void beforeExecute(DownloadTask paramDownloadTask)
  {
    acquireWifiLock();
  }

  private void failRequests(List<DownloadRequest> paramList, IDownloadProgressListener paramIDownloadProgressListener, int paramInt)
  {
    DownloadState localDownloadState = new DownloadState();
    localDownloadState.setFailedState(paramInt);
    Iterator localIterator = paramList.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      DownloadRequest localDownloadRequest = (DownloadRequest)localIterator.next();
      try
      {
        DownloadProgress localDownloadProgress = new DownloadProgress(localDownloadRequest, localDownloadState);
        paramIDownloadProgressListener.onDownloadProgress(localDownloadProgress);
      }
      catch (RemoteException localRemoteException)
      {
        String str = "Failed to call progress callback for request: " + localDownloadRequest;
        Log.e("DownloadQueueManager", str);
      }
    }
  }

  private void releaseWiFiLock()
  {
    if (this.mWifiLock == null)
      return;
    this.mWifiLock.release();
    this.mWifiLock = null;
  }

  private void runTask(DownloadTask paramDownloadTask)
  {
    try
    {
      beforeExecute(paramDownloadTask);
      paramDownloadTask.run();
      afterExecute(paramDownloadTask, true);
      return;
    }
    finally
    {
      afterExecute(paramDownloadTask, false);
    }
  }

  public void cancelAndPurge(int paramInt1, int paramInt2)
    throws RemoteException
  {
    DownloadQueue localDownloadQueue = this.mDownloadQueue;
    DownloadRequest.Owner localOwner = DownloadRequest.Owner.values()[paramInt1];
    localDownloadQueue.cancelAndPurge(localOwner, paramInt2);
  }

  public void download(List<DownloadRequest> paramList, IDownloadProgressListener paramIDownloadProgressListener, int paramInt)
    throws RemoteException
  {
    if (!this.mNetworkMonitorServiceConnection.waitForServiceConnection(10000L))
    {
      failRequests(paramList, paramIDownloadProgressListener, 11);
      Log.e("DownloadQueueManager", "Failed to connect to network monitor service");
      return;
    }
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      DownloadRequest localDownloadRequest = (DownloadRequest)localIterator.next();
      Context localContext = this.mContext;
      MusicPreferences localMusicPreferences = this.mMusicPreferences;
      GoogleHttpClient localGoogleHttpClient = this.mHttpClient;
      NetworkMonitorServiceConnection localNetworkMonitorServiceConnection = this.mNetworkMonitorServiceConnection;
      IDownloadProgressListener localIDownloadProgressListener = paramIDownloadProgressListener;
      DownloadTaskImpl localDownloadTaskImpl = new DownloadTaskImpl(localContext, localDownloadRequest, localIDownloadProgressListener, localMusicPreferences, localGoogleHttpClient, localNetworkMonitorServiceConnection);
      boolean bool1 = localArrayList.add(localDownloadTaskImpl);
    }
    boolean bool2 = this.mDownloadQueue.addTasks(localArrayList, paramInt);
  }

  protected void finalize()
    throws Throwable
  {
    try
    {
      if (this.mWifiLock != null)
      {
        Throwable localThrowable = new Throwable();
        Log.e("DownloadQueueManager", "The wifi lock was never released... now releasing in finalizer", localThrowable);
        this.mWifiLock.release();
      }
      return;
    }
    finally
    {
      super.finalize();
    }
  }

  public void onDestroy()
  {
    if (this.LOGV)
      Log.d("DownloadQueueManager", "onDestroy()");
    this.mShutdown = true;
    this.mDownloadThread.interrupt();
    NetworkMonitorServiceConnection localNetworkMonitorServiceConnection = this.mNetworkMonitorServiceConnection;
    Context localContext = this.mContext;
    localNetworkMonitorServiceConnection.unbindFromService(localContext);
    MusicPreferences.releaseMusicPreferences(this);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.DownloadQueueManagerImpl
 * JD-Core Version:    0.6.2
 */