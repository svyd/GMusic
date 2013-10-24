package android.support.v7.media;

import android.media.MediaRouter;
import android.media.MediaRouter.Callback;
import android.media.MediaRouter.RouteInfo;
import android.media.MediaRouter.UserRouteInfo;

final class MediaRouterJellybeanMr2
{
  public static void addCallback(Object paramObject1, int paramInt1, Object paramObject2, int paramInt2)
  {
    MediaRouter localMediaRouter = (MediaRouter)paramObject1;
    MediaRouter.Callback localCallback = (MediaRouter.Callback)paramObject2;
    localMediaRouter.addCallback(paramInt1, localCallback, paramInt2);
  }

  public static Object getDefaultRoute(Object paramObject)
  {
    return ((MediaRouter)paramObject).getDefaultRoute();
  }

  public static final class UserRouteInfo
  {
    public static void setDescription(Object paramObject, CharSequence paramCharSequence)
    {
      ((MediaRouter.UserRouteInfo)paramObject).setDescription(paramCharSequence);
    }
  }

  public static final class RouteInfo
  {
    public static CharSequence getDescription(Object paramObject)
    {
      return ((MediaRouter.RouteInfo)paramObject).getDescription();
    }

    public static boolean isConnecting(Object paramObject)
    {
      return ((MediaRouter.RouteInfo)paramObject).isConnecting();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.media.MediaRouterJellybeanMr2
 * JD-Core Version:    0.6.2
 */