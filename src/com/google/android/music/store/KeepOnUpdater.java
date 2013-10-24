package com.google.android.music.store;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import com.google.android.music.utils.DbUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class KeepOnUpdater
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.STORE);
  private static final String NO_NULL_COLUMN_HACK = null;
  private static final String[] NO_WHERE_ARGS = null;

  private static void addMissingAlbumsForKeepOnArtists(SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    Cursor localCursor = getMissingKeeponAlbumIds(paramSQLiteDatabase, paramString);
    if (localCursor != null);
    try
    {
      if (localCursor.getCount() > 0)
      {
        ContentValues localContentValues = new ContentValues(1);
        if (localCursor.moveToNext())
        {
          Long localLong = Long.valueOf(localCursor.getLong(0));
          localContentValues.put("AlbumId", localLong);
          long l = paramSQLiteDatabase.insert("KEEPON", null, localContentValues);
        }
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  private static int deleteAlbums(SQLiteDatabase paramSQLiteDatabase, Collection<Long> paramCollection)
  {
    return deleteByIdsInColumn(paramSQLiteDatabase, paramCollection, "AlbumId");
  }

  private static int deleteArtists(SQLiteDatabase paramSQLiteDatabase, Collection<Long> paramCollection)
  {
    return deleteByIdsInColumn(paramSQLiteDatabase, paramCollection, "ArtistId");
  }

  private static int deleteAutoPlaylists(SQLiteDatabase paramSQLiteDatabase, Collection<Long> paramCollection)
  {
    return deleteByIdsInColumn(paramSQLiteDatabase, paramCollection, "AutoListId");
  }

  private static int deleteByIdsInColumn(SQLiteDatabase paramSQLiteDatabase, Collection<Long> paramCollection, String paramString)
  {
    if (paramCollection.isEmpty());
    String str;
    String[] arrayOfString;
    for (int i = 0; ; i = paramSQLiteDatabase.delete("KEEPON", str, arrayOfString))
    {
      return i;
      int j = paramCollection.size() * 11 + 16;
      StringBuilder localStringBuilder1 = new StringBuilder(j);
      StringBuilder localStringBuilder2 = localStringBuilder1.append(paramString);
      DbUtils.appendIN(localStringBuilder1, paramCollection);
      str = localStringBuilder1.toString();
      arrayOfString = NO_WHERE_ARGS;
    }
  }

  private static int deleteInvalidKeeponEntries(SQLiteDatabase paramSQLiteDatabase)
  {
    return paramSQLiteDatabase.delete("KEEPON", "(ListId NOT NULL AND NOT EXISTS (select 1 from LISTS where LISTS.Id=KEEPON.ListId)) OR (AlbumId NOT NULL AND NOT EXISTS (select 1 from MUSIC where MUSIC.AlbumId=KEEPON.AlbumId LIMIT 1)) OR (ArtistId NOT NULL AND NOT EXISTS (select 1 from MUSIC where MUSIC.AlbumArtistId=KEEPON.ArtistId LIMIT 1))", null);
  }

  private static int deleteInvalidShouldKeeponAutoPlayListItems(SQLiteDatabase paramSQLiteDatabase)
  {
    int i = 0;
    String[] arrayOfString1 = new String[2];
    arrayOfString1[0] = "KeepOnId";
    arrayOfString1[1] = "AutoListId";
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    Cursor localCursor = localSQLiteDatabase.query("KEEPON", arrayOfString1, "AutoListId NOT NULL", null, str1, str2, str3);
    if (localCursor != null)
    {
      int j = 1;
      while (true)
      {
        String[] arrayOfString2;
        long l;
        String str4;
        try
        {
          arrayOfString2 = new String[j];
          if (!localCursor.moveToNext())
            break;
          l = localCursor.getLong(1);
          str4 = null;
          if (l == 65532L)
          {
            str4 = "(SELECT DISTINCT MUSIC.Id\nFROM MUSIC\n WHERE MUSIC.SourceAccount <> 0 AND (Rating > 3))";
            if (str4 != null)
              break label194;
            String str5 = "Failed to generate validate query for autoListId: " + l;
            int k = Log.w("KeepOnUpdater", str5);
            continue;
          }
        }
        finally
        {
          Store.safeClose(localCursor);
        }
        if (l == 65535L)
        {
          str4 = "(SELECT DISTINCT MUSIC.Id\nFROM MUSIC\n WHERE MUSIC.SourceAccount <> 0 AND +Domain=0 ORDER BY MUSIC.FileDate DESC  LIMIT 500)";
        }
        else if (l == 65533L)
        {
          str4 = "(SELECT DISTINCT MUSIC.Id\nFROM MUSIC\n WHERE MUSIC.SourceAccount <> 0 AND (TrackType IN (2,3,1) AND +Domain=0))";
        }
        else if (l == 65534L)
        {
          str4 = "(SELECT DISTINCT MUSIC.Id\nFROM MUSIC\n WHERE MUSIC.SourceAccount <> 0 AND +Domain=0)";
          continue;
          label194: String str6 = "KeepOnId=? AND MusicId NOT IN " + str4;
          String str7 = localCursor.getString(0);
          arrayOfString2[0] = str7;
          int m = paramSQLiteDatabase.delete("SHOULDKEEPON", str6, arrayOfString2);
          int n = m;
          i += n;
        }
      }
    }
    Store.safeClose(localCursor);
    return i;
  }

  private static int deleteInvalidShouldKeeponItems(SQLiteDatabase paramSQLiteDatabase)
  {
    int i = deleteInvalidShouldKeeponPlayListItems(paramSQLiteDatabase);
    int j = deleteInvalidShouldKeeponAutoPlayListItems(paramSQLiteDatabase);
    int k = i + j;
    int m = paramSQLiteDatabase.delete("SHOULDKEEPON", " ROWID IN (select SHOULDKEEPON.rowid from KEEPON, SHOULDKEEPON where KEEPON.AlbumId NOT NULL AND KEEPON.KeepOnId=SHOULDKEEPON.KeepOnId AND NOT EXISTS(select 1 from MUSIC where MUSIC.Id=SHOULDKEEPON.MusicId AND MUSIC.AlbumId=KEEPON.AlbumId))", null);
    return k + m;
  }

  private static int deleteInvalidShouldKeeponPlayListItems(SQLiteDatabase paramSQLiteDatabase)
  {
    int i = 0;
    String[] arrayOfString1 = new String[2];
    arrayOfString1[0] = "KeepOnId";
    arrayOfString1[1] = "ListId";
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    Cursor localCursor = localSQLiteDatabase.query("KEEPON", arrayOfString1, "ListId NOT NULL", null, str1, str2, str3);
    if (localCursor != null);
    try
    {
      String[] arrayOfString2 = new String[2];
      while (localCursor.moveToNext())
      {
        String str4 = localCursor.getString(0);
        arrayOfString2[0] = str4;
        String str5 = localCursor.getString(1);
        arrayOfString2[1] = str5;
        int j = paramSQLiteDatabase.delete("SHOULDKEEPON", "KeepOnId=? AND MusicId NOT IN (select MUSIC.Id from LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  where LISTITEMS.ListId=?)", arrayOfString2);
        int k = j;
        i += k;
      }
      return i;
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  private static boolean deleteOrphanedShouldKeeponItems(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    boolean bool = false;
    int i = paramSQLiteDatabase.delete("SHOULDKEEPON", "KeepOnId NOT IN (SELECT KeepOnId FROM KEEPON)", null);
    if (paramBoolean)
    {
      int j = deleteInvalidShouldKeeponItems(paramSQLiteDatabase);
      i += j;
    }
    if ((i > 0) && (Store.resetLocalCopyForOrphanedShouldKeepOnMusic(paramSQLiteDatabase) > 0))
      bool = true;
    if (paramBoolean)
      int k = paramSQLiteDatabase.delete("SHOULDKEEPON", "SHOULDKEEPON.MusicId IN (SELECT MusicId FROM SHOULDKEEPON LEFT JOIN MUSIC ON (SHOULDKEEPON.MusicId=MUSIC.Id) WHERE MUSIC.Id IS NULL)", null);
    return bool;
  }

  private static int deletePlaylists(SQLiteDatabase paramSQLiteDatabase, Collection<Long> paramCollection)
  {
    return deleteByIdsInColumn(paramSQLiteDatabase, paramCollection, "ListId");
  }

  public static void deselectAlbumUpdateKeepOn(Context paramContext, long paramLong)
  {
    ArrayList localArrayList = new ArrayList(1);
    Long localLong = Long.valueOf(paramLong);
    boolean bool = localArrayList.add(localLong);
    Context localContext = paramContext;
    Collection localCollection1 = null;
    Collection localCollection2 = null;
    Collection localCollection3 = null;
    Collection localCollection4 = null;
    Collection localCollection5 = null;
    Collection localCollection6 = null;
    startUpdateKeepon(localContext, null, localArrayList, localCollection1, localCollection2, localCollection3, localCollection4, localCollection5, localCollection6);
  }

  public static void deselectAutoPlaylistUpdateKeepOn(Context paramContext, long paramLong)
  {
    ArrayList localArrayList = new ArrayList(1);
    Long localLong = Long.valueOf(paramLong);
    boolean bool = localArrayList.add(localLong);
    Context localContext = paramContext;
    Collection localCollection1 = null;
    Collection localCollection2 = null;
    Collection localCollection3 = null;
    Collection localCollection4 = null;
    Collection localCollection5 = null;
    Collection localCollection6 = null;
    startUpdateKeepon(localContext, null, localCollection1, localCollection2, localArrayList, localCollection3, localCollection4, localCollection5, localCollection6);
  }

  public static void deselectPlaylistUpdateKeepOn(Context paramContext, long paramLong)
  {
    ArrayList localArrayList = new ArrayList(1);
    Long localLong = Long.valueOf(paramLong);
    boolean bool = localArrayList.add(localLong);
    Context localContext = paramContext;
    Collection localCollection1 = null;
    Collection localCollection2 = null;
    Collection localCollection3 = null;
    Collection localCollection4 = null;
    Collection localCollection5 = null;
    Collection localCollection6 = null;
    startUpdateKeepon(localContext, null, localCollection1, localArrayList, localCollection2, localCollection3, localCollection4, localCollection5, localCollection6);
  }

  private static Cursor getMissingKeeponAlbumIds(SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "ArtistId";
    StringBuilder localStringBuilder = new StringBuilder().append("ArtistId NOT NULL");
    String str1;
    Cursor localCursor1;
    if (paramString == null)
    {
      str1 = "";
      String str2 = str1;
      localCursor1 = paramSQLiteDatabase.query("KEEPON", arrayOfString1, str2, null, null, null, null);
      if (localCursor1 == null);
    }
    while (true)
    {
      StringBuffer localStringBuffer1;
      try
      {
        int i = localCursor1.getCount();
        if (i < 1)
        {
          localCursor2 = null;
          return localCursor2;
          str1 = " IN (" + paramString + ")";
          break;
        }
        int j = localCursor1.getCount() * 6;
        localStringBuffer1 = new StringBuffer(j);
        if (localCursor1.moveToNext())
        {
          String str4 = localCursor1.getString(0);
          StringBuffer localStringBuffer2 = localStringBuffer1.append(str4).append(",");
          continue;
        }
      }
      finally
      {
        Store.safeClose(localCursor1);
      }
      int k = localStringBuffer1.length() + -1;
      localStringBuffer1.setLength(k);
      String str3 = localStringBuffer1.toString();
      String str5 = str3;
      Store.safeClose(localCursor1);
      String[] arrayOfString2 = new String[1];
      arrayOfString2[0] = "MUSIC.AlbumId";
      String str6 = "(MUSIC.AlbumArtistId IN (" + str5 + ")" + " OR " + "MUSIC" + "." + "ArtistId" + " IN (" + str5 + ")" + ")" + " AND " + "KEEPON.KeepOnId" + " IS NULL";
      Cursor localCursor2 = paramSQLiteDatabase.query(true, "MUSIC LEFT  JOIN KEEPON ON (MUSIC.AlbumId = KEEPON.AlbumId) ", arrayOfString2, str6, null, null, null, null, null);
    }
  }

  static Collection<Long> insertAlbums(SQLiteDatabase paramSQLiteDatabase, Collection<Long> paramCollection)
  {
    return insertIdsInColumn(paramSQLiteDatabase, paramCollection, "AlbumId");
  }

  static Collection<Long> insertArtists(SQLiteDatabase paramSQLiteDatabase, Collection<Long> paramCollection)
  {
    return insertIdsInColumn(paramSQLiteDatabase, paramCollection, "ArtistId");
  }

  private static Collection<Long> insertAutoPlaylists(SQLiteDatabase paramSQLiteDatabase, Collection<Long> paramCollection)
  {
    return insertIdsInColumn(paramSQLiteDatabase, paramCollection, "AutoListId");
  }

  private static void insertFromAlbums(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("INSERT INTO SHOULDKEEPON(MusicId,KeepOnId)\nSELECT DISTINCT MUSIC.Id,KEEPON.KeepOnId\nFROM KEEPON\n JOIN MUSIC ON (KEEPON.AlbumId = MUSIC.AlbumId)  WHERE MUSIC.SourceAccount <> 0 AND Domain = 0");
  }

  private static void insertFromAlbums(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    String str = "INSERT INTO SHOULDKEEPON(MusicId,KeepOnId)\nSELECT DISTINCT MUSIC.Id,KEEPON.KeepOnId\nFROM KEEPON\n JOIN MUSIC ON (KEEPON.AlbumId = MUSIC.AlbumId)  WHERE MUSIC.SourceAccount <> 0 AND Domain = 0 AND KEEPON.KeepOnId=" + paramLong;
    paramSQLiteDatabase.execSQL(str);
  }

  private static void insertFromAllSongsPlaylist(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    if (LOGV)
    {
      String str1 = "insertFromAllSongsPlaylist: keepOnId=" + paramLong;
      int i = Log.d("KeepOnUpdater", str1);
    }
    String str2 = "INSERT INTO SHOULDKEEPON(MusicId,KeepOnId)\nSELECT DISTINCT MUSIC.Id," + paramLong + "\n" + "FROM " + "MUSIC" + "\n" + " WHERE " + "MUSIC.SourceAccount" + " <> " + 0 + " AND " + "+Domain=0";
    paramSQLiteDatabase.execSQL(str2);
  }

  private static void insertFromAutoPlaylists(SQLiteDatabase paramSQLiteDatabase)
  {
    if (LOGV)
      int i = Log.d("KeepOnUpdater", "insertFromAutoPlaylists");
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "KeepOnId";
    arrayOfString[1] = "AutoListId";
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    Cursor localCursor = localSQLiteDatabase.query("KEEPON", arrayOfString, "AutoListId NOT NULL", null, str1, str2, str3);
    if (localCursor != null)
      while (true)
      {
        long l1;
        long l2;
        try
        {
          if (!localCursor.moveToNext())
            break;
          l1 = localCursor.getLong(0);
          l2 = localCursor.getLong(1);
          if (l2 == 65532L)
          {
            insertFromThumbsUpPlaylist(paramSQLiteDatabase, l1);
            continue;
          }
        }
        finally
        {
          Store.safeClose(localCursor);
        }
        if (l2 == 65535L)
          insertFromRecentlyAddedPlaylist(paramSQLiteDatabase, l1);
        else if (l2 == 65533L)
          insertFromStoreSongsPlaylist(paramSQLiteDatabase, l1);
        else if (l2 == 65534L)
          insertFromAllSongsPlaylist(paramSQLiteDatabase, l1);
      }
    Store.safeClose(localCursor);
  }

  private static void insertFromAutoPlaylists(SQLiteDatabase paramSQLiteDatabase, long paramLong1, long paramLong2)
  {
    if (LOGV)
    {
      String str1 = "insertFromAutoPlaylists: keepOnId=" + paramLong1 + " autoListId=" + paramLong2;
      int i = Log.d("KeepOnUpdater", str1);
    }
    if (paramLong2 == 65532L)
    {
      insertFromThumbsUpPlaylist(paramSQLiteDatabase, paramLong1);
      return;
    }
    if (paramLong2 == 65535L)
    {
      insertFromRecentlyAddedPlaylist(paramSQLiteDatabase, paramLong1);
      return;
    }
    if (paramLong2 == 65533L)
    {
      insertFromStoreSongsPlaylist(paramSQLiteDatabase, paramLong1);
      return;
    }
    if (paramLong2 == 65534L)
    {
      insertFromAllSongsPlaylist(paramSQLiteDatabase, paramLong1);
      return;
    }
    String str2 = "Failed to insert auto playlist with id: " + paramLong2;
    int j = Log.wtf("KeepOnUpdater", str2);
  }

  private static void insertFromPlaylists(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("INSERT INTO SHOULDKEEPON(MusicId,KeepOnId)\nSELECT DISTINCT MUSIC.Id,KEEPON.KeepOnId\nFROM KEEPON\n JOIN LISTITEMS ON (KEEPON.ListId = LISTITEMS.ListId) \n JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE MUSIC.SourceAccount<> 0");
  }

  private static void insertFromPlaylists(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    String str = "INSERT INTO SHOULDKEEPON(MusicId,KeepOnId)\nSELECT DISTINCT MUSIC.Id,KEEPON.KeepOnId\nFROM KEEPON\n JOIN LISTITEMS ON (KEEPON.ListId = LISTITEMS.ListId) \n JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE MUSIC.SourceAccount<> 0 AND KEEPON.KeepOnId=" + paramLong;
    paramSQLiteDatabase.execSQL(str);
  }

  private static void insertFromRecentlyAddedPlaylist(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    if (LOGV)
    {
      String str1 = "insertFromRecentlyAddedPlaylists: keepOnId=" + paramLong;
      int i = Log.d("KeepOnUpdater", str1);
    }
    String str2 = "INSERT INTO SHOULDKEEPON(MusicId,KeepOnId)\nSELECT DISTINCT MUSIC.Id," + paramLong + "\n" + "FROM " + "MUSIC" + "\n" + " WHERE " + "MUSIC.SourceAccount" + " <> " + 0 + " AND " + "+Domain=0" + " ORDER BY " + "MUSIC.FileDate DESC " + " LIMIT " + 500;
    paramSQLiteDatabase.execSQL(str2);
  }

  private static void insertFromStoreSongsPlaylist(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    if (LOGV)
    {
      String str1 = "insertFromStoreSongsPlaylists: keepOnId=" + paramLong;
      int i = Log.d("KeepOnUpdater", str1);
    }
    String str2 = "INSERT INTO SHOULDKEEPON(MusicId,KeepOnId)\nSELECT DISTINCT MUSIC.Id," + paramLong + "\n" + "FROM " + "MUSIC" + "\n" + " WHERE " + "MUSIC.SourceAccount" + " <> " + 0 + " AND " + "(TrackType IN (2,3,1) AND +Domain=0)";
    paramSQLiteDatabase.execSQL(str2);
  }

  private static void insertFromThumbsUpPlaylist(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    if (LOGV)
    {
      String str1 = "insertFromThumbsUpPlaylists: keepOnId=" + paramLong;
      int i = Log.d("KeepOnUpdater", str1);
    }
    String str2 = "INSERT INTO SHOULDKEEPON(MusicId,KeepOnId)\nSELECT DISTINCT MUSIC.Id," + paramLong + "\n" + "FROM " + "MUSIC" + "\n" + " WHERE " + "MUSIC.SourceAccount" + " <> " + 0 + " AND " + "(Rating > 3)";
    paramSQLiteDatabase.execSQL(str2);
  }

  private static Collection<Long> insertIdsInColumn(SQLiteDatabase paramSQLiteDatabase, Collection<Long> paramCollection, String paramString)
  {
    Iterator localIterator;
    if (LOGV)
    {
      localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
      {
        long l1 = ((Long)localIterator.next()).longValue();
        Object[] arrayOfObject = new Object[2];
        Long localLong1 = Long.valueOf(l1);
        arrayOfObject[0] = localLong1;
        arrayOfObject[1] = paramString;
        String str1 = String.format("insertIdsInColumn: id=%d column=%s", arrayOfObject);
        int i = Log.d("KeepOnUpdater", str1);
      }
    }
    LinkedList localLinkedList = Lists.newLinkedList();
    if (paramCollection.isEmpty());
    while (true)
    {
      return localLinkedList;
      ContentValues localContentValues = new ContentValues();
      localIterator = paramCollection.iterator();
      while (localIterator.hasNext())
      {
        Long localLong2 = Long.valueOf(((Long)localIterator.next()).longValue());
        localContentValues.put(paramString, localLong2);
        String str2 = NO_NULL_COLUMN_HACK;
        long l2 = paramSQLiteDatabase.insert("KEEPON", str2, localContentValues);
        if (l2 != 65535L)
        {
          Long localLong3 = Long.valueOf(l2);
          boolean bool = localLinkedList.add(localLong3);
        }
        localContentValues.clear();
      }
    }
  }

  private static Collection<Long> insertPlaylists(SQLiteDatabase paramSQLiteDatabase, Collection<Long> paramCollection)
  {
    return insertIdsInColumn(paramSQLiteDatabase, paramCollection, "ListId");
  }

  public static void selectAlbumUpdateKeepOn(Context paramContext, long paramLong)
  {
    ArrayList localArrayList = new ArrayList(1);
    Long localLong = Long.valueOf(paramLong);
    boolean bool = localArrayList.add(localLong);
    Context localContext = paramContext;
    Collection localCollection1 = null;
    Collection localCollection2 = null;
    Collection localCollection3 = null;
    Collection localCollection4 = null;
    Collection localCollection5 = null;
    Collection localCollection6 = null;
    startUpdateKeepon(localContext, null, localCollection1, localCollection2, localCollection3, localCollection4, localArrayList, localCollection5, localCollection6);
  }

  public static void selectAutoPlaylistUpdateKeepOn(Context paramContext, long paramLong)
  {
    ArrayList localArrayList = new ArrayList(1);
    Long localLong = Long.valueOf(paramLong);
    boolean bool = localArrayList.add(localLong);
    Context localContext = paramContext;
    Collection localCollection1 = null;
    Collection localCollection2 = null;
    Collection localCollection3 = null;
    Collection localCollection4 = null;
    Collection localCollection5 = null;
    Collection localCollection6 = null;
    startUpdateKeepon(localContext, null, localCollection1, localCollection2, localCollection3, localCollection4, localCollection5, localCollection6, localArrayList);
  }

  public static void selectPlaylistUpdateKeepOn(Context paramContext, long paramLong)
  {
    ArrayList localArrayList = new ArrayList(1);
    Long localLong = Long.valueOf(paramLong);
    boolean bool = localArrayList.add(localLong);
    Context localContext = paramContext;
    Collection localCollection1 = null;
    Collection localCollection2 = null;
    Collection localCollection3 = null;
    Collection localCollection4 = null;
    Collection localCollection5 = null;
    Collection localCollection6 = null;
    startUpdateKeepon(localContext, null, localCollection1, localCollection2, localCollection3, localCollection4, localCollection5, localArrayList, localCollection6);
  }

  public static void sendShouldKeeponUpdatedBroadcast(Context paramContext, boolean paramBoolean)
  {
    Intent localIntent1 = new Intent("com.google.android.music.NEW_SHOULDKEEPON");
    if (paramBoolean)
      Intent localIntent2 = localIntent1.putExtra("deleteCachedFiles", paramBoolean);
    paramContext.sendBroadcast(localIntent1);
  }

  public static void startUpdateKeepon(Context paramContext, Collection<Long> paramCollection1, Collection<Long> paramCollection2, Collection<Long> paramCollection3, Collection<Long> paramCollection4, Collection<Long> paramCollection5, Collection<Long> paramCollection6, Collection<Long> paramCollection7, Collection<Long> paramCollection8)
  {
    if (LOGV)
      int i = Log.d("KeepOnUpdater", "startUpdateKeepon");
    Intent localIntent1 = new Intent("com.google.android.music.UPDATE_KEEP_ON");
    Serializable localSerializable1 = (Serializable)paramCollection1;
    Intent localIntent2 = localIntent1.putExtra("deselectedArtists", localSerializable1);
    Serializable localSerializable2 = (Serializable)paramCollection2;
    Intent localIntent3 = localIntent1.putExtra("deselectedAlbums", localSerializable2);
    Serializable localSerializable3 = (Serializable)paramCollection3;
    Intent localIntent4 = localIntent1.putExtra("deselectedPlaylists", localSerializable3);
    Serializable localSerializable4 = (Serializable)paramCollection4;
    Intent localIntent5 = localIntent1.putExtra("deselectedAutoPlaylists", localSerializable4);
    Serializable localSerializable5 = (Serializable)paramCollection5;
    Intent localIntent6 = localIntent1.putExtra("selectedArtists", localSerializable5);
    Serializable localSerializable6 = (Serializable)paramCollection6;
    Intent localIntent7 = localIntent1.putExtra("selectedAlbums", localSerializable6);
    Serializable localSerializable7 = (Serializable)paramCollection7;
    Intent localIntent8 = localIntent1.putExtra("selectedPlaylists", localSerializable7);
    Serializable localSerializable8 = (Serializable)paramCollection8;
    Intent localIntent9 = localIntent1.putExtra("selectedAutoPlaylists", localSerializable8);
    if (paramContext.startService(localIntent1) != null)
      return;
    throw new IllegalStateException("Could not start the keep on updater service");
  }

  private static void updateKeeponTables(Context paramContext, boolean paramBoolean)
  {
    boolean bool1 = true;
    if (LOGV)
      int i = Log.i("KeepOnUpdater", "Updating SHOULDKEEPON");
    boolean bool2 = false;
    boolean bool3 = false;
    Store localStore = Store.getInstance(paramContext);
    SQLiteDatabase localSQLiteDatabase = localStore.beginWriteTxn();
    try
    {
      int j = deleteInvalidKeeponEntries(localSQLiteDatabase);
      int k = 0;
      if (paramBoolean)
        k = localSQLiteDatabase.delete("SHOULDKEEPON", "1", null);
      addMissingAlbumsForKeepOnArtists(localSQLiteDatabase, null);
      insertFromAlbums(localSQLiteDatabase);
      insertFromPlaylists(localSQLiteDatabase);
      insertFromAutoPlaylists(localSQLiteDatabase);
      localStore.updateKeeponDownloadSongCounts(localSQLiteDatabase);
      if (paramBoolean)
        if (k > 0)
        {
          int m = Store.resetLocalCopyForOrphanedShouldKeepOnMusic(localSQLiteDatabase);
          if (m <= 0)
            break label146;
          bool2 = true;
        }
      while (true)
      {
        localStore.endWriteTxn(localSQLiteDatabase, true);
        if (LOGV)
          int n = Log.i("KeepOnUpdater", "Update of SHOULDKEEPON complete");
        sendShouldKeeponUpdatedBroadcast(paramContext, bool2);
        return;
        label146: bool2 = false;
        continue;
        bool1 = true;
        boolean bool4 = deleteOrphanedShouldKeeponItems(localSQLiteDatabase, bool1);
        bool2 = bool4;
      }
    }
    finally
    {
      localStore.endWriteTxn(localSQLiteDatabase, bool3);
    }
  }

  static boolean updateNeedToKeepOn(SQLiteDatabase paramSQLiteDatabase, Collection<Long> paramCollection)
  {
    if (LOGV)
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("updateNeedToKeepOn: newKeepOnId.size=");
      int i = paramCollection.size();
      String str1 = i;
      int j = Log.d("KeepOnUpdater", str1);
    }
    if (!paramCollection.isEmpty())
    {
      Cursor localCursor = null;
      StringBuilder localStringBuilder2;
      try
      {
        localStringBuilder2 = new java/lang/StringBuilder;
        int k = paramCollection.size() * 10;
        localStringBuilder2.<init>(k);
        Iterator localIterator = paramCollection.iterator();
        while (localIterator.hasNext())
        {
          Long localLong = (Long)localIterator.next();
          StringBuilder localStringBuilder3 = localStringBuilder2.append(localLong).append(',');
        }
      }
      finally
      {
        Store.safeClose(localCursor);
      }
      int m = localStringBuilder2.length() + -1;
      localStringBuilder2.setLength(m);
      String str2 = localStringBuilder2.toString();
      addMissingAlbumsForKeepOnArtists(paramSQLiteDatabase, str2);
      String[] arrayOfString = new String[5];
      arrayOfString[0] = "KeepOnId";
      arrayOfString[1] = "AlbumId";
      arrayOfString[2] = "ArtistId";
      arrayOfString[3] = "ListId";
      arrayOfString[4] = "AutoListId";
      String str3 = "KeepOnId IN (" + str2 + ")";
      localCursor = paramSQLiteDatabase.query("KEEPON", arrayOfString, str3, null, null, null, null);
      while (localCursor.moveToNext())
      {
        long l1 = localCursor.getLong(0);
        if (!localCursor.isNull(1))
          insertFromAlbums(paramSQLiteDatabase, l1);
        else if (localCursor.isNull(2))
          if (!localCursor.isNull(3))
          {
            insertFromPlaylists(paramSQLiteDatabase, l1);
          }
          else if (!localCursor.isNull(4))
          {
            long l2 = localCursor.getLong(4);
            insertFromAutoPlaylists(paramSQLiteDatabase, l1, l2);
          }
      }
      Store.safeClose(localCursor);
    }
    return deleteOrphanedShouldKeeponItems(paramSQLiteDatabase, false);
  }

  public static class UpdateKeepOnTables extends IntentService
  {
    private Handler mHandler;
    private Runnable mSendNotification;

    public UpdateKeepOnTables()
    {
      super();
      Handler localHandler = new Handler();
      this.mHandler = localHandler;
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          ContentResolver localContentResolver1 = KeepOnUpdater.UpdateKeepOnTables.this.getContentResolver();
          Uri localUri1 = MusicContent.KEEP_ON_URI;
          localContentResolver1.notifyChange(localUri1, null, false);
          ContentResolver localContentResolver2 = KeepOnUpdater.UpdateKeepOnTables.this.getContentResolver();
          Uri localUri2 = MusicContent.DOWNLOAD_QUEUE_URI;
          localContentResolver2.notifyChange(localUri2, null, false);
        }
      };
      this.mSendNotification = local1;
    }

    private static Collection<Long> getLongCollection(Intent paramIntent, String paramString)
    {
      return (Collection)paramIntent.getSerializableExtra(paramString);
    }

    protected void onHandleIntent(Intent paramIntent)
    {
      if (KeepOnUpdater.LOGV)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("onHandleIntent: intent=");
        Intent localIntent1 = paramIntent;
        String str1 = localIntent1;
        int i = Log.d("KeepOnUpdater", str1);
      }
      Intent localIntent2 = paramIntent;
      String str2 = "deselectedArtists";
      Collection localCollection1 = getLongCollection(localIntent2, str2);
      Intent localIntent3 = paramIntent;
      String str3 = "deselectedAlbums";
      Collection localCollection2 = getLongCollection(localIntent3, str3);
      Intent localIntent4 = paramIntent;
      String str4 = "deselectedPlaylists";
      Collection localCollection3 = getLongCollection(localIntent4, str4);
      Intent localIntent5 = paramIntent;
      String str5 = "deselectedAutoPlaylists";
      Collection localCollection4 = getLongCollection(localIntent5, str5);
      Intent localIntent6 = paramIntent;
      String str6 = "selectedArtists";
      Collection localCollection5 = getLongCollection(localIntent6, str6);
      Intent localIntent7 = paramIntent;
      String str7 = "selectedAlbums";
      Collection localCollection6 = getLongCollection(localIntent7, str7);
      Intent localIntent8 = paramIntent;
      String str8 = "selectedPlaylists";
      Collection localCollection7 = getLongCollection(localIntent8, str8);
      Intent localIntent9 = paramIntent;
      String str9 = "selectedAutoPlaylists";
      Collection localCollection8 = getLongCollection(localIntent9, str9);
      Store localStore = Store.getInstance(this);
      boolean bool1 = false;
      boolean bool2 = false;
      LinkedList localLinkedList = Lists.newLinkedList();
      SQLiteDatabase localSQLiteDatabase = localStore.beginWriteTxn();
      int j = 0;
      if (localCollection1 != null);
      try
      {
        int k = KeepOnUpdater.deleteArtists(localSQLiteDatabase, localCollection1);
        j += k;
        if (localCollection2 != null)
        {
          int m = KeepOnUpdater.deleteAlbums(localSQLiteDatabase, localCollection2);
          j += m;
        }
        if (localCollection3 != null)
        {
          int n = KeepOnUpdater.deletePlaylists(localSQLiteDatabase, localCollection3);
          j += n;
        }
        if (localCollection4 != null)
        {
          int i1 = KeepOnUpdater.deleteAutoPlaylists(localSQLiteDatabase, localCollection4);
          j += i1;
        }
        if (localCollection5 != null)
        {
          Collection localCollection9 = KeepOnUpdater.insertArtists(localSQLiteDatabase, localCollection5);
          boolean bool3 = localLinkedList.addAll(localCollection9);
        }
        if (localCollection6 != null)
        {
          Collection localCollection10 = KeepOnUpdater.insertAlbums(localSQLiteDatabase, localCollection6);
          boolean bool4 = localLinkedList.addAll(localCollection10);
        }
        if (localCollection7 != null)
        {
          Collection localCollection11 = KeepOnUpdater.insertPlaylists(localSQLiteDatabase, localCollection7);
          boolean bool5 = localLinkedList.addAll(localCollection11);
        }
        if (localCollection8 != null)
        {
          Collection localCollection12 = KeepOnUpdater.insertAutoPlaylists(localSQLiteDatabase, localCollection8);
          boolean bool6 = localLinkedList.addAll(localCollection12);
        }
        int i2 = localLinkedList.size();
        if (j + i2 > 0)
        {
          bool2 = true;
          if (bool2)
          {
            bool1 = KeepOnUpdater.updateNeedToKeepOn(localSQLiteDatabase, localLinkedList);
            localStore.updateKeeponDownloadSongCounts(localSQLiteDatabase);
          }
          localStore.endWriteTxn(localSQLiteDatabase, bool2);
          return;
          Handler localHandler1 = this.mHandler;
          Runnable localRunnable1 = this.mSendNotification;
          localHandler1.removeCallbacks(localRunnable1);
          Handler localHandler2 = this.mHandler;
          Runnable localRunnable2 = this.mSendNotification;
          boolean bool7 = localHandler2.postDelayed(localRunnable2, 2000L);
          return;
        }
        bool2 = false;
      }
      finally
      {
        localStore.endWriteTxn(localSQLiteDatabase, bool2);
      }
    }
  }

  public static class SyncListener extends IntentService
  {
    public SyncListener()
    {
      super();
    }

    protected void onHandleIntent(Intent paramIntent)
    {
      KeepOnUpdater.updateKeeponTables(this, true);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.KeepOnUpdater
 * JD-Core Version:    0.6.2
 */