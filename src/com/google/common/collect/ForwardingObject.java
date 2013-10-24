package com.google.common.collect;

public abstract class ForwardingObject
{
  protected abstract Object delegate();

  public String toString()
  {
    return delegate().toString();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ForwardingObject
 * JD-Core Version:    0.6.2
 */