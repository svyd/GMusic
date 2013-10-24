package com.google.android.music.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import com.google.android.music.R.styleable;

public class TouchDelegateView extends View
{
  private View mDelegateView = null;
  private final int mDelegateViewId;

  public TouchDelegateView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    int[] arrayOfInt = R.styleable.TouchDelagate;
    int i = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt).getResourceId(0, -1);
    this.mDelegateViewId = i;
    if (this.mDelegateViewId != -1)
      return;
    throw new IllegalArgumentException("must provide a delegate");
  }

  private View findDelegate()
  {
    ViewParent localViewParent = getParent();
    View localView1;
    View localView2;
    if ((localViewParent != null) && ((localViewParent instanceof View)))
    {
      localView1 = (View)localViewParent;
      int i = this.mDelegateViewId;
      localView2 = localView1.findViewById(i);
      if (localView2 == null);
    }
    while (true)
    {
      return localView2;
      localViewParent = localView1.getParent();
      break;
      StringBuilder localStringBuilder = new StringBuilder().append("Could not find the delegateView id: ");
      int j = this.mDelegateViewId;
      String str = j;
      int k = Log.e("TouchDelegateView", str);
      localView2 = null;
    }
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    View localView = findDelegate();
    this.mDelegateView = localView;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = false;
    if (this.mDelegateView == null)
      int i = Log.e("TouchDelegateView", "Delegate view was null.  Not passing event");
    while (true)
    {
      return bool;
      if (this.mDelegateView.getVisibility() == 0)
        bool = this.mDelegateView.dispatchTouchEvent(paramMotionEvent);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.widgets.TouchDelegateView
 * JD-Core Version:    0.6.2
 */