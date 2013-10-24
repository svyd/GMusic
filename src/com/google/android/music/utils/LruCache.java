package com.google.android.music.utils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LruCache<K, V>
{
  private int mCapacity;
  private HashMap<K, V> mLruMap = null;
  private ReferenceQueue<V> mQueue;
  private HashMap<K, Entry<K, V>> mSoftMap;

  public LruCache(int paramInt)
  {
    setCapacity(paramInt);
  }

  private void cleanUpSoftMap()
  {
    if (this.mQueue == null)
      return;
    for (Entry localEntry = (Entry)this.mQueue.poll(); ; localEntry = (Entry)this.mQueue.poll())
    {
      if (localEntry == null)
        return;
      HashMap localHashMap = this.mSoftMap;
      Object localObject1 = localEntry.mKey;
      Object localObject2 = localHashMap.remove(localObject1);
    }
  }

  /** @deprecated */
  public void clear()
  {
    try
    {
      if (this.mLruMap != null)
      {
        this.mLruMap.clear();
        this.mSoftMap.clear();
        ReferenceQueue localReferenceQueue = new ReferenceQueue();
        this.mQueue = localReferenceQueue;
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  public V get(K paramK)
  {
    Object localObject1 = null;
    try
    {
      HashMap localHashMap = this.mLruMap;
      if (localHashMap == null);
      while (true)
      {
        return localObject1;
        cleanUpSoftMap();
        Object localObject2 = this.mLruMap.get(paramK);
        if (localObject2 != null)
        {
          localObject1 = localObject2;
        }
        else
        {
          Entry localEntry = (Entry)this.mSoftMap.get(paramK);
          if (localEntry != null)
          {
            Object localObject3 = localEntry.get();
            localObject1 = localObject3;
          }
        }
      }
    }
    finally
    {
    }
  }

  /** @deprecated */
  public V put(K paramK, V paramV)
  {
    Object localObject1 = null;
    try
    {
      HashMap localHashMap1 = this.mLruMap;
      if (localHashMap1 == null);
      while (true)
      {
        return localObject1;
        cleanUpSoftMap();
        Object localObject2 = this.mLruMap.put(paramK, paramV);
        HashMap localHashMap2 = this.mSoftMap;
        ReferenceQueue localReferenceQueue = this.mQueue;
        Entry localEntry1 = new Entry(paramK, paramV, localReferenceQueue);
        Entry localEntry2 = (Entry)localHashMap2.put(paramK, localEntry1);
        if (localEntry2 != null)
        {
          Object localObject3 = localEntry2.get();
          localObject1 = localObject3;
        }
      }
    }
    finally
    {
    }
  }

  /** @deprecated */
  public void remove(K paramK)
  {
    try
    {
      if (this.mLruMap != null)
      {
        Object localObject1 = this.mSoftMap.remove(paramK);
        Object localObject2 = this.mLruMap.remove(paramK);
      }
      return;
    }
    finally
    {
      localObject3 = finally;
      throw localObject3;
    }
  }

  /** @deprecated */
  public void setCapacity(int paramInt)
  {
    try
    {
      this.mCapacity = paramInt;
      if ((paramInt == 0) && (this.mLruMap != null))
      {
        clear();
        this.mLruMap = null;
        this.mQueue = null;
        this.mSoftMap = null;
      }
      while (true)
      {
        return;
        if (this.mLruMap == null)
        {
          HashMap localHashMap = new HashMap();
          this.mSoftMap = localHashMap;
          ReferenceQueue localReferenceQueue = new ReferenceQueue();
          this.mQueue = localReferenceQueue;
          LinkedHashMap local1 = new LinkedHashMap(16, 0.75F, true)
          {
            protected boolean removeEldestEntry(Map.Entry<K, V> paramAnonymousEntry)
            {
              int i = size();
              int j = LruCache.this.mCapacity;
              if (i > j);
              for (boolean bool = true; ; bool = false)
                return bool;
            }
          };
          this.mLruMap = local1;
        }
      }
    }
    finally
    {
    }
  }

  private static class Entry<K, V> extends SoftReference<V>
  {
    K mKey;

    public Entry(K paramK, V paramV, ReferenceQueue<V> paramReferenceQueue)
    {
      super(paramReferenceQueue);
      this.mKey = paramK;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.LruCache
 * JD-Core Version:    0.6.2
 */