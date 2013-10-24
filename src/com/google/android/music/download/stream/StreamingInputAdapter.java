package com.google.android.music.download.stream;

import java.io.IOException;
import java.io.InputStream;

class StreamingInputAdapter
  implements StreamingInput
{
  private final InputStream mStream;

  StreamingInputAdapter(InputStream paramInputStream)
  {
    this.mStream = paramInputStream;
  }

  public void close()
    throws IOException
  {
    this.mStream.close();
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    return this.mStream.read(paramArrayOfByte, paramInt1, paramInt2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.stream.StreamingInputAdapter
 * JD-Core Version:    0.6.2
 */