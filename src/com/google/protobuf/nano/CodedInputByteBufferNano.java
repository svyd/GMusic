package com.google.protobuf.nano;

import java.io.IOException;

public final class CodedInputByteBufferNano
{
  private final byte[] buffer;
  private int bufferPos;
  private int bufferSize;
  private int bufferSizeAfterLimit;
  private int bufferStart;
  private int currentLimit = 2147483647;
  private int lastTag;
  private int recursionDepth;
  private int recursionLimit = 64;
  private int sizeLimit = 67108864;

  private CodedInputByteBufferNano(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.buffer = paramArrayOfByte;
    this.bufferStart = paramInt1;
    int i = paramInt1 + paramInt2;
    this.bufferSize = i;
    this.bufferPos = paramInt1;
  }

  public static CodedInputByteBufferNano newInstance(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return new CodedInputByteBufferNano(paramArrayOfByte, paramInt1, paramInt2);
  }

  private void recomputeBufferSizeAfterLimit()
  {
    int i = this.bufferSize;
    int j = this.bufferSizeAfterLimit;
    int k = i + j;
    this.bufferSize = k;
    int m = this.bufferSize;
    int n = this.currentLimit;
    if (m > n)
    {
      int i1 = this.currentLimit;
      int i2 = m - i1;
      this.bufferSizeAfterLimit = i2;
      int i3 = this.bufferSize;
      int i4 = this.bufferSizeAfterLimit;
      int i5 = i3 - i4;
      this.bufferSize = i5;
      return;
    }
    this.bufferSizeAfterLimit = 0;
  }

  public void checkLastTagWas(int paramInt)
    throws InvalidProtocolBufferNanoException
  {
    if (this.lastTag != paramInt)
      return;
    throw InvalidProtocolBufferNanoException.invalidEndTag();
  }

  public int getBytesUntilLimit()
  {
    if (this.currentLimit == 2147483647);
    int j;
    for (int i = -1; ; i = this.currentLimit - j)
    {
      return i;
      j = this.bufferPos;
    }
  }

  public int getPosition()
  {
    int i = this.bufferPos;
    int j = this.bufferStart;
    return i - j;
  }

