package com.google.android.music.xdi;

import android.content.Context;
import android.content.res.Resources;
import android.database.MatrixCursor;

class MetaTitleCursor extends MatrixCursor
{
  MetaTitleCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    super(paramArrayOfString);
    extractDataForTitle(paramContext, paramArrayOfString, paramString);
  }

  private void extractDataForTitle(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    Object[] arrayOfObject = new Object[paramArrayOfString.length];
    localProjectionMap.writeValueToArray(arrayOfObject, "activity_title", paramString);
    localProjectionMap.writeValueToArray(arrayOfObject, "background_image_uri", null);
    String str = XdiUtils.getDefaultBadgeUri(paramContext);
    localProjectionMap.writeValueToArray(arrayOfObject, "badge_uri", str);
    Integer localInteger = Integer.valueOf(paramContext.getResources().getColor(2131427398));
    localProjectionMap.writeValueToArray(arrayOfObject, "color_hint", localInteger);
    addRow(arrayOfObject);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.MetaTitleCursor
 * JD-Core Version:    0.6.2
 */