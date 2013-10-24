package com.google.common.collect;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import java.util.Map.Entry;

final class RegularImmutableMap<K, V> extends ImmutableMap<K, V>
{
  private static final long serialVersionUID;
  private final transient LinkedEntry<K, V>[] entries;
  private transient ImmutableSet<Map.Entry<K, V>> entrySet;
  private transient ImmutableSet<K> keySet;
  private final transient int keySetHashCode;
  private final transient int mask;
  private final transient LinkedEntry<K, V>[] table;
  private transient ImmutableCollection<V> values;

  RegularImmutableMap(Map.Entry<?, ?>[] paramArrayOfEntry)
  {
    int i = paramArrayOfEntry.length;
    LinkedEntry[] arrayOfLinkedEntry1 = createEntryArray(i);
    this.entries = arrayOfLinkedEntry1;
    int j = chooseTableSize(i);
    LinkedEntry[] arrayOfLinkedEntry2 = createEntryArray(j);
    this.table = arrayOfLinkedEntry2;
    int k = j + -1;
    this.mask = k;
    int m = 0;
    int n = 0;
    while (n < i)
    {
      Map.Entry<?, ?> localEntry = paramArrayOfEntry[n];
      Object localObject1 = localEntry.getKey();
      int i1 = localObject1.hashCode();
      m += i1;
      int i2 = Hashing.smear(i1);
      int i3 = this.mask;
      int i4 = i2 & i3;
      LinkedEntry localLinkedEntry1 = this.table[i4];
      Object localObject2 = localEntry.getValue();
      LinkedEntry localLinkedEntry2 = newLinkedEntry(localObject1, localObject2, localLinkedEntry1);
      this.table[i4] = localLinkedEntry2;
      this.entries[n] = localLinkedEntry2;
      if (localLinkedEntry1 != null)
      {
        Object localObject3 = localLinkedEntry1.getKey();
        if (!localObject1.equals(localObject3));
        for (boolean bool = true; ; bool = false)
        {
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = localObject1;
          Preconditions.checkArgument(bool, "duplicate key: %s", arrayOfObject);
          localLinkedEntry1 = localLinkedEntry1.next();
          break;
        }
      }
      n += 1;
    }
    this.keySetHashCode = m;
  }

