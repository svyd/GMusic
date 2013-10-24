package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.Serializable;

final class NaturalOrdering extends Ordering<Comparable>
  implements Serializable
{
  static final NaturalOrdering INSTANCE = new NaturalOrdering();
  private static final long serialVersionUID;

  private Object readResolve()
  {
    return INSTANCE;
  }

  public int compare(Comparable paramComparable1, Comparable paramComparable2)
  {
    Object localObject1 = Preconditions.checkNotNull(paramComparable1);
    Object localObject2 = Preconditions.checkNotNull(paramComparable2);
    if (paramComparable1 == paramComparable2);
    for (int i = 0; ; i = paramComparable1.compareTo(paramComparable2))
      return i;
  }

  public String toString()
  {
    return "Ordering.natural()";
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.NaturalOrdering
 * JD-Core Version:    0.6.2
 */