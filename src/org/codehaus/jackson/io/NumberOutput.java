package org.codehaus.jackson.io;

public final class NumberOutput
{
  private static int BILLION;
  static final char[] FULL_TRIPLETS;
  static final char[] LEADING_TRIPLETS;
  private static long MAX_INT_AS_LONG;
  private static int MILLION = 1000000;
  private static long MIN_INT_AS_LONG;
  static final String SMALLEST_LONG;
  private static long TEN_BILLION_L;
  private static long THOUSAND_L;
  static final String[] sSmallIntStrs;
  static final String[] sSmallIntStrs2 = arrayOfString2;

  static
  {
    BILLION = 1000000000;
    TEN_BILLION_L = 10000000000L;
    THOUSAND_L = 1000L;
    MIN_INT_AS_LONG = -2147483648L;
    MAX_INT_AS_LONG = 2147483647L;
    SMALLEST_LONG = String.valueOf(-9223372036854775808L);
    LEADING_TRIPLETS = new char[4000];
    FULL_TRIPLETS = new char[4000];
    int i = 0;
    int j = 0;
    while (j < 10)
    {
      int k = (char)(j + 48);
      int m;
      int n;
      if (j == 0)
      {
        m = 0;
        n = 0;
      }
      while (true)
      {
        if (n >= 10)
          break label243;
        int i1 = (char)(n + 48);
        if ((j == 0) && (n == 0));
        for (int i2 = 0; ; i2 = i1)
        {
          int i3 = 0;
          while (i3 < 10)
          {
            int i4 = (char)(i3 + 48);
            LEADING_TRIPLETS[i] = m;
            char[] arrayOfChar1 = LEADING_TRIPLETS;
            int i5 = i + 1;
            arrayOfChar1[i5] = i2;
            char[] arrayOfChar2 = LEADING_TRIPLETS;
            int i6 = i + 2;
            arrayOfChar2[i6] = i4;
            FULL_TRIPLETS[i] = k;
            char[] arrayOfChar3 = FULL_TRIPLETS;
            int i7 = i + 1;
            arrayOfChar3[i7] = i1;
            char[] arrayOfChar4 = FULL_TRIPLETS;
            int i8 = i + 2;
            arrayOfChar4[i8] = i4;
            i += 4;
            i3 += 1;
          }
          m = k;
          break;
        }
        n += 1;
      }
      label243: j += 1;
    }
    String[] arrayOfString1 = new String[11];
    arrayOfString1[0] = "0";
    arrayOfString1[1] = "1";
    arrayOfString1[2] = "2";
    arrayOfString1[3] = "3";
    arrayOfString1[4] = "4";
    arrayOfString1[5] = "5";
    arrayOfString1[6] = "6";
    arrayOfString1[7] = "7";
    arrayOfString1[8] = "8";
    arrayOfString1[9] = "9";
    arrayOfString1[10] = "10";
    sSmallIntStrs = arrayOfString1;
    String[] arrayOfString2 = new String[10];
    arrayOfString2[0] = "-1";
    arrayOfString2[1] = "-2";
    arrayOfString2[2] = "-3";
    arrayOfString2[3] = "-4";
    arrayOfString2[4] = "-5";
    arrayOfString2[5] = "-6";
    arrayOfString2[6] = "-7";
    arrayOfString2[7] = "-8";
    arrayOfString2[8] = "-9";
    arrayOfString2[9] = "-10";
  }

  private static int calcLongStrLength(long paramLong)
  {
    int i = 10;
    long l2;
    long l3;
    for (long l1 = TEN_BILLION_L; ; l1 = l2 + l3)
    {
      if ((paramLong < l1) || (i == 19))
        return i;
      i += 1;
      l2 = l1 << 3;
      l3 = l1 << 1;
    }
  }

  private static int outputFullTriplet(int paramInt1, char[] paramArrayOfChar, int paramInt2)
  {
    int i = paramInt1 << 2;
    int j = paramInt2 + 1;
    char[] arrayOfChar1 = FULL_TRIPLETS;
    int k = i + 1;
    int m = arrayOfChar1[i];
    paramArrayOfChar[paramInt2] = m;
    int n = j + 1;
    char[] arrayOfChar2 = FULL_TRIPLETS;
    int i1 = k + 1;
    int i2 = arrayOfChar2[k];
    paramArrayOfChar[j] = i2;
    int i3 = n + 1;
    int i4 = FULL_TRIPLETS[i1];
    paramArrayOfChar[n] = i4;
    return i3;
  }

