package android.support.v4.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import java.lang.ref.WeakReference;

public class PagerTitleStrip extends ViewGroup
  implements ViewPager.Decor
{
  private static final int[] ATTRS = { 16842804, 16842901, 16842904, 16842927 };
  private static final PagerTitleStripImpl IMPL = new PagerTitleStripImplBase();
  private static final int[] TEXT_ATTRS;
  TextView mCurrText;
  private int mGravity;
  private int mLastKnownCurrentPage = -1;
  private float mLastKnownPositionOffset = -1.0F;
  TextView mNextText;
  private int mNonPrimaryAlpha;
  private final PageListener mPageListener;
  ViewPager mPager;
  TextView mPrevText;
  private int mScaledTextSpacing;
  int mTextColor;
  private boolean mUpdatingPositions;
  private boolean mUpdatingText;
  private WeakReference<PagerAdapter> mWatchingAdapter;

  static
  {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 16843660;
    TEXT_ATTRS = arrayOfInt;
    if (Build.VERSION.SDK_INT >= 14)
    {
      IMPL = new PagerTitleStripImplIcs();
      return;
    }
  }

  public PagerTitleStrip(Context paramContext)
  {
    this(paramContext, null);
  }

  public PagerTitleStrip(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    PageListener localPageListener = new PageListener(null);
    this.mPageListener = localPageListener;
    TextView localTextView1 = new TextView(paramContext);
    this.mPrevText = localTextView1;
    addView(localTextView1);
    TextView localTextView2 = new TextView(paramContext);
    this.mCurrText = localTextView2;
    addView(localTextView2);
    TextView localTextView3 = new TextView(paramContext);
    this.mNextText = localTextView3;
    addView(localTextView3);
    int[] arrayOfInt1 = ATTRS;
    TypedArray localTypedArray1 = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt1);
    int i = localTypedArray1.getResourceId(0, 0);
    if (i != 0)
    {
      this.mPrevText.setTextAppearance(paramContext, i);
      this.mCurrText.setTextAppearance(paramContext, i);
      this.mNextText.setTextAppearance(paramContext, i);
    }
    int j = localTypedArray1.getDimensionPixelSize(1, 0);
    if (j != 0)
    {
      float f1 = j;
      setTextSize(0, f1);
    }
    if (localTypedArray1.hasValue(2))
    {
      int k = localTypedArray1.getColor(2, 0);
      this.mPrevText.setTextColor(k);
      this.mCurrText.setTextColor(k);
      this.mNextText.setTextColor(k);
    }
    int m = localTypedArray1.getInteger(3, 80);
    this.mGravity = m;
    localTypedArray1.recycle();
    int n = this.mCurrText.getTextColors().getDefaultColor();
    this.mTextColor = n;
    setNonPrimaryAlpha(0.6F);
    TextView localTextView4 = this.mPrevText;
    TextUtils.TruncateAt localTruncateAt1 = TextUtils.TruncateAt.END;
    localTextView4.setEllipsize(localTruncateAt1);
    TextView localTextView5 = this.mCurrText;
    TextUtils.TruncateAt localTruncateAt2 = TextUtils.TruncateAt.END;
    localTextView5.setEllipsize(localTruncateAt2);
    TextView localTextView6 = this.mNextText;
    TextUtils.TruncateAt localTruncateAt3 = TextUtils.TruncateAt.END;
    localTextView6.setEllipsize(localTruncateAt3);
    boolean bool = false;
    if (i != 0)
    {
      int[] arrayOfInt2 = TEXT_ATTRS;
      TypedArray localTypedArray2 = paramContext.obtainStyledAttributes(i, arrayOfInt2);
      bool = localTypedArray2.getBoolean(0, false);
      localTypedArray2.recycle();
    }
    if (bool)
    {
      setSingleLineAllCaps(this.mPrevText);
      setSingleLineAllCaps(this.mCurrText);
      setSingleLineAllCaps(this.mNextText);
    }
    while (true)
    {
      float f2 = paramContext.getResources().getDisplayMetrics().density;
      int i1 = (int)(16.0F * f2);
      this.mScaledTextSpacing = i1;
      return;
      this.mPrevText.setSingleLine();
      this.mCurrText.setSingleLine();
      this.mNextText.setSingleLine();
    }
  }

  private static void setSingleLineAllCaps(TextView paramTextView)
  {
    IMPL.setSingleLineAllCaps(paramTextView);
  }

  int getMinHeight()
  {
    int i = 0;
    Drawable localDrawable = getBackground();
    if (localDrawable != null)
      i = localDrawable.getIntrinsicHeight();
    return i;
  }

  public int getTextSpacing()
  {
    return this.mScaledTextSpacing;
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    ViewParent localViewParent = getParent();
    if (!(localViewParent instanceof ViewPager))
      throw new IllegalStateException("PagerTitleStrip must be a direct child of a ViewPager.");
    ViewPager localViewPager = (ViewPager)localViewParent;
    PagerAdapter localPagerAdapter1 = localViewPager.getAdapter();
    PageListener localPageListener1 = this.mPageListener;
    ViewPager.OnPageChangeListener localOnPageChangeListener = localViewPager.setInternalPageChangeListener(localPageListener1);
    PageListener localPageListener2 = this.mPageListener;
    localViewPager.setOnAdapterChangeListener(localPageListener2);
    this.mPager = localViewPager;
    if (this.mWatchingAdapter != null);
    for (PagerAdapter localPagerAdapter2 = (PagerAdapter)this.mWatchingAdapter.get(); ; localPagerAdapter2 = null)
    {
      updateAdapter(localPagerAdapter2, localPagerAdapter1);
      return;
    }
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (this.mPager == null)
      return;
    PagerAdapter localPagerAdapter = this.mPager.getAdapter();
    updateAdapter(localPagerAdapter, null);
    ViewPager.OnPageChangeListener localOnPageChangeListener = this.mPager.setInternalPageChangeListener(null);
    this.mPager.setOnAdapterChangeListener(null);
    this.mPager = null;
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    float f = 0.0F;
    if (this.mPager == null)
      return;
    if (this.mLastKnownPositionOffset >= 0.0F)
      f = this.mLastKnownPositionOffset;
    int i = this.mLastKnownCurrentPage;
    updateTextPositions(i, f, true);
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
    int i1 = getMinHeight();
    int i2 = getPaddingTop();
    int i3 = getPaddingBottom();
    int i4 = i2 + i3;
    int i5 = n - i4;
    int i6 = View.MeasureSpec.makeMeasureSpec((int)(k * 0.8F), -2147483648);
    int i7 = View.MeasureSpec.makeMeasureSpec(i5, -2147483648);
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

  public void setGravity(int paramInt)
  {
    this.mGravity = paramInt;
    requestLayout();
  }

  public void setNonPrimaryAlpha(float paramFloat)
  {
    int i = (int)(255.0F * paramFloat) & 0xFF;
    this.mNonPrimaryAlpha = i;
    int j = this.mNonPrimaryAlpha << 24;
    int k = this.mTextColor & 0xFFFFFF;
    int m = j | k;
    this.mPrevText.setTextColor(m);
    this.mNextText.setTextColor(m);
  }

  public void setTextColor(int paramInt)
  {
    this.mTextColor = paramInt;
    this.mCurrText.setTextColor(paramInt);
    int i = this.mNonPrimaryAlpha << 24;
    int j = this.mTextColor & 0xFFFFFF;
    int k = i | j;
    this.mPrevText.setTextColor(k);
    this.mNextText.setTextColor(k);
  }

  public void setTextSize(int paramInt, float paramFloat)
  {
    this.mPrevText.setTextSize(paramInt, paramFloat);
    this.mCurrText.setTextSize(paramInt, paramFloat);
    this.mNextText.setTextSize(paramInt, paramFloat);
  }

  public void setTextSpacing(int paramInt)
  {
    this.mScaledTextSpacing = paramInt;
    requestLayout();
  }

  void updateAdapter(PagerAdapter paramPagerAdapter1, PagerAdapter paramPagerAdapter2)
  {
    if (paramPagerAdapter1 != null)
    {
      PageListener localPageListener1 = this.mPageListener;
      paramPagerAdapter1.unregisterDataSetObserver(localPageListener1);
      this.mWatchingAdapter = null;
    }
    if (paramPagerAdapter2 != null)
    {
      PageListener localPageListener2 = this.mPageListener;
      paramPagerAdapter2.registerDataSetObserver(localPageListener2);
      WeakReference localWeakReference = new WeakReference(paramPagerAdapter2);
      this.mWatchingAdapter = localWeakReference;
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
      if ((paramPagerAdapter == null) || (paramInt >= i))
        break label274;
    }
    label274: for (CharSequence localCharSequence2 = paramPagerAdapter.getPageTitle(paramInt); ; localCharSequence2 = null)
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
      int i10 = View.MeasureSpec.makeMeasureSpec(i8, -2147483648);
      this.mPrevText.measure(i9, i10);
      this.mCurrText.measure(i9, i10);
      this.mNextText.measure(i9, i10);
      this.mLastKnownCurrentPage = paramInt;
      if (!this.mUpdatingPositions)
      {
        float f = this.mLastKnownPositionOffset;
        updateTextPositions(paramInt, f, false);
      }
      this.mUpdatingText = false;
      return;
      i = 0;
      break;
    }
  }

  void updateTextPositions(int paramInt, float paramFloat, boolean paramBoolean)
  {
    int i = this.mLastKnownCurrentPage;
    int j = paramInt;
    int k = i;
    int n;
    int i2;
    int i4;
    int i5;
    int i6;
    int i7;
    int i8;
    int i9;
    int i17;
    int i18;
    int i25;
    int i26;
    int i27;
    int i34;
    int i35;
    int i36;
    int i37;
    if (j != k)
    {
      PagerAdapter localPagerAdapter1 = this.mPager.getAdapter();
      PagerTitleStrip localPagerTitleStrip = this;
      int m = paramInt;
      PagerAdapter localPagerAdapter2 = localPagerAdapter1;
      localPagerTitleStrip.updateText(m, localPagerAdapter2);
      boolean bool1 = true;
      this.mUpdatingPositions = bool1;
      n = this.mPrevText.getMeasuredWidth();
      int i1 = this.mCurrText.getMeasuredWidth();
      i2 = this.mNextText.getMeasuredWidth();
      int i3 = i1 / 2;
      i4 = getWidth();
      i5 = getHeight();
      i6 = getPaddingLeft();
      i7 = getPaddingRight();
      i8 = getPaddingTop();
      i9 = getPaddingBottom();
      int i10 = i6 + i3;
      int i11 = i7 + i3;
      int i12 = i4 - i10 - i11;
      float f1 = paramFloat + 0.5F;
      if (f1 > 1.0F)
        f1 -= 1.0F;
      int i13 = i4 - i11;
      int i14 = (int)(i12 * f1);
      int i15 = i13 - i14;
      int i16 = i1 / 2;
      i17 = i15 - i16;
      i18 = i17 + i1;
      int i19 = this.mPrevText.getBaseline();
      int i20 = this.mCurrText.getBaseline();
      int i21 = this.mNextText.getBaseline();
      int i22 = Math.max(i19, i20);
      int i23 = i21;
      int i24 = Math.max(i22, i23);
      i25 = i24 - i19;
      i26 = i24 - i20;
      i27 = i24 - i21;
      int i28 = this.mPrevText.getMeasuredHeight();
      int i29 = i25 + i28;
      int i30 = this.mCurrText.getMeasuredHeight();
      int i31 = i26 + i30;
      int i32 = this.mNextText.getMeasuredHeight();
      int i33 = i27 + i32;
      i34 = Math.max(Math.max(i29, i31), i33);
      switch (this.mGravity & 0x70)
      {
      default:
        i35 = i8 + i25;
        i36 = i8 + i26;
        i37 = i8 + i27;
      case 16:
      case 80:
      }
    }
    while (true)
    {
      TextView localTextView1 = this.mCurrText;
      int i38 = this.mCurrText.getMeasuredHeight() + i36;
      TextView localTextView2 = localTextView1;
      int i39 = i36;
      int i40 = i38;
      localTextView2.layout(i17, i39, i18, i40);
      int i41 = this.mScaledTextSpacing;
      int i42 = i17 - i41 - n;
      int i43 = i6;
      int i44 = i42;
      int i45 = Math.min(i43, i44);
      TextView localTextView3 = this.mPrevText;
      int i46 = i45 + n;
      int i47 = this.mPrevText.getMeasuredHeight() + i35;
      TextView localTextView4 = localTextView3;
      int i48 = i45;
      int i49 = i35;
      int i50 = i46;
      int i51 = i47;
      localTextView4.layout(i48, i49, i50, i51);
      int i52 = i4 - i7 - i2;
      int i53 = this.mScaledTextSpacing + i18;
      int i54 = Math.max(i52, i53);
      TextView localTextView5 = this.mNextText;
      int i55 = i54 + i2;
      int i56 = this.mNextText.getMeasuredHeight() + i37;
      TextView localTextView6 = localTextView5;
      int i57 = i54;
      int i58 = i37;
      int i59 = i55;
      int i60 = i56;
      localTextView6.layout(i57, i58, i59, i60);
      float f2 = paramFloat;
      this.mLastKnownPositionOffset = f2;
      boolean bool2 = false;
      this.mUpdatingPositions = bool2;
      return;
      if (paramBoolean)
        break;
      float f3 = this.mLastKnownPositionOffset;
      if (paramFloat != f3)
        break;
      return;
      int i61 = (i5 - i8 - i9 - i34) / 2;
      i35 = i61 + i25;
      i36 = i61 + i26;
      i37 = i61 + i27;
      continue;
      int i62 = i5 - i9 - i34;
      i35 = i62 + i25;
      i36 = i62 + i26;
      i37 = i62 + i27;
    }
  }

  private class PageListener extends DataSetObserver
    implements ViewPager.OnAdapterChangeListener, ViewPager.OnPageChangeListener
  {
    private int mScrollState;

    private PageListener()
    {
    }

    public void onAdapterChanged(PagerAdapter paramPagerAdapter1, PagerAdapter paramPagerAdapter2)
    {
      PagerTitleStrip.this.updateAdapter(paramPagerAdapter1, paramPagerAdapter2);
    }

    public void onChanged()
    {
      float f = 0.0F;
      PagerTitleStrip localPagerTitleStrip1 = PagerTitleStrip.this;
      int i = PagerTitleStrip.this.mPager.getCurrentItem();
      PagerAdapter localPagerAdapter = PagerTitleStrip.this.mPager.getAdapter();
      localPagerTitleStrip1.updateText(i, localPagerAdapter);
      if (PagerTitleStrip.this.mLastKnownPositionOffset >= f)
        f = PagerTitleStrip.this.mLastKnownPositionOffset;
      PagerTitleStrip localPagerTitleStrip2 = PagerTitleStrip.this;
      int j = PagerTitleStrip.this.mPager.getCurrentItem();
      localPagerTitleStrip2.updateTextPositions(j, f, true);
    }

    public void onPageScrollStateChanged(int paramInt)
    {
      this.mScrollState = paramInt;
    }

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
    {
      if (paramFloat > 0.5F)
        paramInt1 += 1;
      PagerTitleStrip.this.updateTextPositions(paramInt1, paramFloat, false);
    }

    public void onPageSelected(int paramInt)
    {
      float f = 0.0F;
      if (this.mScrollState != 0)
        return;
      PagerTitleStrip localPagerTitleStrip1 = PagerTitleStrip.this;
      int i = PagerTitleStrip.this.mPager.getCurrentItem();
      PagerAdapter localPagerAdapter = PagerTitleStrip.this.mPager.getAdapter();
      localPagerTitleStrip1.updateText(i, localPagerAdapter);
      if (PagerTitleStrip.this.mLastKnownPositionOffset >= 0.0F)
        f = PagerTitleStrip.this.mLastKnownPositionOffset;
      PagerTitleStrip localPagerTitleStrip2 = PagerTitleStrip.this;
      int j = PagerTitleStrip.this.mPager.getCurrentItem();
      localPagerTitleStrip2.updateTextPositions(j, f, true);
    }
  }

  static class PagerTitleStripImplIcs
    implements PagerTitleStrip.PagerTitleStripImpl
  {
    public void setSingleLineAllCaps(TextView paramTextView)
    {
      PagerTitleStripIcs.setSingleLineAllCaps(paramTextView);
    }
  }

  static class PagerTitleStripImplBase
    implements PagerTitleStrip.PagerTitleStripImpl
  {
    public void setSingleLineAllCaps(TextView paramTextView)
    {
      paramTextView.setSingleLine();
    }
  }

  static abstract interface PagerTitleStripImpl
  {
    public abstract void setSingleLineAllCaps(TextView paramTextView);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.PagerTitleStrip
 * JD-Core Version:    0.6.2
 */