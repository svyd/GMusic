package com.google.android.music.download.stream;

import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.DownloadRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PrefetchedContentCache
{
  private final Map<DownloadRequest, StreamingContent> mContentMap;

  public PrefetchedContentCache()
  {
    HashMap localHashMap = new HashMap();
    this.mContentMap = localHashMap;
  }

  public StreamingContent findContent(DownloadRequest paramDownloadRequest)
  {
    return (StreamingContent)this.mContentMap.get(paramDownloadRequest);
  }

  public List<StreamingContent> pruneNotMatching(ContentIdentifier[] paramArrayOfContentIdentifier)
  {
    LinkedList localLinkedList = new LinkedList();
    if (paramArrayOfContentIdentifier == null)
    {
      Collection localCollection = this.mContentMap.values();
      boolean bool1 = localLinkedList.addAll(localCollection);
      this.mContentMap.clear();
    }
    while (true)
    {
      return localLinkedList;
      List localList = Arrays.asList(paramArrayOfContentIdentifier);
      HashSet localHashSet = new HashSet(localList);
      Iterator localIterator1 = this.mContentMap.keySet().iterator();
      while (localIterator1.hasNext())
      {
        DownloadRequest localDownloadRequest1 = (DownloadRequest)localIterator1.next();
        ContentIdentifier localContentIdentifier = localDownloadRequest1.getId();
        if (!localHashSet.contains(localContentIdentifier))
        {
          Object localObject1 = this.mContentMap.get(localDownloadRequest1);
          boolean bool2 = localLinkedList.add(localObject1);
        }
      }
      Iterator localIterator2 = localLinkedList.iterator();
      while (localIterator2.hasNext())
      {
        StreamingContent localStreamingContent = (StreamingContent)localIterator2.next();
        Map localMap = this.mContentMap;
        DownloadRequest localDownloadRequest2 = localStreamingContent.getDownloadRequest();
        Object localObject2 = localMap.remove(localDownloadRequest2);
      }
    }
  }

  public boolean shouldFilter(String paramString)
  {
    Iterator localIterator = this.mContentMap.values().iterator();
    do
      if (!localIterator.hasNext())
        break;
    while (!((StreamingContent)localIterator.next()).shouldFilter(paramString));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public int size()
  {
    return this.mContentMap.size();
  }

  public void store(StreamingContent paramStreamingContent)
  {
    Map localMap = this.mContentMap;
    DownloadRequest localDownloadRequest = paramStreamingContent.getDownloadRequest();
    Object localObject = localMap.put(localDownloadRequest, paramStreamingContent);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.stream.PrefetchedContentCache
 * JD-Core Version:    0.6.2
 */