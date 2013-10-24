package com.google.android.music.ui.cardlib.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;

public class PlayCardViewMediumPlus extends PlayCardView
{
  public PlayCardViewMediumPlus(Context paramContext)
  {
    this(paramContext, null);
  }

  public PlayCardViewMediumPlus(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = getPaddingLeft();
    int k = getPaddingRight();
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)this.mThumbnail.getLayoutParams();
    int m = i - j - k;
    int n = localMarginLayoutParams.leftMargin;
    int i1 = m - n;
    int i2 = localMarginLayoutParams.rightMargin;
    int i3 = (i1 - i2) / 2;
    float f1 = i3;
    float f2 = this.mThumbnailAspectRatio;
    int i4 = (int)(f1 * f2);
    this.mThumbnail.getLayoutParams().width = i3;
    this.mThumbnail.getLayoutParams().height = i4;
    this.mThumbnail.setThumbnailMetrics(i3, i4);
    super.onMeasure(paramInt1, paramInt2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.layout.PlayCardViewMediumPlus
 * JD-Core Version:    0.6.2
 */