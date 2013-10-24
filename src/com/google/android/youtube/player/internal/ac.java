package com.google.android.youtube.player.internal;

import android.text.TextUtils;

public final class ac
{
  public static <T> T a(T paramT)
  {
    if (paramT == null)
      throw new NullPointerException("null reference");
    return paramT;
  }

  public static <T> T a(T paramT, Object paramObject)
  {
    if (paramT == null)
    {
      String str = String.valueOf(paramObject);
      throw new NullPointerException(str);
    }
    return paramT;
  }

  public static String a(String paramString, Object paramObject)
  {
    if (TextUtils.isEmpty(paramString))
    {
      String str = String.valueOf(paramObject);
      throw new IllegalArgumentException(str);
    }
    return paramString;
  }

  public static void a(boolean paramBoolean)
  {
    if (paramBoolean)
      return;
    throw new IllegalStateException();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.ac
 * JD-Core Version:    0.6.2
 */