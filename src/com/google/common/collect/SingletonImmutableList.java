package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.NoSuchElementException;

final class SingletonImmutableList<E> extends ImmutableList<E>
{
  final transient E element;

  SingletonImmutableList(E paramE)
  {
    Object localObject = Preconditions.checkNotNull(paramE);
    this.element = localObject;
  }

  public boolean contains(Object paramObject)
  {
    return this.element.equals(paramObject);
  }

  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this);
    while (true)
    {
      return bool;
      if ((paramObject instanceof List))
      {
        List localList = (List)paramObject;
        if (localList.size() == 1)
        {
          Object localObject1 = this.element;
          Object localObject2 = localList.get(0);
          if (localObject1.equals(localObject2));
        }
        else
        {
          bool = false;
        }
      }
      else
      {
        bool = false;
      }
    }
  }

  public E get(int paramInt)
  {
    int i = Preconditions.checkElementIndex(paramInt, 1);
    return this.element;
  }

  public int hashCode()
  {
    return this.element.hashCode() + 31;
  }

  public int indexOf(Object paramObject)
  {
    if (this.element.equals(paramObject));
    for (int i = 0; ; i = -1)
      return i;
  }

  public boolean isEmpty()
  {
    return false;
  }

  boolean isPartialView()
  {
    return false;
  }

  public UnmodifiableIterator<E> iterator()
  {
    return Iterators.singletonIterator(this.element);
  }

  public int lastIndexOf(Object paramObject)
  {
    if (this.element.equals(paramObject));
    for (int i = 0; ; i = -1)
      return i;
  }

  public UnmodifiableListIterator<E> listIterator(final int paramInt)
  {
    int i = Preconditions.checkPositionIndex(paramInt, 1);
    return new UnmodifiableListIterator()
    {
      boolean hasNext;

      public boolean hasNext()
      {
        return this.hasNext;
      }

      public boolean hasPrevious()
      {
        if (!this.hasNext);
        for (boolean bool = true; ; bool = false)
          return bool;
      }

      public E next()
      {
        if (!this.hasNext)
          throw new NoSuchElementException();
        this.hasNext = false;
        return SingletonImmutableList.this.element;
      }

      public int nextIndex()
      {
        if (this.hasNext);
        for (int i = 0; ; i = 1)
          return i;
      }

      public E previous()
      {
        if (this.hasNext)
          throw new NoSuchElementException();
        this.hasNext = true;
        return SingletonImmutableList.this.element;
      }

      public int previousIndex()
      {
        if (this.hasNext);
        for (int i = -1; ; i = 0)
          return i;
      }
    };
  }

  public int size()
  {
    return 1;
  }

  public ImmutableList<E> subList(int paramInt1, int paramInt2)
  {
    Preconditions.checkPositionIndexes(paramInt1, paramInt2, 1);
    if (paramInt1 != paramInt2)
      this = ImmutableList.of();
    return this;
  }

  public Object[] toArray()
  {
    Object[] arrayOfObject = new Object[1];
    Object localObject = this.element;
    arrayOfObject[0] = localObject;
    return arrayOfObject;
  }

  public <T> T[] toArray(T[] paramArrayOfT)
  {
    if (paramArrayOfT.length == 0)
      paramArrayOfT = ObjectArrays.newArray(paramArrayOfT, 1);
    while (true)
    {
      T[] arrayOfT = paramArrayOfT;
      Object localObject = this.element;
      arrayOfT[0] = localObject;
      return paramArrayOfT;
      if (paramArrayOfT.length > 1)
        paramArrayOfT[1] = null;
    }
  }

  public String toString()
  {
    String str = this.element.toString();
    int i = str.length() + 2;
    return i + '[' + str + ']';
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.SingletonImmutableList
 * JD-Core Version:    0.6.2
 */