package android.support.v7.internal.widget;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.bool;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.layout;
import android.support.v7.appcompat.R.string;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.internal.view.menu.ActionMenuItem;
import android.support.v7.internal.view.menu.ActionMenuPresenter;
import android.support.v7.internal.view.menu.ActionMenuView;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.support.v7.internal.view.menu.MenuPresenter;
import android.support.v7.internal.view.menu.MenuPresenter.Callback;
import android.support.v7.internal.view.menu.SubMenuBuilder;
import android.support.v7.view.CollapsibleActionView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.Window.Callback;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import java.util.List;

public class ActionBarView extends AbsActionBarView
{
  private ActionBar.OnNavigationListener mCallback;
  private Context mContext;
  private ActionBarContextView mContextView;
  private View mCustomNavView;
  private int mDisplayOptions = -1;
  View mExpandedActionView;
  private final View.OnClickListener mExpandedActionViewUpListener;
  private HomeView mExpandedHomeLayout;
  private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
  private HomeView mHomeLayout;
  private Drawable mIcon;
  private boolean mIncludeTabs;
  private int mIndeterminateProgressStyle;
  private ProgressBarICS mIndeterminateProgressView;
  private boolean mIsCollapsable;
  private boolean mIsCollapsed;
  private int mItemPadding;
  private LinearLayout mListNavLayout;
  private Drawable mLogo;
  private ActionMenuItem mLogoNavItem;
  private final AdapterViewICS.OnItemSelectedListener mNavItemSelectedListener;
  private int mNavigationMode;
  private MenuBuilder mOptionsMenu;
  private int mProgressBarPadding;
  private int mProgressStyle;
  private ProgressBarICS mProgressView;
  private SpinnerICS mSpinner;
  private SpinnerAdapter mSpinnerAdapter;
  private CharSequence mSubtitle;
  private int mSubtitleStyleRes;
  private TextView mSubtitleView;
  private ScrollingTabContainerView mTabScrollView;
  private Runnable mTabSelector;
  private CharSequence mTitle;
  private LinearLayout mTitleLayout;
  private int mTitleStyleRes;
  private View mTitleUpView;
  private TextView mTitleView;
  private final View.OnClickListener mUpClickListener;
  private boolean mUserTitle;
  Window.Callback mWindowCallback;

