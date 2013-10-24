package org.codehaus.jackson.sym;

import org.codehaus.jackson.util.InternCache;

public final class BytesToNameCanonicalizer
{
  private int _collCount;
  private int _collEnd;
  private Bucket[] _collList;
  private boolean _collListShared;
  private int _count;
  final boolean _intern;
  private int[] _mainHash;
  private int _mainHashMask;
  private boolean _mainHashShared;
  private Name[] _mainNames;
  private boolean _mainNamesShared;
  private transient boolean _needRehash;
  final BytesToNameCanonicalizer _parent;

  private BytesToNameCanonicalizer(int paramInt, boolean paramBoolean)
  {
    this._parent = null;
    this._intern = paramBoolean;
    if (paramInt < 16)
      paramInt = 16;
    while (true)
    {
      initTables(paramInt);
      return;
      if ((paramInt + -1 & paramInt) != 0)
      {
        int i = 16;
        while (i < paramInt)
          i += i;
        paramInt = i;
      }
    }
  }

  private BytesToNameCanonicalizer(BytesToNameCanonicalizer paramBytesToNameCanonicalizer, boolean paramBoolean)
  {
    this._parent = paramBytesToNameCanonicalizer;
    this._intern = paramBoolean;
    int i = paramBytesToNameCanonicalizer._count;
    this._count = i;
    int j = paramBytesToNameCanonicalizer._mainHashMask;
    this._mainHashMask = j;
    int[] arrayOfInt = paramBytesToNameCanonicalizer._mainHash;
    this._mainHash = arrayOfInt;
    Name[] arrayOfName = paramBytesToNameCanonicalizer._mainNames;
    this._mainNames = arrayOfName;
    Bucket[] arrayOfBucket = paramBytesToNameCanonicalizer._collList;
    this._collList = arrayOfBucket;
    int k = paramBytesToNameCanonicalizer._collCount;
    this._collCount = k;
    int m = paramBytesToNameCanonicalizer._collEnd;
    this._collEnd = m;
    this._needRehash = false;
    this._mainHashShared = true;
    this._mainNamesShared = true;
    this._collListShared = true;
  }

  private void _addSymbol(int paramInt, Name paramName)
  {
    if (this._mainHashShared)
      unshareMain();
    if (this._needRehash)
      rehash();
    int i = this._count + 1;
    this._count = i;
    int j = this._mainHashMask;
    int k = paramInt & j;
    int i3;
    if (this._mainNames[k] == null)
    {
      int[] arrayOfInt1 = this._mainHash;
      int m = paramInt << 8;
      arrayOfInt1[k] = m;
      if (this._mainNamesShared)
        unshareNames();
      this._mainNames[k] = paramName;
      int n = this._mainHash.length;
      int i1 = this._count;
      int i2 = n >> 1;
      if (i1 <= i2)
        return;
      i3 = n >> 2;
      int i4 = this._count;
      int i5 = n - i3;
      if (i4 > i5)
        this._needRehash = true;
    }
    else
    {
      if (this._collListShared)
        unshareCollision();
      int i6 = this._collCount + 1;
      this._collCount = i6;
      int i7 = this._mainHash[k];
      int i8 = i7 & 0xFF;
      int i9;
      if (i8 == 0)
        if (this._collEnd <= 254)
        {
          i9 = this._collEnd;
          int i10 = this._collEnd + 1;
          this._collEnd = i10;
          int i11 = this._collList.length;
          if (i9 >= i11)
            expandCollision();
          label248: int[] arrayOfInt2 = this._mainHash;
          int i12 = i7 & 0xFFFFFF00;
          int i13 = i9 + 1;
          int i14 = i12 | i13;
          arrayOfInt2[k] = i14;
        }
      while (true)
      {
        Bucket[] arrayOfBucket = this._collList;
        Bucket localBucket1 = this._collList[i9];
        Bucket localBucket2 = new Bucket(paramName, localBucket1);
        arrayOfBucket[i9] = localBucket2;
        break;
        i9 = findBestBucket();
        break label248;
        i9 = i8 + -1;
      }
    }
    if (this._collCount < i3)
      return;
    this._needRehash = true;
  }

