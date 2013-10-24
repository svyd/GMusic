package com.google.cast;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import java.io.IOException;

public class ApplicationSession
{
  private Logger a;
  private CastContext b;
  private CastDevice c;
  private int d = 32768;
  private ApplicationMetadata e;
  private Uri f;
  private String g;
  private Listener h;
  private ApplicationChannel.a i;
  private ApplicationChannel j;
  private Handler k;
  private n l;
  private NetworkTask.Listener m;
  private NetworkTask n;
  private NetworkTask.Listener o;
  private AsyncTask<Uri, Void, Boolean> p;
  private SessionError q;
  private int r;
  private int s;
  private boolean t;
  private boolean u;
  private boolean v;
  private boolean w;

  public ApplicationSession(CastContext paramCastContext, CastDevice paramCastDevice)
    throws IllegalArgumentException
  {
    this(paramCastContext, paramCastDevice, 32768);
  }

  public ApplicationSession(CastContext paramCastContext, CastDevice paramCastDevice, int paramInt)
    throws IllegalArgumentException
  {
    if (paramInt < 4096)
      throw new IllegalArgumentException("bufferSize too small");
    if (paramCastContext == null)
      throw new IllegalArgumentException("castContext cannot be null");
    if (paramCastDevice == null)
      throw new IllegalArgumentException("device cannot be null");
    Logger localLogger = new Logger("ApplicationSession");
    this.a = localLogger;
    this.s = 0;
    this.b = paramCastContext;
    this.c = paramCastDevice;
    this.d = paramInt;
    NetworkTask.Listener local1 = new NetworkTask.Listener()
    {
      public void onTaskCancelled()
      {
        Logger localLogger = ApplicationSession.b(ApplicationSession.this);
        Object[] arrayOfObject = new Object[0];
        localLogger.d("StartSessionTask cancelled", arrayOfObject);
        n localn = ApplicationSession.a(ApplicationSession.this, null);
        ApplicationSession.a(ApplicationSession.this, null);
      }

      public void onTaskCompleted()
      {
        if (ApplicationSession.a(ApplicationSession.this))
        {
          n localn1 = ApplicationSession.a(ApplicationSession.this, null);
          ApplicationSession.a(ApplicationSession.this, null);
          return;
        }
        Logger localLogger1 = ApplicationSession.b(ApplicationSession.this);
        Object[] arrayOfObject1 = new Object[0];
        localLogger1.d("StartSessionTask completed", arrayOfObject1);
        long l = ApplicationSession.c(ApplicationSession.this).c();
        Uri localUri1 = ApplicationSession.c(ApplicationSession.this).b();
        ApplicationSession localApplicationSession1 = ApplicationSession.this;
        ApplicationMetadata localApplicationMetadata1 = ApplicationSession.c(ApplicationSession.this).e();
        ApplicationMetadata localApplicationMetadata2 = ApplicationSession.a(localApplicationSession1, localApplicationMetadata1);
        ApplicationSession localApplicationSession2 = ApplicationSession.this;
        String str1 = ApplicationSession.c(ApplicationSession.this).a();
        String str2 = ApplicationSession.a(localApplicationSession2, str1);
        ApplicationSession localApplicationSession3 = ApplicationSession.this;
        Uri localUri2 = ApplicationSession.c(ApplicationSession.this).d();
        Uri localUri3 = ApplicationSession.a(localApplicationSession3, localUri2);
        n localn2 = ApplicationSession.a(ApplicationSession.this, null);
        if (localUri1 != null)
        {
          Logger localLogger2 = ApplicationSession.b(ApplicationSession.this);
          Object[] arrayOfObject2 = new Object[1];
          arrayOfObject2[0] = localUri1;
          localLogger2.d("connecting channel at URL: %s", arrayOfObject2);
          ApplicationSession localApplicationSession4 = ApplicationSession.this;
          CastContext localCastContext = ApplicationSession.d(ApplicationSession.this);
          int i = ApplicationSession.e(ApplicationSession.this);
          ApplicationChannel.a locala = ApplicationSession.f(ApplicationSession.this);
          Handler localHandler = ApplicationSession.g(ApplicationSession.this);
          CastDevice localCastDevice = ApplicationSession.h(ApplicationSession.this);
          ApplicationChannel localApplicationChannel1 = new ApplicationChannel(localCastContext, i, l, locala, localHandler, localCastDevice);
          ApplicationChannel localApplicationChannel2 = ApplicationSession.a(localApplicationSession4, localApplicationChannel1);
          ApplicationSession.b(ApplicationSession.this, localUri1);
          if (!ApplicationSession.i(ApplicationSession.this).isProtocolSupported("ramp"))
            return;
          Context localContext = ApplicationSession.d(ApplicationSession.this).getApplicationContext();
          String str3 = ApplicationSession.j(ApplicationSession.this);
          if (ApplicationSession.k(ApplicationSession.this) == null);
          for (String str4 = ""; ; str4 = ApplicationSession.k(ApplicationSession.this).toString())
          {
            int j = ApplicationSession.l(ApplicationSession.this);
            d.a(localContext, str3, str4, j);
            return;
          }
        }
        ApplicationSession.m(ApplicationSession.this);
      }

      public void onTaskFailed(int paramAnonymousInt)
      {
        int i = 2;
        if (ApplicationSession.a(ApplicationSession.this))
        {
          n localn1 = ApplicationSession.a(ApplicationSession.this, null);
          ApplicationSession.a(ApplicationSession.this, null);
          return;
        }
        Logger localLogger1 = ApplicationSession.b(ApplicationSession.this);
        Object[] arrayOfObject1 = new Object[1];
        Integer localInteger = Integer.valueOf(paramAnonymousInt);
        arrayOfObject1[0] = localInteger;
        localLogger1.d("StartSessionTask failed; status=%d", arrayOfObject1);
        n localn2 = ApplicationSession.a(ApplicationSession.this, null);
        if (paramAnonymousInt == -1)
          i = 1;
        while (true)
        {
          SessionError localSessionError = new SessionError(1, i);
          Logger localLogger2 = ApplicationSession.b(ApplicationSession.this);
          Object[] arrayOfObject2 = new Object[1];
          String str = localSessionError.toString();
          arrayOfObject2[0] = str;
          localLogger2.e("StartSessionTask failed with error: %s", arrayOfObject2);
          ApplicationSession.a(ApplicationSession.this, localSessionError);
          return;
          if (paramAnonymousInt == 1)
            i = 4;
          else if (paramAnonymousInt == 2)
            i = 5;
          else if (paramAnonymousInt == 3)
            i = 6;
        }
      }
    };
    this.m = local1;
    NetworkTask.Listener local2 = new NetworkTask.Listener()
    {
      public void onTaskCancelled()
      {
      }

      public void onTaskCompleted()
      {
        boolean bool = ApplicationSession.a(ApplicationSession.this, true);
        NetworkTask localNetworkTask = ApplicationSession.a(ApplicationSession.this, null);
        String str = ApplicationSession.a(ApplicationSession.this, null);
        Uri localUri = ApplicationSession.a(ApplicationSession.this, null);
        ApplicationSession.a(ApplicationSession.this, null);
      }

      public void onTaskFailed(int paramAnonymousInt)
      {
        int i = 1;
        boolean bool = ApplicationSession.a(ApplicationSession.this, i);
        NetworkTask localNetworkTask = ApplicationSession.a(ApplicationSession.this, null);
        SessionError localSessionError;
        if (paramAnonymousInt == -1)
        {
          String str = ApplicationSession.a(ApplicationSession.this, null);
          Uri localUri = ApplicationSession.a(ApplicationSession.this, null);
          localSessionError = null;
          ApplicationSession.a(ApplicationSession.this, localSessionError);
          return;
        }
        if (paramAnonymousInt == -1);
        while (true)
        {
          localSessionError = new SessionError(4, i);
          break;
          int j = 2;
        }
      }
    };
    this.o = local2;
    ApplicationChannel.a local3 = new ApplicationChannel.a()
    {
      public void a(ApplicationChannel paramAnonymousApplicationChannel)
      {
        Logger localLogger = ApplicationSession.b(ApplicationSession.this);
        Object[] arrayOfObject = new Object[0];
        localLogger.d("Channel is connected", arrayOfObject);
        if (ApplicationSession.a(ApplicationSession.this))
        {
          ApplicationSession.a(ApplicationSession.this, null);
          return;
        }
        ApplicationSession.m(ApplicationSession.this);
      }

      public void a(ApplicationChannel paramAnonymousApplicationChannel, int paramAnonymousInt)
      {
        int i = 1;
        Logger localLogger = ApplicationSession.b(ApplicationSession.this);
        Object[] arrayOfObject = new Object[i];
        Integer localInteger = Integer.valueOf(paramAnonymousInt);
        arrayOfObject[0] = localInteger;
        localLogger.d("channel connection failed with error: %d", arrayOfObject);
        ApplicationChannel localApplicationChannel = ApplicationSession.a(ApplicationSession.this, null);
        if (paramAnonymousInt == -1);
        while (true)
        {
          ApplicationSession localApplicationSession = ApplicationSession.this;
          SessionError localSessionError = new SessionError(2, i);
          ApplicationSession.a(localApplicationSession, localSessionError);
          return;
          i = 3;
        }
      }

      public void b(ApplicationChannel paramAnonymousApplicationChannel, int paramAnonymousInt)
      {
        Logger localLogger = ApplicationSession.b(ApplicationSession.this);
        Object[] arrayOfObject = new Object[1];
        Integer localInteger = Integer.valueOf(paramAnonymousInt);
        arrayOfObject[0] = localInteger;
        localLogger.d("channel disconnected with error: %d", arrayOfObject);
        ApplicationChannel localApplicationChannel = ApplicationSession.a(ApplicationSession.this, null);
        switch (paramAnonymousInt)
        {
        default:
          ApplicationSession.a(ApplicationSession.this, null);
          return;
        case -2:
          ApplicationSession localApplicationSession1 = ApplicationSession.this;
          SessionError localSessionError1 = new SessionError(3, 1);
          ApplicationSession.a(localApplicationSession1, localSessionError1);
          return;
        case -1:
        }
        ApplicationSession localApplicationSession2 = ApplicationSession.this;
        SessionError localSessionError2 = new SessionError(3, 3);
        ApplicationSession.a(localApplicationSession2, localSessionError2);
      }
    };
    this.i = local3;
    Looper localLooper = Looper.getMainLooper();
    Handler localHandler = new Handler(localLooper);
    this.k = localHandler;
  }

