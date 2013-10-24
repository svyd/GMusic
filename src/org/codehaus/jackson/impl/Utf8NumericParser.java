package org.codehaus.jackson.impl;

import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.util.TextBuffer;

public abstract class Utf8NumericParser extends StreamBasedParserBase
{
  public Utf8NumericParser(IOContext paramIOContext, int paramInt1, InputStream paramInputStream, byte[] paramArrayOfByte, int paramInt2, int paramInt3, boolean paramBoolean)
  {
    super(paramIOContext, paramInt1, paramInputStream, paramArrayOfByte, paramInt2, paramInt3, paramBoolean);
  }

  protected final JsonToken parseNumberText(int paramInt)
    throws IOException, JsonParseException
  {
    char[] arrayOfChar1 = this._textBuffer.emptyAndGetCurrentSegment();
    int i = 0;
    boolean bool;
    int j;
    int i2;
    int i3;
    label100: int i4;
    if (paramInt == 45)
    {
      bool = true;
      if (bool)
      {
        j = 0 + 1;
        arrayOfChar1[0] = '-';
        int k = this._inputPtr;
        int m = this._inputEnd;
        if (k >= m)
          loadMoreGuaranteed();
        byte[] arrayOfByte1 = this._inputBuffer;
        int n = this._inputPtr;
        int i1 = n + 1;
        this._inputPtr = i1;
        paramInt = arrayOfByte1[n] & 0xFF;
        i = j;
      }
      i2 = 0;
      i3 = 0;
      if (paramInt < 48)
        break label897;
      if (paramInt <= 57)
        break label597;
      i4 = i;
    }
    while (true)
    {
      label115: if (i2 == 0)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Missing integer part (next char ");
        String str1 = _getCharDesc(paramInt);
        String str2 = str1 + ")";
        reportInvalidNumber(str2);
      }
      int i5 = 0;
      int i6;
      if (paramInt == 46)
      {
        i6 = i4 + 1;
        int i7 = (char)paramInt;
        arrayOfChar1[i4] = i7;
        label188: int i8 = this._inputPtr;
        int i9 = this._inputEnd;
        if ((i8 >= i9) && (!loadMore()))
        {
          i3 = 1;
          label217: if (i5 == 0)
            reportUnexpectedNumberChar(paramInt, "Decimal point not followed by a digit");
        }
      }
      while (true)
      {
        int i10 = 0;
        int i18;
        if ((paramInt == 101) || (paramInt == 69))
        {
          int i11 = arrayOfChar1.length;
          if (i6 >= i11)
          {
            arrayOfChar1 = this._textBuffer.finishCurrentSegment();
            i6 = 0;
          }
          i4 = i6 + 1;
          int i12 = (char)paramInt;
          arrayOfChar1[i6] = i12;
          int i13 = this._inputPtr;
          int i14 = this._inputEnd;
          if (i13 >= i14)
            loadMoreGuaranteed();
          byte[] arrayOfByte2 = this._inputBuffer;
          int i15 = this._inputPtr;
          int i16 = i15 + 1;
          this._inputPtr = i16;
          paramInt = arrayOfByte2[i15] & 0xFF;
          if ((paramInt != 45) && (paramInt != 43))
            break label883;
          int i17 = arrayOfChar1.length;
          if (i4 < i17)
            break label876;
          arrayOfChar1 = this._textBuffer.finishCurrentSegment();
          i18 = 0;
          label373: i4 = i18 + 1;
          int i19 = (char)paramInt;
          arrayOfChar1[i18] = i19;
          int i20 = this._inputPtr;
          int i21 = this._inputEnd;
          if (i20 >= i21)
            loadMoreGuaranteed();
          byte[] arrayOfByte3 = this._inputBuffer;
          int i22 = this._inputPtr;
          int i23 = i22 + 1;
          this._inputPtr = i23;
          paramInt = arrayOfByte3[i22] & 0xFF;
          i6 = i4;
        }
        while (true)
        {
          if ((paramInt <= 57) && (paramInt >= 48))
          {
            i10 += 1;
            int i24 = arrayOfChar1.length;
            if (i6 >= i24)
              char[] arrayOfChar2 = this._textBuffer.finishCurrentSegment();
            i4 = i6 + 1;
            int i25 = (char)paramInt;
            arrayOfChar1[i6] = i25;
            int i26 = this._inputPtr;
            int i27 = this._inputEnd;
            if ((i26 >= i27) && (!loadMore()))
            {
              i3 = 1;
              int i28 = i4;
            }
          }
          else
          {
            if (i10 == 0)
              reportUnexpectedNumberChar(paramInt, "Exponent indicator not followed by a digit");
            if (i3 == 0)
            {
              int i29 = this._inputPtr + -1;
              this._inputPtr = i29;
            }
            this._textBuffer.setCurrentLength(i6);
            return reset(bool, i2, i5, i10);
            bool = false;
            break;
            label597: i2 += 1;
            if (i2 == 2)
            {
              int i30 = i + -1;
              if (arrayOfChar1[i30] == '0')
                reportInvalidNumber("Leading zeroes not allowed");
            }
            int i31 = arrayOfChar1.length;
            if (i >= i31)
            {
              arrayOfChar1 = this._textBuffer.finishCurrentSegment();
              i = 0;
            }
            j = i + 1;
            int i32 = (char)paramInt;
            arrayOfChar1[i] = i32;
            int i33 = this._inputPtr;
            int i34 = this._inputEnd;
            if ((i33 >= i34) && (!loadMore()))
            {
              paramInt = 0;
              i3 = 1;
              break label115;
            }
            byte[] arrayOfByte4 = this._inputBuffer;
            int i35 = this._inputPtr;
            int i36 = i35 + 1;
            this._inputPtr = i36;
            paramInt = arrayOfByte4[i35] & 0xFF;
            i = j;
            break label100;
            byte[] arrayOfByte5 = this._inputBuffer;
            int i37 = this._inputPtr;
            int i38 = i37 + 1;
            this._inputPtr = i38;
            paramInt = arrayOfByte5[i37] & 0xFF;
            if ((paramInt < 48) || (paramInt > 57))
              break label217;
            i5 += 1;
            int i39 = arrayOfChar1.length;
            if (i6 >= i39)
            {
              arrayOfChar1 = this._textBuffer.finishCurrentSegment();
              i6 = 0;
            }
            int i40 = i6 + 1;
            int i41 = (char)paramInt;
            arrayOfChar1[i6] = i41;
            i6 = i40;
            break label188;
          }
          byte[] arrayOfByte6 = this._inputBuffer;
          int i42 = this._inputPtr;
          int i43 = i42 + 1;
          this._inputPtr = i43;
          paramInt = arrayOfByte6[i42] & 0xFF;
          i6 = i4;
          continue;
          label876: i18 = i4;
          break label373;
          label883: i6 = i4;
        }
        i6 = i4;
      }
      label897: i4 = i;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.Utf8NumericParser
 * JD-Core Version:    0.6.2
 */