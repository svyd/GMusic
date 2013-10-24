package com.google.android.music.medialist;

import com.google.android.music.download.ContentIdentifier.Domain;

public abstract class NautilusAlbumList extends AlbumList
  implements NautilusMediaList
{
  public NautilusAlbumList()
  {
    super(localDomain, false, false);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.NautilusAlbumList
 * JD-Core Version:    0.6.2
 */