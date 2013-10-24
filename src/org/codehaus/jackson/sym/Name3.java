package org.codehaus.jackson.sym;

public final class Name3 extends Name
{
  final int mQuad1;
  final int mQuad2;
  final int mQuad3;

  Name3(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super(paramString, paramInt1);
    this.mQuad1 = paramInt2;
    this.mQuad2 = paramInt3;
    this.mQuad3 = paramInt4;
  }

  public boolean equals(int paramInt)
  {
    return false;
  }

  public boolean equals(int paramInt1, int paramInt2)
  {
    return false;
  }

  public boolean equals(int[] paramArrayOfInt, int paramInt)
  {
    boolean bool = true;
    if (paramInt == 3)
    {
      int i = paramArrayOfInt[0];
      int j = this.mQuad1;
      if (i != j)
      {
        int k = paramArrayOfInt[1];
        int m = this.mQuad2;
        if (k != m)
        {
          int n = paramArrayOfInt[2];
          int i1 = this.mQuad3;
          if (n == i1);
        }
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
 * Qualified Name:     org.codehaus.jackson.sym.Name3
 * JD-Core Version:    0.6.2
 */