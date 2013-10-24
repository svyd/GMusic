package com.google.android.music.log;

import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Log
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.LOG_FILE);
  private static volatile LogFile sLogFile;
  private static Map<String, LogFile> sLogFilesMap = new HashMap();

  /** @deprecated */
  public static void configure(File paramFile, String paramString)
  {
    if ((paramFile == null) || (paramString == null));
    try
    {
      int i = android.util.Log.w("Log", "Invalid configuration provided");
      while (true)
      {
        return;
        try
        {
          sLogFile = new LogFile("com.google.android.music", paramFile, paramString);
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          int j = android.util.Log.w("Log", "Failed to create log file.");
        }
      }
    }
    finally
    {
    }
  }

  /** @deprecated */
  public static void configureLogFile(String paramString1, File paramFile, String paramString2)
  {
    if ((paramFile == null) || (paramString2 == null));
    try
    {
      int i = android.util.Log.w("Log", "Invalid configuration provided");
      while (true)
      {
        return;
        try
        {
          LogFile localLogFile = new LogFile(paramString1, paramFile, paramString2);
          Object localObject1 = sLogFilesMap.put(paramString1, localLogFile);
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          int j = android.util.Log.w("Log", "Failed to create log file.");
        }
      }
    }
    finally
    {
    }
  }

  public static void d(String paramString1, String paramString2)
  {
    int i = android.util.Log.d(paramString1, paramString2);
    if (sLogFile == null)
      return;
    sLogFile.d(paramString1, paramString2);
  }

  public static void d(String paramString1, String paramString2, Throwable paramThrowable)
  {
    int i = android.util.Log.d(paramString1, paramString2, paramThrowable);
    if (sLogFile == null)
      return;
    sLogFile.d(paramString1, paramString2, paramThrowable);
  }

  /** @deprecated */
  public static void dump(PrintWriter paramPrintWriter)
  {
    try
    {
      if (sLogFile != null)
      {
        sLogFile.dump(paramPrintWriter);
        Iterator localIterator = sLogFilesMap.values().iterator();
        if (localIterator.hasNext())
          ((LogFile)localIterator.next()).dump(paramPrintWriter);
      }
    }
    finally
    {
    }
  }

  public static void e(String paramString1, String paramString2)
  {
    int i = android.util.Log.e(paramString1, paramString2);
    if (sLogFile == null)
      return;
    sLogFile.e(paramString1, paramString2);
  }

  public static void e(String paramString1, String paramString2, Throwable paramThrowable)
  {
    int i = android.util.Log.e(paramString1, paramString2, paramThrowable);
    if (sLogFile == null)
      return;
    sLogFile.i(paramString1, paramString2, paramThrowable);
  }

  public static void f(String paramString1, String paramString2)
  {
    if (sLogFile == null)
      return;
    sLogFile.f(paramString1, paramString2);
  }

  public static void f(String paramString1, String paramString2, Throwable paramThrowable)
  {
    if (sLogFile == null)
      return;
    sLogFile.f(paramString1, paramString2, paramThrowable);
  }

  /** @deprecated */
  public static LogFile getLogFile(String paramString)
  {
    try
    {
      LogFile localLogFile = (LogFile)sLogFilesMap.get(paramString);
      return localLogFile;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static void i(String paramString1, String paramString2)
  {
    int i = android.util.Log.i(paramString1, paramString2);
    if (sLogFile == null)
      return;
    sLogFile.i(paramString1, paramString2);
  }

  public static void i(String paramString1, String paramString2, Throwable paramThrowable)
  {
    int i = android.util.Log.i(paramString1, paramString2, paramThrowable);
    if (sLogFile == null)
      return;
    sLogFile.i(paramString1, paramString2, paramThrowable);
  }

  public static boolean isLoggable(String paramString, int paramInt)
  {
    return android.util.Log.isLoggable(paramString, paramInt);
  }

  public static void v(String paramString1, String paramString2)
  {
    int i = android.util.Log.v(paramString1, paramString2);
    if (sLogFile == null)
      return;
    sLogFile.v(paramString1, paramString2);
  }

  public static void v(String paramString1, String paramString2, Throwable paramThrowable)
  {
    int i = android.util.Log.v(paramString1, paramString2, paramThrowable);
    if (sLogFile == null)
      return;
    sLogFile.v(paramString1, paramString2, paramThrowable);
  }

  public static void w(String paramString1, String paramString2)
  {
    int i = android.util.Log.w(paramString1, paramString2);
    if (sLogFile == null)
      return;
    sLogFile.w(paramString1, paramString2);
  }

  public static void w(String paramString1, String paramString2, Throwable paramThrowable)
  {
    int i = android.util.Log.w(paramString1, paramString2, paramThrowable);
    if (sLogFile == null)
      return;
    sLogFile.w(paramString1, paramString2, paramThrowable);
  }

  public static void wtf(String paramString1, String paramString2)
  {
    int i = android.util.Log.wtf(paramString1, paramString2);
    if (sLogFile == null)
      return;
    sLogFile.wtf(paramString1, paramString2);
  }

  public static void wtf(String paramString1, String paramString2, Throwable paramThrowable)
  {
    int i = android.util.Log.wtf(paramString1, paramString2, paramThrowable);
    if (sLogFile == null)
      return;
    sLogFile.wtf(paramString1, paramString2, paramThrowable);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.log.Log
 * JD-Core Version:    0.6.2
 */