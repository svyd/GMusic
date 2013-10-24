package android.support.v7.media;

import android.content.Context;
import android.media.MediaRouter;
import android.media.MediaRouter.Callback;
import android.media.MediaRouter.RouteCategory;
import android.media.MediaRouter.RouteGroup;
import android.media.MediaRouter.RouteInfo;
import android.media.MediaRouter.UserRouteInfo;
import android.media.MediaRouter.VolumeCallback;
import android.media.RemoteControlClient;
import android.os.Build.VERSION;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

final class MediaRouterJellybean
{
  public static void addCallback(Object paramObject1, int paramInt, Object paramObject2)
  {
    MediaRouter localMediaRouter = (MediaRouter)paramObject1;
    MediaRouter.Callback localCallback = (MediaRouter.Callback)paramObject2;
    localMediaRouter.addCallback(paramInt, localCallback);
  }

  public static void addUserRoute(Object paramObject1, Object paramObject2)
  {
    MediaRouter localMediaRouter = (MediaRouter)paramObject1;
    MediaRouter.UserRouteInfo localUserRouteInfo = (MediaRouter.UserRouteInfo)paramObject2;
    localMediaRouter.addUserRoute(localUserRouteInfo);
  }

  public static Object createCallback(Callback paramCallback)
  {
    return new CallbackProxy(paramCallback);
  }

  public static Object createRouteCategory(Object paramObject, String paramString, boolean paramBoolean)
  {
    return ((MediaRouter)paramObject).createRouteCategory(paramString, paramBoolean);
  }

  public static Object createUserRoute(Object paramObject1, Object paramObject2)
  {
    MediaRouter localMediaRouter = (MediaRouter)paramObject1;
    MediaRouter.RouteCategory localRouteCategory = (MediaRouter.RouteCategory)paramObject2;
    return localMediaRouter.createUserRoute(localRouteCategory);
  }

  public static Object createVolumeCallback(VolumeCallback paramVolumeCallback)
  {
    return new VolumeCallbackProxy(paramVolumeCallback);
  }

  public static Object getMediaRouter(Context paramContext)
  {
    return paramContext.getSystemService("media_router");
  }

  public static List getRoutes(Object paramObject)
  {
    MediaRouter localMediaRouter = (MediaRouter)paramObject;
    int i = localMediaRouter.getRouteCount();
    ArrayList localArrayList = new ArrayList(i);
    int j = 0;
    while (j < i)
    {
      MediaRouter.RouteInfo localRouteInfo = localMediaRouter.getRouteAt(j);
      boolean bool = localArrayList.add(localRouteInfo);
      j += 1;
    }
    return localArrayList;
  }

  public static Object getSelectedRoute(Object paramObject, int paramInt)
  {
    return ((MediaRouter)paramObject).getSelectedRoute(paramInt);
  }

  public static void removeCallback(Object paramObject1, Object paramObject2)
  {
    MediaRouter localMediaRouter = (MediaRouter)paramObject1;
    MediaRouter.Callback localCallback = (MediaRouter.Callback)paramObject2;
    localMediaRouter.removeCallback(localCallback);
  }

  public static void removeUserRoute(Object paramObject1, Object paramObject2)
  {
    MediaRouter localMediaRouter = (MediaRouter)paramObject1;
    MediaRouter.UserRouteInfo localUserRouteInfo = (MediaRouter.UserRouteInfo)paramObject2;
    localMediaRouter.removeUserRoute(localUserRouteInfo);
  }

  public static void selectRoute(Object paramObject1, int paramInt, Object paramObject2)
  {
    MediaRouter localMediaRouter = (MediaRouter)paramObject1;
    MediaRouter.RouteInfo localRouteInfo = (MediaRouter.RouteInfo)paramObject2;
    localMediaRouter.selectRoute(paramInt, localRouteInfo);
  }

  static class VolumeCallbackProxy<T extends MediaRouterJellybean.VolumeCallback> extends MediaRouter.VolumeCallback
  {
    protected final T mCallback;

    public VolumeCallbackProxy(T paramT)
    {
      this.mCallback = paramT;
    }

    public void onVolumeSetRequest(MediaRouter.RouteInfo paramRouteInfo, int paramInt)
    {
      this.mCallback.onVolumeSetRequest(paramRouteInfo, paramInt);
    }

    public void onVolumeUpdateRequest(MediaRouter.RouteInfo paramRouteInfo, int paramInt)
    {
      this.mCallback.onVolumeUpdateRequest(paramRouteInfo, paramInt);
    }
  }

  static class CallbackProxy<T extends MediaRouterJellybean.Callback> extends MediaRouter.Callback
  {
    protected final T mCallback;

