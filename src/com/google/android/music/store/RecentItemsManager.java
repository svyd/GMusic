package com.google.android.music.store;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.music.MusicUserContentBinder;
import com.google.android.music.cloudclient.AlbumJson;
import com.google.android.music.medialist.AlbumSongList;
import com.google.android.music.medialist.ArtistSongList;
import com.google.android.music.medialist.NautilusAlbumSongList;
import com.google.android.music.medialist.PlaylistSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.async.AsyncWorkers;
import java.util.Date;
import java.util.HashMap;

public class RecentItemsManager
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.STORE);
  private static final int UPDATE_RECENT_ITEMS_MESSAGE_TYPE = AsyncWorkers.getUniqueMessageType(AsyncWorkers.sBackendServiceWorker);

  public static void addCreatedPlaylist(Context paramContext, long paramLong)
  {
    boolean bool = addPlaylist(paramContext, paramLong, 6);
  }

  public static void addModifiedPlaylist(Context paramContext, long paramLong)
  {
    boolean bool = addPlaylist(paramContext, paramLong, 5);
  }

  private static void addNautilusAlbum(SQLiteDatabase paramSQLiteDatabase, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("RecentNautilusAlbumId", paramString1);
    localContentValues.put("RecentNautilusAlbum", paramString2);
    localContentValues.put("RecentNautilusAlbumArt", paramString3);
    localContentValues.put("RecentNautilusAlbumArtist", paramString4);
    Long localLong = Long.valueOf(new Date().getTime());
    localContentValues.put("ItemDate", localLong);
    Integer localInteger = Integer.valueOf(paramInt);
    localContentValues.put("RecentReason", localInteger);
    if (paramSQLiteDatabase.insertWithOnConflict("RECENT", null, localContentValues, 5) < 1L)
      int i = Log.wtf("RecentItemsManager", "Failed to insert radio into recent");
    enforceMaxItemsLimit(paramSQLiteDatabase);
  }

  private static boolean addNautilusAlbum(Context paramContext, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt)
  {
    boolean bool1;
    if ((TextUtils.isEmpty(paramString1)) || (TextUtils.isEmpty(paramString2)) || (TextUtils.isEmpty(paramString3)) || (TextUtils.isEmpty(paramString4)))
    {
      String str1 = "Missing fields. Cannot add nautilus item to recent:" + paramString1;
      int i = Log.e("RecentItemsManager", str1);
      bool1 = false;
    }
    while (true)
    {
      return bool1;
      boolean bool2 = false;
      Store localStore = Store.getInstance(paramContext);
      SQLiteDatabase localSQLiteDatabase = Store.getInstance(paramContext).beginWriteTxn();
      String str2 = paramString1;
      String str3 = paramString2;
      String str4 = paramString3;
      String str5 = paramString4;
      int j = paramInt;
      try
      {
        addNautilusAlbum(localSQLiteDatabase, str2, str3, str4, str5, j);
        bool1 = true;
        localStore.endWriteTxn(localSQLiteDatabase, bool1);
        if (!bool1)
          continue;
        notifyContentChange(paramContext);
      }
      finally
      {
        localStore.endWriteTxn(localSQLiteDatabase, bool2);
      }
    }
  }

  public static boolean addNewReleaseNautilusAlbum(Context paramContext, AlbumJson paramAlbumJson)
  {
    String str1 = paramAlbumJson.mAlbumId;
    String str2 = paramAlbumJson.mName;
    String str3 = paramAlbumJson.mAlbumArtRef;
    String str4 = paramAlbumJson.mAlbumArtist;
    return addNautilusAlbum(paramContext, str1, str2, str3, str4, 4);
  }

  private static void addPlayedAlbum(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    ContentValues localContentValues = new ContentValues();
    Long localLong1 = Long.valueOf(paramLong);
    localContentValues.put("RecentAlbumId", localLong1);
    Long localLong2 = Long.valueOf(new Date().getTime());
    localContentValues.put("ItemDate", localLong2);
    Integer localInteger = Integer.valueOf(1);
    localContentValues.put("RecentReason", localInteger);
    long l = paramSQLiteDatabase.insert("RECENT", null, localContentValues);
    enforceMaxItemsLimit(paramSQLiteDatabase);
  }

  private static boolean addPlayedAlbum(Context paramContext, long paramLong)
  {
    boolean bool1 = false;
    if (paramLong < 0L)
    {
      String str = "Cannot add album to recent. Invalid id: " + paramLong;
      int i = Log.e("RecentItemsManager", str);
    }
    while (true)
    {
      return bool1;
      if (paramLong == 0L)
        continue;
      boolean bool2 = false;
      Store localStore = Store.getInstance(paramContext);
      SQLiteDatabase localSQLiteDatabase = Store.getInstance(paramContext).beginWriteTxn();
      try
      {
        addPlayedAlbum(localSQLiteDatabase, paramLong);
        bool1 = true;
        localStore.endWriteTxn(localSQLiteDatabase, bool1);
        if (!bool1)
          continue;
        notifyContentChange(paramContext);
      }
      finally
      {
        localStore.endWriteTxn(localSQLiteDatabase, bool2);
      }
    }
  }

  private static boolean addPlayedArtist(Context paramContext, long paramLong)
  {
    boolean bool1;
    if (paramLong < 0L)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Cannot add artist to recent. Invalid id: ");
      long l1 = paramLong;
      String str1 = l1;
      int i = Log.e("RecentItemsManager", str1);
      bool1 = false;
    }
    while (true)
    {
      return bool1;
      if (paramLong == 0L)
      {
        bool1 = false;
        continue;
      }
      bool1 = false;
      Store localStore = Store.getInstance(paramContext);
      SQLiteDatabase localSQLiteDatabase = Store.getInstance(paramContext).beginWriteTxn();
      try
      {
        String str2 = Long.toString(paramLong);
        String[] arrayOfString1 = new String[2];
        arrayOfString1[0] = str2;
        arrayOfString1[1] = str2;
        String[] arrayOfString2 = new String[1];
        arrayOfString2[0] = "AlbumId";
        localCursor = localSQLiteDatabase.query("MUSIC", arrayOfString2, "AlbumArtistId=? OR ArtistId=?", arrayOfString1, null, null, "CanonicalName", "1");
        if ((localCursor != null) && (localCursor.moveToFirst()))
        {
          long l2 = localCursor.getLong(0);
          addPlayedAlbum(localSQLiteDatabase, l2);
        }
        Store.safeClose(localCursor);
        boolean bool2 = bool1;
        localStore.endWriteTxn(localSQLiteDatabase, bool2);
        if (!bool1)
          continue;
        notifyContentChange(paramContext);
      }
      finally
      {
        Cursor localCursor;
        Store.safeClose(localCursor);
        boolean bool3 = bool1;
        localStore.endWriteTxn(localSQLiteDatabase, bool3);
      }
    }
  }

  private static boolean addPlayedNautilusAlbum(Context paramContext, NautilusAlbumSongList paramNautilusAlbumSongList)
  {
    String str1 = paramNautilusAlbumSongList.getNautilusId();
    String str2 = paramNautilusAlbumSongList.getName(paramContext);
    String str3 = paramNautilusAlbumSongList.getAlbumArtUrl(paramContext);
    String str4 = paramNautilusAlbumSongList.getAlbumArtist(paramContext);
    return addNautilusAlbum(paramContext, str1, str2, str3, str4, 1);
  }

  private static void addPlayedRadio(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    ContentValues localContentValues = new ContentValues();
    Long localLong1 = Long.valueOf(paramLong);
    localContentValues.put("RecentRadioId", localLong1);
    Long localLong2 = Long.valueOf(new Date().getTime());
    localContentValues.put("ItemDate", localLong2);
    Integer localInteger = Integer.valueOf(1);
    localContentValues.put("RecentReason", localInteger);
    if (paramSQLiteDatabase.insertWithOnConflict("RECENT", null, localContentValues, 5) < 1L)
      int i = Log.wtf("RecentItemsManager", "Failed to insert radio into recent");
    enforceMaxItemsLimit(paramSQLiteDatabase);
  }

  private static boolean addPlayedRadio(Context paramContext, long paramLong)
  {
    boolean bool1;
    if (paramLong <= 0L)
    {
      String str = "Cannot add radio to recent. Invalid id: " + paramLong;
      int i = Log.e("RecentItemsManager", str);
      bool1 = false;
    }
    while (true)
    {
      return bool1;
      boolean bool2 = false;
      Store localStore = Store.getInstance(paramContext);
      SQLiteDatabase localSQLiteDatabase = Store.getInstance(paramContext).beginWriteTxn();
      try
      {
        addPlayedRadio(localSQLiteDatabase, paramLong);
        bool1 = true;
        localStore.endWriteTxn(localSQLiteDatabase, bool1);
        if (!bool1)
          continue;
        notifyContentChange(paramContext);
      }
      finally
      {
        localStore.endWriteTxn(localSQLiteDatabase, bool2);
      }
    }
  }

  private static void addPlaylist(SQLiteDatabase paramSQLiteDatabase, long paramLong, int paramInt)
  {
    ContentValues localContentValues = new ContentValues();
    Long localLong1 = Long.valueOf(paramLong);
    localContentValues.put("RecentListId", localLong1);
    Long localLong2 = Long.valueOf(new Date().getTime());
    localContentValues.put("ItemDate", localLong2);
    Integer localInteger = Integer.valueOf(paramInt);
    localContentValues.put("RecentReason", localInteger);
    long l = paramSQLiteDatabase.insert("RECENT", null, localContentValues);
    enforceMaxItemsLimit(paramSQLiteDatabase);
  }

  private static boolean addPlaylist(Context paramContext, long paramLong, int paramInt)
  {
    boolean bool1;
    if (paramLong <= 0L)
    {
      String str = "Cannot add playlist to recent. Invalid id: " + paramLong;
      int i = Log.e("RecentItemsManager", str);
      bool1 = false;
    }
    while (true)
    {
      return bool1;
      boolean bool2 = false;
      Store localStore = Store.getInstance(paramContext);
      SQLiteDatabase localSQLiteDatabase = Store.getInstance(paramContext).beginWriteTxn();
      try
      {
        addPlaylist(localSQLiteDatabase, paramLong, paramInt);
        bool1 = true;
        localStore.endWriteTxn(localSQLiteDatabase, bool1);
        if (!bool1)
          continue;
        notifyContentChange(paramContext);
      }
      finally
      {
        localStore.endWriteTxn(localSQLiteDatabase, bool2);
      }
    }
  }

  private static void addRecentlyAddedAlbums(SQLiteDatabase paramSQLiteDatabase)
  {
    int i = 0;
    long l1 = 0L;
    HashMap localHashMap = new HashMap();
    String[] arrayOfString1 = new String[2];
    arrayOfString1[0] = "ItemDate";
    arrayOfString1[1] = "RecentAlbumId";
    Cursor localCursor1 = paramSQLiteDatabase.query("RECENT", arrayOfString1, null, null, null, null, "ItemDate ASC");
    if (localCursor1 != null);
    Cursor localCursor2;
    try
    {
      if (localCursor1.moveToFirst())
      {
        i = localCursor1.getCount();
        l1 = localCursor1.getLong(0);
        boolean bool1;
        do
        {
          if (!localCursor1.isNull(1))
          {
            long l2 = localCursor1.getLong(0);
            Long localLong1 = Long.valueOf(localCursor1.getLong(1));
            Long localLong2 = Long.valueOf(l2);
            Object localObject1 = localHashMap.put(localLong1, localLong2);
          }
          bool1 = localCursor1.moveToNext();
        }
        while (bool1);
      }
      Store.safeClose(localCursor1);
      String[] arrayOfString2 = new String[3];
      arrayOfString2[0] = "AlbumId";
      arrayOfString2[1] = "max(FileDate) as max_album_date";
      arrayOfString2[2] = "(max(TrackType) = 3 and min(TrackType) = 3) as isPromoContentNotSelectedByUser";
      String str1 = String.valueOf(200);
      localCursor2 = paramSQLiteDatabase.query("MUSIC", arrayOfString2, "Domain=0", null, "AlbumIdSourceText", null, "max_album_date DESC", str1);
      if (localCursor2 == null)
        return;
    }
    finally
    {
      Store.safeClose(localCursor1);
    }
    try
    {
      if (localCursor2.moveToFirst())
      {
        long l3 = localCursor2.getLong(1);
        if ((i < 200) || (l3 > l1))
        {
          boolean bool2 = localCursor2.moveToPosition(-1);
          ContentValues localContentValues1 = new ContentValues();
          int j = 0;
          while (localCursor2.moveToNext())
          {
            long l4 = localCursor2.getLong(0);
            long l5 = localCursor2.getLong(1);
            int k;
            label327: String str2;
            if (localCursor2.getInt(2) != 0)
            {
              k = 1;
              Long localLong3 = Long.valueOf(l4);
              Long localLong4 = (Long)localHashMap.get(localLong3);
              if (localLong4 != null)
              {
                long l6 = localLong4.longValue();
                if (l5 <= l6)
                  break;
              }
              else
              {
                localContentValues1.clear();
                Long localLong5 = Long.valueOf(l4);
                localContentValues1.put("RecentAlbumId", localLong5);
                Long localLong6 = Long.valueOf(l5);
                localContentValues1.put("ItemDate", localLong6);
                str2 = "Priority";
                if (k == 0);
              }
            }
            else
            {
              for (int m = 65526; ; m = 0)
              {
                Integer localInteger = Integer.valueOf(m);
                localContentValues1.put(str2, localInteger);
                SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
                ContentValues localContentValues2 = localContentValues1;
                long l7 = localSQLiteDatabase.insert("RECENT", null, localContentValues2);
                j = 1;
                break;
                k = 0;
                break label327;
              }
            }
          }
          if (j != 0)
          {
            enforceMaxItemsLimit(paramSQLiteDatabase);
            populateAlbumAddedReason(paramSQLiteDatabase);
          }
        }
      }
      Store.safeClose(localCursor2);
      int n = deleteDuplicateNautilusAlbums(paramSQLiteDatabase);
      return;
    }
    finally
    {
      Store.safeClose(localCursor2);
    }
  }

  private static void addRecentlyAddedRadio(SQLiteDatabase paramSQLiteDatabase)
  {
  }

  public static boolean addRecentlyPlayedItem(Context paramContext, SongList paramSongList)
  {
    boolean bool = false;
    if ((paramSongList instanceof PlaylistSongList))
    {
      PlaylistSongList localPlaylistSongList = (PlaylistSongList)paramSongList;
      if (includePlaylistOfType(localPlaylistSongList.getPlaylistType()))
      {
        long l1 = localPlaylistSongList.getPlaylistId();
        bool = addPlaylist(paramContext, l1, 1);
      }
    }
    while (true)
    {
      if (bool)
        MusicUserContentBinder.notifyContentChanged(paramContext);
      return bool;
      if ((paramSongList instanceof AlbumSongList))
      {
        long l2 = ((AlbumSongList)paramSongList).getAlbumId(paramContext);
        bool = addPlayedAlbum(paramContext, l2);
      }
      else if ((paramSongList instanceof ArtistSongList))
      {
        long l3 = ((ArtistSongList)paramSongList).getArtistId();
        bool = addPlayedArtist(paramContext, l3);
      }
      else if ((paramSongList instanceof NautilusAlbumSongList))
      {
        NautilusAlbumSongList localNautilusAlbumSongList = (NautilusAlbumSongList)paramSongList;
        bool = addPlayedNautilusAlbum(paramContext, localNautilusAlbumSongList);
      }
    }
  }

  public static boolean addRecentlyPlayedRadio(Context paramContext, long paramLong)
  {
    if (addPlayedRadio(paramContext, paramLong))
      MusicUserContentBinder.notifyContentChanged(paramContext);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private static int countItems(SQLiteDatabase paramSQLiteDatabase)
  {
    int i = 0;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "count(1)";
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    Cursor localCursor = localSQLiteDatabase.query("RECENT LEFT JOIN MUSIC ON (RecentAlbumId=MUSIC.AlbumId)  LEFT JOIN LISTS ON (RecentListId=LISTS.Id)  LEFT JOIN RADIO_STATIONS ON (RecentRadioId=RADIO_STATIONS.Id) ", arrayOfString, "LISTS.Id NOT NULL OR MUSIC.AlbumId NOT NULL OR RADIO_STATIONS.Id NOT NULL", null, str1, str2, str3);
    if (localCursor != null);
    try
    {
      if (localCursor.moveToFirst())
      {
        int j = localCursor.getInt(0);
        i = j;
      }
      return i;
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  public static int countRecentItems(Context paramContext)
  {
    Store localStore = Store.getInstance(paramContext);
    SQLiteDatabase localSQLiteDatabase = localStore.beginRead();
    try
    {
      int i = countItems(localSQLiteDatabase);
      int j = i;
      return j;
    }
    finally
    {
      localStore.endRead(localSQLiteDatabase);
    }
  }

  private static int deleteDuplicateNautilusAlbums(SQLiteDatabase paramSQLiteDatabase)
  {
    String[] arrayOfString = new String[1];
    String str1 = Integer.toString(5);
    arrayOfString[0] = str1;
    int i = paramSQLiteDatabase.delete("RECENT", "EXISTS (SELECT 1 FROM RECENT as r2, MUSIC WHERE  r2.RecentAlbumId = MUSIC.AlbumId AND StoreAlbumId=RECENT.RecentNautilusAlbumId AND TrackType =?  AND r2.ItemDate > RECENT.ItemDate LIMIT 1)", arrayOfString);
    if ((LOGV) && (i > 0))
    {
      String str2 = "Deleted " + i + " duplicated nautilus albums";
      int j = Log.v("RecentItemsManager", str2);
    }
    return i;
  }

  private static int deleteItemsAndCloseCursor(SQLiteDatabase paramSQLiteDatabase, Cursor paramCursor)
  {
    int i = 0;
    if (paramCursor == null);
    while (true)
    {
      return i;
      StringBuffer localStringBuffer1;
      try
      {
        if (paramCursor.getCount() <= 0)
          break label149;
        int j = paramCursor.getCount() * 2;
        localStringBuffer1 = new StringBuffer(j);
        while (paramCursor.moveToNext())
        {
          long l = paramCursor.getLong(0);
          StringBuffer localStringBuffer2 = localStringBuffer1.append(l).append(',');
        }
      }
      finally
      {
        Store.safeClose(paramCursor);
      }
      int k = localStringBuffer1.length() + -1;
      StringBuffer localStringBuffer3 = localStringBuffer1.deleteCharAt(k);
      String str = "RecentId IN (" + localStringBuffer1 + ")";
      int m = paramSQLiteDatabase.delete("RECENT", str, null);
      i = m;
      Store.safeClose(paramCursor);
      continue;
      label149: Store.safeClose(paramCursor);
    }
  }

  public static boolean deleteNautilusAlbum(Context paramContext, String paramString)
  {
    boolean bool1 = false;
    if (TextUtils.isEmpty(paramString))
    {
      String str = "The metajamId is invalid:" + paramString;
      int i = Log.e("RecentItemsManager", str);
    }
    while (true)
    {
      return bool1;
      boolean bool2 = false;
      Store localStore = Store.getInstance(paramContext);
      SQLiteDatabase localSQLiteDatabase = localStore.beginWriteTxn();
      try
      {
        String[] arrayOfString = new String[1];
        arrayOfString[0] = paramString;
        int j = localSQLiteDatabase.delete("RECENT", "RecentNautilusAlbumId=?", arrayOfString);
        if (j > 0)
        {
          bool3 = true;
          localStore.endWriteTxn(localSQLiteDatabase, bool3);
          if (bool3)
            notifyContentChange(paramContext);
          bool1 = bool3;
          continue;
        }
        boolean bool3 = false;
      }
      finally
      {
        localStore.endWriteTxn(localSQLiteDatabase, bool2);
      }
    }
  }

  private static void enforceMaxItemsLimit(SQLiteDatabase paramSQLiteDatabase)
  {
    removeInvalidItems(paramSQLiteDatabase);
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "RecentId";
    StringBuilder localStringBuilder = new StringBuilder();
    String str1 = String.valueOf(201);
    String str2 = str1 + ",10000";
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    String[] arrayOfString2 = null;
    String str3 = null;
    String str4 = null;
    Cursor localCursor = localSQLiteDatabase.query("RECENT", arrayOfString1, null, arrayOfString2, str3, str4, "Priority DESC, ItemDate DESC", str2);
    int i = deleteItemsAndCloseCursor(paramSQLiteDatabase, localCursor);
  }

  private static boolean includePlaylistOfType(int paramInt)
  {
    switch (paramInt)
    {
    default:
    case 0:
    case 1:
    case 71:
    }
    for (boolean bool = false; ; bool = true)
      return bool;
  }

  private static void notifyContentChange(Context paramContext)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    Uri localUri1 = MusicContent.Recent.CONTENT_URI;
    localContentResolver.notifyChange(localUri1, null, false);
    Uri localUri2 = MusicContent.Mainstage.CONTENT_URI;
    localContentResolver.notifyChange(localUri2, null, false);
    Uri localUri3 = MusicContent.Playlists.RECENTS_URI;
    localContentResolver.notifyChange(localUri3, null, false);
    MusicUserContentBinder.notifyContentChanged(paramContext);
  }

  private static void populateAlbumAddedReason(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("UPDATE RECENT SET RecentReason = CASE (SELECT TrackType FROM MUSIC WHERE AlbumId=RecentAlbumId AND FileDate=ItemDate LIMIT 1) WHEN 1 THEN 2 ELSE 3 END  WHERE RecentAlbumId NOT NULL AND RecentReason=0");
  }

  private static void removeInvalidItems(SQLiteDatabase paramSQLiteDatabase)
  {
    int i = paramSQLiteDatabase.delete("RECENT", "(RecentListId NOT NULL AND NOT EXISTS(SELECT LISTS.Id FROM LISTS WHERE LISTS.Id=RecentListId)) OR (RecentAlbumId NOT NULL AND NOT EXISTS(SELECT MUSIC.AlbumId FROM MUSIC WHERE MUSIC.AlbumId=RecentAlbumId AND +Domain=0)) OR (RecentRadioId NOT NULL AND NOT EXISTS(SELECT Id FROM RADIO_STATIONS WHERE Id=RecentRadioId))", null);
    if (i <= 0)
      return;
    String str = "Deleted " + i + " invalid recent items.";
    int j = Log.i("RecentItemsManager", str);
  }

  static void update(Context paramContext, SQLiteDatabase paramSQLiteDatabase)
  {
    removeInvalidItems(paramSQLiteDatabase);
    addRecentlyAddedAlbums(paramSQLiteDatabase);
    addRecentlyAddedRadio(paramSQLiteDatabase);
  }

  public static void updateRecentItems(Context paramContext)
  {
    Store localStore = Store.getInstance(paramContext);
    SQLiteDatabase localSQLiteDatabase = localStore.beginWriteTxn();
    try
    {
      update(paramContext, localSQLiteDatabase);
      localStore.endWriteTxn(localSQLiteDatabase, true);
      if (1 == 0)
        return;
      notifyContentChange(paramContext);
      return;
    }
    finally
    {
      localStore.endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public static void updateRecentItemsAsync(Context paramContext)
  {
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    int i = UPDATE_RECENT_ITEMS_MESSAGE_TYPE;
    localLoggableHandler.removeMessages(i);
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        RecentItemsManager.updateRecentItems(RecentItemsManager.this);
      }
    };
    Message localMessage = Message.obtain(localLoggableHandler, local1);
    int j = UPDATE_RECENT_ITEMS_MESSAGE_TYPE;
    localMessage.what = j;
    boolean bool = localLoggableHandler.sendMessage(localMessage);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.RecentItemsManager
 * JD-Core Version:    0.6.2
 */