package com.google.android.music.xdi;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import com.google.android.music.store.MusicContent.AlbumArt;
import com.google.android.music.store.MusicContent.Albums;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;

class LauncherClustersCursor extends MatrixCursor
{
  private static final String[] PROJECTION_ALBUMS = arrayOfString;
  private final Context mContext;
  private final int mImageHeight;
  private final int mImageWidth;
  private final ProjectionMap mProjectionMap;

  static
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "_id";
  }

  LauncherClustersCursor(Context paramContext, String[] paramArrayOfString)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    int i = XdiUtils.getDefaultItemWidthPx(paramContext);
    this.mImageWidth = i;
    int j = XdiUtils.getDefaultItemHeightPx(paramContext);
    this.mImageHeight = j;
    addRowsForMyMusic();
  }

  LauncherClustersCursor(Context paramContext, String[] paramArrayOfString, long paramLong)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    int i = XdiUtils.getDefaultItemWidthPx(paramContext);
    this.mImageWidth = i;
    int j = XdiUtils.getDefaultItemHeightPx(paramContext);
    this.mImageHeight = j;
  }

  private void addRowsForMyMusic()
  {
    Context localContext = this.mContext;
    Uri localUri1 = MusicContent.Albums.CONTENT_URI;
    String[] arrayOfString1 = PROJECTION_ALBUMS;
    String[] arrayOfString2 = null;
    String str = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri1, arrayOfString1, null, arrayOfString2, str);
    if (localCursor == null)
      return;
    int i = 1;
    try
    {
      int j = XdiConstants.LAUNCHER_CLUSTER_VISIBLE_COUNT[2];
      while ((localCursor.moveToNext()) && (i <= j))
      {
        long l = localCursor.getLong(0);
        int k = this.mImageWidth;
        int m = this.mImageHeight;
        Uri localUri2 = MusicContent.AlbumArt.getAlbumArtUri(l, true, k, m);
        Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
        ProjectionMap localProjectionMap1 = this.mProjectionMap;
        Long localLong = Long.valueOf(l);
        localProjectionMap1.writeValueToArray(arrayOfObject, "_id", localLong);
        ProjectionMap localProjectionMap2 = this.mProjectionMap;
        Integer localInteger = Integer.valueOf(2);
        localProjectionMap2.writeValueToArray(arrayOfObject, "parent_id", localInteger);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "image_uri", localUri2);
        addRow(arrayOfObject);
        i += 1;
      }
      return;
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.LauncherClustersCursor
 * JD-Core Version:    0.6.2
 */