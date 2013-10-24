package com.google.android.music.sync;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.google.android.music.sync.api.MusicApiClientImpl;
import com.google.android.music.sync.common.AbstractSyncAdapter.Builder;
import com.google.android.music.sync.google.MusicAuthInfo;
import com.google.android.music.sync.google.MusicSyncAdapter;
import com.google.android.music.sync.google.MusicSyncAdapter.MusicSyncAdapterBuilder;

public class SyncAdapterService extends Service
{
  private static MusicSyncAdapter sSyncAdapter = null;
  private static final Object sSyncAdapterLock = new Object();

  public IBinder onBind(Intent paramIntent)
  {
    return sSyncAdapter.getSyncAdapterBinder();
  }

  public void onCreate()
  {
    synchronized (sSyncAdapterLock)
    {
      if (sSyncAdapter == null)
      {
        Context localContext1 = getApplicationContext();
        MusicAuthInfo localMusicAuthInfo = new MusicAuthInfo(localContext1);
        Context localContext2 = getApplicationContext();
        MusicApiClientImpl localMusicApiClientImpl = new MusicApiClientImpl(localContext2, localMusicAuthInfo);
        AbstractSyncAdapter.Builder localBuilder = MusicSyncAdapter.newBuilder().setMusicApiClient(localMusicApiClientImpl).setAuthInfo(localMusicAuthInfo).setActionOnInitialization(0);
        Context localContext3 = getApplicationContext();
        sSyncAdapter = (MusicSyncAdapter)localBuilder.build(localContext3);
      }
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.SyncAdapterService
 * JD-Core Version:    0.6.2
 */