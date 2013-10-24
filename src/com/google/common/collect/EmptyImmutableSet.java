package com.google.common.collect;

import java.util.Collection;
import java.util.Set;

final class EmptyImmutableSet extends ImmutableSet<Object>
{
  private static final Object[] EMPTY_ARRAY = new Object[0];
  static final EmptyImmutableSet INSTANCE = new EmptyImmutableSet();
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
    if ((paramObject instanceof Set));
    for (boolean bool = ((Set)paramObject).isEmpty(); ; bool = false)
      return bool;
  }

  public final int hashCode()
  {
    return 0;
  }

  public boolean isEmpty()
  {
    return true;
  }

  boolean isHashCodeFast()
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

  Object readResolve()
  {
    return INSTANCE;
  }

  public int size()
  {
    return 0;
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
 * Qualified Name:     com.google.common.collect.EmptyImmutableSet
 * JD-Core Version:    0.6.2
 */