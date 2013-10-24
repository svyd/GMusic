package com.google.android.music.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

@Deprecated
public class ActionBarTabsPager extends BaseActivity
{
  protected void replaceContent(Fragment paramFragment, boolean paramBoolean)
  {
    getSupportActionBar().setNavigationMode(0);
    super.replaceContent(paramFragment, paramBoolean);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.ActionBarTabsPager
 * JD-Core Version:    0.6.2
 */