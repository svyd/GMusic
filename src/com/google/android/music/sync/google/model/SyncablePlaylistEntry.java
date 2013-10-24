package com.google.android.music.sync.google.model;

import android.content.Context;
import android.util.Log;
import com.google.android.common.base.Strings;
import com.google.android.music.store.InvalidDataException;
import com.google.android.music.store.PlayList.Item;
import com.google.android.music.sync.api.MusicUrl;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.util.Key;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class SyncablePlaylistEntry extends GenericJson
  implements MusicQueueableSyncEntity
{
  public static final int LOCKER_SOURCE = 1;
  public static final int NAUTILUS_SOURCE = 2;

  @Key("absolutePosition")
  public String mAbsolutePosition;

  @Key("clientId")
  public String mClientId;

  @Key("creationTimestamp")
  public long mCreationTimestamp = 65535L;

  @Key("followingEntryId")
  public String mFollowingEntryClientId;

  @Key("deleted")
  public boolean mIsDeleted;
  public PlayList.Item mItem = null;

  @Key("lastModifiedTimestamp")
  public long mLastModifiedTimestamp = 65535L;
  public long mLocalId = 65535L;
  public long mLocalMusicId = 65535L;
  public long mLocalPlaylistId = 65535L;

  @Key("precedingEntryId")
  public String mPrecedingEntryClientId;

  @Key("id")
  public String mRemoteId;

  @Key("playlistId")
  public String mRemotePlaylistId;

  @Key("source")
  public int mSource = -1;

  @Key("track")
  public Track mTrack;

  @Key("trackId")
  public String mTrackId;

  private static int convertClientSourceTypeToServerSource(int paramInt)
  {
    switch (paramInt)
    {
    default:
      String str = "Not a syncable sourceType=" + paramInt;
      throw new IllegalArgumentException(str);
    case 2:
    case 3:
    }
    for (int i = 1; ; i = 2)
      return i;
  }

  public static int convertServerSourceToClientSourceType(int paramInt)
  {
    switch (paramInt)
    {
    default:
      String str = "Invalid source=" + paramInt;
      throw new IllegalArgumentException(str);
    case 1:
    case 2:
    }
    for (int i = 2; ; i = 3)
      return i;
  }

  public static SyncablePlaylistEntry parse(PlayList.Item paramItem)
  {
    return parse(null, -1, paramItem);
  }

  public static SyncablePlaylistEntry parse(String paramString, int paramInt, PlayList.Item paramItem)
  {
    SyncablePlaylistEntry localSyncablePlaylistEntry = new SyncablePlaylistEntry();
    localSyncablePlaylistEntry.mItem = paramItem;
    localSyncablePlaylistEntry.mTrackId = paramString;
    String str1 = paramItem.getSourceId();
    localSyncablePlaylistEntry.mRemoteId = str1;
    long l1 = paramItem.getId();
    localSyncablePlaylistEntry.mLocalId = l1;
    long l2 = paramItem.getMusicId();
    localSyncablePlaylistEntry.mLocalMusicId = l2;
    if (!Strings.isNullOrEmpty(paramItem.getClientId()))
    {
      String str2 = paramItem.getClientId();
      localSyncablePlaylistEntry.mClientId = str2;
    }
    try
    {
      long l3 = Long.valueOf(paramItem.getSourceVersion()).longValue();
      localSyncablePlaylistEntry.mLastModifiedTimestamp = l3;
      long l4 = paramItem.getListId();
      localSyncablePlaylistEntry.mLocalPlaylistId = l4;
      localSyncablePlaylistEntry.setServerSourceFromClientSourceType(paramInt);
      return localSyncablePlaylistEntry;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (true)
        localSyncablePlaylistEntry.mLastModifiedTimestamp = 0L;
    }
  }

  public static SyncablePlaylistEntry parseFromJsonInputStream(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramInputStream);
      JsonToken localJsonToken = localJsonParser.nextToken();
      SyncablePlaylistEntry localSyncablePlaylistEntry = (SyncablePlaylistEntry)Json.parse(localJsonParser, SyncablePlaylistEntry.class, null);
      return localSyncablePlaylistEntry;
    }
    catch (JsonParseException localJsonParseException)
    {
      String str = localJsonParseException.getMessage();
      throw new IOException(str);
    }
  }

  public PlayList.Item formatAsPlayListItem(PlayList.Item paramItem)
  {
    if (paramItem == null)
      paramItem = new PlayList.Item();
    long l = this.mLocalMusicId;
    paramItem.setMusicId(l);
    String str1 = this.mRemoteId;
    paramItem.setSourceId(str1);
    if (this.mAbsolutePosition != null)
    {
      String str2 = this.mAbsolutePosition;
      paramItem.setServerPosition(str2);
      String str3 = Long.toString(this.mLastModifiedTimestamp);
      paramItem.setSourceVersion(str3);
      if (paramItem.getClientId() != null)
      {
        String str4 = paramItem.getClientId();
        this.mClientId = str4;
      }
      return paramItem;
    }
    StringBuilder localStringBuilder1 = new StringBuilder().append("Server provided no position for id ");
    String str5 = this.mRemoteId;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str5).append(" in playlist");
    String str6 = this.mRemotePlaylistId;
    String str7 = str6;
    throw new InvalidDataException(str7);
  }

  public MusicUrl getBatchMutationUrl(Context paramContext)
  {
    return MusicUrl.forPlaylistEntriesBatchMutation();
  }

  public long getCreationTimestamp()
  {
    return this.mCreationTimestamp;
  }

  public PlayList.Item getEncapsulatedItem()
  {
    return this.mItem;
  }

  public MusicUrl getFeedUrl(Context paramContext)
  {
    return MusicUrl.forPlaylistEntriesFeed();
  }

  public MusicUrl getFeedUrlAsPost(Context paramContext)
  {
    return MusicUrl.forPlaylistEntriesFeedAsPost();
  }

  public long getLastModifiedTimestamp()
  {
    return this.mLastModifiedTimestamp;
  }

  public long getLocalId()
  {
    return this.mLocalId;
  }

  public long getLocalMusicId()
  {
    return this.mLocalMusicId;
  }

  public String getRemoteId()
  {
    return this.mRemoteId;
  }

  public int getSource()
  {
    return this.mSource;
  }

  public Track getTrack()
  {
    return this.mTrack;
  }

  public MusicUrl getUrl(Context paramContext, String paramString)
  {
    return MusicUrl.forPlaylistEntry(paramString);
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
      StringBuilder localStringBuilder = new StringBuilder().append("Unable to serialize a playlist entry as JSON: ");
      String str1 = toString();
      String str2 = str1 + ": ";
      int i = Log.e("MusicSyncAdapter", str2, localIOException);
      throw new InvalidDataException("Unable to serialize playlist entry for upstream sync.", localIOException);
    }
  }

  public void setCreationTimestamp(long paramLong)
  {
    this.mCreationTimestamp = paramLong;
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

  public void setServerSourceFromClientSourceType(int paramInt)
  {
    if (paramInt == -1)
      return;
    int i = convertClientSourceTypeToServerSource(paramInt);
    this.mSource = i;
  }

  public void setTrackId(String paramString)
  {
    this.mTrackId = paramString;
  }

  public String toString()
  {
    StringBuffer localStringBuffer1 = new StringBuffer();
    StringBuffer localStringBuffer2 = localStringBuffer1.append("; remoteid:");
    String str1 = this.mRemoteId;
    StringBuffer localStringBuffer3 = localStringBuffer2.append(str1).append("; ctime:");
    long l1 = this.mCreationTimestamp;
    StringBuffer localStringBuffer4 = localStringBuffer3.append(l1).append("; mtime:");
    long l2 = this.mLastModifiedTimestamp;
    StringBuffer localStringBuffer5 = localStringBuffer4.append(l2).append("; isDeleted: ");
    boolean bool = this.mIsDeleted;
    StringBuffer localStringBuffer6 = localStringBuffer5.append(bool).append("; remote entry id: ");
    String str2 = this.mRemoteId;
    StringBuffer localStringBuffer7 = localStringBuffer6.append(str2).append("; remote playlist id: ");
    String str3 = this.mRemotePlaylistId;
    StringBuffer localStringBuffer8 = localStringBuffer7.append(str3).append("; local playlist id: ");
    long l3 = this.mLocalPlaylistId;
    StringBuffer localStringBuffer9 = localStringBuffer8.append(l3).append("; absolute position: ");
    String str4 = this.mAbsolutePosition;
    StringBuffer localStringBuffer10 = localStringBuffer9.append(str4).append("; preceding id: ");
    String str5 = this.mPrecedingEntryClientId;
    StringBuffer localStringBuffer11 = localStringBuffer10.append(str5).append("; following id: ");
    String str6 = this.mFollowingEntryClientId;
    StringBuffer localStringBuffer12 = localStringBuffer11.append(str6).append("; remote track id: ");
    String str7 = this.mTrackId;
    StringBuffer localStringBuffer13 = localStringBuffer12.append(str7).append("; source id: ");
    String str8 = this.mRemoteId;
    StringBuffer localStringBuffer14 = localStringBuffer13.append(str8).append("; client id: ");
    String str9 = this.mClientId;
    StringBuffer localStringBuffer15 = localStringBuffer14.append(str9).append("; source: ");
    int i = this.mSource;
    StringBuffer localStringBuffer16 = localStringBuffer15.append(i).append("; track: ");
    Track localTrack = this.mTrack;
    StringBuffer localStringBuffer17 = localStringBuffer16.append(localTrack);
    return localStringBuffer1.toString();
  }

  public void validateForUpstreamDelete()
  {
    if (!Strings.isNullOrEmpty(this.mRemoteId))
      return;
    throw new InvalidDataException("Invalid playlist entry for upstream delete.");
  }

  public void validateForUpstreamInsert()
  {
    if ((!Strings.isNullOrEmpty(this.mRemotePlaylistId)) && (!Strings.isNullOrEmpty(this.mTrackId)))
      return;
    throw new InvalidDataException("Invalid playlist entry for upstream insert.");
  }

  public void validateForUpstreamUpdate()
  {
    if ((!Strings.isNullOrEmpty(this.mRemotePlaylistId)) && (!Strings.isNullOrEmpty(this.mTrackId)) && (!Strings.isNullOrEmpty(this.mRemoteId)))
      return;
    throw new InvalidDataException("Invalid playlist entry for upstream update.");
  }

  public void wipeAllFields()
  {
    this.mRemoteId = null;
    this.mRemotePlaylistId = null;
    this.mAbsolutePosition = null;
    this.mTrackId = null;
    this.mCreationTimestamp = 65535L;
    this.mLastModifiedTimestamp = 65535L;
    this.mIsDeleted = false;
    this.mLocalPlaylistId = 65535L;
    this.mItem = null;
    this.mLocalId = 65535L;
    this.mLocalMusicId = 65535L;
    this.mClientId = null;
    this.mSource = -1;
    this.mTrack = null;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.SyncablePlaylistEntry
 * JD-Core Version:    0.6.2
 */