package org.codehaus.jackson.impl;

final class RootWContext extends JsonWriteContext
{
  public RootWContext()
  {
    super(0, null);
  }

  protected void appendDesc(StringBuilder paramStringBuilder)
  {
    StringBuilder localStringBuilder = paramStringBuilder.append("/");
  }

  public int writeFieldName(String paramString)
  {
    return 4;
  }

  public int writeValue()
  {
    int i = this._index + 1;
    this._index = i;
    if (this._index == 0);
    for (int j = 0; ; j = 3)
      return j;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.impl.RootWContext
 * JD-Core Version:    0.6.2
 */