package com.google.common.collect;

import com.google.common.base.Equivalence;
import com.google.common.base.Equivalences;
import java.lang.ref.ReferenceQueue;

 enum MapMakerInternalMap$Strength
{
  static
  {
    // Byte code:
    //   0: new 7	com/google/common/collect/MapMakerInternalMap$Strength$1
    //   3: dup
    //   4: ldc 24
    //   6: iconst_0
    //   7: invokespecial 28	com/google/common/collect/MapMakerInternalMap$Strength$1:<init>	(Ljava/lang/String;I)V
    //   10: putstatic 30	com/google/common/collect/MapMakerInternalMap$Strength:STRONG	Lcom/google/common/collect/MapMakerInternalMap$Strength;
    //   13: new 9	com/google/common/collect/MapMakerInternalMap$Strength$2
    //   16: dup
    //   17: ldc 31
    //   19: iconst_1
    //   20: invokespecial 32	com/google/common/collect/MapMakerInternalMap$Strength$2:<init>	(Ljava/lang/String;I)V
    //   23: putstatic 34	com/google/common/collect/MapMakerInternalMap$Strength:SOFT	Lcom/google/common/collect/MapMakerInternalMap$Strength;
    //   26: new 11	com/google/common/collect/MapMakerInternalMap$Strength$3
    //   29: dup
    //   30: ldc 35
    //   32: iconst_2
    //   33: invokespecial 36	com/google/common/collect/MapMakerInternalMap$Strength$3:<init>	(Ljava/lang/String;I)V
    //   36: putstatic 38	com/google/common/collect/MapMakerInternalMap$Strength:WEAK	Lcom/google/common/collect/MapMakerInternalMap$Strength;
    //   39: iconst_3
    //   40: anewarray 2	com/google/common/collect/MapMakerInternalMap$Strength
    //   43: astore_0
    //   44: getstatic 30	com/google/common/collect/MapMakerInternalMap$Strength:STRONG	Lcom/google/common/collect/MapMakerInternalMap$Strength;
    //   47: astore_1
    //   48: aload_0
    //   49: iconst_0
    //   50: aload_1
    //   51: aastore
    //   52: getstatic 34	com/google/common/collect/MapMakerInternalMap$Strength:SOFT	Lcom/google/common/collect/MapMakerInternalMap$Strength;
    //   55: astore_2
    //   56: aload_0
    //   57: iconst_1
    //   58: aload_2
    //   59: aastore
    //   60: getstatic 38	com/google/common/collect/MapMakerInternalMap$Strength:WEAK	Lcom/google/common/collect/MapMakerInternalMap$Strength;
    //   63: astore_3
    //   64: aload_0
    //   65: iconst_2
    //   66: aload_3
    //   67: aastore
    //   68: aload_0
    //   69: putstatic 40	com/google/common/collect/MapMakerInternalMap$Strength:$VALUES	[Lcom/google/common/collect/MapMakerInternalMap$Strength;
    //   72: return
  }

  abstract Equivalence<Object> defaultEquivalence();

  abstract <K, V> MapMakerInternalMap.ValueReference<K, V> referenceValue(MapMakerInternalMap.Segment<K, V> paramSegment, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry, V paramV);
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.Strength
 * JD-Core Version:    0.6.2
 */