package com.google.android.music.playback;

import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.store.MusicFile;

public abstract interface AsyncMediaPlayer
{
  public abstract long duration();

  public abstract int getAudioSessionId();

  public abstract int getErrorType();

  public abstract String getRemoteSongId();

  public abstract boolean isInErrorState();

  public abstract boolean isInitialized();

  public abstract boolean isPlaying();

  public abstract boolean isPreparing();

  public abstract boolean isRenderingAudioLocally();

  public abstract boolean isStreaming();

  public abstract void pause();

  public abstract long position();

  public abstract void release();

  public abstract long seek(long paramLong);

  public abstract void setAsCurrentPlayer();

  public abstract void setAudioSessionId(int paramInt);

  public abstract void setDataSource(ContentIdentifier paramContentIdentifier, long paramLong1, long paramLong2, boolean paramBoolean1, boolean paramBoolean2, MusicFile paramMusicFile, AsyncCommandCallback paramAsyncCommandCallback);

  public abstract void setNextPlayer(AsyncMediaPlayer paramAsyncMediaPlayer);

  public abstract void setVolume(float paramFloat);

  public abstract void start();

  public abstract void stop();

  public static abstract interface AsyncCommandCallback
  {
    public abstract void onFailure(boolean paramBoolean);

    public abstract void onSuccess();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.playback.AsyncMediaPlayer
 * JD-Core Version:    0.6.2
 */