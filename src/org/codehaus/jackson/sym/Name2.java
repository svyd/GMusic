package org.codehaus.jackson.sym;

public final class Name2 extends Name
{
  final int mQuad1;
  final int mQuad2;

  Name2(String paramString, int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramString, paramInt1);
    this.mQuad1 = paramInt2;
    this.mQuad2 = paramInt3;
  }

  public boolean equals(int paramInt)
  {
    return false;
  }

  public boolean equals(int paramInt1, int paramInt2)
  {
    int i = this.mQuad1;
    if (paramInt1 != i)
    {
      int j = this.mQuad2;
      if (paramInt2 == j);
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean equals(int[] paramArrayOfInt, int paramInt)
  {
    boolean bool = true;
    if (paramInt == 2)
    {
      int i = paramArrayOfInt[0];
      int j = this.mQuad1;
      if (i != j)
      {
        int k = paramArrayOfInt[1];
        int m = this.mQuad2;
        if (k == m);
      }
    }
    while (true)
    {
      return bool;
      bool = false;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.sym.Name2
 * JD-Core Version:    0.6.2
 */