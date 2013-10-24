package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityRecordCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ViewPager extends ViewGroup
{
  private static final Comparator<ItemInfo> COMPARATOR = new Comparator()
  {
    public int compare(ViewPager.ItemInfo paramAnonymousItemInfo1, ViewPager.ItemInfo paramAnonymousItemInfo2)
    {
      int i = paramAnonymousItemInfo1.position;
      int j = paramAnonymousItemInfo2.position;
      return i - j;
    }
  };
  private static final int[] LAYOUT_ATTRS;
  private static final Interpolator sInterpolator = new Interpolator()
  {
    public float getInterpolation(float paramAnonymousFloat)
    {
      float f = paramAnonymousFloat - 1.0F;
      return f * f * f * f * f + 1.0F;
    }
  };
  private static final ViewPositionComparator sPositionComparator = new ViewPositionComparator();
  private int mActivePointerId;
  private PagerAdapter mAdapter;
  private OnAdapterChangeListener mAdapterChangeListener;
  private int mBottomPageBounds;
  private boolean mCalledSuper;
  private int mChildHeightMeasureSpec;
  private int mChildWidthMeasureSpec;
  private int mCloseEnough;
  private int mCurItem;
  private int mDecorChildCount;
  private int mDefaultGutterSize;
  private int mDrawingOrder;
  private ArrayList<View> mDrawingOrderedChildren;
  private final Runnable mEndScrollRunnable;
  private int mExpectedAdapterCount;
  private boolean mFakeDragging;
  private boolean mFirstLayout;
  private float mFirstOffset;
  private int mFlingDistance;
  private int mGutterSize;
  private boolean mInLayout;
  private float mInitialMotionX;
  private float mInitialMotionY;
  private OnPageChangeListener mInternalPageChangeListener;
  private boolean mIsBeingDragged;
  private boolean mIsUnableToDrag;
  private final ArrayList<ItemInfo> mItems;
  private float mLastMotionX;
  private float mLastMotionY;
  private float mLastOffset;
  private EdgeEffectCompat mLeftEdge;
  private Drawable mMarginDrawable;
  private int mMaximumVelocity;
  private int mMinimumVelocity;
  private boolean mNeedCalculatePageOffsets;
  private PagerObserver mObserver;
  private int mOffscreenPageLimit;
  private OnPageChangeListener mOnPageChangeListener;
  private int mPageMargin;
  private PageTransformer mPageTransformer;
  private boolean mPopulatePending;
  private Parcelable mRestoredAdapterState;
  private ClassLoader mRestoredClassLoader;
  private int mRestoredCurItem;
  private EdgeEffectCompat mRightEdge;
  private int mScrollState;
  private Scroller mScroller;
  private boolean mScrollingCacheEnabled;
  private Method mSetChildrenDrawingOrderEnabled;
  private final ItemInfo mTempItem;
  private final Rect mTempRect;
  private int mTopPageBounds;
  private int mTouchSlop;
  private VelocityTracker mVelocityTracker;

  static
  {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 16842931;
    LAYOUT_ATTRS = arrayOfInt;
  }

  public ViewPager(Context paramContext)
  {
    super(paramContext);
    ArrayList localArrayList = new ArrayList();
    this.mItems = localArrayList;
    ItemInfo localItemInfo = new ItemInfo();
    this.mTempItem = localItemInfo;
    Rect localRect = new Rect();
    this.mTempRect = localRect;
    this.mRestoredCurItem = -1;
    this.mRestoredAdapterState = null;
    this.mRestoredClassLoader = null;
    this.mFirstOffset = -3.402824E+38F;
    this.mLastOffset = 3.4028235E+38F;
    this.mOffscreenPageLimit = 1;
    this.mActivePointerId = -1;
    this.mFirstLayout = true;
    this.mNeedCalculatePageOffsets = false;
    Runnable local3 = new Runnable()
    {
      public void run()
      {
        ViewPager.this.setScrollState(0);
        ViewPager.this.populate();
      }
    };
    this.mEndScrollRunnable = local3;
    this.mScrollState = 0;
    initViewPager();
  }

  public ViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    ArrayList localArrayList = new ArrayList();
    this.mItems = localArrayList;
    ItemInfo localItemInfo = new ItemInfo();
    this.mTempItem = localItemInfo;
    Rect localRect = new Rect();
    this.mTempRect = localRect;
    this.mRestoredCurItem = -1;
    this.mRestoredAdapterState = null;
    this.mRestoredClassLoader = null;
    this.mFirstOffset = -3.402824E+38F;
    this.mLastOffset = 3.4028235E+38F;
    this.mOffscreenPageLimit = 1;
    this.mActivePointerId = -1;
    this.mFirstLayout = true;
    this.mNeedCalculatePageOffsets = false;
    Runnable local3 = new Runnable()
    {
      public void run()
      {
        ViewPager.this.setScrollState(0);
        ViewPager.this.populate();
      }
    };
    this.mEndScrollRunnable = local3;
    this.mScrollState = 0;
    initViewPager();
  }

  private void calculatePageOffsets(ItemInfo paramItemInfo1, int paramInt, ItemInfo paramItemInfo2)
  {
    int i = this.mAdapter.getCount();
    int j = getClientWidth();
    float f3;
    int k;
    int n;
    float f6;
    int i1;
    ItemInfo localItemInfo1;
    if (j > 0)
    {
      float f1 = this.mPageMargin;
      float f2 = j;
      f3 = f1 / f2;
      if (paramItemInfo2 == null)
        break label468;
      k = paramItemInfo2.position;
      int m = paramItemInfo1.position;
      if (k < m)
      {
        n = 0;
        float f4 = paramItemInfo2.offset;
        float f5 = paramItemInfo2.widthFactor;
        f6 = f4 + f5 + f3;
        i1 = k + 1;
      }
    }
    else
    {
      while (true)
      {
        int i2 = paramItemInfo1.position;
        if (i1 > i2)
          break label468;
        int i3 = this.mItems.size();
        if (n >= i3)
          break label468;
        localItemInfo1 = (ItemInfo)this.mItems.get(n);
        while (true)
        {
          int i4 = localItemInfo1.position;
          if (i1 <= i4)
            break;
          int i5 = this.mItems.size() + -1;
          if (n >= i5)
            break;
          n += 1;
          ItemInfo localItemInfo2 = (ItemInfo)this.mItems.get(n);
        }
        f3 = 0.0F;
        break;
        while (true)
        {
          int i6 = localItemInfo1.position;
          if (i1 >= i6)
            break;
          float f7 = this.mAdapter.getPageWidth(i1) + f3;
          f6 += f7;
          i1 += 1;
        }
        localItemInfo1.offset = f6;
        float f8 = localItemInfo1.widthFactor + f3;
        f6 += f8;
        i1 += 1;
      }
    }
    int i7 = paramItemInfo1.position;
    if (k > i7)
    {
      n = this.mItems.size() + -1;
      f6 = paramItemInfo2.offset;
      i1 = k + -1;
      while (true)
      {
        int i8 = paramItemInfo1.position;
        if ((i1 < i8) || (n < 0))
          break;
        localItemInfo1 = (ItemInfo)this.mItems.get(n);
        while (true)
        {
          int i9 = localItemInfo1.position;
          if ((i1 >= i9) || (n <= 0))
            break;
          n += -1;
          ItemInfo localItemInfo3 = (ItemInfo)this.mItems.get(n);
        }
        while (true)
        {
          int i10 = localItemInfo1.position;
          if (i1 <= i10)
            break;
          float f9 = this.mAdapter.getPageWidth(i1) + f3;
          f6 -= f9;
          i1 += -1;
        }
        float f10 = localItemInfo1.widthFactor + f3;
        f6 -= f10;
        localItemInfo1.offset = f6;
        i1 += -1;
      }
    }
    label468: int i11 = this.mItems.size();
    float f11 = paramItemInfo1.offset;
    int i12 = paramItemInfo1.position + -1;
    float f12;
    float f15;
    label553: int i15;
    if (paramItemInfo1.position == 0)
    {
      f12 = paramItemInfo1.offset;
      this.mFirstOffset = f12;
      int i13 = paramItemInfo1.position;
      int i14 = i + -1;
      if (i13 == i14)
        break label644;
      float f13 = paramItemInfo1.offset;
      float f14 = paramItemInfo1.widthFactor;
      f15 = f13 + f14 - 1.0F;
      this.mLastOffset = f15;
      i15 = paramInt + -1;
    }
    while (true)
    {
      if (i15 < 0)
        break label706;
      localItemInfo1 = (ItemInfo)this.mItems.get(i15);
      while (true)
      {
        int i16 = localItemInfo1.position;
        if (i12 <= i16)
          break;
        PagerAdapter localPagerAdapter1 = this.mAdapter;
        int i17 = i12 + -1;
        float f16 = localPagerAdapter1.getPageWidth(i12) + f3;
        f11 -= f16;
        i12 = i17;
      }
      f12 = -3.402824E+38F;
      break;
      label644: f15 = 3.4028235E+38F;
      break label553;
      float f17 = localItemInfo1.widthFactor + f3;
      f11 -= f17;
      localItemInfo1.offset = f11;
      if (localItemInfo1.position == 0)
        this.mFirstOffset = f11;
      i15 += -1;
      i12 += -1;
    }
    label706: float f18 = paramItemInfo1.offset;
    float f19 = paramItemInfo1.widthFactor;
    float f20 = f18 + f19 + f3;
    int i18 = paramItemInfo1.position + 1;
    int i19 = paramInt + 1;
    while (i19 < i11)
    {
      localItemInfo1 = (ItemInfo)this.mItems.get(i19);
      while (true)
      {
        int i20 = localItemInfo1.position;
        if (i18 >= i20)
          break;
        PagerAdapter localPagerAdapter2 = this.mAdapter;
        int i21 = i18 + 1;
        float f21 = localPagerAdapter2.getPageWidth(i18) + f3;
        f20 += f21;
        i18 = i21;
      }
      int i22 = localItemInfo1.position;
      int i23 = i + -1;
      if (i22 != i23)
      {
        float f22 = localItemInfo1.widthFactor + f20 - 1.0F;
        this.mLastOffset = f22;
      }
      localItemInfo1.offset = f20;
      float f23 = localItemInfo1.widthFactor + f3;
      f20 += f23;
      i19 += 1;
      i18 += 1;
    }
    this.mNeedCalculatePageOffsets = false;
  }

  private void completeScroll(boolean paramBoolean)
  {
    if (this.mScrollState == 2);
    for (int i = 1; ; i = 0)
    {
      if (i != 0)
      {
        setScrollingCacheEnabled(false);
        this.mScroller.abortAnimation();
        int j = getScrollX();
        int k = getScrollY();
        int m = this.mScroller.getCurrX();
        int n = this.mScroller.getCurrY();
        if ((j == m) || (k != n))
          scrollTo(m, n);
      }
      this.mPopulatePending = false;
      int i1 = 0;
      while (true)
      {
        int i2 = this.mItems.size();
        if (i1 >= i2)
          break;
        ItemInfo localItemInfo = (ItemInfo)this.mItems.get(i1);
        if (localItemInfo.scrolling)
        {
          i = 1;
          localItemInfo.scrolling = false;
        }
        i1 += 1;
      }
    }
    if (i == 0)
      return;
    if (paramBoolean)
    {
      Runnable localRunnable = this.mEndScrollRunnable;
      ViewCompat.postOnAnimation(this, localRunnable);
      return;
    }
    this.mEndScrollRunnable.run();
  }

  private int determineTargetPage(int paramInt1, float paramFloat, int paramInt2, int paramInt3)
  {
    int i = Math.abs(paramInt3);
    int j = this.mFlingDistance;
    if (i > j)
    {
      int k = Math.abs(paramInt2);
      int m = this.mMinimumVelocity;
      if (k > m)
      {
        if (paramInt2 > 0);
        while (true)
        {
          if (this.mItems.size() > 0)
          {
            ItemInfo localItemInfo1 = (ItemInfo)this.mItems.get(0);
            ArrayList localArrayList = this.mItems;
            int n = this.mItems.size() + -1;
            ItemInfo localItemInfo2 = (ItemInfo)localArrayList.get(n);
            int i1 = localItemInfo1.position;
            int i2 = localItemInfo2.position;
            int i3 = Math.min(paramInt1, i2);
            paramInt1 = Math.max(i1, i3);
          }
          return paramInt1;
          paramInt1 += 1;
        }
      }
    }
    int i4 = this.mCurItem;
    if (paramInt1 >= i4);
    for (float f1 = 0.4F; ; f1 = 0.6F)
    {
      float f2 = paramInt1 + paramFloat;
      paramInt1 = (int)(f1 + f2);
      break;
    }
  }

  private void enableLayers(boolean paramBoolean)
  {
    int i = getChildCount();
    int j = 0;
    if (j >= i)
      return;
    if (paramBoolean);
    for (int k = 2; ; k = 0)
    {
      ViewCompat.setLayerType(getChildAt(j), k, null);
      j += 1;
      break;
    }
  }

  private void endDrag()
  {
    this.mIsBeingDragged = false;
    this.mIsUnableToDrag = false;
    if (this.mVelocityTracker == null)
      return;
    this.mVelocityTracker.recycle();
    this.mVelocityTracker = null;
  }

  private Rect getChildRectInPagerCoordinates(Rect paramRect, View paramView)
  {
    if (paramRect == null)
      paramRect = new Rect();
    if (paramView == null)
      paramRect.set(0, 0, 0, 0);
    while (true)
    {
      return paramRect;
      int i = paramView.getLeft();
      paramRect.left = i;
      int j = paramView.getRight();
      paramRect.right = j;
      int k = paramView.getTop();
      paramRect.top = k;
      int m = paramView.getBottom();
      paramRect.bottom = m;
      ViewGroup localViewGroup;
      for (ViewParent localViewParent = paramView.getParent(); ((localViewParent instanceof ViewGroup)) && (localViewParent != this); localViewParent = localViewGroup.getParent())
      {
        localViewGroup = (ViewGroup)localViewParent;
        int n = paramRect.left;
        int i1 = localViewGroup.getLeft();
        int i2 = n + i1;
        paramRect.left = i2;
        int i3 = paramRect.right;
        int i4 = localViewGroup.getRight();
        int i5 = i3 + i4;
        paramRect.right = i5;
        int i6 = paramRect.top;
        int i7 = localViewGroup.getTop();
        int i8 = i6 + i7;
        paramRect.top = i8;
        int i9 = paramRect.bottom;
        int i10 = localViewGroup.getBottom();
        int i11 = i9 + i10;
        paramRect.bottom = i11;
      }
    }
  }

  private int getClientWidth()
  {
    int i = getMeasuredWidth();
    int j = getPaddingLeft();
    int k = i - j;
    int m = getPaddingRight();
    return k - m;
  }

  private ItemInfo infoForCurrentScrollPosition()
  {
    float f1 = 0.0F;
    int i = getClientWidth();
    float f4;
    int j;
    float f7;
    float f8;
    int k;
    Object localObject;
    int m;
    if (i > 0)
    {
      float f2 = getScrollX();
      float f3 = i;
      f4 = f2 / f3;
      if (i > 0)
      {
        float f5 = this.mPageMargin;
        float f6 = i;
        f1 = f5 / f6;
      }
      j = -1;
      f7 = 0.0F;
      f8 = 0.0F;
      k = 1;
      localObject = null;
      m = 0;
    }
    while (true)
    {
      int n = this.mItems.size();
      ItemInfo localItemInfo;
      float f11;
      if (m < n)
      {
        localItemInfo = (ItemInfo)this.mItems.get(m);
        if (k == 0)
        {
          int i1 = localItemInfo.position;
          int i2 = j + 1;
          if (i1 != i2)
          {
            localItemInfo = this.mTempItem;
            float f9 = f7 + f8 + f1;
            localItemInfo.offset = f9;
            int i3 = j + 1;
            localItemInfo.position = i3;
            PagerAdapter localPagerAdapter = this.mAdapter;
            int i4 = localItemInfo.position;
            float f10 = localPagerAdapter.getPageWidth(i4);
            localItemInfo.widthFactor = f10;
            m += -1;
          }
        }
        f11 = localItemInfo.offset;
        float f12 = f11;
        float f13 = localItemInfo.widthFactor + f11 + f1;
        if ((k != 0) || (f4 >= f12))
        {
          if (f4 >= f13)
          {
            int i5 = this.mItems.size() + -1;
            if (m == i5)
              break label273;
          }
          localObject = localItemInfo;
        }
      }
      return localObject;
      f4 = 0.0F;
      break;
      label273: k = 0;
      j = localItemInfo.position;
      f7 = f11;
      f8 = localItemInfo.widthFactor;
      localObject = localItemInfo;
      m += 1;
    }
  }

  private boolean isGutterDrag(float paramFloat1, float paramFloat2)
  {
    float f1 = this.mGutterSize;
    if ((paramFloat1 >= f1) || (paramFloat2 <= 0.0F))
    {
      int i = getWidth();
      int j = this.mGutterSize;
      float f2 = i - j;
      if ((paramFloat1 <= f2) || (paramFloat2 >= 0.0F))
        break label59;
    }
    label59: for (boolean bool = true; ; bool = false)
      return bool;
  }

  private void onSecondaryPointerUp(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getActionIndex(paramMotionEvent);
    int j = MotionEventCompat.getPointerId(paramMotionEvent, i);
    int k = this.mActivePointerId;
    if (j != k)
      return;
    if (i == 0);
    for (int m = 1; ; m = 0)
    {
      float f = MotionEventCompat.getX(paramMotionEvent, m);
      this.mLastMotionX = f;
      int n = MotionEventCompat.getPointerId(paramMotionEvent, m);
      this.mActivePointerId = n;
      if (this.mVelocityTracker == null)
        return;
      this.mVelocityTracker.clear();
      return;
    }
  }

  private boolean pageScrolled(int paramInt)
  {
    boolean bool = false;
    if (this.mItems.size() == 0)
    {
      this.mCalledSuper = false;
      onPageScrolled(0, 0.0F, 0);
      if (!this.mCalledSuper)
        throw new IllegalStateException("onPageScrolled did not call superclass implementation");
    }
    else
    {
      ItemInfo localItemInfo = infoForCurrentScrollPosition();
      int i = getClientWidth();
      int j = this.mPageMargin + i;
      float f1 = this.mPageMargin;
      float f2 = i;
      float f3 = f1 / f2;
      int k = localItemInfo.position;
      float f4 = paramInt;
      float f5 = i;
      float f6 = f4 / f5;
      float f7 = localItemInfo.offset;
      float f8 = f6 - f7;
      float f9 = localItemInfo.widthFactor + f3;
      float f10 = f8 / f9;
      int m = (int)(j * f10);
      this.mCalledSuper = false;
      onPageScrolled(k, f10, m);
      if (!this.mCalledSuper)
        throw new IllegalStateException("onPageScrolled did not call superclass implementation");
      bool = true;
    }
    return bool;
  }

  private boolean performDrag(float paramFloat)
  {
    boolean bool1 = false;
    float f1 = this.mLastMotionX - paramFloat;
    float f2 = paramFloat;
    this.mLastMotionX = f2;
    float f3 = getScrollX() + f1;
    int i = getClientWidth();
    float f4 = i;
    float f5 = this.mFirstOffset;
    float f6 = f4 * f5;
    float f7 = i;
    float f8 = this.mLastOffset;
    float f9 = f7 * f8;
    int j = 1;
    int k = 1;
    ItemInfo localItemInfo1 = (ItemInfo)this.mItems.get(0);
    ArrayList localArrayList = this.mItems;
    int m = this.mItems.size() + -1;
    ItemInfo localItemInfo2 = (ItemInfo)localArrayList.get(m);
    if (localItemInfo1.position != 0)
    {
      j = 0;
      float f10 = localItemInfo1.offset;
      float f11 = i;
      f6 = f10 * f11;
    }
    int n = localItemInfo2.position;
    int i1 = this.mAdapter.getCount() + -1;
    if (n != i1)
    {
      k = 0;
      float f12 = localItemInfo2.offset;
      float f13 = i;
      f9 = f12 * f13;
    }
    if (f3 < f6)
    {
      if (j != 0)
      {
        float f14 = f6 - f3;
        EdgeEffectCompat localEdgeEffectCompat1 = this.mLeftEdge;
        float f15 = Math.abs(f14);
        float f16 = i;
        float f17 = f15 / f16;
        bool1 = localEdgeEffectCompat1.onPull(f17);
      }
      f3 = f6;
    }
    while (true)
    {
      float f18 = this.mLastMotionX;
      float f19 = (int)f3;
      float f20 = f3 - f19;
      float f21 = f18 + f20;
      this.mLastMotionX = f21;
      int i2 = (int)f3;
      int i3 = getScrollY();
      scrollTo(i2, i3);
      int i4 = (int)f3;
      boolean bool2 = pageScrolled(i4);
      return bool1;
      if (f3 > f9)
      {
        if (k != 0)
        {
          float f22 = f3 - f9;
          EdgeEffectCompat localEdgeEffectCompat2 = this.mRightEdge;
          float f23 = Math.abs(f22);
          float f24 = i;
          float f25 = f23 / f24;
          bool1 = localEdgeEffectCompat2.onPull(f25);
        }
        f3 = f9;
      }
    }
  }

  private void recomputeScrollPosition(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if ((paramInt2 > 0) && (!this.mItems.isEmpty()))
    {
      int i = getPaddingLeft();
      int j = paramInt1 - i;
      int k = getPaddingRight();
      int m = j - k + paramInt3;
      int n = getPaddingLeft();
      int i1 = paramInt2 - n;
      int i2 = getPaddingRight();
      int i3 = i1 - i2 + paramInt4;
      float f1 = getScrollX();
      float f2 = i3;
      float f3 = f1 / f2;
      int i4 = (int)(m * f3);
      int i5 = getScrollY();
      scrollTo(i4, i5);
      if (this.mScroller.isFinished())
        return;
      int i6 = this.mScroller.getDuration();
      int i7 = this.mScroller.timePassed();
      int i8 = i6 - i7;
      int i9 = this.mCurItem;
      ItemInfo localItemInfo1 = infoForPosition(i9);
      Scroller localScroller = this.mScroller;
      float f4 = localItemInfo1.offset;
      float f5 = paramInt1;
      int i10 = (int)(f4 * f5);
      localScroller.startScroll(i4, 0, i10, 0, i8);
      return;
    }
    int i11 = this.mCurItem;
    ItemInfo localItemInfo2 = infoForPosition(i11);
    float f6;
    float f7;
    if (localItemInfo2 != null)
    {
      f6 = localItemInfo2.offset;
      f7 = this.mLastOffset;
    }
    for (float f8 = Math.min(f6, f7); ; f8 = 0.0F)
    {
      int i12 = getPaddingLeft();
      int i13 = paramInt1 - i12;
      int i14 = getPaddingRight();
      int i15 = (int)((i13 - i14) * f8);
      int i16 = getScrollX();
      if (i15 != i16)
        return;
      completeScroll(false);
      int i17 = getScrollY();
      scrollTo(i15, i17);
      return;
    }
  }

  private void removeNonDecorViews()
  {
    int i = 0;
    while (true)
    {
      int j = getChildCount();
      if (i >= j)
        return;
      if (!((LayoutParams)getChildAt(i).getLayoutParams()).isDecor)
      {
        removeViewAt(i);
        i += -1;
      }
      i += 1;
    }
  }

  private void scrollToItem(int paramInt1, boolean paramBoolean1, int paramInt2, boolean paramBoolean2)
  {
    ItemInfo localItemInfo = infoForPosition(paramInt1);
    int i = 0;
    if (localItemInfo != null)
    {
      float f1 = getClientWidth();
      float f2 = this.mFirstOffset;
      float f3 = localItemInfo.offset;
      float f4 = this.mLastOffset;
      float f5 = Math.min(f3, f4);
      float f6 = Math.max(f2, f5);
      i = (int)(f1 * f6);
    }
    if (paramBoolean1)
    {
      smoothScrollTo(i, 0, paramInt2);
      if ((paramBoolean2) && (this.mOnPageChangeListener != null))
        this.mOnPageChangeListener.onPageSelected(paramInt1);
      if (!paramBoolean2)
        return;
      if (this.mInternalPageChangeListener == null)
        return;
      this.mInternalPageChangeListener.onPageSelected(paramInt1);
      return;
    }
    if ((paramBoolean2) && (this.mOnPageChangeListener != null))
      this.mOnPageChangeListener.onPageSelected(paramInt1);
    if ((paramBoolean2) && (this.mInternalPageChangeListener != null))
      this.mInternalPageChangeListener.onPageSelected(paramInt1);
    completeScroll(false);
    scrollTo(i, 0);
    boolean bool = pageScrolled(i);
  }

  private void setScrollState(int paramInt)
  {
    if (this.mScrollState != paramInt)
      return;
    this.mScrollState = paramInt;
    if (this.mPageTransformer != null)
      if (paramInt == 0)
        break label51;
    label51: for (boolean bool = true; ; bool = false)
    {
      enableLayers(bool);
      if (this.mOnPageChangeListener == null)
        return;
      this.mOnPageChangeListener.onPageScrollStateChanged(paramInt);
      return;
    }
  }

  private void setScrollingCacheEnabled(boolean paramBoolean)
  {
    if (this.mScrollingCacheEnabled != paramBoolean)
      return;
    this.mScrollingCacheEnabled = paramBoolean;
  }

  private void sortChildDrawingOrder()
  {
    if (this.mDrawingOrder == 0)
      return;
    if (this.mDrawingOrderedChildren == null)
    {
      ArrayList localArrayList1 = new ArrayList();
      this.mDrawingOrderedChildren = localArrayList1;
    }
    while (true)
    {
      int i = getChildCount();
      int j = 0;
      while (j < i)
      {
        View localView = getChildAt(j);
        boolean bool = this.mDrawingOrderedChildren.add(localView);
        j += 1;
      }
      this.mDrawingOrderedChildren.clear();
    }
    ArrayList localArrayList2 = this.mDrawingOrderedChildren;
    ViewPositionComparator localViewPositionComparator = sPositionComparator;
    Collections.sort(localArrayList2, localViewPositionComparator);
  }

  public void addFocusables(ArrayList<View> paramArrayList, int paramInt1, int paramInt2)
  {
    int i = paramArrayList.size();
    int j = getDescendantFocusability();
    if (j != 393216)
    {
      int k = 0;
      while (true)
      {
        int m = getChildCount();
        if (k >= m)
          break;
        View localView = getChildAt(k);
        if (localView.getVisibility() == 0)
        {
          ItemInfo localItemInfo = infoForChild(localView);
          if (localItemInfo != null)
          {
            int n = localItemInfo.position;
            int i1 = this.mCurItem;
            if (n != i1)
              localView.addFocusables(paramArrayList, paramInt1, paramInt2);
          }
        }
        k += 1;
      }
    }
    if (j == 262144)
    {
      int i2 = paramArrayList.size();
      if (i != i2)
        return;
    }
    if (!isFocusable())
      return;
    if (((paramInt2 & 0x1) == 1) && (isInTouchMode()) && (!isFocusableInTouchMode()))
      return;
    if (paramArrayList == null)
      return;
    boolean bool = paramArrayList.add(this);
  }

  ItemInfo addNewItem(int paramInt1, int paramInt2)
  {
    ItemInfo localItemInfo = new ItemInfo();
    localItemInfo.position = paramInt1;
    Object localObject = this.mAdapter.instantiateItem(this, paramInt1);
    localItemInfo.object = localObject;
    float f = this.mAdapter.getPageWidth(paramInt1);
    localItemInfo.widthFactor = f;
    if (paramInt2 >= 0)
    {
      int i = this.mItems.size();
      if (paramInt2 < i);
    }
    else
    {
      boolean bool = this.mItems.add(localItemInfo);
    }
    while (true)
    {
      return localItemInfo;
      this.mItems.add(paramInt2, localItemInfo);
    }
  }

  public void addTouchables(ArrayList<View> paramArrayList)
  {
    int i = 0;
    while (true)
    {
      int j = getChildCount();
      if (i >= j)
        return;
      View localView = getChildAt(i);
      if (localView.getVisibility() == 0)
      {
        ItemInfo localItemInfo = infoForChild(localView);
        if (localItemInfo != null)
        {
          int k = localItemInfo.position;
          int m = this.mCurItem;
          if (k != m)
            localView.addTouchables(paramArrayList);
        }
      }
      i += 1;
    }
  }

  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    if (!checkLayoutParams(paramLayoutParams))
      paramLayoutParams = generateLayoutParams(paramLayoutParams);
    LayoutParams localLayoutParams = (LayoutParams)paramLayoutParams;
    boolean bool1 = localLayoutParams.isDecor;
    boolean bool2 = paramView instanceof Decor;
    boolean bool3 = bool1 | bool2;
    localLayoutParams.isDecor = bool3;
    if (this.mInLayout)
    {
      if ((localLayoutParams != null) && (localLayoutParams.isDecor))
        throw new IllegalStateException("Cannot add pager decor view during layout");
      localLayoutParams.needsMeasure = true;
      boolean bool4 = addViewInLayout(paramView, paramInt, paramLayoutParams);
      return;
    }
    super.addView(paramView, paramInt, paramLayoutParams);
  }

  public boolean arrowScroll(int paramInt)
  {
    int i = 0;
    Object localObject1 = findFocus();
    Object localObject2;
    View localView;
    boolean bool;
    label97: ViewParent localViewParent1;
    if (localObject1 == this)
    {
      localObject2 = null;
      localView = FocusFinder.getInstance().findNextFocus(this, (View)localObject2, paramInt);
      if ((localView == null) || (localView == localObject2))
        break label374;
      if (paramInt != 17)
        break label303;
      Rect localRect1 = this.mTempRect;
      localObject1 = getChildRectInPagerCoordinates(localRect1, localView).left;
      Rect localRect2 = this.mTempRect;
      i = getChildRectInPagerCoordinates(localRect2, (View)localObject2).left;
      if ((localObject2 != null) && (localObject1 >= i))
      {
        bool = pageLeft();
        if (bool)
        {
          int k = SoundEffectConstants.getContantForFocusDirection(paramInt);
          playSoundEffect(k);
        }
        return bool;
      }
    }
    else
    {
      if (localObject1 == null)
        break label420;
      localViewParent1 = ((View)localObject1).getParent();
      if (!(localViewParent1 instanceof ViewGroup))
        break label426;
      if (localViewParent1 != this);
    }
    label420: label426: for (int m = 1; ; m = 0)
    {
      label303: int j;
      if (m == 0)
      {
        StringBuilder localStringBuilder1 = new StringBuilder();
        String str1 = localObject1.getClass().getSimpleName();
        StringBuilder localStringBuilder2 = localStringBuilder1.append(str1);
        ViewParent localViewParent2 = ((View)localObject1).getParent();
        while (true)
          if ((localViewParent2 instanceof ViewGroup))
          {
            StringBuilder localStringBuilder3 = localStringBuilder1.append(" => ");
            String str2 = localViewParent2.getClass().getSimpleName();
            StringBuilder localStringBuilder4 = localStringBuilder3.append(str2);
            localViewParent2 = localViewParent2.getParent();
            continue;
            localViewParent1 = localViewParent1.getParent();
            break;
          }
        StringBuilder localStringBuilder5 = new StringBuilder().append("arrowScroll tried to find focus based on non-child current focused view ");
        String str3 = localStringBuilder1.toString();
        String str4 = str3;
        int n = Log.e("ViewPager", str4);
        localObject2 = null;
        break;
        bool = localView.requestFocus();
        break label97;
        if (paramInt == 66)
        {
          Rect localRect3 = this.mTempRect;
          j = getChildRectInPagerCoordinates(localRect3, localView).left;
          Rect localRect4 = this.mTempRect;
          i = getChildRectInPagerCoordinates(localRect4, (View)localObject2).left;
          if ((localObject2 != null) && (j <= i))
          {
            bool = pageRight();
            break label97;
          }
          bool = localView.requestFocus();
          break label97;
          label374: if ((paramInt == 17) || (paramInt == 1))
          {
            bool = pageLeft();
            break label97;
          }
          if ((paramInt == 66) || (paramInt == 2))
          {
            bool = pageRight();
            break label97;
          }
        }
        bool = false;
        break label97;
      }
      localObject2 = j;
      break;
    }
  }

  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    int k;
    boolean bool;
    if ((paramView instanceof ViewGroup))
    {
      ViewGroup localViewGroup = (ViewGroup)paramView;
      int i = paramView.getScrollX();
      int j = paramView.getScrollY();
      k = localViewGroup.getChildCount() + -1;
      if (k >= 0)
      {
        View localView = localViewGroup.getChildAt(k);
        int m = paramInt2 + i;
        int n = localView.getLeft();
        if (m >= n)
        {
          int i1 = paramInt2 + i;
          int i2 = localView.getRight();
          if (i1 < i2)
          {
            int i3 = paramInt3 + j;
            int i4 = localView.getTop();
            if (i3 >= i4)
            {
              int i5 = paramInt3 + j;
              int i6 = localView.getBottom();
              if (i5 < i6)
              {
                int i7 = paramInt2 + i;
                int i8 = localView.getLeft();
                int i9 = i7 - i8;
                int i10 = paramInt3 + j;
                int i11 = localView.getTop();
                int i12 = i10 - i11;
                ViewPager localViewPager = this;
                int i13 = paramInt1;
                if (localViewPager.canScroll(localView, true, i13, i9, i12))
                  bool = true;
              }
            }
          }
        }
      }
    }
    while (true)
    {
      return bool;
      k += -1;
      break;
      if (paramBoolean)
      {
        int i14 = -paramInt1;
        if (ViewCompat.canScrollHorizontally(paramView, i14))
          bool = true;
      }
      else
      {
        bool = false;
      }
    }
  }

  public boolean canScrollHorizontally(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2 = false;
    if (this.mAdapter == null);
    int i;
    int j;
    do
    {
      return bool2;
      i = getClientWidth();
      j = getScrollX();
      if (paramInt < 0)
      {
        float f1 = i;
        float f2 = this.mFirstOffset;
        int k = (int)(f1 * f2);
        if (j > k);
        while (true)
        {
          bool2 = bool1;
          break;
          bool1 = false;
        }
      }
    }
    while (paramInt <= 0);
    float f3 = i;
    float f4 = this.mLastOffset;
    int m = (int)(f3 * f4);
    if (j < m);
    while (true)
    {
      bool2 = bool1;
      break;
      bool1 = false;
    }
  }

  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if (((paramLayoutParams instanceof LayoutParams)) && (super.checkLayoutParams(paramLayoutParams)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void computeScroll()
  {
    if ((!this.mScroller.isFinished()) && (this.mScroller.computeScrollOffset()))
    {
      int i = getScrollX();
      int j = getScrollY();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      if ((i == k) || (j != m))
      {
        scrollTo(k, m);
        if (!pageScrolled(k))
        {
          this.mScroller.abortAnimation();
          scrollTo(0, m);
        }
      }
      ViewCompat.postInvalidateOnAnimation(this);
      return;
    }
    completeScroll(true);
  }

  void dataSetChanged()
  {
    int i = this.mAdapter.getCount();
    this.mExpectedAdapterCount = i;
    int j = this.mItems.size();
    int k = this.mOffscreenPageLimit * 2 + 1;
    int m;
    int n;
    int i1;
    label61: ItemInfo localItemInfo;
    int i4;
    if ((j < k) && (this.mItems.size() < i))
    {
      m = 1;
      n = this.mCurItem;
      i1 = 0;
      i2 = 0;
      int i3 = this.mItems.size();
      if (i2 >= i3)
        break label311;
      localItemInfo = (ItemInfo)this.mItems.get(i2);
      PagerAdapter localPagerAdapter1 = this.mAdapter;
      Object localObject1 = localItemInfo.object;
      i4 = localPagerAdapter1.getItemPosition(localObject1);
      if (i4 != -1)
        break label135;
    }
    while (true)
    {
      i2 += 1;
      break label61;
      m = 0;
      break;
      label135: if (i4 == -1)
      {
        Object localObject2 = this.mItems.remove(i2);
        i2 += -1;
        if (i1 == 0)
        {
          this.mAdapter.startUpdate(this);
          i1 = 1;
        }
        PagerAdapter localPagerAdapter2 = this.mAdapter;
        int i5 = localItemInfo.position;
        Object localObject3 = localItemInfo.object;
        localPagerAdapter2.destroyItem(this, i5, localObject3);
        m = 1;
        int i6 = this.mCurItem;
        int i7 = localItemInfo.position;
        if (i6 != i7)
        {
          int i8 = this.mCurItem;
          int i9 = i + -1;
          int i10 = Math.min(i8, i9);
          n = Math.max(0, i10);
          m = 1;
        }
      }
      else if (localItemInfo.position != i4)
      {
        int i11 = localItemInfo.position;
        int i12 = this.mCurItem;
        if (i11 != i12)
          n = i4;
        localItemInfo.position = i4;
        m = 1;
      }
    }
    label311: if (i1 != 0)
      this.mAdapter.finishUpdate(this);
    ArrayList localArrayList = this.mItems;
    Comparator localComparator = COMPARATOR;
    Collections.sort(localArrayList, localComparator);
    if (m == 0)
      return;
    int i13 = getChildCount();
    int i2 = 0;
    while (i2 < i13)
    {
      LayoutParams localLayoutParams = (LayoutParams)getChildAt(i2).getLayoutParams();
      if (!localLayoutParams.isDecor)
        localLayoutParams.widthFactor = 0.0F;
      int i14 = i2 + 1;
    }
    setCurrentItemInternal(n, false, true);
    requestLayout();
  }

  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    if ((super.dispatchKeyEvent(paramKeyEvent)) || (executeKeyEvent(paramKeyEvent)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    boolean bool;
    if (paramAccessibilityEvent.getEventType() == 4096)
      bool = super.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent);
    while (true)
    {
      return bool;
      int i = getChildCount();
      int j = 0;
      while (true)
      {
        if (j >= i)
          break label104;
        View localView = getChildAt(j);
        if (localView.getVisibility() == 0)
        {
          ItemInfo localItemInfo = infoForChild(localView);
          if (localItemInfo != null)
          {
            int k = localItemInfo.position;
            int m = this.mCurItem;
            if ((k != m) && (localView.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent)))
            {
              bool = true;
              break;
            }
          }
        }
        j += 1;
      }
      label104: bool = false;
    }
  }

  float distanceInfluenceForSnapDuration(float paramFloat)
  {
    return (float)Math.sin((float)((paramFloat - 0.5F) * 0.47123891676382D));
  }

  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    boolean bool1 = false;
    int i = ViewCompat.getOverScrollMode(this);
    if ((i == 0) || ((i == 1) && (this.mAdapter != null) && (this.mAdapter.getCount() > 1)))
    {
      if (!this.mLeftEdge.isFinished())
      {
        int j = paramCanvas.save();
        int k = getHeight();
        int m = getPaddingTop();
        int n = k - m;
        int i1 = getPaddingBottom();
        int i2 = n - i1;
        int i3 = getWidth();
        paramCanvas.rotate(270.0F);
        int i4 = -i2;
        int i5 = getPaddingTop();
        float f1 = i4 + i5;
        float f2 = this.mFirstOffset;
        float f3 = i3;
        float f4 = f2 * f3;
        paramCanvas.translate(f1, f4);
        this.mLeftEdge.setSize(i2, i3);
        boolean bool2 = this.mLeftEdge.draw(paramCanvas);
        bool1 = false | bool2;
        paramCanvas.restoreToCount(j);
      }
      if (!this.mRightEdge.isFinished())
      {
        int i6 = paramCanvas.save();
        int i7 = getWidth();
        int i8 = getHeight();
        int i9 = getPaddingTop();
        int i10 = i8 - i9;
        int i11 = getPaddingBottom();
        int i12 = i10 - i11;
        paramCanvas.rotate(90.0F);
        float f5 = -getPaddingTop();
        float f6 = -(this.mLastOffset + 1.0F);
        float f7 = i7;
        float f8 = f6 * f7;
        paramCanvas.translate(f5, f8);
        this.mRightEdge.setSize(i12, i7);
        boolean bool3 = this.mRightEdge.draw(paramCanvas);
        bool1 |= bool3;
        paramCanvas.restoreToCount(i6);
      }
    }
    while (true)
    {
      if (!bool1)
        return;
      ViewCompat.postInvalidateOnAnimation(this);
      return;
      this.mLeftEdge.finish();
      this.mRightEdge.finish();
    }
  }

  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    Drawable localDrawable = this.mMarginDrawable;
    if (localDrawable == null)
      return;
    if (!localDrawable.isStateful())
      return;
    int[] arrayOfInt = getDrawableState();
    boolean bool = localDrawable.setState(arrayOfInt);
  }

  public boolean executeKeyEvent(KeyEvent paramKeyEvent)
  {
    boolean bool = false;
    if (paramKeyEvent.getAction() == 0)
      switch (paramKeyEvent.getKeyCode())
      {
      default:
      case 21:
      case 22:
      case 61:
      }
    while (true)
    {
      return bool;
      bool = arrowScroll(17);
      continue;
      bool = arrowScroll(66);
      continue;
      if (Build.VERSION.SDK_INT >= 11)
        if (KeyEventCompat.hasNoModifiers(paramKeyEvent))
          bool = arrowScroll(2);
        else if (KeyEventCompat.hasModifiers(paramKeyEvent, 1))
          bool = arrowScroll(1);
    }
  }

  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams();
  }

  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    Context localContext = getContext();
    return new LayoutParams(localContext, paramAttributeSet);
  }

  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return generateDefaultLayoutParams();
  }

  public PagerAdapter getAdapter()
  {
    return this.mAdapter;
  }

  protected int getChildDrawingOrder(int paramInt1, int paramInt2)
  {
    if (this.mDrawingOrder == 2);
    for (int i = paramInt1 + -1 - paramInt2; ; i = paramInt2)
      return ((LayoutParams)((View)this.mDrawingOrderedChildren.get(i)).getLayoutParams()).childIndex;
  }

  public int getCurrentItem()
  {
    return this.mCurItem;
  }

  ItemInfo infoForAnyChild(View paramView)
  {
    ViewParent localViewParent = paramView.getParent();
    if (localViewParent != this)
      if ((localViewParent != null) && ((localViewParent instanceof View)));
    for (ItemInfo localItemInfo = null; ; localItemInfo = infoForChild(paramView))
    {
      return localItemInfo;
      paramView = (View)localViewParent;
      break;
    }
  }

  ItemInfo infoForChild(View paramView)
  {
    int i = 0;
    int j = this.mItems.size();
    ItemInfo localItemInfo;
    if (i < j)
    {
      localItemInfo = (ItemInfo)this.mItems.get(i);
      PagerAdapter localPagerAdapter = this.mAdapter;
      Object localObject = localItemInfo.object;
      if (!localPagerAdapter.isViewFromObject(paramView, localObject));
    }
    while (true)
    {
      return localItemInfo;
      i += 1;
      break;
      localItemInfo = null;
    }
  }

  ItemInfo infoForPosition(int paramInt)
  {
    int i = 0;
    int j = this.mItems.size();
    ItemInfo localItemInfo;
    if (i < j)
    {
      localItemInfo = (ItemInfo)this.mItems.get(i);
      if (localItemInfo.position == paramInt);
    }
    while (true)
    {
      return localItemInfo;
      i += 1;
      break;
      localItemInfo = null;
    }
  }

  void initViewPager()
  {
    setWillNotDraw(false);
    setDescendantFocusability(262144);
    setFocusable(true);
    Context localContext = getContext();
    Interpolator localInterpolator = sInterpolator;
    Scroller localScroller = new Scroller(localContext, localInterpolator);
    this.mScroller = localScroller;
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(localContext);
    float f = localContext.getResources().getDisplayMetrics().density;
    int i = ViewConfigurationCompat.getScaledPagingTouchSlop(localViewConfiguration);
    this.mTouchSlop = i;
    int j = (int)(400.0F * f);
    this.mMinimumVelocity = j;
    int k = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.mMaximumVelocity = k;
    EdgeEffectCompat localEdgeEffectCompat1 = new EdgeEffectCompat(localContext);
    this.mLeftEdge = localEdgeEffectCompat1;
    EdgeEffectCompat localEdgeEffectCompat2 = new EdgeEffectCompat(localContext);
    this.mRightEdge = localEdgeEffectCompat2;
    int m = (int)(25.0F * f);
    this.mFlingDistance = m;
    int n = (int)(2.0F * f);
    this.mCloseEnough = n;
    int i1 = (int)(16.0F * f);
    this.mDefaultGutterSize = i1;
    MyAccessibilityDelegate localMyAccessibilityDelegate = new MyAccessibilityDelegate();
    ViewCompat.setAccessibilityDelegate(this, localMyAccessibilityDelegate);
    if (ViewCompat.getImportantForAccessibility(this) != 0)
      return;
    ViewCompat.setImportantForAccessibility(this, 1);
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }

  protected void onDetachedFromWindow()
  {
    Runnable localRunnable = this.mEndScrollRunnable;
    boolean bool = removeCallbacks(localRunnable);
    super.onDetachedFromWindow();
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (this.mPageMargin <= 0)
      return;
    if (this.mMarginDrawable == null)
      return;
    if (this.mItems.size() <= 0)
      return;
    if (this.mAdapter == null)
      return;
    int i = getScrollX();
    int j = getWidth();
    float f1 = this.mPageMargin;
    float f2 = j;
    float f3 = f1 / f2;
    int k = 0;
    ItemInfo localItemInfo = (ItemInfo)this.mItems.get(0);
    float f4 = localItemInfo.offset;
    int m = this.mItems.size();
    int n = localItemInfo.position;
    ArrayList localArrayList1 = this.mItems;
    int i1 = m + -1;
    int i2 = ((ItemInfo)localArrayList1.get(i1)).position;
    int i3 = n;
    while (true)
    {
      if (i3 >= i2)
        return;
      while (true)
      {
        int i4 = localItemInfo.position;
        if ((i3 <= i4) || (k >= m))
          break;
        ArrayList localArrayList2 = this.mItems;
        k += 1;
        localItemInfo = (ItemInfo)localArrayList2.get(k);
      }
      int i5 = localItemInfo.position;
      float f9;
      float f10;
      float f11;
      if (i3 != i5)
      {
        float f5 = localItemInfo.offset;
        float f6 = localItemInfo.widthFactor;
        float f7 = f5 + f6;
        float f8 = j;
        f9 = f7 * f8;
        f10 = localItemInfo.offset;
        f11 = localItemInfo.widthFactor;
      }
      float f18;
      for (f4 = f10 + f11 + f3; ; f4 += f18)
      {
        float f12 = this.mPageMargin + f9;
        float f13 = i;
        if (f12 > f13)
        {
          Drawable localDrawable1 = this.mMarginDrawable;
          int i6 = (int)f9;
          int i7 = this.mTopPageBounds;
          int i8 = (int)(this.mPageMargin + f9 + 0.5F);
          int i9 = this.mBottomPageBounds;
          localDrawable1.setBounds(i6, i7, i8, i9);
          Drawable localDrawable2 = this.mMarginDrawable;
          Canvas localCanvas = paramCanvas;
          localDrawable2.draw(localCanvas);
        }
        float f14 = i + j;
        if (f9 <= f14)
          break;
        return;
        float f15 = this.mAdapter.getPageWidth(i3);
        float f16 = f4 + f15;
        float f17 = j;
        f9 = f16 * f17;
        f18 = f15 + f3;
      }
      i3 += 1;
    }
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction() & 0xFF;
    boolean bool1;
    if ((i == 3) || (i == 1))
    {
      this.mIsBeingDragged = false;
      this.mIsUnableToDrag = false;
      this.mActivePointerId = -1;
      if (this.mVelocityTracker != null)
      {
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
      }
      bool1 = false;
    }
    while (true)
    {
      return bool1;
      if (i == 0)
        break;
      if (this.mIsBeingDragged)
      {
        bool1 = true;
      }
      else
      {
        if (!this.mIsUnableToDrag)
          break;
        bool1 = false;
      }
    }
    switch (i)
    {
    default:
    case 2:
    case 0:
    case 6:
    }
    while (true)
    {
      if (this.mVelocityTracker == null)
      {
        VelocityTracker localVelocityTracker = VelocityTracker.obtain();
        this.mVelocityTracker = localVelocityTracker;
      }
      this.mVelocityTracker.addMovement(paramMotionEvent);
      bool1 = this.mIsBeingDragged;
      break;
      int j = this.mActivePointerId;
      if (j != -1)
      {
        int k = MotionEventCompat.findPointerIndex(paramMotionEvent, j);
        float f1 = MotionEventCompat.getX(paramMotionEvent, k);
        float f2 = this.mLastMotionX;
        float f3 = f1 - f2;
        float f4 = Math.abs(f3);
        float f5 = MotionEventCompat.getY(paramMotionEvent, k);
        float f6 = this.mInitialMotionY;
        float f7 = Math.abs(f5 - f6);
        if (f3 != 0.0F)
        {
          float f8 = this.mLastMotionX;
          float f9;
          float f10;
          float f11;
          float f12;
          label377: float f16;
          float f17;
          int i2;
          boolean bool2;
          int i3;
          int i4;
          int i5;
          int i6;
          if (!isGutterDrag(f8, f3))
          {
            int m = (int)f3;
            int n = (int)f1;
            int i1 = (int)f5;
            ViewPager localViewPager1 = this;
            ViewPager localViewPager2 = this;
            if (localViewPager1.canScroll(localViewPager2, false, m, n, i1))
            {
              this.mLastMotionX = f1;
              this.mLastMotionY = f5;
              this.mIsUnableToDrag = true;
              bool1 = false;
              break;
            }
          }
        }
        f9 = this.mTouchSlop;
        if ((f4 > f9) && (0.5F * f4 > f7))
        {
          this.mIsBeingDragged = true;
          setScrollState(1);
          if (f3 > 0.0F)
          {
            f10 = this.mInitialMotionX;
            f11 = this.mTouchSlop;
            f12 = f10 + f11;
            this.mLastMotionX = f12;
            this.mLastMotionY = f5;
            setScrollingCacheEnabled(true);
          }
        }
        while ((this.mIsBeingDragged) && (performDrag(f1)))
        {
          ViewCompat.postInvalidateOnAnimation(this);
          break;
          float f13 = this.mInitialMotionX;
          float f14 = this.mTouchSlop;
          f12 = f13 - f14;
          break label377;
          float f15 = this.mTouchSlop;
          if (f7 > f15)
            this.mIsUnableToDrag = true;
        }
        f16 = paramMotionEvent.getX();
        this.mInitialMotionX = f16;
        this.mLastMotionX = f16;
        f17 = paramMotionEvent.getY();
        this.mInitialMotionY = f17;
        this.mLastMotionY = f17;
        i2 = MotionEventCompat.getPointerId(paramMotionEvent, 0);
        this.mActivePointerId = i2;
        this.mIsUnableToDrag = false;
        bool2 = this.mScroller.computeScrollOffset();
        if (this.mScrollState == 2)
        {
          i3 = this.mScroller.getFinalX();
          i4 = this.mScroller.getCurrX();
          i5 = Math.abs(i3 - i4);
          i6 = this.mCloseEnough;
          if (i5 > i6)
          {
            this.mScroller.abortAnimation();
            this.mPopulatePending = false;
            populate();
            this.mIsBeingDragged = true;
            setScrollState(1);
          }
        }
        else
        {
          completeScroll(false);
          this.mIsBeingDragged = false;
          continue;
          onSecondaryPointerUp(paramMotionEvent);
        }
      }
    }
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getChildCount();
    int j = paramInt3 - paramInt1;
    int k = paramInt4 - paramInt2;
    int m = getPaddingLeft();
    int n = getPaddingTop();
    int i1 = getPaddingRight();
    int i2 = getPaddingBottom();
    int i3 = getScrollX();
    int i4 = 0;
    int i5 = 0;
    if (i5 < i)
    {
      View localView1 = getChildAt(i5);
      int i6 = localView1.getVisibility();
      int i7 = 8;
      int i10;
      label164: int i11;
      if (i6 != i7)
      {
        LayoutParams localLayoutParams1 = (LayoutParams)localView1.getLayoutParams();
        if (localLayoutParams1.isDecor)
        {
          int i8 = localLayoutParams1.gravity & 0x7;
          int i9 = localLayoutParams1.gravity & 0x70;
          switch (i8)
          {
          case 2:
          case 4:
          default:
            i10 = m;
            switch (i9)
            {
            default:
              i11 = n;
            case 48:
            case 16:
            case 80:
            }
            break;
          case 3:
          case 1:
          case 5:
          }
        }
      }
      while (true)
      {
        int i12 = i10 + i3;
        int i13 = localView1.getMeasuredWidth() + i12;
        int i14 = localView1.getMeasuredHeight() + i11;
        int i15 = i13;
        int i16 = i14;
        localView1.layout(i12, i11, i15, i16);
        i4 += 1;
        i5 += 1;
        break;
        i10 = m;
        int i17 = localView1.getMeasuredWidth();
        m += i17;
        break label164;
        int i18 = localView1.getMeasuredWidth();
        int i19 = (j - i18) / 2;
        int i20 = m;
        i10 = Math.max(i19, i20);
        break label164;
        int i21 = j - i1;
        int i22 = localView1.getMeasuredWidth();
        i10 = i21 - i22;
        int i23 = localView1.getMeasuredWidth();
        i1 += i23;
        break label164;
        i11 = n;
        int i24 = localView1.getMeasuredHeight();
        n += i24;
        continue;
        int i25 = localView1.getMeasuredHeight();
        int i26 = (k - i25) / 2;
        int i27 = n;
        i11 = Math.max(i26, i27);
        continue;
        int i28 = k - i2;
        int i29 = localView1.getMeasuredHeight();
        i11 = i28 - i29;
        int i30 = localView1.getMeasuredHeight();
        i2 += i30;
      }
    }
    int i31 = j - m - i1;
    int i32 = 0;
    while (i32 < i)
    {
      View localView2 = getChildAt(i32);
      int i33 = localView2.getVisibility();
      int i34 = 8;
      if (i33 != i34)
      {
        LayoutParams localLayoutParams2 = (LayoutParams)localView2.getLayoutParams();
        if (!localLayoutParams2.isDecor)
        {
          ItemInfo localItemInfo = infoForChild(localView2);
          if (localItemInfo != null)
          {
            float f1 = i31;
            float f2 = localItemInfo.offset;
            int i35 = (int)(f1 * f2);
            int i36 = m + i35;
            int i37 = n;
            if (localLayoutParams2.needsMeasure)
            {
              boolean bool1 = false;
              localLayoutParams2.needsMeasure = bool1;
              float f3 = i31;
              float f4 = localLayoutParams2.widthFactor;
              int i38 = View.MeasureSpec.makeMeasureSpec((int)(f3 * f4), 1073741824);
              int i39 = View.MeasureSpec.makeMeasureSpec(k - n - i2, 1073741824);
              int i40 = i38;
              localView2.measure(i40, i39);
            }
            int i41 = localView2.getMeasuredWidth() + i36;
            int i42 = localView2.getMeasuredHeight() + i37;
            int i43 = i41;
            int i44 = i42;
            localView2.layout(i36, i37, i43, i44);
          }
        }
      }
      i32 += 1;
    }
    int i45 = n;
    this.mTopPageBounds = i45;
    int i46 = k - i2;
    this.mBottomPageBounds = i46;
    this.mDecorChildCount = i4;
    if (this.mFirstLayout)
    {
      int i47 = this.mCurItem;
      ViewPager localViewPager = this;
      int i48 = i47;
      boolean bool2 = false;
      int i49 = 0;
      boolean bool3 = false;
      localViewPager.scrollToItem(i48, bool2, i49, bool3);
    }
    boolean bool4 = false;
    this.mFirstLayout = bool4;
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = 0;
    int j = paramInt1;
    int k = getDefaultSize(i, j);
    int m = 0;
    int n = paramInt2;
    int i1 = getDefaultSize(m, n);
    ViewPager localViewPager = this;
    int i2 = k;
    int i3 = i1;
    localViewPager.setMeasuredDimension(i2, i3);
    int i4 = getMeasuredWidth();
    int i5 = i4 / 10;
    int i6 = this.mDefaultGutterSize;
    int i7 = Math.min(i5, i6);
    this.mGutterSize = i7;
    int i8 = getPaddingLeft();
    int i9 = i4 - i8;
    int i10 = getPaddingRight();
    int i11 = i9 - i10;
    int i12 = getMeasuredHeight();
    int i13 = getPaddingTop();
    int i14 = i12 - i13;
    int i15 = getPaddingBottom();
    int i16 = i14 - i15;
    int i17 = getChildCount();
    int i18 = 0;
    int i19 = i17;
    if (i18 < i19)
    {
      View localView1 = getChildAt(i18);
      int i20 = localView1.getVisibility();
      int i21 = 8;
      int i25;
      int i30;
      label272: int i33;
      if (i20 != i21)
      {
        LayoutParams localLayoutParams1 = (LayoutParams)localView1.getLayoutParams();
        if ((localLayoutParams1 != null) && (localLayoutParams1.isDecor))
        {
          int i22 = localLayoutParams1.gravity & 0x7;
          int i23 = localLayoutParams1.gravity & 0x70;
          int i24 = -2147483648;
          i25 = -2147483648;
          int i26 = i23;
          int i27 = 48;
          if (i26 != i27)
          {
            int i28 = i23;
            int i29 = 80;
            if (i28 != i29)
              break label480;
          }
          i30 = 1;
          int i31 = 3;
          if (i22 != i31)
          {
            int i32 = 5;
            if (i22 != i32)
              break label486;
          }
          i33 = 1;
          label295: if (i30 == 0)
            break label492;
          i24 = 1073741824;
          label305: int i34 = i11;
          int i35 = i16;
          int i36 = localLayoutParams1.width;
          int i37 = 65534;
          if (i36 != i37)
          {
            i24 = 1073741824;
            int i38 = localLayoutParams1.width;
            int i39 = 65535;
            if (i38 != i39)
              i34 = localLayoutParams1.width;
          }
          int i40 = localLayoutParams1.height;
          int i41 = 65534;
          if (i40 != i41)
          {
            i25 = 1073741824;
            int i42 = localLayoutParams1.height;
            int i43 = 65535;
            if (i42 != i43)
              i35 = localLayoutParams1.height;
          }
          int i44 = i34;
          int i45 = i24;
          int i46 = View.MeasureSpec.makeMeasureSpec(i44, i45);
          int i47 = View.MeasureSpec.makeMeasureSpec(i35, i25);
          int i48 = i46;
          localView1.measure(i48, i47);
          if (i30 == 0)
            break label505;
          int i49 = localView1.getMeasuredHeight();
          i16 -= i49;
        }
      }
      while (true)
      {
        i18 += 1;
        break;
        label480: i30 = 0;
        break label272;
        label486: i33 = 0;
        break label295;
        label492: if (i33 == 0)
          break label305;
        i25 = 1073741824;
        break label305;
        label505: if (i33 != 0)
        {
          int i50 = localView1.getMeasuredWidth();
          i11 -= i50;
        }
      }
    }
    int i51 = 1073741824;
    int i52 = View.MeasureSpec.makeMeasureSpec(i11, i51);
    this.mChildWidthMeasureSpec = i52;
    int i53 = 1073741824;
    int i54 = View.MeasureSpec.makeMeasureSpec(i16, i53);
    this.mChildHeightMeasureSpec = i54;
    boolean bool1 = true;
    this.mInLayout = bool1;
    populate();
    boolean bool2 = false;
    this.mInLayout = bool2;
    int i55 = getChildCount();
    int i56 = 0;
    while (true)
    {
      int i57 = i55;
      if (i56 >= i57)
        return;
      View localView2 = getChildAt(i56);
      int i58 = localView2.getVisibility();
      int i59 = 8;
      if (i58 != i59)
      {
        LayoutParams localLayoutParams2 = (LayoutParams)localView2.getLayoutParams();
        if ((localLayoutParams2 == null) || (!localLayoutParams2.isDecor))
        {
          float f1 = i11;
          float f2 = localLayoutParams2.widthFactor;
          int i60 = View.MeasureSpec.makeMeasureSpec((int)(f1 * f2), 1073741824);
          int i61 = this.mChildHeightMeasureSpec;
          int i62 = i60;
          int i63 = i61;
          localView2.measure(i62, i63);
        }
      }
      i56 += 1;
    }
  }

  protected void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
    int i;
    int n;
    int i1;
    if (this.mDecorChildCount > 0)
    {
      i = getScrollX();
      int j = getPaddingLeft();
      int k = getPaddingRight();
      int m = getWidth();
      n = getChildCount();
      i1 = 0;
      while (i1 < n)
      {
        View localView1 = getChildAt(i1);
        LayoutParams localLayoutParams = (LayoutParams)localView1.getLayoutParams();
        if (!localLayoutParams.isDecor)
        {
          i1 += 1;
        }
        else
        {
          int i2;
          switch (localLayoutParams.gravity & 0x7)
          {
          case 2:
          case 4:
          default:
            i2 = j;
          case 3:
          case 1:
          case 5:
          }
          while (true)
          {
            int i3 = i2 + i;
            int i4 = localView1.getLeft();
            int i5 = i3 - i4;
            if (i5 == 0)
              break;
            localView1.offsetLeftAndRight(i5);
            break;
            i2 = j;
            int i6 = localView1.getWidth();
            j += i6;
            continue;
            int i7 = localView1.getMeasuredWidth();
            i2 = Math.max((m - i7) / 2, j);
            continue;
            int i8 = m - k;
            int i9 = localView1.getMeasuredWidth();
            i2 = i8 - i9;
            int i10 = localView1.getMeasuredWidth();
            int i11 = k + i10;
          }
        }
      }
    }
    if (this.mOnPageChangeListener != null)
    {
      OnPageChangeListener localOnPageChangeListener1 = this.mOnPageChangeListener;
      int i12 = paramInt1;
      float f1 = paramFloat;
      int i13 = paramInt2;
      localOnPageChangeListener1.onPageScrolled(i12, f1, i13);
    }
    if (this.mInternalPageChangeListener != null)
    {
      OnPageChangeListener localOnPageChangeListener2 = this.mInternalPageChangeListener;
      int i14 = paramInt1;
      float f2 = paramFloat;
      int i15 = paramInt2;
      localOnPageChangeListener2.onPageScrolled(i14, f2, i15);
    }
    if (this.mPageTransformer != null)
    {
      i = getScrollX();
      n = getChildCount();
      i1 = 0;
      if (i1 < n)
      {
        View localView2 = getChildAt(i1);
        if (((LayoutParams)localView2.getLayoutParams()).isDecor);
        while (true)
        {
          i1 += 1;
          break;
          float f3 = localView2.getLeft() - i;
          float f4 = getClientWidth();
          float f5 = f3 / f4;
          this.mPageTransformer.transformPage(localView2, f5);
        }
      }
    }
    this.mCalledSuper = true;
  }

  protected boolean onRequestFocusInDescendants(int paramInt, Rect paramRect)
  {
    int i = getChildCount();
    int j;
    int k;
    int m;
    int n;
    if ((paramInt & 0x2) != 0)
    {
      j = 0;
      k = 1;
      m = i;
      n = j;
      label24: if (n == m)
        break label123;
      View localView = getChildAt(n);
      if (localView.getVisibility() != 0)
        break label113;
      ItemInfo localItemInfo = infoForChild(localView);
      if (localItemInfo == null)
        break label113;
      int i1 = localItemInfo.position;
      int i2 = this.mCurItem;
      if ((i1 == i2) || (!localView.requestFocus(paramInt, paramRect)))
        break label113;
    }
    label113: label123: for (boolean bool = true; ; bool = false)
    {
      return bool;
      j = i + -1;
      k = -1;
      m = -1;
      break;
      n += k;
      break label24;
    }
  }

  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!(paramParcelable instanceof SavedState))
    {
      super.onRestoreInstanceState(paramParcelable);
      return;
    }
    SavedState localSavedState = (SavedState)paramParcelable;
    Parcelable localParcelable1 = localSavedState.getSuperState();
    super.onRestoreInstanceState(localParcelable1);
    if (this.mAdapter != null)
    {
      PagerAdapter localPagerAdapter = this.mAdapter;
      Parcelable localParcelable2 = localSavedState.adapterState;
      ClassLoader localClassLoader1 = localSavedState.loader;
      localPagerAdapter.restoreState(localParcelable2, localClassLoader1);
      int i = localSavedState.position;
      setCurrentItemInternal(i, false, true);
      return;
    }
    int j = localSavedState.position;
    this.mRestoredCurItem = j;
    Parcelable localParcelable3 = localSavedState.adapterState;
    this.mRestoredAdapterState = localParcelable3;
    ClassLoader localClassLoader2 = localSavedState.loader;
    this.mRestoredClassLoader = localClassLoader2;
  }

  public Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable1 = super.onSaveInstanceState();
    SavedState localSavedState = new SavedState(localParcelable1);
    int i = this.mCurItem;
    localSavedState.position = i;
    if (this.mAdapter != null)
    {
      Parcelable localParcelable2 = this.mAdapter.saveState();
      localSavedState.adapterState = localParcelable2;
    }
    return localSavedState;
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3)
      return;
    int i = this.mPageMargin;
    int j = this.mPageMargin;
    recomputeScrollPosition(paramInt1, paramInt3, i, j);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1;
    if (this.mFakeDragging)
      bool1 = true;
    while (true)
    {
      return bool1;
      if ((paramMotionEvent.getAction() == 0) && (paramMotionEvent.getEdgeFlags() != 0))
      {
        bool1 = false;
      }
      else
      {
        if ((this.mAdapter != null) && (this.mAdapter.getCount() != 0))
          break;
        bool1 = false;
      }
    }
    if (this.mVelocityTracker == null)
    {
      VelocityTracker localVelocityTracker1 = VelocityTracker.obtain();
      this.mVelocityTracker = localVelocityTracker1;
    }
    VelocityTracker localVelocityTracker2 = this.mVelocityTracker;
    MotionEvent localMotionEvent1 = paramMotionEvent;
    localVelocityTracker2.addMovement(localMotionEvent1);
    int i = paramMotionEvent.getAction();
    boolean bool2 = false;
    switch (i & 0xFF)
    {
    case 4:
    default:
    case 0:
    case 2:
    case 1:
    case 3:
    case 5:
    case 6:
    }
    while (true)
    {
      if (bool2)
        ViewCompat.postInvalidateOnAnimation(this);
      bool1 = true;
      break;
      this.mScroller.abortAnimation();
      boolean bool3 = false;
      this.mPopulatePending = bool3;
      populate();
      boolean bool4 = true;
      this.mIsBeingDragged = bool4;
      ViewPager localViewPager1 = this;
      int j = 1;
      localViewPager1.setScrollState(j);
      float f1 = paramMotionEvent.getX();
      float f2 = f1;
      this.mInitialMotionX = f2;
      float f3 = f1;
      this.mLastMotionX = f3;
      float f4 = paramMotionEvent.getY();
      float f5 = f4;
      this.mInitialMotionY = f5;
      float f6 = f4;
      this.mLastMotionY = f6;
      MotionEvent localMotionEvent2 = paramMotionEvent;
      int k = 0;
      int m = MotionEventCompat.getPointerId(localMotionEvent2, k);
      this.mActivePointerId = m;
      continue;
      float f10;
      float f15;
      float f16;
      if (!this.mIsBeingDragged)
      {
        int n = this.mActivePointerId;
        MotionEvent localMotionEvent3 = paramMotionEvent;
        int i1 = n;
        int i2 = MotionEventCompat.findPointerIndex(localMotionEvent3, i1);
        float f7 = MotionEventCompat.getX(paramMotionEvent, i2);
        float f8 = this.mLastMotionX;
        float f9 = Math.abs(f7 - f8);
        f10 = MotionEventCompat.getY(paramMotionEvent, i2);
        float f11 = this.mLastMotionY;
        float f12 = Math.abs(f10 - f11);
        float f13 = this.mTouchSlop;
        if ((f9 > f13) && (f9 > f12))
        {
          boolean bool5 = true;
          this.mIsBeingDragged = bool5;
          float f14 = this.mInitialMotionX;
          if (f7 - f14 <= 0.0F)
            break label527;
          f15 = this.mInitialMotionX;
          f16 = this.mTouchSlop;
        }
      }
      label527: float f22;
      float f23;
      for (float f17 = f15 + f16; ; f17 = f22 - f23)
      {
        float f18 = f17;
        this.mLastMotionX = f18;
        float f19 = f10;
        this.mLastMotionY = f19;
        ViewPager localViewPager2 = this;
        int i3 = 1;
        localViewPager2.setScrollState(i3);
        ViewPager localViewPager3 = this;
        boolean bool6 = true;
        localViewPager3.setScrollingCacheEnabled(bool6);
        if (!this.mIsBeingDragged)
          break;
        int i4 = this.mActivePointerId;
        MotionEvent localMotionEvent4 = paramMotionEvent;
        int i5 = i4;
        int i6 = MotionEventCompat.findPointerIndex(localMotionEvent4, i5);
        float f20 = MotionEventCompat.getX(paramMotionEvent, i6);
        ViewPager localViewPager4 = this;
        float f21 = f20;
        boolean bool7 = localViewPager4.performDrag(f21);
        bool2 |= bool7;
        break;
        f22 = this.mInitialMotionX;
        f23 = this.mTouchSlop;
      }
      if (this.mIsBeingDragged)
      {
        VelocityTracker localVelocityTracker3 = this.mVelocityTracker;
        float f24 = this.mMaximumVelocity;
        VelocityTracker localVelocityTracker4 = localVelocityTracker3;
        int i7 = 1000;
        float f25 = f24;
        localVelocityTracker4.computeCurrentVelocity(i7, f25);
        int i8 = this.mActivePointerId;
        VelocityTracker localVelocityTracker5 = localVelocityTracker3;
        int i9 = i8;
        int i10 = (int)VelocityTrackerCompat.getXVelocity(localVelocityTracker5, i9);
        boolean bool8 = true;
        this.mPopulatePending = bool8;
        int i11 = getClientWidth();
        int i12 = getScrollX();
        ItemInfo localItemInfo = infoForCurrentScrollPosition();
        int i13 = localItemInfo.position;
        float f26 = i12;
        float f27 = i11;
        float f28 = f26 / f27;
        float f29 = localItemInfo.offset;
        float f30 = f28 - f29;
        float f31 = localItemInfo.widthFactor;
        float f32 = f30 / f31;
        int i14 = this.mActivePointerId;
        MotionEvent localMotionEvent5 = paramMotionEvent;
        int i15 = i14;
        int i16 = MotionEventCompat.findPointerIndex(localMotionEvent5, i15);
        float f33 = MotionEventCompat.getX(paramMotionEvent, i16);
        float f34 = this.mInitialMotionX;
        int i17 = (int)(f33 - f34);
        ViewPager localViewPager5 = this;
        int i18 = i17;
        int i19 = localViewPager5.determineTargetPage(i13, f32, i10, i18);
        ViewPager localViewPager6 = this;
        boolean bool9 = true;
        boolean bool10 = true;
        localViewPager6.setCurrentItemInternal(i19, bool9, bool10, i10);
        int i20 = 65535;
        this.mActivePointerId = i20;
        endDrag();
        boolean bool11 = this.mLeftEdge.onRelease();
        boolean bool12 = this.mRightEdge.onRelease();
        bool2 = bool11 | bool12;
        continue;
        if (this.mIsBeingDragged)
        {
          int i21 = this.mCurItem;
          ViewPager localViewPager7 = this;
          int i22 = i21;
          boolean bool13 = true;
          int i23 = 0;
          boolean bool14 = false;
          localViewPager7.scrollToItem(i22, bool13, i23, bool14);
          int i24 = 65535;
          this.mActivePointerId = i24;
          endDrag();
          boolean bool15 = this.mLeftEdge.onRelease();
          boolean bool16 = this.mRightEdge.onRelease();
          bool2 = bool15 | bool16;
          continue;
          int i25 = MotionEventCompat.getActionIndex(paramMotionEvent);
          float f35 = MotionEventCompat.getX(paramMotionEvent, i25);
          this.mLastMotionX = f35;
          int i26 = MotionEventCompat.getPointerId(paramMotionEvent, i25);
          this.mActivePointerId = i26;
          continue;
          onSecondaryPointerUp(paramMotionEvent);
          int i27 = this.mActivePointerId;
          MotionEvent localMotionEvent6 = paramMotionEvent;
          int i28 = i27;
          int i29 = MotionEventCompat.findPointerIndex(localMotionEvent6, i28);
          MotionEvent localMotionEvent7 = paramMotionEvent;
          int i30 = i29;
          float f36 = MotionEventCompat.getX(localMotionEvent7, i30);
          this.mLastMotionX = f36;
        }
      }
    }
  }

  boolean pageLeft()
  {
    boolean bool = true;
    if (this.mCurItem > 0)
    {
      int i = this.mCurItem + -1;
      setCurrentItem(i, true);
    }
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  boolean pageRight()
  {
    boolean bool = true;
    if (this.mAdapter != null)
    {
      int i = this.mCurItem;
      int j = this.mAdapter.getCount() + -1;
      if (i < j)
      {
        int k = this.mCurItem + 1;
        setCurrentItem(k, true);
      }
    }
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  void populate()
  {
    int i = this.mCurItem;
    populate(i);
  }

  void populate(int paramInt)
  {
    ItemInfo localItemInfo1 = null;
    int i = 2;
    int j = this.mCurItem;
    int k = paramInt;
    if (j != k)
    {
      int m = this.mCurItem;
      int n = paramInt;
      if (m >= n)
        break label81;
    }
    label81: for (i = 66; ; i = 17)
    {
      int i1 = this.mCurItem;
      ViewPager localViewPager1 = this;
      int i2 = i1;
      localItemInfo1 = localViewPager1.infoForPosition(i2);
      int i3 = paramInt;
      this.mCurItem = i3;
      if (this.mAdapter != null)
        break;
      sortChildDrawingOrder();
      return;
    }
    if (this.mPopulatePending)
    {
      sortChildDrawingOrder();
      return;
    }
    if (getWindowToken() == null)
      return;
    PagerAdapter localPagerAdapter1 = this.mAdapter;
    ViewPager localViewPager2 = this;
    localPagerAdapter1.startUpdate(localViewPager2);
    int i4 = this.mOffscreenPageLimit;
    int i5 = this.mCurItem - i4;
    int i6 = Math.max(0, i5);
    int i7 = this.mAdapter.getCount();
    int i8 = i7 + -1;
    int i9 = this.mCurItem + i4;
    int i10 = Math.min(i8, i9);
    int i11 = this.mExpectedAdapterCount;
    if (i7 != i11)
      try
      {
        Resources localResources = getResources();
        int i12 = getId();
        String str1 = localResources.getResourceName(i12);
        str2 = str1;
        StringBuilder localStringBuilder1 = new StringBuilder().append("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ");
        int i13 = this.mExpectedAdapterCount;
        StringBuilder localStringBuilder2 = localStringBuilder1.append(i13).append(", found: ").append(i7).append(" Pager id: ");
        String str3 = str2;
        StringBuilder localStringBuilder3 = localStringBuilder2.append(str3).append(" Pager class: ");
        Class localClass1 = getClass();
        StringBuilder localStringBuilder4 = localStringBuilder3.append(localClass1).append(" Problematic adapter: ");
        Class localClass2 = this.mAdapter.getClass();
        String str4 = localClass2;
        throw new IllegalStateException(str4);
      }
      catch (Resources.NotFoundException localNotFoundException)
      {
        while (true)
          String str2 = Integer.toHexString(getId());
      }
    Object localObject1 = null;
    int i14 = 0;
    int i15 = this.mItems.size();
    if (i14 < i15)
    {
      localItemInfo2 = (ItemInfo)this.mItems.get(i14);
      int i16 = localItemInfo2.position;
      int i17 = this.mCurItem;
      int i18 = i16;
      int i19 = i17;
      if (i18 < i19)
        break label897;
      int i20 = localItemInfo2.position;
      int i21 = this.mCurItem;
      int i22 = i20;
      int i23 = i21;
      if (i22 != i23)
        localObject1 = localItemInfo2;
    }
    if ((localObject1 == null) && (i7 > 0))
    {
      int i24 = this.mCurItem;
      ViewPager localViewPager3 = this;
      int i25 = i24;
      localObject1 = localViewPager3.addNewItem(i25, i14);
    }
    float f1;
    int i26;
    label523: int i28;
    float f2;
    label538: int i29;
    float f3;
    int i32;
    ItemInfo localItemInfo3;
    label648: float f4;
    label657: PagerAdapter localPagerAdapter2;
    int i37;
    if (localObject1 != null)
    {
      f1 = 0.0F;
      i26 = i14 + -1;
      if (i26 >= 0)
      {
        ArrayList localArrayList1 = this.mItems;
        int i27 = i26;
        localItemInfo2 = (ItemInfo)localArrayList1.get(i27);
        i28 = getClientWidth();
        if (i28 > 0)
          break label912;
        f2 = 0.0F;
        i29 = this.mCurItem + -1;
        if (i29 >= 0)
        {
          if (f1 < f2)
            break label1101;
          int i30 = i29;
          int i31 = i6;
          if (i30 >= i31)
            break label1101;
          if (localItemInfo2 != null)
            break label956;
        }
        f3 = ((ItemInfo)localObject1).widthFactor;
        i32 = i14 + 1;
        if (f3 < 2.0F)
        {
          int i33 = this.mItems.size();
          int i34 = i32;
          int i35 = i33;
          if (i34 >= i35)
            break label1265;
          ArrayList localArrayList2 = this.mItems;
          int i36 = i32;
          localItemInfo3 = (ItemInfo)localArrayList2.get(i36);
          if (i28 > 0)
            break label1271;
          f4 = 0.0F;
          i29 = this.mCurItem + 1;
          if (i29 < i7)
          {
            if ((f3 < f4) || (i29 <= i10))
              break label1446;
            if (localItemInfo3 != null)
              break label1297;
          }
        }
        ViewPager localViewPager4 = this;
        ItemInfo localItemInfo4 = localItemInfo1;
        localViewPager4.calculatePageOffsets((ItemInfo)localObject1, i14, localItemInfo4);
      }
    }
    else
    {
      localPagerAdapter2 = this.mAdapter;
      i37 = this.mCurItem;
      if (localObject1 == null)
        break label1645;
    }
    int i40;
    label897: label912: label956: label1095: label1101: label1645: for (Object localObject2 = ((ItemInfo)localObject1).object; ; localObject2 = null)
    {
      PagerAdapter localPagerAdapter3 = localPagerAdapter2;
      ViewPager localViewPager5 = this;
      int i38 = i37;
      Object localObject3 = localObject2;
      localPagerAdapter3.setPrimaryItem(localViewPager5, i38, localObject3);
      PagerAdapter localPagerAdapter4 = this.mAdapter;
      ViewPager localViewPager6 = this;
      localPagerAdapter4.finishUpdate(localViewPager6);
      int i39 = getChildCount();
      i40 = 0;
      while (i40 < i39)
      {
        ViewPager localViewPager7 = this;
        int i41 = i40;
        View localView1 = localViewPager7.getChildAt(i41);
        LayoutParams localLayoutParams = (LayoutParams)localView1.getLayoutParams();
        int i42 = i40;
        localLayoutParams.childIndex = i42;
        if ((!localLayoutParams.isDecor) && (localLayoutParams.widthFactor == 0.0F))
        {
          localItemInfo2 = infoForChild(localView1);
          if (localItemInfo2 != null)
          {
            float f5 = localItemInfo2.widthFactor;
            localLayoutParams.widthFactor = f5;
            int i43 = localItemInfo2.position;
            localLayoutParams.position = i43;
          }
        }
        i40 += 1;
      }
      i14 += 1;
      break;
      localItemInfo2 = null;
      break label523;
      float f6 = ((ItemInfo)localObject1).widthFactor;
      float f7 = 2.0F - f6;
      float f8 = getPaddingLeft();
      float f9 = i28;
      float f10 = f8 / f9;
      f2 = f7 + f10;
      break label538;
      int i44 = localItemInfo2.position;
      int i45 = i29;
      int i46 = i44;
      ArrayList localArrayList4;
      int i49;
      if ((i45 != i46) && (!localItemInfo2.scrolling))
      {
        ArrayList localArrayList3 = this.mItems;
        int i47 = i26;
        Object localObject4 = localArrayList3.remove(i47);
        PagerAdapter localPagerAdapter5 = this.mAdapter;
        Object localObject5 = localItemInfo2.object;
        PagerAdapter localPagerAdapter6 = localPagerAdapter5;
        ViewPager localViewPager8 = this;
        int i48 = i29;
        Object localObject6 = localObject5;
        localPagerAdapter6.destroyItem(localViewPager8, i48, localObject6);
        i26 += -1;
        i14 += -1;
        if (i26 < 0)
          break label1095;
        localArrayList4 = this.mItems;
        i49 = i26;
      }
      for (localItemInfo2 = (ItemInfo)localArrayList4.get(i49); ; localItemInfo2 = null)
      {
        i29 += -1;
        break;
      }
      if (localItemInfo2 != null)
      {
        int i50 = localItemInfo2.position;
        int i51 = i29;
        int i52 = i50;
        if (i51 != i52)
        {
          float f11 = localItemInfo2.widthFactor;
          f1 += f11;
          i26 += -1;
          ArrayList localArrayList5;
          int i53;
          if (i26 >= 0)
          {
            localArrayList5 = this.mItems;
            i53 = i26;
          }
          for (localItemInfo2 = (ItemInfo)localArrayList5.get(i53); ; localItemInfo2 = null)
            break;
        }
      }
      int i54 = i26 + 1;
      ViewPager localViewPager9 = this;
      int i55 = i29;
      int i56 = i54;
      float f12 = localViewPager9.addNewItem(i55, i56).widthFactor;
      f1 += f12;
      i14 += 1;
      ArrayList localArrayList6;
      int i57;
      if (i26 >= 0)
      {
        localArrayList6 = this.mItems;
        i57 = i26;
      }
      for (localItemInfo2 = (ItemInfo)localArrayList6.get(i57); ; localItemInfo2 = null)
        break;
      label1265: localItemInfo3 = null;
      break label648;
      label1271: float f13 = getPaddingRight();
      float f14 = i28;
      f4 = f13 / f14 + 2.0F;
      break label657;
      int i58 = localItemInfo3.position;
      int i59 = i29;
      int i60 = i58;
      ArrayList localArrayList8;
      int i66;
      if ((i59 != i60) && (!localItemInfo3.scrolling))
      {
        ArrayList localArrayList7 = this.mItems;
        int i61 = i32;
        Object localObject7 = localArrayList7.remove(i61);
        PagerAdapter localPagerAdapter7 = this.mAdapter;
        Object localObject8 = localItemInfo3.object;
        PagerAdapter localPagerAdapter8 = localPagerAdapter7;
        ViewPager localViewPager10 = this;
        int i62 = i29;
        Object localObject9 = localObject8;
        localPagerAdapter8.destroyItem(localViewPager10, i62, localObject9);
        int i63 = this.mItems.size();
        int i64 = i32;
        int i65 = i63;
        if (i64 >= i65)
          break label1440;
        localArrayList8 = this.mItems;
        i66 = i32;
      }
      for (localItemInfo3 = (ItemInfo)localArrayList8.get(i66); ; localItemInfo3 = null)
      {
        i29 += 1;
        break;
      }
      if (localItemInfo3 != null)
      {
        int i67 = localItemInfo3.position;
        int i68 = i29;
        int i69 = i67;
        if (i68 != i69)
        {
          float f15 = localItemInfo3.widthFactor;
          f3 += f15;
          i32 += 1;
          int i70 = this.mItems.size();
          int i71 = i32;
          int i72 = i70;
          ArrayList localArrayList9;
          int i73;
          if (i71 < i72)
          {
            localArrayList9 = this.mItems;
            i73 = i32;
          }
          for (localItemInfo3 = (ItemInfo)localArrayList9.get(i73); ; localItemInfo3 = null)
            break;
        }
      }
      ViewPager localViewPager11 = this;
      int i74 = i29;
      int i75 = i32;
      ItemInfo localItemInfo5 = localViewPager11.addNewItem(i74, i75);
      i32 += 1;
      float f16 = localItemInfo5.widthFactor;
      f3 += f16;
      int i76 = this.mItems.size();
      int i77 = i32;
      int i78 = i76;
      ArrayList localArrayList10;
      int i79;
      if (i77 < i78)
      {
        localArrayList10 = this.mItems;
        i79 = i32;
      }
      for (localItemInfo3 = (ItemInfo)localArrayList10.get(i79); ; localItemInfo3 = null)
        break;
    }
    label1297: label1440: label1446: sortChildDrawingOrder();
    if (!hasFocus())
      return;
    View localView2 = findFocus();
    if (localView2 != null);
    for (ItemInfo localItemInfo2 = infoForAnyChild(localView2); ; localItemInfo2 = null)
    {
      if (localItemInfo2 != null)
      {
        int i80 = localItemInfo2.position;
        int i81 = this.mCurItem;
        int i82 = i80;
        int i83 = i81;
        if (i82 != i83)
          return;
      }
      i40 = 0;
      while (true)
      {
        int i84 = getChildCount();
        int i85 = i40;
        int i86 = i84;
        if (i85 >= i86)
          return;
        ViewPager localViewPager12 = this;
        int i87 = i40;
        View localView3 = localViewPager12.getChildAt(i87);
        localItemInfo2 = infoForChild(localView3);
        if (localItemInfo2 != null)
        {
          int i88 = localItemInfo2.position;
          int i89 = this.mCurItem;
          int i90 = i88;
          int i91 = i89;
          if ((i90 != i91) && (localView3.requestFocus(i)))
            return;
        }
        i40 += 1;
      }
    }
  }

  public void removeView(View paramView)
  {
    if (this.mInLayout)
    {
      removeViewInLayout(paramView);
      return;
    }
    super.removeView(paramView);
  }

  public void setAdapter(PagerAdapter paramPagerAdapter)
  {
    if (this.mAdapter != null)
    {
      PagerAdapter localPagerAdapter1 = this.mAdapter;
      PagerObserver localPagerObserver1 = this.mObserver;
      localPagerAdapter1.unregisterDataSetObserver(localPagerObserver1);
      this.mAdapter.startUpdate(this);
      int i = 0;
      while (true)
      {
        int j = this.mItems.size();
        if (i >= j)
          break;
        ItemInfo localItemInfo = (ItemInfo)this.mItems.get(i);
        PagerAdapter localPagerAdapter2 = this.mAdapter;
        int k = localItemInfo.position;
        Object localObject = localItemInfo.object;
        localPagerAdapter2.destroyItem(this, k, localObject);
        i += 1;
      }
      this.mAdapter.finishUpdate(this);
      this.mItems.clear();
      removeNonDecorViews();
      this.mCurItem = 0;
      scrollTo(0, 0);
    }
    PagerAdapter localPagerAdapter3 = this.mAdapter;
    this.mAdapter = paramPagerAdapter;
    this.mExpectedAdapterCount = 0;
    boolean bool;
    if (this.mAdapter != null)
    {
      if (this.mObserver == null)
      {
        PagerObserver localPagerObserver2 = new PagerObserver(null);
        this.mObserver = localPagerObserver2;
      }
      PagerAdapter localPagerAdapter4 = this.mAdapter;
      PagerObserver localPagerObserver3 = this.mObserver;
      localPagerAdapter4.registerDataSetObserver(localPagerObserver3);
      this.mPopulatePending = false;
      bool = this.mFirstLayout;
      this.mFirstLayout = true;
      int m = this.mAdapter.getCount();
      this.mExpectedAdapterCount = m;
      if (this.mRestoredCurItem < 0)
        break label321;
      PagerAdapter localPagerAdapter5 = this.mAdapter;
      Parcelable localParcelable = this.mRestoredAdapterState;
      ClassLoader localClassLoader = this.mRestoredClassLoader;
      localPagerAdapter5.restoreState(localParcelable, localClassLoader);
      int n = this.mRestoredCurItem;
      setCurrentItemInternal(n, false, true);
      this.mRestoredCurItem = -1;
      this.mRestoredAdapterState = null;
      this.mRestoredClassLoader = null;
    }
    while (true)
    {
      if (this.mAdapterChangeListener == null)
        return;
      if (localPagerAdapter3 == paramPagerAdapter)
        return;
      this.mAdapterChangeListener.onAdapterChanged(localPagerAdapter3, paramPagerAdapter);
      return;
      label321: if (!bool)
        populate();
      else
        requestLayout();
    }
  }

  void setChildrenDrawingOrderEnabledCompat(boolean paramBoolean)
  {
    if (Build.VERSION.SDK_INT < 7)
      return;
    if (this.mSetChildrenDrawingOrderEnabled == null);
    try
    {
      Class[] arrayOfClass = new Class[1];
      Class localClass = Boolean.TYPE;
      arrayOfClass[0] = localClass;
      Method localMethod1 = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", arrayOfClass);
      this.mSetChildrenDrawingOrderEnabled = localMethod1;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      try
      {
        while (true)
        {
          Method localMethod2 = this.mSetChildrenDrawingOrderEnabled;
          Object[] arrayOfObject = new Object[1];
          Boolean localBoolean = Boolean.valueOf(paramBoolean);
          arrayOfObject[0] = localBoolean;
          Object localObject = localMethod2.invoke(this, arrayOfObject);
          return;
          localNoSuchMethodException = localNoSuchMethodException;
          int i = Log.e("ViewPager", "Can't find setChildrenDrawingOrderEnabled", localNoSuchMethodException);
        }
      }
      catch (Exception localException)
      {
        int j = Log.e("ViewPager", "Error changing children drawing order", localException);
      }
    }
  }

  public void setCurrentItem(int paramInt)
  {
    this.mPopulatePending = false;
    if (!this.mFirstLayout);
    for (boolean bool = true; ; bool = false)
    {
      setCurrentItemInternal(paramInt, bool, false);
      return;
    }
  }

  public void setCurrentItem(int paramInt, boolean paramBoolean)
  {
    this.mPopulatePending = false;
    setCurrentItemInternal(paramInt, paramBoolean, false);
  }

  void setCurrentItemInternal(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    setCurrentItemInternal(paramInt, paramBoolean1, paramBoolean2, 0);
  }

  void setCurrentItemInternal(int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2)
  {
    boolean bool = true;
    if ((this.mAdapter == null) || (this.mAdapter.getCount() <= 0))
    {
      setScrollingCacheEnabled(false);
      return;
    }
    if ((!paramBoolean2) && (this.mCurItem != paramInt1) && (this.mItems.size() != 0))
    {
      setScrollingCacheEnabled(false);
      return;
    }
    if (paramInt1 < 0)
      paramInt1 = 0;
    while (true)
    {
      int i = this.mOffscreenPageLimit;
      int j = this.mCurItem + i;
      if (paramInt1 <= j)
      {
        int k = this.mCurItem - i;
        if (paramInt1 >= k)
          break;
      }
      int m = 0;
      while (true)
      {
        int n = this.mItems.size();
        if (m >= n)
          break;
        ((ItemInfo)this.mItems.get(m)).scrolling = true;
        m += 1;
      }
      int i1 = this.mAdapter.getCount();
      if (paramInt1 >= i1)
        paramInt1 = this.mAdapter.getCount() + -1;
    }
    if (this.mCurItem != paramInt1);
    while (this.mFirstLayout)
    {
      this.mCurItem = paramInt1;
      if ((bool) && (this.mOnPageChangeListener != null))
        this.mOnPageChangeListener.onPageSelected(paramInt1);
      if ((bool) && (this.mInternalPageChangeListener != null))
        this.mInternalPageChangeListener.onPageSelected(paramInt1);
      requestLayout();
      return;
      bool = false;
    }
    populate(paramInt1);
    scrollToItem(paramInt1, paramBoolean1, paramInt2, bool);
  }

  OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener paramOnPageChangeListener)
  {
    OnPageChangeListener localOnPageChangeListener = this.mInternalPageChangeListener;
    this.mInternalPageChangeListener = paramOnPageChangeListener;
    return localOnPageChangeListener;
  }

  public void setOffscreenPageLimit(int paramInt)
  {
    if (paramInt < 1)
    {
      String str = "Requested offscreen page limit " + paramInt + " too small; defaulting to " + 1;
      int i = Log.w("ViewPager", str);
      paramInt = 1;
    }
    int j = this.mOffscreenPageLimit;
    if (paramInt != j)
      return;
    this.mOffscreenPageLimit = paramInt;
    populate();
  }

  void setOnAdapterChangeListener(OnAdapterChangeListener paramOnAdapterChangeListener)
  {
    this.mAdapterChangeListener = paramOnAdapterChangeListener;
  }

  public void setOnPageChangeListener(OnPageChangeListener paramOnPageChangeListener)
  {
    this.mOnPageChangeListener = paramOnPageChangeListener;
  }

  public void setPageMargin(int paramInt)
  {
    int i = this.mPageMargin;
    this.mPageMargin = paramInt;
    int j = getWidth();
    recomputeScrollPosition(j, j, paramInt, i);
    requestLayout();
  }

  public void setPageMarginDrawable(int paramInt)
  {
    Drawable localDrawable = getContext().getResources().getDrawable(paramInt);
    setPageMarginDrawable(localDrawable);
  }

  public void setPageMarginDrawable(Drawable paramDrawable)
  {
    this.mMarginDrawable = paramDrawable;
    if (paramDrawable != null)
      refreshDrawableState();
    if (paramDrawable == null);
    for (boolean bool = true; ; bool = false)
    {
      setWillNotDraw(bool);
      invalidate();
      return;
    }
  }

  public void setPageTransformer(boolean paramBoolean, PageTransformer paramPageTransformer)
  {
    int i = 1;
    if (Build.VERSION.SDK_INT < 11)
      return;
    boolean bool1;
    boolean bool2;
    label28: int j;
    if (paramPageTransformer != null)
    {
      bool1 = true;
      if (this.mPageTransformer == null)
        break label82;
      bool2 = true;
      if (bool1 == bool2)
        break label88;
      j = 1;
      label38: this.mPageTransformer = paramPageTransformer;
      setChildrenDrawingOrderEnabledCompat(bool1);
      if (!bool1)
        break label94;
      if (paramBoolean)
        i = 2;
    }
    label82: label88: label94: for (this.mDrawingOrder = i; ; this.mDrawingOrder = 0)
    {
      if (j == 0)
        return;
      populate();
      return;
      bool1 = false;
      break;
      bool2 = false;
      break label28;
      j = 0;
      break label38;
    }
  }

  void smoothScrollTo(int paramInt1, int paramInt2, int paramInt3)
  {
    if (getChildCount() == 0)
    {
      setScrollingCacheEnabled(false);
      return;
    }
    int i = getScrollX();
    int j = getScrollY();
    int k = paramInt1 - i;
    int m = paramInt2 - j;
    if ((k == 0) && (m == 0))
    {
      completeScroll(false);
      populate();
      setScrollState(0);
      return;
    }
    setScrollingCacheEnabled(true);
    setScrollState(2);
    int n = getClientWidth();
    int i1 = n / 2;
    float f1 = Math.abs(k);
    float f2 = 1.0F * f1;
    float f3 = n;
    float f4 = f2 / f3;
    float f5 = Math.min(1.0F, f4);
    float f6 = i1;
    float f7 = i1;
    float f8 = distanceInfluenceForSnapDuration(f5);
    float f9 = f7 * f8;
    float f10 = f6 + f9;
    paramInt3 = Math.abs(paramInt3);
    float f12;
    if (paramInt3 > 0)
    {
      float f11 = paramInt3;
      f12 = Math.abs(f10 / f11);
    }
    float f18;
    for (int i2 = Math.round(1000.0F * f12) * 4; ; i2 = (int)((1.0F + f18) * 100.0F))
    {
      int i3 = Math.min(i2, 600);
      this.mScroller.startScroll(i, j, k, m, i3);
      ViewCompat.postInvalidateOnAnimation(this);
      return;
      float f13 = n;
      PagerAdapter localPagerAdapter = this.mAdapter;
      int i4 = this.mCurItem;
      float f14 = localPagerAdapter.getPageWidth(i4);
      float f15 = f13 * f14;
      float f16 = Math.abs(k);
      float f17 = this.mPageMargin + f15;
      f18 = f16 / f17;
    }
  }

  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    if (!super.verifyDrawable(paramDrawable))
    {
      Drawable localDrawable = this.mMarginDrawable;
      if (paramDrawable != localDrawable)
        break label22;
    }
    label22: for (boolean bool = true; ; bool = false)
      return bool;
  }

  static class ViewPositionComparator
    implements Comparator<View>
  {
    public int compare(View paramView1, View paramView2)
    {
      ViewPager.LayoutParams localLayoutParams1 = (ViewPager.LayoutParams)paramView1.getLayoutParams();
      ViewPager.LayoutParams localLayoutParams2 = (ViewPager.LayoutParams)paramView2.getLayoutParams();
      boolean bool1 = localLayoutParams1.isDecor;
      boolean bool2 = localLayoutParams2.isDecor;
      int i;
      if (bool1 != bool2)
        if (localLayoutParams1.isDecor)
          i = 1;
      while (true)
      {
        return i;
        i = -1;
        continue;
        int j = localLayoutParams1.position;
        int k = localLayoutParams2.position;
        i = j - k;
      }
    }
  }

  public static class LayoutParams extends ViewGroup.LayoutParams
  {
    int childIndex;
    public int gravity;
    public boolean isDecor;
    boolean needsMeasure;
    int position;
    float widthFactor = 0.0F;

    public LayoutParams()
    {
      super(-1);
    }

    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      int[] arrayOfInt = ViewPager.LAYOUT_ATTRS;
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt);
      int i = localTypedArray.getInteger(0, 48);
      this.gravity = i;
      localTypedArray.recycle();
    }
  }

  private class PagerObserver extends DataSetObserver
  {
    private PagerObserver()
    {
    }

    public void onChanged()
    {
      ViewPager.this.dataSetChanged();
    }

    public void onInvalidated()
    {
      ViewPager.this.dataSetChanged();
    }
  }

  class MyAccessibilityDelegate extends AccessibilityDelegateCompat
  {
    MyAccessibilityDelegate()
    {
    }

    private boolean canScroll()
    {
      boolean bool = true;
      if ((ViewPager.this.mAdapter != null) && (ViewPager.this.mAdapter.getCount() > 1));
      while (true)
      {
        return bool;
        bool = false;
      }
    }

    public void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      super.onInitializeAccessibilityEvent(paramView, paramAccessibilityEvent);
      String str = ViewPager.class.getName();
      paramAccessibilityEvent.setClassName(str);
      AccessibilityRecordCompat localAccessibilityRecordCompat = AccessibilityRecordCompat.obtain();
      boolean bool = canScroll();
      localAccessibilityRecordCompat.setScrollable(bool);
      if (paramAccessibilityEvent.getEventType() != 4096)
        return;
      if (ViewPager.this.mAdapter == null)
        return;
      int i = ViewPager.this.mAdapter.getCount();
      localAccessibilityRecordCompat.setItemCount(i);
      int j = ViewPager.this.mCurItem;
      localAccessibilityRecordCompat.setFromIndex(j);
      int k = ViewPager.this.mCurItem;
      localAccessibilityRecordCompat.setToIndex(k);
    }

    public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
      super.onInitializeAccessibilityNodeInfo(paramView, paramAccessibilityNodeInfoCompat);
      String str = ViewPager.class.getName();
      paramAccessibilityNodeInfoCompat.setClassName(str);
      boolean bool = canScroll();
      paramAccessibilityNodeInfoCompat.setScrollable(bool);
      if (ViewPager.this.canScrollHorizontally(1))
        paramAccessibilityNodeInfoCompat.addAction(4096);
      if (!ViewPager.this.canScrollHorizontally(-1))
        return;
      paramAccessibilityNodeInfoCompat.addAction(8192);
    }

    public boolean performAccessibilityAction(View paramView, int paramInt, Bundle paramBundle)
    {
      boolean bool = true;
      if (super.performAccessibilityAction(paramView, paramInt, paramBundle));
      while (true)
      {
        return bool;
        switch (paramInt)
        {
        default:
          bool = false;
          break;
        case 4096:
          if (ViewPager.this.canScrollHorizontally(1))
          {
            ViewPager localViewPager1 = ViewPager.this;
            int i = ViewPager.this.mCurItem + 1;
            localViewPager1.setCurrentItem(i);
          }
          else
          {
            bool = false;
          }
          break;
        case 8192:
          if (ViewPager.this.canScrollHorizontally(-1))
          {
            ViewPager localViewPager2 = ViewPager.this;
            int j = ViewPager.this.mCurItem + -1;
            localViewPager2.setCurrentItem(j);
          }
          else
          {
            bool = false;
          }
          break;
        }
      }
    }
  }

  public static class SavedState extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks()
    {
      public ViewPager.SavedState createFromParcel(Parcel paramAnonymousParcel, ClassLoader paramAnonymousClassLoader)
      {
        return new ViewPager.SavedState(paramAnonymousParcel, paramAnonymousClassLoader);
      }

      public ViewPager.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ViewPager.SavedState[paramAnonymousInt];
      }
    });
    Parcelable adapterState;
    ClassLoader loader;
    int position;

    SavedState(Parcel paramParcel, ClassLoader paramClassLoader)
    {
      super();
      if (paramClassLoader == null)
        paramClassLoader = getClass().getClassLoader();
      int i = paramParcel.readInt();
      this.position = i;
      Parcelable localParcelable = paramParcel.readParcelable(paramClassLoader);
      this.adapterState = localParcelable;
      this.loader = paramClassLoader;
    }

    public SavedState(Parcelable paramParcelable)
    {
      super();
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("FragmentPager.SavedState{");
      String str = Integer.toHexString(System.identityHashCode(this));
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str).append(" position=");
      int i = this.position;
      return i + "}";
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      int i = this.position;
      paramParcel.writeInt(i);
      Parcelable localParcelable = this.adapterState;
      paramParcel.writeParcelable(localParcelable, paramInt);
    }
  }

  static abstract interface Decor
  {
  }

  static abstract interface OnAdapterChangeListener
  {
    public abstract void onAdapterChanged(PagerAdapter paramPagerAdapter1, PagerAdapter paramPagerAdapter2);
  }

  public static abstract interface PageTransformer
  {
    public abstract void transformPage(View paramView, float paramFloat);
  }

  public static abstract interface OnPageChangeListener
  {
    public abstract void onPageScrollStateChanged(int paramInt);

    public abstract void onPageScrolled(int paramInt1, float paramFloat, int paramInt2);

    public abstract void onPageSelected(int paramInt);
  }

  static class ItemInfo
  {
    Object object;
    float offset;
    int position;
    boolean scrolling;
    float widthFactor;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.ViewPager
 * JD-Core Version:    0.6.2
 */