package com.google.api.client.json;

import java.lang.reflect.Field;
import java.util.Collection;

public class CustomizeJsonParser
{
  public void handleUnrecognizedKey(Object paramObject, String paramString)
  {
  }

  public Collection<Object> newInstanceForArray(Object paramObject, Field paramField)
  {
    return null;
  }

  public Object newInstanceForObject(Object paramObject, Class<?> paramClass)
  {
    return null;
  }

  public boolean stopAt(Object paramObject, String paramString)
  {
    return false;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.json.CustomizeJsonParser
 * JD-Core Version:    0.6.2
 */