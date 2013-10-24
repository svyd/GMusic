package com.google.android.music.download.stream;

import java.io.Closeable;
import java.io.IOException;

public abstract interface StreamingInput extends Closeable
{
  public abstract int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.stream.StreamingInput
 * JD-Core Version:    0.6.2
 */