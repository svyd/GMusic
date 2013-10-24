package org.codehaus.jackson.io;

import java.io.IOException;
import java.io.InputStream;

public final class MergedStream extends InputStream
{
  byte[] _buffer;
  protected final IOContext _context;
  final int _end;
  final InputStream _in;
  int _ptr;

  public MergedStream(IOContext paramIOContext, InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this._context = paramIOContext;
    this._in = paramInputStream;
    this._buffer = paramArrayOfByte;
    this._ptr = paramInt1;
    this._end = paramInt2;
  }

  private void freeMergedBuffer()
  {
    byte[] arrayOfByte = this._buffer;
    if (arrayOfByte == null)
      return;
    this._buffer = null;
    this._context.releaseReadIOBuffer(arrayOfByte);
  }

  public int available()
    throws IOException
  {
    int i;
    int j;
    if (this._buffer != null)
    {
      i = this._end;
      j = this._ptr;
    }
    for (int k = i - j; ; k = this._in.available())
      return k;
  }

  public void close()
    throws IOException
  {
    freeMergedBuffer();
    this._in.close();
  }

  public void mark(int paramInt)
  {
    if (this._buffer != null)
      return;
    this._in.mark(paramInt);
  }

  public boolean markSupported()
  {
    if ((this._buffer == null) && (this._in.markSupported()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public int read()
    throws IOException
  {
    int k;
    if (this._buffer != null)
    {
      byte[] arrayOfByte = this._buffer;
      int i = this._ptr;
      int j = i + 1;
      this._ptr = j;
      k = arrayOfByte[i] & 0xFF;
      int m = this._ptr;
      int n = this._end;
      if (m >= n)
        freeMergedBuffer();
    }
    while (true)
    {
      return k;
      k = this._in.read();
    }
  }

  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    int i = paramArrayOfByte.length;
    return read(paramArrayOfByte, 0, i);
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (this._buffer != null)
    {
      int i = this._end;
      int j = this._ptr;
      int k = i - j;
      if (paramInt2 > k)
        paramInt2 = k;
      byte[] arrayOfByte = this._buffer;
      int m = this._ptr;
      System.arraycopy(arrayOfByte, m, paramArrayOfByte, paramInt1, paramInt2);
      int n = this._ptr + paramInt2;
      this._ptr = n;
      int i1 = this._ptr;
      int i2 = this._end;
      if (i1 >= i2)
        freeMergedBuffer();
    }
    for (int i3 = paramInt2; ; i3 = this._in.read(paramArrayOfByte, paramInt1, paramInt2))
      return i3;
  }

  public void reset()
    throws IOException
  {
    if (this._buffer != null)
      return;
    this._in.reset();
  }

  public long skip(long paramLong)
    throws IOException
  {
    long l1 = 0L;
    int k;
    if (this._buffer != null)
    {
      int i = this._end;
      int j = this._ptr;
      k = i - j;
      if (k > paramLong)
      {
        int m = this._ptr;
        int n = (int)paramLong;
        int i1 = m + n;
        this._ptr = i1;
      }
    }
    for (long l2 = paramLong; ; l2 = l1)
    {
      return l2;
      freeMergedBuffer();
      long l3 = k;
      l1 = 0L + l3;
      long l4 = k;
      paramLong -= l4;
      if (paramLong > 0L)
      {
        long l5 = this._in.skip(paramLong);
        l1 += l5;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.io.MergedStream
 * JD-Core Version:    0.6.2
 */