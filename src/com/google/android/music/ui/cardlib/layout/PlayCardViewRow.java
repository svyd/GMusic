package com.google.android.music.ui.cardlib.layout;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;

public class PlayCardViewRow extends PlayCardView
{
  private View mRowBackground;

  public PlayCardViewRow(Context paramContext)
  {
    this(paramContext, null);
  }

  public PlayCardViewRow(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    View localView = findViewById(2131296485);
    this.mRowBackground = localView;
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)this.mThumbnail.getLayoutParams();
    int i = getResources().getDimensionPixelSize(2131558400);
    int j = localMarginLayoutParams.topMargin;
    int k = i - j;
    int m = localMarginLayoutParams.bottomMargin;
    int n = k - m;
    float f1 = this.mThumbnailAspectRatio;
    float f2 = n;
    int i1 = (int)(f1 * f2);
    this.mThumbnail.getLayoutParams().height = n;
    this.mThumbnail.setThumbnailMetrics(i1, n);
    super.onMeasure(paramInt1, paramInt2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.layout.PlayCardViewRow
 * JD-Core Version:    0.6.2
 */