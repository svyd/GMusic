package com.google.common.collect;

final class MapMakerInternalMap$StrongExpirableEntry<K, V> extends MapMakerInternalMap.StrongEntry<K, V>
  implements MapMakerInternalMap.ReferenceEntry<K, V>
{
  MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable;
  MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable;
  volatile long time = 9223372036854775807L;

  MapMakerInternalMap$StrongExpirableEntry(K paramK, int paramInt, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    super(paramK, paramInt, paramReferenceEntry);
    MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = MapMakerInternalMap.nullEntry();
    this.nextExpirable = localReferenceEntry1;
    MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = MapMakerInternalMap.nullEntry();
    this.previousExpirable = localReferenceEntry2;
  }

  public long getExpirationTime()
  {
    return this.time;
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable()
  {
    return this.nextExpirable;
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable()
  {
    return this.previousExpirable;
  }

  public void setExpirationTime(long paramLong)
  {
    this.time = paramLong;
  }

  public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    this.nextExpirable = paramReferenceEntry;
  }

  public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    this.previousExpirable = paramReferenceEntry;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.StrongExpirableEntry
 * JD-Core Version:    0.6.2
 */