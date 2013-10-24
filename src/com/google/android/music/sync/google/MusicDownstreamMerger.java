package com.google.android.music.sync.google;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;
import com.google.android.music.store.InvalidDataException;
import com.google.android.music.store.MusicContent;
import com.google.android.music.store.MusicFile;
import com.google.android.music.store.PlayList;
import com.google.android.music.store.PlayList.Item;
import com.google.android.music.store.RadioStation;
import com.google.android.music.store.RecentItemsManager;
import com.google.android.music.store.Store;
import com.google.android.music.sync.common.AbstractSyncAdapter.DownstreamMergeQueue;
import com.google.android.music.sync.common.DownstreamMerger;
import com.google.android.music.sync.common.ProviderException;
import com.google.android.music.sync.common.QueueableSyncEntity;
import com.google.android.music.sync.common.SoftSyncException;
import com.google.android.music.sync.google.model.SyncablePlaylist;
import com.google.android.music.sync.google.model.SyncablePlaylistEntry;
import com.google.android.music.sync.google.model.SyncableRadioStation;
import com.google.android.music.sync.google.model.Track;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.async.AsyncWorkers;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

class MusicDownstreamMerger extends DownstreamMerger
{
  private final Account mAccount;
  private MusicBlockMerger mBlockMerger;
  private final Context mContext;
  private SQLiteDatabase mDb;
  private long mLastNotificationTimeNs;
  private long mNotificationPeriodNs = 1000000000L;
  private final Map<String, Object> mProtocolState;
  private final int mRemoteAccount;
  private final Store mStore;
  private boolean mUpdateRecentItemsIncrementally = true;
  private final boolean mUseVerboseLogging;

  public MusicDownstreamMerger(AbstractSyncAdapter.DownstreamMergeQueue paramDownstreamMergeQueue, int paramInt, Context paramContext, Map<String, Object> paramMap, String paramString)
  {
    super(paramDownstreamMergeQueue, paramInt, paramString);
    Object localObject1 = paramMap.get("remote_account");
    int i = ((Integer)Integer.class.cast(localObject1)).intValue();
    this.mRemoteAccount = i;
    Store localStore = Store.getInstance(paramContext);
    this.mStore = localStore;
    this.mProtocolState = paramMap;
    Object localObject2 = paramMap.get("account");
    Account localAccount = (Account)Account.class.cast(localObject2);
    this.mAccount = localAccount;
    this.mContext = paramContext;
    boolean bool = DebugUtils.isLoggable(DebugUtils.MusicTag.SYNC);
    this.mUseVerboseLogging = bool;
  }

  private void saveLastModifiedTimeIfLatest(QueueableSyncEntity paramQueueableSyncEntity)
  {
    if ((paramQueueableSyncEntity instanceof Track))
    {
      l = ((Track)Track.class.cast(paramQueueableSyncEntity)).mLastModifiedTimestamp;
      Object localObject1 = this.mProtocolState.get("merger_track_version");
      if (((Long)Long.class.cast(localObject1)).longValue() >= l)
        return;
      Map localMap1 = this.mProtocolState;
      Long localLong1 = Long.valueOf(l);
      Object localObject2 = localMap1.put("merger_track_version", localLong1);
      return;
    }
    if ((paramQueueableSyncEntity instanceof SyncablePlaylist))
    {
      l = ((SyncablePlaylist)SyncablePlaylist.class.cast(paramQueueableSyncEntity)).mLastModifiedTimestamp;
      Object localObject3 = this.mProtocolState.get("merger_playlist_version");
      if (((Long)Long.class.cast(localObject3)).longValue() >= l)
        return;
      Map localMap2 = this.mProtocolState;
      Long localLong2 = Long.valueOf(l);
      Object localObject4 = localMap2.put("merger_playlist_version", localLong2);
      return;
    }
    if ((paramQueueableSyncEntity instanceof SyncablePlaylistEntry))
    {
      l = ((SyncablePlaylistEntry)SyncablePlaylistEntry.class.cast(paramQueueableSyncEntity)).mLastModifiedTimestamp;
      Object localObject5 = this.mProtocolState.get("merger_plentry_version");
      if (((Long)Long.class.cast(localObject5)).longValue() >= l)
        return;
      Map localMap3 = this.mProtocolState;
      Long localLong3 = Long.valueOf(l);
      Object localObject6 = localMap3.put("merger_plentry_version", localLong3);
      return;
    }
    if (!(paramQueueableSyncEntity instanceof SyncableRadioStation))
      return;
    long l = ((SyncableRadioStation)SyncableRadioStation.class.cast(paramQueueableSyncEntity)).mLastModifiedTimestamp;
    Object localObject7 = this.mProtocolState.get("merger_radio_station_version");
    if (((Long)Long.class.cast(localObject7)).longValue() >= l)
      return;
    Map localMap4 = this.mProtocolState;
    Long localLong4 = Long.valueOf(l);
    Object localObject8 = localMap4.put("merger_radio_station_version", localLong4);
  }

