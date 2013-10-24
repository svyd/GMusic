package com.google.android.music.store;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;

public class StoreService extends IStoreService.Stub
{
  private final Store mStore;

  private StoreService(Context paramContext)
  {
    Store localStore = Store.getInstance(paramContext);
    this.mStore = localStore;
  }

  public Map<Long, Long> getAlbumIdsAndAlbumKeepOnIdsForArtist(long paramLong)
  {
    HashMap localHashMap = Maps.newHashMap();
    Cursor localCursor = this.mStore.getAlbumIdsAndAlbumKeepOnIdsForArtist(paramLong);
    if (localCursor != null)
    {
      while (true)
      {
        try
        {
          if (!localCursor.moveToNext())
            break;
          Long localLong1 = Long.valueOf(localCursor.getLong(0));
          if (localCursor.isNull(1))
          {
            l1 = 65535L;
            Long localLong2 = Long.valueOf(l1);
            Object localObject1 = localHashMap.put(localLong1, localLong2);
            continue;
          }
        }
        finally
        {
          Store.safeClose(localCursor);
        }
        long l1 = 1;
        long l2 = localCursor.getLong(l1);
        l1 = l2;
      }
      Store.safeClose(localCursor);
    }
    return localHashMap;
  }

  public long[] getArtistIdsForAlbum(long paramLong)
  {
    Cursor localCursor = this.mStore.getArtistIdsForAlbum(paramLong);
    int i;
    if (localCursor == null)
      i = 0;
    while (true)
    {
      return i;
      try
      {
        int j = localCursor.getCount();
        int k = 0;
        if (localCursor.moveToNext())
        {
          long l = localCursor.getLong(0);
          j[k] = l;
        }
      }
      finally
      {
        Store.safeClose(localCursor);
      }
    }
  }

  public long getSizeAlbum(long paramLong)
  {
    return this.mStore.getSizeAlbum(paramLong);
  }

  public long getSizeAutoPlaylist(long paramLong)
  {
    return this.mStore.getSizeAutoPlaylist(paramLong);
  }

  public long getSizePlaylist(long paramLong)
  {
    return this.mStore.getSizePlaylist(paramLong);
  }

  public boolean isAlbumSelectedAsKeepOn(long paramLong, boolean paramBoolean)
  {
    return this.mStore.isAlbumSelectedAsKeepOn(paramLong, paramBoolean);
  }

  public boolean isArtistSelectedAsKeepOn(long paramLong)
  {
    return this.mStore.isArtistSelectedAsKeepOn(paramLong);
  }

  public boolean isAutoPlaylistSelectedAsKeepOn(long paramLong)
  {
    return this.mStore.isAutoPlaylistSelectedAsKeepOn(paramLong);
  }

  public boolean isPlaylistSelectedAsKeepOn(long paramLong)
  {
    return this.mStore.isPlaylistSelectedAsKeepOn(paramLong);
  }

  public int setRingtone(long paramLong, String paramString)
    throws RemoteException
  {
    int i = 1;
    MusicRingtoneManager localMusicRingtoneManager = this.mStore.getRingtoneManager();
    if (localMusicRingtoneManager != null)
      i = localMusicRingtoneManager.setRingtoneAttempt(paramLong, paramString, true);
    return i;
  }

  public static class StoreServiceBinder extends Service
  {
    private IStoreService.Stub mStoreService;

    public IBinder onBind(Intent paramIntent)
    {
      return this.mStoreService;
    }

    public void onCreate()
    {
      super.onCreate();
      StoreService localStoreService = new StoreService(this, null);
      this.mStoreService = localStoreService;
    }

    public void onDestroy()
    {
      super.onDestroy();
      this.mStoreService = null;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.StoreService
 * JD-Core Version:    0.6.2
 */