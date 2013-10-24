package com.google.android.music.utils;

import android.content.ContentResolver;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.music.NautilusStatus;
import com.google.android.music.store.ConfigContent;

public class ConfigUtils
{
  private static boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.STORE);
  private static final ConfigCache sAppConfigCache = new ConfigCache(2, localWorker2);
  private static volatile ContentResolver sContentResolver;
  private static final ConfigCache sServerConfigCache;
  private static final Worker sWorker = new Worker();

  static
  {
    Worker localWorker1 = sWorker;
    sServerConfigCache = new ConfigCache(1, localWorker1);
    Worker localWorker2 = sWorker;
  }

  public static void deleteAllServerSettings()
  {
    ContentResolver localContentResolver = sContentResolver;
    Uri localUri = ConfigContent.SERVER_SETTINGS_URI;
    int i = localContentResolver.delete(localUri, null, null);
  }

  private static boolean getBoolean(int paramInt, String paramString, boolean paramBoolean)
  {
    String str1 = getString(paramInt, paramString);
    if (TextUtils.isEmpty(str1));
    while (true)
    {
      return paramBoolean;
      if (str1.equals("true"))
      {
        paramBoolean = true;
      }
      else if (str1.equals("false"))
      {
        paramBoolean = false;
      }
      else
      {
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = paramString;
        arrayOfObject[1] = str1;
        String str2 = String.format("Attempt to read: (%s, %s) as boolean", arrayOfObject);
        int i = Log.w("ConfigUtils", str2);
      }
    }
  }

  public static String getCookie()
  {
    return getString(1, "cookie");
  }

  public static long getInstantSearchDelayMs()
  {
    return getLong(1, "instantSearchDelay", 500L);
  }

  private static long getLong(int paramInt, String paramString, long paramLong)
  {
    String str1 = getString(paramInt, paramString);
    try
    {
      long l1;
      if (!TextUtils.isEmpty(str1))
        l1 = Long.parseLong(str1);
      for (l2 = l1; ; l2 = paramLong)
        return l2;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (true)
      {
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = paramString;
        Long localLong = Long.valueOf(0L);
        arrayOfObject[1] = localLong;
        String str2 = String.format("Attempt to read: (%s, %s) as long", arrayOfObject);
        int i = Log.w("ConfigUtils", str2);
        long l2 = paramLong;
      }
    }
  }

  public static String getMediaRoutePackageSignatures()
  {
    return getString(1, "mediaRoutePackageSignatures");
  }

  public static long getNautilusExpirationTimeInMillisec()
  {
    return getLong(1, "nautilusExpirationTimeMs", 0L);
  }

  public static NautilusStatus getNautilusStatus()
  {
    NautilusStatus localNautilusStatus;
    if (getBoolean(1, "isNautilusUser", false))
      localNautilusStatus = NautilusStatus.GOT_NAUTILUS;
    while (true)
    {
      return localNautilusStatus;
      if (getBoolean(1, "isTrAvailable", false))
        localNautilusStatus = NautilusStatus.TRIAL_AVAILABLE;
      else if (getBoolean(1, "isNautilusAvailable", false))
        localNautilusStatus = NautilusStatus.PURCHASE_AVAILABLE_NO_TRIAL;
      else
        localNautilusStatus = NautilusStatus.UNAVAILABLE;
    }
  }

  public static boolean getShouldValidateMediaRoutes()
  {
    return getBoolean(1, "shouldValidateMediaRoutes", true);
  }

  private static String getString(int paramInt, String paramString)
  {
    switch (paramInt)
    {
    default:
      String str1 = "Unsupported type: " + paramInt;
      throw new IllegalArgumentException(str1);
    case 1:
    case 2:
    }
    for (String str2 = sServerConfigCache.get(paramString); ; str2 = sAppConfigCache.get(paramString))
      return str2;
  }

  public static boolean hasIsNautilusAvailableBeenSet()
  {
    int i = 1;
    if (!TextUtils.isEmpty(getString(i, "isNautilusAvailable")));
    while (true)
    {
      return i;
      int j = 0;
    }
  }

  public static boolean hasIsNautilusFreeTrialAvailableBeenSet()
  {
    int i = 1;
    if (!TextUtils.isEmpty(getString(i, "isTrAvailable")));
    while (true)
    {
      return i;
      int j = 0;
    }
  }

  public static void init(ContentResolver paramContentResolver)
  {
    if (paramContentResolver == null)
      throw new IllegalArgumentException("Missing ContentResolver");
    sContentResolver = paramContentResolver;
    sServerConfigCache.init(paramContentResolver);
    sAppConfigCache.init(paramContentResolver);
  }

  public static boolean isInstantSearchEnabled()
  {
    return getBoolean(1, "isInstantSearchEnabled", true);
  }

  public static boolean isNautilusEnabled()
  {
    return getBoolean(1, "isNautilusUser", false);
  }

  public static boolean isNautilusFreeTrialAvailable()
  {
    return getBoolean(1, "isTrAvailable", false);
  }

  public static boolean isNautilusPurchaseAvailable()
  {
    return getBoolean(1, "isNautilusAvailable", false);
  }

  private static final class Worker extends LoggableHandler
  {
    public Worker()
    {
      super();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.ConfigUtils
 * JD-Core Version:    0.6.2
 */