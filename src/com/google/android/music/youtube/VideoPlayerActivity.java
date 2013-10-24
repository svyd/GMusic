package com.google.android.music.youtube;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Toast;
import com.google.android.music.log.Log;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.ui.UIStateManager;
import com.google.android.music.utils.MusicUtils;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoPlayerActivity extends YouTubeBaseActivity
  implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlayerStateChangeListener
{
  private String mVideoId;
  private YouTubePlayerView mYouTubePlayerView;

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt1 != 1)
      return;
    this.mYouTubePlayerView.initialize("AIzaSyCQ8d_gKWCBARS-s47D9rCG9QtnNxxSz-I", this);
  }

  public void onAdStarted()
  {
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968711);
    YouTubePlayerView localYouTubePlayerView = (YouTubePlayerView)findViewById(2131296570);
    this.mYouTubePlayerView = localYouTubePlayerView;
    this.mYouTubePlayerView.initialize("AIzaSyCQ8d_gKWCBARS-s47D9rCG9QtnNxxSz-I", this);
    String str = getIntent().getStringExtra("videoId");
    this.mVideoId = str;
    if (this.mVideoId != null)
      return;
    throw new RuntimeException("Invalid video ID in intent.");
  }

  public void onError(YouTubePlayer.ErrorReason paramErrorReason)
  {
  }

  public void onInitializationFailure(YouTubePlayer.Provider paramProvider, YouTubeInitializationResult paramYouTubeInitializationResult)
  {
    if (paramYouTubeInitializationResult.isUserRecoverableError())
    {
      paramYouTubeInitializationResult.getErrorDialog(this, 1).show();
      return;
    }
    String str1 = getString(2131230740);
    Object[] arrayOfObject = new Object[1];
    String str2 = paramYouTubeInitializationResult.toString();
    arrayOfObject[0] = str2;
    String str3 = String.format(str1, arrayOfObject);
    Toast.makeText(this, str3, 1).show();
  }

  public void onInitializationSuccess(YouTubePlayer.Provider paramProvider, YouTubePlayer paramYouTubePlayer, boolean paramBoolean)
  {
    paramYouTubePlayer.addFullscreenControlFlag(8);
    paramYouTubePlayer.setShowFullscreenButton(false);
    paramYouTubePlayer.setPlayerStateChangeListener(this);
    IMusicPlaybackService localIMusicPlaybackService = MusicUtils.sService;
    if (localIMusicPlaybackService != null);
    while (true)
    {
      try
      {
        localIMusicPlaybackService.pause();
        if (paramBoolean)
          return;
        String str = this.mVideoId;
        paramYouTubePlayer.loadVideo(str);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.w("VideoPlayerActivity", "Failed to pause the service instance");
        continue;
      }
      Log.w("VideoPlayerActivity", "Failed to get service instance");
    }
  }

  public void onLoaded(String paramString)
  {
  }

  public void onLoading()
  {
  }

  protected void onPause()
  {
    super.onPause();
    UIStateManager.getInstance().onPause();
  }

  protected void onResume()
  {
    super.onResume();
    UIStateManager.getInstance().onResume();
  }

  public void onVideoEnded()
  {
    finish();
  }

  public void onVideoStarted()
  {
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.youtube.VideoPlayerActivity
 * JD-Core Version:    0.6.2
 */