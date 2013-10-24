package com.google.android.play.analytics;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.common.http.GoogleHttpClient;
import com.google.android.play.utils.Assertions;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.zip.GZIPOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

public final class EventLogger
  implements RollingFileStream.WriteCallbacks<ClientAnalytics.LogEvent>
{
  private static final ClientAnalytics.ActiveExperiments EMPTY_EXPERIMENTS = new ClientAnalytics.ActiveExperiments();
  private static final HashSet<Account> sInstantiatedAccounts = new HashSet();
  private final Account mAccount;
  private final long mAndroidId;
  private final String mAppVersion;
  private final String mAuthTokenType;
  private final Context mContext;
  private final long mDelayBetweenUploadsMs;
  private final Handler mHandler;
  private final GoogleHttpClient mHttpClient;
  private ClientAnalytics.ActiveExperiments mLastSentExperiments;
  private final int mLogSource;
  private final String mLoggingId;
  private final int mMaxNumberOfRedirects;
  private final long mMaxUploadSize;
  private final String mMccmnc;
  private final long mMinDelayBetweenUploadsMs;
  private final long mMinImmediateUploadSize;
  private volatile long mNextAllowedUploadTimeMs;
  private final ProtoCache mProtoCache;
  private final RollingFileStream<ClientAnalytics.LogEvent> mRollingFileStream;
  private final String mServerUrl;

  public EventLogger(Context paramContext, String paramString1, String paramString2, LogSource paramLogSource, String paramString3, long paramLong, String paramString4, String paramString5, Configuration paramConfiguration, Account paramAccount)
  {
    while (true)
    {
      synchronized (sInstantiatedAccounts)
      {
        HashSet localHashSet2 = sInstantiatedAccounts;
        Account localAccount1 = paramAccount;
        boolean bool1 = localHashSet2.add(localAccount1);
        StringBuilder localStringBuilder1 = new StringBuilder().append("Already instantiated an EventLogger for ");
        Account localAccount2 = paramAccount;
        String str1 = localAccount2;
        Assertions.checkState(bool1, str1);
        this.mContext = paramContext;
        int i = paramLogSource.getProtoValue();
        this.mLogSource = i;
        this.mLoggingId = paramString1;
        Account localAccount3 = paramAccount;
        this.mAccount = localAccount3;
        String str2 = paramString2;
        this.mAuthTokenType = str2;
        ProtoCache localProtoCache = ProtoCache.getInstance();
        this.mProtoCache = localProtoCache;
        Context localContext = this.mContext;
        String str3 = paramString3;
        GoogleHttpClient localGoogleHttpClient = new GoogleHttpClient(localContext, str3, true);
        this.mHttpClient = localGoogleHttpClient;
        long l1 = paramLong;
        this.mAndroidId = l1;
        String str4 = paramString4;
        this.mAppVersion = str4;
        String str5 = paramString5;
        this.mMccmnc = str5;
        String str6 = paramConfiguration.mServerUrl;
        this.mServerUrl = str6;
        long l2 = paramConfiguration.delayBetweenUploadsMs;
        this.mDelayBetweenUploadsMs = l2;
        long l3 = paramConfiguration.minDelayBetweenUploadsMs;
        this.mMinDelayBetweenUploadsMs = l3;
        int j = paramConfiguration.maxNumberOfRedirects;
        this.mMaxNumberOfRedirects = j;
        long l4 = paramConfiguration.recommendedLogFileSize * 50L / 100L + 1L;
        this.mMinImmediateUploadSize = l4;
        long l5 = paramConfiguration.recommendedLogFileSize * 125L / 100L;
        this.mMaxUploadSize = l5;
        File localFile1 = this.mContext.getCacheDir();
        String str7 = paramConfiguration.logDirectoryName;
        File localFile2 = new File(localFile1, str7);
        if (paramAccount == null)
        {
          str8 = "null_account";
          String str9 = Uri.encode(str8);
          File localFile3 = new File(localFile2, str9);
          maybeRenameLegacyDir(paramAccount, localFile2, localFile3);
          long l6 = paramConfiguration.recommendedLogFileSize;
          long l7 = paramConfiguration.maxStorageSize;
          EventLogger localEventLogger = this;
          RollingFileStream localRollingFileStream = new RollingFileStream(localFile3, "eventlog.store", ".log", l6, l7, localEventLogger);
          this.mRollingFileStream = localRollingFileStream;
          Looper localLooper = startHandlerThread().getLooper();
          Handler local1 = new Handler(localLooper)
          {
            public void dispatchMessage(Message paramAnonymousMessage)
            {
              EventLogger.this.dispatchMessage(paramAnonymousMessage);
            }
          };
          this.mHandler = local1;
          boolean bool2 = this.mHandler.sendEmptyMessage(1);
          return;
        }
      }
      StringBuilder localStringBuilder2 = new StringBuilder();
      String str10 = paramAccount.type;
      StringBuilder localStringBuilder3 = localStringBuilder2.append(str10).append(".");
      String str11 = paramAccount.name;
      String str8 = str11;
    }
  }

  private void addEventImpl(Message paramMessage)
  {
    ClientAnalytics.LogEvent localLogEvent = (ClientAnalytics.LogEvent)paramMessage.obj;
    try
    {
      if (this.mRollingFileStream.write(localLogEvent))
        maybeQueueImmediateUpload();
      return;
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Could not write string (").append(localLogEvent).append(") to file: ");
      String str1 = localIOException.getMessage();
      String str2 = str1;
      int i = Log.e("PlayEventLogger", str2, localIOException);
      return;
    }
    finally
    {
      this.mProtoCache.recycle(localLogEvent);
    }
  }

  private byte[] createByteArrayFrom(InputStream paramInputStream, int paramInt)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    byte[] arrayOfByte = new byte[paramInt];
    int i;
    do
    {
      i = paramInputStream.read(arrayOfByte);
      if (i > 0)
        localByteArrayOutputStream.write(arrayOfByte, 0, i);
    }
    while (i >= 0);
    return localByteArrayOutputStream.toByteArray();
  }

  private void dispatchMessage(Message paramMessage)
  {
    switch (paramMessage.what)
    {
    default:
      StringBuilder localStringBuilder = new StringBuilder().append("Unknown msg: ");
      int i = paramMessage.what;
      String str = i;
      int j = Log.w("PlayEventLogger", str);
      return;
    case 1:
      maybeQueueImmediateUpload();
      long l1 = this.mDelayBetweenUploadsMs;
      queueUpload(l1);
      return;
    case 2:
      addEventImpl(paramMessage);
      return;
    case 3:
    }
    this.mHandler.removeMessages(3);
    if (uploadEventsImpl())
      maybeQueueImmediateUpload();
    long l2 = this.mDelayBetweenUploadsMs;
    queueUpload(l2);
  }

  private String getAuthToken(Account paramAccount)
  {
    Object localObject = null;
    if (paramAccount == null)
      int i = Log.w("PlayEventLogger", "No account for auth token provided");
    while (true)
    {
      return localObject;
      try
      {
        AccountManager localAccountManager = AccountManager.get(this.mContext);
        String str1 = this.mAuthTokenType;
        String str2 = localAccountManager.blockingGetAuthToken(paramAccount, str1, true);
        localObject = str2;
      }
      catch (OperationCanceledException localOperationCanceledException)
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("Failed to get auth token: ");
        String str3 = localOperationCanceledException.getMessage();
        String str4 = str3;
        int j = Log.e("PlayEventLogger", str4, localOperationCanceledException);
      }
      catch (AuthenticatorException localAuthenticatorException)
      {
        StringBuilder localStringBuilder2 = new StringBuilder().append("Failed to get auth token: ");
        String str5 = localAuthenticatorException.getMessage();
        String str6 = str5;
        int k = Log.e("PlayEventLogger", str6, localAuthenticatorException);
      }
      catch (IOException localIOException)
      {
        StringBuilder localStringBuilder3 = new StringBuilder().append("Failed to get auth token: ");
        String str7 = localIOException.getMessage();
        String str8 = str7;
        int m = Log.e("PlayEventLogger", str8, localIOException);
      }
    }
  }

  private void handleResponse(HttpResponse paramHttpResponse)
  {
    try
    {
      InputStream localInputStream = paramHttpResponse.getEntity().getContent();
      ClientAnalytics.LogResponse localLogResponse = ClientAnalytics.LogResponse.parseFrom(createByteArrayFrom(localInputStream, 128));
      if (localLogResponse.nextRequestWaitMillis < 0L)
        return;
      long l = localLogResponse.nextRequestWaitMillis;
      setNextUploadTimeAfter(l);
      return;
    }
    catch (InvalidProtocolBufferNanoException localInvalidProtocolBufferNanoException)
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("Error parsing content: ");
      String str1 = localInvalidProtocolBufferNanoException.getMessage();
      String str2 = str1;
      int i = Log.e("PlayEventLogger", str2);
      return;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      StringBuilder localStringBuilder2 = new StringBuilder().append("Error getting the content of the response body: ");
      String str3 = localIllegalStateException.getMessage();
      String str4 = str3;
      int j = Log.e("PlayEventLogger", str4);
      return;
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder3 = new StringBuilder().append("Error reading the content of the response body: ");
      String str5 = localIOException.getMessage();
      String str6 = str5;
      int k = Log.e("PlayEventLogger", str6);
    }
  }

  private void maybeQueueImmediateUpload()
  {
    long l1 = this.mRollingFileStream.totalUnreadFileLength();
    long l2 = this.mMinImmediateUploadSize;
    if (l1 < l2)
      return;
    queueUpload(0L);
  }

  private static void maybeRenameLegacyDir(Account paramAccount, File paramFile1, File paramFile2)
  {
    if (paramAccount == null)
      return;
    if (paramFile2.exists())
      return;
    String str = Uri.encode(paramAccount.name);
    File localFile = new File(paramFile1, str);
    if (!localFile.exists())
      return;
    if (!localFile.isDirectory())
      return;
    boolean bool = localFile.renameTo(paramFile2);
  }

  private void queueUpload(long paramLong)
  {
    long l1 = System.currentTimeMillis();
    if (paramLong > 0L)
    {
      long l2 = l1 + paramLong;
      long l3 = this.mNextAllowedUploadTimeMs;
      if (l2 < l3)
        paramLong = this.mNextAllowedUploadTimeMs - l1;
      boolean bool1 = this.mHandler.sendEmptyMessageDelayed(3, paramLong);
    }
    while (true)
    {
      long l4 = this.mNextAllowedUploadTimeMs;
      long l5 = this.mMinDelayBetweenUploadsMs + l1;
      long l6 = Math.max(l4, l5);
      this.mNextAllowedUploadTimeMs = l6;
      return;
      boolean bool2 = this.mHandler.sendEmptyMessage(3);
    }
  }

  private byte[][] readSerializedLogEvents()
    throws IOException
  {
    ArrayList localArrayList = new ArrayList();
    long l1 = 0L;
    byte[] arrayOfByte1 = this.mRollingFileStream.read();
    if (arrayOfByte1 == null)
      label26: if (!localArrayList.isEmpty())
        break label108;
    label108: byte[] arrayOfByte2;
    for (Object localObject = (byte[][])null; ; localObject = arrayOfByte2)
    {
      return localObject;
      if (arrayOfByte1.length > 0)
      {
        boolean bool = localArrayList.add(arrayOfByte1);
        long l2 = arrayOfByte1.length;
        l1 += l2;
      }
      long l3 = this.mRollingFileStream.peekNextReadLength();
      if (l3 < 0L)
        break label26;
      long l4 = l1 + l3;
      long l5 = this.mMaxUploadSize;
      if (l4 <= l5)
        break;
      break label26;
      arrayOfByte2 = new byte[localArrayList.size()];
      Object[] arrayOfObject = localArrayList.toArray(arrayOfByte2);
    }
  }

  private void setNextUploadTimeAfter(long paramLong)
  {
    long l1 = Math.max(this.mMinDelayBetweenUploadsMs, paramLong);
    long l2 = System.currentTimeMillis() + l1;
    this.mNextAllowedUploadTimeMs = l2;
  }

  private static HandlerThread startHandlerThread()
  {
    final Semaphore localSemaphore = new Semaphore(0);
    HandlerThread local2 = new HandlerThread("PlayEventLogger", 10)
    {
      protected void onLooperPrepared()
      {
        localSemaphore.release();
      }
    };
    local2.start();
    localSemaphore.acquireUninterruptibly();
    return local2;
  }

  private boolean uploadEventsImpl()
  {
    boolean bool1 = false;
    boolean bool2 = false;
    try
    {
      boolean bool3 = this.mRollingFileStream.hasUnreadFiles();
      if (!bool3)
        if (0 != 0)
          this.mRollingFileStream.deleteAllReadFiles();
      while (true)
      {
        return bool1;
        this.mRollingFileStream.markAllFilesAsUnread();
        continue;
        ClientAnalytics.LogRequest localLogRequest = new ClientAnalytics.LogRequest();
        long l1 = System.currentTimeMillis();
        localLogRequest.requestTimeMs = l1;
        ClientAnalytics.AndroidClientInfo localAndroidClientInfo = new ClientAnalytics.AndroidClientInfo();
        long l2 = this.mAndroidId;
        localAndroidClientInfo.androidId = l2;
        if (this.mLoggingId != null)
        {
          String str1 = this.mLoggingId;
          localAndroidClientInfo.loggingId = str1;
        }
        int i = Build.VERSION.SDK_INT;
        localAndroidClientInfo.sdkVersion = i;
        String str2 = Build.MODEL;
        localAndroidClientInfo.model = str2;
        String str3 = Build.PRODUCT;
        localAndroidClientInfo.product = str3;
        String str4 = Build.HARDWARE;
        localAndroidClientInfo.hardware = str4;
        String str5 = Build.DEVICE;
        localAndroidClientInfo.device = str5;
        String str6 = Build.ID;
        localAndroidClientInfo.osBuild = str6;
        String str7 = this.mMccmnc;
        localAndroidClientInfo.mccMnc = str7;
        Locale localLocale = Locale.getDefault();
        String str8 = localLocale.getLanguage();
        localAndroidClientInfo.locale = str8;
        String str9 = localLocale.getCountry();
        localAndroidClientInfo.country = str9;
        String str10 = this.mAppVersion;
        localAndroidClientInfo.applicationBuild = str10;
        ClientAnalytics.ClientInfo localClientInfo = new ClientAnalytics.ClientInfo();
        localClientInfo.clientType = 4;
        localClientInfo.androidClientInfo = localAndroidClientInfo;
        localLogRequest.clientInfo = localClientInfo;
        int j = this.mLogSource;
        localLogRequest.logSource = j;
        try
        {
          byte[][] arrayOfByte = readSerializedLogEvents();
          localLogRequest.serializedLogEvents = arrayOfByte;
          if (localLogRequest.serializedLogEvents == null)
          {
            int k = Log.d("PlayEventLogger", "uploadEventsImpl: Thought we had files ready to send, but didn't");
            this.mProtoCache.recycleLogRequest(localLogRequest);
            if (0 != 0)
              this.mRollingFileStream.deleteAllReadFiles();
            else
              this.mRollingFileStream.markAllFilesAsUnread();
          }
          else
          {
            Account localAccount = this.mAccount;
            String str11 = this.mServerUrl;
            int m = this.mMaxNumberOfRedirects;
            bool3 = uploadLog(localAccount, localLogRequest, str11, m);
            bool2 = bool3;
            this.mProtoCache.recycleLogRequest(localLogRequest);
            if (bool2)
              this.mRollingFileStream.deleteAllReadFiles();
            while (true)
            {
              bool1 = bool2;
              break;
              this.mRollingFileStream.markAllFilesAsUnread();
            }
          }
        }
        catch (IOException localIOException)
        {
          StringBuilder localStringBuilder1 = new StringBuilder().append("Upload failed ");
          Class localClass = localIOException.getClass();
          StringBuilder localStringBuilder2 = localStringBuilder1.append(localClass).append("(");
          String str12 = localIOException.getMessage();
          String str13 = str12 + ")";
          int n = Log.e("PlayEventLogger", str13);
          this.mProtoCache.recycleLogRequest(localLogRequest);
          if (0 != 0)
            this.mRollingFileStream.deleteAllReadFiles();
          else
            this.mRollingFileStream.markAllFilesAsUnread();
        }
        finally
        {
          this.mProtoCache.recycleLogRequest(localLogRequest);
        }
      }
    }
    finally
    {
      if (!bool2)
        break label543;
    }
    this.mRollingFileStream.deleteAllReadFiles();
    while (true)
    {
      throw localObject2;
      label543: this.mRollingFileStream.markAllFilesAsUnread();
    }
  }

  private boolean uploadLog(Account paramAccount, ClientAnalytics.LogRequest paramLogRequest, String paramString, int paramInt)
    throws IOException
  {
    String str1 = getAuthToken(paramAccount);
    String str2 = paramString;
    HttpPost localHttpPost = new HttpPost(str2);
    String str3 = "GoogleLogin auth=" + str1;
    String str4 = "Authorization";
    String str5 = str3;
    localHttpPost.addHeader(str4, str5);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    GZIPOutputStream localGZIPOutputStream = new GZIPOutputStream(localByteArrayOutputStream);
    byte[] arrayOfByte1 = MessageNano.toByteArray(paramLogRequest);
    localGZIPOutputStream.write(arrayOfByte1);
    localGZIPOutputStream.close();
    byte[] arrayOfByte2 = localByteArrayOutputStream.toByteArray();
    ByteArrayEntity localByteArrayEntity = new ByteArrayEntity(arrayOfByte2);
    String str6 = "gzip";
    localByteArrayEntity.setContentEncoding(str6);
    String str7 = "application/x-gzip";
    localByteArrayEntity.setContentType(str7);
    localHttpPost.setEntity(localByteArrayEntity);
    HttpResponse localHttpResponse1 = this.mHttpClient.execute(localHttpPost);
    StatusLine localStatusLine = localHttpResponse1.getStatusLine();
    int i = localStatusLine.getStatusCode();
    int j = 200;
    int k = i;
    boolean bool;
    if (j <= k)
    {
      int m = i;
      int n = 300;
      if (m < n)
      {
        bool = true;
        EventLogger localEventLogger1 = this;
        HttpResponse localHttpResponse2 = localHttpResponse1;
        localEventLogger1.handleResponse(localHttpResponse2);
      }
    }
    while (true)
    {
      return bool;
      int i1 = 300;
      int i2 = i;
      if (i1 <= i2)
      {
        int i3 = i;
        int i4 = 400;
        if (i3 < i4)
        {
          if (paramInt > 0)
          {
            HttpResponse localHttpResponse3 = localHttpResponse1;
            String str8 = "Location";
            Header localHeader1 = localHttpResponse3.getFirstHeader(str8);
            if (localHeader1 == null)
            {
              StringBuilder localStringBuilder1 = new StringBuilder().append("Status ");
              int i5 = i;
              String str9 = i5 + "... redirect: no location header";
              int i6 = Log.e("PlayEventLogger", str9);
              bool = true;
              continue;
            }
            String str10 = localHeader1.getValue();
            int i7 = paramInt + -1;
            EventLogger localEventLogger2 = this;
            Account localAccount = paramAccount;
            ClientAnalytics.LogRequest localLogRequest = paramLogRequest;
            String str11 = str10;
            int i8 = i7;
            bool = localEventLogger2.uploadLog(localAccount, localLogRequest, str11, i8);
            continue;
          }
          StringBuilder localStringBuilder2 = new StringBuilder().append("Server returned ");
          int i9 = i;
          String str12 = i9 + "... redirect, but no more redirects" + " allowed.";
          int i10 = Log.e("PlayEventLogger", str12);
          bool = false;
        }
      }
      else
      {
        int i11 = i;
        int i12 = 400;
        if (i11 == i12)
        {
          int i13 = Log.e("PlayEventLogger", "Server returned 400... deleting local malformed logs");
          bool = true;
        }
        else
        {
          int i14 = i;
          int i15 = 401;
          if (i14 == i15)
          {
            int i16 = Log.w("PlayEventLogger", "Server returned 401... invalidating auth token");
            AccountManager localAccountManager = AccountManager.get(this.mContext);
            String str13 = paramAccount.type;
            localAccountManager.invalidateAuthToken(str13, str1);
            bool = false;
          }
          else
          {
            int i17 = i;
            int i18 = 500;
            if (i17 == i18)
            {
              int i19 = Log.w("PlayEventLogger", "Server returned 500... server crashed");
              bool = false;
            }
            else
            {
              int i20 = i;
              int i21 = 501;
              if (i20 == i21)
              {
                int i22 = Log.w("PlayEventLogger", "Server returned 501... service doesn't seem to exist");
                bool = false;
              }
              else
              {
                int i23 = i;
                int i24 = 502;
                if (i23 == i24)
                {
                  int i25 = Log.w("PlayEventLogger", "Server returned 502... servers are down");
                  bool = false;
                }
                else
                {
                  int i26 = i;
                  int i27 = 503;
                  if (i26 == i27)
                  {
                    HttpResponse localHttpResponse4 = localHttpResponse1;
                    String str14 = "Retry-After";
                    Header localHeader2 = localHttpResponse4.getFirstHeader(str14);
                    if (localHeader2 != null)
                    {
                      int i28 = 0;
                      String str15 = localHeader2.getValue();
                      try
                      {
                        long l1 = Long.valueOf(str15).longValue();
                        StringBuilder localStringBuilder3 = new StringBuilder().append("Server said to retry after ");
                        long l2 = l1;
                        String str16 = l2 + " seconds";
                        int i29 = Log.w("PlayEventLogger", str16);
                        long l3 = 1000L * l1;
                        EventLogger localEventLogger3 = this;
                        long l4 = l3;
                        localEventLogger3.setNextUploadTimeAfter(l4);
                        i28 = 1;
                        if (i28 == 0)
                          bool = true;
                      }
                      catch (NumberFormatException localNumberFormatException)
                      {
                        while (true)
                        {
                          StringBuilder localStringBuilder4 = new StringBuilder().append("Unknown retry value: ");
                          String str17 = str15;
                          String str18 = str17;
                          int i31 = Log.e("PlayEventLogger", str18);
                          continue;
                          bool = false;
                        }
                      }
                    }
                    else
                    {
                      int i32 = Log.e("PlayEventLogger", "Status 503 without retry-after header");
                      bool = true;
                    }
                  }
                  else
                  {
                    int i33 = i;
                    int i30 = 504;
                    if (i33 == i30)
                    {
                      int i34 = Log.w("PlayEventLogger", "Server returned 504... timeout");
                      bool = false;
                    }
                    else
                    {
                      StringBuilder localStringBuilder5 = new StringBuilder().append("Unexpected error received from server: ");
                      int i35 = i;
                      StringBuilder localStringBuilder6 = localStringBuilder5.append(i35).append(" ");
                      String str19 = localStatusLine.getReasonPhrase();
                      String str20 = str19;
                      int i36 = Log.e("PlayEventLogger", str20);
                      bool = true;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  private void writeRawVarint32(int paramInt, OutputStream paramOutputStream)
    throws IOException
  {
    while (true)
    {
      if ((paramInt & 0xFFFFFF80) == 0)
      {
        paramOutputStream.write(paramInt);
        return;
      }
      int i = paramInt & 0x7F | 0x80;
      paramOutputStream.write(i);
      paramInt >>>= 7;
    }
  }

  public void logEvent(String paramString, ClientAnalytics.ActiveExperiments paramActiveExperiments, byte[] paramArrayOfByte, String[] paramArrayOfString)
  {
    boolean bool;
    ClientAnalytics.LogEvent localLogEvent;
    int j;
    label102: ClientAnalytics.LogEventKeyValues localLogEventKeyValues;
    int n;
    if ((paramArrayOfString == null) || (paramArrayOfString.length % 2 == 0))
    {
      bool = true;
      Assertions.checkState(bool, "Extras must be null or of even length.");
      localLogEvent = this.mProtoCache.obtainEvent();
      long l = System.currentTimeMillis();
      localLogEvent.eventTimeMs = l;
      localLogEvent.tag = paramString;
      localLogEvent.exp = paramActiveExperiments;
      if (paramArrayOfByte != null)
        localLogEvent.sourceExtension = paramArrayOfByte;
      if ((paramArrayOfString == null) || (paramArrayOfString.length == 0))
        break label205;
      int i = paramArrayOfString.length / 2;
      ClientAnalytics.LogEventKeyValues[] arrayOfLogEventKeyValues = new ClientAnalytics.LogEventKeyValues[i];
      localLogEvent.value = arrayOfLogEventKeyValues;
      j = 0;
      if (j >= i)
        break label205;
      localLogEventKeyValues = this.mProtoCache.obtainKeyValue();
      int k = j * 2;
      String str1 = paramArrayOfString[k];
      localLogEventKeyValues.key = str1;
      int m = k + 1;
      if (paramArrayOfString[m] == null)
        break label197;
      n = k + 1;
    }
    label197: for (String str2 = paramArrayOfString[n]; ; str2 = "null")
    {
      localLogEventKeyValues.value = str2;
      localLogEvent.value[j] = localLogEventKeyValues;
      j += 1;
      break label102;
      bool = false;
      break;
    }
    label205: this.mHandler.obtainMessage(2, localLogEvent).sendToTarget();
  }

  public void logEvent(String paramString, ClientAnalytics.ActiveExperiments paramActiveExperiments, String[] paramArrayOfString)
  {
    byte[] arrayOfByte = (byte[])null;
    logEvent(paramString, paramActiveExperiments, arrayOfByte, paramArrayOfString);
  }

  public void onNewOutputFile()
  {
    this.mLastSentExperiments = null;
  }

  public void onWrite(ClientAnalytics.LogEvent paramLogEvent, OutputStream paramOutputStream)
    throws IOException
  {
    ClientAnalytics.ActiveExperiments localActiveExperiments1 = paramLogEvent.exp;
    ClientAnalytics.ActiveExperiments localActiveExperiments2 = this.mLastSentExperiments;
    if (localActiveExperiments1 == localActiveExperiments2)
      paramLogEvent.exp = null;
    while (true)
    {
      byte[] arrayOfByte = MessageNano.toByteArray(paramLogEvent);
      int i = arrayOfByte.length;
      writeRawVarint32(i, paramOutputStream);
      paramOutputStream.write(arrayOfByte);
      return;
      ClientAnalytics.ActiveExperiments localActiveExperiments3 = paramLogEvent.exp;
      this.mLastSentExperiments = localActiveExperiments3;
      if (paramLogEvent.exp == null)
      {
        ClientAnalytics.ActiveExperiments localActiveExperiments4 = EMPTY_EXPERIMENTS;
        paramLogEvent.exp = localActiveExperiments4;
      }
    }
  }

  public static class Configuration
  {
    public long delayBetweenUploadsMs = 300000L;
    public String logDirectoryName = "logs";
    public String mServerUrl = "https://android.clients.google.com/play/log";
    public int maxNumberOfRedirects = 5;
    public long maxStorageSize = 2097152L;
    public long minDelayBetweenUploadsMs = 60000L;
    public long recommendedLogFileSize = 51200L;
  }

  public static enum LogSource
  {
    private final int mProtoValue;

    static
    {
      BOOKS = new LogSource("BOOKS", 2, 2);
      VIDEO = new LogSource("VIDEO", 3, 3);
      MAGAZINES = new LogSource("MAGAZINES", 4, 4);
      GAMES = new LogSource("GAMES", 5, 5);
      LB_A = new LogSource("LB_A", 6, 6);
      ANDROID_IDE = new LogSource("ANDROID_IDE", 7, 7);
      LB_P = new LogSource("LB_P", 8, 8);
      LB_S = new LogSource("LB_S", 9, 9);
      LogSource[] arrayOfLogSource = new LogSource[10];
      LogSource localLogSource1 = MARKET;
      arrayOfLogSource[0] = localLogSource1;
      LogSource localLogSource2 = MUSIC;
      arrayOfLogSource[1] = localLogSource2;
      LogSource localLogSource3 = BOOKS;
      arrayOfLogSource[2] = localLogSource3;
      LogSource localLogSource4 = VIDEO;
      arrayOfLogSource[3] = localLogSource4;
      LogSource localLogSource5 = MAGAZINES;
      arrayOfLogSource[4] = localLogSource5;
      LogSource localLogSource6 = GAMES;
      arrayOfLogSource[5] = localLogSource6;
      LogSource localLogSource7 = LB_A;
      arrayOfLogSource[6] = localLogSource7;
      LogSource localLogSource8 = ANDROID_IDE;
      arrayOfLogSource[7] = localLogSource8;
      LogSource localLogSource9 = LB_P;
      arrayOfLogSource[8] = localLogSource9;
      LogSource localLogSource10 = LB_S;
      arrayOfLogSource[9] = localLogSource10;
    }

    private LogSource(int paramInt)
    {
      this.mProtoValue = paramInt;
    }

    public int getProtoValue()
    {
      return this.mProtoValue;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.play.analytics.EventLogger
 * JD-Core Version:    0.6.2
 */