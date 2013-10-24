package com.google.common.collect;

import com.google.common.base.Equivalence;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentMap;

final class MapMakerInternalMap$SerializationProxy<K, V> extends MapMakerInternalMap.AbstractSerializationProxy<K, V>
{
  private static final long serialVersionUID = 3L;

  MapMakerInternalMap$SerializationProxy(MapMakerInternalMap.Strength paramStrength1, MapMakerInternalMap.Strength paramStrength2, Equivalence<Object> paramEquivalence1, Equivalence<Object> paramEquivalence2, long paramLong1, long paramLong2, int paramInt1, int paramInt2, MapMaker.RemovalListener<? super K, ? super V> paramRemovalListener, ConcurrentMap<K, V> paramConcurrentMap)
  {
    super(paramStrength1, paramStrength2, paramEquivalence1, paramEquivalence2, paramLong1, paramLong2, paramInt1, paramInt2, paramRemovalListener, paramConcurrentMap);
  }

  private void readObject(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    paramObjectInputStream.defaultReadObject();
    ConcurrentMap localConcurrentMap = readMapMaker(paramObjectInputStream).makeMap();
    this.delegate = localConcurrentMap;
    readEntries(paramObjectInputStream);
  }

  private Object readResolve()
  {
    return this.delegate;
  }

  private void writeObject(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    paramObjectOutputStream.defaultWriteObject();
    writeMapTo(paramObjectOutputStream);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.SerializationProxy
 * JD-Core Version:    0.6.2
 */