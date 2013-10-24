package com.google.common.base;

public abstract class Ticker
{
  private static final Ticker SYSTEM_TICKER = new Ticker()
  {
    public long read()
    {
      return Platform.systemNanoTime();
    }
  };

  public static Ticker systemTicker()
  {
    return SYSTEM_TICKER;
  }

  public abstract long read();
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Ticker
 * JD-Core Version:    0.6.2
 */