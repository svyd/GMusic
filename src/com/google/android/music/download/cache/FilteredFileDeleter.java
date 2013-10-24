package com.google.android.music.download.cache;

import com.google.android.music.download.ContentIdentifier;
import java.io.File;
import java.util.Set;

public abstract interface FilteredFileDeleter
{
  public abstract Set<ContentIdentifier> getFilteredIds();

  public abstract boolean requestDeleteFile(File paramFile);
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cache.FilteredFileDeleter
 * JD-Core Version:    0.6.2
 */