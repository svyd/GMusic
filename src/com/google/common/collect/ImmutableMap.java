package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class ImmutableMap<K, V>
  implements Map<K, V>, Serializable
{
  public static <K, V> Builder<K, V> builder()
  {
    return new Builder();
  }

  static <K, V> Map.Entry<K, V> entryOf(K paramK, V paramV)
  {
    Object localObject1 = Preconditions.checkNotNull(paramK, "null key");
    Object localObject2 = Preconditions.checkNotNull(paramV, "null value");
    return Maps.immutableEntry(localObject1, localObject2);
  }

  public static <K, V> ImmutableMap<K, V> of()
  {
    return EmptyImmutableMap.INSTANCE;
  }

  public final void clear()
  {
    throw new UnsupportedOperationException();
  }

  public boolean containsKey(Object paramObject)
  {
    if (get(paramObject) != null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public abstract ImmutableSet<Map.Entry<K, V>> entrySet();

  public boolean equals(Object paramObject)
  {
    boolean bool;
    if (paramObject == this)
      bool = true;
    while (true)
    {
      return bool;
      if ((paramObject instanceof Map))
      {
        Map localMap = (Map)paramObject;
        ImmutableSet localImmutableSet = entrySet();
        Set localSet = localMap.entrySet();
        bool = localImmutableSet.equals(localSet);
      }
      else
      {
        bool = false;
      }
    }
  }

  public abstract V get(Object paramObject);

  public int hashCode()
  {
    return entrySet().hashCode();
  }

  public boolean isEmpty()
  {
    if (size() == 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public abstract ImmutableSet<K> keySet();

  public final V put(K paramK, V paramV)
  {
    throw new UnsupportedOperationException();
  }

  public final void putAll(Map<? extends K, ? extends V> paramMap)
  {
    throw new UnsupportedOperationException();
  }

  public final V remove(Object paramObject)
  {
    throw new UnsupportedOperationException();
  }

  public String toString()
  {
    return Maps.toStringImpl(this);
  }

  public abstract ImmutableCollection<V> values();

  Object writeReplace()
  {
    return new SerializedForm(this);
  }

  static class SerializedForm
    implements Serializable
  {
    private static final long serialVersionUID;
    private final Object[] keys;
    private final Object[] values;

    SerializedForm(ImmutableMap<?, ?> paramImmutableMap)
    {
      Object[] arrayOfObject1 = new Object[paramImmutableMap.size()];
      this.keys = arrayOfObject1;
      Object[] arrayOfObject2 = new Object[paramImmutableMap.size()];
      this.values = arrayOfObject2;
      int i = 0;
      Iterator localIterator = paramImmutableMap.entrySet().iterator();
      while (true)
      {
        if (!localIterator.hasNext())
          return;
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        Object[] arrayOfObject3 = this.keys;
        Object localObject1 = localEntry.getKey();
        arrayOfObject3[i] = localObject1;
        Object[] arrayOfObject4 = this.values;
        Object localObject2 = localEntry.getValue();
        arrayOfObject4[i] = localObject2;
        i += 1;
      }
    }

    Object createMap(ImmutableMap.Builder<Object, Object> paramBuilder)
    {
      int i = 0;
      while (true)
      {
        int j = this.keys.length;
        if (i >= j)
          break;
        Object localObject1 = this.keys[i];
        Object localObject2 = this.values[i];
        ImmutableMap.Builder localBuilder = paramBuilder.put(localObject1, localObject2);
        i += 1;
      }
      return paramBuilder.build();
    }

    Object readResolve()
    {
      ImmutableMap.Builder localBuilder = new ImmutableMap.Builder();
      return createMap(localBuilder);
    }
  }

  public static class Builder<K, V>
  {
    final ArrayList<Map.Entry<K, V>> entries;

    public Builder()
    {
      ArrayList localArrayList = Lists.newArrayList();
      this.entries = localArrayList;
    }

    private static <K, V> ImmutableMap<K, V> fromEntryList(List<Map.Entry<K, V>> paramList)
    {
      Object localObject;
      switch (paramList.size())
      {
      default:
        Map.Entry[] arrayOfEntry1 = new Map.Entry[paramList.size()];
        Map.Entry[] arrayOfEntry2 = (Map.Entry[])paramList.toArray(arrayOfEntry1);
        localObject = new RegularImmutableMap(arrayOfEntry2);
      case 0:
      case 1:
      }
      while (true)
      {
        return localObject;
        localObject = ImmutableMap.of();
        continue;
        Map.Entry localEntry = (Map.Entry)Iterables.getOnlyElement(paramList);
        localObject = new SingletonImmutableMap(localEntry);
      }
    }

    public ImmutableMap<K, V> build()
    {
      return fromEntryList(this.entries);
    }

    public Builder<K, V> put(K paramK, V paramV)
    {
      ArrayList localArrayList = this.entries;
      Map.Entry localEntry = ImmutableMap.entryOf(paramK, paramV);
      boolean bool = localArrayList.add(localEntry);
      return this;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableMap
 * JD-Core Version:    0.6.2
 */