package com.google.android.music.cloudclient;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class ImageRefJson extends GenericJson
{

  @Key("height")
  public int mHeight;

  @Key("url")
  public String mUrl;

  @Key("width")
  public int mWidth;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.ImageRefJson
 * JD-Core Version:    0.6.2
 */