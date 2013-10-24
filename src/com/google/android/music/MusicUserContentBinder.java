package com.google.android.music;

import android.accounts.Account;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.music.download.artwork.ArtDownloadService;
import com.google.android.music.medialist.AlbumSongList;
import com.google.android.music.medialist.PlaylistSongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent.AlbumArt;
import com.google.android.music.store.MusicContent.Albums;
import com.google.android.music.store.MusicContent.PlaylistArt;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.store.Store;
import com.google.android.music.ui.AppNavigation;
import com.google.android.play.IUserContentService.Stub;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MusicUserContentBinder extends IUserContentService.Stub
{
  private static volatile boolean sAlbumArtReceiverRegistered = false;
  private static final BroadcastReceiver sAlbumChangeReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      MusicUserContentBinder.notifyContentChanged(paramAnonymousContext);
    }
  };
  private final Context mContext;

  public MusicUserContentBinder(Context paramContext)
  {
    this.mContext = paramContext;
  }

  private List<Bundle> getWhatsNext(int paramInt)
    throws RemoteException
  {
    LinkedList localLinkedList = Lists.newLinkedList();
    populateRecents(localLinkedList, paramInt);
    MusicPreferences localMusicPreferences;
    if (localLinkedList.size() == 0)
      localMusicPreferences = MusicPreferences.getMusicPreferences(this.mContext, this);
    try
    {
      Account localAccount1 = localMusicPreferences.getStreamingAccount();
      Account localAccount2 = localAccount1;
      MusicPreferences.releaseMusicPreferences(this);
      if ((localAccount2 != null) && (isSyncPendingOrActive(localAccount2)))
        localLinkedList = null;
      return localLinkedList;
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(this);
    }
  }

  private static boolean isSyncPendingOrActive(Account paramAccount)
  {
    if ((ContentResolver.isSyncActive(paramAccount, "com.google.android.music.MusicContent")) || (ContentResolver.isSyncPending(paramAccount, "com.google.android.music.MusicContent")));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static void notifyContentChanged(Context paramContext)
  {
    Intent localIntent1 = new Intent("com.google.android.play.CONTENT_UPDATE");
    Intent localIntent2 = localIntent1.putExtra("Play.DataType", 0);
    Intent localIntent3 = localIntent1.putExtra("Play.BackendId", 2);
    paramContext.sendBroadcast(localIntent1);
    if (!Log.isLoggable("MusicUserContentService", 2))
      return;
    int i = Log.v("MusicUserContentService", "Sending out com.google.android.play.CONTENT_UPDATE broadcast");
  }

  private void populateRecents(List<Bundle> paramList, int paramInt)
  {
    boolean bool1 = Log.isLoggable("MusicUserContentService", 2);
    String[] arrayOfString = new String[9];
    arrayOfString[0] = "RECENT.RecentAlbumId";
    arrayOfString[1] = "MUSIC.Album";
    arrayOfString[2] = "MUSIC.AlbumArtist";
    arrayOfString[3] = "RECENT.RecentListId";
    arrayOfString[4] = "LISTS.Name";
    arrayOfString[5] = "LISTS.ListType";
    arrayOfString[6] = "RECENT.ItemDate";
    arrayOfString[7] = "MUSIC.AlbumArtLocation";
    arrayOfString[8] = "ARTWORK.LocalLocation";
    Cursor localCursor = Store.getInstance(this.mContext).getRecentsJoinedWithArtwork(arrayOfString);
    if (localCursor == null)
    {
      int i = Log.w("MusicUserContentService", "Recent URI returned a null cursor.  Not returning any recents");
      return;
    }
    label778: 
    while (true)
    {
      LinkedList localLinkedList1;
      long l3;
      boolean bool2;
      try
      {
        int j = paramList.size() + paramInt;
        localLinkedList1 = Lists.newLinkedList();
        if (!localCursor.moveToNext())
          break label733;
        int k = paramList.size();
        int m = j;
        if (k >= m)
          break label733;
        long l1 = localCursor.getLong(6);
        Object localObject1 = null;
        localObject3 = null;
        Object localObject4 = null;
        long l2;
        if (!localCursor.isNull(0))
          l2 = localCursor.getLong(0);
        if (localCursor.isNull(3))
          break label778;
        l3 = localCursor.getLong(3);
        if (!localCursor.isNull(5))
          int n = localCursor.getInt(5);
        if (l2 == 65535L)
          break label561;
        String str1 = localCursor.getString(1);
        String str2 = localCursor.getString(2);
        localObject5 = new AlbumSongList(l2, str1, str2, false);
        Uri localUri1 = MusicContent.Albums.getAlbumsUri(l2);
        bool2 = localCursor.isNull(8);
        Long localLong = Long.valueOf(l2);
        boolean bool3 = localLinkedList1.add(localLong);
        if (bool2)
        {
          localObject3 = MusicContent.AlbumArt.getFauxAlbumArtUri(l2, -1, -1);
          if (localCursor.isNull(7))
            break label763;
          Context localContext1 = this.mContext;
          Intent localIntent1 = new Intent(localContext1, ArtDownloadService.class);
          Intent localIntent2 = localIntent1.setAction("com.android.music.REMOTE_ART_REQUESTED");
          Intent localIntent3 = localIntent1.putExtra("albumId", l2);
          ComponentName localComponentName = this.mContext.startService(localIntent1);
          localObject2 = localObject5;
          bool4 = bool2;
          localObject5 = localObject3;
          localObject3 = new Bundle();
          Intent localIntent4 = AppNavigation.getShowSonglistIntent(this.mContext, localObject2);
          ((Bundle)localObject3).putParcelable("Play.ViewIntent", localIntent4);
          long l4 = l1;
          ((Bundle)localObject3).putLong("Play.LastUpdateTimeMillis", l4);
          ((Bundle)localObject3).putParcelable("Play.ImageUri", (Parcelable)localObject5);
          ((Bundle)localObject3).putBoolean("Play.IsGenerated", bool4);
          boolean bool5 = paramList.add(localObject3);
          if (!bool1)
            continue;
          String str3 = "Adding bundle to return: " + localObject3;
          int i1 = Log.v("MusicUserContentService", str3);
          continue;
        }
      }
      finally
      {
        localCursor.close();
      }
      Object localObject3 = null;
      int i2 = -1;
      int i3 = -1;
      Uri localUri2 = MusicContent.AlbumArt.getAlbumArtUri(localObject2, ()localObject3, i2, i3);
      boolean bool4 = bool2;
      Object localObject8 = localObject5;
      Object localObject5 = localUri2;
      Object localObject2 = localObject8;
      continue;
      label561: if (l3 == 65535L)
      {
        String str4 = "Recents must return an album or playlist (albumId:" + localObject2 + " playlistId: " + l3 + ")";
        int i4 = Log.wtf("MusicUserContentService", str4);
      }
      else if (localObject5 == 10)
      {
        if (bool1)
          int i5 = Log.w("MusicUserContentService", "Recents should not contain the play queue.");
      }
      else
      {
        long l5 = localCursor.getLong(3);
        String str5 = localCursor.getString(4);
        int i6 = localCursor.getInt(5);
        PlaylistSongList localPlaylistSongList = new PlaylistSongList(l5, str5, i6, null, null, null, null, null, false);
        Uri localUri3 = MusicContent.Playlists.getPlaylistUri(l3);
        Uri localUri4 = MusicContent.PlaylistArt.getPlaylistArtUri(l3, -1, -1);
        bool4 = true;
        Uri localUri5 = localUri4;
        localObject2 = localPlaylistSongList;
        localObject5 = localUri5;
        continue;
        label733: Context localContext2 = this.mContext;
        MusicUserContentBinder localMusicUserContentBinder = this;
        LinkedList localLinkedList2 = localLinkedList1;
        localMusicUserContentBinder.registerAlbumArtListeners(localContext2, localLinkedList2);
        localCursor.close();
        return;
        label763: localObject2 = localObject5;
        bool4 = bool2;
        localObject5 = localObject3;
        continue;
        Object localObject6 = localObject3;
      }
    }
  }

  private void registerAlbumArtListeners(Context paramContext, List<Long> paramList)
  {
    Context localContext = paramContext.getApplicationContext();
    while (true)
    {
      IntentFilter localIntentFilter;
      synchronized (sAlbumChangeReceiver)
      {
        if (sAlbumArtReceiverRegistered)
        {
          BroadcastReceiver localBroadcastReceiver2 = sAlbumChangeReceiver;
          localContext.unregisterReceiver(localBroadcastReceiver2);
          sAlbumArtReceiverRegistered = false;
        }
        if ((paramList == null) || (paramList.size() == 0))
          return;
        Iterator localIterator = paramList.iterator();
        if (!localIterator.hasNext())
          break;
        Long localLong = (Long)localIterator.next();
        localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("com.google.android.music.AlbumArtChanged");
        Uri localUri = MusicContent.Albums.getAlbumsUri(localLong.longValue());
        String str1 = localUri.getAuthority();
        localIntentFilter.addDataAuthority(str1, null);
        String str2 = localUri.getPath();
        localIntentFilter.addDataPath(str2, 0);
        String str3 = localUri.getScheme();
        localIntentFilter.addDataScheme(str3);
        localIntentFilter.addCategory("android.intent.category.DEFAULT");
      }
      try
      {
        localIntentFilter.addDataType("vnd.android.cursor.item/vnd.google.music.album");
        BroadcastReceiver localBroadcastReceiver3 = sAlbumChangeReceiver;
        Intent localIntent = localContext.registerReceiver(localBroadcastReceiver3, localIntentFilter);
        continue;
        localObject = finally;
        throw localObject;
      }
      catch (IntentFilter.MalformedMimeTypeException localMalformedMimeTypeException)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Error setting data type on filter: ");
        String str4 = localMalformedMimeTypeException.getMessage();
        String str5 = str4;
        int i = Log.e("MusicUserContentService", str5);
      }
    }
    sAlbumArtReceiverRegistered = true;
  }

  public List<Bundle> getDocuments(int paramInt1, int paramInt2)
    throws RemoteException
  {
    switch (paramInt1)
    {
    default:
      String str = "Unknown dataTypeToFetch: " + paramInt1;
      int i = Log.e("MusicUserContentService", str);
      throw new RemoteException();
    case 0:
    }
    return getWhatsNext(paramInt2);
  }

  public static class MusicUserContentService extends Service
  {
    private MusicUserContentBinder mBinder;

    public IBinder onBind(Intent paramIntent)
    {
      return this.mBinder;
    }

    public void onCreate()
    {
      super.onCreate();
      MusicUserContentBinder localMusicUserContentBinder = new MusicUserContentBinder(this);
      this.mBinder = localMusicUserContentBinder;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.MusicUserContentBinder
 * JD-Core Version:    0.6.2
 */