package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.Reader;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.sym.CharsToNameCanonicalizer;
import org.codehaus.jackson.util.CharTypes;
import org.codehaus.jackson.util.TextBuffer;

public final class ReaderBasedParser extends ReaderBasedNumericParser
{
  protected ObjectCodec _objectCodec;
  protected final CharsToNameCanonicalizer _symbols;

  public ReaderBasedParser(IOContext paramIOContext, int paramInt, Reader paramReader, ObjectCodec paramObjectCodec, CharsToNameCanonicalizer paramCharsToNameCanonicalizer)
  {
    super(paramIOContext, paramInt, paramReader);
    this._objectCodec = paramObjectCodec;
    this._symbols = paramCharsToNameCanonicalizer;
  }

  private final JsonToken _nextAfterName()
  {
    this._nameCopied = false;
    JsonToken localJsonToken1 = this._nextToken;
    this._nextToken = null;
    JsonToken localJsonToken2 = JsonToken.START_ARRAY;
    if (localJsonToken1 == localJsonToken2)
    {
      JsonReadContext localJsonReadContext1 = this._parsingContext;
      int i = this._tokenInputRow;
      int j = this._tokenInputCol;
      JsonReadContext localJsonReadContext2 = localJsonReadContext1.createChildArrayContext(i, j);
      this._parsingContext = localJsonReadContext2;
    }
    while (true)
    {
      this._currToken = localJsonToken1;
      return localJsonToken1;
      JsonToken localJsonToken3 = JsonToken.START_OBJECT;
      if (localJsonToken1 == localJsonToken3)
      {
        JsonReadContext localJsonReadContext3 = this._parsingContext;
        int k = this._tokenInputRow;
        int m = this._tokenInputCol;
        JsonReadContext localJsonReadContext4 = localJsonReadContext3.createChildObjectContext(k, m);
        this._parsingContext = localJsonReadContext4;
      }
    }
  }

  private String _parseFieldName2(int paramInt1, int paramInt2, int paramInt3)
    throws IOException, JsonParseException
  {
    TextBuffer localTextBuffer1 = this._textBuffer;
    char[] arrayOfChar1 = this._inputBuffer;
    int i = this._inputPtr - paramInt1;
    localTextBuffer1.resetWithShared(arrayOfChar1, paramInt1, i);
    char[] arrayOfChar2 = this._textBuffer.getCurrentSegment();
    int j = this._textBuffer.getCurrentSegmentSize();
    while (true)
    {
      int k = this._inputPtr;
      int m = this._inputEnd;
      if ((k >= m) && (!loadMore()))
      {
        StringBuilder localStringBuilder = new StringBuilder().append(": was expecting closing '");
        char c = (char)paramInt3;
        String str = c + "' for name";
        _reportInvalidEOF(str);
      }
      char[] arrayOfChar3 = this._inputBuffer;
      int n = this._inputPtr;
      int i1 = n + 1;
      this._inputPtr = i1;
      int i2 = arrayOfChar3[n];
      int i3 = i2;
      if (i3 <= 92)
      {
        if (i3 != 92)
          break label218;
        i2 = _decodeEscaped();
      }
      int i4;
      while (true)
      {
        paramInt2 = paramInt2 * 31 + i3;
        i4 = j + 1;
        arrayOfChar2[j] = i2;
        int i5 = arrayOfChar2.length;
        if (i4 < i5)
          break label299;
        arrayOfChar2 = this._textBuffer.finishCurrentSegment();
        j = 0;
        break;
        label218: if (i3 <= paramInt3)
        {
          if (i3 != paramInt3)
          {
            this._textBuffer.setCurrentLength(j);
            TextBuffer localTextBuffer2 = this._textBuffer;
            char[] arrayOfChar4 = localTextBuffer2.getTextBuffer();
            int i6 = localTextBuffer2.getTextOffset();
            int i7 = localTextBuffer2.size();
            return this._symbols.findSymbol(arrayOfChar4, i6, i7, paramInt2);
          }
          if (i3 < 32)
            _throwUnquotedSpace(i3, "name");
        }
      }
      label299: j = i4;
    }
  }

