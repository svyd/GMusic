package com.google.android.music.xdi;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import com.google.android.music.store.MusicContent.Explore;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;

class ExploreGenreHeadersCursor extends MatrixCursor
{
  private final int mItemHeight;
  private final int mItemWidth;
  private final ProjectionMap mProjectionMap;

  ExploreGenreHeadersCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    super(paramArrayOfString);
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    int i = XdiUtils.getDefaultItemWidthDp(paramContext);
    this.mItemWidth = i;
    int j = XdiUtils.getDefaultItemHeightDp(paramContext);
    this.mItemHeight = j;
    extractSubgenres(paramContext, paramString);
    extractFeatured(paramContext, paramString);
    extractNewReleases(paramContext, paramString);
  }

  void extractFeatured(Context paramContext, String paramString)
  {
    Uri localUri1 = MusicContent.Explore.getTopChartGroupsUri(paramString);
    String[] arrayOfString = MusicProjections.PROJECTION_ENTITY_GROUPS;
    Cursor localCursor = MusicUtils.query(paramContext, localUri1, arrayOfString, null, null, null);
    if (localCursor == null)
      return;
    Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
    try
    {
      if (localCursor.moveToNext())
      {
        long l = localCursor.getLong(0);
        int i = localCursor.getInt(4);
        String str1 = localCursor.getString(1);
        String str2 = localCursor.getString(2);
        StringBuilder localStringBuilder1 = new StringBuilder();
        Uri localUri2 = XdiContentProvider.BASE_URI;
        StringBuilder localStringBuilder2 = localStringBuilder1.append(localUri2).append("/explore/genre/featured/items/").append(l).append("/").append(i).append("/");
        String str3 = paramString;
        Uri localUri3 = Uri.parse(str3);
        ProjectionMap localProjectionMap1 = this.mProjectionMap;
        Integer localInteger1 = Integer.valueOf(getCount());
        localProjectionMap1.writeValueToArray(arrayOfObject, "_id", localInteger1);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "name", str1);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "display_name", str1);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "background_image_uri", null);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "bg_image_uri", null);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "icon_uri", null);
        ProjectionMap localProjectionMap2 = this.mProjectionMap;
        Integer localInteger2 = Integer.valueOf(this.mItemWidth);
        localProjectionMap2.writeValueToArray(arrayOfObject, "default_item_width", localInteger2);
        ProjectionMap localProjectionMap3 = this.mProjectionMap;
        Integer localInteger3 = Integer.valueOf(this.mItemHeight);
        localProjectionMap3.writeValueToArray(arrayOfObject, "default_item_height", localInteger3);
        ProjectionMap localProjectionMap4 = this.mProjectionMap;
        Integer localInteger4 = Integer.valueOf(paramContext.getResources().getColor(2131427398));
        localProjectionMap4.writeValueToArray(arrayOfObject, "color_hint", localInteger4);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "badge_uri", null);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "items_uri", localUri3);
        addRow(arrayOfObject);
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  void extractNewReleases(Context paramContext, String paramString)
  {
    Uri localUri1 = MusicContent.Explore.getNewReleaseGroupsUri(paramString);
    String[] arrayOfString = MusicProjections.PROJECTION_ENTITY_GROUPS;
    Cursor localCursor = MusicUtils.query(paramContext, localUri1, arrayOfString, null, null, null);
    if (localCursor == null)
      return;
    Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
    try
    {
      if (localCursor.moveToNext())
      {
        long l = localCursor.getLong(0);
        int i = localCursor.getInt(4);
        String str1 = localCursor.getString(1);
        String str2 = localCursor.getString(2);
        StringBuilder localStringBuilder1 = new StringBuilder();
        Uri localUri2 = XdiContentProvider.BASE_URI;
        StringBuilder localStringBuilder2 = localStringBuilder1.append(localUri2).append("/explore/genre/newreleases/items/").append(l).append("/").append(i).append("/");
        String str3 = paramString;
        Uri localUri3 = Uri.parse(str3);
        ProjectionMap localProjectionMap1 = this.mProjectionMap;
        Integer localInteger1 = Integer.valueOf(getCount());
        localProjectionMap1.writeValueToArray(arrayOfObject, "_id", localInteger1);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "name", str1);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "display_name", str1);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "background_image_uri", null);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "bg_image_uri", null);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "icon_uri", null);
        ProjectionMap localProjectionMap2 = this.mProjectionMap;
        Integer localInteger2 = Integer.valueOf(this.mItemWidth);
        localProjectionMap2.writeValueToArray(arrayOfObject, "default_item_width", localInteger2);
        ProjectionMap localProjectionMap3 = this.mProjectionMap;
        Integer localInteger3 = Integer.valueOf(this.mItemHeight);
        localProjectionMap3.writeValueToArray(arrayOfObject, "default_item_height", localInteger3);
        ProjectionMap localProjectionMap4 = this.mProjectionMap;
        Integer localInteger4 = Integer.valueOf(paramContext.getResources().getColor(2131427398));
        localProjectionMap4.writeValueToArray(arrayOfObject, "color_hint", localInteger4);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "badge_uri", null);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "items_uri", localUri3);
        addRow(arrayOfObject);
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  void extractSubgenres(Context paramContext, String paramString)
  {
    if (!XdiUtils.hasSubgenres(paramContext, paramString))
      return;
    Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
    String str1 = paramContext.getString(2131230859);
    StringBuilder localStringBuilder = new StringBuilder();
    Uri localUri1 = XdiContentProvider.BASE_URI;
    Uri localUri2 = Uri.parse(localUri1 + "/explore/genre/items/" + paramString);
    ProjectionMap localProjectionMap1 = this.mProjectionMap;
    Integer localInteger1 = Integer.valueOf(getCount());
    localProjectionMap1.writeValueToArray(arrayOfObject, "_id", localInteger1);
    this.mProjectionMap.writeValueToArray(arrayOfObject, "name", str1);
    this.mProjectionMap.writeValueToArray(arrayOfObject, "display_name", str1);
    this.mProjectionMap.writeValueToArray(arrayOfObject, "background_image_uri", null);
    this.mProjectionMap.writeValueToArray(arrayOfObject, "bg_image_uri", null);
    this.mProjectionMap.writeValueToArray(arrayOfObject, "icon_uri", null);
    ProjectionMap localProjectionMap2 = this.mProjectionMap;
    Integer localInteger2 = Integer.valueOf(this.mItemWidth);
    localProjectionMap2.writeValueToArray(arrayOfObject, "default_item_width", localInteger2);
    ProjectionMap localProjectionMap3 = this.mProjectionMap;
    Integer localInteger3 = Integer.valueOf(this.mItemHeight);
    localProjectionMap3.writeValueToArray(arrayOfObject, "default_item_height", localInteger3);
    ProjectionMap localProjectionMap4 = this.mProjectionMap;
    Integer localInteger4 = Integer.valueOf(paramContext.getResources().getColor(2131427398));
    localProjectionMap4.writeValueToArray(arrayOfObject, "color_hint", localInteger4);
    ProjectionMap localProjectionMap5 = this.mProjectionMap;
    String str2 = XdiUtils.getDefaultBadgeUri(paramContext);
    localProjectionMap5.writeValueToArray(arrayOfObject, "badge_uri", str2);
    this.mProjectionMap.writeValueToArray(arrayOfObject, "items_uri", localUri2);
    addRow(arrayOfObject);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.ExploreGenreHeadersCursor
 * JD-Core Version:    0.6.2
 */