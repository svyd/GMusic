package com.google.android.music.sync.google;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ListPartitioner<T>
{
  HashSet<Class<? extends T>> mPartitioningClasses;

  public ListPartitioner()
  {
    HashSet localHashSet = Sets.newHashSet();
    this.mPartitioningClasses = localHashSet;
  }

  private int findFirstIndexOfUnlikeElement(Class<? extends T> paramClass, List<T> paramList)
  {
    int i = 0;
    int j = paramList.size();
    while (true)
    {
      if (i < j)
      {
        Object localObject = paramList.get(i);
        if (paramClass.isInstance(localObject));
      }
      else
      {
        return i;
      }
      i += 1;
    }
  }

  public void addPartitioningClass(Class<? extends T> paramClass)
  {
    boolean bool = this.mPartitioningClasses.add(paramClass);
  }

  public List<List<? extends T>> partition(List<T> paramList)
  {
    ArrayList localArrayList = Lists.newArrayList();
    if ((paramList == null) || (paramList.isEmpty()))
      return localArrayList;
    Object localObject1;
    do
    {
      int i = findFirstIndexOfUnlikeElement((Class)localObject1, paramList);
      List localList = paramList.subList(0, i);
      boolean bool = localArrayList.add(localList);
      int j = paramList.size();
      paramList = paramList.subList(i, j);
      if (paramList.isEmpty())
        break;
      localObject1 = null;
      Iterator localIterator = this.mPartitioningClasses.iterator();
      while (localIterator.hasNext())
      {
        Class localClass = (Class)localIterator.next();
        Object localObject2 = paramList.get(0);
        if (localClass.isInstance(localObject2))
          localObject1 = localClass;
      }
    }
    while (localObject1 != null);
    StringBuilder localStringBuilder = new StringBuilder().append("Trying to partition a list with an unknown element type:");
    String str1 = paramList.get(0).getClass().toString();
    String str2 = str1;
    throw new IllegalArgumentException(str2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.ListPartitioner
 * JD-Core Version:    0.6.2
 */