  private String _parseUnusualFieldName2(int paramInt1, int paramInt2, int[] paramArrayOfInt)
    throws IOException, JsonParseException
  {
    TextBuffer localTextBuffer1 = this._textBuffer;
    char[] arrayOfChar1 = this._inputBuffer;
    int i = this._inputPtr - paramInt1;
    localTextBuffer1.resetWithShared(arrayOfChar1, paramInt1, i);
    char[] arrayOfChar2 = this._textBuffer.getCurrentSegment();
    int j = this._textBuffer.getCurrentSegmentSize();
    int k = paramArrayOfInt.length;
    while (true)
    {
      int m = this._inputPtr;
      int n = this._inputEnd;
      if ((m >= n) && (!loadMore()));
      int i8;
      while (true)
      {
        this._textBuffer.setCurrentLength(j);
        TextBuffer localTextBuffer2 = this._textBuffer;
        char[] arrayOfChar3 = localTextBuffer2.getTextBuffer();
        int i1 = localTextBuffer2.getTextOffset();
        int i2 = localTextBuffer2.size();
        CharsToNameCanonicalizer localCharsToNameCanonicalizer = this._symbols;
        int i3 = paramInt2;
        return localCharsToNameCanonicalizer.findSymbol(arrayOfChar3, i1, i2, i3);
        char[] arrayOfChar4 = this._inputBuffer;
        int i4 = this._inputPtr;
        int i5 = arrayOfChar4[i4];
        int i6 = i5;
        if (i6 <= k)
        {
          if (paramArrayOfInt[i6] != 0);
        }
        else
          while (Character.isJavaIdentifierPart(i5))
          {
            int i7 = this._inputPtr + 1;
            this._inputPtr = i7;
            paramInt2 = paramInt2 * 31 + i6;
            i8 = j + 1;
            arrayOfChar2[j] = i5;
            int i9 = arrayOfChar2.length;
            if (i8 < i9)
              break label247;
            arrayOfChar2 = this._textBuffer.finishCurrentSegment();
            j = 0;
            break;
          }
      }
      label247: j = i8;
    }
  }

  private void _reportInvalidToken(String paramString)
    throws IOException, JsonParseException
  {
    StringBuilder localStringBuilder1 = new StringBuilder(paramString);
    while (true)
    {
      int i = this._inputPtr;
      int j = this._inputEnd;
      if ((i >= j) && (!loadMore()));
      char c;
      do
      {
        StringBuilder localStringBuilder2 = new StringBuilder().append("Unrecognized token '");
        String str1 = localStringBuilder1.toString();
        String str2 = str1 + "': was expecting 'null', 'true' or 'false'";
        _reportError(str2);
        return;
        char[] arrayOfChar = this._inputBuffer;
        int k = this._inputPtr;
        c = arrayOfChar[k];
      }
      while (!Character.isJavaIdentifierPart(c));
      int m = this._inputPtr + 1;
      this._inputPtr = m;
      StringBuilder localStringBuilder3 = localStringBuilder1.append(c);
    }
  }

  private final void _skipCComment()
    throws IOException, JsonParseException
  {
    while (true)
    {
      int i = this._inputPtr;
      int j = this._inputEnd;
      int n;
      if ((i < j) || (loadMore()))
      {
        char[] arrayOfChar1 = this._inputBuffer;
        int k = this._inputPtr;
        int m = k + 1;
        this._inputPtr = m;
        n = arrayOfChar1[k];
        if (n <= 42)
          if (n == 42)
          {
            int i1 = this._inputPtr;
            int i2 = this._inputEnd;
            if ((i1 < i2) || (loadMore()));
          }
      }
      else
      {
        _reportInvalidEOF(" in a comment");
        return;
        char[] arrayOfChar2 = this._inputBuffer;
        int i3 = this._inputPtr;
        if (arrayOfChar2[i3] == '/')
        {
          int i4 = this._inputPtr + 1;
          this._inputPtr = i4;
          return;
          if (n < 32)
            if (n == 10)
              _skipLF();
            else if (n == 13)
              _skipCR();
            else if (n != 9)
              _throwInvalidSpace(n);
        }
      }
    }
  }

