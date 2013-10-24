package org.codehaus.jackson.impl;

final class ArrayWContext extends JsonWriteContext
{
  public ArrayWContext(JsonWriteContext paramJsonWriteContext)
  {
    super(1, paramJsonWriteContext);
  }

  protected void appendDesc(StringBuilder paramStringBuilder)
  {
    StringBuilder localStringBuilder1 = paramStringBuilder.append('[');
    int i = getCurrentIndex();
    StringBuilder localStringBuilder2 = paramStringBuilder.append(i);
    StringBuilder localStringBuilder3 = paramStringBuilder.append(']');
  }

  public int writeFieldName(String paramString)
  {
    return 4;
  }

  public int writeValue()
  {
    int i = this._index;
    int j = this._index + 1;
    this._index = j;
    if (i < 0);
    for (int k = 0; ; k = 1)
      return k;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.ArrayWContext
 * JD-Core Version:    0.6.2
 */