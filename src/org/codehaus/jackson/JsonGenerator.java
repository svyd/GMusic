package org.codehaus.jackson;

import java.io.Closeable;
import java.io.IOException;

public abstract class JsonGenerator
  implements Closeable
{
  protected PrettyPrinter _cfgPrettyPrinter;

  public abstract void close()
    throws IOException;

  public abstract void writeBoolean(boolean paramBoolean)
    throws IOException, JsonGenerationException;

  public abstract void writeEndArray()
    throws IOException, JsonGenerationException;

  public abstract void writeEndObject()
    throws IOException, JsonGenerationException;

  public abstract void writeFieldName(String paramString)
    throws IOException, JsonGenerationException;

  public abstract void writeNull()
    throws IOException, JsonGenerationException;

  public abstract void writeNumber(float paramFloat)
    throws IOException, JsonGenerationException;

  public abstract void writeNumber(int paramInt)
    throws IOException, JsonGenerationException;

  public abstract void writeStartArray()
    throws IOException, JsonGenerationException;

  public abstract void writeStartObject()
    throws IOException, JsonGenerationException;

  public abstract void writeString(String paramString)
    throws IOException, JsonGenerationException;

  public static enum Feature
  {
    final boolean _defaultState;
    final int _mask;

    static
    {
      AUTO_CLOSE_JSON_CONTENT = new Feature("AUTO_CLOSE_JSON_CONTENT", 1, true);
      QUOTE_FIELD_NAMES = new Feature("QUOTE_FIELD_NAMES", 2, true);
      QUOTE_NON_NUMERIC_NUMBERS = new Feature("QUOTE_NON_NUMERIC_NUMBERS", 3, true);
      WRITE_NUMBERS_AS_STRINGS = new Feature("WRITE_NUMBERS_AS_STRINGS", 4, false);
      Feature[] arrayOfFeature = new Feature[5];
      Feature localFeature1 = AUTO_CLOSE_TARGET;
      arrayOfFeature[0] = localFeature1;
      Feature localFeature2 = AUTO_CLOSE_JSON_CONTENT;
      arrayOfFeature[1] = localFeature2;
      Feature localFeature3 = QUOTE_FIELD_NAMES;
      arrayOfFeature[2] = localFeature3;
      Feature localFeature4 = QUOTE_NON_NUMERIC_NUMBERS;
      arrayOfFeature[3] = localFeature4;
      Feature localFeature5 = WRITE_NUMBERS_AS_STRINGS;
      arrayOfFeature[4] = localFeature5;
    }

    private Feature(boolean paramBoolean)
    {
      this._defaultState = paramBoolean;
      int j = ordinal();
      int k = 1 << j;
      this._mask = k;
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

    public int getMask()
    {
      return this._mask;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.JsonGenerator
 * JD-Core Version:    0.6.2
 */