  public boolean isAtEnd()
  {
    int i = this.bufferPos;
    int j = this.bufferSize;
    if (i != j);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void popLimit(int paramInt)
  {
    this.currentLimit = paramInt;
    recomputeBufferSizeAfterLimit();
  }

  public int pushLimit(int paramInt)
    throws InvalidProtocolBufferNanoException
  {
    if (paramInt < 0)
      throw InvalidProtocolBufferNanoException.negativeSize();
    int i = this.bufferPos;
    int j = paramInt + i;
    int k = this.currentLimit;
    if (j > k)
      throw InvalidProtocolBufferNanoException.truncatedMessage();
    this.currentLimit = j;
    recomputeBufferSizeAfterLimit();
    return k;
  }

  public byte[] readBytes()
    throws IOException
  {
    int i = readRawVarint32();
    int j = this.bufferSize;
    int k = this.bufferPos;
    int m = j - k;
    byte[] arrayOfByte1;
    if ((i <= m) && (i > 0))
    {
      arrayOfByte1 = new byte[i];
      byte[] arrayOfByte2 = this.buffer;
      int n = this.bufferPos;
      System.arraycopy(arrayOfByte2, n, arrayOfByte1, 0, i);
      int i1 = this.bufferPos + i;
      this.bufferPos = i1;
    }
    while (true)
    {
      return arrayOfByte1;
      arrayOfByte1 = readRawBytes(i);
    }
  }

  public int readInt32()
    throws IOException
  {
    return readRawVarint32();
  }

  public long readInt64()
    throws IOException
  {
    return readRawVarint64();
  }

  public void readMessage(MessageNano paramMessageNano)
    throws IOException
  {
    int i = readRawVarint32();
    int j = this.recursionDepth;
    int k = this.recursionLimit;
    if (j >= k)
      throw InvalidProtocolBufferNanoException.recursionLimitExceeded();
    int m = pushLimit(i);
    int n = this.recursionDepth + 1;
    this.recursionDepth = n;
    MessageNano localMessageNano = paramMessageNano.mergeFrom(this);
    checkLastTagWas(0);
    int i1 = this.recursionDepth + -1;
    this.recursionDepth = i1;
    popLimit(m);
  }

  public byte readRawByte()
    throws IOException
  {
    int i = this.bufferPos;
    int j = this.bufferSize;
    if (i != j)
      throw InvalidProtocolBufferNanoException.truncatedMessage();
    byte[] arrayOfByte = this.buffer;
    int k = this.bufferPos;
    int m = k + 1;
    this.bufferPos = m;
    return arrayOfByte[k];
  }

  public byte[] readRawBytes(int paramInt)
    throws IOException
  {
    if (paramInt < 0)
      throw InvalidProtocolBufferNanoException.negativeSize();
    int i = this.bufferPos + paramInt;
    int j = this.currentLimit;
    if (i > j)
    {
      int k = this.currentLimit;
      int m = this.bufferPos;
      int n = k - m;
      skipRawBytes(n);
      throw InvalidProtocolBufferNanoException.truncatedMessage();
    }
    int i1 = this.bufferSize;
    int i2 = this.bufferPos;
    int i3 = i1 - i2;
    if (paramInt <= i3)
    {
      byte[] arrayOfByte1 = new byte[paramInt];
      byte[] arrayOfByte2 = this.buffer;
      int i4 = this.bufferPos;
      System.arraycopy(arrayOfByte2, i4, arrayOfByte1, 0, paramInt);
      int i5 = this.bufferPos + paramInt;
      this.bufferPos = i5;
      return arrayOfByte1;
    }
    throw InvalidProtocolBufferNanoException.truncatedMessage();
  }

  public int readRawLittleEndian32()
    throws IOException
  {
    int i = readRawByte();
    int j = readRawByte();
    int k = readRawByte();
    int m = readRawByte();
    int n = i & 0xFF;
    int i1 = (j & 0xFF) << 8;
    int i2 = n | i1;
    int i3 = (k & 0xFF) << 16;
    int i4 = i2 | i3;
    int i5 = (m & 0xFF) << 24;
    return i4 | i5;
  }

  public long readRawLittleEndian64()
    throws IOException
  {
    int i = readRawByte();
    int j = readRawByte();
    int k = readRawByte();
    int m = readRawByte();
    int n = readRawByte();
    int i1 = readRawByte();
    int i2 = readRawByte();
    int i3 = readRawByte();
    long l1 = i & 0xFF;
    long l2 = (j & 0xFF) << 8;
    long l3 = l1 | l2;
    long l4 = (k & 0xFF) << 16;
    long l5 = l3 | l4;
    long l6 = (m & 0xFF) << 24;
    long l7 = l5 | l6;
    long l8 = (n & 0xFF) << 32;
    long l9 = l7 | l8;
    long l10 = (i1 & 0xFF) << 40;
    long l11 = l9 | l10;
    long l12 = (i2 & 0xFF) << 48;
    long l13 = l11 | l12;
    long l14 = (i3 & 0xFF) << 56;
    return l13 | l14;
  }

  public int readRawVarint32()
    throws IOException
  {
    int i = readRawByte();
    int j;
    if (i >= 0)
      j = i;
    int i8;
    do
    {
      int i4;
      while (true)
      {
        return j;
        int k = i & 0x7F;
        i = readRawByte();
        if (i >= 0)
        {
          int m = i << 7;
          j = k | m;
        }
        else
        {
          int n = (i & 0x7F) << 7;
          int i1 = k | n;
          i = readRawByte();
          if (i >= 0)
          {
            int i2 = i << 14;
            j = i1 | i2;
          }
          else
          {
            int i3 = (i & 0x7F) << 14;
            i4 = i1 | i3;
            i = readRawByte();
            if (i < 0)
              break;
            int i5 = i << 21;
            j = i4 | i5;
          }
        }
      }
      int i6 = (i & 0x7F) << 21;
      int i7 = i4 | i6;
      i8 = readRawByte();
      int i9 = i8 << 28;
      j = i7 | i9;
    }
    while (i8 >= 0);
    int i10 = 0;
    while (true)
    {
      if (i10 >= 5)
        break label185;
      if (readRawByte() >= 0)
        break;
      i10 += 1;
    }
    label185: throw InvalidProtocolBufferNanoException.malformedVarint();
  }

  public long readRawVarint64()
    throws IOException
  {
    int i = 0;
    long l1 = 0L;
    while (i < 64)
    {
      int j = readRawByte();
      long l2 = (j & 0x7F) << i;
      l1 |= l2;
      if ((j & 0x80) == 0)
        return l1;
      i += 7;
    }
    throw InvalidProtocolBufferNanoException.malformedVarint();
  }

  public String readString()
    throws IOException
  {
    int i = readRawVarint32();
    int j = this.bufferSize;
    int k = this.bufferPos;
    int m = j - k;
    String str;
    if ((i <= m) && (i > 0))
    {
      byte[] arrayOfByte1 = this.buffer;
      int n = this.bufferPos;
      str = new String(arrayOfByte1, n, i, "UTF-8");
      int i1 = this.bufferPos + i;
      this.bufferPos = i1;
    }
    while (true)
    {
      return str;
      byte[] arrayOfByte2 = readRawBytes(i);
      str = new String(arrayOfByte2, "UTF-8");
    }
  }

  public int readTag()
    throws IOException
  {
    int i = 0;
    if (isAtEnd())
      this.lastTag = 0;
    while (true)
    {
      return i;
      int j = readRawVarint32();
      this.lastTag = j;
      if (this.lastTag == 0)
        throw InvalidProtocolBufferNanoException.invalidTag();
      i = this.lastTag;
    }
  }

  public void rewindToPosition(int paramInt)
  {
    int i = this.bufferPos;
    int j = this.bufferStart;
    int k = i - j;
    if (paramInt > k)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Position ").append(paramInt).append(" is beyond current ");
      int m = this.bufferPos;
      int n = this.bufferStart;
      int i1 = m - n;
      String str1 = i1;
      throw new IllegalArgumentException(str1);
    }
    if (paramInt < 0)
    {
      String str2 = "Bad position " + paramInt;
      throw new IllegalArgumentException(str2);
    }
    int i2 = this.bufferStart + paramInt;
    this.bufferPos = i2;
  }

