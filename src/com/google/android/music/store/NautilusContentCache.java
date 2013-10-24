package com.google.android.music.store;

import android.content.ContentResolver;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.google.android.gsf.Gservices;
import com.google.android.music.cloudclient.AlbumJson;
import com.google.android.music.cloudclient.ArtistJson;
import com.google.android.music.cloudclient.ExploreEntityGroupJson;
import com.google.android.music.cloudclient.ExploreEntityJson;
import com.google.android.music.cloudclient.MusicGenresResponseJson;
import com.google.android.music.cloudclient.SearchClientResponseJson;
import com.google.android.music.cloudclient.SearchClientResponseJson.SearchClientResponseEntry;
import com.google.android.music.cloudclient.TabJson;
import com.google.android.music.sync.api.MusicUrl.ExploreTabType;
import com.google.android.music.sync.google.model.SyncablePlaylistEntry;
import com.google.android.music.sync.google.model.Track;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LruCache;
import java.util.Iterator;
import java.util.List;

public class NautilusContentCache
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.CONTENT_PROVIDER);
  private static NautilusContentCache sInstance;
  private LruCache<String, Pair<AlbumJson, Long>> mAlbumCache;
  private LruCache<String, Pair<ArtistJson, Long>> mArtistCache;
  private final Context mContext;
  private LruCache<String, Pair<Object, Long>> mGenericCache;
  private LruCache<String, Pair<Track, Long>> mTrackCache;

  private NautilusContentCache(Context paramContext)
  {
    this.mContext = paramContext;
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    int i = Gservices.getInt(localContentResolver, "music_memory_cache_capacity", 200);
    int j = Gservices.getInt(localContentResolver, "music_memory_cache_capacity_small", 20);
    LruCache localLruCache1 = new LruCache(i);
    this.mArtistCache = localLruCache1;
    LruCache localLruCache2 = new LruCache(i);
    this.mAlbumCache = localLruCache2;
    LruCache localLruCache3 = new LruCache(i);
    this.mTrackCache = localLruCache3;
    LruCache localLruCache4 = new LruCache(j);
    this.mGenericCache = localLruCache4;
  }

  private String getArtistArtUrlCacheKey(String paramString)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "artistArtUrl";
    arrayOfObject[1] = paramString;
    return String.format("%s:%s", arrayOfObject);
  }

  private String getExploreCacheKey(MusicUrl.ExploreTabType paramExploreTabType, String paramString)
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = "explore";
    arrayOfObject[1] = paramExploreTabType;
    arrayOfObject[2] = paramString;
    return String.format("%s:%s:%s", arrayOfObject);
  }

  private String getGenreCacheKey(String paramString)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "genre";
    if (paramString != null);
    while (true)
    {
      arrayOfObject[1] = paramString;
      return String.format("%s:%s", arrayOfObject);
      paramString = "ROOT";
    }
  }

  /** @deprecated */
  public static NautilusContentCache getInstance(Context paramContext)
  {
    try
    {
      if (sInstance == null)
        sInstance = new NautilusContentCache(paramContext);
      NautilusContentCache localNautilusContentCache = sInstance;
      return localNautilusContentCache;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private <T> T getObject(LruCache<String, Pair<T, Long>> paramLruCache, String paramString, long paramLong)
  {
    while (true)
    {
      try
      {
        Pair localPair = (Pair)paramLruCache.get(paramString);
        if (localPair == null)
        {
          localObject1 = null;
          return localObject1;
        }
        if (paramLong != 65535L)
        {
          long l1 = ((Long)localPair.second).longValue() + paramLong;
          long l2 = System.currentTimeMillis();
          if (l1 <= l2);
        }
        else if (localPair.first != null)
        {
          localObject1 = localPair.first;
          continue;
        }
      }
      finally
      {
      }
      paramLruCache.remove(paramString);
      Object localObject1 = null;
    }
  }

  private String getSearchCacheKey(String paramString)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "search";
    arrayOfObject[1] = paramString;
    return String.format("%s:%s", arrayOfObject);
  }

  private String getSharedPlaylistEntriesCacheKey(String paramString)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "shared_playlist";
    arrayOfObject[1] = paramString;
    return String.format("%s:%s", arrayOfObject);
  }

  private boolean isCacheable(AlbumJson paramAlbumJson)
  {
    if ((paramAlbumJson != null) && (paramAlbumJson.mAlbumId != null) && (paramAlbumJson.mTracks != null));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private boolean isCacheable(ArtistJson paramArtistJson)
  {
    if ((paramArtistJson != null) && (paramArtistJson.mArtistId != null) && (paramArtistJson.mAlbums != null));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private boolean isCacheable(Track paramTrack)
  {
    if ((paramTrack != null) && (paramTrack.mNautilusId != null));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private void putObject(String paramString, Object paramObject)
  {
    if ((TextUtils.isEmpty(paramString)) || (paramObject == null))
    {
      int i = Log.w("NautilusContentCache", "Invalid arguments");
      return;
    }
    synchronized (this.mGenericCache)
    {
      LruCache localLruCache2 = this.mGenericCache;
      Long localLong = Long.valueOf(System.currentTimeMillis());
      Pair localPair = new Pair(paramObject, localLong);
      Object localObject1 = localLruCache2.put(paramString, localPair);
      return;
    }
  }

  private void removeObject(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      int i = Log.w("NautilusContentCache", "Invalid argument");
      return;
    }
    synchronized (this.mGenericCache)
    {
      this.mGenericCache.remove(paramString);
      return;
    }
  }

  public AlbumJson getAlbum(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      String str = "Invalid nautilus id: " + paramString;
      throw new IllegalArgumentException(str);
    }
    int i = Gservices.getInt(this.mContext.getContentResolver(), "music_album_memory_cache_ttl_sec", 86400);
    LruCache localLruCache = this.mAlbumCache;
    long l = i * 1000;
    return (AlbumJson)getObject(localLruCache, paramString, l);
  }

  public ArtistJson getArtist(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      String str = "Invalid nautilus id: " + paramString;
      throw new IllegalArgumentException(str);
    }
    int i = Gservices.getInt(this.mContext.getContentResolver(), "music_artist_memory_cache_ttl_sec", 86400);
    LruCache localLruCache = this.mArtistCache;
    long l = i * 1000;
    return (ArtistJson)getObject(localLruCache, paramString, l);
  }

  public String getArtistArtUrl(String paramString)
  {
    LruCache localLruCache = this.mGenericCache;
    String str = getArtistArtUrlCacheKey(paramString);
    return (String)getObject(localLruCache, str, 65535L);
  }

  public TabJson getExploreResponse(MusicUrl.ExploreTabType paramExploreTabType, String paramString)
  {
    int i = Gservices.getInt(this.mContext.getContentResolver(), "music_explore_memory_cache_ttl_sec", 900);
    LruCache localLruCache = this.mGenericCache;
    String str = getExploreCacheKey(paramExploreTabType, paramString);
    long l = i * 1000;
    return (TabJson)getObject(localLruCache, str, l);
  }

  public MusicGenresResponseJson getMusicGenresResponse(String paramString)
  {
    int i = Gservices.getInt(this.mContext.getContentResolver(), "music_explore_memory_cache_ttl_sec", 900);
    LruCache localLruCache = this.mGenericCache;
    String str = getGenreCacheKey(paramString);
    long l = i * 1000;
    return (MusicGenresResponseJson)getObject(localLruCache, str, l);
  }

  public SearchClientResponseJson getSearchResponse(String paramString)
  {
    int i = Gservices.getInt(this.mContext.getContentResolver(), "music_search_memory_cache_ttl_sec", 900);
    LruCache localLruCache = this.mGenericCache;
    String str = getSearchCacheKey(paramString);
    long l = i * 1000;
    return (SearchClientResponseJson)getObject(localLruCache, str, l);
  }

  public List<SyncablePlaylistEntry> getSharedPlaylistEntriesResponse(String paramString)
  {
    int i = Gservices.getInt(this.mContext.getContentResolver(), "music_shared_playlsits_memory_cache_ttl_sec", 900);
    LruCache localLruCache = this.mGenericCache;
    String str = getSharedPlaylistEntriesCacheKey(paramString);
    long l = i * 1000;
    return (List)getObject(localLruCache, str, l);
  }

  public Track getTrack(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
    {
      String str = "Invalid nautilus id: " + paramString;
      throw new IllegalArgumentException(str);
    }
    int i = Gservices.getInt(this.mContext.getContentResolver(), "music_track_memory_cache_ttl_sec", 86400);
    LruCache localLruCache = this.mTrackCache;
    long l = i * 1000;
    return (Track)getObject(localLruCache, paramString, l);
  }

  public void putAlbum(AlbumJson paramAlbumJson)
  {
    if (!isCacheable(paramAlbumJson))
    {
      if (!LOGV)
        return;
      String str1 = "Not cacheable: " + paramAlbumJson;
      int i = Log.d("NautilusContentCache", str1);
      return;
    }
    synchronized (this.mAlbumCache)
    {
      LruCache localLruCache2 = this.mAlbumCache;
      String str2 = paramAlbumJson.mAlbumId;
      Long localLong = Long.valueOf(System.currentTimeMillis());
      Pair localPair = new Pair(paramAlbumJson, localLong);
      Object localObject1 = localLruCache2.put(str2, localPair);
      if (paramAlbumJson.mTracks == null)
        return;
      Iterator localIterator = paramAlbumJson.mTracks.iterator();
      if (!localIterator.hasNext())
        return;
      Track localTrack = (Track)localIterator.next();
      putTrack(localTrack);
    }
  }

  public void putArtist(ArtistJson paramArtistJson)
  {
    if (!isCacheable(paramArtistJson))
    {
      if (!LOGV)
        return;
      String str1 = "Not cacheable: " + paramArtistJson;
      int i = Log.d("NautilusContentCache", str1);
      return;
    }
    synchronized (this.mArtistCache)
    {
      LruCache localLruCache2 = this.mArtistCache;
      String str2 = paramArtistJson.mArtistId;
      Long localLong = Long.valueOf(System.currentTimeMillis());
      Pair localPair = new Pair(paramArtistJson, localLong);
      Object localObject1 = localLruCache2.put(str2, localPair);
      if (paramArtistJson.mAlbums != null)
      {
        localIterator = paramArtistJson.mAlbums.iterator();
        if (localIterator.hasNext())
        {
          AlbumJson localAlbumJson = (AlbumJson)localIterator.next();
          putAlbum(localAlbumJson);
        }
      }
    }
    if (paramArtistJson.mTopTracks == null)
      return;
    Iterator localIterator = paramArtistJson.mTopTracks.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      Track localTrack = (Track)localIterator.next();
      putTrack(localTrack);
    }
  }

  public void putArtistArtUrl(String paramString1, String paramString2)
  {
    if ((TextUtils.isEmpty(paramString1)) || (TextUtils.isEmpty(paramString2)))
    {
      if (!LOGV)
        return;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramString1;
      arrayOfObject[1] = paramString2;
      String str1 = String.format("Not cacheable. id=%s, url=%s", arrayOfObject);
      int i = Log.d("NautilusContentCache", str1);
      return;
    }
    String str2 = getArtistArtUrlCacheKey(paramString1);
    putObject(str2, paramString2);
  }

  public void putExploreResponse(MusicUrl.ExploreTabType paramExploreTabType, String paramString, TabJson paramTabJson)
  {
    if ((paramTabJson == null) || (paramTabJson.mGroups == null))
    {
      int i = Log.w("NautilusContentCache", "Trying to insert a null explore response");
      return;
    }
    String str = getExploreCacheKey(paramExploreTabType, paramString);
    putObject(str, paramTabJson);
    Iterator localIterator1 = paramTabJson.mGroups.iterator();
    while (true)
    {
      if (!localIterator1.hasNext())
        return;
      List localList = ((ExploreEntityGroupJson)localIterator1.next()).mEntities;
      if (localList != null)
      {
        Iterator localIterator2 = localList.iterator();
        while (localIterator2.hasNext())
        {
          ExploreEntityJson localExploreEntityJson = (ExploreEntityJson)localIterator2.next();
          if (localExploreEntityJson.mTrack != null)
          {
            Track localTrack = localExploreEntityJson.mTrack;
            putTrack(localTrack);
          }
          else if ((localExploreEntityJson.mPlaylist != null) || (localExploreEntityJson.mAlbum == null));
        }
      }
    }
  }

  public void putMusicGenresResponse(String paramString, MusicGenresResponseJson paramMusicGenresResponseJson)
  {
    if ((paramMusicGenresResponseJson == null) || (paramMusicGenresResponseJson.mGenres == null))
    {
      int i = Log.w("NautilusContentCache", "Trying to insert a null genres response");
      return;
    }
    String str = getGenreCacheKey(paramString);
    putObject(str, paramMusicGenresResponseJson);
  }

  public void putSearchResponse(String paramString, SearchClientResponseJson paramSearchClientResponseJson)
  {
    if ((paramSearchClientResponseJson == null) || (paramSearchClientResponseJson.mEntries == null))
    {
      int i = Log.w("NautilusContentCache", "Trying to insert a null search response");
      return;
    }
    String str = getSearchCacheKey(paramString);
    putObject(str, paramSearchClientResponseJson);
    Iterator localIterator = paramSearchClientResponseJson.mEntries.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      SearchClientResponseJson.SearchClientResponseEntry localSearchClientResponseEntry = (SearchClientResponseJson.SearchClientResponseEntry)localIterator.next();
      if (localSearchClientResponseEntry.mArtist == null)
        if (localSearchClientResponseEntry.mAlbum != null)
        {
          AlbumJson localAlbumJson = localSearchClientResponseEntry.mAlbum;
          putAlbum(localAlbumJson);
        }
        else if (localSearchClientResponseEntry.mTrack != null)
        {
          Track localTrack = localSearchClientResponseEntry.mTrack;
          putTrack(localTrack);
        }
    }
  }

  public void putSharedPlaylistEntriesResponse(String paramString, List<SyncablePlaylistEntry> paramList)
  {
    if ((paramList == null) || (paramList.size() == 0))
    {
      int i = Log.w("NautilusContentCache", "Trying to insert a null shared playlist entries response");
      return;
    }
    String str = getSharedPlaylistEntriesCacheKey(paramString);
    putObject(str, paramList);
  }

  public void putTrack(Track paramTrack)
  {
    if (!isCacheable(paramTrack))
    {
      if (!LOGV)
        return;
      String str1 = "Not cacheable: " + paramTrack;
      int i = Log.d("NautilusContentCache", str1);
      return;
    }
    synchronized (this.mTrackCache)
    {
      LruCache localLruCache2 = this.mTrackCache;
      String str2 = paramTrack.getNormalizedNautilusId();
      Long localLong = Long.valueOf(System.currentTimeMillis());
      Pair localPair = new Pair(paramTrack, localLong);
      Object localObject1 = localLruCache2.put(str2, localPair);
      return;
    }
  }

  public void removeExploreResponse(MusicUrl.ExploreTabType paramExploreTabType, String paramString)
  {
    String str = getExploreCacheKey(paramExploreTabType, paramString);
    removeObject(str);
  }

  public void removeSharedPlaylistEntries(String paramString)
  {
    String str = getSharedPlaylistEntriesCacheKey(paramString);
    removeObject(str);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.NautilusContentCache
 * JD-Core Version:    0.6.2
 */