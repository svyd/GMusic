package com.google.common.collect;

final class Hashing
{
  static int smear(int paramInt)
  {
    int i = paramInt >>> 20;
    int j = paramInt >>> 12;
    int k = i ^ j;
    int m = paramInt ^ k;
    int n = m >>> 7 ^ m;
    int i1 = m >>> 4;
    return n ^ i1;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Hashing
 * JD-Core Version:    0.6.2
 */