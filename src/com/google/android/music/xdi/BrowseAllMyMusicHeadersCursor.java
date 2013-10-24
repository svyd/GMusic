package com.google.android.music.xdi;

import android.content.Context;
import android.content.res.Resources;
import android.database.MatrixCursor;

class BrowseAllMyMusicHeadersCursor extends MatrixCursor
{
  private final Context mContext;
  private final int mItemHeight;
  private final int mItemWidth;
  private final ProjectionMap mProjectionMap;

  BrowseAllMyMusicHeadersCursor(Context paramContext, String[] paramArrayOfString)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    int i = XdiUtils.getDefaultItemWidthDp(paramContext);
    this.mItemWidth = i;
    int j = XdiUtils.getDefaultItemHeightDp(paramContext);
    this.mItemHeight = j;
    int k = 0;
    while (true)
    {
      int m = XdiConstants.BROWSE_MY_MUSIC_HEADER_NAMES.length;
      if (k >= m)
        return;
      Object[] arrayOfObject = getColumnValuesForBrowseHeader(k);
      addRow(arrayOfObject);
      k += 1;
    }
  }

  private Object[] getColumnValuesForBrowseHeader(int paramInt)
  {
    Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
    Context localContext = this.mContext;
    int i = XdiConstants.BROWSE_MY_MUSIC_HEADER_NAMES[paramInt];
    String str1 = localContext.getString(i);
    long l = paramInt + 1;
    ProjectionMap localProjectionMap1 = this.mProjectionMap;
    Long localLong = Long.valueOf(l);
    localProjectionMap1.writeValueToArray(arrayOfObject, "_id", localLong);
    this.mProjectionMap.writeValueToArray(arrayOfObject, "name", str1);
    this.mProjectionMap.writeValueToArray(arrayOfObject, "display_name", str1);
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
    String str2 = XdiUtils.getDefaultBadgeUri(this.mContext);
    localProjectionMap5.writeValueToArray(arrayOfObject, "badge_uri", str2);
    this.mProjectionMap.writeValueToArray(arrayOfObject, "items_uri", null);
    return arrayOfObject;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.BrowseAllMyMusicHeadersCursor
 * JD-Core Version:    0.6.2
 */