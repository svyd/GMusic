package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReferenceArray;

abstract class MapMakerInternalMap$HashIterator
{
  MapMakerInternalMap.Segment<K, V> currentSegment;
  AtomicReferenceArray<MapMakerInternalMap.ReferenceEntry<K, V>> currentTable;
  MapMakerInternalMap<K, V>.WriteThroughEntry lastReturned;
  MapMakerInternalMap.ReferenceEntry<K, V> nextEntry;
  MapMakerInternalMap<K, V>.WriteThroughEntry nextExternal;
  int nextSegmentIndex;
  int nextTableIndex;

  MapMakerInternalMap$HashIterator(MapMakerInternalMap paramMapMakerInternalMap)
  {
    int i = paramMapMakerInternalMap.segments.length + -1;
    this.nextSegmentIndex = i;
    this.nextTableIndex = -1;
    advance();
  }

  final void advance()
  {
    this.nextExternal = null;
    if (nextInChain())
      return;
    if (nextInTable())
      return;
    do
    {
      do
      {
        if (this.nextSegmentIndex < 0)
          return;
        MapMakerInternalMap.Segment[] arrayOfSegment = this.this$0.segments;
        int i = this.nextSegmentIndex;
        int j = i + -1;
        this.nextSegmentIndex = j;
        MapMakerInternalMap.Segment localSegment = arrayOfSegment[i];
        this.currentSegment = localSegment;
      }
      while (this.currentSegment.count == 0);
      AtomicReferenceArray localAtomicReferenceArray = this.currentSegment.table;
      this.currentTable = localAtomicReferenceArray;
      int k = this.currentTable.length() + -1;
      this.nextTableIndex = k;
    }
    while (!nextInTable());
  }

  boolean advanceTo(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
  {
    try
    {
      Object localObject1 = paramReferenceEntry.getKey();
      Object localObject2 = this.this$0.getLiveValue(paramReferenceEntry);
      if (localObject2 != null)
      {
        MapMakerInternalMap localMapMakerInternalMap = this.this$0;
        MapMakerInternalMap.WriteThroughEntry localWriteThroughEntry = new MapMakerInternalMap.WriteThroughEntry(localMapMakerInternalMap, localObject1, localObject2);
        this.nextExternal = localWriteThroughEntry;
        bool = true;
        return bool;
      }
      boolean bool = false;
      this.currentSegment.postReadCleanup();
    }
    finally
    {
      this.currentSegment.postReadCleanup();
    }
  }

  public boolean hasNext()
  {
    if (this.nextExternal != null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  MapMakerInternalMap<K, V>.WriteThroughEntry nextEntry()
  {
    if (this.nextExternal == null)
      throw new NoSuchElementException();
    MapMakerInternalMap.WriteThroughEntry localWriteThroughEntry = this.nextExternal;
    this.lastReturned = localWriteThroughEntry;
    advance();
    return this.lastReturned;
  }

  boolean nextInChain()
  {
    if (this.nextEntry != null)
    {
      MapMakerInternalMap.ReferenceEntry localReferenceEntry1 = this.nextEntry.getNext();
      this.nextEntry = localReferenceEntry1;
      if (this.nextEntry != null)
      {
        MapMakerInternalMap.ReferenceEntry localReferenceEntry2 = this.nextEntry;
        if (!advanceTo(localReferenceEntry2));
      }
    }
    for (boolean bool = true; ; bool = false)
    {
      return bool;
      MapMakerInternalMap.ReferenceEntry localReferenceEntry3 = this.nextEntry.getNext();
      this.nextEntry = localReferenceEntry3;
      break;
    }
  }

  boolean nextInTable()
  {
    MapMakerInternalMap.ReferenceEntry localReferenceEntry2;
    do
    {
      MapMakerInternalMap.ReferenceEntry localReferenceEntry1;
      do
      {
        if (this.nextTableIndex < 0)
          break;
        AtomicReferenceArray localAtomicReferenceArray = this.currentTable;
        int i = this.nextTableIndex;
        int j = i + -1;
        this.nextTableIndex = j;
        localReferenceEntry1 = (MapMakerInternalMap.ReferenceEntry)localAtomicReferenceArray.get(i);
        this.nextEntry = localReferenceEntry1;
      }
      while (localReferenceEntry1 == null);
      localReferenceEntry2 = this.nextEntry;
    }
    while ((!advanceTo(localReferenceEntry2)) && (!nextInChain()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void remove()
  {
    if (this.lastReturned != null);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkState(bool);
      MapMakerInternalMap localMapMakerInternalMap = this.this$0;
      Object localObject1 = this.lastReturned.getKey();
      Object localObject2 = localMapMakerInternalMap.remove(localObject1);
      this.lastReturned = null;
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.HashIterator
 * JD-Core Version:    0.6.2
 */