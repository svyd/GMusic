package com.google.android.music.xdi;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Binder;
import android.os.Handler;
import com.google.android.music.log.Log;
import com.google.android.music.store.MusicContent;
import com.google.android.music.tutorial.SignupStatus;
import java.util.Arrays;
import java.util.List;

public class XdiContentProvider extends ContentProvider
{
  static final Uri BASE_URI = Uri.parse("content://com.google.android.music.xdi");
  static final String[] DEFAULT_BROWSE_HEADERS_PROJECTION;
  static final String[] DEFAULT_BROWSE_HEADER_ID_PROJECTION;
  static final String[] DEFAULT_DETAIL_ACTIONS_PROJECTION;
  static final String[] DEFAULT_DETAIL_BLURB_PROJECTION;
  static final String[] DEFAULT_DETAIL_CHILDREN_PROJECTION;
  static final String[] DEFAULT_DETAIL_ITEM_PROJECTION;
  static final String[] DEFAULT_DETAIL_SECTIONS_PROJECTION;
  static final String[] DEFAULT_LAUNCHER_CLUSTER_PROJECTION;
  static final String[] DEFAULT_LAUNCHER_ROOT_PROJECTION;
  static final String[] DEFAULT_SEARCH_RESULTS_PROJECTION;
  static final String[] META_PROJECTION;
  private static final UriMatcher sUriMatcher;
  private ContentObserver mMusicContentObserver;

