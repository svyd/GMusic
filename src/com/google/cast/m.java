package com.google.cast;

import android.net.Uri;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

class m extends NetworkRequest
{
  private Uri a;
  private Uri b;
  private MimeData c;

  public m(CastContext paramCastContext, Uri paramUri, MimeData paramMimeData)
  {
    super(paramCastContext);
    if (paramUri == null)
      throw new IllegalArgumentException("must specify application url");
    this.a = paramUri;
    this.c = paramMimeData;
  }

  public final Uri a()
  {
    return this.b;
  }

  public final int execute()
  {
    int i = -1;
    try
    {
      Uri localUri1 = this.a;
      MimeData localMimeData = this.c;
      int j = DEFAULT_TIMEOUT;
      Object localObject = performHttpPost(localUri1, localMimeData, j);
      int k = ((SimpleHttpRequest)localObject).getResponseStatus();
      if (k == 201)
      {
        localObject = ((SimpleHttpRequest)localObject).getResponseHeader("Location");
        if (localObject == null)
          i = -1;
      }
      while (true)
      {
        label64: return i;
        Uri localUri2 = Uri.parse((String)localObject);
        this.b = localUri2;
        i = 0;
        continue;
        if (k == 404)
          i = -1;
        else if (k == 503)
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
      break label64;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.m
 * JD-Core Version:    0.6.2
 */