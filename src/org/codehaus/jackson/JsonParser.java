package org.codehaus.jackson;

import java.io.Closeable;
import java.io.IOException;

public abstract class JsonParser
  implements Closeable
{
  protected JsonToken _currToken;
  protected int _features;

  protected JsonParseException _constructError(String paramString)
  {
    JsonLocation localJsonLocation = getCurrentLocation();
    return new JsonParseException(paramString, localJsonLocation);
  }

  public abstract void close()
    throws IOException;

  public byte getByteValue()
    throws IOException, JsonParseException
  {
    int i = getIntValue();
    if ((i < 65408) || (i > 127))
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Numeric value (");
      String str1 = getText();
      String str2 = str1 + ") out of range of Java byte";
      throw _constructError(str2);
    }
    return (byte)i;
  }

  public abstract JsonLocation getCurrentLocation();

  public abstract String getCurrentName()
    throws IOException, JsonParseException;

  public abstract float getFloatValue()
    throws IOException, JsonParseException;

  public abstract int getIntValue()
    throws IOException, JsonParseException;

  public short getShortValue()
    throws IOException, JsonParseException
  {
    int i = getIntValue();
    if ((i < 32768) || (i > 32767))
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Numeric value (");
      String str1 = getText();
      String str2 = str1 + ") out of range of Java short";
      throw _constructError(str2);
    }
    return (short)i;
  }

  public abstract String getText()
    throws IOException, JsonParseException;

  public boolean isEnabled(Feature paramFeature)
  {
    int i = this._features;
    int j = paramFeature.getMask();
    if ((i & j) != 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public abstract JsonToken nextToken()
    throws IOException, JsonParseException;

  public abstract JsonParser skipChildren()
    throws IOException, JsonParseException;

  public static enum Feature
  {
    final boolean _defaultState;

    static
    {
      ALLOW_COMMENTS = new Feature("ALLOW_COMMENTS", 1, false);
      ALLOW_UNQUOTED_FIELD_NAMES = new Feature("ALLOW_UNQUOTED_FIELD_NAMES", 2, false);
      ALLOW_SINGLE_QUOTES = new Feature("ALLOW_SINGLE_QUOTES", 3, false);
      ALLOW_UNQUOTED_CONTROL_CHARS = new Feature("ALLOW_UNQUOTED_CONTROL_CHARS", 4, false);
      INTERN_FIELD_NAMES = new Feature("INTERN_FIELD_NAMES", 5, true);
      Feature[] arrayOfFeature = new Feature[6];
      Feature localFeature1 = AUTO_CLOSE_SOURCE;
      arrayOfFeature[0] = localFeature1;
      Feature localFeature2 = ALLOW_COMMENTS;
      arrayOfFeature[1] = localFeature2;
      Feature localFeature3 = ALLOW_UNQUOTED_FIELD_NAMES;
      arrayOfFeature[2] = localFeature3;
      Feature localFeature4 = ALLOW_SINGLE_QUOTES;
      arrayOfFeature[3] = localFeature4;
      Feature localFeature5 = ALLOW_UNQUOTED_CONTROL_CHARS;
      arrayOfFeature[4] = localFeature5;
      Feature localFeature6 = INTERN_FIELD_NAMES;
      arrayOfFeature[5] = localFeature6;
    }

    private Feature(boolean paramBoolean)
    {
      this._defaultState = paramBoolean;
    }

    public static int collectDefaults()
    {
      int i = 0;
      Feature[] arrayOfFeature = values();
      int j = arrayOfFeature.length;
      int k = 0;
      while (k < j)
      {
        Feature localFeature = arrayOfFeature[k];
        if (localFeature.enabledByDefault())
        {
          int m = localFeature.getMask();
          i |= m;
        }
        k += 1;
      }
      return i;
    }

    public boolean enabledByDefault()
    {
      return this._defaultState;
    }

    public boolean enabledIn(int paramInt)
    {
      if ((getMask() & paramInt) != 0);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public int getMask()
    {
      int i = ordinal();
      return 1 << i;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.JsonParser
 * JD-Core Version:    0.6.2
 */