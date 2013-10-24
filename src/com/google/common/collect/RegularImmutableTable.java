package com.google.common.collect;

import com.google.common.base.Function;
import java.util.Map;

abstract class RegularImmutableTable<R, C, V> extends ImmutableTable<R, C, V>
{
  private static final Function<Table.Cell<Object, Object, Object>, Object> GET_VALUE_FUNCTION = new Function()
  {
    public Object apply(Table.Cell<Object, Object, Object> paramAnonymousCell)
    {
      return paramAnonymousCell.getValue();
    }
  };
  private final ImmutableSet<Table.Cell<R, C, V>> cellSet;

  public final ImmutableSet<Table.Cell<R, C, V>> cellSet()
  {
    return this.cellSet;
  }

  static final class DenseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V>
  {
    private final ImmutableBiMap<C, Integer> columnKeyToIndex;
    private final ImmutableBiMap<R, Integer> rowKeyToIndex;
    private volatile transient ImmutableMap<R, Map<C, V>> rowMap;
    private final V[][] values;

    private ImmutableMap<R, Map<C, V>> makeRowMap()
    {
      ImmutableMap.Builder localBuilder1 = ImmutableMap.builder();
      int i = 0;
      while (true)
      {
        int j = this.values.length;
        if (i >= j)
          break;
        Object[] arrayOfObject = this.values[i];
        ImmutableMap.Builder localBuilder2 = ImmutableMap.builder();
        int k = 0;
        while (true)
        {
          int m = arrayOfObject.length;
          if (k >= m)
            break;
          Object localObject1 = arrayOfObject[k];
          if (localObject1 != null)
          {
            ImmutableBiMap localImmutableBiMap1 = this.columnKeyToIndex.inverse();
            Integer localInteger1 = Integer.valueOf(k);
            Object localObject2 = localImmutableBiMap1.get(localInteger1);
            ImmutableMap.Builder localBuilder3 = localBuilder2.put(localObject2, localObject1);
          }
          k += 1;
        }
        ImmutableBiMap localImmutableBiMap2 = this.rowKeyToIndex.inverse();
        Integer localInteger2 = Integer.valueOf(i);
        Object localObject3 = localImmutableBiMap2.get(localInteger2);
        ImmutableMap localImmutableMap = localBuilder2.build();
        ImmutableMap.Builder localBuilder4 = localBuilder1.put(localObject3, localImmutableMap);
        i += 1;
      }
      return localBuilder1.build();
    }

    public ImmutableMap<R, Map<C, V>> rowMap()
    {
      ImmutableMap localImmutableMap = this.rowMap;
      if (localImmutableMap == null)
      {
        localImmutableMap = makeRowMap();
        this.rowMap = localImmutableMap;
      }
      return localImmutableMap;
    }
  }

  static final class SparseImmutableTable<R, C, V> extends RegularImmutableTable<R, C, V>
  {
    private final ImmutableMap<R, Map<C, V>> rowMap;

    public ImmutableMap<R, Map<C, V>> rowMap()
    {
      return this.rowMap;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.RegularImmutableTable
 * JD-Core Version:    0.6.2
 */