  public ActionBarView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    ActionBarView localActionBarView1 = this;
    AdapterViewICS.OnItemSelectedListener local1 = new AdapterViewICS.OnItemSelectedListener()
    {
      public void onItemSelected(AdapterViewICS<?> paramAnonymousAdapterViewICS, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        if (ActionBarView.this.mCallback == null)
          return;
        boolean bool = ActionBarView.this.mCallback.onNavigationItemSelected(paramAnonymousInt, paramAnonymousLong);
      }

      public void onNothingSelected(AdapterViewICS<?> paramAnonymousAdapterViewICS)
      {
      }
    };
    this.mNavItemSelectedListener = local1;
    ActionBarView localActionBarView2 = this;
    View.OnClickListener local2 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MenuItemImpl localMenuItemImpl = ActionBarView.this.mExpandedMenuPresenter.mCurrentExpandedItem;
        if (localMenuItemImpl == null)
          return;
        boolean bool = localMenuItemImpl.collapseActionView();
      }
    };
    this.mExpandedActionViewUpListener = local2;
    ActionBarView localActionBarView3 = this;
    View.OnClickListener local3 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Window.Callback localCallback = ActionBarView.this.mWindowCallback;
        ActionMenuItem localActionMenuItem = ActionBarView.this.mLogoNavItem;
        boolean bool = localCallback.onMenuItemSelected(0, localActionMenuItem);
      }
    };
    this.mUpClickListener = local3;
    Context localContext1 = paramContext;
    this.mContext = localContext1;
    setBackgroundResource(0);
    int[] arrayOfInt = R.styleable.ActionBar;
    int i = R.attr.actionBarStyle;
    Context localContext2 = paramContext;
    AttributeSet localAttributeSet = paramAttributeSet;
    TypedArray localTypedArray = localContext2.obtainStyledAttributes(localAttributeSet, arrayOfInt, i, 0);
    ApplicationInfo localApplicationInfo = paramContext.getApplicationInfo();
    PackageManager localPackageManager = paramContext.getPackageManager();
    int j = localTypedArray.getInt(2, 0);
    this.mNavigationMode = j;
    CharSequence localCharSequence1 = localTypedArray.getText(0);
    this.mTitle = localCharSequence1;
    CharSequence localCharSequence2 = localTypedArray.getText(4);
    this.mSubtitle = localCharSequence2;
    Drawable localDrawable1 = localTypedArray.getDrawable(8);
    this.mLogo = localDrawable1;
    if ((this.mLogo != null) || (Build.VERSION.SDK_INT < 9) || ((paramContext instanceof Activity)));
    try
    {
      ComponentName localComponentName1 = ((Activity)paramContext).getComponentName();
      Drawable localDrawable2 = localPackageManager.getActivityLogo(localComponentName1);
      this.mLogo = localDrawable2;
      if (this.mLogo == null)
      {
        Drawable localDrawable3 = localApplicationInfo.loadLogo(localPackageManager);
        this.mLogo = localDrawable3;
      }
      Drawable localDrawable4 = localTypedArray.getDrawable(7);
      this.mIcon = localDrawable4;
      if (this.mIcon == null)
        if (!(paramContext instanceof Activity));
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException1)
    {
      try
      {
        ComponentName localComponentName2 = ((Activity)paramContext).getComponentName();
        Drawable localDrawable5 = localPackageManager.getActivityIcon(localComponentName2);
        this.mIcon = localDrawable5;
        if (this.mIcon == null)
        {
          Drawable localDrawable6 = localApplicationInfo.loadIcon(localPackageManager);
          this.mIcon = localDrawable6;
        }
        LayoutInflater localLayoutInflater = LayoutInflater.from(paramContext);
        int k = R.layout.abc_action_bar_home;
        int m = localTypedArray.getResourceId(14, k);
        ActionBarView localActionBarView4 = this;
        HomeView localHomeView1 = (HomeView)localLayoutInflater.inflate(m, localActionBarView4, false);
        this.mHomeLayout = localHomeView1;
        ActionBarView localActionBarView5 = this;
        HomeView localHomeView2 = (HomeView)localLayoutInflater.inflate(m, localActionBarView5, false);
        this.mExpandedHomeLayout = localHomeView2;
        this.mExpandedHomeLayout.setUp(true);
        HomeView localHomeView3 = this.mExpandedHomeLayout;
        View.OnClickListener localOnClickListener1 = this.mExpandedActionViewUpListener;
        localHomeView3.setOnClickListener(localOnClickListener1);
        HomeView localHomeView4 = this.mExpandedHomeLayout;
        Resources localResources = getResources();
        int n = R.string.abc_action_bar_up_description;
        CharSequence localCharSequence3 = localResources.getText(n);
        localHomeView4.setContentDescription(localCharSequence3);
        int i1 = localTypedArray.getResourceId(5, 0);
        this.mTitleStyleRes = i1;
        int i2 = localTypedArray.getResourceId(6, 0);
        this.mSubtitleStyleRes = i2;
        int i3 = localTypedArray.getResourceId(15, 0);
        this.mProgressStyle = i3;
        int i4 = localTypedArray.getResourceId(16, 0);
        this.mIndeterminateProgressStyle = i4;
        int i5 = localTypedArray.getDimensionPixelOffset(17, 0);
        this.mProgressBarPadding = i5;
        int i6 = localTypedArray.getDimensionPixelOffset(18, 0);
        this.mItemPadding = i6;
        int i7 = localTypedArray.getInt(3, 0);
        setDisplayOptions(i7);
        int i8 = localTypedArray.getResourceId(13, 0);
        if (i8 != 0)
        {
          ActionBarView localActionBarView6 = this;
          View localView = localLayoutInflater.inflate(i8, localActionBarView6, false);
          this.mCustomNavView = localView;
          this.mNavigationMode = 0;
          int i9 = this.mDisplayOptions | 0x10;
          setDisplayOptions(i9);
        }
        int i10 = localTypedArray.getLayoutDimension(1, 0);
        this.mContentHeight = i10;
        localTypedArray.recycle();
        CharSequence localCharSequence4 = this.mTitle;
        Context localContext3 = paramContext;
        ActionMenuItem localActionMenuItem = new ActionMenuItem(localContext3, 0, 16908332, 0, 0, localCharSequence4);
        this.mLogoNavItem = localActionMenuItem;
        HomeView localHomeView5 = this.mHomeLayout;
        View.OnClickListener localOnClickListener2 = this.mUpClickListener;
        localHomeView5.setOnClickListener(localOnClickListener2);
        this.mHomeLayout.setClickable(true);
        this.mHomeLayout.setFocusable(true);
        return;
        localNameNotFoundException1 = localNameNotFoundException1;
        int i11 = Log.e("ActionBarView", "Activity component name not found!", localNameNotFoundException1);
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException2)
      {
        while (true)
          int i12 = Log.e("ActionBarView", "Activity component name not found!", localNameNotFoundException2);
      }
    }
  }

  private void configPresenters(MenuBuilder paramMenuBuilder)
  {
    if (paramMenuBuilder != null)
    {
      ActionMenuPresenter localActionMenuPresenter1 = this.mActionMenuPresenter;
      paramMenuBuilder.addMenuPresenter(localActionMenuPresenter1);
      ExpandedActionViewMenuPresenter localExpandedActionViewMenuPresenter1 = this.mExpandedMenuPresenter;
      paramMenuBuilder.addMenuPresenter(localExpandedActionViewMenuPresenter1);
    }
    while (true)
    {
      this.mActionMenuPresenter.updateMenuView(true);
      this.mExpandedMenuPresenter.updateMenuView(true);
      return;
      ActionMenuPresenter localActionMenuPresenter2 = this.mActionMenuPresenter;
      Context localContext1 = this.mContext;
      localActionMenuPresenter2.initForMenu(localContext1, null);
      ExpandedActionViewMenuPresenter localExpandedActionViewMenuPresenter2 = this.mExpandedMenuPresenter;
      Context localContext2 = this.mContext;
      localExpandedActionViewMenuPresenter2.initForMenu(localContext2, null);
    }
  }

  private void initTitle()
  {
    boolean bool = true;
    int i2;
    int i3;
    label294: int i4;
    label313: LinearLayout localLinearLayout6;
    if (this.mTitleLayout == null)
    {
      LayoutInflater localLayoutInflater = LayoutInflater.from(getContext());
      int i = R.layout.abc_action_bar_title_item;
      LinearLayout localLinearLayout1 = (LinearLayout)localLayoutInflater.inflate(i, this, false);
      this.mTitleLayout = localLinearLayout1;
      LinearLayout localLinearLayout2 = this.mTitleLayout;
      int j = R.id.action_bar_title;
      TextView localTextView1 = (TextView)localLinearLayout2.findViewById(j);
      this.mTitleView = localTextView1;
      LinearLayout localLinearLayout3 = this.mTitleLayout;
      int k = R.id.action_bar_subtitle;
      TextView localTextView2 = (TextView)localLinearLayout3.findViewById(k);
      this.mSubtitleView = localTextView2;
      LinearLayout localLinearLayout4 = this.mTitleLayout;
      int m = R.id.up;
      View localView1 = localLinearLayout4.findViewById(m);
      this.mTitleUpView = localView1;
      LinearLayout localLinearLayout5 = this.mTitleLayout;
      View.OnClickListener localOnClickListener = this.mUpClickListener;
      localLinearLayout5.setOnClickListener(localOnClickListener);
      if (this.mTitleStyleRes != 0)
      {
        TextView localTextView3 = this.mTitleView;
        Context localContext1 = this.mContext;
        int n = this.mTitleStyleRes;
        localTextView3.setTextAppearance(localContext1, n);
      }
      if (this.mTitle != null)
      {
        TextView localTextView4 = this.mTitleView;
        CharSequence localCharSequence1 = this.mTitle;
        localTextView4.setText(localCharSequence1);
      }
      if (this.mSubtitleStyleRes != 0)
      {
        TextView localTextView5 = this.mSubtitleView;
        Context localContext2 = this.mContext;
        int i1 = this.mSubtitleStyleRes;
        localTextView5.setTextAppearance(localContext2, i1);
      }
      if (this.mSubtitle != null)
      {
        TextView localTextView6 = this.mSubtitleView;
        CharSequence localCharSequence2 = this.mSubtitle;
        localTextView6.setText(localCharSequence2);
        this.mSubtitleView.setVisibility(0);
      }
      if ((this.mDisplayOptions & 0x4) == 0)
        break label393;
      i2 = 1;
      if ((this.mDisplayOptions & 0x2) == 0)
        break label399;
      i3 = 1;
      View localView2 = this.mTitleUpView;
      if (i3 != 0)
        break label411;
      if (i2 == 0)
        break label405;
      i4 = 0;
      localView2.setVisibility(i4);
      localLinearLayout6 = this.mTitleLayout;
      if ((i2 == 0) || (i3 != 0))
        break label418;
    }
    while (true)
    {
      localLinearLayout6.setEnabled(bool);
      LinearLayout localLinearLayout7 = this.mTitleLayout;
      addView(localLinearLayout7);
      if (this.mExpandedActionView == null)
      {
        if (!TextUtils.isEmpty(this.mTitle))
          return;
        if (!TextUtils.isEmpty(this.mSubtitle))
          return;
      }
      this.mTitleLayout.setVisibility(8);
      return;
      label393: i2 = 0;
      break;
      label399: i3 = 0;
      break label294;
      label405: i4 = 4;
      break label313;
      label411: i4 = 8;
      break label313;
      label418: bool = false;
    }
  }

  private void setTitleImpl(CharSequence paramCharSequence)
  {
    int i = 0;
    this.mTitle = paramCharSequence;
    int j;
    LinearLayout localLinearLayout;
    if (this.mTitleView != null)
    {
      this.mTitleView.setText(paramCharSequence);
      if ((this.mExpandedActionView != null) || ((this.mDisplayOptions & 0x8) == 0) || ((TextUtils.isEmpty(this.mTitle)) && (TextUtils.isEmpty(this.mSubtitle))))
        break label96;
      j = 1;
      localLinearLayout = this.mTitleLayout;
      if (j == 0)
        break label101;
    }
    while (true)
    {
      localLinearLayout.setVisibility(i);
      if (this.mLogoNavItem == null)
        return;
      MenuItem localMenuItem = this.mLogoNavItem.setTitle(paramCharSequence);
      return;
      label96: j = 0;
      break;
      label101: i = 8;
    }
  }

  public void collapseActionView()
  {
    if (this.mExpandedMenuPresenter == null);
    for (MenuItemImpl localMenuItemImpl = null; ; localMenuItemImpl = this.mExpandedMenuPresenter.mCurrentExpandedItem)
    {
      if (localMenuItemImpl == null)
        return;
      boolean bool = localMenuItemImpl.collapseActionView();
      return;
    }
  }

  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new ActionBar.LayoutParams(19);
  }

  public ViewGroup.LayoutParams generateLayoutParams(AttributeSet paramAttributeSet)
  {
    Context localContext = getContext();
    return new ActionBar.LayoutParams(localContext, paramAttributeSet);
  }

  public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams)
  {
    if (paramLayoutParams == null)
      paramLayoutParams = generateDefaultLayoutParams();
    return paramLayoutParams;
  }

  public int getDisplayOptions()
  {
    return this.mDisplayOptions;
  }

  public int getDropdownSelectedPosition()
  {
    return this.mSpinner.getSelectedItemPosition();
  }

  public int getNavigationMode()
  {
    return this.mNavigationMode;
  }

  public boolean hasExpandedActionView()
  {
    if ((this.mExpandedMenuPresenter != null) && (this.mExpandedMenuPresenter.mCurrentExpandedItem != null));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void initIndeterminateProgress()
  {
    Context localContext = this.mContext;
    int i = this.mIndeterminateProgressStyle;
    ProgressBarICS localProgressBarICS1 = new ProgressBarICS(localContext, null, 0, i);
    this.mIndeterminateProgressView = localProgressBarICS1;
    ProgressBarICS localProgressBarICS2 = this.mIndeterminateProgressView;
    int j = R.id.progress_circular;
    localProgressBarICS2.setId(j);
    this.mIndeterminateProgressView.setVisibility(8);
    ProgressBarICS localProgressBarICS3 = this.mIndeterminateProgressView;
    addView(localProgressBarICS3);
  }

  public void initProgress()
  {
    Context localContext = this.mContext;
    int i = this.mProgressStyle;
    ProgressBarICS localProgressBarICS1 = new ProgressBarICS(localContext, null, 0, i);
    this.mProgressView = localProgressBarICS1;
    ProgressBarICS localProgressBarICS2 = this.mProgressView;
    int j = R.id.progress_horizontal;
    localProgressBarICS2.setId(j);
    this.mProgressView.setMax(10000);
    this.mProgressView.setVisibility(8);
    ProgressBarICS localProgressBarICS3 = this.mProgressView;
    addView(localProgressBarICS3);
  }

  public boolean isCollapsed()
  {
    return this.mIsCollapsed;
  }

  public boolean isSplitActionBar()
  {
    return this.mSplitActionBar;
  }

  protected void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    this.mTitleView = null;
    this.mSubtitleView = null;
    this.mTitleUpView = null;
    if ((this.mTitleLayout != null) && (this.mTitleLayout.getParent() == this))
    {
      LinearLayout localLinearLayout = this.mTitleLayout;
      removeView(localLinearLayout);
    }
    this.mTitleLayout = null;
    if ((this.mDisplayOptions & 0x8) != 0)
      initTitle();
    if ((this.mTabScrollView != null) && (this.mIncludeTabs))
    {
      ViewGroup.LayoutParams localLayoutParams = this.mTabScrollView.getLayoutParams();
      if (localLayoutParams != null)
      {
        localLayoutParams.width = -1;
        localLayoutParams.height = -1;
      }
      this.mTabScrollView.setAllowCollapse(true);
    }
    if (this.mProgressView != null)
    {
      ProgressBarICS localProgressBarICS1 = this.mProgressView;
      removeView(localProgressBarICS1);
      initProgress();
    }
    if (this.mIndeterminateProgressView == null)
      return;
    ProgressBarICS localProgressBarICS2 = this.mIndeterminateProgressView;
    removeView(localProgressBarICS2);
    initIndeterminateProgress();
  }

  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    Runnable localRunnable = this.mTabSelector;
    boolean bool1 = removeCallbacks(localRunnable);
    if (this.mActionMenuPresenter == null)
      return;
    boolean bool2 = this.mActionMenuPresenter.hideOverflowMenu();
    boolean bool3 = this.mActionMenuPresenter.hideSubMenus();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    HomeView localHomeView = this.mHomeLayout;
    addView(localHomeView);
    if (this.mCustomNavView == null)
      return;
    if ((this.mDisplayOptions & 0x10) == 0)
      return;
    ViewParent localViewParent = this.mCustomNavView.getParent();
    if (localViewParent == this)
      return;
    if ((localViewParent instanceof ViewGroup))
    {
      ViewGroup localViewGroup = (ViewGroup)localViewParent;
      View localView1 = this.mCustomNavView;
      localViewGroup.removeView(localView1);
    }
    View localView2 = this.mCustomNavView;
    addView(localView2);
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
    if (i2 <= 0)
      return;
    HomeView localHomeView;
    int i12;
    label178: int i18;
    label256: View localView;
    label448: ActionBar.LayoutParams localLayoutParams1;
    label475: int i29;
    label487: int i30;
    int i31;
    int i32;
    int i35;
    int i37;
    label589: int i39;
    label628: int i42;
    if (this.mExpandedActionView != null)
    {
      localHomeView = this.mExpandedHomeLayout;
      int i3 = localHomeView.getVisibility();
      int i4 = 8;
      if (i3 != i4)
      {
        int i5 = localHomeView.getLeftOffset();
        int i6 = i + i5;
        ActionBarView localActionBarView1 = this;
        int i7 = i6;
        int i8 = j;
        int i9 = localActionBarView1.positionChild(localHomeView, i7, i8, i2) + i5;
        i += i9;
      }
      if (this.mExpandedActionView == null)
      {
        if (this.mTitleLayout == null)
          break label856;
        int i10 = this.mTitleLayout.getVisibility();
        int i11 = 8;
        if ((i10 == i11) || ((this.mDisplayOptions & 0x8) == 0))
          break label856;
        i12 = 1;
        if (i12 != 0)
        {
          LinearLayout localLinearLayout1 = this.mTitleLayout;
          ActionBarView localActionBarView2 = this;
          LinearLayout localLinearLayout2 = localLinearLayout1;
          int i13 = i;
          int i14 = j;
          int i15 = localActionBarView2.positionChild(localLinearLayout2, i13, i14, i2);
          i += i15;
        }
      }
      switch (this.mNavigationMode)
      {
      case 0:
      default:
        int i16 = paramInt3 - paramInt1;
        int i17 = getPaddingRight();
        i18 = i16 - i17;
        if (this.mMenuView != null)
        {
          ViewParent localViewParent = this.mMenuView.getParent();
          ActionBarView localActionBarView3 = this;
          if (localViewParent == localActionBarView3)
          {
            ActionMenuView localActionMenuView1 = this.mMenuView;
            ActionBarView localActionBarView4 = this;
            ActionMenuView localActionMenuView2 = localActionMenuView1;
            int i19 = i18;
            int i20 = j;
            int i21 = localActionBarView4.positionChildInverse(localActionMenuView2, i19, i20, i2);
            int i22 = this.mMenuView.getMeasuredWidth();
            i18 -= i22;
          }
        }
        if (this.mIndeterminateProgressView != null)
        {
          int i23 = this.mIndeterminateProgressView.getVisibility();
          int i24 = 8;
          if (i23 != i24)
          {
            ProgressBarICS localProgressBarICS1 = this.mIndeterminateProgressView;
            ActionBarView localActionBarView5 = this;
            ProgressBarICS localProgressBarICS2 = localProgressBarICS1;
            int i25 = i18;
            int i26 = j;
            int i27 = localActionBarView5.positionChildInverse(localProgressBarICS2, i25, i26, i2);
            int i28 = this.mIndeterminateProgressView.getMeasuredWidth();
            i18 -= i28;
          }
        }
        localView = null;
        if (this.mExpandedActionView != null)
        {
          localView = this.mExpandedActionView;
          if (localView != null)
          {
            ViewGroup.LayoutParams localLayoutParams = localView.getLayoutParams();
            if (!(localLayoutParams instanceof ActionBar.LayoutParams))
              break label1056;
            localLayoutParams1 = (ActionBar.LayoutParams)localLayoutParams;
            if (localLayoutParams1 == null)
              break label1062;
            i29 = localLayoutParams1.gravity;
            i30 = localView.getMeasuredWidth();
            i31 = 0;
            i32 = 0;
            if (localLayoutParams1 != null)
            {
              int i33 = localLayoutParams1.leftMargin;
              i += i33;
              int i34 = localLayoutParams1.rightMargin;
              i18 -= i34;
              i31 = localLayoutParams1.topMargin;
              i32 = localLayoutParams1.bottomMargin;
            }
            i35 = i29 & 0x7;
            int i36 = 1;
            if (i35 != i36)
              break label1093;
            i37 = (getWidth() - i30) / 2;
            int i38 = i;
            if (i37 >= i38)
              break label1069;
            i35 = 3;
            i39 = 0;
            switch (i35)
            {
            case 2:
            case 4:
            default:
              int i40 = i29 & 0x70;
              int i41 = 65535;
              if (i29 == i41)
                i40 = 16;
              i42 = 0;
              switch (i40)
              {
              default:
              case 16:
              case 48:
              case 80:
              }
              break;
            case 1:
            case 3:
            case 5:
            }
          }
        }
        break;
      case 1:
      case 2:
      }
    }
    while (true)
    {
      int i43 = localView.getMeasuredWidth();
      int i44 = i39 + i43;
      int i45 = localView.getMeasuredHeight() + i42;
      int i46 = i39;
      int i47 = i42;
      int i48 = i44;
      int i49 = i45;
      localView.layout(i46, i47, i48, i49);
      int i50 = i + i43;
      if (this.mProgressView == null)
        return;
      this.mProgressView.bringToFront();
      int i51 = this.mProgressView.getMeasuredHeight() / 2;
      ProgressBarICS localProgressBarICS3 = this.mProgressView;
      int i52 = this.mProgressBarPadding;
      int i53 = -i51;
      int i54 = this.mProgressBarPadding;
      int i55 = this.mProgressView.getMeasuredWidth();
      int i56 = i54 + i55;
      ProgressBarICS localProgressBarICS4 = localProgressBarICS3;
      int i57 = i52;
      int i58 = i53;
      int i59 = i56;
      localProgressBarICS4.layout(i57, i58, i59, i51);
      return;
      localHomeView = this.mHomeLayout;
      break;
      label856: i12 = 0;
      break label178;
      if (this.mListNavLayout == null)
        break label256;
      if (i12 != 0)
      {
        int i60 = this.mItemPadding;
        i += i60;
      }
      LinearLayout localLinearLayout3 = this.mListNavLayout;
      ActionBarView localActionBarView6 = this;
      LinearLayout localLinearLayout4 = localLinearLayout3;
      int i61 = i;
      int i62 = j;
      int i63 = localActionBarView6.positionChild(localLinearLayout4, i61, i62, i2);
      int i64 = this.mItemPadding;
      int i65 = i63 + i64;
      i += i65;
      break label256;
      if (this.mTabScrollView == null)
        break label256;
      if (i12 != 0)
      {
        int i66 = this.mItemPadding;
        i += i66;
      }
      ScrollingTabContainerView localScrollingTabContainerView1 = this.mTabScrollView;
      ActionBarView localActionBarView7 = this;
      ScrollingTabContainerView localScrollingTabContainerView2 = localScrollingTabContainerView1;
      int i67 = i;
      int i68 = j;
      int i69 = localActionBarView7.positionChild(localScrollingTabContainerView2, i67, i68, i2);
      int i70 = this.mItemPadding;
      int i71 = i69 + i70;
      i += i71;
      break label256;
      if (((this.mDisplayOptions & 0x10) == 0) || (this.mCustomNavView == null))
        break label448;
      localView = this.mCustomNavView;
      break label448;
      label1056: localLayoutParams1 = null;
      break label475;
      label1062: i29 = 19;
      break label487;
      label1069: int i72 = i37 + i30;
      int i73 = i18;
      if (i72 <= i73)
        break label589;
      i35 = 5;
      break label589;
      label1093: int i74 = 65535;
      if (i29 != i74)
        break label589;
      i35 = 3;
      break label589;
      i39 = (getWidth() - i30) / 2;
      break label628;
      i39 = i;
      break label628;
      i39 = i18 - i30;
      break label628;
      int i75 = getPaddingTop();
      int i76 = getHeight();
      int i77 = getPaddingBottom();
      int i78 = i76 - i77 - i75;
      int i79 = localView.getMeasuredHeight();
      i42 = (i78 - i79) / 2;
      continue;
      i42 = getPaddingTop() + i31;
      continue;
      int i80 = getHeight();
      int i81 = getPaddingBottom();
      int i82 = i80 - i81;
      int i83 = localView.getMeasuredHeight();
      i42 = i82 - i83 - i32;
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = getChildCount();
    int k;
    if (this.mIsCollapsable)
    {
      int j = 0;
      k = 0;
      while (k < i)
      {
        ActionBarView localActionBarView1 = this;
        int m = k;
        View localView1 = localActionBarView1.getChildAt(m);
        int n = localView1.getVisibility();
        int i1 = 8;
        if (n != i1)
        {
          ActionMenuView localActionMenuView1 = this.mMenuView;
          if ((localView1 != localActionMenuView1) || (this.mMenuView.getChildCount() != 0))
            j += 1;
        }
        k += 1;
      }
      if (j == 0)
      {
        ActionBarView localActionBarView2 = this;
        int i2 = 0;
        int i3 = 0;
        localActionBarView2.setMeasuredDimension(i2, i3);
        boolean bool1 = true;
        this.mIsCollapsed = bool1;
        return;
      }
    }
    boolean bool2 = false;
    this.mIsCollapsed = bool2;
    int i4 = View.MeasureSpec.getMode(paramInt1);
    int i5 = 1073741824;
    if (i4 != i5)
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      String str1 = getClass().getSimpleName();
      String str2 = str1 + " can only be used " + "with android:layout_width=\"MATCH_PARENT\" (or fill_parent)";
      throw new IllegalStateException(str2);
    }
    int i6 = View.MeasureSpec.getMode(paramInt2);
    int i7 = -2147483648;
    if (i6 != i7)
    {
      StringBuilder localStringBuilder2 = new StringBuilder();
      String str3 = getClass().getSimpleName();
      String str4 = str3 + " can only be used " + "with android:layout_height=\"wrap_content\"";
      throw new IllegalStateException(str4);
    }
    int i8 = View.MeasureSpec.getSize(paramInt1);
    int i9;
    int i12;
    int i15;
    int i18;
    int i19;
    int i20;
    HomeView localHomeView1;
    label382: ViewGroup.LayoutParams localLayoutParams1;
    int i24;
    label429: int i44;
    label718: label752: View localView2;
    label768: ActionBar.LayoutParams localLayoutParams;
    label811: int i45;
    int i51;
    int i54;
    int i57;
    label939: int i59;
    label962: int i63;
    if (this.mContentHeight > 0)
    {
      i9 = this.mContentHeight;
      int i10 = getPaddingTop();
      int i11 = getPaddingBottom();
      i12 = i10 + i11;
      int i13 = getPaddingLeft();
      int i14 = getPaddingRight();
      i15 = i9 - i12;
      int i16 = -2147483648;
      int i17 = View.MeasureSpec.makeMeasureSpec(i15, i16);
      i18 = i8 - i13 - i14;
      i19 = i18 / 2;
      i20 = i19;
      if (this.mExpandedActionView == null)
        break label1266;
      localHomeView1 = this.mExpandedHomeLayout;
      int i21 = localHomeView1.getVisibility();
      int i22 = 8;
      if (i21 != i22)
      {
        localLayoutParams1 = localHomeView1.getLayoutParams();
        if (localLayoutParams1.width >= 0)
          break label1275;
        int i23 = -2147483648;
        i24 = View.MeasureSpec.makeMeasureSpec(i18, i23);
        int i25 = 1073741824;
        int i26 = View.MeasureSpec.makeMeasureSpec(i15, i25);
        HomeView localHomeView2 = localHomeView1;
        int i27 = i24;
        int i28 = i26;
        localHomeView2.measure(i27, i28);
        int i29 = localHomeView1.getMeasuredWidth();
        int i30 = localHomeView1.getLeftOffset();
        int i31 = i29 + i30;
        int i32 = i18 - i31;
        i18 = Math.max(0, i32);
        int i33 = i18 - i31;
        i19 = Math.max(0, i33);
      }
      if (this.mMenuView != null)
      {
        ViewParent localViewParent = this.mMenuView.getParent();
        ActionBarView localActionBarView3 = this;
        if (localViewParent == localActionBarView3)
        {
          ActionMenuView localActionMenuView2 = this.mMenuView;
          ActionBarView localActionBarView4 = this;
          ActionMenuView localActionMenuView3 = localActionMenuView2;
          int i34 = 0;
          i18 = localActionBarView4.measureChildView(localActionMenuView3, i18, i17, i34);
          int i35 = this.mMenuView.getMeasuredWidth();
          int i36 = i20 - i35;
          i20 = Math.max(0, i36);
        }
      }
      if (this.mIndeterminateProgressView != null)
      {
        int i37 = this.mIndeterminateProgressView.getVisibility();
        int i38 = 8;
        if (i37 != i38)
        {
          ProgressBarICS localProgressBarICS1 = this.mIndeterminateProgressView;
          ActionBarView localActionBarView5 = this;
          ProgressBarICS localProgressBarICS2 = localProgressBarICS1;
          int i39 = 0;
          i18 = localActionBarView5.measureChildView(localProgressBarICS2, i18, i17, i39);
          int i40 = this.mIndeterminateProgressView.getMeasuredWidth();
          int i41 = i20 - i40;
          i20 = Math.max(0, i41);
        }
      }
      if (this.mTitleLayout == null)
        break label1291;
      int i42 = this.mTitleLayout.getVisibility();
      int i43 = 8;
      if ((i42 == i43) || ((this.mDisplayOptions & 0x8) == 0))
        break label1291;
      i44 = 1;
      if (this.mExpandedActionView == null);
      switch (this.mNavigationMode)
      {
      default:
        localView2 = null;
        if (this.mExpandedActionView != null)
        {
          localView2 = this.mExpandedActionView;
          if (localView2 != null)
          {
            ViewGroup.LayoutParams localLayoutParams2 = localView2.getLayoutParams();
            ActionBarView localActionBarView6 = this;
            ViewGroup.LayoutParams localLayoutParams3 = localLayoutParams2;
            localLayoutParams1 = localActionBarView6.generateLayoutParams(localLayoutParams3);
            if (!(localLayoutParams1 instanceof ActionBar.LayoutParams))
              break label1611;
            localLayoutParams = (ActionBar.LayoutParams)localLayoutParams1;
            i45 = 0;
            int i46 = 0;
            if (localLayoutParams != null)
            {
              int i47 = localLayoutParams.leftMargin;
              int i48 = localLayoutParams.rightMargin;
              i45 = i47 + i48;
              int i49 = localLayoutParams.topMargin;
              int i50 = localLayoutParams.bottomMargin;
              i46 = i49 + i50;
            }
            if (this.mContentHeight > 0)
              break label1617;
            i51 = -2147483648;
            int i52 = 0;
            if (localLayoutParams1.height >= 0)
              i15 = Math.min(localLayoutParams1.height, i15);
            int i53 = i15 - i46;
            i54 = Math.max(i52, i53);
            int i55 = localLayoutParams1.width;
            int i56 = 65534;
            if (i55 == i56)
              break label1652;
            i57 = 1073741824;
            int i58 = 0;
            if (localLayoutParams1.width < 0)
              break label1660;
            i59 = Math.min(localLayoutParams1.width, i18);
            int i60 = i59 - i45;
            int i61 = i58;
            int i62 = i60;
            i63 = Math.max(i61, i62);
            if (localLayoutParams == null)
              break label1667;
          }
        }
        break;
      case 1:
      case 2:
      }
    }
    int i83;
    label1667: for (int i64 = localLayoutParams.gravity; ; i64 = 19)
    {
      int i65 = i64 & 0x7;
      int i66 = 1;
      if (i65 == i66)
      {
        int i67 = localLayoutParams1.width;
        int i68 = 65535;
        if (i67 == i68)
        {
          int i69 = i19;
          int i70 = i20;
          i63 = Math.min(i69, i70) * 2;
        }
      }
      int i71 = View.MeasureSpec.makeMeasureSpec(i63, i57);
      int i72 = View.MeasureSpec.makeMeasureSpec(i54, i51);
      int i73 = i71;
      int i74 = i72;
      localView2.measure(i73, i74);
      int i75 = localView2.getMeasuredWidth() + i45;
      i18 -= i75;
      if ((this.mExpandedActionView == null) && (i44 != 0))
      {
        LinearLayout localLinearLayout1 = this.mTitleLayout;
        int i76 = View.MeasureSpec.makeMeasureSpec(this.mContentHeight, 1073741824);
        ActionBarView localActionBarView7 = this;
        LinearLayout localLinearLayout2 = localLinearLayout1;
        int i77 = i76;
        int i78 = 0;
        int i79 = localActionBarView7.measureChildView(localLinearLayout2, i18, i77, i78);
        int i80 = this.mTitleLayout.getMeasuredWidth();
        int i81 = i19 - i80;
        int i82 = Math.max(0, i81);
      }
      if (this.mContentHeight > 0)
        break label1794;
      i83 = 0;
      k = 0;
      while (k < i)
      {
        ActionBarView localActionBarView8 = this;
        int i84 = k;
        int i85 = localActionBarView8.getChildAt(i84).getMeasuredHeight() + i12;
        int i86 = i85;
        int i87 = i83;
        if (i86 > i87)
          i83 = i85;
        int i88 = k + 1;
      }
      i9 = View.MeasureSpec.getSize(paramInt2);
      break;
      label1266: localHomeView1 = this.mHomeLayout;
      break label382;
      label1275: i24 = View.MeasureSpec.makeMeasureSpec(localLayoutParams1.width, 1073741824);
      break label429;
      label1291: i44 = 0;
      break label718;
      if (this.mListNavLayout == null)
        break label752;
      if (i44 != 0);
      for (int i89 = this.mItemPadding * 2; ; i89 = this.mItemPadding)
      {
        int i90 = i18 - i89;
        int i91 = Math.max(0, i90);
        int i92 = i19 - i89;
        int i93 = Math.max(0, i92);
        LinearLayout localLinearLayout3 = this.mListNavLayout;
        int i94 = -2147483648;
        int i95 = View.MeasureSpec.makeMeasureSpec(i91, i94);
        int i96 = 1073741824;
        int i97 = View.MeasureSpec.makeMeasureSpec(i15, i96);
        localLinearLayout3.measure(i95, i97);
        int i98 = this.mListNavLayout.getMeasuredWidth();
        int i99 = i91 - i98;
        i18 = Math.max(0, i99);
        int i100 = i93 - i98;
        i19 = Math.max(0, i100);
        break;
      }
      if (this.mTabScrollView == null)
        break label752;
      if (i44 != 0);
      for (i89 = this.mItemPadding * 2; ; i89 = this.mItemPadding)
      {
        int i101 = i18 - i89;
        int i102 = Math.max(0, i101);
        int i103 = i19 - i89;
        int i104 = Math.max(0, i103);
        ScrollingTabContainerView localScrollingTabContainerView = this.mTabScrollView;
        int i105 = -2147483648;
        int i106 = View.MeasureSpec.makeMeasureSpec(i102, i105);
        int i107 = 1073741824;
        int i108 = View.MeasureSpec.makeMeasureSpec(i15, i107);
        localScrollingTabContainerView.measure(i106, i108);
        int i109 = this.mTabScrollView.getMeasuredWidth();
        int i110 = i102 - i109;
        i18 = Math.max(0, i110);
        int i111 = i104 - i109;
        i19 = Math.max(0, i111);
        break;
      }
      if (((this.mDisplayOptions & 0x10) == 0) || (this.mCustomNavView == null))
        break label768;
      localView2 = this.mCustomNavView;
      break label768;
      label1611: localLayoutParams = null;
      break label811;
      label1617: int i112 = localLayoutParams1.height;
      int i113 = 65534;
      if (i112 != i113);
      for (i51 = 1073741824; ; i51 = -2147483648)
        break;
      label1652: i57 = -2147483648;
      break label939;
      label1660: i59 = i18;
      break label962;
    }
    ActionBarView localActionBarView9 = this;
    int i114 = i83;
    localActionBarView9.setMeasuredDimension(i8, i114);
    while (true)
    {
      if (this.mContextView != null)
      {
        ActionBarContextView localActionBarContextView = this.mContextView;
        int i115 = getMeasuredHeight();
        localActionBarContextView.setContentHeight(i115);
      }
      if (this.mProgressView == null)
        return;
      int i116 = this.mProgressView.getVisibility();
      int i117 = 8;
      if (i116 == i117)
        return;
      ProgressBarICS localProgressBarICS3 = this.mProgressView;
      int i118 = this.mProgressBarPadding * 2;
      int i119 = View.MeasureSpec.makeMeasureSpec(i8 - i118, 1073741824);
      int i120 = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), -2147483648);
      localProgressBarICS3.measure(i119, i120);
      return;
      label1794: ActionBarView localActionBarView10 = this;
      int i121 = i9;
      localActionBarView10.setMeasuredDimension(i8, i121);
    }
  }

  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    Parcelable localParcelable = localSavedState.getSuperState();
    super.onRestoreInstanceState(localParcelable);
    if ((localSavedState.expandedMenuItemId != 0) && (this.mExpandedMenuPresenter != null) && (this.mOptionsMenu != null))
    {
      MenuBuilder localMenuBuilder = this.mOptionsMenu;
      int i = localSavedState.expandedMenuItemId;
      SupportMenuItem localSupportMenuItem = (SupportMenuItem)localMenuBuilder.findItem(i);
      if (localSupportMenuItem != null)
        boolean bool = localSupportMenuItem.expandActionView();
    }
    if (!localSavedState.isOverflowOpen)
      return;
    postShowOverflowMenu();
  }

  public Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable = super.onSaveInstanceState();
    SavedState localSavedState = new SavedState(localParcelable);
    if ((this.mExpandedMenuPresenter != null) && (this.mExpandedMenuPresenter.mCurrentExpandedItem != null))
    {
      int i = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
      localSavedState.expandedMenuItemId = i;
    }
    boolean bool = isOverflowMenuShowing();
    localSavedState.isOverflowOpen = bool;
    return localSavedState;
  }

  public void setCallback(ActionBar.OnNavigationListener paramOnNavigationListener)
  {
    this.mCallback = paramOnNavigationListener;
  }

  public void setCollapsable(boolean paramBoolean)
  {
    this.mIsCollapsable = paramBoolean;
  }

  public void setContextView(ActionBarContextView paramActionBarContextView)
  {
    this.mContextView = paramActionBarContextView;
  }

  public void setCustomNavigationView(View paramView)
  {
    if ((this.mDisplayOptions & 0x10) != 0);
    for (int i = 1; ; i = 0)
    {
      if ((this.mCustomNavView != null) && (i != 0))
      {
        View localView1 = this.mCustomNavView;
        removeView(localView1);
      }
      this.mCustomNavView = paramView;
      if (this.mCustomNavView == null)
        return;
      if (i == 0)
        return;
      View localView2 = this.mCustomNavView;
      addView(localView2);
      return;
    }
  }

  public void setDisplayOptions(int paramInt)
  {
    Object localObject = null;
    int j = -1;
    boolean bool1 = true;
    int k;
    label37: int m;
    label52: boolean bool2;
    label76: int n;
    label117: Drawable localDrawable;
    label134: label159: int i1;
    label185: label203: LinearLayout localLinearLayout1;
    if (this.mDisplayOptions != j)
    {
      this.mDisplayOptions = paramInt;
      if ((j & 0x1F) == 0)
        break label381;
      if ((paramInt & 0x2) == 0)
        break label300;
      k = 1;
      if ((k == 0) || (this.mExpandedActionView != null))
        break label306;
      m = 0;
      this.mHomeLayout.setVisibility(m);
      if ((j & 0x4) != 0)
      {
        if ((paramInt & 0x4) == 0)
          break label313;
        bool2 = true;
        this.mHomeLayout.setUp(bool2);
        if (bool2)
          setHomeButtonEnabled(true);
      }
      if ((j & 0x1) != 0)
      {
        if ((this.mLogo == null) || ((paramInt & 0x1) == 0))
          break label319;
        n = 1;
        HomeView localHomeView1 = this.mHomeLayout;
        if (n == 0)
          break label325;
        localDrawable = this.mLogo;
        localHomeView1.setIcon(localDrawable);
      }
      if ((j & 0x8) != 0)
      {
        if ((paramInt & 0x8) == 0)
          break label334;
        initTitle();
      }
      if ((this.mTitleLayout != null) && ((j & 0x6) != 0))
      {
        if ((this.mDisplayOptions & 0x4) == 0)
          break label349;
        i1 = 1;
        View localView1 = this.mTitleUpView;
        int i;
        if (k == 0)
        {
          if (i1 == 0)
            break label355;
          i = 0;
        }
        localView1.setVisibility(i);
        localLinearLayout1 = this.mTitleLayout;
        if ((k != 0) || (i1 == 0))
          break label360;
        label224: localLinearLayout1.setEnabled(bool1);
      }
      if (((j & 0x10) != 0) && (this.mCustomNavView != null))
      {
        if ((paramInt & 0x10) == 0)
          break label366;
        View localView2 = this.mCustomNavView;
        addView(localView2);
      }
      label263: requestLayout();
    }
    while (true)
    {
      if (this.mHomeLayout.isEnabled())
        break label388;
      this.mHomeLayout.setContentDescription(null);
      return;
      int i2 = this.mDisplayOptions;
      j = paramInt ^ i2;
      break;
      label300: k = 0;
      break label37;
      label306: m = 8;
      break label52;
      label313: bool2 = false;
      break label76;
      label319: n = 0;
      break label117;
      label325: localDrawable = this.mIcon;
      break label134;
      label334: LinearLayout localLinearLayout2 = this.mTitleLayout;
      removeView(localLinearLayout2);
      break label159;
      label349: i1 = 0;
      break label185;
      label355: localLinearLayout1 = null;
      break label203;
      label360: bool1 = false;
      break label224;
      label366: View localView3 = this.mCustomNavView;
      removeView(localView3);
      break label263;
      label381: invalidate();
    }
    label388: if ((paramInt & 0x4) != 0)
    {
      HomeView localHomeView2 = this.mHomeLayout;
      Resources localResources1 = this.mContext.getResources();
      int i3 = R.string.abc_action_bar_up_description;
      CharSequence localCharSequence1 = localResources1.getText(i3);
      localHomeView2.setContentDescription(localCharSequence1);
      return;
    }
    HomeView localHomeView3 = this.mHomeLayout;
    Resources localResources2 = this.mContext.getResources();
    int i4 = R.string.abc_action_bar_home_description;
    CharSequence localCharSequence2 = localResources2.getText(i4);
    localHomeView3.setContentDescription(localCharSequence2);
  }

  public void setDropdownAdapter(SpinnerAdapter paramSpinnerAdapter)
  {
    this.mSpinnerAdapter = paramSpinnerAdapter;
    if (this.mSpinner == null)
      return;
    this.mSpinner.setAdapter(paramSpinnerAdapter);
  }

  public void setDropdownSelectedPosition(int paramInt)
  {
    this.mSpinner.setSelection(paramInt);
  }

  public void setEmbeddedTabView(ScrollingTabContainerView paramScrollingTabContainerView)
  {
    if (this.mTabScrollView != null)
    {
      ScrollingTabContainerView localScrollingTabContainerView1 = this.mTabScrollView;
      removeView(localScrollingTabContainerView1);
    }
    this.mTabScrollView = paramScrollingTabContainerView;
    if (paramScrollingTabContainerView != null);
    for (boolean bool = true; ; bool = false)
    {
      this.mIncludeTabs = bool;
      if (!this.mIncludeTabs)
        return;
      if (this.mNavigationMode != 2)
        return;
      ScrollingTabContainerView localScrollingTabContainerView2 = this.mTabScrollView;
      addView(localScrollingTabContainerView2);
      ViewGroup.LayoutParams localLayoutParams = this.mTabScrollView.getLayoutParams();
      localLayoutParams.width = -1;
      localLayoutParams.height = -1;
      paramScrollingTabContainerView.setAllowCollapse(true);
      return;
    }
  }

  public void setHomeAsUpIndicator(int paramInt)
  {
    this.mHomeLayout.setUpIndicator(paramInt);
  }

  public void setHomeAsUpIndicator(Drawable paramDrawable)
  {
    this.mHomeLayout.setUpIndicator(paramDrawable);
  }

  public void setHomeButtonEnabled(boolean paramBoolean)
  {
    this.mHomeLayout.setEnabled(paramBoolean);
    this.mHomeLayout.setFocusable(paramBoolean);
    if (!paramBoolean)
    {
      this.mHomeLayout.setContentDescription(null);
      return;
    }
    if ((this.mDisplayOptions & 0x4) != 0)
    {
      HomeView localHomeView1 = this.mHomeLayout;
      Resources localResources1 = this.mContext.getResources();
      int i = R.string.abc_action_bar_up_description;
      CharSequence localCharSequence1 = localResources1.getText(i);
      localHomeView1.setContentDescription(localCharSequence1);
      return;
    }
    HomeView localHomeView2 = this.mHomeLayout;
    Resources localResources2 = this.mContext.getResources();
    int j = R.string.abc_action_bar_home_description;
    CharSequence localCharSequence2 = localResources2.getText(j);
    localHomeView2.setContentDescription(localCharSequence2);
  }

  public void setIcon(int paramInt)
  {
    Drawable localDrawable = this.mContext.getResources().getDrawable(paramInt);
    setIcon(localDrawable);
  }

  public void setIcon(Drawable paramDrawable)
  {
    this.mIcon = paramDrawable;
    if ((paramDrawable != null) && (((this.mDisplayOptions & 0x1) == 0) || (this.mLogo == null)))
      this.mHomeLayout.setIcon(paramDrawable);
    if (this.mExpandedActionView == null)
      return;
    HomeView localHomeView = this.mExpandedHomeLayout;
    Drawable.ConstantState localConstantState = this.mIcon.getConstantState();
    Resources localResources = getResources();
    Drawable localDrawable = localConstantState.newDrawable(localResources);
    localHomeView.setIcon(localDrawable);
  }

  public void setLogo(int paramInt)
  {
    Drawable localDrawable = this.mContext.getResources().getDrawable(paramInt);
    setLogo(localDrawable);
  }

  public void setLogo(Drawable paramDrawable)
  {
    this.mLogo = paramDrawable;
    if (paramDrawable == null)
      return;
    if ((this.mDisplayOptions & 0x1) == 0)
      return;
    this.mHomeLayout.setIcon(paramDrawable);
  }

  public void setMenu(SupportMenu paramSupportMenu, MenuPresenter.Callback paramCallback)
  {
    MenuBuilder localMenuBuilder1 = this.mOptionsMenu;
    if (paramSupportMenu == localMenuBuilder1)
      return;
    if (this.mOptionsMenu != null)
    {
      MenuBuilder localMenuBuilder2 = this.mOptionsMenu;
      ActionMenuPresenter localActionMenuPresenter1 = this.mActionMenuPresenter;
      localMenuBuilder2.removeMenuPresenter(localActionMenuPresenter1);
      MenuBuilder localMenuBuilder3 = this.mOptionsMenu;
      ExpandedActionViewMenuPresenter localExpandedActionViewMenuPresenter1 = this.mExpandedMenuPresenter;
      localMenuBuilder3.removeMenuPresenter(localExpandedActionViewMenuPresenter1);
    }
    MenuBuilder localMenuBuilder4 = (MenuBuilder)paramSupportMenu;
    this.mOptionsMenu = localMenuBuilder4;
    ViewGroup localViewGroup;
    if (this.mMenuView != null)
    {
      localViewGroup = (ViewGroup)this.mMenuView.getParent();
      if (localViewGroup != null)
      {
        ActionMenuView localActionMenuView1 = this.mMenuView;
        localViewGroup.removeView(localActionMenuView1);
      }
    }
    if (this.mActionMenuPresenter == null)
    {
      Context localContext = this.mContext;
      ActionMenuPresenter localActionMenuPresenter2 = new ActionMenuPresenter(localContext);
      this.mActionMenuPresenter = localActionMenuPresenter2;
      this.mActionMenuPresenter.setCallback(paramCallback);
      ActionMenuPresenter localActionMenuPresenter3 = this.mActionMenuPresenter;
      int i = R.id.action_menu_presenter;
      localActionMenuPresenter3.setId(i);
      ExpandedActionViewMenuPresenter localExpandedActionViewMenuPresenter2 = new ExpandedActionViewMenuPresenter(null);
      this.mExpandedMenuPresenter = localExpandedActionViewMenuPresenter2;
    }
    ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, -1);
    ActionMenuView localActionMenuView2;
    if (!this.mSplitActionBar)
    {
      ActionMenuPresenter localActionMenuPresenter4 = this.mActionMenuPresenter;
      Resources localResources = getResources();
      int j = R.bool.abc_action_bar_expanded_action_views_exclusive;
      boolean bool = localResources.getBoolean(j);
      localActionMenuPresenter4.setExpandedActionViewsExclusive(bool);
      configPresenters(localMenuBuilder4);
      localActionMenuView2 = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
      localActionMenuView2.initialize(localMenuBuilder4);
      localViewGroup = (ViewGroup)localActionMenuView2.getParent();
      if ((localViewGroup != null) && (localViewGroup != this))
        localViewGroup.removeView(localActionMenuView2);
      addView(localActionMenuView2, localLayoutParams);
    }
    while (true)
    {
      this.mMenuView = localActionMenuView2;
      return;
      this.mActionMenuPresenter.setExpandedActionViewsExclusive(false);
      ActionMenuPresenter localActionMenuPresenter5 = this.mActionMenuPresenter;
      int k = getContext().getResources().getDisplayMetrics().widthPixels;
      localActionMenuPresenter5.setWidthLimit(k, true);
      this.mActionMenuPresenter.setItemLimit(2147483647);
      localLayoutParams.width = -1;
      configPresenters(localMenuBuilder4);
      localActionMenuView2 = (ActionMenuView)this.mActionMenuPresenter.getMenuView(this);
      if (this.mSplitView != null)
      {
        localViewGroup = (ViewGroup)localActionMenuView2.getParent();
        if (localViewGroup != null)
        {
          ActionBarContainer localActionBarContainer = this.mSplitView;
          if (localViewGroup != localActionBarContainer)
            localViewGroup.removeView(localActionMenuView2);
        }
        int m = getAnimatedVisibility();
        localActionMenuView2.setVisibility(m);
        this.mSplitView.addView(localActionMenuView2, localLayoutParams);
      }
      else
      {
        localActionMenuView2.setLayoutParams(localLayoutParams);
      }
    }
  }

  public void setNavigationMode(int paramInt)
  {
    int i = this.mNavigationMode;
    if (paramInt != i)
      return;
    switch (i)
    {
    default:
      switch (paramInt)
      {
      default:
      case 1:
      case 2:
      }
      break;
    case 1:
    case 2:
    }
    while (true)
    {
      this.mNavigationMode = paramInt;
      requestLayout();
      return;
      if (this.mListNavLayout == null)
        break;
      LinearLayout localLinearLayout1 = this.mListNavLayout;
      removeView(localLinearLayout1);
      break;
      if ((this.mTabScrollView == null) || (!this.mIncludeTabs))
        break;
      ScrollingTabContainerView localScrollingTabContainerView1 = this.mTabScrollView;
      removeView(localScrollingTabContainerView1);
      break;
      if (this.mSpinner == null)
      {
        Context localContext = this.mContext;
        int j = R.attr.actionDropDownStyle;
        SpinnerICS localSpinnerICS1 = new SpinnerICS(localContext, null, j);
        this.mSpinner = localSpinnerICS1;
        LayoutInflater localLayoutInflater = LayoutInflater.from(this.mContext);
        int k = R.layout.abc_action_bar_view_list_nav_layout;
        LinearLayout localLinearLayout2 = (LinearLayout)localLayoutInflater.inflate(k, null);
        this.mListNavLayout = localLinearLayout2;
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -1);
        localLayoutParams.gravity = 17;
        LinearLayout localLinearLayout3 = this.mListNavLayout;
        SpinnerICS localSpinnerICS2 = this.mSpinner;
        localLinearLayout3.addView(localSpinnerICS2, localLayoutParams);
      }
      SpinnerAdapter localSpinnerAdapter1 = this.mSpinner.getAdapter();
      SpinnerAdapter localSpinnerAdapter2 = this.mSpinnerAdapter;
      if (localSpinnerAdapter1 != localSpinnerAdapter2)
      {
        SpinnerICS localSpinnerICS3 = this.mSpinner;
        SpinnerAdapter localSpinnerAdapter3 = this.mSpinnerAdapter;
        localSpinnerICS3.setAdapter(localSpinnerAdapter3);
      }
      SpinnerICS localSpinnerICS4 = this.mSpinner;
      AdapterViewICS.OnItemSelectedListener localOnItemSelectedListener = this.mNavItemSelectedListener;
      localSpinnerICS4.setOnItemSelectedListener(localOnItemSelectedListener);
      LinearLayout localLinearLayout4 = this.mListNavLayout;
      addView(localLinearLayout4);
      continue;
      if ((this.mTabScrollView != null) && (this.mIncludeTabs))
      {
        ScrollingTabContainerView localScrollingTabContainerView2 = this.mTabScrollView;
        addView(localScrollingTabContainerView2);
      }
    }
  }

  public void setSplitActionBar(boolean paramBoolean)
  {
    if (this.mSplitActionBar != paramBoolean)
      return;
    int i;
    if (this.mMenuView != null)
    {
      ViewGroup localViewGroup = (ViewGroup)this.mMenuView.getParent();
      if (localViewGroup != null)
      {
        ActionMenuView localActionMenuView1 = this.mMenuView;
        localViewGroup.removeView(localActionMenuView1);
      }
      if (paramBoolean)
      {
        if (this.mSplitView != null)
        {
          ActionBarContainer localActionBarContainer1 = this.mSplitView;
          ActionMenuView localActionMenuView2 = this.mMenuView;
          localActionBarContainer1.addView(localActionMenuView2);
        }
        this.mMenuView.getLayoutParams().width = -1;
        this.mMenuView.requestLayout();
      }
    }
    else
    {
      if (this.mSplitView != null)
      {
        ActionBarContainer localActionBarContainer2 = this.mSplitView;
        if (!paramBoolean)
          break label194;
        i = 0;
        label110: localActionBarContainer2.setVisibility(i);
      }
      if (this.mActionMenuPresenter != null)
      {
        if (paramBoolean)
          break label201;
        ActionMenuPresenter localActionMenuPresenter1 = this.mActionMenuPresenter;
        Resources localResources = getResources();
        int j = R.bool.abc_action_bar_expanded_action_views_exclusive;
        boolean bool = localResources.getBoolean(j);
        localActionMenuPresenter1.setExpandedActionViewsExclusive(bool);
      }
    }
    while (true)
    {
      super.setSplitActionBar(paramBoolean);
      return;
      ActionMenuView localActionMenuView3 = this.mMenuView;
      addView(localActionMenuView3);
      this.mMenuView.getLayoutParams().width = -1;
      break;
      label194: i = 8;
      break label110;
      label201: this.mActionMenuPresenter.setExpandedActionViewsExclusive(false);
      ActionMenuPresenter localActionMenuPresenter2 = this.mActionMenuPresenter;
      int k = getContext().getResources().getDisplayMetrics().widthPixels;
      localActionMenuPresenter2.setWidthLimit(k, true);
      this.mActionMenuPresenter.setItemLimit(2147483647);
    }
  }

  public void setSubtitle(CharSequence paramCharSequence)
  {
    int i = 0;
    this.mSubtitle = paramCharSequence;
    if (this.mSubtitleView == null)
      return;
    this.mSubtitleView.setText(paramCharSequence);
    TextView localTextView = this.mSubtitleView;
    int j;
    int k;
    label81: LinearLayout localLinearLayout;
    if (paramCharSequence != null)
    {
      j = 0;
      localTextView.setVisibility(j);
      if ((this.mExpandedActionView != null) || ((this.mDisplayOptions & 0x8) == 0) || ((TextUtils.isEmpty(this.mTitle)) && (TextUtils.isEmpty(this.mSubtitle))))
        break label106;
      k = 1;
      localLinearLayout = this.mTitleLayout;
      if (k == 0)
        break label112;
    }
    while (true)
    {
      localLinearLayout.setVisibility(i);
      return;
      j = 8;
      break;
      label106: k = 0;
      break label81;
      label112: i = 8;
    }
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    this.mUserTitle = true;
    setTitleImpl(paramCharSequence);
  }

  public void setWindowCallback(Window.Callback paramCallback)
  {
    this.mWindowCallback = paramCallback;
  }

  public void setWindowTitle(CharSequence paramCharSequence)
  {
    if (this.mUserTitle)
      return;
    setTitleImpl(paramCharSequence);
  }

  public boolean shouldDelayChildPressedState()
  {
    return false;
  }

  private class ExpandedActionViewMenuPresenter
    implements MenuPresenter
  {
    MenuItemImpl mCurrentExpandedItem;
    MenuBuilder mMenu;

    private ExpandedActionViewMenuPresenter()
    {
    }

    public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
    {
      if ((ActionBarView.this.mExpandedActionView instanceof CollapsibleActionView))
        ((CollapsibleActionView)ActionBarView.this.mExpandedActionView).onActionViewCollapsed();
      ActionBarView localActionBarView1 = ActionBarView.this;
      View localView = ActionBarView.this.mExpandedActionView;
      localActionBarView1.removeView(localView);
      ActionBarView localActionBarView2 = ActionBarView.this;
      ActionBarView.HomeView localHomeView = ActionBarView.this.mExpandedHomeLayout;
      localActionBarView2.removeView(localHomeView);
      ActionBarView.this.mExpandedActionView = null;
      if ((ActionBarView.this.mDisplayOptions & 0x2) != 0)
        ActionBarView.this.mHomeLayout.setVisibility(0);
      if ((ActionBarView.this.mDisplayOptions & 0x8) != 0)
      {
        if (ActionBarView.this.mTitleLayout != null)
          break label259;
        ActionBarView.this.initTitle();
      }
      while (true)
      {
        if ((ActionBarView.this.mTabScrollView != null) && (ActionBarView.this.mNavigationMode == 2))
          ActionBarView.this.mTabScrollView.setVisibility(0);
        if ((ActionBarView.this.mSpinner != null) && (ActionBarView.this.mNavigationMode == 1))
          ActionBarView.this.mSpinner.setVisibility(0);
        if ((ActionBarView.this.mCustomNavView != null) && ((ActionBarView.this.mDisplayOptions & 0x10) != 0))
          ActionBarView.this.mCustomNavView.setVisibility(0);
        ActionBarView.this.mExpandedHomeLayout.setIcon(null);
        this.mCurrentExpandedItem = null;
        ActionBarView.this.requestLayout();
        paramMenuItemImpl.setActionViewExpanded(false);
        return true;
        label259: ActionBarView.this.mTitleLayout.setVisibility(0);
      }
    }

    public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
    {
      ActionBarView localActionBarView1 = ActionBarView.this;
      View localView1 = paramMenuItemImpl.getActionView();
      localActionBarView1.mExpandedActionView = localView1;
      ActionBarView.HomeView localHomeView1 = ActionBarView.this.mExpandedHomeLayout;
      Drawable.ConstantState localConstantState = ActionBarView.this.mIcon.getConstantState();
      Resources localResources = ActionBarView.this.getResources();
      Drawable localDrawable = localConstantState.newDrawable(localResources);
      localHomeView1.setIcon(localDrawable);
      this.mCurrentExpandedItem = paramMenuItemImpl;
      ViewParent localViewParent1 = ActionBarView.this.mExpandedActionView.getParent();
      ActionBarView localActionBarView2 = ActionBarView.this;
      if (localViewParent1 != localActionBarView2)
      {
        ActionBarView localActionBarView3 = ActionBarView.this;
        View localView2 = ActionBarView.this.mExpandedActionView;
        localActionBarView3.addView(localView2);
      }
      ViewParent localViewParent2 = ActionBarView.this.mExpandedHomeLayout.getParent();
      ActionBarView localActionBarView4 = ActionBarView.this;
      if (localViewParent2 != localActionBarView4)
      {
        ActionBarView localActionBarView5 = ActionBarView.this;
        ActionBarView.HomeView localHomeView2 = ActionBarView.this.mExpandedHomeLayout;
        localActionBarView5.addView(localHomeView2);
      }
      ActionBarView.this.mHomeLayout.setVisibility(8);
      if (ActionBarView.this.mTitleLayout != null)
        ActionBarView.this.mTitleLayout.setVisibility(8);
      if (ActionBarView.this.mTabScrollView != null)
        ActionBarView.this.mTabScrollView.setVisibility(8);
      if (ActionBarView.this.mSpinner != null)
        ActionBarView.this.mSpinner.setVisibility(8);
      if (ActionBarView.this.mCustomNavView != null)
        ActionBarView.this.mCustomNavView.setVisibility(8);
      ActionBarView.this.requestLayout();
      paramMenuItemImpl.setActionViewExpanded(true);
      if ((ActionBarView.this.mExpandedActionView instanceof CollapsibleActionView))
        ((CollapsibleActionView)ActionBarView.this.mExpandedActionView).onActionViewExpanded();
      return true;
    }

    public boolean flagActionItems()
    {
      return false;
    }

    public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder)
    {
      if ((this.mMenu != null) && (this.mCurrentExpandedItem != null))
      {
        MenuBuilder localMenuBuilder = this.mMenu;
        MenuItemImpl localMenuItemImpl = this.mCurrentExpandedItem;
        boolean bool = localMenuBuilder.collapseItemActionView(localMenuItemImpl);
      }
      this.mMenu = paramMenuBuilder;
    }

    public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
    {
    }

    public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
    {
      return false;
    }

    public void updateMenuView(boolean paramBoolean)
    {
      if (this.mCurrentExpandedItem == null)
        return;
      int i = 0;
      int j;
      int k;
      if (this.mMenu != null)
      {
        j = this.mMenu.size();
        k = 0;
      }
      while (true)
      {
        if (k < j)
        {
          SupportMenuItem localSupportMenuItem = (SupportMenuItem)this.mMenu.getItem(k);
          MenuItemImpl localMenuItemImpl1 = this.mCurrentExpandedItem;
          if (localSupportMenuItem == localMenuItemImpl1)
            i = 1;
        }
        else
        {
          if (i != 0)
            return;
          MenuBuilder localMenuBuilder = this.mMenu;
          MenuItemImpl localMenuItemImpl2 = this.mCurrentExpandedItem;
          boolean bool = collapseItemActionView(localMenuBuilder, localMenuItemImpl2);
          return;
        }
        k += 1;
      }
    }
  }

  private static class HomeView extends FrameLayout
  {
    private Drawable mDefaultUpIndicator;
    private ImageView mIconView;
    private int mUpIndicatorRes;
    private ImageView mUpView;
    private int mUpWidth;

    public HomeView(Context paramContext)
    {
      this(paramContext, null);
    }

    public HomeView(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
    {
      CharSequence localCharSequence = getContentDescription();
      if (!TextUtils.isEmpty(localCharSequence))
        boolean bool = paramAccessibilityEvent.getText().add(localCharSequence);
      return true;
    }

    public int getLeftOffset()
    {
      if (this.mUpView.getVisibility() == 8);
      for (int i = this.mUpWidth; ; i = 0)
        return i;
    }

    protected void onConfigurationChanged(Configuration paramConfiguration)
    {
      super.onConfigurationChanged(paramConfiguration);
      if (this.mUpIndicatorRes == 0)
        return;
      int i = this.mUpIndicatorRes;
      setUpIndicator(i);
    }

    protected void onFinishInflate()
    {
      int i = R.id.up;
      ImageView localImageView1 = (ImageView)findViewById(i);
      this.mUpView = localImageView1;
      int j = R.id.home;
      ImageView localImageView2 = (ImageView)findViewById(j);
      this.mIconView = localImageView2;
      Drawable localDrawable = this.mUpView.getDrawable();
      this.mDefaultUpIndicator = localDrawable;
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      int i = (paramInt4 - paramInt2) / 2;
      int j = paramInt3 - paramInt1;
      int k = 0;
      if (this.mUpView.getVisibility() != 8)
      {
        FrameLayout.LayoutParams localLayoutParams1 = (FrameLayout.LayoutParams)this.mUpView.getLayoutParams();
        int m = this.mUpView.getMeasuredHeight();
        int n = this.mUpView.getMeasuredWidth();
        int i1 = m / 2;
        int i2 = i - i1;
        ImageView localImageView1 = this.mUpView;
        int i3 = i2 + m;
        localImageView1.layout(0, i2, n, i3);
        int i4 = localLayoutParams1.leftMargin + n;
        int i5 = localLayoutParams1.rightMargin;
        k = i4 + i5;
        int i6 = j - k;
        paramInt1 += k;
      }
      FrameLayout.LayoutParams localLayoutParams2 = (FrameLayout.LayoutParams)this.mIconView.getLayoutParams();
      int i7 = this.mIconView.getMeasuredHeight();
      int i8 = this.mIconView.getMeasuredWidth();
      int i9 = (paramInt3 - paramInt1) / 2;
      int i10 = localLayoutParams2.leftMargin;
      int i11 = i8 / 2;
      int i12 = i9 - i11;
      int i13 = Math.max(i10, i12);
      int i14 = k + i13;
      int i15 = localLayoutParams2.topMargin;
      int i16 = i7 / 2;
      int i17 = i - i16;
      int i18 = Math.max(i15, i17);
      ImageView localImageView2 = this.mIconView;
      int i19 = i14 + i8;
      int i20 = i18 + i7;
      localImageView2.layout(i14, i18, i19, i20);
    }

    protected void onMeasure(int paramInt1, int paramInt2)
    {
      ImageView localImageView1 = this.mUpView;
      HomeView localHomeView1 = this;
      int i = paramInt1;
      int j = paramInt2;
      localHomeView1.measureChildWithMargins(localImageView1, i, 0, j, 0);
      FrameLayout.LayoutParams localLayoutParams1 = (FrameLayout.LayoutParams)this.mUpView.getLayoutParams();
      int k = localLayoutParams1.leftMargin;
      int m = this.mUpView.getMeasuredWidth();
      int n = k + m;
      int i1 = localLayoutParams1.rightMargin;
      int i2 = n + i1;
      this.mUpWidth = i2;
      int i3;
      int i16;
      int i22;
      int i25;
      int i26;
      if (this.mUpView.getVisibility() == 8)
      {
        i3 = 0;
        int i4 = localLayoutParams1.topMargin;
        int i5 = this.mUpView.getMeasuredHeight();
        int i6 = i4 + i5;
        int i7 = localLayoutParams1.bottomMargin;
        int i8 = i6 + i7;
        ImageView localImageView2 = this.mIconView;
        HomeView localHomeView2 = this;
        int i9 = paramInt1;
        int i10 = paramInt2;
        localHomeView2.measureChildWithMargins(localImageView2, i9, i3, i10, 0);
        FrameLayout.LayoutParams localLayoutParams2 = (FrameLayout.LayoutParams)this.mIconView.getLayoutParams();
        int i11 = localLayoutParams2.leftMargin;
        int i12 = this.mIconView.getMeasuredWidth();
        int i13 = i11 + i12;
        int i14 = localLayoutParams2.rightMargin;
        int i15 = i13 + i14;
        i16 = i3 + i15;
        int i17 = localLayoutParams2.topMargin;
        int i18 = this.mIconView.getMeasuredHeight();
        int i19 = i17 + i18;
        int i20 = localLayoutParams2.bottomMargin;
        int i21 = i19 + i20;
        i22 = Math.max(i8, i21);
        int i23 = View.MeasureSpec.getMode(paramInt1);
        int i24 = View.MeasureSpec.getMode(paramInt2);
        i25 = View.MeasureSpec.getSize(paramInt1);
        i26 = View.MeasureSpec.getSize(paramInt2);
        switch (i23)
        {
        default:
          label316: switch (i24)
          {
          default:
          case -2147483648:
          case 1073741824:
          }
          break;
        case -2147483648:
        case 1073741824:
        }
      }
      while (true)
      {
        setMeasuredDimension(i16, i22);
        return;
        i3 = this.mUpWidth;
        break;
        i16 = Math.min(i16, i25);
        break label316;
        i16 = i25;
        break label316;
        i22 = Math.min(i22, i26);
        continue;
        i22 = i26;
      }
    }

    public void setIcon(Drawable paramDrawable)
    {
      this.mIconView.setImageDrawable(paramDrawable);
    }

    public void setUp(boolean paramBoolean)
    {
      ImageView localImageView = this.mUpView;
      if (paramBoolean);
      for (int i = 0; ; i = 8)
      {
        localImageView.setVisibility(i);
        return;
      }
    }

    public void setUpIndicator(int paramInt)
    {
      this.mUpIndicatorRes = paramInt;
      ImageView localImageView = this.mUpView;
      if (paramInt != 0);
      for (Drawable localDrawable = getResources().getDrawable(paramInt); ; localDrawable = null)
      {
        localImageView.setImageDrawable(localDrawable);
        return;
      }
    }

    public void setUpIndicator(Drawable paramDrawable)
    {
      ImageView localImageView = this.mUpView;
      if (paramDrawable != null);
      while (true)
      {
        localImageView.setImageDrawable(paramDrawable);
        this.mUpIndicatorRes = 0;
        return;
        paramDrawable = this.mDefaultUpIndicator;
      }
    }
  }

  static class SavedState extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public ActionBarView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new ActionBarView.SavedState(paramAnonymousParcel, null);
      }

      public ActionBarView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new ActionBarView.SavedState[paramAnonymousInt];
      }
    };
    int expandedMenuItemId;
    boolean isOverflowOpen;

    private SavedState(Parcel paramParcel)
    {
      super();
      int i = paramParcel.readInt();
      this.expandedMenuItemId = i;
      if (paramParcel.readInt() != 0);
      for (boolean bool = true; ; bool = false)
      {
        this.isOverflowOpen = bool;
        return;
      }
    }

    SavedState(Parcelable paramParcelable)
    {
      super();
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      int i = this.expandedMenuItemId;
      paramParcel.writeInt(i);
      if (this.isOverflowOpen);
      for (int j = 1; ; j = 0)
      {
        paramParcel.writeInt(j);
        return;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.ActionBarView
 * JD-Core Version:    0.6.2
 */