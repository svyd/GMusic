package com.google.api.client.json;

import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.DataUtil;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.GenericData;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonGenerator.Feature;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.JsonToken;

public class Json
{
  public static final JsonFactory JSON_FACTORY = localJsonFactory2.configure(localFeature1, false);

  static
  {
    JsonFactory localJsonFactory1 = new JsonFactory();
    JsonParser.Feature localFeature = JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS;
    JsonFactory localJsonFactory2 = localJsonFactory1.configure(localFeature, true);
    JsonGenerator.Feature localFeature1 = JsonGenerator.Feature.AUTO_CLOSE_TARGET;
  }

  public static <T> T parse(JsonParser paramJsonParser, Class<T> paramClass, CustomizeJsonParser paramCustomizeJsonParser)
    throws IOException
  {
    Object localObject = ClassInfo.newInstance(paramClass);
    parse(paramJsonParser, localObject, paramCustomizeJsonParser);
    return localObject;
  }

  public static void parse(JsonParser paramJsonParser, Object paramObject, CustomizeJsonParser paramCustomizeJsonParser)
    throws IOException
  {
    Object localObject1 = paramObject.getClass();
    ClassInfo localClassInfo = ClassInfo.of((Class)localObject1);
    boolean bool = GenericData.class.isAssignableFrom((Class)localObject1);
    JsonToken localJsonToken1;
    if ((!bool) && (Map.class.isAssignableFrom((Class)localObject1)))
    {
      Map localMap = (Map)paramObject;
      Class localClass1 = ClassInfo.getMapValueParameter(((Class)localObject1).getGenericSuperclass());
      parseMap(paramJsonParser, localMap, localClass1, paramCustomizeJsonParser);
      return;
      Field localField1 = ((FieldInfo)localObject1).field;
      Class localClass2 = ((FieldInfo)localObject1).type;
      JsonParser localJsonParser1 = paramJsonParser;
      Object localObject2 = paramObject;
      CustomizeJsonParser localCustomizeJsonParser1 = paramCustomizeJsonParser;
      Object localObject3 = parseValue(localJsonParser1, localJsonToken1, localField1, localClass2, localObject2, localCustomizeJsonParser1);
      FieldInfo.setFieldValue(localField1, paramObject, localObject3);
    }
    while (true)
    {
      JsonToken localJsonToken2 = paramJsonParser.nextToken();
      JsonToken localJsonToken3 = JsonToken.END_OBJECT;
      if (localJsonToken2 == localJsonToken3)
        return;
      String str = paramJsonParser.getCurrentName();
      localJsonToken1 = paramJsonParser.nextToken();
      if ((paramCustomizeJsonParser != null) && (paramCustomizeJsonParser.stopAt(paramObject, str)))
        return;
      localObject1 = localClassInfo.getFieldInfo(str);
      if (localObject1 != null)
      {
        if ((!((FieldInfo)localObject1).isFinal) || (((FieldInfo)localObject1).isPrimitive))
          break;
        throw new IllegalArgumentException("final array/object fields are not supported");
      }
      if (bool)
      {
        GenericData localGenericData = (GenericData)paramObject;
        JsonParser localJsonParser2 = paramJsonParser;
        Field localField2 = null;
        Class localClass3 = null;
        Object localObject4 = paramObject;
        CustomizeJsonParser localCustomizeJsonParser2 = paramCustomizeJsonParser;
        Object localObject5 = parseValue(localJsonParser2, localJsonToken1, localField2, localClass3, localObject4, localCustomizeJsonParser2);
        localGenericData.set(str, localObject5);
      }
      else
      {
        if (paramCustomizeJsonParser != null)
          paramCustomizeJsonParser.handleUnrecognizedKey(paramObject, str);
        JsonParser localJsonParser3 = paramJsonParser.skipChildren();
      }
    }
  }

  public static <T> T parseAndClose(JsonParser paramJsonParser, Class<T> paramClass, CustomizeJsonParser paramCustomizeJsonParser)
    throws IOException
  {
    Object localObject = ClassInfo.newInstance(paramClass);
    parseAndClose(paramJsonParser, localObject, paramCustomizeJsonParser);
    return localObject;
  }

  public static void parseAndClose(JsonParser paramJsonParser, Object paramObject, CustomizeJsonParser paramCustomizeJsonParser)
    throws IOException
  {
    try
    {
      parse(paramJsonParser, paramObject, paramCustomizeJsonParser);
      return;
    }
    finally
    {
      paramJsonParser.close();
    }
  }

  public static <T> void parseArray(JsonParser paramJsonParser, Collection<? super T> paramCollection, Class<T> paramClass, CustomizeJsonParser paramCustomizeJsonParser)
    throws IOException
  {
    while (true)
    {
      JsonToken localJsonToken1 = paramJsonParser.nextToken();
      JsonToken localJsonToken2 = JsonToken.END_ARRAY;
      if (localJsonToken1 == localJsonToken2)
        return;
      JsonParser localJsonParser = paramJsonParser;
      Class<T> localClass = paramClass;
      Collection<? super T> localCollection = paramCollection;
      CustomizeJsonParser localCustomizeJsonParser = paramCustomizeJsonParser;
      Object localObject = parseValue(localJsonParser, localJsonToken1, null, localClass, localCollection, localCustomizeJsonParser);
      boolean bool = paramCollection.add(localObject);
    }
  }

