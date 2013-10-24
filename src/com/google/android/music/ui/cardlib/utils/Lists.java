package com.google.android.music.ui.cardlib.utils;

import java.util.ArrayList;

public class Lists
{
  public static <T> ArrayList<T> newArrayList()
  {
    return new ArrayList();
  }

  public static <T> ArrayList<T> newArrayList(int paramInt)
  {
    return new ArrayList(paramInt);
  }

  public static <T> ArrayList<T> newArrayList(T[] paramArrayOfT)
  {
    int i = paramArrayOfT.length;
    ArrayList localArrayList = new ArrayList(i);
    T[] arrayOfT = paramArrayOfT;
    int j = arrayOfT.length;
    int k = 0;
    while (k < j)
    {
      T ? = arrayOfT[k];
      boolean bool = localArrayList.add(?);
      k += 1;
    }
    return localArrayList;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.utils.Lists
 * JD-Core Version:    0.6.2
 */