  public boolean skipField(int paramInt)
    throws IOException
  {
    boolean bool = true;
    switch (WireFormatNano.getTagWireType(paramInt))
    {
    default:
      throw InvalidProtocolBufferNanoException.invalidWireType();
    case 0:
      int i = readInt32();
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    }
    while (true)
    {
      return bool;
      long l = readRawLittleEndian64();
      continue;
      int j = readRawVarint32();
      skipRawBytes(j);
      continue;
      skipMessage();
      int k = WireFormatNano.makeTag(WireFormatNano.getTagFieldNumber(paramInt), 4);
      checkLastTagWas(k);
      continue;
      bool = false;
      continue;
      int m = readRawLittleEndian32();
    }
  }

  public void skipMessage()
    throws IOException
  {
    int i;
    do
    {
      i = readTag();
      if (i == 0)
        return;
    }
    while (skipField(i));
  }

  public void skipRawBytes(int paramInt)
    throws IOException
  {
    if (paramInt < 0)
      throw InvalidProtocolBufferNanoException.negativeSize();
    int i = this.bufferPos + paramInt;
    int j = this.currentLimit;
    if (i > j)
    {
      int k = this.currentLimit;
      int m = this.bufferPos;
      int n = k - m;
      skipRawBytes(n);
      throw InvalidProtocolBufferNanoException.truncatedMessage();
    }
    int i1 = this.bufferSize;
    int i2 = this.bufferPos;
    int i3 = i1 - i2;
    if (paramInt <= i3)
    {
      int i4 = this.bufferPos + paramInt;
      this.bufferPos = i4;
      return;
    }
    throw InvalidProtocolBufferNanoException.truncatedMessage();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.protobuf.nano.CodedInputByteBufferNano
 * JD-Core Version:    0.6.2
 */