  public void onEndMergeBlock(boolean paramBoolean)
    throws SoftSyncException
  {
    if (paramBoolean);
    while (true)
    {
      try
      {
        SQLiteDatabase localSQLiteDatabase1 = this.mDb;
        Account localAccount = this.mAccount;
        ClientSyncState.Builder localBuilder1 = ClientSyncState.newBuilder();
        Object localObject1 = this.mProtocolState.get("remote_account");
        int i = ((Integer)Integer.class.cast(localObject1)).intValue();
        ClientSyncState.Builder localBuilder2 = localBuilder1.setRemoteAccount(i);
        Object localObject2 = this.mProtocolState.get("merger_track_version");
        long l1 = ((Long)Long.class.cast(localObject2)).longValue();
        ClientSyncState.Builder localBuilder3 = localBuilder2.setRemoteTrackVersion(l1);
        Object localObject3 = this.mProtocolState.get("merger_playlist_version");
        long l2 = ((Long)Long.class.cast(localObject3)).longValue();
        ClientSyncState.Builder localBuilder4 = localBuilder3.setRemotePlaylistVersion(l2);
        Object localObject4 = this.mProtocolState.get("merger_plentry_version");
        long l3 = ((Long)Long.class.cast(localObject4)).longValue();
        ClientSyncState.Builder localBuilder5 = localBuilder4.setRemotePlentryVersion(l3);
        Object localObject5 = this.mProtocolState.get("merger_radio_station_version");
        long l4 = ((Long)Long.class.cast(localObject5)).longValue();
        ClientSyncState.Builder localBuilder6 = localBuilder5.setRemoteRadioStationVersion(l4);
        Object localObject6 = this.mProtocolState.get("etag_track");
        String str1 = (String)String.class.cast(localObject6);
        ClientSyncState.Builder localBuilder7 = localBuilder6.setEtagTrack(str1);
        Object localObject7 = this.mProtocolState.get("etag_playlist");
        String str2 = (String)String.class.cast(localObject7);
        ClientSyncState.Builder localBuilder8 = localBuilder7.setEtagPlaylist(str2);
        Object localObject8 = this.mProtocolState.get("etag_playlist_entry");
        String str3 = (String)String.class.cast(localObject8);
        ClientSyncState localClientSyncState = localBuilder8.setEtagPlaylistEntry(str3).build();
        ClientSyncState.Helpers.set(localSQLiteDatabase1, localAccount, localClientSyncState);
        this.mBlockMerger.safelyCloseStatements();
        Store localStore1 = this.mStore;
        SQLiteDatabase localSQLiteDatabase2 = this.mDb;
        localStore1.endWriteTxn(localSQLiteDatabase2, paramBoolean);
        this.mDb = null;
        if (paramBoolean)
          this.mBlockMerger.cleanupLocallyCachedFiles();
        if (Log.isLoggable("MusicSyncAdapter", 2))
          int j = Log.v("MusicSyncAdapter", "Merger: End of block.");
        if (!paramBoolean)
          return;
        if (this.mUpdateRecentItemsIncrementally)
        {
          if (RecentItemsManager.countRecentItems(this.mContext) >= 50)
            this.mUpdateRecentItemsIncrementally = false;
        }
        else
        {
          long l5 = System.nanoTime();
          long l6 = this.mLastNotificationTimeNs;
          long l7 = l5 - l6;
          long l8 = this.mNotificationPeriodNs;
          if (l7 < l8)
            return;
          ContentResolver localContentResolver = this.mContext.getContentResolver();
          Uri localUri = MusicContent.CONTENT_URI;
          localContentResolver.notifyChange(localUri, null, false);
          this.mLastNotificationTimeNs = l5;
          long l9 = this.mNotificationPeriodNs + 1000000000L;
          long l10 = Math.min(10000000000L, l9);
          this.mNotificationPeriodNs = l10;
          return;
        }
      }
      catch (ProviderException localProviderException)
      {
        throw new SoftSyncException("Unable to set the sync state: ", localProviderException);
      }
      finally
      {
        this.mBlockMerger.safelyCloseStatements();
        Store localStore2 = this.mStore;
        SQLiteDatabase localSQLiteDatabase3 = this.mDb;
        localStore2.endWriteTxn(localSQLiteDatabase3, paramBoolean);
        this.mDb = null;
        if (paramBoolean)
          this.mBlockMerger.cleanupLocallyCachedFiles();
      }
      RecentItemsManager.updateRecentItemsAsync(this.mContext);
    }
  }

  public void onStartMergeBlock()
  {
    SQLiteDatabase localSQLiteDatabase1 = this.mStore.beginWriteTxn();
    this.mDb = localSQLiteDatabase1;
    Context localContext = this.mContext;
    SQLiteDatabase localSQLiteDatabase2 = this.mDb;
    Map localMap = this.mProtocolState;
    boolean bool = this.mUseVerboseLogging;
    String str = this.mTag;
    int i = this.mRemoteAccount;
    MusicBlockMerger localMusicBlockMerger = new MusicBlockMerger(localContext, localSQLiteDatabase2, localMap, bool, str, i);
    this.mBlockMerger = localMusicBlockMerger;
  }

