package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.appcompat.R.attr;
import android.support.v7.appcompat.R.dimen;
import android.support.v7.appcompat.R.layout;
import android.support.v7.internal.widget.ListPopupWindow;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import java.util.ArrayList;

public class MenuPopupHelper
  implements MenuPresenter, View.OnKeyListener, ViewTreeObserver.OnGlobalLayoutListener, AdapterView.OnItemClickListener, PopupWindow.OnDismissListener
{
  static final int ITEM_LAYOUT = R.layout.abc_popup_menu_item_layout;
  private MenuAdapter mAdapter;
  private View mAnchorView;
  private Context mContext;
  boolean mForceShowIcon;
  private LayoutInflater mInflater;
  private ViewGroup mMeasureParent;
  private MenuBuilder mMenu;
  private boolean mOverflowOnly;
  private ListPopupWindow mPopup;
  private int mPopupMaxWidth;
  private MenuPresenter.Callback mPresenterCallback;
  private ViewTreeObserver mTreeObserver;

  public MenuPopupHelper(Context paramContext, MenuBuilder paramMenuBuilder, View paramView, boolean paramBoolean)
  {
    this.mContext = paramContext;
    LayoutInflater localLayoutInflater = LayoutInflater.from(paramContext);
    this.mInflater = localLayoutInflater;
    this.mMenu = paramMenuBuilder;
    this.mOverflowOnly = paramBoolean;
    Resources localResources = paramContext.getResources();
    int i = localResources.getDisplayMetrics().widthPixels / 2;
    int j = R.dimen.abc_config_prefDialogWidth;
    int k = localResources.getDimensionPixelSize(j);
    int m = Math.max(i, k);
    this.mPopupMaxWidth = m;
    this.mAnchorView = paramView;
    paramMenuBuilder.addMenuPresenter(this);
  }

  private int measureContentWidth(ListAdapter paramListAdapter)
  {
    int i = 0;
    View localView = null;
    int j = 0;
    int k = View.MeasureSpec.makeMeasureSpec(0, 0);
    int m = View.MeasureSpec.makeMeasureSpec(0, 0);
    int n = paramListAdapter.getCount();
    int i1 = 0;
    while (i1 < n)
    {
      int i2 = paramListAdapter.getItemViewType(i1);
      if (i2 != j)
      {
        j = i2;
        localView = null;
      }
      if (this.mMeasureParent == null)
      {
        Context localContext = this.mContext;
        FrameLayout localFrameLayout = new FrameLayout(localContext);
        this.mMeasureParent = localFrameLayout;
      }
      ViewGroup localViewGroup = this.mMeasureParent;
      localView = paramListAdapter.getView(i1, localView, localViewGroup);
      localView.measure(k, m);
      int i3 = localView.getMeasuredWidth();
      i = Math.max(i, i3);
      i1 += 1;
    }
    return i;
  }

  public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
  {
    return false;
  }

  public void dismiss()
  {
    if (!isShowing())
      return;
    this.mPopup.dismiss();
  }

  public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
  {
    return false;
  }

  public boolean flagActionItems()
  {
    return false;
  }

  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder)
  {
  }

  public boolean isShowing()
  {
    if ((this.mPopup != null) && (this.mPopup.isShowing()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
  {
    MenuBuilder localMenuBuilder = this.mMenu;
    if (paramMenuBuilder != localMenuBuilder)
      return;
    dismiss();
    if (this.mPresenterCallback == null)
      return;
    this.mPresenterCallback.onCloseMenu(paramMenuBuilder, paramBoolean);
  }

  public void onDismiss()
  {
    this.mPopup = null;
    this.mMenu.close();
    if (this.mTreeObserver == null)
      return;
    if (!this.mTreeObserver.isAlive())
    {
      ViewTreeObserver localViewTreeObserver = this.mAnchorView.getViewTreeObserver();
      this.mTreeObserver = localViewTreeObserver;
    }
    this.mTreeObserver.removeGlobalOnLayoutListener(this);
    this.mTreeObserver = null;
  }

  public void onGlobalLayout()
  {
    if (!isShowing())
      return;
    View localView = this.mAnchorView;
    if ((localView == null) || (!localView.isShown()))
    {
      dismiss();
      return;
    }
    if (!isShowing())
      return;
    this.mPopup.show();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    MenuAdapter localMenuAdapter = this.mAdapter;
    MenuBuilder localMenuBuilder = localMenuAdapter.mAdapterMenu;
    MenuItemImpl localMenuItemImpl = localMenuAdapter.getItem(paramInt);
    boolean bool = localMenuBuilder.performItemAction(localMenuItemImpl, 0);
  }

  public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    int i = 1;
    if ((paramKeyEvent.getAction() != i) && (paramInt == 82))
      dismiss();
    while (true)
    {
      return i;
      int j = 0;
    }
  }

  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
  {
    boolean bool1 = false;
    MenuPopupHelper localMenuPopupHelper;
    boolean bool2;
    int i;
    int j;
    if (paramSubMenuBuilder.hasVisibleItems())
    {
      Context localContext = this.mContext;
      View localView = this.mAnchorView;
      localMenuPopupHelper = new MenuPopupHelper(localContext, paramSubMenuBuilder, localView, false);
      MenuPresenter.Callback localCallback = this.mPresenterCallback;
      localMenuPopupHelper.setCallback(localCallback);
      bool2 = false;
      i = paramSubMenuBuilder.size();
      j = 0;
    }
    while (true)
    {
      if (j < i)
      {
        MenuItem localMenuItem = paramSubMenuBuilder.getItem(j);
        if ((localMenuItem.isVisible()) && (localMenuItem.getIcon() != null))
          bool2 = true;
      }
      else
      {
        localMenuPopupHelper.setForceShowIcon(bool2);
        if (localMenuPopupHelper.tryShow())
        {
          if (this.mPresenterCallback != null)
            boolean bool3 = this.mPresenterCallback.onOpenSubMenu(paramSubMenuBuilder);
          bool1 = true;
        }
        return bool1;
      }
      j += 1;
    }
  }

  public void setCallback(MenuPresenter.Callback paramCallback)
  {
    this.mPresenterCallback = paramCallback;
  }

  public void setForceShowIcon(boolean paramBoolean)
  {
    this.mForceShowIcon = paramBoolean;
  }

  public boolean tryShow()
  {
    int i = 0;
    boolean bool = true;
    Context localContext = this.mContext;
    int j = R.attr.popupMenuStyle;
    ListPopupWindow localListPopupWindow1 = new ListPopupWindow(localContext, null, j);
    this.mPopup = localListPopupWindow1;
    this.mPopup.setOnDismissListener(this);
    this.mPopup.setOnItemClickListener(this);
    MenuBuilder localMenuBuilder = this.mMenu;
    MenuAdapter localMenuAdapter1 = new MenuAdapter(localMenuBuilder);
    this.mAdapter = localMenuAdapter1;
    ListPopupWindow localListPopupWindow2 = this.mPopup;
    MenuAdapter localMenuAdapter2 = this.mAdapter;
    localListPopupWindow2.setAdapter(localMenuAdapter2);
    this.mPopup.setModal(bool);
    View localView = this.mAnchorView;
    if (localView != null)
    {
      if (this.mTreeObserver == null)
        i = 1;
      ViewTreeObserver localViewTreeObserver = localView.getViewTreeObserver();
      this.mTreeObserver = localViewTreeObserver;
      if (i != 0)
        this.mTreeObserver.addOnGlobalLayoutListener(this);
      this.mPopup.setAnchorView(localView);
      ListPopupWindow localListPopupWindow3 = this.mPopup;
      MenuAdapter localMenuAdapter3 = this.mAdapter;
      int k = measureContentWidth(localMenuAdapter3);
      int m = this.mPopupMaxWidth;
      int n = Math.min(k, m);
      localListPopupWindow3.setContentWidth(n);
      this.mPopup.setInputMethodMode(2);
      this.mPopup.show();
      this.mPopup.getListView().setOnKeyListener(this);
    }
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  public void updateMenuView(boolean paramBoolean)
  {
    if (this.mAdapter == null)
      return;
    this.mAdapter.notifyDataSetChanged();
  }

  private class MenuAdapter extends BaseAdapter
  {
    private MenuBuilder mAdapterMenu;
    private int mExpandedIndex = -1;

    public MenuAdapter(MenuBuilder arg2)
    {
      Object localObject;
      this.mAdapterMenu = localObject;
      findExpandedIndex();
    }

    void findExpandedIndex()
    {
      MenuItemImpl localMenuItemImpl = MenuPopupHelper.this.mMenu.getExpandedItem();
      if (localMenuItemImpl != null)
      {
        ArrayList localArrayList = MenuPopupHelper.this.mMenu.getNonActionItems();
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
      ArrayList localArrayList;
      if (MenuPopupHelper.this.mOverflowOnly)
      {
        localArrayList = this.mAdapterMenu.getNonActionItems();
        if (this.mExpandedIndex >= 0)
          break label43;
      }
      label43: for (int i = localArrayList.size(); ; i = localArrayList.size() + -1)
      {
        return i;
        localArrayList = this.mAdapterMenu.getVisibleItems();
        break;
      }
    }

    public MenuItemImpl getItem(int paramInt)
    {
      if (MenuPopupHelper.this.mOverflowOnly);
      for (ArrayList localArrayList = this.mAdapterMenu.getNonActionItems(); ; localArrayList = this.mAdapterMenu.getVisibleItems())
      {
        if (this.mExpandedIndex >= 0)
        {
          int i = this.mExpandedIndex;
          if (paramInt >= i)
            paramInt += 1;
        }
        return (MenuItemImpl)localArrayList.get(paramInt);
      }
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      if (paramView == null)
      {
        LayoutInflater localLayoutInflater = MenuPopupHelper.this.mInflater;
        int i = MenuPopupHelper.ITEM_LAYOUT;
        paramView = localLayoutInflater.inflate(i, paramViewGroup, false);
      }
      MenuView.ItemView localItemView = (MenuView.ItemView)paramView;
      if (MenuPopupHelper.this.mForceShowIcon)
        ((ListMenuItemView)paramView).setForceShowIcon(true);
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
 * Qualified Name:     android.support.v7.internal.view.menu.MenuPopupHelper
 * JD-Core Version:    0.6.2
 */