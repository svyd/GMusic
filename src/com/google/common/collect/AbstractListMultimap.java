package com.google.common.collect;

import java.util.Collection;
import java.util.List;
import java.util.Map;

abstract class AbstractListMultimap<K, V> extends AbstractMultimap<K, V>
  implements ListMultimap<K, V>
{
  private static final long serialVersionUID = 6588350623831699109L;

  protected AbstractListMultimap(Map<K, Collection<V>> paramMap)
  {
    super(paramMap);
  }

  public Map<K, Collection<V>> asMap()
  {
    return super.asMap();
  }

  abstract List<V> createCollection();

  public boolean equals(Object paramObject)
  {
    return super.equals(paramObject);
  }

  public List<V> get(K paramK)
  {
    return (List)super.get(paramK);
  }

  public boolean put(K paramK, V paramV)
  {
    return super.put(paramK, paramV);
  }

  public List<V> removeAll(Object paramObject)
  {
    return (List)super.removeAll(paramObject);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.AbstractListMultimap
 * JD-Core Version:    0.6.2
 */