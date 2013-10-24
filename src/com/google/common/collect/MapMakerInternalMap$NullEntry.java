package com.google.common.collect;

 enum MapMakerInternalMap$NullEntry
  implements MapMakerInternalMap.ReferenceEntry<Object, Object>
{
  static
  {
    NullEntry[] arrayOfNullEntry = new NullEntry[1];
    NullEntry localNullEntry = INSTANCE;
    arrayOfNullEntry[0] = localNullEntry;
  }

  public long getExpirationTime()
  {
    return 0L;
  }

  public int getHash()
  {
    return 0;
  }

  public Object getKey()
  {
    return null;
  }

  public MapMakerInternalMap.ReferenceEntry<Object, Object> getNext()
  {
    return null;
  }

  public MapMakerInternalMap.ReferenceEntry<Object, Object> getNextEvictable()
  {
    return this;
  }

  public MapMakerInternalMap.ReferenceEntry<Object, Object> getNextExpirable()
  {
    return this;
  }

  public MapMakerInternalMap.ReferenceEntry<Object, Object> getPreviousEvictable()
  {
    return this;
  }

  public MapMakerInternalMap.ReferenceEntry<Object, Object> getPreviousExpirable()
  {
    return this;
  }

  public MapMakerInternalMap.ValueReference<Object, Object> getValueReference()
  {
    return null;
  }

  public void setExpirationTime(long paramLong)
  {
  }

  public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<Object, Object> paramReferenceEntry)
  {
  }

  public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<Object, Object> paramReferenceEntry)
  {
  }

  public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<Object, Object> paramReferenceEntry)
  {
  }

  public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<Object, Object> paramReferenceEntry)
  {
  }

  public void setValueReference(MapMakerInternalMap.ValueReference<Object, Object> paramValueReference)
  {
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.NullEntry
 * JD-Core Version:    0.6.2
 */