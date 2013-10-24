package android.support.v7.internal.view.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public abstract class BaseMenuPresenter
  implements MenuPresenter
{
  private MenuPresenter.Callback mCallback;
  protected Context mContext;
  private int mId;
  protected LayoutInflater mInflater;
  private int mItemLayoutRes;
  protected MenuBuilder mMenu;
  private int mMenuLayoutRes;
  protected MenuView mMenuView;
  protected Context mSystemContext;
  protected LayoutInflater mSystemInflater;

  public BaseMenuPresenter(Context paramContext, int paramInt1, int paramInt2)
  {
    this.mSystemContext = paramContext;
    LayoutInflater localLayoutInflater = LayoutInflater.from(paramContext);
    this.mSystemInflater = localLayoutInflater;
    this.mMenuLayoutRes = paramInt1;
    this.mItemLayoutRes = paramInt2;
  }

  protected void addItemView(View paramView, int paramInt)
  {
    ViewGroup localViewGroup = (ViewGroup)paramView.getParent();
    if (localViewGroup != null)
      localViewGroup.removeView(paramView);
    ((ViewGroup)this.mMenuView).addView(paramView, paramInt);
  }

  public abstract void bindItemView(MenuItemImpl paramMenuItemImpl, MenuView.ItemView paramItemView);

  public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
  {
    return false;
  }

  public MenuView.ItemView createItemView(ViewGroup paramViewGroup)
  {
    LayoutInflater localLayoutInflater = this.mSystemInflater;
    int i = this.mItemLayoutRes;
    return (MenuView.ItemView)localLayoutInflater.inflate(i, paramViewGroup, false);
  }

  public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
  {
    return false;
  }

  protected boolean filterLeftoverView(ViewGroup paramViewGroup, int paramInt)
  {
    paramViewGroup.removeViewAt(paramInt);
    return true;
  }

  public boolean flagActionItems()
  {
    return false;
  }

  public View getItemView(MenuItemImpl paramMenuItemImpl, View paramView, ViewGroup paramViewGroup)
  {
    if ((paramView instanceof MenuView.ItemView));
    for (MenuView.ItemView localItemView = (MenuView.ItemView)paramView; ; localItemView = createItemView(paramViewGroup))
    {
      bindItemView(paramMenuItemImpl, localItemView);
      return (View)localItemView;
    }
  }

  public MenuView getMenuView(ViewGroup paramViewGroup)
  {
    if (this.mMenuView == null)
    {
      LayoutInflater localLayoutInflater = this.mSystemInflater;
      int i = this.mMenuLayoutRes;
      MenuView localMenuView1 = (MenuView)localLayoutInflater.inflate(i, paramViewGroup, false);
      this.mMenuView = localMenuView1;
      MenuView localMenuView2 = this.mMenuView;
      MenuBuilder localMenuBuilder = this.mMenu;
      localMenuView2.initialize(localMenuBuilder);
      updateMenuView(true);
    }
    return this.mMenuView;
  }

  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder)
  {
    this.mContext = paramContext;
    LayoutInflater localLayoutInflater = LayoutInflater.from(this.mContext);
    this.mInflater = localLayoutInflater;
    this.mMenu = paramMenuBuilder;
  }

  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
  {
    if (this.mCallback == null)
      return;
    this.mCallback.onCloseMenu(paramMenuBuilder, paramBoolean);
  }

  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
  {
    if (this.mCallback != null);
    for (boolean bool = this.mCallback.onOpenSubMenu(paramSubMenuBuilder); ; bool = false)
      return bool;
  }

  public void setCallback(MenuPresenter.Callback paramCallback)
  {
    this.mCallback = paramCallback;
  }

  public void setId(int paramInt)
  {
    this.mId = paramInt;
  }

  public boolean shouldIncludeItem(int paramInt, MenuItemImpl paramMenuItemImpl)
  {
    return true;
  }

  public void updateMenuView(boolean paramBoolean)
  {
    ViewGroup localViewGroup = (ViewGroup)this.mMenuView;
    if (localViewGroup == null)
      return;
    int i = 0;
    if (this.mMenu != null)
    {
      this.mMenu.flagActionItems();
      ArrayList localArrayList = this.mMenu.getVisibleItems();
      int j = localArrayList.size();
      int k = 0;
      if (k < j)
      {
        MenuItemImpl localMenuItemImpl1 = (MenuItemImpl)localArrayList.get(k);
        View localView1;
        if (shouldIncludeItem(i, localMenuItemImpl1))
        {
          localView1 = localViewGroup.getChildAt(i);
          if (!(localView1 instanceof MenuView.ItemView))
            break label155;
        }
        label155: for (MenuItemImpl localMenuItemImpl2 = ((MenuView.ItemView)localView1).getItemData(); ; localMenuItemImpl2 = null)
        {
          View localView2 = getItemView(localMenuItemImpl1, localView1, localViewGroup);
          if (localMenuItemImpl1 != localMenuItemImpl2)
            localView2.setPressed(false);
          if (localView2 != localView1)
            addItemView(localView2, i);
          i += 1;
          k += 1;
          break;
        }
      }
    }
    while (true)
    {
      int m = localViewGroup.getChildCount();
      if (i >= m)
        return;
      if (!filterLeftoverView(localViewGroup, i))
        i += 1;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.BaseMenuPresenter
 * JD-Core Version:    0.6.2
 */