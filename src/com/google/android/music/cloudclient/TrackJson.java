package com.google.android.music.cloudclient;

import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;

public class TrackJson extends GenericJson
{
  private static final Map<String, Integer> RATINGS_MAP = Maps.newHashMapWithExpectedSize(12);
  public static final int TRACK_ORIGIN_NOT_USER_SELECTED = 4;
  public static final int TRACK_TYPE_NAUTILUS_EPHEMERAL = 7;
  public static final int TRACK_TYPE_NAUTILUS_PERSISTENT = 8;
  public static final int TRACK_TYPE_PROMO = 6;
  public static final int TRACK_TYPE_PURCHASED = 4;

  @Key("album")
  public String mAlbum;

  @Key("albumArtRef")
  public List<ImageRef> mAlbumArtRef;

  @Key("albumArtist")
  public String mAlbumArtist;

  @Key("albumId")
  public String mAlbumId;

  @Key("artist")
  public String mArtist;

  @Key("artistArtRef")
  public List<ImageRef> mArtistArtRef;

  @Key("artistId")
  public List<String> mArtistId;

  @Key("beatsPerMinute")
  public int mBeatsPerMinute = -1;

  @Key("clientId")
  public String mClientId;

  @Key("comment")
  public String mComment;

  @Key("composer")
  public String mComposer;

  @Key("creationTimestamp")
  public long mCreationTimestamp = 65535L;

  @Key("discNumber")
  public int mDiscNumber = -1;

  @Key("durationMillis")
  public long mDurationMillis = 65535L;

  @Key("estimatedSize")
  public long mEstimatedSize = 65535L;

  @Key("genre")
  public String mGenre;

  @Key("deleted")
  public boolean mIsDeleted;

  @Key("lastModifiedTimestamp")
  public long mLastModifiedTimestamp = 65535L;

  @Key("nid")
  @Deprecated
  public String mNautilusId;

  @Key("playCount")
  public int mPlayCount;

  @Key("rating")
  public String mRating;

  @Key("id")
  public String mRemoteId;

  @Key("storeId")
  public String mStoreId;

  @Key("title")
  public String mTitle;

  @Key("totalDiscCount")
  public int mTotalDiscCount = -1;

  @Key("trackNumber")
  public int mTrackNumber = -1;

  @Key("trackOrigin")
  public List<TrackOrigin> mTrackOrigin;

  @Key("trackType")
  public int mTrackType = -1;

  @Key("year")
  public int mYear = -1;

  static
  {
    Map localMap1 = RATINGS_MAP;
    Integer localInteger1 = Integer.valueOf(0);
    Object localObject1 = localMap1.put("NOT_RATED", localInteger1);
    Map localMap2 = RATINGS_MAP;
    Integer localInteger2 = Integer.valueOf(0);
    Object localObject2 = localMap2.put("0", localInteger2);
    Map localMap3 = RATINGS_MAP;
    Integer localInteger3 = Integer.valueOf(1);
    Object localObject3 = localMap3.put("ONE_STAR", localInteger3);
    Map localMap4 = RATINGS_MAP;
    Integer localInteger4 = Integer.valueOf(1);
    Object localObject4 = localMap4.put("1", localInteger4);
    Map localMap5 = RATINGS_MAP;
    Integer localInteger5 = Integer.valueOf(2);
    Object localObject5 = localMap5.put("TWO_STARS", localInteger5);
    Map localMap6 = RATINGS_MAP;
    Integer localInteger6 = Integer.valueOf(2);
    Object localObject6 = localMap6.put("2", localInteger6);
    Map localMap7 = RATINGS_MAP;
    Integer localInteger7 = Integer.valueOf(3);
    Object localObject7 = localMap7.put("THREE_STARS", localInteger7);
    Map localMap8 = RATINGS_MAP;
    Integer localInteger8 = Integer.valueOf(3);
    Object localObject8 = localMap8.put("3", localInteger8);
    Map localMap9 = RATINGS_MAP;
    Integer localInteger9 = Integer.valueOf(4);
    Object localObject9 = localMap9.put("FOUR_STARS", localInteger9);
    Map localMap10 = RATINGS_MAP;
    Integer localInteger10 = Integer.valueOf(4);
    Object localObject10 = localMap10.put("4", localInteger10);
    Map localMap11 = RATINGS_MAP;
    Integer localInteger11 = Integer.valueOf(5);
    Object localObject11 = localMap11.put("FIVE_STARS", localInteger11);
    Map localMap12 = RATINGS_MAP;
    Integer localInteger12 = Integer.valueOf(5);
    Object localObject12 = localMap12.put("5", localInteger12);
  }

