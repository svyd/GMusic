package com.google.cast;

import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

final class l
  implements SimpleHttpRequest
{
  public static final int a = (int)TimeUnit.SECONDS.toMillis(3L);
  public static final int b = (int)TimeUnit.SECONDS.toMillis(10L);
  private CastContext c;
  private int d;
  private int e;
  private Uri f;
  private Uri g;
  private int h;
  private HttpRequestBase i;
  private HashMap<String, String> j;
  private MimeData k;
  private Header[] l;
  private Logger m;

  public l(CastContext paramCastContext, int paramInt1, int paramInt2)
  {
    this.c = paramCastContext;
    this.d = paramInt1;
    this.e = paramInt2;
    HashMap localHashMap = new HashMap();
    this.j = localHashMap;
    Logger localLogger = new Logger("SimpleHttpRequestImpl");
    this.m = localLogger;
  }

  private void a(HttpRequestBase paramHttpRequestBase)
    throws IOException, TimeoutException
  {
    Object localObject1 = null;
    this.i = paramHttpRequestBase;
    Iterator localIterator = this.j.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      HttpRequestBase localHttpRequestBase1 = this.i;
      String str1 = (String)localEntry.getKey();
      String str2 = (String)localEntry.getValue();
      localHttpRequestBase1.addHeader(str1, str2);
    }
    BasicHttpContext localBasicHttpContext = new BasicHttpContext();
    AndroidHttpClient localAndroidHttpClient = this.c.a();
    HttpParams localHttpParams1 = localAndroidHttpClient.getParams();
    int i1 = this.d;
    HttpConnectionParams.setConnectionTimeout(localHttpParams1, i1);
    HttpParams localHttpParams2 = localAndroidHttpClient.getParams();
    int i2 = this.e;
    HttpConnectionParams.setSoTimeout(localHttpParams2, i2);
    while (true)
    {
      try
      {
        Logger localLogger1 = this.m;
        Object[] arrayOfObject1 = new Object[2];
        String str3 = this.i.getMethod();
        arrayOfObject1[0] = str3;
        URI localURI1 = this.i.getURI();
        arrayOfObject1[1] = localURI1;
        localLogger1.d("executing http request: %s %s", arrayOfObject1);
        int i3 = 0;
        localObject5 = null;
        if (i3 >= 5)
          break label741;
        HttpRequestBase localHttpRequestBase2 = this.i;
        localObject5 = localAndroidHttpClient.execute(localHttpRequestBase2, localBasicHttpContext);
        int n = ((HttpResponse)localObject5).getStatusLine().getStatusCode();
        if ((n != 302) && (n != 301))
        {
          localObject2 = localObject5;
          localObject5 = (HttpUriRequest)localBasicHttpContext.getAttribute("http.request");
          localObject3 = (HttpHost)localBasicHttpContext.getAttribute("http.target_host");
          if (((HttpUriRequest)localObject5).getURI().isAbsolute())
          {
            localObject5 = localObject5.toString();
            Uri localUri1 = Uri.parse((String)localObject5);
            this.g = localUri1;
            Logger localLogger2 = this.m;
            Object[] arrayOfObject2 = new Object[1];
            Uri localUri2 = this.g;
            arrayOfObject2[0] = localUri2;
            localLogger2.d("final URI: %s", arrayOfObject2);
            Header[] arrayOfHeader = ((HttpResponse)localObject2).getAllHeaders();
            this.l = arrayOfHeader;
            StatusLine localStatusLine = ((HttpResponse)localObject2).getStatusLine();
            Logger localLogger3 = this.m;
            Object[] arrayOfObject3 = new Object[1];
            arrayOfObject3[0] = localStatusLine;
            localLogger3.d("status line: %s", arrayOfObject3);
            int i5 = localStatusLine.getStatusCode();
            this.h = i5;
            localObject2 = ((HttpResponse)localObject2).getEntity();
            if (localObject2 == null)
              break label735;
            localObject5 = ((HttpEntity)localObject2).getContentType();
            if (localObject5 == null)
              continue;
            localObject5 = ((Header)localObject5).getValue();
            if (!TextUtils.isEmpty((CharSequence)localObject5))
              break label728;
            localObject3 = "application/octet-stream";
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            ((HttpEntity)localObject2).writeTo(localByteArrayOutputStream);
            ((HttpEntity)localObject2).consumeContent();
            localByteArrayOutputStream.close();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            localObject5 = new MimeData(arrayOfByte, (String)localObject3);
            this.i = null;
            this.k = ((MimeData)localObject5);
          }
        }
        else
        {
          localObject2 = ((HttpResponse)localObject5).getHeaders("Location");
          if (localObject2 == null)
            break label741;
          int i6 = localObject2.length;
          if (i6 == 0)
            break label741;
        }
      }
      catch (ClientProtocolException localClientProtocolException)
      {
        int i4;
        try
        {
          Object localObject3;
          HttpRequestBase localHttpRequestBase3 = this.i;
          int i7 = localObject2.length + -1;
          String str4 = localObject2[i7].getValue();
          URI localURI2 = new URI(str4);
          localHttpRequestBase3.setURI(localURI2);
          localObject3 += 1;
          localObject2 = ((HttpResponse)localObject5).getEntity();
          if (localObject2 == null)
            continue;
          ((HttpEntity)localObject2).consumeContent();
          continue;
          localClientProtocolException = localClientProtocolException;
          throw new IOException("client protocol error");
        }
        catch (URISyntaxException localURISyntaxException)
        {
          Logger localLogger4 = this.m;
          Object[] arrayOfObject4 = new Object[0];
          localLogger4.w("Redirect failed. Unable to parse Location header into an URI", arrayOfObject4);
          localObject2 = localObject5;
        }
        continue;
        StringBuilder localStringBuilder1 = new StringBuilder();
        String str5 = i4.toURI();
        StringBuilder localStringBuilder2 = localStringBuilder1.append(str5);
        URI localURI3 = ((HttpUriRequest)localObject5).getURI();
        String str6 = localURI3;
        localObject5 = str6;
        continue;
        localObject5 = null;
        continue;
      }
      catch (SocketTimeoutException localSocketTimeoutException)
      {
        throw new TimeoutException();
      }
      catch (IllegalStateException localIllegalStateException)
      {
        throw new MalformedURLException();
      }
      label728: Object localObject4 = localObject5;
      continue;
      label735: Object localObject5 = null;
      continue;
      label741: Object localObject2 = localObject5;
    }
  }

  public void cancel()
  {
    HttpRequestBase localHttpRequestBase = this.i;
    if (localHttpRequestBase == null)
      return;
    try
    {
      Logger localLogger = this.m;
      Object[] arrayOfObject = new Object[0];
      localLogger.w("aborting the HTTP request", arrayOfObject);
      localHttpRequestBase.abort();
      return;
    }
    catch (UnsupportedOperationException localUnsupportedOperationException)
    {
    }
  }

  public Uri getFinalUri()
  {
    return this.g;
  }

  public MimeData getResponseData()
  {
    return this.k;
  }

  public String getResponseHeader(String paramString)
  {
    Header[] arrayOfHeader = this.l;
    int n = arrayOfHeader.length;
    int i1 = 0;
    Header localHeader;
    if (i1 < n)
    {
      localHeader = arrayOfHeader[i1];
      if (!localHeader.getName().equalsIgnoreCase(paramString));
    }
    for (String str = localHeader.getValue(); ; str = null)
    {
      return str;
      i1 += 1;
      break;
    }
  }

  public int getResponseStatus()
  {
    return this.h;
  }

  public void performDelete(Uri paramUri)
    throws IOException, TimeoutException
  {
    this.f = paramUri;
    String str = this.f.toString();
    HttpDelete localHttpDelete = new HttpDelete(str);
    a(localHttpDelete);
  }

  public void performGet(Uri paramUri)
    throws IOException, TimeoutException
  {
    this.f = paramUri;
    String str = this.f.toString();
    HttpGet localHttpGet = new HttpGet(str);
    a(localHttpGet);
  }

  public void performPost(Uri paramUri, MimeData paramMimeData)
    throws IOException, TimeoutException
  {
    this.f = paramUri;
    String str1 = this.f.toString();
    HttpPost localHttpPost = new HttpPost(str1);
    if (paramMimeData != null)
    {
      byte[] arrayOfByte = paramMimeData.getData();
      ByteArrayEntity localByteArrayEntity = new ByteArrayEntity(arrayOfByte);
      String str2 = paramMimeData.getType();
      localByteArrayEntity.setContentType(str2);
      localHttpPost.setEntity(localByteArrayEntity);
    }
    a(localHttpPost);
  }

  public void setRequestHeader(String paramString1, String paramString2)
  {
    Object localObject = this.j.put(paramString1, paramString2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.l
 * JD-Core Version:    0.6.2
 */