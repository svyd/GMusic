package org.codehaus.jackson.impl;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.PrettyPrinter;

public abstract class JsonGeneratorBase extends JsonGenerator
{
  protected boolean _cfgNumbersAsStrings;
  protected boolean _closed;
  protected int _features;
  protected ObjectCodec _objectCodec;
  protected JsonWriteContext _writeContext;

  protected JsonGeneratorBase(int paramInt, ObjectCodec paramObjectCodec)
  {
    this._features = paramInt;
    JsonWriteContext localJsonWriteContext = JsonWriteContext.createRootContext();
    this._writeContext = localJsonWriteContext;
    this._objectCodec = paramObjectCodec;
    JsonGenerator.Feature localFeature = JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS;
    boolean bool = isEnabled(localFeature);
    this._cfgNumbersAsStrings = bool;
  }

  protected void _cantHappen()
  {
    throw new RuntimeException("Internal error: should never end up through this code path");
  }

  protected void _reportError(String paramString)
    throws JsonGenerationException
  {
    throw new JsonGenerationException(paramString);
  }

  protected abstract void _verifyValueWrite(String paramString)
    throws IOException, JsonGenerationException;

  protected abstract void _writeEndArray()
    throws IOException, JsonGenerationException;

  protected abstract void _writeEndObject()
    throws IOException, JsonGenerationException;

  protected abstract void _writeFieldName(String paramString, boolean paramBoolean)
    throws IOException, JsonGenerationException;

  protected abstract void _writeStartArray()
    throws IOException, JsonGenerationException;

  protected abstract void _writeStartObject()
    throws IOException, JsonGenerationException;

  public void close()
    throws IOException
  {
    this._closed = true;
  }

  public final JsonWriteContext getOutputContext()
  {
    return this._writeContext;
  }

  public final boolean isEnabled(JsonGenerator.Feature paramFeature)
  {
    int i = this._features;
    int j = paramFeature.getMask();
    if ((i & j) != 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public final void writeEndArray()
    throws IOException, JsonGenerationException
  {
    if (!this._writeContext.inArray())
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Current context not an ARRAY but ");
      String str1 = this._writeContext.getTypeDesc();
      String str2 = str1;
      _reportError(str2);
    }
    if (this._cfgPrettyPrinter != null)
    {
      PrettyPrinter localPrettyPrinter = this._cfgPrettyPrinter;
      int i = this._writeContext.getEntryCount();
      localPrettyPrinter.writeEndArray(this, i);
    }
    while (true)
    {
      JsonWriteContext localJsonWriteContext = this._writeContext.getParent();
      this._writeContext = localJsonWriteContext;
      return;
      _writeEndArray();
    }
  }

  public final void writeEndObject()
    throws IOException, JsonGenerationException
  {
    if (!this._writeContext.inObject())
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Current context not an object but ");
      String str1 = this._writeContext.getTypeDesc();
      String str2 = str1;
      _reportError(str2);
    }
    JsonWriteContext localJsonWriteContext = this._writeContext.getParent();
    this._writeContext = localJsonWriteContext;
    if (this._cfgPrettyPrinter != null)
    {
      PrettyPrinter localPrettyPrinter = this._cfgPrettyPrinter;
      int i = this._writeContext.getEntryCount();
      localPrettyPrinter.writeEndObject(this, i);
      return;
    }
    _writeEndObject();
  }

  public final void writeFieldName(String paramString)
    throws IOException, JsonGenerationException
  {
    boolean bool = true;
    int i = this._writeContext.writeFieldName(paramString);
    if (i == 4)
      _reportError("Can not write a field name, expecting a value");
    if (i == 1);
    while (true)
    {
      _writeFieldName(paramString, bool);
      return;
      bool = false;
    }
  }

  public final void writeStartArray()
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("start an array");
    JsonWriteContext localJsonWriteContext = this._writeContext.createChildArrayContext();
    this._writeContext = localJsonWriteContext;
    if (this._cfgPrettyPrinter != null)
    {
      this._cfgPrettyPrinter.writeStartArray(this);
      return;
    }
    _writeStartArray();
  }

  public final void writeStartObject()
    throws IOException, JsonGenerationException
  {
    _verifyValueWrite("start an object");
    JsonWriteContext localJsonWriteContext = this._writeContext.createChildObjectContext();
    this._writeContext = localJsonWriteContext;
    if (this._cfgPrettyPrinter != null)
    {
      this._cfgPrettyPrinter.writeStartObject(this);
      return;
    }
    _writeStartObject();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.JsonGeneratorBase
 * JD-Core Version:    0.6.2
 */