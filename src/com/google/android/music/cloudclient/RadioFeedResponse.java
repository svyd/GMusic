package com.google.android.music.cloudclient;

import com.google.android.music.sync.google.model.SyncableRadioStation;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.ArrayList;
import java.util.List;

public class RadioFeedResponse extends GenericJson
{

  @Key("data")
  public RadioData mRadioData;

  public static class RadioData extends GenericJson
  {

    @Key("stations")
    public List<SyncableRadioStation> mRadioStations;

    public RadioData()
    {
      ArrayList localArrayList = new ArrayList();
      this.mRadioStations = localArrayList;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.RadioFeedResponse
 * JD-Core Version:    0.6.2
 */