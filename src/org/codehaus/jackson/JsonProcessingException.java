package org.codehaus.jackson;

import java.io.IOException;

public class JsonProcessingException extends IOException
{
  static final long serialVersionUID = 123L;
  protected JsonLocation mLocation;

  protected JsonProcessingException(String paramString)
  {
    super(paramString);
  }

  protected JsonProcessingException(String paramString, Throwable paramThrowable)
  {
    this(paramString, null, paramThrowable);
  }

  protected JsonProcessingException(String paramString, JsonLocation paramJsonLocation)
  {
    this(paramString, paramJsonLocation, null);
  }

  protected JsonProcessingException(String paramString, JsonLocation paramJsonLocation, Throwable paramThrowable)
  {
    super(paramString);
    if (paramThrowable != null)
      Throwable localThrowable = initCause(paramThrowable);
    this.mLocation = paramJsonLocation;
  }

  protected JsonProcessingException(Throwable paramThrowable)
  {
    this(null, null, paramThrowable);
  }

  public JsonLocation getLocation()
  {
    return this.mLocation;
  }

  public String getMessage()
  {
    String str1 = super.getMessage();
    if (str1 == null)
      str1 = "N/A";
    JsonLocation localJsonLocation = getLocation();
    if (localJsonLocation != null)
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str1);
      StringBuilder localStringBuilder3 = localStringBuilder1.append('\n');
      StringBuilder localStringBuilder4 = localStringBuilder1.append(" at ");
      String str2 = localJsonLocation.toString();
      StringBuilder localStringBuilder5 = localStringBuilder1.append(str2);
      str1 = localStringBuilder1.toString();
    }
    return str1;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str1 = getClass().getName();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(": ");
    String str2 = getMessage();
    return str2;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.JsonProcessingException
 * JD-Core Version:    0.6.2
 */