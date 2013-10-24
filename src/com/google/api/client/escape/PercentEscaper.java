package com.google.api.client.escape;

public class PercentEscaper extends UnicodeEscaper
{
  private static final char[] UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
  private static final char[] URI_ESCAPED_SPACE;
  private final boolean plusForSpace;
  private final boolean[] safeOctets;

  static
  {
    char[] arrayOfChar = new char[1];
    arrayOfChar[0] = '+';
    URI_ESCAPED_SPACE = arrayOfChar;
  }

  public PercentEscaper(String paramString, boolean paramBoolean)
  {
    if (paramString.matches(".*[0-9A-Za-z].*"))
      throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified");
    if ((paramBoolean) && (paramString.contains(" ")))
      throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character");
    if (paramString.contains("%"))
      throw new IllegalArgumentException("The '%' character cannot be specified as 'safe'");
    this.plusForSpace = paramBoolean;
    boolean[] arrayOfBoolean = createSafeOctets(paramString);
    this.safeOctets = arrayOfBoolean;
  }

  private static boolean[] createSafeOctets(String paramString)
  {
    int i = 122;
    char[] arrayOfChar1 = paramString.toCharArray();
    char[] arrayOfChar2 = arrayOfChar1;
    int j = arrayOfChar2.length;
    int k = 0;
    while (k < j)
    {
      i = Math.max(arrayOfChar2[k], i);
      k += 1;
    }
    boolean[] arrayOfBoolean = new boolean[i + 1];
    int m = 48;
    while (m <= 57)
    {
      arrayOfBoolean[m] = true;
      m += 1;
    }
    int n = 65;
    while (n <= 90)
    {
      arrayOfBoolean[n] = true;
      n += 1;
    }
    int i1 = 97;
    while (i1 <= 122)
    {
      arrayOfBoolean[i1] = true;
      i1 += 1;
    }
    char[] arrayOfChar3 = arrayOfChar1;
    int i2 = arrayOfChar3.length;
    int i3 = 0;
    while (i3 < i2)
    {
      int i4 = arrayOfChar3[i3];
      arrayOfBoolean[i4] = true;
      i3 += 1;
    }
    return arrayOfBoolean;
  }

  public String escape(String paramString)
  {
    int i = paramString.length();
    int j = 0;
    while (true)
    {
      if (j < i)
      {
        int k = paramString.charAt(j);
        int m = this.safeOctets.length;
        if ((k >= m) || (this.safeOctets[k] == null))
          paramString = escapeSlow(paramString, j);
      }
      else
      {
        return paramString;
      }
      j += 1;
    }
  }

