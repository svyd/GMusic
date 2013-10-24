package com.google.common.collect;

import java.util.Iterator;
import java.util.Map.Entry;

final class MapMakerInternalMap$EntryIterator extends MapMakerInternalMap<K, V>.HashIterator
  implements Iterator<Map.Entry<K, V>>
{
  MapMakerInternalMap$EntryIterator(MapMakerInternalMap paramMapMakerInternalMap)
  {
    super(paramMapMakerInternalMap);
  }

  public Map.Entry<K, V> next()
  {
    return nextEntry();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.EntryIterator
 * JD-Core Version:    0.6.2
 */