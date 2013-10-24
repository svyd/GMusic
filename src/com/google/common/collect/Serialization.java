package com.google.common.collect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

final class Serialization
{
  static <K, V> void populateMultimap(Multimap<K, V> paramMultimap, ObjectInputStream paramObjectInputStream, int paramInt)
    throws IOException, ClassNotFoundException
  {
    int i = 0;
    while (true)
    {
      if (i >= paramInt)
        return;
      Object localObject1 = paramObjectInputStream.readObject();
      Collection localCollection = paramMultimap.get(localObject1);
      int j = paramObjectInputStream.readInt();
      int k = 0;
      while (k < j)
      {
        Object localObject2 = paramObjectInputStream.readObject();
        boolean bool = localCollection.add(localObject2);
        k += 1;
      }
      i += 1;
    }
  }

  static int readCount(ObjectInputStream paramObjectInputStream)
    throws IOException
  {
    return paramObjectInputStream.readInt();
  }

  static <K, V> void writeMultimap(Multimap<K, V> paramMultimap, ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    int i = paramMultimap.asMap().size();
    paramObjectOutputStream.writeInt(i);
    Iterator localIterator1 = paramMultimap.asMap().entrySet().iterator();
    while (true)
    {
      if (!localIterator1.hasNext())
        return;
      Map.Entry localEntry = (Map.Entry)localIterator1.next();
      Object localObject1 = localEntry.getKey();
      paramObjectOutputStream.writeObject(localObject1);
      int j = ((Collection)localEntry.getValue()).size();
      paramObjectOutputStream.writeInt(j);
      Iterator localIterator2 = ((Collection)localEntry.getValue()).iterator();
      while (localIterator2.hasNext())
      {
        Object localObject2 = localIterator2.next();
        paramObjectOutputStream.writeObject(localObject2);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.Serialization
 * JD-Core Version:    0.6.2
 */