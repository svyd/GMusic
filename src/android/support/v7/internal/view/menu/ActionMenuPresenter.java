package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.view.ActionProvider;
import android.support.v4.view.ActionProvider.SubUiVisibilityListener;
import android.support.v7.appcompat.R.integer;
import android.support.v7.internal.view.ActionBarPolicy;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.ImageButton;
import java.util.ArrayList;

public class ActionMenuPresenter extends BaseMenuPresenter
  implements ActionProvider.SubUiVisibilityListener
{
  private final SparseBooleanArray mActionButtonGroups;
  private ActionButtonSubmenu mActionButtonPopup;
  private int mActionItemWidthLimit;
  private boolean mExpandedActionViewsExclusive;
  private int mMaxItems;
  private boolean mMaxItemsSet;
  private int mMinCellSize;
  int mOpenSubMenuId;
  private View mOverflowButton;
  private OverflowPopup mOverflowPopup;
  final PopupPresenterCallback mPopupPresenterCallback;
  private OpenOverflowRunnable mPostedOpenRunnable;
  private boolean mReserveOverflow;
  private boolean mReserveOverflowSet;
  private View mScrapActionButtonView;
  private boolean mStrictWidthLimit;
  private int mWidthLimit;
  private boolean mWidthLimitSet;

  public ActionMenuPresenter(Context paramContext)
  {
    super(paramContext, i, j);
    SparseBooleanArray localSparseBooleanArray = new SparseBooleanArray();
    this.mActionButtonGroups = localSparseBooleanArray;
    PopupPresenterCallback localPopupPresenterCallback = new PopupPresenterCallback(null);
    this.mPopupPresenterCallback = localPopupPresenterCallback;
  }

  private View findViewForItem(MenuItem paramMenuItem)
  {
    ViewGroup localViewGroup = (ViewGroup)this.mMenuView;
    if (localViewGroup == null);
    label68: for (View localView = null; ; localView = null)
    {
      return localView;
      int i = localViewGroup.getChildCount();
      int j = 0;
      while (true)
      {
        if (j >= i)
          break label68;
        localView = localViewGroup.getChildAt(j);
        if (((localView instanceof MenuView.ItemView)) && (((MenuView.ItemView)localView).getItemData() == paramMenuItem))
          break;
        j += 1;
      }
    }
  }

  public void bindItemView(MenuItemImpl paramMenuItemImpl, MenuView.ItemView paramItemView)
  {
    paramItemView.initialize(paramMenuItemImpl, 0);
    ActionMenuView localActionMenuView = (ActionMenuView)this.mMenuView;
    ((ActionMenuItemView)paramItemView).setItemInvoker(localActionMenuView);
  }

  public boolean dismissPopupMenus()
  {
    boolean bool1 = hideOverflowMenu();
    boolean bool2 = hideSubMenus();
    return bool1 | bool2;
  }

  public boolean filterLeftoverView(ViewGroup paramViewGroup, int paramInt)
  {
    View localView1 = paramViewGroup.getChildAt(paramInt);
    View localView2 = this.mOverflowButton;
    if (localView1 == localView2);
    for (boolean bool = false; ; bool = super.filterLeftoverView(paramViewGroup, paramInt))
      return bool;
  }

  public boolean flagActionItems()
  {
    ArrayList localArrayList1 = this.mMenu.getVisibleItems();
    int i = localArrayList1.size();
    int j = this.mMaxItems;
    int k = this.mActionItemWidthLimit;
    int m = View.MeasureSpec.makeMeasureSpec(0, 0);
    ViewGroup localViewGroup1 = (ViewGroup)this.mMenuView;
    int n = 0;
    int i1 = 0;
    int i2 = 0;
    int i3 = 0;
    int i4 = 0;
    if (i4 < i)
    {
      MenuItemImpl localMenuItemImpl1 = (MenuItemImpl)localArrayList1.get(i4);
      if (localMenuItemImpl1.requiresActionButton())
        n += 1;
      while (true)
      {
        if ((this.mExpandedActionViewsExclusive) && (localMenuItemImpl1.isActionViewExpanded()))
          j = 0;
        i4 += 1;
        break;
        if (localMenuItemImpl1.requestsActionButton())
          i1 += 1;
        else
          i3 = 1;
      }
    }
    if (this.mReserveOverflow)
      if (i3 == 0)
      {
        int i5 = n + i1;
        int i6 = j;
        if (i5 <= i6);
      }
      else
      {
        j += -1;
      }
    int i7 = j - n;
    SparseBooleanArray localSparseBooleanArray1 = this.mActionButtonGroups;
    localSparseBooleanArray1.clear();
    int i8 = 0;
    int i9 = 0;
    if (this.mStrictWidthLimit)
    {
      int i10 = this.mMinCellSize;
      i9 = k / i10;
      int i11 = this.mMinCellSize;
      int i12 = k % i11;
      int i13 = this.mMinCellSize;
      int i14 = i12 / i9;
      i8 = i13 + i14;
    }
    int i15 = 0;
    if (i15 < i)
    {
      MenuItemImpl localMenuItemImpl2 = (MenuItemImpl)localArrayList1.get(i15);
      View localView3;
      int i20;
      if (localMenuItemImpl2.requiresActionButton())
      {
        localView1 = this.mScrapActionButtonView;
        localActionMenuPresenter1 = this;
        localView2 = localView1;
        localViewGroup2 = localViewGroup1;
        localView3 = localActionMenuPresenter1.getItemView(localMenuItemImpl2, localView2, localViewGroup2);
        if (this.mScrapActionButtonView == null)
        {
          localView4 = localView3;
          this.mScrapActionButtonView = localView4;
        }
        if (this.mStrictWidthLimit)
        {
          localView5 = localView3;
          i16 = m;
          i17 = 0;
          i18 = ActionMenuView.measureChildForCells(localView5, i8, i9, i16, i17);
          i19 = i9 - i18;
          i20 = localView3.getMeasuredWidth();
          i21 = k - i20;
          if (i2 == 0)
            i22 = i20;
          i23 = localMenuItemImpl2.getGroupId();
          if (i23 != 0)
          {
            localSparseBooleanArray2 = localSparseBooleanArray1;
            bool1 = true;
            localSparseBooleanArray2.put(i23, bool1);
          }
          bool2 = true;
          localMenuItemImpl2.setIsActionButton(bool2);
        }
      }
      while (!localMenuItemImpl2.requestsActionButton())
        while (true)
        {
          View localView1;
          ActionMenuPresenter localActionMenuPresenter1;
          View localView2;
          ViewGroup localViewGroup2;
          View localView4;
          View localView5;
          int i16;
          int i17;
          int i18;
          int i19;
          int i21;
          int i22;
          SparseBooleanArray localSparseBooleanArray2;
          boolean bool1;
          boolean bool2;
          i15 += 1;
          break;
          View localView6 = localView3;
          int i24 = m;
          int i25 = m;
          localView6.measure(i24, i25);
        }
      int i23 = localMenuItemImpl2.getGroupId();
      boolean bool3 = localSparseBooleanArray1.get(i23);
      boolean bool4;
      label508: boolean bool5;
      if (((i7 > 0) || (bool3)) && (k > 0) && ((!this.mStrictWidthLimit) || (i9 > 0)))
      {
        bool4 = true;
        if (bool4)
        {
          View localView7 = this.mScrapActionButtonView;
          ActionMenuPresenter localActionMenuPresenter2 = this;
          View localView8 = localView7;
          ViewGroup localViewGroup3 = localViewGroup1;
          localView3 = localActionMenuPresenter2.getItemView(localMenuItemImpl2, localView8, localViewGroup3);
          if (this.mScrapActionButtonView == null)
          {
            View localView9 = localView3;
            this.mScrapActionButtonView = localView9;
          }
          if (!this.mStrictWidthLimit)
            break label707;
          View localView10 = localView3;
          int i26 = m;
          int i27 = 0;
          int i28 = ActionMenuView.measureChildForCells(localView10, i8, i9, i26, i27);
          int i29 = i9 - i28;
          if (i28 == 0)
            bool4 = false;
          label608: i20 = localView3.getMeasuredWidth();
          k -= i20;
          if (i2 == 0)
            i2 = i20;
          if (!this.mStrictWidthLimit)
            break label737;
          if (k < 0)
            break label731;
          bool5 = true;
          label646: bool4 &= bool5;
        }
        if ((!bool4) || (i23 == 0))
          break label764;
        SparseBooleanArray localSparseBooleanArray3 = localSparseBooleanArray1;
        boolean bool6 = true;
        localSparseBooleanArray3.put(i23, bool6);
      }
      while (true)
      {
        if (bool4)
          int i30 = i7 + -1;
        localMenuItemImpl2.setIsActionButton(bool4);
        break;
        bool4 = false;
        break label508;
        label707: View localView11 = localView3;
        int i31 = m;
        int i32 = m;
        localView11.measure(i31, i32);
        break label608;
        label731: bool5 = false;
        break label646;
        label737: if (k + i2 > 0);
        for (bool5 = true; ; bool5 = false)
        {
          bool4 &= bool5;
          break;
        }
        label764: if (bool3)
        {
          SparseBooleanArray localSparseBooleanArray4 = localSparseBooleanArray1;
          boolean bool7 = false;
          localSparseBooleanArray4.put(i23, bool7);
          int i33 = 0;
          while (i33 < i15)
          {
            ArrayList localArrayList2 = localArrayList1;
            int i34 = i33;
            MenuItemImpl localMenuItemImpl3 = (MenuItemImpl)localArrayList2.get(i34);
            if (localMenuItemImpl3.getGroupId() != i23)
            {
              if (localMenuItemImpl3.isActionButton())
                i7 += 1;
              boolean bool8 = false;
              localMenuItemImpl3.setIsActionButton(bool8);
            }
            i33 += 1;
          }
        }
      }
    }
    return true;
  }

  public View getItemView(MenuItemImpl paramMenuItemImpl, View paramView, ViewGroup paramViewGroup)
  {
    View localView = paramMenuItemImpl.getActionView();
    if ((localView == null) || (paramMenuItemImpl.hasCollapsibleActionView()))
    {
      if (!(paramView instanceof ActionMenuItemView))
        paramView = null;
      localView = super.getItemView(paramMenuItemImpl, paramView, paramViewGroup);
    }
    if (paramMenuItemImpl.isActionViewExpanded());
    for (int i = 8; ; i = 0)
    {
      localView.setVisibility(i);
      ActionMenuView localActionMenuView = (ActionMenuView)paramViewGroup;
      ViewGroup.LayoutParams localLayoutParams = localView.getLayoutParams();
      if (!localActionMenuView.checkLayoutParams(localLayoutParams))
      {
        ActionMenuView.LayoutParams localLayoutParams1 = localActionMenuView.generateLayoutParams(localLayoutParams);
        localView.setLayoutParams(localLayoutParams1);
      }
      return localView;
    }
  }

  public MenuView getMenuView(ViewGroup paramViewGroup)
  {
    MenuView localMenuView = super.getMenuView(paramViewGroup);
    ((ActionMenuView)localMenuView).setPresenter(this);
    return localMenuView;
  }

  public boolean hideOverflowMenu()
  {
    boolean bool2;
    if ((this.mPostedOpenRunnable != null) && (this.mMenuView != null))
    {
      View localView = (View)this.mMenuView;
      OpenOverflowRunnable localOpenOverflowRunnable = this.mPostedOpenRunnable;
      boolean bool1 = localView.removeCallbacks(localOpenOverflowRunnable);
      this.mPostedOpenRunnable = null;
      bool2 = true;
    }
    while (true)
    {
      return bool2;
      OverflowPopup localOverflowPopup = this.mOverflowPopup;
      if (localOverflowPopup != null)
      {
        localOverflowPopup.dismiss();
        bool2 = true;
      }
      else
      {
        bool2 = false;
      }
    }
  }

  public boolean hideSubMenus()
  {
    if (this.mActionButtonPopup != null)
      this.mActionButtonPopup.dismiss();
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder)
  {
    super.initForMenu(paramContext, paramMenuBuilder);
    Resources localResources = paramContext.getResources();
    ActionBarPolicy localActionBarPolicy = ActionBarPolicy.get(paramContext);
    if (!this.mReserveOverflowSet)
    {
      boolean bool = localActionBarPolicy.showsOverflowMenuButton();
      this.mReserveOverflow = bool;
    }
    if (!this.mWidthLimitSet)
    {
      int i = localActionBarPolicy.getEmbeddedMenuWidthLimit();
      this.mWidthLimit = i;
    }
    if (!this.mMaxItemsSet)
    {
      int j = localActionBarPolicy.getMaxActionButtons();
      this.mMaxItems = j;
    }
    int k = this.mWidthLimit;
    if (this.mReserveOverflow)
    {
      if (this.mOverflowButton == null)
      {
        Context localContext = this.mSystemContext;
        OverflowMenuButton localOverflowMenuButton = new OverflowMenuButton(localContext);
        this.mOverflowButton = localOverflowMenuButton;
        int m = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.mOverflowButton.measure(m, m);
      }
      int n = this.mOverflowButton.getMeasuredWidth();
      k -= n;
    }
    while (true)
    {
      this.mActionItemWidthLimit = k;
      float f = localResources.getDisplayMetrics().density;
      int i1 = (int)(56.0F * f);
      this.mMinCellSize = i1;
      this.mScrapActionButtonView = null;
      return;
      this.mOverflowButton = null;
    }
  }

  public boolean isOverflowMenuShowing()
  {
    if ((this.mOverflowPopup != null) && (this.mOverflowPopup.isShowing()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isOverflowReserved()
  {
    return this.mReserveOverflow;
  }

  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
  {
    boolean bool = dismissPopupMenus();
    super.onCloseMenu(paramMenuBuilder, paramBoolean);
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    if (!this.mMaxItemsSet)
    {
      Resources localResources = this.mContext.getResources();
      int i = R.integer.abc_max_action_buttons;
      int j = localResources.getInteger(i);
      this.mMaxItems = j;
    }
    if (this.mMenu == null)
      return;
    this.mMenu.onItemsChanged(true);
  }

  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
  {
    boolean bool1 = false;
    if (!paramSubMenuBuilder.hasVisibleItems());
    while (true)
    {
      return bool1;
      for (SubMenuBuilder localSubMenuBuilder = paramSubMenuBuilder; ; localSubMenuBuilder = (SubMenuBuilder)localSubMenuBuilder.getParentMenu())
      {
        Menu localMenu = localSubMenuBuilder.getParentMenu();
        MenuBuilder localMenuBuilder = this.mMenu;
        if (localMenu == localMenuBuilder)
          break;
      }
      MenuItem localMenuItem = localSubMenuBuilder.getItem();
      if (findViewForItem(localMenuItem) == null)
      {
        if (this.mOverflowButton != null)
          View localView = this.mOverflowButton;
      }
      else
      {
        int i = paramSubMenuBuilder.getItem().getItemId();
        this.mOpenSubMenuId = i;
        ActionButtonSubmenu localActionButtonSubmenu = new ActionButtonSubmenu(paramSubMenuBuilder);
        this.mActionButtonPopup = localActionButtonSubmenu;
        this.mActionButtonPopup.show(null);
        boolean bool2 = super.onSubMenuSelected(paramSubMenuBuilder);
        bool1 = true;
      }
    }
  }

  public void onSubUiVisibilityChanged(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      boolean bool = super.onSubMenuSelected(null);
      return;
    }
    this.mMenu.close(false);
  }

  public void setExpandedActionViewsExclusive(boolean paramBoolean)
  {
    this.mExpandedActionViewsExclusive = paramBoolean;
  }

  public void setItemLimit(int paramInt)
  {
    this.mMaxItems = paramInt;
    this.mMaxItemsSet = true;
  }

  public void setWidthLimit(int paramInt, boolean paramBoolean)
  {
    this.mWidthLimit = paramInt;
    this.mStrictWidthLimit = paramBoolean;
    this.mWidthLimitSet = true;
  }

  public boolean shouldIncludeItem(int paramInt, MenuItemImpl paramMenuItemImpl)
  {
    return paramMenuItemImpl.isActionButton();
  }

  public boolean showOverflowMenu()
  {
    boolean bool1 = true;
    if ((this.mReserveOverflow) && (!isOverflowMenuShowing()) && (this.mMenu != null) && (this.mMenuView != null) && (this.mPostedOpenRunnable == null))
    {
      Context localContext = this.mContext;
      MenuBuilder localMenuBuilder = this.mMenu;
      View localView1 = this.mOverflowButton;
      ActionMenuPresenter localActionMenuPresenter = this;
      OverflowPopup localOverflowPopup = new OverflowPopup(localContext, localMenuBuilder, localView1, true);
      OpenOverflowRunnable localOpenOverflowRunnable1 = new OpenOverflowRunnable(localOverflowPopup);
      this.mPostedOpenRunnable = localOpenOverflowRunnable1;
      View localView2 = (View)this.mMenuView;
      OpenOverflowRunnable localOpenOverflowRunnable2 = this.mPostedOpenRunnable;
      boolean bool2 = localView2.post(localOpenOverflowRunnable2);
      boolean bool3 = super.onSubMenuSelected(null);
    }
    while (true)
    {
      return bool1;
      bool1 = false;
    }
  }

  public void updateMenuView(boolean paramBoolean)
  {
    super.updateMenuView(paramBoolean);
    if (this.mMenuView == null)
      return;
    int i;
    if (this.mMenu != null)
    {
      ArrayList localArrayList1 = this.mMenu.getActionItems();
      i = localArrayList1.size();
      int j = 0;
      while (j < i)
      {
        ActionProvider localActionProvider = ((MenuItemImpl)localArrayList1.get(j)).getSupportActionProvider();
        if (localActionProvider != null)
          localActionProvider.setSubUiVisibilityListener(this);
        j += 1;
      }
    }
    ArrayList localArrayList2;
    int k;
    if (this.mMenu != null)
    {
      localArrayList2 = this.mMenu.getNonActionItems();
      k = 0;
      if ((this.mReserveOverflow) && (localArrayList2 != null))
      {
        i = localArrayList2.size();
        if (i != 1)
          break label281;
        if (((MenuItemImpl)localArrayList2.get(0)).isActionViewExpanded())
          break label275;
        k = 1;
      }
      label136: if (k == 0)
        break label297;
      if (this.mOverflowButton == null)
      {
        Context localContext = this.mSystemContext;
        OverflowMenuButton localOverflowMenuButton = new OverflowMenuButton(localContext);
        this.mOverflowButton = localOverflowMenuButton;
      }
      ViewGroup localViewGroup1 = (ViewGroup)this.mOverflowButton.getParent();
      MenuView localMenuView1 = this.mMenuView;
      if (localViewGroup1 != localMenuView1)
      {
        if (localViewGroup1 != null)
        {
          View localView1 = this.mOverflowButton;
          localViewGroup1.removeView(localView1);
        }
        ActionMenuView localActionMenuView1 = (ActionMenuView)this.mMenuView;
        View localView2 = this.mOverflowButton;
        ActionMenuView.LayoutParams localLayoutParams = localActionMenuView1.generateOverflowButtonLayoutParams();
        localActionMenuView1.addView(localView2, localLayoutParams);
      }
    }
    while (true)
    {
      ActionMenuView localActionMenuView2 = (ActionMenuView)this.mMenuView;
      boolean bool = this.mReserveOverflow;
      localActionMenuView2.setOverflowReserved(bool);
      return;
      localArrayList2 = null;
      break;
      label275: k = 0;
      break label136;
      label281: if (i > 0);
      for (k = 1; ; k = 0)
        break;
      label297: if (this.mOverflowButton != null)
      {
        ViewParent localViewParent = this.mOverflowButton.getParent();
        MenuView localMenuView2 = this.mMenuView;
        if (localViewParent == localMenuView2)
        {
          ViewGroup localViewGroup2 = (ViewGroup)this.mMenuView;
          View localView3 = this.mOverflowButton;
          localViewGroup2.removeView(localView3);
        }
      }
    }
  }

  private class OpenOverflowRunnable
    implements Runnable
  {
    private ActionMenuPresenter.OverflowPopup mPopup;

    public OpenOverflowRunnable(ActionMenuPresenter.OverflowPopup arg2)
    {
      Object localObject;
      this.mPopup = localObject;
    }

    public void run()
    {
      ActionMenuPresenter.this.mMenu.changeMenuMode();
      View localView = (View)ActionMenuPresenter.this.mMenuView;
      if ((localView != null) && (localView.getWindowToken() != null) && (this.mPopup.tryShow()))
      {
        ActionMenuPresenter localActionMenuPresenter = ActionMenuPresenter.this;
        ActionMenuPresenter.OverflowPopup localOverflowPopup1 = this.mPopup;
        ActionMenuPresenter.OverflowPopup localOverflowPopup2 = ActionMenuPresenter.access$102(localActionMenuPresenter, localOverflowPopup1);
      }
      OpenOverflowRunnable localOpenOverflowRunnable = ActionMenuPresenter.access$302(ActionMenuPresenter.this, null);
    }
  }

  private class PopupPresenterCallback
    implements MenuPresenter.Callback
  {
    private PopupPresenterCallback()
    {
    }

    public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
    {
      if (!(paramMenuBuilder instanceof SubMenuBuilder))
        return;
      ((SubMenuBuilder)paramMenuBuilder).getRootMenu().close(false);
    }

    public boolean onOpenSubMenu(MenuBuilder paramMenuBuilder)
    {
      if (paramMenuBuilder == null);
      while (true)
      {
        return false;
        ActionMenuPresenter localActionMenuPresenter = ActionMenuPresenter.this;
        int i = ((SubMenuBuilder)paramMenuBuilder).getItem().getItemId();
        localActionMenuPresenter.mOpenSubMenuId = i;
      }
    }
  }

  private class ActionButtonSubmenu extends MenuDialogHelper
  {
    public ActionButtonSubmenu(SubMenuBuilder arg2)
    {
      super();
      ActionMenuPresenter.PopupPresenterCallback localPopupPresenterCallback = ActionMenuPresenter.this.mPopupPresenterCallback;
      ActionMenuPresenter.this.setCallback(localPopupPresenterCallback);
    }

    public void onDismiss(DialogInterface paramDialogInterface)
    {
      super.onDismiss(paramDialogInterface);
      ActionButtonSubmenu localActionButtonSubmenu = ActionMenuPresenter.access$202(ActionMenuPresenter.this, null);
      ActionMenuPresenter.this.mOpenSubMenuId = 0;
    }
  }

  private class OverflowPopup extends MenuPopupHelper
  {
    public OverflowPopup(Context paramMenuBuilder, MenuBuilder paramView, View paramBoolean, boolean arg5)
    {
      super(paramView, paramBoolean, bool);
      ActionMenuPresenter.PopupPresenterCallback localPopupPresenterCallback = ActionMenuPresenter.this.mPopupPresenterCallback;
      setCallback(localPopupPresenterCallback);
    }

    public void onDismiss()
    {
      super.onDismiss();
      ActionMenuPresenter.this.mMenu.close();
      OverflowPopup localOverflowPopup = ActionMenuPresenter.access$102(ActionMenuPresenter.this, null);
    }
  }

  private class OverflowMenuButton extends ImageButton
    implements ActionMenuView.ActionMenuChildView
  {
    public OverflowMenuButton(Context arg2)
    {
      super(null, i);
      setClickable(true);
      setFocusable(true);
      setVisibility(0);
      setEnabled(true);
    }

    public boolean needsDividerAfter()
    {
      return false;
    }

    public boolean needsDividerBefore()
    {
      return false;
    }

    public boolean performClick()
    {
      if (super.performClick());
      while (true)
      {
        return true;
        playSoundEffect(0);
        boolean bool = ActionMenuPresenter.this.showOverflowMenu();
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.ActionMenuPresenter
 * JD-Core Version:    0.6.2
 */