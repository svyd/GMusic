package com.google.common.collect;

import com.google.common.base.Equivalence;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map.Entry;

final class MapMakerInternalMap$EntrySet extends AbstractSet<Map.Entry<K, V>>
{
  MapMakerInternalMap$EntrySet(MapMakerInternalMap paramMapMakerInternalMap)
  {
  }

  public void clear()
  {
    this.this$0.clear();
  }

  public boolean contains(Object paramObject)
  {
    boolean bool = false;
    if (!(paramObject instanceof Map.Entry));
    while (true)
    {
      return bool;
      Map.Entry localEntry = (Map.Entry)paramObject;
      Object localObject1 = localEntry.getKey();
      if (localObject1 != null)
      {
        Object localObject2 = this.this$0.get(localObject1);
        if (localObject2 != null)
        {
          Equivalence localEquivalence = this.this$0.valueEquivalence;
          Object localObject3 = localEntry.getValue();
          if (localEquivalence.equivalent(localObject3, localObject2))
            bool = true;
        }
      }
    }
  }

  public boolean isEmpty()
  {
    return this.this$0.isEmpty();
  }

  public Iterator<Map.Entry<K, V>> iterator()
  {
    MapMakerInternalMap localMapMakerInternalMap = this.this$0;
    return new MapMakerInternalMap.EntryIterator(localMapMakerInternalMap);
  }

  public boolean remove(Object paramObject)
  {
    boolean bool = false;
    if (!(paramObject instanceof Map.Entry));
    while (true)
    {
      return bool;
      Map.Entry localEntry = (Map.Entry)paramObject;
      Object localObject1 = localEntry.getKey();
      if (localObject1 != null)
      {
        MapMakerInternalMap localMapMakerInternalMap = this.this$0;
        Object localObject2 = localEntry.getValue();
        if (localMapMakerInternalMap.remove(localObject1, localObject2))
          bool = true;
      }
    }
  }

  public int size()
  {
    return this.this$0.size();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.EntrySet
 * JD-Core Version:    0.6.2
 */