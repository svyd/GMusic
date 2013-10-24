package com.google.android.music.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.music.AsyncAlbumArtImageView;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.SongList;

public class AllSongsView extends LinearLayout
  implements View.OnClickListener
{
  private AsyncAlbumArtImageView mArt;
  private View mContent;
  private SongList mSongList;
  private TextView mSubtitle;
  private TextView mTitle;

  public AllSongsView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void hide()
  {
    this.mContent.setVisibility(8);
  }

  public void onClick(View paramView)
  {
    if (this.mSongList == null)
    {
      Log.w("AllSongsView", "Song list not yet initialized");
      return;
    }
    if (paramView != this)
      return;
    Context localContext = getContext();
    SongList localSongList = this.mSongList;
    Intent localIntent = TrackContainerActivity.buildStartIntent(localContext, localSongList, null);
    localContext.startActivity(localIntent);
  }

  public void onFinishInflate()
  {
    View localView = findViewById(2131296344);
    this.mContent = localView;
    AsyncAlbumArtImageView localAsyncAlbumArtImageView = (AsyncAlbumArtImageView)findViewById(2131296345);
    this.mArt = localAsyncAlbumArtImageView;
    TextView localTextView1 = (TextView)findViewById(2131296347);
    this.mTitle = localTextView1;
    TextView localTextView2 = (TextView)findViewById(2131296348);
    this.mSubtitle = localTextView2;
    this.mTitle.setText(2131230751);
    setOnClickListener(this);
  }

  public void setSongList(SongList paramSongList)
  {
    this.mSongList = paramSongList;
    this.mArt.setArtForSonglist(paramSongList);
  }

  public void show()
  {
    this.mContent.setVisibility(0);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.AllSongsView
 * JD-Core Version:    0.6.2
 */