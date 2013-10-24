package com.google.android.music;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class PlayPauseButton extends ImageButton
{
  private final Context mContext;
  private int mCurrentPlayState = 3;
  private final Drawable mPauseImage;
  private final Drawable mPlayImage;
  private final Drawable mStopImage;

  public PlayPauseButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    int[] arrayOfInt = R.styleable.PlayPauseImages;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt);
    Resources localResources = paramContext.getResources();
    Drawable localDrawable1 = localTypedArray.getDrawable(0);
    this.mPauseImage = localDrawable1;
    Drawable localDrawable2 = localTypedArray.getDrawable(1);
    this.mPlayImage = localDrawable2;
    Drawable localDrawable3 = localTypedArray.getDrawable(2);
    this.mStopImage = localDrawable3;
    updateDrawable();
  }

  private void updateDrawable()
  {
    switch (this.mCurrentPlayState)
    {
    default:
    case 1:
    case 3:
    case 2:
    }
    while (true)
    {
      postInvalidate();
      return;
      Drawable localDrawable1 = this.mStopImage;
      setImageDrawable(localDrawable1);
      String str1 = this.mContext.getString(2131231180);
      setContentDescription(str1);
      continue;
      Drawable localDrawable2 = this.mPlayImage;
      setImageDrawable(localDrawable2);
      String str2 = this.mContext.getString(2131231178);
      setContentDescription(str2);
      continue;
      Drawable localDrawable3 = this.mPauseImage;
      setImageDrawable(localDrawable3);
      String str3 = this.mContext.getString(2131231179);
      setContentDescription(str3);
    }
  }

  public void setCurrentPlayState(int paramInt)
  {
    int i = this.mCurrentPlayState;
    if (paramInt != i)
      return;
    this.mCurrentPlayState = paramInt;
    updateDrawable();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.PlayPauseButton
 * JD-Core Version:    0.6.2
 */