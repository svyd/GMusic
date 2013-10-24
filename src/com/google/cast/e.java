package com.google.cast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.UnmappableCharacterException;
import java.nio.charset.UnsupportedCharsetException;

class e
{
  private byte[] a;
  private int b;
  private int c;
  private int d;
  private boolean e;
  private ByteBuffer[] f;
  private ByteBuffer[] g;

  public e(int paramInt)
  {
    if (paramInt < 1)
    {
      String str = "invalid capacity " + paramInt + "; must be > 0";
      throw new IllegalArgumentException(str);
    }
    byte[] arrayOfByte = new byte[paramInt];
    this.a = arrayOfByte;
    ByteBuffer[] arrayOfByteBuffer1 = new ByteBuffer[1];
    this.f = arrayOfByteBuffer1;
    ByteBuffer[] arrayOfByteBuffer2 = new ByteBuffer[2];
    this.g = arrayOfByteBuffer2;
    this.d = -1;
    this.e = true;
  }

  private String a(byte[] paramArrayOfByte, int paramInt1, int paramInt2, CharsetDecoder paramCharsetDecoder)
    throws UnsupportedEncodingException, f
  {
    CharsetDecoder localCharsetDecoder1 = paramCharsetDecoder.reset();
    try
    {
      ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte, paramInt1, paramInt2);
      CodingErrorAction localCodingErrorAction1 = CodingErrorAction.REPORT;
      CharsetDecoder localCharsetDecoder2 = paramCharsetDecoder.onMalformedInput(localCodingErrorAction1);
      CodingErrorAction localCodingErrorAction2 = CodingErrorAction.REPORT;
      CharsetDecoder localCharsetDecoder3 = paramCharsetDecoder.onUnmappableCharacter(localCodingErrorAction2);
      localCharBuffer = paramCharsetDecoder.decode(localByteBuffer);
      if (localByteBuffer.hasRemaining())
        throw new f("unexpected data after end of string");
    }
    catch (IllegalCharsetNameException localIllegalCharsetNameException)
    {
      CharBuffer localCharBuffer;
      String str1 = localIllegalCharsetNameException.toString();
      throw new UnsupportedEncodingException(str1);
      String str2 = localCharBuffer.toString();
      return str2;
    }
    catch (UnsupportedCharsetException localUnsupportedCharsetException)
    {
      String str3 = localUnsupportedCharsetException.toString();
      throw new UnsupportedEncodingException(str3);
    }
    catch (IllegalStateException localIllegalStateException)
    {
      throw new f(localIllegalStateException);
    }
    catch (MalformedInputException localMalformedInputException)
    {
      throw new f(localMalformedInputException);
    }
    catch (UnmappableCharacterException localUnmappableCharacterException)
    {
      throw new f(localUnmappableCharacterException);
    }
    catch (CharacterCodingException localCharacterCodingException)
    {
      throw new f(localCharacterCodingException);
    }
  }

  private void b(byte paramByte)
  {
    byte[] arrayOfByte = this.a;
    int i = this.c;
    arrayOfByte[i] = paramByte;
    d(1);
  }

  private void c(int paramInt)
  {
    int i = this.b + paramInt;
    this.b = i;
    int j = this.b;
    int k = this.a.length;
    if (j >= k)
    {
      int m = this.b;
      int n = this.a.length;
      int i1 = m - n;
      this.b = i1;
    }
    int i2 = this.b;
    int i3 = this.c;
    if (i2 != i3)
      return;
    if (this.d == -1)
    {
      d();
      return;
    }
    this.e = true;
  }

  private void d(int paramInt)
  {
    int i = this.c + paramInt;
    this.c = i;
    int j = this.c;
    int k = this.a.length;
    if (j >= k)
    {
      int m = this.c;
      int n = this.a.length;
      int i1 = m - n;
      this.c = i1;
    }
    this.e = false;
    this.d = -1;
  }

  private int l()
  {
    int i;
    if (this.e)
      i = 0;
    while (true)
    {
      return i;
      int j = this.b;
      int k = this.c;
      if (j < k)
      {
        int m = this.c;
        int n = this.b;
        i = m - n;
      }
      else
      {
        int i1 = this.a.length;
        int i2 = this.b;
        i = i1 - i2;
      }
    }
  }

  private int m()
  {
    int i;
    if (this.e)
      i = this.a.length;
    while (true)
    {
      return i;
      int j = this.c;
      int k = this.b;
      if (j < k)
      {
        int m = this.b;
        int n = this.c;
        i = m - n;
      }
      else
      {
        int i1 = this.a.length;
        int i2 = this.c;
        i = i1 - i2;
      }
    }
  }

  private byte n()
  {
    byte[] arrayOfByte = this.a;
    int i = this.b;
    byte b1 = arrayOfByte[i];
    c(1);
    return b1;
  }

  public int a(SocketChannel paramSocketChannel)
    throws IOException
  {
    int i;
    if (j())
    {
      i = 0;
      return i;
    }
    int j = this.b;
    int k = this.c;
    ByteBuffer[] arrayOfByteBuffer;
    if (j < k)
    {
      arrayOfByteBuffer = this.f;
      byte[] arrayOfByte1 = this.a;
      int m = this.b;
      int n = this.c;
      int i1 = this.b;
      int i2 = n - i1;
      ByteBuffer localByteBuffer1 = ByteBuffer.wrap(arrayOfByte1, m, i2);
      arrayOfByteBuffer[0] = localByteBuffer1;
    }
    while (true)
    {
      i = (int)paramSocketChannel.write(arrayOfByteBuffer);
      if (i <= 0)
        break label188;
      c(i);
      break;
      arrayOfByteBuffer = this.g;
      byte[] arrayOfByte2 = this.a;
      int i3 = this.b;
      int i4 = this.a.length;
      int i5 = this.b;
      int i6 = i4 - i5;
      ByteBuffer localByteBuffer2 = ByteBuffer.wrap(arrayOfByte2, i3, i6);
      arrayOfByteBuffer[0] = localByteBuffer2;
      byte[] arrayOfByte3 = this.a;
      int i7 = this.c;
      ByteBuffer localByteBuffer3 = ByteBuffer.wrap(arrayOfByte3, 0, i7);
      arrayOfByteBuffer[1] = localByteBuffer3;
    }
    label188: throw new ClosedChannelException();
  }

  public int a(byte[] paramArrayOfByte)
  {
    int i = -1;
    int j = i();
    int k = paramArrayOfByte.length;
    if (j < k)
      return i;
    int m = this.b;
    int n = i();
    int i1 = paramArrayOfByte.length + -1;
    int i2 = n - i1;
    int i3 = 0;
    int i4 = m;
    int i5 = 0;
    label56: int i6;
    int i7;
    if (i3 < i2)
    {
      i6 = i5;
      i7 = 0;
      label70: int i8 = paramArrayOfByte.length;
      if (i7 < i8)
      {
        i6 = 1;
        byte[] arrayOfByte = this.a;
        int i9 = i4 + i7;
        int i10 = this.a.length;
        int i11 = i9 % i10;
        int i12 = arrayOfByte[i11];
        int i13 = paramArrayOfByte[i7];
        if (i12 != i13)
          int i14 = 0;
      }
      else
      {
        if (i6 == 0)
          break label170;
        i5 = i6;
      }
    }
    else
    {
      if (i5 == 0)
        break label210;
    }
    label170: label210: for (int i15 = paramArrayOfByte.length + i3; ; i15 = -1)
    {
      i = i15;
      break;
      i7 += 1;
      break label70;
      int i16 = i4 + 1;
      int i17 = this.a.length;
      if (i16 != i17)
        i16 = 0;
      i3 += 1;
      i4 = i16;
      i5 = i6;
      break label56;
    }
  }

  public Byte a()
  {
    if (i() < 1);
    for (Byte localByte = null; ; localByte = Byte.valueOf(n()))
      return localByte;
  }

  public String a(int paramInt, byte[] paramArrayOfByte, CharsetDecoder paramCharsetDecoder)
    throws UnsupportedEncodingException, f
  {
    int i = 0;
    String str;
    if (i() < paramInt)
      str = null;
    while (true)
    {
      return str;
      int j = l();
      if (j >= paramInt)
      {
        if (paramArrayOfByte != null)
        {
          j = this.b;
          while (i < paramInt)
          {
            byte[] arrayOfByte1 = this.a;
            int k = arrayOfByte1[j];
            int m = paramArrayOfByte.length;
            int n = i % m;
            int i1 = paramArrayOfByte[n];
            int i2 = (byte)(k ^ i1);
            arrayOfByte1[j] = i2;
            j += 1;
            i += 1;
          }
        }
        byte[] arrayOfByte2 = this.a;
        int i3 = this.b;
        str = a(arrayOfByte2, i3, paramInt, paramCharsetDecoder);
        c(paramInt);
      }
      else
      {
        byte[] arrayOfByte3 = new byte[paramInt];
        byte[] arrayOfByte4 = this.a;
        int i4 = this.b;
        System.arraycopy(arrayOfByte4, i4, arrayOfByte3, 0, j);
        c(j);
        byte[] arrayOfByte5 = this.a;
        int i5 = this.b;
        int i6 = paramInt - j;
        System.arraycopy(arrayOfByte5, i5, arrayOfByte3, j, i6);
        int i7 = paramInt - j;
        c(i7);
        if (paramArrayOfByte != null)
        {
          int i8 = 0;
          while (i8 < paramInt)
          {
            int i9 = arrayOfByte3[i8];
            int i10 = paramArrayOfByte.length;
            int i11 = i8 % i10;
            int i12 = paramArrayOfByte[i11];
            int i13 = (byte)(i9 ^ i12);
            arrayOfByte3[i8] = i13;
            i8 += 1;
          }
        }
        int i14 = arrayOfByte3.length;
        str = a(arrayOfByte3, 0, i14, paramCharsetDecoder);
      }
    }
  }

  public boolean a(byte paramByte)
  {
    int i = 1;
    if (h() < i)
      i = 0;
    while (true)
    {
      return i;
      b(paramByte);
    }
  }

  public boolean a(int paramInt)
  {
    if (i() < paramInt);
    for (boolean bool = false; ; bool = true)
    {
      return bool;
      c(paramInt);
    }
  }

  public boolean a(long paramLong)
  {
    if (h() < 8);
    for (boolean bool = false; ; bool = true)
    {
      return bool;
      byte b1 = (byte)(int)(paramLong >> 56 & 0xFF);
      b(b1);
      byte b2 = (byte)(int)(paramLong >> 48 & 0xFF);
      b(b2);
      byte b3 = (byte)(int)(paramLong >> 40 & 0xFF);
      b(b3);
      byte b4 = (byte)(int)(paramLong >> 32 & 0xFF);
      b(b4);
      byte b5 = (byte)(int)(paramLong >> 24 & 0xFF);
      b(b5);
      byte b6 = (byte)(int)(paramLong >> 16 & 0xFF);
      b(b6);
      byte b7 = (byte)(int)(paramLong >> 8 & 0xFF);
      b(b7);
      byte b8 = (byte)(int)(paramLong & 0xFF);
      b(b8);
    }
  }

  public boolean a(String paramString, byte[] paramArrayOfByte, Charset paramCharset)
  {
    byte[] arrayOfByte = paramString.getBytes(paramCharset);
    return b(arrayOfByte, paramArrayOfByte);
  }

  public boolean a(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2)
  {
    int i = 0;
    if (i() < paramInt2);
    while (true)
    {
      return i;
      int j = l();
      int k = Math.min(paramInt2, j);
      byte[] arrayOfByte1 = this.a;
      int m = this.b;
      System.arraycopy(arrayOfByte1, m, paramArrayOfByte1, paramInt1, k);
      c(k);
      int n = paramInt2 - k;
      if (n > 0)
      {
        int i1 = k + paramInt1;
        byte[] arrayOfByte2 = this.a;
        int i2 = this.b;
        System.arraycopy(arrayOfByte2, i2, paramArrayOfByte1, i1, n);
        c(n);
      }
      if (paramArrayOfByte2 != null)
        while (i < paramInt2)
        {
          int i3 = paramArrayOfByte1[i];
          int i4 = paramArrayOfByte2.length;
          int i5 = i % i4;
          int i6 = paramArrayOfByte2[i5];
          int i7 = (byte)(i3 ^ i6);
          paramArrayOfByte1[i] = i7;
          i += 1;
        }
      i = 1;
    }
  }

  public boolean a(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    int i = paramArrayOfByte1.length;
    return a(paramArrayOfByte1, 0, i, paramArrayOfByte2);
  }

  public int b(SocketChannel paramSocketChannel)
    throws IOException
  {
    int i;
    if (k())
    {
      i = 0;
      return i;
    }
    int j = this.c;
    int k = this.b;
    ByteBuffer[] arrayOfByteBuffer;
    if (j < k)
    {
      arrayOfByteBuffer = this.f;
      byte[] arrayOfByte1 = this.a;
      int m = this.c;
      int n = this.b;
      int i1 = this.c;
      int i2 = n - i1;
      ByteBuffer localByteBuffer1 = ByteBuffer.wrap(arrayOfByte1, m, i2);
      arrayOfByteBuffer[0] = localByteBuffer1;
    }
    while (true)
    {
      i = (int)paramSocketChannel.read(arrayOfByteBuffer);
      if (i <= 0)
        break label188;
      d(i);
      break;
      arrayOfByteBuffer = this.g;
      byte[] arrayOfByte2 = this.a;
      int i3 = this.c;
      int i4 = this.a.length;
      int i5 = this.c;
      int i6 = i4 - i5;
      ByteBuffer localByteBuffer2 = ByteBuffer.wrap(arrayOfByte2, i3, i6);
      arrayOfByteBuffer[0] = localByteBuffer2;
      byte[] arrayOfByte3 = this.a;
      int i7 = this.b;
      ByteBuffer localByteBuffer3 = ByteBuffer.wrap(arrayOfByte3, 0, i7);
      arrayOfByteBuffer[1] = localByteBuffer3;
    }
    label188: throw new ClosedChannelException();
  }

  public Integer b()
  {
    if (i() < 2);
    int i;
    int j;
    for (Integer localInteger = null; ; localInteger = Integer.valueOf(i | j))
    {
      return localInteger;
      i = (n() & 0xFF) << 8;
      j = n() & 0xFF;
    }
  }

  public boolean b(int paramInt)
  {
    if (h() < 2);
    for (boolean bool = false; ; bool = true)
    {
      return bool;
      byte b1 = (byte)(paramInt >> 8 & 0xFF);
      b(b1);
      byte b2 = (byte)(paramInt & 0xFF);
      b(b2);
    }
  }

  public boolean b(byte[] paramArrayOfByte1, int paramInt1, int paramInt2, byte[] paramArrayOfByte2)
  {
    int i = 0;
    if (h() < paramInt2);
    while (true)
    {
      return i;
      int k = m();
      int m = Math.min(paramInt2, k);
      byte[] arrayOfByte1 = this.a;
      int n = this.c;
      System.arraycopy(paramArrayOfByte1, paramInt1, arrayOfByte1, n, m);
      if (paramArrayOfByte2 != null)
      {
        int i1 = this.c;
        while (i < m)
        {
          byte[] arrayOfByte2 = this.a;
          int i2 = arrayOfByte2[i1];
          int i3 = paramArrayOfByte2.length;
          int i4 = i % i3;
          int i5 = paramArrayOfByte2[i4];
          int i6 = (byte)(i2 ^ i5);
          arrayOfByte2[i1] = i6;
          i1 += 1;
          i += 1;
        }
      }
      d(m);
      int i7 = paramInt1 + m;
      int i8 = paramInt2 - m;
      if (i8 > 0)
      {
        byte[] arrayOfByte3 = this.a;
        int i9 = this.c;
        System.arraycopy(paramArrayOfByte1, i7, arrayOfByte3, i9, i8);
        if (paramArrayOfByte2 != null)
        {
          int i10 = this.c;
          int i11 = j;
          j = i10;
          while (i11 < paramInt2)
          {
            byte[] arrayOfByte4 = this.a;
            int i12 = arrayOfByte4[j];
            int i13 = paramArrayOfByte2.length;
            int i14 = i11 % i13;
            int i15 = paramArrayOfByte2[i14];
            int i16 = (byte)(i12 ^ i15);
            arrayOfByte4[j] = i16;
            j += 1;
            i11 += 1;
          }
        }
        d(i8);
      }
      int j = 1;
    }
  }

  public boolean b(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    int i = paramArrayOfByte1.length;
    return b(paramArrayOfByte1, 0, i, paramArrayOfByte2);
  }

  public Long c()
  {
    if (i() < 8);
    long l13;
    long l14;
    for (Long localLong = null; ; localLong = Long.valueOf(l13 | l14))
    {
      return localLong;
      long l1 = (n() & 0xFF) << 56;
      long l2 = (n() & 0xFF) << 48;
      long l3 = l1 | l2;
      long l4 = (n() & 0xFF) << 40;
      long l5 = l3 | l4;
      long l6 = (n() & 0xFF) << 32;
      long l7 = l5 | l6;
      long l8 = (n() & 0xFF) << 24;
      long l9 = l7 | l8;
      long l10 = (n() & 0xFF) << 16;
      long l11 = l9 | l10;
      long l12 = (n() & 0xFF) << 8;
      l13 = l11 | l12;
      l14 = n() & 0xFF;
    }
  }

  public void d()
  {
    this.c = 0;
    this.b = 0;
    this.d = -1;
    this.e = true;
  }

  public void e()
  {
    int i = this.b;
    this.d = i;
  }

  public void f()
  {
    this.d = -1;
    if (!this.e)
      return;
    this.c = 0;
    this.b = 0;
  }

  public void g()
  {
    if (this.d == -1)
      return;
    int i = this.b;
    int j = this.d;
    if (i != j)
    {
      int k = this.d;
      this.b = k;
      this.e = false;
    }
    this.d = -1;
  }

  public int h()
  {
    int i;
    if (this.e)
      i = this.a.length;
    while (true)
    {
      return i;
      int j = this.c;
      int k = this.b;
      if (j < k)
      {
        int m = this.b;
        int n = this.c;
        i = m - n;
      }
      else
      {
        int i1 = this.a.length;
        int i2 = this.c;
        int i3 = i1 - i2;
        int i4 = this.b;
        i = i3 + i4;
      }
    }
  }

  public int i()
  {
    int i;
    if (this.e)
      i = 0;
    while (true)
    {
      return i;
      int j = this.b;
      int k = this.c;
      if (j < k)
      {
        int m = this.c;
        int n = this.b;
        i = m - n;
      }
      else
      {
        int i1 = this.a.length;
        int i2 = this.b;
        int i3 = i1 - i2;
        int i4 = this.c;
        i = i3 + i4;
      }
    }
  }

  public boolean j()
  {
    return this.e;
  }

  public boolean k()
  {
    if (!this.e)
    {
      int i = this.b;
      int j = this.c;
      if (i == j);
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.e
 * JD-Core Version:    0.6.2
 */