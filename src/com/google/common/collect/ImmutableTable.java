package com.google.common.collect;

import java.util.Map;
import java.util.Set;

public abstract class ImmutableTable<R, C, V>
  implements Table<R, C, V>
{
  public abstract ImmutableSet<Table.Cell<R, C, V>> cellSet();

  public boolean equals(Object paramObject)
  {
    boolean bool;
    if (paramObject == this)
      bool = true;
    while (true)
    {
      return bool;
      if ((paramObject instanceof Table))
      {
        Table localTable = (Table)paramObject;
        ImmutableSet localImmutableSet = cellSet();
        Set localSet = localTable.cellSet();
        bool = localImmutableSet.equals(localSet);
      }
      else
      {
        bool = false;
      }
    }
  }

  public int hashCode()
  {
    return cellSet().hashCode();
  }

  public abstract ImmutableMap<R, Map<C, V>> rowMap();

  public String toString()
  {
    return rowMap().toString();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.ImmutableTable
 * JD-Core Version:    0.6.2
 */