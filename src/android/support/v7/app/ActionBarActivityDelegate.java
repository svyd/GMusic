package android.support.v7.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle.Delegate;
import android.support.v4.app.NavUtils;
import android.support.v7.appcompat.R.styleable;
import android.support.v7.internal.view.SupportMenuInflater;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

abstract class ActionBarActivityDelegate
{
  private ActionBar mActionBar;
  final ActionBarActivity mActivity;
  private boolean mEnableDefaultActionBarUp;
  boolean mHasActionBar;
  private MenuInflater mMenuInflater;
  boolean mOverlayActionBar;

  ActionBarActivityDelegate(ActionBarActivity paramActionBarActivity)
  {
    this.mActivity = paramActionBarActivity;
  }

  static ActionBarActivityDelegate createDelegate(ActionBarActivity paramActionBarActivity)
  {
    int i = Build.VERSION.SDK_INT;
    Object localObject;
    if (i >= 16)
      localObject = new ActionBarActivityDelegateJB(paramActionBarActivity);
    while (true)
    {
      return localObject;
      if (i >= 14)
        localObject = new ActionBarActivityDelegateICS(paramActionBarActivity);
      else if (i >= 11)
        localObject = new ActionBarActivityDelegateHC(paramActionBarActivity);
      else
        localObject = new ActionBarActivityDelegateBase(paramActionBarActivity);
    }
  }

  abstract void addContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams);

  abstract ActionBar createSupportActionBar();

  protected final Context getActionBarThemedContext()
  {
    Object localObject = this.mActivity;
    ActionBar localActionBar = getSupportActionBar();
    if (localActionBar != null)
      localObject = localActionBar.getThemedContext();
    return localObject;
  }

  abstract ActionBarDrawerToggle.Delegate getDrawerToggleDelegate();

  MenuInflater getMenuInflater()
  {
    SupportMenuInflater localSupportMenuInflater1;
    if (this.mMenuInflater == null)
    {
      ActionBar localActionBar = getSupportActionBar();
      if (localActionBar == null)
        break label40;
      Context localContext = localActionBar.getThemedContext();
      localSupportMenuInflater1 = new SupportMenuInflater(localContext);
    }
    label40: SupportMenuInflater localSupportMenuInflater2;
    for (this.mMenuInflater = localSupportMenuInflater1; ; this.mMenuInflater = localSupportMenuInflater2)
    {
      return this.mMenuInflater;
      ActionBarActivity localActionBarActivity = this.mActivity;
      localSupportMenuInflater2 = new SupportMenuInflater(localActionBarActivity);
    }
  }

  final ActionBar getSupportActionBar()
  {
    if ((this.mHasActionBar) || (this.mOverlayActionBar))
      if (this.mActionBar == null)
      {
        ActionBar localActionBar = createSupportActionBar();
        this.mActionBar = localActionBar;
        if (this.mEnableDefaultActionBarUp)
          this.mActionBar.setDisplayHomeAsUpEnabled(true);
      }
    while (true)
    {
      return this.mActionBar;
      this.mActionBar = null;
    }
  }

  protected final String getUiOptionsFromMetadata()
  {
    try
    {
      PackageManager localPackageManager = this.mActivity.getPackageManager();
      ComponentName localComponentName = this.mActivity.getComponentName();
      ActivityInfo localActivityInfo = localPackageManager.getActivityInfo(localComponentName, 128);
      localObject = null;
      if (localActivityInfo.metaData != null)
      {
        String str1 = localActivityInfo.metaData.getString("android.support.UI_OPTIONS");
        localObject = str1;
      }
      return localObject;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      while (true)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("getUiOptionsFromMetadata: Activity '");
        String str2 = this.mActivity.getClass().getSimpleName();
        String str3 = str2 + "' not in manifest";
        int i = Log.e("ActionBarActivityDelegate", str3);
        Object localObject = null;
      }
    }
  }

  abstract boolean onBackPressed();

  abstract void onConfigurationChanged(Configuration paramConfiguration);

  abstract void onContentChanged();

  void onCreate(Bundle paramBundle)
  {
    ActionBarActivity localActionBarActivity = this.mActivity;
    int[] arrayOfInt = R.styleable.ActionBarWindow;
    TypedArray localTypedArray = localActionBarActivity.obtainStyledAttributes(arrayOfInt);
    if (!localTypedArray.hasValue(0))
    {
      localTypedArray.recycle();
      throw new IllegalStateException("You need to use a Theme.AppCompat theme (or descendant) with this activity.");
    }
    boolean bool1 = localTypedArray.getBoolean(0, false);
    this.mHasActionBar = bool1;
    boolean bool2 = localTypedArray.getBoolean(1, false);
    this.mOverlayActionBar = bool2;
    localTypedArray.recycle();
    if (NavUtils.getParentActivityName(this.mActivity) == null)
      return;
    if (this.mActionBar == null)
    {
      this.mEnableDefaultActionBarUp = true;
      return;
    }
    this.mActionBar.setDisplayHomeAsUpEnabled(true);
  }

  abstract boolean onCreatePanelMenu(int paramInt, Menu paramMenu);

  abstract View onCreatePanelView(int paramInt);

  abstract boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem);

  abstract void onPostResume();

  boolean onPrepareOptionsPanel(View paramView, Menu paramMenu)
  {
    if (Build.VERSION.SDK_INT < 16);
    for (boolean bool = this.mActivity.onPrepareOptionsMenu(paramMenu); ; bool = this.mActivity.superOnPrepareOptionsPanel(paramView, paramMenu))
      return bool;
  }

  abstract boolean onPreparePanel(int paramInt, View paramView, Menu paramMenu);

  abstract void onStop();

  abstract void onTitleChanged(CharSequence paramCharSequence);

  abstract void setContentView(int paramInt);

  abstract void setContentView(View paramView);

  abstract void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams);

  abstract void supportInvalidateOptionsMenu();

  abstract boolean supportRequestWindowFeature(int paramInt);
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.ActionBarActivityDelegate
 * JD-Core Version:    0.6.2
 */