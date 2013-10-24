package com.google.android.music.cloudclient;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

@Deprecated
public class TopChartsResponseJson extends GenericJson
{

  @Key("topAlbums")
  public ExploreEntityGroupJson mTopAlbums;

  @Key("topPlaylists")
  public ExploreEntityGroupJson mTopPlaylists;

  @Key("topTracks")
  public ExploreEntityGroupJson mTopTracks;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.TopChartsResponseJson
 * JD-Core Version:    0.6.2
 */