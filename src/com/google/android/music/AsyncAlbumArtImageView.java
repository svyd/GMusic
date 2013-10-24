package com.google.android.music;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewDebug.ExportedProperty;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import com.google.android.common.base.Preconditions;
import com.google.android.music.download.artwork.AlbumArtDownloadServiceConnection;
import com.google.android.music.download.artwork.AlbumIdSink;
import com.google.android.music.download.artwork.ArtDownloadServiceConnection;
import com.google.android.music.download.artwork.ArtDownloadServiceConnection.ArtChangeListener;
import com.google.android.music.download.artwork.ArtIdRecorder.AlbumIdRecorder;
import com.google.android.music.download.artwork.ArtIdRecorder.RemoteArtUrlRecorder;
import com.google.android.music.download.artwork.CachedArtDownloadServiceConnection;
import com.google.android.music.download.artwork.MissingArtHelper;
import com.google.android.music.download.artwork.RemoteUrlSink;
import com.google.android.music.medialist.ExternalSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.ui.UIStateManager;
import com.google.android.music.utils.AlbumArtUtils;
import com.google.android.music.utils.AlbumArtUtils.AlbumIdIteratorFactory;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.MusicUtils;
import java.util.Set;

public class AsyncAlbumArtImageView extends ImageView
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.UI);
  private static final AlbumArtHandler sHandler = new AlbumArtHandler();

  @ViewDebug.ExportedProperty
  private boolean mActualArtworkSet;
  private final ArtDownloadServiceConnection.ArtChangeListener<Long> mAlbumArtChangeListener;
  private boolean mAllowAnimation;
  private boolean mAttachedToWindow;

  @ViewDebug.ExportedProperty
  private boolean mAvailable;
  private Bitmap mBitmap;
  private final ArtDownloadServiceConnection.ArtChangeListener<String> mCachedArtChangeListener;
  private Animation mFadeInAnimation;
  private boolean mIsScrolling;
  private LayerMode mLayerMode;
  private boolean mLayoutAsSquare;

  @ViewDebug.ExportedProperty
  private boolean mLoadingArtworkSet;
  private MissingAlbumArtHelper mMissingAlbumArtHelper;
  private MissingArtHelper<String> mMissingCachedArtHelper;

  @ViewDebug.ExportedProperty
  private Mode mMode = null;
  private final ColorDrawable mNotAvailableImageOverlay;
  private int mRequestedHeight;
  private int mRequestedWidth;
  private boolean mStretchToFill;
  private int mVirtualHeight;
  private int mVirtualWidth;

  public AsyncAlbumArtImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(localContext, paramAttributeSet);
    LayerMode localLayerMode = LayerMode.BACKGROUND;
    this.mLayerMode = localLayerMode;
    this.mActualArtworkSet = false;
    this.mLoadingArtworkSet = false;
    this.mVirtualWidth = 0;
    this.mVirtualHeight = 0;
    this.mRequestedWidth = 0;
    this.mRequestedHeight = 0;
    this.mAllowAnimation = true;
    this.mAvailable = true;
    ArtDownloadServiceConnection.ArtChangeListener local2 = new ArtDownloadServiceConnection.ArtChangeListener()
    {
      public void notifyArtChanged(Long paramAnonymousLong)
      {
        synchronized (AsyncAlbumArtImageView.this)
        {
          boolean bool1 = AsyncAlbumArtImageView.access$402(AsyncAlbumArtImageView.this, false);
          int i = AsyncAlbumArtImageView.access$502(AsyncAlbumArtImageView.this, 0);
          int j = AsyncAlbumArtImageView.access$602(AsyncAlbumArtImageView.this, 0);
          AsyncAlbumArtImageView localAsyncAlbumArtImageView2 = AsyncAlbumArtImageView.this;
          Runnable local1 = new Runnable()
          {
            public void run()
            {
              AsyncAlbumArtImageView.this.makeDrawable();
            }
          };
          boolean bool2 = localAsyncAlbumArtImageView2.post(local1);
          return;
        }
      }
    };
    this.mAlbumArtChangeListener = local2;
    ArtDownloadServiceConnection.ArtChangeListener local3 = new ArtDownloadServiceConnection.ArtChangeListener()
    {
      public void notifyArtChanged(final String paramAnonymousString)
      {
        synchronized (AsyncAlbumArtImageView.this)
        {
          boolean bool1 = AsyncAlbumArtImageView.access$402(AsyncAlbumArtImageView.this, false);
          int i = AsyncAlbumArtImageView.access$502(AsyncAlbumArtImageView.this, 0);
          int j = AsyncAlbumArtImageView.access$602(AsyncAlbumArtImageView.this, 0);
          AsyncAlbumArtImageView localAsyncAlbumArtImageView2 = AsyncAlbumArtImageView.this;
          Runnable local1 = new Runnable()
          {
            public void run()
            {
              MissingArtHelper localMissingArtHelper = AsyncAlbumArtImageView.this.getMissingCachedArtHelper();
              String str = paramAnonymousString;
              localMissingArtHelper.removeId(str);
              AsyncAlbumArtImageView.this.makeDrawable();
            }
          };
          boolean bool2 = localAsyncAlbumArtImageView2.post(local1);
          return;
        }
      }
    };
    this.mCachedArtChangeListener = local3;
    int[] arrayOfInt = R.styleable.AlbumArt;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt);
    boolean bool1 = localTypedArray.getBoolean(0, false);
    this.mLayoutAsSquare = bool1;
    boolean bool2 = localTypedArray.getBoolean(1, false);
    this.mStretchToFill = bool2;
    AlphaAnimation localAlphaAnimation = new AlphaAnimation(0.0F, 1.0F);
    this.mFadeInAnimation = localAlphaAnimation;
    Animation localAnimation = this.mFadeInAnimation;
    long l = paramContext.getResources().getInteger(2131492866);
    localAnimation.setDuration(l);
    int i = paramContext.getResources().getColor(2131427333);
    ColorDrawable localColorDrawable = new ColorDrawable(i);
    this.mNotAvailableImageOverlay = localColorDrawable;
  }

  private static Drawable createDrawable(Context paramContext, Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    BitmapDrawable localBitmapDrawable = null;
    Resources localResources1;
    if (paramBitmap != null)
    {
      if ((paramInt1 > 0) && (paramInt2 > 0))
        break label36;
      localResources1 = paramContext.getResources();
    }
    label36: Resources localResources2;
    for (localBitmapDrawable = new BitmapDrawable(localResources1, paramBitmap); ; localBitmapDrawable = new BitmapDrawable(localResources2, paramBitmap))
    {
      return localBitmapDrawable;
      localResources2 = paramContext.getResources();
    }
  }

  private int getAlbumHeight()
  {
    if (this.mVirtualHeight == 0);
    for (int i = getHeight(); ; i = this.mVirtualHeight)
      return i;
  }

  private int getAlbumWidth()
  {
    if (this.mVirtualWidth == 0);
    for (int i = getWidth(); ; i = this.mVirtualWidth)
      return i;
  }

  private static Bitmap getBitmapForRemoteUrlOrDefault(Context paramContext, String paramString, int paramInt1, int paramInt2, RemoteUrlSink paramRemoteUrlSink, boolean paramBoolean)
  {
    Bitmap localBitmap;
    if (TextUtils.isEmpty(paramString))
      localBitmap = null;
    while (true)
    {
      return localBitmap;
      Context localContext1 = paramContext;
      String str1 = paramString;
      int i = paramInt1;
      int j = paramInt2;
      boolean bool = paramBoolean;
      localBitmap = AlbumArtUtils.getRealNonAlbumArt(localContext1, str1, i, j, bool);
      if (localBitmap == null)
      {
        RemoteUrlSink localRemoteUrlSink = paramRemoteUrlSink;
        String str2 = paramString;
        localRemoteUrlSink.report(str2);
        if (!UIStateManager.getInstance().isNetworkConnected())
        {
          long l = AlbumArtUtils.makeDefaultArtId(paramString);
          Context localContext2 = paramContext;
          int k = paramInt1;
          int m = paramInt2;
          localBitmap = AlbumArtUtils.getDefaultArtwork(localContext2, true, l, k, m, null, null, null, false);
        }
      }
    }
  }

  private MissingAlbumArtHelper getMissingAlbumArtHelper()
  {
    if (this.mMissingAlbumArtHelper == null)
    {
      AlbumArtDownloadServiceConnection localAlbumArtDownloadServiceConnection = UIStateManager.getInstance(getContext()).getAlbumArtDownloadServiceConnection();
      ArtDownloadServiceConnection.ArtChangeListener localArtChangeListener = this.mAlbumArtChangeListener;
      MissingAlbumArtHelper localMissingAlbumArtHelper = new MissingAlbumArtHelper(localArtChangeListener, localAlbumArtDownloadServiceConnection);
      this.mMissingAlbumArtHelper = localMissingAlbumArtHelper;
    }
    return this.mMissingAlbumArtHelper;
  }

  private MissingArtHelper<String> getMissingCachedArtHelper()
  {
    if (this.mMissingCachedArtHelper == null)
    {
      CachedArtDownloadServiceConnection localCachedArtDownloadServiceConnection = UIStateManager.getInstance(getContext()).getCachedArtDownloadServiceConnection();
      ArtDownloadServiceConnection.ArtChangeListener localArtChangeListener = this.mCachedArtChangeListener;
      MissingArtHelper localMissingArtHelper = new MissingArtHelper(localArtChangeListener, localCachedArtDownloadServiceConnection);
      this.mMissingCachedArtHelper = localMissingArtHelper;
    }
    return this.mMissingCachedArtHelper;
  }

  private void loadArtAsynch()
  {
    setLoadingArtwork();
    if (this.mIsScrolling)
    {
      this.mRequestedWidth = 0;
      this.mRequestedHeight = 0;
      return;
    }
    getMissingAlbumArtHelper().clear();
    getMissingCachedArtHelper().clear();
    sHandler.removeCallbacksAndMessages(this);
    Message localMessage = sHandler.obtainMessage(1);
    localMessage.obj = this;
    boolean bool = sHandler.sendMessage(localMessage);
  }

  /** @deprecated */
  private void makeDrawable()
  {
    int i;
    int k;
    Context localContext1;
    while (true)
    {
      try
      {
        i = getAlbumWidth();
        int j = getAlbumHeight();
        k = j;
        if ((i <= 0) || (k <= 0))
          return;
        if (this.mActualArtworkSet)
          continue;
        int m = this.mRequestedWidth;
        if (i != m)
        {
          int n = this.mRequestedHeight;
          if (k == n)
            continue;
        }
        this.mRequestedWidth = i;
        this.mRequestedHeight = k;
        localContext1 = getContext();
        if (this.mMode == null)
        {
          this.mActualArtworkSet = true;
          continue;
        }
      }
      finally
      {
      }
      if ((this.mMode instanceof AllSongsMode))
      {
        loadArtAsynch();
      }
      else if ((this.mMode instanceof ShuffleAllMode))
      {
        loadArtAsynch();
      }
      else
      {
        ArtIdRecorder.AlbumIdRecorder localAlbumIdRecorder1;
        Bitmap localBitmap1;
        if ((this.mMode instanceof PlaylistMode))
        {
          PlaylistMode localPlaylistMode = (PlaylistMode)this.mMode;
          localAlbumIdRecorder1 = new ArtIdRecorder.AlbumIdRecorder();
          int i1 = localPlaylistMode.style;
          long l1 = localPlaylistMode.id;
          localBitmap1 = AlbumArtUtils.getCachedFauxAlbumArt(localContext1, i1, l1, i, k, localAlbumIdRecorder1, false);
          if (localBitmap1 != null)
          {
            AsyncAlbumArtImageView localAsyncAlbumArtImageView1 = this;
            Bitmap localBitmap2 = localBitmap1;
            localAsyncAlbumArtImageView1.setAlbumImage(localBitmap2, localAlbumIdRecorder1, null, true);
          }
          else
          {
            loadArtAsynch();
          }
        }
        else if ((this.mMode instanceof AlbumMode))
        {
          AlbumMode localAlbumMode = (AlbumMode)this.mMode;
          localAlbumIdRecorder1 = new ArtIdRecorder.AlbumIdRecorder();
          long l2 = localAlbumMode.albumId;
          String str1 = localAlbumMode.album;
          String str2 = localAlbumMode.artist;
          Context localContext2 = localContext1;
          int i2 = i;
          int i3 = k;
          ArtIdRecorder.AlbumIdRecorder localAlbumIdRecorder2 = localAlbumIdRecorder1;
          localBitmap1 = AlbumArtUtils.getCachedBitmap(localContext2, l2, null, i2, i3, str1, str2, localAlbumIdRecorder2, false, true, false);
          if (localBitmap1 != null)
          {
            AsyncAlbumArtImageView localAsyncAlbumArtImageView2 = this;
            Bitmap localBitmap3 = localBitmap1;
            localAsyncAlbumArtImageView2.setAlbumImage(localBitmap3, localAlbumIdRecorder1, null, true);
          }
          else
          {
            loadArtAsynch();
          }
        }
        else if ((this.mMode instanceof ArtistMode))
        {
          ArtistMode localArtistMode = (ArtistMode)this.mMode;
          localAlbumIdRecorder1 = new ArtIdRecorder.AlbumIdRecorder();
          long l3;
          if (localArtistMode.useFauxArt)
            l3 = localArtistMode.id;
          boolean bool1;
          String str4;
          for (localBitmap1 = AlbumArtUtils.getCachedFauxAlbumArt(localContext1, 7, l3, i, k, localAlbumIdRecorder1, false); ; localBitmap1 = AlbumArtUtils.getRealNonAlbumArtCopy(localContext1, str4, i, k, bool1))
          {
            if (localBitmap1 == null)
              break label460;
            AsyncAlbumArtImageView localAsyncAlbumArtImageView3 = this;
            Bitmap localBitmap4 = localBitmap1;
            localAsyncAlbumArtImageView3.setAlbumImage(localBitmap4, localAlbumIdRecorder1, null, true);
            break;
            String str3 = localArtistMode.uri.toString();
            bool1 = localArtistMode.cropToSquare();
            str4 = str3;
          }
          label460: loadArtAsynch();
        }
        else if ((this.mMode instanceof GenreMode))
        {
          GenreMode localGenreMode = (GenreMode)this.mMode;
          localAlbumIdRecorder1 = new ArtIdRecorder.AlbumIdRecorder();
          long l4 = localGenreMode.id;
          localBitmap1 = AlbumArtUtils.getCachedFauxAlbumArt(localContext1, 8, l4, i, k, localAlbumIdRecorder1, false);
          if (localBitmap1 != null)
          {
            AsyncAlbumArtImageView localAsyncAlbumArtImageView4 = this;
            Bitmap localBitmap5 = localBitmap1;
            localAsyncAlbumArtImageView4.setAlbumImage(localBitmap5, localAlbumIdRecorder1, null, true);
          }
          else
          {
            loadArtAsynch();
          }
        }
        else if (((this.mMode instanceof ExternalAlbumMode)) || ((this.mMode instanceof SongListMode)))
        {
          String str5 = this.mMode.getExternalUrl(localContext1);
          boolean bool2 = this.mMode.cropToSquare();
          String str6 = str5;
          localBitmap1 = AlbumArtUtils.getRealNonAlbumArtCopy(localContext1, str6, i, k, bool2);
          if (localBitmap1 != null)
          {
            AsyncAlbumArtImageView localAsyncAlbumArtImageView5 = this;
            Bitmap localBitmap6 = localBitmap1;
            localAsyncAlbumArtImageView5.setAlbumImage(localBitmap6, null, null, true);
          }
          else
          {
            loadArtAsynch();
          }
        }
        else if ((this.mMode instanceof MultiUrlCompositeMode))
        {
          MultiUrlCompositeMode localMultiUrlCompositeMode = (MultiUrlCompositeMode)this.mMode;
          boolean bool3 = localMultiUrlCompositeMode instanceof ExternalRadioMode;
          String str7 = localMultiUrlCompositeMode.mUrls;
          boolean bool4 = bool3;
          Bitmap localBitmap7 = AlbumArtUtils.getCachedMultiImageComposite(localContext1, str7, i, k, bool4);
          if ((!localMultiUrlCompositeMode.isCacheable()) || (localBitmap7 == null))
          {
            loadArtAsynch();
          }
          else
          {
            AsyncAlbumArtImageView localAsyncAlbumArtImageView6 = this;
            Bitmap localBitmap8 = localBitmap7;
            localAsyncAlbumArtImageView6.setAlbumImage(localBitmap8, null, null, true);
          }
        }
        else
        {
          if ((this.mMode instanceof DefaultArtworkMode))
          {
            int[] arrayOfInt1 = 4.$SwitchMap$com$google$android$music$AsyncAlbumArtImageView$LayerMode;
            int i4 = this.mLayerMode.ordinal();
            switch (arrayOfInt1[i4])
            {
            default:
            case 1:
            case 2:
            }
            while (true)
            {
              this.mActualArtworkSet = true;
              break;
              setBackgroundResource(2130837595);
              continue;
              setImageResource(2130837595);
            }
          }
          if (!(this.mMode instanceof ServiceAlbumMode))
            break;
          loadArtAsynch();
        }
      }
    }
    if ((this.mMode instanceof ImFeelingLuckyArtworkMode))
    {
      Mode localMode1 = this.mMode;
      Context localContext3 = localContext1;
      int i5 = i;
      int i6 = k;
      Bitmap localBitmap9 = localMode1.createBitmap(localContext3, i5, i6, null, null);
      int[] arrayOfInt2 = 4.$SwitchMap$com$google$android$music$AsyncAlbumArtImageView$LayerMode;
      int i7 = this.mLayerMode.ordinal();
      switch (arrayOfInt2[i7])
      {
      default:
      case 1:
      case 2:
      }
      while (true)
      {
        this.mActualArtworkSet = true;
        break;
        Context localContext4 = getContext();
        int i8 = this.mRequestedHeight;
        int i9 = this.mRequestedHeight;
        Bitmap localBitmap10 = localBitmap9;
        Drawable localDrawable = createDrawable(localContext4, localBitmap10, i8, i9);
        setBackgroundDrawable(localDrawable);
        continue;
        AsyncAlbumArtImageView localAsyncAlbumArtImageView7 = this;
        Bitmap localBitmap11 = localBitmap9;
        localAsyncAlbumArtImageView7.setImageBitmap(localBitmap11);
      }
    }
    StringBuilder localStringBuilder = new StringBuilder().append("Unkown mode: ");
    Mode localMode2 = this.mMode;
    String str8 = localMode2;
    throw new IllegalArgumentException(str8);
  }

  /** @deprecated */
  private void registerArtChangeListener()
  {
    try
    {
      if (this.mAttachedToWindow)
      {
        getMissingAlbumArtHelper().register();
        getMissingCachedArtHelper().register();
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private void requestMissingArt(ArtIdRecorder.AlbumIdRecorder paramAlbumIdRecorder, ArtIdRecorder.RemoteArtUrlRecorder paramRemoteArtUrlRecorder)
  {
    MusicUtils.assertUiThread();
    if (paramAlbumIdRecorder != null)
    {
      boolean bool1 = this.mMode instanceof PlaylistMode;
      MissingAlbumArtHelper localMissingAlbumArtHelper = getMissingAlbumArtHelper();
      Set localSet1 = paramAlbumIdRecorder.extractIds();
      boolean bool2 = this.mAttachedToWindow;
      localMissingAlbumArtHelper.set(bool1, localSet1, bool2);
    }
    if (paramRemoteArtUrlRecorder == null)
      return;
    MissingArtHelper localMissingArtHelper = getMissingCachedArtHelper();
    Set localSet2 = paramRemoteArtUrlRecorder.extractIds();
    boolean bool3 = this.mAttachedToWindow;
    localMissingArtHelper.set(localSet2, bool3);
  }

  private void setAlbumImage(Bitmap paramBitmap, ArtIdRecorder.AlbumIdRecorder paramAlbumIdRecorder, ArtIdRecorder.RemoteArtUrlRecorder paramRemoteArtUrlRecorder, boolean paramBoolean)
  {
    requestMissingArt(paramAlbumIdRecorder, paramRemoteArtUrlRecorder);
    setAlbumImage(paramBitmap, paramBoolean);
  }

  private void setAlbumImage(Bitmap paramBitmap, boolean paramBoolean)
  {
    Object localObject = Preconditions.checkNotNull(paramBitmap, "Null bitmap");
    AlbumArtUtils.recycleBitmap(this.mBitmap);
    this.mBitmap = paramBitmap;
    int[] arrayOfInt = 4.$SwitchMap$com$google$android$music$AsyncAlbumArtImageView$LayerMode;
    int i = this.mLayerMode.ordinal();
    switch (arrayOfInt[i])
    {
    default:
    case 1:
    case 2:
    }
    while (true)
    {
      if ((getVisibility() == 0) && (this.mAllowAnimation) && (!paramBoolean))
      {
        Animation localAnimation = this.mFadeInAnimation;
        startAnimation(localAnimation);
        this.mAllowAnimation = false;
      }
      this.mActualArtworkSet = true;
      this.mLoadingArtworkSet = false;
      return;
      Context localContext = getContext();
      int j = this.mRequestedHeight;
      int k = this.mRequestedHeight;
      Drawable localDrawable = createDrawable(localContext, paramBitmap, j, k);
      setBackgroundDrawable(localDrawable);
      continue;
      setImageBitmap(paramBitmap);
    }
  }

  private void setLoadingArtwork()
  {
    if (this.mLoadingArtworkSet)
      return;
    int[] arrayOfInt = 4.$SwitchMap$com$google$android$music$AsyncAlbumArtImageView$LayerMode;
    int i = this.mLayerMode.ordinal();
    switch (arrayOfInt[i])
    {
    default:
      return;
    case 1:
      int j = getAlbumWidth();
      int k = getAlbumHeight();
      if (j == 0)
        return;
      if (k == 0)
        return;
      setBackgroundDrawable(null);
      this.mLoadingArtworkSet = true;
      return;
    case 2:
    }
    setImageBitmap(null);
    this.mLoadingArtworkSet = true;
  }

  /** @deprecated */
  private void unregisterArtChangeListener()
  {
    try
    {
      getMissingAlbumArtHelper().unregister();
      getMissingCachedArtHelper().unregister();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void clearArtwork()
  {
    try
    {
      unregisterArtChangeListener();
      this.mMode = null;
      setLoadingArtwork();
      AlbumArtUtils.recycleBitmap(this.mBitmap);
      this.mBitmap = null;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mAttachedToWindow = true;
    registerArtChangeListener();
    this.mAllowAnimation = true;
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.mAttachedToWindow = false;
    unregisterArtChangeListener();
    this.mAllowAnimation = true;
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = 0;
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    if ((this.mVirtualWidth == 0) || (this.mVirtualHeight == 0))
      i = 1;
    if (!paramBoolean)
      return;
    if (i == 0)
      return;
    Object localObject1 = null;
    try
    {
      this.mLoadingArtworkSet = localObject1;
      this.mActualArtworkSet = false;
      makeDrawable();
      return;
    }
    finally
    {
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = 1073741824;
    int j;
    int k;
    int m;
    int n;
    if (this.mLayoutAsSquare)
    {
      j = View.MeasureSpec.getMode(paramInt1);
      k = View.MeasureSpec.getMode(paramInt2);
      m = View.MeasureSpec.getSize(paramInt1);
      n = View.MeasureSpec.getSize(paramInt2);
      if ((j != 0) || (k != 0))
        break label52;
    }
    while (true)
    {
      super.onMeasure(paramInt1, paramInt2);
      return;
      label52: if ((j == 1073741824) && (k == 1073741824))
      {
        paramInt2 = View.MeasureSpec.makeMeasureSpec(Math.max(m, n), 1073741824);
        paramInt1 = paramInt2;
      }
      else
      {
        if ((j != 1073741824) && (k != 1073741824))
          break;
        if (j == 1073741824)
        {
          if ((k != -2147483648) || (n >= m))
            paramInt2 = paramInt1;
        }
        else if ((j != -2147483648) || (m >= n))
          paramInt1 = paramInt2;
      }
    }
    int i1;
    if ((j == -2147483648) && (k == -2147483648))
    {
      i1 = Math.min(m, n);
      if (this.mStretchToFill);
      while (true)
      {
        paramInt2 = View.MeasureSpec.makeMeasureSpec(i1, i);
        paramInt1 = paramInt2;
        break;
        i = -2147483648;
      }
    }
    if ((j == -2147483648) || (k == -2147483648))
    {
      if (j == -2147483648)
      {
        i1 = m;
        label230: if (!this.mStretchToFill)
          break label256;
      }
      while (true)
      {
        paramInt2 = View.MeasureSpec.makeMeasureSpec(i1, i);
        paramInt1 = paramInt2;
        break;
        i1 = n;
        break label230;
        label256: i = -2147483648;
      }
    }
    String str = "Unknown modes: " + j + "/" + k;
    throw new IllegalStateException(str);
  }

  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if (!this.mLayoutAsSquare)
      return;
    if (paramInt1 != paramInt2)
      return;
    String str = "told to layout as square, but provided sizes (" + paramInt1 + "/" + paramInt2 + ") were not square";
    int i = Log.e("AsyncAlbumArtImageView", str);
  }

  public void onStartTemporaryDetach()
  {
    super.onStartTemporaryDetach();
    this.mAllowAnimation = true;
  }

  public void requestLayout()
  {
  }

  public void setAlbumId(long paramLong, String paramString1, String paramString2)
  {
    paramString2 = 0;
    try
    {
      if ((this.mMode instanceof AlbumMode))
      {
        localAlbumMode = (AlbumMode)this.mMode;
        if (!localAlbumMode.equals(paramLong, paramString1, paramString2))
          paramString2 = 1;
        if (paramString2 != 0)
        {
          unregisterArtChangeListener();
          localAlbumMode.albumId = paramLong;
          localAlbumMode.album = paramString1;
          localAlbumMode.artist = paramString2;
          this.mLoadingArtworkSet = false;
          this.mActualArtworkSet = false;
          this.mRequestedWidth = 0;
          this.mRequestedHeight = 0;
          getMissingAlbumArtHelper().clear();
          getMissingCachedArtHelper().clear();
          makeDrawable();
        }
        return;
      }
      AlbumMode localAlbumMode = new AlbumMode();
      this.mMode = localAlbumMode;
      paramString2 = 1;
    }
    finally
    {
    }
  }

  public void setAllSongs(String paramString, long paramLong, Uri paramUri)
  {
    try
    {
      unregisterArtChangeListener();
      if ((this.mMode instanceof AllSongsMode))
      {
        localAllSongsMode = (AllSongsMode)this.mMode;
        this.mMode = localAllSongsMode;
        localAllSongsMode.parentName = paramString;
        localAllSongsMode.id = paramLong;
        localAllSongsMode.uri = paramUri;
        this.mLoadingArtworkSet = false;
        this.mActualArtworkSet = false;
        this.mRequestedWidth = 0;
        this.mRequestedHeight = 0;
        makeDrawable();
        return;
      }
      AllSongsMode localAllSongsMode = new AllSongsMode();
    }
    finally
    {
    }
  }

  public void setArtForSonglist(SongList paramSongList)
  {
    try
    {
      unregisterArtChangeListener();
      if ((this.mMode instanceof SongListMode))
      {
        localSongListMode = (SongListMode)this.mMode;
        this.mMode = localSongListMode;
        localSongListMode.songlist = paramSongList;
        this.mLoadingArtworkSet = false;
        this.mActualArtworkSet = false;
        this.mRequestedWidth = 0;
        this.mRequestedHeight = 0;
        makeDrawable();
        return;
      }
      SongListMode localSongListMode = new SongListMode();
    }
    finally
    {
    }
  }

  public void setArtistArt(String paramString, long paramLong, Uri paramUri, boolean paramBoolean)
  {
    paramBoolean = false;
    try
    {
      if ((this.mMode instanceof ArtistMode))
      {
        localArtistMode = (ArtistMode)this.mMode;
        String str = paramString;
        long l = paramLong;
        Uri localUri = paramUri;
        boolean bool = paramBoolean;
        if (!localArtistMode.equals(str, l, localUri, bool))
          paramBoolean = true;
        if (paramBoolean)
        {
          unregisterArtChangeListener();
          localArtistMode.parentName = paramString;
          localArtistMode.id = paramLong;
          localArtistMode.uri = paramUri;
          localArtistMode.useFauxArt = paramBoolean;
          this.mLoadingArtworkSet = false;
          this.mActualArtworkSet = false;
          this.mRequestedWidth = 0;
          this.mRequestedHeight = 0;
          makeDrawable();
        }
        return;
      }
      ArtistMode localArtistMode = new ArtistMode();
      this.mMode = localArtistMode;
      paramBoolean = true;
    }
    finally
    {
    }
  }

  public void setAvailable(boolean paramBoolean)
  {
    LayerMode localLayerMode1 = this.mLayerMode;
    LayerMode localLayerMode2 = LayerMode.FOREGROUND;
    if (localLayerMode1 == localLayerMode2)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("setAvailable only available in ");
      LayerMode localLayerMode3 = LayerMode.BACKGROUND;
      String str = localLayerMode3;
      throw new IllegalStateException(str);
    }
    this.mAvailable = paramBoolean;
    if (paramBoolean)
    {
      setImageDrawable(null);
      return;
    }
    ColorDrawable localColorDrawable = this.mNotAvailableImageOverlay;
    setImageDrawable(localColorDrawable);
  }

  public void setExternalAlbumArt(String paramString)
  {
    int i = 0;
    try
    {
      if ((this.mMode instanceof ExternalAlbumMode))
      {
        localExternalAlbumMode = (ExternalAlbumMode)this.mMode;
        if (!localExternalAlbumMode.equals(paramString))
          i = 1;
        if (i != 0)
        {
          unregisterArtChangeListener();
          localExternalAlbumMode.mUrl = paramString;
          this.mLoadingArtworkSet = false;
          this.mActualArtworkSet = false;
          this.mRequestedWidth = 0;
          this.mRequestedHeight = 0;
          makeDrawable();
        }
        return;
      }
      ExternalAlbumMode localExternalAlbumMode = new ExternalAlbumMode();
      this.mMode = localExternalAlbumMode;
      i = 1;
    }
    finally
    {
    }
  }

  public void setExternalArtRadio(String paramString)
  {
    int i = 0;
    try
    {
      if ((this.mMode instanceof ExternalRadioMode))
      {
        localExternalRadioMode = (ExternalRadioMode)this.mMode;
        if (!localExternalRadioMode.equals(paramString))
          i = 1;
        if (i != 0)
        {
          unregisterArtChangeListener();
          localExternalRadioMode.mUrls = paramString;
          this.mLoadingArtworkSet = false;
          this.mActualArtworkSet = false;
          this.mRequestedWidth = 0;
          this.mRequestedHeight = 0;
          makeDrawable();
        }
        return;
      }
      ExternalRadioMode localExternalRadioMode = new ExternalRadioMode();
      this.mMode = localExternalRadioMode;
      i = 1;
    }
    finally
    {
    }
  }

  public void setGenreArt(String paramString, long paramLong, Uri paramUri)
  {
    paramUri = 0;
    try
    {
      if ((this.mMode instanceof GenreMode))
      {
        localGenreMode = (GenreMode)this.mMode;
        if (!localGenreMode.equals(paramString, paramLong, paramUri))
          paramUri = 1;
        if (paramUri != 0)
        {
          unregisterArtChangeListener();
          localGenreMode.parentName = paramString;
          localGenreMode.id = paramLong;
          localGenreMode.uri = paramUri;
          this.mLoadingArtworkSet = false;
          this.mActualArtworkSet = false;
          this.mRequestedWidth = 0;
          this.mRequestedHeight = 0;
          makeDrawable();
        }
        return;
      }
      GenreMode localGenreMode = new GenreMode();
      this.mMode = localGenreMode;
      paramUri = 1;
    }
    finally
    {
    }
  }

  public void setIsScrolling(boolean paramBoolean)
  {
    if ((!paramBoolean) && (this.mIsScrolling));
    for (int i = 1; ; i = 0)
    {
      this.mIsScrolling = paramBoolean;
      if (i == 0)
        return;
      makeDrawable();
      return;
    }
  }

  public void setLayerMode(LayerMode paramLayerMode)
  {
    this.mLayerMode = paramLayerMode;
  }

  public void setOnClickListener(View.OnClickListener paramOnClickListener)
  {
    super.setOnClickListener(paramOnClickListener);
    View.OnTouchListener local1 = new View.OnTouchListener()
    {
      final int color;

      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        switch (paramAnonymousMotionEvent.getAction())
        {
        case 2:
        default:
        case 0:
        case 1:
        case 3:
        }
        label173: 
        while (true)
        {
          return false;
          AsyncAlbumArtImageView.LayerMode localLayerMode1 = AsyncAlbumArtImageView.this.mLayerMode;
          AsyncAlbumArtImageView.LayerMode localLayerMode2 = AsyncAlbumArtImageView.LayerMode.FOREGROUND;
          if (localLayerMode1 == localLayerMode2);
          for (Drawable localDrawable = AsyncAlbumArtImageView.this.getDrawable(); localDrawable != null; localDrawable = AsyncAlbumArtImageView.this.getBackground())
          {
            int i = this.color;
            PorterDuff.Mode localMode = PorterDuff.Mode.SRC_OVER;
            localDrawable.setColorFilter(i, localMode);
            AsyncAlbumArtImageView.this.invalidate();
            break;
          }
          AsyncAlbumArtImageView.LayerMode localLayerMode3 = AsyncAlbumArtImageView.this.mLayerMode;
          AsyncAlbumArtImageView.LayerMode localLayerMode4 = AsyncAlbumArtImageView.LayerMode.FOREGROUND;
          if (localLayerMode3 == localLayerMode4);
          for (localDrawable = AsyncAlbumArtImageView.this.getDrawable(); ; localDrawable = AsyncAlbumArtImageView.this.getBackground())
          {
            if (localDrawable == null)
              break label173;
            localDrawable.clearColorFilter();
            AsyncAlbumArtImageView.this.invalidate();
            break;
          }
        }
      }
    };
    setOnTouchListener(local1);
  }

  public void setPlaylistAlbumArt(long paramLong, String paramString, int paramInt)
  {
    paramInt = 0;
    try
    {
      if ((this.mMode instanceof PlaylistMode))
      {
        localPlaylistMode = (PlaylistMode)this.mMode;
        int i = AlbumArtUtils.playlistTypeToArtStyle(paramInt);
        if (!localPlaylistMode.equals(paramString, paramLong, i))
          paramInt = 1;
        if (paramInt != 0)
        {
          unregisterArtChangeListener();
          localPlaylistMode.mainLabel = paramString;
          localPlaylistMode.id = paramLong;
          int j = AlbumArtUtils.playlistTypeToArtStyle(paramInt);
          localPlaylistMode.style = j;
          this.mLoadingArtworkSet = false;
          this.mActualArtworkSet = false;
          this.mRequestedWidth = 0;
          this.mRequestedHeight = 0;
          makeDrawable();
        }
        return;
      }
      PlaylistMode localPlaylistMode = new PlaylistMode();
      this.mMode = localPlaylistMode;
      paramInt = 1;
    }
    finally
    {
    }
  }

  public void setServiceAlbumArt(long paramLong, String paramString1, String paramString2, IMusicPlaybackService paramIMusicPlaybackService)
  {
    try
    {
      unregisterArtChangeListener();
      if ((!(this.mMode instanceof ServiceAlbumMode)) || (((ServiceAlbumMode)this.mMode).albumId != paramLong))
      {
        ServiceAlbumMode localServiceAlbumMode = new ServiceAlbumMode();
        this.mMode = localServiceAlbumMode;
        ((ServiceAlbumMode)this.mMode).albumId = paramLong;
        ((ServiceAlbumMode)this.mMode).artist = paramString1;
        ((ServiceAlbumMode)this.mMode).album = paramString2;
        ((ServiceAlbumMode)this.mMode).service = paramIMusicPlaybackService;
        this.mLoadingArtworkSet = false;
        this.mActualArtworkSet = false;
        this.mRequestedWidth = 0;
        this.mRequestedHeight = 0;
        makeDrawable();
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void setSharedPlaylistArt(String paramString)
  {
    int i = 0;
    try
    {
      if (((this.mMode instanceof MultiUrlCompositeMode)) && (!(this.mMode instanceof ExternalRadioMode)))
      {
        localMultiUrlCompositeMode = (MultiUrlCompositeMode)this.mMode;
        if (!localMultiUrlCompositeMode.equals(paramString))
          i = 1;
        if (i != 0)
        {
          unregisterArtChangeListener();
          localMultiUrlCompositeMode.mUrls = paramString;
          this.mLoadingArtworkSet = false;
          this.mActualArtworkSet = false;
          this.mRequestedWidth = 0;
          this.mRequestedHeight = 0;
          makeDrawable();
        }
        return;
      }
      MultiUrlCompositeMode localMultiUrlCompositeMode = new MultiUrlCompositeMode();
      this.mMode = localMultiUrlCompositeMode;
      i = 1;
    }
    finally
    {
    }
  }

  public void setShuffleAllArt(String paramString)
  {
    try
    {
      unregisterArtChangeListener();
      if ((this.mMode instanceof ShuffleAllMode))
      {
        localShuffleAllMode = (ShuffleAllMode)this.mMode;
        localShuffleAllMode.parentName = paramString;
        this.mMode = localShuffleAllMode;
        this.mLoadingArtworkSet = false;
        this.mActualArtworkSet = false;
        this.mRequestedWidth = 0;
        this.mRequestedHeight = 0;
        makeDrawable();
        return;
      }
      ShuffleAllMode localShuffleAllMode = new ShuffleAllMode();
    }
    finally
    {
    }
  }

  public void setVirtualSize(int paramInt1, int paramInt2)
  {
    if ((paramInt1 != 0) || (paramInt2 != 0))
    {
      LayerMode localLayerMode1 = this.mLayerMode;
      LayerMode localLayerMode2 = LayerMode.FOREGROUND;
      if (localLayerMode1 != localLayerMode2)
        int i = Log.wtf("AsyncAlbumArtImageView", "Can not set virtual size if Album-Art-Mode is not FOREGROUND");
    }
    if ((this.mVirtualWidth != paramInt1) && (this.mVirtualHeight != paramInt2))
      return;
    this.mVirtualWidth = paramInt1;
    this.mVirtualHeight = paramInt2;
    Object localObject1 = null;
    try
    {
      this.mLoadingArtworkSet = localObject1;
      this.mActualArtworkSet = false;
      makeDrawable();
      return;
    }
    finally
    {
    }
  }

  public void showDefaultArtwork()
  {
    try
    {
      if ((this.mMode != null) && ((this.mMode instanceof DefaultArtworkMode)))
        return;
      DefaultArtworkMode localDefaultArtworkMode = new DefaultArtworkMode();
      this.mMode = localDefaultArtworkMode;
      this.mLoadingArtworkSet = false;
      this.mActualArtworkSet = false;
      this.mRequestedWidth = 0;
      this.mRequestedHeight = 0;
      makeDrawable();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void showImFeelingLuckyArtwork()
  {
    try
    {
      if ((this.mMode != null) && ((this.mMode instanceof ImFeelingLuckyArtworkMode)))
        return;
      ImFeelingLuckyArtworkMode localImFeelingLuckyArtworkMode = new ImFeelingLuckyArtworkMode();
      this.mMode = localImFeelingLuckyArtworkMode;
      this.mLoadingArtworkSet = false;
      this.mActualArtworkSet = false;
      this.mRequestedHeight = 0;
      this.mRequestedWidth = 0;
      makeDrawable();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private static class MissingAlbumArtHelper extends MissingArtHelper<Long>
  {
    private boolean mIsPlaylist;

    MissingAlbumArtHelper(ArtDownloadServiceConnection.ArtChangeListener<Long> paramArtChangeListener, ArtDownloadServiceConnection<Long> paramArtDownloadServiceConnection)
    {
      super(paramArtDownloadServiceConnection);
    }

    public void set(boolean paramBoolean1, Set<Long> paramSet, boolean paramBoolean2)
    {
      super.set(paramSet, paramBoolean2);
      this.mIsPlaylist = paramBoolean1;
    }

    public void syncComplete()
    {
      if (!this.mIsPlaylist)
        return;
      if (!isEmpty())
        return;
      getChangeListener().notifyArtChanged(null);
    }
  }

  private static class AlbumArtHandler extends LoggableHandler
  {
    private Handler mHandler;

    AlbumArtHandler()
    {
      super();
      Handler localHandler = new Handler();
      this.mHandler = localHandler;
    }

    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown message ");
        int i = paramMessage.what;
        String str = i;
        throw new IllegalArgumentException(str);
      case 1:
      }
      AsyncAlbumArtImageView localAsyncAlbumArtImageView = (AsyncAlbumArtImageView)paramMessage.obj;
      renderBitmap(localAsyncAlbumArtImageView);
    }

    void renderBitmap(AsyncAlbumArtImageView paramAsyncAlbumArtImageView)
    {
      try
      {
        AsyncAlbumArtImageView.Mode localMode1 = paramAsyncAlbumArtImageView.mMode;
        if (localMode1 == null)
          return;
        AsyncAlbumArtImageView.Mode localMode2 = localMode1.copy();
        Context localContext = paramAsyncAlbumArtImageView.getContext();
        int i = paramAsyncAlbumArtImageView.getAlbumWidth();
        int j = paramAsyncAlbumArtImageView.getAlbumHeight();
        ArtIdRecorder.AlbumIdRecorder localAlbumIdRecorder1 = new ArtIdRecorder.AlbumIdRecorder();
        ArtIdRecorder.RemoteArtUrlRecorder localRemoteArtUrlRecorder1 = new ArtIdRecorder.RemoteArtUrlRecorder();
        final Bitmap localBitmap = localMode2.createBitmap(localContext, i, j, localAlbumIdRecorder1, localRemoteArtUrlRecorder1);
        Handler localHandler = this.mHandler;
        AlbumArtHandler localAlbumArtHandler = this;
        final AsyncAlbumArtImageView localAsyncAlbumArtImageView = paramAsyncAlbumArtImageView;
        final AsyncAlbumArtImageView.Mode localMode3 = localMode2;
        final ArtIdRecorder.AlbumIdRecorder localAlbumIdRecorder2 = localAlbumIdRecorder1;
        final ArtIdRecorder.RemoteArtUrlRecorder localRemoteArtUrlRecorder2 = localRemoteArtUrlRecorder1;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            synchronized (localAsyncAlbumArtImageView)
            {
              AsyncAlbumArtImageView.Mode localMode1 = localMode3;
              AsyncAlbumArtImageView.Mode localMode2 = localAsyncAlbumArtImageView.mMode;
              if (localMode1.equals(localMode2))
              {
                AsyncAlbumArtImageView localAsyncAlbumArtImageView2 = localAsyncAlbumArtImageView;
                ArtIdRecorder.AlbumIdRecorder localAlbumIdRecorder = localAlbumIdRecorder2;
                ArtIdRecorder.RemoteArtUrlRecorder localRemoteArtUrlRecorder = localRemoteArtUrlRecorder2;
                localAsyncAlbumArtImageView2.requestMissingArt(localAlbumIdRecorder, localRemoteArtUrlRecorder);
                if (localBitmap != null)
                {
                  AsyncAlbumArtImageView localAsyncAlbumArtImageView3 = localAsyncAlbumArtImageView;
                  Bitmap localBitmap = localBitmap;
                  localAsyncAlbumArtImageView3.setAlbumImage(localBitmap, false);
                }
                return;
              }
              AlbumArtUtils.recycleBitmap(localBitmap);
            }
          }
        };
        boolean bool = localHandler.postDelayed(local1, 100L);
        return;
      }
      finally
      {
      }
    }
  }

  private static class ImFeelingLuckyArtworkMode extends AsyncAlbumArtImageView.Mode
  {
    private static SparseArray<Bitmap> sRenderedImages = new SparseArray();

    public ImFeelingLuckyArtworkMode()
    {
      super();
    }

    public ImFeelingLuckyArtworkMode(ImFeelingLuckyArtworkMode paramImFeelingLuckyArtworkMode)
    {
      super();
    }

    public AsyncAlbumArtImageView.Mode copy()
    {
      return new ImFeelingLuckyArtworkMode(this);
    }

    public Bitmap createBitmap(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, RemoteUrlSink paramRemoteUrlSink)
    {
      Object localObject = (Bitmap)sRenderedImages.get(paramInt1);
      Bitmap localBitmap1;
      Bitmap localBitmap2;
      int k;
      int m;
      Bitmap localBitmap3;
      String str1;
      StringBuilder localStringBuilder;
      if (localObject == null)
      {
        localBitmap1 = BitmapFactory.decodeResource(paramContext.getResources(), 2130837604);
        localBitmap2 = BitmapFactory.decodeResource(paramContext.getResources(), 2130837605);
        int i = localBitmap1.getWidth();
        int j = localBitmap2.getWidth();
        k = i - j;
        m = -1;
        int n = localBitmap1.getWidth();
        if (paramInt1 < n)
          break label206;
        localBitmap3 = localBitmap1;
        if (AsyncAlbumArtImageView.LOGV)
        {
          str1 = "AsyncAlbumArtImageView";
          localStringBuilder = new StringBuilder().append("delta: ").append(m).append(", chosen bmp: ");
          if (localBitmap3 != localBitmap1)
            break label262;
        }
      }
      label262: for (String str2 = "Large"; ; str2 = "Small")
      {
        String str3 = str2;
        int i1 = Log.v(str1, str3);
        Bitmap localBitmap4 = Bitmap.createScaledBitmap(localBitmap3, paramInt1, paramInt2, true);
        if (!localBitmap4.isMutable())
        {
          Bitmap.Config localConfig = localBitmap4.getConfig();
          localBitmap4 = localBitmap4.copy(localConfig, true);
        }
        AlbumArtUtils.drawImFeelingLuckyRadioArtOverlay(paramContext, localBitmap4, paramInt1, paramInt2);
        sRenderedImages.put(paramInt1, localBitmap4);
        localObject = localBitmap4;
        return localObject;
        label206: int i2 = localBitmap2.getWidth();
        if (paramInt1 <= i2)
        {
          localBitmap3 = localBitmap2;
          break;
        }
        m = localBitmap1.getWidth() - paramInt1;
        int i3 = k / 2;
        if (m < i3)
        {
          localBitmap3 = localBitmap1;
          break;
        }
        localBitmap3 = localBitmap2;
        break;
      }
    }
  }

  private static class DefaultArtworkMode extends AsyncAlbumArtImageView.Mode
  {
    public DefaultArtworkMode()
    {
      super();
    }

    public DefaultArtworkMode(DefaultArtworkMode paramDefaultArtworkMode)
    {
      super();
    }

    public AsyncAlbumArtImageView.Mode copy()
    {
      return new DefaultArtworkMode(this);
    }

    public Bitmap createBitmap(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, RemoteUrlSink paramRemoteUrlSink)
    {
      return null;
    }

    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof DefaultArtworkMode));
      for (boolean bool = false; ; bool = true)
        return bool;
    }

    public String toString()
    {
      return "DefaultArtworkMode<>";
    }
  }

  private static class GenreMode extends AsyncAlbumArtImageView.Mode
  {
    public long id;
    public String parentName;
    public Uri uri;

    public GenreMode()
    {
      super();
    }

    public GenreMode(GenreMode paramGenreMode)
    {
      super();
      String str = paramGenreMode.parentName;
      this.parentName = str;
      long l = paramGenreMode.id;
      this.id = l;
      Uri localUri = paramGenreMode.uri;
      this.uri = localUri;
    }

    public AsyncAlbumArtImageView.Mode copy()
    {
      return new GenreMode(this);
    }

    public Bitmap createBitmap(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, RemoteUrlSink paramRemoteUrlSink)
    {
      AlbumArtUtils.AlbumIdIteratorFactory localAlbumIdIteratorFactory = null;
      if (this.uri != null)
      {
        Uri localUri = this.uri;
        localAlbumIdIteratorFactory = AlbumArtUtils.createAlbumIdIteratorFactoryForContentUri(paramContext, localUri);
      }
      long l = this.id;
      String str = this.parentName;
      Context localContext = paramContext;
      int i = paramInt1;
      int j = paramInt2;
      AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
      return AlbumArtUtils.getFauxAlbumArt(localContext, 8, false, l, i, j, str, null, localAlbumIdIteratorFactory, localAlbumIdSink, false);
    }

    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof GenreMode));
      String str;
      long l;
      Uri localUri;
      for (boolean bool = false; ; bool = equals(str, l, localUri))
      {
        return bool;
        GenreMode localGenreMode = (GenreMode)paramObject;
        str = localGenreMode.parentName;
        l = localGenreMode.id;
        localUri = localGenreMode.uri;
      }
    }

    public boolean equals(String paramString, long paramLong, Uri paramUri)
    {
      if ((isEqual(this.parentName, paramString)) && (this.id == paramLong) && (isEqual(this.uri, paramUri)));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder().append("GenreMode<parentName:");
      String str = this.parentName;
      return str + ">";
    }
  }

  private static class ArtistMode extends AsyncAlbumArtImageView.Mode
  {
    public long id;
    public String parentName;
    public Uri uri;
    public boolean useFauxArt;

    public ArtistMode()
    {
      super();
    }

    public ArtistMode(ArtistMode paramArtistMode)
    {
      super();
      String str = paramArtistMode.parentName;
      this.parentName = str;
      long l = paramArtistMode.id;
      this.id = l;
      Uri localUri = paramArtistMode.uri;
      this.uri = localUri;
      boolean bool = paramArtistMode.useFauxArt;
      this.useFauxArt = bool;
    }

    private Bitmap makeFauxArt(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, Uri paramUri)
    {
      AlbumArtUtils.AlbumIdIteratorFactory localAlbumIdIteratorFactory = null;
      if (paramUri != null)
      {
        Uri localUri = paramUri;
        localAlbumIdIteratorFactory = AlbumArtUtils.createAlbumIdIteratorFactoryForContentUri(paramContext, localUri);
      }
      long l = this.id;
      String str = this.parentName;
      Context localContext = paramContext;
      int i = paramInt1;
      int j = paramInt2;
      AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
      return AlbumArtUtils.getFauxAlbumArt(localContext, 7, false, l, i, j, str, null, localAlbumIdIteratorFactory, localAlbumIdSink, false);
    }

    public AsyncAlbumArtImageView.Mode copy()
    {
      return new ArtistMode(this);
    }

    public Bitmap createBitmap(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, RemoteUrlSink paramRemoteUrlSink)
    {
      Bitmap localBitmap;
      if (this.useFauxArt)
      {
        Uri localUri1 = this.uri;
        ArtistMode localArtistMode1 = this;
        Context localContext1 = paramContext;
        int i = paramInt1;
        int j = paramInt2;
        AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
        localBitmap = localArtistMode1.makeFauxArt(localContext1, i, j, localAlbumIdSink, localUri1);
      }
      while (true)
      {
        return localBitmap;
        String str = this.uri.toString();
        boolean bool = cropToSquare();
        localBitmap = AlbumArtUtils.getRealNonAlbumArt(paramContext, str, paramInt1, paramInt2, bool);
        if (localBitmap == null)
        {
          paramRemoteUrlSink.report(str);
          if (UIStateManager.getInstance().isNetworkConnected())
          {
            localBitmap = null;
          }
          else
          {
            ArtistMode localArtistMode2 = this;
            Context localContext2 = paramContext;
            int k = paramInt1;
            int m = paramInt2;
            Uri localUri2 = null;
            localBitmap = localArtistMode2.makeFauxArt(localContext2, k, m, null, localUri2);
          }
        }
      }
    }

    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof ArtistMode));
      String str;
      long l;
      Uri localUri;
      boolean bool2;
      for (boolean bool1 = false; ; bool1 = equals(str, l, localUri, bool2))
      {
        return bool1;
        ArtistMode localArtistMode = (ArtistMode)paramObject;
        str = localArtistMode.parentName;
        l = localArtistMode.id;
        localUri = localArtistMode.uri;
        bool2 = localArtistMode.useFauxArt;
      }
    }

    public boolean equals(String paramString, long paramLong, Uri paramUri, boolean paramBoolean)
    {
      if ((isEqual(this.parentName, paramString)) && (this.id == paramLong) && (isEqual(this.uri, paramUri)) && (this.useFauxArt != paramBoolean));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder().append("ArtistMode<parentName:");
      String str = this.parentName;
      return str + ">";
    }
  }

  private static class SongListMode extends AsyncAlbumArtImageView.Mode
  {
    public SongList songlist;

    public SongListMode()
    {
      super();
    }

    public SongListMode(SongListMode paramSongListMode)
    {
      super();
      SongList localSongList = paramSongListMode.songlist;
      this.songlist = localSongList;
    }

    public AsyncAlbumArtImageView.Mode copy()
    {
      return new SongListMode(this);
    }

    public Bitmap createBitmap(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, RemoteUrlSink paramRemoteUrlSink)
    {
      String str;
      boolean bool;
      Context localContext1;
      int i;
      int j;
      RemoteUrlSink localRemoteUrlSink;
      if ((this.songlist instanceof ExternalSongList))
      {
        str = ((ExternalSongList)this.songlist).getAlbumArtUrl(paramContext);
        if (!TextUtils.isEmpty(str))
        {
          bool = cropToSquare();
          localContext1 = paramContext;
          i = paramInt1;
          j = paramInt2;
          localRemoteUrlSink = paramRemoteUrlSink;
        }
      }
      SongList localSongList;
      Context localContext2;
      int k;
      int m;
      AlbumIdSink localAlbumIdSink;
      for (Bitmap localBitmap = AsyncAlbumArtImageView.getBitmapForRemoteUrlOrDefault(localContext1, str, i, j, localRemoteUrlSink, bool); ; localBitmap = localSongList.getImage(localContext2, k, m, localAlbumIdSink, false))
      {
        return localBitmap;
        localSongList = this.songlist;
        localContext2 = paramContext;
        k = paramInt1;
        m = paramInt2;
        localAlbumIdSink = paramAlbumIdSink;
      }
    }

    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof SongListMode));
      SongList localSongList1;
      SongList localSongList2;
      for (boolean bool = false; ; bool = localSongList1.equals(localSongList2))
      {
        return bool;
        SongListMode localSongListMode = (SongListMode)paramObject;
        localSongList1 = this.songlist;
        localSongList2 = localSongListMode.songlist;
      }
    }

    public String getExternalUrl(Context paramContext)
    {
      if ((this.songlist instanceof ExternalSongList));
      for (String str = ((ExternalSongList)this.songlist).getAlbumArtUrl(paramContext); ; str = null)
        return str;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder().append("SongListMode<");
      SongList localSongList = this.songlist;
      return localSongList + ">";
    }
  }

  private static class ServiceAlbumMode extends AsyncAlbumArtImageView.Mode
  {
    public String album;
    public long albumId;
    public String artist;
    public IMusicPlaybackService service;

    public ServiceAlbumMode()
    {
      super();
    }

    public ServiceAlbumMode(ServiceAlbumMode paramServiceAlbumMode)
    {
      super();
      long l = paramServiceAlbumMode.albumId;
      this.albumId = l;
      IMusicPlaybackService localIMusicPlaybackService = paramServiceAlbumMode.service;
      this.service = localIMusicPlaybackService;
      String str1 = paramServiceAlbumMode.album;
      this.album = str1;
      String str2 = paramServiceAlbumMode.artist;
      this.artist = str2;
    }

    public AsyncAlbumArtImageView.Mode copy()
    {
      return new ServiceAlbumMode(this);
    }

    public Bitmap createBitmap(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, RemoteUrlSink paramRemoteUrlSink)
    {
      try
      {
        IMusicPlaybackService localIMusicPlaybackService = this.service;
        long l1 = this.albumId;
        String str1 = localIMusicPlaybackService.getAlbumArtUrl(l1);
        Context localContext1 = paramContext;
        int i = paramInt1;
        int j = paramInt2;
        Bitmap localBitmap = AlbumArtUtils.getExternalAlbumArtBitmap(localContext1, str1, i, j, true, true, false);
        if (localBitmap == null)
        {
          long l2 = this.albumId;
          String str2 = this.album;
          String str3 = this.artist;
          Context localContext2 = paramContext;
          int k = paramInt1;
          int m = paramInt2;
          AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
          localBitmap = AlbumArtUtils.getDefaultArtwork(localContext2, false, l2, k, m, str2, str3, localAlbumIdSink, false);
          Long localLong = Long.valueOf(this.albumId);
          paramAlbumIdSink.report(localLong);
        }
        return localBitmap;
      }
      catch (RemoteException localRemoteException)
      {
        while (true)
        {
          String str4 = localRemoteException.getMessage();
          int n = Log.e("AsyncAlbumArtImageView", str4, localRemoteException);
        }
      }
      catch (Exception localException)
      {
        while (true)
        {
          String str5 = localException.getMessage();
          int i1 = Log.e("AsyncAlbumArtImageView", str5, localException);
        }
      }
    }

    public boolean equals(Object paramObject)
    {
      boolean bool = false;
      if (!(paramObject instanceof ServiceAlbumMode));
      while (true)
      {
        return bool;
        ServiceAlbumMode localServiceAlbumMode = (ServiceAlbumMode)paramObject;
        long l1 = this.albumId;
        long l2 = localServiceAlbumMode.albumId;
        if (l1 == l2)
        {
          IMusicPlaybackService localIMusicPlaybackService1 = this.service;
          IMusicPlaybackService localIMusicPlaybackService2 = localServiceAlbumMode.service;
          if (localIMusicPlaybackService1 == localIMusicPlaybackService2)
          {
            String str1 = this.album;
            String str2 = localServiceAlbumMode.album;
            if (isEqual(str1, str2))
            {
              String str3 = this.artist;
              String str4 = localServiceAlbumMode.artist;
              if (isEqual(str3, str4))
                bool = true;
            }
          }
        }
      }
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder().append("ServiceAlbumMode<albumId:");
      long l = this.albumId;
      return l + ">";
    }
  }

  private static class MultiUrlCompositeMode extends AsyncAlbumArtImageView.Mode
  {
    protected boolean mDrawRadioOverlay = false;
    protected String mUrls;

    public MultiUrlCompositeMode()
    {
      super();
    }

    public MultiUrlCompositeMode(MultiUrlCompositeMode paramMultiUrlCompositeMode)
    {
      super();
      String str = paramMultiUrlCompositeMode.mUrls;
      this.mUrls = str;
    }

    public AsyncAlbumArtImageView.Mode copy()
    {
      return new MultiUrlCompositeMode(this);
    }

    // ERROR //
    public Bitmap createBitmap(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, RemoteUrlSink paramRemoteUrlSink)
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 22	com/google/android/music/AsyncAlbumArtImageView$MultiUrlCompositeMode:mUrls	Ljava/lang/String;
      //   4: invokestatic 38	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
      //   7: ifeq +9 -> 16
      //   10: aconst_null
      //   11: astore 6
      //   13: aload 6
      //   15: areturn
      //   16: aload_0
      //   17: getfield 22	com/google/android/music/AsyncAlbumArtImageView$MultiUrlCompositeMode:mUrls	Ljava/lang/String;
      //   20: invokestatic 44	com/google/android/music/utils/MusicUtils:decodeStringArray	(Ljava/lang/String;)[Ljava/lang/String;
      //   23: astore 7
      //   25: aload 7
      //   27: arraylength
      //   28: istore 8
      //   30: iload 8
      //   32: anewarray 46	android/os/ParcelFileDescriptor
      //   35: astore 9
      //   37: iconst_1
      //   38: istore 10
      //   40: iconst_0
      //   41: istore 11
      //   43: iload 11
      //   45: iload 8
      //   47: if_icmpge +60 -> 107
      //   50: aload 7
      //   52: iload 11
      //   54: aaload
      //   55: astore 12
      //   57: aload_1
      //   58: aload 12
      //   60: iconst_0
      //   61: iconst_0
      //   62: invokestatic 52	com/google/android/music/store/MusicContent$CachedArt:openFileDescriptor	(Landroid/content/Context;Ljava/lang/String;II)Landroid/os/ParcelFileDescriptor;
      //   65: astore 13
      //   67: aload 9
      //   69: iload 11
      //   71: aload 13
      //   73: aastore
      //   74: iload 11
      //   76: iconst_1
      //   77: iadd
      //   78: istore 11
      //   80: goto -37 -> 43
      //   83: astore 14
      //   85: aload 7
      //   87: iload 11
      //   89: aaload
      //   90: astore 15
      //   92: aload 5
      //   94: aload 15
      //   96: invokeinterface 58 2 0
      //   101: iconst_0
      //   102: istore 10
      //   104: goto -30 -> 74
      //   107: iload 10
      //   109: ifeq +88 -> 197
      //   112: aload_0
      //   113: getfield 22	com/google/android/music/AsyncAlbumArtImageView$MultiUrlCompositeMode:mUrls	Ljava/lang/String;
      //   116: astore 16
      //   118: aload_0
      //   119: getfield 19	com/google/android/music/AsyncAlbumArtImageView$MultiUrlCompositeMode:mDrawRadioOverlay	Z
      //   122: istore 17
      //   124: aload_1
      //   125: astore 18
      //   127: iload_2
      //   128: istore 19
      //   130: iload_3
      //   131: istore 20
      //   133: aload 18
      //   135: aload 16
      //   137: iload 19
      //   139: iload 20
      //   141: iload 17
      //   143: invokestatic 64	com/google/android/music/utils/AlbumArtUtils:getMultiImageComposite	(Landroid/content/Context;Ljava/lang/String;IIZ)Landroid/graphics/Bitmap;
      //   146: astore 21
      //   148: aload 21
      //   150: astore 6
      //   152: aload 9
      //   154: astore 22
      //   156: aload 22
      //   158: arraylength
      //   159: istore 23
      //   161: iconst_0
      //   162: istore 24
      //   164: iload 24
      //   166: iload 23
      //   168: if_icmpge -155 -> 13
      //   171: aload 22
      //   173: iload 24
      //   175: aaload
      //   176: astore 25
      //   178: aload 25
      //   180: ifnull +8 -> 188
      //   183: aload 25
      //   185: invokevirtual 67	android/os/ParcelFileDescriptor:close	()V
      //   188: iload 24
      //   190: iconst_1
      //   191: iadd
      //   192: istore 24
      //   194: goto -30 -> 164
      //   197: aconst_null
      //   198: astore 6
      //   200: aload 9
      //   202: astore 22
      //   204: aload 22
      //   206: arraylength
      //   207: istore 23
      //   209: iconst_0
      //   210: istore 24
      //   212: iload 24
      //   214: iload 23
      //   216: if_icmpge -203 -> 13
      //   219: aload 22
      //   221: iload 24
      //   223: aaload
      //   224: astore 26
      //   226: aload 26
      //   228: ifnull +8 -> 236
      //   231: aload 26
      //   233: invokevirtual 67	android/os/ParcelFileDescriptor:close	()V
      //   236: iload 24
      //   238: iconst_1
      //   239: iadd
      //   240: istore 24
      //   242: goto -30 -> 212
      //   245: aload 27
      //   247: athrow
      //   248: astore 27
      //   250: aload 9
      //   252: astore 28
      //   254: aload 28
      //   256: arraylength
      //   257: istore 29
      //   259: iconst_0
      //   260: istore 30
      //   262: iload 30
      //   264: iload 29
      //   266: if_icmpge -21 -> 245
      //   269: aload 28
      //   271: iload 30
      //   273: aaload
      //   274: astore 31
      //   276: aload 31
      //   278: ifnull +8 -> 286
      //   281: aload 31
      //   283: invokevirtual 67	android/os/ParcelFileDescriptor:close	()V
      //   286: iload 30
      //   288: iconst_1
      //   289: iadd
      //   290: istore 30
      //   292: goto -30 -> 262
      //   295: astore 32
      //   297: goto -11 -> 286
      //   300: astore 33
      //   302: goto -114 -> 188
      //   305: astore 34
      //   307: goto -71 -> 236
      //
      // Exception table:
      //   from	to	target	type
      //   50	74	83	java/io/FileNotFoundException
      //   112	148	248	finally
      //   281	286	295	java/lang/Exception
      //   183	188	300	java/lang/Exception
      //   231	236	305	java/lang/Exception
    }

    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof MultiUrlCompositeMode));
      String str1;
      String str2;
      for (boolean bool = false; ; bool = isEqual(str1, str2))
      {
        return bool;
        MultiUrlCompositeMode localMultiUrlCompositeMode = (MultiUrlCompositeMode)paramObject;
        str1 = this.mUrls;
        str2 = localMultiUrlCompositeMode.mUrls;
      }
    }

    public boolean isCacheable()
    {
      return true;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder().append("MultiUrlCompositeMode<url:");
      String str = this.mUrls;
      return str + ">";
    }
  }

  private static class ExternalRadioMode extends AsyncAlbumArtImageView.MultiUrlCompositeMode
  {
    boolean mRadioOverlayIsLocal = false;

    public ExternalRadioMode()
    {
      this.mDrawRadioOverlay = true;
    }

    public ExternalRadioMode(ExternalRadioMode paramExternalRadioMode)
    {
      super();
      this.mDrawRadioOverlay = true;
    }

    public AsyncAlbumArtImageView.Mode copy()
    {
      return new ExternalRadioMode(this);
    }

    public Bitmap createBitmap(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, RemoteUrlSink paramRemoteUrlSink)
    {
      String[] arrayOfString = MusicUtils.decodeStringArray(this.mUrls);
      Bitmap localBitmap1;
      if ((arrayOfString == null) || (arrayOfString.length == 0))
      {
        Bitmap.Config localConfig1 = AlbumArtUtils.getPreferredConfig();
        localBitmap1 = AlbumArtUtils.createBitmap(paramInt1, paramInt2, localConfig1);
        this.mRadioOverlayIsLocal = true;
        if (localBitmap1 != null)
          break label121;
      }
      for (Bitmap localBitmap2 = null; ; localBitmap2 = localBitmap1)
      {
        return localBitmap2;
        if (arrayOfString.length == 1)
        {
          String str = arrayOfString[0];
          Context localContext = paramContext;
          int i = paramInt1;
          int j = paramInt2;
          RemoteUrlSink localRemoteUrlSink = paramRemoteUrlSink;
          localBitmap1 = AsyncAlbumArtImageView.getBitmapForRemoteUrlOrDefault(localContext, str, i, j, localRemoteUrlSink, true);
          this.mRadioOverlayIsLocal = true;
          break;
        }
        localBitmap1 = super.createBitmap(paramContext, paramInt1, paramInt2, paramAlbumIdSink, paramRemoteUrlSink);
        this.mRadioOverlayIsLocal = false;
        break;
        label121: if (this.mRadioOverlayIsLocal)
        {
          if (!localBitmap1.isMutable())
          {
            Bitmap.Config localConfig2 = AlbumArtUtils.getPreferredConfig();
            localBitmap1 = localBitmap1.copy(localConfig2, true);
          }
          boolean bool = AlbumArtUtils.drawFauxRadioArtOverlay(paramContext, localBitmap1, paramInt1, paramInt2);
        }
      }
    }

    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof ExternalRadioMode));
      for (boolean bool = false; ; bool = super.equals(paramObject))
        return bool;
    }

    public boolean equals(String paramString)
    {
      return isEqual(this.mUrls, paramString);
    }

    public boolean isCacheable()
    {
      if (!this.mRadioOverlayIsLocal);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder().append("ExternalRadioMode<urls: ");
      String str = this.mUrls;
      return str + ">";
    }
  }

  private static class ExternalAlbumMode extends AsyncAlbumArtImageView.Mode
  {
    public String mUrl;

    public ExternalAlbumMode()
    {
      super();
    }

    public ExternalAlbumMode(ExternalAlbumMode paramExternalAlbumMode)
    {
      super();
      String str = paramExternalAlbumMode.mUrl;
      this.mUrl = str;
    }

    public AsyncAlbumArtImageView.Mode copy()
    {
      return new ExternalAlbumMode(this);
    }

    public Bitmap createBitmap(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, RemoteUrlSink paramRemoteUrlSink)
    {
      String str = this.mUrl;
      boolean bool = cropToSquare();
      Context localContext = paramContext;
      int i = paramInt1;
      int j = paramInt2;
      RemoteUrlSink localRemoteUrlSink = paramRemoteUrlSink;
      return AsyncAlbumArtImageView.getBitmapForRemoteUrlOrDefault(localContext, str, i, j, localRemoteUrlSink, bool);
    }

    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof ExternalAlbumMode));
      String str1;
      String str2;
      for (boolean bool = false; ; bool = isEqual(str1, str2))
      {
        return bool;
        ExternalAlbumMode localExternalAlbumMode = (ExternalAlbumMode)paramObject;
        str1 = this.mUrl;
        str2 = localExternalAlbumMode.mUrl;
      }
    }

    public boolean equals(String paramString)
    {
      return isEqual(this.mUrl, paramString);
    }

    public String getExternalUrl(Context paramContext)
    {
      return this.mUrl;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder().append("ExternalAlbumMode<url:");
      String str = this.mUrl;
      return str + ">";
    }
  }

  private static class ShuffleAllMode extends AsyncAlbumArtImageView.Mode
  {
    public String parentName;

    public ShuffleAllMode()
    {
      super();
    }

    public ShuffleAllMode(ShuffleAllMode paramShuffleAllMode)
    {
      super();
      String str = paramShuffleAllMode.parentName;
      this.parentName = str;
    }

    public AsyncAlbumArtImageView.Mode copy()
    {
      return new ShuffleAllMode(this);
    }

    public Bitmap createBitmap(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, RemoteUrlSink paramRemoteUrlSink)
    {
      long l = this.parentName.hashCode();
      String str = this.parentName;
      Context localContext = paramContext;
      int i = paramInt1;
      int j = paramInt2;
      AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
      return AlbumArtUtils.getFauxAlbumArt(localContext, 5, true, l, i, j, str, null, null, localAlbumIdSink, false);
    }

    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof ShuffleAllMode));
      String str1;
      String str2;
      for (boolean bool = false; ; bool = isEqual(str1, str2))
      {
        return bool;
        ShuffleAllMode localShuffleAllMode = (ShuffleAllMode)paramObject;
        str1 = this.parentName;
        str2 = localShuffleAllMode.parentName;
      }
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder().append("ShuffleAllMode<parentName:");
      String str = this.parentName;
      return str + ">";
    }
  }

  private static class PlaylistMode extends AsyncAlbumArtImageView.Mode
  {
    public long id;
    public String mainLabel;
    public int style;

    public PlaylistMode()
    {
      super();
    }

    public PlaylistMode(PlaylistMode paramPlaylistMode)
    {
      super();
      long l = paramPlaylistMode.id;
      this.id = l;
      String str = paramPlaylistMode.mainLabel;
      this.mainLabel = str;
      int i = paramPlaylistMode.style;
      this.style = i;
    }

    public AsyncAlbumArtImageView.Mode copy()
    {
      return new PlaylistMode(this);
    }

    public Bitmap createBitmap(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, RemoteUrlSink paramRemoteUrlSink)
    {
      int i = this.style;
      long l = this.id;
      String str = this.mainLabel;
      Context localContext = paramContext;
      int j = paramInt1;
      int k = paramInt2;
      AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
      return AlbumArtUtils.getFauxAlbumArt(localContext, i, false, l, j, k, str, null, null, localAlbumIdSink, false);
    }

    public boolean equals(Object paramObject)
    {
      boolean bool = false;
      if (!(paramObject instanceof PlaylistMode));
      while (true)
      {
        return bool;
        PlaylistMode localPlaylistMode = (PlaylistMode)paramObject;
        long l1 = this.id;
        long l2 = localPlaylistMode.id;
        if (l1 == l2)
        {
          String str1 = this.mainLabel;
          String str2 = localPlaylistMode.mainLabel;
          if (isEqual(str1, str2))
          {
            int i = this.style;
            int j = localPlaylistMode.style;
            if (i != j)
              bool = true;
          }
        }
      }
    }

    public boolean equals(String paramString, long paramLong, int paramInt)
    {
      if ((isEqual(this.mainLabel, paramString)) && (this.id == paramLong) && (this.style != paramInt));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("PlaylistMode<id:");
      long l = this.id;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(l).append(", mainLabel:");
      String str = this.mainLabel;
      StringBuilder localStringBuilder3 = localStringBuilder2.append(str).append(", style:");
      int i = this.style;
      return i + ">";
    }
  }

  private static class AllSongsMode extends AsyncAlbumArtImageView.Mode
  {
    public long id;
    public String parentName;
    public Uri uri;

    public AllSongsMode()
    {
      super();
    }

    public AllSongsMode(AllSongsMode paramAllSongsMode)
    {
      super();
      String str = paramAllSongsMode.parentName;
      this.parentName = str;
      long l = paramAllSongsMode.id;
      this.id = l;
      Uri localUri = paramAllSongsMode.uri;
      this.uri = localUri;
    }

    public AsyncAlbumArtImageView.Mode copy()
    {
      return new AllSongsMode(this);
    }

    public Bitmap createBitmap(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, RemoteUrlSink paramRemoteUrlSink)
    {
      AlbumArtUtils.AlbumIdIteratorFactory localAlbumIdIteratorFactory = null;
      if (this.uri != null)
      {
        Uri localUri = this.uri;
        localAlbumIdIteratorFactory = AlbumArtUtils.createAlbumIdIteratorFactoryForContentUri(paramContext, localUri);
      }
      long l = this.id;
      String str = this.parentName;
      Context localContext = paramContext;
      int i = paramInt1;
      int j = paramInt2;
      AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
      return AlbumArtUtils.getFauxAlbumArt(localContext, 5, false, l, i, j, str, null, localAlbumIdIteratorFactory, localAlbumIdSink, false);
    }

    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof AllSongsMode));
      String str;
      long l;
      Uri localUri;
      for (boolean bool = false; ; bool = equals(str, l, localUri))
      {
        return bool;
        AllSongsMode localAllSongsMode = (AllSongsMode)paramObject;
        str = localAllSongsMode.parentName;
        l = localAllSongsMode.id;
        localUri = localAllSongsMode.uri;
      }
    }

    public boolean equals(String paramString, long paramLong, Uri paramUri)
    {
      if ((isEqual(this.parentName, paramString)) && (this.id == paramLong) && (isEqual(this.uri, paramUri)));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder().append("AllSongsMode<parentName:");
      String str = this.parentName;
      return str + ">";
    }
  }

  private static class AlbumMode extends AsyncAlbumArtImageView.Mode
  {
    public String album;
    public long albumId;
    public String artist;

    public AlbumMode()
    {
      super();
    }

    public AlbumMode(AlbumMode paramAlbumMode)
    {
      super();
      long l = paramAlbumMode.albumId;
      this.albumId = l;
      String str1 = paramAlbumMode.album;
      this.album = str1;
      String str2 = paramAlbumMode.artist;
      this.artist = str2;
    }

    public AsyncAlbumArtImageView.Mode copy()
    {
      return new AlbumMode(this);
    }

    public Bitmap createBitmap(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, RemoteUrlSink paramRemoteUrlSink)
    {
      long l = this.albumId;
      Context localContext = paramContext;
      int i = paramInt1;
      int j = paramInt2;
      AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
      return AlbumArtUtils.getCachedBitmap(localContext, l, null, i, j, null, null, localAlbumIdSink, true, true, false);
    }

    public boolean equals(long paramLong, String paramString1, String paramString2)
    {
      if ((this.albumId == paramLong) && (isEqual(this.album, paramString1)) && (isEqual(this.artist, paramString2)));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean equals(Object paramObject)
    {
      if (!(paramObject instanceof AlbumMode));
      long l;
      String str1;
      String str2;
      for (boolean bool = false; ; bool = equals(l, str1, str2))
      {
        return bool;
        AlbumMode localAlbumMode = (AlbumMode)paramObject;
        l = localAlbumMode.albumId;
        str1 = localAlbumMode.album;
        str2 = localAlbumMode.artist;
      }
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("AlbumMode<albumId:");
      long l = this.albumId;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(l).append(", album:");
      String str1 = this.album;
      StringBuilder localStringBuilder3 = localStringBuilder2.append(str1).append(", artist:");
      String str2 = this.artist;
      return str2 + ">";
    }
  }

  private static abstract class Mode
  {
    protected static boolean isEqual(Uri paramUri1, Uri paramUri2)
    {
      boolean bool;
      if (paramUri1 == null)
        if (paramUri2 == null)
          bool = true;
      while (true)
      {
        return bool;
        bool = false;
        continue;
        bool = paramUri1.equals(paramUri2);
      }
    }

    protected static boolean isEqual(String paramString1, String paramString2)
    {
      boolean bool;
      if (paramString1 == null)
        if (paramString2 == null)
          bool = true;
      while (true)
      {
        return bool;
        bool = false;
        continue;
        bool = paramString1.equals(paramString2);
      }
    }

    public abstract Mode copy();

    public abstract Bitmap createBitmap(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, RemoteUrlSink paramRemoteUrlSink);

    public boolean cropToSquare()
    {
      return false;
    }

    public String getExternalUrl(Context paramContext)
    {
      return null;
    }

    public int hashCode()
    {
      throw new RuntimeException("hashCode is not implemented for Mode objects");
    }
  }

  public static enum LayerMode
  {
    static
    {
      LayerMode[] arrayOfLayerMode = new LayerMode[2];
      LayerMode localLayerMode1 = BACKGROUND;
      arrayOfLayerMode[0] = localLayerMode1;
      LayerMode localLayerMode2 = FOREGROUND;
      arrayOfLayerMode[1] = localLayerMode2;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.AsyncAlbumArtImageView
 * JD-Core Version:    0.6.2
 */