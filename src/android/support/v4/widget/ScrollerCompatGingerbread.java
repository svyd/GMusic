package android.support.v4.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

class ScrollerCompatGingerbread
{
  public static void abortAnimation(Object paramObject)
  {
    ((OverScroller)paramObject).abortAnimation();
  }

  public static boolean computeScrollOffset(Object paramObject)
  {
    return ((OverScroller)paramObject).computeScrollOffset();
  }

  public static Object createScroller(Context paramContext, Interpolator paramInterpolator)
  {
    if (paramInterpolator != null);
    for (OverScroller localOverScroller = new OverScroller(paramContext, paramInterpolator); ; localOverScroller = new OverScroller(paramContext))
      return localOverScroller;
  }

  public static int getCurrX(Object paramObject)
  {
    return ((OverScroller)paramObject).getCurrX();
  }

  public static int getCurrY(Object paramObject)
  {
    return ((OverScroller)paramObject).getCurrY();
  }

  public static int getFinalX(Object paramObject)
  {
    return ((OverScroller)paramObject).getFinalX();
  }

  public static int getFinalY(Object paramObject)
  {
    return ((OverScroller)paramObject).getFinalY();
  }

  public static boolean isFinished(Object paramObject)
  {
    return ((OverScroller)paramObject).isFinished();
  }

  public static void startScroll(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    OverScroller localOverScroller = (OverScroller)paramObject;
    int i = paramInt1;
    int j = paramInt2;
    int k = paramInt3;
    int m = paramInt4;
    int n = paramInt5;
    localOverScroller.startScroll(i, j, k, m, n);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.widget.ScrollerCompatGingerbread
 * JD-Core Version:    0.6.2
 */