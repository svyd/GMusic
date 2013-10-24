package com.google.api.client.json;

import com.google.api.client.util.GenericData;

public class GenericJson extends GenericData
  implements Cloneable
{
  public GenericJson clone()
  {
    return (GenericJson)super.clone();
  }

  public String toString()
  {
    return Json.toString(this);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.json.GenericJson
 * JD-Core Version:    0.6.2
 */