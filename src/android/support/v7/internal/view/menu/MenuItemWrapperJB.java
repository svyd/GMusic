package android.support.v7.internal.view.menu;

import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.View;

class MenuItemWrapperJB extends MenuItemWrapperICS
{
  MenuItemWrapperJB(MenuItem paramMenuItem)
  {
    super(paramMenuItem, false);
  }

  MenuItemWrapperICS.ActionProviderWrapper createActionProviderWrapper(ActionProvider paramActionProvider)
  {
    return new ActionProviderWrapperJB(paramActionProvider);
  }

  class ActionProviderWrapperJB extends MenuItemWrapperICS.ActionProviderWrapper
    implements android.support.v4.view.ActionProvider.VisibilityListener
  {
    android.view.ActionProvider.VisibilityListener mListener;

    public ActionProviderWrapperJB(ActionProvider arg2)
    {
      super(localActionProvider);
    }

    public boolean isVisible()
    {
      return this.mInner.isVisible();
    }

    public void onActionProviderVisibilityChanged(boolean paramBoolean)
    {
      if (this.mListener == null)
        return;
      this.mListener.onActionProviderVisibilityChanged(paramBoolean);
    }

    public View onCreateActionView(MenuItem paramMenuItem)
    {
      return this.mInner.onCreateActionView(paramMenuItem);
    }

    public boolean overridesItemVisibility()
    {
      return this.mInner.overridesItemVisibility();
    }

    public void refreshVisibility()
    {
      this.mInner.refreshVisibility();
    }

    public void setVisibilityListener(android.view.ActionProvider.VisibilityListener paramVisibilityListener)
    {
      this.mListener = paramVisibilityListener;
      ActionProvider localActionProvider = this.mInner;
      if (paramVisibilityListener != null);
      while (true)
      {
        localActionProvider.setVisibilityListener(this);
        return;
        this = null;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.MenuItemWrapperJB
 * JD-Core Version:    0.6.2
 */