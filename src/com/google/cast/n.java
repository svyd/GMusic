package com.google.cast;

import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;

final class n extends NetworkTask
{
  private CastContext a;
  private String b;
  private ApplicationMetadata c;
  private MimeData d;
  private Uri e;
  private Uri f;
  private Uri g;
  private Uri h;
  private long i;
  private boolean j;
  private Logger k;

  public n(CastContext paramCastContext, Uri paramUri1, String paramString, Uri paramUri2)
  {
    super(arrayOfNetworkRequest);
    Logger localLogger = new Logger("StartSessionTask");
    this.k = localLogger;
    if (paramUri1 == null)
      throw new IllegalArgumentException("applicationBaseUrl cannot be null");
    if (paramString == null)
      throw new IllegalArgumentException("applicationName cannot be null");
    if (paramUri2 == null)
      throw new IllegalArgumentException("applicationInstanceUrl cannot be null");
    if ((paramUri1 == null) || (paramUri2 == null))
      throw new IllegalArgumentException("applicationUrl and applicationInstanceUrl cannot be null");
    n localn = this;
    CastContext localCastContext = paramCastContext;
    Uri localUri1 = paramUri1;
    String str = paramString;
    Uri localUri2 = paramUri2;
    localn.a(localCastContext, localUri1, str, null, localUri2);
  }

  public n(CastContext paramCastContext, Uri paramUri, String paramString, MimeData paramMimeData)
  {
    super(arrayOfNetworkRequest);
    Logger localLogger = new Logger("StartSessionTask");
    this.k = localLogger;
    if (paramUri == null)
      throw new IllegalArgumentException("applicationBaseUrl cannot be null");
    if ((paramMimeData != null) && (TextUtils.isEmpty(paramString)))
      throw new IllegalArgumentException("applicationName cannot be null if applicationArgument is provided");
    n localn = this;
    CastContext localCastContext = paramCastContext;
    Uri localUri = paramUri;
    String str = paramString;
    MimeData localMimeData = paramMimeData;
    localn.a(localCastContext, localUri, str, localMimeData, null);
  }

  private void a(CastContext paramCastContext, Uri paramUri1, String paramString, MimeData paramMimeData, Uri paramUri2)
  {
    this.a = paramCastContext;
    this.e = paramUri1;
    this.b = paramString;
    this.d = paramMimeData;
    this.g = paramUri2;
    if (paramString == null)
      return;
    Uri.Builder localBuilder = this.e.buildUpon();
    String str = this.b;
    Uri localUri = localBuilder.appendPath(str).build();
    this.f = localUri;
  }

  private void a(g paramg)
  {
    if (this.j)
      return;
    ApplicationMetadata localApplicationMetadata = paramg.a();
    this.c = localApplicationMetadata;
    if (this.c != null)
    {
      String str1 = this.c.getName();
      this.b = str1;
      Logger localLogger = this.k;
      Object[] arrayOfObject = new Object[1];
      String str2 = this.b;
      arrayOfObject[0] = str2;
      localLogger.d("got app name: %s", arrayOfObject);
      Uri.Builder localBuilder = this.e.buildUpon();
      String str3 = this.b;
      Uri localUri = localBuilder.appendPath(str3).build();
      this.f = localUri;
    }
    this.j = true;
  }

  public String a()
  {
    return this.b;
  }

  public Uri b()
  {
    return this.h;
  }

  public long c()
  {
    return this.i;
  }

  public Uri d()
  {
    return this.g;
  }

