package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.Ticker;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantLock;

class MapMakerInternalMap$Segment<K, V> extends ReentrantLock
{
  volatile int count;
  final Queue<MapMakerInternalMap.ReferenceEntry<K, V>> evictionQueue;
  final Queue<MapMakerInternalMap.ReferenceEntry<K, V>> expirationQueue;
  final ReferenceQueue<K> keyReferenceQueue;
  final MapMakerInternalMap<K, V> map;
  final int maxSegmentSize;
  int modCount;
  final AtomicInteger readCount;
  final Queue<MapMakerInternalMap.ReferenceEntry<K, V>> recencyQueue;
  volatile AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> table;
  int threshold;
  final ReferenceQueue<V> valueReferenceQueue;

  MapMakerInternalMap$Segment(MapMakerInternalMap<K, V> paramMapMakerInternalMap, int paramInt1, int paramInt2)
  {
    AtomicInteger localAtomicInteger = new AtomicInteger();
    this.readCount = localAtomicInteger;
    this.map = paramMapMakerInternalMap;
    this.maxSegmentSize = paramInt2;
    AtomicReferenceArray localAtomicReferenceArray = newEntryArray(paramInt1);
    initTable(localAtomicReferenceArray);
    ReferenceQueue localReferenceQueue2;
    Object localObject1;
    label112: Object localObject2;
    if (paramMapMakerInternalMap.usesKeyReferences())
    {
      localReferenceQueue2 = new ReferenceQueue();
      this.keyReferenceQueue = localReferenceQueue2;
      if (paramMapMakerInternalMap.usesValueReferences())
        localReferenceQueue1 = new ReferenceQueue();
      this.valueReferenceQueue = localReferenceQueue1;
      if ((!paramMapMakerInternalMap.evictsBySize()) && (!paramMapMakerInternalMap.expiresAfterAccess()))
        break label169;
      localObject1 = new ConcurrentLinkedQueue();
      this.recencyQueue = ((Queue)localObject1);
      if (!paramMapMakerInternalMap.evictsBySize())
        break label177;
      localObject2 = new MapMakerInternalMap.EvictionQueue();
      label134: this.evictionQueue = ((Queue)localObject2);
      if (!paramMapMakerInternalMap.expires())
        break label185;
    }
    label169: label177: label185: for (Object localObject3 = new MapMakerInternalMap.ExpirationQueue(); ; localObject3 = MapMakerInternalMap.discardingQueue())
    {
      this.expirationQueue = ((Queue)localObject3);
      return;
      localReferenceQueue2 = null;
      break;
      localObject1 = MapMakerInternalMap.discardingQueue();
      break label112;
      localObject2 = MapMakerInternalMap.discardingQueue();
      break label134;
    }
  }

