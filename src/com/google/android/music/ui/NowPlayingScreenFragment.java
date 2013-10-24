package com.google.android.music.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.MediaRouteButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import com.google.android.music.BufferProgressListener;
import com.google.android.music.FadingColorDrawable;
import com.google.android.music.PlayPauseButton;
import com.google.android.music.PlaybackControls;
import com.google.android.music.RatingSelector;
import com.google.android.music.RepeatingImageButton;
import com.google.android.music.SizableTrackSeekBar;
import com.google.android.music.StreamingBufferProgressListener;
import com.google.android.music.animator.Animator;
import com.google.android.music.animator.AnimatorListener;
import com.google.android.music.animator.PropertyAnimator;
import com.google.android.music.animator.StatefulAlphaAnimation;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.medialist.SharedSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.mix.MixDescriptor;
import com.google.android.music.mix.MixGenerationState;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.playback.IMusicPlaybackService.Stub;
import com.google.android.music.playback.MusicPlaybackService;
import com.google.android.music.playback.PlaybackState;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.cardlib.PlayCardMenuHandler;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.MusicUtils.ServiceToken;
import com.google.android.music.utils.ViewUtils;
import com.google.android.music.widgets.ExpandingScrollView;
import com.google.android.music.widgets.ExpandingScrollView.ExpandingScrollViewListener;
import com.google.android.music.widgets.ExpandingScrollView.ExpandingState;
import com.google.android.music.widgets.LinkableViewPager;
import com.google.common.collect.ImmutableSet;
import java.util.Iterator;
import java.util.Set;

