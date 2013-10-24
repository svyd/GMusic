package android.support.v4.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class ScrollerCompat
{
  static final ScrollerCompatImpl IMPL = new ScrollerCompatImplBase();
  Object mScroller;

  static
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 14)
    {
      IMPL = new ScrollerCompatImplIcs();
      return;
    }
    if (i >= 9)
    {
      IMPL = new ScrollerCompatImplGingerbread();
      return;
    }
  }

  ScrollerCompat(Context paramContext, Interpolator paramInterpolator)
  {
    Object localObject = IMPL.createScroller(paramContext, paramInterpolator);
    this.mScroller = localObject;
  }

  public static ScrollerCompat create(Context paramContext, Interpolator paramInterpolator)
  {
    return new ScrollerCompat(paramContext, paramInterpolator);
  }

  public void abortAnimation()
  {
    ScrollerCompatImpl localScrollerCompatImpl = IMPL;
    Object localObject = this.mScroller;
    localScrollerCompatImpl.abortAnimation(localObject);
  }

  public boolean computeScrollOffset()
  {
    ScrollerCompatImpl localScrollerCompatImpl = IMPL;
    Object localObject = this.mScroller;
    return localScrollerCompatImpl.computeScrollOffset(localObject);
  }

  public int getCurrX()
  {
    ScrollerCompatImpl localScrollerCompatImpl = IMPL;
    Object localObject = this.mScroller;
    return localScrollerCompatImpl.getCurrX(localObject);
  }

  public int getCurrY()
  {
    ScrollerCompatImpl localScrollerCompatImpl = IMPL;
    Object localObject = this.mScroller;
    return localScrollerCompatImpl.getCurrY(localObject);
  }

  public int getFinalX()
  {
    ScrollerCompatImpl localScrollerCompatImpl = IMPL;
    Object localObject = this.mScroller;
    return localScrollerCompatImpl.getFinalX(localObject);
  }

  public int getFinalY()
  {
    ScrollerCompatImpl localScrollerCompatImpl = IMPL;
    Object localObject = this.mScroller;
    return localScrollerCompatImpl.getFinalY(localObject);
  }

  public boolean isFinished()
  {
    ScrollerCompatImpl localScrollerCompatImpl = IMPL;
    Object localObject = this.mScroller;
    return localScrollerCompatImpl.isFinished(localObject);
  }

  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    ScrollerCompatImpl localScrollerCompatImpl = IMPL;
    Object localObject = this.mScroller;
    int i = paramInt1;
    int j = paramInt2;
    int k = paramInt3;
    int m = paramInt4;
    int n = paramInt5;
    localScrollerCompatImpl.startScroll(localObject, i, j, k, m, n);
  }

  static class ScrollerCompatImplIcs extends ScrollerCompat.ScrollerCompatImplGingerbread
  {
  }

  static class ScrollerCompatImplGingerbread
    implements ScrollerCompat.ScrollerCompatImpl
  {
    public void abortAnimation(Object paramObject)
    {
      ScrollerCompatGingerbread.abortAnimation(paramObject);
    }

    public boolean computeScrollOffset(Object paramObject)
    {
      return ScrollerCompatGingerbread.computeScrollOffset(paramObject);
    }

    public Object createScroller(Context paramContext, Interpolator paramInterpolator)
    {
      return ScrollerCompatGingerbread.createScroller(paramContext, paramInterpolator);
    }

    public int getCurrX(Object paramObject)
    {
      return ScrollerCompatGingerbread.getCurrX(paramObject);
    }

    public int getCurrY(Object paramObject)
    {
      return ScrollerCompatGingerbread.getCurrY(paramObject);
    }

    public int getFinalX(Object paramObject)
    {
      return ScrollerCompatGingerbread.getFinalX(paramObject);
    }

    public int getFinalY(Object paramObject)
    {
      return ScrollerCompatGingerbread.getFinalY(paramObject);
    }

    public boolean isFinished(Object paramObject)
    {
      return ScrollerCompatGingerbread.isFinished(paramObject);
    }

    public void startScroll(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      ScrollerCompatGingerbread.startScroll(paramObject, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }
  }

  static class ScrollerCompatImplBase
    implements ScrollerCompat.ScrollerCompatImpl
  {
    public void abortAnimation(Object paramObject)
    {
      ((Scroller)paramObject).abortAnimation();
    }

    public boolean computeScrollOffset(Object paramObject)
    {
      return ((Scroller)paramObject).computeScrollOffset();
    }

    public Object createScroller(Context paramContext, Interpolator paramInterpolator)
    {
      if (paramInterpolator != null);
      for (Scroller localScroller = new Scroller(paramContext, paramInterpolator); ; localScroller = new Scroller(paramContext))
        return localScroller;
    }

    public int getCurrX(Object paramObject)
    {
      return ((Scroller)paramObject).getCurrX();
    }

    public int getCurrY(Object paramObject)
    {
      return ((Scroller)paramObject).getCurrY();
    }

    public int getFinalX(Object paramObject)
    {
      return ((Scroller)paramObject).getFinalX();
    }

    public int getFinalY(Object paramObject)
    {
      return ((Scroller)paramObject).getFinalY();
    }

    public boolean isFinished(Object paramObject)
    {
      return ((Scroller)paramObject).isFinished();
    }

    public void startScroll(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      Scroller localScroller = (Scroller)paramObject;
      int i = paramInt1;
      int j = paramInt2;
      int k = paramInt3;
      int m = paramInt4;
      int n = paramInt5;
      localScroller.startScroll(i, j, k, m, n);
    }
  }

  static abstract interface ScrollerCompatImpl
  {
    public abstract void abortAnimation(Object paramObject);

    public abstract boolean computeScrollOffset(Object paramObject);

    public abstract Object createScroller(Context paramContext, Interpolator paramInterpolator);

    public abstract int getCurrX(Object paramObject);

    public abstract int getCurrY(Object paramObject);

    public abstract int getFinalX(Object paramObject);

    public abstract int getFinalY(Object paramObject);

    public abstract boolean isFinished(Object paramObject);

    public abstract void startScroll(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.widget.ScrollerCompat
 * JD-Core Version:    0.6.2
 */