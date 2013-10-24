package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

public class ProgressBarICS extends View
{
  private static final int[] android_R_styleable_ProgressBar = { 16843062, 16843063, 16843064, 16843065, 16843066, 16843067, 16843068, 16843069, 16843070, 16843071, 16843039, 16843072, 16843040, 16843073 };
  private AlphaAnimation mAnimation;
  private int mBehavior;
  private Drawable mCurrentDrawable;
  private int mDuration;
  private boolean mInDrawing;
  private boolean mIndeterminate;
  private Drawable mIndeterminateDrawable;
  private Interpolator mInterpolator;
  private long mLastDrawTime;
  private int mMax;
  int mMaxHeight;
  int mMaxWidth;
  int mMinHeight;
  int mMinWidth;
  private boolean mNoInvalidate;
  private boolean mOnlyIndeterminate;
  private int mProgress;
  private Drawable mProgressDrawable;
  private RefreshProgressRunnable mRefreshProgressRunnable;
  Bitmap mSampleTile;
  private int mSecondaryProgress;
  private boolean mShouldStartAnimationDrawable;
  private Transformation mTransformation;
  private long mUiThreadId;

  public ProgressBarICS(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2)
  {
    super(paramContext, paramAttributeSet, paramInt1);
    long l = Thread.currentThread().getId();
    this.mUiThreadId = l;
    initProgressBar();
    int[] arrayOfInt = android_R_styleable_ProgressBar;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt, paramInt1, paramInt2);
    this.mNoInvalidate = true;
    int j = this.mMax;
    int k = localTypedArray.getInt(i, j);
    setMax(k);
    int m = this.mProgress;
    int n = localTypedArray.getInt(1, m);
    setProgress(n);
    int i1 = this.mSecondaryProgress;
    int i2 = localTypedArray.getInt(2, i1);
    setSecondaryProgress(i2);
    boolean bool1 = this.mIndeterminate;
    boolean bool2 = localTypedArray.getBoolean(3, bool1);
    boolean bool3 = this.mOnlyIndeterminate;
    boolean bool4 = localTypedArray.getBoolean(4, bool3);
    this.mOnlyIndeterminate = bool4;
    Drawable localDrawable1 = localTypedArray.getDrawable(5);
    if (localDrawable1 != null)
    {
      Drawable localDrawable2 = tileifyIndeterminate(localDrawable1);
      setIndeterminateDrawable(localDrawable2);
    }
    Drawable localDrawable3 = localTypedArray.getDrawable(6);
    if (localDrawable3 != null)
    {
      Drawable localDrawable4 = tileify(localDrawable3, false);
      setProgressDrawable(localDrawable4);
    }
    int i3 = this.mDuration;
    int i4 = localTypedArray.getInt(7, i3);
    this.mDuration = i4;
    int i5 = this.mBehavior;
    int i6 = localTypedArray.getInt(8, i5);
    this.mBehavior = i6;
    int i7 = this.mMinWidth;
    int i8 = localTypedArray.getDimensionPixelSize(9, i7);
    this.mMinWidth = i8;
    int i9 = this.mMaxWidth;
    int i10 = localTypedArray.getDimensionPixelSize(10, i9);
    this.mMaxWidth = i10;
    int i11 = this.mMinHeight;
    int i12 = localTypedArray.getDimensionPixelSize(11, i11);
    this.mMinHeight = i12;
    int i13 = this.mMaxHeight;
    int i14 = localTypedArray.getDimensionPixelSize(12, i13);
    this.mMaxHeight = i14;
    int i15 = localTypedArray.getResourceId(13, 17432587);
    if (i15 > 0)
      setInterpolator(paramContext, i15);
    localTypedArray.recycle();
    this.mNoInvalidate = false;
    if ((this.mOnlyIndeterminate) || (bool2))
      i = 1;
    setIndeterminate(i);
  }

  /** @deprecated */
  private void doRefreshProgress(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
  {
    try
    {
      float f3;
      Drawable localDrawable1;
      Drawable localDrawable2;
      if (this.mMax > 0)
      {
        float f1 = paramInt2;
        float f2 = this.mMax;
        f3 = f1 / f2;
        localDrawable1 = this.mCurrentDrawable;
        if (localDrawable1 == null)
          break label99;
        localDrawable2 = null;
        if ((localDrawable1 instanceof LayerDrawable))
          localDrawable2 = ((LayerDrawable)localDrawable1).findDrawableByLayerId(paramInt1);
        int i = (int)(10000.0F * f3);
        if (localDrawable2 == null)
          break label92;
        label73: boolean bool = localDrawable2.setLevel(i);
      }
      while (true)
      {
        return;
        f3 = 0.0F;
        break;
        label92: localDrawable2 = localDrawable1;
        break label73;
        label99: invalidate();
      }
    }
    finally
    {
    }
  }

  private void initProgressBar()
  {
    this.mMax = 100;
    this.mProgress = 0;
    this.mSecondaryProgress = 0;
    this.mIndeterminate = false;
    this.mOnlyIndeterminate = false;
    this.mDuration = 4000;
    this.mBehavior = 1;
    this.mMinWidth = 24;
    this.mMaxWidth = 48;
    this.mMinHeight = 24;
    this.mMaxHeight = 48;
  }

  /** @deprecated */
  private void refreshProgress(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    while (true)
    {
      try
      {
        long l1 = this.mUiThreadId;
        long l2 = Thread.currentThread().getId();
        if (l1 == l2)
        {
          doRefreshProgress(paramInt1, paramInt2, paramBoolean, true);
          return;
        }
        if (this.mRefreshProgressRunnable != null)
        {
          localRefreshProgressRunnable = this.mRefreshProgressRunnable;
          this.mRefreshProgressRunnable = null;
          localRefreshProgressRunnable.setup(paramInt1, paramInt2, paramBoolean);
          boolean bool = post(localRefreshProgressRunnable);
          continue;
        }
      }
      finally
      {
      }
      RefreshProgressRunnable localRefreshProgressRunnable = new RefreshProgressRunnable(paramInt1, paramInt2, paramBoolean);
    }
  }

  private Drawable tileify(Drawable paramDrawable, boolean paramBoolean)
  {
    if ((paramDrawable instanceof LayerDrawable))
    {
      LayerDrawable localLayerDrawable = (LayerDrawable)paramDrawable;
      int i = localLayerDrawable.getNumberOfLayers();
      Drawable[] arrayOfDrawable = new Drawable[i];
      int j = 0;
      if (j < i)
      {
        int k = localLayerDrawable.getId(j);
        Drawable localDrawable1 = localLayerDrawable.getDrawable(j);
        if ((k == 16908301) || (k == 16908303));
        for (boolean bool = true; ; bool = false)
        {
          Drawable localDrawable2 = tileify(localDrawable1, bool);
          arrayOfDrawable[j] = localDrawable2;
          j += 1;
          break;
        }
      }
      localObject1 = new LayerDrawable(arrayOfDrawable);
      int m = 0;
      while (m < i)
      {
        int n = localLayerDrawable.getId(m);
        ((LayerDrawable)localObject1).setId(m, n);
        m += 1;
      }
    }
    Object localObject2;
    if ((paramDrawable instanceof BitmapDrawable))
    {
      Bitmap localBitmap = ((BitmapDrawable)paramDrawable).getBitmap();
      if (this.mSampleTile == null)
        this.mSampleTile = localBitmap;
      Shape localShape = getDrawableShape();
      localObject2 = new ShapeDrawable(localShape);
      Shader.TileMode localTileMode1 = Shader.TileMode.REPEAT;
      Shader.TileMode localTileMode2 = Shader.TileMode.CLAMP;
      BitmapShader localBitmapShader = new BitmapShader(localBitmap, localTileMode1, localTileMode2);
      Shader localShader = ((ShapeDrawable)localObject2).getPaint().setShader(localBitmapShader);
      if (paramBoolean)
        localObject2 = new ClipDrawable((Drawable)localObject2, 3, 1);
    }
    for (Object localObject1 = localObject2; ; localObject1 = paramDrawable)
      return localObject1;
  }

  private Drawable tileifyIndeterminate(Drawable paramDrawable)
  {
    if ((paramDrawable instanceof AnimationDrawable))
    {
      AnimationDrawable localAnimationDrawable1 = (AnimationDrawable)paramDrawable;
      int i = localAnimationDrawable1.getNumberOfFrames();
      AnimationDrawable localAnimationDrawable2 = new AnimationDrawable();
      boolean bool1 = localAnimationDrawable1.isOneShot();
      localAnimationDrawable2.setOneShot(bool1);
      int j = 0;
      while (j < i)
      {
        Drawable localDrawable1 = localAnimationDrawable1.getFrame(j);
        Drawable localDrawable2 = tileify(localDrawable1, true);
        boolean bool2 = localDrawable2.setLevel(10000);
        int k = localAnimationDrawable1.getDuration(j);
        localAnimationDrawable2.addFrame(localDrawable2, k);
        j += 1;
      }
      boolean bool3 = localAnimationDrawable2.setLevel(10000);
      paramDrawable = localAnimationDrawable2;
    }
    return paramDrawable;
  }

  private void updateDrawableBounds(int paramInt1, int paramInt2)
  {
    int i = getPaddingRight();
    int j = paramInt1 - i;
    int k = getPaddingLeft();
    int m = j - k;
    int n = getPaddingBottom();
    int i1 = paramInt2 - n;
    int i2 = getPaddingTop();
    int i3 = i1 - i2;
    int i4 = 0;
    int i5 = 0;
    float f3;
    if (this.mIndeterminateDrawable != null)
      if ((this.mOnlyIndeterminate) && (!(this.mIndeterminateDrawable instanceof AnimationDrawable)))
      {
        int i6 = this.mIndeterminateDrawable.getIntrinsicWidth();
        int i7 = this.mIndeterminateDrawable.getIntrinsicHeight();
        float f1 = i6;
        float f2 = i7;
        f3 = f1 / f2;
        float f4 = paramInt1;
        float f5 = paramInt2;
        float f6 = f4 / f5;
        if (f3 != f6)
        {
          if (f6 <= f3)
            break label204;
          int i8 = (int)(paramInt2 * f3);
          i5 = (paramInt1 - i8) / 2;
          m = i5 + i8;
        }
      }
    while (true)
    {
      this.mIndeterminateDrawable.setBounds(i5, i4, m, i3);
      if (this.mProgressDrawable == null)
        return;
      this.mProgressDrawable.setBounds(0, 0, m, i3);
      return;
      label204: float f7 = paramInt1;
      float f8 = 1.0F / f3;
      int i9 = (int)(f7 * f8);
      i4 = (paramInt2 - i9) / 2;
      i3 = i4 + i9;
    }
  }

  private void updateDrawableState()
  {
    int[] arrayOfInt = getDrawableState();
    if ((this.mProgressDrawable != null) && (this.mProgressDrawable.isStateful()))
      boolean bool1 = this.mProgressDrawable.setState(arrayOfInt);
    if (this.mIndeterminateDrawable == null)
      return;
    if (!this.mIndeterminateDrawable.isStateful())
      return;
    boolean bool2 = this.mIndeterminateDrawable.setState(arrayOfInt);
  }

  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    updateDrawableState();
  }

  Shape getDrawableShape()
  {
    float[] arrayOfFloat = { 1084227584, 1084227584, 1084227584, 1084227584, 1084227584, 1084227584, 1084227584, 1084227584 };
    return new RoundRectShape(arrayOfFloat, null, null);
  }

  public void invalidateDrawable(Drawable paramDrawable)
  {
    if (this.mInDrawing)
      return;
    if (verifyDrawable(paramDrawable))
    {
      Rect localRect = paramDrawable.getBounds();
      int i = getScrollX();
      int j = getPaddingLeft();
      int k = i + j;
      int m = getScrollY();
      int n = getPaddingTop();
      int i1 = m + n;
      int i2 = localRect.left + k;
      int i3 = localRect.top + i1;
      int i4 = localRect.right + k;
      int i5 = localRect.bottom + i1;
      invalidate(i2, i3, i4, i5);
      return;
    }
    super.invalidateDrawable(paramDrawable);
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if (!this.mIndeterminate)
      return;
    startAnimation();
  }

  protected void onDetachedFromWindow()
  {
    if (this.mIndeterminate)
      stopAnimation();
    if (this.mRefreshProgressRunnable != null)
    {
      RefreshProgressRunnable localRefreshProgressRunnable = this.mRefreshProgressRunnable;
      boolean bool = removeCallbacks(localRefreshProgressRunnable);
    }
    super.onDetachedFromWindow();
  }

  /** @deprecated */
  protected void onDraw(Canvas paramCanvas)
  {
    try
    {
      super.onDraw(paramCanvas);
      Drawable localDrawable = this.mCurrentDrawable;
      float f4;
      boolean bool2;
      if (localDrawable != null)
      {
        int i = paramCanvas.save();
        float f1 = getPaddingLeft();
        float f2 = getPaddingTop();
        paramCanvas.translate(f1, f2);
        long l1 = getDrawingTime();
        if (this.mAnimation != null)
        {
          AlphaAnimation localAlphaAnimation = this.mAnimation;
          Transformation localTransformation = this.mTransformation;
          boolean bool1 = localAlphaAnimation.getTransformation(l1, localTransformation);
          float f3 = this.mTransformation.getAlpha();
          f4 = f3;
          bool2 = true;
        }
      }
      try
      {
        this.mInDrawing = bool2;
        int j = (int)(10000.0F * f4);
        boolean bool3 = localDrawable.setLevel(j);
        Object localObject1 = null;
        this.mInDrawing = localObject1;
        long l2 = SystemClock.uptimeMillis();
        long l3 = this.mLastDrawTime;
        if (l2 - l3 >= 200L)
        {
          long l4 = SystemClock.uptimeMillis();
          this.mLastDrawTime = l4;
          postInvalidateDelayed(200L);
        }
        localDrawable.draw(paramCanvas);
        paramCanvas.restore();
        if ((this.mShouldStartAnimationDrawable) && ((localDrawable instanceof Animatable)))
        {
          ((Animatable)localDrawable).start();
          this.mShouldStartAnimationDrawable = false;
        }
        return;
      }
      finally
      {
        localObject2 = finally;
        boolean bool4 = false;
        this.mInDrawing = bool4;
        throw localObject2;
      }
    }
    finally
    {
    }
  }

  /** @deprecated */
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    try
    {
      Drawable localDrawable = this.mCurrentDrawable;
      int i = 0;
      int j = 0;
      if (localDrawable != null)
      {
        int k = this.mMinWidth;
        int m = this.mMaxWidth;
        int n = localDrawable.getIntrinsicWidth();
        int i1 = Math.min(m, n);
        i = Math.max(k, i1);
        int i2 = this.mMinHeight;
        int i3 = this.mMaxHeight;
        int i4 = localDrawable.getIntrinsicHeight();
        int i5 = Math.min(i3, i4);
        j = Math.max(i2, i5);
      }
      updateDrawableState();
      int i6 = getPaddingLeft();
      int i7 = getPaddingRight();
      int i8 = i6 + i7;
      int i9 = i + i8;
      int i10 = getPaddingTop();
      int i11 = getPaddingBottom();
      int i12 = i10 + i11;
      int i13 = j + i12;
      int i14 = resolveSize(i9, paramInt1);
      int i15 = resolveSize(i13, paramInt2);
      setMeasuredDimension(i14, i15);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    Parcelable localParcelable = localSavedState.getSuperState();
    super.onRestoreInstanceState(localParcelable);
    int i = localSavedState.progress;
    setProgress(i);
    int j = localSavedState.secondaryProgress;
    setSecondaryProgress(j);
  }

  public Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable = super.onSaveInstanceState();
    SavedState localSavedState = new SavedState(localParcelable);
    int i = this.mProgress;
    localSavedState.progress = i;
    int j = this.mSecondaryProgress;
    localSavedState.secondaryProgress = j;
    return localSavedState;
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    updateDrawableBounds(paramInt1, paramInt2);
  }

  protected void onVisibilityChanged(View paramView, int paramInt)
  {
    super.onVisibilityChanged(paramView, paramInt);
    if (!this.mIndeterminate)
      return;
    if ((paramInt == 8) || (paramInt == 4))
    {
      stopAnimation();
      return;
    }
    startAnimation();
  }

  public void postInvalidate()
  {
    if (this.mNoInvalidate)
      return;
    super.postInvalidate();
  }

  /** @deprecated */
  public void setIndeterminate(boolean paramBoolean)
  {
    try
    {
      if ((!this.mOnlyIndeterminate) || (!this.mIndeterminate))
      {
        boolean bool = this.mIndeterminate;
        if (paramBoolean != bool)
        {
          this.mIndeterminate = paramBoolean;
          if (!paramBoolean)
            break label52;
          Drawable localDrawable1 = this.mIndeterminateDrawable;
          this.mCurrentDrawable = localDrawable1;
          startAnimation();
        }
      }
      while (true)
      {
        return;
        label52: Drawable localDrawable2 = this.mProgressDrawable;
        this.mCurrentDrawable = localDrawable2;
        stopAnimation();
      }
    }
    finally
    {
    }
  }

  public void setIndeterminateDrawable(Drawable paramDrawable)
  {
    if (paramDrawable != null)
      paramDrawable.setCallback(this);
    this.mIndeterminateDrawable = paramDrawable;
    if (!this.mIndeterminate)
      return;
    this.mCurrentDrawable = paramDrawable;
    postInvalidate();
  }

  public void setInterpolator(Context paramContext, int paramInt)
  {
    Interpolator localInterpolator = AnimationUtils.loadInterpolator(paramContext, paramInt);
    setInterpolator(localInterpolator);
  }

  public void setInterpolator(Interpolator paramInterpolator)
  {
    this.mInterpolator = paramInterpolator;
  }

  /** @deprecated */
  public void setMax(int paramInt)
  {
    if (paramInt < 0)
      paramInt = 0;
    try
    {
      int i = this.mMax;
      if (paramInt != i)
      {
        this.mMax = paramInt;
        postInvalidate();
        if (this.mProgress > paramInt)
          this.mProgress = paramInt;
        int j = this.mProgress;
        refreshProgress(16908301, j, false);
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  public void setProgress(int paramInt)
  {
    try
    {
      setProgress(paramInt, false);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  void setProgress(int paramInt, boolean paramBoolean)
  {
    try
    {
      boolean bool = this.mIndeterminate;
      if (bool);
      while (true)
      {
        return;
        if (paramInt < 0)
          paramInt = 0;
        int i = this.mMax;
        if (paramInt > i)
          paramInt = this.mMax;
        int j = this.mProgress;
        if (paramInt != j)
        {
          this.mProgress = paramInt;
          int k = this.mProgress;
          refreshProgress(16908301, k, paramBoolean);
        }
      }
    }
    finally
    {
    }
  }

  public void setProgressDrawable(Drawable paramDrawable)
  {
    if (this.mProgressDrawable != null)
    {
      Drawable localDrawable = this.mProgressDrawable;
      if (paramDrawable != localDrawable)
        this.mProgressDrawable.setCallback(null);
    }
    for (int i = 1; ; i = 0)
    {
      if (paramDrawable != null)
      {
        paramDrawable.setCallback(this);
        int j = paramDrawable.getMinimumHeight();
        if (this.mMaxHeight < j)
        {
          this.mMaxHeight = j;
          requestLayout();
        }
      }
      this.mProgressDrawable = paramDrawable;
      if (!this.mIndeterminate)
      {
        this.mCurrentDrawable = paramDrawable;
        postInvalidate();
      }
      if (i == 0)
        return;
      int k = getWidth();
      int m = getHeight();
      updateDrawableBounds(k, m);
      updateDrawableState();
      int n = this.mProgress;
      doRefreshProgress(16908301, n, false, false);
      int i1 = this.mSecondaryProgress;
      doRefreshProgress(16908303, i1, false, false);
      return;
    }
  }

  /** @deprecated */
  public void setSecondaryProgress(int paramInt)
  {
    try
    {
      boolean bool = this.mIndeterminate;
      if (bool);
      while (true)
      {
        return;
        if (paramInt < 0)
          paramInt = 0;
        int i = this.mMax;
        if (paramInt > i)
          paramInt = this.mMax;
        int j = this.mSecondaryProgress;
        if (paramInt != j)
        {
          this.mSecondaryProgress = paramInt;
          int k = this.mSecondaryProgress;
          refreshProgress(16908303, k, false);
        }
      }
    }
    finally
    {
    }
  }

  public void setVisibility(int paramInt)
  {
    if (getVisibility() != paramInt)
      return;
    super.setVisibility(paramInt);
    if (!this.mIndeterminate)
      return;
    if ((paramInt == 8) || (paramInt == 4))
    {
      stopAnimation();
      return;
    }
    startAnimation();
  }

  void startAnimation()
  {
    if (getVisibility() != 0)
      return;
    if ((this.mIndeterminateDrawable instanceof Animatable))
    {
      this.mShouldStartAnimationDrawable = true;
      this.mAnimation = null;
    }
    while (true)
    {
      postInvalidate();
      return;
      if (this.mInterpolator == null)
      {
        LinearInterpolator localLinearInterpolator = new LinearInterpolator();
        this.mInterpolator = localLinearInterpolator;
      }
      Transformation localTransformation = new Transformation();
      this.mTransformation = localTransformation;
      AlphaAnimation localAlphaAnimation1 = new AlphaAnimation(0.0F, 1.0F);
      this.mAnimation = localAlphaAnimation1;
      AlphaAnimation localAlphaAnimation2 = this.mAnimation;
      int i = this.mBehavior;
      localAlphaAnimation2.setRepeatMode(i);
      this.mAnimation.setRepeatCount(-1);
      AlphaAnimation localAlphaAnimation3 = this.mAnimation;
      long l = this.mDuration;
      localAlphaAnimation3.setDuration(l);
      AlphaAnimation localAlphaAnimation4 = this.mAnimation;
      Interpolator localInterpolator = this.mInterpolator;
      localAlphaAnimation4.setInterpolator(localInterpolator);
      this.mAnimation.setStartTime(65535L);
    }
  }

  void stopAnimation()
  {
    this.mAnimation = null;
    this.mTransformation = null;
    if ((this.mIndeterminateDrawable instanceof Animatable))
    {
      ((Animatable)this.mIndeterminateDrawable).stop();
      this.mShouldStartAnimationDrawable = false;
    }
    postInvalidate();
  }

  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    Drawable localDrawable1 = this.mProgressDrawable;
    if (paramDrawable != localDrawable1)
    {
      Drawable localDrawable2 = this.mIndeterminateDrawable;
      if ((paramDrawable != localDrawable2) && (!super.verifyDrawable(paramDrawable)))
        break label34;
    }
    label34: for (boolean bool = true; ; bool = false)
      return bool;
  }

  static class SavedState extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public ProgressBarICS.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ProgressBarICS.SavedState(paramAnonymousParcel, null);
      }

      public ProgressBarICS.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ProgressBarICS.SavedState[paramAnonymousInt];
      }
    };
    int progress;
    int secondaryProgress;

    private SavedState(Parcel paramParcel)
    {
      super();
      int i = paramParcel.readInt();
      this.progress = i;
      int j = paramParcel.readInt();
      this.secondaryProgress = j;
    }

    SavedState(Parcelable paramParcelable)
    {
      super();
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      int i = this.progress;
      paramParcel.writeInt(i);
      int j = this.secondaryProgress;
      paramParcel.writeInt(j);
    }
  }

  private class RefreshProgressRunnable
    implements Runnable
  {
    private boolean mFromUser;
    private int mId;
    private int mProgress;

    RefreshProgressRunnable(int paramInt1, int paramBoolean, boolean arg4)
    {
      this.mId = paramInt1;
      this.mProgress = paramBoolean;
      boolean bool;
      this.mFromUser = bool;
    }

    public void run()
    {
      ProgressBarICS localProgressBarICS = ProgressBarICS.this;
      int i = this.mId;
      int j = this.mProgress;
      boolean bool = this.mFromUser;
      localProgressBarICS.doRefreshProgress(i, j, bool, true);
      RefreshProgressRunnable localRefreshProgressRunnable = ProgressBarICS.access$102(ProgressBarICS.this, this);
    }

    public void setup(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      this.mId = paramInt1;
      this.mProgress = paramInt2;
      this.mFromUser = paramBoolean;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.ProgressBarICS
 * JD-Core Version:    0.6.2
 */