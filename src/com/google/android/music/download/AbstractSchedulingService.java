package com.google.android.music.download;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import com.google.android.gsf.Gservices;
import com.google.android.music.download.cache.CacheService;
import com.google.android.music.download.cache.FileLocation;
import com.google.android.music.download.cache.ICacheManager;
import com.google.android.music.download.cache.ICacheManager.Stub;
import com.google.android.music.download.cache.IDeleteFilter;
import com.google.android.music.download.cache.IDeleteFilter.Stub;
import com.google.android.music.download.cache.OutOfSpaceException;
import com.google.android.music.log.Log;
import com.google.android.music.net.INetworkChangeListener;
import com.google.android.music.net.INetworkChangeListener.Stub;
import com.google.android.music.net.INetworkMonitor;
import com.google.android.music.net.NetworkMonitorServiceConnection;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.SafeServiceConnection;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractSchedulingService extends Service
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private final String TAG;
  private final Map<Long, Long> mBlacklistedIds;
  private volatile ICacheManager mCacheManager;
  private final SafeServiceConnection mCacheServiceConnection;
  private final IDeleteFilter mDeleteFilter;
  private final IDownloadProgressListener mDownloadProgressListener;
  private volatile IDownloadQueueManager mDownloadQueueManager;
  private final SafeServiceConnection mDownloadServiceConnection;
  private volatile MusicPreferences mMusicPreferences;
  private final INetworkChangeListener mNetworkChangeListener;
  private final NetworkMonitorServiceConnection mNetworkMonitorServiceConnection;
  private final DownloadRequest.Owner mOwner;
  private SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener;
  List<DownloadRequest> mRequests;
  private volatile int mStartId;
  private final Class<? extends BroadcastReceiver> mStartupReceiverClass;
  private volatile boolean mStartupReceiverEnabled;
  private volatile ServiceState mState;
  private final ServiceWorker mWorker;

  protected AbstractSchedulingService(String paramString, DownloadRequest.Owner paramOwner, Class<? extends BroadcastReceiver> paramClass)
  {
    ServiceState localServiceState = ServiceState.NOT_STARTED;
    this.mState = localServiceState;
    LinkedList localLinkedList = new LinkedList();
    this.mRequests = localLinkedList;
    HashMap localHashMap = new HashMap();
    this.mBlacklistedIds = localHashMap;
    SafeServiceConnection local1 = new SafeServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        AbstractSchedulingService localAbstractSchedulingService = AbstractSchedulingService.this;
        IDownloadQueueManager localIDownloadQueueManager1 = IDownloadQueueManager.Stub.asInterface(paramAnonymousIBinder);
        IDownloadQueueManager localIDownloadQueueManager2 = AbstractSchedulingService.access$002(localAbstractSchedulingService, localIDownloadQueueManager1);
        AbstractSchedulingService.this.checkDependentServices();
      }

      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
      }
    };
    this.mDownloadServiceConnection = local1;
    SafeServiceConnection local2 = new SafeServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        AbstractSchedulingService localAbstractSchedulingService = AbstractSchedulingService.this;
        ICacheManager localICacheManager1 = ICacheManager.Stub.asInterface(paramAnonymousIBinder);
        ICacheManager localICacheManager2 = AbstractSchedulingService.access$202(localAbstractSchedulingService, localICacheManager1);
        try
        {
          ICacheManager localICacheManager3 = AbstractSchedulingService.this.mCacheManager;
          IDeleteFilter localIDeleteFilter = AbstractSchedulingService.this.mDeleteFilter;
          localICacheManager3.registerDeleteFilter(localIDeleteFilter);
          AbstractSchedulingService.this.checkDependentServices();
          return;
        }
        catch (RemoteException localRemoteException)
        {
          while (true)
            Log.w(AbstractSchedulingService.this.TAG, "Failed to register delete filter", localRemoteException);
        }
      }

      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
      }
    };
    this.mCacheServiceConnection = local2;
    IDownloadProgressListener.Stub local3 = new IDownloadProgressListener.Stub()
    {
      public void onDownloadProgress(DownloadProgress paramAnonymousDownloadProgress)
        throws RemoteException
      {
        AbstractSchedulingService.ServiceState localServiceState1 = AbstractSchedulingService.this.mState;
        AbstractSchedulingService.ServiceState localServiceState2 = AbstractSchedulingService.ServiceState.FINISHED;
        if (localServiceState1 == localServiceState2)
          return;
        AbstractSchedulingService.this.sendUpdateProgressMessage(paramAnonymousDownloadProgress);
      }
    };
    this.mDownloadProgressListener = local3;
    IDeleteFilter.Stub local4 = new IDeleteFilter.Stub()
    {
      public ContentIdentifier[] getFilteredIds()
        throws RemoteException
      {
        synchronized (AbstractSchedulingService.this.mRequests)
        {
          if (AbstractSchedulingService.this.mRequests.size() == 0)
          {
            arrayOfContentIdentifier = null;
            return arrayOfContentIdentifier;
          }
          ContentIdentifier[] arrayOfContentIdentifier = new ContentIdentifier[AbstractSchedulingService.this.mRequests.size()];
          int i = 0;
          while (true)
          {
            int j = arrayOfContentIdentifier.length;
            if (i >= j)
              break;
            ContentIdentifier localContentIdentifier = ((DownloadRequest)AbstractSchedulingService.this.mRequests.get(i)).getId();
            arrayOfContentIdentifier[i] = localContentIdentifier;
            i += 1;
          }
        }
      }

      public boolean shouldFilter(String paramAnonymousString)
        throws RemoteException
      {
        synchronized (AbstractSchedulingService.this.mRequests)
        {
          Iterator localIterator = AbstractSchedulingService.this.mRequests.iterator();
          while (localIterator.hasNext())
            if (((DownloadRequest)localIterator.next()).getFileLocation().getFullPath().getAbsolutePath().equals(paramAnonymousString))
            {
              bool = true;
              return bool;
            }
          boolean bool = false;
        }
      }
    };
    this.mDeleteFilter = local4;
    INetworkChangeListener.Stub local5 = new INetworkChangeListener.Stub()
    {
      public void onNetworkChanged(boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2)
      {
        AbstractSchedulingService.ServiceState localServiceState1 = AbstractSchedulingService.this.mState;
        AbstractSchedulingService.ServiceState localServiceState2 = AbstractSchedulingService.ServiceState.DISABLED;
        if (localServiceState1 != localServiceState2)
        {
          AbstractSchedulingService.ServiceState localServiceState3 = AbstractSchedulingService.this.mState;
          AbstractSchedulingService.ServiceState localServiceState4 = AbstractSchedulingService.ServiceState.WORKING;
          if (localServiceState3 != localServiceState4)
            return;
        }
        AbstractSchedulingService.this.sendUpdateEnabledMessage();
      }
    };
    this.mNetworkChangeListener = local5;
    SharedPreferences.OnSharedPreferenceChangeListener local6 = new SharedPreferences.OnSharedPreferenceChangeListener()
    {
      public void onSharedPreferenceChanged(SharedPreferences paramAnonymousSharedPreferences, String paramAnonymousString)
      {
        AbstractSchedulingService.ServiceState localServiceState1 = AbstractSchedulingService.this.mState;
        AbstractSchedulingService.ServiceState localServiceState2 = AbstractSchedulingService.ServiceState.DISABLED;
        if (localServiceState1 != localServiceState2)
        {
          AbstractSchedulingService.ServiceState localServiceState3 = AbstractSchedulingService.this.mState;
          AbstractSchedulingService.ServiceState localServiceState4 = AbstractSchedulingService.ServiceState.WORKING;
          if (localServiceState3 != localServiceState4)
            return;
        }
        AbstractSchedulingService.this.sendUpdateEnabledMessage();
      }
    };
    this.mPreferenceChangeListener = local6;
    this.TAG = paramString;
    ServiceWorker localServiceWorker = new ServiceWorker(paramString);
    this.mWorker = localServiceWorker;
    INetworkChangeListener localINetworkChangeListener = this.mNetworkChangeListener;
    NetworkMonitorServiceConnection localNetworkMonitorServiceConnection = new NetworkMonitorServiceConnection(localINetworkChangeListener);
    this.mNetworkMonitorServiceConnection = localNetworkMonitorServiceConnection;
    this.mStartupReceiverClass = paramClass;
    this.mOwner = paramOwner;
  }

  private void checkDependentServices()
  {
    if (LOGV)
      Log.d(this.TAG, "updateServiceState");
    if (!isAllServicesConnected())
      return;
    ServiceState localServiceState1 = this.mState;
    ServiceState localServiceState2 = ServiceState.WAITING_FOR_SERVICES;
    if (localServiceState1 != localServiceState2)
      return;
    ServiceState localServiceState3 = ServiceState.INITIALIZED;
    this.mState = localServiceState3;
  }

  private void disableStartupReceiver()
  {
    if (this.mStartupReceiverClass == null)
      return;
    if (!this.mStartupReceiverEnabled)
      return;
    this.mStartupReceiverEnabled = false;
    if (LOGV)
      Log.d(this.TAG, "Disabled startup receiver");
    PackageManager localPackageManager = getPackageManager();
    Class localClass = this.mStartupReceiverClass;
    ComponentName localComponentName = new ComponentName(this, localClass);
    localPackageManager.setComponentEnabledSetting(localComponentName, 2, 1);
  }

  private void enableStartupReceiver()
  {
    if (this.mStartupReceiverClass == null)
      return;
    if (this.mStartupReceiverEnabled)
      return;
    this.mStartupReceiverEnabled = true;
    if (LOGV)
      Log.d(this.TAG, "Enabled startup receiver");
    PackageManager localPackageManager = getPackageManager();
    Class localClass = this.mStartupReceiverClass;
    ComponentName localComponentName = new ComponentName(this, localClass);
    localPackageManager.setComponentEnabledSetting(localComponentName, 1, 1);
  }

  private boolean isAllServicesConnected()
  {
    boolean bool1 = true;
    INetworkMonitor localINetworkMonitor = this.mNetworkMonitorServiceConnection.getNetworkMonitor();
    boolean bool2;
    String str3;
    String str4;
    Object[] arrayOfObject;
    boolean bool3;
    label102: boolean bool4;
    label125: int i;
    if ((this.mDownloadQueueManager != null) && (this.mCacheManager != null) && (localINetworkMonitor != null))
    {
      bool2 = true;
      if (LOGV)
      {
        String str1 = this.TAG;
        String str2 = "isAllServicesConnected: " + bool2;
        Log.d(str1, str2);
        if (!bool2)
        {
          str3 = this.TAG;
          str4 = "bindings: dm=%b cm=%b nm=%b";
          arrayOfObject = new Object[3];
          if (this.mDownloadQueueManager == null)
            break label181;
          bool3 = true;
          Boolean localBoolean1 = Boolean.valueOf(bool3);
          arrayOfObject[0] = localBoolean1;
          if (this.mCacheManager == null)
            break label187;
          bool4 = true;
          Boolean localBoolean2 = Boolean.valueOf(bool4);
          arrayOfObject[1] = localBoolean2;
          i = 2;
          if (localINetworkMonitor == null)
            break label193;
        }
      }
    }
    while (true)
    {
      Boolean localBoolean3 = Boolean.valueOf(bool1);
      arrayOfObject[i] = localBoolean3;
      String str5 = String.format(str4, arrayOfObject);
      Log.d(str3, str5);
      return bool2;
      bool2 = false;
      break;
      label181: bool3 = false;
      break label102;
      label187: bool4 = false;
      break label125;
      label193: bool1 = false;
    }
  }

  private void sendCheckInitCompletedMessage(int paramInt)
  {
    Message localMessage = this.mWorker.obtainMessage(4);
    this.mWorker.removeMessages(4);
    localMessage.arg1 = paramInt;
    boolean bool = this.mWorker.sendMessageDelayed(localMessage, 1000L);
  }

  private void sendFinalCheckInitCompletedMessage(int paramInt)
  {
    Message localMessage = this.mWorker.obtainMessage(5);
    this.mWorker.removeMessages(5);
    localMessage.arg1 = paramInt;
    boolean bool = this.mWorker.sendMessageDelayed(localMessage, 10000L);
  }

  private void sendUpdateEnabledMessage()
  {
    if (isDownloadingPaused())
    {
      DisableReason localDisableReason = DisableReason.DOWNLOAD_PAUSED;
      sendUpdateEnabledMessage(localDisableReason);
      return;
    }
    sendUpdateEnabledMessage(null);
  }

  private void sendUpdateEnabledMessage(DisableReason paramDisableReason)
  {
    Message localMessage = this.mWorker.obtainMessage(6);
    localMessage.obj = paramDisableReason;
    boolean bool = this.mWorker.sendMessage(localMessage);
  }

  protected void deleteFromStorage(DownloadRequest paramDownloadRequest)
  {
    if (paramDownloadRequest == null)
      return;
    try
    {
      this.mCacheManager.requestDelete(paramDownloadRequest);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e(this.TAG, "Failed to request delete", localRemoteException);
    }
  }

  protected MusicPreferences getMusicPreferences()
  {
    return this.mMusicPreferences;
  }

  protected INetworkMonitor getNetworkMonitor()
  {
    return this.mNetworkMonitorServiceConnection.getNetworkMonitor();
  }

  protected abstract List<DownloadRequest> getNextDownloads(ICacheManager paramICacheManager, Collection<Long> paramCollection)
    throws OutOfSpaceException;

  protected abstract long getTotalDownloadSize();

  protected abstract boolean isDownloadingPaused();

  protected abstract void notifyAllWorkFinished();

  protected abstract void notifyDisabled(DisableReason paramDisableReason);

  protected abstract void notifyDownloadCompleted(DownloadRequest paramDownloadRequest, DownloadProgress paramDownloadProgress);

  protected abstract void notifyDownloadFailed(DownloadRequest paramDownloadRequest, DownloadProgress paramDownloadProgress);

  protected abstract void notifyDownloadProgress(float paramFloat, DownloadProgress paramDownloadProgress);

  protected abstract void notifyDownloadStarting();

  public IBinder onBind(Intent paramIntent)
  {
    return null;
  }

  public void onCreate()
  {
    boolean bool1 = true;
    super.onCreate();
    if (LOGV)
      Log.d(this.TAG, "onCreate");
    MusicPreferences localMusicPreferences1 = MusicPreferences.getMusicPreferences(this, this);
    this.mMusicPreferences = localMusicPreferences1;
    SafeServiceConnection localSafeServiceConnection1 = this.mDownloadServiceConnection;
    Intent localIntent1 = new Intent(this, DownloadQueueService.class);
    boolean bool2 = localSafeServiceConnection1.bindService(this, localIntent1, 1);
    SafeServiceConnection localSafeServiceConnection2 = this.mCacheServiceConnection;
    Intent localIntent2 = new Intent(this, CacheService.class);
    boolean bool3 = localSafeServiceConnection2.bindService(this, localIntent2, 1);
    this.mNetworkMonitorServiceConnection.bindToService(this);
    MusicPreferences localMusicPreferences2 = this.mMusicPreferences;
    SharedPreferences.OnSharedPreferenceChangeListener localOnSharedPreferenceChangeListener = this.mPreferenceChangeListener;
    localMusicPreferences2.registerOnSharedPreferenceChangeListener(localOnSharedPreferenceChangeListener);
    if (this.mStartupReceiverClass != null)
    {
      PackageManager localPackageManager = getPackageManager();
      Class localClass = this.mStartupReceiverClass;
      ComponentName localComponentName = new ComponentName(this, localClass);
      if (localPackageManager.getComponentEnabledSetting(localComponentName) != 1)
        break label177;
    }
    while (true)
    {
      this.mStartupReceiverEnabled = bool1;
      ServiceState localServiceState = ServiceState.WAITING_FOR_SERVICES;
      this.mState = localServiceState;
      return;
      label177: bool1 = false;
    }
  }

  public void onDestroy()
  {
    if (LOGV)
      Log.d(this.TAG, "onDestroy");
    MusicPreferences localMusicPreferences = this.mMusicPreferences;
    SharedPreferences.OnSharedPreferenceChangeListener localOnSharedPreferenceChangeListener = this.mPreferenceChangeListener;
    localMusicPreferences.unregisterOnSharedPreferenceChangeListener(localOnSharedPreferenceChangeListener);
    this.mWorker.quit();
    try
    {
      if (this.mCacheManager != null)
      {
        ICacheManager localICacheManager = this.mCacheManager;
        IDeleteFilter localIDeleteFilter = this.mDeleteFilter;
        localICacheManager.unregisterDeleteFilter(localIDeleteFilter);
      }
      this.mCacheServiceConnection.unbindService(this);
      this.mDownloadServiceConnection.unbindService(this);
      this.mNetworkMonitorServiceConnection.unbindFromService(this);
      MusicPreferences.releaseMusicPreferences(this);
      ServiceState localServiceState = ServiceState.FINISHED;
      this.mState = localServiceState;
      return;
    }
    catch (RemoteException localRemoteException)
    {
      while (true)
        Log.e(this.TAG, "Failed to unregister delete filter", localRemoteException);
    }
  }

  protected boolean postRunnable(Runnable paramRunnable)
  {
    return this.mWorker.post(paramRunnable);
  }

  protected void sendCancelDownloadsMessage(int paramInt)
  {
    Message localMessage = this.mWorker.obtainMessage(7);
    this.mWorker.removeMessages(2);
    localMessage.arg1 = paramInt;
    boolean bool = this.mWorker.sendMessage(localMessage);
  }

  protected void sendInitScheduleMessage(int paramInt)
  {
    Message localMessage = this.mWorker.obtainMessage(1);
    localMessage.arg1 = paramInt;
    boolean bool = this.mWorker.sendMessage(localMessage);
  }

  protected void sendScheduleDownloadsMessage(int paramInt)
  {
    Message localMessage = this.mWorker.obtainMessage(2);
    localMessage.arg1 = paramInt;
    boolean bool = this.mWorker.sendMessage(localMessage);
  }

  protected void sendUpdateProgressMessage(DownloadProgress paramDownloadProgress)
  {
    Message localMessage = this.mWorker.obtainMessage(3);
    localMessage.obj = paramDownloadProgress;
    boolean bool = this.mWorker.sendMessage(localMessage);
  }

  protected void storeInCache(DownloadRequest paramDownloadRequest, String paramString, long paramLong)
  {
    if (paramDownloadRequest == null)
      return;
    try
    {
      if (paramDownloadRequest.getFileLocation().getCacheType() == 1)
        return;
      String str = this.mCacheManager.storeInCache(paramDownloadRequest, paramString, paramLong);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e(this.TAG, "Failed to srore file in the cache");
    }
  }

  private final class ServiceWorker extends LoggableHandler
  {
    private long mCompletedDownloadsSize;
    private boolean mHighSpeedAvailable = false;
    private boolean mOutOfSpace = false;
    private int mSuccessiveFailures = 0;
    private long mTotalDownloadSize;

    public ServiceWorker(String arg2)
    {
      super();
    }

    private String blackListToString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      Iterator localIterator = AbstractSchedulingService.this.mBlacklistedIds.keySet().iterator();
      while (localIterator.hasNext())
      {
        long l = ((Long)localIterator.next()).longValue();
        StringBuilder localStringBuilder2 = localStringBuilder1.append("(");
        StringBuilder localStringBuilder3 = localStringBuilder1.append(l);
        StringBuilder localStringBuilder4 = localStringBuilder1.append(", ");
        Map localMap = AbstractSchedulingService.this.mBlacklistedIds;
        Long localLong = Long.valueOf(l);
        Object localObject = localMap.get(localLong);
        StringBuilder localStringBuilder5 = localStringBuilder1.append(localObject);
        StringBuilder localStringBuilder6 = localStringBuilder1.append("),");
      }
      if (localStringBuilder1.length() != 0)
      {
        int i = localStringBuilder1.length() + -1;
        StringBuilder localStringBuilder7 = localStringBuilder1.deleteCharAt(i);
      }
      return localStringBuilder1.toString();
    }

    private void handleCancelDownloads(int paramInt)
    {
      synchronized (AbstractSchedulingService.this.mRequests)
      {
        int i = AbstractSchedulingService.this.mRequests.size();
        AbstractSchedulingService.this.mRequests.clear();
        if (i > 0)
          if (AbstractSchedulingService.LOGV)
          {
            String str1 = AbstractSchedulingService.this.TAG;
            Object[] arrayOfObject = new Object[1];
            Integer localInteger = Integer.valueOf(i);
            arrayOfObject[0] = localInteger;
            String str2 = String.format("%d pending requests. Clearing.", arrayOfObject);
            Log.d(str1, str2);
          }
      }
      try
      {
        IDownloadQueueManager localIDownloadQueueManager = AbstractSchedulingService.this.mDownloadQueueManager;
        int j = AbstractSchedulingService.this.mOwner.ordinal();
        localIDownloadQueueManager.cancelAndPurge(j, 2);
        AbstractSchedulingService.this.stopForeground(true);
        AbstractSchedulingService.this.stopSelf(paramInt);
        return;
        localObject = finally;
        throw localObject;
      }
      catch (RemoteException localRemoteException)
      {
        while (true)
          Log.e(AbstractSchedulingService.this.TAG, "Failed to cancel downloads", localRemoteException);
      }
    }

    private void handleCheckInitCompletedMessage()
    {
      if (AbstractSchedulingService.this.isAllServicesConnected())
      {
        AbstractSchedulingService.this.checkDependentServices();
        AbstractSchedulingService.this.sendUpdateEnabledMessage();
        return;
      }
      AbstractSchedulingService localAbstractSchedulingService = AbstractSchedulingService.this;
      int i = AbstractSchedulingService.this.mStartId;
      localAbstractSchedulingService.sendFinalCheckInitCompletedMessage(i);
    }

    private void handleFinalCheckInitCompletedMessage(int paramInt)
    {
      if (!AbstractSchedulingService.this.isAllServicesConnected())
      {
        AbstractSchedulingService.this.stopSelf(paramInt);
        return;
      }
      AbstractSchedulingService.this.checkDependentServices();
      AbstractSchedulingService.this.sendUpdateEnabledMessage();
    }

    private void handleInitSchedule()
    {
      if (AbstractSchedulingService.LOGV)
        Log.d(AbstractSchedulingService.this.TAG, "handleInitSchedule");
      this.mCompletedDownloadsSize = 0L;
      long l1 = AbstractSchedulingService.this.getTotalDownloadSize();
      this.mTotalDownloadSize = l1;
      if (AbstractSchedulingService.LOGV)
      {
        String str1 = AbstractSchedulingService.this.TAG;
        StringBuilder localStringBuilder = new StringBuilder().append("mTotalDownloadSize=");
        long l2 = this.mTotalDownloadSize;
        String str2 = l2;
        Log.d(str1, str2);
      }
      if (this.mTotalDownloadSize > 0L)
        return;
      AbstractSchedulingService localAbstractSchedulingService = AbstractSchedulingService.this;
      int i = AbstractSchedulingService.this.mStartId;
      localAbstractSchedulingService.sendCancelDownloadsMessage(i);
    }

    private void handleProgress(DownloadProgress paramDownloadProgress)
    {
      int i;
      DownloadRequest localDownloadRequest;
      synchronized (AbstractSchedulingService.this.mRequests)
      {
        i = AbstractSchedulingService.this.mRequests.size();
        if (AbstractSchedulingService.LOGV)
        {
          String str1 = AbstractSchedulingService.this.TAG;
          Object[] arrayOfObject1 = new Object[2];
          Integer localInteger = Integer.valueOf(i);
          arrayOfObject1[0] = localInteger;
          arrayOfObject1[1] = paramDownloadProgress;
          String str2 = String.format("requests: %d progress: %s", arrayOfObject1);
          Log.d(str1, str2);
        }
        l1 = 0L;
        localDownloadRequest = null;
        int[] arrayOfInt = AbstractSchedulingService.7.$SwitchMap$com$google$android$music$download$DownloadState$State;
        int j = paramDownloadProgress.getState().ordinal();
        switch (arrayOfInt[j])
        {
        default:
          throw new RuntimeException("Unhandled request state");
        case 2:
        case 1:
        case 3:
        case 4:
        case 5:
        }
      }
      long l1 = paramDownloadProgress.getCompletedBytes();
      while (true)
      {
        if (AbstractSchedulingService.LOGV)
        {
          String str3 = AbstractSchedulingService.this.TAG;
          Object[] arrayOfObject2 = new Object[3];
          Long localLong1 = Long.valueOf(this.mCompletedDownloadsSize);
          arrayOfObject2[0] = localLong1;
          Long localLong2 = Long.valueOf(l1);
          arrayOfObject2[1] = localLong2;
          Long localLong3 = Long.valueOf(this.mTotalDownloadSize);
          arrayOfObject2[2] = localLong3;
          String str4 = String.format("mCompletedDownloadsSize=%d currentDownloadProgress=%d mTotalDownloadSize=%d", arrayOfObject2);
          Log.d(str3, str4);
        }
        float f1 = 0.0F;
        if (this.mTotalDownloadSize != 0L)
        {
          float f2 = (float)this.mCompletedDownloadsSize;
          float f3 = (float)l1;
          float f4 = f2 + f3;
          float f5 = (float)this.mTotalDownloadSize;
          f1 = f4 / f5;
        }
        AbstractSchedulingService.ServiceState localServiceState1 = AbstractSchedulingService.this.mState;
        AbstractSchedulingService.ServiceState localServiceState2 = AbstractSchedulingService.ServiceState.DISABLED;
        if ((localServiceState1 != localServiceState2) && (AbstractSchedulingService.this.mRequests.size() != 0))
        {
          AbstractSchedulingService localAbstractSchedulingService1 = AbstractSchedulingService.this;
          DownloadProgress localDownloadProgress1 = paramDownloadProgress;
          localAbstractSchedulingService1.notifyDownloadProgress(f1, localDownloadProgress1);
        }
        synchronized (AbstractSchedulingService.this.mRequests)
        {
          i = AbstractSchedulingService.this.mRequests.size();
          if (i != 0)
            return;
          if (AbstractSchedulingService.this.isDownloadingPaused())
            return;
          AbstractSchedulingService localAbstractSchedulingService2 = AbstractSchedulingService.this;
          int k = AbstractSchedulingService.this.mStartId;
          localAbstractSchedulingService2.sendScheduleDownloadsMessage(k);
          return;
          l1 = 0L;
          Iterator localIterator;
          synchronized (AbstractSchedulingService.this.mRequests)
          {
            localIterator = AbstractSchedulingService.this.mRequests.iterator();
            while (localIterator.hasNext())
            {
              localDownloadRequest = (DownloadRequest)localIterator.next();
              DownloadProgress localDownloadProgress2 = paramDownloadProgress;
              if (localDownloadRequest.isMyProgress(localDownloadProgress2))
              {
                localIterator.remove();
                long l2 = this.mCompletedDownloadsSize;
                long l3 = paramDownloadProgress.getCompletedBytes();
                long l4 = l2 + l3;
                this.mCompletedDownloadsSize = l4;
              }
            }
            if (localDownloadRequest != null)
            {
              AbstractSchedulingService localAbstractSchedulingService3 = AbstractSchedulingService.this;
              DownloadProgress localDownloadProgress3 = paramDownloadProgress;
              localAbstractSchedulingService3.notifyDownloadCompleted(localDownloadRequest, localDownloadProgress3);
            }
            if (!AbstractSchedulingService.LOGV)
              continue;
            String str5 = AbstractSchedulingService.this.TAG;
            StringBuilder localStringBuilder = new StringBuilder().append("mCompletedDownloadsSize=");
            long l5 = this.mCompletedDownloadsSize;
            String str6 = l5;
            Log.d(str5, str6);
          }
          int m = this.mSuccessiveFailures + 1;
          this.mSuccessiveFailures = m;
          if (paramDownloadProgress.getError() == 5)
          {
            AbstractSchedulingService localAbstractSchedulingService4 = AbstractSchedulingService.this;
            AbstractSchedulingService.DisableReason localDisableReason = AbstractSchedulingService.DisableReason.DEVICE_NOT_AUTHORIZED;
            localAbstractSchedulingService4.sendUpdateEnabledMessage(localDisableReason);
            l1 = 0L;
          }
          synchronized (AbstractSchedulingService.this.mRequests)
          {
            localIterator = AbstractSchedulingService.this.mRequests.iterator();
            while (localIterator.hasNext())
            {
              localDownloadRequest = (DownloadRequest)localIterator.next();
              DownloadProgress localDownloadProgress4 = paramDownloadProgress;
              if (localDownloadRequest.isMyProgress(localDownloadProgress4))
                localIterator.remove();
            }
            if (localDownloadRequest == null)
              continue;
            AbstractSchedulingService localAbstractSchedulingService5 = AbstractSchedulingService.this;
            DownloadProgress localDownloadProgress5 = paramDownloadProgress;
            localAbstractSchedulingService5.notifyDownloadFailed(localDownloadRequest, localDownloadProgress5);
            continue;
            if ((paramDownloadProgress.getError() == 12) || (paramDownloadProgress.getError() == 13))
            {
              int n = Gservices.getInt(AbstractSchedulingService.this.getContentResolver(), "music_download_max_blacklist_size", 1000);
              while (true)
              {
                synchronized (AbstractSchedulingService.this.mBlacklistedIds)
                {
                  long l6 = paramDownloadProgress.getId().getId();
                  Map localMap1 = AbstractSchedulingService.this.mBlacklistedIds;
                  Long localLong4 = Long.valueOf(l6);
                  if (!localMap1.containsKey(localLong4))
                  {
                    if (AbstractSchedulingService.this.mBlacklistedIds.size() >= n)
                      break label959;
                    Map localMap2 = AbstractSchedulingService.this.mBlacklistedIds;
                    Long localLong5 = Long.valueOf(l6);
                    Long localLong6 = Long.valueOf(System.currentTimeMillis());
                    Object localObject4 = localMap2.put(localLong5, localLong6);
                    if (AbstractSchedulingService.LOGV)
                    {
                      String str7 = AbstractSchedulingService.this.TAG;
                      Object[] arrayOfObject3 = new Object[2];
                      ContentIdentifier localContentIdentifier = paramDownloadProgress.getId();
                      arrayOfObject3[0] = localContentIdentifier;
                      String str8 = blackListToString();
                      arrayOfObject3[1] = str8;
                      String str9 = String.format("Blacklisting id=%s blacklist=[%s]", arrayOfObject3);
                      Log.d(str7, str9);
                    }
                  }
                }
                label959: String str10 = AbstractSchedulingService.this.TAG;
                String str11 = "Max blacklist size reached: " + n;
                Log.w(str10, str11);
                AbstractSchedulingService.this.sendUpdateEnabledMessage();
              }
            }
            AbstractSchedulingService.this.sendUpdateEnabledMessage();
          }
        }
      }
    }

    private boolean handleScheduleDownloads()
    {
      boolean bool1 = null;
      if (AbstractSchedulingService.LOGV)
        Log.d(AbstractSchedulingService.this.TAG, "handleScheduleDownloads");
      AbstractSchedulingService.ServiceState localServiceState1 = AbstractSchedulingService.this.mState;
      AbstractSchedulingService.ServiceState localServiceState2 = AbstractSchedulingService.ServiceState.DISABLED;
      if (localServiceState1 == localServiceState2)
        if (AbstractSchedulingService.LOGV)
          Log.d(AbstractSchedulingService.this.TAG, "The service is disabled");
      while (true)
      {
        return bool1;
        synchronized (AbstractSchedulingService.this.mRequests)
        {
          int i = AbstractSchedulingService.this.mRequests.size();
          if (i != 0)
          {
            if (AbstractSchedulingService.LOGV)
            {
              String str1 = AbstractSchedulingService.this.TAG;
              Object[] arrayOfObject = new Object[1];
              Integer localInteger = Integer.valueOf(i);
              arrayOfObject[0] = localInteger;
              String str2 = String.format("We have %d pending requests. Not scheduling new.", arrayOfObject);
              Log.d(str1, str2);
            }
            bool1 = true;
            continue;
          }
        }
        List localList2;
        Object localObject3;
        while (true)
        {
          try
          {
            HashSet localHashSet = new HashSet();
            synchronized (AbstractSchedulingService.this.mBlacklistedIds)
            {
              while (true)
              {
                Set localSet = AbstractSchedulingService.this.mBlacklistedIds.keySet();
                boolean bool2 = localHashSet.addAll(localSet);
                AbstractSchedulingService localAbstractSchedulingService = AbstractSchedulingService.this;
                ICacheManager localICacheManager = AbstractSchedulingService.this.mCacheManager;
                List localList1 = localAbstractSchedulingService.getNextDownloads(localICacheManager, localHashSet);
                localList2 = localList1;
                if (localList2.size() != 0)
                  break;
                AbstractSchedulingService.this.disableStartupReceiver();
                synchronized (AbstractSchedulingService.this.mRequests)
                {
                  boolean bool3 = AbstractSchedulingService.this.mRequests.addAll(localList2);
                  if (AbstractSchedulingService.this.mRequests.size() != 0)
                    break label403;
                }
              }
              localObject1 = finally;
              throw localObject1;
            }
          }
          catch (OutOfSpaceException localOutOfSpaceException)
          {
            String str3 = AbstractSchedulingService.this.TAG;
            StringBuilder localStringBuilder = new StringBuilder().append("Failed to get next downloads: ");
            String str4 = localOutOfSpaceException.getMessage();
            String str5 = str4;
            Log.e(str3, str5);
            this.mOutOfSpace = true;
            AbstractSchedulingService.this.sendUpdateEnabledMessage();
            AbstractSchedulingService.this.enableStartupReceiver();
            localObject3 = null;
          }
          break;
          AbstractSchedulingService.this.enableStartupReceiver();
        }
        try
        {
          label403: IDownloadQueueManager localIDownloadQueueManager = AbstractSchedulingService.this.mDownloadQueueManager;
          IDownloadProgressListener localIDownloadProgressListener = AbstractSchedulingService.this.mDownloadProgressListener;
          localIDownloadQueueManager.download(localList2, localIDownloadProgressListener, 1);
          AbstractSchedulingService.this.notifyDownloadStarting();
          localObject3 = null;
        }
        catch (RemoteException localRemoteException)
        {
          Log.e(AbstractSchedulingService.this.TAG, "Failed to schedule downloads");
        }
      }
    }

    private void handleUpdateEnabled(AbstractSchedulingService.DisableReason paramDisableReason)
    {
      int i = 1;
      if (AbstractSchedulingService.LOGV)
      {
        String str1 = AbstractSchedulingService.this.TAG;
        StringBuilder localStringBuilder = new StringBuilder().append("handleUpdateEnabled: mState=");
        AbstractSchedulingService.ServiceState localServiceState1 = AbstractSchedulingService.this.mState;
        String str2 = localServiceState1;
        Log.d(str1, str2);
      }
      if (AbstractSchedulingService.this.isDownloadingPaused())
      {
        AbstractSchedulingService.ServiceState localServiceState2 = AbstractSchedulingService.this.mState;
        AbstractSchedulingService.ServiceState localServiceState3 = AbstractSchedulingService.ServiceState.DISABLED;
        if (localServiceState2 == localServiceState3)
        {
          if (!AbstractSchedulingService.LOGV)
            return;
          Log.d(AbstractSchedulingService.this.TAG, "Downloading is paused by user");
          return;
        }
      }
      if (paramDisableReason != null)
      {
        if (AbstractSchedulingService.LOGV)
        {
          String str3 = AbstractSchedulingService.this.TAG;
          String str4 = "handleUpdateEnabled: DisableReason=" + paramDisableReason;
          Log.d(str3, str4);
        }
        AbstractSchedulingService.ServiceState localServiceState4 = AbstractSchedulingService.this.mState;
        AbstractSchedulingService.ServiceState localServiceState5 = AbstractSchedulingService.ServiceState.DISABLED;
        if (localServiceState4 == localServiceState5)
          return;
        AbstractSchedulingService localAbstractSchedulingService1 = AbstractSchedulingService.this;
        AbstractSchedulingService.ServiceState localServiceState6 = AbstractSchedulingService.ServiceState.DISABLED;
        AbstractSchedulingService.ServiceState localServiceState7 = AbstractSchedulingService.access$502(localAbstractSchedulingService1, localServiceState6);
        this.mSuccessiveFailures = 0;
        AbstractSchedulingService.this.notifyDisabled(paramDisableReason);
        return;
      }
      if (this.mOutOfSpace)
      {
        if (this.mTotalDownloadSize != 0L)
        {
          AbstractSchedulingService.ServiceState localServiceState8 = AbstractSchedulingService.this.mState;
          AbstractSchedulingService.ServiceState localServiceState9 = AbstractSchedulingService.ServiceState.DISABLED;
          if (localServiceState8 != localServiceState9)
          {
            AbstractSchedulingService localAbstractSchedulingService2 = AbstractSchedulingService.this;
            AbstractSchedulingService.DisableReason localDisableReason1 = AbstractSchedulingService.DisableReason.OUT_OF_SPACE;
            localAbstractSchedulingService2.notifyDisabled(localDisableReason1);
          }
        }
        AbstractSchedulingService localAbstractSchedulingService3 = AbstractSchedulingService.this;
        AbstractSchedulingService.ServiceState localServiceState10 = AbstractSchedulingService.ServiceState.DISABLED;
        AbstractSchedulingService.ServiceState localServiceState11 = AbstractSchedulingService.access$502(localAbstractSchedulingService3, localServiceState10);
        this.mOutOfSpace = false;
        return;
      }
      INetworkMonitor localINetworkMonitor = AbstractSchedulingService.this.mNetworkMonitorServiceConnection.getNetworkMonitor();
      if (localINetworkMonitor == null)
      {
        Log.w(AbstractSchedulingService.this.TAG, "Missing binding to the network monitor");
        return;
      }
      boolean bool3;
      boolean bool4;
      while (true)
      {
        try
        {
          boolean bool1 = localINetworkMonitor.hasMobileOrMeteredConnection();
          boolean bool2 = localINetworkMonitor.hasHighSpeedConnection();
          bool3 = bool2;
          if (AbstractSchedulingService.LOGV)
          {
            String str5 = AbstractSchedulingService.this.TAG;
            Object[] arrayOfObject = new Object[2];
            Boolean localBoolean1 = Boolean.valueOf(bool1);
            arrayOfObject[0] = localBoolean1;
            Boolean localBoolean2 = Boolean.valueOf(bool3);
            arrayOfObject[1] = localBoolean2;
            String str6 = String.format("handleUpdateEnabled: mobileSpeed=%b highSpeed=%b", arrayOfObject);
            Log.d(str5, str6);
          }
          bool4 = AbstractSchedulingService.this.mMusicPreferences.isOfflineDLOnlyOnWifi();
          if ((bool3) || ((bool1) && (!bool4)))
          {
            if (i == 0)
              break;
            AbstractSchedulingService.ServiceState localServiceState12 = AbstractSchedulingService.this.mState;
            AbstractSchedulingService.ServiceState localServiceState13 = AbstractSchedulingService.ServiceState.WORKING;
            if (localServiceState12 == localServiceState13)
              break;
            this.mHighSpeedAvailable = bool3;
            AbstractSchedulingService localAbstractSchedulingService4 = AbstractSchedulingService.this;
            AbstractSchedulingService.ServiceState localServiceState14 = AbstractSchedulingService.ServiceState.WORKING;
            AbstractSchedulingService.ServiceState localServiceState15 = AbstractSchedulingService.access$502(localAbstractSchedulingService4, localServiceState14);
            this.mSuccessiveFailures = 0;
            updateBlacklistedIds();
            AbstractSchedulingService localAbstractSchedulingService5 = AbstractSchedulingService.this;
            int j = AbstractSchedulingService.this.mStartId;
            localAbstractSchedulingService5.sendScheduleDownloadsMessage(j);
            return;
          }
        }
        catch (RemoteException localRemoteException)
        {
          String str7 = AbstractSchedulingService.this.TAG;
          String str8 = localRemoteException.getMessage();
          Log.e(str7, str8, localRemoteException);
          return;
        }
        i = 0;
      }
      if (i == 0)
      {
        AbstractSchedulingService.ServiceState localServiceState16 = AbstractSchedulingService.this.mState;
        AbstractSchedulingService.ServiceState localServiceState17 = AbstractSchedulingService.ServiceState.DISABLED;
        if (localServiceState16 != localServiceState17)
        {
          if (this.mTotalDownloadSize != 0L)
          {
            if ((!this.mHighSpeedAvailable) || (bool3) || (!bool4))
              break label655;
            AbstractSchedulingService localAbstractSchedulingService6 = AbstractSchedulingService.this;
            AbstractSchedulingService.DisableReason localDisableReason2 = AbstractSchedulingService.DisableReason.HIGH_SPEED_LOST;
            localAbstractSchedulingService6.notifyDisabled(localDisableReason2);
          }
          while (true)
          {
            this.mHighSpeedAvailable = bool3;
            AbstractSchedulingService localAbstractSchedulingService7 = AbstractSchedulingService.this;
            AbstractSchedulingService.ServiceState localServiceState18 = AbstractSchedulingService.ServiceState.DISABLED;
            AbstractSchedulingService.ServiceState localServiceState19 = AbstractSchedulingService.access$502(localAbstractSchedulingService7, localServiceState18);
            return;
            label655: AbstractSchedulingService localAbstractSchedulingService8 = AbstractSchedulingService.this;
            AbstractSchedulingService.DisableReason localDisableReason3 = AbstractSchedulingService.DisableReason.CONNECTIVITY_LOST;
            localAbstractSchedulingService8.notifyDisabled(localDisableReason3);
          }
        }
      }
      if (this.mSuccessiveFailures < 3)
        return;
      AbstractSchedulingService.ServiceState localServiceState20 = AbstractSchedulingService.this.mState;
      AbstractSchedulingService.ServiceState localServiceState21 = AbstractSchedulingService.ServiceState.DISABLED;
      if (localServiceState20 == localServiceState21)
        return;
      if (this.mTotalDownloadSize != 0L)
      {
        AbstractSchedulingService localAbstractSchedulingService9 = AbstractSchedulingService.this;
        AbstractSchedulingService.DisableReason localDisableReason4 = AbstractSchedulingService.DisableReason.SUCCESSIVE_FAILURES;
        localAbstractSchedulingService9.notifyDisabled(localDisableReason4);
      }
      AbstractSchedulingService localAbstractSchedulingService10 = AbstractSchedulingService.this;
      AbstractSchedulingService.ServiceState localServiceState22 = AbstractSchedulingService.ServiceState.DISABLED;
      AbstractSchedulingService.ServiceState localServiceState23 = AbstractSchedulingService.access$502(localAbstractSchedulingService10, localServiceState22);
      this.mSuccessiveFailures = 0;
    }

    private void updateBlacklistedIds()
    {
      long l1 = Gservices.getLong(AbstractSchedulingService.this.getContentResolver(), "music_download_blacklist_ttl_sec", 3600L) * 1000L;
      synchronized (AbstractSchedulingService.this.mBlacklistedIds)
      {
        Iterator localIterator = AbstractSchedulingService.this.mBlacklistedIds.keySet().iterator();
        while (localIterator.hasNext())
        {
          long l2 = ((Long)localIterator.next()).longValue();
          Map localMap2 = AbstractSchedulingService.this.mBlacklistedIds;
          Long localLong1 = Long.valueOf(l2);
          long l3 = ((Long)localMap2.get(localLong1)).longValue() + l1;
          long l4 = System.currentTimeMillis();
          if (l3 <= l4)
          {
            Map localMap3 = AbstractSchedulingService.this.mBlacklistedIds;
            Long localLong2 = Long.valueOf(l2);
            Object localObject1 = localMap3.remove(localLong2);
          }
        }
      }
    }

    public void handleMessage(Message paramMessage)
    {
      if (AbstractSchedulingService.LOGV)
      {
        String str1 = AbstractSchedulingService.this.TAG;
        String str2 = "handleMessage: msg" + paramMessage;
        Log.d(str1, str2);
      }
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
        AbstractSchedulingService localAbstractSchedulingService1 = AbstractSchedulingService.this;
        int i = paramMessage.arg1;
        int j = AbstractSchedulingService.access$802(localAbstractSchedulingService1, i);
        AbstractSchedulingService.ServiceState localServiceState1 = AbstractSchedulingService.this.mState;
        AbstractSchedulingService.ServiceState localServiceState2 = AbstractSchedulingService.ServiceState.WAITING_FOR_SERVICES;
        if (localServiceState1 == localServiceState2)
        {
          AbstractSchedulingService localAbstractSchedulingService2 = AbstractSchedulingService.this;
          int k = AbstractSchedulingService.this.mStartId;
          localAbstractSchedulingService2.sendCheckInitCompletedMessage(k);
        }
        while (true)
        {
          handleInitSchedule();
          return;
          AbstractSchedulingService.ServiceState localServiceState3 = AbstractSchedulingService.this.mState;
          AbstractSchedulingService.ServiceState localServiceState4 = AbstractSchedulingService.ServiceState.INITIALIZED;
          if (localServiceState3 == localServiceState4)
          {
            AbstractSchedulingService.this.sendUpdateEnabledMessage();
          }
          else
          {
            AbstractSchedulingService.ServiceState localServiceState5 = AbstractSchedulingService.this.mState;
            AbstractSchedulingService.ServiceState localServiceState6 = AbstractSchedulingService.ServiceState.WORKING;
            if (localServiceState5 == localServiceState6)
            {
              AbstractSchedulingService localAbstractSchedulingService3 = AbstractSchedulingService.this;
              int m = AbstractSchedulingService.this.mStartId;
              localAbstractSchedulingService3.sendScheduleDownloadsMessage(m);
            }
          }
        }
      case 2:
        if (handleScheduleDownloads())
          return;
        AbstractSchedulingService.ServiceState localServiceState7 = AbstractSchedulingService.this.mState;
        AbstractSchedulingService.ServiceState localServiceState8 = AbstractSchedulingService.ServiceState.WORKING;
        if ((localServiceState7 == localServiceState8) && (this.mCompletedDownloadsSize != 0L))
          AbstractSchedulingService.this.notifyAllWorkFinished();
        AbstractSchedulingService.ServiceState localServiceState9 = AbstractSchedulingService.this.mState;
        AbstractSchedulingService.ServiceState localServiceState10 = AbstractSchedulingService.ServiceState.DISABLED;
        if (localServiceState9 == localServiceState10)
          return;
        AbstractSchedulingService localAbstractSchedulingService4 = AbstractSchedulingService.this;
        int n = paramMessage.arg1;
        localAbstractSchedulingService4.stopSelf(n);
        return;
      case 3:
        DownloadProgress localDownloadProgress = (DownloadProgress)paramMessage.obj;
        handleProgress(localDownloadProgress);
        return;
      case 5:
        int i1 = paramMessage.arg1;
        handleFinalCheckInitCompletedMessage(i1);
        return;
      case 4:
        handleCheckInitCompletedMessage();
        return;
      case 6:
        AbstractSchedulingService.DisableReason localDisableReason = (AbstractSchedulingService.DisableReason)paramMessage.obj;
        handleUpdateEnabled(localDisableReason);
        return;
      case 7:
      }
      int i2 = paramMessage.arg1;
      handleCancelDownloads(i2);
    }
  }

  public static enum DisableReason
  {
    static
    {
      CONNECTIVITY_LOST = new DisableReason("CONNECTIVITY_LOST", 3);
      DEVICE_NOT_AUTHORIZED = new DisableReason("DEVICE_NOT_AUTHORIZED", 4);
      DOWNLOAD_PAUSED = new DisableReason("DOWNLOAD_PAUSED", 5);
      DisableReason[] arrayOfDisableReason = new DisableReason[6];
      DisableReason localDisableReason1 = HIGH_SPEED_LOST;
      arrayOfDisableReason[0] = localDisableReason1;
      DisableReason localDisableReason2 = OUT_OF_SPACE;
      arrayOfDisableReason[1] = localDisableReason2;
      DisableReason localDisableReason3 = SUCCESSIVE_FAILURES;
      arrayOfDisableReason[2] = localDisableReason3;
      DisableReason localDisableReason4 = CONNECTIVITY_LOST;
      arrayOfDisableReason[3] = localDisableReason4;
      DisableReason localDisableReason5 = DEVICE_NOT_AUTHORIZED;
      arrayOfDisableReason[4] = localDisableReason5;
      DisableReason localDisableReason6 = DOWNLOAD_PAUSED;
      arrayOfDisableReason[5] = localDisableReason6;
    }
  }

  private static enum ServiceState
  {
    static
    {
      INITIALIZED = new ServiceState("INITIALIZED", 2);
      WORKING = new ServiceState("WORKING", 3);
      DISABLED = new ServiceState("DISABLED", 4);
      FINISHED = new ServiceState("FINISHED", 5);
      ServiceState[] arrayOfServiceState = new ServiceState[6];
      ServiceState localServiceState1 = NOT_STARTED;
      arrayOfServiceState[0] = localServiceState1;
      ServiceState localServiceState2 = WAITING_FOR_SERVICES;
      arrayOfServiceState[1] = localServiceState2;
      ServiceState localServiceState3 = INITIALIZED;
      arrayOfServiceState[2] = localServiceState3;
      ServiceState localServiceState4 = WORKING;
      arrayOfServiceState[3] = localServiceState4;
      ServiceState localServiceState5 = DISABLED;
      arrayOfServiceState[4] = localServiceState5;
      ServiceState localServiceState6 = FINISHED;
      arrayOfServiceState[5] = localServiceState6;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.AbstractSchedulingService
 * JD-Core Version:    0.6.2
 */