package com.google.android.music.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class ActionBarItem extends FrameLayout
{
  private final ColorDrawable mBackgroundDrawable;
  private final BitmapDrawable mFocusBackground;
  private final int mMaxAlpha;
  private boolean mMaxAlphaSet = false;

  public ActionBarItem(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Resources localResources = paramContext.getResources();
    int i = localResources.getColor(2131427332);
    ColorDrawable localColorDrawable1 = new ColorDrawable(i);
    this.mBackgroundDrawable = localColorDrawable1;
    Bitmap localBitmap = BitmapFactory.decodeResource(localResources, 2130837801);
    BitmapDrawable localBitmapDrawable = new BitmapDrawable(localResources, localBitmap);
    this.mFocusBackground = localBitmapDrawable;
    int j = this.mBackgroundDrawable.getAlpha();
    this.mMaxAlpha = j;
    this.mBackgroundDrawable.setAlpha(0);
    ColorDrawable localColorDrawable2 = this.mBackgroundDrawable;
    setBackgroundDrawable(localColorDrawable2);
  }

  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    if (paramBoolean)
    {
      BitmapDrawable localBitmapDrawable = this.mFocusBackground;
      setBackgroundDrawable(localBitmapDrawable);
      return;
    }
    ColorDrawable localColorDrawable = this.mBackgroundDrawable;
    setBackgroundDrawable(localColorDrawable);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    clearAnimation();
    int i = paramMotionEvent.getAction();
    if ((i == 4) || (i == 1))
    {
      this.mBackgroundDrawable.setAlpha(0);
      this.mMaxAlphaSet = false;
    }
    while (true)
    {
      return super.onTouchEvent(paramMotionEvent);
      if (!this.mMaxAlphaSet)
      {
        this.mMaxAlphaSet = true;
        ColorDrawable localColorDrawable1 = this.mBackgroundDrawable;
        int j = this.mMaxAlpha;
        localColorDrawable1.setAlpha(j);
        ColorDrawable localColorDrawable2 = this.mBackgroundDrawable;
        setBackgroundDrawable(localColorDrawable2);
        invalidate();
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.widgets.ActionBarItem
 * JD-Core Version:    0.6.2
 */