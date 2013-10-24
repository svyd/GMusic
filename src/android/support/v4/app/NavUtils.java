package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.util.Log;

public class NavUtils
{
  private static final NavUtilsImpl IMPL = new NavUtilsImplBase();
  public static final String PARENT_ACTIVITY = "android.support.PARENT_ACTIVITY";
  private static final String TAG = "NavUtils";

  static
  {
    if (Build.VERSION.SDK_INT >= 16)
    {
      IMPL = new NavUtilsImplJB();
      return;
    }
  }

  public static Intent getParentActivityIntent(Activity paramActivity)
  {
    return IMPL.getParentActivityIntent(paramActivity);
  }

  public static Intent getParentActivityIntent(Context paramContext, ComponentName paramComponentName)
    throws PackageManager.NameNotFoundException
  {
    String str1 = getParentActivityName(paramContext, paramComponentName);
    if (str1 == null)
    {
      localIntent = null;
      return localIntent;
    }
    String str2 = paramComponentName.getPackageName();
    ComponentName localComponentName = new ComponentName(str2, str1);
    if (getParentActivityName(paramContext, localComponentName) == null);
    for (Intent localIntent = IntentCompat.makeMainActivity(localComponentName); ; localIntent = new Intent().setComponent(localComponentName))
      break;
  }

  public static Intent getParentActivityIntent(Context paramContext, Class<?> paramClass)
    throws PackageManager.NameNotFoundException
  {
    ComponentName localComponentName1 = new ComponentName(paramContext, paramClass);
    String str = getParentActivityName(paramContext, localComponentName1);
    if (str == null)
    {
      localIntent = null;
      return localIntent;
    }
    ComponentName localComponentName2 = new ComponentName(paramContext, str);
    if (getParentActivityName(paramContext, localComponentName2) == null);
    for (Intent localIntent = IntentCompat.makeMainActivity(localComponentName2); ; localIntent = new Intent().setComponent(localComponentName2))
      break;
  }

  public static String getParentActivityName(Activity paramActivity)
  {
    try
    {
      ComponentName localComponentName = paramActivity.getComponentName();
      String str = getParentActivityName(paramActivity, localComponentName);
      return str;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      throw new IllegalArgumentException(localNameNotFoundException);
    }
  }

  public static String getParentActivityName(Context paramContext, ComponentName paramComponentName)
    throws PackageManager.NameNotFoundException
  {
    ActivityInfo localActivityInfo = paramContext.getPackageManager().getActivityInfo(paramComponentName, 128);
    return IMPL.getParentActivityName(paramContext, localActivityInfo);
  }

  public static void navigateUpFromSameTask(Activity paramActivity)
  {
    Intent localIntent = getParentActivityIntent(paramActivity);
    if (localIntent == null)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Activity ");
      String str1 = paramActivity.getClass().getSimpleName();
      String str2 = str1 + " does not have a parent activity name specified." + " (Did you forget to add the android.support.PARENT_ACTIVITY <meta-data> " + " element in your manifest?)";
      throw new IllegalArgumentException(str2);
    }
    navigateUpTo(paramActivity, localIntent);
  }

  public static void navigateUpTo(Activity paramActivity, Intent paramIntent)
  {
    IMPL.navigateUpTo(paramActivity, paramIntent);
  }

  public static boolean shouldUpRecreateTask(Activity paramActivity, Intent paramIntent)
  {
    return IMPL.shouldUpRecreateTask(paramActivity, paramIntent);
  }

  static class NavUtilsImplJB extends NavUtils.NavUtilsImplBase
  {
    public Intent getParentActivityIntent(Activity paramActivity)
    {
      Intent localIntent = NavUtilsJB.getParentActivityIntent(paramActivity);
      if (localIntent == null)
        localIntent = superGetParentActivityIntent(paramActivity);
      return localIntent;
    }

    public String getParentActivityName(Context paramContext, ActivityInfo paramActivityInfo)
    {
      String str = NavUtilsJB.getParentActivityName(paramActivityInfo);
      if (str == null)
        str = super.getParentActivityName(paramContext, paramActivityInfo);
      return str;
    }

    public void navigateUpTo(Activity paramActivity, Intent paramIntent)
    {
      NavUtilsJB.navigateUpTo(paramActivity, paramIntent);
    }

    public boolean shouldUpRecreateTask(Activity paramActivity, Intent paramIntent)
    {
      return NavUtilsJB.shouldUpRecreateTask(paramActivity, paramIntent);
    }

    Intent superGetParentActivityIntent(Activity paramActivity)
    {
      return super.getParentActivityIntent(paramActivity);
    }
  }

  static class NavUtilsImplBase
    implements NavUtils.NavUtilsImpl
  {
    public Intent getParentActivityIntent(Activity paramActivity)
    {
      Object localObject = null;
      String str1 = NavUtils.getParentActivityName(paramActivity);
      if (str1 == null);
      while (true)
      {
        return localObject;
        ComponentName localComponentName = new ComponentName(paramActivity, str1);
        try
        {
          if (NavUtils.getParentActivityName(paramActivity, localComponentName) == null);
          Intent localIntent;
          for (localObject = IntentCompat.makeMainActivity(localComponentName); ; localObject = localIntent)
          {
            break;
            localIntent = new Intent().setComponent(localComponentName);
          }
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
          String str2 = "getParentActivityIntent: bad parentActivityName '" + str1 + "' in manifest";
          int i = Log.e("NavUtils", str2);
        }
      }
    }

    public String getParentActivityName(Context paramContext, ActivityInfo paramActivityInfo)
    {
      String str1;
      if (paramActivityInfo.metaData == null)
        str1 = null;
      while (true)
      {
        return str1;
        str1 = paramActivityInfo.metaData.getString("android.support.PARENT_ACTIVITY");
        if (str1 == null)
        {
          str1 = null;
        }
        else if (str1.charAt(0) == '.')
        {
          StringBuilder localStringBuilder = new StringBuilder();
          String str2 = paramContext.getPackageName();
          str1 = str2 + str1;
        }
      }
    }

    public void navigateUpTo(Activity paramActivity, Intent paramIntent)
    {
      Intent localIntent = paramIntent.addFlags(67108864);
      paramActivity.startActivity(paramIntent);
      paramActivity.finish();
    }

    public boolean shouldUpRecreateTask(Activity paramActivity, Intent paramIntent)
    {
      String str = paramActivity.getIntent().getAction();
      if ((str != null) && (!str.equals("android.intent.action.MAIN")));
      for (boolean bool = true; ; bool = false)
        return bool;
    }
  }

  static abstract interface NavUtilsImpl
  {
    public abstract Intent getParentActivityIntent(Activity paramActivity);

    public abstract String getParentActivityName(Context paramContext, ActivityInfo paramActivityInfo);

    public abstract void navigateUpTo(Activity paramActivity, Intent paramIntent);

    public abstract boolean shouldUpRecreateTask(Activity paramActivity, Intent paramIntent);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.NavUtils
 * JD-Core Version:    0.6.2
 */