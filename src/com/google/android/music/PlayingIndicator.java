package com.google.android.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.google.android.music.playback.PlaybackState;
import com.google.android.music.utils.MusicUtils;

public class PlayingIndicator extends LinearLayout
{
  private AnimationDrawable mAnimation1;
  private AnimationDrawable mAnimation2;
  private AnimationDrawable mAnimation3;
  private boolean mAttachedToWindow;
  private Context mContext;
  private int mCurrentState = 0;
  private ImageView mImage1;
  private ImageView mImage2;
  private ImageView mImage3;
  private BroadcastReceiver mNowPlayingReceiver;
  private boolean mReceiverRegistered = false;

  public PlayingIndicator(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        boolean bool1 = paramAnonymousIntent.getBooleanExtra("playing", false);
        boolean bool2 = paramAnonymousIntent.getBooleanExtra("streaming", false);
        boolean bool3 = paramAnonymousIntent.getBooleanExtra("preparing", false);
        PlayingIndicator.this.updatePlayState(bool1, bool2, bool3);
      }
    };
    this.mNowPlayingReceiver = local1;
    this.mContext = paramContext;
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -1);
    DisplayMetrics localDisplayMetrics = paramContext.getResources().getDisplayMetrics();
    int i = (int)TypedValue.applyDimension(1, 1.0F, localDisplayMetrics);
    localLayoutParams.rightMargin = i;
    ImageView localImageView1 = new ImageView(paramContext);
    this.mImage1 = localImageView1;
    this.mImage1.setLayoutParams(localLayoutParams);
    ImageView localImageView2 = this.mImage1;
    addView(localImageView2);
    ImageView localImageView3 = new ImageView(paramContext);
    this.mImage2 = localImageView3;
    this.mImage2.setLayoutParams(localLayoutParams);
    ImageView localImageView4 = this.mImage2;
    addView(localImageView4);
    ImageView localImageView5 = new ImageView(paramContext);
    this.mImage3 = localImageView5;
    this.mImage3.setLayoutParams(localLayoutParams);
    ImageView localImageView6 = this.mImage3;
    addView(localImageView6);
    AnimationDrawable localAnimationDrawable1 = (AnimationDrawable)paramContext.getResources().getDrawable(2131034124);
    this.mAnimation1 = localAnimationDrawable1;
    AnimationDrawable localAnimationDrawable2 = (AnimationDrawable)paramContext.getResources().getDrawable(2131034125);
    this.mAnimation2 = localAnimationDrawable2;
    AnimationDrawable localAnimationDrawable3 = (AnimationDrawable)paramContext.getResources().getDrawable(2131034126);
    this.mAnimation3 = localAnimationDrawable3;
  }

  private void registerReceiver()
  {
    if (!this.mAttachedToWindow)
      return;
    if (this.mReceiverRegistered)
      return;
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.android.music.playstatechanged");
    localIntentFilter.addAction("com.android.music.asyncopencomplete");
    localIntentFilter.addAction("com.android.music.asyncopenstart");
    localIntentFilter.addAction("com.android.music.playbackfailed");
    Context localContext = this.mContext;
    BroadcastReceiver localBroadcastReceiver = this.mNowPlayingReceiver;
    Intent localIntent = localContext.registerReceiver(localBroadcastReceiver, localIntentFilter);
    this.mReceiverRegistered = true;
  }

  private void setPlayState(int paramInt)
  {
    if (this.mCurrentState != paramInt)
      return;
    this.mCurrentState = paramInt;
    switch (paramInt)
    {
    default:
      String str = "setPlayState: unexpected state " + paramInt;
      int i = Log.wtf("PlayingIndicator", str);
      return;
    case 0:
      unregisterReceiver();
      stopAnimation();
      return;
    case 1:
      registerReceiver();
      stopAnimation();
      return;
    case 2:
    }
    registerReceiver();
    startAnimation();
  }

  private void startAnimation()
  {
    ImageView localImageView1 = this.mImage1;
    AnimationDrawable localAnimationDrawable1 = this.mAnimation1;
    localImageView1.setImageDrawable(localAnimationDrawable1);
    ImageView localImageView2 = this.mImage2;
    AnimationDrawable localAnimationDrawable2 = this.mAnimation2;
    localImageView2.setImageDrawable(localAnimationDrawable2);
    ImageView localImageView3 = this.mImage3;
    AnimationDrawable localAnimationDrawable3 = this.mAnimation3;
    localImageView3.setImageDrawable(localAnimationDrawable3);
    this.mAnimation1.stop();
    this.mAnimation2.stop();
    this.mAnimation3.stop();
    this.mAnimation1.start();
    this.mAnimation2.start();
    this.mAnimation3.start();
  }

  private void stopAnimation()
  {
    this.mAnimation1.stop();
    this.mAnimation2.stop();
    this.mAnimation3.stop();
    this.mImage1.setImageResource(2130837785);
    this.mImage2.setImageResource(2130837785);
    this.mImage3.setImageResource(2130837785);
  }

  private void unregisterReceiver()
  {
    if (!this.mReceiverRegistered)
      return;
    Context localContext = this.mContext;
    BroadcastReceiver localBroadcastReceiver = this.mNowPlayingReceiver;
    localContext.unregisterReceiver(localBroadcastReceiver);
    this.mReceiverRegistered = false;
  }

  private void updatePlayState(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    if ((paramBoolean3) && (paramBoolean2))
    {
      setPlayState(1);
      return;
    }
    if (paramBoolean1)
    {
      setPlayState(2);
      return;
    }
    setPlayState(1);
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mAttachedToWindow = true;
    if (this.mCurrentState == 0)
      return;
    registerReceiver();
    if (this.mCurrentState == 1)
    {
      stopAnimation();
      return;
    }
    if (this.mCurrentState != 2)
      return;
    startAnimation();
  }

  protected void onDetachedFromWindow()
  {
    unregisterReceiver();
    this.mAttachedToWindow = false;
    stopAnimation();
    super.onDetachedFromWindow();
  }

  public void setVisibility(int paramInt)
  {
    super.setVisibility(paramInt);
    if (paramInt == 0)
    {
      PlaybackState localPlaybackState = MusicUtils.getPlaybackState();
      if (localPlaybackState == null)
        return;
      boolean bool1 = localPlaybackState.isPlaying();
      boolean bool2 = localPlaybackState.isStreaming();
      boolean bool3 = localPlaybackState.isPreparing();
      updatePlayState(bool1, bool2, bool3);
      return;
    }
    if (paramInt != 8)
      return;
    setPlayState(0);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.PlayingIndicator
 * JD-Core Version:    0.6.2
 */