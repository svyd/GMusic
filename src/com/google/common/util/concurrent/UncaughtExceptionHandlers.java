package com.google.common.util.concurrent;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class UncaughtExceptionHandlers
{
  static final class Exiter
    implements Thread.UncaughtExceptionHandler
  {
    private static final Logger logger = Logger.getLogger(Exiter.class.getName());
    private final Runtime runtime;

    public void uncaughtException(Thread paramThread, Throwable paramThrowable)
    {
      Logger localLogger = logger;
      Level localLevel = Level.SEVERE;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramThread;
      String str = String.format("Caught an exception in %s.  Shutting down.", arrayOfObject);
      localLogger.log(localLevel, str, paramThrowable);
      this.runtime.exit(1);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.util.concurrent.UncaughtExceptionHandlers
 * JD-Core Version:    0.6.2
 */