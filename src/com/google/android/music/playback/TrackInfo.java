package com.google.android.music.playback;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.music.download.ContentIdentifier;

public class TrackInfo
  implements Parcelable
{
  public static final Parcelable.Creator<TrackInfo> CREATOR = new Parcelable.Creator()
  {
    public TrackInfo createFromParcel(Parcel paramAnonymousParcel)
    {
      return new TrackInfo(paramAnonymousParcel, null);
    }

    public TrackInfo[] newArray(int paramAnonymousInt)
    {
      return new TrackInfo[paramAnonymousInt];
    }
  };
  private long mAlbumId;
  private String mAlbumName;
  private String mArtistName;
  private long mDuration;
  private String mExternalAlbumArtUrl;
  private boolean mIsAlbumArtInService;
  private int mPreviewPlayType;
  private int mRating;
  private ContentIdentifier mSongId;
  private boolean mSupportRating;
  private String mTrackName;

  private TrackInfo(Parcel paramParcel)
  {
    ClassLoader localClassLoader = ContentIdentifier.class.getClassLoader();
    ContentIdentifier localContentIdentifier = (ContentIdentifier)paramParcel.readParcelable(localClassLoader);
    this.mSongId = localContentIdentifier;
    String str1 = paramParcel.readString();
    this.mTrackName = str1;
    String str2 = paramParcel.readString();
    this.mArtistName = str2;
    String str3 = paramParcel.readString();
    this.mAlbumName = str3;
    long l1 = paramParcel.readLong();
    this.mAlbumId = l1;
    boolean bool;
    if (paramParcel.readInt() != i)
    {
      bool = true;
      this.mIsAlbumArtInService = bool;
      long l2 = paramParcel.readLong();
      this.mDuration = l2;
      String str4 = paramParcel.readString();
      this.mExternalAlbumArtUrl = str4;
      if (paramParcel.readInt() != 1)
        break label161;
    }
    while (true)
    {
      this.mSupportRating = i;
      int j = paramParcel.readInt();
      this.mRating = j;
      int k = paramParcel.readInt();
      this.mPreviewPlayType = k;
      return;
      bool = false;
      break;
      label161: i = 0;
    }
  }

  public TrackInfo(ContentIdentifier paramContentIdentifier, String paramString1, String paramString2, String paramString3, long paramLong1, boolean paramBoolean1, long paramLong2, String paramString4, boolean paramBoolean2, int paramInt1, int paramInt2)
  {
    if (paramContentIdentifier == null)
      throw new IllegalArgumentException("songId is Null, which is an invalid value");
    this.mSongId = paramContentIdentifier;
    this.mTrackName = paramString1;
    this.mArtistName = paramString2;
    this.mAlbumName = paramString3;
    this.mAlbumId = paramLong1;
    this.mIsAlbumArtInService = paramBoolean1;
    this.mDuration = paramLong2;
    this.mExternalAlbumArtUrl = paramString4;
    this.mSupportRating = paramBoolean2;
    this.mRating = paramInt1;
    this.mPreviewPlayType = paramInt2;
  }

  public int describeContents()
  {
    return 0;
  }

  public Boolean getAlbumArtIsFromService()
  {
    return Boolean.valueOf(this.mIsAlbumArtInService);
  }

  public long getAlbumId()
  {
    return this.mAlbumId;
  }

  public String getAlbumName()
  {
    return this.mAlbumName;
  }

  public String getArtistName()
  {
    return this.mArtistName;
  }

  public long getDuration()
  {
    return this.mDuration;
  }

  public int getPreviewPlayType()
  {
    return this.mPreviewPlayType;
  }

  public int getRating()
  {
    return this.mRating;
  }

  public ContentIdentifier getSongId()
  {
    return this.mSongId;
  }

  public Boolean getSupportRating()
  {
    return Boolean.valueOf(this.mSupportRating);
  }

  public String getTrackName()
  {
    return this.mTrackName;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("[");
    StringBuilder localStringBuilder3 = localStringBuilder1.append("mSongId=");
    ContentIdentifier localContentIdentifier = this.mSongId;
    StringBuilder localStringBuilder4 = localStringBuilder3.append(localContentIdentifier);
    StringBuilder localStringBuilder5 = localStringBuilder1.append(" mTrackName=\"");
    String str1 = this.mTrackName;
    StringBuilder localStringBuilder6 = localStringBuilder5.append(str1).append("\"");
    StringBuilder localStringBuilder7 = localStringBuilder1.append(" mArtistName=\"");
    String str2 = this.mArtistName;
    StringBuilder localStringBuilder8 = localStringBuilder7.append(str2).append("\"");
    StringBuilder localStringBuilder9 = localStringBuilder1.append(" mAlbumName=\"");
    String str3 = this.mAlbumName;
    StringBuilder localStringBuilder10 = localStringBuilder9.append(str3).append("\"");
    StringBuilder localStringBuilder11 = localStringBuilder1.append(" mAlbumId=");
    long l1 = this.mAlbumId;
    StringBuilder localStringBuilder12 = localStringBuilder11.append(l1);
    StringBuilder localStringBuilder13 = localStringBuilder1.append(" mIsAlbumArtInService=");
    boolean bool1 = this.mIsAlbumArtInService;
    StringBuilder localStringBuilder14 = localStringBuilder13.append(bool1);
    StringBuilder localStringBuilder15 = localStringBuilder1.append(" mDuration=");
    long l2 = this.mDuration;
    StringBuilder localStringBuilder16 = localStringBuilder15.append(l2);
    StringBuilder localStringBuilder17 = localStringBuilder1.append(" mExternalAlbumArtUrl");
    String str4 = this.mExternalAlbumArtUrl;
    StringBuilder localStringBuilder18 = localStringBuilder17.append(str4);
    StringBuilder localStringBuilder19 = localStringBuilder1.append(" mSupportRating=");
    boolean bool2 = this.mSupportRating;
    StringBuilder localStringBuilder20 = localStringBuilder19.append(bool2);
    StringBuilder localStringBuilder21 = localStringBuilder1.append(" mRating=");
    int i = this.mRating;
    StringBuilder localStringBuilder22 = localStringBuilder21.append(i);
    StringBuilder localStringBuilder23 = localStringBuilder1.append(" mPreviewPlayType=");
    int j = this.mPreviewPlayType;
    StringBuilder localStringBuilder24 = localStringBuilder23.append(j);
    StringBuilder localStringBuilder25 = localStringBuilder1.append("]");
    return localStringBuilder1.toString();
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = 1;
    ContentIdentifier localContentIdentifier = this.mSongId;
    paramParcel.writeParcelable(localContentIdentifier, paramInt);
    String str1 = this.mTrackName;
    paramParcel.writeString(str1);
    String str2 = this.mArtistName;
    paramParcel.writeString(str2);
    String str3 = this.mAlbumName;
    paramParcel.writeString(str3);
    long l1 = this.mAlbumId;
    paramParcel.writeLong(l1);
    int j;
    if (this.mIsAlbumArtInService)
    {
      j = 1;
      paramParcel.writeInt(j);
      long l2 = this.mDuration;
      paramParcel.writeLong(l2);
      String str4 = this.mExternalAlbumArtUrl;
      paramParcel.writeString(str4);
      if (!this.mSupportRating)
        break label146;
    }
    while (true)
    {
      paramParcel.writeInt(i);
      int k = this.mRating;
      paramParcel.writeInt(k);
      int m = this.mPreviewPlayType;
      paramParcel.writeInt(m);
      return;
      j = 0;
      break;
      label146: i = 0;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.playback.TrackInfo
 * JD-Core Version:    0.6.2
 */