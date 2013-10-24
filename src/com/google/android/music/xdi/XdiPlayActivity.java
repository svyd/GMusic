package com.google.android.music.xdi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.AlbumSongList;
import com.google.android.music.medialist.ArtistSongList;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.medialist.NautilusAlbumSongList;
import com.google.android.music.medialist.NautilusArtistSongList;
import com.google.android.music.medialist.NautilusSelectedSongList;
import com.google.android.music.medialist.NautilusSingleSongList;
import com.google.android.music.medialist.PlaylistSongList;
import com.google.android.music.medialist.SelectedSongList;
import com.google.android.music.medialist.SharedWithMeSongList;
import com.google.android.music.medialist.SingleSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.mix.MixDescriptor;
import com.google.android.music.mix.MixDescriptor.Type;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.ui.cardlib.utils.Lists;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.MusicUtils.ServiceToken;
import java.util.ArrayList;
import java.util.List;

public class XdiPlayActivity extends Activity
{
  private PlaySongListRadioTask mRadioAsyncTask;
  private MusicUtils.ServiceToken mServiceToken;

  private void playMusic(Intent paramIntent)
  {
    boolean bool = processIntent(paramIntent);
    if (this.mRadioAsyncTask != null)
      return;
    finish();
  }

  private boolean playRadio(MixDescriptor paramMixDescriptor)
  {
    boolean bool = false;
    String str1 = paramMixDescriptor.toString();
    String str2 = "Starting playback of radio.  mix: " + str1;
    Log.d("MusicXdi", str2);
    if (MusicUtils.sService == null)
      Log.w("MusicXdi", "Playback service not initialized.");
    while (true)
    {
      return bool;
      try
      {
        MusicUtils.sService.openMix(paramMixDescriptor);
        bool = true;
      }
      catch (RemoteException localRemoteException)
      {
        String str3 = "Error while playing radio.  mix: " + str1 + "\n" + localRemoteException;
        Log.wtf("MusicXdi", str3);
      }
    }
  }

