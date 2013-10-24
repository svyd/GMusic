package com.google.android.music.xdi;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import com.google.android.music.store.MusicContent.Explore;
import com.google.android.music.utils.MusicUtils;

class ExploreRecommendationsHeadersCursor extends XdiCursor
{
  private final int mItemHeight;
  private final int mItemWidth;
  private final ProjectionMap mProjectionMap;

  ExploreRecommendationsHeadersCursor(Context paramContext, String[] paramArrayOfString)
  {
    super(paramContext, paramArrayOfString, localCursor);
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    int i = XdiUtils.getDefaultItemWidthDp(paramContext);
    this.mItemWidth = i;
    int j = XdiUtils.getDefaultItemHeightDp(paramContext);
    this.mItemHeight = j;
  }

  private static Cursor getCursorForHeader(Context paramContext)
  {
    Uri localUri = MusicContent.Explore.getRecommendedGroupsUri();
    String[] arrayOfString1 = MusicProjections.PROJECTION_ENTITY_GROUPS;
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str = null;
    return MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str);
  }

  protected boolean extractDataForCurrentRow(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    Context localContext = getContext();
    long l = localCursor.getLong(0);
    String str1 = localCursor.getString(1);
    int i = localCursor.getInt(4);
    StringBuilder localStringBuilder = new StringBuilder();
    Uri localUri1 = XdiContentProvider.BASE_URI;
    Uri localUri2 = Uri.parse(localUri1 + "/explore/recommendations/" + l + "/" + i);
    ProjectionMap localProjectionMap1 = this.mProjectionMap;
    Integer localInteger1 = Integer.valueOf(localCursor.getPosition());
    localProjectionMap1.writeValueToArray(paramArrayOfObject, "_id", localInteger1);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "name", str1);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "display_name", str1);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "background_image_uri", null);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "bg_image_uri", null);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "icon_uri", null);
    ProjectionMap localProjectionMap2 = this.mProjectionMap;
    Integer localInteger2 = Integer.valueOf(this.mItemWidth);
    localProjectionMap2.writeValueToArray(paramArrayOfObject, "default_item_width", localInteger2);
    ProjectionMap localProjectionMap3 = this.mProjectionMap;
    Integer localInteger3 = Integer.valueOf(this.mItemHeight);
    localProjectionMap3.writeValueToArray(paramArrayOfObject, "default_item_height", localInteger3);
    ProjectionMap localProjectionMap4 = this.mProjectionMap;
    Integer localInteger4 = Integer.valueOf(localContext.getResources().getColor(2131427398));
    localProjectionMap4.writeValueToArray(paramArrayOfObject, "color_hint", localInteger4);
    ProjectionMap localProjectionMap5 = this.mProjectionMap;
    String str2 = XdiUtils.getDefaultBadgeUri(localContext);
    localProjectionMap5.writeValueToArray(paramArrayOfObject, "badge_uri", str2);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "items_uri", localUri2);
    return true;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.ExploreRecommendationsHeadersCursor
 * JD-Core Version:    0.6.2
 */