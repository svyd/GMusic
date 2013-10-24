package com.google.common.primitives;

import com.google.common.base.Preconditions;

public final class Longs
{
  public static String join(String paramString, long[] paramArrayOfLong)
  {
    Object localObject = Preconditions.checkNotNull(paramString);
    if (paramArrayOfLong.length == 0);
    StringBuilder localStringBuilder1;
    for (String str = ""; ; str = localStringBuilder1.toString())
    {
      return str;
      int i = paramArrayOfLong.length * 10;
      localStringBuilder1 = new StringBuilder(i);
      long l1 = paramArrayOfLong[0];
      StringBuilder localStringBuilder2 = localStringBuilder1.append(l1);
      int j = 1;
      while (true)
      {
        int k = paramArrayOfLong.length;
        if (j >= k)
          break;
        StringBuilder localStringBuilder3 = localStringBuilder1.append(paramString);
        long l2 = paramArrayOfLong[j];
        StringBuilder localStringBuilder4 = localStringBuilder3.append(l2);
        j += 1;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.primitives.Longs
 * JD-Core Version:    0.6.2
 */