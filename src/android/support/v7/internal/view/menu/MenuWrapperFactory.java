package android.support.v7.internal.view.menu;

import android.os.Build.VERSION;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.internal.view.SupportSubMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

public final class MenuWrapperFactory
{
  public static MenuItem createMenuItemWrapper(MenuItem paramMenuItem)
  {
    if (Build.VERSION.SDK_INT >= 16)
      paramMenuItem = new MenuItemWrapperJB(paramMenuItem);
    while (true)
    {
      return paramMenuItem;
      if (Build.VERSION.SDK_INT >= 14)
        paramMenuItem = new MenuItemWrapperICS(paramMenuItem);
    }
  }

  public static Menu createMenuWrapper(Menu paramMenu)
  {
    if (Build.VERSION.SDK_INT >= 14)
      paramMenu = new MenuWrapperICS(paramMenu);
    return paramMenu;
  }

  public static SupportMenuItem createSupportMenuItemWrapper(MenuItem paramMenuItem)
  {
    if (Build.VERSION.SDK_INT >= 16);
    for (Object localObject = new MenuItemWrapperJB(paramMenuItem); ; localObject = new MenuItemWrapperICS(paramMenuItem))
    {
      return localObject;
      if (Build.VERSION.SDK_INT < 14)
        break;
    }
    throw new UnsupportedOperationException();
  }

  public static SupportSubMenu createSupportSubMenuWrapper(SubMenu paramSubMenu)
  {
    if (Build.VERSION.SDK_INT >= 14)
      return new SubMenuWrapperICS(paramSubMenu);
    throw new UnsupportedOperationException();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.MenuWrapperFactory
 * JD-Core Version:    0.6.2
 */