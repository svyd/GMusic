package android.support.v7.media;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.hardware.display.DisplayManagerCompat;
import android.util.Log;
import android.view.Display;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public final class MediaRouter
{
  private static final boolean DEBUG = Log.isLoggable("MediaRouter", 3);
  static GlobalMediaRouter sGlobal;
  final ArrayList<CallbackRecord> mCallbackRecords;
  final Context mContext;

  MediaRouter(Context paramContext)
  {
    ArrayList localArrayList = new ArrayList();
    this.mCallbackRecords = localArrayList;
    this.mContext = paramContext;
  }

  static void checkCallingThread()
  {
    Looper localLooper1 = Looper.myLooper();
    Looper localLooper2 = Looper.getMainLooper();
    if (localLooper1 == localLooper2)
      return;
    throw new IllegalStateException("The media router service must only be accessed on the application's main thread.");
  }

  static <T> boolean equal(T paramT1, T paramT2)
  {
    if ((paramT1 == paramT2) || ((paramT1 != null) && (paramT2 != null) && (paramT1.equals(paramT2))));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private int findCallbackRecord(Callback paramCallback)
  {
    int i = this.mCallbackRecords.size();
    int j = 0;
    if (j < i)
      if (((CallbackRecord)this.mCallbackRecords.get(j)).mCallback != paramCallback);
    while (true)
    {
      return j;
      j += 1;
      break;
      j = -1;
    }
  }

  public static MediaRouter getInstance(Context paramContext)
  {
    if (paramContext == null)
      throw new IllegalArgumentException("context must not be null");
    checkCallingThread();
    if (sGlobal == null)
    {
      Context localContext = paramContext.getApplicationContext();
      sGlobal = new GlobalMediaRouter(localContext);
      sGlobal.start();
    }
    return sGlobal.getRouter(paramContext);
  }

  public void addCallback(MediaRouteSelector paramMediaRouteSelector, Callback paramCallback)
  {
    addCallback(paramMediaRouteSelector, paramCallback, 0);
  }

  public void addCallback(MediaRouteSelector paramMediaRouteSelector, Callback paramCallback, int paramInt)
  {
    if (paramMediaRouteSelector == null)
      throw new IllegalArgumentException("selector must not be null");
    if (paramCallback == null)
      throw new IllegalArgumentException("callback must not be null");
    checkCallingThread();
    if (DEBUG)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("addCallback: selector=").append(paramMediaRouteSelector).append(", callback=").append(paramCallback).append(", flags=");
      String str1 = Integer.toHexString(paramInt);
      String str2 = str1;
      int i = Log.d("MediaRouter", str2);
    }
    int j = findCallbackRecord(paramCallback);
    CallbackRecord localCallbackRecord;
    if (j < 0)
    {
      localCallbackRecord = new CallbackRecord(this, paramCallback);
      boolean bool = this.mCallbackRecords.add(localCallbackRecord);
    }
    while (true)
    {
      int k = 0;
      if (((localCallbackRecord.mFlags ^ 0xFFFFFFFF) & paramInt) != 0)
      {
        int m = localCallbackRecord.mFlags | paramInt;
        localCallbackRecord.mFlags = m;
        k = 1;
      }
      if (!localCallbackRecord.mSelector.contains(paramMediaRouteSelector))
      {
        MediaRouteSelector localMediaRouteSelector1 = localCallbackRecord.mSelector;
        MediaRouteSelector localMediaRouteSelector2 = new MediaRouteSelector.Builder(localMediaRouteSelector1).addSelector(paramMediaRouteSelector).build();
        localCallbackRecord.mSelector = localMediaRouteSelector2;
        k = 1;
      }
      if (k == 0)
        return;
      sGlobal.updateDiscoveryRequest();
      return;
      localCallbackRecord = (CallbackRecord)this.mCallbackRecords.get(j);
    }
  }

  public void addProvider(MediaRouteProvider paramMediaRouteProvider)
  {
    if (paramMediaRouteProvider == null)
      throw new IllegalArgumentException("providerInstance must not be null");
    checkCallingThread();
    if (DEBUG)
    {
      String str = "addProvider: " + paramMediaRouteProvider;
      int i = Log.d("MediaRouter", str);
    }
    sGlobal.addProvider(paramMediaRouteProvider);
  }

  public void addRemoteControlClient(Object paramObject)
  {
    if (paramObject == null)
      throw new IllegalArgumentException("remoteControlClient must not be null");
    checkCallingThread();
    if (DEBUG)
    {
      String str = "addRemoteControlClient: " + paramObject;
      int i = Log.d("MediaRouter", str);
    }
    sGlobal.addRemoteControlClient(paramObject);
  }

  public RouteInfo getDefaultRoute()
  {
    checkCallingThread();
    return sGlobal.getDefaultRoute();
  }

  public List<RouteInfo> getRoutes()
  {
    checkCallingThread();
    return sGlobal.getRoutes();
  }

  public RouteInfo getSelectedRoute()
  {
    checkCallingThread();
    return sGlobal.getSelectedRoute();
  }

  public boolean isRouteAvailable(MediaRouteSelector paramMediaRouteSelector, int paramInt)
  {
    if (paramMediaRouteSelector == null)
      throw new IllegalArgumentException("selector must not be null");
    checkCallingThread();
    return sGlobal.isRouteAvailable(paramMediaRouteSelector, paramInt);
  }

  public void removeCallback(Callback paramCallback)
  {
    if (paramCallback == null)
      throw new IllegalArgumentException("callback must not be null");
    checkCallingThread();
    if (DEBUG)
    {
      String str = "removeCallback: callback=" + paramCallback;
      int i = Log.d("MediaRouter", str);
    }
    int j = findCallbackRecord(paramCallback);
    if (j < 0)
      return;
    Object localObject = this.mCallbackRecords.remove(j);
    sGlobal.updateDiscoveryRequest();
  }

  public void removeProvider(MediaRouteProvider paramMediaRouteProvider)
  {
    if (paramMediaRouteProvider == null)
      throw new IllegalArgumentException("providerInstance must not be null");
    checkCallingThread();
    if (DEBUG)
    {
      String str = "removeProvider: " + paramMediaRouteProvider;
      int i = Log.d("MediaRouter", str);
    }
    sGlobal.removeProvider(paramMediaRouteProvider);
  }

  public void selectRoute(RouteInfo paramRouteInfo)
  {
    if (paramRouteInfo == null)
      throw new IllegalArgumentException("route must not be null");
    checkCallingThread();
    if (DEBUG)
    {
      String str = "selectRoute: " + paramRouteInfo;
      int i = Log.d("MediaRouter", str);
    }
    sGlobal.selectRoute(paramRouteInfo);
  }

  private static final class GlobalMediaRouter
    implements SystemMediaRouteProvider.SyncCallback
  {
    private final Context mApplicationContext;
    private final MediaRouter mApplicationRouter;
    private final CallbackHandler mCallbackHandler;
    private MediaRouter.RouteInfo mDefaultRoute;
    private MediaRouteDiscoveryRequest mDiscoveryRequest;
    private final DisplayManagerCompat mDisplayManager;
    private final RemoteControlClientCompat.PlaybackInfo mPlaybackInfo;
    private final ProviderCallback mProviderCallback;
    private final ArrayList<MediaRouter.ProviderInfo> mProviders;
    private RegisteredMediaRouteProviderWatcher mRegisteredProviderWatcher;
    private final ArrayList<RemoteControlClientRecord> mRemoteControlClients;
    private final ArrayList<WeakReference<MediaRouter>> mRouters;
    private final ArrayList<MediaRouter.RouteInfo> mRoutes;
    private MediaRouter.RouteInfo mSelectedRoute;
    private MediaRouteProvider.RouteController mSelectedRouteController;
    private final SystemMediaRouteProvider mSystemProvider;

    GlobalMediaRouter(Context paramContext)
    {
      ArrayList localArrayList1 = new ArrayList();
      this.mRouters = localArrayList1;
      ArrayList localArrayList2 = new ArrayList();
      this.mRoutes = localArrayList2;
      ArrayList localArrayList3 = new ArrayList();
      this.mProviders = localArrayList3;
      ArrayList localArrayList4 = new ArrayList();
      this.mRemoteControlClients = localArrayList4;
      RemoteControlClientCompat.PlaybackInfo localPlaybackInfo = new RemoteControlClientCompat.PlaybackInfo();
      this.mPlaybackInfo = localPlaybackInfo;
      ProviderCallback localProviderCallback = new ProviderCallback(null);
      this.mProviderCallback = localProviderCallback;
      CallbackHandler localCallbackHandler = new CallbackHandler(null);
      this.mCallbackHandler = localCallbackHandler;
      this.mApplicationContext = paramContext;
      DisplayManagerCompat localDisplayManagerCompat = DisplayManagerCompat.getInstance(paramContext);
      this.mDisplayManager = localDisplayManagerCompat;
      MediaRouter localMediaRouter = getRouter(paramContext);
      this.mApplicationRouter = localMediaRouter;
      SystemMediaRouteProvider localSystemMediaRouteProvider1 = SystemMediaRouteProvider.obtain(paramContext, this);
      this.mSystemProvider = localSystemMediaRouteProvider1;
      SystemMediaRouteProvider localSystemMediaRouteProvider2 = this.mSystemProvider;
      addProvider(localSystemMediaRouteProvider2);
    }

    private String assignRouteUniqueId(MediaRouter.ProviderInfo paramProviderInfo, String paramString)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      String str1 = paramProviderInfo.getPackageName();
      Object localObject = str1 + ":" + paramString;
      if (findRouteByUniqueId((String)localObject) < 0)
        return localObject;
      int i = 2;
      while (true)
      {
        Locale localLocale = Locale.US;
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = localObject;
        Integer localInteger = Integer.valueOf(i);
        arrayOfObject[1] = localInteger;
        String str2 = String.format(localLocale, "%s_%d", arrayOfObject);
        if (findRouteByUniqueId(str2) < 0)
        {
          localObject = str2;
          break;
        }
        i += 1;
      }
    }

    private MediaRouter.RouteInfo chooseFallbackRoute()
    {
      Iterator localIterator = this.mRoutes.iterator();
      MediaRouter.RouteInfo localRouteInfo1;
      MediaRouter.RouteInfo localRouteInfo2;
      do
      {
        if (!localIterator.hasNext())
          break;
        localRouteInfo1 = (MediaRouter.RouteInfo)localIterator.next();
        localRouteInfo2 = this.mDefaultRoute;
      }
      while ((localRouteInfo1 == localRouteInfo2) || (!isSystemLiveAudioOnlyRoute(localRouteInfo1)) || (!isRouteSelectable(localRouteInfo1)));
      while (true)
      {
        return localRouteInfo1;
        localRouteInfo1 = this.mDefaultRoute;
      }
    }

    private int findProviderInfo(MediaRouteProvider paramMediaRouteProvider)
    {
      int i = this.mProviders.size();
      int j = 0;
      if (j < i)
        if (MediaRouter.ProviderInfo.access$500((MediaRouter.ProviderInfo)this.mProviders.get(j)) != paramMediaRouteProvider);
      while (true)
      {
        return j;
        j += 1;
        break;
        j = -1;
      }
    }

    private int findRemoteControlClientRecord(Object paramObject)
    {
      int i = this.mRemoteControlClients.size();
      int j = 0;
      if (j < i)
        if (((RemoteControlClientRecord)this.mRemoteControlClients.get(j)).getRemoteControlClient() != paramObject);
      while (true)
      {
        return j;
        j += 1;
        break;
        j = -1;
      }
    }

    private int findRouteByUniqueId(String paramString)
    {
      int i = this.mRoutes.size();
      int j = 0;
      if (j < i)
        if (!MediaRouter.RouteInfo.access$700((MediaRouter.RouteInfo)this.mRoutes.get(j)).equals(paramString));
      while (true)
      {
        return j;
        j += 1;
        break;
        j = -1;
      }
    }

    private boolean isRouteSelectable(MediaRouter.RouteInfo paramRouteInfo)
    {
      if ((MediaRouter.RouteInfo.access$800(paramRouteInfo) != null) && (MediaRouter.RouteInfo.access$300(paramRouteInfo)));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    private boolean isSystemDefaultRoute(MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteProvider localMediaRouteProvider = paramRouteInfo.getProviderInstance();
      SystemMediaRouteProvider localSystemMediaRouteProvider = this.mSystemProvider;
      if ((localMediaRouteProvider == localSystemMediaRouteProvider) && (MediaRouter.RouteInfo.access$000(paramRouteInfo).equals("DEFAULT_ROUTE")));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    private boolean isSystemLiveAudioOnlyRoute(MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteProvider localMediaRouteProvider = paramRouteInfo.getProviderInstance();
      SystemMediaRouteProvider localSystemMediaRouteProvider = this.mSystemProvider;
      if ((localMediaRouteProvider == localSystemMediaRouteProvider) && (paramRouteInfo.supportsControlCategory("android.media.intent.category.LIVE_AUDIO")) && (!paramRouteInfo.supportsControlCategory("android.media.intent.category.LIVE_VIDEO")));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    private void setSelectedRouteInternal(MediaRouter.RouteInfo paramRouteInfo)
    {
      if (this.mSelectedRoute == paramRouteInfo)
        return;
      if (this.mSelectedRoute != null)
      {
        if (MediaRouter.DEBUG)
        {
          StringBuilder localStringBuilder1 = new StringBuilder().append("Route unselected: ");
          MediaRouter.RouteInfo localRouteInfo1 = this.mSelectedRoute;
          String str1 = localRouteInfo1;
          int i = Log.d("MediaRouter", str1);
        }
        CallbackHandler localCallbackHandler1 = this.mCallbackHandler;
        MediaRouter.RouteInfo localRouteInfo2 = this.mSelectedRoute;
        localCallbackHandler1.post(263, localRouteInfo2);
        if (this.mSelectedRouteController != null)
        {
          this.mSelectedRouteController.onUnselect();
          this.mSelectedRouteController.onRelease();
          this.mSelectedRouteController = null;
        }
      }
      this.mSelectedRoute = paramRouteInfo;
      if (this.mSelectedRoute != null)
      {
        MediaRouteProvider localMediaRouteProvider = paramRouteInfo.getProviderInstance();
        String str2 = MediaRouter.RouteInfo.access$000(paramRouteInfo);
        MediaRouteProvider.RouteController localRouteController = localMediaRouteProvider.onCreateRouteController(str2);
        this.mSelectedRouteController = localRouteController;
        if (this.mSelectedRouteController != null)
          this.mSelectedRouteController.onSelect();
        if (MediaRouter.DEBUG)
        {
          StringBuilder localStringBuilder2 = new StringBuilder().append("Route selected: ");
          MediaRouter.RouteInfo localRouteInfo3 = this.mSelectedRoute;
          String str3 = localRouteInfo3;
          int j = Log.d("MediaRouter", str3);
        }
        CallbackHandler localCallbackHandler2 = this.mCallbackHandler;
        MediaRouter.RouteInfo localRouteInfo4 = this.mSelectedRoute;
        localCallbackHandler2.post(262, localRouteInfo4);
      }
      updatePlaybackInfoFromSelectedRoute();
    }

    private void updatePlaybackInfoFromSelectedRoute()
    {
      if (this.mSelectedRoute == null)
        return;
      RemoteControlClientCompat.PlaybackInfo localPlaybackInfo1 = this.mPlaybackInfo;
      int i = this.mSelectedRoute.getVolume();
      localPlaybackInfo1.volume = i;
      RemoteControlClientCompat.PlaybackInfo localPlaybackInfo2 = this.mPlaybackInfo;
      int j = this.mSelectedRoute.getVolumeMax();
      localPlaybackInfo2.volumeMax = j;
      RemoteControlClientCompat.PlaybackInfo localPlaybackInfo3 = this.mPlaybackInfo;
      int k = this.mSelectedRoute.getVolumeHandling();
      localPlaybackInfo3.volumeHandling = k;
      RemoteControlClientCompat.PlaybackInfo localPlaybackInfo4 = this.mPlaybackInfo;
      int m = this.mSelectedRoute.getPlaybackStream();
      localPlaybackInfo4.playbackStream = m;
      RemoteControlClientCompat.PlaybackInfo localPlaybackInfo5 = this.mPlaybackInfo;
      int n = this.mSelectedRoute.getPlaybackType();
      localPlaybackInfo5.playbackType = n;
      int i1 = this.mRemoteControlClients.size();
      int i2 = 0;
      while (true)
      {
        if (i2 >= i1)
          return;
        ((RemoteControlClientRecord)this.mRemoteControlClients.get(i2)).updatePlaybackInfo();
        i2 += 1;
      }
    }

    private void updateProviderContents(MediaRouter.ProviderInfo paramProviderInfo, MediaRouteProviderDescriptor paramMediaRouteProviderDescriptor)
    {
      if (!paramProviderInfo.updateDescriptor(paramMediaRouteProviderDescriptor))
        return;
      int i = 0;
      boolean bool1 = false;
      if (paramMediaRouteProviderDescriptor != null)
      {
        if (!paramMediaRouteProviderDescriptor.isValid())
          break label576;
        List localList = paramMediaRouteProviderDescriptor.getRoutes();
        int j = localList.size();
        int k = 0;
        int m = i;
        if (k < j)
        {
          MediaRouteDescriptor localMediaRouteDescriptor = (MediaRouteDescriptor)localList.get(k);
          String str1 = localMediaRouteDescriptor.getId();
          int n = paramProviderInfo.findRouteByDescriptorId(str1);
          MediaRouter.RouteInfo localRouteInfo1;
          if (n < 0)
          {
            GlobalMediaRouter localGlobalMediaRouter = this;
            MediaRouter.ProviderInfo localProviderInfo1 = paramProviderInfo;
            String str2 = localGlobalMediaRouter.assignRouteUniqueId(localProviderInfo1, str1);
            MediaRouter.ProviderInfo localProviderInfo2 = paramProviderInfo;
            localRouteInfo1 = new MediaRouter.RouteInfo(localProviderInfo2, str1, str2);
            ArrayList localArrayList1 = MediaRouter.ProviderInfo.access$600(paramProviderInfo);
            i = m + 1;
            localArrayList1.add(m, localRouteInfo1);
            boolean bool2 = this.mRoutes.add(localRouteInfo1);
            int i1 = localRouteInfo1.updateDescriptor(localMediaRouteDescriptor);
            if (MediaRouter.DEBUG)
            {
              String str3 = "Route added: " + localRouteInfo1;
              int i2 = Log.d("MediaRouter", str3);
            }
            this.mCallbackHandler.post(257, localRouteInfo1);
          }
          while (true)
          {
            k += 1;
            m = i;
            break;
            if (n < m)
            {
              String str4 = "Ignoring route descriptor with duplicate id: " + localMediaRouteDescriptor;
              int i3 = Log.w("MediaRouter", str4);
              i = m;
            }
            else
            {
              localRouteInfo1 = (MediaRouter.RouteInfo)MediaRouter.ProviderInfo.access$600(paramProviderInfo).get(n);
              ArrayList localArrayList2 = MediaRouter.ProviderInfo.access$600(paramProviderInfo);
              i = m + 1;
              Collections.swap(localArrayList2, n, m);
              int i4 = localRouteInfo1.updateDescriptor(localMediaRouteDescriptor);
              if (i4 != 0)
              {
                if ((i4 & 0x1) != 0)
                {
                  if (MediaRouter.DEBUG)
                  {
                    String str5 = "Route changed: " + localRouteInfo1;
                    int i5 = Log.d("MediaRouter", str5);
                  }
                  this.mCallbackHandler.post(259, localRouteInfo1);
                }
                if ((i4 & 0x2) != 0)
                {
                  if (MediaRouter.DEBUG)
                  {
                    String str6 = "Route volume changed: " + localRouteInfo1;
                    int i6 = Log.d("MediaRouter", str6);
                  }
                  this.mCallbackHandler.post(260, localRouteInfo1);
                }
                if ((i4 & 0x4) != 0)
                {
                  if (MediaRouter.DEBUG)
                  {
                    String str7 = "Route presentation display changed: " + localRouteInfo1;
                    int i7 = Log.d("MediaRouter", str7);
                  }
                  this.mCallbackHandler.post(261, localRouteInfo1);
                }
                MediaRouter.RouteInfo localRouteInfo2 = this.mSelectedRoute;
                if (localRouteInfo1 == localRouteInfo2)
                  bool1 = true;
              }
            }
          }
        }
        i = m;
      }
      while (true)
      {
        int i8 = MediaRouter.ProviderInfo.access$600(paramProviderInfo).size() + -1;
        while (i8 >= i)
        {
          MediaRouter.RouteInfo localRouteInfo3 = (MediaRouter.RouteInfo)MediaRouter.ProviderInfo.access$600(paramProviderInfo).get(i8);
          int i9 = localRouteInfo3.updateDescriptor(null);
          boolean bool3 = this.mRoutes.remove(localRouteInfo3);
          i8 += -1;
        }
        label576: StringBuilder localStringBuilder1 = new StringBuilder().append("Ignoring invalid provider descriptor: ");
        MediaRouteProviderDescriptor localMediaRouteProviderDescriptor = paramMediaRouteProviderDescriptor;
        String str8 = localMediaRouteProviderDescriptor;
        int i10 = Log.w("MediaRouter", str8);
      }
      updateSelectedRouteIfNeeded(bool1);
      int i11 = MediaRouter.ProviderInfo.access$600(paramProviderInfo).size() + -1;
      while (i11 >= i)
      {
        MediaRouter.RouteInfo localRouteInfo4 = (MediaRouter.RouteInfo)MediaRouter.ProviderInfo.access$600(paramProviderInfo).remove(i11);
        if (MediaRouter.DEBUG)
        {
          String str9 = "Route removed: " + localRouteInfo4;
          int i12 = Log.d("MediaRouter", str9);
        }
        this.mCallbackHandler.post(258, localRouteInfo4);
        i11 += -1;
      }
      if (MediaRouter.DEBUG)
      {
        StringBuilder localStringBuilder2 = new StringBuilder().append("Provider changed: ");
        MediaRouter.ProviderInfo localProviderInfo3 = paramProviderInfo;
        String str10 = localProviderInfo3;
        int i13 = Log.d("MediaRouter", str10);
      }
      CallbackHandler localCallbackHandler = this.mCallbackHandler;
      MediaRouter.ProviderInfo localProviderInfo4 = paramProviderInfo;
      localCallbackHandler.post(515, localProviderInfo4);
    }

    private void updateProviderDescriptor(MediaRouteProvider paramMediaRouteProvider, MediaRouteProviderDescriptor paramMediaRouteProviderDescriptor)
    {
      int i = findProviderInfo(paramMediaRouteProvider);
      if (i < 0)
        return;
      MediaRouter.ProviderInfo localProviderInfo = (MediaRouter.ProviderInfo)this.mProviders.get(i);
      updateProviderContents(localProviderInfo, paramMediaRouteProviderDescriptor);
    }

    private void updateSelectedRouteIfNeeded(boolean paramBoolean)
    {
      if (this.mDefaultRoute != null)
      {
        MediaRouter.RouteInfo localRouteInfo1 = this.mDefaultRoute;
        if (!isRouteSelectable(localRouteInfo1))
        {
          StringBuilder localStringBuilder1 = new StringBuilder().append("Clearing the default route because it is no longer selectable: ");
          MediaRouter.RouteInfo localRouteInfo2 = this.mDefaultRoute;
          String str1 = localRouteInfo2;
          int i = Log.i("MediaRouter", str1);
          this.mDefaultRoute = null;
        }
      }
      if ((this.mDefaultRoute == null) && (!this.mRoutes.isEmpty()))
      {
        Iterator localIterator = this.mRoutes.iterator();
        while (localIterator.hasNext())
        {
          MediaRouter.RouteInfo localRouteInfo3 = (MediaRouter.RouteInfo)localIterator.next();
          if ((isSystemDefaultRoute(localRouteInfo3)) && (isRouteSelectable(localRouteInfo3)))
          {
            this.mDefaultRoute = localRouteInfo3;
            StringBuilder localStringBuilder2 = new StringBuilder().append("Found default route: ");
            MediaRouter.RouteInfo localRouteInfo4 = this.mDefaultRoute;
            String str2 = localRouteInfo4;
            int j = Log.i("MediaRouter", str2);
          }
        }
      }
      if (this.mSelectedRoute != null)
      {
        MediaRouter.RouteInfo localRouteInfo5 = this.mSelectedRoute;
        if (!isRouteSelectable(localRouteInfo5))
        {
          StringBuilder localStringBuilder3 = new StringBuilder().append("Unselecting the current route because it is no longer selectable: ");
          MediaRouter.RouteInfo localRouteInfo6 = this.mSelectedRoute;
          String str3 = localRouteInfo6;
          int k = Log.i("MediaRouter", str3);
          setSelectedRouteInternal(null);
        }
      }
      if (this.mSelectedRoute == null)
      {
        MediaRouter.RouteInfo localRouteInfo7 = chooseFallbackRoute();
        setSelectedRouteInternal(localRouteInfo7);
        return;
      }
      if (!paramBoolean)
        return;
      updatePlaybackInfoFromSelectedRoute();
    }

    public void addProvider(MediaRouteProvider paramMediaRouteProvider)
    {
      if (findProviderInfo(paramMediaRouteProvider) >= 0)
        return;
      MediaRouter.ProviderInfo localProviderInfo = new MediaRouter.ProviderInfo(paramMediaRouteProvider);
      boolean bool = this.mProviders.add(localProviderInfo);
      if (MediaRouter.DEBUG)
      {
        String str = "Provider added: " + localProviderInfo;
        int i = Log.d("MediaRouter", str);
      }
      this.mCallbackHandler.post(513, localProviderInfo);
      MediaRouteProviderDescriptor localMediaRouteProviderDescriptor = paramMediaRouteProvider.getDescriptor();
      updateProviderContents(localProviderInfo, localMediaRouteProviderDescriptor);
      ProviderCallback localProviderCallback = this.mProviderCallback;
      paramMediaRouteProvider.setCallback(localProviderCallback);
      MediaRouteDiscoveryRequest localMediaRouteDiscoveryRequest = this.mDiscoveryRequest;
      paramMediaRouteProvider.setDiscoveryRequest(localMediaRouteDiscoveryRequest);
    }

    public void addRemoteControlClient(Object paramObject)
    {
      if (findRemoteControlClientRecord(paramObject) >= 0)
        return;
      RemoteControlClientRecord localRemoteControlClientRecord = new RemoteControlClientRecord(paramObject);
      boolean bool = this.mRemoteControlClients.add(localRemoteControlClientRecord);
    }

    public ContentResolver getContentResolver()
    {
      return this.mApplicationContext.getContentResolver();
    }

    public MediaRouter.RouteInfo getDefaultRoute()
    {
      if (this.mDefaultRoute == null)
        throw new IllegalStateException("There is no default route.  The media router has not yet been fully initialized.");
      return this.mDefaultRoute;
    }

    public MediaRouter getRouter(Context paramContext)
    {
      int i = this.mRouters.size();
      MediaRouter localMediaRouter1;
      do
        while (true)
        {
          i += -1;
          if (i < 0)
            break label66;
          localMediaRouter1 = (MediaRouter)((WeakReference)this.mRouters.get(i)).get();
          if (localMediaRouter1 != null)
            break;
          Object localObject1 = this.mRouters.remove(i);
        }
      while (localMediaRouter1.mContext != paramContext);
      label66: MediaRouter localMediaRouter2;
      for (Object localObject2 = localMediaRouter1; ; localObject2 = localMediaRouter2)
      {
        return localObject2;
        localMediaRouter2 = new MediaRouter(paramContext);
        ArrayList localArrayList = this.mRouters;
        WeakReference localWeakReference = new WeakReference(localMediaRouter2);
        boolean bool = localArrayList.add(localWeakReference);
      }
    }

    public List<MediaRouter.RouteInfo> getRoutes()
    {
      return this.mRoutes;
    }

    public MediaRouter.RouteInfo getSelectedRoute()
    {
      if (this.mSelectedRoute == null)
        throw new IllegalStateException("There is no currently selected route.  The media router has not yet been fully initialized.");
      return this.mSelectedRoute;
    }

    public MediaRouter.RouteInfo getSystemRouteByDescriptorId(String paramString)
    {
      SystemMediaRouteProvider localSystemMediaRouteProvider = this.mSystemProvider;
      int i = findProviderInfo(localSystemMediaRouteProvider);
      MediaRouter.ProviderInfo localProviderInfo;
      int j;
      if (i >= 0)
      {
        localProviderInfo = (MediaRouter.ProviderInfo)this.mProviders.get(i);
        j = localProviderInfo.findRouteByDescriptorId(paramString);
        if (j < 0);
      }
      for (MediaRouter.RouteInfo localRouteInfo = (MediaRouter.RouteInfo)MediaRouter.ProviderInfo.access$600(localProviderInfo).get(j); ; localRouteInfo = null)
        return localRouteInfo;
    }

    public boolean isRouteAvailable(MediaRouteSelector paramMediaRouteSelector, int paramInt)
    {
      int i = this.mRoutes.size();
      int j = 0;
      if (j < i)
      {
        MediaRouter.RouteInfo localRouteInfo = (MediaRouter.RouteInfo)this.mRoutes.get(j);
        if (((paramInt & 0x1) != 0) && (localRouteInfo.isDefault()));
        while (!localRouteInfo.matchesSelector(paramMediaRouteSelector))
        {
          j += 1;
          break;
        }
      }
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public void removeProvider(MediaRouteProvider paramMediaRouteProvider)
    {
      int i = findProviderInfo(paramMediaRouteProvider);
      if (i < 0)
        return;
      paramMediaRouteProvider.setCallback(null);
      paramMediaRouteProvider.setDiscoveryRequest(null);
      MediaRouter.ProviderInfo localProviderInfo = (MediaRouter.ProviderInfo)this.mProviders.get(i);
      updateProviderContents(localProviderInfo, null);
      if (MediaRouter.DEBUG)
      {
        String str = "Provider removed: " + localProviderInfo;
        int j = Log.d("MediaRouter", str);
      }
      this.mCallbackHandler.post(514, localProviderInfo);
      Object localObject = this.mProviders.remove(i);
    }

    public void requestSetVolume(MediaRouter.RouteInfo paramRouteInfo, int paramInt)
    {
      MediaRouter.RouteInfo localRouteInfo = this.mSelectedRoute;
      if (paramRouteInfo != localRouteInfo)
        return;
      if (this.mSelectedRouteController == null)
        return;
      this.mSelectedRouteController.onSetVolume(paramInt);
    }

    public void requestUpdateVolume(MediaRouter.RouteInfo paramRouteInfo, int paramInt)
    {
      MediaRouter.RouteInfo localRouteInfo = this.mSelectedRoute;
      if (paramRouteInfo != localRouteInfo)
        return;
      if (this.mSelectedRouteController == null)
        return;
      this.mSelectedRouteController.onUpdateVolume(paramInt);
    }

    public void selectRoute(MediaRouter.RouteInfo paramRouteInfo)
    {
      if (!this.mRoutes.contains(paramRouteInfo))
      {
        String str1 = "Ignoring attempt to select removed route: " + paramRouteInfo;
        int i = Log.w("MediaRouter", str1);
        return;
      }
      if (!MediaRouter.RouteInfo.access$300(paramRouteInfo))
      {
        String str2 = "Ignoring attempt to select disabled route: " + paramRouteInfo;
        int j = Log.w("MediaRouter", str2);
        return;
      }
      setSelectedRouteInternal(paramRouteInfo);
    }

    public void sendControlRequest(MediaRouter.RouteInfo paramRouteInfo, Intent paramIntent, MediaRouter.ControlRequestCallback paramControlRequestCallback)
    {
      MediaRouter.RouteInfo localRouteInfo = this.mSelectedRoute;
      if ((paramRouteInfo == localRouteInfo) && (this.mSelectedRouteController != null) && (this.mSelectedRouteController.onControlRequest(paramIntent, paramControlRequestCallback)))
        return;
      if (paramControlRequestCallback == null)
        return;
      paramControlRequestCallback.onError(null, null);
    }

    public void start()
    {
      Context localContext = this.mApplicationContext;
      MediaRouter localMediaRouter = this.mApplicationRouter;
      RegisteredMediaRouteProviderWatcher localRegisteredMediaRouteProviderWatcher = new RegisteredMediaRouteProviderWatcher(localContext, localMediaRouter);
      this.mRegisteredProviderWatcher = localRegisteredMediaRouteProviderWatcher;
      this.mRegisteredProviderWatcher.start();
    }

    public void updateDiscoveryRequest()
    {
      int i = 0;
      boolean bool = false;
      MediaRouteSelector.Builder localBuilder1 = new MediaRouteSelector.Builder();
      int j = this.mRouters.size();
      while (true)
      {
        j += -1;
        if (j < 0)
          break;
        MediaRouter localMediaRouter = (MediaRouter)((WeakReference)this.mRouters.get(j)).get();
        if (localMediaRouter == null)
        {
          Object localObject = this.mRouters.remove(j);
        }
        else
        {
          int k = localMediaRouter.mCallbackRecords.size();
          int m = 0;
          while (m < k)
          {
            MediaRouter.CallbackRecord localCallbackRecord = (MediaRouter.CallbackRecord)localMediaRouter.mCallbackRecords.get(m);
            MediaRouteSelector localMediaRouteSelector1 = localCallbackRecord.mSelector;
            MediaRouteSelector.Builder localBuilder2 = localBuilder1.addSelector(localMediaRouteSelector1);
            if ((localCallbackRecord.mFlags & 0x1) != 0)
            {
              bool = true;
              i = 1;
            }
            if ((localCallbackRecord.mFlags & 0x4) != 0)
              i = 1;
            m += 1;
          }
        }
      }
      if (i != 0);
      for (MediaRouteSelector localMediaRouteSelector2 = localBuilder1.build(); (this.mDiscoveryRequest != null) && (this.mDiscoveryRequest.getSelector().equals(localMediaRouteSelector2)) && (this.mDiscoveryRequest.isActiveScan() != bool); localMediaRouteSelector2 = MediaRouteSelector.EMPTY)
        return;
      if ((localMediaRouteSelector2.isEmpty()) && (!bool))
        if (this.mDiscoveryRequest == null)
          return;
      MediaRouteDiscoveryRequest localMediaRouteDiscoveryRequest3;
      for (this.mDiscoveryRequest = null; ; this.mDiscoveryRequest = localMediaRouteDiscoveryRequest3)
      {
        if (MediaRouter.DEBUG)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("Updated discovery request: ");
          MediaRouteDiscoveryRequest localMediaRouteDiscoveryRequest1 = this.mDiscoveryRequest;
          String str = localMediaRouteDiscoveryRequest1;
          int n = Log.d("MediaRouter", str);
        }
        int i1 = this.mProviders.size();
        j = 0;
        while (true)
        {
          if (j >= i1)
            return;
          MediaRouteProvider localMediaRouteProvider = MediaRouter.ProviderInfo.access$500((MediaRouter.ProviderInfo)this.mProviders.get(j));
          MediaRouteDiscoveryRequest localMediaRouteDiscoveryRequest2 = this.mDiscoveryRequest;
          localMediaRouteProvider.setDiscoveryRequest(localMediaRouteDiscoveryRequest2);
          j += 1;
        }
        localMediaRouteDiscoveryRequest3 = new MediaRouteDiscoveryRequest(localMediaRouteSelector2, bool);
      }
    }

    private final class CallbackHandler extends Handler
    {
      private final ArrayList<MediaRouter.CallbackRecord> mTempCallbackRecords;

      private CallbackHandler()
      {
        ArrayList localArrayList = new ArrayList();
        this.mTempCallbackRecords = localArrayList;
      }

      private void invokeCallback(MediaRouter.CallbackRecord paramCallbackRecord, int paramInt, Object paramObject)
      {
        MediaRouter localMediaRouter = paramCallbackRecord.mRouter;
        MediaRouter.Callback localCallback = paramCallbackRecord.mCallback;
        switch (0xFF00 & paramInt)
        {
        default:
          return;
        case 256:
          MediaRouter.RouteInfo localRouteInfo = (MediaRouter.RouteInfo)paramObject;
          if (!paramCallbackRecord.filterRouteEvent(localRouteInfo))
            return;
          switch (paramInt)
          {
          default:
            return;
          case 257:
            localCallback.onRouteAdded(localMediaRouter, localRouteInfo);
            return;
          case 258:
            localCallback.onRouteRemoved(localMediaRouter, localRouteInfo);
            return;
          case 259:
            localCallback.onRouteChanged(localMediaRouter, localRouteInfo);
            return;
          case 260:
            localCallback.onRouteVolumeChanged(localMediaRouter, localRouteInfo);
            return;
          case 261:
            localCallback.onRoutePresentationDisplayChanged(localMediaRouter, localRouteInfo);
            return;
          case 262:
            localCallback.onRouteSelected(localMediaRouter, localRouteInfo);
            return;
          case 263:
          }
          localCallback.onRouteUnselected(localMediaRouter, localRouteInfo);
          return;
        case 512:
        }
        MediaRouter.ProviderInfo localProviderInfo = (MediaRouter.ProviderInfo)paramObject;
        switch (paramInt)
        {
        default:
          return;
        case 513:
          localCallback.onProviderAdded(localMediaRouter, localProviderInfo);
          return;
        case 514:
          localCallback.onProviderRemoved(localMediaRouter, localProviderInfo);
          return;
        case 515:
        }
        localCallback.onProviderChanged(localMediaRouter, localProviderInfo);
      }

      private void syncWithSystemProvider(int paramInt, Object paramObject)
      {
        switch (paramInt)
        {
        case 260:
        case 261:
        default:
          return;
        case 257:
          SystemMediaRouteProvider localSystemMediaRouteProvider1 = MediaRouter.GlobalMediaRouter.this.mSystemProvider;
          MediaRouter.RouteInfo localRouteInfo1 = (MediaRouter.RouteInfo)paramObject;
          localSystemMediaRouteProvider1.onSyncRouteAdded(localRouteInfo1);
          return;
        case 258:
          SystemMediaRouteProvider localSystemMediaRouteProvider2 = MediaRouter.GlobalMediaRouter.this.mSystemProvider;
          MediaRouter.RouteInfo localRouteInfo2 = (MediaRouter.RouteInfo)paramObject;
          localSystemMediaRouteProvider2.onSyncRouteRemoved(localRouteInfo2);
          return;
        case 259:
          SystemMediaRouteProvider localSystemMediaRouteProvider3 = MediaRouter.GlobalMediaRouter.this.mSystemProvider;
          MediaRouter.RouteInfo localRouteInfo3 = (MediaRouter.RouteInfo)paramObject;
          localSystemMediaRouteProvider3.onSyncRouteChanged(localRouteInfo3);
          return;
        case 262:
        }
        SystemMediaRouteProvider localSystemMediaRouteProvider4 = MediaRouter.GlobalMediaRouter.this.mSystemProvider;
        MediaRouter.RouteInfo localRouteInfo4 = (MediaRouter.RouteInfo)paramObject;
        localSystemMediaRouteProvider4.onSyncRouteSelected(localRouteInfo4);
      }

      public void handleMessage(Message paramMessage)
      {
        int i = paramMessage.what;
        Object localObject1 = paramMessage.obj;
        syncWithSystemProvider(i, localObject1);
        while (true)
        {
          MediaRouter localMediaRouter;
          try
          {
            j = MediaRouter.GlobalMediaRouter.this.mRouters.size();
            j += -1;
            if (j < 0)
              break;
            localMediaRouter = (MediaRouter)((WeakReference)MediaRouter.GlobalMediaRouter.this.mRouters.get(j)).get();
            if (localMediaRouter == null)
            {
              Object localObject2 = MediaRouter.GlobalMediaRouter.this.mRouters.remove(j);
              continue;
            }
          }
          finally
          {
            this.mTempCallbackRecords.clear();
          }
          ArrayList localArrayList1 = this.mTempCallbackRecords;
          ArrayList localArrayList2 = localMediaRouter.mCallbackRecords;
          boolean bool = localArrayList1.addAll(localArrayList2);
        }
        int k = this.mTempCallbackRecords.size();
        int j = 0;
        while (j < k)
        {
          MediaRouter.CallbackRecord localCallbackRecord = (MediaRouter.CallbackRecord)this.mTempCallbackRecords.get(j);
          invokeCallback(localCallbackRecord, i, localObject1);
          j += 1;
        }
        this.mTempCallbackRecords.clear();
      }

      public void post(int paramInt, Object paramObject)
      {
        obtainMessage(paramInt, paramObject).sendToTarget();
      }
    }

    private final class RemoteControlClientRecord
      implements RemoteControlClientCompat.VolumeCallback
    {
      private boolean mDisconnected;
      private final RemoteControlClientCompat mRccCompat;

      public RemoteControlClientRecord(Object arg2)
      {
        Object localObject;
        RemoteControlClientCompat localRemoteControlClientCompat = RemoteControlClientCompat.obtain(MediaRouter.GlobalMediaRouter.this.mApplicationContext, localObject);
        this.mRccCompat = localRemoteControlClientCompat;
        this.mRccCompat.setVolumeCallback(this);
        updatePlaybackInfo();
      }

      public Object getRemoteControlClient()
      {
        return this.mRccCompat.getRemoteControlClient();
      }

      public void onVolumeSetRequest(int paramInt)
      {
        if (this.mDisconnected)
          return;
        if (MediaRouter.GlobalMediaRouter.this.mSelectedRoute == null)
          return;
        MediaRouter.GlobalMediaRouter.this.mSelectedRoute.requestSetVolume(paramInt);
      }

      public void onVolumeUpdateRequest(int paramInt)
      {
        if (this.mDisconnected)
          return;
        if (MediaRouter.GlobalMediaRouter.this.mSelectedRoute == null)
          return;
        MediaRouter.GlobalMediaRouter.this.mSelectedRoute.requestUpdateVolume(paramInt);
      }

      public void updatePlaybackInfo()
      {
        RemoteControlClientCompat localRemoteControlClientCompat = this.mRccCompat;
        RemoteControlClientCompat.PlaybackInfo localPlaybackInfo = MediaRouter.GlobalMediaRouter.this.mPlaybackInfo;
        localRemoteControlClientCompat.setPlaybackInfo(localPlaybackInfo);
      }
    }

    private final class ProviderCallback extends MediaRouteProvider.Callback
    {
      private ProviderCallback()
      {
      }

      public void onDescriptorChanged(MediaRouteProvider paramMediaRouteProvider, MediaRouteProviderDescriptor paramMediaRouteProviderDescriptor)
      {
        MediaRouter.GlobalMediaRouter.this.updateProviderDescriptor(paramMediaRouteProvider, paramMediaRouteProviderDescriptor);
      }
    }
  }

  private static final class CallbackRecord
  {
    public final MediaRouter.Callback mCallback;
    public int mFlags;
    public final MediaRouter mRouter;
    public MediaRouteSelector mSelector;

    public CallbackRecord(MediaRouter paramMediaRouter, MediaRouter.Callback paramCallback)
    {
      this.mRouter = paramMediaRouter;
      this.mCallback = paramCallback;
      MediaRouteSelector localMediaRouteSelector = MediaRouteSelector.EMPTY;
      this.mSelector = localMediaRouteSelector;
    }

    public boolean filterRouteEvent(MediaRouter.RouteInfo paramRouteInfo)
    {
      if ((this.mFlags & 0x2) == 0)
      {
        MediaRouteSelector localMediaRouteSelector = this.mSelector;
        if (!paramRouteInfo.matchesSelector(localMediaRouteSelector))
          break label26;
      }
      label26: for (boolean bool = true; ; bool = false)
        return bool;
    }
  }

  public static abstract class ControlRequestCallback
  {
    public void onError(String paramString, Bundle paramBundle)
    {
    }

    public void onResult(Bundle paramBundle)
    {
    }
  }

  public static abstract class Callback
  {
    public void onProviderAdded(MediaRouter paramMediaRouter, MediaRouter.ProviderInfo paramProviderInfo)
    {
    }

    public void onProviderChanged(MediaRouter paramMediaRouter, MediaRouter.ProviderInfo paramProviderInfo)
    {
    }

    public void onProviderRemoved(MediaRouter paramMediaRouter, MediaRouter.ProviderInfo paramProviderInfo)
    {
    }

    public void onRouteAdded(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
    }

    public void onRouteChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
    }

    public void onRoutePresentationDisplayChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
    }

    public void onRouteRemoved(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
    }

    public void onRouteSelected(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
    }

    public void onRouteUnselected(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
    }

    public void onRouteVolumeChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
    }
  }

  public static final class ProviderInfo
  {
    private MediaRouteProviderDescriptor mDescriptor;
    private final MediaRouteProvider.ProviderMetadata mMetadata;
    private final MediaRouteProvider mProviderInstance;
    private final ArrayList<MediaRouter.RouteInfo> mRoutes;

    ProviderInfo(MediaRouteProvider paramMediaRouteProvider)
    {
      ArrayList localArrayList = new ArrayList();
      this.mRoutes = localArrayList;
      this.mProviderInstance = paramMediaRouteProvider;
      MediaRouteProvider.ProviderMetadata localProviderMetadata = paramMediaRouteProvider.getMetadata();
      this.mMetadata = localProviderMetadata;
    }

    int findRouteByDescriptorId(String paramString)
    {
      int i = this.mRoutes.size();
      int j = 0;
      if (j < i)
        if (!MediaRouter.RouteInfo.access$000((MediaRouter.RouteInfo)this.mRoutes.get(j)).equals(paramString));
      while (true)
      {
        return j;
        j += 1;
        break;
        j = -1;
      }
    }

    public String getPackageName()
    {
      return this.mMetadata.getPackageName();
    }

    public MediaRouteProvider getProviderInstance()
    {
      MediaRouter.checkCallingThread();
      return this.mProviderInstance;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder().append("MediaRouter.RouteProviderInfo{ packageName=");
      String str = getPackageName();
      return str + " }";
    }

    boolean updateDescriptor(MediaRouteProviderDescriptor paramMediaRouteProviderDescriptor)
    {
      if (this.mDescriptor != paramMediaRouteProviderDescriptor)
        this.mDescriptor = paramMediaRouteProviderDescriptor;
      for (boolean bool = true; ; bool = false)
        return bool;
    }
  }

  public static final class RouteInfo
  {
    private boolean mConnecting;
    private final ArrayList<IntentFilter> mControlFilters;
    private String mDescription;
    private MediaRouteDescriptor mDescriptor;
    private final String mDescriptorId;
    private boolean mEnabled;
    private Bundle mExtras;
    private String mName;
    private int mPlaybackStream;
    private int mPlaybackType;
    private Display mPresentationDisplay;
    private int mPresentationDisplayId;
    private final MediaRouter.ProviderInfo mProvider;
    private final String mUniqueId;
    private int mVolume;
    private int mVolumeHandling;
    private int mVolumeMax;

    RouteInfo(MediaRouter.ProviderInfo paramProviderInfo, String paramString1, String paramString2)
    {
      ArrayList localArrayList = new ArrayList();
      this.mControlFilters = localArrayList;
      this.mPresentationDisplayId = -1;
      this.mProvider = paramProviderInfo;
      this.mDescriptorId = paramString1;
      this.mUniqueId = paramString2;
    }

    public String getDescription()
    {
      return this.mDescription;
    }

    String getDescriptorId()
    {
      return this.mDescriptorId;
    }

    public String getId()
    {
      return this.mUniqueId;
    }

    public String getName()
    {
      return this.mName;
    }

    public int getPlaybackStream()
    {
      return this.mPlaybackStream;
    }

    public int getPlaybackType()
    {
      return this.mPlaybackType;
    }

    public MediaRouter.ProviderInfo getProvider()
    {
      return this.mProvider;
    }

    MediaRouteProvider getProviderInstance()
    {
      return this.mProvider.getProviderInstance();
    }

    public int getVolume()
    {
      return this.mVolume;
    }

    public int getVolumeHandling()
    {
      return this.mVolumeHandling;
    }

    public int getVolumeMax()
    {
      return this.mVolumeMax;
    }

    public boolean isConnecting()
    {
      return this.mConnecting;
    }

    public boolean isDefault()
    {
      MediaRouter.checkCallingThread();
      if (MediaRouter.sGlobal.getDefaultRoute() == this);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean isEnabled()
    {
      return this.mEnabled;
    }

    public boolean isSelected()
    {
      MediaRouter.checkCallingThread();
      if (MediaRouter.sGlobal.getSelectedRoute() == this);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean matchesSelector(MediaRouteSelector paramMediaRouteSelector)
    {
      if (paramMediaRouteSelector == null)
        throw new IllegalArgumentException("selector must not be null");
      MediaRouter.checkCallingThread();
      ArrayList localArrayList = this.mControlFilters;
      return paramMediaRouteSelector.matchesControlFilters(localArrayList);
    }

    public void requestSetVolume(int paramInt)
    {
      MediaRouter.checkCallingThread();
      MediaRouter.GlobalMediaRouter localGlobalMediaRouter = MediaRouter.sGlobal;
      int i = this.mVolumeMax;
      int j = Math.max(0, paramInt);
      int k = Math.min(i, j);
      localGlobalMediaRouter.requestSetVolume(this, k);
    }

    public void requestUpdateVolume(int paramInt)
    {
      MediaRouter.checkCallingThread();
      if (paramInt == 0)
        return;
      MediaRouter.sGlobal.requestUpdateVolume(this, paramInt);
    }

    public void select()
    {
      MediaRouter.checkCallingThread();
      MediaRouter.sGlobal.selectRoute(this);
    }

    public void sendControlRequest(Intent paramIntent, MediaRouter.ControlRequestCallback paramControlRequestCallback)
    {
      if (paramIntent == null)
        throw new IllegalArgumentException("intent must not be null");
      MediaRouter.checkCallingThread();
      MediaRouter.sGlobal.sendControlRequest(this, paramIntent, paramControlRequestCallback);
    }

    public boolean supportsControlCategory(String paramString)
    {
      if (paramString == null)
        throw new IllegalArgumentException("category must not be null");
      MediaRouter.checkCallingThread();
      int i = this.mControlFilters.size();
      int j = 0;
      if (j < i)
        if (!((IntentFilter)this.mControlFilters.get(j)).hasCategory(paramString));
      for (boolean bool = true; ; bool = false)
      {
        return bool;
        j += 1;
        break;
      }
    }

    public boolean supportsControlRequest(Intent paramIntent)
    {
      if (paramIntent == null)
        throw new IllegalArgumentException("intent must not be null");
      MediaRouter.checkCallingThread();
      ContentResolver localContentResolver = MediaRouter.sGlobal.getContentResolver();
      int i = this.mControlFilters.size();
      int j = 0;
      if (j < i)
        if (((IntentFilter)this.mControlFilters.get(j)).match(localContentResolver, paramIntent, true, "MediaRouter") < 0);
      for (boolean bool = true; ; bool = false)
      {
        return bool;
        j += 1;
        break;
      }
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("MediaRouter.RouteInfo{ uniqueId=");
      String str1 = this.mUniqueId;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(", name=");
      String str2 = this.mName;
      StringBuilder localStringBuilder3 = localStringBuilder2.append(str2).append(", description=");
      String str3 = this.mDescription;
      StringBuilder localStringBuilder4 = localStringBuilder3.append(str3).append(", enabled=");
      boolean bool1 = this.mEnabled;
      StringBuilder localStringBuilder5 = localStringBuilder4.append(bool1).append(", connecting=");
      boolean bool2 = this.mConnecting;
      StringBuilder localStringBuilder6 = localStringBuilder5.append(bool2).append(", playbackType=");
      int i = this.mPlaybackType;
      StringBuilder localStringBuilder7 = localStringBuilder6.append(i).append(", playbackStream=");
      int j = this.mPlaybackStream;
      StringBuilder localStringBuilder8 = localStringBuilder7.append(j).append(", volumeHandling=");
      int k = this.mVolumeHandling;
      StringBuilder localStringBuilder9 = localStringBuilder8.append(k).append(", volume=");
      int m = this.mVolume;
      StringBuilder localStringBuilder10 = localStringBuilder9.append(m).append(", volumeMax=");
      int n = this.mVolumeMax;
      StringBuilder localStringBuilder11 = localStringBuilder10.append(n).append(", presentationDisplayId=");
      int i1 = this.mPresentationDisplayId;
      StringBuilder localStringBuilder12 = localStringBuilder11.append(i1).append(", extras=");
      Bundle localBundle = this.mExtras;
      StringBuilder localStringBuilder13 = localStringBuilder12.append(localBundle).append(", providerPackageName=");
      String str4 = this.mProvider.getPackageName();
      return str4 + " }";
    }

    int updateDescriptor(MediaRouteDescriptor paramMediaRouteDescriptor)
    {
      int i = 0;
      if (this.mDescriptor != paramMediaRouteDescriptor)
      {
        this.mDescriptor = paramMediaRouteDescriptor;
        if (paramMediaRouteDescriptor != null)
        {
          String str1 = this.mName;
          String str2 = paramMediaRouteDescriptor.getName();
          if (!MediaRouter.equal(str1, str2))
          {
            String str3 = paramMediaRouteDescriptor.getName();
            this.mName = str3;
            i = 0x0 | 0x1;
          }
          String str4 = this.mDescription;
          String str5 = paramMediaRouteDescriptor.getDescription();
          if (!MediaRouter.equal(str4, str5))
          {
            String str6 = paramMediaRouteDescriptor.getDescription();
            this.mDescription = str6;
            i |= 1;
          }
          boolean bool1 = this.mEnabled;
          boolean bool2 = paramMediaRouteDescriptor.isEnabled();
          if (bool1 != bool2)
          {
            boolean bool3 = paramMediaRouteDescriptor.isEnabled();
            this.mEnabled = bool3;
            i |= 1;
          }
          boolean bool4 = this.mConnecting;
          boolean bool5 = paramMediaRouteDescriptor.isConnecting();
          if (bool4 != bool5)
          {
            boolean bool6 = paramMediaRouteDescriptor.isConnecting();
            this.mConnecting = bool6;
            i |= 1;
          }
          ArrayList localArrayList1 = this.mControlFilters;
          List localList1 = paramMediaRouteDescriptor.getControlFilters();
          if (!localArrayList1.equals(localList1))
          {
            this.mControlFilters.clear();
            ArrayList localArrayList2 = this.mControlFilters;
            List localList2 = paramMediaRouteDescriptor.getControlFilters();
            boolean bool7 = localArrayList2.addAll(localList2);
            i |= 1;
          }
          int j = this.mPlaybackType;
          int k = paramMediaRouteDescriptor.getPlaybackType();
          if (j != k)
          {
            int m = paramMediaRouteDescriptor.getPlaybackType();
            this.mPlaybackType = m;
            i |= 1;
          }
          int n = this.mPlaybackStream;
          int i1 = paramMediaRouteDescriptor.getPlaybackStream();
          if (n != i1)
          {
            int i2 = paramMediaRouteDescriptor.getPlaybackStream();
            this.mPlaybackStream = i2;
            i |= 1;
          }
          int i3 = this.mVolumeHandling;
          int i4 = paramMediaRouteDescriptor.getVolumeHandling();
          if (i3 != i4)
          {
            int i5 = paramMediaRouteDescriptor.getVolumeHandling();
            this.mVolumeHandling = i5;
            i |= 3;
          }
          int i6 = this.mVolume;
          int i7 = paramMediaRouteDescriptor.getVolume();
          if (i6 != i7)
          {
            int i8 = paramMediaRouteDescriptor.getVolume();
            this.mVolume = i8;
            i |= 3;
          }
          int i9 = this.mVolumeMax;
          int i10 = paramMediaRouteDescriptor.getVolumeMax();
          if (i9 != i10)
          {
            int i11 = paramMediaRouteDescriptor.getVolumeMax();
            this.mVolumeMax = i11;
            i |= 3;
          }
          int i12 = this.mPresentationDisplayId;
          int i13 = paramMediaRouteDescriptor.getPresentationDisplayId();
          if (i12 != i13)
          {
            int i14 = paramMediaRouteDescriptor.getPresentationDisplayId();
            this.mPresentationDisplayId = i14;
            this.mPresentationDisplay = null;
            i |= 5;
          }
          Bundle localBundle1 = this.mExtras;
          Bundle localBundle2 = paramMediaRouteDescriptor.getExtras();
          if (!MediaRouter.equal(localBundle1, localBundle2))
          {
            Bundle localBundle3 = paramMediaRouteDescriptor.getExtras();
            this.mExtras = localBundle3;
            i |= 1;
          }
        }
      }
      return i;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.media.MediaRouter
 * JD-Core Version:    0.6.2
 */