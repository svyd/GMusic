package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.ReferenceQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

class ComputingConcurrentHashMap<K, V> extends MapMakerInternalMap<K, V>
{
  private static final long serialVersionUID = 4L;
  final Function<? super K, ? extends V> computingFunction;

  ComputingConcurrentHashMap(MapMaker paramMapMaker, Function<? super K, ? extends V> paramFunction)
  {
    super(paramMapMaker);
    Function localFunction = (Function)Preconditions.checkNotNull(paramFunction);
    this.computingFunction = localFunction;
  }

  MapMakerInternalMap.Segment<K, V> createSegment(int paramInt1, int paramInt2)
  {
    return new ComputingSegment(this, paramInt1, paramInt2);
  }

  V getOrCompute(K paramK)
    throws ExecutionException
  {
    Object localObject = Preconditions.checkNotNull(paramK);
    int i = hash(localObject);
    ComputingSegment localComputingSegment = segmentFor(i);
    Function localFunction = this.computingFunction;
    return localComputingSegment.getOrCompute(paramK, i, localFunction);
  }

  ComputingSegment<K, V> segmentFor(int paramInt)
  {
    return (ComputingSegment)super.segmentFor(paramInt);
  }

  Object writeReplace()
  {
    MapMakerInternalMap.Strength localStrength1 = this.keyStrength;
    MapMakerInternalMap.Strength localStrength2 = this.valueStrength;
    Equivalence localEquivalence1 = this.keyEquivalence;
    Equivalence localEquivalence2 = this.valueEquivalence;
    long l1 = this.expireAfterWriteNanos;
    long l2 = this.expireAfterAccessNanos;
    int i = this.maximumSize;
    int j = this.concurrencyLevel;
    MapMaker.RemovalListener localRemovalListener = this.removalListener;
    Function localFunction = this.computingFunction;
    ComputingConcurrentHashMap localComputingConcurrentHashMap = this;
    return new ComputingSerializationProxy(localStrength1, localStrength2, localEquivalence1, localEquivalence2, l1, l2, i, j, localRemovalListener, localComputingConcurrentHashMap, localFunction);
  }

  static final class ComputingSerializationProxy<K, V> extends MapMakerInternalMap.AbstractSerializationProxy<K, V>
  {
    private static final long serialVersionUID = 4L;
    final Function<? super K, ? extends V> computingFunction;

    ComputingSerializationProxy(MapMakerInternalMap.Strength paramStrength1, MapMakerInternalMap.Strength paramStrength2, Equivalence<Object> paramEquivalence1, Equivalence<Object> paramEquivalence2, long paramLong1, long paramLong2, int paramInt1, int paramInt2, MapMaker.RemovalListener<? super K, ? super V> paramRemovalListener, ConcurrentMap<K, V> paramConcurrentMap, Function<? super K, ? extends V> paramFunction)
    {
      super(paramStrength2, paramEquivalence1, paramEquivalence2, paramLong1, paramLong2, paramInt1, paramInt2, paramRemovalListener, paramConcurrentMap);
      this.computingFunction = paramFunction;
    }

    private void readObject(ObjectInputStream paramObjectInputStream)
      throws IOException, ClassNotFoundException
    {
      paramObjectInputStream.defaultReadObject();
      MapMaker localMapMaker = readMapMaker(paramObjectInputStream);
      Function localFunction = this.computingFunction;
      ConcurrentMap localConcurrentMap = localMapMaker.makeComputingMap(localFunction);
      this.delegate = localConcurrentMap;
      readEntries(paramObjectInputStream);
    }

    private void writeObject(ObjectOutputStream paramObjectOutputStream)
      throws IOException
    {
      paramObjectOutputStream.defaultWriteObject();
      writeMapTo(paramObjectOutputStream);
    }

    Object readResolve()
    {
      return this.delegate;
    }
  }

