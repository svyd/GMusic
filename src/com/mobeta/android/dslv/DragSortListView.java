package com.mobeta.android.dslv;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DragSortListView extends ListView
{
  private AdapterWrapper mAdapterWrapper;
  private boolean mAnimate;
  private boolean mBlockLayoutRequests;
  private MotionEvent mCancelEvent;
  private int mCancelMethod;
  private HeightCache mChildHeightCache;
  private float mCurrFloatAlpha;
  private int mDownScrollStartY;
  private float mDownScrollStartYF;
  private int mDragDeltaX;
  private int mDragDeltaY;
  private float mDragDownScrollHeight;
  private float mDragDownScrollStartFrac;
  private boolean mDragEnabled;
  private int mDragFlags;
  private DragListener mDragListener;
  private DragScroller mDragScroller;
  private DragSortTracker mDragSortTracker;
  private int mDragStartY;
  private int mDragState;
  private float mDragUpScrollHeight;
  private float mDragUpScrollStartFrac;
  private DropAnimator mDropAnimator;
  private DropListener mDropListener;
  private int mFirstExpPos;
  private float mFloatAlpha;
  private Point mFloatLoc;
  private int mFloatPos;
  private View mFloatView;
  private int mFloatViewHeight;
  private int mFloatViewHeightHalf;
  private boolean mFloatViewInvalidated;
  private FloatViewManager mFloatViewManager;
  private int mFloatViewMid;
  private boolean mFloatViewOnMeasured;
  private boolean mIgnoreTouchEvent;
  private boolean mInTouchEvent;
  private int mItemHeightCollapsed;
  private boolean mLastCallWasIntercept;
  private int mLastX;
  private int mLastY;
  private LiftAnimator mLiftAnimator;
  private boolean mListViewIntercepted;
  private float mMaxScrollSpeed;
  private DataSetObserver mObserver;
  private int mOffsetX;
  private int mOffsetY;
  private RemoveAnimator mRemoveAnimator;
  private RemoveListener mRemoveListener;
  private float mRemoveVelocityX;
  private View[] mSampleViewTypes;
  private DragScrollProfile mScrollProfile;
  private int mSecondExpPos;
  private float mSlideFrac;
  private float mSlideRegionFrac;
  private int mSrcPos;
  private Point mTouchLoc;
  private boolean mTrackDragSort;
  private int mUpScrollStartY;
  private float mUpScrollStartYF;
  private boolean mUseRemoveVelocity;
  private int mWidthMeasureSpec;
  private int mX;
  private int mY;

  public DragSortListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Point localPoint1 = new Point();
    this.mFloatLoc = localPoint1;
    Point localPoint2 = new Point();
    this.mTouchLoc = localPoint2;
    this.mFloatViewOnMeasured = false;
    this.mFloatAlpha = 1.0F;
    this.mCurrFloatAlpha = 1.0F;
    this.mAnimate = false;
    this.mDragEnabled = true;
    this.mDragState = 0;
    this.mItemHeightCollapsed = 1;
    this.mWidthMeasureSpec = 0;
    View[] arrayOfView = new View[1];
    this.mSampleViewTypes = arrayOfView;
    this.mDragUpScrollStartFrac = 0.3333333F;
    this.mDragDownScrollStartFrac = 0.3333333F;
    this.mMaxScrollSpeed = 0.5F;
    DragSortListView localDragSortListView1 = this;
    DragScrollProfile local1 = new DragScrollProfile()
    {
      public float getSpeed(float paramAnonymousFloat, long paramAnonymousLong)
      {
        return DragSortListView.this.mMaxScrollSpeed * paramAnonymousFloat;
      }
    };
    this.mScrollProfile = local1;
    this.mDragFlags = 0;
    this.mLastCallWasIntercept = false;
    this.mInTouchEvent = false;
    this.mFloatViewManager = null;
    this.mCancelMethod = 0;
    this.mSlideRegionFrac = 0.25F;
    this.mSlideFrac = 0.0F;
    this.mTrackDragSort = false;
    this.mBlockLayoutRequests = false;
    this.mIgnoreTouchEvent = false;
    DragSortListView localDragSortListView2 = this;
    HeightCache localHeightCache = new HeightCache(3);
    this.mChildHeightCache = localHeightCache;
    this.mRemoveVelocityX = 0.0F;
    this.mListViewIntercepted = false;
    this.mFloatViewInvalidated = false;
    int i = 150;
    int j = 150;
    TypedArray localTypedArray1;
    if (paramAttributeSet != null)
    {
      Context localContext = getContext();
      int[] arrayOfInt = R.styleable.DragSortListView;
      AttributeSet localAttributeSet = paramAttributeSet;
      localTypedArray1 = localContext.obtainStyledAttributes(localAttributeSet, arrayOfInt, 0, 0);
      int k = localTypedArray1.getDimensionPixelSize(0, 1);
      int m = Math.max(1, k);
      this.mItemHeightCollapsed = m;
      boolean bool1 = localTypedArray1.getBoolean(5, false);
      this.mTrackDragSort = bool1;
      if (this.mTrackDragSort)
      {
        DragSortListView localDragSortListView3 = this;
        DragSortTracker localDragSortTracker = new DragSortTracker();
        this.mDragSortTracker = localDragSortTracker;
      }
      float f1 = this.mFloatAlpha;
      float f2 = localTypedArray1.getFloat(6, f1);
      this.mFloatAlpha = f2;
      float f3 = this.mFloatAlpha;
      this.mCurrFloatAlpha = f3;
      boolean bool2 = this.mDragEnabled;
      boolean bool3 = localTypedArray1.getBoolean(10, bool2);
      this.mDragEnabled = bool3;
      float f4 = localTypedArray1.getFloat(7, 0.75F);
      float f5 = 1.0F - f4;
      float f6 = Math.min(1.0F, f5);
      float f7 = Math.max(0.0F, f6);
      this.mSlideRegionFrac = f7;
      if (this.mSlideRegionFrac <= 0.0F)
        break label849;
    }
    label849: for (boolean bool4 = true; ; bool4 = false)
    {
      this.mAnimate = bool4;
      float f8 = this.mDragUpScrollStartFrac;
      float f9 = localTypedArray1.getFloat(1, f8);
      DragSortListView localDragSortListView4 = this;
      float f10 = f9;
      localDragSortListView4.setDragScrollStart(f10);
      float f11 = this.mMaxScrollSpeed;
      float f12 = localTypedArray1.getFloat(2, f11);
      this.mMaxScrollSpeed = f12;
      TypedArray localTypedArray2 = localTypedArray1;
      int n = 150;
      i = localTypedArray2.getInt(8, n);
      TypedArray localTypedArray3 = localTypedArray1;
      int i1 = 150;
      j = localTypedArray3.getInt(9, i1);
      if (localTypedArray1.getBoolean(17, true))
      {
        boolean bool5 = localTypedArray1.getBoolean(12, false);
        int i2 = localTypedArray1.getInt(4, 1);
        boolean bool6 = localTypedArray1.getBoolean(11, true);
        int i3 = localTypedArray1.getInt(13, 0);
        int i4 = localTypedArray1.getResourceId(14, 0);
        int i5 = localTypedArray1.getResourceId(15, 0);
        int i6 = localTypedArray1.getResourceId(16, 0);
        int i7 = localTypedArray1.getColor(3, -16777216);
        DragSortListView localDragSortListView5 = this;
        DragSortController localDragSortController = new DragSortController(localDragSortListView5, i4, i3, i2, i6, i5);
        boolean bool7 = bool5;
        localDragSortController.setRemoveEnabled(bool7);
        boolean bool8 = bool6;
        localDragSortController.setSortEnabled(bool8);
        int i8 = i7;
        localDragSortController.setBackgroundColor(i8);
        this.mFloatViewManager = localDragSortController;
        setOnTouchListener(localDragSortController);
      }
      localTypedArray1.recycle();
      DragSortListView localDragSortListView6 = this;
      DragScroller localDragScroller = new DragScroller();
      this.mDragScroller = localDragScroller;
      float f13 = 0.5F;
      if (i > 0)
      {
        DragSortListView localDragSortListView7 = this;
        float f14 = f13;
        int i9 = i;
        RemoveAnimator localRemoveAnimator = new RemoveAnimator(f14, i9);
        this.mRemoveAnimator = localRemoveAnimator;
      }
      if (j > 0)
      {
        DragSortListView localDragSortListView8 = this;
        float f15 = f13;
        int i10 = j;
        DropAnimator localDropAnimator = new DropAnimator(f15, i10);
        this.mDropAnimator = localDropAnimator;
      }
      MotionEvent localMotionEvent = MotionEvent.obtain(0L, 0L, 3, 0.0F, 0.0F, 0.0F, 0.0F, 0, 0.0F, 0.0F, 0, 0);
      this.mCancelEvent = localMotionEvent;
      DragSortListView localDragSortListView9 = this;
      DataSetObserver local2 = new DataSetObserver()
      {
        private void cancel()
        {
          if (DragSortListView.this.mDragState != 4)
            return;
          DragSortListView.this.cancelDrag();
        }

        public void onChanged()
        {
          cancel();
        }

        public void onInvalidated()
        {
          cancel();
        }
      };
      this.mObserver = local2;
      return;
    }
  }

  private void adjustAllItems()
  {
    int i = getFirstVisiblePosition();
    int j = getLastVisiblePosition();
    int k = getHeaderViewsCount() - i;
    int m = Math.max(0, k);
    int n = j - i;
    int i1 = getCount() + -1;
    int i2 = getFooterViewsCount();
    int i3 = i1 - i2 - i;
    int i4 = Math.min(n, i3);
    int i5 = m;
    while (true)
    {
      if (i5 > i4)
        return;
      View localView = getChildAt(i5);
      if (localView != null)
      {
        int i6 = i + i5;
        adjustItem(i6, localView, false);
      }
      i5 += 1;
    }
  }

  private void adjustItem(int paramInt, View paramView, boolean paramBoolean)
  {
    ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    int i = this.mSrcPos;
    int m;
    if (paramInt != i)
    {
      int j = this.mFirstExpPos;
      if (paramInt != j)
      {
        int k = this.mSecondExpPos;
        if (paramInt != k)
        {
          m = -1;
          int n = localLayoutParams.height;
          if (m != n)
          {
            localLayoutParams.height = m;
            paramView.setLayoutParams(localLayoutParams);
          }
          int i1 = this.mFirstExpPos;
          if (paramInt != i1)
          {
            int i2 = this.mSecondExpPos;
            if (paramInt == i2);
          }
          else
          {
            int i3 = this.mSrcPos;
            if (paramInt >= i3)
              break label176;
            ((DragSortItemView)paramView).setGravity(80);
          }
        }
      }
    }
    while (true)
    {
      int i4 = paramView.getVisibility();
      int i5 = 0;
      int i6 = this.mSrcPos;
      if ((paramInt != i6) && (this.mFloatView != null))
        i5 = 4;
      if (i5 != i4)
        return;
      paramView.setVisibility(i5);
      return;
      m = calcItemHeight(paramInt, paramView, paramBoolean);
      break;
      label176: int i7 = this.mSrcPos;
      if (paramInt > i7)
        ((DragSortItemView)paramView).setGravity(48);
    }
  }

  private void adjustOnReorder()
  {
    int i = getFirstVisiblePosition();
    if (this.mSrcPos >= i)
      return;
    View localView = getChildAt(0);
    int j = 0;
    if (localView != null)
      j = localView.getTop();
    int k = i + -1;
    int m = getPaddingTop();
    int n = j - m;
    setSelectionFromTop(k, n);
  }

  private int adjustScroll(int paramInt1, View paramView, int paramInt2, int paramInt3)
  {
    int i = 0;
    int j = getChildHeight(paramInt1);
    int k = paramView.getHeight();
    int m = calcItemHeight(paramInt1, j);
    int n = k;
    int i1 = m;
    int i2 = this.mSrcPos;
    if (paramInt1 != i2)
    {
      n -= j;
      i1 -= j;
    }
    int i3 = this.mFloatViewHeight;
    int i4 = this.mSrcPos;
    int i5 = this.mFirstExpPos;
    if (i4 != i5)
    {
      int i6 = this.mSrcPos;
      int i7 = this.mSecondExpPos;
      if (i6 != i7)
      {
        int i8 = this.mItemHeightCollapsed;
        i3 -= i8;
      }
    }
    if (paramInt1 <= paramInt2)
    {
      int i9 = this.mFirstExpPos;
      if (paramInt1 > i9)
      {
        int i10 = i3 - i1;
        i = 0 + i10;
      }
    }
    while (true)
    {
      return i;
      if (paramInt1 != paramInt3)
      {
        int i11 = this.mFirstExpPos;
        if (paramInt1 <= i11)
        {
          int i12 = n - i3;
          i = 0 + i12;
        }
        else
        {
          int i13 = this.mSecondExpPos;
          if (paramInt1 != i13)
          {
            int i14 = k - m;
            i = 0 + i14;
          }
          else
          {
            i = 0 + n;
          }
        }
      }
      else
      {
        int i15 = this.mFirstExpPos;
        if (paramInt1 <= i15)
        {
          i = 0 - i3;
        }
        else
        {
          int i16 = this.mSecondExpPos;
          if (paramInt1 != i16)
            i = 0 - i1;
        }
      }
    }
  }

  private int calcItemHeight(int paramInt1, int paramInt2)
  {
    int i = getDividerHeight();
    int m;
    int i2;
    int i3;
    int i8;
    if (this.mAnimate)
    {
      int j = this.mFirstExpPos;
      int k = this.mSecondExpPos;
      if (j != k)
      {
        m = 1;
        int n = this.mFloatViewHeight;
        int i1 = this.mItemHeightCollapsed;
        i2 = n - i1;
        float f1 = this.mSlideFrac;
        float f2 = i2;
        i3 = (int)(f1 * f2);
        int i4 = this.mSrcPos;
        if (paramInt1 == i4)
          break label179;
        int i5 = this.mSrcPos;
        int i6 = this.mFirstExpPos;
        if (i5 == i6)
          break label139;
        if (m == 0)
          break label130;
        int i7 = this.mItemHeightCollapsed;
        i8 = i3 + i7;
      }
    }
    while (true)
    {
      return i8;
      m = 0;
      break;
      label130: i8 = this.mFloatViewHeight;
      continue;
      label139: int i9 = this.mSrcPos;
      int i10 = this.mSecondExpPos;
      if (i9 != i10)
      {
        i8 = this.mFloatViewHeight - i3;
      }
      else
      {
        i8 = this.mItemHeightCollapsed;
        continue;
        label179: int i11 = this.mFirstExpPos;
        if (paramInt1 != i11)
        {
          if (m != 0)
            i8 = paramInt2 + i3;
          else
            i8 = paramInt2 + i2;
        }
        else
        {
          int i12 = this.mSecondExpPos;
          if (paramInt1 != i12)
            i8 = paramInt2 + i2 - i3;
          else
            i8 = paramInt2;
        }
      }
    }
  }

  private int calcItemHeight(int paramInt, View paramView, boolean paramBoolean)
  {
    int i = getChildHeight(paramInt, paramView, paramBoolean);
    return calcItemHeight(paramInt, i);
  }

  private void clearPositions()
  {
    this.mSrcPos = -1;
    this.mFirstExpPos = -1;
    this.mSecondExpPos = -1;
    this.mFloatPos = -1;
  }

  private void continueDrag(int paramInt1, int paramInt2)
  {
    Point localPoint1 = this.mFloatLoc;
    int i = this.mDragDeltaX;
    int j = paramInt1 - i;
    localPoint1.x = j;
    Point localPoint2 = this.mFloatLoc;
    int k = this.mDragDeltaY;
    int m = paramInt2 - k;
    localPoint2.y = m;
    doDragFloatView(true);
    int n = this.mFloatViewMid;
    int i1 = this.mFloatViewHeightHalf;
    int i2 = n + i1;
    int i3 = Math.min(paramInt2, i2);
    int i4 = this.mFloatViewMid;
    int i5 = this.mFloatViewHeightHalf;
    int i6 = i4 - i5;
    int i7 = Math.max(paramInt2, i6);
    int i8 = this.mDragScroller.getScrollDir();
    if (((this.mDragFlags & 0x8) == 0) && ((this.mDragFlags & 0x4) == 0))
    {
      if (i8 == -1)
        return;
      this.mDragScroller.stopScrolling(true);
      return;
    }
    int i9 = this.mLastY;
    if (i3 > i9)
    {
      int i10 = this.mDownScrollStartY;
      if ((i3 > i10) && (i8 != 1))
      {
        if (i8 != -1)
          this.mDragScroller.stopScrolling(true);
        this.mDragScroller.startScrolling(1);
        return;
      }
    }
    int i11 = this.mLastY;
    if (i7 < i11)
    {
      int i12 = this.mUpScrollStartY;
      if ((i7 < i12) && (i8 != 0))
      {
        if (i8 != -1)
          this.mDragScroller.stopScrolling(true);
        this.mDragScroller.startScrolling(0);
        return;
      }
    }
    int i13 = this.mUpScrollStartY;
    if (i7 < i13)
      return;
    int i14 = this.mDownScrollStartY;
    if (i3 > i14)
      return;
    if (!this.mDragScroller.isScrolling())
      return;
    this.mDragScroller.stopScrolling(true);
  }

  private void destroyFloatView()
  {
    if (this.mFloatView == null)
      return;
    this.mFloatView.setVisibility(8);
    if (this.mFloatViewManager != null)
    {
      FloatViewManager localFloatViewManager = this.mFloatViewManager;
      View localView = this.mFloatView;
      localFloatViewManager.onDestroyFloatView(localView);
    }
    this.mFloatView = null;
    invalidate();
  }

  private void doActionUpOrCancel()
  {
    this.mCancelMethod = 0;
    this.mInTouchEvent = false;
    if (this.mDragState == 3)
      this.mDragState = 0;
    float f = this.mFloatAlpha;
    this.mCurrFloatAlpha = f;
    this.mListViewIntercepted = false;
    this.mChildHeightCache.clear();
  }

  private void doDragFloatView(int paramInt, View paramView, boolean paramBoolean)
  {
    this.mBlockLayoutRequests = true;
    updateFloatView();
    int i = this.mFirstExpPos;
    int j = this.mSecondExpPos;
    boolean bool = updatePositions();
    if (bool)
    {
      adjustAllItems();
      int k = adjustScroll(paramInt, paramView, i, j);
      int m = paramView.getTop() + k;
      int n = getPaddingTop();
      int i1 = m - n;
      setSelectionFromTop(paramInt, i1);
      layoutChildren();
    }
    if ((bool) || (paramBoolean))
      invalidate();
    this.mBlockLayoutRequests = false;
  }

  private void doDragFloatView(boolean paramBoolean)
  {
    int i = getFirstVisiblePosition();
    int j = getChildCount() / 2;
    int k = i + j;
    int m = getChildCount() / 2;
    View localView = getChildAt(m);
    if (localView == null)
      return;
    doDragFloatView(k, localView, paramBoolean);
  }

  private void doRemoveItem()
  {
    int i = this.mSrcPos;
    int j = getHeaderViewsCount();
    int k = i - j;
    doRemoveItem(k);
  }

  private void doRemoveItem(int paramInt)
  {
    this.mDragState = 1;
    if (this.mRemoveListener != null)
      this.mRemoveListener.remove(paramInt);
    destroyFloatView();
    adjustOnReorder();
    clearPositions();
    if (this.mInTouchEvent)
    {
      this.mDragState = 3;
      return;
    }
    this.mDragState = 0;
  }

  private void drawDivider(int paramInt, Canvas paramCanvas)
  {
    Drawable localDrawable = getDivider();
    int i = getDividerHeight();
    if (localDrawable == null)
      return;
    if (i == 0)
      return;
    int j = getFirstVisiblePosition();
    int k = paramInt - j;
    ViewGroup localViewGroup = (ViewGroup)getChildAt(k);
    if (localViewGroup == null)
      return;
    int m = getPaddingLeft();
    int n = getWidth();
    int i1 = getPaddingRight();
    int i2 = n - i1;
    int i3 = localViewGroup.getChildAt(0).getHeight();
    int i4 = this.mSrcPos;
    int i5;
    int i6;
    if (paramInt > i4)
    {
      i5 = localViewGroup.getTop() + i3;
      i6 = i5 + i;
    }
    while (true)
    {
      int i7 = paramCanvas.save();
      boolean bool = paramCanvas.clipRect(m, i5, i2, i6);
      localDrawable.setBounds(m, i5, i2, i6);
      localDrawable.draw(paramCanvas);
      paramCanvas.restore();
      return;
      i6 = localViewGroup.getBottom() - i3;
      i5 = i6 - i;
    }
  }

  private void dropFloatView()
  {
    this.mDragState = 2;
    if ((this.mDropListener != null) && (this.mFloatPos >= 0))
    {
      int i = this.mFloatPos;
      int j = getCount();
      if (i < j)
      {
        int k = getHeaderViewsCount();
        DropListener localDropListener = this.mDropListener;
        int m = this.mSrcPos - k;
        int n = this.mFloatPos - k;
        localDropListener.drop(m, n);
      }
    }
    destroyFloatView();
    adjustOnReorder();
    clearPositions();
    adjustAllItems();
    if (this.mInTouchEvent)
    {
      this.mDragState = 3;
      return;
    }
    this.mDragState = 0;
  }

  private int getChildHeight(int paramInt)
  {
    int i = 0;
    int j = this.mSrcPos;
    if (paramInt != j);
    View localView1;
    do
    {
      while (true)
      {
        return i;
        int k = getFirstVisiblePosition();
        int m = paramInt - k;
        localView1 = getChildAt(m);
        if (localView1 == null)
          break;
        i = getChildHeight(paramInt, localView1, false);
      }
      i = this.mChildHeightCache.get(paramInt);
    }
    while (i != -1);
    ListAdapter localListAdapter = getAdapter();
    int n = localListAdapter.getItemViewType(paramInt);
    int i1 = localListAdapter.getViewTypeCount();
    int i2 = this.mSampleViewTypes.length;
    if (i1 != i2)
    {
      View[] arrayOfView = new View[i1];
      this.mSampleViewTypes = arrayOfView;
    }
    if (n >= 0)
      if (this.mSampleViewTypes[n] == null)
      {
        localView1 = localListAdapter.getView(paramInt, null, this);
        this.mSampleViewTypes[n] = localView1;
      }
    while (true)
    {
      i = getChildHeight(paramInt, localView1, true);
      this.mChildHeightCache.add(paramInt, i);
      break;
      View localView2 = this.mSampleViewTypes[n];
      localView1 = localListAdapter.getView(paramInt, localView2, this);
      continue;
      localView1 = localListAdapter.getView(paramInt, null, this);
    }
  }

  private int getChildHeight(int paramInt, View paramView, boolean paramBoolean)
  {
    int i = 0;
    int j = this.mSrcPos;
    if (paramInt != j);
    while (true)
    {
      return i;
      int k = getHeaderViewsCount();
      if (paramInt >= k)
      {
        int m = getCount();
        int n = getFooterViewsCount();
        int i1 = m - n;
        if (paramInt < i1)
          break label88;
      }
      label88: for (View localView = paramView; ; localView = ((ViewGroup)paramView).getChildAt(0))
      {
        ViewGroup.LayoutParams localLayoutParams = localView.getLayoutParams();
        if ((localLayoutParams == null) || (localLayoutParams.height <= 0))
          break label101;
        i = localLayoutParams.height;
        break;
      }
      label101: i = localView.getHeight();
      if ((i == 0) || (paramBoolean))
      {
        measureItem(localView);
        i = localView.getMeasuredHeight();
      }
    }
  }

  private int getItemHeight(int paramInt)
  {
    int i = getFirstVisiblePosition();
    int j = paramInt - i;
    View localView = getChildAt(j);
    if (localView != null);
    int m;
    for (int k = localView.getHeight(); ; k = calcItemHeight(paramInt, m))
    {
      return k;
      m = getChildHeight(paramInt);
    }
  }

  private int getShuffleEdge(int paramInt1, int paramInt2)
  {
    int i = getHeaderViewsCount();
    int j = getFooterViewsCount();
    int m;
    if (paramInt1 > i)
    {
      int k = getCount() - j;
      if (paramInt1 < k);
    }
    else
    {
      m = paramInt2;
    }
    while (true)
    {
      return m;
      int n = getDividerHeight();
      int i1 = this.mFloatViewHeight;
      int i2 = this.mItemHeightCollapsed;
      int i3 = i1 - i2;
      int i4 = getChildHeight(paramInt1);
      int i5 = getItemHeight(paramInt1);
      int i6 = paramInt2;
      int i7 = this.mSecondExpPos;
      int i8 = this.mSrcPos;
      if (i7 <= i8)
      {
        int i9 = this.mSecondExpPos;
        if (paramInt1 != i9)
        {
          int i10 = this.mFirstExpPos;
          int i11 = this.mSecondExpPos;
          if (i10 != i11)
          {
            int i12 = this.mSrcPos;
            if (paramInt1 != i12)
            {
              int i13 = paramInt2 + i5;
              int i14 = this.mFloatViewHeight;
              i6 = i13 - i14;
            }
          }
        }
      }
      while (true)
      {
        int i15 = this.mSrcPos;
        if (paramInt1 > i15)
          break label346;
        int i16 = this.mFloatViewHeight - n;
        int i17 = paramInt1 + -1;
        int i18 = getChildHeight(i17);
        int i19 = (i16 - i18) / 2;
        m = i6 + i19;
        break;
        int i20 = i5 - i4;
        i6 = paramInt2 + i20 - i3;
        continue;
        int i21 = this.mSecondExpPos;
        if (paramInt1 > i21)
        {
          int i22 = this.mSrcPos;
          if (paramInt1 <= i22)
          {
            i6 = paramInt2 - i3;
            continue;
            int i23 = this.mSrcPos;
            if (paramInt1 > i23)
            {
              int i24 = this.mFirstExpPos;
              if (paramInt1 <= i24)
                i6 = paramInt2 + i3;
            }
            else
            {
              int i25 = this.mSecondExpPos;
              if (paramInt1 != i25)
              {
                int i26 = this.mFirstExpPos;
                int i27 = this.mSecondExpPos;
                if (i26 != i27)
                {
                  int i28 = i5 - i4;
                  i6 = paramInt2 + i28;
                }
              }
            }
          }
        }
      }
      label346: int i29 = i4 - n;
      int i30 = this.mFloatViewHeight;
      int i31 = (i29 - i30) / 2;
      m = i6 + i31;
    }
  }

  private void measureFloatView()
  {
    if (this.mFloatView == null)
      return;
    View localView = this.mFloatView;
    measureItem(localView);
    int i = this.mFloatView.getMeasuredHeight();
    this.mFloatViewHeight = i;
    int j = this.mFloatViewHeight / 2;
    this.mFloatViewHeightHalf = j;
  }

  private void measureItem(View paramView)
  {
    Object localObject = paramView.getLayoutParams();
    if (localObject == null)
    {
      localObject = new AbsListView.LayoutParams(-1, -1);
      paramView.setLayoutParams((ViewGroup.LayoutParams)localObject);
    }
    int i = this.mWidthMeasureSpec;
    int j = getListPaddingLeft();
    int k = getListPaddingRight();
    int m = j + k;
    int n = ((ViewGroup.LayoutParams)localObject).width;
    int i1 = ViewGroup.getChildMeasureSpec(i, m, n);
    if (((ViewGroup.LayoutParams)localObject).height > 0);
    for (int i2 = View.MeasureSpec.makeMeasureSpec(((ViewGroup.LayoutParams)localObject).height, 1073741824); ; i2 = View.MeasureSpec.makeMeasureSpec(0, 0))
    {
      paramView.measure(i1, i2);
      return;
    }
  }

  private void saveTouchCoords(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction() & 0xFF;
    if (i != 0)
    {
      int j = this.mX;
      this.mLastX = j;
      int k = this.mY;
      this.mLastY = k;
    }
    int m = (int)paramMotionEvent.getX();
    this.mX = m;
    int n = (int)paramMotionEvent.getY();
    this.mY = n;
    if (i == 0)
    {
      int i1 = this.mX;
      this.mLastX = i1;
      int i2 = this.mY;
      this.mLastY = i2;
    }
    int i3 = (int)paramMotionEvent.getRawX();
    int i4 = this.mX;
    int i5 = i3 - i4;
    this.mOffsetX = i5;
    int i6 = (int)paramMotionEvent.getRawY();
    int i7 = this.mY;
    int i8 = i6 - i7;
    this.mOffsetY = i8;
  }

  private void updateFloatView()
  {
    if (this.mFloatViewManager != null)
    {
      Point localPoint1 = this.mTouchLoc;
      int i = this.mX;
      int j = this.mY;
      localPoint1.set(i, j);
      FloatViewManager localFloatViewManager = this.mFloatViewManager;
      View localView = this.mFloatView;
      Point localPoint2 = this.mFloatLoc;
      Point localPoint3 = this.mTouchLoc;
      localFloatViewManager.onDragFloatView(localView, localPoint2, localPoint3);
    }
    int k = this.mFloatLoc.x;
    int m = this.mFloatLoc.y;
    int n = getPaddingLeft();
    int i11;
    if (((this.mDragFlags & 0x1) == 0) && (k > n))
    {
      this.mFloatLoc.x = n;
      int i1 = getHeaderViewsCount();
      int i2 = getFooterViewsCount();
      int i3 = getFirstVisiblePosition();
      int i4 = getLastVisiblePosition();
      int i5 = getPaddingTop();
      if (i3 < i1)
      {
        int i6 = i1 - i3 + -1;
        i5 = getChildAt(i6).getBottom();
      }
      if ((this.mDragFlags & 0x8) == 0)
      {
        int i7 = this.mSrcPos;
        if (i3 <= i7)
        {
          int i8 = this.mSrcPos - i3;
          i5 = Math.max(getChildAt(i8).getTop(), i5);
        }
      }
      int i9 = getHeight();
      int i10 = getPaddingBottom();
      i11 = i9 - i10;
      int i12 = getCount() - i2 + -1;
      if (i4 >= i12)
      {
        int i13 = getCount() - i2 + -1 - i3;
        i11 = getChildAt(i13).getBottom();
      }
      if ((this.mDragFlags & 0x4) == 0)
      {
        int i14 = this.mSrcPos;
        if (i4 >= i14)
        {
          int i15 = this.mSrcPos - i3;
          i11 = Math.min(getChildAt(i15).getBottom(), i11);
        }
      }
      if (m >= i5)
        break label404;
      this.mFloatLoc.y = i5;
    }
    while (true)
    {
      int i16 = this.mFloatLoc.y;
      int i17 = this.mFloatViewHeightHalf;
      int i18 = i16 + i17;
      this.mFloatViewMid = i18;
      return;
      if (((this.mDragFlags & 0x2) != 0) || (k >= n))
        break;
      this.mFloatLoc.x = n;
      break;
      label404: if (this.mFloatViewHeight + m > i11)
      {
        Point localPoint4 = this.mFloatLoc;
        int i19 = this.mFloatViewHeight;
        int i20 = i11 - i19;
        localPoint4.y = i20;
      }
    }
  }

  private boolean updatePositions()
  {
    int i = getFirstVisiblePosition();
    int j = this.mFirstExpPos;
    int k = j - i;
    DragSortListView localDragSortListView1 = this;
    int m = k;
    View localView = localDragSortListView1.getChildAt(m);
    if (localView == null)
    {
      int n = getChildCount() / 2;
      j = i + n;
      int i1 = j - i;
      DragSortListView localDragSortListView2 = this;
      int i2 = i1;
      localView = localDragSortListView2.getChildAt(i2);
    }
    int i3 = localView.getTop();
    int i4 = localView.getHeight();
    DragSortListView localDragSortListView3 = this;
    int i5 = j;
    int i6 = i3;
    int i7 = localDragSortListView3.getShuffleEdge(i5, i6);
    int i8 = i7;
    int i9 = getDividerHeight();
    int i10 = j;
    int i11 = i3;
    label164: int i12;
    int i13;
    boolean bool;
    int i14;
    int i15;
    float f1;
    int i17;
    int i18;
    label231: float f5;
    int i21;
    if (this.mFloatViewMid < i7)
      if (i10 >= 0)
      {
        i10 += -1;
        i4 = getItemHeight(i10);
        if (i10 == 0)
          i7 = i11 - i9 - i4;
      }
      else
      {
        i12 = getHeaderViewsCount();
        i13 = getFooterViewsCount();
        bool = false;
        i14 = this.mFirstExpPos;
        i15 = this.mSecondExpPos;
        f1 = this.mSlideFrac;
        if (!this.mAnimate)
          break label742;
        int i16 = Math.abs(i7 - i8);
        if (this.mFloatViewMid >= i7)
          break label641;
        i17 = i7;
        i18 = i8;
        float f2 = this.mSlideRegionFrac;
        float f3 = 0.5F * f2;
        float f4 = i16;
        int i19 = (int)(f3 * f4);
        f5 = i19;
        int i20 = i18 + i19;
        i21 = i17 - i19;
        int i22 = this.mFloatViewMid;
        int i23 = i20;
        if (i22 >= i23)
          break label652;
        int i24 = i10 + -1;
        this.mFirstExpPos = i24;
        this.mSecondExpPos = i10;
        int i25 = this.mFloatViewMid;
        float f6 = i20 - i25;
        float f7 = 0.5F * f6 / f5;
        this.mSlideFrac = f7;
        label342: if (this.mFirstExpPos >= i12)
          break label757;
        i10 = i12;
        this.mFirstExpPos = i10;
        this.mSecondExpPos = i10;
      }
    while (true)
    {
      int i26 = this.mFirstExpPos;
      int i27 = i14;
      if (i26 != i27)
      {
        int i28 = this.mSecondExpPos;
        int i29 = i15;
        if ((i28 != i29) && (this.mSlideFrac == f1));
      }
      else
      {
        bool = true;
      }
      int i30 = this.mFloatPos;
      if (i10 != i30)
      {
        if (this.mDragListener != null)
        {
          DragListener localDragListener = this.mDragListener;
          int i31 = this.mFloatPos - i12;
          int i32 = i10 - i12;
          localDragListener.drag(i31, i32);
        }
        this.mFloatPos = i10;
        bool = true;
      }
      return bool;
      int i33 = i4 + i9;
      i11 -= i33;
      i7 = getShuffleEdge(i10, i11);
      if (this.mFloatViewMid >= i7)
        break label164;
      i8 = i7;
      break;
      int i34 = getCount();
      while (true)
      {
        if (i10 >= i34)
          break label639;
        int i35 = i34 + -1;
        if (i10 != i35)
        {
          i7 = i11 + i9 + i4;
          break;
        }
        int i36 = i9 + i4;
        i11 += i36;
        int i37 = i10 + 1;
        DragSortListView localDragSortListView4 = this;
        int i38 = i37;
        i4 = localDragSortListView4.getItemHeight(i38);
        int i39 = i10 + 1;
        DragSortListView localDragSortListView5 = this;
        int i40 = i39;
        i7 = localDragSortListView5.getShuffleEdge(i40, i11);
        if (this.mFloatViewMid < i7)
          break;
        i8 = i7;
        i10 += 1;
      }
      label639: break label164;
      label641: i18 = i7;
      i17 = i8;
      break label231;
      label652: int i41 = this.mFloatViewMid;
      int i42 = i21;
      if (i41 < i42)
      {
        this.mFirstExpPos = i10;
        this.mSecondExpPos = i10;
        break label342;
      }
      this.mFirstExpPos = i10;
      int i43 = i10 + 1;
      this.mSecondExpPos = i43;
      int i44 = this.mFloatViewMid;
      float f8 = (i17 - i44) / f5;
      float f9 = 1.0F + f8;
      float f10 = 0.5F * f9;
      this.mSlideFrac = f10;
      break label342;
      label742: this.mFirstExpPos = i10;
      this.mSecondExpPos = i10;
      break label342;
      label757: int i45 = this.mSecondExpPos;
      int i46 = getCount() - i13;
      int i47 = i45;
      int i48 = i46;
      if (i47 >= i48)
      {
        i10 = getCount() - i13 + -1;
        this.mFirstExpPos = i10;
        this.mSecondExpPos = i10;
      }
    }
  }

  private void updateScrollStarts()
  {
    int i = getPaddingTop();
    int j = getHeight() - i;
    int k = getPaddingBottom();
    int m = j - k;
    float f1 = m;
    float f2 = i;
    float f3 = this.mDragUpScrollStartFrac * f1;
    float f4 = f2 + f3;
    this.mUpScrollStartYF = f4;
    float f5 = i;
    float f6 = this.mDragDownScrollStartFrac;
    float f7 = (1.0F - f6) * f1;
    float f8 = f5 + f7;
    this.mDownScrollStartYF = f8;
    int n = (int)this.mUpScrollStartYF;
    this.mUpScrollStartY = n;
    int i1 = (int)this.mDownScrollStartYF;
    this.mDownScrollStartY = i1;
    float f9 = this.mUpScrollStartYF;
    float f10 = i;
    float f11 = f9 - f10;
    this.mDragUpScrollHeight = f11;
    float f12 = i + m;
    float f13 = this.mDownScrollStartYF;
    float f14 = f12 - f13;
    this.mDragDownScrollHeight = f14;
  }

  public void cancelDrag()
  {
    if (this.mDragState != 4)
      return;
    this.mDragScroller.stopScrolling(true);
    destroyFloatView();
    clearPositions();
    adjustAllItems();
    if (this.mInTouchEvent)
    {
      this.mDragState = 3;
      return;
    }
    this.mDragState = 0;
  }

  protected void dispatchDraw(Canvas paramCanvas)
  {
    super.dispatchDraw(paramCanvas);
    if (this.mDragState != 0)
    {
      int i = this.mFirstExpPos;
      int j = this.mSrcPos;
      if (i != j)
      {
        int k = this.mFirstExpPos;
        drawDivider(k, paramCanvas);
      }
      int m = this.mSecondExpPos;
      int n = this.mFirstExpPos;
      if (m != n)
      {
        int i1 = this.mSecondExpPos;
        int i2 = this.mSrcPos;
        if (i1 != i2)
        {
          int i3 = this.mSecondExpPos;
          drawDivider(i3, paramCanvas);
        }
      }
    }
    if (this.mFloatView == null)
      return;
    int i4 = this.mFloatView.getWidth();
    int i5 = this.mFloatView.getHeight();
    int i6 = this.mFloatLoc.x;
    int i7 = getWidth();
    if (i6 < 0)
      i6 = -i6;
    float f3;
    if (i6 < i7)
    {
      float f1 = i7 - i6;
      float f2 = i7;
      f3 = f1 / f2;
    }
    for (float f4 = f3 * f3; ; f4 = 0.0F)
    {
      float f5 = this.mCurrFloatAlpha;
      int i8 = (int)(255.0F * f5 * f4);
      int i9 = paramCanvas.save();
      float f6 = this.mFloatLoc.x;
      float f7 = this.mFloatLoc.y;
      paramCanvas.translate(f6, f7);
      boolean bool = paramCanvas.clipRect(0, 0, i4, i5);
      float f8 = i4;
      float f9 = i5;
      Canvas localCanvas = paramCanvas;
      float f10 = 0.0F;
      int i10 = localCanvas.saveLayerAlpha(0.0F, f10, f8, f9, i8, 31);
      this.mFloatView.draw(paramCanvas);
      paramCanvas.restore();
      paramCanvas.restore();
      return;
    }
  }

  public boolean isDragEnabled()
  {
    return this.mDragEnabled;
  }

  protected void layoutChildren()
  {
    super.layoutChildren();
    if (this.mFloatView == null)
      return;
    if ((this.mFloatView.isLayoutRequested()) && (!this.mFloatViewOnMeasured))
      measureFloatView();
    View localView = this.mFloatView;
    int i = this.mFloatView.getMeasuredWidth();
    int j = this.mFloatView.getMeasuredHeight();
    localView.layout(0, 0, i, j);
    this.mFloatViewOnMeasured = false;
  }

  public boolean listViewIntercepted()
  {
    return this.mListViewIntercepted;
  }

  protected boolean onDragTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getAction() & 0xFF;
    switch (paramMotionEvent.getAction() & 0xFF)
    {
    default:
    case 3:
    case 1:
    case 2:
    }
    while (true)
    {
      return true;
      if (this.mDragState == 4)
        cancelDrag();
      doActionUpOrCancel();
      continue;
      if (this.mDragState == 4)
        boolean bool = stopDrag(false);
      doActionUpOrCancel();
      continue;
      int j = (int)paramMotionEvent.getX();
      int k = (int)paramMotionEvent.getY();
      continueDrag(j, k);
    }
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (!this.mTrackDragSort)
      return;
    this.mDragSortTracker.appendState();
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool;
    if (!this.mDragEnabled)
      bool = super.onInterceptTouchEvent(paramMotionEvent);
    label153: 
    while (true)
    {
      return bool;
      saveTouchCoords(paramMotionEvent);
      this.mLastCallWasIntercept = true;
      int i = paramMotionEvent.getAction() & 0xFF;
      if (i == 0)
      {
        if (this.mDragState != 0)
        {
          this.mIgnoreTouchEvent = true;
          bool = true;
        }
        else
        {
          this.mInTouchEvent = true;
        }
      }
      else
      {
        bool = false;
        if (this.mFloatView != null)
          bool = true;
        while (true)
        {
          if ((i != 1) && (i != 3))
            break label153;
          this.mInTouchEvent = false;
          break;
          if (super.onInterceptTouchEvent(paramMotionEvent))
            this.mListViewIntercepted = true;
          switch (i)
          {
          case 2:
          default:
            if (bool)
              this.mCancelMethod = 1;
            break;
          case 1:
          case 3:
            doActionUpOrCancel();
            continue;
            this.mCancelMethod = 2;
          }
        }
      }
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    if (this.mFloatView != null)
    {
      if (this.mFloatView.isLayoutRequested())
        measureFloatView();
      this.mFloatViewOnMeasured = true;
    }
    this.mWidthMeasureSpec = paramInt1;
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    updateScrollStarts();
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1;
    if (this.mIgnoreTouchEvent)
    {
      this.mIgnoreTouchEvent = false;
      bool1 = false;
    }
    while (true)
    {
      return bool1;
      if (!this.mDragEnabled)
      {
        bool1 = super.onTouchEvent(paramMotionEvent);
      }
      else
      {
        bool1 = false;
        boolean bool2 = this.mLastCallWasIntercept;
        this.mLastCallWasIntercept = false;
        if (!bool2)
          saveTouchCoords(paramMotionEvent);
        if (this.mDragState == 4)
        {
          boolean bool3 = onDragTouchEvent(paramMotionEvent);
          bool1 = true;
        }
        else
        {
          if ((this.mDragState == 0) && (super.onTouchEvent(paramMotionEvent)));
          switch (paramMotionEvent.getAction() & 0xFF)
          {
          case 2:
          default:
            if (bool1)
              this.mCancelMethod = 1;
            break;
          case 1:
          case 3:
            doActionUpOrCancel();
          }
        }
      }
    }
  }

  public void removeItem(int paramInt)
  {
    this.mUseRemoveVelocity = false;
    removeItem(paramInt, 0.0F);
  }

  public void removeItem(int paramInt, float paramFloat)
  {
    if ((this.mDragState != 0) && (this.mDragState != 4))
      return;
    if (this.mDragState == 0)
    {
      int i = getHeaderViewsCount() + paramInt;
      this.mSrcPos = i;
      int j = this.mSrcPos;
      this.mFirstExpPos = j;
      int k = this.mSrcPos;
      this.mSecondExpPos = k;
      int m = this.mSrcPos;
      this.mFloatPos = m;
      int n = this.mSrcPos;
      int i1 = getFirstVisiblePosition();
      int i2 = n - i1;
      View localView = getChildAt(i2);
      if (localView != null)
        localView.setVisibility(4);
    }
    this.mDragState = 1;
    this.mRemoveVelocityX = paramFloat;
    if (this.mInTouchEvent)
      switch (this.mCancelMethod)
      {
      default:
      case 1:
      case 2:
      }
    while (this.mRemoveAnimator != null)
    {
      this.mRemoveAnimator.start();
      return;
      MotionEvent localMotionEvent1 = this.mCancelEvent;
      boolean bool1 = super.onTouchEvent(localMotionEvent1);
      continue;
      MotionEvent localMotionEvent2 = this.mCancelEvent;
      boolean bool2 = super.onInterceptTouchEvent(localMotionEvent2);
    }
    doRemoveItem(paramInt);
  }

  public void requestLayout()
  {
    if (this.mBlockLayoutRequests)
      return;
    super.requestLayout();
  }

  public void setAdapter(ListAdapter paramListAdapter)
  {
    if (paramListAdapter != null)
    {
      AdapterWrapper localAdapterWrapper1 = new AdapterWrapper(paramListAdapter);
      this.mAdapterWrapper = localAdapterWrapper1;
      DataSetObserver localDataSetObserver = this.mObserver;
      paramListAdapter.registerDataSetObserver(localDataSetObserver);
      if ((paramListAdapter instanceof DropListener))
      {
        DropListener localDropListener = (DropListener)paramListAdapter;
        setDropListener(localDropListener);
      }
      if ((paramListAdapter instanceof DragListener))
      {
        DragListener localDragListener = (DragListener)paramListAdapter;
        setDragListener(localDragListener);
      }
      if ((paramListAdapter instanceof RemoveListener))
      {
        RemoveListener localRemoveListener = (RemoveListener)paramListAdapter;
        setRemoveListener(localRemoveListener);
      }
    }
    while (true)
    {
      AdapterWrapper localAdapterWrapper2 = this.mAdapterWrapper;
      super.setAdapter(localAdapterWrapper2);
      return;
      this.mAdapterWrapper = null;
    }
  }

  public void setDragEnabled(boolean paramBoolean)
  {
    this.mDragEnabled = paramBoolean;
  }

  public void setDragListener(DragListener paramDragListener)
  {
    this.mDragListener = paramDragListener;
  }

  public void setDragScrollProfile(DragScrollProfile paramDragScrollProfile)
  {
    if (paramDragScrollProfile == null)
      return;
    this.mScrollProfile = paramDragScrollProfile;
  }

  public void setDragScrollStart(float paramFloat)
  {
    setDragScrollStarts(paramFloat, paramFloat);
  }

  public void setDragScrollStarts(float paramFloat1, float paramFloat2)
  {
    if (paramFloat2 > 0.5F)
    {
      this.mDragDownScrollStartFrac = 0.5F;
      if (paramFloat1 <= 0.5F)
        break label47;
    }
    label47: for (this.mDragUpScrollStartFrac = 0.5F; ; this.mDragUpScrollStartFrac = paramFloat1)
    {
      if (getHeight() == 0)
        return;
      updateScrollStarts();
      return;
      this.mDragDownScrollStartFrac = paramFloat2;
      break;
    }
  }

  public void setDragSortListener(DragSortListener paramDragSortListener)
  {
    setDropListener(paramDragSortListener);
    setDragListener(paramDragSortListener);
    setRemoveListener(paramDragSortListener);
  }

  public void setDropListener(DropListener paramDropListener)
  {
    this.mDropListener = paramDropListener;
  }

  public void setFloatAlpha(float paramFloat)
  {
    this.mCurrFloatAlpha = paramFloat;
  }

  public void setFloatViewManager(FloatViewManager paramFloatViewManager)
  {
    this.mFloatViewManager = paramFloatViewManager;
  }

  public void setMaxScrollSpeed(float paramFloat)
  {
    this.mMaxScrollSpeed = paramFloat;
  }

  public void setRemoveListener(RemoveListener paramRemoveListener)
  {
    this.mRemoveListener = paramRemoveListener;
  }

  public boolean startDrag(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool = false;
    if ((!this.mInTouchEvent) || (this.mFloatViewManager == null));
    while (true)
    {
      return bool;
      View localView = this.mFloatViewManager.onCreateFloatView(paramInt1);
      if (localView != null)
      {
        DragSortListView localDragSortListView = this;
        int i = paramInt1;
        int j = paramInt2;
        int k = paramInt3;
        int m = paramInt4;
        bool = localDragSortListView.startDrag(i, localView, j, k, m);
      }
    }
  }

  public boolean startDrag(int paramInt1, View paramView, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool1 = true;
    if ((this.mDragState != 0) || (!this.mInTouchEvent) || (this.mFloatView != null) || (paramView == null) || (!this.mDragEnabled))
    {
      bool1 = false;
      return bool1;
    }
    if (getParent() != null)
      getParent().requestDisallowInterceptTouchEvent(true);
    int i = getHeaderViewsCount();
    int j = paramInt1 + i;
    this.mFirstExpPos = j;
    this.mSecondExpPos = j;
    this.mSrcPos = j;
    this.mFloatPos = j;
    this.mDragState = 4;
    this.mDragFlags = 0;
    int k = this.mDragFlags | paramInt2;
    this.mDragFlags = k;
    this.mFloatView = paramView;
    measureFloatView();
    this.mDragDeltaX = paramInt3;
    this.mDragDeltaY = paramInt4;
    int m = this.mY;
    this.mDragStartY = m;
    Point localPoint1 = this.mFloatLoc;
    int n = this.mX;
    int i1 = this.mDragDeltaX;
    int i2 = n - i1;
    localPoint1.x = i2;
    Point localPoint2 = this.mFloatLoc;
    int i3 = this.mY;
    int i4 = this.mDragDeltaY;
    int i5 = i3 - i4;
    localPoint2.y = i5;
    int i6 = this.mSrcPos;
    int i7 = getFirstVisiblePosition();
    int i8 = i6 - i7;
    View localView = getChildAt(i8);
    if (localView != null)
      localView.setVisibility(4);
    if (this.mTrackDragSort)
      this.mDragSortTracker.startTracking();
    switch (this.mCancelMethod)
    {
    default:
    case 1:
    case 2:
    }
    while (true)
    {
      requestLayout();
      if (this.mLiftAnimator == null)
        break;
      this.mLiftAnimator.start();
      break;
      MotionEvent localMotionEvent1 = this.mCancelEvent;
      boolean bool2 = super.onTouchEvent(localMotionEvent1);
      continue;
      MotionEvent localMotionEvent2 = this.mCancelEvent;
      boolean bool3 = super.onInterceptTouchEvent(localMotionEvent2);
    }
  }

  public boolean stopDrag(boolean paramBoolean)
  {
    this.mUseRemoveVelocity = false;
    return stopDrag(paramBoolean, 0.0F);
  }

  public boolean stopDrag(boolean paramBoolean, float paramFloat)
  {
    boolean bool = true;
    if (this.mFloatView != null)
    {
      this.mDragScroller.stopScrolling(true);
      if (paramBoolean)
      {
        int i = this.mSrcPos;
        int j = getHeaderViewsCount();
        int k = i - j;
        removeItem(k, paramFloat);
        if (this.mTrackDragSort)
          this.mDragSortTracker.stopTracking();
      }
    }
    while (true)
    {
      return bool;
      if (this.mDropAnimator != null)
      {
        this.mDropAnimator.start();
        break;
      }
      dropFloatView();
      break;
      bool = false;
    }
  }

  public boolean stopDragWithVelocity(boolean paramBoolean, float paramFloat)
  {
    this.mUseRemoveVelocity = true;
    return stopDrag(paramBoolean, paramFloat);
  }

  private class DragSortTracker
  {
    StringBuilder mBuilder;
    File mFile;
    private int mNumFlushes;
    private int mNumInBuffer;
    private boolean mTracking;

    public DragSortTracker()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      this.mBuilder = localStringBuilder;
      this.mNumInBuffer = 0;
      this.mNumFlushes = 0;
      this.mTracking = false;
      File localFile1 = Environment.getExternalStorageDirectory();
      File localFile2 = new File(localFile1, "dslv_state.txt");
      this.mFile = localFile2;
      if (this.mFile.exists())
        return;
      try
      {
        boolean bool = this.mFile.createNewFile();
        int i = Log.d("mobeta", "file created");
        return;
      }
      catch (IOException localIOException)
      {
        int j = Log.w("mobeta", "Could not create dslv_state.txt");
        String str = localIOException.getMessage();
        int k = Log.d("mobeta", str);
      }
    }

    public void appendState()
    {
      if (!this.mTracking)
        return;
      StringBuilder localStringBuilder1 = this.mBuilder.append("<DSLVState>\n");
      int i = DragSortListView.this.getChildCount();
      int j = DragSortListView.this.getFirstVisiblePosition();
      StringBuilder localStringBuilder2 = this.mBuilder.append("    <Positions>");
      int k = 0;
      while (k < i)
      {
        StringBuilder localStringBuilder3 = this.mBuilder;
        int m = j + k;
        StringBuilder localStringBuilder4 = localStringBuilder3.append(m).append(",");
        k += 1;
      }
      StringBuilder localStringBuilder5 = this.mBuilder.append("</Positions>\n");
      StringBuilder localStringBuilder6 = this.mBuilder.append("    <Tops>");
      int n = 0;
      while (n < i)
      {
        StringBuilder localStringBuilder7 = this.mBuilder;
        int i1 = DragSortListView.this.getChildAt(n).getTop();
        StringBuilder localStringBuilder8 = localStringBuilder7.append(i1).append(",");
        n += 1;
      }
      StringBuilder localStringBuilder9 = this.mBuilder.append("</Tops>\n");
      StringBuilder localStringBuilder10 = this.mBuilder.append("    <Bottoms>");
      int i2 = 0;
      while (i2 < i)
      {
        StringBuilder localStringBuilder11 = this.mBuilder;
        int i3 = DragSortListView.this.getChildAt(i2).getBottom();
        StringBuilder localStringBuilder12 = localStringBuilder11.append(i3).append(",");
        i2 += 1;
      }
      StringBuilder localStringBuilder13 = this.mBuilder.append("</Bottoms>\n");
      StringBuilder localStringBuilder14 = this.mBuilder.append("    <FirstExpPos>");
      int i4 = DragSortListView.this.mFirstExpPos;
      StringBuilder localStringBuilder15 = localStringBuilder14.append(i4).append("</FirstExpPos>\n");
      StringBuilder localStringBuilder16 = this.mBuilder.append("    <FirstExpBlankHeight>");
      DragSortListView localDragSortListView1 = DragSortListView.this;
      int i5 = DragSortListView.this.mFirstExpPos;
      int i6 = localDragSortListView1.getItemHeight(i5);
      DragSortListView localDragSortListView2 = DragSortListView.this;
      int i7 = DragSortListView.this.mFirstExpPos;
      int i8 = localDragSortListView2.getChildHeight(i7);
      int i9 = i6 - i8;
      StringBuilder localStringBuilder17 = localStringBuilder16.append(i9).append("</FirstExpBlankHeight>\n");
      StringBuilder localStringBuilder18 = this.mBuilder.append("    <SecondExpPos>");
      int i10 = DragSortListView.this.mSecondExpPos;
      StringBuilder localStringBuilder19 = localStringBuilder18.append(i10).append("</SecondExpPos>\n");
      StringBuilder localStringBuilder20 = this.mBuilder.append("    <SecondExpBlankHeight>");
      DragSortListView localDragSortListView3 = DragSortListView.this;
      int i11 = DragSortListView.this.mSecondExpPos;
      int i12 = localDragSortListView3.getItemHeight(i11);
      DragSortListView localDragSortListView4 = DragSortListView.this;
      int i13 = DragSortListView.this.mSecondExpPos;
      int i14 = localDragSortListView4.getChildHeight(i13);
      int i15 = i12 - i14;
      StringBuilder localStringBuilder21 = localStringBuilder20.append(i15).append("</SecondExpBlankHeight>\n");
      StringBuilder localStringBuilder22 = this.mBuilder.append("    <SrcPos>");
      int i16 = DragSortListView.this.mSrcPos;
      StringBuilder localStringBuilder23 = localStringBuilder22.append(i16).append("</SrcPos>\n");
      StringBuilder localStringBuilder24 = this.mBuilder.append("    <SrcHeight>");
      int i17 = DragSortListView.this.mFloatViewHeight;
      int i18 = DragSortListView.this.getDividerHeight();
      int i19 = i17 + i18;
      StringBuilder localStringBuilder25 = localStringBuilder24.append(i19).append("</SrcHeight>\n");
      StringBuilder localStringBuilder26 = this.mBuilder.append("    <ViewHeight>");
      int i20 = DragSortListView.this.getHeight();
      StringBuilder localStringBuilder27 = localStringBuilder26.append(i20).append("</ViewHeight>\n");
      StringBuilder localStringBuilder28 = this.mBuilder.append("    <LastY>");
      int i21 = DragSortListView.this.mLastY;
      StringBuilder localStringBuilder29 = localStringBuilder28.append(i21).append("</LastY>\n");
      StringBuilder localStringBuilder30 = this.mBuilder.append("    <FloatY>");
      int i22 = DragSortListView.this.mFloatViewMid;
      StringBuilder localStringBuilder31 = localStringBuilder30.append(i22).append("</FloatY>\n");
      StringBuilder localStringBuilder32 = this.mBuilder.append("    <ShuffleEdges>");
      int i23 = 0;
      while (i23 < i)
      {
        StringBuilder localStringBuilder33 = this.mBuilder;
        DragSortListView localDragSortListView5 = DragSortListView.this;
        int i24 = j + i23;
        int i25 = DragSortListView.this.getChildAt(i23).getTop();
        int i26 = localDragSortListView5.getShuffleEdge(i24, i25);
        StringBuilder localStringBuilder34 = localStringBuilder33.append(i26).append(",");
        i23 += 1;
      }
      StringBuilder localStringBuilder35 = this.mBuilder.append("</ShuffleEdges>\n");
      StringBuilder localStringBuilder36 = this.mBuilder.append("</DSLVState>\n");
      int i27 = this.mNumInBuffer + 1;
      this.mNumInBuffer = i27;
      if (this.mNumInBuffer <= 1000)
        return;
      flush();
      this.mNumInBuffer = 0;
    }

    public void flush()
    {
      if (!this.mTracking)
        return;
      boolean bool = true;
      try
      {
        if (this.mNumFlushes == 0)
          bool = false;
        File localFile = this.mFile;
        FileWriter localFileWriter = new FileWriter(localFile, bool);
        String str = this.mBuilder.toString();
        localFileWriter.write(str);
        StringBuilder localStringBuilder1 = this.mBuilder;
        int i = this.mBuilder.length();
        StringBuilder localStringBuilder2 = localStringBuilder1.delete(0, i);
        localFileWriter.flush();
        localFileWriter.close();
        int j = this.mNumFlushes + 1;
        this.mNumFlushes = j;
        return;
      }
      catch (IOException localIOException)
      {
      }
    }

    public void startTracking()
    {
      StringBuilder localStringBuilder = this.mBuilder.append("<DSLVStates>\n");
      this.mNumFlushes = 0;
      this.mTracking = true;
    }

    public void stopTracking()
    {
      if (!this.mTracking)
        return;
      StringBuilder localStringBuilder = this.mBuilder.append("</DSLVStates>\n");
      flush();
      this.mTracking = false;
    }
  }

  private class DragScroller
    implements Runnable
  {
    private float dt;
    private int dy;
    private boolean mAbort;
    private long mCurrTime;
    private long mPrevTime;
    private float mScrollSpeed;
    private boolean mScrolling = false;
    private int scrollDir;
    private long tStart;

    public DragScroller()
    {
    }

    public int getScrollDir()
    {
      if (this.mScrolling);
      for (int i = this.scrollDir; ; i = -1)
        return i;
    }

    public boolean isScrolling()
    {
      return this.mScrolling;
    }

    public void run()
    {
      if (this.mAbort)
      {
        this.mScrolling = false;
        return;
      }
      int i = DragSortListView.this.getFirstVisiblePosition();
      int j = DragSortListView.this.getLastVisiblePosition();
      int k = DragSortListView.this.getCount();
      int m = DragSortListView.this.getPaddingTop();
      int n = DragSortListView.this.getHeight() - m;
      int i1 = DragSortListView.this.getPaddingBottom();
      int i2 = n - i1;
      int i3 = DragSortListView.this.mY;
      int i4 = DragSortListView.this.mFloatViewMid;
      int i5 = DragSortListView.this.mFloatViewHeightHalf;
      int i6 = i4 + i5;
      int i7 = Math.min(i3, i6);
      int i8 = DragSortListView.this.mY;
      int i9 = DragSortListView.this.mFloatViewMid;
      int i10 = DragSortListView.this.mFloatViewHeightHalf;
      int i11 = i9 - i10;
      int i12 = Math.max(i8, i11);
      View localView1;
      if (this.scrollDir == 0)
      {
        localView1 = DragSortListView.this.getChildAt(0);
        if (localView1 == null)
        {
          this.mScrolling = false;
          return;
        }
        if ((i == 0) && (localView1.getTop() != m))
        {
          this.mScrolling = false;
          return;
        }
        DragSortListView.DragScrollProfile localDragScrollProfile1 = DragSortListView.this.mScrollProfile;
        float f1 = DragSortListView.this.mUpScrollStartYF;
        float f2 = i12;
        float f3 = f1 - f2;
        float f4 = DragSortListView.this.mDragUpScrollHeight;
        float f5 = f3 / f4;
        long l1 = this.mPrevTime;
        float f6 = localDragScrollProfile1.getSpeed(f5, l1);
        this.mScrollSpeed = f6;
        long l2 = SystemClock.uptimeMillis();
        this.mCurrTime = l2;
        long l3 = this.mCurrTime;
        long l4 = this.mPrevTime;
        float f7 = (float)(l3 - l4);
        this.dt = f7;
        float f8 = this.mScrollSpeed;
        float f9 = this.dt;
        int i13 = Math.round(f8 * f9);
        this.dy = i13;
        if (this.dy < 0)
          break label668;
        int i14 = this.dy;
        int i15 = Math.min(i2, i14);
        this.dy = i15;
      }
      for (int i16 = i; ; i16 = j)
      {
        DragSortListView localDragSortListView1 = DragSortListView.this;
        int i17 = i16 - i;
        View localView2 = localDragSortListView1.getChildAt(i17);
        int i18 = localView2.getTop();
        int i19 = this.dy;
        int i20 = i18 + i19;
        if ((i16 == 0) && (i20 > m))
          i20 = m;
        boolean bool1 = DragSortListView.access$2602(DragSortListView.this, true);
        DragSortListView localDragSortListView2 = DragSortListView.this;
        int i21 = i20 - m;
        localDragSortListView2.setSelectionFromTop(i16, i21);
        DragSortListView.this.layoutChildren();
        DragSortListView.this.invalidate();
        boolean bool2 = DragSortListView.access$2602(DragSortListView.this, false);
        DragSortListView.this.doDragFloatView(i16, localView2, false);
        long l5 = this.mCurrTime;
        this.mPrevTime = l5;
        boolean bool3 = DragSortListView.this.post(this);
        return;
        DragSortListView localDragSortListView3 = DragSortListView.this;
        int i22 = j - i;
        localView1 = localDragSortListView3.getChildAt(i22);
        if (localView1 == null)
        {
          this.mScrolling = false;
          return;
        }
        int i23 = k + -1;
        if (j != i23)
        {
          int i24 = localView1.getBottom();
          int i25 = i2 + m;
          if (i24 <= i25)
          {
            this.mScrolling = false;
            return;
          }
        }
        DragSortListView.DragScrollProfile localDragScrollProfile2 = DragSortListView.this.mScrollProfile;
        float f10 = i7;
        float f11 = DragSortListView.this.mDownScrollStartYF;
        float f12 = f10 - f11;
        float f13 = DragSortListView.this.mDragDownScrollHeight;
        float f14 = f12 / f13;
        long l6 = this.mPrevTime;
        float f15 = -localDragScrollProfile2.getSpeed(f14, l6);
        this.mScrollSpeed = f15;
        break;
        label668: int i26 = -i2;
        int i27 = this.dy;
        int i28 = Math.max(i26, i27);
        this.dy = i28;
      }
    }

    public void startScrolling(int paramInt)
    {
      if (this.mScrolling)
        return;
      this.mAbort = false;
      this.mScrolling = true;
      long l1 = SystemClock.uptimeMillis();
      this.tStart = l1;
      long l2 = this.tStart;
      this.mPrevTime = l2;
      this.scrollDir = paramInt;
      boolean bool = DragSortListView.this.post(this);
    }

    public void stopScrolling(boolean paramBoolean)
    {
      if (paramBoolean)
      {
        boolean bool = DragSortListView.this.removeCallbacks(this);
        this.mScrolling = false;
        return;
      }
      this.mAbort = true;
    }
  }

  public static abstract interface DragScrollProfile
  {
    public abstract float getSpeed(float paramFloat, long paramLong);
  }

  public static abstract interface DragSortListener extends DragSortListView.DragListener, DragSortListView.DropListener, DragSortListView.RemoveListener
  {
  }

  public static abstract interface RemoveListener
  {
    public abstract void remove(int paramInt);
  }

  public static abstract interface DropListener
  {
    public abstract void drop(int paramInt1, int paramInt2);
  }

  public static abstract interface DragListener
  {
    public abstract void drag(int paramInt1, int paramInt2);
  }

  public static abstract interface FloatViewManager
  {
    public abstract View onCreateFloatView(int paramInt);

    public abstract void onDestroyFloatView(View paramView);

    public abstract void onDragFloatView(View paramView, Point paramPoint1, Point paramPoint2);
  }

  private class RemoveAnimator extends DragSortListView.SmoothAnimator
  {
    private int mFirstChildHeight = -1;
    private int mFirstPos;
    private float mFirstStartBlank;
    private float mFloatLocX;
    private int mSecondChildHeight = -1;
    private int mSecondPos;
    private float mSecondStartBlank;
    private int srcPos;

    public RemoveAnimator(float paramInt, int arg3)
    {
      super(paramInt, i);
    }

    public void onStart()
    {
      int i = -1;
      this.mFirstChildHeight = i;
      this.mSecondChildHeight = i;
      int j = DragSortListView.this.mFirstExpPos;
      this.mFirstPos = j;
      int k = DragSortListView.this.mSecondExpPos;
      this.mSecondPos = k;
      int m = DragSortListView.this.mSrcPos;
      this.srcPos = m;
      int n = DragSortListView.access$102(DragSortListView.this, 1);
      float f1 = DragSortListView.this.mFloatLoc.x;
      this.mFloatLocX = f1;
      if (DragSortListView.this.mUseRemoveVelocity)
      {
        float f2 = DragSortListView.this.getWidth();
        float f3 = 2.0F * f2;
        if (DragSortListView.this.mRemoveVelocityX == 0.0F)
        {
          DragSortListView localDragSortListView1 = DragSortListView.this;
          if (this.mFloatLocX < 0.0F);
          while (true)
          {
            float f4 = i * f3;
            float f5 = DragSortListView.access$1602(localDragSortListView1, f4);
            return;
            i = 1;
          }
        }
        f3 *= 2.0F;
        if (DragSortListView.this.mRemoveVelocityX < 0.0F)
        {
          float f6 = DragSortListView.this.mRemoveVelocityX;
          float f7 = -f3;
          if (f6 > f7)
          {
            DragSortListView localDragSortListView2 = DragSortListView.this;
            float f8 = -f3;
            float f9 = DragSortListView.access$1602(localDragSortListView2, f8);
            return;
          }
        }
        if (DragSortListView.this.mRemoveVelocityX <= 0.0F)
          return;
        if (DragSortListView.this.mRemoveVelocityX >= f3)
          return;
        float f10 = DragSortListView.access$1602(DragSortListView.this, f3);
        return;
      }
      DragSortListView.this.destroyFloatView();
    }

    public void onStop()
    {
      DragSortListView.this.doRemoveItem();
    }

    public void onUpdate(float paramFloat1, float paramFloat2)
    {
      float f1 = 1.0F - paramFloat2;
      int i = DragSortListView.this.getFirstVisiblePosition();
      DragSortListView localDragSortListView1 = DragSortListView.this;
      int j = this.mFirstPos - i;
      View localView = localDragSortListView1.getChildAt(j);
      if (DragSortListView.this.mUseRemoveVelocity)
      {
        long l1 = SystemClock.uptimeMillis();
        long l2 = this.mStartTime;
        float f2 = (float)(l1 - l2) / 1000.0F;
        if (f2 == 0.0F)
          return;
        float f3 = DragSortListView.this.mRemoveVelocityX * f2;
        int k = DragSortListView.this.getWidth();
        DragSortListView localDragSortListView2 = DragSortListView.this;
        if (DragSortListView.this.mRemoveVelocityX > 0.0F);
        for (int m = 1; ; m = -1)
        {
          float f4 = m * f2;
          float f5 = k;
          float f6 = f4 * f5;
          float f7 = DragSortListView.access$1616(localDragSortListView2, f6);
          float f8 = this.mFloatLocX + f3;
          this.mFloatLocX = f8;
          Point localPoint = DragSortListView.this.mFloatLoc;
          int n = (int)this.mFloatLocX;
          localPoint.x = n;
          float f9 = this.mFloatLocX;
          float f10 = k;
          if (f9 >= f10)
            break;
          float f11 = this.mFloatLocX;
          float f12 = -k;
          if (f11 <= f12)
            break;
          long l3 = SystemClock.uptimeMillis();
          this.mStartTime = l3;
          DragSortListView.this.doDragFloatView(true);
          return;
        }
      }
      if (localView != null)
      {
        if (this.mFirstChildHeight == -1)
        {
          DragSortListView localDragSortListView3 = DragSortListView.this;
          int i1 = this.mFirstPos;
          int i2 = localDragSortListView3.getChildHeight(i1, localView, false);
          this.mFirstChildHeight = i2;
          int i3 = localView.getHeight();
          int i4 = this.mFirstChildHeight;
          float f13 = i3 - i4;
          this.mFirstStartBlank = f13;
        }
        int i5 = Math.max((int)(this.mFirstStartBlank * f1), 1);
        ViewGroup.LayoutParams localLayoutParams1 = localView.getLayoutParams();
        int i6 = this.mFirstChildHeight + i5;
        localLayoutParams1.height = i6;
        localView.setLayoutParams(localLayoutParams1);
      }
      int i7 = this.mSecondPos;
      int i8 = this.mFirstPos;
      if (i7 != i8)
        return;
      DragSortListView localDragSortListView4 = DragSortListView.this;
      int i9 = this.mSecondPos - i;
      localView = localDragSortListView4.getChildAt(i9);
      if (localView == null)
        return;
      if (this.mSecondChildHeight == -1)
      {
        DragSortListView localDragSortListView5 = DragSortListView.this;
        int i10 = this.mSecondPos;
        int i11 = localDragSortListView5.getChildHeight(i10, localView, false);
        this.mSecondChildHeight = i11;
        int i12 = localView.getHeight();
        int i13 = this.mSecondChildHeight;
        float f14 = i12 - i13;
        this.mSecondStartBlank = f14;
      }
      int i14 = Math.max((int)(this.mSecondStartBlank * f1), 1);
      ViewGroup.LayoutParams localLayoutParams2 = localView.getLayoutParams();
      int i15 = this.mSecondChildHeight + i14;
      localLayoutParams2.height = i15;
      localView.setLayoutParams(localLayoutParams2);
    }
  }

  private class DropAnimator extends DragSortListView.SmoothAnimator
  {
    private int mDropPos;
    private float mInitDeltaX;
    private float mInitDeltaY;
    private int srcPos;

    public DropAnimator(float paramInt, int arg3)
    {
      super(paramInt, i);
    }

    private int getTargetY()
    {
      int i = DragSortListView.this.getFirstVisiblePosition();
      int j = DragSortListView.this.mItemHeightCollapsed;
      int k = DragSortListView.this.getDividerHeight();
      int m = (j + k) / 2;
      DragSortListView localDragSortListView = DragSortListView.this;
      int n = this.mDropPos - i;
      View localView = localDragSortListView.getChildAt(n);
      int i1 = -1;
      if (localView != null)
      {
        int i2 = this.mDropPos;
        int i3 = this.srcPos;
        if (i2 != i3)
          i1 = localView.getTop();
      }
      while (true)
      {
        return i1;
        int i4 = this.mDropPos;
        int i5 = this.srcPos;
        if (i4 < i5)
        {
          i1 = localView.getTop() - m;
        }
        else
        {
          int i6 = localView.getBottom() + m;
          int i7 = DragSortListView.this.mFloatViewHeight;
          i1 = i6 - i7;
          continue;
          cancel();
        }
      }
    }

    public void onStart()
    {
      int i = DragSortListView.this.mFloatPos;
      this.mDropPos = i;
      int j = DragSortListView.this.mSrcPos;
      this.srcPos = j;
      int k = DragSortListView.access$102(DragSortListView.this, 2);
      int m = DragSortListView.this.mFloatLoc.y;
      int n = getTargetY();
      float f1 = m - n;
      this.mInitDeltaY = f1;
      int i1 = DragSortListView.this.mFloatLoc.x;
      int i2 = DragSortListView.this.getPaddingLeft();
      float f2 = i1 - i2;
      this.mInitDeltaX = f2;
    }

    public void onStop()
    {
      DragSortListView.this.dropFloatView();
    }

    public void onUpdate(float paramFloat1, float paramFloat2)
    {
      int i = getTargetY();
      int j = DragSortListView.this.getPaddingLeft();
      float f1 = DragSortListView.this.mFloatLoc.y - i;
      float f2 = DragSortListView.this.mFloatLoc.x - j;
      float f3 = 1.0F - paramFloat2;
      float f4 = this.mInitDeltaY;
      float f5 = Math.abs(f1 / f4);
      if (f3 >= f5)
      {
        float f6 = this.mInitDeltaX;
        float f7 = Math.abs(f2 / f6);
        if (f3 >= f7)
          return;
      }
      Point localPoint1 = DragSortListView.this.mFloatLoc;
      int k = (int)(this.mInitDeltaY * f3) + i;
      localPoint1.y = k;
      Point localPoint2 = DragSortListView.this.mFloatLoc;
      int m = DragSortListView.this.getPaddingLeft();
      int n = (int)(this.mInitDeltaX * f3);
      int i1 = m + n;
      localPoint2.x = i1;
      DragSortListView.this.doDragFloatView(true);
    }
  }

  private class LiftAnimator extends DragSortListView.SmoothAnimator
  {
    private float mFinalDragDeltaY;
    private float mInitDragDeltaY;

    public LiftAnimator(float paramInt, int arg3)
    {
      super(paramInt, i);
    }

    public void onStart()
    {
      float f1 = DragSortListView.this.mDragDeltaY;
      this.mInitDragDeltaY = f1;
      float f2 = DragSortListView.this.mFloatViewHeightHalf;
      this.mFinalDragDeltaY = f2;
    }

    public void onUpdate(float paramFloat1, float paramFloat2)
    {
      if (DragSortListView.this.mDragState != 4)
      {
        cancel();
        return;
      }
      DragSortListView localDragSortListView = DragSortListView.this;
      float f1 = this.mFinalDragDeltaY * paramFloat2;
      float f2 = 1.0F - paramFloat2;
      float f3 = this.mInitDragDeltaY;
      float f4 = f2 * f3;
      int i = (int)(f1 + f4);
      int j = DragSortListView.access$302(localDragSortListView, i);
      Point localPoint = DragSortListView.this.mFloatLoc;
      int k = DragSortListView.this.mY;
      int m = DragSortListView.this.mDragDeltaY;
      int n = k - m;
      localPoint.y = n;
      DragSortListView.this.doDragFloatView(true);
    }
  }

  private class SmoothAnimator
    implements Runnable
  {
    private float mA;
    private float mAlpha;
    private float mB;
    private float mC;
    private boolean mCanceled;
    private float mD;
    private float mDurationF;
    protected long mStartTime;

    public SmoothAnimator(float paramInt, int arg3)
    {
      this.mAlpha = paramInt;
      int i;
      float f1 = i;
      this.mDurationF = f1;
      float f2 = this.mAlpha * 2.0F;
      float f3 = this.mAlpha;
      float f4 = 1.0F - f3;
      float f5 = f2 * f4;
      float f6 = 1.0F / f5;
      this.mD = f6;
      this.mA = f6;
      float f7 = this.mAlpha;
      float f8 = (this.mAlpha - 1.0F) * 2.0F;
      float f9 = f7 / f8;
      this.mB = f9;
      float f10 = this.mAlpha;
      float f11 = 1.0F - f10;
      float f12 = 1.0F / f11;
      this.mC = f12;
    }

    public void cancel()
    {
      this.mCanceled = true;
    }

    public void onStart()
    {
    }

    public void onStop()
    {
    }

    public void onUpdate(float paramFloat1, float paramFloat2)
    {
    }

    public void run()
    {
      if (this.mCanceled)
        return;
      long l1 = SystemClock.uptimeMillis();
      long l2 = this.mStartTime;
      float f1 = (float)(l1 - l2);
      float f2 = this.mDurationF;
      float f3 = f1 / f2;
      if (f3 >= 1.0F)
      {
        onUpdate(1.0F, 1.0F);
        onStop();
        return;
      }
      float f4 = transform(f3);
      onUpdate(f3, f4);
      boolean bool = DragSortListView.this.post(this);
    }

    public void start()
    {
      long l = SystemClock.uptimeMillis();
      this.mStartTime = l;
      this.mCanceled = false;
      onStart();
      boolean bool = DragSortListView.this.post(this);
    }

    public float transform(float paramFloat)
    {
      float f1 = this.mAlpha;
      float f2;
      if (paramFloat < f1)
        f2 = this.mA * paramFloat * paramFloat;
      while (true)
      {
        return f2;
        float f3 = this.mAlpha;
        float f4 = 1.0F - f3;
        if (paramFloat < f4)
        {
          float f5 = this.mB;
          float f6 = this.mC * paramFloat;
          f2 = f5 + f6;
        }
        else
        {
          float f7 = this.mD;
          float f8 = paramFloat - 1.0F;
          float f9 = f7 * f8;
          float f10 = paramFloat - 1.0F;
          float f11 = f9 * f10;
          f2 = 1.0F - f11;
        }
      }
    }
  }

  private class HeightCache
  {
    private SparseIntArray mMap;
    private int mMaxSize;
    private ArrayList<Integer> mOrder;

    public HeightCache(int arg2)
    {
      int i;
      SparseIntArray localSparseIntArray = new SparseIntArray(i);
      this.mMap = localSparseIntArray;
      ArrayList localArrayList = new ArrayList(i);
      this.mOrder = localArrayList;
      this.mMaxSize = i;
    }

    public void add(int paramInt1, int paramInt2)
    {
      int i = this.mMap.get(paramInt1, -1);
      if (i != paramInt2)
        return;
      if (i == -1)
      {
        int j = this.mMap.size();
        int k = this.mMaxSize;
        if (j != k)
        {
          SparseIntArray localSparseIntArray = this.mMap;
          int m = ((Integer)this.mOrder.remove(0)).intValue();
          localSparseIntArray.delete(m);
        }
      }
      while (true)
      {
        this.mMap.put(paramInt1, paramInt2);
        ArrayList localArrayList1 = this.mOrder;
        Integer localInteger1 = Integer.valueOf(paramInt1);
        boolean bool1 = localArrayList1.add(localInteger1);
        return;
        ArrayList localArrayList2 = this.mOrder;
        Integer localInteger2 = Integer.valueOf(paramInt1);
        boolean bool2 = localArrayList2.remove(localInteger2);
      }
    }

    public void clear()
    {
      this.mMap.clear();
      this.mOrder.clear();
    }

    public int get(int paramInt)
    {
      return this.mMap.get(paramInt, -1);
    }
  }

  private class AdapterWrapper extends BaseAdapter
  {
    private ListAdapter mAdapter;

    public AdapterWrapper(ListAdapter arg2)
    {
      Object localObject;
      this.mAdapter = localObject;
      ListAdapter localListAdapter = this.mAdapter;
      DataSetObserver local1 = new DataSetObserver()
      {
        public void onChanged()
        {
          DragSortListView.AdapterWrapper.this.notifyDataSetChanged();
        }

        public void onInvalidated()
        {
          DragSortListView.AdapterWrapper.this.notifyDataSetInvalidated();
        }
      };
      localListAdapter.registerDataSetObserver(local1);
    }

    public boolean areAllItemsEnabled()
    {
      return this.mAdapter.areAllItemsEnabled();
    }

    public int getCount()
    {
      return this.mAdapter.getCount();
    }

    public Object getItem(int paramInt)
    {
      return this.mAdapter.getItem(paramInt);
    }

    public long getItemId(int paramInt)
    {
      return this.mAdapter.getItemId(paramInt);
    }

    public int getItemViewType(int paramInt)
    {
      return this.mAdapter.getItemViewType(paramInt);
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView != null)
      {
        localObject = (DragSortItemView)paramView;
        View localView1 = ((DragSortItemView)localObject).getChildAt(0);
        ListAdapter localListAdapter1 = this.mAdapter;
        DragSortListView localDragSortListView1 = DragSortListView.this;
        localView2 = localListAdapter1.getView(paramInt, localView1, localDragSortListView1);
        if (localView2 != localView1)
        {
          if (localView1 != null)
            ((DragSortItemView)localObject).removeViewAt(0);
          ((DragSortItemView)localObject).addView(localView2);
        }
        DragSortListView localDragSortListView2 = DragSortListView.this;
        int i = DragSortListView.this.getHeaderViewsCount() + paramInt;
        localDragSortListView2.adjustItem(i, (View)localObject, true);
        return localObject;
      }
      ListAdapter localListAdapter2 = this.mAdapter;
      DragSortListView localDragSortListView3 = DragSortListView.this;
      View localView2 = localListAdapter2.getView(paramInt, null, localDragSortListView3);
      Context localContext1;
      if ((localView2 instanceof Checkable))
        localContext1 = DragSortListView.this.getContext();
      Context localContext2;
      for (Object localObject = new DragSortItemViewCheckable(localContext1); ; localObject = new DragSortItemView(localContext2))
      {
        AbsListView.LayoutParams localLayoutParams = new AbsListView.LayoutParams(-1, -1);
        ((DragSortItemView)localObject).setLayoutParams(localLayoutParams);
        ((DragSortItemView)localObject).addView(localView2);
        break;
        localContext2 = DragSortListView.this.getContext();
      }
    }

    public int getViewTypeCount()
    {
      return this.mAdapter.getViewTypeCount();
    }

    public boolean hasStableIds()
    {
      return this.mAdapter.hasStableIds();
    }

    public boolean isEmpty()
    {
      return this.mAdapter.isEmpty();
    }

    public boolean isEnabled(int paramInt)
    {
      return this.mAdapter.isEnabled(paramInt);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.mobeta.android.dslv.DragSortListView
 * JD-Core Version:    0.6.2
 */