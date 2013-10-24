package com.google.common.collect;

abstract interface MapMakerInternalMap$ReferenceEntry<K, V>
{
  public abstract long getExpirationTime();

  public abstract int getHash();

  public abstract K getKey();

  public abstract ReferenceEntry<K, V> getNext();

  public abstract ReferenceEntry<K, V> getNextEvictable();

  public abstract ReferenceEntry<K, V> getNextExpirable();

  public abstract ReferenceEntry<K, V> getPreviousEvictable();

  public abstract ReferenceEntry<K, V> getPreviousExpirable();

  public abstract MapMakerInternalMap.ValueReference<K, V> getValueReference();

  public abstract void setExpirationTime(long paramLong);

  public abstract void setNextEvictable(ReferenceEntry<K, V> paramReferenceEntry);

  public abstract void setNextExpirable(ReferenceEntry<K, V> paramReferenceEntry);

  public abstract void setPreviousEvictable(ReferenceEntry<K, V> paramReferenceEntry);

  public abstract void setPreviousExpirable(ReferenceEntry<K, V> paramReferenceEntry);

  public abstract void setValueReference(MapMakerInternalMap.ValueReference<K, V> paramValueReference);
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.ReferenceEntry
 * JD-Core Version:    0.6.2
 */