package com.google.android.play.utils;

import java.io.File;
import java.util.Comparator;

public class FileModifiedDateComparator
  implements Comparator<File>
{
  public static final FileModifiedDateComparator INSTANCE = new FileModifiedDateComparator();

  public int compare(File paramFile1, File paramFile2)
  {
    long l1 = paramFile1.lastModified();
    long l2 = paramFile2.lastModified();
    int i;
    if (l1 == l2)
      i = 0;
    while (true)
    {
      return i;
      long l3 = paramFile1.lastModified();
      long l4 = paramFile2.lastModified();
      if (l3 < l4)
        i = -1;
      else
        i = 1;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.play.utils.FileModifiedDateComparator
 * JD-Core Version:    0.6.2
 */