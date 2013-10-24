package com.google.common.collect;

import java.util.Collection;
import java.util.Map;

public abstract interface Multimap<K, V>
{
  public abstract Map<K, Collection<V>> asMap();

  public abstract Collection<V> get(K paramK);
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Multimap
 * JD-Core Version:    0.6.2
 */