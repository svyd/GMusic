package com.google.cast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

class SsdpScanner
{
  private Context a;
  private List<MulticastSocket> b;
  private InetSocketAddress c;
  private String d;
  private int e;
  private int f;
  private Listener g;
  private Handler h;
  private volatile boolean i;
  private List<Thread> j;
  private AtomicBoolean k;
  private ConnectivityManager l;
  private a m;
  private WifiManager n;
  private String o;
  private c p;
  private volatile boolean q;
  private Logger r;

  public SsdpScanner(Context paramContext, String paramString, int paramInt1, int paramInt2, Handler paramHandler)
  {
    Logger localLogger1 = new Logger("SsdpScanner");
    this.r = localLogger1;
    if (paramString == null)
      throw new IllegalArgumentException("target cannot be null");
    if (paramInt1 < 1)
      throw new IllegalArgumentException("delay must be at least 1 second");
    this.a = paramContext;
    this.d = paramString;
    this.f = paramInt2;
    this.e = paramInt1;
    this.h = paramHandler;
    InetSocketAddress localInetSocketAddress = new InetSocketAddress("239.255.255.250", 1900);
    this.c = localInetSocketAddress;
    AtomicBoolean localAtomicBoolean = new AtomicBoolean();
    this.k = localAtomicBoolean;
    ArrayList localArrayList1 = new ArrayList();
    this.b = localArrayList1;
    ArrayList localArrayList2 = new ArrayList();
    this.j = localArrayList2;
    ConnectivityManager localConnectivityManager = (ConnectivityManager)this.a.getSystemService("connectivity");
    this.l = localConnectivityManager;
    if (this.a.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == 0)
    {
      WifiManager localWifiManager = (WifiManager)this.a.getSystemService("wifi");
      this.n = localWifiManager;
      return;
    }
    Logger localLogger2 = this.r;
    Object[] arrayOfObject = new Object[0];
    localLogger2.d("Don't have Wifi permissions.", arrayOfObject);
  }

