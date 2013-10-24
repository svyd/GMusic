package com.google.android.music.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class PlayTabStrip extends LinearLayout
{
  private final Paint mFullUnderlinePaint;
  private final int mFullUnderlineThickness;
  private int mIndexForSelection;
  private final Paint mSelectedUnderlinePaint;
  private final int mSelectedUnderlineThickness;
  private float mSelectionOffset;
  private final int mSideSeparatorHeight;
  private final Paint mSideSeparatorPaint;

  public PlayTabStrip(Context paramContext)
  {
    this(paramContext, null);
  }

  public PlayTabStrip(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setWillNotDraw(false);
    Resources localResources = paramContext.getResources();
    int i = localResources.getDimensionPixelSize(2131558487);
    this.mFullUnderlineThickness = i;
    Paint localPaint1 = new Paint();
    this.mFullUnderlinePaint = localPaint1;
    Paint localPaint2 = this.mFullUnderlinePaint;
    int j = localResources.getColor(2131427382);
    localPaint2.setColor(j);
    int k = localResources.getDimensionPixelSize(2131558486);
    this.mSelectedUnderlineThickness = k;
    Paint localPaint3 = new Paint();
    this.mSelectedUnderlinePaint = localPaint3;
    Paint localPaint4 = new Paint();
    this.mSideSeparatorPaint = localPaint4;
    Paint localPaint5 = this.mSideSeparatorPaint;
    int m = localResources.getColor(2131427383);
    localPaint5.setColor(m);
    Paint localPaint6 = this.mSideSeparatorPaint;
    float f = localResources.getDimensionPixelSize(2131558491);
    localPaint6.setStrokeWidth(f);
    int n = localResources.getDimensionPixelSize(2131558490);
    this.mSideSeparatorHeight = n;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    int i = getHeight();
    int j = getChildCount();
    if (j > 0)
    {
      int k = this.mIndexForSelection;
      View localView1 = getChildAt(k);
      int m = localView1.getLeft();
      int n = localView1.getRight();
      if (this.mSelectionOffset > 0.0F)
      {
        int i1 = this.mIndexForSelection;
        int i2 = getChildCount() + -1;
        if (i1 < i2)
        {
          int i3 = this.mIndexForSelection + 1;
          View localView2 = getChildAt(i3);
          int i4 = localView2.getLeft();
          int i5 = localView2.getRight();
          float f1 = this.mSelectionOffset;
          float f2 = i4;
          float f3 = f1 * f2;
          float f4 = this.mSelectionOffset;
          float f5 = 1.0F - f4;
          float f6 = m;
          float f7 = f5 * f6;
          m = (int)(f3 + f7);
          float f8 = this.mSelectionOffset;
          float f9 = i5;
          float f10 = f8 * f9;
          float f11 = this.mSelectionOffset;
          float f12 = 1.0F - f11;
          float f13 = n;
          float f14 = f12 * f13;
          n = (int)(f10 + f14);
        }
      }
      float f15 = m;
      int i6 = this.mSelectedUnderlineThickness;
      float f16 = i - i6;
      float f17 = n;
      float f18 = i;
      Paint localPaint1 = this.mSelectedUnderlinePaint;
      paramCanvas.drawRect(f15, f16, f17, f18, localPaint1);
    }
    int i7 = this.mFullUnderlineThickness;
    float f19 = i - i7;
    float f20 = getWidth();
    float f21 = i;
    Paint localPaint2 = this.mFullUnderlinePaint;
    paramCanvas.drawRect(0.0F, f19, f20, f21, localPaint2);
    int i8 = 1;
    while (true)
    {
      if (i8 >= j)
        return;
      View localView3 = getChildAt(i8);
      int i9 = localView3.getPaddingTop();
      int i10 = localView3.getPaddingBottom();
      int i11 = (localView3.getHeight() - i9 - i10) / 2 + i9;
      int i12 = this.mSideSeparatorHeight / 2;
      int i13 = i11 - i12;
      float f22 = localView3.getLeft();
      float f23 = i13;
      float f24 = localView3.getLeft();
      float f25 = this.mSideSeparatorHeight + i13;
      Paint localPaint3 = this.mSideSeparatorPaint;
      paramCanvas.drawLine(f22, f23, f24, f25, localPaint3);
      i8 += 1;
    }
  }

  void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
    this.mIndexForSelection = paramInt1;
    this.mSelectionOffset = paramFloat;
    invalidate();
  }

  void onPageSelected(int paramInt)
  {
    this.mIndexForSelection = paramInt;
    this.mSelectionOffset = 0.0F;
    invalidate();
  }

  public void setSelectedIndicatorColor(int paramInt)
  {
    this.mSelectedUnderlinePaint.setColor(paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.PlayTabStrip
 * JD-Core Version:    0.6.2
 */