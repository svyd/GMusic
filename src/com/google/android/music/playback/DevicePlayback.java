package com.google.android.music.playback;

import android.content.Context;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.medialist.SongList;
import com.google.android.music.mix.MixDescriptor;
import com.google.android.music.mix.MixGenerationState;
import java.io.PrintWriter;

public abstract class DevicePlayback
{
  protected final MusicPlaybackService mService;

  protected DevicePlayback(MusicPlaybackService paramMusicPlaybackService)
  {
    this.mService = paramMusicPlaybackService;
  }

  public abstract void cancelMix();

  public abstract void clearQueue();

  public abstract void disableGroupPlay();

  public void dump(PrintWriter paramPrintWriter)
  {
  }

  public abstract long duration();

  public abstract String getAlbumArtUrl(long paramLong);

  public abstract long getAlbumId();

  public abstract String getAlbumName();

  public abstract long getArtistId();

  public abstract String getArtistName();

  public abstract ContentIdentifier getAudioId();

  public abstract int getAudioSessionId();

  protected final Context getContext()
  {
    return this.mService;
  }

  public abstract TrackInfo getCurrentTrackInfo();

  public abstract int getErrorType();

  public abstract long getLastUserInteractionTime();

  public abstract SongList getMediaList();

  public abstract MixGenerationState getMixState();

  public abstract PlaybackState getPlaybackState();

  public abstract int getPreviewPlayType();

  public abstract int getQueuePosition();

  public abstract int getQueueSize();

  public abstract int getRating();

  public abstract int getRepeatMode();

  public abstract int getShuffleMode();

  public abstract String getSongStoreId();

  public abstract String getSortableAlbumArtistName();

  public abstract State getState();

  public abstract String getTrackName();

  public abstract boolean hasLocal();

  public abstract boolean hasRemote();

  public abstract boolean hasValidPlaylist();

  public abstract boolean isCurrentSongLoaded();

  public abstract boolean isInErrorState();

  public abstract boolean isInFatalErrorState();

  public abstract boolean isInfiniteMixMode();

  public abstract boolean isPlaying();

  public abstract boolean isPlayingLocally();

  public abstract boolean isPreparing();

  public abstract boolean isStreaming();

  public abstract boolean isStreamingFullyBuffered();

  public abstract void next();

  protected final void notifyChange(String paramString)
  {
    if (this.mService == null)
      return;
    this.mService.notifyChange(paramString, this);
  }

  protected void notifyFailure()
  {
    if (this.mService == null)
      return;
    this.mService.notifyFailure(this);
  }

  protected void onCreate()
  {
  }

  protected void onDestroy()
  {
  }

  public abstract void open(SongList paramSongList, int paramInt, boolean paramBoolean);

  public abstract void openAndQueue(SongList paramSongList, int paramInt);

  public abstract void openMix(MixDescriptor paramMixDescriptor);

  public abstract void pause();

  public abstract void play();

  public abstract boolean playlistLoading();

  public abstract long position();

  public abstract void prev();

  public abstract void refreshRadio();

  protected void saveState()
  {
  }

  public abstract long seek(long paramLong);

  public abstract void setQueuePosition(int paramInt);

  public abstract void setRating(int paramInt);

  public abstract void setRepeatMode(int paramInt);

  public abstract void setShuffleMode(int paramInt);

  public abstract void shuffleAll();

  public abstract void shuffleOnDevice();

  public abstract void shuffleSongs(SongList paramSongList);

  public abstract void stop();

  public abstract boolean supportsRating();

  public static enum State
  {
    static
    {
      SWITCHING_TRACKS = new State("SWITCHING_TRACKS", 3);
      PLAYING = new State("PLAYING", 4);
      State[] arrayOfState = new State[5];
      State localState1 = NO_PLAYLIST;
      arrayOfState[0] = localState1;
      State localState2 = PAUSED;
      arrayOfState[1] = localState2;
      State localState3 = TRANSIENT_PAUSE;
      arrayOfState[2] = localState3;
      State localState4 = SWITCHING_TRACKS;
      arrayOfState[3] = localState4;
      State localState5 = PLAYING;
      arrayOfState[4] = localState5;
    }

    boolean playingOrWillPlay()
    {
      int[] arrayOfInt = DevicePlayback.1.$SwitchMap$com$google$android$music$playback$DevicePlayback$State;
      int i = ordinal();
      switch (arrayOfInt[i])
      {
      default:
      case 1:
      case 2:
      case 3:
      }
      for (boolean bool = false; ; bool = true)
        return bool;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.playback.DevicePlayback
 * JD-Core Version:    0.6.2
 */