package com.google.android.music.sync.google.model;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import com.google.android.common.base.Strings;
import com.google.android.music.cloudclient.ImageRefJson;
import com.google.android.music.cloudclient.JsonUtils;
import com.google.android.music.store.InvalidDataException;
import com.google.android.music.store.RadioStation;
import com.google.android.music.sync.api.MusicUrl;
import com.google.android.music.utils.MusicUtils;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SyncableRadioStation extends GenericJson
  implements MusicQueueableSyncEntity
{
  public static final int ARTWORK_TYPE_VALUE_RADIO = 2;
  public static final int ARTWORK_TYPE_VALUE_SEED = 1;
  public static final int ARTWORK_TYPE_VALUE_UNKNOWN;

  @Key("clientId")
  public String mClientId;

  @Key("description")
  public String mDescription;

  @Key("imageType")
  public int mImageType;

  @Key("imageUrl")
  public String mImageUrl;

  @Key("imageUrls")
  public List<ImageRefJson> mImageUrls;

  @Key("deleted")
  public boolean mIsDeleted;

  @Key("lastModifiedTimestamp")
  public long mLastModifiedTimestamp = 65535L;
  private long mLocalId;

  @Key("name")
  public String mName;
  private RadioStation mRadioStation;

  @Key("recentTimestamp")
  public long mRecentTimestampMicrosec = 65535L;

  @Key("id")
  public String mRemoteId;

  @Key("seed")
  public RadioSeed mSeed;

  @Key("tracks")
  public List<Track> mTracks;

  public SyncableRadioStation()
  {
    ArrayList localArrayList = new ArrayList();
    this.mTracks = localArrayList;
    this.mIsDeleted = false;
    this.mLocalId = 65535L;
  }

  private static int convertClientArtworkToServerArtworkType(int paramInt)
  {
    int i;
    switch (paramInt)
    {
    default:
      String str = "Not a syncable artworkType: " + paramInt;
      throw new IllegalArgumentException(str);
    case 1:
      i = 1;
    case 2:
    case 0:
    }
    while (true)
    {
      return i;
      i = 2;
      continue;
      i = 0;
    }
  }

  public static int convertServerArtworkToClientArtworkType(int paramInt)
  {
    int i;
    switch (paramInt)
    {
    default:
      String str = "Invalid server artwork type: " + paramInt;
      throw new IllegalArgumentException(str);
    case 1:
      i = 1;
    case 2:
    case 0:
    }
    while (true)
    {
      return i;
      i = 2;
      continue;
      i = 0;
    }
  }

  public static SyncableRadioStation parse(RadioStation paramRadioStation)
  {
    SyncableRadioStation localSyncableRadioStation = new SyncableRadioStation();
    localSyncableRadioStation.mRadioStation = paramRadioStation;
    String str1 = paramRadioStation.getSourceId();
    localSyncableRadioStation.mRemoteId = str1;
    String str2 = paramRadioStation.getClientId();
    localSyncableRadioStation.mClientId = str2;
    try
    {
      long l1 = Long.valueOf(paramRadioStation.getSourceVersion()).longValue();
      localSyncableRadioStation.mLastModifiedTimestamp = l1;
      long l2 = paramRadioStation.getRecentTimestampMicrosec();
      localSyncableRadioStation.mRecentTimestampMicrosec = l2;
      String str3 = paramRadioStation.getName();
      localSyncableRadioStation.mName = str3;
      String str4 = paramRadioStation.getDescription();
      localSyncableRadioStation.mDescription = str4;
      String str5 = paramRadioStation.getSeedSourceId();
      int i = paramRadioStation.getSeedSourceType();
      RadioSeed localRadioSeed = RadioSeed.createRadioSeed(str5, i);
      localSyncableRadioStation.mSeed = localRadioSeed;
      arrayOfString = MusicUtils.decodeStringArray(paramRadioStation.getArtworkLocation());
      if (arrayOfString == null)
      {
        localSyncableRadioStation.mImageUrl = null;
        localSyncableRadioStation.mImageUrls = null;
        int j = convertClientArtworkToServerArtworkType(paramRadioStation.getArtworkType());
        localSyncableRadioStation.mImageType = j;
        long l3 = paramRadioStation.getId();
        localSyncableRadioStation.mLocalId = l3;
        return localSyncableRadioStation;
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (true)
      {
        String[] arrayOfString;
        localSyncableRadioStation.mLastModifiedTimestamp = 0L;
        continue;
        localSyncableRadioStation.setImageUrls(arrayOfString);
      }
    }
  }

  public static SyncableRadioStation parseForTombstone(RadioStation paramRadioStation)
  {
    SyncableRadioStation localSyncableRadioStation = new SyncableRadioStation();
    localSyncableRadioStation.mRadioStation = paramRadioStation;
    String str = paramRadioStation.getSourceId();
    localSyncableRadioStation.mRemoteId = str;
    try
    {
      long l1 = Long.valueOf(paramRadioStation.getSourceVersion()).longValue();
      localSyncableRadioStation.mLastModifiedTimestamp = l1;
      localSyncableRadioStation.mIsDeleted = true;
      long l2 = paramRadioStation.getId();
      localSyncableRadioStation.mLocalId = l2;
      return localSyncableRadioStation;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (true)
        localSyncableRadioStation.mLastModifiedTimestamp = 0L;
    }
  }

  public RadioStation formatAsRadioStation(RadioStation paramRadioStation)
  {
    if (paramRadioStation == null)
      paramRadioStation = new RadioStation();
    String str1 = this.mRemoteId;
    paramRadioStation.setSourceId(str1);
    String str2 = this.mClientId;
    paramRadioStation.setClientId(str2);
    String str3 = Long.toString(this.mLastModifiedTimestamp);
    paramRadioStation.setSourceVersion(str3);
    long l = this.mRecentTimestampMicrosec;
    paramRadioStation.setRecentTimestampMicrosec(l);
    String str4 = this.mName;
    paramRadioStation.setName(str4);
    String str5 = this.mDescription;
    paramRadioStation.setDescription(str5);
    Pair localPair = this.mSeed.getSourceIdAndType();
    String str6 = (String)localPair.first;
    paramRadioStation.setSeedSourceId(str6);
    int i = ((Integer)localPair.second).intValue();
    paramRadioStation.setSeedSourceType(i);
    if ((this.mImageUrls != null) && (!this.mImageUrls.isEmpty()))
    {
      String[] arrayOfString = new String[this.mImageUrls.size()];
      int j = 0;
      while (true)
      {
        int k = arrayOfString.length;
        if (j >= k)
          break;
        String str7 = ((ImageRefJson)this.mImageUrls.get(j)).mUrl;
        arrayOfString[j] = str7;
        j += 1;
      }
      String str8 = MusicUtils.encodeStringArray(arrayOfString);
      paramRadioStation.setArtworkLocation(str8);
    }
    while (true)
    {
      int m = convertServerArtworkToClientArtworkType(this.mImageType);
      paramRadioStation.setArtworkType(m);
      return paramRadioStation;
      String str9 = this.mImageUrl;
      paramRadioStation.setArtworkLocation(str9);
    }
  }

  public MusicUrl getBatchMutationUrl(Context paramContext)
  {
    return MusicUrl.forRadioEditStations();
  }

  public long getCreationTimestamp()
  {
    throw new UnsupportedOperationException("Creation timestamp not suppoted");
  }

  public RadioStation getEncapsulatedRadioStation()
  {
    return this.mRadioStation;
  }

  public MusicUrl getFeedUrl(Context paramContext)
  {
    throw new UnsupportedOperationException("No implemented");
  }

  public MusicUrl getFeedUrlAsPost(Context paramContext)
  {
    return MusicUrl.forRadioGetStations();
  }

  public long getLastModifiedTimestamp()
  {
    return this.mLastModifiedTimestamp;
  }

  public long getLocalId()
  {
    return this.mLocalId;
  }

  public String getRemoteId()
  {
    return this.mRemoteId;
  }

  public MusicUrl getUrl(Context paramContext, String paramString)
  {
    throw new UnsupportedOperationException("No implemented");
  }

  public boolean isDeleted()
  {
    return this.mIsDeleted;
  }

  public boolean isInsert()
  {
    if ((!this.mIsDeleted) && (this.mRemoteId == null));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isUpdate()
  {
    if ((!this.mIsDeleted) && (this.mRemoteId != null));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public byte[] serializeAsJson()
    throws InvalidDataException
  {
    try
    {
      byte[] arrayOfByte = JsonUtils.toJsonByteArray(this);
      return arrayOfByte;
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Unable to serialize a radio station entry as JSON: ");
      String str1 = toString();
      String str2 = str1 + ": ";
      int i = Log.e("MusicSyncAdapter", str2, localIOException);
      throw new InvalidDataException("Unable to serialize radio station for upstream sync.", localIOException);
    }
  }

  public void setCreationTimestamp(long paramLong)
  {
    throw new UnsupportedOperationException("Creation timestamp not suppoted");
  }

  public void setImageUrls(String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    ArrayList localArrayList = new ArrayList(i);
    String[] arrayOfString = paramArrayOfString;
    int j = arrayOfString.length;
    int k = 0;
    while (k < j)
    {
      String str = arrayOfString[k];
      ImageRefJson localImageRefJson = new ImageRefJson();
      localImageRefJson.mUrl = str;
      boolean bool = localArrayList.add(localImageRefJson);
      k += 1;
    }
    this.mImageUrls = localArrayList;
  }

  public void setIsDeleted(boolean paramBoolean)
  {
    this.mIsDeleted = paramBoolean;
  }

  public void setLastModifiedTimestamp(long paramLong)
  {
    this.mLastModifiedTimestamp = paramLong;
  }

  public void setRemoteId(String paramString)
  {
    this.mRemoteId = paramString;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("; localid:");
    long l1 = this.mLocalId;
    StringBuilder localStringBuilder3 = localStringBuilder2.append(l1).append("; remoteId:");
    String str1 = this.mRemoteId;
    StringBuilder localStringBuilder4 = localStringBuilder3.append(str1).append("; clientId:");
    String str2 = this.mClientId;
    StringBuilder localStringBuilder5 = localStringBuilder4.append(str2).append("; mtime:");
    long l2 = this.mLastModifiedTimestamp;
    StringBuilder localStringBuilder6 = localStringBuilder5.append(l2).append("; rtime:");
    long l3 = this.mRecentTimestampMicrosec;
    StringBuilder localStringBuilder7 = localStringBuilder6.append(l3).append("; name:");
    String str3 = this.mName;
    StringBuilder localStringBuilder8 = localStringBuilder7.append(str3).append("; description:");
    String str4 = this.mDescription;
    StringBuilder localStringBuilder9 = localStringBuilder8.append(str4).append("; seed: ");
    RadioSeed localRadioSeed = this.mSeed;
    StringBuilder localStringBuilder10 = localStringBuilder9.append(localRadioSeed).append("; imageUrl: ");
    String str5 = this.mImageUrl;
    StringBuilder localStringBuilder11 = localStringBuilder10.append(str5).append("; imageType: ");
    int i = this.mImageType;
    StringBuilder localStringBuilder12 = localStringBuilder11.append(i).append("; isDeleted: ");
    boolean bool = this.mIsDeleted;
    StringBuilder localStringBuilder13 = localStringBuilder12.append(bool);
    return localStringBuilder1.toString();
  }

  public void validateForUpstreamDelete()
    throws InvalidDataException
  {
    if (!Strings.isNullOrEmpty(this.mRemoteId))
      return;
    throw new InvalidDataException("Invalid radio station for upstream delete.");
  }

  public void validateForUpstreamInsert()
    throws InvalidDataException
  {
    if ((!Strings.isNullOrEmpty(this.mName)) && (this.mSeed.isValidForUpstreamInsert()))
      return;
    throw new InvalidDataException("Invalid radio station for upstream insert.");
  }

  public void validateForUpstreamUpdate()
    throws InvalidDataException
  {
    if ((!Strings.isNullOrEmpty(this.mName)) && (!Strings.isNullOrEmpty(this.mRemoteId)))
      return;
    throw new InvalidDataException("Invalid radio station for upstream update.");
  }

  public void wipeAllFields()
  {
    this.mRemoteId = null;
    this.mClientId = null;
    this.mLastModifiedTimestamp = 65535L;
    this.mRecentTimestampMicrosec = 65535L;
    this.mName = null;
    this.mDescription = null;
    this.mSeed = null;
    this.mTracks = null;
    this.mImageUrl = null;
    this.mImageUrls = null;
    this.mImageType = -1;
    this.mIsDeleted = false;
    this.mLocalId = 65535L;
    this.mRadioStation = null;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.SyncableRadioStation
 * JD-Core Version:    0.6.2
 */