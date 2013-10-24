package com.google.common.collect;

import java.util.ListIterator;

public abstract class UnmodifiableListIterator<E> extends UnmodifiableIterator<E>
  implements ListIterator<E>
{
  public final void add(E paramE)
  {
    throw new UnsupportedOperationException();
  }

  public final void set(E paramE)
  {
    throw new UnsupportedOperationException();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.UnmodifiableListIterator
 * JD-Core Version:    0.6.2
 */