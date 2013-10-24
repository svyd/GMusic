package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.NoSuchElementException;

abstract class AbstractIndexedListIterator<E> extends UnmodifiableListIterator<E>
{
  private int position;
  private final int size;

  protected AbstractIndexedListIterator(int paramInt)
  {
    this(paramInt, 0);
  }

  protected AbstractIndexedListIterator(int paramInt1, int paramInt2)
  {
    int i = Preconditions.checkPositionIndex(paramInt2, paramInt1);
    this.size = paramInt1;
    this.position = paramInt2;
  }

  protected abstract E get(int paramInt);

  public final boolean hasNext()
  {
    int i = this.position;
    int j = this.size;
    if (i < j);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public final boolean hasPrevious()
  {
    if (this.position > 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public final E next()
  {
    if (!hasNext())
      throw new NoSuchElementException();
    int i = this.position;
    int j = i + 1;
    this.position = j;
    return get(i);
  }

  public final int nextIndex()
  {
    return this.position;
  }

  public final E previous()
  {
    if (!hasPrevious())
      throw new NoSuchElementException();
    int i = this.position + -1;
    this.position = i;
    return get(i);
  }

  public final int previousIndex()
  {
    return this.position + -1;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.AbstractIndexedListIterator
 * JD-Core Version:    0.6.2
 */