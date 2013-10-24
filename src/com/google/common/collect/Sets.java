package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public final class Sets
{
  static boolean equalsImpl(Set<?> paramSet, Object paramObject)
  {
    boolean bool1 = true;
    boolean bool2 = false;
    if (paramSet == paramObject)
      bool2 = true;
    while (true)
    {
      return bool2;
      if ((paramObject instanceof Set))
      {
        Set localSet = (Set)paramObject;
        try
        {
          int i = paramSet.size();
          int j = localSet.size();
          if (i != j)
          {
            boolean bool3 = paramSet.containsAll(localSet);
            if (!bool3);
          }
          while (true)
          {
            bool2 = bool1;
            break;
            bool1 = false;
          }
        }
        catch (NullPointerException localNullPointerException)
        {
        }
        catch (ClassCastException localClassCastException)
        {
        }
      }
    }
  }

  static int hashCodeImpl(Set<?> paramSet)
  {
    int i = 0;
    Iterator localIterator = paramSet.iterator();
    if (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      if (localObject != null);
      for (int j = localObject.hashCode(); ; j = 0)
      {
        i += j;
        break;
      }
    }
    return i;
  }

  public static <E> HashSet<E> newHashSet()
  {
    return new HashSet();
  }

  public static <E> HashSet<E> newHashSetWithExpectedSize(int paramInt)
  {
    int i = Maps.capacity(paramInt);
    return new HashSet(i);
  }

  public static <E extends Comparable> TreeSet<E> newTreeSet()
  {
    return new TreeSet();
  }

  public static <E> TreeSet<E> newTreeSet(Comparator<? super E> paramComparator)
  {
    Comparator localComparator = (Comparator)Preconditions.checkNotNull(paramComparator);
    return new TreeSet(localComparator);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Sets
 * JD-Core Version:    0.6.2
 */