package com.google.cast;

import android.os.AsyncTask;
import android.os.Build.VERSION;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;

public class NetworkTask extends AsyncTask<Void, Void, Integer>
{
  private static final Object[] a = new Void[0];
  private NetworkRequest[] b;
  private Listener c;
  private Logger d;
  protected NetworkRequest mCurrentRequest;

  public NetworkTask(NetworkRequest[] paramArrayOfNetworkRequest)
  {
    this.b = paramArrayOfNetworkRequest;
    Logger localLogger = new Logger("NetworkTask");
    this.d = localLogger;
  }

  public final void cancel()
  {
    if (this.mCurrentRequest != null)
      this.mCurrentRequest.cancel();
    boolean bool = super.cancel(true);
  }

  protected Integer doInBackground(Void[] paramArrayOfVoid)
  {
    int i = 0;
    Logger localLogger = this.d;
    Object[] arrayOfObject = new Object[i];
    localLogger.d("doInBackground", arrayOfObject);
    NetworkRequest[] arrayOfNetworkRequest = this.b;
    int j = arrayOfNetworkRequest.length;
    int k = i;
    while (true)
    {
      NetworkRequest localNetworkRequest;
      if (k < j)
      {
        localNetworkRequest = arrayOfNetworkRequest[k];
        if (!isCancelled())
          break label69;
        i = 65437;
      }
      label69: 
      do
      {
        this.mCurrentRequest = null;
        return Integer.valueOf(i);
        this.mCurrentRequest = localNetworkRequest;
        i = this.mCurrentRequest.execute();
      }
      while (i != 0);
      k += 1;
    }
  }

  public final void execute()
  {
    if (Build.VERSION.SDK_INT >= 11);
    try
    {
      Class localClass = getClass();
      Class[] arrayOfClass = new Class[2];
      arrayOfClass[0] = Executor.class;
      arrayOfClass[1] = [Ljava.lang.Object.class;
      Method localMethod = localClass.getMethod("executeOnExecutor", arrayOfClass);
      Object[] arrayOfObject1 = new Object[2];
      Executor localExecutor = THREAD_POOL_EXECUTOR;
      arrayOfObject1[0] = localExecutor;
      Object[] arrayOfObject2 = a;
      arrayOfObject1[1] = arrayOfObject2;
      Object localObject = localMethod.invoke(this, arrayOfObject1);
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      Logger localLogger1 = this.d;
      Object[] arrayOfObject3 = new Object[0];
      localLogger1.e(localNoSuchMethodException, "reflection failed", arrayOfObject3);
      Void[] arrayOfVoid = new Void[0];
      AsyncTask localAsyncTask = super.execute(arrayOfVoid);
      return;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      while (true)
      {
        Logger localLogger2 = this.d;
        Object[] arrayOfObject4 = new Object[0];
        localLogger2.e(localIllegalAccessException, "reflection failed", arrayOfObject4);
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      while (true)
      {
        Logger localLogger3 = this.d;
        Object[] arrayOfObject5 = new Object[0];
        localLogger3.e(localIllegalArgumentException, "reflection failed", arrayOfObject5);
      }
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      while (true)
      {
        Logger localLogger4 = this.d;
        Object[] arrayOfObject6 = new Object[0];
        localLogger4.e(localInvocationTargetException, "reflection failed", arrayOfObject6);
      }
    }
  }

  protected final void onCancelled(Integer paramInteger)
  {
    if (this.c == null)
      return;
    this.c.onTaskCancelled();
  }

  protected final void onPostExecute(Integer paramInteger)
  {
    Logger localLogger = this.d;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramInteger;
    localLogger.d("onPostExecute result: %d", arrayOfObject);
    if (this.c == null)
      return;
    switch (paramInteger.intValue())
    {
    default:
      Listener localListener = this.c;
      int i = paramInteger.intValue();
      localListener.onTaskFailed(i);
      return;
    case 0:
      this.c.onTaskCompleted();
      return;
    case -99:
    }
    this.c.onTaskCancelled();
  }

  public final void setListener(Listener paramListener)
  {
    this.c = paramListener;
  }

  public static abstract interface Listener
  {
    public abstract void onTaskCancelled();

    public abstract void onTaskCompleted();

    public abstract void onTaskFailed(int paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.NetworkTask
 * JD-Core Version:    0.6.2
 */