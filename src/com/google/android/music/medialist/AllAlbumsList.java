package com.google.android.music.medialist;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.music.store.MusicContent.Albums;

public class AllAlbumsList extends AlbumList
{
  public static final Parcelable.Creator<AllAlbumsList> CREATOR = new Parcelable.Creator()
  {
    public AllAlbumsList createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AllAlbumsList();
    }

    public AllAlbumsList[] newArray(int paramAnonymousInt)
    {
      return new AllAlbumsList[paramAnonymousInt];
    }
  };

  public AllAlbumsList()
  {
    super(true);
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.Albums.getAllAlbumsUri();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.AllAlbumsList
 * JD-Core Version:    0.6.2
 */