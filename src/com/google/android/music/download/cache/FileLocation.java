package com.google.android.music.download.cache;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.File;

public final class FileLocation
  implements Parcelable
{
  public static final Parcelable.Creator<FileLocation> CREATOR = new Parcelable.Creator()
  {
    public FileLocation createFromParcel(Parcel paramAnonymousParcel)
    {
      return new FileLocation(paramAnonymousParcel, null);
    }

    public FileLocation[] newArray(int paramAnonymousInt)
    {
      return new FileLocation[paramAnonymousInt];
    }
  };
  private final int mCacheType;
  private final File mFullPath;
  private final CacheUtils.StorageType mStorageType;

  private FileLocation(Parcel paramParcel)
  {
    String str = paramParcel.readString();
    File localFile = new File(str);
    this.mFullPath = localFile;
    CacheUtils.StorageType[] arrayOfStorageType = CacheUtils.StorageType.values();
    int i = paramParcel.readInt();
    CacheUtils.StorageType localStorageType = arrayOfStorageType[i];
    this.mStorageType = localStorageType;
    int j = paramParcel.readInt();
    this.mCacheType = j;
  }

  public FileLocation(File paramFile, CacheUtils.StorageType paramStorageType, int paramInt)
  {
    if (paramFile == null)
      throw new IllegalArgumentException("The full path provided is null");
    this.mFullPath = paramFile;
    this.mStorageType = paramStorageType;
    this.mCacheType = paramInt;
  }

  public int describeContents()
  {
    return 0;
  }

  public int getCacheType()
  {
    return this.mCacheType;
  }

  public File getFullPath()
  {
    return this.mFullPath;
  }

  public int getSchemaValueForCacheType()
  {
    int i = 0;
    if (this.mCacheType == 2)
      i = 100;
    while (true)
    {
      return i;
      if (this.mCacheType == 3)
        i = 200;
    }
  }

  public int getSchemaValueForStorageType()
  {
    return CacheUtils.getSchemaValueForStorageType(this.mStorageType);
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("[");
    StringBuilder localStringBuilder3 = localStringBuilder1.append("mFullPath=");
    String str = this.mFullPath.getAbsolutePath();
    StringBuilder localStringBuilder4 = localStringBuilder3.append(str);
    StringBuilder localStringBuilder5 = localStringBuilder1.append(" mStorageType=");
    CacheUtils.StorageType localStorageType = this.mStorageType;
    StringBuilder localStringBuilder6 = localStringBuilder5.append(localStorageType);
    StringBuilder localStringBuilder7 = localStringBuilder1.append(" mCacheType=");
    int i = this.mCacheType;
    StringBuilder localStringBuilder8 = localStringBuilder7.append(i);
    StringBuilder localStringBuilder9 = localStringBuilder1.append("]");
    return localStringBuilder1.toString();
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    String str = this.mFullPath.getAbsolutePath();
    paramParcel.writeString(str);
    int i = this.mStorageType.ordinal();
    paramParcel.writeInt(i);
    int j = this.mCacheType;
    paramParcel.writeInt(j);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cache.FileLocation
 * JD-Core Version:    0.6.2
 */