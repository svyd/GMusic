package com.google.android.music.cloudclient;

import android.accounts.Account;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.text.TextUtils;
import com.google.android.common.http.GoogleHttpClient;
import com.google.android.gsf.Gservices;
import com.google.android.music.download.DownloadUtils;
import com.google.android.music.download.ServerRejectionException;
import com.google.android.music.download.ServerRejectionException.RejectionReason;
import com.google.android.music.download.ServiceUnavailableException;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.sync.google.MusicAuthInfo;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.IOUtils;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class MusicRequest
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.CLOUD_CLIENT);
  private static GoogleHttpClient mSharedClient;
  private final Account mAccount;
  private final String mAndroidId;
  private final Context mContext;
  private final boolean mLogHttp;
  private final MusicAuthInfo mMusicAuthInfo;
  protected final MusicPreferences mMusicPreferences;
  private final TreeSet<String> mPassthroughCookies;

  public MusicRequest(Context paramContext, MusicPreferences paramMusicPreferences)
  {
    this(paramContext, paramMusicPreferences, localAccount);
  }

  public MusicRequest(Context paramContext, MusicPreferences paramMusicPreferences, Account paramAccount)
  {
    boolean bool1 = DebugUtils.isLoggable(DebugUtils.MusicTag.HTTP);
    this.mLogHttp = bool1;
    TreeSet localTreeSet = new TreeSet();
    this.mPassthroughCookies = localTreeSet;
    this.mMusicPreferences = paramMusicPreferences;
    this.mContext = paramContext;
    String str1 = String.valueOf(Gservices.getLong(paramContext.getContentResolver(), "android_id", 0L));
    this.mAndroidId = str1;
    this.mAccount = paramAccount;
    MusicAuthInfo localMusicAuthInfo = new MusicAuthInfo(paramContext);
    this.mMusicAuthInfo = localMusicAuthInfo;
    String str2 = Gservices.getString(paramContext.getApplicationContext().getContentResolver(), "music_download_passthrough_cookies", "sjsc");
    if (str2 == null)
      return;
    String[] arrayOfString = str2.split(",");
    int i = arrayOfString.length;
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      String str3 = arrayOfString[j];
      if (str3.length() > 0)
        boolean bool2 = this.mPassthroughCookies.add(str3);
      j += 1;
    }
  }

  private static GoogleHttpClient createHttpClient(Context paramContext)
  {
    String str1 = DownloadUtils.getUserAgent(paramContext);
    GoogleHttpClient localGoogleHttpClient = new GoogleHttpClient(paramContext, str1, true);
    String str2 = DebugUtils.HTTP_TAG;
    if (DebugUtils.isAutoLogAll());
    for (int i = 3; ; i = 2)
    {
      localGoogleHttpClient.enableCurlLogging(str2, i);
      return localGoogleHttpClient;
    }
  }

  private final Account getAuthAccount()
  {
    return this.mAccount;
  }

  /** @deprecated */
  public static GoogleHttpClient getSharedHttpClient(Context paramContext)
  {
    try
    {
      if (mSharedClient == null)
        mSharedClient = createHttpClient(paramContext);
      GoogleHttpClient localGoogleHttpClient = mSharedClient;
      return localGoogleHttpClient;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static InputStream logInputStreamContents(InputStream paramInputStream)
    throws IOException
  {
    if (paramInputStream == null)
      return paramInputStream;
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream, 16384);
    localBufferedInputStream.mark(16384);
    int i = 16384;
    int j = 0;
    byte[] arrayOfByte = new byte[i];
    while (true)
    {
      int k;
      if (i > 0)
      {
        k = localBufferedInputStream.read(arrayOfByte, j, i);
        if (k > 0);
      }
      else
      {
        String str1 = DebugUtils.HTTP_TAG;
        String str2 = new String(arrayOfByte, 0, j, "UTF-8");
        Log.d(str1, str2);
        localBufferedInputStream.reset();
        paramInputStream = localBufferedInputStream;
        break;
      }
      i -= k;
      j += k;
    }
  }

  private void prepareAuthorizedRequest(HttpRequestBase paramHttpRequestBase, String paramString)
    throws IOException
  {
    prepareRequest(paramHttpRequestBase);
    updateAuthorization(paramHttpRequestBase, paramString);
  }

  private HttpGet prepareRedirectRequestAndUpdateCookies(HttpResponse paramHttpResponse, List<String> paramList)
    throws IOException
  {
    Header localHeader = paramHttpResponse.getFirstHeader("Location");
    if ((localHeader == null) || (TextUtils.isEmpty(localHeader.getValue())))
    {
      Log.e("MusicStreaming", "Redirect requested but no Location specified.");
      throw new IOException("Redirect requested but no Location specified.");
    }
    if (Log.isLoggable("MusicStreaming", 3))
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Following redirect to ");
      String str1 = localHeader.getValue();
      String str2 = str1;
      Log.d("MusicStreaming", str2);
    }
    String str3 = localHeader.getValue();
    HttpGet localHttpGet = new HttpGet(str3);
    prepareUnauthorizedRequest(localHttpGet);
    Header[] arrayOfHeader = paramHttpResponse.getHeaders("Set-Cookie");
    int i = arrayOfHeader.length;
    int j = 0;
    String str4;
    if (j < i)
    {
      str4 = arrayOfHeader[j].getValue();
      if ((str4 != null) && (str4.length() != 0));
    }
    while (true)
    {
      j += 1;
      break;
      int k = str4.indexOf('=');
      int m = str4.indexOf(';');
      if ((k == -1) || (m == -1))
      {
        String str5 = "Invalid cookie format: " + str4;
        Log.w("MusicStreaming", str5);
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext())
        {
          String str6 = (String)localIterator.next();
          localHttpGet.addHeader("Cookie", str6);
        }
      }
      String str7 = str4.substring(0, k);
      if (this.mPassthroughCookies.contains(str7))
      {
        int n = m + 1;
        String str8 = str4.substring(0, n);
        boolean bool = paramList.add(str8);
      }
    }
    return localHttpGet;
  }

  private void prepareRequest(HttpRequestBase paramHttpRequestBase)
  {
    HttpParams localHttpParams = paramHttpRequestBase.getParams();
    int i = Gservices.getInt(this.mContext.getContentResolver(), "music_http_connection_timeout_ms", 45000);
    int j = Gservices.getInt(this.mContext.getContentResolver(), "music_http_socket_timeout_ms", 30000);
    HttpConnectionParams.setConnectionTimeout(localHttpParams, i);
    HttpConnectionParams.setSoTimeout(localHttpParams, j);
    paramHttpRequestBase.setParams(localHttpParams);
    String str1 = this.mAndroidId;
    paramHttpRequestBase.setHeader("X-Device-ID", str1);
    String str2 = this.mMusicPreferences.getLoggingId();
    paramHttpRequestBase.setHeader("X-Device-Logging-ID", str2);
  }

  private void prepareUnauthorizedRequest(HttpRequestBase paramHttpRequestBase)
    throws IOException
  {
    prepareRequest(paramHttpRequestBase);
    paramHttpRequestBase.removeHeaders("Authorization");
  }

  public static byte[] readAndReleaseShortResponse(HttpRequestBase paramHttpRequestBase, HttpResponse paramHttpResponse, int paramInt)
    throws IOException
  {
    HttpEntity localHttpEntity = paramHttpResponse.getEntity();
    try
    {
      InputStream localInputStream = localHttpEntity.getContent();
      if (DebugUtils.isLoggable(DebugUtils.MusicTag.HTTP))
        localInputStream = logInputStreamContents(localInputStream);
      byte[] arrayOfByte1 = IOUtils.readSmallStream(localInputStream, paramInt);
      byte[] arrayOfByte2 = arrayOfByte1;
      return arrayOfByte2;
    }
    finally
    {
      releaseResponse(paramHttpRequestBase, paramHttpResponse);
    }
  }

  public static void releaseResponse(HttpUriRequest paramHttpUriRequest, HttpResponse paramHttpResponse)
  {
    if (paramHttpResponse != null)
    {
      HttpEntity localHttpEntity = paramHttpResponse.getEntity();
      if (localHttpEntity == null)
        return;
      try
      {
        localHttpEntity.consumeContent();
        return;
      }
      catch (IOException localIOException)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Error consuming entity. ");
        String str1 = localIOException.getMessage();
        String str2 = str1;
        Log.w("MusicStreaming", str2);
        return;
      }
    }
    if (paramHttpUriRequest == null)
      return;
    paramHttpUriRequest.abort();
  }

  private static void updateAuthorization(HttpRequestBase paramHttpRequestBase, String paramString)
    throws IOException
  {
    if (TextUtils.isEmpty(paramString))
      throw new IOException("No auth token available.");
    String str = "GoogleLogin auth=" + paramString;
    paramHttpRequestBase.setHeader("Authorization", str);
  }

  protected String getAuthToken()
    throws InterruptedException, HttpResponseException
  {
    try
    {
      MusicAuthInfo localMusicAuthInfo = this.mMusicAuthInfo;
      Account localAccount = getAuthAccount();
      String str1 = localMusicAuthInfo.getAuthToken(localAccount);
      return str1;
    }
    catch (AuthenticatorException localAuthenticatorException)
    {
      if ((localAuthenticatorException.getCause() instanceof OperationCanceledException))
      {
        Log.w("MusicStreaming", "Getting auth token canceled");
        InterruptedException localInterruptedException = new InterruptedException();
        Throwable localThrowable = localInterruptedException.initCause(localAuthenticatorException);
        throw localInterruptedException;
      }
      Log.w("MusicStreaming", "Failed to get auth token", localAuthenticatorException);
      StringBuilder localStringBuilder = new StringBuilder().append("Unable to obtain auth token for music streaming: ");
      String str2 = localAuthenticatorException.getMessage();
      String str3 = str2;
      throw new HttpResponseException(401, str3);
    }
  }

  protected void onBeforeExecute(HttpRequestBase paramHttpRequestBase)
  {
  }

  public HttpResponse sendRequest(HttpRequestBase paramHttpRequestBase, GoogleHttpClient paramGoogleHttpClient)
    throws InterruptedException, IOException
  {
    String str1 = getAuthToken();
    Account localAccount = getAuthAccount();
    int i = 0;
    int j = 1;
    LinkedList localLinkedList = new LinkedList();
    Thread localThread = Thread.currentThread();
    MusicRequest localMusicRequest1 = this;
    HttpRequestBase localHttpRequestBase1 = paramHttpRequestBase;
    localMusicRequest1.prepareAuthorizedRequest(localHttpRequestBase1, str1);
    onBeforeExecute(paramHttpRequestBase);
    int k = 0;
    while (k == 0)
    {
      if (localThread.isInterrupted())
        throw new InterruptedException();
      if (LOGV)
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("Requesting URL: ");
        URI localURI = paramHttpRequestBase.getURI();
        String str2 = localURI;
        Log.i("MusicStreaming", str2);
      }
      int m = 0;
      Object localObject1 = null;
      HttpResponse localHttpResponse1;
      StatusLine localStatusLine;
      try
      {
        GoogleHttpClient localGoogleHttpClient = paramGoogleHttpClient;
        HttpRequestBase localHttpRequestBase2 = paramHttpRequestBase;
        localHttpResponse1 = localGoogleHttpClient.execute(localHttpRequestBase2);
        localStatusLine = localHttpResponse1.getStatusLine();
        if (localStatusLine == null)
        {
          Log.w("MusicStreaming", "Stream-download response status line is null.");
          throw new IOException("StatusLine is null -- should not happen.");
        }
      }
      finally
      {
        if (m == 0)
        {
          HttpRequestBase localHttpRequestBase3 = paramHttpRequestBase;
          Object localObject3 = localObject1;
          releaseResponse(localHttpRequestBase3, localObject3);
        }
      }
      int n = localStatusLine.getStatusCode();
      if (this.mLogHttp)
      {
        String str3 = DebugUtils.HTTP_TAG;
        StringBuilder localStringBuilder2 = new StringBuilder().append("Respone status=");
        int i1 = n;
        String str4 = i1;
        Log.d(str3, str4);
        Header[] arrayOfHeader = localHttpResponse1.getAllHeaders();
        int i2 = arrayOfHeader.length;
        int i3 = 0;
        if (i3 < i2)
        {
          Header localHeader1 = arrayOfHeader[i3];
          if (localHeader1.getName().compareToIgnoreCase("Set-Cookie") == 0);
          while (true)
          {
            i3 += 1;
            break;
            String str5 = DebugUtils.HTTP_TAG;
            StringBuilder localStringBuilder3 = new StringBuilder().append("Response header: ");
            String str6 = localHeader1.getName();
            StringBuilder localStringBuilder4 = localStringBuilder3.append(str6).append(": ");
            String str7 = localHeader1.getValue();
            String str8 = str7;
            Log.d(str5, str8);
          }
        }
      }
      int i4 = n;
      int i5 = 200;
      if (i4 >= i5)
      {
        int i6 = n;
        int i7 = 300;
        if (i6 < i7)
        {
          HttpEntity localHttpEntity = localHttpResponse1.getEntity();
          if (localHttpEntity != null)
          {
            if (1 == 0)
            {
              HttpRequestBase localHttpRequestBase4 = paramHttpRequestBase;
              HttpResponse localHttpResponse2 = localHttpResponse1;
              releaseResponse(localHttpRequestBase4, localHttpResponse2);
            }
            return localHttpResponse1;
          }
        }
      }
      if (m == 0)
      {
        HttpRequestBase localHttpRequestBase5 = paramHttpRequestBase;
        HttpResponse localHttpResponse3 = localHttpResponse1;
        releaseResponse(localHttpRequestBase5, localHttpResponse3);
      }
      int i8 = n;
      int i9 = 302;
      if (i8 == i9)
      {
        int i10 = 10;
        if (i >= i10)
          throw new IOException("Music request failed due to too many redirects.");
        i += 1;
        MusicRequest localMusicRequest2 = this;
        HttpResponse localHttpResponse4 = localHttpResponse1;
        paramHttpRequestBase = localMusicRequest2.prepareRedirectRequestAndUpdateCookies(localHttpResponse4, localLinkedList);
        k = 0;
      }
      else
      {
        int i11 = n;
        int i12 = 401;
        if (i11 == i12)
        {
          String str9 = "Received 401 Unauthorized from server.";
          if (LOGV)
            Log.v("MusicStreaming", str9);
          if (localAccount != null)
          {
            this.mMusicAuthInfo.invalidateAuthToken(localAccount, str1);
            if ((j != 0) && (i == 0))
            {
              str1 = getAuthToken();
              updateAuthorization(paramHttpRequestBase, str1);
              j = 0;
              if (LOGV)
                Log.v("MusicStreaming", "Will retry with updated token");
              k = 0;
            }
          }
          else
          {
            HttpResponseException localHttpResponseException1 = new org/apache/http/client/HttpResponseException;
            HttpResponseException localHttpResponseException2 = localHttpResponseException1;
            int i13 = n;
            localHttpResponseException2.<init>(i13, str9);
            throw localHttpResponseException1;
          }
        }
        else
        {
          int i14 = n;
          int i15 = 403;
          if (i14 == i15)
          {
            HttpResponse localHttpResponse5 = localHttpResponse1;
            String str10 = "X-Rejected-Reason";
            Header localHeader2 = localHttpResponse5.getLastHeader(str10);
            if (localHeader2 != null)
            {
              String str11 = localHeader2.getValue();
              if (!TextUtils.isEmpty(str11))
              {
                ServerRejectionException.RejectionReason localRejectionReason1 = null;
                String str12 = "DEVICE_NOT_AUTHORIZED";
                String str13 = str11;
                if (str12.equalsIgnoreCase(str13))
                  localRejectionReason1 = ServerRejectionException.RejectionReason.DEVICE_NOT_AUTHORIZED;
                while (localRejectionReason1 != null)
                {
                  ServerRejectionException localServerRejectionException1 = new com/google/android/music/download/ServerRejectionException;
                  ServerRejectionException localServerRejectionException2 = localServerRejectionException1;
                  ServerRejectionException.RejectionReason localRejectionReason2 = localRejectionReason1;
                  localServerRejectionException2.<init>(localRejectionReason2);
                  throw localServerRejectionException1;
                  String str14 = "ANOTHER_STREAM_BEING_PLAYED";
                  String str15 = str11;
                  if (str14.equalsIgnoreCase(str15))
                  {
                    localRejectionReason1 = ServerRejectionException.RejectionReason.ANOTHER_STREAM_BEING_PLAYED;
                  }
                  else
                  {
                    String str16 = "STREAM_RATE_LIMIT_REACHED";
                    String str17 = str11;
                    if (str16.equalsIgnoreCase(str17))
                      localRejectionReason1 = ServerRejectionException.RejectionReason.STREAM_RATE_LIMIT_REACHED;
                  }
                }
                StringBuilder localStringBuilder5 = new StringBuilder().append("Server returned an unknown rejection reason: ");
                String str18 = str11;
                String str19 = str18;
                Log.w("MusicStreaming", str19);
              }
            }
            HttpResponseException localHttpResponseException3 = new org/apache/http/client/HttpResponseException;
            HttpResponseException localHttpResponseException4 = localHttpResponseException3;
            int i16 = n;
            String str20 = "Unable to stream due to 403 error";
            localHttpResponseException4.<init>(i16, str20);
            throw localHttpResponseException3;
          }
          int i17 = n;
          int i18 = 404;
          if (i17 == i18)
          {
            String str21 = "Music request failed due to 404 (file not found) error";
            if (LOGV)
              Log.v("MusicStreaming", str21);
            HttpResponseException localHttpResponseException5 = new org/apache/http/client/HttpResponseException;
            HttpResponseException localHttpResponseException6 = localHttpResponseException5;
            int i19 = n;
            localHttpResponseException6.<init>(i19, str21);
            throw localHttpResponseException5;
          }
          int i20 = n;
          int i21 = 503;
          if (i20 == i21)
          {
            HttpResponse localHttpResponse6 = localHttpResponse1;
            String str22 = "Retry-After";
            if (localHttpResponse6.getFirstHeader(str22) != null)
              try
              {
                HttpResponse localHttpResponse7 = localHttpResponse1;
                String str23 = "Retry-After";
                long l1 = Long.valueOf(localHttpResponse7.getFirstHeader(str23).getValue()).longValue();
                StringBuilder localStringBuilder6 = new StringBuilder().append("Server said to retry after ");
                long l2 = l1;
                String str24 = l2 + " seconds";
                Log.w("MusicStreaming", str24);
                ServiceUnavailableException localServiceUnavailableException1 = new com/google/android/music/download/ServiceUnavailableException;
                StringBuilder localStringBuilder7 = new StringBuilder().append("Music request failed due to 503 (Service Unavailable) error.  Unavailable for ");
                long l3 = l1;
                String str25 = l3 + " seconds.";
                ServiceUnavailableException localServiceUnavailableException2 = localServiceUnavailableException1;
                long l4 = l1;
                String str26 = str25;
                localServiceUnavailableException2.<init>(l4, str26);
                throw localServiceUnavailableException1;
              }
              catch (NumberFormatException localNumberFormatException)
              {
                HttpResponseException localHttpResponseException7 = new org/apache/http/client/HttpResponseException;
                HttpResponseException localHttpResponseException8 = localHttpResponseException7;
                int i22 = n;
                String str27 = "Music request failed due to 503 error.";
                localHttpResponseException8.<init>(i22, str27);
                throw localHttpResponseException7;
              }
            Log.w("MusicStreaming", "Received 503 with no Retry-After header");
            HttpResponseException localHttpResponseException9 = new org/apache/http/client/HttpResponseException;
            HttpResponseException localHttpResponseException10 = localHttpResponseException9;
            int i23 = n;
            String str28 = "Music request failed due to 503 with no Retry-After header.";
            localHttpResponseException10.<init>(i23, str28);
            throw localHttpResponseException9;
          }
          if (LOGV)
          {
            StringBuilder localStringBuilder8 = new StringBuilder().append("Music request failed due to HTTP error ");
            int i24 = n;
            String str29 = i24;
            Log.e("MusicStreaming", str29);
          }
          HttpResponseException localHttpResponseException11 = new org/apache/http/client/HttpResponseException;
          StringBuilder localStringBuilder9 = new StringBuilder().append("Music request failed due to HTTP error ");
          int i25 = n;
          String str30 = i25;
          HttpResponseException localHttpResponseException12 = localHttpResponseException11;
          int i26 = n;
          String str31 = str30;
          localHttpResponseException12.<init>(i26, str31);
          throw localHttpResponseException11;
        }
      }
    }
    throw new IOException("Unable to retreive stream");
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.MusicRequest
 * JD-Core Version:    0.6.2
 */