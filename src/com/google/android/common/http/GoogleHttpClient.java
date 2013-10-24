package com.google.android.common.http;

import android.content.ContentResolver;
import android.content.Context;
import android.net.TrafficStats;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.os.Process;
import android.os.SystemClock;
import android.util.EventLog;
import android.util.Log;
import com.google.android.gsf.Gservices;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.LayeredSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.client.EntityEnclosingRequestWrapper;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

public class GoogleHttpClient
  implements HttpClient
{
  private final String mAppName;
  private final AndroidHttpClient mClient;
  private final ThreadLocal<Boolean> mConnectionAllocated;
  private final ContentResolver mResolver;
  private final String mUserAgent;

  public GoogleHttpClient(Context paramContext, String paramString, boolean paramBoolean)
  {
    ThreadLocal localThreadLocal = new ThreadLocal();
    this.mConnectionAllocated = localThreadLocal;
    StringBuilder localStringBuilder1 = new StringBuilder().append(paramString).append(" (");
    String str1 = Build.DEVICE;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(" ");
    String str2 = Build.ID;
    String str3 = str2 + ")";
    if (paramBoolean)
      str3 = str3 + "; gzip";
    AndroidHttpClient localAndroidHttpClient = AndroidHttpClient.newInstance(str3, paramContext);
    this.mClient = localAndroidHttpClient;
    ContentResolver localContentResolver = paramContext.getContentResolver();
    this.mResolver = localContentResolver;
    this.mAppName = paramString;
    this.mUserAgent = str3;
    SchemeRegistry localSchemeRegistry = getConnectionManager().getSchemeRegistry();
    Iterator localIterator = localSchemeRegistry.getSchemeNames().iterator();
    if (!localIterator.hasNext())
      return;
    String str4 = (String)localIterator.next();
    Scheme localScheme1 = localSchemeRegistry.unregister(str4);
    SocketFactory localSocketFactory = localScheme1.getSocketFactory();
    LayeredSocketFactory localLayeredSocketFactory;
    if ((localSocketFactory instanceof LayeredSocketFactory))
      localLayeredSocketFactory = (LayeredSocketFactory)localSocketFactory;
    for (Object localObject = new WrappedLayeredSocketFactory(localLayeredSocketFactory, null); ; localObject = new WrappedSocketFactory(localSocketFactory, null))
    {
      int i = localScheme1.getDefaultPort();
      Scheme localScheme2 = new Scheme(str4, (SocketFactory)localObject, i);
      Scheme localScheme3 = localSchemeRegistry.register(localScheme2);
      break;
    }
  }

  private static RequestWrapper wrapRequest(HttpUriRequest paramHttpUriRequest)
    throws IOException
  {
    try
    {
      HttpEntityEnclosingRequest localHttpEntityEnclosingRequest;
      if ((paramHttpUriRequest instanceof HttpEntityEnclosingRequest))
        localHttpEntityEnclosingRequest = (HttpEntityEnclosingRequest)paramHttpUriRequest;
      for (Object localObject = new EntityEnclosingRequestWrapper(localHttpEntityEnclosingRequest); ; localObject = new RequestWrapper(paramHttpUriRequest))
      {
        ((RequestWrapper)localObject).resetHeaders();
        return localObject;
      }
    }
    catch (ProtocolException localProtocolException)
    {
      throw new ClientProtocolException(localProtocolException);
    }
  }

  public void close()
  {
    this.mClient.close();
  }

  public void enableCurlLogging(String paramString, int paramInt)
  {
    this.mClient.enableCurlLogging(paramString, paramInt);
  }

  public <T> T execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, ResponseHandler<? extends T> paramResponseHandler)
    throws IOException, ClientProtocolException
  {
    return this.mClient.execute(paramHttpHost, paramHttpRequest, paramResponseHandler);
  }

  public <T> T execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, ResponseHandler<? extends T> paramResponseHandler, HttpContext paramHttpContext)
    throws IOException, ClientProtocolException
  {
    return this.mClient.execute(paramHttpHost, paramHttpRequest, paramResponseHandler, paramHttpContext);
  }

  public <T> T execute(HttpUriRequest paramHttpUriRequest, ResponseHandler<? extends T> paramResponseHandler)
    throws IOException, ClientProtocolException
  {
    return this.mClient.execute(paramHttpUriRequest, paramResponseHandler);
  }

  public <T> T execute(HttpUriRequest paramHttpUriRequest, ResponseHandler<? extends T> paramResponseHandler, HttpContext paramHttpContext)
    throws IOException, ClientProtocolException
  {
    return this.mClient.execute(paramHttpUriRequest, paramResponseHandler, paramHttpContext);
  }

  public HttpResponse execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest)
    throws IOException
  {
    return this.mClient.execute(paramHttpHost, paramHttpRequest);
  }

  public HttpResponse execute(HttpHost paramHttpHost, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws IOException
  {
    return this.mClient.execute(paramHttpHost, paramHttpRequest, paramHttpContext);
  }

  public HttpResponse execute(HttpUriRequest paramHttpUriRequest)
    throws IOException
  {
    HttpContext localHttpContext = (HttpContext)null;
    return execute(paramHttpUriRequest, localHttpContext);
  }

  public HttpResponse execute(HttpUriRequest paramHttpUriRequest, HttpContext paramHttpContext)
    throws IOException
  {
    String str1 = paramHttpUriRequest.getURI().toString();
    UrlRules.Rule localRule = UrlRules.getRules(this.mResolver).matchRule(str1);
    String str2 = localRule.apply(str1);
    if (str2 == null)
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("Blocked by ");
      String str3 = localRule.mName;
      String str4 = str3 + ": " + str1;
      int i = Log.w("GoogleHttpClient", str4);
      throw new BlockedRequestException(localRule);
    }
    HttpResponse localHttpResponse;
    if (str2 == str1)
      localHttpResponse = executeWithoutRewriting(paramHttpUriRequest, paramHttpContext);
    while (true)
    {
      return localHttpResponse;
      try
      {
        URI localURI = new URI(str2);
        RequestWrapper localRequestWrapper1 = wrapRequest(paramHttpUriRequest);
        localRequestWrapper1.setURI(localURI);
        RequestWrapper localRequestWrapper2 = localRequestWrapper1;
        localHttpResponse = executeWithoutRewriting(localRequestWrapper2, paramHttpContext);
      }
      catch (URISyntaxException localURISyntaxException)
      {
        StringBuilder localStringBuilder2 = new StringBuilder().append("Bad URL from rule: ");
        String str5 = localRule.mName;
        String str6 = str5;
        throw new RuntimeException(str6, localURISyntaxException);
      }
    }
  }

  public HttpResponse executeWithoutRewriting(HttpUriRequest paramHttpUriRequest, HttpContext paramHttpContext)
    throws IOException
  {
    long l1 = SystemClock.elapsedRealtime();
    label150: long l6;
    try
    {
      ThreadLocal localThreadLocal = this.mConnectionAllocated;
      Object localObject1 = null;
      localThreadLocal.set(localObject1);
      ContentResolver localContentResolver = this.mResolver;
      String str1 = "http_stats";
      boolean bool = false;
      int i;
      long l2;
      long l3;
      Object localObject2;
      HttpEntity localHttpEntity;
      if (Gservices.getBoolean(localContentResolver, str1, bool))
      {
        i = Process.myUid();
        l2 = TrafficStats.getUidTxBytes(i);
        l3 = TrafficStats.getUidRxBytes(i);
        AndroidHttpClient localAndroidHttpClient1 = this.mClient;
        HttpUriRequest localHttpUriRequest1 = paramHttpUriRequest;
        HttpContext localHttpContext1 = paramHttpContext;
        localObject2 = localAndroidHttpClient1.execute(localHttpUriRequest1, localHttpContext1);
        if (localObject2 == null)
          localHttpEntity = null;
      }
      while (true)
      {
        if (localHttpEntity != null)
        {
          long l4 = SystemClock.elapsedRealtime();
          long l5 = l4 - l1;
          String str2 = this.mAppName;
          NetworkStatsEntity localNetworkStatsEntity = new NetworkStatsEntity(localHttpEntity, str2, i, l2, l3, l5, l4);
          ((HttpResponse)localObject2).setEntity(localNetworkStatsEntity);
        }
        int j = ((HttpResponse)localObject2).getStatusLine().getStatusCode();
        int k = j;
        try
        {
          l6 = SystemClock.elapsedRealtime() - l1;
          if ((this.mConnectionAllocated.get() == null) && (k >= 0));
          for (int m = 1; ; m = 0)
          {
            Object[] arrayOfObject1 = new Object[4];
            Long localLong1 = Long.valueOf(l6);
            arrayOfObject1[0] = localLong1;
            Integer localInteger1 = Integer.valueOf(k);
            arrayOfObject1[1] = localInteger1;
            String str3 = this.mAppName;
            arrayOfObject1[2] = str3;
            Integer localInteger2 = Integer.valueOf(m);
            arrayOfObject1[3] = localInteger2;
            Object[] arrayOfObject2 = arrayOfObject1;
            int n = EventLog.writeEvent(203002, arrayOfObject2);
            return localObject2;
            localHttpEntity = ((HttpResponse)localObject2).getEntity();
            break;
            AndroidHttpClient localAndroidHttpClient2 = this.mClient;
            HttpUriRequest localHttpUriRequest2 = paramHttpUriRequest;
            HttpContext localHttpContext2 = paramHttpContext;
            HttpResponse localHttpResponse = localAndroidHttpClient2.execute(localHttpUriRequest2, localHttpContext2);
            localObject2 = localHttpResponse;
            break label150;
          }
        }
        catch (Exception localException1)
        {
          while (true)
          {
            String str4 = "Error recording stats";
            int i1 = Log.e("GoogleHttpClient", str4, localException1);
          }
        }
      }
    }
    finally
    {
    }
    try
    {
      l6 = SystemClock.elapsedRealtime() - l1;
      if ((this.mConnectionAllocated.get() == null) && (-1 >= 0));
      for (int i2 = 1; ; i2 = 0)
      {
        Object[] arrayOfObject3 = new Object[4];
        Long localLong2 = Long.valueOf(l6);
        arrayOfObject3[0] = localLong2;
        Integer localInteger3 = Integer.valueOf(-1);
        arrayOfObject3[1] = localInteger3;
        String str5 = this.mAppName;
        arrayOfObject3[2] = str5;
        Integer localInteger4 = Integer.valueOf(i2);
        arrayOfObject3[3] = localInteger4;
        int i3 = EventLog.writeEvent(203002, arrayOfObject3);
        throw localObject3;
      }
    }
    catch (Exception localException2)
    {
      while (true)
      {
        String str6 = "GoogleHttpClient";
        String str7 = "Error recording stats";
        int i4 = Log.e(str6, str7, localException2);
      }
    }
  }

  public ClientConnectionManager getConnectionManager()
  {
    return this.mClient.getConnectionManager();
  }

  public HttpParams getParams()
  {
    return this.mClient.getParams();
  }

  public String rewriteURI(String paramString)
  {
    return UrlRules.getRules(this.mResolver).matchRule(paramString).apply(paramString);
  }

  private class WrappedLayeredSocketFactory extends GoogleHttpClient.WrappedSocketFactory
    implements LayeredSocketFactory
  {
    private LayeredSocketFactory mDelegate;

    private WrappedLayeredSocketFactory(LayeredSocketFactory arg2)
    {
      super(localSocketFactory, null);
      this.mDelegate = localSocketFactory;
    }

    public final Socket createSocket(Socket paramSocket, String paramString, int paramInt, boolean paramBoolean)
      throws IOException
    {
      return this.mDelegate.createSocket(paramSocket, paramString, paramInt, paramBoolean);
    }
  }

  private class WrappedSocketFactory
    implements SocketFactory
  {
    private SocketFactory mDelegate;

    private WrappedSocketFactory(SocketFactory arg2)
    {
      Object localObject;
      this.mDelegate = localObject;
    }

    public final Socket connectSocket(Socket paramSocket, String paramString, int paramInt1, InetAddress paramInetAddress, int paramInt2, HttpParams paramHttpParams)
      throws IOException
    {
      ThreadLocal localThreadLocal = GoogleHttpClient.this.mConnectionAllocated;
      Boolean localBoolean = Boolean.TRUE;
      localThreadLocal.set(localBoolean);
      SocketFactory localSocketFactory = this.mDelegate;
      Socket localSocket = paramSocket;
      String str = paramString;
      int i = paramInt1;
      InetAddress localInetAddress = paramInetAddress;
      int j = paramInt2;
      HttpParams localHttpParams = paramHttpParams;
      return localSocketFactory.connectSocket(localSocket, str, i, localInetAddress, j, localHttpParams);
    }

    public final Socket createSocket()
      throws IOException
    {
      return this.mDelegate.createSocket();
    }

    public final boolean isSecure(Socket paramSocket)
    {
      return this.mDelegate.isSecure(paramSocket);
    }
  }

  public static class BlockedRequestException extends IOException
  {
    private final UrlRules.Rule mRule;

    BlockedRequestException(UrlRules.Rule paramRule)
    {
      super();
      this.mRule = paramRule;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.http.GoogleHttpClient
 * JD-Core Version:    0.6.2
 */