  public void processMergeItem(QueueableSyncEntity paramQueueableSyncEntity1, QueueableSyncEntity paramQueueableSyncEntity2)
  {
    if (!this.mBlockMerger.processMergeItem(paramQueueableSyncEntity1, paramQueueableSyncEntity2))
      return;
    saveLastModifiedTimeIfLatest(paramQueueableSyncEntity1);
  }

  public static class MusicBlockMerger
  {
    private List<String> mCacheFilepaths;
    private final Context mContext;
    private final SQLiteDatabase mDb;
    private SQLiteStatement mDeletePlaylistStatement;
    private SQLiteStatement mDeletePlentryStatement;
    private SQLiteStatement mDeleteRadioStationStatement;
    private SQLiteStatement mInsertPlaylistStatement;
    private SQLiteStatement mInsertPlentryStatement;
    private SQLiteStatement mInsertRadioStationStatement;
    private SQLiteStatement mInsertTrackStatement;
    private final MusicFile mMusicFile;
    private final PlayList mPlayList;
    private final PlayList.Item mPlaylistItem;
    private final Map<String, Object> mProtocolState;
    private final RadioStation mRadioStation;
    private final int mRemoteAccount;
    private final String mTag;
    private SQLiteStatement mUpdatePlaylistStatement;
    private SQLiteStatement mUpdatePlentryStatement;
    private SQLiteStatement mUpdateRadioStationStatement;
    private SQLiteStatement mUpdateTrackStatement;
    private final boolean mUseVerboseLogging;

    public MusicBlockMerger(Context paramContext, SQLiteDatabase paramSQLiteDatabase, Map<String, Object> paramMap, boolean paramBoolean, String paramString, int paramInt)
    {
      this.mContext = paramContext;
      this.mDb = paramSQLiteDatabase;
      this.mProtocolState = paramMap;
      this.mUseVerboseLogging = paramBoolean;
      this.mTag = paramString;
      MusicFile localMusicFile = new MusicFile();
      this.mMusicFile = localMusicFile;
      PlayList localPlayList = new PlayList();
      this.mPlayList = localPlayList;
      PlayList.Item localItem = new PlayList.Item();
      this.mPlaylistItem = localItem;
      RadioStation localRadioStation = new RadioStation();
      this.mRadioStation = localRadioStation;
      this.mRemoteAccount = paramInt;
    }

