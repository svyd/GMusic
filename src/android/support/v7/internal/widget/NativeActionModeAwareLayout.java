package android.support.v7.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.View;
import android.widget.LinearLayout;

public class NativeActionModeAwareLayout extends LinearLayout
{
  private OnActionModeForChildListener mActionModeForChildListener;

  public NativeActionModeAwareLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void setActionModeForChildListener(OnActionModeForChildListener paramOnActionModeForChildListener)
  {
    this.mActionModeForChildListener = paramOnActionModeForChildListener;
  }

  public ActionMode startActionModeForChild(View paramView, ActionMode.Callback paramCallback)
  {
    if (this.mActionModeForChildListener != null)
      paramCallback = this.mActionModeForChildListener.onActionModeForChild(paramCallback);
    return super.startActionModeForChild(paramView, paramCallback);
  }

  public static abstract interface OnActionModeForChildListener
  {
    public abstract ActionMode.Callback onActionModeForChild(ActionMode.Callback paramCallback);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.NativeActionModeAwareLayout
 * JD-Core Version:    0.6.2
 */