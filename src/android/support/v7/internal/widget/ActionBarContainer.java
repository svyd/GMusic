package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.styleable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class ActionBarContainer extends FrameLayout
{
  private ActionBarView mActionBarView;
  private Drawable mBackground;
  private boolean mIsSplit;
  private boolean mIsStacked;
  private boolean mIsTransitioning;
  private Drawable mSplitBackground;
  private Drawable mStackedBackground;
  private View mTabContainer;

  public ActionBarContainer(Context paramContext)
  {
    this(paramContext, null);
  }

  public ActionBarContainer(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setBackgroundDrawable(null);
    int[] arrayOfInt = R.styleable.ActionBar;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt);
    Drawable localDrawable1 = localTypedArray.getDrawable(10);
    this.mBackground = localDrawable1;
    Drawable localDrawable2 = localTypedArray.getDrawable(11);
    this.mStackedBackground = localDrawable2;
    int i = getId();
    int j = R.id.split_action_bar;
    if (i != j)
    {
      this.mIsSplit = true;
      Drawable localDrawable3 = localTypedArray.getDrawable(12);
      this.mSplitBackground = localDrawable3;
    }
    localTypedArray.recycle();
    if (this.mIsSplit)
      if (this.mSplitBackground != null);
    while (true)
    {
      setWillNotDraw(bool);
      return;
      bool = false;
      continue;
      if ((this.mBackground != null) || (this.mStackedBackground != null))
        bool = false;
    }
  }

  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    if ((this.mBackground != null) && (this.mBackground.isStateful()))
    {
      Drawable localDrawable1 = this.mBackground;
      int[] arrayOfInt1 = getDrawableState();
      boolean bool1 = localDrawable1.setState(arrayOfInt1);
    }
    if ((this.mStackedBackground != null) && (this.mStackedBackground.isStateful()))
    {
      Drawable localDrawable2 = this.mStackedBackground;
      int[] arrayOfInt2 = getDrawableState();
      boolean bool2 = localDrawable2.setState(arrayOfInt2);
    }
    if (this.mSplitBackground == null)
      return;
    if (!this.mSplitBackground.isStateful())
      return;
    Drawable localDrawable3 = this.mSplitBackground;
    int[] arrayOfInt3 = getDrawableState();
    boolean bool3 = localDrawable3.setState(arrayOfInt3);
  }

  public void onDraw(Canvas paramCanvas)
  {
    if (getWidth() == 0)
      return;
    if (getHeight() == 0)
      return;
    if (this.mIsSplit)
    {
      if (this.mSplitBackground == null)
        return;
      this.mSplitBackground.draw(paramCanvas);
      return;
    }
    if (this.mBackground != null)
      this.mBackground.draw(paramCanvas);
    if (this.mStackedBackground == null)
      return;
    if (!this.mIsStacked)
      return;
    this.mStackedBackground.draw(paramCanvas);
  }

  public void onFinishInflate()
  {
    super.onFinishInflate();
    int i = R.id.action_bar;
    ActionBarView localActionBarView = (ActionBarView)findViewById(i);
    this.mActionBarView = localActionBarView;
  }

  public boolean onHoverEvent(MotionEvent paramMotionEvent)
  {
    return true;
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((this.mIsTransitioning) || (super.onInterceptTouchEvent(paramMotionEvent)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    int i;
    int j;
    int k;
    int n;
    label88: View localView1;
    if ((this.mTabContainer != null) && (this.mTabContainer.getVisibility() != 8))
    {
      i = 1;
      if ((this.mTabContainer == null) || (this.mTabContainer.getVisibility() == 8))
        break label172;
      j = getMeasuredHeight();
      k = this.mTabContainer.getMeasuredHeight();
      if ((this.mActionBarView.getDisplayOptions() & 0x2) != 0)
        break label232;
      int m = getChildCount();
      n = 0;
      if (n >= m)
        break label151;
      localView1 = getChildAt(n);
      View localView2 = this.mTabContainer;
      if (localView1 != localView2)
        break label131;
    }
    while (true)
    {
      n += 1;
      break label88;
      i = 0;
      break;
      label131: if (!this.mActionBarView.isCollapsed())
        localView1.offsetTopAndBottom(k);
    }
    label151: View localView3 = this.mTabContainer;
    int i1 = paramInt3;
    localView3.layout(paramInt1, 0, i1, k);
    label172: int i2;
    while (true)
    {
      i2 = 0;
      if (!this.mIsSplit)
        break;
      if (this.mSplitBackground != null)
      {
        Drawable localDrawable1 = this.mSplitBackground;
        int i3 = getMeasuredWidth();
        int i4 = getMeasuredHeight();
        localDrawable1.setBounds(0, 0, i3, i4);
        i2 = 1;
      }
      if (i2 == 0)
        return;
      invalidate();
      return;
      label232: View localView4 = this.mTabContainer;
      int i5 = j - k;
      int i6 = paramInt3;
      localView4.layout(paramInt1, i5, i6, j);
    }
    if (this.mBackground != null)
    {
      Drawable localDrawable2 = this.mBackground;
      int i7 = this.mActionBarView.getLeft();
      int i8 = this.mActionBarView.getTop();
      int i9 = this.mActionBarView.getRight();
      int i10 = this.mActionBarView.getBottom();
      localDrawable2.setBounds(i7, i8, i9, i10);
      i2 = 1;
    }
    if ((i != 0) && (this.mStackedBackground != null));
    for (boolean bool = true; ; bool = false)
    {
      this.mIsStacked = bool;
      if (!bool)
        break;
      Drawable localDrawable3 = this.mStackedBackground;
      int i11 = this.mTabContainer.getLeft();
      int i12 = this.mTabContainer.getTop();
      int i13 = this.mTabContainer.getRight();
      int i14 = this.mTabContainer.getBottom();
      localDrawable3.setBounds(i11, i12, i13, i14);
      i2 = 1;
      break;
    }
  }

  public void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    if (this.mActionBarView == null)
      return;
    FrameLayout.LayoutParams localLayoutParams = (FrameLayout.LayoutParams)this.mActionBarView.getLayoutParams();
    if (this.mActionBarView.isCollapsed());
    int i2;
    int i3;
    for (int i = 0; ; i = i2 + i3)
    {
      if (this.mTabContainer == null)
        return;
      if (this.mTabContainer.getVisibility() == 8)
        return;
      if (View.MeasureSpec.getMode(paramInt2) != -2147483648)
        return;
      int j = View.MeasureSpec.getSize(paramInt2);
      int k = getMeasuredWidth();
      int m = Math.min(this.mTabContainer.getMeasuredHeight() + i, j);
      setMeasuredDimension(k, m);
      return;
      int n = this.mActionBarView.getMeasuredHeight();
      int i1 = localLayoutParams.topMargin;
      i2 = n + i1;
      i3 = localLayoutParams.bottomMargin;
    }
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = super.onTouchEvent(paramMotionEvent);
    return true;
  }

  public void setPrimaryBackground(Drawable paramDrawable)
  {
    boolean bool = true;
    if (this.mBackground != null)
    {
      this.mBackground.setCallback(null);
      Drawable localDrawable = this.mBackground;
      unscheduleDrawable(localDrawable);
    }
    this.mBackground = paramDrawable;
    if (paramDrawable != null)
      paramDrawable.setCallback(this);
    if (this.mIsSplit)
      if (this.mSplitBackground != null);
    while (true)
    {
      setWillNotDraw(bool);
      invalidate();
      return;
      bool = false;
      continue;
      if ((this.mBackground != null) || (this.mStackedBackground != null))
        bool = false;
    }
  }

  public void setSplitBackground(Drawable paramDrawable)
  {
    boolean bool = true;
    if (this.mSplitBackground != null)
    {
      this.mSplitBackground.setCallback(null);
      Drawable localDrawable = this.mSplitBackground;
      unscheduleDrawable(localDrawable);
    }
    this.mSplitBackground = paramDrawable;
    if (paramDrawable != null)
      paramDrawable.setCallback(this);
    if (this.mIsSplit)
      if (this.mSplitBackground != null);
    while (true)
    {
      setWillNotDraw(bool);
      invalidate();
      return;
      bool = false;
      continue;
      if ((this.mBackground != null) || (this.mStackedBackground != null))
        bool = false;
    }
  }

  public void setStackedBackground(Drawable paramDrawable)
  {
    boolean bool = true;
    if (this.mStackedBackground != null)
    {
      this.mStackedBackground.setCallback(null);
      Drawable localDrawable = this.mStackedBackground;
      unscheduleDrawable(localDrawable);
    }
    this.mStackedBackground = paramDrawable;
    if (paramDrawable != null)
      paramDrawable.setCallback(this);
    if (this.mIsSplit)
      if (this.mSplitBackground != null);
    while (true)
    {
      setWillNotDraw(bool);
      invalidate();
      return;
      bool = false;
      continue;
      if ((this.mBackground != null) || (this.mStackedBackground != null))
        bool = false;
    }
  }

  public void setTabContainer(ScrollingTabContainerView paramScrollingTabContainerView)
  {
    if (this.mTabContainer != null)
    {
      View localView = this.mTabContainer;
      removeView(localView);
    }
    this.mTabContainer = paramScrollingTabContainerView;
    if (paramScrollingTabContainerView == null)
      return;
    addView(paramScrollingTabContainerView);
    ViewGroup.LayoutParams localLayoutParams = paramScrollingTabContainerView.getLayoutParams();
    localLayoutParams.width = -1;
    localLayoutParams.height = -1;
    paramScrollingTabContainerView.setAllowCollapse(false);
  }

  public void setTransitioning(boolean paramBoolean)
  {
    this.mIsTransitioning = paramBoolean;
    if (paramBoolean);
    for (int i = 393216; ; i = 262144)
    {
      setDescendantFocusability(i);
      return;
    }
  }

  public void setVisibility(int paramInt)
  {
    super.setVisibility(paramInt);
    if (paramInt == 0);
    for (boolean bool1 = true; ; bool1 = false)
    {
      if (this.mBackground != null)
        boolean bool2 = this.mBackground.setVisible(bool1, false);
      if (this.mStackedBackground != null)
        boolean bool3 = this.mStackedBackground.setVisible(bool1, false);
      if (this.mSplitBackground == null)
        return;
      boolean bool4 = this.mSplitBackground.setVisible(bool1, false);
      return;
    }
  }

  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    Drawable localDrawable1 = this.mBackground;
    if ((paramDrawable != localDrawable1) || (this.mIsSplit))
    {
      Drawable localDrawable2 = this.mStackedBackground;
      if ((paramDrawable != localDrawable2) || (!this.mIsStacked))
      {
        Drawable localDrawable3 = this.mSplitBackground;
        if (((paramDrawable != localDrawable3) || (!this.mIsSplit)) && (!super.verifyDrawable(paramDrawable)))
          break label67;
      }
    }
    label67: for (boolean bool = true; ; bool = false)
      return bool;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.ActionBarContainer
 * JD-Core Version:    0.6.2
 */