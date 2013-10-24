package com.google.android.common.base;

public class Preconditions
{
  public static void checkArgument(boolean paramBoolean)
  {
    if (paramBoolean)
      return;
    throw new IllegalArgumentException();
  }

  public static void checkArgument(boolean paramBoolean, Object paramObject)
  {
    if (paramBoolean)
      return;
    String str = String.valueOf(paramObject);
    throw new IllegalArgumentException(str);
  }

  public static <T> T checkNotNull(T paramT)
  {
    if (paramT == null)
      throw new NullPointerException();
    return paramT;
  }

  public static <T> T checkNotNull(T paramT, Object paramObject)
  {
    if (paramT == null)
    {
      String str = String.valueOf(paramObject);
      throw new NullPointerException(str);
    }
    return paramT;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.base.Preconditions
 * JD-Core Version:    0.6.2
 */