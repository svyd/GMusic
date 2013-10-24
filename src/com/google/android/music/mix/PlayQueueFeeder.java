package com.google.android.music.mix;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.google.android.gsf.Gservices;
import com.google.android.music.cloudclient.MixTrackId;
import com.google.android.music.cloudclient.MixTrackId.Type;
import com.google.android.music.cloudclient.MusicCloud;
import com.google.android.music.cloudclient.MusicCloudImpl;
import com.google.android.music.cloudclient.RadioEditStationsResponse;
import com.google.android.music.cloudclient.RadioEditStationsResponse.MutateResponse;
import com.google.android.music.cloudclient.RadioFeedResponse;
import com.google.android.music.cloudclient.RadioFeedResponse.RadioData;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.medialist.TracksSongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.RadioStation;
import com.google.android.music.store.RecentItemsManager;
import com.google.android.music.store.Store;
import com.google.android.music.sync.google.model.RadioSeed;
import com.google.android.music.sync.google.model.SyncableRadioStation;
import com.google.android.music.sync.google.model.Track;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncWorkers;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class PlayQueueFeeder extends LoggableHandler
{
  private final boolean LOGV;
  private final MusicCloud mCloudClient;
  private final Context mContext;
  private final PlayQueueFeederListener mListener;
  private final MusicPreferences mMusicPreferences;
  private final MusicEventLogger mTracker;

  public PlayQueueFeeder(Context paramContext, PlayQueueFeederListener paramPlayQueueFeederListener)
  {
    super("PlayQueueFeeder");
    boolean bool = DebugUtils.isLoggable(DebugUtils.MusicTag.PLAYBACK_SERVICE);
    this.LOGV = bool;
    if (paramContext == null)
      throw new IllegalArgumentException("Missing context");
    if (paramPlayQueueFeederListener == null)
      throw new IllegalArgumentException("Missing listener");
    this.mContext = paramContext;
    MusicCloudImpl localMusicCloudImpl = new MusicCloudImpl(paramContext);
    this.mCloudClient = localMusicCloudImpl;
    this.mListener = paramPlayQueueFeederListener;
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(paramContext);
    this.mTracker = localMusicEventLogger;
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this.mContext, this);
    this.mMusicPreferences = localMusicPreferences;
  }

  private void addRadioToRecentAsync(final long paramLong)
  {
    final Context localContext = this.mContext;
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        Context localContext = localContext;
        long l = paramLong;
        boolean bool = RecentItemsManager.addRecentlyPlayedRadio(localContext, l);
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local1);
  }

  private Pair<Long, List<Track>> createRadioStation(String paramString1, String paramString2, String paramString3, int paramInt1, boolean paramBoolean, int paramInt2)
  {
    int i = this.mMusicPreferences.getContentFilter();
    Account localAccount = this.mMusicPreferences.getStreamingAccount();
    Object localObject;
    if (localAccount == null)
    {
      int j = Log.e("PlayQueueFeeder", "Failed to get account");
      localObject = null;
    }
    while (true)
    {
      return localObject;
      SyncableRadioStation localSyncableRadioStation;
      RadioEditStationsResponse localRadioEditStationsResponse;
      try
      {
        localSyncableRadioStation = new SyncableRadioStation();
        String str1 = paramString1;
        localSyncableRadioStation.mName = str1;
        String str2 = Store.generateClientId();
        localSyncableRadioStation.mClientId = str2;
        long l1 = System.currentTimeMillis() * 1000L;
        localSyncableRadioStation.mRecentTimestampMicrosec = l1;
        arrayOfString = MusicUtils.decodeStringArray(paramString2);
        if (arrayOfString == null);
        String str3;
        for (localSyncableRadioStation.mImageUrl = null; ; localSyncableRadioStation.mImageUrl = str3)
        {
          localSyncableRadioStation.mImageType = 1;
          RadioSeed localRadioSeed = RadioSeed.createRadioSeed(paramString3, paramInt1);
          localSyncableRadioStation.mSeed = localRadioSeed;
          MusicCloud localMusicCloud = this.mCloudClient;
          boolean bool = paramBoolean;
          int k = paramInt2;
          localRadioEditStationsResponse = localMusicCloud.createRadioStation(localSyncableRadioStation, bool, k, i);
          if (RadioEditStationsResponse.validateResponse(localRadioEditStationsResponse))
            break label237;
          localObject = null;
          break;
          if (arrayOfString.length != 1)
            break label210;
          str3 = arrayOfString[0];
        }
      }
      catch (IOException localIOException)
      {
        while (true)
        {
          String[] arrayOfString;
          int m = Log.e("PlayQueueFeeder", "Failed to create radio station: ", localIOException);
          localObject = null;
          break;
          localSyncableRadioStation.setImageUrls(arrayOfString);
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        label210: int n = Log.w("PlayQueueFeeder", "Interrupted while creating radio station");
        localObject = null;
      }
      continue;
      label237: String str4 = ((RadioEditStationsResponse.MutateResponse)localRadioEditStationsResponse.mMutateResponses.get(0)).mId;
      List localList = ((RadioEditStationsResponse.MutateResponse)localRadioEditStationsResponse.mMutateResponses.get(0)).mRadioStation.mTracks;
      RadioStation localRadioStation = localSyncableRadioStation.formatAsRadioStation(null);
      localRadioStation.setSourceId(str4);
      int i1 = Store.computeAccountHash(localAccount);
      localRadioStation.setSourceAccount(i1);
      localRadioStation.setNeedsSync(false);
      Store localStore = Store.getInstance(this.mContext);
      long l2 = localStore.saveRadioStation(localRadioStation);
      if (l2 == 65535L)
      {
        String str5 = paramString3;
        int i2 = paramInt1;
        l2 = localStore.getRadioLocalIdBySeedAndType(str5, i2);
      }
      if (l2 == 65535L)
      {
        localObject = null;
      }
      else
      {
        Long localLong = Long.valueOf(l2);
        localObject = new Pair(localLong, localList);
      }
    }
  }

  private static int getMaxMixSize(ContentResolver paramContentResolver)
  {
    return Gservices.getInt(paramContentResolver, "music_suggested_mix_size", 25);
  }

  private void handleCreateRadioStation(MixDescriptor paramMixDescriptor, PostProcessingAction paramPostProcessingAction)
  {
    if (this.LOGV)
    {
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramMixDescriptor;
      arrayOfObject[1] = paramPostProcessingAction;
      String str = String.format("handleCreateRadioStation: mix=%s action=%s", arrayOfObject);
      int i = Log.d("PlayQueueFeeder", str);
    }
    Store localStore = Store.getInstance(this.mContext);
    Pair localPair = transitionSeedToRadioStationIfNeeded(localStore, paramMixDescriptor, false, 0);
    if (localPair != null)
    {
      MixDescriptor localMixDescriptor = (MixDescriptor)localPair.first;
      long l = paramMixDescriptor.getLocalRadioId();
      if (localMixDescriptor != null)
        l = localMixDescriptor.getLocalRadioId();
      addRadioToRecentAsync(l);
      this.mListener.onSuccess(paramMixDescriptor, localMixDescriptor, null, paramPostProcessingAction);
      return;
    }
    this.mListener.onFailure(paramMixDescriptor, paramPostProcessingAction);
  }

  private void handleMoreContent(MixDescriptor paramMixDescriptor, List<ContentIdentifier> paramList, PostProcessingAction paramPostProcessingAction)
  {
    if (this.LOGV)
    {
      Object[] arrayOfObject1 = new Object[3];
      arrayOfObject1[0] = paramMixDescriptor;
      String str1 = DebugUtils.listToString(paramList);
      arrayOfObject1[1] = str1;
      arrayOfObject1[2] = paramPostProcessingAction;
      String str2 = String.format("handleMoreContent: mix=%s recentlyPlayed=%s action=%s", arrayOfObject1);
      int i = Log.d("PlayQueueFeeder", str2);
    }
    Object localObject = null;
    MixDescriptor localMixDescriptor1 = null;
    try
    {
      localStore = Store.getInstance(this.mContext);
      j = getMaxMixSize(this.mContext.getContentResolver());
      List<ContentIdentifier> localList = paramList;
      localList1 = localStore.getServerTrackIds(localList);
      if (this.LOGV)
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("Track ids: ");
        String str3 = TextUtils.join(",", localList1);
        String str4 = str3;
        int k = Log.d("PlayQueueFeeder", str4);
      }
      PlayQueueFeeder localPlayQueueFeeder = this;
      MixDescriptor localMixDescriptor2 = paramMixDescriptor;
      Pair localPair = localPlayQueueFeeder.transitionSeedToRadioStationIfNeeded(localStore, localMixDescriptor2, true, j);
      if (localPair != null)
      {
        localMixDescriptor1 = (MixDescriptor)localPair.first;
        List localList2 = (List)localPair.second;
        l = paramMixDescriptor.getLocalRadioId();
        if (localMixDescriptor1 != null)
          l = localMixDescriptor1.getLocalRadioId();
        addRadioToRecentAsync(l);
        if (localList2 == null)
        {
          m = this.mMusicPreferences.getContentFilter();
          MixDescriptor.Type localType1 = paramMixDescriptor.getType();
          MixDescriptor.Type localType2 = MixDescriptor.Type.LUCKY_RADIO;
          if (localType1 != localType2)
            break label544;
          localRadioFeedResponse = this.mCloudClient.getLuckyRadioFeed(j, localList1, m);
          if (this.LOGV)
          {
            if ((localRadioFeedResponse.mRadioData == null) || (localRadioFeedResponse.mRadioData.mRadioStations == null))
              break label575;
            StringBuilder localStringBuilder2 = new StringBuilder().append("Radio stations result size: ");
            int n = localRadioFeedResponse.mRadioData.mRadioStations.size();
            String str5 = n;
            int i1 = Log.d("PlayQueueFeeder", str5);
          }
          if ((localRadioFeedResponse.mRadioData != null) && (localRadioFeedResponse.mRadioData.mRadioStations != null) && (localRadioFeedResponse.mRadioData.mRadioStations.size() != 0))
            localList2 = ((SyncableRadioStation)localRadioFeedResponse.mRadioData.mRadioStations.get(0)).mTracks;
        }
        if (localList2 == null)
          break label603;
        int i2 = localList2.size();
        int i3 = 1;
        if (i2 <= i3)
          break label603;
        TracksSongList localTracksSongList = TracksSongList.createList(localList2);
        localObject = localTracksSongList;
        if (localObject != null)
          break label666;
        int i4 = Log.e("PlayQueueFeeder", "Failed to create song list");
        PlayQueueFeederListener localPlayQueueFeederListener1 = this.mListener;
        MixDescriptor localMixDescriptor3 = paramMixDescriptor;
        PostProcessingAction localPostProcessingAction1 = paramPostProcessingAction;
        localPlayQueueFeederListener1.onFailure(localMixDescriptor3, localPostProcessingAction1);
      }
      else
      {
        PlayQueueFeederListener localPlayQueueFeederListener2 = this.mListener;
        MixDescriptor localMixDescriptor4 = paramMixDescriptor;
        PostProcessingAction localPostProcessingAction2 = paramPostProcessingAction;
        localPlayQueueFeederListener2.onFailure(localMixDescriptor4, localPostProcessingAction2);
        return;
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        Store localStore;
        int j;
        List localList1;
        long l;
        int m;
        StringBuilder localStringBuilder3 = new StringBuilder().append("Failed to get mix entries:");
        String str6 = localIOException.getMessage();
        String str7 = str6;
        int i5 = Log.e("PlayQueueFeeder", str7);
        continue;
        String str8 = localStore.getRadioRemoteId(l);
        RadioFeedResponse localRadioFeedResponse = this.mCloudClient.getRadioFeed(str8, j, localList1, m);
        continue;
        int i6 = Log.d("PlayQueueFeeder", "Radio stations is null");
      }
    }
    catch (InterruptedException localInterruptedException)
    {
      while (true)
      {
        label544: label575: int i7 = Log.w("PlayQueueFeeder", "Interrupted while getting mix entries");
        continue;
        label603: int i8 = Log.e("PlayQueueFeeder", "Radio station is empty or too small.");
        MusicEventLogger localMusicEventLogger = this.mTracker;
        Object[] arrayOfObject2 = new Object[4];
        arrayOfObject2[0] = "actionArea";
        arrayOfObject2[1] = "createInstantMix";
        arrayOfObject2[2] = "failureReason";
        arrayOfObject2[3] = "musicIds.length <= 0";
        localMusicEventLogger.trackEvent("failure", arrayOfObject2);
      }
      label666: PlayQueueFeederListener localPlayQueueFeederListener3 = this.mListener;
      MixDescriptor localMixDescriptor5 = paramMixDescriptor;
      PostProcessingAction localPostProcessingAction3 = paramPostProcessingAction;
      localPlayQueueFeederListener3.onSuccess(localMixDescriptor5, localMixDescriptor1, localObject, localPostProcessingAction3);
    }
  }

  private Pair<MixDescriptor, List<Track>> transitionSeedToRadioStationIfNeeded(Store paramStore, MixDescriptor paramMixDescriptor, boolean paramBoolean, int paramInt)
  {
    List localList1 = null;
    long l2;
    Object localObject2;
    label336: label345: MixDescriptor.Type localType;
    String str6;
    String str7;
    if (paramMixDescriptor.getType().isSeed())
    {
      if ((!paramMixDescriptor.hasLocalSeedId()) && (!paramMixDescriptor.hasRemoteSeedId()))
        throw new IllegalArgumentException("Seed has neither local nor remote id");
      int[] arrayOfInt = 2.$SwitchMap$com$google$android$music$mix$MixDescriptor$Type;
      int i = paramMixDescriptor.getType().ordinal();
      switch (arrayOfInt[i])
      {
      default:
        throw new IllegalStateException("Unhandled seed type");
      case 1:
        if (!paramMixDescriptor.hasLocalSeedId())
          break;
      case 2:
      case 3:
      case 4:
      }
      while (true)
      {
        try
        {
          long l1 = paramMixDescriptor.getLocalSeedId();
          MixTrackId localMixTrackId1 = paramStore.getServerTrackId(l1);
          MixTrackId localMixTrackId2 = localMixTrackId1;
          str1 = localMixTrackId2.getRemoteId();
          MixTrackId.Type localType1 = localMixTrackId2.getType();
          MixTrackId.Type localType2 = MixTrackId.Type.LOCKER;
          if (localType1 == localType2)
          {
            j = 1;
            l2 = paramStore.getRadioLocalIdBySeedAndType(str1, j);
            if (l2 == 65535L)
            {
              if (!paramBoolean)
                break label345;
              String str2 = paramMixDescriptor.getName();
              String str3 = paramMixDescriptor.getArtLocation();
              PlayQueueFeeder localPlayQueueFeeder = this;
              int k = paramInt;
              localObject1 = localPlayQueueFeeder.createRadioStation(str2, str3, str1, j, true, k);
              if (localObject1 == null)
                break label336;
              l2 = ((Long)((Pair)localObject1).first).longValue();
              localList1 = (List)((Pair)localObject1).second;
            }
            if (l2 != 65535L)
              break;
            localObject2 = null;
            localObject1 = 0;
            return localObject2;
          }
        }
        catch (FileNotFoundException localFileNotFoundException)
        {
          int m = Log.e("PlayQueueFeeder", "Failed to get remote track id", localFileNotFoundException);
          localObject2 = null;
          int ? = 0;
          continue;
          j = 2;
          continue;
        }
        String str1 = paramMixDescriptor.getRemoteSeedId();
        int j = 2;
        continue;
        j = 3;
        str1 = paramMixDescriptor.getRemoteSeedId();
        continue;
        j = 4;
        str1 = paramMixDescriptor.getRemoteSeedId();
        continue;
        j = 5;
        str1 = paramMixDescriptor.getRemoteSeedId();
        continue;
        localObject2 = null;
        int ? = 0;
        continue;
        String str4 = paramMixDescriptor.getName();
        String str5 = paramMixDescriptor.getArtLocation();
        Object localObject3 = this;
        int n = paramInt;
        Object localObject1 = ((PlayQueueFeeder)localObject3).createRadioStation(str4, str5, str1, j, false, n);
        if (localObject1 == null)
        {
          localObject2 = null;
          localObject3 = 0;
        }
        else
        {
          l2 = ((Long)((Pair)localObject1).first).longValue();
        }
      }
      localType = MixDescriptor.Type.RADIO;
      str6 = paramMixDescriptor.getName();
      str7 = paramMixDescriptor.getArtLocation();
    }
    for (MixDescriptor localMixDescriptor = new MixDescriptor(l2, localType, str6, str7); ; localMixDescriptor = null)
    {
      List localList2 = localList1;
      localObject2 = new Pair(localMixDescriptor, localList2);
      break;
    }
  }

  public void cancelMix()
  {
  }

  public void handleMessage(Message paramMessage)
  {
    if (paramMessage.obj == null);
    for (MixRequestData localMixRequestData = null; localMixRequestData == null; localMixRequestData = (MixRequestData)paramMessage.obj)
    {
      int i = Log.e("PlayQueueFeeder", "Failed to get the mix request");
      return;
    }
    switch (paramMessage.what)
    {
    default:
      return;
    case 1:
      MixDescriptor localMixDescriptor1 = localMixRequestData.getMixDescriptor();
      List localList = localMixRequestData.getRecentlyPlayed();
      PostProcessingAction localPostProcessingAction1 = localMixRequestData.getPostProcessingAction();
      handleMoreContent(localMixDescriptor1, localList, localPostProcessingAction1);
      return;
    case 2:
    }
    MixDescriptor localMixDescriptor2 = localMixRequestData.getMixDescriptor();
    PostProcessingAction localPostProcessingAction2 = localMixRequestData.getPostProcessingAction();
    handleCreateRadioStation(localMixDescriptor2, localPostProcessingAction2);
  }

  public void onDestroy()
  {
    quit();
    MusicPreferences.releaseMusicPreferences(this);
  }

  public void requestContent(MixDescriptor paramMixDescriptor, List<ContentIdentifier> paramList, PostProcessingAction paramPostProcessingAction)
  {
    MixRequestData localMixRequestData = new MixRequestData(paramMixDescriptor, paramList, paramPostProcessingAction);
    Message localMessage = obtainMessage(1);
    localMessage.obj = localMixRequestData;
    boolean bool = sendMessage(localMessage);
  }

  public void requestCreateRadioStation(MixDescriptor paramMixDescriptor, PostProcessingAction paramPostProcessingAction)
  {
    MixRequestData localMixRequestData = new MixRequestData(paramMixDescriptor, null, paramPostProcessingAction);
    Message localMessage = obtainMessage(2);
    localMessage.obj = localMixRequestData;
    boolean bool = sendMessage(localMessage);
  }

  private static class MixRequestData
  {
    private final PlayQueueFeeder.PostProcessingAction mAction;
    private final MixDescriptor mMix;
    private final List<ContentIdentifier> mRecentlyPlayed;

    public MixRequestData(MixDescriptor paramMixDescriptor, List<ContentIdentifier> paramList, PlayQueueFeeder.PostProcessingAction paramPostProcessingAction)
    {
      this.mMix = paramMixDescriptor;
      this.mRecentlyPlayed = paramList;
      this.mAction = paramPostProcessingAction;
    }

    public MixDescriptor getMixDescriptor()
    {
      return this.mMix;
    }

    public PlayQueueFeeder.PostProcessingAction getPostProcessingAction()
    {
      return this.mAction;
    }

    public List<ContentIdentifier> getRecentlyPlayed()
    {
      return this.mRecentlyPlayed;
    }
  }

  public static enum PostProcessingAction
  {
    static
    {
      NEXT = new PostProcessingAction("NEXT", 1);
      FEED = new PostProcessingAction("FEED", 2);
      NONE = new PostProcessingAction("NONE", 3);
      FEED_REFRESH = new PostProcessingAction("FEED_REFRESH", 4);
      CLEAR_REFRESH = new PostProcessingAction("CLEAR_REFRESH", 5);
      PostProcessingAction[] arrayOfPostProcessingAction = new PostProcessingAction[6];
      PostProcessingAction localPostProcessingAction1 = OPEN;
      arrayOfPostProcessingAction[0] = localPostProcessingAction1;
      PostProcessingAction localPostProcessingAction2 = NEXT;
      arrayOfPostProcessingAction[1] = localPostProcessingAction2;
      PostProcessingAction localPostProcessingAction3 = FEED;
      arrayOfPostProcessingAction[2] = localPostProcessingAction3;
      PostProcessingAction localPostProcessingAction4 = NONE;
      arrayOfPostProcessingAction[3] = localPostProcessingAction4;
      PostProcessingAction localPostProcessingAction5 = FEED_REFRESH;
      arrayOfPostProcessingAction[4] = localPostProcessingAction5;
      PostProcessingAction localPostProcessingAction6 = CLEAR_REFRESH;
      arrayOfPostProcessingAction[5] = localPostProcessingAction6;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.mix.PlayQueueFeeder
 * JD-Core Version:    0.6.2
 */