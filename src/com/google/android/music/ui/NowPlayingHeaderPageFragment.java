package com.google.android.music.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.music.AsyncAlbumArtImageView;

public class NowPlayingHeaderPageFragment extends BaseFragment
  implements View.OnClickListener
{
  private long mAlbumId;
  private String mAlbumName;
  private AsyncAlbumArtImageView mAlbumSmall;
  private String mArtUrl;
  private String mArtistName;
  private TextView mArtistNameView;
  private ProgressBar mBufferingProgress;
  private View mHeader;
  private int mQueuePosition = -1;
  private View mRootView;
  private BroadcastReceiver mStatusListener;
  private String mTitle;
  private TextView mTrackNameView;

  public NowPlayingHeaderPageFragment()
  {
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        long l1 = paramAnonymousIntent.getLongExtra("ListPosition", 65535L);
        if (NowPlayingHeaderPageFragment.this.mQueuePosition == -1)
          return;
        long l2 = NowPlayingHeaderPageFragment.this.mQueuePosition;
        if (l1 != l2)
          return;
        NowPlayingHeaderPageFragment.this.updateStreaming(paramAnonymousIntent);
      }
    };
    this.mStatusListener = local1;
  }

  private View findViewById(int paramInt)
  {
    return this.mRootView.findViewById(paramInt);
  }

  private void initializeView()
  {
    View localView = findViewById(2131296441);
    this.mHeader = localView;
    AsyncAlbumArtImageView localAsyncAlbumArtImageView = (AsyncAlbumArtImageView)findViewById(2131296439);
    this.mAlbumSmall = localAsyncAlbumArtImageView;
    TextView localTextView1 = (TextView)findViewById(2131296442);
    this.mTrackNameView = localTextView1;
    TextView localTextView2 = (TextView)findViewById(2131296443);
    this.mArtistNameView = localTextView2;
    ProgressBar localProgressBar = (ProgressBar)findViewById(2131296440);
    this.mBufferingProgress = localProgressBar;
    this.mAlbumSmall.setOnClickListener(this);
    this.mHeader.setOnClickListener(this);
  }

  public static NowPlayingHeaderPageFragment newInstance(int paramInt, String paramString1, String paramString2, String paramString3, long paramLong, String paramString4)
  {
    NowPlayingHeaderPageFragment localNowPlayingHeaderPageFragment = new NowPlayingHeaderPageFragment();
    Bundle localBundle = new Bundle();
    localBundle.putInt("position", paramInt);
    localBundle.putString("title", paramString1);
    localBundle.putString("albumName", paramString2);
    localBundle.putString("artistName", paramString3);
    localBundle.putLong("albumId", paramLong);
    localBundle.putString("artUrl", paramString4);
    localNowPlayingHeaderPageFragment.setArguments(localBundle);
    return localNowPlayingHeaderPageFragment;
  }

  private void updateStreaming(Intent paramIntent)
  {
    boolean bool1 = paramIntent.getBooleanExtra("inErrorState", false);
    boolean bool2 = paramIntent.getBooleanExtra("streaming", false);
    boolean bool3 = paramIntent.getBooleanExtra("preparing", false);
    if (bool1)
    {
      this.mAlbumSmall.setAvailable(false);
      this.mBufferingProgress.setVisibility(8);
      return;
    }
    if ((bool3) && (bool2))
    {
      this.mAlbumSmall.setAvailable(false);
      this.mBufferingProgress.setVisibility(0);
      return;
    }
    this.mAlbumSmall.setAvailable(true);
    this.mBufferingProgress.setVisibility(8);
  }

  private void updateTrackInfo()
  {
    TextView localTextView1 = this.mTrackNameView;
    String str1 = this.mTitle;
    localTextView1.setText(str1);
    TextView localTextView2 = this.mArtistNameView;
    String str2 = this.mArtistName;
    localTextView2.setText(str2);
  }

  public void onClick(View paramView)
  {
    Intent localIntent1 = new Intent();
    AsyncAlbumArtImageView localAsyncAlbumArtImageView = this.mAlbumSmall;
    if (paramView != localAsyncAlbumArtImageView)
    {
      View localView = this.mHeader;
      if (paramView != localView);
    }
    else
    {
      Intent localIntent2 = localIntent1.setAction("com.google.android.music.nowplaying.HEADER_CLICKED");
    }
    while (true)
    {
      boolean bool = LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(localIntent1);
      return;
      String str = "Unknown view clicked: " + paramView;
      int i = Log.wtf("NowPlayingHeaderPageFragment", str);
    }
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView1 = super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    View localView2 = paramLayoutInflater.inflate(2130968643, paramViewGroup, false);
    this.mRootView = localView2;
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.android.music.playstatechanged");
    localIntentFilter.addAction("com.android.music.metachanged");
    localIntentFilter.addAction("com.android.music.asyncopencomplete");
    localIntentFilter.addAction("com.android.music.asyncopenstart");
    localIntentFilter.addAction("com.android.music.playbackfailed");
    FragmentActivity localFragmentActivity = getActivity();
    BroadcastReceiver localBroadcastReceiver = this.mStatusListener;
    Intent localIntent = localFragmentActivity.registerReceiver(localBroadcastReceiver, localIntentFilter);
    Bundle localBundle = getArguments();
    int i = localBundle.getInt("position");
    this.mQueuePosition = i;
    String str1 = localBundle.getString("title");
    this.mTitle = str1;
    String str2 = localBundle.getString("artistName");
    this.mArtistName = str2;
    String str3 = localBundle.getString("albumName");
    this.mAlbumName = str3;
    long l = localBundle.getLong("albumId");
    this.mAlbumId = l;
    String str4 = localBundle.getString("artUrl");
    this.mArtUrl = str4;
    initializeView();
    updateTrackInfo();
    return this.mRootView;
  }

  public void onDestroyView()
  {
    FragmentActivity localFragmentActivity = getActivity();
    BroadcastReceiver localBroadcastReceiver = this.mStatusListener;
    localFragmentActivity.unregisterReceiver(localBroadcastReceiver);
    super.onDestroyView();
  }

  public void onStart()
  {
    super.onStart();
    if (!TextUtils.isEmpty(this.mArtUrl))
    {
      AsyncAlbumArtImageView localAsyncAlbumArtImageView1 = this.mAlbumSmall;
      String str = this.mArtUrl;
      localAsyncAlbumArtImageView1.setExternalAlbumArt(str);
      return;
    }
    AsyncAlbumArtImageView localAsyncAlbumArtImageView2 = this.mAlbumSmall;
    long l = this.mAlbumId;
    localAsyncAlbumArtImageView2.setAlbumId(l, null, null);
  }

  public void onStop()
  {
    super.onStop();
    this.mAlbumSmall.clearArtwork();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.NowPlayingHeaderPageFragment
 * JD-Core Version:    0.6.2
 */