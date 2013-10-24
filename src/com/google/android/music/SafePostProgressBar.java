package com.google.android.music;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class SafePostProgressBar extends ProgressBar
{
  private boolean mAttachedToWindow;

  public SafePostProgressBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    try
    {
      this.mAttachedToWindow = true;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void onDetachedFromWindow()
  {
    Object localObject1 = null;
    try
    {
      this.mAttachedToWindow = false;
      super.onDetachedFromWindow();
      return;
    }
    finally
    {
    }
  }

  /** @deprecated */
  public boolean post(Runnable paramRunnable)
  {
    try
    {
      if (this.mAttachedToWindow)
      {
        boolean bool1 = super.post(paramRunnable);
        bool2 = bool1;
        return bool2;
      }
      boolean bool2 = false;
    }
    finally
    {
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.SafePostProgressBar
 * JD-Core Version:    0.6.2
 */