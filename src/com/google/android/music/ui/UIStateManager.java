package com.google.android.music.ui;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap.Config;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.Display;
import android.view.WindowManager;
import com.google.android.music.NautilusStatus;
import com.google.android.music.download.artwork.AlbumArtDownloadServiceConnection;
import com.google.android.music.download.artwork.CachedArtDownloadServiceConnection;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.log.Log;
import com.google.android.music.net.INetworkChangeListener;
import com.google.android.music.net.INetworkChangeListener.Stub;
import com.google.android.music.net.INetworkMonitor;
import com.google.android.music.net.IStreamabilityChangeListener;
import com.google.android.music.net.IStreamabilityChangeListener.Stub;
import com.google.android.music.net.NetworkMonitorServiceConnection;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.IStoreService;
import com.google.android.music.store.IStoreService.Stub;
import com.google.android.music.store.MediaStoreImportService;
import com.google.android.music.utils.AlbumArtUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.MusicUtils.ServiceToken;
import com.google.android.music.utils.SafeServiceConnection;
import com.google.android.music.utils.ViewUtils;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class UIStateManager
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.UI);
  private static UIStateManager sInstance;
  private final BroadcastReceiver mAccountReceiver;
  private final AlbumArtDownloadServiceConnection mAlbumArtDownloadServiceConnection;
  private final Context mApplicationContext;
  private final CachedArtDownloadServiceConnection mCachedArtDownloadServiceConnection;
  private NautilusStatus mCachedNautilusStatus;
  private Account mCachedSelectedAccount;
  private final Point mCurrentScreenSize;
  private boolean mCurrentUIVisibility;
  private final MusicEventLogger mEventLogger;
  private final Handler mHandler;
  private boolean mIsMobileConnected;
  private boolean mIsStreamingEnabled;
  private boolean mIsWifiOrEthernetConnected;
  private final Point mLargestScreenSize;
  private Runnable mMarkUIInvisibileRunnable;
  private Runnable mMarkUIVisibileRunnable;
  private SafeServiceConnection mMediaStoreImportSafeConnection;
  private final INetworkChangeListener mNetworkChangeListener;
  Collection<NetworkChangeListener> mNetworkChangeListeners;
  private final NetworkMonitorServiceConnection mNetworkMonitorServiceConnection;
  private final ServiceConnection mPlaybackServiceConnection;
  private final MusicUtils.ServiceToken mPlaybackServiceToken;
  private final SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener;
  private boolean mPreferenceChangeListenerRegistered;
  private final MusicPreferences mPrefs;
  private List<Runnable> mRunOnPlaybackServiceConnected;
  private final Point mSmallestScreenSize;
  private final List<UIStateChangeListener> mStateChangeListeners;
  private SafeServiceConnection mStoreSafeConnection;
  private IStoreService mStoreService;
  private final IStreamabilityChangeListener mStreamabilityChangeListener;
  Collection<StreamabilityChangeListener> mStreamabilityChangeListeners;

  private UIStateManager(Context paramContext)
  {
    Point localPoint1 = new Point();
    this.mSmallestScreenSize = localPoint1;
    Point localPoint2 = new Point();
    this.mLargestScreenSize = localPoint2;
    Point localPoint3 = new Point();
    this.mCurrentScreenSize = localPoint3;
    LinkedList localLinkedList1 = Lists.newLinkedList();
    this.mStateChangeListeners = localLinkedList1;
    this.mRunOnPlaybackServiceConnected = null;
    LinkedList localLinkedList2 = Lists.newLinkedList();
    this.mStreamabilityChangeListeners = localLinkedList2;
    LinkedList localLinkedList3 = Lists.newLinkedList();
    this.mNetworkChangeListeners = localLinkedList3;
    ServiceConnection local1 = new ServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        if (UIStateManager.LOGV)
          Log.i("UIStateManager", "Connected to playback service");
        if (UIStateManager.this.mRunOnPlaybackServiceConnected != null)
        {
          Iterator localIterator = UIStateManager.this.mRunOnPlaybackServiceConnected.iterator();
          while (localIterator.hasNext())
            ((Runnable)localIterator.next()).run();
        }
        List localList = UIStateManager.access$102(UIStateManager.this, null);
      }

      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
        Log.e("UIStateManager", "Disconnected from playback service");
      }
    };
    this.mPlaybackServiceConnection = local1;
    IStreamabilityChangeListener.Stub local2 = new IStreamabilityChangeListener.Stub()
    {
      public void onStreamabilityChanged(final boolean paramAnonymousBoolean)
        throws RemoteException
      {
        Handler localHandler = UIStateManager.this.mHandler;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            boolean bool1 = UIStateManager.this.mIsStreamingEnabled;
            boolean bool2 = paramAnonymousBoolean;
            if (bool1 != bool2)
              return;
            UIStateManager localUIStateManager = UIStateManager.this;
            boolean bool3 = paramAnonymousBoolean;
            boolean bool4 = UIStateManager.access$202(localUIStateManager, bool3);
            UIStateManager.this.notifyStreamabilityChanged();
          }
        };
        boolean bool = localHandler.post(local1);
      }
    };
    this.mStreamabilityChangeListener = local2;
    INetworkChangeListener.Stub local3 = new INetworkChangeListener.Stub()
    {
      public void onNetworkChanged(final boolean paramAnonymousBoolean1, final boolean paramAnonymousBoolean2)
        throws RemoteException
      {
        Handler localHandler = UIStateManager.this.mHandler;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            boolean bool1 = UIStateManager.this.mIsMobileConnected;
            boolean bool2 = paramAnonymousBoolean1;
            if (bool1 != bool2)
            {
              boolean bool3 = UIStateManager.this.mIsWifiOrEthernetConnected;
              boolean bool4 = paramAnonymousBoolean2;
              if (bool3 != bool4)
                return;
            }
            UIStateManager localUIStateManager1 = UIStateManager.this;
            boolean bool5 = paramAnonymousBoolean1;
            boolean bool6 = UIStateManager.access$502(localUIStateManager1, bool5);
            UIStateManager localUIStateManager2 = UIStateManager.this;
            boolean bool7 = paramAnonymousBoolean2;
            boolean bool8 = UIStateManager.access$602(localUIStateManager2, bool7);
            UIStateManager.this.notifyNetworkChanged();
          }
        };
        boolean bool = localHandler.post(local1);
      }
    };
    this.mNetworkChangeListener = local3;
    SafeServiceConnection local4 = new SafeServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
      }

      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
      }
    };
    this.mMediaStoreImportSafeConnection = local4;
    SafeServiceConnection local5 = new SafeServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        UIStateManager localUIStateManager = UIStateManager.this;
        IStoreService localIStoreService1 = IStoreService.Stub.asInterface(paramAnonymousIBinder);
        IStoreService localIStoreService2 = UIStateManager.access$802(localUIStateManager, localIStoreService1);
      }

      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
        IStoreService localIStoreService = UIStateManager.access$802(UIStateManager.this, null);
      }
    };
    this.mStoreSafeConnection = local5;
    this.mCurrentUIVisibility = false;
    Runnable local6 = new Runnable()
    {
      public void run()
      {
        UIStateManager.this.setUIVisibility(true);
      }
    };
    this.mMarkUIVisibileRunnable = local6;
    Runnable local7 = new Runnable()
    {
      public void run()
      {
        UIStateManager.this.setUIVisibility(false);
      }
    };
    this.mMarkUIInvisibileRunnable = local7;
    SharedPreferences.OnSharedPreferenceChangeListener local8 = new SharedPreferences.OnSharedPreferenceChangeListener()
    {
      public void onSharedPreferenceChanged(SharedPreferences paramAnonymousSharedPreferences, String paramAnonymousString)
      {
        UIStateManager.this.refreshAccountStatus();
      }
    };
    this.mPreferenceChangeListener = local8;
    BroadcastReceiver local9 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        UIStateManager.this.refreshAccountStatus();
      }
    };
    this.mAccountReceiver = local9;
    MusicUtils.assertUiThread();
    MusicUtils.assertUiProcess(paramContext, "UIStateManager should be initialized in UI process only");
    Context localContext1 = paramContext.getApplicationContext();
    this.mApplicationContext = localContext1;
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this.mApplicationContext);
    this.mEventLogger = localMusicEventLogger;
    Handler localHandler = new Handler();
    this.mHandler = localHandler;
    MusicPreferences localMusicPreferences1 = MusicPreferences.getMusicPreferences(this.mApplicationContext, this);
    this.mPrefs = localMusicPreferences1;
    ServiceConnection localServiceConnection = this.mPlaybackServiceConnection;
    MusicUtils.ServiceToken localServiceToken = MusicUtils.bindToService(paramContext, localServiceConnection);
    this.mPlaybackServiceToken = localServiceToken;
    INetworkChangeListener localINetworkChangeListener = this.mNetworkChangeListener;
    IStreamabilityChangeListener localIStreamabilityChangeListener = this.mStreamabilityChangeListener;
    NetworkMonitorServiceConnection localNetworkMonitorServiceConnection1 = new NetworkMonitorServiceConnection(localINetworkChangeListener, localIStreamabilityChangeListener);
    this.mNetworkMonitorServiceConnection = localNetworkMonitorServiceConnection1;
    NetworkMonitorServiceConnection localNetworkMonitorServiceConnection2 = this.mNetworkMonitorServiceConnection;
    Context localContext2 = this.mApplicationContext;
    localNetworkMonitorServiceConnection2.bindToService(localContext2);
    AlbumArtUtils.setPreferredConfig(Bitmap.Config.RGB_565);
    AlbumArtDownloadServiceConnection localAlbumArtDownloadServiceConnection1 = new AlbumArtDownloadServiceConnection();
    this.mAlbumArtDownloadServiceConnection = localAlbumArtDownloadServiceConnection1;
    AlbumArtDownloadServiceConnection localAlbumArtDownloadServiceConnection2 = this.mAlbumArtDownloadServiceConnection;
    Context localContext3 = this.mApplicationContext;
    localAlbumArtDownloadServiceConnection2.onCreate(localContext3);
    CachedArtDownloadServiceConnection localCachedArtDownloadServiceConnection1 = new CachedArtDownloadServiceConnection();
    this.mCachedArtDownloadServiceConnection = localCachedArtDownloadServiceConnection1;
    CachedArtDownloadServiceConnection localCachedArtDownloadServiceConnection2 = this.mCachedArtDownloadServiceConnection;
    Context localContext4 = this.mApplicationContext;
    localCachedArtDownloadServiceConnection2.onCreate(localContext4);
    MusicPreferences localMusicPreferences2 = this.mPrefs;
    SharedPreferences.OnSharedPreferenceChangeListener localOnSharedPreferenceChangeListener = this.mPreferenceChangeListener;
    localMusicPreferences2.registerOnSharedPreferenceChangeListener(localOnSharedPreferenceChangeListener);
    this.mPreferenceChangeListenerRegistered = true;
    WindowManager localWindowManager = (WindowManager)paramContext.getSystemService("window");
    if (localWindowManager != null)
    {
      Display localDisplay = localWindowManager.getDefaultDisplay();
      Point localPoint4 = this.mSmallestScreenSize;
      Point localPoint5 = this.mLargestScreenSize;
      ViewUtils.getCurrentSizeRange(localDisplay, localPoint4, localPoint5);
    }
    SafeServiceConnection localSafeServiceConnection1 = this.mMediaStoreImportSafeConnection;
    Context localContext5 = this.mApplicationContext;
    Context localContext6 = this.mApplicationContext;
    Intent localIntent1 = new Intent(localContext6, MediaStoreImportService.class);
    boolean bool1 = localSafeServiceConnection1.bindService(localContext5, localIntent1, 1);
    SafeServiceConnection localSafeServiceConnection2 = this.mStoreSafeConnection;
    Context localContext7 = this.mApplicationContext;
    Intent localIntent2 = new Intent("com.google.android.music.STORE_SERVICE");
    boolean bool2 = localSafeServiceConnection2.bindService(localContext7, localIntent2, 1);
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.google.android.music.accountchanged");
    localIntentFilter.addAction("android.accounts.LOGIN_ACCOUNTS_CHANGED");
    BroadcastReceiver localBroadcastReceiver = this.mAccountReceiver;
    Intent localIntent3 = paramContext.registerReceiver(localBroadcastReceiver, localIntentFilter);
  }

  public static UIStateManager getInstance()
    throws IllegalStateException
  {
    if (sInstance == null)
      throw new IllegalStateException("App state is not initialized");
    return sInstance;
  }

  public static UIStateManager getInstance(Context paramContext)
  {
    if (sInstance == null)
      sInstance = new UIStateManager(paramContext);
    return sInstance;
  }

  public static boolean isInitialized()
  {
    if (sInstance != null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private void notifyNetworkChanged()
  {
    Iterator localIterator = Lists.newArrayList(this.mNetworkChangeListeners).iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      NetworkChangeListener localNetworkChangeListener = (NetworkChangeListener)localIterator.next();
      boolean bool1 = this.mIsMobileConnected;
      boolean bool2 = this.mIsWifiOrEthernetConnected;
      localNetworkChangeListener.onNetworkChanged(bool1, bool2);
    }
  }

  private void notifyStreamabilityChanged()
  {
    Iterator localIterator = Lists.newArrayList(this.mStreamabilityChangeListeners).iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      StreamabilityChangeListener localStreamabilityChangeListener = (StreamabilityChangeListener)localIterator.next();
      boolean bool = this.mIsStreamingEnabled;
      localStreamabilityChangeListener.onStreamabilityChanged(bool);
    }
  }

  private void refreshAccountStatus()
  {
    Handler localHandler = this.mHandler;
    Runnable local10 = new Runnable()
    {
      public void run()
      {
        NautilusStatus localNautilusStatus1 = UIStateManager.this.mPrefs.getNautilusStatus();
        Account localAccount1 = UIStateManager.this.mPrefs.getSyncAccount();
        if (UIStateManager.this.mCachedNautilusStatus == localNautilusStatus1)
        {
          Account localAccount2 = UIStateManager.this.mCachedSelectedAccount;
          if (MusicUtils.safeEquals(localAccount1, localAccount2))
            return;
        }
        if (UIStateManager.LOGV)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("Nautilus status changed ");
          NautilusStatus localNautilusStatus2 = UIStateManager.this.mCachedNautilusStatus;
          String str = localNautilusStatus2 + " to " + localNautilusStatus1;
          Log.v("UIStateManager", str);
        }
        NautilusStatus localNautilusStatus3 = UIStateManager.access$1202(UIStateManager.this, localNautilusStatus1);
        Account localAccount3 = UIStateManager.access$1302(UIStateManager.this, localAccount1);
        Iterator localIterator = UIStateManager.this.mStateChangeListeners.iterator();
        while (true)
        {
          if (!localIterator.hasNext())
            return;
          UIStateManager.UIStateChangeListener localUIStateChangeListener = (UIStateManager.UIStateChangeListener)localIterator.next();
          Account localAccount4 = UIStateManager.this.mCachedSelectedAccount;
          NautilusStatus localNautilusStatus4 = UIStateManager.this.mCachedNautilusStatus;
          localUIStateChangeListener.onAccountStatusUpdate(localAccount4, localNautilusStatus4);
        }
      }
    };
    boolean bool = localHandler.post(local10);
  }

  private void setUIVisibility(boolean paramBoolean)
  {
    if (this.mCurrentUIVisibility != paramBoolean)
      return;
    try
    {
      if (MusicUtils.sService != null)
      {
        MusicUtils.sService.setUIVisible(paramBoolean);
        this.mCurrentUIVisibility = paramBoolean;
        return;
      }
    }
    catch (RemoteException localRemoteException)
    {
      String str = localRemoteException.getMessage();
      Log.w("UIStateManager", str, localRemoteException);
      return;
    }
    Handler localHandler = this.mHandler;
    if (paramBoolean);
    for (Runnable localRunnable = this.mMarkUIVisibileRunnable; ; localRunnable = this.mMarkUIInvisibileRunnable)
    {
      boolean bool = localHandler.postDelayed(localRunnable, 100L);
      return;
    }
  }

  public void addRunOnPlaybackServiceConnected(Runnable paramRunnable)
  {
    if (MusicUtils.sService != null)
    {
      paramRunnable.run();
      return;
    }
    if (this.mRunOnPlaybackServiceConnected == null)
    {
      LinkedList localLinkedList = Lists.newLinkedList();
      this.mRunOnPlaybackServiceConnected = localLinkedList;
    }
    boolean bool = this.mRunOnPlaybackServiceConnected.add(paramRunnable);
  }

  public final AlbumArtDownloadServiceConnection getAlbumArtDownloadServiceConnection()
  {
    return this.mAlbumArtDownloadServiceConnection;
  }

  public final CachedArtDownloadServiceConnection getCachedArtDownloadServiceConnection()
  {
    return this.mCachedArtDownloadServiceConnection;
  }

  public MusicPreferences getPrefs()
  {
    return this.mPrefs;
  }

  public int getScreenWidth()
  {
    WindowManager localWindowManager = (WindowManager)this.mApplicationContext.getSystemService("window");
    if (localWindowManager != null)
    {
      Display localDisplay = localWindowManager.getDefaultDisplay();
      Point localPoint = this.mCurrentScreenSize;
      ViewUtils.getSize(localDisplay, localPoint);
    }
    return this.mCurrentScreenSize.x;
  }

  public IStoreService getStoreService()
  {
    return this.mStoreService;
  }

  public boolean isDisplayingLocalContent()
  {
    int i = 1;
    if (this.mPrefs.getDisplayOptions() != i);
    while (true)
    {
      return i;
      int j = 0;
    }
  }

  public boolean isMobileConnected()
  {
    return this.mIsMobileConnected;
  }

  public boolean isNetworkConnected()
  {
    boolean bool1 = false;
    INetworkMonitor localINetworkMonitor;
    if (this.mNetworkMonitorServiceConnection != null)
    {
      localINetworkMonitor = this.mNetworkMonitorServiceConnection.getNetworkMonitor();
      if (localINetworkMonitor == null);
    }
    try
    {
      boolean bool2 = localINetworkMonitor.isConnected();
      bool1 = bool2;
      return bool1;
    }
    catch (RemoteException localRemoteException)
    {
      while (true)
        Log.e("UIStateManager", "", localRemoteException);
    }
  }

  public boolean isStreamingEnabled()
  {
    return this.mIsStreamingEnabled;
  }

  public boolean isWifiOrEthernetConnected()
  {
    return this.mIsWifiOrEthernetConnected;
  }

  public void onConfigChange(int paramInt)
  {
    if (paramInt != 1)
      return;
    refreshAccountStatus();
  }

  public void onPause()
  {
    Handler localHandler = this.mHandler;
    Runnable localRunnable = this.mMarkUIInvisibileRunnable;
    boolean bool = localHandler.postDelayed(localRunnable, 300L);
  }

  public void onResume()
  {
    Handler localHandler1 = this.mHandler;
    Runnable localRunnable1 = this.mMarkUIInvisibileRunnable;
    localHandler1.removeCallbacks(localRunnable1);
    Handler localHandler2 = this.mHandler;
    Runnable localRunnable2 = this.mMarkUIVisibileRunnable;
    boolean bool = localHandler2.post(localRunnable2);
  }

  public void registerNetworkChangeListener(NetworkChangeListener paramNetworkChangeListener)
  {
    boolean bool = this.mNetworkChangeListeners.add(paramNetworkChangeListener);
  }

  public void registerStreamabilityChangeListener(StreamabilityChangeListener paramStreamabilityChangeListener)
  {
    boolean bool = this.mStreamabilityChangeListeners.add(paramStreamabilityChangeListener);
  }

  public void registerUIStateChangeListener(UIStateChangeListener paramUIStateChangeListener)
  {
    MusicUtils.assertUiThread();
    boolean bool = this.mStateChangeListeners.add(paramUIStateChangeListener);
  }

  public void removeRunOnPlaybackServiceConnected(Runnable paramRunnable)
  {
    if (this.mRunOnPlaybackServiceConnected == null)
      return;
    while (this.mRunOnPlaybackServiceConnected.remove(paramRunnable));
  }

  public void unregisterNetworkChangeListener(NetworkChangeListener paramNetworkChangeListener)
  {
    boolean bool = this.mNetworkChangeListeners.remove(paramNetworkChangeListener);
  }

  public void unregisterStreamabilityChangeListener(StreamabilityChangeListener paramStreamabilityChangeListener)
  {
    boolean bool = this.mStreamabilityChangeListeners.remove(paramStreamabilityChangeListener);
  }

  public void unregisterUIStateChangeListener(UIStateChangeListener paramUIStateChangeListener)
  {
    MusicUtils.assertUiThread();
    boolean bool = this.mStateChangeListeners.remove(paramUIStateChangeListener);
  }

  public static abstract interface NetworkChangeListener
  {
    public abstract void onNetworkChanged(boolean paramBoolean1, boolean paramBoolean2);
  }

  public static abstract interface StreamabilityChangeListener
  {
    public abstract void onStreamabilityChanged(boolean paramBoolean);
  }

  public static abstract interface UIStateChangeListener
  {
    public abstract void onAccountStatusUpdate(Account paramAccount, NautilusStatus paramNautilusStatus);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.UIStateManager
 * JD-Core Version:    0.6.2
 */