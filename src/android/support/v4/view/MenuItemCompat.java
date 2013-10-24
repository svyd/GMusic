package android.support.v4.view;

import android.os.Build.VERSION;
import android.support.v4.internal.view.SupportMenuItem;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class MenuItemCompat
{
  static final MenuVersionImpl IMPL = new BaseMenuVersionImpl();

  static
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 14)
    {
      IMPL = new IcsMenuVersionImpl();
      return;
    }
    if (i >= 11)
    {
      IMPL = new HoneycombMenuVersionImpl();
      return;
    }
  }

  public static ActionProvider getActionProvider(MenuItem paramMenuItem)
  {
    if ((paramMenuItem instanceof SupportMenuItem));
    for (ActionProvider localActionProvider = ((SupportMenuItem)paramMenuItem).getSupportActionProvider(); ; localActionProvider = null)
    {
      return localActionProvider;
      int i = Log.w("MenuItemCompat", "getActionProvider: item does not implement SupportMenuItem; returning null");
    }
  }

  public static View getActionView(MenuItem paramMenuItem)
  {
    if ((paramMenuItem instanceof SupportMenuItem));
    for (View localView = ((SupportMenuItem)paramMenuItem).getActionView(); ; localView = IMPL.getActionView(paramMenuItem))
      return localView;
  }

  public static MenuItem setActionProvider(MenuItem paramMenuItem, ActionProvider paramActionProvider)
  {
    if ((paramMenuItem instanceof SupportMenuItem))
      paramMenuItem = ((SupportMenuItem)paramMenuItem).setSupportActionProvider(paramActionProvider);
    while (true)
    {
      return paramMenuItem;
      int i = Log.w("MenuItemCompat", "setActionProvider: item does not implement SupportMenuItem; ignoring");
    }
  }

  public static MenuItem setActionView(MenuItem paramMenuItem, int paramInt)
  {
    if ((paramMenuItem instanceof SupportMenuItem));
    for (MenuItem localMenuItem = ((SupportMenuItem)paramMenuItem).setActionView(paramInt); ; localMenuItem = IMPL.setActionView(paramMenuItem, paramInt))
      return localMenuItem;
  }

  public static MenuItem setActionView(MenuItem paramMenuItem, View paramView)
  {
    if ((paramMenuItem instanceof SupportMenuItem));
    for (MenuItem localMenuItem = ((SupportMenuItem)paramMenuItem).setActionView(paramView); ; localMenuItem = IMPL.setActionView(paramMenuItem, paramView))
      return localMenuItem;
  }

  public static void setShowAsAction(MenuItem paramMenuItem, int paramInt)
  {
    if ((paramMenuItem instanceof SupportMenuItem))
    {
      ((SupportMenuItem)paramMenuItem).setShowAsAction(paramInt);
      return;
    }
    IMPL.setShowAsAction(paramMenuItem, paramInt);
  }

  static class IcsMenuVersionImpl extends MenuItemCompat.HoneycombMenuVersionImpl
  {
  }

  static class HoneycombMenuVersionImpl
    implements MenuItemCompat.MenuVersionImpl
  {
    public View getActionView(MenuItem paramMenuItem)
    {
      return MenuItemCompatHoneycomb.getActionView(paramMenuItem);
    }

    public MenuItem setActionView(MenuItem paramMenuItem, int paramInt)
    {
      return MenuItemCompatHoneycomb.setActionView(paramMenuItem, paramInt);
    }

    public MenuItem setActionView(MenuItem paramMenuItem, View paramView)
    {
      return MenuItemCompatHoneycomb.setActionView(paramMenuItem, paramView);
    }

    public void setShowAsAction(MenuItem paramMenuItem, int paramInt)
    {
      MenuItemCompatHoneycomb.setShowAsAction(paramMenuItem, paramInt);
    }
  }

  static class BaseMenuVersionImpl
    implements MenuItemCompat.MenuVersionImpl
  {
    public View getActionView(MenuItem paramMenuItem)
    {
      return null;
    }

    public MenuItem setActionView(MenuItem paramMenuItem, int paramInt)
    {
      return paramMenuItem;
    }

    public MenuItem setActionView(MenuItem paramMenuItem, View paramView)
    {
      return paramMenuItem;
    }

    public void setShowAsAction(MenuItem paramMenuItem, int paramInt)
    {
    }
  }

  public static abstract interface OnActionExpandListener
  {
    public abstract boolean onMenuItemActionCollapse(MenuItem paramMenuItem);

    public abstract boolean onMenuItemActionExpand(MenuItem paramMenuItem);
  }

  static abstract interface MenuVersionImpl
  {
    public abstract View getActionView(MenuItem paramMenuItem);

    public abstract MenuItem setActionView(MenuItem paramMenuItem, int paramInt);

    public abstract MenuItem setActionView(MenuItem paramMenuItem, View paramView);

    public abstract void setShowAsAction(MenuItem paramMenuItem, int paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.MenuItemCompat
 * JD-Core Version:    0.6.2
 */