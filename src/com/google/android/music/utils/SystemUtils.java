package com.google.android.music.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import com.google.common.io.Closeables;
import java.io.InputStream;
import java.util.Scanner;

public class SystemUtils
{
  public static String getSystemProperty(String paramString)
  {
    String str1;
    if (TextUtils.isEmpty(paramString))
      str1 = null;
    while (true)
    {
      return str1;
      str1 = null;
      try
      {
        Runtime localRuntime = Runtime.getRuntime();
        String str2 = "/system/bin/getprop " + paramString;
        localProcess = localRuntime.exec(str2);
        localInputStream = localProcess.getInputStream();
        localScanner = new Scanner(localInputStream).useDelimiter("\\n");
        if (localScanner.hasNext())
        {
          String str3 = localScanner.next();
          String str4 = str3;
        }
        Closeables.closeQuietly(localInputStream);
        if (localScanner != null)
          localScanner.close();
        if (localProcess != null)
          localProcess.destroy();
        String str5 = "getSystemProperty: " + paramString + "=" + str1;
        int i = Log.d("SystemUtils", str5);
      }
      catch (Exception localException)
      {
        while (true)
        {
          String str6 = localException.getMessage();
          int j = Log.w("SystemUtils", str6, localException);
          Closeables.closeQuietly(localInputStream);
          if (localScanner != null)
            localScanner.close();
          if (localProcess != null)
            localProcess.destroy();
        }
      }
      finally
      {
        Process localProcess;
        InputStream localInputStream;
        Scanner localScanner;
        Closeables.closeQuietly(localInputStream);
        if (localScanner != null)
          localScanner.close();
        if (localProcess != null)
          localProcess.destroy();
      }
    }
  }

  public static boolean isBatteryCharging(Context paramContext)
  {
    IntentFilter localIntentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
    int i = paramContext.registerReceiver(null, localIntentFilter).getIntExtra("status", -1);
    if ((i == 2) || (i == 5));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static boolean isConnectedToNetwork(Context paramContext)
  {
    NetworkInfo localNetworkInfo = ((ConnectivityManager)paramContext.getSystemService("connectivity")).getActiveNetworkInfo();
    if (localNetworkInfo != null);
    for (boolean bool = localNetworkInfo.isConnectedOrConnecting(); ; bool = false)
      return bool;
  }

  public static boolean isPackageInstalled(Context paramContext, String paramString)
  {
    boolean bool = false;
    PackageManager localPackageManager = paramContext.getPackageManager();
    try
    {
      PackageInfo localPackageInfo = localPackageManager.getPackageInfo(paramString, 0);
      bool = true;
      label17: return bool;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      break label17;
    }
  }

  public static void setComponentEnabled(Context paramContext, Class<?> paramClass, boolean paramBoolean)
  {
    if (paramBoolean);
    for (int i = 1; ; i = 2)
    {
      ComponentName localComponentName = new ComponentName(paramContext, paramClass);
      paramContext.getPackageManager().setComponentEnabledSetting(localComponentName, i, 1);
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.SystemUtils
 * JD-Core Version:    0.6.2
 */