package com.google.android.music.cloudclient;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.common.io.Closeables;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class JsonUtils
{
  public static <T> T parseFromJsonData(Class<T> paramClass, byte[] paramArrayOfByte)
    throws IOException
  {
    try
    {
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramArrayOfByte);
      JsonToken localJsonToken = localJsonParser.nextToken();
      Object localObject = Json.parse(localJsonParser, paramClass, null);
      return localObject;
    }
    catch (JsonParseException localJsonParseException)
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("Failed to parse ");
      String str1 = paramClass.getSimpleName();
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(" :");
      String str2 = localJsonParseException.getMessage();
      String str3 = str2;
      throw new IOException(str3);
    }
  }

  public static <T> T parseFromJsonInputStream(Class<T> paramClass, InputStream paramInputStream)
    throws IOException
  {
    try
    {
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramInputStream);
      JsonToken localJsonToken = localJsonParser.nextToken();
      Object localObject = Json.parse(localJsonParser, paramClass, null);
      return localObject;
    }
    catch (JsonParseException localJsonParseException)
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("Failed to parse ");
      String str1 = paramClass.getSimpleName();
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(" :");
      String str2 = localJsonParseException.getMessage();
      String str3 = str2;
      throw new IOException(str3);
    }
  }

  public static <T> T parseFromJsonString(Class<T> paramClass, String paramString)
    throws IOException
  {
    try
    {
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramString);
      JsonToken localJsonToken = localJsonParser.nextToken();
      Object localObject = Json.parse(localJsonParser, paramClass, null);
      return localObject;
    }
    catch (JsonParseException localJsonParseException)
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("Failed to parse ");
      String str1 = paramClass.getSimpleName();
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(" :");
      String str2 = localJsonParseException.getMessage();
      String str3 = str2;
      throw new IOException(str3);
    }
  }

  public static byte[] toJsonByteArray(GenericJson paramGenericJson)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    JsonFactory localJsonFactory = Json.JSON_FACTORY;
    JsonEncoding localJsonEncoding = JsonEncoding.UTF8;
    JsonGenerator localJsonGenerator = localJsonFactory.createJsonGenerator(localByteArrayOutputStream, localJsonEncoding);
    try
    {
      Json.serialize(localJsonGenerator, paramGenericJson);
      return localByteArrayOutputStream.toByteArray();
    }
    finally
    {
      localJsonGenerator.close();
    }
  }

  public static String toJsonString(GenericJson paramGenericJson)
  {
    StringWriter localStringWriter = new StringWriter();
    try
    {
      localJsonGenerator = Json.JSON_FACTORY.createJsonGenerator(localStringWriter);
      Json.serialize(localJsonGenerator, paramGenericJson);
      return localStringWriter.toString();
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException("Failed to serialize json", localIOException);
    }
    finally
    {
      JsonGenerator localJsonGenerator;
      Closeables.closeQuietly(localJsonGenerator);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.JsonUtils
 * JD-Core Version:    0.6.2
 */