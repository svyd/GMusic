package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.Reader;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.io.IOContext;

public abstract class ReaderBasedParserBase extends JsonNumericParserBase
{
  protected char[] _inputBuffer;
  protected Reader _reader;

  protected ReaderBasedParserBase(IOContext paramIOContext, int paramInt, Reader paramReader)
  {
    super(paramIOContext, paramInt);
    this._reader = paramReader;
    char[] arrayOfChar = paramIOContext.allocTokenBuffer();
    this._inputBuffer = arrayOfChar;
  }

  protected void _closeInput()
    throws IOException
  {
    if (this._reader == null)
      return;
    if (!this._ioContext.isResourceManaged())
    {
      JsonParser.Feature localFeature = JsonParser.Feature.AUTO_CLOSE_SOURCE;
      if (!isEnabled(localFeature));
    }
    else
    {
      this._reader.close();
    }
    this._reader = null;
  }

  protected void _releaseBuffers()
    throws IOException
  {
    super._releaseBuffers();
    char[] arrayOfChar = this._inputBuffer;
    if (arrayOfChar == null)
      return;
    this._inputBuffer = null;
    this._ioContext.releaseTokenBuffer(arrayOfChar);
  }

  protected char getNextChar(String paramString)
    throws IOException, JsonParseException
  {
    int i = this._inputPtr;
    int j = this._inputEnd;
    if ((i >= j) && (!loadMore()))
      _reportInvalidEOF(paramString);
    char[] arrayOfChar = this._inputBuffer;
    int k = this._inputPtr;
    int m = k + 1;
    this._inputPtr = m;
    return arrayOfChar[k];
  }

  protected final boolean loadMore()
    throws IOException
  {
    boolean bool = false;
    long l1 = this._currInputProcessed;
    long l2 = this._inputEnd;
    long l3 = l1 + l2;
    this._currInputProcessed = l3;
    int i = this._currInputRowStart;
    int j = this._inputEnd;
    int k = i - j;
    this._currInputRowStart = k;
    int n;
    if (this._reader != null)
    {
      Reader localReader = this._reader;
      char[] arrayOfChar = this._inputBuffer;
      int m = this._inputBuffer.length;
      n = localReader.read(arrayOfChar, 0, m);
      if (n <= 0)
        break label109;
      this._inputPtr = 0;
      this._inputEnd = n;
      bool = true;
    }
    label109: 
    do
    {
      return bool;
      _closeInput();
    }
    while (n != 0);
    StringBuilder localStringBuilder = new StringBuilder().append("Reader returned 0 characters when trying to read ");
    int i1 = this._inputEnd;
    String str = i1;
    throw new IOException(str);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.ReaderBasedParserBase
 * JD-Core Version:    0.6.2
 */