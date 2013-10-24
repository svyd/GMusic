package com.google.android.music.utils.async;

import android.os.Handler;
import com.google.android.music.utils.ArrayUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;

public class CallbackRunnable
  implements Runnable
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.ASYNC);
  private final Throwable mCalledFrom;
  private final Handler mCompletionThread;
  private final AsyncRunner mRunnable;

  public CallbackRunnable(Handler paramHandler, AsyncRunner paramAsyncRunner)
  {
    this.mCompletionThread = paramHandler;
    this.mRunnable = paramAsyncRunner;
    if (LOGV);
    for (Throwable localThrowable = new Throwable(); ; localThrowable = null)
    {
      this.mCalledFrom = localThrowable;
      return;
    }
  }

  private void appendCalledFromOntoStackTrace(Exception paramException)
  {
    if (this.mCalledFrom == null)
      return;
    StackTraceElement[] arrayOfStackTraceElement1 = new StackTraceElement[2];
    StackTraceElement[] arrayOfStackTraceElement2 = paramException.getStackTrace();
    arrayOfStackTraceElement1[0] = arrayOfStackTraceElement2;
    StackTraceElement[] arrayOfStackTraceElement3 = this.mCalledFrom.getStackTrace();
    arrayOfStackTraceElement1[1] = arrayOfStackTraceElement3;
    StackTraceElement[] arrayOfStackTraceElement4 = (StackTraceElement[])ArrayUtils.combine(arrayOfStackTraceElement1);
    paramException.setStackTrace(arrayOfStackTraceElement4);
  }

  public void run()
  {
    try
    {
      this.mRunnable.backgroundTask();
      Handler localHandler = this.mCompletionThread;
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          try
          {
            CallbackRunnable.this.mRunnable.taskCompleted();
            return;
          }
          catch (Exception localException)
          {
            CallbackRunnable.this.appendCalledFromOntoStackTrace(localException);
            String str = localException.getMessage();
            throw new RuntimeException(str, localException);
          }
        }
      };
      boolean bool = localHandler.post(local1);
      return;
    }
    catch (Exception localException)
    {
      appendCalledFromOntoStackTrace(localException);
      String str = localException.getMessage();
      throw new RuntimeException(str, localException);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.async.CallbackRunnable
 * JD-Core Version:    0.6.2
 */