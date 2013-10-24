package com.google.android.music.io;

import java.io.IOException;

public abstract interface ChunkedInputStream
{
  public abstract int availableBytes()
    throws IOException;

  public abstract void close()
    throws IOException;

  public abstract int getChunkSize();

  public abstract int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;

  public abstract long skipChunks(long paramLong)
    throws IOException;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.io.ChunkedInputStream
 * JD-Core Version:    0.6.2
 */