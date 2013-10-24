package com.google.android.music.cloudclient;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetSharedPlaylistEntriesRequestJson extends GenericJson
{

  @Key("entries")
  public List<Entry> mEntries;

  @Key("includeDeleted")
  public boolean mIncludeDeleted;

  public GetSharedPlaylistEntriesRequestJson()
  {
    ArrayList localArrayList = new ArrayList();
    this.mEntries = localArrayList;
  }

  public static byte[] serialize(String paramString1, int paramInt, String paramString2, long paramLong)
    throws IOException
  {
    GetSharedPlaylistEntriesRequestJson localGetSharedPlaylistEntriesRequestJson = new GetSharedPlaylistEntriesRequestJson();
    Entry localEntry = new Entry();
    localEntry.mSharePlaylistToken = paramString1;
    localEntry.mMaxResults = paramInt;
    localEntry.mStartToken = paramString2;
    localEntry.mUpdateMin = paramLong;
    boolean bool = localGetSharedPlaylistEntriesRequestJson.mEntries.add(localEntry);
    return JsonUtils.toJsonByteArray(localGetSharedPlaylistEntriesRequestJson);
  }

  public static class Entry extends GenericJson
  {

    @Key("maxResults")
    public int mMaxResults;

    @Key("shareToken")
    public String mSharePlaylistToken;

    @Key("startToken")
    public String mStartToken;

    @Key("updatedMin")
    public long mUpdateMin;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.GetSharedPlaylistEntriesRequestJson
 * JD-Core Version:    0.6.2
 */