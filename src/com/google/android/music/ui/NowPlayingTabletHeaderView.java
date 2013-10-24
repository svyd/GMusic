package com.google.android.music.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.music.AsyncAlbumArtImageView;

public class NowPlayingTabletHeaderView extends RelativeLayout
  implements View.OnClickListener
{
  private long mAlbumId;
  private String mAlbumName;
  private AsyncAlbumArtImageView mAlbumSmall;
  private String mArtUrl;
  private String mArtistName;
  private TextView mArtistNameView;
  private ProgressBar mBufferingProgress;
  private int mClosedWidth = -1;
  private boolean mIsClosed = false;
  private View mMetadataView;
  private int mOpenHeaderRightPadding;
  private BroadcastReceiver mStatusListener;
  private String mTitle;
  private TextView mTrackNameView;

  public NowPlayingTabletHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        boolean bool1 = paramAnonymousIntent.getBooleanExtra("inErrorState", false);
        boolean bool2 = paramAnonymousIntent.getBooleanExtra("streaming", false);
        boolean bool3 = paramAnonymousIntent.getBooleanExtra("preparing", false);
        NowPlayingTabletHeaderView.this.updateStreaming(bool1, bool2, bool3);
      }
    };
    this.mStatusListener = local1;
    View localView = View.inflate(paramContext, 2130968644, this);
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.android.music.playstatechanged");
    localIntentFilter.addAction("com.android.music.metachanged");
    localIntentFilter.addAction("com.android.music.asyncopencomplete");
    localIntentFilter.addAction("com.android.music.asyncopenstart");
    localIntentFilter.addAction("com.android.music.playbackfailed");
    BroadcastReceiver localBroadcastReceiver = this.mStatusListener;
    Intent localIntent = paramContext.registerReceiver(localBroadcastReceiver, localIntentFilter);
    int i = getResources().getDimensionPixelSize(2131558465);
    this.mOpenHeaderRightPadding = i;
    initializeView();
  }

  private void initializeView()
  {
    AsyncAlbumArtImageView localAsyncAlbumArtImageView = (AsyncAlbumArtImageView)findViewById(2131296439);
    this.mAlbumSmall = localAsyncAlbumArtImageView;
    TextView localTextView1 = (TextView)findViewById(2131296442);
    this.mTrackNameView = localTextView1;
    TextView localTextView2 = (TextView)findViewById(2131296443);
    this.mArtistNameView = localTextView2;
    ProgressBar localProgressBar = (ProgressBar)findViewById(2131296440);
    this.mBufferingProgress = localProgressBar;
    View localView = findViewById(2131296444);
    this.mMetadataView = localView;
    setOnClickListener(this);
    this.mMetadataView.setOnClickListener(this);
  }

  private void updateStreaming(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    if (paramBoolean1)
    {
      this.mAlbumSmall.setAvailable(false);
      this.mBufferingProgress.setVisibility(8);
      return;
    }
    if ((paramBoolean3) && (paramBoolean2))
    {
      this.mAlbumSmall.setAvailable(false);
      this.mBufferingProgress.setVisibility(0);
      return;
    }
    this.mAlbumSmall.setAvailable(true);
    this.mBufferingProgress.setVisibility(8);
  }

  private void updateWidth()
  {
    ViewGroup.LayoutParams localLayoutParams = this.mMetadataView.getLayoutParams();
    int i;
    View localView;
    if (this.mIsClosed)
    {
      i = this.mClosedWidth;
      localLayoutParams.width = i;
      this.mMetadataView.setLayoutParams(localLayoutParams);
      localView = this.mMetadataView;
      if (!this.mIsClosed)
        break label64;
    }
    label64: for (int j = 0; ; j = this.mOpenHeaderRightPadding)
    {
      localView.setPadding(0, 0, j, 0);
      return;
      i = -1;
      break;
    }
  }

  public void onClick(View paramView)
  {
    Intent localIntent1 = new Intent();
    Intent localIntent2 = localIntent1.setAction("com.google.android.music.nowplaying.HEADER_CLICKED");
    boolean bool = LocalBroadcastManager.getInstance(getContext()).sendBroadcast(localIntent1);
  }

  public void onDestroyView()
  {
    Context localContext = getContext();
    BroadcastReceiver localBroadcastReceiver = this.mStatusListener;
    localContext.unregisterReceiver(localBroadcastReceiver);
  }

  public void setIsClosed(boolean paramBoolean)
  {
    int i = -1;
    this.mIsClosed = paramBoolean;
    View localView1 = this.mMetadataView;
    int j;
    int k;
    label41: View localView3;
    if (paramBoolean)
    {
      j = 2131296488;
      localView1.setNextFocusRightId(j);
      View localView2 = this.mMetadataView;
      if (!paramBoolean)
        break label76;
      k = 2131296534;
      localView2.setNextFocusUpId(k);
      localView3 = this.mMetadataView;
      if (!paramBoolean)
        break label83;
    }
    while (true)
    {
      localView3.setNextFocusDownId(i);
      updateWidth();
      return;
      j = 2131296453;
      break;
      label76: k = -1;
      break label41;
      label83: i = 2131296487;
    }
  }

  public void setMetadataViewWidth(int paramInt)
  {
    if ((paramInt == -1) || (paramInt == -1) || (paramInt == -1));
    int i;
    for (this.mClosedWidth = paramInt; ; this.mClosedWidth = i)
    {
      updateWidth();
      return;
      i = (int)(paramInt * 0.3F);
    }
  }

  public void updateView(String paramString1, String paramString2, String paramString3, Long paramLong, String paramString4)
  {
    this.mTitle = paramString1;
    this.mArtistName = paramString2;
    this.mAlbumName = paramString3;
    long l1 = paramLong.longValue();
    this.mAlbumId = l1;
    this.mArtUrl = paramString4;
    if (!TextUtils.isEmpty(this.mArtUrl))
    {
      AsyncAlbumArtImageView localAsyncAlbumArtImageView1 = this.mAlbumSmall;
      String str1 = this.mArtUrl;
      localAsyncAlbumArtImageView1.setExternalAlbumArt(str1);
    }
    while (true)
    {
      TextView localTextView1 = this.mTrackNameView;
      String str2 = this.mTitle;
      localTextView1.setText(str2);
      TextView localTextView2 = this.mArtistNameView;
      String str3 = this.mArtistName;
      localTextView2.setText(str3);
      return;
      AsyncAlbumArtImageView localAsyncAlbumArtImageView2 = this.mAlbumSmall;
      long l2 = this.mAlbumId;
      localAsyncAlbumArtImageView2.setAlbumId(l2, null, null);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.NowPlayingTabletHeaderView
 * JD-Core Version:    0.6.2
 */