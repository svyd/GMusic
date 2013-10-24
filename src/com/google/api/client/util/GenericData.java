package com.google.api.client.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class GenericData extends AbstractMap<String, Object>
  implements Cloneable
{
  private final ClassInfo classInfo;
  private EntrySet entrySet;
  public ArrayMap<String, Object> unknownFields;

  public GenericData()
  {
    ArrayMap localArrayMap = ArrayMap.create();
    this.unknownFields = localArrayMap;
    ClassInfo localClassInfo = ClassInfo.of(getClass());
    this.classInfo = localClassInfo;
  }

  public GenericData clone()
  {
    try
    {
      GenericData localGenericData = (GenericData)super.clone();
      localGenericData.entrySet = null;
      DataUtil.cloneInternal(this, localGenericData);
      ArrayMap localArrayMap = (ArrayMap)DataUtil.clone(this.unknownFields);
      localGenericData.unknownFields = localArrayMap;
      return localGenericData;
    }
    catch (CloneNotSupportedException localCloneNotSupportedException)
    {
      throw new IllegalStateException(localCloneNotSupportedException);
    }
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

  public final Object get(Object paramObject)
  {
    Object localObject;
    if (!(paramObject instanceof String))
      localObject = null;
    while (true)
    {
      return localObject;
      String str = (String)paramObject;
      FieldInfo localFieldInfo = this.classInfo.getFieldInfo(str);
      if (localFieldInfo != null)
        localObject = localFieldInfo.getValue(this);
      else
        localObject = this.unknownFields.get(str);
    }
  }

  public final Object put(String paramString, Object paramObject)
  {
    FieldInfo localFieldInfo = this.classInfo.getFieldInfo(paramString);
    Object localObject;
    if (localFieldInfo != null)
    {
      localObject = localFieldInfo.getValue(this);
      localFieldInfo.setValue(this, paramObject);
    }
    while (true)
    {
      return localObject;
      localObject = this.unknownFields.put(paramString, paramObject);
    }
  }

  public final void putAll(Map<? extends String, ?> paramMap)
  {
    Iterator localIterator = paramMap.entrySet().iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      String str = (String)localEntry.getKey();
      Object localObject = localEntry.getValue();
      set(str, localObject);
    }
  }

  public final Object remove(Object paramObject)
  {
    if ((paramObject instanceof String))
    {
      String str = (String)paramObject;
      if (this.classInfo.getFieldInfo(str) != null)
        throw new UnsupportedOperationException();
    }
    for (Object localObject = this.unknownFields.remove(paramObject); ; localObject = null)
      return localObject;
  }

  public final void set(String paramString, Object paramObject)
  {
    FieldInfo localFieldInfo = this.classInfo.getFieldInfo(paramString);
    if (localFieldInfo != null)
    {
      localFieldInfo.setValue(this, paramObject);
      return;
    }
    Object localObject = this.unknownFields.put(paramString, paramObject);
  }

  public int size()
  {
    int i = this.classInfo.getKeyCount();
    int j = this.unknownFields.size();
    return i + j;
  }

  final class EntryIterator
    implements Iterator<Map.Entry<String, Object>>
  {
    private final ReflectionMap.EntryIterator fieldIterator;
    private boolean startedUnknown;
    private final Iterator<Map.Entry<String, Object>> unknownIterator;

    EntryIterator()
    {
      ClassInfo localClassInfo = GenericData.this.classInfo;
      ReflectionMap.EntryIterator localEntryIterator = new ReflectionMap.EntryIterator(localClassInfo, GenericData.this);
      this.fieldIterator = localEntryIterator;
      Iterator localIterator = GenericData.this.unknownFields.entrySet().iterator();
      this.unknownIterator = localIterator;
    }

    public boolean hasNext()
    {
      if (((!this.startedUnknown) && (this.fieldIterator.hasNext())) || (this.unknownIterator.hasNext()));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public Map.Entry<String, Object> next()
    {
      ReflectionMap.EntryIterator localEntryIterator;
      if (!this.startedUnknown)
      {
        localEntryIterator = this.fieldIterator;
        if (!localEntryIterator.hasNext());
      }
      for (Map.Entry localEntry = localEntryIterator.next(); ; localEntry = (Map.Entry)this.unknownIterator.next())
      {
        return localEntry;
        this.startedUnknown = true;
      }
    }

    public void remove()
    {
      if (this.startedUnknown)
        this.unknownIterator.remove();
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
      GenericData localGenericData = GenericData.this;
      return new GenericData.EntryIterator(localGenericData);
    }

    public int size()
    {
      return GenericData.this.size();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.util.GenericData
 * JD-Core Version:    0.6.2
 */