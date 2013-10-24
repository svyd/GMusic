package com.google.common.collect;

import java.util.AbstractSet;
import java.util.Iterator;

final class MapMakerInternalMap$KeySet extends AbstractSet<K>
{
  MapMakerInternalMap$KeySet(MapMakerInternalMap paramMapMakerInternalMap)
  {
  }

  public void clear()
  {
    this.this$0.clear();
  }

  public boolean contains(Object paramObject)
  {
    return this.this$0.containsKey(paramObject);
  }

  public boolean isEmpty()
  {
    return this.this$0.isEmpty();
  }

  public Iterator<K> iterator()
  {
    MapMakerInternalMap localMapMakerInternalMap = this.this$0;
    return new MapMakerInternalMap.KeyIterator(localMapMakerInternalMap);
  }

  public boolean remove(Object paramObject)
  {
    if (this.this$0.remove(paramObject) != null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public int size()
  {
    return this.this$0.size();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.KeySet
 * JD-Core Version:    0.6.2
 */