  public static final int calcHash(int paramInt)
  {
    int i = paramInt;
    int j = i >>> 16;
    int k = i ^ j;
    int m = k >>> 8;
    return k ^ m;
  }

  public static final int calcHash(int paramInt1, int paramInt2)
  {
    int i = paramInt1 * 31 + paramInt2;
    int j = i >>> 16;
    int k = i ^ j;
    int m = k >>> 8;
    return k ^ m;
  }

  public static final int calcHash(int[] paramArrayOfInt, int paramInt)
  {
    int i = paramArrayOfInt[0];
    int j = 1;
    while (j < paramInt)
    {
      int k = i * 31;
      int m = paramArrayOfInt[j];
      i = k + m;
      j += 1;
    }
    int n = i >>> 16;
    int i1 = i ^ n;
    int i2 = i1 >>> 8;
    return i1 ^ i2;
  }

  private static Name constructName(int paramInt1, String paramString, int[] paramArrayOfInt, int paramInt2)
  {
    if (paramInt2 < 4);
    int[] arrayOfInt;
    int i;
    Object localObject;
    switch (paramInt2)
    {
    default:
      arrayOfInt = new int[paramInt2];
      i = 0;
    case 1:
      while (i < paramInt2)
      {
        int j = paramArrayOfInt[i];
        arrayOfInt[i] = j;
        i += 1;
        continue;
        int k = paramArrayOfInt[0];
        localObject = new Name1(paramString, paramInt1, k);
      }
    case 2:
    case 3:
    }
    while (true)
    {
      return localObject;
      int m = paramArrayOfInt[0];
      int n = paramArrayOfInt[1];
      localObject = new Name2(paramString, paramInt1, m, n);
      continue;
      int i1 = paramArrayOfInt[0];
      int i2 = paramArrayOfInt[1];
      int i3 = paramArrayOfInt[2];
      String str = paramString;
      int i4 = paramInt1;
      localObject = new Name3(str, i4, i1, i2, i3);
      continue;
      localObject = new NameN(paramString, paramInt1, arrayOfInt, paramInt2);
    }
  }

  public static BytesToNameCanonicalizer createRoot()
  {
    return new BytesToNameCanonicalizer(64, true);
  }

  private void expandCollision()
  {
    Bucket[] arrayOfBucket1 = this._collList;
    int i = arrayOfBucket1.length;
    Bucket[] arrayOfBucket2 = new Bucket[i + i];
    this._collList = arrayOfBucket2;
    Bucket[] arrayOfBucket3 = this._collList;
    System.arraycopy(arrayOfBucket1, 0, arrayOfBucket3, 0, i);
  }

  private int findBestBucket()
  {
    Bucket[] arrayOfBucket = this._collList;
    int i = 2147483647;
    int j = -1;
    int k = 0;
    int m = this._collEnd;
    int n;
    if (k < m)
    {
      n = arrayOfBucket[k].length();
      if (n < i)
        if (n != 1);
    }
    while (true)
    {
      return k;
      i = n;
      j = k;
      k += 1;
      break;
      k = j;
    }
  }

  public static Name getEmptyName()
  {
    return Name1.getEmptyName();
  }

  private void initTables(int paramInt)
  {
    this._count = 0;
    int[] arrayOfInt = new int[paramInt];
    this._mainHash = arrayOfInt;
    Name[] arrayOfName = new Name[paramInt];
    this._mainNames = arrayOfName;
    this._mainHashShared = false;
    this._mainNamesShared = false;
    int i = paramInt + -1;
    this._mainHashMask = i;
    this._collListShared = true;
    this._collList = null;
    this._collEnd = 0;
    this._needRehash = false;
  }

  private void markAsShared()
  {
    this._mainHashShared = true;
    this._mainNamesShared = true;
    this._collListShared = true;
  }

