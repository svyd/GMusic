package org.codehaus.jackson.impl;

import java.io.ByteArrayInputStream;
import java.io.CharConversionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.MergedStream;
import org.codehaus.jackson.io.UTF32Reader;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.codehaus.jackson.sym.CharsToNameCanonicalizer;

public final class ByteSourceBootstrapper
{
  boolean _bigEndian = true;
  private final boolean _bufferRecyclable;
  int _bytesPerChar = 0;
  final IOContext _context;
  final InputStream _in;
  final byte[] _inputBuffer;
  private int _inputEnd;
  protected int _inputProcessed;
  private int _inputPtr;

  public ByteSourceBootstrapper(IOContext paramIOContext, InputStream paramInputStream)
  {
    this._context = paramIOContext;
    this._in = paramInputStream;
    byte[] arrayOfByte = paramIOContext.allocReadIOBuffer();
    this._inputBuffer = arrayOfByte;
    this._inputPtr = 0;
    this._inputEnd = 0;
    this._inputProcessed = 0;
    this._bufferRecyclable = true;
  }

  public ByteSourceBootstrapper(IOContext paramIOContext, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this._context = paramIOContext;
    this._in = null;
    this._inputBuffer = paramArrayOfByte;
    this._inputPtr = paramInt1;
    int i = paramInt1 + paramInt2;
    this._inputEnd = i;
    int j = -paramInt1;
    this._inputProcessed = j;
    this._bufferRecyclable = false;
  }

  private boolean checkUTF16(int paramInt)
  {
    boolean bool = false;
    if ((0xFF00 & paramInt) == 0);
    for (this._bigEndian = true; ; this._bigEndian = false)
    {
      this._bytesPerChar = 2;
      bool = true;
      do
        return bool;
      while ((paramInt & 0xFF) != 0);
    }
  }

  private boolean checkUTF32(int paramInt)
    throws IOException
  {
    boolean bool = false;
    if (paramInt >> 8 == 0)
      this._bigEndian = true;
    while (true)
    {
      this._bytesPerChar = 4;
      bool = true;
      do
      {
        return bool;
        if ((0xFFFFFF & paramInt) == 0)
        {
          this._bigEndian = false;
          break;
        }
        if ((0xFF00FFFF & paramInt) == 0)
        {
          reportWeirdUCS4("3412");
          break;
        }
      }
      while ((0xFFFF00FF & paramInt) != 0);
      reportWeirdUCS4("2143");
    }
  }

  private boolean handleBOM(int paramInt)
    throws IOException
  {
    boolean bool = true;
    int i;
    switch (paramInt)
    {
    default:
      i = paramInt >>> 16;
      if (i == 65279)
      {
        int j = this._inputPtr + 2;
        this._inputPtr = j;
        this._bytesPerChar = 2;
        this._bigEndian = true;
      }
      break;
    case 65279:
    case -131072:
    case 65534:
    case -16842752:
    }
    while (true)
    {
      return bool;
      this._bigEndian = true;
      int k = this._inputPtr + 4;
      this._inputPtr = k;
      this._bytesPerChar = 4;
      continue;
      int m = this._inputPtr + 4;
      this._inputPtr = m;
      this._bytesPerChar = 4;
      this._bigEndian = false;
      continue;
      reportWeirdUCS4("2143");
      reportWeirdUCS4("3412");
      break;
      if (i == 65534)
      {
        int n = this._inputPtr + 2;
        this._inputPtr = n;
        this._bytesPerChar = 2;
        this._bigEndian = false;
      }
      else if (paramInt >>> 8 == 15711167)
      {
        int i1 = this._inputPtr + 3;
        this._inputPtr = i1;
        this._bytesPerChar = 1;
        this._bigEndian = true;
      }
      else
      {
        bool = false;
      }
    }
  }

  private void reportWeirdUCS4(String paramString)
    throws IOException
  {
    String str = "Unsupported UCS-4 endianness (" + paramString + ") detected";
    throw new CharConversionException(str);
  }

  public JsonParser constructParser(int paramInt, ObjectCodec paramObjectCodec, BytesToNameCanonicalizer paramBytesToNameCanonicalizer, CharsToNameCanonicalizer paramCharsToNameCanonicalizer)
    throws IOException, JsonParseException
  {
    JsonEncoding localJsonEncoding1 = detectEncoding();
    boolean bool1 = JsonParser.Feature.INTERN_FIELD_NAMES.enabledIn(paramInt);
    JsonEncoding localJsonEncoding2 = JsonEncoding.UTF8;
    IOContext localIOContext1;
    InputStream localInputStream;
    BytesToNameCanonicalizer localBytesToNameCanonicalizer;
    byte[] arrayOfByte;
    int i;
    int j;
    boolean bool2;
    int k;
    ObjectCodec localObjectCodec1;
    if (localJsonEncoding1 == localJsonEncoding2)
    {
      localIOContext1 = this._context;
      localInputStream = this._in;
      localBytesToNameCanonicalizer = paramBytesToNameCanonicalizer.makeChild(bool1);
      arrayOfByte = this._inputBuffer;
      i = this._inputPtr;
      j = this._inputEnd;
      bool2 = this._bufferRecyclable;
      k = paramInt;
      localObjectCodec1 = paramObjectCodec;
    }
    IOContext localIOContext2;
    Reader localReader;
    CharsToNameCanonicalizer localCharsToNameCanonicalizer;
    int m;
    ObjectCodec localObjectCodec2;
    for (Object localObject = new Utf8StreamParser(localIOContext1, k, localInputStream, localObjectCodec1, localBytesToNameCanonicalizer, arrayOfByte, i, j, bool2); ; localObject = new ReaderBasedParser(localIOContext2, m, localReader, localObjectCodec2, localCharsToNameCanonicalizer))
    {
      return localObject;
      localIOContext2 = this._context;
      localReader = constructReader();
      localCharsToNameCanonicalizer = paramCharsToNameCanonicalizer.makeChild(bool1);
      m = paramInt;
      localObjectCodec2 = paramObjectCodec;
    }
  }

