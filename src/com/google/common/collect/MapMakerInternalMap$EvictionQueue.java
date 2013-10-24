package com.google.common.collect;

import java.util.AbstractQueue;
import java.util.Iterator;

final class MapMakerInternalMap$EvictionQueue<K, V> extends AbstractQueue<MapMakerInternalMap.ReferenceEntry<K, V>>
{
  final MapMakerInternalMap.ReferenceEntry<K, V> head;

  MapMakerInternalMap$EvictionQueue()
  {
    MapMakerInternalMap.AbstractReferenceEntry local1 = new MapMakerInternalMap.AbstractReferenceEntry()
    {
      MapMakerInternalMap.ReferenceEntry<K, V> nextEvictable = this;
      MapMakerInternalMap.ReferenceEntry<K, V> previousEvictable = this;

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextEvictable()
      {
        return this.nextEvictable;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousEvictable()
      {
        return this.previousEvictable;
      }

      public void setNextEvictable(MapMakerInternalMap.ReferenceEntry<K, V> paramAnonymousReferenceEntry)
      {
        this.nextEvictable = paramAnonymousReferenceEntry;
      }

      public void setPreviousEvictable(MapMakerInternalMap.ReferenceEntry<K, V> paramAnonymousReferenceEntry)
      {
        this.previousEvictable = paramAnonymousReferenceEntry;
      }
    };
    this.head = local1;
  }

  public void clear()
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry2;
    for (Object localObject = this.head.getNextEvictable(); ; localObject = localReferenceEntry2)
    {
      MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = this.head;
      if (localObject == localReferenceEntry1)
        break;
      localReferenceEntry2 = ((MapMakerInternalMap.ReferenceEntry)localObject).getNextEvictable();
      MapMakerInternalMap.nullifyEvictable((MapMakerInternalMap.ReferenceEntry)localObject);
    }
    MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = this.head;
    MapMakerInternalMap.ReferenceEntry localReferenceEntry4 = this.head;
    localReferenceEntry3.setNextEvictable(localReferenceEntry4);
    MapMakerInternalMap.ReferenceEntry localReferenceEntry5 = this.head;
    MapMakerInternalMap.ReferenceEntry localReferenceEntry6 = this.head;
    localReferenceEntry5.setPreviousEvictable(localReferenceEntry6);
  }

  public boolean contains(Object paramObject)
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry = ((MapMakerInternalMap.ReferenceEntry)paramObject).getNextEvictable();
    MapMakerInternalMap.NullEntry localNullEntry = MapMakerInternalMap.NullEntry.INSTANCE;
    if (localReferenceEntry != localNullEntry);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isEmpty()
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = this.head.getNextEvictable();
    MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = this.head;
    if (localReferenceEntry1 == localReferenceEntry2);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public Iterator<MapMakerInternalMap.ReferenceEntry<K, V>> iterator()
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry = peek();
    return new AbstractLinkedIterator(localReferenceEntry)
    {
      protected MapMakerInternalMap.ReferenceEntry<K, V> computeNext(MapMakerInternalMap.ReferenceEntry<K, V> paramAnonymousReferenceEntry)
      {
        MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = paramAnonymousReferenceEntry.getNextEvictable();
        MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = MapMakerInternalMap.EvictionQueue.this.head;
        if (localReferenceEntry1 == localReferenceEntry2)
          localReferenceEntry1 = null;
        return localReferenceEntry1;
      }
    };
  }

  public boolean offer(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = paramReferenceEntry.getPreviousEvictable();
    MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = paramReferenceEntry.getNextEvictable();
    MapMakerInternalMap.connectEvictables(localReferenceEntry1, localReferenceEntry2);
    MapMakerInternalMap.connectEvictables(this.head.getPreviousEvictable(), paramReferenceEntry);
    MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = this.head;
    MapMakerInternalMap.connectEvictables(paramReferenceEntry, localReferenceEntry3);
    return true;
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> peek()
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = this.head.getNextEvictable();
    MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = this.head;
    if (localReferenceEntry1 == localReferenceEntry2)
      localReferenceEntry1 = null;
    return localReferenceEntry1;
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> poll()
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = this.head.getNextEvictable();
    MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = this.head;
    if (localReferenceEntry1 == localReferenceEntry2)
      localReferenceEntry1 = null;
    while (true)
    {
      return localReferenceEntry1;
      boolean bool = remove(localReferenceEntry1);
    }
  }

  public boolean remove(Object paramObject)
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = (MapMakerInternalMap.ReferenceEntry)paramObject;
    MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = localReferenceEntry1.getPreviousEvictable();
    MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = localReferenceEntry1.getNextEvictable();
    MapMakerInternalMap.connectEvictables(localReferenceEntry2, localReferenceEntry3);
    MapMakerInternalMap.nullifyEvictable(localReferenceEntry1);
    MapMakerInternalMap.NullEntry localNullEntry = MapMakerInternalMap.NullEntry.INSTANCE;
    if (localReferenceEntry3 != localNullEntry);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public int size()
  {
    int i = 0;
    for (MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = this.head.getNextEvictable(); ; localReferenceEntry1 = localReferenceEntry1.getNextEvictable())
    {
      MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = this.head;
      if (localReferenceEntry1 == localReferenceEntry2)
        break;
      i += 1;
    }
    return i;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.EvictionQueue
 * JD-Core Version:    0.6.2
 */