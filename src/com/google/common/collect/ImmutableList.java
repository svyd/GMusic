package com.google.common.collect;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

public abstract class ImmutableList<E> extends ImmutableCollection<E>
  implements List<E>, RandomAccess
{
  private static Object checkElementNotNull(Object paramObject, int paramInt)
  {
    if (paramObject == null)
    {
      String str = "at index " + paramInt;
      throw new NullPointerException(str);
    }
    return paramObject;
  }

  private static <E> ImmutableList<E> construct(Object[] paramArrayOfObject)
  {
    int i = 0;
    while (true)
    {
      int j = paramArrayOfObject.length;
      if (i >= j)
        break;
      Object localObject = checkElementNotNull(paramArrayOfObject[i], i);
      i += 1;
    }
    return new RegularImmutableList(paramArrayOfObject);
  }

  private static <E> ImmutableList<E> copyFromCollection(Collection<? extends E> paramCollection)
  {
    Object[] arrayOfObject = paramCollection.toArray();
    Object localObject1;
    switch (arrayOfObject.length)
    {
    default:
      localObject1 = construct(arrayOfObject);
    case 0:
    case 1:
    }
    while (true)
    {
      return localObject1;
      localObject1 = of();
      continue;
      Object localObject2 = arrayOfObject[0];
      localObject1 = new SingletonImmutableList(localObject2);
    }
  }

  public static <E> ImmutableList<E> copyOf(Collection<? extends E> paramCollection)
  {
    if ((paramCollection instanceof ImmutableCollection))
    {
      localImmutableList = ((ImmutableCollection)paramCollection).asList();
      if (!localImmutableList.isPartialView());
    }
    for (ImmutableList localImmutableList = copyFromCollection(localImmutableList); ; localImmutableList = copyFromCollection(paramCollection))
      return localImmutableList;
  }

  public static <E> ImmutableList<E> copyOf(E[] paramArrayOfE)
  {
    Object localObject;
    switch (paramArrayOfE.length)
    {
    default:
      localObject = construct((Object[])paramArrayOfE.clone());
    case 0:
    case 1:
    }
    while (true)
    {
      return localObject;
      localObject = of();
      continue;
      E ? = paramArrayOfE[0];
      localObject = new SingletonImmutableList(?);
    }
  }

  public static <E> ImmutableList<E> of()
  {
    return EmptyImmutableList.INSTANCE;
  }

  public static <E> ImmutableList<E> of(E paramE)
  {
    return new SingletonImmutableList(paramE);
  }

  public static <E> ImmutableList<E> of(E paramE1, E paramE2)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = paramE1;
    arrayOfObject[1] = paramE2;
    return construct(arrayOfObject);
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws InvalidObjectException
  {
    throw new InvalidObjectException("Use SerializedForm");
  }

  public final void add(int paramInt, E paramE)
  {
    throw new UnsupportedOperationException();
  }

  public final boolean addAll(int paramInt, Collection<? extends E> paramCollection)
  {
    throw new UnsupportedOperationException();
  }

  public ImmutableList<E> asList()
  {
    return this;
  }

  public boolean equals(Object paramObject)
  {
    return Lists.equalsImpl(this, paramObject);
  }

  public int hashCode()
  {
    return Lists.hashCodeImpl(this);
  }

  public UnmodifiableIterator<E> iterator()
  {
    return listIterator();
  }

  public UnmodifiableListIterator<E> listIterator()
  {
    return listIterator(0);
  }

  public abstract UnmodifiableListIterator<E> listIterator(int paramInt);

  public final E remove(int paramInt)
  {
    throw new UnsupportedOperationException();
  }

  public final E set(int paramInt, E paramE)
  {
    throw new UnsupportedOperationException();
  }

  public abstract ImmutableList<E> subList(int paramInt1, int paramInt2);

  Object writeReplace()
  {
    Object[] arrayOfObject = toArray();
    return new SerializedForm(arrayOfObject);
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
      return ImmutableList.copyOf(this.elements);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableList
 * JD-Core Version:    0.6.2
 */