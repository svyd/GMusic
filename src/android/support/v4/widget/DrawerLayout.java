package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.KeyEventCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;

public class DrawerLayout extends ViewGroup
{
  private static final int[] LAYOUT_ATTRS = arrayOfInt;
  private boolean mChildrenCanceledTouch;
  private boolean mDisallowInterceptRequested;
  private int mDrawerState;
  private boolean mFirstLayout;
  private boolean mInLayout;
  private float mInitialMotionX;
  private float mInitialMotionY;
  private final ViewDragCallback mLeftCallback;
  private final ViewDragHelper mLeftDragger;
  private DrawerListener mListener;
  private int mLockModeLeft;
  private int mLockModeRight;
  private int mMinDrawerMargin;
  private final ViewDragCallback mRightCallback;
  private final ViewDragHelper mRightDragger;
  private int mScrimColor = -1728053248;
  private float mScrimOpacity;
  private Paint mScrimPaint;
  private Drawable mShadowLeft;
  private Drawable mShadowRight;

  static
  {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 16842931;
  }

  public DrawerLayout(Context paramContext)
  {
    this(paramContext, null);
  }

  public DrawerLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public DrawerLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    Paint localPaint = new Paint();
    this.mScrimPaint = localPaint;
    this.mFirstLayout = true;
    float f1 = getResources().getDisplayMetrics().density;
    int i = (int)(64.0F * f1 + 0.5F);
    this.mMinDrawerMargin = i;
    float f2 = 400.0F * f1;
    ViewDragCallback localViewDragCallback1 = new ViewDragCallback(3);
    this.mLeftCallback = localViewDragCallback1;
    ViewDragCallback localViewDragCallback2 = new ViewDragCallback(5);
    this.mRightCallback = localViewDragCallback2;
    ViewDragCallback localViewDragCallback3 = this.mLeftCallback;
    ViewDragHelper localViewDragHelper1 = ViewDragHelper.create(this, 1.0F, localViewDragCallback3);
    this.mLeftDragger = localViewDragHelper1;
    this.mLeftDragger.setEdgeTrackingEnabled(1);
    this.mLeftDragger.setMinVelocity(f2);
    ViewDragCallback localViewDragCallback4 = this.mLeftCallback;
    ViewDragHelper localViewDragHelper2 = this.mLeftDragger;
    localViewDragCallback4.setDragger(localViewDragHelper2);
    ViewDragCallback localViewDragCallback5 = this.mRightCallback;
    ViewDragHelper localViewDragHelper3 = ViewDragHelper.create(this, 1.0F, localViewDragCallback5);
    this.mRightDragger = localViewDragHelper3;
    this.mRightDragger.setEdgeTrackingEnabled(2);
    this.mRightDragger.setMinVelocity(f2);
    ViewDragCallback localViewDragCallback6 = this.mRightCallback;
    ViewDragHelper localViewDragHelper4 = this.mRightDragger;
    localViewDragCallback6.setDragger(localViewDragHelper4);
    setFocusableInTouchMode(true);
    AccessibilityDelegate localAccessibilityDelegate = new AccessibilityDelegate();
    ViewCompat.setAccessibilityDelegate(this, localAccessibilityDelegate);
    ViewGroupCompat.setMotionEventSplittingEnabled(this, false);
  }

  private View findVisibleDrawer()
  {
    int i = getChildCount();
    int j = 0;
    View localView;
    if (j < i)
    {
      localView = getChildAt(j);
      if ((!isDrawerView(localView)) || (!isDrawerVisible(localView)));
    }
    while (true)
    {
      return localView;
      j += 1;
      break;
      localView = null;
    }
  }

  static String gravityToString(int paramInt)
  {
    String str;
    if ((paramInt & 0x3) == 3)
      str = "LEFT";
    while (true)
    {
      return str;
      if ((paramInt & 0x5) == 5)
        str = "RIGHT";
      else
        str = Integer.toHexString(paramInt);
    }
  }

  private static boolean hasOpaqueBackground(View paramView)
  {
    boolean bool = false;
    Drawable localDrawable = paramView.getBackground();
    if ((localDrawable != null) && (localDrawable.getOpacity() == -1))
      bool = true;
    return bool;
  }

  private boolean hasPeekingDrawer()
  {
    int i = getChildCount();
    int j = 0;
    if (j < i)
      if (!((LayoutParams)getChildAt(j).getLayoutParams()).isPeeking);
    for (boolean bool = true; ; bool = false)
    {
      return bool;
      j += 1;
      break;
    }
  }

  private boolean hasVisibleDrawer()
  {
    if (findVisibleDrawer() != null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  void cancelChildViewTouch()
  {
    if (this.mChildrenCanceledTouch)
      return;
    long l1 = SystemClock.uptimeMillis();
    long l2 = l1;
    float f = 0.0F;
    MotionEvent localMotionEvent = MotionEvent.obtain(l1, l2, 3, 0.0F, f, 0);
    int i = getChildCount();
    int j = 0;
    while (j < i)
    {
      boolean bool = getChildAt(j).dispatchTouchEvent(localMotionEvent);
      j += 1;
    }
    localMotionEvent.recycle();
    this.mChildrenCanceledTouch = true;
  }

  boolean checkDrawerViewAbsoluteGravity(View paramView, int paramInt)
  {
    if ((getDrawerViewAbsoluteGravity(paramView) & paramInt) != paramInt);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if (((paramLayoutParams instanceof LayoutParams)) && (super.checkLayoutParams(paramLayoutParams)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void closeDrawer(int paramInt)
  {
    View localView = findDrawerWithGravity(paramInt);
    if (localView == null)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("No drawer view found with gravity ");
      String str1 = gravityToString(paramInt);
      String str2 = str1;
      throw new IllegalArgumentException(str2);
    }
    closeDrawer(localView);
  }

  public void closeDrawer(View paramView)
  {
    if (!isDrawerView(paramView))
    {
      String str = "View " + paramView + " is not a sliding drawer";
      throw new IllegalArgumentException(str);
    }
    if (this.mFirstLayout)
    {
      LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
      localLayoutParams.onScreen = 0.0F;
      localLayoutParams.knownOpen = false;
    }
    while (true)
    {
      invalidate();
      return;
      if (checkDrawerViewAbsoluteGravity(paramView, 3))
      {
        ViewDragHelper localViewDragHelper1 = this.mLeftDragger;
        int i = -paramView.getWidth();
        int j = paramView.getTop();
        boolean bool1 = localViewDragHelper1.smoothSlideViewTo(paramView, i, j);
      }
      else
      {
        ViewDragHelper localViewDragHelper2 = this.mRightDragger;
        int k = getWidth();
        int m = paramView.getTop();
        boolean bool2 = localViewDragHelper2.smoothSlideViewTo(paramView, k, m);
      }
    }
  }

  public void closeDrawers()
  {
    closeDrawers(false);
  }

  void closeDrawers(boolean paramBoolean)
  {
    boolean bool1 = false;
    int i = getChildCount();
    int j = 0;
    while (j < i)
    {
      View localView = getChildAt(j);
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      if ((!isDrawerView(localView)) || ((paramBoolean) && (!localLayoutParams.isPeeking)))
      {
        j += 1;
      }
      else
      {
        int k = localView.getWidth();
        if (checkDrawerViewAbsoluteGravity(localView, 3))
        {
          ViewDragHelper localViewDragHelper1 = this.mLeftDragger;
          int m = -k;
          int n = localView.getTop();
          boolean bool2 = localViewDragHelper1.smoothSlideViewTo(localView, m, n);
          bool1 |= bool2;
        }
        while (true)
        {
          localLayoutParams.isPeeking = false;
          break;
          ViewDragHelper localViewDragHelper2 = this.mRightDragger;
          int i1 = getWidth();
          int i2 = localView.getTop();
          boolean bool3 = localViewDragHelper2.smoothSlideViewTo(localView, i1, i2);
          bool1 |= bool3;
        }
      }
    }
    this.mLeftCallback.removeCallbacks();
    this.mRightCallback.removeCallbacks();
    if (!bool1)
      return;
    invalidate();
  }

  public void computeScroll()
  {
    int i = getChildCount();
    float f1 = 0.0F;
    int j = 0;
    while (j < i)
    {
      float f2 = ((LayoutParams)getChildAt(j).getLayoutParams()).onScreen;
      f1 = Math.max(f1, f2);
      j += 1;
    }
    this.mScrimOpacity = f1;
    boolean bool1 = this.mLeftDragger.continueSettling(true);
    boolean bool2 = this.mRightDragger.continueSettling(true);
    if (!(bool1 | bool2))
      return;
    ViewCompat.postInvalidateOnAnimation(this);
  }

  void dispatchOnDrawerClosed(View paramView)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    if (!localLayoutParams.knownOpen)
      return;
    localLayoutParams.knownOpen = false;
    if (this.mListener != null)
      this.mListener.onDrawerClosed(paramView);
    sendAccessibilityEvent(32);
  }

  void dispatchOnDrawerOpened(View paramView)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    if (localLayoutParams.knownOpen)
      return;
    localLayoutParams.knownOpen = true;
    if (this.mListener != null)
      this.mListener.onDrawerOpened(paramView);
    paramView.sendAccessibilityEvent(32);
  }

  void dispatchOnDrawerSlide(View paramView, float paramFloat)
  {
    if (this.mListener == null)
      return;
    this.mListener.onDrawerSlide(paramView, paramFloat);
  }

  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong)
  {
    int i = getHeight();
    DrawerLayout localDrawerLayout1 = this;
    View localView1 = paramView;
    boolean bool1 = localDrawerLayout1.isContentView(localView1);
    int j = 0;
    int k = getWidth();
    int m = paramCanvas.save();
    if (bool1)
    {
      int n = getChildCount();
      int i1 = 0;
      if (i1 < n)
      {
        DrawerLayout localDrawerLayout2 = this;
        int i2 = i1;
        View localView2 = localDrawerLayout2.getChildAt(i2);
        View localView3 = localView2;
        View localView4 = paramView;
        if ((localView3 != localView4) && (localView2.getVisibility() == 0) && (hasOpaqueBackground(localView2)))
        {
          DrawerLayout localDrawerLayout3 = this;
          View localView5 = localView2;
          if (localDrawerLayout3.isDrawerView(localView5))
          {
            int i3 = localView2.getHeight();
            int i4 = i;
            if (i3 >= i4)
              break label147;
          }
        }
        while (true)
        {
          i1 += 1;
          break;
          label147: DrawerLayout localDrawerLayout4 = this;
          View localView6 = localView2;
          if (localDrawerLayout4.checkDrawerViewAbsoluteGravity(localView6, 3))
          {
            int i5 = localView2.getRight();
            if (i5 > j)
              j = i5;
          }
          else
          {
            int i6 = localView2.getLeft();
            if (i6 < k)
              k = i6;
          }
        }
      }
      int i7 = getHeight();
      boolean bool2 = paramCanvas.clipRect(j, 0, k, i7);
    }
    boolean bool3 = super.drawChild(paramCanvas, paramView, paramLong);
    Canvas localCanvas1 = paramCanvas;
    int i8 = m;
    localCanvas1.restoreToCount(i8);
    if ((this.mScrimOpacity > 0.0F) && (bool1))
    {
      float f1 = (this.mScrimColor & 0xFF000000) >>> 24;
      float f2 = this.mScrimOpacity;
      int i9 = (int)(f1 * f2) << 24;
      int i10 = this.mScrimColor & 0xFFFFFF;
      int i11 = i9 | i10;
      this.mScrimPaint.setColor(i11);
      float f3 = j;
      float f4 = k;
      float f5 = getHeight();
      Paint localPaint = this.mScrimPaint;
      paramCanvas.drawRect(f3, 0.0F, f4, f5, localPaint);
    }
    while (true)
    {
      return bool3;
      if (this.mShadowLeft != null)
      {
        DrawerLayout localDrawerLayout5 = this;
        View localView7 = paramView;
        if (localDrawerLayout5.checkDrawerViewAbsoluteGravity(localView7, 3))
        {
          int i12 = this.mShadowLeft.getIntrinsicWidth();
          int i13 = paramView.getRight();
          int i14 = this.mLeftDragger.getEdgeSize();
          float f6 = i13;
          float f7 = i14;
          float f8 = Math.min(f6 / f7, 1.0F);
          float f9 = Math.max(0.0F, f8);
          Drawable localDrawable1 = this.mShadowLeft;
          int i15 = paramView.getTop();
          int i16 = i13 + i12;
          int i17 = paramView.getBottom();
          localDrawable1.setBounds(i13, i15, i16, i17);
          Drawable localDrawable2 = this.mShadowLeft;
          int i18 = (int)(255.0F * f9);
          localDrawable2.setAlpha(i18);
          Drawable localDrawable3 = this.mShadowLeft;
          Canvas localCanvas2 = paramCanvas;
          localDrawable3.draw(localCanvas2);
        }
      }
      else if (this.mShadowRight != null)
      {
        DrawerLayout localDrawerLayout6 = this;
        View localView8 = paramView;
        if (localDrawerLayout6.checkDrawerViewAbsoluteGravity(localView8, 5))
        {
          int i19 = this.mShadowRight.getIntrinsicWidth();
          int i20 = paramView.getLeft();
          int i21 = getWidth() - i20;
          int i22 = this.mRightDragger.getEdgeSize();
          float f10 = i21;
          float f11 = i22;
          float f12 = Math.min(f10 / f11, 1.0F);
          float f13 = Math.max(0.0F, f12);
          Drawable localDrawable4 = this.mShadowRight;
          int i23 = i20 - i19;
          int i24 = paramView.getTop();
          int i25 = paramView.getBottom();
          localDrawable4.setBounds(i23, i24, i20, i25);
          Drawable localDrawable5 = this.mShadowRight;
          int i26 = (int)(255.0F * f13);
          localDrawable5.setAlpha(i26);
          Drawable localDrawable6 = this.mShadowRight;
          Canvas localCanvas3 = paramCanvas;
          localDrawable6.draw(localCanvas3);
        }
      }
    }
  }

  View findDrawerWithGravity(int paramInt)
  {
    int i = ViewCompat.getLayoutDirection(this);
    int j = GravityCompat.getAbsoluteGravity(paramInt, i) & 0x7;
    int k = getChildCount();
    int m = 0;
    View localView;
    if (m < k)
    {
      localView = getChildAt(m);
      if ((getDrawerViewAbsoluteGravity(localView) & 0x7) == j);
    }
    while (true)
    {
      return localView;
      m += 1;
      break;
      localView = null;
    }
  }

  View findOpenDrawer()
  {
    int i = getChildCount();
    int j = 0;
    View localView;
    if (j < i)
    {
      localView = getChildAt(j);
      if (!((LayoutParams)localView.getLayoutParams()).knownOpen);
    }
    while (true)
    {
      return localView;
      j += 1;
      break;
      localView = null;
    }
  }

  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new LayoutParams(-1, -1);
  }

  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    Context localContext = getContext();
    return new LayoutParams(localContext, paramAttributeSet);
  }

  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    LayoutParams localLayoutParams2;
    if ((paramLayoutParams instanceof LayoutParams))
    {
      LayoutParams localLayoutParams1 = (LayoutParams)paramLayoutParams;
      localLayoutParams2 = new LayoutParams(localLayoutParams1);
    }
    while (true)
    {
      return localLayoutParams2;
      if ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams))
      {
        ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)paramLayoutParams;
        localLayoutParams2 = new LayoutParams(localMarginLayoutParams);
      }
      else
      {
        localLayoutParams2 = new LayoutParams(paramLayoutParams);
      }
    }
  }

  public int getDrawerLockMode(View paramView)
  {
    int i = getDrawerViewAbsoluteGravity(paramView);
    int j;
    if (i == 3)
      j = this.mLockModeLeft;
    while (true)
    {
      return j;
      if (i == 5)
        j = this.mLockModeRight;
      else
        j = 0;
    }
  }

  int getDrawerViewAbsoluteGravity(View paramView)
  {
    int i = ((LayoutParams)paramView.getLayoutParams()).gravity;
    int j = ViewCompat.getLayoutDirection(this);
    return GravityCompat.getAbsoluteGravity(i, j);
  }

  float getDrawerViewOffset(View paramView)
  {
    return ((LayoutParams)paramView.getLayoutParams()).onScreen;
  }

  boolean isContentView(View paramView)
  {
    if (((LayoutParams)paramView.getLayoutParams()).gravity == 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isDrawerOpen(int paramInt)
  {
    View localView = findDrawerWithGravity(paramInt);
    if (localView != null);
    for (boolean bool = isDrawerOpen(localView); ; bool = false)
      return bool;
  }

  public boolean isDrawerOpen(View paramView)
  {
    if (!isDrawerView(paramView))
    {
      String str = "View " + paramView + " is not a drawer";
      throw new IllegalArgumentException(str);
    }
    return ((LayoutParams)paramView.getLayoutParams()).knownOpen;
  }

  boolean isDrawerView(View paramView)
  {
    int i = ((LayoutParams)paramView.getLayoutParams()).gravity;
    int j = ViewCompat.getLayoutDirection(paramView);
    if ((GravityCompat.getAbsoluteGravity(i, j) & 0x7) != 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isDrawerVisible(int paramInt)
  {
    View localView = findDrawerWithGravity(paramInt);
    if (localView != null);
    for (boolean bool = isDrawerVisible(localView); ; bool = false)
      return bool;
  }

  public boolean isDrawerVisible(View paramView)
  {
    if (!isDrawerView(paramView))
    {
      String str = "View " + paramView + " is not a drawer";
      throw new IllegalArgumentException(str);
    }
    if (((LayoutParams)paramView.getLayoutParams()).onScreen > 0.0F);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mFirstLayout = true;
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.mFirstLayout = true;
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = false;
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    boolean bool2 = this.mLeftDragger.shouldInterceptTouchEvent(paramMotionEvent);
    boolean bool3 = this.mRightDragger.shouldInterceptTouchEvent(paramMotionEvent);
    int j = bool2 | bool3;
    int k = 0;
    switch (i)
    {
    default:
    case 0:
    case 2:
    case 1:
    case 3:
    }
    while (true)
    {
      if ((j != 0) || (k != 0) || (hasPeekingDrawer()) || (this.mChildrenCanceledTouch))
        bool1 = true;
      return bool1;
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      this.mInitialMotionX = f1;
      this.mInitialMotionY = f2;
      if (this.mScrimOpacity > 0.0F)
      {
        ViewDragHelper localViewDragHelper = this.mLeftDragger;
        int m = (int)f1;
        int n = (int)f2;
        View localView = localViewDragHelper.findTopChildUnder(m, n);
        if (isContentView(localView))
          k = 1;
      }
      this.mDisallowInterceptRequested = false;
      this.mChildrenCanceledTouch = false;
      continue;
      if (this.mLeftDragger.checkTouchSlop(3))
      {
        this.mLeftCallback.removeCallbacks();
        this.mRightCallback.removeCallbacks();
        continue;
        closeDrawers(true);
        this.mDisallowInterceptRequested = false;
        this.mChildrenCanceledTouch = false;
      }
    }
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt == 4) && (hasVisibleDrawer()))
      KeyEventCompat.startTracking(paramKeyEvent);
    for (boolean bool = true; ; bool = super.onKeyDown(paramInt, paramKeyEvent))
      return bool;
  }

  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool;
    if (paramInt == 4)
    {
      View localView = findVisibleDrawer();
      if ((localView != null) && (getDrawerLockMode(localView) == 0))
        closeDrawers();
      if (localView != null)
        bool = true;
    }
    while (true)
    {
      return bool;
      bool = false;
      continue;
      bool = super.onKeyUp(paramInt, paramKeyEvent);
    }
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool1 = true;
    this.mInLayout = bool1;
    int i = paramInt3 - paramInt1;
    int j = getChildCount();
    int k = 0;
    if (k < j)
    {
      View localView = getChildAt(k);
      int m = localView.getVisibility();
      int n = 8;
      if (m == n);
      LayoutParams localLayoutParams;
      while (true)
      {
        k += 1;
        break;
        localLayoutParams = (LayoutParams)localView.getLayoutParams();
        if (!isContentView(localView))
          break label173;
        int i1 = localLayoutParams.leftMargin;
        int i2 = localLayoutParams.topMargin;
        int i3 = localLayoutParams.leftMargin;
        int i4 = localView.getMeasuredWidth();
        int i5 = i3 + i4;
        int i6 = localLayoutParams.topMargin;
        int i7 = localView.getMeasuredHeight();
        int i8 = i6 + i7;
        int i9 = i1;
        int i10 = i2;
        int i11 = i5;
        int i12 = i8;
        localView.layout(i9, i10, i11, i12);
      }
      label173: int i13 = localView.getMeasuredWidth();
      int i14 = localView.getMeasuredHeight();
      DrawerLayout localDrawerLayout = this;
      int i15 = 3;
      int i18;
      float f5;
      label257: int i19;
      if (localDrawerLayout.checkDrawerViewAbsoluteGravity(localView, i15))
      {
        int i16 = -i13;
        float f1 = i13;
        float f2 = localLayoutParams.onScreen;
        int i17 = (int)(f1 * f2);
        i18 = i16 + i17;
        float f3 = i13 + i18;
        float f4 = i13;
        f5 = f3 / f4;
        float f6 = localLayoutParams.onScreen;
        if (f5 == f6)
          break label454;
        i19 = 1;
        label275: switch (localLayoutParams.gravity & 0x70)
        {
        default:
          int i20 = localLayoutParams.topMargin;
          int i21 = i18 + i13;
          int i22 = localLayoutParams.topMargin + i14;
          int i23 = i20;
          int i24 = i21;
          int i25 = i22;
          localView.layout(i18, i23, i24, i25);
          label357: if (i19 != 0)
            setDrawerViewOffset(localView, f5);
          if (localLayoutParams.onScreen <= 0.0F)
            break;
        case 80:
        case 16:
        }
      }
      for (int i26 = 0; localView.getVisibility() != i26; i26 = 4)
      {
        localView.setVisibility(i26);
        break;
        float f7 = i13;
        float f8 = localLayoutParams.onScreen;
        int i27 = (int)(f7 * f8);
        i18 = i - i27;
        float f9 = i - i18;
        float f10 = i13;
        f5 = f9 / f10;
        break label257;
        label454: i19 = 0;
        break label275;
        int i28 = paramInt4 - paramInt2;
        int i29 = localLayoutParams.bottomMargin;
        int i30 = i28 - i29;
        int i31 = localView.getMeasuredHeight();
        int i32 = i30 - i31;
        int i33 = i18 + i13;
        int i34 = localLayoutParams.bottomMargin;
        int i35 = i28 - i34;
        int i36 = i32;
        int i37 = i33;
        int i38 = i35;
        localView.layout(i18, i36, i37, i38);
        break label357;
        int i39 = paramInt4 - paramInt2;
        int i40 = (i39 - i14) / 2;
        int i41 = localLayoutParams.topMargin;
        if (i40 < i41)
          i40 = localLayoutParams.topMargin;
        while (true)
        {
          int i42 = i18 + i13;
          int i43 = i40 + i14;
          int i44 = i42;
          int i45 = i43;
          localView.layout(i18, i40, i44, i45);
          break;
          int i46 = i40 + i14;
          int i47 = localLayoutParams.bottomMargin;
          int i48 = i39 - i47;
          int i49 = i46;
          int i50 = i48;
          if (i49 > i50)
          {
            int i51 = localLayoutParams.bottomMargin;
            i40 = i39 - i51 - i14;
          }
        }
      }
    }
    boolean bool2 = false;
    this.mInLayout = bool2;
    boolean bool3 = false;
    this.mFirstLayout = bool3;
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    int k = View.MeasureSpec.getSize(paramInt1);
    int m = View.MeasureSpec.getSize(paramInt2);
    int n = 1073741824;
    if (i == n)
    {
      int i1 = 1073741824;
      if (j == i1);
    }
    else
    {
      if (!isInEditMode())
        break label172;
      int i2 = -2147483648;
      if (i != i2)
        break label147;
      int i3 = -2147483648;
      if (j != i3)
        break label159;
    }
    label76: DrawerLayout localDrawerLayout = this;
    int i4 = k;
    localDrawerLayout.setMeasuredDimension(i4, m);
    int i5 = 0;
    int i6 = getChildCount();
    int i7 = 0;
    label104: if (i7 >= i6)
      return;
    View localView = getChildAt(i7);
    int i8 = localView.getVisibility();
    int i9 = 8;
    if (i8 == i9);
    while (true)
    {
      i7 += 1;
      break label104;
      label147: if (i != 0)
        break;
      k = 300;
      break;
      label159: if (j != 0)
        break label76;
      m = 300;
      break label76;
      label172: throw new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      if (isContentView(localView))
      {
        int i10 = localLayoutParams.leftMargin;
        int i11 = k - i10;
        int i12 = localLayoutParams.rightMargin;
        int i13 = View.MeasureSpec.makeMeasureSpec(i11 - i12, 1073741824);
        int i14 = localLayoutParams.topMargin;
        int i15 = m - i14;
        int i16 = localLayoutParams.bottomMargin;
        int i17 = View.MeasureSpec.makeMeasureSpec(i15 - i16, 1073741824);
        localView.measure(i13, i17);
      }
      else
      {
        if (!isDrawerView(localView))
          break label503;
        int i18 = getDrawerViewAbsoluteGravity(localView) & 0x7;
        if ((i5 & i18) != 0)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("Child drawer has absolute gravity ");
          String str1 = gravityToString(i18);
          String str2 = str1 + " but this " + "DrawerLayout" + " already has a " + "drawer view along that edge";
          throw new IllegalStateException(str2);
        }
        int i19 = this.mMinDrawerMargin;
        int i20 = localLayoutParams.leftMargin;
        int i21 = i19 + i20;
        int i22 = localLayoutParams.rightMargin;
        int i23 = i21 + i22;
        int i24 = localLayoutParams.width;
        int i25 = paramInt1;
        int i26 = i23;
        int i27 = i24;
        int i28 = getChildMeasureSpec(i25, i26, i27);
        int i29 = localLayoutParams.topMargin;
        int i30 = localLayoutParams.bottomMargin;
        int i31 = i29 + i30;
        int i32 = localLayoutParams.height;
        int i33 = paramInt2;
        int i34 = i31;
        int i35 = i32;
        int i36 = getChildMeasureSpec(i33, i34, i35);
        localView.measure(i28, i36);
      }
    }
    label503: String str3 = "Child " + localView + " at index " + i7 + " does not have a valid layout_gravity - must be Gravity.LEFT, " + "Gravity.RIGHT or Gravity.NO_GRAVITY";
    throw new IllegalStateException(str3);
  }

  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    Parcelable localParcelable = localSavedState.getSuperState();
    super.onRestoreInstanceState(localParcelable);
    if (localSavedState.openDrawerGravity != 0)
    {
      int i = localSavedState.openDrawerGravity;
      View localView = findDrawerWithGravity(i);
      if (localView != null)
        openDrawer(localView);
    }
    int j = localSavedState.lockModeLeft;
    setDrawerLockMode(j, 3);
    int k = localSavedState.lockModeRight;
    setDrawerLockMode(k, 5);
  }

  protected Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable = super.onSaveInstanceState();
    SavedState localSavedState = new SavedState(localParcelable);
    int i = getChildCount();
    int j = 0;
    if (j < i)
    {
      View localView = getChildAt(j);
      if (!isDrawerView(localView));
      LayoutParams localLayoutParams;
      do
      {
        j += 1;
        break;
        localLayoutParams = (LayoutParams)localView.getLayoutParams();
      }
      while (!localLayoutParams.knownOpen);
      int k = localLayoutParams.gravity;
      localSavedState.openDrawerGravity = k;
    }
    int m = this.mLockModeLeft;
    localSavedState.lockModeLeft = m;
    int n = this.mLockModeRight;
    localSavedState.lockModeRight = n;
    return localSavedState;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    this.mLeftDragger.processTouchEvent(paramMotionEvent);
    this.mRightDragger.processTouchEvent(paramMotionEvent);
    int i = paramMotionEvent.getAction();
    switch (i & 0xFF)
    {
    case 2:
    default:
    case 0:
    case 1:
    case 3:
    }
    while (true)
    {
      return true;
      float f1 = paramMotionEvent.getX();
      float f2 = paramMotionEvent.getY();
      this.mInitialMotionX = f1;
      this.mInitialMotionY = f2;
      this.mDisallowInterceptRequested = false;
      this.mChildrenCanceledTouch = false;
      continue;
      float f3 = paramMotionEvent.getX();
      float f4 = paramMotionEvent.getY();
      boolean bool = true;
      ViewDragHelper localViewDragHelper = this.mLeftDragger;
      int j = (int)f3;
      int k = (int)f4;
      View localView1 = localViewDragHelper.findTopChildUnder(j, k);
      if ((localView1 != null) && (isContentView(localView1)))
      {
        float f5 = this.mInitialMotionX;
        float f6 = f3 - f5;
        float f7 = this.mInitialMotionY;
        float f8 = f4 - f7;
        int m = this.mLeftDragger.getTouchSlop();
        float f9 = f6 * f6;
        float f10 = f8 * f8;
        float f11 = f9 + f10;
        float f12 = m * m;
        if (f11 < f12)
        {
          View localView2 = findOpenDrawer();
          if (localView2 != null)
            if (getDrawerLockMode(localView2) != 2)
              break label259;
        }
      }
      label259: for (bool = true; ; bool = false)
      {
        closeDrawers(bool);
        this.mDisallowInterceptRequested = false;
        break;
      }
      closeDrawers(true);
      this.mDisallowInterceptRequested = false;
      this.mChildrenCanceledTouch = false;
    }
  }

  public void openDrawer(int paramInt)
  {
    View localView = findDrawerWithGravity(paramInt);
    if (localView == null)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("No drawer view found with gravity ");
      String str1 = gravityToString(paramInt);
      String str2 = str1;
      throw new IllegalArgumentException(str2);
    }
    openDrawer(localView);
  }

  public void openDrawer(View paramView)
  {
    if (!isDrawerView(paramView))
    {
      String str = "View " + paramView + " is not a sliding drawer";
      throw new IllegalArgumentException(str);
    }
    if (this.mFirstLayout)
    {
      LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
      localLayoutParams.onScreen = 1.0F;
      localLayoutParams.knownOpen = true;
    }
    while (true)
    {
      invalidate();
      return;
      if (checkDrawerViewAbsoluteGravity(paramView, 3))
      {
        ViewDragHelper localViewDragHelper1 = this.mLeftDragger;
        int i = paramView.getTop();
        boolean bool1 = localViewDragHelper1.smoothSlideViewTo(paramView, 0, i);
      }
      else
      {
        ViewDragHelper localViewDragHelper2 = this.mRightDragger;
        int j = getWidth();
        int k = paramView.getWidth();
        int m = j - k;
        int n = paramView.getTop();
        boolean bool2 = localViewDragHelper2.smoothSlideViewTo(paramView, m, n);
      }
    }
  }

  public void requestDisallowInterceptTouchEvent(boolean paramBoolean)
  {
    super.requestDisallowInterceptTouchEvent(paramBoolean);
    this.mDisallowInterceptRequested = paramBoolean;
    if (!paramBoolean)
      return;
    closeDrawers(true);
  }

  public void requestLayout()
  {
    if (this.mInLayout)
      return;
    super.requestLayout();
  }

  public void setDrawerListener(DrawerListener paramDrawerListener)
  {
    this.mListener = paramDrawerListener;
  }

  public void setDrawerLockMode(int paramInt)
  {
    setDrawerLockMode(paramInt, 3);
    setDrawerLockMode(paramInt, 5);
  }

  public void setDrawerLockMode(int paramInt1, int paramInt2)
  {
    int i = ViewCompat.getLayoutDirection(this);
    int j = GravityCompat.getAbsoluteGravity(paramInt2, i);
    if (j == 3)
    {
      this.mLockModeLeft = paramInt1;
      label23: if (paramInt1 != 0)
        if (j != 3)
          break label83;
    }
    label83: for (ViewDragHelper localViewDragHelper = this.mLeftDragger; ; localViewDragHelper = this.mRightDragger)
    {
      localViewDragHelper.cancel();
      switch (paramInt1)
      {
      default:
        return;
        if (j != 5)
          break label23;
        this.mLockModeRight = paramInt1;
        break label23;
      case 2:
      case 1:
      }
    }
    View localView1 = findDrawerWithGravity(j);
    if (localView1 == null)
      return;
    openDrawer(localView1);
    return;
    View localView2 = findDrawerWithGravity(j);
    if (localView2 == null)
      return;
    closeDrawer(localView2);
  }

  public void setDrawerLockMode(int paramInt, View paramView)
  {
    if (!isDrawerView(paramView))
    {
      String str = "View " + paramView + " is not a " + "drawer with appropriate layout_gravity";
      throw new IllegalArgumentException(str);
    }
    int i = ((LayoutParams)paramView.getLayoutParams()).gravity;
    setDrawerLockMode(paramInt, i);
  }

  public void setDrawerShadow(int paramInt1, int paramInt2)
  {
    Drawable localDrawable = getResources().getDrawable(paramInt1);
    setDrawerShadow(localDrawable, paramInt2);
  }

  public void setDrawerShadow(Drawable paramDrawable, int paramInt)
  {
    int i = ViewCompat.getLayoutDirection(this);
    int j = GravityCompat.getAbsoluteGravity(paramInt, i);
    if ((j & 0x3) == 3)
    {
      this.mShadowLeft = paramDrawable;
      invalidate();
    }
    if ((j & 0x5) != 5)
      return;
    this.mShadowRight = paramDrawable;
    invalidate();
  }

  void setDrawerViewOffset(View paramView, float paramFloat)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    float f = localLayoutParams.onScreen;
    if (paramFloat == f)
      return;
    localLayoutParams.onScreen = paramFloat;
    dispatchOnDrawerSlide(paramView, paramFloat);
  }

  public void setScrimColor(int paramInt)
  {
    this.mScrimColor = paramInt;
    invalidate();
  }

  void updateDrawerState(int paramInt1, int paramInt2, View paramView)
  {
    int i = this.mLeftDragger.getViewDragState();
    int j = this.mRightDragger.getViewDragState();
    int k;
    LayoutParams localLayoutParams;
    if ((i == 1) || (j == 1))
    {
      k = 1;
      if ((paramView != null) && (paramInt2 == 0))
      {
        localLayoutParams = (LayoutParams)paramView.getLayoutParams();
        if (localLayoutParams.onScreen != 0.0F)
          break label130;
        dispatchOnDrawerClosed(paramView);
      }
    }
    while (true)
    {
      int m = this.mDrawerState;
      if (k != m)
        return;
      this.mDrawerState = k;
      if (this.mListener == null)
        return;
      this.mListener.onDrawerStateChanged(k);
      return;
      if ((i == 2) || (j == 2))
      {
        k = 2;
        break;
      }
      k = 0;
      break;
      label130: if (localLayoutParams.onScreen == 1.0F)
        dispatchOnDrawerOpened(paramView);
    }
  }

  class AccessibilityDelegate extends AccessibilityDelegateCompat
  {
    private final Rect mTmpRect;

    AccessibilityDelegate()
    {
      Rect localRect = new Rect();
      this.mTmpRect = localRect;
    }

    private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat1, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat2)
    {
      Rect localRect = this.mTmpRect;
      paramAccessibilityNodeInfoCompat2.getBoundsInParent(localRect);
      paramAccessibilityNodeInfoCompat1.setBoundsInParent(localRect);
      paramAccessibilityNodeInfoCompat2.getBoundsInScreen(localRect);
      paramAccessibilityNodeInfoCompat1.setBoundsInScreen(localRect);
      boolean bool1 = paramAccessibilityNodeInfoCompat2.isVisibleToUser();
      paramAccessibilityNodeInfoCompat1.setVisibleToUser(bool1);
      CharSequence localCharSequence1 = paramAccessibilityNodeInfoCompat2.getPackageName();
      paramAccessibilityNodeInfoCompat1.setPackageName(localCharSequence1);
      CharSequence localCharSequence2 = paramAccessibilityNodeInfoCompat2.getClassName();
      paramAccessibilityNodeInfoCompat1.setClassName(localCharSequence2);
      CharSequence localCharSequence3 = paramAccessibilityNodeInfoCompat2.getContentDescription();
      paramAccessibilityNodeInfoCompat1.setContentDescription(localCharSequence3);
      boolean bool2 = paramAccessibilityNodeInfoCompat2.isEnabled();
      paramAccessibilityNodeInfoCompat1.setEnabled(bool2);
      boolean bool3 = paramAccessibilityNodeInfoCompat2.isClickable();
      paramAccessibilityNodeInfoCompat1.setClickable(bool3);
      boolean bool4 = paramAccessibilityNodeInfoCompat2.isFocusable();
      paramAccessibilityNodeInfoCompat1.setFocusable(bool4);
      boolean bool5 = paramAccessibilityNodeInfoCompat2.isFocused();
      paramAccessibilityNodeInfoCompat1.setFocused(bool5);
      boolean bool6 = paramAccessibilityNodeInfoCompat2.isAccessibilityFocused();
      paramAccessibilityNodeInfoCompat1.setAccessibilityFocused(bool6);
      boolean bool7 = paramAccessibilityNodeInfoCompat2.isSelected();
      paramAccessibilityNodeInfoCompat1.setSelected(bool7);
      boolean bool8 = paramAccessibilityNodeInfoCompat2.isLongClickable();
      paramAccessibilityNodeInfoCompat1.setLongClickable(bool8);
      int i = paramAccessibilityNodeInfoCompat2.getActions();
      paramAccessibilityNodeInfoCompat1.addAction(i);
    }

    public boolean filter(View paramView)
    {
      View localView = DrawerLayout.this.findOpenDrawer();
      if ((localView != null) && (localView != paramView));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
      AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(paramAccessibilityNodeInfoCompat);
      super.onInitializeAccessibilityNodeInfo(paramView, localAccessibilityNodeInfoCompat);
      paramAccessibilityNodeInfoCompat.setSource(paramView);
      ViewParent localViewParent = ViewCompat.getParentForAccessibility(paramView);
      if ((localViewParent instanceof View))
      {
        View localView1 = (View)localViewParent;
        paramAccessibilityNodeInfoCompat.setParent(localView1);
      }
      copyNodeInfoNoChildren(paramAccessibilityNodeInfoCompat, localAccessibilityNodeInfoCompat);
      localAccessibilityNodeInfoCompat.recycle();
      int i = DrawerLayout.this.getChildCount();
      int j = 0;
      while (true)
      {
        if (j >= i)
          return;
        View localView2 = DrawerLayout.this.getChildAt(j);
        if (!filter(localView2))
          paramAccessibilityNodeInfoCompat.addChild(localView2);
        j += 1;
      }
    }

    public boolean onRequestSendAccessibilityEvent(ViewGroup paramViewGroup, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      if (!filter(paramView));
      for (boolean bool = super.onRequestSendAccessibilityEvent(paramViewGroup, paramView, paramAccessibilityEvent); ; bool = false)
        return bool;
    }
  }

  public static class LayoutParams extends ViewGroup.MarginLayoutParams
  {
    public int gravity = 0;
    boolean isPeeking;
    boolean knownOpen;
    float onScreen;

    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }

    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      int[] arrayOfInt = DrawerLayout.LAYOUT_ATTRS;
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt);
      int i = localTypedArray.getInt(0, 0);
      this.gravity = i;
      localTypedArray.recycle();
    }

    public LayoutParams(LayoutParams paramLayoutParams)
    {
      super();
      int i = paramLayoutParams.gravity;
      this.gravity = i;
    }

    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }

    public LayoutParams(ViewGroup.MarginLayoutParams paramMarginLayoutParams)
    {
      super();
    }
  }

  private class ViewDragCallback extends ViewDragHelper.Callback
  {
    private final int mAbsGravity;
    private ViewDragHelper mDragger;
    private final Runnable mPeekRunnable;

    public ViewDragCallback(int arg2)
    {
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          DrawerLayout.ViewDragCallback.this.peekDrawer();
        }
      };
      this.mPeekRunnable = local1;
      int i;
      this.mAbsGravity = i;
    }

    private void closeOtherDrawer()
    {
      int i = 3;
      if (this.mAbsGravity != i)
        i = 5;
      View localView = DrawerLayout.this.findDrawerWithGravity(i);
      if (localView == null)
        return;
      DrawerLayout.this.closeDrawer(localView);
    }

    private void peekDrawer()
    {
      int i = 0;
      int j = this.mDragger.getEdgeSize();
      int k;
      View localView;
      if (this.mAbsGravity == 3)
      {
        k = 1;
        if (k == 0)
          break label166;
        localView = DrawerLayout.this.findDrawerWithGravity(3);
        if (localView != null)
          i = -localView.getWidth();
      }
      for (int m = i + j; ; m = DrawerLayout.this.getWidth() - j)
      {
        if (localView == null)
          return;
        if ((k == 0) || (localView.getLeft() >= m))
        {
          if (k != 0)
            return;
          if (localView.getLeft() <= m)
            return;
        }
        if (DrawerLayout.this.getDrawerLockMode(localView) != 0)
          return;
        DrawerLayout.LayoutParams localLayoutParams = (DrawerLayout.LayoutParams)localView.getLayoutParams();
        ViewDragHelper localViewDragHelper = this.mDragger;
        int n = localView.getTop();
        boolean bool = localViewDragHelper.smoothSlideViewTo(localView, m, n);
        localLayoutParams.isPeeking = true;
        DrawerLayout.this.invalidate();
        closeOtherDrawer();
        DrawerLayout.this.cancelChildViewTouch();
        return;
        k = 0;
        break;
        label166: localView = DrawerLayout.this.findDrawerWithGravity(5);
      }
    }

    public int clampViewPositionHorizontal(View paramView, int paramInt1, int paramInt2)
    {
      int i;
      int j;
      if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(paramView, 3))
      {
        i = -paramView.getWidth();
        j = Math.min(paramInt1, 0);
      }
      int i1;
      int i2;
      for (int k = Math.max(i, j); ; k = Math.max(i1, i2))
      {
        return k;
        int m = DrawerLayout.this.getWidth();
        int n = paramView.getWidth();
        i1 = m - n;
        i2 = Math.min(paramInt1, m);
      }
    }

    public int clampViewPositionVertical(View paramView, int paramInt1, int paramInt2)
    {
      return paramView.getTop();
    }

    public int getViewHorizontalDragRange(View paramView)
    {
      return paramView.getWidth();
    }

    public void onEdgeDragStarted(int paramInt1, int paramInt2)
    {
      if ((paramInt1 & 0x1) == 1);
      for (View localView = DrawerLayout.this.findDrawerWithGravity(3); ; localView = DrawerLayout.this.findDrawerWithGravity(5))
      {
        if (localView == null)
          return;
        if (DrawerLayout.this.getDrawerLockMode(localView) != 0)
          return;
        this.mDragger.captureChildView(localView, paramInt2);
        return;
      }
    }

    public boolean onEdgeLock(int paramInt)
    {
      return false;
    }

    public void onEdgeTouched(int paramInt1, int paramInt2)
    {
      DrawerLayout localDrawerLayout = DrawerLayout.this;
      Runnable localRunnable = this.mPeekRunnable;
      boolean bool = localDrawerLayout.postDelayed(localRunnable, 160L);
    }

    public void onViewCaptured(View paramView, int paramInt)
    {
      ((DrawerLayout.LayoutParams)paramView.getLayoutParams()).isPeeking = false;
      closeOtherDrawer();
    }

    public void onViewDragStateChanged(int paramInt)
    {
      DrawerLayout localDrawerLayout = DrawerLayout.this;
      int i = this.mAbsGravity;
      View localView = this.mDragger.getCapturedView();
      localDrawerLayout.updateDrawerState(i, paramInt, localView);
    }

    public void onViewPositionChanged(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      int i = paramView.getWidth();
      float f3;
      if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(paramView, 3))
      {
        float f1 = i + paramInt1;
        float f2 = i;
        f3 = f1 / f2;
        DrawerLayout.this.setDrawerViewOffset(paramView, f3);
        if (f3 != 0.0F)
          break label99;
      }
      label99: for (int j = 4; ; j = 0)
      {
        paramView.setVisibility(j);
        DrawerLayout.this.invalidate();
        return;
        float f4 = DrawerLayout.this.getWidth() - paramInt1;
        float f5 = i;
        f3 = f4 / f5;
        break;
      }
    }

    public void onViewReleased(View paramView, float paramFloat1, float paramFloat2)
    {
      float f = DrawerLayout.this.getDrawerViewOffset(paramView);
      int i = paramView.getWidth();
      if (DrawerLayout.this.checkDrawerViewAbsoluteGravity(paramView, 3))
      {
        if ((paramFloat1 > 0.0F) || ((paramFloat1 == 0.0F) && (f > 0.5F)));
        for (j = 0; ; j = -i)
        {
          ViewDragHelper localViewDragHelper = this.mDragger;
          int k = paramView.getTop();
          boolean bool = localViewDragHelper.settleCapturedViewAt(j, k);
          DrawerLayout.this.invalidate();
          return;
        }
      }
      int m = DrawerLayout.this.getWidth();
      if ((paramFloat1 < 0.0F) || ((paramFloat1 == 0.0F) && (f < 0.5F)));
      for (int j = m - i; ; j = m)
        break;
    }

    public void removeCallbacks()
    {
      DrawerLayout localDrawerLayout = DrawerLayout.this;
      Runnable localRunnable = this.mPeekRunnable;
      boolean bool = localDrawerLayout.removeCallbacks(localRunnable);
    }

    public void setDragger(ViewDragHelper paramViewDragHelper)
    {
      this.mDragger = paramViewDragHelper;
    }

    public boolean tryCaptureView(View paramView, int paramInt)
    {
      if (DrawerLayout.this.isDrawerView(paramView))
      {
        DrawerLayout localDrawerLayout = DrawerLayout.this;
        int i = this.mAbsGravity;
        if ((!localDrawerLayout.checkDrawerViewAbsoluteGravity(paramView, i)) || (DrawerLayout.this.getDrawerLockMode(paramView) != 0));
      }
      for (boolean bool = true; ; bool = false)
        return bool;
    }
  }

  protected static class SavedState extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public DrawerLayout.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new DrawerLayout.SavedState(paramAnonymousParcel);
      }

      public DrawerLayout.SavedState[] newArray(int paramAnonymousInt)
      {
        return new DrawerLayout.SavedState[paramAnonymousInt];
      }
    };
    int lockModeLeft = 0;
    int lockModeRight = 0;
    int openDrawerGravity = 0;

    public SavedState(Parcel paramParcel)
    {
      super();
      int i = paramParcel.readInt();
      this.openDrawerGravity = i;
    }

    public SavedState(Parcelable paramParcelable)
    {
      super();
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      int i = this.openDrawerGravity;
      paramParcel.writeInt(i);
    }
  }

  public static abstract class SimpleDrawerListener
    implements DrawerLayout.DrawerListener
  {
    public void onDrawerClosed(View paramView)
    {
    }

    public void onDrawerOpened(View paramView)
    {
    }

    public void onDrawerSlide(View paramView, float paramFloat)
    {
    }

    public void onDrawerStateChanged(int paramInt)
    {
    }
  }

  public static abstract interface DrawerListener
  {
    public abstract void onDrawerClosed(View paramView);

    public abstract void onDrawerOpened(View paramView);

    public abstract void onDrawerSlide(View paramView, float paramFloat);

    public abstract void onDrawerStateChanged(int paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.widget.DrawerLayout
 * JD-Core Version:    0.6.2
 */