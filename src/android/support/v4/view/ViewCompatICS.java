package android.support.v4.view;

import android.view.View;
import android.view.View.AccessibilityDelegate;

class ViewCompatICS
{
  public static boolean canScrollHorizontally(View paramView, int paramInt)
  {
    return paramView.canScrollHorizontally(paramInt);
  }

  public static boolean canScrollVertically(View paramView, int paramInt)
  {
    return paramView.canScrollVertically(paramInt);
  }

  public static void setAccessibilityDelegate(View paramView, Object paramObject)
  {
    View.AccessibilityDelegate localAccessibilityDelegate = (View.AccessibilityDelegate)paramObject;
    paramView.setAccessibilityDelegate(localAccessibilityDelegate);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.ViewCompatICS
 * JD-Core Version:    0.6.2
 */