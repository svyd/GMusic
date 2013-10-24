package com.google.android.music.net;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.v4.net.ConnectivityManagerCompat;
import android.telephony.TelephonyManager;
import com.google.android.music.download.DownloadUtils;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.async.AsyncWorkers;
import java.util.concurrent.atomic.AtomicInteger;

public class NetworkMonitor extends Service
  implements SharedPreferences.OnSharedPreferenceChangeListener
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private static NetworkMonitor sActiveNetworkMonitor = null;
  private static AtomicInteger sCurrentBitrate = new AtomicInteger(96);
  private int mConnectedMobileDataType;
  private final BroadcastReceiver mConnectionBroadcastReceiver;
  private int mConnectivityType;
  private boolean mIsStreamingAvailable;
  private boolean mMobileOrMeteredConnected;
  private MusicPreferences mMusicPreferences;
  private RemoteCallbackList<INetworkChangeListener> mNetworkChangeListeners;
  private NetworkMonitorBinder mNetworkMonitorBinder;
  private Runnable mNotifiyListenersNetworkChangedRunnable;
  private final Object mStateRecievedLock;
  private RemoteCallbackList<IStreamabilityChangeListener> mStreamabilityListeners;
  private TelephonyManager mTelephonyManager = null;
  private boolean mUnmeteredEthernetConnected;
  private boolean mUnmeteredWifiConnected;
  private final Runnable mUpdateStreamingAvailable;
  private boolean mWifiConnected;

  public NetworkMonitor()
  {
    NetworkMonitorBinder localNetworkMonitorBinder = new NetworkMonitorBinder(null);
    this.mNetworkMonitorBinder = localNetworkMonitorBinder;
    RemoteCallbackList localRemoteCallbackList1 = new RemoteCallbackList();
    this.mStreamabilityListeners = localRemoteCallbackList1;
    RemoteCallbackList localRemoteCallbackList2 = new RemoteCallbackList();
    this.mNetworkChangeListeners = localRemoteCallbackList2;
    this.mMobileOrMeteredConnected = false;
    this.mConnectivityType = -1;
    this.mConnectedMobileDataType = -1;
    this.mUnmeteredWifiConnected = false;
    this.mWifiConnected = false;
    this.mUnmeteredEthernetConnected = false;
    this.mIsStreamingAvailable = false;
    Object localObject = new Object();
    this.mStateRecievedLock = localObject;
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        while (true)
        {
          synchronized (NetworkMonitor.this.mStateRecievedLock)
          {
            if (!paramAnonymousIntent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE"))
              break label628;
            NetworkInfo localNetworkInfo = ConnectivityManagerCompat.getNetworkInfoFromBroadcast((ConnectivityManager)NetworkMonitor.this.getSystemService("connectivity"), paramAnonymousIntent);
            if (localNetworkInfo != null)
            {
              int i = localNetworkInfo.getType();
              String str1 = localNetworkInfo.getTypeName();
              if (NetworkMonitor.LOGV)
              {
                Object[] arrayOfObject = new Object[4];
                String str2 = localNetworkInfo.getTypeName();
                arrayOfObject[0] = str2;
                String str3 = localNetworkInfo.getSubtypeName();
                arrayOfObject[1] = str3;
                String str4 = localNetworkInfo.getReason();
                arrayOfObject[2] = str4;
                Boolean localBoolean = Boolean.valueOf(localNetworkInfo.isConnected());
                arrayOfObject[3] = localBoolean;
                String str5 = String.format("type=%s subType=%s reason=%s isConnected=%b", arrayOfObject);
                Log.f("NetworkMonitor", str5);
              }
              if (DownloadUtils.isSupportedNetworkType(i))
              {
                boolean bool1 = localNetworkInfo.isConnected();
                boolean bool2 = paramAnonymousIntent.getBooleanExtra("noConnectivity", false);
                NetworkMonitor localNetworkMonitor1 = NetworkMonitor.this;
                if ((DownloadUtils.isMobileOrMeteredNetworkType(localNetworkInfo, paramAnonymousContext)) && (!bool2))
                {
                  bool3 = true;
                  boolean bool4 = NetworkMonitor.access$302(localNetworkMonitor1, bool3);
                  localNetworkMonitor1 = NetworkMonitor.this;
                  if ((DownloadUtils.isWifiNetworkType(localNetworkInfo)) && (!bool2))
                  {
                    bool3 = true;
                    boolean bool5 = NetworkMonitor.access$402(localNetworkMonitor1, bool3);
                    localNetworkMonitor1 = NetworkMonitor.this;
                    if ((!DownloadUtils.isUnmeteredWifiNetworkType(localNetworkInfo, paramAnonymousContext)) || (bool2))
                      continue;
                    bool3 = true;
                    boolean bool6 = NetworkMonitor.access$502(localNetworkMonitor1, bool3);
                    localNetworkMonitor1 = NetworkMonitor.this;
                    if ((!DownloadUtils.isUnmeteredEthernetNetworkType(localNetworkInfo, paramAnonymousContext)) || (bool2))
                      continue;
                    bool3 = true;
                    boolean bool7 = NetworkMonitor.access$602(localNetworkMonitor1, bool3);
                    int j = NetworkMonitor.access$702(NetworkMonitor.this, i);
                    NetworkMonitor localNetworkMonitor2 = NetworkMonitor.this;
                    int k = NetworkMonitor.this.mTelephonyManager.getNetworkType();
                    int m = NetworkMonitor.access$802(localNetworkMonitor2, k);
                    if (NetworkMonitor.LOGV)
                    {
                      StringBuilder localStringBuilder1 = new StringBuilder().append("Network changed: type(").append(i).append(") name(").append(str1).append(") isConnected : ").append(bool1).append(" noData: ").append(bool2).append(" bitrate:");
                      int n = NetworkMonitor.sCurrentBitrate.get();
                      String str6 = n;
                      Log.i("NetworkMonitor", str6);
                    }
                    NetworkMonitor.this.processConnectivityChange();
                  }
                }
                else
                {
                  bool3 = false;
                  continue;
                }
                boolean bool3 = false;
                continue;
                bool3 = false;
                continue;
                bool3 = false;
                continue;
              }
              if (!NetworkMonitor.LOGV)
                continue;
              StringBuilder localStringBuilder2 = new StringBuilder().append("Ignore following network type: ").append(i).append(" (").append(str1).append(") - action: ");
              String str7 = paramAnonymousIntent.getAction();
              String str8 = str7;
              Log.i("NetworkMonitor", str8);
            }
          }
          NetworkMonitor localNetworkMonitor3 = NetworkMonitor.this;
          NetworkMonitor localNetworkMonitor4 = NetworkMonitor.this;
          boolean bool8 = NetworkMonitor.access$602(NetworkMonitor.this, false);
          boolean bool9 = NetworkMonitor.access$502(localNetworkMonitor4, bool8);
          boolean bool10 = NetworkMonitor.access$302(localNetworkMonitor3, bool9);
          int i1 = NetworkMonitor.access$702(NetworkMonitor.this, -1);
          NetworkMonitor localNetworkMonitor5 = NetworkMonitor.this;
          int i2 = NetworkMonitor.this.mTelephonyManager.getNetworkType();
          int i3 = NetworkMonitor.access$802(localNetworkMonitor5, i2);
          if (NetworkMonitor.LOGV)
            Log.i("NetworkMonitor", "Network changed: No Connectivity");
          NetworkMonitor.this.processConnectivityChange();
          continue;
          label628: StringBuilder localStringBuilder3 = new StringBuilder().append("Unknown action: ");
          String str9 = paramAnonymousIntent.getAction();
          String str10 = str9;
          Log.wtf("NetworkMonitor", str10);
        }
      }
    };
    this.mConnectionBroadcastReceiver = local1;
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        boolean bool1 = NetworkMonitor.this.getIsStreamingAvailable();
        NetworkMonitor.this.notifyListenersNetworkChanged();
        boolean bool2 = NetworkMonitor.this.mIsStreamingAvailable;
        if (bool1 != bool2)
          return;
        boolean bool3 = NetworkMonitor.access$1402(NetworkMonitor.this, bool1);
        String str1;
        StringBuilder localStringBuilder1;
        if (NetworkMonitor.LOGV)
        {
          str1 = "NetworkMonitor";
          localStringBuilder1 = new StringBuilder().append("Connectivity status changed to (");
          if (!NetworkMonitor.this.mIsStreamingAvailable)
            break label201;
        }
        label201: for (String str2 = "CONNECTED"; ; str2 = "NOT CONNECTED")
        {
          StringBuilder localStringBuilder2 = localStringBuilder1.append(str2).append(") ").append("unmetered wifi: ");
          boolean bool4 = NetworkMonitor.this.mUnmeteredWifiConnected;
          StringBuilder localStringBuilder3 = localStringBuilder2.append(bool4).append(" mobileOrMetered: ");
          boolean bool5 = NetworkMonitor.this.mMobileOrMeteredConnected;
          StringBuilder localStringBuilder4 = localStringBuilder3.append(bool5).append("unmetered ethernet: ");
          boolean bool6 = NetworkMonitor.this.mUnmeteredEthernetConnected;
          String str3 = bool6;
          Log.i(str1, str3);
          NetworkMonitor.this.notifyListenersStreamabilityChanged(bool1);
          ContentResolver localContentResolver = NetworkMonitor.this.getContentResolver();
          Uri localUri = MusicContent.CONTENT_URI;
          localContentResolver.notifyChange(localUri, null, false);
          return;
        }
      }
    };
    this.mUpdateStreamingAvailable = local2;
    Runnable local3 = new Runnable()
    {
      public void run()
      {
        while (true)
        {
          synchronized (NetworkMonitor.this.mNetworkChangeListeners)
          {
            int i = NetworkMonitor.this.mNetworkChangeListeners.beginBroadcast();
            if (i <= 0)
              break;
            i += -1;
            INetworkChangeListener localINetworkChangeListener = (INetworkChangeListener)NetworkMonitor.this.mNetworkChangeListeners.getBroadcastItem(i);
            try
            {
              boolean bool1 = NetworkMonitor.this.mMobileOrMeteredConnected;
              if ((NetworkMonitor.this.mUnmeteredWifiConnected) || (NetworkMonitor.this.mUnmeteredEthernetConnected))
              {
                bool2 = true;
                localINetworkChangeListener.onNetworkChanged(bool1, bool2);
              }
            }
            catch (RemoteException localRemoteException)
            {
              StringBuilder localStringBuilder = new StringBuilder().append("Error trying to notify NetworkChangeListener: ");
              String str1 = localRemoteException.getMessage();
              String str2 = str1;
              Log.e("NetworkMonitor", str2, localRemoteException);
            }
          }
          boolean bool2 = false;
        }
        NetworkMonitor.this.mNetworkChangeListeners.finishBroadcast();
      }
    };
    this.mNotifiyListenersNetworkChangedRunnable = local3;
  }

  private boolean getIsStreamingAvailable()
  {
    if (this.mMusicPreferences.isStreamOnlyOnWifi());
    for (boolean bool = this.mNetworkMonitorBinder.hasHighSpeedConnection(); ; bool = this.mNetworkMonitorBinder.isConnected())
      return bool;
  }

  public static int getRecommendedBitrate()
  {
    return sCurrentBitrate.get();
  }

  private void notifyListenersNetworkChanged()
  {
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable localRunnable = this.mNotifiyListenersNetworkChangedRunnable;
    AsyncWorkers.runAsync(localLoggableHandler, localRunnable);
  }

  private void notifyListenersStreamabilityChanged(boolean paramBoolean)
  {
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    NotifyListenersStreamabilityChangedRunnable localNotifyListenersStreamabilityChangedRunnable = new NotifyListenersStreamabilityChangedRunnable(paramBoolean);
    AsyncWorkers.runAsync(localLoggableHandler, localNotifyListenersStreamabilityChangedRunnable);
  }

  private void processConnectivityChange()
  {
    resetBitrate();
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable localRunnable = this.mUpdateStreamingAvailable;
    AsyncWorkers.runAsync(localLoggableHandler, localRunnable);
  }

  public static void reportBitrate(Context paramContext, long paramLong1, long paramLong2)
  {
    if (paramLong1 < 524288L)
      return;
    if (paramLong2 < 3000L)
      return;
    double d1 = (float)(8L * paramLong1) * 0.75F / 1000.0D;
    double d2 = paramLong2 / 1000.0D;
    int i = Math.max((int)(d1 / d2), 48);
    sCurrentBitrate.set(i);
  }

  private void resetBitrate()
  {
    if (this.mUnmeteredWifiConnected)
    {
      sCurrentBitrate.set(512);
      return;
    }
    if (this.mUnmeteredEthernetConnected)
    {
      sCurrentBitrate.set(512);
      return;
    }
    if (this.mMobileOrMeteredConnected)
    {
      if (this.mConnectivityType == 6)
      {
        sCurrentBitrate.set(256);
        return;
      }
      switch (this.mConnectedMobileDataType)
      {
      default:
        sCurrentBitrate.set(96);
        return;
      case 1:
      case 2:
      case 11:
        sCurrentBitrate.set(64);
        return;
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      case 12:
      case 14:
        sCurrentBitrate.set(192);
        return;
      case 13:
      }
      sCurrentBitrate.set(256);
      return;
    }
    sCurrentBitrate.set(96);
  }

  public IBinder onBind(Intent paramIntent)
  {
    return this.mNetworkMonitorBinder;
  }

  public void onCreate()
  {
    boolean bool1 = true;
    super.onCreate();
    sActiveNetworkMonitor = this;
    TelephonyManager localTelephonyManager = (TelephonyManager)getSystemService("phone");
    this.mTelephonyManager = localTelephonyManager;
    NetworkInfo localNetworkInfo = ((ConnectivityManager)getSystemService("connectivity")).getActiveNetworkInfo();
    int i;
    boolean bool3;
    boolean bool4;
    label146: boolean bool5;
    if (localNetworkInfo != null)
    {
      i = localNetworkInfo.getType();
      boolean bool2 = localNetworkInfo.isConnected();
      if (LOGV)
      {
        String str = "Network Type: " + i + " isConnected: " + bool2;
        Log.i("NetworkMonitor", str);
      }
      if (DownloadUtils.isSupportedNetworkType(i))
      {
        if ((!DownloadUtils.isMobileOrMeteredNetworkType(localNetworkInfo, this)) || (!bool2))
          break label278;
        bool3 = true;
        this.mMobileOrMeteredConnected = bool3;
        if ((!DownloadUtils.isWifiNetworkType(localNetworkInfo)) || (!bool2))
          break label284;
        bool4 = true;
        this.mWifiConnected = bool4;
        if ((!DownloadUtils.isUnmeteredWifiNetworkType(localNetworkInfo, this)) || (!bool2))
          break label290;
        bool5 = true;
        label168: this.mUnmeteredWifiConnected = bool5;
        if ((!DownloadUtils.isUnmeteredEthernetNetworkType(localNetworkInfo, this)) || (!bool2))
          break label296;
      }
    }
    while (true)
    {
      this.mUnmeteredEthernetConnected = bool1;
      this.mConnectivityType = i;
      int j = this.mTelephonyManager.getNetworkType();
      this.mConnectedMobileDataType = j;
      resetBitrate();
      MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this, this);
      this.mMusicPreferences = localMusicPreferences;
      this.mMusicPreferences.registerOnSharedPreferenceChangeListener(this);
      onSharedPreferenceChanged(null, null);
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
      BroadcastReceiver localBroadcastReceiver = this.mConnectionBroadcastReceiver;
      Intent localIntent = registerReceiver(localBroadcastReceiver, localIntentFilter);
      return;
      label278: bool3 = false;
      break;
      label284: bool4 = false;
      break label146;
      label290: bool5 = false;
      break label168;
      label296: bool1 = false;
    }
  }

  public void onDestroy()
  {
    sActiveNetworkMonitor = null;
    super.onDestroy();
    MusicPreferences.releaseMusicPreferences(this);
    BroadcastReceiver localBroadcastReceiver = this.mConnectionBroadcastReceiver;
    unregisterReceiver(localBroadcastReceiver);
  }

  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString)
  {
    synchronized (this.mStateRecievedLock)
    {
      boolean bool1 = getIsStreamingAvailable();
      boolean bool2 = this.mIsStreamingAvailable;
      if (bool1 != bool2)
        return;
      this.mIsStreamingAvailable = bool1;
      notifyListenersStreamabilityChanged(bool1);
      return;
    }
  }

  private class NetworkMonitorBinder extends INetworkMonitor.Stub
  {
    private NetworkMonitorBinder()
    {
    }

    public boolean hasHighSpeedConnection()
    {
      if ((NetworkMonitor.this.mUnmeteredWifiConnected) || (NetworkMonitor.this.mUnmeteredEthernetConnected));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean hasMobileOrMeteredConnection()
    {
      return NetworkMonitor.this.mMobileOrMeteredConnected;
    }

    public boolean hasWifiConnection()
    {
      return NetworkMonitor.this.mWifiConnected;
    }

    public boolean isConnected()
    {
      if ((NetworkMonitor.this.mMobileOrMeteredConnected) || (NetworkMonitor.this.mUnmeteredWifiConnected) || (NetworkMonitor.this.mUnmeteredEthernetConnected));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean isStreamingAvailable()
    {
      return NetworkMonitor.this.mIsStreamingAvailable;
    }

    // ERROR //
    public void registerNetworkChangeListener(INetworkChangeListener paramINetworkChangeListener)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 14	com/google/android/music/net/NetworkMonitor$NetworkMonitorBinder:this$0	Lcom/google/android/music/net/NetworkMonitor;
      //   4: invokestatic 50	com/google/android/music/net/NetworkMonitor:access$1700	(Lcom/google/android/music/net/NetworkMonitor;)Landroid/os/RemoteCallbackList;
      //   7: istore_2
      //   8: iload_2
      //   9: monitorenter
      //   10: aload_0
      //   11: getfield 14	com/google/android/music/net/NetworkMonitor$NetworkMonitorBinder:this$0	Lcom/google/android/music/net/NetworkMonitor;
      //   14: invokestatic 50	com/google/android/music/net/NetworkMonitor:access$1700	(Lcom/google/android/music/net/NetworkMonitor;)Landroid/os/RemoteCallbackList;
      //   17: aload_1
      //   18: invokevirtual 56	android/os/RemoteCallbackList:register	(Landroid/os/IInterface;)Z
      //   21: istore_3
      //   22: iload_2
      //   23: monitorexit
      //   24: aload_0
      //   25: getfield 14	com/google/android/music/net/NetworkMonitor$NetworkMonitorBinder:this$0	Lcom/google/android/music/net/NetworkMonitor;
      //   28: invokestatic 33	com/google/android/music/net/NetworkMonitor:access$300	(Lcom/google/android/music/net/NetworkMonitor;)Z
      //   31: istore_2
      //   32: aload_0
      //   33: getfield 14	com/google/android/music/net/NetworkMonitor$NetworkMonitorBinder:this$0	Lcom/google/android/music/net/NetworkMonitor;
      //   36: invokestatic 26	com/google/android/music/net/NetworkMonitor:access$500	(Lcom/google/android/music/net/NetworkMonitor;)Z
      //   39: ifne +13 -> 52
      //   42: aload_0
      //   43: getfield 14	com/google/android/music/net/NetworkMonitor$NetworkMonitorBinder:this$0	Lcom/google/android/music/net/NetworkMonitor;
      //   46: invokestatic 29	com/google/android/music/net/NetworkMonitor:access$600	(Lcom/google/android/music/net/NetworkMonitor;)Z
      //   49: ifeq +23 -> 72
      //   52: iconst_1
      //   53: istore 4
      //   55: aload_1
      //   56: iload_2
      //   57: iload 4
      //   59: invokeinterface 62 3 0
      //   64: return
      //   65: astore 4
      //   67: iload_2
      //   68: monitorexit
      //   69: aload 4
      //   71: athrow
      //   72: iconst_0
      //   73: istore 4
      //   75: goto -20 -> 55
      //   78: astore 5
      //   80: aload 5
      //   82: invokevirtual 66	android/os/RemoteException:getMessage	()Ljava/lang/String;
      //   85: astore 6
      //   87: ldc 68
      //   89: aload 6
      //   91: aload 5
      //   93: invokestatic 74	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
      //   96: return
      //
      // Exception table:
      //   from	to	target	type
      //   10	24	65	finally
      //   67	69	65	finally
      //   24	64	78	android/os/RemoteException
    }

    public void registerStreamabilityChangeListener(IStreamabilityChangeListener paramIStreamabilityChangeListener)
    {
      synchronized (NetworkMonitor.this.mStreamabilityListeners)
      {
        boolean bool1 = NetworkMonitor.this.mStreamabilityListeners.register(paramIStreamabilityChangeListener);
      }
      try
      {
        boolean bool2 = NetworkMonitor.this.mIsStreamingAvailable;
        paramIStreamabilityChangeListener.onStreamabilityChanged(bool2);
        return;
        localObject = finally;
        throw localObject;
      }
      catch (RemoteException localRemoteException)
      {
        String str = localRemoteException.getMessage();
        Log.w("NetworkMonitor", str, localRemoteException);
      }
    }

    public void unregisterNetworkChangeListener(INetworkChangeListener paramINetworkChangeListener)
    {
      synchronized (NetworkMonitor.this.mNetworkChangeListeners)
      {
        boolean bool = NetworkMonitor.this.mNetworkChangeListeners.unregister(paramINetworkChangeListener);
        return;
      }
    }

    public void unregisterStreamabilityChangeListener(IStreamabilityChangeListener paramIStreamabilityChangeListener)
    {
      synchronized (NetworkMonitor.this.mStreamabilityListeners)
      {
        boolean bool = NetworkMonitor.this.mStreamabilityListeners.unregister(paramIStreamabilityChangeListener);
        return;
      }
    }
  }

  private class NotifyListenersStreamabilityChangedRunnable
    implements Runnable
  {
    private final boolean mIsStreamable;

    public NotifyListenersStreamabilityChangedRunnable(boolean arg2)
    {
      boolean bool;
      this.mIsStreamable = bool;
    }

    public void run()
    {
      synchronized (NetworkMonitor.this.mStreamabilityListeners)
      {
        int i = NetworkMonitor.this.mStreamabilityListeners.beginBroadcast();
        while (true)
          if (i > 0)
          {
            i += -1;
            IStreamabilityChangeListener localIStreamabilityChangeListener = (IStreamabilityChangeListener)NetworkMonitor.this.mStreamabilityListeners.getBroadcastItem(i);
            try
            {
              boolean bool = this.mIsStreamable;
              localIStreamabilityChangeListener.onStreamabilityChanged(bool);
            }
            catch (RemoteException localRemoteException)
            {
              StringBuilder localStringBuilder = new StringBuilder().append("Error trying to notify StreamabilityListener: ");
              String str1 = localRemoteException.getMessage();
              String str2 = str1;
              Log.e("NetworkMonitor", str2, localRemoteException);
            }
          }
      }
      NetworkMonitor.this.mStreamabilityListeners.finishBroadcast();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.net.NetworkMonitor
 * JD-Core Version:    0.6.2
 */