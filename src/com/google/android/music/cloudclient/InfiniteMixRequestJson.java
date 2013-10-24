package com.google.android.music.cloudclient;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class InfiniteMixRequestJson extends GenericJson
{

  @Key("numEntries")
  public int mNumEntries;

  @Key("recentlyPlayed")
  public List<RecentlyPlayedEntryJson> mRecentlyPlayed;

  @Key("seedId")
  public String mSeedId;

  @Key("seedType")
  public int mSeedType;

  public static byte[] serialize(MixTrackId paramMixTrackId, int paramInt, List<MixTrackId> paramList)
    throws IOException
  {
    InfiniteMixRequestJson localInfiniteMixRequestJson = new InfiniteMixRequestJson();
    String str1 = paramMixTrackId.getRemoteId();
    localInfiniteMixRequestJson.mSeedId = str1;
    int i = MixTrackId.trackIdTypeToServerType(paramMixTrackId.getType());
    localInfiniteMixRequestJson.mSeedType = i;
    LinkedList localLinkedList = new LinkedList();
    localInfiniteMixRequestJson.mRecentlyPlayed = localLinkedList;
    localInfiniteMixRequestJson.mNumEntries = paramInt;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      MixTrackId localMixTrackId = (MixTrackId)localIterator.next();
      RecentlyPlayedEntryJson localRecentlyPlayedEntryJson = new RecentlyPlayedEntryJson();
      String str2 = localMixTrackId.getRemoteId();
      localRecentlyPlayedEntryJson.mId = str2;
      int j = MixTrackId.trackIdTypeToServerType(localMixTrackId.getType());
      localRecentlyPlayedEntryJson.mType = j;
      boolean bool = localInfiniteMixRequestJson.mRecentlyPlayed.add(localRecentlyPlayedEntryJson);
    }
    return JsonUtils.toJsonByteArray(localInfiniteMixRequestJson);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.InfiniteMixRequestJson
 * JD-Core Version:    0.6.2
 */