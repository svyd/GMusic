package com.google.cast;

import android.net.Uri;
import android.text.TextUtils;
import java.nio.charset.Charset;
import org.json.JSONObject;

public final class MimeData
{
  private static final Charset a = Charset.forName("UTF-8");
  private byte[] b;
  private String c;

  public MimeData(String paramString1, String paramString2)
    throws IllegalArgumentException
  {
    a(paramString2);
    if (paramString1 == null)
      throw new IllegalArgumentException("data cannot be null");
    Charset localCharset = a;
    byte[] arrayOfByte = paramString1.getBytes(localCharset);
    this.b = arrayOfByte;
    this.c = paramString2;
  }

  public MimeData(byte[] paramArrayOfByte, String paramString)
    throws IllegalArgumentException
  {
    a(paramString);
    if (paramArrayOfByte == null)
      throw new IllegalArgumentException("data cannot be null");
    this.b = paramArrayOfByte;
    this.c = paramString;
  }

  private void a(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
      return;
    throw new IllegalArgumentException("mime type cannot be null or empty");
  }

  public static MimeData createJsonData(JSONObject paramJSONObject)
  {
    String str = paramJSONObject.toString();
    return new MimeData(str, "application/json");
  }

  public static MimeData createUrlData(Uri paramUri)
  {
    String str = paramUri.toString();
    return new MimeData(str, "text/url");
  }

  public byte[] getData()
  {
    return this.b;
  }

  public String getTextData()
  {
    byte[] arrayOfByte;
    Charset localCharset;
    if (this.b != null)
    {
      arrayOfByte = this.b;
      localCharset = a;
    }
    for (String str = new String(arrayOfByte, localCharset); ; str = null)
      return str;
  }

  public String getType()
  {
    return this.c;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder().append("[MimeData; type: ");
    String str = this.c;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str).append(", length: ");
    int i = this.b.length;
    return i + "]";
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.MimeData
 * JD-Core Version:    0.6.2
 */