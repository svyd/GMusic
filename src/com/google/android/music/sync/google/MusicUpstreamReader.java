package com.google.android.music.sync.google;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;
import com.google.android.common.base.Strings;
import com.google.android.music.store.MusicFile;
import com.google.android.music.store.MusicFileTombstone;
import com.google.android.music.store.PlayList;
import com.google.android.music.store.PlayList.Item;
import com.google.android.music.store.RadioStation;
import com.google.android.music.store.Store;
import com.google.android.music.sync.common.AbstractSyncAdapter.UpstreamQueue;
import com.google.android.music.sync.common.ClosableBlockingQueue.QueueClosedException;
import com.google.android.music.sync.common.SoftSyncException;
import com.google.android.music.sync.common.UpstreamReader;
import com.google.android.music.sync.google.model.MusicQueueableSyncEntity;
import com.google.android.music.sync.google.model.SyncablePlaylist;
import com.google.android.music.sync.google.model.SyncablePlaylistEntry;
import com.google.android.music.sync.google.model.SyncableRadioStation;
import com.google.android.music.sync.google.model.Track;
import com.google.android.music.sync.google.model.TrackStat;
import com.google.android.music.sync.google.model.TrackTombstone;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MusicUpstreamReader extends UpstreamReader
{
  private final int mAccountId;
  private final boolean mEnableTrackDeletes;
  private final Store mStore;
  private final String mTag;
  private final boolean mUseVerboseLogging;

  public MusicUpstreamReader(AbstractSyncAdapter.UpstreamQueue paramUpstreamQueue, Context paramContext, Map<String, Object> paramMap, String paramString, boolean paramBoolean)
  {
    super(paramUpstreamQueue, paramString);
    this.mTag = paramString;
    Store localStore = Store.getInstance(paramContext);
    this.mStore = localStore;
    boolean bool = DebugUtils.isLoggable(DebugUtils.MusicTag.SYNC);
    this.mUseVerboseLogging = bool;
    Object localObject = paramMap.get("remote_account");
    int i = ((Integer)Integer.class.cast(localObject)).intValue();
    this.mAccountId = i;
    this.mEnableTrackDeletes = paramBoolean;
  }

  private int fillQueueWithEntryChanges()
    throws SoftSyncException
  {
    try
    {
      localSQLiteDatabase = this.mStore.beginRead();
      localCursor = PlayList.Item.getItemsToSync(localSQLiteDatabase);
      int i = 0;
      if (localCursor.moveToFirst());
      while (true)
      {
        i += 1;
        PlayList.Item localItem1 = new PlayList.Item();
        PlayList.Item localItem2 = new PlayList.Item();
        PlayList.Item localItem3 = new PlayList.Item();
        localItem1.populateFromFullProjectionCursor(localCursor);
        try
        {
          long l = localItem1.getMusicId();
          int j = Store.getSourceIdAndTypeForMusicId(localSQLiteDatabase, l);
          int k = j;
          String str1 = (String)k.first;
          int m = ((Integer)k.second).intValue();
          SyncablePlaylistEntry localSyncablePlaylistEntry = SyncablePlaylistEntry.parse(str1, m, localItem1);
          int n = this.mAccountId;
          if (localItem1.findPrecedingItem(localSQLiteDatabase, localItem2, n) != null)
          {
            String str2 = localItem2.getClientId();
            localSyncablePlaylistEntry.mPrecedingEntryClientId = str2;
          }
          int i1 = this.mAccountId;
          if (localItem1.findFollowingItem(localSQLiteDatabase, localItem3, i1) != null)
          {
            String str3 = localItem3.getClientId();
            localSyncablePlaylistEntry.mFollowingEntryClientId = str3;
          }
          boolean bool2 = putItemInQueue(localSyncablePlaylistEntry);
          boolean bool1 = localCursor.moveToNext();
          if (bool1)
            continue;
          return i;
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          throw new SoftSyncException(localFileNotFoundException);
        }
      }
    }
    finally
    {
      SQLiteDatabase localSQLiteDatabase;
      Cursor localCursor;
      if (localCursor != null)
        localCursor.close();
      if (localSQLiteDatabase != null)
        this.mStore.endRead(localSQLiteDatabase);
    }
  }

  private int fillQueueWithEntryTombstones()
    throws SoftSyncException
  {
    try
    {
      localSQLiteDatabase = this.mStore.beginRead();
      localCursor = PlayList.Item.getItemTombstones(localSQLiteDatabase);
      int i = 0;
      if (localCursor.moveToFirst())
      {
        boolean bool2;
        do
        {
          i += 1;
          PlayList.Item localItem = new PlayList.Item();
          localItem.populateFromTombstoneProjectionCursor(localCursor);
          SyncablePlaylistEntry localSyncablePlaylistEntry = SyncablePlaylistEntry.parse(localItem);
          localSyncablePlaylistEntry.mIsDeleted = true;
          boolean bool1 = putItemInQueue(localSyncablePlaylistEntry);
          bool2 = localCursor.moveToNext();
        }
        while (bool2);
      }
      return i;
    }
    finally
    {
      SQLiteDatabase localSQLiteDatabase;
      Cursor localCursor;
      if (localCursor != null)
        localCursor.close();
      if (localSQLiteDatabase != null)
        this.mStore.endRead(localSQLiteDatabase);
    }
  }

  private int fillQueueWithPlaylistChanges()
    throws SoftSyncException
  {
    try
    {
      localSQLiteDatabase = this.mStore.beginRead();
      localCursor = PlayList.getPlaylistsToSync(localSQLiteDatabase);
      int i = 0;
      SyncablePlaylist localSyncablePlaylist;
      if (localCursor.moveToFirst())
      {
        i += 1;
        PlayList localPlayList = new PlayList();
        localPlayList.populateFromFullProjectionCursor(localCursor);
        localSyncablePlaylist = SyncablePlaylist.parse(localPlayList);
        if (localSyncablePlaylist.mName != null)
          break label106;
        int j = Log.w(this.mTag, "Found a playlist with no name. Not syncing this change upstream.");
      }
      while (true)
      {
        boolean bool1 = localCursor.moveToNext();
        if (bool1)
          break;
        return i;
        label106: boolean bool2 = putItemInQueue(localSyncablePlaylist);
      }
    }
    finally
    {
      SQLiteDatabase localSQLiteDatabase;
      Cursor localCursor;
      if (localCursor != null)
        localCursor.close();
      if (localSQLiteDatabase != null)
        this.mStore.endRead(localSQLiteDatabase);
    }
  }

  private int fillQueueWithPlaylistTombstones()
    throws SoftSyncException
  {
    try
    {
      localSQLiteDatabase = this.mStore.beginRead();
      localCursor = PlayList.getPlaylistTombstones(localSQLiteDatabase);
      int i = 0;
      if (localCursor.moveToFirst())
      {
        boolean bool2;
        do
        {
          i += 1;
          PlayList localPlayList = new PlayList();
          localPlayList.populateFromTombstoneProjectionCursor(localCursor);
          SyncablePlaylist localSyncablePlaylist = SyncablePlaylist.parse(localPlayList);
          localSyncablePlaylist.mIsDeleted = true;
          boolean bool1 = putItemInQueue(localSyncablePlaylist);
          bool2 = localCursor.moveToNext();
        }
        while (bool2);
      }
      return i;
    }
    finally
    {
      SQLiteDatabase localSQLiteDatabase;
      Cursor localCursor;
      if (localCursor != null)
        localCursor.close();
      if (localSQLiteDatabase != null)
        this.mStore.endRead(localSQLiteDatabase);
    }
  }

  private int fillQueueWithRadioStationChanges()
    throws SoftSyncException
  {
    SQLiteDatabase localSQLiteDatabase;
    Cursor localCursor;
    int i;
    while (true)
    {
      SyncableRadioStation localSyncableRadioStation;
      try
      {
        localSQLiteDatabase = this.mStore.beginRead();
        localCursor = RadioStation.getRadioStationsToSync(localSQLiteDatabase);
        i = 0;
        if (localCursor == null)
          break;
        if (!localCursor.moveToNext())
          break;
        i += 1;
        RadioStation localRadioStation = new RadioStation();
        localRadioStation.populateFromFullProjectionCursor(localCursor);
        localSyncableRadioStation = SyncableRadioStation.parse(localRadioStation);
        if (localSyncableRadioStation.mName == null)
        {
          int j = Log.w(this.mTag, "Found a radio station with no name. Not syncing this change upstream.");
          continue;
        }
      }
      finally
      {
        Store.safeClose(localCursor);
        if (localSQLiteDatabase != null)
          this.mStore.endRead(localSQLiteDatabase);
      }
      boolean bool = putItemInQueue(localSyncableRadioStation);
    }
    Store.safeClose(localCursor);
    if (localSQLiteDatabase != null)
      this.mStore.endRead(localSQLiteDatabase);
    return i;
  }

  private int fillQueueWithRadioStationTombstones()
    throws SoftSyncException
  {
    SQLiteDatabase localSQLiteDatabase;
    Cursor localCursor;
    int i;
    try
    {
      localSQLiteDatabase = this.mStore.beginRead();
      localCursor = RadioStation.getRadioStationTombstones(localSQLiteDatabase);
      i = 0;
      if ((localCursor != null) && (localCursor.moveToNext()))
      {
        i += 1;
        RadioStation localRadioStation = new RadioStation();
        localRadioStation.populateFromTombstoneProjectionCursor(localCursor);
        SyncableRadioStation localSyncableRadioStation = SyncableRadioStation.parseForTombstone(localRadioStation);
        boolean bool = putItemInQueue(localSyncableRadioStation);
      }
    }
    finally
    {
      Store.safeClose(localCursor);
      if (localSQLiteDatabase != null)
        this.mStore.endRead(localSQLiteDatabase);
    }
    return i;
  }

  private int fillQueueWithTrackChanges()
    throws SoftSyncException
  {
    SQLiteDatabase localSQLiteDatabase = this.mStore.beginRead();
    int i = 0;
    try
    {
      long l = this.mAccountId;
      localCursor = MusicFile.getMusicFilesToSync(localSQLiteDatabase, l);
      if (localCursor != null)
        while (localCursor.moveToNext())
        {
          MusicFile localMusicFile = new MusicFile();
          localMusicFile.populateFromFullProjectionCursor(localCursor);
          Track localTrack = Track.parse(localMusicFile);
          boolean bool = putItemInQueue(localTrack);
          i += 1;
        }
      return i;
    }
    finally
    {
      Cursor localCursor;
      Store.safeClose(localCursor);
      this.mStore.endRead(localSQLiteDatabase);
    }
  }

  private int fillQueueWithTrackStats()
    throws SoftSyncException
  {
    SQLiteDatabase localSQLiteDatabase = this.mStore.beginRead();
    int i = 0;
    Cursor localCursor;
    while (true)
    {
      TrackStat localTrackStat;
      try
      {
        long l = this.mAccountId;
        localCursor = MusicFile.getPlaycountsToSync(localSQLiteDatabase, l);
        if (localCursor == null)
          break;
        if (!localCursor.moveToNext())
          break;
        localTrackStat = TrackStat.parse(localCursor);
        if ((Strings.isNullOrEmpty(localTrackStat.mRemoteId)) || (localTrackStat.mIncrementalPlays <= 0))
        {
          int j = Log.w(this.mTag, "Found a trackstat with no valid track id or zero play count. Not syncing this change upstream.");
          continue;
        }
      }
      finally
      {
        Store.safeClose(localCursor);
        this.mStore.endRead(localSQLiteDatabase);
      }
      boolean bool = putItemInQueue(localTrackStat);
      i += 1;
    }
    Store.safeClose(localCursor);
    this.mStore.endRead(localSQLiteDatabase);
    return i;
  }

  private int fillQueueWithTrackTombstones()
    throws SoftSyncException
  {
    List localList = MusicFileTombstone.getMusicTombstones(this.mStore);
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      TrackTombstone localTrackTombstone = TrackTombstone.parse((MusicFileTombstone)localIterator.next());
      boolean bool = putItemInQueue(localTrackTombstone);
    }
    return localList.size();
  }

  private boolean putItemInQueue(MusicQueueableSyncEntity paramMusicQueueableSyncEntity)
    throws SoftSyncException
  {
    if (this.mUseVerboseLogging)
    {
      String str1 = this.mTag;
      StringBuilder localStringBuilder = new StringBuilder().append("Found item: ");
      String str2 = paramMusicQueueableSyncEntity.toString();
      String str3 = str2;
      int i = Log.v(str1, str3);
    }
    try
    {
      this.mQueue.put(paramMusicQueueableSyncEntity);
      return true;
    }
    catch (ClosableBlockingQueue.QueueClosedException localQueueClosedException)
    {
      String str4 = "The upstream sender has killed the upstream queue, so there's no point in having the reader continue.";
      if (this.mUseVerboseLogging)
        int j = Log.v(this.mTag, str4);
      throw new SoftSyncException(str4, localQueueClosedException);
    }
    catch (InterruptedException localInterruptedException)
    {
      throw new SoftSyncException("Interrupted while putting item into upload queue: ", localInterruptedException);
    }
  }

  public void fillQueue()
    throws SoftSyncException
  {
    int i = fillQueueWithEntryTombstones();
    if (this.mUseVerboseLogging)
    {
      String str1 = this.mTag;
      String str2 = "Upstream reader: " + i + " playlist entry tombstone(s) found.";
      int j = Log.v(str1, str2);
    }
    int k = fillQueueWithPlaylistTombstones();
    if (this.mUseVerboseLogging)
    {
      String str3 = this.mTag;
      String str4 = "Upstream reader: " + k + " playlist tombstone(s) found.";
      int m = Log.v(str3, str4);
    }
    int n = fillQueueWithTrackChanges();
    if (this.mUseVerboseLogging)
    {
      String str5 = this.mTag;
      String str6 = "Upstream reader: " + n + " track change(s) found.";
      int i1 = Log.v(str5, str6);
    }
    int i2 = fillQueueWithPlaylistChanges();
    if (this.mUseVerboseLogging)
    {
      String str7 = this.mTag;
      String str8 = "Upstream reader: " + i2 + " playlist change(s) found.";
      int i3 = Log.v(str7, str8);
    }
    int i4 = fillQueueWithEntryChanges();
    if (this.mUseVerboseLogging)
    {
      String str9 = this.mTag;
      String str10 = "Upstream reader: " + i4 + " playlist entry change(s) found.";
      int i5 = Log.v(str9, str10);
    }
    int i6 = fillQueueWithTrackStats();
    if (this.mUseVerboseLogging)
    {
      String str11 = this.mTag;
      String str12 = "Upstream reader: " + i6 + " track stat change(s) found.";
      int i7 = Log.v(str11, str12);
    }
    int i8 = fillQueueWithRadioStationTombstones();
    if (this.mUseVerboseLogging)
    {
      String str13 = this.mTag;
      String str14 = "Upstream reader: " + i8 + " radio station tombstone(s) found.";
      int i9 = Log.v(str13, str14);
    }
    int i10 = fillQueueWithRadioStationChanges();
    if (this.mUseVerboseLogging)
    {
      String str15 = this.mTag;
      String str16 = "Upstream reader: " + i10 + " radio station found.";
      int i11 = Log.v(str15, str16);
    }
    if (!this.mEnableTrackDeletes)
      return;
    int i12 = fillQueueWithTrackTombstones();
    if (!this.mUseVerboseLogging)
      return;
    String str17 = this.mTag;
    String str18 = "Upstream reader: " + i12 + " track tombstone(s) found.";
    int i13 = Log.v(str17, str18);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.MusicUpstreamReader
 * JD-Core Version:    0.6.2
 */