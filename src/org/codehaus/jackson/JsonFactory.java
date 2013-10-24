package org.codehaus.jackson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.lang.ref.SoftReference;
import org.codehaus.jackson.impl.ByteSourceBootstrapper;
import org.codehaus.jackson.impl.ReaderBasedParser;
import org.codehaus.jackson.impl.WriterBasedGenerator;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.io.UTF8Writer;
import org.codehaus.jackson.sym.BytesToNameCanonicalizer;
import org.codehaus.jackson.sym.CharsToNameCanonicalizer;
import org.codehaus.jackson.util.BufferRecycler;

public class JsonFactory
{
  static final int DEFAULT_GENERATOR_FEATURE_FLAGS = JsonGenerator.Feature.collectDefaults();
  static final int DEFAULT_PARSER_FEATURE_FLAGS = JsonParser.Feature.collectDefaults();
  static final ThreadLocal<SoftReference<BufferRecycler>> _recyclerRef = new ThreadLocal();
  protected int _generatorFeatures;
  protected ObjectCodec _objectCodec;
  protected int _parserFeatures;
  protected BytesToNameCanonicalizer _rootByteSymbols;
  protected CharsToNameCanonicalizer _rootCharSymbols;

  public JsonFactory()
  {
    this(null);
  }

  public JsonFactory(ObjectCodec paramObjectCodec)
  {
    CharsToNameCanonicalizer localCharsToNameCanonicalizer = CharsToNameCanonicalizer.createRoot(true);
    this._rootCharSymbols = localCharsToNameCanonicalizer;
    BytesToNameCanonicalizer localBytesToNameCanonicalizer = BytesToNameCanonicalizer.createRoot();
    this._rootByteSymbols = localBytesToNameCanonicalizer;
    int i = DEFAULT_PARSER_FEATURE_FLAGS;
    this._parserFeatures = i;
    int j = DEFAULT_GENERATOR_FEATURE_FLAGS;
    this._generatorFeatures = j;
    this._objectCodec = paramObjectCodec;
  }

  protected IOContext _createContext(Object paramObject, boolean paramBoolean)
  {
    BufferRecycler localBufferRecycler = _getBufferRecycler();
    return new IOContext(localBufferRecycler, paramObject, paramBoolean);
  }

  protected JsonGenerator _createJsonGenerator(Writer paramWriter, IOContext paramIOContext)
    throws IOException
  {
    int i = this._generatorFeatures;
    ObjectCodec localObjectCodec = this._objectCodec;
    return new WriterBasedGenerator(paramIOContext, i, localObjectCodec, paramWriter);
  }

  protected JsonParser _createJsonParser(InputStream paramInputStream, IOContext paramIOContext)
    throws IOException, JsonParseException
  {
    ByteSourceBootstrapper localByteSourceBootstrapper = new ByteSourceBootstrapper(paramIOContext, paramInputStream);
    int i = this._parserFeatures;
    ObjectCodec localObjectCodec = this._objectCodec;
    BytesToNameCanonicalizer localBytesToNameCanonicalizer = this._rootByteSymbols;
    CharsToNameCanonicalizer localCharsToNameCanonicalizer = this._rootCharSymbols;
    return localByteSourceBootstrapper.constructParser(i, localObjectCodec, localBytesToNameCanonicalizer, localCharsToNameCanonicalizer);
  }

  protected JsonParser _createJsonParser(Reader paramReader, IOContext paramIOContext)
    throws IOException, JsonParseException
  {
    int i = this._parserFeatures;
    ObjectCodec localObjectCodec = this._objectCodec;
    CharsToNameCanonicalizer localCharsToNameCanonicalizer1 = this._rootCharSymbols;
    JsonParser.Feature localFeature = JsonParser.Feature.INTERN_FIELD_NAMES;
    boolean bool = isEnabled(localFeature);
    CharsToNameCanonicalizer localCharsToNameCanonicalizer2 = localCharsToNameCanonicalizer1.makeChild(bool);
    IOContext localIOContext = paramIOContext;
    Reader localReader = paramReader;
    return new ReaderBasedParser(localIOContext, i, localReader, localObjectCodec, localCharsToNameCanonicalizer2);
  }

  protected JsonParser _createJsonParser(byte[] paramArrayOfByte, int paramInt1, int paramInt2, IOContext paramIOContext)
    throws IOException, JsonParseException
  {
    ByteSourceBootstrapper localByteSourceBootstrapper = new ByteSourceBootstrapper(paramIOContext, paramArrayOfByte, paramInt1, paramInt2);
    int i = this._parserFeatures;
    ObjectCodec localObjectCodec = this._objectCodec;
    BytesToNameCanonicalizer localBytesToNameCanonicalizer = this._rootByteSymbols;
    CharsToNameCanonicalizer localCharsToNameCanonicalizer = this._rootCharSymbols;
    return localByteSourceBootstrapper.constructParser(i, localObjectCodec, localBytesToNameCanonicalizer, localCharsToNameCanonicalizer);
  }

