package android.support.v7.media;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.media.MediaRouter.RouteInfo;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class MediaRouterJellybeanMr1
{
  public static Object createCallback(Callback paramCallback)
  {
    return new CallbackProxy(paramCallback);
  }

  static class CallbackProxy<T extends MediaRouterJellybeanMr1.Callback> extends MediaRouterJellybean.CallbackProxy<T>
  {
    public CallbackProxy(T paramT)
    {
      super();
    }

    public void onRoutePresentationDisplayChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      ((MediaRouterJellybeanMr1.Callback)this.mCallback).onRoutePresentationDisplayChanged(paramRouteInfo);
    }
  }

  public static final class IsConnectingWorkaround
  {
    private Method mGetStatusCodeMethod;
    private int mStatusConnecting;

    public IsConnectingWorkaround()
    {
      if (Build.VERSION.SDK_INT != 17)
        throw new UnsupportedOperationException();
      try
      {
        int i = MediaRouter.RouteInfo.class.getField("STATUS_CONNECTING").getInt(null);
        this.mStatusConnecting = i;
        Class[] arrayOfClass = new Class[0];
        Method localMethod = MediaRouter.RouteInfo.class.getMethod("getStatusCode", arrayOfClass);
        this.mGetStatusCodeMethod = localMethod;
        return;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
      }
    }

    public boolean isConnecting(Object paramObject)
    {
      MediaRouter.RouteInfo localRouteInfo = (MediaRouter.RouteInfo)paramObject;
      if (this.mGetStatusCodeMethod != null);
      try
      {
        Method localMethod = this.mGetStatusCodeMethod;
        Object[] arrayOfObject = new Object[0];
        int i = ((Integer)localMethod.invoke(localRouteInfo, arrayOfObject)).intValue();
        int j = this.mStatusConnecting;
        if (i != j);
        for (bool = true; ; bool = false)
          return bool;
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        while (true)
          boolean bool = false;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        label65: break label65;
      }
    }
  }

  public static final class ActiveScanWorkaround
    implements Runnable
  {
    private boolean mActivelyScanningWifiDisplays;
    private final DisplayManager mDisplayManager;
    private final Handler mHandler;
    private Method mScanWifiDisplaysMethod;

    public ActiveScanWorkaround(Context paramContext, Handler paramHandler)
    {
      if (Build.VERSION.SDK_INT != 17)
        throw new UnsupportedOperationException();
      DisplayManager localDisplayManager = (DisplayManager)paramContext.getSystemService("display");
      this.mDisplayManager = localDisplayManager;
      this.mHandler = paramHandler;
      try
      {
        Class[] arrayOfClass = new Class[0];
        Method localMethod = DisplayManager.class.getMethod("scanWifiDisplays", arrayOfClass);
        this.mScanWifiDisplaysMethod = localMethod;
        return;
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
      }
    }

    public void run()
    {
      if (!this.mActivelyScanningWifiDisplays)
        return;
      try
      {
        Method localMethod = this.mScanWifiDisplaysMethod;
        DisplayManager localDisplayManager = this.mDisplayManager;
        Object[] arrayOfObject = new Object[0];
        Object localObject = localMethod.invoke(localDisplayManager, arrayOfObject);
        boolean bool = this.mHandler.postDelayed(this, 15000L);
        return;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        while (true)
          int i = Log.w("MediaRouterJellybeanMr1", "Cannot scan for wifi displays.", localIllegalAccessException);
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        while (true)
          int j = Log.w("MediaRouterJellybeanMr1", "Cannot scan for wifi displays.", localInvocationTargetException);
      }
    }

    public void setActiveScanRouteTypes(int paramInt)
    {
      if ((paramInt & 0x2) != 0)
      {
        if (this.mActivelyScanningWifiDisplays)
          return;
        if (this.mScanWifiDisplaysMethod != null)
        {
          this.mActivelyScanningWifiDisplays = true;
          boolean bool = this.mHandler.post(this);
          return;
        }
        int i = Log.w("MediaRouterJellybeanMr1", "Cannot scan for wifi displays because the DisplayManager.scanWifiDisplays() method is not available on this device.");
        return;
      }
      if (!this.mActivelyScanningWifiDisplays)
        return;
      this.mActivelyScanningWifiDisplays = false;
      this.mHandler.removeCallbacks(this);
    }
  }

  public static abstract interface Callback extends MediaRouterJellybean.Callback
  {
    public abstract void onRoutePresentationDisplayChanged(Object paramObject);
  }

  public static final class RouteInfo
  {
    public static Display getPresentationDisplay(Object paramObject)
    {
      return ((MediaRouter.RouteInfo)paramObject).getPresentationDisplay();
    }

    public static boolean isEnabled(Object paramObject)
    {
      return ((MediaRouter.RouteInfo)paramObject).isEnabled();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.media.MediaRouterJellybeanMr1
 * JD-Core Version:    0.6.2
 */