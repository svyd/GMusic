package com.google.android.music.sync.google.model;

import android.content.Context;
import com.google.android.music.store.InvalidDataException;
import com.google.android.music.sync.api.MusicUrl;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class ConfigEntry extends GenericJson
  implements MusicQueueableSyncEntity
{

  @Key("key")
  public String mKey;

  @Key("value")
  public String mValue;

  public MusicUrl getBatchMutationUrl(Context paramContext)
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public long getCreationTimestamp()
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public MusicUrl getFeedUrl(Context paramContext)
  {
    return MusicUrl.forConfigEntriesFeed();
  }

  public MusicUrl getFeedUrlAsPost(Context paramContext)
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public String getKey()
  {
    return this.mKey;
  }

  public long getLastModifiedTimestamp()
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public long getLocalId()
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public String getRemoteId()
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public MusicUrl getUrl(Context paramContext, String paramString)
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public String getValue()
  {
    return this.mValue;
  }

  public boolean isDeleted()
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public boolean isInsert()
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public boolean isUpdate()
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public byte[] serializeAsJson()
    throws InvalidDataException
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public void setCreationTimestamp(long paramLong)
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public void setIsDeleted(boolean paramBoolean)
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public void setLastModifiedTimestamp(long paramLong)
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public void setRemoteId(String paramString)
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public String toString()
  {
    StringBuffer localStringBuffer1 = new StringBuffer();
    StringBuffer localStringBuffer2 = localStringBuffer1.append("key:");
    String str1 = this.mKey;
    StringBuffer localStringBuffer3 = localStringBuffer2.append(str1);
    StringBuffer localStringBuffer4 = localStringBuffer1.append("; value:");
    String str2 = this.mValue;
    StringBuffer localStringBuffer5 = localStringBuffer4.append(str2);
    return localStringBuffer1.toString();
  }

  public void validateForUpstreamDelete()
    throws InvalidDataException
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public void validateForUpstreamInsert()
    throws InvalidDataException
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public void validateForUpstreamUpdate()
    throws InvalidDataException
  {
    throw new UnsupportedOperationException("Unneeded operation for config API");
  }

  public void wipeAllFields()
  {
    this.mKey = null;
    this.mValue = null;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.ConfigEntry
 * JD-Core Version:    0.6.2
 */