  private static int chooseTableSize(int paramInt)
  {
    int i = Integer.highestOneBit(paramInt) << 1;
    if (i > 0);
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      Integer localInteger = Integer.valueOf(paramInt);
      arrayOfObject[0] = localInteger;
      Preconditions.checkArgument(bool, "table too large: %s", arrayOfObject);
      return i;
    }
  }

  private LinkedEntry<K, V>[] createEntryArray(int paramInt)
  {
    return new LinkedEntry[paramInt];
  }

  private static <K, V> LinkedEntry<K, V> newLinkedEntry(K paramK, V paramV, LinkedEntry<K, V> paramLinkedEntry)
  {
    if (paramLinkedEntry == null);
    for (Object localObject = new TerminalEntry(paramK, paramV); ; localObject = new NonTerminalEntry(paramK, paramV, paramLinkedEntry))
      return localObject;
  }

  public boolean containsValue(Object paramObject)
  {
    boolean bool = false;
    if (paramObject == null);
    label55: 
    while (true)
    {
      return bool;
      LinkedEntry[] arrayOfLinkedEntry = this.entries;
      int i = arrayOfLinkedEntry.length;
      int j = 0;
      while (true)
      {
        if (j >= i)
          break label55;
        if (arrayOfLinkedEntry[j].getValue().equals(paramObject))
        {
          bool = true;
          break;
        }
        j += 1;
      }
    }
  }

  public ImmutableSet<Map.Entry<K, V>> entrySet()
  {
    Object localObject = this.entrySet;
    if (localObject == null)
    {
      localObject = new EntrySet(this);
      this.entrySet = ((ImmutableSet)localObject);
    }
    return localObject;
  }

  public V get(Object paramObject)
  {
    Object localObject1 = null;
    if (paramObject == null);
    label81: 
    while (true)
    {
      return localObject1;
      int i = Hashing.smear(paramObject.hashCode());
      int j = this.mask;
      int k = i & j;
      for (LinkedEntry localLinkedEntry = this.table[k]; ; localLinkedEntry = localLinkedEntry.next())
      {
        if (localLinkedEntry == null)
          break label81;
        Object localObject2 = localLinkedEntry.getKey();
        if (paramObject.equals(localObject2))
        {
          localObject1 = localLinkedEntry.getValue();
          break;
        }
      }
    }
  }

  public boolean isEmpty()
  {
    return false;
  }

  public ImmutableSet<K> keySet()
  {
    Object localObject = this.keySet;
    if (localObject == null)
    {
      localObject = new KeySet(this);
      this.keySet = ((ImmutableSet)localObject);
    }
    return localObject;
  }

  public int size()
  {
    return this.entries.length;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = Collections2.newStringBuilderForCollection(size()).append('{');
    Joiner localJoiner = Collections2.STANDARD_JOINER;
    LinkedEntry[] arrayOfLinkedEntry = this.entries;
    StringBuilder localStringBuilder2 = localJoiner.appendTo(localStringBuilder1, arrayOfLinkedEntry);
    return '}';
  }

  public ImmutableCollection<V> values()
  {
    Object localObject = this.values;
    if (localObject == null)
    {
      localObject = new Values(this);
      this.values = ((ImmutableCollection)localObject);
    }
    return localObject;
  }

  private static class Values<V> extends ImmutableCollection<V>
  {
    final RegularImmutableMap<?, V> map;

    Values(RegularImmutableMap<?, V> paramRegularImmutableMap)
    {
      this.map = paramRegularImmutableMap;
    }

    public boolean contains(Object paramObject)
    {
      return this.map.containsValue(paramObject);
    }

    boolean isPartialView()
    {
      return true;
    }

    public UnmodifiableIterator<V> iterator()
    {
      int i = this.map.entries.length;
      return new AbstractIndexedListIterator(i)
      {
        protected V get(int paramAnonymousInt)
        {
          return RegularImmutableMap.Values.this.map.entries[paramAnonymousInt].getValue();
        }
      };
    }

    public int size()
    {
      return this.map.entries.length;
    }
  }

  private static class KeySet<K, V> extends ImmutableSet.TransformedImmutableSet<Map.Entry<K, V>, K>
  {
    final RegularImmutableMap<K, V> map;

    KeySet(RegularImmutableMap<K, V> paramRegularImmutableMap)
    {
      super(i);
      this.map = paramRegularImmutableMap;
    }

    public boolean contains(Object paramObject)
    {
      return this.map.containsKey(paramObject);
    }

    boolean isPartialView()
    {
      return true;
    }

    K transform(Map.Entry<K, V> paramEntry)
    {
      return paramEntry.getKey();
    }
  }

  private static class EntrySet<K, V> extends ImmutableSet.ArrayImmutableSet<Map.Entry<K, V>>
  {
    final transient RegularImmutableMap<K, V> map;

    EntrySet(RegularImmutableMap<K, V> paramRegularImmutableMap)
    {
      super();
      this.map = paramRegularImmutableMap;
    }

    public boolean contains(Object paramObject)
    {
      boolean bool = false;
      if ((paramObject instanceof Map.Entry))
      {
        Map.Entry localEntry = (Map.Entry)paramObject;
        RegularImmutableMap localRegularImmutableMap = this.map;
        Object localObject1 = localEntry.getKey();
        Object localObject2 = localRegularImmutableMap.get(localObject1);
        if (localObject2 != null)
        {
          Object localObject3 = localEntry.getValue();
          if (localObject2.equals(localObject3))
            bool = true;
        }
      }
      return bool;
    }
  }

  private static final class TerminalEntry<K, V> extends ImmutableEntry<K, V>
    implements RegularImmutableMap.LinkedEntry<K, V>
  {
    TerminalEntry(K paramK, V paramV)
    {
      super(paramV);
    }

    public RegularImmutableMap.LinkedEntry<K, V> next()
    {
      return null;
    }
  }

  private static final class NonTerminalEntry<K, V> extends ImmutableEntry<K, V>
    implements RegularImmutableMap.LinkedEntry<K, V>
  {
    final RegularImmutableMap.LinkedEntry<K, V> next;

    NonTerminalEntry(K paramK, V paramV, RegularImmutableMap.LinkedEntry<K, V> paramLinkedEntry)
    {
      super(paramV);
      this.next = paramLinkedEntry;
    }

    public RegularImmutableMap.LinkedEntry<K, V> next()
    {
      return this.next;
    }
  }

  private static abstract interface LinkedEntry<K, V> extends Map.Entry<K, V>
  {
    public abstract LinkedEntry<K, V> next();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.RegularImmutableMap
 * JD-Core Version:    0.6.2
 */