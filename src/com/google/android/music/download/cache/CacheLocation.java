package com.google.android.music.download.cache;

import java.io.File;

public final class CacheLocation
{
  private final File mPath;
  private final CacheUtils.StorageType mStorageType;

  public CacheLocation(File paramFile, CacheUtils.StorageType paramStorageType)
  {
    if (paramFile == null)
      throw new IllegalArgumentException("The path provided is null");
    this.mPath = paramFile;
    this.mStorageType = paramStorageType;
  }

  public File getCacheFile(String paramString)
  {
    File localFile = this.mPath;
    return new File(localFile, paramString);
  }

  public File getPath()
  {
    return this.mPath;
  }

  public int getSchemaValueForStorageType()
  {
    return CacheUtils.getSchemaValueForStorageType(this.mStorageType);
  }

  public CacheUtils.StorageType getStorageType()
  {
    return this.mStorageType;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("path=");
    String str = this.mPath.getAbsolutePath();
    StringBuilder localStringBuilder3 = localStringBuilder2.append(str).append(" ");
    StringBuilder localStringBuilder4 = localStringBuilder1.append("type=");
    CacheUtils.StorageType localStorageType = this.mStorageType;
    StringBuilder localStringBuilder5 = localStringBuilder4.append(localStorageType);
    return localStringBuilder1.toString();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cache.CacheLocation
 * JD-Core Version:    0.6.2
 */