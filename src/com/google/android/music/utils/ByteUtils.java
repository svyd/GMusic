package com.google.android.music.utils;

public class ByteUtils
{
  public static boolean bytesEqual(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
  {
    boolean bool1 = true;
    int i = 0;
    if (i < paramInt3)
    {
      int j = paramInt1 + i;
      int k = paramArrayOfByte1[j];
      int m = paramInt2 + i;
      int n = paramArrayOfByte2[m];
      if (k != n);
      for (boolean bool2 = true; ; bool2 = false)
      {
        bool1 &= bool2;
        i += 1;
        break;
      }
    }
    return bool1;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.ByteUtils
 * JD-Core Version:    0.6.2
 */