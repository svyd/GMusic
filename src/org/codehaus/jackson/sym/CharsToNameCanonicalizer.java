package org.codehaus.jackson.sym;

import org.codehaus.jackson.util.InternCache;

public final class CharsToNameCanonicalizer
{
  static final CharsToNameCanonicalizer sBootstrapSymbolTable = new CharsToNameCanonicalizer(64, true);
  protected Bucket[] _buckets;
  protected boolean _dirty;
  protected int _indexMask;
  final boolean _intern;
  protected CharsToNameCanonicalizer _parent;
  protected int _size;
  protected int _sizeThreshold;
  protected String[] _symbols;

  public CharsToNameCanonicalizer(int paramInt, boolean paramBoolean)
  {
    this._intern = paramBoolean;
    this._dirty = true;
    if (paramInt < 1)
    {
      String str = "Can not use negative/zero initial size: " + paramInt;
      throw new IllegalArgumentException(str);
    }
    int i = 4;
    while (i < paramInt)
      i += i;
    int j = i;
    initTables(j);
  }

  private CharsToNameCanonicalizer(CharsToNameCanonicalizer paramCharsToNameCanonicalizer, boolean paramBoolean, String[] paramArrayOfString, Bucket[] paramArrayOfBucket, int paramInt)
  {
    this._parent = paramCharsToNameCanonicalizer;
    this._intern = paramBoolean;
    this._symbols = paramArrayOfString;
    this._buckets = paramArrayOfBucket;
    this._size = paramInt;
    int i = paramArrayOfString.length;
    int j = i >> 2;
    int k = i - j;
    this._sizeThreshold = k;
    int m = i + -1;
    this._indexMask = m;
    this._dirty = false;
  }

  public static int calcHash(String paramString)
  {
    int i = paramString.charAt(0);
    int j = 1;
    int k = paramString.length();
    while (j < k)
    {
      int m = i * 31;
      int n = paramString.charAt(j);
      i = m + n;
      j += 1;
    }
    return i;
  }

  public static int calcHash(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = paramArrayOfChar[0];
    int j = 1;
    while (j < paramInt2)
    {
      int k = i * 31;
      int m = paramArrayOfChar[j];
      i = k + m;
      j += 1;
    }
    return i;
  }

  private void copyArrays()
  {
    String[] arrayOfString1 = this._symbols;
    int i = arrayOfString1.length;
    String[] arrayOfString2 = new String[i];
    this._symbols = arrayOfString2;
    String[] arrayOfString3 = this._symbols;
    System.arraycopy(arrayOfString1, 0, arrayOfString3, 0, i);
    Bucket[] arrayOfBucket1 = this._buckets;
    int j = arrayOfBucket1.length;
    Bucket[] arrayOfBucket2 = new Bucket[j];
    this._buckets = arrayOfBucket2;
    Bucket[] arrayOfBucket3 = this._buckets;
    System.arraycopy(arrayOfBucket1, 0, arrayOfBucket3, 0, j);
  }

  public static CharsToNameCanonicalizer createRoot(boolean paramBoolean)
  {
    return sBootstrapSymbolTable.makeOrphan(paramBoolean);
  }

  private void initTables(int paramInt)
  {
    String[] arrayOfString = new String[paramInt];
    this._symbols = arrayOfString;
    Bucket[] arrayOfBucket = new Bucket[paramInt >> 1];
    this._buckets = arrayOfBucket;
    int i = paramInt + -1;
    this._indexMask = i;
    this._size = 0;
    int j = paramInt >> 2;
    int k = paramInt - j;
    this._sizeThreshold = k;
  }

  private CharsToNameCanonicalizer makeOrphan(boolean paramBoolean)
  {
    String[] arrayOfString = this._symbols;
    Bucket[] arrayOfBucket = this._buckets;
    int i = this._size;
    boolean bool = paramBoolean;
    return new CharsToNameCanonicalizer(null, bool, arrayOfString, arrayOfBucket, i);
  }

  /** @deprecated */
  private void mergeChild(CharsToNameCanonicalizer paramCharsToNameCanonicalizer)
  {
    try
    {
      if (paramCharsToNameCanonicalizer.size() > 6000)
        initTables(64);
      while (true)
      {
        this._dirty = false;
        int i;
        int j;
        do
        {
          return;
          i = paramCharsToNameCanonicalizer.size();
          j = size();
        }
        while (i <= j);
        String[] arrayOfString = paramCharsToNameCanonicalizer._symbols;
        this._symbols = arrayOfString;
        Bucket[] arrayOfBucket = paramCharsToNameCanonicalizer._buckets;
        this._buckets = arrayOfBucket;
        int k = paramCharsToNameCanonicalizer._size;
        this._size = k;
        int m = paramCharsToNameCanonicalizer._sizeThreshold;
        this._sizeThreshold = m;
        int n = paramCharsToNameCanonicalizer._indexMask;
        this._indexMask = n;
      }
    }
    finally
    {
    }
  }

