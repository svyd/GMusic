package com.google.api.client.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

public final class ReflectionMap extends AbstractMap<String, Object>
{
  private final ClassInfo classInfo;
  private EntrySet entrySet;
  private final Object object;
  private final int size;

  public ReflectionMap(Object paramObject)
  {
    this.object = paramObject;
    ClassInfo localClassInfo = ClassInfo.of(paramObject.getClass());
    this.classInfo = localClassInfo;
    int i = localClassInfo.getKeyCount();
    this.size = i;
  }

  public Set<Map.Entry<String, Object>> entrySet()
  {
    EntrySet localEntrySet = this.entrySet;
    if (localEntrySet == null)
    {
      localEntrySet = new EntrySet();
      this.entrySet = localEntrySet;
    }
    return localEntrySet;
  }

  static final class Entry
    implements Map.Entry<String, Object>
  {
    private final ClassInfo classInfo;
    private final String fieldName;
    private Object fieldValue;
    private boolean isFieldValueComputed;
    private final Object object;

    public Entry(Object paramObject, String paramString)
    {
      ClassInfo localClassInfo = ClassInfo.of(paramObject.getClass());
      this.classInfo = localClassInfo;
      this.object = paramObject;
      this.fieldName = paramString;
    }

    public String getKey()
    {
      return this.fieldName;
    }

    public Object getValue()
    {
      Object localObject1;
      if (this.isFieldValueComputed)
        localObject1 = this.fieldValue;
      while (true)
      {
        return localObject1;
        this.isFieldValueComputed = true;
        ClassInfo localClassInfo = this.classInfo;
        String str = this.fieldName;
        FieldInfo localFieldInfo = localClassInfo.getFieldInfo(str);
        Object localObject2 = this.object;
        localObject1 = localFieldInfo.getValue(localObject2);
        this.fieldValue = localObject1;
      }
    }

    public Object setValue(Object paramObject)
    {
      ClassInfo localClassInfo = this.classInfo;
      String str = this.fieldName;
      FieldInfo localFieldInfo = localClassInfo.getFieldInfo(str);
      Object localObject1 = getValue();
      Object localObject2 = this.object;
      localFieldInfo.setValue(localObject2, paramObject);
      this.fieldValue = paramObject;
      return localObject1;
    }
  }

  static final class EntryIterator
    implements Iterator<Map.Entry<String, Object>>
  {
    final ClassInfo classInfo;
    private int fieldIndex = 0;
    private final String[] fieldNames;
    private final int numFields;
    private final Object object;

    EntryIterator(ClassInfo paramClassInfo, Object paramObject)
    {
      this.classInfo = paramClassInfo;
      this.object = paramObject;
      Collection localCollection = this.classInfo.getKeyNames();
      int i = localCollection.size();
      this.numFields = i;
      if (i == 0)
      {
        this.fieldNames = null;
        return;
      }
      String[] arrayOfString = new String[i];
      this.fieldNames = arrayOfString;
      int j = 0;
      Iterator localIterator = localCollection.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        int k = j + 1;
        arrayOfString[j] = str;
        j = k;
      }
      Arrays.sort(arrayOfString);
    }

    public boolean hasNext()
    {
      int i = this.fieldIndex;
      int j = this.numFields;
      if (i < j);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public Map.Entry<String, Object> next()
    {
      int i = this.fieldIndex;
      int j = this.numFields;
      if (i >= j)
        throw new NoSuchElementException();
      String str = this.fieldNames[i];
      int k = this.fieldIndex + 1;
      this.fieldIndex = k;
      Object localObject = this.object;
      return new ReflectionMap.Entry(localObject, str);
    }

    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }

  final class EntrySet extends AbstractSet<Map.Entry<String, Object>>
  {
    EntrySet()
    {
    }

    public Iterator<Map.Entry<String, Object>> iterator()
    {
      ClassInfo localClassInfo = ReflectionMap.this.classInfo;
      Object localObject = ReflectionMap.this.object;
      return new ReflectionMap.EntryIterator(localClassInfo, localObject);
    }

    public int size()
    {
      return ReflectionMap.this.size;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.util.ReflectionMap
 * JD-Core Version:    0.6.2
 */