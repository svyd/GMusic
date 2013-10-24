package com.google.android.music;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class ClingView extends View
{
  private View mCommonParent;
  private final Drawable mHighlightImage;
  private View mViewToCling = null;

  public ClingView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Drawable localDrawable = paramContext.getResources().getDrawable(2130837674);
    this.mHighlightImage = localDrawable;
  }

  public void draw(Canvas paramCanvas)
  {
    super.draw(paramCanvas);
    if (this.mViewToCling == null)
      return;
    Paint localPaint = new Paint();
    Paint.Style localStyle = Paint.Style.FILL;
    localPaint.setStyle(localStyle);
    localPaint.setColor(-671088640);
    int[] arrayOfInt1 = getClingingViewLocationRelativeToParent();
    int[] arrayOfInt2 = new int[2];
    int i = arrayOfInt1[0];
    int j = this.mViewToCling.getWidth() / 2;
    int k = i + j;
    arrayOfInt2[0] = k;
    int m = arrayOfInt1[1];
    int n = this.mViewToCling.getHeight() / 2;
    int i1 = m + n;
    arrayOfInt2[1] = i1;
    int i2 = arrayOfInt2[0];
    int i3 = this.mHighlightImage.getIntrinsicWidth() / 2;
    int i4 = i2 - i3;
    int i5 = arrayOfInt2[1];
    int i6 = this.mHighlightImage.getIntrinsicHeight() / 2;
    int i7 = i5 - i6;
    Drawable localDrawable = this.mHighlightImage;
    int i8 = this.mHighlightImage.getIntrinsicWidth() + i4;
    int i9 = this.mHighlightImage.getIntrinsicHeight() + i7;
    localDrawable.setBounds(i4, i7, i8, i9);
    this.mHighlightImage.draw(paramCanvas);
    Rect localRect = this.mHighlightImage.getBounds();
    float f1 = paramCanvas.getWidth();
    float f2 = localRect.top;
    Canvas localCanvas = paramCanvas;
    float f3 = 0.0F;
    localCanvas.drawRect(0.0F, f3, f1, f2, localPaint);
    float f4 = localRect.top;
    float f5 = localRect.left;
    float f6 = paramCanvas.getHeight();
    paramCanvas.drawRect(0.0F, f4, f5, f6, localPaint);
    int i10 = localRect.right;
    int i11 = paramCanvas.getWidth();
    if (i10 < i11)
    {
      float f7 = localRect.right;
      float f8 = localRect.top;
      float f9 = paramCanvas.getWidth();
      float f10 = paramCanvas.getHeight();
      paramCanvas.drawRect(f7, f8, f9, f10, localPaint);
    }
    int i12 = localRect.bottom;
    int i13 = paramCanvas.getHeight();
    if (i12 >= i13)
      return;
    float f11 = localRect.left;
    float f12 = localRect.bottom;
    float f13 = localRect.right;
    float f14 = paramCanvas.getHeight();
    paramCanvas.drawRect(f11, f12, f13, f14, localPaint);
  }

  public int[] getClingingViewLocationRelativeToParent()
  {
    int[] arrayOfInt;
    if (this.mViewToCling == null)
      arrayOfInt = null;
    while (true)
    {
      return arrayOfInt;
      int i = this.mViewToCling.getLeft();
      int j = this.mViewToCling.getTop();
      for (View localView1 = (View)this.mViewToCling.getParent(); ; localView1 = (View)localView1.getParent())
      {
        View localView2 = this.mCommonParent;
        if ((localView1 == localView2) || (localView1 == null))
          break;
        int k = localView1.getLeft();
        i += k;
        int m = localView1.getTop();
        j += m;
      }
      if (localView1 == null)
        throw new IllegalStateException("cling item does not share common parent");
      arrayOfInt = new int[2];
      arrayOfInt[0] = i;
      arrayOfInt[1] = j;
    }
  }

  public void setViewToCling(View paramView1, View paramView2)
  {
    this.mViewToCling = paramView2;
    this.mCommonParent = paramView1;
    postInvalidate();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ClingView
 * JD-Core Version:    0.6.2
 */