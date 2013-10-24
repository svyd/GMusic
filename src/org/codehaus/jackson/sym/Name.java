package org.codehaus.jackson.sym;

public abstract class Name
{
  protected final int mHashCode;
  protected final String mName;

  protected Name(String paramString, int paramInt)
  {
    this.mName = paramString;
    this.mHashCode = paramInt;
  }

  public abstract boolean equals(int paramInt);

  public abstract boolean equals(int paramInt1, int paramInt2);

  public boolean equals(Object paramObject)
  {
    if (paramObject == this);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public abstract boolean equals(int[] paramArrayOfInt, int paramInt);

  public String getName()
  {
    return this.mName;
  }

  public final int hashCode()
  {
    return this.mHashCode;
  }

  public String toString()
  {
    return this.mName;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.sym.Name
 * JD-Core Version:    0.6.2
 */