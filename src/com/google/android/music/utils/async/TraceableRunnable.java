package com.google.android.music.utils.async;

import com.google.android.music.utils.ArrayUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;

public class TraceableRunnable
  implements Runnable
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.ASYNC);
  private final Throwable mCalledFrom;
  private final Runnable mOrigRunnable;

  public TraceableRunnable(Runnable paramRunnable)
  {
    this.mOrigRunnable = paramRunnable;
    if (LOGV);
    for (Throwable localThrowable = new Throwable(); ; localThrowable = null)
    {
      this.mCalledFrom = localThrowable;
      return;
    }
  }

  public void run()
  {
    try
    {
      this.mOrigRunnable.run();
      return;
    }
    catch (Exception localException)
    {
      if (this.mCalledFrom != null)
      {
        StackTraceElement[] arrayOfStackTraceElement1 = new StackTraceElement[2];
        StackTraceElement[] arrayOfStackTraceElement2 = localException.getStackTrace();
        arrayOfStackTraceElement1[0] = arrayOfStackTraceElement2;
        StackTraceElement[] arrayOfStackTraceElement3 = this.mCalledFrom.getStackTrace();
        arrayOfStackTraceElement1[1] = arrayOfStackTraceElement3;
        StackTraceElement[] arrayOfStackTraceElement4 = (StackTraceElement[])ArrayUtils.combine(arrayOfStackTraceElement1);
        localException.setStackTrace(arrayOfStackTraceElement4);
      }
      String str = localException.getMessage();
      throw new RuntimeException(str, localException);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.async.TraceableRunnable
 * JD-Core Version:    0.6.2
 */