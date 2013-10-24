package com.google.common.collect;

import java.util.Map;
import java.util.Set;

public abstract interface BiMap<K, V> extends Map<K, V>
{
  public abstract Set<V> values();
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.BiMap
 * JD-Core Version:    0.6.2
 */