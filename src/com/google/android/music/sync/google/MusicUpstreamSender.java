package com.google.android.music.sync.google;

import android.accounts.Account;
import android.accounts.AuthenticatorException;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.music.store.DataNotFoundException;
import com.google.android.music.store.InvalidDataException;
import com.google.android.music.store.MusicFile;
import com.google.android.music.store.PlayList;
import com.google.android.music.store.PlayList.Item;
import com.google.android.music.store.RadioStation;
import com.google.android.music.store.Store;
import com.google.android.music.sync.api.BadRequestException;
import com.google.android.music.sync.api.ConflictException;
import com.google.android.music.sync.api.ForbiddenException;
import com.google.android.music.sync.api.MusicApiClient;
import com.google.android.music.sync.api.MusicApiClient.OpType;
import com.google.android.music.sync.api.ResourceNotFoundException;
import com.google.android.music.sync.api.ServiceUnavailableException;
import com.google.android.music.sync.common.AbstractSyncAdapter.UpstreamQueue;
import com.google.android.music.sync.common.ConflictDetectedException;
import com.google.android.music.sync.common.HardSyncException;
import com.google.android.music.sync.common.QueueableSyncEntity;
import com.google.android.music.sync.common.SoftSyncException;
import com.google.android.music.sync.common.SyncHttpException;
import com.google.android.music.sync.common.UpstreamSender;
import com.google.android.music.sync.google.model.BatchMutateResponse.MutateResponse;
import com.google.android.music.sync.google.model.BatchMutateResponse.MutateResponse.ResponseCode;
import com.google.android.music.sync.google.model.MusicQueueableSyncEntity;
import com.google.android.music.sync.google.model.SyncablePlaylist;
import com.google.android.music.sync.google.model.SyncablePlaylistEntry;
import com.google.android.music.sync.google.model.SyncableRadioStation;
import com.google.android.music.sync.google.model.Track;
import com.google.android.music.sync.google.model.TrackStat;
import com.google.android.music.sync.google.model.TrackTombstone;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class MusicUpstreamSender extends UpstreamSender
{
  private final Account mAccount;
  private final int mAccountHash;
  private final MusicApiClient mClient;
  private final Context mContext;
  private final boolean mEnableTrackStatsUpSync;
  private final Map<String, Object> mProtocolState;
  private final Store mStore;
  private final String mTag;
  private final boolean mUseVerboseLogging;
  private final ContentValues mValues;

  public MusicUpstreamSender(AbstractSyncAdapter.UpstreamQueue paramUpstreamQueue, int paramInt, Context paramContext, Map<String, Object> paramMap, String paramString, MusicApiClient paramMusicApiClient, boolean paramBoolean)
  {
    super(paramUpstreamQueue, paramInt, paramString);
    this.mTag = paramString;
    boolean bool = DebugUtils.isLoggable(DebugUtils.MusicTag.SYNC);
    this.mUseVerboseLogging = bool;
    this.mContext = paramContext;
    Store localStore = Store.getInstance(paramContext);
    this.mStore = localStore;
    this.mClient = paramMusicApiClient;
    ContentValues localContentValues = new ContentValues();
    this.mValues = localContentValues;
    this.mProtocolState = paramMap;
    Object localObject1 = paramMap.get("account");
    Account localAccount = (Account)Account.class.cast(localObject1);
    this.mAccount = localAccount;
    Object localObject2 = paramMap.get("remote_account");
    int i = ((Integer)Integer.class.cast(localObject2)).intValue();
    this.mAccountHash = i;
    this.mEnableTrackStatsUpSync = paramBoolean;
  }

  private void cleanupBatchMutations(List<? extends MusicQueueableSyncEntity> paramList, List<BatchMutateResponse.MutateResponse> paramList1)
    throws ConflictException, HardSyncException, SoftSyncException, ServiceUnavailableException, AuthenticatorException
  {
    SQLiteDatabase localSQLiteDatabase = this.mStore.beginWriteTxn();
    int i = 0;
    int j = 0;
    int k;
    try
    {
      k = paramList.size();
      int m = paramList1.size();
      if (k > m)
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("A batch mutate response to the server contained ").append(k).append(" mutations, but ");
        int n = paramList1.size();
        String str1 = n + " results were returned.";
        throw new HardSyncException(str1);
      }
    }
    finally
    {
      this.mStore.endWriteTxn(localSQLiteDatabase, false);
    }
    int i1 = 0;
    if (i1 < k)
    {
      MusicQueueableSyncEntity localMusicQueueableSyncEntity = (MusicQueueableSyncEntity)paramList.get(i1);
      BatchMutateResponse.MutateResponse localMutateResponse = (BatchMutateResponse.MutateResponse)paramList1.get(i1);
      String str2 = localMutateResponse.mResponseCode;
      int[] arrayOfInt = 1.$SwitchMap$com$google$android$music$sync$google$model$BatchMutateResponse$MutateResponse$ResponseCode;
      int i2 = mapStringResponseCodeToEnum(str2).ordinal();
      switch (arrayOfInt[i2])
      {
      default:
      case 1:
      case 2:
      case 3:
      case 4:
      }
      while (true)
      {
        boolean bool = localSQLiteDatabase.yieldIfContendedSafely();
        i1 += 1;
        break;
        i += 1;
        if (localMusicQueueableSyncEntity.isInsert())
        {
          if (localMutateResponse.mId == null)
          {
            int i3 = Log.e(this.mTag, "Insert response from server with null id.  Removing.");
            deleteMutatedRow(localMusicQueueableSyncEntity, localSQLiteDatabase);
          }
          else
          {
            String str3 = localMutateResponse.mId;
            localMusicQueueableSyncEntity.setRemoteId(str3);
            cleanupInsert(localMusicQueueableSyncEntity, localSQLiteDatabase);
          }
        }
        else if (localMusicQueueableSyncEntity.isUpdate())
        {
          cleanupUpdate(localMusicQueueableSyncEntity, localSQLiteDatabase);
        }
        else if (localMusicQueueableSyncEntity.isDeleted())
        {
          cleanupDelete(localMusicQueueableSyncEntity, localSQLiteDatabase);
          continue;
          j += 1;
          if (localMusicQueueableSyncEntity.isInsert())
          {
            deleteMutatedRow(localMusicQueueableSyncEntity, localSQLiteDatabase);
          }
          else if (localMusicQueueableSyncEntity.isUpdate())
          {
            int i4 = Log.e(this.mTag, "TOO_MANY_ITEMS response code returned for an update. Likely a server side error.");
            cleanupMutation(localMusicQueueableSyncEntity, localSQLiteDatabase);
          }
          else if (localMusicQueueableSyncEntity.isDeleted())
          {
            int i5 = Log.e(this.mTag, "TOO_MANY_ITEMS response code returned for a delete. Likely a server side error.");
            deleteMutatedRow(localMusicQueueableSyncEntity, localSQLiteDatabase);
            continue;
            if (this.mUseVerboseLogging)
              int i6 = Log.v(this.mTag, "Upstream sender received invalid request on item in batch mutate.  Restoring from server.");
            if ((localMusicQueueableSyncEntity.isInsert()) || (TextUtils.isEmpty(localMusicQueueableSyncEntity.getRemoteId())))
              deleteMutatedRow(localMusicQueueableSyncEntity, localSQLiteDatabase);
            else
              restoreItemFromServer(localMusicQueueableSyncEntity);
          }
        }
      }
    }
    this.mStore.endWriteTxn(localSQLiteDatabase, true);
    String str4;
    StringBuilder localStringBuilder2;
    if (j > 0)
    {
      str4 = this.mTag;
      localStringBuilder2 = new StringBuilder();
      if (j != 1)
        break label568;
    }
    label568: for (String str5 = "1 entry"; ; str5 = j + " entries")
    {
      String str6 = str5 + " rejected due to the constituent entity being too large.";
      int i7 = Log.w(str4, str6);
      if (i == 0)
        return;
      ConflictException localConflictException = new ConflictException();
      localConflictException.setConflictCount(i);
      throw localConflictException;
    }
  }

  private void cleanupDelete(MusicQueueableSyncEntity paramMusicQueueableSyncEntity, SQLiteDatabase paramSQLiteDatabase)
  {
    String str1 = null;
    String str2 = null;
    if ((paramMusicQueueableSyncEntity instanceof SyncablePlaylist))
    {
      str1 = "LIST_TOMBSTONES";
      str2 = "Id";
    }
    while (true)
    {
      if (this.mUseVerboseLogging)
        int i = Log.v(this.mTag, "Upstream sender: Removing playlist entity.");
      String str3 = str2 + "=?";
      String[] arrayOfString = new String[1];
      String str4 = Long.toString(paramMusicQueueableSyncEntity.getLocalId());
      arrayOfString[0] = str4;
      int j = paramSQLiteDatabase.delete(str1, str3, arrayOfString);
      return;
      if ((paramMusicQueueableSyncEntity instanceof SyncablePlaylistEntry))
      {
        str1 = "LISTITEM_TOMBSTONES";
        str2 = "Id";
      }
      else if ((paramMusicQueueableSyncEntity instanceof TrackTombstone))
      {
        str1 = "MUSIC_TOMBSTONES";
        str2 = "Id";
      }
      else if ((paramMusicQueueableSyncEntity instanceof SyncableRadioStation))
      {
        str1 = "RADIO_STATION_TOMBSTONES";
        str2 = "Id";
      }
    }
  }

  private void cleanupInsert(MusicQueueableSyncEntity paramMusicQueueableSyncEntity, SQLiteDatabase paramSQLiteDatabase)
  {
    String str1 = null;
    String str2 = null;
    this.mValues.clear();
    if ((paramMusicQueueableSyncEntity instanceof SyncablePlaylist))
    {
      str1 = "LISTS";
      str2 = "Id";
      ContentValues localContentValues1 = this.mValues;
      Integer localInteger1 = Integer.valueOf(0);
      localContentValues1.put("_sync_dirty", localInteger1);
      ContentValues localContentValues2 = this.mValues;
      String str3 = paramMusicQueueableSyncEntity.getRemoteId();
      localContentValues2.put("SourceId", str3);
      ContentValues localContentValues3 = this.mValues;
      Integer localInteger2 = Integer.valueOf(this.mAccountHash);
      localContentValues3.put("SourceAccount", localInteger2);
    }
    while (true)
    {
      if (this.mUseVerboseLogging)
        int i = Log.v(this.mTag, "Upstream sender: Undirtying inserted entity.");
      ContentValues localContentValues4 = this.mValues;
      String str4 = str2 + "=?";
      String[] arrayOfString = new String[1];
      String str5 = Long.toString(paramMusicQueueableSyncEntity.getLocalId());
      arrayOfString[0] = str5;
      int j = paramSQLiteDatabase.update(str1, localContentValues4, str4, arrayOfString);
      return;
      if ((paramMusicQueueableSyncEntity instanceof SyncablePlaylistEntry))
      {
        str1 = "LISTITEMS";
        str2 = "Id";
        ContentValues localContentValues5 = this.mValues;
        Integer localInteger3 = Integer.valueOf(0);
        localContentValues5.put("_sync_dirty", localInteger3);
        ContentValues localContentValues6 = this.mValues;
        String str6 = paramMusicQueueableSyncEntity.getRemoteId();
        localContentValues6.put("SourceId", str6);
        ContentValues localContentValues7 = this.mValues;
        Integer localInteger4 = Integer.valueOf(this.mAccountHash);
        localContentValues7.put("SourceAccount", localInteger4);
      }
      else if ((paramMusicQueueableSyncEntity instanceof Track))
      {
        str1 = "MUSIC";
        str2 = "Id";
        ContentValues localContentValues8 = this.mValues;
        Integer localInteger5 = Integer.valueOf(0);
        localContentValues8.put("_sync_dirty", localInteger5);
        ContentValues localContentValues9 = this.mValues;
        String str7 = paramMusicQueueableSyncEntity.getRemoteId();
        localContentValues9.put("SourceId", str7);
        ContentValues localContentValues10 = this.mValues;
        Integer localInteger6 = Integer.valueOf(2);
        localContentValues10.put("SourceType", localInteger6);
        ContentValues localContentValues11 = this.mValues;
        Integer localInteger7 = Integer.valueOf(this.mAccountHash);
        localContentValues11.put("SourceAccount", localInteger7);
      }
      else if ((paramMusicQueueableSyncEntity instanceof SyncableRadioStation))
      {
        str1 = "RADIO_STATIONS";
        str2 = "Id";
        ContentValues localContentValues12 = this.mValues;
        Integer localInteger8 = Integer.valueOf(0);
        localContentValues12.put("_sync_dirty", localInteger8);
        ContentValues localContentValues13 = this.mValues;
        String str8 = paramMusicQueueableSyncEntity.getRemoteId();
        localContentValues13.put("SourceId", str8);
        ContentValues localContentValues14 = this.mValues;
        Integer localInteger9 = Integer.valueOf(this.mAccountHash);
        localContentValues14.put("SourceAccount", localInteger9);
      }
    }
  }

  private void cleanupMutation(MusicQueueableSyncEntity paramMusicQueueableSyncEntity, SQLiteDatabase paramSQLiteDatabase)
  {
    if (paramMusicQueueableSyncEntity.isDeleted())
    {
      cleanupDelete(paramMusicQueueableSyncEntity, paramSQLiteDatabase);
      return;
    }
    if (paramMusicQueueableSyncEntity.isUpdate())
    {
      cleanupUpdate(paramMusicQueueableSyncEntity, paramSQLiteDatabase);
      return;
    }
    cleanupInsert(paramMusicQueueableSyncEntity, paramSQLiteDatabase);
  }

  private void cleanupUpdate(MusicQueueableSyncEntity paramMusicQueueableSyncEntity, SQLiteDatabase paramSQLiteDatabase)
  {
    String str1 = null;
    String str2 = null;
    if ((paramMusicQueueableSyncEntity instanceof SyncablePlaylist))
    {
      str1 = "LISTS";
      str2 = "Id";
    }
    while (true)
    {
      if (this.mUseVerboseLogging)
        int i = Log.v(this.mTag, "Upstream sender: Undirtying updated entity.");
      this.mValues.clear();
      ContentValues localContentValues1 = this.mValues;
      Integer localInteger = Integer.valueOf(0);
      localContentValues1.put("_sync_dirty", localInteger);
      ContentValues localContentValues2 = this.mValues;
      String str3 = str2 + "=?";
      String[] arrayOfString = new String[1];
      String str4 = Long.toString(paramMusicQueueableSyncEntity.getLocalId());
      arrayOfString[0] = str4;
      int j = paramSQLiteDatabase.update(str1, localContentValues2, str3, arrayOfString);
      return;
      if ((paramMusicQueueableSyncEntity instanceof SyncablePlaylistEntry))
      {
        str1 = "LISTITEMS";
        str2 = "Id";
      }
      else if ((paramMusicQueueableSyncEntity instanceof Track))
      {
        str1 = "MUSIC";
        str2 = "Id";
      }
      else if ((paramMusicQueueableSyncEntity instanceof SyncableRadioStation))
      {
        str1 = "RADIO_STATIONS";
        str2 = "Id";
      }
    }
  }

  private void deleteMutatedRow(MusicQueueableSyncEntity paramMusicQueueableSyncEntity, SQLiteDatabase paramSQLiteDatabase)
  {
    if (paramMusicQueueableSyncEntity.isDeleted())
    {
      cleanupDelete(paramMusicQueueableSyncEntity, paramSQLiteDatabase);
      return;
    }
    long l = paramMusicQueueableSyncEntity.getLocalId();
    if ((paramMusicQueueableSyncEntity instanceof SyncablePlaylist))
    {
      int i = PlayList.deleteById(paramSQLiteDatabase, l);
      return;
    }
    if ((paramMusicQueueableSyncEntity instanceof SyncablePlaylistEntry))
    {
      PlayList.Item.deleteById(paramSQLiteDatabase, l);
      return;
    }
    if ((paramMusicQueueableSyncEntity instanceof Track))
    {
      MusicFile.deleteByLocalId(this.mContext, l);
      return;
    }
    int j = Log.e(this.mTag, "Attempting to undo a mutation of unknown type.");
  }

  private void deletePlaylist(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = this.mStore.beginWriteTxn();
    try
    {
      int i = PlayList.deleteById(localSQLiteDatabase, paramLong);
      return;
    }
    finally
    {
      Store.safeClose(null);
      this.mStore.endWriteTxn(localSQLiteDatabase, true);
    }
  }

  private void deletePlaylistEntry(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = this.mStore.beginWriteTxn();
    try
    {
      PlayList.Item.deleteById(localSQLiteDatabase, paramLong);
      return;
    }
    finally
    {
      Store.safeClose(null);
      this.mStore.endWriteTxn(localSQLiteDatabase, true);
    }
  }

  private void deleteRadioStation(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = this.mStore.beginWriteTxn();
    try
    {
      RadioStation.deleteById(localSQLiteDatabase, paramLong);
      return;
    }
    finally
    {
      Store.safeClose(null);
      this.mStore.endWriteTxn(localSQLiteDatabase, true);
    }
  }

  private boolean fillInRemoteIdForTrack(SyncablePlaylistEntry paramSyncablePlaylistEntry)
  {
    boolean bool = false;
    SQLiteDatabase localSQLiteDatabase = this.mStore.beginRead();
    try
    {
      MusicFile localMusicFile = new MusicFile();
      try
      {
        long l = paramSyncablePlaylistEntry.mLocalMusicId;
        localMusicFile.load(localSQLiteDatabase, l);
        if (localMusicFile.getTrackType() == 5)
        {
          int i = localMusicFile.getSourceType();
          if (i != 2)
            return bool;
        }
      }
      catch (DataNotFoundException localDataNotFoundException)
      {
        while (true)
        {
          this.mStore.endRead(localSQLiteDatabase);
          continue;
          String str = localMusicFile.getSourceId();
          paramSyncablePlaylistEntry.setTrackId(str);
          int j = localMusicFile.getSourceType();
          paramSyncablePlaylistEntry.setServerSourceFromClientSourceType(j);
          this.mStore.endRead(localSQLiteDatabase);
          bool = true;
        }
      }
    }
    finally
    {
      this.mStore.endRead(localSQLiteDatabase);
    }
  }

  private boolean fillInRemoteIdOfParentPlaylist(SyncablePlaylistEntry paramSyncablePlaylistEntry)
  {
    PlayList localPlayList1 = new PlayList();
    SQLiteDatabase localSQLiteDatabase = this.mStore.beginRead();
    try
    {
      long l = paramSyncablePlaylistEntry.mItem.getListId();
      PlayList localPlayList2 = PlayList.readPlayList(localSQLiteDatabase, l, localPlayList1);
      if (localPlayList1 != null)
      {
        String str1 = localPlayList1.getSourceId();
        if (str1 != null)
          break label63;
      }
      for (boolean bool = false; ; bool = true)
      {
        return bool;
        label63: String str2 = localPlayList1.getSourceId();
        paramSyncablePlaylistEntry.mRemotePlaylistId = str2;
        this.mStore.endRead(localSQLiteDatabase);
      }
    }
    finally
    {
      this.mStore.endRead(localSQLiteDatabase);
    }
  }

  // ERROR //
  private void handleDeleteEntry(SyncablePlaylistEntry paramSyncablePlaylistEntry)
    throws ServiceUnavailableException, AuthenticatorException, HardSyncException, SoftSyncException, ConflictException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   4: ifeq +14 -> 18
    //   7: aload_0
    //   8: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   11: ldc_w 424
    //   14: invokestatic 223	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   17: istore_2
    //   18: aload_0
    //   19: getfield 60	com/google/android/music/sync/google/MusicUpstreamSender:mClient	Lcom/google/android/music/sync/api/MusicApiClient;
    //   22: astore_3
    //   23: aload_0
    //   24: getfield 86	com/google/android/music/sync/google/MusicUpstreamSender:mAccount	Landroid/accounts/Account;
    //   27: astore 4
    //   29: getstatic 430	com/google/android/music/sync/api/MusicApiClient$OpType:DELETE	Lcom/google/android/music/sync/api/MusicApiClient$OpType;
    //   32: astore 5
    //   34: aload_3
    //   35: aload 4
    //   37: aload_1
    //   38: aload 5
    //   40: invokeinterface 436 4 0
    //   45: iconst_0
    //   46: istore 6
    //   48: aload_0
    //   49: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   52: invokevirtual 114	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   55: astore 7
    //   57: aload_0
    //   58: aload_1
    //   59: aload 7
    //   61: invokespecial 211	com/google/android/music/sync/google/MusicUpstreamSender:cleanupDelete	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;Landroid/database/sqlite/SQLiteDatabase;)V
    //   64: aload_0
    //   65: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   68: aload 7
    //   70: iconst_1
    //   71: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   74: return
    //   75: astore 8
    //   77: aload_0
    //   78: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   81: ldc_w 438
    //   84: aload 8
    //   86: invokestatic 441	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   89: istore 9
    //   91: goto -46 -> 45
    //   94: astore 10
    //   96: new 121	java/lang/StringBuilder
    //   99: dup
    //   100: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   103: ldc_w 443
    //   106: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: astore 11
    //   111: aload 10
    //   113: invokevirtual 446	com/google/android/music/sync/common/SyncHttpException:getStatusCode	()I
    //   116: istore 12
    //   118: aload 11
    //   120: iload 12
    //   122: invokevirtual 131	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   125: ldc_w 448
    //   128: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   134: astore 13
    //   136: new 104	com/google/android/music/sync/common/HardSyncException
    //   139: dup
    //   140: aload 13
    //   142: aload 10
    //   144: invokespecial 451	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   147: athrow
    //   148: astore 14
    //   150: new 106	com/google/android/music/sync/common/SoftSyncException
    //   153: dup
    //   154: ldc_w 453
    //   157: aload 14
    //   159: invokespecial 454	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   162: athrow
    //   163: astore 15
    //   165: aload_0
    //   166: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   169: ldc_w 456
    //   172: invokestatic 243	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   175: istore 16
    //   177: goto -132 -> 45
    //   180: astore 17
    //   182: aload_0
    //   183: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   186: ldc_w 458
    //   189: invokestatic 243	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   192: istore 18
    //   194: goto -149 -> 45
    //   197: astore 19
    //   199: aload_0
    //   200: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   203: ldc_w 460
    //   206: invokestatic 243	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   209: istore 20
    //   211: goto -166 -> 45
    //   214: astore 21
    //   216: aload_0
    //   217: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   220: aload 7
    //   222: iload 6
    //   224: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   227: aload 21
    //   229: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   18	45	75	com/google/android/music/store/InvalidDataException
    //   18	45	94	com/google/android/music/sync/common/SyncHttpException
    //   18	45	148	java/io/IOException
    //   18	45	163	com/google/android/music/sync/api/BadRequestException
    //   18	45	180	com/google/android/music/sync/api/ForbiddenException
    //   18	45	197	com/google/android/music/sync/api/ResourceNotFoundException
    //   57	64	214	finally
  }

  // ERROR //
  private void handleDeletePlaylist(SyncablePlaylist paramSyncablePlaylist)
    throws ServiceUnavailableException, AuthenticatorException, HardSyncException, SoftSyncException, ConflictException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   4: ifeq +14 -> 18
    //   7: aload_0
    //   8: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   11: ldc_w 464
    //   14: invokestatic 223	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   17: istore_2
    //   18: aload_0
    //   19: getfield 60	com/google/android/music/sync/google/MusicUpstreamSender:mClient	Lcom/google/android/music/sync/api/MusicApiClient;
    //   22: astore_3
    //   23: aload_0
    //   24: getfield 86	com/google/android/music/sync/google/MusicUpstreamSender:mAccount	Landroid/accounts/Account;
    //   27: astore 4
    //   29: getstatic 430	com/google/android/music/sync/api/MusicApiClient$OpType:DELETE	Lcom/google/android/music/sync/api/MusicApiClient$OpType;
    //   32: astore 5
    //   34: aload_3
    //   35: aload 4
    //   37: aload_1
    //   38: aload 5
    //   40: invokeinterface 436 4 0
    //   45: iconst_0
    //   46: istore 6
    //   48: aload_0
    //   49: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   52: invokevirtual 114	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   55: astore 7
    //   57: aload_0
    //   58: aload_1
    //   59: aload 7
    //   61: invokespecial 211	com/google/android/music/sync/google/MusicUpstreamSender:cleanupDelete	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;Landroid/database/sqlite/SQLiteDatabase;)V
    //   64: aload_0
    //   65: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   68: aload 7
    //   70: iconst_1
    //   71: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   74: return
    //   75: astore 8
    //   77: aload_0
    //   78: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   81: ldc_w 466
    //   84: aload 8
    //   86: invokestatic 441	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   89: istore 9
    //   91: goto -46 -> 45
    //   94: astore 10
    //   96: new 121	java/lang/StringBuilder
    //   99: dup
    //   100: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   103: ldc_w 443
    //   106: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: astore 11
    //   111: aload 10
    //   113: invokevirtual 446	com/google/android/music/sync/common/SyncHttpException:getStatusCode	()I
    //   116: istore 12
    //   118: aload 11
    //   120: iload 12
    //   122: invokevirtual 131	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   125: ldc_w 468
    //   128: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   134: astore 13
    //   136: new 104	com/google/android/music/sync/common/HardSyncException
    //   139: dup
    //   140: aload 13
    //   142: aload 10
    //   144: invokespecial 451	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   147: athrow
    //   148: astore 14
    //   150: new 106	com/google/android/music/sync/common/SoftSyncException
    //   153: dup
    //   154: ldc_w 470
    //   157: aload 14
    //   159: invokespecial 454	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   162: athrow
    //   163: astore 15
    //   165: aload_0
    //   166: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   169: ldc_w 472
    //   172: invokestatic 475	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   175: istore 16
    //   177: goto -132 -> 45
    //   180: astore 17
    //   182: aload_0
    //   183: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   186: ldc_w 477
    //   189: invokestatic 475	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   192: istore 18
    //   194: goto -149 -> 45
    //   197: astore 19
    //   199: aload_0
    //   200: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   203: ldc_w 479
    //   206: invokestatic 475	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   209: istore 20
    //   211: goto -166 -> 45
    //   214: astore 21
    //   216: aload_0
    //   217: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   220: aload 7
    //   222: iload 6
    //   224: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   227: aload 21
    //   229: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   18	45	75	com/google/android/music/store/InvalidDataException
    //   18	45	94	com/google/android/music/sync/common/SyncHttpException
    //   18	45	148	java/io/IOException
    //   18	45	163	com/google/android/music/sync/api/BadRequestException
    //   18	45	180	com/google/android/music/sync/api/ForbiddenException
    //   18	45	197	com/google/android/music/sync/api/ResourceNotFoundException
    //   57	64	214	finally
  }

  // ERROR //
  private void handleDeleteRadioStation(SyncableRadioStation paramSyncableRadioStation)
    throws ServiceUnavailableException, AuthenticatorException, HardSyncException, SoftSyncException, ConflictException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   4: ifeq +14 -> 18
    //   7: aload_0
    //   8: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   11: ldc_w 483
    //   14: invokestatic 223	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   17: istore_2
    //   18: new 485	java/util/ArrayList
    //   21: dup
    //   22: invokespecial 486	java/util/ArrayList:<init>	()V
    //   25: astore_3
    //   26: aload_3
    //   27: aload_1
    //   28: invokeinterface 490 2 0
    //   33: istore 4
    //   35: aload_0
    //   36: getfield 60	com/google/android/music/sync/google/MusicUpstreamSender:mClient	Lcom/google/android/music/sync/api/MusicApiClient;
    //   39: astore 5
    //   41: aload_0
    //   42: getfield 86	com/google/android/music/sync/google/MusicUpstreamSender:mAccount	Landroid/accounts/Account;
    //   45: astore 6
    //   47: aload 5
    //   49: aload 6
    //   51: aload_3
    //   52: invokeinterface 494 3 0
    //   57: invokeinterface 498 1 0
    //   62: astore 7
    //   64: aload 7
    //   66: invokeinterface 503 1 0
    //   71: ifeq +35 -> 106
    //   74: aload 7
    //   76: invokeinterface 507 1 0
    //   81: checkcast 153	com/google/android/music/sync/google/model/BatchMutateResponse$MutateResponse
    //   84: invokevirtual 510	com/google/android/music/sync/google/model/BatchMutateResponse$MutateResponse:throwExceptionFromResponseCode	()V
    //   87: goto -23 -> 64
    //   90: astore 8
    //   92: aload_0
    //   93: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   96: ldc_w 512
    //   99: aload 8
    //   101: invokestatic 441	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   104: istore 9
    //   106: iconst_0
    //   107: istore 10
    //   109: aload_0
    //   110: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   113: invokevirtual 114	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   116: astore 11
    //   118: aload_0
    //   119: aload_1
    //   120: aload 11
    //   122: invokespecial 211	com/google/android/music/sync/google/MusicUpstreamSender:cleanupDelete	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;Landroid/database/sqlite/SQLiteDatabase;)V
    //   125: aload_0
    //   126: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   129: aload 11
    //   131: iconst_1
    //   132: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   135: return
    //   136: astore 12
    //   138: new 121	java/lang/StringBuilder
    //   141: dup
    //   142: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   145: ldc_w 443
    //   148: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: astore 13
    //   153: aload 12
    //   155: invokevirtual 446	com/google/android/music/sync/common/SyncHttpException:getStatusCode	()I
    //   158: istore 14
    //   160: aload 13
    //   162: iload 14
    //   164: invokevirtual 131	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   167: ldc_w 514
    //   170: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   173: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   176: astore 15
    //   178: new 104	com/google/android/music/sync/common/HardSyncException
    //   181: dup
    //   182: aload 15
    //   184: aload 12
    //   186: invokespecial 451	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   189: athrow
    //   190: astore 16
    //   192: new 106	com/google/android/music/sync/common/SoftSyncException
    //   195: dup
    //   196: ldc_w 516
    //   199: aload 16
    //   201: invokespecial 454	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   204: athrow
    //   205: astore 17
    //   207: aload_0
    //   208: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   211: ldc_w 518
    //   214: invokestatic 243	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   217: istore 18
    //   219: goto -113 -> 106
    //   222: astore 19
    //   224: aload_0
    //   225: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   228: ldc_w 520
    //   231: invokestatic 243	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   234: istore 20
    //   236: goto -130 -> 106
    //   239: astore 21
    //   241: aload_0
    //   242: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   245: ldc_w 522
    //   248: invokestatic 243	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   251: istore 22
    //   253: goto -147 -> 106
    //   256: astore 23
    //   258: aload_0
    //   259: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   262: aload 11
    //   264: iload 10
    //   266: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   269: aload 23
    //   271: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   18	87	90	com/google/android/music/store/InvalidDataException
    //   18	87	136	com/google/android/music/sync/common/SyncHttpException
    //   18	87	190	java/io/IOException
    //   18	87	205	com/google/android/music/sync/api/BadRequestException
    //   18	87	222	com/google/android/music/sync/api/ForbiddenException
    //   18	87	239	com/google/android/music/sync/api/ResourceNotFoundException
    //   118	125	256	finally
  }

  // ERROR //
  private void handleDeleteTrack(TrackTombstone paramTrackTombstone)
    throws ServiceUnavailableException, AuthenticatorException, HardSyncException, SoftSyncException, ConflictException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   4: ifeq +14 -> 18
    //   7: aload_0
    //   8: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   11: ldc_w 526
    //   14: invokestatic 223	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   17: istore_2
    //   18: aload_0
    //   19: getfield 60	com/google/android/music/sync/google/MusicUpstreamSender:mClient	Lcom/google/android/music/sync/api/MusicApiClient;
    //   22: astore_3
    //   23: aload_0
    //   24: getfield 86	com/google/android/music/sync/google/MusicUpstreamSender:mAccount	Landroid/accounts/Account;
    //   27: astore 4
    //   29: getstatic 430	com/google/android/music/sync/api/MusicApiClient$OpType:DELETE	Lcom/google/android/music/sync/api/MusicApiClient$OpType;
    //   32: astore 5
    //   34: aload_3
    //   35: aload 4
    //   37: aload_1
    //   38: aload 5
    //   40: invokeinterface 436 4 0
    //   45: iconst_0
    //   46: istore 6
    //   48: aload_0
    //   49: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   52: invokevirtual 114	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   55: astore 7
    //   57: aload_0
    //   58: aload_1
    //   59: aload 7
    //   61: invokespecial 211	com/google/android/music/sync/google/MusicUpstreamSender:cleanupDelete	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;Landroid/database/sqlite/SQLiteDatabase;)V
    //   64: aload_0
    //   65: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   68: aload 7
    //   70: iconst_1
    //   71: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   74: return
    //   75: astore 8
    //   77: aload_0
    //   78: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   81: ldc_w 528
    //   84: aload 8
    //   86: invokestatic 441	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   89: istore 9
    //   91: goto -46 -> 45
    //   94: astore 10
    //   96: new 121	java/lang/StringBuilder
    //   99: dup
    //   100: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   103: ldc_w 443
    //   106: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: astore 11
    //   111: aload 10
    //   113: invokevirtual 446	com/google/android/music/sync/common/SyncHttpException:getStatusCode	()I
    //   116: istore 12
    //   118: aload 11
    //   120: iload 12
    //   122: invokevirtual 131	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   125: ldc_w 530
    //   128: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   134: astore 13
    //   136: new 104	com/google/android/music/sync/common/HardSyncException
    //   139: dup
    //   140: aload 13
    //   142: aload 10
    //   144: invokespecial 451	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   147: athrow
    //   148: astore 14
    //   150: new 106	com/google/android/music/sync/common/SoftSyncException
    //   153: dup
    //   154: ldc_w 532
    //   157: aload 14
    //   159: invokespecial 454	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   162: athrow
    //   163: astore 15
    //   165: aload_0
    //   166: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   169: ldc_w 534
    //   172: invokestatic 243	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   175: istore 16
    //   177: goto -132 -> 45
    //   180: astore 17
    //   182: aload_0
    //   183: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   186: ldc_w 536
    //   189: invokestatic 243	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   192: istore 18
    //   194: goto -149 -> 45
    //   197: astore 19
    //   199: aload_0
    //   200: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   203: ldc_w 538
    //   206: invokestatic 243	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   209: istore 20
    //   211: goto -166 -> 45
    //   214: astore 21
    //   216: aload_0
    //   217: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   220: aload 7
    //   222: iload 6
    //   224: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   227: aload 21
    //   229: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   18	45	75	com/google/android/music/store/InvalidDataException
    //   18	45	94	com/google/android/music/sync/common/SyncHttpException
    //   18	45	148	java/io/IOException
    //   18	45	163	com/google/android/music/sync/api/BadRequestException
    //   18	45	180	com/google/android/music/sync/api/ForbiddenException
    //   18	45	197	com/google/android/music/sync/api/ResourceNotFoundException
    //   57	64	214	finally
  }

  private void handleInsertEntry(SyncablePlaylistEntry paramSyncablePlaylistEntry)
    throws ServiceUnavailableException, AuthenticatorException, HardSyncException, SoftSyncException, ConflictException
  {
    if (!fillInRemoteIdOfParentPlaylist(paramSyncablePlaylistEntry))
    {
      if (!this.mUseVerboseLogging)
        return;
      String str1 = this.mTag;
      StringBuilder localStringBuilder1 = new StringBuilder().append("Upstream sender: Found an inserted playlist entry whose parent has no remote id.  Skipping it until the next sync.");
      String str2 = paramSyncablePlaylistEntry.toString();
      String str3 = str2;
      int i = Log.v(str1, str3);
      return;
    }
    if (!fillInRemoteIdForTrack(paramSyncablePlaylistEntry))
    {
      if (!this.mUseVerboseLogging)
        return;
      String str4 = this.mTag;
      StringBuilder localStringBuilder2 = new StringBuilder().append("Upstream sender: Found an inserted playlist entry whose track has no remote id.  Skipping it until the next sync.");
      String str5 = paramSyncablePlaylistEntry.toString();
      String str6 = str5;
      int j = Log.v(str4, str6);
      return;
    }
    paramSyncablePlaylistEntry.mAbsolutePosition = null;
    if (this.mUseVerboseLogging)
      int k = Log.v(this.mTag, "Upstream sender: Sending inserted playlist entry to cloud.");
    try
    {
      MusicApiClient localMusicApiClient = this.mClient;
      Account localAccount = this.mAccount;
      MusicApiClient.OpType localOpType = MusicApiClient.OpType.INSERT;
      localMusicApiClient.mutateItem(localAccount, paramSyncablePlaylistEntry, localOpType);
      if (paramSyncablePlaylistEntry.mRemoteId == null)
      {
        int m = Log.e(this.mTag, "An inserted playlist entry was returned without a server id.  Skipping.");
        return;
      }
    }
    catch (SyncHttpException localSyncHttpException)
    {
      StringBuilder localStringBuilder3 = new StringBuilder().append("Http code ");
      int n = localSyncHttpException.getStatusCode();
      String str7 = n + " on upstream playlist entry insert.";
      throw new HardSyncException(str7, localSyncHttpException);
    }
    catch (InvalidDataException localInvalidDataException)
    {
      while (true)
        int i1 = Log.e(this.mTag, "Invalid data on playlist entry insert.  Skipping item.", localInvalidDataException);
    }
    catch (IOException localIOException)
    {
      throw new SoftSyncException("IO error on upstream playlist entry insert.", localIOException);
    }
    catch (BadRequestException localBadRequestException)
    {
      while (true)
      {
        if (this.mUseVerboseLogging)
          int i2 = Log.v(this.mTag, "Upstream sender: Server returned 400 on insert. Removing local copy.", localBadRequestException);
        long l1 = paramSyncablePlaylistEntry.mLocalId;
        deletePlaylistEntry(l1);
      }
    }
    catch (ForbiddenException localForbiddenException)
    {
      while (true)
      {
        if (this.mUseVerboseLogging)
          int i3 = Log.v(this.mTag, "Upstream sender: Server returned 403 on insert. Removing local copy.", localForbiddenException);
        long l2 = paramSyncablePlaylistEntry.mLocalId;
        deletePlaylistEntry(l2);
      }
    }
    catch (ResourceNotFoundException localResourceNotFoundException)
    {
      int i4 = Log.e(this.mTag, "Unexpected 404 on playlist entry insert.  Skipping.", localResourceNotFoundException);
      return;
    }
    boolean bool = false;
    SQLiteDatabase localSQLiteDatabase = this.mStore.beginWriteTxn();
    try
    {
      cleanupInsert(paramSyncablePlaylistEntry, localSQLiteDatabase);
      this.mStore.endWriteTxn(localSQLiteDatabase, true);
      return;
    }
    finally
    {
      this.mStore.endWriteTxn(localSQLiteDatabase, bool);
    }
  }

  private void handleInsertPlaylist(SyncablePlaylist paramSyncablePlaylist)
    throws ServiceUnavailableException, AuthenticatorException, HardSyncException, SoftSyncException, ConflictException
  {
    if (this.mUseVerboseLogging)
      int i = Log.v(this.mTag, "Upstream sender: Sending inserted playlist to cloud.");
    try
    {
      MusicApiClient localMusicApiClient = this.mClient;
      Account localAccount = this.mAccount;
      MusicApiClient.OpType localOpType = MusicApiClient.OpType.INSERT;
      localMusicApiClient.mutateItem(localAccount, paramSyncablePlaylist, localOpType);
      if (paramSyncablePlaylist.mRemoteId == null)
      {
        int j = Log.e(this.mTag, "An inserted playlist was returned without a server id.  Skipping.");
        return;
      }
    }
    catch (SyncHttpException localSyncHttpException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Http code ");
      int k = localSyncHttpException.getStatusCode();
      String str = k + " on upstream playlist insert.";
      throw new HardSyncException(str, localSyncHttpException);
    }
    catch (InvalidDataException localInvalidDataException)
    {
      while (true)
        int m = Log.e(this.mTag, "Invalid data on playlist insert.  Skipping item.", localInvalidDataException);
    }
    catch (IOException localIOException)
    {
      throw new SoftSyncException("IO error on upstream playlist insert.", localIOException);
    }
    catch (BadRequestException localBadRequestException)
    {
      while (true)
      {
        if (this.mUseVerboseLogging)
          int n = Log.v(this.mTag, "Upstream sender: Server returned 400 on insert. Removing local copy.", localBadRequestException);
        long l1 = paramSyncablePlaylist.mLocalId;
        deletePlaylist(l1);
      }
    }
    catch (ForbiddenException localForbiddenException)
    {
      while (true)
      {
        if (this.mUseVerboseLogging)
          int i1 = Log.v(this.mTag, "Upstream sender: Server returned 403 on insert. Removing local copy.", localForbiddenException);
        long l2 = paramSyncablePlaylist.mLocalId;
        deletePlaylist(l2);
      }
    }
    catch (ResourceNotFoundException localResourceNotFoundException)
    {
      int i2 = Log.e(this.mTag, "Unexpected 404 on playlist insert.  Skipping.", localResourceNotFoundException);
      return;
    }
    boolean bool = false;
    SQLiteDatabase localSQLiteDatabase = this.mStore.beginWriteTxn();
    try
    {
      cleanupInsert(paramSyncablePlaylist, localSQLiteDatabase);
      this.mStore.endWriteTxn(localSQLiteDatabase, true);
      return;
    }
    finally
    {
      this.mStore.endWriteTxn(localSQLiteDatabase, bool);
    }
  }

  // ERROR //
  private void handleInsertRadioStation(SyncableRadioStation paramSyncableRadioStation)
    throws ServiceUnavailableException, AuthenticatorException, HardSyncException, SoftSyncException, ConflictException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   4: ifeq +14 -> 18
    //   7: aload_0
    //   8: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   11: ldc_w 600
    //   14: invokestatic 223	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   17: istore_2
    //   18: new 485	java/util/ArrayList
    //   21: dup
    //   22: invokespecial 486	java/util/ArrayList:<init>	()V
    //   25: astore_3
    //   26: aload_3
    //   27: aload_1
    //   28: invokeinterface 490 2 0
    //   33: istore 4
    //   35: aload_0
    //   36: getfield 60	com/google/android/music/sync/google/MusicUpstreamSender:mClient	Lcom/google/android/music/sync/api/MusicApiClient;
    //   39: astore 5
    //   41: aload_0
    //   42: getfield 86	com/google/android/music/sync/google/MusicUpstreamSender:mAccount	Landroid/accounts/Account;
    //   45: astore 6
    //   47: aload 5
    //   49: aload 6
    //   51: aload_3
    //   52: invokeinterface 494 3 0
    //   57: invokeinterface 498 1 0
    //   62: astore 7
    //   64: aload 7
    //   66: invokeinterface 503 1 0
    //   71: ifeq +35 -> 106
    //   74: aload 7
    //   76: invokeinterface 507 1 0
    //   81: checkcast 153	com/google/android/music/sync/google/model/BatchMutateResponse$MutateResponse
    //   84: invokevirtual 510	com/google/android/music/sync/google/model/BatchMutateResponse$MutateResponse:throwExceptionFromResponseCode	()V
    //   87: goto -23 -> 64
    //   90: astore 8
    //   92: aload_0
    //   93: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   96: ldc_w 602
    //   99: aload 8
    //   101: invokestatic 441	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   104: istore 9
    //   106: iconst_0
    //   107: istore 10
    //   109: aload_0
    //   110: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   113: invokevirtual 114	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   116: astore 11
    //   118: aload_0
    //   119: aload_1
    //   120: aload 11
    //   122: invokespecial 199	com/google/android/music/sync/google/MusicUpstreamSender:cleanupInsert	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;Landroid/database/sqlite/SQLiteDatabase;)V
    //   125: aload_0
    //   126: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   129: aload 11
    //   131: iconst_1
    //   132: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   135: return
    //   136: astore 12
    //   138: new 121	java/lang/StringBuilder
    //   141: dup
    //   142: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   145: ldc_w 443
    //   148: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: astore 13
    //   153: aload 12
    //   155: invokevirtual 446	com/google/android/music/sync/common/SyncHttpException:getStatusCode	()I
    //   158: istore 14
    //   160: aload 13
    //   162: iload 14
    //   164: invokevirtual 131	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   167: ldc_w 604
    //   170: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   173: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   176: astore 15
    //   178: new 104	com/google/android/music/sync/common/HardSyncException
    //   181: dup
    //   182: aload 15
    //   184: aload 12
    //   186: invokespecial 451	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   189: athrow
    //   190: astore 16
    //   192: new 106	com/google/android/music/sync/common/SoftSyncException
    //   195: dup
    //   196: ldc_w 606
    //   199: aload 16
    //   201: invokespecial 454	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   204: athrow
    //   205: astore 17
    //   207: aload_0
    //   208: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   211: ifeq +17 -> 228
    //   214: aload_0
    //   215: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   218: ldc_w 608
    //   221: aload 17
    //   223: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   226: istore 18
    //   228: aload_1
    //   229: invokevirtual 609	com/google/android/music/sync/google/model/SyncableRadioStation:getLocalId	()J
    //   232: lstore 19
    //   234: aload_0
    //   235: lload 19
    //   237: invokespecial 611	com/google/android/music/sync/google/MusicUpstreamSender:deleteRadioStation	(J)V
    //   240: goto -134 -> 106
    //   243: astore 17
    //   245: aload_0
    //   246: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   249: ifeq +17 -> 266
    //   252: aload_0
    //   253: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   256: ldc_w 613
    //   259: aload 17
    //   261: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   264: istore 21
    //   266: aload_1
    //   267: invokevirtual 609	com/google/android/music/sync/google/model/SyncableRadioStation:getLocalId	()J
    //   270: lstore 22
    //   272: aload_0
    //   273: lload 22
    //   275: invokespecial 611	com/google/android/music/sync/google/MusicUpstreamSender:deleteRadioStation	(J)V
    //   278: goto -172 -> 106
    //   281: astore 17
    //   283: aload_0
    //   284: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   287: ifeq +53 -> 340
    //   290: aload_0
    //   291: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   294: astore 24
    //   296: new 121	java/lang/StringBuilder
    //   299: dup
    //   300: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   303: ldc_w 615
    //   306: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   309: astore 25
    //   311: aload_1
    //   312: invokevirtual 609	com/google/android/music/sync/google/model/SyncableRadioStation:getLocalId	()J
    //   315: lstore 26
    //   317: aload 25
    //   319: lload 26
    //   321: invokevirtual 618	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   324: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   327: astore 28
    //   329: aload 24
    //   331: aload 28
    //   333: aload 17
    //   335: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   338: istore 29
    //   340: aload_1
    //   341: invokevirtual 609	com/google/android/music/sync/google/model/SyncableRadioStation:getLocalId	()J
    //   344: lstore 30
    //   346: aload_0
    //   347: lload 30
    //   349: invokespecial 611	com/google/android/music/sync/google/MusicUpstreamSender:deleteRadioStation	(J)V
    //   352: goto -246 -> 106
    //   355: astore 32
    //   357: aload_0
    //   358: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   361: aload 11
    //   363: iload 10
    //   365: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   368: aload 32
    //   370: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   18	87	90	com/google/android/music/store/InvalidDataException
    //   18	87	136	com/google/android/music/sync/common/SyncHttpException
    //   18	87	190	java/io/IOException
    //   18	87	205	com/google/android/music/sync/api/BadRequestException
    //   18	87	243	com/google/android/music/sync/api/ForbiddenException
    //   18	87	281	com/google/android/music/sync/api/ResourceNotFoundException
    //   118	125	355	finally
  }

  // ERROR //
  private void handleInsertTrack(Track paramTrack)
    throws ServiceUnavailableException, AuthenticatorException, HardSyncException, SoftSyncException, ConflictException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   4: ifeq +14 -> 18
    //   7: aload_0
    //   8: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   11: ldc_w 622
    //   14: invokestatic 223	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   17: istore_2
    //   18: aload_0
    //   19: getfield 60	com/google/android/music/sync/google/MusicUpstreamSender:mClient	Lcom/google/android/music/sync/api/MusicApiClient;
    //   22: astore_3
    //   23: aload_0
    //   24: getfield 86	com/google/android/music/sync/google/MusicUpstreamSender:mAccount	Landroid/accounts/Account;
    //   27: astore 4
    //   29: getstatic 556	com/google/android/music/sync/api/MusicApiClient$OpType:INSERT	Lcom/google/android/music/sync/api/MusicApiClient$OpType;
    //   32: astore 5
    //   34: aload_3
    //   35: aload 4
    //   37: aload_1
    //   38: aload 5
    //   40: invokeinterface 436 4 0
    //   45: iconst_0
    //   46: istore 6
    //   48: aload_0
    //   49: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   52: invokevirtual 114	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   55: astore 7
    //   57: aload_0
    //   58: aload_1
    //   59: aload 7
    //   61: invokespecial 199	com/google/android/music/sync/google/MusicUpstreamSender:cleanupInsert	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;Landroid/database/sqlite/SQLiteDatabase;)V
    //   64: aload_0
    //   65: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   68: aload 7
    //   70: iconst_1
    //   71: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   74: return
    //   75: astore 8
    //   77: aload_0
    //   78: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   81: ldc_w 624
    //   84: aload 8
    //   86: invokestatic 441	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   89: istore 9
    //   91: goto -46 -> 45
    //   94: astore 10
    //   96: new 121	java/lang/StringBuilder
    //   99: dup
    //   100: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   103: ldc_w 443
    //   106: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: astore 11
    //   111: aload 10
    //   113: invokevirtual 446	com/google/android/music/sync/common/SyncHttpException:getStatusCode	()I
    //   116: istore 12
    //   118: aload 11
    //   120: iload 12
    //   122: invokevirtual 131	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   125: ldc_w 626
    //   128: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   134: astore 13
    //   136: new 104	com/google/android/music/sync/common/HardSyncException
    //   139: dup
    //   140: aload 13
    //   142: aload 10
    //   144: invokespecial 451	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   147: athrow
    //   148: astore 14
    //   150: new 106	com/google/android/music/sync/common/SoftSyncException
    //   153: dup
    //   154: ldc_w 628
    //   157: aload 14
    //   159: invokespecial 454	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   162: athrow
    //   163: astore 15
    //   165: aload_0
    //   166: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   169: ifeq +17 -> 186
    //   172: aload_0
    //   173: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   176: ldc_w 630
    //   179: aload 15
    //   181: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   184: istore 16
    //   186: aload_0
    //   187: getfield 50	com/google/android/music/sync/google/MusicUpstreamSender:mContext	Landroid/content/Context;
    //   190: astore 17
    //   192: aload_1
    //   193: invokevirtual 631	com/google/android/music/sync/google/model/Track:getLocalId	()J
    //   196: lstore 18
    //   198: aload 17
    //   200: lload 18
    //   202: invokestatic 344	com/google/android/music/store/MusicFile:deleteByLocalId	(Landroid/content/Context;J)V
    //   205: goto -160 -> 45
    //   208: astore 15
    //   210: aload_0
    //   211: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   214: ifeq +17 -> 231
    //   217: aload_0
    //   218: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   221: ldc_w 633
    //   224: aload 15
    //   226: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   229: istore 20
    //   231: aload_0
    //   232: getfield 50	com/google/android/music/sync/google/MusicUpstreamSender:mContext	Landroid/content/Context;
    //   235: astore 21
    //   237: aload_1
    //   238: invokevirtual 631	com/google/android/music/sync/google/model/Track:getLocalId	()J
    //   241: lstore 22
    //   243: aload 21
    //   245: lload 22
    //   247: invokestatic 344	com/google/android/music/store/MusicFile:deleteByLocalId	(Landroid/content/Context;J)V
    //   250: goto -205 -> 45
    //   253: astore 15
    //   255: aload_0
    //   256: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   259: ifeq +53 -> 312
    //   262: aload_0
    //   263: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   266: astore 24
    //   268: new 121	java/lang/StringBuilder
    //   271: dup
    //   272: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   275: ldc_w 635
    //   278: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   281: astore 25
    //   283: aload_1
    //   284: invokevirtual 631	com/google/android/music/sync/google/model/Track:getLocalId	()J
    //   287: lstore 26
    //   289: aload 25
    //   291: lload 26
    //   293: invokevirtual 618	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   296: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   299: astore 28
    //   301: aload 24
    //   303: aload 28
    //   305: aload 15
    //   307: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   310: istore 29
    //   312: aload_0
    //   313: getfield 50	com/google/android/music/sync/google/MusicUpstreamSender:mContext	Landroid/content/Context;
    //   316: astore 30
    //   318: aload_1
    //   319: invokevirtual 631	com/google/android/music/sync/google/model/Track:getLocalId	()J
    //   322: lstore 31
    //   324: aload 30
    //   326: lload 31
    //   328: invokestatic 344	com/google/android/music/store/MusicFile:deleteByLocalId	(Landroid/content/Context;J)V
    //   331: goto -286 -> 45
    //   334: astore 33
    //   336: aload_0
    //   337: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   340: aload 7
    //   342: iload 6
    //   344: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   347: aload 33
    //   349: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   18	45	75	com/google/android/music/store/InvalidDataException
    //   18	45	94	com/google/android/music/sync/common/SyncHttpException
    //   18	45	148	java/io/IOException
    //   18	45	163	com/google/android/music/sync/api/BadRequestException
    //   18	45	208	com/google/android/music/sync/api/ForbiddenException
    //   18	45	253	com/google/android/music/sync/api/ResourceNotFoundException
    //   57	64	334	finally
  }

  private void handleMutations(List<? extends MusicQueueableSyncEntity> paramList)
    throws AuthenticatorException, HardSyncException, ConflictException, SoftSyncException, ServiceUnavailableException
  {
    int i = 0;
    validateEntryParentsAndRemoteTrackIds(paramList);
    try
    {
      MusicApiClient localMusicApiClient = this.mClient;
      Account localAccount = this.mAccount;
      List localList1 = localMusicApiClient.mutateItems(localAccount, paramList);
      localList2 = localList1;
      k = 0;
      if (k == 0)
      {
        cleanupBatchMutations(paramList, localList2);
        return;
      }
    }
    catch (InvalidDataException localInvalidDataException)
    {
      while (true)
      {
        int m = Log.w(this.mTag, "At least one record was found with invalid data while handling a batched mutations.");
        localList2 = null;
        k = 1;
      }
    }
    catch (SyncHttpException localSyncHttpException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Http code ");
      int n = localSyncHttpException.getStatusCode();
      String str1 = n + " on handling batched playlist mutations.";
      throw new HardSyncException(str1, localSyncHttpException);
    }
    catch (IOException localIOException)
    {
      throw new SoftSyncException("IO error on handing batched mutations.", localIOException);
    }
    catch (BadRequestException localBadRequestException)
    {
      while (true)
      {
        int i1 = Log.e(this.mTag, "Bad-request returned while handling batched mutations.", localBadRequestException);
        localList2 = null;
        k = 1;
      }
    }
    catch (ForbiddenException localForbiddenException)
    {
      while (true)
      {
        int i2 = Log.e(this.mTag, "Forbidden returned while handling batched mutations.", localForbiddenException);
        localList2 = null;
        k = 1;
      }
    }
    catch (ResourceNotFoundException localResourceNotFoundException)
    {
      while (true)
      {
        int i3 = Log.e(this.mTag, "Not-found returned while handling batched mutations.", localResourceNotFoundException);
        List localList2 = null;
        int k = 1;
      }
      if (this.mUseVerboseLogging)
        int i4 = Log.v(this.mTag, "Handling entity mutations individually due to one or more errors.");
      Iterator localIterator = paramList.iterator();
      int j = 0;
      while (true)
        if (localIterator.hasNext())
        {
          Object localObject = (MusicQueueableSyncEntity)localIterator.next();
          try
          {
            if ((localObject instanceof SyncablePlaylist))
            {
              localObject = (SyncablePlaylist)SyncablePlaylist.class.cast(localObject);
              if (((SyncablePlaylist)localObject).mIsDeleted)
                handleDeletePlaylist((SyncablePlaylist)localObject);
              while (true)
              {
                i = j;
                int i5 = i;
                break;
                if (((SyncablePlaylist)localObject).mRemoteId == null)
                  break label338;
                handleUpdatePlaylist((SyncablePlaylist)localObject);
              }
            }
          }
          catch (ConflictException localConflictException2)
          {
            while (true)
            {
              i = localConflictException2.getConflictCount() + j;
              continue;
              label338: handleInsertPlaylist((SyncablePlaylist)localObject);
              continue;
              if ((localObject instanceof SyncablePlaylistEntry))
              {
                localObject = (SyncablePlaylistEntry)SyncablePlaylistEntry.class.cast(localObject);
                if (((SyncablePlaylistEntry)localObject).mIsDeleted)
                  handleDeleteEntry((SyncablePlaylistEntry)localObject);
                else if (((SyncablePlaylistEntry)localObject).mRemoteId != null)
                  handleUpdateEntry((SyncablePlaylistEntry)localObject);
                else
                  handleInsertEntry((SyncablePlaylistEntry)localObject);
              }
              else if ((localObject instanceof Track))
              {
                localObject = (Track)Track.class.cast(localObject);
                if (((Track)localObject).isInsert())
                  handleInsertTrack((Track)localObject);
                else
                  handleUpdateTrack((Track)localObject);
              }
              else if ((localObject instanceof TrackTombstone))
              {
                TrackTombstone localTrackTombstone = (TrackTombstone)TrackTombstone.class.cast(localObject);
                handleDeleteTrack(localTrackTombstone);
              }
              else if ((localObject instanceof SyncableRadioStation))
              {
                localObject = (SyncableRadioStation)SyncableRadioStation.class.cast(localObject);
                if (((SyncableRadioStation)localObject).mIsDeleted)
                  handleDeleteRadioStation((SyncableRadioStation)localObject);
                else if (((SyncableRadioStation)localObject).mRemoteId != null)
                  handleUpdateRadioStation((SyncableRadioStation)localObject);
                else
                  handleInsertRadioStation((SyncableRadioStation)localObject);
              }
            }
          }
        }
      if (j <= 0)
        return;
      String str2 = j + " conflicts detected during individual mutations";
      ConflictException localConflictException1 = new ConflictException(str2);
      localConflictException1.setConflictCount(j);
      throw new ConflictException(localConflictException1);
    }
  }

  // ERROR //
  private void handleUpdateEntry(SyncablePlaylistEntry paramSyncablePlaylistEntry)
    throws ServiceUnavailableException, AuthenticatorException, HardSyncException, SoftSyncException, ConflictException
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: invokespecial 541	com/google/android/music/sync/google/MusicUpstreamSender:fillInRemoteIdOfParentPlaylist	(Lcom/google/android/music/sync/google/model/SyncablePlaylistEntry;)Z
    //   5: ifne +56 -> 61
    //   8: aload_0
    //   9: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   12: ifne +4 -> 16
    //   15: return
    //   16: aload_0
    //   17: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   20: astore_2
    //   21: new 121	java/lang/StringBuilder
    //   24: dup
    //   25: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   28: ldc_w 702
    //   31: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   34: astore_3
    //   35: aload_1
    //   36: invokevirtual 544	com/google/android/music/sync/google/model/SyncablePlaylistEntry:toString	()Ljava/lang/String;
    //   39: astore 4
    //   41: aload_3
    //   42: aload 4
    //   44: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   47: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   50: astore 5
    //   52: aload_2
    //   53: aload 5
    //   55: invokestatic 223	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   58: istore 6
    //   60: return
    //   61: aload_0
    //   62: aload_1
    //   63: invokespecial 546	com/google/android/music/sync/google/MusicUpstreamSender:fillInRemoteIdForTrack	(Lcom/google/android/music/sync/google/model/SyncablePlaylistEntry;)Z
    //   66: ifne +60 -> 126
    //   69: aload_0
    //   70: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   73: ifne +4 -> 77
    //   76: return
    //   77: aload_0
    //   78: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   81: astore 7
    //   83: new 121	java/lang/StringBuilder
    //   86: dup
    //   87: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   90: ldc_w 704
    //   93: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: astore 8
    //   98: aload_1
    //   99: invokevirtual 544	com/google/android/music/sync/google/model/SyncablePlaylistEntry:toString	()Ljava/lang/String;
    //   102: astore 9
    //   104: aload 8
    //   106: aload 9
    //   108: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   111: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   114: astore 10
    //   116: aload 7
    //   118: aload 10
    //   120: invokestatic 223	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   123: istore 11
    //   125: return
    //   126: aload_1
    //   127: aconst_null
    //   128: putfield 551	com/google/android/music/sync/google/model/SyncablePlaylistEntry:mAbsolutePosition	Ljava/lang/String;
    //   131: aload_0
    //   132: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   135: ifeq +15 -> 150
    //   138: aload_0
    //   139: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   142: ldc_w 706
    //   145: invokestatic 223	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   148: istore 12
    //   150: aload_0
    //   151: getfield 60	com/google/android/music/sync/google/MusicUpstreamSender:mClient	Lcom/google/android/music/sync/api/MusicApiClient;
    //   154: astore 13
    //   156: aload_0
    //   157: getfield 86	com/google/android/music/sync/google/MusicUpstreamSender:mAccount	Landroid/accounts/Account;
    //   160: astore 14
    //   162: getstatic 709	com/google/android/music/sync/api/MusicApiClient$OpType:UPDATE	Lcom/google/android/music/sync/api/MusicApiClient$OpType;
    //   165: astore 15
    //   167: aload 13
    //   169: aload 14
    //   171: aload_1
    //   172: aload 15
    //   174: invokeinterface 436 4 0
    //   179: iconst_0
    //   180: istore 16
    //   182: aload_0
    //   183: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   186: invokevirtual 114	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   189: astore 17
    //   191: aload_0
    //   192: aload_1
    //   193: aload 17
    //   195: invokespecial 205	com/google/android/music/sync/google/MusicUpstreamSender:cleanupUpdate	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;Landroid/database/sqlite/SQLiteDatabase;)V
    //   198: aload_0
    //   199: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   202: aload 17
    //   204: iconst_1
    //   205: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   208: return
    //   209: astore 18
    //   211: aload_0
    //   212: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   215: ldc_w 711
    //   218: aload 18
    //   220: invokestatic 441	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   223: istore 19
    //   225: goto -46 -> 179
    //   228: astore 20
    //   230: new 121	java/lang/StringBuilder
    //   233: dup
    //   234: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   237: ldc_w 443
    //   240: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   243: astore 21
    //   245: aload 20
    //   247: invokevirtual 446	com/google/android/music/sync/common/SyncHttpException:getStatusCode	()I
    //   250: istore 22
    //   252: aload 21
    //   254: iload 22
    //   256: invokevirtual 131	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   259: ldc_w 713
    //   262: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   265: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   268: astore 23
    //   270: new 104	com/google/android/music/sync/common/HardSyncException
    //   273: dup
    //   274: aload 23
    //   276: aload 20
    //   278: invokespecial 451	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   281: athrow
    //   282: astore 24
    //   284: new 106	com/google/android/music/sync/common/SoftSyncException
    //   287: dup
    //   288: ldc_w 715
    //   291: aload 24
    //   293: invokespecial 454	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   296: athrow
    //   297: astore 25
    //   299: aload_0
    //   300: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   303: ifeq +17 -> 320
    //   306: aload_0
    //   307: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   310: ldc_w 717
    //   313: aload 25
    //   315: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   318: istore 26
    //   320: aload_0
    //   321: aload_1
    //   322: invokespecial 236	com/google/android/music/sync/google/MusicUpstreamSender:restoreItemFromServer	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;)V
    //   325: goto -146 -> 179
    //   328: astore 25
    //   330: aload_0
    //   331: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   334: ifeq +17 -> 351
    //   337: aload_0
    //   338: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   341: ldc_w 719
    //   344: aload 25
    //   346: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   349: istore 27
    //   351: aload_0
    //   352: aload_1
    //   353: invokespecial 236	com/google/android/music/sync/google/MusicUpstreamSender:restoreItemFromServer	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;)V
    //   356: goto -177 -> 179
    //   359: astore 28
    //   361: aload_0
    //   362: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   365: ifeq +17 -> 382
    //   368: aload_0
    //   369: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   372: ldc_w 721
    //   375: aload 28
    //   377: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   380: istore 29
    //   382: aload_1
    //   383: getfield 574	com/google/android/music/sync/google/model/SyncablePlaylistEntry:mLocalId	J
    //   386: lstore 30
    //   388: aload_0
    //   389: lload 30
    //   391: invokespecial 576	com/google/android/music/sync/google/MusicUpstreamSender:deletePlaylistEntry	(J)V
    //   394: goto -215 -> 179
    //   397: astore 32
    //   399: aload_0
    //   400: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   403: aload 17
    //   405: iload 16
    //   407: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   410: aload 32
    //   412: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   150	179	209	com/google/android/music/store/InvalidDataException
    //   150	179	228	com/google/android/music/sync/common/SyncHttpException
    //   150	179	282	java/io/IOException
    //   150	179	297	com/google/android/music/sync/api/BadRequestException
    //   150	179	328	com/google/android/music/sync/api/ForbiddenException
    //   150	179	359	com/google/android/music/sync/api/ResourceNotFoundException
    //   191	198	397	finally
  }

  // ERROR //
  private void handleUpdatePlaylist(SyncablePlaylist paramSyncablePlaylist)
    throws ServiceUnavailableException, AuthenticatorException, HardSyncException, SoftSyncException, ConflictException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   4: ifeq +14 -> 18
    //   7: aload_0
    //   8: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   11: ldc_w 723
    //   14: invokestatic 223	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   17: istore_2
    //   18: aload_0
    //   19: getfield 60	com/google/android/music/sync/google/MusicUpstreamSender:mClient	Lcom/google/android/music/sync/api/MusicApiClient;
    //   22: astore_3
    //   23: aload_0
    //   24: getfield 86	com/google/android/music/sync/google/MusicUpstreamSender:mAccount	Landroid/accounts/Account;
    //   27: astore 4
    //   29: getstatic 709	com/google/android/music/sync/api/MusicApiClient$OpType:UPDATE	Lcom/google/android/music/sync/api/MusicApiClient$OpType;
    //   32: astore 5
    //   34: aload_3
    //   35: aload 4
    //   37: aload_1
    //   38: aload 5
    //   40: invokeinterface 436 4 0
    //   45: iconst_0
    //   46: istore 6
    //   48: aload_0
    //   49: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   52: invokevirtual 114	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   55: astore 7
    //   57: aload_0
    //   58: aload_1
    //   59: aload 7
    //   61: invokespecial 205	com/google/android/music/sync/google/MusicUpstreamSender:cleanupUpdate	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;Landroid/database/sqlite/SQLiteDatabase;)V
    //   64: aload_0
    //   65: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   68: aload 7
    //   70: iconst_1
    //   71: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   74: return
    //   75: astore 8
    //   77: aload_0
    //   78: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   81: ldc_w 725
    //   84: aload 8
    //   86: invokestatic 441	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   89: istore 9
    //   91: goto -46 -> 45
    //   94: astore 10
    //   96: new 121	java/lang/StringBuilder
    //   99: dup
    //   100: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   103: ldc_w 443
    //   106: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: astore 11
    //   111: aload 10
    //   113: invokevirtual 446	com/google/android/music/sync/common/SyncHttpException:getStatusCode	()I
    //   116: istore 12
    //   118: aload 11
    //   120: iload 12
    //   122: invokevirtual 131	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   125: ldc_w 727
    //   128: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   134: astore 13
    //   136: new 104	com/google/android/music/sync/common/HardSyncException
    //   139: dup
    //   140: aload 13
    //   142: aload 10
    //   144: invokespecial 451	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   147: athrow
    //   148: astore 14
    //   150: new 106	com/google/android/music/sync/common/SoftSyncException
    //   153: dup
    //   154: ldc_w 729
    //   157: aload 14
    //   159: invokespecial 454	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   162: athrow
    //   163: astore 15
    //   165: aload_0
    //   166: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   169: ifeq +17 -> 186
    //   172: aload_0
    //   173: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   176: ldc_w 717
    //   179: aload 15
    //   181: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   184: istore 16
    //   186: aload_0
    //   187: aload_1
    //   188: invokespecial 236	com/google/android/music/sync/google/MusicUpstreamSender:restoreItemFromServer	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;)V
    //   191: goto -146 -> 45
    //   194: astore 15
    //   196: aload_0
    //   197: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   200: ifeq +17 -> 217
    //   203: aload_0
    //   204: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   207: ldc_w 719
    //   210: aload 15
    //   212: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   215: istore 17
    //   217: aload_0
    //   218: aload_1
    //   219: invokespecial 236	com/google/android/music/sync/google/MusicUpstreamSender:restoreItemFromServer	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;)V
    //   222: goto -177 -> 45
    //   225: astore 18
    //   227: aload_0
    //   228: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   231: ifeq +17 -> 248
    //   234: aload_0
    //   235: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   238: ldc_w 721
    //   241: aload 18
    //   243: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   246: istore 19
    //   248: aload_1
    //   249: getfield 593	com/google/android/music/sync/google/model/SyncablePlaylist:mLocalId	J
    //   252: lstore 20
    //   254: aload_0
    //   255: lload 20
    //   257: invokespecial 595	com/google/android/music/sync/google/MusicUpstreamSender:deletePlaylist	(J)V
    //   260: goto -215 -> 45
    //   263: astore 22
    //   265: aload_0
    //   266: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   269: aload 7
    //   271: iload 6
    //   273: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   276: aload 22
    //   278: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   18	45	75	com/google/android/music/store/InvalidDataException
    //   18	45	94	com/google/android/music/sync/common/SyncHttpException
    //   18	45	148	java/io/IOException
    //   18	45	163	com/google/android/music/sync/api/BadRequestException
    //   18	45	194	com/google/android/music/sync/api/ForbiddenException
    //   18	45	225	com/google/android/music/sync/api/ResourceNotFoundException
    //   57	64	263	finally
  }

  // ERROR //
  private void handleUpdateRadioStation(SyncableRadioStation paramSyncableRadioStation)
    throws ServiceUnavailableException, AuthenticatorException, HardSyncException, SoftSyncException, ConflictException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   4: ifeq +14 -> 18
    //   7: aload_0
    //   8: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   11: ldc_w 731
    //   14: invokestatic 223	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   17: istore_2
    //   18: new 485	java/util/ArrayList
    //   21: dup
    //   22: invokespecial 486	java/util/ArrayList:<init>	()V
    //   25: astore_3
    //   26: aload_3
    //   27: aload_1
    //   28: invokeinterface 490 2 0
    //   33: istore 4
    //   35: aload_0
    //   36: getfield 60	com/google/android/music/sync/google/MusicUpstreamSender:mClient	Lcom/google/android/music/sync/api/MusicApiClient;
    //   39: astore 5
    //   41: aload_0
    //   42: getfield 86	com/google/android/music/sync/google/MusicUpstreamSender:mAccount	Landroid/accounts/Account;
    //   45: astore 6
    //   47: aload 5
    //   49: aload 6
    //   51: aload_3
    //   52: invokeinterface 494 3 0
    //   57: invokeinterface 498 1 0
    //   62: astore 7
    //   64: aload 7
    //   66: invokeinterface 503 1 0
    //   71: ifeq +35 -> 106
    //   74: aload 7
    //   76: invokeinterface 507 1 0
    //   81: checkcast 153	com/google/android/music/sync/google/model/BatchMutateResponse$MutateResponse
    //   84: invokevirtual 510	com/google/android/music/sync/google/model/BatchMutateResponse$MutateResponse:throwExceptionFromResponseCode	()V
    //   87: goto -23 -> 64
    //   90: astore 8
    //   92: aload_0
    //   93: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   96: ldc_w 733
    //   99: aload 8
    //   101: invokestatic 441	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   104: istore 9
    //   106: iconst_0
    //   107: istore 10
    //   109: aload_0
    //   110: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   113: invokevirtual 114	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   116: astore 11
    //   118: aload_0
    //   119: aload_1
    //   120: aload 11
    //   122: invokespecial 205	com/google/android/music/sync/google/MusicUpstreamSender:cleanupUpdate	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;Landroid/database/sqlite/SQLiteDatabase;)V
    //   125: aload_0
    //   126: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   129: aload 11
    //   131: iconst_1
    //   132: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   135: return
    //   136: astore 12
    //   138: new 121	java/lang/StringBuilder
    //   141: dup
    //   142: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   145: ldc_w 443
    //   148: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: astore 13
    //   153: aload 12
    //   155: invokevirtual 446	com/google/android/music/sync/common/SyncHttpException:getStatusCode	()I
    //   158: istore 14
    //   160: aload 13
    //   162: iload 14
    //   164: invokevirtual 131	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   167: ldc_w 735
    //   170: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   173: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   176: astore 15
    //   178: new 104	com/google/android/music/sync/common/HardSyncException
    //   181: dup
    //   182: aload 15
    //   184: aload 12
    //   186: invokespecial 451	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   189: athrow
    //   190: astore 16
    //   192: new 106	com/google/android/music/sync/common/SoftSyncException
    //   195: dup
    //   196: ldc_w 737
    //   199: aload 16
    //   201: invokespecial 454	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   204: athrow
    //   205: astore 17
    //   207: aload_0
    //   208: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   211: ifeq +17 -> 228
    //   214: aload_0
    //   215: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   218: ldc_w 739
    //   221: aload 17
    //   223: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   226: istore 18
    //   228: aload_0
    //   229: aload_1
    //   230: invokespecial 236	com/google/android/music/sync/google/MusicUpstreamSender:restoreItemFromServer	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;)V
    //   233: goto -127 -> 106
    //   236: astore 17
    //   238: aload_0
    //   239: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   242: ifeq +17 -> 259
    //   245: aload_0
    //   246: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   249: ldc_w 741
    //   252: aload 17
    //   254: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   257: istore 19
    //   259: aload_0
    //   260: aload_1
    //   261: invokespecial 236	com/google/android/music/sync/google/MusicUpstreamSender:restoreItemFromServer	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;)V
    //   264: goto -158 -> 106
    //   267: astore 17
    //   269: aload_0
    //   270: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   273: ifeq +53 -> 326
    //   276: aload_0
    //   277: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   280: astore 20
    //   282: new 121	java/lang/StringBuilder
    //   285: dup
    //   286: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   289: ldc_w 743
    //   292: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   295: astore 21
    //   297: aload_1
    //   298: invokevirtual 609	com/google/android/music/sync/google/model/SyncableRadioStation:getLocalId	()J
    //   301: lstore 22
    //   303: aload 21
    //   305: lload 22
    //   307: invokevirtual 618	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   310: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   313: astore 24
    //   315: aload 20
    //   317: aload 24
    //   319: aload 17
    //   321: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   324: istore 25
    //   326: aload_1
    //   327: invokevirtual 609	com/google/android/music/sync/google/model/SyncableRadioStation:getLocalId	()J
    //   330: lstore 26
    //   332: aload_0
    //   333: lload 26
    //   335: invokespecial 611	com/google/android/music/sync/google/MusicUpstreamSender:deleteRadioStation	(J)V
    //   338: goto -232 -> 106
    //   341: astore 28
    //   343: aload_0
    //   344: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   347: aload 11
    //   349: iload 10
    //   351: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   354: aload 28
    //   356: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   18	87	90	com/google/android/music/store/InvalidDataException
    //   18	87	136	com/google/android/music/sync/common/SyncHttpException
    //   18	87	190	java/io/IOException
    //   18	87	205	com/google/android/music/sync/api/BadRequestException
    //   18	87	236	com/google/android/music/sync/api/ForbiddenException
    //   18	87	267	com/google/android/music/sync/api/ResourceNotFoundException
    //   118	125	341	finally
  }

  // ERROR //
  private void handleUpdateTrack(Track paramTrack)
    throws ServiceUnavailableException, AuthenticatorException, HardSyncException, SoftSyncException, ConflictException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   4: ifeq +14 -> 18
    //   7: aload_0
    //   8: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   11: ldc_w 745
    //   14: invokestatic 223	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   17: istore_2
    //   18: aload_0
    //   19: getfield 60	com/google/android/music/sync/google/MusicUpstreamSender:mClient	Lcom/google/android/music/sync/api/MusicApiClient;
    //   22: astore_3
    //   23: aload_0
    //   24: getfield 86	com/google/android/music/sync/google/MusicUpstreamSender:mAccount	Landroid/accounts/Account;
    //   27: astore 4
    //   29: getstatic 709	com/google/android/music/sync/api/MusicApiClient$OpType:UPDATE	Lcom/google/android/music/sync/api/MusicApiClient$OpType;
    //   32: astore 5
    //   34: aload_3
    //   35: aload 4
    //   37: aload_1
    //   38: aload 5
    //   40: invokeinterface 436 4 0
    //   45: iconst_0
    //   46: istore 6
    //   48: aload_0
    //   49: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   52: invokevirtual 114	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   55: astore 7
    //   57: aload_0
    //   58: aload_1
    //   59: aload 7
    //   61: invokespecial 205	com/google/android/music/sync/google/MusicUpstreamSender:cleanupUpdate	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;Landroid/database/sqlite/SQLiteDatabase;)V
    //   64: aload_0
    //   65: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   68: aload 7
    //   70: iconst_1
    //   71: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   74: return
    //   75: astore 8
    //   77: aload_0
    //   78: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   81: ldc_w 747
    //   84: aload 8
    //   86: invokestatic 441	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   89: istore 9
    //   91: goto -46 -> 45
    //   94: astore 10
    //   96: new 121	java/lang/StringBuilder
    //   99: dup
    //   100: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   103: ldc_w 443
    //   106: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   109: astore 11
    //   111: aload 10
    //   113: invokevirtual 446	com/google/android/music/sync/common/SyncHttpException:getStatusCode	()I
    //   116: istore 12
    //   118: aload 11
    //   120: iload 12
    //   122: invokevirtual 131	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   125: ldc_w 749
    //   128: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   134: astore 13
    //   136: new 104	com/google/android/music/sync/common/HardSyncException
    //   139: dup
    //   140: aload 13
    //   142: aload 10
    //   144: invokespecial 451	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   147: athrow
    //   148: astore 14
    //   150: new 106	com/google/android/music/sync/common/SoftSyncException
    //   153: dup
    //   154: ldc_w 751
    //   157: aload 14
    //   159: invokespecial 454	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   162: athrow
    //   163: astore 15
    //   165: aload_0
    //   166: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   169: ifeq +17 -> 186
    //   172: aload_0
    //   173: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   176: ldc_w 739
    //   179: aload 15
    //   181: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   184: istore 16
    //   186: aload_1
    //   187: invokevirtual 754	com/google/android/music/sync/google/model/Track:hasLockerId	()Z
    //   190: ifeq +11 -> 201
    //   193: aload_0
    //   194: aload_1
    //   195: invokespecial 236	com/google/android/music/sync/google/MusicUpstreamSender:restoreItemFromServer	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;)V
    //   198: goto -153 -> 45
    //   201: aload_0
    //   202: getfield 50	com/google/android/music/sync/google/MusicUpstreamSender:mContext	Landroid/content/Context;
    //   205: astore 17
    //   207: aload_1
    //   208: invokevirtual 631	com/google/android/music/sync/google/model/Track:getLocalId	()J
    //   211: lstore 18
    //   213: aload 17
    //   215: lload 18
    //   217: invokestatic 344	com/google/android/music/store/MusicFile:deleteByLocalId	(Landroid/content/Context;J)V
    //   220: goto -175 -> 45
    //   223: astore 15
    //   225: aload_0
    //   226: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   229: ifeq +17 -> 246
    //   232: aload_0
    //   233: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   236: ldc_w 741
    //   239: aload 15
    //   241: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   244: istore 20
    //   246: aload_1
    //   247: invokevirtual 754	com/google/android/music/sync/google/model/Track:hasLockerId	()Z
    //   250: ifeq +11 -> 261
    //   253: aload_0
    //   254: aload_1
    //   255: invokespecial 236	com/google/android/music/sync/google/MusicUpstreamSender:restoreItemFromServer	(Lcom/google/android/music/sync/google/model/MusicQueueableSyncEntity;)V
    //   258: goto -213 -> 45
    //   261: aload_0
    //   262: getfield 50	com/google/android/music/sync/google/MusicUpstreamSender:mContext	Landroid/content/Context;
    //   265: astore 21
    //   267: aload_1
    //   268: invokevirtual 631	com/google/android/music/sync/google/model/Track:getLocalId	()J
    //   271: lstore 22
    //   273: aload 21
    //   275: lload 22
    //   277: invokestatic 344	com/google/android/music/store/MusicFile:deleteByLocalId	(Landroid/content/Context;J)V
    //   280: goto -235 -> 45
    //   283: astore 15
    //   285: aload_0
    //   286: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   289: ifeq +53 -> 342
    //   292: aload_0
    //   293: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   296: astore 24
    //   298: new 121	java/lang/StringBuilder
    //   301: dup
    //   302: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   305: ldc_w 756
    //   308: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   311: astore 25
    //   313: aload_1
    //   314: invokevirtual 631	com/google/android/music/sync/google/model/Track:getLocalId	()J
    //   317: lstore 26
    //   319: aload 25
    //   321: lload 26
    //   323: invokevirtual 618	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   326: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   329: astore 28
    //   331: aload 24
    //   333: aload 28
    //   335: aload 15
    //   337: invokestatic 571	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   340: istore 29
    //   342: aload_0
    //   343: getfield 50	com/google/android/music/sync/google/MusicUpstreamSender:mContext	Landroid/content/Context;
    //   346: astore 30
    //   348: aload_1
    //   349: invokevirtual 631	com/google/android/music/sync/google/model/Track:getLocalId	()J
    //   352: lstore 31
    //   354: aload 30
    //   356: lload 31
    //   358: invokestatic 344	com/google/android/music/store/MusicFile:deleteByLocalId	(Landroid/content/Context;J)V
    //   361: goto -316 -> 45
    //   364: astore 33
    //   366: aload_0
    //   367: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   370: aload 7
    //   372: iload 6
    //   374: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   377: aload 33
    //   379: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   18	45	75	com/google/android/music/store/InvalidDataException
    //   18	45	94	com/google/android/music/sync/common/SyncHttpException
    //   18	45	148	java/io/IOException
    //   18	45	163	com/google/android/music/sync/api/BadRequestException
    //   18	45	223	com/google/android/music/sync/api/ForbiddenException
    //   18	45	283	com/google/android/music/sync/api/ResourceNotFoundException
    //   57	64	364	finally
  }

  private BatchMutateResponse.MutateResponse.ResponseCode mapStringResponseCodeToEnum(String paramString)
    throws HardSyncException
  {
    BatchMutateResponse.MutateResponse.ResponseCode[] arrayOfResponseCode = BatchMutateResponse.MutateResponse.ResponseCode.values();
    int i = arrayOfResponseCode.length;
    int j = 0;
    while (j < i)
    {
      BatchMutateResponse.MutateResponse.ResponseCode localResponseCode = arrayOfResponseCode[j];
      if (localResponseCode.name().equals(paramString))
        return localResponseCode;
      j += 1;
    }
    String str = "Unable to map a batch-mutate response code string " + paramString + " to a known enum value!";
    throw new HardSyncException(str);
  }

  // ERROR //
  private void reportTrackStats(List<? extends QueueableSyncEntity> paramList)
    throws AuthenticatorException, HardSyncException, SoftSyncException, ServiceUnavailableException
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnull +12 -> 13
    //   4: aload_1
    //   5: invokeinterface 119 1 0
    //   10: ifne +15 -> 25
    //   13: aload_0
    //   14: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   17: ldc_w 775
    //   20: invokestatic 243	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   23: istore_2
    //   24: return
    //   25: aload_0
    //   26: getfield 98	com/google/android/music/sync/google/MusicUpstreamSender:mEnableTrackStatsUpSync	Z
    //   29: ifne +15 -> 44
    //   32: aload_0
    //   33: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   36: ldc_w 777
    //   39: invokestatic 243	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   42: istore_3
    //   43: return
    //   44: iconst_1
    //   45: istore 4
    //   47: aload_0
    //   48: getfield 60	com/google/android/music/sync/google/MusicUpstreamSender:mClient	Lcom/google/android/music/sync/api/MusicApiClient;
    //   51: astore 5
    //   53: aload_0
    //   54: getfield 86	com/google/android/music/sync/google/MusicUpstreamSender:mAccount	Landroid/accounts/Account;
    //   57: astore 6
    //   59: aload 5
    //   61: aload 6
    //   63: aload_1
    //   64: invokeinterface 780 3 0
    //   69: aload_0
    //   70: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   73: ifeq +59 -> 132
    //   76: aload_0
    //   77: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   80: astore 7
    //   82: new 121	java/lang/StringBuilder
    //   85: dup
    //   86: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   89: ldc_w 782
    //   92: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   95: astore 8
    //   97: aload_1
    //   98: invokeinterface 119 1 0
    //   103: istore 9
    //   105: aload 8
    //   107: iload 9
    //   109: invokevirtual 131	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   112: ldc_w 784
    //   115: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   121: astore 10
    //   123: aload 7
    //   125: aload 10
    //   127: invokestatic 223	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   130: istore 11
    //   132: iload 4
    //   134: ifne +4 -> 138
    //   137: return
    //   138: aload_0
    //   139: aload_1
    //   140: invokespecial 787	com/google/android/music/sync/google/MusicUpstreamSender:resetTrackStats	(Ljava/util/List;)V
    //   143: return
    //   144: astore 12
    //   146: iconst_0
    //   147: istore 4
    //   149: aload 12
    //   151: athrow
    //   152: astore 13
    //   154: iload 4
    //   156: ifeq +8 -> 164
    //   159: aload_0
    //   160: aload_1
    //   161: invokespecial 787	com/google/android/music/sync/google/MusicUpstreamSender:resetTrackStats	(Ljava/util/List;)V
    //   164: aload 13
    //   166: athrow
    //   167: astore 12
    //   169: iconst_0
    //   170: istore 4
    //   172: new 106	com/google/android/music/sync/common/SoftSyncException
    //   175: dup
    //   176: ldc_w 789
    //   179: aload 12
    //   181: invokespecial 454	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   184: athrow
    //   185: astore 14
    //   187: aload_0
    //   188: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   191: ldc_w 791
    //   194: aload 14
    //   196: invokestatic 441	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   199: istore 15
    //   201: iload 4
    //   203: ifne +4 -> 207
    //   206: return
    //   207: aload_0
    //   208: aload_1
    //   209: invokespecial 787	com/google/android/music/sync/google/MusicUpstreamSender:resetTrackStats	(Ljava/util/List;)V
    //   212: return
    //   213: astore 12
    //   215: aload_0
    //   216: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   219: ldc_w 793
    //   222: aload 12
    //   224: invokestatic 441	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   227: istore 16
    //   229: iload 4
    //   231: ifne +4 -> 235
    //   234: return
    //   235: aload_0
    //   236: aload_1
    //   237: invokespecial 787	com/google/android/music/sync/google/MusicUpstreamSender:resetTrackStats	(Ljava/util/List;)V
    //   240: return
    //   241: astore 12
    //   243: aload_0
    //   244: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   247: ldc_w 795
    //   250: aload 12
    //   252: invokestatic 441	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   255: istore 17
    //   257: iload 4
    //   259: ifne +4 -> 263
    //   262: return
    //   263: aload_0
    //   264: aload_1
    //   265: invokespecial 787	com/google/android/music/sync/google/MusicUpstreamSender:resetTrackStats	(Ljava/util/List;)V
    //   268: return
    //   269: astore 12
    //   271: aload_0
    //   272: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   275: ldc_w 797
    //   278: aload 12
    //   280: invokestatic 441	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   283: istore 18
    //   285: iload 4
    //   287: ifne +4 -> 291
    //   290: return
    //   291: aload_0
    //   292: aload_1
    //   293: invokespecial 787	com/google/android/music/sync/google/MusicUpstreamSender:resetTrackStats	(Ljava/util/List;)V
    //   296: return
    //   297: astore 12
    //   299: aload_0
    //   300: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   303: ldc_w 799
    //   306: aload 12
    //   308: invokestatic 441	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   311: istore 19
    //   313: iload 4
    //   315: ifne +4 -> 319
    //   318: return
    //   319: aload_0
    //   320: aload_1
    //   321: invokespecial 787	com/google/android/music/sync/google/MusicUpstreamSender:resetTrackStats	(Ljava/util/List;)V
    //   324: return
    //   325: astore 12
    //   327: new 121	java/lang/StringBuilder
    //   330: dup
    //   331: invokespecial 122	java/lang/StringBuilder:<init>	()V
    //   334: ldc_w 443
    //   337: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   340: astore 20
    //   342: aload 12
    //   344: invokevirtual 446	com/google/android/music/sync/common/SyncHttpException:getStatusCode	()I
    //   347: istore 21
    //   349: aload 20
    //   351: iload 21
    //   353: invokevirtual 131	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   356: ldc_w 801
    //   359: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   362: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   365: astore 22
    //   367: new 104	com/google/android/music/sync/common/HardSyncException
    //   370: dup
    //   371: aload 22
    //   373: aload 12
    //   375: invokespecial 451	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   378: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   47	132	144	com/google/android/music/sync/api/ServiceUnavailableException
    //   47	132	152	finally
    //   149	152	152	finally
    //   172	201	152	finally
    //   215	229	152	finally
    //   243	257	152	finally
    //   271	285	152	finally
    //   299	313	152	finally
    //   327	379	152	finally
    //   47	132	167	java/io/IOException
    //   47	132	185	com/google/android/music/sync/api/BadRequestException
    //   47	132	213	com/google/android/music/sync/api/ForbiddenException
    //   47	132	241	com/google/android/music/sync/api/ResourceNotFoundException
    //   47	132	269	com/google/android/music/sync/api/NotModifiedException
    //   47	132	297	com/google/android/music/sync/api/ConflictException
    //   47	132	325	com/google/android/music/sync/common/SyncHttpException
  }

  private void resetTrackStats(List<? extends QueueableSyncEntity> paramList)
  {
    ArrayList localArrayList = Lists.newArrayListWithCapacity(paramList.size());
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      QueueableSyncEntity localQueueableSyncEntity = (QueueableSyncEntity)localIterator.next();
      Long localLong = Long.valueOf(((TrackStat)TrackStat.class.cast(localQueueableSyncEntity)).getLocalId());
      boolean bool1 = localArrayList.add(localLong);
    }
    boolean bool2 = false;
    try
    {
      localSQLiteDatabase = this.mStore.beginWriteTxn();
      MusicFile.resetPlayCount(localSQLiteDatabase, localArrayList);
      boolean bool3 = true;
      if (localSQLiteDatabase == null)
        return;
      this.mStore.endWriteTxn(localSQLiteDatabase, bool3);
      return;
    }
    finally
    {
      SQLiteDatabase localSQLiteDatabase;
      if (localSQLiteDatabase != null)
        this.mStore.endWriteTxn(localSQLiteDatabase, bool2);
    }
  }

  // ERROR //
  private void restoreItemFromServer(MusicQueueableSyncEntity paramMusicQueueableSyncEntity)
    throws ServiceUnavailableException, AuthenticatorException, HardSyncException, SoftSyncException
  {
    // Byte code:
    //   0: aload_1
    //   1: instanceof 252
    //   4: ifeq +187 -> 191
    //   7: aload_0
    //   8: getfield 60	com/google/android/music/sync/google/MusicUpstreamSender:mClient	Lcom/google/android/music/sync/api/MusicApiClient;
    //   11: astore_2
    //   12: aload_0
    //   13: getfield 86	com/google/android/music/sync/google/MusicUpstreamSender:mAccount	Landroid/accounts/Account;
    //   16: astore_3
    //   17: aload_1
    //   18: invokeinterface 226 1 0
    //   23: astore 4
    //   25: aload_2
    //   26: aload_3
    //   27: aload 4
    //   29: invokeinterface 827 3 0
    //   34: astore 5
    //   36: aload 5
    //   38: astore 6
    //   40: iconst_0
    //   41: istore 7
    //   43: iload 7
    //   45: ifeq +400 -> 445
    //   48: aload_1
    //   49: invokevirtual 833	java/lang/Object:getClass	()Ljava/lang/Class;
    //   52: invokevirtual 836	java/lang/Class:newInstance	()Ljava/lang/Object;
    //   55: checkcast 151	com/google/android/music/sync/google/model/MusicQueueableSyncEntity
    //   58: astore 6
    //   60: aload 6
    //   62: iconst_1
    //   63: invokeinterface 840 2 0
    //   68: aload_1
    //   69: invokeinterface 226 1 0
    //   74: astore 8
    //   76: aload 6
    //   78: aload 8
    //   80: invokeinterface 196 2 0
    //   85: aload 6
    //   87: astore 9
    //   89: aload_0
    //   90: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   93: invokevirtual 114	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   96: astore 10
    //   98: aload_0
    //   99: getfield 50	com/google/android/music/sync/google/MusicUpstreamSender:mContext	Landroid/content/Context;
    //   102: astore 11
    //   104: aload_0
    //   105: getfield 69	com/google/android/music/sync/google/MusicUpstreamSender:mProtocolState	Ljava/util/Map;
    //   108: astore 12
    //   110: aload_0
    //   111: getfield 48	com/google/android/music/sync/google/MusicUpstreamSender:mUseVerboseLogging	Z
    //   114: istore 13
    //   116: aload_0
    //   117: getfield 34	com/google/android/music/sync/google/MusicUpstreamSender:mTag	Ljava/lang/String;
    //   120: astore 14
    //   122: aload_0
    //   123: getfield 96	com/google/android/music/sync/google/MusicUpstreamSender:mAccountHash	I
    //   126: istore 15
    //   128: new 842	com/google/android/music/sync/google/MusicDownstreamMerger$MusicBlockMerger
    //   131: dup
    //   132: aload 11
    //   134: aload 10
    //   136: aload 12
    //   138: iload 13
    //   140: aload 14
    //   142: iload 15
    //   144: invokespecial 845	com/google/android/music/sync/google/MusicDownstreamMerger$MusicBlockMerger:<init>	(Landroid/content/Context;Landroid/database/sqlite/SQLiteDatabase;Ljava/util/Map;ZLjava/lang/String;I)V
    //   147: astore 16
    //   149: aload 16
    //   151: aload 9
    //   153: aload_1
    //   154: invokevirtual 849	com/google/android/music/sync/google/MusicDownstreamMerger$MusicBlockMerger:processMergeItem	(Lcom/google/android/music/sync/common/QueueableSyncEntity;Lcom/google/android/music/sync/common/QueueableSyncEntity;)Z
    //   157: istore 17
    //   159: aload 16
    //   161: ifnull +8 -> 169
    //   164: aload 16
    //   166: invokevirtual 852	com/google/android/music/sync/google/MusicDownstreamMerger$MusicBlockMerger:safelyCloseStatements	()V
    //   169: aload_0
    //   170: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   173: aload 10
    //   175: iconst_1
    //   176: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   179: aload 16
    //   181: ifnonnull +4 -> 185
    //   184: return
    //   185: aload 16
    //   187: invokevirtual 855	com/google/android/music/sync/google/MusicDownstreamMerger$MusicBlockMerger:cleanupLocallyCachedFiles	()V
    //   190: return
    //   191: aload_1
    //   192: instanceof 277
    //   195: ifeq +39 -> 234
    //   198: aload_0
    //   199: getfield 60	com/google/android/music/sync/google/MusicUpstreamSender:mClient	Lcom/google/android/music/sync/api/MusicApiClient;
    //   202: astore 18
    //   204: aload_0
    //   205: getfield 86	com/google/android/music/sync/google/MusicUpstreamSender:mAccount	Landroid/accounts/Account;
    //   208: astore 19
    //   210: aload_1
    //   211: invokeinterface 226 1 0
    //   216: astore 20
    //   218: aload 18
    //   220: aload 19
    //   222: aload 20
    //   224: invokeinterface 859 3 0
    //   229: astore 6
    //   231: goto -191 -> 40
    //   234: aload_1
    //   235: instanceof 319
    //   238: ifne +4 -> 242
    //   241: return
    //   242: aload_0
    //   243: getfield 60	com/google/android/music/sync/google/MusicUpstreamSender:mClient	Lcom/google/android/music/sync/api/MusicApiClient;
    //   246: astore 21
    //   248: aload_0
    //   249: getfield 86	com/google/android/music/sync/google/MusicUpstreamSender:mAccount	Landroid/accounts/Account;
    //   252: astore 22
    //   254: aload_1
    //   255: invokeinterface 226 1 0
    //   260: astore 23
    //   262: aload 21
    //   264: aload 22
    //   266: aload 23
    //   268: invokeinterface 863 3 0
    //   273: astore 5
    //   275: aload 5
    //   277: astore 6
    //   279: goto -239 -> 40
    //   282: astore 24
    //   284: new 110	android/accounts/AuthenticatorException
    //   287: dup
    //   288: ldc_w 865
    //   291: aload 24
    //   293: invokespecial 866	android/accounts/AuthenticatorException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   296: athrow
    //   297: astore 25
    //   299: new 104	com/google/android/music/sync/common/HardSyncException
    //   302: dup
    //   303: ldc_w 868
    //   306: aload 25
    //   308: invokespecial 451	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   311: athrow
    //   312: astore 26
    //   314: new 106	com/google/android/music/sync/common/SoftSyncException
    //   317: dup
    //   318: ldc_w 870
    //   321: aload 26
    //   323: invokespecial 454	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   326: athrow
    //   327: astore 27
    //   329: new 104	com/google/android/music/sync/common/HardSyncException
    //   332: dup
    //   333: ldc_w 872
    //   336: aload 27
    //   338: invokespecial 451	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   341: athrow
    //   342: astore 28
    //   344: iconst_1
    //   345: istore 7
    //   347: aconst_null
    //   348: astore 6
    //   350: goto -307 -> 43
    //   353: astore 29
    //   355: iconst_1
    //   356: istore 7
    //   358: aconst_null
    //   359: astore 6
    //   361: goto -318 -> 43
    //   364: astore 30
    //   366: iconst_1
    //   367: istore 7
    //   369: aconst_null
    //   370: astore 6
    //   372: goto -329 -> 43
    //   375: astore 31
    //   377: new 104	com/google/android/music/sync/common/HardSyncException
    //   380: dup
    //   381: aload 31
    //   383: invokespecial 873	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/Throwable;)V
    //   386: athrow
    //   387: astore 32
    //   389: new 104	com/google/android/music/sync/common/HardSyncException
    //   392: dup
    //   393: aload 32
    //   395: invokespecial 873	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/Throwable;)V
    //   398: athrow
    //   399: astore 33
    //   401: aconst_null
    //   402: astore 34
    //   404: aload 34
    //   406: ifnull +8 -> 414
    //   409: aload 34
    //   411: invokevirtual 852	com/google/android/music/sync/google/MusicDownstreamMerger$MusicBlockMerger:safelyCloseStatements	()V
    //   414: aload_0
    //   415: getfield 58	com/google/android/music/sync/google/MusicUpstreamSender:mStore	Lcom/google/android/music/store/Store;
    //   418: aload 10
    //   420: iconst_0
    //   421: invokevirtual 146	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   424: aload 34
    //   426: ifnull +3 -> 429
    //   429: aload 33
    //   431: athrow
    //   432: astore 35
    //   434: aload 16
    //   436: astore 34
    //   438: aload 35
    //   440: astore 33
    //   442: goto -38 -> 404
    //   445: aload 6
    //   447: astore 9
    //   449: goto -360 -> 89
    //
    // Exception table:
    //   from	to	target	type
    //   0	36	282	android/accounts/AuthenticatorException
    //   191	275	282	android/accounts/AuthenticatorException
    //   0	36	297	com/google/android/music/sync/common/SyncHttpException
    //   191	275	297	com/google/android/music/sync/common/SyncHttpException
    //   0	36	312	java/io/IOException
    //   191	275	312	java/io/IOException
    //   0	36	327	com/google/android/music/sync/api/NotModifiedException
    //   191	275	327	com/google/android/music/sync/api/NotModifiedException
    //   0	36	342	com/google/android/music/sync/api/BadRequestException
    //   191	275	342	com/google/android/music/sync/api/BadRequestException
    //   0	36	353	com/google/android/music/sync/api/ForbiddenException
    //   191	275	353	com/google/android/music/sync/api/ForbiddenException
    //   0	36	364	com/google/android/music/sync/api/ResourceNotFoundException
    //   191	275	364	com/google/android/music/sync/api/ResourceNotFoundException
    //   48	60	375	java/lang/InstantiationException
    //   48	60	387	java/lang/IllegalAccessException
    //   98	149	399	finally
    //   149	159	432	finally
  }

  private void validateEntryParentsAndRemoteTrackIds(List<? extends MusicQueueableSyncEntity> paramList)
  {
    if (!(paramList.get(0) instanceof SyncablePlaylistEntry))
      return;
    Object localObject1 = null;
    int i = paramList.size();
    int j = 0;
    if (j < i)
    {
      Object localObject2 = paramList.get(j);
      SyncablePlaylistEntry localSyncablePlaylistEntry = (SyncablePlaylistEntry)SyncablePlaylistEntry.class.cast(localObject2);
      if (localSyncablePlaylistEntry.mRemotePlaylistId != null);
      while (true)
      {
        j += 1;
        break;
        if (!fillInRemoteIdOfParentPlaylist(localSyncablePlaylistEntry))
        {
          if (localObject1 == null)
            LinkedList localLinkedList1 = new LinkedList();
          Integer localInteger1 = Integer.valueOf(j);
          boolean bool1 = localObject1.add(localInteger1);
        }
        if (!fillInRemoteIdForTrack(localSyncablePlaylistEntry))
        {
          if (localObject1 == null)
            LinkedList localLinkedList2 = new LinkedList();
          Integer localInteger2 = Integer.valueOf(j);
          boolean bool2 = localObject1.add(localInteger2);
        }
      }
    }
    if (localObject1 == null)
      return;
    ListIterator localListIterator = localObject1.listIterator();
    while (true)
    {
      if (!localListIterator.hasPrevious())
        return;
      Object localObject3 = localListIterator.previous();
      boolean bool3 = paramList.remove(localObject3);
    }
  }

  protected void processUpstreamEntityBlock(List<QueueableSyncEntity> paramList)
    throws AuthenticatorException, ServiceUnavailableException, HardSyncException, SoftSyncException, ConflictDetectedException
  {
    ListPartitioner localListPartitioner = new ListPartitioner();
    localListPartitioner.addPartitioningClass(SyncablePlaylist.class);
    localListPartitioner.addPartitioningClass(SyncablePlaylistEntry.class);
    localListPartitioner.addPartitioningClass(Track.class);
    localListPartitioner.addPartitioningClass(TrackStat.class);
    localListPartitioner.addPartitioningClass(TrackTombstone.class);
    localListPartitioner.addPartitioningClass(SyncableRadioStation.class);
    List localList2;
    while (true)
    {
      List localList3;
      try
      {
        List localList1 = localListPartitioner.partition(paramList);
        Iterator localIterator = localList1.iterator();
        localList2 = 0;
        if (!localIterator.hasNext())
          break label180;
        localList3 = (List)localIterator.next();
        if (((QueueableSyncEntity)localList3.get(0) instanceof TrackStat))
        {
          reportTrackStats(localList3);
          continue;
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        int j = Log.wtf(this.mTag, "Unable to partition the client changes into syncable entities:", localIllegalArgumentException);
        throw new HardSyncException(localIllegalArgumentException);
      }
      try
      {
        List localList4 = (List)List.class.cast(localList3);
        handleMutations(localList4);
        localList3 = localList2;
        localList2 = localList3;
      }
      catch (ConflictException localConflictException)
      {
        while (true)
          int i = localConflictException.getConflictCount() + localList2;
      }
    }
    label180: if (localList2 == 0)
      return;
    ConflictDetectedException localConflictDetectedException = new ConflictDetectedException();
    localConflictDetectedException.setConflictCount(localList2);
    throw localConflictDetectedException;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.MusicUpstreamSender
 * JD-Core Version:    0.6.2
 */