package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.io.IOContext;

public abstract class StreamBasedParserBase extends JsonNumericParserBase
{
  protected boolean _bufferRecyclable;
  protected byte[] _inputBuffer;
  protected InputStream _inputStream;

  protected StreamBasedParserBase(IOContext paramIOContext, int paramInt1, InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    super(paramIOContext, paramInt1);
    this._inputStream = paramInputStream;
    this._inputBuffer = paramArrayOfByte;
    this._inputPtr = paramInt2;
    this._inputEnd = paramInt3;
    this._bufferRecyclable = paramBoolean;
  }

  protected void _closeInput()
    throws IOException
  {
    if (this._inputStream == null)
      return;
    if (!this._ioContext.isResourceManaged())
    {
      JsonParser.Feature localFeature = JsonParser.Feature.AUTO_CLOSE_SOURCE;
      if (!isEnabled(localFeature));
    }
    else
    {
      this._inputStream.close();
    }
    this._inputStream = null;
  }

  protected void _releaseBuffers()
    throws IOException
  {
    super._releaseBuffers();
    if (!this._bufferRecyclable)
      return;
    byte[] arrayOfByte = this._inputBuffer;
    if (arrayOfByte == null)
      return;
    this._inputBuffer = null;
    this._ioContext.releaseReadIOBuffer(arrayOfByte);
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
    if (this._inputStream != null)
    {
      InputStream localInputStream = this._inputStream;
      byte[] arrayOfByte = this._inputBuffer;
      int m = this._inputBuffer.length;
      n = localInputStream.read(arrayOfByte, 0, m);
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
 * Qualified Name:     org.codehaus.jackson.impl.StreamBasedParserBase
 * JD-Core Version:    0.6.2
 */