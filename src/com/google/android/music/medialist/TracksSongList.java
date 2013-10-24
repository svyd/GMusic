package com.google.android.music.medialist;

import android.accounts.Account;
import android.content.Context;
import com.google.android.music.cloudclient.JsonUtils;
import com.google.android.music.cloudclient.MixJson;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.Store;
import com.google.android.music.sync.google.model.Track;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class TracksSongList extends ExternalSongList
{
  private final List<Track> mTracks;

  public TracksSongList(String paramString)
  {
    super(localDomain);
    try
    {
      MixJson localMixJson = (MixJson)JsonUtils.parseFromJsonString(MixJson.class, paramString);
      List localList = localMixJson.mTracks;
      this.mTracks = localList;
      return;
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException("Failed to parse json", localIOException);
    }
  }

  public static TracksSongList createList(List<Track> paramList)
  {
    String str = encodeAsString(paramList);
    return new TracksSongList(str);
  }

  private static String encodeAsString(List<Track> paramList)
  {
    MixJson localMixJson = new MixJson();
    localMixJson.mTracks = paramList;
    return JsonUtils.toJsonString(localMixJson);
  }

  public long[] addToStore(Context paramContext, boolean paramBoolean)
  {
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, this);
    while (true)
    {
      Account localAccount2;
      try
      {
        Account localAccount1 = localMusicPreferences.getStreamingAccount();
        localAccount2 = localAccount1;
        MusicPreferences.releaseMusicPreferences(this);
        if (localAccount2 == null)
        {
          int i = 0;
          return i;
        }
      }
      finally
      {
        MusicPreferences.releaseMusicPreferences(this);
      }
      List localList = Store.getInstance(paramContext).insertSongs(this, localAccount2, paramBoolean);
      int j = 0;
      if (localList != null)
      {
        j = localList.size();
        int k = 0;
        Iterator localIterator = localList.iterator();
        while (localIterator.hasNext())
        {
          long l = ((Long)localIterator.next()).longValue();
          int m = k + 1;
          j[k] = l;
          k = m;
        }
      }
      else
      {
        Log.w("TracksSongList", "No songs were inserted");
      }
    }
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[1];
    String str = encodeAsString(this.mTracks);
    arrayOfString[0] = str;
    return arrayOfString;
  }

  public List<Track> getTracks()
  {
    return this.mTracks;
  }

  public boolean isAddToStoreSupported()
  {
    return true;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.TracksSongList
 * JD-Core Version:    0.6.2
 */