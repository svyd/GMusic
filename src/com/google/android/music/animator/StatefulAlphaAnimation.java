package com.google.android.music.animator;

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class StatefulAlphaAnimation extends Animation
{
  private float mCurrentAlpha;
  private final float mFromAlpha;
  private final float mToAlpha;

  public StatefulAlphaAnimation(float paramFloat1, float paramFloat2)
  {
    this.mFromAlpha = paramFloat1;
    this.mToAlpha = paramFloat2;
    float f = this.mFromAlpha;
    this.mCurrentAlpha = f;
  }

  protected void applyTransformation(float paramFloat, Transformation paramTransformation)
  {
    float f1 = this.mFromAlpha;
    float f2 = this.mToAlpha;
    float f3 = this.mFromAlpha;
    float f4 = (f2 - f3) * paramFloat;
    float f5 = f1 + f4;
    this.mCurrentAlpha = f5;
    float f6 = this.mCurrentAlpha;
    paramTransformation.setAlpha(f6);
  }

  public float getCurrentAlpha()
  {
    return this.mCurrentAlpha;
  }

  public boolean willChangeBounds()
  {
    return false;
  }

  public boolean willChangeTransformationMatrix()
  {
    return false;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.animator.StatefulAlphaAnimation
 * JD-Core Version:    0.6.2
 */