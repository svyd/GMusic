package org.codehaus.jackson.impl;

final class ObjectWContext extends JsonWriteContext
{
  protected String _currentName = null;
  protected boolean _expectValue = false;

  public ObjectWContext(JsonWriteContext paramJsonWriteContext)
  {
    super(2, paramJsonWriteContext);
  }

  protected void appendDesc(StringBuilder paramStringBuilder)
  {
    StringBuilder localStringBuilder1 = paramStringBuilder.append('{');
    if (this._currentName != null)
    {
      StringBuilder localStringBuilder2 = paramStringBuilder.append('"');
      String str = this._currentName;
      StringBuilder localStringBuilder3 = paramStringBuilder.append(str);
      StringBuilder localStringBuilder4 = paramStringBuilder.append('"');
    }
    while (true)
    {
      StringBuilder localStringBuilder5 = paramStringBuilder.append(']');
      return;
      StringBuilder localStringBuilder6 = paramStringBuilder.append('?');
    }
  }

  public int writeFieldName(String paramString)
  {
    int i;
    if (this._currentName != null)
      i = 4;
    while (true)
    {
      return i;
      this._currentName = paramString;
      if (this._index < 0)
        i = 0;
      else
        i = 1;
    }
  }

  public int writeValue()
  {
    if (this._currentName == null);
    for (int i = 5; ; i = 2)
    {
      return i;
      this._currentName = null;
      int j = this._index + 1;
      this._index = j;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.ObjectWContext
 * JD-Core Version:    0.6.2
 */