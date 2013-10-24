package com.google.android.music.download.cache;

import com.google.android.music.store.Store;
import java.io.File;

public class FillUpToLimitCacheStrategy extends CacheStrategy
{
  private final FileSystem mFileSys;
  private final long mMaxSizeToUse;
  private final long mMinSizeToLeaveFree;
  private final float mPercentToUse;
  private final Store mStore;

  public FillUpToLimitCacheStrategy(FileSystem paramFileSystem, Store paramStore, float paramFloat, long paramLong1, long paramLong2)
  {
    this.mStore = paramStore;
    this.mFileSys = paramFileSystem;
    this.mMaxSizeToUse = paramLong1;
    this.mPercentToUse = paramFloat;
    this.mMinSizeToLeaveFree = paramLong2;
  }

  public long checkRequiredSpace(long paramLong, CacheLocation paramCacheLocation)
  {
    FileSystem localFileSystem1 = this.mFileSys;
    File localFile1 = paramCacheLocation.getPath();
    long l1 = localFileSystem1.getFreeSpace(localFile1);
    long l2 = this.mStore.getTotalCachedSize(100);
    FileSystem localFileSystem2 = this.mFileSys;
    File localFile2 = paramCacheLocation.getPath();
    long l3 = localFileSystem2.getTotalSpace(localFile2);
    long l4 = l2 + paramLong;
    float f1 = this.mPercentToUse;
    float f2 = (float)l3;
    long l5 = ()(f1 * f2);
    long l6 = this.mMaxSizeToUse;
    long l7 = Math.min(l5, l6);
    long l8 = l1 - paramLong;
    long l9 = this.mMinSizeToLeaveFree;
    long l10;
    if (l8 < l9)
      l10 = this.mMinSizeToLeaveFree - l8;
    while (true)
    {
      return l10;
      if (l4 > l7)
        l10 = l4 - l7;
      else if (paramLong > l1)
        l10 = paramLong - l1;
      else
        l10 = 0L;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cache.FillUpToLimitCacheStrategy
 * JD-Core Version:    0.6.2
 */