package com.google.android.music.store;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;
import com.google.android.music.utils.PostFroyoUtils.CancellationSignalComp;
import com.google.android.music.utils.PostFroyoUtils.SQLiteDatabaseComp;
import java.util.ArrayList;
import java.util.HashMap;

public class DownloadQueueContentProviderHelper
{
  private static HashMap<String, String> sAlbumDownloadQueueProjectionMap = new HashMap();
  private static HashMap<String, String> sPlaylistDownloadQueueProjectionMap;

  static
  {
    MusicContentProvider.addMapping(sAlbumDownloadQueueProjectionMap, "_id", "MUSIC.AlbumId");
    MusicContentProvider.addMapping(sAlbumDownloadQueueProjectionMap, "_title", "Album");
    MusicContentProvider.addMapping(sAlbumDownloadQueueProjectionMap, "_subtitle", "AlbumArtist");
    MusicContentProvider.addNullMapping(sAlbumDownloadQueueProjectionMap, "_type");
    MusicContentProvider.addMapping(sAlbumDownloadQueueProjectionMap, "DateAdded", "DateAdded");
    sPlaylistDownloadQueueProjectionMap = new HashMap();
    MusicContentProvider.addMapping(sPlaylistDownloadQueueProjectionMap, "_id", "LISTS.Id");
    MusicContentProvider.addMapping(sPlaylistDownloadQueueProjectionMap, "_title", "LISTS.Name");
    MusicContentProvider.addNullMapping(sPlaylistDownloadQueueProjectionMap, "_subtitle");
    MusicContentProvider.addMapping(sPlaylistDownloadQueueProjectionMap, "_type", "ListType");
    MusicContentProvider.addMapping(sPlaylistDownloadQueueProjectionMap, "DateAdded", "DateAdded");
  }

