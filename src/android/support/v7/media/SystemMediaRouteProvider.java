package android.support.v7.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.os.Handler;
import android.support.v7.mediarouter.R.string;
import android.view.Display;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

abstract class SystemMediaRouteProvider extends MediaRouteProvider
{
  protected SystemMediaRouteProvider(Context paramContext)
  {
    super(paramContext, localProviderMetadata);
  }

  public static SystemMediaRouteProvider obtain(Context paramContext, SyncCallback paramSyncCallback)
  {
    Object localObject;
    if (Build.VERSION.SDK_INT >= 18)
      localObject = new JellybeanMr2Impl(paramContext, paramSyncCallback);
    while (true)
    {
      return localObject;
      if (Build.VERSION.SDK_INT >= 17)
        localObject = new JellybeanMr1Impl(paramContext, paramSyncCallback);
      else if (Build.VERSION.SDK_INT >= 16)
        localObject = new JellybeanImpl(paramContext, paramSyncCallback);
      else
        localObject = new LegacyImpl(paramContext);
    }
  }

  public void onSyncRouteAdded(MediaRouter.RouteInfo paramRouteInfo)
  {
  }

  public void onSyncRouteChanged(MediaRouter.RouteInfo paramRouteInfo)
  {
  }

  public void onSyncRouteRemoved(MediaRouter.RouteInfo paramRouteInfo)
  {
  }

  public void onSyncRouteSelected(MediaRouter.RouteInfo paramRouteInfo)
  {
  }

  private static class JellybeanMr2Impl extends SystemMediaRouteProvider.JellybeanMr1Impl
  {
    public JellybeanMr2Impl(Context paramContext, SystemMediaRouteProvider.SyncCallback paramSyncCallback)
    {
      super(paramSyncCallback);
    }

    protected Object getDefaultRoute()
    {
      return MediaRouterJellybeanMr2.getDefaultRoute(this.mRouterObj);
    }

    protected boolean isConnecting(SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord paramSystemRouteRecord)
    {
      return MediaRouterJellybeanMr2.RouteInfo.isConnecting(paramSystemRouteRecord.mRouteObj);
    }

    protected void onBuildSystemRouteDescriptor(SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord paramSystemRouteRecord, MediaRouteDescriptor.Builder paramBuilder)
    {
      super.onBuildSystemRouteDescriptor(paramSystemRouteRecord, paramBuilder);
      CharSequence localCharSequence = MediaRouterJellybeanMr2.RouteInfo.getDescription(paramSystemRouteRecord.mRouteObj);
      if (localCharSequence == null)
        return;
      String str = localCharSequence.toString();
      MediaRouteDescriptor.Builder localBuilder = paramBuilder.setDescription(str);
    }

    protected void selectRoute(Object paramObject)
    {
      MediaRouterJellybean.selectRoute(this.mRouterObj, 8388611, paramObject);
    }

    protected void updateCallback()
    {
      int i = 1;
      if (this.mCallbackRegistered)
      {
        Object localObject1 = this.mRouterObj;
        Object localObject2 = this.mCallbackObj;
        MediaRouterJellybean.removeCallback(localObject1, localObject2);
      }
      this.mCallbackRegistered = true;
      Object localObject3 = this.mRouterObj;
      int j = this.mRouteTypes;
      Object localObject4 = this.mCallbackObj;
      if (this.mActiveScan);
      while (true)
      {
        int k = i | 0x2;
        MediaRouterJellybeanMr2.addCallback(localObject3, j, localObject4, k);
        return;
        i = 0;
      }
    }

    protected void updateUserRouteProperties(SystemMediaRouteProvider.JellybeanImpl.UserRouteRecord paramUserRouteRecord)
    {
      super.updateUserRouteProperties(paramUserRouteRecord);
      Object localObject = paramUserRouteRecord.mRouteObj;
      String str = paramUserRouteRecord.mRoute.getDescription();
      MediaRouterJellybeanMr2.UserRouteInfo.setDescription(localObject, str);
    }
  }

