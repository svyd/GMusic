package android.support.v7.internal.view.menu;

import android.support.v4.internal.view.SupportMenuItem;
import android.view.MenuItem;
import android.view.SubMenu;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

abstract class BaseMenuWrapper<T> extends BaseWrapper<T>
{
  private HashMap<MenuItem, SupportMenuItem> mMenuItems;
  private HashMap<SubMenu, SubMenu> mSubMenus;

  BaseMenuWrapper(T paramT)
  {
    super(paramT);
  }

  final SupportMenuItem getMenuItemWrapper(MenuItem paramMenuItem)
  {
    SupportMenuItem localSupportMenuItem;
    if (paramMenuItem != null)
    {
      if (this.mMenuItems == null)
      {
        HashMap localHashMap = new HashMap();
        this.mMenuItems = localHashMap;
      }
      localSupportMenuItem = (SupportMenuItem)this.mMenuItems.get(paramMenuItem);
      if (localSupportMenuItem == null)
      {
        localSupportMenuItem = MenuWrapperFactory.createSupportMenuItemWrapper(paramMenuItem);
        Object localObject = this.mMenuItems.put(paramMenuItem, localSupportMenuItem);
      }
    }
    while (true)
    {
      return localSupportMenuItem;
      localSupportMenuItem = null;
    }
  }

  final SubMenu getSubMenuWrapper(SubMenu paramSubMenu)
  {
    Object localObject1;
    if (paramSubMenu != null)
    {
      if (this.mSubMenus == null)
      {
        HashMap localHashMap = new HashMap();
        this.mSubMenus = localHashMap;
      }
      localObject1 = (SubMenu)this.mSubMenus.get(paramSubMenu);
      if (localObject1 == null)
      {
        localObject1 = MenuWrapperFactory.createSupportSubMenuWrapper(paramSubMenu);
        Object localObject2 = this.mSubMenus.put(paramSubMenu, localObject1);
      }
    }
    while (true)
    {
      return localObject1;
      localObject1 = null;
    }
  }

  final void internalClear()
  {
    if (this.mMenuItems != null)
      this.mMenuItems.clear();
    if (this.mSubMenus == null)
      return;
    this.mSubMenus.clear();
  }

  final void internalRemoveGroup(int paramInt)
  {
    if (this.mMenuItems == null)
      return;
    Iterator localIterator = this.mMenuItems.keySet().iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      int i = ((MenuItem)localIterator.next()).getGroupId();
      if (paramInt != i)
        localIterator.remove();
    }
  }

  final void internalRemoveItem(int paramInt)
  {
    if (this.mMenuItems == null)
      return;
    Iterator localIterator = this.mMenuItems.keySet().iterator();
    int i;
    do
    {
      if (!localIterator.hasNext())
        return;
      i = ((MenuItem)localIterator.next()).getItemId();
    }
    while (paramInt == i);
    localIterator.remove();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.BaseMenuWrapper
 * JD-Core Version:    0.6.2
 */