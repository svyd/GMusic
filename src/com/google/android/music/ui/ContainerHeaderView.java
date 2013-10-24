package com.google.android.music.ui;

import android.animation.LayoutTransition;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.google.android.music.AsyncAlbumArtImageView;
import com.google.android.music.KeepOnView;
import com.google.android.music.download.IntentConstants;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.AlbumSongList;
import com.google.android.music.medialist.ArtistSongList;
import com.google.android.music.medialist.AutoPlaylist;
import com.google.android.music.medialist.GenreSongList;
import com.google.android.music.medialist.NautilusAlbumSongList;
import com.google.android.music.medialist.NautilusSongList;
import com.google.android.music.medialist.PlaylistSongList;
import com.google.android.music.medialist.SharedSongList;
import com.google.android.music.medialist.SharedWithMeSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent;
import com.google.android.music.store.MusicContent.Albums;
import com.google.android.music.store.Store;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.utils.BitmapDiskCache;
import com.google.android.music.utils.BitmapDiskCache.Callback;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class ContainerHeaderView extends RelativeLayout
  implements View.OnClickListener, AbsListView.OnScrollListener
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.UI);
  BaseActionButton[] mActionButtons;
  private ViewGroup mActionsContainer;
  private ProgressBar mAddToLibSpinner;
  private BaseActionButton mAddToLibrary;
  private ImageView mAddedToLibBadge;
  private final ContentObserver mAddedToLibraryStateObserver;
  private AsyncAlbumArtImageView mAlbumArt;
  private TextView mAlbumYear;
  private TextView mAlbumYearHyphen;
  private int mAlbumYearValue = 0;
  private boolean mArtClearedOnStop = false;
  private ImageView mArtistArt1;
  private boolean mArtistArt1ShowingNonDefaultArt;
  private ImageView mArtistArt2;
  private final ArtHelper mArtistArtHelper1;
  private final ArtHelper mArtistArtHelper2;
  private String mArtistArtUrl;
  private ArrayList<String> mArtistArtUrls;
  private long mArtistId = 65535L;
  private String mArtistMetajamId;
  private View mBuyButton;
  private Document mContainerDocument;
  private int mCurrentArtistArtUrlIndex;
  private BaseActionButton mFollowPlaylist;
  private BaseListFragment mFragment;
  private boolean mIsAddedToLibraryObserverRegistered;
  private MusicPreferences mMusicPreferences;
  private View mOverflow;
  private AsyncAlbumArtImageView mOwnerPhoto;
  private KeepOnView mPinButton;
  private BaseActionButton mPlayRadio;
  private String mPrimaryTitle;
  private String mSecondaryTitle;
  private BaseActionButton mSharePlaylist;
  private boolean mShowingFirstSlide = false;
  private BaseActionButton mShuffle;
  private Runnable mSlideRefreshRunnable;
  private Handler mSlideShowHandler;
  private boolean mSlideShowInitialized = false;
  private Runnable mSlideSwitchRunnable;
  private TextView mSongCount;
  private SongList mSongList;
  private TextView mSubtitle;
  private TextView mTitle;
  private final AsyncRunner mUpdateAddToLibraryButtonRunner;
  private ViewFlipper mViewFlipper;

  public ContainerHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    ArtHelper localArtHelper1 = new ArtHelper(2131296362);
    this.mArtistArtHelper1 = localArtHelper1;
    ArtHelper localArtHelper2 = new ArtHelper(2131296363);
    this.mArtistArtHelper2 = localArtHelper2;
    AsyncRunner local1 = new AsyncRunner()
    {
      private boolean mAllInLibrary;

      public void backgroundTask()
      {
        NautilusSongList localNautilusSongList = (NautilusSongList)ContainerHeaderView.this.mSongList;
        Context localContext = ContainerHeaderView.this.getContext();
        boolean bool = localNautilusSongList.isAllInLibrary(localContext);
        this.mAllInLibrary = bool;
      }

      public void taskCompleted()
      {
        if (ContainerHeaderView.this.mFragment.getActivity() == null)
          return;
        ContainerHeaderView localContainerHeaderView = ContainerHeaderView.this;
        if (!this.mAllInLibrary);
        for (boolean bool = true; ; bool = false)
        {
          localContainerHeaderView.updateAddToLibraryButtonVisibility(bool);
          if (!this.mAllInLibrary)
            return;
          ContainerHeaderView.this.enablePinning();
          return;
        }
      }
    };
    this.mUpdateAddToLibraryButtonRunner = local1;
    this.mIsAddedToLibraryObserverRegistered = false;
    Handler localHandler = new Handler();
    ContentObserver local2 = new ContentObserver(localHandler)
    {
      public void onChange(boolean paramAnonymousBoolean)
      {
        if (MusicPreferences.isHoneycombOrGreater())
        {
          ViewGroup localViewGroup = ContainerHeaderView.this.mActionsContainer;
          LayoutTransition localLayoutTransition = new LayoutTransition();
          localViewGroup.setLayoutTransition(localLayoutTransition);
        }
        MusicUtils.runAsyncWithCallback(ContainerHeaderView.this.mUpdateAddToLibraryButtonRunner);
      }
    };
    this.mAddedToLibraryStateObserver = local2;
  }

  private void clearArtistArt(ImageView paramImageView)
  {
    Drawable localDrawable = paramImageView.getDrawable();
    if ((localDrawable != null) && ((localDrawable instanceof BitmapDrawable)))
      ((BitmapDrawable)localDrawable).getBitmap().recycle();
    paramImageView.setImageDrawable(null);
  }

  private void disablePinning()
  {
    this.mPinButton.setVisibility(8);
    this.mPinButton.setOnClick(false);
  }

  private void enablePinning()
  {
    this.mPinButton.setVisibility(0);
    KeepOnView localKeepOnView = this.mPinButton;
    SongList localSongList = this.mSongList;
    localKeepOnView.setSongList(localSongList);
    this.mPinButton.setOnClick(true);
  }

  private void extractArtistArtUrls(Cursor paramCursor, int paramInt)
  {
    long l = SystemClock.uptimeMillis();
    int i = paramCursor.getColumnIndexOrThrow("ArtistArtLocation");
    int j = paramCursor.getPosition();
    boolean bool1 = paramCursor.moveToPosition(-1);
    HashSet localHashSet = new HashSet();
    int k = 0;
    if ((paramCursor.moveToNext()) && (localHashSet.size() < paramInt) && (k < 1000))
    {
      if ((i >= 0) && (!paramCursor.isNull(i)));
      for (String str1 = paramCursor.getString(i); ; str1 = null)
      {
        if (!TextUtils.isEmpty(str1))
          boolean bool2 = localHashSet.add(str1);
        k += 1;
        break;
      }
    }
    if ((!paramCursor.moveToPosition(j)) && (j != -1))
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Failed to restore cursor position. Current pos=");
      int m = paramCursor.getPosition();
      String str2 = m;
      Log.w("ContainerHeader", str2);
    }
    int n = localHashSet.size();
    ArrayList localArrayList = new ArrayList(n);
    this.mArtistArtUrls = localArrayList;
    boolean bool3 = this.mArtistArtUrls.addAll(localHashSet);
    if (!LOGV)
      return;
    Object[] arrayOfObject = new Object[3];
    Integer localInteger1 = Integer.valueOf(this.mArtistArtUrls.size());
    arrayOfObject[0] = localInteger1;
    Long localLong = Long.valueOf(SystemClock.uptimeMillis() - l);
    arrayOfObject[1] = localLong;
    Integer localInteger2 = Integer.valueOf(k);
    arrayOfObject[2] = localInteger2;
    String str3 = String.format("Gathering %d artist urls took: %d ms. Looked at %d rows.", arrayOfObject);
    Log.d("ContainerHeader", str3);
  }

  private String getFirstArtistUrl()
  {
    this.mCurrentArtistArtUrlIndex = 0;
    ArrayList localArrayList = this.mArtistArtUrls;
    int i = this.mCurrentArtistArtUrlIndex;
    return (String)localArrayList.get(i);
  }

  private String getNextArtistUrl()
  {
    int i = this.mCurrentArtistArtUrlIndex + 1;
    int j = this.mArtistArtUrls.size();
    int k = i % j;
    this.mCurrentArtistArtUrlIndex = k;
    ArrayList localArrayList = this.mArtistArtUrls;
    int m = this.mCurrentArtistArtUrlIndex;
    return (String)localArrayList.get(m);
  }

  private boolean isOnDeviceOnlyMode()
  {
    boolean bool = true;
    if ((this.mMusicPreferences != null) && (this.mMusicPreferences.getDisplayOptions() == 1));
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  private void setupArtistArtSlideShow(Cursor paramCursor)
  {
    if (this.mSlideShowInitialized)
      return;
    if (paramCursor == null)
      return;
    if (!MusicUtils.hasCount(paramCursor))
      return;
    if (this.mSlideShowHandler == null)
    {
      Handler localHandler = new Handler();
      this.mSlideShowHandler = localHandler;
    }
    if (this.mSlideSwitchRunnable == null)
    {
      Runnable local8 = new Runnable()
      {
        public void run()
        {
          Handler localHandler1 = ContainerHeaderView.this.mSlideShowHandler;
          Runnable localRunnable1 = ContainerHeaderView.this.mSlideSwitchRunnable;
          localHandler1.removeCallbacks(localRunnable1);
          Handler localHandler2 = ContainerHeaderView.this.mSlideShowHandler;
          Runnable localRunnable2 = ContainerHeaderView.this.mSlideRefreshRunnable;
          localHandler2.removeCallbacks(localRunnable2);
          ContainerHeaderView.ArtHelper localArtHelper;
          ContainerHeaderView localContainerHeaderView;
          if (ContainerHeaderView.this.mShowingFirstSlide)
          {
            localArtHelper = ContainerHeaderView.this.mArtistArtHelper2;
            if (ContainerHeaderView.LOGV)
            {
              StringBuilder localStringBuilder1 = new StringBuilder().append("Slide switch. Switching to: ");
              String str1 = localArtHelper.getViewName();
              StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(" is ready: ");
              boolean bool1 = localArtHelper.isViewReady();
              String str2 = bool1;
              Log.d("ContainerHeader", str2);
            }
            ContainerHeaderView.this.mViewFlipper.showNext();
            localContainerHeaderView = ContainerHeaderView.this;
            if (ContainerHeaderView.this.mShowingFirstSlide)
              break label241;
          }
          label241: for (boolean bool2 = true; ; bool2 = false)
          {
            boolean bool3 = ContainerHeaderView.access$2702(localContainerHeaderView, bool2);
            Handler localHandler3 = ContainerHeaderView.this.mSlideShowHandler;
            Runnable localRunnable3 = ContainerHeaderView.this.mSlideRefreshRunnable;
            boolean bool4 = localHandler3.postDelayed(localRunnable3, 1600L);
            Handler localHandler4 = ContainerHeaderView.this.mSlideShowHandler;
            Runnable localRunnable4 = ContainerHeaderView.this.mSlideSwitchRunnable;
            boolean bool5 = localHandler4.postDelayed(localRunnable4, 5000L);
            return;
            localArtHelper = ContainerHeaderView.this.mArtistArtHelper1;
            break;
          }
        }
      };
      this.mSlideSwitchRunnable = local8;
    }
    if (this.mSlideRefreshRunnable == null)
    {
      Runnable local9 = new Runnable()
      {
        public void run()
        {
          if ((ContainerHeaderView.this.mArtistArtUrls == null) || (ContainerHeaderView.this.mArtistArtUrls.isEmpty()))
          {
            Log.e("ContainerHeader", "mSlideRefreshRunnable: the artist url list is empty");
            return;
          }
          if (ContainerHeaderView.LOGV)
          {
            StringBuilder localStringBuilder = new StringBuilder().append("Slide refresh. Showing first slide=");
            boolean bool = ContainerHeaderView.this.mShowingFirstSlide;
            String str1 = bool;
            Log.d("ContainerHeader", str1);
          }
          ImageView localImageView;
          if (ContainerHeaderView.this.mShowingFirstSlide)
          {
            localImageView = ContainerHeaderView.this.mArtistArt2;
            if (!ContainerHeaderView.this.mShowingFirstSlide)
              break label149;
          }
          label149: for (ContainerHeaderView.ArtHelper localArtHelper = ContainerHeaderView.this.mArtistArtHelper2; ; localArtHelper = ContainerHeaderView.this.mArtistArtHelper1)
          {
            ContainerHeaderView.this.clearArtistArt(localImageView);
            String str2 = ContainerHeaderView.this.getNextArtistUrl();
            localArtHelper.requestBitmap(str2);
            return;
            localImageView = ContainerHeaderView.this.mArtistArt1;
            break;
          }
        }
      };
      this.mSlideRefreshRunnable = local9;
    }
    Collections.shuffle(this.mArtistArtUrls);
    this.mSlideShowInitialized = true;
    startArtistArtSlideShow();
  }

  private boolean shouldDoArtistSlideShow()
  {
    if ((shouldTryArtistSlideShow()) && (this.mArtistArtUrls != null) && (this.mArtistArtUrls.size() >= 2));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private boolean shouldShowSongCount()
  {
    boolean bool1 = false;
    if (this.mSongList == null);
    while (true)
    {
      return bool1;
      if ((this.mSongList instanceof PlaylistSongList))
      {
        if (((PlaylistSongList)this.mSongList).getPlaylistType() != 50);
        for (boolean bool2 = true; ; bool2 = false)
        {
          bool1 = bool2;
          break;
        }
      }
      bool1 = true;
    }
  }

  private boolean shouldTryArtistSlideShow()
  {
    if (((this.mSongList instanceof PlaylistSongList)) || ((this.mSongList instanceof AutoPlaylist)) || ((this.mSongList instanceof SharedWithMeSongList)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private void showButtonIfXLarge(View paramView)
  {
    if (!this.mFragment.getPreferences().isXLargeScreen())
      return;
    paramView.setVisibility(0);
  }

  private void showOwnerProfilePicture(String paramString)
  {
    if (!UIStateManager.getInstance().isNetworkConnected())
      return;
    if (TextUtils.isEmpty(paramString))
      return;
    this.mOwnerPhoto.setVisibility(0);
    this.mOwnerPhoto.setExternalAlbumArt(paramString);
  }

  private void startArtistArtSlideShow()
  {
    if (!this.mSlideShowInitialized)
      return;
    if (this.mArtistArtUrls == null)
      return;
    if (this.mArtistArtUrls.size() < 2)
    {
      if (!LOGV)
        return;
      Log.d("ContainerHeader", "Not enough artists to do a slide show");
      return;
    }
    if (this.mViewFlipper.getDisplayedChild() != 0)
      this.mViewFlipper.showNext();
    String str1 = getFirstArtistUrl();
    this.mArtistArtHelper1.requestBitmap(str1);
    String str2 = getNextArtistUrl();
    this.mArtistArtHelper2.requestBitmap(str2);
    ViewFlipper localViewFlipper1 = this.mViewFlipper;
    Context localContext1 = getContext();
    localViewFlipper1.setInAnimation(localContext1, 2131034118);
    ViewFlipper localViewFlipper2 = this.mViewFlipper;
    Context localContext2 = getContext();
    localViewFlipper2.setOutAnimation(localContext2, 2131034119);
    this.mViewFlipper.getInAnimation().setDuration(1500L);
    this.mViewFlipper.getOutAnimation().setDuration(1500L);
    this.mShowingFirstSlide = true;
    Handler localHandler1 = this.mSlideShowHandler;
    Runnable localRunnable1 = this.mSlideSwitchRunnable;
    localHandler1.removeCallbacks(localRunnable1);
    Handler localHandler2 = this.mSlideShowHandler;
    Runnable localRunnable2 = this.mSlideRefreshRunnable;
    localHandler2.removeCallbacks(localRunnable2);
    Handler localHandler3 = this.mSlideShowHandler;
    Runnable localRunnable3 = this.mSlideSwitchRunnable;
    boolean bool = localHandler3.postDelayed(localRunnable3, 5000L);
  }

  private void updateActionButtonsVisibility()
  {
    if ((this.mSongList instanceof PlaylistSongList))
    {
      BaseActionButton localBaseActionButton1 = this.mShuffle;
      showButtonIfXLarge(localBaseActionButton1);
      return;
    }
    if ((this.mSongList instanceof SharedWithMeSongList))
    {
      this.mFollowPlaylist.setVisibility(0);
      BaseActionButton localBaseActionButton2 = this.mFollowPlaylist;
      BaseActionButton.ActionButtonListener local5 = new BaseActionButton.ActionButtonListener()
      {
        public void onActionFinish()
        {
          ContainerHeaderView.this.mFollowPlaylist.setVisibility(8);
        }

        public void onActionStart()
        {
        }
      };
      localBaseActionButton2.setActionButtonListener(local5);
      BaseActionButton localBaseActionButton3 = this.mShuffle;
      showButtonIfXLarge(localBaseActionButton3);
      return;
    }
    if ((this.mSongList instanceof NautilusSongList))
    {
      if (!this.mIsAddedToLibraryObserverRegistered)
      {
        this.mIsAddedToLibraryObserverRegistered = true;
        ContentResolver localContentResolver = getContext().getContentResolver();
        Uri localUri = MusicContent.CONTENT_URI;
        ContentObserver localContentObserver = this.mAddedToLibraryStateObserver;
        localContentResolver.registerContentObserver(localUri, false, localContentObserver);
      }
      this.mAddToLibrary.setVisibility(4);
      MusicUtils.runAsyncWithCallback(this.mUpdateAddToLibraryButtonRunner);
      return;
    }
    if ((this.mSongList instanceof SharedSongList))
      return;
    if ((this.mSongList instanceof AutoPlaylist))
    {
      BaseActionButton localBaseActionButton4 = this.mShuffle;
      showButtonIfXLarge(localBaseActionButton4);
      return;
    }
    if (((this.mSongList instanceof AlbumSongList)) || ((this.mSongList instanceof ArtistSongList)))
    {
      if (UIStateManager.getInstance().getPrefs().hasStreamingAccount())
      {
        BaseActionButton localBaseActionButton5 = this.mPlayRadio;
        showButtonIfXLarge(localBaseActionButton5);
      }
      BaseActionButton localBaseActionButton6 = this.mShuffle;
      showButtonIfXLarge(localBaseActionButton6);
      return;
    }
    if (!(this.mSongList instanceof GenreSongList))
      return;
    BaseActionButton localBaseActionButton7 = this.mShuffle;
    showButtonIfXLarge(localBaseActionButton7);
  }

  private void updateAddToLibraryButtonVisibility(boolean paramBoolean)
  {
    int i;
    if (paramBoolean)
    {
      i = 0;
      if (this.mAddToLibrary.getVisibility() != i)
      {
        this.mAddToLibrary.setVisibility(i);
        if (paramBoolean)
          break label66;
        this.mAddedToLibBadge.setVisibility(0);
      }
    }
    while (true)
    {
      BaseActionButton localBaseActionButton1 = this.mPlayRadio;
      showButtonIfXLarge(localBaseActionButton1);
      BaseActionButton localBaseActionButton2 = this.mShuffle;
      showButtonIfXLarge(localBaseActionButton2);
      return;
      i = 8;
      break;
      label66: this.mAddedToLibBadge.setVisibility(8);
      BaseActionButton localBaseActionButton3 = this.mAddToLibrary;
      BaseActionButton.ActionButtonListener local6 = new BaseActionButton.ActionButtonListener()
      {
        public void onActionFinish()
        {
          ContainerHeaderView.this.mAlbumArt.setAvailable(true);
          ContainerHeaderView.this.mAddToLibSpinner.setVisibility(8);
        }

        public void onActionStart()
        {
          ContainerHeaderView.this.mAlbumArt.setAvailable(false);
          ContainerHeaderView.this.mAddToLibSpinner.setVisibility(0);
        }
      };
      localBaseActionButton3.setActionButtonListener(local6);
    }
  }

  public int getAlbumArtHeight()
  {
    return this.mAlbumArt.getHeight();
  }

  public boolean isArtistArtShown()
  {
    if (this.mViewFlipper.getVisibility() == 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void onClick(final View paramView)
  {
    final Context localContext = getContext();
    AsyncAlbumArtImageView localAsyncAlbumArtImageView = this.mAlbumArt;
    if (paramView == localAsyncAlbumArtImageView)
    {
      MusicUtils.playMediaList(this.mSongList, -1);
      return;
    }
    View localView1 = this.mBuyButton;
    if (paramView == localView1)
    {
      String str = this.mSongList.getStoreUrl();
      if (TextUtils.isEmpty(str))
        return;
      Intent localIntent = IntentConstants.getStoreBuyIntent(localContext, str);
      localContext.startActivity(localIntent);
      return;
    }
    View localView2 = this.mOverflow;
    if (paramView != localView2)
      return;
    if (this.mContainerDocument != null)
    {
      BaseListFragment localBaseListFragment = this.mFragment;
      Document localDocument = this.mContainerDocument;
      ScreenMenuHandler.ScreenMenuType localScreenMenuType = ScreenMenuHandler.ScreenMenuType.TRACK_CONTAINER;
      new ScreenMenuHandler(localBaseListFragment, localDocument, localScreenMenuType).showPopupMenu(paramView);
      return;
    }
    MusicUtils.runAsyncWithCallback(new AsyncRunner()
    {
      public void backgroundTask()
      {
        ContainerHeaderView localContainerHeaderView = ContainerHeaderView.this;
        Context localContext = localContext;
        SongList localSongList = ContainerHeaderView.this.mSongList;
        Document localDocument1 = Document.fromSongList(localContext, localSongList);
        Document localDocument2 = ContainerHeaderView.access$2302(localContainerHeaderView, localDocument1);
      }

      public void taskCompleted()
      {
        if (ContainerHeaderView.this.mFragment.getActivity() == null)
          return;
        if (ContainerHeaderView.this.mContainerDocument.getType() == null)
          return;
        BaseListFragment localBaseListFragment = ContainerHeaderView.this.mFragment;
        Document localDocument = ContainerHeaderView.this.mContainerDocument;
        ScreenMenuHandler.ScreenMenuType localScreenMenuType = ScreenMenuHandler.ScreenMenuType.TRACK_CONTAINER;
        ScreenMenuHandler localScreenMenuHandler = new ScreenMenuHandler(localBaseListFragment, localDocument, localScreenMenuType);
        View localView = paramView;
        localScreenMenuHandler.showPopupMenu(localView);
      }
    });
  }

  public void onDestroyView()
  {
    if (!this.mIsAddedToLibraryObserverRegistered)
      return;
    ContentResolver localContentResolver = getContext().getContentResolver();
    ContentObserver localContentObserver = this.mAddedToLibraryStateObserver;
    localContentResolver.unregisterContentObserver(localContentObserver);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    ImageView localImageView1 = (ImageView)findViewById(2131296362);
    this.mArtistArt1 = localImageView1;
    ImageView localImageView2 = (ImageView)findViewById(2131296363);
    this.mArtistArt2 = localImageView2;
    AsyncAlbumArtImageView localAsyncAlbumArtImageView1 = (AsyncAlbumArtImageView)findViewById(2131296345);
    this.mAlbumArt = localAsyncAlbumArtImageView1;
    ViewFlipper localViewFlipper = (ViewFlipper)findViewById(2131296361);
    this.mViewFlipper = localViewFlipper;
    BaseActionButton localBaseActionButton1 = (BaseActionButton)findViewById(2131296368);
    this.mSharePlaylist = localBaseActionButton1;
    BaseActionButton localBaseActionButton2 = (BaseActionButton)findViewById(2131296369);
    this.mFollowPlaylist = localBaseActionButton2;
    BaseActionButton localBaseActionButton3 = (BaseActionButton)findViewById(2131296370);
    this.mAddToLibrary = localBaseActionButton3;
    BaseActionButton localBaseActionButton4 = (BaseActionButton)findViewById(2131296351);
    this.mPlayRadio = localBaseActionButton4;
    BaseActionButton localBaseActionButton5 = (BaseActionButton)findViewById(2131296371);
    this.mShuffle = localBaseActionButton5;
    View localView1 = findViewById(2131296372);
    this.mBuyButton = localView1;
    this.mBuyButton.setOnClickListener(this);
    View localView2 = findViewById(2131296374);
    this.mOverflow = localView2;
    this.mOverflow.setOnClickListener(this);
    KeepOnView localKeepOnView = (KeepOnView)findViewById(2131296373);
    this.mPinButton = localKeepOnView;
    ViewGroup localViewGroup = (ViewGroup)findViewById(2131296366);
    this.mActionsContainer = localViewGroup;
    BaseActionButton[] arrayOfBaseActionButton = new BaseActionButton[5];
    BaseActionButton localBaseActionButton6 = this.mShuffle;
    arrayOfBaseActionButton[0] = localBaseActionButton6;
    BaseActionButton localBaseActionButton7 = this.mPlayRadio;
    arrayOfBaseActionButton[1] = localBaseActionButton7;
    BaseActionButton localBaseActionButton8 = this.mAddToLibrary;
    arrayOfBaseActionButton[2] = localBaseActionButton8;
    BaseActionButton localBaseActionButton9 = this.mFollowPlaylist;
    arrayOfBaseActionButton[3] = localBaseActionButton9;
    BaseActionButton localBaseActionButton10 = this.mSharePlaylist;
    arrayOfBaseActionButton[4] = localBaseActionButton10;
    this.mActionButtons = arrayOfBaseActionButton;
    TextView localTextView1 = (TextView)findViewById(2131296326);
    this.mTitle = localTextView1;
    TextView localTextView2 = (TextView)findViewById(2131296376);
    this.mSubtitle = localTextView2;
    TextView localTextView3 = (TextView)findViewById(2131296377);
    this.mSongCount = localTextView3;
    TextView localTextView4 = (TextView)findViewById(2131296379);
    this.mAlbumYear = localTextView4;
    TextView localTextView5 = (TextView)findViewById(2131296378);
    this.mAlbumYearHyphen = localTextView5;
    AsyncAlbumArtImageView localAsyncAlbumArtImageView2 = (AsyncAlbumArtImageView)findViewById(2131296375);
    this.mOwnerPhoto = localAsyncAlbumArtImageView2;
    ProgressBar localProgressBar = (ProgressBar)findViewById(2131296382);
    this.mAddToLibSpinner = localProgressBar;
    ImageView localImageView3 = (ImageView)findViewById(2131296380);
    this.mAddedToLibBadge = localImageView3;
    if (!UIStateManager.getInstance().getPrefs().isTabletMusicExperience())
      return;
    this.mAddedToLibBadge.setImageResource(2130837722);
  }

  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
  {
    if (!MusicPreferences.isHoneycombOrGreater())
      return;
    View localView = paramAbsListView.getChildAt(0);
    if (localView != this)
      return;
    int i = -localView.getTop() / 2;
    ViewFlipper localViewFlipper = this.mViewFlipper;
    float f = i;
    localViewFlipper.setTranslationY(f);
  }

  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
  {
  }

  public void onStart()
  {
    if (this.mArtClearedOnStop)
    {
      if (!shouldDoArtistSlideShow())
        break label24;
      startArtistArtSlideShow();
    }
    while (true)
    {
      this.mArtClearedOnStop = false;
      return;
      label24: if ((this.mArtistArtUrls != null) && (this.mArtistArtUrls.size() > 0) && (this.mArtistArt1.getDrawable() == null))
      {
        ArtHelper localArtHelper1 = this.mArtistArtHelper1;
        String str1 = (String)this.mArtistArtUrls.get(0);
        localArtHelper1.requestBitmap(str1);
      }
      else if (!TextUtils.isEmpty(this.mArtistArtUrl))
      {
        ArtHelper localArtHelper2 = this.mArtistArtHelper1;
        String str2 = this.mArtistArtUrl;
        localArtHelper2.requestBitmap(str2);
      }
    }
  }

  public void onStop()
  {
    if (this.mArtistArt1ShowingNonDefaultArt)
    {
      ImageView localImageView1 = this.mArtistArt1;
      clearArtistArt(localImageView1);
      this.mArtistArt1ShowingNonDefaultArt = false;
    }
    ImageView localImageView2 = this.mArtistArt2;
    clearArtistArt(localImageView2);
    if (this.mSlideShowInitialized)
    {
      Handler localHandler1 = this.mSlideShowHandler;
      Runnable localRunnable1 = this.mSlideRefreshRunnable;
      localHandler1.removeCallbacks(localRunnable1);
      Handler localHandler2 = this.mSlideShowHandler;
      Runnable localRunnable2 = this.mSlideSwitchRunnable;
      localHandler2.removeCallbacks(localRunnable2);
    }
    this.mArtClearedOnStop = true;
  }

  public void setContainerDocument(Document paramDocument)
  {
    this.mContainerDocument = paramDocument;
  }

  public void setCursor(Cursor paramCursor)
  {
    if (paramCursor == null)
      return;
    String str1;
    if (this.mSongCount != null)
    {
      if ((!shouldShowSongCount()) || (!MusicUtils.hasCount(paramCursor)))
        break label198;
      int i = paramCursor.getCount();
      Resources localResources1 = getResources();
      Object[] arrayOfObject1 = new Object[1];
      Integer localInteger = Integer.valueOf(i);
      arrayOfObject1[0] = localInteger;
      str1 = localResources1.getQuantityString(2131623936, i, arrayOfObject1);
      if ((!this.mSongList.getShouldFilter()) || (!isOnDeviceOnlyMode()))
        break label186;
      Resources localResources2 = getResources();
      Object[] arrayOfObject2 = new Object[2];
      arrayOfObject2[0] = str1;
      CharSequence localCharSequence = getResources().getText(2131231037);
      arrayOfObject2[1] = localCharSequence;
      String str2 = localResources2.getString(2131231038, arrayOfObject2);
      this.mSongCount.setText(str2);
    }
    while (true)
    {
      if (!shouldTryArtistSlideShow())
        return;
      if (this.mArtistArtUrls != null)
        return;
      if (!MusicUtils.hasCount(paramCursor))
        return;
      extractArtistArtUrls(paramCursor, 10);
      if (!shouldDoArtistSlideShow())
        break;
      setupArtistArtSlideShow(paramCursor);
      return;
      label186: this.mSongCount.setText(str1);
      continue;
      label198: this.mSongCount.setText(null);
    }
    if (this.mArtistArtUrls.size() <= 0)
      return;
    ArtHelper localArtHelper = this.mArtistArtHelper1;
    String str3 = (String)this.mArtistArtUrls.get(0);
    localArtHelper.requestBitmap(str3);
  }

  public void setFragment(BaseListFragment paramBaseListFragment)
  {
    this.mFragment = paramBaseListFragment;
  }

  public void setMusicPreferences(MusicPreferences paramMusicPreferences)
  {
    this.mMusicPreferences = paramMusicPreferences;
  }

  public void setSongList(SongList paramSongList)
  {
    this.mSongList = paramSongList;
    if (!paramSongList.hasArtistArt())
      this.mViewFlipper.setVisibility(8);
    updateActionButtonsVisibility();
    BaseActionButton localBaseActionButton1 = this.mSharePlaylist;
    SongList localSongList1 = this.mSongList;
    localBaseActionButton1.setMediaList(localSongList1);
    BaseActionButton localBaseActionButton2 = this.mFollowPlaylist;
    SongList localSongList2 = this.mSongList;
    localBaseActionButton2.setMediaList(localSongList2);
    BaseActionButton localBaseActionButton3 = this.mAddToLibrary;
    SongList localSongList3 = this.mSongList;
    localBaseActionButton3.setMediaList(localSongList3);
    BaseActionButton localBaseActionButton4 = this.mPlayRadio;
    SongList localSongList4 = this.mSongList;
    localBaseActionButton4.setMediaList(localSongList4);
    BaseActionButton localBaseActionButton5 = this.mShuffle;
    SongList localSongList5 = this.mSongList;
    localBaseActionButton5.setMediaList(localSongList5);
    AsyncAlbumArtImageView localAsyncAlbumArtImageView = this.mAlbumArt;
    View.OnClickListener local3 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MusicUtils.playMediaList(ContainerHeaderView.this.mSongList, -1);
      }
    };
    localAsyncAlbumArtImageView.setOnClickListener(local3);
    if (this.mPinButton != null)
    {
      if ((!this.mSongList.supportsOfflineCaching()) || ((this.mSongList instanceof NautilusSongList)))
        break label217;
      enablePinning();
    }
    while (true)
    {
      if ((this.mSongList instanceof SharedSongList))
      {
        this.mOverflow.setVisibility(8);
        this.mBuyButton.setVisibility(0);
      }
      final Context localContext = getContext();
      MusicUtils.runAsyncWithCallback(new AsyncRunner()
      {
        private int mSavedAlbumYearValue;
        private String mSavedArtistArtUrl;
        private long mSavedArtistId;
        private String mSavedArtistMetajamId;
        private String mSavedPrimaryTitle;
        private String mSavedSecondaryTitle;
        private final SongList mSavedSongList;

        public void backgroundTask()
        {
          SongList localSongList1 = this.mSavedSongList;
          SongList localSongList2 = ContainerHeaderView.this.mSongList;
          if (localSongList1 != localSongList2)
            return;
          SongList localSongList3 = this.mSavedSongList;
          Context localContext1 = localContext;
          String str1 = localSongList3.getName(localContext1);
          this.mSavedPrimaryTitle = str1;
          SongList localSongList4 = this.mSavedSongList;
          Context localContext2 = localContext;
          String str2 = localSongList4.getSecondaryName(localContext2);
          this.mSavedSecondaryTitle = str2;
          SongList localSongList5 = this.mSavedSongList;
          Context localContext3 = localContext;
          long l1 = localSongList5.getArtistId(localContext3);
          this.mSavedArtistId = l1;
          Context localContext4 = localContext;
          long l2 = this.mSavedArtistId;
          String str3 = MusicUtils.getArtistArtUrl(localContext4, l2);
          this.mSavedArtistArtUrl = str3;
          if ((ContainerHeaderView.this.mSongList instanceof NautilusAlbumSongList))
          {
            String str4 = ((NautilusAlbumSongList)ContainerHeaderView.this.mSongList).getNautilusId();
            try
            {
              Context localContext5 = localContext;
              Uri localUri = MusicContent.Albums.getNautilusAlbumsUri(str4);
              String[] arrayOfString = new String[1];
              arrayOfString[0] = "ArtistMetajamId";
              localCursor = MusicUtils.query(localContext5, localUri, arrayOfString, null, null, null);
              if ((localCursor != null) && (localCursor.moveToFirst()))
              {
                String str5 = localCursor.getString(0);
                this.mSavedArtistMetajamId = str5;
              }
              Store.safeClose(localCursor);
              Context localContext6 = localContext;
              String str6 = this.mSavedArtistMetajamId;
              String str7 = MusicUtils.getNautilusArtistArtUrl(localContext6, str6);
              this.mSavedArtistArtUrl = str7;
              NautilusAlbumSongList localNautilusAlbumSongList = (NautilusAlbumSongList)ContainerHeaderView.this.mSongList;
              Context localContext7 = localContext;
              int i = localNautilusAlbumSongList.getAlbumYear(localContext7);
              this.mSavedAlbumYearValue = i;
              return;
            }
            finally
            {
              Cursor localCursor;
              Store.safeClose(localCursor);
            }
          }
          if (!(ContainerHeaderView.this.mSongList instanceof AlbumSongList))
            return;
          AlbumSongList localAlbumSongList = (AlbumSongList)ContainerHeaderView.this.mSongList;
          Context localContext8 = localContext;
          int j = localAlbumSongList.getAlbumYear(localContext8);
          this.mSavedAlbumYearValue = j;
        }

        public void taskCompleted()
        {
          SongList localSongList1 = this.mSavedSongList;
          SongList localSongList2 = ContainerHeaderView.this.mSongList;
          if (localSongList1 != localSongList2)
            return;
          if (ContainerHeaderView.this.mFragment.getActivity() == null)
            return;
          ContainerHeaderView localContainerHeaderView1 = ContainerHeaderView.this;
          String str1 = this.mSavedPrimaryTitle;
          String str2 = ContainerHeaderView.access$602(localContainerHeaderView1, str1);
          ContainerHeaderView localContainerHeaderView2 = ContainerHeaderView.this;
          String str3 = this.mSavedSecondaryTitle;
          String str4 = ContainerHeaderView.access$702(localContainerHeaderView2, str3);
          ContainerHeaderView localContainerHeaderView3 = ContainerHeaderView.this;
          long l1 = this.mSavedArtistId;
          long l2 = ContainerHeaderView.access$802(localContainerHeaderView3, l1);
          ContainerHeaderView localContainerHeaderView4 = ContainerHeaderView.this;
          String str5 = this.mSavedArtistMetajamId;
          String str6 = ContainerHeaderView.access$902(localContainerHeaderView4, str5);
          ContainerHeaderView localContainerHeaderView5 = ContainerHeaderView.this;
          String str7 = this.mSavedArtistArtUrl;
          String str8 = ContainerHeaderView.access$1002(localContainerHeaderView5, str7);
          ContainerHeaderView localContainerHeaderView6 = ContainerHeaderView.this;
          int i = this.mSavedAlbumYearValue;
          int j = ContainerHeaderView.access$1102(localContainerHeaderView6, i);
          if ((ContainerHeaderView.this.mSongList instanceof PlaylistSongList))
          {
            PlaylistSongList localPlaylistSongList = (PlaylistSongList)ContainerHeaderView.this.mSongList;
            if (localPlaylistSongList.getPlaylistType() == 71)
            {
              ContainerHeaderView localContainerHeaderView7 = ContainerHeaderView.this;
              String str9 = localPlaylistSongList.getOwnerProfilePhotoUrl();
              localContainerHeaderView7.showOwnerProfilePicture(str9);
            }
            if ((ContainerHeaderView.this.isArtistArtShown()) && (!ContainerHeaderView.this.shouldTryArtistSlideShow()))
            {
              if (!TextUtils.isEmpty(this.mSavedArtistArtUrl))
              {
                ContainerHeaderView.ArtHelper localArtHelper = ContainerHeaderView.this.mArtistArtHelper1;
                String str10 = ContainerHeaderView.this.mArtistArtUrl;
                localArtHelper.requestBitmap(str10);
              }
              ContainerHeaderView.this.mArtistArt1.setBackgroundResource(2130837848);
              ImageView localImageView1 = ContainerHeaderView.this.mArtistArt1;
              View.OnTouchListener local1 = new View.OnTouchListener()
              {
                final int color;

                public boolean onTouch(View paramAnonymous2View, MotionEvent paramAnonymous2MotionEvent)
                {
                  switch (paramAnonymous2MotionEvent.getAction())
                  {
                  case 2:
                  default:
                  case 0:
                  case 1:
                  case 3:
                  }
                  while (true)
                  {
                    return false;
                    Drawable localDrawable = ContainerHeaderView.this.mArtistArt1.getDrawable();
                    if (localDrawable != null)
                    {
                      int i = this.color;
                      PorterDuff.Mode localMode = PorterDuff.Mode.SRC_OVER;
                      localDrawable.setColorFilter(i, localMode);
                      ContainerHeaderView.this.mArtistArt1.invalidate();
                      continue;
                      localDrawable = ContainerHeaderView.this.mArtistArt1.getDrawable();
                      if (localDrawable != null)
                      {
                        localDrawable.clearColorFilter();
                        ContainerHeaderView.this.mArtistArt1.invalidate();
                      }
                    }
                  }
                }
              };
              localImageView1.setOnTouchListener(local1);
              ImageView localImageView2 = ContainerHeaderView.this.mArtistArt1;
              View.OnClickListener local2 = new View.OnClickListener()
              {
                public void onClick(View paramAnonymous2View)
                {
                  if (ContainerHeaderView.this.mArtistId > 65535L)
                  {
                    Context localContext1 = ContainerHeaderView.4.this.val$context;
                    long l = ContainerHeaderView.this.mArtistId;
                    String str1 = ContainerHeaderView.this.mSecondaryTitle;
                    ArtistPageActivity.showArtist(localContext1, l, str1, true);
                    return;
                  }
                  if (TextUtils.isEmpty(ContainerHeaderView.this.mArtistMetajamId))
                    return;
                  Context localContext2 = ContainerHeaderView.4.this.val$context;
                  String str2 = ContainerHeaderView.this.mArtistMetajamId;
                  String str3 = ContainerHeaderView.this.mSecondaryTitle;
                  ArtistPageActivity.showNautilusArtist(localContext2, str2, str3);
                }
              };
              localImageView2.setOnClickListener(local2);
            }
            if (!(ContainerHeaderView.this.mSongList instanceof SharedWithMeSongList))
              break label596;
            AsyncAlbumArtImageView localAsyncAlbumArtImageView1 = ContainerHeaderView.this.mAlbumArt;
            String str11 = ((SharedWithMeSongList)ContainerHeaderView.this.mSongList).getArtUrl();
            localAsyncAlbumArtImageView1.setSharedPlaylistArt(str11);
          }
          while (true)
          {
            TextView localTextView1 = ContainerHeaderView.this.mTitle;
            String str12 = ContainerHeaderView.this.mPrimaryTitle;
            localTextView1.setText(str12);
            TextView localTextView2 = ContainerHeaderView.this.mSubtitle;
            String str13 = ContainerHeaderView.this.mSecondaryTitle;
            localTextView2.setText(str13);
            ActionBarController localActionBarController = ContainerHeaderView.this.mFragment.getActionBarController();
            String str14 = ContainerHeaderView.this.mPrimaryTitle;
            localActionBarController.setActionBarTitle(str14);
            if (ContainerHeaderView.this.mAlbumYearValue == 0)
              return;
            ContainerHeaderView.this.mAlbumYear.setVisibility(0);
            ContainerHeaderView.this.mAlbumYearHyphen.setVisibility(0);
            TextView localTextView3 = ContainerHeaderView.this.mAlbumYear;
            String str15 = Integer.toString(ContainerHeaderView.this.mAlbumYearValue);
            localTextView3.setText(str15);
            TextView localTextView4 = ContainerHeaderView.this.mAlbumYearHyphen;
            String str16 = ContainerHeaderView.this.getResources().getString(2131230724);
            localTextView4.setText(str16);
            return;
            if (!(ContainerHeaderView.this.mSongList instanceof SharedWithMeSongList))
              break;
            SharedWithMeSongList localSharedWithMeSongList = (SharedWithMeSongList)ContainerHeaderView.this.mSongList;
            ContainerHeaderView localContainerHeaderView8 = ContainerHeaderView.this;
            String str17 = localSharedWithMeSongList.getOwnerProfilePhotoUrl();
            localContainerHeaderView8.showOwnerProfilePicture(str17);
            break;
            label596: AsyncAlbumArtImageView localAsyncAlbumArtImageView2 = ContainerHeaderView.this.mAlbumArt;
            SongList localSongList3 = ContainerHeaderView.this.mSongList;
            localAsyncAlbumArtImageView2.setArtForSonglist(localSongList3);
          }
        }
      });
      return;
      label217: disablePinning();
    }
  }

  private class ArtHelper
    implements BitmapDiskCache.Callback
  {
    private long mRequestTime;
    private String mRequestedUrl;
    private final int mViewId;
    private boolean mViewReady;

    ArtHelper(int arg2)
    {
      int i;
      this.mViewId = i;
    }

    public String getViewName()
    {
      if (this.mViewId == 2131296362);
      for (String str = "view1"; ; str = "view2")
        return str;
    }

    public boolean isViewReady()
    {
      return this.mViewReady;
    }

    public void onBitmapResult(String paramString, Bitmap paramBitmap, Exception paramException)
    {
      String str1 = this.mRequestedUrl;
      if (!paramString.equals(str1))
      {
        if (!ContainerHeaderView.LOGV)
          return;
        StringBuilder localStringBuilder1 = new StringBuilder().append("Ignoring stale result in onBitmapResult for ");
        String str2 = getViewName();
        StringBuilder localStringBuilder2 = localStringBuilder1.append(str2).append(" received: ");
        long l1 = Store.generateId(paramString);
        StringBuilder localStringBuilder3 = localStringBuilder2.append(l1).append(" requested: ");
        long l2 = Store.generateId(this.mRequestedUrl);
        String str3 = l2;
        Log.w("ContainerHeader", str3);
        return;
      }
      if (ContainerHeaderView.LOGV)
      {
        StringBuilder localStringBuilder4 = new StringBuilder().append("onBitmapResult for ");
        String str4 = getViewName();
        StringBuilder localStringBuilder5 = localStringBuilder4.append(str4).append(" time: ");
        long l3 = SystemClock.uptimeMillis();
        long l4 = this.mRequestTime;
        long l5 = l3 - l4;
        StringBuilder localStringBuilder6 = localStringBuilder5.append(l5).append(" id: ");
        long l6 = Store.generateId(paramString);
        String str5 = l6;
        Log.d("ContainerHeader", str5);
      }
      if (paramBitmap == null)
        return;
      if (ContainerHeaderView.this.mArtClearedOnStop)
        return;
      if (this.mViewId == 2131296362)
      {
        ContainerHeaderView.this.mArtistArt1.setImageBitmap(paramBitmap);
        boolean bool = ContainerHeaderView.access$3602(ContainerHeaderView.this, true);
      }
      while (true)
      {
        this.mViewReady = true;
        return;
        ContainerHeaderView.this.mArtistArt2.setImageBitmap(paramBitmap);
      }
    }

    public void requestBitmap(String paramString)
    {
      this.mRequestedUrl = paramString;
      this.mViewReady = false;
      long l = SystemClock.uptimeMillis();
      this.mRequestTime = l;
      BitmapDiskCache localBitmapDiskCache = BitmapDiskCache.getInstance(ContainerHeaderView.this.getContext());
      String str = this.mRequestedUrl;
      localBitmapDiskCache.getBitmap(str, this);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.ContainerHeaderView
 * JD-Core Version:    0.6.2
 */