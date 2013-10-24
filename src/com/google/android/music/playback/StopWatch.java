package com.google.android.music.playback;

import android.os.SystemClock;

public class StopWatch
{
  private long mCumulativeTime = 0L;
  private boolean mIsRunning = false;
  private Listener mListener;
  private long mStart = 0L;

  /** @deprecated */
  public long getTime()
  {
    try
    {
      if (!this.mIsRunning);
      long l4;
      for (long l1 = this.mCumulativeTime; ; l1 += l4)
      {
        return l1;
        long l2 = SystemClock.elapsedRealtime();
        l1 = this.mCumulativeTime;
        long l3 = this.mStart;
        l4 = l2 - l3;
      }
    }
    finally
    {
    }
  }

  public void pause()
  {
    Listener localListener = null;
    try
    {
      if (this.mIsRunning)
      {
        localListener = this.mListener;
        long l1 = SystemClock.elapsedRealtime();
        long l2 = this.mCumulativeTime;
        long l3 = this.mStart;
        long l4 = l1 - l3;
        long l5 = l2 + l4;
        this.mCumulativeTime = l5;
        this.mIsRunning = false;
      }
      if (localListener == null)
        return;
      localListener.onPause(this);
      return;
    }
    finally
    {
    }
  }

  public void reset()
  {
    try
    {
      Listener localListener = this.mListener;
      this.mCumulativeTime = 0L;
      long l = SystemClock.elapsedRealtime();
      this.mStart = l;
      if (localListener == null)
        return;
      localListener.onReset(this);
      return;
    }
    finally
    {
    }
  }

  public void start()
  {
    Listener localListener = null;
    try
    {
      if (!this.mIsRunning)
      {
        localListener = this.mListener;
        long l = SystemClock.elapsedRealtime();
        this.mStart = l;
        this.mIsRunning = true;
      }
      if (localListener == null)
        return;
      localListener.onStart(this);
      return;
    }
    finally
    {
    }
  }

  public static abstract interface Listener
  {
    public abstract void onPause(StopWatch paramStopWatch);

    public abstract void onReset(StopWatch paramStopWatch);

    public abstract void onStart(StopWatch paramStopWatch);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.playback.StopWatch
 * JD-Core Version:    0.6.2
 */