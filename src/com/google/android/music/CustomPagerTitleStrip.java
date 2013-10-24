package com.google.android.music;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.music.widgets.MultipleListenerViewPager;

public class CustomPagerTitleStrip extends ViewGroup
{
  private static final int[] ATTRS = { 16842804, 16842904, 16842901 };
  private AdapterObservable.AdapterObserver mAdapterObserver;
  private TextView mCurrText;
  private int mLastKnownCurrentPage = -1;
  private float mLastKnownPositionOffset = -1.0F;
  private TextView mNextText;
  private final PageListener mPageListener;
  private ViewPager mPager = null;
  private TextView mPrevText;
  private int mScaledTextSpacing;
  private boolean mUpdatingPositions;
  private boolean mUpdatingText;

  public CustomPagerTitleStrip(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    PageListener localPageListener = new PageListener(null);
    this.mPageListener = localPageListener;
    AdapterObservable.AdapterObserver local1 = new AdapterObservable.AdapterObserver()
    {
    };
    this.mAdapterObserver = local1;
    TextView localTextView1 = new TextView(paramContext);
    this.mPrevText = localTextView1;
    addView(localTextView1);
    TextView localTextView2 = new TextView(paramContext);
    this.mCurrText = localTextView2;
    addView(localTextView2);
    TextView localTextView3 = new TextView(paramContext);
    this.mNextText = localTextView3;
    addView(localTextView3);
    int[] arrayOfInt = ATTRS;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt);
    int i = localTypedArray.getResourceId(0, 0);
    if (i != 0)
    {
      this.mPrevText.setTextAppearance(paramContext, i);
      this.mCurrText.setTextAppearance(paramContext, i);
      this.mNextText.setTextAppearance(paramContext, i);
    }
    if (localTypedArray.hasValue(1))
    {
      int j = localTypedArray.getColor(1, 0);
      this.mPrevText.setTextColor(j);
      this.mCurrText.setTextColor(j);
      this.mNextText.setTextColor(j);
    }
    int k = localTypedArray.getDimensionPixelSize(2, 0);
    if (k != 0)
    {
      TextView localTextView4 = this.mPrevText;
      float f1 = k;
      localTextView4.setTextSize(0, f1);
      TextView localTextView5 = this.mCurrText;
      float f2 = k;
      localTextView5.setTextSize(0, f2);
      TextView localTextView6 = this.mNextText;
      float f3 = k;
      localTextView6.setTextSize(0, f3);
    }
    localTypedArray.recycle();
    this.mPrevText.setGravity(16);
    this.mCurrText.setGravity(16);
    this.mNextText.setGravity(16);
    int m = this.mPrevText.getTextColors().getDefaultColor();
    int n = 0xFFFFFF & m;
    int i1 = 0x99000000 | n;
    this.mPrevText.setTextColor(i1);
    this.mNextText.setTextColor(i1);
    TextView localTextView7 = this.mPrevText;
    TextUtils.TruncateAt localTruncateAt1 = TextUtils.TruncateAt.MARQUEE;
    localTextView7.setEllipsize(localTruncateAt1);
    TextView localTextView8 = this.mCurrText;
    TextUtils.TruncateAt localTruncateAt2 = TextUtils.TruncateAt.MARQUEE;
    localTextView8.setEllipsize(localTruncateAt2);
    TextView localTextView9 = this.mNextText;
    TextUtils.TruncateAt localTruncateAt3 = TextUtils.TruncateAt.MARQUEE;
    localTextView9.setEllipsize(localTruncateAt3);
    this.mPrevText.setSingleLine();
    this.mCurrText.setSingleLine();
    this.mNextText.setSingleLine();
    float f4 = paramContext.getResources().getDisplayMetrics().density;
    int i2 = (int)(16.0F * f4);
    this.mScaledTextSpacing = i2;
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    ViewGroup localViewGroup = (ViewGroup)getParent();
    int i = 0;
    while (true)
    {
      int j = localViewGroup.getChildCount();
      if (i < j)
      {
        if ((localViewGroup.getChildAt(i) instanceof ViewPager))
        {
          ViewPager localViewPager = (ViewPager)localViewGroup.getChildAt(i);
          this.mPager = localViewPager;
        }
      }
      else
      {
        if (this.mPager != null)
          break;
        throw new IllegalStateException("Must be in parent which also contains a ViewPager");
      }
      i += 1;
    }
    PagerAdapter localPagerAdapter = this.mPager.getAdapter();
    if (!(this.mPager instanceof MultipleListenerViewPager))
      throw new IllegalStateException("Expecting to be in a MultipleListenerViewPager");
    MultipleListenerViewPager localMultipleListenerViewPager = (MultipleListenerViewPager)this.mPager;
    PageListener localPageListener = this.mPageListener;
    localMultipleListenerViewPager.addOnPageChangeListener(localPageListener);
    updateAdapter(null, localPagerAdapter);
  }

  protected void onDetachedFromWindow()
  {
    PagerAdapter localPagerAdapter = this.mPager.getAdapter();
    updateAdapter(localPagerAdapter, null);
    MultipleListenerViewPager localMultipleListenerViewPager = (MultipleListenerViewPager)this.mPager;
    PageListener localPageListener = this.mPageListener;
    localMultipleListenerViewPager.removeOnPageChangeListener(localPageListener);
    this.mPager = null;
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (this.mPager == null)
      return;
    if (this.mPageListener.mScrollState != 0)
      return;
    int i = this.mPager.getCurrentItem();
    updateTextPositions(i, 0.0F);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    int k = View.MeasureSpec.getSize(paramInt1);
    int m = View.MeasureSpec.getSize(paramInt2);
    if (i != 1073741824)
      throw new IllegalStateException("Must measure with an exact width");
    int n = m;
    int i1 = 0;
    Drawable localDrawable = getBackground();
    if (localDrawable != null)
      i1 = localDrawable.getIntrinsicHeight();
    int i2 = getPaddingTop();
    int i3 = getPaddingBottom();
    int i4 = i2 + i3;
    int i5 = n - i4;
    int i6 = View.MeasureSpec.makeMeasureSpec((int)(k * 0.8F), -2147483648);
    int i7 = View.MeasureSpec.makeMeasureSpec(i5, j);
    this.mPrevText.measure(i6, i7);
    this.mCurrText.measure(i6, i7);
    this.mNextText.measure(i6, i7);
    if (j == 1073741824)
    {
      setMeasuredDimension(k, m);
      return;
    }
    int i8 = this.mCurrText.getMeasuredHeight() + i4;
    int i9 = Math.max(i1, i8);
    setMeasuredDimension(k, i9);
  }

  public void requestLayout()
  {
    if (this.mUpdatingText)
      return;
    super.requestLayout();
  }

  void updateAdapter(PagerAdapter paramPagerAdapter1, PagerAdapter paramPagerAdapter2)
  {
    if (paramPagerAdapter1 != null)
    {
      if (!(paramPagerAdapter1 instanceof AdapterObservable))
        throw new IllegalStateException("Epecting adapter to be of type AdapterObservable");
      AdapterObservable localAdapterObservable1 = (AdapterObservable)paramPagerAdapter1;
      AdapterObservable.AdapterObserver localAdapterObserver1 = this.mAdapterObserver;
      localAdapterObservable1.unregisterAdapterObserver(localAdapterObserver1);
    }
    if (paramPagerAdapter2 != null)
    {
      if (!(paramPagerAdapter2 instanceof AdapterObservable))
        throw new IllegalStateException("Epecting adapter to be of type AdapterObservable");
      AdapterObservable localAdapterObservable2 = (AdapterObservable)paramPagerAdapter2;
      AdapterObservable.AdapterObserver localAdapterObserver2 = this.mAdapterObserver;
      localAdapterObservable2.registerAdapterObserver(localAdapterObserver2);
    }
    if (this.mPager == null)
      return;
    this.mLastKnownCurrentPage = -1;
    this.mLastKnownPositionOffset = -1.0F;
    int i = this.mPager.getCurrentItem();
    updateText(i, paramPagerAdapter2);
    requestLayout();
  }

  void updateText(int paramInt, PagerAdapter paramPagerAdapter)
  {
    int i;
    TextView localTextView;
    if (paramPagerAdapter != null)
    {
      i = paramPagerAdapter.getCount();
      this.mUpdatingText = true;
      CharSequence localCharSequence1 = null;
      if ((paramInt >= 1) && (paramPagerAdapter != null))
      {
        int j = paramInt + -1;
        localCharSequence1 = paramPagerAdapter.getPageTitle(j);
      }
      this.mPrevText.setText(localCharSequence1);
      localTextView = this.mCurrText;
      if (paramPagerAdapter == null)
        break label265;
    }
    label265: for (CharSequence localCharSequence2 = paramPagerAdapter.getPageTitle(paramInt); ; localCharSequence2 = null)
    {
      localTextView.setText(localCharSequence2);
      CharSequence localCharSequence3 = null;
      if ((paramInt + 1 < i) && (paramPagerAdapter != null))
      {
        int k = paramInt + 1;
        localCharSequence3 = paramPagerAdapter.getPageTitle(k);
      }
      this.mNextText.setText(localCharSequence3);
      int m = getWidth();
      int n = getPaddingLeft();
      int i1 = m - n;
      int i2 = getPaddingRight();
      int i3 = i1 - i2;
      int i4 = getHeight();
      int i5 = getPaddingTop();
      int i6 = i4 - i5;
      int i7 = getPaddingBottom();
      int i8 = i6 - i7;
      int i9 = View.MeasureSpec.makeMeasureSpec((int)(i3 * 0.8F), -2147483648);
      int i10 = View.MeasureSpec.makeMeasureSpec(i8, 1073741824);
      this.mPrevText.measure(i9, i10);
      this.mCurrText.measure(i9, i10);
      this.mNextText.measure(i9, i10);
      this.mLastKnownCurrentPage = paramInt;
      if (!this.mUpdatingPositions)
      {
        float f = this.mLastKnownPositionOffset;
        updateTextPositions(paramInt, f);
      }
      this.mUpdatingText = false;
      return;
      i = 0;
      break;
    }
  }

  void updateTextPositions(int paramInt, float paramFloat)
  {
    int i = this.mLastKnownCurrentPage;
    int j = paramInt;
    int k = i;
    if (j != k)
    {
      PagerAdapter localPagerAdapter1 = this.mPager.getAdapter();
      CustomPagerTitleStrip localCustomPagerTitleStrip = this;
      int m = paramInt;
      PagerAdapter localPagerAdapter2 = localPagerAdapter1;
      localCustomPagerTitleStrip.updateText(m, localPagerAdapter2);
    }
    float f3;
    do
    {
      boolean bool1 = true;
      this.mUpdatingPositions = bool1;
      int n = this.mPrevText.getMeasuredWidth();
      int i1 = this.mCurrText.getMeasuredWidth();
      int i2 = this.mNextText.getMeasuredWidth();
      int i3 = i1 / 2;
      int i4 = getWidth();
      int i5 = getPaddingLeft();
      int i6 = getPaddingRight();
      int i7 = getPaddingTop();
      int i8 = i5 + i3;
      int i9 = i6 + i3;
      int i10 = i4 - i8 - i9;
      float f1 = paramFloat + 0.5F;
      if (f1 > 1.0F)
        f1 -= 1.0F;
      int i11 = i4 - i9;
      int i12 = (int)(i10 * f1);
      int i13 = i11 - i12;
      int i14 = i1 / 2;
      int i15 = i13 - i14;
      int i16 = i15 + i1;
      TextView localTextView1 = this.mCurrText;
      int i17 = this.mCurrText.getMeasuredHeight() + i7;
      TextView localTextView2 = localTextView1;
      int i18 = i17;
      localTextView2.layout(i15, i7, i16, i18);
      int i19 = this.mScaledTextSpacing;
      int i20 = i15 - i19 - n;
      int i21 = Math.min(i5, i20);
      TextView localTextView3 = this.mPrevText;
      int i22 = i21 + n;
      int i23 = this.mPrevText.getMeasuredHeight() + i7;
      TextView localTextView4 = localTextView3;
      int i24 = i22;
      int i25 = i23;
      localTextView4.layout(i21, i7, i24, i25);
      int i26 = i4 - i6 - i2;
      int i27 = this.mScaledTextSpacing + i16;
      int i28 = Math.max(i26, i27);
      TextView localTextView5 = this.mNextText;
      int i29 = i28 + i2;
      int i30 = this.mNextText.getMeasuredHeight() + i7;
      TextView localTextView6 = localTextView5;
      int i31 = i29;
      int i32 = i30;
      localTextView6.layout(i28, i7, i31, i32);
      float f2 = paramFloat;
      this.mLastKnownPositionOffset = f2;
      boolean bool2 = false;
      this.mUpdatingPositions = bool2;
      return;
      f3 = this.mLastKnownPositionOffset;
    }
    while (paramFloat != f3);
  }

  private class PageListener
    implements ViewPager.OnPageChangeListener
  {
    private int mScrollState = 0;

    private PageListener()
    {
    }

    public void onPageScrollStateChanged(int paramInt)
    {
      this.mScrollState = paramInt;
    }

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
    {
      if (paramFloat > 0.5F)
        paramInt1 += 1;
      CustomPagerTitleStrip.this.updateTextPositions(paramInt1, paramFloat);
    }

    public void onPageSelected(int paramInt)
    {
      if (this.mScrollState != 0)
        return;
      CustomPagerTitleStrip localCustomPagerTitleStrip = CustomPagerTitleStrip.this;
      int i = CustomPagerTitleStrip.this.mPager.getCurrentItem();
      PagerAdapter localPagerAdapter = CustomPagerTitleStrip.this.mPager.getAdapter();
      localCustomPagerTitleStrip.updateText(i, localPagerAdapter);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.CustomPagerTitleStrip
 * JD-Core Version:    0.6.2
 */