package com.google.android.music.sync.common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ClosableBlockingQueue<E>
{
  private final int mCapacity;
  private volatile boolean mIsClosed = false;
  private final Condition mNotEmpty;
  private final Condition mNotFull;
  private final LinkedList<E> mQueue;
  private final ReentrantLock mQueueLock;

  public ClosableBlockingQueue(int paramInt)
  {
    ReentrantLock localReentrantLock = new ReentrantLock();
    this.mQueueLock = localReentrantLock;
    Condition localCondition1 = this.mQueueLock.newCondition();
    this.mNotEmpty = localCondition1;
    Condition localCondition2 = this.mQueueLock.newCondition();
    this.mNotFull = localCondition2;
    this.mCapacity = paramInt;
    LinkedList localLinkedList = new LinkedList();
    this.mQueue = localLinkedList;
  }

  public void close()
  {
    try
    {
      this.mQueueLock.lock();
      this.mIsClosed = true;
      this.mNotEmpty.signalAll();
      return;
    }
    finally
    {
      this.mQueueLock.unlock();
    }
  }

  public void kill()
  {
    try
    {
      this.mQueueLock.lock();
      this.mIsClosed = true;
      this.mQueue.clear();
      this.mNotFull.signalAll();
      this.mNotEmpty.signalAll();
      return;
    }
    finally
    {
      this.mQueueLock.unlock();
    }
  }

  public void put(E paramE)
    throws InterruptedException
  {
    if (paramE == null)
      throw new NullPointerException();
    try
    {
      this.mQueueLock.lock();
      try
      {
        while (true)
        {
          int i = this.mQueue.size();
          int j = this.mCapacity;
          if ((i < j) || (this.mIsClosed))
            break;
          this.mNotFull.await();
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        this.mNotFull.signal();
        throw localInterruptedException;
      }
    }
    finally
    {
      this.mQueueLock.unlock();
    }
    if (this.mIsClosed)
      throw new QueueClosedException();
    boolean bool = this.mQueue.add(paramE);
    this.mNotEmpty.signal();
    this.mQueueLock.unlock();
  }

  public E take()
    throws InterruptedException
  {
    try
    {
      this.mQueueLock.lock();
      try
      {
        while ((this.mQueue.isEmpty()) && (!this.mIsClosed))
          this.mNotEmpty.await();
      }
      catch (InterruptedException localInterruptedException)
      {
        this.mNotEmpty.signal();
        throw localInterruptedException;
      }
    }
    finally
    {
      this.mQueueLock.unlock();
    }
    Object localObject2;
    if (!this.mQueue.isEmpty())
    {
      localObject2 = this.mQueue.removeFirst();
      this.mNotFull.signal();
      this.mQueueLock.unlock();
    }
    while (true)
    {
      return localObject2;
      localObject2 = null;
      this.mQueueLock.unlock();
    }
  }

  public ArrayList<E> take(int paramInt)
    throws InterruptedException
  {
    ArrayList localArrayList = new java/util/ArrayList;
    int i;
    int j;
    if (paramInt > 0)
    {
      i = paramInt;
      localArrayList.<init>(i);
      j = 0;
    }
    while (true)
    {
      Object localObject;
      if (j < paramInt)
      {
        localObject = take();
        if (localObject != null);
      }
      else
      {
        return localArrayList;
        i = 1;
        break;
      }
      boolean bool = localArrayList.add(localObject);
      j += 1;
    }
  }

  public static class QueueClosedException extends RuntimeException
  {
    public QueueClosedException()
    {
      super();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.common.ClosableBlockingQueue
 * JD-Core Version:    0.6.2
 */