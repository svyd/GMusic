package com.google.android.music.xdi;

import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import com.google.android.music.utils.MusicUtils;

class DetailAlbumActionsCursor extends MatrixCursor
{
  private final Context mContext;
  private final ProjectionMap mProjectionMap;

  DetailAlbumActionsCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    addRowsForActions(paramString);
  }

  private void addRowsForActions(String paramString)
  {
    Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
    boolean bool = MusicUtils.isNautilusId(paramString);
    Intent localIntent1 = XdiUtils.newXdiPlayIntent();
    if (bool)
    {
      Intent localIntent2 = localIntent1.putExtra("container", 5);
      Intent localIntent3 = localIntent1.putExtra("id_string", paramString);
    }
    while (true)
    {
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
      return;
      Intent localIntent4 = localIntent1.putExtra("container", 1);
      Long localLong = Long.valueOf(paramString);
      Intent localIntent5 = localIntent1.putExtra("id", localLong);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.DetailAlbumActionsCursor
 * JD-Core Version:    0.6.2
 */