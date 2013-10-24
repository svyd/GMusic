package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.layout;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.internal.view.menu.ActionMenuPresenter;
import android.support.v7.internal.view.menu.ActionMenuView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActionBarContextView extends AbsActionBarView
{
  private View mClose;
  private View mCustomView;
  private Drawable mSplitBackground;
  private CharSequence mSubtitle;
  private int mSubtitleStyleRes;
  private TextView mSubtitleView;
  private CharSequence mTitle;
  private LinearLayout mTitleLayout;
  private boolean mTitleOptional;
  private int mTitleStyleRes;
  private TextView mTitleView;

  public ActionBarContextView(Context paramContext)
  {
    this(paramContext, null);
  }

  public ActionBarContextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, i);
  }

  public ActionBarContextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    int[] arrayOfInt = R.styleable.ActionMode;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt, paramInt, 0);
    Drawable localDrawable1 = localTypedArray.getDrawable(3);
    setBackgroundDrawable(localDrawable1);
    int i = localTypedArray.getResourceId(1, 0);
    this.mTitleStyleRes = i;
    int j = localTypedArray.getResourceId(2, 0);
    this.mSubtitleStyleRes = j;
    int k = localTypedArray.getLayoutDimension(0, 0);
    this.mContentHeight = k;
    Drawable localDrawable2 = localTypedArray.getDrawable(4);
    this.mSplitBackground = localDrawable2;
    localTypedArray.recycle();
  }

  private void initTitle()
  {
    int i = 8;
    if (this.mTitleLayout == null)
    {
      LayoutInflater localLayoutInflater = LayoutInflater.from(getContext());
      int j = R.layout.abc_action_bar_title_item;
      View localView = localLayoutInflater.inflate(j, this);
      int k = getChildCount() + -1;
      LinearLayout localLinearLayout1 = (LinearLayout)getChildAt(k);
      this.mTitleLayout = localLinearLayout1;
      LinearLayout localLinearLayout2 = this.mTitleLayout;
      int m = R.id.action_bar_title;
      TextView localTextView1 = (TextView)localLinearLayout2.findViewById(m);
      this.mTitleView = localTextView1;
      LinearLayout localLinearLayout3 = this.mTitleLayout;
      int n = R.id.action_bar_subtitle;
      TextView localTextView2 = (TextView)localLinearLayout3.findViewById(n);
      this.mSubtitleView = localTextView2;
      if (this.mTitleStyleRes != 0)
      {
        TextView localTextView3 = this.mTitleView;
        Context localContext1 = getContext();
        int i1 = this.mTitleStyleRes;
        localTextView3.setTextAppearance(localContext1, i1);
      }
      if (this.mSubtitleStyleRes != 0)
      {
        TextView localTextView4 = this.mSubtitleView;
        Context localContext2 = getContext();
        int i2 = this.mSubtitleStyleRes;
        localTextView4.setTextAppearance(localContext2, i2);
      }
    }
    TextView localTextView5 = this.mTitleView;
    CharSequence localCharSequence1 = this.mTitle;
    localTextView5.setText(localCharSequence1);
    TextView localTextView6 = this.mSubtitleView;
    CharSequence localCharSequence2 = this.mSubtitle;
    localTextView6.setText(localCharSequence2);
    int i3;
    int i4;
    label246: TextView localTextView7;
    if (!TextUtils.isEmpty(this.mTitle))
    {
      i3 = 1;
      if (TextUtils.isEmpty(this.mSubtitle))
        break label321;
      i4 = 1;
      localTextView7 = this.mSubtitleView;
      if (i4 == 0)
        break label327;
    }
    label321: label327: for (int i5 = 0; ; i5 = 8)
    {
      localTextView7.setVisibility(i5);
      LinearLayout localLinearLayout4 = this.mTitleLayout;
      if ((i3 != 0) || (i4 != 0))
        i = 0;
      localLinearLayout4.setVisibility(i);
      if (this.mTitleLayout.getParent() != null)
        return;
      LinearLayout localLinearLayout5 = this.mTitleLayout;
      addView(localLinearLayout5);
      return;
      i3 = 0;
      break;
      i4 = 0;
      break label246;
    }
  }

  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new ViewGroup.MarginLayoutParams(-1, -1);
  }

  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    Context localContext = getContext();
    return new ViewGroup.MarginLayoutParams(localContext, paramAttributeSet);
  }

  public boolean hideOverflowMenu()
  {
    if (this.mActionMenuPresenter != null);
    for (boolean bool = this.mActionMenuPresenter.hideOverflowMenu(); ; bool = false)
      return bool;
  }

  public boolean isOverflowMenuShowing()
  {
    if (this.mActionMenuPresenter != null);
    for (boolean bool = this.mActionMenuPresenter.isOverflowMenuShowing(); ; bool = false)
      return bool;
  }

  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (this.mActionMenuPresenter == null)
      return;
    boolean bool1 = this.mActionMenuPresenter.hideOverflowMenu();
    boolean bool2 = this.mActionMenuPresenter.hideSubMenus();
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getPaddingLeft();
    int j = getPaddingTop();
    int k = paramInt4 - paramInt2;
    int m = getPaddingTop();
    int n = k - m;
    int i1 = getPaddingBottom();
    int i2 = n - i1;
    if ((this.mClose != null) && (this.mClose.getVisibility() != 8))
    {
      ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
      int i3 = localMarginLayoutParams.leftMargin;
      int i4 = i + i3;
      View localView1 = this.mClose;
      int i5 = positionChild(localView1, i4, j, i2);
      int i6 = i4 + i5;
      int i7 = localMarginLayoutParams.rightMargin;
      i = i6 + i7;
    }
    if ((this.mTitleLayout != null) && (this.mCustomView == null) && (this.mTitleLayout.getVisibility() != 8))
    {
      LinearLayout localLinearLayout = this.mTitleLayout;
      int i8 = positionChild(localLinearLayout, i, j, i2);
      i += i8;
    }
    if (this.mCustomView != null)
    {
      View localView2 = this.mCustomView;
      int i9 = positionChild(localView2, i, j, i2);
      int i10 = i + i9;
    }
    int i11 = paramInt3 - paramInt1;
    int i12 = getPaddingRight();
    int i13 = i11 - i12;
    if (this.mMenuView == null)
      return;
    ActionMenuView localActionMenuView = this.mMenuView;
    int i14 = positionChildInverse(localActionMenuView, i13, j, i2);
    int i15 = i13 - i14;
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = 1073741824;
    if (i != j)
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      String str1 = getClass().getSimpleName();
      String str2 = str1 + " can only be used " + "with android:layout_width=\"FILL_PARENT\" (or fill_parent)";
      throw new IllegalStateException(str2);
    }
    if (View.MeasureSpec.getMode(paramInt2) == 0)
    {
      StringBuilder localStringBuilder2 = new StringBuilder();
      String str3 = getClass().getSimpleName();
      String str4 = str3 + " can only be used " + "with android:layout_height=\"wrap_content\"";
      throw new IllegalStateException(str4);
    }
    int k = View.MeasureSpec.getSize(paramInt1);
    int m;
    int i2;
    int i6;
    int i7;
    int i9;
    int i19;
    label412: int i20;
    label438: label453: ViewGroup.LayoutParams localLayoutParams;
    int i24;
    label492: int i25;
    label512: int i28;
    if (this.mContentHeight > 0)
    {
      m = this.mContentHeight;
      int n = getPaddingTop();
      int i1 = getPaddingBottom();
      i2 = n + i1;
      int i3 = getPaddingLeft();
      int i4 = k - i3;
      int i5 = getPaddingRight();
      i6 = i4 - i5;
      i7 = m - i2;
      int i8 = -2147483648;
      i9 = View.MeasureSpec.makeMeasureSpec(i7, i8);
      if (this.mClose != null)
      {
        View localView1 = this.mClose;
        ActionBarContextView localActionBarContextView1 = this;
        View localView2 = localView1;
        int i10 = 0;
        int i11 = localActionBarContextView1.measureChildView(localView2, i6, i9, i10);
        ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)this.mClose.getLayoutParams();
        int i12 = localMarginLayoutParams.leftMargin;
        int i13 = localMarginLayoutParams.rightMargin;
        int i14 = i12 + i13;
        i6 = i11 - i14;
      }
      if (this.mMenuView != null)
      {
        ViewParent localViewParent = this.mMenuView.getParent();
        ActionBarContextView localActionBarContextView2 = this;
        if (localViewParent == localActionBarContextView2)
        {
          ActionMenuView localActionMenuView1 = this.mMenuView;
          ActionBarContextView localActionBarContextView3 = this;
          ActionMenuView localActionMenuView2 = localActionMenuView1;
          int i15 = 0;
          i6 = localActionBarContextView3.measureChildView(localActionMenuView2, i6, i9, i15);
        }
      }
      if ((this.mTitleLayout != null) && (this.mCustomView == null))
      {
        if (!this.mTitleOptional)
          break label678;
        int i16 = View.MeasureSpec.makeMeasureSpec(0, 0);
        LinearLayout localLinearLayout1 = this.mTitleLayout;
        int i17 = i16;
        localLinearLayout1.measure(i17, i9);
        int i18 = this.mTitleLayout.getMeasuredWidth();
        if (i18 > i6)
          break label665;
        i19 = 1;
        if (i19 != 0)
          i6 -= i18;
        LinearLayout localLinearLayout2 = this.mTitleLayout;
        if (i19 == 0)
          break label671;
        i20 = 0;
        LinearLayout localLinearLayout3 = localLinearLayout2;
        int i21 = i20;
        localLinearLayout3.setVisibility(i21);
      }
      if (this.mCustomView != null)
      {
        localLayoutParams = this.mCustomView.getLayoutParams();
        int i22 = localLayoutParams.width;
        int i23 = 65534;
        if (i22 == i23)
          break label712;
        i24 = 1073741824;
        if (localLayoutParams.width < 0)
          break label720;
        i25 = Math.min(localLayoutParams.width, i6);
        int i26 = localLayoutParams.height;
        int i27 = 65534;
        if (i26 == i27)
          break label727;
        i28 = 1073741824;
        label535: if (localLayoutParams.height < 0)
          break label735;
      }
    }
    int i32;
    label665: label671: label678: label712: label720: label727: label735: for (int i29 = Math.min(localLayoutParams.height, i7); ; i29 = i7)
    {
      View localView3 = this.mCustomView;
      int i30 = View.MeasureSpec.makeMeasureSpec(i25, i24);
      int i31 = View.MeasureSpec.makeMeasureSpec(i29, i28);
      localView3.measure(i30, i31);
      if (this.mContentHeight > 0)
        break label759;
      i32 = 0;
      int i33 = getChildCount();
      int i34 = 0;
      while (i34 < i33)
      {
        int i35 = getChildAt(i34).getMeasuredHeight() + i2;
        int i36 = i35;
        int i37 = i32;
        if (i36 > i37)
          i32 = i35;
        i34 += 1;
      }
      m = View.MeasureSpec.getSize(paramInt2);
      break;
      i19 = 0;
      break label412;
      i20 = 8;
      break label438;
      LinearLayout localLinearLayout4 = this.mTitleLayout;
      ActionBarContextView localActionBarContextView4 = this;
      LinearLayout localLinearLayout5 = localLinearLayout4;
      int i38 = 0;
      i6 = localActionBarContextView4.measureChildView(localLinearLayout5, i6, i9, i38);
      break label453;
      i24 = -2147483648;
      break label492;
      i25 = i6;
      break label512;
      i28 = -2147483648;
      break label535;
    }
    ActionBarContextView localActionBarContextView5 = this;
    int i39 = i32;
    localActionBarContextView5.setMeasuredDimension(k, i39);
    return;
    label759: setMeasuredDimension(k, m);
  }

  public void setContentHeight(int paramInt)
  {
    this.mContentHeight = paramInt;
  }

  public void setCustomView(View paramView)
  {
    if (this.mCustomView != null)
    {
      View localView = this.mCustomView;
      removeView(localView);
    }
    this.mCustomView = paramView;
    if (this.mTitleLayout != null)
    {
      LinearLayout localLinearLayout = this.mTitleLayout;
      removeView(localLinearLayout);
      this.mTitleLayout = null;
    }
    if (paramView != null)
      addView(paramView);
    requestLayout();
  }

  public void setSplitActionBar(boolean paramBoolean)
  {
    if (this.mSplitActionBar != paramBoolean)
      return;
    ViewGroup.LayoutParams localLayoutParams;
    ViewGroup localViewGroup;
    if (this.mActionMenuPresenter != null)
    {
      localLayoutParams = new ViewGroup.LayoutParams(-1, -1);
      if (paramBoolean)
        break label106;
      ActionMenuView localActionMenuView1 = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
      this.mMenuView = localActionMenuView1;
      this.mMenuView.setBackgroundDrawable(null);
      localViewGroup = (ViewGroup)this.mMenuView.getParent();
      if (localViewGroup != null)
      {
        ActionMenuView localActionMenuView2 = this.mMenuView;
        localViewGroup.removeView(localActionMenuView2);
      }
      ActionMenuView localActionMenuView3 = this.mMenuView;
      addView(localActionMenuView3, localLayoutParams);
    }
    while (true)
    {
      super.setSplitActionBar(paramBoolean);
      return;
      label106: ActionMenuPresenter localActionMenuPresenter = this.mActionMenuPresenter;
      int i = getContext().getResources().getDisplayMetrics().widthPixels;
      localActionMenuPresenter.setWidthLimit(i, true);
      this.mActionMenuPresenter.setItemLimit(2147483647);
      localLayoutParams.width = -1;
      int j = this.mContentHeight;
      localLayoutParams.height = j;
      ActionMenuView localActionMenuView4 = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
      this.mMenuView = localActionMenuView4;
      ActionMenuView localActionMenuView5 = this.mMenuView;
      Drawable localDrawable = this.mSplitBackground;
      localActionMenuView5.setBackgroundDrawable(localDrawable);
      localViewGroup = (ViewGroup)this.mMenuView.getParent();
      if (localViewGroup != null)
      {
        ActionMenuView localActionMenuView6 = this.mMenuView;
        localViewGroup.removeView(localActionMenuView6);
      }
      ActionBarContainer localActionBarContainer = this.mSplitView;
      ActionMenuView localActionMenuView7 = this.mMenuView;
      localActionBarContainer.addView(localActionMenuView7, localLayoutParams);
    }
  }

  public void setSubtitle(CharSequence paramCharSequence)
  {
    this.mSubtitle = paramCharSequence;
    initTitle();
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    this.mTitle = paramCharSequence;
    initTitle();
  }

  public void setTitleOptional(boolean paramBoolean)
  {
    boolean bool = this.mTitleOptional;
    if (paramBoolean != bool)
      requestLayout();
    this.mTitleOptional = paramBoolean;
  }

  public boolean showOverflowMenu()
  {
    if (this.mActionMenuPresenter != null);
    for (boolean bool = this.mActionMenuPresenter.showOverflowMenu(); ; bool = false)
      return bool;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.ActionBarContextView
 * JD-Core Version:    0.6.2
 */