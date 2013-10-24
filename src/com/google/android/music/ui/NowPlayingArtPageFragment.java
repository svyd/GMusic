package com.google.android.music.ui;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import com.google.android.music.AsyncAlbumArtImageView;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.playback.PlaybackState;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.widgets.ExpandingScrollView;
import com.google.android.music.widgets.ExpandingScrollView.ExpandingState;

public class NowPlayingArtPageFragment extends BaseFragment
{
  private AsyncAlbumArtImageView mAlbum;
  private long mAlbumId;
  private String mAlbumName;
  private String mArtUrl;
  private String mArtistName;
  private ObjectAnimator mCurrentAnimation;
  private boolean mIsCurrentlyActive;
  private BroadcastReceiver mNowPlayingDrawerListener;
  private Animator.AnimatorListener mPanLeftTopListener;
  private Animator.AnimatorListener mPanRightBottomListener;
  private boolean mPanningLeftOrTop = false;
  private int mQueuePosition = -1;
  private View mRootView;
  private Runnable mStartPanningRunnable;
  private boolean mStarted;
  private BroadcastReceiver mStatusListener;

  public NowPlayingArtPageFragment()
  {
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        boolean bool1 = false;
        NowPlayingArtPageFragment.this.stopPanning();
        PlaybackState localPlaybackState = MusicUtils.getPlaybackState();
        if (localPlaybackState != null);
        for (boolean bool2 = localPlaybackState.isPlaying(); ; bool2 = false)
        {
          if (localPlaybackState != null)
            bool1 = localPlaybackState.isPreparing();
          if (!NowPlayingArtPageFragment.this.mIsCurrentlyActive)
            return;
          if (!NowPlayingArtPageFragment.this.isDrawerExpanded())
            return;
          if (!bool2)
            return;
          if (bool1)
            return;
          if (!NowPlayingArtPageFragment.this.mPanningLeftOrTop)
            break;
          NowPlayingArtPageFragment.this.panLeftOrTop();
          return;
        }
        NowPlayingArtPageFragment.this.panRightOrBottom();
      }
    };
    this.mStartPanningRunnable = local1;
    BroadcastReceiver local2 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        long l1 = paramAnonymousIntent.getLongExtra("ListPosition", 65535L);
        NowPlayingArtPageFragment localNowPlayingArtPageFragment = NowPlayingArtPageFragment.this;
        if (NowPlayingArtPageFragment.this.mQueuePosition != -1)
        {
          long l2 = NowPlayingArtPageFragment.this.mQueuePosition;
          if (l1 != l2);
        }
        for (boolean bool1 = true; ; bool1 = false)
        {
          boolean bool2 = NowPlayingArtPageFragment.access$102(localNowPlayingArtPageFragment, bool1);
          String str = paramAnonymousIntent.getAction();
          if ("com.android.music.metachanged".equals(str))
            NowPlayingArtPageFragment.this.updateArtVisibility();
          NowPlayingArtPageFragment.this.startPanningIfNeeded();
          return;
        }
      }
    };
    this.mStatusListener = local2;
    BroadcastReceiver local3 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        String str = paramAnonymousIntent.getAction();
        if (!"com.google.android.music.nowplaying.DRAWER_STATE_CHANGED_ACTION".equals(str))
          return;
        NowPlayingArtPageFragment.this.updateArtVisibility();
        NowPlayingArtPageFragment.this.startPanningIfNeeded();
      }
    };
    this.mNowPlayingDrawerListener = local3;
  }

  private void clearArtwork()
  {
    this.mAlbum.clearArtwork();
    stopPanning();
  }

  private void doPan(float paramFloat1, float paramFloat2, float paramFloat3, String paramString, Animator.AnimatorListener paramAnimatorListener)
  {
    float f = Math.abs(paramFloat2 - paramFloat1) / paramFloat3;
    long l = ()(30000.0F * f);
    AsyncAlbumArtImageView localAsyncAlbumArtImageView = this.mAlbum;
    float[] arrayOfFloat = new float[2];
    arrayOfFloat[0] = paramFloat1;
    arrayOfFloat[1] = paramFloat2;
    ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(localAsyncAlbumArtImageView, paramString, arrayOfFloat);
    this.mCurrentAnimation = localObjectAnimator1;
    ObjectAnimator localObjectAnimator2 = this.mCurrentAnimation.setDuration(l);
    ObjectAnimator localObjectAnimator3 = this.mCurrentAnimation;
    LinearInterpolator localLinearInterpolator = new LinearInterpolator();
    localObjectAnimator3.setInterpolator(localLinearInterpolator);
    this.mCurrentAnimation.addListener(paramAnimatorListener);
    this.mCurrentAnimation.start();
  }

  private View findViewById(int paramInt)
  {
    return this.mRootView.findViewById(paramInt);
  }

  private void initializeView()
  {
    AsyncAlbumArtImageView localAsyncAlbumArtImageView = (AsyncAlbumArtImageView)findViewById(2131296401);
    this.mAlbum = localAsyncAlbumArtImageView;
  }

  private boolean isDrawerExpanded()
  {
    ExpandingScrollView localExpandingScrollView = getBottomDrawer();
    if (localExpandingScrollView != null)
    {
      ExpandingScrollView.ExpandingState localExpandingState1 = localExpandingScrollView.getExpandingState();
      ExpandingScrollView.ExpandingState localExpandingState2 = ExpandingScrollView.ExpandingState.FULLY_EXPANDED;
      if (localExpandingState1 != localExpandingState2);
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static NowPlayingArtPageFragment newInstance(int paramInt, String paramString1, String paramString2, long paramLong, String paramString3)
  {
    NowPlayingArtPageFragment localNowPlayingArtPageFragment = new NowPlayingArtPageFragment();
    Bundle localBundle = new Bundle();
    localBundle.putInt("position", paramInt);
    localBundle.putString("albumName", paramString1);
    localBundle.putString("artistName", paramString2);
    localBundle.putLong("albumId", paramLong);
    localBundle.putString("artUrl", paramString3);
    localNowPlayingArtPageFragment.setArguments(localBundle);
    return localNowPlayingArtPageFragment;
  }

  private void panLeftOrTop()
  {
    FragmentActivity localFragmentActivity = getActivity();
    if (localFragmentActivity == null)
      return;
    float f1;
    float f2;
    String str;
    if (MusicUtils.isLandscape(localFragmentActivity))
    {
      f1 = -this.mAlbum.getTop();
      f2 = this.mAlbum.getY() + f1;
      str = "translationY";
    }
    for (float f3 = Math.abs(this.mAlbum.getTop()) * 2; ; f3 = Math.abs(this.mAlbum.getLeft()) * 2)
    {
      Animator.AnimatorListener localAnimatorListener = this.mPanLeftTopListener;
      doPan(f2, f1, f3, str, localAnimatorListener);
      return;
      f1 = -this.mAlbum.getLeft();
      f2 = this.mAlbum.getX() + f1;
      str = "translationX";
    }
  }

  private void panRightOrBottom()
  {
    FragmentActivity localFragmentActivity = getActivity();
    if (localFragmentActivity == null)
      return;
    float f1;
    float f2;
    String str;
    if (MusicUtils.isLandscape(localFragmentActivity))
    {
      f1 = this.mAlbum.getTop();
      f2 = this.mAlbum.getY() - f1;
      str = "translationY";
    }
    for (float f3 = Math.abs(this.mAlbum.getTop()) * 2; ; f3 = Math.abs(this.mAlbum.getLeft()) * 2)
    {
      Animator.AnimatorListener localAnimatorListener = this.mPanRightBottomListener;
      doPan(f2, f1, f3, str, localAnimatorListener);
      return;
      f1 = this.mAlbum.getLeft();
      f2 = this.mAlbum.getX() - f1;
      str = "translationX";
    }
  }

  private void startPanningIfNeeded()
  {
    startPanningIfNeeded(100L);
  }

  private void startPanningIfNeeded(long paramLong)
  {
    if (!MusicPreferences.isHoneycombOrGreater())
      return;
    if (this.mPanLeftTopListener == null)
    {
      Animator.AnimatorListener local4 = new Animator.AnimatorListener()
      {
        public void onAnimationCancel(Animator paramAnonymousAnimator)
        {
        }

        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          boolean bool = NowPlayingArtPageFragment.access$302(NowPlayingArtPageFragment.this, false);
          NowPlayingArtPageFragment.this.startPanningIfNeeded(1000L);
        }

        public void onAnimationRepeat(Animator paramAnonymousAnimator)
        {
        }

        public void onAnimationStart(Animator paramAnonymousAnimator)
        {
        }
      };
      this.mPanLeftTopListener = local4;
      Animator.AnimatorListener local5 = new Animator.AnimatorListener()
      {
        public void onAnimationCancel(Animator paramAnonymousAnimator)
        {
        }

        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          boolean bool = NowPlayingArtPageFragment.access$302(NowPlayingArtPageFragment.this, true);
          NowPlayingArtPageFragment.this.startPanningIfNeeded(1000L);
        }

        public void onAnimationRepeat(Animator paramAnonymousAnimator)
        {
        }

        public void onAnimationStart(Animator paramAnonymousAnimator)
        {
        }
      };
      this.mPanRightBottomListener = local5;
    }
    AsyncAlbumArtImageView localAsyncAlbumArtImageView1 = this.mAlbum;
    Runnable localRunnable1 = this.mStartPanningRunnable;
    boolean bool1 = localAsyncAlbumArtImageView1.removeCallbacks(localRunnable1);
    AsyncAlbumArtImageView localAsyncAlbumArtImageView2 = this.mAlbum;
    Runnable localRunnable2 = this.mStartPanningRunnable;
    boolean bool2 = localAsyncAlbumArtImageView2.postDelayed(localRunnable2, paramLong);
  }

  private void stopPanning()
  {
    if (this.mCurrentAnimation == null)
      return;
    this.mCurrentAnimation.removeAllListeners();
    this.mCurrentAnimation.cancel();
    this.mCurrentAnimation = null;
  }

  private void updateArtVisibility()
  {
    if ((this.mStarted) && ((this.mIsCurrentlyActive) || (isDrawerExpanded())))
    {
      if (!TextUtils.isEmpty(this.mArtUrl))
      {
        AsyncAlbumArtImageView localAsyncAlbumArtImageView1 = this.mAlbum;
        String str = this.mArtUrl;
        localAsyncAlbumArtImageView1.setExternalAlbumArt(str);
        return;
      }
      AsyncAlbumArtImageView localAsyncAlbumArtImageView2 = this.mAlbum;
      long l = this.mAlbumId;
      localAsyncAlbumArtImageView2.setAlbumId(l, null, null);
      return;
    }
    clearArtwork();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView1 = super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    View localView2 = paramLayoutInflater.inflate(2130968642, paramViewGroup, false);
    this.mRootView = localView2;
    IntentFilter localIntentFilter1 = new IntentFilter();
    localIntentFilter1.addAction("com.android.music.playstatechanged");
    localIntentFilter1.addAction("com.android.music.metachanged");
    localIntentFilter1.addAction("com.android.music.asyncopencomplete");
    localIntentFilter1.addAction("com.android.music.asyncopenstart");
    localIntentFilter1.addAction("com.android.music.playbackfailed");
    FragmentActivity localFragmentActivity = getActivity();
    BroadcastReceiver localBroadcastReceiver1 = this.mStatusListener;
    Intent localIntent = localFragmentActivity.registerReceiver(localBroadcastReceiver1, localIntentFilter1);
    IntentFilter localIntentFilter2 = new IntentFilter();
    localIntentFilter2.addAction("com.google.android.music.nowplaying.DRAWER_STATE_CHANGED_ACTION");
    LocalBroadcastManager localLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
    BroadcastReceiver localBroadcastReceiver2 = this.mNowPlayingDrawerListener;
    localLocalBroadcastManager.registerReceiver(localBroadcastReceiver2, localIntentFilter2);
    Bundle localBundle = getArguments();
    int i = localBundle.getInt("position");
    this.mQueuePosition = i;
    String str1 = localBundle.getString("albumName");
    this.mAlbumName = str1;
    String str2 = localBundle.getString("artistName");
    this.mArtistName = str2;
    long l = localBundle.getLong("albumId");
    this.mAlbumId = l;
    String str3 = localBundle.getString("artUrl");
    this.mArtUrl = str3;
    initializeView();
    return this.mRootView;
  }

  public void onDestroyView()
  {
    FragmentActivity localFragmentActivity = getActivity();
    BroadcastReceiver localBroadcastReceiver1 = this.mStatusListener;
    localFragmentActivity.unregisterReceiver(localBroadcastReceiver1);
    LocalBroadcastManager localLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
    BroadcastReceiver localBroadcastReceiver2 = this.mNowPlayingDrawerListener;
    localLocalBroadcastManager.unregisterReceiver(localBroadcastReceiver2);
    super.onDestroyView();
  }

  public void onPause()
  {
    super.onPause();
    if (!this.mIsCurrentlyActive)
      clearArtwork();
    stopPanning();
  }

  public void onResume()
  {
    super.onResume();
    try
    {
      if (MusicUtils.sService == null)
        return;
      int i = MusicUtils.sService.getQueuePosition();
      int j = this.mQueuePosition;
      if (i != j)
        return;
      this.mIsCurrentlyActive = true;
      updateArtVisibility();
      startPanningIfNeeded();
      return;
    }
    catch (RemoteException localRemoteException)
    {
      String str = localRemoteException.getMessage();
      int k = Log.w("NowPlayingArtPageFragment", str, localRemoteException);
    }
  }

  public void onStart()
  {
    super.onStart();
    this.mStarted = true;
    updateArtVisibility();
  }

  public void onStop()
  {
    super.onStop();
    this.mStarted = false;
    updateArtVisibility();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.NowPlayingArtPageFragment
 * JD-Core Version:    0.6.2
 */