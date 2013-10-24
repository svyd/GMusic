package com.google.android.music.xdi;

import android.content.Context;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;

class SearchHeadersCursor extends MatrixCursor
{
  private final Context mContext;
  private final int mItemHeight;
  private final int mItemWidth;
  private final ProjectionMap mProjectionMap;

  SearchHeadersCursor(Context paramContext, String[] paramArrayOfString, String paramString)
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
      int m = XdiConstants.SEARCH_HEADER_NAMES.length;
      if (k >= m)
        return;
      Object[] arrayOfObject = getColumnValuesForBrowseHeader(k, paramString);
      addRow(arrayOfObject);
      k += 1;
    }
  }

  private Object[] getColumnValuesForBrowseHeader(int paramInt, String paramString)
  {
    Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
    Context localContext = this.mContext;
    int i = XdiConstants.SEARCH_HEADER_NAMES[paramInt];
    String str1 = localContext.getString(i);
    int j = MusicXdiContract.SEARCH_HEADER_INDEX[paramInt];
    Uri localUri1 = XdiContentProvider.BASE_URI;
    String str2 = "search/headers/" + j;
    Uri localUri2 = Uri.withAppendedPath(localUri1, str2).buildUpon().appendQueryParameter("q", paramString).build();
    ProjectionMap localProjectionMap1 = this.mProjectionMap;
    Integer localInteger1 = Integer.valueOf(j);
    localProjectionMap1.writeValueToArray(arrayOfObject, "_id", localInteger1);
    this.mProjectionMap.writeValueToArray(arrayOfObject, "display_name", str1);
    this.mProjectionMap.writeValueToArray(arrayOfObject, "results_uri", localUri2);
    ProjectionMap localProjectionMap2 = this.mProjectionMap;
    Integer localInteger2 = Integer.valueOf(this.mItemWidth);
    localProjectionMap2.writeValueToArray(arrayOfObject, "default_item_width", localInteger2);
    ProjectionMap localProjectionMap3 = this.mProjectionMap;
    Integer localInteger3 = Integer.valueOf(this.mItemHeight);
    localProjectionMap3.writeValueToArray(arrayOfObject, "default_item_height", localInteger3);
    return arrayOfObject;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.SearchHeadersCursor
 * JD-Core Version:    0.6.2
 */