    public CallbackProxy(T paramT)
    {
      this.mCallback = paramT;
    }

    public void onRouteAdded(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      this.mCallback.onRouteAdded(paramRouteInfo);
    }

    public void onRouteChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      this.mCallback.onRouteChanged(paramRouteInfo);
    }

    public void onRouteGrouped(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo, MediaRouter.RouteGroup paramRouteGroup, int paramInt)
    {
      this.mCallback.onRouteGrouped(paramRouteInfo, paramRouteGroup, paramInt);
    }

    public void onRouteRemoved(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      this.mCallback.onRouteRemoved(paramRouteInfo);
    }

    public void onRouteSelected(MediaRouter paramMediaRouter, int paramInt, MediaRouter.RouteInfo paramRouteInfo)
    {
      this.mCallback.onRouteSelected(paramInt, paramRouteInfo);
    }

    public void onRouteUngrouped(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo, MediaRouter.RouteGroup paramRouteGroup)
    {
      this.mCallback.onRouteUngrouped(paramRouteInfo, paramRouteGroup);
    }

    public void onRouteUnselected(MediaRouter paramMediaRouter, int paramInt, MediaRouter.RouteInfo paramRouteInfo)
    {
      this.mCallback.onRouteUnselected(paramInt, paramRouteInfo);
    }

