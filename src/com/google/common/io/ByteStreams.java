package com.google.common.io;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class ByteStreams
{
  public static long copy(InputStream paramInputStream, OutputStream paramOutputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[4096];
    long l2;
    for (long l1 = 0L; ; l1 += l2)
    {
      int i = paramInputStream.read(arrayOfByte);
      if (i == -1)
        return l1;
      paramOutputStream.write(arrayOfByte, 0, i);
      l2 = i;
    }
  }

  public static int read(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 < 0)
      throw new IndexOutOfBoundsException("len is negative");
    int i = 0;
    while (true)
    {
      int m;
      if (i < paramInt2)
      {
        int j = paramInt1 + i;
        int k = paramInt2 - i;
        m = paramInputStream.read(paramArrayOfByte, j, k);
        if (m != -1);
      }
      else
      {
        return i;
      }
      i += m;
    }
  }

  public static void readFully(InputStream paramInputStream, byte[] paramArrayOfByte)
    throws IOException
  {
    int i = paramArrayOfByte.length;
    readFully(paramInputStream, paramArrayOfByte, 0, i);
  }

  public static void readFully(InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (read(paramInputStream, paramArrayOfByte, paramInt1, paramInt2) != paramInt2)
      return;
    throw new EOFException();
  }

  public static byte[] toByteArray(InputStream paramInputStream)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    long l = copy(paramInputStream, localByteArrayOutputStream);
    return localByteArrayOutputStream.toByteArray();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.ByteStreams
 * JD-Core Version:    0.6.2
 */