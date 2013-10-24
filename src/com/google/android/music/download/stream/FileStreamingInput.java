package com.google.android.music.download.stream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

class FileStreamingInput
  implements StreamingInput
{
  private final RandomAccessFile mFile;

  FileStreamingInput(RandomAccessFile paramRandomAccessFile)
    throws FileNotFoundException
  {
    this.mFile = paramRandomAccessFile;
  }

  public void close()
    throws IOException
  {
    this.mFile.close();
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    return this.mFile.read(paramArrayOfByte, paramInt1, paramInt2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.stream.FileStreamingInput
 * JD-Core Version:    0.6.2
 */