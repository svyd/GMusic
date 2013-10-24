package com.google.android.music;

import android.os.RemoteException;
import com.google.android.music.log.Log;
import com.google.android.music.store.IStoreService;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.SafeServiceConnection;
import com.google.android.music.utils.async.AsyncRunner;
import com.google.common.collect.ImmutableList;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class AvailableSpaceTracker
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.UI);
  private LinkedList<WeakReference<AlbumChangedListener>> mAlbumListeners;
  private final Object mAlbumLock;
  private boolean mAvailabilityTransition;
  private final TreeSet<Long> mDeselectedAlbums;
  private final TreeSet<Long> mDeselectedAutoPlaylists;
  private final TreeSet<Long> mDeselectedPlaylists;
  private long mFreeSpaceChange;
  private long mFreeSpaceOnDevice;
  private boolean mIsActive;
  private LinkedList<WeakReference<AvailableSpaceChangedListener>> mListeners;
  private LinkedList<WeakReference<PlaylistChangedListener>> mPlaylistListeners;
  private final TreeSet<Long> mSelectedAlbums;
  private final TreeSet<Long> mSelectedAutoPlaylists;
  private final TreeSet<Long> mSelectedPlaylists;
  private Object mSpaceVariablesLock;
  private SafeServiceConnection mStoreSafeConnection;
  private IStoreService mStoreService;
  private long mTotalSpace;

  private void checkActive()
  {
    if (this.mIsActive)
      return;
    throw new IllegalStateException("Can not make changes once a session is committed or cancelled");
  }

  private boolean clearAlbum(long paramLong)
  {
    synchronized (this.mAlbumLock)
    {
      TreeSet localTreeSet1 = this.mSelectedAlbums;
      TreeSet localTreeSet2 = this.mDeselectedAlbums;
      boolean bool = moveIdFromTo(paramLong, localTreeSet1, localTreeSet2);
      return bool;
    }
  }

  private void clearAlbumAndNotify(long paramLong)
  {
    if (!clearAlbum(paramLong))
      return;
    ImmutableList localImmutableList = ImmutableList.of(Long.valueOf(paramLong));
    notifyAlbumChangedListeners(localImmutableList);
    submitUpdateFreespaceTask(paramLong, 1, false);
  }

  private boolean isAvailableSpaceInitialized()
  {
    synchronized (this.mSpaceVariablesLock)
    {
      if ((this.mFreeSpaceOnDevice != 0L) && (this.mTotalSpace != 0L))
      {
        bool = true;
        return bool;
      }
      boolean bool = false;
    }
  }

  private boolean markAlbum(long paramLong)
  {
    synchronized (this.mAlbumLock)
    {
      TreeSet localTreeSet1 = this.mDeselectedAlbums;
      TreeSet localTreeSet2 = this.mSelectedAlbums;
      boolean bool = moveIdFromTo(paramLong, localTreeSet1, localTreeSet2);
      return bool;
    }
  }

  private void markAlbumAndNotify(long paramLong)
  {
    if (!markAlbum(paramLong))
      return;
    ImmutableList localImmutableList = ImmutableList.of(Long.valueOf(paramLong));
    notifyAlbumChangedListeners(localImmutableList);
    submitUpdateFreespaceTask(paramLong, 1, true);
  }

  private boolean moveIdFromTo(long paramLong, TreeSet<Long> paramTreeSet1, TreeSet<Long> paramTreeSet2)
  {
    Long localLong1 = Long.valueOf(paramLong);
    boolean bool1 = paramTreeSet2.add(localLong1);
    Long localLong2 = Long.valueOf(paramLong);
    boolean bool2 = paramTreeSet1.remove(localLong2);
    return bool1 | bool2;
  }

  private void notifyAlbumChangedListeners(List<Long> paramList)
  {
    while (true)
    {
      AlbumChangedListener localAlbumChangedListener;
      synchronized (this.mAlbumListeners)
      {
        Iterator localIterator = this.mAlbumListeners.iterator();
        if (!localIterator.hasNext())
          break;
        localAlbumChangedListener = (AlbumChangedListener)((WeakReference)localIterator.next()).get();
        if (localAlbumChangedListener == null)
          localIterator.remove();
      }
      localAlbumChangedListener.onAlbumChanged(paramList);
    }
  }

  private void notifyAvailableSpaceChangedListener()
  {
    while (true)
    {
      long l1;
      long l2;
      boolean bool;
      AvailableSpaceChangedListener localAvailableSpaceChangedListener;
      synchronized (this.mSpaceVariablesLock)
      {
        l1 = this.mTotalSpace;
        l2 = getCombinedFreeSpace();
        bool = this.mAvailabilityTransition;
        this.mAvailabilityTransition = false;
        synchronized (this.mListeners)
        {
          Iterator localIterator = this.mListeners.iterator();
          if (!localIterator.hasNext())
            break;
          localAvailableSpaceChangedListener = (AvailableSpaceChangedListener)((WeakReference)localIterator.next()).get();
          if (localAvailableSpaceChangedListener == null)
            localIterator.remove();
        }
      }
      localAvailableSpaceChangedListener.onAvailableSpaceChanged(l1, l2, bool);
    }
  }

  private void notifyPlaylistChangedListeners(List<Long> paramList)
  {
    while (true)
    {
      PlaylistChangedListener localPlaylistChangedListener;
      synchronized (this.mPlaylistListeners)
      {
        Iterator localIterator = this.mPlaylistListeners.iterator();
        if (!localIterator.hasNext())
          break;
        localPlaylistChangedListener = (PlaylistChangedListener)((WeakReference)localIterator.next()).get();
        if (localPlaylistChangedListener == null)
          localIterator.remove();
      }
      localPlaylistChangedListener.onPlaylistChanged(paramList);
    }
  }

  private void submitUpdateFreespaceTask(long paramLong, int paramInt, boolean paramBoolean)
  {
    ArrayList localArrayList = new ArrayList(1);
    Long localLong = Long.valueOf(paramLong);
    boolean bool = localArrayList.add(localLong);
    submitUpdateFreespaceTask(localArrayList, paramInt, paramBoolean);
  }

  private void submitUpdateFreespaceTask(List<Long> paramList, int paramInt, boolean paramBoolean)
  {
    MusicUtils.assertUiThread();
    if (!isAvailableSpaceInitialized())
      return;
    MusicUtils.runAsyncWithCallback(new UpdateFreespaceTask(paramList, paramInt, paramBoolean));
  }

  private void waitForStoreConnection()
  {
    synchronized (this.mStoreSafeConnection)
    {
      IStoreService localIStoreService = this.mStoreService;
      if (localIStoreService != null);
    }
    try
    {
      this.mStoreSafeConnection.wait(10000L);
      label26: if (this.mStoreService == null)
      {
        throw new IllegalStateException("Could not connect to store service");
        localObject = finally;
        throw localObject;
      }
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
      break label26;
    }
  }

  public void deselectAlbum(long paramLong)
  {
    checkActive();
    clearAlbumAndNotify(paramLong);
  }

  public void deselectAutoPlaylist(long paramLong)
  {
    checkActive();
    TreeSet localTreeSet1 = this.mSelectedAutoPlaylists;
    Long localLong1 = Long.valueOf(paramLong);
    if (!localTreeSet1.remove(localLong1))
    {
      TreeSet localTreeSet2 = this.mDeselectedAutoPlaylists;
      Long localLong2 = Long.valueOf(paramLong);
      boolean bool = localTreeSet2.add(localLong2);
    }
    submitUpdateFreespaceTask(paramLong, 3, false);
    ImmutableList localImmutableList = ImmutableList.of(Long.valueOf(paramLong));
    notifyPlaylistChangedListeners(localImmutableList);
  }

  public void deselectPlaylist(long paramLong)
  {
    checkActive();
    TreeSet localTreeSet1 = this.mSelectedPlaylists;
    Long localLong1 = Long.valueOf(paramLong);
    if (!localTreeSet1.remove(localLong1))
    {
      TreeSet localTreeSet2 = this.mDeselectedPlaylists;
      Long localLong2 = Long.valueOf(paramLong);
      boolean bool = localTreeSet2.add(localLong2);
    }
    submitUpdateFreespaceTask(paramLong, 2, false);
    ImmutableList localImmutableList = ImmutableList.of(Long.valueOf(paramLong));
    notifyPlaylistChangedListeners(localImmutableList);
  }

  public long getCombinedFreeSpace()
  {
    long l1 = this.mFreeSpaceOnDevice;
    long l2 = this.mFreeSpaceChange;
    return l1 + l2;
  }

  public void selectAlbum(long paramLong)
  {
    checkActive();
    markAlbumAndNotify(paramLong);
  }

  public void selectAutoPlaylist(long paramLong)
  {
    if (LOGV)
    {
      String str = "selectAutoPlaylist: id=" + paramLong;
      Log.d("AvailableSpaceTracker", str);
    }
    checkActive();
    TreeSet localTreeSet = this.mSelectedAutoPlaylists;
    Long localLong = Long.valueOf(paramLong);
    boolean bool = localTreeSet.add(localLong);
    submitUpdateFreespaceTask(paramLong, 3, true);
    ImmutableList localImmutableList = ImmutableList.of(Long.valueOf(paramLong));
    notifyPlaylistChangedListeners(localImmutableList);
  }

  public void selectPlaylist(long paramLong)
  {
    checkActive();
    TreeSet localTreeSet = this.mSelectedPlaylists;
    Long localLong = Long.valueOf(paramLong);
    boolean bool = localTreeSet.add(localLong);
    submitUpdateFreespaceTask(paramLong, 2, true);
    ImmutableList localImmutableList = ImmutableList.of(Long.valueOf(paramLong));
    notifyPlaylistChangedListeners(localImmutableList);
  }

  public static abstract interface AvailableSpaceChangedListener
  {
    public abstract void onAvailableSpaceChanged(long paramLong1, long paramLong2, boolean paramBoolean);
  }

  private class UpdateFreespaceTask
    implements AsyncRunner
  {
    private boolean mAdded;
    private final List<Long> mIds;
    private final int mType;

    public UpdateFreespaceTask(int paramBoolean, boolean arg3)
    {
      this.mIds = paramBoolean;
      int i;
      this.mType = i;
      boolean bool;
      this.mAdded = bool;
    }

    public void backgroundTask()
    {
      int i = 1;
      long l1 = 0L;
      AvailableSpaceTracker.this.waitForStoreConnection();
      while (true)
      {
        long l2;
        try
        {
          Iterator localIterator = this.mIds.iterator();
          if (!localIterator.hasNext())
            break;
          l2 = ((Long)localIterator.next()).longValue();
          switch (this.mType)
          {
          default:
            StringBuilder localStringBuilder = new StringBuilder().append("Unknown type: ");
            int j = this.mType;
            String str = j;
            throw new IllegalArgumentException(str);
          case 1:
          case 2:
          case 3:
          }
        }
        catch (RemoteException localRemoteException)
        {
          Log.e("AvailableSpaceTracker", "Remote error when trying to get size to download", localRemoteException);
          return;
        }
        long l3 = AvailableSpaceTracker.this.mStoreService.getSizeAlbum(l2);
        l1 += l3;
        continue;
        long l4 = AvailableSpaceTracker.this.mStoreService.getSizePlaylist(l2);
        l1 += l4;
        continue;
        long l5 = AvailableSpaceTracker.this.mStoreService.getSizeAutoPlaylist(l2);
        long l6 = l5;
        l1 += l6;
      }
      while (true)
      {
        synchronized (AvailableSpaceTracker.this.mSpaceVariablesLock)
        {
          if (AvailableSpaceTracker.this.getCombinedFreeSpace() > 0L)
          {
            if (!this.mAdded)
              break label298;
            long l7 = AvailableSpaceTracker.access$1022(AvailableSpaceTracker.this, l1);
            if ((i != 0) && (AvailableSpaceTracker.this.getCombinedFreeSpace() <= 0L))
              boolean bool1 = AvailableSpaceTracker.access$1102(AvailableSpaceTracker.this, true);
            return;
          }
        }
        i = 0;
        continue;
        label298: long l8 = AvailableSpaceTracker.access$1014(AvailableSpaceTracker.this, l1);
        if ((i == 0) && (AvailableSpaceTracker.this.getCombinedFreeSpace() > 0L))
          boolean bool2 = AvailableSpaceTracker.access$1102(AvailableSpaceTracker.this, true);
      }
    }

    public void taskCompleted()
    {
      AvailableSpaceTracker.this.notifyAvailableSpaceChangedListener();
      AvailableSpaceTracker localAvailableSpaceTracker = AvailableSpaceTracker.this;
      if (this.mType == 1);
      for (List localList = this.mIds; ; localList = null)
      {
        localAvailableSpaceTracker.notifyAlbumChangedListeners(localList);
        return;
      }
    }
  }

  public static abstract interface PlaylistChangedListener
  {
    public abstract void onPlaylistChanged(List<Long> paramList);
  }

  public static abstract interface AlbumChangedListener
  {
    public abstract void onAlbumChanged(List<Long> paramList);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.AvailableSpaceTracker
 * JD-Core Version:    0.6.2
 */