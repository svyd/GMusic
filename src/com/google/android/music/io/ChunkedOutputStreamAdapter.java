package com.google.android.music.io;

import java.io.IOException;
import java.io.OutputStream;

public class ChunkedOutputStreamAdapter extends OutputStream
{
  final byte[] mBuffer;
  final int mChunkSize;
  final ChunkedOutputStream mOutput;
  int mPosition;
  boolean mWroteShort;

  public ChunkedOutputStreamAdapter(ChunkedOutputStream paramChunkedOutputStream)
  {
    this.mOutput = paramChunkedOutputStream;
    int i = this.mOutput.getChunkSize();
    this.mChunkSize = i;
    byte[] arrayOfByte = new byte[this.mChunkSize];
    this.mBuffer = arrayOfByte;
    this.mPosition = 0;
  }

  private void flushBuffer()
    throws IOException
  {
    if (this.mPosition == 0)
      return;
    if (this.mWroteShort)
      throw new IllegalStateException("Can't write anything after a short block has been written.");
    int i = this.mPosition;
    int j = this.mChunkSize;
    if (i < j);
    for (boolean bool = true; ; bool = false)
    {
      this.mWroteShort = bool;
      ChunkedOutputStream localChunkedOutputStream = this.mOutput;
      byte[] arrayOfByte = this.mBuffer;
      int k = this.mPosition;
      localChunkedOutputStream.write(arrayOfByte, 0, k);
      this.mPosition = 0;
      return;
    }
  }

  public void close()
    throws IOException
  {
    flushBuffer();
    this.mOutput.close();
  }

  public void flush()
    throws IOException
  {
    flushBuffer();
    this.mOutput.flush();
  }

  public void write(int paramInt)
    throws IOException
  {
    byte[] arrayOfByte = this.mBuffer;
    int i = this.mPosition;
    int j = i + 1;
    this.mPosition = j;
    int k = (byte)paramInt;
    arrayOfByte[i] = k;
    int m = this.mPosition;
    int n = this.mChunkSize;
    if (m != n)
      return;
    flushBuffer();
  }

  public void write(byte[] paramArrayOfByte)
    throws IOException
  {
    int i = paramArrayOfByte.length;
    write(paramArrayOfByte, 0, i);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 <= 0)
      return;
    int i = this.mChunkSize;
    int j = this.mPosition;
    int k = i - j;
    int m = Math.min(paramInt2, k);
    int n = this.mChunkSize;
    if (m != n)
      this.mOutput.write(paramArrayOfByte, paramInt1, m);
    while (true)
    {
      paramInt1 += m;
      paramInt2 -= m;
      break;
      byte[] arrayOfByte = this.mBuffer;
      int i1 = this.mPosition;
      System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, i1, m);
      int i2 = this.mPosition + m;
      this.mPosition = i2;
      int i3 = this.mPosition;
      int i4 = this.mChunkSize;
      if (i3 != i4)
        flushBuffer();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.io.ChunkedOutputStreamAdapter
 * JD-Core Version:    0.6.2
 */