package android.support.v4.app;

import android.content.Context;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

class NoSaveStateFrameLayout extends FrameLayout
{
  public NoSaveStateFrameLayout(Context paramContext)
  {
    super(paramContext);
  }

  static ViewGroup wrap(View paramView)
  {
    Context localContext = paramView.getContext();
    NoSaveStateFrameLayout localNoSaveStateFrameLayout = new NoSaveStateFrameLayout(localContext);
    ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    if (localLayoutParams != null)
      localNoSaveStateFrameLayout.setLayoutParams(localLayoutParams);
    FrameLayout.LayoutParams localLayoutParams1 = new FrameLayout.LayoutParams(-1, -1);
    paramView.setLayoutParams(localLayoutParams1);
    localNoSaveStateFrameLayout.addView(paramView);
    return localNoSaveStateFrameLayout;
  }

  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    dispatchThawSelfOnly(paramSparseArray);
  }

  protected void dispatchSaveInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    dispatchFreezeSelfOnly(paramSparseArray);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.NoSaveStateFrameLayout
 * JD-Core Version:    0.6.2
 */