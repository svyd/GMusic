package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.Writer;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.PrettyPrinter;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.NumberOutput;
import org.codehaus.jackson.util.CharTypes;

public final class WriterBasedGenerator extends JsonGeneratorBase
{
  static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
  protected char[] _entityBuffer;
  protected final IOContext _ioContext;
  protected char[] _outputBuffer;
  protected int _outputEnd;
  protected int _outputHead = 0;
  protected int _outputTail = 0;
  protected final Writer _writer;

  public WriterBasedGenerator(IOContext paramIOContext, int paramInt, ObjectCodec paramObjectCodec, Writer paramWriter)
  {
    super(paramInt, paramObjectCodec);
    this._ioContext = paramIOContext;
    this._writer = paramWriter;
    char[] arrayOfChar = paramIOContext.allocConcatBuffer();
    this._outputBuffer = arrayOfChar;
    int i = this._outputBuffer.length;
    this._outputEnd = i;
  }

  private void _appendSingleEscape(int paramInt1, char[] paramArrayOfChar, int paramInt2)
  {
    if (paramInt1 < 0)
    {
      int i = -(paramInt1 + 1);
      paramArrayOfChar[paramInt2] = '\\';
      int j = paramInt2 + 1;
      paramArrayOfChar[j] = 'u';
      int k = j + 1;
      paramArrayOfChar[k] = '0';
      int m = k + 1;
      paramArrayOfChar[m] = '0';
      int n = m + 1;
      char[] arrayOfChar1 = HEX_CHARS;
      int i1 = i >> 4;
      int i2 = arrayOfChar1[i1];
      paramArrayOfChar[n] = i2;
      int i3 = n + 1;
      char[] arrayOfChar2 = HEX_CHARS;
      int i4 = i & 0xF;
      int i5 = arrayOfChar2[i4];
      paramArrayOfChar[i3] = i5;
      return;
    }
    paramArrayOfChar[paramInt2] = '\\';
    int i6 = paramInt2 + 1;
    int i7 = (char)paramInt1;
    paramArrayOfChar[i6] = i7;
  }

  private void _writeLongString(String paramString)
    throws IOException, JsonGenerationException
  {
    _flushBuffer();
    int i = paramString.length();
    int j = 0;
    int k = this._outputEnd;
    if (j + k > i);
    for (int m = i - j; ; m = k)
    {
      int n = j + m;
      char[] arrayOfChar = this._outputBuffer;
      paramString.getChars(j, n, arrayOfChar, 0);
      _writeSegment(m);
      j += m;
      if (j < i)
        break;
      return;
    }
  }

  private final void _writeNull()
    throws IOException
  {
    int i = this._outputTail + 4;
    int j = this._outputEnd;
    if (i >= j)
      _flushBuffer();
    int k = this._outputTail;
    char[] arrayOfChar = this._outputBuffer;
    arrayOfChar[k] = 'n';
    int m = k + 1;
    arrayOfChar[m] = 'u';
    int n = m + 1;
    arrayOfChar[n] = 'l';
    int i1 = n + 1;
    arrayOfChar[i1] = 'l';
    int i2 = i1 + 1;
    this._outputTail = i2;
  }

  private final void _writeQuotedInt(int paramInt)
    throws IOException
  {
    int i = this._outputTail + 13;
    int j = this._outputEnd;
    if (i >= j)
      _flushBuffer();
    char[] arrayOfChar1 = this._outputBuffer;
    int k = this._outputTail;
    int m = k + 1;
    this._outputTail = m;
    arrayOfChar1[k] = '"';
    char[] arrayOfChar2 = this._outputBuffer;
    int n = this._outputTail;
    int i1 = NumberOutput.outputInt(paramInt, arrayOfChar2, n);
    this._outputTail = i1;
    char[] arrayOfChar3 = this._outputBuffer;
    int i2 = this._outputTail;
    int i3 = i2 + 1;
    this._outputTail = i3;
    arrayOfChar3[i2] = '"';
  }

