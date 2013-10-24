package org.codehaus.jackson.impl;

import org.codehaus.jackson.JsonStreamContext;

public abstract class JsonWriteContext extends JsonStreamContext
{
  JsonWriteContext _childArray = null;
  JsonWriteContext _childObject = null;
  protected final JsonWriteContext _parent;

  protected JsonWriteContext(int paramInt, JsonWriteContext paramJsonWriteContext)
  {
    super(paramInt);
    this._parent = paramJsonWriteContext;
  }

  public static JsonWriteContext createRootContext()
  {
    return new RootWContext();
  }

  protected abstract void appendDesc(StringBuilder paramStringBuilder);

  public final JsonWriteContext createChildArrayContext()
  {
    Object localObject = this._childArray;
    if (localObject == null)
    {
      localObject = new ArrayWContext(this);
      this._childArray = ((JsonWriteContext)localObject);
    }
    while (true)
    {
      return localObject;
      ((JsonWriteContext)localObject)._index = -1;
    }
  }

  public final JsonWriteContext createChildObjectContext()
  {
    Object localObject = this._childObject;
    if (localObject == null)
    {
      localObject = new ObjectWContext(this);
      this._childObject = ((JsonWriteContext)localObject);
    }
    while (true)
    {
      return localObject;
      ((JsonWriteContext)localObject)._index = -1;
    }
  }

  public final JsonWriteContext getParent()
  {
    return this._parent;
  }

  public final String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(64);
    appendDesc(localStringBuilder);
    return localStringBuilder.toString();
  }

  public abstract int writeFieldName(String paramString);

  public abstract int writeValue();
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.JsonWriteContext
 * JD-Core Version:    0.6.2
 */