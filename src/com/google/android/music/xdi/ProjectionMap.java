package com.google.android.music.xdi;

import com.google.common.collect.Maps;
import java.util.LinkedHashMap;

public class ProjectionMap
{
  private final LinkedHashMap<String, Integer> mMap;
  private final String[] mProjection;

  public ProjectionMap(String[] paramArrayOfString)
  {
    LinkedHashMap localLinkedHashMap1 = Maps.newLinkedHashMap();
    this.mMap = localLinkedHashMap1;
    this.mProjection = paramArrayOfString;
    int i = 0;
    while (true)
    {
      int j = paramArrayOfString.length;
      if (i >= j)
        return;
      LinkedHashMap localLinkedHashMap2 = this.mMap;
      String str = paramArrayOfString[i];
      Integer localInteger = Integer.valueOf(i);
      Object localObject = localLinkedHashMap2.put(str, localInteger);
      i += 1;
    }
  }

  public String[] getColumnNames()
  {
    return this.mProjection;
  }

  public int size()
  {
    return this.mMap.size();
  }

  public void writeValueToArray(Object[] paramArrayOfObject, String paramString, Object paramObject)
  {
    Integer localInteger = (Integer)this.mMap.get(paramString);
    if (localInteger == null)
      return;
    int i = localInteger.intValue();
    paramArrayOfObject[i] = paramObject;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.ProjectionMap
 * JD-Core Version:    0.6.2
 */