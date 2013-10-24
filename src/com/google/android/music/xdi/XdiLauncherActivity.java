package com.google.android.music.xdi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;

public class XdiLauncherActivity extends Activity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (XdiUtils.isAccountSelected(this));
    for (int i = 1; ; i = 6)
    {
      Intent localIntent1 = XdiContract.getBrowseIntent(XdiContentProvider.BASE_URI.buildUpon().appendPath("browse").build(), i);
      String str = XdiUtils.getMetaUri(0L).toString();
      Intent localIntent2 = localIntent1.putExtra("meta_uri", str);
      startActivity(localIntent1);
      finish();
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.XdiLauncherActivity
 * JD-Core Version:    0.6.2
 */