    public void onRouteVolumeChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      this.mCallback.onRouteVolumeChanged(paramRouteInfo);
    }
  }

  public static final class GetDefaultRouteWorkaround
  {
    private Method mGetSystemAudioRouteMethod;

    public GetDefaultRouteWorkaround()
    {
      if ((Build.VERSION.SDK_INT < 16) || (Build.VERSION.SDK_INT > 17))
        throw new UnsupportedOperationException();
      try
      {
        Class[] arrayOfClass = new Class[0];
        Method localMethod = MediaRouter.class.getMethod("getSystemAudioRoute", arrayOfClass);
        this.mGetSystemAudioRouteMethod = localMethod;
        return;
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
      }
    }

    public Object getDefaultRoute(Object paramObject)
    {
      MediaRouter localMediaRouter = (MediaRouter)paramObject;
      if (this.mGetSystemAudioRouteMethod != null);
      try
      {
        Method localMethod = this.mGetSystemAudioRouteMethod;
        Object[] arrayOfObject = new Object[0];
        Object localObject1 = localMethod.invoke(localMediaRouter, arrayOfObject);
        localObject2 = localObject1;
        return localObject2;
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        while (true)
          Object localObject2 = localMediaRouter.getRouteAt(0);
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        label41: break label41;
      }
    }
  }

  public static final class SelectRouteWorkaround
  {
    private Method mSelectRouteIntMethod;

    public SelectRouteWorkaround()
    {
      if ((Build.VERSION.SDK_INT < 16) || (Build.VERSION.SDK_INT > 17))
        throw new UnsupportedOperationException();
      try
      {
        Class[] arrayOfClass = new Class[2];
        Class localClass = Integer.TYPE;
        arrayOfClass[0] = localClass;
        arrayOfClass[1] = MediaRouter.RouteInfo.class;
        Method localMethod = MediaRouter.class.getMethod("selectRouteInt", arrayOfClass);
        this.mSelectRouteIntMethod = localMethod;
        return;
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
      }
    }

    public void selectRoute(Object paramObject1, int paramInt, Object paramObject2)
    {
      MediaRouter localMediaRouter = (MediaRouter)paramObject1;
      MediaRouter.RouteInfo localRouteInfo = (MediaRouter.RouteInfo)paramObject2;
      int i = localRouteInfo.getSupportedTypes();
      if (((0x800000 & i) != 0) || (this.mSelectRouteIntMethod != null));
      while (true)
      {
        try
        {
          Method localMethod = this.mSelectRouteIntMethod;
          Object[] arrayOfObject = new Object[2];
          Integer localInteger = Integer.valueOf(paramInt);
          arrayOfObject[0] = localInteger;
          arrayOfObject[1] = localRouteInfo;
          Object localObject = localMethod.invoke(localMediaRouter, arrayOfObject);
          return;
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          int j = Log.w("MediaRouterJellybean", "Cannot programmatically select non-user route.  Media routing may not work.", localIllegalAccessException);
          localMediaRouter.selectRoute(paramInt, localRouteInfo);
          return;
        }
        catch (InvocationTargetException localInvocationTargetException)
        {
          int k = Log.w("MediaRouterJellybean", "Cannot programmatically select non-user route.  Media routing may not work.", localInvocationTargetException);
          continue;
        }
        int m = Log.w("MediaRouterJellybean", "Cannot programmatically select non-user route because the platform is missing the selectRouteInt() method.  Media routing may not work.");
      }
    }
  }

  public static abstract interface VolumeCallback
  {
    public abstract void onVolumeSetRequest(Object paramObject, int paramInt);

    public abstract void onVolumeUpdateRequest(Object paramObject, int paramInt);
  }

  public static abstract interface Callback
  {
    public abstract void onRouteAdded(Object paramObject);

    public abstract void onRouteChanged(Object paramObject);

    public abstract void onRouteGrouped(Object paramObject1, Object paramObject2, int paramInt);

    public abstract void onRouteRemoved(Object paramObject);

    public abstract void onRouteSelected(int paramInt, Object paramObject);

    public abstract void onRouteUngrouped(Object paramObject1, Object paramObject2);

    public abstract void onRouteUnselected(int paramInt, Object paramObject);

    public abstract void onRouteVolumeChanged(Object paramObject);
  }

  public static final class UserRouteInfo
  {
    public static void setName(Object paramObject, CharSequence paramCharSequence)
    {
      ((MediaRouter.UserRouteInfo)paramObject).setName(paramCharSequence);
    }

    public static void setPlaybackStream(Object paramObject, int paramInt)
    {
      ((MediaRouter.UserRouteInfo)paramObject).setPlaybackStream(paramInt);
    }

    public static void setPlaybackType(Object paramObject, int paramInt)
    {
      ((MediaRouter.UserRouteInfo)paramObject).setPlaybackType(paramInt);
    }

    public static void setRemoteControlClient(Object paramObject1, Object paramObject2)
    {
      MediaRouter.UserRouteInfo localUserRouteInfo = (MediaRouter.UserRouteInfo)paramObject1;
      RemoteControlClient localRemoteControlClient = (RemoteControlClient)paramObject2;
      localUserRouteInfo.setRemoteControlClient(localRemoteControlClient);
    }

    public static void setVolume(Object paramObject, int paramInt)
    {
      ((MediaRouter.UserRouteInfo)paramObject).setVolume(paramInt);
    }

    public static void setVolumeCallback(Object paramObject1, Object paramObject2)
    {
      MediaRouter.UserRouteInfo localUserRouteInfo = (MediaRouter.UserRouteInfo)paramObject1;
      MediaRouter.VolumeCallback localVolumeCallback = (MediaRouter.VolumeCallback)paramObject2;
      localUserRouteInfo.setVolumeCallback(localVolumeCallback);
    }

    public static void setVolumeHandling(Object paramObject, int paramInt)
    {
      ((MediaRouter.UserRouteInfo)paramObject).setVolumeHandling(paramInt);
    }

    public static void setVolumeMax(Object paramObject, int paramInt)
    {
      ((MediaRouter.UserRouteInfo)paramObject).setVolumeMax(paramInt);
    }
  }

  public static final class RouteInfo
  {
    public static CharSequence getName(Object paramObject, Context paramContext)
    {
      return ((MediaRouter.RouteInfo)paramObject).getName(paramContext);
    }

    public static int getPlaybackStream(Object paramObject)
    {
      return ((MediaRouter.RouteInfo)paramObject).getPlaybackStream();
    }

    public static int getPlaybackType(Object paramObject)
    {
      return ((MediaRouter.RouteInfo)paramObject).getPlaybackType();
    }

    public static int getSupportedTypes(Object paramObject)
    {
      return ((MediaRouter.RouteInfo)paramObject).getSupportedTypes();
    }

    public static Object getTag(Object paramObject)
    {
      return ((MediaRouter.RouteInfo)paramObject).getTag();
    }

    public static int getVolume(Object paramObject)
    {
      return ((MediaRouter.RouteInfo)paramObject).getVolume();
    }

    public static int getVolumeHandling(Object paramObject)
    {
      return ((MediaRouter.RouteInfo)paramObject).getVolumeHandling();
    }

    public static int getVolumeMax(Object paramObject)
    {
      return ((MediaRouter.RouteInfo)paramObject).getVolumeMax();
    }

    public static void requestSetVolume(Object paramObject, int paramInt)
    {
      ((MediaRouter.RouteInfo)paramObject).requestSetVolume(paramInt);
    }

    public static void requestUpdateVolume(Object paramObject, int paramInt)
    {
      ((MediaRouter.RouteInfo)paramObject).requestUpdateVolume(paramInt);
    }

    public static void setTag(Object paramObject1, Object paramObject2)
    {
      ((MediaRouter.RouteInfo)paramObject1).setTag(paramObject2);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.media.MediaRouterJellybean
 * JD-Core Version:    0.6.2
 */