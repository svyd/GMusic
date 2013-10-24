package com.google.android.music.xdi;

import android.content.Context;
import android.content.res.Resources;
import android.database.MatrixCursor;
import com.google.common.collect.Maps;
import java.util.HashMap;

class MetaIdCursor extends MatrixCursor
{
  private static final HashMap<Long, Integer> sMetaIdMap = Maps.newHashMap();

  static
  {
    HashMap localHashMap1 = sMetaIdMap;
    Long localLong1 = Long.valueOf(0L);
    Integer localInteger1 = Integer.valueOf(2131230929);
    Object localObject1 = localHashMap1.put(localLong1, localInteger1);
    HashMap localHashMap2 = sMetaIdMap;
    Long localLong2 = Long.valueOf(1L);
    Integer localInteger2 = Integer.valueOf(2131230841);
    Object localObject2 = localHashMap2.put(localLong2, localInteger2);
    HashMap localHashMap3 = sMetaIdMap;
    Long localLong3 = Long.valueOf(2L);
    Integer localInteger3 = Integer.valueOf(2131230850);
    Object localObject3 = localHashMap3.put(localLong3, localInteger3);
    HashMap localHashMap4 = sMetaIdMap;
    Long localLong4 = Long.valueOf(3L);
    Integer localInteger4 = Integer.valueOf(2131230845);
    Object localObject4 = localHashMap4.put(localLong4, localInteger4);
    HashMap localHashMap5 = sMetaIdMap;
    Long localLong5 = Long.valueOf(4L);
    Integer localInteger5 = Integer.valueOf(2131230857);
    Object localObject5 = localHashMap5.put(localLong5, localInteger5);
    HashMap localHashMap6 = sMetaIdMap;
    Long localLong6 = Long.valueOf(5L);
    Integer localInteger6 = Integer.valueOf(2131230855);
    Object localObject6 = localHashMap6.put(localLong6, localInteger6);
    HashMap localHashMap7 = sMetaIdMap;
    Long localLong7 = Long.valueOf(6L);
    Integer localInteger7 = Integer.valueOf(2131230856);
    Object localObject7 = localHashMap7.put(localLong7, localInteger7);
    HashMap localHashMap8 = sMetaIdMap;
    Long localLong8 = Long.valueOf(7L);
    Integer localInteger8 = Integer.valueOf(2131230858);
    Object localObject8 = localHashMap8.put(localLong8, localInteger8);
  }

  MetaIdCursor(Context paramContext, String[] paramArrayOfString, long paramLong)
  {
    super(paramArrayOfString);
    extractDataForMetaId(paramContext, paramArrayOfString, paramLong);
  }

  private void extractDataForMetaId(Context paramContext, String[] paramArrayOfString, long paramLong)
  {
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    Object[] arrayOfObject = new Object[paramArrayOfString.length];
    HashMap localHashMap = sMetaIdMap;
    Long localLong = Long.valueOf(paramLong);
    Integer localInteger1 = (Integer)localHashMap.get(localLong);
    if (localInteger1 != null)
    {
      int i = localInteger1.intValue();
      String str1 = paramContext.getString(i);
      localProjectionMap.writeValueToArray(arrayOfObject, "activity_title", str1);
    }
    localProjectionMap.writeValueToArray(arrayOfObject, "background_image_uri", null);
    String str2 = XdiUtils.getDefaultBadgeUri(paramContext);
    localProjectionMap.writeValueToArray(arrayOfObject, "badge_uri", str2);
    Integer localInteger2 = Integer.valueOf(paramContext.getResources().getColor(2131427398));
    localProjectionMap.writeValueToArray(arrayOfObject, "color_hint", localInteger2);
    addRow(arrayOfObject);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.MetaIdCursor
 * JD-Core Version:    0.6.2
 */