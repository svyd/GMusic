package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.layout;
import android.support.v7.internal.view.ActionBarPolicy;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ScrollingTabContainerView extends HorizontalScrollView
  implements AdapterViewICS.OnItemClickListener
{
  private boolean mAllowCollapse;
  private int mContentHeight;
  private final LayoutInflater mInflater;
  int mMaxTabWidth;
  private int mSelectedTabIndex;
  int mStackedTabMaxWidth;
  private TabClickListener mTabClickListener;
  private LinearLayout mTabLayout;
  Runnable mTabSelector;
  private SpinnerICS mTabSpinner;

  public ScrollingTabContainerView(Context paramContext)
  {
    super(paramContext);
    LayoutInflater localLayoutInflater1 = LayoutInflater.from(paramContext);
    this.mInflater = localLayoutInflater1;
    setHorizontalScrollBarEnabled(false);
    ActionBarPolicy localActionBarPolicy = ActionBarPolicy.get(paramContext);
    int i = localActionBarPolicy.getTabContainerHeight();
    setContentHeight(i);
    int j = localActionBarPolicy.getStackedTabMaxWidth();
    this.mStackedTabMaxWidth = j;
    LayoutInflater localLayoutInflater2 = this.mInflater;
    int k = R.layout.abc_action_bar_tabbar;
    LinearLayout localLinearLayout1 = (LinearLayout)localLayoutInflater2.inflate(k, this, false);
    this.mTabLayout = localLinearLayout1;
    LinearLayout localLinearLayout2 = this.mTabLayout;
    ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, -1);
    addView(localLinearLayout2, localLayoutParams);
  }

  private SpinnerICS createSpinner()
  {
    Context localContext = getContext();
    int i = R.attr.actionDropDownStyle;
    SpinnerICS localSpinnerICS = new SpinnerICS(localContext, null, i);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -1);
    localSpinnerICS.setLayoutParams(localLayoutParams);
    localSpinnerICS.setOnItemClickListenerInt(this);
    return localSpinnerICS;
  }

  private TabView createTabView(ActionBar.Tab paramTab, boolean paramBoolean)
  {
    LayoutInflater localLayoutInflater = this.mInflater;
    int i = R.layout.abc_action_bar_tab;
    LinearLayout localLinearLayout = this.mTabLayout;
    TabView localTabView = (TabView)localLayoutInflater.inflate(i, localLinearLayout, false);
    localTabView.attach(this, paramTab, paramBoolean);
    if (paramBoolean)
    {
      localTabView.setBackgroundDrawable(null);
      int j = this.mContentHeight;
      AbsListView.LayoutParams localLayoutParams = new AbsListView.LayoutParams(-1, j);
      localTabView.setLayoutParams(localLayoutParams);
    }
    while (true)
    {
      return localTabView;
      localTabView.setFocusable(true);
      if (this.mTabClickListener == null)
      {
        TabClickListener localTabClickListener1 = new TabClickListener(null);
        this.mTabClickListener = localTabClickListener1;
      }
      TabClickListener localTabClickListener2 = this.mTabClickListener;
      localTabView.setOnClickListener(localTabClickListener2);
    }
  }

  private boolean isCollapsed()
  {
    if ((this.mTabSpinner != null) && (this.mTabSpinner.getParent() == this));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private void performCollapse()
  {
    if (isCollapsed())
      return;
    if (this.mTabSpinner == null)
    {
      SpinnerICS localSpinnerICS1 = createSpinner();
      this.mTabSpinner = localSpinnerICS1;
    }
    LinearLayout localLinearLayout = this.mTabLayout;
    removeView(localLinearLayout);
    SpinnerICS localSpinnerICS2 = this.mTabSpinner;
    ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, -1);
    addView(localSpinnerICS2, localLayoutParams);
    if (this.mTabSpinner.getAdapter() == null)
    {
      SpinnerICS localSpinnerICS3 = this.mTabSpinner;
      TabAdapter localTabAdapter = new TabAdapter(null);
      localSpinnerICS3.setAdapter(localTabAdapter);
    }
    if (this.mTabSelector != null)
    {
      Runnable localRunnable = this.mTabSelector;
      boolean bool = removeCallbacks(localRunnable);
      this.mTabSelector = null;
    }
    SpinnerICS localSpinnerICS4 = this.mTabSpinner;
    int i = this.mSelectedTabIndex;
    localSpinnerICS4.setSelection(i);
  }

  private boolean performExpand()
  {
    if (!isCollapsed());
    while (true)
    {
      return false;
      SpinnerICS localSpinnerICS = this.mTabSpinner;
      removeView(localSpinnerICS);
      LinearLayout localLinearLayout = this.mTabLayout;
      ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, -1);
      addView(localLinearLayout, localLayoutParams);
      int i = this.mTabSpinner.getSelectedItemPosition();
      setTabSelected(i);
    }
  }

  public void animateToTab(int paramInt)
  {
    final View localView = this.mTabLayout.getChildAt(paramInt);
    if (this.mTabSelector != null)
    {
      Runnable localRunnable1 = this.mTabSelector;
      boolean bool1 = removeCallbacks(localRunnable1);
    }
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        int i = localView.getLeft();
        int j = ScrollingTabContainerView.this.getWidth();
        int k = localView.getWidth();
        int m = (j - k) / 2;
        int n = i - m;
        ScrollingTabContainerView.this.smoothScrollTo(n, 0);
        ScrollingTabContainerView.this.mTabSelector = null;
      }
    };
    this.mTabSelector = local1;
    Runnable localRunnable2 = this.mTabSelector;
    boolean bool2 = post(localRunnable2);
  }

  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    if (this.mTabSelector == null)
      return;
    Runnable localRunnable = this.mTabSelector;
    boolean bool = post(localRunnable);
  }

  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    ActionBarPolicy localActionBarPolicy = ActionBarPolicy.get(getContext());
    int i = localActionBarPolicy.getTabContainerHeight();
    setContentHeight(i);
    int j = localActionBarPolicy.getStackedTabMaxWidth();
    this.mStackedTabMaxWidth = j;
  }

  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (this.mTabSelector == null)
      return;
    Runnable localRunnable = this.mTabSelector;
    boolean bool = removeCallbacks(localRunnable);
  }

  public void onItemClick(AdapterViewICS<?> paramAdapterViewICS, View paramView, int paramInt, long paramLong)
  {
    ((TabView)paramView).getTab().select();
  }

  public void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    boolean bool1;
    label70: label97: int i2;
    int i3;
    if (i == 1073741824)
    {
      bool1 = true;
      setFillViewport(bool1);
      int j = this.mTabLayout.getChildCount();
      if ((j <= 1) || ((i != 1073741824) && (i != -2147483648)))
        break label233;
      if (j <= 2)
        break label216;
      int k = (int)(View.MeasureSpec.getSize(paramInt1) * 0.4F);
      this.mMaxTabWidth = k;
      int m = this.mMaxTabWidth;
      int n = this.mStackedTabMaxWidth;
      int i1 = Math.min(m, n);
      this.mMaxTabWidth = i1;
      i2 = View.MeasureSpec.makeMeasureSpec(this.mContentHeight, 1073741824);
      if ((bool1) || (!this.mAllowCollapse))
        break label242;
      i3 = 1;
      label123: if (i3 == 0)
        break label257;
      this.mTabLayout.measure(0, i2);
      int i4 = this.mTabLayout.getMeasuredWidth();
      int i5 = View.MeasureSpec.getSize(paramInt1);
      if (i4 <= i5)
        break label248;
      performCollapse();
    }
    while (true)
    {
      int i6 = getMeasuredWidth();
      super.onMeasure(paramInt1, i2);
      int i7 = getMeasuredWidth();
      if (!bool1)
        return;
      if (i6 != i7)
        return;
      int i8 = this.mSelectedTabIndex;
      setTabSelected(i8);
      return;
      bool1 = false;
      break;
      label216: int i9 = View.MeasureSpec.getSize(paramInt1) / 2;
      this.mMaxTabWidth = i9;
      break label70;
      label233: this.mMaxTabWidth = -1;
      break label97;
      label242: i3 = 0;
      break label123;
      label248: boolean bool2 = performExpand();
      continue;
      label257: boolean bool3 = performExpand();
    }
  }

  public void setAllowCollapse(boolean paramBoolean)
  {
    this.mAllowCollapse = paramBoolean;
  }

  public void setContentHeight(int paramInt)
  {
    this.mContentHeight = paramInt;
    requestLayout();
  }

  public void setTabSelected(int paramInt)
  {
    this.mSelectedTabIndex = paramInt;
    int i = this.mTabLayout.getChildCount();
    int j = 0;
    if (j < i)
    {
      View localView = this.mTabLayout.getChildAt(j);
      if (j != paramInt);
      for (boolean bool = true; ; bool = false)
      {
        localView.setSelected(bool);
        if (bool)
          animateToTab(paramInt);
        j += 1;
        break;
      }
    }
    if (this.mTabSpinner == null)
      return;
    if (paramInt < 0)
      return;
    this.mTabSpinner.setSelection(paramInt);
  }

  private class TabClickListener
    implements View.OnClickListener
  {
    private TabClickListener()
    {
    }

    public void onClick(View paramView)
    {
      ((ScrollingTabContainerView.TabView)paramView).getTab().select();
      int i = ScrollingTabContainerView.this.mTabLayout.getChildCount();
      int j = 0;
      if (j >= i)
        return;
      View localView = ScrollingTabContainerView.this.mTabLayout.getChildAt(j);
      if (localView == paramView);
      for (boolean bool = true; ; bool = false)
      {
        localView.setSelected(bool);
        j += 1;
        break;
      }
    }
  }

  private class TabAdapter extends BaseAdapter
  {
    private TabAdapter()
    {
    }

    public int getCount()
    {
      return ScrollingTabContainerView.this.mTabLayout.getChildCount();
    }

    public Object getItem(int paramInt)
    {
      return ((ScrollingTabContainerView.TabView)ScrollingTabContainerView.this.mTabLayout.getChildAt(paramInt)).getTab();
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        ScrollingTabContainerView localScrollingTabContainerView = ScrollingTabContainerView.this;
        ActionBar.Tab localTab1 = (ActionBar.Tab)getItem(paramInt);
        paramView = localScrollingTabContainerView.createTabView(localTab1, true);
      }
      while (true)
      {
        return paramView;
        ScrollingTabContainerView.TabView localTabView = (ScrollingTabContainerView.TabView)paramView;
        ActionBar.Tab localTab2 = (ActionBar.Tab)getItem(paramInt);
        localTabView.bindTab(localTab2);
      }
    }
  }

  public static class TabView extends LinearLayout
  {
    private View mCustomView;
    private ImageView mIconView;
    private ScrollingTabContainerView mParent;
    private ActionBar.Tab mTab;
    private TextView mTextView;

    public TabView(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }

    void attach(ScrollingTabContainerView paramScrollingTabContainerView, ActionBar.Tab paramTab, boolean paramBoolean)
    {
      this.mParent = paramScrollingTabContainerView;
      this.mTab = paramTab;
      if (paramBoolean)
        setGravity(19);
      update();
    }

    public void bindTab(ActionBar.Tab paramTab)
    {
      this.mTab = paramTab;
      update();
    }

    public ActionBar.Tab getTab()
    {
      return this.mTab;
    }

    public void onMeasure(int paramInt1, int paramInt2)
    {
      super.onMeasure(paramInt1, paramInt2);
      if (this.mParent != null);
      for (int i = this.mParent.mMaxTabWidth; ; i = 0)
      {
        if (i <= 0)
          return;
        if (getMeasuredWidth() <= i)
          return;
        int j = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
        super.onMeasure(j, paramInt2);
        return;
      }
    }

    public void update()
    {
      ActionBar.Tab localTab = this.mTab;
      View localView1 = localTab.getCustomView();
      if (localView1 != null)
      {
        ViewParent localViewParent = localView1.getParent();
        if (localViewParent != this)
        {
          if (localViewParent != null)
            ((ViewGroup)localViewParent).removeView(localView1);
          addView(localView1);
        }
        this.mCustomView = localView1;
        if (this.mTextView != null)
          this.mTextView.setVisibility(8);
        if (this.mIconView == null)
          return;
        this.mIconView.setVisibility(8);
        this.mIconView.setImageDrawable(null);
        return;
      }
      if (this.mCustomView != null)
      {
        View localView2 = this.mCustomView;
        removeView(localView2);
        this.mCustomView = null;
      }
      Drawable localDrawable = localTab.getIcon();
      CharSequence localCharSequence1 = localTab.getText();
      if (localDrawable != null)
      {
        if (this.mIconView == null)
        {
          Context localContext1 = getContext();
          ImageView localImageView1 = new ImageView(localContext1);
          LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(-1, -1);
          localLayoutParams1.gravity = 16;
          localImageView1.setLayoutParams(localLayoutParams1);
          addView(localImageView1, 0);
          this.mIconView = localImageView1;
        }
        this.mIconView.setImageDrawable(localDrawable);
        this.mIconView.setVisibility(0);
        if (localCharSequence1 == null)
          break label370;
        if (this.mTextView == null)
        {
          Context localContext2 = getContext();
          int i = R.attr.actionBarTabTextStyle;
          CompatTextView localCompatTextView = new CompatTextView(localContext2, null, i);
          TextUtils.TruncateAt localTruncateAt = TextUtils.TruncateAt.END;
          localCompatTextView.setEllipsize(localTruncateAt);
          LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(-1, -1);
          localLayoutParams2.gravity = 16;
          localCompatTextView.setLayoutParams(localLayoutParams2);
          addView(localCompatTextView);
          this.mTextView = localCompatTextView;
        }
        this.mTextView.setText(localCharSequence1);
        this.mTextView.setVisibility(0);
      }
      while (true)
      {
        if (this.mIconView == null)
          return;
        ImageView localImageView2 = this.mIconView;
        CharSequence localCharSequence2 = localTab.getContentDescription();
        localImageView2.setContentDescription(localCharSequence2);
        return;
        if (this.mIconView == null)
          break;
        this.mIconView.setVisibility(8);
        this.mIconView.setImageDrawable(null);
        break;
        label370: if (this.mTextView != null)
        {
          this.mTextView.setVisibility(8);
          this.mTextView.setText(null);
        }
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.ScrollingTabContainerView
 * JD-Core Version:    0.6.2
 */