  /** @deprecated */
  private void mergeChild(BytesToNameCanonicalizer paramBytesToNameCanonicalizer)
  {
    while (true)
    {
      try
      {
        int i = paramBytesToNameCanonicalizer._count;
        int j = this._count;
        if (i <= j)
          return;
        if (paramBytesToNameCanonicalizer.size() > 6000)
        {
          initTables(64);
          continue;
        }
      }
      finally
      {
      }
      int k = paramBytesToNameCanonicalizer._count;
      this._count = k;
      int[] arrayOfInt = paramBytesToNameCanonicalizer._mainHash;
      this._mainHash = arrayOfInt;
      Name[] arrayOfName = paramBytesToNameCanonicalizer._mainNames;
      this._mainNames = arrayOfName;
      this._mainHashShared = true;
      this._mainNamesShared = true;
      int m = paramBytesToNameCanonicalizer._mainHashMask;
      this._mainHashMask = m;
      Bucket[] arrayOfBucket = paramBytesToNameCanonicalizer._collList;
      this._collList = arrayOfBucket;
      int n = paramBytesToNameCanonicalizer._collCount;
      this._collCount = n;
      int i1 = paramBytesToNameCanonicalizer._collEnd;
      this._collEnd = i1;
    }
  }

  private void rehash()
  {
    this._needRehash = false;
    this._mainNamesShared = false;
    int i = 0;
    int j = this._mainHash.length;
    int[] arrayOfInt1 = new int[j + j];
    this._mainHash = arrayOfInt1;
    int k = j + j + -1;
    this._mainHashMask = k;
    Name[] arrayOfName1 = this._mainNames;
    Name[] arrayOfName2 = new Name[j + j];
    this._mainNames = arrayOfName2;
    int m = 0;
    while (m < j)
    {
      Name localName1 = arrayOfName1[m];
      if (localName1 != null)
      {
        i += 1;
        int n = localName1.hashCode();
        int i1 = this._mainHashMask;
        int i2 = n & i1;
        this._mainNames[i2] = localName1;
        int[] arrayOfInt2 = this._mainHash;
        int i3 = n << 8;
        arrayOfInt2[i2] = i3;
      }
      m += 1;
    }
    int i4 = this._collEnd;
    if (i4 == 0)
      return;
    this._collCount = 0;
    this._collEnd = 0;
    this._collListShared = false;
    Bucket[] arrayOfBucket1 = this._collList;
    Bucket[] arrayOfBucket2 = new Bucket[arrayOfBucket1.length];
    this._collList = arrayOfBucket2;
    m = 0;
    while (m < i4)
    {
      Bucket localBucket1 = arrayOfBucket1[m];
      while (localBucket1 != null)
      {
        i += 1;
        Name localName2 = localBucket1.mName;
        int i5 = localName2.hashCode();
        int i6 = this._mainHashMask;
        int i7 = i5 & i6;
        int i8 = this._mainHash[i7];
        if (this._mainNames[i7] == null)
        {
          int[] arrayOfInt3 = this._mainHash;
          int i9 = i5 << 8;
          arrayOfInt3[i7] = i9;
          this._mainNames[i7] = localName2;
          localBucket1 = localBucket1.mNext;
        }
        else
        {
          int i10 = this._collCount + 1;
          this._collCount = i10;
          int i11 = i8 & 0xFF;
          int i12;
          if (i11 == 0)
            if (this._collEnd <= 254)
            {
              i12 = this._collEnd;
              int i13 = this._collEnd + 1;
              this._collEnd = i13;
              int i14 = this._collList.length;
              if (i12 >= i14)
                expandCollision();
              label379: int[] arrayOfInt4 = this._mainHash;
              int i15 = i8 & 0xFFFFFF00;
              int i16 = i12 + 1;
              int i17 = i15 | i16;
              arrayOfInt4[i7] = i17;
            }
          while (true)
          {
            Bucket[] arrayOfBucket3 = this._collList;
            Bucket localBucket2 = this._collList[i12];
            Bucket localBucket3 = new Bucket(localName2, localBucket2);
            arrayOfBucket3[i12] = localBucket3;
            break;
            i12 = findBestBucket();
            break label379;
            i12 = i11 + -1;
          }
        }
      }
      int i18 = m + 1;
    }
    int i19 = this._count;
    if (i != i19)
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("Internal error: count after rehash ").append(i).append("; should be ");
    int i20 = this._count;
    String str = i20;
    throw new RuntimeException(str);
  }

