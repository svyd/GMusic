package com.google.android.music.download.cache;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicFile;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.io.File;

public class CacheUtils
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);

  public static boolean cleanUpDirectory(File paramFile)
  {
    boolean bool1 = false;
    boolean bool2 = true;
    if (!paramFile.isDirectory())
      return bool1;
    File[] arrayOfFile = paramFile.listFiles();
    int i = arrayOfFile.length;
    int j = 0;
    if (j < i)
    {
      if ((arrayOfFile[j].delete()) && (bool2));
      for (bool2 = true; ; bool2 = false)
      {
        j += 1;
        break;
      }
    }
    if ((paramFile.delete()) && (bool2));
    for (bool2 = true; ; bool2 = false)
    {
      bool1 = bool2;
      break;
    }
  }

  public static File getExternalAlbumArtworkCacheDirectory(Context paramContext)
  {
    return getExternalCacheDirectory(paramContext, "artwork");
  }

  @Deprecated
  public static File getExternalArtworkCacheDirectory_Old(Context paramContext)
  {
    return getExternalCacheDirectory_Old(paramContext, "artwork");
  }

  public static File getExternalCacheDirectory(Context paramContext, String paramString)
  {
    File localFile;
    if (!isExternalStorageMounted())
      localFile = null;
    while (true)
    {
      return localFile;
      localFile = paramContext.getExternalFilesDir(null);
      if (localFile == null)
        localFile = null;
      else if (paramString != null)
        localFile = new File(localFile, paramString);
    }
  }

  @Deprecated
  private static File getExternalCacheDirectory_Old(Context paramContext, String paramString)
  {
    File localFile1 = null;
    if (!isExternalStorageMounted());
    while (true)
    {
      return localFile1;
      File localFile2 = paramContext.getExternalCacheDir();
      if (localFile2 != null)
        localFile1 = new File(localFile2, paramString);
    }
  }

  public static File getExternalMusicCacheDirectory(Context paramContext)
  {
    return getExternalCacheDirectory(paramContext, "music");
  }

  @Deprecated
  public static File getExternalMusicCacheDirectory_Old(Context paramContext)
  {
    return getExternalCacheDirectory_Old(paramContext, "music");
  }

  public static File getInternalAlbumArtworkCacheDirectory(Context paramContext)
  {
    return getInternalCacheDirectory(paramContext, "artwork");
  }

  @Deprecated
  public static File getInternalArtworkCacheDirectory_Old(Context paramContext)
  {
    return getInternalCacheDirectory_Old(paramContext, "artwork");
  }

  public static File getInternalCacheDirectory(Context paramContext, String paramString)
  {
    File localFile = paramContext.getFilesDir();
    if (localFile != null)
      if (paramString == null);
    for (localFile = new File(localFile, paramString); ; localFile = null)
      return localFile;
  }

  @Deprecated
  private static File getInternalCacheDirectory_Old(Context paramContext, String paramString)
  {
    File localFile1 = paramContext.getCacheDir();
    if (localFile1 != null);
    for (File localFile2 = new File(localFile1, paramString); ; localFile2 = null)
      return localFile2;
  }

  public static File getInternalMusicCacheDirectory(Context paramContext)
  {
    return getInternalCacheDirectory(paramContext, "music");
  }

  @Deprecated
  public static File getInternalMusicCacheDirectory_Old(Context paramContext)
  {
    return getInternalCacheDirectory_Old(paramContext, "music");
  }

  public static CacheLocation getMusicCacheLocation(Context paramContext, int paramInt)
  {
    File localFile = null;
    StorageType localStorageType = StorageType.EXTERNAL;
    switch (paramInt)
    {
    default:
      String str = "Unexpected storage type value: " + paramInt;
      Log.wtf("CacheUtils", str);
    case 0:
      if (localFile != null)
        break;
    case 2:
    case 1:
    case 3:
    }
    for (CacheLocation localCacheLocation = null; ; localCacheLocation = new CacheLocation(localFile, localStorageType))
    {
      return localCacheLocation;
      localFile = getExternalMusicCacheDirectory(paramContext);
      localStorageType = StorageType.EXTERNAL;
      break;
      localFile = getInternalMusicCacheDirectory(paramContext);
      localStorageType = StorageType.INTERNAL;
      break;
      localFile = getSecondaryExternalMusicCacheDirectory(paramContext);
      localStorageType = StorageType.SECONDARY_EXTERNAL;
      break;
    }
  }

  public static int getSchemaValueForStorageType(StorageType paramStorageType)
  {
    int i = 2;
    StorageType localStorageType1 = StorageType.INTERNAL;
    if (paramStorageType == localStorageType1)
      i = 1;
    while (true)
    {
      return i;
      StorageType localStorageType2 = StorageType.SECONDARY_EXTERNAL;
      if (paramStorageType == localStorageType2)
        i = 3;
    }
  }

  public static File getSecondaryExternalCacheDirectory(Context paramContext, String paramString)
  {
    String str1 = getSecondaryExternalMountPoint(paramContext);
    if (str1 == null);
    File localFile4;
    for (File localFile1 = null; ; localFile1 = new File(localFile4, paramString))
    {
      return localFile1;
      File localFile2 = new File(str1, "Android/data/");
      String str2 = paramContext.getPackageName();
      File localFile3 = new File(localFile2, str2);
      localFile4 = new File(localFile3, "files");
    }
  }

  static String getSecondaryExternalMountPoint(Context paramContext)
  {
    Object localObject1 = new Object();
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, localObject1);
    try
    {
      String str1 = localMusicPreferences.getSecondaryExternalStorageMountPoint();
      String str2 = str1;
      return str2;
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(localObject1);
    }
  }

  public static File getSecondaryExternalMusicCacheDirectory(Context paramContext)
  {
    return getSecondaryExternalCacheDirectory(paramContext, "music");
  }

  public static boolean isExternalStorageMounted()
  {
    String str = Environment.getExternalStorageState();
    if (!"mounted".equals(str))
      if (LOGV)
        Log.i("CacheUtils", "External storage is not mounted");
    for (boolean bool = false; ; bool = true)
      return bool;
  }

  public static boolean isSecondaryExternalStorageMounted(Context paramContext)
  {
    String str = StorageProbeUtils.getVolumeState(getSecondaryExternalMountPoint(paramContext));
    if (!"mounted".equals(str))
      if (LOGV)
        Log.i("CacheUtils", "External storage is not mounted");
    for (boolean bool = false; ; bool = true)
      return bool;
  }

  public static File resolveMusicPath(Context paramContext, MusicFile paramMusicFile)
  {
    String str = paramMusicFile.getLocalCopyPath();
    int i = paramMusicFile.getLocalCopyStorageType();
    return resolveMusicPath(paramContext, str, i);
  }

  public static File resolveMusicPath(Context paramContext, String paramString, int paramInt)
  {
    File localFile1 = null;
    if (TextUtils.isEmpty(paramString))
      return localFile1;
    File localFile2 = null;
    switch (paramInt)
    {
    default:
      String str = "Unexpected storage type value: " + paramInt;
      Log.wtf("CacheUtils", str);
    case 0:
    case 2:
    case 1:
    case 3:
    }
    while (localFile2 != null)
    {
      localFile1 = new File(localFile2, paramString);
      break;
      localFile2 = getExternalMusicCacheDirectory(paramContext);
      continue;
      localFile2 = getInternalMusicCacheDirectory(paramContext);
      continue;
      localFile2 = getSecondaryExternalMusicCacheDirectory(paramContext);
    }
  }

  public static boolean setupSecondaryStorageLocation(Context paramContext, StorageLocation paramStorageLocation)
  {
    File localFile = paramStorageLocation.getCacheDirectory(paramContext);
    boolean bool;
    if (localFile.exists())
      if (!localFile.isDirectory())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Path given already exists and is not a folder: ");
        String str1 = localFile.getAbsolutePath();
        String str2 = str1;
        Log.w("CacheUtils", str2);
        bool = false;
      }
    while (true)
    {
      return bool;
      bool = true;
      continue;
      bool = localFile.mkdirs();
    }
  }

  public static class AlbumArtWorkCachePathResolver extends CacheUtils.ArtworkPathResolver
  {
    public AlbumArtWorkCachePathResolver(Context paramContext)
    {
      File localFile1 = CacheUtils.getExternalAlbumArtworkCacheDirectory(paramContext);
      this.mExternalCacheDirectory = localFile1;
      File localFile2 = CacheUtils.getInternalAlbumArtworkCacheDirectory(paramContext);
      this.mInternalCacheDirectory = localFile2;
    }
  }

  public static class NonAlbumArtWorkCachePathResolver extends CacheUtils.ArtworkPathResolver
  {
    public NonAlbumArtWorkCachePathResolver(Context paramContext)
    {
      File localFile1 = CacheUtils.getExternalCacheDirectory(paramContext, null);
      this.mExternalCacheDirectory = localFile1;
      File localFile2 = CacheUtils.getInternalCacheDirectory(paramContext, null);
      this.mInternalCacheDirectory = localFile2;
    }
  }

  public static abstract class ArtworkPathResolver
  {
    protected File mExternalCacheDirectory;
    protected File mInternalCacheDirectory;

    public File resolveArtworkPath(String paramString, int paramInt)
    {
      File localFile1 = null;
      switch (paramInt)
      {
      default:
        String str = "Unexpected storage type value: " + paramInt;
        Log.wtf("CacheUtils", str);
      case 0:
        if (localFile1 != null)
          break;
      case 2:
      case 1:
      }
      for (File localFile2 = null; ; localFile2 = new File(localFile1, paramString))
      {
        return localFile2;
        localFile1 = this.mExternalCacheDirectory;
        break;
        localFile1 = this.mInternalCacheDirectory;
        break;
      }
    }
  }

  public static enum StorageType
  {
    static
    {
      EXTERNAL = new StorageType("EXTERNAL", 1);
      SECONDARY_EXTERNAL = new StorageType("SECONDARY_EXTERNAL", 2);
      StorageType[] arrayOfStorageType = new StorageType[3];
      StorageType localStorageType1 = INTERNAL;
      arrayOfStorageType[0] = localStorageType1;
      StorageType localStorageType2 = EXTERNAL;
      arrayOfStorageType[1] = localStorageType2;
      StorageType localStorageType3 = SECONDARY_EXTERNAL;
      arrayOfStorageType[2] = localStorageType3;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cache.CacheUtils
 * JD-Core Version:    0.6.2
 */