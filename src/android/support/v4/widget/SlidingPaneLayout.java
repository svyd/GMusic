package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SlidingPaneLayout extends ViewGroup
{
  static final SlidingPanelLayoutImpl IMPL = new SlidingPanelLayoutImplBase();
  private boolean mCanSlide;
  private int mCoveredFadeColor;
  private final ViewDragHelper mDragHelper;
  private boolean mFirstLayout = true;
  private float mInitialMotionX;
  private float mInitialMotionY;
  private boolean mIsUnableToDrag;
  private final int mOverhangSize;
  private PanelSlideListener mPanelSlideListener;
  private int mParallaxBy;
  private float mParallaxOffset;
  private final ArrayList<DisableLayerRunnable> mPostedRunnables;
  private boolean mPreservedOpenState;
  private Drawable mShadowDrawable;
  private float mSlideOffset;
  private int mSlideRange;
  private View mSlideableView;
  private int mSliderFadeColor = -858993460;
  private final Rect mTmpRect;

  static
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 17)
    {
      IMPL = new SlidingPanelLayoutImplJBMR1();
      return;
    }
    if (i >= 16)
    {
      IMPL = new SlidingPanelLayoutImplJB();
      return;
    }
  }

  public SlidingPaneLayout(Context paramContext)
  {
    this(paramContext, null);
  }

  public SlidingPaneLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public SlidingPaneLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    Rect localRect = new Rect();
    this.mTmpRect = localRect;
    ArrayList localArrayList = new ArrayList();
    this.mPostedRunnables = localArrayList;
    float f1 = paramContext.getResources().getDisplayMetrics().density;
    int i = (int)(32.0F * f1 + 0.5F);
    this.mOverhangSize = i;
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(paramContext);
    setWillNotDraw(false);
    AccessibilityDelegate localAccessibilityDelegate = new AccessibilityDelegate();
    ViewCompat.setAccessibilityDelegate(this, localAccessibilityDelegate);
    ViewCompat.setImportantForAccessibility(this, 1);
    DragHelperCallback localDragHelperCallback = new DragHelperCallback(null);
    ViewDragHelper localViewDragHelper1 = ViewDragHelper.create(this, 0.5F, localDragHelperCallback);
    this.mDragHelper = localViewDragHelper1;
    this.mDragHelper.setEdgeTrackingEnabled(1);
    ViewDragHelper localViewDragHelper2 = this.mDragHelper;
    float f2 = 400.0F * f1;
    localViewDragHelper2.setMinVelocity(f2);
  }

  private boolean closePane(View paramView, int paramInt)
  {
    boolean bool = false;
    if ((this.mFirstLayout) || (smoothSlideTo(0.0F, paramInt)))
    {
      this.mPreservedOpenState = false;
      bool = true;
    }
    return bool;
  }

  private void dimChildView(View paramView, float paramFloat, int paramInt)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    if ((paramFloat > 0.0F) && (paramInt != 0))
    {
      int i = (int)(((0xFF000000 & paramInt) >>> 24) * paramFloat) << 24;
      int j = 0xFFFFFF & paramInt;
      int k = i | j;
      if (localLayoutParams.dimPaint == null)
      {
        Paint localPaint1 = new Paint();
        localLayoutParams.dimPaint = localPaint1;
      }
      Paint localPaint2 = localLayoutParams.dimPaint;
      PorterDuff.Mode localMode = PorterDuff.Mode.SRC_OVER;
      PorterDuffColorFilter localPorterDuffColorFilter = new PorterDuffColorFilter(k, localMode);
      ColorFilter localColorFilter1 = localPaint2.setColorFilter(localPorterDuffColorFilter);
      if (ViewCompat.getLayerType(paramView) != 2)
      {
        Paint localPaint3 = localLayoutParams.dimPaint;
        ViewCompat.setLayerType(paramView, 2, localPaint3);
      }
      invalidateChildRegion(paramView);
      return;
    }
    if (ViewCompat.getLayerType(paramView) == 0)
      return;
    if (localLayoutParams.dimPaint != null)
      ColorFilter localColorFilter2 = localLayoutParams.dimPaint.setColorFilter(null);
    DisableLayerRunnable localDisableLayerRunnable = new DisableLayerRunnable(paramView);
    boolean bool = this.mPostedRunnables.add(localDisableLayerRunnable);
    ViewCompat.postOnAnimation(this, localDisableLayerRunnable);
  }

  private void invalidateChildRegion(View paramView)
  {
    IMPL.invalidateChildRegion(this, paramView);
  }

  private void onPanelDragged(int paramInt)
  {
    LayoutParams localLayoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
    int i = getPaddingLeft();
    int j = localLayoutParams.leftMargin;
    int k = i + j;
    float f1 = paramInt - k;
    float f2 = this.mSlideRange;
    float f3 = f1 / f2;
    this.mSlideOffset = f3;
    if (this.mParallaxBy != 0)
    {
      float f4 = this.mSlideOffset;
      parallaxOtherViews(f4);
    }
    if (localLayoutParams.dimWhenOffset)
    {
      View localView1 = this.mSlideableView;
      float f5 = this.mSlideOffset;
      int m = this.mSliderFadeColor;
      dimChildView(localView1, f5, m);
    }
    View localView2 = this.mSlideableView;
    dispatchOnPanelSlide(localView2);
  }

  private boolean openPane(View paramView, int paramInt)
  {
    boolean bool = true;
    if ((this.mFirstLayout) || (smoothSlideTo(1.0F, paramInt)))
      this.mPreservedOpenState = true;
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  private void parallaxOtherViews(float paramFloat)
  {
    LayoutParams localLayoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
    int i;
    int k;
    label36: View localView1;
    if ((localLayoutParams.dimWhenOffset) && (localLayoutParams.leftMargin <= 0))
    {
      i = 1;
      int j = getChildCount();
      k = 0;
      if (k >= j)
        return;
      localView1 = getChildAt(k);
      View localView2 = this.mSlideableView;
      if (localView1 != localView2)
        break label79;
    }
    while (true)
    {
      k += 1;
      break label36;
      i = 0;
      break;
      label79: float f1 = this.mParallaxOffset;
      float f2 = 1.0F - f1;
      float f3 = this.mParallaxBy;
      int m = (int)(f2 * f3);
      this.mParallaxOffset = paramFloat;
      float f4 = 1.0F - paramFloat;
      float f5 = this.mParallaxBy;
      int n = (int)(f4 * f5);
      int i1 = m - n;
      localView1.offsetLeftAndRight(i1);
      if (i != 0)
      {
        float f6 = this.mParallaxOffset;
        float f7 = 1.0F - f6;
        int i2 = this.mCoveredFadeColor;
        dimChildView(localView1, f7, i2);
      }
    }
  }

  private static boolean viewIsOpaque(View paramView)
  {
    boolean bool = true;
    if (ViewCompat.isOpaque(paramView));
    while (true)
    {
      return bool;
      if (Build.VERSION.SDK_INT >= 18)
      {
        bool = false;
      }
      else
      {
        Drawable localDrawable = paramView.getBackground();
        if (localDrawable != null)
        {
          if (localDrawable.getOpacity() != -1)
            bool = false;
        }
        else
          bool = false;
      }
    }
  }

  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if (((paramLayoutParams instanceof LayoutParams)) && (super.checkLayoutParams(paramLayoutParams)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean closePane()
  {
    View localView = this.mSlideableView;
    return closePane(localView, 0);
  }

  public void computeScroll()
  {
    if (!this.mDragHelper.continueSettling(true))
      return;
    if (!this.mCanSlide)
    {
      this.mDragHelper.abort();
      return;
    }
    ViewCompat.postInvalidateOnAnimation(this);
  }

  void dispatchOnPanelClosed(View paramView)
  {
    if (this.mPanelSlideListener != null)
      this.mPanelSlideListener.onPanelClosed(paramView);
    sendAccessibilityEvent(32);
  }

  void dispatchOnPanelOpened(View paramView)
  {
    if (this.mPanelSlideListener != null)
      this.mPanelSlideListener.onPanelOpened(paramView);
    sendAccessibilityEvent(32);
  }

  void dispatchOnPanelSlide(View paramView)
  {
    if (this.mPanelSlideListener == null)
      return;
    PanelSlideListener localPanelSlideListener = this.mPanelSlideListener;
    float f = this.mSlideOffset;
    localPanelSlideListener.onPanelSlide(paramView, f);
  }

  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    if (getChildCount() > 1);
    for (View localView = getChildAt(1); ; localView = null)
    {
      if (localView == null)
        return;
      if (this.mShadowDrawable != null)
        break;
      return;
    }
    int i = this.mShadowDrawable.getIntrinsicWidth();
    int j = localView.getLeft();
    int k = localView.getTop();
    int m = localView.getBottom();
    int n = j - i;
    this.mShadowDrawable.setBounds(n, k, j, m);
    this.mShadowDrawable.draw(paramCanvas);
  }

  protected boolean drawChild(Canvas paramCanvas, View paramView, long paramLong)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = paramCanvas.save(2);
    if ((this.mCanSlide) && (!localLayoutParams.slideable) && (this.mSlideableView != null))
    {
      Rect localRect1 = this.mTmpRect;
      boolean bool1 = paramCanvas.getClipBounds(localRect1);
      Rect localRect2 = this.mTmpRect;
      int j = this.mTmpRect.right;
      int k = this.mSlideableView.getLeft();
      int m = Math.min(j, k);
      localRect2.right = m;
      Rect localRect3 = this.mTmpRect;
      boolean bool2 = paramCanvas.clipRect(localRect3);
    }
    boolean bool3;
    if (Build.VERSION.SDK_INT >= 11)
      bool3 = super.drawChild(paramCanvas, paramView, paramLong);
    while (true)
    {
      paramCanvas.restoreToCount(i);
      return bool3;
      if ((localLayoutParams.dimWhenOffset) && (this.mSlideOffset > 0.0F))
      {
        if (!paramView.isDrawingCacheEnabled())
          paramView.setDrawingCacheEnabled(true);
        Bitmap localBitmap = paramView.getDrawingCache();
        if (localBitmap != null)
        {
          float f1 = paramView.getLeft();
          float f2 = paramView.getTop();
          Paint localPaint = localLayoutParams.dimPaint;
          paramCanvas.drawBitmap(localBitmap, f1, f2, localPaint);
          bool3 = false;
        }
        else
        {
          String str = "drawChild: child view " + paramView + " returned null drawing cache";
          int n = Log.e("SlidingPaneLayout", str);
          bool3 = super.drawChild(paramCanvas, paramView, paramLong);
        }
      }
      else
      {
        if (paramView.isDrawingCacheEnabled())
          paramView.setDrawingCacheEnabled(false);
        bool3 = super.drawChild(paramCanvas, paramView, paramLong);
      }
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
    ViewGroup.MarginLayoutParams localMarginLayoutParams;
    if ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams))
      localMarginLayoutParams = (ViewGroup.MarginLayoutParams)paramLayoutParams;
    for (LayoutParams localLayoutParams = new LayoutParams(localMarginLayoutParams); ; localLayoutParams = new LayoutParams(paramLayoutParams))
      return localLayoutParams;
  }

  boolean isDimmed(View paramView)
  {
    boolean bool = false;
    if (paramView == null);
    while (true)
    {
      return bool;
      LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
      if ((this.mCanSlide) && (localLayoutParams.dimWhenOffset) && (this.mSlideOffset > 0.0F))
        bool = true;
    }
  }

  public boolean isOpen()
  {
    if ((!this.mCanSlide) || (this.mSlideOffset == 1.0F));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isSlideable()
  {
    return this.mCanSlide;
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
    int i = 0;
    int j = this.mPostedRunnables.size();
    while (i < j)
    {
      ((DisableLayerRunnable)this.mPostedRunnables.get(i)).run();
      i += 1;
    }
    this.mPostedRunnables.clear();
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    boolean bool1;
    if ((!this.mCanSlide) && (i == 0) && (getChildCount() > 1))
    {
      View localView1 = getChildAt(1);
      if (localView1 != null)
      {
        ViewDragHelper localViewDragHelper1 = this.mDragHelper;
        int j = (int)paramMotionEvent.getX();
        int k = (int)paramMotionEvent.getY();
        if (localViewDragHelper1.isViewUnder(localView1, j, k))
          break label111;
        bool1 = true;
        this.mPreservedOpenState = bool1;
      }
    }
    boolean bool2;
    if ((!this.mCanSlide) || ((this.mIsUnableToDrag) && (i != 0)))
    {
      this.mDragHelper.cancel();
      bool2 = super.onInterceptTouchEvent(paramMotionEvent);
    }
    while (true)
    {
      return bool2;
      label111: bool1 = false;
      break;
      if ((i == 3) || (i == 1))
      {
        this.mDragHelper.cancel();
        bool2 = false;
      }
      else
      {
        int m = 0;
        switch (i)
        {
        case 1:
        default:
        case 0:
        case 2:
        }
        while (true)
          if ((this.mDragHelper.shouldInterceptTouchEvent(paramMotionEvent)) || (m != 0))
          {
            bool2 = true;
            break;
            this.mIsUnableToDrag = false;
            float f1 = paramMotionEvent.getX();
            float f2 = paramMotionEvent.getY();
            this.mInitialMotionX = f1;
            this.mInitialMotionY = f2;
            ViewDragHelper localViewDragHelper2 = this.mDragHelper;
            View localView2 = this.mSlideableView;
            int n = (int)f1;
            int i1 = (int)f2;
            if (localViewDragHelper2.isViewUnder(localView2, n, i1))
            {
              View localView3 = this.mSlideableView;
              if (isDimmed(localView3))
              {
                m = 1;
                continue;
                float f3 = paramMotionEvent.getX();
                float f4 = paramMotionEvent.getY();
                float f5 = this.mInitialMotionX;
                float f6 = Math.abs(f3 - f5);
                float f7 = this.mInitialMotionY;
                float f8 = Math.abs(f4 - f7);
                float f9 = this.mDragHelper.getTouchSlop();
                if ((f6 > f9) && (f8 > f6))
                {
                  this.mDragHelper.cancel();
                  this.mIsUnableToDrag = true;
                  bool2 = false;
                  break;
                }
              }
            }
          }
        bool2 = false;
      }
    }
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramInt3 - paramInt1;
    int j = getPaddingLeft();
    int k = getPaddingRight();
    int m = getPaddingTop();
    int n = getChildCount();
    int i1 = j;
    int i2 = i1;
    if (this.mFirstLayout)
      if ((!this.mCanSlide) || (!this.mPreservedOpenState))
        break label119;
    int i3;
    View localView1;
    label119: for (float f1 = 1.0F; ; f1 = 0.0F)
    {
      float f2 = f1;
      this.mSlideOffset = f2;
      i3 = 0;
      while (true)
      {
        if (i3 >= n)
          break label451;
        localView1 = getChildAt(i3);
        int i4 = localView1.getVisibility();
        int i5 = 8;
        if (i4 != i5)
          break;
        i3 += 1;
      }
    }
    LayoutParams localLayoutParams = (LayoutParams)localView1.getLayoutParams();
    int i6 = localView1.getMeasuredWidth();
    int i7 = 0;
    boolean bool1;
    if (localLayoutParams.slideable)
    {
      int i8 = localLayoutParams.leftMargin;
      int i9 = localLayoutParams.rightMargin;
      int i10 = i8 + i9;
      int i11 = i - k;
      int i12 = this.mOverhangSize;
      int i13 = i11 - i12;
      int i14 = Math.min(i2, i13) - i1 - i10;
      int i15 = i14;
      this.mSlideRange = i15;
      int i16 = localLayoutParams.leftMargin + i1 + i14;
      int i17 = i6 / 2;
      int i18 = i16 + i17;
      int i19 = i - k;
      int i20 = i18;
      int i21 = i19;
      if (i20 > i21)
      {
        bool1 = true;
        label271: boolean bool2 = bool1;
        localLayoutParams.dimWhenOffset = bool2;
        float f3 = i14;
        float f4 = this.mSlideOffset;
        int i22 = (int)(f3 * f4);
        int i23 = localLayoutParams.leftMargin;
        int i24 = i22 + i23;
        i1 += i24;
      }
    }
    while (true)
    {
      int i25 = i1 - i7;
      int i26 = i25 + i6;
      int i27 = m;
      int i28 = localView1.getMeasuredHeight();
      int i29 = i27 + i28;
      int i30 = m;
      localView1.layout(i25, i30, i26, i29);
      int i31 = localView1.getWidth();
      i2 += i31;
      break;
      bool1 = false;
      break label271;
      if ((this.mCanSlide) && (this.mParallaxBy != 0))
      {
        float f5 = this.mSlideOffset;
        float f6 = 1.0F - f5;
        float f7 = this.mParallaxBy;
        i7 = (int)(f6 * f7);
        i1 = i2;
      }
      else
      {
        i1 = i2;
      }
    }
    label451: if (this.mFirstLayout)
    {
      if (!this.mCanSlide)
        break label582;
      if (this.mParallaxBy != 0)
      {
        float f8 = this.mSlideOffset;
        SlidingPaneLayout localSlidingPaneLayout1 = this;
        float f9 = f8;
        localSlidingPaneLayout1.parallaxOtherViews(f9);
      }
      if (((LayoutParams)this.mSlideableView.getLayoutParams()).dimWhenOffset)
      {
        View localView2 = this.mSlideableView;
        float f10 = this.mSlideOffset;
        int i32 = this.mSliderFadeColor;
        SlidingPaneLayout localSlidingPaneLayout2 = this;
        View localView3 = localView2;
        float f11 = f10;
        int i33 = i32;
        localSlidingPaneLayout2.dimChildView(localView3, f11, i33);
      }
    }
    while (true)
    {
      View localView4 = this.mSlideableView;
      SlidingPaneLayout localSlidingPaneLayout3 = this;
      View localView5 = localView4;
      localSlidingPaneLayout3.updateObscuredViewsVisibility(localView5);
      boolean bool3 = false;
      this.mFirstLayout = bool3;
      return;
      label582: i3 = 0;
      while (i3 < n)
      {
        View localView6 = getChildAt(i3);
        int i34 = this.mSliderFadeColor;
        SlidingPaneLayout localSlidingPaneLayout4 = this;
        View localView7 = localView6;
        float f12 = 0.0F;
        int i35 = i34;
        localSlidingPaneLayout4.dimChildView(localView7, f12, i35);
        i3 += 1;
      }
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = View.MeasureSpec.getSize(paramInt2);
    int n = i;
    int i1 = 1073741824;
    int i4;
    int i5;
    label96: float f1;
    boolean bool1;
    int i9;
    int i10;
    int i13;
    label168: View localView1;
    LayoutParams localLayoutParams1;
    if (n != i1)
      if (isInEditMode())
      {
        int i2 = i;
        int i3 = -2147483648;
        if (i2 == i3)
        {
          i4 = 0;
          i5 = 65535;
          switch (k)
          {
          default:
            f1 = 0.0F;
            bool1 = false;
            int i6 = getPaddingLeft();
            int i7 = j - i6;
            int i8 = getPaddingRight();
            i9 = i7 - i8;
            i10 = getChildCount();
            int i11 = 2;
            if (i10 > i11)
              int i12 = Log.e("SlidingPaneLayout", "onMeasure: More than two child views are not supported.");
            Object localObject = null;
            this.mSlideableView = localObject;
            i13 = 0;
            if (i13 >= i10)
              break label697;
            localView1 = getChildAt(i13);
            localLayoutParams1 = (LayoutParams)localView1.getLayoutParams();
            int i14 = localView1.getVisibility();
            int i15 = 8;
            if (i14 == i15)
            {
              boolean bool2 = false;
              localLayoutParams1.dimWhenOffset = bool2;
            }
            break;
          case 1073741824:
          case -2147483648:
          }
        }
      }
    do
    {
      i13 += 1;
      break label168;
      if (i != 0)
        break;
      j = 300;
      break;
      throw new IllegalStateException("Width must have an exact value or MATCH_PARENT");
      if (k != 0)
        break;
      if (isInEditMode())
      {
        if (k != 0)
          break;
        k = -2147483648;
        m = 300;
        break;
      }
      throw new IllegalStateException("Height must not be UNSPECIFIED");
      int i16 = getPaddingTop();
      int i17 = m - i16;
      int i18 = getPaddingBottom();
      i5 = i17 - i18;
      i4 = i5;
      break label96;
      int i19 = getPaddingTop();
      int i20 = m - i19;
      int i21 = getPaddingBottom();
      i5 = i20 - i21;
      break label96;
      if (localLayoutParams1.weight <= 0.0F)
        break label389;
      float f2 = localLayoutParams1.weight;
      f1 += f2;
    }
    while (localLayoutParams1.width == 0);
    label389: int i22 = localLayoutParams1.leftMargin;
    int i23 = localLayoutParams1.rightMargin;
    int i24 = i22 + i23;
    int i25 = localLayoutParams1.width;
    int i26 = 65534;
    int i27;
    label442: int i32;
    if (i25 == i26)
    {
      i27 = View.MeasureSpec.makeMeasureSpec(j - i24, -2147483648);
      int i28 = localLayoutParams1.height;
      int i29 = 65534;
      if (i28 != i29)
        break label635;
      int i30 = i5;
      int i31 = -2147483648;
      i32 = View.MeasureSpec.makeMeasureSpec(i30, i31);
      label479: localView1.measure(i27, i32);
      int i33 = localView1.getMeasuredWidth();
      int i34 = localView1.getMeasuredHeight();
      int i35 = -2147483648;
      if ((k == i35) && (i34 > i4))
      {
        int i36 = i5;
        i4 = Math.min(i34, i36);
      }
      i9 -= i33;
      if (i9 >= 0)
        break label691;
    }
    label691: for (boolean bool3 = true; ; bool3 = false)
    {
      boolean bool4 = bool3;
      localLayoutParams1.slideable = bool4;
      bool1 |= bool3;
      if (!localLayoutParams1.slideable)
        break;
      this.mSlideableView = localView1;
      break;
      int i37 = localLayoutParams1.width;
      int i38 = 65535;
      if (i37 == i38)
      {
        i27 = View.MeasureSpec.makeMeasureSpec(j - i24, 1073741824);
        break label442;
      }
      i27 = View.MeasureSpec.makeMeasureSpec(localLayoutParams1.width, 1073741824);
      break label442;
      label635: int i39 = localLayoutParams1.height;
      int i40 = 65535;
      if (i39 == i40)
      {
        int i41 = i5;
        int i42 = 1073741824;
        i32 = View.MeasureSpec.makeMeasureSpec(i41, i42);
        break label479;
      }
      i32 = View.MeasureSpec.makeMeasureSpec(localLayoutParams1.height, 1073741824);
      break label479;
    }
    label697: if ((bool1) || (f1 > 0.0F))
    {
      int i43 = this.mOverhangSize;
      int i44 = j - i43;
      i13 = 0;
      if (i13 < i10)
      {
        View localView2 = getChildAt(i13);
        int i45 = localView2.getVisibility();
        int i46 = 8;
        if (i45 == i46);
        while (true)
        {
          i13 += 1;
          break;
          LayoutParams localLayoutParams2 = (LayoutParams)localView2.getLayoutParams();
          int i47 = localView2.getVisibility();
          int i48 = 8;
          if (i47 != i48)
          {
            int i49;
            int i50;
            label826: int i55;
            if ((localLayoutParams2.width == 0) && (localLayoutParams2.weight > 0.0F))
            {
              i49 = 1;
              if (i49 != 0)
              {
                i50 = 0;
                if (!bool1)
                  break label1026;
                View localView3 = this.mSlideableView;
                if (localView2 == localView3)
                  break label1026;
                if ((localLayoutParams2.width >= 0) || ((i50 <= i44) && (localLayoutParams2.weight <= 0.0F)))
                  continue;
                if (i49 == 0)
                  break label1010;
                int i51 = localLayoutParams2.height;
                int i52 = 65534;
                if (i51 != i52)
                  break label954;
                int i53 = i5;
                int i54 = -2147483648;
                i55 = View.MeasureSpec.makeMeasureSpec(i53, i54);
              }
            }
            else
            {
              while (true)
              {
                int i56 = 1073741824;
                int i57 = View.MeasureSpec.makeMeasureSpec(i44, i56);
                localView2.measure(i57, i55);
                break;
                i49 = 0;
                break label818;
                i50 = localView2.getMeasuredWidth();
                break label826;
                label954: int i58 = localLayoutParams2.height;
                int i59 = 65535;
                if (i58 == i59)
                {
                  int i60 = i5;
                  int i61 = 1073741824;
                  i55 = View.MeasureSpec.makeMeasureSpec(i60, i61);
                }
                else
                {
                  i55 = View.MeasureSpec.makeMeasureSpec(localLayoutParams2.height, 1073741824);
                  continue;
                  label1010: i55 = View.MeasureSpec.makeMeasureSpec(localView2.getMeasuredHeight(), 1073741824);
                }
              }
              if (localLayoutParams2.weight > 0.0F)
              {
                if (localLayoutParams2.width == 0)
                {
                  int i62 = localLayoutParams2.height;
                  int i63 = 65534;
                  if (i62 == i63)
                  {
                    int i64 = i5;
                    int i65 = -2147483648;
                    i55 = View.MeasureSpec.makeMeasureSpec(i64, i65);
                  }
                }
                while (true)
                {
                  if (!bool1)
                    break label1232;
                  int i66 = localLayoutParams2.leftMargin;
                  int i67 = localLayoutParams2.rightMargin;
                  int i68 = i66 + i67;
                  int i69 = j - i68;
                  int i70 = i69;
                  int i71 = 1073741824;
                  int i72 = View.MeasureSpec.makeMeasureSpec(i70, i71);
                  int i73 = i50;
                  int i74 = i69;
                  if (i73 == i74)
                    break;
                  localView2.measure(i72, i55);
                  break;
                  int i75 = localLayoutParams2.height;
                  int i76 = 65535;
                  if (i75 == i76)
                  {
                    int i77 = i5;
                    int i78 = 1073741824;
                    i55 = View.MeasureSpec.makeMeasureSpec(i77, i78);
                  }
                  else
                  {
                    i55 = View.MeasureSpec.makeMeasureSpec(localLayoutParams2.height, 1073741824);
                    continue;
                    i55 = View.MeasureSpec.makeMeasureSpec(localView2.getMeasuredHeight(), 1073741824);
                  }
                }
                label1232: int i79 = 0;
                int i80 = i9;
                int i81 = Math.max(i79, i80);
                float f3 = localLayoutParams2.weight;
                float f4 = i81;
                int i82 = (int)(f3 * f4 / f1);
                int i83 = View.MeasureSpec.makeMeasureSpec(i50 + i82, 1073741824);
                localView2.measure(i83, i55);
              }
            }
          }
        }
      }
    }
    label818: SlidingPaneLayout localSlidingPaneLayout = this;
    label1026: int i84 = j;
    localSlidingPaneLayout.setMeasuredDimension(i84, i4);
    this.mCanSlide = bool1;
    if (this.mDragHelper.getViewDragState() == 0)
      return;
    if (bool1)
      return;
    this.mDragHelper.abort();
  }

  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    Parcelable localParcelable = localSavedState.getSuperState();
    super.onRestoreInstanceState(localParcelable);
    if (localSavedState.isOpen)
      boolean bool1 = openPane();
    while (true)
    {
      boolean bool2 = localSavedState.isOpen;
      this.mPreservedOpenState = bool2;
      return;
      boolean bool3 = closePane();
    }
  }

  protected Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable = super.onSaveInstanceState();
    SavedState localSavedState = new SavedState(localParcelable);
    if (isSlideable());
    for (boolean bool = isOpen(); ; bool = this.mPreservedOpenState)
    {
      localSavedState.isOpen = bool;
      return localSavedState;
    }
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (paramInt1 != paramInt3)
      return;
    this.mFirstLayout = true;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1;
    if (!this.mCanSlide)
      bool1 = super.onTouchEvent(paramMotionEvent);
    while (true)
    {
      return bool1;
      this.mDragHelper.processTouchEvent(paramMotionEvent);
      int i = paramMotionEvent.getAction();
      bool1 = true;
      switch (i & 0xFF)
      {
      default:
        break;
      case 0:
        float f1 = paramMotionEvent.getX();
        float f2 = paramMotionEvent.getY();
        this.mInitialMotionX = f1;
        this.mInitialMotionY = f2;
        break;
      case 1:
        View localView1 = this.mSlideableView;
        if (isDimmed(localView1))
        {
          float f3 = paramMotionEvent.getX();
          float f4 = paramMotionEvent.getY();
          float f5 = this.mInitialMotionX;
          float f6 = f3 - f5;
          float f7 = this.mInitialMotionY;
          float f8 = f4 - f7;
          int j = this.mDragHelper.getTouchSlop();
          float f9 = f6 * f6;
          float f10 = f8 * f8;
          float f11 = f9 + f10;
          float f12 = j * j;
          if (f11 < f12)
          {
            ViewDragHelper localViewDragHelper = this.mDragHelper;
            View localView2 = this.mSlideableView;
            int k = (int)f3;
            int m = (int)f4;
            if (localViewDragHelper.isViewUnder(localView2, k, m))
            {
              View localView3 = this.mSlideableView;
              boolean bool2 = closePane(localView3, 0);
            }
          }
        }
        break;
      }
    }
  }

  public boolean openPane()
  {
    View localView = this.mSlideableView;
    return openPane(localView, 0);
  }

  public void requestChildFocus(View paramView1, View paramView2)
  {
    super.requestChildFocus(paramView1, paramView2);
    if (isInTouchMode())
      return;
    if (this.mCanSlide)
      return;
    View localView = this.mSlideableView;
    if (paramView1 == localView);
    for (boolean bool = true; ; bool = false)
    {
      this.mPreservedOpenState = bool;
      return;
    }
  }

  void setAllChildrenVisible()
  {
    int i = 0;
    int j = getChildCount();
    while (true)
    {
      if (i >= j)
        return;
      View localView = getChildAt(i);
      if (localView.getVisibility() == 4)
        localView.setVisibility(0);
      i += 1;
    }
  }

  public void setCoveredFadeColor(int paramInt)
  {
    this.mCoveredFadeColor = paramInt;
  }

  public void setPanelSlideListener(PanelSlideListener paramPanelSlideListener)
  {
    this.mPanelSlideListener = paramPanelSlideListener;
  }

  public void setParallaxDistance(int paramInt)
  {
    this.mParallaxBy = paramInt;
    requestLayout();
  }

  public void setShadowDrawable(Drawable paramDrawable)
  {
    this.mShadowDrawable = paramDrawable;
  }

  public void setShadowResource(int paramInt)
  {
    Drawable localDrawable = getResources().getDrawable(paramInt);
    setShadowDrawable(localDrawable);
  }

  public void setSliderFadeColor(int paramInt)
  {
    this.mSliderFadeColor = paramInt;
  }

  boolean smoothSlideTo(float paramFloat, int paramInt)
  {
    boolean bool = false;
    if (!this.mCanSlide);
    while (true)
    {
      return bool;
      LayoutParams localLayoutParams = (LayoutParams)this.mSlideableView.getLayoutParams();
      int i = getPaddingLeft();
      int j = localLayoutParams.leftMargin;
      float f1 = i + j;
      float f2 = this.mSlideRange * paramFloat;
      int k = (int)(f1 + f2);
      ViewDragHelper localViewDragHelper = this.mDragHelper;
      View localView = this.mSlideableView;
      int m = this.mSlideableView.getTop();
      if (localViewDragHelper.smoothSlideViewTo(localView, k, m))
      {
        setAllChildrenVisible();
        ViewCompat.postInvalidateOnAnimation(this);
        bool = true;
      }
    }
  }

  void updateObscuredViewsVisibility(View paramView)
  {
    int i = getPaddingLeft();
    int j = getWidth();
    int k = getPaddingRight();
    int m = j - k;
    int n = getPaddingTop();
    int i1 = getHeight();
    int i2 = getPaddingBottom();
    int i3 = i1 - i2;
    int i4;
    int i5;
    int i6;
    int i7;
    if ((paramView != null) && (viewIsOpaque(paramView)))
    {
      i4 = paramView.getLeft();
      i5 = paramView.getRight();
      i6 = paramView.getTop();
      i7 = paramView.getBottom();
    }
    int i8;
    View localView1;
    while (true)
    {
      i8 = 0;
      int i9 = getChildCount();
      if (i8 >= i9)
        return;
      localView1 = getChildAt(i8);
      View localView2 = paramView;
      if (localView1 != localView2)
        break;
      return;
      i7 = 0;
      i6 = i7;
      i5 = i7;
      i4 = i7;
    }
    int i10 = localView1.getLeft();
    int i11 = Math.max(i, i10);
    int i12 = localView1.getTop();
    int i13 = Math.max(n, i12);
    int i14 = localView1.getRight();
    int i15 = Math.min(m, i14);
    int i16 = localView1.getBottom();
    int i17 = Math.min(i3, i16);
    if ((i11 >= i4) && (i13 >= i6) && (i15 <= i5) && (i17 <= i7));
    for (int i18 = 4; ; i18 = 0)
    {
      int i19 = i18;
      localView1.setVisibility(i19);
      i8 += 1;
      break;
    }
  }

  private class DisableLayerRunnable
    implements Runnable
  {
    final View mChildView;

    DisableLayerRunnable(View arg2)
    {
      Object localObject;
      this.mChildView = localObject;
    }

    public void run()
    {
      ViewParent localViewParent = this.mChildView.getParent();
      SlidingPaneLayout localSlidingPaneLayout1 = SlidingPaneLayout.this;
      if (localViewParent == localSlidingPaneLayout1)
      {
        ViewCompat.setLayerType(this.mChildView, 0, null);
        SlidingPaneLayout localSlidingPaneLayout2 = SlidingPaneLayout.this;
        View localView = this.mChildView;
        localSlidingPaneLayout2.invalidateChildRegion(localView);
      }
      boolean bool = SlidingPaneLayout.this.mPostedRunnables.remove(this);
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
      int j = paramAccessibilityNodeInfoCompat2.getMovementGranularities();
      paramAccessibilityNodeInfoCompat1.setMovementGranularities(j);
    }

    public boolean filter(View paramView)
    {
      return SlidingPaneLayout.this.isDimmed(paramView);
    }

    public void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      super.onInitializeAccessibilityEvent(paramView, paramAccessibilityEvent);
      String str = SlidingPaneLayout.class.getName();
      paramAccessibilityEvent.setClassName(str);
    }

    public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
      AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(paramAccessibilityNodeInfoCompat);
      super.onInitializeAccessibilityNodeInfo(paramView, localAccessibilityNodeInfoCompat);
      copyNodeInfoNoChildren(paramAccessibilityNodeInfoCompat, localAccessibilityNodeInfoCompat);
      localAccessibilityNodeInfoCompat.recycle();
      String str = SlidingPaneLayout.class.getName();
      paramAccessibilityNodeInfoCompat.setClassName(str);
      paramAccessibilityNodeInfoCompat.setSource(paramView);
      ViewParent localViewParent = ViewCompat.getParentForAccessibility(paramView);
      if ((localViewParent instanceof View))
      {
        View localView1 = (View)localViewParent;
        paramAccessibilityNodeInfoCompat.setParent(localView1);
      }
      int i = SlidingPaneLayout.this.getChildCount();
      int j = 0;
      while (true)
      {
        if (j >= i)
          return;
        View localView2 = SlidingPaneLayout.this.getChildAt(j);
        if ((!filter(localView2)) && (localView2.getVisibility() == 0))
        {
          ViewCompat.setImportantForAccessibility(localView2, 1);
          paramAccessibilityNodeInfoCompat.addChild(localView2);
        }
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

  static class SlidingPanelLayoutImplJBMR1 extends SlidingPaneLayout.SlidingPanelLayoutImplBase
  {
    public void invalidateChildRegion(SlidingPaneLayout paramSlidingPaneLayout, View paramView)
    {
      Paint localPaint = ((SlidingPaneLayout.LayoutParams)paramView.getLayoutParams()).dimPaint;
      ViewCompat.setLayerPaint(paramView, localPaint);
    }
  }

  static class SlidingPanelLayoutImplJB extends SlidingPaneLayout.SlidingPanelLayoutImplBase
  {
    private Method mGetDisplayList;
    private Field mRecreateDisplayList;

    SlidingPanelLayoutImplJB()
    {
      try
      {
        Class[] arrayOfClass = (Class[])null;
        Method localMethod = View.class.getDeclaredMethod("getDisplayList", arrayOfClass);
        this.mGetDisplayList = localMethod;
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        try
        {
          while (true)
          {
            Field localField = View.class.getDeclaredField("mRecreateDisplayList");
            this.mRecreateDisplayList = localField;
            this.mRecreateDisplayList.setAccessible(true);
            return;
            localNoSuchMethodException = localNoSuchMethodException;
            int i = Log.e("SlidingPaneLayout", "Couldn't fetch getDisplayList method; dimming won't work right.", localNoSuchMethodException);
          }
        }
        catch (NoSuchFieldException localNoSuchFieldException)
        {
          int j = Log.e("SlidingPaneLayout", "Couldn't fetch mRecreateDisplayList field; dimming will be slow.", localNoSuchFieldException);
        }
      }
    }

    public void invalidateChildRegion(SlidingPaneLayout paramSlidingPaneLayout, View paramView)
    {
      if ((this.mGetDisplayList != null) && (this.mRecreateDisplayList != null))
        try
        {
          this.mRecreateDisplayList.setBoolean(paramView, true);
          Method localMethod = this.mGetDisplayList;
          Object[] arrayOfObject = (Object[])null;
          Object localObject = localMethod.invoke(paramView, arrayOfObject);
          super.invalidateChildRegion(paramSlidingPaneLayout, paramView);
          return;
        }
        catch (Exception localException)
        {
          while (true)
            int i = Log.e("SlidingPaneLayout", "Error refreshing display list state", localException);
        }
      paramView.invalidate();
    }
  }

  static class SlidingPanelLayoutImplBase
    implements SlidingPaneLayout.SlidingPanelLayoutImpl
  {
    public void invalidateChildRegion(SlidingPaneLayout paramSlidingPaneLayout, View paramView)
    {
      int i = paramView.getLeft();
      int j = paramView.getTop();
      int k = paramView.getRight();
      int m = paramView.getBottom();
      ViewCompat.postInvalidateOnAnimation(paramSlidingPaneLayout, i, j, k, m);
    }
  }

  static abstract interface SlidingPanelLayoutImpl
  {
    public abstract void invalidateChildRegion(SlidingPaneLayout paramSlidingPaneLayout, View paramView);
  }

  static class SavedState extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public SlidingPaneLayout.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new SlidingPaneLayout.SavedState(paramAnonymousParcel, null);
      }

      public SlidingPaneLayout.SavedState[] newArray(int paramAnonymousInt)
      {
        return new SlidingPaneLayout.SavedState[paramAnonymousInt];
      }
    };
    boolean isOpen;

    private SavedState(Parcel paramParcel)
    {
      super();
      if (paramParcel.readInt() != 0);
      for (boolean bool = true; ; bool = false)
      {
        this.isOpen = bool;
        return;
      }
    }

    SavedState(Parcelable paramParcelable)
    {
      super();
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      if (this.isOpen);
      for (int i = 1; ; i = 0)
      {
        paramParcel.writeInt(i);
        return;
      }
    }
  }

  public static class LayoutParams extends ViewGroup.MarginLayoutParams
  {
    private static final int[] ATTRS = arrayOfInt;
    Paint dimPaint;
    boolean dimWhenOffset;
    boolean slideable;
    public float weight = 0.0F;

    static
    {
      int[] arrayOfInt = new int[1];
      arrayOfInt[0] = 16843137;
    }

    public LayoutParams()
    {
      super(-1);
    }

    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }

    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      int[] arrayOfInt = ATTRS;
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt);
      float f = localTypedArray.getFloat(0, 0.0F);
      this.weight = f;
      localTypedArray.recycle();
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

  private class DragHelperCallback extends ViewDragHelper.Callback
  {
    private DragHelperCallback()
    {
    }

    public int clampViewPositionHorizontal(View paramView, int paramInt1, int paramInt2)
    {
      SlidingPaneLayout.LayoutParams localLayoutParams = (SlidingPaneLayout.LayoutParams)SlidingPaneLayout.this.mSlideableView.getLayoutParams();
      int i = SlidingPaneLayout.this.getPaddingLeft();
      int j = localLayoutParams.leftMargin;
      int k = i + j;
      int m = SlidingPaneLayout.this.mSlideRange;
      int n = k + m;
      return Math.min(Math.max(paramInt1, k), n);
    }

    public int getViewHorizontalDragRange(View paramView)
    {
      return SlidingPaneLayout.this.mSlideRange;
    }

    public void onEdgeDragStarted(int paramInt1, int paramInt2)
    {
      ViewDragHelper localViewDragHelper = SlidingPaneLayout.this.mDragHelper;
      View localView = SlidingPaneLayout.this.mSlideableView;
      localViewDragHelper.captureChildView(localView, paramInt2);
    }

    public void onViewCaptured(View paramView, int paramInt)
    {
      SlidingPaneLayout.this.setAllChildrenVisible();
    }

    public void onViewDragStateChanged(int paramInt)
    {
      if (SlidingPaneLayout.this.mDragHelper.getViewDragState() != 0)
        return;
      if (SlidingPaneLayout.this.mSlideOffset == 0.0F)
      {
        SlidingPaneLayout localSlidingPaneLayout1 = SlidingPaneLayout.this;
        View localView1 = SlidingPaneLayout.this.mSlideableView;
        localSlidingPaneLayout1.updateObscuredViewsVisibility(localView1);
        SlidingPaneLayout localSlidingPaneLayout2 = SlidingPaneLayout.this;
        View localView2 = SlidingPaneLayout.this.mSlideableView;
        localSlidingPaneLayout2.dispatchOnPanelClosed(localView2);
        boolean bool1 = SlidingPaneLayout.access$502(SlidingPaneLayout.this, false);
        return;
      }
      SlidingPaneLayout localSlidingPaneLayout3 = SlidingPaneLayout.this;
      View localView3 = SlidingPaneLayout.this.mSlideableView;
      localSlidingPaneLayout3.dispatchOnPanelOpened(localView3);
      boolean bool2 = SlidingPaneLayout.access$502(SlidingPaneLayout.this, true);
    }

    public void onViewPositionChanged(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      SlidingPaneLayout.this.onPanelDragged(paramInt1);
      SlidingPaneLayout.this.invalidate();
    }

    public void onViewReleased(View paramView, float paramFloat1, float paramFloat2)
    {
      SlidingPaneLayout.LayoutParams localLayoutParams = (SlidingPaneLayout.LayoutParams)paramView.getLayoutParams();
      int i = SlidingPaneLayout.this.getPaddingLeft();
      int j = localLayoutParams.leftMargin;
      int k = i + j;
      if ((paramFloat1 > 0.0F) || ((paramFloat1 == 0.0F) && (SlidingPaneLayout.this.mSlideOffset > 0.5F)))
      {
        int m = SlidingPaneLayout.this.mSlideRange;
        k += m;
      }
      ViewDragHelper localViewDragHelper = SlidingPaneLayout.this.mDragHelper;
      int n = paramView.getTop();
      boolean bool = localViewDragHelper.settleCapturedViewAt(k, n);
      SlidingPaneLayout.this.invalidate();
    }

    public boolean tryCaptureView(View paramView, int paramInt)
    {
      if (SlidingPaneLayout.this.mIsUnableToDrag);
      for (boolean bool = false; ; bool = ((SlidingPaneLayout.LayoutParams)paramView.getLayoutParams()).slideable)
        return bool;
    }
  }

  public static abstract interface PanelSlideListener
  {
    public abstract void onPanelClosed(View paramView);

    public abstract void onPanelOpened(View paramView);

    public abstract void onPanelSlide(View paramView, float paramFloat);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.widget.SlidingPaneLayout
 * JD-Core Version:    0.6.2
 */