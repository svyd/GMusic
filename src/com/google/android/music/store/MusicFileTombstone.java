package com.google.android.music.store;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

public class MusicFileTombstone
{
  private static final String[] MUSIC_TOMBSTONE_PROJECTION = arrayOfString;
  private static int TOMBSTONE_PROJECTION_INDEX_ID = 0;
  private static int TOMBSTONE_PROJECTION_SOURCE_ID = 1;
  private static int TOMBSTONE_PROJECTION_SOURCE_VERSION = 2;
  private final long mLocalId;
  private final String mSourceId;
  private final String mSourceVersion;

  static
  {
    String[] arrayOfString = new String[3];
    arrayOfString[0] = "Id";
    arrayOfString[1] = "SourceId";
    arrayOfString[2] = "_sync_version";
  }

  private MusicFileTombstone(Cursor paramCursor)
  {
    int i = TOMBSTONE_PROJECTION_INDEX_ID;
    long l = paramCursor.getLong(i);
    this.mLocalId = l;
    int j = TOMBSTONE_PROJECTION_SOURCE_ID;
    String str1;
    if (!paramCursor.isNull(j))
    {
      int k = TOMBSTONE_PROJECTION_SOURCE_ID;
      str1 = paramCursor.getString(k);
    }
    for (this.mSourceId = str1; ; this.mSourceId = null)
    {
      int m = TOMBSTONE_PROJECTION_SOURCE_VERSION;
      if (paramCursor.isNull(m))
        break;
      int n = TOMBSTONE_PROJECTION_SOURCE_VERSION;
      String str2 = paramCursor.getString(n);
      this.mSourceVersion = str2;
      return;
    }
    this.mSourceVersion = null;
  }

  public static List<MusicFileTombstone> getMusicTombstones(Store paramStore)
  {
    SQLiteDatabase localSQLiteDatabase = paramStore.beginRead();
    try
    {
      String[] arrayOfString = MUSIC_TOMBSTONE_PROJECTION;
      localCursor = localSQLiteDatabase.query("MUSIC_TOMBSTONES", arrayOfString, null, null, null, null, null);
      Object localObject1;
      if ((localCursor != null) && (localCursor.moveToFirst()))
      {
        localObject1 = Lists.newArrayListWithCapacity(localCursor.getCount());
        boolean bool2;
        do
        {
          MusicFileTombstone localMusicFileTombstone = new MusicFileTombstone(localCursor);
          boolean bool1 = ((List)localObject1).add(localMusicFileTombstone);
          bool2 = localCursor.moveToNext();
        }
        while (bool2 != null);
      }
      while (true)
      {
        return localObject1;
        ArrayList localArrayList = Lists.newArrayList();
        localObject1 = localArrayList;
      }
    }
    finally
    {
      Cursor localCursor;
      Store.safeClose(localCursor);
      paramStore.endRead(localSQLiteDatabase);
    }
  }

  public static long insertMusicTombstone(SQLiteDatabase paramSQLiteDatabase, String paramString, long paramLong)
  {
    long l = 0L;
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("SourceId", paramString);
    Long localLong1 = Long.valueOf(paramLong);
    localContentValues.put("SourceAccount", localLong1);
    Long localLong2 = Long.valueOf(l);
    localContentValues.put("_sync_version", localLong2);
    if (paramSQLiteDatabase.insert("MUSIC_TOMBSTONES", null, localContentValues) == 65535L);
    while (true)
    {
      return l;
      l = 1L;
    }
  }

  public long getLocalId()
  {
    return this.mLocalId;
  }

  public String getSourceId()
  {
    return this.mSourceId;
  }

  public String getSourceVersion()
  {
    return this.mSourceVersion;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.MusicFileTombstone
 * JD-Core Version:    0.6.2
 */