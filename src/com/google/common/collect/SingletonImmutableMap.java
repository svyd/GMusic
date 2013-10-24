package com.google.common.collect;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

final class SingletonImmutableMap<K, V> extends ImmutableMap<K, V>
{
  private transient Map.Entry<K, V> entry;
  private transient ImmutableSet<Map.Entry<K, V>> entrySet;
  private transient ImmutableSet<K> keySet;
  final transient K singleKey;
  final transient V singleValue;
  private transient ImmutableCollection<V> values;

  SingletonImmutableMap(Map.Entry<K, V> paramEntry)
  {
    this.entry = paramEntry;
    Object localObject1 = paramEntry.getKey();
    this.singleKey = localObject1;
    Object localObject2 = paramEntry.getValue();
    this.singleValue = localObject2;
  }

  private Map.Entry<K, V> entry()
  {
    Map.Entry localEntry = this.entry;
    if (localEntry == null)
    {
      Object localObject1 = this.singleKey;
      Object localObject2 = this.singleValue;
      localEntry = Maps.immutableEntry(localObject1, localObject2);
      this.entry = localEntry;
    }
    return localEntry;
  }

  public boolean containsKey(Object paramObject)
  {
    return this.singleKey.equals(paramObject);
  }

  public boolean containsValue(Object paramObject)
  {
    return this.singleValue.equals(paramObject);
  }

  public ImmutableSet<Map.Entry<K, V>> entrySet()
  {
    ImmutableSet localImmutableSet = this.entrySet;
    if (localImmutableSet == null)
    {
      localImmutableSet = ImmutableSet.of(entry());
      this.entrySet = localImmutableSet;
    }
    return localImmutableSet;
  }

  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (paramObject == this);
    while (true)
    {
      return bool;
      if ((paramObject instanceof Map))
      {
        Map localMap = (Map)paramObject;
        if (localMap.size() != 1)
        {
          bool = false;
        }
        else
        {
          Map.Entry localEntry = (Map.Entry)localMap.entrySet().iterator().next();
          Object localObject1 = this.singleKey;
          Object localObject2 = localEntry.getKey();
          if (localObject1.equals(localObject2))
          {
            Object localObject3 = this.singleValue;
            Object localObject4 = localEntry.getValue();
            if (localObject3.equals(localObject4));
          }
          else
          {
            bool = false;
          }
        }
      }
      else
      {
        bool = false;
      }
    }
  }

  public V get(Object paramObject)
  {
    if (this.singleKey.equals(paramObject));
    for (Object localObject = this.singleValue; ; localObject = null)
      return localObject;
  }

  public int hashCode()
  {
    int i = this.singleKey.hashCode();
    int j = this.singleValue.hashCode();
    return i ^ j;
  }

  public boolean isEmpty()
  {
    return false;
  }

  public ImmutableSet<K> keySet()
  {
    ImmutableSet localImmutableSet = this.keySet;
    if (localImmutableSet == null)
    {
      localImmutableSet = ImmutableSet.of(this.singleKey);
      this.keySet = localImmutableSet;
    }
    return localImmutableSet;
  }

  public int size()
  {
    return 1;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder().append('{');
    String str1 = this.singleKey.toString();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append('=');
    String str2 = this.singleValue.toString();
    return str2 + '}';
  }

  public ImmutableCollection<V> values()
  {
    Object localObject1 = this.values;
    if (localObject1 == null)
    {
      Object localObject2 = this.singleValue;
      localObject1 = new Values(localObject2);
      this.values = ((ImmutableCollection)localObject1);
    }
    return localObject1;
  }

  private static class Values<V> extends ImmutableCollection<V>
  {
    final V singleValue;

    Values(V paramV)
    {
      this.singleValue = paramV;
    }

    public boolean contains(Object paramObject)
    {
      return this.singleValue.equals(paramObject);
    }

    public boolean isEmpty()
    {
      return false;
    }

    boolean isPartialView()
    {
      return true;
    }

    public UnmodifiableIterator<V> iterator()
    {
      return Iterators.singletonIterator(this.singleValue);
    }

    public int size()
    {
      return 1;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.SingletonImmutableMap
 * JD-Core Version:    0.6.2
 */