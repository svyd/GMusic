package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public class SubMenuBuilder extends MenuBuilder
  implements SubMenu
{
  private MenuItemImpl mItem;
  private MenuBuilder mParentMenu;

  public SubMenuBuilder(Context paramContext, MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl)
  {
    super(paramContext);
    this.mParentMenu = paramMenuBuilder;
    this.mItem = paramMenuItemImpl;
  }

  public void clearHeader()
  {
  }

  public boolean collapseItemActionView(MenuItemImpl paramMenuItemImpl)
  {
    return this.mParentMenu.collapseItemActionView(paramMenuItemImpl);
  }

  public boolean dispatchMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem)
  {
    if ((super.dispatchMenuItemSelected(paramMenuBuilder, paramMenuItem)) || (this.mParentMenu.dispatchMenuItemSelected(paramMenuBuilder, paramMenuItem)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean expandItemActionView(MenuItemImpl paramMenuItemImpl)
  {
    return this.mParentMenu.expandItemActionView(paramMenuItemImpl);
  }

  public MenuItem getItem()
  {
    return this.mItem;
  }

  public Menu getParentMenu()
  {
    return this.mParentMenu;
  }

  public MenuBuilder getRootMenu()
  {
    return this.mParentMenu;
  }

  public boolean isQwertyMode()
  {
    return this.mParentMenu.isQwertyMode();
  }

  public boolean isShortcutsVisible()
  {
    return this.mParentMenu.isShortcutsVisible();
  }

  public void setCallback(MenuBuilder.Callback paramCallback)
  {
    this.mParentMenu.setCallback(paramCallback);
  }

  public SubMenu setHeaderIcon(int paramInt)
  {
    Drawable localDrawable = getContext().getResources().getDrawable(paramInt);
    MenuBuilder localMenuBuilder = super.setHeaderIconInt(localDrawable);
    return this;
  }

  public SubMenu setHeaderIcon(Drawable paramDrawable)
  {
    MenuBuilder localMenuBuilder = super.setHeaderIconInt(paramDrawable);
    return this;
  }

  public SubMenu setHeaderTitle(int paramInt)
  {
    String str = getContext().getResources().getString(paramInt);
    MenuBuilder localMenuBuilder = super.setHeaderTitleInt(str);
    return this;
  }

  public SubMenu setHeaderTitle(CharSequence paramCharSequence)
  {
    MenuBuilder localMenuBuilder = super.setHeaderTitleInt(paramCharSequence);
    return this;
  }

  public SubMenu setHeaderView(View paramView)
  {
    MenuBuilder localMenuBuilder = super.setHeaderViewInt(paramView);
    return this;
  }

  public SubMenu setIcon(int paramInt)
  {
    MenuItem localMenuItem = this.mItem.setIcon(paramInt);
    return this;
  }

  public SubMenu setIcon(Drawable paramDrawable)
  {
    MenuItem localMenuItem = this.mItem.setIcon(paramDrawable);
    return this;
  }

  public void setQwertyMode(boolean paramBoolean)
  {
    this.mParentMenu.setQwertyMode(paramBoolean);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.SubMenuBuilder
 * JD-Core Version:    0.6.2
 */