package org.codehaus.jackson.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public final class UTF8Writer extends Writer
{
  protected final IOContext mContext;
  OutputStream mOut;
  byte[] mOutBuffer;
  final int mOutBufferLast;
  int mOutPtr;
  int mSurrogate = 0;

  public UTF8Writer(IOContext paramIOContext, OutputStream paramOutputStream)
  {
    this.mContext = paramIOContext;
    this.mOut = paramOutputStream;
    byte[] arrayOfByte = paramIOContext.allocWriteIOBuffer();
    this.mOutBuffer = arrayOfByte;
    int i = this.mOutBuffer.length + -4;
    this.mOutBufferLast = i;
    this.mOutPtr = 0;
  }

  private int convertSurrogate(int paramInt)
    throws IOException
  {
    int i = this.mSurrogate;
    this.mSurrogate = 0;
    if ((paramInt < 56320) || (paramInt > 57343))
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("Broken surrogate pair: first char 0x");
      String str1 = Integer.toHexString(i);
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(", second 0x");
      String str2 = Integer.toHexString(paramInt);
      String str3 = str2 + "; illegal combination";
      throw new IOException(str3);
    }
    int j = i - 55296 << 10;
    int k = 65536 + j;
    int m = paramInt - 56320;
    return k + m;
  }

  private void throwIllegal(int paramInt)
    throws IOException
  {
    if (paramInt > 1114111)
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("Illegal character point (0x");
      String str1 = Integer.toHexString(paramInt);
      String str2 = str1 + ") to output; max is 0x10FFFF as per RFC 4627";
      throw new IOException(str2);
    }
    if (paramInt >= 55296)
    {
      if (paramInt <= 56319)
      {
        StringBuilder localStringBuilder2 = new StringBuilder().append("Unmatched first part of surrogate pair (0x");
        String str3 = Integer.toHexString(paramInt);
        String str4 = str3 + ")";
        throw new IOException(str4);
      }
      StringBuilder localStringBuilder3 = new StringBuilder().append("Unmatched second part of surrogate pair (0x");
      String str5 = Integer.toHexString(paramInt);
      String str6 = str5 + ")";
      throw new IOException(str6);
    }
    StringBuilder localStringBuilder4 = new StringBuilder().append("Illegal character point (0x");
    String str7 = Integer.toHexString(paramInt);
    String str8 = str7 + ") to output";
    throw new IOException(str8);
  }

  public Writer append(char paramChar)
    throws IOException
  {
    write(paramChar);
    return this;
  }

  public void close()
    throws IOException
  {
    if (this.mOut == null)
      return;
    if (this.mOutPtr > 0)
    {
      OutputStream localOutputStream1 = this.mOut;
      byte[] arrayOfByte1 = this.mOutBuffer;
      int i = this.mOutPtr;
      localOutputStream1.write(arrayOfByte1, 0, i);
      this.mOutPtr = 0;
    }
    OutputStream localOutputStream2 = this.mOut;
    this.mOut = null;
    byte[] arrayOfByte2 = this.mOutBuffer;
    if (arrayOfByte2 != null)
    {
      this.mOutBuffer = null;
      this.mContext.releaseWriteIOBuffer(arrayOfByte2);
    }
    localOutputStream2.close();
    int j = this.mSurrogate;
    this.mSurrogate = 0;
    if (j <= 0)
      return;
    throwIllegal(j);
  }

  public void flush()
    throws IOException
  {
    if (this.mOutPtr > 0)
    {
      OutputStream localOutputStream = this.mOut;
      byte[] arrayOfByte = this.mOutBuffer;
      int i = this.mOutPtr;
      localOutputStream.write(arrayOfByte, 0, i);
      this.mOutPtr = 0;
    }
    this.mOut.flush();
  }

  public void write(int paramInt)
    throws IOException
  {
    if (this.mSurrogate > 0)
      paramInt = convertSurrogate(paramInt);
    while ((paramInt < 55296) || (paramInt > 57343))
    {
      int i = this.mOutPtr;
      int j = this.mOutBufferLast;
      if (i >= j)
      {
        OutputStream localOutputStream = this.mOut;
        byte[] arrayOfByte1 = this.mOutBuffer;
        int k = this.mOutPtr;
        localOutputStream.write(arrayOfByte1, 0, k);
        this.mOutPtr = 0;
      }
      if (paramInt >= 128)
        break;
      byte[] arrayOfByte2 = this.mOutBuffer;
      int m = this.mOutPtr;
      int n = m + 1;
      this.mOutPtr = n;
      int i1 = (byte)paramInt;
      arrayOfByte2[m] = i1;
      return;
    }
    if (paramInt > 56319)
      throwIllegal(paramInt);
    this.mSurrogate = paramInt;
    return;
    int i2 = this.mOutPtr;
    int i5;
    if (paramInt < 2048)
    {
      byte[] arrayOfByte3 = this.mOutBuffer;
      int i3 = i2 + 1;
      int i4 = (byte)(paramInt >> 6 | 0xC0);
      arrayOfByte3[i2] = i4;
      byte[] arrayOfByte4 = this.mOutBuffer;
      i5 = i3 + 1;
      int i6 = (byte)(paramInt & 0x3F | 0x80);
      arrayOfByte4[i3] = i6;
    }
    while (true)
    {
      this.mOutPtr = i5;
      return;
      if (paramInt <= 65535)
      {
        byte[] arrayOfByte5 = this.mOutBuffer;
        int i7 = i2 + 1;
        int i8 = (byte)(paramInt >> 12 | 0xE0);
        arrayOfByte5[i2] = i8;
        byte[] arrayOfByte6 = this.mOutBuffer;
        int i9 = i7 + 1;
        int i10 = (byte)(paramInt >> 6 & 0x3F | 0x80);
        arrayOfByte6[i7] = i10;
        byte[] arrayOfByte7 = this.mOutBuffer;
        int i11 = i9 + 1;
        int i12 = (byte)(paramInt & 0x3F | 0x80);
        arrayOfByte7[i9] = i12;
        i5 = i11;
      }
      else
      {
        if (paramInt > 1114111)
          throwIllegal(paramInt);
        byte[] arrayOfByte8 = this.mOutBuffer;
        int i13 = i2 + 1;
        int i14 = (byte)(paramInt >> 18 | 0xF0);
        arrayOfByte8[i2] = i14;
        byte[] arrayOfByte9 = this.mOutBuffer;
        int i15 = i13 + 1;
        int i16 = (byte)(paramInt >> 12 & 0x3F | 0x80);
        arrayOfByte9[i13] = i16;
        byte[] arrayOfByte10 = this.mOutBuffer;
        int i17 = i15 + 1;
        int i18 = (byte)(paramInt >> 6 & 0x3F | 0x80);
        arrayOfByte10[i15] = i18;
        byte[] arrayOfByte11 = this.mOutBuffer;
        i5 = i17 + 1;
        int i19 = (byte)(paramInt & 0x3F | 0x80);
        arrayOfByte11[i17] = i19;
      }
    }
  }

  public void write(String paramString)
    throws IOException
  {
    int i = paramString.length();
    write(paramString, 0, i);
  }

  public void write(String paramString, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 < 2)
    {
      if (paramInt2 != 1)
        return;
      int i = paramString.charAt(paramInt1);
      write(i);
      return;
    }
    if (this.mSurrogate > 0)
    {
      int j = paramInt1 + 1;
      int k = paramString.charAt(paramInt1);
      paramInt2 += -1;
      int m = convertSurrogate(k);
      write(m);
      paramInt1 = j;
    }
    int n = this.mOutPtr;
    byte[] arrayOfByte = this.mOutBuffer;
    int i1 = this.mOutBufferLast;
    paramInt2 += paramInt1;
    int i2 = paramInt1;
    int i3;
    int i4;
    while (true)
      if (i2 < paramInt2)
      {
        if (n >= i1)
          this.mOut.write(arrayOfByte, 0, n);
        paramInt1 = i2 + 1;
        i3 = paramString.charAt(i2);
        if (i3 < 128)
        {
          i4 = n + 1;
          int i5 = (byte)i3;
          arrayOfByte[n] = i5;
          int i6 = paramInt2 - paramInt1;
          int i7 = i1 - i4;
          if (i6 > i7)
            i6 = i7;
          int i8 = i6 + paramInt1;
          for (i2 = paramInt1; ; i2 = paramInt1)
          {
            if (i2 >= i8)
            {
              int i9 = i4;
              break;
            }
            paramInt1 = i2 + 1;
            i3 = paramString.charAt(i2);
            if (i3 >= 128)
            {
              i2 = paramInt1;
              if (i3 >= 2048)
                break label324;
              int i10 = i4 + 1;
              int i11 = (byte)(i3 >> 6 | 0xC0);
              arrayOfByte[i4] = i11;
              int i12 = i10 + 1;
              int i13 = (byte)(i3 & 0x3F | 0x80);
              arrayOfByte[i10] = i13;
              int i14 = i12;
              paramInt1 = i2;
              label290: i2 = paramInt1;
              break;
            }
            int i15 = i4 + 1;
            int i16 = (byte)i3;
            arrayOfByte[i4] = i16;
            i4 = i15;
          }
          label324: if ((i3 < 55296) || (i3 > 57343))
          {
            int i17 = i4 + 1;
            int i18 = (byte)(i3 >> 12 | 0xE0);
            arrayOfByte[i4] = i18;
            int i19 = i17 + 1;
            int i20 = (byte)(i3 >> 6 & 0x3F | 0x80);
            arrayOfByte[i17] = i20;
            int i21 = i19 + 1;
            int i22 = (byte)(i3 & 0x3F | 0x80);
            arrayOfByte[i19] = i22;
            continue;
          }
          if (i3 > 56319)
          {
            this.mOutPtr = i4;
            throwIllegal(i3);
          }
          this.mSurrogate = i3;
          if (i2 >= paramInt2)
          {
            n = i4;
            int i23 = i2;
          }
        }
      }
    while (true)
    {
      this.mOutPtr = n;
      return;
      paramInt1 = i2 + 1;
      int i24 = paramString.charAt(i2);
      i3 = convertSurrogate(i24);
      if (i3 > 1114111)
      {
        this.mOutPtr = i4;
        throwIllegal(i3);
      }
      int i25 = i4 + 1;
      int i26 = (byte)(i3 >> 18 | 0xF0);
      arrayOfByte[i4] = i26;
      int i27 = i25 + 1;
      int i28 = (byte)(i3 >> 12 & 0x3F | 0x80);
      arrayOfByte[i25] = i28;
      int i29 = i27 + 1;
      int i30 = (byte)(i3 >> 6 & 0x3F | 0x80);
      arrayOfByte[i27] = i30;
      int i31 = i29 + 1;
      int i32 = (byte)(i3 & 0x3F | 0x80);
      arrayOfByte[i29] = i32;
      int i33 = i31;
      break label290;
      i4 = n;
      i2 = paramInt1;
      break;
      int i34 = i2;
    }
  }

  public void write(char[] paramArrayOfChar)
    throws IOException
  {
    int i = paramArrayOfChar.length;
    write(paramArrayOfChar, 0, i);
  }

  public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 < 2)
    {
      if (paramInt2 != 1)
        return;
      int i = paramArrayOfChar[paramInt1];
      write(i);
      return;
    }
    if (this.mSurrogate > 0)
    {
      int j = paramInt1 + 1;
      int k = paramArrayOfChar[paramInt1];
      paramInt2 += -1;
      int m = convertSurrogate(k);
      write(m);
      paramInt1 = j;
    }
    int n = this.mOutPtr;
    byte[] arrayOfByte = this.mOutBuffer;
    int i1 = this.mOutBufferLast;
    paramInt2 += paramInt1;
    int i2 = paramInt1;
    int i3;
    int i4;
    while (true)
      if (i2 < paramInt2)
      {
        if (n >= i1)
          this.mOut.write(arrayOfByte, 0, n);
        paramInt1 = i2 + 1;
        i3 = paramArrayOfChar[i2];
        if (i3 < 128)
        {
          i4 = n + 1;
          int i5 = (byte)i3;
          arrayOfByte[n] = i5;
          int i6 = paramInt2 - paramInt1;
          int i7 = i1 - i4;
          if (i6 > i7)
            i6 = i7;
          int i8 = i6 + paramInt1;
          for (i2 = paramInt1; ; i2 = paramInt1)
          {
            if (i2 >= i8)
            {
              int i9 = i4;
              break;
            }
            paramInt1 = i2 + 1;
            i3 = paramArrayOfChar[i2];
            if (i3 >= 128)
            {
              i2 = paramInt1;
              if (i3 >= 2048)
                break label316;
              int i10 = i4 + 1;
              int i11 = (byte)(i3 >> 6 | 0xC0);
              arrayOfByte[i4] = i11;
              int i12 = i10 + 1;
              int i13 = (byte)(i3 & 0x3F | 0x80);
              arrayOfByte[i10] = i13;
              int i14 = i12;
              paramInt1 = i2;
              label282: i2 = paramInt1;
              break;
            }
            int i15 = i4 + 1;
            int i16 = (byte)i3;
            arrayOfByte[i4] = i16;
            i4 = i15;
          }
          label316: if ((i3 < 55296) || (i3 > 57343))
          {
            int i17 = i4 + 1;
            int i18 = (byte)(i3 >> 12 | 0xE0);
            arrayOfByte[i4] = i18;
            int i19 = i17 + 1;
            int i20 = (byte)(i3 >> 6 & 0x3F | 0x80);
            arrayOfByte[i17] = i20;
            int i21 = i19 + 1;
            int i22 = (byte)(i3 & 0x3F | 0x80);
            arrayOfByte[i19] = i22;
            continue;
          }
          if (i3 > 56319)
          {
            this.mOutPtr = i4;
            throwIllegal(i3);
          }
          this.mSurrogate = i3;
          if (i2 >= paramInt2)
          {
            n = i4;
            int i23 = i2;
          }
        }
      }
    while (true)
    {
      this.mOutPtr = n;
      return;
      paramInt1 = i2 + 1;
      int i24 = paramArrayOfChar[i2];
      i3 = convertSurrogate(i24);
      if (i3 > 1114111)
      {
        this.mOutPtr = i4;
        throwIllegal(i3);
      }
      int i25 = i4 + 1;
      int i26 = (byte)(i3 >> 18 | 0xF0);
      arrayOfByte[i4] = i26;
      int i27 = i25 + 1;
      int i28 = (byte)(i3 >> 12 & 0x3F | 0x80);
      arrayOfByte[i25] = i28;
      int i29 = i27 + 1;
      int i30 = (byte)(i3 >> 6 & 0x3F | 0x80);
      arrayOfByte[i27] = i30;
      int i31 = i29 + 1;
      int i32 = (byte)(i3 & 0x3F | 0x80);
      arrayOfByte[i29] = i32;
      int i33 = i31;
      break label282;
      i4 = n;
      i2 = paramInt1;
      break;
      int i34 = i2;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.io.UTF8Writer
 * JD-Core Version:    0.6.2
 */