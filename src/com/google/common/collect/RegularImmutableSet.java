package com.google.common.collect;

final class RegularImmutableSet<E> extends ImmutableSet.ArrayImmutableSet<E>
{
  private final transient int hashCode;
  private final transient int mask;
  final transient Object[] table;

  RegularImmutableSet(Object[] paramArrayOfObject1, int paramInt1, Object[] paramArrayOfObject2, int paramInt2)
  {
    super(paramArrayOfObject1);
    this.table = paramArrayOfObject2;
    this.mask = paramInt2;
    this.hashCode = paramInt1;
  }

  public boolean contains(Object paramObject)
  {
    boolean bool = false;
    if (paramObject == null)
      return bool;
    int i = Hashing.smear(paramObject.hashCode());
    while (true)
    {
      Object[] arrayOfObject = this.table;
      int j = this.mask & i;
      Object localObject = arrayOfObject[j];
      if (localObject == null)
        break;
      if (localObject.equals(paramObject))
      {
        bool = true;
        break;
      }
      i += 1;
    }
  }

  public int hashCode()
  {
    return this.hashCode;
  }

  boolean isHashCodeFast()
  {
    return true;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.RegularImmutableSet
 * JD-Core Version:    0.6.2
 */