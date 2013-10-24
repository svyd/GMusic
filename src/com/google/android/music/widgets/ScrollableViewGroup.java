package com.google.android.music.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public abstract class ScrollableViewGroup extends ViewGroup
{
  private static final Interpolator INTERPOLATOR = new Interpolator()
  {
    public float getInterpolation(float paramAnonymousFloat)
    {
      float f = paramAnonymousFloat - 1.0F;
      return f * f * f * f * f + 1.0F;
    }
  };
  private float flingVelocity;
  private boolean flingable;
  protected boolean isBeingDragged;
  private float[] lastPosition;
  private final int[] limits;
  private int maximumVelocity;
  private int minimumVelocity;
  private boolean receivedDown;
  private boolean scrollEnabled;
  protected Scroller scroller;
  private int touchSlop;
  private VelocityTracker velocityTracker;
  private boolean vertical;

  public ScrollableViewGroup(Context paramContext)
  {
    super(paramContext);
    float[] arrayOfFloat = { 0, 0 };
    this.lastPosition = arrayOfFloat;
    int[] arrayOfInt = { -2147483647, 2147483647 };
    this.limits = arrayOfInt;
    this.flingVelocity = 0.0F;
    this.vertical = true;
    this.flingable = true;
    this.isBeingDragged = false;
    this.scrollEnabled = true;
    this.receivedDown = false;
    Context localContext = getContext();
    setFocusable(false);
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(localContext);
    int i = localViewConfiguration.getScaledTouchSlop();
    this.touchSlop = i;
    int j = localViewConfiguration.getScaledMinimumFlingVelocity();
    this.minimumVelocity = j;
    int k = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.maximumVelocity = k;
    Interpolator localInterpolator = INTERPOLATOR;
    Scroller localScroller = new Scroller(localContext, localInterpolator);
    this.scroller = localScroller;
  }

  public ScrollableViewGroup(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    float[] arrayOfFloat = { 0, 0 };
    this.lastPosition = arrayOfFloat;
    int[] arrayOfInt = { -2147483647, 2147483647 };
    this.limits = arrayOfInt;
    this.flingVelocity = 0.0F;
    this.vertical = true;
    this.flingable = true;
    this.isBeingDragged = false;
    this.scrollEnabled = true;
    this.receivedDown = false;
    Context localContext = getContext();
    setFocusable(false);
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(localContext);
    int i = localViewConfiguration.getScaledTouchSlop();
    this.touchSlop = i;
    int j = localViewConfiguration.getScaledMinimumFlingVelocity();
    this.minimumVelocity = j;
    int k = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.maximumVelocity = k;
    Interpolator localInterpolator = INTERPOLATOR;
    Scroller localScroller = new Scroller(localContext, localInterpolator);
    this.scroller = localScroller;
  }

  public ScrollableViewGroup(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    float[] arrayOfFloat = { 0, 0 };
    this.lastPosition = arrayOfFloat;
    int[] arrayOfInt = { -2147483647, 2147483647 };
    this.limits = arrayOfInt;
    this.flingVelocity = 0.0F;
    this.vertical = true;
    this.flingable = true;
    this.isBeingDragged = false;
    this.scrollEnabled = true;
    this.receivedDown = false;
    Context localContext = getContext();
    setFocusable(false);
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(localContext);
    int i = localViewConfiguration.getScaledTouchSlop();
    this.touchSlop = i;
    int j = localViewConfiguration.getScaledMinimumFlingVelocity();
    this.minimumVelocity = j;
    int k = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.maximumVelocity = k;
    Interpolator localInterpolator = INTERPOLATOR;
    Scroller localScroller = new Scroller(localContext, localInterpolator);
    this.scroller = localScroller;
  }

  private int clampToScrollLimits(int paramInt)
  {
    int i = this.limits[0];
    if (paramInt < i)
      paramInt = this.limits[0];
    while (true)
    {
      return paramInt;
      int j = this.limits[1];
      if (paramInt > j)
        paramInt = this.limits[1];
    }
  }

  private void fling(float paramFloat)
  {
    this.flingVelocity = paramFloat;
    int i = getScrollX();
    int j = getScrollY();
    if (this.vertical)
    {
      Scroller localScroller1 = this.scroller;
      int k = (int)paramFloat;
      int m = this.limits[0];
      int n = this.limits[1];
      int i1 = 0;
      int i2 = 0;
      localScroller1.fling(i, j, 0, k, i1, i2, m, n);
    }
    while (true)
    {
      invalidate();
      return;
      Scroller localScroller2 = this.scroller;
      int i3 = (int)paramFloat;
      int i4 = this.limits[0];
      int i5 = this.limits[1];
      int i6 = i;
      int i7 = j;
      int i8 = 0;
      int i9 = 0;
      int i10 = 0;
      localScroller2.fling(i6, i7, i3, i8, i4, i5, i9, i10);
    }
  }

  private float getCurrentVelocity()
  {
    VelocityTracker localVelocityTracker = this.velocityTracker;
    float f1 = this.maximumVelocity;
    localVelocityTracker.computeCurrentVelocity(1000, f1);
    if (this.vertical);
    for (float f2 = this.velocityTracker.getYVelocity(); ; f2 = this.velocityTracker.getXVelocity())
      return f2;
  }

  private boolean motionShouldStartDrag(MotionEvent paramMotionEvent)
  {
    boolean bool = true;
    float f1 = paramMotionEvent.getX();
    int i = this.lastPosition[0];
    float f2 = f1 - i;
    float f3 = paramMotionEvent.getY();
    int j = this.lastPosition[bool];
    float f4 = f3 - j;
    float f5 = this.touchSlop;
    int k;
    int m;
    if (f2 <= f5)
    {
      float f6 = -this.touchSlop;
      if (f2 >= f6);
    }
    else
    {
      k = 1;
      float f7 = this.touchSlop;
      if (f4 <= f7)
      {
        float f8 = -this.touchSlop;
        if (f4 >= f8)
          break label135;
      }
      m = 1;
      label110: if (!this.vertical)
        break label146;
      if ((m == 0) || (k != 0))
        break label141;
    }
    while (true)
    {
      return bool;
      k = 0;
      break;
      label135: m = 0;
      break label110;
      label141: bool = false;
      continue;
      label146: if ((k == 0) || (m != 0))
        bool = false;
    }
  }

  private boolean shouldStartDrag(MotionEvent paramMotionEvent)
  {
    boolean bool = false;
    if (!this.scrollEnabled);
    while (true)
    {
      return bool;
      if (this.isBeingDragged)
        this.isBeingDragged = false;
      else
        switch (paramMotionEvent.getAction())
        {
        case 1:
        default:
          break;
        case 0:
          updatePosition(paramMotionEvent);
          if (!this.scroller.isFinished())
          {
            startDrag();
            bool = true;
          }
          else
          {
            this.receivedDown = true;
          }
          break;
        case 2:
          if (motionShouldStartDrag(paramMotionEvent))
          {
            updatePosition(paramMotionEvent);
            startDrag();
            bool = true;
          }
          break;
        }
    }
  }

  private void startDrag()
  {
    if ((!this.isBeingDragged) && (this.scroller.isFinished()))
      dragStarted();
    this.isBeingDragged = true;
    this.flingVelocity = 0.0F;
    this.scroller.abortAnimation();
  }

  private void stopDrag(boolean paramBoolean)
  {
    this.isBeingDragged = false;
    if ((!paramBoolean) && (this.flingable) && (getChildCount() > 0))
    {
      float f1 = getCurrentVelocity();
      float f2 = this.minimumVelocity;
      if (f1 <= f2)
      {
        float f3 = -this.minimumVelocity;
        if (f1 >= f3);
      }
      else
      {
        float f4 = -f1;
        fling(f4);
      }
    }
    while (true)
    {
      if (!this.flingable)
        return;
      if (this.velocityTracker == null)
        return;
      this.velocityTracker.recycle();
      this.velocityTracker = null;
      return;
      onMoveFinished(0.0F);
      continue;
      onMoveFinished(0.0F);
    }
  }

  private float updatePositionAndComputeDelta(MotionEvent paramMotionEvent)
  {
    if (this.vertical);
    for (int i = 1; ; i = 0)
    {
      int j = this.lastPosition[i];
      updatePosition(paramMotionEvent);
      int k = this.lastPosition[i];
      return j - k;
    }
  }

  public void computeScroll()
  {
    if (!this.scroller.computeScrollOffset())
      return;
    int i;
    if (this.vertical)
    {
      i = this.scroller.getCurrY();
      scrollTo(0, i);
      invalidate();
      if (this.flingVelocity != 0.0F)
      {
        float f = this.flingVelocity;
        onMoveFinished(f);
        this.flingVelocity = 0.0F;
      }
      if (!this.vertical)
        break label120;
    }
    label120: for (int j = this.scroller.getFinalY(); ; j = this.scroller.getFinalX())
    {
      if (i != j)
        return;
      this.scroller.abortAnimation();
      if (this.isBeingDragged)
        return;
      dragEnded();
      return;
      i = this.scroller.getCurrX();
      scrollTo(i, 0);
      break;
    }
  }

  void dragEnded()
  {
  }

  void dragStarted()
  {
  }

  public int getScroll()
  {
    if (this.vertical);
    for (int i = getScrollY(); ; i = getScrollX())
      return i;
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    return shouldStartDrag(paramMotionEvent);
  }

  protected void onMoveFinished(float paramFloat)
  {
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = true;
    int i = paramMotionEvent.getAction();
    if (this.flingable)
    {
      if (this.velocityTracker == null)
      {
        VelocityTracker localVelocityTracker = VelocityTracker.obtain();
        this.velocityTracker = localVelocityTracker;
      }
      this.velocityTracker.addMovement(paramMotionEvent);
    }
    if (!this.isBeingDragged)
      if (!shouldStartDrag(paramMotionEvent));
    while (true)
    {
      return bool1;
      if ((i == 1) && (this.receivedDown))
      {
        this.receivedDown = false;
        bool1 = performClick();
        continue;
        switch (i)
        {
        default:
        case 1:
        case 3:
        case 2:
        }
      }
    }
    if (i == 3);
    for (boolean bool2 = true; ; bool2 = false)
    {
      stopDrag(bool2);
      this.receivedDown = false;
      break;
      float f = updatePositionAndComputeDelta(paramMotionEvent);
      int j = getScroll();
      int k = Math.round(f);
      int m = j + k;
      scrollTo(m);
      this.receivedDown = false;
      break;
    }
  }

  protected void scrollTo(int paramInt)
  {
    if (this.vertical)
    {
      int i = clampToScrollLimits(paramInt);
      scrollTo(0, i);
      return;
    }
    int j = clampToScrollLimits(paramInt);
    scrollTo(j, 0);
  }

  public void setFlingable(boolean paramBoolean)
  {
    this.flingable = paramBoolean;
  }

  public void setScrollEnabled(boolean paramBoolean)
  {
    this.scrollEnabled = paramBoolean;
  }

  public void setScrollLimits(int paramInt1, int paramInt2)
  {
    this.limits[0] = paramInt1;
    this.limits[1] = paramInt2;
  }

  public void setVertical(boolean paramBoolean)
  {
    this.vertical = paramBoolean;
  }

  public boolean showContextMenuForChild(View paramView)
  {
    requestDisallowInterceptTouchEvent(true);
    return super.showContextMenuForChild(paramView);
  }

  public void smoothScrollTo(int paramInt)
  {
    smoothScrollTo(paramInt, true);
  }

  public void smoothScrollTo(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean)
      paramInt = clampToScrollLimits(paramInt);
    int i = getScroll();
    int j = paramInt - i;
    if (this.vertical)
    {
      Scroller localScroller1 = this.scroller;
      int k = getScrollY();
      int m = 0;
      localScroller1.startScroll(0, k, m, j, 500);
    }
    while (true)
    {
      invalidate();
      return;
      Scroller localScroller2 = this.scroller;
      int n = getScrollX();
      int i1 = 0;
      int i2 = j;
      int i3 = 0;
      int i4 = 500;
      localScroller2.startScroll(n, i1, i2, i3, i4);
    }
  }

  protected void updatePosition(MotionEvent paramMotionEvent)
  {
    float[] arrayOfFloat1 = this.lastPosition;
    float f1 = paramMotionEvent.getX();
    arrayOfFloat1[0] = f1;
    float[] arrayOfFloat2 = this.lastPosition;
    float f2 = paramMotionEvent.getY();
    arrayOfFloat2[1] = f2;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.widgets.ScrollableViewGroup
 * JD-Core Version:    0.6.2
 */