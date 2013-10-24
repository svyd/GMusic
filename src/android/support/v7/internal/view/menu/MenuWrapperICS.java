package android.support.v7.internal.view.menu;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.internal.view.SupportMenuItem;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

class MenuWrapperICS extends BaseMenuWrapper<Menu>
  implements SupportMenu
{
  MenuWrapperICS(Menu paramMenu)
  {
    super(paramMenu);
  }

  public MenuItem add(int paramInt)
  {
    MenuItem localMenuItem = ((Menu)this.mWrappedObject).add(paramInt);
    return getMenuItemWrapper(localMenuItem);
  }

  public MenuItem add(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    MenuItem localMenuItem = ((Menu)this.mWrappedObject).add(paramInt1, paramInt2, paramInt3, paramInt4);
    return getMenuItemWrapper(localMenuItem);
  }

  public MenuItem add(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence)
  {
    MenuItem localMenuItem = ((Menu)this.mWrappedObject).add(paramInt1, paramInt2, paramInt3, paramCharSequence);
    return getMenuItemWrapper(localMenuItem);
  }

  public MenuItem add(CharSequence paramCharSequence)
  {
    MenuItem localMenuItem = ((Menu)this.mWrappedObject).add(paramCharSequence);
    return getMenuItemWrapper(localMenuItem);
  }

  public int addIntentOptions(int paramInt1, int paramInt2, int paramInt3, ComponentName paramComponentName, Intent[] paramArrayOfIntent, Intent paramIntent, int paramInt4, MenuItem[] paramArrayOfMenuItem)
  {
    MenuItem[] arrayOfMenuItem = null;
    if (paramArrayOfMenuItem != null)
      arrayOfMenuItem = new MenuItem[paramArrayOfMenuItem.length];
    Menu localMenu = (Menu)this.mWrappedObject;
    int i = paramInt1;
    int j = paramInt2;
    int k = paramInt3;
    ComponentName localComponentName = paramComponentName;
    Intent[] arrayOfIntent = paramArrayOfIntent;
    Intent localIntent = paramIntent;
    int m = paramInt4;
    int n = localMenu.addIntentOptions(i, j, k, localComponentName, arrayOfIntent, localIntent, m, arrayOfMenuItem);
    if (arrayOfMenuItem != null)
    {
      int i1 = 0;
      int i2 = arrayOfMenuItem.length;
      while (i1 < i2)
      {
        MenuItem localMenuItem = arrayOfMenuItem[i1];
        SupportMenuItem localSupportMenuItem = getMenuItemWrapper(localMenuItem);
        paramArrayOfMenuItem[i1] = localSupportMenuItem;
        i1 += 1;
      }
    }
    return n;
  }

  public SubMenu addSubMenu(int paramInt)
  {
    SubMenu localSubMenu = ((Menu)this.mWrappedObject).addSubMenu(paramInt);
    return getSubMenuWrapper(localSubMenu);
  }

  public SubMenu addSubMenu(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    SubMenu localSubMenu = ((Menu)this.mWrappedObject).addSubMenu(paramInt1, paramInt2, paramInt3, paramInt4);
    return getSubMenuWrapper(localSubMenu);
  }

  public SubMenu addSubMenu(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence)
  {
    SubMenu localSubMenu = ((Menu)this.mWrappedObject).addSubMenu(paramInt1, paramInt2, paramInt3, paramCharSequence);
    return getSubMenuWrapper(localSubMenu);
  }

  public SubMenu addSubMenu(CharSequence paramCharSequence)
  {
    SubMenu localSubMenu = ((Menu)this.mWrappedObject).addSubMenu(paramCharSequence);
    return getSubMenuWrapper(localSubMenu);
  }

  public void clear()
  {
    internalClear();
    ((Menu)this.mWrappedObject).clear();
  }

  public void close()
  {
    ((Menu)this.mWrappedObject).close();
  }

  public MenuItem findItem(int paramInt)
  {
    MenuItem localMenuItem = ((Menu)this.mWrappedObject).findItem(paramInt);
    return getMenuItemWrapper(localMenuItem);
  }

  public MenuItem getItem(int paramInt)
  {
    MenuItem localMenuItem = ((Menu)this.mWrappedObject).getItem(paramInt);
    return getMenuItemWrapper(localMenuItem);
  }

  public boolean hasVisibleItems()
  {
    return ((Menu)this.mWrappedObject).hasVisibleItems();
  }

  public boolean isShortcutKey(int paramInt, KeyEvent paramKeyEvent)
  {
    return ((Menu)this.mWrappedObject).isShortcutKey(paramInt, paramKeyEvent);
  }

  public boolean performIdentifierAction(int paramInt1, int paramInt2)
  {
    return ((Menu)this.mWrappedObject).performIdentifierAction(paramInt1, paramInt2);
  }

  public boolean performShortcut(int paramInt1, KeyEvent paramKeyEvent, int paramInt2)
  {
    return ((Menu)this.mWrappedObject).performShortcut(paramInt1, paramKeyEvent, paramInt2);
  }

  public void removeGroup(int paramInt)
  {
    internalRemoveGroup(paramInt);
    ((Menu)this.mWrappedObject).removeGroup(paramInt);
  }

  public void removeItem(int paramInt)
  {
    internalRemoveItem(paramInt);
    ((Menu)this.mWrappedObject).removeItem(paramInt);
  }

  public void setGroupCheckable(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    ((Menu)this.mWrappedObject).setGroupCheckable(paramInt, paramBoolean1, paramBoolean2);
  }

  public void setGroupEnabled(int paramInt, boolean paramBoolean)
  {
    ((Menu)this.mWrappedObject).setGroupEnabled(paramInt, paramBoolean);
  }

  public void setGroupVisible(int paramInt, boolean paramBoolean)
  {
    ((Menu)this.mWrappedObject).setGroupVisible(paramInt, paramBoolean);
  }

  public void setQwertyMode(boolean paramBoolean)
  {
    ((Menu)this.mWrappedObject).setQwertyMode(paramBoolean);
  }

  public int size()
  {
    return ((Menu)this.mWrappedObject).size();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.MenuWrapperICS
 * JD-Core Version:    0.6.2
 */