  protected char[] escape(int paramInt)
  {
    int i = this.safeOctets.length;
    char[] arrayOfChar1;
    if ((paramInt < i) && (this.safeOctets[paramInt] != null))
      arrayOfChar1 = null;
    while (true)
    {
      return arrayOfChar1;
      if ((paramInt == 32) && (this.plusForSpace))
      {
        arrayOfChar1 = URI_ESCAPED_SPACE;
      }
      else if (paramInt <= 127)
      {
        arrayOfChar1 = new char[3];
        arrayOfChar1[0] = '%';
        char[] arrayOfChar2 = UPPER_HEX_DIGITS;
        int j = paramInt & 0xF;
        int k = arrayOfChar2[j];
        arrayOfChar1[2] = k;
        char[] arrayOfChar3 = UPPER_HEX_DIGITS;
        int m = paramInt >>> 4;
        int n = arrayOfChar3[m];
        arrayOfChar1[1] = n;
      }
      else if (paramInt <= 2047)
      {
        arrayOfChar1 = new char[6];
        arrayOfChar1[0] = '%';
        arrayOfChar1[3] = '%';
        char[] arrayOfChar4 = UPPER_HEX_DIGITS;
        int i1 = paramInt & 0xF;
        int i2 = arrayOfChar4[i1];
        arrayOfChar1[5] = i2;
        int i3 = paramInt >>> 4;
        char[] arrayOfChar5 = UPPER_HEX_DIGITS;
        int i4 = i3 & 0x3 | 0x8;
        int i5 = arrayOfChar5[i4];
        arrayOfChar1[4] = i5;
        int i6 = i3 >>> 2;
        char[] arrayOfChar6 = UPPER_HEX_DIGITS;
        int i7 = i6 & 0xF;
        int i8 = arrayOfChar6[i7];
        arrayOfChar1[2] = i8;
        int i9 = i6 >>> 4;
        char[] arrayOfChar7 = UPPER_HEX_DIGITS;
        int i10 = i9 | 0xC;
        int i11 = arrayOfChar7[i10];
        arrayOfChar1[1] = i11;
      }
      else if (paramInt <= 65535)
      {
        arrayOfChar1 = new char[9];
        arrayOfChar1[0] = '%';
        arrayOfChar1[1] = 'E';
        arrayOfChar1[3] = '%';
        arrayOfChar1[6] = '%';
        char[] arrayOfChar8 = UPPER_HEX_DIGITS;
        int i12 = paramInt & 0xF;
        int i13 = arrayOfChar8[i12];
        arrayOfChar1[8] = i13;
        int i14 = paramInt >>> 4;
        char[] arrayOfChar9 = UPPER_HEX_DIGITS;
        int i15 = i14 & 0x3 | 0x8;
        int i16 = arrayOfChar9[i15];
        arrayOfChar1[7] = i16;
        int i17 = i14 >>> 2;
        char[] arrayOfChar10 = UPPER_HEX_DIGITS;
        int i18 = i17 & 0xF;
        int i19 = arrayOfChar10[i18];
        arrayOfChar1[5] = i19;
        int i20 = i17 >>> 4;
        char[] arrayOfChar11 = UPPER_HEX_DIGITS;
        int i21 = i20 & 0x3 | 0x8;
        int i22 = arrayOfChar11[i21];
        arrayOfChar1[4] = i22;
        int i23 = i20 >>> 2;
        int i24 = UPPER_HEX_DIGITS[i23];
        arrayOfChar1[2] = i24;
      }
      else
      {
        if (paramInt > 1114111)
          break;
        arrayOfChar1 = new char[12];
        arrayOfChar1[0] = '%';
        arrayOfChar1[1] = 'F';
        arrayOfChar1[3] = '%';
        arrayOfChar1[6] = '%';
        arrayOfChar1[9] = '%';
        char[] arrayOfChar12 = UPPER_HEX_DIGITS;
        int i25 = paramInt & 0xF;
        int i26 = arrayOfChar12[i25];
        arrayOfChar1[11] = i26;
        int i27 = paramInt >>> 4;
        char[] arrayOfChar13 = UPPER_HEX_DIGITS;
        int i28 = i27 & 0x3 | 0x8;
        int i29 = arrayOfChar13[i28];
        arrayOfChar1[10] = i29;
        int i30 = i27 >>> 2;
        char[] arrayOfChar14 = UPPER_HEX_DIGITS;
        int i31 = i30 & 0xF;
        int i32 = arrayOfChar14[i31];
        arrayOfChar1[8] = i32;
        int i33 = i30 >>> 4;
        char[] arrayOfChar15 = UPPER_HEX_DIGITS;
        int i34 = i33 & 0x3 | 0x8;
        int i35 = arrayOfChar15[i34];
        arrayOfChar1[7] = i35;
        int i36 = i33 >>> 2;
        char[] arrayOfChar16 = UPPER_HEX_DIGITS;
        int i37 = i36 & 0xF;
        int i38 = arrayOfChar16[i37];
        arrayOfChar1[5] = i38;
        int i39 = i36 >>> 4;
        char[] arrayOfChar17 = UPPER_HEX_DIGITS;
        int i40 = i39 & 0x3 | 0x8;
        int i41 = arrayOfChar17[i40];
        arrayOfChar1[4] = i41;
        int i42 = i39 >>> 2;
        char[] arrayOfChar18 = UPPER_HEX_DIGITS;
        int i43 = i42 & 0x7;
        int i44 = arrayOfChar18[i43];
        arrayOfChar1[2] = i44;
      }
    }
    String str = "Invalid unicode character value " + paramInt;
    throw new IllegalArgumentException(str);
  }

  protected int nextEscapeIndex(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    while (true)
    {
      if (paramInt1 < paramInt2)
      {
        int i = paramCharSequence.charAt(paramInt1);
        int j = this.safeOctets.length;
        if ((i < j) && (this.safeOctets[i] != null));
      }
      else
      {
        return paramInt1;
      }
      paramInt1 += 1;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.escape.PercentEscaper
 * JD-Core Version:    0.6.2
 */