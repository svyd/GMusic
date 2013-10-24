package com.google.api.client.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.WeakHashMap;

public final class ClassInfo
{
  private static final ThreadLocal<WeakHashMap<Class<?>, ClassInfo>> CACHE = new ThreadLocal()
  {
    protected WeakHashMap<Class<?>, ClassInfo> initialValue()
    {
      return new WeakHashMap();
    }
  };
  public final Class<?> clazz;
  private final IdentityHashMap<String, FieldInfo> keyNameToFieldInfoMap;

  private ClassInfo(Class<?> paramClass)
  {
    this.clazz = paramClass;
    Class localClass = paramClass.getSuperclass();
    IdentityHashMap localIdentityHashMap1 = new IdentityHashMap();
    if (localClass != null)
    {
      IdentityHashMap localIdentityHashMap2 = of(localClass).keyNameToFieldInfoMap;
      if (localIdentityHashMap2 != null)
        localIdentityHashMap1.putAll(localIdentityHashMap2);
    }
    Field[] arrayOfField = paramClass.getDeclaredFields();
    int i = arrayOfField.length;
    int j = 0;
    if (j < i)
    {
      Field localField1 = arrayOfField[j];
      FieldInfo localFieldInfo1 = FieldInfo.of(localField1);
      if (localFieldInfo1 == null);
      while (true)
      {
        j += 1;
        break;
        String str1 = localFieldInfo1.name;
        FieldInfo localFieldInfo2 = (FieldInfo)localIdentityHashMap1.get(str1);
        if (localFieldInfo2 != null)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("two fields have the same data key name: ").append(localField1).append(" and ");
          Field localField2 = localFieldInfo2.field;
          String str2 = localField2;
          throw new IllegalArgumentException(str2);
        }
        Object localObject = localIdentityHashMap1.put(str1, localFieldInfo1);
      }
    }
    if (localIdentityHashMap1.isEmpty())
    {
      this.keyNameToFieldInfoMap = null;
      return;
    }
    this.keyNameToFieldInfoMap = localIdentityHashMap1;
  }

  public static Class<?> getCollectionParameter(Field paramField)
  {
    Type[] arrayOfType;
    if (paramField != null)
    {
      Type localType = paramField.getGenericType();
      if ((localType instanceof ParameterizedType))
      {
        arrayOfType = ((ParameterizedType)localType).getActualTypeArguments();
        if ((arrayOfType.length != 1) || (!(arrayOfType[0] instanceof Class)));
      }
    }
    for (Class localClass = (Class)arrayOfType[0]; ; localClass = null)
      return localClass;
  }

  public static Class<?> getMapValueParameter(Field paramField)
  {
    if (paramField != null);
    for (Class localClass = getMapValueParameter(paramField.getGenericType()); ; localClass = null)
      return localClass;
  }

  public static Class<?> getMapValueParameter(Type paramType)
  {
    Type[] arrayOfType;
    if ((paramType instanceof ParameterizedType))
    {
      arrayOfType = ((ParameterizedType)paramType).getActualTypeArguments();
      if ((arrayOfType.length != 2) || (!(arrayOfType[1] instanceof Class)));
    }
    for (Class localClass = (Class)arrayOfType[1]; ; localClass = null)
      return localClass;
  }

  private static IllegalArgumentException handleExceptionForNewInstance(Exception paramException, Class<?> paramClass)
  {
    StringBuilder localStringBuilder1 = new StringBuilder("unable to create new instance of class ");
    String str1 = paramClass.getName();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1);
    if (Modifier.isAbstract(paramClass.getModifiers()))
      StringBuilder localStringBuilder3 = localStringBuilder2.append(" (and) because it is abstract");
    if ((paramClass.getEnclosingClass() != null) && (!Modifier.isStatic(paramClass.getModifiers())))
      StringBuilder localStringBuilder4 = localStringBuilder2.append(" (and) because it is not static");
    if (!Modifier.isPublic(paramClass.getModifiers()))
      StringBuilder localStringBuilder5 = localStringBuilder2.append(" (and) because it is not public");
    while (true)
    {
      String str2 = localStringBuilder2.toString();
      throw new IllegalArgumentException(str2, paramException);
      int i = 0;
      try
      {
        Class[] arrayOfClass = new Class[i];
        Constructor localConstructor = paramClass.getConstructor(arrayOfClass);
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        StringBuilder localStringBuilder6 = localStringBuilder2.append(" (and) because it has no public default constructor");
      }
    }
  }

  public static Collection<Object> newCollectionInstance(Class<?> paramClass)
  {
    Object localObject;
    if ((paramClass == null) || (paramClass.isAssignableFrom(ArrayList.class)))
      localObject = new ArrayList();
    while (true)
    {
      return localObject;
      if ((paramClass.getModifiers() & 0x600) == 0)
      {
        localObject = (Collection)newInstance(paramClass);
      }
      else if (paramClass.isAssignableFrom(HashSet.class))
      {
        localObject = new HashSet();
      }
      else
      {
        if (!paramClass.isAssignableFrom(TreeSet.class))
          break;
        localObject = new TreeSet();
      }
    }
    StringBuilder localStringBuilder = new StringBuilder().append("no default collection class defined for class: ");
    String str1 = paramClass.getName();
    String str2 = str1;
    throw new IllegalArgumentException(str2);
  }

  // ERROR //
  public static <T> T newInstance(Class<T> paramClass)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 190	java/lang/Class:newInstance	()Ljava/lang/Object;
    //   4: astore_1
    //   5: aload_1
    //   6: areturn
    //   7: aload_0
    //   8: invokestatic 192	com/google/api/client/util/ClassInfo:handleExceptionForNewInstance	(Ljava/lang/Exception;Ljava/lang/Class;)Ljava/lang/IllegalArgumentException;
    //   11: athrow
    //   12: aload_0
    //   13: invokestatic 192	com/google/api/client/util/ClassInfo:handleExceptionForNewInstance	(Ljava/lang/Exception;Ljava/lang/Class;)Ljava/lang/IllegalArgumentException;
    //   16: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   0	5	7	java/lang/IllegalAccessException
    //   0	5	12	java/lang/InstantiationException
  }

  public static Map<String, Object> newMapInstance(Class<?> paramClass)
  {
    Object localObject;
    if ((paramClass != null) && ((paramClass.getModifiers() & 0x600) == 0))
      localObject = (Map)newInstance(paramClass);
    while (true)
    {
      return localObject;
      if ((paramClass == null) || (paramClass.isAssignableFrom(ArrayMap.class)))
      {
        localObject = ArrayMap.create();
      }
      else
      {
        if ((paramClass != null) && (!paramClass.isAssignableFrom(TreeMap.class)))
          break;
        localObject = new TreeMap();
      }
    }
    StringBuilder localStringBuilder = new StringBuilder().append("no default map class defined for class: ");
    String str1 = paramClass.getName();
    String str2 = str1;
    throw new IllegalArgumentException(str2);
  }

  public static ClassInfo of(Class<?> paramClass)
  {
    ClassInfo localClassInfo;
    if (paramClass == null)
      localClassInfo = null;
    while (true)
    {
      return localClassInfo;
      WeakHashMap localWeakHashMap = (WeakHashMap)CACHE.get();
      localClassInfo = (ClassInfo)localWeakHashMap.get(paramClass);
      if (localClassInfo == null)
      {
        localClassInfo = new ClassInfo(paramClass);
        Object localObject = localWeakHashMap.put(paramClass, localClassInfo);
      }
    }
  }

  public FieldInfo getFieldInfo(String paramString)
  {
    FieldInfo localFieldInfo = null;
    if (paramString == null);
    while (true)
    {
      return localFieldInfo;
      IdentityHashMap localIdentityHashMap = this.keyNameToFieldInfoMap;
      if (localIdentityHashMap != null)
      {
        String str = paramString.intern();
        localFieldInfo = (FieldInfo)localIdentityHashMap.get(str);
      }
    }
  }

  public int getKeyCount()
  {
    IdentityHashMap localIdentityHashMap = this.keyNameToFieldInfoMap;
    if (localIdentityHashMap == null);
    for (int i = 0; ; i = localIdentityHashMap.size())
      return i;
  }

  public Collection<String> getKeyNames()
  {
    IdentityHashMap localIdentityHashMap = this.keyNameToFieldInfoMap;
    if (localIdentityHashMap == null);
    for (Set localSet = Collections.emptySet(); ; localSet = Collections.unmodifiableSet(localIdentityHashMap.keySet()))
      return localSet;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.util.ClassInfo
 * JD-Core Version:    0.6.2
 */