package com.google.android.music.sync.api;

public class BadRequestException extends Exception
{
  public BadRequestException()
  {
  }

  public BadRequestException(String paramString)
  {
    super(paramString);
  }

  public BadRequestException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }

  public BadRequestException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.api.BadRequestException
 * JD-Core Version:    0.6.2
 */