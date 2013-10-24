package com.google.android.music.sync.common;

public class HardSyncException extends Exception
{
  private long mRetryAfter = 0L;

  public HardSyncException()
  {
  }

  public HardSyncException(String paramString)
  {
    super(paramString);
  }

  public HardSyncException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }

  public HardSyncException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.common.HardSyncException
 * JD-Core Version:    0.6.2
 */