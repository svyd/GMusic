package android.support.v4.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.widget.TextView;

public class PagerTabStrip extends PagerTitleStrip
{
  private boolean mDrawFullUnderline;
  private boolean mDrawFullUnderlineSet;
  private int mFullUnderlineHeight;
  private boolean mIgnoreTap;
  private int mIndicatorColor;
  private int mIndicatorHeight;
  private float mInitialMotionX;
  private float mInitialMotionY;
  private int mMinPaddingBottom;
  private int mMinStripHeight;
  private int mMinTextSpacing;
  private int mTabAlpha;
  private int mTabPadding;
  private final Paint mTabPaint;
  private final Rect mTempRect;
  private int mTouchSlop;

  public PagerTabStrip(Context paramContext)
  {
    this(paramContext, null);
  }

  public PagerTabStrip(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Paint localPaint1 = new Paint();
    this.mTabPaint = localPaint1;
    Rect localRect = new Rect();
    this.mTempRect = localRect;
    this.mTabAlpha = 255;
    this.mDrawFullUnderline = false;
    this.mDrawFullUnderlineSet = false;
    int i = this.mTextColor;
    this.mIndicatorColor = i;
    Paint localPaint2 = this.mTabPaint;
    int j = this.mIndicatorColor;
    localPaint2.setColor(j);
    float f = paramContext.getResources().getDisplayMetrics().density;
    int k = (int)(3.0F * f + 0.5F);
    this.mIndicatorHeight = k;
    int m = (int)(6.0F * f + 0.5F);
    this.mMinPaddingBottom = m;
    int n = (int)(64.0F * f);
    this.mMinTextSpacing = n;
    int i1 = (int)(16.0F * f + 0.5F);
    this.mTabPadding = i1;
    int i2 = (int)(1.0F * f + 0.5F);
    this.mFullUnderlineHeight = i2;
    int i3 = (int)(32.0F * f + 0.5F);
    this.mMinStripHeight = i3;
    int i4 = ViewConfiguration.get(paramContext).getScaledTouchSlop();
    this.mTouchSlop = i4;
    int i5 = getPaddingLeft();
    int i6 = getPaddingTop();
    int i7 = getPaddingRight();
    int i8 = getPaddingBottom();
    setPadding(i5, i6, i7, i8);
    int i9 = getTextSpacing();
    setTextSpacing(i9);
    setWillNotDraw(false);
    this.mPrevText.setFocusable(true);
    TextView localTextView1 = this.mPrevText;
    View.OnClickListener local1 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        ViewPager localViewPager = PagerTabStrip.this.mPager;
        int i = PagerTabStrip.this.mPager.getCurrentItem() + -1;
        localViewPager.setCurrentItem(i);
      }
    };
    localTextView1.setOnClickListener(local1);
    this.mNextText.setFocusable(true);
    TextView localTextView2 = this.mNextText;
    View.OnClickListener local2 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        ViewPager localViewPager = PagerTabStrip.this.mPager;
        int i = PagerTabStrip.this.mPager.getCurrentItem() + 1;
        localViewPager.setCurrentItem(i);
      }
    };
    localTextView2.setOnClickListener(local2);
    if (getBackground() != null)
      return;
    this.mDrawFullUnderline = true;
  }

  int getMinHeight()
  {
    int i = super.getMinHeight();
    int j = this.mMinStripHeight;
    return Math.max(i, j);
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    int i = getHeight();
    int j = i;
    int k = this.mCurrText.getLeft();
    int m = this.mTabPadding;
    int n = k - m;
    int i1 = this.mCurrText.getRight();
    int i2 = this.mTabPadding;
    int i3 = i1 + i2;
    int i4 = this.mIndicatorHeight;
    int i5 = j - i4;
    Paint localPaint1 = this.mTabPaint;
    int i6 = this.mTabAlpha << 24;
    int i7 = this.mIndicatorColor & 0xFFFFFF;
    int i8 = i6 | i7;
    localPaint1.setColor(i8);
    float f1 = n;
    float f2 = i5;
    float f3 = i3;
    float f4 = j;
    Paint localPaint2 = this.mTabPaint;
    paramCanvas.drawRect(f1, f2, f3, f4, localPaint2);
    if (!this.mDrawFullUnderline)
      return;
    Paint localPaint3 = this.mTabPaint;
    int i9 = this.mIndicatorColor & 0xFFFFFF;
    int i10 = 0xFF000000 | i9;
    localPaint3.setColor(i10);
    float f5 = getPaddingLeft();
    int i11 = this.mFullUnderlineHeight;
    float f6 = i - i11;
    int i12 = getWidth();
    int i13 = getPaddingRight();
    float f7 = i12 - i13;
    float f8 = i;
    Paint localPaint4 = this.mTabPaint;
    paramCanvas.drawRect(f5, f6, f7, f8, localPaint4);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool = false;
    int i = paramMotionEvent.getAction();
    if ((i != 0) && (this.mIgnoreTap))
      return bool;
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    switch (i)
    {
    default:
    case 0:
    case 2:
    case 1:
    }
    while (true)
    {
      bool = true;
      break;
      this.mInitialMotionX = f1;
      this.mInitialMotionY = f2;
      this.mIgnoreTap = false;
      continue;
      float f3 = this.mInitialMotionX;
      float f4 = Math.abs(f1 - f3);
      float f5 = this.mTouchSlop;
      if (f4 <= f5)
      {
        float f6 = this.mInitialMotionY;
        float f7 = Math.abs(f2 - f6);
        float f8 = this.mTouchSlop;
        if (f7 <= f8);
      }
      else
      {
        this.mIgnoreTap = true;
        continue;
        int j = this.mCurrText.getLeft();
        int k = this.mTabPadding;
        float f9 = j - k;
        if (f1 < f9)
        {
          ViewPager localViewPager1 = this.mPager;
          int m = this.mPager.getCurrentItem() + -1;
          localViewPager1.setCurrentItem(m);
        }
        else
        {
          int n = this.mCurrText.getRight();
          int i1 = this.mTabPadding;
          float f10 = n + i1;
          if (f1 > f10)
          {
            ViewPager localViewPager2 = this.mPager;
            int i2 = this.mPager.getCurrentItem() + 1;
            localViewPager2.setCurrentItem(i2);
          }
        }
      }
    }
  }

  public void setBackgroundColor(int paramInt)
  {
    super.setBackgroundColor(paramInt);
    if (this.mDrawFullUnderlineSet)
      return;
    if ((0xFF000000 & paramInt) == 0);
    for (boolean bool = true; ; bool = false)
    {
      this.mDrawFullUnderline = bool;
      return;
    }
  }

  public void setBackgroundDrawable(Drawable paramDrawable)
  {
    super.setBackgroundDrawable(paramDrawable);
    if (this.mDrawFullUnderlineSet)
      return;
    if (paramDrawable == null);
    for (boolean bool = true; ; bool = false)
    {
      this.mDrawFullUnderline = bool;
      return;
    }
  }

  public void setBackgroundResource(int paramInt)
  {
    super.setBackgroundResource(paramInt);
    if (this.mDrawFullUnderlineSet)
      return;
    if (paramInt == 0);
    for (boolean bool = true; ; bool = false)
    {
      this.mDrawFullUnderline = bool;
      return;
    }
  }

  public void setDrawFullUnderline(boolean paramBoolean)
  {
    this.mDrawFullUnderline = paramBoolean;
    this.mDrawFullUnderlineSet = true;
    invalidate();
  }

  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = this.mMinPaddingBottom;
    if (paramInt4 < i)
      paramInt4 = this.mMinPaddingBottom;
    super.setPadding(paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public void setTabIndicatorColor(int paramInt)
  {
    this.mIndicatorColor = paramInt;
    Paint localPaint = this.mTabPaint;
    int i = this.mIndicatorColor;
    localPaint.setColor(i);
    invalidate();
  }

  public void setTabIndicatorColorResource(int paramInt)
  {
    int i = getContext().getResources().getColor(paramInt);
    setTabIndicatorColor(i);
  }

  public void setTextSpacing(int paramInt)
  {
    int i = this.mMinTextSpacing;
    if (paramInt < i)
      paramInt = this.mMinTextSpacing;
    super.setTextSpacing(paramInt);
  }

  void updateTextPositions(int paramInt, float paramFloat, boolean paramBoolean)
  {
    Rect localRect = this.mTempRect;
    int i = getHeight();
    int j = this.mCurrText.getLeft();
    int k = this.mTabPadding;
    int m = j - k;
    int n = this.mCurrText.getRight();
    int i1 = this.mTabPadding;
    int i2 = n + i1;
    int i3 = this.mIndicatorHeight;
    int i4 = i - i3;
    localRect.set(m, i4, i2, i);
    super.updateTextPositions(paramInt, paramFloat, paramBoolean);
    int i5 = (int)(Math.abs(paramFloat - 0.5F) * 2.0F * 255.0F);
    this.mTabAlpha = i5;
    int i6 = this.mCurrText.getLeft();
    int i7 = this.mTabPadding;
    int i8 = i6 - i7;
    int i9 = this.mCurrText.getRight();
    int i10 = this.mTabPadding;
    int i11 = i9 + i10;
    localRect.union(i8, i4, i11, i);
    invalidate(localRect);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.PagerTabStrip
 * JD-Core Version:    0.6.2
 */