package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.List;

class RegularImmutableList<E> extends ImmutableList<E>
{
  private final transient Object[] array;
  private final transient int offset;
  private final transient int size;

  RegularImmutableList(Object[] paramArrayOfObject)
  {
    this(paramArrayOfObject, 0, i);
  }

  RegularImmutableList(Object[] paramArrayOfObject, int paramInt1, int paramInt2)
  {
    this.offset = paramInt1;
    this.size = paramInt2;
    this.array = paramArrayOfObject;
  }

  public boolean contains(Object paramObject)
  {
    if (indexOf(paramObject) != -1);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this);
    label225: 
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
        int i = size();
        int j = localList.size();
        if (i != j)
        {
          bool = false;
        }
        else
        {
          int k = this.offset;
          int i3;
          if ((paramObject instanceof RegularImmutableList))
          {
            RegularImmutableList localRegularImmutableList = (RegularImmutableList)paramObject;
            int m = localRegularImmutableList.offset;
            while (true)
            {
              int n = localRegularImmutableList.offset;
              int i1 = localRegularImmutableList.size;
              int i2 = n + i1;
              if (m >= i2)
                break;
              Object[] arrayOfObject1 = this.array;
              i3 = k + 1;
              Object localObject1 = arrayOfObject1[k];
              Object localObject2 = localRegularImmutableList.array[m];
              if (!localObject1.equals(localObject2))
              {
                bool = false;
                break;
              }
              m += 1;
              k = i3;
            }
          }
          Iterator localIterator = localList.iterator();
          while (true)
          {
            if (!localIterator.hasNext())
              break label225;
            Object localObject3 = localIterator.next();
            Object[] arrayOfObject2 = this.array;
            i3 = k + 1;
            if (!arrayOfObject2[k].equals(localObject3))
            {
              bool = false;
              break;
            }
            k = i3;
          }
        }
      }
    }
  }

  public E get(int paramInt)
  {
    int i = this.size;
    int j = Preconditions.checkElementIndex(paramInt, i);
    Object[] arrayOfObject = this.array;
    int k = this.offset + paramInt;
    return arrayOfObject[k];
  }

  public int hashCode()
  {
    int i = 1;
    int j = this.offset;
    while (true)
    {
      int k = this.offset;
      int m = this.size;
      int n = k + m;
      if (j >= n)
        break;
      int i1 = i * 31;
      int i2 = this.array[j].hashCode();
      i = i1 + i2;
      j += 1;
    }
    return i;
  }

  public int indexOf(Object paramObject)
  {
    int i;
    int n;
    if (paramObject != null)
    {
      i = this.offset;
      int j = this.offset;
      int k = this.size;
      int m = j + k;
      if (i < m)
        if (this.array[i].equals(paramObject))
          n = this.offset;
    }
    for (int i1 = i - n; ; i1 = -1)
    {
      return i1;
      i += 1;
      break;
    }
  }

  public boolean isEmpty()
  {
    return false;
  }

  boolean isPartialView()
  {
    if (this.offset == 0)
    {
      int i = this.size;
      int j = this.array.length;
      if (i == j)
        break label27;
    }
    label27: for (boolean bool = true; ; bool = false)
      return bool;
  }

  public UnmodifiableIterator<E> iterator()
  {
    Object[] arrayOfObject = this.array;
    int i = this.offset;
    int j = this.size;
    return Iterators.forArray(arrayOfObject, i, j);
  }

  public int lastIndexOf(Object paramObject)
  {
    int k;
    int n;
    if (paramObject != null)
    {
      int i = this.offset;
      int j = this.size;
      k = i + j + -1;
      int m = this.offset;
      if (k >= m)
        if (this.array[k].equals(paramObject))
          n = this.offset;
    }
    for (int i1 = k - n; ; i1 = -1)
    {
      return i1;
      k += -1;
      break;
    }
  }

  public UnmodifiableListIterator<E> listIterator(int paramInt)
  {
    int i = this.size;
    return new AbstractIndexedListIterator(i, paramInt)
    {
      protected E get(int paramAnonymousInt)
      {
        Object[] arrayOfObject = RegularImmutableList.this.array;
        int i = RegularImmutableList.this.offset + paramAnonymousInt;
        return arrayOfObject[i];
      }
    };
  }

  public int size()
  {
    return this.size;
  }

  public ImmutableList<E> subList(int paramInt1, int paramInt2)
  {
    int i = this.size;
    Preconditions.checkPositionIndexes(paramInt1, paramInt2, i);
    if (paramInt1 != paramInt2);
    Object[] arrayOfObject;
    int j;
    int k;
    for (Object localObject = ImmutableList.of(); ; localObject = new RegularImmutableList(arrayOfObject, j, k))
    {
      return localObject;
      arrayOfObject = this.array;
      j = this.offset + paramInt1;
      k = paramInt2 - paramInt1;
    }
  }

  public Object[] toArray()
  {
    Object[] arrayOfObject1 = new Object[size()];
    Object[] arrayOfObject2 = this.array;
    int i = this.offset;
    int j = this.size;
    System.arraycopy(arrayOfObject2, i, arrayOfObject1, 0, j);
    return arrayOfObject1;
  }

  public <T> T[] toArray(T[] paramArrayOfT)
  {
    int i = paramArrayOfT.length;
    int j = this.size;
    if (i < j)
    {
      int k = this.size;
      paramArrayOfT = ObjectArrays.newArray(paramArrayOfT, k);
    }
    while (true)
    {
      Object[] arrayOfObject = this.array;
      int m = this.offset;
      int n = this.size;
      System.arraycopy(arrayOfObject, m, paramArrayOfT, 0, n);
      return paramArrayOfT;
      int i1 = paramArrayOfT.length;
      int i2 = this.size;
      if (i1 > i2)
      {
        int i3 = this.size;
        paramArrayOfT[i3] = null;
      }
    }
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = Collections2.newStringBuilderForCollection(size()).append('[');
    Object[] arrayOfObject = this.array;
    int i = this.offset;
    Object localObject1 = arrayOfObject[i];
    StringBuilder localStringBuilder2 = localStringBuilder1.append(localObject1);
    int j = this.offset + 1;
    while (true)
    {
      int k = this.offset;
      int m = this.size;
      int n = k + m;
      if (j >= n)
        break;
      StringBuilder localStringBuilder3 = localStringBuilder2.append(", ");
      Object localObject2 = this.array[j];
      StringBuilder localStringBuilder4 = localStringBuilder3.append(localObject2);
      j += 1;
    }
    return ']';
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.RegularImmutableList
 * JD-Core Version:    0.6.2
 */