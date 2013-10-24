package com.google.android.music.animator;

public class IntEvaluator
  implements TypeEvaluator
{
  public Object evaluate(float paramFloat, Object paramObject1, Object paramObject2)
  {
    float f1 = ((Integer)paramObject1).intValue();
    int i = ((Integer)paramObject2).intValue();
    int j = ((Integer)paramObject1).intValue();
    float f2 = (i - j) * paramFloat;
    return Integer.valueOf((int)(f1 + f2));
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.animator.IntEvaluator
 * JD-Core Version:    0.6.2
 */