    private boolean processMergeEntryImpl(SyncablePlaylistEntry paramSyncablePlaylistEntry1, SyncablePlaylistEntry paramSyncablePlaylistEntry2)
    {
      boolean bool = false;
      try
      {
        if (paramSyncablePlaylistEntry1.mIsDeleted)
        {
          if (this.mUseVerboseLogging)
          {
            String str1 = this.mTag;
            StringBuilder localStringBuilder1 = new StringBuilder().append("Deleting plentry ");
            String str2 = paramSyncablePlaylistEntry1.mRemoteId;
            StringBuilder localStringBuilder2 = localStringBuilder1.append(str2).append(" in playlist ");
            String str3 = paramSyncablePlaylistEntry1.mRemotePlaylistId;
            StringBuilder localStringBuilder3 = localStringBuilder2.append(str3).append(" at version ");
            long l1 = paramSyncablePlaylistEntry1.mLastModifiedTimestamp;
            String str4 = l1;
            int i = Log.v(str1, str4);
          }
          if (this.mDeletePlentryStatement == null)
          {
            SQLiteStatement localSQLiteStatement1 = PlayList.Item.compileItemDeleteStatement(this.mDb);
            this.mDeletePlentryStatement = localSQLiteStatement1;
          }
          SQLiteStatement localSQLiteStatement2 = this.mDeletePlentryStatement;
          int j = this.mRemoteAccount;
          String str5 = paramSyncablePlaylistEntry1.mRemoteId;
          PlayList.Item.deleteBySourceInfo(localSQLiteStatement2, j, str5);
          bool = true;
          return bool;
        }
        if (paramSyncablePlaylistEntry2 == null)
        {
          this.mPlaylistItem.reset();
          PlayList.Item localItem1 = this.mPlaylistItem;
          PlayList.Item localItem2 = paramSyncablePlaylistEntry1.formatAsPlayListItem(localItem1);
          PlayList.Item localItem3 = this.mPlaylistItem;
          int k = this.mRemoteAccount;
          localItem3.setSourceAccount(k);
        }
      }
      catch (InvalidDataException localInvalidDataException)
      {
        while (true)
        {
          try
          {
            PlayList.Item localItem4 = this.mPlaylistItem;
            SQLiteDatabase localSQLiteDatabase1 = this.mDb;
            int m = this.mRemoteAccount;
            String str6 = paramSyncablePlaylistEntry1.mTrackId;
            long l2 = Store.getMusicIdForSourceId(localSQLiteDatabase1, m, str6);
            localItem4.setMusicId(l2);
            if (this.mPlayList != null)
              break label385;
            StringBuilder localStringBuilder4 = new StringBuilder().append("Trying into insert a playlist entry ");
            String str7 = this.mPlaylistItem.getSourceId();
            StringBuilder localStringBuilder5 = localStringBuilder4.append(str7).append(" into a playlist ");
            String str8 = paramSyncablePlaylistEntry1.mRemotePlaylistId;
            String str9 = str8 + " that doesn't exist locally";
            int n = Log.w("MusicSyncAdapter", str9);
            continue;
            localInvalidDataException = localInvalidDataException;
            int i1 = Log.e(this.mTag, "Unable to merge a playlist entry.  Skipping entry.", localInvalidDataException);
          }
          catch (FileNotFoundException localFileNotFoundException1)
          {
            StringBuilder localStringBuilder6 = new StringBuilder().append("Failed to process a playlist entry insert for item id: ");
            String str10 = paramSyncablePlaylistEntry1.mTrackId;
            String str11 = str10;
            int i2 = Log.w("MusicSyncAdapter", str11);
          }
          continue;
          label385: this.mPlayList.reset();
          SQLiteDatabase localSQLiteDatabase2 = this.mDb;
          String str12 = Integer.toString(this.mRemoteAccount);
          String str13 = paramSyncablePlaylistEntry1.mRemotePlaylistId;
          PlayList localPlayList1 = this.mPlayList;
          PlayList localPlayList2 = PlayList.readPlayList(localSQLiteDatabase2, str12, str13, localPlayList1);
          PlayList.Item localItem5 = this.mPlaylistItem;
          long l3 = this.mPlayList.getId();
          localItem5.setListId(l3);
          if (this.mUseVerboseLogging)
          {
            String str14 = this.mTag;
            StringBuilder localStringBuilder7 = new StringBuilder().append("Inserting plentry ");
            String str15 = paramSyncablePlaylistEntry1.mRemoteId;
            StringBuilder localStringBuilder8 = localStringBuilder7.append(str15).append(" in playlist ");
            String str16 = paramSyncablePlaylistEntry1.mRemotePlaylistId;
            StringBuilder localStringBuilder9 = localStringBuilder8.append(str16).append(" at version ");
            long l4 = paramSyncablePlaylistEntry1.mLastModifiedTimestamp;
            String str17 = l4;
            int i3 = Log.v(str14, str17);
          }
          if (this.mInsertPlentryStatement == null)
          {
            SQLiteStatement localSQLiteStatement3 = PlayList.Item.compileItemInsertStatement(this.mDb);
            this.mInsertPlentryStatement = localSQLiteStatement3;
          }
          PlayList.Item localItem6 = this.mPlaylistItem;
          SQLiteStatement localSQLiteStatement4 = this.mInsertPlentryStatement;
          long l5 = localItem6.insertItem(localSQLiteStatement4);
          continue;
          PlayList.Item localItem7 = paramSyncablePlaylistEntry2.getEncapsulatedItem();
          PlayList.Item localItem8 = paramSyncablePlaylistEntry1.formatAsPlayListItem(localItem7);
          try
          {
            SQLiteDatabase localSQLiteDatabase3 = this.mDb;
            int i4 = this.mRemoteAccount;
            String str18 = paramSyncablePlaylistEntry1.mTrackId;
            long l6 = Store.getMusicIdForSourceId(localSQLiteDatabase3, i4, str18);
            localItem7.setMusicId(l6);
            if (this.mUseVerboseLogging)
            {
              String str19 = this.mTag;
              StringBuilder localStringBuilder10 = new StringBuilder().append("Updating plentry ");
              String str20 = paramSyncablePlaylistEntry1.mRemoteId;
              StringBuilder localStringBuilder11 = localStringBuilder10.append(str20).append(" in playlist ");
              String str21 = paramSyncablePlaylistEntry1.mRemotePlaylistId;
              StringBuilder localStringBuilder12 = localStringBuilder11.append(str21).append(" at version ");
              long l7 = paramSyncablePlaylistEntry1.mLastModifiedTimestamp;
              String str22 = l7;
              int i5 = Log.v(str19, str22);
            }
            if (this.mUpdatePlentryStatement == null)
            {
              SQLiteStatement localSQLiteStatement5 = PlayList.Item.compileItemUpdateStatement(this.mDb);
              this.mUpdatePlentryStatement = localSQLiteStatement5;
            }
            localItem7.setNeedsSync(false);
            SQLiteStatement localSQLiteStatement6 = this.mUpdatePlentryStatement;
            localItem7.update(localSQLiteStatement6);
          }
          catch (FileNotFoundException localFileNotFoundException2)
          {
            StringBuilder localStringBuilder13 = new StringBuilder().append("Failed to process a playlist entry update for item id: ");
            String str23 = paramSyncablePlaylistEntry1.mTrackId;
            String str24 = str23;
            int i6 = Log.w("MusicSyncAdapter", str24);
          }
        }
      }
    }

