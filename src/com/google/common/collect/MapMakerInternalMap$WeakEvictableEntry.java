package com.google.common.collect;

import java.lang.ref.ReferenceQueue;

final class MapMakerInternalMap$WeakEvictableEntry<K, V> extends MapMakerInternalMap.WeakEntry<K, V>
  implements MapMakerInternalMap.ReferenceEntry<K, V>
{
  MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable;
  MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable;

  MapMakerInternalMap$WeakEvictableEntry(ReferenceQueue<K> paramReferenceQueue, K paramK, int paramInt, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    super(paramReferenceQueue, paramK, paramInt, paramReferenceEntry);
    MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = MapMakerInternalMap.nullEntry();
    this.nextEvictable = localReferenceEntry1;
    MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = MapMakerInternalMap.nullEntry();
    this.previousEvictable = localReferenceEntry2;
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable()
  {
    return this.nextEvictable;
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable()
  {
    return this.previousEvictable;
  }

  public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    this.nextEvictable = paramReferenceEntry;
  }

  public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    this.previousEvictable = paramReferenceEntry;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.WeakEvictableEntry
 * JD-Core Version:    0.6.2
 */