  static Cursor getDownloadQueueContent(Context paramContext, Store paramStore, String[] paramArrayOfString, PostFroyoUtils.CancellationSignalComp paramCancellationSignalComp)
  {
    String[] arrayOfString1 = new String[5];
    arrayOfString1[0] = "playlist_id";
    arrayOfString1[1] = "playlist_name";
    arrayOfString1[2] = "playlist_description";
    arrayOfString1[3] = "playlist_type";
    arrayOfString1[4] = "DateAdded";
    StringBuilder localStringBuilder1 = new StringBuilder().append(" SELECT DISTINCT AutoListId , DateAdded");
    String str1 = " FROM MUSIC JOIN SHOULDKEEPON JOIN KEEPON";
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1);
    String str2 = " ON MUSIC.Id = SHOULDKEEPON.MusicId AND SHOULDKEEPON.KeepOnId = KEEPON.KeepOnId";
    StringBuilder localStringBuilder3 = localStringBuilder2.append(str2);
    String str3 = " WHERE (LocalCopyPath IS NULL OR LocalCopyType <> 200)";
    String str4 = str3 + " ORDER BY " + "DateAdded";
    SQLiteDatabase localSQLiteDatabase1 = paramStore.beginRead();
    Cursor localCursor2;
    int k;
    MatrixCursor localMatrixCursor1;
    MatrixCursor localMatrixCursor2;
    ArrayList localArrayList1;
    String str6;
    while (true)
    {
      try
      {
        SQLiteQueryBuilder localSQLiteQueryBuilder1 = new SQLiteQueryBuilder();
        localSQLiteQueryBuilder1.setTables("MUSIC JOIN SHOULDKEEPON JOIN KEEPON ON (MUSIC.Id = SHOULDKEEPON.MusicId AND SHOULDKEEPON.KeepOnId = KEEPON.KeepOnId)");
        HashMap localHashMap1 = sAlbumDownloadQueueProjectionMap;
        localSQLiteQueryBuilder1.setProjectionMap(localHashMap1);
        localSQLiteQueryBuilder1.setDistinct(true);
        String[] arrayOfString2 = paramArrayOfString;
        Cursor localCursor1 = localSQLiteQueryBuilder1.query(localSQLiteDatabase1, arrayOfString2, " (LocalCopyPath IS NULL OR LocalCopyType <> 200) AND KEEPON.AlbumId IS NOT NULL", null, null, null, "DateAdded ASC");
        localSQLiteQueryBuilder1.setTables("MUSIC JOIN SHOULDKEEPON JOIN KEEPON JOIN LISTS ON (MUSIC.Id = SHOULDKEEPON.MusicId AND SHOULDKEEPON.KeepOnId = KEEPON.KeepOnId AND LISTS.Id = KEEPON.ListId)");
        HashMap localHashMap2 = sPlaylistDownloadQueueProjectionMap;
        localSQLiteQueryBuilder1.setProjectionMap(localHashMap2);
        SQLiteQueryBuilder localSQLiteQueryBuilder2 = localSQLiteQueryBuilder1;
        SQLiteDatabase localSQLiteDatabase2 = localSQLiteDatabase1;
        String[] arrayOfString3 = paramArrayOfString;
        localCursor2 = localSQLiteQueryBuilder2.query(localSQLiteDatabase2, arrayOfString3, " (LocalCopyPath IS NULL OR LocalCopyType <> 200)", null, null, null, "DateAdded ASC");
        String str5 = str4;
        PostFroyoUtils.CancellationSignalComp localCancellationSignalComp = paramCancellationSignalComp;
        Cursor localCursor3 = PostFroyoUtils.SQLiteDatabaseComp.rawQuery(localSQLiteDatabase1, str5, null, localCancellationSignalComp);
        int i = 0;
        if (localCursor3 != null)
          i = localCursor3.getCount();
        int j = i;
        k = 0;
        if ((localCursor3 != null) && (localCursor3.moveToNext()))
        {
          long l = localCursor3.getLong(0);
          j[k] = l;
          k += 1;
          continue;
        }
        AutoPlaylistCursorFactory localAutoPlaylistCursorFactory1 = new com/google/android/music/store/AutoPlaylistCursorFactory;
        AutoPlaylistCursorFactory localAutoPlaylistCursorFactory2 = localAutoPlaylistCursorFactory1;
        Context localContext = paramContext;
        int m = j;
        localAutoPlaylistCursorFactory2.<init>(localContext, m);
        AutoPlaylistCursorFactory localAutoPlaylistCursorFactory3 = localAutoPlaylistCursorFactory1;
        String[] arrayOfString4 = arrayOfString1;
        localMatrixCursor1 = localAutoPlaylistCursorFactory3.buildCursor(arrayOfString4);
        localMatrixCursor2 = new android/database/MatrixCursor;
        MatrixCursor localMatrixCursor3 = localMatrixCursor2;
        String[] arrayOfString5 = paramArrayOfString;
        localMatrixCursor3.<init>(arrayOfString5);
        if ((localCursor1 == null) || (!localCursor1.moveToNext()))
          break;
        localArrayList1 = new java/util/ArrayList;
        int n = paramArrayOfString.length;
        localArrayList1.<init>(n);
        k = 0;
        int i1 = paramArrayOfString.length;
        if (k >= i1)
          break label593;
        int i2 = k;
        str6 = localCursor1.getColumnName(i2);
        if (str6.equals("_id"))
        {
          int i3 = k;
          Long localLong1 = Long.valueOf(localCursor1.getLong(i3));
          boolean bool1 = localArrayList1.add(localLong1);
          k += 1;
          continue;
        }
        if ((str6.equals("_title")) || (str6.equals("_subtitle")) || (str6.equals("DateAdded")))
        {
          int i4 = k;
          String str7 = localCursor1.getString(i4);
          boolean bool2 = localArrayList1.add(str7);
          continue;
        }
      }
      finally
      {
        paramStore.endRead(localSQLiteDatabase1);
      }
      if (str6.equals("_type"))
      {
        boolean bool3 = localArrayList1.add(null);
      }
      else
      {
        StringBuilder localStringBuilder4 = new StringBuilder().append("Ignoring projection: ");
        String str8 = str6;
        String str9 = str8;
        int i5 = Log.w("DownloadQueueHelper", str9);
        continue;
        label593: int i6 = localArrayList1.size();
        int i7 = paramArrayOfString.length;
        if (i6 != i7)
        {
          MatrixCursor localMatrixCursor4 = localMatrixCursor2;
          ArrayList localArrayList2 = localArrayList1;
          localMatrixCursor4.addRow(localArrayList2);
        }
      }
    }
    if (localCursor2 != null)
      while (localCursor2.moveToNext())
      {
        localArrayList1 = new java/util/ArrayList;
        int i8 = paramArrayOfString.length;
        localArrayList1.<init>(i8);
        k = 0;
        int i9 = paramArrayOfString.length;
        if (k < i9)
        {
          Cursor localCursor4 = localCursor2;
          int i10 = k;
          str6 = localCursor4.getColumnName(i10);
          if (str6.equals("_id"))
          {
            Cursor localCursor5 = localCursor2;
            int i11 = k;
            Long localLong2 = Long.valueOf(localCursor5.getLong(i11));
            boolean bool4 = localArrayList1.add(localLong2);
          }
          while (true)
          {
            k += 1;
            break;
            if ((str6.equals("_title")) || (str6.equals("_type")) || (str6.equals("DateAdded")))
            {
              Cursor localCursor6 = localCursor2;
              int i12 = k;
              String str10 = localCursor6.getString(i12);
              boolean bool5 = localArrayList1.add(str10);
            }
            else if (str6.equals("_subtitle"))
            {
              boolean bool6 = localArrayList1.add(null);
            }
            else
            {
              StringBuilder localStringBuilder5 = new StringBuilder().append("Ignoring projection: ");
              String str11 = str6;
              String str12 = str11;
              int i13 = Log.w("DownloadQueueHelper", str12);
            }
          }
        }
        int i14 = localArrayList1.size();
        int i15 = paramArrayOfString.length;
        if (i14 != i15)
        {
          MatrixCursor localMatrixCursor5 = localMatrixCursor2;
          ArrayList localArrayList3 = localArrayList1;
          localMatrixCursor5.addRow(localArrayList3);
        }
      }
    if (localMatrixCursor1 != null)
      while (localMatrixCursor1.moveToNext())
      {
        localArrayList1 = new java/util/ArrayList;
        int i16 = paramArrayOfString.length;
        localArrayList1.<init>(i16);
        k = 0;
        int i17 = paramArrayOfString.length;
        if (k < i17)
        {
          MatrixCursor localMatrixCursor6 = localMatrixCursor1;
          int i18 = k;
          str6 = localMatrixCursor6.getColumnName(i18);
          String str13 = arrayOfString1[0];
          if (str6.equals(str13))
          {
            MatrixCursor localMatrixCursor7 = localMatrixCursor1;
            int i19 = k;
            Long localLong3 = Long.valueOf(localMatrixCursor7.getLong(i19));
            boolean bool7 = localArrayList1.add(localLong3);
          }
          while (true)
          {
            k += 1;
            break;
            String str14 = arrayOfString1[1];
            if (!str6.equals(str14))
            {
              String str15 = arrayOfString1[3];
              if (!str6.equals(str15))
              {
                String str16 = arrayOfString1[4];
                if (!str6.equals(str16))
                  break label1094;
              }
            }
            MatrixCursor localMatrixCursor8 = localMatrixCursor1;
            int i20 = k;
            String str17 = localMatrixCursor8.getString(i20);
            boolean bool8 = localArrayList1.add(str17);
            continue;
            label1094: String str18 = arrayOfString1[2];
            if (str6.equals(str18))
            {
              boolean bool9 = localArrayList1.add(null);
            }
            else
            {
              StringBuilder localStringBuilder6 = new StringBuilder().append("Ignoring projection: ");
              String str19 = str6;
              String str20 = str19;
              int i21 = Log.w("DownloadQueueHelper", str20);
            }
          }
        }
        int i22 = localArrayList1.size();
        int i23 = paramArrayOfString.length;
        if (i22 != i23)
        {
          MatrixCursor localMatrixCursor9 = localMatrixCursor2;
          ArrayList localArrayList4 = localArrayList1;
          localMatrixCursor9.addRow(localArrayList4);
        }
      }
    ContentResolver localContentResolver = paramContext.getContentResolver();
    Uri localUri = MusicContent.DOWNLOAD_QUEUE_URI;
    localMatrixCursor2.setNotificationUri(localContentResolver, localUri);
    paramStore.endRead(localSQLiteDatabase1);
    return localMatrixCursor2;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.DownloadQueueContentProviderHelper
 * JD-Core Version:    0.6.2
 */