  public Reader constructReader()
    throws IOException
  {
    JsonEncoding localJsonEncoding = this._context.getEncoding();
    int[] arrayOfInt = 1.$SwitchMap$org$codehaus$jackson$JsonEncoding;
    int i = localJsonEncoding.ordinal();
    Object localObject1;
    InputStream localInputStream2;
    Object localObject2;
    switch (arrayOfInt[i])
    {
    default:
      throw new RuntimeException("Internal error");
    case 1:
    case 2:
      IOContext localIOContext1 = this._context;
      InputStream localInputStream1 = this._in;
      byte[] arrayOfByte1 = this._inputBuffer;
      int j = this._inputPtr;
      int k = this._inputEnd;
      boolean bool = this._context.getEncoding().isBigEndian();
      localObject1 = new UTF32Reader(localIOContext1, localInputStream1, arrayOfByte1, j, k, bool);
      return localObject1;
    case 3:
    case 4:
      localInputStream2 = this._in;
      if (localInputStream2 == null)
      {
        byte[] arrayOfByte2 = this._inputBuffer;
        int m = this._inputPtr;
        int n = this._inputEnd;
        localObject2 = new ByteArrayInputStream(arrayOfByte2, m, n);
      }
      break;
    case 5:
    }
    while (true)
    {
      String str = localJsonEncoding.getJavaName();
      localObject1 = new InputStreamReader((InputStream)localObject2, str);
      break;
      int i1 = this._inputPtr;
      int i2 = this._inputEnd;
      if (i1 < i2)
      {
        IOContext localIOContext2 = this._context;
        byte[] arrayOfByte3 = this._inputBuffer;
        int i3 = this._inputPtr;
        int i4 = this._inputEnd;
        localObject2 = new MergedStream(localIOContext2, localInputStream2, arrayOfByte3, i3, i4);
        continue;
        throw new RuntimeException("Internal error: should be using Utf8StreamParser directly");
      }
      else
      {
        localObject2 = localInputStream2;
      }
    }
  }

  public JsonEncoding detectEncoding()
    throws IOException, JsonParseException
  {
    int i = 0;
    int i7;
    if (ensureLoaded(4))
    {
      byte[] arrayOfByte1 = this._inputBuffer;
      int j = this._inputPtr;
      int k = arrayOfByte1[j] << 24;
      byte[] arrayOfByte2 = this._inputBuffer;
      int m = this._inputPtr + 1;
      int n = (arrayOfByte2[m] & 0xFF) << 16;
      int i1 = k | n;
      byte[] arrayOfByte3 = this._inputBuffer;
      int i2 = this._inputPtr + 2;
      int i3 = (arrayOfByte3[i2] & 0xFF) << 8;
      int i4 = i1 | i3;
      byte[] arrayOfByte4 = this._inputBuffer;
      int i5 = this._inputPtr + 3;
      int i6 = arrayOfByte4[i5] & 0xFF;
      i7 = i4 | i6;
      if (handleBOM(i7))
        i = 1;
    }
    JsonEncoding localJsonEncoding;
    while (i == 0)
    {
      localJsonEncoding = JsonEncoding.UTF8;
      this._context.setEncoding(localJsonEncoding);
      return localJsonEncoding;
      if (checkUTF32(i7))
      {
        i = 1;
      }
      else
      {
        int i8 = i7 >>> 16;
        if (checkUTF16(i8))
        {
          i = 1;
          continue;
          if (ensureLoaded(2))
          {
            byte[] arrayOfByte5 = this._inputBuffer;
            int i9 = this._inputPtr;
            int i10 = (arrayOfByte5[i9] & 0xFF) << 8;
            byte[] arrayOfByte6 = this._inputBuffer;
            int i11 = this._inputPtr + 1;
            int i12 = arrayOfByte6[i11] & 0xFF;
            int i13 = i10 | i12;
            if (checkUTF16(i13))
              i = 1;
          }
        }
      }
    }
    if (this._bytesPerChar == 2)
    {
      if (this._bigEndian);
      for (localJsonEncoding = JsonEncoding.UTF16_BE; ; localJsonEncoding = JsonEncoding.UTF16_LE)
        break;
    }
    if (this._bytesPerChar == 4)
    {
      if (this._bigEndian);
      for (localJsonEncoding = JsonEncoding.UTF32_BE; ; localJsonEncoding = JsonEncoding.UTF32_LE)
        break;
    }
    throw new RuntimeException("Internal error");
  }

  protected boolean ensureLoaded(int paramInt)
    throws IOException
  {
    boolean bool = true;
    int i = this._inputEnd;
    int j = this._inputPtr;
    int k = i - j;
    while (true)
    {
      if (k < paramInt)
        if (this._in != null)
          break label46;
      label46: InputStream localInputStream;
      byte[] arrayOfByte;
      int n;
      int i3;
      for (int m = -1; m < 1; m = localInputStream.read(arrayOfByte, n, i3))
      {
        bool = false;
        return bool;
        localInputStream = this._in;
        arrayOfByte = this._inputBuffer;
        n = this._inputEnd;
        int i1 = this._inputBuffer.length;
        int i2 = this._inputEnd;
        i3 = i1 - i2;
      }
      int i4 = this._inputEnd + m;
      this._inputEnd = i4;
      k += m;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.ByteSourceBootstrapper
 * JD-Core Version:    0.6.2
 */