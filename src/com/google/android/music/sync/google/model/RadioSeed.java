package com.google.android.music.sync.google.model;

import android.util.Pair;
import android.util.SparseArray;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class RadioSeed extends GenericJson
{

  @Key("albumId")
  public String mAlbumId;

  @Key("artistId")
  public String mArtistId;

  @Key("genreId")
  public String mGenreId;

  @Key("seedType")
  public int mSeedType;

  @Key("trackId")
  public String mTrackId;

  @Key("trackLockerId")
  public String mTrackLockerId;

  public static RadioSeed createRadioSeed(String paramString, int paramInt)
  {
    RadioSeed localRadioSeed = new RadioSeed();
    RemoteSeedType localRemoteSeedType = RemoteSeedType.fromSchemaValue(paramInt);
    int i = localRemoteSeedType.getRemoteValue();
    localRadioSeed.mSeedType = i;
    int[] arrayOfInt = 1.$SwitchMap$com$google$android$music$sync$google$model$RadioSeed$RemoteSeedType;
    int j = localRemoteSeedType.ordinal();
    switch (arrayOfInt[j])
    {
    default:
      String str = "Unsupported schemaSeedType: " + paramInt;
      throw new IllegalArgumentException(str);
    case 1:
      localRadioSeed.mTrackLockerId = paramString;
    case 6:
    case 2:
    case 4:
    case 3:
    case 5:
    }
    while (true)
    {
      return localRadioSeed;
      localRadioSeed.mTrackId = paramString;
      continue;
      localRadioSeed.mAlbumId = paramString;
      continue;
      localRadioSeed.mArtistId = paramString;
      continue;
      localRadioSeed.mGenreId = paramString;
    }
  }

  public Pair<String, Integer> getSourceIdAndType()
  {
    Pair localPair = null;
    RemoteSeedType localRemoteSeedType = RemoteSeedType.fromRemoteValue(this.mSeedType);
    int i = localRemoteSeedType.getSchemaValue();
    int[] arrayOfInt = 1.$SwitchMap$com$google$android$music$sync$google$model$RadioSeed$RemoteSeedType;
    int j = localRemoteSeedType.ordinal();
    switch (arrayOfInt[j])
    {
    default:
      if (this.mTrackLockerId != null)
      {
        String str1 = this.mTrackLockerId;
        Integer localInteger1 = Integer.valueOf(RemoteSeedType.LOCKER_TRACK.getSchemaValue());
        localPair = new Pair(str1, localInteger1);
      }
      break;
    case 1:
    case 2:
    case 4:
    case 3:
    case 5:
    case 6:
    }
    while (localPair == null)
    {
      throw new IllegalArgumentException("Seed is empty");
      String str2 = this.mTrackLockerId;
      Integer localInteger2 = Integer.valueOf(i);
      localPair = new Pair(str2, localInteger2);
      continue;
      String str3 = this.mTrackId;
      Integer localInteger3 = Integer.valueOf(i);
      localPair = new Pair(str3, localInteger3);
      continue;
      String str4 = this.mAlbumId;
      Integer localInteger4 = Integer.valueOf(i);
      localPair = new Pair(str4, localInteger4);
      continue;
      String str5 = this.mArtistId;
      Integer localInteger5 = Integer.valueOf(i);
      localPair = new Pair(str5, localInteger5);
      continue;
      String str6 = this.mGenreId;
      Integer localInteger6 = Integer.valueOf(i);
      localPair = new Pair(str6, localInteger6);
      continue;
      Integer localInteger7 = Integer.valueOf(i);
      localPair = new Pair(null, localInteger7);
      continue;
      if (this.mTrackId != null)
      {
        String str7 = this.mTrackId;
        Integer localInteger8 = Integer.valueOf(RemoteSeedType.METAJAM_TRACK.getSchemaValue());
        localPair = new Pair(str7, localInteger8);
      }
      else if (this.mAlbumId != null)
      {
        String str8 = this.mAlbumId;
        Integer localInteger9 = Integer.valueOf(RemoteSeedType.ALBUM.getSchemaValue());
        localPair = new Pair(str8, localInteger9);
      }
      else if (this.mArtistId != null)
      {
        String str9 = this.mArtistId;
        Integer localInteger10 = Integer.valueOf(RemoteSeedType.ARTIST.getSchemaValue());
        localPair = new Pair(str9, localInteger10);
      }
      else if (this.mGenreId != null)
      {
        String str10 = this.mGenreId;
        Integer localInteger11 = Integer.valueOf(RemoteSeedType.GENRE.getSchemaValue());
        localPair = new Pair(str10, localInteger11);
      }
    }
    return localPair;
  }

  boolean isValidForUpstreamInsert()
  {
    boolean bool = false;
    int i;
    if ((this.mTrackLockerId == null) && (this.mTrackId == null))
    {
      if (this.mAlbumId == null)
        break label51;
      i = 1;
      if (this.mArtistId == null)
        break label56;
    }
    label51: label56: for (int j = 1; ; j = 0)
    {
      if (((i | j) != 0) || (this.mGenreId != null))
        bool = true;
      return bool;
      i = 0;
      break;
    }
  }

  public static enum RemoteSeedType
  {
    private static final SparseArray<RemoteSeedType> sRemoteValues;
    private static final SparseArray<RemoteSeedType> sSchemaValues;
    private final int mValue;

    static
    {
      LOCKER_TRACK = new RemoteSeedType("LOCKER_TRACK", 1, 1);
      METAJAM_TRACK = new RemoteSeedType("METAJAM_TRACK", 2, 2);
      ARTIST = new RemoteSeedType("ARTIST", 3, 3);
      ALBUM = new RemoteSeedType("ALBUM", 4, 4);
      GENRE = new RemoteSeedType("GENRE", 5, 5);
      LUCKY = new RemoteSeedType("LUCKY", 6, 6);
      RemoteSeedType[] arrayOfRemoteSeedType1 = new RemoteSeedType[7];
      RemoteSeedType localRemoteSeedType1 = UNKNOWN;
      arrayOfRemoteSeedType1[0] = localRemoteSeedType1;
      RemoteSeedType localRemoteSeedType2 = LOCKER_TRACK;
      arrayOfRemoteSeedType1[1] = localRemoteSeedType2;
      RemoteSeedType localRemoteSeedType3 = METAJAM_TRACK;
      arrayOfRemoteSeedType1[2] = localRemoteSeedType3;
      RemoteSeedType localRemoteSeedType4 = ARTIST;
      arrayOfRemoteSeedType1[3] = localRemoteSeedType4;
      RemoteSeedType localRemoteSeedType5 = ALBUM;
      arrayOfRemoteSeedType1[4] = localRemoteSeedType5;
      RemoteSeedType localRemoteSeedType6 = GENRE;
      arrayOfRemoteSeedType1[5] = localRemoteSeedType6;
      RemoteSeedType localRemoteSeedType7 = LUCKY;
      arrayOfRemoteSeedType1[6] = localRemoteSeedType7;
      $VALUES = arrayOfRemoteSeedType1;
      sRemoteValues = new SparseArray();
      sSchemaValues = new SparseArray();
      RemoteSeedType[] arrayOfRemoteSeedType2 = values();
      int i = arrayOfRemoteSeedType2.length;
      int j = 0;
      while (true)
      {
        if (j >= i)
          return;
        RemoteSeedType localRemoteSeedType8 = arrayOfRemoteSeedType2[j];
        SparseArray localSparseArray1 = sRemoteValues;
        int k = localRemoteSeedType8.getRemoteValue();
        localSparseArray1.put(k, localRemoteSeedType8);
        SparseArray localSparseArray2 = sSchemaValues;
        int m = localRemoteSeedType8.getSchemaValue();
        localSparseArray2.put(m, localRemoteSeedType8);
        j += 1;
      }
    }

    private RemoteSeedType(int paramInt)
    {
      this.mValue = paramInt;
    }

    public static RemoteSeedType fromRemoteValue(int paramInt)
    {
      RemoteSeedType localRemoteSeedType = (RemoteSeedType)sRemoteValues.get(paramInt);
      if (localRemoteSeedType != null);
      while (true)
      {
        return localRemoteSeedType;
        localRemoteSeedType = UNKNOWN;
      }
    }

    public static RemoteSeedType fromSchemaValue(int paramInt)
    {
      RemoteSeedType localRemoteSeedType = (RemoteSeedType)sSchemaValues.get(paramInt);
      if (localRemoteSeedType != null);
      while (true)
      {
        return localRemoteSeedType;
        localRemoteSeedType = UNKNOWN;
      }
    }

    public int getRemoteValue()
    {
      return this.mValue;
    }

    public int getSchemaValue()
    {
      int[] arrayOfInt = RadioSeed.1.$SwitchMap$com$google$android$music$sync$google$model$RadioSeed$RemoteSeedType;
      int i = ordinal();
      int j;
      switch (arrayOfInt[i])
      {
      default:
        j = 0;
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      }
      while (true)
      {
        return j;
        j = 1;
        continue;
        j = 2;
        continue;
        j = 4;
        continue;
        j = 3;
        continue;
        j = 5;
        continue;
        j = 6;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.RadioSeed
 * JD-Core Version:    0.6.2
 */