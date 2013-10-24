package android.support.v7.app;

import android.os.Bundle;
import android.view.Window;

class ActionBarActivityDelegateHC extends ActionBarActivityDelegateBase
{
  ActionBarActivityDelegateHC(ActionBarActivity paramActionBarActivity)
  {
    super(paramActionBarActivity);
  }

  public ActionBar createSupportActionBar()
  {
    ensureSubDecor();
    ActionBarActivity localActionBarActivity1 = this.mActivity;
    ActionBarActivity localActionBarActivity2 = this.mActivity;
    return new ActionBarImplHC(localActionBarActivity1, localActionBarActivity2);
  }

  void onCreate(Bundle paramBundle)
  {
    boolean bool = this.mActivity.getWindow().requestFeature(10);
    super.onCreate(paramBundle);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.ActionBarActivityDelegateHC
 * JD-Core Version:    0.6.2
 */