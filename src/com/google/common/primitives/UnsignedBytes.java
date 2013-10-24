package com.google.common.primitives;

import java.util.Comparator;

public final class UnsignedBytes
{
  public static int compare(byte paramByte1, byte paramByte2)
  {
    int i = toInt(paramByte1);
    int j = toInt(paramByte2);
    return i - j;
  }

  static Comparator<byte[]> lexicographicalComparatorJavaImpl()
  {
    return UnsignedBytes.LexicographicalComparatorHolder.PureJavaComparator.INSTANCE;
  }

  public static int toInt(byte paramByte)
  {
    return paramByte & 0xFF;
  }

  static class LexicographicalComparatorHolder
  {
    static final Comparator<byte[]> BEST_COMPARATOR = UnsignedBytes.lexicographicalComparatorJavaImpl();
    static final String UNSAFE_COMPARATOR_NAME;

    static
    {
      StringBuilder localStringBuilder = new StringBuilder();
      String str = LexicographicalComparatorHolder.class.getName();
      UNSAFE_COMPARATOR_NAME = str + "$UnsafeComparator";
    }

    static enum PureJavaComparator
      implements Comparator<byte[]>
    {
      static
      {
        PureJavaComparator[] arrayOfPureJavaComparator = new PureJavaComparator[1];
        PureJavaComparator localPureJavaComparator = INSTANCE;
        arrayOfPureJavaComparator[0] = localPureJavaComparator;
      }

      public int compare(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
      {
        int i = paramArrayOfByte1.length;
        int j = paramArrayOfByte2.length;
        int k = Math.min(i, j);
        int m = 0;
        int n;
        if (m < k)
        {
          byte b1 = paramArrayOfByte1[m];
          byte b2 = paramArrayOfByte2[m];
          n = UnsignedBytes.compare(b1, b2);
          if (n == 0);
        }
        while (true)
        {
          return n;
          m += 1;
          break;
          int i1 = paramArrayOfByte1.length;
          int i2 = paramArrayOfByte2.length;
          n = i1 - i2;
        }
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.primitives.UnsignedBytes
 * JD-Core Version:    0.6.2
 */