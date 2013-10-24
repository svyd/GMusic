package com.google.common.collect;

import java.util.Map.Entry;

final class MapMakerInternalMap$WriteThroughEntry extends AbstractMapEntry<K, V>
{
  final K key;
  V value;

  MapMakerInternalMap$WriteThroughEntry(K paramK, V paramV)
  {
    this.key = paramV;
    Object localObject;
    this.value = localObject;
  }

  public boolean equals(Object paramObject)
  {
    boolean bool = false;
    if ((paramObject instanceof Map.Entry))
    {
      Map.Entry localEntry = (Map.Entry)paramObject;
      Object localObject1 = this.key;
      Object localObject2 = localEntry.getKey();
      if (localObject1.equals(localObject2))
      {
        Object localObject3 = this.value;
        Object localObject4 = localEntry.getValue();
        if (localObject3.equals(localObject4))
          bool = true;
      }
    }
    return bool;
  }

  public K getKey()
  {
    return this.key;
  }

  public V getValue()
  {
    return this.value;
  }

  public int hashCode()
  {
    int i = this.key.hashCode();
    int j = this.value.hashCode();
    return i ^ j;
  }

  public V setValue(V paramV)
  {
    MapMakerInternalMap localMapMakerInternalMap = this.this$0;
    Object localObject1 = this.key;
    Object localObject2 = localMapMakerInternalMap.put(localObject1, paramV);
    this.value = paramV;
    return localObject2;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.WriteThroughEntry
 * JD-Core Version:    0.6.2
 */