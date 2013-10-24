package com.google.android.music.cloudclient;

import com.google.android.music.sync.google.model.Track;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public class SearchClientResponseJson extends GenericJson
{

  @Key("continuation_token")
  public String mContinuationToken;

  @Key("entries")
  public List<SearchClientResponseEntry> mEntries;

  @Key("num_results")
  public int mNumResults;

  public SearchClientResponseJson()
  {
    ArrayList localArrayList = Lists.newArrayList();
    this.mEntries = localArrayList;
  }

  public static class SearchClientResponseEntry
  {

    @Key("album")
    public AlbumJson mAlbum;

    @Key("artist")
    public ArtistJson mArtist;

    @Key("music_genre")
    public MusicGenreJson mGenre;

    @Key("navigational_confidence")
    public float mNavigationalConfidence;

    @Key("navigational_result")
    public boolean mNavigationalResult;

    @Key("track")
    public Track mTrack;

    @Key("type")
    public String mType;

    public Type getType()
    {
      int i = Integer.parseInt(this.mType);
      Type[] arrayOfType = Type.values();
      int j = i + -1;
      return arrayOfType[j];
    }

    public static enum Type
    {
      static
      {
        ARTIST = new Type("ARTIST", 1);
        ALBUM = new Type("ALBUM", 2);
        PLAYLIST = new Type("PLAYLIST", 3);
        GENRE = new Type("GENRE", 4);
        Type[] arrayOfType = new Type[5];
        Type localType1 = TRACK;
        arrayOfType[0] = localType1;
        Type localType2 = ARTIST;
        arrayOfType[1] = localType2;
        Type localType3 = ALBUM;
        arrayOfType[2] = localType3;
        Type localType4 = PLAYLIST;
        arrayOfType[3] = localType4;
        Type localType5 = GENRE;
        arrayOfType[4] = localType5;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.SearchClientResponseJson
 * JD-Core Version:    0.6.2
 */