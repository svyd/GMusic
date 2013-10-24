package com.google.cast;

import android.util.Base64;
import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

class q
{
  private static final q e = new q();
  private LinkedList<WebSocket> a;
  private LinkedList<WebSocket> b;
  private Selector c;
  private SecureRandom d;
  private volatile boolean f;
  private volatile boolean g;
  private volatile Thread h;
  private Charset i;
  private AtomicBoolean j;
  private volatile Throwable k;
  private Logger l;

  private q()
  {
    LinkedList localLinkedList1 = new LinkedList();
    this.a = localLinkedList1;
    LinkedList localLinkedList2 = new LinkedList();
    this.b = localLinkedList2;
    AtomicBoolean localAtomicBoolean = new AtomicBoolean(false);
    this.j = localAtomicBoolean;
    SecureRandom localSecureRandom = new SecureRandom();
    this.d = localSecureRandom;
    Logger localLogger1 = new Logger("WebSocketMultiplexer");
    this.l = localLogger1;
    try
    {
      Charset localCharset = Charset.forName("UTF-8");
      this.i = localCharset;
      return;
    }
    catch (IllegalCharsetNameException localIllegalCharsetNameException)
    {
      Logger localLogger2 = this.l;
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = "UTF-8";
      localLogger2.e(localIllegalCharsetNameException, "Can't find charset %s", arrayOfObject1);
      return;
    }
    catch (UnsupportedCharsetException localUnsupportedCharsetException)
    {
      Logger localLogger3 = this.l;
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = "UTF-8";
      localLogger3.e("Can't find charset %s", arrayOfObject2);
    }
  }

  public static q a()
  {
    return e;
  }

  private void g()
    throws IOException
  {
    if (this.f)
      return;
    long l1 = System.currentTimeMillis();
    if (this.j.getAndSet(false));
    while (true)
    {
      Object localObject4;
      while (true)
      {
        WebSocket localWebSocket2;
        synchronized (this.b)
        {
          Iterator localIterator1 = this.b.iterator();
          if (localIterator1.hasNext())
          {
            WebSocket localWebSocket1 = (WebSocket)localIterator1.next();
            try
            {
              SocketChannel localSocketChannel1 = localWebSocket1.i();
              Selector localSelector1 = this.c;
              Object localObject3 = localSocketChannel1.register(localSelector1, 0).attach(localWebSocket1);
              boolean bool1 = this.a.add(localWebSocket1);
            }
            catch (Exception localException)
            {
            }
            continue;
          }
          this.b.clear();
          Iterator localIterator2 = this.a.iterator();
          localObject4 = null;
          if (!localIterator2.hasNext())
            break label225;
          localWebSocket2 = (WebSocket)localIterator2.next();
          SocketChannel localSocketChannel2 = localWebSocket2.i();
          if (localSocketChannel2 != null)
          {
            Selector localSelector2 = this.c;
            SelectionKey localSelectionKey1 = localSocketChannel2.keyFor(localSelector2);
            if (localWebSocket2.a(localSelectionKey1, l1));
          }
          else
          {
            localIterator2.remove();
            Object localObject5 = localObject4;
            localObject4 = localObject5;
          }
        }
        if (localWebSocket2.c())
        {
          int m = 1;
          continue;
          try
          {
            label225: Object localObject1 = this.c;
            if (localObject4 != null);
            for (long l2 = 1000L; ; l2 = 0L)
            {
              int n = ((Selector)localObject1).select(l2);
              if (n == 0)
                break;
              label253: if (!this.f)
                break label269;
              return;
            }
            label269: localObject1 = this.c.selectedKeys().iterator();
            while (((Iterator)localObject1).hasNext())
              try
              {
                SelectionKey localSelectionKey2 = (SelectionKey)((Iterator)localObject1).next();
                localObject4 = (WebSocket)localSelectionKey2.attachment();
                if ((localSelectionKey2.isConnectable()) && (!((WebSocket)localObject4).f()))
                  boolean bool2 = this.a.remove(localObject4);
                if ((localSelectionKey2.isReadable()) && (!((WebSocket)localObject4).g()))
                  boolean bool3 = this.a.remove(localObject4);
                if ((localSelectionKey2.isWritable()) && (!((WebSocket)localObject4).h()))
                  boolean bool4 = this.a.remove(localObject4);
                label393: ((Iterator)localObject1).remove();
              }
              catch (CancelledKeyException localCancelledKeyException)
              {
                break label393;
              }
          }
          catch (IllegalArgumentException localIllegalArgumentException)
          {
            break label253;
          }
        }
      }
      Object localObject6 = localObject4;
    }
  }

