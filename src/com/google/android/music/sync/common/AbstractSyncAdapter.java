package com.google.android.music.sync.common;

import android.accounts.Account;
import android.accounts.AuthenticatorException;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.content.SyncStats;
import android.os.Bundle;
import android.util.Pair;
import com.google.android.common.LoggingThreadedSyncAdapter;
import com.google.android.music.log.Log;
import com.google.android.music.sync.api.ServiceUnavailableException;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractSyncAdapter extends LoggingThreadedSyncAdapter
{
  private int mActionOnInitialization;
  protected AuthInfo mAuthInfo;
  protected final Context mContext;
  private int mMaxDownstreamLoops;
  private int mMaxQueueSize;
  private final AtomicBoolean mSyncActive;
  protected String mTag = "AbstractSyncAdapter";
  private final ExecutorService mThreadPool;
  private final boolean mUseVerboseLogging;

  protected AbstractSyncAdapter(Context paramContext)
  {
    super(paramContext, false);
    AtomicBoolean localAtomicBoolean = new AtomicBoolean(false);
    this.mSyncActive = localAtomicBoolean;
    ExecutorService localExecutorService = Executors.newFixedThreadPool(2);
    this.mThreadPool = localExecutorService;
    this.mContext = paramContext;
    boolean bool = DebugUtils.isLoggable(DebugUtils.MusicTag.SYNC);
    this.mUseVerboseLogging = bool;
  }

  private final void download(SyncResult paramSyncResult, Account paramAccount, HashMap<String, Object> paramHashMap)
    throws AuthenticatorException, HardSyncException, SoftSyncException
  {
    onDownstreamStart(paramAccount, paramHashMap);
    int i = this.mMaxQueueSize;
    DownstreamFetchQueue localDownstreamFetchQueue = new DownstreamFetchQueue(i);
    Object localObject1 = null;
    Object localObject2 = null;
    while (true)
    {
      try
      {
        Context localContext1 = getContext();
        Map localMap = Collections.unmodifiableMap(paramHashMap);
        DownstreamReader localDownstreamReader = createDownstreamReader(localDownstreamFetchQueue, 100, localContext1, localMap);
        localFuture1 = this.mThreadPool.submit(localDownstreamReader);
        DownstreamMergeQueue localDownstreamMergeQueue = localDownstreamReader.getMergeQueue();
        Context localContext2 = getContext();
        DownstreamMerger localDownstreamMerger = createDownstreamMerger(localDownstreamMergeQueue, localContext2, paramHashMap);
        Future localFuture2 = this.mThreadPool.submit(localDownstreamMerger);
        localObject1 = localFuture2;
        int j = 0;
        j += 1;
        try
        {
          int k = this.mMaxDownstreamLoops;
          if (j <= k)
          {
            if (this.mUseVerboseLogging)
            {
              String str1 = this.mTag;
              String str2 = "Downstream loop " + j;
              Log.v(str1, str2);
            }
            if (fetchDataFromServer(paramAccount, localDownstreamFetchQueue, paramHashMap));
          }
          else
          {
            int m = this.mMaxDownstreamLoops;
            if (j > m)
            {
              if (this.mUseVerboseLogging)
                Log.v(this.mTag, "Exceeded maximum number of downstream loops.");
              paramSyncResult.tooManyRetries = true;
            }
            localDownstreamFetchQueue.close();
            if (this.mUseVerboseLogging)
              Log.v(this.mTag, "Waiting on downstream reader thread to finish...");
            throwIfFutureTaskFailed(localFuture1, paramSyncResult);
            localFuture1 = null;
            if (this.mUseVerboseLogging)
              Log.v(this.mTag, "Waiting on downstream merger thread to finish merging...");
            throwIfFutureTaskFailed(localObject1, paramSyncResult);
            Future localFuture3 = null;
            if ((localFuture1 != null) || (localFuture3 != null))
            {
              if (this.mUseVerboseLogging)
                Log.v(this.mTag, "Error occurred when dowloading. Waiting for download threads.");
              localDownstreamFetchQueue.kill();
              safeWait(localFuture1);
              safeWait(localFuture3);
            }
            onDownstreamComplete(paramAccount, paramHashMap);
            if (!this.mUseVerboseLogging)
              return;
            Log.v(this.mTag, "Downstream sync complete.");
            return;
          }
          if (hasFutureTaskReportedAnError(localFuture1))
          {
            Log.w(this.mTag, "Bailing on downstream reader thread due to an error.");
            continue;
          }
        }
        finally
        {
          localDownstreamFetchQueue.close();
        }
      }
      finally
      {
        Future localFuture1;
        if ((localFuture1 != null) || (localObject1 != null))
        {
          if (this.mUseVerboseLogging)
            Log.v(this.mTag, "Error occurred when dowloading. Waiting for download threads.");
          localDownstreamFetchQueue.kill();
          safeWait(localFuture1);
          safeWait(localObject1);
        }
      }
      if (hasFutureTaskReportedAnError(localObject1))
        Log.w(this.mTag, "Bailing on downstream merger thread due to an error.");
    }
  }

  private boolean hasFutureTaskReportedAnError(Future<?> paramFuture)
  {
    boolean bool = false;
    try
    {
      if (paramFuture.isDone())
        Object localObject = paramFuture.get();
      return bool;
    }
    catch (ExecutionException localExecutionException)
    {
      while (true)
        bool = true;
    }
    catch (InterruptedException localInterruptedException)
    {
      while (true)
        Thread.currentThread().interrupt();
    }
  }

  private final void safeWait(Future<?> paramFuture)
  {
    if (paramFuture == null)
      return;
    try
    {
      Object localObject = paramFuture.get();
      return;
    }
    catch (Exception localException)
    {
      Log.w(this.mTag, "Exception while waiting for completion.", localException);
    }
  }

  private void throwIfFutureTaskFailed(Future<?> paramFuture, SyncResult paramSyncResult)
    throws AuthenticatorException, HardSyncException, SoftSyncException
  {
    try
    {
      Object localObject = paramFuture.get();
      return;
    }
    catch (ExecutionException localExecutionException)
    {
      Throwable localThrowable = localExecutionException.getCause();
      if ((localThrowable instanceof ServiceUnavailableException))
      {
        ServiceUnavailableException localServiceUnavailableException = (ServiceUnavailableException)ServiceUnavailableException.class.cast(localThrowable);
        if (localServiceUnavailableException.getRetryAfter() != 0L)
        {
          long l = localServiceUnavailableException.getRetryAfter();
          paramSyncResult.delayUntil = l;
        }
        throw new SoftSyncException(localServiceUnavailableException);
      }
      if ((localThrowable instanceof IOException))
        throw new SoftSyncException(localThrowable);
      if ((localThrowable instanceof SoftSyncException))
        throw ((SoftSyncException)SoftSyncException.class.cast(localThrowable));
      if ((localThrowable instanceof AuthenticatorException))
        throw new AuthenticatorException(localThrowable);
      throw new HardSyncException(localThrowable);
    }
    catch (InterruptedException localInterruptedException)
    {
      Thread.currentThread().interrupt();
    }
  }

  // ERROR //
  private final void upload(SyncResult paramSyncResult, HashMap<String, Object> paramHashMap)
    throws AuthenticatorException, HardSyncException, SoftSyncException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 76	com/google/android/music/sync/common/AbstractSyncAdapter:mUseVerboseLogging	Z
    //   4: ifeq +13 -> 17
    //   7: aload_0
    //   8: getfield 45	com/google/android/music/sync/common/AbstractSyncAdapter:mTag	Ljava/lang/String;
    //   11: ldc_w 265
    //   14: invokestatic 155	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   17: aload_0
    //   18: getfield 83	com/google/android/music/sync/common/AbstractSyncAdapter:mMaxQueueSize	I
    //   21: istore_3
    //   22: new 6	com/google/android/music/sync/common/AbstractSyncAdapter$UpstreamQueue
    //   25: dup
    //   26: iload_3
    //   27: invokespecial 266	com/google/android/music/sync/common/AbstractSyncAdapter$UpstreamQueue:<init>	(I)V
    //   30: astore 4
    //   32: aload_0
    //   33: getfield 62	com/google/android/music/sync/common/AbstractSyncAdapter:mContext	Landroid/content/Context;
    //   36: astore 5
    //   38: aload_0
    //   39: aload 4
    //   41: aload 5
    //   43: aload_2
    //   44: invokevirtual 270	com/google/android/music/sync/common/AbstractSyncAdapter:createUpstreamReader	(Lcom/google/android/music/sync/common/AbstractSyncAdapter$UpstreamQueue;Landroid/content/Context;Ljava/util/Map;)Lcom/google/android/music/sync/common/UpstreamReader;
    //   47: astore 6
    //   49: aload_0
    //   50: getfield 60	com/google/android/music/sync/common/AbstractSyncAdapter:mThreadPool	Ljava/util/concurrent/ExecutorService;
    //   53: aload 6
    //   55: invokeinterface 121 2 0
    //   60: astore 7
    //   62: aload_0
    //   63: getfield 62	com/google/android/music/sync/common/AbstractSyncAdapter:mContext	Landroid/content/Context;
    //   66: astore 8
    //   68: aload_0
    //   69: aload 4
    //   71: aload 8
    //   73: aload_2
    //   74: invokevirtual 274	com/google/android/music/sync/common/AbstractSyncAdapter:createUpstreamSender	(Lcom/google/android/music/sync/common/AbstractSyncAdapter$UpstreamQueue;Landroid/content/Context;Ljava/util/Map;)Lcom/google/android/music/sync/common/UpstreamSender;
    //   77: astore 9
    //   79: aload_0
    //   80: getfield 60	com/google/android/music/sync/common/AbstractSyncAdapter:mThreadPool	Ljava/util/concurrent/ExecutorService;
    //   83: aload 9
    //   85: invokeinterface 121 2 0
    //   90: astore 10
    //   92: aload_0
    //   93: getfield 76	com/google/android/music/sync/common/AbstractSyncAdapter:mUseVerboseLogging	Z
    //   96: ifeq +13 -> 109
    //   99: aload_0
    //   100: getfield 45	com/google/android/music/sync/common/AbstractSyncAdapter:mTag	Ljava/lang/String;
    //   103: ldc_w 276
    //   106: invokestatic 155	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   109: aload_0
    //   110: aload 7
    //   112: aload_1
    //   113: invokespecial 175	com/google/android/music/sync/common/AbstractSyncAdapter:throwIfFutureTaskFailed	(Ljava/util/concurrent/Future;Landroid/content/SyncResult;)V
    //   116: aconst_null
    //   117: astore 7
    //   119: aload_0
    //   120: getfield 76	com/google/android/music/sync/common/AbstractSyncAdapter:mUseVerboseLogging	Z
    //   123: ifeq +13 -> 136
    //   126: aload_0
    //   127: getfield 45	com/google/android/music/sync/common/AbstractSyncAdapter:mTag	Ljava/lang/String;
    //   130: ldc_w 278
    //   133: invokestatic 155	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   136: aload_0
    //   137: aload 10
    //   139: aload_1
    //   140: invokespecial 175	com/google/android/music/sync/common/AbstractSyncAdapter:throwIfFutureTaskFailed	(Ljava/util/concurrent/Future;Landroid/content/SyncResult;)V
    //   143: aload_1
    //   144: getfield 282	android/content/SyncResult:stats	Landroid/content/SyncStats;
    //   147: astore 11
    //   149: aload 10
    //   151: invokeinterface 216 1 0
    //   156: checkcast 284	com/google/android/music/sync/common/UpstreamSender$UpstreamSenderResult
    //   159: getfield 287	com/google/android/music/sync/common/UpstreamSender$UpstreamSenderResult:mNumConflicts	I
    //   162: i2l
    //   163: lstore 12
    //   165: aload 11
    //   167: lload 12
    //   169: putfield 292	android/content/SyncStats:numConflictDetectedExceptions	J
    //   172: aconst_null
    //   173: astore 14
    //   175: aload 7
    //   177: ifnonnull +8 -> 185
    //   180: aload 14
    //   182: ifnull +37 -> 219
    //   185: aload_0
    //   186: getfield 76	com/google/android/music/sync/common/AbstractSyncAdapter:mUseVerboseLogging	Z
    //   189: ifeq +13 -> 202
    //   192: aload_0
    //   193: getfield 45	com/google/android/music/sync/common/AbstractSyncAdapter:mTag	Ljava/lang/String;
    //   196: ldc_w 294
    //   199: invokestatic 155	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   202: aload 4
    //   204: invokevirtual 295	com/google/android/music/sync/common/AbstractSyncAdapter$UpstreamQueue:kill	()V
    //   207: aload_0
    //   208: aload 7
    //   210: invokespecial 186	com/google/android/music/sync/common/AbstractSyncAdapter:safeWait	(Ljava/util/concurrent/Future;)V
    //   213: aload_0
    //   214: aload 14
    //   216: invokespecial 186	com/google/android/music/sync/common/AbstractSyncAdapter:safeWait	(Ljava/util/concurrent/Future;)V
    //   219: aload_0
    //   220: getfield 76	com/google/android/music/sync/common/AbstractSyncAdapter:mUseVerboseLogging	Z
    //   223: ifne +4 -> 227
    //   226: return
    //   227: aload_0
    //   228: getfield 45	com/google/android/music/sync/common/AbstractSyncAdapter:mTag	Ljava/lang/String;
    //   231: ldc_w 297
    //   234: invokestatic 155	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   237: return
    //   238: astore 15
    //   240: invokestatic 222	java/lang/Thread:currentThread	()Ljava/lang/Thread;
    //   243: invokevirtual 225	java/lang/Thread:interrupt	()V
    //   246: goto -74 -> 172
    //   249: astore 16
    //   251: aload 7
    //   253: ifnonnull +8 -> 261
    //   256: aload 10
    //   258: ifnull +37 -> 295
    //   261: aload_0
    //   262: getfield 76	com/google/android/music/sync/common/AbstractSyncAdapter:mUseVerboseLogging	Z
    //   265: ifeq +13 -> 278
    //   268: aload_0
    //   269: getfield 45	com/google/android/music/sync/common/AbstractSyncAdapter:mTag	Ljava/lang/String;
    //   272: ldc_w 294
    //   275: invokestatic 155	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   278: aload 4
    //   280: invokevirtual 295	com/google/android/music/sync/common/AbstractSyncAdapter$UpstreamQueue:kill	()V
    //   283: aload_0
    //   284: aload 7
    //   286: invokespecial 186	com/google/android/music/sync/common/AbstractSyncAdapter:safeWait	(Ljava/util/concurrent/Future;)V
    //   289: aload_0
    //   290: aload 10
    //   292: invokespecial 186	com/google/android/music/sync/common/AbstractSyncAdapter:safeWait	(Ljava/util/concurrent/Future;)V
    //   295: aload 16
    //   297: athrow
    //   298: astore 17
    //   300: aload_0
    //   301: getfield 45	com/google/android/music/sync/common/AbstractSyncAdapter:mTag	Ljava/lang/String;
    //   304: ldc_w 299
    //   307: aload 17
    //   309: invokestatic 302	com/google/android/music/log/Log:wtf	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   312: goto -140 -> 172
    //
    // Exception table:
    //   from	to	target	type
    //   143	172	238	java/lang/InterruptedException
    //   32	143	249	finally
    //   143	172	249	finally
    //   240	246	249	finally
    //   300	312	249	finally
    //   143	172	298	java/util/concurrent/ExecutionException
  }

  protected abstract DownstreamMerger createDownstreamMerger(DownstreamMergeQueue paramDownstreamMergeQueue, Context paramContext, Map<String, Object> paramMap);

  protected abstract DownstreamReader createDownstreamReader(DownstreamFetchQueue paramDownstreamFetchQueue, int paramInt, Context paramContext, Map<String, Object> paramMap);

  protected abstract UpstreamReader createUpstreamReader(UpstreamQueue paramUpstreamQueue, Context paramContext, Map<String, Object> paramMap);

  protected abstract UpstreamSender createUpstreamSender(UpstreamQueue paramUpstreamQueue, Context paramContext, Map<String, Object> paramMap);

  protected abstract boolean fetchDataFromServer(Account paramAccount, DownstreamFetchQueue paramDownstreamFetchQueue, HashMap<String, Object> paramHashMap)
    throws AuthenticatorException, HardSyncException, SoftSyncException;

  // ERROR //
  void innerPerformSync(Bundle paramBundle, String paramString, ContentProviderClient paramContentProviderClient, SyncResult paramSyncResult, Account paramAccount)
    throws AuthenticatorException, HardSyncException, SoftSyncException
  {
    // Byte code:
    //   0: aload 5
    //   2: getfield 309	android/accounts/Account:name	Ljava/lang/String;
    //   5: astore 6
    //   7: aload 5
    //   9: getfield 312	android/accounts/Account:type	Ljava/lang/String;
    //   12: astore 7
    //   14: aload_0
    //   15: getfield 76	com/google/android/music/sync/common/AbstractSyncAdapter:mUseVerboseLogging	Z
    //   18: ifeq +50 -> 68
    //   21: aload_0
    //   22: getfield 45	com/google/android/music/sync/common/AbstractSyncAdapter:mTag	Ljava/lang/String;
    //   25: astore 8
    //   27: new 133	java/lang/StringBuilder
    //   30: dup
    //   31: invokespecial 136	java/lang/StringBuilder:<init>	()V
    //   34: ldc_w 314
    //   37: invokevirtual 142	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   40: aload 6
    //   42: invokevirtual 142	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   45: ldc_w 316
    //   48: invokevirtual 142	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: aload 7
    //   53: invokevirtual 142	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   56: invokevirtual 149	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   59: astore 9
    //   61: aload 8
    //   63: aload 9
    //   65: invokestatic 155	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   68: aload 6
    //   70: ifnull +8 -> 78
    //   73: aload 7
    //   75: ifnonnull +47 -> 122
    //   78: aload 4
    //   80: getfield 282	android/content/SyncResult:stats	Landroid/content/SyncStats;
    //   83: astore 10
    //   85: aload 10
    //   87: getfield 319	android/content/SyncStats:numAuthExceptions	J
    //   90: ldc2_w 320
    //   93: ladd
    //   94: lstore 11
    //   96: aload 10
    //   98: lload 11
    //   100: putfield 319	android/content/SyncStats:numAuthExceptions	J
    //   103: aload_0
    //   104: getfield 76	com/google/android/music/sync/common/AbstractSyncAdapter:mUseVerboseLogging	Z
    //   107: ifne +4 -> 111
    //   110: return
    //   111: aload_0
    //   112: getfield 45	com/google/android/music/sync/common/AbstractSyncAdapter:mTag	Ljava/lang/String;
    //   115: ldc_w 323
    //   118: invokestatic 155	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   121: return
    //   122: aload_1
    //   123: ldc_w 325
    //   126: iconst_0
    //   127: invokevirtual 331	android/os/Bundle:getBoolean	(Ljava/lang/String;Z)Z
    //   130: ifeq +72 -> 202
    //   133: aload 5
    //   135: astore 13
    //   137: aload_2
    //   138: astore 14
    //   140: aload 13
    //   142: aload 14
    //   144: invokestatic 337	android/content/ContentResolver:getIsSyncable	(Landroid/accounts/Account;Ljava/lang/String;)I
    //   147: ifge +55 -> 202
    //   150: aload_0
    //   151: getfield 80	com/google/android/music/sync/common/AbstractSyncAdapter:mActionOnInitialization	I
    //   154: bipush 255
    //   156: if_icmpeq +46 -> 202
    //   159: aload 5
    //   161: getfield 309	android/accounts/Account:name	Ljava/lang/String;
    //   164: ldc_w 339
    //   167: invokevirtual 345	java/lang/String:endsWith	(Ljava/lang/String;)Z
    //   170: ifeq +23 -> 193
    //   173: iconst_0
    //   174: istore 15
    //   176: aload 5
    //   178: astore 16
    //   180: aload_2
    //   181: astore 17
    //   183: aload 16
    //   185: aload 17
    //   187: iload 15
    //   189: invokestatic 349	android/content/ContentResolver:setIsSyncable	(Landroid/accounts/Account;Ljava/lang/String;I)V
    //   192: return
    //   193: aload_0
    //   194: getfield 80	com/google/android/music/sync/common/AbstractSyncAdapter:mActionOnInitialization	I
    //   197: istore 15
    //   199: goto -23 -> 176
    //   202: new 351	java/lang/Object
    //   205: dup
    //   206: invokespecial 352	java/lang/Object:<init>	()V
    //   209: astore 18
    //   211: aload_0
    //   212: getfield 62	com/google/android/music/sync/common/AbstractSyncAdapter:mContext	Landroid/content/Context;
    //   215: aload 18
    //   217: invokestatic 358	com/google/android/music/preferences/MusicPreferences:getMusicPreferences	(Landroid/content/Context;Ljava/lang/Object;)Lcom/google/android/music/preferences/MusicPreferences;
    //   220: astore 19
    //   222: aload 19
    //   224: invokevirtual 361	com/google/android/music/preferences/MusicPreferences:isValidAccount	()Z
    //   227: ifne +62 -> 289
    //   230: aload 19
    //   232: invokevirtual 364	com/google/android/music/preferences/MusicPreferences:getUpgradeSyncDone	()Z
    //   235: ifeq +271 -> 506
    //   238: aload_0
    //   239: getfield 62	com/google/android/music/sync/common/AbstractSyncAdapter:mContext	Landroid/content/Context;
    //   242: invokevirtual 370	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   245: ldc_w 372
    //   248: iconst_0
    //   249: invokestatic 377	com/google/android/gsf/Gservices:getBoolean	(Landroid/content/ContentResolver;Ljava/lang/String;Z)Z
    //   252: ifne +23 -> 275
    //   255: aload_1
    //   256: ldc_w 379
    //   259: invokevirtual 382	android/os/Bundle:containsKey	(Ljava/lang/String;)Z
    //   262: ifeq +175 -> 437
    //   265: aload_0
    //   266: getfield 45	com/google/android/music/sync/common/AbstractSyncAdapter:mTag	Ljava/lang/String;
    //   269: ldc_w 384
    //   272: invokestatic 387	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   275: aload_1
    //   276: ldc_w 389
    //   279: invokevirtual 382	android/os/Bundle:containsKey	(Ljava/lang/String;)Z
    //   282: istore 20
    //   284: iload 20
    //   286: ifne +167 -> 453
    //   289: aload 18
    //   291: invokestatic 393	com/google/android/music/preferences/MusicPreferences:releaseMusicPreferences	(Ljava/lang/Object;)V
    //   294: invokestatic 399	com/google/common/collect/Maps:newHashMap	()Ljava/util/HashMap;
    //   297: astore 21
    //   299: iconst_0
    //   300: istore 22
    //   302: aload_0
    //   303: getfield 62	com/google/android/music/sync/common/AbstractSyncAdapter:mContext	Landroid/content/Context;
    //   306: astore 23
    //   308: aload 5
    //   310: astore 24
    //   312: aload_1
    //   313: astore 25
    //   315: aload_0
    //   316: aload 24
    //   318: aload 23
    //   320: aload 21
    //   322: aload 25
    //   324: invokevirtual 403	com/google/android/music/sync/common/AbstractSyncAdapter:onSyncStart	(Landroid/accounts/Account;Landroid/content/Context;Ljava/util/Map;Landroid/os/Bundle;)V
    //   327: new 351	java/lang/Object
    //   330: dup
    //   331: invokespecial 352	java/lang/Object:<init>	()V
    //   334: astore 18
    //   336: aload_0
    //   337: getfield 62	com/google/android/music/sync/common/AbstractSyncAdapter:mContext	Landroid/content/Context;
    //   340: aload 18
    //   342: invokestatic 358	com/google/android/music/preferences/MusicPreferences:getMusicPreferences	(Landroid/content/Context;Ljava/lang/Object;)Lcom/google/android/music/preferences/MusicPreferences;
    //   345: astore 19
    //   347: aload_1
    //   348: ldc_w 404
    //   351: iconst_0
    //   352: invokevirtual 331	android/os/Bundle:getBoolean	(Ljava/lang/String;Z)Z
    //   355: ifeq +164 -> 519
    //   358: aload_0
    //   359: getfield 76	com/google/android/music/sync/common/AbstractSyncAdapter:mUseVerboseLogging	Z
    //   362: ifeq +13 -> 375
    //   365: aload_0
    //   366: getfield 45	com/google/android/music/sync/common/AbstractSyncAdapter:mTag	Ljava/lang/String;
    //   369: ldc_w 406
    //   372: invokestatic 155	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   375: aload 4
    //   377: astore 26
    //   379: aload_0
    //   380: aload 26
    //   382: aload 21
    //   384: invokespecial 408	com/google/android/music/sync/common/AbstractSyncAdapter:upload	(Landroid/content/SyncResult;Ljava/util/HashMap;)V
    //   387: iconst_1
    //   388: istore 22
    //   390: aload 19
    //   392: invokevirtual 364	com/google/android/music/preferences/MusicPreferences:getUpgradeSyncDone	()Z
    //   395: ifne +14 -> 409
    //   398: aload 19
    //   400: iconst_1
    //   401: invokevirtual 411	com/google/android/music/preferences/MusicPreferences:setUpgradeSyncDone	(Z)V
    //   404: aload 19
    //   406: invokevirtual 414	com/google/android/music/preferences/MusicPreferences:setLastUpgradeSyncVersion	()V
    //   409: aload 18
    //   411: invokestatic 393	com/google/android/music/preferences/MusicPreferences:releaseMusicPreferences	(Ljava/lang/Object;)V
    //   414: aload_0
    //   415: getfield 62	com/google/android/music/sync/common/AbstractSyncAdapter:mContext	Landroid/content/Context;
    //   418: astore 27
    //   420: aload 5
    //   422: astore 28
    //   424: aload_0
    //   425: aload 28
    //   427: aload 27
    //   429: aload 21
    //   431: iload 22
    //   433: invokevirtual 418	com/google/android/music/sync/common/AbstractSyncAdapter:onSyncEnd	(Landroid/accounts/Account;Landroid/content/Context;Ljava/util/Map;Z)V
    //   436: return
    //   437: aload_0
    //   438: getfield 45	com/google/android/music/sync/common/AbstractSyncAdapter:mTag	Ljava/lang/String;
    //   441: ldc_w 420
    //   444: invokestatic 387	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   447: aload 18
    //   449: invokestatic 393	com/google/android/music/preferences/MusicPreferences:releaseMusicPreferences	(Ljava/lang/Object;)V
    //   452: return
    //   453: aload_1
    //   454: ldc_w 389
    //   457: invokevirtual 424	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   460: astore 29
    //   462: ldc_w 426
    //   465: aload 29
    //   467: invokevirtual 430	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   470: ifne -181 -> 289
    //   473: aload_1
    //   474: ldc_w 432
    //   477: invokevirtual 382	android/os/Bundle:containsKey	(Ljava/lang/String;)Z
    //   480: ifne -191 -> 289
    //   483: aload_0
    //   484: getfield 45	com/google/android/music/sync/common/AbstractSyncAdapter:mTag	Ljava/lang/String;
    //   487: ldc_w 434
    //   490: invokestatic 387	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   493: goto -46 -> 447
    //   496: astore 30
    //   498: aload 18
    //   500: invokestatic 393	com/google/android/music/preferences/MusicPreferences:releaseMusicPreferences	(Ljava/lang/Object;)V
    //   503: aload 30
    //   505: athrow
    //   506: aload_0
    //   507: getfield 45	com/google/android/music/sync/common/AbstractSyncAdapter:mTag	Ljava/lang/String;
    //   510: ldc_w 436
    //   513: invokestatic 387	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   516: goto -227 -> 289
    //   519: aload 4
    //   521: astore 31
    //   523: aload 5
    //   525: astore 32
    //   527: aload_0
    //   528: aload 31
    //   530: aload 32
    //   532: aload 21
    //   534: invokespecial 438	com/google/android/music/sync/common/AbstractSyncAdapter:download	(Landroid/content/SyncResult;Landroid/accounts/Account;Ljava/util/HashMap;)V
    //   537: goto -162 -> 375
    //   540: astore 33
    //   542: aload 18
    //   544: invokestatic 393	com/google/android/music/preferences/MusicPreferences:releaseMusicPreferences	(Ljava/lang/Object;)V
    //   547: aload_0
    //   548: getfield 62	com/google/android/music/sync/common/AbstractSyncAdapter:mContext	Landroid/content/Context;
    //   551: astore 34
    //   553: aload 5
    //   555: astore 35
    //   557: aload_0
    //   558: aload 35
    //   560: aload 34
    //   562: aload 21
    //   564: iload 22
    //   566: invokevirtual 418	com/google/android/music/sync/common/AbstractSyncAdapter:onSyncEnd	(Landroid/accounts/Account;Landroid/content/Context;Ljava/util/Map;Z)V
    //   569: aload 33
    //   571: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   222	284	496	finally
    //   437	447	496	finally
    //   453	493	496	finally
    //   506	516	496	finally
    //   347	409	540	finally
    //   519	537	540	finally
  }

  protected abstract void onDownstreamComplete(Account paramAccount, HashMap<String, Object> paramHashMap)
    throws AuthenticatorException, HardSyncException, SoftSyncException;

  protected abstract void onDownstreamStart(Account paramAccount, HashMap<String, Object> paramHashMap)
    throws AuthenticatorException, HardSyncException, SoftSyncException;

  public void onPerformLoggedSync(Account paramAccount, Bundle paramBundle, String paramString, ContentProviderClient paramContentProviderClient, SyncResult paramSyncResult)
  {
    try
    {
      this.mSyncActive.set(true);
      AbstractSyncAdapter localAbstractSyncAdapter = this;
      Bundle localBundle = paramBundle;
      String str1 = paramString;
      ContentProviderClient localContentProviderClient = paramContentProviderClient;
      SyncResult localSyncResult = paramSyncResult;
      Account localAccount = paramAccount;
      localAbstractSyncAdapter.innerPerformSync(localBundle, str1, localContentProviderClient, localSyncResult, localAccount);
      localAtomicBoolean = this.mSyncActive;
      localAtomicBoolean.set(false);
      return;
    }
    catch (SoftSyncException localSoftSyncException)
    {
      while (true)
      {
        if (localSoftSyncException.getRetryAfter() != 0L)
        {
          long l1 = localSoftSyncException.getRetryAfter();
          paramSyncResult.delayUntil = l1;
        }
        SyncStats localSyncStats1 = paramSyncResult.stats;
        long l2 = localSyncStats1.numIoExceptions + 1L;
        localSyncStats1.numIoExceptions = l2;
        Log.i(this.mTag, "Sync failed due to soft error.");
        if (this.mUseVerboseLogging)
        {
          String str2 = this.mTag;
          String str3 = localSoftSyncException.getMessage();
          Log.v(str2, str3, localSoftSyncException);
        }
        localAtomicBoolean = this.mSyncActive;
      }
    }
    catch (HardSyncException localHardSyncException)
    {
      while (true)
      {
        SyncStats localSyncStats2 = paramSyncResult.stats;
        long l3 = localSyncStats2.numParseExceptions + 1L;
        localSyncStats2.numParseExceptions = l3;
        Log.i(this.mTag, "Sync failed due to a hard error.", localHardSyncException);
        localAtomicBoolean = this.mSyncActive;
      }
    }
    catch (AuthenticatorException localAuthenticatorException)
    {
      while (true)
      {
        SyncStats localSyncStats3 = paramSyncResult.stats;
        long l4 = localSyncStats3.numAuthExceptions + 1L;
        localSyncStats3.numAuthExceptions = l4;
        Log.w(this.mTag, "Sync failed due to an authentication issue.");
        if (this.mUseVerboseLogging)
        {
          String str4 = this.mTag;
          String str5 = localAuthenticatorException.getMessage();
          Log.v(str4, str5, localAuthenticatorException);
        }
        AtomicBoolean localAtomicBoolean = this.mSyncActive;
      }
    }
    finally
    {
      this.mSyncActive.set(false);
    }
  }

  protected abstract void onSyncEnd(Account paramAccount, Context paramContext, Map<String, Object> paramMap, boolean paramBoolean)
    throws AuthenticatorException, HardSyncException, SoftSyncException;

  protected abstract void onSyncStart(Account paramAccount, Context paramContext, Map<String, Object> paramMap, Bundle paramBundle)
    throws AuthenticatorException, HardSyncException, SoftSyncException;

  public static class UpstreamQueue extends ClosableBlockingQueue<QueueableSyncEntity>
  {
    public UpstreamQueue(int paramInt)
    {
      super();
    }
  }

  public static class DownstreamMergeQueue extends ClosableBlockingQueue<AbstractSyncAdapter.DownstreamMergeQueueEntry>
  {
    public DownstreamMergeQueue(int paramInt)
    {
      super();
    }
  }

  public static class DownstreamMergeQueueEntry extends Pair<QueueableSyncEntity, QueueableSyncEntity>
  {
    public DownstreamMergeQueueEntry(QueueableSyncEntity paramQueueableSyncEntity1, QueueableSyncEntity paramQueueableSyncEntity2)
    {
      super(paramQueueableSyncEntity2);
    }
  }

  public static class DownstreamFetchQueue extends ClosableBlockingQueue<QueueableSyncEntity>
  {
    public DownstreamFetchQueue(int paramInt)
    {
      super();
    }
  }

  public static abstract class Builder<T extends Builder<T, V>, V extends AbstractSyncAdapter>
  {
    private int mActionOnInitialization = 1;
    private AuthInfo mAuthInfo;
    private int mMaxDownstreamLoops = 50;
    private int mMaxQueueSize = 100;

    public V build(Context paramContext)
    {
      if (paramContext == null)
        throw new IllegalStateException("A context needs to be provided to the builder.");
      AbstractSyncAdapter localAbstractSyncAdapter = buildEmpty(paramContext);
      int i = this.mActionOnInitialization;
      int j = AbstractSyncAdapter.access$002(localAbstractSyncAdapter, i);
      int k = this.mMaxQueueSize;
      int m = AbstractSyncAdapter.access$102(localAbstractSyncAdapter, k);
      int n = this.mMaxDownstreamLoops;
      int i1 = AbstractSyncAdapter.access$202(localAbstractSyncAdapter, n);
      AuthInfo localAuthInfo = this.mAuthInfo;
      localAbstractSyncAdapter.mAuthInfo = localAuthInfo;
      return localAbstractSyncAdapter;
    }

    protected abstract V buildEmpty(Context paramContext);

    public Builder<T, V> setActionOnInitialization(int paramInt)
    {
      this.mActionOnInitialization = paramInt;
      return this;
    }

    public Builder<T, V> setAuthInfo(AuthInfo paramAuthInfo)
    {
      this.mAuthInfo = paramAuthInfo;
      return this;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.common.AbstractSyncAdapter
 * JD-Core Version:    0.6.2
 */