package com.google.android.music.ui.cardlib.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import com.google.android.music.utils.MusicUtils;

public class PlayCardViewMedium extends PlayCardView
{
  public PlayCardViewMedium(Context paramContext)
  {
    this(paramContext, null);
  }

  public PlayCardViewMedium(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if (!MusicUtils.isLandscape(getContext()))
      return;
    int i = getPaddingLeft();
    int j = getPaddingTop();
    int k = this.mThumbnail.getMeasuredHeight();
    int m = this.mThumbnail.getMeasuredWidth();
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)this.mThumbnail.getLayoutParams();
    int n = (m - k) / 2;
    int i1 = localMarginLayoutParams.topMargin;
    int i2 = j + i1;
    PlayCardThumbnail localPlayCardThumbnail = this.mThumbnail;
    int i3 = i + m;
    int i4 = i2 + k;
    localPlayCardThumbnail.layout(n, i2, i3, i4);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = getPaddingLeft();
    int k = getPaddingRight();
    ViewGroup.MarginLayoutParams localMarginLayoutParams1 = (ViewGroup.MarginLayoutParams)this.mThumbnail.getLayoutParams();
    int m = i / 2 - j - k;
    int n = localMarginLayoutParams1.leftMargin;
    int i1 = m - n;
    int i2 = localMarginLayoutParams1.rightMargin;
    int i3 = i1 - i2;
    float f1 = this.mThumbnailAspectRatio;
    float f2 = i3;
    int i4 = (int)(f1 * f2);
    this.mThumbnail.getLayoutParams().width = i3;
    this.mThumbnail.getLayoutParams().height = i4;
    this.mThumbnail.setThumbnailMetrics(i3, i4);
    PlayCardThumbnail localPlayCardThumbnail = this.mThumbnail;
    int i5 = View.MeasureSpec.makeMeasureSpec(i3, 1073741824);
    int i6 = View.MeasureSpec.makeMeasureSpec(i4, 1073741824);
    localPlayCardThumbnail.measure(i5, i6);
    ViewGroup.MarginLayoutParams localMarginLayoutParams2 = (ViewGroup.MarginLayoutParams)this.mTitle.getLayoutParams();
    int i7 = i - j - k;
    int i8 = this.mThumbnail.getMeasuredWidth();
    int i9 = i7 - i8;
    int i10 = localMarginLayoutParams1.rightMargin;
    int i11 = i9 - i10;
    int i12 = localMarginLayoutParams1.leftMargin;
    int i13 = i11 - i12;
    int i14 = localMarginLayoutParams2.rightMargin;
    int i15 = i13 - i14;
    int i16 = localMarginLayoutParams2.leftMargin;
    int i17 = i15 - i16;
    this.mTitle.measure(0, 0);
    int i18 = this.mTitle.getMeasuredWidth();
    PlayCardReason localPlayCardReason1 = this.mReason1;
    int i19;
    int i30;
    if (i18 > i17)
    {
      i19 = 2;
      localPlayCardReason1.setReasonMaxLines(i19);
      if (this.mReason1.getVisibility() != 8)
      {
        ViewGroup.MarginLayoutParams localMarginLayoutParams3 = (ViewGroup.MarginLayoutParams)this.mReason1.getLayoutParams();
        int i20 = i - j - k;
        int i21 = this.mThumbnail.getMeasuredWidth();
        int i22 = i20 - i21;
        int i23 = localMarginLayoutParams1.rightMargin;
        int i24 = i22 - i23;
        int i25 = localMarginLayoutParams1.leftMargin;
        int i26 = i24 - i25;
        int i27 = -localMarginLayoutParams3.leftMargin;
        int i28 = i26 - i27;
        int i29 = localMarginLayoutParams3.rightMargin;
        i30 = i28 - i29;
        if (localMarginLayoutParams3.height <= 0)
          break label470;
        PlayCardReason localPlayCardReason2 = this.mReason1;
        int i31 = View.MeasureSpec.makeMeasureSpec(i30, 1073741824);
        int i32 = View.MeasureSpec.makeMeasureSpec(localMarginLayoutParams3.height, 1073741824);
        localPlayCardReason2.measure(i31, i32);
      }
    }
    while (true)
    {
      int i33 = View.MeasureSpec.makeMeasureSpec(i4, 1073741824);
      PlayCardViewMedium localPlayCardViewMedium = this;
      int i34 = paramInt1;
      localPlayCardViewMedium.onMeasure(i34, i33);
      return;
      i19 = 3;
      break;
      label470: PlayCardReason localPlayCardReason3 = this.mReason1;
      int i35 = View.MeasureSpec.makeMeasureSpec(i30, 1073741824);
      localPlayCardReason3.measure(i35, 0);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.layout.PlayCardViewMedium
 * JD-Core Version:    0.6.2
 */