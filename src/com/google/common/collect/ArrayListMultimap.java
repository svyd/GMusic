package com.google.common.collect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ArrayListMultimap<K, V> extends AbstractListMultimap<K, V>
{
  private static final long serialVersionUID;
  transient int expectedValuesPerKey = 10;

  private ArrayListMultimap()
  {
    super(localHashMap);
  }

  public static <K, V> ArrayListMultimap<K, V> create()
  {
    return new ArrayListMultimap();
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    int i = paramObjectInputStream.readInt();
    this.expectedValuesPerKey = i;
    int j = Serialization.readCount(paramObjectInputStream);
    HashMap localHashMap = Maps.newHashMapWithExpectedSize(j);
    setMap(localHashMap);
    Serialization.populateMultimap(this, paramObjectInputStream, j);
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.defaultWriteObject();
    int i = this.expectedValuesPerKey;
    paramObjectOutputStream.writeInt(i);
    Serialization.writeMultimap(this, paramObjectOutputStream);
  }

  List<V> createCollection()
  {
    int i = this.expectedValuesPerKey;
    return new ArrayList(i);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ArrayListMultimap
 * JD-Core Version:    0.6.2
 */