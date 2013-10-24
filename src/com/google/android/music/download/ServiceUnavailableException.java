package com.google.android.music.download;

import org.apache.http.client.HttpResponseException;

public class ServiceUnavailableException extends HttpResponseException
{
  private long mRetryAfterInSeconds;

  public ServiceUnavailableException(long paramLong, String paramString)
  {
    super(503, paramString);
    this.mRetryAfterInSeconds = paramLong;
  }

  public long getRetryAfterInSeconds()
  {
    return this.mRetryAfterInSeconds;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.ServiceUnavailableException
 * JD-Core Version:    0.6.2
 */