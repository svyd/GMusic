package android.support.v7.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.appcompat.R.anim;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.id;
import android.support.v7.internal.view.ActionBarPolicy;
import android.support.v7.internal.widget.ActionBarContainer;
import android.support.v7.internal.widget.ActionBarContextView;
import android.support.v7.internal.widget.ActionBarOverlayLayout;
import android.support.v7.internal.widget.ActionBarView;
import android.support.v7.internal.widget.ScrollingTabContainerView;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SpinnerAdapter;
import java.util.ArrayList;

class ActionBarImplBase extends ActionBar
{
  private ActionBarView mActionView;
  private ActionBarActivity mActivity;
  private ActionBar.Callback mCallback;
  private ActionBarContainer mContainerView;
  private Context mContext;
  private int mContextDisplayMode;
  private ActionBarContextView mContextView;
  private int mCurWindowVisibility;
  private boolean mDisplayHomeAsUpSet;
  final Handler mHandler;
  private boolean mHasEmbeddedTabs;
  private boolean mHiddenByApp;
  private boolean mHiddenBySystem;
  private ArrayList<ActionBar.OnMenuVisibilityListener> mMenuVisibilityListeners;
  private boolean mNowShowing;
  private ActionBarOverlayLayout mOverlayLayout;
  private int mSavedTabPosition;
  private TabImpl mSelectedTab;
  private boolean mShowHideAnimationEnabled;
  private boolean mShowingForMode;
  private ActionBarContainer mSplitView;
  private ScrollingTabContainerView mTabScrollView;
  private ArrayList<TabImpl> mTabs;
  private Context mThemedContext;
  private ViewGroup mTopVisibilityView;

  public ActionBarImplBase(ActionBarActivity paramActionBarActivity, ActionBar.Callback paramCallback)
  {
    ArrayList localArrayList1 = new ArrayList();
    this.mTabs = localArrayList1;
    this.mSavedTabPosition = -1;
    ArrayList localArrayList2 = new ArrayList();
    this.mMenuVisibilityListeners = localArrayList2;
    Handler localHandler = new Handler();
    this.mHandler = localHandler;
    this.mCurWindowVisibility = 0;
    this.mNowShowing = true;
    this.mActivity = paramActionBarActivity;
    this.mContext = paramActionBarActivity;
    this.mCallback = paramCallback;
    ActionBarActivity localActionBarActivity = this.mActivity;
    init(localActionBarActivity);
  }

  private static boolean checkShowingFlags(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    boolean bool = true;
    if (paramBoolean3);
    while (true)
    {
      return bool;
      if ((paramBoolean1) || (paramBoolean2))
        bool = false;
    }
  }

  private void ensureTabsExist()
  {
    if (this.mTabScrollView != null)
      return;
    Context localContext = this.mContext;
    ScrollingTabContainerView localScrollingTabContainerView = new ScrollingTabContainerView(localContext);
    if (this.mHasEmbeddedTabs)
    {
      localScrollingTabContainerView.setVisibility(0);
      this.mActionView.setEmbeddedTabView(localScrollingTabContainerView);
      this.mTabScrollView = localScrollingTabContainerView;
      return;
    }
    if (getNavigationMode() == 2)
      localScrollingTabContainerView.setVisibility(0);
    while (true)
    {
      this.mContainerView.setTabContainer(localScrollingTabContainerView);
      break;
      localScrollingTabContainerView.setVisibility(8);
    }
  }

