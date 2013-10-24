package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;

public abstract class ImmutableSortedSet<E> extends ImmutableSortedSetFauxverideShim<E>
  implements SortedSet<E>, SortedIterable<E>
{
  private static final ImmutableSortedSet<Comparable> NATURAL_EMPTY_SET = new EmptyImmutableSortedSet(localComparator);
  private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
  final transient Comparator<? super E> comparator;

  static
  {
    Comparator localComparator = NATURAL_ORDER;
  }

  ImmutableSortedSet(Comparator<? super E> paramComparator)
  {
    this.comparator = paramComparator;
  }

  private static <E> ImmutableSortedSet<E> copyOfInternal(Comparator<? super E> paramComparator, Iterator<? extends E> paramIterator)
  {
    ImmutableList localImmutableList = ImmutableList.copyOf(SortedIterables.sortedUnique(paramComparator, paramIterator));
    if (localImmutableList.isEmpty());
    for (Object localObject = emptySet(paramComparator); ; localObject = new RegularImmutableSortedSet(localImmutableList, paramComparator))
      return localObject;
  }

  private static <E> ImmutableSortedSet<E> emptySet()
  {
    return NATURAL_EMPTY_SET;
  }

  static <E> ImmutableSortedSet<E> emptySet(Comparator<? super E> paramComparator)
  {
    if (NATURAL_ORDER.equals(paramComparator));
    for (Object localObject = emptySet(); ; localObject = new EmptyImmutableSortedSet(paramComparator))
      return localObject;
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws InvalidObjectException
  {
    throw new InvalidObjectException("Use SerializedForm");
  }

  static int unsafeCompare(Comparator<?> paramComparator, Object paramObject1, Object paramObject2)
  {
    return paramComparator.compare(paramObject1, paramObject2);
  }

  public Comparator<? super E> comparator()
  {
    return this.comparator;
  }

  public ImmutableSortedSet<E> headSet(E paramE)
  {
    return headSet(paramE, false);
  }

  ImmutableSortedSet<E> headSet(E paramE, boolean paramBoolean)
  {
    Object localObject = Preconditions.checkNotNull(paramE);
    return headSetImpl(localObject, paramBoolean);
  }

  abstract ImmutableSortedSet<E> headSetImpl(E paramE, boolean paramBoolean);

  abstract int indexOf(Object paramObject);

  public abstract UnmodifiableIterator<E> iterator();

  public ImmutableSortedSet<E> subSet(E paramE1, E paramE2)
  {
    return subSet(paramE1, true, paramE2, false);
  }

  ImmutableSortedSet<E> subSet(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2)
  {
    Object localObject1 = Preconditions.checkNotNull(paramE1);
    Object localObject2 = Preconditions.checkNotNull(paramE2);
    if (this.comparator.compare(paramE1, paramE2) <= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return subSetImpl(paramE1, paramBoolean1, paramE2, paramBoolean2);
    }
  }

  abstract ImmutableSortedSet<E> subSetImpl(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2);

  public ImmutableSortedSet<E> tailSet(E paramE)
  {
    return tailSet(paramE, true);
  }

  ImmutableSortedSet<E> tailSet(E paramE, boolean paramBoolean)
  {
    Object localObject = Preconditions.checkNotNull(paramE);
    return tailSetImpl(localObject, paramBoolean);
  }

  abstract ImmutableSortedSet<E> tailSetImpl(E paramE, boolean paramBoolean);

  int unsafeCompare(Object paramObject1, Object paramObject2)
  {
    return unsafeCompare(this.comparator, paramObject1, paramObject2);
  }

  Object writeReplace()
  {
    Comparator localComparator = this.comparator;
    Object[] arrayOfObject = toArray();
    return new SerializedForm(localComparator, arrayOfObject);
  }

  private static class SerializedForm<E>
    implements Serializable
  {
    private static final long serialVersionUID;
    final Comparator<? super E> comparator;
    final Object[] elements;

    public SerializedForm(Comparator<? super E> paramComparator, Object[] paramArrayOfObject)
    {
      this.comparator = paramComparator;
      this.elements = paramArrayOfObject;
    }

    Object readResolve()
    {
      Comparator localComparator = this.comparator;
      ImmutableSortedSet.Builder localBuilder = new ImmutableSortedSet.Builder(localComparator);
      Object[] arrayOfObject = (Object[])this.elements;
      return localBuilder.add(arrayOfObject).build();
    }
  }

  public static final class Builder<E> extends ImmutableSet.Builder<E>
  {
    private final Comparator<? super E> comparator;

    public Builder(Comparator<? super E> paramComparator)
    {
      Comparator localComparator = (Comparator)Preconditions.checkNotNull(paramComparator);
      this.comparator = localComparator;
    }

    public Builder<E> add(E paramE)
    {
      ImmutableSet.Builder localBuilder = super.add(paramE);
      return this;
    }

    public Builder<E> add(E[] paramArrayOfE)
    {
      ImmutableSet.Builder localBuilder = super.add(paramArrayOfE);
      return this;
    }

    public ImmutableSortedSet<E> build()
    {
      Comparator localComparator = this.comparator;
      Iterator localIterator = this.contents.iterator();
      return ImmutableSortedSet.copyOfInternal(localComparator, localIterator);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableSortedSet
 * JD-Core Version:    0.6.2
 */