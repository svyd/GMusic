package com.google.android.music;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class VerticalTextSpinner extends View
{
  private static int SCROLL_DISTANCE;
  private static int TEXT1_Y;
  private static int TEXT2_Y;
  private static int TEXT3_Y;
  private static int TEXT4_Y;
  private static int TEXT5_Y;
  private static int TEXT_MARGIN_RIGHT;
  private static int TEXT_SIZE;
  private static int TEXT_SPACING;
  private boolean isDraggingSelector;
  private final Drawable mBackgroundFocused;
  private int mCurrentSelectedPos;
  private long mDelayBetweenAnimations;
  private int mDistanceOfEachAnimation;
  private int mDownY;
  private boolean mIsAnimationRunning;
  private OnChangedListener mListener;
  private int mNumberOfAnimations;
  private long mScrollInterval;
  private int mScrollMode;
  private Drawable mSelector;
  private final int mSelectorDefaultY;
  private final Drawable mSelectorFocused;
  private final int mSelectorHeight;
  private final int mSelectorMaxY;
  private final int mSelectorMinY;
  private final Drawable mSelectorNormal;
  private int mSelectorY;
  private boolean mStopAnimation;
  private String mText1;
  private String mText2;
  private String mText3;
  private String mText4;
  private String mText5;
  private String[] mTextList;
  private final TextPaint mTextPaintDark;
  private final TextPaint mTextPaintLight;
  private int mTotalAnimatedDistance;
  private boolean mWrapAround = true;

  public VerticalTextSpinner(Context paramContext)
  {
    this(paramContext, null);
  }

  public VerticalTextSpinner(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public VerticalTextSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    float f1 = getResources().getDisplayMetrics().density;
    TEXT_SPACING = (int)(18.0F * f1);
    TEXT_MARGIN_RIGHT = (int)(25.0F * f1);
    TEXT_SIZE = (int)(22.0F * f1);
    int i = TEXT_SIZE;
    int j = TEXT_SPACING;
    SCROLL_DISTANCE = i + j;
    int k = TEXT_SIZE * 0;
    int m = TEXT_SPACING * -1;
    TEXT1_Y = k + m;
    int n = TEXT_SIZE * 1;
    int i1 = TEXT_SPACING * 0;
    TEXT2_Y = n + i1;
    int i2 = TEXT_SIZE * 2;
    int i3 = TEXT_SPACING * 1;
    TEXT3_Y = i2 + i3;
    int i4 = TEXT_SIZE * 3;
    int i5 = TEXT_SPACING * 2;
    TEXT4_Y = i4 + i5;
    int i6 = TEXT_SIZE * 4;
    int i7 = TEXT_SPACING * 3;
    TEXT5_Y = i6 + i7;
    Drawable localDrawable1 = paramContext.getResources().getDrawable(2130837850);
    this.mBackgroundFocused = localDrawable1;
    Drawable localDrawable2 = paramContext.getResources().getDrawable(2130837851);
    this.mSelectorFocused = localDrawable2;
    Drawable localDrawable3 = paramContext.getResources().getDrawable(2130837852);
    this.mSelectorNormal = localDrawable3;
    int i8 = this.mSelectorFocused.getIntrinsicHeight();
    this.mSelectorHeight = i8;
    int i9 = this.mBackgroundFocused.getIntrinsicHeight();
    int i10 = this.mSelectorHeight;
    int i11 = (i9 - i10) / 2;
    this.mSelectorDefaultY = i11;
    this.mSelectorMinY = 0;
    int i12 = this.mBackgroundFocused.getIntrinsicHeight();
    int i13 = this.mSelectorHeight;
    int i14 = i12 - i13;
    this.mSelectorMaxY = i14;
    Drawable localDrawable4 = this.mSelectorNormal;
    this.mSelector = localDrawable4;
    int i15 = this.mSelectorDefaultY;
    this.mSelectorY = i15;
    TextPaint localTextPaint1 = new TextPaint(1);
    this.mTextPaintDark = localTextPaint1;
    TextPaint localTextPaint2 = this.mTextPaintDark;
    float f2 = TEXT_SIZE;
    localTextPaint2.setTextSize(f2);
    TextPaint localTextPaint3 = this.mTextPaintDark;
    int i16 = paramContext.getResources().getColor(17170435);
    localTextPaint3.setColor(i16);
    TextPaint localTextPaint4 = new TextPaint(1);
    this.mTextPaintLight = localTextPaint4;
    TextPaint localTextPaint5 = this.mTextPaintLight;
    float f3 = TEXT_SIZE;
    localTextPaint5.setTextSize(f3);
    TextPaint localTextPaint6 = this.mTextPaintLight;
    int i17 = paramContext.getResources().getColor(17170437);
    localTextPaint6.setColor(i17);
    this.mScrollMode = 0;
    this.mScrollInterval = 400L;
    calculateAnimationValues();
  }

  private void calculateAnimationValues()
  {
    int i = (int)this.mScrollInterval;
    int j = SCROLL_DISTANCE;
    int k = i / j;
    this.mNumberOfAnimations = k;
    if (this.mNumberOfAnimations < 4)
    {
      this.mNumberOfAnimations = 4;
      int m = SCROLL_DISTANCE;
      int n = this.mNumberOfAnimations;
      int i1 = m / n;
      this.mDistanceOfEachAnimation = i1;
      this.mDelayBetweenAnimations = 0L;
      return;
    }
    int i2 = SCROLL_DISTANCE;
    int i3 = this.mNumberOfAnimations;
    int i4 = i2 / i3;
    this.mDistanceOfEachAnimation = i4;
    long l1 = this.mScrollInterval;
    long l2 = this.mNumberOfAnimations;
    long l3 = l1 / l2;
    this.mDelayBetweenAnimations = l3;
  }

  private void calculateTextPositions()
  {
    String str1 = getTextToDraw(-1);
    this.mText1 = str1;
    String str2 = getTextToDraw(-1);
    this.mText2 = str2;
    String str3 = getTextToDraw(0);
    this.mText3 = str3;
    String str4 = getTextToDraw(1);
    this.mText4 = str4;
    String str5 = getTextToDraw(2);
    this.mText5 = str5;
  }

  private boolean canScrollDown()
  {
    if ((this.mCurrentSelectedPos > 0) || (this.mWrapAround));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private boolean canScrollUp()
  {
    int i = this.mCurrentSelectedPos;
    int j = this.mTextList.length + -1;
    if ((i < j) || (this.mWrapAround));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private void drawText(Canvas paramCanvas, String paramString, int paramInt, TextPaint paramTextPaint)
  {
    int i = (int)paramTextPaint.measureText(paramString);
    int j = getMeasuredWidth() - i;
    int k = TEXT_MARGIN_RIGHT;
    float f1 = j - k;
    float f2 = paramInt;
    paramCanvas.drawText(paramString, f1, f2, paramTextPaint);
  }

  private int getNewIndex(int paramInt)
  {
    int i = -1;
    int j = this.mCurrentSelectedPos + paramInt;
    if (j < 0)
      if (this.mWrapAround)
      {
        i = this.mTextList.length;
        j += i;
      }
    while (true)
    {
      int k = j;
      do
      {
        return i;
        int m = this.mTextList.length;
        if (j < m)
          break;
      }
      while (!this.mWrapAround);
      i = this.mTextList.length;
      j -= i;
    }
  }

  private String getTextToDraw(int paramInt)
  {
    int i = getNewIndex(paramInt);
    if (i < 0);
    for (String str = ""; ; str = this.mTextList[i])
      return str;
  }

  private void scroll()
  {
    if (this.mIsAnimationRunning)
      return;
    this.mTotalAnimatedDistance = 0;
    this.mIsAnimationRunning = true;
    invalidate();
  }

  protected void onDraw(Canvas paramCanvas)
  {
    int i = this.mSelectorY;
    int j = getWidth();
    int k = this.mSelectorY;
    int m = this.mSelectorHeight;
    int n = k + m;
    Drawable localDrawable1 = this.mSelector;
    int i1 = 0;
    localDrawable1.setBounds(i1, i, j, n);
    Drawable localDrawable2 = this.mSelector;
    Canvas localCanvas1 = paramCanvas;
    localDrawable2.draw(localCanvas1);
    if (this.mTextList == null)
      return;
    TextPaint localTextPaint1 = this.mTextPaintDark;
    int i65;
    int i67;
    if (hasFocus())
    {
      int i2 = j;
      int i3 = i + 15;
      String str1 = this.mText1;
      String str2 = this.mText2;
      String str3 = this.mText3;
      String str4 = this.mText4;
      String str5 = this.mText5;
      TextPaint localTextPaint2 = this.mTextPaintLight;
      int i4 = paramCanvas.save();
      Canvas localCanvas2 = paramCanvas;
      int i5 = 0;
      int i6 = 0;
      int i7 = i2;
      int i8 = i3;
      boolean bool1 = localCanvas2.clipRect(i5, i6, i7, i8);
      int i9 = TEXT1_Y;
      int i10 = this.mTotalAnimatedDistance;
      int i11 = i9 + i10;
      VerticalTextSpinner localVerticalTextSpinner1 = this;
      Canvas localCanvas3 = paramCanvas;
      String str6 = str1;
      int i12 = i11;
      TextPaint localTextPaint3 = localTextPaint2;
      localVerticalTextSpinner1.drawText(localCanvas3, str6, i12, localTextPaint3);
      int i13 = TEXT2_Y;
      int i14 = this.mTotalAnimatedDistance;
      int i15 = i13 + i14;
      VerticalTextSpinner localVerticalTextSpinner2 = this;
      Canvas localCanvas4 = paramCanvas;
      String str7 = str2;
      int i16 = i15;
      TextPaint localTextPaint4 = localTextPaint2;
      localVerticalTextSpinner2.drawText(localCanvas4, str7, i16, localTextPaint4);
      int i17 = TEXT3_Y;
      int i18 = this.mTotalAnimatedDistance;
      int i19 = i17 + i18;
      VerticalTextSpinner localVerticalTextSpinner3 = this;
      Canvas localCanvas5 = paramCanvas;
      String str8 = str3;
      int i20 = i19;
      TextPaint localTextPaint5 = localTextPaint2;
      localVerticalTextSpinner3.drawText(localCanvas5, str8, i20, localTextPaint5);
      paramCanvas.restore();
      int i21 = paramCanvas.save();
      int i22 = i + 15;
      int i23 = n + -15;
      Canvas localCanvas6 = paramCanvas;
      int i24 = 0;
      int i25 = i22;
      int i26 = i23;
      boolean bool2 = localCanvas6.clipRect(i24, i25, j, i26);
      int i27 = TEXT2_Y;
      int i28 = this.mTotalAnimatedDistance;
      int i29 = i27 + i28;
      VerticalTextSpinner localVerticalTextSpinner4 = this;
      Canvas localCanvas7 = paramCanvas;
      String str9 = str2;
      int i30 = i29;
      TextPaint localTextPaint6 = localTextPaint1;
      localVerticalTextSpinner4.drawText(localCanvas7, str9, i30, localTextPaint6);
      int i31 = TEXT3_Y;
      int i32 = this.mTotalAnimatedDistance;
      int i33 = i31 + i32;
      VerticalTextSpinner localVerticalTextSpinner5 = this;
      Canvas localCanvas8 = paramCanvas;
      String str10 = str3;
      int i34 = i33;
      TextPaint localTextPaint7 = localTextPaint1;
      localVerticalTextSpinner5.drawText(localCanvas8, str10, i34, localTextPaint7);
      int i35 = TEXT4_Y;
      int i36 = this.mTotalAnimatedDistance;
      int i37 = i35 + i36;
      VerticalTextSpinner localVerticalTextSpinner6 = this;
      Canvas localCanvas9 = paramCanvas;
      String str11 = str4;
      int i38 = i37;
      TextPaint localTextPaint8 = localTextPaint1;
      localVerticalTextSpinner6.drawText(localCanvas9, str11, i38, localTextPaint8);
      paramCanvas.restore();
      int i39 = n + -15;
      int i40 = j;
      int i41 = getMeasuredHeight();
      int i42 = paramCanvas.save();
      Canvas localCanvas10 = paramCanvas;
      int i43 = 0;
      boolean bool3 = localCanvas10.clipRect(i43, i39, i40, i41);
      int i44 = TEXT3_Y;
      int i45 = this.mTotalAnimatedDistance;
      int i46 = i44 + i45;
      VerticalTextSpinner localVerticalTextSpinner7 = this;
      Canvas localCanvas11 = paramCanvas;
      String str12 = str3;
      int i47 = i46;
      TextPaint localTextPaint9 = localTextPaint2;
      localVerticalTextSpinner7.drawText(localCanvas11, str12, i47, localTextPaint9);
      int i48 = TEXT4_Y;
      int i49 = this.mTotalAnimatedDistance;
      int i50 = i48 + i49;
      VerticalTextSpinner localVerticalTextSpinner8 = this;
      Canvas localCanvas12 = paramCanvas;
      String str13 = str4;
      int i51 = i50;
      TextPaint localTextPaint10 = localTextPaint2;
      localVerticalTextSpinner8.drawText(localCanvas12, str13, i51, localTextPaint10);
      int i52 = TEXT5_Y;
      int i53 = this.mTotalAnimatedDistance;
      int i54 = i52 + i53;
      VerticalTextSpinner localVerticalTextSpinner9 = this;
      Canvas localCanvas13 = paramCanvas;
      String str14 = str5;
      int i55 = i54;
      TextPaint localTextPaint11 = localTextPaint2;
      localVerticalTextSpinner9.drawText(localCanvas13, str14, i55, localTextPaint11);
      paramCanvas.restore();
      if (!this.mIsAnimationRunning)
        return;
      int i56 = Math.abs(this.mTotalAnimatedDistance);
      int i57 = this.mDistanceOfEachAnimation;
      int i58 = i56 + i57;
      int i59 = SCROLL_DISTANCE;
      int i60 = i58;
      int i61 = i59;
      if (i60 <= i61)
        break label1219;
      int i62 = 0;
      this.mTotalAnimatedDistance = i62;
      int i63 = this.mScrollMode;
      int i64 = 1;
      if (i63 != i64)
        break label1081;
      i65 = this.mCurrentSelectedPos;
      VerticalTextSpinner localVerticalTextSpinner10 = this;
      int i66 = 1;
      i67 = localVerticalTextSpinner10.getNewIndex(i66);
      if (i67 >= 0)
      {
        this.mCurrentSelectedPos = i67;
        if (this.mListener != null)
        {
          OnChangedListener localOnChangedListener1 = this.mListener;
          int i68 = this.mCurrentSelectedPos;
          String[] arrayOfString1 = this.mTextList;
          OnChangedListener localOnChangedListener2 = localOnChangedListener1;
          VerticalTextSpinner localVerticalTextSpinner11 = this;
          int i69 = i68;
          String[] arrayOfString2 = arrayOfString1;
          localOnChangedListener2.onChanged(localVerticalTextSpinner11, i65, i69, arrayOfString2);
        }
      }
      if (i67 >= 0)
      {
        int i70 = this.mTextList.length + -1;
        if ((i67 < i70) || (this.mWrapAround));
      }
      else
      {
        boolean bool4 = true;
        this.mStopAnimation = bool4;
      }
      calculateTextPositions();
      label916: if (this.mStopAnimation)
      {
        int i71 = this.mScrollMode;
        boolean bool5 = false;
        this.mIsAnimationRunning = bool5;
        boolean bool6 = false;
        this.mStopAnimation = bool6;
        int i72 = 0;
        this.mScrollMode = i72;
        String[] arrayOfString3 = this.mTextList;
        int i73 = this.mCurrentSelectedPos;
        String str15 = arrayOfString3[i73];
        if ("".equals(str15))
        {
          this.mScrollMode = i71;
          scroll();
          boolean bool7 = true;
          this.mStopAnimation = bool7;
        }
      }
    }
    while (true)
    {
      if (this.mDelayBetweenAnimations <= 0L)
        break label1307;
      long l1 = this.mDelayBetweenAnimations;
      VerticalTextSpinner localVerticalTextSpinner12 = this;
      long l2 = l1;
      localVerticalTextSpinner12.postInvalidateDelayed(l2);
      return;
      String str16 = this.mText3;
      int i74 = TEXT3_Y;
      VerticalTextSpinner localVerticalTextSpinner13 = this;
      Canvas localCanvas14 = paramCanvas;
      String str17 = str16;
      int i75 = i74;
      TextPaint localTextPaint12 = localTextPaint1;
      localVerticalTextSpinner13.drawText(localCanvas14, str17, i75, localTextPaint12);
      break;
      label1081: int i77 = this.mScrollMode;
      int i76 = 2;
      if (i77 != i76)
        break label916;
      i65 = this.mCurrentSelectedPos;
      VerticalTextSpinner localVerticalTextSpinner14 = this;
      int i78 = 65535;
      i67 = localVerticalTextSpinner14.getNewIndex(i78);
      if (i67 >= 0)
      {
        this.mCurrentSelectedPos = i67;
        if (this.mListener != null)
        {
          OnChangedListener localOnChangedListener3 = this.mListener;
          int i79 = this.mCurrentSelectedPos;
          String[] arrayOfString4 = this.mTextList;
          OnChangedListener localOnChangedListener4 = localOnChangedListener3;
          VerticalTextSpinner localVerticalTextSpinner15 = this;
          int i80 = i79;
          String[] arrayOfString5 = arrayOfString4;
          localOnChangedListener4.onChanged(localVerticalTextSpinner15, i65, i80, arrayOfString5);
        }
      }
      if ((i67 < 0) || ((i67 == 0) && (!this.mWrapAround)))
      {
        i81 = 1;
        this.mStopAnimation = i81;
      }
      calculateTextPositions();
      break label916;
      label1219: int i82 = this.mScrollMode;
      int i81 = 1;
      int i85;
      if (i82 == i81)
      {
        int i83 = this.mTotalAnimatedDistance;
        int i84 = this.mDistanceOfEachAnimation;
        i85 = i83 - i84;
        this.mTotalAnimatedDistance = i85;
      }
      else
      {
        int i86 = this.mScrollMode;
        i85 = 2;
        if (i86 == i85)
        {
          int i87 = this.mTotalAnimatedDistance;
          int i88 = this.mDistanceOfEachAnimation;
          int i89 = i87 + i88;
          this.mTotalAnimatedDistance = i89;
        }
      }
    }
    label1307: invalidate();
  }

  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    if (paramBoolean)
    {
      Drawable localDrawable1 = this.mBackgroundFocused;
      setBackgroundDrawable(localDrawable1);
      Drawable localDrawable2 = this.mSelectorFocused;
      this.mSelector = localDrawable2;
      return;
    }
    setBackgroundDrawable(null);
    Drawable localDrawable3 = this.mSelectorNormal;
    this.mSelector = localDrawable3;
    int i = this.mSelectorDefaultY;
    this.mSelectorY = i;
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = true;
    if ((paramInt == 19) && (canScrollDown()))
    {
      this.mScrollMode = 2;
      scroll();
      this.mStopAnimation = true;
    }
    while (true)
    {
      return bool;
      if ((paramInt == 20) && (canScrollUp()))
      {
        this.mScrollMode = 1;
        scroll();
        this.mStopAnimation = true;
      }
      else
      {
        bool = super.onKeyDown(paramInt, paramKeyEvent);
      }
    }
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = false;
    int i = paramMotionEvent.getAction();
    int j = (int)paramMotionEvent.getY();
    switch (i)
    {
    case 1:
    default:
      int k = this.mSelectorDefaultY;
      this.mSelectorY = k;
      this.mStopAnimation = true;
      invalidate();
    case 0:
    case 2:
    }
    while (true)
    {
      return true;
      boolean bool2 = requestFocus();
      this.mDownY = j;
      int m = this.mSelectorY;
      if (j >= m)
      {
        int n = this.mSelectorY;
        int i1 = this.mSelector.getIntrinsicHeight();
        int i2 = n + i1;
        if (j <= i2)
          bool1 = true;
      }
      this.isDraggingSelector = bool1;
      continue;
      if (this.isDraggingSelector)
      {
        int i3 = this.mSelectorDefaultY;
        int i4 = this.mDownY;
        int i5 = j - i4;
        int i6 = i3 + i5;
        int i7 = this.mSelectorMinY;
        if ((i6 <= i7) && (canScrollDown()))
        {
          int i8 = this.mSelectorMinY;
          this.mSelectorY = i8;
          this.mStopAnimation = false;
          if (this.mScrollMode != 2)
          {
            this.mScrollMode = 2;
            scroll();
          }
        }
        else
        {
          int i9 = this.mSelectorMaxY;
          if ((i6 >= i9) && (canScrollUp()))
          {
            int i10 = this.mSelectorMaxY;
            this.mSelectorY = i10;
            this.mStopAnimation = false;
            if (this.mScrollMode != 1)
            {
              this.mScrollMode = 1;
              scroll();
            }
          }
          else
          {
            this.mSelectorY = i6;
            this.mStopAnimation = true;
          }
        }
      }
    }
  }

  public void setItems(String[] paramArrayOfString)
  {
    this.mTextList = paramArrayOfString;
    calculateTextPositions();
  }

  public void setOnChangeListener(OnChangedListener paramOnChangedListener)
  {
    this.mListener = paramOnChangedListener;
  }

  public void setScrollInterval(long paramLong)
  {
    this.mScrollInterval = paramLong;
    calculateAnimationValues();
  }

  public void setSelectedPos(int paramInt)
  {
    this.mCurrentSelectedPos = paramInt;
    calculateTextPositions();
    postInvalidate();
  }

  public void setWrapAround(boolean paramBoolean)
  {
    this.mWrapAround = paramBoolean;
  }

  public static abstract interface OnChangedListener
  {
    public abstract void onChanged(VerticalTextSpinner paramVerticalTextSpinner, int paramInt1, int paramInt2, String[] paramArrayOfString);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.VerticalTextSpinner
 * JD-Core Version:    0.6.2
 */