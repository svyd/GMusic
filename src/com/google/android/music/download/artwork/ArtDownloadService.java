package com.google.android.music.download.artwork;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.music.SharedPreferencesCompat;
import com.google.android.music.download.cache.CacheUtils;
import com.google.android.music.download.cache.CacheUtils.AlbumArtWorkCachePathResolver;
import com.google.android.music.download.cache.CacheUtils.ArtworkPathResolver;
import com.google.android.music.download.cache.CacheUtils.NonAlbumArtWorkCachePathResolver;
import com.google.android.music.download.cache.FileSystem;
import com.google.android.music.download.cache.FileSystemImpl;
import com.google.android.music.log.Log;
import com.google.android.music.net.INetworkMonitor;
import com.google.android.music.net.IStreamabilityChangeListener;
import com.google.android.music.net.IStreamabilityChangeListener.Stub;
import com.google.android.music.net.NetworkMonitorServiceConnection;
import com.google.android.music.store.Store;
import com.google.android.music.utils.DbUtils;
import com.google.android.music.utils.DbUtils.CursorHelper;
import com.google.android.music.utils.DbUtils.LongCursorHelper;
import com.google.android.music.utils.DbUtils.StringCursorHelper;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.async.AsyncWorkers;
import com.google.common.collect.Sets;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ArtDownloadService extends Service
  implements ArtDownloadExecutor.DownloadQueueCompleteListener
{
  static final String[] ALBUMS_ART_PROJECTION;
  static final String[] CACHED_ART_PROJECTION = arrayOfString2;
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private BroadcastReceiver mAlbumArtWatchedBroadcastResultReceiver;
  private ArtDownloadExecutor mArtExecutor = null;
  private final FileSystem mFileSystem;
  private NetworkMonitorServiceConnection mNetworkMonitorServiceConnection;
  private Thread mOrphanedFilesScavenger;
  private SharedPreferences mPreferences;
  private final IStreamabilityChangeListener mStreamabilityChangeListener;

  static
  {
    String[] arrayOfString1 = new String[3];
    arrayOfString1[0] = "AlbumId";
    arrayOfString1[1] = "LocalLocation";
    arrayOfString1[2] = "LocalLocationStorageType";
    ALBUMS_ART_PROJECTION = arrayOfString1;
    String[] arrayOfString2 = new String[3];
    arrayOfString2[0] = "RemoteLocation";
    arrayOfString2[1] = "LocalLocation";
    arrayOfString2[2] = "LocalLocationStorageType";
  }

  public ArtDownloadService()
  {
    IStreamabilityChangeListener.Stub local6 = new IStreamabilityChangeListener.Stub()
    {
      public void onStreamabilityChanged(boolean paramAnonymousBoolean)
        throws RemoteException
      {
        if (paramAnonymousBoolean)
        {
          LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
          Runnable local1 = new Runnable()
          {
            public void run()
            {
              ArtDownloadService.this.onDownloadQueueCompleted();
            }
          };
          AsyncWorkers.runAsync(localLoggableHandler, local1);
          return;
        }
        ArtDownloadService.this.shutdownArtExecutorNow();
      }
    };
    this.mStreamabilityChangeListener = local6;
    FileSystemImpl localFileSystemImpl = new FileSystemImpl(null);
    this.mFileSystem = localFileSystemImpl;
  }

  private void adjustCacheSize(long paramLong)
  {
    synchronized (this.mPreferences)
    {
      long l = this.mPreferences.getLong("AlbumArtCacheSize", 0L) + paramLong;
      SharedPreferences.Editor localEditor1 = this.mPreferences.edit();
      SharedPreferences.Editor localEditor2 = localEditor1;
      try
      {
        SharedPreferences.Editor localEditor3 = localEditor2.putLong("AlbumArtCacheSize", l);
        SharedPreferencesCompat.apply(localEditor2);
        return;
      }
      finally
      {
        localObject1 = finally;
        SharedPreferencesCompat.apply(localEditor2);
        throw localObject1;
      }
    }
  }

  private static <ArtId> long clearOrphanedArtFiles(ArtDownloadService paramArtDownloadService, String paramString1, String[] paramArrayOfString, String paramString2, DbUtils.CursorHelper<ArtId> paramCursorHelper, File paramFile1, File paramFile2, CacheUtils.ArtworkPathResolver paramArtworkPathResolver)
  {
    Store localStore1 = Store.getInstance(paramArtDownloadService);
    SQLiteDatabase localSQLiteDatabase1 = localStore1.beginRead();
    SQLiteDatabase localSQLiteDatabase2 = null;
    long l1 = 0L;
    boolean bool1 = false;
    String str1 = paramString1;
    String[] arrayOfString = paramArrayOfString;
    label425: 
    while (true)
    {
      int j;
      long l2;
      HashMap localHashMap;
      HashSet localHashSet1;
      Object localObject2;
      String str2;
      File localFile1;
      try
      {
        int i = localSQLiteDatabase1.query(str1, arrayOfString, null, null, null, null, null);
        j = i;
        if (j == 0)
        {
          l2 = 65535L;
          if (localSQLiteDatabase1 != null)
            localStore1.endRead(localSQLiteDatabase1);
          if (0 != 0)
          {
            Store localStore2 = localStore1;
            SQLiteDatabase localSQLiteDatabase3 = null;
            boolean bool3 = false;
            localStore2.endWriteTxn(localSQLiteDatabase3, bool3);
          }
          return l2;
        }
        int k = j.getCount();
        localHashMap = new HashMap(k);
        localHashSet1 = new HashSet();
        try
        {
          if (!j.moveToNext())
            break label425;
          boolean bool2 = Thread.interrupted();
          if (bool2)
          {
            l2 = 65535L;
            Store.safeClose(j);
            if (localSQLiteDatabase1 != null)
              localStore1.endRead(localSQLiteDatabase1);
            if (0 == 0)
              continue;
            Store localStore3 = localStore1;
            SQLiteDatabase localSQLiteDatabase4 = null;
            boolean bool4 = false;
            localStore3.endWriteTxn(localSQLiteDatabase4, bool4);
            continue;
          }
          str1 = null;
          DbUtils.CursorHelper<ArtId> localCursorHelper1 = paramCursorHelper;
          int m = j;
          localObject2 = localCursorHelper1.getValue(m, str1);
          str2 = j.getString(1);
          int n = j.getInt(2);
          CacheUtils.ArtworkPathResolver localArtworkPathResolver = paramArtworkPathResolver;
          String str3 = str2;
          int i1 = n;
          localFile1 = localArtworkPathResolver.resolveArtworkPath(str3, i1);
          if (localFile1 == null)
            break label374;
          if (!localFile1.exists())
          {
            boolean bool5 = localHashSet1.add(localObject2);
            continue;
          }
        }
        finally
        {
          Store.safeClose(j);
        }
      }
      finally
      {
        if (localSQLiteDatabase1 != null)
          localStore1.endRead(localSQLiteDatabase1);
        if (localSQLiteDatabase2 != null)
        {
          Store localStore4 = localStore1;
          SQLiteDatabase localSQLiteDatabase5 = localSQLiteDatabase2;
          boolean bool6 = false;
          localStore4.endWriteTxn(localSQLiteDatabase5, bool6);
        }
      }
      long l3 = localFile1.length();
      l1 += l3;
      String str4 = localFile1.getAbsolutePath();
      Object localObject4 = localHashMap.put(str4, localObject2);
      continue;
      label374: if (LOGV)
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("Ignoring path: ");
        String str5 = str2;
        String str6 = str5 + " from validation as the storage is missing";
        Log.i("ArtDownloadService", str6);
        continue;
        Store.safeClose(j);
        localStore1.endRead(localSQLiteDatabase1);
        localSQLiteDatabase1 = null;
        ArtDownloadService localArtDownloadService1 = paramArtDownloadService;
        File localFile2 = paramFile1;
        validateLocalFiles(localArtDownloadService1, localHashMap, localFile2);
        if ((paramFile2 != null) && ((paramFile1 == null) || (paramFile1.compareTo(paramFile2) != 0)))
        {
          ArtDownloadService localArtDownloadService2 = paramArtDownloadService;
          File localFile3 = paramFile2;
          validateLocalFiles(localArtDownloadService2, localHashMap, localFile3);
        }
        if ((!localHashSet1.isEmpty()) || (!localHashMap.isEmpty()))
        {
          Collection localCollection = localHashMap.values();
          boolean bool7 = localHashSet1.addAll(localCollection);
          StringBuilder localStringBuilder2 = new StringBuilder();
          StringBuilder localStringBuilder3 = localStringBuilder2;
          String str7 = paramString2;
          StringBuilder localStringBuilder4 = localStringBuilder3.append(str7);
          DbUtils.CursorHelper<ArtId> localCursorHelper2 = paramCursorHelper;
          StringBuilder localStringBuilder5 = localStringBuilder2;
          HashSet localHashSet2 = localHashSet1;
          localCursorHelper2.appendIN(localStringBuilder5, localHashSet2);
          localSQLiteDatabase2 = localStore1.beginWriteTxn();
          String str8 = localStringBuilder2.toString();
          SQLiteDatabase localSQLiteDatabase6 = localSQLiteDatabase2;
          String str9 = paramString1;
          int i2 = localSQLiteDatabase6.delete(str9, str8, null);
          bool1 = true;
        }
        if (localSQLiteDatabase1 != null)
          localStore1.endRead(localSQLiteDatabase1);
        if (localSQLiteDatabase2 != null)
        {
          Store localStore5 = localStore1;
          SQLiteDatabase localSQLiteDatabase7 = localSQLiteDatabase2;
          boolean bool8 = bool1;
          localStore5.endWriteTxn(localSQLiteDatabase7, bool8);
        }
        l2 = l1;
      }
    }
  }

  private void clearOrphanedFiles()
  {
    String[] arrayOfString1 = ALBUMS_ART_PROJECTION;
    DbUtils.LongCursorHelper localLongCursorHelper = new DbUtils.LongCursorHelper();
    File localFile1 = CacheUtils.getExternalAlbumArtworkCacheDirectory(this);
    File localFile2 = CacheUtils.getInternalAlbumArtworkCacheDirectory(this);
    CacheUtils.AlbumArtWorkCachePathResolver localAlbumArtWorkCachePathResolver = new CacheUtils.AlbumArtWorkCachePathResolver(this);
    long l1 = clearOrphanedArtFiles(this, "ARTWORK", arrayOfString1, "AlbumId", localLongCursorHelper, localFile1, localFile2, localAlbumArtWorkCachePathResolver);
    String[] arrayOfString2 = CACHED_ART_PROJECTION;
    DbUtils.StringCursorHelper localStringCursorHelper = new DbUtils.StringCursorHelper();
    File localFile3 = CacheUtils.getExternalCacheDirectory(this, "artwork2");
    File localFile4 = CacheUtils.getInternalCacheDirectory(this, "artwork2");
    CacheUtils.NonAlbumArtWorkCachePathResolver localNonAlbumArtWorkCachePathResolver = new CacheUtils.NonAlbumArtWorkCachePathResolver(this);
    long l2 = clearOrphanedArtFiles(this, "ARTWORK_CACHE", arrayOfString2, "RemoteLocation", localStringCursorHelper, localFile3, localFile4, localNonAlbumArtWorkCachePathResolver);
    long l3 = l1 + l2;
    setCacheSize(l3);
  }

  private void clearOrphanedFilesAndStartAsyncDownloads()
  {
    clearOrphanedFiles();
    startAsyncDownloads();
  }

  private void clearOrphanedFilesAsync()
  {
    destroyOrphanedFilesScavenger();
    Thread local2 = new Thread("OrphanedFilesScavenger")
    {
      public void run()
      {
        setPriority(1);
        ArtDownloadService.this.clearOrphanedFilesAndStartAsyncDownloads();
      }
    };
    this.mOrphanedFilesScavenger = local2;
    this.mOrphanedFilesScavenger.start();
  }

  public static long compareFreeDiskSpaceToTarget(FileSystem paramFileSystem, File paramFile, int paramInt)
  {
    long l1 = paramFileSystem.getFreeSpace(paramFile);
    long l2 = paramFileSystem.getTotalSpace(paramFile);
    long l3 = paramInt * l2 / 100L;
    return l1 - l3;
  }

  private static <ArtId> long deleteCachedArtFile(ArtDownloadService paramArtDownloadService, Cursor paramCursor, Set<ArtId> paramSet, CacheUtils.ArtworkPathResolver paramArtworkPathResolver, DbUtils.CursorHelper<ArtId> paramCursorHelper)
  {
    long l;
    if ((paramCursor == null) || (paramCursor.isAfterLast()))
      l = 0L;
    while (true)
    {
      return l;
      Object localObject = paramCursorHelper.getValue(paramCursor, 0);
      String str1 = paramCursor.getString(1);
      int i = paramCursor.getInt(2);
      File localFile = paramArtworkPathResolver.resolveArtworkPath(str1, i);
      if ((localFile != null) && (localFile.exists()))
      {
        l = localFile.length();
        String str2 = localFile.getAbsolutePath();
        if (paramArtDownloadService.deleteFileIfPossible(str2))
          boolean bool = paramSet.add(localObject);
      }
      else
      {
        l = 0L;
      }
    }
  }

  private boolean deleteFileIfPossible(String paramString)
  {
    try
    {
      boolean bool;
      if (this.mArtExecutor != null)
        bool = this.mArtExecutor.deleteFileIfPossible(paramString);
      while (true)
      {
        return bool;
        bool = new File(paramString).delete();
      }
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private void destroyOrphanedFilesScavenger()
  {
    if (this.mOrphanedFilesScavenger == null)
      return;
    this.mOrphanedFilesScavenger.interrupt();
    this.mOrphanedFilesScavenger = null;
  }

  /** @deprecated */
  private boolean ensureEnoughDiskSpace()
  {
    try
    {
      long l1 = getCacheSize();
      long l2 = percentageToBytesDiskSpace(5);
      long l3 = l2;
      boolean bool2;
      if (l1 <= l3)
      {
        bool2 = true;
        return bool2;
      }
      long l4 = null;
      long l5 = l3 - 1048576L;
      l3 = Math.max(l4, l5);
      Store localStore1 = Store.getInstance(this);
      int i = localStore1.beginRead();
      int k = i;
      SQLiteDatabase localSQLiteDatabase1 = null;
      boolean bool3 = false;
      try
      {
        String[] arrayOfString1 = ALBUMS_ART_PROJECTION;
        Cursor localCursor1 = k.query("ARTWORK", arrayOfString1, null, null, null, null, null);
        String[] arrayOfString2 = CACHED_ART_PROJECTION;
        localCursor2 = k.query("ARTWORK_CACHE", arrayOfString2, null, null, null, null, "Timestamp ASC");
        if (localCursor1 != null)
        {
          n = 1;
          if (localCursor2 == null)
            break label597;
          i1 = 1;
          if ((n != 0) || (i1 != 0))
          {
            localHashSet1 = new java/util/HashSet;
            if (n == 0)
              break label603;
            j = n.getCount();
            localHashSet1.<init>(j);
            localHashSet2 = new java/util/HashSet;
            if (i1 == 0)
              break label609;
            j = localCursor2.getCount();
            localHashSet2.<init>(j);
            ArtDownloadService localArtDownloadService1 = this;
            localAlbumArtWorkCachePathResolver = new CacheUtils.AlbumArtWorkCachePathResolver(localArtDownloadService1);
            ArtDownloadService localArtDownloadService2 = this;
            localNonAlbumArtWorkCachePathResolver = new CacheUtils.NonAlbumArtWorkCachePathResolver(localArtDownloadService2);
            localLongCursorHelper1 = new DbUtils.LongCursorHelper();
            localStringCursorHelper1 = new DbUtils.StringCursorHelper();
          }
        }
      }
      finally
      {
        int m;
        try
        {
          while (true)
          {
            int i1;
            HashSet localHashSet1;
            int j;
            HashSet localHashSet2;
            CacheUtils.AlbumArtWorkCachePathResolver localAlbumArtWorkCachePathResolver;
            CacheUtils.NonAlbumArtWorkCachePathResolver localNonAlbumArtWorkCachePathResolver;
            DbUtils.LongCursorHelper localLongCursorHelper1;
            DbUtils.StringCursorHelper localStringCursorHelper1;
            boolean bool4 = DbUtils.moveToNext(n);
            boolean bool5 = DbUtils.moveToNext(localCursor2);
            ArtDownloadService localArtDownloadService3 = this;
            Object localObject3 = n;
            HashSet localHashSet3 = localHashSet1;
            DbUtils.LongCursorHelper localLongCursorHelper2 = localLongCursorHelper1;
            long l6 = deleteCachedArtFile(localArtDownloadService3, localObject3, localHashSet3, localAlbumArtWorkCachePathResolver, localLongCursorHelper2);
            long l7 = l1 - l6;
            ArtDownloadService localArtDownloadService4 = this;
            Cursor localCursor3 = localCursor2;
            HashSet localHashSet4 = localHashSet2;
            DbUtils.StringCursorHelper localStringCursorHelper2 = localStringCursorHelper1;
            long l8 = deleteCachedArtFile(localArtDownloadService4, localCursor3, localHashSet4, localNonAlbumArtWorkCachePathResolver, localStringCursorHelper2);
            l1 = l7 - l8;
            boolean bool1;
            if (!DbUtils.hasMore(n))
            {
              bool1 = DbUtils.hasMore(localCursor2);
              if (!bool1)
                break label615;
            }
            i2 = 1;
            if ((i2 == 0) || (l1 <= l3))
            {
              Store.safeClose(n);
              Store.safeClose(localCursor2);
              setCacheSize(l1);
              localStore1.endRead(k);
              m = 0;
              if ((!localHashSet1.isEmpty()) || (!localHashSet2.isEmpty()))
              {
                localSQLiteDatabase1 = localStore1.beginWriteTxn();
                if (!localHashSet1.isEmpty())
                {
                  StringBuilder localStringBuilder1 = new StringBuilder();
                  StringBuilder localStringBuilder2 = localStringBuilder1.append("AlbumId");
                  StringBuilder localStringBuilder3 = localStringBuilder1;
                  HashSet localHashSet5 = localHashSet1;
                  DbUtils.appendIN(localStringBuilder3, localHashSet5);
                  String str1 = localStringBuilder1.toString();
                  int i3 = localSQLiteDatabase1.delete("ARTWORK", str1, null);
                }
                if (!localHashSet2.isEmpty())
                {
                  StringBuilder localStringBuilder4 = new StringBuilder();
                  StringBuilder localStringBuilder5 = localStringBuilder4.append("RemoteLocation");
                  StringBuilder localStringBuilder6 = localStringBuilder4;
                  HashSet localHashSet6 = localHashSet2;
                  DbUtils.stringAppendIN(localStringBuilder6, localHashSet6);
                  String str2 = localStringBuilder4.toString();
                  int i4 = localSQLiteDatabase1.delete("ARTWORK_CACHE", str2, null);
                }
                bool3 = true;
              }
              if (m != 0)
                localStore1.endRead(m);
              if (localSQLiteDatabase1 != null)
              {
                Store localStore2 = localStore1;
                SQLiteDatabase localSQLiteDatabase2 = localSQLiteDatabase1;
                boolean bool6 = bool3;
                localStore2.endWriteTxn(localSQLiteDatabase2, bool6);
              }
              bool1 = isEnoughDiskSpace();
              bool2 = bool1;
              break;
              n = 0;
              continue;
              label597: i1 = 0;
              continue;
              label603: j = 0;
              continue;
              label609: j = 0;
            }
          }
          label615: int i2 = 0;
        }
        finally
        {
          Cursor localCursor2;
          int n;
          Store.safeClose(n);
          Store.safeClose(localCursor2);
          setCacheSize(l1);
        }
        if (localSQLiteDatabase1 != null)
        {
          Store localStore3 = localStore1;
          SQLiteDatabase localSQLiteDatabase3 = localSQLiteDatabase1;
          boolean bool7 = bool3;
          localStore3.endWriteTxn(localSQLiteDatabase3, bool7);
        }
      }
    }
    finally
    {
    }
  }

  public static long getCacheLimit(Context paramContext)
  {
    FileSystemImpl localFileSystemImpl = new FileSystemImpl(null);
    return percentageToBytesDiskSpace(paramContext, localFileSystemImpl, 5);
  }

  private static File getCacheRootDir(Context paramContext)
  {
    File localFile = paramContext.getExternalCacheDir();
    if (localFile == null)
      localFile = paramContext.getCacheDir();
    return localFile;
  }

  private long getCacheSize()
  {
    return this.mPreferences.getLong("AlbumArtCacheSize", 0L);
  }

  private boolean isCacheFull(boolean paramBoolean)
  {
    long l1 = getCacheSize();
    long l2 = percentageToBytesDiskSpace(5);
    if (!paramBoolean)
    {
      long l3 = l2 - 2097152L;
      l2 = Math.max(0L, l3);
    }
    if (l1 > l2);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private boolean isEnoughDiskSpace()
  {
    FileSystem localFileSystem = this.mFileSystem;
    File localFile = getCacheRootDir(this);
    if (compareFreeDiskSpaceToTarget(localFileSystem, localFile, 10) > 0L);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private void onWatchListReceived(Set<Long> paramSet, Set<String> paramSet1)
  {
    int i = 1;
    INetworkMonitor localINetworkMonitor = this.mNetworkMonitorServiceConnection.getNetworkMonitor();
    if (localINetworkMonitor == null)
    {
      NetworkMonitorServiceConnection localNetworkMonitorServiceConnection = this.mNetworkMonitorServiceConnection;
      Runnable local5 = new Runnable()
      {
        public void run()
        {
          ArtDownloadService.this.onDownloadQueueCompleted();
        }
      };
      localNetworkMonitorServiceConnection.addRunOnServiceConnected(local5);
      return;
    }
    try
    {
      if (!localINetworkMonitor.isStreamingAvailable())
      {
        stopSelf();
        return;
      }
    }
    catch (RemoteException localRemoteException1)
    {
      Log.w("ArtDownloadService", "onWatchListReceived: Remote call to NetworkMonitor failed");
      return;
    }
    while (true)
    {
      int j;
      try
      {
        if (this.mArtExecutor == null)
        {
          ArtDownloadExecutor localArtDownloadExecutor1 = new ArtDownloadExecutor(this, this);
          this.mArtExecutor = localArtDownloadExecutor1;
          if ((paramSet == null) || (paramSet.size() <= 0))
            break label173;
          j = 1;
          if ((paramSet1 == null) || (paramSet1.size() <= 0))
            break label179;
          if (((j == 0) && (i == 0)) || (isDownloadSpaceAvailable(true)))
            break;
          Log.e("ArtDownloadService", "Not enough free space for watched art, stopping service.");
          stopSelf();
          return;
        }
      }
      finally
      {
      }
      if (this.mArtExecutor.getCurrentNumberOfDownloads() != 0)
      {
        return;
        label173: j = 0;
        continue;
        label179: i = 0;
      }
    }
    int k = 0;
    if ((paramSet != null) && (paramSet.size() != 0))
    {
      Cursor localCursor1 = Store.getInstance(this).getAlbumsRequiringArtworkDownload(paramSet);
      localCursor3 = localCursor1;
    }
    while (true)
    {
      if (localCursor3 != null);
      try
      {
        if (localCursor3.moveToNext())
        {
          ArtDownloadExecutor localArtDownloadExecutor2 = this.mArtExecutor;
          Long localLong1 = Long.valueOf(localCursor3.getLong(0));
          localArtDownloadExecutor2.addDownload(localLong1);
          k = 1;
        }
        else
        {
          Store.safeClose(localCursor3);
          if (paramSet1 != null)
          {
            Iterator localIterator = paramSet1.iterator();
            if (localIterator.hasNext())
            {
              String str = (String)localIterator.next();
              this.mArtExecutor.addDownload(str);
              k = 1;
            }
          }
        }
      }
      finally
      {
        Store.safeClose(localCursor3);
      }
    }
    return;
    if (localINetworkMonitor != null);
    try
    {
      if (localINetworkMonitor.hasHighSpeedConnection())
      {
        boolean bool = localINetworkMonitor.isConnected();
        if (bool);
      }
      else
      {
        return;
      }
    }
    catch (RemoteException localRemoteException2)
    {
      return;
    }
    Cursor localCursor2 = Store.getInstance(this).getAlbumsRequiringArtworkDownload(null);
    Cursor localCursor3 = localCursor2;
    int m = 0;
    while (true)
    {
      if (localCursor3 != null);
      try
      {
        if (localCursor3.moveToNext())
        {
          if (!isDownloadSpaceAvailable(false))
          {
            Log.e("ArtDownloadService", "Not enough free space for prefetched art stopping service.");
            stopSelf();
            Store.safeClose(localCursor3);
            return;
          }
          ArtDownloadExecutor localArtDownloadExecutor3 = this.mArtExecutor;
          Long localLong2 = Long.valueOf(localCursor3.getLong(0));
          localArtDownloadExecutor3.addDownload(localLong2);
          m = 1;
          continue;
        }
        if (m == 0)
          stopSelf();
        Store.safeClose(localCursor3);
        return;
      }
      finally
      {
        Store.safeClose(localCursor3);
      }
    }
  }

  private long percentageToBytesDiskSpace(int paramInt)
  {
    FileSystem localFileSystem = this.mFileSystem;
    return percentageToBytesDiskSpace(this, localFileSystem, paramInt);
  }

  private static long percentageToBytesDiskSpace(Context paramContext, FileSystem paramFileSystem, int paramInt)
  {
    File localFile = getCacheRootDir(paramContext);
    long l = paramFileSystem.getTotalSpace(localFile);
    return paramInt * l / 100L;
  }

  private void queueAlbumArtRequestImp(Long paramLong, String paramString)
  {
    try
    {
      if (this.mArtExecutor == null)
      {
        ArtDownloadExecutor localArtDownloadExecutor = new ArtDownloadExecutor(this, this);
        this.mArtExecutor = localArtDownloadExecutor;
      }
      this.mArtExecutor.addDownload(paramLong);
      this.mArtExecutor.addDownload(paramString);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private void setCacheSize(long paramLong)
  {
    String str = "Setting cache size " + paramLong;
    Log.w("ArtDownloadService", str);
    synchronized (this.mPreferences)
    {
      SharedPreferences.Editor localEditor1 = this.mPreferences.edit();
      SharedPreferences.Editor localEditor2 = localEditor1;
      try
      {
        SharedPreferences.Editor localEditor3 = localEditor2.putLong("AlbumArtCacheSize", paramLong);
        SharedPreferencesCompat.apply(localEditor2);
        return;
      }
      finally
      {
        localObject1 = finally;
        SharedPreferencesCompat.apply(localEditor2);
        throw localObject1;
      }
    }
  }

  /** @deprecated */
  private void shutdownArtExecutorNow()
  {
    try
    {
      if (this.mArtExecutor != null)
      {
        List localList = this.mArtExecutor.shutdownNow();
        this.mArtExecutor = null;
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private void startAsyncDownloads()
  {
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local3 = new Runnable()
    {
      public void run()
      {
        ArtDownloadService.this.onDownloadQueueCompleted();
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local3);
  }

  private static <ArtId> void validateLocalFiles(ArtDownloadService paramArtDownloadService, HashMap<String, ArtId> paramHashMap, File paramFile)
  {
    if (paramFile == null)
      return;
    if (!paramFile.exists())
      return;
    String str1 = paramFile.getAbsolutePath();
    if (paramFile.isFile())
    {
      String str2 = paramFile.getName();
      if (".nomedia".equals(str2))
        return;
      if (paramHashMap.containsKey(str1))
      {
        Object localObject = paramHashMap.remove(str1);
        return;
      }
      boolean bool = paramArtDownloadService.deleteFileIfPossible(str1);
      return;
    }
    File[] arrayOfFile1 = paramFile.listFiles();
    if (arrayOfFile1 == null)
    {
      String str3 = "Neither file nor directory: " + str1;
      Log.w("ArtDownloadService", str3);
      return;
    }
    File[] arrayOfFile2 = arrayOfFile1;
    int i = arrayOfFile2.length;
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      File localFile = arrayOfFile2[j];
      validateLocalFiles(paramArtDownloadService, paramHashMap, localFile);
      j += 1;
    }
  }

  public boolean isDownloadSpaceAvailable(boolean paramBoolean)
  {
    boolean bool;
    if (paramBoolean)
      bool = ensureEnoughDiskSpace();
    while (true)
    {
      return bool;
      if ((!isCacheFull(paramBoolean)) && (isEnoughDiskSpace()))
        bool = true;
      else
        bool = false;
    }
  }

  public IBinder onBind(Intent paramIntent)
  {
    return null;
  }

  public void onCreate()
  {
    super.onCreate();
    SharedPreferences localSharedPreferences = getSharedPreferences("ArtDownload", 0);
    this.mPreferences = localSharedPreferences;
    IStreamabilityChangeListener localIStreamabilityChangeListener = this.mStreamabilityChangeListener;
    NetworkMonitorServiceConnection localNetworkMonitorServiceConnection = new NetworkMonitorServiceConnection(localIStreamabilityChangeListener);
    this.mNetworkMonitorServiceConnection = localNetworkMonitorServiceConnection;
    this.mNetworkMonitorServiceConnection.bindToService(this);
    clearOrphanedFilesAsync();
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        final HashSet localHashSet1 = Sets.newHashSet();
        final HashSet localHashSet2 = Sets.newHashSet();
        Bundle localBundle = getResultExtras(false);
        if (localBundle != null)
        {
          ArrayList localArrayList1 = localBundle.getStringArrayList("albumIdList");
          Iterator localIterator;
          if (localArrayList1 != null)
          {
            localIterator = localArrayList1.iterator();
            while (localIterator.hasNext())
            {
              Long localLong = Long.valueOf(Long.parseLong((String)localIterator.next()));
              boolean bool1 = localHashSet1.add(localLong);
            }
          }
          ArrayList localArrayList2 = localBundle.getStringArrayList("remoteUrlList");
          if (localArrayList2 != null)
          {
            localIterator = localArrayList2.iterator();
            while (localIterator.hasNext())
            {
              String str = (String)localIterator.next();
              boolean bool2 = localHashSet2.add(str);
            }
          }
        }
        LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            ArtDownloadService localArtDownloadService = ArtDownloadService.this;
            Set localSet1 = localHashSet1;
            Set localSet2 = localHashSet2;
            localArtDownloadService.onWatchListReceived(localSet1, localSet2);
          }
        };
        AsyncWorkers.runAsync(localLoggableHandler, local1);
      }
    };
    this.mAlbumArtWatchedBroadcastResultReceiver = local1;
  }

  public void onDestroy()
  {
    super.onDestroy();
    destroyOrphanedFilesScavenger();
    this.mNetworkMonitorServiceConnection.unbindFromService(this);
    shutdownArtExecutorNow();
  }

  public void onDownloadArtworkFile(File paramFile)
  {
    long l = paramFile.length();
    adjustCacheSize(l);
  }

  public void onDownloadQueueCompleted()
  {
    Intent localIntent = new Intent("com.google.android.music.ArtQueryWatched");
    BroadcastReceiver localBroadcastReceiver = this.mAlbumArtWatchedBroadcastResultReceiver;
    ArtDownloadService localArtDownloadService = this;
    Handler localHandler = null;
    String str = null;
    Bundle localBundle = null;
    localArtDownloadService.sendOrderedBroadcast(localIntent, null, localBroadcastReceiver, localHandler, -1, str, localBundle);
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    if (paramIntent != null)
    {
      String str1 = paramIntent.getAction();
      if ("com.google.android.music.SYNC_COMPLETE".equals(str1))
        clearOrphanedFilesAsync();
    }
    while (true)
    {
      return super.onStartCommand(paramIntent, paramInt1, paramInt2);
      if (paramIntent != null)
      {
        String str2 = paramIntent.getAction();
        if ("com.android.music.REMOTE_ART_REQUESTED".equals(str2))
        {
          Bundle localBundle = paramIntent.getExtras();
          if (localBundle == null)
            continue;
          Object localObject = localBundle.get("albumId");
          String str3 = localBundle.getString("remoteUrl");
          if ((localObject != null) || (str3 != null))
          {
            if ((localObject instanceof Long));
            for (Long localLong = (Long)localObject; ; localLong = null)
            {
              queueAlbumArtRequest(localLong, str3);
              break;
            }
          }
          Log.wtf("ArtDownloadService", "Art request should have album or url");
        }
      }
      else
      {
        startAsyncDownloads();
      }
    }
  }

  public void queueAlbumArtRequest(final Long paramLong, final String paramString)
  {
    INetworkMonitor localINetworkMonitor = this.mNetworkMonitorServiceConnection.getNetworkMonitor();
    if (localINetworkMonitor == null)
    {
      NetworkMonitorServiceConnection localNetworkMonitorServiceConnection = this.mNetworkMonitorServiceConnection;
      Runnable local4 = new Runnable()
      {
        public void run()
        {
          ArtDownloadService localArtDownloadService = ArtDownloadService.this;
          Long localLong = paramLong;
          String str = paramString;
          localArtDownloadService.queueAlbumArtRequest(localLong, str);
        }
      };
      localNetworkMonitorServiceConnection.addRunOnServiceConnected(local4);
      return;
    }
    try
    {
      if (!localINetworkMonitor.isStreamingAvailable())
      {
        stopSelf();
        return;
      }
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("ArtDownloadService", "queueAlbumArtRequest: remote call to NetworkMonitor failed");
      return;
    }
    queueAlbumArtRequestImp(paramLong, paramString);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.artwork.ArtDownloadService
 * JD-Core Version:    0.6.2
 */