package com.google.android.music.download.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import com.google.android.gsf.Gservices;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.DownloadRequest;
import com.google.android.music.download.DownloadRequest.Owner;
import com.google.android.music.download.DownloadUtils;
import com.google.android.music.log.Log;
import com.google.android.music.log.LogFile;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.Store;
import com.google.android.music.utils.DbUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.PostFroyoUtils.EnvironmentCompat;
import com.google.common.collect.ImmutableMap;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CacheManagerImpl extends ICacheManager.Stub
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private static final LogFile sLogFile = Log.getLogFile("com.google.android.music.pin");
  private volatile boolean mAllowCaching = true;
  private volatile CacheStrategy mCacheStrategy;
  private final Context mContext;
  final RemoteCallbackList<IDeleteFilter> mDeleteFilters;
  private volatile DeletionStrategy mDeletionStrategy;
  private final FileSystem mFileSystem;
  final FilteredFileDeleter mFilteredFileDeleter;
  private final boolean mIsExternalSameAsInternal;
  private volatile CacheStrategy mLongTermCacheStrategy;
  private final MusicPreferences mMusicPreferences;

  public CacheManagerImpl(Context paramContext, MusicPreferences paramMusicPreferences)
  {
    RemoteCallbackList localRemoteCallbackList = new RemoteCallbackList();
    this.mDeleteFilters = localRemoteCallbackList;
    FilteredFileDeleter local1 = new FilteredFileDeleter()
    {
      public Set<ContentIdentifier> getFilteredIds()
      {
        return CacheManagerImpl.this.handleGetFilteredIds();
      }

      public boolean requestDeleteFile(File paramAnonymousFile)
      {
        return CacheManagerImpl.this.handleDeleteFile(paramAnonymousFile);
      }
    };
    this.mFilteredFileDeleter = local1;
    boolean bool = PostFroyoUtils.EnvironmentCompat.isExternalStorageEmulated();
    this.mIsExternalSameAsInternal = bool;
    this.mContext = paramContext;
    this.mMusicPreferences = paramMusicPreferences;
    FilteredFileDeleter localFilteredFileDeleter = this.mFilteredFileDeleter;
    FileSystemImpl localFileSystemImpl = new FileSystemImpl(localFilteredFileDeleter);
    this.mFileSystem = localFileSystemImpl;
    initCacheStrategies();
  }

  private void cleanUpDirectoryIfExists(File paramFile)
  {
    if (paramFile == null)
      return;
    if (!paramFile.exists())
      return;
    String str = "Cleaning up an old dir: " + paramFile;
    Log.w("CacheManagerImpl", str);
    boolean bool = CacheUtils.cleanUpDirectory(paramFile);
  }

  private CacheLocation getCacheLocation()
  {
    File localFile1 = CacheUtils.getSecondaryExternalMusicCacheDirectory(this.mContext);
    CacheLocation localCacheLocation;
    if ((localFile1 != null) && ((localFile1.exists()) || (prepareDirectory(localFile1))))
    {
      CacheUtils.StorageType localStorageType1 = CacheUtils.StorageType.SECONDARY_EXTERNAL;
      localCacheLocation = new CacheLocation(localFile1, localStorageType1);
    }
    while (true)
    {
      return localCacheLocation;
      if (!this.mIsExternalSameAsInternal)
      {
        File localFile2 = CacheUtils.getExternalMusicCacheDirectory(this.mContext);
        if ((localFile2 != null) && ((localFile2.exists()) || (prepareDirectory(localFile2))))
        {
          CacheUtils.StorageType localStorageType2 = CacheUtils.StorageType.EXTERNAL;
          localCacheLocation = new CacheLocation(localFile2, localStorageType2);
        }
      }
      else
      {
        File localFile3 = CacheUtils.getInternalMusicCacheDirectory(this.mContext);
        if ((localFile3 != null) && ((localFile3.exists()) || (prepareDirectory(localFile3))))
        {
          CacheUtils.StorageType localStorageType3 = CacheUtils.StorageType.INTERNAL;
          localCacheLocation = new CacheLocation(localFile3, localStorageType3);
        }
        else
        {
          localCacheLocation = null;
        }
      }
    }
  }

  private void handleClearCache()
  {
    ContentValues localContentValues = new ContentValues();
    Integer localInteger1 = Integer.valueOf(0);
    localContentValues.put("LocalCopyType", localInteger1);
    Integer localInteger2 = Integer.valueOf(0);
    localContentValues.put("LocalCopyStorageType", localInteger2);
    localContentValues.putNull("LocalCopyPath");
    Integer localInteger3 = Integer.valueOf(0);
    localContentValues.put("LocalCopySize", localInteger3);
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("LocalCopyType").append('=').append(100);
    Set localSet = this.mFilteredFileDeleter.getFilteredIds();
    HashSet localHashSet = new HashSet();
    Iterator localIterator = localSet.iterator();
    while (localIterator.hasNext())
    {
      ContentIdentifier localContentIdentifier = (ContentIdentifier)localIterator.next();
      if (localContentIdentifier.isCacheable())
      {
        Long localLong = Long.valueOf(localContentIdentifier.getId());
        boolean bool1 = localHashSet.add(localLong);
      }
    }
    if (localHashSet.size() != 0)
    {
      StringBuilder localStringBuilder3 = localStringBuilder1.append(" AND ").append("Id").append(" NOT ");
      DbUtils.appendIN(localStringBuilder1, localHashSet);
    }
    Store localStore = Store.getInstance(this.mContext);
    boolean bool2 = false;
    SQLiteDatabase localSQLiteDatabase = localStore.beginWriteTxn();
    try
    {
      String str = localStringBuilder1.toString();
      int i = localSQLiteDatabase.update("MUSIC", localContentValues, str, null);
      localStore.endWriteTxn(localSQLiteDatabase, true);
      if (1 == 0)
        return;
      clearOrphanedFiles();
      return;
    }
    finally
    {
      localStore.endWriteTxn(localSQLiteDatabase, bool2);
    }
  }

  private boolean handleDeleteFile(File paramFile)
  {
    int i = 0;
    while (true)
    {
      synchronized (this.mDeleteFilters)
      {
        int j = this.mDeleteFilters.beginBroadcast();
        int k = j;
        int m = 0;
        if (m < k);
        try
        {
          IDeleteFilter localIDeleteFilter = (IDeleteFilter)this.mDeleteFilters.getBroadcastItem(m);
          String str1 = paramFile.getAbsolutePath();
          boolean bool1 = localIDeleteFilter.shouldFilter(str1);
          if (bool1)
          {
            i = 1;
            this.mDeleteFilters.finishBroadcast();
            if (i != 0)
              break label164;
            bool2 = paramFile.delete();
            return bool2;
          }
        }
        catch (RemoteException localRemoteException)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("Failed to call delete filter for ");
          String str2 = paramFile.getAbsolutePath();
          String str3 = str2;
          Log.e("CacheManagerImpl", str3, localRemoteException);
          m += 1;
          continue;
        }
        finally
        {
          this.mDeleteFilters.finishBroadcast();
        }
      }
      label164: boolean bool2 = false;
    }
  }

  private HashSet<ContentIdentifier> handleGetFilteredIds()
  {
    HashSet localHashSet = new HashSet();
    synchronized (this.mDeleteFilters)
    {
      int i = this.mDeleteFilters.beginBroadcast();
      int j = i;
      int k = 0;
      while (true)
      {
        if (k < j);
        try
        {
          ContentIdentifier[] arrayOfContentIdentifier1 = ((IDeleteFilter)this.mDeleteFilters.getBroadcastItem(k)).getFilteredIds();
          if (arrayOfContentIdentifier1 != null)
          {
            ContentIdentifier[] arrayOfContentIdentifier2 = arrayOfContentIdentifier1;
            int m = arrayOfContentIdentifier2.length;
            int n = 0;
            while (n < m)
            {
              ContentIdentifier localContentIdentifier = arrayOfContentIdentifier2[n];
              boolean bool = localHashSet.add(localContentIdentifier);
              n += 1;
            }
          }
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("CacheManagerImpl", "Failed to get deelte filter ids", localRemoteException);
          k += 1;
          continue;
          this.mDeleteFilters.finishBroadcast();
          return localHashSet;
        }
        finally
        {
          this.mDeleteFilters.finishBroadcast();
        }
      }
    }
  }

  private void initCacheStrategies()
  {
    boolean bool = this.mMusicPreferences.isCachedStreamingMusicEnabled();
    this.mAllowCaching = bool;
    Store localStore = Store.getInstance(this.mContext);
    LastUsedDeleteStrategy localLastUsedDeleteStrategy;
    if (this.mAllowCaching)
    {
      long l1 = Gservices.getLong(this.mContext.getContentResolver(), "music_short_term_cache_min_free_space", 500L);
      FileSystem localFileSystem1 = this.mFileSystem;
      long l2 = l1 * 1024L * 1024L;
      FillUpToLimitCacheStrategy localFillUpToLimitCacheStrategy = new FillUpToLimitCacheStrategy(localFileSystem1, localStore, 0.8F, 2147483648L, l2);
      this.mCacheStrategy = localFillUpToLimitCacheStrategy;
      Context localContext1 = this.mContext;
      FileSystem localFileSystem2 = this.mFileSystem;
      localLastUsedDeleteStrategy = new LastUsedDeleteStrategy(localContext1, localFileSystem2, localStore);
    }
    MaxFreeSpaceDeletionStrategy localMaxFreeSpaceDeletionStrategy;
    for (this.mDeletionStrategy = localLastUsedDeleteStrategy; ; this.mDeletionStrategy = localMaxFreeSpaceDeletionStrategy)
    {
      FileSystem localFileSystem3 = this.mFileSystem;
      FillAllSpaceCacheStrategy localFillAllSpaceCacheStrategy = new FillAllSpaceCacheStrategy(localFileSystem3);
      this.mLongTermCacheStrategy = localFillAllSpaceCacheStrategy;
      return;
      DeleteAllCacheStrategy localDeleteAllCacheStrategy = new DeleteAllCacheStrategy();
      this.mCacheStrategy = localDeleteAllCacheStrategy;
      Context localContext2 = this.mContext;
      FileSystem localFileSystem4 = this.mFileSystem;
      localMaxFreeSpaceDeletionStrategy = new MaxFreeSpaceDeletionStrategy(localContext2, localFileSystem4, localStore);
    }
  }

  private boolean prepareDirectory(File paramFile)
  {
    boolean bool = false;
    if ((!this.mFileSystem.exists(paramFile)) && (!paramFile.mkdirs()))
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("Could not create directory: ");
      String str1 = paramFile.getAbsolutePath();
      String str2 = str1;
      Log.e("CacheManagerImpl", str2);
    }
    while (true)
    {
      return bool;
      File localFile = new File(paramFile, ".nomedia");
      if (!localFile.exists())
        try
        {
          if ((localFile.createNewFile()) || (localFile.exists()))
            break label200;
          StringBuilder localStringBuilder2 = new StringBuilder().append("Could not create: ");
          String str3 = localFile.getAbsolutePath();
          String str4 = str3;
          Log.e("CacheManagerImpl", str4);
        }
        catch (IOException localIOException)
        {
          StringBuilder localStringBuilder3 = new StringBuilder().append("Error while trying to create (").append(localFile).append("): ");
          String str5 = localIOException.getMessage();
          String str6 = str5;
          Log.w("CacheManagerImpl", str6);
        }
      else
        label200: bool = true;
    }
  }

  private void validateLocalFiles(Set<String> paramSet, File paramFile, boolean paramBoolean)
    throws IOException
  {
    if (paramFile == null)
    {
      if (LOGV)
        Log.i("CacheManagerImpl", "Cached file or directory is null");
      if (sLogFile == null)
        return;
      sLogFile.d("CacheManagerImpl", "Cached file or directory is null");
      return;
    }
    if (!paramFile.exists())
    {
      if (LOGV)
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("Cached file or directory \"");
        String str1 = paramFile.getAbsolutePath();
        String str2 = str1 + "\" does not exist.";
        Log.i("CacheManagerImpl", str2);
      }
      if (sLogFile != null)
      {
        LogFile localLogFile1 = sLogFile;
        StringBuilder localStringBuilder2 = new StringBuilder().append("Cached file or directory \"");
        String str3 = paramFile.getAbsolutePath();
        String str4 = str3 + "\" does not exist.";
        localLogFile1.w("CacheManagerImpl", str4);
      }
      if (!paramBoolean)
        return;
      if (CacheUtils.isExternalStorageMounted())
        return;
      if (sLogFile != null)
        sLogFile.w("CacheManagerImpl", "External storage not mounted");
      throw new IOException("External storage not mounted");
    }
    String str5 = paramFile.getAbsolutePath();
    if (paramFile.isFile())
    {
      String str6 = paramFile.getName();
      if (".nomedia".equals(str6))
        return;
      if (paramSet.contains(str5))
        return;
      if (!this.mFileSystem.delete(paramFile))
        return;
      if (LOGV)
      {
        String str7 = "Deleted orphaned file: " + str5;
        Log.i("CacheManagerImpl", str7);
      }
      if (sLogFile == null)
        return;
      LogFile localLogFile2 = sLogFile;
      String str8 = "Deleted orphaned file: " + str5;
      localLogFile2.d("CacheManagerImpl", str8);
      return;
    }
    File[] arrayOfFile1 = paramFile.listFiles();
    if (arrayOfFile1 == null)
    {
      if (!LOGV)
        return;
      String str9 = "Neither file nor directory: " + str5;
      Log.i("CacheManagerImpl", str9);
      return;
    }
    if (sLogFile != null)
    {
      LogFile localLogFile3 = sLogFile;
      StringBuilder localStringBuilder3 = new StringBuilder().append("File.listFiles(): ");
      int i = arrayOfFile1.length;
      String str10 = i;
      localLogFile3.d("CacheManagerImpl", str10);
    }
    File[] arrayOfFile2 = arrayOfFile1;
    int j = arrayOfFile2.length;
    int k = 0;
    while (true)
    {
      if (k >= j)
        return;
      File localFile = arrayOfFile2[k];
      validateLocalFiles(paramSet, localFile, paramBoolean);
      k += 1;
    }
  }

  void clearCachedFiles()
  {
    initCacheStrategies();
    handleClearCache();
  }

  void clearOrphanedFiles()
  {
    if (sLogFile != null)
      sLogFile.d("CacheManagerImpl", "clearOrphanedFiles");
    Store localStore1 = Store.getInstance(this.mContext);
    int i = localStore1.deleteOrphanedExternalMusic();
    SQLiteDatabase localSQLiteDatabase = localStore1.beginRead();
    File localFile1;
    File localFile2;
    Cursor localCursor;
    HashSet localHashSet1;
    HashSet localHashSet2;
    HashMap localHashMap1;
    while (true)
    {
      File localFile3;
      String str1;
      long l;
      int j;
      File localFile4;
      try
      {
        localFile1 = CacheUtils.getExternalMusicCacheDirectory(this.mContext);
        localFile2 = CacheUtils.getInternalMusicCacheDirectory(this.mContext);
        localFile3 = CacheUtils.getSecondaryExternalMusicCacheDirectory(this.mContext);
        String[] arrayOfString = new String[4];
        arrayOfString[0] = "Id";
        arrayOfString[1] = "LocalCopyPath";
        arrayOfString[2] = "LocalCopySize";
        arrayOfString[3] = "LocalCopyStorageType";
        localCursor = localSQLiteDatabase.query("MUSIC", arrayOfString, "LocalCopyType IN (100,200)", null, null, null, null);
        localHashSet1 = new HashSet();
        localHashSet2 = new HashSet();
        localHashMap1 = new HashMap();
        if ((localCursor == null) || (!localCursor.moveToNext()))
          break;
        str1 = localCursor.getString(1);
        l = localCursor.getLong(0);
        j = localCursor.getInt(3);
        localFile4 = null;
        if ((j == 2) && (localFile1 != null))
        {
          String str2 = str1;
          localFile4 = new File(localFile1, str2);
          if (localFile4 == null)
            break label397;
          if (localFile4.exists())
            break label331;
          Long localLong1 = Long.valueOf(l);
          boolean bool1 = localHashSet2.add(localLong1);
          continue;
        }
      }
      finally
      {
        if (localSQLiteDatabase != null)
          localStore1.endRead(localSQLiteDatabase);
      }
      if ((j == 1) && (localFile2 != null))
      {
        File localFile5 = localFile2;
        String str3 = str1;
        localFile4 = new File(localFile5, str3);
      }
      else if ((j == 3) && (localFile3 != null))
      {
        File localFile6 = localFile3;
        String str4 = str1;
        localFile4 = new File(localFile6, str4);
        continue;
        label331: String str5 = localFile4.getAbsolutePath();
        boolean bool2 = localHashSet1.add(str5);
        if (localCursor.getLong(2) == 0L)
        {
          Long localLong2 = Long.valueOf(l);
          Long localLong3 = Long.valueOf(localFile4.length());
          Object localObject2 = localHashMap1.put(localLong2, localLong3);
          continue;
          label397: if (LOGV)
          {
            StringBuilder localStringBuilder1 = new StringBuilder().append("Ignoring path: ");
            String str6 = str1;
            String str7 = str6 + " from validation as the storage is missing";
            Log.i("CacheManagerImpl", str7);
          }
        }
      }
    }
    Store.safeClose(localCursor);
    localStore1.endRead(localSQLiteDatabase);
    localSQLiteDatabase = null;
    if (sLogFile != null)
    {
      LogFile localLogFile1 = sLogFile;
      Object[] arrayOfObject1 = new Object[2];
      Integer localInteger1 = Integer.valueOf(localHashSet1.size());
      arrayOfObject1[0] = localInteger1;
      Integer localInteger2 = Integer.valueOf(localHashSet2.size());
      arrayOfObject1[1] = localInteger2;
      String str8 = String.format("Before validation: knownFiles=%d nonExisting=%d", arrayOfObject1);
      localLogFile1.d("CacheManagerImpl", str8);
    }
    if (!localHashSet2.isEmpty())
    {
      StringBuilder localStringBuilder2 = new StringBuilder().append("Detected ");
      int k = localHashSet2.size();
      String str9 = k + " non-existing files " + "referenced in db";
      Log.w("CacheManagerImpl", str9);
      if (sLogFile != null)
      {
        LogFile localLogFile2 = sLogFile;
        Object[] arrayOfObject2 = new Object[2];
        Integer localInteger3 = Integer.valueOf(localHashSet2.size());
        arrayOfObject2[0] = localInteger3;
        arrayOfObject2[1] = localHashSet2;
        String str10 = String.format("nonExisting=%d Clear references: %s", arrayOfObject2);
        localLogFile2.i("CacheManagerImpl", str10);
      }
      Store localStore2 = localStore1;
      HashSet localHashSet3 = localHashSet2;
      localStore2.clearReferencesInDatabase(localHashSet3);
    }
    try
    {
      if (sLogFile != null)
      {
        LogFile localLogFile3 = sLogFile;
        Object[] arrayOfObject3 = new Object[1];
        String str11 = localFile1.getAbsolutePath();
        arrayOfObject3[0] = str11;
        String str12 = String.format("validateLocalFiles: externalCacheDir=%s", arrayOfObject3);
        localLogFile3.d("CacheManagerImpl", str12);
      }
      CacheManagerImpl localCacheManagerImpl1 = this;
      HashSet localHashSet4 = localHashSet1;
      localCacheManagerImpl1.validateLocalFiles(localHashSet4, localFile1, true);
      if (localFile2 != null)
        if (localFile1 != null)
        {
          File localFile7 = localFile2;
          if (localFile1.compareTo(localFile7) == 0);
        }
        else
        {
          if (sLogFile != null)
          {
            LogFile localLogFile4 = sLogFile;
            Object[] arrayOfObject4 = new Object[1];
            String str13 = localFile2.getAbsolutePath();
            arrayOfObject4[0] = str13;
            String str14 = String.format("validateLocalFiles: localCacheDir=%s", arrayOfObject4);
            localLogFile4.d("CacheManagerImpl", str14);
          }
          CacheManagerImpl localCacheManagerImpl2 = this;
          HashSet localHashSet5 = localHashSet1;
          File localFile8 = localFile2;
          localCacheManagerImpl2.validateLocalFiles(localHashSet5, localFile8, false);
        }
      Store localStore3 = localStore1;
      HashMap localHashMap2 = localHashMap1;
      localStore3.fixLocalPathSize(localHashMap2);
      if (localSQLiteDatabase != null)
        localStore1.endRead(localSQLiteDatabase);
      File localFile9 = CacheUtils.getExternalMusicCacheDirectory_Old(this.mContext);
      cleanUpDirectoryIfExists(localFile9);
      File localFile10 = CacheUtils.getExternalArtworkCacheDirectory_Old(this.mContext);
      cleanUpDirectoryIfExists(localFile10);
      File localFile11 = CacheUtils.getInternalMusicCacheDirectory_Old(this.mContext);
      cleanUpDirectoryIfExists(localFile11);
      File localFile12 = CacheUtils.getInternalArtworkCacheDirectory_Old(this.mContext);
      cleanUpDirectoryIfExists(localFile12);
      return;
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        StringBuilder localStringBuilder3 = new StringBuilder().append("Failed to validate files: ");
        String str15 = localIOException.getMessage();
        String str16 = str15;
        Log.e("CacheManagerImpl", str16);
        if (sLogFile != null)
        {
          LogFile localLogFile5 = sLogFile;
          StringBuilder localStringBuilder4 = new StringBuilder().append("Failed to validate files: ");
          String str17 = localIOException.getMessage();
          String str18 = str17;
          localLogFile5.e("CacheManagerImpl", str18, localIOException);
        }
      }
    }
  }

  public long getAvailableFreeStorageSpaceInBytes()
    throws RemoteException
  {
    CacheLocation localCacheLocation = getCacheLocation();
    if (localCacheLocation == null);
    long l4;
    long l5;
    for (long l1 = 0L; ; l1 = l4 - l5)
    {
      return l1;
      FileSystem localFileSystem = this.mFileSystem;
      File localFile = localCacheLocation.getPath();
      long l2 = localFileSystem.getFreeSpace(localFile);
      long l3 = Store.getInstance(this.mContext).getTotalCachedSize(100);
      l4 = l2 + l3;
      l5 = Store.getInstance(this.mContext).getSizeOfUndownloadedKeepOnFiles();
    }
  }

  public FileLocation getTempFileLocation(ContentIdentifier paramContentIdentifier, int paramInt1, long paramLong, int paramInt2)
    throws RemoteException
  {
    DownloadRequest.Owner localOwner = DownloadRequest.Owner.values()[paramInt1];
    if (LOGV)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("getTempFileLocation: id=").append(paramContentIdentifier).append(" cacheType=");
      int i = paramInt2;
      String str1 = i;
      Log.d("CacheManagerImpl", str1);
    }
    CacheLocation localCacheLocation = getCacheLocation();
    FileLocation localFileLocation;
    if (localCacheLocation == null)
      localFileLocation = null;
    while (true)
    {
      return localFileLocation;
      CacheStrategy localCacheStrategy1;
      long l1;
      if (paramInt2 == 2)
      {
        localCacheStrategy1 = this.mCacheStrategy;
        l1 = paramLong;
      }
      CacheStrategy localCacheStrategy2;
      long l3;
      for (long l2 = localCacheStrategy1.checkRequiredSpace(l1, localCacheLocation); ; l2 = localCacheStrategy2.checkRequiredSpace(l3, localCacheLocation))
      {
        if (l2 == 0L)
          break label178;
        DeletionStrategy localDeletionStrategy = this.mDeletionStrategy;
        FilteredFileDeleter localFilteredFileDeleter = this.mFilteredFileDeleter;
        if (localDeletionStrategy.createSpace(l2, localCacheLocation, localFilteredFileDeleter))
          break label178;
        Log.w("CacheManagerImpl", "Failed to find playback cache space");
        localFileLocation = null;
        break;
        localCacheStrategy2 = this.mLongTermCacheStrategy;
        l3 = paramLong;
      }
      label178: Object[] arrayOfObject = new Object[2];
      String str2 = localOwner.toFileSystemString();
      arrayOfObject[0] = str2;
      String str3 = paramContentIdentifier.toFileSystemString();
      arrayOfObject[1] = str3;
      String str4 = String.format("%s_%s.tmp", arrayOfObject);
      File localFile = localCacheLocation.getCacheFile(str4);
      CacheUtils.StorageType localStorageType = localCacheLocation.getStorageType();
      int j = paramInt2;
      localFileLocation = new FileLocation(localFile, localStorageType, j);
    }
  }

  public long getTotalStorageSpaceInBytes()
    throws RemoteException
  {
    CacheLocation localCacheLocation = getCacheLocation();
    if (localCacheLocation == null);
    FileSystem localFileSystem;
    File localFile;
    for (long l = 0L; ; l = localFileSystem.getTotalSpace(localFile))
    {
      return l;
      localFileSystem = this.mFileSystem;
      localFile = localCacheLocation.getPath();
    }
  }

  public void registerDeleteFilter(IDeleteFilter paramIDeleteFilter)
    throws RemoteException
  {
    if (paramIDeleteFilter == null)
      return;
    boolean bool = this.mDeleteFilters.register(paramIDeleteFilter);
  }

  public void requestDelete(DownloadRequest paramDownloadRequest)
    throws RemoteException
  {
    File localFile = paramDownloadRequest.getFileLocation().getFullPath();
    boolean bool = handleDeleteFile(localFile);
  }

  void setAllowCaching(boolean paramBoolean)
  {
    this.mAllowCaching = paramBoolean;
    initCacheStrategies();
  }

  public String storeInCache(DownloadRequest paramDownloadRequest, String paramString, long paramLong)
    throws RemoteException
  {
    if (LOGV)
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("storeInCache: ");
      DownloadRequest localDownloadRequest1 = paramDownloadRequest;
      String str1 = localDownloadRequest1;
      Log.d("CacheManagerImpl", str1);
    }
    ContentIdentifier localContentIdentifier = paramDownloadRequest.getId();
    String str2;
    if (!localContentIdentifier.isCacheable())
    {
      Log.w("CacheManagerImpl", "Trying to cache track from non cacheable domain");
      str2 = null;
    }
    while (true)
    {
      return str2;
      Store localStore = Store.getInstance(this.mContext);
      StringBuilder localStringBuilder2 = new StringBuilder();
      String str3 = localContentIdentifier.toFileSystemString();
      StringBuilder localStringBuilder3 = localStringBuilder2.append(str3);
      ImmutableMap localImmutableMap = DownloadUtils.MimeToExtensionMap;
      String str4 = paramString;
      String str5 = (String)localImmutableMap.get(str4);
      if (str5 == null)
      {
        StringBuilder localStringBuilder4 = new StringBuilder().append("Missing file extension for download request: ");
        DownloadRequest localDownloadRequest2 = paramDownloadRequest;
        String str6 = localDownloadRequest2;
        Log.e("CacheManagerImpl", str6);
        str2 = null;
      }
      else
      {
        StringBuilder localStringBuilder5 = localStringBuilder2.append(".");
        StringBuilder localStringBuilder6 = localStringBuilder2.append(str5);
        Context localContext = this.mContext;
        int i = paramDownloadRequest.getFileLocation().getSchemaValueForStorageType();
        CacheLocation localCacheLocation = CacheUtils.getMusicCacheLocation(localContext, i);
        File localFile1 = paramDownloadRequest.getFileLocation().getFullPath();
        if (localFile1.length() == 0L)
        {
          StringBuilder localStringBuilder7 = new StringBuilder().append("Failed to store empty file in cache: ");
          String str7 = localFile1.getAbsolutePath();
          String str8 = str7;
          Log.w("CacheManagerImpl", str8);
          str2 = null;
        }
        else
        {
          String str9 = localStringBuilder2.toString();
          File localFile2 = localCacheLocation.getCacheFile(str9);
          boolean bool = localFile2.delete();
          if (!localFile1.renameTo(localFile2))
          {
            Object[] arrayOfObject = new Object[2];
            String str10 = localFile1.getAbsolutePath();
            arrayOfObject[0] = str10;
            String str11 = localFile2.getAbsolutePath();
            arrayOfObject[1] = str11;
            String str12 = String.format("Failed to rename file %s to file %s", arrayOfObject);
            Log.e("CacheManagerImpl", str12);
            str2 = null;
          }
          else
          {
            long l1 = localContentIdentifier.getId();
            String str13 = localStringBuilder2.toString();
            int j = paramDownloadRequest.getFileLocation().getSchemaValueForCacheType();
            int k = paramDownloadRequest.getFileLocation().getSchemaValueForStorageType();
            long l2 = paramLong;
            localStore.updateCachedFileLocation(l1, str13, j, l2, k);
            str2 = localFile2.getAbsolutePath();
          }
        }
      }
    }
  }

  public void unregisterDeleteFilter(IDeleteFilter paramIDeleteFilter)
    throws RemoteException
  {
    if (paramIDeleteFilter == null)
      return;
    boolean bool = this.mDeleteFilters.unregister(paramIDeleteFilter);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cache.CacheManagerImpl
 * JD-Core Version:    0.6.2
 */