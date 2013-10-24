package com.google.common.collect;

import java.util.Iterator;

final class MapMakerInternalMap$ValueIterator extends MapMakerInternalMap<K, V>.HashIterator
  implements Iterator<V>
{
  MapMakerInternalMap$ValueIterator(MapMakerInternalMap paramMapMakerInternalMap)
  {
    super(paramMapMakerInternalMap);
  }

  public V next()
  {
    return nextEntry().getValue();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.ValueIterator
 * JD-Core Version:    0.6.2
 */