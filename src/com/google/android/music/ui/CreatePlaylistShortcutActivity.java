package com.google.android.music.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class CreatePlaylistShortcutActivity extends FragmentActivity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    FragmentManager localFragmentManager = getSupportFragmentManager();
    new CreatePlaylistShortcutDialogFragment().show(localFragmentManager, "playlist_shortcut_dialog");
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.CreatePlaylistShortcutActivity
 * JD-Core Version:    0.6.2
 */