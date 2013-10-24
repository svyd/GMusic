package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.Reader;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.util.TextBuffer;

public abstract class ReaderBasedNumericParser extends ReaderBasedParserBase
{
  public ReaderBasedNumericParser(IOContext paramIOContext, int paramInt, Reader paramReader)
  {
    super(paramIOContext, paramInt, paramReader);
  }

  private final JsonToken parseNumberText2(boolean paramBoolean)
    throws IOException, JsonParseException
  {
    char[] arrayOfChar1 = this._textBuffer.emptyAndGetCurrentSegment();
    int i = 0;
    if (paramBoolean)
    {
      int j = 0 + 1;
      arrayOfChar1[0] = '-';
      i = j;
    }
    int k = 0;
    int m = 0;
    int n = this._inputPtr;
    int i1 = this._inputEnd;
    int i2;
    label65: int i3;
    label137: label166: int i7;
    int i9;
    label276: int i15;
    if ((n >= i1) && (!loadMore()))
    {
      i2 = 0;
      m = 1;
      if (k == 0)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Missing integer part (next char ");
        String str1 = _getCharDesc(i2);
        String str2 = str1 + ")";
        reportInvalidNumber(str2);
      }
      i3 = 0;
      if (i2 == 46)
      {
        int i4 = i + 1;
        arrayOfChar1[i] = i2;
        i = i4;
        int i5 = this._inputPtr;
        int i6 = this._inputEnd;
        if ((i5 < i6) || (loadMore()))
          break label626;
        m = 1;
        if (i3 == 0)
          reportUnexpectedNumberChar(i2, "Decimal point not followed by a digit");
      }
      i7 = 0;
      if ((i2 == 101) || (i2 == 69))
      {
        int i8 = arrayOfChar1.length;
        if (i >= i8)
        {
          arrayOfChar1 = this._textBuffer.finishCurrentSegment();
          i = 0;
        }
        i9 = i + 1;
        arrayOfChar1[i] = i2;
        int i10 = this._inputPtr;
        int i11 = this._inputEnd;
        if (i10 >= i11)
          break label713;
        char[] arrayOfChar2 = this._inputBuffer;
        int i12 = this._inputPtr;
        int i13 = i12 + 1;
        this._inputPtr = i13;
        i2 = arrayOfChar2[i12];
        if ((i2 != 45) && (i2 != 43))
          break label779;
        int i14 = arrayOfChar1.length;
        if (i9 < i14)
          break label772;
        arrayOfChar1 = this._textBuffer.finishCurrentSegment();
        i15 = 0;
        label312: i9 = i15 + 1;
        arrayOfChar1[i15] = i2;
        int i16 = this._inputPtr;
        int i17 = this._inputEnd;
        if (i16 >= i17)
          break label724;
        char[] arrayOfChar3 = this._inputBuffer;
        int i18 = this._inputPtr;
        int i19 = i18 + 1;
        this._inputPtr = i19;
        i2 = arrayOfChar3[i18];
        label374: i = i9;
      }
    }
    while (true)
    {
      if ((i2 <= 57) && (i2 >= 48))
      {
        i7 += 1;
        int i20 = arrayOfChar1.length;
        if (i >= i20)
          char[] arrayOfChar4 = this._textBuffer.finishCurrentSegment();
        i9 = i + 1;
        arrayOfChar1[i] = i2;
        int i21 = this._inputPtr;
        int i22 = this._inputEnd;
        if ((i21 >= i22) && (!loadMore()))
        {
          m = 1;
          int i23 = i9;
        }
      }
      else
      {
        if (i7 == 0)
          reportUnexpectedNumberChar(i2, "Exponent indicator not followed by a digit");
        if (m == 0)
        {
          int i24 = this._inputPtr + -1;
          this._inputPtr = i24;
        }
        this._textBuffer.setCurrentLength(i);
        return reset(paramBoolean, k, i3, i7);
        char[] arrayOfChar5 = this._inputBuffer;
        int i25 = this._inputPtr;
        int i26 = i25 + 1;
        this._inputPtr = i26;
        i2 = arrayOfChar5[i25];
        if ((i2 < 48) || (i2 > 57))
          break label65;
        k += 1;
        if (k == 2)
        {
          int i27 = i + -1;
          if (arrayOfChar1[i27] == '0')
            reportInvalidNumber("Leading zeroes not allowed");
        }
        int i28 = arrayOfChar1.length;
        if (i >= i28)
        {
          arrayOfChar1 = this._textBuffer.finishCurrentSegment();
          i = 0;
        }
        int i29 = i + 1;
        arrayOfChar1[i] = i2;
        i = i29;
        break;
        label626: char[] arrayOfChar6 = this._inputBuffer;
        int i30 = this._inputPtr;
        int i31 = i30 + 1;
        this._inputPtr = i31;
        i2 = arrayOfChar6[i30];
        if ((i2 < 48) || (i2 > 57))
          break label166;
        i3 += 1;
        int i32 = arrayOfChar1.length;
        if (i >= i32)
        {
          arrayOfChar1 = this._textBuffer.finishCurrentSegment();
          i = 0;
        }
        int i33 = i + 1;
        arrayOfChar1[i] = i2;
        i = i33;
        break label137;
        label713: i2 = getNextChar("expected a digit for number exponent");
        break label276;
        label724: i2 = getNextChar("expected a digit for number exponent");
        break label374;
      }
      char[] arrayOfChar7 = this._inputBuffer;
      int i34 = this._inputPtr;
      int i35 = i34 + 1;
      this._inputPtr = i35;
      i2 = arrayOfChar7[i34];
      i = i9;
      continue;
      label772: i15 = i9;
      break label312;
      label779: i = i9;
    }
  }

