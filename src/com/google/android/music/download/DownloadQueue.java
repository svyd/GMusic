package com.google.android.music.download;

import com.google.android.music.log.Log;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.util.AbstractQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class DownloadQueue
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private DownloadTask mCurrentTask;
  private final Thread mDownloadThread;
  private final AbstractQueue<DownloadTask> mQueue;
  private final Object mQueueLock;
  private long mTaskSubmissionOrder;
  private final Map<DownloadTask, Long> mTaskSubmissionOrderMap;

  DownloadQueue()
  {
    Object localObject = new Object();
    this.mQueueLock = localObject;
    DownloadTaskComparator localDownloadTaskComparator = new DownloadTaskComparator(null);
    PriorityQueue localPriorityQueue = new PriorityQueue(10, localDownloadTaskComparator);
    this.mQueue = localPriorityQueue;
    HashMap localHashMap = new HashMap();
    this.mTaskSubmissionOrderMap = localHashMap;
    this.mDownloadThread = null;
  }

  public DownloadQueue(Thread paramThread)
  {
    if (paramThread == null)
      throw new IllegalArgumentException("The download thread passed is null");
    Object localObject = new Object();
    this.mQueueLock = localObject;
    DownloadTaskComparator localDownloadTaskComparator = new DownloadTaskComparator(null);
    PriorityQueue localPriorityQueue = new PriorityQueue(10, localDownloadTaskComparator);
    this.mQueue = localPriorityQueue;
    HashMap localHashMap = new HashMap();
    this.mTaskSubmissionOrderMap = localHashMap;
    this.mDownloadThread = paramThread;
  }

  private void cancelCurrentTask()
  {
    if (this.mCurrentTask == null)
      return;
    if (LOGV)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Canceling current: ");
      DownloadTask localDownloadTask = this.mCurrentTask;
      String str = localDownloadTask;
      Log.d("DownloadQueue", str);
    }
    this.mCurrentTask.cancel();
    this.mCurrentTask = null;
    if (this.mDownloadThread == null)
      return;
    this.mDownloadThread.interrupt();
  }

  private void purgeTasks(DownloadRequest.Owner paramOwner, int paramInt, List<DownloadTask> paramList)
  {
    Iterator localIterator1;
    switch (paramInt)
    {
    case 1:
    default:
      return;
    case 2:
      localIterator1 = this.mQueue.iterator();
      while (true)
      {
        if (!localIterator1.hasNext())
          return;
        if (((DownloadTask)localIterator1.next()).getDownloadRequest().getOwner() == paramOwner)
          localIterator1.remove();
      }
    case 3:
    }
    if ((paramList != null) && (paramList.size() > 0))
    {
      int i = ((DownloadTask)paramList.get(0)).getDownloadRequest().getPriority();
      Iterator localIterator2 = paramList.iterator();
      while (localIterator2.hasNext())
      {
        DownloadTask localDownloadTask1 = (DownloadTask)localIterator2.next();
        if (localDownloadTask1.getDownloadRequest().getPriority() < i)
          i = localDownloadTask1.getDownloadRequest().getPriority();
      }
      localIterator1 = this.mQueue.iterator();
      while (true)
      {
        if (!localIterator1.hasNext())
          return;
        DownloadTask localDownloadTask2 = (DownloadTask)localIterator1.next();
        if ((localDownloadTask2.getDownloadRequest().getOwner() == paramOwner) && (localDownloadTask2.getDownloadRequest().getPriority() >= i))
          localIterator1.remove();
      }
    }
    String str = "List of tasks required for purge policy " + paramInt;
    Log.w("DownloadQueue", str);
  }

  static String queueToString(AbstractQueue<DownloadTask> paramAbstractQueue)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("[");
    Iterator localIterator = paramAbstractQueue.iterator();
    while (localIterator.hasNext())
    {
      DownloadTask localDownloadTask = (DownloadTask)localIterator.next();
      StringBuilder localStringBuilder3 = localStringBuilder1.append(localDownloadTask);
      StringBuilder localStringBuilder4 = localStringBuilder1.append(", ");
    }
    int i = localStringBuilder1.length();
    if (i > 2)
    {
      int j = i + -2;
      StringBuilder localStringBuilder5 = localStringBuilder1.delete(j, i);
    }
    StringBuilder localStringBuilder6 = localStringBuilder1.append("]");
    return localStringBuilder1.toString();
  }

  public boolean addTasks(List<DownloadTask> paramList, int paramInt)
  {
    boolean bool1 = false;
    if ((paramList == null) || (paramList.size() == 0))
    {
      if (LOGV)
        Log.w("DownloadQueue", "addTasks: Empty list of tasks provided");
      return bool1;
    }
    DownloadRequest.Owner localOwner = ((DownloadTask)paramList.get(0)).getDownloadRequest().getOwner();
    synchronized (this.mQueueLock)
    {
      purgeTasks(localOwner, paramInt, paramList);
      Iterator localIterator = paramList.iterator();
      if (localIterator.hasNext())
      {
        DownloadTask localDownloadTask1 = (DownloadTask)localIterator.next();
        if (this.mTaskSubmissionOrder < 0L)
          this.mTaskSubmissionOrder = 0L;
        Map localMap = this.mTaskSubmissionOrderMap;
        long l1 = this.mTaskSubmissionOrder;
        long l2 = 1L + l1;
        this.mTaskSubmissionOrder = l2;
        Long localLong = Long.valueOf(l1);
        Object localObject2 = localMap.put(localDownloadTask1, localLong);
        boolean bool2 = this.mQueue.add(localDownloadTask1);
      }
    }
    DownloadTask localDownloadTask2 = (DownloadTask)this.mQueue.peek();
    if ((localDownloadTask2 != null) && (this.mCurrentTask != null))
    {
      int i = localDownloadTask2.getDownloadRequest().getPriority();
      int j = this.mCurrentTask.getDownloadRequest().getPriority();
      if (i <= j)
      {
        if (!this.mCurrentTask.upgrade(localDownloadTask2))
          break label321;
        Object localObject4 = this.mQueue.poll();
      }
    }
    while (true)
    {
      if (LOGV)
      {
        Object[] arrayOfObject = new Object[1];
        String str1 = queueToString(this.mQueue);
        arrayOfObject[0] = str1;
        String str2 = String.format("addTasks: queue=%s", arrayOfObject);
        Log.d("DownloadQueue", str2);
      }
      this.mQueueLock.notifyAll();
      bool1 = true;
      break;
      label321: if (paramInt != 3)
        cancelCurrentTask();
    }
  }

  public void cancelAndPurge(DownloadRequest.Owner paramOwner, int paramInt)
  {
    if (LOGV)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramOwner;
      Integer localInteger = Integer.valueOf(paramInt);
      arrayOfObject[1] = localInteger;
      String str = String.format("cancelAndPurge: owner=%s, policy=%d", arrayOfObject);
      Log.d("DownloadQueue", str);
    }
    synchronized (this.mQueueLock)
    {
      if ((this.mCurrentTask != null) && (this.mCurrentTask.getDownloadRequest().getOwner() == paramOwner) && (paramInt != 3))
        cancelCurrentTask();
      purgeTasks(paramOwner, paramInt, null);
      this.mQueueLock.notifyAll();
      return;
    }
  }

  void clearCurrentTask()
  {
    if (LOGV)
      Log.d("DownloadQueue", "clearCurrentTask");
    Object localObject1 = this.mQueueLock;
    Object localObject2 = null;
    try
    {
      this.mCurrentTask = localObject2;
      return;
    }
    finally
    {
      localObject3 = finally;
      throw localObject3;
    }
  }

  DownloadTask getNextTask()
    throws InterruptedException
  {
    synchronized (this.mQueueLock)
    {
      if (this.mQueue.size() == 0)
      {
        if (LOGV)
          Log.d("DownloadQueue", "getNextTask(): wait");
        this.mQueueLock.wait();
      }
    }
    DownloadTask localDownloadTask1 = (DownloadTask)this.mQueue.poll();
    this.mCurrentTask = localDownloadTask1;
    Map localMap = this.mTaskSubmissionOrderMap;
    DownloadTask localDownloadTask2 = this.mCurrentTask;
    Object localObject3 = localMap.remove(localDownloadTask2);
    if (LOGV)
    {
      Object[] arrayOfObject = new Object[3];
      DownloadTask localDownloadTask3 = this.mCurrentTask;
      arrayOfObject[0] = localDownloadTask3;
      Integer localInteger = Integer.valueOf(this.mQueue.size());
      arrayOfObject[1] = localInteger;
      String str1 = queueToString(this.mQueue);
      arrayOfObject[2] = str1;
      String str2 = String.format("currentTask=%s mQueue.size=%d queue=%s", arrayOfObject);
      Log.d("DownloadQueue", str2);
    }
    return this.mCurrentTask;
  }

  private final class DownloadTaskComparator
    implements Comparator<DownloadTask>
  {
    private DownloadTaskComparator()
    {
    }

    public int compare(DownloadTask paramDownloadTask1, DownloadTask paramDownloadTask2)
    {
      int i = paramDownloadTask1.getDownloadRequest().getPriority();
      int j = paramDownloadTask2.getDownloadRequest().getPriority();
      int k = i - j;
      if (k != 0);
      while (true)
      {
        return k;
        Long localLong1 = (Long)DownloadQueue.this.mTaskSubmissionOrderMap.get(paramDownloadTask1);
        if (localLong1 == null)
          throw new IllegalStateException("Failed to find submission order for the lhs task");
        Long localLong2 = (Long)DownloadQueue.this.mTaskSubmissionOrderMap.get(paramDownloadTask2);
        if (localLong2 == null)
          throw new IllegalStateException("Failed to find submission order for the rhs task");
        long l1 = localLong1.longValue();
        long l2 = localLong2.longValue();
        k = (int)(l1 - l2);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.DownloadQueue
 * JD-Core Version:    0.6.2
 */