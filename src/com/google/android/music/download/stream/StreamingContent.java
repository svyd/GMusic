package com.google.android.music.download.stream;

import android.content.Context;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.DownloadProgress;
import com.google.android.music.download.DownloadRequest;
import com.google.android.music.download.DownloadState.State;
import com.google.android.music.download.DownloadUtils;
import com.google.android.music.download.cache.CacheUtils;
import com.google.android.music.download.cache.FileLocation;
import com.google.android.music.download.cache.ICacheManager;
import com.google.android.music.download.cp.CpInputStream;
import com.google.android.music.download.cp.CpUtils;
import com.google.android.music.download.cp.UnrecognizedDataCpException;
import com.google.android.music.io.ChunkedInputStreamAdapter;
import com.google.android.music.log.Log;
import com.google.android.music.store.DataNotFoundException;
import com.google.android.music.store.MusicFile;
import com.google.android.music.store.Store;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.IOUtils;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.List;

public class StreamingContent
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private long mCompleted = 0L;
  private final Context mContext;
  private DownloadProgress mDownloadProgress;
  private final DownloadRequest mDownloadRequest;
  private long mExtraChunkSize = 0L;
  private String mFilepath;
  private long mLastWaitLog = 0L;
  private final long mSecureId;
  private volatile long mStartReadPoint = 0L;
  private boolean mWaitingContentTypeAllowed = false;

  public StreamingContent(Context paramContext, DownloadRequest paramDownloadRequest)
  {
    this.mContext = paramContext;
    this.mDownloadRequest = paramDownloadRequest;
    String str = paramDownloadRequest.getFileLocation().getFullPath().getAbsolutePath();
    this.mFilepath = str;
    long l = CpUtils.getRandom().nextLong() & 0xFFFFFFFF;
    this.mSecureId = l;
  }

  public static String contentListToString(String paramString, List<StreamingContent> paramList)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(paramString);
    StringBuilder localStringBuilder3 = localStringBuilder1.append("=[");
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      ContentIdentifier localContentIdentifier = ((StreamingContent)localIterator.next()).getId();
      StringBuilder localStringBuilder4 = localStringBuilder1.append(localContentIdentifier);
      StringBuilder localStringBuilder5 = localStringBuilder1.append(", ");
    }
    StringBuilder localStringBuilder6 = localStringBuilder1.append("]");
    return localStringBuilder1.toString();
  }

  public void clearFileIfNotSavable(ICacheManager paramICacheManager)
  {
    if (paramICacheManager == null)
    {
      Log.w("StreamingContent", "cacheManager is null");
      return;
    }
    int i = 0;
    try
    {
      if ((!this.mDownloadRequest.getId().isCacheable()) || ((this.mDownloadProgress != null) && (!this.mDownloadProgress.isSavable())))
        i = 1;
      if (i == 0)
        return;
      try
      {
        if (LOGV)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("clearFileIfNotSavable: ");
          String str1 = this.mDownloadRequest.getFileLocation().getFullPath().getAbsolutePath();
          String str2 = str1;
          Log.d("StreamingContent", str2);
        }
        DownloadRequest localDownloadRequest = this.mDownloadRequest;
        paramICacheManager.requestDelete(localDownloadRequest);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.e("StreamingContent", "Failed to call request delete", localRemoteException);
        return;
      }
    }
    finally
    {
    }
  }

  public Context getContext()
  {
    return this.mContext;
  }

  public DownloadRequest getDownloadRequest()
  {
    return this.mDownloadRequest;
  }

  public ContentIdentifier getId()
  {
    return this.mDownloadRequest.getId();
  }

  long getSecureId()
  {
    return this.mSecureId;
  }

  public long getStartReadPoint()
  {
    return this.mStartReadPoint;
  }

  /** @deprecated */
  public StreamingInput getStreamingInput(long paramLong)
    throws FileNotFoundException, IOException
  {
    while (true)
    {
      File localFile2;
      Object localObject1;
      byte[] arrayOfByte;
      try
      {
        if (this.mFilepath != null)
        {
          String str = this.mFilepath;
          File localFile1 = new File(str);
          localFile2 = localFile1;
          if (localFile2 == null)
          {
            localObject1 = null;
            return localObject1;
          }
        }
        else
        {
          localFile2 = this.mDownloadRequest.getFileLocation().getFullPath();
          continue;
        }
        arrayOfByte = null;
        ContentIdentifier localContentIdentifier = this.mDownloadRequest.getId();
        if (localContentIdentifier.isCacheable())
        {
          Store localStore = Store.getInstance(this.mContext);
          long l1 = localContentIdentifier.getId();
          arrayOfByte = localStore.getCpData(l1, false);
        }
        if (arrayOfByte == null)
        {
          RandomAccessFile localRandomAccessFile = new RandomAccessFile(localFile2, "r");
          localRandomAccessFile.seek(paramLong);
          localObject1 = new FileStreamingInput(localRandomAccessFile);
          continue;
        }
      }
      finally
      {
      }
      FileInputStream localFileInputStream = new FileInputStream(localFile2);
      try
      {
        CpInputStream localCpInputStream = new CpInputStream(localFileInputStream, arrayOfByte);
        ChunkedInputStreamAdapter localChunkedInputStreamAdapter = new ChunkedInputStreamAdapter(localCpInputStream);
        long l2 = localChunkedInputStreamAdapter.skip(paramLong);
        long l3 = localCpInputStream.getChunkSize();
        this.mExtraChunkSize = l3;
        localObject1 = new StreamingInputAdapter(localChunkedInputStreamAdapter);
      }
      catch (UnrecognizedDataCpException localUnrecognizedDataCpException)
      {
        Log.e("StreamingContent", "Invalid CP file", localUnrecognizedDataCpException);
      }
    }
    throw new FileNotFoundException("No valid cp file is found");
  }

  public boolean hasId(ContentIdentifier paramContentIdentifier)
  {
    return this.mDownloadRequest.getId().equals(paramContentIdentifier);
  }

  public boolean hasRequest(DownloadRequest paramDownloadRequest)
  {
    return this.mDownloadRequest.equals(paramDownloadRequest);
  }

  /** @deprecated */
  public boolean isCompleted()
  {
    boolean bool = false;
    try
    {
      Object localObject1 = this.mDownloadProgress;
      if (localObject1 == null);
      while (true)
      {
        return bool;
        localObject1 = this.mDownloadProgress.getState();
        DownloadState.State localState = DownloadState.State.COMPLETED;
        if (localObject1 == localState)
          bool = true;
      }
    }
    finally
    {
    }
  }

  /** @deprecated */
  public boolean isFinished()
  {
    try
    {
      DownloadProgress localDownloadProgress = this.mDownloadProgress;
      if (localDownloadProgress == null);
      boolean bool2;
      for (boolean bool1 = false; ; bool1 = bool2)
      {
        return bool1;
        bool2 = this.mDownloadProgress.getState().isFinished();
      }
    }
    finally
    {
    }
  }

  public boolean isMyProgress(DownloadProgress paramDownloadProgress)
  {
    return this.mDownloadRequest.isMyProgress(paramDownloadProgress);
  }

  /** @deprecated */
  // ERROR //
  public void notifyDownloadProgress(DownloadProgress paramDownloadProgress, ICacheManager paramICacheManager)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 58	com/google/android/music/download/stream/StreamingContent:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   6: aload_1
    //   7: invokevirtual 285	com/google/android/music/download/DownloadRequest:isMyProgress	(Lcom/google/android/music/download/DownloadProgress;)Z
    //   10: ifne +48 -> 58
    //   13: getstatic 38	com/google/android/music/download/stream/StreamingContent:LOGV	Z
    //   16: ifeq +39 -> 55
    //   19: iconst_2
    //   20: anewarray 4	java/lang/Object
    //   23: astore_3
    //   24: aload_0
    //   25: getfield 58	com/google/android/music/download/stream/StreamingContent:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   28: astore 4
    //   30: aload_3
    //   31: iconst_0
    //   32: aload 4
    //   34: aastore
    //   35: aload_3
    //   36: iconst_1
    //   37: aload_1
    //   38: aastore
    //   39: ldc_w 289
    //   42: aload_3
    //   43: invokestatic 295	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   46: astore 5
    //   48: ldc 141
    //   50: aload 5
    //   52: invokestatic 167	com/google/android/music/log/Log:d	(Ljava/lang/String;Ljava/lang/String;)V
    //   55: aload_0
    //   56: monitorexit
    //   57: return
    //   58: aload_0
    //   59: aload_1
    //   60: putfield 157	com/google/android/music/download/stream/StreamingContent:mDownloadProgress	Lcom/google/android/music/download/DownloadProgress;
    //   63: aload_1
    //   64: invokevirtual 298	com/google/android/music/download/DownloadProgress:getCompletedBytes	()J
    //   67: lstore 6
    //   69: aload_0
    //   70: lload 6
    //   72: putfield 46	com/google/android/music/download/stream/StreamingContent:mCompleted	J
    //   75: aload_0
    //   76: getfield 157	com/google/android/music/download/stream/StreamingContent:mDownloadProgress	Lcom/google/android/music/download/DownloadProgress;
    //   79: invokevirtual 162	com/google/android/music/download/DownloadProgress:isSavable	()Z
    //   82: istore 8
    //   84: iload 8
    //   86: ifeq +139 -> 225
    //   89: aconst_null
    //   90: astore 9
    //   92: aload_2
    //   93: ifnull +146 -> 239
    //   96: getstatic 38	com/google/android/music/download/stream/StreamingContent:LOGV	Z
    //   99: ifeq +43 -> 142
    //   102: iconst_2
    //   103: anewarray 4	java/lang/Object
    //   106: astore 10
    //   108: aload_0
    //   109: getfield 58	com/google/android/music/download/stream/StreamingContent:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   112: astore 11
    //   114: aload 10
    //   116: iconst_0
    //   117: aload 11
    //   119: aastore
    //   120: aload 10
    //   122: iconst_1
    //   123: aload_1
    //   124: aastore
    //   125: ldc_w 300
    //   128: aload 10
    //   130: invokestatic 295	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   133: astore 12
    //   135: ldc 141
    //   137: aload 12
    //   139: invokestatic 167	com/google/android/music/log/Log:d	(Ljava/lang/String;Ljava/lang/String;)V
    //   142: aload_0
    //   143: getfield 58	com/google/android/music/download/stream/StreamingContent:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   146: astore 13
    //   148: aload_0
    //   149: getfield 157	com/google/android/music/download/stream/StreamingContent:mDownloadProgress	Lcom/google/android/music/download/DownloadProgress;
    //   152: invokevirtual 303	com/google/android/music/download/DownloadProgress:getHttpContentType	()Ljava/lang/String;
    //   155: astore 14
    //   157: aload_0
    //   158: getfield 157	com/google/android/music/download/stream/StreamingContent:mDownloadProgress	Lcom/google/android/music/download/DownloadProgress;
    //   161: invokevirtual 306	com/google/android/music/download/DownloadProgress:getDownloadByteLength	()J
    //   164: lstore 15
    //   166: aload_2
    //   167: aload 13
    //   169: aload 14
    //   171: lload 15
    //   173: invokeinterface 310 5 0
    //   178: astore 9
    //   180: aload 9
    //   182: ifnonnull +82 -> 264
    //   185: new 98	java/lang/StringBuilder
    //   188: dup
    //   189: invokespecial 99	java/lang/StringBuilder:<init>	()V
    //   192: ldc_w 312
    //   195: invokevirtual 103	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   198: astore 17
    //   200: aload_0
    //   201: getfield 58	com/google/android/music/download/stream/StreamingContent:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   204: astore 18
    //   206: aload 17
    //   208: aload 18
    //   210: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   213: invokevirtual 135	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   216: astore 19
    //   218: ldc 141
    //   220: aload 19
    //   222: invokestatic 149	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;)V
    //   225: aload_0
    //   226: invokevirtual 315	java/lang/Object:notifyAll	()V
    //   229: goto -174 -> 55
    //   232: astore 20
    //   234: aload_0
    //   235: monitorexit
    //   236: aload 20
    //   238: athrow
    //   239: ldc 141
    //   241: ldc 143
    //   243: invokestatic 149	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;)V
    //   246: goto -66 -> 180
    //   249: astore 21
    //   251: ldc 141
    //   253: ldc_w 317
    //   256: aload 21
    //   258: invokestatic 179	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   261: goto -36 -> 225
    //   264: aload_0
    //   265: aload 9
    //   267: putfield 78	com/google/android/music/download/stream/StreamingContent:mFilepath	Ljava/lang/String;
    //   270: goto -45 -> 225
    //
    // Exception table:
    //   from	to	target	type
    //   2	55	232	finally
    //   58	84	232	finally
    //   96	225	232	finally
    //   225	229	232	finally
    //   239	246	232	finally
    //   251	261	232	finally
    //   264	270	232	finally
    //   96	225	249	android/os/RemoteException
    //   239	246	249	android/os/RemoteException
    //   264	270	249	android/os/RemoteException
  }

  /** @deprecated */
  public void setWaitingContentTypeAllowed(boolean paramBoolean)
  {
    try
    {
      if (LOGV)
      {
        Object[] arrayOfObject = new Object[2];
        Boolean localBoolean = Boolean.valueOf(paramBoolean);
        arrayOfObject[0] = localBoolean;
        DownloadRequest localDownloadRequest = this.mDownloadRequest;
        arrayOfObject[1] = localDownloadRequest;
        String str = String.format("setWaitingContentTypeAllowed: %s request=%s", arrayOfObject);
        Log.d("StreamingContent", str);
      }
      this.mWaitingContentTypeAllowed = paramBoolean;
      notifyAll();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  public boolean shouldFilter(String paramString)
  {
    try
    {
      String str = this.mFilepath;
      boolean bool1 = TextUtils.equals(paramString, str);
      boolean bool2 = bool1;
      return bool2;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public String toString()
  {
    return this.mDownloadRequest.toString();
  }

  /** @deprecated */
  public String waitForContentType()
    throws InterruptedException
  {
    while (true)
    {
      try
      {
        if (LOGV)
        {
          StringBuilder localStringBuilder1 = new StringBuilder().append("waitForContentType: ");
          DownloadRequest localDownloadRequest1 = this.mDownloadRequest;
          String str1 = localDownloadRequest1;
          Log.d("StreamingContent", str1);
        }
        if ((this.mDownloadProgress == null) || ((this.mDownloadProgress.getHttpContentType() == null) && (!isFinished())))
        {
          if (!this.mWaitingContentTypeAllowed)
          {
            if (LOGV)
            {
              Object[] arrayOfObject1 = new Object[1];
              DownloadRequest localDownloadRequest2 = this.mDownloadRequest;
              arrayOfObject1[0] = localDownloadRequest2;
              String str2 = String.format("waitForContentType: streaming not allowed request=%s", arrayOfObject1);
              Log.d("StreamingContent", str2);
            }
            str3 = null;
            return str3;
          }
          wait();
          continue;
        }
      }
      finally
      {
      }
      String str3 = this.mDownloadProgress.getHttpContentType();
      if (str3 == null)
      {
        DownloadState.State localState1 = this.mDownloadProgress.getState();
        DownloadState.State localState2 = DownloadState.State.COMPLETED;
        if (localState1 != localState2)
        {
          str3 = null;
        }
        else
        {
          MusicFile localMusicFile2;
          try
          {
            Store localStore = Store.getInstance(this.mContext);
            long l1 = this.mDownloadRequest.getId().getId();
            MusicFile localMusicFile1 = MusicFile.getSummaryMusicFile(localStore, null, l1);
            localMusicFile2 = localMusicFile1;
            if (localMusicFile2 != null)
              break label281;
            StringBuilder localStringBuilder2 = new StringBuilder().append("Failed to load music file for ");
            DownloadRequest localDownloadRequest3 = this.mDownloadRequest;
            String str4 = localDownloadRequest3;
            Log.e("StreamingContent", str4);
            str3 = null;
          }
          catch (DataNotFoundException localDataNotFoundException)
          {
            Log.w("StreamingContent", "Failed to load track data: ", localDataNotFoundException);
            str3 = null;
          }
          continue;
          label281: File localFile = CacheUtils.resolveMusicPath(this.mContext, localMusicFile2);
          String str5 = IOUtils.getFileExtension(localFile);
          if (str5 == null)
          {
            StringBuilder localStringBuilder3 = new StringBuilder().append("Failed to parse file extension for location: ");
            String str6 = localFile.getAbsolutePath();
            String str7 = str6;
            Log.e("StreamingContent", str7);
            str3 = null;
          }
          else
          {
            str3 = (String)DownloadUtils.ExtensionToMimeMap.get(str5);
            long l2 = localMusicFile2.getLocalCopySize();
            this.mCompleted = l2;
            String str8 = localFile.getAbsolutePath();
            this.mFilepath = str8;
            long l3 = this.mDownloadProgress.getSeekMs();
            if (l3 != 0L)
            {
              float f1 = (float)this.mCompleted;
              float f2 = (float)l3;
              float f3 = f1 * f2;
              float f4 = (float)localMusicFile2.getDurationInMilliSec();
              long l4 = ()(f3 / f4);
              this.mStartReadPoint = l4;
            }
            if (LOGV)
            {
              Object[] arrayOfObject2 = new Object[3];
              arrayOfObject2[0] = str3;
              Long localLong = Long.valueOf(this.mCompleted);
              arrayOfObject2[1] = localLong;
              String str9 = this.mFilepath;
              arrayOfObject2[2] = str9;
              String str10 = String.format("contentType=%s completed=%d fileName=%s", arrayOfObject2);
              Log.d("StreamingContent", str10);
            }
            if (LOGV)
            {
              Object[] arrayOfObject3 = new Object[2];
              arrayOfObject3[0] = str3;
              DownloadRequest localDownloadRequest4 = this.mDownloadRequest;
              arrayOfObject3[1] = localDownloadRequest4;
              String str11 = String.format("contentType: %s for request %s", arrayOfObject3);
              Log.d("StreamingContent", str11);
            }
          }
        }
      }
    }
  }

  /** @deprecated */
  public void waitForData(long paramLong)
    throws InterruptedException
  {
    label124: 
    try
    {
      if (!isFinished())
      {
        long l1 = this.mCompleted;
        long l2 = this.mExtraChunkSize + paramLong;
        if (l1 >= l2)
          break label124;
        long l3 = SystemClock.uptimeMillis();
        if ((LOGV) && (this.mLastWaitLog + 10000L < l3))
        {
          this.mLastWaitLog = l3;
          StringBuilder localStringBuilder = new StringBuilder().append("waiting for ").append(paramLong).append(" bytes in file: ");
          String str1 = this.mFilepath;
          String str2 = str1;
          Log.i("StreamingContent", str2);
        }
        wait();
      }
    }
    finally
    {
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.stream.StreamingContent
 * JD-Core Version:    0.6.2
 */