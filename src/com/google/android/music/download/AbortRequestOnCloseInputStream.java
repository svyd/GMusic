package com.google.android.music.download;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.client.methods.AbortableHttpRequest;

public class AbortRequestOnCloseInputStream extends InputStream
{
  private AbortableHttpRequest mAbortable;
  private boolean mEofReached = false;
  private InputStream mWrappedStream;

  public AbortRequestOnCloseInputStream(InputStream paramInputStream, AbortableHttpRequest paramAbortableHttpRequest)
  {
    if ((paramInputStream == null) || (paramAbortableHttpRequest == null))
    {
      NullPointerException localNullPointerException = new java/lang/NullPointerException;
      StringBuilder localStringBuilder = new StringBuilder();
      if (paramInputStream == null);
      for (String str1 = "wrappedStream"; ; str1 = "abortable")
      {
        String str2 = str1 + " was null";
        localNullPointerException.<init>(str2);
        throw localNullPointerException;
      }
    }
    this.mWrappedStream = paramInputStream;
    this.mAbortable = paramAbortableHttpRequest;
  }

  public int available()
    throws IOException
  {
    return this.mWrappedStream.available();
  }

  public void close()
    throws IOException
  {
    try
    {
      if (!this.mEofReached)
        this.mAbortable.abort();
      return;
    }
    finally
    {
      this.mWrappedStream.close();
    }
  }

  public void mark(int paramInt)
  {
    this.mWrappedStream.mark(paramInt);
  }

  public boolean markSupported()
  {
    return this.mWrappedStream.markSupported();
  }

  public int read()
    throws IOException
  {
    int i = this.mWrappedStream.read();
    if (i == -1)
      this.mEofReached = true;
    return i;
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = this.mWrappedStream.read(paramArrayOfByte, paramInt1, paramInt2);
    if (i == -1)
      this.mEofReached = true;
    return i;
  }

  public void reset()
    throws IOException
  {
    int i = this.mWrappedStream.read();
  }

  public long skip(long paramLong)
    throws IOException
  {
    return this.mWrappedStream.skip(paramLong);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.AbortRequestOnCloseInputStream
 * JD-Core Version:    0.6.2
 */