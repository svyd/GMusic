package com.google.android.music.utils;

import java.lang.reflect.Array;

public class ArrayUtils<T>
{
  public static <T> T[] combine(T[][] paramArrayOfT)
  {
    Object localObject = null;
    if ((paramArrayOfT == null) || (paramArrayOfT.length == 0));
    while (true)
    {
      return localObject;
      int i = 0;
      Class localClass = null;
      T[][] arrayOfT = paramArrayOfT;
      int j = arrayOfT.length;
      int k = 0;
      while (k < j)
      {
        T[] arrayOfT1 = arrayOfT[k];
        if (arrayOfT1 != null)
        {
          int m = arrayOfT1.length;
          i += m;
          localClass = arrayOfT1.getClass().getComponentType();
        }
        k += 1;
      }
      if ((i != 0) && (localClass != null))
      {
        localObject = (Object[])Array.newInstance(localClass, i);
        int n = 0;
        arrayOfT = paramArrayOfT;
        j = arrayOfT.length;
        k = 0;
        while (k < j)
        {
          T[] arrayOfT2 = arrayOfT[k];
          if (arrayOfT2 != null)
          {
            int i1 = arrayOfT2.length;
            System.arraycopy(arrayOfT2, 0, localObject, n, i1);
            int i2 = arrayOfT2.length;
            int i3 = n + i2;
          }
          k += 1;
        }
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.ArrayUtils
 * JD-Core Version:    0.6.2
 */