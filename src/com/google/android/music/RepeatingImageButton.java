package com.google.android.music;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class RepeatingImageButton extends ImageButton
{
  private long mInterval = 500L;
  private RepeatListener mListener;
  private int mRepeatCount;
  private Runnable mRepeater;
  private long mStartTime;

  public RepeatingImageButton(Context paramContext)
  {
    this(paramContext, null);
  }

  public RepeatingImageButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 16842866);
  }

  public RepeatingImageButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        RepeatingImageButton.this.doRepeat(false);
        if (!RepeatingImageButton.this.isPressed())
          return;
        RepeatingImageButton localRepeatingImageButton = RepeatingImageButton.this;
        long l = RepeatingImageButton.this.mInterval;
        boolean bool = localRepeatingImageButton.postDelayed(this, l);
      }
    };
    this.mRepeater = local1;
    setFocusable(true);
    setLongClickable(true);
  }

  private void doRepeat(boolean paramBoolean)
  {
    long l1 = SystemClock.elapsedRealtime();
    if (this.mListener == null)
      return;
    RepeatListener localRepeatListener = this.mListener;
    long l2 = this.mStartTime;
    long l3 = l1 - l2;
    int i;
    if (paramBoolean)
      i = -1;
    while (true)
    {
      localRepeatListener.onRepeat(this, l3, i);
      return;
      i = this.mRepeatCount;
      int j = i + 1;
      this.mRepeatCount = j;
    }
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    switch (paramInt)
    {
    default:
    case 23:
    case 66:
    }
    for (boolean bool1 = super.onKeyDown(paramInt, paramKeyEvent); ; bool1 = true)
    {
      return bool1;
      boolean bool2 = super.onKeyDown(paramInt, paramKeyEvent);
    }
  }

  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    switch (paramInt)
    {
    default:
    case 23:
    case 66:
    }
    while (true)
    {
      return super.onKeyUp(paramInt, paramKeyEvent);
      Runnable localRunnable = this.mRepeater;
      boolean bool = removeCallbacks(localRunnable);
      if (this.mStartTime != 0L)
      {
        doRepeat(true);
        this.mStartTime = 0L;
      }
    }
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 1)
    {
      Runnable localRunnable = this.mRepeater;
      boolean bool = removeCallbacks(localRunnable);
      if (this.mStartTime != 0L)
      {
        doRepeat(true);
        this.mStartTime = 0L;
      }
    }
    return super.onTouchEvent(paramMotionEvent);
  }

  public boolean performLongClick()
  {
    boolean bool1 = false;
    if (this.mListener == null);
    while (true)
    {
      return bool1;
      long l = SystemClock.elapsedRealtime();
      this.mStartTime = l;
      this.mRepeatCount = 0;
      Runnable localRunnable = this.mRepeater;
      boolean bool2 = post(localRunnable);
      bool1 = true;
    }
  }

  public void setRepeatListener(RepeatListener paramRepeatListener, long paramLong)
  {
    this.mListener = paramRepeatListener;
    this.mInterval = paramLong;
  }

  public static abstract interface RepeatListener
  {
    public abstract void onRepeat(View paramView, long paramLong, int paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.RepeatingImageButton
 * JD-Core Version:    0.6.2
 */