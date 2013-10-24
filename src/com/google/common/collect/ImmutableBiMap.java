package com.google.common.collect;

import java.util.Map.Entry;

public abstract class ImmutableBiMap<K, V> extends ImmutableMap<K, V>
  implements BiMap<K, V>
{
  private static final ImmutableBiMap<Object, Object> EMPTY_IMMUTABLE_BIMAP = new EmptyBiMap();

  public static <K, V> ImmutableBiMap<K, V> of()
  {
    return EMPTY_IMMUTABLE_BIMAP;
  }

  public boolean containsKey(Object paramObject)
  {
    return delegate().containsKey(paramObject);
  }

  public boolean containsValue(Object paramObject)
  {
    return inverse().containsKey(paramObject);
  }

  abstract ImmutableMap<K, V> delegate();

  public ImmutableSet<Map.Entry<K, V>> entrySet()
  {
    return delegate().entrySet();
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject == this) || (delegate().equals(paramObject)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public V get(Object paramObject)
  {
    return delegate().get(paramObject);
  }

  public int hashCode()
  {
    return delegate().hashCode();
  }

  public abstract ImmutableBiMap<V, K> inverse();

  public boolean isEmpty()
  {
    return delegate().isEmpty();
  }

  public ImmutableSet<K> keySet()
  {
    return delegate().keySet();
  }

  public int size()
  {
    return delegate().size();
  }

  public String toString()
  {
    return delegate().toString();
  }

  public ImmutableSet<V> values()
  {
    return inverse().keySet();
  }

  Object writeReplace()
  {
    return new SerializedForm(this);
  }

  private static class SerializedForm extends ImmutableMap.SerializedForm
  {
    private static final long serialVersionUID;

    SerializedForm(ImmutableBiMap<?, ?> paramImmutableBiMap)
    {
      super();
    }

    Object readResolve()
    {
      ImmutableBiMap.Builder localBuilder = new ImmutableBiMap.Builder();
      return createMap(localBuilder);
    }
  }

  static class EmptyBiMap extends ImmutableBiMap<Object, Object>
  {
    ImmutableMap<Object, Object> delegate()
    {
      return ImmutableMap.of();
    }

    public ImmutableBiMap<Object, Object> inverse()
    {
      return this;
    }

    Object readResolve()
    {
      return ImmutableBiMap.EMPTY_IMMUTABLE_BIMAP;
    }
  }

  public static final class Builder<K, V> extends ImmutableMap.Builder<K, V>
  {
    public ImmutableBiMap<K, V> build()
    {
      ImmutableMap localImmutableMap = super.build();
      if (localImmutableMap.isEmpty());
      for (Object localObject = ImmutableBiMap.of(); ; localObject = new RegularImmutableBiMap(localImmutableMap))
        return localObject;
    }

    public Builder<K, V> put(K paramK, V paramV)
    {
      ImmutableMap.Builder localBuilder = super.put(paramK, paramV);
      return this;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableBiMap
 * JD-Core Version:    0.6.2
 */