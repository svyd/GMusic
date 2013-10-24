package com.google.android.music.net;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.music.utils.SafeServiceConnection;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class NetworkMonitorServiceConnection
{
  private final INetworkChangeListener mNetworkChangeListener;
  private INetworkMonitor mNetworkMonitor = null;
  private List<Runnable> mRunOnServiceConnected = null;
  private SafeServiceConnection mSafeServiceConnection;
  private final IStreamabilityChangeListener mStreamabilityChangeListener;

  public NetworkMonitorServiceConnection()
  {
    this(null, null);
  }

  public NetworkMonitorServiceConnection(INetworkChangeListener paramINetworkChangeListener)
  {
    this(paramINetworkChangeListener, null);
  }

  public NetworkMonitorServiceConnection(INetworkChangeListener paramINetworkChangeListener, IStreamabilityChangeListener paramIStreamabilityChangeListener)
  {
    SafeServiceConnection local1 = new SafeServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        synchronized (NetworkMonitorServiceConnection.this)
        {
          NetworkMonitorServiceConnection localNetworkMonitorServiceConnection2 = NetworkMonitorServiceConnection.this;
          INetworkMonitor localINetworkMonitor1 = INetworkMonitor.Stub.asInterface(paramAnonymousIBinder);
          INetworkMonitor localINetworkMonitor2 = NetworkMonitorServiceConnection.access$002(localNetworkMonitorServiceConnection2, localINetworkMonitor1);
          NetworkMonitorServiceConnection.this.registerListeners();
          if (NetworkMonitorServiceConnection.this.mRunOnServiceConnected != null)
          {
            Iterator localIterator = NetworkMonitorServiceConnection.this.mRunOnServiceConnected.iterator();
            if (localIterator.hasNext())
              ((Runnable)localIterator.next()).run();
          }
        }
        List localList = NetworkMonitorServiceConnection.access$102(NetworkMonitorServiceConnection.this, null);
        NetworkMonitorServiceConnection.this.notifyAll();
      }

      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
        synchronized (NetworkMonitorServiceConnection.this)
        {
          INetworkMonitor localINetworkMonitor = NetworkMonitorServiceConnection.access$002(NetworkMonitorServiceConnection.this, null);
          return;
        }
      }
    };
    this.mSafeServiceConnection = local1;
    this.mNetworkChangeListener = paramINetworkChangeListener;
    this.mStreamabilityChangeListener = paramIStreamabilityChangeListener;
  }

  public NetworkMonitorServiceConnection(IStreamabilityChangeListener paramIStreamabilityChangeListener)
  {
    this(null, paramIStreamabilityChangeListener);
  }

  private static Intent getBindIntent()
  {
    return new Intent("com.google.android.music.NETWORK_MONITOR_SERVICE");
  }

  /** @deprecated */
  public void addRunOnServiceConnected(Runnable paramRunnable)
  {
    try
    {
      if (this.mNetworkMonitor != null)
        paramRunnable.run();
      while (true)
      {
        return;
        if (this.mRunOnServiceConnected == null)
        {
          LinkedList localLinkedList = Lists.newLinkedList();
          this.mRunOnServiceConnected = localLinkedList;
        }
        boolean bool = this.mRunOnServiceConnected.add(paramRunnable);
      }
    }
    finally
    {
    }
  }

  public void bindToService(Context paramContext)
  {
    SafeServiceConnection localSafeServiceConnection = this.mSafeServiceConnection;
    Intent localIntent = getBindIntent();
    boolean bool = localSafeServiceConnection.bindService(paramContext, localIntent, 1);
  }

  protected void finalize()
    throws Throwable
  {
    if (this.mNetworkMonitor != null)
    {
      Throwable localThrowable = new Throwable();
      int i = Log.e("NetworkMonitor", "NetworkMonitorServiceConnection not unbinded cleanly", localThrowable);
    }
    super.finalize();
  }

  public INetworkMonitor getNetworkMonitor()
  {
    return this.mNetworkMonitor;
  }

  /** @deprecated */
  public void registerListeners()
  {
    try
    {
      if (this.mNetworkMonitor == null)
        int i = Log.w("NetworkMonitor", "registerListeners: networkMonitor was never connected");
      while (true)
      {
        return;
        try
        {
          if (this.mNetworkChangeListener != null)
          {
            INetworkMonitor localINetworkMonitor1 = this.mNetworkMonitor;
            INetworkChangeListener localINetworkChangeListener = this.mNetworkChangeListener;
            localINetworkMonitor1.registerNetworkChangeListener(localINetworkChangeListener);
          }
          if (this.mStreamabilityChangeListener != null)
          {
            INetworkMonitor localINetworkMonitor2 = this.mNetworkMonitor;
            IStreamabilityChangeListener localIStreamabilityChangeListener = this.mStreamabilityChangeListener;
            localINetworkMonitor2.registerStreamabilityChangeListener(localIStreamabilityChangeListener);
          }
        }
        catch (RemoteException localRemoteException)
        {
          String str = localRemoteException.getMessage();
          int j = Log.e("NetworkMonitor", str, localRemoteException);
        }
      }
    }
    finally
    {
    }
  }

  public void unbindFromService(Context paramContext)
  {
    unregisterListeners();
    this.mSafeServiceConnection.unbindService(paramContext);
    this.mNetworkMonitor = null;
  }

  /** @deprecated */
  public void unregisterListeners()
  {
    try
    {
      if (this.mNetworkMonitor == null)
        int i = Log.w("NetworkMonitor", "unregisterListeners: networkMonitor was never connected");
      while (true)
      {
        return;
        try
        {
          if (this.mNetworkChangeListener != null)
          {
            INetworkMonitor localINetworkMonitor1 = this.mNetworkMonitor;
            INetworkChangeListener localINetworkChangeListener = this.mNetworkChangeListener;
            localINetworkMonitor1.unregisterNetworkChangeListener(localINetworkChangeListener);
          }
          if (this.mStreamabilityChangeListener != null)
          {
            INetworkMonitor localINetworkMonitor2 = this.mNetworkMonitor;
            IStreamabilityChangeListener localIStreamabilityChangeListener = this.mStreamabilityChangeListener;
            localINetworkMonitor2.unregisterStreamabilityChangeListener(localIStreamabilityChangeListener);
          }
        }
        catch (RemoteException localRemoteException)
        {
          String str = localRemoteException.getMessage();
          int j = Log.e("NetworkMonitor", str, localRemoteException);
        }
      }
    }
    finally
    {
    }
  }

  public boolean waitForServiceConnection(long paramLong)
  {
    while (true)
    {
      try
      {
        localINetworkMonitor = this.mNetworkMonitor;
        if (localINetworkMonitor == null);
        try
        {
          wait(paramLong);
          if (this.mNetworkMonitor != null)
          {
            localINetworkMonitor = null;
            return localINetworkMonitor;
          }
        }
        catch (InterruptedException localInterruptedException)
        {
          String str = localInterruptedException.getMessage();
          int i = Log.w("NetworkMonitor", str);
          continue;
        }
      }
      finally
      {
      }
      INetworkMonitor localINetworkMonitor = null;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.net.NetworkMonitorServiceConnection
 * JD-Core Version:    0.6.2
 */