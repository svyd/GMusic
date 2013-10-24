package com.google.android.music.sync.common;

public class ConflictDetectedException extends Exception
{
  private int mCount;

  public ConflictDetectedException()
  {
  }

  public ConflictDetectedException(String paramString)
  {
    super(paramString);
  }

  public ConflictDetectedException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }

  public ConflictDetectedException(Throwable paramThrowable)
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
 * Qualified Name:     com.google.android.music.sync.common.ConflictDetectedException
 * JD-Core Version:    0.6.2
 */