package com.google.android.music.ui.cardlib.layout;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.TextView;
import com.google.android.music.KeepOnViewSmall;

public class PlayCardViewSmall extends PlayCardView
{
  private final int mExtraVSpace;

  public PlayCardViewSmall(Context paramContext)
  {
    this(paramContext, null);
  }

  public PlayCardViewSmall(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    int i = paramContext.getResources().getDimensionPixelSize(2131558477);
    this.mExtraVSpace = i;
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getPaddingLeft();
    int j = getPaddingRight();
    int k = getPaddingTop();
    int m = getPaddingBottom();
    int n = getWidth();
    int i1 = getHeight();
    int i2 = paramInt4 - paramInt2 - i1;
    ViewGroup.MarginLayoutParams localMarginLayoutParams1 = (ViewGroup.MarginLayoutParams)this.mThumbnail.getLayoutParams();
    ViewGroup.MarginLayoutParams localMarginLayoutParams2 = (ViewGroup.MarginLayoutParams)this.mTitle.getLayoutParams();
    ViewGroup.MarginLayoutParams localMarginLayoutParams3 = (ViewGroup.MarginLayoutParams)this.mSubtitle.getLayoutParams();
    ViewGroup.MarginLayoutParams localMarginLayoutParams4 = (ViewGroup.MarginLayoutParams)this.mReason1.getLayoutParams();
    ViewGroup.MarginLayoutParams localMarginLayoutParams5 = (ViewGroup.MarginLayoutParams)this.mOverflow.getLayoutParams();
    ViewGroup.MarginLayoutParams localMarginLayoutParams6 = (ViewGroup.MarginLayoutParams)this.mKeepOn.getLayoutParams();
    int i3 = this.mThumbnail.getMeasuredHeight();
    int i4 = this.mThumbnail.getMeasuredWidth();
    int i5 = localMarginLayoutParams1.leftMargin;
    int i6 = i + i5;
    int i7 = localMarginLayoutParams1.topMargin;
    int i8 = k + i7;
    PlayCardThumbnail localPlayCardThumbnail1 = this.mThumbnail;
    int i9 = i6 + i4;
    int i10 = i8 + i3;
    PlayCardThumbnail localPlayCardThumbnail2 = localPlayCardThumbnail1;
    int i11 = i6;
    int i12 = i8;
    int i13 = i9;
    int i14 = i10;
    localPlayCardThumbnail2.layout(i11, i12, i13, i14);
    int i15 = i3;
    int i16 = k + i15;
    int i17 = localMarginLayoutParams2.topMargin;
    int i18 = i16 + i17;
    int i19 = localMarginLayoutParams2.leftMargin;
    int i20 = i + i19;
    int i21 = this.mTitle.getMeasuredHeight();
    int i22 = this.mTitle.getMeasuredWidth();
    TextView localTextView1 = this.mTitle;
    int i23 = i20 + i22;
    int i24 = i18 + i21;
    TextView localTextView2 = localTextView1;
    int i25 = i20;
    int i26 = i18;
    int i27 = i23;
    int i28 = i24;
    localTextView2.layout(i25, i26, i27, i28);
    int i29 = this.mOverflow.getVisibility();
    int i30 = 8;
    if (i29 != i30)
    {
      int i31 = k + i15;
      int i32 = localMarginLayoutParams5.topMargin;
      int i33 = i31 + i32;
      int i34 = n - j;
      int i35 = localMarginLayoutParams5.rightMargin;
      int i36 = i34 - i35;
      PlayCardOverflowView localPlayCardOverflowView1 = this.mOverflow;
      int i37 = this.mOverflow.getMeasuredWidth();
      int i38 = i36 - i37;
      int i39 = this.mOverflow.getMeasuredHeight() + i33;
      PlayCardOverflowView localPlayCardOverflowView2 = localPlayCardOverflowView1;
      int i40 = i38;
      int i41 = i33;
      int i42 = i39;
      localPlayCardOverflowView2.layout(i40, i41, i36, i42);
    }
    int i43 = this.mSubtitle.getVisibility();
    int i44 = 8;
    if (i43 != i44)
    {
      int i45 = i18 + i21;
      int i46 = localMarginLayoutParams2.bottomMargin;
      int i47 = i45 + i46;
      int i48 = localMarginLayoutParams3.topMargin;
      int i49 = i47 + i48;
      int i50 = localMarginLayoutParams3.leftMargin;
      int i51 = i + i50;
      TextView localTextView3 = this.mSubtitle;
      int i52 = this.mSubtitle.getMeasuredWidth() + i51;
      int i53 = this.mSubtitle.getMeasuredHeight() + i49;
      TextView localTextView4 = localTextView3;
      int i54 = i51;
      int i55 = i49;
      int i56 = i52;
      int i57 = i53;
      localTextView4.layout(i54, i55, i56, i57);
      int i58 = this.mReason1.getVisibility();
      int i59 = 8;
      if (i58 == i59)
      {
        int i60 = localMarginLayoutParams6.topMargin;
        int i61 = i49 + i60;
        int i62 = n - j;
        int i63 = localMarginLayoutParams6.rightMargin;
        int i64 = i62 - i63;
        KeepOnViewSmall localKeepOnViewSmall1 = this.mKeepOn;
        int i65 = this.mKeepOn.getMeasuredWidth();
        int i66 = i64 - i65;
        int i67 = this.mKeepOn.getMeasuredHeight() + i61;
        KeepOnViewSmall localKeepOnViewSmall2 = localKeepOnViewSmall1;
        int i68 = i66;
        int i69 = i67;
        localKeepOnViewSmall2.layout(i68, i61, i64, i69);
      }
    }
    int i70 = this.mReason1.getVisibility();
    int i71 = 8;
    if (i70 != i71)
    {
      int i72 = i1 - m;
      int i73 = localMarginLayoutParams4.bottomMargin;
      int i74 = i72 - i73;
      int i75 = i2 / 2;
      int i76 = i74 - i75;
      int i77 = localMarginLayoutParams4.leftMargin;
      int i78 = i + i77;
      PlayCardReason localPlayCardReason1 = this.mReason1;
      int i79 = this.mReason1.getMeasuredHeight();
      int i80 = i76 - i79;
      int i81 = this.mReason1.getMeasuredWidth() + i78;
      PlayCardReason localPlayCardReason2 = localPlayCardReason1;
      int i82 = i78;
      int i83 = i80;
      int i84 = i81;
      int i85 = i76;
      localPlayCardReason2.layout(i82, i83, i84, i85);
      int i86 = i1 - m;
      int i87 = localMarginLayoutParams6.bottomMargin;
      int i88 = i86 - i87;
      int i89 = i2 / 2;
      int i90 = i88 - i89;
      int i91 = n - j;
      int i92 = localMarginLayoutParams6.rightMargin;
      int i93 = i91 - i92;
      KeepOnViewSmall localKeepOnViewSmall3 = this.mKeepOn;
      int i94 = this.mKeepOn.getMeasuredWidth();
      int i95 = i93 - i94;
      int i96 = this.mKeepOn.getMeasuredHeight();
      int i97 = i90 - i96;
      KeepOnViewSmall localKeepOnViewSmall4 = localKeepOnViewSmall3;
      int i98 = i95;
      int i99 = i97;
      localKeepOnViewSmall4.layout(i98, i99, i93, i90);
    }
    ViewGroup.MarginLayoutParams localMarginLayoutParams7 = (ViewGroup.MarginLayoutParams)this.mAccessibilityOverlay.getLayoutParams();
    View localView = this.mAccessibilityOverlay;
    int i100 = localMarginLayoutParams7.leftMargin + i;
    int i101 = localMarginLayoutParams7.topMargin + k;
    int i102 = localMarginLayoutParams7.leftMargin + i;
    int i103 = this.mAccessibilityOverlay.getMeasuredWidth();
    int i104 = i102 + i103;
    int i105 = localMarginLayoutParams7.topMargin + k;
    int i106 = this.mAccessibilityOverlay.getMeasuredHeight();
    int i107 = i105 + i106;
    localView.layout(i100, i101, i104, i107);
    PlayCardOverflowView localPlayCardOverflowView3 = this.mOverflow;
    Rect localRect1 = this.mOverflowArea;
    localPlayCardOverflowView3.getHitRect(localRect1);
    Rect localRect2 = this.mOverflowArea;
    int i108 = localRect2.top;
    int i109 = this.mOverflowTouchExtend;
    int i110 = i108 - i109;
    localRect2.top = i110;
    Rect localRect3 = this.mOverflowArea;
    int i111 = localRect3.bottom;
    int i112 = this.mOverflowTouchExtend;
    int i113 = i111 + i112;
    localRect3.bottom = i113;
    Rect localRect4 = this.mOverflowArea;
    int i114 = localRect4.left;
    int i115 = this.mOverflowTouchExtend;
    int i116 = i114 - i115;
    localRect4.left = i116;
    Rect localRect5 = this.mOverflowArea;
    int i117 = localRect5.right;
    int i118 = this.mOverflowTouchExtend;
    int i119 = i117 + i118;
    localRect5.right = i119;
    int i120 = this.mOverflowArea.top;
    int i121 = this.mOldOverflowArea.top;
    int i122 = i120;
    int i123 = i121;
    if (i122 != i123)
    {
      int i124 = this.mOverflowArea.bottom;
      int i125 = this.mOldOverflowArea.bottom;
      int i126 = i124;
      int i127 = i125;
      if (i126 != i127)
      {
        int i128 = this.mOverflowArea.left;
        int i129 = this.mOldOverflowArea.left;
        int i130 = i128;
        int i131 = i129;
        if (i130 != i131)
        {
          int i132 = this.mOverflowArea.right;
          int i133 = this.mOldOverflowArea.right;
          int i134 = i132;
          int i135 = i133;
          if (i134 != i135)
            return;
        }
      }
    }
    Rect localRect6 = this.mOverflowArea;
    PlayCardOverflowView localPlayCardOverflowView4 = this.mOverflow;
    TouchDelegate localTouchDelegate1 = new TouchDelegate(localRect6, localPlayCardOverflowView4);
    PlayCardViewSmall localPlayCardViewSmall = this;
    TouchDelegate localTouchDelegate2 = localTouchDelegate1;
    localPlayCardViewSmall.setTouchDelegate(localTouchDelegate2);
    Rect localRect7 = this.mOldOverflowArea;
    Rect localRect8 = this.mOverflowArea;
    localRect7.set(localRect8);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = getPaddingLeft();
    int j = getPaddingRight();
    int k = getPaddingTop();
    int m = getPaddingBottom();
    int n = View.MeasureSpec.getSize(paramInt1);
    int i1 = m + k;
    int i2 = n - i - j;
    ViewGroup.MarginLayoutParams localMarginLayoutParams1 = (ViewGroup.MarginLayoutParams)this.mThumbnail.getLayoutParams();
    ViewGroup.MarginLayoutParams localMarginLayoutParams2 = (ViewGroup.MarginLayoutParams)this.mTitle.getLayoutParams();
    ViewGroup.MarginLayoutParams localMarginLayoutParams3 = (ViewGroup.MarginLayoutParams)this.mSubtitle.getLayoutParams();
    ViewGroup.MarginLayoutParams localMarginLayoutParams4 = (ViewGroup.MarginLayoutParams)this.mReason1.getLayoutParams();
    ViewGroup.MarginLayoutParams localMarginLayoutParams5 = (ViewGroup.MarginLayoutParams)this.mOverflow.getLayoutParams();
    ViewGroup.MarginLayoutParams localMarginLayoutParams6 = (ViewGroup.MarginLayoutParams)this.mKeepOn.getLayoutParams();
    int i3 = localMarginLayoutParams1.leftMargin;
    int i4 = i2 - i3;
    int i5 = localMarginLayoutParams1.rightMargin;
    int i6 = i4 - i5;
    float f1 = this.mThumbnailAspectRatio;
    float f2 = i6;
    int i7 = (int)(f1 * f2);
    int i8 = i7;
    localMarginLayoutParams1.height = i8;
    PlayCardThumbnail localPlayCardThumbnail1 = this.mThumbnail;
    int i9 = i6;
    int i10 = i7;
    localPlayCardThumbnail1.setThumbnailMetrics(i9, i10);
    PlayCardThumbnail localPlayCardThumbnail2 = this.mThumbnail;
    int i11 = i6;
    int i12 = 1073741824;
    int i13 = View.MeasureSpec.makeMeasureSpec(i11, i12);
    int i14 = i7;
    int i15 = 1073741824;
    int i16 = View.MeasureSpec.makeMeasureSpec(i14, i15);
    localPlayCardThumbnail2.measure(i13, i16);
    int i17 = this.mThumbnail.getMeasuredHeight();
    int i18 = localMarginLayoutParams1.bottomMargin;
    int i19 = i17 + i18;
    int i20 = localMarginLayoutParams1.topMargin;
    int i21 = i19 + i20;
    int i22 = i1 + i21;
    int i23 = localMarginLayoutParams2.leftMargin;
    int i24 = i2 - i23;
    int i25 = localMarginLayoutParams2.rightMargin;
    int i26 = i24 - i25;
    TextView localTextView1 = this.mTitle;
    int i27 = i26;
    int i28 = 1073741824;
    int i29 = View.MeasureSpec.makeMeasureSpec(i27, i28);
    localTextView1.measure(i29, 0);
    int i30 = this.mTitle.getMeasuredHeight();
    int i31 = localMarginLayoutParams2.topMargin;
    int i32 = i30 + i31;
    int i33 = localMarginLayoutParams2.bottomMargin;
    int i34 = i32 + i33;
    this.mOverflow.measure(0, 0);
    int i35 = this.mOverflow.getMeasuredHeight();
    int i36 = localMarginLayoutParams5.topMargin;
    int i37 = i35 + i36;
    int i38 = localMarginLayoutParams5.bottomMargin;
    int i39 = i37 + i38;
    int i40 = Math.max(i34, i39);
    int i41 = i22 + i40;
    this.mKeepOn.measure(0, 0);
    int i42 = this.mSubtitle.getVisibility();
    int i43 = 8;
    if (i42 != i43)
    {
      TextView localTextView2 = this.mSubtitle;
      int i44 = i26;
      int i45 = 1073741824;
      int i46 = View.MeasureSpec.makeMeasureSpec(i44, i45);
      localTextView2.measure(i46, 0);
      int i47 = this.mSubtitle.getMeasuredHeight();
      int i48 = localMarginLayoutParams3.bottomMargin;
      int i49 = i47 + i48;
      int i50 = localMarginLayoutParams3.topMargin;
      int i51 = i49 + i50;
      int i52 = this.mKeepOn.getMeasuredHeight();
      int i53 = localMarginLayoutParams6.topMargin;
      int i54 = i52 + i53;
      int i55 = Math.max(i51, i54);
      i41 += i55;
    }
    int i56 = this.mReason1.getVisibility();
    int i57 = 8;
    int i61;
    if (i56 != i57)
    {
      int i58 = localMarginLayoutParams4.leftMargin;
      int i59 = i2 - i58;
      int i60 = localMarginLayoutParams4.rightMargin;
      i61 = i59 - i60;
      if (localMarginLayoutParams4.height <= 0)
        break label905;
      PlayCardReason localPlayCardReason1 = this.mReason1;
      int i62 = i61;
      int i63 = 1073741824;
      int i64 = View.MeasureSpec.makeMeasureSpec(i62, i63);
      int i65 = View.MeasureSpec.makeMeasureSpec(localMarginLayoutParams4.height, 1073741824);
      localPlayCardReason1.measure(i64, i65);
    }
    while (true)
    {
      int i66 = this.mReason1.getMeasuredHeight();
      int i67 = localMarginLayoutParams4.topMargin;
      int i68 = i66 + i67;
      int i69 = localMarginLayoutParams4.bottomMargin;
      int i70 = i68 + i69;
      int i71 = this.mKeepOn.getMeasuredHeight();
      int i72 = localMarginLayoutParams6.bottomMargin;
      int i73 = i71 + i72;
      int i74 = i70;
      int i75 = Math.max(i73, i74);
      i41 += i75;
      PlayCardViewSmall localPlayCardViewSmall = this;
      int i76 = n;
      int i77 = i41;
      localPlayCardViewSmall.setMeasuredDimension(i76, i77);
      int i78 = i41 - k - m;
      ViewGroup.MarginLayoutParams localMarginLayoutParams7 = (ViewGroup.MarginLayoutParams)this.mAccessibilityOverlay.getLayoutParams();
      int i79 = localMarginLayoutParams7.leftMargin;
      int i80 = i2 - i79;
      int i81 = localMarginLayoutParams7.rightMargin;
      int i82 = i80 - i81;
      int i83 = localMarginLayoutParams7.topMargin;
      int i84 = i78 - i83;
      int i85 = localMarginLayoutParams7.bottomMargin;
      int i86 = i84 - i85;
      View localView = this.mAccessibilityOverlay;
      int i87 = 1073741824;
      int i88 = View.MeasureSpec.makeMeasureSpec(i82, i87);
      int i89 = 1073741824;
      int i90 = View.MeasureSpec.makeMeasureSpec(i86, i89);
      localView.measure(i88, i90);
      return;
      label905: PlayCardReason localPlayCardReason2 = this.mReason1;
      int i91 = i61;
      int i92 = 1073741824;
      int i93 = View.MeasureSpec.makeMeasureSpec(i91, i92);
      localPlayCardReason2.measure(i93, 0);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.layout.PlayCardViewSmall
 * JD-Core Version:    0.6.2
 */