package com.google.android.music.download.cache;

import android.content.Context;
import android.os.StatFs;
import java.io.File;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;

public class StorageLocation
{
  public static final Comparator<StorageLocation> SIZE_SORT_COMPARATOR = new Comparator()
  {
    public int compare(StorageLocation paramAnonymousStorageLocation1, StorageLocation paramAnonymousStorageLocation2)
    {
      long l = paramAnonymousStorageLocation1.mFreeSpace >> 20;
      return (int)((paramAnonymousStorageLocation2.mFreeSpace >> 20) - l);
    }
  };
  public static StorageLocation sInternal;
  private static final HashMap<String, StorageLocation> sKnownLocations;
  private static final StatFs sStatFs = new StatFs("/");
  public String mDescription;
  public final EnumSet<StorageProbeUtils.ProbeMethod> mFoundBy;
  public long mFreeSpace;
  public boolean mIsEmulated;
  public boolean mIsInternal = false;
  public boolean mIsMounted;
  public boolean mIsRemovable;
  public final String mMountPoint;
  public long mTotalSpace;

  static
  {
    sKnownLocations = new HashMap();
  }

  private StorageLocation(String paramString, StorageProbeUtils.ProbeMethod paramProbeMethod, boolean paramBoolean)
  {
    this.mMountPoint = paramString;
    EnumSet localEnumSet1;
    if (paramProbeMethod != null)
      localEnumSet1 = EnumSet.of(paramProbeMethod);
    EnumSet localEnumSet2;
    for (this.mFoundBy = localEnumSet1; ; this.mFoundBy = localEnumSet2)
    {
      if (!paramBoolean)
        return;
      Object localObject = sKnownLocations.put(paramString, this);
      return;
      localEnumSet2 = EnumSet.noneOf(StorageProbeUtils.ProbeMethod.class);
    }
  }

  public static StorageLocation get(String paramString, StorageProbeUtils.ProbeMethod paramProbeMethod)
  {
    StorageLocation localStorageLocation;
    if (sKnownLocations.containsKey(paramString))
    {
      localStorageLocation = (StorageLocation)sKnownLocations.get(paramString);
      if (localStorageLocation.mFoundBy != null)
        boolean bool = localStorageLocation.mFoundBy.add(paramProbeMethod);
    }
    while (true)
    {
      StatFs localStatFs = sStatFs;
      String str = localStorageLocation.mMountPoint;
      localStatFs.restat(str);
      long l1 = sStatFs.getAvailableBlocks();
      long l2 = sStatFs.getBlockSize();
      long l3 = l1 * l2;
      localStorageLocation.mFreeSpace = l3;
      long l4 = sStatFs.getBlockCount();
      long l5 = sStatFs.getBlockSize();
      long l6 = l4 * l5;
      localStorageLocation.mTotalSpace = l6;
      return localStorageLocation;
      localStorageLocation = new StorageLocation(paramString, paramProbeMethod, true);
    }
  }

  public static StorageLocation getInternal(Context paramContext)
  {
    StorageLocation localStorageLocation;
    if (sInternal != null)
      localStorageLocation = sInternal;
    while (true)
    {
      return localStorageLocation;
      String str1 = CacheUtils.getInternalCacheDirectory(paramContext, null).getAbsolutePath();
      localStorageLocation = new StorageLocation(str1, null, false);
      String str2 = paramContext.getString(2131231379);
      localStorageLocation.mDescription = str2;
      localStorageLocation.mIsMounted = true;
      localStorageLocation.mIsInternal = true;
      StatFs localStatFs = sStatFs;
      String str3 = localStorageLocation.mMountPoint;
      localStatFs.restat(str3);
      long l1 = sStatFs.getAvailableBlocks();
      long l2 = sStatFs.getBlockSize();
      long l3 = l1 * l2;
      localStorageLocation.mFreeSpace = l3;
      long l4 = sStatFs.getBlockCount();
      long l5 = sStatFs.getBlockSize();
      long l6 = l4 * l5;
      localStorageLocation.mTotalSpace = l6;
      sInternal = localStorageLocation;
    }
  }

  public File getCacheDirectory(Context paramContext)
  {
    String str1;
    if (this.mIsInternal)
      str1 = this.mMountPoint;
    String str2;
    File localFile2;
    for (File localFile1 = new File(str1); ; localFile1 = new File(localFile2, str2))
    {
      return localFile1;
      str2 = paramContext.getPackageName();
      String str3 = this.mMountPoint;
      localFile2 = new File(str3, "Android/data/");
    }
  }

  public String toString()
  {
    Object[] arrayOfObject = new Object[2];
    String str = this.mMountPoint;
    arrayOfObject[0] = str;
    EnumSet localEnumSet = this.mFoundBy;
    arrayOfObject[1] = localEnumSet;
    return String.format("StorageLocation{mMountPoint='%s', mFoundBy='%s'}", arrayOfObject);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cache.StorageLocation
 * JD-Core Version:    0.6.2
 */