  private void init(ActionBarActivity paramActionBarActivity)
  {
    boolean bool1 = false;
    int i = R.id.action_bar_overlay_layout;
    ActionBarOverlayLayout localActionBarOverlayLayout = (ActionBarOverlayLayout)paramActionBarActivity.findViewById(i);
    this.mOverlayLayout = localActionBarOverlayLayout;
    if (this.mOverlayLayout != null)
      this.mOverlayLayout.setActionBar(this);
    int j = R.id.action_bar;
    ActionBarView localActionBarView1 = (ActionBarView)paramActionBarActivity.findViewById(j);
    this.mActionView = localActionBarView1;
    int k = R.id.action_context_bar;
    ActionBarContextView localActionBarContextView1 = (ActionBarContextView)paramActionBarActivity.findViewById(k);
    this.mContextView = localActionBarContextView1;
    int m = R.id.action_bar_container;
    ActionBarContainer localActionBarContainer1 = (ActionBarContainer)paramActionBarActivity.findViewById(m);
    this.mContainerView = localActionBarContainer1;
    int n = R.id.top_action_bar;
    ViewGroup localViewGroup = (ViewGroup)paramActionBarActivity.findViewById(n);
    this.mTopVisibilityView = localViewGroup;
    if (this.mTopVisibilityView == null)
    {
      ActionBarContainer localActionBarContainer2 = this.mContainerView;
      this.mTopVisibilityView = localActionBarContainer2;
    }
    int i1 = R.id.split_action_bar;
    ActionBarContainer localActionBarContainer3 = (ActionBarContainer)paramActionBarActivity.findViewById(i1);
    this.mSplitView = localActionBarContainer3;
    if ((this.mActionView == null) || (this.mContextView == null) || (this.mContainerView == null))
    {
      StringBuilder localStringBuilder = new StringBuilder();
      String str1 = getClass().getSimpleName();
      String str2 = str1 + " can only be used " + "with a compatible window decor layout";
      throw new IllegalStateException(str2);
    }
    ActionBarView localActionBarView2 = this.mActionView;
    ActionBarContextView localActionBarContextView2 = this.mContextView;
    localActionBarView2.setContextView(localActionBarContextView2);
    int i2;
    if (this.mActionView.isSplitActionBar())
    {
      i2 = 1;
      this.mContextDisplayMode = i2;
      if ((this.mActionView.getDisplayOptions() & 0x4) == 0)
        break label364;
    }
    label364: for (int i3 = 1; ; i3 = 0)
    {
      if (i3 != 0)
        this.mDisplayHomeAsUpSet = true;
      ActionBarPolicy localActionBarPolicy = ActionBarPolicy.get(this.mContext);
      if ((localActionBarPolicy.enableHomeButtonByDefault()) || (i3 != 0))
        bool1 = true;
      setHomeButtonEnabled(bool1);
      boolean bool2 = localActionBarPolicy.hasEmbeddedTabs();
      setHasEmbeddedTabs(bool2);
      CharSequence localCharSequence = this.mActivity.getTitle();
      setTitle(localCharSequence);
      return;
      i2 = 0;
      break;
    }
  }

  private void setHasEmbeddedTabs(boolean paramBoolean)
  {
    boolean bool = true;
    this.mHasEmbeddedTabs = paramBoolean;
    int i;
    label50: label70: ActionBarView localActionBarView1;
    if (!this.mHasEmbeddedTabs)
    {
      this.mActionView.setEmbeddedTabView(null);
      ActionBarContainer localActionBarContainer = this.mContainerView;
      ScrollingTabContainerView localScrollingTabContainerView1 = this.mTabScrollView;
      localActionBarContainer.setTabContainer(localScrollingTabContainerView1);
      if (getNavigationMode() != 2)
        break label125;
      i = 1;
      if (this.mTabScrollView != null)
      {
        if (i == 0)
          break label131;
        this.mTabScrollView.setVisibility(0);
      }
      localActionBarView1 = this.mActionView;
      if ((this.mHasEmbeddedTabs) || (i == 0))
        break label143;
    }
    while (true)
    {
      localActionBarView1.setCollapsable(bool);
      return;
      this.mContainerView.setTabContainer(null);
      ActionBarView localActionBarView2 = this.mActionView;
      ScrollingTabContainerView localScrollingTabContainerView2 = this.mTabScrollView;
      localActionBarView2.setEmbeddedTabView(localScrollingTabContainerView2);
      break;
      label125: i = 0;
      break label50;
      label131: this.mTabScrollView.setVisibility(8);
      break label70;
      label143: bool = false;
    }
  }

