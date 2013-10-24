package com.google.api.client.http;

import com.google.api.client.escape.CharEscapers;
import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.GenericData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public final class UrlEncodedParser
{
  public String contentType = "application/x-www-form-urlencoded";

  public static void parse(String paramString, Object paramObject)
  {
    Map localMap = null;
    Class localClass = paramObject.getClass();
    ClassInfo localClassInfo = ClassInfo.of(localClass);
    GenericData localGenericData;
    int i;
    int j;
    int k;
    label59: int m;
    if (GenericData.class.isAssignableFrom(localClass))
    {
      localGenericData = (GenericData)paramObject;
      if (Map.class.isAssignableFrom(localClass))
        localMap = (Map)paramObject;
      i = paramString.length();
      j = paramString.indexOf('=');
      k = 0;
      if (k >= i)
        return;
      m = paramString.indexOf('&', k);
      if (m != -1)
        break label383;
    }
    label159: label294: label314: label367: label383: for (int n = i; ; n = m)
    {
      Object localObject1;
      Object localObject2;
      Object localObject3;
      FieldInfo localFieldInfo;
      if ((j != -1) && (j < n))
      {
        String str1 = paramString.substring(k, j);
        int i1 = j + 1;
        String str2 = CharEscapers.decodeUri(paramString.substring(i1, n));
        int i2 = n + 1;
        j = paramString.indexOf('=', i2);
        String str3 = str2;
        localObject1 = str1;
        localObject2 = str3;
        localObject3 = CharEscapers.decodeUri((String)localObject1);
        localFieldInfo = localClassInfo.getFieldInfo((String)localObject3);
        if (localFieldInfo == null)
          break label314;
        localObject3 = localFieldInfo.type;
        if (!Collection.class.isAssignableFrom((Class)localObject3))
          break label294;
        localObject4 = (Collection)localFieldInfo.getValue(paramObject);
        if (localObject4 == null)
        {
          localObject4 = ClassInfo.newCollectionInstance((Class)localObject3);
          localFieldInfo.setValue(paramObject, localObject4);
        }
        Object localObject5 = FieldInfo.parsePrimitiveValue(ClassInfo.getCollectionParameter(localFieldInfo.field), (String)localObject2);
        boolean bool1 = ((Collection)localObject4).add(localObject5);
      }
      while (true)
      {
        k = n + 1;
        break label59;
        localGenericData = null;
        break;
        String str4 = paramString.substring(k, n);
        String str5 = "";
        localObject1 = str4;
        localObject2 = str5;
        break label159;
        Object localObject6 = FieldInfo.parsePrimitiveValue((Class)localObject3, (String)localObject2);
        localFieldInfo.setValue(paramObject, localObject6);
      }
      Object localObject4 = (ArrayList)localMap.get(localObject3);
      if (localObject4 == null)
      {
        localObject4 = new ArrayList();
        if (localGenericData == null)
          break label367;
        localGenericData.set((String)localObject3, localObject4);
      }
      while (true)
      {
        boolean bool2 = ((ArrayList)localObject4).add(localObject2);
        break;
        Object localObject7 = localMap.put(localObject3, localObject4);
      }
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.http.UrlEncodedParser
 * JD-Core Version:    0.6.2
 */