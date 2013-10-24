package com.google.android.music.xdi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import com.google.android.music.store.MusicContent.AlbumArt;
import com.google.android.music.store.MusicContent.Genres;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;

class BrowseMyGenresHeaderCursor extends MatrixCursor
{
  private static final String[] PROJECTION_ALBUMS = arrayOfString;
  private final Context mContext;
  private final int mImageHeight;
  private final int mImageWidth;
  private final ProjectionMap mProjectionMap;

  static
  {
    String[] arrayOfString = new String[3];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "album_name";
    arrayOfString[2] = "album_artist";
  }

  BrowseMyGenresHeaderCursor(Context paramContext, String[] paramArrayOfString, long paramLong)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    int i = XdiUtils.getDefaultItemWidthPx(paramContext);
    this.mImageWidth = i;
    int j = XdiUtils.getDefaultItemHeightPx(paramContext);
    this.mImageHeight = j;
    addRowsForGenre(paramLong);
  }

  private void addRowsForGenre(long paramLong)
  {
    Context localContext = this.mContext;
    Uri localUri1 = MusicContent.Genres.getAlbumsOfGenreUri(paramLong);
    String[] arrayOfString = PROJECTION_ALBUMS;
    Cursor localCursor = MusicUtils.query(localContext, localUri1, arrayOfString, null, null, null);
    if (localCursor == null)
      return;
    try
    {
      if (localCursor.moveToNext())
      {
        long l = localCursor.getLong(0);
        String str1 = localCursor.getString(1);
        String str2 = localCursor.getString(2);
        int i = this.mImageWidth;
        int j = this.mImageHeight;
        Uri localUri2 = MusicContent.AlbumArt.getAlbumArtUri(l, true, i, j);
        Uri localUri3 = XdiContentProvider.BASE_URI;
        String str3 = "details/albums/" + l;
        Uri localUri4 = Uri.withAppendedPath(localUri3, str3);
        Intent localIntent = new Intent("com.google.android.xdi.action.DETAIL", localUri4);
        Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
        ProjectionMap localProjectionMap1 = this.mProjectionMap;
        Long localLong1 = Long.valueOf(l);
        localProjectionMap1.writeValueToArray(arrayOfObject, "_id", localLong1);
        ProjectionMap localProjectionMap2 = this.mProjectionMap;
        Long localLong2 = Long.valueOf(paramLong);
        localProjectionMap2.writeValueToArray(arrayOfObject, "parent_id", localLong2);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "display_name", str1);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "display_description", str2);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "image_uri", localUri2);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "width", null);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "height", null);
        ProjectionMap localProjectionMap3 = this.mProjectionMap;
        String str4 = localIntent.toUri(1);
        localProjectionMap3.writeValueToArray(arrayOfObject, "intent_uri", str4);
        addRow(arrayOfObject);
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.BrowseMyGenresHeaderCursor
 * JD-Core Version:    0.6.2
 */