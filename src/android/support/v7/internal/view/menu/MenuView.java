package android.support.v7.internal.view.menu;

public abstract interface MenuView
{
  public abstract void initialize(MenuBuilder paramMenuBuilder);

  public static abstract interface ItemView
  {
    public abstract MenuItemImpl getItemData();

    public abstract void initialize(MenuItemImpl paramMenuItemImpl, int paramInt);

    public abstract boolean prefersCondensedTitle();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.MenuView
 * JD-Core Version:    0.6.2
 */