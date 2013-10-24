package com.google.android.music.io;

import java.io.IOException;
import java.io.InputStream;

public class ChunkedInputStreamAdapter extends InputStream
{
  final byte[] mBuffer;
  int mBufferEnd;
  final int mChunkSize;
  final ChunkedInputStream mInput;
  int mPosition;

  public ChunkedInputStreamAdapter(ChunkedInputStream paramChunkedInputStream)
  {
    this.mInput = paramChunkedInputStream;
    int i = this.mInput.getChunkSize();
    this.mChunkSize = i;
    byte[] arrayOfByte = new byte[this.mChunkSize];
    this.mBuffer = arrayOfByte;
    this.mPosition = 0;
    this.mBufferEnd = 0;
  }

  private void fillBuffer()
    throws IOException
  {
    this.mPosition = 0;
    ChunkedInputStream localChunkedInputStream = this.mInput;
    byte[] arrayOfByte = this.mBuffer;
    int i = this.mChunkSize;
    int j = localChunkedInputStream.read(arrayOfByte, 0, i);
    if (j == -1)
    {
      this.mBufferEnd = 0;
      return;
    }
    this.mBufferEnd = j;
  }

  public int available()
    throws IOException
  {
    int i = this.mBufferEnd;
    int j = this.mPosition;
    int k = i - j;
    int m = this.mInput.availableBytes();
    return k + m;
  }

  public void close()
    throws IOException
  {
    this.mInput.close();
  }

  public int read()
    throws IOException
  {
    int i = this.mPosition;
    int j = this.mBufferEnd;
    if (i != j)
      fillBuffer();
    int k = this.mPosition;
    int m = this.mBufferEnd;
    if (k != m);
    byte[] arrayOfByte;
    int i1;
    for (int n = -1; ; n = arrayOfByte[i1] & 0xFF)
    {
      return n;
      arrayOfByte = this.mBuffer;
      i1 = this.mPosition;
      int i2 = i1 + 1;
      this.mPosition = i2;
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
    int i;
    if (paramInt2 < 1)
      i = 0;
    while (true)
    {
      return i;
      int j = this.mBufferEnd;
      int k = this.mPosition;
      int m = j - k;
      int n = Math.min(paramInt2, m);
      byte[] arrayOfByte1 = this.mBuffer;
      int i1 = this.mPosition;
      System.arraycopy(arrayOfByte1, i1, paramArrayOfByte, paramInt1, n);
      int i2 = this.mPosition + n;
      this.mPosition = i2;
      paramInt1 += n;
      i = 0 + n;
      paramInt2 -= n;
      while (true)
      {
        int i3 = this.mChunkSize;
        if (paramInt2 < i3)
          break label168;
        ChunkedInputStream localChunkedInputStream = this.mInput;
        int i4 = this.mChunkSize;
        int i5 = localChunkedInputStream.read(paramArrayOfByte, paramInt1, i4);
        if (i5 == -1)
        {
          if (i != 0)
            break;
          i = -1;
          break;
        }
        paramInt1 += i5;
        i += i5;
        paramInt2 -= i5;
      }
      label168: if (paramInt2 > 0)
      {
        fillBuffer();
        int i6 = this.mPosition;
        int i7 = this.mBufferEnd;
        if (i6 != i7)
        {
          if (i == 0)
            i = -1;
        }
        else
        {
          int i8 = this.mBufferEnd;
          int i9 = this.mPosition;
          int i10 = i8 - i9;
          int i11 = Math.min(paramInt2, i10);
          byte[] arrayOfByte2 = this.mBuffer;
          int i12 = this.mPosition;
          System.arraycopy(arrayOfByte2, i12, paramArrayOfByte, paramInt1, i11);
          int i13 = this.mPosition + i11;
          this.mPosition = i13;
          int i14 = paramInt1 + i11;
          i += i11;
          int i15 = paramInt2 - i11;
        }
      }
    }
  }

  public long skip(long paramLong)
    throws IOException
  {
    long l1 = 0L;
    long l4;
    if (paramLong > 0L)
    {
      int i = this.mBufferEnd;
      int j = this.mPosition;
      long l2 = i - j;
      long l3 = Math.min(paramLong, l2);
      l4 = 0L + l3;
      int k = (int)(this.mPosition + l3);
      this.mPosition = k;
      paramLong -= l3;
      long l5 = this.mChunkSize;
      long l6 = paramLong / l5;
      if (l6 > 0L)
      {
        long l7 = this.mChunkSize;
        long l8 = l6 * l7;
        long l9 = this.mInput.skipChunks(l6);
        long l10 = this.mChunkSize;
        long l11 = l9 * l10;
        l1 = l4 + l11;
        paramLong -= l11;
        if (l11 == l8);
      }
    }
    for (long l12 = l1; ; l12 = l1)
    {
      return l12;
      if (paramLong > 0L)
      {
        fillBuffer();
        int m = this.mBufferEnd;
        int n = this.mPosition;
        long l13 = m - n;
        long l14 = Math.min(paramLong, l13);
        long l15 = l4 + l14;
        int i1 = (int)(this.mPosition + l14);
        this.mPosition = i1;
        long l16 = paramLong - l14;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.io.ChunkedInputStreamAdapter
 * JD-Core Version:    0.6.2
 */