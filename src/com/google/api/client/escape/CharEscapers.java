package com.google.api.client.escape;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public final class CharEscapers
{
  private static final Escaper URI_ESCAPER = new PercentEscaper("-_.*", true);
  private static final Escaper URI_PATH_ESCAPER = new PercentEscaper("-_.!~*'()@:$&,;=", false);
  private static final Escaper URI_QUERY_STRING_ESCAPER = new PercentEscaper("-_.!~*'()@:$,;/?:", false);

  public static String decodeUri(String paramString)
  {
    try
    {
      String str = URLDecoder.decode(paramString, "UTF-8");
      return str;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new RuntimeException(localUnsupportedEncodingException);
    }
  }

  public static String escapeUriPath(String paramString)
  {
    return URI_PATH_ESCAPER.escape(paramString);
  }

  public static String escapeUriQuery(String paramString)
  {
    return URI_QUERY_STRING_ESCAPER.escape(paramString);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.escape.CharEscapers
 * JD-Core Version:    0.6.2
 */