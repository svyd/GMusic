package com.google.common.collect;

import java.util.Set;

public abstract interface Table<R, C, V>
{
  public abstract Set<Cell<R, C, V>> cellSet();

  public static abstract interface Cell<R, C, V>
  {
    public abstract V getValue();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Table
 * JD-Core Version:    0.6.2
 */