package com.google.api.client.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

public class ArrayMap<K, V> extends AbstractMap<K, V>
  implements Cloneable
{
  private Object[] data;
  private ArrayMap<K, V>.EntrySet entrySet;
  private int size;

  public static <K, V> ArrayMap<K, V> create()
  {
    return new ArrayMap();
  }

  private int getDataIndexOfKey(Object paramObject)
  {
    int i = this.size << 1;
    Object[] arrayOfObject = this.data;
    int j = 0;
    Object localObject;
    if (j < i)
    {
      localObject = arrayOfObject[j];
      if (paramObject == null)
        if (localObject != null)
          break label48;
    }
    while (true)
    {
      return j;
      if (!paramObject.equals(localObject))
      {
        label48: j += 2;
        break;
        j = -1;
      }
    }
  }

  private V removeFromDataIndexOfKey(int paramInt)
  {
    int i = this.size << 1;
    Object localObject;
    if ((paramInt < 0) || (paramInt >= i))
      localObject = null;
    while (true)
    {
      return localObject;
      int j = paramInt + 1;
      localObject = valueAtDataIndex(j);
      Object[] arrayOfObject = this.data;
      int k = i - paramInt + -2;
      if (k != 0)
      {
        int m = paramInt + 2;
        System.arraycopy(arrayOfObject, m, arrayOfObject, paramInt, k);
      }
      int n = this.size + -1;
      this.size = n;
      int i1 = i + -2;
      setData(i1, null, null);
    }
  }

  private void setData(int paramInt, K paramK, V paramV)
  {
    Object[] arrayOfObject = this.data;
    arrayOfObject[paramInt] = paramK;
    int i = paramInt + 1;
    arrayOfObject[i] = paramV;
  }

  private void setDataCapacity(int paramInt)
  {
    if (paramInt == 0)
    {
      this.data = null;
      return;
    }
    int i = this.size;
    Object[] arrayOfObject1 = this.data;
    if (i != 0)
    {
      int j = arrayOfObject1.length;
      if (paramInt != j)
        return;
    }
    Object[] arrayOfObject2 = new Object[paramInt];
    this.data = arrayOfObject2;
    if (i == 0)
      return;
    int k = i << 1;
    System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, k);
  }

  private V valueAtDataIndex(int paramInt)
  {
    if (paramInt < 0);
    for (Object localObject = null; ; localObject = this.data[paramInt])
      return localObject;
  }

  public void clear()
  {
    this.size = 0;
    this.data = null;
  }

  public ArrayMap<K, V> clone()
  {
    try
    {
      localArrayMap = (ArrayMap)super.clone();
      localArrayMap.entrySet = null;
      Object[] arrayOfObject1 = this.data;
      if (arrayOfObject1 != null)
      {
        int i = arrayOfObject1.length;
        Object[] arrayOfObject2 = new Object[i];
        localArrayMap.data = arrayOfObject2;
        System.arraycopy(arrayOfObject1, 0, arrayOfObject2, 0, i);
      }
      return localArrayMap;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
      while (true)
        ArrayMap localArrayMap = null;
    }
  }

  public final boolean containsKey(Object paramObject)
  {
    int i = getDataIndexOfKey(paramObject);
    if (-1 != i);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public final boolean containsValue(Object paramObject)
  {
    int i = this.size << 1;
    Object[] arrayOfObject = this.data;
    int j = 1;
    Object localObject;
    if (j < i)
    {
      localObject = arrayOfObject[j];
      if (paramObject == null)
        if (localObject != null)
          break label51;
    }
    label36: for (boolean bool = true; ; bool = false)
    {
      return bool;
      if (paramObject.equals(localObject))
        break label36;
      label51: j += 2;
      break;
    }
  }

  public final void ensureCapacity(int paramInt)
  {
    Object[] arrayOfObject = this.data;
    int i = paramInt << 1;
    if (arrayOfObject == null);
    for (int j = 0; ; j = arrayOfObject.length)
    {
      if (i <= j)
        return;
      int k = j / 2 * 3 + 1;
      if (k % 2 == 1)
        k += 1;
      if (k < i)
        k = i;
      setDataCapacity(k);
      return;
    }
  }

  public final Set<Map.Entry<K, V>> entrySet()
  {
    EntrySet localEntrySet = this.entrySet;
    if (localEntrySet == null)
    {
      localEntrySet = new EntrySet();
      this.entrySet = localEntrySet;
    }
    return localEntrySet;
  }

  public final V get(Object paramObject)
  {
    int i = getDataIndexOfKey(paramObject) + 1;
    return valueAtDataIndex(i);
  }

  public final int getIndexOfKey(K paramK)
  {
    return getDataIndexOfKey(paramK) >> 1;
  }

  public final K getKey(int paramInt)
  {
    if (paramInt >= 0)
    {
      int i = this.size;
      if (paramInt < i)
        break label18;
    }
    label18: Object[] arrayOfObject;
    int j;
    for (K ? = null; ; ? = arrayOfObject[j])
    {
      return ?;
      arrayOfObject = this.data;
      j = paramInt << 1;
    }
  }

  public final V getValue(int paramInt)
  {
    if (paramInt >= 0)
    {
      int i = this.size;
      if (paramInt < i)
        break label18;
    }
    label18: int j;
    for (Object localObject = null; ; localObject = valueAtDataIndex(j))
    {
      return localObject;
      j = (paramInt << 1) + 1;
    }
  }

  public final V put(K paramK, V paramV)
  {
    int i = getIndexOfKey(paramK);
    if (i == -1)
      i = this.size;
    return set(i, paramK, paramV);
  }

  public final V remove(int paramInt)
  {
    int i = paramInt << 1;
    return removeFromDataIndexOfKey(i);
  }

  public final V remove(Object paramObject)
  {
    int i = getDataIndexOfKey(paramObject);
    return removeFromDataIndexOfKey(i);
  }

  public final V set(int paramInt, V paramV)
  {
    int i = this.size;
    if ((paramInt < 0) || (paramInt >= i))
      throw new IndexOutOfBoundsException();
    int j = (paramInt << 1) + 1;
    Object localObject = valueAtDataIndex(j);
    this.data[j] = paramV;
    return localObject;
  }

  public final V set(int paramInt, K paramK, V paramV)
  {
    if (paramInt < 0)
      throw new IndexOutOfBoundsException();
    int i = paramInt + 1;
    ensureCapacity(i);
    int j = paramInt << 1;
    int k = j + 1;
    Object localObject = valueAtDataIndex(k);
    setData(j, paramK, paramV);
    int m = this.size;
    if (i > m)
      this.size = i;
    return localObject;
  }

  public final int size()
  {
    return this.size;
  }

  final class Entry
    implements Map.Entry<K, V>
  {
    private int index;

    Entry(int arg2)
    {
      int i;
      this.index = i;
    }

    public K getKey()
    {
      ArrayMap localArrayMap = ArrayMap.this;
      int i = this.index;
      return localArrayMap.getKey(i);
    }

    public V getValue()
    {
      ArrayMap localArrayMap = ArrayMap.this;
      int i = this.index;
      return localArrayMap.getValue(i);
    }

    public V setValue(V paramV)
    {
      ArrayMap localArrayMap = ArrayMap.this;
      int i = this.index;
      return localArrayMap.set(i, paramV);
    }
  }

  final class EntryIterator
    implements Iterator<Map.Entry<K, V>>
  {
    private int nextIndex;
    private boolean removed;

    EntryIterator()
    {
    }

    public boolean hasNext()
    {
      int i = this.nextIndex;
      int j = ArrayMap.this.size;
      if (i < j);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public Map.Entry<K, V> next()
    {
      int i = this.nextIndex;
      int j = ArrayMap.this.size;
      if (i != j)
        throw new NoSuchElementException();
      int k = this.nextIndex + 1;
      this.nextIndex = k;
      ArrayMap localArrayMap = ArrayMap.this;
      return new ArrayMap.Entry(localArrayMap, i);
    }

    public void remove()
    {
      int i = this.nextIndex + -1;
      if ((this.removed) || (i < 0))
        throw new IllegalArgumentException();
      Object localObject = ArrayMap.this.remove(i);
      this.removed = true;
    }
  }

  final class EntrySet extends AbstractSet<Map.Entry<K, V>>
  {
    EntrySet()
    {
    }

    public Iterator<Map.Entry<K, V>> iterator()
    {
      ArrayMap localArrayMap = ArrayMap.this;
      return new ArrayMap.EntryIterator(localArrayMap);
    }

    public int size()
    {
      return ArrayMap.this.size;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.util.ArrayMap
 * JD-Core Version:    0.6.2
 */