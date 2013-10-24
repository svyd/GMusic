package android.support.v7.app;

class ActionBarActivityDelegateJB extends ActionBarActivityDelegateICS
{
  ActionBarActivityDelegateJB(ActionBarActivity paramActionBarActivity)
  {
    super(paramActionBarActivity);
  }

  public ActionBar createSupportActionBar()
  {
    ActionBarActivity localActionBarActivity1 = this.mActivity;
    ActionBarActivity localActionBarActivity2 = this.mActivity;
    return new ActionBarImplJB(localActionBarActivity1, localActionBarActivity2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.ActionBarActivityDelegateJB
 * JD-Core Version:    0.6.2
 */