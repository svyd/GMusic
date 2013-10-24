package com.google.cast;

import android.content.Intent;
import android.support.v7.media.MediaRouteProvider;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.text.TextUtils;

public final class MediaRouteHelper
{
  private static MediaRouteProvider a;

  public static boolean registerMediaRouteProvider(CastContext paramCastContext, String paramString, MimeData paramMimeData, int paramInt, boolean paramBoolean)
  {
    p.a();
    if (a != null);
    for (boolean bool1 = false; ; bool1 = true)
    {
      return bool1;
      if (TextUtils.isEmpty(paramString))
        paramString = null;
      MediaRouter localMediaRouter = MediaRouter.getInstance(paramCastContext.getApplicationContext());
      CastContext localCastContext = paramCastContext;
      String str = paramString;
      MimeData localMimeData = paramMimeData;
      int i = paramInt;
      boolean bool2 = paramBoolean;
      a = new c(localCastContext, str, localMimeData, i, bool2);
      MediaRouteProvider localMediaRouteProvider = a;
      localMediaRouter.addProvider(localMediaRouteProvider);
    }
  }

  public static boolean registerMinimalMediaRouteProvider(CastContext paramCastContext, MediaRouteAdapter paramMediaRouteAdapter)
  {
    p.a();
    if (a != null);
    for (boolean bool = false; ; bool = true)
    {
      return bool;
      MediaRouter localMediaRouter = MediaRouter.getInstance(paramCastContext.getApplicationContext());
      a = new j(paramCastContext, paramMediaRouteAdapter);
      MediaRouteProvider localMediaRouteProvider = a;
      localMediaRouter.addProvider(localMediaRouteProvider);
    }
  }

  public static boolean requestCastDeviceForRoute(MediaRouter.RouteInfo paramRouteInfo)
  {
    if ((a == null) || (!(a instanceof j)));
    for (boolean bool = false; ; bool = true)
    {
      return bool;
      Intent localIntent1 = new Intent("com.google.cast.ACTION_GET_DEVICE");
      String str = paramRouteInfo.getId();
      Intent localIntent2 = localIntent1.putExtra("com.google.cast.EXTRA_ROUTE_ID", str);
      Intent localIntent3 = localIntent1.addCategory("com.google.cast.CATEGORY_CAST");
      paramRouteInfo.sendControlRequest(localIntent1, null);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.MediaRouteHelper
 * JD-Core Version:    0.6.2
 */