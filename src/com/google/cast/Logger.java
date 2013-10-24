package com.google.cast;

import android.util.Log;

public class Logger
{
  private static boolean a = false;
  private String b;
  private boolean c;

  public Logger(String paramString)
  {
    this(paramString, bool);
  }

  public Logger(String paramString, boolean paramBoolean)
  {
    this.b = paramString;
    this.c = paramBoolean;
  }

  public static boolean isDebugEnabledByDefault()
  {
    return a;
  }

  public static void setDebugEnabledByDefault(boolean paramBoolean)
  {
    a = paramBoolean;
  }

  public void d(String paramString, Object[] paramArrayOfObject)
  {
    if (!this.c)
      return;
    String str1 = this.b;
    String str2 = String.format(paramString, paramArrayOfObject);
    int i = Log.d(str1, str2);
  }

  public void d(Throwable paramThrowable, String paramString, Object[] paramArrayOfObject)
  {
    if (!this.c)
      return;
    String str1 = this.b;
    String str2 = String.format(paramString, paramArrayOfObject);
    int i = Log.d(str1, str2, paramThrowable);
  }

  public void e(String paramString, Object[] paramArrayOfObject)
  {
    String str1 = this.b;
    String str2 = String.format(paramString, paramArrayOfObject);
    int i = Log.e(str1, str2);
  }

  public void e(Throwable paramThrowable, String paramString, Object[] paramArrayOfObject)
  {
    String str1 = this.b;
    String str2 = String.format(paramString, paramArrayOfObject);
    int i = Log.e(str1, str2, paramThrowable);
  }

  public void v(String paramString, Object[] paramArrayOfObject)
  {
    if (!this.c)
      return;
    String str1 = this.b;
    String str2 = String.format(paramString, paramArrayOfObject);
    int i = Log.v(str1, str2);
  }

  public void w(String paramString, Object[] paramArrayOfObject)
  {
    String str1 = this.b;
    String str2 = String.format(paramString, paramArrayOfObject);
    int i = Log.w(str1, str2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.Logger
 * JD-Core Version:    0.6.2
 */