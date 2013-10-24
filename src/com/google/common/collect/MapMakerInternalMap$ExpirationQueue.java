package com.google.common.collect;

import java.util.AbstractQueue;
import java.util.Iterator;

final class MapMakerInternalMap$ExpirationQueue<K, V> extends AbstractQueue<MapMakerInternalMap.ReferenceEntry<K, V>>
{
  final MapMakerInternalMap.ReferenceEntry<K, V> head;

  MapMakerInternalMap$ExpirationQueue()
  {
    MapMakerInternalMap.AbstractReferenceEntry local1 = new MapMakerInternalMap.AbstractReferenceEntry()
    {
      MapMakerInternalMap.ReferenceEntry<K, V> nextExpirable = this;
      MapMakerInternalMap.ReferenceEntry<K, V> previousExpirable = this;

      public long getExpirationTime()
      {
        return 9223372036854775807L;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getNextExpirable()
      {
        return this.nextExpirable;
      }

      public MapMakerInternalMap.ReferenceEntry<K, V> getPreviousExpirable()
      {
        return this.previousExpirable;
      }

      public void setExpirationTime(long paramAnonymousLong)
      {
      }

      public void setNextExpirable(MapMakerInternalMap.ReferenceEntry<K, V> paramAnonymousReferenceEntry)
      {
        this.nextExpirable = paramAnonymousReferenceEntry;
      }

      public void setPreviousExpirable(MapMakerInternalMap.ReferenceEntry<K, V> paramAnonymousReferenceEntry)
      {
        this.previousExpirable = paramAnonymousReferenceEntry;
      }
    };
    this.head = local1;
  }

  public void clear()
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry2;
    for (Object localObject = this.head.getNextExpirable(); ; localObject = localReferenceEntry2)
    {
      MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = this.head;
      if (localObject == localReferenceEntry1)
        break;
      localReferenceEntry2 = ((MapMakerInternalMap.ReferenceEntry)localObject).getNextExpirable();
      MapMakerInternalMap.nullifyExpirable((MapMakerInternalMap.ReferenceEntry)localObject);
    }
    MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = this.head;
    MapMakerInternalMap.ReferenceEntry localReferenceEntry4 = this.head;
    localReferenceEntry3.setNextExpirable(localReferenceEntry4);
    MapMakerInternalMap.ReferenceEntry localReferenceEntry5 = this.head;
    MapMakerInternalMap.ReferenceEntry localReferenceEntry6 = this.head;
    localReferenceEntry5.setPreviousExpirable(localReferenceEntry6);
  }

  public boolean contains(Object paramObject)
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry = ((MapMakerInternalMap.ReferenceEntry)paramObject).getNextExpirable();
    MapMakerInternalMap.NullEntry localNullEntry = MapMakerInternalMap.NullEntry.INSTANCE;
    if (localReferenceEntry != localNullEntry);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isEmpty()
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = this.head.getNextExpirable();
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
        MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = paramAnonymousReferenceEntry.getNextExpirable();
        MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = MapMakerInternalMap.ExpirationQueue.this.head;
        if (localReferenceEntry1 == localReferenceEntry2)
          localReferenceEntry1 = null;
        return localReferenceEntry1;
      }
    };
  }

  public boolean offer(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = paramReferenceEntry.getPreviousExpirable();
    MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = paramReferenceEntry.getNextExpirable();
    MapMakerInternalMap.connectExpirables(localReferenceEntry1, localReferenceEntry2);
    MapMakerInternalMap.connectExpirables(this.head.getPreviousExpirable(), paramReferenceEntry);
    MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = this.head;
    MapMakerInternalMap.connectExpirables(paramReferenceEntry, localReferenceEntry3);
    return true;
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> peek()
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = this.head.getNextExpirable();
    MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = this.head;
    if (localReferenceEntry1 == localReferenceEntry2)
      localReferenceEntry1 = null;
    return localReferenceEntry1;
  }

  public MapMakerInternalMap.ReferenceEntry<K, V> poll()
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = this.head.getNextExpirable();
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
    MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = localReferenceEntry1.getPreviousExpirable();
    MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = localReferenceEntry1.getNextExpirable();
    MapMakerInternalMap.connectExpirables(localReferenceEntry2, localReferenceEntry3);
    MapMakerInternalMap.nullifyExpirable(localReferenceEntry1);
    MapMakerInternalMap.NullEntry localNullEntry = MapMakerInternalMap.NullEntry.INSTANCE;
    if (localReferenceEntry3 != localNullEntry);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public int size()
  {
    int i = 0;
    for (MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = this.head.getNextExpirable(); ; localReferenceEntry1 = localReferenceEntry1.getNextExpirable())
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
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.ExpirationQueue
 * JD-Core Version:    0.6.2
 */