    private boolean processMergePlaylistImpl(SyncablePlaylist paramSyncablePlaylist1, SyncablePlaylist paramSyncablePlaylist2)
    {
      boolean bool = false;
      try
      {
        if (paramSyncablePlaylist1.mIsDeleted)
        {
          if (this.mUseVerboseLogging)
          {
            String str1 = this.mTag;
            StringBuilder localStringBuilder1 = new StringBuilder().append("Deleting playlist ");
            String str2 = paramSyncablePlaylist1.mRemoteId;
            StringBuilder localStringBuilder2 = localStringBuilder1.append(str2).append(" at version ");
            long l1 = paramSyncablePlaylist1.mLastModifiedTimestamp;
            String str3 = l1;
            int i = Log.v(str1, str3);
          }
          if (this.mDeletePlaylistStatement == null)
          {
            SQLiteStatement localSQLiteStatement1 = PlayList.compilePlaylistDeleteStatement(this.mDb);
            this.mDeletePlaylistStatement = localSQLiteStatement1;
          }
          SQLiteStatement localSQLiteStatement2 = this.mDeletePlaylistStatement;
          int j = this.mRemoteAccount;
          String str4 = paramSyncablePlaylist1.mRemoteId;
          PlayList.deleteBySourceInfo(localSQLiteStatement2, j, str4);
        }
        while (true)
        {
          bool = true;
          return bool;
          if (paramSyncablePlaylist2 != null)
            break;
          this.mPlayList.reset();
          PlayList localPlayList1 = this.mPlayList;
          PlayList localPlayList2 = paramSyncablePlaylist1.formatAsPlayList(localPlayList1);
          PlayList localPlayList3 = this.mPlayList;
          int k = this.mRemoteAccount;
          localPlayList3.setSourceAccount(k);
          this.mPlayList.setNeedsSync(false);
          if (this.mUseVerboseLogging)
          {
            String str5 = this.mTag;
            StringBuilder localStringBuilder3 = new StringBuilder().append("Inserting playlist ");
            String str6 = paramSyncablePlaylist1.mRemoteId;
            StringBuilder localStringBuilder4 = localStringBuilder3.append(str6).append(" at version ");
            long l2 = paramSyncablePlaylist1.mLastModifiedTimestamp;
            String str7 = l2;
            int m = Log.v(str5, str7);
          }
          if (this.mInsertPlaylistStatement == null)
          {
            SQLiteStatement localSQLiteStatement3 = PlayList.compilePlayListInsertStatement(this.mDb);
            this.mInsertPlaylistStatement = localSQLiteStatement3;
          }
          PlayList localPlayList4 = this.mPlayList;
          SQLiteStatement localSQLiteStatement4 = this.mInsertPlaylistStatement;
          long l3 = localPlayList4.insertList(localSQLiteStatement4);
        }
      }
      catch (InvalidDataException localInvalidDataException)
      {
        while (true)
        {
          int n = Log.e(this.mTag, "Unable to merge a playlist.  Skipping entry.", localInvalidDataException);
          continue;
          PlayList localPlayList5 = paramSyncablePlaylist2.getEncapsulatedPlayList();
          PlayList localPlayList6 = paramSyncablePlaylist1.formatAsPlayList(localPlayList5);
          int i1 = this.mRemoteAccount;
          localPlayList5.setSourceAccount(i1);
          localPlayList5.setNeedsSync(false);
          if (this.mUseVerboseLogging)
          {
            String str8 = this.mTag;
            StringBuilder localStringBuilder5 = new StringBuilder().append("Updating playlist ");
            String str9 = paramSyncablePlaylist1.mRemoteId;
            StringBuilder localStringBuilder6 = localStringBuilder5.append(str9).append(" at version ");
            long l4 = paramSyncablePlaylist1.mLastModifiedTimestamp;
            StringBuilder localStringBuilder7 = localStringBuilder6.append(l4).append(" with local id ");
            long l5 = localPlayList5.getId();
            String str10 = l5;
            int i2 = Log.v(str8, str10);
          }
          if (this.mUpdatePlaylistStatement == null)
          {
            SQLiteStatement localSQLiteStatement5 = PlayList.compilePlayListUpdateStatement(this.mDb);
            this.mUpdatePlaylistStatement = localSQLiteStatement5;
          }
          localPlayList5.setNeedsSync(false);
          SQLiteStatement localSQLiteStatement6 = this.mUpdatePlaylistStatement;
          localPlayList5.update(localSQLiteStatement6);
        }
      }
    }

