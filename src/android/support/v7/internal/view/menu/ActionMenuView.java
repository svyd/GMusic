package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.internal.widget.LinearLayoutICS;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout.LayoutParams;

public class ActionMenuView extends LinearLayoutICS
  implements MenuBuilder.ItemInvoker, MenuView
{
  private boolean mFormatItems;
  private int mFormatItemsWidth;
  private int mGeneratedItemPadding;
  private int mMaxItemHeight;
  private int mMeasuredExtraWidth;
  private MenuBuilder mMenu;
  private int mMinCellSize;
  private ActionMenuPresenter mPresenter;
  private boolean mReserveOverflow;

  public ActionMenuView(Context paramContext)
  {
    this(paramContext, null);
  }

  public ActionMenuView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setBaselineAligned(false);
    float f = paramContext.getResources().getDisplayMetrics().density;
    int i = (int)(56.0F * f);
    this.mMinCellSize = i;
    int j = (int)(4.0F * f);
    this.mGeneratedItemPadding = j;
    int[] arrayOfInt = R.styleable.ActionBar;
    int k = R.attr.actionBarStyle;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt, k, 0);
    int m = localTypedArray.getDimensionPixelSize(1, 0);
    this.mMaxItemHeight = m;
    localTypedArray.recycle();
  }

  static int measureChildForCells(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    LayoutParams localLayoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = View.MeasureSpec.getSize(paramInt3) - paramInt4;
    int j = View.MeasureSpec.getMode(paramInt3);
    int k = View.MeasureSpec.makeMeasureSpec(i, j);
    ActionMenuItemView localActionMenuItemView;
    int m;
    label62: int n;
    if ((paramView instanceof ActionMenuItemView))
    {
      localActionMenuItemView = (ActionMenuItemView)paramView;
      if ((localActionMenuItemView == null) || (!localActionMenuItemView.hasText()))
        break label194;
      m = 1;
      n = 0;
      if ((paramInt2 > 0) && ((m == 0) || (paramInt2 >= 2)))
      {
        int i1 = View.MeasureSpec.makeMeasureSpec(paramInt1 * paramInt2, -2147483648);
        paramView.measure(i1, k);
        int i2 = paramView.getMeasuredWidth();
        n = i2 / paramInt1;
        if (i2 % paramInt1 != 0)
          int i3 = n + 1;
        if ((m != 0) && (n < 2))
          n = 2;
      }
      if ((localLayoutParams.isOverflowButton) || (m == 0))
        break label200;
    }
    label194: label200: for (boolean bool = true; ; bool = false)
    {
      localLayoutParams.expandable = bool;
      localLayoutParams.cellsUsed = n;
      int i4 = View.MeasureSpec.makeMeasureSpec(n * paramInt1, 1073741824);
      paramView.measure(i4, k);
      return n;
      localActionMenuItemView = null;
      break;
      m = 0;
      break label62;
    }
  }

  private void onMeasureExactFormat(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt2);
    int j = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getSize(paramInt2);
    int m = getPaddingLeft();
    int n = getPaddingRight();
    int i1 = m + n;
    int i2 = getPaddingTop();
    int i3 = getPaddingBottom();
    int i4 = i2 + i3;
    int i5 = i;
    int i6 = 1073741824;
    if (i5 == i6);
    int i8;
    int i10;
    int i12;
    int i15;
    int i16;
    for (int i7 = View.MeasureSpec.makeMeasureSpec(k - i4, 1073741824); ; i7 = View.MeasureSpec.makeMeasureSpec(Math.min(i15, i16), -2147483648))
    {
      i8 = j - i1;
      int i9 = this.mMinCellSize;
      i10 = i8 / i9;
      int i11 = this.mMinCellSize;
      i12 = i8 % i11;
      if (i10 != 0)
        break;
      ActionMenuView localActionMenuView1 = this;
      int i13 = i8;
      int i14 = 0;
      localActionMenuView1.setMeasuredDimension(i13, i14);
      return;
      i15 = this.mMaxItemHeight;
      i16 = k - i4;
    }
    int i17 = this.mMinCellSize;
    int i18 = i12 / i10;
    int i19 = i17 + i18;
    int i20 = i10;
    int i21 = 0;
    int i22 = 0;
    int i23 = 0;
    int i24 = 0;
    int i25 = 0;
    long l1 = 0L;
    int i26 = getChildCount();
    int i27 = 0;
    while (i27 < i26)
    {
      ActionMenuView localActionMenuView2 = this;
      int i28 = i27;
      View localView1 = localActionMenuView2.getChildAt(i28);
      int i29 = localView1.getVisibility();
      i29 = 8;
      if (i29 == i29)
      {
        i27 += 1;
      }
      else
      {
        boolean bool1 = localView1 instanceof ActionMenuItemView;
        i24 += 1;
        if (bool1)
        {
          int i30 = this.mGeneratedItemPadding;
          int i31 = this.mGeneratedItemPadding;
          int i32 = i30;
          int i33 = 0;
          int i34 = i31;
          int i35 = 0;
          localView1.setPadding(i32, i33, i34, i35);
        }
        LayoutParams localLayoutParams1 = (LayoutParams)localView1.getLayoutParams();
        boolean bool2 = false;
        localLayoutParams1.expanded = bool2;
        int i36 = 0;
        localLayoutParams1.extraPixels = i36;
        int i37 = 0;
        localLayoutParams1.cellsUsed = i37;
        boolean bool3 = false;
        localLayoutParams1.expandable = bool3;
        int i38 = 0;
        localLayoutParams1.leftMargin = i38;
        int i39 = 0;
        localLayoutParams1.rightMargin = i39;
        boolean bool4;
        if ((bool1) && (((ActionMenuItemView)localView1).hasText()))
        {
          bool4 = true;
          label418: boolean bool5 = bool4;
          localLayoutParams1.preventEdgeOffset = bool5;
          if (!localLayoutParams1.isOverflowButton)
            break label561;
        }
        label561: for (int i40 = 1; ; i40 = i20)
        {
          int i41 = i7;
          int i42 = i4;
          int i43 = measureChildForCells(localView1, i19, i40, i41, i42);
          i22 = Math.max(i22, i43);
          if (localLayoutParams1.expandable)
            i23 += 1;
          if (localLayoutParams1.isOverflowButton)
            i25 = 1;
          i20 -= i43;
          int i44 = localView1.getMeasuredHeight();
          int i45 = i21;
          int i46 = i44;
          i21 = Math.max(i45, i46);
          i46 = 1;
          if (i43 != i46)
            break;
          long l2 = 1 << i27;
          l1 |= l2;
          break;
          bool4 = false;
          break label418;
        }
      }
    }
    int i48;
    int i49;
    int i50;
    long l3;
    int i51;
    label618: LayoutParams localLayoutParams2;
    if (i25 != 0)
    {
      i48 = i24;
      int i47 = 2;
      if (i48 == i47)
      {
        i48 = 1;
        i49 = 0;
        if ((i23 <= 0) || (i20 <= 0))
          break label763;
        i50 = 2147483647;
        l3 = 0L;
        i51 = 0;
        i27 = 0;
        if (i27 >= i26)
          break label749;
        ActionMenuView localActionMenuView3 = this;
        int i52 = i27;
        localLayoutParams2 = (LayoutParams)localActionMenuView3.getChildAt(i52).getLayoutParams();
        if (localLayoutParams2.expandable)
          break label670;
      }
    }
    label670: int i57;
    while (true)
    {
      int i53 = i27 + 1;
      break label618;
      i48 = 0;
      break;
      int i54 = localLayoutParams2.cellsUsed;
      int i55 = i50;
      if (i54 < i55)
      {
        i50 = localLayoutParams2.cellsUsed;
        l3 = 1 << i27;
        i51 = 1;
      }
      else
      {
        int i56 = localLayoutParams2.cellsUsed;
        i57 = i50;
        if (i56 != i57)
        {
          long l4 = 1 << i27;
          l3 |= l4;
          i51 += 1;
        }
      }
    }
    label749: l1 |= l3;
    label763: int i58;
    label785: int i67;
    if (i51 > i20)
    {
      if (i25 != 0)
        break label1211;
      int i59 = i24;
      i57 = 1;
      if (i59 != i57)
        break label1211;
      i58 = 1;
      if ((i20 <= 0) || (l1 == 0L))
        break label1411;
      int i60 = i24 + -1;
      if ((i20 >= i60) && (i58 == 0))
      {
        int i61 = i22;
        int i62 = 1;
        if (i61 <= i62)
          break label1411;
      }
      float f = Long.bitCount(l1);
      if (i58 == 0)
      {
        if ((0x1 & l1) != 0L)
        {
          ActionMenuView localActionMenuView4 = this;
          int i63 = 0;
          if (!((LayoutParams)localActionMenuView4.getChildAt(i63).getLayoutParams()).preventEdgeOffset)
            f -= 0.5F;
        }
        int i64 = i26 + -1;
        if ((1 << i64 & l1) != 0L)
        {
          int i65 = i26 + -1;
          ActionMenuView localActionMenuView5 = this;
          int i66 = i65;
          if (!((LayoutParams)localActionMenuView5.getChildAt(i66).getLayoutParams()).preventEdgeOffset)
            f -= 0.5F;
        }
      }
      if (f <= 0.0F)
        break label1217;
      i67 = (int)(i20 * i19 / f);
      label972: i27 = 0;
      label975: if (i27 >= i26)
        break label1408;
      if ((1 << i27 & l1) != 0L)
        break label1223;
    }
    label1211: label1217: label1223: LayoutParams localLayoutParams4;
    while (true)
    {
      int i68 = i27 + 1;
      break label975;
      int i69 = i50 + 1;
      i27 = 0;
      if (i27 < i26)
      {
        ActionMenuView localActionMenuView6 = this;
        int i70 = i27;
        View localView2 = localActionMenuView6.getChildAt(i70);
        LayoutParams localLayoutParams3 = (LayoutParams)localView2.getLayoutParams();
        int i71;
        if ((1 << i27 & l3) == 0L)
        {
          i71 = localLayoutParams3.cellsUsed;
          int i72 = i69;
          if (i71 != i72)
          {
            long l5 = 1 << i27;
            l1 |= l5;
          }
        }
        while (true)
        {
          int i73 = i27 + 1;
          break;
          if ((i48 != 0) && (localLayoutParams3.preventEdgeOffset))
          {
            i71 = 1;
            if (i20 == i71)
            {
              int i74 = this.mGeneratedItemPadding + i19;
              int i75 = this.mGeneratedItemPadding;
              int i76 = i74;
              int i77 = 0;
              int i78 = i75;
              int i79 = 0;
              localView2.setPadding(i76, i77, i78, i79);
            }
          }
          int i80 = localLayoutParams3.cellsUsed + 1;
          localLayoutParams3.cellsUsed = i80;
          boolean bool6 = true;
          localLayoutParams3.expanded = bool6;
          i20 += -1;
        }
      }
      i49 = 1;
      break;
      i58 = 0;
      break label785;
      i67 = 0;
      break label972;
      ActionMenuView localActionMenuView7 = this;
      int i81 = i27;
      View localView3 = localActionMenuView7.getChildAt(i81);
      localLayoutParams4 = (LayoutParams)localView3.getLayoutParams();
      if ((localView3 instanceof ActionMenuItemView))
      {
        localLayoutParams4.extraPixels = i67;
        boolean bool7 = true;
        localLayoutParams4.expanded = bool7;
        if ((i27 == 0) && (!localLayoutParams4.preventEdgeOffset))
        {
          int i82 = -i67 / 2;
          localLayoutParams4.leftMargin = i82;
        }
        i49 = 1;
      }
      else if (localLayoutParams4.isOverflowButton)
      {
        localLayoutParams4.extraPixels = i67;
        boolean bool8 = true;
        localLayoutParams4.expanded = bool8;
        int i83 = -i67 / 2;
        localLayoutParams4.rightMargin = i83;
        i49 = 1;
      }
      else
      {
        if (i27 != 0)
        {
          int i84 = i67 / 2;
          localLayoutParams4.leftMargin = i84;
        }
        int i85 = i26 + -1;
        int i86 = i27;
        int i87 = i85;
        if (i86 != i87)
        {
          int i88 = i67 / 2;
          localLayoutParams4.rightMargin = i88;
        }
      }
    }
    label1408: i20 = 0;
    label1411: if (i49 != 0)
    {
      i27 = 0;
      if (i27 < i26)
      {
        ActionMenuView localActionMenuView8 = this;
        int i89 = i27;
        View localView4 = localActionMenuView8.getChildAt(i89);
        localLayoutParams4 = (LayoutParams)localView4.getLayoutParams();
        if (!localLayoutParams4.expanded);
        while (true)
        {
          i27 += 1;
          break;
          int i90 = localLayoutParams4.cellsUsed * i19;
          int i91 = localLayoutParams4.extraPixels;
          i92 = i90 + i91;
          int i93 = 1073741824;
          int i94 = View.MeasureSpec.makeMeasureSpec(i92, i93);
          int i95 = i7;
          localView4.measure(i94, i95);
        }
      }
    }
    int i96 = i;
    int i92 = 1073741824;
    if (i96 != i92)
      k = i21;
    ActionMenuView localActionMenuView9 = this;
    int i97 = i8;
    int i98 = k;
    localActionMenuView9.setMeasuredDimension(i97, i98);
    int i99 = i20 * i19;
    this.mMeasuredExtraWidth = i99;
  }

  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if ((paramLayoutParams != null) && ((paramLayoutParams instanceof LayoutParams)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    return false;
  }

  protected LayoutParams generateDefaultLayoutParams()
  {
    LayoutParams localLayoutParams = new LayoutParams(-1, -1);
    localLayoutParams.gravity = 16;
    return localLayoutParams;
  }

  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    Context localContext = getContext();
    return new LayoutParams(localContext, paramAttributeSet);
  }

  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    LayoutParams localLayoutParams2;
    if ((paramLayoutParams instanceof LayoutParams))
    {
      LayoutParams localLayoutParams1 = (LayoutParams)paramLayoutParams;
      localLayoutParams2 = new LayoutParams(localLayoutParams1);
      if (localLayoutParams2.gravity <= 0)
        localLayoutParams2.gravity = 16;
    }
    while (true)
    {
      return localLayoutParams2;
      localLayoutParams2 = generateDefaultLayoutParams();
    }
  }

  public LayoutParams generateOverflowButtonLayoutParams()
  {
    LayoutParams localLayoutParams = generateDefaultLayoutParams();
    localLayoutParams.isOverflowButton = true;
    return localLayoutParams;
  }

  protected boolean hasSupportDividerBeforeChildAt(int paramInt)
  {
    int i = paramInt + -1;
    View localView1 = getChildAt(i);
    View localView2 = getChildAt(paramInt);
    boolean bool1 = false;
    int j = getChildCount();
    if ((paramInt < j) && ((localView1 instanceof ActionMenuChildView)))
    {
      boolean bool2 = ((ActionMenuChildView)localView1).needsDividerAfter();
      bool1 = false | bool2;
    }
    if ((paramInt > 0) && ((localView2 instanceof ActionMenuChildView)))
    {
      boolean bool3 = ((ActionMenuChildView)localView2).needsDividerBefore();
      bool1 |= bool3;
    }
    return bool1;
  }

  public void initialize(MenuBuilder paramMenuBuilder)
  {
    this.mMenu = paramMenuBuilder;
  }

  public boolean invokeItem(MenuItemImpl paramMenuItemImpl)
  {
    return this.mMenu.performItemAction(paramMenuItemImpl, 0);
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    if (Build.VERSION.SDK_INT >= 8)
      super.onConfigurationChanged(paramConfiguration);
    this.mPresenter.updateMenuView(false);
    if (this.mPresenter == null)
      return;
    if (!this.mPresenter.isOverflowMenuShowing())
      return;
    boolean bool1 = this.mPresenter.hideOverflowMenu();
    boolean bool2 = this.mPresenter.showOverflowMenu();
  }

  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    boolean bool = this.mPresenter.dismissPopupMenus();
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    if (!this.mFormatItems)
    {
      super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    }
    int i = getChildCount();
    int j = (paramInt2 + paramInt4) / 2;
    int k = getSupportDividerWidth();
    int m = 0;
    int n = 0;
    int i1 = paramInt3 - paramInt1;
    int i2 = getPaddingRight();
    int i3 = i1 - i2;
    int i4 = getPaddingLeft();
    int i5 = i3 - i4;
    int i6 = 0;
    int i7 = 0;
    if (i7 < i)
    {
      View localView1 = getChildAt(i7);
      int i8 = localView1.getVisibility();
      int i9 = 8;
      if (i8 == i9);
      while (true)
      {
        i7 += 1;
        break;
        LayoutParams localLayoutParams1 = (LayoutParams)localView1.getLayoutParams();
        if (localLayoutParams1.isOverflowButton)
        {
          int i10 = localView1.getMeasuredWidth();
          if (hasSupportDividerBeforeChildAt(i7))
            int i11 = i10 + k;
          int i12 = localView1.getMeasuredHeight();
          int i13 = getWidth();
          int i14 = getPaddingRight();
          int i15 = i13 - i14;
          int i16 = localLayoutParams1.rightMargin;
          int i17 = i15 - i16;
          int i18 = i17 - i10;
          int i19 = i12 / 2;
          int i20 = j - i19;
          int i21 = i20 + i12;
          View localView2 = localView1;
          int i22 = i20;
          int i23 = i17;
          localView2.layout(i18, i22, i23, i21);
          i5 -= i10;
          i6 = 1;
        }
        else
        {
          int i24 = localView1.getMeasuredWidth();
          int i25 = localLayoutParams1.leftMargin;
          int i26 = i24 + i25;
          int i27 = localLayoutParams1.rightMargin;
          int i28 = i26 + i27;
          m += i28;
          i5 -= i28;
          if (hasSupportDividerBeforeChildAt(i7))
            int i29 = m + k;
          n += 1;
        }
      }
    }
    int i30 = 1;
    if ((i == i30) && (i6 == 0))
    {
      ActionMenuView localActionMenuView = this;
      int i31 = 0;
      View localView3 = localActionMenuView.getChildAt(i31);
      int i32 = localView3.getMeasuredWidth();
      int i33 = localView3.getMeasuredHeight();
      int i34 = (paramInt3 - paramInt1) / 2;
      int i35 = i32 / 2;
      int i36 = i34 - i35;
      int i37 = i33 / 2;
      int i38 = j - i37;
      int i39 = i36 + i32;
      int i40 = i38 + i33;
      View localView4 = localView3;
      int i41 = i38;
      int i42 = i39;
      int i43 = i40;
      localView4.layout(i36, i41, i42, i43);
      return;
    }
    int i44;
    int i47;
    label497: int i50;
    int i51;
    label523: View localView5;
    LayoutParams localLayoutParams2;
    if (i6 != 0)
    {
      i44 = 0;
      int i45 = n - i44;
      int i46 = 0;
      if (i45 <= 0)
        break label590;
      i47 = i5 / i45;
      int i48 = i46;
      int i49 = i47;
      i50 = Math.max(i48, i49);
      i51 = getPaddingLeft();
      i7 = 0;
      if (i7 >= i)
        return;
      localView5 = getChildAt(i7);
      localLayoutParams2 = (LayoutParams)localView5.getLayoutParams();
      int i52 = localView5.getVisibility();
      int i53 = 8;
      if ((i52 != i53) && (!localLayoutParams2.isOverflowButton))
        break label596;
    }
    while (true)
    {
      i7 += 1;
      break label523;
      i44 = 1;
      break;
      label590: i47 = 0;
      break label497;
      label596: int i54 = localLayoutParams2.leftMargin;
      int i55 = i51 + i54;
      int i56 = localView5.getMeasuredWidth();
      int i57 = localView5.getMeasuredHeight();
      int i58 = i57 / 2;
      int i59 = j - i58;
      int i60 = i55 + i56;
      int i61 = i59 + i57;
      View localView6 = localView5;
      int i62 = i55;
      int i63 = i59;
      int i64 = i60;
      int i65 = i61;
      localView6.layout(i62, i63, i64, i65);
      int i66 = localLayoutParams2.rightMargin + i56 + i50;
      int i67 = i55 + i66;
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    boolean bool1 = this.mFormatItems;
    if (View.MeasureSpec.getMode(paramInt1) == 1073741824);
    for (boolean bool2 = true; ; bool2 = false)
    {
      this.mFormatItems = bool2;
      boolean bool3 = this.mFormatItems;
      if (bool1 != bool3)
        this.mFormatItemsWidth = 0;
      int i = View.MeasureSpec.getMode(paramInt1);
      if ((this.mFormatItems) && (this.mMenu != null))
      {
        int j = this.mFormatItemsWidth;
        if (i != j)
        {
          this.mFormatItemsWidth = i;
          this.mMenu.onItemsChanged(true);
        }
      }
      if (!this.mFormatItems)
        break;
      onMeasureExactFormat(paramInt1, paramInt2);
      return;
    }
    int k = getChildCount();
    int m = 0;
    while (m < k)
    {
      LayoutParams localLayoutParams = (LayoutParams)getChildAt(m).getLayoutParams();
      localLayoutParams.rightMargin = 0;
      localLayoutParams.leftMargin = 0;
      m += 1;
    }
    super.onMeasure(paramInt1, paramInt2);
  }

  public void setOverflowReserved(boolean paramBoolean)
  {
    this.mReserveOverflow = paramBoolean;
  }

  public void setPresenter(ActionMenuPresenter paramActionMenuPresenter)
  {
    this.mPresenter = paramActionMenuPresenter;
  }

  public static class LayoutParams extends LinearLayout.LayoutParams
  {

    @ViewDebug.ExportedProperty
    public int cellsUsed;

    @ViewDebug.ExportedProperty
    public boolean expandable;
    public boolean expanded;

    @ViewDebug.ExportedProperty
    public int extraPixels;

    @ViewDebug.ExportedProperty
    public boolean isOverflowButton;

    @ViewDebug.ExportedProperty
    public boolean preventEdgeOffset;

    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
      this.isOverflowButton = false;
    }

    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }

    public LayoutParams(LayoutParams paramLayoutParams)
    {
      super();
      boolean bool = paramLayoutParams.isOverflowButton;
      this.isOverflowButton = bool;
    }
  }

  public static abstract interface ActionMenuChildView
  {
    public abstract boolean needsDividerAfter();

    public abstract boolean needsDividerBefore();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.ActionMenuView
 * JD-Core Version:    0.6.2
 */