package com.google.android.music.cloudclient;

import com.google.android.music.sync.google.model.Track;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.List;

public class AlbumJson extends GenericJson
{

  @Key("albumArtRef")
  public String mAlbumArtRef;

  @Key("albumArtist")
  public String mAlbumArtist;

  @Key("albumId")
  public String mAlbumId;

  @Key("artist")
  public String mArtist;

  @Key("artistId")
  public List<String> mArtistId;

  @Key("creationTimestamp")
  public long mCreationTimestamp;

  @Key("isCompilation")
  public boolean mIsCompilation;

  @Key("lastTimePlayed")
  public long mLastTimePlayed;

  @Key("name")
  public String mName;

  @Key("trackCount")
  public int mTrackCount;

  @Key("tracks")
  public List<Track> mTracks;

  @Key("year")
  public int mYear;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.AlbumJson
 * JD-Core Version:    0.6.2
 */