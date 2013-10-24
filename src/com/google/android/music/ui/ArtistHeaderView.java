package com.google.android.music.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.google.android.music.medialist.AlbumList;
import com.google.android.music.medialist.ArtistSongList;
import com.google.android.music.medialist.NautilusAlbumList;
import com.google.android.music.medialist.NautilusArtistSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.BitmapDiskCache;
import com.google.android.music.utils.BitmapDiskCache.Callback;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;

public class ArtistHeaderView extends RelativeLayout
  implements AbsListView.OnScrollListener, BitmapDiskCache.Callback
{
  private boolean mArtWasClearedOnStop = false;
  private ImageView mArtistArt;
  private String mArtistArtUrl;
  private long mArtistId;
  private String mArtistName;
  private SongList mPlayRadioSongList;
  private ImageView mRadioButton;
  private View mRadioButtonWrapper;
  private boolean mShowingNonDefaultArt = false;

  public ArtistHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void onBitmapResult(String paramString, Bitmap paramBitmap, Exception paramException)
  {
    if (paramBitmap == null)
      return;
    this.mArtistArt.setImageBitmap(paramBitmap);
    this.mShowingNonDefaultArt = true;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    ImageView localImageView1 = (ImageView)findViewById(2131296349);
    this.mArtistArt = localImageView1;
    View localView = findViewById(2131296350);
    this.mRadioButtonWrapper = localView;
    ImageView localImageView2 = (ImageView)findViewById(2131296351);
    this.mRadioButton = localImageView2;
    ImageView localImageView3 = this.mRadioButton;
    View.OnClickListener local2 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MusicUtils.runAsync(new Runnable()
        {
          public void run()
          {
            Context localContext = ArtistHeaderView.this.getContext();
            SongList localSongList = ArtistHeaderView.this.mPlayRadioSongList;
            MusicUtils.playRadio(localContext, localSongList);
          }
        });
      }
    };
    localImageView3.setOnClickListener(local2);
  }

  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
  {
    if (!MusicPreferences.isHoneycombOrGreater())
      return;
    View localView = paramAbsListView.getChildAt(0);
    if (localView != this)
      return;
    int i = -localView.getTop() / 2;
    ImageView localImageView = this.mArtistArt;
    float f = i;
    localImageView.setTranslationY(f);
  }

  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
  {
  }

  public void onStart()
  {
    if (!this.mArtWasClearedOnStop)
      return;
    BitmapDiskCache localBitmapDiskCache = BitmapDiskCache.getInstance(getContext());
    String str = this.mArtistArtUrl;
    localBitmapDiskCache.getBitmap(str, this);
  }

  public void onStop()
  {
    if (!this.mShowingNonDefaultArt)
      return;
    Drawable localDrawable = this.mArtistArt.getDrawable();
    if ((localDrawable != null) && ((localDrawable instanceof BitmapDrawable)))
      ((BitmapDrawable)localDrawable).getBitmap().recycle();
    this.mArtistArt.setImageDrawable(null);
    this.mShowingNonDefaultArt = false;
    this.mArtWasClearedOnStop = true;
  }

  public void setAlbumList(final AlbumList paramAlbumList)
  {
    MusicPreferences localMusicPreferences = UIStateManager.getInstance().getPrefs();
    if (!localMusicPreferences.hasStreamingAccount())
    {
      this.mRadioButtonWrapper.setVisibility(8);
      return;
    }
    if (!localMusicPreferences.isNautilusEnabled())
      this.mRadioButton.setImageResource(2130837714);
    MusicUtils.runAsyncWithCallback(new AsyncRunner()
    {
      private String mSavedArtistArtUrl;
      private long mSavedArtistId;
      private String mSavedArtistName;

      public void backgroundTask()
      {
        Context localContext1 = ArtistHeaderView.this.getContext();
        AlbumList localAlbumList1 = paramAlbumList;
        Context localContext2 = ArtistHeaderView.this.getContext();
        String str1 = localAlbumList1.getName(localContext2);
        this.mSavedArtistName = str1;
        AlbumList localAlbumList2 = paramAlbumList;
        Context localContext3 = ArtistHeaderView.this.getContext();
        long l1 = localAlbumList2.getArtistId(localContext3);
        this.mSavedArtistId = l1;
        if ((paramAlbumList instanceof NautilusAlbumList))
        {
          String str2 = ((NautilusAlbumList)paramAlbumList).getNautilusId();
          String str3 = MusicUtils.getNautilusArtistArtUrl(localContext1, str2);
          this.mSavedArtistArtUrl = str3;
          return;
        }
        long l2 = this.mSavedArtistId;
        String str4 = MusicUtils.getArtistArtUrl(localContext1, l2);
        this.mSavedArtistArtUrl = str4;
      }

      public void taskCompleted()
      {
        if (ArtistHeaderView.this.getContext() == null)
          return;
        ArtistHeaderView localArtistHeaderView1 = ArtistHeaderView.this;
        String str1 = this.mSavedArtistName;
        String str2 = ArtistHeaderView.access$002(localArtistHeaderView1, str1);
        ArtistHeaderView localArtistHeaderView2 = ArtistHeaderView.this;
        long l1 = this.mSavedArtistId;
        long l2 = ArtistHeaderView.access$102(localArtistHeaderView2, l1);
        ArtistHeaderView localArtistHeaderView3 = ArtistHeaderView.this;
        String str3 = this.mSavedArtistArtUrl;
        String str4 = ArtistHeaderView.access$202(localArtistHeaderView3, str3);
        if (!TextUtils.isEmpty(this.mSavedArtistArtUrl))
        {
          BitmapDiskCache localBitmapDiskCache = BitmapDiskCache.getInstance(ArtistHeaderView.this.getContext());
          String str5 = this.mSavedArtistArtUrl;
          ArtistHeaderView localArtistHeaderView4 = ArtistHeaderView.this;
          localBitmapDiskCache.getBitmap(str5, localArtistHeaderView4);
        }
        if ((paramAlbumList instanceof NautilusAlbumList))
        {
          String str6 = ((NautilusAlbumList)paramAlbumList).getNautilusId();
          ArtistHeaderView localArtistHeaderView5 = ArtistHeaderView.this;
          String str7 = ArtistHeaderView.this.mArtistName;
          NautilusArtistSongList localNautilusArtistSongList = new NautilusArtistSongList(str6, str7);
          SongList localSongList1 = ArtistHeaderView.access$302(localArtistHeaderView5, localNautilusArtistSongList);
          return;
        }
        ArtistHeaderView localArtistHeaderView6 = ArtistHeaderView.this;
        long l3 = ArtistHeaderView.this.mArtistId;
        String str8 = ArtistHeaderView.this.mArtistName;
        ArtistSongList localArtistSongList = new ArtistSongList(l3, str8, -1, false);
        SongList localSongList2 = ArtistHeaderView.access$302(localArtistHeaderView6, localArtistSongList);
      }
    });
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.ArtistHeaderView
 * JD-Core Version:    0.6.2
 */