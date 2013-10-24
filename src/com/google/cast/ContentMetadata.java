package com.google.cast;

import android.net.Uri;
import org.json.JSONObject;

public class ContentMetadata
{
  private JSONObject a = null;
  private String b = null;
  private Uri c = null;

  public JSONObject getContentInfo()
  {
    return this.a;
  }

  public Uri getImageUrl()
  {
    return this.c;
  }

  public String getTitle()
  {
    return this.b;
  }

  public void setContentInfo(JSONObject paramJSONObject)
  {
    this.a = paramJSONObject;
  }

  public void setImageUrl(Uri paramUri)
  {
    this.c = paramUri;
  }

  public void setTitle(String paramString)
  {
    this.b = paramString;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.ContentMetadata
 * JD-Core Version:    0.6.2
 */