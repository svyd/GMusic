package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import java.util.Iterator;

public final class Iterables
{
  public static <T> T getOnlyElement(Iterable<T> paramIterable)
  {
    return Iterators.getOnlyElement(paramIterable.iterator());
  }

  public static String toString(Iterable<?> paramIterable)
  {
    return Iterators.toString(paramIterable.iterator());
  }

  public static <F, T> Iterable<T> transform(Iterable<F> paramIterable, final Function<? super F, ? extends T> paramFunction)
  {
    Object localObject1 = Preconditions.checkNotNull(paramIterable);
    Object localObject2 = Preconditions.checkNotNull(paramFunction);
    return new IterableWithToString()
    {
      public Iterator<T> iterator()
      {
        Iterator localIterator = Iterables.this.iterator();
        Function localFunction = paramFunction;
        return Iterators.transform(localIterator, localFunction);
      }
    };
  }

  static abstract class IterableWithToString<E>
    implements Iterable<E>
  {
    public String toString()
    {
      return Iterables.toString(this);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Iterables
 * JD-Core Version:    0.6.2
 */