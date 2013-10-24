package android.support.v4.content;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.util.TimeUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

public abstract class AsyncTaskLoader<D> extends Loader<D>
{
  volatile AsyncTaskLoader<D>.LoadTask mCancellingTask;
  Handler mHandler;
  long mLastLoadCompleteTime = 55536L;
  volatile AsyncTaskLoader<D>.LoadTask mTask;
  long mUpdateThrottle;

  public AsyncTaskLoader(Context paramContext)
  {
    super(paramContext);
  }

  public boolean cancelLoad()
  {
    boolean bool = false;
    if (this.mTask != null)
    {
      if (this.mCancellingTask == null)
        break label56;
      if (this.mTask.waiting)
      {
        this.mTask.waiting = false;
        Handler localHandler1 = this.mHandler;
        LoadTask localLoadTask1 = this.mTask;
        localHandler1.removeCallbacks(localLoadTask1);
      }
      this.mTask = null;
    }
    while (true)
    {
      return bool;
      label56: if (this.mTask.waiting)
      {
        this.mTask.waiting = false;
        Handler localHandler2 = this.mHandler;
        LoadTask localLoadTask2 = this.mTask;
        localHandler2.removeCallbacks(localLoadTask2);
        this.mTask = null;
      }
      else
      {
        bool = this.mTask.cancel(false);
        if (bool)
        {
          LoadTask localLoadTask3 = this.mTask;
          this.mCancellingTask = localLoadTask3;
        }
        this.mTask = null;
      }
    }
  }

  void dispatchOnCancelled(AsyncTaskLoader<D>.LoadTask paramAsyncTaskLoader, D paramD)
  {
    onCanceled(paramD);
    if (this.mCancellingTask != paramAsyncTaskLoader)
      return;
    rollbackContentChanged();
    long l = SystemClock.uptimeMillis();
    this.mLastLoadCompleteTime = l;
    this.mCancellingTask = null;
    executePendingTask();
  }

  void dispatchOnLoadComplete(AsyncTaskLoader<D>.LoadTask paramAsyncTaskLoader, D paramD)
  {
    if (this.mTask != paramAsyncTaskLoader)
    {
      dispatchOnCancelled(paramAsyncTaskLoader, paramD);
      return;
    }
    if (isAbandoned())
    {
      onCanceled(paramD);
      return;
    }
    commitContentChanged();
    long l = SystemClock.uptimeMillis();
    this.mLastLoadCompleteTime = l;
    this.mTask = null;
    deliverResult(paramD);
  }

  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    super.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    if (this.mTask != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mTask=");
      LoadTask localLoadTask1 = this.mTask;
      paramPrintWriter.print(localLoadTask1);
      paramPrintWriter.print(" waiting=");
      boolean bool1 = this.mTask.waiting;
      paramPrintWriter.println(bool1);
    }
    if (this.mCancellingTask != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mCancellingTask=");
      LoadTask localLoadTask2 = this.mCancellingTask;
      paramPrintWriter.print(localLoadTask2);
      paramPrintWriter.print(" waiting=");
      boolean bool2 = this.mCancellingTask.waiting;
      paramPrintWriter.println(bool2);
    }
    if (this.mUpdateThrottle == 0L)
      return;
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mUpdateThrottle=");
    TimeUtils.formatDuration(this.mUpdateThrottle, paramPrintWriter);
    paramPrintWriter.print(" mLastLoadCompleteTime=");
    long l1 = this.mLastLoadCompleteTime;
    long l2 = SystemClock.uptimeMillis();
    TimeUtils.formatDuration(l1, l2, paramPrintWriter);
    paramPrintWriter.println();
  }

  void executePendingTask()
  {
    if (this.mCancellingTask != null)
      return;
    if (this.mTask == null)
      return;
    if (this.mTask.waiting)
    {
      this.mTask.waiting = false;
      Handler localHandler1 = this.mHandler;
      LoadTask localLoadTask1 = this.mTask;
      localHandler1.removeCallbacks(localLoadTask1);
    }
    if (this.mUpdateThrottle > 0L)
    {
      long l1 = SystemClock.uptimeMillis();
      long l2 = this.mLastLoadCompleteTime;
      long l3 = this.mUpdateThrottle;
      long l4 = l2 + l3;
      if (l1 < l4)
      {
        this.mTask.waiting = true;
        Handler localHandler2 = this.mHandler;
        LoadTask localLoadTask2 = this.mTask;
        long l5 = this.mLastLoadCompleteTime;
        long l6 = this.mUpdateThrottle;
        long l7 = l5 + l6;
        boolean bool = localHandler2.postAtTime(localLoadTask2, l7);
        return;
      }
    }
    LoadTask localLoadTask3 = this.mTask;
    Executor localExecutor = ModernAsyncTask.THREAD_POOL_EXECUTOR;
    Void[] arrayOfVoid = (Void[])null;
    ModernAsyncTask localModernAsyncTask = localLoadTask3.executeOnExecutor(localExecutor, arrayOfVoid);
  }

  public abstract D loadInBackground();

  public void onCanceled(D paramD)
  {
  }

  protected void onForceLoad()
  {
    super.onForceLoad();
    boolean bool = cancelLoad();
    LoadTask localLoadTask = new LoadTask();
    this.mTask = localLoadTask;
    executePendingTask();
  }

  protected D onLoadInBackground()
  {
    return loadInBackground();
  }

  final class LoadTask extends ModernAsyncTask<Void, Void, D>
    implements Runnable
  {
    private CountDownLatch done;
    D result;
    boolean waiting;

    LoadTask()
    {
      CountDownLatch localCountDownLatch = new CountDownLatch(1);
      this.done = localCountDownLatch;
    }

    protected D doInBackground(Void[] paramArrayOfVoid)
    {
      Object localObject = AsyncTaskLoader.this.onLoadInBackground();
      this.result = localObject;
      return this.result;
    }

    protected void onCancelled()
    {
      try
      {
        AsyncTaskLoader localAsyncTaskLoader = AsyncTaskLoader.this;
        Object localObject1 = this.result;
        localAsyncTaskLoader.dispatchOnCancelled(this, localObject1);
        return;
      }
      finally
      {
        this.done.countDown();
      }
    }

    protected void onPostExecute(D paramD)
    {
      try
      {
        AsyncTaskLoader.this.dispatchOnLoadComplete(this, paramD);
        return;
      }
      finally
      {
        this.done.countDown();
      }
    }

    public void run()
    {
      this.waiting = false;
      AsyncTaskLoader.this.executePendingTask();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.content.AsyncTaskLoader
 * JD-Core Version:    0.6.2
 */