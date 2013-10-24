package com.google.android.music.sync.common;

public class SoftSyncException extends Exception
{
  private long mRetryAfter = 0L;

  public SoftSyncException()
  {
  }

  public SoftSyncException(String paramString)
  {
    super(paramString);
  }

  public SoftSyncException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }

  public SoftSyncException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }

  public long getRetryAfter()
  {
    return this.mRetryAfter;
  }

  public void setRetryAfter(long paramLong)
  {
    this.mRetryAfter = paramLong;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.common.SoftSyncException
 * JD-Core Version:    0.6.2
 */