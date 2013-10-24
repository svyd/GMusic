package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

final class EmptyImmutableList extends ImmutableList<Object>
{
  private static final Object[] EMPTY_ARRAY = new Object[0];
  static final EmptyImmutableList INSTANCE = new EmptyImmutableList();
  static final UnmodifiableListIterator<Object> ITERATOR = new UnmodifiableListIterator()
  {
    public boolean hasNext()
    {
      return false;
    }

    public boolean hasPrevious()
    {
      return false;
    }

    public Object next()
    {
      throw new NoSuchElementException();
    }

    public int nextIndex()
    {
      return 0;
    }

    public Object previous()
    {
      throw new NoSuchElementException();
    }

    public int previousIndex()
    {
      return -1;
    }
  };
  private static final long serialVersionUID;

  public boolean contains(Object paramObject)
  {
    return false;
  }

  public boolean containsAll(Collection<?> paramCollection)
  {
    return paramCollection.isEmpty();
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof List));
    for (boolean bool = ((List)paramObject).isEmpty(); ; bool = false)
      return bool;
  }

  public Object get(int paramInt)
  {
    int i = Preconditions.checkElementIndex(paramInt, 0);
    throw new AssertionError("unreachable");
  }

  public int hashCode()
  {
    return 1;
  }

  public int indexOf(Object paramObject)
  {
    return -1;
  }

  public boolean isEmpty()
  {
    return true;
  }

  boolean isPartialView()
  {
    return false;
  }

  public UnmodifiableIterator<Object> iterator()
  {
    return Iterators.emptyIterator();
  }

  public int lastIndexOf(Object paramObject)
  {
    return -1;
  }

  public UnmodifiableListIterator<Object> listIterator()
  {
    return ITERATOR;
  }

  public UnmodifiableListIterator<Object> listIterator(int paramInt)
  {
    int i = Preconditions.checkPositionIndex(paramInt, 0);
    return ITERATOR;
  }

  Object readResolve()
  {
    return INSTANCE;
  }

  public int size()
  {
    return 0;
  }

  public ImmutableList<Object> subList(int paramInt1, int paramInt2)
  {
    Preconditions.checkPositionIndexes(paramInt1, paramInt2, 0);
    return this;
  }

  public Object[] toArray()
  {
    return EMPTY_ARRAY;
  }

  public <T> T[] toArray(T[] paramArrayOfT)
  {
    if (paramArrayOfT.length > 0)
      paramArrayOfT[0] = null;
    return paramArrayOfT;
  }

  public String toString()
  {
    return "[]";
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.EmptyImmutableList
 * JD-Core Version:    0.6.2
 */