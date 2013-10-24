package android.support.v7.internal.view.menu;

import android.graphics.drawable.Drawable;
import android.support.v4.internal.view.SupportSubMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

class SubMenuWrapperICS extends MenuWrapperICS
  implements SupportSubMenu
{
  SubMenuWrapperICS(SubMenu paramSubMenu)
  {
    super(paramSubMenu);
  }

  public void clearHeader()
  {
    ((SubMenu)this.mWrappedObject).clearHeader();
  }

  public MenuItem getItem()
  {
    MenuItem localMenuItem = ((SubMenu)this.mWrappedObject).getItem();
    return getMenuItemWrapper(localMenuItem);
  }

  public SubMenu setHeaderIcon(int paramInt)
  {
    SubMenu localSubMenu = ((SubMenu)this.mWrappedObject).setHeaderIcon(paramInt);
    return this;
  }

  public SubMenu setHeaderIcon(Drawable paramDrawable)
  {
    SubMenu localSubMenu = ((SubMenu)this.mWrappedObject).setHeaderIcon(paramDrawable);
    return this;
  }

  public SubMenu setHeaderTitle(int paramInt)
  {
    SubMenu localSubMenu = ((SubMenu)this.mWrappedObject).setHeaderTitle(paramInt);
    return this;
  }

  public SubMenu setHeaderTitle(CharSequence paramCharSequence)
  {
    SubMenu localSubMenu = ((SubMenu)this.mWrappedObject).setHeaderTitle(paramCharSequence);
    return this;
  }

  public SubMenu setHeaderView(View paramView)
  {
    SubMenu localSubMenu = ((SubMenu)this.mWrappedObject).setHeaderView(paramView);
    return this;
  }

  public SubMenu setIcon(int paramInt)
  {
    SubMenu localSubMenu = ((SubMenu)this.mWrappedObject).setIcon(paramInt);
    return this;
  }

  public SubMenu setIcon(Drawable paramDrawable)
  {
    SubMenu localSubMenu = ((SubMenu)this.mWrappedObject).setIcon(paramDrawable);
    return this;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.SubMenuWrapperICS
 * JD-Core Version:    0.6.2
 */