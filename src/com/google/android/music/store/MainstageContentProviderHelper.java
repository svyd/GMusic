package com.google.android.music.store;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.Log;
import com.google.android.music.utils.DbUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.PostFroyoUtils.CancellationSignalComp;
import com.google.android.music.utils.PostFroyoUtils.SQLiteDatabaseComp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

class MainstageContentProviderHelper
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.CONTENT_PROVIDER);
  private static List<ItemCategory> sCategoryMapping;
  private static HashMap<String, String> sNewReleasesProjectionMap;
  private static HashMap<String, String> sRecentProjectionMap = new HashMap();
  private static HashMap<String, String> sRecentRadioProjectionMap;
  private static HashMap<String, String> sSuggestedMixesProjectionMap;

  static
  {
    MusicContentProvider.addNullAlbumMappings(sRecentProjectionMap);
    MusicContentProvider.addDefaultPlaylistMappings(sRecentProjectionMap);
    addMapping(sRecentProjectionMap, "playlist_id", "RecentListId");
    addMapping(sRecentProjectionMap, "reason", "RecentReason");
    addMapping(sRecentProjectionMap, "_id", "RecentId");
    addMapping(sRecentProjectionMap, "album_id", "RecentAlbumId");
    addMapping(sRecentProjectionMap, "album_artist_id", "AlbumArtistId");
    addMapping(sRecentProjectionMap, "StoreAlbumId", "RecentNautilusAlbumId");
    addMapping(sRecentProjectionMap, "artworkUrl", "RecentNautilusAlbumArt");
    addLocalAndNautilusAlbumMapping("album_name", "Album", "RecentNautilusAlbum");
    addLocalAndNautilusAlbumMapping("album_artist", "AlbumArtist", "RecentNautilusAlbumArtist");
    addLocalAndNautilusAlbumMapping("album_artist_sort", "CanonicalAlbumArtist", "RecentNautilusAlbumArtist");
    addLocalAndNautilusAlbumMapping("hasPersistNautilus", "EXISTS(select 1 from MUSIC WHERE AlbumId=RecentAlbumId AND TrackType=5 LIMIT 1)", "0");
    MusicContentProvider.addNullRadioMappings(sRecentProjectionMap);
    addOfflineMapping("hasLocal", "0", "EXISTS(select 1 from MUSIC WHERE AlbumId=RecentAlbumId AND LocalCopyType IN (100,200,300) LIMIT 1)", "EXISTS(select 1 from LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE ListId=RecentListId AND LocalCopyType IN (100,200,300) LIMIT 1)");
    addOfflineMapping("hasRemote", "1", "EXISTS(select 1 from MUSIC WHERE AlbumId=RecentAlbumId AND LocalCopyType != 300 LIMIT 1)", "EXISTS(select 1 from LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE ListId=RecentListId AND LocalCopyType != 300 LIMIT 1)");
    addOfflineMapping("KeepOnId", "null", "(select KeepOnId from KEEPON WHERE AlbumId=RecentAlbumId LIMIT 1)", "(select KeepOnId from KEEPON WHERE ListId=RecentListId LIMIT 1)");
    addOfflineMapping("isAllLocal", "0", "NOT EXISTS(select 1 from MUSIC AS m  WHERE MUSIC.AlbumId=m.AlbumId GROUP BY m.SongId HAVING MAX(m.LocalCopyType = 0) LIMIT 1)", "NOT EXISTS(select 1 from MUSIC AS m, LISTITEMS as i WHERE i.ListId=LISTS.Id AND m.Id=i.MusicId GROUP BY m.SongId HAVING MAX(m.LocalCopyType = 0) LIMIT 1)");
    sNewReleasesProjectionMap = new HashMap();
    MusicContentProvider.addNullAlbumMappings(sNewReleasesProjectionMap);
    MusicContentProvider.addNullPlaylistMappings(sNewReleasesProjectionMap);
    MusicContentProvider.addNullRadioMappings(sNewReleasesProjectionMap);
    addMapping(sNewReleasesProjectionMap, "StoreAlbumId", "RecentNautilusAlbumId");
    addMapping(sNewReleasesProjectionMap, "artworkUrl", "RecentNautilusAlbumArt");
    addMapping(sNewReleasesProjectionMap, "album_name", "RecentNautilusAlbum");
    addMapping(sNewReleasesProjectionMap, "album_artist", "RecentNautilusAlbumArtist");
    addMapping(sNewReleasesProjectionMap, "album_artist_sort", "RecentNautilusAlbumArtist");
    addMapping(sNewReleasesProjectionMap, "_id", "RecentId");
    addMapping(sNewReleasesProjectionMap, "reason", "RecentReason");
    addMapping(sNewReleasesProjectionMap, "hasLocal", "0");
    addMapping(sNewReleasesProjectionMap, "isAllLocal", "0");
    addMapping(sNewReleasesProjectionMap, "hasRemote", "1");
    addMapping(sNewReleasesProjectionMap, "KeepOnId", "null");
    sRecentRadioProjectionMap = new HashMap();
    MusicContentProvider.addNullAlbumMappings(sRecentRadioProjectionMap);
    MusicContentProvider.addNullPlaylistMappings(sRecentRadioProjectionMap);
    MusicContentProvider.addDefaultRadioMappings(sRecentRadioProjectionMap);
    addMapping(sRecentRadioProjectionMap, "reason", "RecentReason");
    addMapping(sRecentRadioProjectionMap, "_id", "RADIO_STATIONS.Id");
    addMapping(sRecentRadioProjectionMap, "hasLocal", "0");
    addMapping(sRecentRadioProjectionMap, "isAllLocal", "0");
    addMapping(sRecentRadioProjectionMap, "hasRemote", "1");
    addMapping(sRecentRadioProjectionMap, "KeepOnId", "null");
    sSuggestedMixesProjectionMap = new HashMap();
    MusicContentProvider.addNullAlbumMappings(sSuggestedMixesProjectionMap);
    MusicContentProvider.addNullRadioMappings(sSuggestedMixesProjectionMap);
    MusicContentProvider.addDefaultPlaylistMappings(sSuggestedMixesProjectionMap);
    addMapping(sSuggestedMixesProjectionMap, "item_id", "SUGGESTED_SEEDS.SeedListId");
    addMapping(sSuggestedMixesProjectionMap, "item_type", "'4'");
    addMapping(sSuggestedMixesProjectionMap, "item_name", "LISTS.Name");
    addMapping(sSuggestedMixesProjectionMap, "reason", "'100'");
    addMapping(sSuggestedMixesProjectionMap, "_id", "SUGGESTED_SEEDS.SeedListId");
    MusicContentProvider.addNullMapping(sSuggestedMixesProjectionMap, "KeepOnId");
    addMapping(sSuggestedMixesProjectionMap, "isAllLocal", "NOT EXISTS(select 1 from MUSIC AS m, LISTITEMS as i WHERE i.ListId=LISTS.Id AND m.Id=i.MusicId GROUP BY m.SongId HAVING MAX(m.LocalCopyType = 0) LIMIT 1)");
    addMapping(sSuggestedMixesProjectionMap, "hasLocal", "EXISTS (SELECT 1 FROM LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE (ListId=LISTS.Id) AND LocalCopyType IN (100,200,300) LIMIT 1)");
    addMapping(sSuggestedMixesProjectionMap, "hasRemote", "EXISTS (SELECT 1 FROM LISTITEMS JOIN MUSIC ON (LISTITEMS.MusicId=MUSIC.Id)  WHERE (ListId=LISTS.Id) AND LocalCopyType != 300 LIMIT 1)");
  }

  private static void addLocalAndNautilusAlbumMapping(String paramString1, String paramString2, String paramString3)
  {
    HashMap localHashMap = sRecentProjectionMap;
    String[] arrayOfString = new String[4];
    arrayOfString[0] = "RecentAlbumId";
    arrayOfString[1] = paramString2;
    arrayOfString[2] = "RecentNautilusAlbumId";
    arrayOfString[3] = paramString3;
    MusicContentProvider.addNotNullCaseMapping(localHashMap, paramString1, arrayOfString);
  }

  private static void addMapping(HashMap<String, String> paramHashMap, String paramString1, String paramString2)
  {
    MusicContentProvider.addMapping(paramHashMap, paramString1, paramString2);
  }

  private static void addOfflineMapping(String paramString1, String paramString2, String paramString3, String paramString4)
  {
    HashMap localHashMap = sRecentProjectionMap;
    String[] arrayOfString = new String[4];
    arrayOfString[0] = "RecentAlbumId";
    arrayOfString[1] = paramString3;
    arrayOfString[2] = "RecentListId";
    arrayOfString[3] = paramString4;
    MusicContentProvider.addNotNullCaseMappingWithDefault(localHashMap, paramString1, paramString2, arrayOfString);
  }

  private static List<ItemCategory> generateCategoryMapping()
  {
    LinkedList localLinkedList = new LinkedList();
    ItemCategory[] arrayOfItemCategory1 = ItemCategory.values();
    int i = arrayOfItemCategory1.length;
    HashMap localHashMap = new HashMap(i);
    ItemCategory[] arrayOfItemCategory2 = arrayOfItemCategory1;
    int j = arrayOfItemCategory2.length;
    int k = 0;
    while (k < j)
    {
      ItemCategory localItemCategory1 = arrayOfItemCategory2[k];
      Integer localInteger1 = Integer.valueOf(0);
      Object localObject1 = localHashMap.put(localItemCategory1, localInteger1);
      k += 1;
    }
    int m = 0;
    if (m < 100)
    {
      ItemCategory localItemCategory2 = ItemCategory.access$000();
      int n = ((Integer)localHashMap.get(localItemCategory2)).intValue();
      int i1 = localItemCategory2.mMaxNumItems;
      if (n < i1)
      {
        boolean bool1 = localLinkedList.add(localItemCategory2);
        Integer localInteger2 = Integer.valueOf(n + 1);
        Object localObject2 = localHashMap.put(localItemCategory2, localInteger2);
      }
      while (true)
      {
        m += 1;
        break;
        localItemCategory2 = getFallbackCategory(localItemCategory2, localHashMap);
        if (localItemCategory2 != null)
        {
          int i2 = ((Integer)localHashMap.get(localItemCategory2)).intValue();
          boolean bool2 = localLinkedList.add(localItemCategory2);
          Integer localInteger3 = Integer.valueOf(i2 + 1);
          Object localObject3 = localHashMap.put(localItemCategory2, localInteger3);
        }
      }
    }
    if (LOGV)
    {
      String str1 = "Generated category mappings: " + localLinkedList;
      int i3 = Log.d("MainstageContentProvider", str1);
      String str2 = "Category counts: " + localHashMap;
      int i4 = Log.d("MainstageContentProvider", str2);
    }
    return localLinkedList;
  }

  private static Map<ItemCategory, Cursor> getCursorsMap(Uri paramUri, String[] paramArrayOfString, Store paramStore)
  {
    LinkedHashMap localLinkedHashMap = new LinkedHashMap();
    int i = MusicContentProvider.getMusicFilterIndex(paramUri);
    ItemCategory localItemCategory1 = ItemCategory.RECENT;
    Cursor localCursor1 = queryRecent(paramUri, paramArrayOfString, paramStore, i);
    Object localObject1 = localLinkedHashMap.put(localItemCategory1, localCursor1);
    ItemCategory localItemCategory2 = ItemCategory.RECENT_RADIO;
    Cursor localCursor2 = queryRecentRadios(paramUri, paramArrayOfString, paramStore, i);
    Object localObject2 = localLinkedHashMap.put(localItemCategory2, localCursor2);
    ItemCategory localItemCategory3 = ItemCategory.SUGGESTED_MIX;
    Cursor localCursor3 = querySuggestedMixes(paramUri, paramArrayOfString, paramStore, i);
    Object localObject3 = localLinkedHashMap.put(localItemCategory3, localCursor3);
    ItemCategory localItemCategory4 = ItemCategory.NEW_RELEASE;
    Cursor localCursor4 = queryNewReleases(paramUri, paramArrayOfString, paramStore, i);
    Object localObject4 = localLinkedHashMap.put(localItemCategory4, localCursor4);
    return localLinkedHashMap;
  }

  private static ItemCategory getFallbackCategory(ItemCategory paramItemCategory, Map<ItemCategory, Integer> paramMap)
  {
    ItemCategory[] arrayOfItemCategory = ItemCategory.values();
    int i = paramItemCategory.ordinal() + 1;
    int j = 0;
    int k = arrayOfItemCategory.length;
    ItemCategory localItemCategory;
    if (j < k)
    {
      int m = arrayOfItemCategory.length;
      int n = i % m;
      localItemCategory = arrayOfItemCategory[n];
      int i1 = ((Integer)paramMap.get(localItemCategory)).intValue();
      int i2 = localItemCategory.mMaxNumItems;
      if (i1 < i2)
        if (LOGV)
        {
          String str1 = "Falling back to " + localItemCategory + " from " + paramItemCategory;
          int i3 = Log.d("MainstageContentProvider", str1);
        }
    }
    while (true)
    {
      return localItemCategory;
      i += 1;
      j += 1;
      break;
      if (LOGV)
      {
        String str2 = "Could not fall back from " + paramItemCategory;
        int i4 = Log.wtf("MainstageContentProvider", str2);
      }
      localItemCategory = null;
    }
  }

  static Cursor getMainstageContent(Context paramContext, Uri paramUri, String[] paramArrayOfString, Store paramStore)
  {
    MatrixCursor localMatrixCursor = new MatrixCursor(paramArrayOfString);
    Map localMap = getCursorsMap(paramUri, paramArrayOfString, paramStore);
    if (sCategoryMapping == null)
      sCategoryMapping = generateCategoryMapping();
    Iterator localIterator1 = sCategoryMapping.iterator();
    label286: 
    while (true)
    {
      ItemCategory localItemCategory;
      if (localIterator1.hasNext())
      {
        localItemCategory = (ItemCategory)localIterator1.next();
        if (!localMap.isEmpty());
      }
      else
      {
        Iterator localIterator2 = localMap.values().iterator();
        while (localIterator2.hasNext())
          Store.safeClose((Cursor)localIterator2.next());
      }
      Cursor localCursor1 = (Cursor)localMap.get(localItemCategory);
      if ((localCursor1 != null) && (localCursor1.moveToNext()))
      {
        DbUtils.addRowToMatrixCursor(localMatrixCursor, localCursor1);
      }
      else
      {
        if (LOGV)
        {
          String str = "No data found for " + localItemCategory + ". Falling back.";
          int i = Log.d("MainstageContentProvider", str);
        }
        Object localObject = localMap.remove(localItemCategory);
        Store.safeClose(localCursor1);
        Iterator localIterator3 = localMap.values().iterator();
        while (true)
        {
          if (!localIterator3.hasNext())
            break label286;
          Cursor localCursor2 = (Cursor)localIterator3.next();
          if ((localCursor2 != null) && (localCursor2.moveToNext()))
          {
            DbUtils.addRowToMatrixCursor(localMatrixCursor, localCursor2);
            break;
          }
          localIterator3.remove();
          Store.safeClose(localCursor2);
        }
      }
    }
    ContentResolver localContentResolver = paramContext.getContentResolver();
    localMatrixCursor.setNotificationUri(localContentResolver, paramUri);
    return localMatrixCursor;
  }

  private static Cursor queryNewReleases(Uri paramUri, String[] paramArrayOfString, Store paramStore, int paramInt)
  {
    Cursor localCursor = null;
    if (Filters.doesExcludeOnlineMusic(paramInt));
    while (true)
    {
      return localCursor;
      SQLiteQueryBuilder localSQLiteQueryBuilder = new SQLiteQueryBuilder();
      localSQLiteQueryBuilder.setTables("RECENT");
      HashMap localHashMap = sNewReleasesProjectionMap;
      localSQLiteQueryBuilder.setProjectionMap(localHashMap);
      String str1 = String.valueOf(10);
      String str2 = "RecentId";
      String str3 = "Priority DESC, ItemDate DESC";
      localSQLiteQueryBuilder.appendWhere("RecentReason=4");
      SQLiteDatabase localSQLiteDatabase = paramStore.beginRead();
      String str4 = null;
      String[] arrayOfString1 = null;
      String str5 = null;
      PostFroyoUtils.CancellationSignalComp localCancellationSignalComp = null;
      String[] arrayOfString2 = paramArrayOfString;
      try
      {
        localCursor = PostFroyoUtils.SQLiteDatabaseComp.query(localSQLiteQueryBuilder, localSQLiteDatabase, arrayOfString2, str4, arrayOfString1, str2, str5, str3, str1, localCancellationSignalComp);
        if (localCursor != null)
          int i = localCursor.getCount();
        paramStore.endRead(localSQLiteDatabase);
      }
      finally
      {
        paramStore.endRead(localSQLiteDatabase);
      }
    }
  }

  private static Cursor queryRecent(Uri paramUri, String[] paramArrayOfString, Store paramStore, int paramInt)
  {
    SQLiteQueryBuilder localSQLiteQueryBuilder = new SQLiteQueryBuilder();
    localSQLiteQueryBuilder.setTables("RECENT LEFT JOIN MUSIC ON (RecentAlbumId=MUSIC.AlbumId)  LEFT JOIN LISTS ON (RecentListId=LISTS.Id) ");
    HashMap localHashMap = sRecentProjectionMap;
    localSQLiteQueryBuilder.setProjectionMap(localHashMap);
    String str1 = String.valueOf(100);
    StringBuilder localStringBuilder1 = new StringBuilder();
    int i = Filters.setExternalFiltering(paramInt, true);
    int j = Filters.setExternalFiltering(paramInt, false);
    String str2 = "";
    if (i != 0)
    {
      StringBuilder localStringBuilder2 = new StringBuilder();
      StringBuilder localStringBuilder3 = localStringBuilder2;
      int k = i;
      MusicContentProvider.appendPlaylistFilteringCondition(localStringBuilder3, k);
      if (localStringBuilder2.length() > 0)
      {
        StringBuilder localStringBuilder4 = new StringBuilder().append(" AND ");
        String str3 = localStringBuilder2.toString();
        str2 = str3;
      }
    }
    String str4 = "";
    if (j != 3)
    {
      StringBuilder localStringBuilder5 = new StringBuilder().append(" AND ");
      String str5 = Filters.FILTERS[j];
      str4 = str5;
    }
    String str6 = "((LISTS.Id NOT NULL " + str2 + ") " + " OR " + "RecentNautilusAlbumId" + " NOT NULL" + " OR " + "(" + "MUSIC.AlbumId" + " NOT NULL " + str4 + "))" + " AND " + "RecentReason" + " NOT IN(" + 4 + "," + 5 + "," + 6 + ")";
    StringBuilder localStringBuilder6 = MusicContentProvider.appendAndCondition(localStringBuilder1, str6);
    if (Filters.doesExcludeOnlineMusic(paramInt))
      StringBuilder localStringBuilder7 = MusicContentProvider.appendAndCondition(localStringBuilder1, "RecentNautilusAlbumId IS NULL ");
    localSQLiteQueryBuilder.appendWhere(localStringBuilder1);
    SQLiteDatabase localSQLiteDatabase = paramStore.beginRead();
    String str7 = null;
    String[] arrayOfString1 = null;
    String str8 = null;
    PostFroyoUtils.CancellationSignalComp localCancellationSignalComp = null;
    String[] arrayOfString2 = paramArrayOfString;
    try
    {
      Cursor localCursor = PostFroyoUtils.SQLiteDatabaseComp.query(localSQLiteQueryBuilder, localSQLiteDatabase, arrayOfString2, str7, arrayOfString1, "RecentId", str8, "Priority DESC, ItemDate DESC", str1, localCancellationSignalComp);
      if (localCursor != null)
        int m = localCursor.getCount();
      return localCursor;
    }
    finally
    {
      paramStore.endRead(localSQLiteDatabase);
    }
  }

  private static Cursor queryRecentRadios(Uri paramUri, String[] paramArrayOfString, Store paramStore, int paramInt)
  {
    Cursor localCursor = null;
    if (Filters.doesExcludeOnlineMusic(paramInt));
    while (true)
    {
      return localCursor;
      SQLiteQueryBuilder localSQLiteQueryBuilder = new SQLiteQueryBuilder();
      localSQLiteQueryBuilder.setTables("RECENT JOIN RADIO_STATIONS ON (RecentRadioId=RADIO_STATIONS.Id) ");
      HashMap localHashMap = sRecentRadioProjectionMap;
      localSQLiteQueryBuilder.setProjectionMap(localHashMap);
      String str1 = String.valueOf(100);
      String str2 = "RecentId";
      String str3 = "Priority DESC, ItemDate DESC";
      localSQLiteQueryBuilder.appendWhere("RADIO_STATIONS.Id NOT NULL");
      SQLiteDatabase localSQLiteDatabase = paramStore.beginRead();
      String str4 = null;
      String[] arrayOfString1 = null;
      String str5 = null;
      PostFroyoUtils.CancellationSignalComp localCancellationSignalComp = null;
      String[] arrayOfString2 = paramArrayOfString;
      try
      {
        localCursor = PostFroyoUtils.SQLiteDatabaseComp.query(localSQLiteQueryBuilder, localSQLiteDatabase, arrayOfString2, str4, arrayOfString1, str2, str5, str3, str1, localCancellationSignalComp);
        if (localCursor != null)
          int i = localCursor.getCount();
        paramStore.endRead(localSQLiteDatabase);
      }
      finally
      {
        paramStore.endRead(localSQLiteDatabase);
      }
    }
  }

  private static Cursor querySuggestedMixes(Uri paramUri, String[] paramArrayOfString, Store paramStore, int paramInt)
  {
    SQLiteQueryBuilder localSQLiteQueryBuilder = new SQLiteQueryBuilder();
    String str1 = String.valueOf(10);
    localSQLiteQueryBuilder.setTables("LISTS JOIN SUGGESTED_SEEDS ON (LISTS.Id=SUGGESTED_SEEDS.SeedListId)  LEFT  JOIN KEEPON ON (KEEPON.ListId = LISTS.Id) ");
    HashMap localHashMap = sSuggestedMixesProjectionMap;
    localSQLiteQueryBuilder.setProjectionMap(localHashMap);
    int i = Filters.setExternalFiltering(paramInt, true);
    StringBuilder localStringBuilder = new StringBuilder();
    MusicContentProvider.appendPlaylistFilteringCondition(localStringBuilder, i);
    if (localStringBuilder.length() > 0)
    {
      String str2 = localStringBuilder.toString();
      localSQLiteQueryBuilder.appendWhere(str2);
    }
    SQLiteDatabase localSQLiteDatabase = paramStore.beginRead();
    String str3 = null;
    String[] arrayOfString1 = null;
    String str4 = null;
    String str5 = null;
    PostFroyoUtils.CancellationSignalComp localCancellationSignalComp = null;
    String[] arrayOfString2 = paramArrayOfString;
    try
    {
      Cursor localCursor = PostFroyoUtils.SQLiteDatabaseComp.query(localSQLiteQueryBuilder, localSQLiteDatabase, arrayOfString2, str3, arrayOfString1, str4, str5, "SeedOrder", str1, localCancellationSignalComp);
      if (localCursor != null)
        int j = localCursor.getCount();
      return localCursor;
    }
    finally
    {
      paramStore.endRead(localSQLiteDatabase);
    }
  }

  private static enum ItemCategory
  {
    private static TreeMap<Float, ItemCategory> sRangeMap;
    private int mMaxNumItems;
    private float mProbability;

    static
    {
      NEW_RELEASE = new ItemCategory("NEW_RELEASE", 3, 0.14F, 5);
      ItemCategory[] arrayOfItemCategory1 = new ItemCategory[4];
      ItemCategory localItemCategory1 = RECENT;
      arrayOfItemCategory1[0] = localItemCategory1;
      ItemCategory localItemCategory2 = RECENT_RADIO;
      arrayOfItemCategory1[1] = localItemCategory2;
      ItemCategory localItemCategory3 = SUGGESTED_MIX;
      arrayOfItemCategory1[2] = localItemCategory3;
      ItemCategory localItemCategory4 = NEW_RELEASE;
      arrayOfItemCategory1[3] = localItemCategory4;
      $VALUES = arrayOfItemCategory1;
      sRangeMap = new TreeMap();
      float f1 = 0.0F;
      ItemCategory[] arrayOfItemCategory2 = values();
      int i = arrayOfItemCategory2.length;
      int j = 0;
      while (j < i)
      {
        ItemCategory localItemCategory5 = arrayOfItemCategory2[j];
        TreeMap localTreeMap = sRangeMap;
        Float localFloat = Float.valueOf(f1);
        Object localObject = localTreeMap.put(localFloat, localItemCategory5);
        float f2 = localItemCategory5.mProbability;
        f1 += f2;
        j += 1;
      }
      if (Math.abs(f1 - 1.0F) <= 0.001F)
        return;
      String str = "The probabilities do not add up to 1: " + f1;
      throw new RuntimeException(str);
    }

    private ItemCategory(float paramFloat, int paramInt)
    {
      this.mProbability = paramFloat;
      this.mMaxNumItems = paramInt;
    }

    private static ItemCategory getRandomCategory()
    {
      float f1 = (float)Math.random();
      ItemCategory localItemCategory;
      if (Build.VERSION.SDK_INT >= 9)
      {
        TreeMap localTreeMap1 = sRangeMap;
        Float localFloat1 = Float.valueOf(f1);
        localItemCategory = (ItemCategory)localTreeMap1.floorEntry(localFloat1).getValue();
      }
      while (true)
      {
        return localItemCategory;
        float f2 = 0.0F;
        Iterator localIterator = sRangeMap.keySet().iterator();
        while (true)
        {
          if (!localIterator.hasNext())
            break label120;
          float f3 = ((Float)localIterator.next()).floatValue();
          if (f3 > f1)
          {
            TreeMap localTreeMap2 = sRangeMap;
            Float localFloat2 = Float.valueOf(f2);
            localItemCategory = (ItemCategory)localTreeMap2.get(localFloat2);
            break;
          }
          f2 = f3;
        }
        label120: TreeMap localTreeMap3 = sRangeMap;
        Float localFloat3 = Float.valueOf(f2);
        localItemCategory = (ItemCategory)localTreeMap3.get(localFloat3);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.MainstageContentProviderHelper
 * JD-Core Version:    0.6.2
 */