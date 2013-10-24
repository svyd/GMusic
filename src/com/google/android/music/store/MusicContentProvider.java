package com.google.android.music.store;

import android.accounts.Account;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.google.android.music.download.artwork.ArtDownloadService;
import com.google.android.music.download.cache.CacheUtils.AlbumArtWorkCachePathResolver;
import com.google.android.music.download.cache.CacheUtils.ArtworkPathResolver;
import com.google.android.music.download.cache.CacheUtils.NonAlbumArtWorkCachePathResolver;
import com.google.android.music.eventlog.MusicEventConstants;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.net.NetworkMonitorServiceConnection;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.SearchActivity;
import com.google.android.music.utils.AlbumArtUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.PostFroyoUtils.CancellationSignalComp;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MusicContentProvider extends ContentProvider
{
  private static final String[] DEFAULT_SEARCH_SUGGESTIONS_PROJECTION;
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.CONTENT_PROVIDER);
  private static HashMap<String, String> sAlbumArtistClustersProjectionMap;
  private static HashMap<String, String> sAlbumArtistsProjectionMap;
  private static HashMap<String, String> sAlbumsProjectionMap;
  private static HashMap<String, String> sAutoPlaylistMembersProjectionMap;
  private static HashMap<String, String> sAutoPlaylistsProjectionMap;
  private static HashMap<String, String> sGenresProjectionMap;
  private static HashMap<String, String> sGroupedMusicProjectionMap;
  private static HashMap<String, String> sKeepOnProjectionMap;
  private static HashMap<String, String> sMusicProjectionMap;
  private static HashMap<String, String> sPlaylistMembersProjectionMap;
  private static HashMap<String, String> sPlaylistsProjectionMap;
  private static HashMap<String, String> sRadioProjectionMap;
  private static HashMap<String, String> sRecentProjectionMap;
  private static HashMap<String, String> sSuggestedSeedsMap;
  private static HashMap<String, String> sTrackArtistsProjectionMap;
  private static final UriMatcher sUriMatcher;
  private ThreadPoolExecutor mExecutor;
  private NetworkMonitorServiceConnection mNetworkMonitorConnection;

  static
  {
    String[] arrayOfString = new String[9];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "suggest_text_1";
    arrayOfString[2] = "suggest_text_2";
    arrayOfString[3] = "suggest_icon_1";
    arrayOfString[4] = "suggest_icon_large";
    arrayOfString[5] = "suggest_intent_data";
    arrayOfString[6] = "suggest_intent_data_id";
    arrayOfString[7] = "suggest_shortcut_id";
    arrayOfString[8] = "suggest_last_access_hint";
    DEFAULT_SEARCH_SUGGESTIONS_PROJECTION = arrayOfString;
    sUriMatcher = new UriMatcher(-1);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "audio", 300);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "audio/selected/*", 306);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "audio/*", 301);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "DownloadQueue", 1000);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "play", 305);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "search/search_suggest_query", 1103);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "search/search_suggest_query/*", 1102);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "search", 1101);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "search/*", 1100);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "album", 400);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "album/artists", 402);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "album/store", 403);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "album/*", 401);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "album/*/audio", 302);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "artists", 500);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "artists/*", 501);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "artists/*/album", 502);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "artists/*/audio", 303);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "artists/*/topsongs", 503);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "artists/*/artists", 504);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "artists/*/artUrl", 505);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "playlists", 600);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "playlists/suggested", 604);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "playlists/recent", 605);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "playlists/radio_stations", 606);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "playlists/#", 601);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "playlists/#/members", 602);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "playlists/#/members/#", 603);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "auto_playlists", 620);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "auto_playlists/#", 621);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "auto_playlists/#/members", 622);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "genres", 700);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "genres/#/members", 701);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "genres/#/album", 702);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "genres/album", 703);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "genres/#", 704);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "albumart/#", 800);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "playlistfauxart/#", 802);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "artistfauxart/#", 801);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "albumorfauxart/#", 803);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "largealbumart/#", 805);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "albumfauxart/#", 804);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "cachedart", 806);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "Recent", 900);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "Mainstage", 1800);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "KeepOn", 950);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "KeepOn/#", 951);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "MediaStore/audio/#", 1200);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "account", 1300);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "isNautilusEnabled", 1301);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "seeds", 1400);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "seeds/#", 1401);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "remote", 1500);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "explore/recommended", 1600);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "explore/recommended/#", 1601);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "explore/topcharts", 1610);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "explore/topcharts/#", 1611);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "explore/newreleases", 1620);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "explore/newreleases/#", 1621);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "explore/genres", 1630);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "explore/genres/*", 1631);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "radio_stations", 1700);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "radio_stations/#", 1701);
    sUriMatcher.addURI("com.google.android.music.MusicContent", "shared_with_me_playlist/*", 1900);
    Uri localUri1 = MusicContent.CONTENT_URI.buildUpon().appendPath("albumorfauxart").build();
    Uri localUri2 = MusicContent.CONTENT_URI.buildUpon().appendPath("playlistfauxart").build();
    sMusicProjectionMap = new HashMap();
    addMapping(sMusicProjectionMap, "_id", "MUSIC.Id");
    addMapping(sMusicProjectionMap, "audio_id", "MUSIC.Id");
    addAudioMapping(sMusicProjectionMap, false);
    addExistsAndCountMapping(sMusicProjectionMap, "count(*)");
    addCategoryMappings(sMusicProjectionMap, true, false);
    sGroupedMusicProjectionMap = new HashMap();
    addMapping(sGroupedMusicProjectionMap, "_id", "MUSIC.Id");
    addMapping(sGroupedMusicProjectionMap, "audio_id", "MUSIC.Id");
    addAudioMapping(sGroupedMusicProjectionMap, true);
    addExistsAndCountMapping(sGroupedMusicProjectionMap, "count(distinct(SongId))");
    addCategoryMappings(sGroupedMusicProjectionMap, true, true);
    sGenresProjectionMap = new HashMap();
    addMapping(sGenresProjectionMap, "_id", "GenreId");
    addMapping(sGenresProjectionMap, "name", "Genre");
    addMapping(sGenresProjectionMap, "CanonicalGenre");
    addMapping(sGenresProjectionMap, "album", "Album");
    addMapping(sGenresProjectionMap, "AlbumArtist", "AlbumArtist");
    addMapping(sGenresProjectionMap, "album_id", "AlbumId");
    addMapping(sGenresProjectionMap, "SongCount", "count(distinct MUSIC.SongId)");
    addMapping(sGenresProjectionMap, "genreServerId", "null");
    addMapping(sGenresProjectionMap, "subgenreCount", "null");
    addExistsAndCountMapping(sGenresProjectionMap, "count(distinct GenreId)");
    addCategoryMappings(sGenresProjectionMap, false, true);
    sAlbumArtistsProjectionMap = new HashMap();
    addMapping(sAlbumArtistsProjectionMap, "_id", "AlbumArtistId");
    addMapping(sAlbumArtistsProjectionMap, "artist", "AlbumArtist");
    addMapping(sAlbumArtistsProjectionMap, "artistSort", "CanonicalAlbumArtist");
    addMapping(sAlbumArtistsProjectionMap, "isTrackOnly", "0");
    addExistsAndCountMapping(sAlbumArtistsProjectionMap, "count(distinct AlbumArtistId)");
    addCategoryMappings(sAlbumArtistsProjectionMap, true, true);
    addMapping(sAlbumArtistsProjectionMap, "ArtistMetajamId", "(SELECT m1.ArtistMetajamId FROM MUSIC as m1  WHERE m1.ArtistId=MUSIC.AlbumArtistId AND m1.ArtistMetajamId NOT NULL  GROUP BY m1.ArtistMetajamId ORDER BY count(1) DESC LIMIT 1)");
    addMapping(sAlbumArtistsProjectionMap, "artworkUrl", "(SELECT m1.ArtistArtLocation FROM MUSIC as m1  WHERE m1.ArtistId=MUSIC.AlbumArtistId AND m1.ArtistMetajamId NOT NULL  GROUP BY m1.ArtistMetajamId ORDER BY count(1) DESC LIMIT 1)");
    sTrackArtistsProjectionMap = new HashMap();
    addMapping(sTrackArtistsProjectionMap, "_id", "ArtistId");
    addMapping(sTrackArtistsProjectionMap, "artist", "Artist");
    addMapping(sTrackArtistsProjectionMap, "artistSort", "CanonicalArtist");
    addMapping(sTrackArtistsProjectionMap, "isTrackOnly", "1");
    addMapping(sTrackArtistsProjectionMap, "artworkUrl", "max(ArtistArtLocation)");
    addExistsAndCountMapping(sTrackArtistsProjectionMap, "count(distinct ArtistId)");
    addCategoryMappings(sTrackArtistsProjectionMap, false, true);
    addMapping(sTrackArtistsProjectionMap, "KeepOnId", "null");
    addMapping(sTrackArtistsProjectionMap, "ArtistMetajamId", "max(ArtistMetajamId)");
    sAlbumsProjectionMap = new HashMap();
    addMapping(sAlbumsProjectionMap, "_id", "MUSIC.AlbumId");
    addMapping(sAlbumsProjectionMap, "album_id", "MUSIC.AlbumId");
    addMapping(sAlbumsProjectionMap, "album_name", "Album");
    addMapping(sAlbumsProjectionMap, "album_sort", "CanonicalAlbum");
    addMapping(sAlbumsProjectionMap, "album_art", "''");
    addMapping(sAlbumsProjectionMap, "album_artist", "AlbumArtist");
    addMapping(sAlbumsProjectionMap, "album_artist_sort", "CanonicalAlbumArtist");
    addMapping(sAlbumsProjectionMap, "album_artist_id", "AlbumArtistId");
    addMapping(sAlbumsProjectionMap, "SongCount", "count(distinct MUSIC.SongId)");
    addMapping(sAlbumsProjectionMap, "HasDifferentTrackArtists", "((min(MUSIC.ArtistId) != MUSIC.AlbumArtistId) OR (max(MUSIC.ArtistId) != MUSIC.AlbumArtistId))");
    addMapping(sAlbumsProjectionMap, "isAllLocal", "NOT EXISTS(select 1 from MUSIC AS m  WHERE MUSIC.AlbumId=m.AlbumId GROUP BY m.SongId HAVING MAX(m.LocalCopyType = 0) LIMIT 1)");
    addMapping(sAlbumsProjectionMap, "StoreAlbumId", "max(StoreAlbumId)");
    addMapping(sAlbumsProjectionMap, "ArtistMetajamId", "(SELECT m1.ArtistMetajamId FROM MUSIC as m1  WHERE m1.ArtistId=MUSIC.AlbumArtistId AND m1.ArtistMetajamId NOT NULL  GROUP BY m1.ArtistMetajamId ORDER BY count(1) DESC LIMIT 1)");
    addMapping(sAlbumsProjectionMap, "hasPersistNautilus", "EXISTS(select 1 from MUSIC AS m  WHERE MUSIC.AlbumId=m.AlbumId AND +m.TrackType = 5 LIMIT 1)");
    addNullNautilusMappings(sAlbumsProjectionMap);
    addExistsAndCountMapping(sAlbumsProjectionMap, "count(distinct MUSIC.AlbumId)");
    addCategoryMappings(sAlbumsProjectionMap, true, true);
    addMapping(sAlbumsProjectionMap, "album_year", "max(Year)");
    sAlbumArtistClustersProjectionMap = new HashMap();
    addMapping(sAlbumArtistClustersProjectionMap, "_id", "c_album_id");
    addMapping(sAlbumArtistClustersProjectionMap, "album_id", "c_album_id");
    addMapping(sAlbumArtistClustersProjectionMap, "album_name", "c_album");
    addMapping(sAlbumArtistClustersProjectionMap, "album_sort", "c_canonicalAlbum");
    addMapping(sAlbumArtistClustersProjectionMap, "album_art", "''");
    addMapping(sAlbumArtistClustersProjectionMap, "album_artist", "c_artist");
    addMapping(sAlbumArtistClustersProjectionMap, "album_artist_sort", "c_canonicalArtist");
    addMapping(sAlbumArtistClustersProjectionMap, "album_artist_id", "c_artistId");
    addMapping(sAlbumArtistClustersProjectionMap, "SongCount", "c_songCount");
    addMapping(sAlbumArtistClustersProjectionMap, "KeepOnId", "KEEPON.KeepOnId");
    addMapping(sAlbumArtistClustersProjectionMap, "hasLocal", "EXISTS(select 1 from MUSIC WHERE AlbumId=c_album_id AND LocalCopyType IN (100,200,300) LIMIT 1)");
    addMapping(sAlbumArtistClustersProjectionMap, "hasRemote", "EXISTS(select 1 from MUSIC WHERE AlbumId=c_album_id AND LocalCopyType != 300 LIMIT 1)");
    sPlaylistsProjectionMap = new HashMap();
    addMapping(sPlaylistsProjectionMap, "_id", "LISTS.Id");
    addMapping(sPlaylistsProjectionMap, "playlist_name", "LISTS.Name");
    addMapping(sPlaylistsProjectionMap, "playlist_description", "Description");
    addMapping(sPlaylistsProjectionMap, "playlist_owner_name", "OwnerName");
    addMapping(sPlaylistsProjectionMap, "playlist_share_token", "ShareToken");
    addMapping(sPlaylistsProjectionMap, "playlist_art_url", "ListArtworkLocation");
    addMapping(sPlaylistsProjectionMap, "playlist_owner_profile_photo_url", "OwnerProfilePhotoUrl");
    addMapping(sPlaylistsProjectionMap, "playlist_id", "LISTS.Id");
    addExistsAndCountMapping(sPlaylistsProjectionMap, "count(*)");
    addMapping(sPlaylistsProjectionMap, "isAllLocal", "NOT EXISTS(select 1 from MUSIC AS m, LISTITEMS as i WHERE i.ListId=LISTS.Id AND m.Id=i.MusicId GROUP BY m.SongId HAVING MAX(m.LocalCopyType = 0) LIMIT 1)");
    addMapping(sPlaylistsProjectionMap, "hasLocal", "EXISTS (SELECT 1 FROM LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE (ListId=LISTS.Id) AND LocalCopyType IN (100,200,300) LIMIT 1)");
    addMapping(sPlaylistsProjectionMap, "hasRemote", "EXISTS (SELECT 1 FROM LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE (ListId=LISTS.Id) AND LocalCopyType != 300 LIMIT 1)");
    addMapping(sPlaylistsProjectionMap, "playlist_type", "ListType");
    addKeepOnMapping(sPlaylistsProjectionMap);
    sPlaylistMembersProjectionMap = new HashMap();
    addMapping(sPlaylistMembersProjectionMap, "_id", "LISTITEMS.Id");
    addMapping(sPlaylistMembersProjectionMap, "audio_id", "MUSIC.Id");
    addMapping(sPlaylistMembersProjectionMap, "playlist_id", "LISTS.Id");
    addAudioMapping(sPlaylistMembersProjectionMap, false);
    addExistsAndCountMapping(sPlaylistMembersProjectionMap, "count(*)");
    addCategoryMappings(sPlaylistMembersProjectionMap, false, false);
    sAutoPlaylistsProjectionMap = new HashMap();
    addExistsAndCountMapping(sAutoPlaylistsProjectionMap, "count(*)");
    addKeepOnMapping(sAutoPlaylistsProjectionMap);
    addMapping(sAutoPlaylistsProjectionMap, "playlist_type", "ListType");
    sAutoPlaylistMembersProjectionMap = new HashMap();
    addMapping(sAutoPlaylistMembersProjectionMap, "_id", "MUSIC.Id");
    addMapping(sAutoPlaylistMembersProjectionMap, "audio_id", "MUSIC.Id");
    addAudioMapping(sAutoPlaylistMembersProjectionMap, false);
    addExistsAndCountMapping(sAutoPlaylistMembersProjectionMap, "count(distinct MUSIC.SongId)");
    addCategoryMappings(sAutoPlaylistMembersProjectionMap, false, true);
    sRecentProjectionMap = new HashMap();
    addMapping(sRecentProjectionMap, "_id", "RecentId");
    addMapping(sRecentProjectionMap, "ItemDate", "ItemDate");
    addMapping(sRecentProjectionMap, "RecentReason");
    addMapping(sRecentProjectionMap, "album_id", "RecentAlbumId");
    addMapping(sRecentProjectionMap, "album_name", "Album");
    addMapping(sRecentProjectionMap, "album_artist", "AlbumArtist");
    addMapping(sRecentProjectionMap, "album_artist_id", "AlbumArtistId");
    addMapping(sRecentProjectionMap, "album_artist_sort", "CanonicalAlbumArtist");
    addNullMapping(sRecentProjectionMap, "album_year");
    addMapping(sRecentProjectionMap, "playlist_id", "RecentListId");
    addMapping(sRecentProjectionMap, "playlist_name", "LISTS.Name");
    addMapping(sRecentProjectionMap, "playlist_type", "ListType");
    addMapping(sRecentProjectionMap, "playlist_description", "Description");
    addMapping(sRecentProjectionMap, "playlist_owner_name", "OwnerName");
    addMapping(sRecentProjectionMap, "playlist_share_token", "ShareToken");
    addMapping(sRecentProjectionMap, "playlist_art_url", "ListArtworkLocation");
    addMapping(sRecentProjectionMap, "playlist_owner_profile_photo_url", "OwnerProfilePhotoUrl");
    addRecentAlbumOrPlaylistMapping(sRecentProjectionMap, "hasLocal", "EXISTS(select 1 from MUSIC WHERE AlbumId=RecentAlbumId AND LocalCopyType IN (100,200,300) LIMIT 1)", "EXISTS(select 1 from LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE ListId=RecentListId AND LocalCopyType IN (100,200,300) LIMIT 1)");
    addRecentAlbumOrPlaylistMapping(sRecentProjectionMap, "hasRemote", "EXISTS(select 1 from MUSIC WHERE AlbumId=RecentAlbumId AND LocalCopyType != 300 LIMIT 1)", "EXISTS(select 1 from LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE ListId=RecentListId AND LocalCopyType != 300 LIMIT 1)");
    addRecentAlbumOrPlaylistMapping(sRecentProjectionMap, "hasPersistNautilus", "EXISTS(select 1 from MUSIC WHERE AlbumId=RecentAlbumId AND TrackType=5 LIMIT 1)", "null");
    addRecentAlbumOrPlaylistMapping(sRecentProjectionMap, "KeepOnId", "(select KeepOnId from KEEPON WHERE AlbumId=RecentAlbumId LIMIT 1)", "(select KeepOnId from KEEPON WHERE ListId=RecentListId LIMIT 1)");
    addRecentAlbumOrPlaylistMapping(sRecentProjectionMap, "isAllLocal", "NOT EXISTS(select 1 from MUSIC AS m  WHERE MUSIC.AlbumId=m.AlbumId GROUP BY m.SongId HAVING MAX(m.LocalCopyType = 0) LIMIT 1)", "NOT EXISTS(select 1 from MUSIC AS m, LISTITEMS as i WHERE i.ListId=LISTS.Id AND m.Id=i.MusicId GROUP BY m.SongId HAVING MAX(m.LocalCopyType = 0) LIMIT 1)");
    addRecentAlbumOrPlaylistMapping(sRecentProjectionMap, "suggest_text_1", "Album", "LISTS.Name");
    addMapping(sRecentProjectionMap, "suggest_text_2", "AlbumArtist");
    HashMap localHashMap1 = sRecentProjectionMap;
    String str1 = "'" + localUri1 + "/' || " + "RecentAlbumId";
    String str2 = "'" + localUri2 + "/' || " + "RecentListId";
    addRecentAlbumOrPlaylistMapping(localHashMap1, "suggest_icon_1", str1, str2);
    addMapping(sRecentProjectionMap, "suggest_icon_large", "null");
    HashMap localHashMap2 = sRecentProjectionMap;
    StringBuilder localStringBuilder1 = new StringBuilder().append("'");
    String str3 = SearchActivity.SUGGEST_DATA_ALBUM.toString();
    String str4 = str3 + "'";
    StringBuilder localStringBuilder2 = new StringBuilder().append("'");
    String str5 = SearchActivity.SUGGEST_DATA_PLAYLIST.toString();
    String str6 = str5 + "'";
    addRecentAlbumOrPlaylistMapping(localHashMap2, "suggest_intent_data", str4, str6);
    addRecentAlbumOrPlaylistMapping(sRecentProjectionMap, "suggest_intent_data_id", "RecentAlbumId", "RecentListId");
    addMapping(sRecentProjectionMap, "suggest_last_access_hint", "ItemDate");
    addMapping(sRecentProjectionMap, "suggest_shortcut_id", "'_-1'");
    addExistsAndCountMapping(sRecentProjectionMap, "MIN(count(distinct RecentId),50)");
    sKeepOnProjectionMap = new HashMap();
    addMapping(sKeepOnProjectionMap, "KeepOnId", "KEEPON.KeepOnId");
    addMapping(sKeepOnProjectionMap, "ListId", "KEEPON.ListId");
    addMapping(sKeepOnProjectionMap, "AlbumId", "KEEPON.AlbumId");
    addMapping(sKeepOnProjectionMap, "ArtistId", "KEEPON.ArtistId");
    addMapping(sKeepOnProjectionMap, "AutoListId", "KEEPON.AutoListId");
    addMapping(sKeepOnProjectionMap, "DateAdded", "KEEPON.DateAdded");
    addMapping(sKeepOnProjectionMap, "songCount", "SongCount");
    addMapping(sKeepOnProjectionMap, "downloadedSongCount", "DownloadedSongCount");
    addExistsAndCountMapping(sKeepOnProjectionMap, "count(*)");
    sSuggestedSeedsMap = new HashMap();
    addMapping(sSuggestedSeedsMap, "_id", "SUGGESTED_SEEDS.Id");
    addMapping(sSuggestedSeedsMap, "SeedAudioId", "MUSIC.Id");
    addAudioMapping(sSuggestedSeedsMap, false);
    addMapping(sSuggestedSeedsMap, "SeedListId");
    addExistsAndCountMapping(sSuggestedSeedsMap, "count(*)");
    addMapping(sSuggestedSeedsMap, "hasLocal", "LocalCopyType IN (100,200,300)");
    addMapping(sSuggestedSeedsMap, "hasRemote", "1");
    sRadioProjectionMap = new HashMap();
    addMapping(sRadioProjectionMap, "_id", "Id");
    addExistsAndCountMapping(sRadioProjectionMap, "count(*)");
    addDefaultRadioMappings(sRadioProjectionMap);
  }

  private static void addAudioMapping(HashMap<String, String> paramHashMap, boolean paramBoolean)
  {
    addMapping(paramHashMap, "title", "Title");
    addMapping(paramHashMap, "CanonicalName");
    addMapping(paramHashMap, "album", "Album");
    addMapping(paramHashMap, "CanonicalAlbum");
    addMapping(paramHashMap, "album_id", "MUSIC.AlbumId");
    addMapping(paramHashMap, "artist", "Artist");
    addMapping(paramHashMap, "artist_id", "ArtistId");
    addMapping(paramHashMap, "artistSort", "CanonicalAlbumArtist");
    addMapping(paramHashMap, "StoreAlbumId");
    addMapping(paramHashMap, "AlbumArtistId", "MUSIC.AlbumArtistId");
    addMapping(paramHashMap, "AlbumArtist");
    addMapping(paramHashMap, "AlbumArtistId");
    addMapping(paramHashMap, "composer", "Composer");
    addMapping(paramHashMap, "Genre", "Genre");
    addMapping(paramHashMap, "GenreId");
    addMapping(paramHashMap, "year", "Year");
    addMapping(paramHashMap, "duration", "Duration");
    addMapping(paramHashMap, "TrackCount");
    addMapping(paramHashMap, "track", "TrackNumber");
    addMapping(paramHashMap, "DiscCount");
    addMapping(paramHashMap, "DiscNumber");
    addMapping(paramHashMap, "Compilation");
    addMapping(paramHashMap, "BitRate");
    addMapping(paramHashMap, "FileDate");
    addMapping(paramHashMap, "Size");
    addMapping(paramHashMap, "Rating");
    addMapping(paramHashMap, "StoreId");
    addMapping(paramHashMap, "SongId");
    addMapping(paramHashMap, "Domain");
    addMapping(paramHashMap, "domainParam", "null");
    addMapping(paramHashMap, "bookmark", "0");
    addMapping(paramHashMap, "is_podcast", "0");
    addMapping(paramHashMap, "is_music", "1");
    addMapping(paramHashMap, "mime_type", "'audio/*'");
    addMapping(paramHashMap, "SourceId", "MUSIC.SourceId");
    addMapping(paramHashMap, "SourceAccount", "MUSIC.SourceAccount");
    addMapping(paramHashMap, "isAllPersistentNautilus", "NOT EXISTS(select 1 from MUSIC AS m WHERE MUSIC.SongId=m.SongId AND +m.TrackType != 5 LIMIT 1)");
    addNullNautilusMappings(paramHashMap);
    if (paramBoolean)
    {
      addMapping(paramHashMap, "Nid", "max(Nid)");
      addMapping(paramHashMap, "StoreAlbumId", "max(StoreAlbumId)");
      addMapping(paramHashMap, "ArtistMetajamId", "max(ArtistMetajamId)");
      addMapping(paramHashMap, "ArtistArtLocation", "max(ArtistArtLocation)");
      return;
    }
    addMapping(paramHashMap, "Nid", "Nid");
    addMapping(paramHashMap, "StoreAlbumId", "StoreAlbumId");
    addMapping(paramHashMap, "ArtistMetajamId", "ArtistMetajamId");
    addMapping(paramHashMap, "ArtistArtLocation", "ArtistArtLocation");
  }

  static void addCategoryMappings(HashMap<String, String> paramHashMap, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean1)
      addKeepOnMapping(paramHashMap);
    String str1 = "hasLocal";
    String str2;
    String str3;
    if (paramBoolean2)
    {
      str2 = "(MAX(LocalCopyType)  IN (100,200,300))";
      addMapping(paramHashMap, str1, str2);
      str3 = "hasRemote";
      if (!paramBoolean2)
        break label59;
    }
    label59: for (String str4 = "(MIN(LocalCopyType)  != 300)"; ; str4 = "LocalCopyType != 300")
    {
      addMapping(paramHashMap, str3, str4);
      return;
      str2 = "LocalCopyType IN (100,200,300)";
      break;
    }
  }

  static void addDefaultPlaylistMappings(HashMap<String, String> paramHashMap)
  {
    addMapping(paramHashMap, "playlist_id", "LISTS.Id");
    addMapping(paramHashMap, "playlist_name", "LISTS.Name");
    addMapping(paramHashMap, "playlist_type", "ListType");
    addMapping(paramHashMap, "playlist_owner_name", "OwnerName");
    addMapping(paramHashMap, "playlist_description", "Description");
    addMapping(paramHashMap, "playlist_share_token", "ShareToken");
    addMapping(paramHashMap, "playlist_art_url", "ListArtworkLocation");
    addMapping(paramHashMap, "playlist_owner_profile_photo_url", "OwnerProfilePhotoUrl");
  }

  static void addDefaultRadioMappings(HashMap<String, String> paramHashMap)
  {
    addMapping(paramHashMap, "radio_id", "RADIO_STATIONS.Id");
    addMapping(paramHashMap, "radio_name", "RADIO_STATIONS.Name");
    addMapping(paramHashMap, "radio_description", "RADIO_STATIONS.Description");
    addMapping(paramHashMap, "radio_art", "RADIO_STATIONS.ArtworkLocation");
    addMapping(paramHashMap, "radio_seed_source_id", "RADIO_STATIONS.SeedSourceId");
    addMapping(paramHashMap, "radio_seed_source_type", "RADIO_STATIONS.SeedSourceType");
    addMapping(paramHashMap, "radio_source_id", "RADIO_STATIONS.SourceId");
  }

  static void addExistsAndCountMapping(HashMap<String, String> paramHashMap, String paramString)
  {
    addMapping(paramHashMap, "_count", paramString);
    String str = paramString + " AS " + "_count";
    Object localObject = paramHashMap.put("count(*)", str);
    addMapping(paramHashMap, "hasAny", "1");
  }

  static void addKeepOnMapping(HashMap<String, String> paramHashMap)
  {
    addMapping(paramHashMap, "KeepOnId", "KEEPON.KeepOnId");
    addMapping(paramHashMap, "keeponSongCount", "SongCount");
    addMapping(paramHashMap, "keeponDownloadedSongCount", "DownloadedSongCount");
  }

  protected static void addMapping(HashMap<String, String> paramHashMap, String paramString)
  {
    Object localObject = paramHashMap.put(paramString, paramString);
  }

  static void addMapping(HashMap<String, String> paramHashMap, String paramString1, String paramString2)
  {
    String str = paramString2 + " AS " + paramString1;
    Object localObject = paramHashMap.put(paramString1, str);
  }

  protected static void addNotNullCaseMapping(HashMap<String, String> paramHashMap, String paramString, String[] paramArrayOfString)
  {
    StringBuilder localStringBuilder1 = new StringBuilder("CASE");
    int i = 0;
    while (true)
    {
      int j = paramArrayOfString.length + -1;
      if (i >= j)
        break;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(" WHEN ");
      String str1 = paramArrayOfString[i];
      StringBuilder localStringBuilder3 = localStringBuilder1.append(str1);
      StringBuilder localStringBuilder4 = localStringBuilder1.append(" IS NOT NULL THEN ");
      int k = i + 1;
      String str2 = paramArrayOfString[k];
      StringBuilder localStringBuilder5 = localStringBuilder1.append(str2);
      i += 2;
    }
    String str3 = " END";
    addMapping(paramHashMap, paramString, str3);
  }

  protected static void addNotNullCaseMappingWithDefault(HashMap<String, String> paramHashMap, String paramString1, String paramString2, String[] paramArrayOfString)
  {
    StringBuilder localStringBuilder1 = new StringBuilder("CASE");
    int i = 0;
    while (true)
    {
      int j = paramArrayOfString.length + -1;
      if (i >= j)
        break;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(" WHEN ");
      String str1 = paramArrayOfString[i];
      StringBuilder localStringBuilder3 = localStringBuilder1.append(str1);
      StringBuilder localStringBuilder4 = localStringBuilder1.append(" IS NOT NULL THEN ");
      int k = i + 1;
      String str2 = paramArrayOfString[k];
      StringBuilder localStringBuilder5 = localStringBuilder1.append(str2);
      i += 2;
    }
    StringBuilder localStringBuilder6 = localStringBuilder1.append(" ELSE ").append(paramString2);
    String str3 = " END";
    addMapping(paramHashMap, paramString1, str3);
  }

  static void addNullAlbumMappings(HashMap<String, String> paramHashMap)
  {
    addNullMapping(paramHashMap, "album_id");
    addNullMapping(paramHashMap, "album_name");
    addNullMapping(paramHashMap, "album_sort");
    addNullMapping(paramHashMap, "album_art");
    addNullMapping(paramHashMap, "album_artist");
    addNullMapping(paramHashMap, "album_artist_id");
    addNullMapping(paramHashMap, "album_artist_sort");
    addNullMapping(paramHashMap, "StoreAlbumId");
    addNullMapping(paramHashMap, "album_year");
    addNullNautilusMappings(paramHashMap);
  }

  protected static void addNullMapping(HashMap<String, String> paramHashMap, String paramString)
  {
    String str = "null AS " + paramString;
    Object localObject = paramHashMap.put(paramString, str);
  }

  static void addNullNautilusMappings(HashMap<String, String> paramHashMap)
  {
    addNullMapping(paramHashMap, "artworkUrl");
  }

  static void addNullPlaylistMappings(HashMap<String, String> paramHashMap)
  {
    addNullMapping(paramHashMap, "playlist_id");
    addNullMapping(paramHashMap, "playlist_name");
    addNullMapping(paramHashMap, "playlist_description");
    addNullMapping(paramHashMap, "playlist_share_token");
    addNullMapping(paramHashMap, "playlist_type");
    addNullMapping(paramHashMap, "playlist_art_url");
    addNullMapping(paramHashMap, "playlist_owner_name");
    addNullMapping(paramHashMap, "playlist_owner_profile_photo_url");
  }

  static void addNullRadioMappings(HashMap<String, String> paramHashMap)
  {
    addNullMapping(paramHashMap, "radio_id");
    addNullMapping(paramHashMap, "radio_name");
    addNullMapping(paramHashMap, "radio_description");
    addNullMapping(paramHashMap, "radio_art");
    addNullMapping(paramHashMap, "radio_seed_source_id");
    addNullMapping(paramHashMap, "radio_seed_source_type");
    addNullMapping(paramHashMap, "radio_source_id");
  }

  private static void addRecentAlbumOrPlaylistMapping(HashMap<String, String> paramHashMap, String paramString1, String paramString2, String paramString3)
  {
    String[] arrayOfString = new String[4];
    arrayOfString[0] = "RecentAlbumId";
    arrayOfString[1] = paramString2;
    arrayOfString[2] = "RecentListId";
    arrayOfString[3] = paramString3;
    addNotNullCaseMapping(paramHashMap, paramString1, arrayOfString);
  }

  static StringBuilder appendAndCondition(StringBuilder paramStringBuilder, String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
    {
      if (paramStringBuilder.length() > 0)
        StringBuilder localStringBuilder1 = paramStringBuilder.append(" AND ");
      StringBuilder localStringBuilder2 = paramStringBuilder.append('(').append(paramString).append(')');
    }
    return paramStringBuilder;
  }

  private void appendMusicFilteringCondition(StringBuilder paramStringBuilder, Uri paramUri, int paramInt, boolean paramBoolean)
  {
    if (paramInt == 0)
      return;
    if (paramInt == 3)
    {
      String str1 = Filters.appendUserAllFilter(paramBoolean);
      StringBuilder localStringBuilder1 = appendAndCondition(paramStringBuilder, str1);
      return;
    }
    String str2 = Filters.FILTERS[paramInt];
    StringBuilder localStringBuilder2 = appendAndCondition(paramStringBuilder, str2);
  }

  private void appendMusicFilteringCondition(StringBuilder paramStringBuilder, Uri paramUri, boolean paramBoolean)
  {
    int i = getMusicFilterIndex(paramUri);
    appendMusicFilteringCondition(paramStringBuilder, paramUri, i, paramBoolean);
  }

  static void appendPlaylistFilteringCondition(StringBuilder paramStringBuilder, int paramInt)
  {
    if (paramInt == 0)
      return;
    String str1 = Filters.FILTERS[paramInt];
    if (TextUtils.isEmpty(str1))
      return;
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("EXISTS (SELECT 1 FROM ");
    StringBuilder localStringBuilder3 = localStringBuilder1.append("LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id) ");
    StringBuilder localStringBuilder4 = localStringBuilder1.append(" WHERE (");
    StringBuilder localStringBuilder5 = localStringBuilder1.append("ListId=LISTS.Id");
    StringBuilder localStringBuilder6 = localStringBuilder1.append(") AND ");
    StringBuilder localStringBuilder7 = localStringBuilder1.append(str1);
    StringBuilder localStringBuilder8 = localStringBuilder1.append(" LIMIT 1)");
    String str2 = localStringBuilder1.toString();
    StringBuilder localStringBuilder9 = appendAndCondition(paramStringBuilder, str2);
  }

  static void appendPlaylistFilteringCondition(StringBuilder paramStringBuilder, Uri paramUri)
  {
    int i = getMusicFilterIndex(paramUri);
    appendPlaylistFilteringCondition(paramStringBuilder, i);
  }

  private void checkHasGetAccountsPermission()
  {
    if (getContext().checkCallingOrSelfPermission("android.permission.GET_ACCOUNTS") == 0)
      return;
    int i = Log.e("MusicContentProvider", "Not enough permissions");
    StringBuilder localStringBuilder1 = new StringBuilder().append("Caller (uid:");
    int j = Binder.getCallingUid();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(j).append(", pid:");
    int k = Binder.getCallingPid();
    String str = k + ") does not have " + "android.permission.GET_ACCOUNTS" + " permission.";
    throw new SecurityException(str);
  }

  private void checkSignatureOrSystem()
  {
    PackageManager localPackageManager = getContext().getPackageManager();
    int i = Binder.getCallingUid();
    int j = Process.myUid();
    if (localPackageManager.checkSignatures(i, j) == 0);
    for (int k = 1; ; k = 0)
    {
      String[] arrayOfString = getCallerPackages();
      boolean bool = isPackageFromSystem(arrayOfString);
      MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(getContext());
      Object[] arrayOfObject = new Object[6];
      arrayOfObject[0] = "callingPackages";
      String str1 = Arrays.toString(arrayOfString);
      arrayOfObject[1] = str1;
      arrayOfObject[2] = "signaturePassed";
      String str2 = MusicEventConstants.getBooleanValue(bool);
      arrayOfObject[3] = str2;
      arrayOfObject[4] = "systemPassed";
      String str3 = MusicEventConstants.getBooleanValue(bool);
      arrayOfObject[5] = str3;
      localMusicEventLogger.trackEvent("setAccountPermissionCheck", arrayOfObject);
      if (k != 0)
        return;
      if (bool)
        return;
      StringBuilder localStringBuilder1 = new StringBuilder().append("Calling process (");
      int m = Binder.getCallingUid();
      StringBuilder localStringBuilder2 = localStringBuilder1.append(m).append("/");
      int n = Binder.getCallingPid();
      StringBuilder localStringBuilder3 = localStringBuilder2.append(n).append(") does not have same signature as this process (");
      int i1 = Process.myUid();
      StringBuilder localStringBuilder4 = localStringBuilder3.append(i1).append("/");
      int i2 = Process.myUid();
      String str4 = i2 + ")";
      throw new SecurityException(str4);
    }
  }

  private static void checkWritePermission()
  {
    int i = Binder.getCallingUid();
    int j = Process.myUid();
    if (i != j)
      return;
    StringBuilder localStringBuilder1 = new StringBuilder().append("Another application (uid: ");
    int k = Binder.getCallingUid();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(k).append(", pid:");
    int m = Binder.getCallingPid();
    String str = m + ") is attempting a write operation";
    int n = Log.e("MusicContentProvider", str);
    throw new SecurityException("Music content provider access is not allowed.");
  }

  private static Cursor countGroups(Store paramStore, String paramString1, String paramString2)
  {
    Store localStore = paramStore;
    String str1 = paramString1;
    String str2 = paramString2;
    String str3 = null;
    String[] arrayOfString = null;
    return countGroups(localStore, str1, str2, null, str3, arrayOfString);
  }

  private static Cursor countGroups(Store paramStore, String paramString1, String paramString2, String paramString3)
  {
    Store localStore = paramStore;
    String str1 = paramString1;
    String str2 = paramString2;
    String str3 = paramString3;
    String[] arrayOfString = null;
    return countGroups(localStore, str1, str2, str3, null, arrayOfString);
  }

  private static Cursor countGroups(Store paramStore, String paramString1, String paramString2, String paramString3, String paramString4, String[] paramArrayOfString)
  {
    int i = 1;
    int j;
    label21: StringBuilder localStringBuilder1;
    if (!TextUtils.isEmpty(paramString3))
    {
      j = 1;
      if (TextUtils.isEmpty(paramString4))
        break label142;
      if (i == 0)
        break label156;
      localStringBuilder1 = new StringBuilder().append("SELECT COUNT(1) AS _count FROM (SELECT 1 FROM ").append(paramString1).append(" WHERE (").append(paramString4).append(") ");
      if (j == 0)
        break label148;
    }
    Cursor localCursor;
    label142: label148: for (String str1 = " AND (" + paramString3 + ") "; ; str1 = "")
    {
      String str2 = str1 + " GROUP BY " + paramString2 + ")";
      localCursor = paramStore.executeRawQuery(str2, paramArrayOfString);
      return localCursor;
      j = 0;
      break;
      i = 0;
      break label21;
    }
    label156: StringBuilder localStringBuilder2 = new StringBuilder().append("SELECT COUNT(1) AS _count FROM (SELECT 1 FROM ").append(paramString1);
    if (j != 0);
    for (String str3 = " WHERE (" + paramString3 + ") "; ; str3 = "")
    {
      String str4 = str3 + " GROUP BY " + paramString2 + ")";
      localCursor = paramStore.executeRawQuery(str4);
      break;
    }
  }

  private String[] getCallerPackages()
  {
    PackageManager localPackageManager = getContext().getPackageManager();
    int i = Binder.getCallingUid();
    String[] arrayOfString = localPackageManager.getPackagesForUid(i);
    if (arrayOfString == null)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Calling binder id (");
      int j = Binder.getCallingUid();
      String str = j + ") did not have any packages";
      int k = Log.e("MusicContentProvider", str);
    }
    return arrayOfString;
  }

  private ParcelFileDescriptor getFauxAlbumArt(long paramLong, int paramInt1, int paramInt2)
    throws FileNotFoundException
  {
    String[] arrayOfString1 = new String[2];
    arrayOfString1[0] = "album_name";
    arrayOfString1[1] = "album_artist";
    Context localContext1 = getContext();
    Uri localUri = MusicContent.Albums.getAlbumsUri(paramLong);
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext1, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor != null);
    while (true)
    {
      try
      {
        if (!localCursor.moveToFirst())
          break label195;
        str2 = localCursor.getString(0);
        String str3 = localCursor.getString(1);
        str4 = str3;
        Store.safeClose(localCursor);
        if (Build.VERSION.SDK_INT >= 9)
        {
          Context localContext2 = getContext();
          int i = 0;
          long l1 = paramLong;
          int j = paramInt1;
          int k = paramInt2;
          localParcelFileDescriptor = AlbumArtUtils.getStaticFauxArtPipe(localContext2, i, l1, str2, str4, j, k);
          return localParcelFileDescriptor;
        }
      }
      finally
      {
        Store.safeClose(localCursor);
      }
      MusicContentProvider localMusicContentProvider = this;
      int m = 0;
      long l2 = paramLong;
      int n = paramInt1;
      int i1 = paramInt2;
      ParcelFileDescriptor localParcelFileDescriptor = localMusicContentProvider.getStaticFauxArt(m, l2, str2, str4, n, i1);
      continue;
      label195: String str4 = null;
      String str2 = null;
    }
  }

  static int getMusicFilterIndex(Uri paramUri)
  {
    String str1 = paramUri.getQueryParameter("filter");
    if (str1 != null);
    while (true)
    {
      try
      {
        int i = Integer.parseInt(str1);
        j = i;
        return j;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        String str2 = localNumberFormatException.getMessage();
        int k = Log.w("MusicContentProvider", str2, localNumberFormatException);
      }
      int j = 0;
    }
  }

  private Pair<String, Boolean> getSortOrderFromQueryParam(String paramString, boolean paramBoolean)
  {
    boolean bool1 = true;
    Object localObject;
    if ((paramString != null) && (paramString.length() > 0))
      if ("name".equals(paramString))
        localObject = "MUSIC.CanonicalName";
    while (true)
    {
      Boolean localBoolean = Boolean.valueOf(bool1);
      return new Pair(localObject, localBoolean);
      if ("album".equals(paramString))
      {
        localObject = "CanonicalAlbum, DiscNumber, TrackNumber, CanonicalName";
      }
      else if ("artist".equals(paramString))
      {
        localObject = "CanonicalArtist, CanonicalName";
      }
      else
      {
        if ("date".equals(paramString))
        {
          if (paramBoolean);
          for (String str1 = "MAX(MUSIC.FileDate) DESC "; ; str1 = "MUSIC.FileDate DESC ")
          {
            boolean bool2 = false;
            localObject = str1;
            bool1 = bool2;
            break;
          }
        }
        String str2 = "invalid sort param " + paramString;
        throw new IllegalArgumentException(str2);
        localObject = null;
      }
    }
  }

  private ParcelFileDescriptor getStaticFauxArt(int paramInt1, long paramLong, int paramInt2, int paramInt3)
    throws FileNotFoundException
  {
    MusicContentProvider localMusicContentProvider = this;
    int i = paramInt1;
    long l = paramLong;
    String str = null;
    int j = paramInt2;
    int k = paramInt3;
    return localMusicContentProvider.getStaticFauxArt(i, l, null, str, j, k);
  }

  private ParcelFileDescriptor getStaticFauxArt(int paramInt1, long paramLong, String paramString1, String paramString2, int paramInt2, int paramInt3)
    throws FileNotFoundException
  {
    Context localContext = getContext();
    int i = paramInt1;
    long l = paramLong;
    String str1 = paramString1;
    String str2 = paramString2;
    int j = paramInt2;
    int k = paramInt3;
    return ParcelFileDescriptor.open(AlbumArtUtils.getStaticFauxArtFile(localContext, i, l, str1, str2, j, k), 268435456);
  }

  private Account getStreamingAccount()
  {
    Object localObject1 = new Object();
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(getContext(), localObject1);
    try
    {
      Account localAccount1 = localMusicPreferences.getStreamingAccount();
      Account localAccount2 = localAccount1;
      return localAccount2;
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(localObject1);
    }
  }

  static boolean hasCount(String[] paramArrayOfString)
  {
    int i;
    if (paramArrayOfString != null)
    {
      i = 0;
      int j = paramArrayOfString.length;
      if (i < j)
        if (!paramArrayOfString[i].contains("count("))
        {
          String str1 = paramArrayOfString[i];
          if (!"_count".equals(str1))
            break label135;
        }
    }
    for (boolean bool = true; ; bool = false)
    {
      if ((bool) && (paramArrayOfString.length > 1))
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("Count can be the only column in the projection. ");
        int k = paramArrayOfString.length;
        StringBuilder localStringBuilder2 = localStringBuilder1.append(k).append(": ");
        String str2 = paramArrayOfString[0];
        StringBuilder localStringBuilder3 = localStringBuilder2.append(str2).append(", ");
        String str3 = paramArrayOfString[1];
        String str4 = str3;
        throw new IllegalArgumentException(str4);
        label135: i += 1;
        break;
      }
      return bool;
    }
  }

  private boolean isNautilusUser()
  {
    int i = Binder.getCallingUid();
    int j = Process.myUid();
    if ((i != j) && (!MusicPreferences.isGlass()))
      throw new SecurityException("isNautilusUser can only be accessed on from within music app or on a glass device");
    checkSignatureOrSystem();
    long l = Binder.clearCallingIdentity();
    Object localObject1 = new Object();
    try
    {
      boolean bool1 = MusicPreferences.getMusicPreferences(getContext(), localObject1).isNautilusEnabled();
      boolean bool2 = bool1;
      return bool2;
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(localObject1);
      Binder.restoreCallingIdentity(l);
    }
  }

  private boolean isPackageFromSystem(String[] paramArrayOfString)
  {
    boolean bool = false;
    if (paramArrayOfString == null);
    label106: 
    while (true)
    {
      return bool;
      PackageManager localPackageManager = getContext().getPackageManager();
      int i = paramArrayOfString.length;
      int j = 0;
      while (true)
        while (true)
        {
          if (j >= i)
            break label106;
          int k = paramArrayOfString[j];
          int n = 0;
          try
          {
            m = localPackageManager.getApplicationInfo(k, n).flags;
            if ((m & 0x1) != 0)
              bool = true;
          }
          catch (PackageManager.NameNotFoundException localNameNotFoundException)
          {
            int m;
            String str = "Could not find ApplicationInfo for package: " + m;
            int i1 = Log.w("MusicContentProvider", str);
            j += 1;
          }
        }
    }
  }

  private static boolean isPlayQueue(Store paramStore, long paramLong)
  {
    PlayList localPlayList = paramStore.getPlayQueuePlaylist();
    if ((localPlayList != null) && (localPlayList.getId() == paramLong));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private static int parseIntegerParameter(Uri paramUri, String paramString, int paramInt)
  {
    String str1 = paramUri.getQueryParameter(paramString);
    if (str1 != null);
    try
    {
      int i = Integer.parseInt(str1);
      paramInt = i;
      return paramInt;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (true)
      {
        String str2 = "could not parse param \"" + paramString + "\" from URI: " + paramUri;
        int j = Log.e("MusicContentProvider", str2);
      }
    }
  }

  // ERROR //
  private Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, PostFroyoUtils.CancellationSignalComp paramCancellationSignalComp)
  {
    // Byte code:
    //   0: getstatic 48	com/google/android/music/store/MusicContentProvider:LOGV	Z
    //   3: ifeq +70 -> 73
    //   6: iconst_5
    //   7: anewarray 855	java/lang/Object
    //   10: astore 7
    //   12: aload 7
    //   14: iconst_0
    //   15: aload_1
    //   16: aastore
    //   17: aload_2
    //   18: invokestatic 1122	com/google/android/music/utils/DebugUtils:arrayToString	([Ljava/lang/String;)Ljava/lang/String;
    //   21: astore 8
    //   23: aload 7
    //   25: iconst_1
    //   26: aload 8
    //   28: aastore
    //   29: aload 7
    //   31: iconst_2
    //   32: aload_3
    //   33: aastore
    //   34: aload 4
    //   36: invokestatic 1122	com/google/android/music/utils/DebugUtils:arrayToString	([Ljava/lang/String;)Ljava/lang/String;
    //   39: astore 9
    //   41: aload 7
    //   43: iconst_3
    //   44: aload 9
    //   46: aastore
    //   47: aload 7
    //   49: iconst_4
    //   50: aload 5
    //   52: aastore
    //   53: ldc_w 1124
    //   56: aload 7
    //   58: invokestatic 1128	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   61: astore 10
    //   63: ldc_w 793
    //   66: aload 10
    //   68: invokestatic 1131	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   71: istore 11
    //   73: aload_0
    //   74: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   77: invokestatic 1134	com/google/android/music/store/Store:getInstance	(Landroid/content/Context;)Lcom/google/android/music/store/Store;
    //   80: astore 12
    //   82: new 1136	android/database/sqlite/SQLiteQueryBuilder
    //   85: dup
    //   86: invokespecial 1137	android/database/sqlite/SQLiteQueryBuilder:<init>	()V
    //   89: astore 13
    //   91: iconst_0
    //   92: istore 14
    //   94: aconst_null
    //   95: astore 15
    //   97: aload_1
    //   98: ldc_w 1139
    //   101: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   104: astore 16
    //   106: aconst_null
    //   107: astore 17
    //   109: new 502	java/lang/StringBuilder
    //   112: dup
    //   113: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   116: astore 18
    //   118: getstatic 78	com/google/android/music/store/MusicContentProvider:sUriMatcher	Landroid/content/UriMatcher;
    //   121: astore 19
    //   123: aload_1
    //   124: astore 20
    //   126: aload 19
    //   128: aload 20
    //   130: invokevirtual 1142	android/content/UriMatcher:match	(Landroid/net/Uri;)I
    //   133: astore 21
    //   135: aconst_null
    //   136: astore 22
    //   138: aconst_null
    //   139: astore 23
    //   141: aconst_null
    //   142: astore 24
    //   144: aconst_null
    //   145: astore 25
    //   147: aconst_null
    //   148: astore 26
    //   150: aload_2
    //   151: ifnull +525 -> 676
    //   154: aload_2
    //   155: arraylength
    //   156: iconst_1
    //   157: if_icmpne +519 -> 676
    //   160: aload_2
    //   161: iconst_0
    //   162: aaload
    //   163: astore 27
    //   165: ldc_w 696
    //   168: aload 27
    //   170: invokevirtual 998	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   173: ifeq +503 -> 676
    //   176: aconst_null
    //   177: astore 28
    //   179: aload 28
    //   181: ifnull +11026 -> 11207
    //   184: ldc_w 336
    //   187: astore 16
    //   189: aconst_null
    //   190: astore 29
    //   192: aload 21
    //   194: lookupswitch	default:+442->636, 300:+3937->4131, 301:+5263->5457, 302:+4499->4693, 303:+4876->5070, 306:+5828->6022, 400:+488->682, 401:+1228->1422, 402:+1401->1595, 403:+1686->1880, 500:+1962->2156, 501:+2418->2612, 502:+2748->2942, 503:+2991->3185, 504:+3081->3275, 505:+3171->3365, 600:+7733->7927, 601:+8527->8721, 602:+8624->8818, 603:+8858->9052, 604:+8167->8361, 605:+8001->8195, 620:+6404->6598, 621:+6502->6696, 622:+6615->6809, 700:+3261->3455, 701:+3549->3743, 702:+3789->3983, 703:+3455->3649, 704:+3360->3554, 900:+9018->9212, 950:+9301->9495, 951:+9343->9537, 1000:+9441->9635, 1100:+11003->11197, 1101:+9919->10113, 1102:+9641->9835, 1103:+9007->9201, 1200:+9925->10119, 1300:+10063->10257, 1301:+10218->10412, 1400:+10317->10511, 1401:+10432->10626, 1600:+10539->10733, 1601:+10539->10733, 1610:+10539->10733, 1611:+10539->10733, 1620:+10539->10733, 1621:+10539->10733, 1630:+10539->10733, 1631:+10539->10733, 1700:+8258->8452, 1701:+8333->8527, 1800:+9253->9447, 1900:+8440->8634
    //   637: aconst_null
    //   638: <illegal opcode>
    //   639: dup
    //   640: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   643: ldc_w 1144
    //   646: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   649: astore 30
    //   651: aload_1
    //   652: astore 31
    //   654: aload 30
    //   656: aload 31
    //   658: invokevirtual 512	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   661: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   664: astore 32
    //   666: new 1025	java/lang/IllegalArgumentException
    //   669: dup
    //   670: aload 32
    //   672: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   675: athrow
    //   676: aconst_null
    //   677: astore 28
    //   679: goto -500 -> 179
    //   682: aload_1
    //   683: invokestatic 759	com/google/android/music/store/MusicContentProvider:getMusicFilterIndex	(Landroid/net/Uri;)I
    //   686: istore 33
    //   688: aload 28
    //   690: ifnull +258 -> 948
    //   693: aload 13
    //   695: ldc_w 1146
    //   698: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   701: aload 17
    //   703: astore 34
    //   705: aload 16
    //   707: astore 35
    //   709: aload 29
    //   711: astore 36
    //   713: aload_0
    //   714: astore 37
    //   716: aload 18
    //   718: astore 38
    //   720: aload_1
    //   721: astore 39
    //   723: aload 37
    //   725: aload 38
    //   727: aload 39
    //   729: iload 33
    //   731: iconst_0
    //   732: invokespecial 761	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;IZ)V
    //   735: getstatic 346	com/google/android/music/store/MusicContentProvider:sAlbumsProjectionMap	Ljava/util/HashMap;
    //   738: astore 40
    //   740: aload 24
    //   742: astore 41
    //   744: aload 34
    //   746: astore 42
    //   748: aload 35
    //   750: astore 43
    //   752: aload 36
    //   754: astore 28
    //   756: aload 4
    //   758: astore 44
    //   760: aload 40
    //   762: astore 45
    //   764: aload_2
    //   765: astore 46
    //   767: aload_3
    //   768: astore 47
    //   770: aload 45
    //   772: ifnull +49 -> 821
    //   775: aload 13
    //   777: aload 45
    //   779: invokevirtual 1153	android/database/sqlite/SQLiteQueryBuilder:setProjectionMap	(Ljava/util/Map;)V
    //   782: getstatic 48	com/google/android/music/store/MusicContentProvider:LOGV	Z
    //   785: ifeq +36 -> 821
    //   788: new 502	java/lang/StringBuilder
    //   791: dup
    //   792: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   795: ldc_w 1155
    //   798: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   801: aload 45
    //   803: invokevirtual 512	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   806: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   809: astore 48
    //   811: ldc_w 793
    //   814: aload 48
    //   816: invokestatic 1131	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   819: istore 49
    //   821: aload 18
    //   823: aload 26
    //   825: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   828: astore 50
    //   830: aload 18
    //   832: invokevirtual 736	java/lang/StringBuilder:length	()I
    //   835: ifle +56 -> 891
    //   838: aload 18
    //   840: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   843: astore 51
    //   845: getstatic 48	com/google/android/music/store/MusicContentProvider:LOGV	Z
    //   848: ifeq +36 -> 884
    //   851: new 502	java/lang/StringBuilder
    //   854: dup
    //   855: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   858: ldc_w 1157
    //   861: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   864: aload 51
    //   866: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   869: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   872: astore 52
    //   874: ldc_w 793
    //   877: aload 52
    //   879: invokestatic 1160	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   882: istore 53
    //   884: aload 13
    //   886: aload 51
    //   888: invokevirtual 1164	android/database/sqlite/SQLiteQueryBuilder:appendWhere	(Ljava/lang/CharSequence;)V
    //   891: aload 15
    //   893: ifnull +9880 -> 10773
    //   896: aload 47
    //   898: ifnull +9875 -> 10773
    //   901: aload 47
    //   903: invokevirtual 994	java/lang/String:length	()I
    //   906: ifle +9867 -> 10773
    //   909: new 502	java/lang/StringBuilder
    //   912: dup
    //   913: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   916: ldc_w 1166
    //   919: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   922: aload 47
    //   924: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   927: ldc_w 1168
    //   930: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   933: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   936: astore 54
    //   938: new 1025	java/lang/IllegalArgumentException
    //   941: dup
    //   942: aload 54
    //   944: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   947: athrow
    //   948: aload_2
    //   949: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   952: ifeq +71 -> 1023
    //   955: iload 33
    //   957: ifne +19 -> 976
    //   960: aload 12
    //   962: ldc_w 1146
    //   965: ldc_w 290
    //   968: invokestatic 1172	com/google/android/music/store/MusicContentProvider:countGroups	(Lcom/google/android/music/store/Store;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   971: astore 55
    //   973: aload 55
    //   975: areturn
    //   976: aload_0
    //   977: astore 56
    //   979: aload 18
    //   981: astore 57
    //   983: aload_1
    //   984: astore 58
    //   986: aload 56
    //   988: aload 57
    //   990: aload 58
    //   992: iload 33
    //   994: iconst_0
    //   995: invokespecial 761	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;IZ)V
    //   998: aload 18
    //   1000: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1003: astore 59
    //   1005: aload 12
    //   1007: ldc_w 1146
    //   1010: ldc_w 1174
    //   1013: aload 59
    //   1015: invokestatic 1176	com/google/android/music/store/MusicContentProvider:countGroups	(Lcom/google/android/music/store/Store;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   1018: astore 55
    //   1020: goto -47 -> 973
    //   1023: iload 33
    //   1025: iconst_4
    //   1026: if_icmpeq +9 -> 1035
    //   1029: iload 33
    //   1031: iconst_5
    //   1032: if_icmpne +245 -> 1277
    //   1035: ldc_w 1178
    //   1038: astore 60
    //   1040: aload 13
    //   1042: aload 60
    //   1044: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   1047: ldc_w 1180
    //   1050: astore 61
    //   1052: ldc_w 1180
    //   1055: astore 62
    //   1057: aload_1
    //   1058: ldc_w 1182
    //   1061: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   1064: astore 63
    //   1066: aload 63
    //   1068: ifnull +27 -> 1095
    //   1071: aload 63
    //   1073: invokevirtual 994	java/lang/String:length	()I
    //   1076: ifle +19 -> 1095
    //   1079: ldc_w 308
    //   1082: aload 63
    //   1084: invokevirtual 998	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   1087: ifeq +198 -> 1285
    //   1090: ldc_w 1184
    //   1093: astore 62
    //   1095: new 502	java/lang/StringBuilder
    //   1098: dup
    //   1099: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   1102: astore 64
    //   1104: aload_0
    //   1105: astore 65
    //   1107: aload_1
    //   1108: astore 66
    //   1110: aload 65
    //   1112: aload 64
    //   1114: aload 66
    //   1116: iload 33
    //   1118: iconst_0
    //   1119: invokespecial 761	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;IZ)V
    //   1122: aload 64
    //   1124: invokevirtual 736	java/lang/StringBuilder:length	()I
    //   1127: ifle +201 -> 1328
    //   1130: new 502	java/lang/StringBuilder
    //   1133: dup
    //   1134: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   1137: ldc_w 1186
    //   1140: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1143: astore 67
    //   1145: aload 64
    //   1147: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1150: astore 68
    //   1152: aload 67
    //   1154: aload 68
    //   1156: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1159: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1162: astore 69
    //   1164: new 502	java/lang/StringBuilder
    //   1167: dup
    //   1168: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   1171: ldc_w 1188
    //   1174: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1177: aload 69
    //   1179: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1182: astore 70
    //   1184: aload 61
    //   1186: ifnonnull +150 -> 1336
    //   1189: ldc_w 914
    //   1192: astore 71
    //   1194: aload 70
    //   1196: aload 71
    //   1198: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1201: astore 72
    //   1203: aload 62
    //   1205: ifnonnull +157 -> 1362
    //   1208: ldc_w 914
    //   1211: astore 73
    //   1213: aload 72
    //   1215: aload 73
    //   1217: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1220: astore 74
    //   1222: aload 16
    //   1224: ifnonnull +164 -> 1388
    //   1227: ldc_w 914
    //   1230: astore 75
    //   1232: aload 74
    //   1234: aload 75
    //   1236: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1239: ldc_w 902
    //   1242: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1245: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1248: astore 76
    //   1250: aload 18
    //   1252: aload 76
    //   1254: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1257: astore 77
    //   1259: aconst_null
    //   1260: astore 35
    //   1262: aload 61
    //   1264: astore 78
    //   1266: aload 62
    //   1268: astore 36
    //   1270: aload 78
    //   1272: astore 34
    //   1274: goto -561 -> 713
    //   1277: ldc_w 1190
    //   1280: astore 60
    //   1282: goto -242 -> 1040
    //   1285: ldc 104
    //   1287: aload 63
    //   1289: invokevirtual 998	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   1292: ifne -197 -> 1095
    //   1295: new 502	java/lang/StringBuilder
    //   1298: dup
    //   1299: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   1302: ldc_w 1192
    //   1305: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1308: aload 63
    //   1310: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1313: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1316: astore 79
    //   1318: new 1025	java/lang/IllegalArgumentException
    //   1321: dup
    //   1322: aload 79
    //   1324: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   1327: athrow
    //   1328: ldc_w 914
    //   1331: astore 69
    //   1333: goto -169 -> 1164
    //   1336: new 502	java/lang/StringBuilder
    //   1339: dup
    //   1340: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   1343: ldc_w 906
    //   1346: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1349: aload 61
    //   1351: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1354: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1357: astore 71
    //   1359: goto -165 -> 1194
    //   1362: new 502	java/lang/StringBuilder
    //   1365: dup
    //   1366: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   1369: ldc_w 1194
    //   1372: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1375: aload 62
    //   1377: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1380: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1383: astore 73
    //   1385: goto -172 -> 1213
    //   1388: new 502	java/lang/StringBuilder
    //   1391: dup
    //   1392: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   1395: ldc_w 1196
    //   1398: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1401: astore 80
    //   1403: aload 16
    //   1405: astore 81
    //   1407: aload 80
    //   1409: aload 81
    //   1411: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1414: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1417: astore 75
    //   1419: goto -187 -> 1232
    //   1422: aload_1
    //   1423: invokevirtual 1199	android/net/Uri:getLastPathSegment	()Ljava/lang/String;
    //   1426: astore 44
    //   1428: aload 44
    //   1430: invokestatic 1203	com/google/android/music/utils/MusicUtils:isNautilusId	(Ljava/lang/String;)Z
    //   1433: ifeq +66 -> 1499
    //   1436: aload_0
    //   1437: getfield 1205	com/google/android/music/store/MusicContentProvider:mExecutor	Ljava/util/concurrent/ThreadPoolExecutor;
    //   1440: astore 82
    //   1442: aload_0
    //   1443: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   1446: astore 83
    //   1448: aload_1
    //   1449: astore 84
    //   1451: aload_2
    //   1452: astore 85
    //   1454: aload 82
    //   1456: aload 83
    //   1458: aload 21
    //   1460: aload 84
    //   1462: aload 44
    //   1464: aload 85
    //   1466: invokestatic 1210	com/google/android/music/store/NautilusContentProviderHelper:query	(Ljava/util/concurrent/ThreadPoolExecutor;Landroid/content/Context;ILandroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)Lcom/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult;
    //   1469: astore 86
    //   1471: aload 86
    //   1473: ifnonnull +9 -> 1482
    //   1476: aconst_null
    //   1477: astore 55
    //   1479: goto -506 -> 973
    //   1482: aload 86
    //   1484: invokevirtual 1216	com/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult:getCursor	()Landroid/database/Cursor;
    //   1487: astore 25
    //   1489: aload 86
    //   1491: invokevirtual 1219	com/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult:getLocalId	()J
    //   1494: invokestatic 1224	java/lang/Long:toString	(J)Ljava/lang/String;
    //   1497: astore 44
    //   1499: aload 28
    //   1501: ifnonnull +10 -> 1511
    //   1504: aload_2
    //   1505: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   1508: ifeq +76 -> 1584
    //   1511: aload 13
    //   1513: ldc_w 1146
    //   1516: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   1519: getstatic 346	com/google/android/music/store/MusicContentProvider:sAlbumsProjectionMap	Ljava/util/HashMap;
    //   1522: astore 45
    //   1524: new 502	java/lang/StringBuilder
    //   1527: dup
    //   1528: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   1531: ldc_w 1226
    //   1534: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1537: aload 44
    //   1539: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1542: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1545: astore 87
    //   1547: aload 18
    //   1549: aload 87
    //   1551: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1554: astore 88
    //   1556: aload 17
    //   1558: astore 42
    //   1560: aload 16
    //   1562: astore 43
    //   1564: aconst_null
    //   1565: astore 28
    //   1567: aload 4
    //   1569: astore 44
    //   1571: aload_3
    //   1572: astore 47
    //   1574: aload_2
    //   1575: astore 46
    //   1577: aload 24
    //   1579: astore 41
    //   1581: goto -811 -> 770
    //   1584: aload 13
    //   1586: ldc_w 1178
    //   1589: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   1592: goto -73 -> 1519
    //   1595: aload 28
    //   1597: ifnonnull +10 -> 1607
    //   1600: aload_2
    //   1601: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   1604: ifeq +11 -> 1615
    //   1607: new 1228	java/lang/UnsupportedOperationException
    //   1610: dup
    //   1611: invokespecial 1229	java/lang/UnsupportedOperationException:<init>	()V
    //   1614: athrow
    //   1615: new 1231	java/lang/StringBuffer
    //   1618: dup
    //   1619: invokespecial 1232	java/lang/StringBuffer:<init>	()V
    //   1622: astore 89
    //   1624: getstatic 754	com/google/android/music/store/Filters:FILTERS	[Ljava/lang/String;
    //   1627: astore 90
    //   1629: aload_1
    //   1630: invokestatic 759	com/google/android/music/store/MusicContentProvider:getMusicFilterIndex	(Landroid/net/Uri;)I
    //   1633: istore 91
    //   1635: aload 90
    //   1637: iload 91
    //   1639: aaload
    //   1640: astore 92
    //   1642: aload 92
    //   1644: invokevirtual 994	java/lang/String:length	()I
    //   1647: ifle +215 -> 1862
    //   1650: iconst_1
    //   1651: istore 93
    //   1653: aload 89
    //   1655: iconst_0
    //   1656: invokevirtual 1235	java/lang/StringBuffer:setLength	(I)V
    //   1659: aload 89
    //   1661: ldc_w 1237
    //   1664: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   1667: astore 94
    //   1669: getstatic 392	com/google/android/music/store/MusicContentProvider:sAlbumArtistClustersProjectionMap	Ljava/util/HashMap;
    //   1672: astore 95
    //   1674: aload_2
    //   1675: aload 95
    //   1677: invokestatic 1246	com/google/android/music/utils/DbUtils:formatProjection	([Ljava/lang/String;Ljava/util/Map;)Ljava/lang/String;
    //   1680: astore 96
    //   1682: aload 89
    //   1684: aload 96
    //   1686: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   1689: astore 97
    //   1691: aload 89
    //   1693: ldc_w 1248
    //   1696: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   1699: astore 98
    //   1701: iload 93
    //   1703: ifeq +18 -> 1721
    //   1706: aload 89
    //   1708: ldc_w 1186
    //   1711: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   1714: aload 92
    //   1716: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   1719: astore 99
    //   1721: aload 89
    //   1723: ldc_w 1250
    //   1726: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   1729: astore 100
    //   1731: iload 93
    //   1733: ifeq +18 -> 1751
    //   1736: aload 89
    //   1738: ldc_w 738
    //   1741: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   1744: aload 92
    //   1746: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   1749: astore 101
    //   1751: aload 89
    //   1753: ldc_w 1252
    //   1756: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   1759: astore 102
    //   1761: iload 93
    //   1763: ifeq +18 -> 1781
    //   1766: aload 89
    //   1768: ldc_w 738
    //   1771: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   1774: aload 92
    //   1776: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   1779: astore 103
    //   1781: aload 89
    //   1783: ldc_w 1254
    //   1786: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   1789: astore 104
    //   1791: aload 12
    //   1793: invokevirtual 1258	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   1796: astore 105
    //   1798: aload 89
    //   1800: invokevirtual 1259	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   1803: astore 106
    //   1805: aload 105
    //   1807: aload 106
    //   1809: aconst_null
    //   1810: invokevirtual 1264	android/database/sqlite/SQLiteDatabase:rawQuery	(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   1813: astore 55
    //   1815: aload 55
    //   1817: ifnull +35 -> 1852
    //   1820: aload_0
    //   1821: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   1824: invokevirtual 1268	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   1827: astore 107
    //   1829: aload_1
    //   1830: astore 108
    //   1832: aload 55
    //   1834: aload 107
    //   1836: aload 108
    //   1838: invokeinterface 1272 3 0
    //   1843: aload 55
    //   1845: invokeinterface 1275 1 0
    //   1850: istore 109
    //   1852: aload 12
    //   1854: aload 105
    //   1856: invokevirtual 1279	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   1859: goto -886 -> 973
    //   1862: iconst_0
    //   1863: istore 93
    //   1865: goto -212 -> 1653
    //   1868: astore 110
    //   1870: aload 12
    //   1872: aload 105
    //   1874: invokevirtual 1279	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   1877: aload 110
    //   1879: athrow
    //   1880: aload_1
    //   1881: ldc_w 1281
    //   1884: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   1887: astore 111
    //   1889: aload 111
    //   1891: invokestatic 732	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   1894: ifne +125 -> 2019
    //   1897: iconst_1
    //   1898: istore 112
    //   1900: iload 112
    //   1902: ifeq +123 -> 2025
    //   1905: new 502	java/lang/StringBuilder
    //   1908: dup
    //   1909: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   1912: ldc_w 1283
    //   1915: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1918: astore 113
    //   1920: aload 111
    //   1922: invokestatic 1286	com/google/android/music/utils/DbUtils:quoteStringValue	(Ljava/lang/String;)Ljava/lang/String;
    //   1925: astore 114
    //   1927: aload 113
    //   1929: aload 114
    //   1931: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1934: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1937: astore 115
    //   1939: aload 18
    //   1941: aload 115
    //   1943: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1946: astore 116
    //   1948: aload 18
    //   1950: ldc_w 1288
    //   1953: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1956: astore 117
    //   1958: aload 28
    //   1960: ifnonnull +10 -> 1970
    //   1963: aload_2
    //   1964: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   1967: ifeq +81 -> 2048
    //   1970: aload 13
    //   1972: ldc_w 1146
    //   1975: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   1978: aconst_null
    //   1979: astore 118
    //   1981: getstatic 346	com/google/android/music/store/MusicContentProvider:sAlbumsProjectionMap	Ljava/util/HashMap;
    //   1984: astore 119
    //   1986: aload 17
    //   1988: astore 42
    //   1990: aload 16
    //   1992: astore 43
    //   1994: aload 118
    //   1996: astore 28
    //   1998: aload 4
    //   2000: astore 44
    //   2002: aload_3
    //   2003: astore 47
    //   2005: aload_2
    //   2006: astore 46
    //   2008: aload 24
    //   2010: astore 41
    //   2012: aload 119
    //   2014: astore 45
    //   2016: goto -1246 -> 770
    //   2019: iconst_0
    //   2020: istore 112
    //   2022: goto -122 -> 1900
    //   2025: aload_0
    //   2026: astore 120
    //   2028: aload 18
    //   2030: astore 121
    //   2032: aload_1
    //   2033: astore 122
    //   2035: aload 120
    //   2037: aload 121
    //   2039: aload 122
    //   2041: iconst_1
    //   2042: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   2045: goto -97 -> 1948
    //   2048: aload 13
    //   2050: ldc_w 1178
    //   2053: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   2056: iload 112
    //   2058: ifeq +9 -> 2067
    //   2061: aconst_null
    //   2062: astore 118
    //   2064: goto -83 -> 1981
    //   2067: ldc_w 1292
    //   2070: astore 118
    //   2072: aload_1
    //   2073: ldc_w 1182
    //   2076: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   2079: astore 123
    //   2081: aload 123
    //   2083: ifnull -102 -> 1981
    //   2086: aload 123
    //   2088: invokevirtual 994	java/lang/String:length	()I
    //   2091: ifle -110 -> 1981
    //   2094: ldc_w 308
    //   2097: aload 123
    //   2099: invokevirtual 998	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   2102: ifeq +11 -> 2113
    //   2105: ldc_w 1184
    //   2108: astore 118
    //   2110: goto -129 -> 1981
    //   2113: ldc 104
    //   2115: aload 123
    //   2117: invokevirtual 998	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   2120: ifne -139 -> 1981
    //   2123: new 502	java/lang/StringBuilder
    //   2126: dup
    //   2127: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   2130: ldc_w 1192
    //   2133: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2136: aload 123
    //   2138: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2141: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2144: astore 124
    //   2146: new 1025	java/lang/IllegalArgumentException
    //   2149: dup
    //   2150: aload 124
    //   2152: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   2155: athrow
    //   2156: aload_1
    //   2157: invokestatic 759	com/google/android/music/store/MusicContentProvider:getMusicFilterIndex	(Landroid/net/Uri;)I
    //   2160: istore 35
    //   2162: aload 28
    //   2164: ifnull +67 -> 2231
    //   2167: aload 13
    //   2169: ldc_w 1146
    //   2172: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   2175: getstatic 304	com/google/android/music/store/MusicContentProvider:sAlbumArtistsProjectionMap	Ljava/util/HashMap;
    //   2178: astore 45
    //   2180: aload_0
    //   2181: astore 125
    //   2183: aload 18
    //   2185: astore 126
    //   2187: aload_1
    //   2188: astore 127
    //   2190: aload 125
    //   2192: aload 126
    //   2194: aload 127
    //   2196: iload 35
    //   2198: iconst_0
    //   2199: invokespecial 761	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;IZ)V
    //   2202: aload 17
    //   2204: astore 42
    //   2206: aload 16
    //   2208: astore 43
    //   2210: aload 29
    //   2212: astore 28
    //   2214: aload 4
    //   2216: astore 44
    //   2218: aload_3
    //   2219: astore 47
    //   2221: aload_2
    //   2222: astore 46
    //   2224: aload 24
    //   2226: astore 41
    //   2228: goto -1458 -> 770
    //   2231: aload_2
    //   2232: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   2235: ifeq +71 -> 2306
    //   2238: iload 35
    //   2240: ifne +19 -> 2259
    //   2243: aload 12
    //   2245: ldc_w 1146
    //   2248: ldc_w 306
    //   2251: invokestatic 1172	com/google/android/music/store/MusicContentProvider:countGroups	(Lcom/google/android/music/store/Store;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   2254: astore 55
    //   2256: goto -1283 -> 973
    //   2259: aload_0
    //   2260: astore 128
    //   2262: aload 18
    //   2264: astore 129
    //   2266: aload_1
    //   2267: astore 130
    //   2269: aload 128
    //   2271: aload 129
    //   2273: aload 130
    //   2275: iload 35
    //   2277: iconst_0
    //   2278: invokespecial 761	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;IZ)V
    //   2281: aload 18
    //   2283: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2286: astore 131
    //   2288: aload 12
    //   2290: ldc_w 1146
    //   2293: ldc_w 312
    //   2296: aload 131
    //   2298: invokestatic 1176	com/google/android/music/store/MusicContentProvider:countGroups	(Lcom/google/android/music/store/Store;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   2301: astore 55
    //   2303: goto -1330 -> 973
    //   2306: iload 35
    //   2308: iconst_4
    //   2309: if_icmpeq +9 -> 2318
    //   2312: iload 35
    //   2314: iconst_5
    //   2315: if_icmpne +195 -> 2510
    //   2318: ldc_w 1294
    //   2321: astore 132
    //   2323: aload 13
    //   2325: aload 132
    //   2327: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   2330: ldc_w 1296
    //   2333: astore 41
    //   2335: ldc_w 1296
    //   2338: astore 29
    //   2340: new 502	java/lang/StringBuilder
    //   2343: dup
    //   2344: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   2347: astore 133
    //   2349: aload_0
    //   2350: astore 134
    //   2352: aload_1
    //   2353: astore 135
    //   2355: aload 134
    //   2357: aload 133
    //   2359: aload 135
    //   2361: iload 35
    //   2363: iconst_0
    //   2364: invokespecial 761	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;IZ)V
    //   2367: aload 133
    //   2369: invokevirtual 736	java/lang/StringBuilder:length	()I
    //   2372: ifle +146 -> 2518
    //   2375: new 502	java/lang/StringBuilder
    //   2378: dup
    //   2379: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   2382: ldc_w 1186
    //   2385: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2388: astore 136
    //   2390: aload 133
    //   2392: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2395: astore 137
    //   2397: aload 136
    //   2399: aload 137
    //   2401: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2404: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2407: astore 138
    //   2409: new 502	java/lang/StringBuilder
    //   2412: dup
    //   2413: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   2416: ldc_w 1298
    //   2419: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2422: aload 138
    //   2424: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2427: astore 139
    //   2429: aload 41
    //   2431: ifnonnull +95 -> 2526
    //   2434: ldc_w 914
    //   2437: astore 140
    //   2439: aload 139
    //   2441: aload 140
    //   2443: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2446: astore 141
    //   2448: aload 29
    //   2450: ifnonnull +102 -> 2552
    //   2453: ldc_w 914
    //   2456: astore 142
    //   2458: aload 141
    //   2460: aload 142
    //   2462: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2465: astore 143
    //   2467: aload 16
    //   2469: ifnonnull +109 -> 2578
    //   2472: ldc_w 914
    //   2475: astore 144
    //   2477: aload 143
    //   2479: aload 144
    //   2481: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2484: ldc_w 902
    //   2487: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2490: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2493: astore 145
    //   2495: aload 18
    //   2497: aload 145
    //   2499: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2502: astore 146
    //   2504: aconst_null
    //   2505: astore 16
    //   2507: goto -332 -> 2175
    //   2510: ldc_w 1300
    //   2513: astore 132
    //   2515: goto -192 -> 2323
    //   2518: ldc_w 914
    //   2521: astore 138
    //   2523: goto -114 -> 2409
    //   2526: new 502	java/lang/StringBuilder
    //   2529: dup
    //   2530: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   2533: ldc_w 906
    //   2536: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2539: aload 41
    //   2541: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2544: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2547: astore 140
    //   2549: goto -110 -> 2439
    //   2552: new 502	java/lang/StringBuilder
    //   2555: dup
    //   2556: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   2559: ldc_w 1194
    //   2562: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2565: aload 29
    //   2567: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2570: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2573: astore 142
    //   2575: goto -117 -> 2458
    //   2578: new 502	java/lang/StringBuilder
    //   2581: dup
    //   2582: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   2585: ldc_w 1196
    //   2588: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2591: astore 147
    //   2593: aload 16
    //   2595: astore 148
    //   2597: aload 147
    //   2599: aload 148
    //   2601: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   2604: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   2607: astore 144
    //   2609: goto -132 -> 2477
    //   2612: aload_1
    //   2613: invokevirtual 1199	android/net/Uri:getLastPathSegment	()Ljava/lang/String;
    //   2616: astore 44
    //   2618: aload 44
    //   2620: invokestatic 1203	com/google/android/music/utils/MusicUtils:isNautilusId	(Ljava/lang/String;)Z
    //   2623: ifeq +59 -> 2682
    //   2626: aload_0
    //   2627: getfield 1205	com/google/android/music/store/MusicContentProvider:mExecutor	Ljava/util/concurrent/ThreadPoolExecutor;
    //   2630: astore 149
    //   2632: aload_0
    //   2633: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   2636: astore 150
    //   2638: aload_1
    //   2639: astore 151
    //   2641: aload_2
    //   2642: astore 152
    //   2644: aload 149
    //   2646: aload 150
    //   2648: aload 21
    //   2650: aload 151
    //   2652: aload 44
    //   2654: aload 152
    //   2656: invokestatic 1210	com/google/android/music/store/NautilusContentProviderHelper:query	(Ljava/util/concurrent/ThreadPoolExecutor;Landroid/content/Context;ILandroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)Lcom/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult;
    //   2659: astore 153
    //   2661: aload 153
    //   2663: ifnonnull +9 -> 2672
    //   2666: aconst_null
    //   2667: astore 55
    //   2669: goto -1696 -> 973
    //   2672: aload 153
    //   2674: invokevirtual 1216	com/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult:getCursor	()Landroid/database/Cursor;
    //   2677: astore 55
    //   2679: goto -1706 -> 973
    //   2682: iconst_1
    //   2683: anewarray 50	java/lang/String
    //   2686: astore 154
    //   2688: aload 154
    //   2690: iconst_0
    //   2691: aload 44
    //   2693: aastore
    //   2694: aload 12
    //   2696: invokevirtual 1258	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   2699: astore 35
    //   2701: aload 28
    //   2703: ifnonnull +10 -> 2713
    //   2706: aload_2
    //   2707: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   2710: ifeq +104 -> 2814
    //   2713: aload 13
    //   2715: ldc_w 1146
    //   2718: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   2721: getstatic 304	com/google/android/music/store/MusicContentProvider:sAlbumArtistsProjectionMap	Ljava/util/HashMap;
    //   2724: astore 155
    //   2726: aload 13
    //   2728: aload 155
    //   2730: invokevirtual 1153	android/database/sqlite/SQLiteQueryBuilder:setProjectionMap	(Ljava/util/Map;)V
    //   2733: aload 13
    //   2735: astore 156
    //   2737: aload_2
    //   2738: astore 157
    //   2740: aload 154
    //   2742: astore 158
    //   2744: aload 156
    //   2746: aload 35
    //   2748: aload 157
    //   2750: ldc_w 1302
    //   2753: aload 158
    //   2755: ldc_w 583
    //   2758: aconst_null
    //   2759: aconst_null
    //   2760: aconst_null
    //   2761: invokevirtual 1305	android/database/sqlite/SQLiteQueryBuilder:query	(Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   2764: astore 159
    //   2766: aload 159
    //   2768: ifnull +69 -> 2837
    //   2771: aload 159
    //   2773: invokeinterface 1275 1 0
    //   2778: ifle +59 -> 2837
    //   2781: aload_0
    //   2782: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   2785: invokevirtual 1268	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   2788: astore 160
    //   2790: aload_1
    //   2791: astore 161
    //   2793: aload 159
    //   2795: aload 160
    //   2797: aload 161
    //   2799: invokeinterface 1272 3 0
    //   2804: aload 12
    //   2806: aload 35
    //   2808: invokevirtual 1279	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   2811: goto -1838 -> 973
    //   2814: aload 13
    //   2816: ldc_w 1294
    //   2819: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   2822: goto -101 -> 2721
    //   2825: astore 162
    //   2827: aload 12
    //   2829: aload 35
    //   2831: invokevirtual 1279	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   2834: aload 162
    //   2836: athrow
    //   2837: aload 159
    //   2839: invokestatic 955	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   2842: aload 13
    //   2844: ldc_w 1146
    //   2847: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   2850: getstatic 328	com/google/android/music/store/MusicContentProvider:sTrackArtistsProjectionMap	Ljava/util/HashMap;
    //   2853: astore 163
    //   2855: aload 13
    //   2857: aload 163
    //   2859: invokevirtual 1153	android/database/sqlite/SQLiteQueryBuilder:setProjectionMap	(Ljava/util/Map;)V
    //   2862: aload 13
    //   2864: astore 164
    //   2866: aload_2
    //   2867: astore 165
    //   2869: aload 154
    //   2871: astore 166
    //   2873: aload 164
    //   2875: aload 35
    //   2877: aload 165
    //   2879: ldc_w 1307
    //   2882: aload 166
    //   2884: ldc_w 1309
    //   2887: aconst_null
    //   2888: aconst_null
    //   2889: aconst_null
    //   2890: invokevirtual 1305	android/database/sqlite/SQLiteQueryBuilder:query	(Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   2893: astore 55
    //   2895: aload 55
    //   2897: ifnull +35 -> 2932
    //   2900: aload_0
    //   2901: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   2904: invokevirtual 1268	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   2907: astore 167
    //   2909: aload_1
    //   2910: astore 168
    //   2912: aload 55
    //   2914: aload 167
    //   2916: aload 168
    //   2918: invokeinterface 1272 3 0
    //   2923: aload 55
    //   2925: invokeinterface 1275 1 0
    //   2930: istore 169
    //   2932: aload 12
    //   2934: aload 35
    //   2936: invokevirtual 1279	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   2939: goto -1966 -> 973
    //   2942: aload_1
    //   2943: invokevirtual 1313	android/net/Uri:getPathSegments	()Ljava/util/List;
    //   2946: iconst_1
    //   2947: invokeinterface 1319 2 0
    //   2952: checkcast 50	java/lang/String
    //   2955: astore 44
    //   2957: aload 44
    //   2959: invokestatic 1203	com/google/android/music/utils/MusicUtils:isNautilusId	(Ljava/lang/String;)Z
    //   2962: ifeq +59 -> 3021
    //   2965: aload_0
    //   2966: getfield 1205	com/google/android/music/store/MusicContentProvider:mExecutor	Ljava/util/concurrent/ThreadPoolExecutor;
    //   2969: astore 170
    //   2971: aload_0
    //   2972: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   2975: astore 171
    //   2977: aload_1
    //   2978: astore 172
    //   2980: aload_2
    //   2981: astore 173
    //   2983: aload 170
    //   2985: aload 171
    //   2987: aload 21
    //   2989: aload 172
    //   2991: aload 44
    //   2993: aload 173
    //   2995: invokestatic 1210	com/google/android/music/store/NautilusContentProviderHelper:query	(Ljava/util/concurrent/ThreadPoolExecutor;Landroid/content/Context;ILandroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)Lcom/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult;
    //   2998: astore 174
    //   3000: aload 174
    //   3002: ifnonnull +9 -> 3011
    //   3005: aconst_null
    //   3006: astore 55
    //   3008: goto -2035 -> 973
    //   3011: aload 174
    //   3013: invokevirtual 1216	com/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult:getCursor	()Landroid/database/Cursor;
    //   3016: astore 55
    //   3018: goto -2045 -> 973
    //   3021: aload 28
    //   3023: ifnonnull +10 -> 3033
    //   3026: aload_2
    //   3027: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   3030: ifeq +139 -> 3169
    //   3033: aload 13
    //   3035: ldc_w 1146
    //   3038: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   3041: aconst_null
    //   3042: astore 175
    //   3044: getstatic 346	com/google/android/music/store/MusicContentProvider:sAlbumsProjectionMap	Ljava/util/HashMap;
    //   3047: astore 176
    //   3049: new 502	java/lang/StringBuilder
    //   3052: dup
    //   3053: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   3056: ldc_w 1321
    //   3059: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3062: aload 44
    //   3064: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3067: ldc_w 1323
    //   3070: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3073: ldc_w 1146
    //   3076: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3079: ldc_w 1325
    //   3082: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3085: ldc_w 330
    //   3088: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3091: ldc_w 1327
    //   3094: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3097: aload 44
    //   3099: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3102: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   3105: astore 177
    //   3107: aload 18
    //   3109: aload 177
    //   3111: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3114: astore 178
    //   3116: aload_0
    //   3117: astore 179
    //   3119: aload 18
    //   3121: astore 180
    //   3123: aload_1
    //   3124: astore 181
    //   3126: aload 179
    //   3128: aload 180
    //   3130: aload 181
    //   3132: iconst_1
    //   3133: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   3136: aload 17
    //   3138: astore 42
    //   3140: aload 16
    //   3142: astore 43
    //   3144: aload 175
    //   3146: astore 28
    //   3148: aload 4
    //   3150: astore 44
    //   3152: aload_3
    //   3153: astore 47
    //   3155: aload_2
    //   3156: astore 46
    //   3158: aload 24
    //   3160: astore 41
    //   3162: aload 176
    //   3164: astore 45
    //   3166: goto -2396 -> 770
    //   3169: aload 13
    //   3171: ldc_w 1178
    //   3174: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   3177: ldc_w 1292
    //   3180: astore 175
    //   3182: goto -138 -> 3044
    //   3185: aload_1
    //   3186: invokevirtual 1313	android/net/Uri:getPathSegments	()Ljava/util/List;
    //   3189: iconst_1
    //   3190: invokeinterface 1319 2 0
    //   3195: checkcast 50	java/lang/String
    //   3198: astore 44
    //   3200: aload 44
    //   3202: invokestatic 1203	com/google/android/music/utils/MusicUtils:isNautilusId	(Ljava/lang/String;)Z
    //   3205: ifeq +59 -> 3264
    //   3208: aload_0
    //   3209: getfield 1205	com/google/android/music/store/MusicContentProvider:mExecutor	Ljava/util/concurrent/ThreadPoolExecutor;
    //   3212: astore 182
    //   3214: aload_0
    //   3215: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   3218: astore 183
    //   3220: aload_1
    //   3221: astore 184
    //   3223: aload_2
    //   3224: astore 185
    //   3226: aload 182
    //   3228: aload 183
    //   3230: aload 21
    //   3232: aload 184
    //   3234: aload 44
    //   3236: aload 185
    //   3238: invokestatic 1210	com/google/android/music/store/NautilusContentProviderHelper:query	(Ljava/util/concurrent/ThreadPoolExecutor;Landroid/content/Context;ILandroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)Lcom/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult;
    //   3241: astore 186
    //   3243: aload 186
    //   3245: ifnull +13 -> 3258
    //   3248: aload 186
    //   3250: invokevirtual 1216	com/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult:getCursor	()Landroid/database/Cursor;
    //   3253: astore 55
    //   3255: goto -2282 -> 973
    //   3258: aconst_null
    //   3259: astore 55
    //   3261: goto -2288 -> 973
    //   3264: new 1025	java/lang/IllegalArgumentException
    //   3267: dup
    //   3268: ldc_w 1329
    //   3271: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   3274: athrow
    //   3275: aload_1
    //   3276: invokevirtual 1313	android/net/Uri:getPathSegments	()Ljava/util/List;
    //   3279: iconst_1
    //   3280: invokeinterface 1319 2 0
    //   3285: checkcast 50	java/lang/String
    //   3288: astore 44
    //   3290: aload 44
    //   3292: invokestatic 1203	com/google/android/music/utils/MusicUtils:isNautilusId	(Ljava/lang/String;)Z
    //   3295: ifeq +59 -> 3354
    //   3298: aload_0
    //   3299: getfield 1205	com/google/android/music/store/MusicContentProvider:mExecutor	Ljava/util/concurrent/ThreadPoolExecutor;
    //   3302: astore 187
    //   3304: aload_0
    //   3305: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   3308: astore 188
    //   3310: aload_1
    //   3311: astore 189
    //   3313: aload_2
    //   3314: astore 190
    //   3316: aload 187
    //   3318: aload 188
    //   3320: aload 21
    //   3322: aload 189
    //   3324: aload 44
    //   3326: aload 190
    //   3328: invokestatic 1210	com/google/android/music/store/NautilusContentProviderHelper:query	(Ljava/util/concurrent/ThreadPoolExecutor;Landroid/content/Context;ILandroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)Lcom/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult;
    //   3331: astore 191
    //   3333: aload 191
    //   3335: ifnull +13 -> 3348
    //   3338: aload 191
    //   3340: invokevirtual 1216	com/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult:getCursor	()Landroid/database/Cursor;
    //   3343: astore 55
    //   3345: goto -2372 -> 973
    //   3348: aconst_null
    //   3349: astore 55
    //   3351: goto -2378 -> 973
    //   3354: new 1025	java/lang/IllegalArgumentException
    //   3357: dup
    //   3358: ldc_w 1331
    //   3361: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   3364: athrow
    //   3365: aload_1
    //   3366: invokevirtual 1313	android/net/Uri:getPathSegments	()Ljava/util/List;
    //   3369: iconst_1
    //   3370: invokeinterface 1319 2 0
    //   3375: checkcast 50	java/lang/String
    //   3378: astore 44
    //   3380: aload 44
    //   3382: invokestatic 1203	com/google/android/music/utils/MusicUtils:isNautilusId	(Ljava/lang/String;)Z
    //   3385: ifeq +59 -> 3444
    //   3388: aload_0
    //   3389: getfield 1205	com/google/android/music/store/MusicContentProvider:mExecutor	Ljava/util/concurrent/ThreadPoolExecutor;
    //   3392: astore 192
    //   3394: aload_0
    //   3395: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   3398: astore 193
    //   3400: aload_1
    //   3401: astore 194
    //   3403: aload_2
    //   3404: astore 195
    //   3406: aload 192
    //   3408: aload 193
    //   3410: aload 21
    //   3412: aload 194
    //   3414: aload 44
    //   3416: aload 195
    //   3418: invokestatic 1210	com/google/android/music/store/NautilusContentProviderHelper:query	(Ljava/util/concurrent/ThreadPoolExecutor;Landroid/content/Context;ILandroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)Lcom/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult;
    //   3421: astore 196
    //   3423: aload 196
    //   3425: ifnull +13 -> 3438
    //   3428: aload 196
    //   3430: invokevirtual 1216	com/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult:getCursor	()Landroid/database/Cursor;
    //   3433: astore 55
    //   3435: goto -2462 -> 973
    //   3438: aconst_null
    //   3439: astore 55
    //   3441: goto -2468 -> 973
    //   3444: new 1025	java/lang/IllegalArgumentException
    //   3447: dup
    //   3448: ldc_w 1333
    //   3451: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   3454: athrow
    //   3455: aload 13
    //   3457: ldc_w 1146
    //   3460: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   3463: aload 28
    //   3465: ifnonnull +10 -> 3475
    //   3468: aload_2
    //   3469: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   3472: ifeq +74 -> 3546
    //   3475: aconst_null
    //   3476: astore 197
    //   3478: getstatic 272	com/google/android/music/store/MusicContentProvider:sGenresProjectionMap	Ljava/util/HashMap;
    //   3481: astore 198
    //   3483: aload_0
    //   3484: astore 199
    //   3486: aload 18
    //   3488: astore 200
    //   3490: aload_1
    //   3491: astore 201
    //   3493: aload 199
    //   3495: aload 200
    //   3497: aload 201
    //   3499: iconst_1
    //   3500: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   3503: aload 18
    //   3505: ldc_w 1335
    //   3508: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3511: astore 202
    //   3513: aload 17
    //   3515: astore 42
    //   3517: aload 16
    //   3519: astore 43
    //   3521: aload 197
    //   3523: astore 28
    //   3525: aload 4
    //   3527: astore 44
    //   3529: aload_3
    //   3530: astore 47
    //   3532: aload_2
    //   3533: astore 46
    //   3535: aload 24
    //   3537: astore 41
    //   3539: aload 198
    //   3541: astore 45
    //   3543: goto -2773 -> 770
    //   3546: ldc_w 1337
    //   3549: astore 197
    //   3551: goto -73 -> 3478
    //   3554: aload 28
    //   3556: ifnonnull +10 -> 3566
    //   3559: aload_2
    //   3560: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   3563: ifne +3 -> 3566
    //   3566: aload 13
    //   3568: ldc_w 1146
    //   3571: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   3574: new 502	java/lang/StringBuilder
    //   3577: dup
    //   3578: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   3581: ldc_w 1339
    //   3584: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3587: astore 203
    //   3589: aload_1
    //   3590: invokevirtual 1199	android/net/Uri:getLastPathSegment	()Ljava/lang/String;
    //   3593: astore 204
    //   3595: aload 203
    //   3597: aload 204
    //   3599: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3602: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   3605: astore 205
    //   3607: aload 18
    //   3609: aload 205
    //   3611: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3614: astore 206
    //   3616: getstatic 272	com/google/android/music/store/MusicContentProvider:sGenresProjectionMap	Ljava/util/HashMap;
    //   3619: astore 45
    //   3621: aload 17
    //   3623: astore 42
    //   3625: aload 16
    //   3627: astore 43
    //   3629: aconst_null
    //   3630: astore 28
    //   3632: aload 4
    //   3634: astore 44
    //   3636: aload_3
    //   3637: astore 47
    //   3639: aload_2
    //   3640: astore 46
    //   3642: aload 24
    //   3644: astore 41
    //   3646: goto -2876 -> 770
    //   3649: aload 28
    //   3651: ifnonnull +10 -> 3661
    //   3654: aload_2
    //   3655: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   3658: ifeq +11 -> 3669
    //   3661: new 1228	java/lang/UnsupportedOperationException
    //   3664: dup
    //   3665: invokespecial 1229	java/lang/UnsupportedOperationException:<init>	()V
    //   3668: athrow
    //   3669: aload 13
    //   3671: ldc_w 1146
    //   3674: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   3677: getstatic 272	com/google/android/music/store/MusicContentProvider:sGenresProjectionMap	Ljava/util/HashMap;
    //   3680: astore 45
    //   3682: aload_0
    //   3683: astore 207
    //   3685: aload 18
    //   3687: astore 208
    //   3689: aload_1
    //   3690: astore 209
    //   3692: aload 207
    //   3694: aload 208
    //   3696: aload 209
    //   3698: iconst_1
    //   3699: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   3702: aload 18
    //   3704: ldc_w 1335
    //   3707: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3710: astore 210
    //   3712: ldc_w 1341
    //   3715: astore 42
    //   3717: aload 16
    //   3719: astore 43
    //   3721: ldc_w 1343
    //   3724: astore 28
    //   3726: aload 4
    //   3728: astore 44
    //   3730: aload_3
    //   3731: astore 47
    //   3733: aload_2
    //   3734: astore 46
    //   3736: aload 24
    //   3738: astore 41
    //   3740: goto -2970 -> 770
    //   3743: aload 28
    //   3745: ifnonnull +10 -> 3755
    //   3748: aload_2
    //   3749: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   3752: ifeq +170 -> 3922
    //   3755: aload 13
    //   3757: ldc_w 1146
    //   3760: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   3763: aconst_null
    //   3764: astore 35
    //   3766: getstatic 268	com/google/android/music/store/MusicContentProvider:sGroupedMusicProjectionMap	Ljava/util/HashMap;
    //   3769: astore 211
    //   3771: new 502	java/lang/StringBuilder
    //   3774: dup
    //   3775: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   3778: ldc_w 1345
    //   3781: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3784: astore 212
    //   3786: aload_1
    //   3787: invokevirtual 1313	android/net/Uri:getPathSegments	()Ljava/util/List;
    //   3790: iconst_1
    //   3791: invokeinterface 1319 2 0
    //   3796: checkcast 50	java/lang/String
    //   3799: astore 213
    //   3801: aload 212
    //   3803: aload 213
    //   3805: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3808: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   3811: astore 214
    //   3813: aload 18
    //   3815: aload 214
    //   3817: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3820: astore 215
    //   3822: aload_0
    //   3823: astore 216
    //   3825: aload 18
    //   3827: astore 217
    //   3829: aload_1
    //   3830: astore 218
    //   3832: aload 216
    //   3834: aload 217
    //   3836: aload 218
    //   3838: iconst_1
    //   3839: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   3842: aload_1
    //   3843: ldc_w 1347
    //   3846: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   3849: astore 219
    //   3851: aload 219
    //   3853: ifnull +7304 -> 11157
    //   3856: new 502	java/lang/StringBuilder
    //   3859: dup
    //   3860: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   3863: ldc_w 1349
    //   3866: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3869: aload 219
    //   3871: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3874: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   3877: astore 220
    //   3879: aload 18
    //   3881: aload 220
    //   3883: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   3886: astore 221
    //   3888: aload 211
    //   3890: astore 45
    //   3892: aload 17
    //   3894: astore 42
    //   3896: aload 16
    //   3898: astore 43
    //   3900: ldc_w 1351
    //   3903: astore 28
    //   3905: aload 4
    //   3907: astore 44
    //   3909: aload_3
    //   3910: astore 47
    //   3912: aload 24
    //   3914: astore 41
    //   3916: aload_2
    //   3917: astore 46
    //   3919: goto -3149 -> 770
    //   3922: aload 13
    //   3924: ldc_w 1146
    //   3927: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   3930: aload 29
    //   3932: ifnull +14 -> 3946
    //   3935: new 1025	java/lang/IllegalArgumentException
    //   3938: dup
    //   3939: ldc_w 1353
    //   3942: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   3945: athrow
    //   3946: aload_1
    //   3947: ldc_w 1182
    //   3950: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   3953: astore 222
    //   3955: aload_0
    //   3956: aload 222
    //   3958: iconst_1
    //   3959: invokespecial 1355	com/google/android/music/store/MusicContentProvider:getSortOrderFromQueryParam	(Ljava/lang/String;Z)Landroid/util/Pair;
    //   3962: getfield 1359	android/util/Pair:first	Ljava/lang/Object;
    //   3965: checkcast 50	java/lang/String
    //   3968: astore 223
    //   3970: aload 223
    //   3972: ifnonnull +7218 -> 11190
    //   3975: ldc_w 1000
    //   3978: astore 35
    //   3980: goto -214 -> 3766
    //   3983: aload 28
    //   3985: ifnonnull +10 -> 3995
    //   3988: aload_2
    //   3989: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   3992: ifeq +123 -> 4115
    //   3995: aload 13
    //   3997: ldc_w 1146
    //   4000: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   4003: aconst_null
    //   4004: astore 35
    //   4006: getstatic 346	com/google/android/music/store/MusicContentProvider:sAlbumsProjectionMap	Ljava/util/HashMap;
    //   4009: astore 224
    //   4011: new 502	java/lang/StringBuilder
    //   4014: dup
    //   4015: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   4018: ldc_w 1339
    //   4021: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4024: astore 225
    //   4026: aload_1
    //   4027: invokevirtual 1313	android/net/Uri:getPathSegments	()Ljava/util/List;
    //   4030: iconst_1
    //   4031: invokeinterface 1319 2 0
    //   4036: checkcast 50	java/lang/String
    //   4039: astore 226
    //   4041: aload 225
    //   4043: aload 226
    //   4045: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4048: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4051: astore 227
    //   4053: aload 18
    //   4055: aload 227
    //   4057: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4060: astore 228
    //   4062: aload_0
    //   4063: astore 229
    //   4065: aload 18
    //   4067: astore 230
    //   4069: aload_1
    //   4070: astore 231
    //   4072: aload 229
    //   4074: aload 230
    //   4076: aload 231
    //   4078: iconst_1
    //   4079: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   4082: aload 224
    //   4084: astore 45
    //   4086: aload 17
    //   4088: astore 42
    //   4090: aload 16
    //   4092: astore 43
    //   4094: aload 35
    //   4096: astore 28
    //   4098: aload 4
    //   4100: astore 44
    //   4102: aload_3
    //   4103: astore 47
    //   4105: aload 24
    //   4107: astore 41
    //   4109: aload_2
    //   4110: astore 46
    //   4112: goto -3342 -> 770
    //   4115: aload 13
    //   4117: ldc_w 1178
    //   4120: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   4123: ldc_w 1292
    //   4126: astore 35
    //   4128: goto -122 -> 4006
    //   4131: aload 13
    //   4133: ldc_w 1146
    //   4136: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   4139: aload_1
    //   4140: invokestatic 759	com/google/android/music/store/MusicContentProvider:getMusicFilterIndex	(Landroid/net/Uri;)I
    //   4143: istore 33
    //   4145: aload_2
    //   4146: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   4149: ifeq +97 -> 4246
    //   4152: iload 33
    //   4154: ifne +30 -> 4184
    //   4157: aload_3
    //   4158: ifnull +10 -> 4168
    //   4161: aload_3
    //   4162: invokevirtual 994	java/lang/String:length	()I
    //   4165: ifne +19 -> 4184
    //   4168: aload 12
    //   4170: ldc_w 1146
    //   4173: ldc_w 619
    //   4176: invokestatic 1172	com/google/android/music/store/MusicContentProvider:countGroups	(Lcom/google/android/music/store/Store;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   4179: astore 55
    //   4181: goto -3208 -> 973
    //   4184: aload_0
    //   4185: astore 232
    //   4187: aload 18
    //   4189: astore 233
    //   4191: aload_1
    //   4192: astore 234
    //   4194: aload 232
    //   4196: aload 233
    //   4198: aload 234
    //   4200: iload 33
    //   4202: iconst_0
    //   4203: invokespecial 761	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;IZ)V
    //   4206: aload 18
    //   4208: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4211: astore 235
    //   4213: aload 12
    //   4215: astore 236
    //   4217: aload_3
    //   4218: astore 237
    //   4220: aload 4
    //   4222: astore 238
    //   4224: aload 236
    //   4226: ldc_w 1146
    //   4229: ldc_w 1361
    //   4232: aload 235
    //   4234: aload 237
    //   4236: aload 238
    //   4238: invokestatic 897	com/google/android/music/store/MusicContentProvider:countGroups	(Lcom/google/android/music/store/Store;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
    //   4241: astore 55
    //   4243: goto -3270 -> 973
    //   4246: aload 28
    //   4248: ifnonnull +6902 -> 11150
    //   4251: aload 29
    //   4253: ifnull +14 -> 4267
    //   4256: new 1025	java/lang/IllegalArgumentException
    //   4259: dup
    //   4260: ldc_w 1353
    //   4263: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   4266: athrow
    //   4267: aload_1
    //   4268: ldc_w 1182
    //   4271: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   4274: astore 239
    //   4276: aload_0
    //   4277: aload 239
    //   4279: iconst_1
    //   4280: invokespecial 1355	com/google/android/music/store/MusicContentProvider:getSortOrderFromQueryParam	(Ljava/lang/String;Z)Landroid/util/Pair;
    //   4283: astore 21
    //   4285: aload 21
    //   4287: getfield 1359	android/util/Pair:first	Ljava/lang/Object;
    //   4290: checkcast 50	java/lang/String
    //   4293: astore 240
    //   4295: aload 240
    //   4297: ifnonnull +6846 -> 11143
    //   4300: ldc_w 579
    //   4303: astore 35
    //   4305: aload 21
    //   4307: getfield 1364	android/util/Pair:second	Ljava/lang/Object;
    //   4310: checkcast 1002	java/lang/Boolean
    //   4313: invokevirtual 1367	java/lang/Boolean:booleanValue	()Z
    //   4316: ifeq +267 -> 4583
    //   4319: new 502	java/lang/StringBuilder
    //   4322: dup
    //   4323: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   4326: aload 35
    //   4328: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4331: ldc_w 1369
    //   4334: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4337: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4340: astore 17
    //   4342: aconst_null
    //   4343: astore 35
    //   4345: aconst_null
    //   4346: astore 15
    //   4348: getstatic 268	com/google/android/music/store/MusicContentProvider:sGroupedMusicProjectionMap	Ljava/util/HashMap;
    //   4351: astore 241
    //   4353: new 502	java/lang/StringBuilder
    //   4356: dup
    //   4357: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   4360: astore 242
    //   4362: aload_0
    //   4363: astore 243
    //   4365: aload_1
    //   4366: astore 244
    //   4368: aload 243
    //   4370: aload 242
    //   4372: aload 244
    //   4374: iload 33
    //   4376: iconst_0
    //   4377: invokespecial 761	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;IZ)V
    //   4380: aload 242
    //   4382: invokevirtual 736	java/lang/StringBuilder:length	()I
    //   4385: ifle +206 -> 4591
    //   4388: new 502	java/lang/StringBuilder
    //   4391: dup
    //   4392: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   4395: ldc_w 1186
    //   4398: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4401: astore 245
    //   4403: aload 242
    //   4405: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4408: astore 246
    //   4410: aload 245
    //   4412: aload 246
    //   4414: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4417: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4420: astore 247
    //   4422: new 502	java/lang/StringBuilder
    //   4425: dup
    //   4426: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   4429: ldc_w 1371
    //   4432: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4435: aload 247
    //   4437: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4440: astore 248
    //   4442: aload 17
    //   4444: ifnonnull +155 -> 4599
    //   4447: ldc_w 914
    //   4450: astore 249
    //   4452: aload 248
    //   4454: aload 249
    //   4456: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4459: astore 250
    //   4461: aload 35
    //   4463: ifnonnull +162 -> 4625
    //   4466: ldc_w 914
    //   4469: astore 251
    //   4471: aload 250
    //   4473: aload 251
    //   4475: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4478: astore 252
    //   4480: aload 16
    //   4482: ifnonnull +169 -> 4651
    //   4485: ldc_w 914
    //   4488: astore 253
    //   4490: aload 252
    //   4492: aload 253
    //   4494: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4497: ldc_w 902
    //   4500: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4503: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4506: astore 254
    //   4508: aload 18
    //   4510: aload 254
    //   4512: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4515: astore 255
    //   4517: aload_0
    //   4518: wide
    //   4522: aload 18
    //   4524: wide
    //   4528: aload_1
    //   4529: wide
    //   4533: wide
    //   4537: wide
    //   4541: wide
    //   4545: iload 33
    //   4547: iconst_0
    //   4548: invokespecial 761	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;IZ)V
    //   4551: aload 241
    //   4553: astore 45
    //   4555: aload 17
    //   4557: astore 42
    //   4559: aconst_null
    //   4560: astore 43
    //   4562: aload 35
    //   4564: astore 28
    //   4566: aload 4
    //   4568: astore 44
    //   4570: aload_3
    //   4571: astore 47
    //   4573: aload 24
    //   4575: astore 41
    //   4577: aload_2
    //   4578: astore 46
    //   4580: goto -3810 -> 770
    //   4583: ldc_w 619
    //   4586: astore 17
    //   4588: goto -243 -> 4345
    //   4591: ldc_w 914
    //   4594: astore 247
    //   4596: goto -174 -> 4422
    //   4599: new 502	java/lang/StringBuilder
    //   4602: dup
    //   4603: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   4606: ldc_w 906
    //   4609: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4612: aload 17
    //   4614: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4617: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4620: astore 249
    //   4622: goto -170 -> 4452
    //   4625: new 502	java/lang/StringBuilder
    //   4628: dup
    //   4629: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   4632: ldc_w 1194
    //   4635: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4638: aload 35
    //   4640: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4643: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4646: astore 251
    //   4648: goto -177 -> 4471
    //   4651: new 502	java/lang/StringBuilder
    //   4654: dup
    //   4655: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   4658: ldc_w 1196
    //   4661: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4664: wide
    //   4668: aload 16
    //   4670: wide
    //   4674: wide
    //   4678: wide
    //   4682: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4685: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4688: astore 253
    //   4690: goto -200 -> 4490
    //   4693: aload_1
    //   4694: invokevirtual 1313	android/net/Uri:getPathSegments	()Ljava/util/List;
    //   4697: iconst_1
    //   4698: invokeinterface 1319 2 0
    //   4703: checkcast 50	java/lang/String
    //   4706: astore 44
    //   4708: aload 44
    //   4710: invokestatic 1203	com/google/android/music/utils/MusicUtils:isNautilusId	(Ljava/lang/String;)Z
    //   4713: ifeq +217 -> 4930
    //   4716: ldc_w 647
    //   4719: astore 28
    //   4721: aload_2
    //   4722: aload 28
    //   4724: invokestatic 1375	com/google/android/music/utils/DbUtils:injectColumnIntoProjection	([Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;
    //   4727: astore 42
    //   4729: aload_0
    //   4730: getfield 1205	com/google/android/music/store/MusicContentProvider:mExecutor	Ljava/util/concurrent/ThreadPoolExecutor;
    //   4733: wide
    //   4737: aload_0
    //   4738: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   4741: wide
    //   4745: aload_1
    //   4746: wide
    //   4750: wide
    //   4754: wide
    //   4758: aload 21
    //   4760: wide
    //   4764: aload 44
    //   4766: aload 42
    //   4768: invokestatic 1210	com/google/android/music/store/NautilusContentProviderHelper:query	(Ljava/util/concurrent/ThreadPoolExecutor;Landroid/content/Context;ILandroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)Lcom/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult;
    //   4771: wide
    //   4775: wide
    //   4779: ifnonnull +9 -> 4788
    //   4782: aconst_null
    //   4783: astore 55
    //   4785: goto -3812 -> 973
    //   4788: wide
    //   4792: invokevirtual 1216	com/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult:getCursor	()Landroid/database/Cursor;
    //   4795: astore 25
    //   4797: aload 13
    //   4799: ldc_w 1146
    //   4802: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   4805: getstatic 268	com/google/android/music/store/MusicContentProvider:sGroupedMusicProjectionMap	Ljava/util/HashMap;
    //   4808: astore 45
    //   4810: new 502	java/lang/StringBuilder
    //   4813: dup
    //   4814: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   4817: ldc_w 1283
    //   4820: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4823: wide
    //   4827: aload 44
    //   4829: invokestatic 1286	com/google/android/music/utils/DbUtils:quoteStringValue	(Ljava/lang/String;)Ljava/lang/String;
    //   4832: wide
    //   4836: wide
    //   4840: wide
    //   4844: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4847: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4850: wide
    //   4854: aload 18
    //   4856: wide
    //   4860: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4863: wide
    //   4867: aload_0
    //   4868: wide
    //   4872: aload 18
    //   4874: wide
    //   4878: aload_1
    //   4879: wide
    //   4883: wide
    //   4887: wide
    //   4891: wide
    //   4895: iconst_1
    //   4896: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   4899: aload 16
    //   4901: astore 43
    //   4903: aload 4
    //   4905: astore 44
    //   4907: aload_3
    //   4908: astore 47
    //   4910: aload 42
    //   4912: astore 46
    //   4914: ldc_w 1377
    //   4917: astore 42
    //   4919: aload 28
    //   4921: astore 41
    //   4923: aload 29
    //   4925: astore 28
    //   4927: goto -4157 -> 770
    //   4930: aload 13
    //   4932: ldc_w 1146
    //   4935: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   4938: aload 28
    //   4940: ifnonnull +10 -> 4950
    //   4943: aload_2
    //   4944: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   4947: ifeq +110 -> 5057
    //   4950: aconst_null
    //   4951: astore 29
    //   4953: getstatic 268	com/google/android/music/store/MusicContentProvider:sGroupedMusicProjectionMap	Ljava/util/HashMap;
    //   4956: astore 45
    //   4958: new 502	java/lang/StringBuilder
    //   4961: dup
    //   4962: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   4965: ldc_w 1226
    //   4968: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4971: aload 44
    //   4973: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4976: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   4979: wide
    //   4983: aload 18
    //   4985: wide
    //   4989: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   4992: wide
    //   4996: aload_0
    //   4997: wide
    //   5001: aload 18
    //   5003: wide
    //   5007: aload_1
    //   5008: wide
    //   5012: wide
    //   5016: wide
    //   5020: wide
    //   5024: iconst_1
    //   5025: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   5028: aload 17
    //   5030: astore 42
    //   5032: aload 16
    //   5034: astore 43
    //   5036: aload 29
    //   5038: astore 28
    //   5040: aload 4
    //   5042: astore 44
    //   5044: aload_3
    //   5045: astore 47
    //   5047: aload_2
    //   5048: astore 46
    //   5050: aload 24
    //   5052: astore 41
    //   5054: goto -4284 -> 770
    //   5057: aload 29
    //   5059: ifnonnull -106 -> 4953
    //   5062: ldc_w 1351
    //   5065: astore 29
    //   5067: goto -114 -> 4953
    //   5070: aload_1
    //   5071: invokevirtual 1313	android/net/Uri:getPathSegments	()Ljava/util/List;
    //   5074: iconst_1
    //   5075: invokeinterface 1319 2 0
    //   5080: checkcast 50	java/lang/String
    //   5083: wide
    //   5087: wide
    //   5091: invokestatic 1203	com/google/android/music/utils/MusicUtils:isNautilusId	(Ljava/lang/String;)Z
    //   5094: ifeq +6036 -> 11130
    //   5097: aload_0
    //   5098: getfield 1205	com/google/android/music/store/MusicContentProvider:mExecutor	Ljava/util/concurrent/ThreadPoolExecutor;
    //   5101: wide
    //   5105: aload_0
    //   5106: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   5109: wide
    //   5113: aload_1
    //   5114: wide
    //   5118: aload_2
    //   5119: wide
    //   5123: wide
    //   5127: wide
    //   5131: aload 21
    //   5133: wide
    //   5137: wide
    //   5141: wide
    //   5145: invokestatic 1210	com/google/android/music/store/NautilusContentProviderHelper:query	(Ljava/util/concurrent/ThreadPoolExecutor;Landroid/content/Context;ILandroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)Lcom/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult;
    //   5148: wide
    //   5152: wide
    //   5156: ifnonnull +9 -> 5165
    //   5159: aconst_null
    //   5160: astore 55
    //   5162: goto -4189 -> 973
    //   5165: wide
    //   5169: invokevirtual 1216	com/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult:getCursor	()Landroid/database/Cursor;
    //   5172: wide
    //   5176: wide
    //   5180: invokevirtual 1219	com/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult:getLocalId	()J
    //   5183: invokestatic 1224	java/lang/Long:toString	(J)Ljava/lang/String;
    //   5186: wide
    //   5190: wide
    //   5194: ldc_w 1377
    //   5197: invokevirtual 1380	com/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult:getExcludeClause	(Ljava/lang/String;)Ljava/lang/String;
    //   5200: wide
    //   5204: wide
    //   5208: wide
    //   5212: wide
    //   5216: astore 35
    //   5218: aload 13
    //   5220: ldc_w 1146
    //   5223: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   5226: aload 28
    //   5228: ifnonnull +10 -> 5238
    //   5231: aload_2
    //   5232: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   5235: ifeq +159 -> 5394
    //   5238: aconst_null
    //   5239: wide
    //   5243: getstatic 268	com/google/android/music/store/MusicContentProvider:sGroupedMusicProjectionMap	Ljava/util/HashMap;
    //   5246: wide
    //   5250: new 502	java/lang/StringBuilder
    //   5253: dup
    //   5254: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   5257: ldc_w 1382
    //   5260: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5263: wide
    //   5267: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5270: ldc_w 1323
    //   5273: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5276: ldc_w 330
    //   5279: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5282: ldc_w 1327
    //   5285: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5288: wide
    //   5292: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5295: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5298: wide
    //   5302: aload 18
    //   5304: wide
    //   5308: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5311: wide
    //   5315: aload_0
    //   5316: wide
    //   5320: aload 18
    //   5322: wide
    //   5326: aload_1
    //   5327: wide
    //   5331: wide
    //   5335: wide
    //   5339: wide
    //   5343: iconst_1
    //   5344: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   5347: aload 35
    //   5349: astore 26
    //   5351: wide
    //   5355: astore 25
    //   5357: aload 17
    //   5359: astore 42
    //   5361: aload 16
    //   5363: astore 43
    //   5365: wide
    //   5369: astore 28
    //   5371: aload 4
    //   5373: astore 44
    //   5375: aload 24
    //   5377: astore 41
    //   5379: wide
    //   5383: astore 45
    //   5385: aload_2
    //   5386: astore 46
    //   5388: aload_3
    //   5389: astore 47
    //   5391: goto -4621 -> 770
    //   5394: aload 29
    //   5396: ifnull +14 -> 5410
    //   5399: new 1025	java/lang/IllegalArgumentException
    //   5402: dup
    //   5403: ldc_w 1353
    //   5406: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   5409: athrow
    //   5410: aload_1
    //   5411: ldc_w 1182
    //   5414: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   5417: wide
    //   5421: aload_0
    //   5422: wide
    //   5426: iconst_1
    //   5427: invokespecial 1355	com/google/android/music/store/MusicContentProvider:getSortOrderFromQueryParam	(Ljava/lang/String;Z)Landroid/util/Pair;
    //   5430: getfield 1359	android/util/Pair:first	Ljava/lang/Object;
    //   5433: checkcast 50	java/lang/String
    //   5436: wide
    //   5440: wide
    //   5444: ifnonnull -201 -> 5243
    //   5447: ldc_w 1000
    //   5450: wide
    //   5454: goto -211 -> 5243
    //   5457: aload_1
    //   5458: invokevirtual 1199	android/net/Uri:getLastPathSegment	()Ljava/lang/String;
    //   5461: astore 44
    //   5463: aload 44
    //   5465: invokestatic 1203	com/google/android/music/utils/MusicUtils:isNautilusId	(Ljava/lang/String;)Z
    //   5468: ifeq +214 -> 5682
    //   5471: ldc_w 647
    //   5474: astore 28
    //   5476: aload_2
    //   5477: aload 28
    //   5479: invokestatic 1375	com/google/android/music/utils/DbUtils:injectColumnIntoProjection	([Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;
    //   5482: astore 42
    //   5484: aload_0
    //   5485: getfield 1205	com/google/android/music/store/MusicContentProvider:mExecutor	Ljava/util/concurrent/ThreadPoolExecutor;
    //   5488: wide
    //   5492: aload_0
    //   5493: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   5496: wide
    //   5500: aload_1
    //   5501: wide
    //   5505: wide
    //   5509: wide
    //   5513: aload 21
    //   5515: wide
    //   5519: aload 44
    //   5521: aload 42
    //   5523: invokestatic 1210	com/google/android/music/store/NautilusContentProviderHelper:query	(Ljava/util/concurrent/ThreadPoolExecutor;Landroid/content/Context;ILandroid/net/Uri;Ljava/lang/String;[Ljava/lang/String;)Lcom/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult;
    //   5526: wide
    //   5530: wide
    //   5534: ifnonnull +9 -> 5543
    //   5537: aconst_null
    //   5538: astore 55
    //   5540: goto -4567 -> 973
    //   5543: wide
    //   5547: invokevirtual 1216	com/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult:getCursor	()Landroid/database/Cursor;
    //   5550: astore 25
    //   5552: wide
    //   5556: invokevirtual 1219	com/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult:getLocalId	()J
    //   5559: invokestatic 1224	java/lang/Long:toString	(J)Ljava/lang/String;
    //   5562: wide
    //   5566: aload 13
    //   5568: ldc_w 1146
    //   5571: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   5574: getstatic 268	com/google/android/music/store/MusicContentProvider:sGroupedMusicProjectionMap	Ljava/util/HashMap;
    //   5577: astore 45
    //   5579: new 502	java/lang/StringBuilder
    //   5582: dup
    //   5583: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   5586: ldc_w 1384
    //   5589: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5592: wide
    //   5596: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5599: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5602: wide
    //   5606: aload 18
    //   5608: wide
    //   5612: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5615: wide
    //   5619: aload_0
    //   5620: wide
    //   5624: aload 18
    //   5626: wide
    //   5630: aload_1
    //   5631: wide
    //   5635: wide
    //   5639: wide
    //   5643: wide
    //   5647: iconst_1
    //   5648: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   5651: aload 16
    //   5653: astore 43
    //   5655: aload 4
    //   5657: astore 44
    //   5659: aload_3
    //   5660: astore 47
    //   5662: aload 42
    //   5664: astore 46
    //   5666: ldc_w 1377
    //   5669: astore 42
    //   5671: aload 28
    //   5673: astore 41
    //   5675: aload 29
    //   5677: astore 28
    //   5679: goto -4909 -> 770
    //   5682: aload 44
    //   5684: invokestatic 1388	java/lang/Long:parseLong	(Ljava/lang/String;)J
    //   5687: wide
    //   5691: aload 13
    //   5693: ldc_w 1146
    //   5696: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   5699: getstatic 244	com/google/android/music/store/MusicContentProvider:sMusicProjectionMap	Ljava/util/HashMap;
    //   5702: astore 45
    //   5704: aload_1
    //   5705: ldc_w 1390
    //   5708: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   5711: wide
    //   5715: wide
    //   5719: ifnonnull +72 -> 5791
    //   5722: new 502	java/lang/StringBuilder
    //   5725: dup
    //   5726: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   5729: ldc_w 1392
    //   5732: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5735: wide
    //   5739: invokevirtual 1395	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   5742: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5745: wide
    //   5749: aload 18
    //   5751: wide
    //   5755: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5758: wide
    //   5762: aload 17
    //   5764: astore 42
    //   5766: aload 16
    //   5768: astore 43
    //   5770: aload 29
    //   5772: astore 28
    //   5774: aload 4
    //   5776: astore 44
    //   5778: aload_3
    //   5779: astore 47
    //   5781: aload_2
    //   5782: astore 46
    //   5784: aload 24
    //   5786: astore 41
    //   5788: goto -5018 -> 770
    //   5791: ldc_w 336
    //   5794: astore 16
    //   5796: new 502	java/lang/StringBuilder
    //   5799: dup
    //   5800: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   5803: ldc_w 1397
    //   5806: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5809: wide
    //   5813: invokevirtual 1395	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   5816: ldc_w 775
    //   5819: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5822: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5825: wide
    //   5829: aload 18
    //   5831: wide
    //   5835: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5838: wide
    //   5842: ldc_w 1399
    //   5845: wide
    //   5849: invokevirtual 998	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   5852: ifeq +61 -> 5913
    //   5855: new 502	java/lang/StringBuilder
    //   5858: dup
    //   5859: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   5862: ldc_w 1401
    //   5865: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5868: wide
    //   5872: getstatic 1407	com/google/android/music/store/MusicFile:MEDIA_STORE_SOURCE_ACCOUNT_AS_STRING	Ljava/lang/String;
    //   5875: wide
    //   5879: wide
    //   5883: wide
    //   5887: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5890: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5893: wide
    //   5897: aload 18
    //   5899: wide
    //   5903: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5906: wide
    //   5910: goto -148 -> 5762
    //   5913: ldc 190
    //   5915: wide
    //   5919: invokevirtual 998	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   5922: ifeq +61 -> 5983
    //   5925: new 502	java/lang/StringBuilder
    //   5928: dup
    //   5929: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   5932: ldc_w 1409
    //   5935: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5938: wide
    //   5942: getstatic 1407	com/google/android/music/store/MusicFile:MEDIA_STORE_SOURCE_ACCOUNT_AS_STRING	Ljava/lang/String;
    //   5945: wide
    //   5949: wide
    //   5953: wide
    //   5957: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5960: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   5963: wide
    //   5967: aload 18
    //   5969: wide
    //   5973: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5976: wide
    //   5980: goto -218 -> 5762
    //   5983: new 502	java/lang/StringBuilder
    //   5986: dup
    //   5987: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   5990: ldc_w 1411
    //   5993: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   5996: wide
    //   6000: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   6003: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   6006: wide
    //   6010: new 1025	java/lang/IllegalArgumentException
    //   6013: dup
    //   6014: wide
    //   6018: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   6021: athrow
    //   6022: ldc_w 1413
    //   6025: invokestatic 1419	com/google/common/base/Splitter:on	(Ljava/lang/String;)Lcom/google/common/base/Splitter;
    //   6028: wide
    //   6032: aload_1
    //   6033: invokevirtual 1199	android/net/Uri:getLastPathSegment	()Ljava/lang/String;
    //   6036: wide
    //   6040: wide
    //   6044: wide
    //   6048: invokevirtual 1423	com/google/common/base/Splitter:split	(Ljava/lang/CharSequence;)Ljava/lang/Iterable;
    //   6051: invokeinterface 1429 1 0
    //   6056: wide
    //   6060: wide
    //   6064: invokeinterface 1434 1 0
    //   6069: ifne +14 -> 6083
    //   6072: new 1025	java/lang/IllegalArgumentException
    //   6075: dup
    //   6076: ldc_w 1436
    //   6079: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   6082: athrow
    //   6083: aconst_null
    //   6084: wide
    //   6088: new 1438	java/util/LinkedList
    //   6091: dup
    //   6092: invokespecial 1439	java/util/LinkedList:<init>	()V
    //   6095: wide
    //   6099: aload 12
    //   6101: invokevirtual 1258	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   6104: astore 35
    //   6106: getstatic 244	com/google/android/music/store/MusicContentProvider:sMusicProjectionMap	Ljava/util/HashMap;
    //   6109: wide
    //   6113: aload 13
    //   6115: wide
    //   6119: invokevirtual 1153	android/database/sqlite/SQLiteQueryBuilder:setProjectionMap	(Ljava/util/Map;)V
    //   6122: ldc_w 1441
    //   6125: astore 28
    //   6127: wide
    //   6131: invokeinterface 1434 1 0
    //   6136: ifeq +347 -> 6483
    //   6139: new 1231	java/lang/StringBuffer
    //   6142: dup
    //   6143: invokespecial 1232	java/lang/StringBuffer:<init>	()V
    //   6146: astore 33
    //   6148: aload 33
    //   6150: ldc_w 1443
    //   6153: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   6156: wide
    //   6160: wide
    //   6164: invokeinterface 1434 1 0
    //   6169: ifeq +4950 -> 11119
    //   6172: wide
    //   6176: invokeinterface 1447 1 0
    //   6181: checkcast 50	java/lang/String
    //   6184: wide
    //   6188: wide
    //   6192: invokestatic 1388	java/lang/Long:parseLong	(Ljava/lang/String;)J
    //   6195: wide
    //   6199: aload 33
    //   6201: ldc_w 1449
    //   6204: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   6207: wide
    //   6211: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   6214: ldc_w 1451
    //   6217: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   6220: wide
    //   6224: wide
    //   6228: iconst_1
    //   6229: iadd
    //   6230: wide
    //   6234: aload 33
    //   6236: wide
    //   6240: invokevirtual 1454	java/lang/StringBuffer:append	(I)Ljava/lang/StringBuffer;
    //   6243: ldc_w 1456
    //   6246: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   6249: wide
    //   6253: wide
    //   6257: sipush 200
    //   6260: irem
    //   6261: ifne +187 -> 6448
    //   6264: wide
    //   6268: wide
    //   6272: aload 33
    //   6274: ldc_w 1458
    //   6277: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   6280: ldc_w 1146
    //   6283: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   6286: ldc_w 1460
    //   6289: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   6292: wide
    //   6296: aload 33
    //   6298: ldc 246
    //   6300: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   6303: ldc_w 1327
    //   6306: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   6309: ldc_w 1462
    //   6312: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   6315: wide
    //   6319: aload 33
    //   6321: invokevirtual 1259	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   6324: wide
    //   6328: aload 13
    //   6330: wide
    //   6334: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   6337: aload 13
    //   6339: wide
    //   6343: aload_2
    //   6344: wide
    //   6348: aload 6
    //   6350: wide
    //   6354: wide
    //   6358: aload 35
    //   6360: wide
    //   6364: aconst_null
    //   6365: aconst_null
    //   6366: aconst_null
    //   6367: aconst_null
    //   6368: aload 28
    //   6370: aconst_null
    //   6371: wide
    //   6375: invokestatic 1467	com/google/android/music/utils/PostFroyoUtils$SQLiteDatabaseComp:query	(Landroid/database/sqlite/SQLiteQueryBuilder;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/android/music/utils/PostFroyoUtils$CancellationSignalComp;)Landroid/database/Cursor;
    //   6378: wide
    //   6382: wide
    //   6386: ifnull +51 -> 6437
    //   6389: aload_0
    //   6390: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   6393: invokevirtual 1268	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   6396: wide
    //   6400: aload_1
    //   6401: wide
    //   6405: wide
    //   6409: wide
    //   6413: wide
    //   6417: invokeinterface 1272 3 0
    //   6422: wide
    //   6426: wide
    //   6430: invokevirtual 1470	java/util/LinkedList:add	(Ljava/lang/Object;)Z
    //   6433: wide
    //   6437: wide
    //   6441: wide
    //   6445: goto -318 -> 6127
    //   6448: wide
    //   6452: invokeinterface 1434 1 0
    //   6457: ifeq +15 -> 6472
    //   6460: aload 33
    //   6462: ldc_w 1472
    //   6465: invokevirtual 1240	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   6468: wide
    //   6472: wide
    //   6476: wide
    //   6480: goto -320 -> 6160
    //   6483: wide
    //   6487: invokevirtual 1474	java/util/LinkedList:isEmpty	()Z
    //   6490: ifne +56 -> 6546
    //   6493: iconst_0
    //   6494: anewarray 943	android/database/Cursor
    //   6497: wide
    //   6501: wide
    //   6505: wide
    //   6509: invokevirtual 1478	java/util/LinkedList:toArray	([Ljava/lang/Object;)[Ljava/lang/Object;
    //   6512: checkcast 1480	[Landroid/database/Cursor;
    //   6515: wide
    //   6519: new 1482	com/google/android/music/store/CustomMergeCursor
    //   6522: dup
    //   6523: wide
    //   6527: invokespecial 1485	com/google/android/music/store/CustomMergeCursor:<init>	([Landroid/database/Cursor;)V
    //   6530: astore 105
    //   6532: aload 12
    //   6534: aload 35
    //   6536: invokevirtual 1279	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   6539: aload 105
    //   6541: astore 55
    //   6543: goto -5570 -> 973
    //   6546: aload 12
    //   6548: aload 35
    //   6550: invokevirtual 1279	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   6553: aload 22
    //   6555: astore 45
    //   6557: aload 17
    //   6559: astore 42
    //   6561: aload 16
    //   6563: astore 43
    //   6565: aload 4
    //   6567: astore 44
    //   6569: aload_3
    //   6570: astore 47
    //   6572: aload_2
    //   6573: astore 46
    //   6575: aload 24
    //   6577: astore 41
    //   6579: goto -5809 -> 770
    //   6582: wide
    //   6586: aload 12
    //   6588: aload 35
    //   6590: invokevirtual 1279	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   6593: wide
    //   6597: athrow
    //   6598: getstatic 465	com/google/android/music/store/MusicContentProvider:sAutoPlaylistsProjectionMap	Ljava/util/HashMap;
    //   6601: wide
    //   6605: aconst_null
    //   6606: dup
    //   6607: iconst_0
    //   6608: ldc2_w 1486
    //   6611: lastore
    //   6612: dup
    //   6613: iconst_1
    //   6614: ldc2_w 1488
    //   6617: lastore
    //   6618: dup
    //   6619: iconst_2
    //   6620: ldc2_w 1490
    //   6623: lastore
    //   6624: wide
    //   6628: aload_0
    //   6629: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   6632: wide
    //   6636: aload_2
    //   6637: wide
    //   6641: new 1493	com/google/android/music/store/AutoPlaylistCursor
    //   6644: dup
    //   6645: wide
    //   6649: wide
    //   6653: wide
    //   6657: invokespecial 1496	com/google/android/music/store/AutoPlaylistCursor:<init>	(Landroid/content/Context;[Ljava/lang/String;[J)V
    //   6660: astore 55
    //   6662: aload_0
    //   6663: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   6666: invokevirtual 1268	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   6669: wide
    //   6673: aload_1
    //   6674: wide
    //   6678: aload 55
    //   6680: wide
    //   6684: wide
    //   6688: invokeinterface 1272 3 0
    //   6693: goto -5720 -> 973
    //   6696: getstatic 465	com/google/android/music/store/MusicContentProvider:sAutoPlaylistsProjectionMap	Ljava/util/HashMap;
    //   6699: wide
    //   6703: aload_1
    //   6704: invokevirtual 1313	android/net/Uri:getPathSegments	()Ljava/util/List;
    //   6707: iconst_1
    //   6708: invokeinterface 1319 2 0
    //   6713: checkcast 50	java/lang/String
    //   6716: invokestatic 1388	java/lang/Long:parseLong	(Ljava/lang/String;)J
    //   6719: invokestatic 1502	com/google/android/music/store/MusicContent$AutoPlaylists:uriIdToAutoPlaylistId	(J)J
    //   6722: wide
    //   6726: aconst_null
    //   6727: wide
    //   6731: wide
    //   6735: iconst_0
    //   6736: wide
    //   6740: lastore
    //   6741: aload_0
    //   6742: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   6745: wide
    //   6749: aload_2
    //   6750: wide
    //   6754: new 1493	com/google/android/music/store/AutoPlaylistCursor
    //   6757: dup
    //   6758: wide
    //   6762: wide
    //   6766: wide
    //   6770: invokespecial 1496	com/google/android/music/store/AutoPlaylistCursor:<init>	(Landroid/content/Context;[Ljava/lang/String;[J)V
    //   6773: astore 55
    //   6775: aload_0
    //   6776: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   6779: invokevirtual 1268	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   6782: wide
    //   6786: aload_1
    //   6787: wide
    //   6791: aload 55
    //   6793: wide
    //   6797: wide
    //   6801: invokeinterface 1272 3 0
    //   6806: goto -5833 -> 973
    //   6809: getstatic 467	com/google/android/music/store/MusicContentProvider:sAutoPlaylistMembersProjectionMap	Ljava/util/HashMap;
    //   6812: wide
    //   6816: aload_1
    //   6817: invokevirtual 1313	android/net/Uri:getPathSegments	()Ljava/util/List;
    //   6820: iconst_1
    //   6821: invokeinterface 1319 2 0
    //   6826: checkcast 50	java/lang/String
    //   6829: invokestatic 1388	java/lang/Long:parseLong	(Ljava/lang/String;)J
    //   6832: invokestatic 1502	com/google/android/music/store/MusicContent$AutoPlaylists:uriIdToAutoPlaylistId	(J)J
    //   6835: wide
    //   6839: wide
    //   6843: ldc2_w 1503
    //   6846: lcmp
    //   6847: ifne +242 -> 7089
    //   6850: aload_0
    //   6851: wide
    //   6855: aload 18
    //   6857: wide
    //   6861: aload_1
    //   6862: wide
    //   6866: wide
    //   6870: wide
    //   6874: wide
    //   6878: iconst_1
    //   6879: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   6882: aload 18
    //   6884: ldc_w 1506
    //   6887: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   6890: wide
    //   6894: aload 28
    //   6896: ifnonnull +10 -> 6906
    //   6899: aload_2
    //   6900: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   6903: ifeq +53 -> 6956
    //   6906: aload 13
    //   6908: ldc_w 1146
    //   6911: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   6914: aconst_null
    //   6915: wide
    //   6919: aload 17
    //   6921: astore 42
    //   6923: aload 16
    //   6925: astore 43
    //   6927: wide
    //   6931: astore 28
    //   6933: aload 4
    //   6935: astore 44
    //   6937: aload_3
    //   6938: astore 47
    //   6940: wide
    //   6944: astore 45
    //   6946: aload 24
    //   6948: astore 41
    //   6950: aload_2
    //   6951: astore 46
    //   6953: goto -6183 -> 770
    //   6956: aload 13
    //   6958: ldc_w 1146
    //   6961: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   6964: aload 29
    //   6966: ifnull +14 -> 6980
    //   6969: new 1025	java/lang/IllegalArgumentException
    //   6972: dup
    //   6973: ldc_w 1353
    //   6976: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   6979: athrow
    //   6980: aload_1
    //   6981: ldc_w 1182
    //   6984: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   6987: wide
    //   6991: aload_0
    //   6992: wide
    //   6996: iconst_1
    //   6997: invokespecial 1355	com/google/android/music/store/MusicContentProvider:getSortOrderFromQueryParam	(Ljava/lang/String;Z)Landroid/util/Pair;
    //   7000: getfield 1359	android/util/Pair:first	Ljava/lang/Object;
    //   7003: checkcast 50	java/lang/String
    //   7006: wide
    //   7010: wide
    //   7014: ifnonnull +3 -> 7017
    //   7017: new 502	java/lang/StringBuilder
    //   7020: dup
    //   7021: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   7024: ldc_w 1508
    //   7027: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7030: wide
    //   7034: aload 18
    //   7036: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7039: wide
    //   7043: wide
    //   7047: wide
    //   7051: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7054: ldc_w 1510
    //   7057: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7060: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7063: wide
    //   7067: aload 18
    //   7069: iconst_0
    //   7070: invokevirtual 1511	java/lang/StringBuilder:setLength	(I)V
    //   7073: aload 18
    //   7075: wide
    //   7079: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7082: wide
    //   7086: goto -167 -> 6919
    //   7089: wide
    //   7093: ldc2_w 1512
    //   7096: lcmp
    //   7097: ifne +286 -> 7383
    //   7100: aload 28
    //   7102: ifnonnull +10 -> 7112
    //   7105: aload_2
    //   7106: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   7109: ifeq +193 -> 7302
    //   7112: aload 13
    //   7114: ldc_w 1146
    //   7117: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   7120: aconst_null
    //   7121: wide
    //   7125: aload_0
    //   7126: wide
    //   7130: aload 18
    //   7132: wide
    //   7136: aload_1
    //   7137: wide
    //   7141: wide
    //   7145: wide
    //   7149: wide
    //   7153: iconst_1
    //   7154: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   7157: new 502	java/lang/StringBuilder
    //   7160: dup
    //   7161: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   7164: ldc_w 1515
    //   7167: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7170: wide
    //   7174: aload 18
    //   7176: invokevirtual 736	java/lang/StringBuilder:length	()I
    //   7179: ifle +194 -> 7373
    //   7182: new 502	java/lang/StringBuilder
    //   7185: dup
    //   7186: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   7189: ldc_w 769
    //   7192: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7195: wide
    //   7199: aload 18
    //   7201: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7204: wide
    //   7208: wide
    //   7212: wide
    //   7216: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7219: ldc_w 885
    //   7222: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7225: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7228: wide
    //   7232: wide
    //   7236: wide
    //   7240: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7243: ldc_w 1194
    //   7246: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7249: ldc_w 1021
    //   7252: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7255: ldc_w 1196
    //   7258: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7261: sipush 500
    //   7264: invokevirtual 811	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   7267: ldc_w 885
    //   7270: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7273: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7276: wide
    //   7280: aload 18
    //   7282: iconst_0
    //   7283: invokevirtual 1511	java/lang/StringBuilder:setLength	(I)V
    //   7286: aload 18
    //   7288: wide
    //   7292: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7295: wide
    //   7299: goto -380 -> 6919
    //   7302: aload 13
    //   7304: ldc_w 1146
    //   7307: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   7310: aload 29
    //   7312: ifnull +14 -> 7326
    //   7315: new 1025	java/lang/IllegalArgumentException
    //   7318: dup
    //   7319: ldc_w 1353
    //   7322: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   7325: athrow
    //   7326: aload_1
    //   7327: ldc_w 1182
    //   7330: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   7333: wide
    //   7337: aload_0
    //   7338: wide
    //   7342: iconst_1
    //   7343: invokespecial 1355	com/google/android/music/store/MusicContentProvider:getSortOrderFromQueryParam	(Ljava/lang/String;Z)Landroid/util/Pair;
    //   7346: getfield 1359	android/util/Pair:first	Ljava/lang/Object;
    //   7349: checkcast 50	java/lang/String
    //   7352: wide
    //   7356: wide
    //   7360: ifnonnull -235 -> 7125
    //   7363: ldc_w 1019
    //   7366: wide
    //   7370: goto -245 -> 7125
    //   7373: ldc_w 914
    //   7376: wide
    //   7380: goto -148 -> 7232
    //   7383: wide
    //   7387: ldc2_w 1516
    //   7390: lcmp
    //   7391: ifne +275 -> 7666
    //   7394: aload_1
    //   7395: ldc_w 1519
    //   7398: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   7401: wide
    //   7405: wide
    //   7409: invokestatic 732	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   7412: ifeq +75 -> 7487
    //   7415: aload_0
    //   7416: wide
    //   7420: aload 18
    //   7422: wide
    //   7426: aload_1
    //   7427: wide
    //   7431: wide
    //   7435: wide
    //   7439: wide
    //   7443: iconst_1
    //   7444: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   7447: aload 18
    //   7449: ldc_w 1521
    //   7452: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7455: wide
    //   7459: aload 28
    //   7461: ifnonnull +10 -> 7471
    //   7464: aload_2
    //   7465: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   7468: ifeq +81 -> 7549
    //   7471: aload 13
    //   7473: ldc_w 1146
    //   7476: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   7479: aconst_null
    //   7480: wide
    //   7484: goto -565 -> 6919
    //   7487: new 502	java/lang/StringBuilder
    //   7490: dup
    //   7491: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   7494: ldc_w 1523
    //   7497: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7500: wide
    //   7504: wide
    //   7508: invokestatic 1286	com/google/android/music/utils/DbUtils:quoteStringValue	(Ljava/lang/String;)Ljava/lang/String;
    //   7511: wide
    //   7515: wide
    //   7519: wide
    //   7523: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7526: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7529: wide
    //   7533: aload 18
    //   7535: wide
    //   7539: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7542: wide
    //   7546: goto -99 -> 7447
    //   7549: aload 13
    //   7551: ldc_w 1146
    //   7554: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   7557: aload_1
    //   7558: ldc_w 1182
    //   7561: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   7564: wide
    //   7568: aload_0
    //   7569: wide
    //   7573: iconst_1
    //   7574: invokespecial 1355	com/google/android/music/store/MusicContentProvider:getSortOrderFromQueryParam	(Ljava/lang/String;Z)Landroid/util/Pair;
    //   7577: getfield 1359	android/util/Pair:first	Ljava/lang/Object;
    //   7580: checkcast 50	java/lang/String
    //   7583: wide
    //   7587: wide
    //   7591: ifnonnull +3 -> 7594
    //   7594: new 502	java/lang/StringBuilder
    //   7597: dup
    //   7598: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   7601: ldc_w 1508
    //   7604: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7607: wide
    //   7611: aload 18
    //   7613: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7616: wide
    //   7620: wide
    //   7624: wide
    //   7628: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7631: ldc_w 1510
    //   7634: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7637: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7640: wide
    //   7644: aload 18
    //   7646: iconst_0
    //   7647: invokevirtual 1511	java/lang/StringBuilder:setLength	(I)V
    //   7650: aload 18
    //   7652: wide
    //   7656: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7659: wide
    //   7663: goto -744 -> 6919
    //   7666: wide
    //   7670: ldc2_w 1524
    //   7673: lcmp
    //   7674: ifne +201 -> 7875
    //   7677: aload_1
    //   7678: ldc_w 1519
    //   7681: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   7684: wide
    //   7688: wide
    //   7692: invokestatic 732	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   7695: ifeq +63 -> 7758
    //   7698: aload_0
    //   7699: wide
    //   7703: aload 18
    //   7705: wide
    //   7709: aload_1
    //   7710: wide
    //   7714: wide
    //   7718: wide
    //   7722: wide
    //   7726: iconst_0
    //   7727: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   7730: aload 28
    //   7732: ifnonnull +10 -> 7742
    //   7735: aload_2
    //   7736: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   7739: ifeq +81 -> 7820
    //   7742: aload 13
    //   7744: ldc_w 1146
    //   7747: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   7750: aconst_null
    //   7751: wide
    //   7755: goto -836 -> 6919
    //   7758: new 502	java/lang/StringBuilder
    //   7761: dup
    //   7762: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   7765: ldc_w 1523
    //   7768: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7771: wide
    //   7775: wide
    //   7779: invokestatic 1286	com/google/android/music/utils/DbUtils:quoteStringValue	(Ljava/lang/String;)Ljava/lang/String;
    //   7782: wide
    //   7786: wide
    //   7790: wide
    //   7794: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7797: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7800: wide
    //   7804: aload 18
    //   7806: wide
    //   7810: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7813: wide
    //   7817: goto -87 -> 7730
    //   7820: aload 13
    //   7822: ldc_w 1146
    //   7825: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   7828: aload_1
    //   7829: ldc_w 1182
    //   7832: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   7835: wide
    //   7839: aload_0
    //   7840: wide
    //   7844: iconst_1
    //   7845: invokespecial 1355	com/google/android/music/store/MusicContentProvider:getSortOrderFromQueryParam	(Ljava/lang/String;Z)Landroid/util/Pair;
    //   7848: getfield 1359	android/util/Pair:first	Ljava/lang/Object;
    //   7851: checkcast 50	java/lang/String
    //   7854: wide
    //   7858: wide
    //   7862: ifnonnull -943 -> 6919
    //   7865: ldc_w 1019
    //   7868: wide
    //   7872: goto -953 -> 6919
    //   7875: new 502	java/lang/StringBuilder
    //   7878: dup
    //   7879: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   7882: ldc_w 1527
    //   7885: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   7888: wide
    //   7892: aload_1
    //   7893: wide
    //   7897: wide
    //   7901: wide
    //   7905: invokevirtual 512	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   7908: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   7911: wide
    //   7915: new 1025	java/lang/IllegalArgumentException
    //   7918: dup
    //   7919: wide
    //   7923: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   7926: athrow
    //   7927: getstatic 418	com/google/android/music/store/MusicContentProvider:sPlaylistsProjectionMap	Ljava/util/HashMap;
    //   7930: wide
    //   7934: aload 28
    //   7936: ifnonnull +10 -> 7946
    //   7939: aload_2
    //   7940: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   7943: ifeq +152 -> 8095
    //   7946: aload 13
    //   7948: ldc_w 1529
    //   7951: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   7954: aconst_null
    //   7955: wide
    //   7959: aload_1
    //   7960: ldc_w 276
    //   7963: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   7966: wide
    //   7970: wide
    //   7974: ifnull +139 -> 8113
    //   7977: wide
    //   7981: invokevirtual 994	java/lang/String:length	()I
    //   7984: ifle +129 -> 8113
    //   7987: ldc_w 1531
    //   7990: astore_3
    //   7991: iconst_1
    //   7992: anewarray 50	java/lang/String
    //   7995: astore 4
    //   7997: aload 4
    //   7999: iconst_0
    //   8000: wide
    //   8004: aastore
    //   8005: aconst_null
    //   8006: astore 35
    //   8008: aload_1
    //   8009: ldc_w 1533
    //   8012: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   8015: wide
    //   8019: wide
    //   8023: invokestatic 732	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   8026: ifne +116 -> 8142
    //   8029: wide
    //   8033: invokestatic 1536	java/lang/Boolean:valueOf	(Ljava/lang/String;)Ljava/lang/Boolean;
    //   8036: invokevirtual 1367	java/lang/Boolean:booleanValue	()Z
    //   8039: ifeq +103 -> 8142
    //   8042: aload 18
    //   8044: ldc_w 1538
    //   8047: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8050: wide
    //   8054: aload 17
    //   8056: astore 42
    //   8058: aload 16
    //   8060: astore 43
    //   8062: aload 35
    //   8064: astore 15
    //   8066: wide
    //   8070: astore 28
    //   8072: aload 4
    //   8074: astore 44
    //   8076: aload_3
    //   8077: astore 47
    //   8079: aload 24
    //   8081: astore 41
    //   8083: wide
    //   8087: astore 45
    //   8089: aload_2
    //   8090: astore 46
    //   8092: goto -7322 -> 770
    //   8095: aload 13
    //   8097: ldc_w 1540
    //   8100: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   8103: ldc_w 1542
    //   8106: wide
    //   8110: goto -151 -> 7959
    //   8113: aload 18
    //   8115: wide
    //   8119: aload_1
    //   8120: wide
    //   8124: wide
    //   8128: wide
    //   8132: invokestatic 1544	com/google/android/music/store/MusicContentProvider:appendPlaylistFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;)V
    //   8135: aload 15
    //   8137: astore 35
    //   8139: goto -131 -> 8008
    //   8142: aload 18
    //   8144: ldc_w 1546
    //   8147: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8150: wide
    //   8154: aload 17
    //   8156: astore 42
    //   8158: aload 16
    //   8160: astore 43
    //   8162: aload 35
    //   8164: astore 15
    //   8166: wide
    //   8170: astore 28
    //   8172: aload 4
    //   8174: astore 44
    //   8176: aload_3
    //   8177: astore 47
    //   8179: aload 24
    //   8181: astore 41
    //   8183: wide
    //   8187: astore 45
    //   8189: aload_2
    //   8190: astore 46
    //   8192: goto -7422 -> 770
    //   8195: getstatic 418	com/google/android/music/store/MusicContentProvider:sPlaylistsProjectionMap	Ljava/util/HashMap;
    //   8198: astore 35
    //   8200: aload 28
    //   8202: ifnonnull +10 -> 8212
    //   8205: aload_2
    //   8206: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   8209: ifeq +119 -> 8328
    //   8212: aload 13
    //   8214: ldc_w 1548
    //   8217: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   8220: aconst_null
    //   8221: wide
    //   8225: aload_1
    //   8226: ldc_w 1533
    //   8229: invokevirtual 979	android/net/Uri:getQueryParameter	(Ljava/lang/String;)Ljava/lang/String;
    //   8232: wide
    //   8236: wide
    //   8240: invokestatic 732	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   8243: ifne +103 -> 8346
    //   8246: wide
    //   8250: invokestatic 1536	java/lang/Boolean:valueOf	(Ljava/lang/String;)Ljava/lang/Boolean;
    //   8253: invokevirtual 1367	java/lang/Boolean:booleanValue	()Z
    //   8256: ifeq +90 -> 8346
    //   8259: aload 18
    //   8261: ldc_w 1538
    //   8264: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8267: wide
    //   8271: aload 18
    //   8273: wide
    //   8277: aload_1
    //   8278: wide
    //   8282: wide
    //   8286: wide
    //   8290: invokestatic 1544	com/google/android/music/store/MusicContentProvider:appendPlaylistFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;)V
    //   8293: aload 17
    //   8295: astore 42
    //   8297: aload 16
    //   8299: astore 43
    //   8301: wide
    //   8305: astore 28
    //   8307: aload 4
    //   8309: astore 44
    //   8311: aload_3
    //   8312: astore 47
    //   8314: aload_2
    //   8315: astore 46
    //   8317: aload 24
    //   8319: astore 41
    //   8321: aload 35
    //   8323: astore 45
    //   8325: goto -7555 -> 770
    //   8328: aload 13
    //   8330: ldc_w 1550
    //   8333: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   8336: ldc_w 1552
    //   8339: wide
    //   8343: goto -118 -> 8225
    //   8346: aload 18
    //   8348: ldc_w 1546
    //   8351: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8354: wide
    //   8358: goto -87 -> 8271
    //   8361: getstatic 418	com/google/android/music/store/MusicContentProvider:sPlaylistsProjectionMap	Ljava/util/HashMap;
    //   8364: astore 45
    //   8366: aload 28
    //   8368: ifnonnull +10 -> 8378
    //   8371: aload_2
    //   8372: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   8375: ifeq +39 -> 8414
    //   8378: aload 13
    //   8380: ldc_w 1554
    //   8383: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   8386: aload 17
    //   8388: astore 42
    //   8390: aload 16
    //   8392: astore 43
    //   8394: aconst_null
    //   8395: astore 28
    //   8397: aload 4
    //   8399: astore 44
    //   8401: aload_3
    //   8402: astore 47
    //   8404: aload_2
    //   8405: astore 46
    //   8407: aload 24
    //   8409: astore 41
    //   8411: goto -7641 -> 770
    //   8414: aload 13
    //   8416: ldc_w 1556
    //   8419: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   8422: aload 17
    //   8424: astore 42
    //   8426: aload 16
    //   8428: astore 43
    //   8430: ldc_w 1558
    //   8433: astore 28
    //   8435: aload 4
    //   8437: astore 44
    //   8439: aload_3
    //   8440: astore 47
    //   8442: aload_2
    //   8443: astore 46
    //   8445: aload 24
    //   8447: astore 41
    //   8449: goto -7679 -> 770
    //   8452: getstatic 567	com/google/android/music/store/MusicContentProvider:sRadioProjectionMap	Ljava/util/HashMap;
    //   8455: astore 35
    //   8457: aload 28
    //   8459: ifnonnull +10 -> 8469
    //   8462: aload_2
    //   8463: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   8466: ifeq +51 -> 8517
    //   8469: aconst_null
    //   8470: wide
    //   8474: aload 13
    //   8476: ldc_w 1560
    //   8479: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   8482: aload 17
    //   8484: astore 42
    //   8486: aload 16
    //   8488: astore 43
    //   8490: wide
    //   8494: astore 28
    //   8496: aload 4
    //   8498: astore 44
    //   8500: aload_3
    //   8501: astore 47
    //   8503: aload_2
    //   8504: astore 46
    //   8506: aload 24
    //   8508: astore 41
    //   8510: aload 35
    //   8512: astore 45
    //   8514: goto -7744 -> 770
    //   8517: ldc_w 1562
    //   8520: wide
    //   8524: goto -50 -> 8474
    //   8527: aload 13
    //   8529: ldc_w 1560
    //   8532: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   8535: getstatic 567	com/google/android/music/store/MusicContentProvider:sRadioProjectionMap	Ljava/util/HashMap;
    //   8538: wide
    //   8542: aload_1
    //   8543: invokevirtual 1313	android/net/Uri:getPathSegments	()Ljava/util/List;
    //   8546: iconst_1
    //   8547: invokeinterface 1319 2 0
    //   8552: checkcast 50	java/lang/String
    //   8555: wide
    //   8559: new 502	java/lang/StringBuilder
    //   8562: dup
    //   8563: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   8566: ldc_w 1564
    //   8569: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8572: wide
    //   8576: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8579: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   8582: wide
    //   8586: aload 18
    //   8588: wide
    //   8592: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8595: wide
    //   8599: wide
    //   8603: astore 45
    //   8605: aload 17
    //   8607: astore 42
    //   8609: aload 16
    //   8611: astore 43
    //   8613: aload 29
    //   8615: astore 28
    //   8617: aload 4
    //   8619: astore 44
    //   8621: aload_3
    //   8622: astore 47
    //   8624: aload_2
    //   8625: astore 46
    //   8627: aload 24
    //   8629: astore 41
    //   8631: goto -7861 -> 770
    //   8634: aload_0
    //   8635: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   8638: wide
    //   8642: aload_1
    //   8643: wide
    //   8647: aload_2
    //   8648: wide
    //   8652: wide
    //   8656: wide
    //   8660: aload 21
    //   8662: wide
    //   8666: invokestatic 1569	com/google/android/music/store/SharedContentProviderHelper:query	(Landroid/content/Context;Landroid/net/Uri;I[Ljava/lang/String;)Landroid/database/Cursor;
    //   8669: astore 55
    //   8671: aload 55
    //   8673: ifnull -7700 -> 973
    //   8676: aload_0
    //   8677: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   8680: invokevirtual 1268	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   8683: wide
    //   8687: aload_1
    //   8688: wide
    //   8692: aload 55
    //   8694: wide
    //   8698: wide
    //   8702: invokeinterface 1272 3 0
    //   8707: aload 55
    //   8709: invokeinterface 1275 1 0
    //   8714: wide
    //   8718: goto -7745 -> 973
    //   8721: aload 13
    //   8723: ldc_w 1540
    //   8726: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   8729: new 502	java/lang/StringBuilder
    //   8732: dup
    //   8733: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   8736: ldc_w 1571
    //   8739: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8742: wide
    //   8746: aload_1
    //   8747: invokevirtual 1199	android/net/Uri:getLastPathSegment	()Ljava/lang/String;
    //   8750: wide
    //   8754: wide
    //   8758: wide
    //   8762: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8765: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   8768: wide
    //   8772: aload 18
    //   8774: wide
    //   8778: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8781: wide
    //   8785: getstatic 418	com/google/android/music/store/MusicContentProvider:sPlaylistsProjectionMap	Ljava/util/HashMap;
    //   8788: astore 45
    //   8790: aload 17
    //   8792: astore 42
    //   8794: aload 16
    //   8796: astore 43
    //   8798: aconst_null
    //   8799: astore 28
    //   8801: aload 4
    //   8803: astore 44
    //   8805: aload_3
    //   8806: astore 47
    //   8808: aload_2
    //   8809: astore 46
    //   8811: aload 24
    //   8813: astore 41
    //   8815: goto -8045 -> 770
    //   8818: aload 13
    //   8820: ldc_w 1573
    //   8823: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   8826: getstatic 461	com/google/android/music/store/MusicContentProvider:sPlaylistMembersProjectionMap	Ljava/util/HashMap;
    //   8829: astore 35
    //   8831: aload_1
    //   8832: invokevirtual 1313	android/net/Uri:getPathSegments	()Ljava/util/List;
    //   8835: iconst_1
    //   8836: invokeinterface 1319 2 0
    //   8841: checkcast 50	java/lang/String
    //   8844: invokestatic 1388	java/lang/Long:parseLong	(Ljava/lang/String;)J
    //   8847: wide
    //   8851: aload_0
    //   8852: wide
    //   8856: aload 18
    //   8858: wide
    //   8862: aload_1
    //   8863: wide
    //   8867: wide
    //   8871: wide
    //   8875: wide
    //   8879: iconst_1
    //   8880: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   8883: new 502	java/lang/StringBuilder
    //   8886: dup
    //   8887: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   8890: ldc_w 1571
    //   8893: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8896: wide
    //   8900: invokevirtual 1395	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   8903: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   8906: wide
    //   8910: aload 18
    //   8912: wide
    //   8916: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   8919: wide
    //   8923: aload 28
    //   8925: ifnonnull +10 -> 8935
    //   8928: aload_2
    //   8929: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   8932: ifeq +35 -> 8967
    //   8935: aload 35
    //   8937: astore 45
    //   8939: aload 17
    //   8941: astore 42
    //   8943: aload 16
    //   8945: astore 43
    //   8947: aconst_null
    //   8948: astore 28
    //   8950: aload 4
    //   8952: astore 44
    //   8954: aload_3
    //   8955: astore 47
    //   8957: aload_2
    //   8958: astore 46
    //   8960: aload 24
    //   8962: astore 41
    //   8964: goto -8194 -> 770
    //   8967: ldc_w 1575
    //   8970: astore 29
    //   8972: aload 12
    //   8974: wide
    //   8978: invokevirtual 1578	com/google/android/music/store/Store:getShareTokenIfSharedPlaylist	(J)Ljava/lang/String;
    //   8981: wide
    //   8985: wide
    //   8989: ifnull +30 -> 9019
    //   8992: aload_0
    //   8993: invokespecial 1579	com/google/android/music/store/MusicContentProvider:getStreamingAccount	()Landroid/accounts/Account;
    //   8996: wide
    //   9000: aload_0
    //   9001: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   9004: wide
    //   9008: wide
    //   9012: wide
    //   9016: invokestatic 1583	com/google/android/music/store/SharedContentProviderHelper:updateFollowedSharedPlaylist	(Landroid/content/Context;Landroid/accounts/Account;JLjava/lang/String;)V
    //   9019: aload 35
    //   9021: astore 45
    //   9023: aload 17
    //   9025: astore 42
    //   9027: aload 16
    //   9029: astore 43
    //   9031: aload 29
    //   9033: astore 28
    //   9035: aload 4
    //   9037: astore 44
    //   9039: aload_3
    //   9040: astore 47
    //   9042: aload_2
    //   9043: astore 46
    //   9045: aload 24
    //   9047: astore 41
    //   9049: goto -8279 -> 770
    //   9052: aload 13
    //   9054: ldc_w 1573
    //   9057: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   9060: getstatic 461	com/google/android/music/store/MusicContentProvider:sPlaylistMembersProjectionMap	Ljava/util/HashMap;
    //   9063: wide
    //   9067: aload_1
    //   9068: invokevirtual 1313	android/net/Uri:getPathSegments	()Ljava/util/List;
    //   9071: iconst_1
    //   9072: invokeinterface 1319 2 0
    //   9077: checkcast 50	java/lang/String
    //   9080: wide
    //   9084: aload_1
    //   9085: invokevirtual 1313	android/net/Uri:getPathSegments	()Ljava/util/List;
    //   9088: iconst_3
    //   9089: invokeinterface 1319 2 0
    //   9094: checkcast 50	java/lang/String
    //   9097: wide
    //   9101: new 502	java/lang/StringBuilder
    //   9104: dup
    //   9105: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   9108: ldc_w 1571
    //   9111: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9114: wide
    //   9118: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9121: ldc_w 738
    //   9124: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9127: ldc_w 463
    //   9130: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9133: ldc_w 1327
    //   9136: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9139: wide
    //   9143: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9146: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   9149: wide
    //   9153: aload 18
    //   9155: wide
    //   9159: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9162: wide
    //   9166: wide
    //   9170: astore 45
    //   9172: aload 17
    //   9174: astore 42
    //   9176: aload 16
    //   9178: astore 43
    //   9180: aload 29
    //   9182: astore 28
    //   9184: aload 4
    //   9186: astore 44
    //   9188: aload_3
    //   9189: astore 47
    //   9191: aload 24
    //   9193: astore 41
    //   9195: aload_2
    //   9196: astore 46
    //   9198: goto -8428 -> 770
    //   9201: aload_2
    //   9202: ifnonnull +7 -> 9209
    //   9205: getstatic 70	com/google/android/music/store/MusicContentProvider:DEFAULT_SEARCH_SUGGESTIONS_PROJECTION	[Ljava/lang/String;
    //   9208: astore_2
    //   9209: iconst_1
    //   9210: istore 23
    //   9212: aload 13
    //   9214: ldc_w 1585
    //   9217: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   9220: getstatic 469	com/google/android/music/store/MusicContentProvider:sRecentProjectionMap	Ljava/util/HashMap;
    //   9223: astore 35
    //   9225: aload 28
    //   9227: ifnonnull +27 -> 9254
    //   9230: aload_2
    //   9231: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   9234: ifne +20 -> 9254
    //   9237: aload 23
    //   9239: ifnonnull +15 -> 9254
    //   9242: ldc_w 1552
    //   9245: astore 29
    //   9247: bipush 50
    //   9249: invokestatic 1587	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   9252: astore 16
    //   9254: aload_1
    //   9255: invokestatic 759	com/google/android/music/store/MusicContentProvider:getMusicFilterIndex	(Landroid/net/Uri;)I
    //   9258: wide
    //   9262: ldc_w 914
    //   9265: wide
    //   9269: wide
    //   9273: ifeq +50 -> 9323
    //   9276: new 502	java/lang/StringBuilder
    //   9279: dup
    //   9280: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   9283: ldc_w 738
    //   9286: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9289: wide
    //   9293: getstatic 754	com/google/android/music/store/Filters:FILTERS	[Ljava/lang/String;
    //   9296: wide
    //   9300: aaload
    //   9301: wide
    //   9305: wide
    //   9309: wide
    //   9313: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9316: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   9319: wide
    //   9323: new 502	java/lang/StringBuilder
    //   9326: dup
    //   9327: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   9330: ldc_w 1589
    //   9333: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9336: wide
    //   9340: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9343: ldc_w 885
    //   9346: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9349: ldc_w 885
    //   9352: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9355: ldc_w 738
    //   9358: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9361: ldc_w 475
    //   9364: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9367: ldc_w 1591
    //   9370: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9373: iconst_5
    //   9374: invokevirtual 811	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   9377: ldc_w 1413
    //   9380: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9383: bipush 6
    //   9385: invokevirtual 811	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   9388: ldc_w 885
    //   9391: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9394: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   9397: wide
    //   9401: aload 18
    //   9403: wide
    //   9407: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9410: wide
    //   9414: aload 35
    //   9416: astore 45
    //   9418: aload 17
    //   9420: astore 42
    //   9422: aload 16
    //   9424: astore 43
    //   9426: aload 29
    //   9428: astore 28
    //   9430: aload 4
    //   9432: astore 44
    //   9434: aload_3
    //   9435: astore 47
    //   9437: aload_2
    //   9438: astore 46
    //   9440: aload 24
    //   9442: astore 41
    //   9444: goto -8674 -> 770
    //   9447: aload_0
    //   9448: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   9451: wide
    //   9455: aload_1
    //   9456: wide
    //   9460: aload_2
    //   9461: wide
    //   9465: aload 12
    //   9467: wide
    //   9471: wide
    //   9475: wide
    //   9479: wide
    //   9483: wide
    //   9487: invokestatic 1597	com/google/android/music/store/MainstageContentProviderHelper:getMainstageContent	(Landroid/content/Context;Landroid/net/Uri;[Ljava/lang/String;Lcom/google/android/music/store/Store;)Landroid/database/Cursor;
    //   9490: astore 55
    //   9492: goto -8519 -> 973
    //   9495: aload 13
    //   9497: ldc_w 1599
    //   9500: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   9503: getstatic 533	com/google/android/music/store/MusicContentProvider:sKeepOnProjectionMap	Ljava/util/HashMap;
    //   9506: astore 45
    //   9508: aload 17
    //   9510: astore 42
    //   9512: aload 16
    //   9514: astore 43
    //   9516: aload 29
    //   9518: astore 28
    //   9520: aload 4
    //   9522: astore 44
    //   9524: aload_3
    //   9525: astore 47
    //   9527: aload_2
    //   9528: astore 46
    //   9530: aload 24
    //   9532: astore 41
    //   9534: goto -8764 -> 770
    //   9537: aload 13
    //   9539: ldc_w 1599
    //   9542: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   9545: getstatic 533	com/google/android/music/store/MusicContentProvider:sKeepOnProjectionMap	Ljava/util/HashMap;
    //   9548: astore 45
    //   9550: new 502	java/lang/StringBuilder
    //   9553: dup
    //   9554: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   9557: ldc_w 1601
    //   9560: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9563: wide
    //   9567: aload_1
    //   9568: invokevirtual 1199	android/net/Uri:getLastPathSegment	()Ljava/lang/String;
    //   9571: wide
    //   9575: wide
    //   9579: wide
    //   9583: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9586: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   9589: wide
    //   9593: aload 18
    //   9595: wide
    //   9599: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9602: wide
    //   9606: aload 17
    //   9608: astore 42
    //   9610: aload 16
    //   9612: astore 43
    //   9614: aload 29
    //   9616: astore 28
    //   9618: aload 4
    //   9620: astore 44
    //   9622: aload_3
    //   9623: astore 47
    //   9625: aload_2
    //   9626: astore 46
    //   9628: aload 24
    //   9630: astore 41
    //   9632: goto -8862 -> 770
    //   9635: aload_3
    //   9636: ifnull +55 -> 9691
    //   9639: new 502	java/lang/StringBuilder
    //   9642: dup
    //   9643: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   9646: wide
    //   9650: aload_1
    //   9651: wide
    //   9655: wide
    //   9659: wide
    //   9663: invokevirtual 512	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   9666: ldc_w 1603
    //   9669: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9672: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   9675: wide
    //   9679: new 1025	java/lang/IllegalArgumentException
    //   9682: dup
    //   9683: wide
    //   9687: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   9690: athrow
    //   9691: aload_2
    //   9692: ifnonnull +55 -> 9747
    //   9695: new 502	java/lang/StringBuilder
    //   9698: dup
    //   9699: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   9702: wide
    //   9706: aload_1
    //   9707: wide
    //   9711: wide
    //   9715: wide
    //   9719: invokevirtual 512	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   9722: ldc_w 1605
    //   9725: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   9728: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   9731: wide
    //   9735: new 1025	java/lang/IllegalArgumentException
    //   9738: dup
    //   9739: wide
    //   9743: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   9746: athrow
    //   9747: aload_0
    //   9748: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   9751: invokestatic 1609	com/google/android/music/preferences/MusicPreferences:usingNewDownloadUI	(Landroid/content/Context;)Z
    //   9754: ifeq +52 -> 9806
    //   9757: aload_0
    //   9758: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   9761: wide
    //   9765: aload 12
    //   9767: wide
    //   9771: aload_2
    //   9772: wide
    //   9776: aload 6
    //   9778: wide
    //   9782: wide
    //   9786: wide
    //   9790: wide
    //   9794: wide
    //   9798: invokestatic 1615	com/google/android/music/store/DownloadQueueContentProviderHelper:getDownloadQueueContent	(Landroid/content/Context;Lcom/google/android/music/store/Store;[Ljava/lang/String;Lcom/google/android/music/utils/PostFroyoUtils$CancellationSignalComp;)Landroid/database/Cursor;
    //   9801: astore 55
    //   9803: goto -8830 -> 973
    //   9806: aload 12
    //   9808: wide
    //   9812: aload_2
    //   9813: wide
    //   9817: wide
    //   9821: aconst_null
    //   9822: wide
    //   9826: aconst_null
    //   9827: invokevirtual 1619	com/google/android/music/store/Store:getNeedToKeepOn	(Ljava/lang/String;[Ljava/lang/String;Ljava/util/Collection;)Landroid/database/Cursor;
    //   9830: astore 55
    //   9832: goto -8859 -> 973
    //   9835: aload_2
    //   9836: ifnonnull +7 -> 9843
    //   9839: getstatic 70	com/google/android/music/store/MusicContentProvider:DEFAULT_SEARCH_SUGGESTIONS_PROJECTION	[Ljava/lang/String;
    //   9842: astore_2
    //   9843: aconst_null
    //   9844: astore 26
    //   9846: aload_2
    //   9847: astore 43
    //   9849: aload 43
    //   9851: ifnonnull +14 -> 9865
    //   9854: new 1025	java/lang/IllegalArgumentException
    //   9857: dup
    //   9858: ldc_w 1621
    //   9861: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   9864: athrow
    //   9865: aload_1
    //   9866: invokevirtual 1199	android/net/Uri:getLastPathSegment	()Ljava/lang/String;
    //   9869: astore 29
    //   9871: aconst_null
    //   9872: astore 25
    //   9874: iconst_0
    //   9875: wide
    //   9879: aload_0
    //   9880: getfield 1623	com/google/android/music/store/MusicContentProvider:mNetworkMonitorConnection	Lcom/google/android/music/net/NetworkMonitorServiceConnection;
    //   9883: invokevirtual 1629	com/google/android/music/net/NetworkMonitorServiceConnection:getNetworkMonitor	()Lcom/google/android/music/net/INetworkMonitor;
    //   9886: wide
    //   9890: wide
    //   9894: ifnonnull +151 -> 10045
    //   9897: ldc_w 793
    //   9900: ldc_w 1631
    //   9903: invokestatic 1098	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   9906: wide
    //   9910: aload 26
    //   9912: ifnonnull +157 -> 10069
    //   9915: wide
    //   9919: ifeq +150 -> 10069
    //   9922: aload_0
    //   9923: invokespecial 1633	com/google/android/music/store/MusicContentProvider:isNautilusUser	()Z
    //   9926: wide
    //   9930: wide
    //   9934: ifeq +135 -> 10069
    //   9937: aconst_null
    //   9938: wide
    //   9942: wide
    //   9946: astore 25
    //   9948: aload_0
    //   9949: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   9952: wide
    //   9956: aload_0
    //   9957: getfield 1205	com/google/android/music/store/MusicContentProvider:mExecutor	Ljava/util/concurrent/ThreadPoolExecutor;
    //   9960: wide
    //   9964: aload 12
    //   9966: wide
    //   9970: aload 13
    //   9972: wide
    //   9976: aload_1
    //   9977: wide
    //   9981: aload 16
    //   9983: wide
    //   9987: aload 6
    //   9989: wide
    //   9993: new 1635	com/google/android/music/store/SearchHandler
    //   9996: dup
    //   9997: wide
    //   10001: wide
    //   10005: wide
    //   10009: wide
    //   10013: wide
    //   10017: aload 29
    //   10019: wide
    //   10023: aload 43
    //   10025: iconst_3
    //   10026: wide
    //   10030: aload 26
    //   10032: aload 25
    //   10034: invokespecial 1638	com/google/android/music/store/SearchHandler:<init>	(Landroid/content/Context;Lcom/google/android/music/store/Store;Ljava/util/concurrent/ThreadPoolExecutor;Landroid/database/sqlite/SQLiteQueryBuilder;Landroid/net/Uri;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;ILcom/google/android/music/utils/PostFroyoUtils$CancellationSignalComp;ZZ)V
    //   10037: invokevirtual 1641	com/google/android/music/store/SearchHandler:performQuery	()Landroid/database/Cursor;
    //   10040: astore 55
    //   10042: goto -9069 -> 973
    //   10045: wide
    //   10049: invokeinterface 1646 1 0
    //   10054: wide
    //   10058: wide
    //   10062: wide
    //   10066: goto -156 -> 9910
    //   10069: aconst_null
    //   10070: wide
    //   10074: goto -132 -> 9942
    //   10077: wide
    //   10081: wide
    //   10085: invokevirtual 1647	android/os/RemoteException:getMessage	()Ljava/lang/String;
    //   10088: wide
    //   10092: ldc_w 793
    //   10095: wide
    //   10099: wide
    //   10103: invokestatic 991	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   10106: wide
    //   10110: goto -162 -> 9948
    //   10113: aconst_null
    //   10114: astore 55
    //   10116: goto -9143 -> 973
    //   10119: aload_1
    //   10120: invokevirtual 1199	android/net/Uri:getLastPathSegment	()Ljava/lang/String;
    //   10123: invokestatic 1388	java/lang/Long:parseLong	(Ljava/lang/String;)J
    //   10126: wide
    //   10130: aload_0
    //   10131: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   10134: invokestatic 1134	com/google/android/music/store/Store:getInstance	(Landroid/content/Context;)Lcom/google/android/music/store/Store;
    //   10137: wide
    //   10141: iconst_1
    //   10142: invokevirtual 1651	com/google/android/music/store/Store:getMusicIdForSystemMediaStoreId	(JZ)J
    //   10145: wide
    //   10149: aload 13
    //   10151: ldc_w 1146
    //   10154: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   10157: getstatic 244	com/google/android/music/store/MusicContentProvider:sMusicProjectionMap	Ljava/util/HashMap;
    //   10160: astore 45
    //   10162: new 502	java/lang/StringBuilder
    //   10165: dup
    //   10166: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   10169: ldc_w 1392
    //   10172: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10175: wide
    //   10179: invokevirtual 1395	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   10182: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   10185: wide
    //   10189: aload 18
    //   10191: wide
    //   10195: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10198: wide
    //   10202: aload 17
    //   10204: astore 42
    //   10206: aload 16
    //   10208: astore 43
    //   10210: aload 29
    //   10212: astore 28
    //   10214: aload 4
    //   10216: astore 44
    //   10218: aload_3
    //   10219: astore 47
    //   10221: aload_2
    //   10222: astore 46
    //   10224: aload 24
    //   10226: astore 41
    //   10228: goto -9458 -> 770
    //   10231: wide
    //   10235: aload_2
    //   10236: wide
    //   10240: new 1653	android/database/MatrixCursor
    //   10243: dup
    //   10244: wide
    //   10248: iconst_0
    //   10249: invokespecial 1656	android/database/MatrixCursor:<init>	([Ljava/lang/String;I)V
    //   10252: astore 55
    //   10254: goto -9281 -> 973
    //   10257: aload_0
    //   10258: invokespecial 1658	com/google/android/music/store/MusicContentProvider:checkHasGetAccountsPermission	()V
    //   10261: iconst_2
    //   10262: anewarray 50	java/lang/String
    //   10265: wide
    //   10269: wide
    //   10273: iconst_0
    //   10274: ldc_w 276
    //   10277: aastore
    //   10278: wide
    //   10282: iconst_1
    //   10283: ldc_w 1660
    //   10286: aastore
    //   10287: aload_2
    //   10288: ifnull +33 -> 10321
    //   10291: aload_2
    //   10292: wide
    //   10296: wide
    //   10300: wide
    //   10304: invokestatic 1663	java/util/Arrays:equals	([Ljava/lang/Object;[Ljava/lang/Object;)Z
    //   10307: ifne +14 -> 10321
    //   10310: new 1025	java/lang/IllegalArgumentException
    //   10313: dup
    //   10314: ldc_w 1665
    //   10317: invokespecial 1026	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   10320: athrow
    //   10321: new 1653	android/database/MatrixCursor
    //   10324: dup
    //   10325: wide
    //   10329: iconst_1
    //   10330: invokespecial 1656	android/database/MatrixCursor:<init>	([Ljava/lang/String;I)V
    //   10333: astore 55
    //   10335: aload_0
    //   10336: invokespecial 1579	com/google/android/music/store/MusicContentProvider:getStreamingAccount	()Landroid/accounts/Account;
    //   10339: wide
    //   10343: wide
    //   10347: ifnull -9374 -> 973
    //   10350: iconst_2
    //   10351: anewarray 855	java/lang/Object
    //   10354: wide
    //   10358: wide
    //   10362: getfield 1669	android/accounts/Account:name	Ljava/lang/String;
    //   10365: wide
    //   10369: wide
    //   10373: iconst_0
    //   10374: wide
    //   10378: aastore
    //   10379: wide
    //   10383: getfield 1671	android/accounts/Account:type	Ljava/lang/String;
    //   10386: wide
    //   10390: wide
    //   10394: iconst_1
    //   10395: wide
    //   10399: aastore
    //   10400: aload 55
    //   10402: wide
    //   10406: invokevirtual 1675	android/database/MatrixCursor:addRow	([Ljava/lang/Object;)V
    //   10409: goto -9436 -> 973
    //   10412: iconst_1
    //   10413: anewarray 50	java/lang/String
    //   10416: wide
    //   10420: wide
    //   10424: iconst_0
    //   10425: ldc 184
    //   10427: aastore
    //   10428: new 1653	android/database/MatrixCursor
    //   10431: dup
    //   10432: wide
    //   10436: iconst_1
    //   10437: invokespecial 1656	android/database/MatrixCursor:<init>	([Ljava/lang/String;I)V
    //   10440: wide
    //   10444: iconst_1
    //   10445: anewarray 855	java/lang/Object
    //   10448: astore 46
    //   10450: aconst_null
    //   10451: astore 47
    //   10453: aload_0
    //   10454: invokespecial 1633	com/google/android/music/store/MusicContentProvider:isNautilusUser	()Z
    //   10457: ifeq +46 -> 10503
    //   10460: iconst_1
    //   10461: wide
    //   10465: wide
    //   10469: invokestatic 1678	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   10472: wide
    //   10476: aload 46
    //   10478: aload 47
    //   10480: wide
    //   10484: aastore
    //   10485: wide
    //   10489: aload 46
    //   10491: invokevirtual 1675	android/database/MatrixCursor:addRow	([Ljava/lang/Object;)V
    //   10494: wide
    //   10498: astore 55
    //   10500: goto -9527 -> 973
    //   10503: iconst_0
    //   10504: wide
    //   10508: goto -43 -> 10465
    //   10511: aload 13
    //   10513: ldc_w 1680
    //   10516: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   10519: getstatic 557	com/google/android/music/store/MusicContentProvider:sSuggestedSeedsMap	Ljava/util/HashMap;
    //   10522: astore 45
    //   10524: aload_0
    //   10525: wide
    //   10529: aload 18
    //   10531: wide
    //   10535: aload_1
    //   10536: wide
    //   10540: wide
    //   10544: wide
    //   10548: wide
    //   10552: iconst_0
    //   10553: invokespecial 1290	com/google/android/music/store/MusicContentProvider:appendMusicFilteringCondition	(Ljava/lang/StringBuilder;Landroid/net/Uri;Z)V
    //   10556: aload 28
    //   10558: ifnonnull +10 -> 10568
    //   10561: aload_2
    //   10562: invokestatic 1170	com/google/android/music/store/MusicContentProvider:hasCount	([Ljava/lang/String;)Z
    //   10565: ifeq +31 -> 10596
    //   10568: aload 17
    //   10570: astore 42
    //   10572: aload 16
    //   10574: astore 43
    //   10576: iconst_0
    //   10577: astore 28
    //   10579: aload 4
    //   10581: astore 44
    //   10583: aload_3
    //   10584: astore 47
    //   10586: aload_2
    //   10587: astore 46
    //   10589: aload 24
    //   10591: astore 41
    //   10593: goto -9823 -> 770
    //   10596: aload 17
    //   10598: astore 42
    //   10600: aload 16
    //   10602: astore 43
    //   10604: ldc_w 1558
    //   10607: astore 28
    //   10609: aload 4
    //   10611: astore 44
    //   10613: aload_3
    //   10614: astore 47
    //   10616: aload_2
    //   10617: astore 46
    //   10619: aload 24
    //   10621: astore 41
    //   10623: goto -9853 -> 770
    //   10626: aload 13
    //   10628: ldc_w 1680
    //   10631: invokevirtual 1149	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   10634: getstatic 557	com/google/android/music/store/MusicContentProvider:sSuggestedSeedsMap	Ljava/util/HashMap;
    //   10637: wide
    //   10641: aload_1
    //   10642: invokevirtual 1313	android/net/Uri:getPathSegments	()Ljava/util/List;
    //   10645: iconst_1
    //   10646: invokeinterface 1319 2 0
    //   10651: checkcast 50	java/lang/String
    //   10654: wide
    //   10658: new 502	java/lang/StringBuilder
    //   10661: dup
    //   10662: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   10665: ldc_w 1682
    //   10668: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10671: wide
    //   10675: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10678: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   10681: wide
    //   10685: aload 18
    //   10687: wide
    //   10691: invokestatic 751	com/google/android/music/store/MusicContentProvider:appendAndCondition	(Ljava/lang/StringBuilder;Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10694: wide
    //   10698: wide
    //   10702: astore 45
    //   10704: aload 17
    //   10706: astore 42
    //   10708: aload 16
    //   10710: astore 43
    //   10712: aload 29
    //   10714: astore 28
    //   10716: aload 4
    //   10718: astore 44
    //   10720: aload_3
    //   10721: astore 47
    //   10723: aload_2
    //   10724: astore 46
    //   10726: aload 24
    //   10728: astore 41
    //   10730: goto -9960 -> 770
    //   10733: aload_0
    //   10734: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   10737: wide
    //   10741: aload_1
    //   10742: wide
    //   10746: aload_2
    //   10747: wide
    //   10751: wide
    //   10755: wide
    //   10759: aload 21
    //   10761: wide
    //   10765: invokestatic 1685	com/google/android/music/store/ExploreContentProviderHelper:query	(Landroid/content/Context;Landroid/net/Uri;I[Ljava/lang/String;)Landroid/database/Cursor;
    //   10768: astore 55
    //   10770: goto -9797 -> 973
    //   10773: getstatic 48	com/google/android/music/store/MusicContentProvider:LOGV	Z
    //   10776: ifeq +123 -> 10899
    //   10779: aload 46
    //   10781: invokestatic 1122	com/google/android/music/utils/DebugUtils:arrayToString	([Ljava/lang/String;)Ljava/lang/String;
    //   10784: wide
    //   10788: aload 44
    //   10790: invokestatic 1122	com/google/android/music/utils/DebugUtils:arrayToString	([Ljava/lang/String;)Ljava/lang/String;
    //   10793: wide
    //   10797: new 502	java/lang/StringBuilder
    //   10800: dup
    //   10801: invokespecial 503	java/lang/StringBuilder:<init>	()V
    //   10804: ldc_w 1687
    //   10807: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10810: wide
    //   10814: aload_1
    //   10815: wide
    //   10819: wide
    //   10823: wide
    //   10827: invokevirtual 512	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   10830: ldc_w 1689
    //   10833: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10836: wide
    //   10840: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10843: ldc_w 1691
    //   10846: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10849: aload 47
    //   10851: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10854: ldc_w 1693
    //   10857: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10860: wide
    //   10864: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10867: ldc_w 1695
    //   10870: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10873: aload 28
    //   10875: invokevirtual 509	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   10878: invokevirtual 518	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   10881: wide
    //   10885: ldc_w 793
    //   10888: wide
    //   10892: invokestatic 1131	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   10895: wide
    //   10899: aload 13
    //   10901: iload 14
    //   10903: invokevirtual 1699	android/database/sqlite/SQLiteQueryBuilder:setDistinct	(Z)V
    //   10906: aload 12
    //   10908: invokevirtual 1258	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   10911: wide
    //   10915: aconst_null
    //   10916: astore 29
    //   10918: aload 13
    //   10920: wide
    //   10924: aload 6
    //   10926: astore 154
    //   10928: wide
    //   10932: wide
    //   10936: aload 46
    //   10938: aload 47
    //   10940: aload 44
    //   10942: aload 42
    //   10944: aload 29
    //   10946: aload 28
    //   10948: aload 43
    //   10950: aload 154
    //   10952: invokestatic 1467	com/google/android/music/utils/PostFroyoUtils$SQLiteDatabaseComp:query	(Landroid/database/sqlite/SQLiteQueryBuilder;Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/google/android/music/utils/PostFroyoUtils$CancellationSignalComp;)Landroid/database/Cursor;
    //   10955: wide
    //   10959: aload 25
    //   10961: ifnull +23 -> 10984
    //   10964: aload 41
    //   10966: ifnull +83 -> 11049
    //   10969: aload 25
    //   10971: wide
    //   10975: aload 41
    //   10977: invokestatic 1703	com/google/android/music/store/NautilusContentProviderHelper:merge	(Landroid/database/Cursor;Landroid/database/Cursor;Ljava/lang/String;)Landroid/database/Cursor;
    //   10980: wide
    //   10984: wide
    //   10988: ifnull +49 -> 11037
    //   10991: aload_0
    //   10992: invokevirtual 783	com/google/android/music/store/MusicContentProvider:getContext	()Landroid/content/Context;
    //   10995: invokevirtual 1268	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   10998: wide
    //   11002: aload_1
    //   11003: wide
    //   11007: wide
    //   11011: wide
    //   11015: wide
    //   11019: invokeinterface 1272 3 0
    //   11024: wide
    //   11028: invokeinterface 1275 1 0
    //   11033: wide
    //   11037: aload 12
    //   11039: wide
    //   11043: invokevirtual 1279	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   11046: goto -10073 -> 973
    //   11049: aconst_null
    //   11050: astore 46
    //   11052: aload 46
    //   11054: anewarray 943	android/database/Cursor
    //   11057: wide
    //   11061: wide
    //   11065: iconst_0
    //   11066: aload 25
    //   11068: aastore
    //   11069: wide
    //   11073: iconst_1
    //   11074: wide
    //   11078: aastore
    //   11079: wide
    //   11083: invokestatic 1706	com/google/android/music/store/NautilusContentProviderHelper:merge	([Landroid/database/Cursor;)Landroid/database/Cursor;
    //   11086: wide
    //   11090: wide
    //   11094: wide
    //   11098: goto -114 -> 10984
    //   11101: wide
    //   11105: aload 12
    //   11107: wide
    //   11111: invokevirtual 1279	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   11114: wide
    //   11118: athrow
    //   11119: wide
    //   11123: wide
    //   11127: goto -4855 -> 6272
    //   11130: aload 26
    //   11132: astore 35
    //   11134: aload 25
    //   11136: wide
    //   11140: goto -5922 -> 5218
    //   11143: aload 240
    //   11145: astore 35
    //   11147: goto -6842 -> 4305
    //   11150: aload 29
    //   11152: astore 35
    //   11154: goto -6809 -> 4345
    //   11157: aload 211
    //   11159: astore 45
    //   11161: aload 17
    //   11163: astore 42
    //   11165: aload 16
    //   11167: astore 43
    //   11169: aload 35
    //   11171: astore 28
    //   11173: aload 4
    //   11175: astore 44
    //   11177: aload_3
    //   11178: astore 47
    //   11180: aload 24
    //   11182: astore 41
    //   11184: aload_2
    //   11185: astore 46
    //   11187: goto -10417 -> 770
    //   11190: aload 223
    //   11192: astore 35
    //   11194: goto -7428 -> 3766
    //   11197: aload 23
    //   11199: astore 26
    //   11201: aload_2
    //   11202: astore 43
    //   11204: goto -1355 -> 9849
    //   11207: aload 5
    //   11209: astore 29
    //   11211: goto -11019 -> 192
    //
    // Exception table:
    //   from	to	target	type
    //   1798	1852	1868	finally
    //   2706	2804	2825	finally
    //   2814	2822	2825	finally
    //   2837	2932	2825	finally
    //   6106	6532	6582	finally
    //   9879	9930	10077	android/os/RemoteException
    //   10045	10058	10077	android/os/RemoteException
    //   10130	10202	10231	java/io/FileNotFoundException
    //   10928	11037	11101	finally
    //   11052	11090	11101	finally
  }

  private int updateAudio(ContentValues paramContentValues, long paramLong)
  {
    if (paramContentValues.size() != 1)
      throw new IllegalArgumentException("Only rating can be update");
    Integer localInteger = paramContentValues.getAsInteger("Rating");
    if (localInteger == null)
      throw new IllegalArgumentException("Missing value for rating");
    Store localStore = Store.getInstance(getContext());
    int i = localInteger.intValue();
    int j = localStore.updateRating(paramLong, i);
    if (j > 0)
    {
      ContentResolver localContentResolver = getContext().getContentResolver();
      Uri localUri = MusicContent.AutoPlaylists.getAutoPlaylistUri(65532L);
      localContentResolver.notifyChange(localUri, null, false);
    }
    return j;
  }

  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    int i = 1;
    checkWritePermission();
    Context localContext = getContext();
    Store localStore = Store.getInstance(localContext);
    boolean bool = MusicUtils.areUpstreamTrackDeletesEnabled(localContext);
    int k = sUriMatcher.match(paramUri);
    ContentResolver localContentResolver = localContext.getContentResolver();
    long l1;
    int j;
    int m;
    int n;
    switch (k)
    {
    default:
      StringBuilder localStringBuilder = new StringBuilder().append("Delete not supported on URI: ");
      String str1 = paramUri.toString();
      String str2 = str1;
      throw new UnsupportedOperationException(str2);
    case 301:
      l1 = Long.parseLong(paramUri.getLastPathSegment());
      if (bool)
      {
        j = localStore.deleteAllDefaultDomainMatchingMusicFiles(l1);
        if (j > 0)
        {
          m = 1;
          i = m;
          n = j;
          label186: localContentResolver.notifyChange(paramUri, null, i);
          if ((k != 301) && (k != 401))
            break label486;
          Uri localUri1 = MusicContent.CONTENT_URI;
          localContentResolver.notifyChange(localUri1, null, false);
        }
      }
      break;
    case 601:
    case 1701:
    case 603:
    case 1500:
    case 401:
    }
    while (true)
    {
      return n;
      m = 0;
      break;
      n = localStore.deleteAllMatchingLocalMusicFiles(l1);
      i = 0;
      break label186;
      long l2 = Long.parseLong(paramUri.getLastPathSegment());
      if (localStore.deletePlaylist(localContext, l2))
      {
        n = 1;
        break label186;
      }
      n = 0;
      break label186;
      long l3 = Long.parseLong(paramUri.getLastPathSegment());
      if (localStore.deleteRadioStation(localContext, l3))
      {
        n = 1;
        break label186;
      }
      n = 0;
      break label186;
      l1 = Long.parseLong((String)paramUri.getPathSegments().get(1));
      if (!isPlayQueue(localStore, l1))
      {
        j = 1;
        label346: long l4 = Long.parseLong((String)paramUri.getPathSegments().get(3));
        if (!localStore.deletePlaylistItem(l1, l4))
          break label393;
      }
      while (true)
      {
        n = i;
        i = j;
        break;
        j = 0;
        break label346;
        label393: i = 0;
      }
      if (localStore.deleteRemoteMusicAndPlaylists(localContext, false));
      while (true)
      {
        n = i;
        i = 0;
        break;
        i = 0;
      }
      l1 = Long.parseLong(paramUri.getLastPathSegment());
      String str3 = paramUri.getQueryParameter("deleteMode");
      if ("NAUTILUS".equals(str3))
      {
        n = localStore.deletePersistentNautilusTracksFromAlbum(l1);
        if (n > 0)
          break label186;
        i = 0;
        break label186;
      }
      throw new UnsupportedOperationException("Full deletion not supported on Albums.");
      label486: Uri localUri2 = MusicContent.Recent.CONTENT_URI;
      localContentResolver.notifyChange(localUri2, null, false);
      Uri localUri3 = MusicContent.Mainstage.CONTENT_URI;
      localContentResolver.notifyChange(localUri3, null, false);
    }
  }

  public String getType(Uri paramUri)
  {
    String str3;
    switch (sUriMatcher.match(paramUri))
    {
    default:
      String str1 = "Unknown URI " + paramUri;
      IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException(str1);
      String str2 = "getType called on Unknonw Uri: " + paramUri;
      int i = Log.e("MusicContentProvider", str2, localIllegalArgumentException);
      throw localIllegalArgumentException;
    case 300:
    case 301:
    case 302:
    case 303:
    case 304:
    case 305:
    case 306:
    case 503:
    case 701:
    case 1200:
      str3 = "vnd.android.cursor.item/vnd.google.xaudio";
    case 400:
    case 402:
    case 403:
    case 502:
    case 702:
    case 401:
    case 500:
    case 504:
    case 501:
    case 700:
    case 703:
    case 704:
    case 800:
    case 803:
    case 804:
    case 805:
    case 806:
    case 900:
    case 950:
    case 951:
    case 1000:
    case 600:
    case 604:
    case 605:
    case 606:
    case 601:
    case 602:
    case 603:
    case 620:
    case 621:
    case 622:
    case 1300:
    case 1301:
    case 1400:
    case 1401:
    case 1500:
    case 1700:
    case 1701:
    case 1800:
    case 1900:
    }
    while (true)
    {
      return str3;
      str3 = "vnd.android.cursor.dir/vnd.google.music.album";
      continue;
      str3 = "vnd.android.cursor.item/vnd.google.music.album";
      continue;
      str3 = "vnd.android.cursor.dir/vnd.google.music.artist";
      continue;
      str3 = "vnd.android.cursor.item/vnd.google.music.artist";
      continue;
      str3 = "vnd.android.cursor.dir/vnd.google.music.genre";
      continue;
      str3 = "vnd.android.cursor.item/vnd.google.music.genre";
      continue;
      str3 = "vnd.android.cursor.item/vnd.google.music.albumart";
      continue;
      str3 = "vnd.android.cursor.item/vnd.google.music.cachedart";
      continue;
      str3 = "vnd.android.cursor.dir/vnd.google.music.recent";
      continue;
      str3 = "vnd.android.cursor.dir/vnd.google.music.keepon";
      continue;
      str3 = "vnd.android.cursor.item/vnd.google.music.keepon";
      continue;
      str3 = "vnd.android.cursor.dir/vnd.google.music.downloadqueue";
      continue;
      str3 = "vnd.android.cursor.dir/vnd.google.music.playlist";
      continue;
      str3 = "vnd.android.cursor.item/vnd.google.music.playlist";
      continue;
      str3 = "vnd.android.cursor.dir/vnd.google.listitems";
      continue;
      str3 = "vnd.android.cursor.item/vnd.google.listitems";
      continue;
      str3 = "vnd.android.cursor.dir/vnd.google.music.autoplaylist";
      continue;
      str3 = "vnd.android.cursor.item/vnd.google.music.autoplaylist";
      continue;
      str3 = "vnd.android.cursor.dir/vnd.google.autolistitems";
      continue;
      str3 = "vnd.android.cursor.item/vnd.google.music.account";
      continue;
      str3 = "vnd.android.cursor.item/vnd.google.music.config";
      continue;
      str3 = "vnd.android.cursor.dir/vnd.google.music.seeds";
      continue;
      str3 = "vnd.android.cursor.item/vnd.google.music.seeds";
      continue;
      str3 = "vnd.android.cursor.dir/vnd.google.xaudio";
      continue;
      str3 = "vnd.android.cursor.dir/vnd.google.music.radio_stations";
      continue;
      str3 = "vnd.android.cursor.item/vnd.google.music.radio_stations";
      continue;
      str3 = "vnd.android.cursor.item/vnd.google.music.mainstage";
      continue;
      str3 = "vnd.android.cursor.dir/vnd.google.music.shared_with_me_playlist";
    }
  }

  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    int i = 0;
    checkWritePermission();
    Context localContext = getContext();
    Store localStore = Store.getInstance(localContext);
    int j = sUriMatcher.match(paramUri);
    String str3;
    long l1;
    Object localObject;
    switch (j)
    {
    default:
      StringBuilder localStringBuilder = new StringBuilder().append("Insert not supported on URI: ");
      String str1 = paramUri.toString();
      String str2 = str1 + " (match=" + j + ")";
      throw new UnsupportedOperationException(str2);
    case 600:
      str3 = paramContentValues.getAsString("playlist_name");
      if (paramContentValues.containsKey("playlist_type"))
        i = paramContentValues.getAsInteger("playlist_type").intValue();
      if (i == 71)
      {
        String str4 = paramContentValues.getAsString("playlist_share_token");
        String str5 = paramContentValues.getAsString("playlist_description");
        String str6 = paramContentValues.getAsString("playlist_owner_name");
        String str7 = paramContentValues.getAsString("playlist_art_url");
        String str8 = paramContentValues.getAsString("playlist_owner_profile_photo_url");
        Account localAccount = getStreamingAccount();
        l1 = SharedContentProviderHelper.createFollowedSharedPlaylist(localContext, localAccount, localStore, str3, str5, str6, str4, str7, str8);
        if (l1 == 0L)
          break label694;
        localObject = MusicContent.Playlists.getPlaylistUri(l1);
      }
      break;
    case 602:
    case 301:
    case 306:
    case 302:
    case 303:
    case 1900:
    }
    while (true)
    {
      if (localObject != null)
        localContext.getContentResolver().notifyChange((Uri)localObject, null, true);
      return localObject;
      l1 = localStore.createPlaylist(str3, i);
      break;
      Long localLong = paramContentValues.getAsLong("audio_id");
      if ((localLong == null) || (localLong.longValue() == 0L) || (paramContentValues.size() != 1))
        throw new IllegalArgumentException("When inserting playlist items, the music id must be provided and nothing else");
      long l2 = Long.parseLong((String)paramUri.getPathSegments().get(1));
      try
      {
        long l3 = localLong.longValue();
        l1 = localStore.appendPlaylistItem(l2, l3);
        if (l1 == 0L)
          break label694;
        Uri localUri = MusicContent.Playlists.Members.getPlaylistItemUri(l2, l1);
        localObject = localUri;
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        int k = Log.e("MusicContentProvider", "Failed to insert playlist item", localFileNotFoundException);
        localObject = null;
      }
      continue;
      String str10 = paramUri.getLastPathSegment();
      if (!MusicUtils.isNautilusId(str10))
      {
        String str11 = "Cannot insert a local item: " + str10;
        throw new IllegalArgumentException(str11);
      }
      localObject = NautilusContentProviderHelper.insert(localContext, localStore, j, paramUri, str10);
      continue;
      String str9 = paramUri.getLastPathSegment();
      int m = str9.indexOf(",");
      if (m > -1);
      for (boolean bool = MusicUtils.isNautilusId(str9.substring(0, m)); !bool; bool = MusicUtils.isNautilusId(str9))
      {
        String str12 = "Cannot insert local items: " + str9;
        throw new IllegalArgumentException(str12);
      }
      localObject = NautilusContentProviderHelper.insert(localContext, localStore, j, paramUri, str9);
      continue;
      String str13 = (String)paramUri.getPathSegments().get(1);
      if (!MusicUtils.isNautilusId(str13))
      {
        String str14 = "Cannot insert a local item: " + str13;
        throw new IllegalArgumentException(str14);
      }
      localObject = NautilusContentProviderHelper.insert(localContext, localStore, j, paramUri, str13);
      continue;
      localObject = SharedContentProviderHelper.insert(localContext, localStore, j, paramUri);
      continue;
      label694: localObject = null;
    }
  }

  public boolean onCreate()
  {
    Store localStore = Store.getInstance(getContext());
    NetworkMonitorServiceConnection localNetworkMonitorServiceConnection1 = new NetworkMonitorServiceConnection();
    this.mNetworkMonitorConnection = localNetworkMonitorServiceConnection1;
    NetworkMonitorServiceConnection localNetworkMonitorServiceConnection2 = this.mNetworkMonitorConnection;
    Context localContext = getContext();
    localNetworkMonitorServiceConnection2.bindToService(localContext);
    TimeUnit localTimeUnit = TimeUnit.SECONDS;
    LinkedBlockingQueue localLinkedBlockingQueue = new LinkedBlockingQueue();
    ThreadPoolExecutor localThreadPoolExecutor = new ThreadPoolExecutor(1, 2, 3L, localTimeUnit, localLinkedBlockingQueue);
    this.mExecutor = localThreadPoolExecutor;
    return true;
  }

  public ParcelFileDescriptor openFile(Uri paramUri, String paramString)
    throws FileNotFoundException
  {
    Object localObject1 = null;
    Context localContext1 = getContext();
    long l1 = Store.getInstance(localContext1);
    UriMatcher localUriMatcher = sUriMatcher;
    Uri localUri1 = paramUri;
    int i = localUriMatcher.match(localUri1);
    int j = parseIntegerParameter(paramUri, "w", -1);
    int k = parseIntegerParameter(paramUri, "h", -1);
    long l2;
    int m;
    Object localObject2;
    Object localObject3;
    if ((i == 800) || (i == 803) || (i == 805))
    {
      l2 = Long.parseLong(paramUri.getLastPathSegment());
      if (l2 <= 0L)
      {
        if (LOGV)
          m = Log.w("MusicContentProvider", "Unknown album art requested");
        if (localObject1 != null)
          break label964;
        throw new FileNotFoundException();
      }
      localObject2 = null;
      if (getStreamingAccount() != null)
      {
        localObject3 = null;
        label138: if (localObject3 == null)
          break label1033;
      }
    }
    label470: label870: for (Object localObject4 = l1.getArtwork(l2); ; localObject4 = localObject2)
    {
      CacheUtils.AlbumArtWorkCachePathResolver localAlbumArtWorkCachePathResolver = new CacheUtils.AlbumArtWorkCachePathResolver(localContext1);
      Object localObject5;
      Object localObject6;
      Uri localUri2;
      if (localObject4 == null)
      {
        localObject5 = null;
        if ((localObject5 == null) || (!((File)localObject5).exists()) || (((File)localObject5).length() == 0L))
        {
          localObject6 = l1.getArtLocationForAlbum(l2);
          if (localObject6 != null)
          {
            if (!((String)localObject6).startsWith("mediastore:"))
              break label375;
            int n = "mediastore:".length();
            localUri2 = MusicContent.AlbumArt.getMediaStoreAlbumArt(Long.parseLong(((String)localObject6).substring(n)));
            if (localUri2 == null)
              break label1027;
          }
        }
      }
      label751: 
      while (true)
      {
        try
        {
          ParcelFileDescriptor localParcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(localUri2, "r");
          localObject5 = localParcelFileDescriptor;
          localObject1 = localObject5;
          if (localObject1 != null)
            break;
          if (localObject4 == null)
            break label470;
          String str1 = (String)((Pair)localObject4).first;
          int i1 = ((Integer)((Pair)localObject4).second).intValue();
          localObject1 = ParcelFileDescriptor.open(localAlbumArtWorkCachePathResolver.resolveArtworkPath(str1, i1), 268435456);
          break;
          m = 0;
          break label138;
          String str2 = (String)((Pair)localObject4).first;
          int i2 = ((Integer)((Pair)localObject4).second).intValue();
          localObject5 = localAlbumArtWorkCachePathResolver.resolveArtworkPath(str2, i2);
        }
        catch (Exception localException)
        {
          localObject5 = null;
          continue;
        }
        label375: if (localObject3 != null)
        {
          if (LOGV)
          {
            String str3 = "Attemping to download album art: " + l2;
            int i3 = Log.i("MusicContentProvider", str3);
          }
          localObject4 = null;
          Intent localIntent1 = new Intent(localContext1, ArtDownloadService.class);
          Intent localIntent2 = localIntent1.setAction("com.android.music.REMOTE_ART_REQUESTED");
          Intent localIntent3 = localIntent1.putExtra("albumId", l2);
          ComponentName localComponentName1 = localContext1.startService(localIntent1);
          continue;
          if (i != 803)
            break;
          localObject1 = getFauxAlbumArt(l2, j, k);
          break;
          if (i == 804)
          {
            long l3 = Long.parseLong(paramUri.getLastPathSegment());
            localObject1 = getFauxAlbumArt(l3, j, k);
            break;
          }
          if (i == 802)
          {
            l1 = Long.parseLong(paramUri.getLastPathSegment());
            String[] arrayOfString = new String[2];
            arrayOfString[0] = "_id";
            arrayOfString[1] = "playlist_name";
            Context localContext2 = getContext();
            Uri localUri3 = MusicContent.Playlists.getPlaylistUri(l1);
            Cursor localCursor = MusicUtils.query(localContext2, localUri3, arrayOfString, null, null, null);
            while (true)
            {
              MusicContentProvider localMusicContentProvider;
              try
              {
                boolean bool = localCursor.moveToFirst();
                String str4 = localCursor.getString(1);
                localObject3 = str4;
                Store.safeClose(localCursor);
                if (Build.VERSION.SDK_INT >= 9)
                {
                  Context localContext3 = getContext();
                  long l4 = l1;
                  localObject6 = AlbumArtUtils.getStaticFauxArtPipe(localContext3, 1, l4, (String)localObject3, null, j, k);
                  localObject1 = localObject6;
                }
              }
              finally
              {
                Store.safeClose(localCursor);
              }
              long l5 = l1;
              localObject6 = localMusicContentProvider.getStaticFauxArt(1, l5, (String)localObject3, null, j, k);
            }
          }
          if (i == 801)
          {
            long l6 = Long.parseLong(paramUri.getLastPathSegment());
            localObject1 = getStaticFauxArt(5, l6, j, k);
            break;
          }
          if (i == 806)
          {
            localObject4 = paramUri.getQueryParameter("url");
            int i4;
            Pair localPair;
            if (getStreamingAccount() != null)
            {
              i4 = 1;
              if (i4 == 0)
                break label827;
              localPair = l1.getArtwork((String)localObject4);
              label765: localObject3 = new CacheUtils.NonAlbumArtWorkCachePathResolver(localContext1);
              if (localPair != null)
                break label833;
            }
            String str5;
            int i5;
            for (localObject6 = null; ; localObject6 = ((CacheUtils.ArtworkPathResolver)localObject3).resolveArtworkPath(str5, i5))
            {
              if ((localObject6 == null) || (!((File)localObject6).exists()) || (((File)localObject6).length() <= 0L))
                break label870;
              localObject1 = ParcelFileDescriptor.open((File)localObject6, 268435456);
              break;
              i4 = 0;
              break label751;
              localPair = null;
              break label765;
              str5 = (String)localPair.first;
              i5 = ((Integer)localPair.second).intValue();
            }
            Intent localIntent4 = new Intent(localContext1, ArtDownloadService.class);
            Intent localIntent5 = localIntent4.setAction("com.android.music.REMOTE_ART_REQUESTED");
            Intent localIntent6 = localIntent4.putExtra("remoteUrl", (String)localObject4);
            ComponentName localComponentName2 = localContext1.startService(localIntent4);
            break;
          }
          StringBuilder localStringBuilder1 = new StringBuilder().append("Unrecognized openFile request: ");
          String str6 = paramUri.toString();
          String str7 = str6;
          int i6 = Log.e("MusicContentProvider", str7);
          break;
          if (!((ParcelFileDescriptor)localObject1).getFileDescriptor().valid())
          {
            StringBuilder localStringBuilder2 = new StringBuilder().append("Invalid file descriptor for ");
            String str8 = paramUri.toString();
            String str9 = str8;
            int i7 = Log.e("MusicContentProvider", str9);
            throw new FileNotFoundException();
          }
          return localObject1;
          localObject5 = null;
        }
      }
    }
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    PostFroyoUtils.CancellationSignalComp localCancellationSignalComp = new PostFroyoUtils.CancellationSignalComp();
    MusicContentProvider localMusicContentProvider = this;
    Uri localUri = paramUri;
    String[] arrayOfString1 = paramArrayOfString1;
    String str1 = paramString1;
    String[] arrayOfString2 = paramArrayOfString2;
    String str2 = paramString2;
    return localMusicContentProvider.query(localUri, arrayOfString1, str1, arrayOfString2, str2, localCancellationSignalComp);
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, CancellationSignal paramCancellationSignal)
  {
    PostFroyoUtils.CancellationSignalComp localCancellationSignalComp = new PostFroyoUtils.CancellationSignalComp(paramCancellationSignal);
    MusicContentProvider localMusicContentProvider = this;
    Uri localUri = paramUri;
    String[] arrayOfString1 = paramArrayOfString1;
    String str1 = paramString1;
    String[] arrayOfString2 = paramArrayOfString2;
    String str2 = paramString2;
    return localMusicContentProvider.query(localUri, arrayOfString1, str1, arrayOfString2, str2, localCancellationSignalComp);
  }

  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    int i = 0;
    boolean bool = true;
    Context localContext = getContext();
    int j = Store.getInstance(localContext);
    int m = sUriMatcher.match(paramUri);
    if (m != 1300)
      checkWritePermission();
    int k;
    switch (m)
    {
    default:
      StringBuilder localStringBuilder = new StringBuilder().append("Update not supported on URI: ");
      String str1 = paramUri.toString();
      String str2 = str1;
      throw new UnsupportedOperationException(str2);
    case 301:
      long l2 = Long.parseLong(paramUri.getLastPathSegment());
      i = updateAudio(paramContentValues, l2);
    case 601:
      while (true)
      {
        localContext.getContentResolver().notifyChange(paramUri, null, bool);
        return i;
        long l1 = Long.parseLong(paramUri.getLastPathSegment());
        String str3 = paramUri.getQueryParameter("action");
        if ((str3 == null) || (str3.length() == 0))
        {
          String str4 = paramContentValues.getAsString("playlist_name");
          i = j.renamePlaylist(localContext, l1, str4);
        }
        else if ("album".equals(str3))
        {
          long l3 = paramContentValues.getAsLong("album_id").longValue();
          i = j.appendAlbumToPlaylist(l1, l3);
        }
        else if ("artist".equals(str3))
        {
          long l4 = paramContentValues.getAsLong("AlbumArtistId").longValue();
          i = j.appendArtistToPlaylist(l1, l4);
        }
        else
        {
          if ("genre".equals(str3))
          {
            long l5 = paramContentValues.getAsLong("GenreId").longValue();
            long l6;
            if (paramContentValues.containsKey("album_id"))
              l6 = paramContentValues.getAsLong("album_id").longValue();
            for (k = j.appendGenreToPlaylist(l1, l5, l6); ; k = k.appendGenreToPlaylist(l1, l5))
            {
              i = k;
              break;
            }
          }
          if ("playlist".equals(str3))
          {
            long l7 = paramContentValues.getAsLong("playlist_id").longValue();
            i = k.appendPlaylistToPlaylist(l1, l7);
          }
        }
      }
    case 603:
      int n = Long.parseLong((String)paramUri.getPathSegments().get(1));
      if (!isPlayQueue(k, n));
      while (true)
      {
        long l8 = Long.parseLong((String)paramUri.getPathSegments().get(3));
        long l9 = Long.parseLong(paramUri.getQueryParameter("moveBefore"));
        k.movePlaylistItem(n, l8, l9);
        break;
        bool = false;
      }
    case 1300:
    }
    throw new UnsupportedOperationException();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.MusicContentProvider
 * JD-Core Version:    0.6.2
 */