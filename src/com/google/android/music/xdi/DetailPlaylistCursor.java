package com.google.android.music.xdi;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.music.store.MusicContent.PlaylistArt;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;

class DetailPlaylistCursor extends MatrixCursor
{
  private static final String[] PROJECTION_PLAYLISTS = arrayOfString;
  private final Context mContext;
  private final int mImageHeight;
  private final int mImageWidth;
  private final ProjectionMap mProjectionMap;

  static
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "playlist_name";
  }

  DetailPlaylistCursor(Context paramContext, String[] paramArrayOfString, long paramLong)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    int i = XdiUtils.getDefaultItemWidthPx(paramContext);
    this.mImageWidth = i;
    int j = XdiUtils.getDefaultItemHeightPx(paramContext);
    this.mImageHeight = j;
    addRowForPlaylist(paramLong);
  }

  private void addRowForPlaylist(long paramLong)
  {
    Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
    Context localContext = this.mContext;
    Uri.Builder localBuilder = MusicContent.Playlists.CONTENT_URI.buildUpon();
    String str1 = Long.toString(paramLong);
    Uri localUri1 = localBuilder.appendPath(str1).build();
    String[] arrayOfString1 = PROJECTION_PLAYLISTS;
    String[] arrayOfString2 = null;
    String str2 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri1, arrayOfString1, null, arrayOfString2, str2);
    if (localCursor == null)
      return;
    try
    {
      if (localCursor.moveToNext())
      {
        String str3 = localCursor.getString(1);
        int i = this.mImageWidth;
        int j = this.mImageHeight;
        Uri localUri2 = MusicContent.PlaylistArt.getPlaylistArtUri(paramLong, i, j);
        ProjectionMap localProjectionMap1 = this.mProjectionMap;
        Long localLong = Long.valueOf(paramLong);
        localProjectionMap1.writeValueToArray(arrayOfObject, "_id", localLong);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "foreground_image_uri", localUri2);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "background_image_uri", localUri2);
        ProjectionMap localProjectionMap2 = this.mProjectionMap;
        String str4 = XdiUtils.getDefaultBadgeUri(this.mContext);
        localProjectionMap2.writeValueToArray(arrayOfObject, "badge_uri", str4);
        ProjectionMap localProjectionMap3 = this.mProjectionMap;
        Integer localInteger = Integer.valueOf(this.mContext.getResources().getColor(2131427398));
        localProjectionMap3.writeValueToArray(arrayOfObject, "color_hint", localInteger);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "text_color_hint", null);
        addRow(arrayOfObject);
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
 * Qualified Name:     com.google.android.music.xdi.DetailPlaylistCursor
 * JD-Core Version:    0.6.2
 */