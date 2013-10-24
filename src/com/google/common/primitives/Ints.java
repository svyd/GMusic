package com.google.common.primitives;

public final class Ints
{
  public static int saturatedCast(long paramLong)
  {
    int i;
    if (paramLong > 2147483647L)
      i = 2147483647;
    while (true)
    {
      return i;
      if (paramLong < -2147483648L)
        i = -2147483648;
      else
        i = (int)paramLong;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.primitives.Ints
 * JD-Core Version:    0.6.2
 */