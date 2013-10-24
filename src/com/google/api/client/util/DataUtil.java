package com.google.api.client.util;

import TT;;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DataUtil
{
  public static <T> T clone(T paramT)
  {
    if (FieldInfo.isPrimitive(paramT))
      return paramT;
    Object localObject;
    if ((paramT instanceof GenericData))
      localObject = ((GenericData)paramT).clone();
    while (true)
    {
      cloneInternal(paramT, localObject);
      paramT = (TT)localObject;
      break;
      if ((paramT instanceof ArrayMap))
        localObject = ((ArrayMap)paramT).clone();
      else
        localObject = ClassInfo.newInstance(paramT.getClass());
    }
  }

  static void cloneInternal(Object paramObject1, Object paramObject2)
  {
    Class localClass = paramObject1.getClass();
    if (Collection.class.isAssignableFrom(localClass))
    {
      paramObject1 = (Collection)paramObject1;
      if (ArrayList.class.isAssignableFrom(localClass))
      {
        ArrayList localArrayList = (ArrayList)paramObject2;
        int i = paramObject1.size();
        localArrayList.ensureCapacity(i);
      }
      paramObject2 = (Collection)paramObject2;
      Iterator localIterator1 = paramObject1.iterator();
      while (true)
      {
        if (!localIterator1.hasNext())
          return;
        Object localObject1 = clone(localIterator1.next());
        boolean bool1 = paramObject2.add(localObject1);
      }
    }
    boolean bool2 = GenericData.class.isAssignableFrom(localClass);
    Object localObject2;
    if ((bool2) || (!Map.class.isAssignableFrom(localClass)))
    {
      localObject2 = ClassInfo.of(localClass);
      Iterator localIterator3 = ((ClassInfo)localObject2).getKeyNames().iterator();
      while (true)
      {
        if (!localIterator3.hasNext())
          return;
        String str = (String)localIterator3.next();
        FieldInfo localFieldInfo = ((ClassInfo)localObject2).getFieldInfo(str);
        if ((!localFieldInfo.isFinal) && ((!bool2) || (!localFieldInfo.isPrimitive)))
        {
          Object localObject3 = localFieldInfo.getValue(paramObject1);
          if (localObject3 != null)
          {
            Object localObject4 = clone(localObject3);
            localFieldInfo.setValue(paramObject2, localObject4);
          }
        }
      }
    }
    if (ArrayMap.class.isAssignableFrom(localClass))
    {
      paramObject2 = (ArrayMap)paramObject2;
      paramObject1 = (ArrayMap)paramObject1;
      int k = paramObject1.size();
      int j = 0;
      while (true)
      {
        if (j >= k)
          return;
        localObject2 = paramObject1.getValue(j);
        if (!FieldInfo.isPrimitive(localObject2))
        {
          Object localObject5 = clone(localObject2);
          Object localObject6 = paramObject2.set(j, localObject5);
        }
        j += 1;
      }
    }
    paramObject2 = (Map)paramObject2;
    Iterator localIterator2 = ((Map)paramObject1).entrySet().iterator();
    while (true)
    {
      if (!localIterator2.hasNext())
        return;
      Map.Entry localEntry = (Map.Entry)localIterator2.next();
      Object localObject7 = localEntry.getKey();
      Object localObject8 = clone(localEntry.getValue());
      Object localObject9 = paramObject2.put(localObject7, localObject8);
    }
  }

  public static Map<String, Object> mapOf(Object paramObject)
  {
    Object localObject;
    if (paramObject == null)
      localObject = Collections.emptyMap();
    while (true)
    {
      return localObject;
      if ((paramObject instanceof Map))
        localObject = (Map)paramObject;
      else
        localObject = new ReflectionMap(paramObject);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.util.DataUtil
 * JD-Core Version:    0.6.2
 */