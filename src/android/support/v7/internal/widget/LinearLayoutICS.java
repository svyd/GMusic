package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R.styleable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class LinearLayoutICS extends LinearLayout
{
  private final Drawable mDivider;
  private final int mDividerHeight;
  private final int mDividerPadding;
  private final int mDividerWidth;
  private final int mShowDividers;

  public LinearLayoutICS(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    int[] arrayOfInt = R.styleable.LinearLayoutICS;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt);
    Drawable localDrawable = localTypedArray.getDrawable(0);
    this.mDivider = localDrawable;
    if (this.mDivider != null)
    {
      int i = this.mDivider.getIntrinsicWidth();
      this.mDividerWidth = i;
      int j = this.mDivider.getIntrinsicHeight();
      this.mDividerHeight = j;
      int k = localTypedArray.getInt(1, 0);
      this.mShowDividers = k;
      int m = localTypedArray.getDimensionPixelSize(2, 0);
      this.mDividerPadding = m;
      localTypedArray.recycle();
      if (this.mDivider != null)
        break label134;
    }
    while (true)
    {
      setWillNotDraw(bool);
      return;
      this.mDividerWidth = 0;
      this.mDividerHeight = 0;
      break;
      label134: bool = false;
    }
  }

  void drawSupportDividersHorizontal(Canvas paramCanvas)
  {
    int i = getChildCount();
    int j = 0;
    while (j < i)
    {
      View localView1 = getChildAt(j);
      if ((localView1 != null) && (localView1.getVisibility() != 8) && (hasSupportDividerBeforeChildAt(j)))
      {
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)localView1.getLayoutParams();
        int k = localView1.getLeft();
        int m = localLayoutParams.leftMargin;
        int n = k - m;
        drawSupportVerticalDivider(paramCanvas, n);
      }
      j += 1;
    }
    if (!hasSupportDividerBeforeChildAt(i))
      return;
    int i1 = i + -1;
    View localView2 = getChildAt(i1);
    int i4;
    int i5;
    if (localView2 == null)
    {
      int i2 = getWidth();
      int i3 = getPaddingRight();
      i4 = i2 - i3;
      i5 = this.mDividerWidth;
    }
    for (int i6 = i4 - i5; ; i6 = localView2.getRight())
    {
      drawSupportVerticalDivider(paramCanvas, i6);
      return;
    }
  }

  void drawSupportDividersVertical(Canvas paramCanvas)
  {
    int i = getChildCount();
    int j = 0;
    while (j < i)
    {
      View localView1 = getChildAt(j);
      if ((localView1 != null) && (localView1.getVisibility() != 8) && (hasSupportDividerBeforeChildAt(j)))
      {
        LinearLayout.LayoutParams localLayoutParams = (LinearLayout.LayoutParams)localView1.getLayoutParams();
        int k = localView1.getTop();
        int m = localLayoutParams.topMargin;
        int n = k - m;
        drawSupportHorizontalDivider(paramCanvas, n);
      }
      j += 1;
    }
    if (!hasSupportDividerBeforeChildAt(i))
      return;
    int i1 = i + -1;
    View localView2 = getChildAt(i1);
    int i4;
    int i5;
    if (localView2 == null)
    {
      int i2 = getHeight();
      int i3 = getPaddingBottom();
      i4 = i2 - i3;
      i5 = this.mDividerHeight;
    }
    for (int i6 = i4 - i5; ; i6 = localView2.getBottom())
    {
      drawSupportHorizontalDivider(paramCanvas, i6);
      return;
    }
  }

  void drawSupportHorizontalDivider(Canvas paramCanvas, int paramInt)
  {
    Drawable localDrawable = this.mDivider;
    int i = getPaddingLeft();
    int j = this.mDividerPadding;
    int k = i + j;
    int m = getWidth();
    int n = getPaddingRight();
    int i1 = m - n;
    int i2 = this.mDividerPadding;
    int i3 = i1 - i2;
    int i4 = this.mDividerHeight + paramInt;
    localDrawable.setBounds(k, paramInt, i3, i4);
    this.mDivider.draw(paramCanvas);
  }

  void drawSupportVerticalDivider(Canvas paramCanvas, int paramInt)
  {
    Drawable localDrawable = this.mDivider;
    int i = getPaddingTop();
    int j = this.mDividerPadding;
    int k = i + j;
    int m = this.mDividerWidth + paramInt;
    int n = getHeight();
    int i1 = getPaddingBottom();
    int i2 = n - i1;
    int i3 = this.mDividerPadding;
    int i4 = i2 - i3;
    localDrawable.setBounds(paramInt, k, m, i4);
    this.mDivider.draw(paramCanvas);
  }

  public int getSupportDividerWidth()
  {
    return this.mDividerWidth;
  }

  protected boolean hasSupportDividerBeforeChildAt(int paramInt)
  {
    boolean bool1 = true;
    if (paramInt == 0)
      if ((this.mShowDividers & 0x1) == 0);
    while (true)
    {
      return bool1;
      bool1 = false;
      continue;
      int i = getChildCount();
      if (paramInt != i)
      {
        if ((this.mShowDividers & 0x4) == 0)
          bool1 = false;
      }
      else
      {
        if ((this.mShowDividers & 0x2) != 0)
        {
          boolean bool2 = false;
          int j = paramInt + -1;
          while (true)
          {
            if (j >= 0)
            {
              if (getChildAt(j).getVisibility() != 8)
                bool2 = true;
            }
            else
            {
              bool1 = bool2;
              break;
            }
            j += -1;
          }
        }
        bool1 = false;
      }
    }
  }

  protected void measureChildWithMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i;
    int j;
    LinearLayout.LayoutParams localLayoutParams;
    if (this.mDivider != null)
    {
      i = indexOfChild(paramView);
      j = getChildCount();
      localLayoutParams = (LinearLayout.LayoutParams)paramView.getLayoutParams();
      if (getOrientation() != 1)
        break label110;
      if (!hasSupportDividerBeforeChildAt(i))
        break label71;
      int k = this.mDividerHeight;
      localLayoutParams.topMargin = k;
    }
    while (true)
    {
      super.measureChildWithMargins(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
      return;
      label71: int m = j + -1;
      if ((i != m) && (hasSupportDividerBeforeChildAt(j)))
      {
        int n = this.mDividerHeight;
        localLayoutParams.bottomMargin = n;
        continue;
        label110: if (hasSupportDividerBeforeChildAt(i))
        {
          int i1 = this.mDividerWidth;
          localLayoutParams.leftMargin = i1;
        }
        else
        {
          int i2 = j + -1;
          if ((i != i2) && (hasSupportDividerBeforeChildAt(j)))
          {
            int i3 = this.mDividerWidth;
            localLayoutParams.rightMargin = i3;
          }
        }
      }
    }
  }

  protected void onDraw(Canvas paramCanvas)
  {
    if (this.mDivider == null)
      return;
    if (getOrientation() == 1)
    {
      drawSupportDividersVertical(paramCanvas);
      return;
    }
    drawSupportDividersHorizontal(paramCanvas);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.LinearLayoutICS
 * JD-Core Version:    0.6.2
 */