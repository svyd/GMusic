package com.google.android.music.menu;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import com.google.android.music.R.styleable;
import java.util.ArrayList;

public class MusicOptionsMenuView extends ViewGroup
{
  private boolean mHasStaleChildren = true;
  private Drawable mHorizontalDivider;
  private int mHorizontalDividerHeight;
  private ArrayList<Rect> mHorizontalDividerRects;
  private int[] mLayout;
  private int mLayoutNumRows;
  private int mMaxItemsPerRow;
  private int mMaxRows;
  private MusicMenu mMenu;
  private int mRowHeight;
  private Drawable mVerticalDivider;
  private ArrayList<Rect> mVerticalDividerRects;
  private int mVerticalDividerWidth;

  public MusicOptionsMenuView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    int[] arrayOfInt1 = R.styleable.MusicMenuView;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt1);
    int i = localTypedArray.getDimensionPixelSize(0, 66);
    this.mRowHeight = i;
    int j = localTypedArray.getInt(1, 5);
    this.mMaxItemsPerRow = j;
    int k = localTypedArray.getInt(2, 1);
    this.mMaxRows = k;
    Resources localResources = paramContext.getResources();
    Drawable localDrawable1 = localResources.getDrawable(2130837677);
    this.mHorizontalDivider = localDrawable1;
    ArrayList localArrayList1 = new ArrayList();
    this.mHorizontalDividerRects = localArrayList1;
    Drawable localDrawable2 = localResources.getDrawable(2130837678);
    this.mVerticalDivider = localDrawable2;
    ArrayList localArrayList2 = new ArrayList();
    this.mVerticalDividerRects = localArrayList2;
    if (this.mHorizontalDivider != null)
    {
      int m = this.mHorizontalDivider.getIntrinsicHeight();
      this.mHorizontalDividerHeight = m;
      if (this.mHorizontalDividerHeight == -1)
        this.mHorizontalDividerHeight = 1;
    }
    if (this.mVerticalDivider != null)
    {
      int n = this.mVerticalDivider.getIntrinsicWidth();
      this.mVerticalDividerWidth = n;
      if (this.mVerticalDividerWidth == -1)
        this.mVerticalDividerWidth = 1;
    }
    int[] arrayOfInt2 = new int[this.mMaxRows];
    this.mLayout = arrayOfInt2;
    setWillNotDraw(false);
    setFocusableInTouchMode(true);
    setDescendantFocusability(262144);
    ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, -1);
    setLayoutParams(localLayoutParams);
  }

  private void addItemView(MusicOptionsMenuItemView paramMusicOptionsMenuItemView)
  {
    if (paramMusicOptionsMenuItemView.getParent() != null)
    {
      String str1 = "The itemView already has a parent, need to remove this parent. " + paramMusicOptionsMenuItemView;
      int i = Log.v("MusicOptionsMenuView", str1);
      if ((paramMusicOptionsMenuItemView.getParent() instanceof ViewGroup))
        ((ViewGroup)paramMusicOptionsMenuItemView.getParent()).removeView(paramMusicOptionsMenuItemView);
    }
    else
    {
      LayoutParams localLayoutParams = paramMusicOptionsMenuItemView.getTextAppropriateLayoutParams();
      addView(paramMusicOptionsMenuItemView, localLayoutParams);
      return;
    }
    String str2 = "Failed to add an itemView since its parent is not a ViewGroup " + paramMusicOptionsMenuItemView;
    int j = Log.e("MusicOptionsMenuView", str2);
  }

  private void calculateItemFittingMetadata(int paramInt)
  {
    int i = this.mMaxItemsPerRow;
    int j = getChildCount();
    int k = 0;
    if (k >= j)
      return;
    LayoutParams localLayoutParams = (LayoutParams)getChildAt(k).getLayoutParams();
    localLayoutParams.maxNumItemsOnRow = 1;
    int m = i;
    while (true)
    {
      if (m > 0)
      {
        int n = localLayoutParams.desiredWidth;
        int i1 = paramInt / m;
        if (n < i1)
          localLayoutParams.maxNumItemsOnRow = m;
      }
      else
      {
        k += 1;
        break;
      }
      m += -1;
    }
  }

  private boolean doItemsFit()
  {
    boolean bool = true;
    int i = 0;
    int[] arrayOfInt = this.mLayout;
    int j = this.mLayoutNumRows;
    int k = 0;
    int m;
    if (k < j)
    {
      m = arrayOfInt[k];
      if (m == 1)
        i += 1;
    }
    while (true)
    {
      k += 1;
      break;
      int n = m;
      for (int i1 = i; n > 0; i1 = i)
      {
        i = i1 + 1;
        if (((LayoutParams)getChildAt(i1).getLayoutParams()).maxNumItemsOnRow < m)
        {
          bool = false;
          return bool;
        }
        n += -1;
      }
      i = i1;
    }
  }

  private void layoutItems(int paramInt)
  {
    int i = 0;
    int j = getChildCount();
    if (j == 0)
    {
      this.mLayoutNumRows = 0;
      return;
    }
    int k = this.mMaxItemsPerRow;
    int m = j / k;
    int n = this.mMaxItemsPerRow;
    if (j % n != 0)
      i = 1;
    int i1 = m + i;
    while (true)
    {
      int i2 = this.mMaxRows;
      if (i1 > i2)
        return;
      layoutItemsUsingGravity(i1, j);
      if (i1 >= j)
        return;
      if (doItemsFit())
        return;
      i1 += 1;
    }
  }

  private void layoutItemsUsingGravity(int paramInt1, int paramInt2)
  {
    int i = paramInt2 / paramInt1;
    int j = paramInt2 % paramInt1;
    int k = paramInt1 - j;
    int[] arrayOfInt = this.mLayout;
    int m = 0;
    while (m < paramInt1)
    {
      arrayOfInt[m] = i;
      if (m >= k)
      {
        int n = arrayOfInt[m] + 1;
        arrayOfInt[m] = n;
      }
      m += 1;
    }
    this.mLayoutNumRows = paramInt1;
  }

  private void positionChildren(int paramInt1, int paramInt2)
  {
    if (this.mHorizontalDivider != null)
      this.mHorizontalDividerRects.clear();
    if (this.mVerticalDivider != null)
      this.mVerticalDividerRects.clear();
    int i = this.mLayoutNumRows;
    int j = i + -1;
    int[] arrayOfInt = this.mLayout;
    int k = 0;
    LayoutParams localLayoutParams = null;
    float f1 = 0.0F;
    int m = this.mHorizontalDividerHeight;
    int n = i + -1;
    int i1 = m * n;
    float f2 = paramInt2 - i1;
    float f3 = i;
    float f4 = f2 / f3;
    int i2 = 0;
    while (true)
    {
      if (i2 >= i)
        return;
      float f5 = 0.0F;
      int i3 = this.mVerticalDividerWidth;
      int i4 = arrayOfInt[i2] + -1;
      int i5 = i3 * i4;
      float f6 = paramInt1 - i5;
      float f7 = arrayOfInt[i2];
      float f8 = f6 / f7;
      int i6 = 0;
      while (true)
      {
        int i7 = arrayOfInt[i2];
        if (i6 >= i7)
          break;
        View localView = getChildAt(k);
        int i8 = View.MeasureSpec.makeMeasureSpec((int)f8, 1073741824);
        int i9 = View.MeasureSpec.makeMeasureSpec((int)f4, 1073741824);
        int i10 = i8;
        int i11 = i9;
        localView.measure(i10, i11);
        localLayoutParams = (LayoutParams)localView.getLayoutParams();
        int i12 = (int)f5;
        localLayoutParams.left = i12;
        int i13 = (int)(f5 + f8);
        localLayoutParams.right = i13;
        int i14 = (int)f1;
        localLayoutParams.top = i14;
        int i15 = (int)(f1 + f4);
        localLayoutParams.bottom = i15;
        float f9 = f5 + f8;
        k += 1;
        if (this.mVerticalDivider != null)
        {
          ArrayList localArrayList1 = this.mVerticalDividerRects;
          int i16 = (int)f9;
          int i17 = (int)f1;
          int i18 = (int)(this.mVerticalDividerWidth + f9);
          int i19 = (int)(f1 + f4);
          Rect localRect1 = new Rect(i16, i17, i18, i19);
          boolean bool1 = localArrayList1.add(localRect1);
        }
        float f10 = this.mVerticalDividerWidth;
        f5 = f9 + f10;
        i6 += 1;
      }
      if (localLayoutParams != null)
      {
        int i20 = paramInt1;
        localLayoutParams.right = i20;
      }
      f1 += f4;
      if ((this.mHorizontalDivider != null) && (i2 < j))
      {
        ArrayList localArrayList2 = this.mHorizontalDividerRects;
        Rect localRect2 = new android/graphics/Rect;
        int i21 = (int)f1;
        int i22 = (int)(this.mHorizontalDividerHeight + f1);
        Rect localRect3 = localRect2;
        int i23 = 0;
        int i24 = i21;
        int i25 = paramInt1;
        int i26 = i22;
        localRect3.<init>(i23, i24, i25, i26);
        boolean bool2 = localArrayList2.add(localRect2);
        float f11 = this.mHorizontalDividerHeight;
        float f12 = f1 + f11;
      }
      i2 += 1;
    }
  }

  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    return paramLayoutParams instanceof LayoutParams;
  }

  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    Context localContext = getContext();
    return new LayoutParams(localContext, paramAttributeSet);
  }

  public void onAttachedToWindow()
  {
    View localView = getRootView();
    if (localView == null)
      return;
    Drawable localDrawable = getContext().getResources().getDrawable(2130837803);
    localView.setBackgroundDrawable(localDrawable);
  }

  protected void onDraw(Canvas paramCanvas)
  {
    Drawable localDrawable1 = this.mHorizontalDivider;
    if (localDrawable1 != null)
    {
      localArrayList = this.mHorizontalDividerRects;
      i = localArrayList.size() + -1;
      while (i >= 0)
      {
        Rect localRect1 = (Rect)localArrayList.get(i);
        localDrawable1.setBounds(localRect1);
        localDrawable1.draw(paramCanvas);
        i += -1;
      }
    }
    Drawable localDrawable2 = this.mVerticalDivider;
    if (localDrawable2 == null)
      return;
    ArrayList localArrayList = this.mVerticalDividerRects;
    int i = localArrayList.size() + -1;
    while (true)
    {
      if (i < 0)
        return;
      Rect localRect2 = (Rect)localArrayList.get(i);
      localDrawable2.setBounds(localRect2);
      localDrawable2.draw(paramCanvas);
      i += -1;
    }
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getChildCount() + -1;
    while (true)
    {
      if (i < 0)
        return;
      View localView = getChildAt(i);
      LayoutParams localLayoutParams = (LayoutParams)localView.getLayoutParams();
      int j = localLayoutParams.left;
      int k = localLayoutParams.top;
      int m = localLayoutParams.right;
      int n = localLayoutParams.bottom;
      localView.layout(j, k, m, n);
      i += -1;
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (this.mHasStaleChildren)
    {
      this.mHasStaleChildren = false;
      updateChildren(false);
    }
    int i = resolveSize(2147483647, paramInt1);
    calculateItemFittingMetadata(i);
    layoutItems(i);
    int j = this.mLayoutNumRows;
    int k = this.mRowHeight;
    int m = this.mHorizontalDividerHeight;
    int n = (k + m) * j;
    int i1 = this.mHorizontalDividerHeight;
    int i2 = resolveSize(n - i1, paramInt2);
    setMeasuredDimension(i, i2);
    if (j <= 0)
      return;
    int i3 = getMeasuredWidth();
    int i4 = getMeasuredHeight();
    positionChildren(i3, i4);
  }

  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    Parcelable localParcelable = localSavedState.getSuperState();
    super.onRestoreInstanceState(localParcelable);
    int i = localSavedState.focusedPosition;
    int j = getChildCount();
    if (i >= j)
      return;
    int k = localSavedState.focusedPosition;
    View localView = getChildAt(k);
    if (localView == null)
      return;
    boolean bool = localView.requestFocus();
  }

  protected Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable = super.onSaveInstanceState();
    View localView = getFocusedChild();
    int i = getChildCount() + -1;
    if (i >= 0)
      if (getChildAt(i) != localView);
    for (SavedState localSavedState = new SavedState(localParcelable, i); ; localSavedState = new SavedState(localParcelable, -1))
    {
      return localSavedState;
      i += -1;
      break;
    }
  }

  public void updateChildren(boolean paramBoolean)
  {
    removeAllViews();
    ArrayList localArrayList = this.mMenu.getVisibleItems();
    int i = localArrayList.size();
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      MusicOptionsMenuItemView localMusicOptionsMenuItemView = (MusicOptionsMenuItemView)((MusicOptionsMenuItem)localArrayList.get(j)).getItemView(this);
      addItemView(localMusicOptionsMenuItemView);
      j += 1;
    }
  }

  public static class LayoutParams extends ViewGroup.MarginLayoutParams
  {
    int bottom;
    int desiredWidth;
    int left;
    int maxNumItemsOnRow;
    int right;
    int top;

    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
    }

    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }
  }

  private static class SavedState extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public MusicOptionsMenuView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new MusicOptionsMenuView.SavedState(paramAnonymousParcel, null);
      }

      public MusicOptionsMenuView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new MusicOptionsMenuView.SavedState[paramAnonymousInt];
      }
    };
    int focusedPosition;

    private SavedState(Parcel paramParcel)
    {
      super();
      int i = paramParcel.readInt();
      this.focusedPosition = i;
    }

    public SavedState(Parcelable paramParcelable, int paramInt)
    {
      super();
      this.focusedPosition = paramInt;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      int i = this.focusedPosition;
      paramParcel.writeInt(i);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.menu.MusicOptionsMenuView
 * JD-Core Version:    0.6.2
 */