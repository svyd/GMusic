package com.google.api.client.escape;

public abstract class UnicodeEscaper extends Escaper
{
  protected static int codePointAt(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    if (paramInt1 < paramInt2)
    {
      int i = paramInt1 + 1;
      int j = paramCharSequence.charAt(paramInt1);
      if ((j < 55296) || (j > 57343));
      char c;
      int k;
      while (true)
      {
        return j;
        if (j > 56319)
          break label135;
        if (i != paramInt2)
        {
          j = -j;
        }
        else
        {
          c = paramCharSequence.charAt(i);
          if (!Character.isLowSurrogate(c))
            break;
          k = Character.toCodePoint(j, c);
        }
      }
      String str1 = "Expected low surrogate but got char '" + c + "' with value " + c + " at index " + i;
      throw new IllegalArgumentException(str1);
      label135: StringBuilder localStringBuilder = new StringBuilder().append("Unexpected low surrogate character '").append(k).append("' with value ").append(k).append(" at index ");
      int m = i + -1;
      String str2 = m;
      throw new IllegalArgumentException(str2);
    }
    throw new IndexOutOfBoundsException("Index exceeds specified range");
  }

  private static char[] growBuffer(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    char[] arrayOfChar = new char[paramInt2];
    if (paramInt1 > 0)
      System.arraycopy(paramArrayOfChar, 0, arrayOfChar, 0, paramInt1);
    return arrayOfChar;
  }

  protected abstract char[] escape(int paramInt);

  protected final String escapeSlow(String paramString, int paramInt)
  {
    int i = paramString.length();
    char[] arrayOfChar1 = Platform.charBufferFromThreadLocal();
    int j = 0;
    int k = 0;
    if (paramInt < i)
    {
      int m = codePointAt(paramString, paramInt, i);
      if (m < 0)
        throw new IllegalArgumentException("Trailing high surrogate at end of input");
      char[] arrayOfChar2 = escape(m);
      if (Character.isSupplementaryCodePoint(m));
      for (int n = 2; ; n = 1)
      {
        int i1 = paramInt + n;
        if (arrayOfChar2 != null)
        {
          int i2 = paramInt - k;
          int i3 = j + i2;
          int i4 = arrayOfChar2.length;
          int i5 = i3 + i4;
          if (arrayOfChar1.length < i5)
          {
            int i6 = i - paramInt + i5 + 32;
            arrayOfChar1 = growBuffer(arrayOfChar1, j, i6);
          }
          if (i2 > 0)
          {
            paramString.getChars(k, paramInt, arrayOfChar1, j);
            j += i2;
          }
          if (arrayOfChar2.length > 0)
          {
            int i7 = arrayOfChar2.length;
            System.arraycopy(arrayOfChar2, 0, arrayOfChar1, j, i7);
            int i8 = arrayOfChar2.length;
            j += i8;
          }
          k = i1;
        }
        paramInt = nextEscapeIndex(paramString, i1, i);
        break;
      }
    }
    int i9 = i - k;
    if (i9 > 0)
    {
      int i10 = j + i9;
      if (arrayOfChar1.length < i10)
        arrayOfChar1 = growBuffer(arrayOfChar1, j, i10);
      paramString.getChars(k, i, arrayOfChar1, j);
      j = i10;
    }
    return new String(arrayOfChar1, 0, j);
  }

  protected abstract int nextEscapeIndex(CharSequence paramCharSequence, int paramInt1, int paramInt2);
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.escape.UnicodeEscaper
 * JD-Core Version:    0.6.2
 */