  // ERROR //
  private void a(MulticastSocket paramMulticastSocket)
  {
    // Byte code:
    //   0: sipush 4096
    //   3: newarray byte
    //   5: astore_2
    //   6: aload_2
    //   7: arraylength
    //   8: istore_3
    //   9: new 161	java/net/DatagramPacket
    //   12: dup
    //   13: aload_2
    //   14: iload_3
    //   15: invokespecial 164	java/net/DatagramPacket:<init>	([BI)V
    //   18: astore 4
    //   20: aload_0
    //   21: getfield 166	com/google/cast/SsdpScanner:i	Z
    //   24: astore 5
    //   26: aload 5
    //   28: ifnull +4 -> 32
    //   31: return
    //   32: aload_1
    //   33: aload 4
    //   35: invokevirtual 172	java/net/MulticastSocket:receive	(Ljava/net/DatagramPacket;)V
    //   38: aload_0
    //   39: getfield 166	com/google/cast/SsdpScanner:i	Z
    //   42: ifeq +4 -> 46
    //   45: return
    //   46: new 21	com/google/cast/SsdpScanner$SsdpResponse
    //   49: dup
    //   50: invokespecial 173	com/google/cast/SsdpScanner$SsdpResponse:<init>	()V
    //   53: astore 5
    //   55: aload 4
    //   57: invokevirtual 177	java/net/DatagramPacket:getData	()[B
    //   60: astore 6
    //   62: aload 4
    //   64: invokevirtual 181	java/net/DatagramPacket:getOffset	()I
    //   67: istore 7
    //   69: aload 4
    //   71: invokevirtual 184	java/net/DatagramPacket:getLength	()I
    //   74: istore 8
    //   76: new 186	java/lang/String
    //   79: dup
    //   80: aload 6
    //   82: iload 7
    //   84: iload 8
    //   86: ldc 188
    //   88: invokespecial 191	java/lang/String:<init>	([BIILjava/lang/String;)V
    //   91: astore 9
    //   93: new 193	java/io/StringReader
    //   96: dup
    //   97: aload 9
    //   99: invokespecial 194	java/io/StringReader:<init>	(Ljava/lang/String;)V
    //   102: astore 10
    //   104: new 196	java/io/BufferedReader
    //   107: dup
    //   108: aload 10
    //   110: invokespecial 199	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   113: astore 11
    //   115: aload 11
    //   117: invokevirtual 203	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   120: astore 12
    //   122: aload 12
    //   124: ifnull +370 -> 494
    //   127: invokestatic 209	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   130: astore 13
    //   132: aload 12
    //   134: aload 13
    //   136: invokevirtual 213	java/lang/String:toUpperCase	(Ljava/util/Locale;)Ljava/lang/String;
    //   139: astore 14
    //   141: aload 14
    //   143: ldc 215
    //   145: invokevirtual 219	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   148: ifeq +102 -> 250
    //   151: ldc 215
    //   153: invokevirtual 222	java/lang/String:length	()I
    //   156: istore 15
    //   158: aload 12
    //   160: iload 15
    //   162: invokevirtual 226	java/lang/String:substring	(I)Ljava/lang/String;
    //   165: invokevirtual 229	java/lang/String:trim	()Ljava/lang/String;
    //   168: astore 16
    //   170: aload 5
    //   172: aload 16
    //   174: invokestatic 232	com/google/cast/SsdpScanner$SsdpResponse:a	(Lcom/google/cast/SsdpScanner$SsdpResponse;Ljava/lang/String;)V
    //   177: goto -62 -> 115
    //   180: astore 17
    //   182: aload_0
    //   183: getfield 71	com/google/cast/SsdpScanner:r	Lcom/google/cast/Logger;
    //   186: astore 18
    //   188: new 234	java/lang/StringBuilder
    //   191: dup
    //   192: invokespecial 235	java/lang/StringBuilder:<init>	()V
    //   195: ldc 237
    //   197: invokevirtual 241	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   200: astore 19
    //   202: aload_0
    //   203: getfield 166	com/google/cast/SsdpScanner:i	Z
    //   206: istore 20
    //   208: aload 19
    //   210: iload 20
    //   212: invokevirtual 244	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
    //   215: invokevirtual 247	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   218: astore 21
    //   220: iconst_0
    //   221: anewarray 4	java/lang/Object
    //   224: astore 22
    //   226: aload 18
    //   228: aload 17
    //   230: aload 21
    //   232: aload 22
    //   234: invokevirtual 250	com/google/cast/Logger:d	(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V
    //   237: aload_0
    //   238: getfield 166	com/google/cast/SsdpScanner:i	Z
    //   241: ifeq +4 -> 245
    //   244: return
    //   245: aload_0
    //   246: invokespecial 252	com/google/cast/SsdpScanner:e	()V
    //   249: return
    //   250: aload 14
    //   252: ldc 254
    //   254: invokevirtual 219	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   257: ifeq +32 -> 289
    //   260: ldc 254
    //   262: invokevirtual 222	java/lang/String:length	()I
    //   265: istore 23
    //   267: aload 12
    //   269: iload 23
    //   271: invokevirtual 226	java/lang/String:substring	(I)Ljava/lang/String;
    //   274: invokevirtual 229	java/lang/String:trim	()Ljava/lang/String;
    //   277: astore 24
    //   279: aload 5
    //   281: aload 24
    //   283: invokestatic 256	com/google/cast/SsdpScanner$SsdpResponse:b	(Lcom/google/cast/SsdpScanner$SsdpResponse;Ljava/lang/String;)V
    //   286: goto -171 -> 115
    //   289: aload 14
    //   291: ldc_w 258
    //   294: invokevirtual 219	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   297: ifeq +33 -> 330
    //   300: ldc_w 258
    //   303: invokevirtual 222	java/lang/String:length	()I
    //   306: istore 25
    //   308: aload 12
    //   310: iload 25
    //   312: invokevirtual 226	java/lang/String:substring	(I)Ljava/lang/String;
    //   315: invokevirtual 229	java/lang/String:trim	()Ljava/lang/String;
    //   318: astore 26
    //   320: aload 5
    //   322: aload 26
    //   324: invokestatic 260	com/google/cast/SsdpScanner$SsdpResponse:c	(Lcom/google/cast/SsdpScanner$SsdpResponse;Ljava/lang/String;)V
    //   327: goto -212 -> 115
    //   330: aload 14
    //   332: ldc_w 262
    //   335: invokevirtual 219	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   338: ifeq +33 -> 371
    //   341: ldc_w 262
    //   344: invokevirtual 222	java/lang/String:length	()I
    //   347: istore 27
    //   349: aload 12
    //   351: iload 27
    //   353: invokevirtual 226	java/lang/String:substring	(I)Ljava/lang/String;
    //   356: invokevirtual 229	java/lang/String:trim	()Ljava/lang/String;
    //   359: astore 28
    //   361: aload 5
    //   363: aload 28
    //   365: invokestatic 264	com/google/cast/SsdpScanner$SsdpResponse:d	(Lcom/google/cast/SsdpScanner$SsdpResponse;Ljava/lang/String;)V
    //   368: goto -253 -> 115
    //   371: aload 14
    //   373: ldc_w 266
    //   376: invokevirtual 219	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   379: ifeq +33 -> 412
    //   382: ldc_w 266
    //   385: invokevirtual 222	java/lang/String:length	()I
    //   388: istore 29
    //   390: aload 12
    //   392: iload 29
    //   394: invokevirtual 226	java/lang/String:substring	(I)Ljava/lang/String;
    //   397: invokevirtual 229	java/lang/String:trim	()Ljava/lang/String;
    //   400: astore 30
    //   402: aload 5
    //   404: aload 30
    //   406: invokestatic 268	com/google/cast/SsdpScanner$SsdpResponse:e	(Lcom/google/cast/SsdpScanner$SsdpResponse;Ljava/lang/String;)V
    //   409: goto -294 -> 115
    //   412: aload 14
    //   414: ldc_w 270
    //   417: invokevirtual 219	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   420: ifeq +33 -> 453
    //   423: ldc_w 270
    //   426: invokevirtual 222	java/lang/String:length	()I
    //   429: istore 31
    //   431: aload 12
    //   433: iload 31
    //   435: invokevirtual 226	java/lang/String:substring	(I)Ljava/lang/String;
    //   438: invokevirtual 229	java/lang/String:trim	()Ljava/lang/String;
    //   441: astore 32
    //   443: aload 5
    //   445: aload 32
    //   447: invokestatic 272	com/google/cast/SsdpScanner$SsdpResponse:f	(Lcom/google/cast/SsdpScanner$SsdpResponse;Ljava/lang/String;)V
    //   450: goto -335 -> 115
    //   453: aload 14
    //   455: ldc_w 274
    //   458: invokevirtual 219	java/lang/String:startsWith	(Ljava/lang/String;)Z
    //   461: ifeq -346 -> 115
    //   464: ldc_w 274
    //   467: invokevirtual 222	java/lang/String:length	()I
    //   470: istore 33
    //   472: aload 12
    //   474: iload 33
    //   476: invokevirtual 226	java/lang/String:substring	(I)Ljava/lang/String;
    //   479: invokevirtual 229	java/lang/String:trim	()Ljava/lang/String;
    //   482: astore 34
    //   484: aload 5
    //   486: aload 34
    //   488: invokestatic 276	com/google/cast/SsdpScanner$SsdpResponse:g	(Lcom/google/cast/SsdpScanner$SsdpResponse;Ljava/lang/String;)V
    //   491: goto -376 -> 115
    //   494: aload 11
    //   496: invokevirtual 279	java/io/BufferedReader:close	()V
    //   499: aload 5
    //   501: invokevirtual 283	com/google/cast/SsdpScanner$SsdpResponse:isValidCastDevice	()Z
    //   504: ifeq -484 -> 20
    //   507: aload_0
    //   508: getfield 285	com/google/cast/SsdpScanner:g	Lcom/google/cast/SsdpScanner$Listener;
    //   511: ifnull -491 -> 20
    //   514: aload_0
    //   515: getfield 88	com/google/cast/SsdpScanner:h	Landroid/os/Handler;
    //   518: astore 35
    //   520: new 8	com/google/cast/SsdpScanner$2
    //   523: dup
    //   524: aload_0
    //   525: aload 5
    //   527: invokespecial 288	com/google/cast/SsdpScanner$2:<init>	(Lcom/google/cast/SsdpScanner;Lcom/google/cast/SsdpScanner$SsdpResponse;)V
    //   530: astore 36
    //   532: aload 35
    //   534: aload 36
    //   536: invokevirtual 294	android/os/Handler:post	(Ljava/lang/Runnable;)Z
    //   539: istore 37
    //   541: goto -521 -> 20
    //   544: astore 38
    //   546: goto -526 -> 20
    //
    // Exception table:
    //   from	to	target	type
    //   20	26	180	java/io/IOException
    //   32	38	180	java/io/IOException
    //   38	177	180	java/io/IOException
    //   250	541	180	java/io/IOException
    //   32	38	544	java/net/SocketTimeoutException
  }

