package com.google.android.music.utils.async;

import android.os.Handler;
import android.os.Message;
import com.google.android.music.utils.LoggableHandler;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncWorkers
{
  public static final LoggableHandler sBackendServiceWorker = new LoggableHandler("BGAsyncWorker");
  public static final LoggableHandler sUIBackgroundWorker = new LoggableHandler("UIAsyncWorker");
  private static final HashMap<LoggableHandler, AtomicInteger> sUniqueMessageTypes = new HashMap(1);

  /** @deprecated */
  public static int getUniqueMessageType(LoggableHandler paramLoggableHandler)
  {
    try
    {
      AtomicInteger localAtomicInteger = (AtomicInteger)sUniqueMessageTypes.get(paramLoggableHandler);
      if (localAtomicInteger == null)
      {
        localAtomicInteger = new AtomicInteger(0);
        Object localObject1 = sUniqueMessageTypes.put(paramLoggableHandler, localAtomicInteger);
      }
      int i = localAtomicInteger.incrementAndGet();
      int j = i;
      return j;
    }
    finally
    {
      localObject2 = finally;
      throw localObject2;
    }
  }

  public static void runAsync(LoggableHandler paramLoggableHandler, int paramInt, Runnable paramRunnable, boolean paramBoolean)
  {
    if (paramBoolean)
      paramLoggableHandler.removeMessages(paramInt);
    TraceableRunnable localTraceableRunnable = new TraceableRunnable(paramRunnable);
    Message localMessage = Message.obtain(paramLoggableHandler, localTraceableRunnable);
    localMessage.what = paramInt;
    boolean bool = paramLoggableHandler.sendMessage(localMessage);
  }

  public static void runAsync(LoggableHandler paramLoggableHandler, Runnable paramRunnable)
  {
    TraceableRunnable localTraceableRunnable = new TraceableRunnable(paramRunnable);
    boolean bool = paramLoggableHandler.post(localTraceableRunnable);
  }

  public static void runAsyncWithCallback(LoggableHandler paramLoggableHandler, AsyncRunner paramAsyncRunner)
  {
    Handler localHandler = new Handler();
    CallbackRunnable localCallbackRunnable = new CallbackRunnable(localHandler, paramAsyncRunner);
    boolean bool = paramLoggableHandler.post(localCallbackRunnable);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.async.AsyncWorkers
 * JD-Core Version:    0.6.2
 */