  private final void _skipComment()
    throws IOException, JsonParseException
  {
    JsonParser.Feature localFeature = JsonParser.Feature.ALLOW_COMMENTS;
    if (!isEnabled(localFeature))
      _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
    int i = this._inputPtr;
    int j = this._inputEnd;
    if ((i >= j) && (!loadMore()))
      _reportInvalidEOF(" in a comment");
    char[] arrayOfChar = this._inputBuffer;
    int k = this._inputPtr;
    int m = k + 1;
    this._inputPtr = m;
    int n = arrayOfChar[k];
    if (n == 47)
    {
      _skipCppComment();
      return;
    }
    if (n == 42)
    {
      _skipCComment();
      return;
    }
    _reportUnexpectedChar(n, "was expecting either '*' or '/' for a comment");
  }

  private final void _skipCppComment()
    throws IOException, JsonParseException
  {
    while (true)
    {
      int i = this._inputPtr;
      int j = this._inputEnd;
      if ((i >= j) && (!loadMore()))
        return;
      char[] arrayOfChar = this._inputBuffer;
      int k = this._inputPtr;
      int m = k + 1;
      this._inputPtr = m;
      int n = arrayOfChar[k];
      if (n < 32)
      {
        if (n == 10)
        {
          _skipLF();
          return;
        }
        if (n == 13)
        {
          _skipCR();
          return;
        }
        if (n != 9)
          _throwInvalidSpace(n);
      }
    }
  }

  private final int _skipWS()
    throws IOException, JsonParseException
  {
    while (true)
    {
      int i = this._inputPtr;
      int j = this._inputEnd;
      if ((i >= j) && (!loadMore()))
        break;
      char[] arrayOfChar = this._inputBuffer;
      int k = this._inputPtr;
      int m = k + 1;
      this._inputPtr = m;
      int n = arrayOfChar[k];
      if (n > 32)
      {
        if (n != 47)
          return n;
        _skipComment();
      }
      else if (n != 32)
      {
        if (n == 10)
          _skipLF();
        else if (n == 13)
          _skipCR();
        else if (n != 9)
          _throwInvalidSpace(n);
      }
    }
    StringBuilder localStringBuilder = new StringBuilder().append("Unexpected end-of-input within/between ");
    String str1 = this._parsingContext.getTypeDesc();
    String str2 = str1 + " entries";
    throw _constructError(str2);
  }

  private final int _skipWSOrEnd()
    throws IOException, JsonParseException
  {
    int i = this._inputPtr;
    int j = this._inputEnd;
    int n;
    if ((i < j) || (loadMore()))
    {
      char[] arrayOfChar = this._inputBuffer;
      int k = this._inputPtr;
      int m = k + 1;
      this._inputPtr = m;
      n = arrayOfChar[k];
      if (n > 32)
        if (n == 47);
    }
    while (true)
    {
      return n;
      _skipComment();
      break;
      if (n == 32)
        break;
      if (n == 10)
      {
        _skipLF();
        break;
      }
      if (n == 13)
      {
        _skipCR();
        break;
      }
      if (n == 9)
        break;
      _throwInvalidSpace(n);
      break;
      _handleEOF();
      n = -1;
    }
  }

  protected final char _decodeEscaped()
    throws IOException, JsonParseException
  {
    int i = this._inputPtr;
    int j = this._inputEnd;
    if ((i >= j) && (!loadMore()))
      _reportInvalidEOF(" in character escape sequence");
    char[] arrayOfChar1 = this._inputBuffer;
    int k = this._inputPtr;
    int m = k + 1;
    this._inputPtr = m;
    char c = arrayOfChar1[k];
    int n;
    int i1;
    switch (c)
    {
    default:
      StringBuilder localStringBuilder = new StringBuilder().append("Unrecognized character escape ");
      String str1 = _getCharDesc(c);
      String str2 = str1;
      _reportError(str2);
    case 'u':
      n = 0;
      i1 = 0;
    case 'b':
      while (i1 < 4)
      {
        int i2 = this._inputPtr;
        int i3 = this._inputEnd;
        if ((i2 >= i3) && (!loadMore()))
          _reportInvalidEOF(" in character escape sequence");
        char[] arrayOfChar2 = this._inputBuffer;
        int i4 = this._inputPtr;
        int i5 = i4 + 1;
        this._inputPtr = i5;
        int i6 = arrayOfChar2[i4];
        int i7 = CharTypes.charToHex(i6);
        if (i7 < 0)
          _reportUnexpectedChar(i6, "expected a hex-digit for character escape sequence");
        n = n << 4 | i7;
        i1 += 1;
        continue;
        c = '\b';
      }
    case '"':
    case '/':
    case '\\':
    case 't':
    case 'n':
    case 'f':
    case 'r':
    }
    while (true)
    {
      return c;
      c = '\t';
      continue;
      c = '\n';
      continue;
      c = '\f';
      continue;
      c = '\r';
      continue;
      c = (char)n;
    }
  }

