package com.google.android.music.toshare;

import android.os.Handler;
import java.util.concurrent.Executor;

public class HandlerExecutor
  implements Executor
{
  private final Handler mHandler;

  public HandlerExecutor(Handler paramHandler)
  {
    this.mHandler = paramHandler;
  }

  public void execute(Runnable paramRunnable)
  {
    boolean bool = this.mHandler.post(paramRunnable);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.toshare.HandlerExecutor
 * JD-Core Version:    0.6.2
 */