  public static int outputInt(int paramInt1, char[] paramArrayOfChar, int paramInt2)
  {
    int i;
    if (paramInt1 < 0)
    {
      if (paramInt1 == -2147483648)
      {
        i = outputLong(paramInt1, paramArrayOfChar, paramInt2);
        return i;
      }
      int j = paramInt2 + 1;
      paramArrayOfChar[paramInt2] = '-';
      paramInt1 = -paramInt1;
      paramInt2 = j;
    }
    int k = MILLION;
    if (paramInt1 < k)
    {
      if (paramInt1 < 1000)
        if (paramInt1 < 10)
        {
          int m = paramInt2 + 1;
          int n = (char)(paramInt1 + 48);
          paramArrayOfChar[paramInt2] = n;
          paramInt2 = m;
        }
      while (true)
      {
        i = paramInt2;
        break;
        paramInt2 = outputLeadingTriplet(paramInt1, paramArrayOfChar, paramInt2);
        continue;
        int i1 = paramInt1 / 1000;
        int i2 = i1 * 1000;
        int i3 = paramInt1 - i2;
        int i4 = outputLeadingTriplet(i1, paramArrayOfChar, paramInt2);
        paramInt2 = outputFullTriplet(i3, paramArrayOfChar, i4);
      }
    }
    int i5 = BILLION;
    int i6;
    label151: label200: int i13;
    int i15;
    int i17;
    if (paramInt1 >= i5)
    {
      i6 = 1;
      if (i6 != 0)
      {
        int i7 = BILLION;
        paramInt1 -= i7;
        int i8 = BILLION;
        if (paramInt1 < i8)
          break label290;
        int i9 = BILLION;
        paramInt1 -= i9;
        int i10 = paramInt2 + 1;
        paramArrayOfChar[paramInt2] = '2';
        paramInt2 = i10;
      }
      int i11 = paramInt1 / 1000;
      int i12 = i11 * 1000;
      i13 = paramInt1 - i12;
      int i14 = i11;
      i15 = i11 / 1000;
      int i16 = i15 * 1000;
      i17 = i14 - i16;
      if (i6 == 0)
        break label306;
    }
    label290: label306: for (int i18 = outputFullTriplet(i15, paramArrayOfChar, paramInt2); ; i18 = outputLeadingTriplet(i15, paramArrayOfChar, paramInt2))
    {
      int i19 = outputFullTriplet(i17, paramArrayOfChar, i18);
      i = outputFullTriplet(i13, paramArrayOfChar, i19);
      break;
      i6 = 0;
      break label151;
      int i20 = paramInt2 + 1;
      paramArrayOfChar[paramInt2] = '1';
      paramInt2 = i20;
      break label200;
    }
  }

  private static int outputLeadingTriplet(int paramInt1, char[] paramArrayOfChar, int paramInt2)
  {
    int i = paramInt1 << 2;
    char[] arrayOfChar1 = LEADING_TRIPLETS;
    int j = i + 1;
    int k = arrayOfChar1[i];
    if (k != null)
    {
      int m = paramInt2 + 1;
      paramArrayOfChar[paramInt2] = k;
      paramInt2 = m;
    }
    char[] arrayOfChar2 = LEADING_TRIPLETS;
    int n = j + 1;
    int i1 = arrayOfChar2[j];
    if (i1 != null)
    {
      int i2 = paramInt2 + 1;
      paramArrayOfChar[paramInt2] = i1;
      paramInt2 = i2;
    }
    int i3 = paramInt2 + 1;
    int i4 = LEADING_TRIPLETS[n];
    paramArrayOfChar[paramInt2] = i4;
    return i3;
  }

  public static int outputLong(long paramLong, char[] paramArrayOfChar, int paramInt)
  {
    int i;
    if (paramLong < 0L)
    {
      long l1 = MIN_INT_AS_LONG;
      if (paramLong > l1)
        i = outputInt((int)paramLong, paramArrayOfChar, paramInt);
    }
    while (true)
    {
      return i;
      if (paramLong == -9223372036854775808L)
      {
        int j = SMALLEST_LONG.length();
        SMALLEST_LONG.getChars(0, j, paramArrayOfChar, paramInt);
        i = paramInt + j;
      }
      else
      {
        int k = paramInt + 1;
        paramArrayOfChar[paramInt] = '-';
        paramLong = -paramLong;
        paramInt = k;
        int m;
        int i1;
        long l7;
        do
        {
          m = paramInt;
          int n = calcLongStrLength(paramLong);
          paramInt += n;
          i1 = paramInt;
          while (true)
          {
            long l2 = MAX_INT_AS_LONG;
            if (paramLong <= l2)
              break;
            i1 += -3;
            long l3 = THOUSAND_L;
            long l4 = paramLong / l3;
            long l5 = THOUSAND_L * l4;
            int i2 = outputFullTriplet((int)(paramLong - l5), paramArrayOfChar, i1);
            long l6 = l4;
          }
          l7 = MAX_INT_AS_LONG;
        }
        while (paramLong > l7);
        i = outputInt((int)paramLong, paramArrayOfChar, paramInt);
        continue;
        int i4;
        for (int i3 = (int)paramLong; i3 >= 1000; i3 = i4)
        {
          i1 += -3;
          i4 = i3 / 1000;
          int i5 = i4 * 1000;
          int i6 = outputFullTriplet(i3 - i5, paramArrayOfChar, i1);
        }
        int i7 = outputLeadingTriplet(i3, paramArrayOfChar, m);
        i = paramInt;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.io.NumberOutput
 * JD-Core Version:    0.6.2
 */