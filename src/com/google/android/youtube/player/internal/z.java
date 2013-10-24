package com.google.android.youtube.player.internal;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;

public final class z
{
  private static final Uri a = Uri.parse("http://play.google.com/store/apps/details");

  public static int a(Context paramContext1, Context paramContext2)
  {
    int i = 0;
    if (paramContext2 != null)
    {
      Resources localResources = paramContext2.getResources();
      String str = a(paramContext1);
      i = localResources.getIdentifier("clientTheme", "style", str);
    }
    if (i == 0)
    {
      if (Build.VERSION.SDK_INT < 14)
        break label45;
      i = 16974120;
    }
    while (true)
    {
      return i;
      label45: if (Build.VERSION.SDK_INT >= 11)
        i = 16973931;
      else
        i = 16973829;
    }
  }

  public static Intent a(String paramString)
  {
    Uri localUri = Uri.fromParts("package", paramString, null);
    Intent localIntent1 = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
    Intent localIntent2 = localIntent1.setData(localUri);
    return localIntent1;
  }

  public static String a(Context paramContext)
  {
    Intent localIntent = new Intent("com.google.android.youtube.api.service.START");
    PackageManager localPackageManager = paramContext.getPackageManager();
    ResolveInfo localResolveInfo = localPackageManager.resolveService(localIntent, 0);
    String str;
    if ((localResolveInfo != null) && (localResolveInfo.serviceInfo != null) && (localResolveInfo.serviceInfo.packageName != null))
      str = localResolveInfo.serviceInfo.packageName;
    while (true)
    {
      return str;
      if (localPackageManager.hasSystemFeature("com.google.android.tv"))
        str = "com.google.android.youtube.googletv";
      else
        str = "com.google.android.youtube";
    }
  }

  public static boolean a(Context paramContext, String paramString)
  {
    boolean bool = true;
    try
    {
      Resources localResources1 = paramContext.getPackageManager().getResourcesForApplication(paramString);
      Resources localResources2 = localResources1;
      if (paramString.equals("com.google.android.youtube.googletvdev"))
        paramString = "com.google.android.youtube.googletv";
      int i = localResources2.getIdentifier("youtube_api_version_code", "integer", paramString);
      if (i == 0);
      while (true)
      {
        label43: return bool;
        int j = localResources2.getInteger(i);
        if (1000 <= j)
          bool = false;
      }
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      break label43;
    }
  }

  public static Context b(Context paramContext)
  {
    try
    {
      String str = a(paramContext);
      Context localContext1 = paramContext.createPackageContext(str, 3);
      localContext2 = localContext1;
      return localContext2;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      while (true)
        Context localContext2 = null;
    }
  }

  public static Intent b(String paramString)
  {
    Intent localIntent1 = new Intent("android.intent.action.VIEW");
    Uri localUri = a.buildUpon().appendQueryParameter("id", paramString).build();
    Intent localIntent2 = localIntent1.setData(localUri);
    Intent localIntent3 = localIntent1.setPackage("com.android.vending");
    Intent localIntent4 = localIntent1.addFlags(524288);
    return localIntent1;
  }

  public static int c(Context paramContext)
  {
    Context localContext = b(paramContext);
    return a(paramContext, localContext);
  }

  public static String d(Context paramContext)
  {
    try
    {
      PackageManager localPackageManager = paramContext.getPackageManager();
      String str1 = paramContext.getPackageName();
      String str2 = localPackageManager.getPackageInfo(str1, 0).versionName;
      return str2;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      throw new IllegalStateException("Cannot retrieve calling Context's PackageInfo", localNameNotFoundException);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.z
 * JD-Core Version:    0.6.2
 */