package com.google.android.music.cloudclient;

import com.google.android.music.sync.google.model.SyncablePlaylist;
import com.google.android.music.sync.google.model.Track;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class ExploreEntityJson extends GenericJson
{

  @Key("album")
  public AlbumJson mAlbum;

  @Key("playlist")
  public SyncablePlaylist mPlaylist;

  @Key("track")
  public Track mTrack;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.ExploreEntityJson
 * JD-Core Version:    0.6.2
 */