  private final void _writeSegment(int paramInt)
    throws IOException, JsonGenerationException
  {
    int[] arrayOfInt = CharTypes.getOutputEscapes();
    int i = arrayOfInt.length;
    int j = 0;
    while (true)
    {
      if (j >= paramInt)
        return;
      int k = j;
      int m = this._outputBuffer[j];
      if ((m < i) && (arrayOfInt[m] != 0));
      while (true)
      {
        int n = j - k;
        if (n <= 0)
          break label100;
        Writer localWriter = this._writer;
        char[] arrayOfChar1 = this._outputBuffer;
        localWriter.write(arrayOfChar1, k, n);
        if (j < paramInt)
          break label100;
        return;
        j += 1;
        if (j < paramInt)
          break;
      }
      label100: int i1 = this._outputBuffer[j];
      int i2 = arrayOfInt[i1];
      j += 1;
      if (i2 < 0);
      for (int i3 = 6; ; i3 = 2)
      {
        int i4 = this._outputTail;
        if (i3 <= i4)
          break label158;
        _writeSingleEscape(i2);
        break;
      }
      label158: j -= i3;
      char[] arrayOfChar2 = this._outputBuffer;
      _appendSingleEscape(i2, arrayOfChar2, j);
    }
  }

  private void _writeSingleEscape(int paramInt)
    throws IOException
  {
    char[] arrayOfChar1 = this._entityBuffer;
    if (arrayOfChar1 == null)
    {
      arrayOfChar1 = new char[6];
      arrayOfChar1[0] = '\\';
      arrayOfChar1[2] = '0';
      arrayOfChar1[3] = '0';
    }
    if (paramInt < 0)
    {
      int i = -(paramInt + 1);
      arrayOfChar1[1] = 'u';
      char[] arrayOfChar2 = HEX_CHARS;
      int j = i >> 4;
      int k = arrayOfChar2[j];
      arrayOfChar1[4] = k;
      char[] arrayOfChar3 = HEX_CHARS;
      int m = i & 0xF;
      int n = arrayOfChar3[m];
      arrayOfChar1[5] = n;
      this._writer.write(arrayOfChar1, 0, 6);
      return;
    }
    int i1 = (char)paramInt;
    arrayOfChar1[1] = i1;
    this._writer.write(arrayOfChar1, 0, 2);
  }

  private void _writeString(String paramString)
    throws IOException, JsonGenerationException
  {
    int i = paramString.length();
    int j = this._outputEnd;
    if (i > j)
    {
      _writeLongString(paramString);
      return;
    }
    int k = this._outputTail + i;
    int m = this._outputEnd;
    if (k > m)
      _flushBuffer();
    char[] arrayOfChar1 = this._outputBuffer;
    int n = this._outputTail;
    paramString.getChars(0, i, arrayOfChar1, n);
    int i1 = this._outputTail + i;
    int[] arrayOfInt = CharTypes.getOutputEscapes();
    int i2 = arrayOfInt.length;
    while (true)
    {
      if (this._outputTail >= i1)
        return;
      label96: char[] arrayOfChar2 = this._outputBuffer;
      int i3 = this._outputTail;
      int i4 = arrayOfChar2[i3];
      int i11;
      if ((i4 < i2) && (arrayOfInt[i4] != 0))
      {
        int i5 = this._outputTail;
        int i6 = this._outputHead;
        int i7 = i5 - i6;
        if (i7 > 0)
        {
          Writer localWriter = this._writer;
          char[] arrayOfChar3 = this._outputBuffer;
          int i8 = this._outputHead;
          localWriter.write(arrayOfChar3, i8, i7);
        }
        char[] arrayOfChar4 = this._outputBuffer;
        int i9 = this._outputTail;
        int i10 = arrayOfChar4[i9];
        i11 = arrayOfInt[i10];
        int i12 = this._outputTail + 1;
        this._outputTail = i12;
        if (i11 >= 0)
          break label288;
      }
      label288: for (int i13 = 6; ; i13 = 2)
      {
        int i14 = this._outputTail;
        if (i13 <= i14)
          break label294;
        int i15 = this._outputTail;
        this._outputHead = i15;
        _writeSingleEscape(i11);
        break;
        int i16 = this._outputTail + 1;
        this._outputTail = i16;
        if (i16 < i1)
          break label96;
        return;
      }
      label294: int i17 = this._outputTail - i13;
      this._outputHead = i17;
      char[] arrayOfChar5 = this._outputBuffer;
      _appendSingleEscape(i11, arrayOfChar5, i17);
    }
  }

