package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public abstract class ImmutableSet<E> extends ImmutableCollection<E>
  implements Set<E>
{
  static int chooseTableSize(int paramInt)
  {
    int i;
    if (paramInt < 536870912)
    {
      i = Integer.highestOneBit(paramInt) << 2;
      return i;
    }
    if (paramInt < 1073741824);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool, "collection too large");
      i = 1073741824;
      break;
    }
  }

  private static <E> ImmutableSet<E> construct(Object[] paramArrayOfObject)
  {
    int i = chooseTableSize(paramArrayOfObject.length);
    Object[] arrayOfObject1 = new Object[i];
    int j = i + -1;
    ArrayList localArrayList = null;
    int k = 0;
    int m = 0;
    int n = paramArrayOfObject.length;
    if (m < n)
    {
      Object localObject1 = paramArrayOfObject[m];
      int i1 = localObject1.hashCode();
      int i2 = Hashing.smear(i1);
      while (true)
      {
        int i3 = i2 & j;
        Object localObject2 = arrayOfObject1[i3];
        if (localObject2 == null)
        {
          if (localArrayList != null)
            boolean bool1 = localArrayList.add(localObject1);
          arrayOfObject1[i3] = localObject1;
          k += i1;
        }
        while (true)
        {
          m += 1;
          break;
          if (!localObject2.equals(localObject1))
            break label173;
          if (localArrayList == null)
          {
            int i4 = paramArrayOfObject.length;
            localArrayList = new ArrayList(i4);
            int i5 = 0;
            while (i5 < m)
            {
              Object localObject3 = paramArrayOfObject[i5];
              boolean bool2 = localArrayList.add(localObject3);
              i5 += 1;
            }
          }
        }
        label173: i2 += 1;
      }
    }
    Object[] arrayOfObject2;
    Object localObject5;
    if (localArrayList == null)
    {
      arrayOfObject2 = paramArrayOfObject;
      int i6 = arrayOfObject2.length;
      int i7 = 1;
      if (i6 != i7)
        break label238;
      Object localObject4 = arrayOfObject2[0];
      localObject5 = new com/google/common/collect/SingletonImmutableSet;
      ((SingletonImmutableSet)localObject5).<init>(localObject4, k);
    }
    while (true)
    {
      return localObject5;
      arrayOfObject2 = localArrayList.toArray();
      break;
      label238: int i8 = chooseTableSize(arrayOfObject2.length) * 2;
      if (i > i8)
      {
        localObject5 = construct(arrayOfObject2);
      }
      else
      {
        localObject5 = new com/google/common/collect/RegularImmutableSet;
        ((RegularImmutableSet)localObject5).<init>(arrayOfObject2, k, arrayOfObject1, j);
      }
    }
  }

  private static <E> ImmutableSet<E> copyFromCollection(Collection<? extends E> paramCollection)
  {
    Object[] arrayOfObject = paramCollection.toArray();
    ImmutableSet localImmutableSet;
    switch (arrayOfObject.length)
    {
    default:
      localImmutableSet = construct(arrayOfObject);
    case 0:
    case 1:
    }
    while (true)
    {
      return localImmutableSet;
      localImmutableSet = of();
      continue;
      localImmutableSet = of(arrayOfObject[0]);
    }
  }

  public static <E> ImmutableSet<E> copyOf(Collection<? extends E> paramCollection)
  {
    ImmutableSet localImmutableSet;
    if (((paramCollection instanceof ImmutableSet)) && (!(paramCollection instanceof ImmutableSortedSet)))
    {
      localImmutableSet = (ImmutableSet)paramCollection;
      if (localImmutableSet.isPartialView());
    }
    while (true)
    {
      return localImmutableSet;
      localImmutableSet = copyFromCollection(paramCollection);
    }
  }

  public static <E> ImmutableSet<E> copyOf(E[] paramArrayOfE)
  {
    ImmutableSet localImmutableSet;
    switch (paramArrayOfE.length)
    {
    default:
      localImmutableSet = construct((Object[])paramArrayOfE.clone());
    case 0:
    case 1:
    }
    while (true)
    {
      return localImmutableSet;
      localImmutableSet = of();
      continue;
      localImmutableSet = of(paramArrayOfE[0]);
    }
  }

  public static <E> ImmutableSet<E> of()
  {
    return EmptyImmutableSet.INSTANCE;
  }

  public static <E> ImmutableSet<E> of(E paramE)
  {
    return new SingletonImmutableSet(paramE);
  }

  public static <E> ImmutableSet<E> of(E paramE1, E paramE2, E paramE3)
  {
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = paramE1;
    arrayOfObject[1] = paramE2;
    arrayOfObject[2] = paramE3;
    return construct(arrayOfObject);
  }

  public boolean equals(Object paramObject)
  {
    boolean bool;
    if (paramObject == this)
      bool = true;
    while (true)
    {
      return bool;
      if (((paramObject instanceof ImmutableSet)) && (isHashCodeFast()) && (((ImmutableSet)paramObject).isHashCodeFast()))
      {
        int i = hashCode();
        int j = paramObject.hashCode();
        if (i != j)
          bool = false;
      }
      else
      {
        bool = Sets.equalsImpl(this, paramObject);
      }
    }
  }

  public int hashCode()
  {
    return Sets.hashCodeImpl(this);
  }

  boolean isHashCodeFast()
  {
    return false;
  }

  public abstract UnmodifiableIterator<E> iterator();

  Object writeReplace()
  {
    Object[] arrayOfObject = toArray();
    return new SerializedForm(arrayOfObject);
  }

  public static class Builder<E> extends ImmutableCollection.Builder<E>
  {
    final ArrayList<E> contents;

    public Builder()
    {
      ArrayList localArrayList = Lists.newArrayList();
      this.contents = localArrayList;
    }

    public Builder<E> add(E paramE)
    {
      ArrayList localArrayList = this.contents;
      Object localObject = Preconditions.checkNotNull(paramE);
      boolean bool = localArrayList.add(localObject);
      return this;
    }

    public Builder<E> add(E[] paramArrayOfE)
    {
      ArrayList localArrayList = this.contents;
      int i = this.contents.size();
      int j = paramArrayOfE.length;
      int k = i + j;
      localArrayList.ensureCapacity(k);
      ImmutableCollection.Builder localBuilder = super.add(paramArrayOfE);
      return this;
    }

    public ImmutableSet<E> build()
    {
      return ImmutableSet.copyOf(this.contents);
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
      return ImmutableSet.copyOf(this.elements);
    }
  }

  static abstract class TransformedImmutableSet<D, E> extends ImmutableSet<E>
  {
    final int hashCode;
    final D[] source;

    TransformedImmutableSet(D[] paramArrayOfD, int paramInt)
    {
      this.source = paramArrayOfD;
      this.hashCode = paramInt;
    }

    public final int hashCode()
    {
      return this.hashCode;
    }

    public boolean isEmpty()
    {
      return false;
    }

    boolean isHashCodeFast()
    {
      return true;
    }

    public UnmodifiableIterator<E> iterator()
    {
      int i = this.source.length;
      return new AbstractIndexedListIterator(i)
      {
        protected E get(int paramAnonymousInt)
        {
          ImmutableSet.TransformedImmutableSet localTransformedImmutableSet = ImmutableSet.TransformedImmutableSet.this;
          Object localObject = ImmutableSet.TransformedImmutableSet.this.source[paramAnonymousInt];
          return localTransformedImmutableSet.transform(localObject);
        }
      };
    }

    public int size()
    {
      return this.source.length;
    }

    public Object[] toArray()
    {
      Object[] arrayOfObject = new Object[size()];
      return toArray(arrayOfObject);
    }

    public <T> T[] toArray(T[] paramArrayOfT)
    {
      int i = size();
      if (paramArrayOfT.length < i)
        paramArrayOfT = ObjectArrays.newArray(paramArrayOfT, i);
      while (true)
      {
        T[] arrayOfT = paramArrayOfT;
        int j = 0;
        while (true)
        {
          int k = this.source.length;
          if (j >= k)
            break;
          Object localObject1 = this.source[j];
          Object localObject2 = transform(localObject1);
          arrayOfT[j] = localObject2;
          j += 1;
        }
        if (paramArrayOfT.length > i)
          paramArrayOfT[i] = null;
      }
      return paramArrayOfT;
    }

    abstract E transform(D paramD);
  }

  static abstract class ArrayImmutableSet<E> extends ImmutableSet<E>
  {
    final transient Object[] elements;

    ArrayImmutableSet(Object[] paramArrayOfObject)
    {
      this.elements = paramArrayOfObject;
    }

    public boolean containsAll(Collection<?> paramCollection)
    {
      boolean bool = true;
      if (paramCollection == this);
      label101: 
      while (true)
      {
        return bool;
        if (!(paramCollection instanceof ArrayImmutableSet))
        {
          bool = super.containsAll(paramCollection);
        }
        else
        {
          int i = paramCollection.size();
          int j = size();
          if (i > j)
          {
            bool = false;
          }
          else
          {
            Object[] arrayOfObject = ((ArrayImmutableSet)paramCollection).elements;
            int k = arrayOfObject.length;
            int m = 0;
            while (true)
            {
              if (m >= k)
                break label101;
              Object localObject = arrayOfObject[m];
              if (!contains(localObject))
              {
                bool = false;
                break;
              }
              m += 1;
            }
          }
        }
      }
    }

    ImmutableList<E> createAsList()
    {
      Object[] arrayOfObject = this.elements;
      return new ImmutableAsList(arrayOfObject, this);
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

    public Object[] toArray()
    {
      Object[] arrayOfObject1 = new Object[size()];
      Object[] arrayOfObject2 = this.elements;
      int i = size();
      System.arraycopy(arrayOfObject2, 0, arrayOfObject1, 0, i);
      return arrayOfObject1;
    }

    public <T> T[] toArray(T[] paramArrayOfT)
    {
      int i = size();
      if (paramArrayOfT.length < i)
        paramArrayOfT = ObjectArrays.newArray(paramArrayOfT, i);
      while (true)
      {
        System.arraycopy(this.elements, 0, paramArrayOfT, 0, i);
        return paramArrayOfT;
        if (paramArrayOfT.length > i)
          paramArrayOfT[i] = null;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableSet
 * JD-Core Version:    0.6.2
 */