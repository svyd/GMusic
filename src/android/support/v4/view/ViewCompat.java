package android.support.v4.view;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewParent;

public class ViewCompat
{
  static final ViewCompatImpl IMPL = new BaseViewCompatImpl();

  static
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 17)
    {
      IMPL = new JbMr1ViewCompatImpl();
      return;
    }
    if (i >= 16)
    {
      IMPL = new JBViewCompatImpl();
      return;
    }
    if (i >= 14)
    {
      IMPL = new ICSViewCompatImpl();
      return;
    }
    if (i >= 11)
    {
      IMPL = new HCViewCompatImpl();
      return;
    }
    if (i >= 9)
    {
      IMPL = new GBViewCompatImpl();
      return;
    }
  }

  public static boolean canScrollHorizontally(View paramView, int paramInt)
  {
    return IMPL.canScrollHorizontally(paramView, paramInt);
  }

  public static boolean canScrollVertically(View paramView, int paramInt)
  {
    return IMPL.canScrollVertically(paramView, paramInt);
  }

  public static int getImportantForAccessibility(View paramView)
  {
    return IMPL.getImportantForAccessibility(paramView);
  }

  public static int getLayerType(View paramView)
  {
    return IMPL.getLayerType(paramView);
  }

  public static int getLayoutDirection(View paramView)
  {
    return IMPL.getLayoutDirection(paramView);
  }

  public static int getOverScrollMode(View paramView)
  {
    return IMPL.getOverScrollMode(paramView);
  }

  public static ViewParent getParentForAccessibility(View paramView)
  {
    return IMPL.getParentForAccessibility(paramView);
  }

  public static boolean isOpaque(View paramView)
  {
    return IMPL.isOpaque(paramView);
  }

  public static void postInvalidateOnAnimation(View paramView)
  {
    IMPL.postInvalidateOnAnimation(paramView);
  }

  public static void postInvalidateOnAnimation(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ViewCompatImpl localViewCompatImpl = IMPL;
    View localView = paramView;
    int i = paramInt1;
    int j = paramInt2;
    int k = paramInt3;
    int m = paramInt4;
    localViewCompatImpl.postInvalidateOnAnimation(localView, i, j, k, m);
  }

  public static void postOnAnimation(View paramView, Runnable paramRunnable)
  {
    IMPL.postOnAnimation(paramView, paramRunnable);
  }

  public static void setAccessibilityDelegate(View paramView, AccessibilityDelegateCompat paramAccessibilityDelegateCompat)
  {
    IMPL.setAccessibilityDelegate(paramView, paramAccessibilityDelegateCompat);
  }

  public static void setImportantForAccessibility(View paramView, int paramInt)
  {
    IMPL.setImportantForAccessibility(paramView, paramInt);
  }

  public static void setLayerPaint(View paramView, Paint paramPaint)
  {
    IMPL.setLayerPaint(paramView, paramPaint);
  }

  public static void setLayerType(View paramView, int paramInt, Paint paramPaint)
  {
    IMPL.setLayerType(paramView, paramInt, paramPaint);
  }

  static class JbMr1ViewCompatImpl extends ViewCompat.JBViewCompatImpl
  {
    public int getLayoutDirection(View paramView)
    {
      return ViewCompatJellybeanMr1.getLayoutDirection(paramView);
    }

    public void setLayerPaint(View paramView, Paint paramPaint)
    {
      ViewCompatJellybeanMr1.setLayerPaint(paramView, paramPaint);
    }
  }

  static class JBViewCompatImpl extends ViewCompat.ICSViewCompatImpl
  {
    public int getImportantForAccessibility(View paramView)
    {
      return ViewCompatJB.getImportantForAccessibility(paramView);
    }

    public ViewParent getParentForAccessibility(View paramView)
    {
      return ViewCompatJB.getParentForAccessibility(paramView);
    }

    public void postInvalidateOnAnimation(View paramView)
    {
      ViewCompatJB.postInvalidateOnAnimation(paramView);
    }

    public void postInvalidateOnAnimation(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      ViewCompatJB.postInvalidateOnAnimation(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
    }

    public void postOnAnimation(View paramView, Runnable paramRunnable)
    {
      ViewCompatJB.postOnAnimation(paramView, paramRunnable);
    }

    public void setImportantForAccessibility(View paramView, int paramInt)
    {
      ViewCompatJB.setImportantForAccessibility(paramView, paramInt);
    }
  }

  static class ICSViewCompatImpl extends ViewCompat.HCViewCompatImpl
  {
    public boolean canScrollHorizontally(View paramView, int paramInt)
    {
      return ViewCompatICS.canScrollHorizontally(paramView, paramInt);
    }

    public boolean canScrollVertically(View paramView, int paramInt)
    {
      return ViewCompatICS.canScrollVertically(paramView, paramInt);
    }

    public void setAccessibilityDelegate(View paramView, AccessibilityDelegateCompat paramAccessibilityDelegateCompat)
    {
      Object localObject = paramAccessibilityDelegateCompat.getBridge();
      ViewCompatICS.setAccessibilityDelegate(paramView, localObject);
    }
  }

  static class HCViewCompatImpl extends ViewCompat.GBViewCompatImpl
  {
    long getFrameTime()
    {
      return ViewCompatHC.getFrameTime();
    }

    public int getLayerType(View paramView)
    {
      return ViewCompatHC.getLayerType(paramView);
    }

    public void setLayerPaint(View paramView, Paint paramPaint)
    {
      int i = getLayerType(paramView);
      setLayerType(paramView, i, paramPaint);
      paramView.invalidate();
    }

    public void setLayerType(View paramView, int paramInt, Paint paramPaint)
    {
      ViewCompatHC.setLayerType(paramView, paramInt, paramPaint);
    }
  }

  static class GBViewCompatImpl extends ViewCompat.EclairMr1ViewCompatImpl
  {
    public int getOverScrollMode(View paramView)
    {
      return ViewCompatGingerbread.getOverScrollMode(paramView);
    }
  }

  static class EclairMr1ViewCompatImpl extends ViewCompat.BaseViewCompatImpl
  {
    public boolean isOpaque(View paramView)
    {
      return ViewCompatEclairMr1.isOpaque(paramView);
    }
  }

  static class BaseViewCompatImpl
    implements ViewCompat.ViewCompatImpl
  {
    public boolean canScrollHorizontally(View paramView, int paramInt)
    {
      return false;
    }

    public boolean canScrollVertically(View paramView, int paramInt)
    {
      return false;
    }

    long getFrameTime()
    {
      return 10L;
    }

    public int getImportantForAccessibility(View paramView)
    {
      return 0;
    }

    public int getLayerType(View paramView)
    {
      return 0;
    }

    public int getLayoutDirection(View paramView)
    {
      return 0;
    }

    public int getOverScrollMode(View paramView)
    {
      return 2;
    }

    public ViewParent getParentForAccessibility(View paramView)
    {
      return paramView.getParent();
    }

    public boolean isOpaque(View paramView)
    {
      boolean bool = false;
      Drawable localDrawable = paramView.getBackground();
      if ((localDrawable != null) && (localDrawable.getOpacity() == -1))
        bool = true;
      return bool;
    }

    public void postInvalidateOnAnimation(View paramView)
    {
      long l = getFrameTime();
      paramView.postInvalidateDelayed(l);
    }

    public void postInvalidateOnAnimation(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      long l = getFrameTime();
      View localView = paramView;
      int i = paramInt1;
      int j = paramInt2;
      int k = paramInt3;
      int m = paramInt4;
      localView.postInvalidateDelayed(l, i, j, k, m);
    }

    public void postOnAnimation(View paramView, Runnable paramRunnable)
    {
      long l = getFrameTime();
      boolean bool = paramView.postDelayed(paramRunnable, l);
    }

    public void setAccessibilityDelegate(View paramView, AccessibilityDelegateCompat paramAccessibilityDelegateCompat)
    {
    }

    public void setImportantForAccessibility(View paramView, int paramInt)
    {
    }

    public void setLayerPaint(View paramView, Paint paramPaint)
    {
    }

    public void setLayerType(View paramView, int paramInt, Paint paramPaint)
    {
    }
  }

  static abstract interface ViewCompatImpl
  {
    public abstract boolean canScrollHorizontally(View paramView, int paramInt);

    public abstract boolean canScrollVertically(View paramView, int paramInt);

    public abstract int getImportantForAccessibility(View paramView);

    public abstract int getLayerType(View paramView);

    public abstract int getLayoutDirection(View paramView);

    public abstract int getOverScrollMode(View paramView);

    public abstract ViewParent getParentForAccessibility(View paramView);

    public abstract boolean isOpaque(View paramView);

    public abstract void postInvalidateOnAnimation(View paramView);

    public abstract void postInvalidateOnAnimation(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4);

    public abstract void postOnAnimation(View paramView, Runnable paramRunnable);

    public abstract void setAccessibilityDelegate(View paramView, AccessibilityDelegateCompat paramAccessibilityDelegateCompat);

    public abstract void setImportantForAccessibility(View paramView, int paramInt);

    public abstract void setLayerPaint(View paramView, Paint paramPaint);

    public abstract void setLayerType(View paramView, int paramInt, Paint paramPaint);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.ViewCompat
 * JD-Core Version:    0.6.2
 */