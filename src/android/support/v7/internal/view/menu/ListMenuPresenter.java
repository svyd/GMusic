package android.support.v7.internal.view.menu;

import android.content.Context;
import android.support.v7.appcompat.R.layout;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import java.util.ArrayList;

public class ListMenuPresenter
  implements MenuPresenter, AdapterView.OnItemClickListener
{
  MenuAdapter mAdapter;
  private MenuPresenter.Callback mCallback;
  Context mContext;
  LayoutInflater mInflater;
  private int mItemIndexOffset;
  int mItemLayoutRes;
  MenuBuilder mMenu;
  ExpandedMenuView mMenuView;
  int mThemeRes;

  public ListMenuPresenter(int paramInt1, int paramInt2)
  {
    this.mItemLayoutRes = paramInt1;
    this.mThemeRes = paramInt2;
  }

  public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
  {
    return false;
  }

  public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
  {
    return false;
  }

  public boolean flagActionItems()
  {
    return false;
  }

  public ListAdapter getAdapter()
  {
    if (this.mAdapter == null)
    {
      MenuAdapter localMenuAdapter = new MenuAdapter();
      this.mAdapter = localMenuAdapter;
    }
    return this.mAdapter;
  }

  public MenuView getMenuView(ViewGroup paramViewGroup)
  {
    if (this.mAdapter == null)
    {
      MenuAdapter localMenuAdapter1 = new MenuAdapter();
      this.mAdapter = localMenuAdapter1;
    }
    if (!this.mAdapter.isEmpty())
      if (this.mMenuView == null)
      {
        LayoutInflater localLayoutInflater = this.mInflater;
        int i = R.layout.abc_expanded_menu_layout;
        ExpandedMenuView localExpandedMenuView1 = (ExpandedMenuView)localLayoutInflater.inflate(i, paramViewGroup, false);
        this.mMenuView = localExpandedMenuView1;
        ExpandedMenuView localExpandedMenuView2 = this.mMenuView;
        MenuAdapter localMenuAdapter2 = this.mAdapter;
        localExpandedMenuView2.setAdapter(localMenuAdapter2);
        this.mMenuView.setOnItemClickListener(this);
      }
    for (ExpandedMenuView localExpandedMenuView3 = this.mMenuView; ; localExpandedMenuView3 = null)
      return localExpandedMenuView3;
  }

  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder)
  {
    if (this.mThemeRes != 0)
    {
      int i = this.mThemeRes;
      ContextThemeWrapper localContextThemeWrapper = new ContextThemeWrapper(paramContext, i);
      this.mContext = localContextThemeWrapper;
      LayoutInflater localLayoutInflater1 = LayoutInflater.from(this.mContext);
      this.mInflater = localLayoutInflater1;
    }
    while (true)
    {
      this.mMenu = paramMenuBuilder;
      if (this.mAdapter == null)
        return;
      this.mAdapter.notifyDataSetChanged();
      return;
      if (this.mContext != null)
      {
        this.mContext = paramContext;
        if (this.mInflater == null)
        {
          LayoutInflater localLayoutInflater2 = LayoutInflater.from(this.mContext);
          this.mInflater = localLayoutInflater2;
        }
      }
    }
  }

  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
  {
    if (this.mCallback == null)
      return;
    this.mCallback.onCloseMenu(paramMenuBuilder, paramBoolean);
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    MenuBuilder localMenuBuilder = this.mMenu;
    MenuItemImpl localMenuItemImpl = this.mAdapter.getItem(paramInt);
    boolean bool = localMenuBuilder.performItemAction(localMenuItemImpl, 0);
  }

  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
  {
    if (!paramSubMenuBuilder.hasVisibleItems());
    for (boolean bool1 = false; ; bool1 = true)
    {
      return bool1;
      new MenuDialogHelper(paramSubMenuBuilder).show(null);
      if (this.mCallback != null)
        boolean bool2 = this.mCallback.onOpenSubMenu(paramSubMenuBuilder);
    }
  }

  public void setCallback(MenuPresenter.Callback paramCallback)
  {
    this.mCallback = paramCallback;
  }

  public void updateMenuView(boolean paramBoolean)
  {
    if (this.mAdapter == null)
      return;
    this.mAdapter.notifyDataSetChanged();
  }

  private class MenuAdapter extends BaseAdapter
  {
    private int mExpandedIndex = -1;

    public MenuAdapter()
    {
      findExpandedIndex();
    }

    void findExpandedIndex()
    {
      MenuItemImpl localMenuItemImpl = ListMenuPresenter.this.mMenu.getExpandedItem();
      if (localMenuItemImpl != null)
      {
        ArrayList localArrayList = ListMenuPresenter.this.mMenu.getNonActionItems();
        int i = localArrayList.size();
        int j = 0;
        while (j < i)
        {
          if ((MenuItemImpl)localArrayList.get(j) == localMenuItemImpl)
          {
            this.mExpandedIndex = j;
            return;
          }
          j += 1;
        }
      }
      this.mExpandedIndex = -1;
    }

    public int getCount()
    {
      int i = ListMenuPresenter.this.mMenu.getNonActionItems().size();
      int j = ListMenuPresenter.this.mItemIndexOffset;
      int k = i - j;
      if (this.mExpandedIndex < 0);
      while (true)
      {
        return k;
        k += -1;
      }
    }

    public MenuItemImpl getItem(int paramInt)
    {
      ArrayList localArrayList = ListMenuPresenter.this.mMenu.getNonActionItems();
      int i = ListMenuPresenter.this.mItemIndexOffset;
      int j = paramInt + i;
      if (this.mExpandedIndex >= 0)
      {
        int k = this.mExpandedIndex;
        if (j >= k)
          j += 1;
      }
      return (MenuItemImpl)localArrayList.get(j);
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        LayoutInflater localLayoutInflater = ListMenuPresenter.this.mInflater;
        int i = ListMenuPresenter.this.mItemLayoutRes;
        paramView = localLayoutInflater.inflate(i, paramViewGroup, false);
      }
      MenuView.ItemView localItemView = (MenuView.ItemView)paramView;
      MenuItemImpl localMenuItemImpl = getItem(paramInt);
      localItemView.initialize(localMenuItemImpl, 0);
      return paramView;
    }

    public void notifyDataSetChanged()
    {
      findExpandedIndex();
      super.notifyDataSetChanged();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.ListMenuPresenter
 * JD-Core Version:    0.6.2
 */