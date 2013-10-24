package com.google.android.music.xdi;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import com.google.android.music.store.MusicContent.Genres;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;

class BrowseAllMyGenresHeadersCursor extends MatrixCursor
{
  private static final String[] PROJECTION_GENRES = arrayOfString;
  private final Context mContext;
  private int mItemHeight;
  private int mItemWidth;
  private final ProjectionMap mProjectionMap;

  static
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "name";
  }

  BrowseAllMyGenresHeadersCursor(Context paramContext, String[] paramArrayOfString)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    int i = XdiUtils.getDefaultItemWidthDp(paramContext);
    this.mItemWidth = i;
    int j = XdiUtils.getDefaultItemHeightDp(paramContext);
    this.mItemHeight = j;
    addRowsForGenres();
  }

  private void addRowsForGenres()
  {
    Context localContext = this.mContext;
    Uri localUri = MusicContent.Genres.CONTENT_URI;
    String[] arrayOfString1 = PROJECTION_GENRES;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor == null)
      return;
    int i = 1;
    try
    {
      while (localCursor.moveToNext())
      {
        long l = localCursor.getLong(0);
        String str2 = localCursor.getString(1);
        Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
        ProjectionMap localProjectionMap1 = this.mProjectionMap;
        Long localLong = Long.valueOf(l);
        localProjectionMap1.writeValueToArray(arrayOfObject, "_id", localLong);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "name", str2);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "display_name", str2);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "background_image_uri", null);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "bg_image_uri", null);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "icon_uri", null);
        ProjectionMap localProjectionMap2 = this.mProjectionMap;
        Integer localInteger1 = Integer.valueOf(this.mItemWidth);
        localProjectionMap2.writeValueToArray(arrayOfObject, "default_item_width", localInteger1);
        ProjectionMap localProjectionMap3 = this.mProjectionMap;
        Integer localInteger2 = Integer.valueOf(this.mItemHeight);
        localProjectionMap3.writeValueToArray(arrayOfObject, "default_item_height", localInteger2);
        ProjectionMap localProjectionMap4 = this.mProjectionMap;
        Integer localInteger3 = Integer.valueOf(this.mContext.getResources().getColor(2131427398));
        localProjectionMap4.writeValueToArray(arrayOfObject, "color_hint", localInteger3);
        ProjectionMap localProjectionMap5 = this.mProjectionMap;
        String str3 = XdiUtils.getDefaultBadgeUri(this.mContext);
        localProjectionMap5.writeValueToArray(arrayOfObject, "badge_uri", str3);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "items_uri", null);
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
 * Qualified Name:     com.google.android.music.xdi.BrowseAllMyGenresHeadersCursor
 * JD-Core Version:    0.6.2
 */