  private static class JellybeanMr1Impl extends SystemMediaRouteProvider.JellybeanImpl
    implements MediaRouterJellybeanMr1.Callback
  {
    private MediaRouterJellybeanMr1.ActiveScanWorkaround mActiveScanWorkaround;
    private MediaRouterJellybeanMr1.IsConnectingWorkaround mIsConnectingWorkaround;

    public JellybeanMr1Impl(Context paramContext, SystemMediaRouteProvider.SyncCallback paramSyncCallback)
    {
      super(paramSyncCallback);
    }

    protected Object createCallbackObj()
    {
      return MediaRouterJellybeanMr1.createCallback(this);
    }

    protected boolean isConnecting(SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord paramSystemRouteRecord)
    {
      if (this.mIsConnectingWorkaround == null)
      {
        MediaRouterJellybeanMr1.IsConnectingWorkaround localIsConnectingWorkaround1 = new MediaRouterJellybeanMr1.IsConnectingWorkaround();
        this.mIsConnectingWorkaround = localIsConnectingWorkaround1;
      }
      MediaRouterJellybeanMr1.IsConnectingWorkaround localIsConnectingWorkaround2 = this.mIsConnectingWorkaround;
      Object localObject = paramSystemRouteRecord.mRouteObj;
      return localIsConnectingWorkaround2.isConnecting(localObject);
    }

    protected void onBuildSystemRouteDescriptor(SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord paramSystemRouteRecord, MediaRouteDescriptor.Builder paramBuilder)
    {
      super.onBuildSystemRouteDescriptor(paramSystemRouteRecord, paramBuilder);
      if (!MediaRouterJellybeanMr1.RouteInfo.isEnabled(paramSystemRouteRecord.mRouteObj))
        MediaRouteDescriptor.Builder localBuilder1 = paramBuilder.setEnabled(false);
      if (isConnecting(paramSystemRouteRecord))
        MediaRouteDescriptor.Builder localBuilder2 = paramBuilder.setConnecting(true);
      Display localDisplay = MediaRouterJellybeanMr1.RouteInfo.getPresentationDisplay(paramSystemRouteRecord.mRouteObj);
      if (localDisplay == null)
        return;
      int i = localDisplay.getDisplayId();
      MediaRouteDescriptor.Builder localBuilder3 = paramBuilder.setPresentationDisplayId(i);
    }

    public void onRoutePresentationDisplayChanged(Object paramObject)
    {
      int i = findSystemRouteRecord(paramObject);
      if (i < 0)
        return;
      SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord localSystemRouteRecord = (SystemMediaRouteProvider.JellybeanImpl.SystemRouteRecord)this.mSystemRouteRecords.get(i);
      Display localDisplay = MediaRouterJellybeanMr1.RouteInfo.getPresentationDisplay(paramObject);
      if (localDisplay != null);
      for (int j = localDisplay.getDisplayId(); ; j = -1)
      {
        int k = localSystemRouteRecord.mRouteDescriptor.getPresentationDisplayId();
        if (j != k)
          return;
        MediaRouteDescriptor localMediaRouteDescriptor1 = localSystemRouteRecord.mRouteDescriptor;
        MediaRouteDescriptor localMediaRouteDescriptor2 = new MediaRouteDescriptor.Builder(localMediaRouteDescriptor1).setPresentationDisplayId(j).build();
        localSystemRouteRecord.mRouteDescriptor = localMediaRouteDescriptor2;
        publishRoutes();
        return;
      }
    }

    protected void updateCallback()
    {
      super.updateCallback();
      if (this.mActiveScanWorkaround == null)
      {
        Context localContext = getContext();
        Handler localHandler = getHandler();
        MediaRouterJellybeanMr1.ActiveScanWorkaround localActiveScanWorkaround1 = new MediaRouterJellybeanMr1.ActiveScanWorkaround(localContext, localHandler);
        this.mActiveScanWorkaround = localActiveScanWorkaround1;
      }
      MediaRouterJellybeanMr1.ActiveScanWorkaround localActiveScanWorkaround2 = this.mActiveScanWorkaround;
      if (this.mActiveScan);
      for (int i = this.mRouteTypes; ; i = 0)
      {
        localActiveScanWorkaround2.setActiveScanRouteTypes(i);
        return;
      }
    }
  }