  private void unshareCollision()
  {
    Bucket[] arrayOfBucket1 = this._collList;
    if (arrayOfBucket1 == null)
    {
      Bucket[] arrayOfBucket2 = new Bucket[32];
      this._collList = arrayOfBucket2;
    }
    while (true)
    {
      this._collListShared = false;
      return;
      int i = arrayOfBucket1.length;
      Bucket[] arrayOfBucket3 = new Bucket[i];
      this._collList = arrayOfBucket3;
      Bucket[] arrayOfBucket4 = this._collList;
      System.arraycopy(arrayOfBucket1, 0, arrayOfBucket4, 0, i);
    }
  }

  private void unshareMain()
  {
    int[] arrayOfInt1 = this._mainHash;
    int i = this._mainHash.length;
    int[] arrayOfInt2 = new int[i];
    this._mainHash = arrayOfInt2;
    int[] arrayOfInt3 = this._mainHash;
    System.arraycopy(arrayOfInt1, 0, arrayOfInt3, 0, i);
    this._mainHashShared = false;
  }

  private void unshareNames()
  {
    Name[] arrayOfName1 = this._mainNames;
    int i = arrayOfName1.length;
    Name[] arrayOfName2 = new Name[i];
    this._mainNames = arrayOfName2;
    Name[] arrayOfName3 = this._mainNames;
    System.arraycopy(arrayOfName1, 0, arrayOfName3, 0, i);
    this._mainNamesShared = false;
  }

  public Name addName(String paramString, int[] paramArrayOfInt, int paramInt)
  {
    if (this._intern)
      paramString = InternCache.instance.intern(paramString);
    int i = calcHash(paramArrayOfInt, paramInt);
    Name localName = constructName(i, paramString, paramArrayOfInt, paramInt);
    _addSymbol(i, localName);
    return localName;
  }

  public Name findName(int paramInt)
  {
    int i = calcHash(paramInt);
    int j = this._mainHashMask;
    int k = i & j;
    int m = this._mainHash[k];
    Name localName;
    if ((m >> 8 ^ i) << 8 == 0)
    {
      localName = this._mainNames[k];
      if (localName == null)
        localName = null;
    }
    while (true)
    {
      return localName;
      if (!localName.equals(paramInt))
      {
        do
        {
          m &= 255;
          if (m <= 0)
            break label124;
          int n = m + -1;
          Bucket localBucket = this._collList[n];
          if (localBucket == null)
            break label124;
          localName = localBucket.find(i, paramInt, 0);
          break;
        }
        while (m != 0);
        localName = null;
        continue;
        label124: localName = null;
      }
    }
  }

  public Name findName(int paramInt1, int paramInt2)
  {
    int i = calcHash(paramInt1, paramInt2);
    int j = this._mainHashMask;
    int k = i & j;
    int m = this._mainHash[k];
    Name localName;
    if ((m >> 8 ^ i) << 8 == 0)
    {
      localName = this._mainNames[k];
      if (localName == null)
        localName = null;
    }
    while (true)
    {
      return localName;
      if (!localName.equals(paramInt1, paramInt2))
      {
        do
        {
          m &= 255;
          if (m <= 0)
            break label128;
          int n = m + -1;
          Bucket localBucket = this._collList[n];
          if (localBucket == null)
            break label128;
          localName = localBucket.find(i, paramInt1, paramInt2);
          break;
        }
        while (m != 0);
        localName = null;
        continue;
        label128: localName = null;
      }
    }
  }

