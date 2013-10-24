package com.google.android.music.medialist;

import com.google.android.music.download.ContentIdentifier.Domain;

public abstract class PlaylistsList extends MediaList
{
  public PlaylistsList()
  {
    super(localDomain, true, true);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.PlaylistsList
 * JD-Core Version:    0.6.2
 */