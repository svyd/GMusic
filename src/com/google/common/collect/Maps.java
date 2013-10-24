package com.google.common.collect;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class Maps
{
  static final Joiner.MapJoiner STANDARD_JOINER = Collections2.STANDARD_JOINER.withKeyValueSeparator("=");

  static int capacity(int paramInt)
  {
    boolean bool;
    int i;
    if (paramInt < 3)
      if (paramInt >= 0)
      {
        bool = true;
        Preconditions.checkArgument(bool);
        i = paramInt + 1;
      }
    while (true)
    {
      return i;
      bool = false;
      break;
      if (paramInt < 1073741824)
        i = paramInt / 3 + paramInt;
      else
        i = 2147483647;
    }
  }

  public static <K, V> Map.Entry<K, V> immutableEntry(K paramK, V paramV)
  {
    return new ImmutableEntry(paramK, paramV);
  }

  public static <K, V> HashMap<K, V> newHashMap()
  {
    return new HashMap();
  }

  public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int paramInt)
  {
    int i = capacity(paramInt);
    return new HashMap(i);
  }

  public static <K, V> LinkedHashMap<K, V> newLinkedHashMap()
  {
    return new LinkedHashMap();
  }

  public static <K, V> LinkedHashMap<K, V> newLinkedHashMap(Map<? extends K, ? extends V> paramMap)
  {
    return new LinkedHashMap(paramMap);
  }

  static boolean safeContainsKey(Map<?, ?> paramMap, Object paramObject)
  {
    try
    {
      boolean bool1 = paramMap.containsKey(paramObject);
      bool2 = bool1;
      return bool2;
    }
    catch (ClassCastException localClassCastException)
    {
      while (true)
        boolean bool2 = false;
    }
  }

  static <V> V safeGet(Map<?, V> paramMap, Object paramObject)
  {
    try
    {
      Object localObject1 = paramMap.get(paramObject);
      localObject2 = localObject1;
      return localObject2;
    }
    catch (ClassCastException localClassCastException)
    {
      while (true)
        Object localObject2 = null;
    }
  }

  static String toStringImpl(Map<?, ?> paramMap)
  {
    StringBuilder localStringBuilder1 = Collections2.newStringBuilderForCollection(paramMap.size()).append('{');
    StringBuilder localStringBuilder2 = STANDARD_JOINER.appendTo(localStringBuilder1, paramMap);
    return '}';
  }

  static abstract class EntrySet<K, V> extends AbstractSet<Map.Entry<K, V>>
  {
    public void clear()
    {
      map().clear();
    }

    public boolean contains(Object paramObject)
    {
      boolean bool = false;
      if ((paramObject instanceof Map.Entry))
      {
        Map.Entry localEntry = (Map.Entry)paramObject;
        Object localObject1 = localEntry.getKey();
        Object localObject2 = map().get(localObject1);
        Object localObject3 = localEntry.getValue();
        if ((Objects.equal(localObject2, localObject3)) && ((localObject2 != null) || (map().containsKey(localObject1))))
          bool = true;
      }
      return bool;
    }

    public boolean isEmpty()
    {
      return map().isEmpty();
    }

    abstract Map<K, V> map();

    public boolean remove(Object paramObject)
    {
      Set localSet;
      Object localObject;
      if (contains(paramObject))
      {
        Map.Entry localEntry = (Map.Entry)paramObject;
        localSet = map().keySet();
        localObject = localEntry.getKey();
      }
      for (boolean bool = localSet.remove(localObject); ; bool = false)
        return bool;
    }

    public boolean removeAll(Collection<?> paramCollection)
    {
      boolean bool2;
      Iterator localIterator;
      try
      {
        Collection localCollection = (Collection)Preconditions.checkNotNull(paramCollection);
        boolean bool1 = super.removeAll(localCollection);
        bool2 = bool1;
        return bool2;
      }
      catch (UnsupportedOperationException localUnsupportedOperationException)
      {
        bool2 = true;
        localIterator = paramCollection.iterator();
      }
      while (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        boolean bool3 = remove(localObject);
        bool2 |= bool3;
      }
    }

    public boolean retainAll(Collection<?> paramCollection)
    {
      try
      {
        Collection localCollection = (Collection)Preconditions.checkNotNull(paramCollection);
        boolean bool1 = super.retainAll(localCollection);
        bool2 = bool1;
        return bool2;
      }
      catch (UnsupportedOperationException localUnsupportedOperationException)
      {
        while (true)
        {
          HashSet localHashSet = Sets.newHashSetWithExpectedSize(paramCollection.size());
          Iterator localIterator = paramCollection.iterator();
          while (localIterator.hasNext())
          {
            Object localObject1 = localIterator.next();
            if (contains(localObject1))
            {
              Object localObject2 = ((Map.Entry)localObject1).getKey();
              boolean bool3 = localHashSet.add(localObject2);
            }
          }
          boolean bool2 = map().keySet().retainAll(localHashSet);
        }
      }
    }

    public int size()
    {
      return map().size();
    }
  }

  static abstract class KeySet<K, V> extends AbstractSet<K>
  {
    public void clear()
    {
      map().clear();
    }

    public boolean contains(Object paramObject)
    {
      return map().containsKey(paramObject);
    }

    public boolean isEmpty()
    {
      return map().isEmpty();
    }

    public Iterator<K> iterator()
    {
      Iterator localIterator = map().entrySet().iterator();
      Function local1 = new Function()
      {
        public K apply(Map.Entry<K, V> paramAnonymousEntry)
        {
          return paramAnonymousEntry.getKey();
        }
      };
      return Iterators.transform(localIterator, local1);
    }

    abstract Map<K, V> map();

    public boolean remove(Object paramObject)
    {
      if (contains(paramObject))
        Object localObject = map().remove(paramObject);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean removeAll(Collection<?> paramCollection)
    {
      Collection localCollection = (Collection)Preconditions.checkNotNull(paramCollection);
      return super.removeAll(localCollection);
    }

    public int size()
    {
      return map().size();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Maps
 * JD-Core Version:    0.6.2
 */