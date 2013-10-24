package com.google.android.music.store;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import com.google.android.gsf.Gservices;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.utils.DbUtils;
import java.util.Collection;

public class AutoCacheManager
{
  private Store mStore;

  public AutoCacheManager(Store paramStore)
  {
    this.mStore = paramStore;
  }

  private Cursor doAutoCacheQuery(String[] paramArrayOfString, int paramInt, Collection<Long> paramCollection)
  {
    int i = Gservices.getInt(this.mStore.getContext().getApplicationContext().getContentResolver(), "music_autocache_num_suggested_mixes", 4);
    int j = Gservices.getInt(this.mStore.getContext().getApplicationContext().getContentResolver(), "music_autocache_suggested_mix_size", 10);
    Object localObject1;
    if ((i <= 0) || (i > 100) || (j <= 0) || (j > 1000))
    {
      Object[] arrayOfObject = new Object[2];
      Integer localInteger1 = Integer.valueOf(i);
      arrayOfObject[0] = localInteger1;
      Integer localInteger2 = Integer.valueOf(j);
      arrayOfObject[1] = localInteger2;
      String str1 = String.format("Invalid limit(s): numSuggestedMixes=%d, numTracksPerMix=%d", arrayOfObject);
      int k = Log.w("AutoCacheManager", str1);
      localObject1 = null;
    }
    while (true)
    {
      return localObject1;
      SQLiteQueryBuilder localSQLiteQueryBuilder = new SQLiteQueryBuilder();
      String str2 = "(SELECT SeedListId,SeedOrder FROM SUGGESTED_SEEDS ORDER BY SeedOrder LIMIT " + i + ")" + " JOIN LISTITEMS ON SeedListId=LISTITEMS.ListId" + " JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id) ";
      localSQLiteQueryBuilder.setTables(str2);
      SQLiteDatabase localSQLiteDatabase = this.mStore.beginRead();
      try
      {
        String str3 = "LocalCopyType IN (0, 300) AND ClientPosition < " + j;
        if ((paramCollection != null) && (!paramCollection.isEmpty()))
        {
          StringBuilder localStringBuilder = new StringBuilder().append(str3).append(" AND ");
          String str4 = DbUtils.getNotInClause("MUSIC.Id", paramCollection);
          str3 = str4;
        }
        String[] arrayOfString1 = null;
        String str5 = null;
        String str6 = null;
        String str7 = "SeedOrder,ClientPosition";
        if (paramInt != -1)
        {
          str8 = Integer.toString(paramInt);
          String[] arrayOfString2 = paramArrayOfString;
          Cursor localCursor = localSQLiteQueryBuilder.query(localSQLiteDatabase, arrayOfString2, str3, arrayOfString1, str5, str6, str7, str8);
          localObject1 = localCursor;
          this.mStore.endRead(localSQLiteDatabase);
          continue;
        }
        String str8 = null;
      }
      finally
      {
        this.mStore.endRead(localSQLiteDatabase);
      }
    }
  }

  public ContentIdentifier[] getNextAutoCacheDownloads(int paramInt, Collection<Long> paramCollection)
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "MUSIC.Id";
    arrayOfString[1] = "Domain";
    Cursor localCursor = doAutoCacheQuery(arrayOfString, paramInt, paramCollection);
    if (localCursor != null);
    while (true)
    {
      try
      {
        arrayOfContentIdentifier = new ContentIdentifier[localCursor.getCount()];
        int i = 0;
        if (localCursor.moveToNext())
        {
          int j = i + 1;
          long l = localCursor.getLong(0);
          ContentIdentifier.Domain localDomain = ContentIdentifier.Domain.fromDBValue(localCursor.getInt(1));
          ContentIdentifier localContentIdentifier = new ContentIdentifier(l, localDomain);
          arrayOfContentIdentifier[i] = localContentIdentifier;
          i = j;
          continue;
        }
        return arrayOfContentIdentifier;
      }
      finally
      {
        Store.safeClose(localCursor);
      }
      ContentIdentifier[] arrayOfContentIdentifier = null;
    }
  }

  public long getTotalSizeToAutoCache()
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "SUM(Size)";
    Cursor localCursor = doAutoCacheQuery(arrayOfString, -1, null);
    if (localCursor != null);
    try
    {
      if (localCursor.moveToFirst())
      {
        long l1 = localCursor.getLong(0);
        l2 = l1;
        return l2;
      }
      Store.safeClose(localCursor);
      long l2 = 0L;
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.AutoCacheManager
 * JD-Core Version:    0.6.2
 */