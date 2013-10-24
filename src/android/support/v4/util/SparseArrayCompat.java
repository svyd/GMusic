package android.support.v4.util;

public class SparseArrayCompat<E>
{
  private static final Object DELETED = new Object();
  private boolean mGarbage = false;
  private int[] mKeys;
  private int mSize;
  private Object[] mValues;

  public SparseArrayCompat()
  {
    this(10);
  }

  public SparseArrayCompat(int paramInt)
  {
    int i = idealIntArraySize(paramInt);
    int[] arrayOfInt = new int[i];
    this.mKeys = arrayOfInt;
    Object[] arrayOfObject = new Object[i];
    this.mValues = arrayOfObject;
    this.mSize = 0;
  }

  private static int binarySearch(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt1 + paramInt2;
    int j = paramInt1 + -1;
    while (i - j > 1)
    {
      int k = (i + j) / 2;
      if (paramArrayOfInt[k] < paramInt3)
        j = k;
      else
        int m = k;
    }
    int n = paramInt1 + paramInt2;
    if (i != n)
      i = paramInt1 + paramInt2 ^ 0xFFFFFFFF;
    while (true)
    {
      return i;
      if (paramArrayOfInt[i] != paramInt3)
        i ^= -1;
    }
  }

  private void gc()
  {
    int i = this.mSize;
    int j = 0;
    int[] arrayOfInt = this.mKeys;
    Object[] arrayOfObject = this.mValues;
    int k = 0;
    while (k < i)
    {
      Object localObject1 = arrayOfObject[k];
      Object localObject2 = DELETED;
      if (localObject1 != localObject2)
      {
        if (k != j)
        {
          int m = arrayOfInt[k];
          arrayOfInt[j] = m;
          arrayOfObject[j] = localObject1;
        }
        j += 1;
      }
      k += 1;
    }
    this.mGarbage = false;
    this.mSize = j;
  }

  static int idealByteArraySize(int paramInt)
  {
    int i = 4;
    while (true)
    {
      if (i < 32)
      {
        int j = (1 << i) + -12;
        if (paramInt <= j)
          paramInt = (1 << i) + -12;
      }
      else
      {
        return paramInt;
      }
      i += 1;
    }
  }

  static int idealIntArraySize(int paramInt)
  {
    return idealByteArraySize(paramInt * 4) / 4;
  }

  public void clear()
  {
    int i = this.mSize;
    Object[] arrayOfObject = this.mValues;
    int j = 0;
    while (j < i)
    {
      arrayOfObject[j] = null;
      j += 1;
    }
    this.mSize = 0;
    this.mGarbage = false;
  }

  public void delete(int paramInt)
  {
    int[] arrayOfInt = this.mKeys;
    int i = this.mSize;
    int j = binarySearch(arrayOfInt, 0, i, paramInt);
    if (j < 0)
      return;
    Object localObject1 = this.mValues[j];
    Object localObject2 = DELETED;
    if (localObject1 == localObject2)
      return;
    Object[] arrayOfObject = this.mValues;
    Object localObject3 = DELETED;
    arrayOfObject[j] = localObject3;
    this.mGarbage = true;
  }

  public E get(int paramInt)
  {
    return get(paramInt, null);
  }

  public E get(int paramInt, E paramE)
  {
    int[] arrayOfInt = this.mKeys;
    int i = this.mSize;
    int j = binarySearch(arrayOfInt, 0, i, paramInt);
    if (j >= 0)
    {
      Object localObject1 = this.mValues[j];
      Object localObject2 = DELETED;
      if (localObject1 != localObject2)
        break label49;
    }
    while (true)
    {
      return paramE;
      label49: paramE = this.mValues[j];
    }
  }

  public int indexOfKey(int paramInt)
  {
    if (this.mGarbage)
      gc();
    int[] arrayOfInt = this.mKeys;
    int i = this.mSize;
    return binarySearch(arrayOfInt, 0, i, paramInt);
  }

  public int keyAt(int paramInt)
  {
    if (this.mGarbage)
      gc();
    return this.mKeys[paramInt];
  }

  public void put(int paramInt, E paramE)
  {
    int[] arrayOfInt1 = this.mKeys;
    int i = this.mSize;
    int j = binarySearch(arrayOfInt1, 0, i, paramInt);
    if (j >= 0)
    {
      this.mValues[j] = paramE;
      return;
    }
    j ^= -1;
    int k = this.mSize;
    if (j < k)
    {
      Object localObject1 = this.mValues[j];
      Object localObject2 = DELETED;
      if (localObject1 == localObject2)
      {
        this.mKeys[j] = paramInt;
        this.mValues[j] = paramE;
        return;
      }
    }
    if (this.mGarbage)
    {
      int m = this.mSize;
      int n = this.mKeys.length;
      if (m >= n)
      {
        gc();
        int[] arrayOfInt2 = this.mKeys;
        int i1 = this.mSize;
        j = binarySearch(arrayOfInt2, 0, i1, paramInt) ^ 0xFFFFFFFF;
      }
    }
    int i2 = this.mSize;
    int i3 = this.mKeys.length;
    if (i2 >= i3)
    {
      int i4 = idealIntArraySize(this.mSize + 1);
      int[] arrayOfInt3 = new int[i4];
      Object[] arrayOfObject1 = new Object[i4];
      int[] arrayOfInt4 = this.mKeys;
      int i5 = this.mKeys.length;
      System.arraycopy(arrayOfInt4, 0, arrayOfInt3, 0, i5);
      Object[] arrayOfObject2 = this.mValues;
      int i6 = this.mValues.length;
      System.arraycopy(arrayOfObject2, 0, arrayOfObject1, 0, i6);
      this.mKeys = arrayOfInt3;
      this.mValues = arrayOfObject1;
    }
    if (this.mSize - j != 0)
    {
      int[] arrayOfInt5 = this.mKeys;
      int[] arrayOfInt6 = this.mKeys;
      int i7 = j + 1;
      int i8 = this.mSize - j;
      System.arraycopy(arrayOfInt5, j, arrayOfInt6, i7, i8);
      Object[] arrayOfObject3 = this.mValues;
      Object[] arrayOfObject4 = this.mValues;
      int i9 = j + 1;
      int i10 = this.mSize - j;
      System.arraycopy(arrayOfObject3, j, arrayOfObject4, i9, i10);
    }
    this.mKeys[j] = paramInt;
    this.mValues[j] = paramE;
    int i11 = this.mSize + 1;
    this.mSize = i11;
  }

  public void remove(int paramInt)
  {
    delete(paramInt);
  }

  public void removeAt(int paramInt)
  {
    Object localObject1 = this.mValues[paramInt];
    Object localObject2 = DELETED;
    if (localObject1 == localObject2)
      return;
    Object[] arrayOfObject = this.mValues;
    Object localObject3 = DELETED;
    arrayOfObject[paramInt] = localObject3;
    this.mGarbage = true;
  }

  public int size()
  {
    if (this.mGarbage)
      gc();
    return this.mSize;
  }

  public E valueAt(int paramInt)
  {
    if (this.mGarbage)
      gc();
    return this.mValues[paramInt];
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.util.SparseArrayCompat
 * JD-Core Version:    0.6.2
 */