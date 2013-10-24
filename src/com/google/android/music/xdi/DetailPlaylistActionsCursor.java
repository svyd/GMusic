package com.google.android.music.xdi;

import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;

class DetailPlaylistActionsCursor extends MatrixCursor
{
  private final Context mContext;
  private final ProjectionMap mProjectionMap;

  DetailPlaylistActionsCursor(Context paramContext, String[] paramArrayOfString, long paramLong)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    addRowsForActions(paramLong);
  }

  private void addRowsForActions(long paramLong)
  {
    Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
    Intent localIntent1 = XdiUtils.newXdiPlayIntent();
    Intent localIntent2 = localIntent1.putExtra("container", 2);
    Intent localIntent3 = localIntent1.putExtra("id", paramLong);
    ProjectionMap localProjectionMap1 = this.mProjectionMap;
    Integer localInteger = Integer.valueOf(1);
    localProjectionMap1.writeValueToArray(arrayOfObject, "_id", localInteger);
    ProjectionMap localProjectionMap2 = this.mProjectionMap;
    String str1 = this.mContext.getString(2131231390);
    localProjectionMap2.writeValueToArray(arrayOfObject, "display_name", str1);
    this.mProjectionMap.writeValueToArray(arrayOfObject, "display_subname", null);
    ProjectionMap localProjectionMap3 = this.mProjectionMap;
    String str2 = localIntent1.toUri(1);
    localProjectionMap3.writeValueToArray(arrayOfObject, "intent_uri", str2);
    addRow(arrayOfObject);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.DetailPlaylistActionsCursor
 * JD-Core Version:    0.6.2
 */