  protected final JsonToken parseNumberText(int paramInt)
    throws IOException, JsonParseException
  {
    if (paramInt == 45);
    int j;
    int k;
    JsonToken localJsonToken;
    for (boolean bool = true; ; bool = false)
    {
      i = this._inputPtr;
      j = i + -1;
      k = this._inputEnd;
      if (!bool)
        break label110;
      int m = this._inputEnd;
      if (i < m)
        break;
      if (bool)
        j += 1;
      this._inputPtr = j;
      localJsonToken = parseNumberText2(bool);
      label64: return localJsonToken;
    }
    char[] arrayOfChar1 = this._inputBuffer;
    int n = i + 1;
    paramInt = arrayOfChar1[i];
    if ((paramInt > 57) || (paramInt < 48))
      reportUnexpectedNumberChar(paramInt, "expected digit (0-9) to follow minus sign, for valid numeric value");
    int i = n;
    label110: int i1 = 1;
    while (true)
    {
      label113: int i2 = this._inputEnd;
      if (i >= i2)
        break;
      char[] arrayOfChar2 = this._inputBuffer;
      n = i + 1;
      paramInt = arrayOfChar2[i];
      int i3;
      if ((paramInt < 48) || (paramInt > 57))
      {
        i3 = 0;
        if (paramInt != 46)
          break label267;
      }
      label267: int i6;
      while (true)
      {
        if (n >= k)
        {
          int i4 = n;
          break;
          i1 += 1;
          if (i1 != 2)
            break label493;
          char[] arrayOfChar3 = this._inputBuffer;
          int i5 = n + -2;
          if (arrayOfChar3[i5] != '0')
            break label493;
          reportInvalidNumber("Leading zeroes not allowed");
          i = n;
          break label113;
        }
        char[] arrayOfChar4 = this._inputBuffer;
        i = n + 1;
        paramInt = arrayOfChar4[n];
        if ((paramInt < 48) || (paramInt > 57))
        {
          if (i3 == 0)
            reportUnexpectedNumberChar(paramInt, "Decimal point not followed by a digit");
          n = i;
          i6 = 0;
          if ((paramInt != 101) && (paramInt != 69))
            break label428;
          if (n < k)
            break label308;
          int i7 = n;
          break;
        }
        i3 += 1;
        n = i;
      }
      label308: char[] arrayOfChar5 = this._inputBuffer;
      i = n + 1;
      paramInt = arrayOfChar5[n];
      if ((paramInt == 45) || (paramInt == 43))
      {
        if (i >= k)
          break;
        char[] arrayOfChar6 = this._inputBuffer;
        n = i + 1;
        paramInt = arrayOfChar6[i];
      }
      while (true)
      {
        if ((paramInt <= 57) && (paramInt >= 48))
        {
          i6 += 1;
          if (n >= k)
          {
            int i8 = n;
            break;
          }
          char[] arrayOfChar7 = this._inputBuffer;
          int i9 = n + 1;
          paramInt = arrayOfChar7[n];
          n = i9;
          continue;
        }
        if (i6 == 0)
          reportUnexpectedNumberChar(paramInt, "Exponent indicator not followed by a digit");
        label428: int i10 = n + -1;
        this._inputPtr = i10;
        int i11 = i10 - j;
        TextBuffer localTextBuffer = this._textBuffer;
        char[] arrayOfChar8 = this._inputBuffer;
        localTextBuffer.resetWithShared(arrayOfChar8, j, i11);
        localJsonToken = reset(bool, i1, i3, i6);
        break label64;
        n = i;
      }
      label493: i = n;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.ReaderBasedNumericParser
 * JD-Core Version:    0.6.2
 */