  static class JellybeanImpl extends SystemMediaRouteProvider
    implements MediaRouterJellybean.Callback, MediaRouterJellybean.VolumeCallback
  {
    private static final ArrayList<IntentFilter> LIVE_AUDIO_CONTROL_FILTERS;
    private static final ArrayList<IntentFilter> LIVE_VIDEO_CONTROL_FILTERS;
    protected boolean mActiveScan;
    protected final Object mCallbackObj;
    protected boolean mCallbackRegistered;
    private MediaRouterJellybean.GetDefaultRouteWorkaround mGetDefaultRouteWorkaround;
    protected int mRouteTypes;
    protected final Object mRouterObj;
    private MediaRouterJellybean.SelectRouteWorkaround mSelectRouteWorkaround;
    private final SystemMediaRouteProvider.SyncCallback mSyncCallback;
    protected final ArrayList<SystemRouteRecord> mSystemRouteRecords;
    protected final Object mUserRouteCategoryObj;
    protected final ArrayList<UserRouteRecord> mUserRouteRecords;
    protected final Object mVolumeCallbackObj;

    static
    {
      IntentFilter localIntentFilter1 = new IntentFilter();
      localIntentFilter1.addCategory("android.media.intent.category.LIVE_AUDIO");
      LIVE_AUDIO_CONTROL_FILTERS = new ArrayList();
      boolean bool1 = LIVE_AUDIO_CONTROL_FILTERS.add(localIntentFilter1);
      IntentFilter localIntentFilter2 = new IntentFilter();
      localIntentFilter2.addCategory("android.media.intent.category.LIVE_VIDEO");
      LIVE_VIDEO_CONTROL_FILTERS = new ArrayList();
      boolean bool2 = LIVE_VIDEO_CONTROL_FILTERS.add(localIntentFilter2);
    }

    public JellybeanImpl(Context paramContext, SystemMediaRouteProvider.SyncCallback paramSyncCallback)
    {
      super();
      ArrayList localArrayList1 = new ArrayList();
      this.mSystemRouteRecords = localArrayList1;
      ArrayList localArrayList2 = new ArrayList();
      this.mUserRouteRecords = localArrayList2;
      this.mSyncCallback = paramSyncCallback;
      Object localObject1 = MediaRouterJellybean.getMediaRouter(paramContext);
      this.mRouterObj = localObject1;
      Object localObject2 = createCallbackObj();
      this.mCallbackObj = localObject2;
      Object localObject3 = createVolumeCallbackObj();
      this.mVolumeCallbackObj = localObject3;
      Resources localResources = paramContext.getResources();
      Object localObject4 = this.mRouterObj;
      int i = R.string.mr_user_route_category_name;
      String str = localResources.getString(i);
      Object localObject5 = MediaRouterJellybean.createRouteCategory(localObject4, str, false);
      this.mUserRouteCategoryObj = localObject5;
      updateSystemRoutes();
    }

    private boolean addSystemRouteNoPublish(Object paramObject)
    {
      if ((getUserRouteRecord(paramObject) == null) && (findSystemRouteRecord(paramObject) < 0))
      {
        String str = assignRouteId(paramObject);
        SystemRouteRecord localSystemRouteRecord = new SystemRouteRecord(paramObject, str);
        updateSystemRouteDescriptor(localSystemRouteRecord);
        boolean bool1 = this.mSystemRouteRecords.add(localSystemRouteRecord);
      }
      for (boolean bool2 = true; ; bool2 = false)
        return bool2;
    }

    private String assignRouteId(Object paramObject)
    {
      int i;
      if (getDefaultRoute() == paramObject)
      {
        i = 1;
        if (i == 0)
          break label32;
      }
      label32: Locale localLocale1;
      Object[] arrayOfObject1;
      for (Object localObject = "DEFAULT_ROUTE"; ; localObject = String.format(localLocale1, "ROUTE_%08x", arrayOfObject1))
      {
        if (findSystemRouteRecordByDescriptorId((String)localObject) >= 0)
          break label75;
        return localObject;
        i = 0;
        break;
        localLocale1 = Locale.US;
        arrayOfObject1 = new Object[1];
        Integer localInteger1 = Integer.valueOf(getRouteName(paramObject).hashCode());
        arrayOfObject1[0] = localInteger1;
      }
      label75: int j = 2;
      while (true)
      {
        Locale localLocale2 = Locale.US;
        Object[] arrayOfObject2 = new Object[2];
        arrayOfObject2[0] = localObject;
        Integer localInteger2 = Integer.valueOf(j);
        arrayOfObject2[1] = localInteger2;
        String str = String.format(localLocale2, "%s_%d", arrayOfObject2);
        if (findSystemRouteRecordByDescriptorId(str) < 0)
        {
          localObject = str;
          break;
        }
        j += 1;
      }
    }

    private void updateSystemRoutes()
    {
      boolean bool1 = false;
      Iterator localIterator = MediaRouterJellybean.getRoutes(this.mRouterObj).iterator();
      while (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        boolean bool2 = addSystemRouteNoPublish(localObject);
        bool1 |= bool2;
      }
      if (!bool1)
        return;
      publishRoutes();
    }

    protected Object createCallbackObj()
    {
      return MediaRouterJellybean.createCallback(this);
    }

    protected Object createVolumeCallbackObj()
    {
      return MediaRouterJellybean.createVolumeCallback(this);
    }

    protected int findSystemRouteRecord(Object paramObject)
    {
      int i = this.mSystemRouteRecords.size();
      int j = 0;
      if (j < i)
        if (((SystemRouteRecord)this.mSystemRouteRecords.get(j)).mRouteObj != paramObject);
      while (true)
      {
        return j;
        j += 1;
        break;
        j = -1;
      }
    }

    protected int findSystemRouteRecordByDescriptorId(String paramString)
    {
      int i = this.mSystemRouteRecords.size();
      int j = 0;
      if (j < i)
        if (!((SystemRouteRecord)this.mSystemRouteRecords.get(j)).mRouteDescriptorId.equals(paramString));
      while (true)
      {
        return j;
        j += 1;
        break;
        j = -1;
      }
    }

    protected int findUserRouteRecord(MediaRouter.RouteInfo paramRouteInfo)
    {
      int i = this.mUserRouteRecords.size();
      int j = 0;
      if (j < i)
        if (((UserRouteRecord)this.mUserRouteRecords.get(j)).mRoute != paramRouteInfo);
      while (true)
      {
        return j;
        j += 1;
        break;
        j = -1;
      }
    }

    protected Object getDefaultRoute()
    {
      if (this.mGetDefaultRouteWorkaround == null)
      {
        MediaRouterJellybean.GetDefaultRouteWorkaround localGetDefaultRouteWorkaround1 = new MediaRouterJellybean.GetDefaultRouteWorkaround();
        this.mGetDefaultRouteWorkaround = localGetDefaultRouteWorkaround1;
      }
      MediaRouterJellybean.GetDefaultRouteWorkaround localGetDefaultRouteWorkaround2 = this.mGetDefaultRouteWorkaround;
      Object localObject = this.mRouterObj;
      return localGetDefaultRouteWorkaround2.getDefaultRoute(localObject);
    }

    protected String getRouteName(Object paramObject)
    {
      Context localContext = getContext();
      CharSequence localCharSequence = MediaRouterJellybean.RouteInfo.getName(paramObject, localContext);
      if (localCharSequence != null);
      for (String str = localCharSequence.toString(); ; str = "")
        return str;
    }

    protected UserRouteRecord getUserRouteRecord(Object paramObject)
    {
      Object localObject = MediaRouterJellybean.RouteInfo.getTag(paramObject);
      if ((localObject instanceof UserRouteRecord));
      for (UserRouteRecord localUserRouteRecord = (UserRouteRecord)localObject; ; localUserRouteRecord = null)
        return localUserRouteRecord;
    }

    protected void onBuildSystemRouteDescriptor(SystemRouteRecord paramSystemRouteRecord, MediaRouteDescriptor.Builder paramBuilder)
    {
      int i = MediaRouterJellybean.RouteInfo.getSupportedTypes(paramSystemRouteRecord.mRouteObj);
      if ((i & 0x1) != 0)
      {
        ArrayList localArrayList1 = LIVE_AUDIO_CONTROL_FILTERS;
        MediaRouteDescriptor.Builder localBuilder1 = paramBuilder.addControlFilters(localArrayList1);
      }
      if ((i & 0x2) != 0)
      {
        ArrayList localArrayList2 = LIVE_VIDEO_CONTROL_FILTERS;
        MediaRouteDescriptor.Builder localBuilder2 = paramBuilder.addControlFilters(localArrayList2);
      }
      int j = MediaRouterJellybean.RouteInfo.getPlaybackType(paramSystemRouteRecord.mRouteObj);
      MediaRouteDescriptor.Builder localBuilder3 = paramBuilder.setPlaybackType(j);
      int k = MediaRouterJellybean.RouteInfo.getPlaybackStream(paramSystemRouteRecord.mRouteObj);
      MediaRouteDescriptor.Builder localBuilder4 = paramBuilder.setPlaybackStream(k);
      int m = MediaRouterJellybean.RouteInfo.getVolume(paramSystemRouteRecord.mRouteObj);
      MediaRouteDescriptor.Builder localBuilder5 = paramBuilder.setVolume(m);
      int n = MediaRouterJellybean.RouteInfo.getVolumeMax(paramSystemRouteRecord.mRouteObj);
      MediaRouteDescriptor.Builder localBuilder6 = paramBuilder.setVolumeMax(n);
      int i1 = MediaRouterJellybean.RouteInfo.getVolumeHandling(paramSystemRouteRecord.mRouteObj);
      MediaRouteDescriptor.Builder localBuilder7 = paramBuilder.setVolumeHandling(i1);
    }

    public MediaRouteProvider.RouteController onCreateRouteController(String paramString)
    {
      int i = findSystemRouteRecordByDescriptorId(paramString);
      Object localObject;
      if (i >= 0)
        localObject = ((SystemRouteRecord)this.mSystemRouteRecords.get(i)).mRouteObj;
      for (SystemRouteController localSystemRouteController = new SystemRouteController(localObject); ; localSystemRouteController = null)
        return localSystemRouteController;
    }

    public void onDiscoveryRequestChanged(MediaRouteDiscoveryRequest paramMediaRouteDiscoveryRequest)
    {
      int i = 0;
      boolean bool = false;
      if (paramMediaRouteDiscoveryRequest != null)
      {
        List localList = paramMediaRouteDiscoveryRequest.getSelector().getControlCategories();
        int j = localList.size();
        int k = 0;
        if (k < j)
        {
          String str = (String)localList.get(k);
          if (str.equals("android.media.intent.category.LIVE_AUDIO"))
            i |= 1;
          while (true)
          {
            k += 1;
            break;
            if (str.equals("android.media.intent.category.LIVE_VIDEO"))
              i |= 2;
            else
              i |= 8388608;
          }
        }
        bool = paramMediaRouteDiscoveryRequest.isActiveScan();
      }
      if ((this.mRouteTypes != i) && (this.mActiveScan != bool))
        return;
      this.mRouteTypes = i;
      this.mActiveScan = bool;
      updateCallback();
      updateSystemRoutes();
    }

    public void onRouteAdded(Object paramObject)
    {
      if (!addSystemRouteNoPublish(paramObject))
        return;
      publishRoutes();
    }

    public void onRouteChanged(Object paramObject)
    {
      if (getUserRouteRecord(paramObject) != null)
        return;
      int i = findSystemRouteRecord(paramObject);
      if (i < 0)
        return;
      SystemRouteRecord localSystemRouteRecord = (SystemRouteRecord)this.mSystemRouteRecords.get(i);
      updateSystemRouteDescriptor(localSystemRouteRecord);
      publishRoutes();
    }

    public void onRouteGrouped(Object paramObject1, Object paramObject2, int paramInt)
    {
    }

    public void onRouteRemoved(Object paramObject)
    {
      if (getUserRouteRecord(paramObject) != null)
        return;
      int i = findSystemRouteRecord(paramObject);
      if (i < 0)
        return;
      Object localObject = this.mSystemRouteRecords.remove(i);
      publishRoutes();
    }

    public void onRouteSelected(int paramInt, Object paramObject)
    {
      Object localObject = MediaRouterJellybean.getSelectedRoute(this.mRouterObj, 8388611);
      if (paramObject != localObject)
        return;
      UserRouteRecord localUserRouteRecord = getUserRouteRecord(paramObject);
      if (localUserRouteRecord != null)
      {
        localUserRouteRecord.mRoute.select();
        return;
      }
      int i = findSystemRouteRecord(paramObject);
      if (i < 0)
        return;
      SystemRouteRecord localSystemRouteRecord = (SystemRouteRecord)this.mSystemRouteRecords.get(i);
      SystemMediaRouteProvider.SyncCallback localSyncCallback = this.mSyncCallback;
      String str = localSystemRouteRecord.mRouteDescriptorId;
      MediaRouter.RouteInfo localRouteInfo = localSyncCallback.getSystemRouteByDescriptorId(str);
      if (localRouteInfo == null)
        return;
      localRouteInfo.select();
    }

    public void onRouteUngrouped(Object paramObject1, Object paramObject2)
    {
    }

    public void onRouteUnselected(int paramInt, Object paramObject)
    {
    }

    public void onRouteVolumeChanged(Object paramObject)
    {
      if (getUserRouteRecord(paramObject) != null)
        return;
      int i = findSystemRouteRecord(paramObject);
      if (i < 0)
        return;
      SystemRouteRecord localSystemRouteRecord = (SystemRouteRecord)this.mSystemRouteRecords.get(i);
      int j = MediaRouterJellybean.RouteInfo.getVolume(paramObject);
      int k = localSystemRouteRecord.mRouteDescriptor.getVolume();
      if (j != k)
        return;
      MediaRouteDescriptor localMediaRouteDescriptor1 = localSystemRouteRecord.mRouteDescriptor;
      MediaRouteDescriptor localMediaRouteDescriptor2 = new MediaRouteDescriptor.Builder(localMediaRouteDescriptor1).setVolume(j).build();
      localSystemRouteRecord.mRouteDescriptor = localMediaRouteDescriptor2;
      publishRoutes();
    }

    public void onSyncRouteAdded(MediaRouter.RouteInfo paramRouteInfo)
    {
      if (paramRouteInfo.getProviderInstance() != this)
      {
        Object localObject1 = this.mRouterObj;
        Object localObject2 = this.mUserRouteCategoryObj;
        Object localObject3 = MediaRouterJellybean.createUserRoute(localObject1, localObject2);
        UserRouteRecord localUserRouteRecord = new UserRouteRecord(paramRouteInfo, localObject3);
        MediaRouterJellybean.RouteInfo.setTag(localObject3, localUserRouteRecord);
        Object localObject4 = this.mVolumeCallbackObj;
        MediaRouterJellybean.UserRouteInfo.setVolumeCallback(localObject3, localObject4);
        updateUserRouteProperties(localUserRouteRecord);
        boolean bool = this.mUserRouteRecords.add(localUserRouteRecord);
        MediaRouterJellybean.addUserRoute(this.mRouterObj, localObject3);
        return;
      }
      Object localObject5 = MediaRouterJellybean.getSelectedRoute(this.mRouterObj, 8388611);
      int i = findSystemRouteRecord(localObject5);
      if (i < 0)
        return;
      String str1 = ((SystemRouteRecord)this.mSystemRouteRecords.get(i)).mRouteDescriptorId;
      String str2 = paramRouteInfo.getDescriptorId();
      if (!str1.equals(str2))
        return;
      paramRouteInfo.select();
    }

    public void onSyncRouteChanged(MediaRouter.RouteInfo paramRouteInfo)
    {
      if (paramRouteInfo.getProviderInstance() == this)
        return;
      int i = findUserRouteRecord(paramRouteInfo);
      if (i < 0)
        return;
      UserRouteRecord localUserRouteRecord = (UserRouteRecord)this.mUserRouteRecords.get(i);
      updateUserRouteProperties(localUserRouteRecord);
    }

    public void onSyncRouteRemoved(MediaRouter.RouteInfo paramRouteInfo)
    {
      if (paramRouteInfo.getProviderInstance() == this)
        return;
      int i = findUserRouteRecord(paramRouteInfo);
      if (i < 0)
        return;
      UserRouteRecord localUserRouteRecord = (UserRouteRecord)this.mUserRouteRecords.remove(i);
      MediaRouterJellybean.RouteInfo.setTag(localUserRouteRecord.mRouteObj, null);
      MediaRouterJellybean.UserRouteInfo.setVolumeCallback(localUserRouteRecord.mRouteObj, null);
      Object localObject1 = this.mRouterObj;
      Object localObject2 = localUserRouteRecord.mRouteObj;
      MediaRouterJellybean.removeUserRoute(localObject1, localObject2);
    }

    public void onSyncRouteSelected(MediaRouter.RouteInfo paramRouteInfo)
    {
      if (!paramRouteInfo.isSelected())
        return;
      if (paramRouteInfo.getProviderInstance() != this)
      {
        i = findUserRouteRecord(paramRouteInfo);
        if (i < 0)
          return;
        Object localObject1 = ((UserRouteRecord)this.mUserRouteRecords.get(i)).mRouteObj;
        selectRoute(localObject1);
        return;
      }
      String str = paramRouteInfo.getDescriptorId();
      int i = findSystemRouteRecordByDescriptorId(str);
      if (i < 0)
        return;
      Object localObject2 = ((SystemRouteRecord)this.mSystemRouteRecords.get(i)).mRouteObj;
      selectRoute(localObject2);
    }

    public void onVolumeSetRequest(Object paramObject, int paramInt)
    {
      UserRouteRecord localUserRouteRecord = getUserRouteRecord(paramObject);
      if (localUserRouteRecord == null)
        return;
      localUserRouteRecord.mRoute.requestSetVolume(paramInt);
    }

    public void onVolumeUpdateRequest(Object paramObject, int paramInt)
    {
      UserRouteRecord localUserRouteRecord = getUserRouteRecord(paramObject);
      if (localUserRouteRecord == null)
        return;
      localUserRouteRecord.mRoute.requestUpdateVolume(paramInt);
    }

    protected void publishRoutes()
    {
      MediaRouteProviderDescriptor.Builder localBuilder1 = new MediaRouteProviderDescriptor.Builder();
      int i = this.mSystemRouteRecords.size();
      int j = 0;
      while (j < i)
      {
        MediaRouteDescriptor localMediaRouteDescriptor = ((SystemRouteRecord)this.mSystemRouteRecords.get(j)).mRouteDescriptor;
        MediaRouteProviderDescriptor.Builder localBuilder2 = localBuilder1.addRoute(localMediaRouteDescriptor);
        j += 1;
      }
      MediaRouteProviderDescriptor localMediaRouteProviderDescriptor = localBuilder1.build();
      setDescriptor(localMediaRouteProviderDescriptor);
    }

    protected void selectRoute(Object paramObject)
    {
      if (this.mSelectRouteWorkaround == null)
      {
        MediaRouterJellybean.SelectRouteWorkaround localSelectRouteWorkaround1 = new MediaRouterJellybean.SelectRouteWorkaround();
        this.mSelectRouteWorkaround = localSelectRouteWorkaround1;
      }
      MediaRouterJellybean.SelectRouteWorkaround localSelectRouteWorkaround2 = this.mSelectRouteWorkaround;
      Object localObject = this.mRouterObj;
      localSelectRouteWorkaround2.selectRoute(localObject, 8388611, paramObject);
    }

    protected void updateCallback()
    {
      if (this.mCallbackRegistered)
      {
        this.mCallbackRegistered = false;
        Object localObject1 = this.mRouterObj;
        Object localObject2 = this.mCallbackObj;
        MediaRouterJellybean.removeCallback(localObject1, localObject2);
      }
      if (this.mRouteTypes == 0)
        return;
      this.mCallbackRegistered = true;
      Object localObject3 = this.mRouterObj;
      int i = this.mRouteTypes;
      Object localObject4 = this.mCallbackObj;
      MediaRouterJellybean.addCallback(localObject3, i, localObject4);
    }

    protected void updateSystemRouteDescriptor(SystemRouteRecord paramSystemRouteRecord)
    {
      String str1 = paramSystemRouteRecord.mRouteDescriptorId;
      Object localObject = paramSystemRouteRecord.mRouteObj;
      String str2 = getRouteName(localObject);
      MediaRouteDescriptor.Builder localBuilder = new MediaRouteDescriptor.Builder(str1, str2);
      onBuildSystemRouteDescriptor(paramSystemRouteRecord, localBuilder);
      MediaRouteDescriptor localMediaRouteDescriptor = localBuilder.build();
      paramSystemRouteRecord.mRouteDescriptor = localMediaRouteDescriptor;
    }

    protected void updateUserRouteProperties(UserRouteRecord paramUserRouteRecord)
    {
      Object localObject1 = paramUserRouteRecord.mRouteObj;
      String str = paramUserRouteRecord.mRoute.getName();
      MediaRouterJellybean.UserRouteInfo.setName(localObject1, str);
      Object localObject2 = paramUserRouteRecord.mRouteObj;
      int i = paramUserRouteRecord.mRoute.getPlaybackType();
      MediaRouterJellybean.UserRouteInfo.setPlaybackType(localObject2, i);
      Object localObject3 = paramUserRouteRecord.mRouteObj;
      int j = paramUserRouteRecord.mRoute.getPlaybackStream();
      MediaRouterJellybean.UserRouteInfo.setPlaybackStream(localObject3, j);
      Object localObject4 = paramUserRouteRecord.mRouteObj;
      int k = paramUserRouteRecord.mRoute.getVolume();
      MediaRouterJellybean.UserRouteInfo.setVolume(localObject4, k);
      Object localObject5 = paramUserRouteRecord.mRouteObj;
      int m = paramUserRouteRecord.mRoute.getVolumeMax();
      MediaRouterJellybean.UserRouteInfo.setVolumeMax(localObject5, m);
      Object localObject6 = paramUserRouteRecord.mRouteObj;
      int n = paramUserRouteRecord.mRoute.getVolumeHandling();
      MediaRouterJellybean.UserRouteInfo.setVolumeHandling(localObject6, n);
    }

    protected final class SystemRouteController extends MediaRouteProvider.RouteController
    {
      private final Object mRouteObj;

      public SystemRouteController(Object arg2)
      {
        Object localObject;
        this.mRouteObj = localObject;
      }

      public void onSetVolume(int paramInt)
      {
        MediaRouterJellybean.RouteInfo.requestSetVolume(this.mRouteObj, paramInt);
      }

      public void onUpdateVolume(int paramInt)
      {
        MediaRouterJellybean.RouteInfo.requestUpdateVolume(this.mRouteObj, paramInt);
      }
    }

    protected static final class UserRouteRecord
    {
      public final MediaRouter.RouteInfo mRoute;
      public final Object mRouteObj;

      public UserRouteRecord(MediaRouter.RouteInfo paramRouteInfo, Object paramObject)
      {
        this.mRoute = paramRouteInfo;
        this.mRouteObj = paramObject;
      }
    }

    protected static final class SystemRouteRecord
    {
      public MediaRouteDescriptor mRouteDescriptor;
      public final String mRouteDescriptorId;
      public final Object mRouteObj;

      public SystemRouteRecord(Object paramObject, String paramString)
      {
        this.mRouteObj = paramObject;
        this.mRouteDescriptorId = paramString;
      }
    }
  }