  private void rehash()
  {
    int i = this._symbols.length;
    int j = i + i;
    String[] arrayOfString1 = this._symbols;
    Bucket[] arrayOfBucket1 = this._buckets;
    String[] arrayOfString2 = new String[j];
    this._symbols = arrayOfString2;
    Bucket[] arrayOfBucket2 = new Bucket[j >> 1];
    this._buckets = arrayOfBucket2;
    int k = j + -1;
    this._indexMask = k;
    int m = this._sizeThreshold;
    int n = this._sizeThreshold;
    int i1 = m + n;
    this._sizeThreshold = i1;
    int i2 = 0;
    int i3 = 0;
    if (i3 < i)
    {
      String str1 = arrayOfString1[i3];
      int i6;
      if (str1 != null)
      {
        i2 += 1;
        int i4 = calcHash(str1);
        int i5 = this._indexMask;
        i6 = i4 & i5;
        if (this._symbols[i6] != null)
          break label161;
        this._symbols[i6] = str1;
      }
      while (true)
      {
        i3 += 1;
        break;
        label161: int i7 = i6 >> 1;
        Bucket[] arrayOfBucket3 = this._buckets;
        Bucket localBucket1 = this._buckets[i7];
        Bucket localBucket2 = new Bucket(str1, localBucket1);
        arrayOfBucket3[i7] = localBucket2;
      }
    }
    int i8 = i >> 1;
    int i9 = 0;
    while (i9 < i8)
    {
      Bucket localBucket3 = arrayOfBucket1[i9];
      if (localBucket3 != null)
      {
        i2 += 1;
        String str2 = localBucket3.getSymbol();
        int i10 = calcHash(str2);
        int i11 = this._indexMask;
        int i12 = i10 & i11;
        if (this._symbols[i12] == null)
          this._symbols[i12] = str2;
        while (true)
        {
          localBucket3 = localBucket3.getNext();
          break;
          int i13 = i12 >> 1;
          Bucket[] arrayOfBucket4 = this._buckets;
          Bucket localBucket4 = this._buckets[i13];
          Bucket localBucket5 = new Bucket(str2, localBucket4);
          arrayOfBucket4[i13] = localBucket5;
        }
      }
      i9 += 1;
    }
    int i14 = this._size;
    if (i2 != i14)
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("Internal error on SymbolTable.rehash(): had ");
    int i15 = this._size;
    String str3 = i15 + " entries; now have " + i2 + ".";
    throw new Error(str3);
  }

  public String findSymbol(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3)
  {
    Object localObject;
    if (paramInt2 < 1)
      localObject = "";
    int j;
    label51: label79: 
    do
    {
      return localObject;
      int i = this._indexMask;
      paramInt3 &= i;
      localObject = this._symbols[paramInt3];
      if (localObject == null)
        break;
      if (((String)localObject).length() != paramInt2)
      {
        j = 0;
        int k = ((String)localObject).charAt(j);
        int m = paramInt1 + j;
        int n = paramArrayOfChar[m];
        if (k == n)
          break label209;
        if (j == paramInt2)
          break label222;
      }
      Bucket[] arrayOfBucket1 = this._buckets;
      int i1 = paramInt3 >> 1;
      Bucket localBucket1 = arrayOfBucket1[i1];
      if (localBucket1 == null)
        break;
      localObject = localBucket1.find(paramArrayOfChar, paramInt1, paramInt2);
    }
    while (localObject != null);
    label140: String str;
    if (!this._dirty)
    {
      copyArrays();
      this._dirty = true;
      int i2 = this._size + 1;
      this._size = i2;
      str = new String(paramArrayOfChar, paramInt1, paramInt2);
      if (this._intern)
        str = InternCache.instance.intern(str);
      if (this._symbols[paramInt3] != null)
        break label271;
      this._symbols[paramInt3] = str;
    }
    while (true)
    {
      localObject = str;
      break;
      label209: j += 1;
      if (j < paramInt2)
        break label51;
      break label79;
      label222: break;
      int i3 = this._size;
      int i4 = this._sizeThreshold;
      if (i3 < i4)
        break label140;
      rehash();
      int i5 = calcHash(paramArrayOfChar, paramInt1, paramInt2);
      int i6 = this._indexMask;
      paramInt3 = i5 & i6;
      break label140;
      label271: int i7 = paramInt3 >> 1;
      Bucket[] arrayOfBucket2 = this._buckets;
      Bucket localBucket2 = this._buckets[i7];
      Bucket localBucket3 = new Bucket(str, localBucket2);
      arrayOfBucket2[i7] = localBucket3;
    }
  }

  /** @deprecated */
  public CharsToNameCanonicalizer makeChild(boolean paramBoolean)
  {
    try
    {
      String[] arrayOfString = this._symbols;
      Bucket[] arrayOfBucket = this._buckets;
      int i = this._size;
      CharsToNameCanonicalizer localCharsToNameCanonicalizer1 = this;
      boolean bool = paramBoolean;
      CharsToNameCanonicalizer localCharsToNameCanonicalizer2 = new CharsToNameCanonicalizer(localCharsToNameCanonicalizer1, bool, arrayOfString, arrayOfBucket, i);
      return localCharsToNameCanonicalizer2;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public boolean maybeDirty()
  {
    return this._dirty;
  }

  public void release()
  {
    if (!maybeDirty())
      return;
    if (this._parent == null)
      return;
    this._parent.mergeChild(this);
    this._dirty = false;
  }

  public int size()
  {
    return this._size;
  }

  static final class Bucket
  {
    private final String _symbol;
    private final Bucket mNext;

    public Bucket(String paramString, Bucket paramBucket)
    {
      this._symbol = paramString;
      this.mNext = paramBucket;
    }

    public String find(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    {
      String str = this._symbol;
      for (Bucket localBucket = this.mNext; ; localBucket = localBucket.getNext())
      {
        int i;
        if (str.length() != paramInt2)
        {
          i = 0;
          int j = str.charAt(i);
          int k = paramInt1 + i;
          int m = paramArrayOfChar[k];
          if (j != m)
            label52: if (i == paramInt2)
              break label76;
        }
        while (true)
        {
          return str;
          i += 1;
          if (i < paramInt2)
            break;
          break label52;
          label76: if (localBucket != null)
            break label87;
          str = null;
        }
        label87: str = localBucket.getSymbol();
      }
    }

    public Bucket getNext()
    {
      return this.mNext;
    }

    public String getSymbol()
    {
      return this._symbol;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.sym.CharsToNameCanonicalizer
 * JD-Core Version:    0.6.2
 */