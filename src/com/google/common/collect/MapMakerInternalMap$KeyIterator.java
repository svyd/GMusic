package com.google.common.collect;

import java.util.Iterator;

final class MapMakerInternalMap$KeyIterator extends MapMakerInternalMap<K, V>.HashIterator
  implements Iterator<K>
{
  MapMakerInternalMap$KeyIterator(MapMakerInternalMap paramMapMakerInternalMap)
  {
    super(paramMapMakerInternalMap);
  }

  public K next()
  {
    return nextEntry().getKey();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.KeyIterator
 * JD-Core Version:    0.6.2
 */