package org.codehaus.jackson.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

abstract class BaseReader extends Reader
{
  protected byte[] mBuffer;
  protected final IOContext mContext;
  protected InputStream mIn;
  protected int mLength;
  protected int mPtr;
  char[] mTmpBuf = null;

  protected BaseReader(IOContext paramIOContext, InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.mContext = paramIOContext;
    this.mIn = paramInputStream;
    this.mBuffer = paramArrayOfByte;
    this.mPtr = paramInt1;
    this.mLength = paramInt2;
  }

  public void close()
    throws IOException
  {
    InputStream localInputStream = this.mIn;
    if (localInputStream == null)
      return;
    this.mIn = null;
    freeBuffers();
    localInputStream.close();
  }

  public final void freeBuffers()
  {
    byte[] arrayOfByte = this.mBuffer;
    if (arrayOfByte == null)
      return;
    this.mBuffer = null;
    this.mContext.releaseReadIOBuffer(arrayOfByte);
  }

  public int read()
    throws IOException
  {
    if (this.mTmpBuf == null)
    {
      char[] arrayOfChar1 = new char[1];
      this.mTmpBuf = arrayOfChar1;
    }
    char[] arrayOfChar2 = this.mTmpBuf;
    if (read(arrayOfChar2, 0, 1) < 1);
    for (int i = -1; ; i = this.mTmpBuf[0])
      return i;
  }

  protected void reportBounds(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    StringBuilder localStringBuilder = new StringBuilder().append("read(buf,").append(paramInt1).append(",").append(paramInt2).append("), cbuf[");
    int i = paramArrayOfChar.length;
    String str = i + "]";
    throw new ArrayIndexOutOfBoundsException(str);
  }

  protected void reportStrangeStream()
    throws IOException
  {
    throw new IOException("Strange I/O stream, returned 0 bytes on read");
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.io.BaseReader
 * JD-Core Version:    0.6.2
 */