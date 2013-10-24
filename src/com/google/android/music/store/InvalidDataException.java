package com.google.android.music.store;

public class InvalidDataException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public InvalidDataException()
  {
  }

  public InvalidDataException(String paramString)
  {
    super(paramString);
  }

  public InvalidDataException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }

  public InvalidDataException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.InvalidDataException
 * JD-Core Version:    0.6.2
 */