  protected void _finishString()
    throws IOException, JsonParseException
  {
    int i = this._inputPtr;
    int j = this._inputEnd;
    if (i < j)
    {
      int[] arrayOfInt = CharTypes.getInputCodeLatin1();
      int k = arrayOfInt.length;
      do
      {
        int m = this._inputBuffer[i];
        if ((m < k) && (arrayOfInt[m] != 0))
        {
          if (m != 34)
            break;
          TextBuffer localTextBuffer1 = this._textBuffer;
          char[] arrayOfChar1 = this._inputBuffer;
          int n = this._inputPtr;
          int i1 = this._inputPtr;
          int i2 = i - i1;
          localTextBuffer1.resetWithShared(arrayOfChar1, n, i2);
          int i3 = i + 1;
          this._inputPtr = i3;
          return;
        }
        i += 1;
      }
      while (i < j);
    }
    TextBuffer localTextBuffer2 = this._textBuffer;
    char[] arrayOfChar2 = this._inputBuffer;
    int i4 = this._inputPtr;
    int i5 = this._inputPtr;
    int i6 = i - i5;
    localTextBuffer2.resetWithCopy(arrayOfChar2, i4, i6);
    this._inputPtr = i;
    _finishString2();
  }

  protected void _finishString2()
    throws IOException, JsonParseException
  {
    char[] arrayOfChar1 = this._textBuffer.getCurrentSegment();
    int i = this._textBuffer.getCurrentSegmentSize();
    int j = this._inputPtr;
    int k = this._inputEnd;
    if ((j >= k) && (!loadMore()))
      _reportInvalidEOF(": was expecting closing quote for a string value");
    char[] arrayOfChar2 = this._inputBuffer;
    int m = this._inputPtr;
    int n = m + 1;
    this._inputPtr = n;
    int i1 = arrayOfChar2[m];
    int i2 = i1;
    if (i2 <= 92)
    {
      if (i2 != 92)
        break label137;
      i1 = _decodeEscaped();
    }
    while (true)
    {
      int i3 = arrayOfChar1.length;
      if (i >= i3)
      {
        arrayOfChar1 = this._textBuffer.finishCurrentSegment();
        i = 0;
      }
      int i4 = i + 1;
      arrayOfChar1[i] = i1;
      i = i4;
      break;
      label137: if (i2 <= 34)
      {
        if (i2 == 34)
        {
          this._textBuffer.setCurrentLength(i);
          return;
        }
        if (i2 < 32)
          _throwUnquotedSpace(i2, "string value");
      }
    }
  }

  protected final JsonToken _handleUnexpectedValue(int paramInt)
    throws IOException, JsonParseException
  {
    if (paramInt == 39)
    {
      JsonParser.Feature localFeature = JsonParser.Feature.ALLOW_SINGLE_QUOTES;
      if (isEnabled(localFeature));
    }
    else
    {
      _reportUnexpectedChar(paramInt, "expected a valid value (number, String, array, object, 'true', 'false' or 'null')");
    }
    char[] arrayOfChar1 = this._textBuffer.emptyAndGetCurrentSegment();
    int i = this._textBuffer.getCurrentSegmentSize();
    int j = this._inputPtr;
    int k = this._inputEnd;
    if ((j >= k) && (!loadMore()))
      _reportInvalidEOF(": was expecting closing quote for a string value");
    char[] arrayOfChar2 = this._inputBuffer;
    int m = this._inputPtr;
    int n = m + 1;
    this._inputPtr = n;
    int i1 = arrayOfChar2[m];
    paramInt = i1;
    if (paramInt <= 92)
    {
      if (paramInt != 92)
        break label168;
      i1 = _decodeEscaped();
    }
    while (true)
    {
      int i2 = arrayOfChar1.length;
      if (i >= i2)
      {
        arrayOfChar1 = this._textBuffer.finishCurrentSegment();
        i = 0;
      }
      int i3 = i + 1;
      arrayOfChar1[i] = i1;
      i = i3;
      break;
      label168: if (paramInt <= 39)
      {
        if (paramInt == 39)
        {
          this._textBuffer.setCurrentLength(i);
          return JsonToken.VALUE_STRING;
        }
        if (paramInt < 32)
          _throwUnquotedSpace(paramInt, "string value");
      }
    }
  }

