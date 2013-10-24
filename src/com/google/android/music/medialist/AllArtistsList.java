package com.google.android.music.medialist;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.music.store.MusicContent.Artists;

public class AllArtistsList extends ArtistList
{
  public static final Parcelable.Creator<AllArtistsList> CREATOR = new Parcelable.Creator()
  {
    public AllArtistsList createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AllArtistsList();
    }

    public AllArtistsList[] newArray(int paramAnonymousInt)
    {
      return new AllArtistsList[paramAnonymousInt];
    }
  };

  public AllArtistsList()
  {
    super(true);
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.Artists.getAllArtistsUri();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.AllArtistsList
 * JD-Core Version:    0.6.2
 */