  private void a(List<NetworkInterface> paramList)
  {
    int i1 = 1;
    Logger localLogger1 = this.r;
    Object[] arrayOfObject1 = new Object[0];
    localLogger1.d("startInternal", arrayOfObject1);
    if ((paramList == null) || (paramList.isEmpty()))
    {
      Logger localLogger2 = this.r;
      Object[] arrayOfObject2 = new Object[0];
      localLogger2.d("No suitable network interface!", arrayOfObject2);
      f();
      return;
    }
    try
    {
      Iterator localIterator1 = paramList.iterator();
      while (localIterator1.hasNext())
      {
        NetworkInterface localNetworkInterface = (NetworkInterface)localIterator1.next();
        MulticastSocket localMulticastSocket1 = new MulticastSocket();
        localMulticastSocket1.setNetworkInterface(localNetworkInterface);
        localMulticastSocket1.setTimeToLive(2);
        boolean bool1 = this.b.add(localMulticastSocket1);
      }
    }
    catch (IOException localIOException)
    {
      Logger localLogger3 = this.r;
      Object[] arrayOfObject3 = new Object[0];
      localLogger3.e(localIOException, "couldn't create socket", arrayOfObject3);
      f();
      return;
      this.p = null;
      this.q = false;
      this.i = false;
      this.k.set(false);
      Thread local1 = new Thread()
      {
        public void run()
        {
          SsdpScanner.a(SsdpScanner.this);
        }
      };
      local1.setName("SsdpScanner send thread");
      local1.start();
      boolean bool2 = this.j.add(local1);
      int i2 = this.b.size();
      Iterator localIterator2 = this.b.iterator();
      int i3 = 1;
      while (localIterator2.hasNext())
      {
        MulticastSocket localMulticastSocket2 = (MulticastSocket)localIterator2.next();
        b localb = new b(localMulticastSocket2);
        Thread localThread = new Thread(localb);
        String str1 = "SsdpScanner receive thread #" + i3 + " of " + i2;
        localThread.setName(str1);
        localThread.start();
        boolean bool3 = this.j.add(localThread);
        i3 += 1;
      }
    }
    finally
    {
      this.p = null;
    }
    WifiInfo localWifiInfo;
    if (this.n != null)
    {
      localWifiInfo = this.n.getConnectionInfo();
      if (localWifiInfo == null)
        break label501;
    }
    label501: for (String str2 = localWifiInfo.getBSSID(); ; str2 = null)
    {
      if ((this.o == null) || (str2 == null) || (!this.o.equals(str2)))
      {
        Logger localLogger4 = this.r;
        Object[] arrayOfObject4 = new Object[0];
        localLogger4.d("BSSID changed", arrayOfObject4);
      }
      while (true)
      {
        this.o = str2;
        if ((i1 != 0) && (this.g != null))
          this.g.onResultsInvalidated();
        Logger localLogger5 = this.r;
        Object[] arrayOfObject5 = new Object[0];
        localLogger5.d("scan started", arrayOfObject5);
        return;
        i1 = 0;
      }
    }
  }

