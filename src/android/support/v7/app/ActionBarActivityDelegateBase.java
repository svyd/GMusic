package android.support.v7.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActionBarDrawerToggle.Delegate;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.bool;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.layout;
import android.support.v7.appcompat.R.style;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.internal.view.menu.ListMenuPresenter;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.internal.view.menu.MenuBuilder.Callback;
import android.support.v7.internal.view.menu.MenuPresenter.Callback;
import android.support.v7.internal.view.menu.MenuView;
import android.support.v7.internal.view.menu.MenuWrapperFactory;
import android.support.v7.internal.widget.ActionBarContainer;
import android.support.v7.internal.widget.ActionBarContextView;
import android.support.v7.internal.widget.ActionBarView;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.FrameLayout;

class ActionBarActivityDelegateBase extends ActionBarActivityDelegate
  implements MenuBuilder.Callback, MenuPresenter.Callback
{
  private static final int[] ACTION_BAR_DRAWABLE_TOGGLE_ATTRS = arrayOfInt;
  private ActionBarView mActionBarView;
  private ActionMode mActionMode;
  private boolean mFeatureIndeterminateProgress;
  private boolean mFeatureProgress;
  private boolean mInvalidateMenuPosted;
  private final Runnable mInvalidateMenuRunnable;
  private ListMenuPresenter mListMenuPresenter;
  private MenuBuilder mMenu;
  private boolean mSubDecorInstalled;
  private CharSequence mTitleToSet;

  static
  {
    int[] arrayOfInt = new int[1];
    int i = R.attr.homeAsUpIndicator;
    arrayOfInt[0] = i;
  }

  ActionBarActivityDelegateBase(ActionBarActivity paramActionBarActivity)
  {
    super(paramActionBarActivity);
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        MenuBuilder localMenuBuilder = ActionBarActivityDelegateBase.this.createMenu();
        if ((ActionBarActivityDelegateBase.this.mActivity.superOnCreatePanelMenu(0, localMenuBuilder)) && (ActionBarActivityDelegateBase.this.mActivity.superOnPreparePanel(0, null, localMenuBuilder)))
          ActionBarActivityDelegateBase.this.setMenu(localMenuBuilder);
        while (true)
        {
          boolean bool = ActionBarActivityDelegateBase.access$202(ActionBarActivityDelegateBase.this, false);
          return;
          ActionBarActivityDelegateBase.this.setMenu(null);
        }
      }
    };
    this.mInvalidateMenuRunnable = local1;
  }

  private MenuBuilder createMenu()
  {
    Context localContext = getActionBarThemedContext();
    MenuBuilder localMenuBuilder = new MenuBuilder(localContext);
    localMenuBuilder.setCallback(this);
    return localMenuBuilder;
  }

  private MenuView getListMenuView(Context paramContext, MenuPresenter.Callback paramCallback)
  {
    MenuView localMenuView;
    if (this.mMenu == null)
    {
      localMenuView = null;
      return localMenuView;
    }
    if (this.mListMenuPresenter == null)
    {
      int[] arrayOfInt = R.styleable.Theme;
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(arrayOfInt);
      int i = R.style.Theme_AppCompat_CompactMenu;
      int j = localTypedArray.getResourceId(4, i);
      localTypedArray.recycle();
      int k = R.layout.abc_list_menu_item_layout;
      ListMenuPresenter localListMenuPresenter1 = new ListMenuPresenter(k, j);
      this.mListMenuPresenter = localListMenuPresenter1;
      this.mListMenuPresenter.setCallback(paramCallback);
      MenuBuilder localMenuBuilder = this.mMenu;
      ListMenuPresenter localListMenuPresenter2 = this.mListMenuPresenter;
      localMenuBuilder.addMenuPresenter(localListMenuPresenter2);
    }
    while (true)
    {
      ListMenuPresenter localListMenuPresenter3 = this.mListMenuPresenter;
      FrameLayout localFrameLayout = new FrameLayout(paramContext);
      localMenuView = localListMenuPresenter3.getMenuView(localFrameLayout);
      break;
      this.mListMenuPresenter.updateMenuView(false);
    }
  }

  private void reopenMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
  {
    if ((this.mActionBarView != null) && (this.mActionBarView.isOverflowReserved()))
    {
      if ((!this.mActionBarView.isOverflowMenuShowing()) || (!paramBoolean))
      {
        if (this.mActionBarView.getVisibility() != 0)
          return;
        boolean bool1 = this.mActionBarView.showOverflowMenu();
        return;
      }
      boolean bool2 = this.mActionBarView.hideOverflowMenu();
      return;
    }
    paramMenuBuilder.close();
  }

  private void setMenu(MenuBuilder paramMenuBuilder)
  {
    MenuBuilder localMenuBuilder1 = this.mMenu;
    if (paramMenuBuilder == localMenuBuilder1)
      return;
    if (this.mMenu != null)
    {
      MenuBuilder localMenuBuilder2 = this.mMenu;
      ListMenuPresenter localListMenuPresenter1 = this.mListMenuPresenter;
      localMenuBuilder2.removeMenuPresenter(localListMenuPresenter1);
    }
    this.mMenu = paramMenuBuilder;
    if ((paramMenuBuilder != null) && (this.mListMenuPresenter != null))
    {
      ListMenuPresenter localListMenuPresenter2 = this.mListMenuPresenter;
      paramMenuBuilder.addMenuPresenter(localListMenuPresenter2);
    }
    if (this.mActionBarView == null)
      return;
    this.mActionBarView.setMenu(paramMenuBuilder, this);
  }

  public void addContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    ensureSubDecor();
    if (this.mHasActionBar)
      ((ViewGroup)this.mActivity.findViewById(16908290)).addView(paramView, paramLayoutParams);
    while (true)
    {
      this.mActivity.onSupportContentChanged();
      return;
      this.mActivity.superSetContentView(paramView, paramLayoutParams);
    }
  }

  public ActionBar createSupportActionBar()
  {
    ensureSubDecor();
    ActionBarActivity localActionBarActivity1 = this.mActivity;
    ActionBarActivity localActionBarActivity2 = this.mActivity;
    return new ActionBarImplBase(localActionBarActivity1, localActionBarActivity2);
  }

  final void ensureSubDecor()
  {
    if (!this.mHasActionBar)
      return;
    if (this.mSubDecorInstalled)
      return;
    boolean bool1;
    boolean bool2;
    if (this.mOverlayActionBar)
    {
      ActionBarActivity localActionBarActivity1 = this.mActivity;
      int i = R.layout.abc_action_bar_decor_overlay;
      localActionBarActivity1.superSetContentView(i);
      ActionBarActivity localActionBarActivity2 = this.mActivity;
      int j = R.id.action_bar;
      ActionBarView localActionBarView1 = (ActionBarView)localActionBarActivity2.findViewById(j);
      this.mActionBarView = localActionBarView1;
      ActionBarView localActionBarView2 = this.mActionBarView;
      ActionBarActivity localActionBarActivity3 = this.mActivity;
      localActionBarView2.setWindowCallback(localActionBarActivity3);
      if (this.mFeatureProgress)
        this.mActionBarView.initProgress();
      if (this.mFeatureIndeterminateProgress)
        this.mActionBarView.initIndeterminateProgress();
      String str = getUiOptionsFromMetadata();
      bool1 = "splitActionBarWhenNarrow".equals(str);
      if (!bool1)
        break label352;
      Resources localResources = this.mActivity.getResources();
      int k = R.bool.abc_split_action_bar_is_narrow;
      bool2 = localResources.getBoolean(k);
    }
    while (true)
    {
      ActionBarActivity localActionBarActivity4 = this.mActivity;
      int m = R.id.split_action_bar;
      ActionBarContainer localActionBarContainer = (ActionBarContainer)localActionBarActivity4.findViewById(m);
      if (localActionBarContainer != null)
      {
        this.mActionBarView.setSplitView(localActionBarContainer);
        this.mActionBarView.setSplitActionBar(bool2);
        this.mActionBarView.setSplitWhenNarrow(bool1);
        ActionBarActivity localActionBarActivity5 = this.mActivity;
        int n = R.id.action_context_bar;
        ActionBarContextView localActionBarContextView = (ActionBarContextView)localActionBarActivity5.findViewById(n);
        localActionBarContextView.setSplitView(localActionBarContainer);
        localActionBarContextView.setSplitActionBar(bool2);
        localActionBarContextView.setSplitWhenNarrow(bool1);
      }
      this.mActivity.findViewById(16908290).setId(-1);
      ActionBarActivity localActionBarActivity6 = this.mActivity;
      int i1 = R.id.action_bar_activity_content;
      localActionBarActivity6.findViewById(i1).setId(16908290);
      if (this.mTitleToSet != null)
      {
        ActionBarView localActionBarView3 = this.mActionBarView;
        CharSequence localCharSequence = this.mTitleToSet;
        localActionBarView3.setWindowTitle(localCharSequence);
        this.mTitleToSet = null;
      }
      this.mSubDecorInstalled = true;
      supportInvalidateOptionsMenu();
      return;
      ActionBarActivity localActionBarActivity7 = this.mActivity;
      int i2 = R.layout.abc_action_bar_decor;
      localActionBarActivity7.superSetContentView(i2);
      break;
      label352: ActionBarActivity localActionBarActivity8 = this.mActivity;
      int[] arrayOfInt = R.styleable.ActionBarWindow;
      TypedArray localTypedArray = localActionBarActivity8.obtainStyledAttributes(arrayOfInt);
      bool2 = localTypedArray.getBoolean(2, false);
      localTypedArray.recycle();
    }
  }

  ActionBarDrawerToggle.Delegate getDrawerToggleDelegate()
  {
    return new ActionBarDrawableToggleImpl(null);
  }

  public boolean onBackPressed()
  {
    boolean bool = true;
    if (this.mActionMode != null)
      this.mActionMode.finish();
    while (true)
    {
      return bool;
      if ((this.mActionBarView != null) && (this.mActionBarView.hasExpandedActionView()))
        this.mActionBarView.collapseActionView();
      else
        bool = false;
    }
  }

  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
  {
    this.mActivity.closeOptionsMenu();
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    if (!this.mHasActionBar)
      return;
    if (!this.mSubDecorInstalled)
      return;
    ((ActionBarImplBase)getSupportActionBar()).onConfigurationChanged(paramConfiguration);
  }

  public void onContentChanged()
  {
  }

  public boolean onCreatePanelMenu(int paramInt, Menu paramMenu)
  {
    if (paramInt != 0);
    for (boolean bool = this.mActivity.superOnCreatePanelMenu(paramInt, paramMenu); ; bool = false)
      return bool;
  }

  public View onCreatePanelView(int paramInt)
  {
    View localView = null;
    if (paramInt == 0)
    {
      boolean bool = true;
      MenuBuilder localMenuBuilder = this.mMenu;
      if (this.mActionMode == null)
      {
        if (localMenuBuilder == null)
        {
          localMenuBuilder = createMenu();
          setMenu(localMenuBuilder);
          localMenuBuilder.stopDispatchingItemsChanged();
          bool = this.mActivity.superOnCreatePanelMenu(0, localMenuBuilder);
        }
        if (bool)
        {
          localMenuBuilder.stopDispatchingItemsChanged();
          bool = this.mActivity.superOnPreparePanel(0, null, localMenuBuilder);
        }
      }
      if (!bool)
        break label103;
      ActionBarActivity localActionBarActivity = this.mActivity;
      localView = (View)getListMenuView(localActionBarActivity, this);
      localMenuBuilder.startDispatchingItemsChanged();
    }
    while (true)
    {
      return localView;
      label103: setMenu(null);
    }
  }

  public boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem)
  {
    if (paramInt == 0)
      paramMenuItem = MenuWrapperFactory.createMenuItemWrapper(paramMenuItem);
    return this.mActivity.superOnMenuItemSelected(paramInt, paramMenuItem);
  }

  public boolean onMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem)
  {
    return this.mActivity.onMenuItemSelected(0, paramMenuItem);
  }

  public void onMenuModeChange(MenuBuilder paramMenuBuilder)
  {
    reopenMenu(paramMenuBuilder, true);
  }

  public boolean onOpenSubMenu(MenuBuilder paramMenuBuilder)
  {
    return false;
  }

  public void onPostResume()
  {
    ActionBarImplBase localActionBarImplBase = (ActionBarImplBase)getSupportActionBar();
    if (localActionBarImplBase == null)
      return;
    localActionBarImplBase.setShowHideAnimationEnabled(true);
  }

  public boolean onPreparePanel(int paramInt, View paramView, Menu paramMenu)
  {
    if (paramInt != 0);
    for (boolean bool = this.mActivity.superOnPreparePanel(paramInt, paramView, paramMenu); ; bool = false)
      return bool;
  }

  public void onStop()
  {
    ActionBarImplBase localActionBarImplBase = (ActionBarImplBase)getSupportActionBar();
    if (localActionBarImplBase == null)
      return;
    localActionBarImplBase.setShowHideAnimationEnabled(false);
  }

  public void onTitleChanged(CharSequence paramCharSequence)
  {
    if (this.mActionBarView != null)
    {
      this.mActionBarView.setWindowTitle(paramCharSequence);
      return;
    }
    this.mTitleToSet = paramCharSequence;
  }

  public void setContentView(int paramInt)
  {
    ensureSubDecor();
    if (this.mHasActionBar)
    {
      ViewGroup localViewGroup = (ViewGroup)this.mActivity.findViewById(16908290);
      localViewGroup.removeAllViews();
      View localView = this.mActivity.getLayoutInflater().inflate(paramInt, localViewGroup);
    }
    while (true)
    {
      this.mActivity.onSupportContentChanged();
      return;
      this.mActivity.superSetContentView(paramInt);
    }
  }

  public void setContentView(View paramView)
  {
    ensureSubDecor();
    if (this.mHasActionBar)
    {
      ViewGroup localViewGroup = (ViewGroup)this.mActivity.findViewById(16908290);
      localViewGroup.removeAllViews();
      localViewGroup.addView(paramView);
    }
    while (true)
    {
      this.mActivity.onSupportContentChanged();
      return;
      this.mActivity.superSetContentView(paramView);
    }
  }

  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    ensureSubDecor();
    if (this.mHasActionBar)
    {
      ViewGroup localViewGroup = (ViewGroup)this.mActivity.findViewById(16908290);
      localViewGroup.removeAllViews();
      localViewGroup.addView(paramView, paramLayoutParams);
    }
    while (true)
    {
      this.mActivity.onSupportContentChanged();
      return;
      this.mActivity.superSetContentView(paramView, paramLayoutParams);
    }
  }

  public void supportInvalidateOptionsMenu()
  {
    if (this.mInvalidateMenuPosted)
      return;
    this.mInvalidateMenuPosted = true;
    View localView = this.mActivity.getWindow().getDecorView();
    Runnable localRunnable = this.mInvalidateMenuRunnable;
    boolean bool = localView.post(localRunnable);
  }

  public boolean supportRequestWindowFeature(int paramInt)
  {
    boolean bool = true;
    switch (paramInt)
    {
    case 3:
    case 4:
    case 6:
    case 7:
    default:
      bool = this.mActivity.requestWindowFeature(paramInt);
    case 8:
    case 9:
    case 2:
    case 5:
    }
    while (true)
    {
      return bool;
      this.mHasActionBar = true;
      continue;
      this.mOverlayActionBar = true;
      continue;
      this.mFeatureProgress = true;
      continue;
      this.mFeatureIndeterminateProgress = true;
    }
  }

  private class ActionBarDrawableToggleImpl
    implements ActionBarDrawerToggle.Delegate
  {
    private ActionBarDrawableToggleImpl()
    {
    }

    public Drawable getThemeUpIndicator()
    {
      ActionBarActivity localActionBarActivity = ActionBarActivityDelegateBase.this.mActivity;
      int[] arrayOfInt = ActionBarActivityDelegateBase.ACTION_BAR_DRAWABLE_TOGGLE_ATTRS;
      TypedArray localTypedArray = localActionBarActivity.obtainStyledAttributes(arrayOfInt);
      Drawable localDrawable = localTypedArray.getDrawable(0);
      localTypedArray.recycle();
      return localDrawable;
    }

    public void setActionBarDescription(int paramInt)
    {
    }

    public void setActionBarUpIndicator(Drawable paramDrawable, int paramInt)
    {
      if (ActionBarActivityDelegateBase.this.mActionBarView == null)
        return;
      ActionBarActivityDelegateBase.this.mActionBarView.setHomeAsUpIndicator(paramDrawable);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.ActionBarActivityDelegateBase
 * JD-Core Version:    0.6.2
 */