  private boolean processIntent(Intent paramIntent)
  {
    StringBuilder localStringBuilder1 = new StringBuilder().append("Received intent: ");
    String str1 = paramIntent.toString();
    String str2 = str1;
    Log.d("MusicXdi", str2);
    int i = paramIntent.getIntExtra("container", -1);
    boolean bool1;
    if (i == -1)
    {
      Log.wtf("MusicXdi", "Container not set in intent.");
      bool1 = false;
    }
    String str3;
    long l1;
    String str4;
    int j;
    String str5;
    while (true)
    {
      return bool1;
      str3 = paramIntent.getStringExtra("name");
      l1 = paramIntent.getLongExtra("id", 65535L);
      str4 = paramIntent.getStringExtra("id_string");
      j = paramIntent.getIntExtra("offset", 0);
      str5 = paramIntent.getStringExtra("art_uri");
      switch (i)
      {
      case 16:
      case 17:
      default:
        StringBuilder localStringBuilder2 = new StringBuilder().append("Unexpected container: ");
        int k = i;
        String str6 = k;
        Log.wtf("MusicXdi", str6);
        bool1 = false;
        break;
      case 2:
        if (l1 != 65535L)
          break label281;
        Log.wtf("MusicXdi", "Playlist ID not set in intent.");
        bool1 = false;
      case 11:
      case 1:
      case 5:
      case 4:
      case 13:
      case 14:
      case 15:
      case 8:
      case 9:
      case 10:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 3:
      case 6:
      case 7:
      case 12:
      case 23:
      }
    }
    label281: String str7 = "Starting playback of playlist " + l1;
    Log.d("MusicXdi", str7);
    PlaylistSongList localPlaylistSongList = new PlaylistSongList(l1, str3, 0, null, null, null, null, null, false);
    int m = j;
    MusicUtils.playMediaList(localPlaylistSongList, m);
    while (true)
    {
      bool1 = true;
      break;
      if (l1 == 65535L)
      {
        Log.wtf("MusicXdi", "Shared playlist ID not set in intent.");
        bool1 = false;
        break;
      }
      String str8 = "Starting playback of shared playlist " + l1;
      Log.d("MusicXdi", str8);
      String str9 = paramIntent.getStringExtra("playlist_share_token");
      if (TextUtils.isEmpty(str9))
      {
        Log.wtf("MusicXdi", "Shared playlist token not set in intent.");
        bool1 = false;
        break;
      }
      String str10 = paramIntent.getStringExtra("description");
      String str11 = paramIntent.getStringExtra("playlist_share_token");
      String str12 = paramIntent.getStringExtra("playlist_owner_photo_url");
      long l2 = l1;
      String str13 = str3;
      SharedWithMeSongList localSharedWithMeSongList = new SharedWithMeSongList(l2, str9, str13, str10, str11, str5, str12);
      int n = j;
      MusicUtils.playMediaList(localSharedWithMeSongList, n);
      continue;
      if (l1 == 65535L)
      {
        Log.wtf("MusicXdi", "Album ID not set in intent.");
        bool1 = false;
        break;
      }
      String str14 = "Starting playback of album " + l1;
      Log.d("MusicXdi", str14);
      AlbumSongList localAlbumSongList1 = new AlbumSongList(l1, false);
      int i1 = j;
      MusicUtils.playMediaList(localAlbumSongList1, i1);
      continue;
      if (TextUtils.isEmpty(str4))
      {
        Log.wtf("MusicXdi", "Nautilus album ID not set in intent.");
        bool1 = false;
        break;
      }
      StringBuilder localStringBuilder3 = new StringBuilder().append("Starting playback of Nautilus album ");
      String str15 = str4;
      String str16 = str15;
      Log.d("MusicXdi", str16);
      String str17 = str4;
      NautilusAlbumSongList localNautilusAlbumSongList1 = new NautilusAlbumSongList(str17);
      int i2 = j;
      MusicUtils.playMediaList(localNautilusAlbumSongList1, i2);
      continue;
      if (l1 == 65535L)
      {
        Log.wtf("MusicXdi", "Radio ID not set in intent.");
        bool1 = false;
        break;
      }
      String str18 = "Starting playback of radio " + l1;
      Log.d("MusicXdi", str18);
      MixDescriptor.Type localType1 = MixDescriptor.Type.RADIO;
      long l3 = l1;
      String str19 = str3;
      String str20 = str5;
      MixDescriptor localMixDescriptor1 = new MixDescriptor(l3, localType1, str19, str20);
      if (!playRadio(localMixDescriptor1))
      {
        bool1 = false;
        break;
        if (l1 == 65535L)
        {
          Log.wtf("MusicXdi", "Artist local seed ID not set in intent.");
          bool1 = false;
          break;
        }
        ArtistSongList localArtistSongList = new ArtistSongList(l1, str3, -1, false);
        startPlaySongListRadioTask(localArtistSongList);
        String str21 = "started radio async task for artist " + l1;
        Log.v("MusicXdi", str21);
        continue;
        if (l1 == 65535L)
        {
          Log.wtf("MusicXdi", "Album local seed ID not set in intent.");
          bool1 = false;
          break;
        }
        AlbumSongList localAlbumSongList2 = new AlbumSongList(l1, false);
        startPlaySongListRadioTask(localAlbumSongList2);
        String str22 = "started radio async task for album " + l1;
        Log.v("MusicXdi", str22);
        continue;
        if (l1 == 65535L)
        {
          Log.wtf("MusicXdi", "Track local seed ID not set in intent.");
          bool1 = false;
          break;
        }
        SingleSongList localSingleSongList = new SingleSongList(l1, str3);
        startPlaySongListRadioTask(localSingleSongList);
        String str23 = "started radio async task for single track " + l1;
        Log.v("MusicXdi", str23);
        continue;
        if (TextUtils.isEmpty(str4))
        {
          Log.wtf("MusicXdi", "Nautilus artist remote seed ID not set in intent.");
          bool1 = false;
          break;
        }
        String str24 = str4;
        NautilusArtistSongList localNautilusArtistSongList = new NautilusArtistSongList(str24, str3);
        startPlaySongListRadioTask(localNautilusArtistSongList);
        StringBuilder localStringBuilder4 = new StringBuilder().append("started radio async task for nautilus artist ");
        String str25 = str4;
        String str26 = str25;
        Log.v("MusicXdi", str26);
        continue;
        if (TextUtils.isEmpty(str4))
        {
          Log.wtf("MusicXdi", "Nautilus album remote seed ID not set in intent.");
          bool1 = false;
          break;
        }
        String str27 = str4;
        NautilusAlbumSongList localNautilusAlbumSongList2 = new NautilusAlbumSongList(str27);
        startPlaySongListRadioTask(localNautilusAlbumSongList2);
        StringBuilder localStringBuilder5 = new StringBuilder().append("started radio async task for nautilus album ");
        String str28 = str4;
        String str29 = str28;
        Log.v("MusicXdi", str29);
        continue;
        if (TextUtils.isEmpty(str4))
        {
          Log.wtf("MusicXdi", "Nautilus track remote seed ID not set in intent.");
          bool1 = false;
          break;
        }
        String str30 = str4;
        NautilusSingleSongList localNautilusSingleSongList = new NautilusSingleSongList(str30, str3);
        startPlaySongListRadioTask(localNautilusSingleSongList);
        StringBuilder localStringBuilder6 = new StringBuilder().append("started radio async task for single nautilus track ");
        String str31 = str4;
        String str32 = str31;
        Log.v("MusicXdi", str32);
        continue;
        if (TextUtils.isEmpty(str4))
        {
          Log.wtf("MusicXdi", "Artist remote seed ID not set in intent.");
          bool1 = false;
          break;
        }
        Object localObject1;
        if (MusicUtils.isNautilusId(str4))
        {
          String str33 = str4;
          localObject1 = new NautilusArtistSongList(str33, str3);
        }
        while (true)
        {
          startPlaySongListRadioTask((SongList)localObject1);
          StringBuilder localStringBuilder7 = new StringBuilder().append("started radio async task for artist ");
          String str34 = str4;
          String str35 = str34;
          Log.v("MusicXdi", str35);
          break;
          try
          {
            long l4 = Long.valueOf(str4).longValue();
            long l5 = l4;
            localObject1 = new ArtistSongList(l5, str3, -1, false);
          }
          catch (NumberFormatException localNumberFormatException1)
          {
            StringBuilder localStringBuilder8 = new StringBuilder().append("Error parsing long value from ");
            String str36 = str4;
            String str37 = str36;
            Log.wtf("MusicXdi", str37);
            bool1 = false;
          }
        }
        break;
        if (TextUtils.isEmpty(str4))
        {
          Log.wtf("MusicXdi", "Album remote seed ID not set in intent.");
          bool1 = false;
          break;
        }
        if (MusicUtils.isNautilusId(str4))
        {
          String str38 = str4;
          localObject1 = new NautilusAlbumSongList(str38);
        }
        while (true)
        {
          startPlaySongListRadioTask((SongList)localObject1);
          StringBuilder localStringBuilder9 = new StringBuilder().append("started radio async task for album ");
          String str39 = str4;
          String str40 = str39;
          Log.v("MusicXdi", str40);
          break;
          try
          {
            long l6 = Long.valueOf(str4).longValue();
            long l7 = l6;
            localObject1 = new AlbumSongList(l7, false);
          }
          catch (NumberFormatException localNumberFormatException2)
          {
            StringBuilder localStringBuilder10 = new StringBuilder().append("Error parsing long value from ");
            String str41 = str4;
            String str42 = str41;
            Log.wtf("MusicXdi", str42);
            bool1 = false;
          }
        }
        break;
        if (TextUtils.isEmpty(str4))
        {
          Log.wtf("MusicXdi", "Track remote seed ID not set in intent.");
          bool1 = false;
          break;
        }
        if (MusicUtils.isNautilusId(str4))
        {
          String str43 = str4;
          localObject1 = new NautilusSingleSongList(str43, str3);
        }
        while (true)
        {
          startPlaySongListRadioTask((SongList)localObject1);
          StringBuilder localStringBuilder11 = new StringBuilder().append("started radio async task for single track ");
          String str44 = str4;
          String str45 = str44;
          Log.v("MusicXdi", str45);
          break;
          try
          {
            long l8 = Long.valueOf(str4).longValue();
            long l9 = l8;
            localObject1 = new SingleSongList(l9, str3);
          }
          catch (NumberFormatException localNumberFormatException3)
          {
            StringBuilder localStringBuilder12 = new StringBuilder().append("Error parsing long value from ");
            String str46 = str4;
            String str47 = str46;
            Log.wtf("MusicXdi", str47);
            bool1 = false;
          }
        }
        break;
        MixDescriptor localMixDescriptor2 = MixDescriptor.getLuckyRadio(this);
        boolean bool2 = playRadio(localMixDescriptor2);
        Log.v("MusicXdi", "started I am feeling lucky radio.");
        continue;
        if (TextUtils.isEmpty(str4))
        {
          Log.wtf("MusicXdi", "Genre remote seed ID not set in intent.");
          bool1 = false;
          break;
        }
        MixDescriptor localMixDescriptor3 = new com/google/android/music/mix/MixDescriptor;
        MixDescriptor.Type localType2 = MixDescriptor.Type.GENRE_SEED;
        MixDescriptor localMixDescriptor4 = localMixDescriptor3;
        String str48 = str4;
        localMixDescriptor4.<init>(str48, localType2, str3, str5);
        XdiPlayActivity localXdiPlayActivity = this;
        MixDescriptor localMixDescriptor5 = localMixDescriptor3;
        boolean bool3 = localXdiPlayActivity.playRadio(localMixDescriptor5);
        Log.v("MusicXdi", "started Genre radio.");
        continue;
        if (l1 == 65535L)
        {
          Log.wtf("MusicXdi", "Suggested mix ID not set in intent.");
          bool1 = false;
          break;
        }
        String str49 = "Starting playback of suggested mix " + l1;
        Log.d("MusicXdi", str49);
        long l10 = l1;
        String str50 = str3;
        MusicUtils.playMediaList(new PlaylistSongList(l10, str50, 50, null, null, null, null, null, false), 0);
        continue;
        if (l1 == 65535L)
        {
          Log.wtf("MusicXdi", "Track ID not set in intent.");
          bool1 = false;
          break;
        }
        String str51 = "Starting playback of track " + l1;
        Log.d("MusicXdi", str51);
        MusicUtils.playMediaList(new SingleSongList(l1, str3));
        continue;
        if (TextUtils.isEmpty(str4))
        {
          Log.wtf("MusicXdi", "Nautilus track ID not set in intent.");
          bool1 = false;
          break;
        }
        StringBuilder localStringBuilder13 = new StringBuilder().append("Starting playback of Nautilus track ");
        String str52 = str4;
        String str53 = str52;
        Log.d("MusicXdi", str53);
        String str54 = str4;
        MusicUtils.playMediaList(new NautilusSingleSongList(str54, str3));
        continue;
        String str55 = paramIntent.getStringExtra("song_list");
        if (TextUtils.isEmpty(str55))
        {
          Log.wtf("MusicXdi", "Nautilus song list not set in intent.");
          bool1 = false;
          break;
        }
        Object localObject2 = (NautilusSelectedSongList)MediaList.thaw(str55);
        if (localObject2 == null)
        {
          Log.wtf("MusicXdi", "Error deserializing Nautilus song list in intent.");
          bool1 = false;
          break;
        }
        Log.d("MusicXdi", "Starting playback of Nautilus song list.");
        Object localObject3 = localObject2;
        int i3 = j;
        MusicUtils.playMediaList(localObject3, i3);
        continue;
        str55 = paramIntent.getStringExtra("song_list");
        if (TextUtils.isEmpty(str55))
        {
          Log.wtf("MusicXdi", "Local song list not set in intent.");
          bool1 = false;
          break;
        }
        localObject2 = (SelectedSongList)MediaList.thaw(str55);
        if (localObject2 == null)
        {
          Log.wtf("MusicXdi", "Error deserializing song list in intent.");
          bool1 = false;
          break;
        }
        Log.d("MusicXdi", "Starting playback of song list.");
        Object localObject4 = localObject2;
        int i4 = j;
        MusicUtils.playMediaList(localObject4, i4);
      }
    }
  }

