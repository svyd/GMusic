package org.codehaus.jackson;

public enum JsonToken
{
  final String _serialized;
  final byte[] _serializedBytes;
  final char[] _serializedChars;

  static
  {
    END_OBJECT = new JsonToken("END_OBJECT", 2, "}");
    START_ARRAY = new JsonToken("START_ARRAY", 3, "[");
    END_ARRAY = new JsonToken("END_ARRAY", 4, "]");
    FIELD_NAME = new JsonToken("FIELD_NAME", 5, null);
    VALUE_EMBEDDED_OBJECT = new JsonToken("VALUE_EMBEDDED_OBJECT", 6, null);
    VALUE_STRING = new JsonToken("VALUE_STRING", 7, null);
    VALUE_NUMBER_INT = new JsonToken("VALUE_NUMBER_INT", 8, null);
    VALUE_NUMBER_FLOAT = new JsonToken("VALUE_NUMBER_FLOAT", 9, null);
    VALUE_TRUE = new JsonToken("VALUE_TRUE", 10, "true");
    VALUE_FALSE = new JsonToken("VALUE_FALSE", 11, "false");
    VALUE_NULL = new JsonToken("VALUE_NULL", 12, "null");
    JsonToken[] arrayOfJsonToken = new JsonToken[13];
    JsonToken localJsonToken1 = NOT_AVAILABLE;
    arrayOfJsonToken[0] = localJsonToken1;
    JsonToken localJsonToken2 = START_OBJECT;
    arrayOfJsonToken[1] = localJsonToken2;
    JsonToken localJsonToken3 = END_OBJECT;
    arrayOfJsonToken[2] = localJsonToken3;
    JsonToken localJsonToken4 = START_ARRAY;
    arrayOfJsonToken[3] = localJsonToken4;
    JsonToken localJsonToken5 = END_ARRAY;
    arrayOfJsonToken[4] = localJsonToken5;
    JsonToken localJsonToken6 = FIELD_NAME;
    arrayOfJsonToken[5] = localJsonToken6;
    JsonToken localJsonToken7 = VALUE_EMBEDDED_OBJECT;
    arrayOfJsonToken[6] = localJsonToken7;
    JsonToken localJsonToken8 = VALUE_STRING;
    arrayOfJsonToken[7] = localJsonToken8;
    JsonToken localJsonToken9 = VALUE_NUMBER_INT;
    arrayOfJsonToken[8] = localJsonToken9;
    JsonToken localJsonToken10 = VALUE_NUMBER_FLOAT;
    arrayOfJsonToken[9] = localJsonToken10;
    JsonToken localJsonToken11 = VALUE_TRUE;
    arrayOfJsonToken[10] = localJsonToken11;
    JsonToken localJsonToken12 = VALUE_FALSE;
    arrayOfJsonToken[11] = localJsonToken12;
    JsonToken localJsonToken13 = VALUE_NULL;
    arrayOfJsonToken[12] = localJsonToken13;
  }

  private JsonToken(String paramString)
  {
    if (paramString == null)
    {
      this._serialized = null;
      this._serializedChars = null;
      this._serializedBytes = null;
      return;
    }
    this._serialized = paramString;
    char[] arrayOfChar = paramString.toCharArray();
    this._serializedChars = arrayOfChar;
    int j = this._serializedChars.length;
    byte[] arrayOfByte1 = new byte[j];
    this._serializedBytes = arrayOfByte1;
    int k = 0;
    while (true)
    {
      if (k >= j)
        return;
      byte[] arrayOfByte2 = this._serializedBytes;
      int m = (byte)this._serializedChars[k];
      arrayOfByte2[k] = m;
      k += 1;
    }
  }

  public byte[] asByteArray()
  {
    return this._serializedBytes;
  }

  public String asString()
  {
    return this._serialized;
  }

  public boolean isNumeric()
  {
    JsonToken localJsonToken1 = VALUE_NUMBER_INT;
    if (this != localJsonToken1)
    {
      JsonToken localJsonToken2 = VALUE_NUMBER_FLOAT;
      if (this != localJsonToken2)
        break label22;
    }
    label22: for (boolean bool = true; ; bool = false)
      return bool;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.JsonToken
 * JD-Core Version:    0.6.2
 */