package com.google.android.music.ui;

import android.support.v4.app.Fragment;
import com.google.android.music.medialist.MediaList;

public abstract interface MusicFragment
{
  public abstract long getAlbumId();

  public abstract String getAlbumMetajamId();

  public abstract long getArtistId();

  public abstract String getArtistMetajamId();

  public abstract Fragment getFragment();

  public abstract MediaList getFragmentMediaList();

  public abstract void onTutorialCardClosed();
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.MusicFragment
 * JD-Core Version:    0.6.2
 */