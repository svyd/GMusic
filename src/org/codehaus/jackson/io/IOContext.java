package org.codehaus.jackson.io;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.util.BufferRecycler;
import org.codehaus.jackson.util.BufferRecycler.ByteBufferType;
import org.codehaus.jackson.util.BufferRecycler.CharBufferType;
import org.codehaus.jackson.util.TextBuffer;

public final class IOContext
{
  final BufferRecycler _bufferRecycler;
  protected char[] _concatBuffer = null;
  protected JsonEncoding _encoding;
  protected final boolean _managedResource;
  protected char[] _nameCopyBuffer = null;
  protected byte[] _readIOBuffer = null;
  final Object _sourceRef;
  protected char[] _tokenBuffer = null;
  protected byte[] _writeIOBuffer = null;

  public IOContext(BufferRecycler paramBufferRecycler, Object paramObject, boolean paramBoolean)
  {
    this._bufferRecycler = paramBufferRecycler;
    this._sourceRef = paramObject;
    this._managedResource = paramBoolean;
  }

  public char[] allocConcatBuffer()
  {
    if (this._concatBuffer != null)
      throw new IllegalStateException("Trying to call allocConcatBuffer() second time");
    BufferRecycler localBufferRecycler = this._bufferRecycler;
    BufferRecycler.CharBufferType localCharBufferType = BufferRecycler.CharBufferType.CONCAT_BUFFER;
    char[] arrayOfChar = localBufferRecycler.allocCharBuffer(localCharBufferType);
    this._concatBuffer = arrayOfChar;
    return this._concatBuffer;
  }

  public byte[] allocReadIOBuffer()
  {
    if (this._readIOBuffer != null)
      throw new IllegalStateException("Trying to call allocReadIOBuffer() second time");
    BufferRecycler localBufferRecycler = this._bufferRecycler;
    BufferRecycler.ByteBufferType localByteBufferType = BufferRecycler.ByteBufferType.READ_IO_BUFFER;
    byte[] arrayOfByte = localBufferRecycler.allocByteBuffer(localByteBufferType);
    this._readIOBuffer = arrayOfByte;
    return this._readIOBuffer;
  }

  public char[] allocTokenBuffer()
  {
    if (this._tokenBuffer != null)
      throw new IllegalStateException("Trying to call allocTokenBuffer() second time");
    BufferRecycler localBufferRecycler = this._bufferRecycler;
    BufferRecycler.CharBufferType localCharBufferType = BufferRecycler.CharBufferType.TOKEN_BUFFER;
    char[] arrayOfChar = localBufferRecycler.allocCharBuffer(localCharBufferType);
    this._tokenBuffer = arrayOfChar;
    return this._tokenBuffer;
  }

  public byte[] allocWriteIOBuffer()
  {
    if (this._writeIOBuffer != null)
      throw new IllegalStateException("Trying to call allocWriteIOBuffer() second time");
    BufferRecycler localBufferRecycler = this._bufferRecycler;
    BufferRecycler.ByteBufferType localByteBufferType = BufferRecycler.ByteBufferType.WRITE_IO_BUFFER;
    byte[] arrayOfByte = localBufferRecycler.allocByteBuffer(localByteBufferType);
    this._writeIOBuffer = arrayOfByte;
    return this._writeIOBuffer;
  }

  public TextBuffer constructTextBuffer()
  {
    BufferRecycler localBufferRecycler = this._bufferRecycler;
    return new TextBuffer(localBufferRecycler);
  }

  public JsonEncoding getEncoding()
  {
    return this._encoding;
  }

  public Object getSourceReference()
  {
    return this._sourceRef;
  }

  public boolean isResourceManaged()
  {
    return this._managedResource;
  }

  public void releaseConcatBuffer(char[] paramArrayOfChar)
  {
    if (paramArrayOfChar == null)
      return;
    char[] arrayOfChar = this._concatBuffer;
    if (paramArrayOfChar != arrayOfChar)
      throw new IllegalArgumentException("Trying to release buffer not owned by the context");
    this._concatBuffer = null;
    BufferRecycler localBufferRecycler = this._bufferRecycler;
    BufferRecycler.CharBufferType localCharBufferType = BufferRecycler.CharBufferType.CONCAT_BUFFER;
    localBufferRecycler.releaseCharBuffer(localCharBufferType, paramArrayOfChar);
  }

  public void releaseNameCopyBuffer(char[] paramArrayOfChar)
  {
    if (paramArrayOfChar == null)
      return;
    char[] arrayOfChar = this._nameCopyBuffer;
    if (paramArrayOfChar != arrayOfChar)
      throw new IllegalArgumentException("Trying to release buffer not owned by the context");
    this._nameCopyBuffer = null;
    BufferRecycler localBufferRecycler = this._bufferRecycler;
    BufferRecycler.CharBufferType localCharBufferType = BufferRecycler.CharBufferType.NAME_COPY_BUFFER;
    localBufferRecycler.releaseCharBuffer(localCharBufferType, paramArrayOfChar);
  }

  public void releaseReadIOBuffer(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      return;
    byte[] arrayOfByte = this._readIOBuffer;
    if (paramArrayOfByte != arrayOfByte)
      throw new IllegalArgumentException("Trying to release buffer not owned by the context");
    this._readIOBuffer = null;
    BufferRecycler localBufferRecycler = this._bufferRecycler;
    BufferRecycler.ByteBufferType localByteBufferType = BufferRecycler.ByteBufferType.READ_IO_BUFFER;
    localBufferRecycler.releaseByteBuffer(localByteBufferType, paramArrayOfByte);
  }

  public void releaseTokenBuffer(char[] paramArrayOfChar)
  {
    if (paramArrayOfChar == null)
      return;
    char[] arrayOfChar = this._tokenBuffer;
    if (paramArrayOfChar != arrayOfChar)
      throw new IllegalArgumentException("Trying to release buffer not owned by the context");
    this._tokenBuffer = null;
    BufferRecycler localBufferRecycler = this._bufferRecycler;
    BufferRecycler.CharBufferType localCharBufferType = BufferRecycler.CharBufferType.TOKEN_BUFFER;
    localBufferRecycler.releaseCharBuffer(localCharBufferType, paramArrayOfChar);
  }

  public void releaseWriteIOBuffer(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null)
      return;
    byte[] arrayOfByte = this._writeIOBuffer;
    if (paramArrayOfByte != arrayOfByte)
      throw new IllegalArgumentException("Trying to release buffer not owned by the context");
    this._writeIOBuffer = null;
    BufferRecycler localBufferRecycler = this._bufferRecycler;
    BufferRecycler.ByteBufferType localByteBufferType = BufferRecycler.ByteBufferType.WRITE_IO_BUFFER;
    localBufferRecycler.releaseByteBuffer(localByteBufferType, paramArrayOfByte);
  }

  public void setEncoding(JsonEncoding paramJsonEncoding)
  {
    this._encoding = paramJsonEncoding;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.io.IOContext
 * JD-Core Version:    0.6.2
 */