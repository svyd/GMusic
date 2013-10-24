package org.codehaus.jackson.impl;

import java.io.IOException;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.util.ByteArrayBuilder;
import org.codehaus.jackson.util.TextBuffer;

public abstract class JsonParserBase extends JsonParser
{
  protected byte[] _binaryValue;
  ByteArrayBuilder _byteArrayBuilder = null;
  protected boolean _closed;
  protected long _currInputProcessed = 0L;
  protected int _currInputRow = 1;
  protected int _currInputRowStart = 0;
  protected int _inputEnd = 0;
  protected int _inputPtr = 0;
  protected final IOContext _ioContext;
  protected boolean _nameCopied = false;
  protected char[] _nameCopyBuffer = null;
  protected JsonToken _nextToken;
  protected JsonReadContext _parsingContext;
  protected final TextBuffer _textBuffer;
  protected boolean _tokenIncomplete = false;
  protected int _tokenInputCol = 0;
  protected int _tokenInputRow = 1;
  protected long _tokenInputTotal = 0L;

  protected JsonParserBase(IOContext paramIOContext, int paramInt)
  {
    this._ioContext = paramIOContext;
    this._features = paramInt;
    TextBuffer localTextBuffer = paramIOContext.constructTextBuffer();
    this._textBuffer = localTextBuffer;
    int i = this._tokenInputRow;
    int j = this._tokenInputCol;
    JsonReadContext localJsonReadContext = JsonReadContext.createRootContext(i, j);
    this._parsingContext = localJsonReadContext;
  }

