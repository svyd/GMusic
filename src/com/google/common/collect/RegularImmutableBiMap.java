package com.google.common.collect;

import java.util.Iterator;
import java.util.Map.Entry;

class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V>
{
  final transient ImmutableMap<K, V> delegate;
  final transient ImmutableBiMap<V, K> inverse;

  RegularImmutableBiMap(ImmutableMap<K, V> paramImmutableMap)
  {
    this.delegate = paramImmutableMap;
    ImmutableMap.Builder localBuilder1 = ImmutableMap.builder();
    Iterator localIterator = paramImmutableMap.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Object localObject1 = localEntry.getValue();
      Object localObject2 = localEntry.getKey();
      ImmutableMap.Builder localBuilder2 = localBuilder1.put(localObject1, localObject2);
    }
    ImmutableMap localImmutableMap = localBuilder1.build();
    RegularImmutableBiMap localRegularImmutableBiMap = new RegularImmutableBiMap(localImmutableMap, this);
    this.inverse = localRegularImmutableBiMap;
  }

  RegularImmutableBiMap(ImmutableMap<K, V> paramImmutableMap, ImmutableBiMap<V, K> paramImmutableBiMap)
  {
    this.delegate = paramImmutableMap;
    this.inverse = paramImmutableBiMap;
  }

  ImmutableMap<K, V> delegate()
  {
    return this.delegate;
  }

  public ImmutableBiMap<V, K> inverse()
  {
    return this.inverse;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.RegularImmutableBiMap
 * JD-Core Version:    0.6.2
 */