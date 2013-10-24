package com.google.cast;

import android.net.Uri;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

class o extends NetworkRequest
{
  private Uri a;

  public o(CastContext paramCastContext, Uri paramUri)
  {
    super(paramCastContext);
    this.a = paramUri;
  }

  public final int execute()
  {
    int i = -1;
    try
    {
      Uri localUri = this.a;
      int j = DEFAULT_TIMEOUT;
      int k = performHttpDelete(localUri, j).getResponseStatus();
      int m = k;
      if (m == 200)
        i = 0;
      while (true)
      {
        label39: return i;
        if (m == 404)
          i = -1;
        else if (m == 501)
          i = -1;
      }
    }
    catch (TimeoutException localTimeoutException)
    {
      while (true)
        i = -1;
    }
    catch (IOException localIOException)
    {
      break label39;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.o
 * JD-Core Version:    0.6.2
 */