package android.support.v4.view;

import android.view.View;
import android.view.ViewParent;

class ViewCompatJB
{
  public static int getImportantForAccessibility(View paramView)
  {
    return paramView.getImportantForAccessibility();
  }

  public static ViewParent getParentForAccessibility(View paramView)
  {
    return paramView.getParentForAccessibility();
  }

  public static void postInvalidateOnAnimation(View paramView)
  {
    paramView.postInvalidateOnAnimation();
  }

  public static void postInvalidateOnAnimation(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramView.postInvalidate(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public static void postOnAnimation(View paramView, Runnable paramRunnable)
  {
    paramView.postOnAnimation(paramRunnable);
  }

  public static void setImportantForAccessibility(View paramView, int paramInt)
  {
    paramView.setImportantForAccessibility(paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.ViewCompatJB
 * JD-Core Version:    0.6.2
 */