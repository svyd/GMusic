package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import java.util.Arrays;

public class ViewDragHelper
{
  private static final Interpolator sInterpolator = new Interpolator()
  {
    public float getInterpolation(float paramAnonymousFloat)
    {
      float f = paramAnonymousFloat - 1.0F;
      return f * f * f * f * f + 1.0F;
    }
  };
  private int mActivePointerId = -1;
  private final Callback mCallback;
  private View mCapturedView;
  private int mDragState;
  private int[] mEdgeDragsInProgress;
  private int[] mEdgeDragsLocked;
  private int mEdgeSize;
  private int[] mInitialEdgesTouched;
  private float[] mInitialMotionX;
  private float[] mInitialMotionY;
  private float[] mLastMotionX;
  private float[] mLastMotionY;
  private float mMaxVelocity;
  private float mMinVelocity;
  private final ViewGroup mParentView;
  private int mPointersDown;
  private boolean mReleaseInProgress;
  private ScrollerCompat mScroller;
  private final Runnable mSetIdleRunnable;
  private int mTouchSlop;
  private int mTrackingEdges;
  private VelocityTracker mVelocityTracker;

  private ViewDragHelper(Context paramContext, ViewGroup paramViewGroup, Callback paramCallback)
  {
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        ViewDragHelper.this.setDragState(0);
      }
    };
    this.mSetIdleRunnable = local2;
    if (paramViewGroup == null)
      throw new IllegalArgumentException("Parent view may not be null");
    if (paramCallback == null)
      throw new IllegalArgumentException("Callback may not be null");
    this.mParentView = paramViewGroup;
    this.mCallback = paramCallback;
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(paramContext);
    float f1 = paramContext.getResources().getDisplayMetrics().density;
    int i = (int)(20.0F * f1 + 0.5F);
    this.mEdgeSize = i;
    int j = localViewConfiguration.getScaledTouchSlop();
    this.mTouchSlop = j;
    float f2 = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.mMaxVelocity = f2;
    float f3 = localViewConfiguration.getScaledMinimumFlingVelocity();
    this.mMinVelocity = f3;
    Interpolator localInterpolator = sInterpolator;
    ScrollerCompat localScrollerCompat = ScrollerCompat.create(paramContext, localInterpolator);
    this.mScroller = localScrollerCompat;
  }

  private boolean checkNewEdgeDrag(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2)
  {
    boolean bool = false;
    float f1 = Math.abs(paramFloat1);
    float f2 = Math.abs(paramFloat2);
    if (((this.mInitialEdgesTouched[paramInt1] & paramInt2) != paramInt2) && ((this.mTrackingEdges & paramInt2) != 0) && ((this.mEdgeDragsLocked[paramInt1] & paramInt2) != paramInt2) && ((this.mEdgeDragsInProgress[paramInt1] & paramInt2) != paramInt2))
    {
      float f3 = this.mTouchSlop;
      if (f1 > f3)
        break label100;
      float f4 = this.mTouchSlop;
      if (f2 > f4)
        break label100;
    }
    while (true)
    {
      return bool;
      label100: float f5 = 0.5F * f2;
      if ((f1 < f5) && (this.mCallback.onEdgeLock(paramInt2)))
      {
        int[] arrayOfInt = this.mEdgeDragsLocked;
        int i = arrayOfInt[paramInt1] | paramInt2;
        arrayOfInt[paramInt1] = i;
      }
      else if ((this.mEdgeDragsInProgress[paramInt1] & paramInt2) == 0)
      {
        float f6 = this.mTouchSlop;
        if (f1 > f6)
          bool = true;
      }
    }
  }

  private boolean checkTouchSlop(View paramView, float paramFloat1, float paramFloat2)
  {
    boolean bool = true;
    if (paramView == null)
      bool = false;
    while (true)
    {
      return bool;
      int i;
      if (this.mCallback.getViewHorizontalDragRange(paramView) > 0)
      {
        i = 1;
        label27: if (this.mCallback.getViewVerticalDragRange(paramView) <= 0)
          break label108;
      }
      label108: for (int j = 1; ; j = 0)
      {
        if ((i == 0) || (j == 0))
          break label114;
        float f1 = paramFloat1 * paramFloat1;
        float f2 = paramFloat2 * paramFloat2;
        float f3 = f1 + f2;
        int k = this.mTouchSlop;
        int m = this.mTouchSlop;
        float f4 = k * m;
        if (f3 > f4)
          break;
        bool = false;
        break;
        i = 0;
        break label27;
      }
      label114: if (i != 0)
      {
        float f5 = Math.abs(paramFloat1);
        float f6 = this.mTouchSlop;
        if (f5 <= f6)
          bool = false;
      }
      else if (j != 0)
      {
        float f7 = Math.abs(paramFloat2);
        float f8 = this.mTouchSlop;
        if (f7 <= f8)
          bool = false;
      }
      else
      {
        bool = false;
      }
    }
  }

  private float clampMag(float paramFloat1, float paramFloat2, float paramFloat3)
  {
    float f = Math.abs(paramFloat1);
    if (f < paramFloat2)
      paramFloat3 = 0.0F;
    while (true)
    {
      return paramFloat3;
      if (f > paramFloat3)
      {
        if (paramFloat1 <= 0.0F)
          paramFloat3 = -paramFloat3;
      }
      else
        paramFloat3 = paramFloat1;
    }
  }

  private int clampMag(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = Math.abs(paramInt1);
    if (i < paramInt2)
      paramInt3 = 0;
    while (true)
    {
      return paramInt3;
      if (i > paramInt3)
      {
        if (paramInt1 <= 0)
          paramInt3 = -paramInt3;
      }
      else
        paramInt3 = paramInt1;
    }
  }

  private void clearMotionHistory()
  {
    if (this.mInitialMotionX == null)
      return;
    Arrays.fill(this.mInitialMotionX, 0.0F);
    Arrays.fill(this.mInitialMotionY, 0.0F);
    Arrays.fill(this.mLastMotionX, 0.0F);
    Arrays.fill(this.mLastMotionY, 0.0F);
    Arrays.fill(this.mInitialEdgesTouched, 0);
    Arrays.fill(this.mEdgeDragsInProgress, 0);
    Arrays.fill(this.mEdgeDragsLocked, 0);
    this.mPointersDown = 0;
  }

  private void clearMotionHistory(int paramInt)
  {
    if (this.mInitialMotionX == null)
      return;
    this.mInitialMotionX[paramInt] = 0;
    this.mInitialMotionY[paramInt] = 0;
    this.mLastMotionX[paramInt] = 0;
    this.mLastMotionY[paramInt] = 0;
    this.mInitialEdgesTouched[paramInt] = 0;
    this.mEdgeDragsInProgress[paramInt] = 0;
    this.mEdgeDragsLocked[paramInt] = 0;
    int i = this.mPointersDown;
    int j = 1 << paramInt ^ 0xFFFFFFFF;
    int k = i & j;
    this.mPointersDown = k;
  }

  private int computeAxisDuration(int paramInt1, int paramInt2, int paramInt3)
  {
    int i;
    if (paramInt1 == 0)
    {
      i = 0;
      return i;
    }
    int j = this.mParentView.getWidth();
    int k = j / 2;
    float f1 = Math.abs(paramInt1);
    float f2 = j;
    float f3 = f1 / f2;
    float f4 = Math.min(1.0F, f3);
    float f5 = k;
    float f6 = k;
    float f7 = distanceInfluenceForSnapDuration(f4);
    float f8 = f6 * f7;
    float f9 = f5 + f8;
    paramInt2 = Math.abs(paramInt2);
    float f11;
    if (paramInt2 > 0)
    {
      float f10 = paramInt2;
      f11 = Math.abs(f9 / f10);
    }
    float f12;
    float f13;
    for (int m = Math.round(1000.0F * f11) * 4; ; m = (int)((f12 / f13 + 1.0F) * 256.0F))
    {
      i = Math.min(m, 600);
      break;
      f12 = Math.abs(paramInt1);
      f13 = paramInt3;
    }
  }

  private int computeSettleDuration(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = (int)this.mMinVelocity;
    int j = (int)this.mMaxVelocity;
    int k = paramInt3;
    int m = clampMag(k, i, j);
    int n = (int)this.mMinVelocity;
    int i1 = (int)this.mMaxVelocity;
    int i2 = paramInt4;
    int i3 = clampMag(i2, n, i1);
    int i4 = Math.abs(paramInt1);
    int i5 = Math.abs(paramInt2);
    int i6 = Math.abs(m);
    int i7 = Math.abs(i3);
    int i8 = i6 + i7;
    int i9 = i4 + i5;
    float f3;
    float f4;
    float f5;
    if (m != 0)
    {
      float f1 = i6;
      float f2 = i8;
      f3 = f1 / f2;
      if (i3 == 0)
        break label245;
      f4 = i7;
      f5 = i8;
    }
    label245: float f11;
    float f12;
    for (float f6 = f4 / f5; ; f6 = f11 / f12)
    {
      int i10 = this.mCallback.getViewHorizontalDragRange(paramView);
      int i11 = paramInt1;
      int i12 = m;
      int i13 = computeAxisDuration(i11, i12, i10);
      int i14 = this.mCallback.getViewVerticalDragRange(paramView);
      int i15 = paramInt2;
      int i16 = i3;
      int i17 = computeAxisDuration(i15, i16, i14);
      float f7 = i13 * f3;
      float f8 = i17 * f6;
      return (int)(f7 + f8);
      float f9 = i4;
      float f10 = i9;
      f3 = f9 / f10;
      break;
      f11 = i5;
      f12 = i9;
    }
  }

  public static ViewDragHelper create(ViewGroup paramViewGroup, float paramFloat, Callback paramCallback)
  {
    ViewDragHelper localViewDragHelper = create(paramViewGroup, paramCallback);
    float f1 = localViewDragHelper.mTouchSlop;
    float f2 = 1.0F / paramFloat;
    int i = (int)(f1 * f2);
    localViewDragHelper.mTouchSlop = i;
    return localViewDragHelper;
  }

  public static ViewDragHelper create(ViewGroup paramViewGroup, Callback paramCallback)
  {
    Context localContext = paramViewGroup.getContext();
    return new ViewDragHelper(localContext, paramViewGroup, paramCallback);
  }

  private void dispatchViewReleased(float paramFloat1, float paramFloat2)
  {
    this.mReleaseInProgress = true;
    Callback localCallback = this.mCallback;
    View localView = this.mCapturedView;
    localCallback.onViewReleased(localView, paramFloat1, paramFloat2);
    this.mReleaseInProgress = false;
    if (this.mDragState != 1)
      return;
    setDragState(0);
  }

  private float distanceInfluenceForSnapDuration(float paramFloat)
  {
    return (float)Math.sin((float)((paramFloat - 0.5F) * 0.47123891676382D));
  }

  private void dragTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt1;
    int j = paramInt2;
    int k = this.mCapturedView.getLeft();
    int m = this.mCapturedView.getTop();
    if (paramInt3 != 0)
    {
      Callback localCallback1 = this.mCallback;
      View localView1 = this.mCapturedView;
      i = localCallback1.clampViewPositionHorizontal(localView1, paramInt1, paramInt3);
      View localView2 = this.mCapturedView;
      int n = i - k;
      localView2.offsetLeftAndRight(n);
    }
    if (paramInt4 != 0)
    {
      Callback localCallback2 = this.mCallback;
      View localView3 = this.mCapturedView;
      j = localCallback2.clampViewPositionVertical(localView3, paramInt2, paramInt4);
      View localView4 = this.mCapturedView;
      int i1 = j - m;
      localView4.offsetTopAndBottom(i1);
    }
    if ((paramInt3 == 0) && (paramInt4 == 0))
      return;
    int i2 = i - k;
    int i3 = j - m;
    Callback localCallback3 = this.mCallback;
    View localView5 = this.mCapturedView;
    localCallback3.onViewPositionChanged(localView5, i, j, i2, i3);
  }

  private void ensureMotionHistorySizeForId(int paramInt)
  {
    if ((this.mInitialMotionX != null) && (this.mInitialMotionX.length > paramInt))
      return;
    float[] arrayOfFloat1 = new float[paramInt + 1];
    float[] arrayOfFloat2 = new float[paramInt + 1];
    float[] arrayOfFloat3 = new float[paramInt + 1];
    float[] arrayOfFloat4 = new float[paramInt + 1];
    int[] arrayOfInt1 = new int[paramInt + 1];
    int[] arrayOfInt2 = new int[paramInt + 1];
    int[] arrayOfInt3 = new int[paramInt + 1];
    if (this.mInitialMotionX != null)
    {
      float[] arrayOfFloat5 = this.mInitialMotionX;
      int i = this.mInitialMotionX.length;
      System.arraycopy(arrayOfFloat5, 0, arrayOfFloat1, 0, i);
      float[] arrayOfFloat6 = this.mInitialMotionY;
      int j = this.mInitialMotionY.length;
      System.arraycopy(arrayOfFloat6, 0, arrayOfFloat2, 0, j);
      float[] arrayOfFloat7 = this.mLastMotionX;
      int k = this.mLastMotionX.length;
      System.arraycopy(arrayOfFloat7, 0, arrayOfFloat3, 0, k);
      float[] arrayOfFloat8 = this.mLastMotionY;
      int m = this.mLastMotionY.length;
      System.arraycopy(arrayOfFloat8, 0, arrayOfFloat4, 0, m);
      int[] arrayOfInt4 = this.mInitialEdgesTouched;
      int n = this.mInitialEdgesTouched.length;
      System.arraycopy(arrayOfInt4, 0, arrayOfInt1, 0, n);
      int[] arrayOfInt5 = this.mEdgeDragsInProgress;
      int i1 = this.mEdgeDragsInProgress.length;
      System.arraycopy(arrayOfInt5, 0, arrayOfInt2, 0, i1);
      int[] arrayOfInt6 = this.mEdgeDragsLocked;
      int i2 = this.mEdgeDragsLocked.length;
      System.arraycopy(arrayOfInt6, 0, arrayOfInt3, 0, i2);
    }
    this.mInitialMotionX = arrayOfFloat1;
    this.mInitialMotionY = arrayOfFloat2;
    this.mLastMotionX = arrayOfFloat3;
    this.mLastMotionY = arrayOfFloat4;
    this.mInitialEdgesTouched = arrayOfInt1;
    this.mEdgeDragsInProgress = arrayOfInt2;
    this.mEdgeDragsLocked = arrayOfInt3;
  }

  private boolean forceSettleCapturedViewAt(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool = false;
    int i = this.mCapturedView.getLeft();
    int j = this.mCapturedView.getTop();
    int k = paramInt1 - i;
    int m = paramInt2 - j;
    if ((k == 0) && (m == 0))
    {
      this.mScroller.abortAnimation();
      setDragState(0);
    }
    while (true)
    {
      return bool;
      View localView = this.mCapturedView;
      ViewDragHelper localViewDragHelper = this;
      int n = paramInt3;
      int i1 = paramInt4;
      int i2 = localViewDragHelper.computeSettleDuration(localView, k, m, n, i1);
      ScrollerCompat localScrollerCompat = this.mScroller;
      int i3 = i;
      int i4 = k;
      int i5 = m;
      localScrollerCompat.startScroll(i3, j, i4, i5, i2);
      setDragState(2);
      bool = true;
    }
  }

  private int getEdgesTouched(int paramInt1, int paramInt2)
  {
    int i = 0;
    int j = this.mParentView.getLeft();
    int k = this.mEdgeSize;
    int m = j + k;
    if (paramInt1 < m)
      i = 0x0 | 0x1;
    int n = this.mParentView.getTop();
    int i1 = this.mEdgeSize;
    int i2 = n + i1;
    if (paramInt2 < i2)
      i |= 4;
    int i3 = this.mParentView.getRight();
    int i4 = this.mEdgeSize;
    int i5 = i3 - i4;
    if (paramInt1 > i5)
      i |= 2;
    int i6 = this.mParentView.getBottom();
    int i7 = this.mEdgeSize;
    int i8 = i6 - i7;
    if (paramInt2 > i8)
      i |= 8;
    return i;
  }

  private void releaseViewForPointerUp()
  {
    VelocityTracker localVelocityTracker1 = this.mVelocityTracker;
    float f1 = this.mMaxVelocity;
    localVelocityTracker1.computeCurrentVelocity(1000, f1);
    VelocityTracker localVelocityTracker2 = this.mVelocityTracker;
    int i = this.mActivePointerId;
    float f2 = VelocityTrackerCompat.getXVelocity(localVelocityTracker2, i);
    float f3 = this.mMinVelocity;
    float f4 = this.mMaxVelocity;
    float f5 = clampMag(f2, f3, f4);
    VelocityTracker localVelocityTracker3 = this.mVelocityTracker;
    int j = this.mActivePointerId;
    float f6 = VelocityTrackerCompat.getYVelocity(localVelocityTracker3, j);
    float f7 = this.mMinVelocity;
    float f8 = this.mMaxVelocity;
    float f9 = clampMag(f6, f7, f8);
    dispatchViewReleased(f5, f9);
  }

  private void reportNewEdgeDrags(float paramFloat1, float paramFloat2, int paramInt)
  {
    int i = 0;
    if (checkNewEdgeDrag(paramFloat1, paramFloat2, paramInt, 1))
      i = 0x0 | 0x1;
    if (checkNewEdgeDrag(paramFloat2, paramFloat1, paramInt, 4))
      i |= 4;
    if (checkNewEdgeDrag(paramFloat1, paramFloat2, paramInt, 2))
      i |= 2;
    if (checkNewEdgeDrag(paramFloat2, paramFloat1, paramInt, 8))
      i |= 8;
    if (i == 0)
      return;
    int[] arrayOfInt = this.mEdgeDragsInProgress;
    int j = arrayOfInt[paramInt] | i;
    arrayOfInt[paramInt] = j;
    this.mCallback.onEdgeDragStarted(i, paramInt);
  }

  private void saveInitialMotion(float paramFloat1, float paramFloat2, int paramInt)
  {
    ensureMotionHistorySizeForId(paramInt);
    float[] arrayOfFloat1 = this.mInitialMotionX;
    this.mLastMotionX[paramInt] = paramFloat1;
    arrayOfFloat1[paramInt] = paramFloat1;
    float[] arrayOfFloat2 = this.mInitialMotionY;
    this.mLastMotionY[paramInt] = paramFloat2;
    arrayOfFloat2[paramInt] = paramFloat2;
    int[] arrayOfInt = this.mInitialEdgesTouched;
    int i = (int)paramFloat1;
    int j = (int)paramFloat2;
    int k = getEdgesTouched(i, j);
    arrayOfInt[paramInt] = k;
    int m = this.mPointersDown;
    int n = 1 << paramInt;
    int i1 = m | n;
    this.mPointersDown = i1;
  }

  private void saveLastMotion(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getPointerCount(paramMotionEvent);
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      int k = MotionEventCompat.getPointerId(paramMotionEvent, j);
      float f1 = MotionEventCompat.getX(paramMotionEvent, j);
      float f2 = MotionEventCompat.getY(paramMotionEvent, j);
      this.mLastMotionX[k] = f1;
      this.mLastMotionY[k] = f2;
      j += 1;
    }
  }

  public void abort()
  {
    cancel();
    if (this.mDragState == 2)
    {
      int i = this.mScroller.getCurrX();
      int j = this.mScroller.getCurrY();
      this.mScroller.abortAnimation();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      Callback localCallback = this.mCallback;
      View localView = this.mCapturedView;
      int n = k - i;
      int i1 = m - j;
      localCallback.onViewPositionChanged(localView, k, m, n, i1);
    }
    setDragState(0);
  }

  public void cancel()
  {
    this.mActivePointerId = -1;
    clearMotionHistory();
    if (this.mVelocityTracker == null)
      return;
    this.mVelocityTracker.recycle();
    this.mVelocityTracker = null;
  }

  public void captureChildView(View paramView, int paramInt)
  {
    ViewParent localViewParent = paramView.getParent();
    ViewGroup localViewGroup1 = this.mParentView;
    if (localViewParent != localViewGroup1)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (");
      ViewGroup localViewGroup2 = this.mParentView;
      String str = localViewGroup2 + ")";
      throw new IllegalArgumentException(str);
    }
    this.mCapturedView = paramView;
    this.mActivePointerId = paramInt;
    this.mCallback.onViewCaptured(paramView, paramInt);
    setDragState(1);
  }

  public boolean checkTouchSlop(int paramInt)
  {
    int i = this.mInitialMotionX.length;
    int j = 0;
    if (j < i)
      if (!checkTouchSlop(paramInt, j));
    for (boolean bool = true; ; bool = false)
    {
      return bool;
      j += 1;
      break;
    }
  }

  public boolean checkTouchSlop(int paramInt1, int paramInt2)
  {
    boolean bool = true;
    if (!isPointerDown(paramInt2))
      bool = false;
    while (true)
    {
      return bool;
      int i;
      if ((paramInt1 & 0x1) == 1)
      {
        i = 1;
        label24: if ((paramInt1 & 0x2) != 2)
          break label150;
      }
      float f1;
      float f2;
      label150: for (int j = 1; ; j = 0)
      {
        int k = this.mLastMotionX[paramInt2];
        int m = this.mInitialMotionX[paramInt2];
        f1 = k - m;
        int n = this.mLastMotionY[paramInt2];
        int i1 = this.mInitialMotionY[paramInt2];
        f2 = n - i1;
        if ((i == 0) || (j == 0))
          break label156;
        float f3 = f1 * f1;
        float f4 = f2 * f2;
        float f5 = f3 + f4;
        int i2 = this.mTouchSlop;
        int i3 = this.mTouchSlop;
        float f6 = i2 * i3;
        if (f5 > f6)
          break;
        bool = false;
        break;
        i = 0;
        break label24;
      }
      label156: if (i != 0)
      {
        float f7 = Math.abs(f1);
        float f8 = this.mTouchSlop;
        if (f7 <= f8)
          bool = false;
      }
      else if (j != 0)
      {
        float f9 = Math.abs(f2);
        float f10 = this.mTouchSlop;
        if (f9 <= f10)
          bool = false;
      }
      else
      {
        bool = false;
      }
    }
  }

  public boolean continueSettling(boolean paramBoolean)
  {
    if (this.mDragState == 2)
    {
      boolean bool1 = this.mScroller.computeScrollOffset();
      int i = this.mScroller.getCurrX();
      int j = this.mScroller.getCurrY();
      int k = this.mCapturedView.getLeft();
      int m = i - k;
      int n = this.mCapturedView.getTop();
      int i1 = j - n;
      if (m != 0)
        this.mCapturedView.offsetLeftAndRight(m);
      if (i1 != 0)
        this.mCapturedView.offsetTopAndBottom(i1);
      if ((m != 0) || (i1 != 0))
      {
        Callback localCallback = this.mCallback;
        View localView = this.mCapturedView;
        localCallback.onViewPositionChanged(localView, i, j, m, i1);
      }
      if (bool1)
      {
        int i2 = this.mScroller.getFinalX();
        if (i != i2)
        {
          int i3 = this.mScroller.getFinalY();
          if (j != i3)
          {
            this.mScroller.abortAnimation();
            bool1 = this.mScroller.isFinished();
          }
        }
      }
      if (!bool1)
      {
        if (!paramBoolean)
          break label221;
        ViewGroup localViewGroup = this.mParentView;
        Runnable localRunnable = this.mSetIdleRunnable;
        boolean bool2 = localViewGroup.post(localRunnable);
      }
    }
    if (this.mDragState == 2);
    for (boolean bool3 = true; ; bool3 = false)
    {
      return bool3;
      label221: setDragState(0);
      break;
    }
  }

  public View findTopChildUnder(int paramInt1, int paramInt2)
  {
    int i = this.mParentView.getChildCount() + -1;
    View localView;
    if (i >= 0)
    {
      ViewGroup localViewGroup = this.mParentView;
      int j = this.mCallback.getOrderedChildIndex(i);
      localView = localViewGroup.getChildAt(j);
      int k = localView.getLeft();
      if (paramInt1 >= k)
      {
        int m = localView.getRight();
        if (paramInt1 < m)
        {
          int n = localView.getTop();
          if (paramInt2 >= n)
          {
            int i1 = localView.getBottom();
            if (paramInt2 >= i1);
          }
        }
      }
    }
    while (true)
    {
      return localView;
      i += -1;
      break;
      localView = null;
    }
  }

  public View getCapturedView()
  {
    return this.mCapturedView;
  }

  public int getEdgeSize()
  {
    return this.mEdgeSize;
  }

  public int getTouchSlop()
  {
    return this.mTouchSlop;
  }

  public int getViewDragState()
  {
    return this.mDragState;
  }

  public boolean isCapturedViewUnder(int paramInt1, int paramInt2)
  {
    View localView = this.mCapturedView;
    return isViewUnder(localView, paramInt1, paramInt2);
  }

  public boolean isPointerDown(int paramInt)
  {
    int i = 1;
    int j = this.mPointersDown;
    int k = i << paramInt;
    if ((j & k) != 0);
    while (true)
    {
      return i;
      i = 0;
    }
  }

  public boolean isViewUnder(View paramView, int paramInt1, int paramInt2)
  {
    boolean bool = false;
    if (paramView == null);
    while (true)
    {
      return bool;
      int i = paramView.getLeft();
      if (paramInt1 >= i)
      {
        int j = paramView.getRight();
        if (paramInt1 < j)
        {
          int k = paramView.getTop();
          if (paramInt2 >= k)
          {
            int m = paramView.getBottom();
            if (paramInt2 < m)
              bool = true;
          }
        }
      }
    }
  }

  public void processTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    int j = MotionEventCompat.getActionIndex(paramMotionEvent);
    if (i == 0)
      cancel();
    if (this.mVelocityTracker == null)
    {
      VelocityTracker localVelocityTracker1 = VelocityTracker.obtain();
      this.mVelocityTracker = localVelocityTracker1;
    }
    VelocityTracker localVelocityTracker2 = this.mVelocityTracker;
    MotionEvent localMotionEvent1 = paramMotionEvent;
    localVelocityTracker2.addMovement(localMotionEvent1);
    int m;
    int i4;
    float f5;
    float f6;
    int i32;
    int i33;
    switch (i)
    {
    case 4:
    default:
      return;
    case 0:
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      MotionEvent localMotionEvent2 = paramMotionEvent;
      int k = 0;
      m = MotionEventCompat.getPointerId(localMotionEvent2, k);
      int n = (int)f1;
      int i1 = (int)f2;
      ViewDragHelper localViewDragHelper1 = this;
      int i2 = n;
      int i3 = i1;
      View localView1 = localViewDragHelper1.findTopChildUnder(i2, i3);
      ViewDragHelper localViewDragHelper2 = this;
      float f3 = f1;
      float f4 = f2;
      localViewDragHelper2.saveInitialMotion(f3, f4, m);
      ViewDragHelper localViewDragHelper3 = this;
      View localView2 = localView1;
      boolean bool1 = localViewDragHelper3.tryCaptureViewForDrag(localView2, m);
      i4 = this.mInitialEdgesTouched[m];
      if ((this.mTrackingEdges & i4) == 0)
        return;
      Callback localCallback1 = this.mCallback;
      int i5 = this.mTrackingEdges & i4;
      Callback localCallback2 = localCallback1;
      int i6 = i5;
      localCallback2.onEdgeTouched(i6, m);
      return;
    case 5:
      m = MotionEventCompat.getPointerId(paramMotionEvent, j);
      f5 = MotionEventCompat.getX(paramMotionEvent, j);
      f6 = MotionEventCompat.getY(paramMotionEvent, j);
      ViewDragHelper localViewDragHelper4 = this;
      float f7 = f5;
      float f8 = f6;
      localViewDragHelper4.saveInitialMotion(f7, f8, m);
      if (this.mDragState == 0)
      {
        int i7 = (int)f5;
        int i8 = (int)f6;
        ViewDragHelper localViewDragHelper5 = this;
        int i9 = i7;
        int i10 = i8;
        View localView3 = localViewDragHelper5.findTopChildUnder(i9, i10);
        ViewDragHelper localViewDragHelper6 = this;
        View localView4 = localView3;
        boolean bool2 = localViewDragHelper6.tryCaptureViewForDrag(localView4, m);
        i4 = this.mInitialEdgesTouched[m];
        if ((this.mTrackingEdges & i4) == 0)
          return;
        Callback localCallback3 = this.mCallback;
        int i11 = this.mTrackingEdges & i4;
        Callback localCallback4 = localCallback3;
        int i12 = i11;
        localCallback4.onEdgeTouched(i12, m);
        return;
      }
      int i13 = (int)f5;
      int i14 = (int)f6;
      ViewDragHelper localViewDragHelper7 = this;
      int i15 = i13;
      int i16 = i14;
      if (!localViewDragHelper7.isCapturedViewUnder(i15, i16))
        return;
      View localView5 = this.mCapturedView;
      ViewDragHelper localViewDragHelper8 = this;
      View localView6 = localView5;
      boolean bool3 = localViewDragHelper8.tryCaptureViewForDrag(localView6, m);
      return;
    case 2:
      int i17 = this.mDragState;
      int i18 = 1;
      if (i17 == i18)
      {
        int i19 = this.mActivePointerId;
        MotionEvent localMotionEvent3 = paramMotionEvent;
        int i20 = i19;
        int i21 = MotionEventCompat.findPointerIndex(localMotionEvent3, i20);
        float f9 = MotionEventCompat.getX(paramMotionEvent, i21);
        float f10 = MotionEventCompat.getY(paramMotionEvent, i21);
        float[] arrayOfFloat1 = this.mLastMotionX;
        int i22 = this.mActivePointerId;
        int i23 = arrayOfFloat1[i22];
        int i24 = (int)(f9 - i23);
        float[] arrayOfFloat2 = this.mLastMotionY;
        int i25 = this.mActivePointerId;
        int i26 = arrayOfFloat2[i25];
        int i27 = (int)(f10 - i26);
        int i28 = this.mCapturedView.getLeft() + i24;
        int i29 = this.mCapturedView.getTop() + i27;
        ViewDragHelper localViewDragHelper9 = this;
        int i30 = i28;
        int i31 = i29;
        localViewDragHelper9.dragTo(i30, i31, i24, i27);
        saveLastMotion(paramMotionEvent);
        return;
      }
      i32 = MotionEventCompat.getPointerCount(paramMotionEvent);
      i33 = 0;
      while (true)
      {
        float f11;
        float f12;
        if (i33 < i32)
        {
          m = MotionEventCompat.getPointerId(paramMotionEvent, i33);
          f5 = MotionEventCompat.getX(paramMotionEvent, i33);
          f6 = MotionEventCompat.getY(paramMotionEvent, i33);
          int i34 = this.mInitialMotionX[m];
          f11 = f5 - i34;
          int i35 = this.mInitialMotionY[m];
          f12 = f6 - i35;
          reportNewEdgeDrags(f11, f12, m);
          int i36 = this.mDragState;
          int i37 = 1;
          if (i36 != i37)
            break label727;
        }
        ViewDragHelper localViewDragHelper12;
        View localView9;
        do
        {
          saveLastMotion(paramMotionEvent);
          return;
          int i38 = (int)f5;
          int i39 = (int)f6;
          ViewDragHelper localViewDragHelper10 = this;
          int i40 = i38;
          int i41 = i39;
          View localView7 = localViewDragHelper10.findTopChildUnder(i40, i41);
          ViewDragHelper localViewDragHelper11 = this;
          View localView8 = localView7;
          if (!localViewDragHelper11.checkTouchSlop(localView8, f11, f12))
            break;
          localViewDragHelper12 = this;
          localView9 = localView7;
        }
        while (localViewDragHelper12.tryCaptureViewForDrag(localView9, m));
        i33 += 1;
      }
    case 6:
      m = MotionEventCompat.getPointerId(paramMotionEvent, j);
      int i42 = this.mDragState;
      int i43 = 1;
      if (i42 == i43)
      {
        int i44 = this.mActivePointerId;
        if (m != i44)
        {
          int i45 = -1;
          i32 = MotionEventCompat.getPointerCount(paramMotionEvent);
          i33 = 0;
          if (i33 < i32)
          {
            int i46 = MotionEventCompat.getPointerId(paramMotionEvent, i33);
            int i47 = this.mActivePointerId;
            if (i46 != i47);
            ViewDragHelper localViewDragHelper14;
            View localView15;
            do
            {
              View localView12;
              View localView13;
              do
              {
                i33 += 1;
                break;
                float f13 = MotionEventCompat.getX(paramMotionEvent, i33);
                float f14 = MotionEventCompat.getY(paramMotionEvent, i33);
                int i48 = (int)f13;
                int i49 = (int)f14;
                ViewDragHelper localViewDragHelper13 = this;
                int i50 = i48;
                int i51 = i49;
                View localView10 = localViewDragHelper13.findTopChildUnder(i50, i51);
                View localView11 = this.mCapturedView;
                localView12 = localView10;
                localView13 = localView11;
              }
              while (localView12 != localView13);
              View localView14 = this.mCapturedView;
              localViewDragHelper14 = this;
              localView15 = localView14;
            }
            while (!localViewDragHelper14.tryCaptureViewForDrag(localView15, i46));
            i45 = this.mActivePointerId;
          }
          int i52 = 65535;
          if (i45 == i52)
            releaseViewForPointerUp();
        }
      }
      clearMotionHistory(m);
      return;
    case 1:
      label727: int i53 = this.mDragState;
      int i54 = 1;
      if (i53 == i54)
        releaseViewForPointerUp();
      cancel();
      return;
    case 3:
    }
    int i55 = this.mDragState;
    int i56 = 1;
    if (i55 == i56)
    {
      ViewDragHelper localViewDragHelper15 = this;
      float f15 = 0.0F;
      float f16 = 0.0F;
      localViewDragHelper15.dispatchViewReleased(f15, f16);
    }
    cancel();
  }

  void setDragState(int paramInt)
  {
    if (this.mDragState != paramInt)
      return;
    this.mDragState = paramInt;
    this.mCallback.onViewDragStateChanged(paramInt);
    if (paramInt != 0)
      return;
    this.mCapturedView = null;
  }

  public void setEdgeTrackingEnabled(int paramInt)
  {
    this.mTrackingEdges = paramInt;
  }

  public void setMinVelocity(float paramFloat)
  {
    this.mMinVelocity = paramFloat;
  }

  public boolean settleCapturedViewAt(int paramInt1, int paramInt2)
  {
    if (!this.mReleaseInProgress)
      throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
    VelocityTracker localVelocityTracker1 = this.mVelocityTracker;
    int i = this.mActivePointerId;
    int j = (int)VelocityTrackerCompat.getXVelocity(localVelocityTracker1, i);
    VelocityTracker localVelocityTracker2 = this.mVelocityTracker;
    int k = this.mActivePointerId;
    int m = (int)VelocityTrackerCompat.getYVelocity(localVelocityTracker2, k);
    return forceSettleCapturedViewAt(paramInt1, paramInt2, j, m);
  }

  public boolean shouldInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    int j = MotionEventCompat.getActionIndex(paramMotionEvent);
    if (i == 0)
      cancel();
    if (this.mVelocityTracker == null)
    {
      VelocityTracker localVelocityTracker = VelocityTracker.obtain();
      this.mVelocityTracker = localVelocityTracker;
    }
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (i)
    {
    case 4:
    default:
      if (this.mDragState != 1)
        break;
    case 0:
    case 5:
    case 2:
    case 6:
    case 1:
    case 3:
    }
    for (boolean bool1 = true; ; bool1 = false)
    {
      return bool1;
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      int k = MotionEventCompat.getPointerId(paramMotionEvent, 0);
      saveInitialMotion(f1, f2, k);
      int m = (int)f1;
      int n = (int)f2;
      View localView1 = findTopChildUnder(m, n);
      View localView2 = this.mCapturedView;
      if ((localView1 == localView2) && (this.mDragState == 2))
        boolean bool2 = tryCaptureViewForDrag(localView1, k);
      int i1 = this.mInitialEdgesTouched[k];
      if ((this.mTrackingEdges & i1) == 0)
        break;
      Callback localCallback1 = this.mCallback;
      int i2 = this.mTrackingEdges & i1;
      localCallback1.onEdgeTouched(i2, k);
      break;
      k = MotionEventCompat.getPointerId(paramMotionEvent, j);
      float f3 = MotionEventCompat.getX(paramMotionEvent, j);
      float f4 = MotionEventCompat.getY(paramMotionEvent, j);
      saveInitialMotion(f3, f4, k);
      if (this.mDragState == 0)
      {
        i1 = this.mInitialEdgesTouched[k];
        if ((this.mTrackingEdges & i1) == 0)
          break;
        Callback localCallback2 = this.mCallback;
        int i3 = this.mTrackingEdges & i1;
        localCallback2.onEdgeTouched(i3, k);
        break;
      }
      if (this.mDragState != 2)
        break;
      int i4 = (int)f3;
      int i5 = (int)f4;
      localView1 = findTopChildUnder(i4, i5);
      View localView3 = this.mCapturedView;
      if (localView1 != localView3)
        break;
      boolean bool3 = tryCaptureViewForDrag(localView1, k);
      break;
      int i6 = MotionEventCompat.getPointerCount(paramMotionEvent);
      int i7 = 0;
      while (true)
      {
        float f5;
        float f6;
        if (i7 < i6)
        {
          k = MotionEventCompat.getPointerId(paramMotionEvent, i7);
          f3 = MotionEventCompat.getX(paramMotionEvent, i7);
          f4 = MotionEventCompat.getY(paramMotionEvent, i7);
          int i8 = this.mInitialMotionX[k];
          f5 = f3 - i8;
          int i9 = this.mInitialMotionY[k];
          f6 = f4 - i9;
          reportNewEdgeDrags(f5, f6, k);
          if (this.mDragState != 1)
            break label464;
        }
        label464: View localView4;
        do
        {
          saveLastMotion(paramMotionEvent);
          break;
          int i10 = (int)f3;
          int i11 = (int)f4;
          localView4 = findTopChildUnder(i10, i11);
        }
        while ((localView4 != null) && (checkTouchSlop(localView4, f5, f6)) && (tryCaptureViewForDrag(localView4, k)));
        i7 += 1;
      }
      int i12 = MotionEventCompat.getPointerId(paramMotionEvent, j);
      clearMotionHistory(i12);
      break;
      cancel();
      break;
    }
  }

  public boolean smoothSlideViewTo(View paramView, int paramInt1, int paramInt2)
  {
    this.mCapturedView = paramView;
    this.mActivePointerId = -1;
    return forceSettleCapturedViewAt(paramInt1, paramInt2, 0, 0);
  }

  boolean tryCaptureViewForDrag(View paramView, int paramInt)
  {
    boolean bool = true;
    View localView = this.mCapturedView;
    if ((paramView == localView) && (this.mActivePointerId != paramInt));
    while (true)
    {
      return bool;
      if ((paramView != null) && (this.mCallback.tryCaptureView(paramView, paramInt)))
      {
        this.mActivePointerId = paramInt;
        captureChildView(paramView, paramInt);
      }
      else
      {
        bool = false;
      }
    }
  }

  public static abstract class Callback
  {
    public int clampViewPositionHorizontal(View paramView, int paramInt1, int paramInt2)
    {
      return 0;
    }

    public int clampViewPositionVertical(View paramView, int paramInt1, int paramInt2)
    {
      return 0;
    }

    public int getOrderedChildIndex(int paramInt)
    {
      return paramInt;
    }

    public int getViewHorizontalDragRange(View paramView)
    {
      return 0;
    }

    public int getViewVerticalDragRange(View paramView)
    {
      return 0;
    }

    public void onEdgeDragStarted(int paramInt1, int paramInt2)
    {
    }

    public boolean onEdgeLock(int paramInt)
    {
      return false;
    }

    public void onEdgeTouched(int paramInt1, int paramInt2)
    {
    }

    public void onViewCaptured(View paramView, int paramInt)
    {
    }

    public void onViewDragStateChanged(int paramInt)
    {
    }

    public void onViewPositionChanged(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
    }

    public void onViewReleased(View paramView, float paramFloat1, float paramFloat2)
    {
    }

    public abstract boolean tryCaptureView(View paramView, int paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.widget.ViewDragHelper
 * JD-Core Version:    0.6.2
 */