  private void writeRawLong(String paramString)
    throws IOException, JsonGenerationException
  {
    int i = this._outputEnd;
    int j = this._outputTail;
    int k = i - j;
    char[] arrayOfChar1 = this._outputBuffer;
    int m = this._outputTail;
    paramString.getChars(0, k, arrayOfChar1, m);
    int n = this._outputTail + k;
    this._outputTail = n;
    _flushBuffer();
    int i1 = k;
    int i2 = paramString.length() - k;
    while (true)
    {
      int i3 = this._outputEnd;
      if (i2 <= i3)
        break;
      int i4 = this._outputEnd;
      int i5 = i1 + i4;
      char[] arrayOfChar2 = this._outputBuffer;
      paramString.getChars(i1, i5, arrayOfChar2, 0);
      this._outputHead = 0;
      this._outputTail = i4;
      _flushBuffer();
      i1 += i4;
      i2 -= i4;
    }
    int i6 = i1 + i2;
    char[] arrayOfChar3 = this._outputBuffer;
    paramString.getChars(i1, i6, arrayOfChar3, 0);
    this._outputHead = 0;
    this._outputTail = i2;
  }

  protected final void _flushBuffer()
    throws IOException
  {
    int i = this._outputTail;
    int j = this._outputHead;
    int k = i - j;
    if (k <= 0)
      return;
    int m = this._outputHead;
    this._outputHead = 0;
    this._outputTail = 0;
    Writer localWriter = this._writer;
    char[] arrayOfChar = this._outputBuffer;
    localWriter.write(arrayOfChar, m, k);
  }

  protected void _releaseBuffers()
  {
    char[] arrayOfChar = this._outputBuffer;
    if (arrayOfChar == null)
      return;
    this._outputBuffer = null;
    this._ioContext.releaseConcatBuffer(arrayOfChar);
  }

  protected final void _verifyPrettyValueWrite(String paramString, int paramInt)
    throws IOException, JsonGenerationException
  {
    switch (paramInt)
    {
    default:
      _cantHappen();
      return;
    case 1:
      this._cfgPrettyPrinter.writeArrayValueSeparator(this);
      return;
    case 2:
      this._cfgPrettyPrinter.writeObjectFieldValueSeparator(this);
      return;
    case 3:
      this._cfgPrettyPrinter.writeRootValueSeparator(this);
      return;
    case 0:
    }
    if (this._writeContext.inArray())
    {
      this._cfgPrettyPrinter.beforeArrayValues(this);
      return;
    }
    if (!this._writeContext.inObject())
      return;
    this._cfgPrettyPrinter.beforeObjectEntries(this);
  }

  protected final void _verifyValueWrite(String paramString)
    throws IOException, JsonGenerationException
  {
    int i = this._writeContext.writeValue();
    if (i == 5)
    {
      String str = "Can not " + paramString + ", expecting field name";
      _reportError(str);
    }
    if (this._cfgPrettyPrinter == null)
    {
      int j;
      switch (i)
      {
      default:
        return;
      case 1:
        j = 44;
      case 2:
      case 3:
      }
      while (true)
      {
        int k = this._outputTail;
        int m = this._outputEnd;
        if (k >= m)
          _flushBuffer();
        char[] arrayOfChar = this._outputBuffer;
        int n = this._outputTail;
        arrayOfChar[n] = j;
        int i1 = this._outputTail + 1;
        this._outputTail = i1;
        return;
        j = 58;
        continue;
        j = 32;
      }
    }
    _verifyPrettyValueWrite(paramString, i);
  }

  protected void _writeEndArray()
    throws IOException, JsonGenerationException
  {
    int i = this._outputTail;
    int j = this._outputEnd;
    if (i >= j)
      _flushBuffer();
    char[] arrayOfChar = this._outputBuffer;
    int k = this._outputTail;
    int m = k + 1;
    this._outputTail = m;
    arrayOfChar[k] = ']';
  }

