package com.google.android.music.store;

public class DataNotFoundException extends MusicStoreException
{
  private static final long serialVersionUID = 1L;

  public DataNotFoundException()
  {
  }

  public DataNotFoundException(String paramString)
  {
    super(paramString);
  }

  public DataNotFoundException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }

  public DataNotFoundException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.DataNotFoundException
 * JD-Core Version:    0.6.2
 */