package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Comparator;

final class ImmutableSortedAsList<E> extends ImmutableList<E>
  implements SortedIterable<E>
{
  private final transient ImmutableList<E> backingList;
  private final transient ImmutableSortedSet<E> backingSet;

  ImmutableSortedAsList(ImmutableSortedSet<E> paramImmutableSortedSet, ImmutableList<E> paramImmutableList)
  {
    this.backingSet = paramImmutableSortedSet;
    this.backingList = paramImmutableList;
  }

  public Comparator<? super E> comparator()
  {
    return this.backingSet.comparator();
  }

  public boolean contains(Object paramObject)
  {
    if (this.backingSet.indexOf(paramObject) >= 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean equals(Object paramObject)
  {
    return this.backingList.equals(paramObject);
  }

  public E get(int paramInt)
  {
    return this.backingList.get(paramInt);
  }

  public int hashCode()
  {
    return this.backingList.hashCode();
  }

  public int indexOf(Object paramObject)
  {
    return this.backingSet.indexOf(paramObject);
  }

  boolean isPartialView()
  {
    return this.backingList.isPartialView();
  }

  public UnmodifiableIterator<E> iterator()
  {
    return this.backingList.iterator();
  }

  public int lastIndexOf(Object paramObject)
  {
    return this.backingSet.indexOf(paramObject);
  }

  public UnmodifiableListIterator<E> listIterator()
  {
    return this.backingList.listIterator();
  }

  public UnmodifiableListIterator<E> listIterator(int paramInt)
  {
    return this.backingList.listIterator(paramInt);
  }

  public int size()
  {
    return this.backingList.size();
  }

  public ImmutableList<E> subList(int paramInt1, int paramInt2)
  {
    int i = size();
    Preconditions.checkPositionIndexes(paramInt1, paramInt2, i);
    if (paramInt1 != paramInt2);
    ImmutableList localImmutableList2;
    Comparator localComparator;
    for (ImmutableList localImmutableList1 = ImmutableList.of(); ; localImmutableList1 = new RegularImmutableSortedSet(localImmutableList2, localComparator).asList())
    {
      return localImmutableList1;
      localImmutableList2 = this.backingList.subList(paramInt1, paramInt2);
      localComparator = this.backingSet.comparator();
    }
  }

  Object writeReplace()
  {
    ImmutableSortedSet localImmutableSortedSet = this.backingSet;
    return new ImmutableAsList.SerializedForm(localImmutableSortedSet);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableSortedAsList
 * JD-Core Version:    0.6.2
 */