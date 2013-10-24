package com.google.android.music.download.cp;

import com.google.android.music.io.ChunkedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CpOutputStream
  implements ChunkedOutputStream
{
  private final byte[] mBuffer;
  private final Cipher mCipher;
  private final byte[] mIvBuffer;
  private final SecretKeySpec mSecretKeySpec;
  private final OutputStream mSink;
  private boolean mWroteShortBlock;

  public CpOutputStream(OutputStream paramOutputStream, byte[] paramArrayOfByte)
    throws IOException
  {
    if (paramArrayOfByte.length != 16)
      throw new IllegalArgumentException("secretKey length must be 16");
    this.mSink = paramOutputStream;
    Cipher localCipher = CpUtils.getCipher();
    this.mCipher = localCipher;
    SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte, "AES");
    this.mSecretKeySpec = localSecretKeySpec;
    byte[] arrayOfByte1 = new byte[1024];
    this.mBuffer = arrayOfByte1;
    byte[] arrayOfByte2 = new byte[16];
    this.mIvBuffer = arrayOfByte2;
    SecureRandom localSecureRandom = CpUtils.getRandom();
    byte[] arrayOfByte3 = this.mIvBuffer;
    localSecureRandom.nextBytes(arrayOfByte3);
    CpUtils.writeMagicNumber(paramOutputStream);
  }

  private void updateIv()
  {
    int i = 0;
    while (true)
    {
      if (i >= 16)
        return;
      byte[] arrayOfByte = this.mIvBuffer;
      int j = (byte)(arrayOfByte[i] + 1);
      arrayOfByte[i] = j;
      if (j != null)
        return;
      i += 1;
    }
  }

  public void close()
    throws IOException
  {
    this.mSink.close();
  }

  public void flush()
    throws IOException
  {
    this.mSink.flush();
  }

  public int getChunkSize()
  {
    return 1008;
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (this.mWroteShortBlock)
      throw new IllegalStateException("Already wrote short block.");
    if (paramInt2 > 0)
    {
      int i = getChunkSize();
      if (paramInt2 <= i);
    }
    else
    {
      String str = "length out of range 0 < length <= chunkSize: " + paramInt2;
      throw new IllegalArgumentException(str);
    }
    byte[] arrayOfByte1 = this.mIvBuffer;
    IvParameterSpec localIvParameterSpec = new IvParameterSpec(arrayOfByte1);
    try
    {
      Cipher localCipher1 = this.mCipher;
      SecretKeySpec localSecretKeySpec = this.mSecretKeySpec;
      localCipher1.init(1, localSecretKeySpec, localIvParameterSpec);
      byte[] arrayOfByte2 = this.mIvBuffer;
      byte[] arrayOfByte3 = this.mBuffer;
      int j = this.mIvBuffer.length;
      System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 0, j);
      Cipher localCipher2 = this.mCipher;
      byte[] arrayOfByte4 = this.mBuffer;
      byte[] arrayOfByte5 = paramArrayOfByte;
      int k = paramInt1;
      int m = paramInt2;
      if (localCipher2.doFinal(arrayOfByte5, k, m, arrayOfByte4, 16) != paramInt2)
        throw new IOException("Bad update length");
    }
    catch (Exception localException)
    {
      throw new IOException("Problem with cipher", localException);
    }
    OutputStream localOutputStream = this.mSink;
    byte[] arrayOfByte6 = this.mBuffer;
    int n = paramInt2 + 16;
    localOutputStream.write(arrayOfByte6, 0, n);
    int i1 = getChunkSize();
    if (paramInt2 < i1);
    for (boolean bool = true; ; bool = false)
    {
      this.mWroteShortBlock = bool;
      updateIv();
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cp.CpOutputStream
 * JD-Core Version:    0.6.2
 */