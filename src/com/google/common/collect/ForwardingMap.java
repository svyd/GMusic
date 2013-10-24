package com.google.common.collect;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public abstract class ForwardingMap<K, V> extends ForwardingObject
  implements Map<K, V>
{
  public void clear()
  {
    delegate().clear();
  }

  public boolean containsKey(Object paramObject)
  {
    return delegate().containsKey(paramObject);
  }

  public boolean containsValue(Object paramObject)
  {
    return delegate().containsValue(paramObject);
  }

  protected abstract Map<K, V> delegate();

  public Set<Map.Entry<K, V>> entrySet()
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

  public boolean isEmpty()
  {
    return delegate().isEmpty();
  }

  public Set<K> keySet()
  {
    return delegate().keySet();
  }

  public V put(K paramK, V paramV)
  {
    return delegate().put(paramK, paramV);
  }

  public void putAll(Map<? extends K, ? extends V> paramMap)
  {
    delegate().putAll(paramMap);
  }

  public V remove(Object paramObject)
  {
    return delegate().remove(paramObject);
  }

  public int size()
  {
    return delegate().size();
  }

  public Collection<V> values()
  {
    return delegate().values();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ForwardingMap
 * JD-Core Version:    0.6.2
 */