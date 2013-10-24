package com.google.android.music.animator;

public class FloatEvaluator
  implements TypeEvaluator
{
  public Object evaluate(float paramFloat, Object paramObject1, Object paramObject2)
  {
    float f1 = ((Float)paramObject1).floatValue();
    float f2 = ((Float)paramObject2).floatValue();
    float f3 = ((Float)paramObject1).floatValue();
    float f4 = (f2 - f3) * paramFloat;
    return Float.valueOf(f1 + f4);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.animator.FloatEvaluator
 * JD-Core Version:    0.6.2
 */