package org.codehaus.jackson.util;

public final class BufferRecycler
{
  protected final byte[][] mByteBuffers;
  protected final char[][] mCharBuffers;

  public BufferRecycler()
  {
    byte[] arrayOfByte = new byte[ByteBufferType.values().length];
    this.mByteBuffers = arrayOfByte;
    char[] arrayOfChar = new char[CharBufferType.values().length];
    this.mCharBuffers = arrayOfChar;
  }

  private byte[] balloc(int paramInt)
  {
    return new byte[paramInt];
  }

  private char[] calloc(int paramInt)
  {
    return new char[paramInt];
  }

  public byte[] allocByteBuffer(ByteBufferType paramByteBufferType)
  {
    int i = paramByteBufferType.ordinal();
    byte[] arrayOfByte = this.mByteBuffers[i];
    if (arrayOfByte == null)
    {
      int j = paramByteBufferType.size;
      arrayOfByte = balloc(j);
    }
    while (true)
    {
      return arrayOfByte;
      this.mByteBuffers[i] = null;
    }
  }

  public char[] allocCharBuffer(CharBufferType paramCharBufferType)
  {
    return allocCharBuffer(paramCharBufferType, 0);
  }

  public char[] allocCharBuffer(CharBufferType paramCharBufferType, int paramInt)
  {
    if (paramCharBufferType.size > paramInt)
      paramInt = paramCharBufferType.size;
    int i = paramCharBufferType.ordinal();
    char[] arrayOfChar = this.mCharBuffers[i];
    if ((arrayOfChar == null) || (arrayOfChar.length < paramInt))
      arrayOfChar = calloc(paramInt);
    while (true)
    {
      return arrayOfChar;
      this.mCharBuffers[i] = null;
    }
  }

  public void releaseByteBuffer(ByteBufferType paramByteBufferType, byte[] paramArrayOfByte)
  {
    byte[][] arrayOfByte = this.mByteBuffers;
    int i = paramByteBufferType.ordinal();
    arrayOfByte[i] = paramArrayOfByte;
  }

  public void releaseCharBuffer(CharBufferType paramCharBufferType, char[] paramArrayOfChar)
  {
    char[][] arrayOfChar = this.mCharBuffers;
    int i = paramCharBufferType.ordinal();
    arrayOfChar[i] = paramArrayOfChar;
  }

  public static enum CharBufferType
  {
    private final int size;

    static
    {
      CONCAT_BUFFER = new CharBufferType("CONCAT_BUFFER", 1, 2000);
      TEXT_BUFFER = new CharBufferType("TEXT_BUFFER", 2, 200);
      NAME_COPY_BUFFER = new CharBufferType("NAME_COPY_BUFFER", 3, 200);
      CharBufferType[] arrayOfCharBufferType = new CharBufferType[4];
      CharBufferType localCharBufferType1 = TOKEN_BUFFER;
      arrayOfCharBufferType[0] = localCharBufferType1;
      CharBufferType localCharBufferType2 = CONCAT_BUFFER;
      arrayOfCharBufferType[1] = localCharBufferType2;
      CharBufferType localCharBufferType3 = TEXT_BUFFER;
      arrayOfCharBufferType[2] = localCharBufferType3;
      CharBufferType localCharBufferType4 = NAME_COPY_BUFFER;
      arrayOfCharBufferType[3] = localCharBufferType4;
    }

    private CharBufferType(int paramInt)
    {
      this.size = paramInt;
    }
  }

  public static enum ByteBufferType
  {
    private final int size;

    static
    {
      ByteBufferType[] arrayOfByteBufferType = new ByteBufferType[2];
      ByteBufferType localByteBufferType1 = READ_IO_BUFFER;
      arrayOfByteBufferType[0] = localByteBufferType1;
      ByteBufferType localByteBufferType2 = WRITE_IO_BUFFER;
      arrayOfByteBufferType[1] = localByteBufferType2;
    }

    private ByteBufferType(int paramInt)
    {
      this.size = paramInt;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.util.BufferRecycler
 * JD-Core Version:    0.6.2
 */