  private void h()
    throws IllegalStateException
  {
    if (this.h == null)
      throw new IllegalStateException("not started; call start()");
    if (!this.g)
      return;
    StringBuffer localStringBuffer1 = new StringBuffer();
    StringBuffer localStringBuffer2 = localStringBuffer1.append("selector thread aborted due to ");
    if (this.k != null)
    {
      String str1 = this.k.getClass().getName();
      StringBuffer localStringBuffer3 = localStringBuffer1.append(str1);
      StackTraceElement[] arrayOfStackTraceElement = this.k.getStackTrace();
      StringBuffer localStringBuffer4 = localStringBuffer1.append(" at ");
      String str2 = arrayOfStackTraceElement[0].getFileName();
      StringBuffer localStringBuffer5 = localStringBuffer4.append(str2).append(':');
      int m = arrayOfStackTraceElement[0].getLineNumber();
      StringBuffer localStringBuffer6 = localStringBuffer5.append(m);
    }
    while (true)
    {
      String str3 = localStringBuffer1.toString();
      throw new IllegalStateException(str3);
      StringBuffer localStringBuffer7 = localStringBuffer1.append("unknown condition");
    }
  }

  /** @deprecated */
  public void a(WebSocket paramWebSocket)
    throws IllegalStateException
  {
    try
    {
      h();
      synchronized (this.b)
      {
        boolean bool = this.b.add(paramWebSocket);
        this.j.set(true);
        Selector localSelector = this.c.wakeup();
        return;
      }
    }
    finally
    {
    }
  }

  /** @deprecated */
  public void b()
    throws IOException
  {
    try
    {
      Thread localThread1 = this.h;
      if (localThread1 != null);
      while (true)
      {
        return;
        localThread1 = null;
        this.g = localThread1;
        Selector localSelector = Selector.open();
        this.c = localSelector;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            try
            {
              q.a(q.this);
              Thread localThread1;
              return;
            }
            catch (Throwable localThrowable1)
            {
              Logger localLogger = q.b(q.this);
              Object[] arrayOfObject = new Object[0];
              localLogger.e(localThrowable1, "Unexpected throwable in selector loop", arrayOfObject);
              Throwable localThrowable2 = q.a(q.this, localThrowable1);
              boolean bool = q.a(q.this, true);
              Thread localThread2;
              return;
            }
            finally
            {
              Thread localThread3 = q.a(q.this, null);
            }
          }
        };
        Thread localThread2 = new Thread(local1);
        this.h = localThread2;
        this.h.start();
      }
    }
    finally
    {
    }
  }

  /** @deprecated */
  public void c()
    throws IllegalStateException
  {
    try
    {
      h();
      Selector localSelector = this.c.wakeup();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  String d()
  {
    byte[] arrayOfByte = new byte[16];
    this.d.nextBytes(arrayOfByte);
    int m = arrayOfByte.length;
    return Base64.encodeToString(arrayOfByte, 0, m, 2);
  }

  byte[] e()
  {
    byte[] arrayOfByte = new byte[4];
    this.d.nextBytes(arrayOfByte);
    return arrayOfByte;
  }

  Charset f()
  {
    return this.i;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.q
 * JD-Core Version:    0.6.2
 */