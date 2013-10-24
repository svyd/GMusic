package org.codehaus.jackson.util;

import java.math.BigDecimal;
import java.util.ArrayList;

public final class TextBuffer
{
  static final char[] NO_CHARS = new char[0];
  private final BufferRecycler _allocator;
  private char[] _currentSegment;
  private int _currentSize;
  private boolean _hasSegments = false;
  private char[] _inputBuffer;
  private int _inputLen;
  private int _inputStart;
  private char[] _resultArray;
  private String _resultString;
  private int _segmentSize;
  private ArrayList<char[]> _segments;

  public TextBuffer(BufferRecycler paramBufferRecycler)
  {
    this._allocator = paramBufferRecycler;
  }

  private final char[] _charArray(int paramInt)
  {
    return new char[paramInt];
  }

  private char[] buildResultArray()
  {
    char[] arrayOfChar1;
    if (this._resultString != null)
      arrayOfChar1 = this._resultString.toCharArray();
    while (true)
    {
      return arrayOfChar1;
      if (this._inputStart >= 0)
      {
        if (this._inputLen < 1)
        {
          arrayOfChar1 = NO_CHARS;
        }
        else
        {
          int i = this._inputLen;
          arrayOfChar1 = _charArray(i);
          char[] arrayOfChar2 = this._inputBuffer;
          int j = this._inputStart;
          int k = this._inputLen;
          System.arraycopy(arrayOfChar2, j, arrayOfChar1, 0, k);
        }
      }
      else
      {
        int m = size();
        if (m < 1)
        {
          arrayOfChar1 = NO_CHARS;
        }
        else
        {
          int n = 0;
          arrayOfChar1 = _charArray(m);
          if (this._segments != null)
          {
            int i1 = 0;
            int i2 = this._segments.size();
            while (i1 < i2)
            {
              char[] arrayOfChar3 = (char[])this._segments.get(i1);
              int i3 = arrayOfChar3.length;
              System.arraycopy(arrayOfChar3, 0, arrayOfChar1, n, i3);
              n += i3;
              i1 += 1;
            }
          }
          char[] arrayOfChar4 = this._currentSegment;
          int i4 = this._currentSize;
          System.arraycopy(arrayOfChar4, 0, arrayOfChar1, n, i4);
        }
      }
    }
  }

  private final void clearSegments()
  {
    this._hasSegments = false;
    this._segments.clear();
    this._segmentSize = 0;
    this._currentSize = 0;
  }

  private void expand(int paramInt)
  {
    if (this._segments == null)
    {
      ArrayList localArrayList = new ArrayList();
      this._segments = localArrayList;
    }
    char[] arrayOfChar1 = this._currentSegment;
    this._hasSegments = true;
    boolean bool = this._segments.add(arrayOfChar1);
    int i = this._segmentSize;
    int j = arrayOfChar1.length;
    int k = i + j;
    this._segmentSize = k;
    int m = arrayOfChar1.length;
    int n = m >> 1;
    if (n < paramInt)
      n = paramInt;
    int i1 = m + n;
    int i2 = Math.min(262144, i1);
    char[] arrayOfChar2 = _charArray(i2);
    this._currentSize = 0;
    this._currentSegment = arrayOfChar2;
  }

  private final char[] findBuffer(int paramInt)
  {
    BufferRecycler localBufferRecycler = this._allocator;
    BufferRecycler.CharBufferType localCharBufferType = BufferRecycler.CharBufferType.TEXT_BUFFER;
    return localBufferRecycler.allocCharBuffer(localCharBufferType, paramInt);
  }

  private void unshare(int paramInt)
  {
    int i = this._inputLen;
    this._inputLen = 0;
    char[] arrayOfChar1 = this._inputBuffer;
    this._inputBuffer = null;
    int j = this._inputStart;
    this._inputStart = -1;
    int k = i + paramInt;
    if (this._currentSegment != null)
    {
      int m = this._currentSegment.length;
      if (k <= m);
    }
    else
    {
      char[] arrayOfChar2 = findBuffer(k);
      this._currentSegment = arrayOfChar2;
    }
    if (i > 0)
    {
      char[] arrayOfChar3 = this._currentSegment;
      System.arraycopy(arrayOfChar1, j, arrayOfChar3, 0, i);
    }
    this._segmentSize = 0;
    this._currentSize = i;
  }

