package com.google.android.music.utils;

import android.graphics.Typeface;
import android.util.Log;
import android.widget.TextView;

public class TypefaceUtil
{
  private static boolean sIsRobotoBoldLoaded;
  private static boolean sIsRobotoCondensedItalicLoaded;
  private static boolean sIsRobotoLightLoaded;
  private static boolean sIsRobotoRegularLoaded;
  private static Typeface sRobotoBold;
  private static Typeface sRobotoCondensedItalic;
  private static Typeface sRobotoLight;
  private static Typeface sRobotoRegular;

  public static Typeface getRobotoBoldTypeface()
  {
    try
    {
      if (!sIsRobotoBoldLoaded)
      {
        sRobotoBold = Typeface.createFromFile("/system/fonts/Roboto-Bold.ttf");
        sIsRobotoBoldLoaded = true;
      }
      localTypeface = sRobotoBold;
      return localTypeface;
    }
    catch (RuntimeException localRuntimeException)
    {
      while (true)
      {
        int i = Log.w("TypefaceUtil", "Failed to create /system/fonts/Roboto-Bold.ttf");
        Typeface localTypeface = null;
      }
    }
  }

  public static Typeface getRobotoCondensedItalicTypeface()
  {
    try
    {
      if (!sIsRobotoCondensedItalicLoaded)
      {
        sRobotoCondensedItalic = Typeface.createFromFile("/system/fonts/RobotoCondensed-Italic.ttf");
        sIsRobotoCondensedItalicLoaded = true;
      }
      localTypeface = sRobotoCondensedItalic;
      return localTypeface;
    }
    catch (RuntimeException localRuntimeException)
    {
      while (true)
      {
        int i = Log.w("TypefaceUtil", "Failed to create /system/fonts/RobotoCondensed-Italic.ttf");
        Typeface localTypeface = null;
      }
    }
  }

  public static Typeface getRobotoLightTypeface()
  {
    try
    {
      if (!sIsRobotoLightLoaded)
      {
        sRobotoLight = Typeface.createFromFile("/system/fonts/Roboto-Light.ttf");
        sIsRobotoLightLoaded = true;
      }
      localTypeface = sRobotoLight;
      return localTypeface;
    }
    catch (RuntimeException localRuntimeException)
    {
      while (true)
      {
        int i = Log.w("TypefaceUtil", "Failed to create /system/fonts/Roboto-Light.ttf");
        Typeface localTypeface = null;
      }
    }
  }

  public static Typeface getRobotoRegularTypeface()
  {
    try
    {
      if (!sIsRobotoRegularLoaded)
      {
        sRobotoRegular = Typeface.createFromFile("/system/fonts/Roboto-Regular.ttf");
        sIsRobotoRegularLoaded = true;
      }
      localTypeface = sRobotoRegular;
      return localTypeface;
    }
    catch (RuntimeException localRuntimeException)
    {
      while (true)
      {
        int i = Log.w("TypefaceUtil", "Failed to create /system/fonts/Roboto-Regular.ttf");
        Typeface localTypeface = null;
      }
    }
  }

  public static void setTypeface(TextView paramTextView, int paramInt)
  {
    switch (paramInt)
    {
    default:
      return;
    case 1:
      if (getRobotoRegularTypeface() == null)
        return;
      Typeface localTypeface1 = sRobotoRegular;
      paramTextView.setTypeface(localTypeface1);
      return;
    case 2:
      if (getRobotoLightTypeface() == null)
        return;
      Typeface localTypeface2 = sRobotoLight;
      paramTextView.setTypeface(localTypeface2);
      return;
    case 3:
      if (getRobotoBoldTypeface() == null)
        return;
      Typeface localTypeface3 = sRobotoBold;
      paramTextView.setTypeface(localTypeface3);
      return;
    case 4:
    }
    if (getRobotoCondensedItalicTypeface() == null)
      return;
    Typeface localTypeface4 = sRobotoCondensedItalic;
    paramTextView.setTypeface(localTypeface4);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.TypefaceUtil
 * JD-Core Version:    0.6.2
 */