  private void startPlaySongListRadioTask(SongList paramSongList)
  {
    stopPlaySongListRadioTaskIfRunning();
    PlaySongListRadioTask localPlaySongListRadioTask1 = new PlaySongListRadioTask(this);
    this.mRadioAsyncTask = localPlaySongListRadioTask1;
    PlaySongListRadioTask localPlaySongListRadioTask2 = this.mRadioAsyncTask;
    SongList[] arrayOfSongList = new SongList[1];
    arrayOfSongList[0] = paramSongList;
    AsyncTask localAsyncTask = localPlaySongListRadioTask2.execute(arrayOfSongList);
  }

  private void stopPlaySongListRadioTaskIfRunning()
  {
    if (this.mRadioAsyncTask == null)
      return;
    if (!this.mRadioAsyncTask.isCancelled())
      boolean bool = this.mRadioAsyncTask.cancel(true);
    this.mRadioAsyncTask = null;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    ServiceConnection local1 = new ServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        XdiPlayActivity localXdiPlayActivity = XdiPlayActivity.this;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            XdiPlayActivity localXdiPlayActivity = XdiPlayActivity.this;
            Intent localIntent = XdiPlayActivity.this.getIntent();
            localXdiPlayActivity.playMusic(localIntent);
          }
        };
        localXdiPlayActivity.runOnUiThread(local1);
      }

      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
        MusicUtils.ServiceToken localServiceToken = XdiPlayActivity.access$202(XdiPlayActivity.this, null);
      }
    };
    MusicUtils.ServiceToken localServiceToken = MusicUtils.bindToService(this, local1);
    this.mServiceToken = localServiceToken;
  }

  protected void onDestroy()
  {
    super.onDestroy();
    stopPlaySongListRadioTaskIfRunning();
    if (this.mServiceToken == null)
      return;
    MusicUtils.unbindFromService(this.mServiceToken);
  }

  private class PlaySongListRadioTask extends AsyncTask<SongList, Void, List<MixDescriptor>>
  {
    private final Context mContext;

    PlaySongListRadioTask(Context arg2)
    {
      Object localObject;
      this.mContext = localObject;
    }

    protected List<MixDescriptor> doInBackground(SongList[] paramArrayOfSongList)
    {
      MixDescriptor[] arrayOfMixDescriptor;
      if (paramArrayOfSongList.length > 0)
      {
        arrayOfMixDescriptor = new MixDescriptor[1];
        Context localContext = this.mContext;
        SongList localSongList = paramArrayOfSongList[0];
        MixDescriptor localMixDescriptor = MusicUtils.getSongListRadioMixDescriptor(localContext, localSongList);
        arrayOfMixDescriptor[0] = localMixDescriptor;
      }
      for (ArrayList localArrayList = Lists.newArrayList(arrayOfMixDescriptor); ; localArrayList = null)
      {
        return localArrayList;
        Log.wtf("MusicXdi", "Empty SongList.");
      }
    }

    protected void onPostExecute(List<MixDescriptor> paramList)
    {
      if ((paramList != null) && (!paramList.isEmpty()))
      {
        MixDescriptor localMixDescriptor = (MixDescriptor)paramList.get(0);
        StringBuilder localStringBuilder = new StringBuilder().append("playing radio.  mix: ");
        String str1 = localMixDescriptor.toString();
        String str2 = str1;
        Log.v("MusicXdi", str2);
        boolean bool = XdiPlayActivity.this.playRadio(localMixDescriptor);
      }
      while (true)
      {
        XdiPlayActivity.this.finish();
        return;
        Log.wtf("MusicXdi", "Invalid MixDescriptors for SongList.");
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.XdiPlayActivity
 * JD-Core Version:    0.6.2
 */