package android.support.v7.app;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.support.v7.mediarouter.R.attr;
import android.support.v7.mediarouter.R.style;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;

final class MediaRouterThemeHelper
{
  public static Context createThemedContext(Context paramContext, boolean paramBoolean)
  {
    boolean bool = isLightTheme(paramContext);
    if ((bool) && (paramBoolean))
    {
      int i = R.style.Theme_AppCompat;
      ContextThemeWrapper localContextThemeWrapper1 = new ContextThemeWrapper(paramContext, i);
      bool = false;
      paramContext = localContextThemeWrapper1;
    }
    ContextThemeWrapper localContextThemeWrapper2 = new android/view/ContextThemeWrapper;
    if (bool);
    for (int j = R.style.Theme_MediaRouter_Light; ; j = R.style.Theme_MediaRouter)
    {
      localContextThemeWrapper2.<init>(paramContext, j);
      return localContextThemeWrapper2;
    }
  }

  public static Drawable getThemeDrawable(Context paramContext, int paramInt)
  {
    int i = getThemeResource(paramContext, paramInt);
    if (i != 0);
    for (Drawable localDrawable = paramContext.getResources().getDrawable(i); ; localDrawable = null)
      return localDrawable;
  }

  public static int getThemeResource(Context paramContext, int paramInt)
  {
    TypedValue localTypedValue = new TypedValue();
    if (paramContext.getTheme().resolveAttribute(paramInt, localTypedValue, true));
    for (int i = localTypedValue.resourceId; ; i = 0)
      return i;
  }

  private static boolean isLightTheme(Context paramContext)
  {
    boolean bool = true;
    TypedValue localTypedValue = new TypedValue();
    Resources.Theme localTheme = paramContext.getTheme();
    int i = R.attr.isLightTheme;
    if ((localTheme.resolveAttribute(i, localTypedValue, bool)) && (localTypedValue.data != 0));
    while (true)
    {
      return bool;
      bool = false;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.MediaRouterThemeHelper
 * JD-Core Version:    0.6.2
 */