  private static void parseMap(JsonParser paramJsonParser, Map<String, Object> paramMap, Class<?> paramClass, CustomizeJsonParser paramCustomizeJsonParser)
    throws IOException
  {
    while (true)
    {
      JsonToken localJsonToken1 = paramJsonParser.nextToken();
      JsonToken localJsonToken2 = JsonToken.END_OBJECT;
      if (localJsonToken1 == localJsonToken2)
        return;
      String str = paramJsonParser.getCurrentName();
      JsonToken localJsonToken3 = paramJsonParser.nextToken();
      if ((paramCustomizeJsonParser != null) && (paramCustomizeJsonParser.stopAt(paramMap, str)))
        return;
      JsonParser localJsonParser = paramJsonParser;
      Class<?> localClass = paramClass;
      Map<String, Object> localMap = paramMap;
      CustomizeJsonParser localCustomizeJsonParser = paramCustomizeJsonParser;
      Object localObject1 = parseValue(localJsonParser, localJsonToken3, null, localClass, localMap, localCustomizeJsonParser);
      Object localObject2 = paramMap.put(str, localObject1);
    }
  }

  private static Object parseValue(JsonParser paramJsonParser, JsonToken paramJsonToken, Field paramField, Class<?> paramClass, Object paramObject, CustomizeJsonParser paramCustomizeJsonParser)
    throws IOException
  {
    Object localObject1 = null;
    int[] arrayOfInt = 1.$SwitchMap$org$codehaus$jackson$JsonToken;
    int i = paramJsonToken.ordinal();
    Object localObject2;
    switch (arrayOfInt[i])
    {
    default:
      StringBuilder localStringBuilder1 = new StringBuilder();
      String str1 = paramJsonParser.getCurrentName();
      String str2 = str1 + ": unexpected JSON node type: " + paramJsonToken;
      throw new IllegalArgumentException(str2);
    case 1:
      if ((paramClass == null) || (Collection.class.isAssignableFrom(paramClass)))
      {
        if ((paramCustomizeJsonParser != null) && (paramField != null))
          localObject1 = paramCustomizeJsonParser.newInstanceForArray(paramObject, paramField);
        if (localObject1 == null)
          localObject1 = ClassInfo.newCollectionInstance(paramClass);
        Class localClass1 = ClassInfo.getCollectionParameter(paramField);
        parseArray(paramJsonParser, (Collection)localObject1, localClass1, paramCustomizeJsonParser);
      }
    case 8:
      return localObject1;
      String str3 = "expected field type that implements Collection but got " + paramClass + " for field " + paramField;
      throw new IllegalArgumentException(str3);
    case 2:
      if ((paramClass == null) || (Map.class.isAssignableFrom(paramClass)))
      {
        localObject2 = null;
        label228: if ((paramClass == null) || (paramCustomizeJsonParser == null))
          break label776;
        localObject1 = paramCustomizeJsonParser.newInstanceForObject(paramObject, paramClass);
      }
      break;
    case 3:
    case 4:
    case 5:
    case 6:
    case 7:
    }
    label776: for (Object localObject3 = localObject1; ; localObject3 = null)
      while (true)
      {
        if (localObject3 == null)
        {
          if (localObject2 != null)
            Map localMap1 = ClassInfo.newMapInstance(paramClass);
        }
        else
        {
          label267: if ((localObject2 == null) || (paramClass == null))
            break label350;
          if (paramField == null)
            break label334;
          localObject1 = ClassInfo.getMapValueParameter(paramField);
        }
        for (localObject2 = localObject1; ; localObject2 = localObject1)
        {
          if (localObject2 == null)
            break label350;
          Map localMap2 = (Map)localObject3;
          parseMap(paramJsonParser, localMap2, localObject2, paramCustomizeJsonParser);
          Object localObject4 = localObject3;
          break;
          localObject2 = null;
          break label228;
          localObject3 = ClassInfo.newInstance(paramClass);
          break label267;
          label334: localObject1 = ClassInfo.getMapValueParameter(paramClass.getGenericSuperclass());
        }
        label350: parse(paramJsonParser, localObject3, paramCustomizeJsonParser);
        Object localObject5 = localObject3;
        break;
        if ((paramClass != null) && (paramClass != Boolean.class))
        {
          Class localClass2 = Boolean.TYPE;
          if (paramClass != localClass2)
          {
            StringBuilder localStringBuilder2 = new StringBuilder();
            String str4 = paramJsonParser.getCurrentName();
            String str5 = str4 + ": expected type Boolean or boolean but got " + paramClass + " for field " + paramField;
            throw new IllegalArgumentException(str5);
          }
        }
        JsonToken localJsonToken = JsonToken.VALUE_TRUE;
        if (paramJsonToken == localJsonToken)
        {
          Boolean localBoolean1 = Boolean.TRUE;
          break;
        }
        Boolean localBoolean2 = Boolean.FALSE;
        break;
        if ((paramClass != null) && (paramClass != Float.class))
        {
          Class localClass3 = Float.TYPE;
          if (paramClass != localClass3)
          {
            StringBuilder localStringBuilder3 = new StringBuilder();
            String str6 = paramJsonParser.getCurrentName();
            String str7 = str6 + ": expected type Float or float but got " + paramClass + " for field " + paramField;
            throw new IllegalArgumentException(str7);
          }
        }
        Float localFloat = Float.valueOf(paramJsonParser.getFloatValue());
        break;
        if ((paramClass != null) && (paramClass != Integer.class))
        {
          Class localClass4 = Integer.TYPE;
          if (paramClass != localClass4);
        }
        else
        {
          Integer localInteger = Integer.valueOf(paramJsonParser.getIntValue());
          break;
        }
        if (paramClass != Short.class)
        {
          Class localClass5 = Short.TYPE;
          if (paramClass != localClass5);
        }
        else
        {
          Short localShort = Short.valueOf(paramJsonParser.getShortValue());
          break;
        }
        if (paramClass != Byte.class)
        {
          Class localClass6 = Byte.TYPE;
          if (paramClass != localClass6);
        }
        else
        {
          Byte localByte = Byte.valueOf(paramJsonParser.getByteValue());
          break;
        }
        StringBuilder localStringBuilder4 = new StringBuilder();
        String str8 = paramJsonParser.getCurrentName();
        String str9 = str8 + ": expected type Integer/int/Short/short/Byte/byte but got " + paramClass + " for field " + paramField;
        throw new IllegalArgumentException(str9);
        try
        {
          String str10 = paramJsonParser.getText();
          Object localObject6 = FieldInfo.parsePrimitiveValue(paramClass, str10);
          Object localObject7 = localObject6;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          StringBuilder localStringBuilder5 = new StringBuilder();
          String str11 = paramJsonParser.getCurrentName();
          String str12 = str11 + " for field " + paramField;
          throw new IllegalArgumentException(str12, localIllegalArgumentException);
        }
      }
  }

