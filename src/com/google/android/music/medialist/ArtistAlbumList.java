package com.google.android.music.medialist;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.music.download.artwork.AlbumIdSink;
import com.google.android.music.store.MusicContent.Artists;

public class ArtistAlbumList extends AlbumList
{
  public static final Parcelable.Creator<ArtistAlbumList> CREATOR = new Parcelable.Creator()
  {
    public ArtistAlbumList createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = 1;
      long l = paramAnonymousParcel.readLong();
      String str = paramAnonymousParcel.readString();
      if (paramAnonymousParcel.readInt() != i);
      while (true)
      {
        return new ArtistAlbumList(l, str, i);
        int j = 0;
      }
    }

    public ArtistAlbumList[] newArray(int paramAnonymousInt)
    {
      return new ArtistAlbumList[paramAnonymousInt];
    }
  };
  private final long mArtistId;
  private final String mArtistName;

  public ArtistAlbumList(long paramLong, String paramString, boolean paramBoolean)
  {
    super(paramBoolean);
    if (paramLong < 0L)
    {
      String str = "Invalid artist id: " + paramLong;
      throw new IllegalArgumentException(str);
    }
    this.mArtistId = paramLong;
    this.mArtistName = paramString;
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[3];
    StringBuilder localStringBuilder = new StringBuilder().append("");
    long l = this.mArtistId;
    String str1 = l;
    arrayOfString[0] = str1;
    String str2 = this.mArtistName;
    arrayOfString[1] = str2;
    String str3 = Boolean.toString(this.mShouldFilter);
    arrayOfString[2] = str3;
    return arrayOfString;
  }

  public long getArtistId(Context paramContext)
  {
    return this.mArtistId;
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.Artists.getAlbumsByArtistsUri(this.mArtistId);
  }

  public Bitmap getImage(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, boolean paramBoolean)
  {
    return null;
  }

  public String getName(Context paramContext)
  {
    return this.mArtistName;
  }

  public String getSecondaryName(Context paramContext)
  {
    return null;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    long l = this.mArtistId;
    paramParcel.writeLong(l);
    String str = this.mArtistName;
    paramParcel.writeString(str);
    if (this.mShouldFilter);
    for (int i = 1; ; i = 0)
    {
      paramParcel.writeInt(i);
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.ArtistAlbumList
 * JD-Core Version:    0.6.2
 */