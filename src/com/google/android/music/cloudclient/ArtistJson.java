package com.google.android.music.cloudclient;

import com.google.android.music.sync.google.model.Track;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public class ArtistJson extends GenericJson
{

  @Key("albums")
  public List<AlbumJson> mAlbums;

  @Key("artistArtRef")
  public String mArtistArtRef;

  @Key("artistBio")
  public String mArtistBio;

  @Key("artistId")
  public String mArtistId;

  @Key("name")
  public String mName;

  @Key("related_artists")
  public List<ArtistJson> mRelatedArtists;

  @Key("topTracks")
  public List<Track> mTopTracks;

  @Key("totalTrackCount")
  public int mTotalTrackCount;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.ArtistJson
 * JD-Core Version:    0.6.2
 */