package com.google.common.collect;

import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ExecutionException;

abstract interface MapMakerInternalMap$ValueReference<K, V>
{
  public abstract void clear(ValueReference<K, V> paramValueReference);

  public abstract ValueReference<K, V> copyFor(ReferenceQueue<V> paramReferenceQueue, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry);

  public abstract V get();

  public abstract MapMakerInternalMap.ReferenceEntry<K, V> getEntry();

  public abstract boolean isComputingReference();

  public abstract V waitForValue()
    throws ExecutionException;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.ValueReference
 * JD-Core Version:    0.6.2
 */