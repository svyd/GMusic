package com.google.android.music.sync.common;

public class SyncHttpException extends Exception
{
  private long mRetryAfter = 0L;
  private final int mStatusCode;

  public SyncHttpException(String paramString, int paramInt)
  {
    super(paramString);
    this.mStatusCode = paramInt;
  }

  public SyncHttpException(String paramString, Throwable paramThrowable, int paramInt)
  {
    super(paramString, paramThrowable);
    this.mStatusCode = paramInt;
  }

  public int getStatusCode()
  {
    return this.mStatusCode;
  }

  public void setRetryAfter(long paramLong)
  {
    this.mRetryAfter = paramLong;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.common.SyncHttpException
 * JD-Core Version:    0.6.2
 */