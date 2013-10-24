package com.google.common.collect;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;

final class ImmutableAsList<E> extends RegularImmutableList<E>
{
  private final transient ImmutableCollection<E> collection;

  ImmutableAsList(Object[] paramArrayOfObject, ImmutableCollection<E> paramImmutableCollection)
  {
    super(paramArrayOfObject, 0, i);
    this.collection = paramImmutableCollection;
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws InvalidObjectException
  {
    throw new InvalidObjectException("Use SerializedForm");
  }

  public boolean contains(Object paramObject)
  {
    return this.collection.contains(paramObject);
  }

  Object writeReplace()
  {
    ImmutableCollection localImmutableCollection = this.collection;
    return new SerializedForm(localImmutableCollection);
  }

  static class SerializedForm
    implements Serializable
  {
    private static final long serialVersionUID;
    final ImmutableCollection<?> collection;

    SerializedForm(ImmutableCollection<?> paramImmutableCollection)
    {
      this.collection = paramImmutableCollection;
    }

    Object readResolve()
    {
      return this.collection.asList();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableAsList
 * JD-Core Version:    0.6.2
 */