  public void append(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    if (this._inputStart >= 0)
      unshare(paramInt2);
    this._resultString = null;
    this._resultArray = null;
    char[] arrayOfChar1 = this._currentSegment;
    int i = arrayOfChar1.length;
    int j = this._currentSize;
    int k = i - j;
    if (k >= paramInt2)
    {
      int m = this._currentSize;
      System.arraycopy(paramArrayOfChar, paramInt1, arrayOfChar1, m, paramInt2);
      int n = this._currentSize + paramInt2;
      this._currentSize = n;
      return;
    }
    if (k > 0)
    {
      int i1 = this._currentSize;
      System.arraycopy(paramArrayOfChar, paramInt1, arrayOfChar1, i1, k);
      paramInt1 += k;
      paramInt2 -= k;
    }
    expand(paramInt2);
    char[] arrayOfChar2 = this._currentSegment;
    System.arraycopy(paramArrayOfChar, paramInt1, arrayOfChar2, 0, paramInt2);
    this._currentSize = paramInt2;
  }

  public char[] contentsAsArray()
  {
    char[] arrayOfChar = this._resultArray;
    if (arrayOfChar == null)
    {
      arrayOfChar = buildResultArray();
      this._resultArray = arrayOfChar;
    }
    return arrayOfChar;
  }

  public BigDecimal contentsAsDecimal()
    throws NumberFormatException
  {
    BigDecimal localBigDecimal;
    if (this._resultArray != null)
    {
      char[] arrayOfChar1 = this._resultArray;
      localBigDecimal = new BigDecimal(arrayOfChar1);
    }
    while (true)
    {
      return localBigDecimal;
      if (this._inputStart >= 0)
      {
        char[] arrayOfChar2 = this._inputBuffer;
        int i = this._inputStart;
        int j = this._inputLen;
        localBigDecimal = new BigDecimal(arrayOfChar2, i, j);
      }
      else if (this._segmentSize == 0)
      {
        char[] arrayOfChar3 = this._currentSegment;
        int k = this._currentSize;
        localBigDecimal = new BigDecimal(arrayOfChar3, 0, k);
      }
      else
      {
        char[] arrayOfChar4 = contentsAsArray();
        localBigDecimal = new BigDecimal(arrayOfChar4);
      }
    }
  }

  public double contentsAsDouble()
    throws NumberFormatException
  {
    return Double.parseDouble(contentsAsString());
  }

  public String contentsAsString()
  {
    if (this._resultString == null)
    {
      if (this._resultArray == null)
        break label40;
      char[] arrayOfChar1 = this._resultArray;
      String str1 = new String(arrayOfChar1);
      this._resultString = str1;
    }
    while (true)
    {
      String str2 = this._resultString;
      while (true)
      {
        return str2;
        label40: if (this._inputStart < 0)
          break label108;
        if (this._inputLen >= 1)
          break;
        str2 = "";
        this._resultString = str2;
      }
      char[] arrayOfChar2 = this._inputBuffer;
      int i = this._inputStart;
      int j = this._inputLen;
      String str3 = new String(arrayOfChar2, i, j);
      this._resultString = str3;
      continue;
      label108: int k = this._segmentSize;
      int m = this._currentSize;
      if (k == 0)
      {
        if (m == 0);
        char[] arrayOfChar3;
        for (String str4 = ""; ; str4 = new String(arrayOfChar3, 0, m))
        {
          this._resultString = str4;
          break;
          arrayOfChar3 = this._currentSegment;
        }
      }
      int n = k + m;
      StringBuilder localStringBuilder1 = new StringBuilder(n);
      if (this._segments != null)
      {
        int i1 = 0;
        int i2 = this._segments.size();
        while (i1 < i2)
        {
          char[] arrayOfChar4 = (char[])this._segments.get(i1);
          int i3 = arrayOfChar4.length;
          StringBuilder localStringBuilder2 = localStringBuilder1.append(arrayOfChar4, 0, i3);
          i1 += 1;
        }
      }
      char[] arrayOfChar5 = this._currentSegment;
      int i4 = this._currentSize;
      StringBuilder localStringBuilder3 = localStringBuilder1.append(arrayOfChar5, 0, i4);
      String str5 = localStringBuilder1.toString();
      this._resultString = str5;
    }
  }

  public char[] emptyAndGetCurrentSegment()
  {
    resetWithEmpty();
    char[] arrayOfChar = this._currentSegment;
    if (arrayOfChar == null)
    {
      arrayOfChar = findBuffer(0);
      this._currentSegment = arrayOfChar;
    }
    return arrayOfChar;
  }

