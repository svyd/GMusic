package com.google.common.collect;

import java.util.Comparator;

abstract interface SortedIterable<T> extends Iterable<T>
{
  public abstract Comparator<? super T> comparator();
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.SortedIterable
 * JD-Core Version:    0.6.2
 */