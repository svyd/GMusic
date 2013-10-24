package com.google.android.music.xdi;

import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;

class LauncherRootCursor extends MatrixCursor
{
  private final Context mContext;
  private final ProjectionMap mProjectionMap;

  LauncherRootCursor(Context paramContext, String[] paramArrayOfString)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    int i = 0;
    while (true)
    {
      int j = XdiConstants.LAUNCHER_CLUSTER_NAMES.length;
      if (i >= j)
        return;
      Object[] arrayOfObject = getColumnValuesForLauncherRoot(i);
      addRow(arrayOfObject);
      i += 1;
    }
  }

  private Object[] getColumnValuesForLauncherRoot(int paramInt)
  {
    long l = paramInt + 1;
    Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
    Context localContext = this.mContext;
    int i = XdiConstants.LAUNCHER_CLUSTER_NAMES[paramInt];
    String str1 = localContext.getString(i);
    Uri localUri1 = XdiContentProvider.BASE_URI.buildUpon().appendPath("browse").build();
    int j = XdiConstants.LAUNCHER_CLUSTER_BROWSE_INDEX[paramInt];
    Intent localIntent1 = XdiContract.getBrowseIntent(localUri1, j);
    String str2 = XdiUtils.getMetaUri(0L).toString();
    Intent localIntent2 = localIntent1.putExtra("meta_uri", str2);
    ProjectionMap localProjectionMap1 = this.mProjectionMap;
    Long localLong1 = Long.valueOf(l);
    localProjectionMap1.writeValueToArray(arrayOfObject, "_id", localLong1);
    this.mProjectionMap.writeValueToArray(arrayOfObject, "name", str1);
    ProjectionMap localProjectionMap2 = this.mProjectionMap;
    Long localLong2 = Long.valueOf(10L - l);
    localProjectionMap2.writeValueToArray(arrayOfObject, "importance", localLong2);
    this.mProjectionMap.writeValueToArray(arrayOfObject, "display_name", str1);
    ProjectionMap localProjectionMap3 = this.mProjectionMap;
    Integer localInteger1 = Integer.valueOf(XdiConstants.LAUNCHER_CLUSTER_VISIBLE_COUNT[paramInt]);
    localProjectionMap3.writeValueToArray(arrayOfObject, "visible_count", localInteger1);
    ProjectionMap localProjectionMap4 = this.mProjectionMap;
    Integer localInteger2 = Integer.valueOf(86400000);
    localProjectionMap4.writeValueToArray(arrayOfObject, "cache_time_ms", localInteger2);
    ProjectionMap localProjectionMap5 = this.mProjectionMap;
    Integer localInteger3 = Integer.valueOf(0);
    localProjectionMap5.writeValueToArray(arrayOfObject, "image_crop_allowed", localInteger3);
    if (XdiConstants.LAUNCHER_CLUSTER_USE_BROWSE_INTENT[paramInt] != null)
    {
      ProjectionMap localProjectionMap6 = this.mProjectionMap;
      StringBuilder localStringBuilder = new StringBuilder().append("content://com.google.android.music.xdi/browse/");
      int k = XdiConstants.LAUNCHER_CLUSTER_HEADER_ROOT_ID[paramInt];
      Uri localUri2 = Uri.parse(k);
      localProjectionMap6.writeValueToArray(arrayOfObject, "browse_items_uri", localUri2);
    }
    while (true)
    {
      ProjectionMap localProjectionMap7 = this.mProjectionMap;
      String str3 = localIntent1.toUri(1);
      localProjectionMap7.writeValueToArray(arrayOfObject, "intent_uri", str3);
      this.mProjectionMap.writeValueToArray(arrayOfObject, "notification_text", null);
      return arrayOfObject;
      this.mProjectionMap.writeValueToArray(arrayOfObject, "browse_items_uri", null);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.LauncherRootCursor
 * JD-Core Version:    0.6.2
 */