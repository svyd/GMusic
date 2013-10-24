package com.google.common.collect;

import com.google.common.base.Function;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Ordering<T>
  implements Comparator<T>
{
  public static <C extends Comparable> Ordering<C> natural()
  {
    return NaturalOrdering.INSTANCE;
  }

  static class ArbitraryOrdering extends Ordering<Object>
  {
    private Map<Object, Integer> uids;

    ArbitraryOrdering()
    {
      MapMaker localMapMaker = Platform.tryWeakKeys(new MapMaker());
      Function local1 = new Function()
      {
        final AtomicInteger counter;

        public Integer apply(Object paramAnonymousObject)
        {
          return Integer.valueOf(this.counter.getAndIncrement());
        }
      };
      ConcurrentMap localConcurrentMap = localMapMaker.makeComputingMap(local1);
      this.uids = localConcurrentMap;
    }

    public int compare(Object paramObject1, Object paramObject2)
    {
      int i;
      if (paramObject1 == paramObject2)
        i = 0;
      while (true)
      {
        return i;
        int j = identityHashCode(paramObject1);
        int k = identityHashCode(paramObject2);
        if (j != k)
        {
          if (j < k)
            i = -1;
          else
            i = 1;
        }
        else
        {
          Integer localInteger1 = (Integer)this.uids.get(paramObject1);
          Integer localInteger2 = (Integer)this.uids.get(paramObject2);
          int m = localInteger1.compareTo(localInteger2);
          if (m == 0)
            throw new AssertionError();
          i = m;
        }
      }
    }

    int identityHashCode(Object paramObject)
    {
      return System.identityHashCode(paramObject);
    }

    public String toString()
    {
      return "Ordering.arbitrary()";
    }
  }

  static class IncomparableValueException extends ClassCastException
  {
    private static final long serialVersionUID;
    final Object value;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Ordering
 * JD-Core Version:    0.6.2
 */