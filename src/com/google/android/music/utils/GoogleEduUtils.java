package com.google.android.music.utils;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.util.Log;
import java.lang.reflect.Method;

public class GoogleEduUtils
{
  public static boolean isEduDevice(Context paramContext)
  {
    DevicePolicyManager localDevicePolicyManager = (DevicePolicyManager)paramContext.getSystemService("device_policy");
    try
    {
      Class[] arrayOfClass = new Class[1];
      arrayOfClass[0] = String.class;
      Method localMethod = DevicePolicyManager.class.getMethod("isDeviceOwnerApp", arrayOfClass);
      if (localMethod != null)
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = "com.google.android.apps.enterprise.dmagent";
        Object localObject1 = localMethod.invoke(localDevicePolicyManager, arrayOfObject);
        localObject2 = localObject1;
        if (!(localObject2 instanceof Boolean))
          break label98;
        bool = ((Boolean)localObject2).booleanValue();
        return bool;
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        int i = Log.w("GoogleEduUtils", "isEduDevice failed: ", localException);
        Object localObject2 = null;
        continue;
        label98: boolean bool = false;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.GoogleEduUtils
 * JD-Core Version:    0.6.2
 */