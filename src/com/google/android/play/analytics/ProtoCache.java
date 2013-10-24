package com.google.android.play.analytics;

import java.lang.reflect.Array;

public class ProtoCache
{
  private static ProtoCache INSTANCE = null;
  private static final String TAG = ProtoCache.class.getSimpleName();
  private final ElementCache<ClientAnalytics.LogEvent> mCacheLogEvent;
  private final ElementCache<ClientAnalytics.LogEventKeyValues> mCacheLogEventKeyValues;

  private ProtoCache()
  {
    ElementCache localElementCache1 = new ElementCache(ClientAnalytics.LogEvent.class, 60);
    this.mCacheLogEvent = localElementCache1;
    ElementCache localElementCache2 = new ElementCache(ClientAnalytics.LogEventKeyValues.class, 50);
    this.mCacheLogEventKeyValues = localElementCache2;
  }

  /** @deprecated */
  public static ProtoCache getInstance()
  {
    try
    {
      if (INSTANCE == null)
        INSTANCE = new ProtoCache();
      ProtoCache localProtoCache = INSTANCE;
      return localProtoCache;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private void recycle(ClientAnalytics.LogEventKeyValues paramLogEventKeyValues)
  {
    ClientAnalytics.LogEventKeyValues localLogEventKeyValues = paramLogEventKeyValues.clear();
    this.mCacheLogEventKeyValues.recycle(paramLogEventKeyValues);
  }

  public ClientAnalytics.LogEvent obtainEvent()
  {
    return (ClientAnalytics.LogEvent)this.mCacheLogEvent.obtain();
  }

  public ClientAnalytics.LogEventKeyValues obtainKeyValue()
  {
    return (ClientAnalytics.LogEventKeyValues)this.mCacheLogEventKeyValues.obtain();
  }

  public void recycle(ClientAnalytics.LogEvent paramLogEvent)
  {
    ClientAnalytics.LogEventKeyValues[] arrayOfLogEventKeyValues = paramLogEvent.value;
    int i = 0;
    while (true)
    {
      int j = arrayOfLogEventKeyValues.length;
      if (i >= j)
        break;
      ClientAnalytics.LogEventKeyValues localLogEventKeyValues = arrayOfLogEventKeyValues[i];
      recycle(localLogEventKeyValues);
      i += 1;
    }
    ClientAnalytics.LogEvent localLogEvent = paramLogEvent.clear();
    this.mCacheLogEvent.recycle(paramLogEvent);
  }

  public void recycleLogRequest(ClientAnalytics.LogRequest paramLogRequest)
  {
    ClientAnalytics.LogEvent[] arrayOfLogEvent = paramLogRequest.logEvent;
    int i = 0;
    while (true)
    {
      int j = arrayOfLogEvent.length;
      if (i >= j)
        return;
      ClientAnalytics.LogEvent localLogEvent = arrayOfLogEvent[i];
      recycle(localLogEvent);
      i += 1;
    }
  }

  private static class ElementCache<T>
  {
    private T[] mCache;
    Class<?> mClazz;
    private int mCount;
    private int mHighWater;
    private final int mLimit;

    public ElementCache(Class<?> paramClass, int paramInt)
    {
      this.mLimit = paramInt;
      this.mCount = 0;
      this.mHighWater = 0;
      Object[] arrayOfObject = (Object[])Array.newInstance(paramClass, paramInt);
      this.mCache = arrayOfObject;
      this.mClazz = paramClass;
    }

    // ERROR //
    public T obtain()
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore_1
      //   2: aload_0
      //   3: monitorenter
      //   4: aload_0
      //   5: getfield 28	com/google/android/play/analytics/ProtoCache$ElementCache:mCount	I
      //   8: ifle +54 -> 62
      //   11: aload_0
      //   12: getfield 28	com/google/android/play/analytics/ProtoCache$ElementCache:mCount	I
      //   15: bipush 255
      //   17: iadd
      //   18: istore_2
      //   19: aload_0
      //   20: iload_2
      //   21: putfield 28	com/google/android/play/analytics/ProtoCache$ElementCache:mCount	I
      //   24: aload_0
      //   25: getfield 39	com/google/android/play/analytics/ProtoCache$ElementCache:mCache	[Ljava/lang/Object;
      //   28: astore_3
      //   29: aload_0
      //   30: getfield 28	com/google/android/play/analytics/ProtoCache$ElementCache:mCount	I
      //   33: istore 4
      //   35: aload_3
      //   36: iload 4
      //   38: aaload
      //   39: astore_1
      //   40: aload_0
      //   41: getfield 39	com/google/android/play/analytics/ProtoCache$ElementCache:mCache	[Ljava/lang/Object;
      //   44: astore 5
      //   46: aload_0
      //   47: getfield 28	com/google/android/play/analytics/ProtoCache$ElementCache:mCount	I
      //   50: istore 6
      //   52: aload 5
      //   54: iload 6
      //   56: aconst_null
      //   57: aastore
      //   58: aload_0
      //   59: monitorexit
      //   60: aload_1
      //   61: areturn
      //   62: aload_0
      //   63: monitorexit
      //   64: aload_0
      //   65: getfield 41	com/google/android/play/analytics/ProtoCache$ElementCache:mClazz	Ljava/lang/Class;
      //   68: invokevirtual 49	java/lang/Class:newInstance	()Ljava/lang/Object;
      //   71: astore 7
      //   73: aload 7
      //   75: astore_1
      //   76: goto -16 -> 60
      //   79: astore 8
      //   81: aload_0
      //   82: monitorexit
      //   83: aload 8
      //   85: athrow
      //   86: astore 9
      //   88: invokestatic 53	com/google/android/play/analytics/ProtoCache:access$000	()Ljava/lang/String;
      //   91: ldc 55
      //   93: aload 9
      //   95: invokestatic 61	android/util/Log:wtf	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   98: istore 10
      //   100: goto -40 -> 60
      //
      // Exception table:
      //   from	to	target	type
      //   4	64	79	finally
      //   81	83	79	finally
      //   64	73	86	java/lang/Exception
    }

    public void recycle(T paramT)
    {
      try
      {
        int i = this.mCount;
        int j = this.mLimit;
        if (i < j)
        {
          Object[] arrayOfObject = this.mCache;
          int k = this.mCount;
          arrayOfObject[k] = paramT;
          int m = this.mCount + 1;
          this.mCount = m;
          int n = this.mCount;
          int i1 = this.mHighWater;
          if (n > i1)
          {
            int i2 = this.mCount;
            this.mHighWater = i2;
          }
        }
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.play.analytics.ProtoCache
 * JD-Core Version:    0.6.2
 */