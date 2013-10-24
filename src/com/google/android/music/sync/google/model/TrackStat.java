package com.google.android.music.sync.google.model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.google.android.common.base.Strings;
import com.google.android.music.store.InvalidDataException;
import com.google.android.music.store.MusicFile;
import com.google.android.music.sync.api.MusicUrl;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.util.Key;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;

public class TrackStat extends GenericJson
  implements MusicQueueableSyncEntity
{
  public static final int LOCKER_TYPE = 1;
  public static final int NAUTILUS_TYPE = 2;
  public static final int UNKNOWN_TYPE;

  @Key("incremental_plays")
  public int mIncrementalPlays = 0;

  @Key("last_play_time_millis")
  public long mLastPlayTimeMillis = 0L;
  private long mLocalId = 0L;

  @Key("id")
  public String mRemoteId;

  @Key("type")
  public int mType = 0;

  private static int convertSourceTypeToTrackStatType(int paramInt)
  {
    int i = 2;
    if (paramInt != i)
      i = 1;
    while (paramInt == 3)
      return i;
    String str = "Invalid sourceType: " + paramInt;
    throw new IllegalArgumentException(str);
  }

  public static TrackStat parse(Cursor paramCursor)
  {
    TrackStat localTrackStat = new TrackStat();
    long l1 = paramCursor.getLong(0);
    localTrackStat.mLocalId = l1;
    String str = paramCursor.getString(2);
    localTrackStat.mRemoteId = str;
    int i = paramCursor.getInt(3);
    localTrackStat.mIncrementalPlays = i;
    long l2 = paramCursor.getLong(4);
    localTrackStat.mLastPlayTimeMillis = l2;
    int j = convertSourceTypeToTrackStatType(paramCursor.getInt(5));
    localTrackStat.mType = j;
    return localTrackStat;
  }

  public static TrackStat parse(MusicFile paramMusicFile)
  {
    TrackStat localTrackStat = new TrackStat();
    String str = paramMusicFile.getSourceId();
    localTrackStat.mRemoteId = str;
    int i = paramMusicFile.getPlayCount();
    localTrackStat.mIncrementalPlays = i;
    long l1 = paramMusicFile.getLastPlayDate();
    localTrackStat.mLastPlayTimeMillis = l1;
    long l2 = paramMusicFile.getLocalId();
    localTrackStat.mLocalId = l2;
    int j = convertSourceTypeToTrackStatType(paramMusicFile.getSourceType());
    localTrackStat.mType = j;
    return localTrackStat;
  }

  public MusicUrl getBatchMutationUrl(Context paramContext)
  {
    return MusicUrl.forTrackStatsBatchReport();
  }

  public long getCreationTimestamp()
  {
    throw new UnsupportedOperationException("mCreationTimestamp is not defined for this type.");
  }

  public MusicUrl getFeedUrl(Context paramContext)
  {
    throw new UnsupportedOperationException("Only BatchMutationUrl is supported on this type.");
  }

  public MusicUrl getFeedUrlAsPost(Context paramContext)
  {
    throw new UnsupportedOperationException("Only BatchMutationUrl is supported on this type.");
  }

  public int getIncrementalPlays()
  {
    return this.mIncrementalPlays;
  }

  public long getLastModifiedTimestamp()
  {
    throw new UnsupportedOperationException("mLastModifiedTimestamp is not defined for this type.");
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
    throw new UnsupportedOperationException("Only BatchMutationUrl is supported on this type.");
  }

  public boolean isDeleted()
  {
    throw new UnsupportedOperationException("mIsDeleted is not defined for this type.");
  }

  public boolean isInsert()
  {
    if (this.mRemoteId == null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isUpdate()
  {
    if (this.mRemoteId != null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public byte[] serializeAsJson()
    throws InvalidDataException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      JsonFactory localJsonFactory = Json.JSON_FACTORY;
      JsonEncoding localJsonEncoding = JsonEncoding.UTF8;
      JsonGenerator localJsonGenerator1 = localJsonFactory.createJsonGenerator(localByteArrayOutputStream, localJsonEncoding);
      JsonGenerator localJsonGenerator2 = localJsonGenerator1;
      try
      {
        Json.serialize(localJsonGenerator2, this);
        return localByteArrayOutputStream.toByteArray();
      }
      finally
      {
        localJsonGenerator2.close();
      }
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Unable to serialize a TrackStat as JSON: ");
      String str1 = toString();
      String str2 = str1 + ": ";
      int i = Log.e("MusicSyncAdapter", str2, localIOException);
      throw new InvalidDataException("Unable to serialize track stat for upstream sync.", localIOException);
    }
  }

  public void setCreationTimestamp(long paramLong)
  {
    throw new UnsupportedOperationException("mCreationTimestamp is not defined for this type.");
  }

  public void setIsDeleted(boolean paramBoolean)
  {
    throw new UnsupportedOperationException("mIsDeleted is not defined for this type.");
  }

  public void setLastModifiedTimestamp(long paramLong)
  {
    throw new UnsupportedOperationException("mLastModifiedTimestamp is not defined for this type.");
  }

  public void setRemoteId(String paramString)
  {
    this.mRemoteId = paramString;
  }

  public void validateForUpstreamDelete()
    throws InvalidDataException
  {
    throw new UnsupportedOperationException("Upstream deletion is not defined for this type.");
  }

  public void validateForUpstreamInsert()
    throws InvalidDataException
  {
    if (Strings.isNullOrEmpty(this.mRemoteId))
      throw new InvalidDataException("mRemoteId should not be null or empty.");
    if (this.mIncrementalPlays != 0)
      return;
    throw new InvalidDataException("mIncrementalPlays should be a positive int.");
  }

  public void validateForUpstreamUpdate()
    throws InvalidDataException
  {
    throw new UnsupportedOperationException("Upstream update is not defined for this type.");
  }

  public void wipeAllFields()
  {
    this.mRemoteId = null;
    this.mIncrementalPlays = 0;
    this.mLastPlayTimeMillis = 0L;
    this.mLocalId = 0L;
    this.mType = 0;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.TrackStat
 * JD-Core Version:    0.6.2
 */