  private void updateVisibility(boolean paramBoolean)
  {
    boolean bool1 = this.mHiddenByApp;
    boolean bool2 = this.mHiddenBySystem;
    boolean bool3 = this.mShowingForMode;
    if (checkShowingFlags(bool1, bool2, bool3))
    {
      if (this.mNowShowing)
        return;
      this.mNowShowing = true;
      doShow(paramBoolean);
      return;
    }
    if (!this.mNowShowing)
      return;
    this.mNowShowing = false;
    doHide(paramBoolean);
  }

  public void doHide(boolean paramBoolean)
  {
    this.mTopVisibilityView.clearAnimation();
    if (this.mTopVisibilityView.getVisibility() == 8)
      return;
    if ((isShowHideAnimationEnabled()) || (paramBoolean));
    for (int i = 1; ; i = 0)
    {
      if (i != 0)
      {
        Context localContext1 = this.mContext;
        int j = R.anim.abc_slide_out_top;
        Animation localAnimation1 = AnimationUtils.loadAnimation(localContext1, j);
        this.mTopVisibilityView.startAnimation(localAnimation1);
      }
      this.mTopVisibilityView.setVisibility(8);
      if (this.mSplitView == null)
        return;
      if (this.mSplitView.getVisibility() == 8)
        return;
      if (i != 0)
      {
        Context localContext2 = this.mContext;
        int k = R.anim.abc_slide_out_bottom;
        Animation localAnimation2 = AnimationUtils.loadAnimation(localContext2, k);
        this.mSplitView.startAnimation(localAnimation2);
      }
      this.mSplitView.setVisibility(8);
      return;
    }
  }

  public void doShow(boolean paramBoolean)
  {
    this.mTopVisibilityView.clearAnimation();
    if (this.mTopVisibilityView.getVisibility() == 0)
      return;
    if ((isShowHideAnimationEnabled()) || (paramBoolean));
    for (int i = 1; ; i = 0)
    {
      if (i != 0)
      {
        Context localContext1 = this.mContext;
        int j = R.anim.abc_slide_in_top;
        Animation localAnimation1 = AnimationUtils.loadAnimation(localContext1, j);
        this.mTopVisibilityView.startAnimation(localAnimation1);
      }
      this.mTopVisibilityView.setVisibility(0);
      if (this.mSplitView == null)
        return;
      if (this.mSplitView.getVisibility() == 0)
        return;
      if (i != 0)
      {
        Context localContext2 = this.mContext;
        int k = R.anim.abc_slide_in_bottom;
        Animation localAnimation2 = AnimationUtils.loadAnimation(localContext2, k);
        this.mSplitView.startAnimation(localAnimation2);
      }
      this.mSplitView.setVisibility(0);
      return;
    }
  }

  public int getDisplayOptions()
  {
    return this.mActionView.getDisplayOptions();
  }

  public int getNavigationMode()
  {
    return this.mActionView.getNavigationMode();
  }

  public int getSelectedNavigationIndex()
  {
    int i = -1;
    switch (this.mActionView.getNavigationMode())
    {
    default:
    case 2:
    case 1:
    }
    while (true)
    {
      return i;
      if (this.mSelectedTab != null)
      {
        i = this.mSelectedTab.getPosition();
        continue;
        i = this.mActionView.getDropdownSelectedPosition();
      }
    }
  }

