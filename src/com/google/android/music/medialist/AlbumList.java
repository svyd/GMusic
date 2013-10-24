package com.google.android.music.medialist;

import com.google.android.music.download.ContentIdentifier.Domain;

public abstract class AlbumList extends MediaList
{
  public AlbumList(ContentIdentifier.Domain paramDomain, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramDomain, paramBoolean1, paramBoolean2);
  }

  public AlbumList(boolean paramBoolean)
  {
    super(localDomain, paramBoolean, false);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.AlbumList
 * JD-Core Version:    0.6.2
 */