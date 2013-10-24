package com.google.android.music.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.music.medialist.GenreList;
import com.google.android.music.store.MusicContent.Explore;

public class GenreExploreList extends GenreList
{
  public static final Parcelable.Creator<GenreExploreList> CREATOR = new Parcelable.Creator()
  {
    public GenreExploreList createFromParcel(Parcel paramAnonymousParcel)
    {
      String str = paramAnonymousParcel.readString();
      return new GenreExploreList(str);
    }

    public GenreExploreList[] newArray(int paramAnonymousInt)
    {
      return new GenreExploreList[paramAnonymousInt];
    }
  };
  private String mGenreNautilusId;

  GenreExploreList(String paramString)
  {
    super(false);
    this.mGenreNautilusId = paramString;
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.Explore.getGenresUri(this.mGenreNautilusId);
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    super.writeToParcel(paramParcel, paramInt);
    String str = this.mGenreNautilusId;
    paramParcel.writeString(str);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.GenreExploreList
 * JD-Core Version:    0.6.2
 */