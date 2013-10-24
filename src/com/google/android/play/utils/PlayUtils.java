package com.google.android.play.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class PlayUtils
{
  public static String getDefaultUserAgentString(Context paramContext)
  {
    StringBuilder localStringBuilder1 = new StringBuilder("Android/");
    try
    {
      PackageManager localPackageManager = paramContext.getPackageManager();
      String str1 = paramContext.getPackageName();
      PackageInfo localPackageInfo = localPackageManager.getPackageInfo(str1, 0);
      String str2 = localPackageInfo.packageName;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str2);
      StringBuilder localStringBuilder3 = localStringBuilder1.append("/");
      int i = localPackageInfo.versionCode;
      StringBuilder localStringBuilder4 = localStringBuilder1.append(i);
      return localStringBuilder1.toString();
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      while (true)
      {
        String str3 = localNameNotFoundException.getMessage();
        int j = Log.wtf("PlayUtils", str3, localNameNotFoundException);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.play.utils.PlayUtils
 * JD-Core Version:    0.6.2
 */