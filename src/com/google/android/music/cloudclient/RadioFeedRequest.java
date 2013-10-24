package com.google.android.music.cloudclient;

import android.util.Log;
import com.google.android.music.sync.google.model.RadioSeed;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RadioFeedRequest extends GenericJson
{
  public static final int EXPLICIT_CONTENT_FILTER_VALUE = 2;
  public static final int NO_CONTENT_FILTER_VALUE = 1;

  @Key("contentFilter")
  public int mContentFilter;

  @Key("mixes")
  public MixRequest mMixes;

  @Key("stations")
  public List<RadioStationRequest> mStations;

  public RadioFeedRequest()
  {
    ArrayList localArrayList = new ArrayList();
    this.mStations = localArrayList;
    this.mContentFilter = 1;
  }

  private static int checkContentFilter(int paramInt)
  {
    if (!validateContentFilter(paramInt))
    {
      String str = "Invalid value for content filter: " + paramInt;
      int i = Log.wtf("RadioFeedRequest", str);
      paramInt = 1;
    }
    return paramInt;
  }

  public static byte[] serialize(int paramInt1, int paramInt2, int paramInt3)
    throws IOException
  {
    if (!validateContentFilter(paramInt3))
    {
      String str = "Invalid value for content filter: " + paramInt3;
      int i = Log.wtf("RadioFeedRequest", str);
      paramInt3 = 1;
    }
    RadioFeedRequest localRadioFeedRequest = new RadioFeedRequest();
    localRadioFeedRequest.mContentFilter = paramInt3;
    MixRequest localMixRequest = new MixRequest();
    localRadioFeedRequest.mMixes = localMixRequest;
    localRadioFeedRequest.mMixes.mNumSeeds = paramInt1;
    localRadioFeedRequest.mMixes.mNumEntries = paramInt2;
    return JsonUtils.toJsonByteArray(localRadioFeedRequest);
  }

  public static byte[] serialize(String paramString, int paramInt1, List<MixTrackId> paramList, int paramInt2)
    throws IOException
  {
    int i = checkContentFilter(paramInt2);
    RadioFeedRequest localRadioFeedRequest = new RadioFeedRequest();
    localRadioFeedRequest.mContentFilter = i;
    RadioStationRequest localRadioStationRequest = new RadioStationRequest();
    localRadioStationRequest.mRadioId = paramString;
    LinkedList localLinkedList = new LinkedList();
    localRadioStationRequest.mRecentlyPlayed = localLinkedList;
    localRadioStationRequest.mNumEntries = paramInt1;
    localRadioStationRequest.setRecentlyPlayedFromMixList(paramList);
    boolean bool = localRadioFeedRequest.mStations.add(localRadioStationRequest);
    return JsonUtils.toJsonByteArray(localRadioFeedRequest);
  }

  public static byte[] serializeForLuckyRadio(int paramInt1, List<MixTrackId> paramList, int paramInt2)
    throws IOException
  {
    int i = checkContentFilter(paramInt2);
    RadioFeedRequest localRadioFeedRequest = new RadioFeedRequest();
    localRadioFeedRequest.mContentFilter = i;
    RadioStationRequest localRadioStationRequest = new RadioStationRequest();
    RadioSeed localRadioSeed = RadioSeed.createRadioSeed(null, 6);
    localRadioStationRequest.mSeed = localRadioSeed;
    localRadioStationRequest.mNumEntries = paramInt1;
    localRadioStationRequest.setRecentlyPlayedFromMixList(paramList);
    boolean bool = localRadioFeedRequest.mStations.add(localRadioStationRequest);
    return JsonUtils.toJsonByteArray(localRadioFeedRequest);
  }

  private static boolean validateContentFilter(int paramInt)
  {
    int i = 1;
    if ((paramInt == i) || (paramInt == 2));
    while (true)
    {
      return i;
      i = 0;
    }
  }

  public static class MixRequest extends GenericJson
  {

    @Key("numEntries")
    public int mNumEntries;

    @Key("numSeeds")
    public int mNumSeeds;
  }

  public static class RadioStationRequest extends GenericJson
  {

    @Key("numEntries")
    public int mNumEntries;

    @Key("radioId")
    public String mRadioId;

    @Key("recentlyPlayed")
    public List<RecentlyPlayedEntryJson> mRecentlyPlayed;

    @Key("seed")
    public RadioSeed mSeed;

    public RadioStationRequest()
    {
      ArrayList localArrayList = new ArrayList();
      this.mRecentlyPlayed = localArrayList;
    }

    public void setRecentlyPlayedFromMixList(List<MixTrackId> paramList)
    {
      this.mRecentlyPlayed.clear();
      Iterator localIterator = paramList.iterator();
      while (true)
      {
        if (!localIterator.hasNext())
          return;
        MixTrackId localMixTrackId = (MixTrackId)localIterator.next();
        RecentlyPlayedEntryJson localRecentlyPlayedEntryJson = new RecentlyPlayedEntryJson();
        String str = localMixTrackId.getRemoteId();
        localRecentlyPlayedEntryJson.mId = str;
        int i = MixTrackId.trackIdTypeToServerType(localMixTrackId.getType());
        localRecentlyPlayedEntryJson.mType = i;
        boolean bool = this.mRecentlyPlayed.add(localRecentlyPlayedEntryJson);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.RadioFeedRequest
 * JD-Core Version:    0.6.2
 */