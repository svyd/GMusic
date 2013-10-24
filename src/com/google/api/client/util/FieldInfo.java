package com.google.api.client.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.WeakHashMap;

public class FieldInfo
{
  private static final ThreadLocal<WeakHashMap<Field, FieldInfo>> CACHE = new ThreadLocal()
  {
    protected WeakHashMap<Field, FieldInfo> initialValue()
    {
      return new WeakHashMap();
    }
  };
  public final Field field;
  public final boolean isFinal;
  public final boolean isPrimitive;
  public final String name;
  public final Class<?> type;

  FieldInfo(Field paramField, String paramString)
  {
    this.field = paramField;
    String str = paramString.intern();
    this.name = str;
    boolean bool1 = Modifier.isFinal(paramField.getModifiers());
    this.isFinal = bool1;
    Class localClass = paramField.getType();
    this.type = localClass;
    boolean bool2 = isPrimitive(localClass);
    this.isPrimitive = bool2;
  }

  public static Object getFieldValue(Field paramField, Object paramObject)
  {
    try
    {
      Object localObject = paramField.get(paramObject);
      return localObject;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      throw new IllegalArgumentException(localIllegalAccessException);
    }
  }

  public static boolean isPrimitive(Class<?> paramClass)
  {
    if ((paramClass.isPrimitive()) || (paramClass == Character.class) || (paramClass == String.class) || (paramClass == Integer.class) || (paramClass == Long.class) || (paramClass == Short.class) || (paramClass == Byte.class) || (paramClass == Float.class) || (paramClass == Double.class) || (paramClass == BigInteger.class) || (paramClass == BigDecimal.class) || (paramClass == DateTime.class) || (paramClass == Boolean.class));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static boolean isPrimitive(Object paramObject)
  {
    if ((paramObject == null) || (isPrimitive(paramObject.getClass())));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static FieldInfo of(Field paramField)
  {
    Object localObject1;
    if (paramField == null)
      localObject1 = null;
    while (true)
    {
      return localObject1;
      WeakHashMap localWeakHashMap = (WeakHashMap)CACHE.get();
      localObject1 = (FieldInfo)localWeakHashMap.get(paramField);
      if ((localObject1 == null) && (!Modifier.isStatic(paramField.getModifiers())))
      {
        Key localKey = (Key)paramField.getAnnotation(Key.class);
        if (localKey == null)
        {
          localObject1 = null;
        }
        else
        {
          String str = localKey.value();
          if ("##default".equals(str))
            str = paramField.getName();
          FieldInfo localFieldInfo = new FieldInfo(paramField, str);
          Object localObject2 = localWeakHashMap.put(paramField, localFieldInfo);
          paramField.setAccessible(true);
          localObject1 = localFieldInfo;
        }
      }
    }
  }

  public static Object parsePrimitiveValue(Class<?> paramClass, String paramString)
  {
    if ((paramString == null) || (paramClass == null) || (paramClass == String.class));
    while (true)
    {
      return paramString;
      if (paramClass != Character.class)
      {
        Class localClass1 = Character.TYPE;
        if (paramClass != localClass1);
      }
      else
      {
        if (paramString.length() != 1)
        {
          String str1 = "expected type Character/char but got " + paramClass;
          throw new IllegalArgumentException(str1);
        }
        paramString = Character.valueOf(paramString.charAt(0));
        continue;
      }
      if (paramClass != Boolean.class)
      {
        Class localClass2 = Boolean.TYPE;
        if (paramClass != localClass2);
      }
      else
      {
        paramString = Boolean.valueOf(paramString);
        continue;
      }
      if (paramClass != Byte.class)
      {
        Class localClass3 = Byte.TYPE;
        if (paramClass != localClass3);
      }
      else
      {
        paramString = Byte.valueOf(paramString);
        continue;
      }
      if (paramClass != Short.class)
      {
        Class localClass4 = Short.TYPE;
        if (paramClass != localClass4);
      }
      else
      {
        paramString = Short.valueOf(paramString);
        continue;
      }
      if (paramClass != Integer.class)
      {
        Class localClass5 = Integer.TYPE;
        if (paramClass != localClass5);
      }
      else
      {
        paramString = Integer.valueOf(paramString);
        continue;
      }
      if (paramClass != Long.class)
      {
        Class localClass6 = Long.TYPE;
        if (paramClass != localClass6);
      }
      else
      {
        paramString = Long.valueOf(paramString);
        continue;
      }
      if (paramClass != Float.class)
      {
        Class localClass7 = Float.TYPE;
        if (paramClass != localClass7);
      }
      else
      {
        paramString = Float.valueOf(paramString);
        continue;
      }
      if (paramClass != Double.class)
      {
        Class localClass8 = Double.TYPE;
        if (paramClass != localClass8);
      }
      else
      {
        paramString = Double.valueOf(paramString);
        continue;
      }
      if (paramClass == DateTime.class)
      {
        paramString = DateTime.parseRfc3339(paramString);
      }
      else if (paramClass == BigInteger.class)
      {
        paramString = new BigInteger(paramString);
      }
      else
      {
        if (paramClass != BigDecimal.class)
          break;
        paramString = new BigDecimal(paramString);
      }
    }
    String str2 = "expected primitive class, but got: " + paramClass;
    throw new IllegalArgumentException(str2);
  }

  public static void setFieldValue(Field paramField, Object paramObject1, Object paramObject2)
  {
    if (Modifier.isFinal(paramField.getModifiers()))
    {
      Object localObject = getFieldValue(paramField, paramObject1);
      if (paramObject2 == null)
        if (localObject == null)
          return;
      while (!paramObject2.equals(localObject))
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("expected final value <").append(localObject).append("> but was <").append(paramObject2).append("> on ");
        String str1 = paramField.getName();
        StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(" field in ");
        String str2 = paramObject1.getClass().getName();
        String str3 = str2;
        throw new IllegalArgumentException(str3);
      }
      return;
    }
    try
    {
      paramField.set(paramObject1, paramObject2);
      return;
    }
    catch (SecurityException localSecurityException)
    {
      throw new IllegalArgumentException(localSecurityException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      throw new IllegalArgumentException(localIllegalAccessException);
    }
  }

  public Object getValue(Object paramObject)
  {
    return getFieldValue(this.field, paramObject);
  }

  public void setValue(Object paramObject1, Object paramObject2)
  {
    setFieldValue(this.field, paramObject1, paramObject2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.util.FieldInfo
 * JD-Core Version:    0.6.2
 */