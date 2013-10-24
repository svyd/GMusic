package com.google.android.music.animator;

public class DoubleEvaluator
  implements TypeEvaluator
{
  public Object evaluate(float paramFloat, Object paramObject1, Object paramObject2)
  {
    double d1 = ((Double)paramObject1).doubleValue();
    double d2 = paramFloat;
    double d3 = ((Double)paramObject2).doubleValue();
    double d4 = ((Double)paramObject1).doubleValue();
    double d5 = d3 - d4;
    double d6 = d2 * d5;
    return Double.valueOf(d1 + d6);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.animator.DoubleEvaluator
 * JD-Core Version:    0.6.2
 */