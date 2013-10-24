package com.google.android.music.download.artwork;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.text.TextUtils;
import com.google.android.music.download.DownloadUtils;
import com.google.android.music.download.cache.CacheUtils;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.log.Log;
import com.google.android.music.store.MusicContent.Albums;
import com.google.android.music.store.Store;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.FileSetProtector;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.PostFroyoUtils.EnvironmentCompat;
import com.google.android.music.utils.async.AsyncWorkers;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

public class ArtDownloadExecutor extends ThreadPoolExecutor
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private static final RejectedExecutionHandler sRejectionHandler = new RejectedExecutionHandler()
  {
    public void rejectedExecution(Runnable paramAnonymousRunnable, ThreadPoolExecutor paramAnonymousThreadPoolExecutor)
    {
    }
  };
  private File mAlbumsArtCacheDir;
  private final Context mContext;
  private Set<ArtDownloadTask> mCurrentDownloads;
  private final DownloadQueueCompleteListener mDownloadQueueCompleteListener;
  private List<ArtDownloadTask> mFutureDownloads;
  private File mNonAlbumArworkCacheDir;
  private final FileSetProtector mPendingAbsolutePaths;
  private int mRunningThreads;
  private final int mStorageType;

  public ArtDownloadExecutor(Context paramContext, DownloadQueueCompleteListener paramDownloadQueueCompleteListener)
  {
    super(5, 5, 3L, localTimeUnit, localLinkedBlockingQueue, localRejectedExecutionHandler);
    List localList = Collections.synchronizedList(new LinkedList());
    this.mFutureDownloads = localList;
    Set localSet = Collections.synchronizedSet(new HashSet(4));
    this.mCurrentDownloads = localSet;
    FileSetProtector localFileSetProtector = new FileSetProtector(5);
    this.mPendingAbsolutePaths = localFileSetProtector;
    this.mRunningThreads = 0;
    Context localContext = paramContext;
    this.mContext = localContext;
    DownloadQueueCompleteListener localDownloadQueueCompleteListener = paramDownloadQueueCompleteListener;
    this.mDownloadQueueCompleteListener = localDownloadQueueCompleteListener;
    int i = 0;
    if (!PostFroyoUtils.EnvironmentCompat.isExternalStorageEmulated())
    {
      File localFile1 = CacheUtils.getExternalAlbumArtworkCacheDirectory(this.mContext);
      this.mAlbumsArtCacheDir = localFile1;
      File localFile2 = CacheUtils.getExternalCacheDirectory(this.mContext, null);
      this.mNonAlbumArworkCacheDir = localFile2;
      i = 1;
    }
    if (this.mAlbumsArtCacheDir == null)
    {
      File localFile3 = CacheUtils.getInternalAlbumArtworkCacheDirectory(this.mContext);
      this.mAlbumsArtCacheDir = localFile3;
      File localFile4 = CacheUtils.getInternalCacheDirectory(this.mContext, null);
      this.mNonAlbumArworkCacheDir = localFile4;
      i = 0;
    }
    int j;
    if (i != 0)
      j = 2;
    while (true)
    {
      this.mStorageType = j;
      File[] arrayOfFile1 = new File[2];
      File localFile5 = this.mAlbumsArtCacheDir;
      arrayOfFile1[0] = localFile5;
      File localFile6 = this.mNonAlbumArworkCacheDir;
      arrayOfFile1[1] = localFile6;
      File[] arrayOfFile2 = arrayOfFile1;
      int k = arrayOfFile2.length;
      int m = 0;
      label248: int n = k;
      if (m >= n)
        return;
      File localFile7 = arrayOfFile2[m];
      if (!localFile7.exists())
        boolean bool1 = localFile7.mkdirs();
      try
      {
        boolean bool2 = new File(localFile7, ".nomedia").createNewFile();
        m += 1;
        break label248;
        j = 1;
      }
      catch (IOException localIOException)
      {
        while (true)
        {
          String str = localIOException.getMessage();
          Log.e("ArtDownloadExecutor", str, localIOException);
        }
      }
    }
  }

  /** @deprecated */
  private void afterExecute(Runnable paramRunnable, Throwable paramThrowable, boolean paramBoolean)
  {
    try
    {
      int i = this.mRunningThreads + -1;
      this.mRunningThreads = i;
      ArtDownloadTask localArtDownloadTask = (ArtDownloadTask)paramRunnable;
      if (paramThrowable != null);
      try
      {
        if (LOGV)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("Error running download thread: ");
          String str1 = paramThrowable.getMessage();
          String str2 = str1;
          Log.e("ArtDownloadExecutor", str2, paramThrowable);
        }
        if (localArtDownloadTask.mLocalLocationFullPath != null)
          boolean bool1 = localArtDownloadTask.mLocalLocationFullPath.delete();
        while (true)
        {
          boolean bool2 = this.mCurrentDownloads.remove(localArtDownloadTask);
          String str3 = localArtDownloadTask.getLocalLocationAbsolutePath();
          this.mPendingAbsolutePaths.unRegisterAbsolutePath(str3);
          if ((!paramBoolean) && (localArtDownloadTask.wasDownloadSuccessful()))
            localArtDownloadTask.notifyArtChanged();
          if ((this.mCurrentDownloads.isEmpty()) && (this.mFutureDownloads.isEmpty()) && (paramThrowable == null) && (localArtDownloadTask.wasDownloadSuccessful()))
          {
            LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
            Runnable local1 = new Runnable()
            {
              public void run()
              {
                ArtDownloadExecutor.this.notifyQueueComplete();
              }
            };
            AsyncWorkers.runAsync(localLoggableHandler, local1);
          }
          return;
          if ((localArtDownloadTask.mArtworkExists) && (localArtDownloadTask.wasDownloadSuccessful()))
          {
            DownloadQueueCompleteListener localDownloadQueueCompleteListener = this.mDownloadQueueCompleteListener;
            localArtDownloadTask.afterExecute(localDownloadQueueCompleteListener);
          }
        }
      }
      finally
      {
        boolean bool3 = this.mCurrentDownloads.remove(localArtDownloadTask);
        String str4 = localArtDownloadTask.getLocalLocationAbsolutePath();
        this.mPendingAbsolutePaths.unRegisterAbsolutePath(str4);
      }
    }
    finally
    {
    }
  }

  private boolean shouldIgnoreDownload(String paramString)
  {
    boolean bool = true;
    if (TextUtils.isEmpty(paramString));
    while (true)
    {
      return bool;
      Iterator localIterator = this.mCurrentDownloads.iterator();
      while (true)
        if (localIterator.hasNext())
        {
          String str1 = ((ArtDownloadTask)localIterator.next()).getTaskId();
          if (paramString.equals(str1))
            break;
        }
      localIterator = this.mFutureDownloads.iterator();
      while (true)
        if (localIterator.hasNext())
        {
          String str2 = ((ArtDownloadTask)localIterator.next()).getTaskId();
          if (paramString.equals(str2))
            break;
        }
      bool = false;
    }
  }

  // ERROR //
  public void addDownload(Long paramLong)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnull +21 -> 24
    //   6: aload_1
    //   7: invokevirtual 272	java/lang/Long:toString	()Ljava/lang/String;
    //   10: astore_2
    //   11: aload_0
    //   12: aload_2
    //   13: invokespecial 274	com/google/android/music/download/artwork/ArtDownloadExecutor:shouldIgnoreDownload	(Ljava/lang/String;)Z
    //   16: ifeq +13 -> 29
    //   19: aload_0
    //   20: monitorexit
    //   21: iconst_0
    //   22: astore_3
    //   23: return
    //   24: aconst_null
    //   25: astore_2
    //   26: goto -15 -> 11
    //   29: aload_0
    //   30: getfield 112	com/google/android/music/download/artwork/ArtDownloadExecutor:mContext	Landroid/content/Context;
    //   33: astore 4
    //   35: aload_0
    //   36: getfield 128	com/google/android/music/download/artwork/ArtDownloadExecutor:mAlbumsArtCacheDir	Ljava/io/File;
    //   39: astore 5
    //   41: aload_0
    //   42: getfield 142	com/google/android/music/download/artwork/ArtDownloadExecutor:mStorageType	I
    //   45: istore 6
    //   47: aload_1
    //   48: invokevirtual 278	java/lang/Long:longValue	()J
    //   51: lstore 7
    //   53: new 16	com/google/android/music/download/artwork/ArtDownloadExecutor$AlbumArtDownloadTask
    //   56: dup
    //   57: aload 4
    //   59: aload 5
    //   61: iload 6
    //   63: lload 7
    //   65: invokespecial 281	com/google/android/music/download/artwork/ArtDownloadExecutor$AlbumArtDownloadTask:<init>	(Landroid/content/Context;Ljava/io/File;IJ)V
    //   68: astore 9
    //   70: aload 9
    //   72: invokevirtual 284	com/google/android/music/download/artwork/ArtDownloadExecutor$ArtDownloadTask:initLocalLocation	()Ljava/lang/String;
    //   75: astore 10
    //   77: aload_0
    //   78: getfield 108	com/google/android/music/download/artwork/ArtDownloadExecutor:mPendingAbsolutePaths	Lcom/google/android/music/utils/FileSetProtector;
    //   81: aload 10
    //   83: invokevirtual 287	com/google/android/music/utils/FileSetProtector:registerAbsolutePath	(Ljava/lang/String;)V
    //   86: aload_0
    //   87: getfield 92	com/google/android/music/download/artwork/ArtDownloadExecutor:mFutureDownloads	Ljava/util/List;
    //   90: aload 9
    //   92: invokeinterface 290 2 0
    //   97: istore 11
    //   99: aload_0
    //   100: monitorexit
    //   101: aload_0
    //   102: aload 9
    //   104: invokevirtual 294	com/google/android/music/download/artwork/ArtDownloadExecutor:execute	(Ljava/lang/Runnable;)V
    //   107: return
    //   108: astore 12
    //   110: iconst_0
    //   111: astore 12
    //   113: aload_0
    //   114: monitorexit
    //   115: aload 12
    //   117: athrow
    //   118: astore 12
    //   120: goto -7 -> 113
    //
    // Exception table:
    //   from	to	target	type
    //   6	70	108	finally
    //   70	101	118	finally
    //   113	115	118	finally
  }

  // ERROR //
  public void addDownload(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokespecial 274	com/google/android/music/download/artwork/ArtDownloadExecutor:shouldIgnoreDownload	(Ljava/lang/String;)Z
    //   7: ifeq +6 -> 13
    //   10: aload_0
    //   11: monitorexit
    //   12: return
    //   13: aload_0
    //   14: getfield 112	com/google/android/music/download/artwork/ArtDownloadExecutor:mContext	Landroid/content/Context;
    //   17: astore_2
    //   18: aload_0
    //   19: getfield 134	com/google/android/music/download/artwork/ArtDownloadExecutor:mNonAlbumArworkCacheDir	Ljava/io/File;
    //   22: astore_3
    //   23: aload_0
    //   24: getfield 142	com/google/android/music/download/artwork/ArtDownloadExecutor:mStorageType	I
    //   27: istore 4
    //   29: new 13	com/google/android/music/download/artwork/ArtDownloadExecutor$CachedArtDownloadTask
    //   32: dup
    //   33: aload_2
    //   34: aload_3
    //   35: iload 4
    //   37: aload_1
    //   38: invokespecial 297	com/google/android/music/download/artwork/ArtDownloadExecutor$CachedArtDownloadTask:<init>	(Landroid/content/Context;Ljava/io/File;ILjava/lang/String;)V
    //   41: astore 5
    //   43: aload 5
    //   45: invokevirtual 284	com/google/android/music/download/artwork/ArtDownloadExecutor$ArtDownloadTask:initLocalLocation	()Ljava/lang/String;
    //   48: astore 6
    //   50: aload_0
    //   51: getfield 108	com/google/android/music/download/artwork/ArtDownloadExecutor:mPendingAbsolutePaths	Lcom/google/android/music/utils/FileSetProtector;
    //   54: aload 6
    //   56: invokevirtual 287	com/google/android/music/utils/FileSetProtector:registerAbsolutePath	(Ljava/lang/String;)V
    //   59: aload_0
    //   60: getfield 92	com/google/android/music/download/artwork/ArtDownloadExecutor:mFutureDownloads	Ljava/util/List;
    //   63: aload 5
    //   65: invokeinterface 290 2 0
    //   70: istore 7
    //   72: aload_0
    //   73: monitorexit
    //   74: aload_0
    //   75: aload 5
    //   77: invokevirtual 294	com/google/android/music/download/artwork/ArtDownloadExecutor:execute	(Ljava/lang/Runnable;)V
    //   80: aload 5
    //   82: astore 8
    //   84: return
    //   85: astore 9
    //   87: aload_0
    //   88: monitorexit
    //   89: aload 9
    //   91: athrow
    //   92: astore 9
    //   94: aload 5
    //   96: astore 10
    //   98: goto -11 -> 87
    //
    // Exception table:
    //   from	to	target	type
    //   2	43	85	finally
    //   87	89	85	finally
    //   43	74	92	finally
  }

  /** @deprecated */
  protected void afterExecute(Runnable paramRunnable, Throwable paramThrowable)
  {
    try
    {
      afterExecute(paramRunnable, paramThrowable, false);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  protected void beforeExecute(Thread paramThread, Runnable paramRunnable)
  {
    try
    {
      ArtDownloadTask localArtDownloadTask = (ArtDownloadTask)paramRunnable;
      boolean bool1 = this.mCurrentDownloads.add(localArtDownloadTask);
      boolean bool2 = this.mFutureDownloads.remove(localArtDownloadTask);
      int i = this.mRunningThreads + 1;
      this.mRunningThreads = i;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public boolean deleteFileIfPossible(String paramString)
  {
    return this.mPendingAbsolutePaths.deleteFileIfPossible(paramString);
  }

  /** @deprecated */
  public int getCurrentNumberOfDownloads()
  {
    try
    {
      int i = this.mCurrentDownloads.size();
      int j = i;
      return j;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  protected void notifyQueueComplete()
  {
    this.mDownloadQueueCompleteListener.onDownloadQueueCompleted();
  }

  public static abstract interface DownloadQueueCompleteListener
  {
    public abstract void onDownloadArtworkFile(File paramFile);

    public abstract void onDownloadQueueCompleted();
  }

  private static class CachedArtDownloadTask extends ArtDownloadExecutor.ArtDownloadTask
  {
    private static String SUB_FOLDER = "folder";
    private final File mRelativeCacheDirectory;
    private final String mRemoteUrl;

    public CachedArtDownloadTask(Context paramContext, File paramFile, int paramInt, String paramString)
    {
      super(paramFile, paramInt);
      this.mRemoteUrl = paramString;
      String str = SUB_FOLDER;
      File localFile = new File("artwork2", str);
      this.mRelativeCacheDirectory = localFile;
    }

    public void afterExecute(ArtDownloadExecutor.DownloadQueueCompleteListener paramDownloadQueueCompleteListener)
    {
      Store localStore = Store.getInstance(this.mContext);
      long l = this.mLocalLocationFullPath.length();
      String str1 = this.mRemoteUrl;
      String str2 = this.mLocalLocationRelativePath;
      int i = this.mStorageType;
      localStore.saveArtwork(str1, str2, i, l);
      super.afterExecute(paramDownloadQueueCompleteListener);
    }

    protected String getLocalLocationRelativePath()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      String str1 = String.valueOf(Store.generateId(this.mRemoteUrl));
      String str2 = str1 + ".jpg";
      File localFile = this.mRelativeCacheDirectory;
      return new File(localFile, str2).toString();
    }

    protected String getRemoteLocation()
    {
      return this.mRemoteUrl;
    }

    public String getTaskId()
    {
      return this.mRemoteUrl;
    }

    protected void logMissingRemoteLocation()
    {
      Log.w("ArtDownloadExecutor", "Empty remote url.");
    }

    public void notifyArtChanged()
    {
      Intent localIntent1 = new Intent("com.google.android.music.ArtChanged");
      String str = this.mRemoteUrl;
      Intent localIntent2 = localIntent1.putExtra("remoteUrl", str);
      this.mContext.sendBroadcast(localIntent1, "com.google.android.music.download.artwork.RECEIVE_BROADCAST_PERMISSION");
    }
  }

  private static class AlbumArtDownloadTask extends ArtDownloadExecutor.ArtDownloadTask
  {
    private final long mAlbumId;
    private final String mTaskId;

    public AlbumArtDownloadTask(Context paramContext, File paramFile, int paramInt, long paramLong)
    {
      super(paramFile, paramInt);
      this.mAlbumId = paramLong;
      String str = Long.toString(this.mAlbumId);
      this.mTaskId = str;
    }

    public void afterExecute(ArtDownloadExecutor.DownloadQueueCompleteListener paramDownloadQueueCompleteListener)
    {
      Store localStore = Store.getInstance(this.mContext);
      long l = this.mAlbumId;
      String str = this.mLocalLocationRelativePath;
      int i = this.mStorageType;
      localStore.saveArtwork(l, str, i);
      super.afterExecute(paramDownloadQueueCompleteListener);
    }

    protected String getLocalLocationRelativePath()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      String str = String.valueOf(this.mAlbumId);
      return str + ".jpg";
    }

    protected String getRemoteLocation()
    {
      Store localStore = Store.getInstance(this.mContext);
      long l = this.mAlbumId;
      return localStore.getRemoteArtLocationForAlbum(l);
    }

    public String getTaskId()
    {
      return this.mTaskId;
    }

    protected void logMissingRemoteLocation()
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Unknown albumId: ");
      long l = this.mAlbumId;
      String str = l;
      Log.w("ArtDownloadExecutor", str);
    }

    public void notifyArtChanged()
    {
      Intent localIntent1 = new Intent("com.google.android.music.AlbumArtChanged");
      Uri localUri = MusicContent.Albums.getAlbumsUri(this.mAlbumId);
      Intent localIntent2 = localIntent1.setData(localUri);
      long l = this.mAlbumId;
      Intent localIntent3 = localIntent1.putExtra("albumId", l);
      this.mContext.sendBroadcast(localIntent1, "com.google.android.music.download.artwork.RECEIVE_BROADCAST_PERMISSION");
    }
  }

  private static abstract class ArtDownloadTask
    implements Runnable
  {
    protected final boolean LOGV;
    private boolean mArtworkExists;
    private final File mCacheDir;
    protected final Context mContext;
    private boolean mDownloadSuccessful;
    private final MusicEventLogger mEventLogger;
    protected File mLocalLocationFullPath;
    protected String mLocalLocationRelativePath;
    protected final int mStorageType;

    public ArtDownloadTask(Context paramContext, File paramFile, int paramInt)
    {
      boolean bool = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
      this.LOGV = bool;
      this.mDownloadSuccessful = false;
      this.mArtworkExists = true;
      this.mContext = paramContext;
      this.mCacheDir = paramFile;
      this.mStorageType = paramInt;
      MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this.mContext);
      this.mEventLogger = localMusicEventLogger;
    }

    private void logHttpRequest(String paramString)
    {
      MusicEventLogger localMusicEventLogger = this.mEventLogger;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = "httpUrl";
      arrayOfObject[1] = paramString;
      localMusicEventLogger.trackEvent("artDownload", arrayOfObject);
    }

    private void streamFromCloud(String paramString)
      throws IOException
    {
      if (this.mLocalLocationFullPath.length() > 0L)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Local file already exists skipping it ");
        File localFile1 = this.mLocalLocationFullPath;
        String str1 = localFile1;
        Log.w("ArtDownloadTask", str1);
        this.mDownloadSuccessful = true;
        return;
      }
      String str2 = DownloadUtils.getUserAgent(this.mContext);
      Context localContext = this.mContext;
      AndroidHttpClient localAndroidHttpClient = AndroidHttpClient.newInstance(str2, localContext);
      if (this.LOGV)
        localAndroidHttpClient.enableCurlLogging("ArtDownloadTask-Curl", 2);
      HttpPost localHttpPost = new HttpPost(paramString);
      boolean bool1 = this.mLocalLocationFullPath.getParentFile().mkdirs();
      File localFile2 = this.mLocalLocationFullPath;
      FileOutputStream localFileOutputStream = new FileOutputStream(localFile2);
      InputStream localInputStream;
      try
      {
        logHttpRequest(paramString);
        localInputStream = localAndroidHttpClient.execute(localHttpPost).getEntity().getContent();
        byte[] arrayOfByte = new byte[8192];
        while (true)
        {
          int i = localInputStream.read(arrayOfByte);
          if (i == -1)
            break;
          localFileOutputStream.write(arrayOfByte, 0, i);
        }
      }
      finally
      {
        if (localInputStream == null);
      }
      try
      {
        localInputStream.close();
        try
        {
          label205: localFileOutputStream.flush();
          localFileOutputStream.getFD().sync();
          localFileOutputStream.close();
          localAndroidHttpClient.close();
          throw localObject;
          boolean bool2 = true;
          this.mDownloadSuccessful = bool2;
          if (localInputStream == null);
        }
        catch (IOException localIOException2)
        {
          try
          {
            localInputStream.close();
            try
            {
              label250: localFileOutputStream.flush();
              localFileOutputStream.getFD().sync();
              localFileOutputStream.close();
              localAndroidHttpClient.close();
              return;
            }
            catch (IOException localIOException1)
            {
              while (true)
                this.mDownloadSuccessful = false;
            }
            localIOException2 = localIOException2;
            this.mDownloadSuccessful = false;
          }
          catch (IOException localIOException3)
          {
            break label250;
          }
        }
      }
      catch (IOException localIOException4)
      {
        break label205;
      }
    }

    public void afterExecute(ArtDownloadExecutor.DownloadQueueCompleteListener paramDownloadQueueCompleteListener)
    {
      File localFile = this.mLocalLocationFullPath;
      paramDownloadQueueCompleteListener.onDownloadArtworkFile(localFile);
    }

    public String getLocalLocationAbsolutePath()
    {
      return this.mLocalLocationFullPath.getAbsolutePath();
    }

    protected abstract String getLocalLocationRelativePath();

    protected abstract String getRemoteLocation();

    public abstract String getTaskId();

    public String initLocalLocation()
    {
      String str1 = getLocalLocationRelativePath();
      this.mLocalLocationRelativePath = str1;
      File localFile1 = this.mCacheDir;
      String str2 = this.mLocalLocationRelativePath;
      File localFile2 = new File(localFile1, str2);
      this.mLocalLocationFullPath = localFile2;
      return getLocalLocationAbsolutePath();
    }

    protected abstract void logMissingRemoteLocation();

    public abstract void notifyArtChanged();

    public void run()
    {
      String str1 = getRemoteLocation();
      if ((str1 == null) || (str1.length() == 0))
      {
        this.mArtworkExists = false;
        logMissingRemoteLocation();
        return;
      }
      try
      {
        streamFromCloud(str1);
        return;
      }
      catch (IOException localIOException)
      {
        String str2 = localIOException.getMessage();
        Log.e("ArtDownloadTask", str2, localIOException);
      }
    }

    public boolean wasDownloadSuccessful()
    {
      return this.mDownloadSuccessful;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.artwork.ArtDownloadExecutor
 * JD-Core Version:    0.6.2
 */