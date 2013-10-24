package com.google.android.music.xdi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.music.log.Log;
import com.google.android.music.store.MusicContent.AlbumArt;
import com.google.android.music.store.MusicContent.PlaylistArt;
import com.google.android.music.store.MusicContent.Search;
import com.google.android.music.utils.MusicUtils;

class SearchHeaderItemsCursor extends XdiCursor
{
  private static final String[] PROJECTION_SEARCH = arrayOfString;
  private final int mArtistItemHeight;
  private final int mArtistItemWidth;
  private final Context mContext;
  private final long mHeaderId;
  private final int mImageHeight;
  private final int mImageWidth;
  private final ProjectionMap mProjectionMap;

  static
  {
    String[] arrayOfString = new String[20];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "searchType";
    arrayOfString[2] = "ListType";
    arrayOfString[3] = "searchName";
    arrayOfString[4] = "Album";
    arrayOfString[5] = "AlbumArtist";
    arrayOfString[6] = "AlbumArtistId";
    arrayOfString[7] = "Artist";
    arrayOfString[8] = "itemCount";
    arrayOfString[9] = "albumCount";
    arrayOfString[10] = "year";
    arrayOfString[11] = "duration";
    arrayOfString[12] = "AlbumId";
    arrayOfString[13] = "searchSortName";
    arrayOfString[14] = "hasLocal";
    arrayOfString[15] = "hasRemote";
    arrayOfString[16] = "artworkUrl";
    arrayOfString[17] = "Nid";
    arrayOfString[18] = "StoreAlbumId";
    arrayOfString[19] = "ArtistMetajamId";
  }

  SearchHeaderItemsCursor(Context paramContext, String[] paramArrayOfString, String paramString, long paramLong)
  {
    super(paramContext, paramArrayOfString, localCursor);
    this.mContext = paramContext;
    this.mHeaderId = paramLong;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    int i = XdiUtils.getDefaultItemWidthPx(paramContext);
    this.mImageWidth = i;
    int j = XdiUtils.getDefaultItemHeightPx(paramContext);
    this.mImageHeight = j;
    int k = XdiUtils.getDefaultArtistItemWidthDp(paramContext);
    this.mArtistItemWidth = k;
    int m = XdiUtils.getDefaultArtistItemHeightDp(paramContext);
    this.mArtistItemHeight = m;
  }

  private void extractDataForAlbum(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    int i = localCursor.getInt(1);
    String str1 = localCursor.getString(16);
    String str2;
    if (i == 7)
    {
      str2 = localCursor.getString(18);
      if (TextUtils.isEmpty(str1))
        str1 = XdiUtils.getDefaultAlbumArtUri(this.mContext);
    }
    while (true)
    {
      String str3 = localCursor.getString(3);
      String str4 = localCursor.getString(5);
      Uri localUri1 = XdiContentProvider.BASE_URI;
      String str5 = "details/albums/" + str2;
      Uri localUri2 = Uri.withAppendedPath(localUri1, str5);
      Intent localIntent = new Intent("com.google.android.xdi.action.DETAIL", localUri2);
      ProjectionMap localProjectionMap1 = this.mProjectionMap;
      Integer localInteger1 = Integer.valueOf(localCursor.getPosition() + 1);
      localProjectionMap1.writeValueToArray(paramArrayOfObject, "_id", localInteger1);
      ProjectionMap localProjectionMap2 = this.mProjectionMap;
      Integer localInteger2 = Integer.valueOf(1);
      localProjectionMap2.writeValueToArray(paramArrayOfObject, "parent_id", localInteger2);
      this.mProjectionMap.writeValueToArray(paramArrayOfObject, "display_name", str3);
      this.mProjectionMap.writeValueToArray(paramArrayOfObject, "display_description", str4);
      this.mProjectionMap.writeValueToArray(paramArrayOfObject, "image_uri", str1);
      this.mProjectionMap.writeValueToArray(paramArrayOfObject, "width", null);
      this.mProjectionMap.writeValueToArray(paramArrayOfObject, "height", null);
      ProjectionMap localProjectionMap3 = this.mProjectionMap;
      String str6 = localIntent.toUri(1);
      localProjectionMap3.writeValueToArray(paramArrayOfObject, "intent_uri", str6);
      this.mProjectionMap.writeValueToArray(paramArrayOfObject, "music_album", str3);
      this.mProjectionMap.writeValueToArray(paramArrayOfObject, "music_albumArtist", str4);
      ProjectionMap localProjectionMap4 = this.mProjectionMap;
      Integer localInteger3 = Integer.valueOf(0);
      localProjectionMap4.writeValueToArray(paramArrayOfObject, "music_trackCount", localInteger3);
      return;
      long l = localCursor.getLong(12);
      str2 = String.valueOf(l);
      if (TextUtils.isEmpty(str1))
      {
        int j = this.mImageWidth;
        int k = this.mImageHeight;
        str1 = MusicContent.AlbumArt.getAlbumArtUri(l, true, j, k).toString();
      }
    }
  }

