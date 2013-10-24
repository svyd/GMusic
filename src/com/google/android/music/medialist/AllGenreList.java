package com.google.android.music.medialist;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.music.store.MusicContent.Genres;

public class AllGenreList extends GenreList
{
  public static final Parcelable.Creator<AllGenreList> CREATOR = new Parcelable.Creator()
  {
    public AllGenreList createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AllGenreList();
    }

    public AllGenreList[] newArray(int paramAnonymousInt)
    {
      return new AllGenreList[paramAnonymousInt];
    }
  };

  public AllGenreList()
  {
    super(true);
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.Genres.CONTENT_URI;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.AllGenreList
 * JD-Core Version:    0.6.2
 */