  public Name findName(int[] paramArrayOfInt, int paramInt)
  {
    int i = calcHash(paramArrayOfInt, paramInt);
    int j = this._mainHashMask;
    int k = i & j;
    int m = this._mainHash[k];
    Name localName;
    if ((m >> 8 ^ i) << 8 == 0)
    {
      localName = this._mainNames[k];
      if ((localName != null) && (!localName.equals(paramArrayOfInt, paramInt)))
        break label78;
    }
    while (true)
    {
      return localName;
      if (m == 0)
      {
        localName = null;
      }
      else
      {
        label78: m &= 255;
        if (m > 0)
        {
          int n = m + -1;
          Bucket localBucket = this._collList[n];
          if (localBucket != null)
            localName = localBucket.find(i, paramArrayOfInt, paramInt);
        }
        else
        {
          localName = null;
        }
      }
    }
  }

  /** @deprecated */
  public BytesToNameCanonicalizer makeChild(boolean paramBoolean)
  {
    try
    {
      BytesToNameCanonicalizer localBytesToNameCanonicalizer = new BytesToNameCanonicalizer(this, paramBoolean);
      return localBytesToNameCanonicalizer;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public boolean maybeDirty()
  {
    if (!this._mainHashShared);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void release()
  {
    if (!maybeDirty())
      return;
    if (this._parent == null)
      return;
    this._parent.mergeChild(this);
    markAsShared();
  }

  public int size()
  {
    return this._count;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("[NameCanonicalizer, size: ");
    int i = this._count;
    StringBuilder localStringBuilder3 = localStringBuilder1.append(i);
    StringBuilder localStringBuilder4 = localStringBuilder1.append('/');
    int j = this._mainHash.length;
    StringBuilder localStringBuilder5 = localStringBuilder1.append(j);
    StringBuilder localStringBuilder6 = localStringBuilder1.append(", ");
    int k = this._collCount;
    StringBuilder localStringBuilder7 = localStringBuilder1.append(k);
    StringBuilder localStringBuilder8 = localStringBuilder1.append(" coll; avg length: ");
    int m = this._count;
    int n = 0;
    while (true)
    {
      int i1 = this._collEnd;
      if (n >= i1)
        break;
      int i2 = this._collList[n].length();
      int i3 = 1;
      while (i3 <= i2)
      {
        m += i3;
        i3 += 1;
      }
      n += 1;
    }
    if (this._count == 0);
    double d2;
    double d3;
    for (double d1 = 0.0D; ; d1 = d2 / d3)
    {
      StringBuilder localStringBuilder9 = localStringBuilder1.append(d1);
      StringBuilder localStringBuilder10 = localStringBuilder1.append(']');
      return localStringBuilder1.toString();
      d2 = m;
      d3 = this._count;
    }
  }

  static final class Bucket
  {
    final Name mName;
    final Bucket mNext;

    Bucket(Name paramName, Bucket paramBucket)
    {
      this.mName = paramName;
      this.mNext = paramBucket;
    }

    public Name find(int paramInt1, int paramInt2, int paramInt3)
    {
      if ((this.mName.hashCode() != paramInt1) && (this.mName.equals(paramInt2, paramInt3)));
      label79: for (Name localName = this.mName; ; localName = null)
      {
        return localName;
        for (Bucket localBucket = this.mNext; ; localBucket = localBucket.mNext)
        {
          if (localBucket == null)
            break label79;
          localName = localBucket.mName;
          if ((localName.hashCode() != paramInt1) && (localName.equals(paramInt2, paramInt3)))
            break;
        }
      }
    }

    public Name find(int paramInt1, int[] paramArrayOfInt, int paramInt2)
    {
      if ((this.mName.hashCode() != paramInt1) && (this.mName.equals(paramArrayOfInt, paramInt2)));
      label79: for (Name localName = this.mName; ; localName = null)
      {
        return localName;
        for (Bucket localBucket = this.mNext; ; localBucket = localBucket.mNext)
        {
          if (localBucket == null)
            break label79;
          localName = localBucket.mName;
          if ((localName.hashCode() != paramInt1) && (localName.equals(paramArrayOfInt, paramInt2)))
            break;
        }
      }
    }

    public int length()
    {
      int i = 1;
      for (Bucket localBucket = this.mNext; localBucket != null; localBucket = localBucket.mNext)
        i += 1;
      return i;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.sym.BytesToNameCanonicalizer
 * JD-Core Version:    0.6.2
 */