package com.google.android.play.analytics;

import android.util.Log;
import com.google.android.play.utils.Assertions;
import com.google.android.play.utils.FileModifiedDateComparator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class RollingFileStream<T>
{
  private final WriteCallbacks<T> mCallbacks;
  private File mCurrentOutputFile;
  private final File mDirectory;
  private final String mFileNamePrefix;
  private final String mFileNameSuffix;
  private FileOutputStream mFileOutputStream;
  private final long mMaxStorageSize;
  private final ArrayList<File> mReadFiles;
  private final long mRecommendedFileSize;
  private final ArrayList<File> mWrittenFiles;

  public RollingFileStream(File paramFile, String paramString1, String paramString2, long paramLong1, long paramLong2, WriteCallbacks<T> paramWriteCallbacks)
  {
    boolean bool2;
    boolean bool3;
    if (paramLong1 > 0L)
    {
      bool2 = true;
      Assertions.checkArgument(bool2, "recommendedFileSize must be positive");
      if (paramLong2 <= 0L)
        break label235;
      bool3 = true;
      label38: Assertions.checkArgument(bool3, "maxStorageSize must be positive");
      if (paramWriteCallbacks == null)
        break label241;
    }
    while (true)
    {
      Assertions.checkArgument(bool1, "callbacks cannot be null");
      this.mDirectory = paramFile;
      this.mFileNamePrefix = paramString1;
      this.mFileNameSuffix = paramString2;
      this.mRecommendedFileSize = paramLong1;
      this.mMaxStorageSize = paramLong2;
      this.mCallbacks = paramWriteCallbacks;
      createNewOutputFile();
      if (this.mCurrentOutputFile == null)
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("Could not create a temp file with prefix: \"");
        String str1 = this.mFileNamePrefix;
        StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append("\" and suffix: \"");
        String str2 = this.mFileNameSuffix;
        StringBuilder localStringBuilder3 = localStringBuilder2.append(str2).append("\" in dir: \"");
        String str3 = this.mDirectory.getAbsolutePath();
        String str4 = str3 + "\".";
        int i = Log.e("RollingFileStream", str4);
      }
      ArrayList localArrayList1 = new ArrayList();
      this.mWrittenFiles = localArrayList1;
      ArrayList localArrayList2 = new ArrayList();
      this.mReadFiles = localArrayList2;
      loadWrittenFiles();
      ensureMaxStorageSizeLimit();
      return;
      bool2 = false;
      break;
      label235: bool3 = false;
      break label38;
      label241: bool1 = false;
    }
  }

  private void createNewOutputFile()
  {
    if (!this.mDirectory.exists())
      boolean bool1 = this.mDirectory.mkdirs();
    this.mCurrentOutputFile = null;
    try
    {
      String str1 = this.mFileNamePrefix;
      String str2 = this.mFileNameSuffix;
      File localFile1 = this.mDirectory;
      File localFile2 = File.createTempFile(str1, str2, localFile1);
      this.mCurrentOutputFile = localFile2;
      File localFile3 = this.mCurrentOutputFile;
      FileOutputStream localFileOutputStream = new FileOutputStream(localFile3);
      this.mFileOutputStream = localFileOutputStream;
      this.mCallbacks.onNewOutputFile();
      return;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      if (this.mCurrentOutputFile != null)
        boolean bool2 = this.mCurrentOutputFile.delete();
      this.mCurrentOutputFile = null;
      return;
    }
    catch (IOException localIOException)
    {
    }
  }

  private void ensureMaxStorageSizeLimit()
  {
    long l1 = 0L;
    Iterator localIterator1 = this.mReadFiles.iterator();
    while (localIterator1.hasNext())
    {
      long l2 = ((File)localIterator1.next()).length();
      l1 += l2;
    }
    Iterator localIterator2 = this.mWrittenFiles.iterator();
    while (localIterator2.hasNext())
    {
      long l3 = ((File)localIterator2.next()).length();
      l1 += l3;
    }
    if (this.mCurrentOutputFile != null)
    {
      long l4 = this.mCurrentOutputFile.length();
      l1 += l4;
    }
    int i = 0;
    while (true)
    {
      long l5 = this.mMaxStorageSize;
      if (l1 <= l5)
        break;
      i += 1;
      if (this.mReadFiles.size() > 0)
      {
        File localFile1 = (File)this.mReadFiles.remove(0);
        long l6 = localFile1.length();
        l1 -= l6;
        boolean bool1 = localFile1.delete();
      }
      else if (this.mWrittenFiles.size() > 0)
      {
        File localFile2 = (File)this.mWrittenFiles.remove(0);
        long l7 = localFile2.length();
        l1 -= l7;
        boolean bool2 = localFile2.delete();
      }
      else if (this.mCurrentOutputFile != null)
      {
        long l8 = this.mCurrentOutputFile.length();
        l1 -= l8;
        boolean bool3 = this.mCurrentOutputFile.delete();
        this.mCurrentOutputFile = null;
      }
    }
    if (i <= 0)
      return;
    StringBuilder localStringBuilder = new StringBuilder().append(i).append(" files were purged due to exceeding total storage size of: ");
    long l9 = this.mMaxStorageSize;
    String str = l9;
    int j = Log.d("RollingFileStream", str);
  }

  private void loadWrittenFiles()
  {
    if (!this.mDirectory.exists())
      boolean bool1 = this.mDirectory.mkdirs();
    boolean bool2 = this.mDirectory.isDirectory();
    StringBuilder localStringBuilder = new StringBuilder().append("Expected a directory for path: ");
    String str1 = this.mDirectory.getAbsolutePath();
    String str2 = str1;
    Assertions.checkState(bool2, str2);
    this.mWrittenFiles.clear();
    File[] arrayOfFile = this.mDirectory.listFiles();
    int i = arrayOfFile.length;
    int j = 0;
    if (j < i)
    {
      File localFile1 = arrayOfFile[j];
      if (localFile1.isFile())
      {
        File localFile2 = this.mCurrentOutputFile;
        if (!localFile1.equals(localFile2))
        {
          if (localFile1.length() != 0L)
            break label155;
          boolean bool3 = localFile1.delete();
        }
      }
      while (true)
      {
        j += 1;
        break;
        label155: boolean bool4 = this.mWrittenFiles.add(localFile1);
      }
    }
    ArrayList localArrayList = this.mWrittenFiles;
    FileModifiedDateComparator localFileModifiedDateComparator = FileModifiedDateComparator.INSTANCE;
    Collections.sort(localArrayList, localFileModifiedDateComparator);
  }

  private boolean shouldStartNewOutputFile()
  {
    if (this.mCurrentOutputFile != null)
    {
      long l1 = this.mCurrentOutputFile.length();
      long l2 = this.mRecommendedFileSize;
      if (l1 < l2);
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private byte[] toByteArray(File paramFile)
    throws IOException
  {
    long l = paramFile.length();
    if (l > 2147483647L)
    {
      String str1 = "Too large to fit in a byte array: " + l;
      throw new OutOfMemoryError(str1);
    }
    byte[] arrayOfByte;
    if (l == 0L)
      arrayOfByte = new byte[0];
    while (true)
    {
      return arrayOfByte;
      FileInputStream localFileInputStream = new FileInputStream(paramFile);
      int i = (int)l;
      while (true)
      {
        int j;
        int n;
        try
        {
          arrayOfByte = new byte[i];
          j = 0;
          int k = arrayOfByte.length;
          if (j >= k)
            break;
          int m = arrayOfByte.length - j;
          n = localFileInputStream.read(arrayOfByte, j, m);
          if (n == -1)
          {
            StringBuilder localStringBuilder = new StringBuilder().append("Unexpected EOS: ");
            int i1 = arrayOfByte.length;
            String str2 = i1 + ", " + j;
            throw new IOException(str2);
          }
        }
        finally
        {
          localFileInputStream.close();
        }
        j += n;
      }
      localFileInputStream.close();
    }
  }

  public void deleteAllReadFiles()
  {
    Iterator localIterator = this.mReadFiles.iterator();
    while (localIterator.hasNext())
      boolean bool = ((File)localIterator.next()).delete();
    this.mReadFiles.clear();
  }

  public boolean hasUnreadFiles()
  {
    if (!this.mWrittenFiles.isEmpty());
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void markAllFilesAsUnread()
  {
    ArrayList localArrayList1 = this.mWrittenFiles;
    ArrayList localArrayList2 = this.mReadFiles;
    boolean bool = localArrayList1.addAll(localArrayList2);
    ArrayList localArrayList3 = this.mWrittenFiles;
    FileModifiedDateComparator localFileModifiedDateComparator = FileModifiedDateComparator.INSTANCE;
    Collections.sort(localArrayList3, localFileModifiedDateComparator);
    this.mReadFiles.clear();
  }

  public long peekNextReadLength()
  {
    if (this.mWrittenFiles.isEmpty());
    for (long l = 65535L; ; l = ((File)this.mWrittenFiles.get(0)).length())
      return l;
  }

  public byte[] read()
    throws IOException
  {
    byte[] arrayOfByte;
    if (this.mWrittenFiles.isEmpty())
    {
      int i = Log.e("RollingFileStream", "This method should never be called when there are no written files.");
      arrayOfByte = null;
    }
    while (true)
    {
      return arrayOfByte;
      File localFile = (File)this.mWrittenFiles.remove(0);
      arrayOfByte = toByteArray(localFile);
      boolean bool = this.mReadFiles.add(localFile);
    }
  }

  public long totalUnreadFileLength()
  {
    long l1 = 0L;
    int i = 0;
    while (true)
    {
      int j = this.mWrittenFiles.size();
      if (i >= j)
        break;
      long l2 = ((File)this.mWrittenFiles.get(i)).length();
      l1 += l2;
      i += 1;
    }
    return l1;
  }

  public boolean write(T paramT)
    throws IOException
  {
    boolean bool1 = false;
    if (this.mCurrentOutputFile == null)
    {
      createNewOutputFile();
      if (this.mCurrentOutputFile != null);
    }
    while (true)
    {
      return bool1;
      WriteCallbacks localWriteCallbacks = this.mCallbacks;
      FileOutputStream localFileOutputStream = this.mFileOutputStream;
      localWriteCallbacks.onWrite(paramT, localFileOutputStream);
      this.mFileOutputStream.flush();
      if (shouldStartNewOutputFile())
      {
        this.mFileOutputStream.close();
        ArrayList localArrayList = this.mWrittenFiles;
        File localFile = this.mCurrentOutputFile;
        boolean bool2 = localArrayList.add(localFile);
        createNewOutputFile();
        ensureMaxStorageSizeLimit();
        bool1 = true;
      }
    }
  }

  public static abstract interface WriteCallbacks<T>
  {
    public abstract void onNewOutputFile();

    public abstract void onWrite(T paramT, OutputStream paramOutputStream)
      throws IOException;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.play.analytics.RollingFileStream
 * JD-Core Version:    0.6.2
 */