  public Context getThemedContext()
  {
    ContextThemeWrapper localContextThemeWrapper;
    if (this.mThemedContext == null)
    {
      TypedValue localTypedValue = new TypedValue();
      Resources.Theme localTheme = this.mContext.getTheme();
      int i = R.attr.actionBarWidgetTheme;
      boolean bool = localTheme.resolveAttribute(i, localTypedValue, true);
      int j = localTypedValue.resourceId;
      if (j == 0)
        break label77;
      Context localContext1 = this.mContext;
      localContextThemeWrapper = new ContextThemeWrapper(localContext1, j);
    }
    label77: Context localContext2;
    for (this.mThemedContext = localContextThemeWrapper; ; this.mThemedContext = localContext2)
    {
      return this.mThemedContext;
      localContext2 = this.mContext;
    }
  }

  public void hide()
  {
    if (this.mHiddenByApp)
      return;
    this.mHiddenByApp = true;
    updateVisibility(false);
  }

  void hideForActionMode()
  {
    if (!this.mShowingForMode)
      return;
    this.mShowingForMode = false;
    updateVisibility(false);
  }

  boolean isShowHideAnimationEnabled()
  {
    return this.mShowHideAnimationEnabled;
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    boolean bool = ActionBarPolicy.get(this.mContext).hasEmbeddedTabs();
    setHasEmbeddedTabs(bool);
  }

  public void selectTab(ActionBar.Tab paramTab)
  {
    int i = -1;
    if (getNavigationMode() != 2)
    {
      if (paramTab != null)
        i = paramTab.getPosition();
      this.mSavedTabPosition = i;
      return;
    }
    FragmentTransaction localFragmentTransaction = this.mActivity.getSupportFragmentManager().beginTransaction().disallowAddToBackStack();
    if (this.mSelectedTab == paramTab)
      if (this.mSelectedTab != null)
      {
        ActionBar.TabListener localTabListener1 = this.mSelectedTab.getCallback();
        TabImpl localTabImpl1 = this.mSelectedTab;
        localTabListener1.onTabReselected(localTabImpl1, localFragmentTransaction);
        ScrollingTabContainerView localScrollingTabContainerView1 = this.mTabScrollView;
        int j = paramTab.getPosition();
        localScrollingTabContainerView1.animateToTab(j);
      }
    while (true)
    {
      if (localFragmentTransaction.isEmpty())
        return;
      int k = localFragmentTransaction.commit();
      return;
      ScrollingTabContainerView localScrollingTabContainerView2 = this.mTabScrollView;
      if (paramTab != null)
        i = paramTab.getPosition();
      localScrollingTabContainerView2.setTabSelected(i);
      if (this.mSelectedTab != null)
      {
        ActionBar.TabListener localTabListener2 = this.mSelectedTab.getCallback();
        TabImpl localTabImpl2 = this.mSelectedTab;
        localTabListener2.onTabUnselected(localTabImpl2, localFragmentTransaction);
      }
      TabImpl localTabImpl3 = (TabImpl)paramTab;
      this.mSelectedTab = localTabImpl3;
      if (this.mSelectedTab != null)
      {
        ActionBar.TabListener localTabListener3 = this.mSelectedTab.getCallback();
        TabImpl localTabImpl4 = this.mSelectedTab;
        localTabListener3.onTabSelected(localTabImpl4, localFragmentTransaction);
      }
    }
  }

  public void setBackgroundDrawable(Drawable paramDrawable)
  {
    this.mContainerView.setPrimaryBackground(paramDrawable);
  }

  public void setCustomView(View paramView, ActionBar.LayoutParams paramLayoutParams)
  {
    paramView.setLayoutParams(paramLayoutParams);
    this.mActionView.setCustomNavigationView(paramView);
  }