  static class LegacyImpl extends SystemMediaRouteProvider
  {
    private static final ArrayList<IntentFilter> CONTROL_FILTERS;
    private final AudioManager mAudioManager;
    private int mLastReportedVolume = -1;
    private final VolumeChangeReceiver mVolumeChangeReceiver;

    static
    {
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addCategory("android.media.intent.category.LIVE_AUDIO");
      localIntentFilter.addCategory("android.media.intent.category.LIVE_VIDEO");
      CONTROL_FILTERS = new ArrayList();
      boolean bool = CONTROL_FILTERS.add(localIntentFilter);
    }

    public LegacyImpl(Context paramContext)
    {
      super();
      AudioManager localAudioManager = (AudioManager)paramContext.getSystemService("audio");
      this.mAudioManager = localAudioManager;
      VolumeChangeReceiver localVolumeChangeReceiver1 = new VolumeChangeReceiver();
      this.mVolumeChangeReceiver = localVolumeChangeReceiver1;
      VolumeChangeReceiver localVolumeChangeReceiver2 = this.mVolumeChangeReceiver;
      IntentFilter localIntentFilter = new IntentFilter("android.media.VOLUME_CHANGED_ACTION");
      Intent localIntent = paramContext.registerReceiver(localVolumeChangeReceiver2, localIntentFilter);
      publishRoutes();
    }