  public char[] expandCurrentSegment()
  {
    char[] arrayOfChar1 = this._currentSegment;
    int i = arrayOfChar1.length;
    if (i == 262144);
    int k;
    for (int j = 262145; ; j = Math.min(262144, k))
    {
      char[] arrayOfChar2 = _charArray(j);
      this._currentSegment = arrayOfChar2;
      char[] arrayOfChar3 = this._currentSegment;
      System.arraycopy(arrayOfChar1, 0, arrayOfChar3, 0, i);
      return this._currentSegment;
      k = (i >> 1) + i;
    }
  }

  public char[] finishCurrentSegment()
  {
    if (this._segments == null)
    {
      ArrayList localArrayList1 = new ArrayList();
      this._segments = localArrayList1;
    }
    this._hasSegments = true;
    ArrayList localArrayList2 = this._segments;
    char[] arrayOfChar1 = this._currentSegment;
    boolean bool = localArrayList2.add(arrayOfChar1);
    int i = this._currentSegment.length;
    int j = this._segmentSize + i;
    this._segmentSize = j;
    int k = Math.min((i >> 1) + i, 262144);
    char[] arrayOfChar2 = _charArray(k);
    this._currentSize = 0;
    this._currentSegment = arrayOfChar2;
    return arrayOfChar2;
  }

  public char[] getCurrentSegment()
  {
    if (this._inputStart >= 0)
      unshare(1);
    while (true)
    {
      return this._currentSegment;
      char[] arrayOfChar1 = this._currentSegment;
      if (arrayOfChar1 == null)
      {
        char[] arrayOfChar2 = findBuffer(0);
        this._currentSegment = arrayOfChar2;
      }
      else
      {
        int i = this._currentSize;
        int j = arrayOfChar1.length;
        if (i >= j)
          expand(1);
      }
    }
  }

  public int getCurrentSegmentSize()
  {
    return this._currentSize;
  }

  public char[] getTextBuffer()
  {
    char[] arrayOfChar;
    if (this._inputStart >= 0)
      arrayOfChar = this._inputBuffer;
    while (true)
    {
      return arrayOfChar;
      if (!this._hasSegments)
        arrayOfChar = this._currentSegment;
      else
        arrayOfChar = contentsAsArray();
    }
  }

  public int getTextOffset()
  {
    if (this._inputStart >= 0);
    for (int i = this._inputStart; ; i = 0)
      return i;
  }

  public void releaseBuffers()
  {
    if (this._allocator == null)
      return;
    if (this._currentSegment == null)
      return;
    resetWithEmpty();
    char[] arrayOfChar = this._currentSegment;
    this._currentSegment = null;
    BufferRecycler localBufferRecycler = this._allocator;
    BufferRecycler.CharBufferType localCharBufferType = BufferRecycler.CharBufferType.TEXT_BUFFER;
    localBufferRecycler.releaseCharBuffer(localCharBufferType, arrayOfChar);
  }

  public void resetWithCopy(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    this._inputBuffer = null;
    this._inputStart = -1;
    this._inputLen = 0;
    this._resultString = null;
    this._resultArray = null;
    if (this._hasSegments)
      clearSegments();
    while (true)
    {
      this._segmentSize = 0;
      this._currentSize = 0;
      append(paramArrayOfChar, paramInt1, paramInt2);
      return;
      if (this._currentSegment == null)
      {
        char[] arrayOfChar = findBuffer(paramInt2);
        this._currentSegment = arrayOfChar;
      }
    }
  }

  public void resetWithEmpty()
  {
    this._inputBuffer = null;
    this._inputStart = -1;
    this._inputLen = 0;
    this._resultString = null;
    this._resultArray = null;
    if (this._hasSegments)
      clearSegments();
    this._currentSize = 0;
  }

  public void resetWithShared(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    this._resultString = null;
    this._resultArray = null;
    this._inputBuffer = paramArrayOfChar;
    this._inputStart = paramInt1;
    this._inputLen = paramInt2;
    if (!this._hasSegments)
      return;
    clearSegments();
  }

  public void setCurrentLength(int paramInt)
  {
    this._currentSize = paramInt;
  }

  public int size()
  {
    if (this._inputStart >= 0);
    int j;
    int k;
    for (int i = this._inputLen; ; i = j + k)
    {
      return i;
      j = this._segmentSize;
      k = this._currentSize;
    }
  }

  public String toString()
  {
    return contentsAsString();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.util.TextBuffer
 * JD-Core Version:    0.6.2
 */