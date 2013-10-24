package com.google.android.music.store;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteProgram;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Audio.Playlists;
import android.text.TextUtils;
import android.util.Pair;
import com.android.common.content.SyncStateContentProviderHelper;
import com.google.android.gsf.Gservices;
import com.google.android.music.cloudclient.MixTrackId;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.download.DownloadRequest;
import com.google.android.music.download.DownloadState.State;
import com.google.android.music.download.cache.CacheUtils;
import com.google.android.music.download.cache.FileLocation;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.log.Log;
import com.google.android.music.log.LogFile;
import com.google.android.music.medialist.ExternalSongList;
import com.google.android.music.medialist.TracksSongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.sync.common.ProviderException;
import com.google.android.music.sync.google.ClientSyncState;
import com.google.android.music.sync.google.ClientSyncState.Builder;
import com.google.android.music.sync.google.ClientSyncState.Helpers;
import com.google.android.music.sync.google.model.SyncablePlaylistEntry;
import com.google.android.music.sync.google.model.Track;
import com.google.android.music.utils.BobJenkinsLookup3;
import com.google.android.music.utils.DbUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.PostFroyoUtils.EnvironmentCompat;
import com.google.android.music.utils.async.AsyncWorkers;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class Store extends BaseStore
{
  static final boolean LOGV;
  static final String OPERATIONS_PER_TXN_AS_STRING;
  private static HashMap<String, String> sDownloadQueueProjectionMap;
  private static Store sInstance = new Store();
  private AutoCacheManager mAutoCacheManager;
  private Context mContext;
  private DatabaseHelper mDbOpener;
  private final AtomicBoolean mDowngraded;
  private MediaStoreImporter mMediaStoreImporter;
  private MusicRingtoneManager mRingtoneManager;
  private SyncStateContentProviderHelper mSyncHelper;
  private MusicEventLogger mTracker;

  static
  {
    OPERATIONS_PER_TXN_AS_STRING = String.valueOf(512);
    LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.STORE);
    sDownloadQueueProjectionMap = new HashMap();
    MusicContentProvider.addMapping(sDownloadQueueProjectionMap, "_id", "MUSIC.Id");
    MusicContentProvider.addMapping(sDownloadQueueProjectionMap, "Album", "Album");
    MusicContentProvider.addMapping(sDownloadQueueProjectionMap, "Artist", "Artist");
    MusicContentProvider.addMapping(sDownloadQueueProjectionMap, "AlbumArtist", "AlbumArtist");
    MusicContentProvider.addMapping(sDownloadQueueProjectionMap, "Title", "Title");
    HashMap localHashMap = sDownloadQueueProjectionMap;
    String str = Integer.toString(DownloadState.State.NOT_STARTED.ordinal());
    MusicContentProvider.addMapping(localHashMap, "DownloadStatus", str);
    MusicContentProvider.addMapping(sDownloadQueueProjectionMap, "Size", "Size");
  }

  public Store()
  {
    AtomicBoolean localAtomicBoolean = new AtomicBoolean(false);
    this.mDowngraded = localAtomicBoolean;
    SyncStateContentProviderHelper localSyncStateContentProviderHelper = new SyncStateContentProviderHelper();
    this.mSyncHelper = localSyncStateContentProviderHelper;
    MediaStoreImporter localMediaStoreImporter = new MediaStoreImporter();
    this.mMediaStoreImporter = localMediaStoreImporter;
    MusicRingtoneManager localMusicRingtoneManager = new MusicRingtoneManager(this);
    this.mRingtoneManager = localMusicRingtoneManager;
    AutoCacheManager localAutoCacheManager = new AutoCacheManager(this);
    this.mAutoCacheManager = localAutoCacheManager;
  }

  private PlayQueueInsertResult addSongsToPlay(Cursor paramCursor, boolean paramBoolean1, int paramInt, boolean paramBoolean2, boolean paramBoolean3, long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      PlayQueue localPlayQueue = PlayQueue.getInstance();
      if (paramLong != 65535L)
      {
        long l = paramLong;
        int i = localPlayQueue.removeGroup(localSQLiteDatabase, l);
      }
      Cursor localCursor = paramCursor;
      boolean bool1 = paramBoolean2;
      boolean bool2 = paramBoolean1;
      int j = paramInt;
      boolean bool3 = paramBoolean3;
      PlayQueueInsertResult localPlayQueueInsertResult1 = localPlayQueue.queueAndMovePlayPosition(localSQLiteDatabase, localCursor, bool1, bool2, j, bool3, 0);
      PlayQueueInsertResult localPlayQueueInsertResult2 = localPlayQueueInsertResult1;
      endWriteTxn(localSQLiteDatabase, true);
      ContentResolver localContentResolver = this.mContext.getContentResolver();
      localPlayQueue.notifyChange(localContentResolver);
      return localPlayQueueInsertResult2;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public static Pair<Long, String> canonicalizeAndGenerateId(TagNormalizer paramTagNormalizer, String paramString)
  {
    long l = 0L;
    String str1 = null;
    if (paramString != null)
    {
      str1 = paramTagNormalizer.normalize(paramString);
      if (TextUtils.isEmpty(str1))
        String str2 = paramString;
      l = generateId(str1);
    }
    Long localLong = Long.valueOf(l);
    return new Pair(localLong, str1);
  }

  private static int clearInvalidPlaylistRefsFromSuggestedSeeds(SQLiteDatabase paramSQLiteDatabase)
  {
    ContentValues localContentValues = new ContentValues(1);
    localContentValues.putNull("SeedListId");
    int i = paramSQLiteDatabase.update("SUGGESTED_SEEDS", localContentValues, "SeedListId NOT IN (SELECT Id FROM LISTS WHERE ListType=50)", null);
    if (i > 0)
    {
      String str = "Cleared " + i + " invalid playlist references in suggested seeds";
      Log.w("MusicStore", str);
    }
    return i;
  }

  public static int computeAccountHash(Account paramAccount)
  {
    String str1 = paramAccount.name;
    String str2 = paramAccount.type;
    return computeAccountHash(str1, str2);
  }

  public static int computeAccountHash(String paramString1, String paramString2)
  {
    int i = paramString1 + paramString2;
    BobJenkinsLookup3 localBobJenkinsLookup3 = new BobJenkinsLookup3();
    try
    {
      byte[] arrayOfByte = i.getBytes("UTF-8");
      int j = (int)localBobJenkinsLookup3.hashbig(arrayOfByte);
      if (j == 0)
      {
        Log.w("MusicStore", "Hash collision with media store value");
        j = 1;
      }
      return j;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      Log.wtf("MusicStore", "UTF-8 is not supported?");
      throw new RuntimeException("Failed to convert to UTF-8", localUnsupportedEncodingException);
    }
  }

  static void configureDatabaseConnection(SQLiteDatabase paramSQLiteDatabase)
  {
    try
    {
      Class[] arrayOfClass = (Class[])null;
      Method localMethod1 = SQLiteDatabase.class.getMethod("disableWriteAheadLogging", arrayOfClass);
      Method localMethod2 = localMethod1;
      int i = 0;
      try
      {
        Object[] arrayOfObject = new Object[i];
        Object localObject = localMethod2.invoke(paramSQLiteDatabase, arrayOfObject);
        if (!LOGV)
          return;
        Log.d("MusicStore", "Disabled WAL");
        return;
      }
      catch (Exception localException)
      {
        Log.e("MusicStore", "Fail to invoke disableWriteAheadLogging");
        throw new RuntimeException("Fail to disable write ahead logging", localException);
      }
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
    }
  }

  static int countLocalMusic(SQLiteDatabase paramSQLiteDatabase)
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "count(1)";
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    Cursor localCursor = localSQLiteDatabase.query("MUSIC", arrayOfString, "MUSIC.SourceAccount=0", null, str1, str2, str3);
    if (localCursor != null);
    try
    {
      if (localCursor.moveToFirst())
      {
        int i = localCursor.getInt(0);
        int j = i;
        return j;
      }
      throw new RuntimeException("Failed to count local media");
    }
    finally
    {
      safeClose(localCursor);
    }
  }

  private static boolean deleteFromMediaStore(Context paramContext, long paramLong)
  {
    int i = 0;
    Uri localUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, paramLong);
    String[] arrayOfString1 = new String[1];
    arrayOfString1[i] = "_data";
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor != null);
    while (true)
    {
      try
      {
        Object localObject3;
        String str4;
        if (localCursor.moveToFirst())
        {
          String str2 = localCursor.getString(0);
          File localFile = new File(str2);
          boolean bool = localFile.delete();
          if (bool)
          {
            Object localObject2 = null;
            safeClose(localCursor);
            Object localObject1 = localObject2;
            if (localObject1 != null)
            {
              int j = paramContext.getContentResolver().delete(localUri, null, null);
              if (j == 0)
              {
                String str3 = "Audio file with id " + paramLong + " is not found in the media store";
                Log.w("MusicStore", str3);
              }
            }
            else
            {
              return localObject1;
            }
          }
          else
          {
            if (!localFile.exists())
            {
              localObject3 = null;
              continue;
            }
            localObject3 = null;
            continue;
          }
        }
        else
        {
          str4 = "Failed to locate audio file in media store. Id=" + paramLong;
          Log.w("MusicStore", str4);
          localObject3 = null;
          continue;
        }
      }
      catch (SecurityException localSecurityException)
      {
        Log.w("MusicStore", "Failed to delete music file due to security exception", localSecurityException);
        safeClose(localCursor);
        continue;
      }
      finally
      {
        safeClose(localCursor);
      }
      if (localSecurityException != 1)
      {
        String str5 = "Unexpected return value of " + localSecurityException + " when deleting media store audio file + " + paramLong;
        Log.w("MusicStore", str5);
      }
    }
  }

  private static void deleteLocalMusic(SQLiteDatabase paramSQLiteDatabase)
  {
    int i = paramSQLiteDatabase.delete("MUSIC", "MUSIC.SourceAccount=0", null);
  }

  private static void deleteLocalMusicAndPlaylists(SQLiteDatabase paramSQLiteDatabase)
  {
    deleteLocalMusic(paramSQLiteDatabase);
    int i = PlayList.deleteMediaStorePlaylists(paramSQLiteDatabase);
  }

  private int deleteLockerTracks(String paramString, String[] paramArrayOfString)
  {
    List localList = getLockerTracksAccountAndSourceId(paramString, paramArrayOfString);
    return deleteLockerTracks(localList);
  }

  private int deleteLockerTracks(List<Pair<Integer, String>> paramList)
  {
    int i = 0;
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    final HashSet localHashSet = Sets.newHashSet();
    try
    {
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        Pair localPair = (Pair)localIterator.next();
        int j = ((Integer)localPair.first).intValue();
        String str1 = (String)localPair.second;
        String str2 = MusicFile.deleteAndGetLocalCacheFilepath(this.mContext, localSQLiteDatabase, j, str1);
        long l1 = i;
        long l2 = j;
        long l3 = MusicFileTombstone.insertMusicTombstone(localSQLiteDatabase, str1, l2);
        i = (int)(l1 + l3);
        if (str2 != null)
          boolean bool = localHashSet.add(str2);
      }
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
    if (1 != 0)
    {
      LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          Iterator localIterator = localHashSet.iterator();
          while (true)
          {
            if (!localIterator.hasNext())
              return;
            String str1 = (String)localIterator.next();
            try
            {
              if (!new File(str1).delete())
              {
                String str2 = "Could not delete cache file <" + str1 + ">";
                Log.w("MusicStore", str2);
              }
            }
            catch (Exception localException)
            {
              String str3 = "Exception while deleting cache file <" + str1 + ">";
              Log.e("MusicStore", str3, localException);
            }
          }
        }
      };
      AsyncWorkers.runAsync(localLoggableHandler, local1);
    }
    while (true)
    {
      return i;
      i = 0;
    }
  }

  private static boolean deleteRemoteMusicAndPlaylists(Context paramContext, SQLiteDatabase paramSQLiteDatabase)
  {
    int i = 0;
    int j = paramSQLiteDatabase.delete("MUSIC", "MUSIC.SourceAccount!=0", null) + i;
    int k = PlayList.deleteSyncedPlaylists(paramSQLiteDatabase);
    int m = j + k;
    int n = paramSQLiteDatabase.delete("RECENT", "RecentNautilusAlbumId IS NOT NULL", null);
    int i1 = m + n;
    if (i1 > 0)
      RecentItemsManager.update(paramContext, paramSQLiteDatabase);
    int i2 = paramSQLiteDatabase.delete("LISTITEMS", "LISTITEMS.Id IN (SELECT LISTITEMS.Id FROM LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE MUSIC.SourceAccount!=0)", null);
    int i3 = i1 + i2;
    int i4 = paramSQLiteDatabase.delete("LIST_TOMBSTONES", null, null);
    int i5 = i3 + i4;
    int i6 = paramSQLiteDatabase.delete("LISTITEM_TOMBSTONES", null, null);
    int i7 = i5 + i6;
    int i8 = paramSQLiteDatabase.delete("RADIO_STATIONS", null, null);
    int i9 = i7 + i8;
    int i10 = paramSQLiteDatabase.delete("RADIO_STATION_TOMBSTONES", null, null);
    int i11 = i9 + i10;
    int i12 = paramSQLiteDatabase.delete("_sync_state", null, null);
    int i13 = i11 + i12;
    int i14 = paramSQLiteDatabase.delete("KEEPON", null, null);
    int i15 = i13 + i14;
    int i16 = paramSQLiteDatabase.delete("SHOULDKEEPON", null, null);
    int i17 = i15 + i16;
    int i18 = paramSQLiteDatabase.delete("SUGGESTED_SEEDS", null, null);
    int i19 = i17 + i18;
    int i20 = removeOrphanedSuggestedMixes(paramSQLiteDatabase);
    if (i19 + i20 > 0)
      i = 1;
    return i;
  }

  private int deleteTracks(SQLiteDatabase paramSQLiteDatabase, Set<Long> paramSet)
  {
    int i;
    if ((paramSet == null) || (paramSet.isEmpty()))
      i = 0;
    while (true)
    {
      return i;
      StringBuilder localStringBuilder1 = new StringBuilder();
      StringBuilder localStringBuilder2 = localStringBuilder1.append("MUSIC.Id");
      DbUtils.appendIN(localStringBuilder1, paramSet);
      String str1 = localStringBuilder1.toString();
      i = paramSQLiteDatabase.delete("MUSIC", str1, null);
      if ((LOGV) && (i > 0))
      {
        String str2 = "Deleted " + i + " music records";
        Log.i("MusicStore", str2);
      }
    }
  }

  private SQLiteDatabase downgrade(String paramString, boolean paramBoolean)
  {
    synchronized (this.mDowngraded)
    {
      if (!this.mDowngraded.get())
      {
        this.mDbOpener.close();
        if (new File(paramString).delete())
        {
          String str1 = "Sucessfully deleted old database file at " + paramString;
          Log.i("MusicStore", str1);
          this.mDowngraded.set(true);
        }
      }
      else
      {
        SQLiteDatabase localSQLiteDatabase = this.mDbOpener.getWritableDatabase();
        return localSQLiteDatabase;
      }
      String str2 = "Failed to delete old database file at " + paramString;
      Log.e("MusicStore", str2);
    }
  }

  public static String generateClientId()
  {
    return UUID.randomUUID().toString();
  }

  public static long generateId(String paramString)
  {
    long l1;
    if ((paramString == null) || (paramString.length() == 0))
      l1 = 0L;
    while (true)
    {
      return l1;
      BobJenkinsLookup3 localBobJenkinsLookup3 = new BobJenkinsLookup3();
      try
      {
        byte[] arrayOfByte = paramString.getBytes("UTF-8");
        long l2 = localBobJenkinsLookup3.hashbig(arrayOfByte);
        l1 = l2;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        Log.wtf("MusicStore", "UTF-8 is not supported?");
        throw new RuntimeException("Failed to convert to UTF-8", localUnsupportedEncodingException);
      }
    }
  }

  public static int getDatabaseVersion()
  {
    return 72;
  }

  public static int getDbTableSize(SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "count(1)";
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    String str1 = paramString;
    String[] arrayOfString2 = null;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    Cursor localCursor = localSQLiteDatabase.query(str1, arrayOfString1, null, arrayOfString2, str2, str3, str4);
    if (localCursor != null);
    try
    {
      if (localCursor.moveToFirst())
      {
        int i = localCursor.getInt(0);
        j = i;
        return j;
      }
      int j = -1;
    }
    finally
    {
      safeClose(localCursor);
    }
  }

  private Set<Long> getEphemeralTracksNotInPlaylist(SQLiteDatabase paramSQLiteDatabase)
  {
    HashSet localHashSet = new HashSet();
    Cursor localCursor;
    try
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = "MUSIC.Id";
      localCursor = paramSQLiteDatabase.query("MUSIC", arrayOfString, "Domain = 4 AND NOT EXISTS( SELECT 1 FROM LISTITEMS WHERE LISTITEMS.MusicId=MUSIC.Id)", null, null, null, null);
      if ((localCursor != null) && (localCursor.moveToNext()))
      {
        Long localLong = Long.valueOf(localCursor.getLong(0));
        boolean bool = localHashSet.add(localLong);
      }
    }
    finally
    {
      safeClose(localCursor);
    }
    return localHashSet;
  }

  public static Store getInstance(Context paramContext)
  {
    sInstance.init(paramContext);
    return sInstance;
  }

  // ERROR //
  private long getListIdByShareToken(SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    // Byte code:
    //   0: iconst_2
    //   1: anewarray 49	java/lang/String
    //   4: astore_3
    //   5: aload_3
    //   6: iconst_0
    //   7: aload_2
    //   8: aastore
    //   9: bipush 71
    //   11: invokestatic 53	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   14: astore 4
    //   16: aload_3
    //   17: iconst_1
    //   18: aload 4
    //   20: aastore
    //   21: iconst_1
    //   22: anewarray 49	java/lang/String
    //   25: astore 5
    //   27: aload 5
    //   29: iconst_0
    //   30: ldc_w 635
    //   33: aastore
    //   34: aload_1
    //   35: ldc_w 637
    //   38: aload 5
    //   40: ldc_w 639
    //   43: aload_3
    //   44: aconst_null
    //   45: aconst_null
    //   46: aconst_null
    //   47: aconst_null
    //   48: invokevirtual 642	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   51: lstore 6
    //   53: lload 6
    //   55: lstore 8
    //   57: lload 8
    //   59: ifnull +53 -> 112
    //   62: lload 8
    //   64: invokeinterface 374 1 0
    //   69: ifeq +43 -> 112
    //   72: lload 8
    //   74: iconst_0
    //   75: invokeinterface 626 2 0
    //   80: lstore 6
    //   82: lload 6
    //   84: lstore 10
    //   86: lload 8
    //   88: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   91: lload 10
    //   93: lreturn
    //   94: astore 12
    //   96: iconst_0
    //   97: lstore 8
    //   99: lload 8
    //   101: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   104: aload 12
    //   106: athrow
    //   107: astore 12
    //   109: goto -10 -> 99
    //   112: ldc2_w 198
    //   115: lstore 10
    //   117: goto -31 -> 86
    //
    // Exception table:
    //   from	to	target	type
    //   21	53	94	finally
    //   62	82	107	finally
  }

  private List<Pair<Integer, String>> getLockerTracksAccountAndSourceId(String paramString, String[] paramArrayOfString)
  {
    ArrayList localArrayList = Lists.newArrayList();
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      String[] arrayOfString1 = new String[3];
      arrayOfString1[0] = "SourceAccount";
      arrayOfString1[1] = "SourceId";
      arrayOfString1[2] = "Domain";
      String str1 = paramString;
      String[] arrayOfString2 = paramArrayOfString;
      Cursor localCursor1 = localSQLiteDatabase.query("MUSIC", arrayOfString1, str1, arrayOfString2, null, null, null);
      localCursor2 = localCursor1;
      if (localCursor2 != null)
        while (true)
        {
          int i;
          String str2;
          int j;
          try
          {
            if (!localCursor2.moveToNext())
              break;
            i = localCursor2.getInt(0);
            str2 = localCursor2.getString(1);
            j = localCursor2.getInt(2);
            if (i != 0)
              break label155;
            Exception localException1 = new Exception();
            Log.wtf("MusicStore", "Sideloaded song encountered when expecting locker songs only", localException1);
            continue;
          }
          finally
          {
          }
          safeClose(localCursor2);
          endRead(localSQLiteDatabase);
          throw localObject1;
          label155: if (j != 0)
          {
            String str3 = "Non-default domain is encountered when expecting locker songs only. " + j;
            Exception localException2 = new Exception();
            Log.wtf("MusicStore", str3, localException2);
          }
          else
          {
            Integer localInteger = Integer.valueOf(i);
            Pair localPair = new Pair(localInteger, str2);
            boolean bool = localArrayList.add(localPair);
          }
        }
      safeClose(localCursor2);
      endRead(localSQLiteDatabase);
      return localArrayList;
    }
    finally
    {
      while (true)
        Cursor localCursor2 = null;
    }
  }

  public static long getMusicIdForSourceId(SQLiteDatabase paramSQLiteDatabase, int paramInt, String paramString)
    throws FileNotFoundException
  {
    Cursor localCursor;
    try
    {
      String[] arrayOfString1 = new String[1];
      arrayOfString1[0] = "Id";
      String[] arrayOfString2 = new String[2];
      String str1 = String.valueOf(paramInt);
      arrayOfString2[0] = str1;
      arrayOfString2[1] = paramString;
      localCursor = paramSQLiteDatabase.query("MUSIC", arrayOfString1, "SourceAccount=? AND SourceId=? ", arrayOfString2, null, null, null);
      if ((localCursor == null) || (!localCursor.moveToFirst()))
      {
        String str2 = "File with sourceId " + paramString + " is not found";
        throw new FileNotFoundException(str2);
      }
    }
    finally
    {
      safeClose(localCursor);
    }
    int i = 0;
    long l1 = localCursor.getLong(i);
    long l2 = l1;
    safeClose(localCursor);
    return l2;
  }

  public static Cursor getMusicIdsForSourceIds(SQLiteDatabase paramSQLiteDatabase, Cursor paramCursor, int paramInt)
  {
    CustomMergeCursor localCustomMergeCursor = null;
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "Id";
    LinkedList localLinkedList = new LinkedList();
    int i = 0;
    StringBuilder localStringBuilder1;
    if ((!paramCursor.isLast()) && (!paramCursor.isAfterLast()))
    {
      localStringBuilder1 = new StringBuilder();
      StringBuilder localStringBuilder2 = localStringBuilder1.append("(");
      label64: if (!paramCursor.moveToNext())
        break label380;
      String str1 = paramCursor.getString(0);
      StringBuilder localStringBuilder3 = localStringBuilder1.append("select ");
      StringBuilder localStringBuilder4 = localStringBuilder1.append("'").append(str1).append("' as sel_id, ");
      StringBuilder localStringBuilder5 = localStringBuilder1.append("'");
      i += 1;
      StringBuilder localStringBuilder6 = localStringBuilder5.append(i).append("' as sel_pos, ");
      StringBuilder localStringBuilder7 = localStringBuilder1.append("'");
      StringBuilder localStringBuilder8 = new StringBuilder();
      String str2 = String.valueOf(paramInt);
      String str3 = str2 + "' as sel_account";
      StringBuilder localStringBuilder9 = localStringBuilder7.append(str3);
      if (i % 200 != 0);
    }
    label380: for (int j = i; ; j = i)
    {
      StringBuilder localStringBuilder10 = localStringBuilder1.append(") as selected JOIN ").append("MUSIC");
      StringBuilder localStringBuilder11 = localStringBuilder1.append(" ON (").append("SourceId").append("= selected.sel_id AND selected.sel_account = ").append("MUSIC.SourceAccount").append(") ");
      String str4 = localStringBuilder1.toString();
      SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
      String[] arrayOfString2 = null;
      String str5 = null;
      String str6 = null;
      Cursor localCursor = localSQLiteDatabase.query(str4, arrayOfString1, null, arrayOfString2, str5, str6, "selected.sel_pos");
      if (localCursor != null)
        boolean bool = localLinkedList.add(localCursor);
      i = j;
      break;
      if (paramCursor.isLast())
        break label64;
      StringBuilder localStringBuilder12 = localStringBuilder1.append(" UNION ALL ");
      break label64;
      if (!localLinkedList.isEmpty())
      {
        Cursor[] arrayOfCursor1 = new Cursor[0];
        Cursor[] arrayOfCursor2 = (Cursor[])localLinkedList.toArray(arrayOfCursor1);
        localCustomMergeCursor = new CustomMergeCursor(arrayOfCursor2);
      }
      return localCustomMergeCursor;
    }
  }

  private static ContentIdentifier getPreferredMusicIdForSongId(SQLiteDatabase paramSQLiteDatabase, long paramLong, int[] paramArrayOfInt, int paramInt)
    throws FileNotFoundException
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("SongId").append("=?");
    StringBuilder localStringBuilder3 = localStringBuilder1.append(" AND ").append("+Domain");
    int[] arrayOfInt = paramArrayOfInt;
    DbUtils.appendIN(localStringBuilder1, arrayOfInt);
    String str1;
    if (paramInt == 0)
      str1 = " DESC, ";
    ContentIdentifier localContentIdentifier;
    while (true)
    {
      localContentIdentifier = null;
      try
      {
        String[] arrayOfString1 = new String[2];
        arrayOfString1[0] = "MUSIC.Id";
        arrayOfString1[1] = "Domain";
        String str2 = localStringBuilder1.toString();
        String[] arrayOfString2 = new String[1];
        String str3 = Long.toString(paramLong);
        arrayOfString2[0] = str3;
        StringBuilder localStringBuilder4 = new StringBuilder().append("LocalCopyType");
        String str4 = str1;
        String str5 = str4 + "Domain" + " ASC, " + "MUSIC.Id" + " DESC";
        localCursor = paramSQLiteDatabase.query("MUSIC", arrayOfString1, str2, arrayOfString2, null, null, str5, "1");
        if ((localCursor != null) && (localCursor.moveToFirst()))
        {
          long l1 = localCursor.getLong(0);
          int i = localCursor.getInt(1);
          localContentIdentifier = new com/google/android/music/download/ContentIdentifier;
          ContentIdentifier.Domain localDomain = ContentIdentifier.Domain.fromDBValue(i);
          localContentIdentifier.<init>(l1, localDomain);
        }
        safeClose(localCursor);
        if (localContentIdentifier == null)
        {
          StringBuilder localStringBuilder5 = new StringBuilder().append("Could not find file for song id:");
          long l2 = paramLong;
          String str6 = l2;
          throw new FileNotFoundException(str6);
          str1 = " ASC, ";
        }
      }
      finally
      {
        Cursor localCursor;
        safeClose(localCursor);
      }
    }
    return localContentIdentifier;
  }

  private long getShouldKeeponDownloadedSongCount(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    return shouldKeeponSongCountWithCondition(paramSQLiteDatabase, paramLong, "LocalCopyType=200");
  }

  private long getShouldKeeponSongCount(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    return shouldKeeponSongCountWithCondition(paramSQLiteDatabase, paramLong, null);
  }

  public static long getSongId(SQLiteDatabase paramSQLiteDatabase, long paramLong)
    throws FileNotFoundException
  {
    Cursor localCursor;
    try
    {
      String[] arrayOfString1 = new String[1];
      arrayOfString1[0] = "SongId";
      String[] arrayOfString2 = new String[1];
      String str1 = String.valueOf(paramLong);
      arrayOfString2[0] = str1;
      localCursor = paramSQLiteDatabase.query("MUSIC", arrayOfString1, "Id=?", arrayOfString2, null, null, null);
      if ((localCursor == null) || (localCursor.getCount() < 1))
      {
        String str2 = "File with id " + paramLong + " is not found";
        throw new FileNotFoundException(str2);
      }
    }
    finally
    {
      safeClose(localCursor);
    }
    boolean bool = localCursor.moveToFirst();
    long l1 = localCursor.getLong(0);
    long l2 = l1;
    safeClose(localCursor);
    return l2;
  }

  public static Pair<String, Integer> getSourceIdAndTypeForMusicId(SQLiteDatabase paramSQLiteDatabase, long paramLong)
    throws FileNotFoundException
  {
    Cursor localCursor;
    try
    {
      String[] arrayOfString1 = new String[2];
      arrayOfString1[0] = "SourceId";
      arrayOfString1[1] = "SourceType";
      String[] arrayOfString2 = new String[1];
      String str1 = String.valueOf(paramLong);
      arrayOfString2[0] = str1;
      localCursor = paramSQLiteDatabase.query("MUSIC", arrayOfString1, "Id=? ", arrayOfString2, null, null, null);
      if ((localCursor == null) || (localCursor.getCount() < 1))
      {
        String str2 = "File with musicId " + paramLong + " is not found";
        throw new FileNotFoundException(str2);
      }
    }
    finally
    {
      safeClose(localCursor);
    }
    boolean bool = localCursor.moveToFirst();
    String str3 = localCursor.getString(0);
    int i = localCursor.getInt(1);
    int j = i;
    safeClose(localCursor);
    if ((str3 == null) || (j == 0))
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = str3;
      Integer localInteger1 = Integer.valueOf(j);
      arrayOfObject[1] = localInteger1;
      String str4 = String.format("Invalid value for sourceId=%s or sourceType=%s ", arrayOfObject);
      throw new IllegalStateException(str4);
    }
    Integer localInteger2 = Integer.valueOf(j);
    return new Pair(str3, localInteger2);
  }

  /** @deprecated */
  private void init(Context paramContext)
  {
    try
    {
      if (this.mDbOpener == null)
      {
        MusicUtils.assertMainProcess(paramContext, "Store being initialized in wrong process");
        this.mContext = paramContext;
        Log.i("MusicStore", "Database version: 72");
        DatabaseHelper localDatabaseHelper = new DatabaseHelper();
        this.mDbOpener = localDatabaseHelper;
        MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(paramContext);
        this.mTracker = localMusicEventLogger;
        SharedPreferences localSharedPreferences = this.mContext.getSharedPreferences("store.preferences", 0);
        if (!localSharedPreferences.getBoolean("media.store.cache.migration.tried", false))
        {
          boolean bool = localSharedPreferences.edit().putBoolean("media.store.cache.migration.tried", true).commit();
          migrateCachedFiles();
        }
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private void migrateCachedFiles()
  {
    long l1 = SystemClock.uptimeMillis();
    if (LOGV)
      Log.v("MusicStore", "migrating the external storage");
    File localFile1 = CacheUtils.getExternalMusicCacheDirectory_Old(this.mContext);
    File localFile2 = CacheUtils.getExternalMusicCacheDirectory(this.mContext);
    migrateCachedFilesHelper(localFile1, localFile2);
    File localFile3 = CacheUtils.getExternalArtworkCacheDirectory_Old(this.mContext);
    File localFile4 = CacheUtils.getExternalAlbumArtworkCacheDirectory(this.mContext);
    migrateCachedFilesHelper(localFile3, localFile4);
    if (LOGV)
      Log.v("MusicStore", "migrating the internal storage");
    File localFile5 = CacheUtils.getInternalMusicCacheDirectory_Old(this.mContext);
    File localFile6 = CacheUtils.getInternalMusicCacheDirectory(this.mContext);
    migrateCachedFilesHelper(localFile5, localFile6);
    File localFile7 = CacheUtils.getInternalArtworkCacheDirectory_Old(this.mContext);
    File localFile8 = CacheUtils.getInternalAlbumArtworkCacheDirectory(this.mContext);
    migrateCachedFilesHelper(localFile7, localFile8);
    long l2 = SystemClock.uptimeMillis() - l1;
    String str = "Migrating cache files took " + l2 + " ms";
    Log.i("MusicStore", str);
  }

  private void migrateCachedFilesHelper(File paramFile1, File paramFile2)
  {
    if (paramFile1 == null)
      return;
    try
    {
      if (!paramFile1.exists())
        return;
      if (LOGV)
      {
        String str1 = "migrating from " + paramFile1 + " to " + paramFile2;
        Log.v("MusicStore", str1);
      }
      if (paramFile1.renameTo(paramFile2))
        return;
      Log.w("MusicStore", "Failed to migrate the cache dir. Cleaning up..");
      if (CacheUtils.cleanUpDirectory(paramFile1))
        return;
      Log.w("MusicStore", "Failed to clean up the cache dir.");
      return;
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Migration failed: ");
      String str2 = localException.getMessage();
      String str3 = str2;
      Log.w("MusicStore", str3, localException);
    }
  }

  private void notifySuggestedMixChange()
  {
    ContentResolver localContentResolver1 = this.mContext.getContentResolver();
    Uri localUri1 = MusicContent.SuggestedSeeds.CONTENT_URI;
    localContentResolver1.notifyChange(localUri1, null, false);
    ContentResolver localContentResolver2 = this.mContext.getContentResolver();
    Uri localUri2 = MusicContent.Playlists.CONTENT_URI;
    localContentResolver2.notifyChange(localUri2, null, false);
    ContentResolver localContentResolver3 = this.mContext.getContentResolver();
    Uri localUri3 = MusicContent.Mainstage.CONTENT_URI;
    localContentResolver3.notifyChange(localUri3, null, false);
  }

  private static boolean preparePlaylistForModification(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    boolean bool = false;
    if (PlayQueue.getInstance().getListId() == paramLong)
    {
      bool = true;
      if (!bool)
        break label26;
    }
    while (true)
    {
      return bool;
      bool = false;
      break;
      label26: PlayList.disconnectFromMediaStore(paramSQLiteDatabase, paramLong);
      bool = true;
    }
  }

  private static int removeOrphanPlayQueueItemsAndGroups(SQLiteDatabase paramSQLiteDatabase)
  {
    PlayQueue localPlayQueue = PlayQueue.getInstance();
    int i = localPlayQueue.removeOrphanItems(paramSQLiteDatabase);
    int j = localPlayQueue.removeOrphanGroups(paramSQLiteDatabase);
    return i + j;
  }

  private static int removeOrphanedSuggestedMixes(SQLiteDatabase paramSQLiteDatabase)
  {
    int i = 0;
    StringBuffer localStringBuffer1 = new StringBuffer();
    StringBuffer localStringBuffer2 = localStringBuffer1.append("ListType=50");
    StringBuffer localStringBuffer3 = localStringBuffer1.append(" AND ");
    StringBuffer localStringBuffer4 = localStringBuffer1.append("Id NOT IN (SELECT SeedListId FROM SUGGESTED_SEEDS WHERE SeedListId NOT NULL)");
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "Id";
    String str1 = localStringBuffer1.toString();
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    int k = i;
    int m = i;
    int n = i;
    Object localObject1 = localSQLiteDatabase.query("LISTS", arrayOfString1, str1, i, k, m, n);
    if (localObject1 != null);
    int j;
    try
    {
      if (((Cursor)localObject1).getCount() > 0)
      {
        j = ((Cursor)localObject1).getCount();
        int i1 = 0;
        if (((Cursor)localObject1).moveToNext())
        {
          int i2 = i1 + 1;
          long l = ((Cursor)localObject1).getLong(0);
          j[i1] = l;
          i1 = i2;
        }
      }
    }
    finally
    {
      safeClose((Cursor)localObject1);
    }
    if (j != 0)
    {
      String[] arrayOfString2 = new String[1];
      localObject1 = j.length;
      int i3 = 0;
      while (i3 < localObject1)
      {
        String str2 = Long.toString(j[i3]);
        arrayOfString2[0] = str2;
        int i4 = paramSQLiteDatabase.delete("LISTITEMS", "ListId=?", arrayOfString2);
        int i5 = paramSQLiteDatabase.delete("LISTS", "Id=?", arrayOfString2);
        i3 += 1;
      }
      if (LOGV)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Removed ");
        int i6 = j.length;
        String str3 = i6 + " orphaned suggested mixes";
        Log.i("MusicStore", str3);
      }
    }
    for (int i7 = j.length; ; i7 = 0)
      return i7;
  }

  private void replaceSuggestedSeeds(SQLiteDatabase paramSQLiteDatabase, int paramInt, String[] paramArrayOfString)
  {
    int j;
    Object localObject4;
    if (paramArrayOfString != null)
      while (true)
      {
        Object localObject2;
        Cursor localCursor1;
        try
        {
          if (paramArrayOfString.length <= 0)
            break label481;
          int i = paramArrayOfString.length;
          j = new HashSet(i);
          int m = paramArrayOfString.length;
          localObject2 = null;
          if (localObject2 >= m)
            break;
          localObject4 = paramArrayOfString[localObject2];
          if (!TextUtils.isEmpty((CharSequence)localObject4))
            break label76;
          throw new IllegalArgumentException("Empty track id for suggested seed");
        }
        finally
        {
          localCursor1 = null;
        }
        safeClose(localCursor1);
        throw localObject5;
        label76: boolean bool1 = j.add(localObject4);
        int n = localObject2 + 1;
      }
    label481: int i2;
    for (int i1 = j; ; i2 = 0)
    {
      ArrayList localArrayList = new ArrayList();
      String[] arrayOfString = new String[3];
      arrayOfString[0] = "Id";
      arrayOfString[1] = "SeedSourceAccount";
      arrayOfString[2] = "SeedTrackSourceId";
      Cursor localCursor2 = paramSQLiteDatabase.query("SUGGESTED_SEEDS", arrayOfString, null, null, null, null, null);
      Object localObject3 = localCursor2;
      int k;
      Object localObject1;
      if (localObject3 != null)
      {
        try
        {
          while (localObject3.moveToNext())
          {
            k = localObject3.getInt(1);
            localObject1 = localObject3.getString(2);
            if ((i1 == 0) || (k == paramInt) || (!i1.contains(localObject1)))
            {
              Long localLong = Long.valueOf(localObject3.getLong(0));
              boolean bool2 = localArrayList.add(localLong);
            }
          }
        }
        finally
        {
        }
        break;
        localObject3.close();
        if (!localArrayList.isEmpty())
        {
          StringBuilder localStringBuilder1 = new StringBuilder();
          StringBuilder localStringBuilder2 = localStringBuilder1.append("Id");
          DbUtils.appendIN(localStringBuilder1, localArrayList);
          String str1 = localStringBuilder1.toString();
          k = paramSQLiteDatabase.delete("SUGGESTED_SEEDS", str1, null);
          if (LOGV)
          {
            String str2 = "Deleted " + k + " suggested seeds";
            Log.i("MusicStore", str2);
          }
        }
        localObject3 = null;
      }
      if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
      {
        localObject1 = new ContentValues(3);
        Integer localInteger1 = Integer.valueOf(paramInt);
        ((ContentValues)localObject1).put("SeedSourceAccount", localInteger1);
        localObject4 = new Random();
        int i3 = paramArrayOfString.length;
        k = 0;
        while (k < i3)
        {
          String str3 = paramArrayOfString[k];
          ((ContentValues)localObject1).put("SeedTrackSourceId", str3);
          Integer localInteger2 = Integer.valueOf(((Random)localObject4).nextInt(2147483646) + 1);
          ((ContentValues)localObject1).put("SeedOrder", localInteger2);
          long l = paramSQLiteDatabase.insert("SUGGESTED_SEEDS", null, (ContentValues)localObject1);
          k += 1;
        }
      }
      int i4 = clearInvalidPlaylistRefsFromSuggestedSeeds(paramSQLiteDatabase);
      int i5 = removeOrphanedSuggestedMixes(paramSQLiteDatabase);
      safeClose(localObject3);
      return;
    }
  }

  static int resetLocalCopyForOrphanedShouldKeepOnMusic(SQLiteDatabase paramSQLiteDatabase)
  {
    ContentValues localContentValues = new ContentValues(2);
    localContentValues.putNull("LocalCopyPath");
    Integer localInteger1 = Integer.valueOf(0);
    localContentValues.put("LocalCopyType", localInteger1);
    Integer localInteger2 = Integer.valueOf(0);
    localContentValues.put("LocalCopyStorageType", localInteger2);
    Integer localInteger3 = Integer.valueOf(0);
    localContentValues.put("LocalCopySize", localInteger3);
    Integer localInteger4 = Integer.valueOf(0);
    localContentValues.put("CacheDate", localInteger4);
    LogFile localLogFile = Log.getLogFile("com.google.android.music.pin");
    if (localLogFile != null)
      localLogFile.d("MusicStore", "resetLocalCopyForOrphanedShouldKeepOnMusic()");
    return paramSQLiteDatabase.update("MUSIC", localContentValues, "LocalCopyType=200 AND Id NOT IN (SELECT DISTINCT MusicId FROM SHOULDKEEPON)", null);
  }

  public static void safeClose(Cursor paramCursor)
  {
    if (paramCursor == null)
      return;
    try
    {
      paramCursor.close();
      return;
    }
    catch (Exception localException)
    {
      Log.e("MusicStore", "Failed to close cursor", localException);
    }
  }

  public static void safeClose(SQLiteProgram paramSQLiteProgram)
  {
    if (paramSQLiteProgram == null)
      return;
    try
    {
      paramSQLiteProgram.close();
      return;
    }
    catch (Exception localException)
    {
      Log.e("MusicStore", "Failed to close SQLiteProgram", localException);
    }
  }

  // ERROR //
  private long shouldKeeponSongCountWithCondition(SQLiteDatabase paramSQLiteDatabase, long paramLong, String paramString)
  {
    // Byte code:
    //   0: ldc_w 1044
    //   3: astore 5
    //   5: iconst_1
    //   6: anewarray 49	java/lang/String
    //   9: astore 6
    //   11: aload 6
    //   13: iconst_0
    //   14: ldc_w 360
    //   17: aastore
    //   18: new 250	java/lang/StringBuilder
    //   21: dup
    //   22: invokespecial 251	java/lang/StringBuilder:<init>	()V
    //   25: ldc_w 1046
    //   28: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   31: astore 7
    //   33: aload 4
    //   35: ifnonnull +98 -> 133
    //   38: ldc_w 1048
    //   41: astore 8
    //   43: aload 7
    //   45: aload 8
    //   47: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   50: invokevirtual 265	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   53: astore 9
    //   55: iconst_1
    //   56: anewarray 49	java/lang/String
    //   59: astore 10
    //   61: lload_2
    //   62: invokestatic 750	java/lang/Long:toString	(J)Ljava/lang/String;
    //   65: astore 11
    //   67: aload 10
    //   69: iconst_0
    //   70: aload 11
    //   72: aastore
    //   73: aload_1
    //   74: iconst_1
    //   75: aload 5
    //   77: aload 6
    //   79: aload 9
    //   81: aload 10
    //   83: aconst_null
    //   84: aconst_null
    //   85: aconst_null
    //   86: aconst_null
    //   87: invokevirtual 1051	android/database/sqlite/SQLiteDatabase:query	(ZLjava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   90: astore 12
    //   92: aload 12
    //   94: astore 5
    //   96: aload 5
    //   98: ifnull +78 -> 176
    //   101: aload 5
    //   103: invokeinterface 622 1 0
    //   108: ifeq +68 -> 176
    //   111: aload 5
    //   113: iconst_0
    //   114: invokeinterface 626 2 0
    //   119: lstore 12
    //   121: lload 12
    //   123: lstore 13
    //   125: aload 5
    //   127: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   130: lload 13
    //   132: lreturn
    //   133: new 250	java/lang/StringBuilder
    //   136: dup
    //   137: invokespecial 251	java/lang/StringBuilder:<init>	()V
    //   140: ldc_w 740
    //   143: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: aload 4
    //   148: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   151: invokevirtual 265	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   154: astore 12
    //   156: aload 12
    //   158: astore 8
    //   160: goto -117 -> 43
    //   163: astore 15
    //   165: aconst_null
    //   166: astore 16
    //   168: aload 16
    //   170: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   173: aload 15
    //   175: athrow
    //   176: aload 5
    //   178: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   181: ldc2_w 198
    //   184: lstore 13
    //   186: goto -56 -> 130
    //   189: astore 15
    //   191: aload 5
    //   193: astore 16
    //   195: goto -27 -> 168
    //
    // Exception table:
    //   from	to	target	type
    //   0	92	163	finally
    //   133	156	163	finally
    //   101	121	189	finally
  }

  private static void throwIfPlayQueue(long paramLong)
  {
    if (PlayQueue.getInstance().getListId() != paramLong)
      return;
    throw new RuntimeException("The operation cannot be performed on a play queue");
  }

  private List<Long> tryToInsertOrUpdateExternalSongs(SQLiteDatabase paramSQLiteDatabase, int paramInt, List<Track> paramList, boolean paramBoolean)
  {
    String str1 = Integer.toString(paramInt);
    TagNormalizer localTagNormalizer = new TagNormalizer();
    MusicFile localMusicFile1 = new MusicFile();
    localMusicFile1.setTagNormalizer(localTagNormalizer);
    int i = Gservices.getInt(this.mContext.getContentResolver(), "music_max_nautilus_tracks", 100000);
    Object localObject1;
    if (paramBoolean)
    {
      int j = getPersistentNautilusTracksCount(paramSQLiteDatabase);
      if (paramList.size() + j >= i)
      {
        String str2 = "Max number of nautilus tracks reached: " + j;
        Log.w("MusicStore", str2);
        localObject1 = new ArrayList();
        return localObject1;
      }
    }
    int k = paramList.size();
    ArrayList localArrayList = new ArrayList(k);
    SQLiteProgram localSQLiteProgram1 = null;
    SQLiteStatement localSQLiteStatement1 = MusicFile.compileMusicInsertStatement(paramSQLiteDatabase);
    int m = 0;
    long l1;
    label162: int n;
    long l2;
    try
    {
      l1 = System.currentTimeMillis();
      Iterator localIterator = paramList.iterator();
      while (true)
      {
        if (!localIterator.hasNext())
          break label642;
        n = (Track)localIterator.next();
        l2 = n.getEffectiveRemoteId();
        if ((l2 != null) && (l2.length() != 0))
          break;
        StringBuilder localStringBuilder1 = new StringBuilder().append("Track: ");
        String str3 = n.mTitle;
        String str4 = str3 + " has no id.";
        Log.e("MusicStore", str4);
      }
    }
    finally
    {
      safeClose(localSQLiteStatement1);
      safeClose(localSQLiteProgram1);
    }
    localMusicFile1.reset();
    ContentIdentifier.Domain localDomain1;
    if (!n.getDomain().isDefaultDomain())
      localDomain1 = null;
    SQLiteProgram localSQLiteProgram2;
    while (true)
      if (MusicFile.readMusicFile(paramSQLiteDatabase, str1, l2, localMusicFile1) == null)
      {
        if (localDomain1 == null)
        {
          StringBuilder localStringBuilder2 = new StringBuilder().append("Track ");
          String str5 = n.mTitle;
          String str6 = str5 + " (" + l2 + ") not found";
          Log.e("MusicStore", str6);
          break;
          localDomain1 = null;
          continue;
        }
        MusicFile localMusicFile2 = n.formatAsMusicFile(localMusicFile1);
        int i2 = paramInt;
        localMusicFile1.setSourceAccount(i2);
        localMusicFile1.setAddedTime(l1);
        if (paramBoolean)
        {
          ContentIdentifier.Domain localDomain2 = ContentIdentifier.Domain.DEFAULT;
          localMusicFile1.setDomain(localDomain2);
          localMusicFile1.setTrackType(5);
          localMusicFile1.setNeedsSync(true);
        }
        l2 = localMusicFile1.tryToInsertMusicFile(localSQLiteStatement1);
        if (l2 != 65535L)
        {
          Long localLong1 = Long.valueOf(l2);
          boolean bool1 = localArrayList.add(localLong1);
        }
        n = m;
        localSQLiteProgram2 = localSQLiteProgram1;
      }
    while (true)
    {
      SQLiteProgram localSQLiteProgram3 = localSQLiteProgram2;
      int i3 = n;
      break label162;
      Long localLong2 = Long.valueOf(localMusicFile1.getLocalId());
      boolean bool2 = localArrayList.add(localLong2);
      if (localDomain1 != null)
      {
        ContentIdentifier.Domain localDomain3 = n.getDomain();
        ContentIdentifier.Domain localDomain4 = localMusicFile1.getDomain();
        if (localDomain3.equals(localDomain4))
        {
          localDomain1 = localMusicFile1.getDomain();
          MusicFile localMusicFile3 = n.formatAsMusicFile(localMusicFile1);
          int i4 = paramInt;
          localMusicFile1.setSourceAccount(i4);
          localMusicFile1.setAddedTime(l1);
          if (paramBoolean)
          {
            ContentIdentifier.Domain localDomain5 = ContentIdentifier.Domain.DEFAULT;
            localMusicFile1.setDomain(localDomain5);
            localMusicFile1.setTrackType(5);
            localMusicFile1.setNeedsSync(true);
          }
          while (true)
          {
            if (localSQLiteProgram1 == null)
              SQLiteStatement localSQLiteStatement2 = MusicFile.compileFullUpdateStatement(paramSQLiteDatabase);
            SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
            if (!localMusicFile1.tryToUpdateMusicFile(localSQLiteProgram1, localSQLiteDatabase))
              break label694;
            i1 = m + 1;
            localSQLiteProgram2 = localSQLiteProgram1;
            break;
            localMusicFile1.setDomain(localDomain1);
          }
          if ((paramBoolean) && (!localArrayList.isEmpty()))
          {
            MusicEventLogger localMusicEventLogger = this.mTracker;
            Object[] arrayOfObject = new Object[0];
            localMusicEventLogger.trackEvent("addToLibrary", arrayOfObject);
          }
          safeClose(localSQLiteStatement1);
          safeClose(localSQLiteProgram1);
          localObject1 = localArrayList;
          break;
        }
      }
      label642: label694: int i1 = m;
      localSQLiteProgram2 = localSQLiteProgram1;
    }
  }

  public static void updateAlbumIdSourceText(SQLiteDatabase paramSQLiteDatabase)
  {
    String str1 = " '" + '\037' + "' || ";
    String str2 = "UPDATE MUSIC SET AlbumIdSourceText=CanonicalAlbum || (CASE WHEN CanonicalAlbumArtist='' OR (AlbumArtistOrigin=? AND CanonicalAlbum!='') THEN '' ELSE " + str1 + "CanonicalAlbumArtist" + " END)";
    String[] arrayOfString = new String[1];
    String str3 = String.valueOf(1);
    arrayOfString[0] = str3;
    paramSQLiteDatabase.execSQL(str2, arrayOfString);
  }

  public PlayQueueInsertResult addSongsToPlay(Cursor paramCursor, boolean paramBoolean1, boolean paramBoolean2, int paramInt, long paramLong)
  {
    Store localStore = this;
    Cursor localCursor = paramCursor;
    int i = paramInt;
    boolean bool1 = paramBoolean1;
    boolean bool2 = paramBoolean2;
    long l = paramLong;
    return localStore.addSongsToPlay(localCursor, true, i, bool1, bool2, l);
  }

  public PlayQueueInsertResult addSongsToPlay(Cursor paramCursor, boolean paramBoolean1, boolean paramBoolean2, long paramLong)
  {
    Store localStore = this;
    Cursor localCursor = paramCursor;
    int i = 0;
    boolean bool1 = paramBoolean1;
    boolean bool2 = paramBoolean2;
    long l = paramLong;
    return localStore.addSongsToPlay(localCursor, false, i, bool1, bool2, l);
  }

  public PlayQueueAddResult addToPlayQueue(Cursor paramCursor, long paramLong, boolean paramBoolean)
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      PlayQueue localPlayQueue = PlayQueue.getInstance();
      Cursor localCursor = paramCursor;
      long l = paramLong;
      boolean bool = paramBoolean;
      PlayQueueAddResult localPlayQueueAddResult1 = localPlayQueue.queue(localSQLiteDatabase, localCursor, l, bool);
      PlayQueueAddResult localPlayQueueAddResult2 = localPlayQueueAddResult1;
      endWriteTxn(localSQLiteDatabase, true);
      ContentResolver localContentResolver = this.mContext.getContentResolver();
      localPlayQueue.notifyChange(localContentResolver);
      return localPlayQueueAddResult2;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public int appendAlbumToPlaylist(long paramLong1, long paramLong2)
  {
    throwIfPlayQueue(paramLong1);
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      throwIfPlayQueue(paramLong1);
      boolean bool = preparePlaylistForModification(localSQLiteDatabase, paramLong1);
      int i = PlayList.appendAlbum(localSQLiteDatabase, paramLong1, paramLong2);
      int j = i;
      endWriteTxn(localSQLiteDatabase, true);
      if (1 != 0)
        RecentItemsManager.addModifiedPlaylist(this.mContext, paramLong1);
      return j;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public int appendArtistToPlaylist(long paramLong1, long paramLong2)
  {
    throwIfPlayQueue(paramLong1);
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      boolean bool = preparePlaylistForModification(localSQLiteDatabase, paramLong1);
      int i = PlayList.appendArtist(localSQLiteDatabase, paramLong1, paramLong2);
      int j = i;
      endWriteTxn(localSQLiteDatabase, true);
      if (1 != 0)
        RecentItemsManager.addModifiedPlaylist(this.mContext, paramLong1);
      return j;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public int appendGenreToPlaylist(long paramLong1, long paramLong2)
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      boolean bool = preparePlaylistForModification(localSQLiteDatabase, paramLong1);
      int i = PlayList.appendGenre(localSQLiteDatabase, paramLong1, paramLong2);
      int j = i;
      endWriteTxn(localSQLiteDatabase, true);
      if (1 != 0)
        RecentItemsManager.addModifiedPlaylist(this.mContext, paramLong1);
      return j;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public int appendGenreToPlaylist(long paramLong1, long paramLong2, long paramLong3)
  {
    throwIfPlayQueue(paramLong1);
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      boolean bool = preparePlaylistForModification(localSQLiteDatabase, paramLong1);
      long l1 = paramLong1;
      long l2 = paramLong2;
      long l3 = paramLong3;
      int i = PlayList.appendGenre(localSQLiteDatabase, l1, l2, l3);
      int j = i;
      endWriteTxn(localSQLiteDatabase, true);
      if (1 != 0)
        RecentItemsManager.addModifiedPlaylist(this.mContext, paramLong1);
      return j;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public long appendPlaylistItem(long paramLong1, long paramLong2)
    throws FileNotFoundException
  {
    throwIfPlayQueue(paramLong1);
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      boolean bool1 = preparePlaylistForModification(localSQLiteDatabase, paramLong1);
      long l1 = PlayList.appendItem(localSQLiteDatabase, paramLong1, paramLong2);
      long l2 = l1;
      if (l2 != 0L)
      {
        bool2 = true;
        endWriteTxn(localSQLiteDatabase, bool2);
        if (bool2);
        return l2;
      }
      boolean bool2 = false;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public int appendPlaylistToPlaylist(long paramLong1, long paramLong2)
  {
    throwIfPlayQueue(paramLong1);
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      boolean bool = preparePlaylistForModification(localSQLiteDatabase, paramLong1);
      int i = PlayList.appendPlaylist(localSQLiteDatabase, paramLong1, paramLong2);
      int j = i;
      endWriteTxn(localSQLiteDatabase, true);
      if (1 != 0)
        RecentItemsManager.addModifiedPlaylist(this.mContext, paramLong1);
      return j;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public void clearFuturePlayQueueItems(int paramInt)
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      PlayQueue localPlayQueue = PlayQueue.getInstance();
      localPlayQueue.clearItemsAfter(localSQLiteDatabase, paramInt);
      int i = deleteOrphanedExternalMusic(localSQLiteDatabase);
      endWriteTxn(localSQLiteDatabase, true);
      ContentResolver localContentResolver = this.mContext.getContentResolver();
      localPlayQueue.notifyChange(localContentResolver);
      return;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public void clearPlayQueue()
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      PlayQueue localPlayQueue = PlayQueue.getInstance();
      localPlayQueue.clear(localSQLiteDatabase);
      int i = deleteOrphanedExternalMusic(localSQLiteDatabase);
      endWriteTxn(localSQLiteDatabase, true);
      ContentResolver localContentResolver = this.mContext.getContentResolver();
      localPlayQueue.notifyChange(localContentResolver);
      return;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public void clearReferencesInDatabase(Set<Long> paramSet)
  {
    Store localStore = getInstance(this.mContext);
    if (paramSet.isEmpty())
      return;
    StringBuilder localStringBuilder = new StringBuilder();
    DbUtils.appendIN(localStringBuilder, paramSet);
    if (LOGV)
    {
      String str1 = "These don't have files on the file system anymore: " + paramSet;
      Log.i("MusicStore", str1);
    }
    try
    {
      localSQLiteDatabase = localStore.beginWriteTxn();
      ContentValues localContentValues = new ContentValues();
      Integer localInteger1 = Integer.valueOf(0);
      localContentValues.put("LocalCopyType", localInteger1);
      localContentValues.putNull("LocalCopyPath");
      Integer localInteger2 = Integer.valueOf(0);
      localContentValues.put("LocalCopySize", localInteger2);
      Integer localInteger3 = Integer.valueOf(0);
      localContentValues.put("LocalCopyStorageType", localInteger3);
      Integer localInteger4 = Integer.valueOf(0);
      localContentValues.put("CacheDate", localInteger4);
      String str2 = "Id " + localStringBuilder;
      int i = localSQLiteDatabase.update("MUSIC", localContentValues, str2, null);
      boolean bool = true;
      if (localSQLiteDatabase == null)
        return;
      localStore.endWriteTxn(localSQLiteDatabase, bool);
      return;
    }
    finally
    {
      SQLiteDatabase localSQLiteDatabase;
      if (localSQLiteDatabase != null)
        localStore.endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public int countLocalMusic()
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      int i = countLocalMusic(localSQLiteDatabase);
      int j = i;
      return j;
    }
    finally
    {
      endRead(localSQLiteDatabase);
    }
  }

  void createDatabase()
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    endRead(localSQLiteDatabase);
  }

  long createFollowedSharedPlaylist(Account paramAccount, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, List<SyncablePlaylistEntry> paramList)
  {
    long l;
    if ((paramList == null) || (paramList.isEmpty()))
    {
      Log.w("MusicStore", "Missing playlist entries");
      l = 0L;
    }
    while (true)
    {
      return l;
      int i = computeAccountHash(paramAccount);
      SQLiteDatabase localSQLiteDatabase1 = beginWriteTxn();
      boolean bool1 = false;
      LinkedList localLinkedList;
      while (true)
      {
        try
        {
          l = getListIdByShareToken(localSQLiteDatabase1, paramString4);
          if (l == 0L)
          {
            String str1 = paramString1;
            String str2 = paramString2;
            String str3 = paramString3;
            String str4 = paramString4;
            String str5 = paramString5;
            String str6 = paramString6;
            l = PlayList.createPlayList(localSQLiteDatabase1, i, str1, str2, str3, 71, str4, str5, str6).getId();
            localLinkedList = new LinkedList();
            Iterator localIterator = paramList.iterator();
            if (!localIterator.hasNext())
              break;
            SyncablePlaylistEntry localSyncablePlaylistEntry = (SyncablePlaylistEntry)localIterator.next();
            if (localSyncablePlaylistEntry.mIsDeleted)
              continue;
            Track localTrack = localSyncablePlaylistEntry.mTrack;
            boolean bool2 = localLinkedList.add(localTrack);
            continue;
          }
        }
        finally
        {
          endWriteTxn(localSQLiteDatabase1, bool1);
        }
        String[] arrayOfString = new String[1];
        String str7 = Long.toString(l);
        arrayOfString[0] = str7;
        int j = localSQLiteDatabase1.delete("LISTITEMS", "ListId=?", arrayOfString);
      }
      List localList = tryToInsertOrUpdateExternalSongs(localSQLiteDatabase1, i, localLinkedList, false);
      SQLiteDatabase localSQLiteDatabase2 = localSQLiteDatabase1;
      int k = i;
      List<SyncablePlaylistEntry> localList1 = paramList;
      PlayList.appendItemsToSharedPlaylist(localSQLiteDatabase2, l, 0, k, localList, localList1);
      endWriteTxn(localSQLiteDatabase1, true);
      ContentResolver localContentResolver = this.mContext.getContentResolver();
      Uri localUri = MusicContent.Playlists.CONTENT_URI;
      localContentResolver.notifyChange(localUri, null, true);
    }
  }

  public long createPlaylist(String paramString, int paramInt)
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      long l1 = PlayList.createPlayList(localSQLiteDatabase, paramString, paramInt).getId();
      long l2 = l1;
      endWriteTxn(localSQLiteDatabase, true);
      if ((1 != 0) && (paramInt == 0))
        RecentItemsManager.addCreatedPlaylist(this.mContext, l2);
      return l2;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  long createPlaylistForSuggestedSeed(int paramInt, String paramString1, String paramString2, List<Track> paramList)
    throws DataNotFoundException
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    Store localStore = this;
    int i = paramInt;
    String str1 = paramString1;
    String str2 = paramString2;
    List<Track> localList = paramList;
    try
    {
      long l1 = localStore.createPlaylistForSuggestedSeed(localSQLiteDatabase, i, str1, str2, localList);
      long l2 = l1;
      endWriteTxn(localSQLiteDatabase, true);
      if (1 != 0)
      {
        ContentResolver localContentResolver = this.mContext.getContentResolver();
        Uri localUri = MusicContent.Playlists.CONTENT_URI;
        localContentResolver.notifyChange(localUri, null, false);
      }
      return l2;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  long createPlaylistForSuggestedSeed(SQLiteDatabase paramSQLiteDatabase, int paramInt, String paramString1, String paramString2, List<Track> paramList)
    throws DataNotFoundException
  {
    try
    {
      int i = clearInvalidPlaylistRefsFromSuggestedSeeds(paramSQLiteDatabase);
      Object localObject1 = new String[2];
      String str1 = Integer.toString(paramInt);
      localObject1[0] = str1;
      localObject1[1] = paramString1;
      String[] arrayOfString1 = new String[1];
      arrayOfString1[0] = "SeedListId";
      long l1 = paramSQLiteDatabase.query("SUGGESTED_SEEDS", arrayOfString1, "SeedSourceAccount=? AND SeedTrackSourceId=?", (String[])localObject1, null, null, null);
      long l2 = l1;
      if (l2 == null)
      {
        try
        {
          String str2 = "Null cursor when quering suggested seed with track id:" + paramString1;
          throw new DataNotFoundException(str2);
        }
        finally
        {
        }
        safeClose(l2);
        throw localObject3;
      }
      if (!l2.moveToFirst())
      {
        String str3 = "Suggested seed is not found. Track Id:" + paramString1;
        throw new DataNotFoundException(str3);
      }
      long l4;
      label168: Cursor localCursor;
      if (l2.isNull(0))
      {
        l4 = 0L;
        l2.close();
        localCursor = null;
        if (l4 == 0L)
          break label346;
      }
      List localList;
      while (true)
      {
        Object localObject2;
        try
        {
          String[] arrayOfString2 = new String[1];
          String str4 = Long.toString(l4);
          arrayOfString2[0] = str4;
          int j = paramSQLiteDatabase.delete("LISTITEMS", "ListId=?", arrayOfString2);
          l2 = l4;
          localList = tryToInsertOrUpdateExternalSongs(paramSQLiteDatabase, paramInt, paramList, false);
          int k = paramList.size();
          ArrayList localArrayList = new ArrayList(k);
          localObject1 = paramList.iterator();
          if (!((Iterator)localObject1).hasNext())
            break label453;
          int m = ((Track)((Iterator)localObject1).next()).getEffectiveRemoteId();
          if ((m == 0) || (m.length() <= 0))
            continue;
          boolean bool = localArrayList.add(m);
          continue;
        }
        finally
        {
          localObject2 = localCursor;
        }
        break;
        int i1 = 0;
        l1 = localObject2.getLong(i1);
        l4 = l1;
        break label168;
        label346: int n = null;
        l3 = PlayList.createPlayList(paramSQLiteDatabase, paramString2, n).getId();
        ContentValues localContentValues = new ContentValues(1);
        Long localLong = Long.valueOf(l3);
        localContentValues.put("SeedListId", localLong);
        n = paramSQLiteDatabase.update("SUGGESTED_SEEDS", localContentValues, "SeedSourceAccount=? AND SeedTrackSourceId=?", (String[])localObject1);
        if (n != 1)
        {
          String str5 = "Unexpected number of seeds updated: " + n + ", id: " + paramString1;
          Log.e("MusicStore", str5);
        }
      }
      label453: SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
      int i2 = paramInt;
      PlayList.appendItemsToTransientPlaylist(localSQLiteDatabase, l3, 0, i2, localList);
      safeClose(localCursor);
      return l3;
    }
    finally
    {
      while (true)
        long l3 = 0;
    }
  }

  public int deleteAllDefaultDomainMatchingMusicFiles(long paramLong)
  {
    int i = 0;
    int j = deleteAllMatchingLocalMusicFiles(paramLong);
    Object[] arrayOfObject1 = new Object[2];
    Integer localInteger1 = Integer.valueOf(j);
    arrayOfObject1[i] = localInteger1;
    Long localLong1 = Long.valueOf(paramLong);
    arrayOfObject1[1] = localLong1;
    String str1 = String.format("%d local files deleted for musicId %d", arrayOfObject1);
    Log.i("MusicStore", str1);
    try
    {
      long l1 = getSongId(paramLong);
      long l2 = l1;
      StringBuilder localStringBuilder = new StringBuilder().append("SongId=? AND SourceAccount<>");
      String str2 = MusicFile.MEDIA_STORE_SOURCE_ACCOUNT_AS_STRING;
      String str3 = str2 + " AND " + "Domain" + "=" + 0;
      String[] arrayOfString = new String[1];
      String str4 = String.valueOf(l2);
      arrayOfString[0] = str4;
      int k = deleteLockerTracks(str3, arrayOfString);
      Object[] arrayOfObject2 = new Object[2];
      Integer localInteger2 = Integer.valueOf(k);
      arrayOfObject2[0] = localInteger2;
      Long localLong2 = Long.valueOf(paramLong);
      arrayOfObject2[1] = localLong2;
      String str5 = String.format("%d remote files deleted for musicId %d", arrayOfObject2);
      Log.i("MusicStore", str5);
      i = j + k;
      return i;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      while (true)
      {
        String str6 = "Music file not found. Id: " + paramLong;
        Log.w("MusicStore", str6);
      }
    }
  }

  // ERROR //
  public int deleteAllMatchingLocalMusicFiles(long paramLong)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: new 976	java/util/ArrayList
    //   5: dup
    //   6: invokespecial 977	java/util/ArrayList:<init>	()V
    //   9: astore 4
    //   11: aload_0
    //   12: invokevirtual 651	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   15: istore 5
    //   17: iload 5
    //   19: lload_1
    //   20: invokestatic 1351	com/google/android/music/store/Store:getSongId	(Landroid/database/sqlite/SQLiteDatabase;J)J
    //   23: lstore 6
    //   25: iconst_1
    //   26: anewarray 49	java/lang/String
    //   29: astore 8
    //   31: aload 8
    //   33: iconst_0
    //   34: ldc_w 635
    //   37: aastore
    //   38: iconst_2
    //   39: anewarray 49	java/lang/String
    //   42: astore 9
    //   44: lload 6
    //   46: invokestatic 783	java/lang/String:valueOf	(J)Ljava/lang/String;
    //   49: astore 10
    //   51: aload 9
    //   53: iconst_0
    //   54: aload 10
    //   56: aastore
    //   57: sipush 300
    //   60: invokestatic 53	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   63: astore 11
    //   65: aload 9
    //   67: iconst_1
    //   68: aload 11
    //   70: aastore
    //   71: iload 5
    //   73: ldc_w 362
    //   76: aload 8
    //   78: ldc_w 1353
    //   81: aload 9
    //   83: aconst_null
    //   84: aconst_null
    //   85: aconst_null
    //   86: invokevirtual 368	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   89: astore 12
    //   91: aload 12
    //   93: astore 13
    //   95: aload 13
    //   97: ifnull +141 -> 238
    //   100: aload 13
    //   102: invokeinterface 622 1 0
    //   107: ifeq +131 -> 238
    //   110: aload 13
    //   112: iconst_0
    //   113: invokeinterface 626 2 0
    //   118: invokestatic 220	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   121: astore 14
    //   123: aload 4
    //   125: aload 14
    //   127: invokeinterface 671 2 0
    //   132: istore 15
    //   134: goto -34 -> 100
    //   137: astore 16
    //   139: ldc_w 267
    //   142: ldc_w 1355
    //   145: aload 16
    //   147: invokestatic 440	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   150: aload 13
    //   152: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   155: aload_0
    //   156: iload 5
    //   158: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   161: aload 4
    //   163: invokeinterface 476 1 0
    //   168: astore 17
    //   170: iconst_0
    //   171: istore 18
    //   173: aload 17
    //   175: invokeinterface 481 1 0
    //   180: ifeq +55 -> 235
    //   183: aload 17
    //   185: invokeinterface 485 1 0
    //   190: checkcast 217	java/lang/Long
    //   193: invokevirtual 1358	java/lang/Long:longValue	()J
    //   196: lstore 19
    //   198: aload_0
    //   199: lload 19
    //   201: invokevirtual 1362	com/google/android/music/store/Store:deleteLocalMusicFile	(J)Z
    //   204: ifeq +58 -> 262
    //   207: iload 18
    //   209: iconst_1
    //   210: iadd
    //   211: istore 5
    //   213: iload 5
    //   215: istore 18
    //   217: goto -44 -> 173
    //   220: astore 21
    //   222: aload_3
    //   223: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   226: aload_0
    //   227: iload 5
    //   229: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   232: aload 21
    //   234: athrow
    //   235: iload 18
    //   237: ireturn
    //   238: aload 13
    //   240: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   243: goto -88 -> 155
    //   246: astore 21
    //   248: aload 13
    //   250: astore_3
    //   251: goto -29 -> 222
    //   254: astore 16
    //   256: aconst_null
    //   257: astore 13
    //   259: goto -120 -> 139
    //   262: iload 18
    //   264: istore 5
    //   266: goto -53 -> 213
    //
    // Exception table:
    //   from	to	target	type
    //   100	134	137	java/io/FileNotFoundException
    //   17	91	220	finally
    //   100	134	246	finally
    //   139	150	246	finally
    //   17	91	254	java/io/FileNotFoundException
  }

  public boolean deleteAllNautilusContent()
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      String[] arrayOfString = new String[2];
      String str1 = Integer.toString(4);
      arrayOfString[0] = str1;
      String str2 = Integer.toString(5);
      arrayOfString[1] = str2;
      int i = localSQLiteDatabase.delete("MUSIC", "SourceAccount != 0 AND TrackType IN (?, ?)", arrayOfString);
      int j = 0 + i;
      int k = PlayList.removeOrphanedItems(localSQLiteDatabase);
      int m = j + k;
      int n = localSQLiteDatabase.delete("LISTS", "ListType = 71", null);
      int i1 = m + n;
      int i2 = localSQLiteDatabase.delete("RADIO_STATIONS", null, null);
      int i3 = i1 + i2;
      int i4 = localSQLiteDatabase.delete("SUGGESTED_SEEDS", null, null);
      int i5 = i3 + i4;
      int i6 = localSQLiteDatabase.delete("RECENT", "RecentNautilusAlbumId IS NOT NULL", null);
      if (i5 + i6 > 0)
        RecentItemsManager.update(this.mContext, localSQLiteDatabase);
      endWriteTxn(localSQLiteDatabase, true);
      return true;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  /** @deprecated */
  public void deleteEverything()
  {
    boolean bool = false;
    try
    {
      SQLiteDatabase localSQLiteDatabase1 = beginWriteTxn();
      SQLiteDatabase localSQLiteDatabase2 = localSQLiteDatabase1;
      try
      {
        String[] arrayOfString = Schema.ALL_TABLES;
        int i = arrayOfString.length;
        int j = 0;
        while (j < i)
        {
          String str = arrayOfString[j];
          int k = localSQLiteDatabase2.delete(str, null, null);
          j += 1;
        }
        bool = true;
        endWriteTxn(localSQLiteDatabase2, bool);
        return;
      }
      finally
      {
        localObject1 = finally;
        endWriteTxn(localSQLiteDatabase2, false);
        throw localObject1;
      }
    }
    finally
    {
    }
  }

  public void deleteLocalMusic()
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      deleteLocalMusic(localSQLiteDatabase);
      endWriteTxn(localSQLiteDatabase, true);
      return;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public boolean deleteLocalMusicFile(long paramLong)
  {
    boolean bool = false;
    try
    {
      MusicFile localMusicFile = MusicFile.getSummaryMusicFile(this, null, paramLong);
      SQLiteDatabase localSQLiteDatabase2;
      if (localMusicFile.getLocalCopyType() == 300)
      {
        long l = Long.parseLong(localMusicFile.getSourceId());
        if ((l != 0L) && (deleteFromMediaStore(this.mContext, l)))
        {
          bool = false;
          SQLiteDatabase localSQLiteDatabase1 = beginWriteTxn();
          localSQLiteDatabase2 = localSQLiteDatabase1;
        }
      }
      try
      {
        localSQLiteStatement = MusicFile.compileDeleteByLocalIdStatement(localSQLiteDatabase2);
        MusicFile.delete(localSQLiteStatement, paramLong);
        int i = PlayList.deleteLocalMusic(localSQLiteDatabase2, paramLong);
        bool = true;
        return bool;
      }
      finally
      {
        SQLiteStatement localSQLiteStatement;
        safeClose(localSQLiteStatement);
        endWriteTxn(localSQLiteDatabase2, bool);
      }
    }
    catch (DataNotFoundException localDataNotFoundException)
    {
      while (true)
        bool = true;
    }
  }

  public int deleteOrphanedExternalMusic()
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      int i = deleteOrphanedExternalMusic(localSQLiteDatabase);
      int j = i;
      endWriteTxn(localSQLiteDatabase, true);
      return j;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  int deleteOrphanedExternalMusic(SQLiteDatabase paramSQLiteDatabase)
  {
    long l = System.currentTimeMillis() - 86400000L;
    String[] arrayOfString = new String[1];
    String str1 = Long.toString(l);
    arrayOfString[0] = str1;
    int i = paramSQLiteDatabase.delete("MUSIC", "Domain > 0 AND NOT EXISTS( SELECT 1 FROM LISTITEMS WHERE LISTITEMS.MusicId=MUSIC.Id) AND FileDate < ? AND +Rating < 4", arrayOfString);
    if ((LOGV) && (i > 0))
    {
      String str2 = "Deleted " + i + " external music records";
      Log.i("MusicStore", str2);
    }
    return i;
  }

  int deletePersistentNautilusTracksFromAlbum(long paramLong)
  {
    String[] arrayOfString = new String[1];
    String str1 = String.valueOf(paramLong);
    arrayOfString[0] = str1;
    int i = deleteLockerTracks("AlbumId=? AND TrackType=5", arrayOfString);
    Object[] arrayOfObject = new Object[2];
    Integer localInteger = Integer.valueOf(i);
    arrayOfObject[0] = localInteger;
    Long localLong = Long.valueOf(paramLong);
    arrayOfObject[1] = localLong;
    String str2 = String.format("%d nautilus files deleted for album %d.", arrayOfObject);
    Log.i("MusicStore", str2);
    return i;
  }

  public boolean deletePlaylist(Context paramContext, long paramLong)
  {
    boolean bool1 = false;
    throwIfPlayQueue(paramLong);
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      PlayList localPlayList = PlayList.readPlayList(localSQLiteDatabase, paramLong, null);
      if (localPlayList == null)
        Log.w("MusicStore", "Requested playlist is not found");
      while (true)
      {
        return bool1;
        boolean bool2 = true;
        boolean bool3 = true;
        boolean bool4 = localPlayList.delete(localSQLiteDatabase, bool2, bool3);
        bool1 = bool4;
        endWriteTxn(localSQLiteDatabase, true);
        if (localPlayList.getMediaStoreId() != 0L)
        {
          Uri localUri1 = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
          long l = localPlayList.getMediaStoreId();
          Uri localUri2 = ContentUris.withAppendedId(localUri1, l);
          int i = paramContext.getContentResolver().delete(localUri2, null, null);
        }
      }
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public boolean deletePlaylistItem(long paramLong1, long paramLong2)
  {
    boolean bool1 = false;
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      boolean bool2 = preparePlaylistForModification(localSQLiteDatabase, paramLong1);
      PlayList.Item localItem = PlayList.Item.readItem(localSQLiteDatabase, paramLong2, null);
      if (localItem != null)
      {
        boolean bool3 = localItem.delete(localSQLiteDatabase, bool2);
        bool1 = bool3;
      }
      endWriteTxn(localSQLiteDatabase, bool1);
      if (bool1)
        RecentItemsManager.addModifiedPlaylist(this.mContext, paramLong1);
      return bool1;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public boolean deleteRadioStation(Context paramContext, long paramLong)
  {
    boolean bool1 = false;
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      RadioStation localRadioStation = RadioStation.read(localSQLiteDatabase, paramLong, null);
      if (localRadioStation == null)
        Log.w("MusicStore", "Requested radio is not found");
      while (true)
      {
        return bool1;
        boolean bool2 = true;
        boolean bool3 = localRadioStation.delete(localSQLiteDatabase, bool2);
        bool1 = bool3;
        endWriteTxn(localSQLiteDatabase, bool1);
      }
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public boolean deleteRemoteMusicAndPlaylists(Context paramContext, boolean paramBoolean)
  {
    boolean bool1 = false;
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      boolean bool2 = deleteRemoteMusicAndPlaylists(paramContext, localSQLiteDatabase);
      int i = removeOrphanPlayQueueItemsAndGroups(localSQLiteDatabase);
      if (i > 0)
        bool1 = true;
      boolean bool3 = bool1 | bool2;
      endWriteTxn(localSQLiteDatabase, true);
      if (LOGV)
      {
        String str = "deleteRemoteMusicAndPlaylists somethingChanged: " + bool3;
        Log.d("MusicStore", str);
      }
      if ((bool3) || (paramBoolean))
      {
        ContentResolver localContentResolver = this.mContext.getContentResolver();
        Uri localUri = MusicContent.CONTENT_URI;
        localContentResolver.notifyChange(localUri, null, paramBoolean);
      }
      return bool3;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public Cursor executeQuery(boolean paramBoolean, String paramString1, String[] paramArrayOfString1, String paramString2, String[] paramArrayOfString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    boolean bool = paramBoolean;
    String str1 = paramString1;
    String[] arrayOfString1 = paramArrayOfString1;
    String str2 = paramString2;
    String[] arrayOfString2 = paramArrayOfString2;
    String str3 = paramString3;
    String str4 = paramString4;
    String str5 = paramString5;
    String str6 = paramString6;
    try
    {
      localCursor = localSQLiteDatabase.query(bool, str1, arrayOfString1, str2, arrayOfString2, str3, str4, str5, str6);
      int i = localCursor.getCount();
      if (1 == 0)
      {
        safeClose(localCursor);
        localCursor = null;
      }
      endRead(localSQLiteDatabase);
      return localCursor;
    }
    finally
    {
      Cursor localCursor;
      if (0 == 0)
        safeClose(localCursor);
      endRead(localSQLiteDatabase);
    }
  }

  public Cursor executeRawQuery(String paramString)
  {
    return executeRawQuery(paramString, null);
  }

  public Cursor executeRawQuery(String paramString, String[] paramArrayOfString)
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      localCursor = localSQLiteDatabase.rawQuery(paramString, paramArrayOfString);
      int i = localCursor.getCount();
      if (1 == 0)
      {
        safeClose(localCursor);
        localCursor = null;
      }
      endRead(localSQLiteDatabase);
      return localCursor;
    }
    finally
    {
      Cursor localCursor;
      if (0 == 0)
        safeClose(localCursor);
      endRead(localSQLiteDatabase);
    }
  }

  // ERROR //
  public void fixLocalPathSize(java.util.Map<Long, Long> paramMap)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 145	com/google/android/music/store/Store:mContext	Landroid/content/Context;
    //   4: invokestatic 1250	com/google/android/music/store/Store:getInstance	(Landroid/content/Context;)Lcom/google/android/music/store/Store;
    //   7: astore_2
    //   8: aconst_null
    //   9: astore_3
    //   10: aload_1
    //   11: invokeinterface 1473 1 0
    //   16: ifeq +4 -> 20
    //   19: return
    //   20: new 250	java/lang/StringBuilder
    //   23: dup
    //   24: invokespecial 251	java/lang/StringBuilder:<init>	()V
    //   27: ldc_w 1475
    //   30: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   33: astore 4
    //   35: aload_1
    //   36: invokeinterface 1476 1 0
    //   41: istore 5
    //   43: aload 4
    //   45: iload 5
    //   47: invokevirtual 260	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   50: ldc_w 1478
    //   53: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   56: invokevirtual 265	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   59: astore 6
    //   61: ldc_w 267
    //   64: aload 6
    //   66: invokestatic 273	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;)V
    //   69: iconst_0
    //   70: istore 7
    //   72: aload_2
    //   73: invokevirtual 165	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   76: astore 8
    //   78: aload 8
    //   80: astore 9
    //   82: ldc2_w 198
    //   85: lstore 10
    //   87: aload_1
    //   88: invokeinterface 1482 1 0
    //   93: invokeinterface 1483 1 0
    //   98: astore 12
    //   100: lload 10
    //   102: lstore 13
    //   104: aload 12
    //   106: invokeinterface 481 1 0
    //   111: ifeq +132 -> 243
    //   114: aload 12
    //   116: invokeinterface 485 1 0
    //   121: checkcast 1485	java/util/Map$Entry
    //   124: astore 15
    //   126: new 229	android/content/ContentValues
    //   129: dup
    //   130: invokespecial 1253	android/content/ContentValues:<init>	()V
    //   133: astore 16
    //   135: aload 15
    //   137: invokeinterface 1488 1 0
    //   142: checkcast 217	java/lang/Long
    //   145: astore 17
    //   147: aload 16
    //   149: ldc_w 1017
    //   152: aload 17
    //   154: invokevirtual 1318	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
    //   157: iconst_1
    //   158: anewarray 49	java/lang/String
    //   161: astore 18
    //   163: aload 15
    //   165: invokeinterface 1491 1 0
    //   170: checkcast 217	java/lang/Long
    //   173: invokevirtual 1358	java/lang/Long:longValue	()J
    //   176: invokestatic 750	java/lang/Long:toString	(J)Ljava/lang/String;
    //   179: astore 19
    //   181: aload 18
    //   183: iconst_0
    //   184: aload 19
    //   186: aastore
    //   187: aload 9
    //   189: ldc_w 362
    //   192: aload 16
    //   194: ldc_w 785
    //   197: aload 18
    //   199: invokevirtual 248	android/database/sqlite/SQLiteDatabase:update	(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   202: istore 20
    //   204: ldc2_w 1492
    //   207: lload 13
    //   209: ladd
    //   210: lstore 10
    //   212: lload 10
    //   214: ldc2_w 1494
    //   217: lcmp
    //   218: iflt +18 -> 236
    //   221: ldc2_w 198
    //   224: lstore 10
    //   226: aload 9
    //   228: ldc2_w 1496
    //   231: invokevirtual 1500	android/database/sqlite/SQLiteDatabase:yieldIfContendedSafely	(J)Z
    //   234: istore 21
    //   236: lload 10
    //   238: lstore 13
    //   240: goto -136 -> 104
    //   243: iconst_1
    //   244: istore 22
    //   246: aload 9
    //   248: ifnonnull +4 -> 252
    //   251: return
    //   252: aload_2
    //   253: aload 9
    //   255: iload 22
    //   257: invokevirtual 185	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   260: ldc_w 267
    //   263: ldc_w 1502
    //   266: invokestatic 578	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   269: return
    //   270: astore 23
    //   272: aload_3
    //   273: ifnull +19 -> 292
    //   276: aload_2
    //   277: aload_3
    //   278: iload 7
    //   280: invokevirtual 185	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   283: ldc_w 267
    //   286: ldc_w 1502
    //   289: invokestatic 578	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   292: aload 23
    //   294: athrow
    //   295: astore 23
    //   297: aload 9
    //   299: astore_3
    //   300: goto -28 -> 272
    //
    // Exception table:
    //   from	to	target	type
    //   72	78	270	finally
    //   87	236	295	finally
  }

  public Account getAccountByHash(int paramInt)
    throws ProviderException
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      Account localAccount1 = ClientSyncState.Helpers.get(localSQLiteDatabase, paramInt);
      Account localAccount2 = localAccount1;
      return localAccount2;
    }
    finally
    {
      endRead(localSQLiteDatabase);
    }
  }

  public Cursor getAlbumIdsAndAlbumKeepOnIdsForArtist(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      String str = Long.toString(paramLong);
      String[] arrayOfString1 = new String[2];
      arrayOfString1[0] = "MUSIC.AlbumId";
      arrayOfString1[1] = "KEEPON.KeepOnId";
      String[] arrayOfString2 = new String[2];
      arrayOfString2[0] = str;
      arrayOfString2[1] = str;
      Cursor localCursor = localSQLiteDatabase.query(true, "MUSIC LEFT  JOIN KEEPON ON (MUSIC.AlbumId = KEEPON.AlbumId) ", arrayOfString1, "MUSIC.AlbumArtistId=? OR MUSIC.ArtistId=?", arrayOfString2, null, null, null, null);
      if (localCursor != null)
        int i = localCursor.getCount();
      return localCursor;
    }
    finally
    {
      endRead(localSQLiteDatabase);
    }
  }

  public Cursor getAlbumsRequiringArtworkDownload(Set<Long> paramSet)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    if ((paramSet != null) && (!paramSet.isEmpty()))
    {
      Iterator localIterator = paramSet.iterator();
      while (localIterator.hasNext())
      {
        Long localLong = (Long)localIterator.next();
        StringBuilder localStringBuilder2 = localStringBuilder1.append(localLong).append(',');
      }
      int i = localStringBuilder1.length() + -1;
      localStringBuilder1.setLength(i);
    }
    StringBuilder localStringBuilder3 = new StringBuilder().append("SELECT DISTINCT MUSIC.AlbumId FROM MUSIC WHERE MUSIC.AlbumId NOT IN (SELECT ARTWORK.AlbumId FROM ARTWORK WHERE LocalLocation IS NOT NULL) AND AlbumArtLocation IS NOT NULL AND MUSIC.SourceAccount!=0");
    StringBuilder localStringBuilder4;
    String str1;
    if (localStringBuilder1.length() > 0)
    {
      localStringBuilder4 = new StringBuilder().append(" AND MUSIC.AlbumId IN (");
      str1 = localStringBuilder1.toString();
    }
    for (String str2 = str1 + ")"; ; str2 = " LIMIT 10")
    {
      String str3 = str2;
      return executeRawQuery(str3);
    }
  }

  public String getArtLocationForAlbum(long paramLong)
  {
    Object localObject1 = null;
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "AlbumArtLocation";
    String[] arrayOfString2 = new String[1];
    String str1 = Long.toString(paramLong);
    arrayOfString2[0] = str1;
    Store localStore = this;
    Object localObject2 = localObject1;
    Cursor localCursor = localStore.executeQuery(false, "MUSIC", arrayOfString1, "AlbumId=? AND AlbumArtLocation IS NOT NULL", arrayOfString2, (String)localObject1, localObject2, "LocalCopyType DESC", "1");
    if (localCursor != null);
    try
    {
      if (localCursor.moveToNext())
      {
        String str2 = localCursor.getString(0);
        localObject1 = str2;
      }
      return localObject1;
    }
    finally
    {
      safeClose(localCursor);
    }
  }

  public Cursor getArtistIdsForAlbum(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      String str = Long.toString(paramLong);
      String[] arrayOfString = new String[2];
      arrayOfString[0] = str;
      arrayOfString[1] = str;
      Cursor localCursor = localSQLiteDatabase.rawQuery("SELECT _artistId FROM ( SELECT DISTINCT AlbumArtistId as _artistId FROM MUSIC WHERE AlbumId=? UNION  SELECT DISTINCT ArtistId as _artistId FROM MUSIC WHERE AlbumId=?)", arrayOfString);
      if (localCursor != null)
        int i = localCursor.getCount();
      return localCursor;
    }
    finally
    {
      endRead(localSQLiteDatabase);
    }
  }

  // ERROR //
  public Pair<String, Integer> getArtwork(long paramLong)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aload_0
    //   3: invokevirtual 651	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   6: astore 4
    //   8: iconst_2
    //   9: anewarray 49	java/lang/String
    //   12: astore 5
    //   14: aload 5
    //   16: iconst_0
    //   17: ldc_w 1553
    //   20: aastore
    //   21: aload 5
    //   23: iconst_1
    //   24: ldc_w 1555
    //   27: aastore
    //   28: iconst_1
    //   29: anewarray 49	java/lang/String
    //   32: astore 6
    //   34: lload_1
    //   35: invokestatic 750	java/lang/Long:toString	(J)Ljava/lang/String;
    //   38: astore 7
    //   40: aload 6
    //   42: iconst_0
    //   43: aload 7
    //   45: aastore
    //   46: aload 4
    //   48: ldc_w 1557
    //   51: aload 5
    //   53: ldc_w 1559
    //   56: aload 6
    //   58: aconst_null
    //   59: aconst_null
    //   60: aconst_null
    //   61: invokevirtual 368	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   64: astore 8
    //   66: aload 8
    //   68: astore 9
    //   70: aload 9
    //   72: ifnull +67 -> 139
    //   75: aload 9
    //   77: invokeinterface 374 1 0
    //   82: ifeq +57 -> 139
    //   85: aload 9
    //   87: iconst_0
    //   88: invokeinterface 412 2 0
    //   93: astore 10
    //   95: aload 9
    //   97: iconst_1
    //   98: invokeinterface 378 2 0
    //   103: invokestatic 670	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   106: astore 11
    //   108: new 222	android/util/Pair
    //   111: dup
    //   112: aload 10
    //   114: aload 11
    //   116: invokespecial 225	android/util/Pair:<init>	(Ljava/lang/Object;Ljava/lang/Object;)V
    //   119: astore 12
    //   121: aload 9
    //   123: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   126: aload_0
    //   127: aload 4
    //   129: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   132: aload 12
    //   134: astore 13
    //   136: aload 13
    //   138: areturn
    //   139: aload 9
    //   141: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   144: aload_0
    //   145: aload 4
    //   147: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   150: aconst_null
    //   151: astore 13
    //   153: goto -17 -> 136
    //   156: astore 14
    //   158: aload_3
    //   159: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   162: aload_0
    //   163: aload 4
    //   165: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   168: aload 14
    //   170: athrow
    //   171: astore 14
    //   173: aload 9
    //   175: astore_3
    //   176: goto -18 -> 158
    //
    // Exception table:
    //   from	to	target	type
    //   8	66	156	finally
    //   75	121	171	finally
  }

  // ERROR //
  public Pair<String, Integer> getArtwork(String paramString)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_0
    //   3: invokevirtual 165	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   6: astore_3
    //   7: iconst_2
    //   8: anewarray 49	java/lang/String
    //   11: astore 4
    //   13: aload 4
    //   15: iconst_0
    //   16: ldc_w 1553
    //   19: aastore
    //   20: aload 4
    //   22: iconst_1
    //   23: ldc_w 1555
    //   26: aastore
    //   27: iconst_1
    //   28: anewarray 49	java/lang/String
    //   31: astore 5
    //   33: aload 5
    //   35: iconst_0
    //   36: aload_1
    //   37: aastore
    //   38: aload_3
    //   39: ldc_w 1562
    //   42: aload 4
    //   44: ldc_w 1564
    //   47: aload 5
    //   49: aconst_null
    //   50: aconst_null
    //   51: aconst_null
    //   52: invokevirtual 368	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   55: istore 6
    //   57: iload 6
    //   59: istore 7
    //   61: iload 7
    //   63: ifeq +223 -> 286
    //   66: iload 7
    //   68: invokeinterface 374 1 0
    //   73: ifeq +213 -> 286
    //   76: iload 7
    //   78: iconst_0
    //   79: invokeinterface 412 2 0
    //   84: astore 8
    //   86: iload 7
    //   88: iconst_1
    //   89: invokeinterface 378 2 0
    //   94: invokestatic 670	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   97: astore 9
    //   99: new 222	android/util/Pair
    //   102: dup
    //   103: aload 8
    //   105: aload 9
    //   107: invokespecial 225	android/util/Pair:<init>	(Ljava/lang/Object;Ljava/lang/Object;)V
    //   110: astore_2
    //   111: new 229	android/content/ContentValues
    //   114: dup
    //   115: iconst_1
    //   116: invokespecial 232	android/content/ContentValues:<init>	(I)V
    //   119: astore 10
    //   121: invokestatic 1089	java/lang/System:currentTimeMillis	()J
    //   124: lstore 11
    //   126: lload 11
    //   128: invokestatic 220	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   131: astore 13
    //   133: aload 10
    //   135: ldc_w 1566
    //   138: aload 13
    //   140: invokevirtual 1318	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Long;)V
    //   143: iconst_1
    //   144: anewarray 49	java/lang/String
    //   147: astore 14
    //   149: aload 14
    //   151: iconst_0
    //   152: aload_1
    //   153: aastore
    //   154: aload_3
    //   155: ldc_w 1562
    //   158: aload 10
    //   160: ldc_w 1564
    //   163: aload 14
    //   165: invokevirtual 248	android/database/sqlite/SQLiteDatabase:update	(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   168: istore 6
    //   170: iload 6
    //   172: istore 15
    //   174: iload 15
    //   176: iconst_1
    //   177: if_icmpne +67 -> 244
    //   180: iconst_1
    //   181: istore 16
    //   183: iload 16
    //   185: ifne +45 -> 230
    //   188: new 250	java/lang/StringBuilder
    //   191: dup
    //   192: invokespecial 251	java/lang/StringBuilder:<init>	()V
    //   195: ldc_w 1568
    //   198: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   201: iload 15
    //   203: invokevirtual 260	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   206: ldc_w 1570
    //   209: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   212: lload 11
    //   214: invokevirtual 428	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   217: invokevirtual 265	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   220: astore 17
    //   222: ldc_w 267
    //   225: aload 17
    //   227: invokestatic 355	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;)V
    //   230: iload 7
    //   232: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   235: aload_0
    //   236: aload_3
    //   237: iload 16
    //   239: invokevirtual 185	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   242: aload_2
    //   243: areturn
    //   244: iconst_0
    //   245: istore 16
    //   247: goto -64 -> 183
    //   250: astore 18
    //   252: iconst_0
    //   253: istore 16
    //   255: iconst_0
    //   256: istore 7
    //   258: iload 7
    //   260: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   263: aload_0
    //   264: aload_3
    //   265: iload 16
    //   267: invokevirtual 185	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   270: aload 18
    //   272: athrow
    //   273: astore 18
    //   275: iconst_0
    //   276: istore 16
    //   278: goto -20 -> 258
    //   281: astore 18
    //   283: goto -25 -> 258
    //   286: iconst_0
    //   287: istore 16
    //   289: goto -59 -> 230
    //
    // Exception table:
    //   from	to	target	type
    //   7	57	250	finally
    //   66	170	273	finally
    //   188	230	281	finally
  }

  public AutoCacheManager getAutoCacheManager()
  {
    return this.mAutoCacheManager;
  }

  public boolean getAutoPlayIsAllLocal(long paramLong)
  {
    boolean bool = false;
    if (LOGV)
    {
      Object[] arrayOfObject = new Object[1];
      Long localLong = Long.valueOf(paramLong);
      arrayOfObject[0] = localLong;
      String str1 = String.format("getAutoPlayIsAllLocal: autoPlaylistId=%d ", arrayOfObject);
      Log.d("MusicStore", str1);
    }
    String str2 = "MAX(LocalCopyType) = 0";
    String str3;
    if (paramLong == 65532L)
      str3 = "(Rating > 3)";
    while (true)
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = "Id";
      Store localStore = this;
      String str4 = null;
      Cursor localCursor = localStore.executeQuery(false, "MUSIC", arrayOfString, str3, null, "SongId", str2, str4, "1");
      if (localCursor != null);
      try
      {
        if (!localCursor.moveToFirst())
          bool = true;
        if (LOGV)
        {
          String str5 = "getAutoPlayIsAllLocal: res=" + bool;
          Log.d("MusicStore", str5);
        }
        safeClose(localCursor);
        return bool;
        if (paramLong == 65535L)
        {
          str3 = "SongId IN (SELECT SongId FROM MUSIC WHERE +Domain=0 ORDER BY MUSIC.FileDate DESC  LIMIT 500)";
          continue;
        }
        if (paramLong == 65533L)
        {
          str3 = "(TrackType IN (2,3,1) AND +Domain=0)";
          continue;
        }
        if (paramLong == 65534L)
        {
          str3 = "+Domain=0";
          continue;
        }
        String str6 = "Failed to create query for auto list id: " + paramLong;
        throw new IllegalStateException(str6);
      }
      finally
      {
        safeClose(localCursor);
      }
    }
  }

  boolean getAutoPlaylistContains(long paramLong, ItemType paramItemType)
  {
    boolean bool1 = false;
    if (LOGV)
    {
      Object[] arrayOfObject = new Object[2];
      Long localLong = Long.valueOf(paramLong);
      arrayOfObject[0] = localLong;
      arrayOfObject[1] = paramItemType;
      String str1 = String.format("getAutoPlaylistContains: autoPlaylistId=%d itemType=%s", arrayOfObject);
      Log.d("MusicStore", str1);
    }
    int[] arrayOfInt = 2.$SwitchMap$com$google$android$music$store$Store$ItemType;
    int i = paramItemType.ordinal();
    String str3;
    switch (arrayOfInt[i])
    {
    default:
      String str2 = "Unknown itme type: " + paramItemType;
      throw new IllegalStateException(str2);
    case 1:
      str3 = "LocalCopyType IN (100,200,300)";
    case 2:
    }
    while (true)
    {
      String str4;
      label156: Cursor localCursor;
      if (paramLong == 65532L)
      {
        str4 = "(Rating > 3) AND " + str3;
        String[] arrayOfString = new String[1];
        arrayOfString[0] = "Id";
        Store localStore = this;
        String str5 = null;
        String str6 = null;
        String str7 = null;
        localCursor = localStore.executeQuery(false, "MUSIC", arrayOfString, str4, null, str5, str6, str7, "1");
        if (localCursor == null);
      }
      try
      {
        boolean bool2 = localCursor.moveToFirst();
        if (bool2)
          bool1 = true;
        safeClose(localCursor);
        return bool1;
        str3 = "LocalCopyType != 300";
        continue;
        if (paramLong == 65535L)
        {
          str4 = "SongId IN (SELECT SongId FROM MUSIC WHERE +Domain=0 ORDER BY MUSIC.FileDate DESC  LIMIT 500) AND " + str3;
          break label156;
        }
        if (paramLong == 65533L)
        {
          str4 = "(TrackType IN (2,3,1) AND +Domain=0) AND " + str3;
          break label156;
        }
        if (paramLong == 65534L)
        {
          str4 = "+Domain=0 AND " + str3;
          break label156;
        }
        String str8 = "Failed to create query for auto list id: " + paramLong;
        throw new IllegalStateException(str8);
      }
      finally
      {
        safeClose(localCursor);
      }
    }
  }

  Context getContext()
  {
    return this.mContext;
  }

  // ERROR //
  public final byte[] getCpData(long paramLong, boolean paramBoolean)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore 4
    //   3: iconst_3
    //   4: anewarray 49	java/lang/String
    //   7: astore 5
    //   9: aload 5
    //   11: iconst_0
    //   12: ldc_w 1624
    //   15: aastore
    //   16: aload 5
    //   18: iconst_1
    //   19: ldc_w 1626
    //   22: aastore
    //   23: aload 5
    //   25: iconst_2
    //   26: ldc_w 657
    //   29: aastore
    //   30: aload_0
    //   31: invokevirtual 165	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   34: astore 6
    //   36: iconst_1
    //   37: anewarray 49	java/lang/String
    //   40: astore 7
    //   42: lload_1
    //   43: invokestatic 750	java/lang/Long:toString	(J)Ljava/lang/String;
    //   46: astore 8
    //   48: aload 7
    //   50: iconst_0
    //   51: aload 8
    //   53: aastore
    //   54: aload 6
    //   56: ldc_w 362
    //   59: aload 5
    //   61: ldc_w 785
    //   64: aload 7
    //   66: aconst_null
    //   67: aconst_null
    //   68: aconst_null
    //   69: invokevirtual 368	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   72: astore 9
    //   74: aload 9
    //   76: astore 5
    //   78: aload 5
    //   80: ifnull +231 -> 311
    //   83: aload 5
    //   85: invokeinterface 374 1 0
    //   90: ifeq +221 -> 311
    //   93: aload 5
    //   95: iconst_0
    //   96: invokeinterface 1315 2 0
    //   101: ifne +36 -> 137
    //   104: aload 5
    //   106: iconst_0
    //   107: invokeinterface 1630 2 0
    //   112: astore 9
    //   114: aload 9
    //   116: astore 10
    //   118: aload 5
    //   120: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   123: aload_0
    //   124: aload 6
    //   126: iconst_0
    //   127: invokevirtual 185	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   130: aload 10
    //   132: astore 11
    //   134: aload 11
    //   136: areturn
    //   137: iload_3
    //   138: ifeq +205 -> 343
    //   141: iconst_1
    //   142: istore 12
    //   144: aload 5
    //   146: iload 12
    //   148: invokeinterface 378 2 0
    //   153: istore 12
    //   155: aload 5
    //   157: iconst_2
    //   158: invokeinterface 378 2 0
    //   163: invokestatic 766	com/google/android/music/download/ContentIdentifier$Domain:fromDBValue	(I)Lcom/google/android/music/download/ContentIdentifier$Domain;
    //   166: astore 13
    //   168: getstatic 1633	com/google/android/music/download/ContentIdentifier$Domain:NAUTILUS	Lcom/google/android/music/download/ContentIdentifier$Domain;
    //   171: aload 13
    //   173: invokevirtual 1158	com/google/android/music/download/ContentIdentifier$Domain:equals	(Ljava/lang/Object;)Z
    //   176: ifne +15 -> 191
    //   179: iload 12
    //   181: iconst_4
    //   182: if_icmpeq +9 -> 191
    //   185: iload 12
    //   187: iconst_5
    //   188: if_icmpne +155 -> 343
    //   191: invokestatic 1639	com/google/android/music/download/cp/CpUtils:newCpData	()[B
    //   194: astore 14
    //   196: new 229	android/content/ContentValues
    //   199: dup
    //   200: invokespecial 1253	android/content/ContentValues:<init>	()V
    //   203: astore 15
    //   205: aload 15
    //   207: ldc_w 1624
    //   210: aload 14
    //   212: invokevirtual 1642	android/content/ContentValues:put	(Ljava/lang/String;[B)V
    //   215: aload 6
    //   217: ldc_w 362
    //   220: aload 15
    //   222: ldc_w 785
    //   225: aload 7
    //   227: invokevirtual 248	android/database/sqlite/SQLiteDatabase:update	(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   230: istore 16
    //   232: iload 16
    //   234: iconst_1
    //   235: if_icmpeq +57 -> 292
    //   238: new 250	java/lang/StringBuilder
    //   241: dup
    //   242: invokespecial 251	java/lang/StringBuilder:<init>	()V
    //   245: ldc_w 1644
    //   248: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   251: iload 16
    //   253: invokevirtual 260	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   256: invokevirtual 265	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   259: astore 17
    //   261: new 806	java/lang/IllegalStateException
    //   264: dup
    //   265: aload 17
    //   267: invokespecial 807	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   270: athrow
    //   271: astore 18
    //   273: aload 5
    //   275: astore 4
    //   277: aload 4
    //   279: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   282: aload_0
    //   283: aload 6
    //   285: iconst_0
    //   286: invokevirtual 185	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   289: aload 18
    //   291: athrow
    //   292: aload 5
    //   294: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   297: aload_0
    //   298: aload 6
    //   300: iconst_1
    //   301: invokevirtual 185	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   304: aload 14
    //   306: astore 11
    //   308: goto -174 -> 134
    //   311: new 250	java/lang/StringBuilder
    //   314: dup
    //   315: invokespecial 251	java/lang/StringBuilder:<init>	()V
    //   318: ldc_w 1646
    //   321: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   324: lload_1
    //   325: invokevirtual 428	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   328: invokevirtual 265	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   331: astore 19
    //   333: new 675	java/io/FileNotFoundException
    //   336: dup
    //   337: aload 19
    //   339: invokespecial 682	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
    //   342: athrow
    //   343: aload 5
    //   345: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   348: aload_0
    //   349: aload 6
    //   351: iconst_0
    //   352: invokevirtual 185	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   355: aconst_null
    //   356: astore 11
    //   358: goto -224 -> 134
    //   361: astore 18
    //   363: goto -86 -> 277
    //
    // Exception table:
    //   from	to	target	type
    //   83	114	271	finally
    //   144	271	271	finally
    //   311	343	271	finally
    //   36	74	361	finally
  }

  protected SQLiteDatabase getDatabase(boolean paramBoolean)
  {
    MusicUtils.checkMainThread(this.mContext, "db access on main thread");
    try
    {
      SQLiteDatabase localSQLiteDatabase1 = this.mDbOpener.getWritableDatabase();
      localSQLiteDatabase2 = localSQLiteDatabase1;
      return localSQLiteDatabase2;
    }
    catch (DowngradeException localDowngradeException)
    {
      while (true)
      {
        String str1 = localDowngradeException.getFilepath();
        localSQLiteDatabase2 = downgrade(str1, paramBoolean);
      }
    }
    catch (SQLiteException localSQLiteException)
    {
      while (true)
      {
        MusicEventLogger localMusicEventLogger = this.mTracker;
        Object[] arrayOfObject = new Object[4];
        arrayOfObject[0] = "failureReason";
        arrayOfObject[1] = "getDatabaseError";
        arrayOfObject[2] = "failureMsg";
        String str2 = localSQLiteException.getMessage();
        arrayOfObject[3] = str2;
        localMusicEventLogger.trackEvent("wtf", arrayOfObject);
        if (this.mDbOpener.mDBPath == null)
          break;
        Log.e("MusicStore", "Error trying to open the DB", localSQLiteException);
        MediaStoreImporter localMediaStoreImporter = this.mMediaStoreImporter;
        Context localContext = this.mContext;
        localMediaStoreImporter.invalidateMediaStoreImport(localContext);
        String str3 = this.mDbOpener.mDBPath;
        SQLiteDatabase localSQLiteDatabase2 = downgrade(str3, paramBoolean);
      }
      throw localSQLiteException;
    }
  }

  public Cursor getDeletableFiles(int paramInt)
    throws IllegalArgumentException
  {
    if ((paramInt != 1) && (paramInt != 2) && (paramInt != 3))
    {
      String str1 = "Invalid storage type: " + paramInt;
      throw new IllegalArgumentException(str1);
    }
    SQLiteDatabase localSQLiteDatabase = beginRead();
    String[] arrayOfString1 = new String[1];
    String str2 = Integer.toString(paramInt);
    arrayOfString1[0] = str2;
    try
    {
      String[] arrayOfString2 = new String[3];
      arrayOfString2[0] = "Id";
      arrayOfString2[1] = "LocalCopyPath";
      arrayOfString2[2] = "LocalCopyStorageType";
      Cursor localCursor1 = localSQLiteDatabase.query("MUSIC", arrayOfString2, "LocalCopyType IN (100,200) AND LocalCopyStorageType =? AND Id NOT IN (SELECT MusicId FROM SHOULDKEEPON)", arrayOfString1, null, null, null);
      Cursor localCursor2 = localCursor1;
      return localCursor2;
    }
    finally
    {
      endRead(localSQLiteDatabase);
    }
  }

  public ContentIdentifier getFirstItemId(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      ContentIdentifier localContentIdentifier1 = PlayList.getFirstItemId(localSQLiteDatabase, paramLong);
      ContentIdentifier localContentIdentifier2 = localContentIdentifier1;
      return localContentIdentifier2;
    }
    finally
    {
      endRead(localSQLiteDatabase);
    }
  }

  public Cursor getKeepOn(String paramString, String[] paramArrayOfString, Collection<Long> paramCollection, boolean paramBoolean)
  {
    Object localObject1 = new SQLiteQueryBuilder();
    ((SQLiteQueryBuilder)localObject1).setTables("SHOULDKEEPON JOIN MUSIC ON (SHOULDKEEPON.MusicId = MUSIC.Id)");
    HashMap localHashMap = sDownloadQueueProjectionMap;
    ((SQLiteQueryBuilder)localObject1).setProjectionMap(localHashMap);
    StringBuilder localStringBuilder3;
    String str1;
    if ((paramCollection != null) && (!paramCollection.isEmpty()))
    {
      Iterator localIterator = paramCollection.iterator();
      StringBuilder localStringBuilder1 = new StringBuilder();
      while (localIterator.hasNext())
      {
        Object localObject2 = localIterator.next();
        StringBuilder localStringBuilder2 = localStringBuilder1.append(localObject2).append(',');
      }
      localStringBuilder3 = new StringBuilder().append("MusicId NOT IN (");
      int i = localStringBuilder1.length() + -1;
      str1 = localStringBuilder1.substring(0, i);
    }
    for (String str2 = str1 + ")"; ; str2 = null)
    {
      String str3 = "";
      if (!paramBoolean)
        str3 = "(LocalCopyType <> 200)";
      if (str2 != null)
      {
        if (str3.length() > 0)
          str3 = str3 + " AND ";
        str3 = str3 + "(" + str2 + ")";
      }
      if (str3.length() == 0)
        str3 = null;
      SQLiteDatabase localSQLiteDatabase = beginRead();
      String[] arrayOfString1 = null;
      try
      {
        String[] arrayOfString2 = paramArrayOfString;
        String str4 = paramString;
        localObject1 = ((SQLiteQueryBuilder)localObject1).query(localSQLiteDatabase, arrayOfString2, str3, arrayOfString1, "MusicId", null, "MUSIC.AlbumArtistId ASC, MUSIC.AlbumId ASC, MUSIC.TrackNumber ASC", str4);
        if (localObject1 != null)
          int j = ((Cursor)localObject1).getCount();
        return localObject1;
      }
      finally
      {
        endRead(localSQLiteDatabase);
      }
    }
  }

  public Cursor getKeeponAutoListInfo(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    String[] arrayOfString1 = new String[4];
    arrayOfString1[0] = "KeepOnId";
    arrayOfString1[1] = "SongCount";
    arrayOfString1[2] = "DownloadedSongCount";
    arrayOfString1[3] = "DateAdded";
    try
    {
      String[] arrayOfString2 = new String[1];
      String str = Long.toString(paramLong);
      arrayOfString2[0] = str;
      Cursor localCursor1 = localSQLiteDatabase.query(true, "KEEPON", arrayOfString1, "AutoListId=?", arrayOfString2, null, null, null, null);
      Cursor localCursor2 = localCursor1;
      return localCursor2;
    }
    finally
    {
      endRead(localSQLiteDatabase);
    }
  }

  public Cursor getLeastRecentlyUsedCacheFiles(int paramInt)
  {
    String[] arrayOfString1 = new String[1];
    String str1 = Integer.toString(100);
    arrayOfString1[0] = str1;
    if (!PostFroyoUtils.EnvironmentCompat.isExternalStorageEmulated())
    {
      arrayOfString1 = new String[2];
      String str2 = Integer.toString(100);
      arrayOfString1[0] = str2;
      String str3 = Integer.toString(paramInt);
      arrayOfString1[1] = str3;
    }
    for (String str4 = " AND LocalCopyStorageType=?"; ; str4 = "")
    {
      SQLiteDatabase localSQLiteDatabase = beginRead();
      while (true)
      {
        try
        {
          String[] arrayOfString2 = new String[3];
          arrayOfString2[0] = "Id";
          arrayOfString2[1] = "LocalCopyPath";
          arrayOfString2[2] = "LocalCopyStorageType";
          String str5 = "LocalCopyType=?" + str4;
          Cursor localCursor1 = localSQLiteDatabase.query("MUSIC", arrayOfString2, str5, arrayOfString1, null, null, "CacheDate", "10");
          if (localCursor1 != null)
          {
            int i = localCursor1.getCount();
            endRead(localSQLiteDatabase);
            localCursor2 = localCursor1;
            return localCursor2;
          }
        }
        finally
        {
          endRead(localSQLiteDatabase);
        }
        endRead(localSQLiteDatabase);
        Cursor localCursor2 = null;
      }
    }
  }

  public long getMostRecentPlayDate()
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "LastPlayDate";
    Store localStore = this;
    String[] arrayOfString2 = null;
    String str1 = null;
    String str2 = null;
    Cursor localCursor = localStore.executeQuery(false, "MUSIC", arrayOfString1, null, arrayOfString2, str1, str2, "LastPlayDate DESC", "1");
    if (localCursor != null);
    try
    {
      if (localCursor.moveToFirst())
      {
        long l1 = localCursor.getLong(0);
        l2 = l1;
        return l2;
      }
      long l2 = 0L;
    }
    finally
    {
      safeClose(localCursor);
    }
  }

  // ERROR //
  public long getMusicIdForSystemMediaStoreId(long paramLong, boolean paramBoolean)
    throws FileNotFoundException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 651	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   4: astore 4
    //   6: iconst_1
    //   7: anewarray 49	java/lang/String
    //   10: astore 5
    //   12: aload 5
    //   14: iconst_0
    //   15: ldc_w 635
    //   18: aastore
    //   19: iconst_2
    //   20: anewarray 49	java/lang/String
    //   23: astore 6
    //   25: iconst_0
    //   26: invokestatic 53	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   29: astore 7
    //   31: aload 6
    //   33: iconst_0
    //   34: aload 7
    //   36: aastore
    //   37: lload_1
    //   38: invokestatic 783	java/lang/String:valueOf	(J)Ljava/lang/String;
    //   41: astore 8
    //   43: aload 6
    //   45: iconst_1
    //   46: aload 8
    //   48: aastore
    //   49: aload 4
    //   51: ldc_w 362
    //   54: aload 5
    //   56: ldc_w 1754
    //   59: aload 6
    //   61: aconst_null
    //   62: aconst_null
    //   63: aconst_null
    //   64: invokevirtual 368	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   67: lstore 9
    //   69: lload 9
    //   71: lstore 11
    //   73: lload 11
    //   75: ifnull +156 -> 231
    //   78: lload 11
    //   80: invokeinterface 374 1 0
    //   85: ifeq +146 -> 231
    //   88: lload 11
    //   90: iconst_0
    //   91: invokeinterface 626 2 0
    //   96: lstore 9
    //   98: lload 9
    //   100: lstore 13
    //   102: lload 11
    //   104: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   107: aload_0
    //   108: aload 4
    //   110: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   113: lload 13
    //   115: ldc2_w 198
    //   118: lcmp
    //   119: ifne +96 -> 215
    //   122: iload_3
    //   123: ifeq +54 -> 177
    //   126: aload_0
    //   127: getfield 129	com/google/android/music/store/Store:mMediaStoreImporter	Lcom/google/android/music/store/MediaStoreImporter;
    //   130: astore 15
    //   132: aload_0
    //   133: getfield 145	com/google/android/music/store/Store:mContext	Landroid/content/Context;
    //   136: astore 16
    //   138: aload 15
    //   140: aload 16
    //   142: invokevirtual 1758	com/google/android/music/store/MediaStoreImporter:doImport	(Landroid/content/Context;)Z
    //   145: istore 17
    //   147: aload_0
    //   148: lload_1
    //   149: iconst_0
    //   150: invokevirtual 1760	com/google/android/music/store/Store:getMusicIdForSystemMediaStoreId	(JZ)J
    //   153: lstore 18
    //   155: lload 18
    //   157: lreturn
    //   158: astore 20
    //   160: iconst_0
    //   161: lstore 21
    //   163: lload 21
    //   165: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   168: aload_0
    //   169: aload 4
    //   171: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   174: aload 20
    //   176: athrow
    //   177: new 250	java/lang/StringBuilder
    //   180: dup
    //   181: invokespecial 251	java/lang/StringBuilder:<init>	()V
    //   184: ldc_w 790
    //   187: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   190: lload_1
    //   191: invokevirtual 428	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   194: ldc_w 1762
    //   197: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   200: invokevirtual 265	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   203: astore 23
    //   205: new 675	java/io/FileNotFoundException
    //   208: dup
    //   209: aload 23
    //   211: invokespecial 682	java/io/FileNotFoundException:<init>	(Ljava/lang/String;)V
    //   214: athrow
    //   215: lload 13
    //   217: lstore 18
    //   219: goto -64 -> 155
    //   222: astore 20
    //   224: lload 11
    //   226: lstore 21
    //   228: goto -65 -> 163
    //   231: ldc2_w 198
    //   234: lstore 13
    //   236: goto -134 -> 102
    //
    // Exception table:
    //   from	to	target	type
    //   6	69	158	finally
    //   78	98	222	finally
  }

  public Cursor getNeedToKeepOn(String paramString, String[] paramArrayOfString, Collection<Long> paramCollection)
  {
    return getKeepOn(paramString, paramArrayOfString, paramCollection, false);
  }

  public ContentIdentifier[] getNextKeeponToDownload(int paramInt, Collection<Long> paramCollection)
  {
    ContentIdentifier[] arrayOfContentIdentifier = null;
    String str = Integer.toString(paramInt);
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "_id";
    Cursor localCursor = getNeedToKeepOn(str, arrayOfString, paramCollection);
    if (localCursor == null);
    while (true)
    {
      return arrayOfContentIdentifier;
      try
      {
        if (localCursor.moveToFirst())
        {
          long l = localCursor.getLong(0);
          arrayOfContentIdentifier = new ContentIdentifier[1];
          ContentIdentifier.Domain localDomain = ContentIdentifier.Domain.DEFAULT;
          ContentIdentifier localContentIdentifier = new ContentIdentifier(l, localDomain);
          arrayOfContentIdentifier[0] = localContentIdentifier;
        }
        localCursor.close();
      }
      finally
      {
        localCursor.close();
      }
    }
  }

  int getPersistentNautilusTracksCount(SQLiteDatabase paramSQLiteDatabase)
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "count(1)";
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    Cursor localCursor = localSQLiteDatabase.query("MUSIC", arrayOfString, "TrackType=5", null, str1, str2, str3);
    if (localCursor != null);
    try
    {
      if (localCursor.moveToFirst())
      {
        int i = localCursor.getInt(0);
        int j = i;
        return j;
      }
      throw new IllegalStateException("Failed to count nautilus tracks");
    }
    finally
    {
      safeClose(localCursor);
    }
  }

  public Pair<Long, Integer> getPlayQueueLastGroupInfo()
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      Pair localPair1 = PlayQueue.getInstance().getLastGroupInfo(localSQLiteDatabase, 0);
      Pair localPair2 = localPair1;
      return localPair2;
    }
    finally
    {
      endRead(localSQLiteDatabase);
    }
  }

  public PlayList getPlayQueuePlaylist()
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    PlayList localPlayList;
    try
    {
      long l = PlayQueue.getInstance().getListId();
      localPlayList = PlayList.readPlayList(localSQLiteDatabase, l, null);
      if (localPlayList == null)
      {
        String str = "PlayQueue playlist is not found. Id:" + l;
        throw new IllegalStateException(str);
      }
    }
    finally
    {
      endRead(localSQLiteDatabase);
    }
    endRead(localSQLiteDatabase);
    return localPlayList;
  }

  public long getPlayQueuePlaylistId()
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      long l1 = PlayQueue.getInstance().getListId();
      long l2 = l1;
      return l2;
    }
    finally
    {
      endRead(localSQLiteDatabase);
    }
  }

  public ContentIdentifier getPreferredMusicId(ContentIdentifier paramContentIdentifier, int paramInt)
    throws FileNotFoundException
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      long l1 = paramContentIdentifier.getId();
      long l2 = getSongId(localSQLiteDatabase, l1);
      Object localObject1;
      if (paramContentIdentifier.isDefaultDomain())
      {
        localObject1 = new int[1];
        localObject1[0] = 0;
      }
      while (true)
      {
        localObject1 = getPreferredMusicIdForSongId(localSQLiteDatabase, l2, (int[])localObject1, paramInt);
        if ((LOGV) && (!paramContentIdentifier.equals((ContentIdentifier)localObject1)))
        {
          String str = "Using preferred file id " + localObject1 + " for file id " + paramContentIdentifier;
          Log.i("MusicStore", str);
        }
        return localObject1;
        localObject1 = null;
        localObject1 = new int[localObject1];
        localObject1[0] = 0;
        int i = paramContentIdentifier.getDomain().getDBValue();
        localObject1[1] = i;
      }
    }
    finally
    {
      endRead(localSQLiteDatabase);
    }
  }

  public long getRadioLocalIdBySeedAndType(String paramString, int paramInt)
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    int i = 1;
    try
    {
      String[] arrayOfString1 = new String[1];
      arrayOfString1[0] = "Id";
      String[] arrayOfString2 = new String[2];
      arrayOfString2[0] = paramString;
      String str = String.valueOf(paramInt);
      arrayOfString2[1] = str;
      long l1 = localSQLiteDatabase.query("RADIO_STATIONS", arrayOfString1, "SeedSourceId =? AND SeedSourceType =? ", arrayOfString2, null, null, null);
      l3 = l1;
      if (l3 == null);
    }
    finally
    {
      try
      {
        boolean bool = l3.moveToFirst();
        if (!bool)
        {
          safeClose(l3);
          endRead(localSQLiteDatabase);
        }
        long l5;
        for (long l4 = 65535L; ; l4 = l5)
        {
          return l4;
          i = 0;
          long l2 = l3.getLong(i);
          l5 = l2;
          safeClose(l3);
          endRead(localSQLiteDatabase);
        }
        localObject1 = finally;
        l6 = 0;
        safeClose(l6);
        endRead(localSQLiteDatabase);
        throw localObject1;
      }
      finally
      {
        while (true)
        {
          long l3;
          long l6 = l3;
        }
      }
    }
  }

  public String getRadioRemoteId(long paramLong)
    throws FileNotFoundException
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    int i = 1;
    try
    {
      String[] arrayOfString1 = new String[1];
      arrayOfString1[0] = "SourceId";
      String[] arrayOfString2 = new String[1];
      String str1 = Long.toString(paramLong);
      arrayOfString2[0] = str1;
      Object localObject1 = localSQLiteDatabase.query("RADIO_STATIONS", arrayOfString1, "Id =? ", arrayOfString2, null, null, null);
      localObject2 = localObject1;
      if (localObject2 != null);
      try
      {
        if (localObject2.moveToFirst())
          break label124;
        String str2 = "Could not find source id  for radio id: " + paramLong;
        throw new FileNotFoundException(str2);
      }
      finally
      {
      }
      safeClose(localObject2);
      endRead(localSQLiteDatabase);
      throw localObject3;
      label124: i = 0;
      localObject1 = localObject2.getString(i);
      Object localObject5 = localObject1;
      safeClose(localObject2);
      endRead(localSQLiteDatabase);
      return localObject5;
    }
    finally
    {
      while (true)
        Object localObject2 = null;
    }
  }

  public int getRating(long paramLong)
    throws FileNotFoundException
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      String[] arrayOfString1 = new String[1];
      arrayOfString1[0] = "Rating";
      String[] arrayOfString2 = new String[1];
      String str1 = Long.toString(paramLong);
      arrayOfString2[0] = str1;
      int i = localSQLiteDatabase.query("MUSIC", arrayOfString1, "MUSIC.Id=?", arrayOfString2, null, null, null);
      int k = i;
      if (k != 0);
      try
      {
        if (k.moveToNext())
        {
          int j = k.getInt(0);
          int n = j;
          safeClose(k);
          endRead(localSQLiteDatabase);
          return n;
        }
        String str2 = "Song with id " + paramLong + " was not found in the store.";
        throw new FileNotFoundException(str2);
      }
      finally
      {
      }
      safeClose(k);
      endRead(localSQLiteDatabase);
      throw localObject1;
    }
    finally
    {
      int m = 0;
    }
  }

  public Cursor getRecentsJoinedWithArtwork(String[] paramArrayOfString)
  {
    SQLiteQueryBuilder localSQLiteQueryBuilder = new SQLiteQueryBuilder();
    localSQLiteQueryBuilder.setTables("RECENT LEFT JOIN MUSIC ON (RecentAlbumId=MUSIC.AlbumId)  LEFT JOIN LISTS ON (RecentListId=LISTS.Id)  LEFT JOIN ARTWORK ON (RECENT.RecentAlbumId = ARTWORK.AlbumId)");
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      String[] arrayOfString = paramArrayOfString;
      Cursor localCursor1 = localSQLiteQueryBuilder.query(localSQLiteDatabase, arrayOfString, "LISTS.Id NOT NULL OR MUSIC.AlbumId NOT NULL", null, "RecentId", null, "ItemDate DESC");
      Cursor localCursor2 = localCursor1;
      return localCursor2;
    }
    finally
    {
      endRead(localSQLiteDatabase);
    }
  }

  public String getRemoteArtLocationForAlbum(long paramLong)
  {
    Object localObject1 = null;
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "AlbumArtLocation";
    String[] arrayOfString2 = new String[1];
    String str1 = Long.toString(paramLong);
    arrayOfString2[0] = str1;
    Store localStore = this;
    Object localObject2 = localObject1;
    Object localObject3 = localObject1;
    Cursor localCursor = localStore.executeQuery(false, "MUSIC", arrayOfString1, "AlbumId=? AND MUSIC.SourceAccount!=0 AND AlbumArtLocation IS NOT NULL", arrayOfString2, (String)localObject1, localObject2, localObject3, "1");
    if (localCursor != null);
    try
    {
      if (localCursor.moveToNext())
      {
        String str2 = localCursor.getString(0);
        localObject1 = str2;
      }
      return localObject1;
    }
    finally
    {
      safeClose(localCursor);
    }
  }

  public MusicRingtoneManager getRingtoneManager()
  {
    return this.mRingtoneManager;
  }

  public MixTrackId getServerTrackId(long paramLong)
    throws FileNotFoundException
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    int i = 2;
    try
    {
      String[] arrayOfString1 = new String[2];
      arrayOfString1[0] = "SourceId";
      arrayOfString1[1] = "SourceType";
      String[] arrayOfString2 = new String[1];
      String str1 = Long.toString(paramLong);
      arrayOfString2[0] = str1;
      Cursor localCursor1 = localSQLiteDatabase.query("MUSIC", arrayOfString1, "(SongId = (SELECT SongId     FROM MUSIC     WHERE MUSIC.Id = ?)) AND (MUSIC.SourceAccount != 0)", arrayOfString2, null, null, null);
      localCursor2 = localCursor1;
      if (localCursor2 != null);
      try
      {
        if (localCursor2.moveToFirst())
          break label131;
        String str2 = "Could not find source id  for music id: " + paramLong;
        throw new FileNotFoundException(str2);
      }
      finally
      {
      }
      safeClose(localCursor2);
      endRead(localSQLiteDatabase);
      throw localObject1;
      label131: i = 1;
      i = localCursor2.getInt(i);
      MixTrackId localMixTrackId = MixTrackId.createTrackId(localCursor2.getString(0), i);
      if (localMixTrackId == null)
      {
        Object[] arrayOfObject = new Object[2];
        Long localLong = Long.valueOf(paramLong);
        arrayOfObject[0] = localLong;
        Integer localInteger = Integer.valueOf(i);
        arrayOfObject[1] = localInteger;
        String str3 = String.format("Could not create server track id for musicId=%s sourceType=%s", arrayOfObject);
        throw new FileNotFoundException(str3);
      }
      safeClose(localCursor2);
      endRead(localSQLiteDatabase);
      return localMixTrackId;
    }
    finally
    {
      while (true)
        Cursor localCursor2 = null;
    }
  }

  // ERROR //
  public List<MixTrackId> getServerTrackIds(List<ContentIdentifier> paramList)
  {
    // Byte code:
    //   0: iconst_1
    //   1: istore_2
    //   2: aconst_null
    //   3: astore_3
    //   4: new 686	java/util/LinkedList
    //   7: dup
    //   8: invokespecial 687	java/util/LinkedList:<init>	()V
    //   11: astore 4
    //   13: aload_1
    //   14: ifnull +12 -> 26
    //   17: aload_1
    //   18: invokeinterface 1077 1 0
    //   23: ifne +10 -> 33
    //   26: aload 4
    //   28: astore 5
    //   30: aload 5
    //   32: areturn
    //   33: new 250	java/lang/StringBuilder
    //   36: dup
    //   37: invokespecial 251	java/lang/StringBuilder:<init>	()V
    //   40: astore 6
    //   42: aload_1
    //   43: iconst_0
    //   44: invokeinterface 1856 2 0
    //   49: checkcast 760	com/google/android/music/download/ContentIdentifier
    //   52: invokevirtual 1788	com/google/android/music/download/ContentIdentifier:getId	()J
    //   55: lstore 7
    //   57: aload 6
    //   59: lload 7
    //   61: invokevirtual 428	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   64: astore 9
    //   66: aload_1
    //   67: invokeinterface 1077 1 0
    //   72: istore 10
    //   74: iload_2
    //   75: iload 10
    //   77: if_icmpge +44 -> 121
    //   80: aload 6
    //   82: ldc_w 1858
    //   85: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   88: astore 11
    //   90: aload_1
    //   91: iload_2
    //   92: invokeinterface 1856 2 0
    //   97: checkcast 760	com/google/android/music/download/ContentIdentifier
    //   100: invokevirtual 1788	com/google/android/music/download/ContentIdentifier:getId	()J
    //   103: lstore 12
    //   105: aload 6
    //   107: lload 12
    //   109: invokevirtual 428	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   112: astore 14
    //   114: iload_2
    //   115: iconst_1
    //   116: iadd
    //   117: istore_2
    //   118: goto -52 -> 66
    //   121: aload_0
    //   122: invokevirtual 651	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   125: astore 15
    //   127: iconst_2
    //   128: istore 16
    //   130: iload 16
    //   132: anewarray 49	java/lang/String
    //   135: astore 17
    //   137: aload 17
    //   139: iconst_0
    //   140: ldc_w 655
    //   143: aastore
    //   144: aload 17
    //   146: iconst_1
    //   147: ldc_w 794
    //   150: aastore
    //   151: new 250	java/lang/StringBuilder
    //   154: dup
    //   155: invokespecial 251	java/lang/StringBuilder:<init>	()V
    //   158: ldc_w 1860
    //   161: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   164: astore 18
    //   166: aload 6
    //   168: invokevirtual 265	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   171: astore 19
    //   173: aload 18
    //   175: aload 19
    //   177: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   180: ldc_w 1862
    //   183: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: ldc_w 1864
    //   189: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   192: ldc_w 713
    //   195: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   198: ldc_w 1866
    //   201: invokevirtual 257	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   204: invokevirtual 265	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   207: astore 20
    //   209: aload 15
    //   211: ldc_w 362
    //   214: aload 17
    //   216: aload 20
    //   218: aconst_null
    //   219: aconst_null
    //   220: aconst_null
    //   221: aconst_null
    //   222: invokevirtual 368	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   225: astore 21
    //   227: aload 21
    //   229: astore_3
    //   230: aload_3
    //   231: ifnull +124 -> 355
    //   234: aload_3
    //   235: invokeinterface 622 1 0
    //   240: ifeq +115 -> 355
    //   243: aload_3
    //   244: iconst_1
    //   245: invokeinterface 378 2 0
    //   250: istore 16
    //   252: aload_3
    //   253: iconst_0
    //   254: invokeinterface 412 2 0
    //   259: iload 16
    //   261: invokestatic 1849	com/google/android/music/cloudclient/MixTrackId:createTrackId	(Ljava/lang/String;I)Lcom/google/android/music/cloudclient/MixTrackId;
    //   264: astore 6
    //   266: aload 6
    //   268: ifnonnull +73 -> 341
    //   271: iconst_2
    //   272: anewarray 339	java/lang/Object
    //   275: astore 22
    //   277: aload_3
    //   278: iconst_0
    //   279: invokeinterface 412 2 0
    //   284: astore 23
    //   286: aload 22
    //   288: iconst_0
    //   289: aload 23
    //   291: aastore
    //   292: iload 16
    //   294: invokestatic 670	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   297: astore 24
    //   299: aload 22
    //   301: iconst_1
    //   302: aload 24
    //   304: aastore
    //   305: ldc_w 1851
    //   308: aload 22
    //   310: invokestatic 804	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   313: astore 25
    //   315: ldc_w 267
    //   318: aload 25
    //   320: invokestatic 273	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;)V
    //   323: goto -93 -> 230
    //   326: astore 26
    //   328: aload_3
    //   329: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   332: aload_0
    //   333: aload 15
    //   335: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   338: aload 26
    //   340: athrow
    //   341: aload 4
    //   343: aload 6
    //   345: invokeinterface 671 2 0
    //   350: istore 27
    //   352: goto -122 -> 230
    //   355: aload_3
    //   356: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   359: aload_0
    //   360: aload 15
    //   362: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   365: aload 4
    //   367: astore 5
    //   369: goto -339 -> 30
    //   372: astore 26
    //   374: aconst_null
    //   375: astore_3
    //   376: goto -48 -> 328
    //
    // Exception table:
    //   from	to	target	type
    //   234	323	326	finally
    //   341	352	326	finally
    //   130	227	372	finally
  }

  // ERROR //
  String getShareTokenIfSharedPlaylist(long paramLong)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_3
    //   2: aload_0
    //   3: invokevirtual 651	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   6: astore 4
    //   8: iconst_2
    //   9: anewarray 49	java/lang/String
    //   12: astore 5
    //   14: lload_1
    //   15: invokestatic 783	java/lang/String:valueOf	(J)Ljava/lang/String;
    //   18: astore 6
    //   20: aload 5
    //   22: iconst_0
    //   23: aload 6
    //   25: aastore
    //   26: bipush 71
    //   28: invokestatic 53	java/lang/String:valueOf	(I)Ljava/lang/String;
    //   31: astore 7
    //   33: aload 5
    //   35: iconst_1
    //   36: aload 7
    //   38: aastore
    //   39: iconst_1
    //   40: anewarray 49	java/lang/String
    //   43: astore 8
    //   45: aload 8
    //   47: iconst_0
    //   48: ldc_w 1869
    //   51: aastore
    //   52: aload 4
    //   54: ldc_w 637
    //   57: aload 8
    //   59: ldc_w 1871
    //   62: aload 5
    //   64: aconst_null
    //   65: aconst_null
    //   66: aconst_null
    //   67: aconst_null
    //   68: invokevirtual 642	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   71: astore 9
    //   73: aload 9
    //   75: astore 10
    //   77: aload 10
    //   79: ifnull +26 -> 105
    //   82: aload 10
    //   84: invokeinterface 374 1 0
    //   89: ifeq +16 -> 105
    //   92: aload 10
    //   94: iconst_0
    //   95: invokeinterface 412 2 0
    //   100: astore 9
    //   102: aload 9
    //   104: astore_3
    //   105: aload_0
    //   106: aload 4
    //   108: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   111: aload 10
    //   113: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   116: aload_3
    //   117: areturn
    //   118: astore 11
    //   120: aconst_null
    //   121: astore 10
    //   123: aload_0
    //   124: aload 4
    //   126: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   129: aload 10
    //   131: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   134: aload 11
    //   136: athrow
    //   137: astore 11
    //   139: goto -16 -> 123
    //
    // Exception table:
    //   from	to	target	type
    //   39	73	118	finally
    //   82	102	137	finally
  }

  long getSharedEntryLastUpdateTimeMs(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    long l = 0L;
    try
    {
      String[] arrayOfString = new String[1];
      String str = String.valueOf(paramLong);
      arrayOfString[0] = str;
      localCursor = localSQLiteDatabase.rawQuery("SELECT max(_sync_version) AS MAX_TIME FROM LISTITEMS  WHERE ListId=?", arrayOfString);
      if ((localCursor != null) && (localCursor.moveToFirst()))
        l = localCursor.getLong(0) / 1000L;
      return l;
    }
    finally
    {
      Cursor localCursor;
      endRead(localSQLiteDatabase);
      safeClose(localCursor);
    }
  }

  public long getSizeAlbum(long paramLong)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "sum(Size)";
    String[] arrayOfString2 = new String[1];
    String str1 = Long.toString(paramLong);
    arrayOfString2[0] = str1;
    Store localStore = this;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    Cursor localCursor = localStore.executeQuery(false, "MUSIC", arrayOfString1, "AlbumId= ? AND LocalCopyType != 300", arrayOfString2, null, str2, str3, str4);
    if (localCursor != null);
    try
    {
      if (localCursor.moveToFirst())
      {
        long l1 = localCursor.getLong(0);
        l2 = l1;
        return l2;
      }
      long l2 = 0L;
    }
    finally
    {
      safeClose(localCursor);
    }
  }

  public long getSizeAutoPlaylist(long paramLong)
  {
    long l1 = 0L;
    String str1;
    if (paramLong == 65532L)
      str1 = "MUSIC.SourceAccount!=0 AND (Rating > 3) AND LocalCopyType != 300";
    while (true)
    {
      if (str1 == null)
      {
        String str2 = "Failed to create query for auto list id: " + paramLong;
        Log.w("MusicStore", str2);
      }
      while (true)
      {
        return l1;
        if (paramLong == 65535L)
        {
          str1 = "MUSIC.SourceAccount!=0 AND SongId IN (SELECT SongId FROM MUSIC WHERE +Domain=0 ORDER BY MUSIC.FileDate DESC  LIMIT 500) AND LocalCopyType != 300";
          break;
        }
        if (paramLong == 65533L)
        {
          str1 = "MUSIC.SourceAccount!=0 AND (TrackType IN (2,3,1) AND +Domain=0) AND LocalCopyType != 300";
          break;
        }
        Cursor localCursor;
        if (paramLong == 65534L)
        {
          str1 = "MUSIC.SourceAccount!=0 AND +Domain=0 AND LocalCopyType != 300";
          break;
          String[] arrayOfString = new String[1];
          arrayOfString[0] = "sum(Size)";
          Store localStore = this;
          String str3 = null;
          String str4 = null;
          String str5 = null;
          String str6 = null;
          localCursor = localStore.executeQuery(false, "MUSIC", arrayOfString, str1, null, str3, str4, str5, str6);
          if (localCursor == null);
        }
        try
        {
          long l3;
          if (localCursor.moveToFirst())
          {
            long l2 = localCursor.getLong(0);
            l3 = l2;
            safeClose(localCursor);
            l1 = l3;
          }
          else
          {
            l3 = 0L;
          }
        }
        finally
        {
          safeClose(localCursor);
        }
      }
    }
  }

  public long getSizeOfUndownloadedKeepOnFiles()
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "Size";
    Cursor localCursor = getNeedToKeepOn(null, arrayOfString, null);
    long l1 = 0L;
    try
    {
      if (localCursor.moveToNext())
      {
        long l2 = localCursor.getLong(0);
        long l3 = l2;
        l1 += l3;
      }
    }
    finally
    {
      safeClose(localCursor);
    }
    return l1;
  }

  public long getSizePlaylist(long paramLong)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "sum(Size)";
    String[] arrayOfString2 = new String[1];
    String str1 = Long.toString(paramLong);
    arrayOfString2[0] = str1;
    Store localStore = this;
    String str2 = null;
    String str3 = null;
    Cursor localCursor = localStore.executeQuery(false, "LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id) ", arrayOfString1, "LISTITEMS.ListId=? AND LocalCopyType != 300", arrayOfString2, "MUSIC.Id", null, str2, str3);
    if (localCursor != null);
    try
    {
      if (localCursor.moveToFirst())
      {
        long l1 = localCursor.getLong(0);
        l2 = l1;
        return l2;
      }
      long l2 = 0L;
    }
    finally
    {
      safeClose(localCursor);
    }
  }

  long getSongId(long paramLong)
    throws FileNotFoundException
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      long l1 = getSongId(localSQLiteDatabase, paramLong);
      long l2 = l1;
      return l2;
    }
    finally
    {
      endRead(localSQLiteDatabase);
    }
  }

  public long getTotalCachedSize(int paramInt)
  {
    long l1;
    if ((paramInt == 0) || (paramInt == 300))
    {
      l1 = 0L;
      label15: return l1;
    }
    SQLiteDatabase localSQLiteDatabase = beginRead();
    boolean bool = true;
    try
    {
      String[] arrayOfString1 = new String[1];
      arrayOfString1[0] = "sum(LocalCopySize)";
      String[] arrayOfString2 = new String[1];
      String str = Integer.toString(paramInt);
      arrayOfString2[0] = str;
      l2 = localSQLiteDatabase.query(bool, "MUSIC", arrayOfString1, "LocalCopyType=?", arrayOfString2, null, null, null, null);
      l3 = l2;
    }
    finally
    {
      try
      {
        long l2;
        if (l3.moveToNext())
        {
          l2 = l3.getLong(0);
          long l4 = l2;
          safeClose(l3);
          endRead(localSQLiteDatabase);
          l1 = l4;
          break label15;
        }
        safeClose(l3);
        endRead(localSQLiteDatabase);
        l1 = 0L;
        break label15;
        localObject1 = finally;
        l5 = 0;
        safeClose(l5);
        endRead(localSQLiteDatabase);
        throw localObject1;
      }
      finally
      {
        long l3;
        long l5 = l3;
      }
    }
  }

  public Pair<Integer, Long> getTotalNeedToKeepOn()
  {
    Object localObject1 = null;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "Size";
    Cursor localCursor = getNeedToKeepOn((String)localObject1, arrayOfString, (Collection)localObject1);
    if (localCursor == null);
    while (true)
    {
      return localObject1;
      long l1 = 0L;
      try
      {
        while (localCursor.moveToNext())
        {
          long l2 = localCursor.getLong(0);
          l1 += l2;
        }
        Integer localInteger = Integer.valueOf(localCursor.getCount());
        Long localLong = Long.valueOf(l1);
        Pair localPair = Pair.create(localInteger, localLong);
        localObject1 = localPair;
        localCursor.close();
      }
      finally
      {
        localCursor.close();
      }
    }
  }

  public boolean hasRemoteSongs(int paramInt)
  {
    boolean bool1 = true;
    String[] arrayOfString = new String[bool1];
    String str = Integer.toString(paramInt);
    arrayOfString[0] = str;
    Cursor localCursor = executeRawQuery("SELECT 1 FROM MUSIC WHERE MUSIC.SourceAccount!=0 LIMIT ?,1", arrayOfString);
    if (localCursor != null);
    try
    {
      boolean bool2 = localCursor.moveToFirst();
      if (bool2)
        return bool1;
      bool1 = false;
    }
    finally
    {
      safeClose(localCursor);
    }
  }

  public void importMediaStore(Context paramContext)
  {
    boolean bool = this.mMediaStoreImporter.doImport(paramContext);
  }

  public List<Long> insertSongs(ExternalSongList paramExternalSongList, Account paramAccount, boolean paramBoolean)
  {
    if (paramExternalSongList.isDefaultDomain())
      throw new IllegalArgumentException("Default domains should not be added to the library");
    if (paramAccount == null)
      throw new IllegalArgumentException("Streaming account must not be null");
    int i = computeAccountHash(paramAccount);
    Object localObject1;
    if ((paramExternalSongList instanceof TracksSongList))
    {
      paramExternalSongList = (TracksSongList)paramExternalSongList;
      localObject1 = beginWriteTxn();
    }
    while (true)
    {
      try
      {
        List localList1 = paramExternalSongList.getTracks();
        List localList2 = tryToInsertOrUpdateExternalSongs((SQLiteDatabase)localObject1, i, localList1, paramBoolean);
        localObject2 = localList2;
        endWriteTxn((SQLiteDatabase)localObject1, true);
        return localObject2;
      }
      finally
      {
        endWriteTxn((SQLiteDatabase)localObject1, false);
      }
      Object localObject2 = Lists.newLinkedList();
      TagNormalizer localTagNormalizer = new TagNormalizer();
      MusicFile localMusicFile1 = new MusicFile();
      localMusicFile1.setTagNormalizer(localTagNormalizer);
      SQLiteProgram localSQLiteProgram = null;
      SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
      try
      {
        localObject1 = MusicFile.compileMusicInsertStatement(localSQLiteDatabase);
        MusicFile localMusicFile2 = localMusicFile1;
        int j = 0;
        int k = paramExternalSongList.getItemCount();
        if (j < k)
        {
          localMusicFile2.reset();
          localMusicFile2 = paramExternalSongList.getMusicFile(j, localMusicFile2);
          localMusicFile2.setSourceAccount(i);
          Long localLong = Long.valueOf(localMusicFile2.insertMusicFile((SQLiteStatement)localObject1));
          boolean bool = ((List)localObject2).add(localLong);
          j += 1;
        }
      }
      finally
      {
        safeClose(localSQLiteProgram);
        endWriteTxn(localSQLiteDatabase, false);
      }
    }
  }

  void invalidateMediaStoreImport(Context paramContext)
  {
    this.mMediaStoreImporter.invalidateMediaStoreImport(paramContext);
  }

  // ERROR //
  public boolean isAlbumSelectedAsKeepOn(long paramLong, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 651	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   4: astore 4
    //   6: aconst_null
    //   7: astore 5
    //   9: iconst_1
    //   10: istore 6
    //   12: iconst_1
    //   13: anewarray 49	java/lang/String
    //   16: astore 7
    //   18: lload_1
    //   19: invokestatic 750	java/lang/Long:toString	(J)Ljava/lang/String;
    //   22: astore 8
    //   24: aload 7
    //   26: iconst_0
    //   27: aload 8
    //   29: aastore
    //   30: iconst_1
    //   31: anewarray 49	java/lang/String
    //   34: astore 9
    //   36: aload 9
    //   38: iconst_0
    //   39: ldc_w 1725
    //   42: aastore
    //   43: aload 4
    //   45: iconst_1
    //   46: ldc_w 556
    //   49: aload 9
    //   51: ldc_w 1559
    //   54: aload 7
    //   56: aconst_null
    //   57: aconst_null
    //   58: aconst_null
    //   59: aconst_null
    //   60: invokevirtual 1051	android/database/sqlite/SQLiteDatabase:query	(ZLjava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   63: astore 10
    //   65: aload 10
    //   67: astore 5
    //   69: aload 5
    //   71: invokeinterface 622 1 0
    //   76: istore 10
    //   78: iload 10
    //   80: ifeq +20 -> 100
    //   83: aload 5
    //   85: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   88: aload_0
    //   89: aload 4
    //   91: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   94: iconst_1
    //   95: istore 11
    //   97: iload 11
    //   99: ireturn
    //   100: aload 5
    //   102: invokeinterface 986 1 0
    //   107: iload_3
    //   108: ifne +20 -> 128
    //   111: aload 5
    //   113: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   116: aload_0
    //   117: aload 4
    //   119: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   122: iconst_0
    //   123: istore 11
    //   125: goto -28 -> 97
    //   128: iconst_1
    //   129: istore 6
    //   131: iconst_1
    //   132: anewarray 49	java/lang/String
    //   135: astore 12
    //   137: aload 12
    //   139: iconst_0
    //   140: ldc_w 1517
    //   143: aastore
    //   144: aload 4
    //   146: iload 6
    //   148: ldc_w 1941
    //   151: aload 12
    //   153: ldc_w 1943
    //   156: aload 7
    //   158: aconst_null
    //   159: aconst_null
    //   160: aconst_null
    //   161: aconst_null
    //   162: invokevirtual 1051	android/database/sqlite/SQLiteDatabase:query	(ZLjava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   165: astore 10
    //   167: aload 10
    //   169: astore 13
    //   171: aload 13
    //   173: invokeinterface 622 1 0
    //   178: istore 10
    //   180: iload 10
    //   182: istore 14
    //   184: aload 13
    //   186: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   189: aload_0
    //   190: aload 4
    //   192: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   195: iload 14
    //   197: istore 11
    //   199: goto -102 -> 97
    //   202: astore 15
    //   204: aconst_null
    //   205: astore 13
    //   207: aload 13
    //   209: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   212: aload_0
    //   213: aload 4
    //   215: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   218: aload 15
    //   220: athrow
    //   221: astore 15
    //   223: aload 5
    //   225: astore 13
    //   227: goto -20 -> 207
    //   230: astore 15
    //   232: goto -25 -> 207
    //
    // Exception table:
    //   from	to	target	type
    //   12	65	202	finally
    //   69	78	221	finally
    //   100	107	221	finally
    //   131	167	221	finally
    //   171	180	230	finally
  }

  // ERROR //
  public boolean isArtistSelectedAsKeepOn(long paramLong)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 651	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   4: astore_3
    //   5: iconst_1
    //   6: anewarray 49	java/lang/String
    //   9: astore 4
    //   11: aload 4
    //   13: iconst_0
    //   14: ldc_w 1725
    //   17: aastore
    //   18: iconst_1
    //   19: anewarray 49	java/lang/String
    //   22: astore 5
    //   24: lload_1
    //   25: invokestatic 750	java/lang/Long:toString	(J)Ljava/lang/String;
    //   28: astore 6
    //   30: aload 5
    //   32: iconst_0
    //   33: aload 6
    //   35: aastore
    //   36: aload_3
    //   37: iconst_1
    //   38: ldc_w 556
    //   41: aload 4
    //   43: ldc_w 1946
    //   46: aload 5
    //   48: aconst_null
    //   49: aconst_null
    //   50: aconst_null
    //   51: aconst_null
    //   52: invokevirtual 1051	android/database/sqlite/SQLiteDatabase:query	(ZLjava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   55: istore 7
    //   57: iload 7
    //   59: istore 8
    //   61: iload 8
    //   63: invokeinterface 622 1 0
    //   68: istore 7
    //   70: iload 7
    //   72: istore 9
    //   74: iload 8
    //   76: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   79: aload_0
    //   80: aload_3
    //   81: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   84: iload 9
    //   86: ireturn
    //   87: astore 10
    //   89: iconst_0
    //   90: istore 8
    //   92: iload 8
    //   94: invokestatic 382	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   97: aload_0
    //   98: aload_3
    //   99: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   102: aload 10
    //   104: athrow
    //   105: astore 10
    //   107: goto -15 -> 92
    //
    // Exception table:
    //   from	to	target	type
    //   5	57	87	finally
    //   61	70	105	finally
  }

  public boolean isAutoPlaylistSelectedAsKeepOn(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      String[] arrayOfString1 = new String[1];
      arrayOfString1[0] = "KeepOnId";
      String[] arrayOfString2 = new String[1];
      String str = Long.toString(paramLong);
      arrayOfString2[0] = str;
      int i = localSQLiteDatabase.query(true, "KEEPON", arrayOfString1, "AutoListId=?", arrayOfString2, null, null, null, null);
      j = i;
      if (j == 0);
    }
    finally
    {
      try
      {
        int j;
        boolean bool1 = j.moveToNext();
        if (bool1);
        for (boolean bool2 = true; ; bool2 = false)
        {
          safeClose(j);
          endRead(localSQLiteDatabase);
          return bool2;
        }
        localObject1 = finally;
        int k = 0;
        safeClose(k);
        endRead(localSQLiteDatabase);
        throw localObject1;
      }
      finally
      {
      }
    }
  }

  public boolean isLocalCopyCp(long paramLong)
  {
    boolean bool1 = true;
    String[] arrayOfString = new String[bool1];
    String str = Long.toString(paramLong);
    arrayOfString[0] = str;
    Cursor localCursor = executeRawQuery("SELECT 1 FROM MUSIC WHERE Id=?AND CpData NOT NULL AND LocalCopyType IN (100,200)", arrayOfString);
    if (localCursor != null);
    try
    {
      boolean bool2 = localCursor.moveToFirst();
      if (bool2)
        return bool1;
      bool1 = false;
    }
    finally
    {
      safeClose(localCursor);
    }
  }

  public boolean isPlaylistSelectedAsKeepOn(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      String[] arrayOfString1 = new String[1];
      arrayOfString1[0] = "KeepOnId";
      String[] arrayOfString2 = new String[1];
      String str = Long.toString(paramLong);
      arrayOfString2[0] = str;
      int i = localSQLiteDatabase.query(true, "KEEPON", arrayOfString1, "ListId=?", arrayOfString2, null, null, null, null);
      j = i;
      if (j == 0);
    }
    finally
    {
      try
      {
        int j;
        boolean bool1 = j.moveToNext();
        if (bool1);
        for (boolean bool2 = true; ; bool2 = false)
        {
          safeClose(j);
          endRead(localSQLiteDatabase);
          return bool2;
        }
        localObject1 = finally;
        int k = 0;
        safeClose(k);
        endRead(localSQLiteDatabase);
        throw localObject1;
      }
      finally
      {
      }
    }
  }

  public void markSongPlayed(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    while (true)
      try
      {
        arrayOfString1 = new String[1];
        String str = Long.toString(paramLong);
        arrayOfString1[0] = str;
        String[] arrayOfString2 = new String[1];
        arrayOfString2[0] = "PlayCount";
        int i = localSQLiteDatabase.query("MUSIC", arrayOfString2, "Id=?", arrayOfString1, null, null, null);
        k = i;
        if (k == 0);
      }
      finally
      {
        try
        {
          String[] arrayOfString1;
          int k;
          if (k.moveToNext())
          {
            long l1 = k.getLong(0);
            long l2 = System.currentTimeMillis();
            ContentValues localContentValues = new ContentValues();
            Long localLong1 = Long.valueOf(l1 + 1L);
            localContentValues.put("PlayCount", localLong1);
            Long localLong2 = Long.valueOf(l2);
            localContentValues.put("LastPlayDate", localLong2);
            Long localLong3 = Long.valueOf(l2);
            localContentValues.put("CacheDate", localLong3);
            int j = localSQLiteDatabase.update("MUSIC", localContentValues, "Id=?", arrayOfString1);
            if (j == 1)
            {
              bool = true;
              safeClose(k);
              endWriteTxn(localSQLiteDatabase, bool);
              return;
            }
            bool = false;
            continue;
            localObject1 = finally;
            int m = 0;
            safeClose(m);
            endWriteTxn(localSQLiteDatabase, false);
            throw localObject1;
          }
        }
        finally
        {
        }
        boolean bool = false;
      }
  }

  public void movePlaylistItem(long paramLong1, long paramLong2, long paramLong3)
  {
    SQLiteDatabase localSQLiteDatabase1 = beginWriteTxn();
    try
    {
      long l1 = paramLong1;
      boolean bool1 = preparePlaylistForModification(localSQLiteDatabase1, l1);
      boolean bool2;
      if (paramLong3 == 0L)
      {
        long l2 = paramLong1;
        long l3 = paramLong2;
        bool2 = PlayList.moveItemToTop(localSQLiteDatabase1, l2, l3, true, bool1);
      }
      for (boolean bool3 = bool2; ; bool3 = bool2)
      {
        Store localStore1 = this;
        boolean bool4 = bool3;
        localStore1.endWriteTxn(localSQLiteDatabase1, bool4);
        if (!bool3)
          return;
        Context localContext = this.mContext;
        long l4 = paramLong1;
        RecentItemsManager.addModifiedPlaylist(localContext, l4);
        return;
        boolean bool5 = true;
        SQLiteDatabase localSQLiteDatabase2 = localSQLiteDatabase1;
        long l5 = paramLong1;
        long l6 = paramLong2;
        long l7 = paramLong3;
        boolean bool6 = bool1;
        bool2 = PlayList.moveItem(localSQLiteDatabase2, l5, l6, l7, bool5, bool6);
      }
    }
    finally
    {
      Store localStore2 = this;
      boolean bool7 = false;
      localStore2.endWriteTxn(localSQLiteDatabase1, bool7);
    }
  }

  public void populateEphemeralTop(Account paramAccount, List<Track> paramList)
  {
    if (paramList.isEmpty())
      return;
    boolean bool1 = false;
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      Set localSet = getEphemeralTracksNotInPlaylist(localSQLiteDatabase);
      int i = computeAccountHash(paramAccount);
      List localList = tryToInsertOrUpdateExternalSongs(localSQLiteDatabase, i, paramList, false);
      boolean bool2 = localSet.removeAll(localList);
      int j = deleteTracks(localSQLiteDatabase, localSet);
      endWriteTxn(localSQLiteDatabase, true);
      return;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, bool1);
    }
  }

  public boolean purgeNautilusTrackByLocalId(Context paramContext, long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      String[] arrayOfString = new String[3];
      String str1 = String.valueOf(paramLong);
      arrayOfString[0] = str1;
      String str2 = Integer.toString(4);
      arrayOfString[1] = str2;
      String str3 = Integer.toString(5);
      arrayOfString[2] = str3;
      int i = localSQLiteDatabase.delete("MUSIC", "SourceAccount != 0 AND MUSIC.Id =?  AND TrackType IN (?, ?)", arrayOfString);
      int j = 0 + i;
      if (j > 0)
      {
        int k = PlayList.removeOrphanedItems(localSQLiteDatabase);
        j += k;
      }
      if (j > 0)
        RecentItemsManager.update(this.mContext, localSQLiteDatabase);
      if (j > 0)
      {
        bool = true;
        endWriteTxn(localSQLiteDatabase, bool);
        ContentResolver localContentResolver;
        Uri localUri;
        if (bool)
        {
          localContentResolver = this.mContext.getContentResolver();
          localUri = MusicContent.CONTENT_URI;
        }
        return bool;
      }
      boolean bool = false;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public void removeAllSuggestedMixes()
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      int i = localSQLiteDatabase.delete("SUGGESTED_SEEDS", null, null);
      int j = removeOrphanedSuggestedMixes(localSQLiteDatabase);
      if (j > 0)
        int k = deleteOrphanedExternalMusic(localSQLiteDatabase);
      endWriteTxn(localSQLiteDatabase, true);
      if ((i <= 0) && (j <= 0))
        return;
      notifySuggestedMixChange();
      return;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public void removeFileLocation(long paramLong)
  {
    ContentValues localContentValues = new ContentValues(2);
    localContentValues.putNull("LocalCopyPath");
    Integer localInteger1 = Integer.valueOf(0);
    localContentValues.put("LocalCopyType", localInteger1);
    Integer localInteger2 = Integer.valueOf(0);
    localContentValues.put("LocalCopyStorageType", localInteger2);
    Integer localInteger3 = Integer.valueOf(0);
    localContentValues.put("LocalCopySize", localInteger3);
    Integer localInteger4 = Integer.valueOf(0);
    localContentValues.put("CacheDate", localInteger4);
    LogFile localLogFile = Log.getLogFile("com.google.android.music.pin");
    if (localLogFile != null)
    {
      Object[] arrayOfObject = new Object[1];
      Long localLong = Long.valueOf(paramLong);
      arrayOfObject[0] = localLong;
      String str1 = String.format("removeFileLocation: %d", arrayOfObject);
      localLogFile.d("MusicStore", str1);
    }
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      String[] arrayOfString = new String[1];
      String str2 = Long.toString(paramLong);
      arrayOfString[0] = str2;
      if (localSQLiteDatabase.update("MUSIC", localContentValues, "Id=?", arrayOfString) != 1)
      {
        String str3 = "Unknown Music.Id: " + paramLong;
        throw new IllegalArgumentException(str3);
      }
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
    endWriteTxn(localSQLiteDatabase, true);
  }

  public int renamePlaylist(Context paramContext, long paramLong, String paramString)
  {
    int i = 1;
    int j = 0;
    throwIfPlayQueue(paramLong);
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    while (true)
    {
      try
      {
        PlayList localPlayList = PlayList.readPlayList(localSQLiteDatabase, paramLong, null);
        if (localPlayList == null)
        {
          Log.w("MusicStore", "Requested playlist is not found");
          return j;
        }
        boolean bool1 = PlayList.rename(localSQLiteDatabase, paramLong, paramString);
        boolean bool2 = bool1;
        endWriteTxn(localSQLiteDatabase, true);
        if (localPlayList.getMediaStoreId() != 0L)
        {
          ContentValues localContentValues = new ContentValues(1);
          localContentValues.put("name", paramString);
          Uri localUri1 = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
          long l = localPlayList.getMediaStoreId();
          Uri localUri2 = ContentUris.withAppendedId(localUri1, l);
          int k = paramContext.getContentResolver().update(localUri2, localContentValues, null, null);
        }
        if (bool2)
        {
          j = i;
          continue;
        }
      }
      finally
      {
        endWriteTxn(localSQLiteDatabase, false);
      }
      i = 0;
    }
  }

  void replaceSuggestedSeeds(int paramInt, String[] paramArrayOfString)
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      replaceSuggestedSeeds(localSQLiteDatabase, paramInt, paramArrayOfString);
      endWriteTxn(localSQLiteDatabase, true);
      if (1 == 0)
        return;
      notifySuggestedMixChange();
      return;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public boolean requiresDownload(DownloadRequest paramDownloadRequest)
  {
    SQLiteDatabase localSQLiteDatabase = beginRead();
    try
    {
      String[] arrayOfString1 = new String[1];
      String str1 = Long.toString(paramDownloadRequest.getId().getId());
      arrayOfString1[0] = str1;
      String[] arrayOfString2 = new String[3];
      arrayOfString2[0] = "LocalCopyPath";
      arrayOfString2[1] = "LocalCopyType";
      arrayOfString2[2] = "LocalCopyStorageType";
      int i = localSQLiteDatabase.query("MUSIC", arrayOfString2, "Id =?", arrayOfString1, null, null, null);
      k = i;
      if (k == 0);
    }
    finally
    {
      try
      {
        int k;
        boolean bool1 = k.moveToNext();
        boolean bool3;
        if (!bool1)
        {
          safeClose(k);
          endRead(localSQLiteDatabase);
          bool3 = true;
        }
        while (true)
        {
          return bool3;
          int n = null;
          String str2 = k.getString(n);
          Object localObject1 = str2;
          if (localObject1 == null)
          {
            safeClose(k);
            endRead(localSQLiteDatabase);
            bool3 = true;
          }
          else
          {
            int i1 = paramDownloadRequest.getFileLocation().getSchemaValueForCacheType();
            int j = k.getInt(1);
            int i2 = j;
            if ((i1 == 100) && (100 != i2) && (200 != i2))
            {
              safeClose(k);
              endRead(localSQLiteDatabase);
              bool3 = true;
            }
            else if ((i1 == 200) && (i1 != i2))
            {
              safeClose(k);
              endRead(localSQLiteDatabase);
              bool3 = true;
            }
            else
            {
              i1 = 2;
              int i3 = k.getInt(i1);
              File localFile = CacheUtils.resolveMusicPath(this.mContext, (String)localObject1, i3);
              localObject1 = localFile;
              if (localObject1 == null)
              {
                safeClose(k);
                endRead(localSQLiteDatabase);
                bool3 = true;
              }
              else
              {
                boolean bool2 = ((File)localObject1).exists();
                if (!bool2)
                {
                  safeClose(k);
                  endRead(localSQLiteDatabase);
                  bool3 = true;
                }
                else
                {
                  safeClose(k);
                  endRead(localSQLiteDatabase);
                  bool3 = false;
                }
              }
            }
          }
        }
        localObject2 = finally;
        int m = 0;
        label343: safeClose(m);
        endRead(localSQLiteDatabase);
        throw localObject2;
      }
      finally
      {
        break label343;
      }
    }
  }

  public long[] requiresDownloadManager(long[] paramArrayOfLong)
  {
    int i = 0;
    SQLiteQueryBuilder localSQLiteQueryBuilder = new SQLiteQueryBuilder();
    localSQLiteQueryBuilder.setTables("MUSIC");
    localSQLiteQueryBuilder.appendWhere("MUSIC.SourceAccount!=0");
    int j = paramArrayOfLong.length * 2;
    StringBuilder localStringBuilder1 = new StringBuilder(j);
    int k = 0;
    while (true)
    {
      int m = paramArrayOfLong.length;
      if (k >= m)
        break;
      long l1 = paramArrayOfLong[k];
      StringBuilder localStringBuilder2 = localStringBuilder1.append(l1);
      int n = paramArrayOfLong.length + -1;
      if (k != n)
        StringBuilder localStringBuilder3 = localStringBuilder1.append(",");
      k += 1;
    }
    StringBuilder localStringBuilder4 = new StringBuilder().append(" AND MUSIC.Id IN (");
    String str1 = localStringBuilder1.toString();
    String str2 = str1 + ")";
    localSQLiteQueryBuilder.appendWhere(str2);
    SQLiteDatabase localSQLiteDatabase = beginRead();
    int i1 = 1;
    while (true)
    {
      Cursor localCursor;
      try
      {
        String[] arrayOfString = new String[i1];
        arrayOfString[0] = "MUSIC.Id";
        localCursor = localSQLiteQueryBuilder.query(localSQLiteDatabase, arrayOfString, null, null, null, null, null);
        int i2 = localCursor.getCount();
        int i3 = i2;
        if (i3 == 0)
          return i;
        i = i3;
        k = 0;
        if (localCursor.moveToNext())
        {
          long l2 = localCursor.getLong(0);
          i[k] = l2;
          k += 1;
          continue;
        }
      }
      finally
      {
        safeClose(localCursor);
        endRead(localSQLiteDatabase);
      }
      safeClose(localCursor);
    }
  }

  public boolean resetSyncState()
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      int i = localSQLiteDatabase.delete("_sync_state", null, null);
      endWriteTxn(localSQLiteDatabase, true);
      return true;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public void saveArtwork(long paramLong, String paramString, int paramInt)
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      ContentValues localContentValues = new ContentValues();
      Long localLong = Long.valueOf(paramLong);
      localContentValues.put("AlbumId", localLong);
      localContentValues.put("LocalLocation", paramString);
      Integer localInteger = Integer.valueOf(paramInt);
      localContentValues.put("LocalLocationStorageType", localInteger);
      long l = localSQLiteDatabase.insertWithOnConflict("ARTWORK", null, localContentValues, 5);
      endWriteTxn(localSQLiteDatabase, true);
      return;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public void saveArtwork(String paramString1, String paramString2, int paramInt, long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      ContentValues localContentValues = new ContentValues();
      localContentValues.put("RemoteLocation", paramString1);
      localContentValues.put("LocalLocation", paramString2);
      Integer localInteger = Integer.valueOf(paramInt);
      localContentValues.put("LocalLocationStorageType", localInteger);
      Long localLong1 = Long.valueOf(System.currentTimeMillis());
      localContentValues.put("Timestamp", localLong1);
      Long localLong2 = Long.valueOf(paramLong);
      localContentValues.put("FileSize", localLong2);
      long l = localSQLiteDatabase.insertWithOnConflict("ARTWORK_CACHE", null, localContentValues, 5);
      endWriteTxn(localSQLiteDatabase, true);
      return;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  // ERROR //
  public long saveRadioStation(RadioStation paramRadioStation)
  {
    // Byte code:
    //   0: ldc2_w 172
    //   3: istore_2
    //   4: aload_0
    //   5: invokevirtual 165	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   8: astore_3
    //   9: aload_3
    //   10: invokestatic 2048	com/google/android/music/store/RadioStation:compileInsertStatement	(Landroid/database/sqlite/SQLiteDatabase;)Landroid/database/sqlite/SQLiteStatement;
    //   13: lstore 4
    //   15: lload 4
    //   17: lstore 6
    //   19: aload_1
    //   20: lload 6
    //   22: invokevirtual 2050	com/google/android/music/store/RadioStation:insert	(Landroid/database/sqlite/SQLiteStatement;)J
    //   25: lstore 4
    //   27: lload 4
    //   29: lstore 8
    //   31: lload 8
    //   33: ldc2_w 172
    //   36: lcmp
    //   37: ifeq +143 -> 180
    //   40: iconst_1
    //   41: istore_2
    //   42: aload_0
    //   43: aload_3
    //   44: iload_2
    //   45: invokevirtual 185	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   48: lload 6
    //   50: invokestatic 1103	com/google/android/music/store/Store:safeClose	(Landroid/database/sqlite/SQLiteProgram;)V
    //   53: iload_2
    //   54: ifeq +26 -> 80
    //   57: aload_0
    //   58: getfield 145	com/google/android/music/store/Store:mContext	Landroid/content/Context;
    //   61: invokevirtual 191	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   64: astore 10
    //   66: getstatic 2053	com/google/android/music/store/MusicContent$RadioStations:CONTENT_URI	Landroid/net/Uri;
    //   69: astore 11
    //   71: aload 10
    //   73: aload 11
    //   75: aconst_null
    //   76: iconst_0
    //   77: invokevirtual 933	android/content/ContentResolver:notifyChange	(Landroid/net/Uri;Landroid/database/ContentObserver;Z)V
    //   80: lload 8
    //   82: lreturn
    //   83: astore 12
    //   85: iconst_0
    //   86: lstore 13
    //   88: iconst_1
    //   89: anewarray 339	java/lang/Object
    //   92: astore 15
    //   94: aload 15
    //   96: iconst_0
    //   97: aload_1
    //   98: aastore
    //   99: ldc_w 2055
    //   102: aload 15
    //   104: invokestatic 804	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   107: astore 16
    //   109: ldc_w 267
    //   112: aload 16
    //   114: aload 12
    //   116: invokestatic 440	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   119: aload_0
    //   120: aload_3
    //   121: iconst_0
    //   122: invokevirtual 185	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   125: lload 13
    //   127: invokestatic 1103	com/google/android/music/store/Store:safeClose	(Landroid/database/sqlite/SQLiteProgram;)V
    //   130: ldc2_w 172
    //   133: lstore 8
    //   135: goto -55 -> 80
    //   138: astore 17
    //   140: iconst_0
    //   141: lstore 13
    //   143: aload_0
    //   144: aload_3
    //   145: iconst_0
    //   146: invokevirtual 185	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   149: lload 13
    //   151: invokestatic 1103	com/google/android/music/store/Store:safeClose	(Landroid/database/sqlite/SQLiteProgram;)V
    //   154: aload 17
    //   156: athrow
    //   157: astore 17
    //   159: lload 6
    //   161: lstore 13
    //   163: goto -20 -> 143
    //   166: astore 17
    //   168: goto -25 -> 143
    //   171: astore 12
    //   173: lload 6
    //   175: lstore 13
    //   177: goto -89 -> 88
    //   180: iconst_0
    //   181: istore_2
    //   182: goto -140 -> 42
    //
    // Exception table:
    //   from	to	target	type
    //   9	15	83	java/lang/RuntimeException
    //   9	15	138	finally
    //   19	27	157	finally
    //   88	119	166	finally
    //   19	27	171	java/lang/RuntimeException
  }

  public void shufflePlayQueue(int paramInt)
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      PlayQueue localPlayQueue = PlayQueue.getInstance();
      localPlayQueue.shuffle(localSQLiteDatabase, paramInt);
      endWriteTxn(localSQLiteDatabase, true);
      ContentResolver localContentResolver = this.mContext.getContentResolver();
      localPlayQueue.notifyChange(localContentResolver);
      return;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public List<Long> tryToInsertOrUpdateExternalSongs(int paramInt, List<Track> paramList, boolean paramBoolean)
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      List localList1 = tryToInsertOrUpdateExternalSongs(localSQLiteDatabase, paramInt, paramList, paramBoolean);
      List localList2 = localList1;
      endWriteTxn(localSQLiteDatabase, true);
      return localList2;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public void updateCachedFileLocation(long paramLong1, String paramString, int paramInt1, long paramLong2, int paramInt2)
  {
    LogFile localLogFile = Log.getLogFile("com.google.android.music.pin");
    ContentValues localContentValues = new ContentValues();
    if (!TextUtils.isEmpty(paramString))
    {
      localContentValues.put("LocalCopyPath", paramString);
      if ((paramInt1 != 100) && (paramInt1 != 200))
      {
        String str1 = "Invalid value for localCopyStatus: " + paramInt1;
        throw new IllegalArgumentException(str1);
      }
      Integer localInteger1 = Integer.valueOf(paramInt1);
      localContentValues.put("LocalCopyType", localInteger1);
      Long localLong1 = Long.valueOf(paramLong2);
      localContentValues.put("LocalCopySize", localLong1);
      Integer localInteger2 = Integer.valueOf(paramInt2);
      localContentValues.put("LocalCopyStorageType", localInteger2);
      Long localLong2 = Long.valueOf(System.currentTimeMillis());
      localContentValues.put("CacheDate", localLong2);
      if (localLogFile != null)
      {
        Object[] arrayOfObject1 = new Object[3];
        Long localLong3 = Long.valueOf(paramLong1);
        arrayOfObject1[0] = localLong3;
        arrayOfObject1[1] = paramString;
        Integer localInteger3 = Integer.valueOf(paramInt1);
        arrayOfObject1[2] = localInteger3;
        String str2 = String.format("saveFileLocation: saving reference for id=%d localPath=%s localCopyType=%d", arrayOfObject1);
        localLogFile.d("MusicStore", str2);
      }
    }
    while (true)
    {
      boolean bool = false;
      SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
      try
      {
        String[] arrayOfString = new String[1];
        String str3 = Long.toString(paramLong1);
        arrayOfString[0] = str3;
        if (localSQLiteDatabase.update("MUSIC", localContentValues, "Id=?", arrayOfString) != 1)
        {
          String str4 = "Trying to save music location for unknown Music.Id: " + paramLong1;
          Log.w("MusicStore", str4);
          return;
          localContentValues.putNull("LocalCopyPath");
          Integer localInteger4 = Integer.valueOf(0);
          localContentValues.put("LocalCopyType", localInteger4);
          Integer localInteger5 = Integer.valueOf(0);
          localContentValues.put("LocalCopyType", localInteger5);
          Integer localInteger6 = Integer.valueOf(0);
          localContentValues.put("LocalCopySize", localInteger6);
          Integer localInteger7 = Integer.valueOf(0);
          localContentValues.put("CacheDate", localInteger7);
          localContentValues.putNull("CpData");
          if (localLogFile == null)
            continue;
          Object[] arrayOfObject2 = new Object[4];
          Long localLong4 = Long.valueOf(paramLong1);
          arrayOfObject2[0] = localLong4;
          arrayOfObject2[1] = paramString;
          Integer localInteger8 = Integer.valueOf(paramInt1);
          arrayOfObject2[2] = localInteger8;
          Integer localInteger9 = Integer.valueOf(paramInt2);
          arrayOfObject2[3] = localInteger9;
          String str5 = String.format("updateCachedFileLocation: clear database reference for id=%d localPath=%s localCopyType=%d localCopyStorageType=%d", arrayOfObject2);
          localLogFile.d("MusicStore", str5);
          continue;
        }
        endWriteTxn(localSQLiteDatabase, true);
        return;
      }
      finally
      {
        endWriteTxn(localSQLiteDatabase, bool);
      }
    }
  }

  void updateFollowedSharedPlaylistItems(Account paramAccount, String paramString, List<SyncablePlaylistEntry> paramList)
  {
    if (paramList == null)
    {
      Log.w("MusicStore", "Missing playlist entries");
      return;
    }
    int i = computeAccountHash(paramAccount);
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    long l;
    LinkedList localLinkedList;
    try
    {
      l = getListIdByShareToken(localSQLiteDatabase, paramString);
      String[] arrayOfString = new String[1];
      String str = Long.toString(l);
      arrayOfString[0] = str;
      int j = localSQLiteDatabase.delete("LISTITEMS", "ListId=?", arrayOfString);
      localLinkedList = new LinkedList();
      Iterator localIterator = paramList.iterator();
      while (localIterator.hasNext())
      {
        localSyncablePlaylistEntry = (SyncablePlaylistEntry)localIterator.next();
        if (!localSyncablePlaylistEntry.mIsDeleted)
        {
          Track localTrack = localSyncablePlaylistEntry.mTrack;
          boolean bool = localLinkedList.add(localTrack);
        }
      }
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
    SyncablePlaylistEntry localSyncablePlaylistEntry = null;
    List localList = tryToInsertOrUpdateExternalSongs(localSQLiteDatabase, i, localLinkedList, localSyncablePlaylistEntry);
    List<SyncablePlaylistEntry> localList1 = paramList;
    PlayList.appendItemsToSharedPlaylist(localSQLiteDatabase, l, 0, i, localList, localList1);
    endWriteTxn(localSQLiteDatabase, true);
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    Uri localUri = MusicContent.Playlists.CONTENT_URI;
    localContentResolver.notifyChange(localUri, null, false);
  }

  public void updateKeeponDownloadSongCounts()
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      updateKeeponDownloadSongCounts(localSQLiteDatabase);
      endWriteTxn(localSQLiteDatabase, true);
      return;
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public void updateKeeponDownloadSongCounts(SQLiteDatabase paramSQLiteDatabase)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "KeepOnId";
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    String[] arrayOfString2 = null;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    Cursor localCursor = localSQLiteDatabase.query("KEEPON", arrayOfString1, null, arrayOfString2, str1, str2, str3);
    if (localCursor != null);
    try
    {
      while (localCursor.moveToNext())
      {
        long l1 = localCursor.getLong(0);
        long l2 = getShouldKeeponSongCount(paramSQLiteDatabase, l1);
        long l3 = getShouldKeeponDownloadedSongCount(paramSQLiteDatabase, l1);
        ContentValues localContentValues = new ContentValues();
        Long localLong1 = Long.valueOf(l2);
        localContentValues.put("SongCount", localLong1);
        Long localLong2 = Long.valueOf(l3);
        localContentValues.put("DownloadedSongCount", localLong2);
        String[] arrayOfString3 = new String[1];
        String str4 = Long.toString(l1);
        arrayOfString3[0] = str4;
        if (paramSQLiteDatabase.update("KEEPON", localContentValues, "KEEPON.KeepOnId=?", arrayOfString3) != 1)
        {
          String str5 = "Failed to udpate the download counts for keeponId=" + l1;
          Log.w("MusicStore", str5);
        }
      }
    }
    finally
    {
      safeClose(localCursor);
    }
  }

  // ERROR //
  public int updateRating(long paramLong, int paramInt)
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore 4
    //   3: iload_3
    //   4: invokestatic 2088	com/google/android/music/store/MusicFile:throwIfInvalidRating	(I)V
    //   7: aload_0
    //   8: invokevirtual 651	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   11: astore 5
    //   13: aload 5
    //   15: lload_1
    //   16: invokestatic 1351	com/google/android/music/store/Store:getSongId	(Landroid/database/sqlite/SQLiteDatabase;J)J
    //   19: lstore 6
    //   21: lload 6
    //   23: lstore 8
    //   25: aload_0
    //   26: aload 5
    //   28: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   31: new 229	android/content/ContentValues
    //   34: dup
    //   35: iconst_2
    //   36: invokespecial 232	android/content/ContentValues:<init>	(I)V
    //   39: astore 5
    //   41: iload_3
    //   42: invokestatic 670	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   45: astore 10
    //   47: aload 5
    //   49: ldc_w 1813
    //   52: aload 10
    //   54: invokevirtual 993	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   57: iconst_1
    //   58: invokestatic 670	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   61: astore 11
    //   63: aload 5
    //   65: ldc_w 2090
    //   68: aload 11
    //   70: invokevirtual 993	android/content/ContentValues:put	(Ljava/lang/String;Ljava/lang/Integer;)V
    //   73: aload_0
    //   74: invokevirtual 165	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   77: astore 12
    //   79: iconst_1
    //   80: anewarray 49	java/lang/String
    //   83: astore 13
    //   85: lload 8
    //   87: invokestatic 750	java/lang/Long:toString	(J)Ljava/lang/String;
    //   90: astore 14
    //   92: aload 13
    //   94: iconst_0
    //   95: aload 14
    //   97: aastore
    //   98: aload 12
    //   100: ldc_w 362
    //   103: aload 5
    //   105: ldc_w 2092
    //   108: aload 13
    //   110: invokevirtual 248	android/database/sqlite/SQLiteDatabase:update	(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   113: istore 15
    //   115: iload 15
    //   117: istore 4
    //   119: aload_0
    //   120: aload 12
    //   122: iconst_1
    //   123: invokevirtual 185	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   126: iload 4
    //   128: ireturn
    //   129: astore 16
    //   131: aload_0
    //   132: aload 5
    //   134: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   137: aload 16
    //   139: athrow
    //   140: astore 17
    //   142: aload_0
    //   143: aload 12
    //   145: iconst_0
    //   146: invokevirtual 185	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   149: aload 17
    //   151: athrow
    //   152: astore 18
    //   154: aload_0
    //   155: aload 5
    //   157: invokevirtual 665	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   160: goto -34 -> 126
    //
    // Exception table:
    //   from	to	target	type
    //   13	21	129	finally
    //   79	115	140	finally
    //   13	21	152	java/io/FileNotFoundException
  }

  public void updateSuggestedMixes(int paramInt, String[] paramArrayOfString1, String[] paramArrayOfString2, List<Track>[] paramArrayOfList)
  {
    SQLiteDatabase localSQLiteDatabase = beginWriteTxn();
    try
    {
      replaceSuggestedSeeds(localSQLiteDatabase, paramInt, paramArrayOfString1);
      int i = 0;
      while (true)
      {
        int j = paramArrayOfString1.length;
        if (i >= j)
          break;
        String str1 = paramArrayOfString1[i];
        String str2 = paramArrayOfString2[i];
        List<Track> localList = paramArrayOfList[i];
        Store localStore = this;
        int k = paramInt;
        long l = localStore.createPlaylistForSuggestedSeed(localSQLiteDatabase, k, str1, str2, localList);
        i += 1;
      }
      int m = deleteOrphanedExternalMusic(localSQLiteDatabase);
      m = 1;
      endWriteTxn(localSQLiteDatabase, m);
      if (m == 0)
        return;
      notifySuggestedMixChange();
      return;
    }
    catch (DataNotFoundException localDataNotFoundException)
    {
      while (true)
      {
        Log.wtf("MusicStore", "Seed record not found");
        endWriteTxn(localSQLiteDatabase, false);
        int n = 0;
      }
    }
    finally
    {
      endWriteTxn(localSQLiteDatabase, false);
    }
  }

  private class DatabaseHelper extends SQLiteOpenHelper
  {
    private String mDBPath = null;
    private boolean mFullResync = false;
    private boolean mResetDerivedAudioData = false;
    private boolean mResetMediaStoreImport = false;
    private boolean mResetRemoteContent = false;

    DatabaseHelper()
    {
      super("music.db", null, 72);
    }

    private void postImportProcessing(SQLiteDatabase paramSQLiteDatabase)
    {
      if (this.mResetMediaStoreImport)
      {
        Store.deleteLocalMusicAndPlaylists(paramSQLiteDatabase);
        MediaStoreImporter localMediaStoreImporter = Store.this.mMediaStoreImporter;
        Context localContext = Store.this.mContext;
        localMediaStoreImporter.invalidateMediaStoreImport(localContext);
      }
      if ((this.mResetRemoteContent) && (Store.deleteRemoteMusicAndPlaylists(Store.this.mContext, paramSQLiteDatabase)))
      {
        Bundle localBundle1 = new Bundle();
        ContentResolver.requestSync(null, "com.google.android.music.MusicContent", localBundle1);
      }
      if (this.mFullResync)
      {
        int i = paramSQLiteDatabase.delete("_sync_state", null, null);
        Bundle localBundle2 = new Bundle();
        ContentResolver.requestSync(null, "com.google.android.music.MusicContent", localBundle2);
      }
      if (this.mResetDerivedAudioData)
        upgradeDerivedMusicData(paramSQLiteDatabase);
      Object localObject = new Object();
      MusicPreferences.getMusicPreferences(Store.this.mContext, localObject).setDatabaseVersion(72);
      MusicPreferences.releaseMusicPreferences(localObject);
    }

    private void resetTrackSyncState(SQLiteDatabase paramSQLiteDatabase)
    {
      String[] arrayOfString1 = new String[2];
      arrayOfString1[0] = "_id";
      arrayOfString1[1] = "data";
      Cursor localCursor = paramSQLiteDatabase.query("_sync_state", arrayOfString1, null, null, null, null, null);
      ClientSyncState localClientSyncState1 = null;
      ClientSyncState localClientSyncState2 = null;
      if (localCursor != null);
      try
      {
        boolean bool = localCursor.moveToFirst();
        if (!bool);
        String str1;
        String[] arrayOfString2;
        SQLiteDatabase localSQLiteDatabase;
        ContentValues localContentValues2;
        do
        {
          do
          {
            return;
            int i = 0;
            str1 = localCursor.getString(i);
            localClientSyncState1 = ClientSyncState.parseFrom(localCursor.getBlob(1));
          }
          while (localClientSyncState1 == null);
          localClientSyncState2 = ClientSyncState.newBuilder(localClientSyncState1).setRemoteTrackVersion(1L).setEtagTrack(null).build();
          ContentValues localContentValues1 = new android/content/ContentValues;
          localContentValues1.<init>(1);
          byte[] arrayOfByte = localClientSyncState2.toBytes();
          localContentValues1.put("data", arrayOfByte);
          arrayOfString2 = new String[1];
          arrayOfString2[0] = str1;
          localSQLiteDatabase = paramSQLiteDatabase;
          localContentValues2 = localContentValues1;
        }
        while (localSQLiteDatabase.update("_sync_state", localContentValues2, "_id=?", arrayOfString2) == 1);
        String str2 = "Can't update record in _sync_state table for ID: " + str1;
        throw new IllegalArgumentException(str2);
      }
      catch (ProviderException localProviderException)
      {
        while (true)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("Can't update client sync state. original: ").append(localClientSyncState1).append(" updated: ");
          ClientSyncState localClientSyncState3 = localClientSyncState2;
          String str3 = localClientSyncState3;
          Log.e("MusicStore", str3);
        }
      }
      finally
      {
        Store.safeClose(localCursor);
      }
    }

    private void updatefullPathToRelativePathAndStorageType(SQLiteDatabase paramSQLiteDatabase, String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, File paramFile)
    {
      String str1 = DbUtils.quoteStringValue("0123456789abcdefghijkmnopqrstuvwxyzABCDEFGHIJKMNOPQRSTUVWXYZ.-~`@#$%^&*()_+=;,<>? ");
      StringBuilder localStringBuilder = new StringBuilder().append("UPDATE ").append(paramString1).append(" SET ").append(paramString2).append(" = ").append("substr(").append(paramString2).append(", ").append("length(").append("rtrim(").append(paramString2).append(", ").append(str1).append(")").append(") + 1").append("), ").append(paramString3).append(" = ").append(" CASE ").append(" WHEN ").append(paramString2).append(" LIKE '");
      String str2 = DbUtils.escapeForLikeOperator(paramFile.getAbsolutePath(), '!');
      String str3 = str2 + "%' ESCAPE '!' " + " THEN " + paramInt1 + " ELSE " + paramInt2 + " END" + " WHERE " + paramString2 + " NOT NULL ";
      paramSQLiteDatabase.execSQL(str3);
    }

    private void upgradeDerivedMusicData(SQLiteDatabase paramSQLiteDatabase)
    {
      SQLiteStatement localSQLiteStatement = null;
      String[] arrayOfString1 = MusicFile.FULL_PROJECTION;
      SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
      String[] arrayOfString2 = null;
      String str1 = null;
      String str2 = null;
      String str3 = null;
      Cursor localCursor = localSQLiteDatabase.query("MUSIC", arrayOfString1, null, arrayOfString2, str1, str2, str3);
      if (localCursor != null);
      try
      {
        MusicFile localMusicFile = new MusicFile();
        localSQLiteStatement = MusicFile.compileFullUpdateStatement(paramSQLiteDatabase);
        if (localCursor.moveToNext())
        {
          localMusicFile.populateFromFullProjectionCursor(localCursor);
          localMusicFile.resetDerivedFields();
          localMusicFile.updateMusicFile(localSQLiteStatement, paramSQLiteDatabase);
        }
      }
      finally
      {
        Store.safeClose(localCursor);
        Store.safeClose(localSQLiteStatement);
      }
    }

    private void upgradeFrom25(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN LocalCopySize INTEGER DEFAULT 0");
      String[] arrayOfString1 = new String[2];
      arrayOfString1[0] = "Id";
      arrayOfString1[1] = "LocalCopyPath";
      String[] arrayOfString2 = new String[2];
      String str1 = Integer.toString(200);
      arrayOfString2[0] = str1;
      String str2 = Integer.toString(100);
      arrayOfString2[1] = str2;
      SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
      String str3 = null;
      String str4 = null;
      Cursor localCursor = localSQLiteDatabase.query("MUSIC", arrayOfString1, "LocalCopyType in (?,?) ", arrayOfString2, null, str3, str4);
      while (true)
      {
        ContentValues localContentValues;
        try
        {
          int i = localCursor.getCount();
          if (!localCursor.moveToNext())
            break;
          String str5 = localCursor.getString(1);
          File localFile = new File(str5);
          localContentValues = new ContentValues();
          if ((localFile.exists()) && (localFile.isFile()))
          {
            Long localLong = Long.valueOf(localFile.length());
            localContentValues.put("LocalCopySize", localLong);
            localContentValues.remove("LocalCopyType");
            String[] arrayOfString3 = new String[1];
            String str6 = Long.toString(localCursor.getLong(0));
            arrayOfString3[0] = str6;
            int j = paramSQLiteDatabase.update("MUSIC", localContentValues, "Id=?", arrayOfString3);
            continue;
          }
        }
        finally
        {
          Store.safeClose(localCursor);
        }
        Integer localInteger1 = Integer.valueOf(0);
        localContentValues.put("LocalCopyType", localInteger1);
        Integer localInteger2 = Integer.valueOf(0);
        localContentValues.put("LocalCopySize", localInteger2);
      }
      Store.safeClose(localCursor);
    }

    private void upgradeFrom26(SQLiteDatabase paramSQLiteDatabase)
    {
      this.mResetRemoteContent = true;
      paramSQLiteDatabase.execSQL("DROP INDEX LISTITEMS_ORDER_INDEX");
      paramSQLiteDatabase.execSQL("ALTER TABLE LISTITEMS ADD COLUMN ServerOrder TEXT DEFAULT ''");
      paramSQLiteDatabase.execSQL("CREATE INDEX LISTITEMS_ORDER_INDEX ON LISTITEMS (ListId, ServerOrder, ClientPosition);");
    }

    private void upgradeFrom27(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE LISTITEMS ADD COLUMN ClientId TEXT");
    }

    private void upgradeFrom28(SQLiteDatabase paramSQLiteDatabase)
    {
      this.mResetDerivedAudioData = true;
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN TrackType INTEGER NOT NULL DEFAULT 0");
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN LocalCopyBitrate INTEGER NOT NULL DEFAULT 0");
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN ArtistOrigin INTEGER NOT NULL DEFAULT 0");
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN ArtistId INTEGER NOT NULL DEFAULT 0");
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN CanonicalArtist TEXT");
      paramSQLiteDatabase.execSQL("create index MUSIC_ARTISTID_INDEX on MUSIC(ArtistId)");
      paramSQLiteDatabase.execSQL("ALTER TABLE LISTS ADD COLUMN ListType INTEGER NOT NULL DEFAULT 0");
      paramSQLiteDatabase.execSQL("ALTER TABLE LISTS ADD COLUMN ListArtworkLocation TEXT ");
    }

    private void upgradeFrom29(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("DROP INDEX MUSIC_CANONICAL_NAME_INDEX");
      paramSQLiteDatabase.execSQL("DROP INDEX MUSIC_CANONICAL_ALBUM_CANONICAL_NAME_INDEX");
      paramSQLiteDatabase.execSQL("DROP INDEX MUSIC_CANONICAL_ARTIST_CANONICAL_NAME_INDEX");
      paramSQLiteDatabase.execSQL("create index LIST_SYNC_INDEX on LISTS(SourceAccount,SourceId)");
      paramSQLiteDatabase.execSQL("create index LISTITEMS_SYNC_INDEX on LISTS(SourceAccount,SourceId)");
    }

    private void upgradeFrom30(SQLiteDatabase paramSQLiteDatabase)
    {
      this.mResetMediaStoreImport = true;
      paramSQLiteDatabase.execSQL("DROP INDEX LISTITEMS_SYNC_INDEX");
      paramSQLiteDatabase.execSQL("create index LISTITEMS_SYNC_INDEX on LISTITEMS(SourceAccount,SourceId)");
    }

    private void upgradeFrom31(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN Rating INTEGER NOT NULL DEFAULT 0");
    }

    private void upgradeFrom32(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE RINGTONES(Id INTEGER PRIMARY KEY AUTOINCREMENT, MusicId INTEGER UNIQUE ON CONFLICT REPLACE, RequestDate INTEGER NOT NULL);");
    }

    private void upgradeFrom33(SQLiteDatabase paramSQLiteDatabase)
    {
      resetTrackSyncState(paramSQLiteDatabase);
    }

    private void upgradeFrom34(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("CREATE INDEX MUSIC_RATING ON MUSIC (Rating)");
      paramSQLiteDatabase.execSQL("CREATE INDEX MUSIC_FILE_DATE ON MUSIC (FileDate)");
      paramSQLiteDatabase.execSQL("CREATE INDEX MUSIC_TRACK_TYPE ON MUSIC (TrackType)");
    }

    private void upgradeFrom35(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN StoreId TEXT ");
    }

    private void upgradeFrom36(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN StoreAlbumId TEXT ");
    }

    private void upgradeFrom37(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE RECENT ADD COLUMN RecentReason INTEGER NOT NULL DEFAULT 0");
      paramSQLiteDatabase.execSQL("UPDATE RECENT SET RecentReason = CASE (SELECT TrackType FROM MUSIC WHERE AlbumId=RecentAlbumId AND FileDate=ItemDate LIMIT 1) WHEN 0 THEN 3 WHEN 2 THEN 3 WHEN 1 THEN 2 ELSE 1 END");
    }

    private void upgradeFrom38(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE SUGGESTED_SEEDS(Id INTEGER PRIMARY KEY AUTOINCREMENT, SeedSourceAccount INTEGER NOT NULL, SeedTrackSourceId TEXT NOT NULL, SeedListId INTEGER, UNIQUE( SeedSourceAccount,SeedTrackSourceId) ON CONFLICT IGNORE);");
    }

    private void upgradeFrom39(SQLiteDatabase paramSQLiteDatabase)
    {
      File localFile = CacheUtils.getInternalMusicCacheDirectory_Old(Store.this.mContext);
      if (localFile == null)
        throw new RuntimeException("Failed to find the internal cache location");
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN LocalCopyStorageType INTEGER NOT NULL DEFAULT 0");
      DatabaseHelper localDatabaseHelper = this;
      SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
      localDatabaseHelper.updatefullPathToRelativePathAndStorageType(localSQLiteDatabase, "MUSIC", "LocalCopyPath", "LocalCopyStorageType", 1, 2, localFile);
    }

    private void upgradeFrom40(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE PLAYQ_GROUPS(Id INTEGER PRIMARY KEY AUTOINCREMENT, Time INTEGER NOT NULL );");
    }

    private void upgradeFrom41(SQLiteDatabase paramSQLiteDatabase)
    {
      File localFile = CacheUtils.getInternalArtworkCacheDirectory_Old(Store.this.mContext);
      if (localFile == null)
        throw new RuntimeException("Failed to find the internal cache location");
      paramSQLiteDatabase.execSQL("ALTER TABLE ARTWORK ADD COLUMN LocalLocationStorageType INTEGER NOT NULL DEFAULT 0");
      DatabaseHelper localDatabaseHelper = this;
      SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
      localDatabaseHelper.updatefullPathToRelativePathAndStorageType(localSQLiteDatabase, "ARTWORK", "LocalLocation", "LocalLocationStorageType", 1, 2, localFile);
    }

    private void upgradeFrom42(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE RECENT ADD COLUMN Priority INTEGER NOT NULL DEFAULT 0");
    }

    private void upgradeFrom43(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE MUSIC_TOMBSTONES(Id INTEGER PRIMARY KEY AUTOINCREMENT, SourceAccount INTEGER, SourceId TEXT, _sync_version TEXT);");
    }

    private void upgradeFrom44(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE SUGGESTED_SEEDS ADD COLUMN SeedOrder INTEGER NOT NULL DEFAULT 0");
    }

    private void upgradeFrom45(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE KEEPON ADD COLUMN AutoListId INTEGER ");
    }

    private void upgradeFrom46(SQLiteDatabase paramSQLiteDatabase)
    {
      int i = paramSQLiteDatabase.delete("KEEPON", "ArtistId IS NOT NULL", null);
    }

    private void upgradeFrom47(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN Domain INTEGER NOT NULL DEFAULT 0");
    }

    private void upgradeFrom48(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE KEEPON ADD COLUMN SongCount INTEGER NOT NULL DEFAULT 0");
      paramSQLiteDatabase.execSQL("ALTER TABLE KEEPON ADD COLUMN DownloadedSongCount INTEGER NOT NULL DEFAULT 0");
      Store.this.updateKeeponDownloadSongCounts(paramSQLiteDatabase);
    }

    private void upgradeFrom49(SQLiteDatabase paramSQLiteDatabase, int paramInt)
    {
      if (paramInt < 49)
        return;
      try
      {
        Store.safeClose(paramSQLiteDatabase.rawQuery("SELECT SONG_COUNT, DOWNLOADED_SONG_COUNT FROM KEEPON LIMIT 1", null));
        i = 1;
        if (i == 0)
          return;
        Log.w("MusicStore", "Renaming KeepOn columns");
        paramSQLiteDatabase.execSQL("ALTER TABLE KEEPON RENAME TO temp_49Upgrade_KEEPON");
        paramSQLiteDatabase.execSQL("CREATE TABLE KEEPON(KeepOnId INTEGER PRIMARY KEY AUTOINCREMENT, ListId INTEGER UNIQUE, AlbumId INTEGER UNIQUE, ArtistId INTEGER UNIQUE, DateAdded INTEGER ,AutoListId INTEGER ,SongCount INTEGER NOT NULL DEFAULT 0,DownloadedSongCount INTEGER NOT NULL DEFAULT 0);");
        paramSQLiteDatabase.execSQL("INSERT INTO KEEPON(KeepOnId, ListId, AlbumId, ArtistId, DateAdded, AutoListId, SongCount, DownloadedSongCount) SELECT KeepOnId, ListId, AlbumId, ArtistId, DateAdded, AutoListId, SONG_COUNT, DOWNLOADED_SONG_COUNT FROM temp_49Upgrade_KEEPON");
        paramSQLiteDatabase.execSQL("DROP TABLE temp_49Upgrade_KEEPON");
        return;
      }
      catch (Exception localException)
      {
        while (true)
        {
          Log.w("MusicStore", "Old column names not found", localException);
          int i = 0;
        }
      }
    }

    private void upgradeFrom50(SQLiteDatabase paramSQLiteDatabase)
    {
      OldMusicFile50.fixUnknownAlbumsAndArtists50(paramSQLiteDatabase);
      String[] arrayOfString1 = new String[2];
      arrayOfString1[0] = "0";
      arrayOfString1[1] = "431126106";
      int i = paramSQLiteDatabase.delete("SHOULDKEEPON", "KeepOnId IN (SELECT KeepOnId FROM KEEPON WHERE AlbumId=? OR AlbumId=? )", arrayOfString1);
      String[] arrayOfString2 = new String[2];
      arrayOfString2[0] = "0";
      arrayOfString2[1] = "431126106";
      int j = paramSQLiteDatabase.delete("KEEPON", "AlbumId=? OR AlbumId=? ", arrayOfString2);
      int k = paramSQLiteDatabase.delete("SHOULDKEEPON", "KeepOnId NOT IN (SELECT KeepOnId FROM KEEPON)", null);
    }

    private void upgradeFrom51(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN CacheDate INTEGER NOT NULL DEFAULT 0");
      paramSQLiteDatabase.execSQL("UPDATE MUSIC SET CacheDate=LastPlayDate WHERE LastPlayDate!=0 AND LocalCopyType IN (100,200)");
    }

    private void upgradeFrom52(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN ArtistArtLocation TEXT");
    }

    private void upgradeFrom53(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE temp_52Upgrade_LISTITEMS(Id INTEGER PRIMARY KEY AUTOINCREMENT, ClientId TEXT, MusicId INTEGER NOT NULL REFERENCES MUSIC, ListId INTEGER NOT NULL REFERENCES LISTS, ClientPosition INTEGER NOT NULL, ServerOrder TEXT DEFAULT '', PlayGroupId INTEGER NOT NULL DEFAULT 0, SourceAccount INTEGER, SourceId TEXT, _sync_version TEXT, _sync_dirty INTEGER NOT NULL DEFAULT 0);");
      String str1 = " JOIN MUSIC ON (" + "LISTITEMS.MusicSourceAccount=MUSIC.SourceAccount AND LISTITEMS.MusicSourceId=MUSIC.SourceId" + ") ";
      String str2 = "INSERT INTO temp_52Upgrade_LISTITEMS(Id, ClientId, MusicId,ListId, ClientPosition, ServerOrder, PlayGroupId, SourceAccount, SourceId, _sync_version, _sync_dirty) SELECT LISTITEMS.Id, LISTITEMS.ClientId, MUSIC.Id, LISTITEMS.ListId, LISTITEMS.ClientPosition, LISTITEMS.ServerOrder, LISTITEMS.ServerPosition,LISTITEMS.SourceAccount,LISTITEMS.SourceId,LISTITEMS._sync_version,LISTITEMS._sync_dirty FROM LISTITEMS" + str1;
      paramSQLiteDatabase.execSQL(str2);
      paramSQLiteDatabase.execSQL("DROP TABLE LISTITEMS");
      paramSQLiteDatabase.execSQL("ALTER TABLE temp_52Upgrade_LISTITEMS RENAME TO LISTITEMS");
      paramSQLiteDatabase.execSQL("CREATE INDEX LISTITEMS_ORDER_INDEX ON LISTITEMS (ListId, ServerOrder, ClientPosition);");
      paramSQLiteDatabase.execSQL("CREATE INDEX LISTITEMS_SYNC_INDEX on LISTITEMS(SourceAccount,SourceId)");
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN SourceType INTEGER NOT NULL DEFAULT 0");
      paramSQLiteDatabase.execSQL("UPDATE MUSIC SET SourceType=1 WHERE MUSIC.SourceAccount=0");
      paramSQLiteDatabase.execSQL("UPDATE MUSIC SET SourceType=2 WHERE MUSIC.SourceAccount!=0 AND TrackType!=4 AND TrackType!=5");
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN Nid TEXT");
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN ClientId TEXT");
      StringBuilder localStringBuilder1 = new StringBuilder().append("UPDATE MUSIC SET SourceType=3, Nid=SourceId, ClientId='");
      String str3 = Store.generateClientId();
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str3).append("'").append(" WHERE ").append("SourceAccount").append("!=");
      String str4 = MusicFile.MEDIA_STORE_SOURCE_ACCOUNT_AS_STRING;
      String str5 = str4 + " AND " + "TrackType" + "=" + 4 + " or " + "TrackType" + "=" + 5;
      paramSQLiteDatabase.execSQL(str5);
    }

    private void upgradeFrom54(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE RADIO_STATIONS(Id INTEGER PRIMARY KEY AUTOINCREMENT, ClientId TEXT NOT NULL, Name TEXT NOT NULL, Description TEXT, RecentTimestamp INTEGER NOT NULL DEFAULT 0, ArtworkLocation TEXT, SeedSourceId TEXT NOT NULL, SeedSourceType INTEGER NOT NULL DEFAULT 0, SourceAccount INTEGER NOT NULL, SourceId TEXT , _sync_version TEXT, _sync_dirty INTEGER NOT NULL DEFAULT 0 );");
      paramSQLiteDatabase.execSQL("CREATE TABLE RADIO_STATION_TOMBSTONES(Id INTEGER PRIMARY KEY AUTOINCREMENT, SourceAccount INTEGER, SourceId TEXT, _sync_version TEXT);");
    }

    private void upgradeFrom55(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE RADIO_STATIONS ADD COLUMN ArtworkType INTEGER NOT NULL DEFAULT 0");
    }

    private void upgradeFrom56(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE RECENT ADD COLUMN RecentRadioId INTEGER");
      paramSQLiteDatabase.execSQL("CREATE UNIQUE INDEX RECENT_RADIO_ID_IDX ON RECENT (RecentRadioId)");
    }

    private void upgradeFrom57(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("DROP TABLE RADIO_STATIONS");
      paramSQLiteDatabase.execSQL("CREATE TABLE RADIO_STATIONS(Id INTEGER PRIMARY KEY AUTOINCREMENT, ClientId TEXT NOT NULL, Name TEXT NOT NULL, Description TEXT, RecentTimestamp INTEGER NOT NULL DEFAULT 0, ArtworkLocation TEXT, ArtworkType INTEGER NOT NULL DEFAULT 0, SeedSourceId TEXT NOT NULL, SeedSourceType INTEGER NOT NULL DEFAULT 0, SourceAccount INTEGER NOT NULL, SourceId TEXT , _sync_version TEXT, _sync_dirty INTEGER NOT NULL DEFAULT 0, UNIQUE( SeedSourceId,SeedSourceType) ON CONFLICT IGNORE);");
    }

    private void upgradeFrom58(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN CpData BLOB");
    }

    private void upgradeFrom59(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE CONFIG(id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT NOT NULL, Value TEXT, UNIQUE(Name) ON CONFLICT REPLACE);");
    }

    private void upgradeFrom60(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE RECENT ADD COLUMN RecentNautilusAlbum TEXT");
      paramSQLiteDatabase.execSQL("ALTER TABLE RECENT ADD COLUMN RecentNautilusAlbumId TEXT");
      paramSQLiteDatabase.execSQL("ALTER TABLE RECENT ADD COLUMN RecentNautilusAlbumArt TEXT");
      paramSQLiteDatabase.execSQL("ALTER TABLE RECENT ADD COLUMN RecentNautilusAlbumArtist TEXT");
      paramSQLiteDatabase.execSQL("CREATE UNIQUE INDEX RECENT_NAUTILUS_ALBUM_ID_IDX ON RECENT (RecentNautilusAlbumId)");
    }

    private void upgradeFrom61(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("DROP TABLE CONFIG");
    }

    private void upgradeFrom62(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN ArtistMetajamId TEXT");
    }

    private void upgradeFrom63(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE LISTS ADD COLUMN ShareToken TEXT");
      paramSQLiteDatabase.execSQL("ALTER TABLE LISTS ADD COLUMN OwnerName TEXT");
      paramSQLiteDatabase.execSQL("ALTER TABLE LISTS ADD COLUMN Description TEXT");
    }

    private void upgradeFrom64(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS ARTWORK_CACHE");
      paramSQLiteDatabase.execSQL("CREATE TABLE ARTWORK_CACHE(RemoteLocation TEXT PRIMARY KEY,Timestamp INTEGER, FileSize INTEGER, LocalLocation STRING,LocalLocationStorageType INTEGER NOT NULL DEFAULT 0)");
      paramSQLiteDatabase.execSQL("create index ARTWORK_CACHE_TIMESTAMP_INDEX on ARTWORK_CACHE(Timestamp)");
    }

    private void upgradeFrom65(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE LISTS ADD COLUMN OwnerProfilePhotoUrl TEXT");
    }

    private void upgradeFrom66()
    {
      this.mFullResync = true;
    }

    private void upgradeFrom67(SQLiteDatabase paramSQLiteDatabase)
    {
    }

    private void upgradeFrom68(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("DROP INDEX IF EXISTS MUSIC_TRACK_TYPE_INDEX");
      paramSQLiteDatabase.execSQL("CREATE INDEX MUSIC_ALBUM_METAJAM_ID_INDEX on MUSIC(StoreAlbumId)");
    }

    private void upgradeFrom69(SQLiteDatabase paramSQLiteDatabase)
    {
      ContentValues localContentValues = new ContentValues(2);
      Integer localInteger1 = Integer.valueOf(5);
      localContentValues.put("TrackType", localInteger1);
      Integer localInteger2 = Integer.valueOf(1);
      localContentValues.put("_sync_dirty", localInteger2);
      int i = paramSQLiteDatabase.update("MUSIC", localContentValues, "Domain=0 AND TrackType=4", null);
      int j = paramSQLiteDatabase.delete("RECENT", "RecentAlbumId NOT NULL AND NOT EXISTS(SELECT MUSIC.AlbumId FROM MUSIC WHERE MUSIC.AlbumId=RecentAlbumId AND Domain=0)", null);
    }

    private void upgradeFrom70(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("create index MUSIC_LOCAL_COPY_TYPE_INDEX on MUSIC(LocalCopyType)");
      paramSQLiteDatabase.execSQL("CREATE INDEX LISTITEMS_MUSICID_INDEX on LISTITEMS(MusicId)");
    }

    private void upgradeFrom71(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("DROP INDEX IF EXISTS MUSIC_TRACKS_BY_ARTIST_SORT_INDEX");
      paramSQLiteDatabase.execSQL("DROP INDEX IF EXISTS MUSIC_TRACKS_BY_ALBUM_SORT_INDEX");
      paramSQLiteDatabase.execSQL("DROP INDEX IF EXISTS MUSIC_TRACKS_BY_NAME_SORT_INDEX");
      paramSQLiteDatabase.execSQL("CREATE INDEX MUSIC_DOMAIN_CANONICALNAME_SONGID_INDEX ON MUSIC(Domain, CanonicalName, SongId)");
      paramSQLiteDatabase.execSQL("CREATE INDEX MUSIC_DOMAIN_CANONICAL_ALBUM_ARTIST_INDEX ON MUSIC(Domain, CanonicalAlbumArtist)");
      paramSQLiteDatabase.execSQL("ALTER TABLE MUSIC ADD COLUMN AlbumIdSourceText TEXT");
      Store.updateAlbumIdSourceText(paramSQLiteDatabase);
      paramSQLiteDatabase.execSQL("CREATE INDEX MUSIC_DOMAIN_ALBUMID_SOURCE_TEXT_INDEX ON MUSIC(Domain, AlbumIdSourceText, FileDate)");
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      if (Store.LOGV)
        Log.d("MusicStore", "Database created");
      String str = paramSQLiteDatabase.getPath();
      this.mDBPath = str;
      Store.this.mSyncHelper.createDatabase(paramSQLiteDatabase);
      onUpgrade(paramSQLiteDatabase, -1, 72);
    }

    public void onDowngrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      MediaStoreImporter localMediaStoreImporter = Store.this.mMediaStoreImporter;
      Context localContext = Store.this.mContext;
      localMediaStoreImporter.invalidateMediaStoreImport(localContext);
      String str = paramSQLiteDatabase.getPath();
      throw new DowngradeException(str);
    }

    public void onOpen(SQLiteDatabase paramSQLiteDatabase)
    {
      MusicUtils.checkMainThread(Store.this.mContext, "Database opened on main thread");
      Store.configureDatabaseConnection(paramSQLiteDatabase);
      paramSQLiteDatabase.execSQL("CREATE TEMP VIEW USER_MUSIC AS SELECT * FROM MUSIC WHERE Domain=0");
      PlayQueue localPlayQueue = PlayQueue.initQueue(paramSQLiteDatabase);
      super.onOpen(paramSQLiteDatabase);
      if (!Store.LOGV)
        return;
      Log.d("MusicStore", "Database opened");
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      String str1 = paramSQLiteDatabase.getPath();
      this.mDBPath = str1;
      long l1 = SystemClock.uptimeMillis();
      int i = paramInt1;
      try
      {
        MusicUtils.checkMainThread(Store.this.mContext, "Database upgraded on main thread");
        if (paramInt1 < 25)
        {
          this.mResetMediaStoreImport = true;
          paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS XFILESMUSIC");
          paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS XFILES");
          paramSQLiteDatabase.execSQL("DROP INDEX IF EXISTS XFILESMUSIC_SONGID_INDEX");
          paramSQLiteDatabase.execSQL("DROP INDEX IF EXISTS XFILESMUSIC_ALBUMID_INDEX");
          paramSQLiteDatabase.execSQL("DROP INDEX IF EXISTS XFILESMUSIC_ALBUMARTISTID_INDEX");
          paramSQLiteDatabase.execSQL("DROP INDEX IF EXISTS XFILESMUSIC_GENREID_INDEX");
          paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS LIBRARIES");
          paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS MUSIC");
          paramSQLiteDatabase.execSQL("CREATE TABLE MUSIC(Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, SourceAccount INTEGER NOT NULL, SourceId TEXT NOT NULL, SourcePath TEXT, Size INTEGER NOT NULL, FileType INTEGER NOT NULL, FileDate INTEGER NOT NULL, LocalCopyPath TEXT, LocalCopyType INTEGER NOT NULL, PlayCount INTEGER NOT NULL DEFAULT 0, LastPlayDate INTEGER NOT NULL DEFAULT 0, Title TEXT NOT NULL, Album TEXT, Artist TEXT, AlbumArtist TEXT, AlbumArtistOrigin INTEGER, Composer TEXT , Genre TEXT, Year INTEGER, Duration INTEGER, TrackCount INTEGER, TrackNumber INTEGER, DiscCount INTEGER, DiscNumber INTEGER, Compilation INTEGER, BitRate INTEGER, AlbumArtLocation TEXT, SongId INTEGER NOT NULL, AlbumId INTEGER NOT NULL DEFAULT 0, AlbumArtistId INTEGER NOT NULL DEFAULT 0, GenreId INTEGER NOT NULL DEFAULT 0, CanonicalName TEXT NOT NULL, CanonicalAlbum TEXT, CanonicalAlbumArtist TEXT, CanonicalGenre TEXT, _sync_dirty INTEGER NOT NULL DEFAULT 0, _sync_version TEXT, UNIQUE( SourceAccount,SourceId) ON CONFLICT REPLACE);");
          paramSQLiteDatabase.execSQL("create index MUSIC_SONGID_INDEX on MUSIC(SongId)");
          paramSQLiteDatabase.execSQL("create index MUSIC_ALBUMID_INDEX on MUSIC(AlbumId)");
          paramSQLiteDatabase.execSQL("create index MUSIC_ALBUMARTISTID_INDEX on MUSIC(AlbumArtistId)");
          paramSQLiteDatabase.execSQL("create index MUSIC_GENREID_INDEX on MUSIC(GenreId)");
          paramSQLiteDatabase.execSQL("create index MUSIC_CANONICAL_NAME_INDEX on MUSIC(CanonicalName)");
          paramSQLiteDatabase.execSQL("create index MUSIC_CANONICAL_ALBUM_CANONICAL_NAME_INDEX on MUSIC(CanonicalAlbum, CanonicalName)");
          paramSQLiteDatabase.execSQL("create index MUSIC_CANONICAL_ARTIST_CANONICAL_NAME_INDEX on MUSIC(CanonicalAlbumArtist, CanonicalName)");
          paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS LISTS");
          paramSQLiteDatabase.execSQL("CREATE TABLE LISTS(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT NOT NULL, SourceAccount INTEGER, SourceId TEXT, _sync_version TEXT, _sync_dirty INTEGER NOT NULL DEFAULT 0, MediaStoreId INTEGER);");
          paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS LIST_TOMBSTONES");
          paramSQLiteDatabase.execSQL("CREATE TABLE LIST_TOMBSTONES(Id INTEGER PRIMARY KEY AUTOINCREMENT, SourceAccount INTEGER, SourceId TEXT, _sync_version TEXT);");
          paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS LISTITEMS");
          paramSQLiteDatabase.execSQL("CREATE TABLE LISTITEMS(Id INTEGER PRIMARY KEY AUTOINCREMENT, ListId INTEGER NOT NULL REFERENCES LISTS, MusicSourceAccount INTEGER NOT NULL, MusicSourceId TEXT NOT NULL, ClientPosition INTEGER NOT NULL, ServerPosition INTEGER NOT NULL DEFAULT 0, SourceAccount INTEGER, SourceId TEXT, _sync_version TEXT, _sync_dirty INTEGER NOT NULL DEFAULT 0);");
          paramSQLiteDatabase.execSQL("CREATE INDEX LISTITEMS_ORDER_INDEX ON LISTITEMS (ListId, ServerPosition, ClientPosition);");
          paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS LISTITEM_TOMBSTONES");
          paramSQLiteDatabase.execSQL("CREATE TABLE LISTITEM_TOMBSTONES(Id INTEGER PRIMARY KEY AUTOINCREMENT, SourceAccount INTEGER, SourceId TEXT, _sync_version TEXT);");
          paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS KEEPON");
          paramSQLiteDatabase.execSQL("CREATE TABLE KEEPON(KeepOnId INTEGER PRIMARY KEY AUTOINCREMENT, ListId INTEGER UNIQUE, AlbumId INTEGER UNIQUE, ArtistId INTEGER UNIQUE, DateAdded INTEGER );");
          paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS ARTWORK");
          paramSQLiteDatabase.execSQL("CREATE TABLE ARTWORK(AlbumId INTEGER PRIMARY KEY,LocalLocation STRING)");
          paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS SHOULDKEEPON");
          paramSQLiteDatabase.execSQL("CREATE TABLE SHOULDKEEPON(\nMusicId INTEGER,\nKeepOnId INTEGER,\nFOREIGN KEY (KeepOnId) REFERENCES KEEPON (KeepOnId) ON DELETE CASCADE,\nFOREIGN KEY (MusicId) REFERENCES MUSIC (Id) ON DELETE CASCADE,\nUNIQUE (MusicId, KeepOnId) ON CONFLICT IGNORE)");
          paramSQLiteDatabase.execSQL("CREATE INDEX SHOULDKEEPON_MusicId ON SHOULDKEEPON (MusicId);");
          paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS RECENT");
          paramSQLiteDatabase.execSQL("CREATE TABLE RECENT(RecentId INTEGER PRIMARY KEY AUTOINCREMENT, ItemDate INTEGER, RecentListId INTEGER UNIQUE ON CONFLICT REPLACE, RecentAlbumId INTEGER UNIQUE ON CONFLICT REPLACE);");
          paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS _sync_state");
          SyncStateContentProviderHelper localSyncStateContentProviderHelper = Store.this.mSyncHelper;
          SQLiteDatabase localSQLiteDatabase1 = paramSQLiteDatabase;
          localSyncStateContentProviderHelper.createDatabase(localSQLiteDatabase1);
          paramInt1 = 25;
        }
        if (paramInt1 < 26)
        {
          upgradeFrom25(paramSQLiteDatabase);
          paramInt1 = 26;
        }
        if (paramInt1 < 27)
        {
          upgradeFrom26(paramSQLiteDatabase);
          paramInt1 = 26;
        }
        if (paramInt1 < 28)
        {
          upgradeFrom27(paramSQLiteDatabase);
          paramInt1 = 27;
        }
        if (paramInt1 < 29)
        {
          upgradeFrom28(paramSQLiteDatabase);
          paramInt1 = 28;
        }
        if (paramInt1 < 30)
        {
          upgradeFrom29(paramSQLiteDatabase);
          paramInt1 = 29;
        }
        if (paramInt1 < 31)
        {
          upgradeFrom30(paramSQLiteDatabase);
          paramInt1 = 30;
        }
        if (paramInt1 < 32)
        {
          upgradeFrom31(paramSQLiteDatabase);
          paramInt1 = 31;
        }
        if (paramInt1 < 33)
        {
          upgradeFrom32(paramSQLiteDatabase);
          paramInt1 = 32;
        }
        if (paramInt1 < 34)
        {
          upgradeFrom33(paramSQLiteDatabase);
          paramInt1 = 33;
        }
        if (paramInt1 < 35)
        {
          upgradeFrom34(paramSQLiteDatabase);
          paramInt1 = 34;
        }
        if (paramInt1 < 36)
        {
          upgradeFrom35(paramSQLiteDatabase);
          paramInt1 = 35;
        }
        if (paramInt1 < 37)
        {
          upgradeFrom36(paramSQLiteDatabase);
          paramInt1 = 36;
        }
        if (paramInt1 < 38)
        {
          upgradeFrom37(paramSQLiteDatabase);
          paramInt1 = 37;
        }
        if (paramInt1 < 39)
        {
          upgradeFrom38(paramSQLiteDatabase);
          paramInt1 = 38;
        }
        if (paramInt1 < 40)
        {
          upgradeFrom39(paramSQLiteDatabase);
          paramInt1 = 39;
        }
        if (paramInt1 < 41)
        {
          upgradeFrom40(paramSQLiteDatabase);
          paramInt1 = 40;
        }
        if (paramInt1 < 42)
        {
          upgradeFrom41(paramSQLiteDatabase);
          paramInt1 = 41;
        }
        if (paramInt1 < 43)
        {
          upgradeFrom42(paramSQLiteDatabase);
          paramInt1 = 42;
        }
        if (paramInt1 < 44)
        {
          upgradeFrom43(paramSQLiteDatabase);
          paramInt1 = 43;
        }
        if (paramInt1 < 45)
        {
          upgradeFrom44(paramSQLiteDatabase);
          paramInt1 = 44;
        }
        if (paramInt1 < 46)
        {
          upgradeFrom45(paramSQLiteDatabase);
          paramInt1 = 45;
        }
        if (paramInt1 < 47)
        {
          upgradeFrom46(paramSQLiteDatabase);
          paramInt1 = 46;
        }
        if (paramInt1 < 48)
        {
          upgradeFrom47(paramSQLiteDatabase);
          paramInt1 = 47;
        }
        if (paramInt1 < 49)
        {
          upgradeFrom48(paramSQLiteDatabase);
          paramInt1 = 48;
        }
        if (paramInt1 < 50)
        {
          SQLiteDatabase localSQLiteDatabase2 = paramSQLiteDatabase;
          upgradeFrom49(localSQLiteDatabase2, i);
          paramInt1 = 49;
        }
        if (paramInt1 < 51)
        {
          upgradeFrom50(paramSQLiteDatabase);
          paramInt1 = 50;
        }
        if (paramInt1 < 52)
        {
          upgradeFrom51(paramSQLiteDatabase);
          paramInt1 = 51;
        }
        if (paramInt1 < 53)
        {
          upgradeFrom52(paramSQLiteDatabase);
          paramInt1 = 52;
        }
        if (paramInt1 < 54)
        {
          upgradeFrom53(paramSQLiteDatabase);
          paramInt1 = 53;
        }
        if (paramInt1 < 55)
        {
          upgradeFrom54(paramSQLiteDatabase);
          paramInt1 = 54;
        }
        if (paramInt1 < 56)
        {
          upgradeFrom55(paramSQLiteDatabase);
          paramInt1 = 55;
        }
        if (paramInt1 < 57)
        {
          upgradeFrom56(paramSQLiteDatabase);
          paramInt1 = 56;
        }
        if (paramInt1 < 58)
        {
          upgradeFrom57(paramSQLiteDatabase);
          paramInt1 = 57;
        }
        if (paramInt1 < 59)
        {
          upgradeFrom58(paramSQLiteDatabase);
          paramInt1 = 58;
        }
        if (paramInt1 < 60)
        {
          upgradeFrom59(paramSQLiteDatabase);
          paramInt1 = 59;
        }
        if (paramInt1 < 61)
        {
          upgradeFrom60(paramSQLiteDatabase);
          paramInt1 = 60;
        }
        if (paramInt1 < 62)
        {
          upgradeFrom61(paramSQLiteDatabase);
          paramInt1 = 61;
        }
        if (paramInt1 < 63)
        {
          upgradeFrom62(paramSQLiteDatabase);
          paramInt1 = 62;
        }
        if (paramInt1 < 64)
        {
          upgradeFrom63(paramSQLiteDatabase);
          paramInt1 = 63;
        }
        if (paramInt1 < 65)
        {
          upgradeFrom64(paramSQLiteDatabase);
          paramInt1 = 64;
        }
        if (paramInt1 < 66)
        {
          upgradeFrom65(paramSQLiteDatabase);
          paramInt1 = 65;
        }
        if (paramInt1 < 67)
        {
          upgradeFrom66();
          paramInt1 = 66;
        }
        if (paramInt1 < 68)
        {
          upgradeFrom67(paramSQLiteDatabase);
          paramInt1 = 67;
        }
        if (paramInt1 < 69)
        {
          upgradeFrom68(paramSQLiteDatabase);
          paramInt1 = 68;
        }
        if (paramInt1 < 70)
        {
          upgradeFrom69(paramSQLiteDatabase);
          paramInt1 = 69;
        }
        if (paramInt1 < 71)
        {
          upgradeFrom70(paramSQLiteDatabase);
          paramInt1 = 70;
        }
        if (paramInt1 < 72)
          upgradeFrom71(paramSQLiteDatabase);
        postImportProcessing(paramSQLiteDatabase);
        long l2 = SystemClock.uptimeMillis() - l1;
        String str2 = "Upgrade from " + i + " to " + 72 + " took " + l2 + " ms";
        Log.i("MusicStore", str2);
        if (i <= -1)
          return;
        int j = Store.getDbTableSize(paramSQLiteDatabase, "MUSIC");
        int k = Store.getDbTableSize(paramSQLiteDatabase, "LISTITEMS");
        int m = Store.getDbTableSize(paramSQLiteDatabase, "LISTS");
        MusicEventLogger localMusicEventLogger1 = MusicEventLogger.getInstance(Store.this.mContext);
        Object[] arrayOfObject1 = new Object[12];
        arrayOfObject1[0] = "databaseOldVersion";
        Integer localInteger1 = Integer.valueOf(i);
        arrayOfObject1[1] = localInteger1;
        arrayOfObject1[2] = "databaseNewVersion";
        Integer localInteger2 = Integer.valueOf(72);
        arrayOfObject1[3] = localInteger2;
        arrayOfObject1[4] = "databaseUpgradeTimeMillisec";
        Long localLong1 = Long.valueOf(l2);
        arrayOfObject1[5] = localLong1;
        arrayOfObject1[6] = "databaseMusicTableSize";
        Integer localInteger3 = Integer.valueOf(j);
        arrayOfObject1[7] = localInteger3;
        arrayOfObject1[8] = "databaseListItemsTableSize";
        Integer localInteger4 = Integer.valueOf(k);
        arrayOfObject1[9] = localInteger4;
        arrayOfObject1[10] = "databaseListsTableSize";
        Integer localInteger5 = Integer.valueOf(m);
        arrayOfObject1[11] = localInteger5;
        localMusicEventLogger1.trackEvent("databaseUpgrade", arrayOfObject1);
        return;
      }
      finally
      {
        long l3 = SystemClock.uptimeMillis() - l1;
        String str3 = "Upgrade from " + i + " to " + 72 + " took " + l3 + " ms";
        Log.i("MusicStore", str3);
        if (i > -1)
        {
          int n = Store.getDbTableSize(paramSQLiteDatabase, "MUSIC");
          int i1 = Store.getDbTableSize(paramSQLiteDatabase, "LISTITEMS");
          int i2 = Store.getDbTableSize(paramSQLiteDatabase, "LISTS");
          MusicEventLogger localMusicEventLogger2 = MusicEventLogger.getInstance(Store.this.mContext);
          Object[] arrayOfObject2 = new Object[12];
          arrayOfObject2[0] = "databaseOldVersion";
          Integer localInteger6 = Integer.valueOf(i);
          arrayOfObject2[1] = localInteger6;
          arrayOfObject2[2] = "databaseNewVersion";
          Integer localInteger7 = Integer.valueOf(72);
          arrayOfObject2[3] = localInteger7;
          arrayOfObject2[4] = "databaseUpgradeTimeMillisec";
          Long localLong2 = Long.valueOf(l3);
          arrayOfObject2[5] = localLong2;
          arrayOfObject2[6] = "databaseMusicTableSize";
          Integer localInteger8 = Integer.valueOf(n);
          arrayOfObject2[7] = localInteger8;
          arrayOfObject2[8] = "databaseListItemsTableSize";
          Integer localInteger9 = Integer.valueOf(i1);
          arrayOfObject2[9] = localInteger9;
          arrayOfObject2[10] = "databaseListsTableSize";
          Integer localInteger10 = Integer.valueOf(i2);
          arrayOfObject2[11] = localInteger10;
          localMusicEventLogger2.trackEvent("databaseUpgrade", arrayOfObject2);
        }
      }
    }
  }

  public static enum ItemType
  {
    static
    {
      ItemType[] arrayOfItemType = new ItemType[2];
      ItemType localItemType1 = LOCAL;
      arrayOfItemType[0] = localItemType1;
      ItemType localItemType2 = REMOTE;
      arrayOfItemType[1] = localItemType2;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.Store
 * JD-Core Version:    0.6.2
 */