package com.google.android.music.cloudclient;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import android.net.http.AndroidHttpClient;
import android.text.TextUtils;
import com.google.android.common.http.GoogleHttpClient;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.sync.api.MusicUrl;
import com.google.android.music.sync.api.MusicUrl.ExploreTabType;
import com.google.android.music.sync.google.model.FeedAsPostRequest;
import com.google.android.music.sync.google.model.RadioEditStationsRequest;
import com.google.android.music.sync.google.model.SyncablePlaylist;
import com.google.android.music.sync.google.model.SyncableRadioStation;
import com.google.android.music.sync.google.model.Track;
import com.google.android.music.sync.google.model.TrackFeed;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;

public class MusicCloudImpl
  implements MusicCloud
{
  private static final String DEFAULT_SEARCH_CONTENT_TYPE = TextUtils.join(",", arrayOfInteger);
  private final Context mContext;
  private final GoogleHttpClient mHttpClient;

  static
  {
    Integer[] arrayOfInteger = new Integer[5];
    Integer localInteger1 = Integer.valueOf(1);
    arrayOfInteger[0] = localInteger1;
    Integer localInteger2 = Integer.valueOf(2);
    arrayOfInteger[1] = localInteger2;
    Integer localInteger3 = Integer.valueOf(3);
    arrayOfInteger[2] = localInteger3;
    Integer localInteger4 = Integer.valueOf(4);
    arrayOfInteger[3] = localInteger4;
    Integer localInteger5 = Integer.valueOf(5);
    arrayOfInteger[4] = localInteger5;
  }

  public MusicCloudImpl(Context paramContext)
  {
    this.mContext = paramContext;
    GoogleHttpClient localGoogleHttpClient = MusicRequest.getSharedHttpClient(paramContext);
    this.mHttpClient = localGoogleHttpClient;
  }

  private <T> T executeGet(String paramString, Class<T> paramClass)
    throws IOException, InterruptedException
  {
    Object localObject1 = new Object();
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this.mContext, localObject1);
    try
    {
      Context localContext = this.mContext;
      MusicRequest localMusicRequest = new MusicRequest(localContext, localMusicPreferences);
      Object localObject2 = executeGet(paramString, paramClass, localMusicRequest);
      Object localObject3 = localObject2;
      return localObject3;
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(localObject1);
    }
  }

  private <T> T executeGet(String paramString, Class<T> paramClass, MusicRequest paramMusicRequest)
    throws IOException, InterruptedException
  {
    String str = paramString.toString();
    HttpGet localHttpGet = new HttpGet(str);
    GoogleHttpClient localGoogleHttpClient = this.mHttpClient;
    HttpResponse localHttpResponse = paramMusicRequest.sendRequest(localHttpGet, localGoogleHttpClient);
    byte[] arrayOfByte = MusicRequest.readAndReleaseShortResponse(localHttpGet, localHttpResponse, 5242880);
    return JsonUtils.parseFromJsonData(paramClass, arrayOfByte);
  }

  private <T> T executeJsonPost(String paramString, byte[] paramArrayOfByte, Class<T> paramClass)
    throws IOException, InterruptedException
  {
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    AbstractHttpEntity localAbstractHttpEntity = AndroidHttpClient.getCompressedEntity(paramArrayOfByte, localContentResolver);
    localAbstractHttpEntity.setContentType("application/json");
    return executePost(paramString, localAbstractHttpEntity, paramClass);
  }

  private <T> T executeJsonPost(String paramString, byte[] paramArrayOfByte, Class<T> paramClass, Account paramAccount)
    throws IOException, InterruptedException
  {
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    AbstractHttpEntity localAbstractHttpEntity = AndroidHttpClient.getCompressedEntity(paramArrayOfByte, localContentResolver);
    localAbstractHttpEntity.setContentType("application/json");
    return executePost(paramString, localAbstractHttpEntity, paramClass, paramAccount);
  }

  private <T> T executePost(String paramString, HttpEntity paramHttpEntity, Class<T> paramClass)
    throws IOException, InterruptedException
  {
    Object localObject1 = new Object();
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this.mContext, localObject1);
    try
    {
      Context localContext = this.mContext;
      MusicRequest localMusicRequest = new MusicRequest(localContext, localMusicPreferences);
      Object localObject2 = executePost(paramString, paramHttpEntity, paramClass, localMusicRequest);
      Object localObject3 = localObject2;
      return localObject3;
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(localObject1);
    }
  }

  private <T> T executePost(String paramString, HttpEntity paramHttpEntity, Class<T> paramClass, Account paramAccount)
    throws IOException, InterruptedException
  {
    Object localObject1 = new Object();
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this.mContext, localObject1);
    try
    {
      Context localContext = this.mContext;
      MusicRequest localMusicRequest = new MusicRequest(localContext, localMusicPreferences, paramAccount);
      Object localObject2 = executePost(paramString, paramHttpEntity, paramClass, localMusicRequest);
      Object localObject3 = localObject2;
      return localObject3;
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(localObject1);
    }
  }

  private <T> T executePost(String paramString, HttpEntity paramHttpEntity, Class<T> paramClass, MusicRequest paramMusicRequest)
    throws IOException, InterruptedException
  {
    String str = paramString.toString();
    HttpPost localHttpPost = new HttpPost(str);
    localHttpPost.setEntity(paramHttpEntity);
    GoogleHttpClient localGoogleHttpClient = this.mHttpClient;
    HttpResponse localHttpResponse = paramMusicRequest.sendRequest(localHttpPost, localGoogleHttpClient);
    byte[] arrayOfByte = MusicRequest.readAndReleaseShortResponse(localHttpPost, localHttpResponse, 5242880);
    return JsonUtils.parseFromJsonData(paramClass, arrayOfByte);
  }

  public RadioEditStationsResponse createRadioStation(SyncableRadioStation paramSyncableRadioStation, boolean paramBoolean, int paramInt1, int paramInt2)
    throws InterruptedException, IOException
  {
    if (paramSyncableRadioStation == null)
      throw new IllegalArgumentException("Missing radio station");
    if (paramInt1 > 100)
    {
      String str1 = "Too many entries requested: " + paramInt1;
      throw new IllegalArgumentException(str1);
    }
    MusicUrl localMusicUrl = MusicUrl.forRadioEditStations();
    byte[] arrayOfByte = RadioEditStationsRequest.serialize(paramSyncableRadioStation, paramBoolean, paramInt1, paramInt2);
    String str2 = localMusicUrl.toString();
    return (RadioEditStationsResponse)executeJsonPost(str2, arrayOfByte, RadioEditStationsResponse.class);
  }

  public TrackFeed getEphemeralTopTracks(int paramInt1, String paramString, int paramInt2)
    throws InterruptedException, IOException
  {
    FeedAsPostRequest localFeedAsPostRequest = new FeedAsPostRequest();
    if (paramString != null)
      localFeedAsPostRequest.mStartToken = paramString;
    localFeedAsPostRequest.mMaxResults = paramInt1;
    byte[] arrayOfByte = localFeedAsPostRequest.serializeAsJson();
    String str = MusicUrl.forEphemeralTop(paramInt2).toString();
    return (TrackFeed)executeJsonPost(str, arrayOfByte, TrackFeed.class);
  }

  public MusicGenresResponseJson getGenres(String paramString)
    throws InterruptedException, IOException
  {
    String str = MusicUrl.forGenres(paramString).toString();
    return (MusicGenresResponseJson)executeGet(str, MusicGenresResponseJson.class);
  }

  public RadioFeedResponse getLuckyRadioFeed(int paramInt1, List<MixTrackId> paramList, int paramInt2)
    throws InterruptedException, IOException
  {
    if (paramInt1 > 100)
    {
      String str1 = "Too many entries requested: " + paramInt1;
      throw new IllegalArgumentException(str1);
    }
    MusicUrl localMusicUrl = MusicUrl.forRadioStationFeed();
    byte[] arrayOfByte = RadioFeedRequest.serializeForLuckyRadio(paramInt1, paramList, paramInt2);
    String str2 = localMusicUrl.toString();
    return (RadioFeedResponse)executeJsonPost(str2, arrayOfByte, RadioFeedResponse.class);
  }

  public RadioFeedResponse getMixesFeed(int paramInt1, int paramInt2, int paramInt3)
    throws InterruptedException, IOException
  {
    if (paramInt1 > 100)
    {
      String str1 = "Too many seeds requested: " + paramInt1;
      throw new IllegalArgumentException(str1);
    }
    if (paramInt2 > 100)
    {
      String str2 = "Too many entries requested: " + paramInt2;
      throw new IllegalArgumentException(str2);
    }
    MusicUrl localMusicUrl = MusicUrl.forRadioStationFeed();
    byte[] arrayOfByte = RadioFeedRequest.serialize(paramInt1, paramInt2, paramInt3);
    String str3 = localMusicUrl.toString();
    return (RadioFeedResponse)executeJsonPost(str3, arrayOfByte, RadioFeedResponse.class);
  }

  public AlbumJson getNautilusAlbum(String paramString)
    throws InterruptedException, IOException
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("Nautilus id must be specified.");
    String str = MusicUrl.forNautilusAlbum(paramString).toString();
    return (AlbumJson)executeGet(str, AlbumJson.class);
  }

  public ArtistJson getNautilusArtist(String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
    throws InterruptedException, IOException
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("Nautilus id must be specified.");
    if ((paramInt1 < 0) || (paramInt1 > 100) || (paramInt2 < 0) || (paramInt2 > 100))
      throw new IllegalArgumentException("Invalid top tracks or related artists params");
    String str = MusicUrl.forNautilusArtist(paramString, paramInt1, paramInt2, paramBoolean).toString();
    return (ArtistJson)executeGet(str, ArtistJson.class);
  }

  public Track getNautilusTrack(String paramString)
    throws InterruptedException, IOException
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("Nautilus id must be specified.");
    String str = MusicUrl.forNautilusTrack(paramString).toString();
    return (Track)executeGet(str, Track.class);
  }

  public NewReleasesResponseJson getNewReleases(int paramInt)
    throws InterruptedException, IOException
  {
    if ((paramInt < 1) || (paramInt > 100))
    {
      String str1 = "Invalid max entities: " + paramInt;
      throw new IllegalArgumentException(str1);
    }
    String str2 = MusicUrl.forNewReleases(paramInt).toString();
    return (NewReleasesResponseJson)executeGet(str2, NewReleasesResponseJson.class);
  }

  public OffersResponseJson getOffersForAccount(Account paramAccount)
    throws InterruptedException, IOException
  {
    byte[] arrayOfByte = OffersRequestJson.serialize(null);
    String str = MusicUrl.forOffers().toString();
    return (OffersResponseJson)executeJsonPost(str, arrayOfByte, OffersResponseJson.class, paramAccount);
  }

  public SyncablePlaylist getPlaylist(String paramString)
    throws InterruptedException, IOException
  {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("Playlist id must be specified.");
    String str = MusicUrl.forPlaylist(paramString).toString();
    return (SyncablePlaylist)executeGet(str, SyncablePlaylist.class);
  }

  public RadioFeedResponse getRadioFeed(String paramString, int paramInt1, List<MixTrackId> paramList, int paramInt2)
    throws InterruptedException, IOException
  {
    if (paramString == null)
      throw new IllegalArgumentException("Null radio station id");
    if (paramInt1 > 100)
    {
      String str1 = "Too many entries requested: " + paramInt1;
      throw new IllegalArgumentException(str1);
    }
    MusicUrl localMusicUrl = MusicUrl.forRadioStationFeed();
    byte[] arrayOfByte = RadioFeedRequest.serialize(paramString, paramInt1, paramList, paramInt2);
    String str2 = localMusicUrl.toString();
    return (RadioFeedResponse)executeJsonPost(str2, arrayOfByte, RadioFeedResponse.class);
  }

  public GetSharedPlaylistEntriesResponseJson getSharedEntries(String paramString1, int paramInt, String paramString2, long paramLong)
    throws InterruptedException, IOException
  {
    MusicUrl localMusicUrl = MusicUrl.forGetSharedPlaylistEntries(paramInt, paramString2, paramLong);
    byte[] arrayOfByte = GetSharedPlaylistEntriesRequestJson.serialize(paramString1, paramInt, paramString2, paramLong);
    String str = localMusicUrl.toString();
    return (GetSharedPlaylistEntriesResponseJson)executeJsonPost(str, arrayOfByte, GetSharedPlaylistEntriesResponseJson.class);
  }

  public TabJson getTab(MusicUrl.ExploreTabType paramExploreTabType, int paramInt, String paramString)
    throws InterruptedException, IOException
  {
    if ((paramInt < 1) || (paramInt > 100))
    {
      String str1 = "Invalid max entities: " + paramInt;
      throw new IllegalArgumentException(str1);
    }
    MusicUrl.ExploreTabType localExploreTabType1 = MusicUrl.ExploreTabType.RECOMMENDED;
    if (paramExploreTabType != localExploreTabType1)
    {
      MusicUrl.ExploreTabType localExploreTabType2 = MusicUrl.ExploreTabType.GENRES;
      if (paramExploreTabType != localExploreTabType2);
    }
    else if (!TextUtils.isEmpty(paramString))
    {
      throw new IllegalArgumentException("Scoping by genre is not allowed for tab typeRECOMMENDED and GENRES");
    }
    String str2 = MusicUrl.forTab(paramExploreTabType, paramInt, paramString).toString();
    TabsResponseJson localTabsResponseJson = (TabsResponseJson)executeGet(str2, TabsResponseJson.class);
    if ((localTabsResponseJson != null) && (localTabsResponseJson.mTabs != null) && (!localTabsResponseJson.mTabs.isEmpty()));
    for (TabJson localTabJson = (TabJson)localTabsResponseJson.mTabs.get(0); ; localTabJson = null)
      return localTabJson;
  }

  public SearchClientResponseJson search(String paramString1, String paramString2, int paramInt)
    throws InterruptedException, IOException
  {
    if (TextUtils.isEmpty(paramString1))
      throw new IllegalArgumentException("Query string must be specified.");
    if ((paramInt < 1) || (paramInt > 100))
    {
      String str1 = "Invalid max results: " + paramInt;
      throw new IllegalArgumentException(str1);
    }
    Uri.Builder localBuilder1 = Uri.parse("https://www.googleapis.com/sj/v1.1/query").buildUpon();
    Uri.Builder localBuilder2 = localBuilder1.appendQueryParameter("q", paramString1);
    if (!TextUtils.isEmpty(paramString2))
      Uri.Builder localBuilder3 = localBuilder1.appendQueryParameter("start-token", paramString2);
    String str2 = DEFAULT_SEARCH_CONTENT_TYPE;
    Uri.Builder localBuilder4 = localBuilder1.appendQueryParameter("ct", str2);
    String str3 = paramInt + "";
    Uri.Builder localBuilder5 = localBuilder1.appendQueryParameter("max-results", str3);
    String str4 = localBuilder1.build().toString();
    return (SearchClientResponseJson)executeGet(str4, SearchClientResponseJson.class);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.MusicCloudImpl
 * JD-Core Version:    0.6.2
 */