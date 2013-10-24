package com.google.android.music.sync.api;

public class ConflictException extends Exception
{
  private int mCount = 1;

  public ConflictException()
  {
  }

  public ConflictException(String paramString)
  {
    super(paramString);
  }

  public ConflictException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }

  public ConflictException(Throwable paramThrowable)
  {
    super(paramThrowable);
  }

  public int getConflictCount()
  {
    return this.mCount;
  }

  public void setConflictCount(int paramInt)
  {
    this.mCount = paramInt;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.api.ConflictException
 * JD-Core Version:    0.6.2
 */