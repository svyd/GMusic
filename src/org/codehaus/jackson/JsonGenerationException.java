package org.codehaus.jackson;

public class JsonGenerationException extends JsonProcessingException
{
  static final long serialVersionUID = 123L;

  public JsonGenerationException(String paramString)
  {
    super(paramString, localJsonLocation);
  }

  public JsonGenerationException(String paramString, Throwable paramThrowable)
  {
    super(paramString, localJsonLocation, paramThrowable);
  }

  public JsonGenerationException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.JsonGenerationException
 * JD-Core Version:    0.6.2
 */