package com.google.android.music.download.cp;

import com.google.android.music.utils.ByteUtils;
import com.google.common.io.ByteStreams;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class CpUtils
{
  static final byte[] MAGIC_NUMBER = { 18, 211, 21, 39 };

  static Cipher getCipher()
  {
    try
    {
      Cipher localCipher = Cipher.getInstance("AES/CTR/NoPadding");
      return localCipher;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new IllegalStateException("Required content protection algorithm is not present: AES/CTR/NoPadding", localNoSuchAlgorithmException);
    }
    catch (NoSuchPaddingException localNoSuchPaddingException)
    {
      throw new IllegalStateException("Required padding is not supported: AES/CTR/NoPadding", localNoSuchPaddingException);
    }
  }

  public static long getDecryptedSize(long paramLong)
  {
    if (paramLong < 4L);
    long l3;
    long l6;
    for (long l1 = 0L; ; l1 = 1008L * l3 + l6)
    {
      return l1;
      long l2 = paramLong - 4L;
      l3 = l2 / 1024L;
      long l4 = 1024L * l3;
      long l5 = l2 - l4 - 16L;
      l6 = Math.max(0L, l5);
    }
  }

  public static long getEncryptedSize(long paramLong)
  {
    long l1 = paramLong / 1008L;
    long l2 = 1008L * l1;
    long l3 = paramLong - l2;
    long l4 = 1024L * l1;
    long l5 = 4L + l4;
    if (l3 > 0L)
    {
      long l6 = 16L + l3;
      l5 += l6;
    }
    return l5;
  }

  public static SecureRandom getRandom()
  {
    try
    {
      SecureRandom localSecureRandom = SecureRandom.getInstance("SHA1PRNG");
      return localSecureRandom;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new IllegalStateException("Required SHA1PRNG algorithm is not present", localNoSuchAlgorithmException);
    }
  }

  public static byte[] newCpData()
  {
    try
    {
      KeyGenerator localKeyGenerator = KeyGenerator.getInstance("AES");
      SecureRandom localSecureRandom = SecureRandom.getInstance("SHA1PRNG");
      localKeyGenerator.init(128, localSecureRandom);
      byte[] arrayOfByte = localKeyGenerator.generateKey().getEncoded();
      return arrayOfByte;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new IllegalStateException("Required content protection algorithm is not present", localNoSuchAlgorithmException);
    }
  }

  static void readAndCheckMagicNumber(InputStream paramInputStream)
    throws IOException, UnrecognizedDataCpException
  {
    byte[] arrayOfByte1 = new byte[4];
    try
    {
      ByteStreams.readFully(paramInputStream, arrayOfByte1);
      byte[] arrayOfByte2 = MAGIC_NUMBER;
      if (ByteUtils.bytesEqual(arrayOfByte1, 0, arrayOfByte2, 0, 4))
        return;
      throw new UnrecognizedDataCpException("Magic number is not found");
    }
    catch (EOFException localEOFException)
    {
      throw new UnrecognizedDataCpException("Magic number is not found. Input is too short");
    }
  }

  static void writeMagicNumber(OutputStream paramOutputStream)
    throws IOException
  {
    byte[] arrayOfByte = MAGIC_NUMBER;
    paramOutputStream.write(arrayOfByte);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cp.CpUtils
 * JD-Core Version:    0.6.2
 */