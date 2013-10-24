package com.google.common.collect;

import java.util.AbstractCollection;
import java.util.Iterator;

final class MapMakerInternalMap$Values extends AbstractCollection<V>
{
  MapMakerInternalMap$Values(MapMakerInternalMap paramMapMakerInternalMap)
  {
  }

  public void clear()
  {
    this.this$0.clear();
  }

  public boolean contains(Object paramObject)
  {
    return this.this$0.containsValue(paramObject);
  }

  public boolean isEmpty()
  {
    return this.this$0.isEmpty();
  }

  public Iterator<V> iterator()
  {
    MapMakerInternalMap localMapMakerInternalMap = this.this$0;
    return new MapMakerInternalMap.ValueIterator(localMapMakerInternalMap);
  }

  public int size()
  {
    return this.this$0.size();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.Values
 * JD-Core Version:    0.6.2
 */