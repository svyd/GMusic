package com.google.android.music.download.artwork;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ArtIdRecorder<ArtId>
{
  private Set<ArtId> mIds;

  public Set<ArtId> extractIds()
  {
    Set localSet = this.mIds;
    this.mIds = null;
    return localSet;
  }

  public void report(ArtId paramArtId)
  {
    if (this.mIds == null)
    {
      HashSet localHashSet = new HashSet();
      this.mIds = localHashSet;
    }
    boolean bool = this.mIds.add(paramArtId);
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("[");
    if (this.mIds != null)
    {
      int i = 1;
      Iterator localIterator = this.mIds.iterator();
      if (localIterator.hasNext())
      {
        Object localObject = localIterator.next();
        if (i != 0)
          i = 0;
        while (true)
        {
          StringBuilder localStringBuilder3 = localStringBuilder1.append(localObject);
          break;
          StringBuilder localStringBuilder4 = localStringBuilder1.append(", ");
        }
      }
    }
    StringBuilder localStringBuilder5 = localStringBuilder1.append("]");
    return localStringBuilder1.toString();
  }

  public static class RemoteArtUrlRecorder extends ArtIdRecorder<String>
    implements RemoteUrlSink
  {
  }

  public static class AlbumIdRecorder extends ArtIdRecorder<Long>
    implements AlbumIdSink
  {
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.artwork.ArtIdRecorder
 * JD-Core Version:    0.6.2
 */