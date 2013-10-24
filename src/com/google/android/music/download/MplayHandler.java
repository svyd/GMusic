package com.google.android.music.download;

import android.accounts.Account;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.text.TextUtils;
import com.google.android.common.http.GoogleHttpClient;
import com.google.android.gsf.Gservices;
import com.google.android.music.cloudclient.MusicRequest;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.sharedpreview.PreviewResponse;
import com.google.android.music.sharedpreview.SharedPreviewClient;
import com.google.android.music.store.MusicRingtoneManager;
import com.google.android.music.store.Store;
import com.google.android.music.sync.common.ProviderException;
import com.google.android.music.sync.google.MusicAuthInfo;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Closeables;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class MplayHandler
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private final String mAcceptHeaderValue;
  private Account mAccount;
  private final String mAndroidId;
  private volatile String mContentType;
  private final Context mContext;
  private final DownloadRequest mDownloadRequest;
  private final DownloadState mDownloadState;
  private boolean mDownloadSucceeded;
  private final MusicEventLogger mEventLogger;
  private final GoogleHttpClient mHttpClient;
  private long mHttpContentLength;
  private InputStream mInputStream;
  private final boolean mLogHttp;
  private final MusicAuthInfo mMusicAuthInfo;
  private final MusicPreferences mMusicPreferences;
  private long mPartialLength;
  private TreeSet<String> mPassthroughCookies;
  private volatile HttpRequestBase mRequest;
  private volatile HttpResponse mResponse;
  private final TreeSet<String> mSupportedInternetMediaTypes;

  public MplayHandler(Context paramContext, MusicPreferences paramMusicPreferences, GoogleHttpClient paramGoogleHttpClient, DownloadRequest paramDownloadRequest, DownloadState paramDownloadState)
  {
    boolean bool1 = DebugUtils.isLoggable(DebugUtils.MusicTag.HTTP);
    this.mLogHttp = bool1;
    this.mDownloadSucceeded = false;
    this.mHttpContentLength = 65535L;
    this.mPartialLength = 65535L;
    Context localContext1 = paramContext;
    this.mContext = localContext1;
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(paramContext);
    this.mEventLogger = localMusicEventLogger;
    MusicPreferences localMusicPreferences = paramMusicPreferences;
    this.mMusicPreferences = localMusicPreferences;
    GoogleHttpClient localGoogleHttpClient = paramGoogleHttpClient;
    this.mHttpClient = localGoogleHttpClient;
    DownloadRequest localDownloadRequest = paramDownloadRequest;
    this.mDownloadRequest = localDownloadRequest;
    DownloadState localDownloadState = paramDownloadState;
    this.mDownloadState = localDownloadState;
    String str1 = String.valueOf(Gservices.getLong(paramContext.getContentResolver(), "android_id", 0L));
    this.mAndroidId = str1;
    Account localAccount = this.mMusicPreferences.getStreamingAccount();
    this.mAccount = localAccount;
    setSyncAccount();
    Context localContext2 = paramContext;
    MusicAuthInfo localMusicAuthInfo = new MusicAuthInfo(localContext2);
    this.mMusicAuthInfo = localMusicAuthInfo;
    String str2 = Gservices.getString(paramContext.getApplicationContext().getContentResolver(), "music_download_passthrough_cookies", "sjsc");
    TreeSet localTreeSet1 = new TreeSet();
    this.mPassthroughCookies = localTreeSet1;
    if (str2 != null)
    {
      String[] arrayOfString1 = str2.split(",");
      int i = arrayOfString1.length;
      int j = 0;
      while (j < i)
      {
        String str3 = arrayOfString1[j];
        if (str3.length() > 0)
          boolean bool2 = this.mPassthroughCookies.add(str3);
        j += 1;
      }
    }
    String str4 = Gservices.getString(paramContext.getApplicationContext().getContentResolver(), "music_supported_audio", "mpeg,mp3");
    StringBuilder localStringBuilder1 = new StringBuilder();
    TreeSet localTreeSet2 = new TreeSet();
    this.mSupportedInternetMediaTypes = localTreeSet2;
    String[] arrayOfString2 = str4.split(",");
    int k = 0;
    int m = arrayOfString2.length;
    if (k < m)
    {
      StringBuilder localStringBuilder2 = new StringBuilder().append("audio/");
      String str5 = arrayOfString2[k].trim();
      String str6 = str5;
      if (DownloadUtils.MimeToExtensionMap.keySet().contains(str6))
      {
        boolean bool3 = this.mSupportedInternetMediaTypes.add(str6);
        if (k != 0)
          StringBuilder localStringBuilder3 = localStringBuilder1.append(", ");
        StringBuilder localStringBuilder4 = localStringBuilder1.append(str6);
      }
      while (true)
      {
        k += 1;
        break;
        String str7 = "Can't enable unsupported audio type: " + str6;
        Log.w("MplayHandler", str7);
      }
    }
    String str8 = localStringBuilder1.toString();
    this.mAcceptHeaderValue = str8;
    if (!LOGV)
      return;
    StringBuilder localStringBuilder5 = new StringBuilder().append("Accept header value: ");
    String str9 = this.mAcceptHeaderValue;
    String str10 = str9;
    Log.i("MplayHandler", str10);
  }

  private String getAuthToken(Account paramAccount)
    throws InterruptedException, HttpResponseException
  {
    try
    {
      String str1 = this.mMusicAuthInfo.getAuthToken(paramAccount);
      return str1;
    }
    catch (AuthenticatorException localAuthenticatorException)
    {
      if ((localAuthenticatorException.getCause() instanceof OperationCanceledException))
      {
        Log.w("MplayHandler", "Getting auth token canceled");
        InterruptedException localInterruptedException = new InterruptedException();
        Throwable localThrowable = localInterruptedException.initCause(localAuthenticatorException);
        throw localInterruptedException;
      }
      Log.w("MplayHandler", "Failed to get auth token", localAuthenticatorException);
      StringBuilder localStringBuilder = new StringBuilder().append("Unable to obtain auth token for music streaming: ");
      String str2 = localAuthenticatorException.getMessage();
      String str3 = str2;
      throw new HttpResponseException(401, str3);
    }
  }

  private void logDownloadedSize(long paramLong)
  {
    MusicEventLogger localMusicEventLogger = this.mEventLogger;
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = "retmoteId";
    String str = this.mDownloadRequest.getRemoteId();
    arrayOfObject[1] = str;
    arrayOfObject[2] = "downloadedSize";
    Long localLong = Long.valueOf(paramLong);
    arrayOfObject[3] = localLong;
    localMusicEventLogger.trackEvent("downloadFinished", arrayOfObject);
  }

  private void logFinalStreamingUrl(String paramString)
  {
    MusicEventLogger localMusicEventLogger = this.mEventLogger;
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = "retmoteId";
    String str = this.mDownloadRequest.getRemoteId();
    arrayOfObject[1] = str;
    arrayOfObject[2] = "httpUrl";
    arrayOfObject[3] = paramString;
    localMusicEventLogger.trackEvent("downloadFinalStreamingHost", arrayOfObject);
  }

  private void logFirstBufferReceivedEvent()
  {
    NetworkInfo localNetworkInfo = DownloadUtils.getActiveNetworkInfo(this.mContext);
    int i;
    if (localNetworkInfo != null)
    {
      i = localNetworkInfo.getType();
      if (localNetworkInfo == null)
        break label250;
    }
    label250: for (int j = localNetworkInfo.getSubtype(); ; j = 0)
    {
      long l = this.mDownloadState.calculateLatency();
      MusicEventLogger localMusicEventLogger = this.mEventLogger;
      Object[] arrayOfObject = new Object[16];
      arrayOfObject[0] = "id";
      ContentIdentifier localContentIdentifier = this.mDownloadRequest.getId();
      arrayOfObject[1] = localContentIdentifier;
      arrayOfObject[2] = "retmoteId";
      String str = this.mDownloadRequest.getRemoteId();
      arrayOfObject[3] = str;
      arrayOfObject[4] = "downloadOwner";
      DownloadRequest.Owner localOwner = this.mDownloadRequest.getOwner();
      arrayOfObject[5] = localOwner;
      arrayOfObject[6] = "downloadPriority";
      Integer localInteger1 = Integer.valueOf(this.mDownloadRequest.getPriority());
      arrayOfObject[7] = localInteger1;
      arrayOfObject[8] = "downloadSeekMs";
      Long localLong1 = Long.valueOf(this.mDownloadRequest.getSeekMs());
      arrayOfObject[9] = localLong1;
      arrayOfObject[10] = "latency";
      Long localLong2 = Long.valueOf(l);
      arrayOfObject[11] = localLong2;
      arrayOfObject[12] = "networkType";
      Integer localInteger2 = Integer.valueOf(i);
      arrayOfObject[13] = localInteger2;
      arrayOfObject[14] = "networkSubType";
      Integer localInteger3 = Integer.valueOf(j);
      arrayOfObject[15] = localInteger3;
      localMusicEventLogger.trackEvent("downloadFirstBufferReceived", arrayOfObject);
      return;
      i = 10000;
      break;
    }
  }

  private void logHttpRequest(String paramString1, String paramString2)
  {
    MusicEventLogger localMusicEventLogger = this.mEventLogger;
    Object[] arrayOfObject = new Object[6];
    arrayOfObject[0] = "retmoteId";
    String str = this.mDownloadRequest.getRemoteId();
    arrayOfObject[1] = str;
    arrayOfObject[2] = "httpUrl";
    arrayOfObject[3] = paramString1;
    arrayOfObject[4] = "httpRangeHeaderValue";
    arrayOfObject[5] = paramString2;
    localMusicEventLogger.trackEvent("downloadHttpRequest", arrayOfObject);
  }

  private void logHttpResponse(int paramInt)
  {
    MusicEventLogger localMusicEventLogger = this.mEventLogger;
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = "retmoteId";
    String str = this.mDownloadRequest.getRemoteId();
    arrayOfObject[1] = str;
    arrayOfObject[2] = "httpResponseCode";
    Integer localInteger = Integer.valueOf(paramInt);
    arrayOfObject[3] = localInteger;
    localMusicEventLogger.trackEvent("downloadHttpResponse", arrayOfObject);
  }

  private void notifySharedPreviewMetadataUpdate(PreviewResponse paramPreviewResponse)
  {
    Intent localIntent1 = new Intent("com.android.music.sharedpreviewmetadataupdate");
    String str = this.mDownloadRequest.getDomainParam();
    Intent localIntent2 = localIntent1.putExtra("sharedurl", str);
    int i = paramPreviewResponse.mPreviewDurationMillis;
    Intent localIntent3 = localIntent1.putExtra("duration", i);
    int j = PreviewResponse.convertPreviewType(paramPreviewResponse.mPlayType);
    Intent localIntent4 = localIntent1.putExtra("playtype", j);
    this.mContext.sendBroadcast(localIntent1);
  }

  private String parseContentType(HttpResponse paramHttpResponse)
  {
    Header[] arrayOfHeader = paramHttpResponse.getHeaders("Content-Type");
    String str = null;
    if ((arrayOfHeader != null) && (arrayOfHeader.length != 0))
      str = arrayOfHeader[0].getValue().split(";")[0].trim();
    return str;
  }

  public void cancel()
  {
    if (this.mRequest == null)
      return;
    this.mRequest.abort();
  }

  public boolean downloadSucceeded()
  {
    return this.mDownloadSucceeded;
  }

  public void downloadTo(OutputStream paramOutputStream)
    throws IOException, InterruptedException
  {
    if (this.mInputStream == null)
      throw new IOException("Missing input stream");
    int i = 0;
    long l1 = 0L;
    int j = 1024;
    long l2;
    boolean bool1;
    label237: long l5;
    do
    {
      int m;
      do
      {
        int k;
        InputStream localInputStream1;
        try
        {
          byte[] arrayOfByte = new byte[j];
          k = 0;
          l2 = 0L;
          m = this.mInputStream.read(k);
          if (m == -1)
            break;
          if (!Thread.interrupted())
            break label237;
          throw new InterruptedException();
        }
        catch (IOException localIOException)
        {
          if (LOGV)
          {
            StringBuilder localStringBuilder1 = new StringBuilder().append("MplayHandler.downloadTo: IOException: ");
            String str1 = localIOException.getMessage();
            String str2 = str1;
            Log.f("MplayHandler", str2, localIOException);
          }
          throw localIOException;
        }
        finally
        {
          localInputStream1 = this.mInputStream;
          if (i != 0)
            break label619;
        }
        bool1 = true;
        Closeables.close(localInputStream1, bool1);
        this.mInputStream = null;
        if (this.mLogHttp)
        {
          String str3 = DebugUtils.HTTP_TAG;
          StringBuilder localStringBuilder2 = new StringBuilder().append("Download finished: ");
          DownloadRequest localDownloadRequest1 = this.mDownloadRequest;
          StringBuilder localStringBuilder3 = localStringBuilder2.append(localDownloadRequest1).append(" state=");
          DownloadState localDownloadState1 = this.mDownloadState;
          String str4 = localDownloadState1;
          Log.d(str3, str4);
        }
        logDownloadedSize(l1);
        throw localObject;
        long l3 = m;
        l1 += l3;
        if (k == 0)
        {
          logFirstBufferReceivedEvent();
          k = 1;
        }
        paramOutputStream.write(k, 0, m);
      }
      while (this.mPartialLength == 65535L);
      long l4 = m;
      l2 += l4;
      l5 = this.mPartialLength;
    }
    while (l2 < l5);
    if (LOGV)
    {
      StringBuilder localStringBuilder4 = new StringBuilder().append("Reached partial length of ");
      long l6 = this.mPartialLength;
      String str5 = l6;
      Log.d("MplayHandler", str5);
    }
    i = 1;
    if (LOGV)
    {
      StringBuilder localStringBuilder5 = new StringBuilder().append("downloadTo: done ");
      DownloadRequest localDownloadRequest2 = this.mDownloadRequest;
      String str6 = localDownloadRequest2;
      Log.f("MplayHandler", str6);
    }
    InputStream localInputStream2 = this.mInputStream;
    boolean bool2;
    if (i == 0)
    {
      bool2 = true;
      label412: Closeables.close(localInputStream2, bool2);
      this.mInputStream = null;
      if (this.mLogHttp)
      {
        String str7 = DebugUtils.HTTP_TAG;
        StringBuilder localStringBuilder6 = new StringBuilder().append("Download finished: ");
        DownloadRequest localDownloadRequest3 = this.mDownloadRequest;
        StringBuilder localStringBuilder7 = localStringBuilder6.append(localDownloadRequest3).append(" state=");
        DownloadState localDownloadState2 = this.mDownloadState;
        String str8 = localDownloadState2;
        Log.d(str7, str8);
      }
      logDownloadedSize(l1);
      this.mDownloadSucceeded = true;
      if (this.mHttpContentLength != 65535L)
      {
        long l7 = this.mHttpContentLength;
        if (l1 != l7)
          break label625;
      }
    }
    label619: label625: for (boolean bool3 = true; ; bool3 = false)
    {
      this.mDownloadSucceeded = bool3;
      if (!this.mDownloadSucceeded)
      {
        Object[] arrayOfObject = new Object[2];
        Long localLong1 = Long.valueOf(this.mHttpContentLength);
        arrayOfObject[0] = localLong1;
        Long localLong2 = Long.valueOf(l1);
        arrayOfObject[1] = localLong2;
        String str9 = String.format("Failed to download complete content: mHttpContentLength=%s totalRead=%s", arrayOfObject);
        Log.w("MplayHandler", str9);
      }
      if (!LOGV)
        return;
      Log.f("MplayHandler", "Download finished gracefully");
      return;
      bool2 = false;
      break label412;
      bool1 = false;
      break;
    }
  }

  protected InputStream getDownloadStream()
    throws IOException, InterruptedException
  {
    long l1 = this.mDownloadRequest.getSeekMs();
    Account localAccount = this.mAccount;
    String str1 = getAuthToken(localAccount);
    String str2 = this.mAndroidId;
    String str3 = this.mMusicPreferences.getLoggingId();
    ContentIdentifier localContentIdentifier1 = this.mDownloadRequest.getId();
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str4 = this.mDownloadRequest.getRemoteId();
    String str5 = "https://android.clients.google.com/music/mplay?songid=" + str4;
    label214: NetworkInfo localNetworkInfo;
    label254: long l2;
    label295: label326: LinkedList localLinkedList;
    Object localObject1;
    int m;
    int n;
    String str8;
    if (localContentIdentifier1.isSharedDomain())
    {
      Context localContext1 = this.mContext;
      SharedPreviewClient localSharedPreviewClient = new SharedPreviewClient(localContext1);
      String str6 = this.mDownloadRequest.getDomainParam();
      PreviewResponse localPreviewResponse = localSharedPreviewClient.getPreviewResponse(str6);
      str5 = localPreviewResponse.mUrl;
      if (str5 == null)
        throw new IOException("Failed to retrieve streaming url");
      notifySharedPreviewMetadataUpdate(localPreviewResponse);
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str5);
      StringBuilder localStringBuilder3 = localStringBuilder1.append("&targetkbps=");
      int i = this.mDownloadState.getRecommendedBitrate();
      StringBuilder localStringBuilder4 = localStringBuilder3.append(i);
      if (!this.mMusicPreferences.isHighStreamQuality())
        break label549;
      StringBuilder localStringBuilder5 = localStringBuilder1.append("&opt=hi");
      localNetworkInfo = DownloadUtils.getActiveNetworkInfo(this.mContext);
      if (localNetworkInfo != null)
      {
        Context localContext2 = this.mContext;
        if (!DownloadUtils.isMobileOrMeteredNetworkType(localNetworkInfo, localContext2))
          break label595;
        StringBuilder localStringBuilder6 = localStringBuilder1.append("&net=mob");
      }
      DownloadRequest.Owner localOwner1 = this.mDownloadRequest.getOwner();
      DownloadRequest.Owner localOwner2 = DownloadRequest.Owner.MUSIC_PLAYBACK;
      if (localOwner1 != localOwner2)
        break label710;
      if (!this.mDownloadRequest.isExplicit())
        break label697;
      StringBuilder localStringBuilder7 = localStringBuilder1.append("&pt=e");
      int j = this.mDownloadRequest.getPriority();
      int k = DownloadRequest.PRIORITY_STREAM;
      if (j != k)
        StringBuilder localStringBuilder8 = localStringBuilder1.append("&dt=pc");
      l2 = this.mDownloadState.getCompletedBytes();
      long l3 = 65535L;
      this.mPartialLength = l3;
      DownloadRequest.Owner localOwner3 = this.mDownloadRequest.getOwner();
      DownloadRequest.Owner localOwner4 = DownloadRequest.Owner.RINGTONE;
      if (localOwner3 == localOwner4)
      {
        long l4 = MusicRingtoneManager.getRingtoneFileSize(this.mDownloadState.getRecommendedBitrate());
        this.mPartialLength = l4;
        long l5 = this.mPartialLength;
        long l6 = this.mDownloadState.getDownloadByteLength();
        if (l5 >= l6)
        {
          long l7 = 65535L;
          this.mPartialLength = l7;
        }
      }
      if (l1 != 0L)
        StringBuilder localStringBuilder9 = localStringBuilder1.append("&start=").append(l1);
      RequestSigningUtil.appendMplayUrlSignatureParams(str4, localStringBuilder1);
      String str7 = localStringBuilder1.toString();
      localLinkedList = new LinkedList();
      localObject1 = str1;
      m = 1;
      n = 0;
      str8 = str7;
    }
    while (true)
    {
      label549: label595: Object localObject2;
      label697: label710: label2012: Header localHeader2;
      if (n < 10)
      {
        this.mResponse = null;
        if (Thread.interrupted())
        {
          throw new InterruptedException();
          if ((!localContentIdentifier1.isNautilusDomain()) && (!str4.startsWith("T")))
            break;
          str5 = "https://android.clients.google.com/music/mplay?mjck=" + str4;
          break;
          if (this.mMusicPreferences.isNormalStreamQuality())
          {
            StringBuilder localStringBuilder10 = localStringBuilder1.append("&opt=med");
            break label214;
          }
          if (!this.mMusicPreferences.isLowStreamQuality())
            break label214;
          StringBuilder localStringBuilder11 = localStringBuilder1.append("&opt=low");
          break label214;
          Context localContext3 = this.mContext;
          if (DownloadUtils.isUnmeteredWifiNetworkType(localNetworkInfo, localContext3))
          {
            StringBuilder localStringBuilder12 = localStringBuilder1.append("&net=wifi");
            break label254;
          }
          Context localContext4 = this.mContext;
          if (DownloadUtils.isUnmeteredEthernetNetworkType(localNetworkInfo, localContext4))
          {
            StringBuilder localStringBuilder13 = localStringBuilder1.append("&net=ether");
            break label254;
          }
          StringBuilder localStringBuilder14 = new StringBuilder().append("Unsupported network type: ");
          int i1 = localNetworkInfo.getType();
          String str9 = i1;
          Log.w("MplayHandler", str9);
          break label254;
          StringBuilder localStringBuilder15 = localStringBuilder1.append("&pt=a");
          break label295;
          DownloadRequest.Owner localOwner5 = this.mDownloadRequest.getOwner();
          DownloadRequest.Owner localOwner6 = DownloadRequest.Owner.KEEPON;
          if (localOwner5 == localOwner6)
          {
            StringBuilder localStringBuilder16 = localStringBuilder1.append("&dt=uc");
            break label326;
          }
          DownloadRequest.Owner localOwner7 = this.mDownloadRequest.getOwner();
          DownloadRequest.Owner localOwner8 = DownloadRequest.Owner.RINGTONE;
          if (localOwner7 == localOwner8)
          {
            StringBuilder localStringBuilder17 = localStringBuilder1.append("&dt=rt");
            break label326;
          }
          DownloadRequest.Owner localOwner9 = this.mDownloadRequest.getOwner();
          DownloadRequest.Owner localOwner10 = DownloadRequest.Owner.AUTOCACHE;
          if (localOwner9 == localOwner10)
          {
            StringBuilder localStringBuilder18 = localStringBuilder1.append("&dt=sc");
            break label326;
          }
          StringBuilder localStringBuilder19 = new StringBuilder().append("Unexpected download request: ");
          DownloadRequest localDownloadRequest = this.mDownloadRequest;
          String str10 = localDownloadRequest;
          Log.wtf("MplayHandler", str10);
          break label326;
        }
        HttpGet localHttpGet = new HttpGet(str8);
        this.mRequest = localHttpGet;
        HttpParams localHttpParams = this.mRequest.getParams();
        HttpConnectionParams.setConnectionTimeout(localHttpParams, 12000);
        HttpConnectionParams.setSoTimeout(localHttpParams, 10000);
        this.mRequest.setParams(localHttpParams);
        if (TextUtils.isEmpty((CharSequence)localObject1))
          throw new IOException("No auth token available.");
        if (n == 0)
        {
          HttpRequestBase localHttpRequestBase1 = this.mRequest;
          String str11 = "GoogleLogin auth=" + (String)localObject1;
          localHttpRequestBase1.addHeader("Authorization", str11);
        }
        StatusLine localStatusLine;
        while (true)
        {
          this.mRequest.addHeader("X-Device-ID", str2);
          this.mRequest.addHeader("X-Device-Logging-ID", str3);
          HttpRequestBase localHttpRequestBase2 = this.mRequest;
          String str12 = this.mAcceptHeaderValue;
          localHttpRequestBase2.addHeader("Accept", str12);
          String str13 = null;
          if (l2 != 0L)
          {
            if (LOGV)
            {
              StringBuilder localStringBuilder20 = new StringBuilder().append("Setting range headers to start at byte ");
              long l8 = l2;
              String str14 = l8;
              Log.v("MplayHandler", str14);
            }
            StringBuilder localStringBuilder21 = new StringBuilder().append("bytes=");
            long l9 = l2;
            str13 = l9 + "-";
            this.mRequest.addHeader("Range", str13);
          }
          if (LOGV)
          {
            StringBuilder localStringBuilder22 = new StringBuilder().append("Requesting URL: ");
            URI localURI = this.mRequest.getURI();
            String str15 = localURI;
            Log.i("MplayHandler", str15);
          }
          if (str13 == null)
            str13 = "";
          logHttpRequest(str8, str13);
          GoogleHttpClient localGoogleHttpClient = this.mHttpClient;
          HttpRequestBase localHttpRequestBase3 = this.mRequest;
          HttpResponse localHttpResponse1 = localGoogleHttpClient.execute(localHttpRequestBase3);
          this.mResponse = localHttpResponse1;
          localStatusLine = this.mResponse.getStatusLine();
          if (localStatusLine != null)
            break;
          Log.w("MplayHandler", "Stream-download response status line is null.");
          throw new IOException("StatusLine is null -- should not happen.");
          if (localLinkedList.size() > 0)
          {
            Iterator localIterator = localLinkedList.iterator();
            while (localIterator.hasNext())
            {
              String str16 = (String)localIterator.next();
              this.mRequest.addHeader("Cookie", str16);
            }
          }
        }
        int i2 = localStatusLine.getStatusCode();
        logHttpResponse(i2);
        if (LOGV)
        {
          String str17 = "Response: status=" + i2;
          Log.i("MplayHandler", str17);
        }
        if (this.mLogHttp)
        {
          String str18 = DebugUtils.HTTP_TAG;
          String str19 = "status=" + i2;
          Log.d(str18, str19);
          Header[] arrayOfHeader1 = this.mResponse.getAllHeaders();
          int i4 = arrayOfHeader1.length;
          int i5 = 0;
          if (i5 < i4)
          {
            Header localHeader1 = arrayOfHeader1[i5];
            if (localHeader1.getName().compareToIgnoreCase("Set-Cookie") == 0);
            while (true)
            {
              i5 += 1;
              break;
              String str20 = DebugUtils.HTTP_TAG;
              StringBuilder localStringBuilder23 = new StringBuilder().append("Response header: ");
              String str21 = localHeader1.getName();
              StringBuilder localStringBuilder24 = localStringBuilder23.append(str21).append(": ");
              String str22 = localHeader1.getValue();
              StringBuilder localStringBuilder25 = localStringBuilder24;
              String str23 = str22;
              String str24 = str23;
              String str25 = str20;
              String str26 = str24;
              Log.d(str25, str26);
            }
          }
        }
        localObject2 = this.mResponse.getEntity();
        if ((i2 >= 200) && (i2 < 300) && (localObject2 != null))
        {
          logFinalStreamingUrl(str8);
          HttpResponse localHttpResponse2 = this.mResponse;
          String str27 = parseContentType(localHttpResponse2);
          this.mContentType = str27;
          if (this.mContentType != null)
          {
            TreeSet localTreeSet1 = this.mSupportedInternetMediaTypes;
            String str28 = this.mContentType;
            if (localTreeSet1.contains(str28));
          }
          else
          {
            String str29 = this.mContentType;
            throw new UnsupportedAudioTypeException(str29);
          }
          DownloadState localDownloadState1 = this.mDownloadState;
          String str30 = this.mContentType;
          localDownloadState1.setHttpContentType(str30);
          if (LOGV)
          {
            StringBuilder localStringBuilder26 = new StringBuilder().append("Received valid response for playback with content type: ");
            String str31 = this.mContentType;
            String str32 = str31;
            Log.i("MplayHandler", str32);
          }
          Object localObject3 = this.mResponse.getHeaders("Content-Length");
          String str33;
          if ((localObject3 != null) && (localObject3.length > 0))
            str33 = null;
          while (true)
          {
            if (str33 != null)
              localObject3 = localObject3[0].getValue();
            try
            {
              long l10 = Long.parseLong((String)localObject3);
              this.mHttpContentLength = l10;
              DownloadState localDownloadState2 = this.mDownloadState;
              long l11 = this.mHttpContentLength;
              localDownloadState2.adjustDownloadLengthUsingHttpContentLength(l11);
              Header[] arrayOfHeader2 = this.mResponse.getHeaders("X-Estimated-Content-Length");
              if ((arrayOfHeader2 != null) && (arrayOfHeader2.length > 0))
                str33 = arrayOfHeader2[0].getValue();
            }
            catch (NumberFormatException localNumberFormatException1)
            {
              while (true)
              {
                AbortRequestOnCloseInputStream localAbortRequestOnCloseInputStream;
                Object localObject4;
                try
                {
                  DownloadState localDownloadState3 = this.mDownloadState;
                  long l12 = Long.parseLong(str33);
                  localDownloadState3.setEstimatedDownloadByteLength(l12);
                  InputStream localInputStream = AndroidHttpClient.getUngzippedContent((HttpEntity)localObject2);
                  HttpRequestBase localHttpRequestBase4 = this.mRequest;
                  localAbortRequestOnCloseInputStream = new AbortRequestOnCloseInputStream(localInputStream, localHttpRequestBase4);
                  Header[] arrayOfHeader3 = this.mResponse.getHeaders("X-ID3-Footer-Attached");
                  if ((arrayOfHeader3 == null) || (arrayOfHeader3.length == 0))
                    break label2012;
                  DownloadState localDownloadState4 = this.mDownloadState;
                  localObject4 = new ID3v1FooterInputStream(localAbortRequestOnCloseInputStream, localDownloadState4);
                  return localObject4;
                  str33 = null;
                  break;
                  localNumberFormatException1 = localNumberFormatException1;
                  String str34 = "Server sent invalid content length: " + (String)localObject3;
                  Log.e("MplayHandler", str34, localNumberFormatException1);
                }
                catch (NumberFormatException localNumberFormatException2)
                {
                  String str35 = "Server sent invalid estimated content length: " + str33;
                  Log.e("MplayHandler", str35, localNumberFormatException2);
                  continue;
                }
                if (str33 == null)
                {
                  Log.w("MplayHandler", "No Content-Length or X-Estimated-Content-Length provided");
                  continue;
                  localObject4 = localAbortRequestOnCloseInputStream;
                }
              }
            }
          }
        }
        if (i2 != 302)
          break label2383;
        ((HttpEntity)localObject2).consumeContent();
        localHeader2 = this.mResponse.getFirstHeader("Location");
        if (localHeader2 != null)
          break label2088;
        if (Log.isLoggable("MplayHandler", 3))
          Log.d("MplayHandler", "Redirect requested but no Location specified.");
      }
      label2088: int i9;
      label2177: label2218: label2383: String str43;
      if (n >= 10)
      {
        throw new IOException("Unable to download stream due to too many redirects.");
        if (Log.isLoggable("MplayHandler", 3))
        {
          StringBuilder localStringBuilder27 = new StringBuilder().append("Following redirect to ");
          String str36 = localHeader2.getValue();
          String str37 = str36;
          Log.d("MplayHandler", str37);
        }
        str8 = localHeader2.getValue();
        n += 1;
        int i3 = this.mResponse.getHeaders("Set-Cookie");
        localObject2 = i3.length;
        i9 = 0;
        if (i9 < localObject2)
        {
          str4 = i3[i9].getValue();
          if ((str4 != null) && (str4.length() != 0))
            break label2218;
        }
        while (true)
        {
          i9 += 1;
          break label2177;
          break;
          int i10 = 61;
          int i6 = str4.indexOf(i10);
          int i11 = 59;
          int i12 = str4.indexOf(i11);
          int i13 = i6;
          int i7 = 65535;
          if (i13 != i7)
          {
            int i14 = i12;
            int i8 = 65535;
            if (i14 != i8);
          }
          else
          {
            String str38 = "Invalid cookie format: " + str4;
            Log.w("MplayHandler", str38);
            break;
          }
          int i15 = 0;
          int i16 = i6;
          String str39 = str4.substring(i15, i16);
          TreeSet localTreeSet2 = this.mPassthroughCookies;
          String str40 = str39;
          if (localTreeSet2.contains(str40))
          {
            int i17 = i12 + 1;
            int i18 = 0;
            int i19 = i17;
            String str41 = str4.substring(i18, i19);
            boolean bool = localLinkedList.add(str41);
          }
        }
        if (i3 == 401)
        {
          String str42 = "Received 401 Unauthorized from server.";
          if (LOGV)
            Log.v("MplayHandler", str42);
          if (localAccount != null)
          {
            this.mMusicAuthInfo.invalidateAuthToken(localAccount, (String)localObject1);
            if ((m != 0) && (n == 0))
            {
              str43 = getAuthToken(localAccount);
              i9 = 0;
              if (!LOGV)
                break label2961;
              Log.v("MplayHandler", "Will retry with updated token");
              localObject1 = str43;
              m = i9;
            }
          }
          else
          {
            throw new HttpResponseException(i3, str42);
          }
        }
        else
        {
          if (i3 == 403)
          {
            Header localHeader3 = this.mResponse.getLastHeader("X-Rejected-Reason");
            if (localHeader3 != null)
            {
              String str44 = localHeader3.getValue();
              if (!TextUtils.isEmpty(str44))
              {
                ServerRejectionException.RejectionReason localRejectionReason = null;
                if ("DEVICE_NOT_AUTHORIZED".equalsIgnoreCase(str44))
                  localRejectionReason = ServerRejectionException.RejectionReason.DEVICE_NOT_AUTHORIZED;
                while (localRejectionReason != null)
                {
                  throw new ServerRejectionException(localRejectionReason);
                  if ("ANOTHER_STREAM_BEING_PLAYED".equalsIgnoreCase(str44))
                  {
                    localRejectionReason = ServerRejectionException.RejectionReason.ANOTHER_STREAM_BEING_PLAYED;
                  }
                  else if ("STREAM_RATE_LIMIT_REACHED".equalsIgnoreCase(str44))
                  {
                    localRejectionReason = ServerRejectionException.RejectionReason.STREAM_RATE_LIMIT_REACHED;
                  }
                  else if ("TRACK_NOT_IN_SUBSCRIPTION".equalsIgnoreCase(str44))
                  {
                    localRejectionReason = ServerRejectionException.RejectionReason.TRACK_NOT_IN_SUBSCRIPTION;
                    Context localContext5 = this.mContext;
                    ContentIdentifier localContentIdentifier2 = this.mDownloadRequest.getId();
                    DownloadUtils.purgeNautilusTrackByLocalId(localContext5, localContentIdentifier2);
                  }
                }
                String str45 = "Server returned an unknown rejection reason: " + str44;
                Log.w("MplayHandler", str45);
              }
            }
            throw new HttpResponseException(i3, "Unable to stream due to 403 error");
          }
          if (i3 == 404)
          {
            String str46 = "Unable to download stream due to 404 (file not found) error";
            if (LOGV)
              Log.v("MplayHandler", str46);
            throw new HttpResponseException(i3, str46);
          }
          if (i3 == 503)
          {
            if (this.mResponse.getFirstHeader("Retry-After") != null)
              try
              {
                long l13 = Long.valueOf(this.mResponse.getFirstHeader("Retry-After").getValue()).longValue();
                String str47 = "Server said to retry after " + l13 + " seconds";
                Log.w("MplayHandler", str47);
                String str48 = "Unable to download stream due to 503 (Service Unavailable) error.  Unavailable for " + l13 + " seconds.";
                throw new ServiceUnavailableException(l13, str48);
              }
              catch (NumberFormatException localNumberFormatException3)
              {
                throw new HttpResponseException(i3, "Unable to download stream due to 503 error.");
              }
            Log.w("MplayHandler", "Received 503 with no Retry-After header");
            throw new HttpResponseException(i3, "Unable to download stream due to 503 error.");
          }
          if (LOGV)
          {
            String str49 = "Unable to download stream due to HTTP error " + i3;
            Log.e("MplayHandler", str49);
          }
          String str50 = "Unable to download stream due to HTTP error " + i3;
          throw new HttpResponseException(i3, str50);
        }
      }
      else
      {
        throw new IOException("Unable to retreive stream");
        label2961: localObject1 = str43;
        m = i9;
      }
    }
  }

  public void prepareInputStream()
    throws IOException, InterruptedException
  {
    InputStream localInputStream = getDownloadStream();
    this.mInputStream = localInputStream;
    if (this.mInputStream == null)
      return;
    if (!this.mDownloadState.isCpOn())
      return;
    this.mMusicPreferences.updateNautilusTimestamp();
  }

  public void releaseConnection()
  {
    HttpRequestBase localHttpRequestBase = this.mRequest;
    HttpResponse localHttpResponse = this.mResponse;
    MusicRequest.releaseResponse(localHttpRequestBase, localHttpResponse);
  }

  public void setSyncAccount()
  {
    if (!this.mDownloadRequest.getId().isDefaultDomain())
      return;
    try
    {
      Store localStore = Store.getInstance(this.mContext);
      int i = this.mDownloadRequest.getSourceAccount();
      Account localAccount = localStore.getAccountByHash(i);
      this.mAccount = localAccount;
      return;
    }
    catch (ProviderException localProviderException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Could not find account for sourceAccount: ");
      int j = this.mDownloadRequest.getSourceAccount();
      String str = j;
      Log.e("MplayHandler", str, localProviderException);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.MplayHandler
 * JD-Core Version:    0.6.2
 */