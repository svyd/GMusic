package com.google.android.music.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import java.util.concurrent.Semaphore;

public class LoggableHandler extends Handler
{
  public LoggableHandler(String paramString)
  {
    this(paramString, 10);
  }

  protected LoggableHandler(String paramString, int paramInt)
  {
    super(localLooper);
  }

  private static HandlerThread startHandlerThread(String paramString, int paramInt)
  {
    final Semaphore localSemaphore = new Semaphore(0);
    HandlerThread local1 = new HandlerThread(paramString, paramInt)
    {
      protected void onLooperPrepared()
      {
        localSemaphore.release();
      }
    };
    local1.start();
    localSemaphore.acquireUninterruptibly();
    return local1;
  }

  public void dispatchMessage(Message paramMessage)
  {
    super.dispatchMessage(paramMessage);
  }

  public void quit()
  {
    getLooper().quit();
  }

  public boolean sendMessageAtTime(Message paramMessage, long paramLong)
  {
    return super.sendMessageAtTime(paramMessage, paramLong);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.LoggableHandler
 * JD-Core Version:    0.6.2
 */