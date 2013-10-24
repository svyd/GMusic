package com.google.android.music.download.artwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import com.google.android.music.log.Log;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

public abstract class ArtDownloadServiceConnection<ArtId>
{
  private ArtDownloadServiceConnection<ArtId>.ArtSyncCompleteBroadcastReceiver mArtSyncCompleteBroadcastReceiver;
  private ArtDownloadServiceConnection<ArtId>.ArtWatchedBroadcastReceiver mArtWatchedBroadcastReceiver;
  private HashMap<ArtId, LinkedList<WeakReference<ArtChangeListener<ArtId>>>> mArtworkListeners;
  private Set<MissingArtHelper<ArtId>> mMissingArtHelpers;
  private ReferenceQueue<ArtChangeListener<ArtId>> mReferenceQueue;
  private HashMap<WeakReference<ArtChangeListener<ArtId>>, Set<ArtId>> mReverseArtListenerCache;

  public ArtDownloadServiceConnection()
  {
    HashMap localHashMap1 = Maps.newHashMap();
    this.mArtworkListeners = localHashMap1;
    ReferenceQueue localReferenceQueue = new ReferenceQueue();
    this.mReferenceQueue = localReferenceQueue;
    HashMap localHashMap2 = Maps.newHashMap();
    this.mReverseArtListenerCache = localHashMap2;
    HashSet localHashSet = Sets.newHashSet();
    this.mMissingArtHelpers = localHashSet;
  }

  private void addArtworkListenerLocked(ArtId paramArtId, LinkedList<WeakReference<ArtChangeListener<ArtId>>> paramLinkedList)
  {
    Object localObject = this.mArtworkListeners.put(paramArtId, paramLinkedList);
  }

  private void cleanArtListenerCache()
  {
    synchronized (this.mArtworkListeners)
    {
      Reference localReference;
      Set localSet;
      do
      {
        localReference = this.mReferenceQueue.poll();
        if (localReference == null)
          break;
        localSet = (Set)this.mReverseArtListenerCache.remove(localReference);
      }
      while (localSet == null);
      Iterator localIterator = localSet.iterator();
      Object localObject1;
      LinkedList localLinkedList;
      do
      {
        do
        {
          if (!localIterator.hasNext())
            break;
          localObject1 = localIterator.next();
          localLinkedList = (LinkedList)this.mArtworkListeners.get(localObject1);
        }
        while (localLinkedList == null);
        while (localLinkedList.remove(localReference));
      }
      while (!localLinkedList.isEmpty());
      removeArtworkListenerLocked(localObject1);
    }
  }

