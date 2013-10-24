package com.google.common.collect;

import java.lang.reflect.Array;

class Platform
{
  static <T> T[] clone(T[] paramArrayOfT)
  {
    return (Object[])paramArrayOfT.clone();
  }

  static <T> T[] newArray(T[] paramArrayOfT, int paramInt)
  {
    return (Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), paramInt);
  }

  static MapMaker tryWeakKeys(MapMaker paramMapMaker)
  {
    return paramMapMaker.weakKeys();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Platform
 * JD-Core Version:    0.6.2
 */