  private void a()
  {
    this.s = 0;
    this.w = false;
    this.v = false;
    this.t = false;
    this.q = null;
  }

  private void a(Uri paramUri)
  {
    AsyncTask local4 = new AsyncTask()
    {
      protected Boolean a(Uri[] paramAnonymousArrayOfUri)
      {
        boolean bool = true;
        try
        {
          Logger localLogger1 = ApplicationSession.b(ApplicationSession.this);
          Object[] arrayOfObject1 = new Object[1];
          Uri localUri1 = paramAnonymousArrayOfUri[0];
          arrayOfObject1[0] = localUri1;
          localLogger1.d("trying to connect channel to: %s", arrayOfObject1);
          ApplicationChannel localApplicationChannel = ApplicationSession.n(ApplicationSession.this);
          Uri localUri2 = paramAnonymousArrayOfUri[0];
          localApplicationChannel.a(localUri2);
          AsyncTask localAsyncTask = ApplicationSession.a(ApplicationSession.this, null);
          return Boolean.valueOf(bool);
        }
        catch (IOException localIOException)
        {
          while (true)
          {
            Logger localLogger2 = ApplicationSession.b(ApplicationSession.this);
            Object[] arrayOfObject2 = new Object[0];
            localLogger2.e(localIOException, "error while connecting channel", arrayOfObject2);
            bool = false;
          }
        }
      }

      protected void a(Boolean paramAnonymousBoolean)
      {
        Logger localLogger = ApplicationSession.b(ApplicationSession.this);
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = paramAnonymousBoolean;
        localLogger.d("ConnectChannelTask completed with success status: %b", arrayOfObject);
        AsyncTask localAsyncTask = ApplicationSession.a(ApplicationSession.this, null);
        if (ApplicationSession.a(ApplicationSession.this))
        {
          ApplicationSession.a(ApplicationSession.this, null);
          return;
        }
        if (paramAnonymousBoolean.booleanValue())
          return;
        ApplicationSession localApplicationSession = ApplicationSession.this;
        SessionError localSessionError = new SessionError(2, 3);
        ApplicationSession.a(localApplicationSession, localSessionError);
      }

      protected void onCancelled()
      {
        AsyncTask localAsyncTask = ApplicationSession.a(ApplicationSession.this, null);
        ApplicationSession.a(ApplicationSession.this, null);
      }
    };
    this.p = local4;
    AsyncTask localAsyncTask1 = this.p;
    Uri[] arrayOfUri = new Uri[1];
    arrayOfUri[0] = paramUri;
    AsyncTask localAsyncTask2 = localAsyncTask1.execute(arrayOfUri);
  }

