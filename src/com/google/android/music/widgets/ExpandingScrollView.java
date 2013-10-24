package com.google.android.music.widgets;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import com.google.android.music.utils.ViewUtils;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExpandingScrollView extends ScrollableViewGroup
{
  public static final ExpandingStateTransition DEFAULT_EXPANDING_STATE_TRANSITION = new ExpandingStateTransition();
  public static final ExpandingStateTransition NO_TWO_THIRDS_EXPANDING_STATE_TRANSITION = new NoTwoThirdsExpandingStateTransition();
  private static final Interpolator bounceInterpolator = new OvershootInterpolator();
  private static boolean initialized;
  private boolean alwaysExpanded = false;
  private Runnable animateInRunnable;
  private ConsistentTouchEventDispatcher childDispatcher;
  private boolean childHasScrolled;
  private ExpandingState coercedState;
  private int collapsedHeaderViewId;
  private View contentView;
  private ExpandingState desiredState;
  private ExpandingState expandingState;
  private ExpandingStateTransition expandingStateTransition;
  private final float[] exposurePercentages;
  private float lastChildDownY;
  private float lastDownX;
  private float lastDownY;
  private float lastDownYOffset;
  private float lastY;
  private List<ExpandingScrollViewListener> listeners;
  private int maxExpandPixels;
  private int minExposure;
  private int pagingTouchSlop;
  private ConsistentTouchEventDispatcher parentDispatcher;
  private ExpandingStateTransition portraitExpandingStateTransition;
  private ScrollState scrollState;
  private Drawable shadow;
  private int shadowHeight;
  private int touchSlop;

  public ExpandingScrollView(Context paramContext)
  {
    super(paramContext);
    ScrollState localScrollState = ScrollState.NO_SCROLL;
    this.scrollState = localScrollState;
    ExpandingStateTransition localExpandingStateTransition1 = DEFAULT_EXPANDING_STATE_TRANSITION;
    this.expandingStateTransition = localExpandingStateTransition1;
    ExpandingStateTransition localExpandingStateTransition2 = DEFAULT_EXPANDING_STATE_TRANSITION;
    this.portraitExpandingStateTransition = localExpandingStateTransition2;
    ExpandingState localExpandingState = ExpandingState.HIDDEN;
    this.expandingState = localExpandingState;
    float[] arrayOfFloat = new float[ExpandingState.values().length];
    this.exposurePercentages = arrayOfFloat;
    ArrayList localArrayList = new ArrayList();
    this.listeners = localArrayList;
    Context localContext = getContext();
    Resources localResources = localContext.getResources();
    if (!initialized)
    {
      Configuration localConfiguration = localResources.getConfiguration();
      updateStateTransition(localConfiguration);
      initialized = true;
    }
    Drawable localDrawable = localResources.getDrawable(2130837609);
    this.shadow = localDrawable;
    int i = localResources.getDimensionPixelSize(2131558456);
    this.shadowHeight = i;
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(localContext);
    int j = localViewConfiguration.getScaledTouchSlop();
    this.touchSlop = j;
    int k = localViewConfiguration.getScaledPagingTouchSlop();
    this.pagingTouchSlop = k;
    ExpandingScrollView.ConsistentTouchEventDispatcher.TouchEventDispatcher local1 = new ExpandingScrollView.ConsistentTouchEventDispatcher.TouchEventDispatcher()
    {
      public boolean dispatchEvent(MotionEvent paramAnonymousMotionEvent)
      {
        return ExpandingScrollView.this.onTouchEvent(paramAnonymousMotionEvent);
      }
    };
    ConsistentTouchEventDispatcher localConsistentTouchEventDispatcher = new ConsistentTouchEventDispatcher(local1);
    this.parentDispatcher = localConsistentTouchEventDispatcher;
    resetExposurePercentages();
  }

  public ExpandingScrollView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    ScrollState localScrollState = ScrollState.NO_SCROLL;
    this.scrollState = localScrollState;
    ExpandingStateTransition localExpandingStateTransition1 = DEFAULT_EXPANDING_STATE_TRANSITION;
    this.expandingStateTransition = localExpandingStateTransition1;
    ExpandingStateTransition localExpandingStateTransition2 = DEFAULT_EXPANDING_STATE_TRANSITION;
    this.portraitExpandingStateTransition = localExpandingStateTransition2;
    ExpandingState localExpandingState = ExpandingState.HIDDEN;
    this.expandingState = localExpandingState;
    float[] arrayOfFloat = new float[ExpandingState.values().length];
    this.exposurePercentages = arrayOfFloat;
    ArrayList localArrayList = new ArrayList();
    this.listeners = localArrayList;
    Context localContext = getContext();
    Resources localResources = localContext.getResources();
    if (!initialized)
    {
      Configuration localConfiguration = localResources.getConfiguration();
      updateStateTransition(localConfiguration);
      initialized = true;
    }
    Drawable localDrawable = localResources.getDrawable(2130837609);
    this.shadow = localDrawable;
    int i = localResources.getDimensionPixelSize(2131558456);
    this.shadowHeight = i;
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(localContext);
    int j = localViewConfiguration.getScaledTouchSlop();
    this.touchSlop = j;
    int k = localViewConfiguration.getScaledPagingTouchSlop();
    this.pagingTouchSlop = k;
    ExpandingScrollView.ConsistentTouchEventDispatcher.TouchEventDispatcher local1 = new ExpandingScrollView.ConsistentTouchEventDispatcher.TouchEventDispatcher()
    {
      public boolean dispatchEvent(MotionEvent paramAnonymousMotionEvent)
      {
        return ExpandingScrollView.this.onTouchEvent(paramAnonymousMotionEvent);
      }
    };
    ConsistentTouchEventDispatcher localConsistentTouchEventDispatcher = new ConsistentTouchEventDispatcher(local1);
    this.parentDispatcher = localConsistentTouchEventDispatcher;
    resetExposurePercentages();
  }

  public ExpandingScrollView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    ScrollState localScrollState = ScrollState.NO_SCROLL;
    this.scrollState = localScrollState;
    ExpandingStateTransition localExpandingStateTransition1 = DEFAULT_EXPANDING_STATE_TRANSITION;
    this.expandingStateTransition = localExpandingStateTransition1;
    ExpandingStateTransition localExpandingStateTransition2 = DEFAULT_EXPANDING_STATE_TRANSITION;
    this.portraitExpandingStateTransition = localExpandingStateTransition2;
    ExpandingState localExpandingState = ExpandingState.HIDDEN;
    this.expandingState = localExpandingState;
    float[] arrayOfFloat = new float[ExpandingState.values().length];
    this.exposurePercentages = arrayOfFloat;
    ArrayList localArrayList = new ArrayList();
    this.listeners = localArrayList;
    Context localContext = getContext();
    Resources localResources = localContext.getResources();
    if (!initialized)
    {
      Configuration localConfiguration = localResources.getConfiguration();
      updateStateTransition(localConfiguration);
      initialized = true;
    }
    Drawable localDrawable = localResources.getDrawable(2130837609);
    this.shadow = localDrawable;
    int i = localResources.getDimensionPixelSize(2131558456);
    this.shadowHeight = i;
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(localContext);
    int j = localViewConfiguration.getScaledTouchSlop();
    this.touchSlop = j;
    int k = localViewConfiguration.getScaledPagingTouchSlop();
    this.pagingTouchSlop = k;
    ExpandingScrollView.ConsistentTouchEventDispatcher.TouchEventDispatcher local1 = new ExpandingScrollView.ConsistentTouchEventDispatcher.TouchEventDispatcher()
    {
      public boolean dispatchEvent(MotionEvent paramAnonymousMotionEvent)
      {
        return ExpandingScrollView.this.onTouchEvent(paramAnonymousMotionEvent);
      }
    };
    ConsistentTouchEventDispatcher localConsistentTouchEventDispatcher = new ConsistentTouchEventDispatcher(local1);
    this.parentDispatcher = localConsistentTouchEventDispatcher;
    resetExposurePercentages();
  }

  private int getHeaderHeight()
  {
    int i = this.collapsedHeaderViewId;
    View localView = findViewById(i);
    if (localView == null);
    for (int j = 0; ; j = localView.getHeight())
      return j;
  }

  private ExpandingState getPreviousExpandingState(ExpandingState paramExpandingState)
  {
    return this.expandingStateTransition.getPrevious(paramExpandingState);
  }

  private ExpandingState nearestState(int paramInt)
  {
    Object localObject = null;
    int i = 2147483647;
    Iterator localIterator = this.expandingStateTransition.getScrollableStates().iterator();
    while (localIterator.hasNext())
    {
      ExpandingState localExpandingState = (ExpandingState)localIterator.next();
      int j = Math.abs(getExposurePixels(localExpandingState) - paramInt);
      if (j < i)
      {
        i = j;
        localObject = localExpandingState;
      }
    }
    return localObject;
  }

  private void notifyOnMoving(Iterable<ExpandingScrollViewListener> paramIterable)
  {
    int i = getScroll();
    Object localObject = ExpandingState.values()[0];
    ExpandingState[] arrayOfExpandingState = ExpandingState.values();
    int j = arrayOfExpandingState.length;
    int k = 0;
    Iterator localIterator;
    while (true)
    {
      ExpandingState localExpandingState1;
      if (k < j)
      {
        localExpandingState1 = arrayOfExpandingState[k];
        int m = getExposurePixels(localExpandingState1);
        if (i >= m);
      }
      else
      {
        float[] arrayOfFloat = this.exposurePercentages;
        int n = ((ExpandingState)localObject).ordinal();
        if (arrayOfFloat[n] != 100.0F)
          break;
        localIterator = paramIterable.iterator();
        while (true)
        {
          if (!localIterator.hasNext())
            return;
          ((ExpandingScrollViewListener)localIterator.next()).onMoving(this, (ExpandingState)localObject, 0.0F);
        }
      }
      localObject = localExpandingState1;
      k += 1;
    }
    int i1 = getExposurePixels((ExpandingState)localObject);
    ExpandingState localExpandingState2 = ExpandingState.HIDDEN;
    if (localObject == localExpandingState2);
    for (ExpandingState localExpandingState3 = ExpandingState.COLLAPSED; ; localExpandingState3 = getNextExpandingState((ExpandingState)localObject))
    {
      int i2 = getExposurePixels(localExpandingState3);
      float f1 = i;
      float f2 = i1;
      float f3 = f1 - f2;
      float f4 = i2 - i1;
      float f5 = f3 / f4;
      localIterator = paramIterable.iterator();
      while (true)
      {
        if (!localIterator.hasNext())
          return;
        ((ExpandingScrollViewListener)localIterator.next()).onMoving(this, (ExpandingState)localObject, f5);
      }
    }
  }

  private void scrollTo(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    super.scrollTo(paramInt1, paramInt2);
    if (!paramBoolean)
      return;
    if (this.listeners.isEmpty())
      return;
    List localList = this.listeners;
    notifyOnMoving(localList);
  }

  private void setScrollLimits()
  {
    ExpandingState localExpandingState1 = this.expandingState;
    ExpandingState localExpandingState2 = ExpandingState.HIDDEN;
    if (localExpandingState1 == localExpandingState2)
    {
      ExpandingState localExpandingState3 = ExpandingState.HIDDEN;
      int i = getExposurePixels(localExpandingState3);
      setScrollLimits(i, i);
      return;
    }
    ExpandingState localExpandingState4 = ExpandingState.COLLAPSED;
    int j = getExposurePixels(localExpandingState4);
    ExpandingState localExpandingState5 = ExpandingState.FULLY_EXPANDED;
    int k = getExposurePixels(localExpandingState5);
    setScrollLimits(j, k);
  }

  private void updateStateTransition(Configuration paramConfiguration)
  {
    if (paramConfiguration.orientation == 2);
    for (ExpandingStateTransition localExpandingStateTransition = NO_TWO_THIRDS_EXPANDING_STATE_TRANSITION; ; localExpandingStateTransition = this.portraitExpandingStateTransition)
    {
      this.expandingStateTransition = localExpandingStateTransition;
      ExpandingState localExpandingState = this.expandingState;
      setExpandingState(localExpandingState);
      return;
    }
  }

  public void addListener(ExpandingScrollViewListener paramExpandingScrollViewListener)
  {
    boolean bool = this.listeners.add(paramExpandingScrollViewListener);
    ImmutableList localImmutableList = ImmutableList.of(paramExpandingScrollViewListener);
    notifyOnMoving(localImmutableList);
    ExpandingState localExpandingState1 = this.expandingState;
    ExpandingState localExpandingState2 = this.expandingState;
    paramExpandingScrollViewListener.onExpandingStateChanged(this, localExpandingState1, localExpandingState2);
  }

  public boolean collapse()
  {
    int[] arrayOfInt = 3.$SwitchMap$com$google$android$music$widgets$ExpandingScrollView$ExpandingState;
    int i = this.expandingState.ordinal();
    switch (arrayOfInt[i])
    {
    default:
    case 3:
    }
    for (boolean bool = false; ; bool = true)
    {
      return bool;
      ExpandingState localExpandingState = ExpandingState.COLLAPSED;
      moveToExpandingState(localExpandingState);
    }
  }

  protected void dispatchDraw(Canvas paramCanvas)
  {
    this.shadow.draw(paramCanvas);
    super.dispatchDraw(paramCanvas);
  }

  void dragEnded()
  {
    super.dragEnded();
    Iterator localIterator = this.listeners.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      ExpandingScrollViewListener localExpandingScrollViewListener = (ExpandingScrollViewListener)localIterator.next();
      ExpandingState localExpandingState = this.expandingState;
      localExpandingScrollViewListener.onDragEnded(this, localExpandingState);
    }
  }

  void dragStarted()
  {
    super.dragStarted();
    Iterator localIterator = this.listeners.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      ExpandingScrollViewListener localExpandingScrollViewListener = (ExpandingScrollViewListener)localIterator.next();
      ExpandingState localExpandingState = this.expandingState;
      localExpandingScrollViewListener.onDragStarted(this, localExpandingState);
    }
  }

  public View getContent()
  {
    return this.contentView;
  }

  public ExpandingState getExpandingState()
  {
    return this.expandingState;
  }

  public int getExposurePixels(ExpandingState paramExpandingState)
  {
    float f = this.maxExpandPixels;
    float[] arrayOfFloat = this.exposurePercentages;
    int i = paramExpandingState.ordinal();
    int j = arrayOfFloat[i];
    return Math.round(f * j / 100.0F);
  }

  ExpandingState getNextExpandingState(ExpandingState paramExpandingState)
  {
    return this.expandingStateTransition.getNext(paramExpandingState);
  }

  public void moveToExpandingState(ExpandingState paramExpandingState)
  {
    setExpandingState(paramExpandingState);
    ExpandingState localExpandingState = this.expandingState;
    int i = getExposurePixels(localExpandingState);
    smoothScrollTo(i, false);
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    ExpandingState localExpandingState1 = this.expandingState;
    updateStateTransition(paramConfiguration);
    if (this.expandingState != localExpandingState1)
    {
      this.desiredState = localExpandingState1;
      ExpandingState localExpandingState2 = this.expandingState;
      this.coercedState = localExpandingState2;
      return;
    }
    if (this.desiredState == null)
      return;
    List localList = this.expandingStateTransition.getScrollableStates();
    ExpandingState localExpandingState3 = this.desiredState;
    if (!localList.contains(localExpandingState3))
      return;
    ExpandingState localExpandingState4 = this.expandingState;
    ExpandingState localExpandingState5 = this.coercedState;
    if (localExpandingState4 == localExpandingState5)
    {
      ExpandingState localExpandingState6 = this.desiredState;
      setExpandingState(localExpandingState6);
    }
    this.desiredState = null;
    this.coercedState = null;
  }

  protected void onDetachedFromWindow()
  {
    if (this.animateInRunnable != null)
    {
      Runnable localRunnable = this.animateInRunnable;
      boolean bool = removeCallbacks(localRunnable);
      this.animateInRunnable = null;
    }
    clearAnimation();
    super.onDetachedFromWindow();
  }

  public boolean onHoverEvent(MotionEvent paramMotionEvent)
  {
    return false;
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    return true;
  }

  public void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (this.alwaysExpanded);
    for (int i = 0; ; i = this.maxExpandPixels)
    {
      Drawable localDrawable = this.shadow;
      int j = this.shadowHeight + i;
      localDrawable.setBounds(paramInt1, i, paramInt3, j);
      int k = this.shadowHeight;
      int m = i + k;
      int n = paramInt3 - paramInt1;
      int i1 = 0;
      while (true)
      {
        int i2 = getChildCount();
        if (i1 >= i2)
          break;
        View localView = getChildAt(i1);
        int i3 = localView.getMeasuredHeight();
        int i4 = m + i3;
        int i5 = getPaddingRight();
        int i6 = n - i5;
        int i7 = getPaddingLeft();
        localView.layout(i7, m, i6, i4);
        m = i4;
        i1 += 1;
      }
    }
    ExpandingState localExpandingState1 = ExpandingState.COLLAPSED;
    int i8 = getHeaderHeight();
    int i9 = getContext().getResources().getDimensionPixelSize(2131558456);
    int i10 = i8 + i9;
    setExposurePixels(localExpandingState1, i10);
    setScrollLimits();
    if (this.alwaysExpanded);
    ExpandingState localExpandingState2;
    for (int i11 = 0; ; i11 = getExposurePixels(localExpandingState2))
    {
      scrollTo(0, i11, false);
      return;
      localExpandingState2 = this.expandingState;
    }
  }

  public void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt2);
    this.minExposure = 0;
    int j = this.minExposure;
    int k = i - j;
    this.maxExpandPixels = k;
    int m = 0;
    int n = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
    int i1 = 0;
    while (true)
    {
      int i2 = getChildCount();
      if (i1 >= i2)
        break;
      getChildAt(i1).measure(paramInt1, n);
      int i3 = getChildAt(i1).getMeasuredWidth();
      m = Math.max(m, i3);
      i1 += 1;
    }
    int i4 = this.maxExpandPixels + i;
    setMeasuredDimension(m, i4);
  }

  protected void onMoveFinished(float paramFloat)
  {
    ExpandingState localExpandingState1 = this.expandingState;
    ExpandingState localExpandingState2 = ExpandingState.HIDDEN;
    if (localExpandingState1 == localExpandingState2)
      return;
    float f1 = 0.3F * paramFloat;
    float f2 = getScroll();
    int i = (int)(f1 + f2);
    Object localObject = nearestState(i);
    ExpandingState localExpandingState3 = this.expandingState;
    ExpandingState localExpandingState5;
    if (localObject == localExpandingState3)
    {
      int j = getScroll();
      ExpandingState localExpandingState4 = this.expandingState;
      int k = getExposurePixels(localExpandingState4);
      if (j <= k)
        break label175;
      localExpandingState5 = this.expandingState;
    }
    label175: ExpandingState localExpandingState9;
    for (ExpandingState localExpandingState6 = getNextExpandingState(localExpandingState5); ; localExpandingState6 = getPreviousExpandingState(localExpandingState9))
    {
      ExpandingState localExpandingState7 = this.expandingState;
      if (localExpandingState6 != localExpandingState7)
      {
        ExpandingState localExpandingState8 = this.expandingState;
        int m = getExposurePixels(localExpandingState8);
        int n = getExposurePixels(localExpandingState6);
        float f3 = getScroll() - m;
        float f4 = n - m;
        if (f3 / f4 > 0.1F)
          localObject = localExpandingState6;
      }
      moveToExpandingState((ExpandingState)localObject);
      return;
      localExpandingState9 = this.expandingState;
    }
  }

  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    Parcelable localParcelable = localSavedState.getSuperState();
    super.onRestoreInstanceState(localParcelable);
    Iterator localIterator = this.listeners.iterator();
    while (localIterator.hasNext())
    {
      ExpandingScrollViewListener localExpandingScrollViewListener = (ExpandingScrollViewListener)localIterator.next();
      ExpandingState localExpandingState1 = this.expandingState;
      ExpandingState localExpandingState2 = localSavedState.expandingState;
      localExpandingScrollViewListener.onExpandingStateChanged(this, localExpandingState1, localExpandingState2);
    }
    ExpandingState localExpandingState3 = localSavedState.expandingState;
    this.expandingState = localExpandingState3;
  }

  protected Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable = super.onSaveInstanceState();
    ExpandingState localExpandingState = this.expandingState;
    float[] arrayOfFloat = this.exposurePercentages;
    return new SavedState(localParcelable, localExpandingState, arrayOfFloat);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    ExpandingState localExpandingState1 = this.expandingState;
    ExpandingState localExpandingState2 = ExpandingState.HIDDEN;
    boolean bool1;
    if (localExpandingState1 == localExpandingState2)
      bool1 = false;
    int k;
    int m;
    float f1;
    float f2;
    while (true)
    {
      return bool1;
      int i = this.maxExpandPixels;
      int j = getScrollY();
      k = i - j;
      m = paramMotionEvent.getAction();
      f1 = paramMotionEvent.getX();
      f2 = paramMotionEvent.getY();
      if (m == 0)
      {
        float f3 = k;
        if (f2 < f3)
          break label152;
      }
      label152: for (ScrollState localScrollState1 = ScrollState.START_TOUCH; ; localScrollState1 = ScrollState.NO_SCROLL)
      {
        this.scrollState = localScrollState1;
        this.lastDownX = f1;
        this.lastDownY = f2;
        float f4 = k;
        float f5 = f2 - f4;
        this.lastDownYOffset = f5;
        this.lastChildDownY = -1.0F;
        this.childHasScrolled = false;
        ScrollState localScrollState2 = this.scrollState;
        ScrollState localScrollState3 = ScrollState.NO_SCROLL;
        if (localScrollState2 != localScrollState3)
          break label160;
        bool1 = false;
        break;
      }
      label160: if (this.contentView != null)
        break;
      bool1 = super.onTouchEvent(paramMotionEvent);
    }
    boolean bool2 = this.childDispatcher.hasActiveEventStream();
    boolean bool3 = bool2;
    if (m == 0)
    {
      bool3 = true;
      label198: if (!bool3)
        break label647;
      this.parentDispatcher.cancelEventStream();
      float f6 = -k;
      paramMotionEvent.offsetLocation(0.0F, f6);
      if (this.lastChildDownY < 0.0F)
      {
        float f7 = paramMotionEvent.getY();
        this.lastChildDownY = f7;
      }
      boolean bool4 = this.childDispatcher.dispatchEvent(paramMotionEvent);
    }
    while (true)
    {
      this.lastY = f2;
      bool1 = true;
      break;
      if (m == 2)
      {
        ScrollState localScrollState4 = this.scrollState;
        ScrollState localScrollState5 = ScrollState.START_TOUCH;
        if (localScrollState4 == localScrollState5)
        {
          float f8 = this.lastDownY;
          float f9 = Math.abs(f2 - f8);
          float f10 = this.touchSlop;
          if (f9 > f10)
          {
            ScrollState localScrollState6 = ScrollState.VERTICAL_SCROLL;
            this.scrollState = localScrollState6;
          }
        }
        else
        {
          label336: ScrollState localScrollState7 = this.scrollState;
          ScrollState localScrollState8 = ScrollState.VERTICAL_SCROLL;
          if (localScrollState7 != localScrollState8)
            break label198;
          float f11 = this.lastY;
          float f12 = f2 - f11;
          int n = getScroll();
          int i1 = this.maxExpandPixels;
          if (n == i1)
            break label567;
          float f13 = getHeaderHeight();
          float f14 = this.lastDownYOffset;
          if (f13 >= f14)
            break label567;
          View localView = this.contentView;
          int i2 = (int)f12;
          int i3 = (int)this.lastDownX;
          int i4 = (int)this.lastDownYOffset;
          if (!ViewUtils.canScrollVertically(localView, true, i2, i3, i4))
            break label567;
        }
        label567: for (bool3 = true; ; bool3 = false)
        {
          float f15 = k;
          float f16 = f2 - f15;
          float f17 = this.lastChildDownY;
          float f18 = f16 - f17;
          if ((!bool3) || (!bool2) || (this.childHasScrolled))
            break label573;
          float f19 = Math.abs(f18);
          float f20 = this.touchSlop;
          if (f19 <= f20)
            break label573;
          this.childHasScrolled = true;
          break;
          float f21 = this.lastDownX;
          float f22 = Math.abs(f1 - f21);
          float f23 = this.touchSlop;
          if (f22 <= f23)
            break label336;
          ScrollState localScrollState9 = ScrollState.HORIZONTAL_SCROLL;
          this.scrollState = localScrollState9;
          break label336;
        }
        label573: if (bool3)
          break label198;
        this.childHasScrolled = false;
        break label198;
      }
      if (m != 1)
        break label198;
      ScrollState localScrollState10 = this.scrollState;
      ScrollState localScrollState11 = ScrollState.START_TOUCH;
      if (localScrollState10 == localScrollState11)
      {
        bool3 = true;
        break label198;
      }
      ScrollState localScrollState12 = this.scrollState;
      ScrollState localScrollState13 = ScrollState.VERTICAL_SCROLL;
      if ((localScrollState12 != localScrollState13) || (this.childHasScrolled))
        break label198;
      bool3 = false;
      break label198;
      label647: this.childDispatcher.cancelEventStream();
      this.lastChildDownY = -1.0F;
      boolean bool5 = this.parentDispatcher.dispatchEvent(paramMotionEvent);
    }
  }

  public boolean removeListener(ExpandingScrollViewListener paramExpandingScrollViewListener)
  {
    return this.listeners.remove(paramExpandingScrollViewListener);
  }

  public void resetExposurePercentages()
  {
    ExpandingState[] arrayOfExpandingState = ExpandingState.values();
    int i = arrayOfExpandingState.length;
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      ExpandingState localExpandingState = arrayOfExpandingState[j];
      float[] arrayOfFloat = this.exposurePercentages;
      int k = localExpandingState.ordinal();
      float f = localExpandingState.defaultExposurePercentage;
      arrayOfFloat[k] = f;
      j += 1;
    }
  }

  public void scrollTo(int paramInt1, int paramInt2)
  {
    scrollTo(paramInt1, paramInt2, true);
  }

  public void setAlwaysExpanded(boolean paramBoolean)
  {
    this.alwaysExpanded = paramBoolean;
  }

  public void setContent(View paramView)
  {
    removeAllViews();
    this.contentView = paramView;
    if (paramView != null)
    {
      addView(paramView);
      ExpandingScrollView.ConsistentTouchEventDispatcher.TouchEventDispatcher local2 = new ExpandingScrollView.ConsistentTouchEventDispatcher.TouchEventDispatcher()
      {
        public boolean dispatchEvent(MotionEvent paramAnonymousMotionEvent)
        {
          return ExpandingScrollView.this.contentView.dispatchTouchEvent(paramAnonymousMotionEvent);
        }
      };
      ConsistentTouchEventDispatcher localConsistentTouchEventDispatcher = new ConsistentTouchEventDispatcher(local2);
      this.childDispatcher = localConsistentTouchEventDispatcher;
      return;
    }
    this.childDispatcher = null;
  }

  public void setExpandingState(ExpandingState paramExpandingState)
  {
    ExpandingState localExpandingState1 = this.expandingState;
    ExpandingState localExpandingState2 = this.expandingStateTransition.convertToAllowedState(paramExpandingState);
    this.expandingState = localExpandingState2;
    setScrollLimits();
    if (this.expandingState == localExpandingState1)
      return;
    Iterator localIterator = this.listeners.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      ExpandingScrollViewListener localExpandingScrollViewListener = (ExpandingScrollViewListener)localIterator.next();
      ExpandingState localExpandingState3 = this.expandingState;
      localExpandingScrollViewListener.onExpandingStateChanged(this, localExpandingState1, localExpandingState3);
    }
  }

  public void setExpandingStateTransition(ExpandingStateTransition paramExpandingStateTransition)
  {
    this.portraitExpandingStateTransition = paramExpandingStateTransition;
    Configuration localConfiguration = getContext().getResources().getConfiguration();
    updateStateTransition(localConfiguration);
    this.desiredState = null;
    this.coercedState = null;
  }

  public void setExposurePercentage(ExpandingState paramExpandingState, float paramFloat)
  {
    int i = paramExpandingState.ordinal();
    if (this.exposurePercentages[i] == paramFloat)
      return;
    ExpandingState localExpandingState1 = getPreviousExpandingState(paramExpandingState);
    if (paramExpandingState != localExpandingState1)
    {
      float[] arrayOfFloat1 = this.exposurePercentages;
      int j = localExpandingState1.ordinal();
      int k = arrayOfFloat1[j];
      if (paramFloat < k)
      {
        Object[] arrayOfObject1 = new Object[3];
        arrayOfObject1[0] = paramExpandingState;
        Float localFloat1 = Float.valueOf(paramFloat);
        arrayOfObject1[1] = localFloat1;
        float[] arrayOfFloat2 = this.exposurePercentages;
        int m = localExpandingState1.ordinal();
        Float localFloat2 = Float.valueOf(arrayOfFloat2[m]);
        arrayOfObject1[2] = localFloat2;
        String str1 = String.format("Exposure percentage less than previous state: state=%s, percentage=%f, prevStatePercentage=%f", arrayOfObject1);
        int n = Log.w("ExpandingScrollView", str1);
      }
    }
    ExpandingState localExpandingState2 = getNextExpandingState(paramExpandingState);
    if (paramExpandingState != localExpandingState2)
    {
      float[] arrayOfFloat3 = this.exposurePercentages;
      int i1 = localExpandingState2.ordinal();
      int i2 = arrayOfFloat3[i1];
      if (paramFloat > i2)
      {
        Object[] arrayOfObject2 = new Object[3];
        arrayOfObject2[0] = paramExpandingState;
        Float localFloat3 = Float.valueOf(paramFloat);
        arrayOfObject2[1] = localFloat3;
        float[] arrayOfFloat4 = this.exposurePercentages;
        int i3 = localExpandingState2.ordinal();
        Float localFloat4 = Float.valueOf(arrayOfFloat4[i3]);
        arrayOfObject2[2] = localFloat4;
        String str2 = String.format("Exposure percentage more than next state: state=%s, percentage=%f, nextStatePercentage=%f", arrayOfObject2);
        int i4 = Log.w("ExpandingScrollView", str2);
      }
    }
    this.exposurePercentages[i] = paramFloat;
    setScrollLimits();
    if (this.isBeingDragged)
    {
      int i5 = getScroll();
      while (true)
      {
        ExpandingState localExpandingState3 = this.expandingState;
        ExpandingState localExpandingState4 = getPreviousExpandingState(localExpandingState3);
        int i6 = getExposurePixels(localExpandingState4);
        if (i5 >= i6)
          break;
        ExpandingState localExpandingState5 = this.expandingState;
        ExpandingState localExpandingState6 = getPreviousExpandingState(localExpandingState5);
        setExpandingState(localExpandingState6);
      }
      while (true)
      {
        ExpandingState localExpandingState7 = this.expandingState;
        ExpandingState localExpandingState8 = getNextExpandingState(localExpandingState7);
        int i7 = getExposurePixels(localExpandingState8);
        if (i5 <= i7)
          return;
        ExpandingState localExpandingState9 = this.expandingState;
        ExpandingState localExpandingState10 = getNextExpandingState(localExpandingState9);
        setExpandingState(localExpandingState10);
      }
    }
    if (this.expandingState != paramExpandingState)
      return;
    int i8 = getExposurePixels(paramExpandingState);
    smoothScrollTo(i8);
  }

  public void setExposurePixels(ExpandingState paramExpandingState, int paramInt)
  {
    float f1 = paramInt * 100.0F;
    float f2 = this.maxExpandPixels;
    float f3 = f1 / f2;
    setExposurePercentage(paramExpandingState, f3);
  }

  public void setHidden(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      ExpandingState localExpandingState1 = ExpandingState.HIDDEN;
      moveToExpandingState(localExpandingState1);
      return;
    }
    ExpandingState localExpandingState2 = this.expandingState;
    ExpandingState localExpandingState3 = ExpandingState.HIDDEN;
    if (localExpandingState2 != localExpandingState3)
      return;
    ExpandingState localExpandingState4 = ExpandingState.COLLAPSED;
    moveToExpandingState(localExpandingState4);
  }

  public void setViewIdForSizingCollapsedState(int paramInt)
  {
    this.collapsedHeaderViewId = paramInt;
  }

  private static class ConsistentTouchEventDispatcher
  {
    private TouchEventDispatcher dispatcher;
    private MotionEvent lastSentEvent;

    public ConsistentTouchEventDispatcher(TouchEventDispatcher paramTouchEventDispatcher)
    {
      this.dispatcher = paramTouchEventDispatcher;
    }

    private static int getPointerCountAfterEvent(MotionEvent paramMotionEvent)
    {
      int i;
      if (paramMotionEvent == null)
        i = 0;
      while (true)
      {
        return i;
        i = paramMotionEvent.getPointerCount();
        int j = paramMotionEvent.getActionMasked();
        if (j == 3)
          i = 0;
        else if ((j == 6) || (j == 1))
          i += -1;
      }
    }

    public void cancelEventStream()
    {
      if (this.lastSentEvent == null)
        return;
      this.lastSentEvent.setAction(3);
      TouchEventDispatcher localTouchEventDispatcher = this.dispatcher;
      MotionEvent localMotionEvent = this.lastSentEvent;
      boolean bool = localTouchEventDispatcher.dispatchEvent(localMotionEvent);
      this.lastSentEvent.recycle();
      this.lastSentEvent = null;
    }

    public boolean dispatchEvent(MotionEvent paramMotionEvent)
    {
      int i = paramMotionEvent.getActionMasked();
      int j;
      int k;
      MotionEvent localMotionEvent1;
      MotionEvent localMotionEvent2;
      if (i != 3)
      {
        j = paramMotionEvent.getPointerCount();
        if ((i == 5) || (i == 0))
          j += -1;
        k = getPointerCountAfterEvent(this.lastSentEvent);
        if (k < j)
        {
          localMotionEvent1 = MotionEvent.obtain(paramMotionEvent);
          if (k == 0)
          {
            localMotionEvent1.setAction(0);
            boolean bool1 = this.dispatcher.dispatchEvent(localMotionEvent1);
            k += 1;
          }
          while (k < j)
          {
            int m = k << 8 | 0x5;
            localMotionEvent1.setAction(m);
            boolean bool2 = this.dispatcher.dispatchEvent(localMotionEvent1);
            k += 1;
          }
          localMotionEvent1.recycle();
        }
      }
      else
      {
        if ((i == 1) || (i == 3))
          break label269;
        localMotionEvent2 = MotionEvent.obtain(paramMotionEvent);
      }
      label269: for (this.lastSentEvent = localMotionEvent2; ; this.lastSentEvent = null)
      {
        return this.dispatcher.dispatchEvent(paramMotionEvent);
        if (k <= j)
          break;
        localMotionEvent1 = MotionEvent.obtain(this.lastSentEvent);
        int n = Math.max(j, 1);
        while (k > n)
        {
          k += -1;
          int i1 = k << 8 | 0x6;
          localMotionEvent1.setAction(i1);
          boolean bool3 = this.dispatcher.dispatchEvent(localMotionEvent1);
        }
        if (j == 0)
        {
          localMotionEvent1.setAction(1);
          boolean bool4 = this.dispatcher.dispatchEvent(localMotionEvent1);
          int i2 = k + -1;
        }
        localMotionEvent1.recycle();
        break;
      }
    }

    public boolean hasActiveEventStream()
    {
      if (this.lastSentEvent != null);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public static abstract interface TouchEventDispatcher
    {
      public abstract boolean dispatchEvent(MotionEvent paramMotionEvent);
    }
  }

  private static class NoTwoThirdsExpandingStateTransition extends ExpandingScrollView.ExpandingStateTransition
  {
    NoTwoThirdsExpandingStateTransition()
    {
      super();
    }

    public ExpandingScrollView.ExpandingState convertToAllowedState(ExpandingScrollView.ExpandingState paramExpandingState)
    {
      return paramExpandingState;
    }

    public ExpandingScrollView.ExpandingState getPrevious(ExpandingScrollView.ExpandingState paramExpandingState)
    {
      return super.getPrevious(paramExpandingState);
    }
  }

  public static class ExpandingStateTransition
  {
    private final ImmutableList<ExpandingScrollView.ExpandingState> scrollableStates;

    ExpandingStateTransition()
    {
      this(localImmutableList);
    }

    protected ExpandingStateTransition(ImmutableList<ExpandingScrollView.ExpandingState> paramImmutableList)
    {
      this.scrollableStates = paramImmutableList;
    }

    public ExpandingScrollView.ExpandingState convertToAllowedState(ExpandingScrollView.ExpandingState paramExpandingState)
    {
      return paramExpandingState;
    }

    public ExpandingScrollView.ExpandingState getNext(ExpandingScrollView.ExpandingState paramExpandingState)
    {
      ExpandingScrollView.ExpandingState localExpandingState = paramExpandingState.next;
      return convertToAllowedState(localExpandingState);
    }

    public ExpandingScrollView.ExpandingState getPrevious(ExpandingScrollView.ExpandingState paramExpandingState)
    {
      return paramExpandingState.previous;
    }

    public List<ExpandingScrollView.ExpandingState> getScrollableStates()
    {
      return this.scrollableStates;
    }
  }

  public static enum ExpandingState
  {
    final float defaultExposurePercentage;
    ExpandingState next;
    ExpandingState previous;

    static
    {
      COLLAPSED = new ExpandingState("COLLAPSED", 1, 25.0F);
      FULLY_EXPANDED = new ExpandingState("FULLY_EXPANDED", 2, 100.0F);
      ExpandingState[] arrayOfExpandingState = new ExpandingState[3];
      ExpandingState localExpandingState1 = HIDDEN;
      arrayOfExpandingState[0] = localExpandingState1;
      ExpandingState localExpandingState2 = COLLAPSED;
      arrayOfExpandingState[1] = localExpandingState2;
      ExpandingState localExpandingState3 = FULLY_EXPANDED;
      arrayOfExpandingState[2] = localExpandingState3;
      $VALUES = arrayOfExpandingState;
      ExpandingState localExpandingState4 = HIDDEN;
      ExpandingState localExpandingState5 = HIDDEN;
      localExpandingState4.previous = localExpandingState5;
      ExpandingState localExpandingState6 = HIDDEN;
      ExpandingState localExpandingState7 = HIDDEN;
      localExpandingState6.next = localExpandingState7;
      ExpandingState localExpandingState8 = COLLAPSED;
      ExpandingState localExpandingState9 = COLLAPSED;
      localExpandingState8.previous = localExpandingState9;
      ExpandingState localExpandingState10 = COLLAPSED;
      ExpandingState localExpandingState11 = FULLY_EXPANDED;
      localExpandingState10.next = localExpandingState11;
      ExpandingState localExpandingState12 = FULLY_EXPANDED;
      ExpandingState localExpandingState13 = COLLAPSED;
      localExpandingState12.previous = localExpandingState13;
      ExpandingState localExpandingState14 = FULLY_EXPANDED;
      ExpandingState localExpandingState15 = FULLY_EXPANDED;
      localExpandingState14.next = localExpandingState15;
    }

    private ExpandingState(float paramFloat)
    {
      this.defaultExposurePercentage = paramFloat;
    }
  }

  public static class SavedState extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public ExpandingScrollView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ExpandingScrollView.SavedState(paramAnonymousParcel, null);
      }

      public ExpandingScrollView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ExpandingScrollView.SavedState[paramAnonymousInt];
      }
    };
    private ExpandingScrollView.ExpandingState expandingState;
    private float[] exposurePercentages;

    private SavedState(Parcel paramParcel)
    {
      super();
      ExpandingScrollView.ExpandingState localExpandingState = ExpandingScrollView.ExpandingState.valueOf(paramParcel.readString());
      this.expandingState = localExpandingState;
      float[] arrayOfFloat = paramParcel.createFloatArray();
      this.exposurePercentages = arrayOfFloat;
    }

    public SavedState(Parcelable paramParcelable, ExpandingScrollView.ExpandingState paramExpandingState, float[] paramArrayOfFloat)
    {
      super();
      this.expandingState = paramExpandingState;
      this.exposurePercentages = paramArrayOfFloat;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      String str = this.expandingState.toString();
      paramParcel.writeString(str);
      float[] arrayOfFloat = this.exposurePercentages;
      paramParcel.writeFloatArray(arrayOfFloat);
    }
  }

  public static enum ScrollState
  {
    static
    {
      HORIZONTAL_SCROLL = new ScrollState("HORIZONTAL_SCROLL", 2);
      VERTICAL_SCROLL = new ScrollState("VERTICAL_SCROLL", 3);
      ScrollState[] arrayOfScrollState = new ScrollState[4];
      ScrollState localScrollState1 = NO_SCROLL;
      arrayOfScrollState[0] = localScrollState1;
      ScrollState localScrollState2 = START_TOUCH;
      arrayOfScrollState[1] = localScrollState2;
      ScrollState localScrollState3 = HORIZONTAL_SCROLL;
      arrayOfScrollState[2] = localScrollState3;
      ScrollState localScrollState4 = VERTICAL_SCROLL;
      arrayOfScrollState[3] = localScrollState4;
    }
  }

  public static abstract interface ExpandingScrollViewListener
  {
    public abstract void onDragEnded(ExpandingScrollView paramExpandingScrollView, ExpandingScrollView.ExpandingState paramExpandingState);

    public abstract void onDragStarted(ExpandingScrollView paramExpandingScrollView, ExpandingScrollView.ExpandingState paramExpandingState);

    public abstract void onExpandingStateChanged(ExpandingScrollView paramExpandingScrollView, ExpandingScrollView.ExpandingState paramExpandingState1, ExpandingScrollView.ExpandingState paramExpandingState2);

    public abstract void onMoving(ExpandingScrollView paramExpandingScrollView, ExpandingScrollView.ExpandingState paramExpandingState, float paramFloat);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.widgets.ExpandingScrollView
 * JD-Core Version:    0.6.2
 */