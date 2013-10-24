package android.support.v7.internal.view.menu;

import android.content.Context;

public abstract interface MenuPresenter
{
  public abstract boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl);

  public abstract boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl);

  public abstract boolean flagActionItems();

  public abstract void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder);

  public abstract void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean);

  public abstract boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder);

  public abstract void updateMenuView(boolean paramBoolean);

  public static abstract interface Callback
  {
    public abstract void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean);

    public abstract boolean onOpenSubMenu(MenuBuilder paramMenuBuilder);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.MenuPresenter
 * JD-Core Version:    0.6.2
 */