  private void extractDataForArtist(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    int i = localCursor.getInt(1);
    String str1 = localCursor.getString(16);
    if (TextUtils.isEmpty(str1))
      str1 = XdiUtils.getDefaultArtistArtUri(this.mContext);
    int j = 0;
    int k = 0;
    if (i == 6);
    for (String str2 = localCursor.getString(19); ; str2 = String.valueOf(localCursor.getLong(6)))
    {
      String str3 = localCursor.getString(3);
      Uri localUri1 = XdiContentProvider.BASE_URI;
      String str4 = "details/artists/" + str2;
      Uri localUri2 = Uri.withAppendedPath(localUri1, str4);
      Intent localIntent = new Intent("com.google.android.xdi.action.DETAIL", localUri2);
      Resources localResources = this.mContext.getResources();
      Object[] arrayOfObject1 = new Object[1];
      Integer localInteger1 = Integer.valueOf(j);
      arrayOfObject1[0] = localInteger1;
      String str5 = localResources.getQuantityString(2131623938, j, arrayOfObject1);
      ProjectionMap localProjectionMap1 = this.mProjectionMap;
      Integer localInteger2 = Integer.valueOf(localCursor.getPosition() + 1);
      Object[] arrayOfObject2 = paramArrayOfObject;
      localProjectionMap1.writeValueToArray(arrayOfObject2, "_id", localInteger2);
      ProjectionMap localProjectionMap2 = this.mProjectionMap;
      Integer localInteger3 = Integer.valueOf(3);
      Object[] arrayOfObject3 = paramArrayOfObject;
      localProjectionMap2.writeValueToArray(arrayOfObject3, "parent_id", localInteger3);
      ProjectionMap localProjectionMap3 = this.mProjectionMap;
      Object[] arrayOfObject4 = paramArrayOfObject;
      localProjectionMap3.writeValueToArray(arrayOfObject4, "display_name", str3);
      ProjectionMap localProjectionMap4 = this.mProjectionMap;
      Object[] arrayOfObject5 = paramArrayOfObject;
      localProjectionMap4.writeValueToArray(arrayOfObject5, "display_description", str5);
      ProjectionMap localProjectionMap5 = this.mProjectionMap;
      Object[] arrayOfObject6 = paramArrayOfObject;
      localProjectionMap5.writeValueToArray(arrayOfObject6, "image_uri", str1);
      ProjectionMap localProjectionMap6 = this.mProjectionMap;
      Integer localInteger4 = Integer.valueOf(this.mArtistItemWidth);
      Object[] arrayOfObject7 = paramArrayOfObject;
      localProjectionMap6.writeValueToArray(arrayOfObject7, "width", localInteger4);
      ProjectionMap localProjectionMap7 = this.mProjectionMap;
      Integer localInteger5 = Integer.valueOf(this.mArtistItemHeight);
      Object[] arrayOfObject8 = paramArrayOfObject;
      localProjectionMap7.writeValueToArray(arrayOfObject8, "height", localInteger5);
      ProjectionMap localProjectionMap8 = this.mProjectionMap;
      String str6 = localIntent.toUri(1);
      Object[] arrayOfObject9 = paramArrayOfObject;
      localProjectionMap8.writeValueToArray(arrayOfObject9, "intent_uri", str6);
      ProjectionMap localProjectionMap9 = this.mProjectionMap;
      Object[] arrayOfObject10 = paramArrayOfObject;
      localProjectionMap9.writeValueToArray(arrayOfObject10, "music_album", str3);
      ProjectionMap localProjectionMap10 = this.mProjectionMap;
      String str7 = localCursor.getString(5);
      Object[] arrayOfObject11 = paramArrayOfObject;
      localProjectionMap10.writeValueToArray(arrayOfObject11, "music_albumArtist", str7);
      ProjectionMap localProjectionMap11 = this.mProjectionMap;
      Integer localInteger6 = Integer.valueOf(j);
      Object[] arrayOfObject12 = paramArrayOfObject;
      localProjectionMap11.writeValueToArray(arrayOfObject12, "music_albumCount", localInteger6);
      ProjectionMap localProjectionMap12 = this.mProjectionMap;
      Integer localInteger7 = Integer.valueOf(k);
      Object[] arrayOfObject13 = paramArrayOfObject;
      localProjectionMap12.writeValueToArray(arrayOfObject13, "music_trackCount", localInteger7);
      return;
    }
  }

