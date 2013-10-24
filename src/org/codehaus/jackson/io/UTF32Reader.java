package org.codehaus.jackson.io;

import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;

public final class UTF32Reader extends BaseReader
{
  final boolean mBigEndian;
  int mByteCount = 0;
  int mCharCount = 0;
  char mSurrogate = '\000';

  public UTF32Reader(IOContext paramIOContext, InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    super(paramIOContext, paramInputStream, paramArrayOfByte, paramInt1, paramInt2);
    this.mBigEndian = paramBoolean;
  }

  private boolean loadMore(int paramInt)
    throws IOException
  {
    boolean bool = false;
    int i = this.mByteCount;
    int j = this.mLength - paramInt;
    int k = i + j;
    this.mByteCount = k;
    if (paramInt > 0)
    {
      if (this.mPtr > 0)
      {
        int m = 0;
        while (m < paramInt)
        {
          byte[] arrayOfByte1 = this.mBuffer;
          byte[] arrayOfByte2 = this.mBuffer;
          int n = this.mPtr + m;
          int i1 = arrayOfByte2[n];
          arrayOfByte1[m] = i1;
          m += 1;
        }
        this.mPtr = 0;
      }
      int i8;
      for (this.mLength = paramInt; this.mLength < 4; this.mLength = i8)
      {
        InputStream localInputStream1 = this.mIn;
        byte[] arrayOfByte3 = this.mBuffer;
        int i2 = this.mLength;
        int i3 = this.mBuffer.length;
        int i4 = this.mLength;
        int i5 = i3 - i4;
        i6 = localInputStream1.read(arrayOfByte3, i2, i5);
        if (i6 < 1)
        {
          if (i6 < 0)
          {
            freeBuffers();
            int i7 = this.mLength;
            reportUnexpectedEOF(i7, 4);
          }
          reportStrangeStream();
        }
        i8 = this.mLength + i6;
      }
    }
    this.mPtr = 0;
    InputStream localInputStream2 = this.mIn;
    byte[] arrayOfByte4 = this.mBuffer;
    int i6 = localInputStream2.read(arrayOfByte4);
    if (i6 < 1)
    {
      this.mLength = 0;
      if (i6 < 0)
        freeBuffers();
    }
    while (true)
    {
      return bool;
      reportStrangeStream();
      this.mLength = i6;
      break;
      bool = true;
    }
  }

  private void reportInvalid(int paramInt1, int paramInt2, String paramString)
    throws IOException
  {
    int i = this.mByteCount;
    int j = this.mPtr;
    int k = i + j + -1;
    int m = this.mCharCount + paramInt2;
    StringBuilder localStringBuilder = new StringBuilder().append("Invalid UTF-32 character 0x");
    String str1 = Integer.toHexString(paramInt1);
    String str2 = str1 + paramString + " at char #" + m + ", byte #" + k + ")";
    throw new CharConversionException(str2);
  }

  private void reportUnexpectedEOF(int paramInt1, int paramInt2)
    throws IOException
  {
    int i = this.mByteCount + paramInt1;
    int j = this.mCharCount;
    String str = "Unexpected EOF in the middle of a 4-byte UTF-32 char: got " + paramInt1 + ", needed " + paramInt2 + ", at char #" + j + ", byte #" + i + ")";
    throw new CharConversionException(str);
  }

  public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = -1;
    if (this.mBuffer == null);
    while (true)
    {
      return i;
      if (paramInt2 >= 1)
        break;
      int j = paramInt2;
    }
    if (paramInt1 >= 0)
    {
      int k = paramInt1 + paramInt2;
      int m = paramArrayOfChar.length;
      if (k <= m);
    }
    else
    {
      reportBounds(paramArrayOfChar, paramInt1, paramInt2);
    }
    paramInt2 += paramInt1;
    int n = paramInt1;
    int i1;
    label93: int i3;
    int i13;
    if (this.mSurrogate != null)
    {
      i1 = n + 1;
      int i2 = this.mSurrogate;
      paramArrayOfChar[n] = i2;
      i = 0;
      this.mSurrogate = i;
      if (i1 >= paramInt2)
        break label595;
      i3 = this.mPtr;
      if (!this.mBigEndian)
        break label437;
      int i4 = this.mBuffer[i3] << 24;
      byte[] arrayOfByte1 = this.mBuffer;
      int i5 = i3 + 1;
      int i6 = (arrayOfByte1[i5] & 0xFF) << 16;
      int i7 = i4 | i6;
      byte[] arrayOfByte2 = this.mBuffer;
      int i8 = i3 + 2;
      int i9 = (arrayOfByte2[i8] & 0xFF) << 8;
      int i10 = i7 | i9;
      byte[] arrayOfByte3 = this.mBuffer;
      int i11 = i3 + 3;
      int i12 = arrayOfByte3[i11] & 0xFF;
      i13 = i10 | i12;
      label220: int i14 = this.mPtr + 4;
      this.mPtr = i14;
      if (i13 <= 65535)
        break label548;
      if (i13 > 1114111)
      {
        int i15 = i1 - paramInt1;
        StringBuilder localStringBuilder = new StringBuilder().append("(above ");
        String str1 = Integer.toHexString(1114111);
        String str2 = str1 + ") ";
        reportInvalid(i13, i15, str2);
      }
      int i16 = i13 - 65536;
      n = i1 + 1;
      int i17 = i16 >> 10;
      int i18 = (char)(55296 + i17);
      paramArrayOfChar[i1] = i18;
      int i19 = i16 & 0x3FF;
      i13 = 0xDC00 | i19;
      if (n < paramInt2)
        break label552;
      i = (char)i13;
      this.mSurrogate = i;
    }
    while (true)
    {
      int i20 = n - paramInt1;
      int i21 = this.mCharCount + i20;
      this.mCharCount = i21;
      int i22 = i20;
      break;
      int i23 = this.mLength;
      int i24 = this.mPtr;
      int i25 = i23 - i24;
      if ((i25 < 4) && (!loadMore(i25)))
        break;
      i1 = n;
      break label93;
      label437: int i26 = this.mBuffer[i3] & 0xFF;
      byte[] arrayOfByte4 = this.mBuffer;
      int i27 = i3 + 1;
      int i28 = (arrayOfByte4[i27] & 0xFF) << 8;
      int i29 = i26 | i28;
      byte[] arrayOfByte5 = this.mBuffer;
      int i30 = i3 + 2;
      int i31 = (arrayOfByte5[i30] & 0xFF) << 16;
      int i32 = i29 | i31;
      byte[] arrayOfByte6 = this.mBuffer;
      int i33 = i3 + 3;
      int i34 = arrayOfByte6[i33] << 24;
      i13 = i32 | i34;
      break label220;
      label548: n = i1;
      label552: i1 = n + 1;
      int i35 = (char)i13;
      paramArrayOfChar[n] = i35;
      i = this.mPtr;
      int i36 = this.mLength;
      if (i < i36)
        break label93;
      n = i1;
      continue;
      label595: n = i1;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.io.UTF32Reader
 * JD-Core Version:    0.6.2
 */