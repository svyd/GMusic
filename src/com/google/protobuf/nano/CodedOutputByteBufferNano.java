package com.google.protobuf.nano;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public final class CodedOutputByteBufferNano
{
  private final byte[] buffer;
  private final int limit;
  private int position;

  private CodedOutputByteBufferNano(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.buffer = paramArrayOfByte;
    this.position = paramInt1;
    int i = paramInt1 + paramInt2;
    this.limit = i;
  }

  public static int computeBytesSize(int paramInt, byte[] paramArrayOfByte)
  {
    int i = computeTagSize(paramInt);
    int j = computeBytesSizeNoTag(paramArrayOfByte);
    return i + j;
  }

  public static int computeBytesSizeNoTag(byte[] paramArrayOfByte)
  {
    int i = computeRawVarint32Size(paramArrayOfByte.length);
    int j = paramArrayOfByte.length;
    return i + j;
  }

  public static int computeInt32Size(int paramInt1, int paramInt2)
  {
    int i = computeTagSize(paramInt1);
    int j = computeInt32SizeNoTag(paramInt2);
    return i + j;
  }

  public static int computeInt32SizeNoTag(int paramInt)
  {
    if (paramInt >= 0);
    for (int i = computeRawVarint32Size(paramInt); ; i = 10)
      return i;
  }

  public static int computeInt64Size(int paramInt, long paramLong)
  {
    int i = computeTagSize(paramInt);
    int j = computeInt64SizeNoTag(paramLong);
    return i + j;
  }

  public static int computeInt64SizeNoTag(long paramLong)
  {
    return computeRawVarint64Size(paramLong);
  }

  public static int computeMessageSize(int paramInt, MessageNano paramMessageNano)
  {
    int i = computeTagSize(paramInt);
    int j = computeMessageSizeNoTag(paramMessageNano);
    return i + j;
  }

  public static int computeMessageSizeNoTag(MessageNano paramMessageNano)
  {
    int i = paramMessageNano.getSerializedSize();
    return computeRawVarint32Size(i) + i;
  }

  public static int computeRawVarint32Size(int paramInt)
  {
    int i;
    if ((paramInt & 0xFFFFFF80) == 0)
      i = 1;
    while (true)
    {
      return i;
      if ((paramInt & 0xFFFFC000) == 0)
        i = 2;
      else if ((0xFFE00000 & paramInt) == 0)
        i = 3;
      else if ((0xF0000000 & paramInt) == 0)
        i = 4;
      else
        i = 5;
    }
  }

  public static int computeRawVarint64Size(long paramLong)
  {
    int i;
    if ((0xFF80 & paramLong) == 0L)
      i = 1;
    while (true)
    {
      return i;
      if ((0xC000 & paramLong) == 0L)
        i = 2;
      else if ((0xFFE00000 & paramLong) == 0L)
        i = 3;
      else if ((0xF0000000 & paramLong) == 0L)
        i = 4;
      else if ((0x0 & paramLong) == 0L)
        i = 5;
      else if ((0x0 & paramLong) == 0L)
        i = 6;
      else if ((0x0 & paramLong) == 0L)
        i = 7;
      else if ((0x0 & paramLong) == 0L)
        i = 8;
      else if ((0x0 & paramLong) == 0L)
        i = 9;
      else
        i = 10;
    }
  }

  public static int computeStringSize(int paramInt, String paramString)
  {
    int i = computeTagSize(paramInt);
    int j = computeStringSizeNoTag(paramString);
    return i + j;
  }

  public static int computeStringSizeNoTag(String paramString)
  {
    try
    {
      byte[] arrayOfByte = paramString.getBytes("UTF-8");
      int i = computeRawVarint32Size(arrayOfByte.length);
      int j = arrayOfByte.length;
      return i + j;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    throw new RuntimeException("UTF-8 not supported.");
  }

  public static int computeTagSize(int paramInt)
  {
    return computeRawVarint32Size(WireFormatNano.makeTag(paramInt, 0));
  }

  public static CodedOutputByteBufferNano newInstance(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return new CodedOutputByteBufferNano(paramArrayOfByte, paramInt1, paramInt2);
  }

  public void checkNoSpaceLeft()
  {
    if (spaceLeft() == 0)
      return;
    throw new IllegalStateException("Did not write as much data as expected.");
  }

  public int spaceLeft()
  {
    int i = this.limit;
    int j = this.position;
    return i - j;
  }

  public void writeBytes(int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    writeTag(paramInt, 2);
    writeBytesNoTag(paramArrayOfByte);
  }

  public void writeBytesNoTag(byte[] paramArrayOfByte)
    throws IOException
  {
    int i = paramArrayOfByte.length;
    writeRawVarint32(i);
    writeRawBytes(paramArrayOfByte);
  }

  public void writeInt32(int paramInt1, int paramInt2)
    throws IOException
  {
    writeTag(paramInt1, 0);
    writeInt32NoTag(paramInt2);
  }

  public void writeInt32NoTag(int paramInt)
    throws IOException
  {
    if (paramInt >= 0)
    {
      writeRawVarint32(paramInt);
      return;
    }
    long l = paramInt;
    writeRawVarint64(l);
  }

  public void writeInt64(int paramInt, long paramLong)
    throws IOException
  {
    writeTag(paramInt, 0);
    writeInt64NoTag(paramLong);
  }

  public void writeInt64NoTag(long paramLong)
    throws IOException
  {
    writeRawVarint64(paramLong);
  }

  public void writeMessage(int paramInt, MessageNano paramMessageNano)
    throws IOException
  {
    writeTag(paramInt, 2);
    writeMessageNoTag(paramMessageNano);
  }

  public void writeMessageNoTag(MessageNano paramMessageNano)
    throws IOException
  {
    int i = paramMessageNano.getCachedSize();
    writeRawVarint32(i);
    paramMessageNano.writeTo(this);
  }

  public void writeRawByte(byte paramByte)
    throws IOException
  {
    int i = this.position;
    int j = this.limit;
    if (i != j)
    {
      int k = this.position;
      int m = this.limit;
      throw new OutOfSpaceException(k, m);
    }
    byte[] arrayOfByte = this.buffer;
    int n = this.position;
    int i1 = n + 1;
    this.position = i1;
    arrayOfByte[n] = paramByte;
  }

  public void writeRawByte(int paramInt)
    throws IOException
  {
    byte b = (byte)paramInt;
    writeRawByte(b);
  }

  public void writeRawBytes(byte[] paramArrayOfByte)
    throws IOException
  {
    int i = paramArrayOfByte.length;
    writeRawBytes(paramArrayOfByte, 0, i);
  }

  public void writeRawBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = this.limit;
    int j = this.position;
    if (i - j >= paramInt2)
    {
      byte[] arrayOfByte = this.buffer;
      int k = this.position;
      System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, k, paramInt2);
      int m = this.position + paramInt2;
      this.position = m;
      return;
    }
    int n = this.position;
    int i1 = this.limit;
    throw new OutOfSpaceException(n, i1);
  }

  public void writeRawVarint32(int paramInt)
    throws IOException
  {
    while (true)
    {
      if ((paramInt & 0xFFFFFF80) == 0)
      {
        writeRawByte(paramInt);
        return;
      }
      int i = paramInt & 0x7F | 0x80;
      writeRawByte(i);
      paramInt >>>= 7;
    }
  }

  public void writeRawVarint64(long paramLong)
    throws IOException
  {
    while (true)
    {
      if ((0xFF80 & paramLong) == 0L)
      {
        int i = (int)paramLong;
        writeRawByte(i);
        return;
      }
      int j = (int)paramLong & 0x7F | 0x80;
      writeRawByte(j);
      paramLong >>>= 7;
    }
  }

  public void writeString(int paramInt, String paramString)
    throws IOException
  {
    writeTag(paramInt, 2);
    writeStringNoTag(paramString);
  }

  public void writeStringNoTag(String paramString)
    throws IOException
  {
    byte[] arrayOfByte = paramString.getBytes("UTF-8");
    int i = arrayOfByte.length;
    writeRawVarint32(i);
    writeRawBytes(arrayOfByte);
  }

  public void writeTag(int paramInt1, int paramInt2)
    throws IOException
  {
    int i = WireFormatNano.makeTag(paramInt1, paramInt2);
    writeRawVarint32(i);
  }

  public static class OutOfSpaceException extends IOException
  {
    private static final long serialVersionUID = -6947486886997889499L;

    OutOfSpaceException(int paramInt1, int paramInt2)
    {
      super();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.protobuf.nano.CodedOutputByteBufferNano
 * JD-Core Version:    0.6.2
 */