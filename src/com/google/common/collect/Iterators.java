package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Iterators
{
  static final UnmodifiableIterator<Object> EMPTY_ITERATOR = new UnmodifiableIterator()
  {
    public boolean hasNext()
    {
      return false;
    }

    public Object next()
    {
      throw new NoSuchElementException();
    }
  };
  private static final Iterator<Object> EMPTY_MODIFIABLE_ITERATOR = new Iterator()
  {
    public boolean hasNext()
    {
      return false;
    }

    public Object next()
    {
      throw new NoSuchElementException();
    }

    public void remove()
    {
      throw new IllegalStateException();
    }
  };

  public static <T> boolean addAll(Collection<T> paramCollection, Iterator<? extends T> paramIterator)
  {
    Object localObject1 = Preconditions.checkNotNull(paramCollection);
    boolean bool1 = false;
    while (paramIterator.hasNext())
    {
      Object localObject2 = paramIterator.next();
      boolean bool2 = paramCollection.add(localObject2);
      bool1 |= bool2;
    }
    return bool1;
  }

  static void clear(Iterator<?> paramIterator)
  {
    Object localObject1 = Preconditions.checkNotNull(paramIterator);
    while (true)
    {
      if (!paramIterator.hasNext())
        return;
      Object localObject2 = paramIterator.next();
      paramIterator.remove();
    }
  }

  public static boolean contains(Iterator<?> paramIterator, Object paramObject)
  {
    boolean bool = true;
    if (paramObject == null)
      do
        if (!paramIterator.hasNext())
          break;
      while (paramIterator.next() != null);
    while (true)
    {
      return bool;
      while (true)
        if (paramIterator.hasNext())
        {
          Object localObject = paramIterator.next();
          if (paramObject.equals(localObject))
            break;
        }
      bool = false;
    }
  }

  public static boolean elementsEqual(Iterator<?> paramIterator1, Iterator<?> paramIterator2)
  {
    boolean bool = false;
    if (paramIterator1.hasNext())
      if (paramIterator2.hasNext());
    while (true)
    {
      return bool;
      Object localObject1 = paramIterator1.next();
      Object localObject2 = paramIterator2.next();
      if (Objects.equal(localObject1, localObject2))
        break;
      continue;
      if (!paramIterator2.hasNext())
        bool = true;
    }
  }

  public static <T> UnmodifiableIterator<T> emptyIterator()
  {
    return EMPTY_ITERATOR;
  }

  public static <T> UnmodifiableIterator<T> forArray(final T[] paramArrayOfT)
  {
    Object localObject = Preconditions.checkNotNull(paramArrayOfT);
    int i = paramArrayOfT.length;
    return new AbstractIndexedListIterator(i)
    {
      protected T get(int paramAnonymousInt)
      {
        return paramArrayOfT[paramAnonymousInt];
      }
    };
  }

  static <T> UnmodifiableIterator<T> forArray(final T[] paramArrayOfT, final int paramInt1, int paramInt2)
  {
    if (paramInt2 >= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      int i = paramInt1 + paramInt2;
      int j = paramArrayOfT.length;
      Preconditions.checkPositionIndexes(paramInt1, i, j);
      return new AbstractIndexedListIterator(paramInt2)
      {
        protected T get(int paramAnonymousInt)
        {
          Object[] arrayOfObject = paramArrayOfT;
          int i = paramInt1 + paramAnonymousInt;
          return arrayOfObject[i];
        }
      };
    }
  }

  public static <T> T getOnlyElement(Iterator<T> paramIterator)
  {
    Object localObject1 = paramIterator.next();
    if (!paramIterator.hasNext())
      return localObject1;
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str1 = "expected one element but was: <" + localObject1;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1);
    int i = 0;
    while ((i < 4) && (paramIterator.hasNext()))
    {
      StringBuilder localStringBuilder3 = new StringBuilder().append(", ");
      Object localObject2 = paramIterator.next();
      String str2 = localObject2;
      StringBuilder localStringBuilder4 = localStringBuilder1.append(str2);
      i += 1;
    }
    if (paramIterator.hasNext())
      StringBuilder localStringBuilder5 = localStringBuilder1.append(", ...");
    StringBuilder localStringBuilder6 = localStringBuilder1.append('>');
    String str3 = localStringBuilder1.toString();
    throw new IllegalArgumentException(str3);
  }

  public static <T> UnmodifiableIterator<T> singletonIterator(T paramT)
  {
    return new UnmodifiableIterator()
    {
      boolean done;

      public boolean hasNext()
      {
        if (!this.done);
        for (boolean bool = true; ; bool = false)
          return bool;
      }

      public T next()
      {
        if (this.done)
          throw new NoSuchElementException();
        this.done = true;
        return Iterators.this;
      }
    };
  }

  public static String toString(Iterator<?> paramIterator)
  {
    if (!paramIterator.hasNext());
    StringBuilder localStringBuilder1;
    for (String str = "[]"; ; str = ']')
    {
      return str;
      localStringBuilder1 = new StringBuilder();
      StringBuilder localStringBuilder2 = localStringBuilder1.append('[');
      Object localObject1 = paramIterator.next();
      StringBuilder localStringBuilder3 = localStringBuilder2.append(localObject1);
      while (paramIterator.hasNext())
      {
        StringBuilder localStringBuilder4 = localStringBuilder1.append(", ");
        Object localObject2 = paramIterator.next();
        StringBuilder localStringBuilder5 = localStringBuilder4.append(localObject2);
      }
    }
  }

  public static <F, T> Iterator<T> transform(Iterator<F> paramIterator, final Function<? super F, ? extends T> paramFunction)
  {
    Object localObject1 = Preconditions.checkNotNull(paramIterator);
    Object localObject2 = Preconditions.checkNotNull(paramFunction);
    return new Iterator()
    {
      public boolean hasNext()
      {
        return Iterators.this.hasNext();
      }

      public T next()
      {
        Object localObject = Iterators.this.next();
        return paramFunction.apply(localObject);
      }

      public void remove()
      {
        Iterators.this.remove();
      }
    };
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Iterators
 * JD-Core Version:    0.6.2
 */