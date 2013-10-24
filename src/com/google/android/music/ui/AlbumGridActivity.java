package com.google.android.music.ui;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.music.medialist.AlbumList;

public class AlbumGridActivity extends BaseActivity
{
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getContent() != null)
      return;
    AlbumGridFragment localAlbumGridFragment = AlbumGridFragment.newInstance((AlbumList)getIntent().getParcelableExtra("albumlist"));
    replaceContent(localAlbumGridFragment, false);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.AlbumGridActivity
 * JD-Core Version:    0.6.2
 */