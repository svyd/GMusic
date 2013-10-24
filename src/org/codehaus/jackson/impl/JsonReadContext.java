package org.codehaus.jackson.impl;

import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.util.CharTypes;

public final class JsonReadContext extends JsonStreamContext
{
  JsonReadContext _child = null;
  protected int _columnNr;
  protected String _currentName;
  protected int _lineNr;
  protected final JsonReadContext _parent;

  public JsonReadContext(JsonReadContext paramJsonReadContext, int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramInt1);
    this._parent = paramJsonReadContext;
    this._lineNr = paramInt2;
    this._columnNr = paramInt3;
  }

  public static JsonReadContext createRootContext(int paramInt1, int paramInt2)
  {
    return new JsonReadContext(null, 0, paramInt1, paramInt2);
  }

  public final JsonReadContext createChildArrayContext(int paramInt1, int paramInt2)
  {
    JsonReadContext localJsonReadContext = this._child;
    if (localJsonReadContext == null)
    {
      localJsonReadContext = new JsonReadContext(this, 1, paramInt1, paramInt2);
      this._child = localJsonReadContext;
    }
    while (true)
    {
      return localJsonReadContext;
      localJsonReadContext.reset(1, paramInt1, paramInt2);
    }
  }

  public final JsonReadContext createChildObjectContext(int paramInt1, int paramInt2)
  {
    JsonReadContext localJsonReadContext = this._child;
    if (localJsonReadContext == null)
    {
      localJsonReadContext = new JsonReadContext(this, 2, paramInt1, paramInt2);
      this._child = localJsonReadContext;
    }
    while (true)
    {
      return localJsonReadContext;
      localJsonReadContext.reset(2, paramInt1, paramInt2);
    }
  }

  public final boolean expectComma()
  {
    int i = this._index + 1;
    this._index = i;
    if ((this._type != 0) && (i > 0));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public final String getCurrentName()
  {
    return this._currentName;
  }

  public final JsonReadContext getParent()
  {
    return this._parent;
  }

  public final JsonLocation getStartLocation(Object paramObject)
  {
    int i = this._lineNr;
    int j = this._columnNr;
    Object localObject = paramObject;
    return new JsonLocation(localObject, 65535L, i, j);
  }

  protected final void reset(int paramInt1, int paramInt2, int paramInt3)
  {
    this._type = paramInt1;
    this._index = -1;
    this._lineNr = paramInt2;
    this._columnNr = paramInt3;
    this._currentName = null;
  }

  public void setCurrentName(String paramString)
  {
    this._currentName = paramString;
  }

  public final String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder(64);
    switch (this._type)
    {
    default:
    case 0:
    case 1:
      while (true)
      {
        return localStringBuilder1.toString();
        StringBuilder localStringBuilder2 = localStringBuilder1.append("/");
        continue;
        StringBuilder localStringBuilder3 = localStringBuilder1.append('[');
        int i = getCurrentIndex();
        StringBuilder localStringBuilder4 = localStringBuilder1.append(i);
        StringBuilder localStringBuilder5 = localStringBuilder1.append(']');
      }
    case 2:
    }
    StringBuilder localStringBuilder6 = localStringBuilder1.append('{');
    if (this._currentName != null)
    {
      StringBuilder localStringBuilder7 = localStringBuilder1.append('"');
      String str = this._currentName;
      CharTypes.appendQuoted(localStringBuilder1, str);
      StringBuilder localStringBuilder8 = localStringBuilder1.append('"');
    }
    while (true)
    {
      StringBuilder localStringBuilder9 = localStringBuilder1.append(']');
      break;
      StringBuilder localStringBuilder10 = localStringBuilder1.append('?');
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.JsonReadContext
 * JD-Core Version:    0.6.2
 */