package com.google.common.base;

import java.util.Iterator;

public final class Splitter
{
  private final int limit;
  private final boolean omitEmptyStrings;
  private final Strategy strategy;
  private final CharMatcher trimmer;

  private Splitter(Strategy paramStrategy)
  {
    this(paramStrategy, false, localCharMatcher, 2147483647);
  }

  private Splitter(Strategy paramStrategy, boolean paramBoolean, CharMatcher paramCharMatcher, int paramInt)
  {
    this.strategy = paramStrategy;
    this.omitEmptyStrings = paramBoolean;
    this.trimmer = paramCharMatcher;
    this.limit = paramInt;
  }

  public static Splitter on(String paramString)
  {
    if (paramString.length() != 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool, "The separator may not be the empty string.");
      Strategy local2 = new Strategy()
      {
        public Splitter.SplittingIterator iterator(Splitter paramAnonymousSplitter, CharSequence paramAnonymousCharSequence)
        {
          return new Splitter.SplittingIterator(paramAnonymousSplitter, paramAnonymousCharSequence)
          {
            public int separatorEnd(int paramAnonymous2Int)
            {
              return Splitter.this.length() + paramAnonymous2Int;
            }

            public int separatorStart(int paramAnonymous2Int)
            {
              int i = Splitter.this.length();
              int j = paramAnonymous2Int;
              int k = this.toSplit.length() - i;
              if (j <= k)
              {
                int m = 0;
                while (true)
                {
                  if (m >= i)
                    break label104;
                  CharSequence localCharSequence = this.toSplit;
                  int n = m + j;
                  int i1 = localCharSequence.charAt(n);
                  int i2 = Splitter.this.charAt(m);
                  if (i1 != i2)
                  {
                    j += 1;
                    break;
                  }
                  m += 1;
                }
              }
              j = -1;
              label104: return j;
            }
          };
        }
      };
      return new Splitter(local2);
    }
  }

  private Iterator<String> spliterator(CharSequence paramCharSequence)
  {
    return this.strategy.iterator(this, paramCharSequence);
  }

  public Iterable<String> split(final CharSequence paramCharSequence)
  {
    Object localObject = Preconditions.checkNotNull(paramCharSequence);
    return new Iterable()
    {
      public Iterator<String> iterator()
      {
        Splitter localSplitter = Splitter.this;
        CharSequence localCharSequence = paramCharSequence;
        return localSplitter.spliterator(localCharSequence);
      }
    };
  }

  private static abstract class SplittingIterator extends AbstractIterator<String>
  {
    int limit;
    int offset = 0;
    final boolean omitEmptyStrings;
    final CharSequence toSplit;
    final CharMatcher trimmer;

    protected SplittingIterator(Splitter paramSplitter, CharSequence paramCharSequence)
    {
      CharMatcher localCharMatcher = paramSplitter.trimmer;
      this.trimmer = localCharMatcher;
      boolean bool = paramSplitter.omitEmptyStrings;
      this.omitEmptyStrings = bool;
      int i = paramSplitter.limit;
      this.limit = i;
      this.toSplit = paramCharSequence;
    }

    protected String computeNext()
    {
      int i;
      int m;
      while (this.offset != -1)
      {
        i = this.offset;
        int j = this.offset;
        int k = separatorStart(j);
        if (k == -1)
        {
          m = this.toSplit.length();
          this.offset = -1;
        }
        while (i < m)
        {
          CharMatcher localCharMatcher1 = this.trimmer;
          char c1 = this.toSplit.charAt(i);
          if (!localCharMatcher1.matches(c1))
            break;
          i += 1;
          continue;
          m = k;
          int n = separatorEnd(k);
          this.offset = n;
        }
        while (m > i)
        {
          CharMatcher localCharMatcher2 = this.trimmer;
          CharSequence localCharSequence1 = this.toSplit;
          int i1 = m + -1;
          char c2 = localCharSequence1.charAt(i1);
          if (!localCharMatcher2.matches(c2))
            break;
          int i2 = m + -1;
        }
        if ((!this.omitEmptyStrings) || (i != m))
        {
          if (this.limit == 1)
          {
            m = this.toSplit.length();
            this.offset = -1;
            while (m > i)
            {
              CharMatcher localCharMatcher3 = this.trimmer;
              CharSequence localCharSequence2 = this.toSplit;
              int i3 = m + -1;
              char c3 = localCharSequence2.charAt(i3);
              if (!localCharMatcher3.matches(c3))
                break;
              m += -1;
            }
          }
          int i4 = this.limit + -1;
          this.limit = i4;
        }
      }
      for (String str = this.toSplit.subSequence(i, m).toString(); ; str = (String)endOfData())
        return str;
    }

    abstract int separatorEnd(int paramInt);

    abstract int separatorStart(int paramInt);
  }

  private static abstract interface Strategy
  {
    public abstract Iterator<String> iterator(Splitter paramSplitter, CharSequence paramCharSequence);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Splitter
 * JD-Core Version:    0.6.2
 */