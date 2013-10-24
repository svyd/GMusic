package com.google.api.client.googleapis;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

public class GoogleUrl extends GenericUrl
{

  @Key
  public String alt;

  public GoogleUrl()
  {
  }

  public GoogleUrl(String paramString)
  {
    super(paramString);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.googleapis.GoogleUrl
 * JD-Core Version:    0.6.2
 */