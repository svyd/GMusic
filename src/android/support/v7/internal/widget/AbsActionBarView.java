package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.bool;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.internal.view.menu.ActionMenuPresenter;
import android.support.v7.internal.view.menu.ActionMenuView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

abstract class AbsActionBarView extends ViewGroup
{
  protected ActionMenuPresenter mActionMenuPresenter;
  protected int mContentHeight;
  protected ActionMenuView mMenuView;
  protected boolean mSplitActionBar;
  protected ActionBarContainer mSplitView;
  protected boolean mSplitWhenNarrow;

  AbsActionBarView(Context paramContext)
  {
    super(paramContext);
  }

  AbsActionBarView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  AbsActionBarView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public int getAnimatedVisibility()
  {
    return getVisibility();
  }

  public boolean hideOverflowMenu()
  {
    if (this.mActionMenuPresenter != null);
    for (boolean bool = this.mActionMenuPresenter.hideOverflowMenu(); ; bool = false)
      return bool;
  }

  public boolean isOverflowMenuShowing()
  {
    if (this.mActionMenuPresenter != null);
    for (boolean bool = this.mActionMenuPresenter.isOverflowMenuShowing(); ; bool = false)
      return bool;
  }

  public boolean isOverflowReserved()
  {
    if ((this.mActionMenuPresenter != null) && (this.mActionMenuPresenter.isOverflowReserved()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  protected int measureChildView(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = View.MeasureSpec.makeMeasureSpec(paramInt1, -2147483648);
    paramView.measure(i, paramInt2);
    int j = paramView.getMeasuredWidth();
    int k = paramInt1 - j - paramInt3;
    return Math.max(0, k);
  }

  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    if (Build.VERSION.SDK_INT >= 8)
      super.onConfigurationChanged(paramConfiguration);
    Context localContext = getContext();
    int[] arrayOfInt = R.styleable.ActionBar;
    int i = R.attr.actionBarStyle;
    TypedArray localTypedArray = localContext.obtainStyledAttributes(null, arrayOfInt, i, 0);
    int j = localTypedArray.getLayoutDimension(1, 0);
    setContentHeight(j);
    localTypedArray.recycle();
    if (this.mSplitWhenNarrow)
    {
      Resources localResources = getContext().getResources();
      int k = R.bool.abc_split_action_bar_is_narrow;
      boolean bool = localResources.getBoolean(k);
      setSplitActionBar(bool);
    }
    if (this.mActionMenuPresenter == null)
      return;
    this.mActionMenuPresenter.onConfigurationChanged(paramConfiguration);
  }

  protected int positionChild(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramView.getMeasuredWidth();
    int j = paramView.getMeasuredHeight();
    int k = (paramInt3 - j) / 2;
    int m = paramInt2 + k;
    int n = paramInt1 + i;
    int i1 = m + j;
    paramView.layout(paramInt1, m, n, i1);
    return i;
  }

  protected int positionChildInverse(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramView.getMeasuredWidth();
    int j = paramView.getMeasuredHeight();
    int k = (paramInt3 - j) / 2;
    int m = paramInt2 + k;
    int n = paramInt1 - i;
    int i1 = m + j;
    paramView.layout(n, m, paramInt1, i1);
    return i;
  }

  public void postShowOverflowMenu()
  {
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        boolean bool = AbsActionBarView.this.showOverflowMenu();
      }
    };
    boolean bool = post(local1);
  }

  public void setContentHeight(int paramInt)
  {
    this.mContentHeight = paramInt;
    requestLayout();
  }

  public void setSplitActionBar(boolean paramBoolean)
  {
    this.mSplitActionBar = paramBoolean;
  }

  public void setSplitView(ActionBarContainer paramActionBarContainer)
  {
    this.mSplitView = paramActionBarContainer;
  }

  public void setSplitWhenNarrow(boolean paramBoolean)
  {
    this.mSplitWhenNarrow = paramBoolean;
  }

  public void setVisibility(int paramInt)
  {
    int i = getVisibility();
    if (paramInt != i)
      return;
    super.setVisibility(paramInt);
  }

  public boolean showOverflowMenu()
  {
    if (this.mActionMenuPresenter != null);
    for (boolean bool = this.mActionMenuPresenter.showOverflowMenu(); ; bool = false)
      return bool;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.AbsActionBarView
 * JD-Core Version:    0.6.2
 */