package org.codehaus.jackson.util;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public final class InternCache extends LinkedHashMap<String, String>
{
  public static final InternCache instance = new InternCache();

  private InternCache()
  {
    super(192, 0.8F, true);
  }

  /** @deprecated */
  public String intern(String paramString)
  {
    try
    {
      String str = (String)get(paramString);
      if (str == null)
      {
        str = paramString.intern();
        Object localObject1 = put(str, str);
      }
      return str;
    }
    finally
    {
      localObject2 = finally;
      throw localObject2;
    }
  }

  protected boolean removeEldestEntry(Map.Entry<String, String> paramEntry)
  {
    if (size() > 192);
    for (boolean bool = true; ; bool = false)
      return bool;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.util.InternCache
 * JD-Core Version:    0.6.2
 */