    private boolean processMergeRadioStationImpl(SyncableRadioStation paramSyncableRadioStation1, SyncableRadioStation paramSyncableRadioStation2)
    {
      boolean bool = false;
      try
      {
        if (paramSyncableRadioStation1.mIsDeleted)
        {
          if (this.mUseVerboseLogging)
          {
            String str1 = this.mTag;
            StringBuilder localStringBuilder1 = new StringBuilder().append("Deleting radio station ");
            String str2 = paramSyncableRadioStation1.mRemoteId;
            StringBuilder localStringBuilder2 = localStringBuilder1.append(str2).append(" at version ");
            long l1 = paramSyncableRadioStation1.mLastModifiedTimestamp;
            String str3 = l1;
            int i = Log.v(str1, str3);
          }
          if (this.mDeleteRadioStationStatement == null)
          {
            SQLiteStatement localSQLiteStatement1 = RadioStation.compileDeleteStatement(this.mDb);
            this.mDeleteRadioStationStatement = localSQLiteStatement1;
          }
          SQLiteStatement localSQLiteStatement2 = this.mDeleteRadioStationStatement;
          int j = this.mRemoteAccount;
          String str4 = paramSyncableRadioStation1.mRemoteId;
          RadioStation.deleteBySourceInfo(localSQLiteStatement2, j, str4);
        }
        while (true)
        {
          bool = true;
          return bool;
          if (paramSyncableRadioStation2 != null)
            break;
          this.mRadioStation.reset();
          RadioStation localRadioStation1 = this.mRadioStation;
          RadioStation localRadioStation2 = paramSyncableRadioStation1.formatAsRadioStation(localRadioStation1);
          RadioStation localRadioStation3 = this.mRadioStation;
          int k = this.mRemoteAccount;
          localRadioStation3.setSourceAccount(k);
          this.mRadioStation.setNeedsSync(false);
          if (this.mUseVerboseLogging)
          {
            String str5 = this.mTag;
            StringBuilder localStringBuilder3 = new StringBuilder().append("Inserting radio station ");
            String str6 = paramSyncableRadioStation1.mRemoteId;
            StringBuilder localStringBuilder4 = localStringBuilder3.append(str6).append(" at version ");
            long l2 = paramSyncableRadioStation1.mLastModifiedTimestamp;
            String str7 = l2;
            int m = Log.v(str5, str7);
          }
          if (this.mInsertRadioStationStatement == null)
          {
            SQLiteStatement localSQLiteStatement3 = RadioStation.compileInsertStatement(this.mDb);
            this.mInsertRadioStationStatement = localSQLiteStatement3;
          }
          RadioStation localRadioStation4 = this.mRadioStation;
          SQLiteStatement localSQLiteStatement4 = this.mInsertRadioStationStatement;
          long l3 = localRadioStation4.insert(localSQLiteStatement4);
        }
      }
      catch (InvalidDataException localInvalidDataException)
      {
        while (true)
        {
          int n = Log.e(this.mTag, "Unable to merge a radio station.  Skipping.", localInvalidDataException);
          continue;
          RadioStation localRadioStation5 = paramSyncableRadioStation2.getEncapsulatedRadioStation();
          RadioStation localRadioStation6 = paramSyncableRadioStation1.formatAsRadioStation(localRadioStation5);
          int i1 = this.mRemoteAccount;
          localRadioStation5.setSourceAccount(i1);
          localRadioStation5.setNeedsSync(false);
          if (this.mUseVerboseLogging)
          {
            String str8 = this.mTag;
            StringBuilder localStringBuilder5 = new StringBuilder().append("Updating radio station ");
            String str9 = paramSyncableRadioStation1.mRemoteId;
            StringBuilder localStringBuilder6 = localStringBuilder5.append(str9).append(" at version ");
            long l4 = paramSyncableRadioStation1.mLastModifiedTimestamp;
            StringBuilder localStringBuilder7 = localStringBuilder6.append(l4).append(" with local id ");
            long l5 = localRadioStation5.getId();
            String str10 = l5;
            int i2 = Log.v(str8, str10);
          }
          if (this.mUpdateRadioStationStatement == null)
          {
            SQLiteStatement localSQLiteStatement5 = RadioStation.compileUpdateStatement(this.mDb);
            this.mUpdateRadioStationStatement = localSQLiteStatement5;
          }
          localRadioStation5.setNeedsSync(false);
          SQLiteStatement localSQLiteStatement6 = this.mUpdateRadioStationStatement;
          localRadioStation5.update(localSQLiteStatement6);
        }
      }
    }

