package com.google.android.music.medialist;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import com.google.android.music.download.artwork.AlbumIdSink;
import com.google.android.music.store.MusicContent.XAudio;
import com.google.android.music.utils.AlbumArtUtils;

public class AllSongsList extends AutoPlaylist
{
  public AllSongsList(int paramInt)
  {
    super(paramInt, 65534L);
  }

  public AllSongsList(int paramInt, boolean paramBoolean)
  {
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[1];
    String str = Integer.toString(getSortOrder());
    arrayOfString[0] = str;
    return arrayOfString;
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.XAudio.getAudioSorted(getSortParam());
  }

  public Bitmap getImage(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, boolean paramBoolean)
  {
    Context localContext = paramContext;
    int i = paramInt1;
    int j = paramInt2;
    AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
    boolean bool = paramBoolean;
    return AlbumArtUtils.getFauxAlbumArt(localContext, 5, false, 0L, i, j, null, null, null, localAlbumIdSink, bool);
  }

  protected int getListingNameResourceId()
  {
    return 2131230882;
  }

  public String getSecondaryName(Context paramContext)
  {
    return "";
  }

  protected int getTitleResourceId()
  {
    return 2131230883;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.AllSongsList
 * JD-Core Version:    0.6.2
 */