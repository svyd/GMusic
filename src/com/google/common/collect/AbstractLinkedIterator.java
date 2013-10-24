package com.google.common.collect;

import java.util.NoSuchElementException;

public abstract class AbstractLinkedIterator<T> extends UnmodifiableIterator<T>
{
  private T nextOrNull;

  protected AbstractLinkedIterator(T paramT)
  {
    this.nextOrNull = paramT;
  }

  protected abstract T computeNext(T paramT);

  public final boolean hasNext()
  {
    if (this.nextOrNull != null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public final T next()
  {
    if (!hasNext())
      throw new NoSuchElementException();
    try
    {
      Object localObject1 = this.nextOrNull;
      Object localObject2;
      Object localObject3;
      return localObject1;
    }
    finally
    {
      Object localObject5 = this.nextOrNull;
      Object localObject6 = computeNext(localObject5);
      this.nextOrNull = localObject6;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.AbstractLinkedIterator
 * JD-Core Version:    0.6.2
 */