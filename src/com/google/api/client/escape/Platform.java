package com.google.api.client.escape;

final class Platform
{
  private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal()
  {
    protected char[] initialValue()
    {
      return new char[1024];
    }
  };

  static char[] charBufferFromThreadLocal()
  {
    return (char[])DEST_TL.get();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.escape.Platform
 * JD-Core Version:    0.6.2
 */