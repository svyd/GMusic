package com.google.android.music.download.cache;

import android.os.StatFs;
import java.io.File;

public class FileSystemImpl
  implements FileSystem
{
  private final FilteredFileDeleter mFilteredFileDeleter;

  public FileSystemImpl(FilteredFileDeleter paramFilteredFileDeleter)
  {
    this.mFilteredFileDeleter = paramFilteredFileDeleter;
  }

  public boolean delete(File paramFile)
  {
    return this.mFilteredFileDeleter.requestDeleteFile(paramFile);
  }

  public boolean exists(File paramFile)
  {
    return paramFile.exists();
  }

  public long getFreeSpace(File paramFile)
  {
    String str = paramFile.getAbsolutePath();
    StatFs localStatFs = new StatFs(str);
    long l1 = localStatFs.getAvailableBlocks();
    long l2 = localStatFs.getBlockSize();
    return l1 * l2;
  }

  public long getLength(File paramFile)
  {
    return paramFile.length();
  }

  public long getTotalSpace(File paramFile)
  {
    String str = paramFile.getAbsolutePath();
    StatFs localStatFs = new StatFs(str);
    long l1 = localStatFs.getBlockCount();
    long l2 = localStatFs.getBlockSize();
    return l1 * l2;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cache.FileSystemImpl
 * JD-Core Version:    0.6.2
 */