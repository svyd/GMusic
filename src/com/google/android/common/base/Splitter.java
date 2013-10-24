package com.google.android.common.base;

public final class Splitter
{
  private final boolean omitEmptyStrings;
  private final Strategy strategy;
  private final CharMatcher trimmer;

  private Splitter(Strategy paramStrategy)
  {
    this(paramStrategy, false, localCharMatcher);
  }

  private Splitter(Strategy paramStrategy, boolean paramBoolean, CharMatcher paramCharMatcher)
  {
    this.strategy = paramStrategy;
    this.omitEmptyStrings = paramBoolean;
    this.trimmer = paramCharMatcher;
  }

  public static Splitter on(char paramChar)
  {
    return on(CharMatcher.is(paramChar));
  }

  public static Splitter on(CharMatcher paramCharMatcher)
  {
    Object localObject = Preconditions.checkNotNull(paramCharMatcher);
    Strategy local1 = new Strategy()
    {
    };
    return new Splitter(local1);
  }

  public Splitter omitEmptyStrings()
  {
    Strategy localStrategy = this.strategy;
    CharMatcher localCharMatcher = this.trimmer;
    return new Splitter(localStrategy, true, localCharMatcher);
  }

  private static abstract interface Strategy
  {
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.base.Splitter
 * JD-Core Version:    0.6.2
 */