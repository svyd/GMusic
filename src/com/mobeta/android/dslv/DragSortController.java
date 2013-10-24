package com.mobeta.android.dslv;

import android.content.Context;
import android.graphics.Point;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;

public class DragSortController extends SimpleFloatViewManager
  implements GestureDetector.OnGestureListener, View.OnTouchListener
{
  private boolean mCanDrag;
  private int mClickRemoveHitPos = -1;
  private int mClickRemoveId;
  private int mCurrX;
  private int mCurrY;
  private GestureDetector mDetector;
  private int mDragHandleId;
  private int mDragInitMode = 0;
  private boolean mDragging;
  private DragSortListView mDslv;
  private int mFlingHandleId;
  private int mFlingHitPos = -1;
  private GestureDetector mFlingRemoveDetector;
  private GestureDetector.OnGestureListener mFlingRemoveListener;
  private float mFlingSpeed;
  private int mHitPos = -1;
  private boolean mIsRemoving = false;
  private int mItemX;
  private int mItemY;
  private int mPositionX;
  private boolean mRemoveEnabled = false;
  private int mRemoveMode;
  private boolean mSortEnabled = true;
  private int[] mTempLoc;
  private int mTouchSlop;

  public DragSortController(DragSortListView paramDragSortListView)
  {
    this(paramDragSortListView, 0, 0, 1);
  }

  public DragSortController(DragSortListView paramDragSortListView, int paramInt1, int paramInt2, int paramInt3)
  {
  }

  public DragSortController(DragSortListView paramDragSortListView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
  }

  public DragSortController(DragSortListView paramDragSortListView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    super(paramDragSortListView);
    int[] arrayOfInt = new int[2];
    this.mTempLoc = arrayOfInt;
    this.mDragging = false;
    this.mFlingSpeed = 500.0F;
    GestureDetector.SimpleOnGestureListener local1 = new GestureDetector.SimpleOnGestureListener()
    {
      public final boolean onFling(MotionEvent paramAnonymousMotionEvent1, MotionEvent paramAnonymousMotionEvent2, float paramAnonymousFloat1, float paramAnonymousFloat2)
      {
        int i;
        if ((DragSortController.this.mRemoveEnabled) && (DragSortController.this.mIsRemoving))
        {
          i = DragSortController.this.mDslv.getWidth() / 5;
          float f1 = DragSortController.this.mFlingSpeed;
          if (paramAnonymousFloat1 <= f1)
            break label97;
          int j = DragSortController.this.mPositionX;
          int k = -i;
          if (j > k)
            boolean bool1 = DragSortController.this.mDslv.stopDragWithVelocity(true, paramAnonymousFloat1);
        }
        while (true)
        {
          boolean bool2 = DragSortController.access$102(DragSortController.this, false);
          return false;
          label97: float f2 = -DragSortController.this.mFlingSpeed;
          if ((paramAnonymousFloat1 < f2) && (DragSortController.this.mPositionX < i))
            boolean bool3 = DragSortController.this.mDslv.stopDragWithVelocity(true, paramAnonymousFloat1);
        }
      }
    };
    this.mFlingRemoveListener = local1;
    this.mDslv = paramDragSortListView;
    Context localContext1 = paramDragSortListView.getContext();
    GestureDetector localGestureDetector1 = new GestureDetector(localContext1, this);
    this.mDetector = localGestureDetector1;
    Context localContext2 = paramDragSortListView.getContext();
    GestureDetector.OnGestureListener localOnGestureListener = this.mFlingRemoveListener;
    GestureDetector localGestureDetector2 = new GestureDetector(localContext2, localOnGestureListener);
    this.mFlingRemoveDetector = localGestureDetector2;
    this.mFlingRemoveDetector.setIsLongpressEnabled(false);
    int i = ViewConfiguration.get(paramDragSortListView.getContext()).getScaledTouchSlop();
    this.mTouchSlop = i;
    this.mDragHandleId = paramInt1;
    this.mClickRemoveId = paramInt4;
    this.mFlingHandleId = paramInt5;
    setRemoveMode(paramInt3);
    setDragInitMode(paramInt2);
  }

  public int dragHandleHitPosition(MotionEvent paramMotionEvent)
  {
    int i = this.mDragHandleId;
    return viewIdHitPosition(paramMotionEvent, i);
  }

  public int flingHandleHitPosition(MotionEvent paramMotionEvent)
  {
    int i = this.mFlingHandleId;
    return viewIdHitPosition(paramMotionEvent, i);
  }

  public boolean onDown(MotionEvent paramMotionEvent)
  {
    if ((this.mRemoveEnabled) && (this.mRemoveMode == 0))
    {
      int i = this.mClickRemoveId;
      int j = viewIdHitPosition(paramMotionEvent, i);
      this.mClickRemoveHitPos = j;
    }
    int k = startDragPosition(paramMotionEvent);
    this.mHitPos = k;
    if ((this.mHitPos != -1) && (this.mDragInitMode == 0))
    {
      int m = this.mHitPos;
      int n = (int)paramMotionEvent.getX();
      int i1 = this.mItemX;
      int i2 = n - i1;
      int i3 = (int)paramMotionEvent.getY();
      int i4 = this.mItemY;
      int i5 = i3 - i4;
      boolean bool = startDrag(m, i2, i5);
    }
    this.mIsRemoving = false;
    this.mCanDrag = true;
    this.mPositionX = 0;
    int i6 = startFlingPosition(paramMotionEvent);
    this.mFlingHitPos = i6;
    return true;
  }

  public void onDragFloatView(View paramView, Point paramPoint1, Point paramPoint2)
  {
    if (!this.mRemoveEnabled)
      return;
    if (!this.mIsRemoving)
      return;
    int i = paramPoint1.x;
    this.mPositionX = i;
  }

  public final boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
  {
    return false;
  }

  public void onLongPress(MotionEvent paramMotionEvent)
  {
    if (this.mHitPos == -1)
      return;
    if (this.mDragInitMode != 2)
      return;
    boolean bool1 = this.mDslv.performHapticFeedback(0);
    int i = this.mHitPos;
    int j = this.mCurrX;
    int k = this.mItemX;
    int m = j - k;
    int n = this.mCurrY;
    int i1 = this.mItemY;
    int i2 = n - i1;
    boolean bool2 = startDrag(i, m, i2);
  }

  public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
  {
    int i = (int)paramMotionEvent1.getX();
    int j = (int)paramMotionEvent1.getY();
    int k = (int)paramMotionEvent2.getX();
    int m = (int)paramMotionEvent2.getY();
    int n = this.mItemX;
    int i1 = k - n;
    int i2 = this.mItemY;
    int i3 = m - i2;
    if ((this.mCanDrag) && (!this.mDragging) && ((this.mHitPos != -1) || (this.mFlingHitPos != -1)))
    {
      if (this.mHitPos == -1)
        break label216;
      if (this.mDragInitMode != 1)
        break label153;
      int i4 = Math.abs(m - j);
      int i5 = this.mTouchSlop;
      if ((i4 <= i5) || (!this.mSortEnabled))
        break label153;
      int i6 = this.mHitPos;
      boolean bool1 = startDrag(i6, i1, i3);
    }
    while (true)
    {
      return false;
      label153: if (this.mDragInitMode != 0)
      {
        int i7 = Math.abs(k - i);
        int i8 = this.mTouchSlop;
        if ((i7 > i8) && (this.mRemoveEnabled))
        {
          this.mIsRemoving = true;
          int i9 = this.mFlingHitPos;
          boolean bool2 = startDrag(i9, i1, i3);
          continue;
          label216: if (this.mFlingHitPos != -1)
          {
            int i10 = Math.abs(k - i);
            int i11 = Math.abs(m - j);
            int i12 = this.mTouchSlop;
            if (i10 > i12)
            {
              int i13 = i11 * 2;
              if ((i10 > i13) && (this.mRemoveEnabled))
              {
                this.mIsRemoving = true;
                int i14 = this.mFlingHitPos;
                boolean bool3 = startDrag(i14, i1, i3);
              }
            }
            else
            {
              int i15 = this.mTouchSlop;
              if (i11 > i15)
                this.mCanDrag = false;
            }
          }
        }
      }
    }
  }

  public void onShowPress(MotionEvent paramMotionEvent)
  {
  }

  public boolean onSingleTapUp(MotionEvent paramMotionEvent)
  {
    if ((this.mRemoveEnabled) && (this.mRemoveMode == 0) && (this.mClickRemoveHitPos != -1))
    {
      DragSortListView localDragSortListView = this.mDslv;
      int i = this.mClickRemoveHitPos;
      int j = this.mDslv.getHeaderViewsCount();
      int k = i - j;
      localDragSortListView.removeItem(k);
    }
    return true;
  }

  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    if ((!this.mDslv.isDragEnabled()) || (this.mDslv.listViewIntercepted()));
    while (true)
    {
      return false;
      boolean bool1 = this.mDetector.onTouchEvent(paramMotionEvent);
      if ((this.mRemoveEnabled) && (this.mDragging) && (this.mRemoveMode == 1))
        boolean bool2 = this.mFlingRemoveDetector.onTouchEvent(paramMotionEvent);
      switch (paramMotionEvent.getAction() & 0xFF)
      {
      case 2:
      default:
        break;
      case 0:
        int i = (int)paramMotionEvent.getX();
        this.mCurrX = i;
        int j = (int)paramMotionEvent.getY();
        this.mCurrY = j;
      case 1:
      case 3:
      }
    }
    if ((this.mRemoveEnabled) && (this.mIsRemoving))
      if (this.mPositionX < 0)
        break label202;
    label202: for (int k = this.mPositionX; ; k = -this.mPositionX)
    {
      int m = this.mDslv.getWidth() / 2;
      if (k > m)
        boolean bool3 = this.mDslv.stopDragWithVelocity(true, 0.0F);
      this.mIsRemoving = false;
      this.mDragging = false;
      break;
    }
  }

  public void setDragInitMode(int paramInt)
  {
    this.mDragInitMode = paramInt;
  }

  public void setRemoveEnabled(boolean paramBoolean)
  {
    this.mRemoveEnabled = paramBoolean;
  }

  public void setRemoveMode(int paramInt)
  {
    this.mRemoveMode = paramInt;
  }

  public void setSortEnabled(boolean paramBoolean)
  {
    this.mSortEnabled = paramBoolean;
  }

  public boolean startDrag(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 0;
    if ((this.mSortEnabled) && (!this.mIsRemoving))
      i = 0x0 | 0xC;
    if ((this.mRemoveEnabled) && (this.mIsRemoving))
      i = i | 0x1 | 0x2;
    DragSortListView localDragSortListView = this.mDslv;
    int j = this.mDslv.getHeaderViewsCount();
    int k = paramInt1 - j;
    boolean bool = localDragSortListView.startDrag(k, i, paramInt2, paramInt3);
    this.mDragging = bool;
    return this.mDragging;
  }

  public int startDragPosition(MotionEvent paramMotionEvent)
  {
    return dragHandleHitPosition(paramMotionEvent);
  }

  public int startFlingPosition(MotionEvent paramMotionEvent)
  {
    if (this.mRemoveMode == 1);
    for (int i = flingHandleHitPosition(paramMotionEvent); ; i = -1)
      return i;
  }

  public int viewIdHitPosition(MotionEvent paramMotionEvent, int paramInt)
  {
    int i = (int)paramMotionEvent.getX();
    int j = (int)paramMotionEvent.getY();
    int k = this.mDslv.pointToPosition(i, j);
    int m = this.mDslv.getHeaderViewsCount();
    int n = this.mDslv.getFooterViewsCount();
    int i1 = this.mDslv.getCount();
    View localView1;
    View localView2;
    if ((k != -1) && (k >= m))
    {
      int i2 = i1 - n;
      if (k < i2)
      {
        DragSortListView localDragSortListView = this.mDslv;
        int i3 = this.mDslv.getFirstVisiblePosition();
        int i4 = k - i3;
        localView1 = localDragSortListView.getChildAt(i4);
        int i5 = (int)paramMotionEvent.getRawX();
        int i6 = (int)paramMotionEvent.getRawY();
        if (paramInt == 0)
        {
          localView2 = localView1;
          if (localView2 == null)
            break label279;
          int[] arrayOfInt = this.mTempLoc;
          localView2.getLocationOnScreen(arrayOfInt);
          int i7 = this.mTempLoc[0];
          if (i5 <= i7)
            break label279;
          int i8 = this.mTempLoc[1];
          if (i6 <= i8)
            break label279;
          int i9 = this.mTempLoc[0];
          int i10 = localView2.getWidth();
          int i11 = i9 + i10;
          if (i5 >= i11)
            break label279;
          int i12 = this.mTempLoc[1];
          int i13 = localView2.getHeight();
          int i14 = i12 + i13;
          if (i6 >= i14)
            break label279;
          int i15 = localView1.getLeft();
          this.mItemX = i15;
          int i16 = localView1.getTop();
          this.mItemY = i16;
        }
      }
    }
    while (true)
    {
      return k;
      localView2 = localView1.findViewById(paramInt);
      break;
      label279: k = -1;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.mobeta.android.dslv.DragSortController
 * JD-Core Version:    0.6.2
 */