package com.google.android.music.download.cache;

import android.content.Context;
import android.database.Cursor;
import com.google.android.music.store.Store;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.io.File;

public class MaxFreeSpaceDeletionStrategy
  implements DeletionStrategy
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private final Context mContext;
  private final FileSystem mFileSys;
  private final Store mStore;

  public MaxFreeSpaceDeletionStrategy(Context paramContext, FileSystem paramFileSystem, Store paramStore)
  {
    this.mFileSys = paramFileSystem;
    this.mStore = paramStore;
    this.mContext = paramContext;
  }

  public boolean createSpace(long paramLong, CacheLocation paramCacheLocation, FilteredFileDeleter paramFilteredFileDeleter)
  {
    boolean bool = true;
    Cursor localCursor;
    try
    {
      Store localStore = this.mStore;
      int i = paramCacheLocation.getSchemaValueForStorageType();
      localCursor = localStore.getDeletableFiles(i);
      while (localCursor.moveToNext())
      {
        long l1 = localCursor.getLong(0);
        Context localContext = this.mContext;
        String str = localCursor.getString(1);
        int j = localCursor.getInt(2);
        File localFile1 = CacheUtils.resolveMusicPath(localContext, str, j);
        if (this.mFileSys.delete(localFile1))
          this.mStore.removeFileLocation(l1);
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
    FileSystem localFileSystem = this.mFileSys;
    File localFile2 = paramCacheLocation.getPath();
    long l2 = localFileSystem.getFreeSpace(localFile2);
    if (paramLong < l2);
    while (true)
    {
      return bool;
      bool = false;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cache.MaxFreeSpaceDeletionStrategy
 * JD-Core Version:    0.6.2
 */