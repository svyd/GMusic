package com.google.android.music.xdi;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent.AlbumArt;
import com.google.android.music.store.MusicContent.Explore;
import com.google.android.music.store.Store;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.ExploreDocumentClusterBuilder;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.SystemUtils;
import java.util.Random;

class XdiUtils
{
  private static final String[] SUBGENRES_COUNT_COLUMNS = arrayOfString;
  private static final String[] XDI_PACKAGE_NAMES = new String[0];
  static final Random sRandom = new Random();

  static
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "count(*)";
  }

  static void extractDataForAlbums(Context paramContext, Cursor paramCursor, ProjectionMap paramProjectionMap, Object[] paramArrayOfObject, int paramInt1, int paramInt2)
  {
    Document localDocument = ExploreDocumentClusterBuilder.getAlbumDocument(new Document(), paramCursor);
    String str1 = localDocument.getArtUrl();
    String str2 = localDocument.getAlbumMetajamId();
    if (!TextUtils.isEmpty(str2))
      if (TextUtils.isEmpty(str1))
        str1 = getDefaultAlbumArtUri(paramContext);
    while (true)
    {
      String str3 = localDocument.getTitle();
      String str4 = localDocument.getArtistName();
      Uri localUri1 = XdiContentProvider.BASE_URI;
      String str5 = "details/albums/" + str2;
      Uri localUri2 = Uri.withAppendedPath(localUri1, str5);
      Intent localIntent = new Intent("com.google.android.xdi.action.DETAIL", localUri2);
      Integer localInteger1 = Integer.valueOf(paramCursor.getPosition());
      ProjectionMap localProjectionMap1 = paramProjectionMap;
      Object[] arrayOfObject1 = paramArrayOfObject;
      localProjectionMap1.writeValueToArray(arrayOfObject1, "_id", localInteger1);
      Integer localInteger2 = Integer.valueOf(1);
      ProjectionMap localProjectionMap2 = paramProjectionMap;
      Object[] arrayOfObject2 = paramArrayOfObject;
      localProjectionMap2.writeValueToArray(arrayOfObject2, "parent_id", localInteger2);
      ProjectionMap localProjectionMap3 = paramProjectionMap;
      Object[] arrayOfObject3 = paramArrayOfObject;
      localProjectionMap3.writeValueToArray(arrayOfObject3, "display_name", str3);
      ProjectionMap localProjectionMap4 = paramProjectionMap;
      Object[] arrayOfObject4 = paramArrayOfObject;
      localProjectionMap4.writeValueToArray(arrayOfObject4, "display_description", str4);
      ProjectionMap localProjectionMap5 = paramProjectionMap;
      Object[] arrayOfObject5 = paramArrayOfObject;
      localProjectionMap5.writeValueToArray(arrayOfObject5, "image_uri", str1);
      ProjectionMap localProjectionMap6 = paramProjectionMap;
      Object[] arrayOfObject6 = paramArrayOfObject;
      localProjectionMap6.writeValueToArray(arrayOfObject6, "width", null);
      ProjectionMap localProjectionMap7 = paramProjectionMap;
      Object[] arrayOfObject7 = paramArrayOfObject;
      localProjectionMap7.writeValueToArray(arrayOfObject7, "height", null);
      String str6 = localIntent.toUri(1);
      ProjectionMap localProjectionMap8 = paramProjectionMap;
      Object[] arrayOfObject8 = paramArrayOfObject;
      localProjectionMap8.writeValueToArray(arrayOfObject8, "intent_uri", str6);
      return;
      long l = localDocument.getAlbumId();
      str2 = String.valueOf(l);
      if (TextUtils.isEmpty(str1))
      {
        int i = paramInt1;
        int j = paramInt2;
        str1 = MusicContent.AlbumArt.getAlbumArtUri(l, true, i, j).toString();
      }
    }
  }

  static void extractDataForArtists(Context paramContext, Cursor paramCursor, ProjectionMap paramProjectionMap, Object[] paramArrayOfObject)
  {
    Document localDocument = ExploreDocumentClusterBuilder.getArtistDocument(new Document(), paramCursor);
    String str1 = localDocument.getArtUrl();
    if (TextUtils.isEmpty(str1))
      str1 = getDefaultArtistArtUri(paramContext);
    String str2 = localDocument.getArtistMetajamId();
    if (TextUtils.isEmpty(str2))
      str2 = String.valueOf(localDocument.getArtistId());
    String str3 = localDocument.getArtistName();
    Uri localUri1 = XdiContentProvider.BASE_URI;
    String str4 = "details/artists/" + str2;
    Uri localUri2 = Uri.withAppendedPath(localUri1, str4);
    Intent localIntent = new Intent("com.google.android.xdi.action.DETAIL", localUri2);
    Integer localInteger1 = Integer.valueOf(paramCursor.getPosition());
    paramProjectionMap.writeValueToArray(paramArrayOfObject, "_id", localInteger1);
    Integer localInteger2 = Integer.valueOf(1);
    paramProjectionMap.writeValueToArray(paramArrayOfObject, "parent_id", localInteger2);
    paramProjectionMap.writeValueToArray(paramArrayOfObject, "display_name", str3);
    paramProjectionMap.writeValueToArray(paramArrayOfObject, "display_description", null);
    paramProjectionMap.writeValueToArray(paramArrayOfObject, "image_uri", str1);
    paramProjectionMap.writeValueToArray(paramArrayOfObject, "width", null);
    paramProjectionMap.writeValueToArray(paramArrayOfObject, "height", null);
    String str5 = localIntent.toUri(1);
    paramProjectionMap.writeValueToArray(paramArrayOfObject, "intent_uri", str5);
  }

  static boolean extractDataForGroupType(Context paramContext, Cursor paramCursor, ProjectionMap paramProjectionMap, Object[] paramArrayOfObject, int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool;
    switch (paramInt1)
    {
    default:
      String str = "Unexpected group type: " + paramInt1;
      Log.wtf("MusicXdi", str);
      bool = false;
      return bool;
    case 0:
      extractDataForSongs(paramContext, paramCursor, paramProjectionMap, paramArrayOfObject);
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      bool = true;
      break;
      Context localContext1 = paramContext;
      Cursor localCursor1 = paramCursor;
      ProjectionMap localProjectionMap1 = paramProjectionMap;
      Object[] arrayOfObject1 = paramArrayOfObject;
      int i = paramInt2;
      int j = paramInt3;
      extractDataForAlbums(localContext1, localCursor1, localProjectionMap1, arrayOfObject1, i, j);
      continue;
      extractDataForArtists(paramContext, paramCursor, paramProjectionMap, paramArrayOfObject);
      continue;
      Context localContext2 = paramContext;
      Cursor localCursor2 = paramCursor;
      ProjectionMap localProjectionMap2 = paramProjectionMap;
      Object[] arrayOfObject2 = paramArrayOfObject;
      int k = paramInt2;
      int m = paramInt3;
      extractDataForSharedPlaylists(localContext2, localCursor2, localProjectionMap2, arrayOfObject2, k, m);
    }
  }

  static void extractDataForSharedPlaylists(Context paramContext, Cursor paramCursor, ProjectionMap paramProjectionMap, Object[] paramArrayOfObject, int paramInt1, int paramInt2)
  {
    Document localDocument = ExploreDocumentClusterBuilder.getPlaylistDocument(new Document(), paramCursor);
    long l = localDocument.getId();
    String str1 = localDocument.getArtUrl();
    if (TextUtils.isEmpty(str1));
    String[] arrayOfString;
    int j;
    for (str1 = getDefaultArtUri(paramContext); ; str1 = arrayOfString[j])
    {
      do
      {
        String str2 = localDocument.getPlaylistName();
        String str3 = localDocument.getDescription();
        Intent localIntent1 = newXdiPlayIntent();
        Intent localIntent2 = localIntent1.putExtra("container", 11);
        Intent localIntent3 = localIntent1.putExtra("id", l);
        Intent localIntent4 = localIntent1.putExtra("offset", 0);
        Intent localIntent5 = localIntent1.putExtra("name", str2);
        String str4 = localDocument.getPlaylistShareToken();
        Intent localIntent6 = localIntent1.putExtra("playlist_share_token", str4);
        Intent localIntent7 = localIntent1.putExtra("description", str3);
        String str5 = localDocument.getPlaylistOwnerName();
        Intent localIntent8 = localIntent1.putExtra("owner_name", str5);
        String str6 = localDocument.getPlaylistOwnerProfilePhotoUrl();
        Intent localIntent9 = localIntent1.putExtra("playlist_owner_photo_url", str6);
        Intent localIntent10 = localIntent1.putExtra("art_uri", str1);
        Integer localInteger1 = Integer.valueOf(paramCursor.getPosition());
        paramProjectionMap.writeValueToArray(paramArrayOfObject, "_id", localInteger1);
        Integer localInteger2 = Integer.valueOf(1);
        paramProjectionMap.writeValueToArray(paramArrayOfObject, "parent_id", localInteger2);
        paramProjectionMap.writeValueToArray(paramArrayOfObject, "display_name", str2);
        paramProjectionMap.writeValueToArray(paramArrayOfObject, "display_description", str3);
        paramProjectionMap.writeValueToArray(paramArrayOfObject, "image_uri", str1);
        paramProjectionMap.writeValueToArray(paramArrayOfObject, "width", null);
        paramProjectionMap.writeValueToArray(paramArrayOfObject, "height", null);
        String str7 = localIntent1.toUri(1);
        paramProjectionMap.writeValueToArray(paramArrayOfObject, "intent_uri", str7);
        return;
        arrayOfString = MusicUtils.decodeStringArray(str1);
      }
      while ((arrayOfString == null) || (arrayOfString.length <= 0));
      Random localRandom = sRandom;
      int i = arrayOfString.length + -1;
      j = localRandom.nextInt(i);
    }
  }

  static void extractDataForSongs(Context paramContext, Cursor paramCursor, ProjectionMap paramProjectionMap, Object[] paramArrayOfObject)
  {
    Document localDocument = ExploreDocumentClusterBuilder.getTrackDocument(new Document(), paramCursor);
    String str1 = localDocument.getArtUrl();
    if (TextUtils.isEmpty(str1))
      str1 = getDefaultArtUri(paramContext);
    String str2 = localDocument.getTitle();
    String str3 = localDocument.getAlbumName();
    Intent localIntent1 = newXdiPlayIntent();
    String str4 = localDocument.getTrackMetajamId();
    if (!TextUtils.isEmpty(str4))
    {
      Intent localIntent2 = localIntent1.putExtra("container", 7);
      Intent localIntent3 = localIntent1.putExtra("id_string", str4);
    }
    while (true)
    {
      Intent localIntent4 = localIntent1.putExtra("name", str2);
      Intent localIntent5 = localIntent1.putExtra("art_uri", str1);
      Integer localInteger1 = Integer.valueOf(paramCursor.getPosition());
      paramProjectionMap.writeValueToArray(paramArrayOfObject, "_id", localInteger1);
      Integer localInteger2 = Integer.valueOf(1);
      paramProjectionMap.writeValueToArray(paramArrayOfObject, "parent_id", localInteger2);
      paramProjectionMap.writeValueToArray(paramArrayOfObject, "display_name", str2);
      paramProjectionMap.writeValueToArray(paramArrayOfObject, "display_description", str3);
      paramProjectionMap.writeValueToArray(paramArrayOfObject, "image_uri", str1);
      paramProjectionMap.writeValueToArray(paramArrayOfObject, "width", null);
      paramProjectionMap.writeValueToArray(paramArrayOfObject, "height", null);
      String str5 = localIntent1.toUri(1);
      paramProjectionMap.writeValueToArray(paramArrayOfObject, "intent_uri", str5);
      return;
      Intent localIntent6 = localIntent1.putExtra("container", 6);
      long l = localDocument.getId();
      Intent localIntent7 = localIntent1.putExtra("id", l);
    }
  }

  static String[] getClusterProjection(int paramInt)
  {
    String[] arrayOfString;
    switch (paramInt)
    {
    default:
      String str = "Invalid group type: " + paramInt;
      Log.wtf("MusicXdi", str);
      arrayOfString = null;
    case 0:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      return arrayOfString;
      arrayOfString = MusicProjections.SONG_COLUMNS;
      continue;
      arrayOfString = ExploreDocumentClusterBuilder.ALBUM_COLUMNS;
      continue;
      arrayOfString = ExploreDocumentClusterBuilder.ARTIST_COLUMNS;
      continue;
      arrayOfString = ExploreDocumentClusterBuilder.SHARED_WITH_ME_PLAYLIST_COLUMNS;
    }
  }

  static String getDefaultAlbumArtUri(Context paramContext)
  {
    return UriUtils.getAndroidResourceUri(paramContext, 2130837597);
  }

  static String getDefaultArtUri(Context paramContext)
  {
    return UriUtils.getAndroidResourceUri(paramContext, 2130837675);
  }

  static String getDefaultArtistArtUri(Context paramContext)
  {
    return UriUtils.getAndroidResourceUri(paramContext, 2130837598);
  }

  static int getDefaultArtistItemHeightDp(Context paramContext)
  {
    return paramContext.getResources().getInteger(2131492879);
  }

  static int getDefaultArtistItemWidthDp(Context paramContext)
  {
    return paramContext.getResources().getInteger(2131492878);
  }

  static String getDefaultBadgeUri(Context paramContext)
  {
    return UriUtils.getAndroidResourceUri(paramContext, 2130837720);
  }

  static String getDefaultGenreArtUri(Context paramContext)
  {
    return getDefaultArtUri(paramContext);
  }

  static int getDefaultItemHeightDp(Context paramContext)
  {
    return paramContext.getResources().getInteger(2131492877);
  }

  static int getDefaultItemHeightPx(Context paramContext)
  {
    float f = getDefaultItemHeightDp(paramContext);
    return (int)DipUtil.getPxFromDip(paramContext, f);
  }

  static int getDefaultItemWidthDp(Context paramContext)
  {
    return paramContext.getResources().getInteger(2131492876);
  }

  static int getDefaultItemWidthPx(Context paramContext)
  {
    float f = getDefaultItemWidthDp(paramContext);
    return (int)DipUtil.getPxFromDip(paramContext, f);
  }

  static Uri getMetaTitleUri(String paramString)
  {
    Uri.Builder localBuilder = XdiContentProvider.BASE_URI.buildUpon().appendPath("metatitle");
    String str = String.valueOf(paramString);
    return localBuilder.appendPath(str).build();
  }

  static Uri getMetaUri(long paramLong)
  {
    Uri.Builder localBuilder = XdiContentProvider.BASE_URI.buildUpon().appendPath("meta");
    String str = String.valueOf(paramLong);
    return localBuilder.appendPath(str).build();
  }

  static Account getSelectedAccount(Context paramContext)
  {
    Object localObject1 = new Object();
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, localObject1);
    try
    {
      Account localAccount1 = localMusicPreferences.getSyncAccount();
      Account localAccount2 = localAccount1;
      return localAccount2;
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(localObject1);
    }
  }

  static boolean hasSubgenres(Context paramContext, String paramString)
  {
    int i = 0;
    Uri localUri = MusicContent.Explore.getGenresUri(paramString);
    String[] arrayOfString1 = SUBGENRES_COUNT_COLUMNS;
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str);
    if (localCursor != null);
    while (true)
    {
      try
      {
        if (localCursor.moveToFirst())
        {
          int j = localCursor.getInt(0);
          i = j;
        }
        Store.safeClose(localCursor);
        if (i > 0)
        {
          bool = true;
          return bool;
        }
      }
      finally
      {
        Store.safeClose(localCursor);
      }
      boolean bool = false;
    }
  }

  static boolean isAccountSelected(Context paramContext)
  {
    if (getSelectedAccount(paramContext) != null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  static boolean isXdiClientInstalled(Context paramContext)
  {
    String[] arrayOfString = XDI_PACKAGE_NAMES;
    int i = arrayOfString.length;
    int j = 0;
    if (j < i)
    {
      String str = arrayOfString[j];
      if (!SystemUtils.isPackageInstalled(paramContext, str));
    }
    for (boolean bool = true; ; bool = false)
    {
      return bool;
      j += 1;
      break;
    }
  }

  public static boolean isXdiEnvironment(Context paramContext)
  {
    if ((MusicPreferences.isGlass()) || (isXdiClientInstalled(paramContext)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  static Intent newXdiPlayIntent()
  {
    return new Intent("com.google.android.music.xdi.intent.PLAY");
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.XdiUtils
 * JD-Core Version:    0.6.2
 */