  protected static final String _getCharDesc(int paramInt)
  {
    char c = (char)paramInt;
    String str1;
    if (Character.isISOControl(c))
      str1 = "(CTRL-CHAR, code " + paramInt + ")";
    while (true)
    {
      return str1;
      if (paramInt > 255)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("'").append(c).append("' (code ").append(paramInt).append(" / 0x");
        String str2 = Integer.toHexString(paramInt);
        str1 = str2 + ")";
      }
      else
      {
        str1 = "'" + c + "' (code " + paramInt + ")";
      }
    }
  }

  protected abstract void _closeInput()
    throws IOException;

  protected final JsonParseException _constructError(String paramString, Throwable paramThrowable)
  {
    JsonLocation localJsonLocation = getCurrentLocation();
    return new JsonParseException(paramString, localJsonLocation, paramThrowable);
  }

  protected abstract void _finishString()
    throws IOException, JsonParseException;

  protected void _handleEOF()
    throws JsonParseException
  {
    if (this._parsingContext.inRoot())
      return;
    StringBuilder localStringBuilder1 = new StringBuilder().append(": expected close marker for ");
    String str1 = this._parsingContext.getTypeDesc();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(" (from ");
    JsonReadContext localJsonReadContext = this._parsingContext;
    Object localObject = this._ioContext.getSourceReference();
    JsonLocation localJsonLocation = localJsonReadContext.getStartLocation(localObject);
    String str2 = localJsonLocation + ")";
    _reportInvalidEOF(str2);
  }

  protected void _releaseBuffers()
    throws IOException
  {
    this._textBuffer.releaseBuffers();
    char[] arrayOfChar = this._nameCopyBuffer;
    if (arrayOfChar == null)
      return;
    this._nameCopyBuffer = null;
    this._ioContext.releaseNameCopyBuffer(arrayOfChar);
  }

  protected final void _reportError(String paramString)
    throws JsonParseException
  {
    throw _constructError(paramString);
  }

  protected void _reportInvalidEOF()
    throws JsonParseException
  {
    StringBuilder localStringBuilder = new StringBuilder().append(" in ");
    JsonToken localJsonToken = this._currToken;
    String str = localJsonToken;
    _reportInvalidEOF(str);
  }

  protected void _reportInvalidEOF(String paramString)
    throws JsonParseException
  {
    String str = "Unexpected end-of-input" + paramString;
    _reportError(str);
  }

  protected void _reportMismatchedEndMarker(int paramInt, char paramChar)
    throws JsonParseException
  {
    StringBuilder localStringBuilder1 = new StringBuilder().append("");
    JsonReadContext localJsonReadContext = this._parsingContext;
    Object localObject = this._ioContext.getSourceReference();
    JsonLocation localJsonLocation = localJsonReadContext.getStartLocation(localObject);
    String str1 = localJsonLocation;
    StringBuilder localStringBuilder2 = new StringBuilder().append("Unexpected close marker '");
    char c = (char)paramInt;
    StringBuilder localStringBuilder3 = localStringBuilder2.append(c).append("': expected '").append(paramChar).append("' (for ");
    String str2 = this._parsingContext.getTypeDesc();
    String str3 = str2 + " starting at " + str1 + ")";
    _reportError(str3);
  }

  protected void _reportUnexpectedChar(int paramInt, String paramString)
    throws JsonParseException
  {
    StringBuilder localStringBuilder = new StringBuilder().append("Unexpected character (");
    String str1 = _getCharDesc(paramInt);
    String str2 = str1 + ")";
    if (paramString != null)
      str2 = str2 + ": " + paramString;
    _reportError(str2);
  }

  protected final void _throwInternal()
  {
    throw new RuntimeException("Internal error: this code path should never get executed");
  }

  protected void _throwInvalidSpace(int paramInt)
    throws JsonParseException
  {
    int i = (char)paramInt;
    StringBuilder localStringBuilder = new StringBuilder().append("Illegal character (");
    String str1 = _getCharDesc(i);
    String str2 = str1 + "): only regular white space (\\r, \\n, \\t) is allowed between tokens";
    _reportError(str2);
  }

  protected void _throwUnquotedSpace(int paramInt, String paramString)
    throws JsonParseException
  {
    JsonParser.Feature localFeature = JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS;
    if ((isEnabled(localFeature)) && (paramInt < 32))
      return;
    int i = (char)paramInt;
    StringBuilder localStringBuilder = new StringBuilder().append("Illegal unquoted character (");
    String str1 = _getCharDesc(i);
    String str2 = str1 + "): has to be escaped using backslash to be included in " + paramString;
    _reportError(str2);
  }

  protected final void _wrapError(String paramString, Throwable paramThrowable)
    throws JsonParseException
  {
    throw _constructError(paramString, paramThrowable);
  }

  public void close()
    throws IOException
  {
    this._closed = true;
    _closeInput();
    _releaseBuffers();
  }

  public JsonLocation getCurrentLocation()
  {
    int i = this._inputPtr;
    int j = this._currInputRowStart;
    int k = i - j + 1;
    Object localObject = this._ioContext.getSourceReference();
    long l1 = this._currInputProcessed;
    long l2 = this._inputPtr;
    long l3 = l1 + l2 - 1L;
    int m = this._currInputRow;
    return new JsonLocation(localObject, l3, m, k);
  }

  public String getCurrentName()
    throws IOException, JsonParseException
  {
    return this._parsingContext.getCurrentName();
  }

  public String getText()
    throws IOException, JsonParseException
  {
    String str;
    if (this._currToken != null)
    {
      int[] arrayOfInt = 1.$SwitchMap$org$codehaus$jackson$JsonToken;
      int i = this._currToken.ordinal();
      switch (arrayOfInt[i])
      {
      default:
        str = this._currToken.asString();
      case 5:
      case 6:
      case 7:
      case 8:
      }
    }
    while (true)
    {
      return str;
      str = this._parsingContext.getCurrentName();
      continue;
      if (this._tokenIncomplete)
      {
        this._tokenIncomplete = false;
        _finishString();
      }
      str = this._textBuffer.contentsAsString();
      continue;
      str = null;
    }
  }

  protected abstract boolean loadMore()
    throws IOException;

  protected final void loadMoreGuaranteed()
    throws IOException
  {
    if (loadMore())
      return;
    _reportInvalidEOF();
  }

  public abstract JsonToken nextToken()
    throws IOException, JsonParseException;

  public JsonParser skipChildren()
    throws IOException, JsonParseException
  {
    JsonToken localJsonToken1 = this._currToken;
    JsonToken localJsonToken2 = JsonToken.START_OBJECT;
    if (localJsonToken1 != localJsonToken2)
    {
      JsonToken localJsonToken3 = this._currToken;
      JsonToken localJsonToken4 = JsonToken.START_ARRAY;
      if (localJsonToken3 == localJsonToken4);
    }
    while (true)
    {
      return this;
      int i = 1;
      do
      {
        while (true)
        {
          JsonToken localJsonToken5 = nextToken();
          if (localJsonToken5 == null)
          {
            _handleEOF();
            break;
          }
          int[] arrayOfInt = 1.$SwitchMap$org$codehaus$jackson$JsonToken;
          int j = localJsonToken5.ordinal();
          switch (arrayOfInt[j])
          {
          default:
            break;
          case 1:
          case 2:
            i += 1;
          case 3:
          case 4:
          }
        }
        i += -1;
      }
      while (i != 0);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.JsonParserBase
 * JD-Core Version:    0.6.2
 */