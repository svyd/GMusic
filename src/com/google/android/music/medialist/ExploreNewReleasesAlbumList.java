package com.google.android.music.medialist;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.music.store.MusicContent.Explore;

public class ExploreNewReleasesAlbumList extends NautilusAlbumList
{
  public static final Parcelable.Creator<ExploreNewReleasesAlbumList> CREATOR = new Parcelable.Creator()
  {
    public ExploreNewReleasesAlbumList createFromParcel(Parcel paramAnonymousParcel)
    {
      String str = paramAnonymousParcel.readString();
      return new ExploreNewReleasesAlbumList(str);
    }

    public ExploreNewReleasesAlbumList[] newArray(int paramAnonymousInt)
    {
      return new ExploreNewReleasesAlbumList[paramAnonymousInt];
    }
  };
  private final String mGenreId;

  public ExploreNewReleasesAlbumList(String paramString)
  {
    this.mGenreId = paramString;
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[1];
    String str = this.mGenreId;
    arrayOfString[0] = str;
    return arrayOfString;
  }

  public Uri getContentUri(Context paramContext)
  {
    String str = this.mGenreId;
    return MusicContent.Explore.getNewReleasesUri(0L, str);
  }

  public String getName(Context paramContext)
  {
    return this.mGenreId;
  }

  public String getNautilusId()
  {
    return this.mGenreId;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    String str = this.mGenreId;
    paramParcel.writeString(str);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.ExploreNewReleasesAlbumList
 * JD-Core Version:    0.6.2
 */