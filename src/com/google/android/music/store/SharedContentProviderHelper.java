package com.google.android.music.store;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;
import com.google.android.gsf.Gservices;
import com.google.android.music.cloudclient.GetSharedPlaylistEntriesResponseJson;
import com.google.android.music.cloudclient.GetSharedPlaylistEntriesResponseJson.Entry;
import com.google.android.music.cloudclient.MusicCloud;
import com.google.android.music.cloudclient.MusicCloudImpl;
import com.google.android.music.sync.google.model.SyncablePlaylistEntry;
import com.google.android.music.sync.google.model.Track;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SharedContentProviderHelper
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.CONTENT_PROVIDER);

  public static long createFollowedSharedPlaylist(Context paramContext, Account paramAccount, Store paramStore, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    String str1 = paramString4;
    List localList = getSharedSyncablePlaylistEntries(paramContext, str1);
    if (localList == null);
    Store localStore;
    Account localAccount;
    String str2;
    String str3;
    String str4;
    String str5;
    String str6;
    String str7;
    for (long l = 0L; ; l = localStore.createFollowedSharedPlaylist(localAccount, str2, str3, str4, str5, str6, str7, localList))
    {
      return l;
      localStore = paramStore;
      localAccount = paramAccount;
      str2 = paramString1;
      str3 = paramString2;
      str4 = paramString3;
      str5 = paramString4;
      str6 = paramString5;
      str7 = paramString6;
    }
  }

  private static GetSharedPlaylistEntriesResponseJson getSharedEntries(Context paramContext, String paramString1, int paramInt, String paramString2)
  {
    try
    {
      MusicCloudImpl localMusicCloudImpl = new MusicCloudImpl(paramContext);
      long l = 0;
      String str1 = paramString1;
      int i = paramInt;
      String str2 = paramString2;
      GetSharedPlaylistEntriesResponseJson localGetSharedPlaylistEntriesResponseJson1 = localMusicCloudImpl.getSharedEntries(str1, i, str2, l);
      localGetSharedPlaylistEntriesResponseJson2 = localGetSharedPlaylistEntriesResponseJson1;
      return localGetSharedPlaylistEntriesResponseJson2;
    }
    catch (InterruptedException localInterruptedException)
    {
      while (true)
      {
        String str3 = localInterruptedException.getMessage();
        int j = Log.w("SharedContentProviderHelper", str3, localInterruptedException);
        localGetSharedPlaylistEntriesResponseJson2 = null;
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        String str4 = localIOException.getMessage();
        int k = Log.w("SharedContentProviderHelper", str4, localIOException);
        GetSharedPlaylistEntriesResponseJson localGetSharedPlaylistEntriesResponseJson2 = null;
      }
    }
  }

  private static List<SyncablePlaylistEntry> getSharedSyncablePlaylistEntries(Context paramContext, String paramString)
  {
    String str1 = null;
    List localList1 = NautilusContentCache.getInstance(paramContext).getSharedPlaylistEntriesResponse(paramString);
    if (localList1 != null);
    int j;
    GetSharedPlaylistEntriesResponseJson localGetSharedPlaylistEntriesResponseJson;
    for (List localList2 = localList1; ; localList2 = localList1)
    {
      return localList2;
      int i = Gservices.getInt(paramContext.getContentResolver(), "music_downstream_page_size", 250);
      j = Gservices.getInt(paramContext.getContentResolver(), "music_max_shared_playlist_size", 1000);
      localGetSharedPlaylistEntriesResponseJson = getSharedEntries(paramContext, paramString, i, str1);
      if ((localGetSharedPlaylistEntriesResponseJson != null) && (localGetSharedPlaylistEntriesResponseJson.mEntries != null) && (!localGetSharedPlaylistEntriesResponseJson.mEntries.isEmpty()) && (((GetSharedPlaylistEntriesResponseJson.Entry)localGetSharedPlaylistEntriesResponseJson.mEntries.get(0)).mPlaylistEntry != null))
        break;
      int k = Log.w("SharedContentProviderHelper", "Failed to get playlist entries response");
      label114: if ((localList1 != null) && (!localList1.isEmpty()))
        NautilusContentCache.getInstance(paramContext).putSharedPlaylistEntriesResponse(paramString, localList1);
    }
    if (localList1 == null)
      localList1 = ((GetSharedPlaylistEntriesResponseJson.Entry)localGetSharedPlaylistEntriesResponseJson.mEntries.get(0)).mPlaylistEntry;
    while (true)
    {
      str1 = localGetSharedPlaylistEntriesResponseJson.mNextPageToken;
      if (str1 != null)
        break;
      break label114;
      int m = localList1.size();
      if (((GetSharedPlaylistEntriesResponseJson.Entry)localGetSharedPlaylistEntriesResponseJson.mEntries.get(0)).mPlaylistEntry.size() + m > j)
      {
        Object[] arrayOfObject = new Object[2];
        Integer localInteger = Integer.valueOf(j);
        arrayOfObject[0] = localInteger;
        arrayOfObject[1] = paramString;
        String str2 = String.format("Shared playlists is larger than the max allowed: %s shareToken=%s", arrayOfObject);
        int n = Log.w("SharedContentProviderHelper", str2);
        break label114;
      }
      List localList3 = ((GetSharedPlaylistEntriesResponseJson.Entry)localGetSharedPlaylistEntriesResponseJson.mEntries.get(0)).mPlaylistEntry;
      boolean bool = localList1.addAll(localList3);
    }
  }

  public static Uri insert(Context paramContext, Store paramStore, int paramInt, Uri paramUri)
  {
    Object localObject = null;
    boolean bool1 = Boolean.parseBoolean(paramUri.getQueryParameter("addToLibrary"));
    Uri localUri1 = null;
    List localList;
    if (paramInt == 1900)
    {
      String str = (String)paramUri.getPathSegments().get(1);
      localList = getSharedSyncablePlaylistEntries(paramContext, str);
      if (localList != null);
    }
    while (true)
    {
      return localObject;
      LinkedList localLinkedList = new LinkedList();
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        Track localTrack = ((SyncablePlaylistEntry)localIterator.next()).mTrack;
        boolean bool2 = localLinkedList.add(localTrack);
      }
      localUri1 = NautilusContentProviderHelper.insertTracks(paramContext, paramStore, localLinkedList, bool1);
      if ((localUri1 != null) && (bool1))
      {
        ContentResolver localContentResolver = paramContext.getContentResolver();
        Uri localUri2 = MusicContent.CONTENT_URI;
        localContentResolver.notifyChange(localUri2, null);
        RecentItemsManager.updateRecentItemsAsync(paramContext);
      }
      localObject = localUri1;
    }
  }

  public static Cursor query(Context paramContext, Uri paramUri, int paramInt, String[] paramArrayOfString)
  {
    MatrixCursor localMatrixCursor = new MatrixCursor(paramArrayOfString);
    List localList;
    if (paramInt == 1900)
    {
      String str = (String)paramUri.getPathSegments().get(1);
      localList = getSharedSyncablePlaylistEntries(paramContext, str);
      if (localList != null)
        break label51;
      localMatrixCursor = null;
    }
    while (true)
    {
      return localMatrixCursor;
      label51: if (MusicContentProvider.hasCount(paramArrayOfString))
      {
        Object[] arrayOfObject1 = new Object[1];
        Integer localInteger = Integer.valueOf(localList.size());
        arrayOfObject1[0] = localInteger;
        localMatrixCursor.addRow(arrayOfObject1);
      }
      else
      {
        Iterator localIterator = localList.iterator();
        while (localIterator.hasNext())
        {
          Object[] arrayOfObject2 = ProjectionUtils.project((SyncablePlaylistEntry)localIterator.next(), paramArrayOfString);
          localMatrixCursor.addRow(arrayOfObject2);
        }
      }
    }
  }

  public static void updateFollowedSharedPlaylist(Context paramContext, Account paramAccount, long paramLong, String paramString)
  {
    Store localStore = Store.getInstance(paramContext);
    long l1 = localStore.getSharedEntryLastUpdateTimeMs(paramLong);
    if (LOGV)
    {
      String str = "lastUpateTime=" + l1;
      int i = Log.d("SharedContentProviderHelper", str);
    }
    long l2 = Gservices.getLong(paramContext.getContentResolver(), "music_shared_playlist_update_delay_sec", 900L);
    if ((System.currentTimeMillis() - l1) / 1000L < l2)
    {
      if (!LOGV)
        return;
      int j = Log.d("SharedContentProviderHelper", "Not updating shared playlist as the request came too soon");
      return;
    }
    NautilusContentCache.getInstance(paramContext).removeSharedPlaylistEntries(paramString);
    List localList = getSharedSyncablePlaylistEntries(paramContext, paramString);
    if (localList == null)
      return;
    localStore.updateFollowedSharedPlaylistItems(paramAccount, paramString, localList);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.SharedContentProviderHelper
 * JD-Core Version:    0.6.2
 */