package com.google.cast;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class ApplicationChannel
{
  private Map<String, MessageStream> a;
  private WebSocket b;
  private int c;
  private a d;
  private Handler e;
  private Logger f;
  private i g;
  private CastDevice h;
  private CastContext i;
  private long j;
  private long k;
  private Runnable l;
  private Runnable m;

  ApplicationChannel(CastContext paramCastContext, int paramInt, long paramLong, a parama, Handler paramHandler, CastDevice paramCastDevice)
  {
    this.i = paramCastContext;
    this.c = paramInt;
    long l1 = 2L * paramLong;
    this.k = l1;
    this.d = parama;
    this.e = paramHandler;
    this.h = paramCastDevice;
    HashMap localHashMap = new HashMap();
    this.a = localHashMap;
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        ApplicationChannel.a(ApplicationChannel.this);
      }
    };
    this.l = local1;
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        try
        {
          JSONObject localJSONObject1 = new JSONObject();
          JSONObject localJSONObject2 = localJSONObject1.put("type", "pong");
          ApplicationChannel.a(ApplicationChannel.this, "cm", localJSONObject1);
          return;
        }
        catch (IOException localIOException)
        {
          ApplicationChannel.a(ApplicationChannel.this, -1);
          return;
        }
        catch (IllegalStateException localIllegalStateException)
        {
        }
        catch (JSONException localJSONException)
        {
        }
      }
    };
    this.m = local2;
    Logger localLogger = new Logger("ApplicationChannel");
    this.f = localLogger;
    i local3 = new i()
    {
      public void a(String paramAnonymousString, JSONObject paramAnonymousJSONObject)
        throws IOException
      {
        ApplicationChannel.a(ApplicationChannel.this, paramAnonymousString, paramAnonymousJSONObject);
      }
    };
    this.g = local3;
    int n = this.c;
    WebSocket localWebSocket1 = new WebSocket("https://www.google.com", "", n);
    this.b = localWebSocket1;
    WebSocket localWebSocket2 = this.b;
    WebSocket.Listener local4 = new WebSocket.Listener()
    {
      public void onConnected()
      {
        if (ApplicationChannel.b(ApplicationChannel.this) != 0L)
        {
          ApplicationChannel localApplicationChannel = ApplicationChannel.this;
          long l1 = SystemClock.uptimeMillis();
          long l2 = ApplicationChannel.a(localApplicationChannel, l1);
          Handler localHandler1 = ApplicationChannel.d(ApplicationChannel.this);
          Runnable localRunnable = ApplicationChannel.c(ApplicationChannel.this);
          boolean bool1 = localHandler1.postDelayed(localRunnable, 1000L);
        }
        Handler localHandler2 = ApplicationChannel.d(ApplicationChannel.this);
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            ApplicationChannel.a locala = ApplicationChannel.e(ApplicationChannel.this);
            ApplicationChannel localApplicationChannel = ApplicationChannel.this;
            locala.a(localApplicationChannel);
          }
        };
        boolean bool2 = localHandler2.post(local1);
      }

      public void onConnectionFailed(final int paramAnonymousInt)
      {
        long l = ApplicationChannel.a(ApplicationChannel.this, 0L);
        Handler localHandler = ApplicationChannel.d(ApplicationChannel.this);
        Runnable local2 = new Runnable()
        {
          public void run()
          {
            ApplicationChannel.a locala = ApplicationChannel.e(ApplicationChannel.this);
            ApplicationChannel localApplicationChannel1 = ApplicationChannel.this;
            ApplicationChannel localApplicationChannel2 = ApplicationChannel.this;
            int i = paramAnonymousInt;
            int j = ApplicationChannel.b(localApplicationChannel2, i);
            locala.a(localApplicationChannel1, j);
          }
        };
        boolean bool = localHandler.post(local2);
      }

      public void onContinuationMessageReceived(String paramAnonymousString, boolean paramAnonymousBoolean)
      {
        ApplicationChannel.a(ApplicationChannel.this, null, paramAnonymousString);
      }

      public void onContinuationMessageReceived(byte[] paramAnonymousArrayOfByte, boolean paramAnonymousBoolean)
      {
        ApplicationChannel.a(ApplicationChannel.this, null, paramAnonymousArrayOfByte);
      }

      public void onDisconnected(final int paramAnonymousInt1, int paramAnonymousInt2)
      {
        Logger localLogger = ApplicationChannel.f(ApplicationChannel.this);
        Object[] arrayOfObject = new Object[1];
        Integer localInteger = Integer.valueOf(paramAnonymousInt1);
        arrayOfObject[0] = localInteger;
        localLogger.d("onDisconnected(); error=%d", arrayOfObject);
        Handler localHandler = ApplicationChannel.d(ApplicationChannel.this);
        Runnable local3 = new Runnable()
        {
          public void run()
          {
            ApplicationChannel localApplicationChannel1 = ApplicationChannel.this;
            ApplicationChannel localApplicationChannel2 = ApplicationChannel.this;
            int i = paramAnonymousInt1;
            int j = ApplicationChannel.b(localApplicationChannel2, i);
            ApplicationChannel.a(localApplicationChannel1, j);
          }
        };
        boolean bool = localHandler.post(local3);
      }

      public void onMessageReceived(String paramAnonymousString, boolean paramAnonymousBoolean)
      {
        if (!paramAnonymousBoolean)
        {
          ApplicationChannel.a(ApplicationChannel.this, null, paramAnonymousString);
          return;
        }
        ApplicationChannel.a(ApplicationChannel.this, paramAnonymousString);
      }

      public void onMessageReceived(byte[] paramAnonymousArrayOfByte, boolean paramAnonymousBoolean)
      {
        ApplicationChannel.a(ApplicationChannel.this, null, paramAnonymousArrayOfByte);
      }
    };
    localWebSocket2.a(local4);
  }

  private void a(int paramInt)
  {
    b();
    this.b = null;
    detachAllMessageStreams();
    this.d.b(this, paramInt);
  }

  private void a(String paramString)
  {
    Object localObject;
    String str;
    final JSONObject localJSONObject;
    try
    {
      localObject = new JSONArray(paramString);
      if (((JSONArray)localObject).length() != 2)
      {
        a(null, paramString);
        return;
      }
      str = ((JSONArray)localObject).getString(0);
      localJSONObject = ((JSONArray)localObject).getJSONObject(1);
      if (str.equals("cm"))
      {
        if (!localJSONObject.getString("type").equals("ping"))
          return;
        long l1 = SystemClock.uptimeMillis();
        this.j = l1;
        Handler localHandler1 = this.e;
        Runnable localRunnable = this.m;
        boolean bool1 = localHandler1.post(localRunnable);
        return;
      }
    }
    catch (JSONException localJSONException)
    {
      a(null, paramString, localJSONException);
      return;
    }
    synchronized (this.a)
    {
      localObject = (MessageStream)this.a.get(str);
      if (localObject == null)
      {
        a(str, paramString);
        return;
      }
    }
    Handler localHandler2 = this.e;
    Runnable local5 = new Runnable()
    {
      public void run()
      {
        MessageStream localMessageStream = localMessageStream;
        JSONObject localJSONObject = localJSONObject;
        localMessageStream.onMessageReceived(localJSONObject);
      }
    };
    boolean bool2 = localHandler2.post(local5);
  }

  private void a(String paramString1, String paramString2)
  {
    Logger localLogger = this.f;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = paramString1;
    arrayOfObject[1] = paramString2;
    localLogger.d("Received unsupported text message with namespace %s: %s", arrayOfObject);
  }

  private void a(String paramString1, String paramString2, Throwable paramThrowable)
  {
    Logger localLogger = this.f;
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = paramString1;
    arrayOfObject[1] = paramString2;
    localLogger.d(paramThrowable, "Received unsupported text message with namespace %s: %s", arrayOfObject);
  }

  private void a(String paramString, JSONObject paramJSONObject)
    throws IOException, IllegalStateException
  {
    if (this.b == null)
      throw new IllegalStateException("Channel is not currently connected");
    Object localObject = null;
    try
    {
      JSONArray localJSONArray1 = new JSONArray();
      JSONArray localJSONArray2 = localJSONArray1.put(0, paramString);
      JSONArray localJSONArray3 = localJSONArray1.put(1, paramJSONObject);
      String str = localJSONArray1.toString();
      localObject = str;
      try
      {
        label56: this.b.a(localObject);
        return;
      }
      catch (IllegalStateException localIllegalStateException)
      {
        throw new IOException("Channel has become disconnected");
      }
    }
    catch (JSONException localJSONException)
    {
      break label56;
    }
  }

  private void a(String paramString, byte[] paramArrayOfByte)
  {
    Logger localLogger = this.f;
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramString;
    localLogger.d("Received unsupported binary message with namespace: %s", arrayOfObject);
  }

  private int b(int paramInt)
  {
    int n;
    switch (paramInt)
    {
    case -1:
    default:
      n = -1;
    case -2:
    case 0:
    case -3:
    }
    while (true)
    {
      return n;
      n = 0;
      continue;
      n = -1;
    }
  }

  private void b()
  {
    if (this.k == 0L)
      return;
    Handler localHandler1 = this.e;
    Runnable localRunnable1 = this.l;
    localHandler1.removeCallbacks(localRunnable1);
    Handler localHandler2 = this.e;
    Runnable localRunnable2 = this.m;
    localHandler2.removeCallbacks(localRunnable2);
    this.j = 0L;
  }

  private void c()
  {
    long l1 = SystemClock.uptimeMillis();
    long l2 = this.j;
    long l3 = l1 - l2;
    long l4 = this.k;
    if (l3 >= l4)
    {
      Logger localLogger = this.f;
      Object[] arrayOfObject = new Object[0];
      localLogger.d("timeout waiting for ping; force-closing channel", arrayOfObject);
      this.b.a(-1);
      return;
    }
    Handler localHandler = this.e;
    Runnable localRunnable = this.l;
    boolean bool = localHandler.postDelayed(localRunnable, 1000L);
  }

  void a()
  {
    Logger localLogger1 = this.f;
    Object[] arrayOfObject1 = new Object[0];
    localLogger1.d("disconnect()", arrayOfObject1);
    b();
    detachAllMessageStreams();
    if (this.b == null)
      return;
    try
    {
      this.b.a();
      this.b = null;
      return;
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        Logger localLogger2 = this.f;
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = localIOException;
        localLogger2.e("Error while disconnecting socket", arrayOfObject2);
      }
    }
  }

  void a(Uri paramUri)
    throws IOException
  {
    this.b.a(paramUri);
  }

  public void attachMessageStream(MessageStream paramMessageStream)
  {
    p.a();
    String str1 = paramMessageStream.getNamespace();
    synchronized (this.a)
    {
      if (this.a.containsKey(str1))
      {
        String str2 = "MessageStream with namespace " + str1 + " already registered";
        throw new IllegalArgumentException(str2);
      }
    }
    Object localObject2 = this.a.put(str1, paramMessageStream);
    i locali = this.g;
    paramMessageStream.a(locali);
    String str3 = paramMessageStream.getNamespace();
    if ("ramp".equals(str3))
    {
      Context localContext = this.i.getApplicationContext();
      CastDevice localCastDevice = this.h;
      d.a(localContext, localCastDevice);
    }
    paramMessageStream.onAttached();
  }

  public void detachAllMessageStreams()
  {
    p.a();
    synchronized (this.a)
    {
      Collection localCollection = this.a.values();
      ArrayList localArrayList = new ArrayList(localCollection);
      this.a.clear();
      Iterator localIterator = localArrayList.iterator();
      if (!localIterator.hasNext())
        return;
      MessageStream localMessageStream = (MessageStream)localIterator.next();
      localMessageStream.a(null);
      localMessageStream.onDetached();
    }
  }

  static abstract interface a
  {
    public abstract void a(ApplicationChannel paramApplicationChannel);

    public abstract void a(ApplicationChannel paramApplicationChannel, int paramInt);

    public abstract void b(ApplicationChannel paramApplicationChannel, int paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.ApplicationChannel
 * JD-Core Version:    0.6.2
 */