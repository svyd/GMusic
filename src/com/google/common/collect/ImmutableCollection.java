package com.google.common.collect;

import java.io.Serializable;
import java.util.Collection;

public abstract class ImmutableCollection<E>
  implements Collection<E>, Serializable
{
  static final ImmutableCollection<Object> EMPTY_IMMUTABLE_COLLECTION = new EmptyImmutableCollection(null);
  private transient ImmutableList<E> asList;

  public final boolean add(E paramE)
  {
    throw new UnsupportedOperationException();
  }

  public final boolean addAll(Collection<? extends E> paramCollection)
  {
    throw new UnsupportedOperationException();
  }

  public ImmutableList<E> asList()
  {
    ImmutableList localImmutableList = this.asList;
    if (localImmutableList == null)
    {
      localImmutableList = createAsList();
      this.asList = localImmutableList;
    }
    return localImmutableList;
  }

  public final void clear()
  {
    throw new UnsupportedOperationException();
  }

  public boolean contains(Object paramObject)
  {
    if ((paramObject != null) && (Iterators.contains(iterator(), paramObject)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean containsAll(Collection<?> paramCollection)
  {
    return Collections2.containsAllImpl(this, paramCollection);
  }

  ImmutableList<E> createAsList()
  {
    Object localObject;
    switch (size())
    {
    default:
      Object[] arrayOfObject = toArray();
      localObject = new ImmutableAsList(arrayOfObject, this);
    case 0:
    case 1:
    }
    while (true)
    {
      return localObject;
      localObject = ImmutableList.of();
      continue;
      localObject = ImmutableList.of(iterator().next());
    }
  }

  public boolean isEmpty()
  {
    if (size() == 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  abstract boolean isPartialView();

  public abstract UnmodifiableIterator<E> iterator();

  public final boolean remove(Object paramObject)
  {
    throw new UnsupportedOperationException();
  }

  public final boolean removeAll(Collection<?> paramCollection)
  {
    throw new UnsupportedOperationException();
  }

  public final boolean retainAll(Collection<?> paramCollection)
  {
    throw new UnsupportedOperationException();
  }

  public Object[] toArray()
  {
    return ObjectArrays.toArrayImpl(this);
  }

  public <T> T[] toArray(T[] paramArrayOfT)
  {
    return ObjectArrays.toArrayImpl(this, paramArrayOfT);
  }

  public String toString()
  {
    return Collections2.toStringImpl(this);
  }

  Object writeReplace()
  {
    Object[] arrayOfObject = toArray();
    return new SerializedForm(arrayOfObject);
  }

  public static abstract class Builder<E>
  {
    public abstract Builder<E> add(E paramE);

    public Builder<E> add(E[] paramArrayOfE)
    {
      E[] arrayOfE = paramArrayOfE;
      int i = arrayOfE.length;
      int j = 0;
      while (j < i)
      {
        E ? = arrayOfE[j];
        Builder localBuilder = add(?);
        j += 1;
      }
      return this;
    }
  }

  private static class SerializedForm
    implements Serializable
  {
    private static final long serialVersionUID;
    final Object[] elements;

    SerializedForm(Object[] paramArrayOfObject)
    {
      this.elements = paramArrayOfObject;
    }

    Object readResolve()
    {
      if (this.elements.length == 0);
      Object[] arrayOfObject;
      for (Object localObject = ImmutableCollection.EMPTY_IMMUTABLE_COLLECTION; ; localObject = new ImmutableCollection.ArrayImmutableCollection(arrayOfObject))
      {
        return localObject;
        arrayOfObject = Platform.clone(this.elements);
      }
    }
  }

  private static class ArrayImmutableCollection<E> extends ImmutableCollection<E>
  {
    private final E[] elements;

    ArrayImmutableCollection(E[] paramArrayOfE)
    {
      this.elements = paramArrayOfE;
    }

    ImmutableList<E> createAsList()
    {
      Object localObject1;
      if (this.elements.length == 1)
        localObject1 = this.elements[0];
      Object[] arrayOfObject;
      for (Object localObject2 = new SingletonImmutableList(localObject1); ; localObject2 = new RegularImmutableList(arrayOfObject))
      {
        return localObject2;
        arrayOfObject = this.elements;
      }
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
      return Iterators.forArray(this.elements);
    }

    public int size()
    {
      return this.elements.length;
    }
  }

  private static class EmptyImmutableCollection extends ImmutableCollection<Object>
  {
    private static final Object[] EMPTY_ARRAY = new Object[0];

    public boolean contains(Object paramObject)
    {
      return false;
    }

    ImmutableList<Object> createAsList()
    {
      return ImmutableList.of();
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
      return Iterators.EMPTY_ITERATOR;
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
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableCollection
 * JD-Core Version:    0.6.2
 */