  protected void _writeEndObject()
    throws IOException, JsonGenerationException
  {
    int i = this._outputTail;
    int j = this._outputEnd;
    if (i >= j)
      _flushBuffer();
    char[] arrayOfChar = this._outputBuffer;
    int k = this._outputTail;
    int m = k + 1;
    this._outputTail = m;
    arrayOfChar[k] = '}';
  }

  protected void _writeFieldName(String paramString, boolean paramBoolean)
    throws IOException, JsonGenerationException
  {
    if (this._cfgPrettyPrinter != null)
    {
      _writePPFieldName(paramString, paramBoolean);
      return;
    }
    int i = this._outputTail + 1;
    int j = this._outputEnd;
    if (i >= j)
      _flushBuffer();
    if (paramBoolean)
    {
      char[] arrayOfChar1 = this._outputBuffer;
      int k = this._outputTail;
      int m = k + 1;
      this._outputTail = m;
      arrayOfChar1[k] = ',';
    }
    JsonGenerator.Feature localFeature = JsonGenerator.Feature.QUOTE_FIELD_NAMES;
    if (!isEnabled(localFeature))
    {
      _writeString(paramString);
      return;
    }
    char[] arrayOfChar2 = this._outputBuffer;
    int n = this._outputTail;
    int i1 = n + 1;
    this._outputTail = i1;
    arrayOfChar2[n] = '"';
    _writeString(paramString);
    int i2 = this._outputTail;
    int i3 = this._outputEnd;
    if (i2 >= i3)
      _flushBuffer();
    char[] arrayOfChar3 = this._outputBuffer;
    int i4 = this._outputTail;
    int i5 = i4 + 1;
    this._outputTail = i5;
    arrayOfChar3[i4] = '"';
  }

  protected final void _writePPFieldName(String paramString, boolean paramBoolean)
    throws IOException, JsonGenerationException
  {
    if (paramBoolean)
      this._cfgPrettyPrinter.writeObjectEntrySeparator(this);
    while (true)
    {
      JsonGenerator.Feature localFeature = JsonGenerator.Feature.QUOTE_FIELD_NAMES;
      if (!isEnabled(localFeature))
        break;
      int i = this._outputTail;
      int j = this._outputEnd;
      if (i >= j)
        _flushBuffer();
      char[] arrayOfChar1 = this._outputBuffer;
      int k = this._outputTail;
      int m = k + 1;
      this._outputTail = m;
      arrayOfChar1[k] = '"';
      _writeString(paramString);
      int n = this._outputTail;
      int i1 = this._outputEnd;
      if (n >= i1)
        _flushBuffer();
      char[] arrayOfChar2 = this._outputBuffer;
      int i2 = this._outputTail;
      int i3 = i2 + 1;
      this._outputTail = i3;
      arrayOfChar2[i2] = '"';
      return;
      this._cfgPrettyPrinter.beforeObjectEntries(this);
    }
    _writeString(paramString);
  }

  protected void _writeStartArray()
    throws IOException, JsonGenerationException
  {
    int i = this._outputTail;
    int j = this._outputEnd;
    if (i >= j)
      _flushBuffer();
    char[] arrayOfChar = this._outputBuffer;
    int k = this._outputTail;
    int m = k + 1;
    this._outputTail = m;
    arrayOfChar[k] = '[';
  }

  protected void _writeStartObject()
    throws IOException, JsonGenerationException
  {
    int i = this._outputTail;
    int j = this._outputEnd;
    if (i >= j)
      _flushBuffer();
    char[] arrayOfChar = this._outputBuffer;
    int k = this._outputTail;
    int m = k + 1;
    this._outputTail = m;
    arrayOfChar[k] = '{';
  }

  public void close()
    throws IOException
  {
    super.close();
    if (this._outputBuffer != null)
    {
      JsonGenerator.Feature localFeature1 = JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT;
      if (isEnabled(localFeature1))
        while (true)
        {
          JsonWriteContext localJsonWriteContext = getOutputContext();
          if (localJsonWriteContext.inArray())
          {
            writeEndArray();
          }
          else
          {
            if (!localJsonWriteContext.inObject())
              break;
            writeEndObject();
          }
        }
    }
    _flushBuffer();
    if (!this._ioContext.isResourceManaged())
    {
      JsonGenerator.Feature localFeature2 = JsonGenerator.Feature.AUTO_CLOSE_TARGET;
      if (!isEnabled(localFeature2));
    }
    else
    {
      this._writer.close();
    }
    while (true)
    {
      _releaseBuffers();
      return;
      this._writer.flush();
    }
  }

