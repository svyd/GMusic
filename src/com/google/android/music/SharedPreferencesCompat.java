package com.google.android.music;

import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SharedPreferencesCompat
{
  private static final Method sApplyMethod = findApplyMethod();

  public static void apply(SharedPreferences.Editor paramEditor)
  {
    if (sApplyMethod != null);
    try
    {
      Method localMethod = sApplyMethod;
      Object[] arrayOfObject = new Object[0];
      Object localObject = localMethod.invoke(paramEditor, arrayOfObject);
      return;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      boolean bool = paramEditor.commit();
      return;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      label25: break label25;
    }
  }

  private static Method findApplyMethod()
  {
    if (Build.VERSION.SDK_INT >= 9);
    while (true)
    {
      try
      {
        Class[] arrayOfClass = new Class[0];
        Method localMethod1 = SharedPreferences.Editor.class.getMethod("apply", arrayOfClass);
        localMethod2 = localMethod1;
        return localMethod2;
      }
      catch (Exception localException)
      {
      }
      Method localMethod2 = null;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.SharedPreferencesCompat
 * JD-Core Version:    0.6.2
 */