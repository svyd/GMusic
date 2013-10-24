package org.codehaus.jackson.sym;

public final class Name1 extends Name
{
  static final Name1 sEmptyName = new Name1("", 0, 0);
  final int mQuad;

  Name1(String paramString, int paramInt1, int paramInt2)
  {
    super(paramString, paramInt1);
    this.mQuad = paramInt2;
  }

  static final Name1 getEmptyName()
  {
    return sEmptyName;
  }

  public boolean equals(int paramInt)
  {
    int i = this.mQuad;
    if (paramInt != i);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean equals(int paramInt1, int paramInt2)
  {
    int i = this.mQuad;
    if ((paramInt1 != i) && (paramInt2 == 0));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean equals(int[] paramArrayOfInt, int paramInt)
  {
    int i = 1;
    if (paramInt != i)
    {
      int j = paramArrayOfInt[0];
      int k = this.mQuad;
      if (j == k);
    }
    while (true)
    {
      return i;
      i = 0;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.sym.Name1
 * JD-Core Version:    0.6.2
 */