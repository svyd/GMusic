package com.google.common.collect;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

final class MapMakerInternalMap$WeakValueReference<K, V> extends WeakReference<V>
  implements MapMakerInternalMap.ValueReference<K, V>
{
  final MapMakerInternalMap.ReferenceEntry<K, V> entry;

  MapMakerInternalMap$WeakValueReference(ReferenceQueue<V> paramReferenceQueue, V paramV, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    super(paramV, paramReferenceQueue);
    this.entry = paramReferenceEntry;
  }

  public void clear(MapMakerInternalMap.ValueReference<K, V> paramValueReference)
  {
    clear();
  }

  public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> paramReferenceQueue, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    Object localObject = get();
    return new WeakValueReference(paramReferenceQueue, localObject, paramReferenceEntry);
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> getEntry()
  {
    return this.entry;
  }

  public boolean isComputingReference()
  {
    return false;
  }

  public V waitForValue()
  {
    return get();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.WeakValueReference
 * JD-Core Version:    0.6.2
 */