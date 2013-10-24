package com.google.common.collect;

import java.lang.ref.ReferenceQueue;

final class MapMakerInternalMap$StrongValueReference<K, V>
  implements MapMakerInternalMap.ValueReference<K, V>
{
  final V referent;

  MapMakerInternalMap$StrongValueReference(V paramV)
  {
    this.referent = paramV;
  }

  public void clear(MapMakerInternalMap.ValueReference<K, V> paramValueReference)
  {
  }

  public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> paramReferenceQueue, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    return this;
  }

  public V get()
  {
    return this.referent;
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> getEntry()
  {
    return null;
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
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.StrongValueReference
 * JD-Core Version:    0.6.2
 */