  public ContentIdentifier.Domain getDomain()
  {
    ContentIdentifier.Domain localDomain;
    if (hasLockerId())
      localDomain = ContentIdentifier.Domain.DEFAULT;
    while (true)
    {
      return localDomain;
      if (hasNautilusId())
        localDomain = ContentIdentifier.Domain.NAUTILUS;
      else
        localDomain = ContentIdentifier.Domain.DEFAULT;
    }
  }

  public String getEffectiveRemoteId()
  {
    String str;
    if (hasLockerId())
      str = this.mRemoteId;
    while (true)
    {
      return str;
      if (hasNautilusId())
        str = getNormalizedNautilusId();
      else
        str = null;
    }
  }

  public String getNormalizedNautilusId()
  {
    if ((this.mNautilusId == null) || (this.mNautilusId.length() == 0) || (this.mNautilusId.charAt(0) == 'T'));
    StringBuilder localStringBuilder;
    String str2;
    for (String str1 = this.mNautilusId; ; str1 = str2)
    {
      return str1;
      localStringBuilder = new StringBuilder().append("T");
      str2 = this.mNautilusId;
    }
  }

  public int getRatingAsInt()
  {
    Map localMap1 = RATINGS_MAP;
    String str1 = this.mRating;
    Map localMap2;
    String str2;
    if (localMap1.containsKey(str1))
    {
      localMap2 = RATINGS_MAP;
      str2 = this.mRating;
    }
    for (int i = ((Integer)localMap2.get(str2)).intValue(); ; i = -1)
      return i;
  }

  public int getTrackType()
  {
    return this.mTrackType;
  }

