package com.google.common.base;

public final class Ascii
{
  public static boolean isUpperCase(char paramChar)
  {
    if ((paramChar >= 'A') && (paramChar <= 'Z'));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static char toLowerCase(char paramChar)
  {
    if (isUpperCase(paramChar))
      paramChar = (char)(paramChar ^ 0x20);
    return paramChar;
  }

  public static String toLowerCase(String paramString)
  {
    int i = paramString.length();
    StringBuilder localStringBuilder1 = new StringBuilder(i);
    int j = 0;
    while (j < i)
    {
      char c = toLowerCase(paramString.charAt(j));
      StringBuilder localStringBuilder2 = localStringBuilder1.append(c);
      j += 1;
    }
    return localStringBuilder1.toString();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Ascii
 * JD-Core Version:    0.6.2
 */