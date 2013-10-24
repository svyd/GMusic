package com.google.android.music.store;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.net.Uri.Builder;
import android.util.Log;
import com.google.android.music.cloudclient.MusicCloud;
import com.google.android.music.cloudclient.MusicCloudImpl;
import com.google.android.music.cloudclient.SearchClientResponseJson;
import com.google.android.music.cloudclient.SearchClientResponseJson.SearchClientResponseEntry.Type;
import com.google.android.music.ui.SearchActivity;
import com.google.android.music.utils.DbUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.PostFroyoUtils.CancellationSignalComp;
import com.google.android.music.utils.PostFroyoUtils.SQLiteDatabaseComp;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;

public class SearchHandler
{
  private static final String[] LIST_ITEM_COUNT_EXPRESSIONS;
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.SEARCH);
  private static final String[] SEARCH_ALBUM_COUNT_EXPRESSIONS;
  private static final String[] SEARCH_SONG_COUNT_EXPRESSIONS;
  private static HashMap<String, String> sSearchAlbumMap;
  private static HashMap<String, String>[] sSearchArtistMaps;
  private static HashMap<String, String>[] sSearchPlaylistMaps;
  private static HashMap<String, String> sSearchTrackMap;
  private final PostFroyoUtils.CancellationSignalComp mCancellationSignal;
  private final Context mContext;
  private final ThreadPoolExecutor mExecutor;
  private final int mFilterIndex;
  private final boolean mIsGlobalSearch;
  private final boolean mIsNautilusEnabled;
  private final String mLimit;
  private final String[] mProjection;
  private final SQLiteQueryBuilder mQueryBuilder;
  private final String mSearchQuery;
  private final Store mStore;
  private final Uri mUri;

  static
  {
    String[] arrayOfString1 = new String[6];
    arrayOfString1[0] = "(SELECT count(distinct(SongId)) FROM MUSIC WHERE (AlbumArtistId=search_artist_id OR ArtistId=search_artist_id))";
    StringBuilder localStringBuilder1 = new StringBuilder().append("(SELECT count(distinct(SongId)) FROM MUSIC WHERE (AlbumArtistId=search_artist_id OR ArtistId=search_artist_id) AND ");
    String str1 = Filters.FILTERS[1];
    String str2 = str1 + ")";
    arrayOfString1[1] = str2;
    StringBuilder localStringBuilder2 = new StringBuilder().append("(SELECT count(distinct(SongId)) FROM MUSIC WHERE (AlbumArtistId=search_artist_id OR ArtistId=search_artist_id) AND ");
    String str3 = Filters.FILTERS[2];
    String str4 = str3 + ")";
    arrayOfString1[2] = str4;
    StringBuilder localStringBuilder3 = new StringBuilder().append("(SELECT count(distinct(SongId)) FROM MUSIC WHERE (AlbumArtistId=search_artist_id OR ArtistId=search_artist_id) AND ");
    String str5 = Filters.FILTERS[3];
    String str6 = str5 + ")";
    arrayOfString1[3] = str6;
    StringBuilder localStringBuilder4 = new StringBuilder().append("(SELECT count(distinct(SongId)) FROM MUSIC WHERE (AlbumArtistId=search_artist_id OR ArtistId=search_artist_id) AND ");
    String str7 = Filters.FILTERS[4];
    String str8 = str7 + ")";
    arrayOfString1[4] = str8;
    StringBuilder localStringBuilder5 = new StringBuilder().append("(SELECT count(distinct(SongId)) FROM MUSIC WHERE (AlbumArtistId=search_artist_id OR ArtistId=search_artist_id) AND ");
    String str9 = Filters.FILTERS[5];
    String str10 = str9 + ")";
    arrayOfString1[5] = str10;
    SEARCH_SONG_COUNT_EXPRESSIONS = arrayOfString1;
    String[] arrayOfString2 = new String[6];
    arrayOfString2[0] = "(SELECT count(distinct(AlbumId)) FROM MUSIC WHERE (AlbumArtistId=search_artist_id OR ArtistId=search_artist_id))";
    StringBuilder localStringBuilder6 = new StringBuilder().append("(SELECT count(distinct(AlbumId)) FROM MUSIC WHERE (AlbumArtistId=search_artist_id OR ArtistId=search_artist_id) AND ");
    String str11 = Filters.FILTERS[1];
    String str12 = str11 + ")";
    arrayOfString2[1] = str12;
    StringBuilder localStringBuilder7 = new StringBuilder().append("(SELECT count(distinct(AlbumId)) FROM MUSIC WHERE (AlbumArtistId=search_artist_id OR ArtistId=search_artist_id) AND ");
    String str13 = Filters.FILTERS[2];
    String str14 = str13 + ")";
    arrayOfString2[2] = str14;
    StringBuilder localStringBuilder8 = new StringBuilder().append("(SELECT count(distinct(AlbumId)) FROM MUSIC WHERE (AlbumArtistId=search_artist_id OR ArtistId=search_artist_id) AND ");
    String str15 = Filters.FILTERS[3];
    String str16 = str15 + ")";
    arrayOfString2[3] = str16;
    StringBuilder localStringBuilder9 = new StringBuilder().append("(SELECT count(distinct(AlbumId)) FROM MUSIC WHERE (AlbumArtistId=search_artist_id OR ArtistId=search_artist_id) AND ");
    String str17 = Filters.FILTERS[4];
    String str18 = str17 + ")";
    arrayOfString2[4] = str18;
    StringBuilder localStringBuilder10 = new StringBuilder().append("(SELECT count(distinct(AlbumId)) FROM MUSIC WHERE (AlbumArtistId=search_artist_id OR ArtistId=search_artist_id) AND ");
    String str19 = Filters.FILTERS[5];
    String str20 = str19 + ")";
    arrayOfString2[5] = str20;
    SEARCH_ALBUM_COUNT_EXPRESSIONS = arrayOfString2;
    String[] arrayOfString3 = new String[6];
    arrayOfString3[0] = "(SELECT count(1) FROM LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE ListId=LISTS.Id)";
    StringBuilder localStringBuilder11 = new StringBuilder().append("(SELECT count(1) FROM LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE ListId=LISTS.Id AND ");
    String str21 = Filters.FILTERS[1];
    String str22 = str21 + ")";
    arrayOfString3[1] = str22;
    StringBuilder localStringBuilder12 = new StringBuilder().append("(SELECT count(1) FROM LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE ListId=LISTS.Id AND ");
    String str23 = Filters.FILTERS[2];
    String str24 = str23 + ")";
    arrayOfString3[2] = str24;
    StringBuilder localStringBuilder13 = new StringBuilder().append("(SELECT count(1) FROM LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE ListId=LISTS.Id AND ");
    String str25 = Filters.FILTERS[3];
    String str26 = str25 + ")";
    arrayOfString3[3] = str26;
    StringBuilder localStringBuilder14 = new StringBuilder().append("(SELECT count(1) FROM LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE ListId=LISTS.Id AND ");
    String str27 = Filters.FILTERS[4];
    String str28 = str27 + ")";
    arrayOfString3[4] = str28;
    StringBuilder localStringBuilder15 = new StringBuilder().append("(SELECT count(1) FROM LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE ListId=LISTS.Id AND ");
    String str29 = Filters.FILTERS[5];
    String str30 = str29 + ")";
    arrayOfString3[5] = str30;
    LIST_ITEM_COUNT_EXPRESSIONS = arrayOfString3;
    Uri localUri1 = MusicContent.CONTENT_URI.buildUpon().appendPath("albumorfauxart").build();
    Uri localUri2 = MusicContent.CONTENT_URI.buildUpon().appendPath("largealbumart").build();
    String str31 = "CASE WHEN ( AlbumArtLocation IS NOT NULL AND AlbumArtLocation LIKE 'mediastore:%' ) OR EXISTS ( SELECT 1 FROM ARTWORK AS m WHERE m.AlbumId = MUSIC.AlbumId AND m.LocalLocationStorageType <> 0 LIMIT 1 ) THEN '" + localUri2 + "/' || " + "AlbumId" + " END";
    Uri localUri3 = MusicContent.CONTENT_URI.buildUpon().appendPath("playlistfauxart").build();
    Uri localUri4 = MusicContent.CONTENT_URI.buildUpon().appendPath("artistfauxart").build();
    HashMap localHashMap1 = new HashMap();
    addMapping(localHashMap1, "searchType", "search_type");
    addMapping(localHashMap1, "ListType", "null");
    addMapping(localHashMap1, "_id", "search_artist_id");
    addMapping(localHashMap1, "searchName", "search_artist");
    addMapping(localHashMap1, "searchSortName", "search_canonical_artist");
    addMapping(localHashMap1, "Artist", "search_artist");
    addMapping(localHashMap1, "AlbumArtist", "search_artist");
    addMapping(localHashMap1, "AlbumArtistId", "search_artist_id");
    addMapping(localHashMap1, "Album", "null");
    addMapping(localHashMap1, "duration", "null");
    addMapping(localHashMap1, "year", "null");
    addMapping(localHashMap1, "AlbumId", "null");
    addMapping(localHashMap1, "suggest_text_1", "search_artist");
    addMapping(localHashMap1, "suggest_text_2", "null");
    String str32 = "'" + localUri4 + "/' || " + "search_artist_id";
    addMapping(localHashMap1, "suggest_icon_1", str32);
    addMapping(localHashMap1, "suggest_icon_large", "null");
    addMapping(localHashMap1, "suggest_intent_data", "search_intent");
    addMapping(localHashMap1, "suggest_intent_data_id", "search_artist_id");
    addMapping(localHashMap1, "suggest_last_access_hint", "null");
    addMapping(localHashMap1, "suggest_shortcut_id", "'_-1'");
    addMapping(localHashMap1, "hasRemote", "0");
    addMapping(localHashMap1, "StoreAlbumId", "null");
    addMapping(localHashMap1, "Nid", "null");
    addNullGenreMappings(localHashMap1);
    sSearchArtistMaps = createMapsForFilters(localHashMap1);
    HashMap[] arrayOfHashMap1 = sSearchArtistMaps;
    String[] arrayOfString4 = SEARCH_SONG_COUNT_EXPRESSIONS;
    addMappings(arrayOfHashMap1, "itemCount", arrayOfString4);
    HashMap[] arrayOfHashMap2 = sSearchArtistMaps;
    String[] arrayOfString5 = SEARCH_ALBUM_COUNT_EXPRESSIONS;
    addMappings(arrayOfHashMap2, "albumCount", arrayOfString5);
    sSearchAlbumMap = new HashMap();
    addMapping(sSearchAlbumMap, "searchType", "'3'");
    addMapping(sSearchAlbumMap, "_id", "AlbumId");
    addMapping(sSearchAlbumMap, "ListType", "null");
    addMapping(sSearchAlbumMap, "searchName", "Album");
    addMapping(sSearchAlbumMap, "searchSortName", "CanonicalAlbum");
    addMapping(sSearchAlbumMap, "Artist", "Artist");
    addMapping(sSearchAlbumMap, "AlbumArtist", "AlbumArtist");
    addMapping(sSearchAlbumMap, "AlbumArtistId", "AlbumArtistId");
    addMapping(sSearchAlbumMap, "Album", "Album");
    addMapping(sSearchAlbumMap, "itemCount", "count(distinct songid)");
    addMapping(sSearchAlbumMap, "albumCount", "''");
    addMapping(sSearchAlbumMap, "duration", "null");
    addMapping(sSearchAlbumMap, "year", "null");
    addMapping(sSearchAlbumMap, "AlbumId", "AlbumId");
    addMapping(sSearchAlbumMap, "suggest_text_1", "Album");
    addMapping(sSearchAlbumMap, "suggest_text_2", "AlbumArtist");
    HashMap localHashMap2 = sSearchAlbumMap;
    String str33 = "'" + localUri1 + "/' || " + "AlbumId";
    addMapping(localHashMap2, "suggest_icon_1", str33);
    addMapping(sSearchAlbumMap, "suggest_icon_large", str31);
    HashMap localHashMap3 = sSearchAlbumMap;
    StringBuilder localStringBuilder16 = new StringBuilder().append("'");
    String str34 = SearchActivity.SUGGEST_DATA_ALBUM.toString();
    String str35 = str34 + "'";
    addMapping(localHashMap3, "suggest_intent_data", str35);
    addMapping(sSearchAlbumMap, "suggest_intent_data_id", "AlbumId");
    addMapping(sSearchAlbumMap, "suggest_last_access_hint", "null");
    addMapping(sSearchAlbumMap, "suggest_shortcut_id", "'_-1'");
    addMapping(sSearchAlbumMap, "hasLocal", "(MAX(LocalCopyType)  IN (100,200,300))");
    addMapping(sSearchAlbumMap, "hasRemote", "0");
    addMapping(sSearchAlbumMap, "artworkUrl", "null");
    addMapping(sSearchAlbumMap, "ArtistMetajamId", "max(ArtistMetajamId)");
    addMapping(sSearchAlbumMap, "StoreAlbumId", "max(StoreAlbumId)");
    addMapping(sSearchAlbumMap, "Nid", "null");
    addNullGenreMappings(sSearchAlbumMap);
    sSearchTrackMap = new HashMap();
    addMapping(sSearchTrackMap, "searchType", "'5'");
    addMapping(sSearchTrackMap, "ListType", null);
    addMapping(sSearchTrackMap, "_id", "Id");
    addMapping(sSearchTrackMap, "searchName", "Title");
    addMapping(sSearchTrackMap, "searchSortName", "CanonicalName");
    addMapping(sSearchTrackMap, "Artist", "Artist");
    addMapping(sSearchTrackMap, "AlbumArtist", "AlbumArtist");
    addMapping(sSearchTrackMap, "AlbumArtistId", "AlbumArtistId");
    addMapping(sSearchTrackMap, "Album", "Album");
    addMapping(sSearchTrackMap, "itemCount", "''");
    addMapping(sSearchTrackMap, "albumCount", "''");
    addMapping(sSearchTrackMap, "duration", "Duration");
    addMapping(sSearchTrackMap, "year", "Year");
    addMapping(sSearchTrackMap, "AlbumId", "AlbumId");
    addMapping(sSearchTrackMap, "suggest_text_1", "Title");
    addMapping(sSearchTrackMap, "suggest_text_2", "Artist");
    HashMap localHashMap4 = sSearchTrackMap;
    String str36 = "'" + localUri1 + "/' || " + "AlbumId";
    addMapping(localHashMap4, "suggest_icon_1", str36);
    addMapping(sSearchTrackMap, "suggest_icon_large", str31);
    HashMap localHashMap5 = sSearchTrackMap;
    StringBuilder localStringBuilder17 = new StringBuilder().append("'");
    String str37 = SearchActivity.SUGGEST_DATA_TRACK.toString();
    String str38 = str37 + "'";
    addMapping(localHashMap5, "suggest_intent_data", str38);
    addMapping(sSearchTrackMap, "suggest_intent_data_id", "Id");
    addMapping(sSearchTrackMap, "suggest_last_access_hint", "null");
    addMapping(sSearchTrackMap, "suggest_shortcut_id", "'_-1'");
    MusicContentProvider.addCategoryMappings(sSearchTrackMap, false, true);
    addMapping(sSearchTrackMap, "artworkUrl", "null");
    addMapping(sSearchTrackMap, "ArtistMetajamId", "max(ArtistMetajamId)");
    addMapping(sSearchTrackMap, "StoreAlbumId", "max(StoreAlbumId)");
    addMapping(sSearchTrackMap, "Nid", "max(Nid)");
    addNullGenreMappings(sSearchTrackMap);
    HashMap localHashMap6 = new HashMap();
    addMapping(localHashMap6, "searchType", "'4'");
    addMapping(localHashMap6, "ListType", "ListType");
    addMapping(localHashMap6, "_id", "Id");
    addMapping(localHashMap6, "searchName", "Name");
    addMapping(localHashMap6, "searchSortName", "null");
    addMapping(localHashMap6, "Artist", "null");
    addMapping(localHashMap6, "AlbumArtist", "null");
    addMapping(localHashMap6, "AlbumArtistId", "null");
    addMapping(localHashMap6, "Album", "null");
    addMapping(localHashMap6, "albumCount", "''");
    addMapping(localHashMap6, "duration", "null");
    addMapping(localHashMap6, "year", "null");
    addMapping(localHashMap6, "AlbumId", "null");
    addMapping(localHashMap6, "suggest_text_1", "Name");
    addMapping(localHashMap6, "suggest_text_2", "null");
    String str39 = "'" + localUri3 + "/' || " + "Id";
    addMapping(localHashMap6, "suggest_icon_1", str39);
    addMapping(localHashMap6, "suggest_icon_large", "null");
    StringBuilder localStringBuilder18 = new StringBuilder().append("'");
    String str40 = SearchActivity.SUGGEST_DATA_PLAYLIST.toString();
    String str41 = str40 + "'";
    addMapping(localHashMap6, "suggest_intent_data", str41);
    addMapping(localHashMap6, "suggest_intent_data_id", "Id");
    addMapping(localHashMap6, "suggest_last_access_hint", "null");
    addMapping(localHashMap6, "suggest_shortcut_id", "'_-1'");
    addMapping(localHashMap6, "hasLocal", "EXISTS (SELECT 1 FROM LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE (ListId=LISTS.Id) AND LocalCopyType IN (100,200,300) LIMIT 1)");
    addMapping(localHashMap6, "hasRemote", "0");
    addMapping(localHashMap6, "artworkUrl", "null");
    addMapping(localHashMap6, "ArtistMetajamId", "null");
    addMapping(localHashMap6, "StoreAlbumId", "null");
    addMapping(localHashMap6, "Nid", "null");
    addNullGenreMappings(localHashMap6);
    sSearchPlaylistMaps = createMapsForFilters(localHashMap6);
    HashMap[] arrayOfHashMap3 = sSearchPlaylistMaps;
    String[] arrayOfString6 = LIST_ITEM_COUNT_EXPRESSIONS;
    addMappings(arrayOfHashMap3, "itemCount", arrayOfString6);
  }

  public SearchHandler(Context paramContext, Store paramStore, ThreadPoolExecutor paramThreadPoolExecutor, SQLiteQueryBuilder paramSQLiteQueryBuilder, Uri paramUri, String paramString1, String paramString2, String[] paramArrayOfString, int paramInt, PostFroyoUtils.CancellationSignalComp paramCancellationSignalComp, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mContext = paramContext;
    this.mStore = paramStore;
    this.mExecutor = paramThreadPoolExecutor;
    this.mQueryBuilder = paramSQLiteQueryBuilder;
    this.mUri = paramUri;
    this.mSearchQuery = paramString1;
    this.mProjection = paramArrayOfString;
    this.mFilterIndex = paramInt;
    this.mCancellationSignal = paramCancellationSignalComp;
    this.mIsGlobalSearch = paramBoolean1;
    this.mIsNautilusEnabled = paramBoolean2;
    if ((paramString2 == null) || (paramString2.length() == 0))
      paramString2 = "1000";
    this.mLimit = paramString2;
  }

  private static <T> void addIfNotNull(Collection<? super T> paramCollection, T paramT)
  {
    if (paramT == null)
      return;
    boolean bool = paramCollection.add(paramT);
  }

  private static void addMapping(HashMap<String, String> paramHashMap, String paramString1, String paramString2)
  {
    String str = paramString2 + " AS " + paramString1;
    Object localObject = paramHashMap.put(paramString1, str);
  }

  private static void addMappings(HashMap<String, String>[] paramArrayOfHashMap, String paramString, String[] paramArrayOfString)
  {
    int i = 0;
    while (true)
    {
      int j = paramArrayOfHashMap.length;
      if (i >= j)
        return;
      HashMap<String, String> localHashMap = paramArrayOfHashMap[i];
      String str = paramArrayOfString[i];
      addMapping(localHashMap, paramString, str);
      i += 1;
    }
  }

  private static void addNullGenreMappings(HashMap<String, String> paramHashMap)
  {
    addMapping(paramHashMap, "name", "null");
    addMapping(paramHashMap, "genreArtUris", "null");
    addMapping(paramHashMap, "genreServerId", "null");
    addMapping(paramHashMap, "parentGenreId", "null");
    addMapping(paramHashMap, "subgenreCount", "null");
  }

  private static HashMap<String, String>[] createMapsForFilters(HashMap<String, String> paramHashMap)
  {
    HashMap[] arrayOfHashMap = new HashMap[6];
    arrayOfHashMap[0] = paramHashMap;
    int i = 1;
    while (i < 6)
    {
      HashMap localHashMap = new HashMap(paramHashMap);
      arrayOfHashMap[i] = localHashMap;
      i += 1;
    }
    return arrayOfHashMap;
  }

  private Cursor doLocalAlbumQuery(SQLiteDatabase paramSQLiteDatabase, String[] paramArrayOfString, String paramString1, String paramString2, boolean paramBoolean)
  {
    this.mQueryBuilder.setTables("MUSIC");
    SQLiteQueryBuilder localSQLiteQueryBuilder1 = this.mQueryBuilder;
    HashMap localHashMap = sSearchAlbumMap;
    localSQLiteQueryBuilder1.setProjectionMap(localHashMap);
    Cursor localCursor;
    if (paramBoolean)
    {
      localSQLiteQueryBuilder2 = this.mQueryBuilder;
      arrayOfString1 = this.mProjection;
      StringBuilder localStringBuilder1 = new StringBuilder().append("CanonicalAlbum").append(" IN (SELECT ").append("CanonicalAlbum").append(" FROM ").append("MUSIC").append(" WHERE ").append("CanonicalAlbum").append(" LIKE ? ESCAPE '!' ").append(" OR ").append("CanonicalAlbum").append(" LIKE ? ESCAPE '!' ").append(" GROUP BY ").append("CanonicalAlbum").append(")");
      String str1 = paramString1;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str1);
      String str2 = paramString2;
      str3 = str2;
      str4 = "AlbumId";
      str5 = null;
      if (this.mIsGlobalSearch);
      for (str6 = null; ; str6 = "CanonicalAlbum")
      {
        String str7 = this.mLimit;
        PostFroyoUtils.CancellationSignalComp localCancellationSignalComp1 = this.mCancellationSignal;
        SQLiteDatabase localSQLiteDatabase1 = paramSQLiteDatabase;
        String[] arrayOfString2 = paramArrayOfString;
        localCursor = PostFroyoUtils.SQLiteDatabaseComp.query(localSQLiteQueryBuilder2, localSQLiteDatabase1, arrayOfString1, str3, arrayOfString2, str4, str5, str6, str7, localCancellationSignalComp1);
        int i = localCursor.getCount();
        return localCursor;
      }
    }
    SQLiteQueryBuilder localSQLiteQueryBuilder2 = this.mQueryBuilder;
    String[] arrayOfString1 = this.mProjection;
    StringBuilder localStringBuilder3 = new StringBuilder().append("(").append("Album").append(" LIKE ? ESCAPE '!' ").append(" OR ").append("Album").append(" LIKE ? ESCAPE '!' ").append(")");
    String str8 = paramString1;
    StringBuilder localStringBuilder4 = localStringBuilder3.append(str8);
    String str9 = paramString2;
    String str3 = str9;
    String str4 = "AlbumId";
    String str5 = null;
    if (this.mIsGlobalSearch);
    for (String str6 = null; ; str6 = "CanonicalAlbum")
    {
      String str10 = this.mLimit;
      PostFroyoUtils.CancellationSignalComp localCancellationSignalComp2 = this.mCancellationSignal;
      SQLiteDatabase localSQLiteDatabase2 = paramSQLiteDatabase;
      String[] arrayOfString3 = paramArrayOfString;
      localCursor = PostFroyoUtils.SQLiteDatabaseComp.query(localSQLiteQueryBuilder2, localSQLiteDatabase2, arrayOfString1, str3, arrayOfString3, str4, str5, str6, str10, localCancellationSignalComp2);
      break;
    }
  }

  private Cursor doLocalArtistQuery(SQLiteDatabase paramSQLiteDatabase, String[] paramArrayOfString, String paramString1, String paramString2, boolean paramBoolean)
  {
    this.mQueryBuilder.setTables("MUSIC");
    String str1;
    String str2;
    label30: String[] arrayOfString1;
    StringBuilder localStringBuilder3;
    if (paramBoolean)
    {
      str1 = "CanonicalAlbumArtist";
      if (!paramBoolean)
        break label895;
      str2 = "CanonicalArtist";
      arrayOfString1 = new String[paramArrayOfString.length * 2];
      int i = paramArrayOfString.length;
      System.arraycopy(paramArrayOfString, 0, arrayOfString1, 0, i);
      int j = paramArrayOfString.length;
      int k = paramArrayOfString.length;
      System.arraycopy(paramArrayOfString, 0, arrayOfString1, j, k);
      String[] arrayOfString2 = this.mProjection;
      HashMap[] arrayOfHashMap = sSearchArtistMaps;
      int m = this.mFilterIndex;
      HashMap localHashMap = arrayOfHashMap[m];
      String str3 = DbUtils.formatProjection(arrayOfString2, localHashMap);
      StringBuilder localStringBuilder1 = new StringBuilder().append("SELECT ").append(str3).append(" FROM ").append("(").append("SELECT ").append("'").append(1).append("' as ").append("search_type").append(",").append("AlbumArtistId").append(" as ").append("search_artist_id").append(", ").append("AlbumArtist").append(" as ").append("search_artist").append(", ").append("CanonicalAlbumArtist").append(" as ").append("search_canonical_artist").append(",").append("'");
      String str4 = SearchActivity.SUGGEST_DATA_ALBUM_ARTIST.toString();
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str4).append("'").append(" as ").append("search_intent").append(",").append("(MAX(LocalCopyType)  IN (100,200,300))").append(" as ").append("hasLocal").append(",").append("max(").append("ArtistArtLocation").append(") as ").append("artworkUrl").append(",").append("max(").append("ArtistMetajamId").append(") as ").append("ArtistMetajamId").append(" FROM ").append("MUSIC").append(" WHERE (").append(str1).append(" LIKE ? ESCAPE '!' ").append(" OR ").append(str1).append(" LIKE ? ESCAPE '!' ").append(")").append(paramString1).append(paramString2).append(" GROUP BY ").append("AlbumArtistId").append(" UNION ").append("SELECT ").append("'").append(2).append("' as ").append("search_type").append(",").append("ArtistId").append(" as ").append("search_artist_id").append(", ").append("Artist").append(" as ").append("search_artist").append(", ").append("CanonicalArtist").append(" as ").append("search_canonical_artist").append(",").append("'");
      String str5 = SearchActivity.SUGGEST_DATA_ARTIST.toString();
      localStringBuilder3 = localStringBuilder2.append(str5).append("'").append(" as ").append("search_intent").append(",").append("(MAX(LocalCopyType)  IN (100,200,300))").append(" as ").append("hasLocal").append(",").append("max(").append("ArtistArtLocation").append(") as ").append("artworkUrl").append(",").append("max(").append("ArtistMetajamId").append(") as ").append("ArtistMetajamId").append(" FROM ").append("MUSIC").append(" WHERE ").append("(").append(str2).append(" LIKE ? ESCAPE '!' ").append(" OR ").append(str2).append(" LIKE ? ESCAPE '!' ").append(")").append(" AND (").append("ArtistId").append("<>").append("AlbumArtistId").append(")").append(" AND NOT EXISTS( ").append("SELECT 1 FROM ").append("MUSIC").append(" as m ").append("WHERE m.").append("AlbumArtistId").append("=").append("MUSIC").append(".").append("ArtistId").append(paramString1).append(")").append(paramString1).append(paramString2).append(" GROUP BY ").append("CanonicalArtist").append(")");
      if (!this.mIsGlobalSearch)
        break label902;
    }
    label902: for (String str6 = ""; ; str6 = " ORDER BY search_canonical_artist")
    {
      StringBuilder localStringBuilder4 = localStringBuilder3.append(str6).append(" LIMIT ");
      String str7 = this.mLimit;
      String str8 = str7;
      PostFroyoUtils.CancellationSignalComp localCancellationSignalComp = this.mCancellationSignal;
      return PostFroyoUtils.SQLiteDatabaseComp.rawQuery(paramSQLiteDatabase, str8, arrayOfString1, localCancellationSignalComp);
      str1 = "AlbumArtist";
      break;
      label895: str2 = "Artist";
      break label30;
    }
  }

  private Cursor doLocalPlaylistQuery(SQLiteDatabase paramSQLiteDatabase, String[] paramArrayOfString)
  {
    this.mQueryBuilder.setTables("LISTS");
    SQLiteQueryBuilder localSQLiteQueryBuilder1 = this.mQueryBuilder;
    HashMap[] arrayOfHashMap = sSearchPlaylistMaps;
    int i = this.mFilterIndex;
    HashMap localHashMap = arrayOfHashMap[i];
    localSQLiteQueryBuilder1.setProjectionMap(localHashMap);
    SQLiteQueryBuilder localSQLiteQueryBuilder2 = this.mQueryBuilder;
    String[] arrayOfString1 = this.mProjection;
    String str1 = "(" + "Name" + " LIKE ? ESCAPE '!' " + " OR " + "Name" + " LIKE ? ESCAPE '!' " + ")" + " AND " + "ListType IN (0, 1)";
    if (this.mIsGlobalSearch);
    for (String str2 = null; ; str2 = "Name")
    {
      String str3 = this.mLimit;
      PostFroyoUtils.CancellationSignalComp localCancellationSignalComp = this.mCancellationSignal;
      SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
      String[] arrayOfString2 = paramArrayOfString;
      String str4 = null;
      Cursor localCursor = PostFroyoUtils.SQLiteDatabaseComp.query(localSQLiteQueryBuilder2, localSQLiteDatabase, arrayOfString1, str1, arrayOfString2, null, str4, str2, str3, localCancellationSignalComp);
      int j = localCursor.getCount();
      return localCursor;
    }
  }

  private Cursor doLocalTrackQuery(SQLiteDatabase paramSQLiteDatabase, String[] paramArrayOfString, String paramString1, String paramString2, boolean paramBoolean)
  {
    this.mQueryBuilder.setTables("MUSIC");
    SQLiteQueryBuilder localSQLiteQueryBuilder1 = this.mQueryBuilder;
    HashMap localHashMap = sSearchTrackMap;
    localSQLiteQueryBuilder1.setProjectionMap(localHashMap);
    String str1;
    SQLiteQueryBuilder localSQLiteQueryBuilder2;
    String[] arrayOfString1;
    String str4;
    String str5;
    String str6;
    if (paramBoolean)
    {
      str1 = "CanonicalName";
      localSQLiteQueryBuilder2 = this.mQueryBuilder;
      arrayOfString1 = this.mProjection;
      StringBuilder localStringBuilder1 = new StringBuilder().append("(").append(str1).append(" LIKE ? ESCAPE '!' ").append(" OR ").append(str1).append(" LIKE ? ESCAPE '!' ").append(")");
      String str2 = paramString1;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str2);
      String str3 = paramString2;
      str4 = str3;
      str5 = "CanonicalName,SongId";
      str6 = null;
      if (!this.mIsGlobalSearch)
        break label207;
    }
    label207: for (String str7 = null; ; str7 = "CanonicalName")
    {
      String str8 = this.mLimit;
      PostFroyoUtils.CancellationSignalComp localCancellationSignalComp = this.mCancellationSignal;
      SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
      String[] arrayOfString2 = paramArrayOfString;
      Cursor localCursor = PostFroyoUtils.SQLiteDatabaseComp.query(localSQLiteQueryBuilder2, localSQLiteDatabase, arrayOfString1, str4, arrayOfString2, str5, str6, str7, str8, localCancellationSignalComp);
      int i = localCursor.getCount();
      return localCursor;
      str1 = "Title";
      break;
    }
  }

  // ERROR //
  public Cursor performQuery()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   4: ifnull +29 -> 33
    //   7: aload_0
    //   8: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   11: arraylength
    //   12: ifeq +21 -> 33
    //   15: aload_0
    //   16: getfield 304	com/google/android/music/store/SearchHandler:mSearchQuery	Ljava/lang/String;
    //   19: ifnull +14 -> 33
    //   22: aload_0
    //   23: getfield 304	com/google/android/music/store/SearchHandler:mSearchQuery	Ljava/lang/String;
    //   26: invokevirtual 318	java/lang/String:length	()I
    //   29: iconst_2
    //   30: if_icmpge +7 -> 37
    //   33: aconst_null
    //   34: astore_1
    //   35: aload_1
    //   36: areturn
    //   37: new 482	com/google/android/music/store/TagNormalizer
    //   40: dup
    //   41: invokespecial 483	com/google/android/music/store/TagNormalizer:<init>	()V
    //   44: astore_2
    //   45: aconst_null
    //   46: astore_3
    //   47: aconst_null
    //   48: astore 4
    //   50: aconst_null
    //   51: astore 5
    //   53: aconst_null
    //   54: astore 6
    //   56: aconst_null
    //   57: astore 7
    //   59: aconst_null
    //   60: astore 8
    //   62: aconst_null
    //   63: astore 9
    //   65: aconst_null
    //   66: astore 10
    //   68: aconst_null
    //   69: astore 11
    //   71: aconst_null
    //   72: astore 12
    //   74: aconst_null
    //   75: astore 13
    //   77: aload_0
    //   78: getfield 302	com/google/android/music/store/SearchHandler:mUri	Landroid/net/Uri;
    //   81: ldc_w 485
    //   84: invokevirtual 489	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   87: astore 14
    //   89: aload 14
    //   91: astore 15
    //   93: ldc_w 491
    //   96: aload 15
    //   98: invokevirtual 494	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   101: istore 16
    //   103: aload 14
    //   105: ifnull +8 -> 113
    //   108: iload 16
    //   110: ifeq +370 -> 480
    //   113: iconst_1
    //   114: istore 17
    //   116: iload 17
    //   118: ifne +18 -> 136
    //   121: aload 14
    //   123: astore 18
    //   125: ldc_w 496
    //   128: aload 18
    //   130: invokevirtual 494	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   133: ifeq +353 -> 486
    //   136: iconst_1
    //   137: istore 19
    //   139: iload 17
    //   141: ifne +18 -> 159
    //   144: aload 14
    //   146: astore 20
    //   148: ldc_w 498
    //   151: aload 20
    //   153: invokevirtual 494	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   156: ifeq +336 -> 492
    //   159: iconst_1
    //   160: istore 21
    //   162: iload 17
    //   164: ifne +18 -> 182
    //   167: aload 14
    //   169: astore 22
    //   171: ldc_w 500
    //   174: aload 22
    //   176: invokevirtual 494	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   179: ifeq +319 -> 498
    //   182: iconst_1
    //   183: istore 23
    //   185: iload 17
    //   187: ifne +18 -> 205
    //   190: aload 14
    //   192: astore 24
    //   194: ldc_w 502
    //   197: aload 24
    //   199: invokevirtual 494	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   202: ifeq +302 -> 504
    //   205: iconst_1
    //   206: istore 25
    //   208: iload 17
    //   210: ifne +18 -> 228
    //   213: aload 14
    //   215: astore 26
    //   217: ldc_w 504
    //   220: aload 26
    //   222: invokevirtual 494	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   225: ifeq +285 -> 510
    //   228: iconst_1
    //   229: istore 27
    //   231: aload_0
    //   232: getfield 314	com/google/android/music/store/SearchHandler:mIsNautilusEnabled	Z
    //   235: ifeq +2069 -> 2304
    //   238: aload_0
    //   239: getfield 294	com/google/android/music/store/SearchHandler:mContext	Landroid/content/Context;
    //   242: invokevirtual 510	android/content/Context:getApplicationContext	()Landroid/content/Context;
    //   245: invokevirtual 514	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   248: ldc_w 516
    //   251: bipush 10
    //   253: invokestatic 522	com/google/android/gsf/Gservices:getInt	(Landroid/content/ContentResolver;Ljava/lang/String;I)I
    //   256: istore 28
    //   258: aload_0
    //   259: getfield 304	com/google/android/music/store/SearchHandler:mSearchQuery	Ljava/lang/String;
    //   262: astore 29
    //   264: aload_0
    //   265: getfield 298	com/google/android/music/store/SearchHandler:mExecutor	Ljava/util/concurrent/ThreadPoolExecutor;
    //   268: astore 30
    //   270: aload_0
    //   271: astore 31
    //   273: aload 29
    //   275: astore 32
    //   277: iload 28
    //   279: istore 33
    //   281: new 6	com/google/android/music/store/SearchHandler$1
    //   284: dup
    //   285: aload 31
    //   287: aload 32
    //   289: iload 33
    //   291: invokespecial 525	com/google/android/music/store/SearchHandler$1:<init>	(Lcom/google/android/music/store/SearchHandler;Ljava/lang/String;I)V
    //   294: astore 34
    //   296: aload 30
    //   298: aload 34
    //   300: invokevirtual 531	java/util/concurrent/ThreadPoolExecutor:submit	(Ljava/util/concurrent/Callable;)Ljava/util/concurrent/Future;
    //   303: astore 35
    //   305: getstatic 537	java/util/concurrent/TimeUnit:SECONDS	Ljava/util/concurrent/TimeUnit;
    //   308: astore 36
    //   310: aload 35
    //   312: ldc2_w 538
    //   315: aload 36
    //   317: invokeinterface 545 4 0
    //   322: checkcast 547	com/google/android/music/cloudclient/SearchClientResponseJson
    //   325: astore 37
    //   327: aload 37
    //   329: ifnull +1961 -> 2290
    //   332: aload 37
    //   334: getfield 551	com/google/android/music/cloudclient/SearchClientResponseJson:mEntries	Ljava/util/List;
    //   337: ifnull +1953 -> 2290
    //   340: getstatic 59	com/google/android/music/store/SearchHandler:LOGV	Z
    //   343: ifeq +50 -> 393
    //   346: iconst_1
    //   347: anewarray 4	java/lang/Object
    //   350: astore 38
    //   352: aload 37
    //   354: getfield 551	com/google/android/music/cloudclient/SearchClientResponseJson:mEntries	Ljava/util/List;
    //   357: invokeinterface 556 1 0
    //   362: invokestatic 562	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   365: astore 39
    //   367: aload 38
    //   369: iconst_0
    //   370: aload 39
    //   372: aastore
    //   373: ldc_w 564
    //   376: aload 38
    //   378: invokestatic 568	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   381: astore 40
    //   383: ldc_w 570
    //   386: aload 40
    //   388: invokestatic 576	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   391: istore 41
    //   393: iload 16
    //   395: ifeq +1063 -> 1458
    //   398: ldc_w 577
    //   401: fstore 42
    //   403: aconst_null
    //   404: astore 43
    //   406: aload 37
    //   408: getfield 551	com/google/android/music/cloudclient/SearchClientResponseJson:mEntries	Ljava/util/List;
    //   411: invokeinterface 581 1 0
    //   416: astore 44
    //   418: aload 44
    //   420: invokeinterface 586 1 0
    //   425: ifeq +91 -> 516
    //   428: aload 44
    //   430: invokeinterface 590 1 0
    //   435: checkcast 592	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry
    //   438: astore 45
    //   440: aload 45
    //   442: invokevirtual 596	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:getType	()Lcom/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry$Type;
    //   445: astore 46
    //   447: aload 45
    //   449: getfield 599	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mNavigationalResult	Z
    //   452: ifeq -34 -> 418
    //   455: aload 45
    //   457: getfield 603	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mNavigationalConfidence	F
    //   460: fload 42
    //   462: fcmpl
    //   463: ifle -45 -> 418
    //   466: aload 45
    //   468: getfield 603	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mNavigationalConfidence	F
    //   471: fstore 42
    //   473: aload 45
    //   475: astore 43
    //   477: goto -59 -> 418
    //   480: iconst_0
    //   481: istore 17
    //   483: goto -367 -> 116
    //   486: iconst_0
    //   487: istore 19
    //   489: goto -350 -> 139
    //   492: iconst_0
    //   493: istore 21
    //   495: goto -333 -> 162
    //   498: iconst_0
    //   499: istore 23
    //   501: goto -316 -> 185
    //   504: iconst_0
    //   505: istore 25
    //   507: goto -299 -> 208
    //   510: iconst_0
    //   511: istore 27
    //   513: goto -282 -> 231
    //   516: new 605	android/database/MatrixCursor
    //   519: astore_1
    //   520: aload_0
    //   521: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   524: astore 47
    //   526: aload_1
    //   527: aload 47
    //   529: invokespecial 608	android/database/MatrixCursor:<init>	([Ljava/lang/String;)V
    //   532: aload 43
    //   534: ifnull -499 -> 35
    //   537: getstatic 612	com/google/android/music/store/SearchHandler$2:$SwitchMap$com$google$android$music$cloudclient$SearchClientResponseJson$SearchClientResponseEntry$Type	[I
    //   540: astore 48
    //   542: aload 43
    //   544: invokevirtual 596	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:getType	()Lcom/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry$Type;
    //   547: invokevirtual 617	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry$Type:ordinal	()I
    //   550: istore 49
    //   552: aload 48
    //   554: iload 49
    //   556: iaload
    //   557: tableswitch	default:+31 -> 588, 1:+34->591, 2:+808->1365, 3:+839->1396, 4:+870->1427
    //   589: <illegal opcode>
    //   590: <illegal opcode>
    //   591: aload 43
    //   593: getfield 621	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mArtist	Lcom/google/android/music/cloudclient/ArtistJson;
    //   596: astore 50
    //   598: aload_0
    //   599: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   602: astore 51
    //   604: aload 50
    //   606: aload 51
    //   608: invokestatic 627	com/google/android/music/store/ProjectionUtils:project	(Lcom/google/android/music/cloudclient/ArtistJson;[Ljava/lang/String;)[Ljava/lang/Object;
    //   611: astore 52
    //   613: aload_1
    //   614: aload 52
    //   616: invokevirtual 631	android/database/MatrixCursor:addRow	([Ljava/lang/Object;)V
    //   619: goto -584 -> 35
    //   622: astore 53
    //   624: aload 53
    //   626: invokevirtual 634	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   629: astore 54
    //   631: aload 53
    //   633: astore 55
    //   635: ldc_w 570
    //   638: aload 54
    //   640: aload 55
    //   642: invokestatic 638	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   645: istore 56
    //   647: aload 11
    //   649: ifnull +13 -> 662
    //   652: aload 11
    //   654: invokeinterface 641 1 0
    //   659: ifeq +1669 -> 2328
    //   662: ldc_w 457
    //   665: astore 57
    //   667: aload 12
    //   669: ifnull +13 -> 682
    //   672: aload 12
    //   674: invokeinterface 641 1 0
    //   679: ifeq +1722 -> 2401
    //   682: ldc_w 457
    //   685: astore 58
    //   687: aload 13
    //   689: ifnull +13 -> 702
    //   692: aload 13
    //   694: invokeinterface 641 1 0
    //   699: ifeq +1746 -> 2445
    //   702: ldc_w 457
    //   705: astore 59
    //   707: iconst_1
    //   708: istore 60
    //   710: aload_0
    //   711: getfield 304	com/google/android/music/store/SearchHandler:mSearchQuery	Ljava/lang/String;
    //   714: astore 61
    //   716: aload_2
    //   717: astore 62
    //   719: aload 61
    //   721: astore 63
    //   723: aload 62
    //   725: aload 63
    //   727: invokevirtual 644	com/google/android/music/store/TagNormalizer:normalize	(Ljava/lang/String;)Ljava/lang/String;
    //   730: astore 64
    //   732: aload 64
    //   734: invokevirtual 318	java/lang/String:length	()I
    //   737: iconst_2
    //   738: if_icmpge +10 -> 748
    //   741: iconst_0
    //   742: istore 60
    //   744: aload 61
    //   746: astore 64
    //   748: aload 61
    //   750: bipush 33
    //   752: invokestatic 648	com/google/android/music/utils/DbUtils:escapeForLikeOperator	(Ljava/lang/String;C)Ljava/lang/String;
    //   755: astore 65
    //   757: aload 64
    //   759: bipush 33
    //   761: invokestatic 648	com/google/android/music/utils/DbUtils:escapeForLikeOperator	(Ljava/lang/String;C)Ljava/lang/String;
    //   764: astore 66
    //   766: iconst_2
    //   767: anewarray 61	java/lang/String
    //   770: astore 67
    //   772: new 65	java/lang/StringBuilder
    //   775: dup
    //   776: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   779: astore 68
    //   781: aload 65
    //   783: astore 69
    //   785: aload 68
    //   787: aload 69
    //   789: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   792: ldc_w 650
    //   795: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   798: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   801: astore 70
    //   803: aload 67
    //   805: iconst_0
    //   806: aload 70
    //   808: aastore
    //   809: new 65	java/lang/StringBuilder
    //   812: dup
    //   813: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   816: ldc_w 652
    //   819: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   822: astore 71
    //   824: aload 65
    //   826: astore 72
    //   828: aload 71
    //   830: aload 72
    //   832: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   835: ldc_w 650
    //   838: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   841: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   844: astore 73
    //   846: aload 67
    //   848: iconst_1
    //   849: aload 73
    //   851: aastore
    //   852: iconst_2
    //   853: anewarray 61	java/lang/String
    //   856: astore 74
    //   858: new 65	java/lang/StringBuilder
    //   861: dup
    //   862: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   865: astore 75
    //   867: aload 66
    //   869: astore 76
    //   871: aload 75
    //   873: aload 76
    //   875: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   878: ldc_w 650
    //   881: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   884: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   887: astore 77
    //   889: aload 74
    //   891: iconst_0
    //   892: aload 77
    //   894: aastore
    //   895: new 65	java/lang/StringBuilder
    //   898: dup
    //   899: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   902: ldc_w 652
    //   905: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   908: astore 78
    //   910: aload 66
    //   912: astore 79
    //   914: aload 78
    //   916: aload 79
    //   918: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   921: ldc_w 650
    //   924: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   927: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   930: astore 80
    //   932: aload 74
    //   934: iconst_1
    //   935: aload 80
    //   937: aastore
    //   938: aload_0
    //   939: getfield 296	com/google/android/music/store/SearchHandler:mStore	Lcom/google/android/music/store/Store;
    //   942: invokevirtual 658	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   945: astore 81
    //   947: ldc_w 457
    //   950: astore 82
    //   952: aload_0
    //   953: getfield 308	com/google/android/music/store/SearchHandler:mFilterIndex	I
    //   956: ifeq +48 -> 1004
    //   959: new 65	java/lang/StringBuilder
    //   962: dup
    //   963: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   966: ldc_w 471
    //   969: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   972: astore 83
    //   974: getstatic 79	com/google/android/music/store/Filters:FILTERS	[Ljava/lang/String;
    //   977: astore 84
    //   979: aload_0
    //   980: getfield 308	com/google/android/music/store/SearchHandler:mFilterIndex	I
    //   983: istore 85
    //   985: aload 84
    //   987: iload 85
    //   989: aaload
    //   990: astore 86
    //   992: aload 83
    //   994: aload 86
    //   996: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   999: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1002: astore 82
    //   1004: iload 19
    //   1006: ifeq +1483 -> 2489
    //   1009: aload_0
    //   1010: aload 81
    //   1012: aload 74
    //   1014: aload 82
    //   1016: aload 57
    //   1018: iload 60
    //   1020: invokespecial 660	com/google/android/music/store/SearchHandler:doLocalArtistQuery	(Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Landroid/database/Cursor;
    //   1023: astore 87
    //   1025: iload 21
    //   1027: ifeq +1468 -> 2495
    //   1030: aload_0
    //   1031: astore 88
    //   1033: aload 81
    //   1035: astore 89
    //   1037: aload 74
    //   1039: astore 90
    //   1041: aload 82
    //   1043: astore 91
    //   1045: iload 60
    //   1047: istore 92
    //   1049: aload 88
    //   1051: aload 89
    //   1053: aload 90
    //   1055: aload 91
    //   1057: aload 58
    //   1059: iload 92
    //   1061: invokespecial 662	com/google/android/music/store/SearchHandler:doLocalAlbumQuery	(Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Landroid/database/Cursor;
    //   1064: astore 93
    //   1066: iload 25
    //   1068: ifeq +1433 -> 2501
    //   1071: aload_0
    //   1072: astore 94
    //   1074: aload 67
    //   1076: astore 95
    //   1078: aload 94
    //   1080: aload 81
    //   1082: aload 95
    //   1084: invokespecial 664	com/google/android/music/store/SearchHandler:doLocalPlaylistQuery	(Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;)Landroid/database/Cursor;
    //   1087: astore 96
    //   1089: iload 23
    //   1091: ifeq +1416 -> 2507
    //   1094: aload_0
    //   1095: astore 97
    //   1097: aload 81
    //   1099: astore 98
    //   1101: aload 74
    //   1103: astore 99
    //   1105: aload 82
    //   1107: astore 100
    //   1109: iload 60
    //   1111: istore 101
    //   1113: aload 97
    //   1115: aload 98
    //   1117: aload 99
    //   1119: aload 100
    //   1121: aload 59
    //   1123: iload 101
    //   1125: invokespecial 666	com/google/android/music/store/SearchHandler:doLocalTrackQuery	(Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)Landroid/database/Cursor;
    //   1128: astore 102
    //   1130: aload 102
    //   1132: astore 103
    //   1134: aload_0
    //   1135: getfield 296	com/google/android/music/store/SearchHandler:mStore	Lcom/google/android/music/store/Store;
    //   1138: aload 81
    //   1140: invokevirtual 670	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   1143: new 672	java/util/ArrayList
    //   1146: astore 104
    //   1148: aload 104
    //   1150: bipush 10
    //   1152: invokespecial 675	java/util/ArrayList:<init>	(I)V
    //   1155: aload 104
    //   1157: astore 105
    //   1159: aload_3
    //   1160: astore 106
    //   1162: aload 105
    //   1164: aload 106
    //   1166: invokestatic 677	com/google/android/music/store/SearchHandler:addIfNotNull	(Ljava/util/Collection;Ljava/lang/Object;)V
    //   1169: aload 104
    //   1171: astore 107
    //   1173: aload 87
    //   1175: astore 108
    //   1177: aload 107
    //   1179: aload 108
    //   1181: invokestatic 677	com/google/android/music/store/SearchHandler:addIfNotNull	(Ljava/util/Collection;Ljava/lang/Object;)V
    //   1184: aload 104
    //   1186: astore 109
    //   1188: aload 7
    //   1190: astore 110
    //   1192: aload 109
    //   1194: aload 110
    //   1196: invokestatic 677	com/google/android/music/store/SearchHandler:addIfNotNull	(Ljava/util/Collection;Ljava/lang/Object;)V
    //   1199: aload 104
    //   1201: astore 111
    //   1203: aload 4
    //   1205: astore 112
    //   1207: aload 111
    //   1209: aload 112
    //   1211: invokestatic 677	com/google/android/music/store/SearchHandler:addIfNotNull	(Ljava/util/Collection;Ljava/lang/Object;)V
    //   1214: aload 104
    //   1216: astore 113
    //   1218: aload 93
    //   1220: astore 114
    //   1222: aload 113
    //   1224: aload 114
    //   1226: invokestatic 677	com/google/android/music/store/SearchHandler:addIfNotNull	(Ljava/util/Collection;Ljava/lang/Object;)V
    //   1229: aload 104
    //   1231: astore 115
    //   1233: aload 8
    //   1235: astore 116
    //   1237: aload 115
    //   1239: aload 116
    //   1241: invokestatic 677	com/google/android/music/store/SearchHandler:addIfNotNull	(Ljava/util/Collection;Ljava/lang/Object;)V
    //   1244: aload 104
    //   1246: astore 117
    //   1248: aload 5
    //   1250: astore 118
    //   1252: aload 117
    //   1254: aload 118
    //   1256: invokestatic 677	com/google/android/music/store/SearchHandler:addIfNotNull	(Ljava/util/Collection;Ljava/lang/Object;)V
    //   1259: aload 104
    //   1261: astore 119
    //   1263: aload 103
    //   1265: astore 120
    //   1267: aload 119
    //   1269: aload 120
    //   1271: invokestatic 677	com/google/android/music/store/SearchHandler:addIfNotNull	(Ljava/util/Collection;Ljava/lang/Object;)V
    //   1274: aload 104
    //   1276: astore 121
    //   1278: aload 9
    //   1280: astore 122
    //   1282: aload 121
    //   1284: aload 122
    //   1286: invokestatic 677	com/google/android/music/store/SearchHandler:addIfNotNull	(Ljava/util/Collection;Ljava/lang/Object;)V
    //   1289: aload 104
    //   1291: astore 123
    //   1293: aload 96
    //   1295: astore 124
    //   1297: aload 123
    //   1299: aload 124
    //   1301: invokestatic 677	com/google/android/music/store/SearchHandler:addIfNotNull	(Ljava/util/Collection;Ljava/lang/Object;)V
    //   1304: aload 104
    //   1306: astore 125
    //   1308: aload 6
    //   1310: astore 126
    //   1312: aload 125
    //   1314: aload 126
    //   1316: invokestatic 677	com/google/android/music/store/SearchHandler:addIfNotNull	(Ljava/util/Collection;Ljava/lang/Object;)V
    //   1319: aload 104
    //   1321: astore 127
    //   1323: aload 10
    //   1325: astore 128
    //   1327: aload 127
    //   1329: aload 128
    //   1331: invokestatic 677	com/google/android/music/store/SearchHandler:addIfNotNull	(Ljava/util/Collection;Ljava/lang/Object;)V
    //   1334: new 679	com/google/android/music/store/CustomMergeCursor
    //   1337: astore_1
    //   1338: iconst_0
    //   1339: anewarray 386	android/database/Cursor
    //   1342: astore 129
    //   1344: aload 104
    //   1346: aload 129
    //   1348: invokevirtual 683	java/util/ArrayList:toArray	([Ljava/lang/Object;)[Ljava/lang/Object;
    //   1351: checkcast 685	[Landroid/database/Cursor;
    //   1354: astore 130
    //   1356: aload_1
    //   1357: aload 130
    //   1359: invokespecial 688	com/google/android/music/store/CustomMergeCursor:<init>	([Landroid/database/Cursor;)V
    //   1362: goto -1327 -> 35
    //   1365: aload 43
    //   1367: getfield 692	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mAlbum	Lcom/google/android/music/cloudclient/AlbumJson;
    //   1370: astore 131
    //   1372: aload_0
    //   1373: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   1376: astore 132
    //   1378: aload 131
    //   1380: aload 132
    //   1382: invokestatic 695	com/google/android/music/store/ProjectionUtils:project	(Lcom/google/android/music/cloudclient/AlbumJson;[Ljava/lang/String;)[Ljava/lang/Object;
    //   1385: astore 133
    //   1387: aload_1
    //   1388: aload 133
    //   1390: invokevirtual 631	android/database/MatrixCursor:addRow	([Ljava/lang/Object;)V
    //   1393: goto -1358 -> 35
    //   1396: aload 43
    //   1398: getfield 699	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mTrack	Lcom/google/android/music/sync/google/model/Track;
    //   1401: astore 134
    //   1403: aload_0
    //   1404: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   1407: astore 135
    //   1409: aload 134
    //   1411: aload 135
    //   1413: invokestatic 702	com/google/android/music/store/ProjectionUtils:project	(Lcom/google/android/music/cloudclient/TrackJson;[Ljava/lang/String;)[Ljava/lang/Object;
    //   1416: astore 136
    //   1418: aload_1
    //   1419: aload 136
    //   1421: invokevirtual 631	android/database/MatrixCursor:addRow	([Ljava/lang/Object;)V
    //   1424: goto -1389 -> 35
    //   1427: aload 43
    //   1429: getfield 706	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mGenre	Lcom/google/android/music/cloudclient/MusicGenreJson;
    //   1432: astore 137
    //   1434: aload_0
    //   1435: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   1438: astore 138
    //   1440: aload 137
    //   1442: aload 138
    //   1444: invokestatic 709	com/google/android/music/store/ProjectionUtils:project	(Lcom/google/android/music/cloudclient/MusicGenreJson;[Ljava/lang/String;)[Ljava/lang/Object;
    //   1447: astore 139
    //   1449: aload_1
    //   1450: aload 139
    //   1452: invokevirtual 631	android/database/MatrixCursor:addRow	([Ljava/lang/Object;)V
    //   1455: goto -1420 -> 35
    //   1458: new 605	android/database/MatrixCursor
    //   1461: astore 140
    //   1463: aload_0
    //   1464: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   1467: astore 141
    //   1469: aload 140
    //   1471: aload 141
    //   1473: invokespecial 608	android/database/MatrixCursor:<init>	([Ljava/lang/String;)V
    //   1476: new 605	android/database/MatrixCursor
    //   1479: astore 142
    //   1481: aload_0
    //   1482: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   1485: astore 143
    //   1487: aload 142
    //   1489: aload 143
    //   1491: invokespecial 608	android/database/MatrixCursor:<init>	([Ljava/lang/String;)V
    //   1494: new 605	android/database/MatrixCursor
    //   1497: astore 144
    //   1499: aload_0
    //   1500: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   1503: astore 145
    //   1505: aload 144
    //   1507: aload 145
    //   1509: invokespecial 608	android/database/MatrixCursor:<init>	([Ljava/lang/String;)V
    //   1512: new 605	android/database/MatrixCursor
    //   1515: astore 146
    //   1517: aload_0
    //   1518: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   1521: astore 147
    //   1523: aload 146
    //   1525: aload 147
    //   1527: invokespecial 608	android/database/MatrixCursor:<init>	([Ljava/lang/String;)V
    //   1530: new 605	android/database/MatrixCursor
    //   1533: astore 148
    //   1535: aload_0
    //   1536: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   1539: astore 149
    //   1541: aload 148
    //   1543: aload 149
    //   1545: invokespecial 608	android/database/MatrixCursor:<init>	([Ljava/lang/String;)V
    //   1548: new 605	android/database/MatrixCursor
    //   1551: astore 150
    //   1553: aload_0
    //   1554: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   1557: astore 151
    //   1559: aload 150
    //   1561: aload 151
    //   1563: invokespecial 608	android/database/MatrixCursor:<init>	([Ljava/lang/String;)V
    //   1566: new 605	android/database/MatrixCursor
    //   1569: astore 152
    //   1571: aload_0
    //   1572: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   1575: astore 153
    //   1577: aload 152
    //   1579: aload 153
    //   1581: invokespecial 608	android/database/MatrixCursor:<init>	([Ljava/lang/String;)V
    //   1584: new 605	android/database/MatrixCursor
    //   1587: astore 154
    //   1589: aload_0
    //   1590: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   1593: astore 155
    //   1595: aload 154
    //   1597: aload 155
    //   1599: invokespecial 608	android/database/MatrixCursor:<init>	([Ljava/lang/String;)V
    //   1602: new 711	java/util/LinkedList
    //   1605: dup
    //   1606: invokespecial 712	java/util/LinkedList:<init>	()V
    //   1609: astore 156
    //   1611: new 711	java/util/LinkedList
    //   1614: dup
    //   1615: invokespecial 712	java/util/LinkedList:<init>	()V
    //   1618: astore 157
    //   1620: new 711	java/util/LinkedList
    //   1623: dup
    //   1624: invokespecial 712	java/util/LinkedList:<init>	()V
    //   1627: astore 158
    //   1629: aload 37
    //   1631: getfield 551	com/google/android/music/cloudclient/SearchClientResponseJson:mEntries	Ljava/util/List;
    //   1634: invokeinterface 581 1 0
    //   1639: astore 44
    //   1641: aload 44
    //   1643: invokeinterface 586 1 0
    //   1648: ifeq +596 -> 2244
    //   1651: aload 44
    //   1653: invokeinterface 590 1 0
    //   1658: checkcast 592	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry
    //   1661: astore 45
    //   1663: aload 45
    //   1665: invokevirtual 596	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:getType	()Lcom/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry$Type;
    //   1668: astore 159
    //   1670: aload 45
    //   1672: getfield 599	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mNavigationalResult	Z
    //   1675: istore 160
    //   1677: getstatic 612	com/google/android/music/store/SearchHandler$2:$SwitchMap$com$google$android$music$cloudclient$SearchClientResponseJson$SearchClientResponseEntry$Type	[I
    //   1680: astore 161
    //   1682: aload 159
    //   1684: invokevirtual 617	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry$Type:ordinal	()I
    //   1687: istore 162
    //   1689: aload 161
    //   1691: iload 162
    //   1693: iaload
    //   1694: tableswitch	default:+30 -> 1724, 1:+33->1727, 2:+205->1899, 3:+329->2023, 4:+453->2147
    //   1725: impdep2
    //   1726: lreturn
    //   1727: iload 19
    //   1729: ifeq -88 -> 1641
    //   1732: aload 45
    //   1734: getfield 621	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mArtist	Lcom/google/android/music/cloudclient/ArtistJson;
    //   1737: ifnull -96 -> 1641
    //   1740: aload 45
    //   1742: getfield 621	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mArtist	Lcom/google/android/music/cloudclient/ArtistJson;
    //   1745: getfield 717	com/google/android/music/cloudclient/ArtistJson:mArtistId	Ljava/lang/String;
    //   1748: ifnull -107 -> 1641
    //   1751: aload 45
    //   1753: getfield 621	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mArtist	Lcom/google/android/music/cloudclient/ArtistJson;
    //   1756: astore 163
    //   1758: aload_2
    //   1759: astore 164
    //   1761: aload 163
    //   1763: astore 165
    //   1765: aload 164
    //   1767: aload 165
    //   1769: invokestatic 723	com/google/android/music/store/NautilusContentProviderHelper:generateLocalId	(Lcom/google/android/music/store/TagNormalizer;Lcom/google/android/music/cloudclient/ArtistJson;)J
    //   1772: invokestatic 728	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   1775: astore 166
    //   1777: aload 156
    //   1779: aload 166
    //   1781: invokeinterface 729 2 0
    //   1786: istore 167
    //   1788: iload 160
    //   1790: ifeq +84 -> 1874
    //   1793: aload 140
    //   1795: invokevirtual 730	android/database/MatrixCursor:getCount	()I
    //   1798: ifne +76 -> 1874
    //   1801: aload_0
    //   1802: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   1805: astore 168
    //   1807: aload 163
    //   1809: aload 168
    //   1811: invokestatic 627	com/google/android/music/store/ProjectionUtils:project	(Lcom/google/android/music/cloudclient/ArtistJson;[Ljava/lang/String;)[Ljava/lang/Object;
    //   1814: astore 169
    //   1816: aload 140
    //   1818: aload 169
    //   1820: invokevirtual 631	android/database/MatrixCursor:addRow	([Ljava/lang/Object;)V
    //   1823: goto -182 -> 1641
    //   1826: astore 53
    //   1828: aload 158
    //   1830: astore 13
    //   1832: aload 157
    //   1834: astore 12
    //   1836: aload 156
    //   1838: astore 11
    //   1840: aload 154
    //   1842: astore 10
    //   1844: aload 152
    //   1846: astore 9
    //   1848: aload 150
    //   1850: astore 8
    //   1852: aload 148
    //   1854: astore 7
    //   1856: aload 146
    //   1858: astore 6
    //   1860: aload 144
    //   1862: astore 5
    //   1864: aload 142
    //   1866: astore 4
    //   1868: aload 140
    //   1870: astore_3
    //   1871: goto -1247 -> 624
    //   1874: aload_0
    //   1875: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   1878: astore 170
    //   1880: aload 163
    //   1882: aload 170
    //   1884: invokestatic 627	com/google/android/music/store/ProjectionUtils:project	(Lcom/google/android/music/cloudclient/ArtistJson;[Ljava/lang/String;)[Ljava/lang/Object;
    //   1887: astore 171
    //   1889: aload 148
    //   1891: aload 171
    //   1893: invokevirtual 631	android/database/MatrixCursor:addRow	([Ljava/lang/Object;)V
    //   1896: goto -255 -> 1641
    //   1899: iload 21
    //   1901: ifeq -260 -> 1641
    //   1904: aload 45
    //   1906: getfield 692	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mAlbum	Lcom/google/android/music/cloudclient/AlbumJson;
    //   1909: ifnull -268 -> 1641
    //   1912: aload 45
    //   1914: getfield 692	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mAlbum	Lcom/google/android/music/cloudclient/AlbumJson;
    //   1917: getfield 735	com/google/android/music/cloudclient/AlbumJson:mAlbumId	Ljava/lang/String;
    //   1920: ifnull -279 -> 1641
    //   1923: aload 45
    //   1925: getfield 692	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mAlbum	Lcom/google/android/music/cloudclient/AlbumJson;
    //   1928: astore 172
    //   1930: aload_2
    //   1931: astore 173
    //   1933: aload 172
    //   1935: astore 174
    //   1937: aload 173
    //   1939: aload 174
    //   1941: invokestatic 738	com/google/android/music/store/NautilusContentProviderHelper:generateLocalId	(Lcom/google/android/music/store/TagNormalizer;Lcom/google/android/music/cloudclient/AlbumJson;)J
    //   1944: invokestatic 728	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   1947: astore 175
    //   1949: aload 157
    //   1951: aload 175
    //   1953: invokeinterface 729 2 0
    //   1958: istore 176
    //   1960: iload 160
    //   1962: ifeq +36 -> 1998
    //   1965: aload 142
    //   1967: invokevirtual 730	android/database/MatrixCursor:getCount	()I
    //   1970: ifne +28 -> 1998
    //   1973: aload_0
    //   1974: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   1977: astore 177
    //   1979: aload 172
    //   1981: aload 177
    //   1983: invokestatic 695	com/google/android/music/store/ProjectionUtils:project	(Lcom/google/android/music/cloudclient/AlbumJson;[Ljava/lang/String;)[Ljava/lang/Object;
    //   1986: astore 178
    //   1988: aload 142
    //   1990: aload 178
    //   1992: invokevirtual 631	android/database/MatrixCursor:addRow	([Ljava/lang/Object;)V
    //   1995: goto -354 -> 1641
    //   1998: aload_0
    //   1999: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   2002: astore 179
    //   2004: aload 172
    //   2006: aload 179
    //   2008: invokestatic 695	com/google/android/music/store/ProjectionUtils:project	(Lcom/google/android/music/cloudclient/AlbumJson;[Ljava/lang/String;)[Ljava/lang/Object;
    //   2011: astore 180
    //   2013: aload 150
    //   2015: aload 180
    //   2017: invokevirtual 631	android/database/MatrixCursor:addRow	([Ljava/lang/Object;)V
    //   2020: goto -379 -> 1641
    //   2023: iload 23
    //   2025: ifeq -384 -> 1641
    //   2028: aload 45
    //   2030: getfield 699	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mTrack	Lcom/google/android/music/sync/google/model/Track;
    //   2033: ifnull -392 -> 1641
    //   2036: aload 45
    //   2038: getfield 699	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mTrack	Lcom/google/android/music/sync/google/model/Track;
    //   2041: getfield 743	com/google/android/music/sync/google/model/Track:mNautilusId	Ljava/lang/String;
    //   2044: ifnull -403 -> 1641
    //   2047: aload 45
    //   2049: getfield 699	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mTrack	Lcom/google/android/music/sync/google/model/Track;
    //   2052: astore 181
    //   2054: aload_2
    //   2055: astore 182
    //   2057: aload 181
    //   2059: astore 183
    //   2061: aload 182
    //   2063: aload 183
    //   2065: invokestatic 746	com/google/android/music/store/NautilusContentProviderHelper:generateLocalId	(Lcom/google/android/music/store/TagNormalizer;Lcom/google/android/music/sync/google/model/Track;)J
    //   2068: invokestatic 728	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   2071: astore 184
    //   2073: aload 158
    //   2075: aload 184
    //   2077: invokeinterface 729 2 0
    //   2082: istore 185
    //   2084: iload 160
    //   2086: ifeq +36 -> 2122
    //   2089: aload 144
    //   2091: invokevirtual 730	android/database/MatrixCursor:getCount	()I
    //   2094: ifne +28 -> 2122
    //   2097: aload_0
    //   2098: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   2101: astore 186
    //   2103: aload 181
    //   2105: aload 186
    //   2107: invokestatic 702	com/google/android/music/store/ProjectionUtils:project	(Lcom/google/android/music/cloudclient/TrackJson;[Ljava/lang/String;)[Ljava/lang/Object;
    //   2110: astore 187
    //   2112: aload 144
    //   2114: aload 187
    //   2116: invokevirtual 631	android/database/MatrixCursor:addRow	([Ljava/lang/Object;)V
    //   2119: goto -478 -> 1641
    //   2122: aload_0
    //   2123: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   2126: astore 188
    //   2128: aload 181
    //   2130: aload 188
    //   2132: invokestatic 702	com/google/android/music/store/ProjectionUtils:project	(Lcom/google/android/music/cloudclient/TrackJson;[Ljava/lang/String;)[Ljava/lang/Object;
    //   2135: astore 189
    //   2137: aload 152
    //   2139: aload 189
    //   2141: invokevirtual 631	android/database/MatrixCursor:addRow	([Ljava/lang/Object;)V
    //   2144: goto -503 -> 1641
    //   2147: iload 27
    //   2149: ifeq -508 -> 1641
    //   2152: aload 45
    //   2154: getfield 706	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mGenre	Lcom/google/android/music/cloudclient/MusicGenreJson;
    //   2157: ifnull -516 -> 1641
    //   2160: aload 45
    //   2162: getfield 706	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mGenre	Lcom/google/android/music/cloudclient/MusicGenreJson;
    //   2165: getfield 751	com/google/android/music/cloudclient/MusicGenreJson:mId	Ljava/lang/String;
    //   2168: invokestatic 756	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   2171: ifne -530 -> 1641
    //   2174: aload 45
    //   2176: getfield 706	com/google/android/music/cloudclient/SearchClientResponseJson$SearchClientResponseEntry:mGenre	Lcom/google/android/music/cloudclient/MusicGenreJson;
    //   2179: astore 190
    //   2181: iload 160
    //   2183: ifeq +36 -> 2219
    //   2186: aload 146
    //   2188: invokevirtual 730	android/database/MatrixCursor:getCount	()I
    //   2191: ifne +28 -> 2219
    //   2194: aload_0
    //   2195: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   2198: astore 191
    //   2200: aload 190
    //   2202: aload 191
    //   2204: invokestatic 709	com/google/android/music/store/ProjectionUtils:project	(Lcom/google/android/music/cloudclient/MusicGenreJson;[Ljava/lang/String;)[Ljava/lang/Object;
    //   2207: astore 192
    //   2209: aload 146
    //   2211: aload 192
    //   2213: invokevirtual 631	android/database/MatrixCursor:addRow	([Ljava/lang/Object;)V
    //   2216: goto -575 -> 1641
    //   2219: aload_0
    //   2220: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   2223: astore 193
    //   2225: aload 190
    //   2227: aload 193
    //   2229: invokestatic 709	com/google/android/music/store/ProjectionUtils:project	(Lcom/google/android/music/cloudclient/MusicGenreJson;[Ljava/lang/String;)[Ljava/lang/Object;
    //   2232: astore 194
    //   2234: aload 154
    //   2236: aload 194
    //   2238: invokevirtual 631	android/database/MatrixCursor:addRow	([Ljava/lang/Object;)V
    //   2241: goto -600 -> 1641
    //   2244: aload 158
    //   2246: astore 13
    //   2248: aload 157
    //   2250: astore 12
    //   2252: aload 156
    //   2254: astore 11
    //   2256: aload 154
    //   2258: astore 10
    //   2260: aload 152
    //   2262: astore 9
    //   2264: aload 150
    //   2266: astore 8
    //   2268: aload 148
    //   2270: astore 7
    //   2272: aload 146
    //   2274: astore 6
    //   2276: aload 144
    //   2278: astore 5
    //   2280: aload 142
    //   2282: astore 4
    //   2284: aload 140
    //   2286: astore_3
    //   2287: goto -1640 -> 647
    //   2290: ldc_w 570
    //   2293: ldc_w 758
    //   2296: invokestatic 761	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   2299: istore 195
    //   2301: goto -1654 -> 647
    //   2304: iload 16
    //   2306: ifeq -1659 -> 647
    //   2309: new 605	android/database/MatrixCursor
    //   2312: astore_1
    //   2313: aload_0
    //   2314: getfield 306	com/google/android/music/store/SearchHandler:mProjection	[Ljava/lang/String;
    //   2317: astore 196
    //   2319: aload_1
    //   2320: aload 196
    //   2322: invokespecial 608	android/database/MatrixCursor:<init>	([Ljava/lang/String;)V
    //   2325: goto -2290 -> 35
    //   2328: new 65	java/lang/StringBuilder
    //   2331: dup
    //   2332: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   2335: ldc_w 471
    //   2338: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2341: astore 197
    //   2343: aload 11
    //   2345: astore 198
    //   2347: ldc_w 763
    //   2350: aload 198
    //   2352: invokestatic 767	com/google/android/music/utils/DbUtils:getNotInClause	(Ljava/lang/String;Ljava/util/Collection;)Ljava/lang/String;
    //   2355: astore 199
    //   2357: aload 197
    //   2359: aload 199
    //   2361: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2364: ldc_w 471
    //   2367: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2370: astore 200
    //   2372: aload 11
    //   2374: astore 201
    //   2376: ldc_w 769
    //   2379: aload 201
    //   2381: invokestatic 767	com/google/android/music/utils/DbUtils:getNotInClause	(Ljava/lang/String;Ljava/util/Collection;)Ljava/lang/String;
    //   2384: astore 202
    //   2386: aload 200
    //   2388: aload 202
    //   2390: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2393: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2396: astore 57
    //   2398: goto -1731 -> 667
    //   2401: new 65	java/lang/StringBuilder
    //   2404: dup
    //   2405: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   2408: ldc_w 471
    //   2411: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2414: astore 203
    //   2416: aload 12
    //   2418: astore 204
    //   2420: ldc_w 771
    //   2423: aload 204
    //   2425: invokestatic 767	com/google/android/music/utils/DbUtils:getNotInClause	(Ljava/lang/String;Ljava/util/Collection;)Ljava/lang/String;
    //   2428: astore 205
    //   2430: aload 203
    //   2432: aload 205
    //   2434: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2437: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2440: astore 58
    //   2442: goto -1755 -> 687
    //   2445: new 65	java/lang/StringBuilder
    //   2448: dup
    //   2449: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   2452: ldc_w 471
    //   2455: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2458: astore 206
    //   2460: aload 13
    //   2462: astore 207
    //   2464: ldc_w 773
    //   2467: aload 207
    //   2469: invokestatic 767	com/google/android/music/utils/DbUtils:getNotInClause	(Ljava/lang/String;Ljava/util/Collection;)Ljava/lang/String;
    //   2472: astore 208
    //   2474: aload 206
    //   2476: aload 208
    //   2478: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2481: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2484: astore 59
    //   2486: goto -1779 -> 707
    //   2489: aconst_null
    //   2490: astore 87
    //   2492: goto -1467 -> 1025
    //   2495: aconst_null
    //   2496: astore 93
    //   2498: goto -1432 -> 1066
    //   2501: aconst_null
    //   2502: astore 96
    //   2504: goto -1415 -> 1089
    //   2507: aconst_null
    //   2508: astore 103
    //   2510: goto -1376 -> 1134
    //   2513: astore 209
    //   2515: aload_0
    //   2516: getfield 296	com/google/android/music/store/SearchHandler:mStore	Lcom/google/android/music/store/Store;
    //   2519: aload 81
    //   2521: invokevirtual 670	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   2524: aload 209
    //   2526: athrow
    //   2527: astore 53
    //   2529: aload 140
    //   2531: astore_3
    //   2532: goto -1908 -> 624
    //   2535: astore 53
    //   2537: aload 142
    //   2539: astore 4
    //   2541: aload 140
    //   2543: astore_3
    //   2544: goto -1920 -> 624
    //   2547: astore 53
    //   2549: aload 144
    //   2551: astore 5
    //   2553: aload 142
    //   2555: astore 4
    //   2557: aload 140
    //   2559: astore_3
    //   2560: goto -1936 -> 624
    //   2563: astore 53
    //   2565: aload 146
    //   2567: astore 6
    //   2569: aload 144
    //   2571: astore 5
    //   2573: aload 142
    //   2575: astore 4
    //   2577: aload 140
    //   2579: astore_3
    //   2580: goto -1956 -> 624
    //   2583: astore 53
    //   2585: aload 148
    //   2587: astore 7
    //   2589: aload 146
    //   2591: astore 6
    //   2593: aload 144
    //   2595: astore 5
    //   2597: aload 142
    //   2599: astore 4
    //   2601: aload 140
    //   2603: astore_3
    //   2604: goto -1980 -> 624
    //   2607: astore 53
    //   2609: aload 150
    //   2611: astore 8
    //   2613: aload 148
    //   2615: astore 7
    //   2617: aload 146
    //   2619: astore 6
    //   2621: aload 144
    //   2623: astore 5
    //   2625: aload 142
    //   2627: astore 4
    //   2629: aload 140
    //   2631: astore_3
    //   2632: goto -2008 -> 624
    //   2635: astore 53
    //   2637: aload 152
    //   2639: astore 9
    //   2641: aload 150
    //   2643: astore 8
    //   2645: aload 148
    //   2647: astore 7
    //   2649: aload 146
    //   2651: astore 6
    //   2653: aload 144
    //   2655: astore 5
    //   2657: aload 142
    //   2659: astore 4
    //   2661: aload 140
    //   2663: astore_3
    //   2664: goto -2040 -> 624
    //   2667: astore 53
    //   2669: aload 154
    //   2671: astore 10
    //   2673: aload 152
    //   2675: astore 9
    //   2677: aload 150
    //   2679: astore 8
    //   2681: aload 148
    //   2683: astore 7
    //   2685: aload 146
    //   2687: astore 6
    //   2689: aload 144
    //   2691: astore 5
    //   2693: aload 142
    //   2695: astore 4
    //   2697: aload 140
    //   2699: astore_3
    //   2700: goto -2076 -> 624
    //   2703: astore 53
    //   2705: aload 156
    //   2707: astore 11
    //   2709: aload 154
    //   2711: astore 10
    //   2713: aload 152
    //   2715: astore 9
    //   2717: aload 150
    //   2719: astore 8
    //   2721: aload 148
    //   2723: astore 7
    //   2725: aload 146
    //   2727: astore 6
    //   2729: aload 144
    //   2731: astore 5
    //   2733: aload 142
    //   2735: astore 4
    //   2737: aload 140
    //   2739: astore_3
    //   2740: goto -2116 -> 624
    //   2743: astore 53
    //   2745: aload 157
    //   2747: astore 12
    //   2749: aload 156
    //   2751: astore 11
    //   2753: aload 154
    //   2755: astore 10
    //   2757: aload 152
    //   2759: astore 9
    //   2761: aload 150
    //   2763: astore 8
    //   2765: aload 148
    //   2767: astore 7
    //   2769: aload 146
    //   2771: astore 6
    //   2773: aload 144
    //   2775: astore 5
    //   2777: aload 142
    //   2779: astore 4
    //   2781: aload 140
    //   2783: astore_3
    //   2784: goto -2160 -> 624
    //
    // Exception table:
    //   from	to	target	type
    //   258	619	622	java/lang/Exception
    //   1365	1476	622	java/lang/Exception
    //   2290	2301	622	java/lang/Exception
    //   1629	1826	1826	java/lang/Exception
    //   947	1130	2513	finally
    //   1476	1494	2527	java/lang/Exception
    //   1494	1512	2535	java/lang/Exception
    //   1512	1530	2547	java/lang/Exception
    //   1530	1548	2563	java/lang/Exception
    //   1548	1566	2583	java/lang/Exception
    //   1566	1584	2607	java/lang/Exception
    //   1584	1602	2635	java/lang/Exception
    //   1602	1611	2667	java/lang/Exception
    //   1611	1620	2703	java/lang/Exception
    //   1620	1629	2743	java/lang/Exception
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.SearchHandler
 * JD-Core Version:    0.6.2
 */