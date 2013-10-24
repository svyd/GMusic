package com.google.common.collect;

import java.lang.ref.ReferenceQueue;

final class MapMakerInternalMap$WeakExpirableEvictableEntry<K, V> extends MapMakerInternalMap.WeakEntry<K, V>
  implements MapMakerInternalMap.ReferenceEntry<K, V>
{
  MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable;
  MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable;
  MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable;
  MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable;
  volatile long time = 9223372036854775807L;

  MapMakerInternalMap$WeakExpirableEvictableEntry(ReferenceQueue<K> paramReferenceQueue, K paramK, int paramInt, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    super(paramReferenceQueue, paramK, paramInt, paramReferenceEntry);
    MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = MapMakerInternalMap.nullEntry();
    this.nextExpirable = localReferenceEntry1;
    MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = MapMakerInternalMap.nullEntry();
    this.previousExpirable = localReferenceEntry2;
    MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = MapMakerInternalMap.nullEntry();
    this.nextEvictable = localReferenceEntry3;
    MapMakerInternalMap.ReferenceEntry localReferenceEntry4 = MapMakerInternalMap.nullEntry();
    this.previousEvictable = localReferenceEntry4;
  }

  public long getExpirationTime()
  {
    return this.time;
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable()
  {
    return this.nextEvictable;
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable()
  {
    return this.nextExpirable;
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable()
  {
    return this.previousEvictable;
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable()
  {
    return this.previousExpirable;
  }

  public void setExpirationTime(long paramLong)
  {
    this.time = paramLong;
  }

  public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    this.nextEvictable = paramReferenceEntry;
  }

  public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    this.nextExpirable = paramReferenceEntry;
  }

  public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    this.previousEvictable = paramReferenceEntry;
  }

  public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    this.previousExpirable = paramReferenceEntry;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.WeakExpirableEvictableEntry
 * JD-Core Version:    0.6.2
 */