  private void c()
  {
    if (this.p != null)
    {
      boolean bool = this.p.cancel(true);
      this.p = null;
    }
    if (this.b.isEmpty())
      return;
    this.i = true;
    Iterator localIterator1 = this.b.iterator();
    while (localIterator1.hasNext())
      ((MulticastSocket)localIterator1.next()).close();
    Iterator localIterator2 = this.j.iterator();
    if (localIterator2.hasNext())
    {
      Thread localThread = (Thread)localIterator2.next();
      localThread.interrupt();
      while (true)
        try
        {
          localThread.join();
        }
        catch (InterruptedException localInterruptedException)
        {
        }
    }
    this.j.clear();
    this.b.clear();
  }

  private void d()
  {
    Locale localLocale = Locale.getDefault();
    Object[] arrayOfObject1 = new Object[4];
    arrayOfObject1[0] = "239.255.255.250";
    Integer localInteger1 = Integer.valueOf(1900);
    arrayOfObject1[1] = localInteger1;
    Integer localInteger2 = Integer.valueOf(this.e);
    arrayOfObject1[2] = localInteger2;
    String str1 = this.d;
    arrayOfObject1[3] = str1;
    int i1 = String.format(localLocale, "M-SEARCH * HTTP/1.1\r\nHOST: %s:%d\r\nMAN: \"ssdp:discover\"\r\nMX: %d\r\nST: %s\r\n\r\n", arrayOfObject1).getBytes();
    try
    {
      int i2 = i1.length;
      InetSocketAddress localInetSocketAddress = this.c;
      DatagramPacket localDatagramPacket = new DatagramPacket(i1, i2, localInetSocketAddress);
      while (true)
      {
        if (this.i)
          return;
        Iterator localIterator = this.b.iterator();
        boolean bool1;
        while (localIterator.hasNext())
        {
          ((MulticastSocket)localIterator.next()).send(localDatagramPacket);
          try
          {
            Thread.sleep(this.f);
          }
          catch (InterruptedException localInterruptedException)
          {
            bool1 = this.i;
          }
        }
        if (!bool1)
          break;
      }
    }
    catch (IOException localIOException)
    {
      Logger localLogger = this.r;
      StringBuilder localStringBuilder = new StringBuilder().append("Send thread got an exception; mShouldStop=");
      boolean bool2 = this.i;
      String str2 = bool2;
      Object[] arrayOfObject2 = new Object[0];
      localLogger.d(localIOException, str2, arrayOfObject2);
      if (this.i)
        return;
      e();
    }
  }

