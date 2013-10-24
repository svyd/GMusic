package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.ActionProvider.VisibilityListener;
import android.util.Log;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import java.lang.reflect.Method;

public class MenuItemWrapperICS extends BaseMenuWrapper<MenuItem>
  implements SupportMenuItem
{
  private final boolean mEmulateProviderVisibilityOverride;
  private boolean mLastRequestVisible;
  private Method mSetExclusiveCheckableMethod;

  MenuItemWrapperICS(MenuItem paramMenuItem)
  {
    this(paramMenuItem, true);
  }

  MenuItemWrapperICS(MenuItem paramMenuItem, boolean paramBoolean)
  {
    super(paramMenuItem);
    boolean bool = paramMenuItem.isVisible();
    this.mLastRequestVisible = bool;
    this.mEmulateProviderVisibilityOverride = paramBoolean;
  }

  final boolean checkActionProviderOverrideVisibility()
  {
    boolean bool = false;
    if (this.mLastRequestVisible)
    {
      android.support.v4.view.ActionProvider localActionProvider = getSupportActionProvider();
      if ((localActionProvider != null) && (localActionProvider.overridesItemVisibility()) && (!localActionProvider.isVisible()))
      {
        MenuItem localMenuItem = wrappedSetVisible(false);
        bool = true;
      }
    }
    return bool;
  }

  public boolean collapseActionView()
  {
    return ((MenuItem)this.mWrappedObject).collapseActionView();
  }

  ActionProviderWrapper createActionProviderWrapper(android.support.v4.view.ActionProvider paramActionProvider)
  {
    return new ActionProviderWrapper(paramActionProvider);
  }

  public boolean expandActionView()
  {
    return ((MenuItem)this.mWrappedObject).expandActionView();
  }

  public android.view.ActionProvider getActionProvider()
  {
    return ((MenuItem)this.mWrappedObject).getActionProvider();
  }

  public View getActionView()
  {
    View localView = ((MenuItem)this.mWrappedObject).getActionView();
    if ((localView instanceof CollapsibleActionViewWrapper))
      localView = ((CollapsibleActionViewWrapper)localView).getWrappedView();
    return localView;
  }

  public char getAlphabeticShortcut()
  {
    return ((MenuItem)this.mWrappedObject).getAlphabeticShortcut();
  }

  public int getGroupId()
  {
    return ((MenuItem)this.mWrappedObject).getGroupId();
  }

  public Drawable getIcon()
  {
    return ((MenuItem)this.mWrappedObject).getIcon();
  }

  public Intent getIntent()
  {
    return ((MenuItem)this.mWrappedObject).getIntent();
  }

  public int getItemId()
  {
    return ((MenuItem)this.mWrappedObject).getItemId();
  }

  public ContextMenu.ContextMenuInfo getMenuInfo()
  {
    return ((MenuItem)this.mWrappedObject).getMenuInfo();
  }

  public char getNumericShortcut()
  {
    return ((MenuItem)this.mWrappedObject).getNumericShortcut();
  }

  public int getOrder()
  {
    return ((MenuItem)this.mWrappedObject).getOrder();
  }

  public SubMenu getSubMenu()
  {
    SubMenu localSubMenu = ((MenuItem)this.mWrappedObject).getSubMenu();
    return getSubMenuWrapper(localSubMenu);
  }

  public android.support.v4.view.ActionProvider getSupportActionProvider()
  {
    ActionProviderWrapper localActionProviderWrapper = (ActionProviderWrapper)((MenuItem)this.mWrappedObject).getActionProvider();
    if (localActionProviderWrapper != null);
    for (android.support.v4.view.ActionProvider localActionProvider = localActionProviderWrapper.mInner; ; localActionProvider = null)
      return localActionProvider;
  }

  public CharSequence getTitle()
  {
    return ((MenuItem)this.mWrappedObject).getTitle();
  }

  public CharSequence getTitleCondensed()
  {
    return ((MenuItem)this.mWrappedObject).getTitleCondensed();
  }

  public boolean hasSubMenu()
  {
    return ((MenuItem)this.mWrappedObject).hasSubMenu();
  }

  public boolean isActionViewExpanded()
  {
    return ((MenuItem)this.mWrappedObject).isActionViewExpanded();
  }

  public boolean isCheckable()
  {
    return ((MenuItem)this.mWrappedObject).isCheckable();
  }

  public boolean isChecked()
  {
    return ((MenuItem)this.mWrappedObject).isChecked();
  }

  public boolean isEnabled()
  {
    return ((MenuItem)this.mWrappedObject).isEnabled();
  }

  public boolean isVisible()
  {
    return ((MenuItem)this.mWrappedObject).isVisible();
  }

  public MenuItem setActionProvider(android.view.ActionProvider paramActionProvider)
  {
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setActionProvider(paramActionProvider);
    if ((paramActionProvider != null) && (this.mEmulateProviderVisibilityOverride))
      boolean bool = checkActionProviderOverrideVisibility();
    return this;
  }

  public MenuItem setActionView(int paramInt)
  {
    MenuItem localMenuItem1 = ((MenuItem)this.mWrappedObject).setActionView(paramInt);
    View localView = ((MenuItem)this.mWrappedObject).getActionView();
    if ((localView instanceof android.support.v7.view.CollapsibleActionView))
    {
      MenuItem localMenuItem2 = (MenuItem)this.mWrappedObject;
      CollapsibleActionViewWrapper localCollapsibleActionViewWrapper = new CollapsibleActionViewWrapper(localView);
      MenuItem localMenuItem3 = localMenuItem2.setActionView(localCollapsibleActionViewWrapper);
    }
    return this;
  }

  public MenuItem setActionView(View paramView)
  {
    if ((paramView instanceof android.support.v7.view.CollapsibleActionView))
      paramView = new CollapsibleActionViewWrapper(paramView);
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setActionView(paramView);
    return this;
  }

  public MenuItem setAlphabeticShortcut(char paramChar)
  {
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setAlphabeticShortcut(paramChar);
    return this;
  }

  public MenuItem setCheckable(boolean paramBoolean)
  {
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setCheckable(paramBoolean);
    return this;
  }

  public MenuItem setChecked(boolean paramBoolean)
  {
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setChecked(paramBoolean);
    return this;
  }

  public MenuItem setEnabled(boolean paramBoolean)
  {
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setEnabled(paramBoolean);
    return this;
  }

  public void setExclusiveCheckable(boolean paramBoolean)
  {
    try
    {
      if (this.mSetExclusiveCheckableMethod == null)
      {
        Class localClass1 = ((MenuItem)this.mWrappedObject).getClass();
        Class[] arrayOfClass = new Class[1];
        Class localClass2 = Boolean.TYPE;
        arrayOfClass[0] = localClass2;
        Method localMethod1 = localClass1.getDeclaredMethod("setExclusiveCheckable", arrayOfClass);
        this.mSetExclusiveCheckableMethod = localMethod1;
      }
      Method localMethod2 = this.mSetExclusiveCheckableMethod;
      Object localObject1 = this.mWrappedObject;
      Object[] arrayOfObject = new Object[1];
      Boolean localBoolean = Boolean.valueOf(paramBoolean);
      arrayOfObject[0] = localBoolean;
      Object localObject2 = localMethod2.invoke(localObject1, arrayOfObject);
      return;
    }
    catch (Exception localException)
    {
      int i = Log.w("MenuItemWrapper", "Error while calling setExclusiveCheckable", localException);
    }
  }

  public MenuItem setIcon(int paramInt)
  {
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setIcon(paramInt);
    return this;
  }

  public MenuItem setIcon(Drawable paramDrawable)
  {
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setIcon(paramDrawable);
    return this;
  }

  public MenuItem setIntent(Intent paramIntent)
  {
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setIntent(paramIntent);
    return this;
  }

  public MenuItem setNumericShortcut(char paramChar)
  {
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setNumericShortcut(paramChar);
    return this;
  }

  public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener paramOnActionExpandListener)
  {
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setOnActionExpandListener(paramOnActionExpandListener);
    return this;
  }

  public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener paramOnMenuItemClickListener)
  {
    MenuItem localMenuItem1 = (MenuItem)this.mWrappedObject;
    if (paramOnMenuItemClickListener != null);
    for (OnMenuItemClickListenerWrapper localOnMenuItemClickListenerWrapper = new OnMenuItemClickListenerWrapper(paramOnMenuItemClickListener); ; localOnMenuItemClickListenerWrapper = null)
    {
      MenuItem localMenuItem2 = localMenuItem1.setOnMenuItemClickListener(localOnMenuItemClickListenerWrapper);
      return this;
    }
  }

  public MenuItem setShortcut(char paramChar1, char paramChar2)
  {
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setShortcut(paramChar1, paramChar2);
    return this;
  }

  public void setShowAsAction(int paramInt)
  {
    ((MenuItem)this.mWrappedObject).setShowAsAction(paramInt);
  }

  public MenuItem setShowAsActionFlags(int paramInt)
  {
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setShowAsActionFlags(paramInt);
    return this;
  }

  public SupportMenuItem setSupportActionProvider(android.support.v4.view.ActionProvider paramActionProvider)
  {
    MenuItem localMenuItem1 = (MenuItem)this.mWrappedObject;
    if (paramActionProvider != null);
    for (ActionProviderWrapper localActionProviderWrapper = createActionProviderWrapper(paramActionProvider); ; localActionProviderWrapper = null)
    {
      MenuItem localMenuItem2 = localMenuItem1.setActionProvider(localActionProviderWrapper);
      return this;
    }
  }

  public MenuItem setTitle(int paramInt)
  {
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setTitle(paramInt);
    return this;
  }

  public MenuItem setTitle(CharSequence paramCharSequence)
  {
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setTitle(paramCharSequence);
    return this;
  }

  public MenuItem setTitleCondensed(CharSequence paramCharSequence)
  {
    MenuItem localMenuItem = ((MenuItem)this.mWrappedObject).setTitleCondensed(paramCharSequence);
    return this;
  }

  public MenuItem setVisible(boolean paramBoolean)
  {
    if (this.mEmulateProviderVisibilityOverride)
    {
      this.mLastRequestVisible = paramBoolean;
      if (!checkActionProviderOverrideVisibility());
    }
    while (true)
    {
      return this;
      this = wrappedSetVisible(paramBoolean);
    }
  }

  final MenuItem wrappedSetVisible(boolean paramBoolean)
  {
    return ((MenuItem)this.mWrappedObject).setVisible(paramBoolean);
  }

  static class CollapsibleActionViewWrapper extends FrameLayout
    implements android.view.CollapsibleActionView
  {
    final android.support.v7.view.CollapsibleActionView mWrappedView;

    CollapsibleActionViewWrapper(View paramView)
    {
      super();
      android.support.v7.view.CollapsibleActionView localCollapsibleActionView = (android.support.v7.view.CollapsibleActionView)paramView;
      this.mWrappedView = localCollapsibleActionView;
      addView(paramView);
    }

    View getWrappedView()
    {
      return (View)this.mWrappedView;
    }

    public void onActionViewCollapsed()
    {
      this.mWrappedView.onActionViewCollapsed();
    }

    public void onActionViewExpanded()
    {
      this.mWrappedView.onActionViewExpanded();
    }
  }

  class ActionProviderWrapper extends android.view.ActionProvider
  {
    final android.support.v4.view.ActionProvider mInner;

    public ActionProviderWrapper(android.support.v4.view.ActionProvider arg2)
    {
      super();
      this.mInner = localObject;
      if (!MenuItemWrapperICS.this.mEmulateProviderVisibilityOverride)
        return;
      android.support.v4.view.ActionProvider localActionProvider = this.mInner;
      ActionProvider.VisibilityListener local1 = new ActionProvider.VisibilityListener()
      {
        public void onActionProviderVisibilityChanged(boolean paramAnonymousBoolean)
        {
          if (!MenuItemWrapperICS.ActionProviderWrapper.this.mInner.overridesItemVisibility())
            return;
          if (!MenuItemWrapperICS.this.mLastRequestVisible)
            return;
          MenuItem localMenuItem = MenuItemWrapperICS.this.wrappedSetVisible(paramAnonymousBoolean);
        }
      };
      localActionProvider.setVisibilityListener(local1);
    }

    public boolean hasSubMenu()
    {
      return this.mInner.hasSubMenu();
    }

    public View onCreateActionView()
    {
      if (MenuItemWrapperICS.this.mEmulateProviderVisibilityOverride)
        boolean bool = MenuItemWrapperICS.this.checkActionProviderOverrideVisibility();
      return this.mInner.onCreateActionView();
    }

    public boolean onPerformDefaultAction()
    {
      return this.mInner.onPerformDefaultAction();
    }

    public void onPrepareSubMenu(SubMenu paramSubMenu)
    {
      android.support.v4.view.ActionProvider localActionProvider = this.mInner;
      SubMenu localSubMenu = MenuItemWrapperICS.this.getSubMenuWrapper(paramSubMenu);
      localActionProvider.onPrepareSubMenu(localSubMenu);
    }
  }

  private class OnMenuItemClickListenerWrapper extends BaseWrapper<MenuItem.OnMenuItemClickListener>
    implements MenuItem.OnMenuItemClickListener
  {
    OnMenuItemClickListenerWrapper(MenuItem.OnMenuItemClickListener arg2)
    {
      super();
    }

    public boolean onMenuItemClick(MenuItem paramMenuItem)
    {
      MenuItem.OnMenuItemClickListener localOnMenuItemClickListener = (MenuItem.OnMenuItemClickListener)this.mWrappedObject;
      SupportMenuItem localSupportMenuItem = MenuItemWrapperICS.this.getMenuItemWrapper(paramMenuItem);
      return localOnMenuItemClickListener.onMenuItemClick(localSupportMenuItem);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.MenuItemWrapperICS
 * JD-Core Version:    0.6.2
 */