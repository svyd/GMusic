package com.google.android.music.sync.google;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gsf.Gservices;
import com.google.android.music.SharedPreferencesCompat;
import com.google.android.music.cloudclient.AlbumJson;
import com.google.android.music.cloudclient.ExploreEntityGroupJson;
import com.google.android.music.cloudclient.ExploreEntityJson;
import com.google.android.music.cloudclient.MusicCloud;
import com.google.android.music.cloudclient.MusicCloudImpl;
import com.google.android.music.cloudclient.NewReleasesResponseJson;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.RecentItemsManager;
import com.google.android.music.store.Store;
import com.google.android.music.sync.google.model.Track;
import com.google.android.music.sync.google.model.TrackFeed;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class MusicPeriodicUpdater
{
  public static boolean checkAndEnablePeriodicUpdate(Account paramAccount, Context paramContext)
  {
    long l1 = Gservices.getLong(paramContext.getContentResolver(), "music_periodic_sync_frequency_in_seconds", 0L);
    boolean bool;
    if (l1 == 0L)
    {
      int i = Log.i("MusicSyncAdapter", "Periodic sync is disabled");
      disablePeriodicSync(paramAccount);
      bool = false;
    }
    while (true)
    {
      return bool;
      if (l1 < 0L)
      {
        String str1 = "Periodic sync frequency is invalid: " + l1;
        int j = Log.e("MusicSyncAdapter", str1);
        bool = false;
      }
      else
      {
        enablePeriodicSync(paramAccount, l1);
        SharedPreferences localSharedPreferences = paramContext.getSharedPreferences("periodicUpdate.prefs", 0);
        String str2 = localSharedPreferences.getString("lastRunAccountName", null);
        String str3 = localSharedPreferences.getString("lastRunAccountType", null);
        if ((str2 != null) && (str3 != null))
        {
          Account localAccount = new Account(str2, str3);
          if (!localAccount.equals(paramAccount))
          {
            String str4 = "Account changed from " + localAccount + " to " + paramAccount;
            int k = Log.i("MusicSyncAdapter", str4);
            disablePeriodicSync(localAccount);
            bool = true;
          }
        }
        else
        {
          String str5 = "First periodic run for account: " + paramAccount;
          int m = Log.i("MusicSyncAdapter", str5);
          bool = true;
          continue;
          long l2 = localSharedPreferences.getLong("lastRunTime", 0L) / 1000L;
          if (l2 == 0L)
          {
            String str6 = "First periodic run for account: " + paramAccount;
            int n = Log.i("MusicSyncAdapter", str6);
            bool = true;
          }
          else
          {
            long l3 = System.currentTimeMillis() / 1000L;
            long l4 = l2 + l1;
            if (l3 >= l4)
            {
              String str7 = "Will attempt periodic sync for account: " + paramAccount;
              int i1 = Log.i("MusicSyncAdapter", str7);
              bool = true;
            }
            else if (l2 - l3 > 86400L)
            {
              String str8 = "Periodic sync last run time is too far in the future: " + l2;
              int i2 = Log.w("MusicSyncAdapter", str8);
              bool = true;
            }
            else
            {
              StringBuilder localStringBuilder = new StringBuilder().append("Skipping periodic sync. Last sync was ");
              long l5 = l3 - l2;
              String str9 = l5 + " seconds ago. Frequency: " + l1;
              int i3 = Log.i("MusicSyncAdapter", str9);
              bool = false;
            }
          }
        }
      }
    }
  }

  private static Bundle createPeriodicSyncExtras()
  {
    Bundle localBundle = new Bundle(1);
    localBundle.putString("com.google.android.music.PERIODIC_SYNC", "");
    return localBundle;
  }

  private static void disablePeriodicSync(Account paramAccount)
  {
    Bundle localBundle = createPeriodicSyncExtras();
    ContentResolver.removePeriodicSync(paramAccount, "com.google.android.music.MusicContent", localBundle);
  }

  private static void doPeriodicUpdate(Account paramAccount, Context paramContext)
  {
    int i = Log.i("MusicSyncAdapter", "Periodic update");
    Object localObject1 = new Object();
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, localObject1);
    try
    {
      boolean bool1 = localMusicPreferences.isNautilusEnabled();
      boolean bool2 = bool1;
      MusicPreferences.releaseMusicPreferences(localObject1);
      if (!bool2)
        return;
      populateNewReleases(paramAccount, paramContext);
      populateEphemeralTop(paramAccount, paramContext);
      return;
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(localObject1);
    }
  }

  private static void enablePeriodicSync(Account paramAccount, long paramLong)
  {
    Bundle localBundle = createPeriodicSyncExtras();
    ContentResolver.addPeriodicSync(paramAccount, "com.google.android.music.MusicContent", localBundle, paramLong);
  }

  public static void performPeriodicUpdate(Account paramAccount, Context paramContext)
  {
    try
    {
      doPeriodicUpdate(paramAccount, paramContext);
      recordSuccessfulRun(paramAccount, paramContext);
      return;
    }
    catch (Exception localException)
    {
      int i = Log.e("MusicSyncAdapter", "Periodic update failed", localException);
    }
  }

  private static void populateEphemeralTop(Account paramAccount, Context paramContext)
  {
    TrackFeed localTrackFeed;
    try
    {
      MusicCloudImpl localMusicCloudImpl = new MusicCloudImpl(paramContext);
      int i = Gservices.getInt(paramContext.getContentResolver(), "music_max_ephemeral_top_size", 1000);
      if (i == 0)
        return;
      localTrackFeed = localMusicCloudImpl.getEphemeralTopTracks(i, null, 0);
      if ((localTrackFeed == null) || (localTrackFeed.getItemList() == null) || (localTrackFeed.getItemList().isEmpty()))
      {
        int j = Log.w("MusicSyncAdapter", "No ephemeral top trcks");
        return;
      }
    }
    catch (InterruptedException localInterruptedException)
    {
      String str1 = localInterruptedException.getMessage();
      int k = Log.w("MusicSyncAdapter", str1, localInterruptedException);
      return;
      Iterator localIterator = localTrackFeed.getItemList().iterator();
      while (localIterator.hasNext())
        ((Track)localIterator.next()).mRemoteId = null;
    }
    catch (IOException localIOException)
    {
      String str2 = localIOException.getMessage();
      int m = Log.w("MusicSyncAdapter", str2, localIOException);
      return;
    }
    Store localStore = Store.getInstance(paramContext);
    List localList = localTrackFeed.getItemList();
    localStore.populateEphemeralTop(paramAccount, localList);
  }

  private static void populateNewReleases(Account paramAccount, Context paramContext)
  {
    try
    {
      NewReleasesResponseJson localNewReleasesResponseJson = new MusicCloudImpl(paramContext).getNewReleases(10);
      if ((localNewReleasesResponseJson == null) || (localNewReleasesResponseJson.mGroups == null) || (localNewReleasesResponseJson.mGroups.isEmpty()))
      {
        int i = Log.w("MusicSyncAdapter", "No recommended new releases");
        return;
      }
      localExploreEntityGroupJson = (ExploreEntityGroupJson)localNewReleasesResponseJson.mGroups.get(0);
      if ((localExploreEntityGroupJson == null) || (localExploreEntityGroupJson.mEntities == null) || (localExploreEntityGroupJson.mEntities.isEmpty()))
      {
        int j = Log.w("MusicSyncAdapter", "No recommended new releases");
        return;
      }
    }
    catch (InterruptedException localInterruptedException)
    {
      ExploreEntityGroupJson localExploreEntityGroupJson;
      String str1 = localInterruptedException.getMessage();
      int k = Log.w("MusicSyncAdapter", str1, localInterruptedException);
      return;
      Iterator localIterator = localExploreEntityGroupJson.mEntities.iterator();
      while (true)
      {
        if (!localIterator.hasNext())
          return;
        AlbumJson localAlbumJson = ((ExploreEntityJson)localIterator.next()).mAlbum;
        if (localAlbumJson != null)
          boolean bool = RecentItemsManager.addNewReleaseNautilusAlbum(paramContext, localAlbumJson);
      }
    }
    catch (IOException localIOException)
    {
      String str2 = localIOException.getMessage();
      int m = Log.w("MusicSyncAdapter", str2, localIOException);
    }
  }

  private static void recordSuccessfulRun(Account paramAccount, Context paramContext)
  {
    SharedPreferences.Editor localEditor1 = paramContext.getSharedPreferences("periodicUpdate.prefs", 0).edit();
    long l = System.currentTimeMillis();
    SharedPreferences.Editor localEditor2 = localEditor1.putLong("lastRunTime", l);
    String str1 = paramAccount.name;
    SharedPreferences.Editor localEditor3 = localEditor1.putString("lastRunAccountName", str1);
    String str2 = paramAccount.type;
    SharedPreferences.Editor localEditor4 = localEditor1.putString("lastRunAccountType", str2);
    SharedPreferencesCompat.apply(localEditor1);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.MusicPeriodicUpdater
 * JD-Core Version:    0.6.2
 */