package com.google.android.youtube.player.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import java.util.ArrayList;

public abstract class r<T extends IInterface>
  implements t
{
  final Handler a;
  private final Context b;
  private T c;
  private ArrayList<t.a> d;
  private final ArrayList<t.a> e;
  private boolean f;
  private ArrayList<t.b> g;
  private boolean h;
  private final ArrayList<r<T>.b<?>> i;
  private ServiceConnection j;
  private boolean k;

  protected r(Context paramContext, t.a parama, t.b paramb)
  {
    ArrayList localArrayList1 = new ArrayList();
    this.e = localArrayList1;
    this.f = false;
    this.h = false;
    ArrayList localArrayList2 = new ArrayList();
    this.i = localArrayList2;
    this.k = false;
    Thread localThread1 = Looper.getMainLooper().getThread();
    Thread localThread2 = Thread.currentThread();
    if (localThread1 != localThread2)
      throw new IllegalStateException("Clients must be created on the UI thread.");
    Context localContext = (Context)ac.a(paramContext);
    this.b = localContext;
    ArrayList localArrayList3 = new ArrayList();
    this.d = localArrayList3;
    ArrayList localArrayList4 = this.d;
    Object localObject1 = ac.a(parama);
    boolean bool1 = localArrayList4.add(localObject1);
    ArrayList localArrayList5 = new ArrayList();
    this.g = localArrayList5;
    ArrayList localArrayList6 = this.g;
    Object localObject2 = ac.a(paramb);
    boolean bool2 = localArrayList6.add(localObject2);
    a locala = new a();
    this.a = locala;
  }

  private static YouTubeInitializationResult b(String paramString)
  {
    try
    {
      YouTubeInitializationResult localYouTubeInitializationResult1 = YouTubeInitializationResult.valueOf(paramString);
      localYouTubeInitializationResult2 = localYouTubeInitializationResult1;
      return localYouTubeInitializationResult2;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      while (true)
        localYouTubeInitializationResult2 = YouTubeInitializationResult.UNKNOWN_ERROR;
    }
    catch (NullPointerException localNullPointerException)
    {
      while (true)
        YouTubeInitializationResult localYouTubeInitializationResult2 = YouTubeInitializationResult.UNKNOWN_ERROR;
    }
  }

  protected abstract T a(IBinder paramIBinder);

  protected final void a(YouTubeInitializationResult paramYouTubeInitializationResult)
  {
    this.a.removeMessages(4);
    synchronized (this.g)
    {
      this.h = true;
      ArrayList localArrayList2 = this.g;
      int m = localArrayList2.size();
      int n = 0;
      while (n < m)
      {
        if (!this.k)
          return;
        ArrayList localArrayList3 = this.g;
        Object localObject1 = localArrayList2.get(n);
        if (localArrayList3.contains(localObject1))
          ((t.b)localArrayList2.get(n)).a(paramYouTubeInitializationResult);
        n += 1;
      }
      this.h = false;
      return;
    }
  }

  protected abstract void a(i parami, r<T>.d paramr)
    throws RemoteException;

  protected abstract String b();

  protected final void b(IBinder paramIBinder)
  {
    try
    {
      i locali = i.a.a(paramIBinder);
      d locald = new d();
      a(locali, locald);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      int m = Log.w("YouTubeClient", "service died");
    }
  }

  protected abstract String c();

  public void d()
  {
    h();
    this.k = false;
    synchronized (this.i)
    {
      int m = this.i.size();
      int n = 0;
      while (n < m)
      {
        ((b)this.i.get(n)).b();
        n += 1;
      }
      this.i.clear();
      this.c = null;
      if (this.j == null)
        return;
      Context localContext = this.b;
      ServiceConnection localServiceConnection = this.j;
      localContext.unbindService(localServiceConnection);
      this.j = null;
      return;
    }
  }

  public final void e()
  {
    this.k = true;
    YouTubeInitializationResult localYouTubeInitializationResult1 = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this.b);
    YouTubeInitializationResult localYouTubeInitializationResult2 = YouTubeInitializationResult.SUCCESS;
    if (localYouTubeInitializationResult1 != localYouTubeInitializationResult2)
    {
      Handler localHandler1 = this.a;
      Message localMessage1 = this.a.obtainMessage(3, localYouTubeInitializationResult1);
      boolean bool1 = localHandler1.sendMessage(localMessage1);
      return;
    }
    String str = c();
    Intent localIntent = new Intent(str);
    if (this.j != null)
    {
      int m = Log.e("YouTubeClient", "Calling connect() while still connected, missing disconnect().");
      this.c = null;
      Context localContext1 = this.b;
      ServiceConnection localServiceConnection1 = this.j;
      localContext1.unbindService(localServiceConnection1);
    }
    e locale = new e();
    this.j = locale;
    Context localContext2 = this.b;
    ServiceConnection localServiceConnection2 = this.j;
    if (localContext2.bindService(localIntent, localServiceConnection2, 129))
      return;
    Handler localHandler2 = this.a;
    Handler localHandler3 = this.a;
    YouTubeInitializationResult localYouTubeInitializationResult3 = YouTubeInitializationResult.ERROR_CONNECTING_TO_SERVICE;
    Message localMessage2 = localHandler3.obtainMessage(3, localYouTubeInitializationResult3);
    boolean bool2 = localHandler2.sendMessage(localMessage2);
  }

  public final boolean f()
  {
    if (this.c != null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  protected final void g()
  {
    int m = 1;
    synchronized (this.d)
    {
      if (!this.f)
      {
        boolean bool = null;
        ac.a(bool);
        this.a.removeMessages(4);
        this.f = true;
        if (this.e.size() != 0)
          break label139;
      }
      while (true)
      {
        ac.a(m);
        ArrayList localArrayList2 = this.d;
        int i1 = localArrayList2.size();
        m = 0;
        while ((m < i1) && (this.k) && (f()))
        {
          int i2 = this.e.size();
          ArrayList localArrayList3 = this.e;
          Object localObject1 = localArrayList2.get(m);
          if (!localArrayList3.contains(localObject1))
            ((t.a)localArrayList2.get(m)).a();
          m += 1;
        }
        localArrayList2 = null;
        break;
        label139: int n = 0;
      }
      this.e.clear();
      this.f = false;
      return;
    }
  }

  protected final void h()
  {
    this.a.removeMessages(4);
    synchronized (this.d)
    {
      this.f = true;
      ArrayList localArrayList2 = this.d;
      int m = localArrayList2.size();
      int n = 0;
      while ((n < m) && (this.k))
      {
        ArrayList localArrayList3 = this.d;
        Object localObject1 = localArrayList2.get(n);
        if (localArrayList3.contains(localObject1))
          ((t.a)localArrayList2.get(n)).b();
        n += 1;
      }
      this.f = false;
      return;
    }
  }

  protected final void i()
  {
    if (f())
      return;
    throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
  }

  protected final T j()
  {
    i();
    return this.c;
  }

  protected final class d extends c.a
  {
    protected d()
    {
    }

    public final void a(String paramString, IBinder paramIBinder)
    {
      Handler localHandler1 = r.this.a;
      Handler localHandler2 = r.this.a;
      r localr = r.this;
      r.c localc = new r.c(localr, paramString, paramIBinder);
      Message localMessage = localHandler2.obtainMessage(1, localc);
      boolean bool = localHandler1.sendMessage(localMessage);
    }
  }

  protected final class c extends r<T>.b<Boolean>
  {
    public final YouTubeInitializationResult b;
    public final IBinder c;

    public c(String paramIBinder, IBinder arg3)
    {
      super(localBoolean);
      YouTubeInitializationResult localYouTubeInitializationResult = r.a(paramIBinder);
      this.b = localYouTubeInitializationResult;
      Object localObject;
      this.c = localObject;
    }
  }

  protected abstract class b<TListener>
  {
    private TListener b;

    public b()
    {
      Object localObject1;
      this.b = localObject1;
      synchronized (r.c(r.this))
      {
        boolean bool = r.c(r.this).add(this);
        return;
      }
    }

    public final void a()
    {
      try
      {
        Object localObject1 = this.b;
        a(localObject1);
        return;
      }
      finally
      {
      }
    }

    protected abstract void a(TListener paramTListener);

    public final void b()
    {
      try
      {
        this.b = null;
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
  }

  final class a extends Handler
  {
    a()
    {
    }

    public final void handleMessage(Message paramMessage)
    {
      if (paramMessage.what == 3)
      {
        r localr = r.this;
        YouTubeInitializationResult localYouTubeInitializationResult = (YouTubeInitializationResult)paramMessage.obj;
        localr.a(localYouTubeInitializationResult);
        return;
      }
      if (paramMessage.what == 4)
        synchronized (r.a(r.this))
        {
          if ((r.b(r.this)) && (r.this.f()))
          {
            ArrayList localArrayList2 = r.a(r.this);
            Object localObject1 = paramMessage.obj;
            if (localArrayList2.contains(localObject1))
              ((t.a)paramMessage.obj).a();
          }
          return;
        }
      if ((paramMessage.what == 2) && (!r.this.f()))
        return;
      if ((paramMessage.what != 2) && (paramMessage.what != 1))
        return;
      ((r.b)paramMessage.obj).a();
    }
  }

  final class e
    implements ServiceConnection
  {
    e()
    {
    }

    public final void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      r.this.b(paramIBinder);
    }

    public final void onServiceDisconnected(ComponentName paramComponentName)
    {
      IInterface localIInterface = r.a(r.this, null);
      r.this.h();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.r
 * JD-Core Version:    0.6.2
 */