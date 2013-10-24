package com.google.android.play.utils;

public class Assertions
{
  public static void checkArgument(boolean paramBoolean, String paramString)
    throws IllegalArgumentException
  {
    if (paramBoolean)
      return;
    throw new IllegalArgumentException(paramString);
  }

  public static void checkState(boolean paramBoolean, String paramString)
    throws IllegalStateException
  {
    if (paramBoolean)
      return;
    throw new IllegalStateException(paramString);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.play.utils.Assertions
 * JD-Core Version:    0.6.2
 */