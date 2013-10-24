package com.google.android.music.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.google.android.gsf.Gservices;

public class SDCardSelectorActivity extends FragmentActivity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!Gservices.getBoolean(getContentResolver(), "music_enable_secondary_sdcards", false))
    {
      finish();
      return;
    }
    FragmentManager localFragmentManager = getSupportFragmentManager();
    new SDCardSelectorFragment().show(localFragmentManager, null);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.SDCardSelectorActivity
 * JD-Core Version:    0.6.2
 */