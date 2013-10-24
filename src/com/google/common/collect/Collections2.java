package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Iterator;

public final class Collections2
{
  static final Joiner STANDARD_JOINER = Joiner.on(", ");

  static <T> Collection<T> cast(Iterable<T> paramIterable)
  {
    return (Collection)paramIterable;
  }

  static boolean containsAllImpl(Collection<?> paramCollection1, Collection<?> paramCollection2)
  {
    Object localObject1 = Preconditions.checkNotNull(paramCollection1);
    Iterator localIterator = paramCollection2.iterator();
    Object localObject2;
    do
    {
      if (!localIterator.hasNext())
        break;
      localObject2 = localIterator.next();
    }
    while (paramCollection1.contains(localObject2));
    for (boolean bool = false; ; bool = true)
      return bool;
  }

  static StringBuilder newStringBuilderForCollection(int paramInt)
  {
    if (paramInt >= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool, "size must be non-negative");
      int i = (int)Math.min(paramInt * 8L, 1073741824L);
      return new StringBuilder(i);
    }
  }

  static boolean safeContains(Collection<?> paramCollection, Object paramObject)
  {
    try
    {
      boolean bool1 = paramCollection.contains(paramObject);
      bool2 = bool1;
      return bool2;
    }
    catch (ClassCastException localClassCastException)
    {
      while (true)
        boolean bool2 = false;
    }
  }

  static String toStringImpl(Collection<?> paramCollection)
  {
    StringBuilder localStringBuilder1 = newStringBuilderForCollection(paramCollection.size()).append('[');
    Joiner localJoiner = STANDARD_JOINER;
    Function local1 = new Function()
    {
      public Object apply(Object paramAnonymousObject)
      {
        Collection localCollection = Collections2.this;
        if (paramAnonymousObject == localCollection)
          paramAnonymousObject = "(this Collection)";
        return paramAnonymousObject;
      }
    };
    Iterable localIterable = Iterables.transform(paramCollection, local1);
    StringBuilder localStringBuilder2 = localJoiner.appendTo(localStringBuilder1, localIterable);
    return ']';
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Collections2
 * JD-Core Version:    0.6.2
 */