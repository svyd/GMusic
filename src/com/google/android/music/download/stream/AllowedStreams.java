package com.google.android.music.download.stream;

import com.google.android.music.download.DownloadRequest;
import com.google.android.music.log.Log;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AllowedStreams
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private final List<StreamingContent> mAllowed;

  public AllowedStreams()
  {
    LinkedList localLinkedList = new LinkedList();
    this.mAllowed = localLinkedList;
  }

  /** @deprecated */
  public StreamingContent findStreamByRequest(DownloadRequest paramDownloadRequest)
  {
    try
    {
      Iterator localIterator = this.mAllowed.iterator();
      StreamingContent localStreamingContent;
      while (true)
        if (localIterator.hasNext())
        {
          localStreamingContent = (StreamingContent)localIterator.next();
          if (localStreamingContent.hasRequest(paramDownloadRequest))
            if (LOGV)
            {
              String str1 = "findStreamByRequest: found " + localStreamingContent;
              Log.d("AllowedStreams", str1);
            }
        }
      while (true)
      {
        return localStreamingContent;
        if (LOGV)
        {
          String str2 = "findStreamByRequest: didn't find for request " + paramDownloadRequest;
          Log.d("AllowedStreams", str2);
        }
        localStreamingContent = null;
      }
    }
    finally
    {
    }
  }

  /** @deprecated */
  public StreamingContent findStreamBySecureId(long paramLong)
  {
    try
    {
      Iterator localIterator = this.mAllowed.iterator();
      StreamingContent localStreamingContent;
      while (true)
        if (localIterator.hasNext())
        {
          localStreamingContent = (StreamingContent)localIterator.next();
          long l = localStreamingContent.getSecureId();
          if (paramLong == l)
            if (LOGV)
            {
              String str = "findStreamById: found " + localStreamingContent;
              Log.d("AllowedStreams", str);
            }
        }
      while (true)
      {
        return localStreamingContent;
        if (LOGV)
          Log.d("AllowedStreams", "findStreamById: didn't find streaming content");
        localStreamingContent = null;
      }
    }
    finally
    {
    }
  }

  /** @deprecated */
  public void setAllowedStreams(StreamingContent paramStreamingContent1, StreamingContent paramStreamingContent2)
  {
    try
    {
      if (LOGV)
      {
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = paramStreamingContent1;
        arrayOfObject[1] = paramStreamingContent2;
        String str = String.format("setAllowedStreams: current=%s next=%s", arrayOfObject);
        Log.d("AllowedStreams", str);
      }
      this.mAllowed.clear();
      if (paramStreamingContent1 != null)
        boolean bool1 = this.mAllowed.add(paramStreamingContent1);
      if (paramStreamingContent2 != null)
        boolean bool2 = this.mAllowed.add(paramStreamingContent2);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  public String toString()
  {
    StringBuilder localStringBuilder1;
    try
    {
      localStringBuilder1 = new StringBuilder();
      StringBuilder localStringBuilder2 = localStringBuilder1.append("(");
      Iterator localIterator = this.mAllowed.iterator();
      while (localIterator.hasNext())
      {
        String str1 = ((StreamingContent)localIterator.next()).toString();
        StringBuilder localStringBuilder3 = localStringBuilder1.append(str1);
        StringBuilder localStringBuilder4 = localStringBuilder1.append(",");
      }
    }
    finally
    {
    }
    StringBuilder localStringBuilder5 = localStringBuilder1.append(")");
    String str2 = localStringBuilder1.toString();
    String str3 = str2;
    return str3;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.stream.AllowedStreams
 * JD-Core Version:    0.6.2
 */