public class NowPlayingScreenFragment extends BaseFragment
  implements View.OnTouchListener, ExpandingScrollView.ExpandingScrollViewListener, View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>, MusicFragment
{
  private static final boolean DEBUG_QUEUE = DebugUtils.isLoggable(DebugUtils.MusicTag.UI);
  private LinkableViewPager mArtPager;
  private PagerAdapter mArtPagerAdapter;
  private ViewGroup mControlsParent;
  private ContentIdentifier mCurrentAudioId;
  private LightsState mCurrentLightsState;
  private MixDescriptor mCurrentMix;
  private ExpandingScrollView.ExpandingState mCurrentState;
  private TextView mCurrentTime;
  private ColorStateList mCurrentTimeColor;
  private long mDuration;
  private ColorStateList mFlashWhenPausedColor;
  private ViewGroup mFooterButtonsWrapper;
  private final Handler mHandler;
  private View mHeaderButtonsWrapper;
  private LinkableViewPager mHeaderPager;
  private PagerAdapter mHeaderPagerAdapter;
  private PlayPauseButton mHeaderPauseButton;
  private boolean mIsRestrictedPlaybackMode;
  private boolean mIsStarted;
  private boolean mIsTablet;
  private Set<View> mLightUpOnlyViewsAll;
  private Set<View> mLightUpOnlyViewsLimited;
  private View mLightsUpInterceptor;
  private boolean mLockNowPlayingScreen = false;
  private MediaRouteManager mMediaRouteManager;
  private View[] mMovableControls;
  private RepeatingImageButton mNextButton;
  private ImageButton mOverflowMenu;
  private PlayPauseButton mPauseButton;
  private PlaybackControls mPlaybackControls;
  private ServiceConnection mPlaybackServiceConnection;
  private MusicUtils.ServiceToken mPlaybackServiceToken;
  private ViewGroup mPlayingFromHeaderContainer;
  private View mPlayingFromHeaderRefreshButton;
  private ViewGroup mPlayingFromHeaderView;
  private long mPosOverride = 65535L;
  private RepeatingImageButton mPrevButton;
  private final AnimatorListener mProgresAnimationListener;
  private SizableTrackSeekBar mProgress;
  private BaseTrackListView mQueue;
  private QueueTrackListAdapter mQueueAdapter;
  private Cursor mQueueCursor;
  private Animation mQueueFadeInAnimation;
  private Animation mQueueFadeOutAnimation;
  private boolean mQueueShown = false;
  private final Animation.AnimationListener mQueueSlideDownAnimationListener;
  private final Animation.AnimationListener mQueueSlideUpAnimationListener;
  private ImageButton mQueueSwitcher;
  private View mQueueWrapper;
  private RatingSelector mRatings;
  private final View.OnClickListener mRefreshButtonListener;
  private long mRefreshDelayMs;
  private ImageView mRepeatButton;
  private View mRootView;
  private SeekBar.OnSeekBarChangeListener mSeekListener;
  private IMusicPlaybackService mService = null;
  private ImageView mShuffleButton;
  private BufferProgressListener mSongBufferListener;
  private SongList mSongList;
  private BroadcastReceiver mStatusListener;
  private NowPlayingTabletHeaderView mTabletHeaderView;
  private PropertyAnimator mThumbAlphaAnimation;
  private TextView mTotalTime;
  private BroadcastReceiver mUIInteractionListener;

  public NowPlayingScreenFragment()
  {
    LightsState localLightsState = LightsState.OFF;
    this.mCurrentLightsState = localLightsState;
    this.mThumbAlphaAnimation = null;
    Animation.AnimationListener local1 = new Animation.AnimationListener()
    {
      public void onAnimationEnd(Animation paramAnonymousAnimation)
      {
      }

      public void onAnimationRepeat(Animation paramAnonymousAnimation)
      {
      }

      public void onAnimationStart(Animation paramAnonymousAnimation)
      {
      }
    };
    this.mQueueSlideDownAnimationListener = local1;
    Animation.AnimationListener local2 = new Animation.AnimationListener()
    {
      public void onAnimationEnd(Animation paramAnonymousAnimation)
      {
        NowPlayingScreenFragment.this.mQueueWrapper.setVisibility(4);
      }

      public void onAnimationRepeat(Animation paramAnonymousAnimation)
      {
      }

      public void onAnimationStart(Animation paramAnonymousAnimation)
      {
      }
    };
    this.mQueueSlideUpAnimationListener = local2;
    View.OnClickListener local3 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (NowPlayingScreenFragment.this.mLockNowPlayingScreen)
          return;
        NowPlayingScreenFragment.this.mPlayingFromHeaderRefreshButton.setEnabled(false);
        boolean bool = NowPlayingScreenFragment.access$102(NowPlayingScreenFragment.this, true);
        NowPlayingScreenFragment.this.mQueueAdapter.setEnabled(false);
        Animation localAnimation = AnimationUtils.loadAnimation(NowPlayingScreenFragment.this.getActivity(), 2131034131);
        localAnimation.setRepeatCount(-1);
        NowPlayingScreenFragment.this.mPlayingFromHeaderRefreshButton.startAnimation(localAnimation);
        try
        {
          MusicUtils.sService.refreshRadio();
          return;
        }
        catch (RemoteException localRemoteException)
        {
          int i = Log.e("NowPlayingFragment", "Unable to get playback service, refreshing station failed.");
          NowPlayingScreenFragment.this.unlockNowPlayingScreen();
        }
      }
    };
    this.mRefreshButtonListener = local3;
    BroadcastReceiver local4 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        String str = paramAnonymousIntent.getAction();
        ExpandingScrollView localExpandingScrollView;
        if ("com.google.android.music.nowplaying.HEADER_CLICKED".equals(str))
        {
          localExpandingScrollView = NowPlayingScreenFragment.this.getBottomDrawer();
          if (localExpandingScrollView == null)
            return;
          ExpandingScrollView.ExpandingState localExpandingState1 = localExpandingScrollView.getExpandingState();
          ExpandingScrollView.ExpandingState localExpandingState2 = ExpandingScrollView.ExpandingState.FULLY_EXPANDED;
          if (localExpandingState1 == localExpandingState2)
          {
            ExpandingScrollView.ExpandingState localExpandingState3 = ExpandingScrollView.ExpandingState.COLLAPSED;
            localExpandingScrollView.moveToExpandingState(localExpandingState3);
            return;
          }
          ExpandingScrollView.ExpandingState localExpandingState4 = ExpandingScrollView.ExpandingState.FULLY_EXPANDED;
          localExpandingScrollView.moveToExpandingState(localExpandingState4);
          return;
        }
        if ("com.google.android.music.nowplaying.HEADER_ART_CLICKED".equals(str))
        {
          ExpandingScrollView.ExpandingState localExpandingState5 = NowPlayingScreenFragment.this.mCurrentState;
          ExpandingScrollView.ExpandingState localExpandingState6 = ExpandingScrollView.ExpandingState.FULLY_EXPANDED;
          if (localExpandingState5 == localExpandingState6)
          {
            NowPlayingScreenFragment localNowPlayingScreenFragment = NowPlayingScreenFragment.this;
            if (!NowPlayingScreenFragment.this.mQueueShown);
            for (boolean bool1 = true; ; bool1 = false)
            {
              localNowPlayingScreenFragment.showQueue(bool1, true);
              return;
            }
          }
          localExpandingScrollView = NowPlayingScreenFragment.this.getBottomDrawer();
          if (localExpandingScrollView == null)
            return;
          ExpandingScrollView.ExpandingState localExpandingState7 = ExpandingScrollView.ExpandingState.FULLY_EXPANDED;
          localExpandingScrollView.moveToExpandingState(localExpandingState7);
          return;
        }
        if (!"com.google.android.music.OPEN_DRAWER".equals(str))
          return;
        if (!NowPlayingScreenFragment.this.mIsStarted)
          return;
        Handler localHandler = NowPlayingScreenFragment.this.mHandler;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            ExpandingScrollView localExpandingScrollView = NowPlayingScreenFragment.this.getBottomDrawer();
            if (localExpandingScrollView == null)
              return;
            NowPlayingScreenFragment localNowPlayingScreenFragment = NowPlayingScreenFragment.this;
            ExpandingScrollView.ExpandingState localExpandingState1 = ExpandingScrollView.ExpandingState.FULLY_EXPANDED;
            localNowPlayingScreenFragment.setCurrentState(localExpandingState1);
            ExpandingScrollView.ExpandingState localExpandingState2 = ExpandingScrollView.ExpandingState.FULLY_EXPANDED;
            localExpandingScrollView.moveToExpandingState(localExpandingState2);
          }
        };
        boolean bool2 = localHandler.postDelayed(local1, 500L);
      }
    };
    this.mUIInteractionListener = local4;
    ExpandingScrollView.ExpandingState localExpandingState = ExpandingScrollView.ExpandingState.COLLAPSED;
    this.mCurrentState = localExpandingState;
    AnimatorListener local8 = new AnimatorListener()
    {
      private boolean mCancelled = false;

      private boolean isToVisible(Animator paramAnonymousAnimator)
      {
        if (((Integer)paramAnonymousAnimator.getValueTo()).intValue() == 255);
        for (boolean bool = true; ; bool = false)
          return bool;
      }

      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        if (this.mCancelled)
          return;
        if (isToVisible(paramAnonymousAnimator));
      }

      public void onAnimationRepeat(Animator paramAnonymousAnimator)
      {
      }

      public void onAnimationStart(Animator paramAnonymousAnimator)
      {
        this.mCancelled = false;
        if (!isToVisible(paramAnonymousAnimator))
          return;
        NowPlayingScreenFragment.this.mProgress.setEnabled(true);
      }
    };
    this.mProgresAnimationListener = local8;
    SeekBar.OnSeekBarChangeListener local10 = new SeekBar.OnSeekBarChangeListener()
    {
      private int mCurrentProgress;
      private boolean mFromTouch = false;

      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        if (paramAnonymousSeekBar.getAnimation() != null)
          ((View)paramAnonymousSeekBar.getParent()).postInvalidate();
        if (!paramAnonymousBoolean)
          return;
        this.mCurrentProgress = paramAnonymousInt;
        NowPlayingScreenFragment localNowPlayingScreenFragment = NowPlayingScreenFragment.this;
        long l1 = NowPlayingScreenFragment.this.mDuration;
        long l2 = this.mCurrentProgress;
        long l3 = l1 * l2 / 1000L;
        long l4 = NowPlayingScreenFragment.access$1702(localNowPlayingScreenFragment, l3);
        if (this.mFromTouch)
          return;
        if (!paramAnonymousBoolean)
          return;
        onStopTrackingTouch(paramAnonymousSeekBar);
      }

      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
        this.mFromTouch = true;
      }

      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
        if (NowPlayingScreenFragment.this.mService == null)
          return;
        try
        {
          IMusicPlaybackService localIMusicPlaybackService = NowPlayingScreenFragment.this.mService;
          long l1 = NowPlayingScreenFragment.this.mPosOverride;
          long l2 = localIMusicPlaybackService.seek(l1);
          this.mFromTouch = false;
          long l3 = NowPlayingScreenFragment.access$1702(NowPlayingScreenFragment.this, 65535L);
          return;
        }
        catch (RemoteException localRemoteException)
        {
          while (true)
          {
            String str = localRemoteException.getMessage();
            int i = Log.e("NowPlayingFragment", str, localRemoteException);
          }
        }
      }
    };
    this.mSeekListener = local10;
    ServiceConnection local11 = new ServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        if (NowPlayingScreenFragment.DEBUG_QUEUE)
          int i = Log.d("NowPlayingFragment", "Service connected");
        NowPlayingScreenFragment localNowPlayingScreenFragment = NowPlayingScreenFragment.this;
        IMusicPlaybackService localIMusicPlaybackService1 = IMusicPlaybackService.Stub.asInterface(paramAnonymousIBinder);
        IMusicPlaybackService localIMusicPlaybackService2 = NowPlayingScreenFragment.access$1902(localNowPlayingScreenFragment, localIMusicPlaybackService1);
        NowPlayingScreenFragment.this.mPlaybackControls.refreshButtonImages();
        NowPlayingScreenFragment.this.updateTrackInfo();
        long l = NowPlayingScreenFragment.this.refreshNow();
        NowPlayingScreenFragment.this.queueNextRefresh(l);
        NowPlayingScreenFragment.this.setupPlayQueue();
      }

      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
        IMusicPlaybackService localIMusicPlaybackService = NowPlayingScreenFragment.access$1902(NowPlayingScreenFragment.this, null);
      }
    };
    this.mPlaybackServiceConnection = local11;
    Handler local12 = new Handler()
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        switch (paramAnonymousMessage.what)
        {
        default:
          return;
        case 1:
          long l = NowPlayingScreenFragment.this.refreshNow();
          if (!NowPlayingScreenFragment.this.isResumed())
            return;
          NowPlayingScreenFragment.this.queueNextRefresh(l);
          return;
        case 5:
          NowPlayingScreenFragment localNowPlayingScreenFragment = NowPlayingScreenFragment.this;
          Intent localIntent = (Intent)paramAnonymousMessage.obj;
          localNowPlayingScreenFragment.updateTrackInfoImpl(localIntent);
          return;
        case 7:
        }
        NowPlayingScreenFragment.this.turnLightsOff();
      }
    };
    this.mHandler = local12;
    BroadcastReceiver local13 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        String str1 = paramAnonymousIntent.getAction();
        if (NowPlayingScreenFragment.DEBUG_QUEUE)
        {
          String str2 = "onReceive: action=" + str1;
          int i = Log.d("NowPlayingFragment", str2);
        }
        if (str1.equals("com.android.music.metachanged"))
        {
          NowPlayingScreenFragment.this.updateTrackInfo(paramAnonymousIntent);
          NowPlayingScreenFragment.this.mPlaybackControls.refreshButtonImages();
          NowPlayingScreenFragment.this.queueNextRefresh(1L);
          return;
        }
        if (str1.equals("com.android.music.playstatechanged"))
        {
          NowPlayingScreenFragment.this.mPlaybackControls.refreshButtonImages();
          NowPlayingScreenFragment.this.queueNextRefresh(1L);
          return;
        }
        if ((str1.equals("com.android.music.asyncopencomplete")) || (str1.equals("com.android.music.asyncopenstart")) || (str1.equals("com.android.music.playbackfailed")))
        {
          if (str1.equals("com.android.music.asyncopenstart"))
            NowPlayingScreenFragment.this.mHandler.removeMessages(1);
          while (true)
          {
            long l1 = NowPlayingScreenFragment.access$1702(NowPlayingScreenFragment.this, 65535L);
            NowPlayingScreenFragment.this.mPlaybackControls.refreshButtonImages();
            NowPlayingScreenFragment.this.updateTrackInfo(paramAnonymousIntent);
            return;
            if (paramAnonymousIntent.getBooleanExtra("playing", false))
              NowPlayingScreenFragment.this.queueNextRefresh(1L);
            else
              long l2 = NowPlayingScreenFragment.this.refreshNow();
          }
        }
        if (str1.equals("com.google.android.music.mix.playbackmodechanged"))
        {
          NowPlayingScreenFragment.this.updateButtonsVisibility();
          NowPlayingScreenFragment.this.updatePlayingFromHeader();
          return;
        }
        if (str1.equals("com.android.music.queuechanged"))
        {
          if (NowPlayingScreenFragment.this.mLockNowPlayingScreen)
            return;
          NowPlayingScreenFragment.this.setupPlayQueue();
          return;
        }
        if (str1.equals("com.google.android.music.refreshcomplete"))
        {
          NowPlayingScreenFragment.this.unlockNowPlayingScreen();
          NowPlayingScreenFragment.this.setupPlayQueue();
          return;
        }
        if (!str1.equals("com.google.android.music.refreshfailed"))
          return;
        NowPlayingScreenFragment.this.unlockNowPlayingScreen();
      }
    };
    this.mStatusListener = local13;
  }

  private void clearDisplay()
  {
    MusicUtils.assertUiThread();
    this.mSongBufferListener.updateCurrentSong(null, false);
    this.mProgress.setProgress(0);
    this.mRatings.setRating(0);
    this.mTotalTime.setText(null);
  }

  private View findViewById(int paramInt)
  {
    return this.mRootView.findViewById(paramInt);
  }

  private int getLightsAnimationDuration(boolean paramBoolean)
  {
    return 200;
  }

  private int getPageCount()
  {
    MusicUtils.assertUiThread();
    if (this.mQueueCursor != null);
    for (int i = this.mQueueCursor.getCount(); ; i = 0)
      return i;
  }

  private void handlePageSelected(final int paramInt)
  {
    if (this.mService == null)
      return;
    MusicUtils.runAsync(new Runnable()
    {
      public void run()
      {
        try
        {
          if (NowPlayingScreenFragment.this.mService != null)
          {
            int i = paramInt;
            int j = NowPlayingScreenFragment.this.mService.getQueuePosition();
            if (i != j)
            {
              IMusicPlaybackService localIMusicPlaybackService = NowPlayingScreenFragment.this.mService;
              int k = paramInt;
              localIMusicPlaybackService.setQueuePosition(k);
              return;
            }
          }
          if (!NowPlayingScreenFragment.DEBUG_QUEUE)
            return;
          int m = Log.d("NowPlayingFragment", "Setting to the same position. No-op.");
          return;
        }
        catch (RemoteException localRemoteException)
        {
          String str = localRemoteException.getMessage();
          int n = Log.w("NowPlayingFragment", str, localRemoteException);
        }
      }
    });
  }

  private void hideActionBar()
  {
    getActionBarController().hideActionBar();
  }

  private void initializeView()
  {
    Resources localResources = getResources();
    TextView localTextView1 = (TextView)findViewById(2131296446);
    this.mCurrentTime = localTextView1;
    TextView localTextView2 = (TextView)findViewById(2131296447);
    this.mTotalTime = localTextView2;
    SizableTrackSeekBar localSizableTrackSeekBar1 = (SizableTrackSeekBar)findViewById(16908301);
    this.mProgress = localSizableTrackSeekBar1;
    this.mProgress.setEnabled(true);
    long l = MusicUtils.getRefreshDelay(this.mProgress);
    this.mRefreshDelayMs = l;
    ImageButton localImageButton1 = (ImageButton)findViewById(2131296453);
    this.mQueueSwitcher = localImageButton1;
    ImageButton localImageButton2 = this.mQueueSwitcher;
    FragmentActivity localFragmentActivity1 = getActivity();
    ImageButton localImageButton3 = this.mQueueSwitcher;
    FadingColorDrawable localFadingColorDrawable1 = new FadingColorDrawable(localFragmentActivity1, localImageButton3);
    localImageButton2.setBackgroundDrawable(localFadingColorDrawable1);
    ImageButton localImageButton4 = (ImageButton)findViewById(2131296374);
    this.mOverflowMenu = localImageButton4;
    ImageButton localImageButton5 = this.mOverflowMenu;
    FragmentActivity localFragmentActivity2 = getActivity();
    ImageButton localImageButton6 = this.mOverflowMenu;
    FadingColorDrawable localFadingColorDrawable2 = new FadingColorDrawable(localFragmentActivity2, localImageButton6);
    localImageButton5.setBackgroundDrawable(localFadingColorDrawable2);
    PlayPauseButton localPlayPauseButton1 = (PlayPauseButton)findViewById(2131296454);
    this.mHeaderPauseButton = localPlayPauseButton1;
    if (this.mHeaderPauseButton != null)
    {
      PlayPauseButton localPlayPauseButton2 = this.mHeaderPauseButton;
      FragmentActivity localFragmentActivity3 = getActivity();
      PlayPauseButton localPlayPauseButton3 = this.mHeaderPauseButton;
      FadingColorDrawable localFadingColorDrawable3 = new FadingColorDrawable(localFragmentActivity3, localPlayPauseButton3);
      localPlayPauseButton2.setBackgroundDrawable(localFadingColorDrawable3);
      this.mHeaderPauseButton.setOnClickListener(this);
      this.mHeaderPauseButton.setVisibility(0);
    }
    RatingSelector localRatingSelector1 = (RatingSelector)findViewById(2131296448);
    this.mRatings = localRatingSelector1;
    RepeatingImageButton localRepeatingImageButton1 = (RepeatingImageButton)findViewById(2131296488);
    this.mPrevButton = localRepeatingImageButton1;
    PlayPauseButton localPlayPauseButton4 = (PlayPauseButton)findViewById(2131296487);
    this.mPauseButton = localPlayPauseButton4;
    boolean bool = this.mPauseButton.requestFocus();
    RepeatingImageButton localRepeatingImageButton2 = (RepeatingImageButton)findViewById(2131296489);
    this.mNextButton = localRepeatingImageButton2;
    ImageView localImageView1 = (ImageView)findViewById(2131296371);
    this.mShuffleButton = localImageView1;
    ImageView localImageView2 = (ImageView)findViewById(2131296486);
    this.mRepeatButton = localImageView2;
    FragmentActivity localFragmentActivity4 = getActivity();
    MusicPreferences localMusicPreferences = getPreferences();
    RepeatingImageButton localRepeatingImageButton3 = this.mPrevButton;
    PlayPauseButton localPlayPauseButton5 = this.mPauseButton;
    RepeatingImageButton localRepeatingImageButton4 = this.mNextButton;
    ImageView localImageView3 = this.mRepeatButton;
    ImageView localImageView4 = this.mShuffleButton;
    PlaybackControls localPlaybackControls = new PlaybackControls(localFragmentActivity4, localMusicPreferences, localRepeatingImageButton3, localPlayPauseButton5, localRepeatingImageButton4, localImageView3, localImageView4);
    this.mPlaybackControls = localPlaybackControls;
    View[] arrayOfView = new View[6];
    PlayPauseButton localPlayPauseButton6 = this.mPauseButton;
    arrayOfView[0] = localPlayPauseButton6;
    RepeatingImageButton localRepeatingImageButton5 = this.mNextButton;
    arrayOfView[1] = localRepeatingImageButton5;
    RepeatingImageButton localRepeatingImageButton6 = this.mPrevButton;
    arrayOfView[2] = localRepeatingImageButton6;
    RatingSelector localRatingSelector2 = this.mRatings;
    arrayOfView[3] = localRatingSelector2;
    ImageView localImageView5 = this.mShuffleButton;
    arrayOfView[4] = localImageView5;
    ImageView localImageView6 = this.mRepeatButton;
    arrayOfView[5] = localImageView6;
    this.mMovableControls = arrayOfView;
    SizableTrackSeekBar localSizableTrackSeekBar2 = this.mProgress;
    SeekBar.OnSeekBarChangeListener localOnSeekBarChangeListener = this.mSeekListener;
    localSizableTrackSeekBar2.setOnSeekBarChangeListener(localOnSeekBarChangeListener);
    this.mProgress.setMax(1000);
    View localView1 = findViewById(2131296451);
    this.mHeaderButtonsWrapper = localView1;
    ViewGroup localViewGroup1 = (ViewGroup)findViewById(2131296449);
    this.mFooterButtonsWrapper = localViewGroup1;
    LinkableViewPager localLinkableViewPager1 = (LinkableViewPager)findViewById(2131296445);
    this.mArtPager = localLinkableViewPager1;
    LinkableViewPager localLinkableViewPager2 = (LinkableViewPager)findViewById(2131296450);
    this.mHeaderPager = localLinkableViewPager2;
    if (this.mHeaderPager != null)
    {
      LinkableViewPager localLinkableViewPager3 = this.mArtPager;
      LinkableViewPager localLinkableViewPager4 = this.mHeaderPager;
      localLinkableViewPager3.link(localLinkableViewPager4);
    }
    NowPlayingTabletHeaderView localNowPlayingTabletHeaderView = (NowPlayingTabletHeaderView)findViewById(2131296456);
    this.mTabletHeaderView = localNowPlayingTabletHeaderView;
    BaseTrackListView localBaseTrackListView1 = (BaseTrackListView)findViewById(2131296458);
    this.mQueue = localBaseTrackListView1;
    View localView2 = findViewById(2131296457);
    this.mQueueWrapper = localView2;
    FragmentActivity localFragmentActivity5 = getActivity();
    FrameLayout localFrameLayout = new FrameLayout(localFragmentActivity5);
    this.mPlayingFromHeaderContainer = localFrameLayout;
    BaseTrackListView localBaseTrackListView2 = this.mQueue;
    ViewGroup localViewGroup2 = this.mPlayingFromHeaderContainer;
    localBaseTrackListView2.addHeaderView(localViewGroup2);
    this.mQueue.setHeaderDividersEnabled(false);
    SizableTrackSeekBar localSizableTrackSeekBar3 = this.mProgress;
    StreamingBufferProgressListener localStreamingBufferProgressListener = new StreamingBufferProgressListener(localSizableTrackSeekBar3);
    this.mSongBufferListener = localStreamingBufferProgressListener;
    ColorStateList localColorStateList1 = localResources.getColorStateList(2131427374);
    this.mCurrentTimeColor = localColorStateList1;
    ColorStateList localColorStateList2 = localResources.getColorStateList(2131427375);
    this.mFlashWhenPausedColor = localColorStateList2;
    View localView3 = findViewById(2131296455);
    this.mLightsUpInterceptor = localView3;
    if (this.mLightsUpInterceptor != null)
      this.mLightsUpInterceptor.setOnTouchListener(this);
    TextView localTextView3 = this.mCurrentTime;
    TextView localTextView4 = this.mTotalTime;
    RatingSelector localRatingSelector3 = this.mRatings;
    ImmutableSet localImmutableSet1 = ImmutableSet.of(localTextView3, localTextView4, localRatingSelector3);
    this.mLightUpOnlyViewsAll = localImmutableSet1;
    TextView localTextView5 = this.mCurrentTime;
    TextView localTextView6 = this.mTotalTime;
    RatingSelector localRatingSelector4 = this.mRatings;
    ImmutableSet localImmutableSet2 = ImmutableSet.of(localTextView5, localTextView6, localRatingSelector4);
    this.mLightUpOnlyViewsLimited = localImmutableSet2;
    this.mQueueSwitcher.setOnClickListener(this);
    this.mOverflowMenu.setOnClickListener(this);
    showQueue(false, false);
    this.mOverflowMenu.setVisibility(4);
    Animation localAnimation1 = AnimationUtils.loadAnimation(getActivity(), 2131034120);
    this.mQueueFadeInAnimation = localAnimation1;
    Animation localAnimation2 = AnimationUtils.loadAnimation(getActivity(), 2131034132);
    this.mQueueFadeOutAnimation = localAnimation2;
    MediaRouteButton localMediaRouteButton = (MediaRouteButton)this.mRootView.findViewById(2131296452);
    if (localMediaRouteButton != null)
    {
      FragmentActivity localFragmentActivity6 = getActivity();
      MediaRouteManager localMediaRouteManager = new MediaRouteManager(localFragmentActivity6);
      this.mMediaRouteManager = localMediaRouteManager;
      this.mMediaRouteManager.onCreate();
      this.mMediaRouteManager.bindMediaRouteButton(localMediaRouteButton);
    }
    View localView4 = this.mRootView;
    View.OnKeyListener local6 = new View.OnKeyListener()
    {
      public boolean onKey(View paramAnonymousView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
      {
        if ((NowPlayingScreenFragment.this.mMediaRouteManager != null) && (paramAnonymousKeyEvent.getAction() == 0));
        for (boolean bool = NowPlayingScreenFragment.this.mMediaRouteManager.onKeyDown(paramAnonymousInt, paramAnonymousKeyEvent); ; bool = false)
          return bool;
      }
    };
    localView4.setOnKeyListener(local6);
    LayoutInflater localLayoutInflater = getLayoutInflater(null);
    ViewGroup localViewGroup3 = this.mPlayingFromHeaderContainer;
    ViewGroup localViewGroup4 = (ViewGroup)localLayoutInflater.inflate(2130968661, localViewGroup3, false);
    this.mPlayingFromHeaderView = localViewGroup4;
    this.mPlayingFromHeaderView.setVisibility(8);
    ViewGroup localViewGroup5 = this.mPlayingFromHeaderContainer;
    ViewGroup localViewGroup6 = this.mPlayingFromHeaderView;
    localViewGroup5.addView(localViewGroup6);
    ((TextView)this.mPlayingFromHeaderView.findViewById(2131296493)).setTypeface(null, 1);
    final View localView5 = this.mPlayingFromHeaderView.findViewById(2131296492);
    View.OnClickListener localOnClickListener = this.mRefreshButtonListener;
    localView5.setOnClickListener(localOnClickListener);
    this.mPlayingFromHeaderRefreshButton = localView5;
    View.OnKeyListener local7 = new View.OnKeyListener()
    {
      public boolean onKey(View paramAnonymousView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
      {
        boolean bool1 = false;
        if (paramAnonymousKeyEvent.getAction() != 1);
        while (true)
        {
          return bool1;
          View localView = NowPlayingScreenFragment.this.mQueue.getSelectedView();
          if (localView != null)
          {
            ViewGroup localViewGroup = NowPlayingScreenFragment.this.mPlayingFromHeaderContainer;
            if ((localView == localViewGroup) && ((paramAnonymousInt == 23) || (paramAnonymousInt == 66)))
            {
              boolean bool2 = localView5.performClick();
              bool1 = true;
            }
          }
        }
      }
    };
    this.mQueue.setOnKeyListener(local7);
  }

  private void queueNextRefresh(long paramLong)
  {
    Message localMessage = this.mHandler.obtainMessage(1);
    this.mHandler.removeMessages(1);
    boolean bool = this.mHandler.sendMessageDelayed(localMessage, paramLong);
  }

  private long refreshNow()
  {
    long l1;
    if (this.mService != null)
    {
      ExpandingScrollView.ExpandingState localExpandingState1 = this.mCurrentState;
      ExpandingScrollView.ExpandingState localExpandingState2 = ExpandingScrollView.ExpandingState.HIDDEN;
      if (localExpandingState1 != localExpandingState2);
    }
    else
    {
      l1 = 500L;
    }
    while (true)
    {
      return l1;
      PlaybackState localPlaybackState;
      while (true)
      {
        try
        {
          localPlaybackState = this.mService.getPlaybackState();
          if (this.mHeaderPauseButton != null)
          {
            if ((localPlaybackState.isPreparing()) && (localPlaybackState.isStreaming()))
              this.mHeaderPauseButton.setCurrentPlayState(1);
          }
          else
          {
            ExpandingScrollView.ExpandingState localExpandingState3 = this.mCurrentState;
            ExpandingScrollView.ExpandingState localExpandingState4 = ExpandingScrollView.ExpandingState.COLLAPSED;
            if (localExpandingState3 != localExpandingState4)
              break label152;
            l1 = 500L;
            break;
          }
          if (!localPlaybackState.isPlaying())
            break label141;
          this.mHeaderPauseButton.setCurrentPlayState(2);
          continue;
        }
        catch (RemoteException localRemoteException)
        {
          String str1 = localRemoteException.getMessage();
          int i = Log.w("NowPlayingFragment", str1, localRemoteException);
          l1 = 500L;
        }
        break;
        label141: this.mHeaderPauseButton.setCurrentPlayState(3);
      }
      label152: if (this.mPosOverride < 0L);
      for (long l2 = localPlaybackState.getPosition(); ; l2 = this.mPosOverride)
      {
        long l3 = this.mRefreshDelayMs;
        long l4 = this.mRefreshDelayMs;
        long l5 = l2 % l4;
        l1 = l3 - l5;
        if ((l2 < 0L) || (this.mDuration <= 0L))
          break label417;
        Long localLong = Long.valueOf(l2 / 1000L);
        Object localObject = this.mCurrentTime.getTag();
        if (!localLong.equals(localObject))
        {
          TextView localTextView1 = this.mCurrentTime;
          FragmentActivity localFragmentActivity = getActivity();
          long l6 = localLong.longValue();
          String str2 = MusicUtils.makeTimeString(localFragmentActivity, l6);
          localTextView1.setText(str2);
          this.mCurrentTime.setTag(localLong);
        }
        if (!localPlaybackState.isPlaying())
          break label359;
        TextView localTextView2 = this.mCurrentTime;
        ColorStateList localColorStateList1 = this.mCurrentTimeColor;
        localTextView2.setTextColor(localColorStateList1);
        long l7 = l2 * 1000L;
        long l9 = this.mDuration;
        int j = (int)(l7 / l9);
        this.mProgress.setProgress(j);
        break;
      }
      label359: TextView localTextView3 = this.mCurrentTime;
      ColorStateList localColorStateList2 = this.mCurrentTime.getTextColors();
      ColorStateList localColorStateList3 = this.mCurrentTimeColor;
      if (localColorStateList2 == localColorStateList3);
      for (ColorStateList localColorStateList4 = this.mFlashWhenPausedColor; ; localColorStateList4 = this.mCurrentTimeColor)
      {
        localTextView3.setTextColor(localColorStateList4);
        long l8 = 500L;
        break;
      }
      label417: this.mCurrentTime.setText("--:--");
      this.mCurrentTime.setTag(null);
      TextView localTextView4 = this.mCurrentTime;
      ColorStateList localColorStateList5 = this.mCurrentTimeColor;
      localTextView4.setTextColor(localColorStateList5);
      this.mProgress.setProgress(0);
    }
  }

  private void reparentControls(ViewGroup paramViewGroup)
  {
    ViewGroup localViewGroup = this.mControlsParent;
    if (paramViewGroup == localViewGroup)
      return;
    View[] arrayOfView = this.mMovableControls;
    int i = arrayOfView.length;
    int j = 0;
    if (j < i)
    {
      View localView = arrayOfView[j];
      if (localView == null);
      while (true)
      {
        j += 1;
        break;
        ((ViewGroup)localView.getParent()).removeView(localView);
        paramViewGroup.addView(localView);
      }
    }
    this.mControlsParent = paramViewGroup;
    updateButtonsVisibility();
  }

  private void setCurrentPage()
  {
    try
    {
      int i = this.mService.getQueuePosition();
      if (this.mArtPagerAdapter != null)
      {
        int j = this.mArtPagerAdapter.getCount();
        if (i < j)
        {
          ContentIdentifier localContentIdentifier1 = this.mService.getAudioId();
          if (localContentIdentifier1 != null)
          {
            ContentIdentifier localContentIdentifier2 = this.mCurrentAudioId;
            if (localContentIdentifier1.equals(localContentIdentifier2));
          }
          for (boolean bool = true; ; bool = false)
          {
            this.mArtPager.setCurrentItem(i, bool);
            this.mCurrentAudioId = localContentIdentifier1;
            return;
          }
        }
      }
      if (!DEBUG_QUEUE)
        return;
      int k = Log.d("NowPlayingFragment", "setCurrentPage: Cursor not updated yet. Wait for the next update.");
      return;
    }
    catch (RemoteException localRemoteException)
    {
      String str = localRemoteException.getMessage();
      int m = Log.w("NowPlayingFragment", str, localRemoteException);
    }
  }

  private void setCurrentState(ExpandingScrollView.ExpandingState paramExpandingState)
  {
    this.mCurrentState = paramExpandingState;
    if (getBaseActivity().isEmptyScreenShowing())
      return;
    BaseActivity localBaseActivity = getBaseActivity();
    ExpandingScrollView.ExpandingState localExpandingState1 = this.mCurrentState;
    ExpandingScrollView.ExpandingState localExpandingState2 = ExpandingScrollView.ExpandingState.FULLY_EXPANDED;
    if (localExpandingState1 != localExpandingState2);
    for (boolean bool = true; ; bool = false)
    {
      localBaseActivity.enableSideDrawer(bool);
      return;
    }
  }

  private void setTabletMetadataWidth()
  {
    if (!this.mIsTablet)
      return;
    View localView = this.mRootView;
    if (localView != null);
    for (int i = localView.getWidth(); ; i = -1)
    {
      if (i == 0)
        i = -1;
      this.mTabletHeaderView.setMetadataViewWidth(i);
      return;
    }
  }

  private void setupNormalPlaybackMode()
  {
    this.mIsRestrictedPlaybackMode = false;
    this.mRatings.setVisibility(0);
    this.mTotalTime.setVisibility(0);
    this.mCurrentTime.setVisibility(0);
    if (this.mQueueShown)
      return;
    this.mProgress.setThumbAlpha(255);
    this.mProgress.setEnabled(true);
  }

  private void setupPlayQueue()
  {
    int i = 1;
    if (this.mLockNowPlayingScreen)
      return;
    ExpandingScrollView localExpandingScrollView = getBottomDrawer();
    PlaybackState localPlaybackState = MusicUtils.getPlaybackState();
    if ((localPlaybackState != null) && (localPlaybackState.hasValidPlaylist()))
    {
      SongList localSongList1 = localPlaybackState.getMediaList();
      this.mSongList = localSongList1;
      if (this.mSongList == null)
      {
        int j = Log.e("NowPlayingFragment", "The play queue not available.");
        return;
      }
      if (getActivity() == null)
      {
        int k = Log.e("NowPlayingFragment", "Not attached to an activity.");
        return;
      }
      SongList localSongList2 = this.mSongList;
      QueueTrackListAdapter localQueueTrackListAdapter1 = new QueueTrackListAdapter(this, true, localSongList2);
      this.mQueueAdapter = localQueueTrackListAdapter1;
      QueueTrackListAdapter localQueueTrackListAdapter2 = this.mQueueAdapter;
      boolean bool = this.mQueueShown;
      localQueueTrackListAdapter2.showAlbumArt(bool);
      BaseTrackListView localBaseTrackListView1 = this.mQueue;
      QueueTrackListAdapter localQueueTrackListAdapter3 = this.mQueueAdapter;
      localBaseTrackListView1.setAdapter(localQueueTrackListAdapter3);
      this.mQueue.setFastScrollEnabled(false);
      this.mQueue.scrollToNowPlaying();
      BaseTrackListView localBaseTrackListView2 = this.mQueue;
      SongList localSongList3 = this.mSongList;
      localBaseTrackListView2.setSongList(localSongList3);
      BaseTrackListView localBaseTrackListView3 = this.mQueue;
      AdapterView.OnItemClickListener local14 = new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
        {
          if ((paramAnonymousAdapterView instanceof BaseTrackListView))
          {
            BaseTrackListView localBaseTrackListView1 = (BaseTrackListView)paramAnonymousAdapterView;
            BaseTrackListView localBaseTrackListView2 = localBaseTrackListView1;
            View localView = paramAnonymousView;
            int i = paramAnonymousInt;
            long l = paramAnonymousLong;
            localBaseTrackListView1.handleItemClick(localBaseTrackListView2, localView, i, l);
            return;
          }
          int j = Log.w("NowPlayingFragment", "Unknown listview type");
        }
      };
      localBaseTrackListView3.setOnItemClickListener(local14);
      updateButtonsVisibility();
      if (this.mSongList.isFlagSet(256))
        setupNormalPlaybackMode();
      while (true)
      {
        SongList localSongList4 = this.mSongList;
        FragmentActivity localFragmentActivity = getActivity();
        if (localSongList4.getFullContentUri(localFragmentActivity) != null)
          i = 0;
        Loader localLoader = getLoaderManager().initLoader(i, null, this);
        if (localExpandingScrollView != null)
          localExpandingScrollView.setHidden(false);
        updatePlayingFromHeader();
        return;
        setupRestrictedPlaybackMode();
      }
    }
    showQueue(false, false);
    showActionBar();
    if (localExpandingScrollView == null)
      return;
    localExpandingScrollView.setHidden(true);
  }

  private void setupRestrictedPlaybackMode()
  {
    this.mIsRestrictedPlaybackMode = true;
    this.mRatings.setVisibility(8);
    this.mTotalTime.setVisibility(8);
    this.mCurrentTime.setVisibility(8);
    this.mProgress.setThumbAlpha(0);
    this.mProgress.setEnabled(false);
  }

  private void setupViewPager()
  {
    if (this.mArtPagerAdapter == null)
    {
      LinkableViewPager localLinkableViewPager1 = this.mArtPager;
      NowPlayingArtPageAdapter localNowPlayingArtPageAdapter = new NowPlayingArtPageAdapter(localLinkableViewPager1);
      this.mArtPagerAdapter = localNowPlayingArtPageAdapter;
      if (this.mHeaderPager != null)
      {
        if (this.mHeaderPagerAdapter != null)
          break label78;
        LinkableViewPager localLinkableViewPager2 = this.mHeaderPager;
        NowPlayingHeaderPageAdapter localNowPlayingHeaderPageAdapter = new NowPlayingHeaderPageAdapter(localLinkableViewPager2);
        this.mHeaderPagerAdapter = localNowPlayingHeaderPageAdapter;
      }
    }
    while (true)
    {
      setCurrentPage();
      return;
      this.mArtPagerAdapter.notifyDataSetChanged();
      break;
      label78: this.mHeaderPagerAdapter.notifyDataSetChanged();
    }
  }

  private boolean shouldHideShuffleAndRepeat()
  {
    boolean bool = false;
    if (this.mIsTablet)
    {
      ViewGroup localViewGroup = this.mControlsParent;
      NowPlayingTabletHeaderView localNowPlayingTabletHeaderView = this.mTabletHeaderView;
      if (localViewGroup != localNowPlayingTabletHeaderView);
    }
    for (int i = 1; ; i = 0)
    {
      if ((i != 0) || (MusicUtils.isInIniniteMixMode()) || (MusicUtils.isCurrentContentFromStoreUrl()))
        bool = true;
      return bool;
    }
  }

  private void showActionBar()
  {
    getActionBarController().showActionBar();
  }

  private void showQueue(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.mLockNowPlayingScreen)
      return;
    Animation localAnimation1;
    if (paramBoolean2)
    {
      localAnimation1 = this.mArtPager.getAnimation();
      if (localAnimation1 != null)
        localAnimation1.setAnimationListener(null);
      this.mArtPager.clearAnimation();
    }
    if (paramBoolean1)
    {
      this.mQueueWrapper.setVisibility(0);
      if (this.mQueueAdapter != null)
        this.mQueueAdapter.showAlbumArt(true);
      this.mQueue.scrollToNowPlaying();
      if (!getPreferences().isXLargeScreen())
      {
        this.mProgress.setThumbAlpha(0);
        this.mProgress.setEnabled(false);
      }
      if (paramBoolean2)
      {
        localAnimation1 = this.mArtPager.getAnimation();
        if (localAnimation1 != null)
          localAnimation1.setAnimationListener(null);
        Animation localAnimation2 = this.mQueueFadeInAnimation;
        Animation.AnimationListener localAnimationListener1 = this.mQueueSlideDownAnimationListener;
        localAnimation2.setAnimationListener(localAnimationListener1);
        View localView1 = this.mQueueWrapper;
        Animation localAnimation3 = this.mQueueFadeInAnimation;
        localView1.startAnimation(localAnimation3);
      }
    }
    while (true)
    {
      this.mQueueShown = paramBoolean1;
      updateQueueSwitcherState();
      return;
      this.mArtPager.setVisibility(0);
      if (!this.mIsRestrictedPlaybackMode)
      {
        this.mProgress.setThumbAlpha(255);
        this.mProgress.setEnabled(true);
      }
      if (paramBoolean2)
      {
        Animation localAnimation4 = this.mQueueFadeOutAnimation;
        Animation.AnimationListener localAnimationListener2 = this.mQueueSlideUpAnimationListener;
        localAnimation4.setAnimationListener(localAnimationListener2);
        View localView2 = this.mQueueWrapper;
        Animation localAnimation5 = this.mQueueFadeOutAnimation;
        localView2.startAnimation(localAnimation5);
      }
      else
      {
        this.mQueueWrapper.setVisibility(4);
      }
    }
  }

  private void startLightsAnimation(boolean paramBoolean)
  {
    int i = 255;
    MusicUtils.assertUiThread();
    Set localSet;
    if (paramBoolean)
    {
      LightsState localLightsState1 = LightsState.TRANSITIONING_UP;
      this.mCurrentLightsState = localLightsState1;
      if (!paramBoolean)
        break label101;
      if (!shouldHideShuffleAndRepeat())
        break label92;
      localSet = this.mLightUpOnlyViewsLimited;
    }
    while (true)
    {
      Iterator localIterator = localSet.iterator();
      while (localIterator.hasNext())
      {
        View localView = (View)localIterator.next();
        startLightsAnimation(paramBoolean, localView);
      }
      LightsState localLightsState2 = LightsState.TRANSITIONING_DOWN;
      this.mCurrentLightsState = localLightsState2;
      break;
      label92: localSet = this.mLightUpOnlyViewsAll;
      continue;
      label101: localSet = this.mLightUpOnlyViewsAll;
    }
    int j;
    if (paramBoolean)
    {
      j = 0;
      if (!paramBoolean)
        break label268;
    }
    while (true)
    {
      if (this.mThumbAlphaAnimation != null)
      {
        Object localObject = this.mThumbAlphaAnimation.getAnimatedValue();
        if ((localObject != null) && ((localObject instanceof Integer)))
          j = ((Integer)this.mThumbAlphaAnimation.getAnimatedValue()).intValue();
        PropertyAnimator localPropertyAnimator1 = this.mThumbAlphaAnimation;
        AnimatorListener localAnimatorListener1 = this.mProgresAnimationListener;
        localPropertyAnimator1.removeListener(localAnimatorListener1);
        this.mThumbAlphaAnimation.cancel();
      }
      int k = j;
      int m = getLightsAnimationDuration(paramBoolean);
      SizableTrackSeekBar localSizableTrackSeekBar = this.mProgress;
      PropertyAnimator localPropertyAnimator2 = new PropertyAnimator(m, localSizableTrackSeekBar, "thumbAlpha", k, i);
      this.mThumbAlphaAnimation = localPropertyAnimator2;
      PropertyAnimator localPropertyAnimator3 = this.mThumbAlphaAnimation;
      AnimatorListener localAnimatorListener2 = this.mProgresAnimationListener;
      localPropertyAnimator3.addListener(localAnimatorListener2);
      this.mThumbAlphaAnimation.start();
      return;
      j = 255;
      break;
      label268: i = 0;
    }
  }

  private void startLightsAnimation(final boolean paramBoolean, final View paramView)
  {
    float f1 = 1.0F;
    Animation localAnimation = paramView.getAnimation();
    if (localAnimation != null)
      localAnimation.setAnimationListener(null);
    if ((!paramBoolean) && (paramView.getVisibility() == 4))
    {
      paramView.clearAnimation();
      return;
    }
    float f2;
    StatefulAlphaAnimation localStatefulAlphaAnimation;
    if ((localAnimation != null) && ((localAnimation instanceof StatefulAlphaAnimation)))
    {
      f2 = ((StatefulAlphaAnimation)localAnimation).getCurrentAlpha();
      paramView.clearAnimation();
      localStatefulAlphaAnimation = new com/google/android/music/animator/StatefulAlphaAnimation;
      if (!paramBoolean)
        break label143;
    }
    while (true)
    {
      localStatefulAlphaAnimation.<init>(f2, f1);
      long l = getLightsAnimationDuration(paramBoolean);
      localStatefulAlphaAnimation.setDuration(l);
      Animation.AnimationListener local9 = new Animation.AnimationListener()
      {
        public void onAnimationEnd(Animation paramAnonymousAnimation)
        {
          MusicUtils.assertUiThread();
          if (paramBoolean)
          {
            NowPlayingScreenFragment localNowPlayingScreenFragment1 = NowPlayingScreenFragment.this;
            NowPlayingScreenFragment.LightsState localLightsState1 = NowPlayingScreenFragment.LightsState.ON;
            NowPlayingScreenFragment.LightsState localLightsState2 = NowPlayingScreenFragment.access$1602(localNowPlayingScreenFragment1, localLightsState1);
            return;
          }
          paramView.setVisibility(4);
          ViewUtils.setEnabledAll(paramView, false);
          NowPlayingScreenFragment localNowPlayingScreenFragment2 = NowPlayingScreenFragment.this;
          NowPlayingScreenFragment.LightsState localLightsState3 = NowPlayingScreenFragment.LightsState.OFF;
          NowPlayingScreenFragment.LightsState localLightsState4 = NowPlayingScreenFragment.access$1602(localNowPlayingScreenFragment2, localLightsState3);
        }

        public void onAnimationRepeat(Animation paramAnonymousAnimation)
        {
        }

        public void onAnimationStart(Animation paramAnonymousAnimation)
        {
          if (!paramBoolean)
            return;
          paramView.setVisibility(0);
          ViewUtils.setEnabledAll(paramView, true);
        }
      };
      localStatefulAlphaAnimation.setAnimationListener(local9);
      paramView.startAnimation(localStatefulAlphaAnimation);
      return;
      if (paramBoolean);
      for (f2 = 0.0F; ; f2 = 1.0F)
        break;
      label143: f1 = 0.0F;
    }
  }

  private void turnLightsOff()
  {
    MusicUtils.assertUiThread();
    this.mHandler.removeMessages(7);
    LightsState localLightsState1 = this.mCurrentLightsState;
    LightsState localLightsState2 = LightsState.OFF;
    if (localLightsState1 == localLightsState2)
      return;
    LightsState localLightsState3 = this.mCurrentLightsState;
    LightsState localLightsState4 = LightsState.TRANSITIONING_DOWN;
    if (localLightsState3 == localLightsState4)
      return;
    startLightsAnimation(false);
  }

  private void unlockNowPlayingScreen()
  {
    if (this.mQueueAdapter != null)
    {
      Animation localAnimation = this.mPlayingFromHeaderRefreshButton.getAnimation();
      if (localAnimation != null)
        localAnimation.setRepeatCount(0);
      this.mQueueAdapter.setEnabled(true);
      this.mPlayingFromHeaderRefreshButton.setEnabled(true);
    }
    this.mLockNowPlayingScreen = false;
  }

  private void updateButtonsVisibility()
  {
    if (shouldHideShuffleAndRepeat())
    {
      this.mShuffleButton.setVisibility(4);
      this.mShuffleButton.setEnabled(false);
      this.mRepeatButton.setVisibility(4);
      this.mRepeatButton.setEnabled(false);
      return;
    }
    this.mShuffleButton.setVisibility(0);
    this.mShuffleButton.setEnabled(true);
    this.mRepeatButton.setVisibility(0);
    this.mRepeatButton.setEnabled(true);
  }

  private void updatePlayingFromHeader()
  {
    boolean bool = false;
    if (this.mPlayingFromHeaderView == null)
      return;
    PlaybackState localPlaybackState = MusicUtils.getPlaybackState();
    Object localObject1 = null;
    if (localPlaybackState.isInIniniteMixMode());
    while (true)
    {
      try
      {
        MixDescriptor localMixDescriptor1 = this.mService.getMixState().getMix();
        localObject2 = localMixDescriptor1;
        MixDescriptor localMixDescriptor2 = this.mCurrentMix;
        if (localObject2 == localMixDescriptor2)
          return;
        if (localObject2 != null)
        {
          TextView localTextView = (TextView)this.mPlayingFromHeaderView.findViewById(2131296493);
          if (getPreferences().isNautilusEnabled())
          {
            i = 2131231423;
            String str1 = getString(i);
            Object[] arrayOfObject = new Object[2];
            String str2 = ((MixDescriptor)localObject2).getName();
            arrayOfObject[0] = str2;
            arrayOfObject[1] = str1;
            String str3 = getString(2131231421, arrayOfObject);
            localTextView.setText(str3);
          }
        }
        else
        {
          this.mCurrentMix = ((MixDescriptor)localObject2);
          ViewGroup localViewGroup = this.mPlayingFromHeaderView;
          if (localObject2 == null)
            continue;
          j = 0;
          localViewGroup.setVisibility(j);
          BaseTrackListView localBaseTrackListView = this.mQueue;
          if (localObject2 != null)
            bool = true;
          localBaseTrackListView.setHeaderDividersEnabled(bool);
          this.mQueue.invalidateViews();
          return;
        }
      }
      catch (RemoteException localRemoteException)
      {
        localObject2 = localObject1;
        continue;
        int i = 2131231424;
        continue;
        int j = 8;
        continue;
      }
      Object localObject2 = localObject1;
    }
  }

  private void updateQueueSwitcherState()
  {
    ImageButton localImageButton = this.mQueueSwitcher;
    if (this.mQueueShown);
    for (int i = 2130837733; ; i = 2130837732)
    {
      localImageButton.setImageResource(i);
      return;
    }
  }

  private void updateTrackInfo()
  {
    if (this.mService == null)
      return;
    Intent localIntent1 = new Intent("com.android.music.metachanged");
    IMusicPlaybackService localIMusicPlaybackService = this.mService;
    FragmentActivity localFragmentActivity = getActivity();
    Intent localIntent2 = MusicPlaybackService.populateExtras(localIntent1, localIMusicPlaybackService, localFragmentActivity);
    updateTrackInfo(localIntent2);
  }

  private void updateTrackInfo(Intent paramIntent)
  {
    Message localMessage = this.mHandler.obtainMessage(5, null);
    localMessage.obj = paramIntent;
    this.mHandler.removeMessages(5);
    boolean bool = this.mHandler.sendMessage(localMessage);
  }

  private void updateTrackInfoImpl(Intent paramIntent)
  {
    if (getActivity() == null)
      return;
    if (paramIntent.getBooleanExtra("currentSongLoaded", false))
    {
      long l1 = paramIntent.getLongExtra("id", 65535L);
      ContentIdentifier.Domain[] arrayOfDomain = ContentIdentifier.Domain.values();
      int i = paramIntent.getIntExtra("domain", -1);
      ContentIdentifier.Domain localDomain = arrayOfDomain[i];
      ContentIdentifier localContentIdentifier = new ContentIdentifier(l1, localDomain);
      BufferProgressListener localBufferProgressListener = this.mSongBufferListener;
      boolean bool = paramIntent.getBooleanExtra("streaming", false);
      localBufferProgressListener.updateCurrentSong(localContentIdentifier, bool);
      String str1 = paramIntent.getStringExtra("artist");
      if (MusicUtils.isUnknown(str1))
        str1 = getActivity().getString(2131230890);
      String str2 = paramIntent.getStringExtra("album");
      if (MusicUtils.isUnknown(str2))
        str2 = getActivity().getString(2131230891);
      long l2 = paramIntent.getLongExtra("albumId", 65535L);
      String str3 = paramIntent.getStringExtra("track");
      long l3 = paramIntent.getLongExtra("duration", 0L);
      this.mDuration = l3;
      FragmentActivity localFragmentActivity = getActivity();
      long l4 = this.mDuration / 1000L;
      String str4 = MusicUtils.makeTimeString(localFragmentActivity, l4);
      this.mTotalTime.setText(str4);
      RatingSelector localRatingSelector = this.mRatings;
      int j = paramIntent.getIntExtra("rating", 0);
      localRatingSelector.setRating(j);
      String str5 = paramIntent.getStringExtra("externalAlbumArtUrl");
      if (this.mTabletHeaderView != null)
      {
        NowPlayingTabletHeaderView localNowPlayingTabletHeaderView = this.mTabletHeaderView;
        Long localLong = Long.valueOf(l2);
        localNowPlayingTabletHeaderView.updateView(str3, str1, str2, localLong, str5);
      }
      setCurrentPage();
      return;
    }
    this.mSongBufferListener.updateCurrentSong(null, false);
  }

  public MediaList getFragmentMediaList()
  {
    return this.mSongList;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    ExpandingScrollView localExpandingScrollView = getBottomDrawer();
    View localView = this.mRootView;
    localExpandingScrollView.setContent(localView);
    localExpandingScrollView.setViewIdForSizingCollapsedState(2131296451);
    if (paramBundle == null)
      return;
    ExpandingScrollView.ExpandingState localExpandingState = ExpandingScrollView.ExpandingState.valueOf(paramBundle.getString("expandingState"));
    setCurrentState(localExpandingState);
    boolean bool = paramBundle.getBoolean("queueShown");
    this.mQueueShown = bool;
  }

  public void onClick(View paramView)
  {
    ImageButton localImageButton1 = this.mQueueSwitcher;
    if (paramView == localImageButton1)
    {
      if (!this.mQueueShown);
      for (boolean bool = true; ; bool = false)
      {
        showQueue(bool, true);
        return;
      }
    }
    ImageButton localImageButton2 = this.mOverflowMenu;
    if (paramView == localImageButton2)
    {
      if (this.mIsRestrictedPlaybackMode)
      {
        Document localDocument1 = new Document();
        ScreenMenuHandler.ScreenMenuType localScreenMenuType1 = ScreenMenuHandler.ScreenMenuType.NOW_PLAYING_RESTRICTED;
        new ScreenMenuHandler(this, localDocument1, localScreenMenuType1).showPopupMenu(paramView);
        return;
      }
      if (this.mQueueAdapter == null)
        return;
      try
      {
        int i = this.mService.getQueuePosition();
        Document localDocument2 = this.mQueueAdapter.getDocument(i);
        ScreenMenuHandler.ScreenMenuType localScreenMenuType2 = ScreenMenuHandler.ScreenMenuType.NOW_PLAYING;
        new ScreenMenuHandler(this, localDocument2, localScreenMenuType2).showPopupMenu(paramView);
        return;
      }
      catch (RemoteException localRemoteException1)
      {
        String str1 = localRemoteException1.getMessage();
        int j = Log.w("NowPlayingFragment", str1, localRemoteException1);
        return;
      }
    }
    PlayPauseButton localPlayPauseButton = this.mHeaderPauseButton;
    if (paramView != localPlayPauseButton)
      return;
    try
    {
      if ((this.mService.isPreparing()) && (this.mService.isStreaming()))
      {
        this.mService.stop();
        return;
      }
    }
    catch (RemoteException localRemoteException2)
    {
      String str2 = localRemoteException2.getMessage();
      int k = Log.w("NowPlayingFragment", str2, localRemoteException2);
      return;
    }
    if (this.mService.isPlaying())
    {
      this.mService.pause();
      return;
    }
    this.mService.play();
  }

  public Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    FragmentActivity localFragmentActivity1;
    SongList localSongList1;
    String[] arrayOfString1;
    if (paramInt == 0)
    {
      localFragmentActivity1 = getActivity();
      localSongList1 = this.mSongList;
      arrayOfString1 = TrackListAdapter.PROJECTION;
    }
    FragmentActivity localFragmentActivity2;
    SongList localSongList2;
    String[] arrayOfString2;
    for (Object localObject = new MediaListCursorLoader(localFragmentActivity1, localSongList1, arrayOfString1); ; localObject = new NonUriMediaListCursorLoader(localFragmentActivity2, localSongList2, arrayOfString2))
    {
      return localObject;
      localFragmentActivity2 = getActivity();
      localSongList2 = this.mSongList;
      arrayOfString2 = TrackListAdapter.PROJECTION;
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130968646, paramViewGroup, false);
    this.mRootView = localView;
    initializeView();
    clearDisplay();
    boolean bool = getPreferences().isTabletMusicExperience();
    this.mIsTablet = bool;
    queueNextRefresh(1L);
    this.mPlaybackControls.refreshButtonImages();
    FragmentActivity localFragmentActivity1 = getActivity();
    ServiceConnection localServiceConnection = this.mPlaybackServiceConnection;
    MusicUtils.ServiceToken localServiceToken = MusicUtils.bindToService(localFragmentActivity1, localServiceConnection);
    this.mPlaybackServiceToken = localServiceToken;
    IntentFilter localIntentFilter1 = new IntentFilter();
    localIntentFilter1.addAction("com.android.music.playstatechanged");
    localIntentFilter1.addAction("com.android.music.metachanged");
    localIntentFilter1.addAction("com.android.music.asyncopencomplete");
    localIntentFilter1.addAction("com.android.music.asyncopenstart");
    localIntentFilter1.addAction("com.android.music.playbackfailed");
    localIntentFilter1.addAction("com.android.music.queuechanged");
    localIntentFilter1.addAction("com.google.android.music.mix.playbackmodechanged");
    localIntentFilter1.addAction("com.google.android.music.refreshcomplete");
    localIntentFilter1.addAction("com.google.android.music.refreshfailed");
    FragmentActivity localFragmentActivity2 = getActivity();
    BroadcastReceiver localBroadcastReceiver1 = this.mStatusListener;
    Intent localIntent = localFragmentActivity2.registerReceiver(localBroadcastReceiver1, localIntentFilter1);
    IntentFilter localIntentFilter2 = new IntentFilter();
    localIntentFilter2.addAction("com.google.android.music.nowplaying.HEADER_CLICKED");
    localIntentFilter2.addAction("com.google.android.music.nowplaying.HEADER_ART_CLICKED");
    localIntentFilter2.addAction("com.google.android.music.OPEN_DRAWER");
    LocalBroadcastManager localLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
    BroadcastReceiver localBroadcastReceiver2 = this.mUIInteractionListener;
    localLocalBroadcastManager.registerReceiver(localBroadcastReceiver2, localIntentFilter2);
    updateButtonsVisibility();
    return super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
  }

  public void onDestroy()
  {
    this.mHandler.removeMessages(1);
    this.mHandler.removeMessages(5);
    this.mHandler.removeMessages(7);
    LocalBroadcastManager localLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
    BroadcastReceiver localBroadcastReceiver1 = this.mUIInteractionListener;
    localLocalBroadcastManager.unregisterReceiver(localBroadcastReceiver1);
    FragmentActivity localFragmentActivity = getActivity();
    BroadcastReceiver localBroadcastReceiver2 = this.mStatusListener;
    localFragmentActivity.unregisterReceiver(localBroadcastReceiver2);
    this.mSongBufferListener.updateCurrentSong(null, false);
    MusicUtils.unbindFromService(this.mPlaybackServiceToken);
    this.mService = null;
    this.mSongBufferListener.destroy();
    super.onDestroy();
  }

  public void onDestroyView()
  {
    if (this.mTabletHeaderView != null)
      this.mTabletHeaderView.onDestroyView();
    if (this.mMediaRouteManager != null)
      this.mMediaRouteManager.onDestroy();
    super.onDestroyView();
  }

  public void onDragEnded(ExpandingScrollView paramExpandingScrollView, ExpandingScrollView.ExpandingState paramExpandingState)
  {
    BaseActivity localBaseActivity;
    if (!getBaseActivity().isEmptyScreenShowing())
    {
      localBaseActivity = getBaseActivity();
      ExpandingScrollView.ExpandingState localExpandingState1 = ExpandingScrollView.ExpandingState.FULLY_EXPANDED;
      if (paramExpandingState == localExpandingState1)
        break label67;
    }
    label67: for (boolean bool = true; ; bool = false)
    {
      localBaseActivity.enableSideDrawer(bool);
      if (!this.mIsTablet)
        return;
      ExpandingScrollView.ExpandingState localExpandingState2 = ExpandingScrollView.ExpandingState.FULLY_EXPANDED;
      if (paramExpandingState != localExpandingState2)
        break;
      this.mTabletHeaderView.setIsClosed(false);
      updateButtonsVisibility();
      return;
    }
    ExpandingScrollView.ExpandingState localExpandingState3 = ExpandingScrollView.ExpandingState.COLLAPSED;
    if (paramExpandingState != localExpandingState3)
      return;
    NowPlayingTabletHeaderView localNowPlayingTabletHeaderView = this.mTabletHeaderView;
    reparentControls(localNowPlayingTabletHeaderView);
    this.mTabletHeaderView.setIsClosed(true);
  }

  public void onDragStarted(ExpandingScrollView paramExpandingScrollView, ExpandingScrollView.ExpandingState paramExpandingState)
  {
    getBaseActivity().enableSideDrawer(false);
    if (!this.mIsTablet)
      return;
    ViewGroup localViewGroup = this.mFooterButtonsWrapper;
    reparentControls(localViewGroup);
    this.mTabletHeaderView.setIsClosed(false);
  }

  public void onExpandingStateChanged(ExpandingScrollView paramExpandingScrollView, ExpandingScrollView.ExpandingState paramExpandingState1, ExpandingScrollView.ExpandingState paramExpandingState2)
  {
    setCurrentState(paramExpandingState2);
    ExpandingScrollView.ExpandingState localExpandingState1 = ExpandingScrollView.ExpandingState.FULLY_EXPANDED;
    if (paramExpandingState2 == localExpandingState1)
    {
      if (this.mHeaderPauseButton != null)
        this.mHeaderPauseButton.setVisibility(8);
      if (this.mIsTablet)
      {
        ViewGroup localViewGroup = this.mFooterButtonsWrapper;
        reparentControls(localViewGroup);
        this.mTabletHeaderView.setIsClosed(false);
      }
      this.mOverflowMenu.setVisibility(0);
      this.mQueueSwitcher.setVisibility(0);
      this.mMediaRouteManager.setMediaRouteButtonVisibility(true);
      if (this.mHeaderPager != null)
        this.mHeaderPager.getBackground().setAlpha(230);
      if (this.mTabletHeaderView != null)
        this.mTabletHeaderView.getBackground().setAlpha(230);
    }
    while (true)
    {
      LocalBroadcastManager localLocalBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
      Intent localIntent = new Intent("com.google.android.music.nowplaying.DRAWER_STATE_CHANGED_ACTION");
      boolean bool = localLocalBroadcastManager.sendBroadcast(localIntent);
      return;
      ExpandingScrollView.ExpandingState localExpandingState2 = ExpandingScrollView.ExpandingState.COLLAPSED;
      if (paramExpandingState2 == localExpandingState2)
      {
        if (this.mHeaderPauseButton != null)
          this.mHeaderPauseButton.setVisibility(0);
        if (this.mIsTablet)
        {
          NowPlayingTabletHeaderView localNowPlayingTabletHeaderView = this.mTabletHeaderView;
          reparentControls(localNowPlayingTabletHeaderView);
          this.mTabletHeaderView.setIsClosed(true);
        }
        this.mOverflowMenu.setVisibility(8);
        this.mQueueSwitcher.setVisibility(8);
        this.mMediaRouteManager.setMediaRouteButtonVisibility(false);
        if (this.mHeaderPager != null)
          this.mHeaderPager.getBackground().setAlpha(255);
        if (this.mTabletHeaderView != null)
          this.mTabletHeaderView.getBackground().setAlpha(255);
        showQueue(false, false);
      }
    }
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    this.mQueueCursor = paramCursor;
    if (DEBUG_QUEUE)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Cursor updated: count=");
      int i = paramCursor.getCount();
      String str = i;
      int j = Log.d("NowPlayingFragment", str);
    }
    setupViewPager();
    Cursor localCursor = this.mQueueAdapter.swapCursor(paramCursor);
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
    this.mQueueCursor = null;
    Cursor localCursor = this.mQueueAdapter.swapCursor(null);
  }

  public void onMoving(ExpandingScrollView paramExpandingScrollView, ExpandingScrollView.ExpandingState paramExpandingState, float paramFloat)
  {
    int[] arrayOfInt = 16.$SwitchMap$com$google$android$music$widgets$ExpandingScrollView$ExpandingState;
    int i = paramExpandingState.ordinal();
    switch (arrayOfInt[i])
    {
    default:
      return;
    case 1:
      if (paramFloat > 0.8D)
        hideActionBar();
      while (true)
      {
        if (MusicPreferences.isHoneycombOrGreater())
        {
          float f = Math.max(paramFloat, 0.5F);
          this.mArtPager.setAlpha(f);
        }
        Drawable localDrawable = paramExpandingScrollView.getBackground();
        int j = (int)(255.0F * paramFloat);
        localDrawable.setAlpha(j);
        return;
        showActionBar();
      }
    case 2:
      ExpandingScrollView.ExpandingState localExpandingState1 = this.mCurrentState;
      ExpandingScrollView.ExpandingState localExpandingState2 = ExpandingScrollView.ExpandingState.FULLY_EXPANDED;
      if (localExpandingState1 == localExpandingState2)
        hideActionBar();
      ViewUtils.setAlpha(this.mArtPager, 1.0F);
      paramExpandingScrollView.getBackground().setAlpha(0);
      return;
    case 3:
    }
    paramExpandingScrollView.getBackground().setAlpha(0);
  }

  public void onPause()
  {
    super.onPause();
    ExpandingScrollView localExpandingScrollView = getBottomDrawer();
    if (localExpandingScrollView != null)
      boolean bool = localExpandingScrollView.removeListener(this);
    if (this.mMediaRouteManager == null)
      return;
    this.mMediaRouteManager.onPause();
  }

  public void onResume()
  {
    super.onResume();
    long l = refreshNow();
    queueNextRefresh(l);
    ExpandingScrollView localExpandingScrollView = getBottomDrawer();
    if (localExpandingScrollView != null);
    try
    {
      if ((MusicUtils.sService == null) || (!MusicUtils.sService.hasValidPlaylist()))
      {
        localExpandingScrollView.setHidden(true);
        ExpandingScrollView.ExpandingState localExpandingState1 = ExpandingScrollView.ExpandingState.HIDDEN;
        setCurrentState(localExpandingState1);
      }
      while (true)
      {
        localExpandingScrollView.addListener(this);
        if (this.mMediaRouteManager == null)
          return;
        this.mMediaRouteManager.onResume();
        return;
        ExpandingScrollView.ExpandingState localExpandingState2 = this.mCurrentState;
        ExpandingScrollView.ExpandingState localExpandingState3 = ExpandingScrollView.ExpandingState.HIDDEN;
        if (localExpandingState2 == localExpandingState3)
        {
          ExpandingScrollView.ExpandingState localExpandingState4 = ExpandingScrollView.ExpandingState.COLLAPSED;
          setCurrentState(localExpandingState4);
        }
        ExpandingScrollView.ExpandingState localExpandingState5 = this.mCurrentState;
        localExpandingScrollView.moveToExpandingState(localExpandingState5);
        boolean bool = this.mQueueShown;
        showQueue(bool, false);
      }
    }
    catch (RemoteException localRemoteException)
    {
      while (true)
      {
        String str = localRemoteException.getMessage();
        int i = Log.w("NowPlayingFragment", str, localRemoteException);
      }
    }
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    String str = this.mCurrentState.toString();
    paramBundle.putString("expandingState", str);
    boolean bool = this.mQueueShown;
    paramBundle.putBoolean("queueShown", bool);
  }

  public void onStart()
  {
    super.onStart();
    ExpandingScrollView localExpandingScrollView = getBottomDrawer();
    if (localExpandingScrollView.getContent() == null)
    {
      View localView1 = this.mRootView;
      localExpandingScrollView.setContent(localView1);
      localExpandingScrollView.setViewIdForSizingCollapsedState(2131296451);
    }
    this.mIsStarted = true;
    updateTrackInfo();
    View localView2 = this.mRootView;
    Runnable local5 = new Runnable()
    {
      public void run()
      {
        NowPlayingScreenFragment.this.setTabletMetadataWidth();
      }
    };
    boolean bool = localView2.post(local5);
    if (this.mMediaRouteManager == null)
      return;
    this.mMediaRouteManager.onStart();
  }

  public void onStop()
  {
    super.onStop();
    this.mIsStarted = false;
    ExpandingScrollView localExpandingScrollView = getBottomDrawer();
    if (localExpandingScrollView != null)
      localExpandingScrollView.setContent(null);
    clearDisplay();
    if (this.mMediaRouteManager == null)
      return;
    this.mMediaRouteManager.onStop();
  }

  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    return false;
  }

  private class NowPlayingHeaderPageAdapter extends FragmentStatePagerAdapter
    implements ViewPager.OnPageChangeListener
  {
    private boolean mButtonsHidden;
    private boolean mPageScrolling;
    private ViewPager mPager;

    public NowPlayingHeaderPageAdapter(ViewPager arg2)
    {
      super();
      Object localObject;
      this.mPager = localObject;
      this.mPager.setAdapter(this);
      this.mPager.setOnPageChangeListener(this);
    }

    public int getCount()
    {
      return NowPlayingScreenFragment.this.getPageCount();
    }

    public Fragment getItem(int paramInt)
    {
      MusicUtils.assertUiThread();
      int i;
      if (NowPlayingScreenFragment.this.mQueueCursor != null)
        i = NowPlayingScreenFragment.this.mQueueCursor.getPosition();
      try
      {
        Cursor localCursor1 = NowPlayingScreenFragment.this.mQueueCursor;
        int j = paramInt;
        if (localCursor1.moveToPosition(j))
        {
          int k = NowPlayingScreenFragment.this.mQueueCursor.getColumnIndexOrThrow("title");
          int m = NowPlayingScreenFragment.this.mQueueCursor.getColumnIndexOrThrow("artist");
          int n = NowPlayingScreenFragment.this.mQueueCursor.getColumnIndexOrThrow("album_id");
          int i1 = NowPlayingScreenFragment.this.mQueueCursor.getColumnIndexOrThrow("album");
          Cursor localCursor2 = NowPlayingScreenFragment.this.mQueueCursor;
          int i2 = k;
          String str1 = localCursor2.getString(i2);
          Cursor localCursor3 = NowPlayingScreenFragment.this.mQueueCursor;
          int i3 = i1;
          String str2 = localCursor3.getString(i3);
          Cursor localCursor4 = NowPlayingScreenFragment.this.mQueueCursor;
          int i4 = m;
          String str3 = localCursor4.getString(i4);
          long l = NowPlayingScreenFragment.this.mQueueCursor.getLong(n);
          if (MusicUtils.isUnknown(str2))
            str2 = NowPlayingScreenFragment.this.getActivity().getString(2131230891);
          if (MusicUtils.isUnknown(str3))
            str3 = NowPlayingScreenFragment.this.getActivity().getString(2131230890);
          String str4 = null;
          if ((NowPlayingScreenFragment.this.mSongList instanceof SharedSongList))
          {
            SharedSongList localSharedSongList = (SharedSongList)NowPlayingScreenFragment.this.mSongList;
            FragmentActivity localFragmentActivity = NowPlayingScreenFragment.this.getActivity();
            str4 = localSharedSongList.getAlbumArtUrl(localFragmentActivity);
          }
          NowPlayingHeaderPageFragment localNowPlayingHeaderPageFragment1 = NowPlayingHeaderPageFragment.newInstance(paramInt, str1, str2, str3, l, str4);
          localNowPlayingHeaderPageFragment2 = localNowPlayingHeaderPageFragment1;
          Cursor localCursor5;
          int i5;
          boolean bool1;
          return localNowPlayingHeaderPageFragment2;
        }
        Cursor localCursor6 = NowPlayingScreenFragment.this.mQueueCursor;
        int i6 = i;
        boolean bool2 = localCursor6.moveToPosition(i6);
        StringBuilder localStringBuilder = new StringBuilder().append("Could not extract metadata for queue position ");
        int i7 = paramInt;
        String str5 = i7;
        int i8 = Log.e("NowPlayingFragment", str5);
        NowPlayingHeaderPageFragment localNowPlayingHeaderPageFragment2 = NowPlayingHeaderPageFragment.newInstance(paramInt, "", "", "", 0L, "");
      }
      finally
      {
        Cursor localCursor7 = NowPlayingScreenFragment.this.mQueueCursor;
        int i9 = i;
        boolean bool3 = localCursor7.moveToPosition(i9);
      }
    }

    public int getItemPosition(Object paramObject)
    {
      return -1;
    }

    public void onPageScrollStateChanged(int paramInt)
    {
      if (paramInt == 0)
      {
        if (!this.mButtonsHidden)
          return;
        NowPlayingScreenFragment.this.mHeaderButtonsWrapper.clearAnimation();
        StatefulAlphaAnimation localStatefulAlphaAnimation = new StatefulAlphaAnimation(0.0F, 1.0F);
        localStatefulAlphaAnimation.setDuration(100L);
        localStatefulAlphaAnimation.setFillAfter(true);
        NowPlayingScreenFragment.this.mHeaderButtonsWrapper.startAnimation(localStatefulAlphaAnimation);
        this.mPageScrolling = false;
        this.mButtonsHidden = false;
        return;
      }
      this.mPageScrolling = true;
    }

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
    {
      if (!this.mPageScrolling)
        return;
      if (this.mButtonsHidden)
        return;
      if ((paramFloat <= 0.01D) || (paramFloat >= 0.5D))
      {
        if (paramFloat >= 0.99D)
          return;
        if (paramFloat <= 0.5D)
          return;
      }
      NowPlayingScreenFragment.this.mHeaderButtonsWrapper.clearAnimation();
      StatefulAlphaAnimation localStatefulAlphaAnimation = new StatefulAlphaAnimation(1.0F, 0.0F);
      localStatefulAlphaAnimation.setDuration(100L);
      localStatefulAlphaAnimation.setFillAfter(true);
      NowPlayingScreenFragment.this.mHeaderButtonsWrapper.startAnimation(localStatefulAlphaAnimation);
      this.mButtonsHidden = true;
    }

    public void onPageSelected(int paramInt)
    {
      NowPlayingScreenFragment.this.handlePageSelected(paramInt);
    }
  }

  private class NowPlayingArtPageAdapter extends FragmentStatePagerAdapter
    implements ViewPager.OnPageChangeListener
  {
    private ViewPager mPager;

    public NowPlayingArtPageAdapter(ViewPager arg2)
    {
      super();
      Object localObject;
      this.mPager = localObject;
      this.mPager.setAdapter(this);
      this.mPager.setOnPageChangeListener(this);
    }

    public int getCount()
    {
      return NowPlayingScreenFragment.this.getPageCount();
    }

    public Fragment getItem(int paramInt)
    {
      MusicUtils.assertUiThread();
      int i;
      if (NowPlayingScreenFragment.this.mQueueCursor != null)
        i = NowPlayingScreenFragment.this.mQueueCursor.getPosition();
      try
      {
        Cursor localCursor1 = NowPlayingScreenFragment.this.mQueueCursor;
        int j = paramInt;
        if (localCursor1.moveToPosition(j))
        {
          int k = NowPlayingScreenFragment.this.mQueueCursor.getColumnIndexOrThrow("title");
          int m = NowPlayingScreenFragment.this.mQueueCursor.getColumnIndexOrThrow("album_id");
          int n = NowPlayingScreenFragment.this.mQueueCursor.getColumnIndexOrThrow("album");
          int i1 = NowPlayingScreenFragment.this.mQueueCursor.getColumnIndexOrThrow("artist");
          Cursor localCursor2 = NowPlayingScreenFragment.this.mQueueCursor;
          int i2 = k;
          String str1 = localCursor2.getString(i2);
          long l = NowPlayingScreenFragment.this.mQueueCursor.getLong(m);
          String str2 = NowPlayingScreenFragment.this.mQueueCursor.getString(n);
          String str3 = NowPlayingScreenFragment.this.mQueueCursor.getString(i1);
          if (MusicUtils.isUnknown(str2))
            str2 = NowPlayingScreenFragment.this.getActivity().getString(2131230891);
          if (MusicUtils.isUnknown(str3))
            str3 = NowPlayingScreenFragment.this.getActivity().getString(2131230890);
          String str4 = null;
          if ((NowPlayingScreenFragment.this.mSongList instanceof SharedSongList))
          {
            SharedSongList localSharedSongList = (SharedSongList)NowPlayingScreenFragment.this.mSongList;
            FragmentActivity localFragmentActivity = NowPlayingScreenFragment.this.getActivity();
            str4 = localSharedSongList.getAlbumArtUrl(localFragmentActivity);
          }
          NowPlayingArtPageFragment localNowPlayingArtPageFragment1 = NowPlayingArtPageFragment.newInstance(paramInt, str2, str3, l, str4);
          localNowPlayingArtPageFragment2 = localNowPlayingArtPageFragment1;
          Cursor localCursor3;
          int i3;
          boolean bool1;
          return localNowPlayingArtPageFragment2;
        }
        Cursor localCursor4 = NowPlayingScreenFragment.this.mQueueCursor;
        int i4 = i;
        boolean bool2 = localCursor4.moveToPosition(i4);
        StringBuilder localStringBuilder = new StringBuilder().append("Could not extract metadata for queue position ");
        int i5 = paramInt;
        String str5 = i5;
        int i6 = Log.e("NowPlayingFragment", str5);
        NowPlayingArtPageFragment localNowPlayingArtPageFragment2 = NowPlayingArtPageFragment.newInstance(paramInt, "", "", 0L, "");
      }
      finally
      {
        Cursor localCursor5 = NowPlayingScreenFragment.this.mQueueCursor;
        int i7 = i;
        boolean bool3 = localCursor5.moveToPosition(i7);
      }
    }

    public int getItemPosition(Object paramObject)
    {
      return -1;
    }

    public void onPageScrollStateChanged(int paramInt)
    {
    }

    public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
    {
    }

    public void onPageSelected(int paramInt)
    {
      NowPlayingScreenFragment.this.handlePageSelected(paramInt);
    }
  }

  private static enum LightsState
  {
    static
    {
      TRANSITIONING_DOWN = new LightsState("TRANSITIONING_DOWN", 2);
      ON = new LightsState("ON", 3);
      LightsState[] arrayOfLightsState = new LightsState[4];
      LightsState localLightsState1 = OFF;
      arrayOfLightsState[0] = localLightsState1;
      LightsState localLightsState2 = TRANSITIONING_UP;
      arrayOfLightsState[1] = localLightsState2;
      LightsState localLightsState3 = TRANSITIONING_DOWN;
      arrayOfLightsState[2] = localLightsState3;
      LightsState localLightsState4 = ON;
      arrayOfLightsState[3] = localLightsState4;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.NowPlayingScreenFragment
 * JD-Core Version:    0.6.2
 */