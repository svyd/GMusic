package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

final class RegularImmutableSortedSet<E> extends ImmutableSortedSet<E>
{
  private final transient ImmutableList<E> elements;

  RegularImmutableSortedSet(ImmutableList<E> paramImmutableList, Comparator<? super E> paramComparator)
  {
    super(paramComparator);
    this.elements = paramImmutableList;
    if (!paramImmutableList.isEmpty());
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return;
    }
  }

  private int binarySearch(Object paramObject)
  {
    Comparator localComparator = this.comparator;
    return Collections.binarySearch(this.elements, paramObject, localComparator);
  }

  private ImmutableSortedSet<E> createSubset(int paramInt1, int paramInt2)
  {
    if (paramInt1 == 0)
    {
      int i = size();
      if (paramInt2 == i);
    }
    while (true)
    {
      return this;
      if (paramInt1 < paramInt2)
      {
        ImmutableList localImmutableList = this.elements.subList(paramInt1, paramInt2);
        Comparator localComparator = this.comparator;
        this = new RegularImmutableSortedSet(localImmutableList, localComparator);
      }
      else
      {
        this = emptySet(this.comparator);
      }
    }
  }

  public boolean contains(Object paramObject)
  {
    boolean bool = false;
    if (paramObject == null);
    while (true)
    {
      return bool;
      try
      {
        int i = binarySearch(paramObject);
        if (i >= 0)
          bool = true;
      }
      catch (ClassCastException localClassCastException)
      {
      }
    }
  }

  public boolean containsAll(Collection<?> paramCollection)
  {
    boolean bool = true;
    if ((!SortedIterables.hasSameComparator(comparator(), paramCollection)) || (paramCollection.size() <= 1))
      bool = super.containsAll(paramCollection);
    while (true)
    {
      return bool;
      UnmodifiableIterator localUnmodifiableIterator = iterator();
      Iterator localIterator = paramCollection.iterator();
      Object localObject1 = localIterator.next();
      try
      {
        int i;
        label111: 
        do
          while (true)
          {
            if (!localUnmodifiableIterator.hasNext())
              break label135;
            Object localObject2 = localUnmodifiableIterator.next();
            i = unsafeCompare(localObject2, localObject1);
            if (i != 0)
              break label111;
            if (!localIterator.hasNext())
              break;
            Object localObject3 = localIterator.next();
            localObject1 = localObject3;
          }
        while (i <= 0);
        bool = false;
      }
      catch (NullPointerException localNullPointerException)
      {
        bool = false;
      }
      catch (ClassCastException localClassCastException)
      {
        bool = false;
      }
      continue;
      label135: bool = false;
    }
  }

  ImmutableList<E> createAsList()
  {
    ImmutableList localImmutableList = this.elements;
    return new ImmutableSortedAsList(this, localImmutableList);
  }

  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this);
    while (true)
    {
      return bool;
      if (!(paramObject instanceof Set))
      {
        bool = false;
      }
      else
      {
        Set localSet = (Set)paramObject;
        int i = size();
        int j = localSet.size();
        if (i != j)
        {
          bool = false;
        }
        else if (SortedIterables.hasSameComparator(this.comparator, localSet))
        {
          Iterator localIterator = localSet.iterator();
          try
          {
            UnmodifiableIterator localUnmodifiableIterator = iterator();
            int k;
            do
            {
              if (!localUnmodifiableIterator.hasNext())
                break;
              Object localObject1 = localUnmodifiableIterator.next();
              Object localObject2 = localIterator.next();
              if (localObject2 == null)
                break label125;
              k = unsafeCompare(localObject1, localObject2);
            }
            while (k == 0);
            label125: bool = false;
          }
          catch (ClassCastException localClassCastException)
          {
            bool = false;
          }
          catch (NoSuchElementException localNoSuchElementException)
          {
            bool = false;
          }
        }
        else
        {
          bool = containsAll(localSet);
        }
      }
    }
  }

  public E first()
  {
    return this.elements.get(0);
  }

  ImmutableSortedSet<E> headSetImpl(E paramE, boolean paramBoolean)
  {
    ImmutableList localImmutableList1;
    Object localObject1;
    Comparator localComparator1;
    SortedLists.KeyPresentBehavior localKeyPresentBehavior1;
    SortedLists.KeyAbsentBehavior localKeyAbsentBehavior1;
    if (paramBoolean)
    {
      localImmutableList1 = this.elements;
      localObject1 = Preconditions.checkNotNull(paramE);
      localComparator1 = comparator();
      localKeyPresentBehavior1 = SortedLists.KeyPresentBehavior.FIRST_AFTER;
      localKeyAbsentBehavior1 = SortedLists.KeyAbsentBehavior.NEXT_HIGHER;
    }
    ImmutableList localImmutableList2;
    Object localObject2;
    Comparator localComparator2;
    SortedLists.KeyPresentBehavior localKeyPresentBehavior2;
    SortedLists.KeyAbsentBehavior localKeyAbsentBehavior2;
    for (int i = SortedLists.binarySearch(localImmutableList1, localObject1, localComparator1, localKeyPresentBehavior1, localKeyAbsentBehavior1); ; i = SortedLists.binarySearch(localImmutableList2, localObject2, localComparator2, localKeyPresentBehavior2, localKeyAbsentBehavior2))
    {
      return createSubset(0, i);
      localImmutableList2 = this.elements;
      localObject2 = Preconditions.checkNotNull(paramE);
      localComparator2 = comparator();
      localKeyPresentBehavior2 = SortedLists.KeyPresentBehavior.FIRST_PRESENT;
      localKeyAbsentBehavior2 = SortedLists.KeyAbsentBehavior.NEXT_HIGHER;
    }
  }

  int indexOf(Object paramObject)
  {
    int i = -1;
    if (paramObject == null)
      return i;
    while (true)
    {
      try
      {
        ImmutableList localImmutableList = this.elements;
        Comparator localComparator = comparator();
        SortedLists.KeyPresentBehavior localKeyPresentBehavior = SortedLists.KeyPresentBehavior.ANY_PRESENT;
        SortedLists.KeyAbsentBehavior localKeyAbsentBehavior = SortedLists.KeyAbsentBehavior.INVERTED_INSERTION_INDEX;
        int j = SortedLists.binarySearch(localImmutableList, paramObject, localComparator, localKeyPresentBehavior, localKeyAbsentBehavior);
        k = j;
        if ((k < 0) || (!this.elements.get(k).equals(paramObject)))
          break label79;
        i = k;
      }
      catch (ClassCastException localClassCastException)
      {
      }
      break;
      label79: int k = -1;
    }
  }

  public boolean isEmpty()
  {
    return false;
  }

  boolean isPartialView()
  {
    return this.elements.isPartialView();
  }

  public UnmodifiableIterator<E> iterator()
  {
    return this.elements.iterator();
  }

  public E last()
  {
    ImmutableList localImmutableList = this.elements;
    int i = size() + -1;
    return localImmutableList.get(i);
  }

  public int size()
  {
    return this.elements.size();
  }

  ImmutableSortedSet<E> subSetImpl(E paramE1, boolean paramBoolean1, E paramE2, boolean paramBoolean2)
  {
    return tailSetImpl(paramE1, paramBoolean1).headSetImpl(paramE2, paramBoolean2);
  }

  ImmutableSortedSet<E> tailSetImpl(E paramE, boolean paramBoolean)
  {
    ImmutableList localImmutableList1;
    Object localObject1;
    Comparator localComparator1;
    SortedLists.KeyPresentBehavior localKeyPresentBehavior1;
    SortedLists.KeyAbsentBehavior localKeyAbsentBehavior1;
    if (paramBoolean)
    {
      localImmutableList1 = this.elements;
      localObject1 = Preconditions.checkNotNull(paramE);
      localComparator1 = comparator();
      localKeyPresentBehavior1 = SortedLists.KeyPresentBehavior.FIRST_PRESENT;
      localKeyAbsentBehavior1 = SortedLists.KeyAbsentBehavior.NEXT_HIGHER;
    }
    ImmutableList localImmutableList2;
    Object localObject2;
    Comparator localComparator2;
    SortedLists.KeyPresentBehavior localKeyPresentBehavior2;
    SortedLists.KeyAbsentBehavior localKeyAbsentBehavior2;
    for (int i = SortedLists.binarySearch(localImmutableList1, localObject1, localComparator1, localKeyPresentBehavior1, localKeyAbsentBehavior1); ; i = SortedLists.binarySearch(localImmutableList2, localObject2, localComparator2, localKeyPresentBehavior2, localKeyAbsentBehavior2))
    {
      int j = size();
      return createSubset(i, j);
      localImmutableList2 = this.elements;
      localObject2 = Preconditions.checkNotNull(paramE);
      localComparator2 = comparator();
      localKeyPresentBehavior2 = SortedLists.KeyPresentBehavior.FIRST_AFTER;
      localKeyAbsentBehavior2 = SortedLists.KeyAbsentBehavior.NEXT_HIGHER;
    }
  }

  public Object[] toArray()
  {
    return this.elements.toArray();
  }

  public <T> T[] toArray(T[] paramArrayOfT)
  {
    return this.elements.toArray(paramArrayOfT);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.RegularImmutableSortedSet
 * JD-Core Version:    0.6.2
 */