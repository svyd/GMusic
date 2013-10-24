package com.google.common.collect;

import java.util.Map;
import java.util.Map.Entry;

final class EmptyImmutableMap extends ImmutableMap<Object, Object>
{
  static final EmptyImmutableMap INSTANCE = new EmptyImmutableMap();
  private static final long serialVersionUID;

  public boolean containsKey(Object paramObject)
  {
    return false;
  }

  public boolean containsValue(Object paramObject)
  {
    return false;
  }

  public ImmutableSet<Map.Entry<Object, Object>> entrySet()
  {
    return ImmutableSet.of();
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof Map));
    for (boolean bool = ((Map)paramObject).isEmpty(); ; bool = false)
      return bool;
  }

  public Object get(Object paramObject)
  {
    return null;
  }

  public int hashCode()
  {
    return 0;
  }

  public boolean isEmpty()
  {
    return true;
  }

  public ImmutableSet<Object> keySet()
  {
    return ImmutableSet.of();
  }

  Object readResolve()
  {
    return INSTANCE;
  }

  public int size()
  {
    return 0;
  }

  public String toString()
  {
    return "{}";
  }

  public ImmutableCollection<Object> values()
  {
    return ImmutableCollection.EMPTY_IMMUTABLE_COLLECTION;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.EmptyImmutableMap
 * JD-Core Version:    0.6.2
 */