    private void publishRoutes()
    {
      Resources localResources = getContext().getResources();
      int i = this.mAudioManager.getStreamMaxVolume(3);
      int j = this.mAudioManager.getStreamVolume(3);
      this.mLastReportedVolume = j;
      int k = R.string.mr_system_route_name;
      String str = localResources.getString(k);
      MediaRouteDescriptor.Builder localBuilder1 = new MediaRouteDescriptor.Builder("DEFAULT_ROUTE", str);
      ArrayList localArrayList = CONTROL_FILTERS;
      MediaRouteDescriptor.Builder localBuilder2 = localBuilder1.addControlFilters(localArrayList).setPlaybackStream(3).setPlaybackType(0).setVolumeHandling(1).setVolumeMax(i);
      int m = this.mLastReportedVolume;
      MediaRouteDescriptor localMediaRouteDescriptor = localBuilder2.setVolume(m).build();
      MediaRouteProviderDescriptor localMediaRouteProviderDescriptor = new MediaRouteProviderDescriptor.Builder().addRoute(localMediaRouteDescriptor).build();
      setDescriptor(localMediaRouteProviderDescriptor);
    }

    public MediaRouteProvider.RouteController onCreateRouteController(String paramString)
    {
      if (paramString.equals("DEFAULT_ROUTE"));
      for (DefaultRouteController localDefaultRouteController = new DefaultRouteController(); ; localDefaultRouteController = null)
        return localDefaultRouteController;
    }

