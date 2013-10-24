package org.codehaus.jackson.io;

public final class NumberInput
{
  static final String MAX_LONG_STR = String.valueOf(9223372036854775807L);
  static final String MIN_LONG_STR_NO_SIGN = String.valueOf(-9223372036854775808L).substring(1);

  public static final boolean inLongRange(char[] paramArrayOfChar, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    boolean bool = true;
    String str;
    int i;
    if (paramBoolean)
    {
      str = MIN_LONG_STR_NO_SIGN;
      i = str.length();
      if (paramInt2 >= i)
        break label36;
    }
    label36: label99: 
    while (true)
    {
      return bool;
      str = MAX_LONG_STR;
      break;
      if (paramInt2 > i)
      {
        bool = false;
      }
      else
      {
        int j = 0;
        while (true)
        {
          if (j >= i)
            break label99;
          int k = paramInt1 + j;
          int m = paramArrayOfChar[k];
          int n = str.charAt(j);
          if (m > n)
          {
            bool = false;
            break;
          }
          j += 1;
        }
      }
    }
  }

  public static final int parseInt(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = paramArrayOfChar[paramInt1] + '\0*0';
    int j = paramInt2 + paramInt1;
    int k = paramInt1 + 1;
    if (k < j)
    {
      int m = i * 10;
      int n = paramArrayOfChar[k] + '\0*0';
      i = m + n;
      k += 1;
      if (k < j)
      {
        int i1 = i * 10;
        int i2 = paramArrayOfChar[k] + '\0*0';
        i = i1 + i2;
        k += 1;
        if (k < j)
        {
          int i3 = i * 10;
          int i4 = paramArrayOfChar[k] + '\0*0';
          i = i3 + i4;
          k += 1;
          if (k < j)
          {
            int i5 = i * 10;
            int i6 = paramArrayOfChar[k] + '\0*0';
            i = i5 + i6;
            k += 1;
            if (k < j)
            {
              int i7 = i * 10;
              int i8 = paramArrayOfChar[k] + '\0*0';
              i = i7 + i8;
              k += 1;
              if (k < j)
              {
                int i9 = i * 10;
                int i10 = paramArrayOfChar[k] + '\0*0';
                i = i9 + i10;
                k += 1;
                if (k < j)
                {
                  int i11 = i * 10;
                  int i12 = paramArrayOfChar[k] + '\0*0';
                  i = i11 + i12;
                  k += 1;
                  if (k < j)
                  {
                    int i13 = i * 10;
                    int i14 = paramArrayOfChar[k] + '\0*0';
                    i = i13 + i14;
                  }
                }
              }
            }
          }
        }
      }
    }
    return i;
  }

  public static final long parseLong(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = paramInt2 + -9;
    long l = parseInt(paramArrayOfChar, paramInt1, i) * 1000000000L;
    int j = paramInt1 + i;
    return parseInt(paramArrayOfChar, j, 9) + l;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.io.NumberInput
 * JD-Core Version:    0.6.2
 */