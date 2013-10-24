package com.google.android.music.download.cache;

import java.io.File;

public abstract interface FileSystem
{
  public abstract boolean delete(File paramFile);

  public abstract boolean exists(File paramFile);

  public abstract long getFreeSpace(File paramFile);

  public abstract long getLength(File paramFile);

  public abstract long getTotalSpace(File paramFile);
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cache.FileSystem
 * JD-Core Version:    0.6.2
 */