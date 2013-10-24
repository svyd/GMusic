package com.google.android.music.store;

public class DowngradeException extends RuntimeException
{
  private static final long serialVersionUID = 1L;
  private final String mFilepath;

  public DowngradeException(String paramString)
  {
    this.mFilepath = paramString;
  }

  public String getFilepath()
  {
    return this.mFilepath;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.DowngradeException
 * JD-Core Version:    0.6.2
 */