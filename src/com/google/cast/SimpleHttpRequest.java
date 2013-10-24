package com.google.cast;

import android.net.Uri;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public abstract interface SimpleHttpRequest
{
  public abstract void cancel();

  public abstract Uri getFinalUri();

  public abstract MimeData getResponseData();

  public abstract String getResponseHeader(String paramString);

  public abstract int getResponseStatus();

  public abstract void performDelete(Uri paramUri)
    throws IOException, TimeoutException;

  public abstract void performGet(Uri paramUri)
    throws IOException, TimeoutException;

  public abstract void performPost(Uri paramUri, MimeData paramMimeData)
    throws IOException, TimeoutException;

  public abstract void setRequestHeader(String paramString1, String paramString2);
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.SimpleHttpRequest
 * JD-Core Version:    0.6.2
 */