  protected Writer _createWriter(OutputStream paramOutputStream, JsonEncoding paramJsonEncoding, IOContext paramIOContext)
    throws IOException
  {
    JsonEncoding localJsonEncoding = JsonEncoding.UTF8;
    if (paramJsonEncoding == localJsonEncoding);
    String str;
    for (Object localObject = new UTF8Writer(paramIOContext, paramOutputStream); ; localObject = new OutputStreamWriter(paramOutputStream, str))
    {
      return localObject;
      str = paramJsonEncoding.getJavaName();
    }
  }

  public BufferRecycler _getBufferRecycler()
  {
    SoftReference localSoftReference1 = (SoftReference)_recyclerRef.get();
    if (localSoftReference1 == null);
    for (BufferRecycler localBufferRecycler = null; ; localBufferRecycler = (BufferRecycler)localSoftReference1.get())
    {
      if (localBufferRecycler == null)
      {
        localBufferRecycler = new BufferRecycler();
        if (localSoftReference1 == null)
        {
          ThreadLocal localThreadLocal = _recyclerRef;
          SoftReference localSoftReference2 = new SoftReference(localBufferRecycler);
          localThreadLocal.set(localSoftReference2);
        }
      }
      return localBufferRecycler;
    }
  }

  public final JsonFactory configure(JsonGenerator.Feature paramFeature, boolean paramBoolean)
  {
    if (paramBoolean)
      JsonFactory localJsonFactory1 = enable(paramFeature);
    while (true)
    {
      return this;
      JsonFactory localJsonFactory2 = disable(paramFeature);
    }
  }

  public final JsonFactory configure(JsonParser.Feature paramFeature, boolean paramBoolean)
  {
    if (paramBoolean)
      JsonFactory localJsonFactory1 = enable(paramFeature);
    while (true)
    {
      return this;
      JsonFactory localJsonFactory2 = disable(paramFeature);
    }
  }

  public JsonGenerator createJsonGenerator(OutputStream paramOutputStream, JsonEncoding paramJsonEncoding)
    throws IOException
  {
    IOContext localIOContext = _createContext(paramOutputStream, false);
    localIOContext.setEncoding(paramJsonEncoding);
    Writer localWriter = _createWriter(paramOutputStream, paramJsonEncoding, localIOContext);
    return _createJsonGenerator(localWriter, localIOContext);
  }

  public JsonGenerator createJsonGenerator(Writer paramWriter)
    throws IOException
  {
    IOContext localIOContext = _createContext(paramWriter, false);
    return _createJsonGenerator(paramWriter, localIOContext);
  }

  public JsonParser createJsonParser(InputStream paramInputStream)
    throws IOException, JsonParseException
  {
    IOContext localIOContext = _createContext(paramInputStream, false);
    return _createJsonParser(paramInputStream, localIOContext);
  }

  public JsonParser createJsonParser(String paramString)
    throws IOException, JsonParseException
  {
    StringReader localStringReader = new StringReader(paramString);
    IOContext localIOContext = _createContext(localStringReader, true);
    return _createJsonParser(localStringReader, localIOContext);
  }

  public JsonParser createJsonParser(byte[] paramArrayOfByte)
    throws IOException, JsonParseException
  {
    int i = paramArrayOfByte.length;
    IOContext localIOContext = _createContext(paramArrayOfByte, true);
    return _createJsonParser(paramArrayOfByte, 0, i, localIOContext);
  }

  public JsonFactory disable(JsonGenerator.Feature paramFeature)
  {
    int i = this._generatorFeatures;
    int j = paramFeature.getMask() ^ 0xFFFFFFFF;
    int k = i & j;
    this._generatorFeatures = k;
    return this;
  }

  public JsonFactory disable(JsonParser.Feature paramFeature)
  {
    int i = this._parserFeatures;
    int j = paramFeature.getMask() ^ 0xFFFFFFFF;
    int k = i & j;
    this._parserFeatures = k;
    return this;
  }

  public JsonFactory enable(JsonGenerator.Feature paramFeature)
  {
    int i = this._generatorFeatures;
    int j = paramFeature.getMask();
    int k = i | j;
    this._generatorFeatures = k;
    return this;
  }

  public JsonFactory enable(JsonParser.Feature paramFeature)
  {
    int i = this._parserFeatures;
    int j = paramFeature.getMask();
    int k = i | j;
    this._parserFeatures = k;
    return this;
  }

  public final boolean isEnabled(JsonParser.Feature paramFeature)
  {
    int i = this._parserFeatures;
    int j = paramFeature.getMask();
    if ((i & j) != 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.JsonFactory
 * JD-Core Version:    0.6.2
 */