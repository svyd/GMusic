package org.codehaus.jackson;

public abstract class JsonStreamContext
{
  protected int _index;
  protected int _type;

  public JsonStreamContext(int paramInt)
  {
    this._type = paramInt;
    this._index = -1;
  }

  public final int getCurrentIndex()
  {
    if (this._index < 0);
    for (int i = 0; ; i = this._index)
      return i;
  }

  public final int getEntryCount()
  {
    return this._index + 1;
  }

  public final String getTypeDesc()
  {
    String str;
    switch (this._type)
    {
    default:
      str = "?";
    case 0:
    case 1:
    case 2:
    }
    while (true)
    {
      return str;
      str = "ROOT";
      continue;
      str = "ARRAY";
      continue;
      str = "OBJECT";
    }
  }

  public final boolean inArray()
  {
    int i = 1;
    if (this._type != i);
    while (true)
    {
      return i;
      int j = 0;
    }
  }

  public final boolean inObject()
  {
    if (this._type == 2);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public final boolean inRoot()
  {
    if (this._type == 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.JsonStreamContext
 * JD-Core Version:    0.6.2
 */