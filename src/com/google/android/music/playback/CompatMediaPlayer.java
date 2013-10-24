package com.google.android.music.playback;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.SystemClock;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CompatMediaPlayer extends MediaPlayer
  implements MediaPlayer.OnCompletionListener
{
  private boolean mCompatMode = true;
  private MediaPlayer.OnCompletionListener mCompletion;
  private MediaPlayer mNextPlayer;
  private Method mSetNextPlayer;

  public CompatMediaPlayer()
  {
    try
    {
      Class[] arrayOfClass = new Class[1];
      arrayOfClass[0] = MediaPlayer.class;
      Method localMethod = MediaPlayer.class.getMethod("setNextMediaPlayer", arrayOfClass);
      this.mSetNextPlayer = localMethod;
      this.mCompatMode = false;
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      this.mCompatMode = true;
      super.setOnCompletionListener(this);
    }
  }

  public MediaPlayer getNextPlayer()
  {
    return this.mNextPlayer;
  }

  public void onCompletion(MediaPlayer paramMediaPlayer)
  {
    if (this.mNextPlayer != null)
    {
      SystemClock.sleep(50L);
      this.mNextPlayer.start();
    }
    if (this.mCompletion == null)
      return;
    this.mCompletion.onCompletion(this);
  }

  public void setNextMediaPlayerCompat(MediaPlayer paramMediaPlayer)
  {
    if (this.mCompatMode)
    {
      this.mNextPlayer = paramMediaPlayer;
      return;
    }
    try
    {
      Method localMethod = this.mSetNextPlayer;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramMediaPlayer;
      Object localObject = localMethod.invoke(this, arrayOfObject);
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new RuntimeException(localIllegalArgumentException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      throw new RuntimeException(localIllegalAccessException);
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      throw new RuntimeException(localInvocationTargetException);
    }
  }

  public void setOnCompletionListener(MediaPlayer.OnCompletionListener paramOnCompletionListener)
  {
    if (this.mCompatMode)
    {
      this.mCompletion = paramOnCompletionListener;
      return;
    }
    super.setOnCompletionListener(paramOnCompletionListener);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.playback.CompatMediaPlayer
 * JD-Core Version:    0.6.2
 */