  public boolean hasLockerId()
  {
    if ((this.mRemoteId != null) && (this.mRemoteId.length() > 0));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean hasNautilusId()
  {
    if ((this.mNautilusId != null) && (this.mNautilusId.length() > 0));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void setRating(int paramInt)
  {
    String str = Integer.toString(paramInt);
    this.mRating = str;
  }

  public String toString()
  {
    String str1 = null;
    StringBuffer localStringBuffer1 = new StringBuffer();
    StringBuffer localStringBuffer2 = localStringBuffer1.append("; remoteid:");
    String str2 = this.mRemoteId;
    StringBuffer localStringBuffer3 = localStringBuffer2.append(str2).append("; ctime:");
    long l1 = this.mCreationTimestamp;
    StringBuffer localStringBuffer4 = localStringBuffer3.append(l1).append("; mtime:");
    long l2 = this.mLastModifiedTimestamp;
    StringBuffer localStringBuffer5 = localStringBuffer4.append(l2).append("; isDeleted: ");
    boolean bool = this.mIsDeleted;
    StringBuffer localStringBuffer6 = localStringBuffer5.append(bool).append("; title: ");
    String str3 = this.mTitle;
    StringBuffer localStringBuffer7 = localStringBuffer6.append(str3).append("; artist: ");
    String str4 = this.mArtist;
    StringBuffer localStringBuffer8 = localStringBuffer7.append(str4).append("; composer: ");
    String str5 = this.mComposer;
    StringBuffer localStringBuffer9 = localStringBuffer8.append(str5).append("; album: ");
    String str6 = this.mAlbum;
    StringBuffer localStringBuffer10 = localStringBuffer9.append(str6).append("; albumartist: ");
    String str7 = this.mAlbumArtist;
    StringBuffer localStringBuffer11 = localStringBuffer10.append(str7).append("; year: ");
    int i = this.mYear;
    StringBuffer localStringBuffer12 = localStringBuffer11.append(i).append("; comment: ");
    String str8 = this.mComment;
    StringBuffer localStringBuffer13 = localStringBuffer12.append(str8).append("; track num: ");
    int j = this.mTrackNumber;
    StringBuffer localStringBuffer14 = localStringBuffer13.append(j).append("; genre: ");
    String str9 = this.mGenre;
    StringBuffer localStringBuffer15 = localStringBuffer14.append(str9).append("; duration: ");
    long l3 = this.mDurationMillis;
    StringBuffer localStringBuffer16 = localStringBuffer15.append(l3).append("; albumartref: ");
    String str10;
    String str11;
    label342: String str15;
    label556: StringBuffer localStringBuffer29;
    if (this.mAlbumArtRef == null)
    {
      str10 = null;
      StringBuffer localStringBuffer17 = localStringBuffer16.append(str10).append("; artistartref: ");
      if (this.mArtistArtRef != null)
        break label624;
      str11 = null;
      StringBuffer localStringBuffer18 = localStringBuffer17.append(str11).append("; bpm: ");
      int k = this.mBeatsPerMinute;
      StringBuffer localStringBuffer19 = localStringBuffer18.append(k).append("; playCount: ");
      int m = this.mPlayCount;
      StringBuffer localStringBuffer20 = localStringBuffer19.append(m).append("; estimatedSize: ");
      long l4 = this.mEstimatedSize;
      StringBuffer localStringBuffer21 = localStringBuffer20.append(l4).append("; discNumber:");
      int n = this.mDiscNumber;
      StringBuffer localStringBuffer22 = localStringBuffer21.append(n).append("; totalDiscCount:");
      int i1 = this.mTotalDiscCount;
      StringBuffer localStringBuffer23 = localStringBuffer22.append(i1).append("; trackType:");
      int i2 = this.mTrackType;
      StringBuffer localStringBuffer24 = localStringBuffer23.append(i2).append("; rating:");
      String str12 = this.mRating;
      StringBuffer localStringBuffer25 = localStringBuffer24.append(str12).append("; storeId:");
      String str13 = this.mStoreId;
      StringBuffer localStringBuffer26 = localStringBuffer25.append(str13).append("; albumId:");
      String str14 = this.mAlbumId;
      StringBuffer localStringBuffer27 = localStringBuffer26.append(str14).append("; trackOrigin:");
      if (this.mTrackOrigin != null)
        break label636;
      str15 = null;
      StringBuffer localStringBuffer28 = localStringBuffer27.append(str15).append("; clientId:");
      String str16 = this.mClientId;
      localStringBuffer29 = localStringBuffer28.append(str16).append("; artistId: ");
      if (this.mArtistId != null)
        break label648;
    }
    while (true)
    {
      StringBuffer localStringBuffer30 = localStringBuffer29.append(str1);
      return localStringBuffer1.toString();
      str10 = this.mAlbumArtRef.toString();
      break;
      label624: str11 = this.mArtistArtRef.toString();
      break label342;
      label636: str15 = this.mTrackOrigin.toString();
      break label556;
      label648: str1 = this.mArtistId.toString();
    }
  }

  public void wipeAllFields()
  {
    this.mRemoteId = null;
    this.mCreationTimestamp = 0L;
    this.mLastModifiedTimestamp = 0L;
    this.mIsDeleted = false;
    this.mTitle = null;
    this.mArtist = null;
    this.mComposer = null;
    this.mAlbum = null;
    this.mAlbumArtist = null;
    this.mYear = -1;
    this.mComment = null;
    this.mTrackNumber = -1;
    this.mDurationMillis = 65535L;
    this.mAlbumArtRef = null;
    this.mArtistArtRef = null;
    this.mBeatsPerMinute = -1;
    this.mPlayCount = -1;
    this.mEstimatedSize = 65535L;
    this.mTotalDiscCount = -1;
    this.mDiscNumber = -1;
    this.mTrackType = -1;
    this.mRating = null;
    this.mStoreId = null;
    this.mAlbumId = null;
    this.mTrackOrigin = null;
    this.mClientId = null;
    this.mArtistId = null;
  }

  public static class TrackOrigin extends GenericJson
  {

    @Key("origin")
    public int mValue;
  }

  public static class ImageRef extends GenericJson
  {

    @Key("height")
    public int mHeight;

    @Key("url")
    public String mUrl;

    @Key("width")
    public int mWidth;

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("(width: ");
      int i = this.mWidth;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(i).append(";height: ");
      int j = this.mHeight;
      StringBuilder localStringBuilder3 = localStringBuilder2.append(j).append(";url:");
      String str = this.mUrl;
      return str + ")";
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.TrackJson
 * JD-Core Version:    0.6.2
 */