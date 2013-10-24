package com.google.android.music.download.cache;

import android.content.Context;
import android.database.Cursor;
import com.google.android.music.log.Log;
import com.google.android.music.store.Store;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.io.File;

public class LastUsedDeleteStrategy
  implements DeletionStrategy
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private final Context mContext;
  private final FileSystem mFileSys;
  private final Store mStore;

  public LastUsedDeleteStrategy(Context paramContext, FileSystem paramFileSystem, Store paramStore)
  {
    this.mStore = paramStore;
    this.mFileSys = paramFileSystem;
    this.mContext = paramContext;
  }

  public boolean createSpace(long paramLong, CacheLocation paramCacheLocation, FilteredFileDeleter paramFilteredFileDeleter)
  {
    Store localStore = this.mStore;
    int i = paramCacheLocation.getSchemaValueForStorageType();
    Cursor localCursor = localStore.getLeastRecentlyUsedCacheFiles(i);
    boolean bool;
    if (localCursor == null)
      bool = false;
    while (true)
    {
      return bool;
      long l1 = 0L;
      label37: if (l1 < paramLong);
      try
      {
        if (localCursor.moveToNext())
        {
          long l2 = localCursor.getLong(0);
          Context localContext = this.mContext;
          String str1 = localCursor.getString(1);
          int j = localCursor.getInt(2);
          File localFile = CacheUtils.resolveMusicPath(localContext, str1, j);
          if (!localFile.exists())
            break label37;
          long l3 = this.mFileSys.getLength(localFile);
          if (l3 <= 0L)
            break label37;
          if (LOGV)
          {
            StringBuilder localStringBuilder = new StringBuilder().append("About to delete local file for: ");
            String str2 = localFile.getAbsolutePath();
            String str3 = str2;
            Log.i("MusicCache", str3);
          }
          if (!this.mFileSys.delete(localFile))
            break label37;
          this.mStore.removeFileLocation(l2);
          l1 += l3;
          break label37;
        }
        if (l1 >= paramLong)
        {
          bool = true;
          localCursor.close();
          continue;
        }
        Object[] arrayOfObject = new Object[3];
        arrayOfObject[0] = paramCacheLocation;
        Long localLong1 = Long.valueOf(paramLong);
        arrayOfObject[1] = localLong1;
        Long localLong2 = Long.valueOf(l1);
        arrayOfObject[2] = localLong2;
        String str4 = String.format("Could not create enough space in %s requiredSpace=%d recoveredSize=%d", arrayOfObject);
        Log.w("MusicCache", str4);
        bool = false;
        localCursor.close();
      }
      finally
      {
        localCursor.close();
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cache.LastUsedDeleteStrategy
 * JD-Core Version:    0.6.2
 */