  public void writeBoolean(boolean paramBoolean)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write boolean value");
    int i = this._outputTail + 5;
    int j = this._outputEnd;
    if (i >= j)
      _flushBuffer();
    int k = this._outputTail;
    char[] arrayOfChar = this._outputBuffer;
    int i1;
    if (paramBoolean)
    {
      arrayOfChar[k] = 't';
      int m = k + 1;
      arrayOfChar[m] = 'r';
      int n = m + 1;
      arrayOfChar[n] = 'u';
      i1 = n + 1;
      arrayOfChar[i1] = 'e';
    }
    while (true)
    {
      int i2 = i1 + 1;
      this._outputTail = i2;
      return;
      arrayOfChar[k] = 'f';
      int i3 = k + 1;
      arrayOfChar[i3] = 'a';
      int i4 = i3 + 1;
      arrayOfChar[i4] = 'l';
      int i5 = i4 + 1;
      arrayOfChar[i5] = 's';
      i1 = i5 + 1;
      arrayOfChar[i1] = 'e';
    }
  }

  public void writeNull()
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write null value");
    _writeNull();
  }

  public void writeNumber(float paramFloat)
    throws IOException, JsonGenerationException
  {
    if (!this._cfgNumbersAsStrings)
    {
      if ((Float.isNaN(paramFloat)) || (Float.isInfinite(paramFloat)))
      {
        JsonGenerator.Feature localFeature = JsonGenerator.Feature.QUOTE_NON_NUMERIC_NUMBERS;
        if (!isEnabled(localFeature));
      }
    }
    else
    {
      String str1 = String.valueOf(paramFloat);
      writeString(str1);
      return;
    }
    _verifyValueWrite("write number");
    String str2 = String.valueOf(paramFloat);
    writeRaw(str2);
  }

  public void writeNumber(int paramInt)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write number");
    int i = this._outputTail + 11;
    int j = this._outputEnd;
    if (i >= j)
      _flushBuffer();
    if (this._cfgNumbersAsStrings)
    {
      _writeQuotedInt(paramInt);
      return;
    }
    char[] arrayOfChar = this._outputBuffer;
    int k = this._outputTail;
    int m = NumberOutput.outputInt(paramInt, arrayOfChar, k);
    this._outputTail = m;
  }

  public void writeRaw(String paramString)
    throws IOException, JsonGenerationException
  {
    int i = paramString.length();
    int j = this._outputEnd;
    int k = this._outputTail;
    int m = j - k;
    if (m == 0)
    {
      _flushBuffer();
      int n = this._outputEnd;
      int i1 = this._outputTail;
      m = n - i1;
    }
    if (m >= i)
    {
      char[] arrayOfChar = this._outputBuffer;
      int i2 = this._outputTail;
      paramString.getChars(0, i, arrayOfChar, i2);
      int i3 = this._outputTail + i;
      this._outputTail = i3;
      return;
    }
    writeRawLong(paramString);
  }

  public void writeString(String paramString)
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("write text value");
    if (paramString == null)
    {
      _writeNull();
      return;
    }
    int i = this._outputTail;
    int j = this._outputEnd;
    if (i >= j)
      _flushBuffer();
    char[] arrayOfChar1 = this._outputBuffer;
    int k = this._outputTail;
    int m = k + 1;
    this._outputTail = m;
    arrayOfChar1[k] = '"';
    _writeString(paramString);
    int n = this._outputTail;
    int i1 = this._outputEnd;
    if (n >= i1)
      _flushBuffer();
    char[] arrayOfChar2 = this._outputBuffer;
    int i2 = this._outputTail;
    int i3 = i2 + 1;
    this._outputTail = i3;
    arrayOfChar2[i2] = '"';
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.WriterBasedGenerator
 * JD-Core Version:    0.6.2
 */