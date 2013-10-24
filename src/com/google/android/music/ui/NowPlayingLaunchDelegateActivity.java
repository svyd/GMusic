package com.google.android.music.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class NowPlayingLaunchDelegateActivity extends Activity
{
  public static Intent getLaunchIntent(Context paramContext)
  {
    Intent localIntent1 = new Intent(paramContext, NowPlayingLaunchDelegateActivity.class);
    Intent localIntent2 = localIntent1.addFlags(1073741824);
    Intent localIntent3 = localIntent1.addFlags(536870912);
    Intent localIntent4 = localIntent1.addFlags(67108864);
    Intent localIntent5 = localIntent1.addFlags(268435456);
    return localIntent1;
  }

  protected void onResume()
  {
    super.onResume();
    if (isTaskRoot())
      AppNavigation.goHome(this);
    finish();
  }

  protected void onStop()
  {
    AppNavigation.openNowPlayingDrawer(this);
    super.onStop();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.NowPlayingLaunchDelegateActivity
 * JD-Core Version:    0.6.2
 */