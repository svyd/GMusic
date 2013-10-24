package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;

public final class ObjectArrays
{
  private static Object[] fillArray(Iterable<?> paramIterable, Object[] paramArrayOfObject)
  {
    int i = 0;
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      int j = i + 1;
      paramArrayOfObject[i] = localObject;
      i = j;
    }
    return paramArrayOfObject;
  }

  public static <T> T[] newArray(T[] paramArrayOfT, int paramInt)
  {
    return Platform.newArray(paramArrayOfT, paramInt);
  }

  static Object[] toArrayImpl(Collection<?> paramCollection)
  {
    Object[] arrayOfObject = new Object[paramCollection.size()];
    return fillArray(paramCollection, arrayOfObject);
  }

  static <T> T[] toArrayImpl(Collection<?> paramCollection, T[] paramArrayOfT)
  {
    int i = paramCollection.size();
    if (paramArrayOfT.length < i)
      paramArrayOfT = newArray(paramArrayOfT, i);
    Object[] arrayOfObject = fillArray(paramCollection, paramArrayOfT);
    if (paramArrayOfT.length > i)
      paramArrayOfT[i] = null;
    return paramArrayOfT;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ObjectArrays
 * JD-Core Version:    0.6.2
 */