  private void e()
  {
    if (this.k.getAndSet(true))
      return;
    if (this.g == null)
      return;
    Handler localHandler = this.h;
    Runnable local3 = new Runnable()
    {
      public void run()
      {
        SsdpScanner.e(SsdpScanner.this);
      }
    };
    boolean bool = localHandler.post(local3);
  }

  private void f()
  {
    if (this.q)
      return;
    Logger localLogger = this.r;
    Object[] arrayOfObject = new Object[0];
    localLogger.d("reportNetworkError; errorState now true", arrayOfObject);
    this.q = true;
    if (this.g == null)
      return;
    this.g.onNetworkError();
  }

  private void g()
  {
    if (this.m != null)
      return;
    a locala1 = new a(null);
    this.m = locala1;
    IntentFilter localIntentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
    Context localContext = this.a;
    a locala2 = this.m;
    Intent localIntent = localContext.registerReceiver(locala2, localIntentFilter);
  }

  private void h()
  {
    if (this.m == null)
      return;
    try
    {
      Context localContext = this.a;
      a locala = this.m;
      localContext.unregisterReceiver(locala);
      label23: this.m = null;
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      break label23;
    }
  }

  public void a()
  {
    Logger localLogger = this.r;
    Object[] arrayOfObject = new Object[0];
    localLogger.d("start", arrayOfObject);
    if (!this.b.isEmpty())
      return;
    if (this.p != null)
      return;
    g();
    c localc1 = new c(null);
    this.p = localc1;
    c localc2 = this.p;
    Void[] arrayOfVoid = new Void[0];
    AsyncTask localAsyncTask = localc2.execute(arrayOfVoid);
  }

  public void a(Listener paramListener)
  {
    this.g = paramListener;
  }

  public void b()
  {
    h();
    c();
  }

  private class b
    implements Runnable
  {
    private MulticastSocket b;

    public b(MulticastSocket arg2)
    {
      Object localObject;
      this.b = localObject;
    }

    public void run()
    {
      SsdpScanner localSsdpScanner = SsdpScanner.this;
      MulticastSocket localMulticastSocket = this.b;
      SsdpScanner.a(localSsdpScanner, localMulticastSocket);
    }
  }

