package com.google.common.base;

public final class Strings
{
  public static boolean isNullOrEmpty(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  static boolean validSurrogatePairAt(CharSequence paramCharSequence, int paramInt)
  {
    if (paramInt >= 0)
    {
      int i = paramCharSequence.length() + -2;
      if ((paramInt <= i) && (Character.isHighSurrogate(paramCharSequence.charAt(paramInt))))
      {
        int j = paramInt + 1;
        if (!Character.isLowSurrogate(paramCharSequence.charAt(j)));
      }
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Strings
 * JD-Core Version:    0.6.2
 */