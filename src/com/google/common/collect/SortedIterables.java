package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

final class SortedIterables
{
  public static boolean hasSameComparator(Comparator<?> paramComparator, Iterable<?> paramIterable)
  {
    Object localObject1 = Preconditions.checkNotNull(paramComparator);
    Object localObject2 = Preconditions.checkNotNull(paramIterable);
    Object localObject3;
    if ((paramIterable instanceof SortedSet))
    {
      localObject3 = ((SortedSet)paramIterable).comparator();
      if (localObject3 == null)
        localObject3 = Ordering.natural();
    }
    while (true)
    {
      return paramComparator.equals(localObject3);
      if ((paramIterable instanceof SortedIterable))
        localObject3 = ((SortedIterable)paramIterable).comparator();
      else
        localObject3 = null;
    }
  }

  public static <E> Collection<E> sortedUnique(Comparator<? super E> paramComparator, Iterator<E> paramIterator)
  {
    TreeSet localTreeSet = Sets.newTreeSet(paramComparator);
    boolean bool = Iterators.addAll(localTreeSet, paramIterator);
    return localTreeSet;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.SortedIterables
 * JD-Core Version:    0.6.2
 */