  protected Integer doInBackground(Void[] paramArrayOfVoid)
  {
    Uri localUri1 = null;
    g localg1;
    long l1;
    Integer localInteger1;
    if (this.g == null)
      if (this.b == null)
      {
        Logger localLogger1 = this.k;
        Object[] arrayOfObject1 = new Object[0];
        localLogger1.d("No application name supplied, so peforming GetApplicationInfoRequest", arrayOfObject1);
        CastContext localCastContext1 = this.a;
        Uri localUri2 = this.e;
        localg1 = new g(localCastContext1, localUri2);
        this.mCurrentRequest = localg1;
        l1 = localg1.execute();
        if (isCancelled())
          localInteger1 = Integer.valueOf(65437);
      }
    while (true)
    {
      return localInteger1;
      if (l1 == -1)
      {
        localInteger1 = Integer.valueOf(1);
        continue;
      }
      if (l1 != null)
      {
        Logger localLogger2 = this.k;
        Object[] arrayOfObject2 = new Object[1];
        Integer localInteger2 = Integer.valueOf(l1);
        arrayOfObject2[0] = localInteger2;
        localLogger2.d("GetApplicationInfoRequest failed with status: %d", arrayOfObject2);
        localInteger1 = Integer.valueOf(l1);
        continue;
      }
      if (localg1.a() == null)
      {
        localInteger1 = Integer.valueOf(1);
        continue;
      }
      a(localg1);
      localUri1 = localg1.d();
      Logger localLogger3 = this.k;
      Object[] arrayOfObject3 = new Object[1];
      Uri localUri3 = this.f;
      arrayOfObject3[0] = localUri3;
      localLogger3.d("performing StartApplicationRequest on app url: %s", arrayOfObject3);
      CastContext localCastContext2 = this.a;
      Uri localUri4 = this.f;
      MimeData localMimeData = this.d;
      m localm = new m(localCastContext2, localUri4, localMimeData);
      this.mCurrentRequest = localm;
      int m = localm.execute();
      if (isCancelled())
      {
        localInteger1 = Integer.valueOf(65437);
        continue;
      }
      if (m != 0)
      {
        Logger localLogger4 = this.k;
        Object[] arrayOfObject4 = new Object[1];
        Integer localInteger3 = Integer.valueOf(m);
        arrayOfObject4[0] = localInteger3;
        localLogger4.w("StartApplicationRequest failed with status: %d", arrayOfObject4);
        localInteger1 = Integer.valueOf(m);
        continue;
      }
      Uri localUri5 = localm.a();
      this.g = localUri5;
      Logger localLogger5 = this.k;
      Object[] arrayOfObject5 = new Object[1];
      Uri localUri6 = this.g;
      arrayOfObject5[0] = localUri6;
      localLogger5.d("got app instance url: %s", arrayOfObject5);
      long l2 = System.currentTimeMillis();
      long l3 = 10000L + l2;
      label388: if ((l2 < l3) && (localUri1 == null))
      {
        Logger localLogger6 = this.k;
        Object[] arrayOfObject6 = new Object[1];
        Uri localUri7 = this.f;
        arrayOfObject6[0] = localUri7;
        localLogger6.d("no connection service info yet; performing GetApplicationInfoRequest for %s", arrayOfObject6);
        CastContext localCastContext3 = this.a;
        Uri localUri8 = this.f;
        g localg2 = new g(localCastContext3, localUri8);
        this.mCurrentRequest = localg2;
        int n = localg2.execute();
        if (isCancelled())
        {
          localInteger1 = Integer.valueOf(65437);
          continue;
        }
        if (n != 0)
        {
          Logger localLogger7 = this.k;
          Object[] arrayOfObject7 = new Object[1];
          Integer localInteger4 = Integer.valueOf(n);
          arrayOfObject7[0] = localInteger4;
          localLogger7.d("GetApplicationInfoRequest failed with status: %d", arrayOfObject7);
          localInteger1 = Integer.valueOf(n);
          continue;
        }
        if (localg2.b() != 1)
        {
          Logger localLogger8 = this.k;
          Object[] arrayOfObject8 = new Object[0];
          localLogger8.d("Application is no longer running!", arrayOfObject8);
          localInteger1 = Integer.valueOf(1);
          continue;
        }
        if (this.g != null)
        {
          Uri localUri9 = this.g;
          Uri localUri10 = localg2.c();
          if (!localUri9.equals(localUri10))
          {
            Logger localLogger9 = this.k;
            Object[] arrayOfObject9 = new Object[0];
            localLogger9.d("Application instance is different!", arrayOfObject9);
            localInteger1 = Integer.valueOf(2);
            continue;
          }
        }
        if (!localg2.e())
        {
          Logger localLogger10 = this.k;
          Object[] arrayOfObject10 = new Object[0];
          localLogger10.d("Session does not support channels", arrayOfObject10);
          localInteger1 = Integer.valueOf(0);
          continue;
        }
        localUri1 = localg2.d();
        if (localUri1 == null)
          break label750;
        Logger localLogger11 = this.k;
        Object[] arrayOfObject11 = new Object[1];
        arrayOfObject11[0] = localUri1;
        localLogger11.d("Got connection service URL: %s", arrayOfObject11);
        a(localg2);
      }
      label750: long l4;
      if (localUri1 == null)
      {
        Logger localLogger12 = this.k;
        Object[] arrayOfObject12 = new Object[0];
        localLogger12.w("Expected a channel but never got a connection service URL", arrayOfObject12);
        localInteger1 = Integer.valueOf(3);
        continue;
        l4 = System.currentTimeMillis() - l2;
        if (l4 < 1000L)
          l4 = 1000L - l4;
      }
      try
      {
        Thread.sleep(l4);
        label780: l2 = System.currentTimeMillis();
        if (!isCancelled())
          break label388;
        localInteger1 = Integer.valueOf(65437);
        continue;
        Logger localLogger13 = this.k;
        Object[] arrayOfObject13 = new Object[0];
        localLogger13.d("performing an OpenChannelRequest", arrayOfObject13);
        CastContext localCastContext4 = this.a;
        k localk = new k(localCastContext4, localUri1);
        this.mCurrentRequest = localk;
        int i1 = localk.execute();
        if (isCancelled())
        {
          localInteger1 = Integer.valueOf(65437);
          continue;
        }
        if (i1 != 0)
        {
          Logger localLogger14 = this.k;
          Object[] arrayOfObject14 = new Object[1];
          Integer localInteger5 = Integer.valueOf(i1);
          arrayOfObject14[0] = localInteger5;
          localLogger14.d("OpenChannelRequest failed with status: %d", arrayOfObject14);
          localInteger1 = Integer.valueOf(i1);
          continue;
        }
        Uri localUri11 = localk.a();
        this.h = localUri11;
        long l5 = localk.b();
        this.i = l5;
        Logger localLogger15 = this.k;
        Object[] arrayOfObject15 = new Object[1];
        Uri localUri12 = this.h;
        arrayOfObject15[0] = localUri12;
        localLogger15.d("got a channel URL: %s", arrayOfObject15);
        localInteger1 = Integer.valueOf(0);
      }
      catch (InterruptedException localInterruptedException)
      {
        break label780;
      }
    }
  }

  public final ApplicationMetadata e()
  {
    return this.c;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.n
 * JD-Core Version:    0.6.2
 */