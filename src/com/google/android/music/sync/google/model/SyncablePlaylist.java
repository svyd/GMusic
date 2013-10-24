package com.google.android.music.sync.google.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.common.base.Strings;
import com.google.android.music.cloudclient.TrackJson.ImageRef;
import com.google.android.music.store.InvalidDataException;
import com.google.android.music.store.PlayList;
import com.google.android.music.sync.api.MusicUrl;
import com.google.android.music.utils.MusicUtils;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.util.Key;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class SyncablePlaylist extends GenericJson
  implements MusicQueueableSyncEntity
{

  @Key("albumArtRef")
  public List<TrackJson.ImageRef> mArtUrls;

  @Key("clientId")
  public String mClientId;

  @Key("creationTimestamp")
  public long mCreationTimestamp = 65535L;

  @Key("description")
  public String mDescription;

  @Key("deleted")
  public boolean mIsDeleted;

  @Key("lastModifiedTimestamp")
  public long mLastModifiedTimestamp = 65535L;
  public long mLocalId = 65535L;

  @Key("name")
  public String mName;

  @Key("ownerName")
  public String mOwnerName;

  @Key("ownerProfilePhotoUrl")
  public String mOwnerProfilePhotoUrl;
  private PlayList mPlayList;

  @Key("id")
  public String mRemoteId;

  @Key("shareToken")
  public String mShareToken;

  @Key("type")
  public String mType = "USER_GENERATED";

  public static SyncablePlaylist parse(PlayList paramPlayList)
  {
    SyncablePlaylist localSyncablePlaylist = new SyncablePlaylist();
    localSyncablePlaylist.mPlayList = paramPlayList;
    String str1 = paramPlayList.getSourceId();
    localSyncablePlaylist.mRemoteId = str1;
    long l1 = paramPlayList.getId();
    localSyncablePlaylist.mLocalId = l1;
    if (paramPlayList.getName() != null)
    {
      String str2 = paramPlayList.getName();
      localSyncablePlaylist.mName = str2;
    }
    if (paramPlayList.getType() == 1)
      localSyncablePlaylist.mType = "MAGIC";
    try
    {
      while (true)
      {
        long l2 = Long.valueOf(paramPlayList.getSourceVersion()).longValue();
        localSyncablePlaylist.mLastModifiedTimestamp = l2;
        String str3 = paramPlayList.getShareToken();
        localSyncablePlaylist.mShareToken = str3;
        String str4 = paramPlayList.getOwnerName();
        localSyncablePlaylist.mOwnerName = str4;
        String str5 = paramPlayList.getDescription();
        localSyncablePlaylist.mDescription = str5;
        String str6 = paramPlayList.getArtworkLocation();
        if (TextUtils.isEmpty(str6))
          break label255;
        String[] arrayOfString = MusicUtils.decodeStringArray(str6);
        arrayOfImageRef = new TrackJson.ImageRef[arrayOfString.length];
        int i = 0;
        while (true)
        {
          int j = arrayOfString.length;
          if (i >= j)
            break;
          TrackJson.ImageRef localImageRef = new TrackJson.ImageRef();
          String str7 = arrayOfString[i];
          localImageRef.mUrl = str7;
          arrayOfImageRef[i] = localImageRef;
          i += 1;
        }
        if (paramPlayList.getType() == 71)
          localSyncablePlaylist.mType = "SHARED";
        else
          localSyncablePlaylist.mType = "USER_GENERATED";
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
      TrackJson.ImageRef[] arrayOfImageRef;
      while (true)
        localSyncablePlaylist.mLastModifiedTimestamp = 0L;
      List localList = Arrays.asList(arrayOfImageRef);
      localSyncablePlaylist.mArtUrls = localList;
      label255: String str8 = paramPlayList.getOwnerProfilePhotoUrl();
      localSyncablePlaylist.mOwnerProfilePhotoUrl = str8;
    }
    return localSyncablePlaylist;
  }

  public static SyncablePlaylist parseFromJsonInputStream(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramInputStream);
      JsonToken localJsonToken = localJsonParser.nextToken();
      SyncablePlaylist localSyncablePlaylist = (SyncablePlaylist)Json.parse(localJsonParser, SyncablePlaylist.class, null);
      return localSyncablePlaylist;
    }
    catch (JsonParseException localJsonParseException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Failed to parse playlist: ");
      String str1 = localJsonParseException.getMessage();
      String str2 = str1;
      throw new IOException(str2);
    }
  }

  public PlayList formatAsPlayList(PlayList paramPlayList)
  {
    if (paramPlayList == null)
      paramPlayList = new PlayList();
    String str1 = this.mRemoteId;
    paramPlayList.setSourceId(str1);
    String str2 = this.mName;
    paramPlayList.setName(str2);
    String str3 = Long.toString(this.mLastModifiedTimestamp);
    paramPlayList.setSourceVersion(str3);
    String str4 = this.mType;
    if ("MAGIC".equals(str4))
      paramPlayList.setType(1);
    String[] arrayOfString;
    while (true)
    {
      String str5 = this.mShareToken;
      paramPlayList.setShareToken(str5);
      String str6 = this.mOwnerName;
      paramPlayList.setOwnerName(str6);
      String str7 = this.mDescription;
      paramPlayList.setDescription(str7);
      if ((this.mArtUrls == null) || (this.mArtUrls.isEmpty()))
        break label233;
      arrayOfString = new String[this.mArtUrls.size()];
      int i = 0;
      while (true)
      {
        int j = arrayOfString.length;
        if (i >= j)
          break;
        String str8 = ((TrackJson.ImageRef)this.mArtUrls.get(i)).mUrl;
        arrayOfString[i] = str8;
        i += 1;
      }
      String str9 = this.mType;
      if ("SHARED".equals(str9))
        paramPlayList.setType(71);
      else
        paramPlayList.setType(0);
    }
    String str10 = MusicUtils.encodeStringArray(arrayOfString);
    paramPlayList.setArtworkLocation(str10);
    label233: String str11 = this.mOwnerProfilePhotoUrl;
    paramPlayList.setOwnerProfilePhotoUrl(str11);
    return paramPlayList;
  }

  public MusicUrl getBatchMutationUrl(Context paramContext)
  {
    return MusicUrl.forPlaylistsBatchMutation();
  }

  public long getCreationTimestamp()
  {
    return this.mCreationTimestamp;
  }

  public PlayList getEncapsulatedPlayList()
  {
    return this.mPlayList;
  }

  public MusicUrl getFeedUrl(Context paramContext)
  {
    return MusicUrl.forPlaylistsFeed();
  }

  public MusicUrl getFeedUrlAsPost(Context paramContext)
  {
    return MusicUrl.forPlaylistsFeedAsPost();
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
    return MusicUrl.forPlaylist(paramString);
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
        localJsonGenerator2.close();
        byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
        if (Log.isLoggable("MusicSyncAdapter", 2))
        {
          String str1 = localByteArrayOutputStream.toString();
          int i = Log.v("MusicSyncAdapter", str1);
        }
        return arrayOfByte;
      }
      finally
      {
        localJsonGenerator2.close();
      }
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Unable to serialize a playlist as JSON: ");
      String str2 = toString();
      String str3 = str2 + ": ";
      int j = Log.e("MusicSyncAdapter", str3, localIOException);
      throw new InvalidDataException("Unable to serialize playlist for upstream sync.", localIOException);
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

  public String toString()
  {
    StringBuffer localStringBuffer1 = new StringBuffer();
    StringBuffer localStringBuffer2 = localStringBuffer1.append("; localid:");
    long l1 = this.mLocalId;
    StringBuffer localStringBuffer3 = localStringBuffer2.append(l1).append("; remoteid:");
    String str1 = this.mRemoteId;
    StringBuffer localStringBuffer4 = localStringBuffer3.append(str1).append("; ctime:");
    long l2 = this.mCreationTimestamp;
    StringBuffer localStringBuffer5 = localStringBuffer4.append(l2).append("; mtime:");
    long l3 = this.mLastModifiedTimestamp;
    StringBuffer localStringBuffer6 = localStringBuffer5.append(l3).append("; isDeleted: ");
    boolean bool = this.mIsDeleted;
    StringBuffer localStringBuffer7 = localStringBuffer6.append(bool).append("; name: ");
    String str2 = this.mName;
    StringBuffer localStringBuffer8 = localStringBuffer7.append(str2).append("; clientId: ");
    String str3 = this.mClientId;
    StringBuffer localStringBuffer9 = localStringBuffer8.append(str3).append("; type: ");
    String str4 = this.mType;
    StringBuffer localStringBuffer10 = localStringBuffer9.append(str4).append("; shareToken: ");
    String str5 = this.mShareToken;
    StringBuffer localStringBuffer11 = localStringBuffer10.append(str5).append("; ownerName: ");
    String str6 = this.mOwnerName;
    StringBuffer localStringBuffer12 = localStringBuffer11.append(str6).append("; description: ");
    String str7 = this.mDescription;
    StringBuffer localStringBuffer13 = localStringBuffer12.append(str7).append("; artUrls: ");
    List localList = this.mArtUrls;
    StringBuffer localStringBuffer14 = localStringBuffer13.append(localList).append("; ownerProfilePhotoUrl");
    String str8 = this.mOwnerProfilePhotoUrl;
    StringBuffer localStringBuffer15 = localStringBuffer14.append(str8);
    return localStringBuffer1.toString();
  }

  public void validateForUpstreamDelete()
  {
    if (!Strings.isNullOrEmpty(this.mRemoteId))
      return;
    throw new InvalidDataException("Invalid playlist for upstream delete.");
  }

  public void validateForUpstreamInsert()
  {
    if (!Strings.isNullOrEmpty(this.mName))
      return;
    throw new InvalidDataException("Invalid playlist for upstream insert.");
  }

  public void validateForUpstreamUpdate()
  {
    if ((!Strings.isNullOrEmpty(this.mName)) && (!Strings.isNullOrEmpty(this.mRemoteId)))
      return;
    throw new InvalidDataException("Invalid playlist for upstream update.");
  }

  public void wipeAllFields()
  {
    this.mRemoteId = null;
    this.mCreationTimestamp = 65535L;
    this.mLastModifiedTimestamp = 65535L;
    this.mIsDeleted = false;
    this.mName = null;
    this.mPlayList = null;
    this.mLocalId = 65535L;
    this.mClientId = null;
    this.mType = "USER_GENERATED";
    this.mShareToken = null;
    this.mOwnerName = null;
    this.mDescription = null;
    this.mArtUrls = null;
    this.mOwnerProfilePhotoUrl = null;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.SyncablePlaylist
 * JD-Core Version:    0.6.2
 */