package com.mobeta.android.dslv;

import android.content.Context;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView.LayoutParams;

public class DragSortItemView extends ViewGroup
{
  private int mGravity = 48;

  public DragSortItemView(Context paramContext)
  {
    super(paramContext);
    AbsListView.LayoutParams localLayoutParams = new AbsListView.LayoutParams(-1, -1);
    setLayoutParams(localLayoutParams);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    View localView = getChildAt(0);
    if (localView == null)
      return;
    if (this.mGravity == 48)
    {
      int i = getMeasuredWidth();
      int j = localView.getMeasuredHeight();
      localView.layout(0, 0, i, j);
      return;
    }
    int k = getMeasuredHeight();
    int m = localView.getMeasuredHeight();
    int n = k - m;
    int i1 = getMeasuredWidth();
    int i2 = getMeasuredHeight();
    localView.layout(0, n, i1, i2);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt2);
    int j = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    View localView = getChildAt(0);
    if (localView == null)
    {
      setMeasuredDimension(0, j);
      return;
    }
    if (localView.isLayoutRequested())
    {
      int m = View.MeasureSpec.makeMeasureSpec(0, 0);
      measureChild(localView, paramInt1, m);
    }
    ViewGroup.LayoutParams localLayoutParams;
    if (k == 0)
    {
      localLayoutParams = getLayoutParams();
      if (localLayoutParams.height <= 0)
        break label94;
    }
    label94: for (i = localLayoutParams.height; ; i = localView.getMeasuredHeight())
    {
      setMeasuredDimension(j, i);
      return;
    }
  }

  public void setGravity(int paramInt)
  {
    this.mGravity = paramInt;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.mobeta.android.dslv.DragSortItemView
 * JD-Core Version:    0.6.2
 */