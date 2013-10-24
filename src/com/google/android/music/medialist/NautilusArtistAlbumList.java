package com.google.android.music.medialist;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.music.store.MusicContent.Artists;

public class NautilusArtistAlbumList extends NautilusAlbumList
{
  public static final Parcelable.Creator<NautilusArtistAlbumList> CREATOR = new Parcelable.Creator()
  {
    public NautilusArtistAlbumList createFromParcel(Parcel paramAnonymousParcel)
    {
      String str1 = paramAnonymousParcel.readString();
      String str2 = paramAnonymousParcel.readString();
      return new NautilusArtistAlbumList(str1, str2);
    }

    public NautilusArtistAlbumList[] newArray(int paramAnonymousInt)
    {
      return new NautilusArtistAlbumList[paramAnonymousInt];
    }
  };
  private final String mArtistName;
  private final String mNautilusArtistId;

  public NautilusArtistAlbumList(String paramString1, String paramString2)
  {
    if (TextUtils.isEmpty(paramString1))
    {
      String str = "Invalid artist id: " + paramString1;
      throw new IllegalArgumentException(str);
    }
    this.mNautilusArtistId = paramString1;
    this.mArtistName = paramString2;
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
    return MusicContent.Artists.getAlbumsByNautilusArtistsUri(this.mNautilusArtistId);
  }

  public String getName(Context paramContext)
  {
    return this.mArtistName;
  }

  public String getNautilusId()
  {
    return this.mNautilusArtistId;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    String str1 = this.mNautilusArtistId;
    paramParcel.writeString(str1);
    String str2 = this.mArtistName;
    paramParcel.writeString(str2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.NautilusArtistAlbumList
 * JD-Core Version:    0.6.2
 */