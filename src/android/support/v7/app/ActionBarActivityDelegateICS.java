package android.support.v7.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle.Delegate;
import android.support.v7.internal.view.ActionModeWrapper;
import android.support.v7.internal.view.menu.MenuWrapperFactory;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.Window.Callback;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;

class ActionBarActivityDelegateICS extends ActionBarActivityDelegate
{
  Menu mMenu;

  ActionBarActivityDelegateICS(ActionBarActivity paramActionBarActivity)
  {
    super(paramActionBarActivity);
  }

  public void addContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    this.mActivity.superAddContentView(paramView, paramLayoutParams);
  }

  public ActionBar createSupportActionBar()
  {
    ActionBarActivity localActionBarActivity1 = this.mActivity;
    ActionBarActivity localActionBarActivity2 = this.mActivity;
    return new ActionBarImplICS(localActionBarActivity1, localActionBarActivity2);
  }

  Window.Callback createWindowCallbackWrapper(Window.Callback paramCallback)
  {
    return new WindowCallbackWrapper(paramCallback);
  }

  public ActionBarDrawerToggle.Delegate getDrawerToggleDelegate()
  {
    return null;
  }

  public void onActionModeFinished(ActionMode paramActionMode)
  {
    ActionBarActivity localActionBarActivity = this.mActivity;
    Context localContext = getActionBarThemedContext();
    ActionModeWrapper localActionModeWrapper = new ActionModeWrapper(localContext, paramActionMode);
    localActionBarActivity.onSupportActionModeFinished(localActionModeWrapper);
  }

  public void onActionModeStarted(ActionMode paramActionMode)
  {
    ActionBarActivity localActionBarActivity = this.mActivity;
    Context localContext = getActionBarThemedContext();
    ActionModeWrapper localActionModeWrapper = new ActionModeWrapper(localContext, paramActionMode);
    localActionBarActivity.onSupportActionModeStarted(localActionModeWrapper);
  }

  public boolean onBackPressed()
  {
    return false;
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
  }

  public void onContentChanged()
  {
    this.mActivity.onSupportContentChanged();
  }

  public void onCreate(Bundle paramBundle)
  {
    String str = getUiOptionsFromMetadata();
    if ("splitActionBarWhenNarrow".equals(str))
      this.mActivity.getWindow().setUiOptions(1, 1);
    super.onCreate(paramBundle);
    if (this.mHasActionBar)
      boolean bool1 = this.mActivity.requestWindowFeature(8);
    if (this.mOverlayActionBar)
      boolean bool2 = this.mActivity.requestWindowFeature(9);
    Window localWindow = this.mActivity.getWindow();
    Window.Callback localCallback1 = localWindow.getCallback();
    Window.Callback localCallback2 = createWindowCallbackWrapper(localCallback1);
    localWindow.setCallback(localCallback2);
  }

  public boolean onCreatePanelMenu(int paramInt, Menu paramMenu)
  {
    ActionBarActivity localActionBarActivity;
    Menu localMenu2;
    if ((paramInt == 0) || (paramInt == 8))
    {
      if (this.mMenu == null)
      {
        Menu localMenu1 = MenuWrapperFactory.createMenuWrapper(paramMenu);
        this.mMenu = localMenu1;
      }
      localActionBarActivity = this.mActivity;
      localMenu2 = this.mMenu;
    }
    for (boolean bool = localActionBarActivity.superOnCreatePanelMenu(paramInt, localMenu2); ; bool = this.mActivity.superOnCreatePanelMenu(paramInt, paramMenu))
      return bool;
  }

  public View onCreatePanelView(int paramInt)
  {
    return null;
  }

  public boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem)
  {
    if (paramInt == 0)
      paramMenuItem = MenuWrapperFactory.createMenuItemWrapper(paramMenuItem);
    return this.mActivity.superOnMenuItemSelected(paramInt, paramMenuItem);
  }

  public void onPostResume()
  {
  }

  public boolean onPreparePanel(int paramInt, View paramView, Menu paramMenu)
  {
    ActionBarActivity localActionBarActivity;
    Menu localMenu;
    if ((paramInt == 0) || (paramInt == 8))
    {
      localActionBarActivity = this.mActivity;
      localMenu = this.mMenu;
    }
    for (boolean bool = localActionBarActivity.superOnPreparePanel(paramInt, paramView, localMenu); ; bool = this.mActivity.superOnPreparePanel(paramInt, paramView, paramMenu))
      return bool;
  }

  public void onStop()
  {
  }

  public void onTitleChanged(CharSequence paramCharSequence)
  {
  }

  public void setContentView(int paramInt)
  {
    this.mActivity.superSetContentView(paramInt);
  }

  public void setContentView(View paramView)
  {
    this.mActivity.superSetContentView(paramView);
  }

  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    this.mActivity.superSetContentView(paramView, paramLayoutParams);
  }

  public void supportInvalidateOptionsMenu()
  {
    this.mMenu = null;
  }

  public boolean supportRequestWindowFeature(int paramInt)
  {
    return this.mActivity.requestWindowFeature(paramInt);
  }

  class WindowCallbackWrapper
    implements Window.Callback
  {
    final Window.Callback mWrapped;

    public WindowCallbackWrapper(Window.Callback arg2)
    {
      Object localObject;
      this.mWrapped = localObject;
    }

    public boolean dispatchGenericMotionEvent(MotionEvent paramMotionEvent)
    {
      return this.mWrapped.dispatchGenericMotionEvent(paramMotionEvent);
    }

    public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
    {
      return this.mWrapped.dispatchKeyEvent(paramKeyEvent);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent paramKeyEvent)
    {
      return this.mWrapped.dispatchKeyShortcutEvent(paramKeyEvent);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
    {
      return this.mWrapped.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent);
    }

    public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
    {
      return this.mWrapped.dispatchTouchEvent(paramMotionEvent);
    }

    public boolean dispatchTrackballEvent(MotionEvent paramMotionEvent)
    {
      return this.mWrapped.dispatchTrackballEvent(paramMotionEvent);
    }

    public void onActionModeFinished(ActionMode paramActionMode)
    {
      this.mWrapped.onActionModeFinished(paramActionMode);
      ActionBarActivityDelegateICS.this.onActionModeFinished(paramActionMode);
    }

    public void onActionModeStarted(ActionMode paramActionMode)
    {
      this.mWrapped.onActionModeStarted(paramActionMode);
      ActionBarActivityDelegateICS.this.onActionModeStarted(paramActionMode);
    }

    public void onAttachedToWindow()
    {
      this.mWrapped.onAttachedToWindow();
    }

    public void onContentChanged()
    {
      this.mWrapped.onContentChanged();
    }

    public boolean onCreatePanelMenu(int paramInt, Menu paramMenu)
    {
      return this.mWrapped.onCreatePanelMenu(paramInt, paramMenu);
    }

    public View onCreatePanelView(int paramInt)
    {
      return this.mWrapped.onCreatePanelView(paramInt);
    }

    public void onDetachedFromWindow()
    {
      this.mWrapped.onDetachedFromWindow();
    }

    public boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem)
    {
      return this.mWrapped.onMenuItemSelected(paramInt, paramMenuItem);
    }

    public boolean onMenuOpened(int paramInt, Menu paramMenu)
    {
      return this.mWrapped.onMenuOpened(paramInt, paramMenu);
    }

    public void onPanelClosed(int paramInt, Menu paramMenu)
    {
      this.mWrapped.onPanelClosed(paramInt, paramMenu);
    }

    public boolean onPreparePanel(int paramInt, View paramView, Menu paramMenu)
    {
      return this.mWrapped.onPreparePanel(paramInt, paramView, paramMenu);
    }

    public boolean onSearchRequested()
    {
      return this.mWrapped.onSearchRequested();
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams paramLayoutParams)
    {
      this.mWrapped.onWindowAttributesChanged(paramLayoutParams);
    }

    public void onWindowFocusChanged(boolean paramBoolean)
    {
      this.mWrapped.onWindowFocusChanged(paramBoolean);
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback paramCallback)
    {
      return this.mWrapped.onWindowStartingActionMode(paramCallback);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.ActionBarActivityDelegateICS
 * JD-Core Version:    0.6.2
 */