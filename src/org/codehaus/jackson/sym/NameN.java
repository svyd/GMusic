package org.codehaus.jackson.sym;

public final class NameN extends Name
{
  final int mQuadLen;
  final int[] mQuads;

  NameN(String paramString, int paramInt1, int[] paramArrayOfInt, int paramInt2)
  {
    super(paramString, paramInt1);
    if (paramInt2 < 3)
      throw new IllegalArgumentException("Qlen must >= 3");
    this.mQuads = paramArrayOfInt;
    this.mQuadLen = paramInt2;
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
    boolean bool = false;
    int i = this.mQuadLen;
    if (paramInt != i);
    while (true)
    {
      return bool;
      int j = 0;
      while (true)
      {
        if (j >= paramInt)
          break label56;
        int k = paramArrayOfInt[j];
        int m = this.mQuads[j];
        if (k == m)
          break;
        j += 1;
      }
      label56: bool = true;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     org.codehaus.jackson.sym.NameN
 * JD-Core Version:    0.6.2
 */