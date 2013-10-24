package com.google.android.music.utils;

import com.google.android.music.log.Log;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FileSetProtector
{
  private final Set<String> mAbsolutePaths;
  private final Object mAbsolutePathsLock;

  public FileSetProtector(int paramInt)
  {
    Object localObject = new Object();
    this.mAbsolutePathsLock = localObject;
    HashSet localHashSet = new HashSet(paramInt);
    this.mAbsolutePaths = localHashSet;
  }

  public boolean deleteFileIfPossible(String paramString)
  {
    synchronized (this.mAbsolutePathsLock)
    {
      if (this.mAbsolutePaths.contains(paramString))
      {
        bool = false;
        return bool;
      }
      boolean bool = new File(paramString).delete();
      if (!bool)
      {
        String str = "Could not delete file " + paramString;
        Log.e("FileSetProtector", str);
      }
    }
  }

  public void registerAbsolutePath(String paramString)
  {
    synchronized (this.mAbsolutePathsLock)
    {
      boolean bool = this.mAbsolutePaths.add(paramString);
      return;
    }
  }

  public void unRegisterAbsolutePath(String paramString)
  {
    synchronized (this.mAbsolutePathsLock)
    {
      boolean bool = this.mAbsolutePaths.remove(paramString);
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.FileSetProtector
 * JD-Core Version:    0.6.2
 */