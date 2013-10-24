package com.google.cast;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

class d
{
  private static Intent a(String paramString)
  {
    Intent localIntent1 = new Intent(paramString);
    Intent localIntent2 = localIntent1.setPackage("com.google.android.apps.chromecast.app");
    return localIntent1;
  }

  public static void a(Context paramContext, CastDevice paramCastDevice)
  {
    if (b(paramContext))
      return;
    if (!a(paramContext))
      return;
    Intent localIntent1 = a("com.google.android.apps.chromecast.app.notification.action.DEVICE_FLUNG");
    Intent localIntent2 = localIntent1.putExtra("device", paramCastDevice);
    paramContext.sendBroadcast(localIntent1);
  }

  public static void a(Context paramContext, String paramString1, String paramString2, int paramInt)
  {
    boolean bool1 = true;
    if (b(paramContext))
      return;
    if (!a(paramContext))
      return;
    boolean bool2;
    if ((paramInt & 0x2) == 2)
    {
      bool2 = true;
      if ((paramInt & 0x1) != 1)
        break label115;
    }
    while (true)
    {
      Intent localIntent1 = a("com.google.android.apps.chromecast.app.notification.action.NOTIFICATION_SETTINGS");
      Intent localIntent2 = localIntent1.putExtra("INTENT_CAST_APP_NAME", paramString1);
      String str = paramContext.getPackageName();
      Intent localIntent3 = localIntent1.putExtra("INTENT_APP_PACKAGE_NAME", str);
      Intent localIntent4 = localIntent1.putExtra("INTENT_INSTANCE_URL", paramString2);
      Intent localIntent5 = localIntent1.putExtra("INTENT_DISABLE_REMOTE_CONTROL", bool2);
      Intent localIntent6 = localIntent1.putExtra("INTENT_DISABLE_NOTIFICATION", bool1);
      paramContext.sendBroadcast(localIntent1);
      return;
      bool2 = false;
      break;
      label115: bool1 = false;
    }
  }

  private static boolean a(Context paramContext)
  {
    boolean bool = true;
    try
    {
      PackageInfo localPackageInfo = paramContext.getPackageManager().getPackageInfo("com.google.android.apps.chromecast.app", 1);
      return bool;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      while (true)
        bool = false;
    }
  }

  private static boolean b(Context paramContext)
  {
    String str = paramContext.getPackageName();
    return "com.google.android.apps.chromecast.app".equals(str);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.d
 * JD-Core Version:    0.6.2
 */