  private void extractDataForPlaylist(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    long l = localCursor.getLong(0);
    String str1 = localCursor.getString(3);
    String str2 = MusicContent.PlaylistArt.getPlaylistArtUri(l, -1, -1).toString();
    Uri localUri1 = XdiContentProvider.BASE_URI;
    String str3 = "details/playlists/" + l;
    Uri localUri2 = Uri.withAppendedPath(localUri1, str3);
    Intent localIntent = new Intent("com.google.android.xdi.action.DETAIL", localUri2);
    Resources localResources = this.mContext.getResources();
    Object[] arrayOfObject = new Object[1];
    Integer localInteger1 = Integer.valueOf(0);
    arrayOfObject[0] = localInteger1;
    String str4 = localResources.getQuantityString(2131623940, 0, arrayOfObject);
    ProjectionMap localProjectionMap1 = this.mProjectionMap;
    Integer localInteger2 = Integer.valueOf(localCursor.getPosition() + 1);
    localProjectionMap1.writeValueToArray(paramArrayOfObject, "_id", localInteger2);
    ProjectionMap localProjectionMap2 = this.mProjectionMap;
    Integer localInteger3 = Integer.valueOf(4);
    localProjectionMap2.writeValueToArray(paramArrayOfObject, "parent_id", localInteger3);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "display_name", str1);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "display_description", str4);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "image_uri", str2);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "width", null);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "height", null);
    ProjectionMap localProjectionMap3 = this.mProjectionMap;
    String str5 = localIntent.toUri(1);
    localProjectionMap3.writeValueToArray(paramArrayOfObject, "intent_uri", str5);
    ProjectionMap localProjectionMap4 = this.mProjectionMap;
    Integer localInteger4 = Integer.valueOf(0);
    localProjectionMap4.writeValueToArray(paramArrayOfObject, "music_trackCount", localInteger4);
  }

  private void extractDataForTrack(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    int i = localCursor.getInt(1);
    String str1 = localCursor.getString(16);
    if (TextUtils.isEmpty(str1))
      str1 = XdiUtils.getDefaultArtUri(this.mContext);
    String str2 = localCursor.getString(3);
    String str3 = localCursor.getString(4);
    String str4 = localCursor.getString(5);
    String str5 = localCursor.getString(7);
    Intent localIntent1 = XdiUtils.newXdiPlayIntent();
    Resources localResources = this.mContext.getResources();
    if (i == 8)
    {
      String str6 = localCursor.getString(17);
      Intent localIntent2 = localIntent1.putExtra("container", 7);
      Intent localIntent3 = localIntent1.putExtra("id_string", str6);
    }
    while (true)
    {
      Intent localIntent4 = localIntent1.putExtra("name", str2);
      Intent localIntent5 = localIntent1.putExtra("art_uri", str1);
      ProjectionMap localProjectionMap1 = this.mProjectionMap;
      Integer localInteger1 = Integer.valueOf(localCursor.getPosition() + 1);
      Object[] arrayOfObject1 = paramArrayOfObject;
      localProjectionMap1.writeValueToArray(arrayOfObject1, "_id", localInteger1);
      ProjectionMap localProjectionMap2 = this.mProjectionMap;
      Integer localInteger2 = Integer.valueOf(2);
      Object[] arrayOfObject2 = paramArrayOfObject;
      localProjectionMap2.writeValueToArray(arrayOfObject2, "parent_id", localInteger2);
      ProjectionMap localProjectionMap3 = this.mProjectionMap;
      Object[] arrayOfObject3 = paramArrayOfObject;
      localProjectionMap3.writeValueToArray(arrayOfObject3, "display_name", str2);
      ProjectionMap localProjectionMap4 = this.mProjectionMap;
      Object[] arrayOfObject4 = paramArrayOfObject;
      localProjectionMap4.writeValueToArray(arrayOfObject4, "display_description", str3);
      ProjectionMap localProjectionMap5 = this.mProjectionMap;
      Object[] arrayOfObject5 = paramArrayOfObject;
      localProjectionMap5.writeValueToArray(arrayOfObject5, "image_uri", str1);
      ProjectionMap localProjectionMap6 = this.mProjectionMap;
      Object[] arrayOfObject6 = paramArrayOfObject;
      localProjectionMap6.writeValueToArray(arrayOfObject6, "width", null);
      ProjectionMap localProjectionMap7 = this.mProjectionMap;
      Object[] arrayOfObject7 = paramArrayOfObject;
      localProjectionMap7.writeValueToArray(arrayOfObject7, "height", null);
      ProjectionMap localProjectionMap8 = this.mProjectionMap;
      String str7 = localIntent1.toUri(1);
      Object[] arrayOfObject8 = paramArrayOfObject;
      localProjectionMap8.writeValueToArray(arrayOfObject8, "intent_uri", str7);
      ProjectionMap localProjectionMap9 = this.mProjectionMap;
      Object[] arrayOfObject9 = paramArrayOfObject;
      localProjectionMap9.writeValueToArray(arrayOfObject9, "music_album", str3);
      ProjectionMap localProjectionMap10 = this.mProjectionMap;
      Object[] arrayOfObject10 = paramArrayOfObject;
      localProjectionMap10.writeValueToArray(arrayOfObject10, "music_trackname", str2);
      ProjectionMap localProjectionMap11 = this.mProjectionMap;
      Object[] arrayOfObject11 = paramArrayOfObject;
      localProjectionMap11.writeValueToArray(arrayOfObject11, "music_albumArtist", str4);
      ProjectionMap localProjectionMap12 = this.mProjectionMap;
      Object[] arrayOfObject12 = paramArrayOfObject;
      localProjectionMap12.writeValueToArray(arrayOfObject12, "music_trackArtist", str5);
      ProjectionMap localProjectionMap13 = this.mProjectionMap;
      Long localLong = Long.valueOf(localCursor.getLong(11));
      Object[] arrayOfObject13 = paramArrayOfObject;
      localProjectionMap13.writeValueToArray(arrayOfObject13, "music_duration", localLong);
      return;
      long l = localCursor.getLong(0);
      String str8 = String.valueOf(l);
      Intent localIntent6 = localIntent1.putExtra("container", 6);
      Intent localIntent7 = localIntent1.putExtra("id", l);
    }
  }

  private static Cursor getCursorForQuery(Context paramContext, String paramString, long paramLong)
  {
    Cursor localCursor = null;
    String str2;
    switch ((int)paramLong)
    {
    default:
      String str1 = "Unexpected header id: " + paramLong;
      Log.wtf("MusicXdi", str1);
      return localCursor;
    case 1:
      str2 = "album";
    case 3:
    case 2:
    case 4:
    }
    while (true)
    {
      Uri localUri = MusicContent.Search.getSearchUri(paramString, str2);
      String[] arrayOfString1 = PROJECTION_SEARCH;
      Context localContext = paramContext;
      String[] arrayOfString2 = null;
      String str3 = null;
      localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str3);
      break;
      str2 = "artist";
      continue;
      str2 = "track";
      continue;
      str2 = "playlist";
    }
  }

  protected boolean extractDataForCurrentRow(Object[] paramArrayOfObject)
  {
    boolean bool;
    switch ((int)this.mHeaderId)
    {
    default:
      StringBuilder localStringBuilder = new StringBuilder().append("Unexpected header id: ");
      long l = this.mHeaderId;
      String str = l;
      Log.wtf("MusicXdi", str);
      bool = false;
      return bool;
    case 1:
      extractDataForAlbum(paramArrayOfObject);
    case 3:
    case 2:
    case 4:
    }
    while (true)
    {
      bool = true;
      break;
      extractDataForArtist(paramArrayOfObject);
      continue;
      extractDataForTrack(paramArrayOfObject);
      continue;
      extractDataForPlaylist(paramArrayOfObject);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.SearchHeaderItemsCursor
 * JD-Core Version:    0.6.2
 */