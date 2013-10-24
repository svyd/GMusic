package com.google.android.music.sync.google;

import android.accounts.Account;
import android.accounts.AuthenticatorException;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.content.SyncStats;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.util.EventLog;
import com.google.android.gsf.Gservices;
import com.google.android.gsf.SubscribedFeeds.Feeds;
import com.google.android.music.MusicGservicesKeys;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.AlbumSongList;
import com.google.android.music.medialist.AutoPlaylist;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.ConfigContent;
import com.google.android.music.store.ConfigItem;
import com.google.android.music.store.ConfigStore;
import com.google.android.music.store.MediaStoreImporter;
import com.google.android.music.store.MusicContent;
import com.google.android.music.store.RecentItemsManager;
import com.google.android.music.store.Store;
import com.google.android.music.sync.api.BadRequestException;
import com.google.android.music.sync.api.ForbiddenException;
import com.google.android.music.sync.api.MusicApiClient;
import com.google.android.music.sync.api.MusicApiClient.GetResult;
import com.google.android.music.sync.api.NotModifiedException;
import com.google.android.music.sync.api.ResourceNotFoundException;
import com.google.android.music.sync.api.ServiceUnavailableException;
import com.google.android.music.sync.common.AbstractSyncAdapter;
import com.google.android.music.sync.common.AbstractSyncAdapter.Builder;
import com.google.android.music.sync.common.AbstractSyncAdapter.DownstreamFetchQueue;
import com.google.android.music.sync.common.AbstractSyncAdapter.DownstreamMergeQueue;
import com.google.android.music.sync.common.AbstractSyncAdapter.UpstreamQueue;
import com.google.android.music.sync.common.ClosableBlockingQueue.QueueClosedException;
import com.google.android.music.sync.common.DownstreamMerger;
import com.google.android.music.sync.common.DownstreamReader;
import com.google.android.music.sync.common.HardSyncException;
import com.google.android.music.sync.common.SoftSyncException;
import com.google.android.music.sync.common.SyncHttpException;
import com.google.android.music.sync.common.UpstreamReader;
import com.google.android.music.sync.common.UpstreamSender;
import com.google.android.music.sync.google.model.ConfigEntry;
import com.google.android.music.sync.google.model.MusicQueueableSyncEntity;
import com.google.android.music.ui.AppNavigation;
import com.google.android.music.utils.ConfigUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncWorkers;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class MusicSyncAdapter extends AbstractSyncAdapter
{
  private String ALARM_NAUTILUS_EXPIRATION_ACTION = "com.google.android.music.sync.EXP_ALARM";
  private final boolean LOGV;
  private long mInitialPlaylistEntryVersion;
  private long mInitialPlaylistVersion;
  private long mInitialRadioStationVersion;
  private long mInitialTrackVersion;
  private MusicApiClient mMusicApiClient;
  private Notification mSyncingNotification;

  public MusicSyncAdapter(Context paramContext)
  {
    super(paramContext);
    this.mTag = "MusicSyncAdapter";
    boolean bool = DebugUtils.isLoggable(DebugUtils.MusicTag.SYNC);
    this.LOGV = bool;
  }

  private PendingIntent createAlbumIntent(long paramLong, String paramString1, String paramString2)
  {
    long l = paramLong;
    String str1 = paramString1;
    String str2 = paramString2;
    AlbumSongList localAlbumSongList = new AlbumSongList(l, str1, str2, false);
    Intent localIntent = AppNavigation.getShowSonglistIntent(this.mContext, localAlbumSongList);
    return PendingIntent.getActivity(this.mContext, 0, localIntent, 0);
  }

  private PendingIntent createFreeAndPurchasedIntent()
  {
    Object localObject1 = new Object();
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this.mContext, localObject1);
    try
    {
      AutoPlaylist localAutoPlaylist = AutoPlaylist.getAutoPlaylist(65533L, false, localMusicPreferences);
      Intent localIntent = AppNavigation.getShowSonglistIntent(this.mContext, localAutoPlaylist);
      PendingIntent localPendingIntent1 = PendingIntent.getActivity(this.mContext, 0, localIntent, 0);
      PendingIntent localPendingIntent2 = localPendingIntent1;
      return localPendingIntent2;
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(localObject1);
    }
  }

  private boolean getChangesFromServerAsDom(Account paramAccount, AbstractSyncAdapter.DownstreamFetchQueue paramDownstreamFetchQueue, HashMap<String, Object> paramHashMap)
    throws AuthenticatorException, HardSyncException, SoftSyncException
  {
    boolean bool1 = false;
    Object localObject1 = paramHashMap.get("downstream_state");
    DownstreamState localDownstreamState = (DownstreamState)DownstreamState.class.cast(localObject1);
    Object localObject2 = paramHashMap.get("continuation");
    String str1 = (String)String.class.cast(localObject2);
    if (this.LOGV)
    {
      String str2 = this.mTag;
      StringBuilder localStringBuilder1 = new StringBuilder().append("Fetcher: Getting changes from server for ");
      MusicSyncAdapter.DownstreamState.Feed localFeed1 = localDownstreamState.getNextFeedToSync();
      String str3 = localFeed1 + " with continuation token " + str1;
      Log.v(str2, str3);
    }
    boolean bool2 = ((Boolean)paramHashMap.get("is_manual_sync")).booleanValue();
    try
    {
      i = Gservices.getInt(getContext().getContentResolver(), "music_downstream_page_size", 250);
      MusicSyncAdapter.DownstreamState.Feed localFeed2 = localDownstreamState.getNextFeedToSync();
      int[] arrayOfInt = 2.$SwitchMap$com$google$android$music$sync$google$MusicSyncAdapter$DownstreamState$Feed;
      int j = localFeed2.ordinal();
      switch (arrayOfInt[j])
      {
      default:
        String str4 = "Unknown feed type for downstream sync: " + localFeed2;
        throw new IOException(str4);
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      }
    }
    catch (SyncHttpException localSyncHttpException)
    {
      String str5 = localSyncHttpException.getMessage();
      Log.e("MusicSyncAdapter", str5, localSyncHttpException);
      StringBuilder localStringBuilder2 = new StringBuilder().append("Http code ");
      int k = localSyncHttpException.getStatusCode();
      String str6 = k + " on fetch";
      throw new HardSyncException(str6, localSyncHttpException);
      Object localObject3 = paramHashMap.get("etag_track");
      String str7 = (String)String.class.cast(localObject3);
      MusicApiClient localMusicApiClient1 = this.mMusicApiClient;
      long l1 = this.mInitialTrackVersion;
      Account localAccount1 = paramAccount;
      localGetResult = localMusicApiClient1.getTracks(localAccount1, i, str7, l1, str1);
      if (localGetResult.mEtag != null)
      {
        String str8 = localGetResult.mEtag;
        Object localObject4 = paramHashMap.put("etag_track", str8);
      }
      while (true)
      {
        String str9 = localGetResult.mContinuationToken;
        Object localObject5 = paramHashMap.put("continuation", str9);
        if (localGetResult.mItems != null)
          break;
        if (Log.isLoggable("MusicSyncAdapter", 2))
        {
          StringBuilder localStringBuilder3 = new StringBuilder().append("No mutations found for feed ");
          MusicSyncAdapter.DownstreamState.Feed localFeed3 = localDownstreamState.getNextFeedToSync();
          String str10 = localFeed3;
          Log.v("MusicSyncAdapter", str10);
        }
        if (localGetResult.mContinuationToken != null)
          break label990;
        localDownstreamState.onDoneWithFeed();
        Object localObject6 = paramHashMap.remove("continuation");
        if (localDownstreamState.getNextFeedToSync() != null)
          bool1 = true;
        return bool1;
        Object localObject7 = paramHashMap.get("etag_playlist");
        String str11 = (String)String.class.cast(localObject7);
        MusicApiClient localMusicApiClient2 = this.mMusicApiClient;
        long l2 = this.mInitialPlaylistVersion;
        Account localAccount2 = paramAccount;
        localGetResult = localMusicApiClient2.getPlaylists(localAccount2, i, str11, l2, str1);
        if (localGetResult.mEtag != null)
        {
          String str12 = localGetResult.mEtag;
          Object localObject8 = paramHashMap.put("etag_playlist", str12);
        }
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        throw new SoftSyncException(localIOException);
        Object localObject9 = paramHashMap.get("etag_playlist_entry");
        String str13 = (String)String.class.cast(localObject9);
        MusicApiClient localMusicApiClient3 = this.mMusicApiClient;
        long l3 = this.mInitialPlaylistEntryVersion;
        Account localAccount3 = paramAccount;
        localGetResult = localMusicApiClient3.getPlaylistEntries(localAccount3, i, str13, l3, str1);
        if (localGetResult.mEtag != null)
        {
          String str14 = localGetResult.mEtag;
          Object localObject10 = paramHashMap.put("etag_playlist_entry", str14);
        }
      }
    }
    catch (BadRequestException localBadRequestException)
    {
      while (true)
      {
        int i;
        throw new HardSyncException(localBadRequestException);
        String str15 = null;
        MusicApiClient localMusicApiClient4 = this.mMusicApiClient;
        long l4 = this.mInitialRadioStationVersion;
        Account localAccount4 = paramAccount;
        localGetResult = localMusicApiClient4.getRadioStations(localAccount4, i, str15, l4, str1);
        continue;
        localGetResult = this.mMusicApiClient.getConfig(paramAccount, bool2);
        processConfigResult(localGetResult);
      }
    }
    catch (ForbiddenException localForbiddenException)
    {
      throw new HardSyncException(localForbiddenException);
    }
    catch (ResourceNotFoundException localResourceNotFoundException)
    {
      throw new HardSyncException(localResourceNotFoundException);
    }
    catch (ServiceUnavailableException localServiceUnavailableException)
    {
      SoftSyncException localSoftSyncException = new SoftSyncException(localServiceUnavailableException);
      long l5 = localServiceUnavailableException.getRetryAfter();
      localSoftSyncException.setRetryAfter(l5);
      throw localSoftSyncException;
    }
    catch (NotModifiedException localNotModifiedException)
    {
      while (true)
      {
        MusicApiClient.GetResult localGetResult = new MusicApiClient.GetResult(null, null, null);
        continue;
        int m = 0;
        label818: if (localGetResult.mItems.hasNext())
        {
          int n = m + 1;
          try
          {
            Object localObject11 = localGetResult.mItems.next();
            paramDownstreamFetchQueue.put(localObject11);
          }
          catch (ClosableBlockingQueue.QueueClosedException localQueueClosedException)
          {
            if (!Log.isLoggable("MusicSyncAdapter", 2))
              break label818;
            String str16 = n + "The reader has killed the fetch queue, so there's " + "no point in having the fetcher continue.";
            Log.v("MusicSyncAdapter", str16);
            continue;
          }
          catch (InterruptedException localInterruptedException)
          {
            throw new SoftSyncException(localInterruptedException);
          }
        }
        else if (Log.isLoggable("MusicSyncAdapter", 2))
        {
          StringBuilder localStringBuilder4 = new StringBuilder().append("Fetcher: ").append(m).append(" ");
          MusicSyncAdapter.DownstreamState.Feed localFeed4 = localDownstreamState.getNextFeedToSync();
          String str17 = localFeed4 + "  mutation(s) found.";
          Log.v("MusicSyncAdapter", str17);
          continue;
          label990: bool1 = true;
        }
      }
    }
  }

  private void maybeUpdateSubscribedFeeds(Account paramAccount)
  {
    ContentResolver localContentResolver = getContext().getContentResolver();
    HashSet localHashSet = new HashSet();
    boolean bool1 = localHashSet.add("track-update");
    boolean bool2 = localHashSet.add("playlist-update");
    boolean bool3 = localHashSet.add("playlist-entry-update");
    if (Gservices.getBoolean(localContentResolver, "music_sync_radio", true))
      boolean bool4 = localHashSet.add("radio-station-update");
    if (Gservices.getBoolean(localContentResolver, "music_sync_config", true))
      boolean bool5 = localHashSet.add("config-update");
    HashMap localHashMap = Maps.newHashMap();
    String[] arrayOfString1 = new String[2];
    arrayOfString1[0] = "_id";
    arrayOfString1[1] = "feed";
    String[] arrayOfString2 = new String[3];
    String str1 = paramAccount.name;
    arrayOfString2[0] = str1;
    String str2 = paramAccount.type;
    arrayOfString2[1] = str2;
    arrayOfString2[2] = "com.google.android.music.MusicContent";
    Context localContext = getContext();
    Uri localUri1 = SubscribedFeeds.Feeds.CONTENT_URI;
    Cursor localCursor = MusicUtils.query(localContext, localUri1, arrayOfString1, "_sync_account=? AND _sync_account_type=? AND authority=?", arrayOfString2, null);
    if (localCursor == null)
    {
      Log.w("MusicSyncAdapter", "Can't find sync subscription feeds.");
      return;
    }
    try
    {
      if (localCursor.moveToNext())
      {
        long l1 = localCursor.getLong(0);
        String str3 = localCursor.getString(1);
        Long localLong = Long.valueOf(l1);
        Object localObject1 = localHashMap.put(str3, localLong);
      }
    }
    finally
    {
      localCursor.close();
    }
    Iterator localIterator1 = localHashSet.iterator();
    while (localIterator1.hasNext())
    {
      String str4 = (String)localIterator1.next();
      String str5 = str4;
      if (!localHashMap.containsKey(str5))
      {
        ContentValues localContentValues = new ContentValues();
        String str6 = paramAccount.name;
        localContentValues.put("_sync_account", str6);
        String str7 = paramAccount.type;
        localContentValues.put("_sync_account_type", str7);
        String str8 = str4;
        localContentValues.put("feed", str8);
        localContentValues.put("service", "sj");
        localContentValues.put("authority", "com.google.android.music.MusicContent");
        Uri localUri2 = SubscribedFeeds.Feeds.CONTENT_URI;
        Uri localUri3 = localContentResolver.insert(localUri2, localContentValues);
      }
      else
      {
        String str9 = str4;
        Object localObject3 = localHashMap.remove(str9);
      }
    }
    Iterator localIterator2 = localHashMap.entrySet().iterator();
    while (true)
    {
      if (!localIterator2.hasNext())
        return;
      long l2 = ((Long)((Map.Entry)localIterator2.next()).getValue()).longValue();
      Uri localUri4 = ContentUris.withAppendedId(SubscribedFeeds.Feeds.CONTENT_URI, l2);
      int i = localContentResolver.delete(localUri4, null, null);
    }
  }

  public static MusicSyncAdapterBuilder newBuilder()
  {
    return new MusicSyncAdapterBuilder();
  }

  private void processConfigResult(MusicPreferences paramMusicPreferences, MusicApiClient.GetResult<? extends MusicQueueableSyncEntity> paramGetResult)
  {
    boolean bool1 = ConfigUtils.isNautilusEnabled();
    boolean bool2 = paramMusicPreferences.isValidAccount();
    boolean bool3 = ConfigUtils.isNautilusPurchaseAvailable();
    boolean bool4 = ConfigUtils.isNautilusFreeTrialAvailable();
    boolean bool5 = paramMusicPreferences.getClearNautilusContent();
    boolean bool6 = paramMusicPreferences.getResetSyncState();
    ConfigItem localConfigItem1 = new ConfigItem();
    ConfigStore localConfigStore1 = ConfigStore.getInstance(this.mContext);
    SQLiteDatabase localSQLiteDatabase1 = localConfigStore1.beginWriteTxn();
    long l1 = 0L;
    SQLiteStatement localSQLiteStatement1 = ConfigItem.compileInsertStatement(localSQLiteDatabase1);
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this.mContext);
    while (true)
    {
      boolean bool7;
      boolean bool8;
      boolean bool9;
      boolean bool10;
      Object localObject2;
      try
      {
        int i = ConfigItem.deleteAllServerSettings(localSQLiteDatabase1);
        bool7 = bool4;
        bool8 = bool3;
        bool9 = bool2;
        bool10 = bool1;
        if (paramGetResult.mItems.hasNext())
        {
          Object localObject1 = (ConfigEntry)paramGetResult.mItems.next();
          if (this.LOGV)
          {
            Object[] arrayOfObject1 = new Object[2];
            String str1 = ((ConfigEntry)localObject1).getKey();
            arrayOfObject1[0] = str1;
            String str2 = ((ConfigEntry)localObject1).getValue();
            arrayOfObject1[1] = str2;
            String str3 = String.format("Config entry key=%s value=%s", arrayOfObject1);
            Log.v("MusicSyncAdapter", str3);
          }
          localConfigItem1.reset();
          String str4 = ((ConfigEntry)localObject1).getKey();
          localObject1 = ((ConfigEntry)localObject1).getValue();
          if (!TextUtils.isEmpty(str4))
          {
            ConfigItem localConfigItem2 = localConfigItem1;
            String str5 = str4;
            localConfigItem2.setName(str5);
            localConfigItem1.setValue((String)localObject1);
            ConfigItem localConfigItem3 = localConfigItem1;
            int j = 1;
            localConfigItem3.setType(j);
            ConfigItem localConfigItem4 = localConfigItem1;
            SQLiteStatement localSQLiteStatement2 = localSQLiteStatement1;
            long l3 = localConfigItem4.insert(localSQLiteStatement2);
            if (str4.equals("isNautilusUser"))
            {
              boolean bool11 = "true".equals(localObject1);
              long l4 = l1;
              bool12 = bool7;
              bool7 = bool8;
              bool8 = bool9;
              bool9 = bool11;
              l2 = l4;
              bool10 = bool9;
              bool9 = bool8;
              bool8 = bool7;
              bool7 = bool12;
              l1 = l2;
              continue;
            }
            if (str4.equals("nautilusExpirationTimeMs"))
            {
              l2 = Long.valueOf(l2).longValue();
              bool12 = bool7;
              bool7 = bool8;
              bool8 = bool9;
              bool9 = bool10;
              continue;
            }
            if (str4.equals("nextConfigSyncDelayInSeconds"))
            {
              long l5 = Long.valueOf(l2).longValue();
              MusicSyncAdapter localMusicSyncAdapter = this;
              long l6 = l5;
              localMusicSyncAdapter.scheduleAdditionalSync(l6);
              l2 = l1;
              bool12 = bool7;
              bool7 = bool8;
              bool8 = bool9;
              bool9 = bool10;
              continue;
            }
            if (str4.equals("isNautilusAvailable"))
            {
              boolean bool13 = "true".equals(l2);
              bool8 = bool9;
              bool9 = bool10;
              boolean bool14 = bool13;
              l2 = l1;
              bool12 = bool7;
              bool7 = bool14;
              continue;
            }
            if (str4.equals("isTrAvailable"))
            {
              boolean bool15 = "true".equals(l2);
              bool7 = bool8;
              bool8 = bool9;
              bool9 = bool10;
              boolean bool16 = bool15;
              l2 = l1;
              bool12 = bool16;
              continue;
            }
            if (!str4.equals("isAnyServiceAvailable"))
              continue;
            boolean bool17 = "true".equals(l2);
            bool9 = bool10;
            boolean bool18 = bool7;
            bool7 = bool8;
            bool8 = bool17;
            l2 = l1;
            bool12 = bool18;
            continue;
          }
          String str6 = "Empty config value";
          Log.w("MusicSyncAdapter", str6);
          long l2 = l1;
          boolean bool12 = bool7;
          bool7 = bool8;
          bool8 = bool9;
          bool9 = bool10;
          continue;
        }
        if ((!bool2) && (bool9))
        {
          paramMusicPreferences.setIsValidAccount(true);
          Log.i("MusicSyncAdapter", "Service became available to invalid account.");
          Object[] arrayOfObject2 = new Object[0];
          localMusicEventLogger.trackEvent("userBecameValid", arrayOfObject2);
          Account localAccount1 = paramMusicPreferences.getSyncAccount();
          Bundle localBundle = new Bundle();
          ContentResolver.requestSync(localAccount1, "com.google.android.music.MusicContent", localBundle);
          if ((bool1) && (!bool10))
          {
            Log.i("MusicSyncAdapter", "User lost Nautilus.");
            Object[] arrayOfObject3 = new Object[0];
            localMusicEventLogger.trackEvent("userLostNautilus", arrayOfObject3);
            paramMusicPreferences.setClearNautilusContent(true);
            bool5 = true;
            if ((!bool10) && (bool9) && ((ConfigUtils.hasIsNautilusAvailableBeenSet()) || (ConfigUtils.hasIsNautilusFreeTrialAvailableBeenSet())))
            {
              if ((!bool3) && (!bool4))
                break label1065;
              bool9 = true;
              if ((!bool8) && (!bool7))
                break label1071;
              localObject2 = null;
              if ((!bool9) && (localObject2 != null))
                paramMusicPreferences.setTutorialViewed(false);
            }
            if ((bool4) || (!bool7))
              break label1077;
            Log.i("MusicSyncAdapter", "Nautilus trial became available.");
            Object[] arrayOfObject4 = new Object[0];
            localMusicEventLogger.trackEvent("nautilusTrialBecameAvailable", arrayOfObject4);
            if ((bool3) || (!bool8))
              break label1114;
            Log.i("MusicSyncAdapter", "Nautilus purchase became available.");
            Object[] arrayOfObject5 = new Object[0];
            localMusicEventLogger.trackEvent("nautilusPurchaseBecameAvailable", arrayOfObject5);
            ConfigStore localConfigStore2 = localConfigStore1;
            SQLiteDatabase localSQLiteDatabase2 = localSQLiteDatabase1;
            localConfigStore2.endWriteTxn(localSQLiteDatabase2, true);
            Store.safeClose(localSQLiteStatement1);
            ContentResolver localContentResolver = this.mContext.getContentResolver();
            Uri localUri = ConfigContent.SERVER_SETTINGS_URI;
            localContentResolver.notifyChange(localUri, null, false);
            Store localStore = Store.getInstance(this.mContext);
            if ((bool5) && (localStore.deleteAllNautilusContent()))
              paramMusicPreferences.setClearNautilusContent(false);
            if (bool6)
            {
              Account localAccount2 = paramMusicPreferences.getSyncAccount();
              if (resetSync(localAccount2))
              {
                paramMusicPreferences.setResetSyncState(false);
                paramMusicPreferences.setShowSyncNotification(true);
              }
            }
            if (!bool10)
              return;
            paramMusicPreferences.updateNautilusTimestamp();
            scheduleNautilusExpirationSync(l1);
          }
        }
        else
        {
          if ((!bool2) || (bool9))
            continue;
          Log.i("MusicSyncAdapter", "Service became unavailable to valid account.");
          Object[] arrayOfObject6 = new Object[0];
          localMusicEventLogger.trackEvent("userBecameInvalid", arrayOfObject6);
          continue;
        }
      }
      finally
      {
        localConfigStore1.endWriteTxn(localSQLiteDatabase1, false);
        Store.safeClose(localSQLiteStatement1);
      }
      if ((!bool1) && (bool10))
      {
        Log.i("MusicSyncAdapter", "User gained Nautilus.");
        Object[] arrayOfObject7 = new Object[0];
        localMusicEventLogger.trackEvent("userGainedNautilus", arrayOfObject7);
        paramMusicPreferences.setResetSyncState(true);
        paramMusicPreferences.setShowSyncNotification(true);
        bool6 = true;
        continue;
        label1065: bool9 = false;
        continue;
        label1071: localObject2 = null;
        continue;
        label1077: if ((bool4) && (!bool7))
        {
          Log.i("MusicSyncAdapter", "Nautilus trial became unavailable.");
          Object[] arrayOfObject8 = new Object[0];
          localMusicEventLogger.trackEvent("nautilusTrialBecameUnavailable", arrayOfObject8);
          continue;
          label1114: if ((bool3) && (!bool8))
          {
            Log.i("MusicSyncAdapter", "Nautilus purchase became unavailable.");
            Object[] arrayOfObject9 = new Object[0];
            localMusicEventLogger.trackEvent("nautilusPurchaseBecameUnavailable", arrayOfObject9);
          }
        }
      }
    }
  }

  private void processConfigResult(MusicApiClient.GetResult<? extends MusicQueueableSyncEntity> paramGetResult)
  {
    Object localObject1 = new Object();
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this.mContext, localObject1);
    try
    {
      processConfigResult(localMusicPreferences, paramGetResult);
      return;
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(localObject1);
    }
  }

  private boolean resetSync(Account paramAccount)
  {
    boolean bool = Store.getInstance(this.mContext).resetSyncState();
    Bundle localBundle = new Bundle();
    ContentResolver.requestSync(paramAccount, "com.google.android.music.MusicContent", localBundle);
    return bool;
  }

  private void scheduleAdditionalSync(long paramLong)
  {
    if (paramLong <= 0L)
      return;
    if (paramLong > 172800L)
    {
      String str = "nextConfigSyncDelayInSeconds from server config response too large: " + paramLong;
      Log.e("MusicSyncAdapter", str);
      return;
    }
    AlarmManager localAlarmManager = (AlarmManager)this.mContext.getSystemService("alarm");
    Context localContext = this.mContext;
    Intent localIntent = new Intent("com.google.android.music.sync.SERVER_REQUEST_CONFIG_ALARM");
    PendingIntent localPendingIntent = PendingIntent.getBroadcast(localContext, 0, localIntent, 0);
    long l1 = System.currentTimeMillis();
    long l2 = 1000L * paramLong;
    long l3 = l1 + l2;
    localAlarmManager.set(1, l3, localPendingIntent);
  }

  private void scheduleNautilusExpirationSync(long paramLong)
  {
    if (paramLong <= 0L)
      return;
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    String str1 = MusicGservicesKeys.MUSIC_EXPIRATION_SYNC_ALARM_MINUTES;
    long l1 = Gservices.getLong(localContentResolver, str1, 60L) * 60L * 1000L;
    long l2 = paramLong + l1;
    long l3 = System.currentTimeMillis();
    if (l2 <= l3)
      return;
    AlarmManager localAlarmManager = (AlarmManager)this.mContext.getSystemService("alarm");
    Context localContext = this.mContext;
    String str2 = this.ALARM_NAUTILUS_EXPIRATION_ACTION;
    Intent localIntent = new Intent(str2);
    PendingIntent localPendingIntent = PendingIntent.getBroadcast(localContext, 0, localIntent, 0);
    localAlarmManager.set(1, l2, localPendingIntent);
  }

  private void sendNotificationIfNecessary(Account paramAccount, Map<String, Object> paramMap)
  {
    int i = ((AtomicInteger)paramMap.get("new_purchased_count")).get();
    if (i == 0)
      return;
    long l1 = ((Long)paramMap.get("new_purchased_albumId")).longValue();
    String str1 = (String)paramMap.get("new_purchased_album_name");
    String str2 = (String)paramMap.get("new_purchased_artist_name");
    String str3 = (String)paramMap.get("new_purchased_song_title");
    Object localObject;
    PendingIntent localPendingIntent;
    if (i == 1)
    {
      Context localContext1 = this.mContext;
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = str3;
      arrayOfObject1[1] = str2;
      localObject = localContext1.getString(2131231151, arrayOfObject1);
      localPendingIntent = createFreeAndPurchasedIntent();
    }
    while (true)
    {
      Notification localNotification = new Notification();
      long l2 = System.currentTimeMillis();
      localNotification.when = l2;
      localNotification.flags = 24;
      localNotification.icon = 2130837879;
      localNotification.defaults = 0;
      Context localContext2 = this.mContext;
      String str4 = this.mContext.getString(2131231154);
      localNotification.setLatestEventInfo(localContext2, (CharSequence)localObject, str4, localPendingIntent);
      ((NotificationManager)this.mContext.getSystemService("notification")).notify(25, localNotification);
      return;
      if (((Boolean)paramMap.get("new_purchased_same_album")).booleanValue())
      {
        Context localContext3 = this.mContext;
        Object[] arrayOfObject2 = new Object[2];
        arrayOfObject2[0] = str1;
        arrayOfObject2[1] = str2;
        String str5 = localContext3.getString(2131231152, arrayOfObject2);
        localPendingIntent = createAlbumIntent(l1, str1, str2);
        localObject = str5;
      }
      else
      {
        Context localContext4 = this.mContext;
        Object[] arrayOfObject3 = new Object[1];
        Integer localInteger = Integer.valueOf(i);
        arrayOfObject3[0] = localInteger;
        localObject = localContext4.getString(2131231153, arrayOfObject3);
        localPendingIntent = createFreeAndPurchasedIntent();
      }
    }
  }

  private void showSyncingNotification()
  {
    if (this.mSyncingNotification != null)
      return;
    NotificationManager localNotificationManager = (NotificationManager)this.mContext.getSystemService("notification");
    String str = this.mContext.getResources().getString(2131230982);
    Context localContext = this.mContext;
    NotificationCompat.Builder localBuilder = new NotificationCompat.Builder(localContext).setSmallIcon(2130837878);
    PendingIntent localPendingIntent = AppNavigation.getHomeScreenPendingIntent(this.mContext);
    Notification localNotification1 = localBuilder.setContentIntent(localPendingIntent).setContentTitle(str).setContentText("").setOngoing(true).setAutoCancel(false).setProgress(0, 0, true).setTicker(str).build();
    this.mSyncingNotification = localNotification1;
    Notification localNotification2 = this.mSyncingNotification;
    localNotificationManager.notify(101, localNotification2);
  }

  protected DownstreamMerger createDownstreamMerger(AbstractSyncAdapter.DownstreamMergeQueue paramDownstreamMergeQueue, Context paramContext, Map<String, Object> paramMap)
  {
    int i = Gservices.getInt(paramContext.getContentResolver(), "music_merge_block_size", 100);
    String str = this.mTag;
    AbstractSyncAdapter.DownstreamMergeQueue localDownstreamMergeQueue = paramDownstreamMergeQueue;
    Context localContext = paramContext;
    Map<String, Object> localMap = paramMap;
    return new MusicDownstreamMerger(localDownstreamMergeQueue, i, localContext, localMap, str);
  }

  protected DownstreamReader createDownstreamReader(AbstractSyncAdapter.DownstreamFetchQueue paramDownstreamFetchQueue, int paramInt, Context paramContext, Map<String, Object> paramMap)
  {
    String str = this.mTag;
    AbstractSyncAdapter.DownstreamFetchQueue localDownstreamFetchQueue = paramDownstreamFetchQueue;
    int i = paramInt;
    Context localContext = paramContext;
    Map<String, Object> localMap = paramMap;
    return new MusicDownstreamReader(localDownstreamFetchQueue, i, localContext, localMap, str);
  }

  protected UpstreamReader createUpstreamReader(AbstractSyncAdapter.UpstreamQueue paramUpstreamQueue, Context paramContext, Map<String, Object> paramMap)
  {
    boolean bool = MusicUtils.areUpstreamTrackDeletesEnabled(paramContext);
    if (!bool)
      Log.v("MusicSyncAdapter", "Upstream track deletions have been disabled.");
    String str = this.mTag;
    AbstractSyncAdapter.UpstreamQueue localUpstreamQueue = paramUpstreamQueue;
    Context localContext = paramContext;
    Map<String, Object> localMap = paramMap;
    return new MusicUpstreamReader(localUpstreamQueue, localContext, localMap, str, bool);
  }

  protected UpstreamSender createUpstreamSender(AbstractSyncAdapter.UpstreamQueue paramUpstreamQueue, Context paramContext, Map<String, Object> paramMap)
  {
    int i = Gservices.getInt(paramContext.getContentResolver(), "music_upstream_page_size", 250);
    boolean bool = Gservices.getBoolean(paramContext.getContentResolver(), "music_enable_track_stats_upsync", false);
    String str = this.mTag;
    MusicApiClient localMusicApiClient = this.mMusicApiClient;
    AbstractSyncAdapter.UpstreamQueue localUpstreamQueue = paramUpstreamQueue;
    Context localContext = paramContext;
    Map<String, Object> localMap = paramMap;
    return new MusicUpstreamSender(localUpstreamQueue, i, localContext, localMap, str, localMusicApiClient, bool);
  }

  protected boolean fetchDataFromServer(Account paramAccount, AbstractSyncAdapter.DownstreamFetchQueue paramDownstreamFetchQueue, HashMap<String, Object> paramHashMap)
    throws AuthenticatorException, HardSyncException, SoftSyncException
  {
    return getChangesFromServerAsDom(paramAccount, paramDownstreamFetchQueue, paramHashMap);
  }

  protected void getStatsString(StringBuffer paramStringBuffer, SyncResult paramSyncResult)
  {
    if (paramSyncResult.stats.numUpdates > 0L)
    {
      StringBuffer localStringBuffer1 = paramStringBuffer.append("u");
      long l1 = paramSyncResult.stats.numUpdates;
      StringBuffer localStringBuffer2 = localStringBuffer1.append(l1);
    }
    if (paramSyncResult.stats.numInserts > 0L)
    {
      StringBuffer localStringBuffer3 = paramStringBuffer.append("i");
      long l2 = paramSyncResult.stats.numInserts;
      StringBuffer localStringBuffer4 = localStringBuffer3.append(l2);
    }
    if (paramSyncResult.stats.numDeletes > 0L)
    {
      StringBuffer localStringBuffer5 = paramStringBuffer.append("d");
      long l3 = paramSyncResult.stats.numDeletes;
      StringBuffer localStringBuffer6 = localStringBuffer5.append(l3);
    }
    if (paramSyncResult.stats.numEntries > 0L)
    {
      StringBuffer localStringBuffer7 = paramStringBuffer.append("n");
      long l4 = paramSyncResult.stats.numEntries;
      StringBuffer localStringBuffer8 = localStringBuffer7.append(l4);
    }
    if (paramSyncResult.stats.numSkippedEntries > 0L)
    {
      StringBuffer localStringBuffer9 = paramStringBuffer.append("k");
      long l5 = paramSyncResult.stats.numSkippedEntries;
      StringBuffer localStringBuffer10 = localStringBuffer9.append(l5);
    }
    String str = paramSyncResult.toDebugString();
    StringBuffer localStringBuffer11 = paramStringBuffer.append(str);
  }

  protected void onDownstreamComplete(Account paramAccount, HashMap<String, Object> paramHashMap)
  {
    MediaStoreImporter.updateLocalMusicBasedOnRemoteContentAsync(this.mContext);
    RecentItemsManager.updateRecentItemsAsync(this.mContext);
    boolean bool = ((Boolean)paramHashMap.get("is_manual_sync")).booleanValue();
    Context localContext1 = this.mContext;
    if ((MusicPeriodicUpdater.checkAndEnablePeriodicUpdate(paramAccount, localContext1)) || (bool))
    {
      Context localContext2 = this.mContext;
      MusicPeriodicUpdater.performPeriodicUpdate(paramAccount, localContext2);
    }
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    Uri localUri = MusicContent.CONTENT_URI;
    localContentResolver.notifyChange(localUri, null, false);
  }

  protected void onDownstreamStart(Account paramAccount, HashMap<String, Object> paramHashMap)
  {
  }

  protected void onLogSyncDetails(long paramLong1, long paramLong2, SyncResult paramSyncResult)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    getStatsString(localStringBuffer, paramSyncResult);
    Object[] arrayOfObject = new Object[4];
    arrayOfObject[0] = "com.google.android.music";
    Long localLong1 = Long.valueOf(paramLong1);
    arrayOfObject[1] = localLong1;
    Long localLong2 = Long.valueOf(paramLong2);
    arrayOfObject[2] = localLong2;
    String str = localStringBuffer.toString();
    arrayOfObject[3] = str;
    int i = EventLog.writeEvent(203001, arrayOfObject);
  }

  protected void onSyncEnd(Account paramAccount, final Context paramContext, Map<String, Object> paramMap, boolean paramBoolean)
  {
    final boolean bool = true;
    int i = 0;
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this.mContext, this);
    while (true)
    {
      Account localAccount2;
      try
      {
        Account localAccount1 = localMusicPreferences.getSyncAccount();
        localAccount2 = localAccount1;
        MusicPreferences.releaseMusicPreferences(this);
        if (localAccount2 == null)
        {
          Log.w("MusicSyncAdapter", "Just synced account has been removed");
          MusicEventLogger localMusicEventLogger1 = MusicEventLogger.getInstance(this.mContext);
          Object[] arrayOfObject1 = new Object[2];
          arrayOfObject1[0] = "syncAccountMismatchDesc";
          arrayOfObject1[1] = "None configured.";
          localMusicEventLogger1.trackEvent("syncAccountMismatch", arrayOfObject1);
          i = 1;
          if (i != 0)
          {
            if (localAccount2 == null)
              bool = false;
            LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
            Runnable local1 = new Runnable()
            {
              public void run()
              {
                Store localStore = Store.getInstance(paramContext);
                Context localContext = paramContext;
                boolean bool1 = bool;
                boolean bool2 = localStore.deleteRemoteMusicAndPlaylists(localContext, bool1);
              }
            };
            AsyncWorkers.runAsync(localLoggableHandler, local1);
          }
          Intent localIntent = new Intent("com.google.android.music.SYNC_COMPLETE");
          paramContext.sendBroadcast(localIntent);
          if ((localMusicPreferences.getShowSyncNotification()) || (this.mSyncingNotification != null))
          {
            ((NotificationManager)paramContext.getSystemService("notification")).cancel(101);
            this.mSyncingNotification = null;
            localMusicPreferences.setShowSyncNotification(false);
          }
          if (i != 0)
            return;
          if (!paramBoolean)
            return;
          sendNotificationIfNecessary(paramAccount, paramMap);
          return;
        }
      }
      finally
      {
        MusicPreferences.releaseMusicPreferences(this);
      }
      if (!paramAccount.equals(localAccount2))
      {
        Log.w("MusicSyncAdapter", "Streaming account has changed");
        MusicEventLogger localMusicEventLogger2 = MusicEventLogger.getInstance(this.mContext);
        Object[] arrayOfObject2 = new Object[2];
        arrayOfObject2[0] = "syncAccountMismatchDesc";
        arrayOfObject2[1] = "Configured different from syncing.";
        localMusicEventLogger2.trackEvent("syncAccountMismatch", arrayOfObject2);
        i = 1;
      }
    }
  }

  // ERROR //
  protected void onSyncStart(Account paramAccount, Context paramContext, Map<String, Object> paramMap, Bundle paramBundle)
    throws HardSyncException, SoftSyncException
  {
    // Byte code:
    //   0: aload_2
    //   1: aload_0
    //   2: invokestatic 101	com/google/android/music/preferences/MusicPreferences:getMusicPreferences	(Landroid/content/Context;Ljava/lang/Object;)Lcom/google/android/music/preferences/MusicPreferences;
    //   5: astore 5
    //   7: aload_0
    //   8: aload_1
    //   9: invokespecial 1055	com/google/android/music/sync/google/MusicSyncAdapter:maybeUpdateSubscribedFeeds	(Landroid/accounts/Account;)V
    //   12: aload 5
    //   14: invokevirtual 618	com/google/android/music/preferences/MusicPreferences:getSyncAccount	()Landroid/accounts/Account;
    //   17: astore 6
    //   19: aload 5
    //   21: invokevirtual 499	com/google/android/music/preferences/MusicPreferences:isValidAccount	()Z
    //   24: ifne +78 -> 102
    //   27: iconst_1
    //   28: istore 7
    //   30: aload 5
    //   32: invokevirtual 1039	com/google/android/music/preferences/MusicPreferences:getShowSyncNotification	()Z
    //   35: ifeq +7 -> 42
    //   38: aload_0
    //   39: invokespecial 1057	com/google/android/music/sync/google/MusicSyncAdapter:showSyncingNotification	()V
    //   42: aload_0
    //   43: invokestatic 113	com/google/android/music/preferences/MusicPreferences:releaseMusicPreferences	(Ljava/lang/Object;)V
    //   46: aload 4
    //   48: ldc_w 1059
    //   51: iconst_1
    //   52: invokevirtual 1062	android/os/Bundle:getBoolean	(Ljava/lang/String;Z)Z
    //   55: ifeq +53 -> 108
    //   58: aload_1
    //   59: aload 6
    //   61: invokevirtual 1045	android/accounts/Account:equals	(Ljava/lang/Object;)Z
    //   64: ifne +44 -> 108
    //   67: aload_1
    //   68: ldc_w 396
    //   71: iconst_0
    //   72: invokestatic 1066	android/content/ContentResolver:setSyncAutomatically	(Landroid/accounts/Account;Ljava/lang/String;Z)V
    //   75: aload_1
    //   76: ldc_w 396
    //   79: iconst_0
    //   80: invokestatic 1070	android/content/ContentResolver:setIsSyncable	(Landroid/accounts/Account;Ljava/lang/String;I)V
    //   83: ldc 44
    //   85: ldc_w 1072
    //   88: invokestatic 415	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;)V
    //   91: new 119	com/google/android/music/sync/common/HardSyncException
    //   94: dup
    //   95: ldc_w 1074
    //   98: invokespecial 1075	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;)V
    //   101: athrow
    //   102: iconst_0
    //   103: istore 7
    //   105: goto -75 -> 30
    //   108: aload_0
    //   109: getfield 61	com/google/android/music/sync/google/MusicSyncAdapter:LOGV	Z
    //   112: ifeq +13 -> 125
    //   115: aload_0
    //   116: getfield 47	com/google/android/music/sync/google/MusicSyncAdapter:mTag	Ljava/lang/String;
    //   119: ldc_w 1077
    //   122: invokestatic 184	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   125: aload_0
    //   126: invokevirtual 196	com/google/android/music/sync/google/MusicSyncAdapter:getContext	()Landroid/content/Context;
    //   129: invokevirtual 1080	android/content/Context:getApplicationContext	()Landroid/content/Context;
    //   132: invokestatic 671	com/google/android/music/store/Store:getInstance	(Landroid/content/Context;)Lcom/google/android/music/store/Store;
    //   135: astore 8
    //   137: aload 8
    //   139: invokevirtual 1083	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   142: astore 9
    //   144: aload 9
    //   146: aload_1
    //   147: invokestatic 1088	com/google/android/music/sync/google/ClientSyncState$Helpers:get	(Landroid/database/sqlite/SQLiteDatabase;Landroid/accounts/Account;)Lcom/google/android/music/sync/google/ClientSyncState;
    //   150: astore 10
    //   152: aload 10
    //   154: astore 11
    //   156: aload 8
    //   158: aload 9
    //   160: invokevirtual 1092	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   163: aload 11
    //   165: ifnonnull +90 -> 255
    //   168: aload_0
    //   169: getfield 61	com/google/android/music/sync/google/MusicSyncAdapter:LOGV	Z
    //   172: ifeq +13 -> 185
    //   175: aload_0
    //   176: getfield 47	com/google/android/music/sync/google/MusicSyncAdapter:mTag	Ljava/lang/String;
    //   179: ldc_w 1094
    //   182: invokestatic 184	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   185: aload 8
    //   187: invokevirtual 1095	com/google/android/music/store/Store:beginWriteTxn	()Landroid/database/sqlite/SQLiteDatabase;
    //   190: astore 9
    //   192: invokestatic 1100	com/google/android/music/sync/google/ClientSyncState:newBuilder	()Lcom/google/android/music/sync/google/ClientSyncState$Builder;
    //   195: ldc2_w 525
    //   198: invokevirtual 1106	com/google/android/music/sync/google/ClientSyncState$Builder:setRemoteTrackVersion	(J)Lcom/google/android/music/sync/google/ClientSyncState$Builder;
    //   201: ldc2_w 525
    //   204: invokevirtual 1109	com/google/android/music/sync/google/ClientSyncState$Builder:setRemotePlaylistVersion	(J)Lcom/google/android/music/sync/google/ClientSyncState$Builder;
    //   207: ldc2_w 525
    //   210: invokevirtual 1112	com/google/android/music/sync/google/ClientSyncState$Builder:setRemotePlentryVersion	(J)Lcom/google/android/music/sync/google/ClientSyncState$Builder;
    //   213: ldc2_w 525
    //   216: invokevirtual 1115	com/google/android/music/sync/google/ClientSyncState$Builder:setRemoteRadioStationVersion	(J)Lcom/google/android/music/sync/google/ClientSyncState$Builder;
    //   219: astore 12
    //   221: aload_1
    //   222: invokestatic 1119	com/google/android/music/store/Store:computeAccountHash	(Landroid/accounts/Account;)I
    //   225: istore 13
    //   227: aload 12
    //   229: iload 13
    //   231: invokevirtual 1123	com/google/android/music/sync/google/ClientSyncState$Builder:setRemoteAccount	(I)Lcom/google/android/music/sync/google/ClientSyncState$Builder;
    //   234: invokevirtual 1126	com/google/android/music/sync/google/ClientSyncState$Builder:build	()Lcom/google/android/music/sync/google/ClientSyncState;
    //   237: astore 11
    //   239: aload 9
    //   241: aload_1
    //   242: aload 11
    //   244: invokestatic 1129	com/google/android/music/sync/google/ClientSyncState$Helpers:set	(Landroid/database/sqlite/SQLiteDatabase;Landroid/accounts/Account;Lcom/google/android/music/sync/google/ClientSyncState;)V
    //   247: aload 8
    //   249: aload 9
    //   251: iconst_1
    //   252: invokevirtual 1130	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   255: aload_0
    //   256: getfield 61	com/google/android/music/sync/google/MusicSyncAdapter:LOGV	Z
    //   259: ifeq +50 -> 309
    //   262: aload_0
    //   263: getfield 47	com/google/android/music/sync/google/MusicSyncAdapter:mTag	Ljava/lang/String;
    //   266: astore 14
    //   268: new 158	java/lang/StringBuilder
    //   271: dup
    //   272: invokespecial 159	java/lang/StringBuilder:<init>	()V
    //   275: ldc_w 1132
    //   278: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   281: astore 15
    //   283: aload 11
    //   285: invokevirtual 1133	com/google/android/music/sync/google/ClientSyncState:toString	()Ljava/lang/String;
    //   288: astore 16
    //   290: aload 15
    //   292: aload 16
    //   294: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   297: invokevirtual 178	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   300: astore 17
    //   302: aload 14
    //   304: aload 17
    //   306: invokestatic 184	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   309: aload 11
    //   311: getfield 1137	com/google/android/music/sync/google/ClientSyncState:mRemoteTrackVersion	Ljava/lang/Long;
    //   314: ifnonnull +475 -> 789
    //   317: ldc2_w 525
    //   320: lstore 18
    //   322: aload_0
    //   323: lload 18
    //   325: putfield 249	com/google/android/music/sync/google/MusicSyncAdapter:mInitialTrackVersion	J
    //   328: aload 11
    //   330: getfield 1140	com/google/android/music/sync/google/ClientSyncState:mRemotePlaylistVersion	Ljava/lang/Long;
    //   333: ifnonnull +469 -> 802
    //   336: ldc2_w 525
    //   339: lstore 20
    //   341: aload_0
    //   342: lload 20
    //   344: putfield 286	com/google/android/music/sync/google/MusicSyncAdapter:mInitialPlaylistVersion	J
    //   347: aload 11
    //   349: getfield 1143	com/google/android/music/sync/google/ClientSyncState:mRemotePlentryVersion	Ljava/lang/Long;
    //   352: ifnonnull +463 -> 815
    //   355: ldc2_w 525
    //   358: lstore 22
    //   360: aload_0
    //   361: lload 22
    //   363: putfield 296	com/google/android/music/sync/google/MusicSyncAdapter:mInitialPlaylistEntryVersion	J
    //   366: aload 11
    //   368: getfield 1146	com/google/android/music/sync/google/ClientSyncState:mRemoteRadioStationVersion	Ljava/lang/Long;
    //   371: ifnonnull +457 -> 828
    //   374: ldc2_w 525
    //   377: lstore 24
    //   379: aload_0
    //   380: lload 24
    //   382: putfield 302	com/google/android/music/sync/google/MusicSyncAdapter:mInitialRadioStationVersion	J
    //   385: aload_0
    //   386: getfield 249	com/google/android/music/sync/google/MusicSyncAdapter:mInitialTrackVersion	J
    //   389: invokestatic 434	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   392: astore 26
    //   394: aload_3
    //   395: ldc_w 1148
    //   398: aload 26
    //   400: invokeinterface 1149 3 0
    //   405: astore 27
    //   407: aload_0
    //   408: getfield 286	com/google/android/music/sync/google/MusicSyncAdapter:mInitialPlaylistVersion	J
    //   411: invokestatic 434	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   414: astore 28
    //   416: aload_3
    //   417: ldc_w 1151
    //   420: aload 28
    //   422: invokeinterface 1149 3 0
    //   427: astore 29
    //   429: aload_0
    //   430: getfield 296	com/google/android/music/sync/google/MusicSyncAdapter:mInitialPlaylistEntryVersion	J
    //   433: invokestatic 434	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   436: astore 30
    //   438: aload_3
    //   439: ldc_w 1153
    //   442: aload 30
    //   444: invokeinterface 1149 3 0
    //   449: astore 31
    //   451: aload_0
    //   452: getfield 302	com/google/android/music/sync/google/MusicSyncAdapter:mInitialRadioStationVersion	J
    //   455: invokestatic 434	java/lang/Long:valueOf	(J)Ljava/lang/Long;
    //   458: astore 32
    //   460: aload_3
    //   461: ldc_w 1155
    //   464: aload 32
    //   466: invokeinterface 1149 3 0
    //   471: astore 33
    //   473: aload 11
    //   475: getfield 1158	com/google/android/music/sync/google/ClientSyncState:mEtagTrack	Ljava/lang/String;
    //   478: ifnull +22 -> 500
    //   481: aload 11
    //   483: getfield 1158	com/google/android/music/sync/google/ClientSyncState:mEtagTrack	Ljava/lang/String;
    //   486: astore 34
    //   488: aload_3
    //   489: ldc 247
    //   491: aload 34
    //   493: invokeinterface 1149 3 0
    //   498: astore 35
    //   500: aload 11
    //   502: getfield 1161	com/google/android/music/sync/google/ClientSyncState:mEtagPlaylist	Ljava/lang/String;
    //   505: ifnull +23 -> 528
    //   508: aload 11
    //   510: getfield 1161	com/google/android/music/sync/google/ClientSyncState:mEtagPlaylist	Ljava/lang/String;
    //   513: astore 36
    //   515: aload_3
    //   516: ldc_w 284
    //   519: aload 36
    //   521: invokeinterface 1149 3 0
    //   526: astore 37
    //   528: aload 11
    //   530: getfield 1164	com/google/android/music/sync/google/ClientSyncState:mEtagPlaylistEntry	Ljava/lang/String;
    //   533: ifnull +23 -> 556
    //   536: aload 11
    //   538: getfield 1164	com/google/android/music/sync/google/ClientSyncState:mEtagPlaylistEntry	Ljava/lang/String;
    //   541: astore 38
    //   543: aload_3
    //   544: ldc_w 294
    //   547: aload 38
    //   549: invokeinterface 1149 3 0
    //   554: astore 39
    //   556: aload 11
    //   558: getfield 1168	com/google/android/music/sync/google/ClientSyncState:mRemoteAccount	Ljava/lang/Integer;
    //   561: astore 40
    //   563: aload_3
    //   564: ldc_w 1170
    //   567: aload 40
    //   569: invokeinterface 1149 3 0
    //   574: astore 41
    //   576: aload_3
    //   577: ldc_w 1172
    //   580: aload_1
    //   581: invokeinterface 1149 3 0
    //   586: astore 42
    //   588: aload 4
    //   590: ldc_w 386
    //   593: invokevirtual 1175	android/os/Bundle:containsKey	(Ljava/lang/String;)Z
    //   596: ifeq +615 -> 1211
    //   599: aload 4
    //   601: ldc_w 386
    //   604: invokevirtual 1178	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   607: astore 43
    //   609: ldc_w 354
    //   612: aload 43
    //   614: invokevirtual 579	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   617: ifeq +224 -> 841
    //   620: aload_0
    //   621: getfield 61	com/google/android/music/sync/google/MusicSyncAdapter:LOGV	Z
    //   624: ifeq +13 -> 637
    //   627: aload_0
    //   628: getfield 47	com/google/android/music/sync/google/MusicSyncAdapter:mTag	Ljava/lang/String;
    //   631: ldc_w 1180
    //   634: invokestatic 184	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   637: getstatic 1184	com/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed:TRACKS	Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;
    //   640: astore 44
    //   642: new 16	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState
    //   645: dup
    //   646: aload 44
    //   648: invokespecial 1187	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState:<init>	(Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;)V
    //   651: astore 45
    //   653: aload_3
    //   654: ldc 141
    //   656: aload 45
    //   658: invokeinterface 1149 3 0
    //   663: astore 46
    //   665: new 766	java/util/concurrent/atomic/AtomicInteger
    //   668: dup
    //   669: iconst_0
    //   670: invokespecial 1189	java/util/concurrent/atomic/AtomicInteger:<init>	(I)V
    //   673: astore 47
    //   675: aload_3
    //   676: ldc_w 761
    //   679: aload 47
    //   681: invokeinterface 1149 3 0
    //   686: astore 48
    //   688: iconst_1
    //   689: invokestatic 1192	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   692: astore 49
    //   694: aload_3
    //   695: ldc_w 815
    //   698: aload 49
    //   700: invokeinterface 1149 3 0
    //   705: astore 50
    //   707: aload 4
    //   709: ldc_w 1194
    //   712: iconst_0
    //   713: invokevirtual 1062	android/os/Bundle:getBoolean	(Ljava/lang/String;Z)Z
    //   716: invokestatic 1192	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   719: astore 51
    //   721: aload_3
    //   722: ldc 186
    //   724: aload 51
    //   726: invokeinterface 1149 3 0
    //   731: astore 52
    //   733: return
    //   734: astore 53
    //   736: new 121	com/google/android/music/sync/common/SoftSyncException
    //   739: dup
    //   740: ldc_w 1196
    //   743: aload 53
    //   745: invokespecial 1197	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   748: athrow
    //   749: astore 54
    //   751: aload 8
    //   753: aload 9
    //   755: invokevirtual 1092	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   758: aload 54
    //   760: athrow
    //   761: astore 55
    //   763: new 121	com/google/android/music/sync/common/SoftSyncException
    //   766: dup
    //   767: ldc_w 1199
    //   770: aload 55
    //   772: invokespecial 1197	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   775: athrow
    //   776: astore 56
    //   778: aload 8
    //   780: aload 9
    //   782: iconst_1
    //   783: invokevirtual 1130	com/google/android/music/store/Store:endWriteTxn	(Landroid/database/sqlite/SQLiteDatabase;Z)V
    //   786: aload 56
    //   788: athrow
    //   789: aload 11
    //   791: getfield 1137	com/google/android/music/sync/google/ClientSyncState:mRemoteTrackVersion	Ljava/lang/Long;
    //   794: invokevirtual 477	java/lang/Long:longValue	()J
    //   797: lstore 18
    //   799: goto -477 -> 322
    //   802: aload 11
    //   804: getfield 1140	com/google/android/music/sync/google/ClientSyncState:mRemotePlaylistVersion	Ljava/lang/Long;
    //   807: invokevirtual 477	java/lang/Long:longValue	()J
    //   810: lstore 20
    //   812: goto -471 -> 341
    //   815: aload 11
    //   817: getfield 1143	com/google/android/music/sync/google/ClientSyncState:mRemotePlentryVersion	Ljava/lang/Long;
    //   820: invokevirtual 477	java/lang/Long:longValue	()J
    //   823: lstore 22
    //   825: goto -465 -> 360
    //   828: aload 11
    //   830: getfield 1146	com/google/android/music/sync/google/ClientSyncState:mRemoteRadioStationVersion	Ljava/lang/Long;
    //   833: invokevirtual 477	java/lang/Long:longValue	()J
    //   836: lstore 24
    //   838: goto -459 -> 379
    //   841: ldc_w 362
    //   844: aload 43
    //   846: invokevirtual 579	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   849: ifeq +51 -> 900
    //   852: aload_0
    //   853: getfield 61	com/google/android/music/sync/google/MusicSyncAdapter:LOGV	Z
    //   856: ifeq +13 -> 869
    //   859: aload_0
    //   860: getfield 47	com/google/android/music/sync/google/MusicSyncAdapter:mTag	Ljava/lang/String;
    //   863: ldc_w 1201
    //   866: invokestatic 184	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   869: getstatic 1204	com/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed:PLAYLISTS	Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;
    //   872: astore 57
    //   874: new 16	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState
    //   877: dup
    //   878: aload 57
    //   880: invokespecial 1187	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState:<init>	(Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;)V
    //   883: astore 58
    //   885: aload_3
    //   886: ldc 141
    //   888: aload 58
    //   890: invokeinterface 1149 3 0
    //   895: astore 59
    //   897: goto -232 -> 665
    //   900: ldc_w 364
    //   903: aload 43
    //   905: invokevirtual 579	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   908: ifeq +51 -> 959
    //   911: aload_0
    //   912: getfield 61	com/google/android/music/sync/google/MusicSyncAdapter:LOGV	Z
    //   915: ifeq +13 -> 928
    //   918: aload_0
    //   919: getfield 47	com/google/android/music/sync/google/MusicSyncAdapter:mTag	Ljava/lang/String;
    //   922: ldc_w 1206
    //   925: invokestatic 184	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   928: getstatic 1209	com/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed:PLENTRIES	Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;
    //   931: astore 60
    //   933: new 16	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState
    //   936: dup
    //   937: aload 60
    //   939: invokespecial 1187	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState:<init>	(Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;)V
    //   942: astore 61
    //   944: aload_3
    //   945: ldc 141
    //   947: aload 61
    //   949: invokeinterface 1149 3 0
    //   954: astore 62
    //   956: goto -291 -> 665
    //   959: ldc_w 372
    //   962: aload 43
    //   964: invokevirtual 579	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   967: ifeq +51 -> 1018
    //   970: aload_0
    //   971: getfield 61	com/google/android/music/sync/google/MusicSyncAdapter:LOGV	Z
    //   974: ifeq +13 -> 987
    //   977: aload_0
    //   978: getfield 47	com/google/android/music/sync/google/MusicSyncAdapter:mTag	Ljava/lang/String;
    //   981: ldc_w 1211
    //   984: invokestatic 184	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   987: getstatic 1214	com/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed:RADIO_STATIONS	Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;
    //   990: astore 63
    //   992: new 16	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState
    //   995: dup
    //   996: aload 63
    //   998: invokespecial 1187	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState:<init>	(Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;)V
    //   1001: astore 64
    //   1003: aload_3
    //   1004: ldc 141
    //   1006: aload 64
    //   1008: invokeinterface 1149 3 0
    //   1013: astore 65
    //   1015: goto -350 -> 665
    //   1018: ldc_w 376
    //   1021: aload 43
    //   1023: invokevirtual 579	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   1026: ifeq +51 -> 1077
    //   1029: aload_0
    //   1030: getfield 61	com/google/android/music/sync/google/MusicSyncAdapter:LOGV	Z
    //   1033: ifeq +13 -> 1046
    //   1036: aload_0
    //   1037: getfield 47	com/google/android/music/sync/google/MusicSyncAdapter:mTag	Ljava/lang/String;
    //   1040: ldc_w 1216
    //   1043: invokestatic 184	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   1046: getstatic 1219	com/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed:CONFIG	Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;
    //   1049: astore 66
    //   1051: new 16	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState
    //   1054: dup
    //   1055: aload 66
    //   1057: invokespecial 1187	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState:<init>	(Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;)V
    //   1060: astore 67
    //   1062: aload_3
    //   1063: ldc 141
    //   1065: aload 67
    //   1067: invokeinterface 1149 3 0
    //   1072: astore 68
    //   1074: goto -409 -> 665
    //   1077: aload_0
    //   1078: getfield 61	com/google/android/music/sync/google/MusicSyncAdapter:LOGV	Z
    //   1081: ifeq +45 -> 1126
    //   1084: aload_0
    //   1085: getfield 47	com/google/android/music/sync/google/MusicSyncAdapter:mTag	Ljava/lang/String;
    //   1088: astore 69
    //   1090: new 158	java/lang/StringBuilder
    //   1093: dup
    //   1094: invokespecial 159	java/lang/StringBuilder:<init>	()V
    //   1097: ldc_w 1221
    //   1100: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1103: aload 43
    //   1105: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1108: ldc_w 1223
    //   1111: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1114: invokevirtual 178	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1117: astore 70
    //   1119: aload 69
    //   1121: aload 70
    //   1123: invokestatic 184	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   1126: iload 7
    //   1128: ifeq +51 -> 1179
    //   1131: aload_0
    //   1132: getfield 61	com/google/android/music/sync/google/MusicSyncAdapter:LOGV	Z
    //   1135: ifeq +13 -> 1148
    //   1138: aload_0
    //   1139: getfield 47	com/google/android/music/sync/google/MusicSyncAdapter:mTag	Ljava/lang/String;
    //   1142: ldc_w 1225
    //   1145: invokestatic 184	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   1148: getstatic 1219	com/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed:CONFIG	Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;
    //   1151: astore 71
    //   1153: new 16	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState
    //   1156: dup
    //   1157: aload 71
    //   1159: invokespecial 1187	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState:<init>	(Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;)V
    //   1162: astore 72
    //   1164: aload_3
    //   1165: ldc 141
    //   1167: aload 72
    //   1169: invokeinterface 1149 3 0
    //   1174: astore 73
    //   1176: goto -511 -> 665
    //   1179: aload_2
    //   1180: invokevirtual 202	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   1183: astore 74
    //   1185: new 13	com/google/android/music/sync/google/MusicSyncAdapter$AllFeedsDownstreamState
    //   1188: dup
    //   1189: aload 74
    //   1191: invokespecial 1228	com/google/android/music/sync/google/MusicSyncAdapter$AllFeedsDownstreamState:<init>	(Landroid/content/ContentResolver;)V
    //   1194: astore 75
    //   1196: aload_3
    //   1197: ldc 141
    //   1199: aload 75
    //   1201: invokeinterface 1149 3 0
    //   1206: astore 76
    //   1208: goto -543 -> 665
    //   1211: aload 4
    //   1213: ldc_w 1230
    //   1216: invokevirtual 1175	android/os/Bundle:containsKey	(Ljava/lang/String;)Z
    //   1219: ifeq +51 -> 1270
    //   1222: aload_0
    //   1223: getfield 61	com/google/android/music/sync/google/MusicSyncAdapter:LOGV	Z
    //   1226: ifeq +13 -> 1239
    //   1229: aload_0
    //   1230: getfield 47	com/google/android/music/sync/google/MusicSyncAdapter:mTag	Ljava/lang/String;
    //   1233: ldc_w 1232
    //   1236: invokestatic 184	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   1239: getstatic 1235	com/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed:CONFIG_ALARM	Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;
    //   1242: astore 77
    //   1244: new 16	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState
    //   1247: dup
    //   1248: aload 77
    //   1250: invokespecial 1187	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState:<init>	(Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;)V
    //   1253: astore 78
    //   1255: aload_3
    //   1256: ldc 141
    //   1258: aload 78
    //   1260: invokeinterface 1149 3 0
    //   1265: astore 79
    //   1267: goto -602 -> 665
    //   1270: aload_0
    //   1271: getfield 61	com/google/android/music/sync/google/MusicSyncAdapter:LOGV	Z
    //   1274: ifeq +13 -> 1287
    //   1277: aload_0
    //   1278: getfield 47	com/google/android/music/sync/google/MusicSyncAdapter:mTag	Ljava/lang/String;
    //   1281: ldc_w 1237
    //   1284: invokestatic 184	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   1287: iload 7
    //   1289: ifeq +51 -> 1340
    //   1292: aload_0
    //   1293: getfield 61	com/google/android/music/sync/google/MusicSyncAdapter:LOGV	Z
    //   1296: ifeq +13 -> 1309
    //   1299: aload_0
    //   1300: getfield 47	com/google/android/music/sync/google/MusicSyncAdapter:mTag	Ljava/lang/String;
    //   1303: ldc_w 1225
    //   1306: invokestatic 184	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   1309: getstatic 1219	com/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed:CONFIG	Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;
    //   1312: astore 80
    //   1314: new 16	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState
    //   1317: dup
    //   1318: aload 80
    //   1320: invokespecial 1187	com/google/android/music/sync/google/MusicSyncAdapter$TickledDownstreamState:<init>	(Lcom/google/android/music/sync/google/MusicSyncAdapter$DownstreamState$Feed;)V
    //   1323: astore 81
    //   1325: aload_3
    //   1326: ldc 141
    //   1328: aload 81
    //   1330: invokeinterface 1149 3 0
    //   1335: astore 82
    //   1337: goto -672 -> 665
    //   1340: aload_2
    //   1341: invokevirtual 202	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   1344: astore 83
    //   1346: new 13	com/google/android/music/sync/google/MusicSyncAdapter$AllFeedsDownstreamState
    //   1349: dup
    //   1350: aload 83
    //   1352: invokespecial 1228	com/google/android/music/sync/google/MusicSyncAdapter$AllFeedsDownstreamState:<init>	(Landroid/content/ContentResolver;)V
    //   1355: astore 84
    //   1357: aload_3
    //   1358: ldc 141
    //   1360: aload 84
    //   1362: invokeinterface 1149 3 0
    //   1367: astore 85
    //   1369: goto -704 -> 665
    //
    // Exception table:
    //   from	to	target	type
    //   144	152	734	com/google/android/music/sync/common/ProviderException
    //   144	152	749	finally
    //   736	749	749	finally
    //   185	247	761	com/google/android/music/sync/common/ProviderException
    //   185	247	776	finally
    //   763	776	776	finally
  }

  public static class MusicSyncAdapterBuilder extends AbstractSyncAdapter.Builder<MusicSyncAdapterBuilder, MusicSyncAdapter>
  {
    private MusicApiClient mMusicApiClient = null;

    public MusicSyncAdapter build(Context paramContext)
    {
      MusicSyncAdapter localMusicSyncAdapter = (MusicSyncAdapter)super.build(paramContext);
      MusicApiClient localMusicApiClient1 = this.mMusicApiClient;
      MusicApiClient localMusicApiClient2 = MusicSyncAdapter.access$102(localMusicSyncAdapter, localMusicApiClient1);
      return localMusicSyncAdapter;
    }

    protected MusicSyncAdapter buildEmpty(Context paramContext)
    {
      return new MusicSyncAdapter(paramContext);
    }

    public MusicSyncAdapterBuilder setMusicApiClient(MusicApiClient paramMusicApiClient)
    {
      this.mMusicApiClient = paramMusicApiClient;
      return this;
    }
  }

  private static class AllFeedsDownstreamState extends MusicSyncAdapter.DownstreamState
  {
    public AllFeedsDownstreamState(ContentResolver paramContentResolver)
    {
      super();
      if (Gservices.getBoolean(paramContentResolver, "music_sync_config", true))
      {
        MusicSyncAdapter.DownstreamState.Feed localFeed1 = MusicSyncAdapter.DownstreamState.Feed.CONFIG;
        this.mFeed = localFeed1;
        return;
      }
      MusicSyncAdapter.DownstreamState.Feed localFeed2 = MusicSyncAdapter.DownstreamState.Feed.TRACKS;
      this.mFeed = localFeed2;
    }

    public void onDoneWithFeed()
    {
      int[] arrayOfInt = MusicSyncAdapter.2.$SwitchMap$com$google$android$music$sync$google$MusicSyncAdapter$DownstreamState$Feed;
      int i = this.mFeed.ordinal();
      switch (arrayOfInt[i])
      {
      default:
        return;
      case 5:
        MusicSyncAdapter.DownstreamState.Feed localFeed1 = MusicSyncAdapter.DownstreamState.Feed.TRACKS;
        this.mFeed = localFeed1;
        return;
      case 1:
        MusicSyncAdapter.DownstreamState.Feed localFeed2 = MusicSyncAdapter.DownstreamState.Feed.PLAYLISTS;
        this.mFeed = localFeed2;
        return;
      case 2:
        MusicSyncAdapter.DownstreamState.Feed localFeed3 = MusicSyncAdapter.DownstreamState.Feed.PLENTRIES;
        this.mFeed = localFeed3;
        return;
      case 3:
        MusicSyncAdapter.DownstreamState.Feed localFeed4 = MusicSyncAdapter.DownstreamState.Feed.RADIO_STATIONS;
        this.mFeed = localFeed4;
        return;
      case 4:
      }
      this.mFeed = null;
    }
  }

  private static class TickledDownstreamState extends MusicSyncAdapter.DownstreamState
  {
    public TickledDownstreamState(MusicSyncAdapter.DownstreamState.Feed paramFeed)
    {
      super();
      int[] arrayOfInt = MusicSyncAdapter.2.$SwitchMap$com$google$android$music$sync$google$MusicSyncAdapter$DownstreamState$Feed;
      int i = paramFeed.ordinal();
      switch (arrayOfInt[i])
      {
      default:
        return;
      case 1:
        MusicSyncAdapter.DownstreamState.Feed localFeed1 = MusicSyncAdapter.DownstreamState.Feed.TRACKS;
        this.mFeed = localFeed1;
        return;
      case 2:
      case 3:
        MusicSyncAdapter.DownstreamState.Feed localFeed2 = MusicSyncAdapter.DownstreamState.Feed.PLAYLISTS;
        this.mFeed = localFeed2;
        return;
      case 4:
        MusicSyncAdapter.DownstreamState.Feed localFeed3 = MusicSyncAdapter.DownstreamState.Feed.RADIO_STATIONS;
        this.mFeed = localFeed3;
        return;
      case 5:
        MusicSyncAdapter.DownstreamState.Feed localFeed4 = MusicSyncAdapter.DownstreamState.Feed.CONFIG;
        this.mFeed = localFeed4;
      case 6:
      }
      MusicSyncAdapter.DownstreamState.Feed localFeed5 = MusicSyncAdapter.DownstreamState.Feed.CONFIG_ALARM;
      this.mFeed = localFeed5;
    }

    public void onDoneWithFeed()
    {
      MusicSyncAdapter.DownstreamState.Feed localFeed1 = this.mFeed;
      MusicSyncAdapter.DownstreamState.Feed localFeed2 = MusicSyncAdapter.DownstreamState.Feed.PLAYLISTS;
      if (localFeed1 == localFeed2)
      {
        MusicSyncAdapter.DownstreamState.Feed localFeed3 = MusicSyncAdapter.DownstreamState.Feed.PLENTRIES;
        this.mFeed = localFeed3;
        return;
      }
      this.mFeed = null;
    }
  }

  private static abstract class DownstreamState
  {
    protected Feed mFeed;

    public Feed getNextFeedToSync()
    {
      return this.mFeed;
    }

    public abstract void onDoneWithFeed();

    public static enum Feed
    {
      static
      {
        PLAYLISTS = new Feed("PLAYLISTS", 1);
        PLENTRIES = new Feed("PLENTRIES", 2);
        RADIO_STATIONS = new Feed("RADIO_STATIONS", 3);
        CONFIG = new Feed("CONFIG", 4);
        CONFIG_ALARM = new Feed("CONFIG_ALARM", 5);
        Feed[] arrayOfFeed = new Feed[6];
        Feed localFeed1 = TRACKS;
        arrayOfFeed[0] = localFeed1;
        Feed localFeed2 = PLAYLISTS;
        arrayOfFeed[1] = localFeed2;
        Feed localFeed3 = PLENTRIES;
        arrayOfFeed[2] = localFeed3;
        Feed localFeed4 = RADIO_STATIONS;
        arrayOfFeed[3] = localFeed4;
        Feed localFeed5 = CONFIG;
        arrayOfFeed[4] = localFeed5;
        Feed localFeed6 = CONFIG_ALARM;
        arrayOfFeed[5] = localFeed6;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.MusicSyncAdapter
 * JD-Core Version:    0.6.2
 */