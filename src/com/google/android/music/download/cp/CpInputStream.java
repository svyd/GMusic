package com.google.android.music.download.cp;

import com.google.android.music.io.ChunkedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CpInputStream
  implements ChunkedInputStream
{
  private final byte[] mBuffer;
  private final Cipher mCipher;
  private boolean mEOF;
  private final byte[] mIvBuffer;
  private boolean mReadShortBlock;
  private final SecretKeySpec mSecretKeySpec;
  private final InputStream mSource;

  public CpInputStream(InputStream paramInputStream, byte[] paramArrayOfByte)
    throws IOException, UnrecognizedDataCpException
  {
    byte[] arrayOfByte1 = new byte[16];
    this.mIvBuffer = arrayOfByte1;
    if (paramArrayOfByte.length != 16)
      throw new IllegalArgumentException("secretKey length must be 16");
    this.mSource = paramInputStream;
    Cipher localCipher = CpUtils.getCipher();
    this.mCipher = localCipher;
    SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte, "AES");
    this.mSecretKeySpec = localSecretKeySpec;
    byte[] arrayOfByte2 = new byte[1024];
    this.mBuffer = arrayOfByte2;
    CpUtils.readAndCheckMagicNumber(paramInputStream);
  }

  public int availableBytes()
    throws IOException
  {
    return (int)CpUtils.getDecryptedSize(this.mSource.available());
  }

  public void close()
    throws IOException
  {
    this.mSource.close();
  }

  public int getChunkSize()
  {
    return 1008;
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (this.mEOF)
    {
      i = -1;
      return i;
    }
    if (this.mReadShortBlock)
      throw new IllegalStateException("Already read short block.");
    if (paramInt2 > 0)
    {
      int j = getChunkSize();
      if (paramInt2 <= j);
    }
    else
    {
      String str = "length out of range 0 < length <= chunkSize: " + paramInt2;
      throw new IllegalArgumentException(str);
    }
    int k = paramInt2 + 16;
    int m = 0;
    while (true)
    {
      if (k <= 0)
        break label172;
      InputStream localInputStream = this.mSource;
      byte[] arrayOfByte1 = this.mBuffer;
      int n = localInputStream.read(arrayOfByte1, m, k);
      if (n == -1)
      {
        this.mEOF = true;
        if (m == 0)
        {
          i = -1;
          break;
        }
        if (m > 16)
          break label172;
        i = -1;
        break;
      }
      m += n;
      k -= n;
    }
    label172: byte[] arrayOfByte2 = this.mBuffer;
    byte[] arrayOfByte3 = this.mIvBuffer;
    System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 0, 16);
    byte[] arrayOfByte4 = this.mIvBuffer;
    IvParameterSpec localIvParameterSpec = new IvParameterSpec(arrayOfByte4);
    int i = m + -16;
    try
    {
      Cipher localCipher1 = this.mCipher;
      SecretKeySpec localSecretKeySpec = this.mSecretKeySpec;
      localCipher1.init(2, localSecretKeySpec, localIvParameterSpec);
      Cipher localCipher2 = this.mCipher;
      byte[] arrayOfByte5 = this.mBuffer;
      byte[] arrayOfByte6 = paramArrayOfByte;
      int i1 = paramInt1;
      if (localCipher2.doFinal(arrayOfByte5, 16, i, arrayOfByte6, i1) != i)
        throw new IllegalStateException("wrong size decrypted block.");
    }
    catch (Exception localException)
    {
      throw new IOException("Problem with cipher", localException);
    }
    int i2 = getChunkSize();
    if (i < i2);
    for (boolean bool = true; ; bool = false)
    {
      this.mReadShortBlock = bool;
      break;
    }
  }

  public long skipChunks(long paramLong)
    throws IOException
  {
    long l = paramLong * 1024L;
    return this.mSource.skip(l) / 1024L;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cp.CpInputStream
 * JD-Core Version:    0.6.2
 */