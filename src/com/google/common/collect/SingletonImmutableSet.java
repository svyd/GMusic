package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Set;

final class SingletonImmutableSet<E> extends ImmutableSet<E>
{
  private transient int cachedHashCode;
  final transient E element;

  SingletonImmutableSet(E paramE)
  {
    Object localObject = Preconditions.checkNotNull(paramE);
    this.element = localObject;
  }

  SingletonImmutableSet(E paramE, int paramInt)
  {
    this.element = paramE;
    this.cachedHashCode = paramInt;
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
      if ((paramObject instanceof Set))
      {
        Set localSet = (Set)paramObject;
        if (localSet.size() == 1)
        {
          Object localObject1 = this.element;
          Object localObject2 = localSet.iterator().next();
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

  public final int hashCode()
  {
    int i = this.cachedHashCode;
    if (i == 0)
    {
      i = this.element.hashCode();
      this.cachedHashCode = i;
    }
    return i;
  }

  public boolean isEmpty()
  {
    return false;
  }

  boolean isHashCodeFast()
  {
    if (this.cachedHashCode != 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  boolean isPartialView()
  {
    return false;
  }

  public UnmodifiableIterator<E> iterator()
  {
    return Iterators.singletonIterator(this.element);
  }

  public int size()
  {
    return 1;
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
 * Qualified Name:     com.google.common.collect.SingletonImmutableSet
 * JD-Core Version:    0.6.2
 */