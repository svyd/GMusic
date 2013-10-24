package android.support.v7.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.SpinnerAdapter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

class ActionBarImplICS extends ActionBar
{
  final android.app.ActionBar mActionBar;
  final Activity mActivity;
  private ArrayList<WeakReference<ActionBarImplICS.OnMenuVisibilityListenerWrapper>> mAddedMenuVisWrappers;
  final ActionBar.Callback mCallback;

  public ActionBarImplICS(Activity paramActivity, ActionBar.Callback paramCallback)
  {
    this(paramActivity, paramCallback, true);
  }

  ActionBarImplICS(Activity paramActivity, ActionBar.Callback paramCallback, boolean paramBoolean)
  {
    ArrayList localArrayList = new ArrayList();
    this.mAddedMenuVisWrappers = localArrayList;
    this.mActivity = paramActivity;
    this.mCallback = paramCallback;
    android.app.ActionBar localActionBar = paramActivity.getActionBar();
    this.mActionBar = localActionBar;
    if (!paramBoolean)
      return;
    if ((getDisplayOptions() & 0x4) == 0)
      return;
    setHomeButtonEnabled(true);
  }

  public int getDisplayOptions()
  {
    return this.mActionBar.getDisplayOptions();
  }

  public Context getThemedContext()
  {
    return this.mActionBar.getThemedContext();
  }

  public void hide()
  {
    this.mActionBar.hide();
  }

  public void setBackgroundDrawable(Drawable paramDrawable)
  {
    this.mActionBar.setBackgroundDrawable(paramDrawable);
  }

  public void setCustomView(View paramView, ActionBar.LayoutParams paramLayoutParams)
  {
    android.app.ActionBar.LayoutParams localLayoutParams = new android.app.ActionBar.LayoutParams(paramLayoutParams);
    int i = paramLayoutParams.gravity;
    localLayoutParams.gravity = i;
    this.mActionBar.setCustomView(paramView, localLayoutParams);
  }

  public void setDisplayHomeAsUpEnabled(boolean paramBoolean)
  {
    this.mActionBar.setDisplayHomeAsUpEnabled(paramBoolean);
  }

  public void setDisplayOptions(int paramInt1, int paramInt2)
  {
    this.mActionBar.setDisplayOptions(paramInt1, paramInt2);
  }

  public void setDisplayShowCustomEnabled(boolean paramBoolean)
  {
    this.mActionBar.setDisplayShowCustomEnabled(paramBoolean);
  }

  public void setDisplayShowHomeEnabled(boolean paramBoolean)
  {
    this.mActionBar.setDisplayShowHomeEnabled(paramBoolean);
  }

  public void setHomeButtonEnabled(boolean paramBoolean)
  {
    this.mActionBar.setHomeButtonEnabled(paramBoolean);
  }

  public void setListNavigationCallbacks(SpinnerAdapter paramSpinnerAdapter, ActionBar.OnNavigationListener paramOnNavigationListener)
  {
    android.app.ActionBar localActionBar = this.mActionBar;
    if (paramOnNavigationListener != null);
    for (OnNavigationListenerWrapper localOnNavigationListenerWrapper = new OnNavigationListenerWrapper(paramOnNavigationListener); ; localOnNavigationListenerWrapper = null)
    {
      localActionBar.setListNavigationCallbacks(paramSpinnerAdapter, localOnNavigationListenerWrapper);
      return;
    }
  }

  public void setNavigationMode(int paramInt)
  {
    this.mActionBar.setNavigationMode(paramInt);
  }

  public void setSelectedNavigationItem(int paramInt)
  {
    this.mActionBar.setSelectedNavigationItem(paramInt);
  }

  public void setTitle(int paramInt)
  {
    this.mActionBar.setTitle(paramInt);
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    this.mActionBar.setTitle(paramCharSequence);
  }

  public void show()
  {
    this.mActionBar.show();
  }

  static class OnNavigationListenerWrapper
    implements android.app.ActionBar.OnNavigationListener
  {
    private final ActionBar.OnNavigationListener mWrappedListener;

    public OnNavigationListenerWrapper(ActionBar.OnNavigationListener paramOnNavigationListener)
    {
      this.mWrappedListener = paramOnNavigationListener;
    }

    public boolean onNavigationItemSelected(int paramInt, long paramLong)
    {
      return this.mWrappedListener.onNavigationItemSelected(paramInt, paramLong);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.ActionBarImplICS
 * JD-Core Version:    0.6.2
 */