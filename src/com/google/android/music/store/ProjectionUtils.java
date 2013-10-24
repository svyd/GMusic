package com.google.android.music.store;

import android.text.TextUtils;
import android.util.Log;
import com.google.android.music.cloudclient.AlbumJson;
import com.google.android.music.cloudclient.ArtistJson;
import com.google.android.music.cloudclient.ExploreEntityGroupJson;
import com.google.android.music.cloudclient.ExploreEntityJson;
import com.google.android.music.cloudclient.ImageRefJson;
import com.google.android.music.cloudclient.MusicGenreJson;
import com.google.android.music.cloudclient.TrackJson;
import com.google.android.music.cloudclient.TrackJson.ImageRef;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.sync.google.model.SyncablePlaylist;
import com.google.android.music.sync.google.model.SyncablePlaylistEntry;
import com.google.android.music.sync.google.model.Track;
import com.google.android.music.utils.MusicUtils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectionUtils
{
  private static Map<Class, ColumnMappings> sProjectionMaps = new HashMap();

  static
  {
    ColumnMappings localColumnMappings1 = new ColumnMappings(null);
    localColumnMappings1.putField("Album", "album");
    localColumnMappings1.putField("AlbumArtist", "albumArtist");
    localColumnMappings1.putField("Artist", "artist");
    localColumnMappings1.putField("duration", "durationMillis");
    Integer localInteger1 = Integer.valueOf(0);
    localColumnMappings1.putValue("hasLocal", localInteger1);
    Integer localInteger2 = Integer.valueOf(1);
    localColumnMappings1.putValue("hasRemote", localInteger2);
    localColumnMappings1.putField("searchName", "title");
    localColumnMappings1.putField("searchSortName", "title");
    localColumnMappings1.putField("year", "year");
    Integer localInteger3 = Integer.valueOf(8);
    localColumnMappings1.putValue("searchType", localInteger3);
    localColumnMappings1.putField("album", "album");
    localColumnMappings1.putField("AlbumArtist", "albumArtist");
    localColumnMappings1.putField("CanonicalAlbum", "album");
    localColumnMappings1.putField("artistSort", "artist");
    localColumnMappings1.putField("composer", "composer");
    localColumnMappings1.putField("duration", "durationMillis");
    localColumnMappings1.putField("Genre", "genre");
    localColumnMappings1.putField("title", "title");
    localColumnMappings1.putField("CanonicalName", "title");
    localColumnMappings1.putField("artist", "artist");
    localColumnMappings1.putField("track", "trackNumber");
    localColumnMappings1.putField("year", "year");
    localColumnMappings1.putField("StoreAlbumId", "albumId");
    localColumnMappings1.putField("StoreId", "storeId");
    Integer localInteger4 = Integer.valueOf(0);
    localColumnMappings1.putValue("isAllPersistentNautilus", localInteger4);
    localColumnMappings1.putField("Size", "estimatedSize");
    localColumnMappings1.putField("suggest_text_1", "title");
    localColumnMappings1.putField("suggest_text_2", "album");
    Object localObject1 = sProjectionMaps.put(Track.class, localColumnMappings1);
    ColumnMappings localColumnMappings2 = new ColumnMappings(null);
    localColumnMappings2.putField("Album", "name");
    localColumnMappings2.putField("AlbumArtist", "albumArtist");
    localColumnMappings2.putField("AlbumId", "albumId");
    localColumnMappings2.putField("Artist", "artist");
    Integer localInteger5 = Integer.valueOf(0);
    localColumnMappings2.putValue("hasLocal", localInteger5);
    Integer localInteger6 = Integer.valueOf(1);
    localColumnMappings2.putValue("hasRemote", localInteger6);
    localColumnMappings2.putField("searchName", "name");
    localColumnMappings2.putField("searchSortName", "name");
    Integer localInteger7 = Integer.valueOf(7);
    localColumnMappings2.putValue("searchType", localInteger7);
    localColumnMappings2.putField("album_name", "name");
    localColumnMappings2.putField("album_art", "albumArtRef");
    localColumnMappings2.putField("album_id", "albumId");
    localColumnMappings2.putField("album_sort", "name");
    localColumnMappings2.putField("album_artist", "albumArtist");
    localColumnMappings2.putField("album_artist_sort", "albumArtist");
    localColumnMappings2.putField("StoreAlbumId", "albumId");
    Integer localInteger8 = Integer.valueOf(0);
    localColumnMappings2.putValue("hasPersistNautilus", localInteger8);
    localColumnMappings2.putField("album_year", "year");
    localColumnMappings2.putField("suggest_text_1", "name");
    localColumnMappings2.putField("suggest_text_2", "albumArtist");
    localColumnMappings2.putField("suggest_icon_1", "albumArtRef");
    localColumnMappings2.putField("artworkUrl", "albumArtRef");
    Object localObject2 = sProjectionMaps.put(AlbumJson.class, localColumnMappings2);
    ColumnMappings localColumnMappings3 = new ColumnMappings(null);
    localColumnMappings3.putField("AlbumArtist", "name");
    localColumnMappings3.putField("AlbumArtistId", "artistId");
    localColumnMappings3.putField("Artist", "name");
    Integer localInteger9 = Integer.valueOf(0);
    localColumnMappings3.putValue("hasLocal", localInteger9);
    Integer localInteger10 = Integer.valueOf(1);
    localColumnMappings3.putValue("hasRemote", localInteger10);
    localColumnMappings3.putField("searchName", "name");
    localColumnMappings3.putField("searchSortName", "name");
    Integer localInteger11 = Integer.valueOf(6);
    localColumnMappings3.putValue("searchType", localInteger11);
    localColumnMappings3.putField("artist", "name");
    localColumnMappings3.putField("artistSort", "name");
    localColumnMappings3.putField("ArtistMetajamId", "artistId");
    localColumnMappings3.putField("suggest_text_1", "name");
    localColumnMappings3.putField("suggest_icon_1", "artistArtRef");
    localColumnMappings3.putField("artworkUrl", "artistArtRef");
    Object localObject3 = sProjectionMaps.put(ArtistJson.class, localColumnMappings3);
    ColumnMappings localColumnMappings4 = new ColumnMappings(null);
    localColumnMappings4.putField("_id", "id");
    localColumnMappings4.putField("title", "title");
    localColumnMappings4.putField("description", "description");
    Object localObject4 = sProjectionMaps.put(ExploreEntityGroupJson.class, localColumnMappings4);
    ColumnMappings localColumnMappings5 = new ColumnMappings(null);
    localColumnMappings5.putField("genreServerId", "id");
    localColumnMappings5.putField("name", "name");
    localColumnMappings5.putField("parentGenreId", "parentId");
    Integer localInteger12 = Integer.valueOf(9);
    localColumnMappings5.putValue("searchType", localInteger12);
    Object localObject5 = sProjectionMaps.put(MusicGenreJson.class, localColumnMappings5);
    ColumnMappings localColumnMappings6 = new ColumnMappings(null);
    String str1 = MusicContent.SharedWithMePlaylist.CREATION_TIMESTAMP;
    localColumnMappings6.putField(str1, "creationTimestamp");
    String str2 = MusicContent.SharedWithMePlaylist.LAST_MODIFIED_TIMESTAMP;
    localColumnMappings6.putField(str2, "lastModifiedTimestamp");
    String str3 = MusicContent.SharedWithMePlaylist.NAME;
    localColumnMappings6.putField(str3, "name");
    String str4 = MusicContent.SharedWithMePlaylist.SHARE_TOKEN;
    localColumnMappings6.putField(str4, "shareToken");
    String str5 = MusicContent.SharedWithMePlaylist.OWNER_NAME;
    localColumnMappings6.putField(str5, "ownerName");
    String str6 = MusicContent.SharedWithMePlaylist.DESCRIPTION;
    localColumnMappings6.putField(str6, "description");
    String str7 = MusicContent.SharedWithMePlaylist.OWNER_PROFILE_PHOTO_URL;
    localColumnMappings6.putField(str7, "ownerProfilePhotoUrl");
    Object localObject6 = sProjectionMaps.put(SyncablePlaylist.class, localColumnMappings6);
  }

  private static int getRandomId()
  {
    return -(int)(Math.random() * 10000.0D) + -100;
  }

  public static boolean isFauxNautilusId(long paramLong)
  {
    if (paramLong <= 65436L);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static boolean isHasDifferentArtistProjection(String[] paramArrayOfString)
  {
    boolean bool = true;
    if ((paramArrayOfString != null) && (paramArrayOfString.length == 1) && (paramArrayOfString[0].equals("HasDifferentTrackArtists")));
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  public static Object[] project(AlbumJson paramAlbumJson, String[] paramArrayOfString)
  {
    Object[] arrayOfObject;
    if (paramAlbumJson == null)
      arrayOfObject = null;
    ColumnMappings localColumnMappings;
    int i;
    int j;
    do
    {
      return arrayOfObject;
      Map localMap = sProjectionMaps;
      Class localClass = paramAlbumJson.getClass();
      localColumnMappings = (ColumnMappings)localMap.get(localClass);
      arrayOfObject = new Object[paramArrayOfString.length];
      i = 0;
      j = paramArrayOfString.length;
    }
    while (i >= j);
    String str1 = paramArrayOfString[i];
    if (localColumnMappings.hasColumn(str1))
    {
      String str2 = paramArrayOfString[i];
      Object localObject1 = localColumnMappings.evaluate(paramAlbumJson, str2);
      arrayOfObject[i] = localObject1;
    }
    while (true)
    {
      i += 1;
      break;
      if ((paramArrayOfString[i].equals("itemCount")) || (paramArrayOfString[i].equals("SongCount")))
      {
        if (paramAlbumJson.mTracks != null)
        {
          Integer localInteger = Integer.valueOf(paramAlbumJson.mTracks.size());
          arrayOfObject[i] = localInteger;
        }
      }
      else if ((paramArrayOfString[i].equals("ArtistMetajamId")) && (paramAlbumJson.mArtistId != null) && (!paramAlbumJson.mArtistId.isEmpty()))
      {
        Object localObject2 = paramAlbumJson.mArtistId.get(0);
        arrayOfObject[i] = localObject2;
      }
    }
  }

  public static Object[] project(ArtistJson paramArtistJson, String[] paramArrayOfString)
  {
    Object[] arrayOfObject;
    if (paramArtistJson == null)
      arrayOfObject = null;
    ColumnMappings localColumnMappings;
    int i;
    int j;
    do
    {
      return arrayOfObject;
      Map localMap = sProjectionMaps;
      Class localClass = paramArtistJson.getClass();
      localColumnMappings = (ColumnMappings)localMap.get(localClass);
      arrayOfObject = new Object[paramArrayOfString.length];
      i = 0;
      j = paramArrayOfString.length;
    }
    while (i >= j);
    String str1 = paramArrayOfString[i];
    if (localColumnMappings.hasColumn(str1))
    {
      String str2 = paramArrayOfString[i];
      Object localObject = localColumnMappings.evaluate(paramArtistJson, str2);
      arrayOfObject[i] = localObject;
    }
    while (true)
    {
      i += 1;
      break;
      if ((paramArrayOfString[i].equals("albumCount")) && (paramArtistJson.mAlbums != null))
      {
        Integer localInteger = Integer.valueOf(paramArtistJson.mAlbums.size());
        arrayOfObject[i] = localInteger;
      }
    }
  }

  public static Object[] project(ExploreEntityGroupJson paramExploreEntityGroupJson, String[] paramArrayOfString)
  {
    Object[] arrayOfObject;
    if (paramExploreEntityGroupJson == null)
      arrayOfObject = null;
    List localList;
    ColumnMappings localColumnMappings;
    int i;
    int j;
    do
    {
      return arrayOfObject;
      localList = paramExploreEntityGroupJson.mEntities;
      Map localMap = sProjectionMaps;
      Class localClass = paramExploreEntityGroupJson.getClass();
      localColumnMappings = (ColumnMappings)localMap.get(localClass);
      arrayOfObject = new Object[paramArrayOfString.length];
      i = 0;
      j = paramArrayOfString.length;
    }
    while (i >= j);
    String str1 = paramArrayOfString[i];
    if (localColumnMappings.hasColumn(str1))
    {
      String str2 = paramArrayOfString[i];
      Object localObject = localColumnMappings.evaluate(paramExploreEntityGroupJson, str2);
      arrayOfObject[i] = localObject;
    }
    while (true)
    {
      i += 1;
      break;
      if (paramArrayOfString[i].equals("size"))
      {
        if (localList == null);
        for (int k = 0; ; k = localList.size())
        {
          Integer localInteger1 = Integer.valueOf(k);
          arrayOfObject[i] = localInteger1;
          break;
        }
      }
      if (paramArrayOfString[i].equals("groupType"))
        if ((localList != null) && (!localList.isEmpty()))
        {
          ExploreEntityJson localExploreEntityJson = (ExploreEntityJson)localList.get(0);
          if (localExploreEntityJson.mTrack != null)
          {
            Integer localInteger2 = Integer.valueOf(0);
            arrayOfObject[i] = localInteger2;
          }
          else if (localExploreEntityJson.mAlbum != null)
          {
            Integer localInteger3 = Integer.valueOf(1);
            arrayOfObject[i] = localInteger3;
          }
          else if (localExploreEntityJson.mPlaylist != null)
          {
            Integer localInteger4 = Integer.valueOf(3);
            arrayOfObject[i] = localInteger4;
          }
          else
          {
            String str3 = "Could not determine the entity type: " + localExploreEntityJson;
            int m = Log.w("ProjectionUtils", str3);
          }
        }
        else
        {
          int n = Log.w("ProjectionUtils", "Could not determine the entity type. Entity group empty.");
        }
    }
  }

  public static Object[] project(MusicGenreJson paramMusicGenreJson, String[] paramArrayOfString)
  {
    Object[] arrayOfObject;
    if (paramMusicGenreJson == null)
      arrayOfObject = null;
    ColumnMappings localColumnMappings;
    int i;
    int j;
    do
    {
      return arrayOfObject;
      Map localMap = sProjectionMaps;
      Class localClass = paramMusicGenreJson.getClass();
      localColumnMappings = (ColumnMappings)localMap.get(localClass);
      arrayOfObject = new Object[paramArrayOfString.length];
      i = 0;
      j = paramArrayOfString.length;
    }
    while (i >= j);
    String str1 = paramArrayOfString[i];
    if (localColumnMappings.hasColumn(str1))
    {
      String str2 = paramArrayOfString[i];
      Object localObject = localColumnMappings.evaluate(paramMusicGenreJson, str2);
      arrayOfObject[i] = localObject;
    }
    while (true)
    {
      i += 1;
      break;
      if (paramArrayOfString[i].equals("_id"))
      {
        Long localLong = Long.valueOf(Store.generateId(paramMusicGenreJson.mId));
        arrayOfObject[i] = localLong;
      }
      else
      {
        if (paramArrayOfString[i].equals("subgenreCount"))
        {
          if (paramMusicGenreJson.mChildren != null);
          for (int k = paramMusicGenreJson.mChildren.size(); ; k = 0)
          {
            Integer localInteger = Integer.valueOf(k);
            arrayOfObject[i] = localInteger;
            break;
          }
        }
        if ((paramArrayOfString[i].equals("genreArtUris")) && (paramMusicGenreJson.mImages != null) && (!paramMusicGenreJson.mImages.isEmpty()))
        {
          String[] arrayOfString = new String[paramMusicGenreJson.mImages.size()];
          int m = 0;
          while (true)
          {
            int n = arrayOfString.length;
            if (m >= n)
              break;
            String str3 = ((ImageRefJson)paramMusicGenreJson.mImages.get(m)).mUrl;
            arrayOfString[m] = str3;
            m += 1;
          }
          String str4 = MusicUtils.encodeStringArray(arrayOfString);
          arrayOfObject[i] = str4;
        }
      }
    }
  }

  public static Object[] project(TrackJson paramTrackJson, String[] paramArrayOfString)
  {
    Object[] arrayOfObject;
    if (paramTrackJson == null)
      arrayOfObject = null;
    ColumnMappings localColumnMappings;
    int i;
    int j;
    do
    {
      return arrayOfObject;
      Map localMap = sProjectionMaps;
      Class localClass = paramTrackJson.getClass();
      localColumnMappings = (ColumnMappings)localMap.get(localClass);
      arrayOfObject = new Object[paramArrayOfString.length];
      i = 0;
      j = paramArrayOfString.length;
    }
    while (i >= j);
    String str1 = paramArrayOfString[i];
    if (localColumnMappings.hasColumn(str1))
    {
      String str2 = paramArrayOfString[i];
      Object localObject1 = localColumnMappings.evaluate(paramTrackJson, str2);
      arrayOfObject[i] = localObject1;
    }
    while (true)
    {
      i += 1;
      break;
      if ((paramArrayOfString[i].equals("Nid")) || (paramArrayOfString[i].equals("SourceId")))
      {
        String str3 = paramTrackJson.getNormalizedNautilusId();
        arrayOfObject[i] = str3;
      }
      else if (paramArrayOfString[i].equals("ArtistMetajamId"))
      {
        if ((paramTrackJson.mArtistId != null) && (!paramTrackJson.mArtistId.isEmpty()))
        {
          Object localObject2 = paramTrackJson.mArtistId.get(0);
          arrayOfObject[i] = localObject2;
        }
      }
      else if (paramArrayOfString[i].equals("Domain"))
      {
        Integer localInteger = Integer.valueOf(paramTrackJson.getDomain().getDBValue());
        arrayOfObject[i] = localInteger;
      }
      else if ((paramArrayOfString[i].equals("artworkUrl")) || (paramArrayOfString[i].equals("suggest_icon_1")))
      {
        if ((paramTrackJson.mAlbumArtRef != null) && (paramTrackJson.mAlbumArtRef.size() > 0))
        {
          String str4 = ((TrackJson.ImageRef)paramTrackJson.mAlbumArtRef.get(0)).mUrl;
          arrayOfObject[i] = str4;
        }
      }
      else if ((paramArrayOfString[i].equals("ArtistArtLocation")) && (paramTrackJson.mArtistArtRef != null) && (!paramTrackJson.mArtistArtRef.isEmpty()))
      {
        String str5 = ((TrackJson.ImageRef)paramTrackJson.mArtistArtRef.get(0)).mUrl;
        arrayOfObject[i] = str5;
      }
    }
  }

  public static Object[] project(SyncablePlaylist paramSyncablePlaylist, String[] paramArrayOfString)
  {
    Object[] arrayOfObject;
    if (paramSyncablePlaylist == null)
      arrayOfObject = null;
    ColumnMappings localColumnMappings;
    int i;
    int j;
    do
    {
      return arrayOfObject;
      Map localMap = sProjectionMaps;
      Class localClass = paramSyncablePlaylist.getClass();
      localColumnMappings = (ColumnMappings)localMap.get(localClass);
      arrayOfObject = new Object[paramArrayOfString.length];
      i = 0;
      j = paramArrayOfString.length;
    }
    while (i >= j);
    String str1 = paramArrayOfString[i];
    if (localColumnMappings.hasColumn(str1))
    {
      String str2 = paramArrayOfString[i];
      Object localObject = localColumnMappings.evaluate(paramSyncablePlaylist, str2);
      arrayOfObject[i] = localObject;
    }
    while (true)
    {
      i += 1;
      break;
      String str3 = paramArrayOfString[i];
      String str4 = MusicContent.SharedWithMePlaylist.ART_URL;
      if (str3.equals(str4))
      {
        List localList = paramSyncablePlaylist.mArtUrls;
        if ((localList != null) && (!localList.isEmpty()))
        {
          String[] arrayOfString = new String[localList.size()];
          int k = 0;
          while (true)
          {
            int m = arrayOfString.length;
            if (k >= m)
              break;
            String str5 = ((TrackJson.ImageRef)localList.get(k)).mUrl;
            arrayOfString[k] = str5;
            k += 1;
          }
          String str6 = MusicUtils.encodeStringArray(arrayOfString);
          arrayOfObject[i] = str6;
        }
      }
    }
  }

  public static Object[] project(SyncablePlaylistEntry paramSyncablePlaylistEntry, String[] paramArrayOfString)
  {
    return project(paramSyncablePlaylistEntry.mTrack, paramArrayOfString);
  }

  private static class ColumnMappings
  {
    private HashMap<String, String> mFieldsMap;
    private HashMap<String, Object> mValuesMap;

    private ColumnMappings()
    {
      HashMap localHashMap1 = new HashMap();
      this.mFieldsMap = localHashMap1;
      HashMap localHashMap2 = new HashMap();
      this.mValuesMap = localHashMap2;
    }

    public Object evaluate(Object paramObject, String paramString)
    {
      if (paramObject == null)
        throw new IllegalArgumentException("Source object is null.");
      if (TextUtils.isEmpty(paramString))
        throw new IllegalArgumentException("Column name is empty.");
      Object localObject1;
      if (this.mValuesMap.containsKey(paramString))
        localObject1 = this.mValuesMap.get(paramString);
      while (true)
      {
        return localObject1;
        String str1;
        if (this.mFieldsMap.containsKey(paramString))
          str1 = (String)this.mFieldsMap.get(paramString);
        try
        {
          Object localObject2 = paramObject.getClass().getField(str1).get(paramObject);
          localObject1 = localObject2;
        }
        catch (NoSuchFieldException localNoSuchFieldException)
        {
          Object[] arrayOfObject1 = new Object[2];
          arrayOfObject1[0] = str1;
          String str2 = paramObject.getClass().getCanonicalName();
          arrayOfObject1[1] = str2;
          String str3 = String.format("Field %s doesn't exist on class %s", arrayOfObject1);
          int i = Log.w("ProjectionUtils", str3);
          if ("_id".equals(paramString))
            localObject1 = Integer.valueOf(ProjectionUtils.access$000());
        }
        catch (Exception localException)
        {
          while (true)
          {
            Object[] arrayOfObject2 = new Object[2];
            arrayOfObject2[0] = str1;
            String str4 = paramObject.getClass().getCanonicalName();
            arrayOfObject2[1] = str4;
            String str5 = String.format("Couldn't get the value for field %s from an object of class %s", arrayOfObject2);
            int j = Log.w("ProjectionUtils", str5);
          }
          localObject1 = null;
        }
      }
    }

    public boolean hasColumn(String paramString)
    {
      if ((this.mFieldsMap.containsKey(paramString)) || (this.mValuesMap.containsKey(paramString)) || ("_id".equals(paramString)));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public void putField(String paramString1, String paramString2)
    {
      if (TextUtils.isEmpty(paramString1))
        return;
      if (TextUtils.isEmpty(paramString2))
        return;
      StringBuilder localStringBuilder1 = new StringBuilder().append("m");
      String str1 = paramString2.substring(0, 1).toUpperCase();
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str1);
      if (paramString2.length() > 1);
      for (String str2 = paramString2.substring(1); ; str2 = "")
      {
        String str3 = str2;
        Object localObject = this.mFieldsMap.put(paramString1, str3);
        return;
      }
    }

    public void putValue(String paramString, Object paramObject)
    {
      if (TextUtils.isEmpty(paramString))
        return;
      if (paramObject == null)
        return;
      Object localObject = this.mValuesMap.put(paramString, paramObject);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.ProjectionUtils
 * JD-Core Version:    0.6.2
 */