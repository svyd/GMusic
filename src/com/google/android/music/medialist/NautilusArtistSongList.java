package com.google.android.music.medialist;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import com.google.android.music.store.MusicContent.Artists;
import com.google.android.music.utils.MusicUtils;

public class NautilusArtistSongList extends NautilusSongList
{
  private String mArtistArtUrl;
  private final String mArtistName;
  private final String mNautilusArtistId;

  public NautilusArtistSongList(String paramString1, String paramString2)
  {
    this.mNautilusArtistId = paramString1;
    this.mArtistName = paramString2;
  }

  public String getAlbumArtUrl(Context paramContext)
  {
    if (this.mArtistArtUrl == null)
    {
      String str1 = this.mNautilusArtistId;
      String str2 = MusicUtils.getNautilusArtistArtUrl(paramContext, str1);
      this.mArtistArtUrl = str2;
    }
    return this.mArtistArtUrl;
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[2];
    String str1 = this.mNautilusArtistId;
    arrayOfString[0] = str1;
    String str2 = this.mArtistName;
    arrayOfString[1] = str2;
    return arrayOfString;
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.Artists.getAudioByNautilusArtistUri(this.mNautilusArtistId, null);
  }

  public String getName(Context paramContext)
  {
    return this.mArtistName;
  }

  public String getNautilusId()
  {
    return this.mNautilusArtistId;
  }

  public String getSecondaryName(Context paramContext)
  {
    return paramContext.getResources().getString(2131230751);
  }

  public boolean hasDifferentTrackArtists(Context paramContext)
  {
    return false;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.NautilusArtistSongList
 * JD-Core Version:    0.6.2
 */