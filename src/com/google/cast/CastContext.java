package com.google.cast;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;

public final class CastContext
{
  private Context a;
  private String b;
  private AndroidHttpClient c;

  public CastContext(Context paramContext)
    throws IllegalArgumentException
  {
    if (paramContext == null)
      throw new IllegalArgumentException("invalid application context");
    this.a = paramContext;
    String str1 = this.a.getPackageName();
    if (str1 == null)
      throw new IllegalArgumentException("invalid application context");
    String str2;
    try
    {
      str2 = this.a.getPackageManager().getPackageInfo(str1, 0).versionName;
      if (str2 == null)
        throw new IllegalArgumentException("invalid application context");
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      throw new IllegalArgumentException("invalid application context");
    }
    String str3 = str1 + '/' + str2;
    String str4 = System.getProperty("http.agent");
    int i = str4.indexOf('(');
    String str6;
    if (i > 0)
    {
      StringBuilder localStringBuilder = new StringBuilder().append(str3).append(' ');
      String str5 = str4.substring(i);
      str6 = str5;
    }
    for (this.b = str6; ; this.b = str3)
    {
      AndroidHttpClient localAndroidHttpClient = AndroidHttpClient.newInstance(this.b);
      this.c = localAndroidHttpClient;
      return;
    }
  }

  AndroidHttpClient a()
  {
    return this.c;
  }

  public void dispose()
  {
    this.c.close();
  }

  public Context getApplicationContext()
  {
    return this.a;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.CastContext
 * JD-Core Version:    0.6.2
 */