    private boolean processMergeTrackImpl(Track paramTrack1, Track paramTrack2)
    {
      try
      {
        if (paramTrack1.mIsDeleted)
        {
          if (this.mUseVerboseLogging)
          {
            String str1 = this.mTag;
            StringBuilder localStringBuilder1 = new StringBuilder().append("Deleting track ");
            String str2 = paramTrack1.mRemoteId;
            StringBuilder localStringBuilder2 = localStringBuilder1.append(str2).append(" at version ");
            long l1 = paramTrack1.mLastModifiedTimestamp;
            String str3 = l1;
            int i = Log.v(str1, str3);
          }
          Context localContext = this.mContext;
          SQLiteDatabase localSQLiteDatabase1 = this.mDb;
          int j = this.mRemoteAccount;
          String str4 = paramTrack1.mRemoteId;
          String str5 = MusicFile.deleteAndGetLocalCacheFilepath(localContext, localSQLiteDatabase1, j, str4);
          if (str5 != null)
          {
            if (this.mCacheFilepaths == null)
            {
              ArrayList localArrayList = new ArrayList();
              this.mCacheFilepaths = localArrayList;
            }
            boolean bool1 = this.mCacheFilepaths.add(str5);
          }
        }
        while (true)
        {
          bool2 = true;
          return bool2;
          if (paramTrack1.getTrackType() == 7)
          {
            SQLiteDatabase localSQLiteDatabase2 = this.mDb;
            String str6 = String.valueOf(this.mRemoteAccount);
            String str7 = paramTrack1.getNormalizedNautilusId();
            MusicFile localMusicFile1 = MusicFile.readMusicFile(localSQLiteDatabase2, str6, str7, null);
            if (localMusicFile1 != null)
              paramTrack2 = Track.parse(localMusicFile1);
          }
          if (paramTrack2 != null)
            break label646;
          this.mMusicFile.reset();
          MusicFile localMusicFile2 = this.mMusicFile;
          MusicFile localMusicFile3 = paramTrack1.formatAsMusicFile(localMusicFile2);
          MusicFile localMusicFile4 = this.mMusicFile;
          int k = this.mRemoteAccount;
          localMusicFile4.setSourceAccount(k);
          if (this.mUseVerboseLogging)
          {
            String str8 = this.mTag;
            StringBuilder localStringBuilder3 = new StringBuilder().append("Inserting track ");
            String str9 = paramTrack1.getEffectiveRemoteId();
            StringBuilder localStringBuilder4 = localStringBuilder3.append(str9).append(" at version ");
            long l2 = paramTrack1.mLastModifiedTimestamp;
            String str10 = l2;
            int m = Log.v(str8, str10);
          }
          if (this.mInsertTrackStatement == null)
          {
            SQLiteStatement localSQLiteStatement1 = MusicFile.compileMusicInsertStatement(this.mDb);
            this.mInsertTrackStatement = localSQLiteStatement1;
          }
          MusicFile localMusicFile5 = this.mMusicFile;
          SQLiteStatement localSQLiteStatement2 = this.mInsertTrackStatement;
          long l3 = localMusicFile5.insertMusicFile(localSQLiteStatement2);
          if (this.mMusicFile.isPurchasedTrack())
          {
            long l4 = System.currentTimeMillis();
            long l5 = this.mMusicFile.getAddedTime();
            if (l4 - l5 < 18000000L)
            {
              if (((AtomicInteger)this.mProtocolState.get("new_purchased_count")).incrementAndGet() != 1)
                break;
              Map localMap1 = this.mProtocolState;
              String str11 = this.mMusicFile.getAlbumName();
              Object localObject1 = localMap1.put("new_purchased_album_name", str11);
              Map localMap2 = this.mProtocolState;
              String str12 = this.mMusicFile.getAlbumArtist();
              Object localObject2 = localMap2.put("new_purchased_artist_name", str12);
              Map localMap3 = this.mProtocolState;
              Long localLong = Long.valueOf(this.mMusicFile.getAlbumId());
              Object localObject3 = localMap3.put("new_purchased_albumId", localLong);
              Map localMap4 = this.mProtocolState;
              String str13 = this.mMusicFile.getTitle();
              Object localObject4 = localMap4.put("new_purchased_song_title", str13);
            }
          }
        }
      }
      catch (InvalidDataException localInvalidDataException)
      {
        while (true)
        {
          int n = Log.e(this.mTag, "Unable to merge a track.  Skipping entry.", localInvalidDataException);
          boolean bool2 = false;
          continue;
          long l6 = ((Long)this.mProtocolState.get("new_purchased_albumId")).longValue();
          long l7 = this.mMusicFile.getAlbumId();
          if (l6 != l7)
          {
            Map localMap5 = this.mProtocolState;
            Boolean localBoolean = Boolean.valueOf(false);
            Object localObject5 = localMap5.put("new_purchased_same_album", localBoolean);
            continue;
            label646: MusicFile localMusicFile6 = paramTrack2.getEncapsulatedMusicFile();
            MusicFile localMusicFile7 = paramTrack1.formatAsMusicFile(localMusicFile6);
            int i1 = this.mRemoteAccount;
            localMusicFile6.setSourceAccount(i1);
            if (this.mUseVerboseLogging)
            {
              String str14 = this.mTag;
              StringBuilder localStringBuilder5 = new StringBuilder().append("Updating track ");
              String str15 = paramTrack1.getEffectiveRemoteId();
              StringBuilder localStringBuilder6 = localStringBuilder5.append(str15).append(" at version ");
              long l8 = paramTrack1.mLastModifiedTimestamp;
              StringBuilder localStringBuilder7 = localStringBuilder6.append(l8).append(" with local id ");
              long l9 = localMusicFile6.getLocalId();
              String str16 = l9;
              int i2 = Log.v(str14, str16);
            }
            if (this.mUpdateTrackStatement == null)
            {
              SQLiteStatement localSQLiteStatement3 = MusicFile.compileFullUpdateStatement(this.mDb);
              this.mUpdateTrackStatement = localSQLiteStatement3;
            }
            SQLiteStatement localSQLiteStatement4 = this.mUpdateTrackStatement;
            SQLiteDatabase localSQLiteDatabase3 = this.mDb;
            localMusicFile6.updateMusicFile(localSQLiteStatement4, localSQLiteDatabase3);
          }
        }
      }
    }

