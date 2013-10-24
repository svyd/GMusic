package com.google.common.collect;

import java.util.Collection;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Set;

class EmptyImmutableSortedSet<E> extends ImmutableSortedSet<E>
{
  private static final Object[] EMPTY_ARRAY = new Object[0];

  EmptyImmutableSortedSet(Comparator<? super E> paramComparator)
  {
    super(paramComparator);
  }

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
    if ((paramObject instanceof Set));
    for (boolean bool = ((Set)paramObject).isEmpty(); ; bool = false)
      return bool;
  }

  public E first()
  {
    throw new NoSuchElementException();
  }

  public int hashCode()
  {
    return 0;
  }

  ImmutableSortedSet<E> headSetImpl(E paramE, boolean paramBoolean)
  {
    return this;
  }

  int indexOf(Object paramObject)
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

  public UnmodifiableIterator<E> iterator()
  {
    return Iterators.emptyIterator();
  }

  public E last()
  {
    throw new NoSuchElementException();
  }

  public int size()
  {
    return 0;
  }

  ImmutableSortedSet<E> subSetImpl(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2)
  {
    return this;
  }

  ImmutableSortedSet<E> tailSetImpl(E paramE, boolean paramBoolean)
  {
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
 * Qualified Name:     com.google.common.collect.EmptyImmutableSortedSet
 * JD-Core Version:    0.6.2
 */