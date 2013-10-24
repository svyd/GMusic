package com.google.common.collect;

import com.google.common.base.Equivalence;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

abstract class MapMakerInternalMap$AbstractSerializationProxy<K, V> extends ForwardingConcurrentMap<K, V>
  implements Serializable
{
  private static final long serialVersionUID = 3L;
  final int concurrencyLevel;
  transient ConcurrentMap<K, V> delegate;
  final long expireAfterAccessNanos;
  final long expireAfterWriteNanos;
  final Equivalence<Object> keyEquivalence;
  final MapMakerInternalMap.Strength keyStrength;
  final int maximumSize;
  final MapMaker.RemovalListener<? super K, ? super V> removalListener;
  final Equivalence<Object> valueEquivalence;
  final MapMakerInternalMap.Strength valueStrength;

  MapMakerInternalMap$AbstractSerializationProxy(MapMakerInternalMap.Strength paramStrength1, MapMakerInternalMap.Strength paramStrength2, Equivalence<Object> paramEquivalence1, Equivalence<Object> paramEquivalence2, long paramLong1, long paramLong2, int paramInt1, int paramInt2, MapMaker.RemovalListener<? super K, ? super V> paramRemovalListener, ConcurrentMap<K, V> paramConcurrentMap)
  {
    this.keyStrength = paramStrength1;
    this.valueStrength = paramStrength2;
    this.keyEquivalence = paramEquivalence1;
    this.valueEquivalence = paramEquivalence2;
    this.expireAfterWriteNanos = paramLong1;
    this.expireAfterAccessNanos = paramLong2;
    this.maximumSize = paramInt1;
    this.concurrencyLevel = paramInt2;
    this.removalListener = paramRemovalListener;
    this.delegate = paramConcurrentMap;
  }

  protected ConcurrentMap<K, V> delegate()
  {
    return this.delegate;
  }

  void readEntries(ObjectInputStream paramObjectInputStream)
    throws IOException, ClassNotFoundException
  {
    while (true)
    {
      Object localObject1 = paramObjectInputStream.readObject();
      if (localObject1 == null)
        return;
      Object localObject2 = paramObjectInputStream.readObject();
      Object localObject3 = this.delegate.put(localObject1, localObject2);
    }
  }

  MapMaker readMapMaker(ObjectInputStream paramObjectInputStream)
    throws IOException
  {
    int i = paramObjectInputStream.readInt();
    MapMaker localMapMaker1 = new MapMaker().initialCapacity(i);
    MapMakerInternalMap.Strength localStrength1 = this.keyStrength;
    MapMaker localMapMaker2 = localMapMaker1.setKeyStrength(localStrength1);
    MapMakerInternalMap.Strength localStrength2 = this.valueStrength;
    MapMaker localMapMaker3 = localMapMaker2.setValueStrength(localStrength2);
    Equivalence localEquivalence1 = this.keyEquivalence;
    MapMaker localMapMaker4 = localMapMaker3.keyEquivalence(localEquivalence1);
    Equivalence localEquivalence2 = this.valueEquivalence;
    MapMaker localMapMaker5 = localMapMaker4.valueEquivalence(localEquivalence2);
    int j = this.concurrencyLevel;
    MapMaker localMapMaker6 = localMapMaker5.concurrencyLevel(j);
    MapMaker.RemovalListener localRemovalListener = this.removalListener;
    GenericMapMaker localGenericMapMaker = localMapMaker6.removalListener(localRemovalListener);
    if (this.expireAfterWriteNanos > 0L)
    {
      long l1 = this.expireAfterWriteNanos;
      TimeUnit localTimeUnit1 = TimeUnit.NANOSECONDS;
      MapMaker localMapMaker7 = localMapMaker6.expireAfterWrite(l1, localTimeUnit1);
    }
    if (this.expireAfterAccessNanos > 0L)
    {
      long l2 = this.expireAfterAccessNanos;
      TimeUnit localTimeUnit2 = TimeUnit.NANOSECONDS;
      MapMaker localMapMaker8 = localMapMaker6.expireAfterAccess(l2, localTimeUnit2);
    }
    if (this.maximumSize != -1)
    {
      int k = this.maximumSize;
      MapMaker localMapMaker9 = localMapMaker6.maximumSize(k);
    }
    return localMapMaker6;
  }

  void writeMapTo(ObjectOutputStream paramObjectOutputStream)
    throws IOException
  {
    int i = this.delegate.size();
    paramObjectOutputStream.writeInt(i);
    Iterator localIterator = this.delegate.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Object localObject1 = localEntry.getKey();
      paramObjectOutputStream.writeObject(localObject1);
      Object localObject2 = localEntry.getValue();
      paramObjectOutputStream.writeObject(localObject2);
    }
    paramObjectOutputStream.writeObject(null);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.AbstractSerializationProxy
 * JD-Core Version:    0.6.2
 */