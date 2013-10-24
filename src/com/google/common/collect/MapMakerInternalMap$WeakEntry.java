package com.google.common.collect;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

class MapMakerInternalMap$WeakEntry<K, V> extends WeakReference<K>
  implements MapMakerInternalMap.ReferenceEntry<K, V>
{
  final int hash;
  final MapMakerInternalMap.ReferenceEntry<K, V> next;
  volatile MapMakerInternalMap.ValueReference<K, V> valueReference;

  MapMakerInternalMap$WeakEntry(ReferenceQueue<K> paramReferenceQueue, K paramK, int paramInt, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    super(paramK, paramReferenceQueue);
    MapMakerInternalMap.ValueReference localValueReference = MapMakerInternalMap.unset();
    this.valueReference = localValueReference;
    this.hash = paramInt;
    this.next = paramReferenceEntry;
  }

  public long getExpirationTime()
  {
    throw new UnsupportedOperationException();
  }

  public int getHash()
  {
    return this.hash;
  }

  public K getKey()
  {
    return get();
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> getNext()
  {
    return this.next;
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable()
  {
    throw new UnsupportedOperationException();
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable()
  {
    throw new UnsupportedOperationException();
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable()
  {
    throw new UnsupportedOperationException();
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable()
  {
    throw new UnsupportedOperationException();
  }

  public MapMakerInternalMap.ValueReference<K, V> getValueReference()
  {
    return this.valueReference;
  }

  public void setExpirationTime(long paramLong)
  {
    throw new UnsupportedOperationException();
  }

  public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    throw new UnsupportedOperationException();
  }

  public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    throw new UnsupportedOperationException();
  }

  public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    throw new UnsupportedOperationException();
  }

  public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    throw new UnsupportedOperationException();
  }

  public void setValueReference(MapMakerInternalMap.ValueReference<K, V> paramValueReference)
  {
    MapMakerInternalMap.ValueReference localValueReference = this.valueReference;
    this.valueReference = paramValueReference;
    localValueReference.clear(paramValueReference);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.WeakEntry
 * JD-Core Version:    0.6.2
 */