package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class Lists
{
  static int computeArrayListCapacity(int paramInt)
  {
    if (paramInt >= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      long l1 = paramInt;
      long l2 = 5L + l1;
      long l3 = paramInt / 10;
      return Ints.saturatedCast(l2 + l3);
    }
  }

  static boolean equalsImpl(List<?> paramList, Object paramObject)
  {
    boolean bool = true;
    Object localObject = Preconditions.checkNotNull(paramList);
    if (paramObject == localObject);
    while (true)
    {
      return bool;
      if (!(paramObject instanceof List))
      {
        bool = false;
      }
      else
      {
        List localList = (List)paramObject;
        int i = paramList.size();
        int j = localList.size();
        if (i != j)
        {
          Iterator localIterator1 = paramList.iterator();
          Iterator localIterator2 = localList.iterator();
          if (Iterators.elementsEqual(localIterator1, localIterator2));
        }
        else
        {
          bool = false;
        }
      }
    }
  }

  static int hashCodeImpl(List<?> paramList)
  {
    int i = 1;
    Iterator localIterator = paramList.iterator();
    if (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      int j = i * 31;
      if (localObject == null);
      for (int k = 0; ; k = localObject.hashCode())
      {
        i = j + k;
        break;
      }
    }
    return i;
  }

  public static <E> ArrayList<E> newArrayList()
  {
    return new ArrayList();
  }

  public static <E> ArrayList<E> newArrayList(Iterable<? extends E> paramIterable)
  {
    Object localObject = Preconditions.checkNotNull(paramIterable);
    Collection localCollection;
    if ((paramIterable instanceof Collection))
      localCollection = Collections2.cast(paramIterable);
    for (ArrayList localArrayList = new ArrayList(localCollection); ; localArrayList = newArrayList(paramIterable.iterator()))
      return localArrayList;
  }

  public static <E> ArrayList<E> newArrayList(Iterator<? extends E> paramIterator)
  {
    Object localObject1 = Preconditions.checkNotNull(paramIterator);
    ArrayList localArrayList = newArrayList();
    while (paramIterator.hasNext())
    {
      Object localObject2 = paramIterator.next();
      boolean bool = localArrayList.add(localObject2);
    }
    return localArrayList;
  }

  public static <E> ArrayList<E> newArrayList(E[] paramArrayOfE)
  {
    Object localObject = Preconditions.checkNotNull(paramArrayOfE);
    int i = computeArrayListCapacity(paramArrayOfE.length);
    ArrayList localArrayList = new ArrayList(i);
    boolean bool = Collections.addAll(localArrayList, paramArrayOfE);
    return localArrayList;
  }

  public static <E> ArrayList<E> newArrayListWithCapacity(int paramInt)
  {
    if (paramInt >= 0);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return new ArrayList(paramInt);
    }
  }

  public static <E> LinkedList<E> newLinkedList()
  {
    return new LinkedList();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Lists
 * JD-Core Version:    0.6.2
 */