package com.google.android.music.leanback;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.text.TextUtils;
import com.google.android.gsf.Gservices;
import com.google.android.music.cloudclient.MusicCloud;
import com.google.android.music.cloudclient.MusicCloudImpl;
import com.google.android.music.cloudclient.RadioFeedResponse;
import com.google.android.music.cloudclient.RadioFeedResponse.RadioData;
import com.google.android.music.cloudclient.TrackJson;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.Store;
import com.google.android.music.sync.google.model.SyncableRadioStation;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

class SuggestedMixUpdater
{
  private static final boolean LOGV = LeanbackLog.LOGV;
  private final MusicCloud mCloudClient;
  private final Context mContext;
  private final MusicPreferences mPrefs;
  private final Store mStore;

  SuggestedMixUpdater(Context paramContext, MusicPreferences paramMusicPreferences)
  {
    this.mContext = paramContext;
    MusicCloudImpl localMusicCloudImpl = new MusicCloudImpl(paramContext);
    this.mCloudClient = localMusicCloudImpl;
    Store localStore = Store.getInstance(paramContext);
    this.mStore = localStore;
    this.mPrefs = paramMusicPreferences;
  }

  static boolean cleanupIfDisabled(Context paramContext)
  {
    boolean bool = false;
    ContentResolver localContentResolver = paramContext.getContentResolver();
    int i = getMaxNumberOfSeeds(localContentResolver);
    int j = getMaxMixSize(localContentResolver);
    if ((i <= 0) || (j <= 0))
      if (LOGV)
      {
        Object[] arrayOfObject = new Object[2];
        Integer localInteger1 = Integer.valueOf(i);
        arrayOfObject[0] = localInteger1;
        Integer localInteger2 = Integer.valueOf(j);
        arrayOfObject[1] = localInteger2;
        String str1 = String.format("Suggested mixes turned off via GSerives. Seeds: %d, Mix size: %d", arrayOfObject);
        Log.d("MusicLeanback", str1);
      }
    for (bool = true; ; bool = true)
    {
      int k;
      do
      {
        if (bool)
          Store.getInstance(paramContext).removeAllSuggestedMixes();
        return bool;
        k = Gservices.getInt(localContentResolver, "music_suggested_mixes_min_locker_tracks", 200);
      }
      while ((k <= 0) || (Store.getInstance(paramContext).hasRemoteSongs(k)));
      if (LOGV)
      {
        String str2 = "Suggested mixes turned off. Locker has less than " + k;
        Log.d("MusicLeanback", str2);
      }
    }
  }

  private static int getMaxMixSize(ContentResolver paramContentResolver)
  {
    return Gservices.getInt(paramContentResolver, "music_suggested_mix_size", 25);
  }

  private static int getMaxNumberOfSeeds(ContentResolver paramContentResolver)
  {
    return Gservices.getInt(paramContentResolver, "music_max_suggested_mixes", 4);
  }

  private boolean storeMixes(int paramInt, RadioFeedResponse paramRadioFeedResponse)
  {
    List localList1 = paramRadioFeedResponse.mRadioData.mRadioStations;
    int i = paramRadioFeedResponse.mRadioData.mRadioStations.size();
    String[] arrayOfString1 = new String[i];
    String[] arrayOfString2 = new String[i];
    List[] arrayOfList = new List[i];
    int j = 0;
    Iterator localIterator = localList1.iterator();
    List localList2;
    boolean bool;
    if (localIterator.hasNext())
    {
      localList2 = ((SyncableRadioStation)localIterator.next()).mTracks;
      if (localList2.isEmpty())
      {
        Log.e("MusicLeanback", "Empty suggested mix");
        bool = false;
      }
    }
    while (true)
    {
      return bool;
      TrackJson localTrackJson = (TrackJson)localList2.get(0);
      String str1 = localTrackJson.getEffectiveRemoteId();
      arrayOfString1[j] = str1;
      if (TextUtils.isEmpty(arrayOfString1[j]))
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Empty id for seed track: ");
        String str2 = localTrackJson.toString();
        String str3 = str2;
        Exception localException = new Exception();
        Log.wtf("MusicLeanback", str3, localException);
        bool = false;
      }
      else
      {
        String str4 = localTrackJson.mTitle;
        arrayOfString2[j] = str4;
        arrayOfList[j] = localList2;
        j += 1;
        break;
        this.mStore.updateSuggestedMixes(paramInt, arrayOfString1, arrayOfString2, arrayOfList);
        bool = true;
      }
    }
  }

  boolean updateMixes()
  {
    boolean bool1 = null;
    int i = 0;
    try
    {
      Account localAccount = this.mPrefs.getStreamingAccount();
      if (localAccount == null)
        Log.w("MusicLeanback", "No valid streaming account configured");
      while (true)
      {
        return bool1;
        if (!cleanupIfDisabled(this.mContext))
        {
          j = Store.computeAccountHash(localAccount);
          k = getMaxNumberOfSeeds(this.mContext.getContentResolver());
          m = getMaxMixSize(this.mContext.getContentResolver());
          if ((k > 0) && (m > 0))
            break;
          Object[] arrayOfObject = new Object[2];
          Integer localInteger1 = Integer.valueOf(k);
          arrayOfObject[0] = localInteger1;
          Integer localInteger2 = Integer.valueOf(m);
          arrayOfObject[1] = localInteger2;
          String str3 = String.format("Invalid  number of seeds (%d) or mix size (%d)", arrayOfObject);
          Log.wtf("MusicLeanback", str3);
        }
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        int k;
        int m;
        StringBuilder localStringBuilder1 = new StringBuilder().append("IOException while updating suggested mixes. ");
        String str4 = localIOException.getMessage();
        String str5 = str4;
        Log.e("MusicLeanback", str5);
        bool1 = LOGV;
        if (bool1 != null)
        {
          String str1 = "MusicLeanback";
          Log.e(str1, "IOException", localIOException);
        }
        int n = i;
        continue;
        int i1 = this.mPrefs.getContentFilter();
        localRadioFeedResponse = this.mCloudClient.getMixesFeed(k, m, i1);
        if ((localRadioFeedResponse != null) && (localRadioFeedResponse.mRadioData != null) && (localRadioFeedResponse.mRadioData.mRadioStations != null) && (localRadioFeedResponse.mRadioData.mRadioStations.size() >= 1))
          break;
        Log.e("MusicLeanback", "Failed to get suggested mixes content.");
      }
    }
    catch (InterruptedException localInterruptedException)
    {
      while (true)
      {
        int j;
        RadioFeedResponse localRadioFeedResponse;
        StringBuilder localStringBuilder2 = new StringBuilder().append("InterruptedException while updating suggested mixes. ");
        String str6 = localInterruptedException.getMessage();
        String str7 = str6;
        Log.e("MusicLeanback", str7);
        boolean bool2 = LOGV;
        if (bool2)
        {
          String str2 = "MusicLeanback";
          Log.e(str2, "InterruptedException", localInterruptedException);
          continue;
          boolean bool3 = storeMixes(j, localRadioFeedResponse);
          i = bool3;
        }
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.leanback.SuggestedMixUpdater
 * JD-Core Version:    0.6.2
 */