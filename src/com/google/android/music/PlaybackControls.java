package com.google.android.music;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.playback.PlaybackState;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.MusicUtils;
import java.util.HashMap;

public class PlaybackControls
  implements View.OnClickListener
{
  private static final HashMap<Integer, Integer> NORMAL_ASSETS;
  private static final HashMap<Integer, Integer> TABLET_ASSETS = new HashMap();
  private final HashMap<Integer, Integer> mAssets;
  private Context mContext;
  private RepeatingImageButton mNextButton;
  private PlayPauseButton mPauseButton;
  private RepeatingImageButton mPrevButton;
  private ImageView mRepeatButton;
  private ImageView mShuffleButton;

  static
  {
    NORMAL_ASSETS = new HashMap();
    HashMap localHashMap1 = TABLET_ASSETS;
    Integer localInteger1 = Integer.valueOf(2130837742);
    Integer localInteger2 = Integer.valueOf(2130837743);
    Object localObject1 = localHashMap1.put(localInteger1, localInteger2);
    HashMap localHashMap2 = TABLET_ASSETS;
    Integer localInteger3 = Integer.valueOf(2130837745);
    Integer localInteger4 = Integer.valueOf(2130837746);
    Object localObject2 = localHashMap2.put(localInteger3, localInteger4);
    HashMap localHashMap3 = TABLET_ASSETS;
    Integer localInteger5 = Integer.valueOf(2130837741);
    Integer localInteger6 = Integer.valueOf(2130837744);
    Object localObject3 = localHashMap3.put(localInteger5, localInteger6);
    HashMap localHashMap4 = TABLET_ASSETS;
    Integer localInteger7 = Integer.valueOf(2130837754);
    Integer localInteger8 = Integer.valueOf(2130837757);
    Object localObject4 = localHashMap4.put(localInteger7, localInteger8);
    HashMap localHashMap5 = TABLET_ASSETS;
    Integer localInteger9 = Integer.valueOf(2130837755);
    Integer localInteger10 = Integer.valueOf(2130837756);
    Object localObject5 = localHashMap5.put(localInteger9, localInteger10);
    HashMap localHashMap6 = NORMAL_ASSETS;
    Integer localInteger11 = Integer.valueOf(2130837742);
    Integer localInteger12 = Integer.valueOf(2130837742);
    Object localObject6 = localHashMap6.put(localInteger11, localInteger12);
    HashMap localHashMap7 = NORMAL_ASSETS;
    Integer localInteger13 = Integer.valueOf(2130837745);
    Integer localInteger14 = Integer.valueOf(2130837745);
    Object localObject7 = localHashMap7.put(localInteger13, localInteger14);
    HashMap localHashMap8 = NORMAL_ASSETS;
    Integer localInteger15 = Integer.valueOf(2130837741);
    Integer localInteger16 = Integer.valueOf(2130837741);
    Object localObject8 = localHashMap8.put(localInteger15, localInteger16);
    HashMap localHashMap9 = NORMAL_ASSETS;
    Integer localInteger17 = Integer.valueOf(2130837754);
    Integer localInteger18 = Integer.valueOf(2130837754);
    Object localObject9 = localHashMap9.put(localInteger17, localInteger18);
    HashMap localHashMap10 = NORMAL_ASSETS;
    Integer localInteger19 = Integer.valueOf(2130837755);
    Integer localInteger20 = Integer.valueOf(2130837755);
    Object localObject10 = localHashMap10.put(localInteger19, localInteger20);
  }

  public PlaybackControls(Context paramContext, MusicPreferences paramMusicPreferences, RepeatingImageButton paramRepeatingImageButton1, PlayPauseButton paramPlayPauseButton, RepeatingImageButton paramRepeatingImageButton2, ImageView paramImageView1, ImageView paramImageView2)
  {
    this.mContext = paramContext;
    this.mPrevButton = paramRepeatingImageButton1;
    this.mPauseButton = paramPlayPauseButton;
    boolean bool = this.mPauseButton.requestFocus();
    this.mNextButton = paramRepeatingImageButton2;
    this.mShuffleButton = paramImageView2;
    this.mRepeatButton = paramImageView1;
    HashMap localHashMap1;
    if (paramMusicPreferences.isTabletMusicExperience())
      localHashMap1 = TABLET_ASSETS;
    HashMap localHashMap2;
    for (this.mAssets = localHashMap1; ; this.mAssets = localHashMap2)
    {
      initButtons();
      return;
      localHashMap2 = NORMAL_ASSETS;
    }
  }

  private void cycleRepeat()
  {
    IMusicPlaybackService localIMusicPlaybackService = MusicUtils.sService;
    if (localIMusicPlaybackService == null)
      return;
    while (true)
    {
      int i;
      int j;
      try
      {
        i = localIMusicPlaybackService.getRepeatMode();
        if (i == 0)
        {
          j = 2;
          localIMusicPlaybackService.setRepeatMode(j);
          setRepeatButtonImage(j);
          return;
        }
      }
      catch (RemoteException localRemoteException)
      {
        String str = localRemoteException.getMessage();
        int k = Log.w("PlaybackControls", str, localRemoteException);
        return;
      }
      if (i == 2)
      {
        j = 1;
        if (localIMusicPlaybackService.getShuffleMode() != 0)
        {
          localIMusicPlaybackService.setShuffleMode(0);
          setShuffleButtonImage(0);
        }
      }
      else
      {
        j = 0;
      }
    }
  }

  private void doPauseResume()
  {
    IMusicPlaybackService localIMusicPlaybackService = MusicUtils.sService;
    if (localIMusicPlaybackService == null)
      return;
    PlaybackState localPlaybackState = MusicUtils.getPlaybackState();
    try
    {
      if (localPlaybackState.isPreparing())
        localIMusicPlaybackService.stop();
      while (true)
      {
        setPauseButtonImage(localPlaybackState);
        return;
        if (!localPlaybackState.isPlaying())
          break;
        localIMusicPlaybackService.pause();
      }
    }
    catch (RemoteException localRemoteException)
    {
      while (true)
      {
        String str = localRemoteException.getMessage();
        int i = Log.w("PlaybackControls", str, localRemoteException);
        continue;
        localIMusicPlaybackService.play();
      }
    }
  }

  private void initButtons()
  {
    RepeatingImageButton localRepeatingImageButton1 = this.mPrevButton;
    prepButton(localRepeatingImageButton1);
    PlayPauseButton localPlayPauseButton = this.mPauseButton;
    prepButton(localPlayPauseButton);
    RepeatingImageButton localRepeatingImageButton2 = this.mNextButton;
    prepButton(localRepeatingImageButton2);
    ImageView localImageView1 = this.mShuffleButton;
    prepButton(localImageView1);
    ImageView localImageView2 = this.mRepeatButton;
    prepButton(localImageView2);
    refreshButtonImages();
  }

  private void prepButton(View paramView)
  {
    paramView.setOnClickListener(this);
    Context localContext = this.mContext;
    FadingColorDrawable localFadingColorDrawable = new FadingColorDrawable(localContext, paramView);
    paramView.setBackgroundDrawable(localFadingColorDrawable);
  }

  private void setPauseButtonImage(PlaybackState paramPlaybackState)
  {
    if (paramPlaybackState == null)
      return;
    if ((paramPlaybackState.isPreparing()) && (paramPlaybackState.isStreaming()))
    {
      this.mPauseButton.setCurrentPlayState(1);
      return;
    }
    if (paramPlaybackState.isPlaying())
    {
      this.mPauseButton.setCurrentPlayState(2);
      return;
    }
    this.mPauseButton.setCurrentPlayState(3);
  }

  private void setRepeatButtonImage(int paramInt)
  {
    switch (paramInt)
    {
    default:
      ImageView localImageView1 = this.mRepeatButton;
      HashMap localHashMap1 = this.mAssets;
      Integer localInteger1 = Integer.valueOf(2130837741);
      int i = ((Integer)localHashMap1.get(localInteger1)).intValue();
      localImageView1.setImageResource(i);
      return;
    case 2:
      ImageView localImageView2 = this.mRepeatButton;
      HashMap localHashMap2 = this.mAssets;
      Integer localInteger2 = Integer.valueOf(2130837742);
      int j = ((Integer)localHashMap2.get(localInteger2)).intValue();
      localImageView2.setImageResource(j);
      return;
    case 1:
    }
    ImageView localImageView3 = this.mRepeatButton;
    HashMap localHashMap3 = this.mAssets;
    Integer localInteger3 = Integer.valueOf(2130837745);
    int k = ((Integer)localHashMap3.get(localInteger3)).intValue();
    localImageView3.setImageResource(k);
  }

  private void setRepeatButtonImage(PlaybackState paramPlaybackState)
  {
    if (this.mRepeatButton == null)
      return;
    if (paramPlaybackState == null)
      return;
    int i = paramPlaybackState.getRepeatMode();
    setRepeatButtonImage(i);
  }

  private void setShuffleButtonImage(int paramInt)
  {
    switch (paramInt)
    {
    default:
      ImageView localImageView1 = this.mShuffleButton;
      HashMap localHashMap1 = this.mAssets;
      Integer localInteger1 = Integer.valueOf(2130837755);
      int i = ((Integer)localHashMap1.get(localInteger1)).intValue();
      localImageView1.setImageResource(i);
      return;
    case 0:
    }
    ImageView localImageView2 = this.mShuffleButton;
    HashMap localHashMap2 = this.mAssets;
    Integer localInteger2 = Integer.valueOf(2130837754);
    int j = ((Integer)localHashMap2.get(localInteger2)).intValue();
    localImageView2.setImageResource(j);
  }

  private void setShuffleButtonImage(PlaybackState paramPlaybackState)
  {
    if (this.mShuffleButton == null)
      return;
    if (paramPlaybackState == null)
      return;
    int i = paramPlaybackState.getShuffleMode();
    setShuffleButtonImage(i);
  }

  private void toggleShuffle()
  {
    IMusicPlaybackService localIMusicPlaybackService = MusicUtils.sService;
    if (localIMusicPlaybackService == null)
      return;
    while (true)
    {
      int i;
      int j;
      try
      {
        i = localIMusicPlaybackService.getShuffleMode();
        j = 0;
        if (i == 0)
        {
          j = 1;
          if (localIMusicPlaybackService.getRepeatMode() == 1)
          {
            localIMusicPlaybackService.setRepeatMode(2);
            setRepeatButtonImage(2);
          }
          localIMusicPlaybackService.setShuffleMode(j);
          setShuffleButtonImage(j);
          return;
        }
      }
      catch (RemoteException localRemoteException)
      {
        String str1 = localRemoteException.getMessage();
        int k = Log.w("PlaybackControls", str1, localRemoteException);
        return;
      }
      if (i == 1)
      {
        j = 0;
      }
      else
      {
        String str2 = "Invalid shuffle mode: " + i;
        int m = Log.e("MediaPlaybackActivity", str2);
      }
    }
  }

  public void onClick(View paramView)
  {
    IMusicPlaybackService localIMusicPlaybackService = MusicUtils.sService;
    ImageView localImageView1 = this.mShuffleButton;
    if (paramView == localImageView1)
    {
      toggleShuffle();
      return;
    }
    ImageView localImageView2 = this.mRepeatButton;
    if (paramView == localImageView2)
    {
      cycleRepeat();
      return;
    }
    PlayPauseButton localPlayPauseButton = this.mPauseButton;
    if (paramView == localPlayPauseButton)
    {
      doPauseResume();
      return;
    }
    RepeatingImageButton localRepeatingImageButton1 = this.mPrevButton;
    if (paramView == localRepeatingImageButton1)
    {
      if (localIMusicPlaybackService == null)
        return;
      try
      {
        localIMusicPlaybackService.prev();
        return;
      }
      catch (RemoteException localRemoteException1)
      {
        String str1 = localRemoteException1.getMessage();
        int i = Log.e("PlaybackControls", str1, localRemoteException1);
        return;
      }
    }
    RepeatingImageButton localRepeatingImageButton2 = this.mNextButton;
    if (paramView == localRepeatingImageButton2)
    {
      if (localIMusicPlaybackService == null)
        return;
      try
      {
        localIMusicPlaybackService.next();
        return;
      }
      catch (RemoteException localRemoteException2)
      {
        String str2 = localRemoteException2.getMessage();
        int j = Log.e("PlaybackControls", str2, localRemoteException2);
        return;
      }
    }
    String str3 = "Unknown view clicked on: " + paramView;
    throw new RuntimeException(str3);
  }

  public void refreshButtonImages()
  {
    PlaybackState localPlaybackState = MusicUtils.getPlaybackState();
    setPauseButtonImage(localPlaybackState);
    setRepeatButtonImage(localPlaybackState);
    setShuffleButtonImage(localPlaybackState);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.PlaybackControls
 * JD-Core Version:    0.6.2
 */