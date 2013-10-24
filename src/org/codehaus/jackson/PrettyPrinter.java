package org.codehaus.jackson;

import java.io.IOException;

public abstract interface PrettyPrinter
{
  public abstract void beforeArrayValues(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;

  public abstract void beforeObjectEntries(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;

  public abstract void writeArrayValueSeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;

  public abstract void writeEndArray(JsonGenerator paramJsonGenerator, int paramInt)
    throws IOException, JsonGenerationException;

  public abstract void writeEndObject(JsonGenerator paramJsonGenerator, int paramInt)
    throws IOException, JsonGenerationException;

  public abstract void writeObjectEntrySeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;

  public abstract void writeObjectFieldValueSeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;

  public abstract void writeRootValueSeparator(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;

  public abstract void writeStartArray(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;

  public abstract void writeStartObject(JsonGenerator paramJsonGenerator)
    throws IOException, JsonGenerationException;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.PrettyPrinter
 * JD-Core Version:    0.6.2
 */