    public void cleanupLocallyCachedFiles()
    {
      if (this.mCacheFilepaths == null)
        return;
      if (this.mCacheFilepaths.isEmpty())
        return;
      List localList = this.mCacheFilepaths;
      String[] arrayOfString1 = new String[0];
      final String[] arrayOfString2 = (String[])localList.toArray(arrayOfString1);
      this.mCacheFilepaths.clear();
      LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          String[] arrayOfString = arrayOfString2;
          int i = arrayOfString.length;
          int j = 0;
          while (true)
          {
            if (j >= i)
              return;
            String str1 = arrayOfString[j];
            try
            {
              if (!new File(str1).delete())
              {
                String str2 = MusicDownstreamMerger.MusicBlockMerger.this.mTag;
                String str3 = "Could not cache file <" + str1 + ">";
                int k = Log.w(str2, str3);
              }
              j += 1;
            }
            catch (Exception localException)
            {
              while (true)
              {
                String str4 = MusicDownstreamMerger.MusicBlockMerger.this.mTag;
                String str5 = "Exception while deleting cache file <" + str1 + ">";
                int m = Log.e(str4, str5, localException);
              }
            }
          }
        }
      };
      AsyncWorkers.runAsync(localLoggableHandler, local1);
    }

    public boolean processMergeItem(QueueableSyncEntity paramQueueableSyncEntity1, QueueableSyncEntity paramQueueableSyncEntity2)
    {
      boolean bool;
      if ((paramQueueableSyncEntity1 instanceof Track))
      {
        Track localTrack1 = (Track)Track.class.cast(paramQueueableSyncEntity1);
        Track localTrack2 = (Track)Track.class.cast(paramQueueableSyncEntity2);
        bool = processMergeTrackImpl(localTrack1, localTrack2);
      }
      while (true)
      {
        return bool;
        if ((paramQueueableSyncEntity1 instanceof SyncablePlaylist))
        {
          SyncablePlaylist localSyncablePlaylist1 = (SyncablePlaylist)SyncablePlaylist.class.cast(paramQueueableSyncEntity1);
          SyncablePlaylist localSyncablePlaylist2 = (SyncablePlaylist)SyncablePlaylist.class.cast(paramQueueableSyncEntity2);
          bool = processMergePlaylistImpl(localSyncablePlaylist1, localSyncablePlaylist2);
        }
        else if ((paramQueueableSyncEntity1 instanceof SyncablePlaylistEntry))
        {
          SyncablePlaylistEntry localSyncablePlaylistEntry1 = (SyncablePlaylistEntry)SyncablePlaylistEntry.class.cast(paramQueueableSyncEntity1);
          SyncablePlaylistEntry localSyncablePlaylistEntry2 = (SyncablePlaylistEntry)SyncablePlaylistEntry.class.cast(paramQueueableSyncEntity2);
          bool = processMergeEntryImpl(localSyncablePlaylistEntry1, localSyncablePlaylistEntry2);
        }
        else if ((paramQueueableSyncEntity1 instanceof SyncableRadioStation))
        {
          SyncableRadioStation localSyncableRadioStation1 = (SyncableRadioStation)SyncableRadioStation.class.cast(paramQueueableSyncEntity1);
          SyncableRadioStation localSyncableRadioStation2 = (SyncableRadioStation)SyncableRadioStation.class.cast(paramQueueableSyncEntity2);
          bool = processMergeRadioStationImpl(localSyncableRadioStation1, localSyncableRadioStation2);
        }
        else
        {
          bool = false;
        }
      }
    }

    public void safelyCloseStatements()
    {
      Store.safeClose(this.mInsertTrackStatement);
      Store.safeClose(this.mUpdateTrackStatement);
      Store.safeClose(this.mInsertPlaylistStatement);
      Store.safeClose(this.mUpdatePlaylistStatement);
      Store.safeClose(this.mDeletePlaylistStatement);
      Store.safeClose(this.mInsertPlentryStatement);
      Store.safeClose(this.mUpdatePlentryStatement);
      Store.safeClose(this.mDeletePlentryStatement);
      Store.safeClose(this.mInsertRadioStationStatement);
      Store.safeClose(this.mUpdateRadioStationStatement);
      Store.safeClose(this.mDeleteRadioStationStatement);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.MusicDownstreamMerger
 * JD-Core Version:    0.6.2
 */