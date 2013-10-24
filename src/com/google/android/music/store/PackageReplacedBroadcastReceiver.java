package com.google.android.music.store;

import android.content.BroadcastReceiver;
import android.content.Context;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.async.AsyncWorkers;

abstract class PackageReplacedBroadcastReceiver extends BroadcastReceiver
{
  protected void startUpgrade(final Context paramContext)
  {
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        Store localStore = Store.getInstance(paramContext);
      }
    };
    boolean bool = localLoggableHandler.post(local1);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.PackageReplacedBroadcastReceiver
 * JD-Core Version:    0.6.2
 */