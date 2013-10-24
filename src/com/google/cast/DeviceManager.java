package com.google.cast;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

class DeviceManager
{
  private static final int a = (int)TimeUnit.SECONDS.toSeconds(1L);
  private static final int b = (int)TimeUnit.MILLISECONDS.toMillis(1000L);
  private static final int c = b * 10;
  private static final int d = (int)TimeUnit.SECONDS.toMillis(3L);
  private CastContext e;
  private b f;
  private List<Listener> g;
  private SsdpScanner h;
  private Handler i;
  private Runnable j;
  private int k = 0;
  private Logger l;

  public DeviceManager(CastContext paramCastContext)
    throws IllegalArgumentException
  {
    Logger localLogger = new Logger("DeviceManager");
    this.l = localLogger;
    if (paramCastContext == null)
      throw new IllegalArgumentException("castContext cannot be null");
    this.e = paramCastContext;
    b localb = new b();
    this.f = localb;
    ArrayList localArrayList = new ArrayList();
    this.g = localArrayList;
    Looper localLooper = Looper.getMainLooper();
    Handler localHandler = new Handler(localLooper);
    this.i = localHandler;
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        DeviceManager.a(DeviceManager.this);
      }
    };
    this.j = local1;
  }

  private Inet4Address a(String paramString)
  {
    String[] arrayOfString = TextUtils.split(paramString, "\\.");
    Inet4Address localInet4Address;
    if (arrayOfString.length != 4)
      localInet4Address = null;
    while (true)
    {
      return localInet4Address;
      byte[] arrayOfByte = new byte[4];
      int m = 0;
      while (true)
      {
        if (m >= 4)
          break label64;
        try
        {
          int n = (byte)Integer.parseInt(arrayOfString[m]);
          arrayOfByte[m] = n;
          m += 1;
        }
        catch (NumberFormatException localNumberFormatException)
        {
          localInet4Address = null;
        }
      }
      continue;
      label64: InetAddress localInetAddress2;
      try
      {
        InetAddress localInetAddress1 = InetAddress.getByAddress(arrayOfByte);
        localInetAddress2 = localInetAddress1;
        if ((localInetAddress2 instanceof Inet4Address))
          break label95;
        localInet4Address = null;
      }
      catch (UnknownHostException localUnknownHostException)
      {
        localInet4Address = null;
      }
      continue;
      label95: localInet4Address = (Inet4Address)localInetAddress2;
    }
  }

  private void a(final int paramInt)
  {
    if (this.k != paramInt)
      return;
    Logger localLogger = this.l;
    Object[] arrayOfObject = new Object[1];
    Integer localInteger = Integer.valueOf(paramInt);
    arrayOfObject[0] = localInteger;
    localLogger.d("notifyStateChanged: %d", arrayOfObject);
    this.k = paramInt;
    final List localList = g();
    if (localList == null)
      return;
    Handler localHandler = this.i;
    Runnable local4 = new Runnable()
    {
      public void run()
      {
        Iterator localIterator = localList.iterator();
        while (true)
        {
          if (!localIterator.hasNext())
            return;
          DeviceManager.Listener localListener = (DeviceManager.Listener)localIterator.next();
          int i = paramInt;
          localListener.onScanStateChanged(i);
        }
      }
    };
    boolean bool = localHandler.post(local4);
  }

  private void a(SsdpScanner.SsdpResponse paramSsdpResponse)
  {
    long l1 = System.currentTimeMillis();
    Uri localUri = Uri.parse(paramSsdpResponse.getLocation());
    String str1 = localUri.getHost();
    Inet4Address localInet4Address1 = a(str1);
    if (localInet4Address1 == null)
      return;
    a locala1;
    while (true)
    {
      synchronized (this.f)
      {
        localIterator = this.f.a.iterator();
        if (!localIterator.hasNext())
          break;
        locala1 = (a)localIterator.next();
        String str2 = locala1.a;
        String str3 = paramSsdpResponse.getUsn();
        if (!str2.equals(str3))
          continue;
        if (!locala1.f)
        {
          if (locala1.b.getIpAddress().equals(localInet4Address1))
            locala1.e = l1;
        }
        else
          return;
      }
      localInet4Address1 = null;
      locala1.f = localInet4Address1;
    }
    Iterator localIterator = this.f.b.iterator();
    while (localIterator.hasNext())
    {
      locala1 = (a)localIterator.next();
      String str4 = locala1.a;
      String str5 = paramSsdpResponse.getUsn();
      if (str4.equals(str5))
      {
        if (!locala1.f)
        {
          if (!locala1.b.getIpAddress().equals(localInet4Address1))
            break label250;
          locala1.e = l1;
        }
        while (true)
        {
          return;
          label250: locala1.f = true;
        }
      }
    }
    final a locala2 = new a(null);
    Inet4Address localInet4Address2 = (Inet4Address)localInet4Address1;
    CastDevice localCastDevice1 = new CastDevice(localInet4Address2);
    locala2.b = localCastDevice1;
    String str6 = paramSsdpResponse.getUsn();
    locala2.a = str6;
    locala2.e = l1;
    CastContext localCastContext = this.e;
    CastDevice localCastDevice2 = locala2.b;
    h localh1 = new h(localCastContext, localUri, localCastDevice2);
    locala2.c = localh1;
    NetworkRequest[] arrayOfNetworkRequest = new NetworkRequest[1];
    h localh2 = locala2.c;
    arrayOfNetworkRequest[0] = localh2;
    NetworkTask localNetworkTask1 = new NetworkTask(arrayOfNetworkRequest);
    locala2.d = localNetworkTask1;
    NetworkTask localNetworkTask2 = locala2.d;
    NetworkTask.Listener local3 = new NetworkTask.Listener()
    {
      public void onTaskCancelled()
      {
        synchronized (DeviceManager.c(DeviceManager.this))
        {
          List localList = DeviceManager.c(DeviceManager.this).a;
          DeviceManager.a locala = locala2;
          boolean bool = localList.remove(locala);
          return;
        }
      }

      public void onTaskCompleted()
      {
        synchronized (DeviceManager.c(DeviceManager.this))
        {
          List localList1 = DeviceManager.c(DeviceManager.this).a;
          DeviceManager.a locala1 = locala2;
          boolean bool1 = localList1.remove(locala1);
          List localList2 = DeviceManager.c(DeviceManager.this).b;
          DeviceManager.a locala2 = locala2;
          boolean bool2 = localList2.add(locala2);
          locala2.c = null;
          locala2.d = null;
          CastDevice localCastDevice = locala2.b;
          Logger localLogger = DeviceManager.d(DeviceManager.this);
          Object[] arrayOfObject = new Object[0];
          localLogger.d("onDeviceOnline", arrayOfObject);
          List localList3 = DeviceManager.e(DeviceManager.this);
          if (localList3 == null)
            return;
          Iterator localIterator = localList3.iterator();
          if (!localIterator.hasNext())
            return;
          ((DeviceManager.Listener)localIterator.next()).onDeviceOnline(localCastDevice);
        }
      }

      public void onTaskFailed(int paramAnonymousInt)
      {
        locala2.f = true;
      }
    };
    localNetworkTask2.setListener(local3);
    boolean bool = this.f.a.add(locala2);
    locala2.d.execute();
  }

  private void d()
  {
    if (this.h == null)
      return;
    this.h.b();
    this.i.removeCallbacksAndMessages(null);
    synchronized (this.f)
    {
      Iterator localIterator = this.f.a.iterator();
      while (localIterator.hasNext())
      {
        a locala = (a)localIterator.next();
        if (locala.d != null)
          locala.d.cancel();
      }
    }
    this.f.a.clear();
  }

  private void e()
  {
    Object localObject1 = null;
    long l1 = System.currentTimeMillis();
    synchronized (this.f)
    {
      localIterator1 = this.f.a.iterator();
      while (localIterator1.hasNext())
        if (((a)localIterator1.next()).a(l1))
          localIterator1.remove();
    }
    Iterator localIterator1 = this.f.b.iterator();
    while (localIterator1.hasNext())
    {
      a locala = (a)localIterator1.next();
      if ((locala.f) || (locala.a(l1)))
      {
        if (localObject1 == null)
          localObject1 = new LinkedList();
        CastDevice localCastDevice = locala.b;
        boolean bool1 = ((List)localObject1).add(localCastDevice);
        localIterator1.remove();
      }
      localObject1 = localObject1;
    }
    if (localObject1 != null)
    {
      List localList = g();
      if (localList != null)
      {
        Iterator localIterator2 = ((List)localObject1).iterator();
        while (localIterator2.hasNext())
        {
          localObject1 = (CastDevice)localIterator2.next();
          ??? = localList.iterator();
          while (((Iterator)???).hasNext())
            ((Listener)((Iterator)???).next()).onDeviceOffline((CastDevice)localObject1);
        }
      }
    }
    Handler localHandler = this.i;
    Runnable localRunnable = this.j;
    long l2 = d;
    boolean bool2 = localHandler.postDelayed(localRunnable, l2);
  }

  private void f()
  {
    Logger localLogger = this.l;
    Object[] arrayOfObject = new Object[0];
    localLogger.d("flushing device list", arrayOfObject);
    synchronized (this.f)
    {
      this.f.a.clear();
      if (this.f.b.isEmpty())
        break label168;
      List localList = g();
      if (localList != null)
      {
        Iterator localIterator1 = this.f.b.iterator();
        while (localIterator1.hasNext())
        {
          a locala = (a)localIterator1.next();
          Iterator localIterator2 = localList.iterator();
          if (localIterator2.hasNext())
          {
            Listener localListener = (Listener)localIterator2.next();
            CastDevice localCastDevice = locala.b;
            localListener.onDeviceOffline(localCastDevice);
          }
        }
      }
    }
    this.f.b.clear();
    label168:
  }

  private List<Listener> g()
  {
    ArrayList localArrayList = null;
    synchronized (this.g)
    {
      if (!this.g.isEmpty())
      {
        List localList2 = this.g;
        localArrayList = new ArrayList(localList2);
      }
      return localArrayList;
    }
  }

  public void a()
  {
    Logger localLogger = this.l;
    Object[] arrayOfObject = new Object[0];
    localLogger.d("startScan", arrayOfObject);
    p.a();
    if (this.h == null)
    {
      Context localContext = this.e.getApplicationContext();
      int m = a;
      int n = b;
      Handler localHandler1 = this.i;
      SsdpScanner localSsdpScanner1 = new SsdpScanner(localContext, "urn:dial-multiscreen-org:service:dial:1", m, n, localHandler1);
      this.h = localSsdpScanner1;
      SsdpScanner localSsdpScanner2 = this.h;
      SsdpScanner.Listener local2 = new SsdpScanner.Listener()
      {
        public void onNetworkError()
        {
          DeviceManager.b(DeviceManager.this);
          DeviceManager.a(DeviceManager.this, 2);
        }

        public void onResultsInvalidated()
        {
          DeviceManager.b(DeviceManager.this);
        }

        public void onSsdpResponse(SsdpScanner.SsdpResponse paramAnonymousSsdpResponse)
        {
          DeviceManager.a(DeviceManager.this, paramAnonymousSsdpResponse);
        }
      };
      localSsdpScanner2.a(local2);
    }
    a(1);
    this.h.a();
    Handler localHandler2 = this.i;
    Runnable localRunnable = this.j;
    long l1 = d;
    boolean bool = localHandler2.postDelayed(localRunnable, l1);
  }

  public void a(Listener paramListener)
    throws IllegalArgumentException
  {
    if (paramListener == null)
      throw new IllegalArgumentException("listener cannot be null");
    synchronized (this.g)
    {
      if (this.g.contains(paramListener))
        throw new IllegalArgumentException("the same listener cannot be added twice");
    }
    boolean bool = this.g.add(paramListener);
  }

  public void b()
  {
    p.a();
    d();
    a(0);
  }

  private class b
  {
    public final List<DeviceManager.a> a;
    public final List<DeviceManager.a> b;

    public b()
    {
      ArrayList localArrayList1 = new ArrayList();
      this.a = localArrayList1;
      ArrayList localArrayList2 = new ArrayList();
      this.b = localArrayList2;
    }
  }

  private class a
  {
    String a;
    CastDevice b;
    h c;
    NetworkTask d;
    long e;
    boolean f;

    private a()
    {
    }

    boolean a(long paramLong)
    {
      long l1 = this.e;
      long l2 = paramLong - l1;
      long l3 = DeviceManager.c();
      if (l2 >= l3);
      for (boolean bool = true; ; bool = false)
        return bool;
    }
  }

  public static abstract interface Listener
  {
    public abstract void onDeviceOffline(CastDevice paramCastDevice);

    public abstract void onDeviceOnline(CastDevice paramCastDevice);

    public abstract void onScanStateChanged(int paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.DeviceManager
 * JD-Core Version:    0.6.2
 */