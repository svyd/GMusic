package org.codehaus.jackson.util;

import java.util.Arrays;

public final class CharTypes
{
  static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
  static final int[] sHexValues;
  static final int[] sInputCodes;
  static final int[] sInputCodesComment;
  static final int[] sInputCodesJsNames;
  static final int[] sInputCodesUtf8;
  static final int[] sInputCodesUtf8JsNames;
  static final int[] sOutputEscapes;

  static
  {
    int[] arrayOfInt1 = new int[256];
    int i = 0;
    while (i < 32)
    {
      arrayOfInt1[i] = -1;
      i += 1;
    }
    arrayOfInt1[34] = 1;
    arrayOfInt1[92] = 1;
    sInputCodes = arrayOfInt1;
    int[] arrayOfInt2 = new int[sInputCodes.length];
    int[] arrayOfInt3 = sInputCodes;
    int j = sInputCodes.length;
    System.arraycopy(arrayOfInt3, 0, arrayOfInt2, 0, j);
    int k = 128;
    if (k < 256)
    {
      int m;
      if ((k & 0xE0) == 192)
        m = 2;
      while (true)
      {
        arrayOfInt2[k] = m;
        k += 1;
        break;
        if ((k & 0xF0) == 224)
          m = 3;
        else if ((k & 0xF8) == 240)
          m = 4;
        else
          m = -1;
      }
    }
    sInputCodesUtf8 = arrayOfInt2;
    int[] arrayOfInt4 = new int[256];
    Arrays.fill(arrayOfInt4, -1);
    int n = 33;
    while (n < 256)
    {
      if (Character.isJavaIdentifierPart((char)n))
        arrayOfInt4[n] = 0;
      n += 1;
    }
    sInputCodesJsNames = arrayOfInt4;
    int[] arrayOfInt5 = new int[256];
    int[] arrayOfInt6 = sInputCodesJsNames;
    int i1 = sInputCodesJsNames.length;
    System.arraycopy(arrayOfInt6, 0, arrayOfInt5, 0, i1);
    Arrays.fill(arrayOfInt5, 128, 128, 0);
    sInputCodesUtf8JsNames = arrayOfInt5;
    sInputCodesComment = new int[256];
    int[] arrayOfInt7 = sInputCodesUtf8;
    int[] arrayOfInt8 = sInputCodesComment;
    System.arraycopy(arrayOfInt7, 128, arrayOfInt8, 128, 128);
    Arrays.fill(sInputCodesComment, 0, 32, -1);
    sInputCodesComment[9] = 0;
    sInputCodesComment[10] = 10;
    sInputCodesComment[13] = 13;
    sInputCodesComment[42] = 42;
    int[] arrayOfInt9 = new int[256];
    int i2 = 0;
    while (i2 < 32)
    {
      int i3 = -(i2 + 1);
      arrayOfInt9[i2] = i3;
      i2 += 1;
    }
    arrayOfInt9[34] = 34;
    arrayOfInt9[92] = 92;
    arrayOfInt9[8] = 98;
    arrayOfInt9[9] = 116;
    arrayOfInt9[12] = 102;
    arrayOfInt9[10] = 110;
    arrayOfInt9[13] = 114;
    sOutputEscapes = arrayOfInt9;
    sHexValues = new int[''];
    Arrays.fill(sHexValues, -1);
    int i4 = 0;
    while (i4 < 10)
    {
      int[] arrayOfInt10 = sHexValues;
      int i5 = i4 + 48;
      arrayOfInt10[i5] = i4;
      i4 += 1;
    }
    int i6 = 0;
    while (true)
    {
      if (i6 >= 6)
        return;
      int[] arrayOfInt11 = sHexValues;
      int i7 = i6 + 97;
      int i8 = i6 + 10;
      arrayOfInt11[i7] = i8;
      int[] arrayOfInt12 = sHexValues;
      int i9 = i6 + 65;
      int i10 = i6 + 10;
      arrayOfInt12[i9] = i10;
      i6 += 1;
    }
  }

  public static void appendQuoted(StringBuilder paramStringBuilder, String paramString)
  {
    int[] arrayOfInt = sOutputEscapes;
    int i = arrayOfInt.length;
    int j = 0;
    int k = paramString.length();
    if (j >= k)
      return;
    int m = paramString.charAt(j);
    if ((m >= i) || (arrayOfInt[m] == 0))
      StringBuilder localStringBuilder1 = paramStringBuilder.append(m);
    while (true)
    {
      j += 1;
      break;
      StringBuilder localStringBuilder2 = paramStringBuilder.append('\\');
      int n = arrayOfInt[m];
      if (n < 0)
      {
        StringBuilder localStringBuilder3 = paramStringBuilder.append('u');
        StringBuilder localStringBuilder4 = paramStringBuilder.append('0');
        StringBuilder localStringBuilder5 = paramStringBuilder.append('0');
        int i1 = -(n + 1);
        char[] arrayOfChar1 = HEX_CHARS;
        int i2 = i1 >> 4;
        char c1 = arrayOfChar1[i2];
        StringBuilder localStringBuilder6 = paramStringBuilder.append(c1);
        char[] arrayOfChar2 = HEX_CHARS;
        int i3 = i1 & 0xF;
        char c2 = arrayOfChar2[i3];
        StringBuilder localStringBuilder7 = paramStringBuilder.append(c2);
      }
      else
      {
        char c3 = (char)n;
        StringBuilder localStringBuilder8 = paramStringBuilder.append(c3);
      }
    }
  }

  public static int charToHex(int paramInt)
  {
    if (paramInt > 127);
    for (int i = -1; ; i = sHexValues[paramInt])
      return i;
  }

  public static final int[] getInputCodeComment()
  {
    return sInputCodesComment;
  }

  public static final int[] getInputCodeLatin1()
  {
    return sInputCodes;
  }

  public static final int[] getInputCodeLatin1JsNames()
  {
    return sInputCodesJsNames;
  }

  public static final int[] getInputCodeUtf8()
  {
    return sInputCodesUtf8;
  }

  public static final int[] getInputCodeUtf8JsNames()
  {
    return sInputCodesUtf8JsNames;
  }

  public static final int[] getOutputEscapes()
  {
    return sOutputEscapes;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.util.CharTypes
 * JD-Core Version:    0.6.2
 */