  public void setDisplayHomeAsUpEnabled(boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = 4; ; i = 0)
    {
      setDisplayOptions(i, 4);
      return;
    }
  }

  public void setDisplayOptions(int paramInt1, int paramInt2)
  {
    int i = this.mActionView.getDisplayOptions();
    if ((paramInt2 & 0x4) != 0)
      this.mDisplayHomeAsUpSet = true;
    ActionBarView localActionBarView = this.mActionView;
    int j = paramInt1 & paramInt2;
    int k = (paramInt2 ^ 0xFFFFFFFF) & i;
    int m = j | k;
    localActionBarView.setDisplayOptions(m);
  }

  public void setDisplayShowCustomEnabled(boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = 16; ; i = 0)
    {
      setDisplayOptions(i, 16);
      return;
    }
  }

  public void setDisplayShowHomeEnabled(boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = 2; ; i = 0)
    {
      setDisplayOptions(i, 2);
      return;
    }
  }

  public void setHomeButtonEnabled(boolean paramBoolean)
  {
    this.mActionView.setHomeButtonEnabled(paramBoolean);
  }

  public void setListNavigationCallbacks(SpinnerAdapter paramSpinnerAdapter, ActionBar.OnNavigationListener paramOnNavigationListener)
  {
    this.mActionView.setDropdownAdapter(paramSpinnerAdapter);
    this.mActionView.setCallback(paramOnNavigationListener);
  }

  public void setNavigationMode(int paramInt)
  {
    boolean bool = false;
    switch (this.mActionView.getNavigationMode())
    {
    default:
      this.mActionView.setNavigationMode(paramInt);
      switch (paramInt)
      {
      default:
      case 2:
      }
      break;
    case 2:
    }
    while (true)
    {
      ActionBarView localActionBarView = this.mActionView;
      if ((paramInt == 2) && (!this.mHasEmbeddedTabs))
        bool = true;
      localActionBarView.setCollapsable(bool);
      return;
      int i = getSelectedNavigationIndex();
      this.mSavedTabPosition = i;
      selectTab(null);
      this.mTabScrollView.setVisibility(8);
      break;
      ensureTabsExist();
      this.mTabScrollView.setVisibility(0);
      if (this.mSavedTabPosition != -1)
      {
        int j = this.mSavedTabPosition;
        setSelectedNavigationItem(j);
        this.mSavedTabPosition = -1;
      }
    }
  }

  public void setSelectedNavigationItem(int paramInt)
  {
    switch (this.mActionView.getNavigationMode())
    {
    default:
      throw new IllegalStateException("setSelectedNavigationIndex not valid for current navigation mode");
    case 2:
      ActionBar.Tab localTab = (ActionBar.Tab)this.mTabs.get(paramInt);
      selectTab(localTab);
      return;
    case 1:
    }
    this.mActionView.setDropdownSelectedPosition(paramInt);
  }

  public void setShowHideAnimationEnabled(boolean paramBoolean)
  {
    this.mShowHideAnimationEnabled = paramBoolean;
    if (paramBoolean)
      return;
    this.mTopVisibilityView.clearAnimation();
    if (this.mSplitView == null)
      return;
    this.mSplitView.clearAnimation();
  }

  public void setTitle(int paramInt)
  {
    String str = this.mContext.getString(paramInt);
    setTitle(str);
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    this.mActionView.setTitle(paramCharSequence);
  }

  public void show()
  {
    if (!this.mHiddenByApp)
      return;
    this.mHiddenByApp = false;
    updateVisibility(false);
  }

  void showForActionMode()
  {
    if (this.mShowingForMode)
      return;
    this.mShowingForMode = true;
    updateVisibility(false);
  }

  public class TabImpl extends ActionBar.Tab
  {
    private ActionBar.TabListener mCallback;
    private CharSequence mContentDesc;
    private View mCustomView;
    private Drawable mIcon;
    private int mPosition;
    private CharSequence mText;

    public ActionBar.TabListener getCallback()
    {
      return this.mCallback;
    }

    public CharSequence getContentDescription()
    {
      return this.mContentDesc;
    }

    public View getCustomView()
    {
      return this.mCustomView;
    }

    public Drawable getIcon()
    {
      return this.mIcon;
    }

    public int getPosition()
    {
      return this.mPosition;
    }

    public CharSequence getText()
    {
      return this.mText;
    }

    public void select()
    {
      this.this$0.selectTab(this);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.ActionBarImplBase
 * JD-Core Version:    0.6.2
 */