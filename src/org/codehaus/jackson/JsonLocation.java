package org.codehaus.jackson;

import java.io.Serializable;

public class JsonLocation
  implements Serializable
{
  public static final JsonLocation NA = new JsonLocation("N/A", 65535L, l, -1, i);
  private static final long serialVersionUID = 1L;
  final int _columnNr;
  final int _lineNr;
  final Object _sourceRef;
  final long _totalBytes;
  final long _totalChars;

  static
  {
    long l = 65535L;
    int i = -1;
  }

  public JsonLocation(Object paramObject, long paramLong, int paramInt1, int paramInt2)
  {
  }

  public JsonLocation(Object paramObject, long paramLong1, long paramLong2, int paramInt1, int paramInt2)
  {
    this._sourceRef = paramObject;
    this._totalBytes = paramLong1;
    this._totalChars = paramLong2;
    this._lineNr = paramInt1;
    this._columnNr = paramInt2;
  }

  public boolean equals(Object paramObject)
  {
    boolean bool1 = true;
    boolean bool2 = false;
    if (paramObject == this)
      bool2 = true;
    JsonLocation localJsonLocation;
    do
    {
      do
        return bool2;
      while ((paramObject == null) || (!(paramObject instanceof JsonLocation)));
      localJsonLocation = (JsonLocation)paramObject;
      if (this._sourceRef != null)
        break;
    }
    while (localJsonLocation._sourceRef != null);
    label45: int i = this._lineNr;
    int j = localJsonLocation._lineNr;
    if (i != j)
    {
      int k = this._columnNr;
      int m = localJsonLocation._columnNr;
      if (k != m)
      {
        long l1 = this._totalChars;
        long l2 = localJsonLocation._totalChars;
        if (l1 == l2)
        {
          long l3 = getByteOffset();
          long l4 = localJsonLocation.getByteOffset();
          if (l3 != l4);
        }
      }
    }
    while (true)
    {
      bool2 = bool1;
      break;
      Object localObject1 = this._sourceRef;
      Object localObject2 = localJsonLocation._sourceRef;
      if (localObject1.equals(localObject2))
        break label45;
      break;
      bool1 = false;
    }
  }

  public long getByteOffset()
  {
    return this._totalBytes;
  }

  public int hashCode()
  {
    if (this._sourceRef == null);
    for (int i = 1; ; i = this._sourceRef.hashCode())
    {
      int j = this._lineNr;
      int k = i ^ j;
      int m = this._columnNr;
      int n = k + m;
      int i1 = (int)this._totalChars;
      int i2 = n ^ i1;
      int i3 = (int)this._totalBytes;
      return i2 + i3;
    }
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder(80);
    StringBuilder localStringBuilder2 = localStringBuilder1.append("[Source: ");
    if (this._sourceRef == null)
      StringBuilder localStringBuilder3 = localStringBuilder1.append("UNKNOWN");
    while (true)
    {
      StringBuilder localStringBuilder4 = localStringBuilder1.append("; line: ");
      int i = this._lineNr;
      StringBuilder localStringBuilder5 = localStringBuilder1.append(i);
      StringBuilder localStringBuilder6 = localStringBuilder1.append(", column: ");
      int j = this._columnNr;
      StringBuilder localStringBuilder7 = localStringBuilder1.append(j);
      StringBuilder localStringBuilder8 = localStringBuilder1.append(']');
      return localStringBuilder1.toString();
      String str = this._sourceRef.toString();
      StringBuilder localStringBuilder9 = localStringBuilder1.append(str);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.JsonLocation
 * JD-Core Version:    0.6.2
 */