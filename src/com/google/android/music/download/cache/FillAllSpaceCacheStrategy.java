package com.google.android.music.download.cache;

import java.io.File;

public class FillAllSpaceCacheStrategy extends CacheStrategy
{
  private final FileSystem mFileSys;

  public FillAllSpaceCacheStrategy(FileSystem paramFileSystem)
  {
    this.mFileSys = paramFileSystem;
  }

  public long checkRequiredSpace(long paramLong, CacheLocation paramCacheLocation)
  {
    FileSystem localFileSystem = this.mFileSys;
    File localFile = paramCacheLocation.getPath();
    long l1 = localFileSystem.getFreeSpace(localFile);
    if (paramLong > l1);
    for (long l2 = paramLong - l1; ; l2 = 0L)
      return l2;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cache.FillAllSpaceCacheStrategy
 * JD-Core Version:    0.6.2
 */