  protected final String _handleUnusualFieldName(int paramInt)
    throws IOException, JsonParseException
  {
    String str;
    if (paramInt == 39)
    {
      JsonParser.Feature localFeature1 = JsonParser.Feature.ALLOW_SINGLE_QUOTES;
      if (isEnabled(localFeature1))
        str = _parseApostropheFieldName();
    }
    while (true)
    {
      return str;
      JsonParser.Feature localFeature2 = JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES;
      if (!isEnabled(localFeature2))
        _reportUnexpectedChar(paramInt, "was expecting double-quote to start field name");
      int[] arrayOfInt = CharTypes.getInputCodeLatin1JsNames();
      int i = arrayOfInt.length;
      boolean bool;
      label85: int j;
      int k;
      int m;
      if (paramInt < i)
        if ((arrayOfInt[paramInt] == 0) && ((paramInt < 48) || (paramInt > 57)))
        {
          bool = true;
          if (!bool)
            _reportUnexpectedChar(paramInt, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
          j = this._inputPtr;
          k = 0;
          m = this._inputEnd;
          if (j >= m)
            break label294;
        }
      label271: 
      do
      {
        int n = this._inputBuffer[j];
        if (n < i)
        {
          if (arrayOfInt[n] == 0)
            break label271;
          int i1 = this._inputPtr + -1;
          this._inputPtr = j;
          CharsToNameCanonicalizer localCharsToNameCanonicalizer1 = this._symbols;
          char[] arrayOfChar1 = this._inputBuffer;
          int i2 = j - i1;
          str = localCharsToNameCanonicalizer1.findSymbol(arrayOfChar1, i1, i2, k);
          break;
          bool = false;
          break label85;
          bool = Character.isJavaIdentifierPart((char)paramInt);
          break label85;
        }
        if (!Character.isJavaIdentifierPart((char)n))
        {
          int i3 = this._inputPtr + -1;
          this._inputPtr = j;
          CharsToNameCanonicalizer localCharsToNameCanonicalizer2 = this._symbols;
          char[] arrayOfChar2 = this._inputBuffer;
          int i4 = j - i3;
          str = localCharsToNameCanonicalizer2.findSymbol(arrayOfChar2, i3, i4, k);
          break;
        }
        k = k * 31 + n;
        j += 1;
      }
      while (j < m);
      label294: int i5 = this._inputPtr + -1;
      this._inputPtr = j;
      str = _parseUnusualFieldName2(i5, k, arrayOfInt);
    }
  }

  protected void _matchToken(JsonToken paramJsonToken)
    throws IOException, JsonParseException
  {
    String str1 = paramJsonToken.asString();
    int i = 1;
    int j = str1.length();
    while (true)
    {
      if (i >= j)
        return;
      int k = this._inputPtr;
      int m = this._inputEnd;
      if ((k >= m) && (!loadMore()))
        _reportInvalidEOF(" in a value");
      char[] arrayOfChar = this._inputBuffer;
      int n = this._inputPtr;
      int i1 = arrayOfChar[n];
      int i2 = str1.charAt(i);
      if (i1 != i2)
      {
        String str2 = str1.substring(0, i);
        _reportInvalidToken(str2);
      }
      int i3 = this._inputPtr + 1;
      this._inputPtr = i3;
      i += 1;
    }
  }

  protected final String _parseApostropheFieldName()
    throws IOException, JsonParseException
  {
    int i = this._inputPtr;
    int j = 0;
    int k = this._inputEnd;
    int n;
    String str;
    if (i < k)
    {
      int[] arrayOfInt = CharTypes.getInputCodeLatin1();
      int m = arrayOfInt.length;
      n = this._inputBuffer[i];
      if (n == 39)
      {
        int i1 = this._inputPtr;
        int i2 = i + 1;
        this._inputPtr = i2;
        CharsToNameCanonicalizer localCharsToNameCanonicalizer = this._symbols;
        char[] arrayOfChar = this._inputBuffer;
        int i3 = i - i1;
        str = localCharsToNameCanonicalizer.findSymbol(arrayOfChar, i1, i3, j);
        label91: return str;
      }
      if ((n >= m) || (arrayOfInt[n] == 0))
        break label134;
    }
    while (true)
    {
      int i4 = this._inputPtr;
      this._inputPtr = i;
      str = _parseFieldName2(i4, j, 39);
      break label91;
      label134: j = j * 31 + n;
      i += 1;
      if (i < k)
        break;
    }
  }

  protected final String _parseFieldName(int paramInt)
    throws IOException, JsonParseException
  {
    String str;
    if (paramInt != 34)
      str = _handleUnusualFieldName(paramInt);
    while (true)
    {
      return str;
      int i = this._inputPtr;
      int j = 0;
      int k = this._inputEnd;
      if (i < k)
      {
        int[] arrayOfInt = CharTypes.getInputCodeLatin1();
        int m = arrayOfInt.length;
        do
        {
          int n = this._inputBuffer[i];
          if ((n < m) && (arrayOfInt[n] != 0))
          {
            if (n != 34)
              break label146;
            int i1 = this._inputPtr;
            int i2 = i + 1;
            this._inputPtr = i2;
            CharsToNameCanonicalizer localCharsToNameCanonicalizer = this._symbols;
            char[] arrayOfChar = this._inputBuffer;
            int i3 = i - i1;
            str = localCharsToNameCanonicalizer.findSymbol(arrayOfChar, i1, i3, j);
            break;
          }
          j = j * 31 + n;
          i += 1;
        }
        while (i < k);
      }
      label146: int i4 = this._inputPtr;
      this._inputPtr = i;
      str = _parseFieldName2(i4, j, 34);
    }
  }

  protected final void _skipCR()
    throws IOException
  {
    int i = this._inputPtr;
    int j = this._inputEnd;
    if ((i < j) || (loadMore()))
    {
      char[] arrayOfChar = this._inputBuffer;
      int k = this._inputPtr;
      if (arrayOfChar[k] == '\n')
      {
        int m = this._inputPtr + 1;
        this._inputPtr = m;
      }
    }
    int n = this._currInputRow + 1;
    this._currInputRow = n;
    int i1 = this._inputPtr;
    this._currInputRowStart = i1;
  }

  protected final void _skipLF()
    throws IOException
  {
    int i = this._currInputRow + 1;
    this._currInputRow = i;
    int j = this._inputPtr;
    this._currInputRowStart = j;
  }

  protected void _skipString()
    throws IOException, JsonParseException
  {
    this._tokenIncomplete = false;
    int i = this._inputPtr;
    int j = this._inputEnd;
    char[] arrayOfChar = this._inputBuffer;
    while (true)
    {
      if (i >= j)
      {
        this._inputPtr = i;
        if (!loadMore())
          _reportInvalidEOF(": was expecting closing quote for a string value");
        i = this._inputPtr;
        j = this._inputEnd;
      }
      int k = i + 1;
      int m = arrayOfChar[i];
      if (m <= 92)
      {
        if (m == 92)
        {
          this._inputPtr = k;
          int n = _decodeEscaped();
          i = this._inputPtr;
          j = this._inputEnd;
        }
        else if (m <= 34)
        {
          if (m == 34)
          {
            this._inputPtr = k;
            return;
          }
          if (m < 32)
          {
            this._inputPtr = k;
            _throwUnquotedSpace(m, "string value");
          }
        }
      }
      else
        i = k;
    }
  }

  public void close()
    throws IOException
  {
    super.close();
    this._symbols.release();
  }

  public JsonToken nextToken()
    throws IOException, JsonParseException
  {
    JsonToken localJsonToken1 = null;
    JsonToken localJsonToken2 = this._currToken;
    JsonToken localJsonToken3 = JsonToken.FIELD_NAME;
    if (localJsonToken2 == localJsonToken3)
      localJsonToken1 = _nextAfterName();
    while (true)
    {
      return localJsonToken1;
      if (this._tokenIncomplete)
        _skipString();
      int i = _skipWSOrEnd();
      if (i < 0)
      {
        close();
        this._currToken = null;
      }
      else
      {
        long l1 = this._currInputProcessed;
        long l2 = this._inputPtr;
        long l3 = l1 + l2 - 1L;
        this._tokenInputTotal = l3;
        int j = this._currInputRow;
        this._tokenInputRow = j;
        int k = this._inputPtr;
        int m = this._currInputRowStart;
        int n = k - m + -1;
        this._tokenInputCol = n;
        this._binaryValue = null;
        if (i == 93)
        {
          if (!this._parsingContext.inArray())
            _reportMismatchedEndMarker(i, '}');
          JsonReadContext localJsonReadContext1 = this._parsingContext.getParent();
          this._parsingContext = localJsonReadContext1;
          localJsonToken1 = JsonToken.END_ARRAY;
          this._currToken = localJsonToken1;
        }
        else if (i == 125)
        {
          if (!this._parsingContext.inObject())
            _reportMismatchedEndMarker(i, ']');
          JsonReadContext localJsonReadContext2 = this._parsingContext.getParent();
          this._parsingContext = localJsonReadContext2;
          localJsonToken1 = JsonToken.END_OBJECT;
          this._currToken = localJsonToken1;
        }
        else
        {
          if (this._parsingContext.expectComma())
          {
            if (i != 44)
            {
              StringBuilder localStringBuilder = new StringBuilder().append("was expecting comma to separate ");
              String str1 = this._parsingContext.getTypeDesc();
              String str2 = str1 + " entries";
              _reportUnexpectedChar(i, str2);
            }
            i = _skipWS();
          }
          boolean bool = this._parsingContext.inObject();
          if (bool)
          {
            String str3 = _parseFieldName(i);
            this._parsingContext.setCurrentName(str3);
            JsonToken localJsonToken4 = JsonToken.FIELD_NAME;
            this._currToken = localJsonToken4;
            int i1 = _skipWS();
            if (i1 != 58)
              _reportUnexpectedChar(i1, "was expecting a colon to separate field name and value");
            i = _skipWS();
          }
          switch (i)
          {
          default:
            localJsonToken1 = _handleUnexpectedValue(i);
          case 34:
          case 91:
          case 123:
          case 93:
          case 125:
          case 116:
          case 102:
          case 110:
          case 45:
          case 48:
          case 49:
          case 50:
          case 51:
          case 52:
          case 53:
          case 54:
          case 55:
          case 56:
          case 57:
          }
          while (true)
          {
            if (!bool)
              break label748;
            this._nextToken = localJsonToken1;
            localJsonToken1 = this._currToken;
            break;
            this._tokenIncomplete = true;
            localJsonToken1 = JsonToken.VALUE_STRING;
            continue;
            if (!bool)
            {
              JsonReadContext localJsonReadContext3 = this._parsingContext;
              int i2 = this._tokenInputRow;
              int i3 = this._tokenInputCol;
              JsonReadContext localJsonReadContext4 = localJsonReadContext3.createChildArrayContext(i2, i3);
              this._parsingContext = localJsonReadContext4;
            }
            localJsonToken1 = JsonToken.START_ARRAY;
            continue;
            if (!bool)
            {
              JsonReadContext localJsonReadContext5 = this._parsingContext;
              int i4 = this._tokenInputRow;
              int i5 = this._tokenInputCol;
              JsonReadContext localJsonReadContext6 = localJsonReadContext5.createChildObjectContext(i4, i5);
              this._parsingContext = localJsonReadContext6;
            }
            localJsonToken1 = JsonToken.START_OBJECT;
            continue;
            _reportUnexpectedChar(i, "expected a value");
            JsonToken localJsonToken5 = JsonToken.VALUE_TRUE;
            _matchToken(localJsonToken5);
            localJsonToken1 = JsonToken.VALUE_TRUE;
            continue;
            JsonToken localJsonToken6 = JsonToken.VALUE_FALSE;
            _matchToken(localJsonToken6);
            localJsonToken1 = JsonToken.VALUE_FALSE;
            continue;
            JsonToken localJsonToken7 = JsonToken.VALUE_NULL;
            _matchToken(localJsonToken7);
            localJsonToken1 = JsonToken.VALUE_NULL;
            continue;
            localJsonToken1 = parseNumberText(i);
          }
          label748: this._currToken = localJsonToken1;
        }
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.ReaderBasedParser
 * JD-Core Version:    0.6.2
 */