  private void a(SessionError paramSessionError)
  {
    this.s = 3;
    if (this.q == null)
      this.q = paramSessionError;
    if (this.j != null)
    {
      this.j.a();
      return;
    }
    this.e = null;
    if ((this.u) && (!this.v) && (this.f != null))
    {
      NetworkRequest[] arrayOfNetworkRequest = new NetworkRequest[1];
      CastContext localCastContext = this.b;
      Uri localUri = this.f;
      o localo = new o(localCastContext, localUri);
      arrayOfNetworkRequest[0] = localo;
      NetworkTask localNetworkTask1 = new NetworkTask(arrayOfNetworkRequest);
      this.n = localNetworkTask1;
      NetworkTask localNetworkTask2 = this.n;
      NetworkTask.Listener localListener = this.o;
      localNetworkTask2.setListener(localListener);
      this.n.execute();
      return;
    }
    this.s = 0;
    if ((this.w) || (this.t))
    {
      if (this.h == null)
        return;
      Listener localListener1 = this.h;
      SessionError localSessionError1 = this.q;
      localListener1.onSessionEnded(localSessionError1);
      return;
    }
    if (this.h == null)
      return;
    Listener localListener2 = this.h;
    SessionError localSessionError2 = this.q;
    localListener2.onSessionStartFailed(localSessionError2);
  }