    final class VolumeChangeReceiver extends BroadcastReceiver
    {
      VolumeChangeReceiver()
      {
      }

      public void onReceive(Context paramContext, Intent paramIntent)
      {
        if (!paramIntent.getAction().equals("android.media.VOLUME_CHANGED_ACTION"))
          return;
        if (paramIntent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1) != 3)
          return;
        int i = paramIntent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", -1);
        if (i < 0)
          return;
        int j = SystemMediaRouteProvider.LegacyImpl.this.mLastReportedVolume;
        if (i != j)
          return;
        SystemMediaRouteProvider.LegacyImpl.this.publishRoutes();
      }
    }

    final class DefaultRouteController extends MediaRouteProvider.RouteController
    {
      DefaultRouteController()
      {
      }

      public void onSetVolume(int paramInt)
      {
        SystemMediaRouteProvider.LegacyImpl.this.mAudioManager.setStreamVolume(3, paramInt, 0);
        SystemMediaRouteProvider.LegacyImpl.this.publishRoutes();
      }

      public void onUpdateVolume(int paramInt)
      {
        int i = SystemMediaRouteProvider.LegacyImpl.this.mAudioManager.getStreamVolume(3);
        int j = SystemMediaRouteProvider.LegacyImpl.this.mAudioManager.getStreamMaxVolume(3);
        int k = i + paramInt;
        int m = Math.max(0, k);
        if (Math.min(j, m) != i)
          SystemMediaRouteProvider.LegacyImpl.this.mAudioManager.setStreamVolume(3, i, 0);
        SystemMediaRouteProvider.LegacyImpl.this.publishRoutes();
      }
    }
  }

  public static abstract interface SyncCallback
  {
    public abstract MediaRouter.RouteInfo getSystemRouteByDescriptorId(String paramString);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.media.SystemMediaRouteProvider
 * JD-Core Version:    0.6.2
 */