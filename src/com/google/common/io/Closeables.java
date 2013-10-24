package com.google.common.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Closeables
{
  static final Logger logger = Logger.getLogger(Closeables.class.getName());

  public static void close(Closeable paramCloseable, boolean paramBoolean)
    throws IOException
  {
    if (paramCloseable == null)
      return;
    try
    {
      paramCloseable.close();
      return;
    }
    catch (IOException localIOException)
    {
      if (paramBoolean)
      {
        Logger localLogger = logger;
        Level localLevel = Level.WARNING;
        localLogger.log(localLevel, "IOException thrown while closing Closeable.", localIOException);
        return;
      }
      throw localIOException;
    }
  }

  public static void closeQuietly(Closeable paramCloseable)
  {
    try
    {
      close(paramCloseable, true);
      return;
    }
    catch (IOException localIOException)
    {
      Logger localLogger = logger;
      Level localLevel = Level.SEVERE;
      localLogger.log(localLevel, "IOException should not have been thrown.", localIOException);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.Closeables
 * JD-Core Version:    0.6.2
 */