  private void b()
  {
    Logger localLogger = this.a;
    Object[] arrayOfObject = new Object[0];
    localLogger.d("Notifying listener(s) that startup is complete", arrayOfObject);
    this.w = true;
    this.s = 2;
    if (this.h == null)
      return;
    Listener localListener = this.h;
    ApplicationMetadata localApplicationMetadata = this.e;
    localListener.onSessionStarted(localApplicationMetadata);
  }

  public boolean endSession()
    throws IOException
  {
    int i1 = 0;
    boolean bool1 = true;
    p.a();
    this.t = bool1;
    switch (this.s)
    {
    default:
      bool1 = false;
    case 1:
    case 2:
    }
    while (true)
    {
      return bool1;
      if (this.p != null)
      {
        boolean bool2 = this.p.cancel(true);
        i1 = 1;
      }
      if (this.l != null)
      {
        boolean bool3 = this.l.cancel(true);
        i1 = 1;
      }
      if (i1 == 0)
      {
        a(null);
        continue;
        a(null);
      }
    }
  }

  public ApplicationChannel getChannel()
  {
    return this.j;
  }

  public boolean isResumable()
  {
    if ((this.s == 0) && (this.f != null) && (this.g != null));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isStarting()
  {
    int i1 = 1;
    if (this.s != i1);
    while (true)
    {
      return i1;
      int i2 = 0;
    }
  }

  public void resumeSession()
    throws IOException, IllegalStateException
  {
    p.a();
    if (this.s != 0)
      throw new IllegalStateException("Session is not currently stopped");
    if ((this.f == null) || (this.g == null))
      throw new IllegalStateException("No previous session to resume");
    a();
    this.s = 1;
    CastContext localCastContext = this.b;
    Uri localUri1 = this.c.getApplicationUrl();
    String str = this.g;
    Uri localUri2 = this.f;
    n localn1 = new n(localCastContext, localUri1, str, localUri2);
    this.l = localn1;
    n localn2 = this.l;
    NetworkTask.Listener localListener = this.m;
    localn2.setListener(localListener);
    this.l.execute();
  }

  public void setApplicationOptions(int paramInt)
    throws IllegalArgumentException
  {
    if ((paramInt > 3) || (paramInt < 0))
    {
      String str = "invalid flags: " + paramInt;
      throw new IllegalArgumentException(str);
    }
    this.r = paramInt;
  }

  public void setListener(Listener paramListener)
  {
    this.h = paramListener;
  }

  public void setStopApplicationWhenEnding(boolean paramBoolean)
    throws IllegalStateException
  {
    if (this.s == 3)
      throw new IllegalStateException("Call is not allowed while session is stopping");
    this.u = paramBoolean;
  }

  public void startSession(String paramString, MimeData paramMimeData)
    throws IOException, IllegalStateException
  {
    p.a();
    if (this.s != 0)
      throw new IllegalStateException("Session is not currently stopped");
    a();
    this.s = 1;
    CastContext localCastContext = this.b;
    Uri localUri = this.c.getApplicationUrl();
    n localn1 = new n(localCastContext, localUri, paramString, paramMimeData);
    this.l = localn1;
    n localn2 = this.l;
    NetworkTask.Listener localListener = this.m;
    localn2.setListener(localListener);
    this.l.execute();
  }

  public static abstract interface Listener
  {
    public abstract void onSessionEnded(SessionError paramSessionError);

    public abstract void onSessionStartFailed(SessionError paramSessionError);

    public abstract void onSessionStarted(ApplicationMetadata paramApplicationMetadata);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.ApplicationSession
 * JD-Core Version:    0.6.2
 */