  private void createArtWatchedQueryBroadcastReceiver(Context paramContext)
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.google.android.music.ArtQueryWatched");
    int i = getArtQueryWatchedClientPriority();
    localIntentFilter.setPriority(i);
    ArtWatchedBroadcastReceiver localArtWatchedBroadcastReceiver1 = new ArtWatchedBroadcastReceiver(null);
    this.mArtWatchedBroadcastReceiver = localArtWatchedBroadcastReceiver1;
    ArtWatchedBroadcastReceiver localArtWatchedBroadcastReceiver2 = this.mArtWatchedBroadcastReceiver;
    Intent localIntent = paramContext.registerReceiver(localArtWatchedBroadcastReceiver2, localIntentFilter);
  }

  private void createSyncCompleteBroadcastReceiver(Context paramContext)
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.google.android.music.SYNC_COMPLETE");
    ArtSyncCompleteBroadcastReceiver localArtSyncCompleteBroadcastReceiver1 = new ArtSyncCompleteBroadcastReceiver(null);
    this.mArtSyncCompleteBroadcastReceiver = localArtSyncCompleteBroadcastReceiver1;
    ArtSyncCompleteBroadcastReceiver localArtSyncCompleteBroadcastReceiver2 = this.mArtSyncCompleteBroadcastReceiver;
    Intent localIntent = paramContext.registerReceiver(localArtSyncCompleteBroadcastReceiver2, localIntentFilter);
  }

  private ArrayList<String> getWatchedArtIdsExtra()
  {
    ArrayList localArrayList;
    synchronized (this.mArtworkListeners)
    {
      int i = this.mArtworkListeners.size();
      localArrayList = new ArrayList(i);
      Iterator localIterator = this.mArtworkListeners.keySet().iterator();
      if (localIterator.hasNext())
      {
        String str = localIterator.next().toString();
        boolean bool = localArrayList.add(str);
      }
    }
    return localArrayList;
  }

  private void removeArtworkListenerLocked(ArtId paramArtId)
  {
    Object localObject = this.mArtworkListeners.remove(paramArtId);
  }

  protected abstract int getArtQueryWatchedClientPriority();

  protected abstract String getWatchedArtIdListKey();

  protected void handleArtChanged(ArtId paramArtId)
  {
    Object localObject1 = null;
    synchronized (this.mArtworkListeners)
    {
      LinkedList localLinkedList = (LinkedList)this.mArtworkListeners.get(paramArtId);
      if (localLinkedList != null)
      {
        int i = localLinkedList.size();
        Iterator localIterator1 = localLinkedList.iterator();
        ArrayList localArrayList1 = null;
        ArrayList localArrayList2 = new ArrayList(i);
        while (true)
        {
          ArtChangeListener localArtChangeListener;
          try
          {
            if (!localIterator1.hasNext())
              break;
            WeakReference localWeakReference = (WeakReference)localIterator1.next();
            localArtChangeListener = (ArtChangeListener)localWeakReference.get();
            if (localArtChangeListener != null)
              break label131;
            if (localArrayList1 == null)
              localArrayList1 = new ArrayList(i);
            boolean bool1 = localArrayList1.add(localWeakReference);
            continue;
          }
          finally
          {
            ArrayList localArrayList3 = localArrayList2;
          }
          throw localObject2;
          label131: boolean bool2 = localArrayList2.add(localArtChangeListener);
        }
        if (localArrayList1 != null)
          boolean bool3 = localLinkedList.removeAll(localArrayList1);
        if (localLinkedList.isEmpty())
          removeArtworkListenerLocked(paramArtId);
        localObject1 = localArrayList2;
      }
      if (localObject1 == null)
        return;
      Iterator localIterator2 = localObject1.iterator();
      while (true)
      {
        if (!localIterator2.hasNext())
          return;
        ((ArtChangeListener)localIterator2.next()).notifyArtChanged(paramArtId);
      }
      return;
    }
  }

  public void onCreate(Context paramContext)
  {
    createArtWatchedQueryBroadcastReceiver(paramContext);
    createSyncCompleteBroadcastReceiver(paramContext);
  }

  public void register(MissingArtHelper<ArtId> paramMissingArtHelper)
  {
    synchronized (this.mMissingArtHelpers)
    {
      if (this.mMissingArtHelpers.contains(paramMissingArtHelper))
        return;
      boolean bool = this.mMissingArtHelpers.add(paramMissingArtHelper);
      return;
    }
  }

  public void registerArtChangeListener(ArtId paramArtId, ArtChangeListener<ArtId> paramArtChangeListener)
  {
    cleanArtListenerCache();
    synchronized (this.mArtworkListeners)
    {
      LinkedList localLinkedList = (LinkedList)this.mArtworkListeners.get(paramArtId);
      if (localLinkedList == null)
      {
        localLinkedList = new LinkedList();
        addArtworkListenerLocked(paramArtId, localLinkedList);
      }
      ReferenceQueue localReferenceQueue = this.mReferenceQueue;
      WeakReference localWeakReference = new WeakReference(paramArtChangeListener, localReferenceQueue);
      boolean bool1 = localLinkedList.add(localWeakReference);
      Object localObject1 = (Set)this.mReverseArtListenerCache.get(localWeakReference);
      if (localObject1 == null)
      {
        localObject1 = new TreeSet();
        Object localObject2 = this.mReverseArtListenerCache.put(localWeakReference, localObject1);
      }
      boolean bool2 = ((Set)localObject1).add(paramArtId);
      return;
    }
  }

  public void removeArtChangeListener(ArtId paramArtId, ArtChangeListener<ArtId> paramArtChangeListener)
  {
    LinkedList localLinkedList;
    ArrayList localArrayList;
    synchronized (this.mArtworkListeners)
    {
      localLinkedList = (LinkedList)this.mArtworkListeners.get(paramArtId);
      if (localLinkedList == null)
        break label197;
      Iterator localIterator = localLinkedList.iterator();
      localArrayList = null;
      while (localIterator.hasNext())
      {
        WeakReference localWeakReference = (WeakReference)localIterator.next();
        ArtChangeListener localArtChangeListener = (ArtChangeListener)localWeakReference.get();
        if ((localArtChangeListener == null) || (localArtChangeListener == paramArtChangeListener))
        {
          if (localArrayList == null)
          {
            int i = localLinkedList.size();
            localArrayList = new ArrayList(i);
          }
          boolean bool1 = localArrayList.add(localWeakReference);
          Set localSet = (Set)this.mReverseArtListenerCache.get(localWeakReference);
          if (localSet != null)
          {
            boolean bool2 = localSet.remove(paramArtId);
            if (localSet.isEmpty())
              Object localObject1 = this.mReverseArtListenerCache.remove(localWeakReference);
          }
        }
      }
    }
    if (localArrayList != null)
      boolean bool3 = localLinkedList.removeAll(localArrayList);
    if (localLinkedList.isEmpty())
      removeArtworkListenerLocked(paramArtId);
    label197:
  }

  protected void syncComplete()
  {
    synchronized (this.mMissingArtHelpers)
    {
      Iterator localIterator = this.mMissingArtHelpers.iterator();
      if (localIterator.hasNext())
        ((MissingArtHelper)localIterator.next()).syncComplete();
    }
  }

  public void unregister(MissingArtHelper<ArtId> paramMissingArtHelper)
  {
    synchronized (this.mMissingArtHelpers)
    {
      if (!this.mMissingArtHelpers.contains(paramMissingArtHelper))
        return;
      boolean bool = this.mMissingArtHelpers.remove(paramMissingArtHelper);
      return;
    }
  }

  public static abstract interface ArtChangeListener<ArtId>
  {
    public abstract void notifyArtChanged(ArtId paramArtId);
  }

  private class ArtSyncCompleteBroadcastReceiver extends BroadcastReceiver
  {
    private ArtSyncCompleteBroadcastReceiver()
    {
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      ArtDownloadServiceConnection.this.syncComplete();
    }
  }

  private class ArtWatchedBroadcastReceiver extends BroadcastReceiver
  {
    private ArtWatchedBroadcastReceiver()
    {
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      Bundle localBundle = getResultExtras(true);
      ArrayList localArrayList = ArtDownloadServiceConnection.this.getWatchedArtIdsExtra();
      String str1 = ArtDownloadServiceConnection.this.getWatchedArtIdListKey();
      if (localBundle.getStringArrayList(str1) != null)
      {
        String str2 = "This ArtWatchedBroadcastReceiver should be the only one setting " + str1;
        Log.e("ArtDownload", str2);
      }
      localBundle.putStringArrayList(str1, localArrayList);
      setResultExtras(localBundle);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.artwork.ArtDownloadServiceConnection
 * JD-Core Version:    0.6.2
 */