  static final class ComputingMapAdapter<K, V> extends ComputingConcurrentHashMap<K, V>
    implements Serializable
  {
    private static final long serialVersionUID;

    ComputingMapAdapter(MapMaker paramMapMaker, Function<? super K, ? extends V> paramFunction)
    {
      super(paramFunction);
    }

    public V get(Object paramObject)
    {
      Object localObject2;
      try
      {
        Object localObject1 = getOrCompute(paramObject);
        localObject2 = localObject1;
        if (localObject2 == null)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          Function localFunction = this.computingFunction;
          String str = localFunction + " returned null for key " + paramObject + ".";
          throw new NullPointerException(str);
        }
      }
      catch (ExecutionException localExecutionException)
      {
        Throwable localThrowable = localExecutionException.getCause();
        Throwables.propagateIfInstanceOf(localThrowable, ComputationException.class);
        throw new ComputationException(localThrowable);
      }
      return localObject2;
    }
  }

  private static final class ComputingValueReference<K, V>
    implements MapMakerInternalMap.ValueReference<K, V>
  {
    volatile MapMakerInternalMap.ValueReference<K, V> computedReference;
    final Function<? super K, ? extends V> computingFunction;

    public ComputingValueReference(Function<? super K, ? extends V> paramFunction)
    {
      MapMakerInternalMap.ValueReference localValueReference = MapMakerInternalMap.unset();
      this.computedReference = localValueReference;
      this.computingFunction = paramFunction;
    }

    public void clear(MapMakerInternalMap.ValueReference<K, V> paramValueReference)
    {
      setValueReference(paramValueReference);
    }

    V compute(K paramK, int paramInt)
      throws ExecutionException
    {
      try
      {
        Object localObject1 = this.computingFunction.apply(paramK);
        Object localObject2 = localObject1;
        ComputingConcurrentHashMap.ComputedReference localComputedReference = new ComputingConcurrentHashMap.ComputedReference(localObject2);
        setValueReference(localComputedReference);
        return localObject2;
      }
      catch (Throwable localThrowable)
      {
        ComputingConcurrentHashMap.ComputationExceptionReference localComputationExceptionReference = new ComputingConcurrentHashMap.ComputationExceptionReference(localThrowable);
        setValueReference(localComputationExceptionReference);
        throw new ExecutionException(localThrowable);
      }
    }

    public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> paramReferenceQueue, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      return this;
    }

    public V get()
    {
      return null;
    }

    public MapMakerInternalMap.ReferenceEntry<K, V> getEntry()
    {
      return null;
    }

    public boolean isComputingReference()
    {
      return true;
    }

    void setValueReference(MapMakerInternalMap.ValueReference<K, V> paramValueReference)
    {
      try
      {
        MapMakerInternalMap.ValueReference localValueReference1 = this.computedReference;
        MapMakerInternalMap.ValueReference localValueReference2 = MapMakerInternalMap.UNSET;
        if (localValueReference1 == localValueReference2)
        {
          this.computedReference = paramValueReference;
          notifyAll();
        }
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public V waitForValue()
      throws ExecutionException
    {
      MapMakerInternalMap.ValueReference localValueReference1 = this.computedReference;
      MapMakerInternalMap.ValueReference localValueReference2 = MapMakerInternalMap.UNSET;
      int i;
      if (localValueReference1 == localValueReference2)
        i = 0;
      try
      {
        try
        {
          while (true)
          {
            MapMakerInternalMap.ValueReference localValueReference3 = this.computedReference;
            MapMakerInternalMap.ValueReference localValueReference4 = MapMakerInternalMap.UNSET;
            if (localValueReference3 != localValueReference4)
              break;
            try
            {
              wait();
            }
            catch (InterruptedException localInterruptedException)
            {
              i = 1;
            }
          }
          return this.computedReference.waitForValue();
        }
        finally
        {
        }
      }
      finally
      {
        if (i != 0)
          Thread.currentThread().interrupt();
      }
    }
  }

  private static final class ComputedReference<K, V>
    implements MapMakerInternalMap.ValueReference<K, V>
  {
    final V value;

    ComputedReference(V paramV)
    {
      this.value = paramV;
    }

    public void clear(MapMakerInternalMap.ValueReference<K, V> paramValueReference)
    {
    }

    public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> paramReferenceQueue, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      return this;
    }

    public V get()
    {
      return this.value;
    }

    public MapMakerInternalMap.ReferenceEntry<K, V> getEntry()
    {
      return null;
    }

    public boolean isComputingReference()
    {
      return false;
    }

    public V waitForValue()
    {
      return get();
    }
  }

  private static final class ComputationExceptionReference<K, V>
    implements MapMakerInternalMap.ValueReference<K, V>
  {
    final Throwable t;

    ComputationExceptionReference(Throwable paramThrowable)
    {
      this.t = paramThrowable;
    }

    public void clear(MapMakerInternalMap.ValueReference<K, V> paramValueReference)
    {
    }

    public MapMakerInternalMap.ValueReference<K, V> copyFor(ReferenceQueue<V> paramReferenceQueue, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry)
    {
      return this;
    }

    public V get()
    {
      return null;
    }

    public MapMakerInternalMap.ReferenceEntry<K, V> getEntry()
    {
      return null;
    }

    public boolean isComputingReference()
    {
      return false;
    }

    public V waitForValue()
      throws ExecutionException
    {
      Throwable localThrowable = this.t;
      throw new ExecutionException(localThrowable);
    }
  }

  static final class ComputingSegment<K, V> extends MapMakerInternalMap.Segment<K, V>
  {
    ComputingSegment(MapMakerInternalMap<K, V> paramMapMakerInternalMap, int paramInt1, int paramInt2)
    {
      super(paramInt1, paramInt2);
    }

    V compute(K paramK, int paramInt, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry, ComputingConcurrentHashMap.ComputingValueReference<K, V> paramComputingValueReference)
      throws ExecutionException
    {
      Object localObject1 = null;
      long l1 = System.nanoTime();
      long l2 = 0L;
      try
      {
        try
        {
          localObject1 = paramComputingValueReference.compute(paramK, paramInt);
          l2 = System.nanoTime();
          if (localObject1 != null)
          {
            boolean bool1 = null;
            if (put(paramK, paramInt, localObject1, bool1) != null)
            {
              MapMaker.RemovalCause localRemovalCause = MapMaker.RemovalCause.REPLACED;
              enqueueNotification(paramK, paramInt, localObject1, localRemovalCause);
            }
          }
          long l3;
          boolean bool2;
          return localObject1;
        }
        finally
        {
        }
      }
      finally
      {
        if (l2 == 0L)
          long l4 = System.nanoTime();
        if (localObject1 == null)
          boolean bool3 = clearValue(paramK, paramInt, paramComputingValueReference);
      }
    }

    // ERROR //
    V getOrCompute(K paramK, int paramInt, Function<? super K, ? extends V> paramFunction)
      throws ExecutionException
    {
      // Byte code:
      //   0: aload_0
      //   1: aload_1
      //   2: iload_2
      //   3: invokevirtual 54	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:getEntry	(Ljava/lang/Object;I)Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;
      //   6: astore 4
      //   8: aload 4
      //   10: ifnull +29 -> 39
      //   13: aload_0
      //   14: aload 4
      //   16: invokevirtual 58	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:getLiveValue	(Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;)Ljava/lang/Object;
      //   19: astore 5
      //   21: aload 5
      //   23: ifnull +16 -> 39
      //   26: aload_0
      //   27: aload 4
      //   29: invokevirtual 62	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:recordRead	(Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;)V
      //   32: aload_0
      //   33: invokevirtual 66	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:postReadCleanup	()V
      //   36: aload 5
      //   38: areturn
      //   39: aload 4
      //   41: ifnull +18 -> 59
      //   44: aload 4
      //   46: invokeinterface 72 1 0
      //   51: invokeinterface 78 1 0
      //   56: ifne +430 -> 486
      //   59: iconst_1
      //   60: istore 6
      //   62: aconst_null
      //   63: astore 7
      //   65: aload_0
      //   66: invokevirtual 81	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:lock	()V
      //   69: aload_0
      //   70: invokevirtual 84	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:preWriteCleanup	()V
      //   73: aload_0
      //   74: getfield 88	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:count	I
      //   77: bipush 255
      //   79: iadd
      //   80: istore 8
      //   82: aload_0
      //   83: getfield 92	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:table	Ljava/util/concurrent/atomic/AtomicReferenceArray;
      //   86: astore 9
      //   88: aload 9
      //   90: invokevirtual 98	java/util/concurrent/atomic/AtomicReferenceArray:length	()I
      //   93: bipush 255
      //   95: iadd
      //   96: istore 10
      //   98: iload_2
      //   99: iload 10
      //   101: iand
      //   102: istore 11
      //   104: aload 9
      //   106: iload 11
      //   108: invokevirtual 102	java/util/concurrent/atomic/AtomicReferenceArray:get	(I)Ljava/lang/Object;
      //   111: checkcast 68	com/google/common/collect/MapMakerInternalMap$ReferenceEntry
      //   114: astore 12
      //   116: aload 12
      //   118: astore 4
      //   120: aload 4
      //   122: ifnull +70 -> 192
      //   125: aload 4
      //   127: invokeinterface 106 1 0
      //   132: astore 13
      //   134: aload 4
      //   136: invokeinterface 109 1 0
      //   141: istore 14
      //   143: iload_2
      //   144: istore 15
      //   146: iload 14
      //   148: iload 15
      //   150: if_icmpeq +304 -> 454
      //   153: aload 13
      //   155: ifnull +299 -> 454
      //   158: aload_0
      //   159: getfield 113	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:map	Lcom/google/common/collect/MapMakerInternalMap;
      //   162: getfield 119	com/google/common/collect/MapMakerInternalMap:keyEquivalence	Lcom/google/common/base/Equivalence;
      //   165: aload_1
      //   166: aload 13
      //   168: invokevirtual 125	com/google/common/base/Equivalence:equivalent	(Ljava/lang/Object;Ljava/lang/Object;)Z
      //   171: ifeq +283 -> 454
      //   174: aload 4
      //   176: invokeinterface 72 1 0
      //   181: invokeinterface 78 1 0
      //   186: ifeq +106 -> 292
      //   189: iconst_0
      //   190: istore 6
      //   192: iload 6
      //   194: ifeq +58 -> 252
      //   197: aload_3
      //   198: astore 16
      //   200: new 27	com/google/common/collect/ComputingConcurrentHashMap$ComputingValueReference
      //   203: dup
      //   204: aload 16
      //   206: invokespecial 128	com/google/common/collect/ComputingConcurrentHashMap$ComputingValueReference:<init>	(Lcom/google/common/base/Function;)V
      //   209: astore 17
      //   211: aload 4
      //   213: ifnonnull +257 -> 470
      //   216: iload_2
      //   217: istore 18
      //   219: aload_0
      //   220: aload_1
      //   221: iload 18
      //   223: aload 12
      //   225: invokevirtual 132	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:newEntry	(Ljava/lang/Object;ILcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;)Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;
      //   228: astore 4
      //   230: aload 4
      //   232: aload 17
      //   234: invokeinterface 136 2 0
      //   239: aload 9
      //   241: iload 11
      //   243: aload 4
      //   245: invokevirtual 140	java/util/concurrent/atomic/AtomicReferenceArray:set	(ILjava/lang/Object;)V
      //   248: aload 17
      //   250: astore 7
      //   252: aload_0
      //   253: invokevirtual 143	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:unlock	()V
      //   256: aload_0
      //   257: invokevirtual 146	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:postWriteCleanup	()V
      //   260: iload 6
      //   262: ifeq +224 -> 486
      //   265: iload_2
      //   266: istore 19
      //   268: aload_0
      //   269: aload_1
      //   270: iload 19
      //   272: aload 4
      //   274: aload 7
      //   276: invokevirtual 148	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:compute	(Ljava/lang/Object;ILcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;Lcom/google/common/collect/ComputingConcurrentHashMap$ComputingValueReference;)Ljava/lang/Object;
      //   279: astore 20
      //   281: aload 20
      //   283: astore 5
      //   285: aload_0
      //   286: invokevirtual 66	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:postReadCleanup	()V
      //   289: goto -253 -> 36
      //   292: aload 4
      //   294: invokeinterface 72 1 0
      //   299: invokeinterface 150 1 0
      //   304: astore 5
      //   306: aload 5
      //   308: ifnonnull +80 -> 388
      //   311: getstatic 153	com/google/common/collect/MapMaker$RemovalCause:COLLECTED	Lcom/google/common/collect/MapMaker$RemovalCause;
      //   314: astore 21
      //   316: iload_2
      //   317: istore 22
      //   319: aload_0
      //   320: aload 13
      //   322: iload 22
      //   324: aload 5
      //   326: aload 21
      //   328: invokevirtual 44	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:enqueueNotification	(Ljava/lang/Object;ILjava/lang/Object;Lcom/google/common/collect/MapMaker$RemovalCause;)V
      //   331: aload_0
      //   332: getfield 157	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:evictionQueue	Ljava/util/Queue;
      //   335: aload 4
      //   337: invokeinterface 163 2 0
      //   342: istore 23
      //   344: aload_0
      //   345: getfield 166	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:expirationQueue	Ljava/util/Queue;
      //   348: aload 4
      //   350: invokeinterface 163 2 0
      //   355: istore 24
      //   357: aload_0
      //   358: iload 8
      //   360: putfield 88	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:count	I
      //   363: goto -171 -> 192
      //   366: astore 25
      //   368: aload_0
      //   369: invokevirtual 143	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:unlock	()V
      //   372: aload_0
      //   373: invokevirtual 146	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:postWriteCleanup	()V
      //   376: aload 25
      //   378: athrow
      //   379: astore 26
      //   381: aload_0
      //   382: invokevirtual 66	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:postReadCleanup	()V
      //   385: aload 26
      //   387: athrow
      //   388: aload_0
      //   389: getfield 113	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:map	Lcom/google/common/collect/MapMakerInternalMap;
      //   392: invokevirtual 169	com/google/common/collect/MapMakerInternalMap:expires	()Z
      //   395: ifeq +38 -> 433
      //   398: aload_0
      //   399: getfield 113	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:map	Lcom/google/common/collect/MapMakerInternalMap;
      //   402: aload 4
      //   404: invokevirtual 173	com/google/common/collect/MapMakerInternalMap:isExpired	(Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;)Z
      //   407: ifeq +26 -> 433
      //   410: getstatic 176	com/google/common/collect/MapMaker$RemovalCause:EXPIRED	Lcom/google/common/collect/MapMaker$RemovalCause;
      //   413: astore 27
      //   415: iload_2
      //   416: istore 28
      //   418: aload_0
      //   419: aload 13
      //   421: iload 28
      //   423: aload 5
      //   425: aload 27
      //   427: invokevirtual 44	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:enqueueNotification	(Ljava/lang/Object;ILjava/lang/Object;Lcom/google/common/collect/MapMaker$RemovalCause;)V
      //   430: goto -99 -> 331
      //   433: aload_0
      //   434: aload 4
      //   436: invokevirtual 179	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:recordLockedRead	(Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;)V
      //   439: aload_0
      //   440: invokevirtual 143	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:unlock	()V
      //   443: aload_0
      //   444: invokevirtual 146	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:postWriteCleanup	()V
      //   447: aload_0
      //   448: invokevirtual 66	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:postReadCleanup	()V
      //   451: goto -415 -> 36
      //   454: aload 4
      //   456: invokeinterface 183 1 0
      //   461: astore 20
      //   463: aload 20
      //   465: astore 4
      //   467: goto -347 -> 120
      //   470: aload 4
      //   472: aload 17
      //   474: invokeinterface 136 2 0
      //   479: aload 17
      //   481: astore 7
      //   483: goto -231 -> 252
      //   486: aload 4
      //   488: invokestatic 188	java/lang/Thread:holdsLock	(Ljava/lang/Object;)Z
      //   491: ifne +45 -> 536
      //   494: aconst_null
      //   495: astore 25
      //   497: aload 25
      //   499: ldc 190
      //   501: invokestatic 196	com/google/common/base/Preconditions:checkState	(ZLjava/lang/Object;)V
      //   504: aload 4
      //   506: invokeinterface 72 1 0
      //   511: invokeinterface 199 1 0
      //   516: astore 5
      //   518: aload 5
      //   520: ifnull -520 -> 0
      //   523: aload_0
      //   524: aload 4
      //   526: invokevirtual 62	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:recordRead	(Lcom/google/common/collect/MapMakerInternalMap$ReferenceEntry;)V
      //   529: aload_0
      //   530: invokevirtual 66	com/google/common/collect/ComputingConcurrentHashMap$ComputingSegment:postReadCleanup	()V
      //   533: goto -497 -> 36
      //   536: iconst_0
      //   537: istore 25
      //   539: goto -42 -> 497
      //   542: astore 25
      //   544: aload 17
      //   546: astore 29
      //   548: goto -180 -> 368
      //
      // Exception table:
      //   from	to	target	type
      //   69	211	366	finally
      //   292	363	366	finally
      //   388	439	366	finally
      //   454	463	366	finally
      //   0	32	379	finally
      //   44	69	379	finally
      //   252	281	379	finally
      //   368	379	379	finally
      //   439	447	379	finally
      //   486	529	379	finally
      //   216	248	542	finally
      //   470	479	542	finally
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ComputingConcurrentHashMap
 * JD-Core Version:    0.6.2
 */