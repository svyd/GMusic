package com.google.android.music;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public final class SizableTrackSeekBar extends SafePostSeekBar
{
  private boolean mEnableAccessibility = false;
  private Drawable mThumb;
  private float mThumbAlpha = 1.0F;
  private int mTrackHeight;
  private int mTrackPadding;

  public SizableTrackSeekBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    int[] arrayOfInt = R.styleable.SizableSeekbar;
    int i = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt).getDimensionPixelSize(0, -1);
    if (i == -1)
      setTrackHeightDip(3.0F);
    while (true)
    {
      configureThumbPadding();
      return;
      setTrackHeight(i);
    }
  }

  private void configureThumbPadding()
  {
    if (this.mThumb != null)
    {
      int i = Math.round(this.mThumb.getIntrinsicWidth() / 2);
      this.mTrackPadding = i;
      if (this.mThumb != null)
        break label150;
    }
    label150: for (int j = 0; ; j = this.mThumb.getIntrinsicHeight())
    {
      int k = getPaddingRight();
      int m = getPaddingLeft();
      int n = getPaddingTop();
      int i1 = getPaddingBottom();
      int i2 = this.mTrackHeight;
      int i3 = (j - i2) / 2;
      Drawable localDrawable = getProgressDrawable();
      int i4 = this.mTrackPadding;
      int i5 = getWidth() - k - m;
      int i6 = this.mTrackPadding;
      int i7 = i5 - i6;
      int i8 = getHeight() - i1 - i3 - n;
      localDrawable.setBounds(i4, i3, i7, i8);
      setThumbOffset(0);
      return;
      this.mTrackPadding = 0;
      break;
    }
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    configureThumbPadding();
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    switch (paramMotionEvent.getAction())
    {
    default:
    case 0:
    case 1:
    }
    while (true)
    {
      return super.onTouchEvent(paramMotionEvent);
      this.mEnableAccessibility = true;
      continue;
      this.mEnableAccessibility = false;
    }
  }

  public void sendAccessibilityEvent(int paramInt)
  {
    if (!this.mEnableAccessibility)
      return;
    super.sendAccessibilityEvent(paramInt);
  }

  public void setEnableAccessibility(boolean paramBoolean)
  {
    this.mEnableAccessibility = paramBoolean;
  }

  public void setThumb(Drawable paramDrawable)
  {
    this.mThumb = paramDrawable;
    configureThumbPadding();
    super.setThumb(paramDrawable);
  }

  public void setThumbAlpha(int paramInt)
  {
    float f = paramInt / 255.0F;
    this.mThumbAlpha = f;
    if (this.mThumb != null)
      this.mThumb.setAlpha(paramInt);
    configureThumbPadding();
  }

  public void setTrackHeight(int paramInt)
  {
    this.mTrackHeight = paramInt;
    requestLayout();
  }

  public void setTrackHeightDip(float paramFloat)
  {
    int i = (int)(getContext().getResources().getDisplayMetrics().density * paramFloat);
    this.mTrackHeight = i;
    requestLayout();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.SizableTrackSeekBar
 * JD-Core Version:    0.6.2
 */