  private class a extends BroadcastReceiver
  {
    private a()
    {
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      NetworkInfo localNetworkInfo = SsdpScanner.f(SsdpScanner.this).getActiveNetworkInfo();
      if ((localNetworkInfo != null) && (localNetworkInfo.isConnected()));
      for (boolean bool = true; ; bool = false)
      {
        Logger localLogger1 = SsdpScanner.b(SsdpScanner.this);
        Object[] arrayOfObject1 = new Object[2];
        Boolean localBoolean1 = Boolean.valueOf(bool);
        arrayOfObject1[0] = localBoolean1;
        Boolean localBoolean2 = Boolean.valueOf(SsdpScanner.g(SsdpScanner.this));
        arrayOfObject1[1] = localBoolean2;
        localLogger1.d("connectivity state changed; connected? %b, errorState? %b", arrayOfObject1);
        if (!bool)
          String str = SsdpScanner.a(SsdpScanner.this, null);
        SsdpScanner.h(SsdpScanner.this);
        if (!bool)
          break;
        Logger localLogger2 = SsdpScanner.b(SsdpScanner.this);
        Object[] arrayOfObject2 = new Object[0];
        localLogger2.d("re-established connectivity after connectivity changed; restarting scan", arrayOfObject2);
        SsdpScanner.this.a();
        return;
      }
      if (SsdpScanner.g(SsdpScanner.this))
        return;
      Logger localLogger3 = SsdpScanner.b(SsdpScanner.this);
      Object[] arrayOfObject3 = new Object[0];
      localLogger3.d("lost connectivity while scanning;", arrayOfObject3);
      SsdpScanner.i(SsdpScanner.this);
    }
  }

  public static abstract interface Listener
  {
    public abstract void onNetworkError();

    public abstract void onResultsInvalidated();

    public abstract void onSsdpResponse(SsdpScanner.SsdpResponse paramSsdpResponse);
  }

  public static final class SsdpResponse
  {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;

    private void a(String paramString)
    {
      this.a = paramString;
    }

    private void b(String paramString)
    {
      this.b = paramString;
    }

    private void c(String paramString)
    {
      this.c = paramString;
    }

    private void d(String paramString)
    {
      this.d = paramString;
    }

    private void e(String paramString)
    {
      this.e = paramString;
    }

    private void f(String paramString)
    {
      this.f = paramString;
    }

    private void g(String paramString)
    {
      this.g = paramString;
    }

    public String getLocation()
    {
      return this.c;
    }

    public String getUsn()
    {
      return this.f;
    }

    public boolean isValidCastDevice()
    {
      if ((!TextUtils.isEmpty(this.c)) && (!TextUtils.isEmpty(this.f)));
      for (boolean bool = true; ; bool = false)
        return bool;
    }
  }

  private class c extends AsyncTask<Void, Void, List<NetworkInterface>>
  {
    private c()
    {
    }

    protected List<NetworkInterface> a(Void[] paramArrayOfVoid)
    {
      ArrayList localArrayList = new ArrayList();
      try
      {
        Enumeration localEnumeration = NetworkInterface.getNetworkInterfaces();
        if (localEnumeration != null);
        while (true)
        {
          if ((!localEnumeration.hasMoreElements()) || (isCancelled()))
            return localArrayList;
          NetworkInterface localNetworkInterface = (NetworkInterface)localEnumeration.nextElement();
          if ((localNetworkInterface.isUp()) && (!localNetworkInterface.isLoopback()) && (!localNetworkInterface.isPointToPoint()) && (localNetworkInterface.supportsMulticast()))
          {
            Iterator localIterator = localNetworkInterface.getInterfaceAddresses().iterator();
            while (localIterator.hasNext())
              if ((((InterfaceAddress)localIterator.next()).getAddress() instanceof Inet4Address))
                boolean bool = localArrayList.add(localNetworkInterface);
          }
        }
      }
      catch (IOException localIOException)
      {
        while (true)
        {
          Logger localLogger = SsdpScanner.b(SsdpScanner.this);
          Object[] arrayOfObject = new Object[0];
          localLogger.d(localIOException, "Exception while selecting network interface", arrayOfObject);
        }
      }
    }

    protected void a(List<NetworkInterface> paramList)
    {
      if (SsdpScanner.c(SsdpScanner.this) != this)
        return;
      if (paramList != null)
      {
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
        {
          NetworkInterface localNetworkInterface = (NetworkInterface)localIterator.next();
          Logger localLogger = SsdpScanner.b(SsdpScanner.this);
          Object[] arrayOfObject = new Object[1];
          String str = localNetworkInterface.getDisplayName();
          arrayOfObject[0] = str;
          localLogger.d("Multicast using: %s", arrayOfObject);
        }
      }
      SsdpScanner.a(SsdpScanner.this, paramList);
    }

    protected void b(List<NetworkInterface> paramList)
    {
      if (SsdpScanner.c(SsdpScanner.this) != this)
        return;
      c localc = SsdpScanner.a(SsdpScanner.this, null);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.SsdpScanner
 * JD-Core Version:    0.6.2
 */