  void clear()
  {
    if (this.count == 0)
      return;
    lock();
    try
    {
      AtomicReferenceArray localAtomicReferenceArray = this.table;
      Queue localQueue1 = this.map.removalNotificationQueue;
      Queue localQueue2 = MapMakerInternalMap.DISCARDING_QUEUE;
      if (localQueue1 != localQueue2)
      {
        i = 0;
        while (true)
        {
          int j = localAtomicReferenceArray.length();
          if (i >= j)
            break;
          for (MapMakerInternalMap.ReferenceEntry localReferenceEntry = (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray.get(i); localReferenceEntry != null; localReferenceEntry = localReferenceEntry.getNext())
            if (!localReferenceEntry.getValueReference().isComputingReference())
            {
              MapMaker.RemovalCause localRemovalCause = MapMaker.RemovalCause.EXPLICIT;
              enqueueNotification(localReferenceEntry, localRemovalCause);
            }
          i += 1;
        }
      }
      int i = 0;
      while (true)
      {
        int k = localAtomicReferenceArray.length();
        if (i >= k)
          break;
        localAtomicReferenceArray.set(i, null);
        i += 1;
      }
      clearReferenceQueues();
      this.evictionQueue.clear();
      this.expirationQueue.clear();
      this.readCount.set(0);
      int m = this.modCount + 1;
      this.modCount = m;
      this.count = 0;
      return;
    }
    finally
    {
      unlock();
      postWriteCleanup();
    }
  }

  void clearKeyReferenceQueue()
  {
    while (this.keyReferenceQueue.poll() != null);
  }

  void clearReferenceQueues()
  {
    if (this.map.usesKeyReferences())
      clearKeyReferenceQueue();
    if (!this.map.usesValueReferences())
      return;
    clearValueReferenceQueue();
  }

  boolean clearValue(K paramK, int paramInt, MapMakerInternalMap.ValueReference<K, V> paramValueReference)
  {
    boolean bool = false;
    lock();
    try
    {
      AtomicReferenceArray localAtomicReferenceArray = this.table;
      int i = localAtomicReferenceArray.length() + -1;
      int j = paramInt & i;
      MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray.get(j);
      Object localObject1 = localReferenceEntry1;
      if (localObject1 != null)
      {
        Object localObject2 = ((MapMakerInternalMap.ReferenceEntry)localObject1).getKey();
        if ((((MapMakerInternalMap.ReferenceEntry)localObject1).getHash() != paramInt) && (localObject2 != null) && (this.map.keyEquivalence.equivalent(paramK, localObject2)))
          if (((MapMakerInternalMap.ReferenceEntry)localObject1).getValueReference() == paramValueReference)
          {
            MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = removeFromChain(localReferenceEntry1, (MapMakerInternalMap.ReferenceEntry)localObject1);
            localAtomicReferenceArray.set(j, localReferenceEntry2);
            bool = true;
          }
      }
      while (true)
      {
        return bool;
        unlock();
        postWriteCleanup();
        continue;
        MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = ((MapMakerInternalMap.ReferenceEntry)localObject1).getNext();
        localObject1 = localReferenceEntry3;
        break;
        unlock();
        postWriteCleanup();
      }
    }
    finally
    {
      unlock();
      postWriteCleanup();
    }
  }

  void clearValueReferenceQueue()
  {
    while (this.valueReferenceQueue.poll() != null);
  }

  boolean containsKey(Object paramObject, int paramInt)
  {
    boolean bool = false;
    try
    {
      Object localObject1;
      Object localObject2;
      if (this.count != 0)
      {
        localObject1 = getLiveEntry(paramObject, paramInt);
        localObject2 = localObject1;
        if (localObject2 != null);
      }
      while (true)
      {
        return bool;
        localObject1 = localObject2.getValueReference().get();
        if (localObject1 != null)
          bool = true;
        postReadCleanup();
        continue;
        postReadCleanup();
      }
    }
    finally
    {
      postReadCleanup();
    }
  }

  boolean containsValue(Object paramObject)
  {
    try
    {
      if (this.count != 0)
      {
        AtomicReferenceArray localAtomicReferenceArray = this.table;
        int i = localAtomicReferenceArray.length();
        int j = 0;
        while (j < i)
        {
          MapMakerInternalMap.ReferenceEntry localReferenceEntry = (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray.get(j);
          if (localReferenceEntry != null)
          {
            Object localObject1 = getLiveValue(localReferenceEntry);
            if (localObject1 == null);
            boolean bool1;
            do
            {
              localReferenceEntry = localReferenceEntry.getNext();
              break;
              bool1 = this.map.valueEquivalence.equivalent(paramObject, localObject1);
            }
            while (!bool1);
            bool2 = true;
            return bool2;
          }
          j += 1;
        }
      }
      boolean bool2 = false;
      postReadCleanup();
    }
    finally
    {
      postReadCleanup();
    }
  }

  MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry1, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry2)
  {
    MapMakerInternalMap.ValueReference localValueReference1 = paramReferenceEntry1.getValueReference();
    MapMakerInternalMap.ReferenceEntry localReferenceEntry = this.map.entryFactory.copyEntry(this, paramReferenceEntry1, paramReferenceEntry2);
    ReferenceQueue localReferenceQueue = this.valueReferenceQueue;
    MapMakerInternalMap.ValueReference localValueReference2 = localValueReference1.copyFor(localReferenceQueue, localReferenceEntry);
    localReferenceEntry.setValueReference(localValueReference2);
    return localReferenceEntry;
  }

  void drainKeyReferenceQueue()
  {
    int i = 0;
    do
    {
      Reference localReference = this.keyReferenceQueue.poll();
      if (localReference == null)
        return;
      MapMakerInternalMap.ReferenceEntry localReferenceEntry = (MapMakerInternalMap.ReferenceEntry)localReference;
      this.map.reclaimKey(localReferenceEntry);
      i += 1;
    }
    while (i != 16);
  }

  void drainRecencyQueue()
  {
    while (true)
    {
      MapMakerInternalMap.ReferenceEntry localReferenceEntry = (MapMakerInternalMap.ReferenceEntry)this.recencyQueue.poll();
      if (localReferenceEntry == null)
        return;
      if (this.evictionQueue.contains(localReferenceEntry))
        boolean bool1 = this.evictionQueue.add(localReferenceEntry);
      if ((this.map.expiresAfterAccess()) && (this.expirationQueue.contains(localReferenceEntry)))
        boolean bool2 = this.expirationQueue.add(localReferenceEntry);
    }
  }

  void drainReferenceQueues()
  {
    if (this.map.usesKeyReferences())
      drainKeyReferenceQueue();
    if (!this.map.usesValueReferences())
      return;
    drainValueReferenceQueue();
  }

  void drainValueReferenceQueue()
  {
    int i = 0;
    do
    {
      Reference localReference = this.valueReferenceQueue.poll();
      if (localReference == null)
        return;
      MapMakerInternalMap.ValueReference localValueReference = (MapMakerInternalMap.ValueReference)localReference;
      this.map.reclaimValue(localValueReference);
      i += 1;
    }
    while (i != 16);
  }

  void enqueueNotification(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry, MapMaker.RemovalCause paramRemovalCause)
  {
    Object localObject1 = paramReferenceEntry.getKey();
    int i = paramReferenceEntry.getHash();
    Object localObject2 = paramReferenceEntry.getValueReference().get();
    enqueueNotification(localObject1, i, localObject2, paramRemovalCause);
  }

  void enqueueNotification(K paramK, int paramInt, V paramV, MapMaker.RemovalCause paramRemovalCause)
  {
    Queue localQueue1 = this.map.removalNotificationQueue;
    Queue localQueue2 = MapMakerInternalMap.DISCARDING_QUEUE;
    if (localQueue1 == localQueue2)
      return;
    MapMaker.RemovalNotification localRemovalNotification = new MapMaker.RemovalNotification(paramK, paramV, paramRemovalCause);
    boolean bool = this.map.removalNotificationQueue.offer(localRemovalNotification);
  }

  boolean evictEntries()
  {
    if (this.map.evictsBySize())
    {
      int i = this.count;
      int j = this.maxSegmentSize;
      if (i >= j)
      {
        drainRecencyQueue();
        MapMakerInternalMap.ReferenceEntry localReferenceEntry = (MapMakerInternalMap.ReferenceEntry)this.evictionQueue.remove();
        int k = localReferenceEntry.getHash();
        MapMaker.RemovalCause localRemovalCause = MapMaker.RemovalCause.SIZE;
        if (!removeEntry(localReferenceEntry, k, localRemovalCause))
          throw new AssertionError();
      }
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  void expand()
  {
    AtomicReferenceArray localAtomicReferenceArray1 = this.table;
    int i = localAtomicReferenceArray1.length();
    int j = 1073741824;
    if (i >= j)
      return;
    int k = this.count;
    int m = i << 1;
    Segment localSegment = this;
    int n = m;
    AtomicReferenceArray localAtomicReferenceArray2 = localSegment.newEntryArray(n);
    int i1 = localAtomicReferenceArray2.length() * 3 / 4;
    this.threshold = i1;
    int i2 = localAtomicReferenceArray2.length() + -1;
    int i3 = 0;
    while (i3 < i)
    {
      MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray1.get(i3);
      MapMakerInternalMap.ReferenceEntry localReferenceEntry2;
      int i4;
      if (localReferenceEntry1 != null)
      {
        localReferenceEntry2 = localReferenceEntry1.getNext();
        i4 = localReferenceEntry1.getHash() & i2;
        if (localReferenceEntry2 == null)
          localAtomicReferenceArray2.set(i4, localReferenceEntry1);
      }
      else
      {
        i3 += 1;
        continue;
      }
      Object localObject = localReferenceEntry1;
      int i5 = i4;
      for (MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = localReferenceEntry2; localReferenceEntry3 != null; localReferenceEntry3 = localReferenceEntry3.getNext())
      {
        int i6 = localReferenceEntry3.getHash() & i2;
        int i7 = i5;
        if (i6 != i7)
        {
          i5 = i6;
          localObject = localReferenceEntry3;
        }
      }
      int i8 = i5;
      localAtomicReferenceArray2.set(i8, localObject);
      MapMakerInternalMap.ReferenceEntry localReferenceEntry4 = localReferenceEntry1;
      label220: if (localReferenceEntry4 != localObject)
      {
        if (!isCollected(localReferenceEntry4))
          break label261;
        removeCollectedEntry(localReferenceEntry4);
        k += -1;
      }
      while (true)
      {
        localReferenceEntry4 = localReferenceEntry4.getNext();
        break label220;
        break;
        label261: int i9 = localReferenceEntry4.getHash() & i2;
        MapMakerInternalMap.ReferenceEntry localReferenceEntry5 = (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray2.get(i9);
        MapMakerInternalMap.ReferenceEntry localReferenceEntry6 = copyEntry(localReferenceEntry4, localReferenceEntry5);
        localAtomicReferenceArray2.set(i9, localReferenceEntry6);
      }
    }
    this.table = localAtomicReferenceArray2;
    this.count = k;
  }

  void expireEntries()
  {
    drainRecencyQueue();
    if (this.expirationQueue.isEmpty())
      return;
    long l = this.map.ticker.read();
    MapMakerInternalMap.ReferenceEntry localReferenceEntry;
    int i;
    MapMaker.RemovalCause localRemovalCause;
    do
    {
      localReferenceEntry = (MapMakerInternalMap.ReferenceEntry)this.expirationQueue.peek();
      if (localReferenceEntry == null)
        return;
      if (!this.map.isExpired(localReferenceEntry, l))
        return;
      i = localReferenceEntry.getHash();
      localRemovalCause = MapMaker.RemovalCause.EXPIRED;
    }
    while (removeEntry(localReferenceEntry, i, localRemovalCause));
    throw new AssertionError();
  }

  V get(Object paramObject, int paramInt)
  {
    try
    {
      MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = getLiveEntry(paramObject, paramInt);
      MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = localReferenceEntry1;
      if (localReferenceEntry2 == null)
      {
        localObject1 = null;
        return localObject1;
      }
      Object localObject1 = localReferenceEntry2.getValueReference().get();
      if (localObject1 != null)
        recordRead(localReferenceEntry2);
      while (true)
      {
        postReadCleanup();
        break;
        tryDrainReferenceQueues();
      }
    }
    finally
    {
      postReadCleanup();
    }
  }

  MapMakerInternalMap.ReferenceEntry<K, V> getEntry(Object paramObject, int paramInt)
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry;
    if (this.count != 0)
    {
      localReferenceEntry = getFirst(paramInt);
      if (localReferenceEntry != null)
      {
        if (localReferenceEntry.getHash() != paramInt);
        Object localObject;
        label57: 
        do
          while (true)
          {
            localReferenceEntry = localReferenceEntry.getNext();
            break;
            localObject = localReferenceEntry.getKey();
            if (localObject != null)
              break label57;
            tryDrainReferenceQueues();
          }
        while (!this.map.keyEquivalence.equivalent(paramObject, localObject));
      }
    }
    while (true)
    {
      return localReferenceEntry;
      localReferenceEntry = null;
    }
  }

  MapMakerInternalMap.ReferenceEntry<K, V> getFirst(int paramInt)
  {
    AtomicReferenceArray localAtomicReferenceArray = this.table;
    int i = localAtomicReferenceArray.length() + -1 & paramInt;
    return (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray.get(i);
  }

  MapMakerInternalMap.ReferenceEntry<K, V> getLiveEntry(Object paramObject, int paramInt)
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry = getEntry(paramObject, paramInt);
    if (localReferenceEntry == null);
    for (localReferenceEntry = null; ; localReferenceEntry = null)
    {
      do
        return localReferenceEntry;
      while ((!this.map.expires()) || (!this.map.isExpired(localReferenceEntry)));
      tryExpireEntries();
    }
  }

  V getLiveValue(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    Object localObject;
    if (paramReferenceEntry.getKey() == null)
    {
      tryDrainReferenceQueues();
      localObject = null;
    }
    while (true)
    {
      return localObject;
      localObject = paramReferenceEntry.getValueReference().get();
      if (localObject == null)
      {
        tryDrainReferenceQueues();
        localObject = null;
      }
      else if ((this.map.expires()) && (this.map.isExpired(paramReferenceEntry)))
      {
        tryExpireEntries();
        localObject = null;
      }
    }
  }

  void initTable(AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> paramAtomicReferenceArray)
  {
    int i = paramAtomicReferenceArray.length() * 3 / 4;
    this.threshold = i;
    int j = this.threshold;
    int k = this.maxSegmentSize;
    if (j != k)
    {
      int m = this.threshold + 1;
      this.threshold = m;
    }
    this.table = paramAtomicReferenceArray;
  }

  boolean isCollected(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    if (paramReferenceEntry.getKey() == null);
    MapMakerInternalMap.ValueReference localValueReference;
    for (boolean bool = true; ; bool = isCollected(localValueReference))
    {
      return bool;
      localValueReference = paramReferenceEntry.getValueReference();
    }
  }

  boolean isCollected(MapMakerInternalMap.ValueReference<K, V> paramValueReference)
  {
    boolean bool = false;
    if (paramValueReference.isComputingReference());
    while (true)
    {
      return bool;
      if (paramValueReference.get() == null)
        bool = true;
    }
  }

  MapMakerInternalMap.ReferenceEntry<K, V> newEntry(K paramK, int paramInt, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    return this.map.entryFactory.newEntry(this, paramK, paramInt, paramReferenceEntry);
  }

  AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> newEntryArray(int paramInt)
  {
    return new AtomicReferenceArray(paramInt);
  }

  void postReadCleanup()
  {
    if ((this.readCount.incrementAndGet() & 0x3F) != 0)
      return;
    runCleanup();
  }

  void postWriteCleanup()
  {
    runUnlockedCleanup();
  }

  void preWriteCleanup()
  {
    runLockedCleanup();
  }

  V put(K paramK, int paramInt, V paramV, boolean paramBoolean)
  {
    lock();
    try
    {
      preWriteCleanup();
      int i = this.count + 1;
      int j = this.threshold;
      if (i > j)
      {
        expand();
        i = this.count + 1;
      }
      AtomicReferenceArray localAtomicReferenceArray = this.table;
      int k = localAtomicReferenceArray.length() + -1;
      int m = paramInt & k;
      MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray.get(m);
      MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = localReferenceEntry1;
      Object localObject2;
      if (localReferenceEntry2 != null)
      {
        Object localObject1 = localReferenceEntry2.getKey();
        if ((localReferenceEntry2.getHash() != paramInt) && (localObject1 != null) && (this.map.keyEquivalence.equivalent(paramK, localObject1)))
        {
          MapMakerInternalMap.ValueReference localValueReference = localReferenceEntry2.getValueReference();
          localObject2 = localValueReference.get();
          if (localObject2 == null)
          {
            int n = this.modCount + 1;
            this.modCount = n;
            setValue(localReferenceEntry2, paramV);
            if (!localValueReference.isComputingReference())
            {
              MapMaker.RemovalCause localRemovalCause1 = MapMaker.RemovalCause.COLLECTED;
              enqueueNotification(paramK, paramInt, localObject2, localRemovalCause1);
              i = this.count;
              label200: this.count = i;
              unlock();
              postWriteCleanup();
              localObject2 = null;
            }
          }
        }
      }
      while (true)
      {
        return localObject2;
        if (!evictEntries())
          break label200;
        i = this.count + 1;
        break label200;
        if (paramBoolean)
        {
          recordLockedRead(localReferenceEntry2);
          unlock();
          postWriteCleanup();
        }
        else
        {
          int i1 = this.modCount + 1;
          this.modCount = i1;
          MapMaker.RemovalCause localRemovalCause2 = MapMaker.RemovalCause.REPLACED;
          enqueueNotification(paramK, paramInt, localObject2, localRemovalCause2);
          setValue(localReferenceEntry2, paramV);
          unlock();
          postWriteCleanup();
          continue;
          localReferenceEntry2 = localReferenceEntry2.getNext();
          break;
          int i2 = this.modCount + 1;
          this.modCount = i2;
          MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = newEntry(paramK, paramInt, localReferenceEntry1);
          setValue(localReferenceEntry3, paramV);
          localAtomicReferenceArray.set(m, localReferenceEntry3);
          if (evictEntries())
            i = this.count + 1;
          this.count = i;
          unlock();
          postWriteCleanup();
          localObject2 = null;
        }
      }
    }
    finally
    {
      unlock();
      postWriteCleanup();
    }
  }

  boolean reclaimKey(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry, int paramInt)
  {
    lock();
    try
    {
      int i = this.count + -1;
      AtomicReferenceArray localAtomicReferenceArray = this.table;
      int j = localAtomicReferenceArray.length() + -1;
      int k = paramInt & j;
      MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray.get(k);
      Object localObject1 = localReferenceEntry1;
      boolean bool;
      if (localObject1 != null)
        if (localObject1 == paramReferenceEntry)
        {
          int m = this.modCount + 1;
          this.modCount = m;
          Object localObject2 = ((MapMakerInternalMap.ReferenceEntry)localObject1).getKey();
          Object localObject3 = ((MapMakerInternalMap.ReferenceEntry)localObject1).getValueReference().get();
          MapMaker.RemovalCause localRemovalCause = MapMaker.RemovalCause.COLLECTED;
          enqueueNotification(localObject2, paramInt, localObject3, localRemovalCause);
          MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = removeFromChain(localReferenceEntry1, (MapMakerInternalMap.ReferenceEntry)localObject1);
          int n = this.count + -1;
          localAtomicReferenceArray.set(k, localReferenceEntry2);
          this.count = n;
          bool = true;
        }
      while (true)
      {
        return bool;
        MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = ((MapMakerInternalMap.ReferenceEntry)localObject1).getNext();
        localObject1 = localReferenceEntry3;
        break;
        bool = false;
        unlock();
        postWriteCleanup();
      }
    }
    finally
    {
      unlock();
      postWriteCleanup();
    }
  }

  boolean reclaimValue(K paramK, int paramInt, MapMakerInternalMap.ValueReference<K, V> paramValueReference)
  {
    boolean bool = false;
    lock();
    try
    {
      int i = this.count + -1;
      AtomicReferenceArray localAtomicReferenceArray = this.table;
      int j = localAtomicReferenceArray.length() + -1;
      int k = paramInt & j;
      MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray.get(k);
      Object localObject1 = localReferenceEntry1;
      if (localObject1 != null)
      {
        Object localObject2 = ((MapMakerInternalMap.ReferenceEntry)localObject1).getKey();
        if ((((MapMakerInternalMap.ReferenceEntry)localObject1).getHash() != paramInt) && (localObject2 != null) && (this.map.keyEquivalence.equivalent(paramK, localObject2)))
          if (((MapMakerInternalMap.ReferenceEntry)localObject1).getValueReference() == paramValueReference)
          {
            int m = this.modCount + 1;
            this.modCount = m;
            Object localObject3 = paramValueReference.get();
            MapMaker.RemovalCause localRemovalCause = MapMaker.RemovalCause.COLLECTED;
            enqueueNotification(paramK, paramInt, localObject3, localRemovalCause);
            MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = removeFromChain(localReferenceEntry1, (MapMakerInternalMap.ReferenceEntry)localObject1);
            int n = this.count + -1;
            localAtomicReferenceArray.set(k, localReferenceEntry2);
            this.count = n;
            bool = true;
          }
      }
      while (true)
      {
        return bool;
        unlock();
        if (!isHeldByCurrentThread())
        {
          postWriteCleanup();
          continue;
          MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = ((MapMakerInternalMap.ReferenceEntry)localObject1).getNext();
          localObject1 = localReferenceEntry3;
          break;
          unlock();
          if (!isHeldByCurrentThread())
            postWriteCleanup();
        }
      }
    }
    finally
    {
      unlock();
      if (!isHeldByCurrentThread())
        postWriteCleanup();
    }
  }

  void recordExpirationTime(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry, long paramLong)
  {
    long l = this.map.ticker.read() + paramLong;
    paramReferenceEntry.setExpirationTime(l);
  }

  void recordLockedRead(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    boolean bool1 = this.evictionQueue.add(paramReferenceEntry);
    if (!this.map.expiresAfterAccess())
      return;
    long l = this.map.expireAfterAccessNanos;
    recordExpirationTime(paramReferenceEntry, l);
    boolean bool2 = this.expirationQueue.add(paramReferenceEntry);
  }

  void recordRead(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    if (this.map.expiresAfterAccess())
    {
      long l = this.map.expireAfterAccessNanos;
      recordExpirationTime(paramReferenceEntry, l);
    }
    boolean bool = this.recencyQueue.add(paramReferenceEntry);
  }

  void recordWrite(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    drainRecencyQueue();
    boolean bool1 = this.evictionQueue.add(paramReferenceEntry);
    if (!this.map.expires())
      return;
    if (this.map.expiresAfterAccess());
    for (long l = this.map.expireAfterAccessNanos; ; l = this.map.expireAfterWriteNanos)
    {
      recordExpirationTime(paramReferenceEntry, l);
      boolean bool2 = this.expirationQueue.add(paramReferenceEntry);
      return;
    }
  }

  V remove(Object paramObject, int paramInt)
  {
    lock();
    try
    {
      preWriteCleanup();
      int i = this.count + -1;
      AtomicReferenceArray localAtomicReferenceArray = this.table;
      int j = localAtomicReferenceArray.length() + -1;
      int k = paramInt & j;
      MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray.get(k);
      Object localObject1 = localReferenceEntry1;
      MapMakerInternalMap.ValueReference localValueReference;
      Object localObject3;
      MapMaker.RemovalCause localRemovalCause;
      if (localObject1 != null)
      {
        Object localObject2 = ((MapMakerInternalMap.ReferenceEntry)localObject1).getKey();
        if ((((MapMakerInternalMap.ReferenceEntry)localObject1).getHash() != paramInt) && (localObject2 != null) && (this.map.keyEquivalence.equivalent(paramObject, localObject2)))
        {
          localValueReference = ((MapMakerInternalMap.ReferenceEntry)localObject1).getValueReference();
          localObject3 = localValueReference.get();
          if (localObject3 != null)
          {
            localRemovalCause = MapMaker.RemovalCause.EXPLICIT;
            label128: int m = this.modCount + 1;
            this.modCount = m;
            enqueueNotification(localObject2, paramInt, localObject3, localRemovalCause);
            MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = removeFromChain(localReferenceEntry1, (MapMakerInternalMap.ReferenceEntry)localObject1);
            int n = this.count + -1;
            localAtomicReferenceArray.set(k, localReferenceEntry2);
            this.count = n;
          }
        }
      }
      while (true)
      {
        return localObject3;
        if (isCollected(localValueReference))
        {
          localRemovalCause = MapMaker.RemovalCause.COLLECTED;
          break label128;
        }
        unlock();
        postWriteCleanup();
        localObject3 = null;
        continue;
        MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = ((MapMakerInternalMap.ReferenceEntry)localObject1).getNext();
        localObject1 = localReferenceEntry3;
        break;
        unlock();
        postWriteCleanup();
        localObject3 = null;
      }
    }
    finally
    {
      unlock();
      postWriteCleanup();
    }
  }

  boolean remove(Object paramObject1, int paramInt, Object paramObject2)
  {
    boolean bool = false;
    lock();
    try
    {
      preWriteCleanup();
      int i = this.count + -1;
      AtomicReferenceArray localAtomicReferenceArray = this.table;
      int j = localAtomicReferenceArray.length() + -1;
      int k = paramInt & j;
      MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray.get(k);
      Object localObject1 = localReferenceEntry1;
      MapMakerInternalMap.ValueReference localValueReference;
      MapMaker.RemovalCause localRemovalCause1;
      if (localObject1 != null)
      {
        Object localObject2 = ((MapMakerInternalMap.ReferenceEntry)localObject1).getKey();
        if ((((MapMakerInternalMap.ReferenceEntry)localObject1).getHash() != paramInt) && (localObject2 != null) && (this.map.keyEquivalence.equivalent(paramObject1, localObject2)))
        {
          localValueReference = ((MapMakerInternalMap.ReferenceEntry)localObject1).getValueReference();
          Object localObject3 = localValueReference.get();
          if (this.map.valueEquivalence.equivalent(paramObject2, localObject3))
          {
            localRemovalCause1 = MapMaker.RemovalCause.EXPLICIT;
            label143: int m = this.modCount + 1;
            this.modCount = m;
            enqueueNotification(localObject2, paramInt, localObject3, localRemovalCause1);
            MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = removeFromChain(localReferenceEntry1, (MapMakerInternalMap.ReferenceEntry)localObject1);
            int n = this.count + -1;
            localAtomicReferenceArray.set(k, localReferenceEntry2);
            this.count = n;
            MapMaker.RemovalCause localRemovalCause2 = MapMaker.RemovalCause.EXPLICIT;
            if (localRemovalCause1 == localRemovalCause2)
              bool = true;
          }
        }
      }
      while (true)
      {
        return bool;
        if (isCollected(localValueReference))
        {
          localRemovalCause1 = MapMaker.RemovalCause.COLLECTED;
          break label143;
        }
        unlock();
        postWriteCleanup();
        continue;
        MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = ((MapMakerInternalMap.ReferenceEntry)localObject1).getNext();
        localObject1 = localReferenceEntry3;
        break;
        unlock();
        postWriteCleanup();
      }
    }
    finally
    {
      unlock();
      postWriteCleanup();
    }
  }

  void removeCollectedEntry(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    MapMaker.RemovalCause localRemovalCause = MapMaker.RemovalCause.COLLECTED;
    enqueueNotification(paramReferenceEntry, localRemovalCause);
    boolean bool1 = this.evictionQueue.remove(paramReferenceEntry);
    boolean bool2 = this.expirationQueue.remove(paramReferenceEntry);
  }

  boolean removeEntry(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry, int paramInt, MapMaker.RemovalCause paramRemovalCause)
  {
    int i = this.count + -1;
    AtomicReferenceArray localAtomicReferenceArray = this.table;
    int j = localAtomicReferenceArray.length() + -1;
    int k = paramInt & j;
    MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray.get(k);
    MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = localReferenceEntry1;
    if (localReferenceEntry2 != null)
      if (localReferenceEntry2 == paramReferenceEntry)
      {
        int m = this.modCount + 1;
        this.modCount = m;
        Object localObject1 = localReferenceEntry2.getKey();
        Object localObject2 = localReferenceEntry2.getValueReference().get();
        enqueueNotification(localObject1, paramInt, localObject2, paramRemovalCause);
        MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = removeFromChain(localReferenceEntry1, localReferenceEntry2);
        int n = this.count + -1;
        localAtomicReferenceArray.set(k, localReferenceEntry3);
        this.count = n;
      }
    for (boolean bool = true; ; bool = false)
    {
      return bool;
      localReferenceEntry2 = localReferenceEntry2.getNext();
      break;
    }
  }

  MapMakerInternalMap.ReferenceEntry<K, V> removeFromChain(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry1, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry2)
  {
    boolean bool1 = this.evictionQueue.remove(paramReferenceEntry2);
    boolean bool2 = this.expirationQueue.remove(paramReferenceEntry2);
    int i = this.count;
    MapMakerInternalMap.ReferenceEntry localReferenceEntry = paramReferenceEntry2.getNext();
    Object localObject = paramReferenceEntry1;
    if (localObject != paramReferenceEntry2)
    {
      if (isCollected((MapMakerInternalMap.ReferenceEntry)localObject))
      {
        removeCollectedEntry((MapMakerInternalMap.ReferenceEntry)localObject);
        i += -1;
      }
      while (true)
      {
        localObject = ((MapMakerInternalMap.ReferenceEntry)localObject).getNext();
        break;
        localReferenceEntry = copyEntry((MapMakerInternalMap.ReferenceEntry)localObject, localReferenceEntry);
      }
    }
    this.count = i;
    return localReferenceEntry;
  }

  V replace(K paramK, int paramInt, V paramV)
  {
    lock();
    try
    {
      preWriteCleanup();
      AtomicReferenceArray localAtomicReferenceArray = this.table;
      int i = localAtomicReferenceArray.length() + -1;
      int j = paramInt & i;
      MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray.get(j);
      Object localObject1 = localReferenceEntry1;
      Object localObject3;
      if (localObject1 != null)
      {
        Object localObject2 = ((MapMakerInternalMap.ReferenceEntry)localObject1).getKey();
        if ((((MapMakerInternalMap.ReferenceEntry)localObject1).getHash() != paramInt) && (localObject2 != null) && (this.map.keyEquivalence.equivalent(paramK, localObject2)))
        {
          MapMakerInternalMap.ValueReference localValueReference = ((MapMakerInternalMap.ReferenceEntry)localObject1).getValueReference();
          localObject3 = localValueReference.get();
          if (localObject3 == null)
          {
            if (isCollected(localValueReference))
            {
              int k = this.count + -1;
              int m = this.modCount + 1;
              this.modCount = m;
              MapMaker.RemovalCause localRemovalCause1 = MapMaker.RemovalCause.COLLECTED;
              enqueueNotification(localObject2, paramInt, localObject3, localRemovalCause1);
              MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = removeFromChain(localReferenceEntry1, (MapMakerInternalMap.ReferenceEntry)localObject1);
              int n = this.count + -1;
              localAtomicReferenceArray.set(j, localReferenceEntry2);
              this.count = n;
            }
            unlock();
            postWriteCleanup();
            localObject3 = null;
          }
        }
      }
      while (true)
      {
        return localObject3;
        int i1 = this.modCount + 1;
        this.modCount = i1;
        MapMaker.RemovalCause localRemovalCause2 = MapMaker.RemovalCause.REPLACED;
        enqueueNotification(paramK, paramInt, localObject3, localRemovalCause2);
        setValue((MapMakerInternalMap.ReferenceEntry)localObject1, paramV);
        unlock();
        postWriteCleanup();
        continue;
        MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = ((MapMakerInternalMap.ReferenceEntry)localObject1).getNext();
        localObject1 = localReferenceEntry3;
        break;
        unlock();
        postWriteCleanup();
        localObject3 = null;
      }
    }
    finally
    {
      unlock();
      postWriteCleanup();
    }
  }

  boolean replace(K paramK, int paramInt, V paramV1, V paramV2)
  {
    boolean bool = false;
    lock();
    try
    {
      preWriteCleanup();
      AtomicReferenceArray localAtomicReferenceArray = this.table;
      int i = localAtomicReferenceArray.length() + -1;
      int j = paramInt & i;
      MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray.get(j);
      Object localObject1 = localReferenceEntry1;
      Object localObject3;
      if (localObject1 != null)
      {
        Object localObject2 = ((MapMakerInternalMap.ReferenceEntry)localObject1).getKey();
        if ((((MapMakerInternalMap.ReferenceEntry)localObject1).getHash() != paramInt) && (localObject2 != null) && (this.map.keyEquivalence.equivalent(paramK, localObject2)))
        {
          MapMakerInternalMap.ValueReference localValueReference = ((MapMakerInternalMap.ReferenceEntry)localObject1).getValueReference();
          localObject3 = localValueReference.get();
          if (localObject3 == null)
            if (isCollected(localValueReference))
            {
              int k = this.count + -1;
              int m = this.modCount + 1;
              this.modCount = m;
              MapMaker.RemovalCause localRemovalCause1 = MapMaker.RemovalCause.COLLECTED;
              enqueueNotification(localObject2, paramInt, localObject3, localRemovalCause1);
              MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = removeFromChain(localReferenceEntry1, (MapMakerInternalMap.ReferenceEntry)localObject1);
              int n = this.count + -1;
              localAtomicReferenceArray.set(j, localReferenceEntry2);
              this.count = n;
            }
        }
      }
      while (true)
      {
        return bool;
        if (this.map.valueEquivalence.equivalent(paramV1, localObject3))
        {
          int i1 = this.modCount + 1;
          this.modCount = i1;
          MapMaker.RemovalCause localRemovalCause2 = MapMaker.RemovalCause.REPLACED;
          enqueueNotification(paramK, paramInt, localObject3, localRemovalCause2);
          setValue((MapMakerInternalMap.ReferenceEntry)localObject1, paramV2);
          bool = true;
          unlock();
          postWriteCleanup();
        }
        else
        {
          recordLockedRead((MapMakerInternalMap.ReferenceEntry)localObject1);
          unlock();
          postWriteCleanup();
          continue;
          MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = ((MapMakerInternalMap.ReferenceEntry)localObject1).getNext();
          localObject1 = localReferenceEntry3;
          break;
          unlock();
          postWriteCleanup();
        }
      }
    }
    finally
    {
      unlock();
      postWriteCleanup();
    }
  }

  void runCleanup()
  {
    runLockedCleanup();
    runUnlockedCleanup();
  }

  void runLockedCleanup()
  {
    if (!tryLock())
      return;
    try
    {
      drainReferenceQueues();
      expireEntries();
      this.readCount.set(0);
      return;
    }
    finally
    {
      unlock();
    }
  }

  void runUnlockedCleanup()
  {
    if (isHeldByCurrentThread())
      return;
    this.map.processPendingNotifications();
  }

  void setValue(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry, V paramV)
  {
    MapMakerInternalMap.ValueReference localValueReference = this.map.valueStrength.referenceValue(this, paramReferenceEntry, paramV);
    paramReferenceEntry.setValueReference(localValueReference);
    recordWrite(paramReferenceEntry);
  }

  void tryDrainReferenceQueues()
  {
    if (!tryLock())
      return;
    try
    {
      drainReferenceQueues();
      return;
    }
    finally
    {
      unlock();
    }
  }

  void tryExpireEntries()
  {
    if (!tryLock())
      return;
    try
    {
      expireEntries();
      return;
    }
    finally
    {
      unlock();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.Segment
 * JD-Core Version:    0.6.2
 */