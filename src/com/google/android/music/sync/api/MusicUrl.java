package com.google.android.music.sync.api;

import android.text.TextUtils;
import com.google.android.music.utils.ConfigUtils;
import com.google.api.client.googleapis.GoogleUrl;
import java.util.List;
import java.util.Locale;

public class MusicUrl extends GoogleUrl
{
  private MusicUrl(String paramString)
  {
    super(paramString);
    Locale localLocale = Locale.getDefault();
    String str1 = localLocale.getLanguage();
    String str2 = localLocale.getCountry();
    if (TextUtils.isEmpty(str2));
    for (String str3 = str1; ; str3 = str1 + "_" + str2)
    {
      Object localObject = put("hl", str3);
      return;
    }
  }

  public static MusicUrl forConfigEntriesFeed()
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    String str = ConfigUtils.getCookie();
    if (!TextUtils.isEmpty(str))
      Object localObject = localMusicUrl.put("cookie", str);
    localMusicUrl.alt = "json";
    boolean bool = localMusicUrl.pathParts.add("config");
    return localMusicUrl;
  }

  public static MusicUrl forEphemeralTop(int paramInt)
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool1 = localMusicUrl.pathParts.add("ephemeral");
    boolean bool2 = localMusicUrl.pathParts.add("top");
    Integer localInteger = Integer.valueOf(paramInt);
    Object localObject = localMusicUrl.put("updated-min", localInteger);
    return localMusicUrl;
  }

  public static MusicUrl forGenres(String paramString)
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool1 = localMusicUrl.pathParts.add("explore");
    boolean bool2 = localMusicUrl.pathParts.add("genres");
    if (paramString != null)
      Object localObject = localMusicUrl.put("parent-genre", paramString);
    return localMusicUrl;
  }

  public static MusicUrl forGetSharedPlaylistEntries(int paramInt, String paramString, long paramLong)
  {
    if (paramInt < 1)
      throw new IllegalArgumentException("Invalid number of max entities requested.");
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool1 = localMusicUrl.pathParts.add("plentries");
    boolean bool2 = localMusicUrl.pathParts.add("shared");
    return localMusicUrl;
  }

  public static MusicUrl forNautilusAlbum(String paramString)
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool = localMusicUrl.pathParts.add("fetchalbum");
    Object localObject1 = localMusicUrl.put("nid", paramString);
    Object localObject2 = localMusicUrl.put("include-tracks", "true");
    return localMusicUrl;
  }

  public static MusicUrl forNautilusArtist(String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool = localMusicUrl.pathParts.add("fetchartist");
    Object localObject1 = localMusicUrl.put("nid", paramString);
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    Object localObject2 = localMusicUrl.put("include-albums", localBoolean);
    if (paramInt1 > 0)
    {
      Integer localInteger1 = Integer.valueOf(paramInt1);
      Object localObject3 = localMusicUrl.put("num-top-tracks", localInteger1);
    }
    if (paramInt2 > 0)
    {
      Integer localInteger2 = Integer.valueOf(paramInt2);
      Object localObject4 = localMusicUrl.put("num-related-artists", localInteger2);
    }
    return localMusicUrl;
  }

  public static MusicUrl forNautilusTrack(String paramString)
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool = localMusicUrl.pathParts.add("fetchtrack");
    Object localObject = localMusicUrl.put("nid", paramString);
    return localMusicUrl;
  }

  public static MusicUrl forNewReleases(int paramInt)
  {
    if (paramInt < 1)
      throw new IllegalArgumentException("Invalid number of entities requested.");
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool1 = localMusicUrl.pathParts.add("explore");
    boolean bool2 = localMusicUrl.pathParts.add("newreleases");
    Integer localInteger = Integer.valueOf(paramInt);
    Object localObject = localMusicUrl.put("max-entities-per-group", localInteger);
    return localMusicUrl;
  }

  public static MusicUrl forOffers()
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool1 = localMusicUrl.pathParts.add("signup");
    boolean bool2 = localMusicUrl.pathParts.add("offers");
    return localMusicUrl;
  }

  public static MusicUrl forPlaylist(String paramString)
  {
    MusicUrl localMusicUrl = forPlaylistsFeed();
    boolean bool = localMusicUrl.pathParts.add(paramString);
    return localMusicUrl;
  }

  public static MusicUrl forPlaylistEntriesBatchMutation()
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool = localMusicUrl.pathParts.add("plentriesbatch");
    return localMusicUrl;
  }

  public static MusicUrl forPlaylistEntriesFeed()
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool = localMusicUrl.pathParts.add("plentries");
    return localMusicUrl;
  }

  public static MusicUrl forPlaylistEntriesFeedAsPost()
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool = localMusicUrl.pathParts.add("plentryfeed");
    return localMusicUrl;
  }

  public static MusicUrl forPlaylistEntry(String paramString)
  {
    MusicUrl localMusicUrl = forPlaylistEntriesFeed();
    boolean bool = localMusicUrl.pathParts.add(paramString);
    return localMusicUrl;
  }

  public static MusicUrl forPlaylistsBatchMutation()
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool = localMusicUrl.pathParts.add("playlistbatch");
    return localMusicUrl;
  }

  public static MusicUrl forPlaylistsFeed()
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool = localMusicUrl.pathParts.add("playlists");
    return localMusicUrl;
  }

  public static MusicUrl forPlaylistsFeedAsPost()
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool = localMusicUrl.pathParts.add("playlistfeed");
    return localMusicUrl;
  }

  public static MusicUrl forRadioEditStations()
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool1 = localMusicUrl.pathParts.add("radio");
    boolean bool2 = localMusicUrl.pathParts.add("editstation");
    return localMusicUrl;
  }

  public static MusicUrl forRadioGetStations()
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool1 = localMusicUrl.pathParts.add("radio");
    boolean bool2 = localMusicUrl.pathParts.add("station");
    return localMusicUrl;
  }

  public static MusicUrl forRadioStationFeed()
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool1 = localMusicUrl.pathParts.add("radio");
    boolean bool2 = localMusicUrl.pathParts.add("stationfeed");
    return localMusicUrl;
  }

  public static MusicUrl forTab(ExploreTabType paramExploreTabType, int paramInt, String paramString)
  {
    if (paramInt < 1)
      throw new IllegalArgumentException("Invalid number of max entities requested.");
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool1 = localMusicUrl.pathParts.add("explore");
    boolean bool2 = localMusicUrl.pathParts.add("tabs");
    Integer localInteger1 = Integer.valueOf(paramExploreTabType.ordinal());
    Object localObject1 = localMusicUrl.put("tabs", localInteger1);
    Integer localInteger2 = Integer.valueOf(paramInt);
    Object localObject2 = localMusicUrl.put("num-items", localInteger2);
    if (paramString != null)
      Object localObject3 = localMusicUrl.put("genre", paramString);
    return localMusicUrl;
  }

  public static MusicUrl forTrack(String paramString, int paramInt)
  {
    MusicUrl localMusicUrl = forTracksFeed(paramInt);
    boolean bool = localMusicUrl.pathParts.add(paramString);
    return localMusicUrl;
  }

  public static MusicUrl forTrackStatsBatchReport()
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool = localMusicUrl.pathParts.add("trackstats");
    return localMusicUrl;
  }

  public static MusicUrl forTracksBatchMutation()
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool = localMusicUrl.pathParts.add("trackbatch");
    return localMusicUrl;
  }

  public static MusicUrl forTracksFeed(int paramInt)
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool = localMusicUrl.pathParts.add("tracks");
    Integer localInteger = Integer.valueOf(paramInt);
    Object localObject = localMusicUrl.put("art-dimension", localInteger);
    return localMusicUrl;
  }

  public static MusicUrl forTracksFeedAsPost(int paramInt)
  {
    MusicUrl localMusicUrl = new MusicUrl("https://www.googleapis.com/sj/v1.1");
    localMusicUrl.alt = "json";
    boolean bool = localMusicUrl.pathParts.add("trackfeed");
    Integer localInteger = Integer.valueOf(paramInt);
    Object localObject = localMusicUrl.put("art-dimension", localInteger);
    return localMusicUrl;
  }

  public static enum ExploreTabType
  {
    static
    {
      NEW_RELEASES = new ExploreTabType("NEW_RELEASES", 2);
      GENRES = new ExploreTabType("GENRES", 3);
      ExploreTabType[] arrayOfExploreTabType = new ExploreTabType[4];
      ExploreTabType localExploreTabType1 = RECOMMENDED;
      arrayOfExploreTabType[0] = localExploreTabType1;
      ExploreTabType localExploreTabType2 = TOP_CHARTS;
      arrayOfExploreTabType[1] = localExploreTabType2;
      ExploreTabType localExploreTabType3 = NEW_RELEASES;
      arrayOfExploreTabType[2] = localExploreTabType3;
      ExploreTabType localExploreTabType4 = GENRES;
      arrayOfExploreTabType[3] = localExploreTabType4;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.api.MusicUrl
 * JD-Core Version:    0.6.2
 */