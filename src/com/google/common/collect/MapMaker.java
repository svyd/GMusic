package com.google.common.collect;

import com.google.common.base.Ascii;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public final class MapMaker extends GenericMapMaker<Object, Object>
{
  int concurrencyLevel = -1;
  long expireAfterAccessNanos = 65535L;
  long expireAfterWriteNanos = 65535L;
  int initialCapacity = -1;
  Equivalence<Object> keyEquivalence;
  MapMakerInternalMap.Strength keyStrength;
  int maximumSize = -1;
  RemovalCause nullRemovalCause;
  Ticker ticker;
  boolean useCustomMap;
  Equivalence<Object> valueEquivalence;
  MapMakerInternalMap.Strength valueStrength;

  private void checkExpiration(long paramLong, TimeUnit paramTimeUnit)
  {
    boolean bool1;
    boolean bool2;
    if (this.expireAfterWriteNanos == 65535L)
    {
      bool1 = true;
      Object[] arrayOfObject1 = new Object[1];
      Long localLong1 = Long.valueOf(this.expireAfterWriteNanos);
      arrayOfObject1[0] = localLong1;
      Preconditions.checkState(bool1, "expireAfterWrite was already set to %s ns", arrayOfObject1);
      if (this.expireAfterAccessNanos != 65535L)
        break label138;
      bool2 = true;
      label58: Object[] arrayOfObject2 = new Object[1];
      Long localLong2 = Long.valueOf(this.expireAfterAccessNanos);
      arrayOfObject2[0] = localLong2;
      Preconditions.checkState(bool2, "expireAfterAccess was already set to %s ns", arrayOfObject2);
      if (paramLong < 0L)
        break label144;
    }
    label138: label144: for (boolean bool3 = true; ; bool3 = false)
    {
      Object[] arrayOfObject3 = new Object[2];
      Long localLong3 = Long.valueOf(paramLong);
      arrayOfObject3[0] = localLong3;
      arrayOfObject3[1] = paramTimeUnit;
      Preconditions.checkArgument(bool3, "duration cannot be negative: %s %s", arrayOfObject3);
      return;
      bool1 = false;
      break;
      bool2 = false;
      break label58;
    }
  }

  private boolean useNullMap()
  {
    if (this.nullRemovalCause == null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public MapMaker concurrencyLevel(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2;
    if (this.concurrencyLevel == -1)
    {
      bool2 = true;
      Object[] arrayOfObject = new Object[1];
      Integer localInteger = Integer.valueOf(this.concurrencyLevel);
      arrayOfObject[0] = localInteger;
      Preconditions.checkState(bool2, "concurrency level was already set to %s", arrayOfObject);
      if (paramInt <= 0)
        break label62;
    }
    while (true)
    {
      Preconditions.checkArgument(bool1);
      this.concurrencyLevel = paramInt;
      return this;
      bool2 = false;
      break;
      label62: bool1 = false;
    }
  }

  @Deprecated
  MapMaker expireAfterAccess(long paramLong, TimeUnit paramTimeUnit)
  {
    checkExpiration(paramLong, paramTimeUnit);
    long l = paramTimeUnit.toNanos(paramLong);
    this.expireAfterAccessNanos = l;
    if ((paramLong == 0L) && (this.nullRemovalCause == null))
    {
      RemovalCause localRemovalCause = RemovalCause.EXPIRED;
      this.nullRemovalCause = localRemovalCause;
    }
    this.useCustomMap = true;
    return this;
  }

  @Deprecated
  MapMaker expireAfterWrite(long paramLong, TimeUnit paramTimeUnit)
  {
    checkExpiration(paramLong, paramTimeUnit);
    long l = paramTimeUnit.toNanos(paramLong);
    this.expireAfterWriteNanos = l;
    if ((paramLong == 0L) && (this.nullRemovalCause == null))
    {
      RemovalCause localRemovalCause = RemovalCause.EXPIRED;
      this.nullRemovalCause = localRemovalCause;
    }
    this.useCustomMap = true;
    return this;
  }

  int getConcurrencyLevel()
  {
    if (this.concurrencyLevel == -1);
    for (int i = 4; ; i = this.concurrencyLevel)
      return i;
  }

  long getExpireAfterAccessNanos()
  {
    if (this.expireAfterAccessNanos == 65535L);
    for (long l = 0L; ; l = this.expireAfterAccessNanos)
      return l;
  }

  long getExpireAfterWriteNanos()
  {
    if (this.expireAfterWriteNanos == 65535L);
    for (long l = 0L; ; l = this.expireAfterWriteNanos)
      return l;
  }

  int getInitialCapacity()
  {
    if (this.initialCapacity == -1);
    for (int i = 16; ; i = this.initialCapacity)
      return i;
  }

  Equivalence<Object> getKeyEquivalence()
  {
    Equivalence localEquivalence1 = this.keyEquivalence;
    Equivalence localEquivalence2 = getKeyStrength().defaultEquivalence();
    return (Equivalence)Objects.firstNonNull(localEquivalence1, localEquivalence2);
  }

  MapMakerInternalMap.Strength getKeyStrength()
  {
    MapMakerInternalMap.Strength localStrength1 = this.keyStrength;
    MapMakerInternalMap.Strength localStrength2 = MapMakerInternalMap.Strength.STRONG;
    return (MapMakerInternalMap.Strength)Objects.firstNonNull(localStrength1, localStrength2);
  }

  Ticker getTicker()
  {
    Ticker localTicker1 = this.ticker;
    Ticker localTicker2 = Ticker.systemTicker();
    return (Ticker)Objects.firstNonNull(localTicker1, localTicker2);
  }

  Equivalence<Object> getValueEquivalence()
  {
    Equivalence localEquivalence1 = this.valueEquivalence;
    Equivalence localEquivalence2 = getValueStrength().defaultEquivalence();
    return (Equivalence)Objects.firstNonNull(localEquivalence1, localEquivalence2);
  }

  MapMakerInternalMap.Strength getValueStrength()
  {
    MapMakerInternalMap.Strength localStrength1 = this.valueStrength;
    MapMakerInternalMap.Strength localStrength2 = MapMakerInternalMap.Strength.STRONG;
    return (MapMakerInternalMap.Strength)Objects.firstNonNull(localStrength1, localStrength2);
  }

  public MapMaker initialCapacity(int paramInt)
  {
    boolean bool1 = true;
    boolean bool2;
    if (this.initialCapacity == -1)
    {
      bool2 = true;
      Object[] arrayOfObject = new Object[1];
      Integer localInteger = Integer.valueOf(this.initialCapacity);
      arrayOfObject[0] = localInteger;
      Preconditions.checkState(bool2, "initial capacity was already set to %s", arrayOfObject);
      if (paramInt < 0)
        break label62;
    }
    while (true)
    {
      Preconditions.checkArgument(bool1);
      this.initialCapacity = paramInt;
      return this;
      bool2 = false;
      break;
      label62: bool1 = false;
    }
  }

  MapMaker keyEquivalence(Equivalence<Object> paramEquivalence)
  {
    if (this.keyEquivalence == null);
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      Equivalence localEquivalence1 = this.keyEquivalence;
      arrayOfObject[0] = localEquivalence1;
      Preconditions.checkState(bool, "key equivalence was already set to %s", arrayOfObject);
      Equivalence localEquivalence2 = (Equivalence)Preconditions.checkNotNull(paramEquivalence);
      this.keyEquivalence = localEquivalence2;
      this.useCustomMap = true;
      return this;
    }
  }

  @Deprecated
  public <K, V> ConcurrentMap<K, V> makeComputingMap(Function<? super K, ? extends V> paramFunction)
  {
    if (useNullMap());
    for (Object localObject = new ComputingConcurrentHashMap.ComputingMapAdapter(this, paramFunction); ; localObject = new NullComputingConcurrentMap(this, paramFunction))
      return localObject;
  }

  public <K, V> ConcurrentMap<K, V> makeMap()
  {
    Object localObject;
    if (!this.useCustomMap)
    {
      int i = getInitialCapacity();
      int j = getConcurrencyLevel();
      localObject = new ConcurrentHashMap(i, 0.75F, j);
    }
    while (true)
    {
      return localObject;
      if (this.nullRemovalCause == null)
        localObject = new MapMakerInternalMap(this);
      else
        localObject = new NullConcurrentMap(this);
    }
  }

  @Deprecated
  MapMaker maximumSize(int paramInt)
  {
    boolean bool1 = false;
    if (this.maximumSize == -1);
    for (boolean bool2 = true; ; bool2 = false)
    {
      Object[] arrayOfObject = new Object[1];
      Integer localInteger = Integer.valueOf(this.maximumSize);
      arrayOfObject[0] = localInteger;
      Preconditions.checkState(bool2, "maximum size was already set to %s", arrayOfObject);
      if (paramInt >= 0)
        bool1 = true;
      Preconditions.checkArgument(bool1, "maximum size must not be negative");
      this.maximumSize = paramInt;
      this.useCustomMap = true;
      if (this.maximumSize == 0)
      {
        RemovalCause localRemovalCause = RemovalCause.SIZE;
        this.nullRemovalCause = localRemovalCause;
      }
      return this;
    }
  }

  @Deprecated
  <K, V> GenericMapMaker<K, V> removalListener(RemovalListener<K, V> paramRemovalListener)
  {
    if (this.removalListener == null);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkState(bool);
      MapMaker localMapMaker = this;
      RemovalListener localRemovalListener = (RemovalListener)Preconditions.checkNotNull(paramRemovalListener);
      localMapMaker.removalListener = localRemovalListener;
      this.useCustomMap = true;
      return localMapMaker;
    }
  }

  MapMaker setKeyStrength(MapMakerInternalMap.Strength paramStrength)
  {
    if (this.keyStrength == null);
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      MapMakerInternalMap.Strength localStrength1 = this.keyStrength;
      arrayOfObject[0] = localStrength1;
      Preconditions.checkState(bool, "Key strength was already set to %s", arrayOfObject);
      MapMakerInternalMap.Strength localStrength2 = (MapMakerInternalMap.Strength)Preconditions.checkNotNull(paramStrength);
      this.keyStrength = localStrength2;
      MapMakerInternalMap.Strength localStrength3 = MapMakerInternalMap.Strength.STRONG;
      if (paramStrength != localStrength3)
        this.useCustomMap = true;
      return this;
    }
  }

  MapMaker setValueStrength(MapMakerInternalMap.Strength paramStrength)
  {
    if (this.valueStrength == null);
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      MapMakerInternalMap.Strength localStrength1 = this.valueStrength;
      arrayOfObject[0] = localStrength1;
      Preconditions.checkState(bool, "Value strength was already set to %s", arrayOfObject);
      MapMakerInternalMap.Strength localStrength2 = (MapMakerInternalMap.Strength)Preconditions.checkNotNull(paramStrength);
      this.valueStrength = localStrength2;
      MapMakerInternalMap.Strength localStrength3 = MapMakerInternalMap.Strength.STRONG;
      if (paramStrength != localStrength3)
        this.useCustomMap = true;
      return this;
    }
  }

  public String toString()
  {
    Objects.ToStringHelper localToStringHelper1 = Objects.toStringHelper(this);
    if (this.initialCapacity != -1)
    {
      int i = this.initialCapacity;
      Objects.ToStringHelper localToStringHelper2 = localToStringHelper1.add("initialCapacity", i);
    }
    if (this.concurrencyLevel != -1)
    {
      int j = this.concurrencyLevel;
      Objects.ToStringHelper localToStringHelper3 = localToStringHelper1.add("concurrencyLevel", j);
    }
    if (this.maximumSize != -1)
    {
      int k = this.maximumSize;
      Objects.ToStringHelper localToStringHelper4 = localToStringHelper1.add("maximumSize", k);
    }
    if (this.expireAfterWriteNanos != 65535L)
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      long l1 = this.expireAfterWriteNanos;
      String str1 = l1 + "ns";
      Objects.ToStringHelper localToStringHelper5 = localToStringHelper1.add("expireAfterWrite", str1);
    }
    if (this.expireAfterAccessNanos != 65535L)
    {
      StringBuilder localStringBuilder2 = new StringBuilder();
      long l2 = this.expireAfterAccessNanos;
      String str2 = l2 + "ns";
      Objects.ToStringHelper localToStringHelper6 = localToStringHelper1.add("expireAfterAccess", str2);
    }
    if (this.keyStrength != null)
    {
      String str3 = Ascii.toLowerCase(this.keyStrength.toString());
      Objects.ToStringHelper localToStringHelper7 = localToStringHelper1.add("keyStrength", str3);
    }
    if (this.valueStrength != null)
    {
      String str4 = Ascii.toLowerCase(this.valueStrength.toString());
      Objects.ToStringHelper localToStringHelper8 = localToStringHelper1.add("valueStrength", str4);
    }
    if (this.keyEquivalence != null)
      Objects.ToStringHelper localToStringHelper9 = localToStringHelper1.addValue("keyEquivalence");
    if (this.valueEquivalence != null)
      Objects.ToStringHelper localToStringHelper10 = localToStringHelper1.addValue("valueEquivalence");
    if (this.removalListener != null)
      Objects.ToStringHelper localToStringHelper11 = localToStringHelper1.addValue("removalListener");
    return localToStringHelper1.toString();
  }

  MapMaker valueEquivalence(Equivalence<Object> paramEquivalence)
  {
    if (this.valueEquivalence == null);
    for (boolean bool = true; ; bool = false)
    {
      Object[] arrayOfObject = new Object[1];
      Equivalence localEquivalence1 = this.valueEquivalence;
      arrayOfObject[0] = localEquivalence1;
      Preconditions.checkState(bool, "value equivalence was already set to %s", arrayOfObject);
      Equivalence localEquivalence2 = (Equivalence)Preconditions.checkNotNull(paramEquivalence);
      this.valueEquivalence = localEquivalence2;
      this.useCustomMap = true;
      return this;
    }
  }

  public MapMaker weakKeys()
  {
    MapMakerInternalMap.Strength localStrength = MapMakerInternalMap.Strength.WEAK;
    return setKeyStrength(localStrength);
  }

  static final class NullComputingConcurrentMap<K, V> extends MapMaker.NullConcurrentMap<K, V>
  {
    private static final long serialVersionUID;
    final Function<? super K, ? extends V> computingFunction;

    NullComputingConcurrentMap(MapMaker paramMapMaker, Function<? super K, ? extends V> paramFunction)
    {
      super();
      Function localFunction = (Function)Preconditions.checkNotNull(paramFunction);
      this.computingFunction = localFunction;
    }

    private V compute(K paramK)
    {
      Object localObject1 = Preconditions.checkNotNull(paramK);
      try
      {
        Object localObject2 = this.computingFunction.apply(paramK);
        return localObject2;
      }
      catch (ComputationException localComputationException)
      {
        throw localComputationException;
      }
      catch (Throwable localThrowable)
      {
        throw new ComputationException(localThrowable);
      }
    }

    public V get(Object paramObject)
    {
      Object localObject1 = paramObject;
      Object localObject2 = compute(localObject1);
      StringBuilder localStringBuilder = new StringBuilder();
      Function localFunction = this.computingFunction;
      String str = localFunction + " returned null for key " + localObject1 + ".";
      Object localObject3 = Preconditions.checkNotNull(localObject2, str);
      notifyRemoval(localObject1, localObject2);
      return localObject2;
    }
  }

  static class NullConcurrentMap<K, V> extends AbstractMap<K, V>
    implements ConcurrentMap<K, V>, Serializable
  {
    private static final long serialVersionUID;
    private final MapMaker.RemovalCause removalCause;
    private final MapMaker.RemovalListener<K, V> removalListener;

    NullConcurrentMap(MapMaker paramMapMaker)
    {
      MapMaker.RemovalListener localRemovalListener = paramMapMaker.getRemovalListener();
      this.removalListener = localRemovalListener;
      MapMaker.RemovalCause localRemovalCause = paramMapMaker.nullRemovalCause;
      this.removalCause = localRemovalCause;
    }

    public boolean containsKey(Object paramObject)
    {
      return false;
    }

    public boolean containsValue(Object paramObject)
    {
      return false;
    }

    public Set<Map.Entry<K, V>> entrySet()
    {
      return Collections.emptySet();
    }

    public V get(Object paramObject)
    {
      return null;
    }

    void notifyRemoval(K paramK, V paramV)
    {
      MapMaker.RemovalCause localRemovalCause = this.removalCause;
      MapMaker.RemovalNotification localRemovalNotification = new MapMaker.RemovalNotification(paramK, paramV, localRemovalCause);
      this.removalListener.onRemoval(localRemovalNotification);
    }

    public V put(K paramK, V paramV)
    {
      Object localObject1 = Preconditions.checkNotNull(paramK);
      Object localObject2 = Preconditions.checkNotNull(paramV);
      notifyRemoval(paramK, paramV);
      return null;
    }

    public V putIfAbsent(K paramK, V paramV)
    {
      return put(paramK, paramV);
    }

    public V remove(Object paramObject)
    {
      return null;
    }

    public boolean remove(Object paramObject1, Object paramObject2)
    {
      return false;
    }

    public V replace(K paramK, V paramV)
    {
      Object localObject1 = Preconditions.checkNotNull(paramK);
      Object localObject2 = Preconditions.checkNotNull(paramV);
      return null;
    }

    public boolean replace(K paramK, V paramV1, V paramV2)
    {
      Object localObject1 = Preconditions.checkNotNull(paramK);
      Object localObject2 = Preconditions.checkNotNull(paramV2);
      return false;
    }
  }

  static abstract enum RemovalCause
  {
    static
    {
      // Byte code:
      //   0: new 7	com/google/common/collect/MapMaker$RemovalCause$1
      //   3: dup
      //   4: ldc 30
      //   6: iconst_0
      //   7: invokespecial 34	com/google/common/collect/MapMaker$RemovalCause$1:<init>	(Ljava/lang/String;I)V
      //   10: putstatic 36	com/google/common/collect/MapMaker$RemovalCause:EXPLICIT	Lcom/google/common/collect/MapMaker$RemovalCause;
      //   13: new 9	com/google/common/collect/MapMaker$RemovalCause$2
      //   16: dup
      //   17: ldc 37
      //   19: iconst_1
      //   20: invokespecial 38	com/google/common/collect/MapMaker$RemovalCause$2:<init>	(Ljava/lang/String;I)V
      //   23: putstatic 40	com/google/common/collect/MapMaker$RemovalCause:REPLACED	Lcom/google/common/collect/MapMaker$RemovalCause;
      //   26: new 11	com/google/common/collect/MapMaker$RemovalCause$3
      //   29: dup
      //   30: ldc 41
      //   32: iconst_2
      //   33: invokespecial 42	com/google/common/collect/MapMaker$RemovalCause$3:<init>	(Ljava/lang/String;I)V
      //   36: putstatic 44	com/google/common/collect/MapMaker$RemovalCause:COLLECTED	Lcom/google/common/collect/MapMaker$RemovalCause;
      //   39: new 13	com/google/common/collect/MapMaker$RemovalCause$4
      //   42: dup
      //   43: ldc 45
      //   45: iconst_3
      //   46: invokespecial 46	com/google/common/collect/MapMaker$RemovalCause$4:<init>	(Ljava/lang/String;I)V
      //   49: putstatic 48	com/google/common/collect/MapMaker$RemovalCause:EXPIRED	Lcom/google/common/collect/MapMaker$RemovalCause;
      //   52: new 15	com/google/common/collect/MapMaker$RemovalCause$5
      //   55: dup
      //   56: ldc 49
      //   58: iconst_4
      //   59: invokespecial 50	com/google/common/collect/MapMaker$RemovalCause$5:<init>	(Ljava/lang/String;I)V
      //   62: putstatic 52	com/google/common/collect/MapMaker$RemovalCause:SIZE	Lcom/google/common/collect/MapMaker$RemovalCause;
      //   65: iconst_5
      //   66: anewarray 2	com/google/common/collect/MapMaker$RemovalCause
      //   69: astore_0
      //   70: getstatic 36	com/google/common/collect/MapMaker$RemovalCause:EXPLICIT	Lcom/google/common/collect/MapMaker$RemovalCause;
      //   73: astore_1
      //   74: aload_0
      //   75: iconst_0
      //   76: aload_1
      //   77: aastore
      //   78: getstatic 40	com/google/common/collect/MapMaker$RemovalCause:REPLACED	Lcom/google/common/collect/MapMaker$RemovalCause;
      //   81: astore_2
      //   82: aload_0
      //   83: iconst_1
      //   84: aload_2
      //   85: aastore
      //   86: getstatic 44	com/google/common/collect/MapMaker$RemovalCause:COLLECTED	Lcom/google/common/collect/MapMaker$RemovalCause;
      //   89: astore_3
      //   90: aload_0
      //   91: iconst_2
      //   92: aload_3
      //   93: aastore
      //   94: getstatic 48	com/google/common/collect/MapMaker$RemovalCause:EXPIRED	Lcom/google/common/collect/MapMaker$RemovalCause;
      //   97: astore 4
      //   99: aload_0
      //   100: iconst_3
      //   101: aload 4
      //   103: aastore
      //   104: getstatic 52	com/google/common/collect/MapMaker$RemovalCause:SIZE	Lcom/google/common/collect/MapMaker$RemovalCause;
      //   107: astore 5
      //   109: aload_0
      //   110: iconst_4
      //   111: aload 5
      //   113: aastore
      //   114: aload_0
      //   115: putstatic 54	com/google/common/collect/MapMaker$RemovalCause:$VALUES	[Lcom/google/common/collect/MapMaker$RemovalCause;
      //   118: return
    }
  }

  static final class RemovalNotification<K, V> extends ImmutableEntry<K, V>
  {
    private static final long serialVersionUID;
    private final MapMaker.RemovalCause cause;

    RemovalNotification(K paramK, V paramV, MapMaker.RemovalCause paramRemovalCause)
    {
      super(paramV);
      this.cause = paramRemovalCause;
    }
  }

  static abstract interface RemovalListener<K, V>
  {
    public abstract void onRemoval(MapMaker.RemovalNotification<K, V> paramRemovalNotification);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMaker
 * JD-Core Version:    0.6.2
 */