  static
  {
    String[] arrayOfString1 = new String[10];
    arrayOfString1[0] = "_id";
    arrayOfString1[1] = "name";
    arrayOfString1[2] = "importance";
    arrayOfString1[3] = "display_name";
    arrayOfString1[4] = "visible_count";
    arrayOfString1[5] = "cache_time_ms";
    arrayOfString1[6] = "image_crop_allowed";
    arrayOfString1[7] = "browse_items_uri";
    arrayOfString1[8] = "intent_uri";
    arrayOfString1[9] = "notification_text";
    DEFAULT_LAUNCHER_ROOT_PROJECTION = arrayOfString1;
    String[] arrayOfString2 = new String[3];
    arrayOfString2[0] = "_id";
    arrayOfString2[1] = "parent_id";
    arrayOfString2[2] = "image_uri";
    DEFAULT_LAUNCHER_CLUSTER_PROJECTION = arrayOfString2;
    String[] arrayOfString3 = new String[11];
    arrayOfString3[0] = "_id";
    arrayOfString3[1] = "name";
    arrayOfString3[2] = "display_name";
    arrayOfString3[3] = "background_image_uri";
    arrayOfString3[4] = "bg_image_uri";
    arrayOfString3[5] = "icon_uri";
    arrayOfString3[6] = "default_item_width";
    arrayOfString3[7] = "default_item_height";
    arrayOfString3[8] = "color_hint";
    arrayOfString3[9] = "badge_uri";
    arrayOfString3[10] = "items_uri";
    DEFAULT_BROWSE_HEADERS_PROJECTION = arrayOfString3;
    String[] arrayOfString4 = new String[8];
    arrayOfString4[0] = "_id";
    arrayOfString4[1] = "parent_id";
    arrayOfString4[2] = "display_name";
    arrayOfString4[3] = "display_description";
    arrayOfString4[4] = "image_uri";
    arrayOfString4[5] = "width";
    arrayOfString4[6] = "height";
    arrayOfString4[7] = "intent_uri";
    DEFAULT_BROWSE_HEADER_ID_PROJECTION = arrayOfString4;
    String[] arrayOfString5 = new String[7];
    arrayOfString5[0] = "_id";
    arrayOfString5[1] = "display_name";
    arrayOfString5[2] = "foreground_image_uri";
    arrayOfString5[3] = "background_image_uri";
    arrayOfString5[4] = "badge_uri";
    arrayOfString5[5] = "color_hint";
    arrayOfString5[6] = "text_color_hint";
    DEFAULT_DETAIL_ITEM_PROJECTION = arrayOfString5;
    String[] arrayOfString6 = new String[11];
    arrayOfString6[0] = "_id";
    arrayOfString6[1] = "name";
    arrayOfString6[2] = "display_header";
    arrayOfString6[3] = "display_name";
    arrayOfString6[4] = "section_type";
    arrayOfString6[5] = "blob_content";
    arrayOfString6[6] = "content_uri";
    arrayOfString6[7] = "action_uri";
    arrayOfString6[8] = "user_rating_custom";
    arrayOfString6[9] = "user_rating";
    arrayOfString6[10] = "user_rating_count";
    DEFAULT_DETAIL_SECTIONS_PROJECTION = arrayOfString6;
    String[] arrayOfString7 = new String[9];
    arrayOfString7[0] = "_id";
    arrayOfString7[1] = "display_name";
    arrayOfString7[2] = "display_subname";
    arrayOfString7[3] = "display_description";
    arrayOfString7[4] = "display_category";
    arrayOfString7[5] = "user_rating_count";
    arrayOfString7[6] = "user_rating";
    arrayOfString7[7] = "user_rating_custom";
    arrayOfString7[8] = "badge_uri";
    DEFAULT_DETAIL_BLURB_PROJECTION = arrayOfString7;
    String[] arrayOfString8 = new String[10];
    arrayOfString8[0] = "_id";
    arrayOfString8[1] = "display_name";
    arrayOfString8[2] = "display_subname";
    arrayOfString8[3] = "display_description";
    arrayOfString8[4] = "display_number";
    arrayOfString8[5] = "image_uri";
    arrayOfString8[6] = "item_display_type";
    arrayOfString8[7] = "user_rating_count";
    arrayOfString8[8] = "user_rating";
    arrayOfString8[9] = "action_uri";
    DEFAULT_DETAIL_CHILDREN_PROJECTION = arrayOfString8;
    String[] arrayOfString9 = new String[4];
    arrayOfString9[0] = "_id";
    arrayOfString9[1] = "display_name";
    arrayOfString9[2] = "display_subname";
    arrayOfString9[3] = "intent_uri";
    DEFAULT_DETAIL_ACTIONS_PROJECTION = arrayOfString9;
    String[] arrayOfString10 = new String[5];
    arrayOfString10[0] = "_id";
    arrayOfString10[1] = "display_name";
    arrayOfString10[2] = "results_uri";
    arrayOfString10[3] = "default_item_width";
    arrayOfString10[4] = "default_item_height";
    DEFAULT_SEARCH_RESULTS_PROJECTION = arrayOfString10;
    String[] arrayOfString11 = new String[4];
    arrayOfString11[0] = "background_image_uri";
    arrayOfString11[1] = "badge_uri";
    arrayOfString11[2] = "color_hint";
    arrayOfString11[3] = "activity_title";
    META_PROJECTION = arrayOfString11;
    sUriMatcher = new UriMatcher(-1);
    sUriMatcher.addURI("com.google.android.music.xdi", "launcher", 2);
    sUriMatcher.addURI("com.google.android.music.xdi", "launcher/items/", 3);
    sUriMatcher.addURI("com.google.android.music.xdi", "launcher/items/#", 4);
    sUriMatcher.addURI("com.google.android.music.xdi", "browse/headers", 5);
    sUriMatcher.addURI("com.google.android.music.xdi", "browse/#", 6);
    sUriMatcher.addURI("com.google.android.music.xdi", "mymusic/headers", 7);
    sUriMatcher.addURI("com.google.android.music.xdi", "mymusic/#", 8);
    sUriMatcher.addURI("com.google.android.music.xdi", "mygenres/headers", 9);
    sUriMatcher.addURI("com.google.android.music.xdi", "mygenres/#", 10);
    sUriMatcher.addURI("com.google.android.music.xdi", "details/albums/*", 100);
    sUriMatcher.addURI("com.google.android.music.xdi", "details/albums/*/sections", 101);
    sUriMatcher.addURI("com.google.android.music.xdi", "details/albums/*/tracks", 102);
    sUriMatcher.addURI("com.google.android.music.xdi", "details/albums/*/actions", 103);
    sUriMatcher.addURI("com.google.android.music.xdi", "details/playlists/#", 104);
    sUriMatcher.addURI("com.google.android.music.xdi", "details/playlists/#/sections", 105);
    sUriMatcher.addURI("com.google.android.music.xdi", "details/playlists/#/tracks", 106);
    sUriMatcher.addURI("com.google.android.music.xdi", "details/playlists/#/actions", 107);
    sUriMatcher.addURI("com.google.android.music.xdi", "details/artists/*", 108);
    sUriMatcher.addURI("com.google.android.music.xdi", "details/artists/*/sections", 109);
    sUriMatcher.addURI("com.google.android.music.xdi", "details/artists/*/albums", 110);
    sUriMatcher.addURI("com.google.android.music.xdi", "details/artists/*/actions", 111);
    sUriMatcher.addURI("com.google.android.music.xdi", "details/artists/*/topsongs", 112);
    sUriMatcher.addURI("com.google.android.music.xdi", "details/artists/*/lockersongs", 113);
    sUriMatcher.addURI("com.google.android.music.xdi", "search", 11);
    sUriMatcher.addURI("com.google.android.music.xdi", "search/headers/#", 12);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/headers", 13);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/#", 14);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/featured/headers", 15);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/featured/*/*", 16);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/recommendations/headers", 17);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/recommendations/*/*", 18);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/newreleases/headers", 19);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/newreleases/*/*", 20);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/genres/*/*/headers", 21);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/genres/*/*", 22);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/genre/featured/items/*/*/*", 26);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/genre/newreleases/items/*/*/*", 28);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/genre/featured/*/headers", 25);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/genre/newreleases/*/headers", 27);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/genre/items/*", 24);
    sUriMatcher.addURI("com.google.android.music.xdi", "explore/genre/*/headers", 23);
    sUriMatcher.addURI("com.google.android.music.xdi", "meta/#", 29);
    sUriMatcher.addURI("com.google.android.music.xdi", "metatitle/*", 30);
  }

  public static void notifyAccountChanged(Context paramContext)
  {
    notifyClustersDataChanged(paramContext);
    notifyHeadersDataChanged(paramContext);
  }

  public static void notifyChange(Context paramContext, Uri paramUri)
  {
    paramContext.getContentResolver().notifyChange(paramUri, null, false);
  }

  public static void notifyClustersDataChanged(Context paramContext)
  {
    Uri localUri1 = BASE_URI.buildUpon().appendEncodedPath("launcher").build();
    notifyChange(paramContext, localUri1);
    Uri localUri2 = BASE_URI.buildUpon().appendEncodedPath("launcher/items/").build();
    notifyChange(paramContext, localUri2);
  }

  public static void notifyHeadersDataChanged(Context paramContext)
  {
    Uri localUri = BASE_URI.buildUpon().appendEncodedPath("browse").build();
    notifyChange(paramContext, localUri);
  }

  private Cursor queryImpl(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    if (Log.isLoggable("MusicXdi", 2))
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("Queried: ");
      Uri localUri1 = paramUri;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(localUri1).append("; projection: ");
      String str1 = Arrays.deepToString(paramArrayOfString1);
      String str2 = str1;
      Log.v("MusicXdi", str2);
    }
    UriMatcher localUriMatcher = sUriMatcher;
    Uri localUri2 = paramUri;
    int i = localUriMatcher.match(localUri2);
    Object localObject1 = null;
    switch (i)
    {
    default:
    case 2:
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
    case 8:
    case 9:
    case 10:
    case 11:
    case 12:
    case 13:
    case 14:
    case 15:
    case 25:
    case 16:
    case 17:
    case 18:
    case 19:
    case 27:
    case 20:
    case 21:
    case 22:
    case 23:
    case 24:
    case 26:
    case 28:
    case 100:
    case 101:
    case 102:
    case 103:
    case 104:
    case 105:
    case 106:
    case 107:
    case 108:
    case 109:
    case 110:
    case 112:
    case 113:
    case 29:
    case 30:
    }
    while (true)
    {
      if (Log.isLoggable("MusicXdi", 2))
      {
        StringBuilder localStringBuilder3 = new StringBuilder().append("Cursor: ");
        String str3 = DatabaseUtils.dumpCursorToString((Cursor)localObject1);
        String str4 = str3;
        Log.v("MusicXdi", str4);
      }
      return localObject1;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_LAUNCHER_ROOT_PROJECTION;
      Context localContext1 = getContext();
      String[] arrayOfString1 = paramArrayOfString1;
      localObject1 = new LauncherRootCursor(localContext1, arrayOfString1);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_LAUNCHER_CLUSTER_PROJECTION;
      Context localContext2 = getContext();
      String[] arrayOfString2 = paramArrayOfString1;
      localObject1 = new LauncherClustersCursor(localContext2, arrayOfString2);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_LAUNCHER_CLUSTER_PROJECTION;
      long l1 = ContentUris.parseId(paramUri);
      Context localContext3 = getContext();
      String[] arrayOfString3 = paramArrayOfString1;
      long l2 = l1;
      localObject1 = new LauncherClustersCursor(localContext3, arrayOfString3, l2);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADERS_PROJECTION;
      Context localContext4 = getContext();
      String[] arrayOfString4 = paramArrayOfString1;
      localObject1 = new BrowseAllRootHeadersCursor(localContext4, arrayOfString4);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      long l3 = ContentUris.parseId(paramUri);
      Context localContext5 = getContext();
      String[] arrayOfString5 = paramArrayOfString1;
      localObject1 = new BrowseRootHeaderCursor(localContext5, arrayOfString5, l3);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADERS_PROJECTION;
      Context localContext6 = getContext();
      String[] arrayOfString6 = paramArrayOfString1;
      localObject1 = new BrowseAllMyMusicHeadersCursor(localContext6, arrayOfString6);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      long l4 = ContentUris.parseId(paramUri);
      Context localContext7 = getContext();
      String[] arrayOfString7 = paramArrayOfString1;
      localObject1 = new BrowseMyMusicHeaderCursor(localContext7, arrayOfString7, l4);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADERS_PROJECTION;
      Context localContext8 = getContext();
      String[] arrayOfString8 = paramArrayOfString1;
      localObject1 = new BrowseAllMyGenresHeadersCursor(localContext8, arrayOfString8);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      long l5 = ContentUris.parseId(paramUri);
      Context localContext9 = getContext();
      String[] arrayOfString9 = paramArrayOfString1;
      localObject1 = new BrowseMyGenresHeaderCursor(localContext9, arrayOfString9, l5);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_SEARCH_RESULTS_PROJECTION;
      String str5 = paramUri.getQueryParameter("q");
      Context localContext10 = getContext();
      String[] arrayOfString10 = paramArrayOfString1;
      localObject1 = new SearchHeadersCursor(localContext10, arrayOfString10, str5);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      long l6 = ContentUris.parseId(paramUri);
      String str6 = paramUri.getQueryParameter("q");
      Context localContext11 = getContext();
      String[] arrayOfString11 = paramArrayOfString1;
      localObject1 = new SearchHeaderItemsCursor(localContext11, arrayOfString11, str6, l6);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADERS_PROJECTION;
      Context localContext12 = getContext();
      String[] arrayOfString12 = paramArrayOfString1;
      localObject1 = new ExploreHeadersCursor(localContext12, arrayOfString12);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      long l7 = ContentUris.parseId(paramUri);
      localObject1 = new com/google/android/music/xdi/ExploreItemsCursor;
      Context localContext13 = getContext();
      Object localObject2 = localObject1;
      String[] arrayOfString13 = paramArrayOfString1;
      long l8 = l7;
      localObject2.<init>(localContext13, arrayOfString13, l8, null);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADERS_PROJECTION;
      Context localContext14 = getContext();
      String[] arrayOfString14 = paramArrayOfString1;
      localObject1 = new ExploreFeaturedHeadersCursor(localContext14, arrayOfString14, null);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADERS_PROJECTION;
      List localList1 = paramUri.getPathSegments();
      int j = localList1.size() + -2;
      String str7 = (String)localList1.get(j);
      Context localContext15 = getContext();
      String[] arrayOfString15 = paramArrayOfString1;
      localObject1 = new ExploreFeaturedHeadersCursor(localContext15, arrayOfString15, str7);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      List localList2 = paramUri.getPathSegments();
      int k = localList2.size() + -2;
      long l9 = Long.valueOf((String)localList2.get(k)).longValue();
      int m = localList2.size() + -1;
      int n = Integer.valueOf((String)localList2.get(m)).intValue();
      localObject1 = new com/google/android/music/xdi/ExploreFeaturedItemsCursor;
      Context localContext16 = getContext();
      Object localObject3 = localObject1;
      String[] arrayOfString16 = paramArrayOfString1;
      localObject3.<init>(localContext16, arrayOfString16, l9, n, null);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADERS_PROJECTION;
      Context localContext17 = getContext();
      String[] arrayOfString17 = paramArrayOfString1;
      localObject1 = new ExploreRecommendationsHeadersCursor(localContext17, arrayOfString17);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      List localList3 = paramUri.getPathSegments();
      int i1 = localList3.size() + -2;
      long l10 = Long.valueOf((String)localList3.get(i1)).longValue();
      int i2 = localList3.size() + -1;
      int i3 = Integer.valueOf((String)localList3.get(i2)).intValue();
      localObject1 = new com/google/android/music/xdi/ExploreRecommendationsItemsCursor;
      Context localContext18 = getContext();
      Object localObject4 = localObject1;
      String[] arrayOfString18 = paramArrayOfString1;
      localObject4.<init>(localContext18, arrayOfString18, l10, i3);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADERS_PROJECTION;
      Context localContext19 = getContext();
      String[] arrayOfString19 = paramArrayOfString1;
      localObject1 = new ExploreNewReleasesHeadersCursor(localContext19, arrayOfString19, null);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADERS_PROJECTION;
      List localList4 = paramUri.getPathSegments();
      int i4 = localList4.size() + -2;
      String str8 = (String)localList4.get(i4);
      Context localContext20 = getContext();
      String[] arrayOfString20 = paramArrayOfString1;
      localObject1 = new ExploreNewReleasesHeadersCursor(localContext20, arrayOfString20, str8);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      List localList5 = paramUri.getPathSegments();
      int i5 = localList5.size() + -2;
      long l11 = Long.valueOf((String)localList5.get(i5)).longValue();
      int i6 = localList5.size() + -1;
      int i7 = Integer.valueOf((String)localList5.get(i6)).intValue();
      localObject1 = new com/google/android/music/xdi/ExploreNewReleasesItemsCursor;
      Context localContext21 = getContext();
      Object localObject5 = localObject1;
      String[] arrayOfString21 = paramArrayOfString1;
      localObject5.<init>(localContext21, arrayOfString21, l11, i7, null);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADERS_PROJECTION;
      Context localContext22 = getContext();
      String[] arrayOfString22 = paramArrayOfString1;
      localObject1 = new ExploreGenresHeadersCursor(localContext22, arrayOfString22, null);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      List localList6 = paramUri.getPathSegments();
      int i8 = localList6.size() + -2;
      String str9 = (String)localList6.get(i8);
      int i9 = localList6.size() + -1;
      String str10 = (String)localList6.get(i9);
      Context localContext23 = getContext();
      String[] arrayOfString23 = paramArrayOfString1;
      String str11 = str10;
      localObject1 = new ExploreGenresItemsCursor(localContext23, arrayOfString23, str9, str11);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADERS_PROJECTION;
      List localList7 = paramUri.getPathSegments();
      int i10 = localList7.size() + -2;
      String str12 = (String)localList7.get(i10);
      Context localContext24 = getContext();
      String[] arrayOfString24 = paramArrayOfString1;
      localObject1 = new ExploreGenreHeadersCursor(localContext24, arrayOfString24, str12);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      Context localContext25 = getContext();
      String str13 = paramUri.getLastPathSegment();
      String[] arrayOfString25 = paramArrayOfString1;
      localObject1 = new ExploreGenreItemsCursor(localContext25, arrayOfString25, str13);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      List localList8 = paramUri.getPathSegments();
      int i11 = localList8.size() + -3;
      long l12 = Long.valueOf((String)localList8.get(i11)).longValue();
      int i12 = localList8.size() + -2;
      int i13 = Integer.valueOf((String)localList8.get(i12)).intValue();
      int i14 = localList8.size() + -1;
      String str14 = (String)localList8.get(i14);
      localObject1 = new com/google/android/music/xdi/ExploreFeaturedItemsCursor;
      Context localContext26 = getContext();
      Object localObject6 = localObject1;
      String[] arrayOfString26 = paramArrayOfString1;
      localObject6.<init>(localContext26, arrayOfString26, l12, i13, str14);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      List localList9 = paramUri.getPathSegments();
      int i15 = localList9.size() + -3;
      long l13 = Long.valueOf((String)localList9.get(i15)).longValue();
      int i16 = localList9.size() + -2;
      int i17 = Integer.valueOf((String)localList9.get(i16)).intValue();
      int i18 = localList9.size() + -1;
      String str15 = (String)localList9.get(i18);
      localObject1 = new com/google/android/music/xdi/ExploreNewReleasesItemsCursor;
      Context localContext27 = getContext();
      Object localObject7 = localObject1;
      String[] arrayOfString27 = paramArrayOfString1;
      localObject7.<init>(localContext27, arrayOfString27, l13, i17, str15);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_DETAIL_ITEM_PROJECTION;
      Context localContext28 = getContext();
      String str16 = paramUri.getLastPathSegment();
      String[] arrayOfString28 = paramArrayOfString1;
      localObject1 = new DetailAlbumCursor(localContext28, arrayOfString28, str16);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_DETAIL_SECTIONS_PROJECTION;
      List localList10 = paramUri.getPathSegments();
      int i19 = localList10.size() + -2;
      String str17 = (String)localList10.get(i19);
      Context localContext29 = getContext();
      String[] arrayOfString29 = paramArrayOfString1;
      String str18 = str17;
      localObject1 = new DetailAlbumSectionsCursor(localContext29, arrayOfString29, str18);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_DETAIL_CHILDREN_PROJECTION;
      List localList11 = paramUri.getPathSegments();
      int i20 = localList11.size() + -2;
      String str19 = (String)localList11.get(i20);
      Context localContext30 = getContext();
      String[] arrayOfString30 = paramArrayOfString1;
      String str20 = str19;
      localObject1 = new DetailAlbumTracklistCursor(localContext30, arrayOfString30, str20);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_DETAIL_ACTIONS_PROJECTION;
      List localList12 = paramUri.getPathSegments();
      int i21 = localList12.size() + -2;
      String str21 = (String)localList12.get(i21);
      Context localContext31 = getContext();
      String[] arrayOfString31 = paramArrayOfString1;
      String str22 = str21;
      localObject1 = new DetailAlbumActionsCursor(localContext31, arrayOfString31, str22);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_DETAIL_ITEM_PROJECTION;
      long l14 = ContentUris.parseId(paramUri);
      Context localContext32 = getContext();
      String[] arrayOfString32 = paramArrayOfString1;
      long l15 = l14;
      localObject1 = new DetailPlaylistCursor(localContext32, arrayOfString32, l15);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_DETAIL_SECTIONS_PROJECTION;
      List localList13 = paramUri.getPathSegments();
      int i22 = localList13.size() + -2;
      long l16 = Long.parseLong((String)localList13.get(i22));
      Context localContext33 = getContext();
      String[] arrayOfString33 = paramArrayOfString1;
      long l17 = l16;
      localObject1 = new DetailPlaylistSectionsCursor(localContext33, arrayOfString33, l17);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_DETAIL_CHILDREN_PROJECTION;
      List localList14 = paramUri.getPathSegments();
      int i23 = localList14.size() + -2;
      long l18 = Long.parseLong((String)localList14.get(i23));
      Context localContext34 = getContext();
      String[] arrayOfString34 = paramArrayOfString1;
      long l19 = l18;
      localObject1 = new DetailPlaylistTracklistCursor(localContext34, arrayOfString34, l19);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_DETAIL_ACTIONS_PROJECTION;
      List localList15 = paramUri.getPathSegments();
      int i24 = localList15.size() + -2;
      long l20 = Long.parseLong((String)localList15.get(i24));
      Context localContext35 = getContext();
      String[] arrayOfString35 = paramArrayOfString1;
      long l21 = l20;
      localObject1 = new DetailPlaylistActionsCursor(localContext35, arrayOfString35, l21);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_DETAIL_ITEM_PROJECTION;
      Context localContext36 = getContext();
      String str23 = paramUri.getLastPathSegment();
      String[] arrayOfString36 = paramArrayOfString1;
      localObject1 = new DetailArtistCursor(localContext36, arrayOfString36, str23);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_DETAIL_SECTIONS_PROJECTION;
      List localList16 = paramUri.getPathSegments();
      int i25 = localList16.size() + -2;
      String str24 = (String)localList16.get(i25);
      Context localContext37 = getContext();
      String[] arrayOfString37 = paramArrayOfString1;
      String str25 = str24;
      localObject1 = new DetailArtistSectionsCursor(localContext37, arrayOfString37, str25);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_DETAIL_CHILDREN_PROJECTION;
      List localList17 = paramUri.getPathSegments();
      int i26 = localList17.size() + -2;
      String str26 = (String)localList17.get(i26);
      Context localContext38 = getContext();
      String[] arrayOfString38 = paramArrayOfString1;
      String str27 = str26;
      localObject1 = new DetailArtistAlbumsCursor(localContext38, arrayOfString38, str27);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_DETAIL_CHILDREN_PROJECTION;
      List localList18 = paramUri.getPathSegments();
      int i27 = localList18.size() + -2;
      String str28 = (String)localList18.get(i27);
      Context localContext39 = getContext();
      String[] arrayOfString39 = paramArrayOfString1;
      String str29 = str28;
      localObject1 = new DetailArtistTopSongsCursor(localContext39, arrayOfString39, str29);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = DEFAULT_DETAIL_CHILDREN_PROJECTION;
      List localList19 = paramUri.getPathSegments();
      int i28 = localList19.size() + -2;
      String str30 = (String)localList19.get(i28);
      Context localContext40 = getContext();
      String[] arrayOfString40 = paramArrayOfString1;
      String str31 = str30;
      localObject1 = new DetailArtistLockerSongsCursor(localContext40, arrayOfString40, str31);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = META_PROJECTION;
      Context localContext41 = getContext();
      long l22 = ContentUris.parseId(paramUri);
      String[] arrayOfString41 = paramArrayOfString1;
      localObject1 = new MetaIdCursor(localContext41, arrayOfString41, l22);
      continue;
      if (paramArrayOfString1 == null)
        paramArrayOfString1 = META_PROJECTION;
      Context localContext42 = getContext();
      String str32 = paramUri.getLastPathSegment();
      String[] arrayOfString42 = paramArrayOfString1;
      localObject1 = new MetaTitleCursor(localContext42, arrayOfString42, str32);
    }
  }

  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException("Delete not supported");
  }

  public String getType(Uri paramUri)
  {
    return null;
  }

  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    throw new UnsupportedOperationException("Insert not supported");
  }

  public boolean onCreate()
  {
    if (XdiUtils.isXdiEnvironment(getContext()))
    {
      SignupStatus.launchVerificationCheck(getContext());
      registerMusicContentObserver();
    }
    return true;
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    long l = Binder.clearCallingIdentity();
    try
    {
      Cursor localCursor1 = queryImpl(paramUri, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2);
      Cursor localCursor2 = localCursor1;
      return localCursor2;
    }
    finally
    {
      Binder.restoreCallingIdentity(l);
    }
  }

  void registerMusicContentObserver()
  {
    if (this.mMusicContentObserver != null)
      return;
    Handler localHandler = new Handler();
    ContentObserver local1 = new ContentObserver(localHandler)
    {
      public void onChange(boolean paramAnonymousBoolean)
      {
        Context localContext = XdiContentProvider.this.getContext();
        Uri localUri = XdiContentProvider.BASE_URI;
        XdiContentProvider.notifyChange(localContext, localUri);
      }
    };
    this.mMusicContentObserver = local1;
    ContentResolver localContentResolver = getContext().getContentResolver();
    Uri localUri = MusicContent.CONTENT_URI;
    ContentObserver localContentObserver = this.mMusicContentObserver;
    localContentResolver.registerContentObserver(localUri, false, localContentObserver);
  }

  public void shutdown()
  {
    unregisterMusicContentObserver();
  }

  void unregisterMusicContentObserver()
  {
    if (this.mMusicContentObserver == null)
      return;
    ContentResolver localContentResolver = getContext().getContentResolver();
    ContentObserver localContentObserver = this.mMusicContentObserver;
    localContentResolver.unregisterContentObserver(localContentObserver);
    this.mMusicContentObserver = null;
  }

  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException("Update not supported");
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.XdiContentProvider
 * JD-Core Version:    0.6.2
 */