  public static void serialize(JsonGenerator paramJsonGenerator, Object paramObject)
    throws IOException
  {
    if (paramObject == null)
      paramJsonGenerator.writeNull();
    if (((paramObject instanceof String)) || ((paramObject instanceof Long)) || ((paramObject instanceof Double)) || ((paramObject instanceof BigInteger)) || ((paramObject instanceof BigDecimal)))
    {
      String str1 = paramObject.toString();
      paramJsonGenerator.writeString(str1);
      return;
    }
    if ((paramObject instanceof Boolean))
    {
      boolean bool = ((Boolean)paramObject).booleanValue();
      paramJsonGenerator.writeBoolean(bool);
      return;
    }
    if (((paramObject instanceof Integer)) || ((paramObject instanceof Short)) || ((paramObject instanceof Byte)))
    {
      int i = ((Number)paramObject).intValue();
      paramJsonGenerator.writeNumber(i);
      return;
    }
    if ((paramObject instanceof Float))
    {
      float f = ((Float)paramObject).floatValue();
      paramJsonGenerator.writeNumber(f);
      return;
    }
    if ((paramObject instanceof DateTime))
    {
      String str2 = ((DateTime)paramObject).toStringRfc3339();
      paramJsonGenerator.writeString(str2);
      return;
    }
    if ((paramObject instanceof List))
    {
      paramJsonGenerator.writeStartArray();
      List localList = (List)paramObject;
      int j = localList.size();
      int k = 0;
      while (k < j)
      {
        Object localObject1 = localList.get(k);
        serialize(paramJsonGenerator, localObject1);
        k += 1;
      }
      paramJsonGenerator.writeEndArray();
      return;
    }
    paramJsonGenerator.writeStartObject();
    Iterator localIterator = DataUtil.mapOf(paramObject).entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Object localObject2 = localEntry.getValue();
      if (localObject2 != null)
      {
        String str3 = (String)localEntry.getKey();
        paramJsonGenerator.writeFieldName(str3);
        serialize(paramJsonGenerator, localObject2);
      }
    }
    paramJsonGenerator.writeEndObject();
  }

  public static String toString(Object paramObject)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      JsonFactory localJsonFactory = JSON_FACTORY;
      JsonEncoding localJsonEncoding = JsonEncoding.UTF8;
      JsonGenerator localJsonGenerator1 = localJsonFactory.createJsonGenerator(localByteArrayOutputStream, localJsonEncoding);
      JsonGenerator localJsonGenerator2 = localJsonGenerator1;
      try
      {
        serialize(localJsonGenerator2, paramObject);
        return localByteArrayOutputStream.toString();
      }
      finally
      {
        localJsonGenerator2.close();
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        PrintStream localPrintStream = new PrintStream(localByteArrayOutputStream);
        localIOException.printStackTrace(localPrintStream);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.json.Json
 * JD-Core Version:    0.6.2
 */