package com.google.android.music.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.medialist.NautilusAlbumSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;

public class TrackContainerFragment extends BaseTrackListFragment
{
  private TrackListAdapter mAdapter;
  private long mAlbumId = 65535L;
  private String mAlbumMetajamId;
  private Document mContainerDocument;
  private ContainerHeaderView mHeader;
  private SongList mSongList;
  private BaseTrackListView mTrackList;
  private View mView;

  private void extractIds()
  {
    MusicUtils.runAsyncWithCallback(new AsyncRunner()
    {
      private long mSavedAlbumId = 65535L;
      private String mSavedAlbumMetajamId;

      public void backgroundTask()
      {
        FragmentActivity localFragmentActivity = TrackContainerFragment.this.getActivity();
        if (localFragmentActivity == null)
          return;
        if ((TrackContainerFragment.this.mSongList instanceof NautilusAlbumSongList))
        {
          String str = ((NautilusAlbumSongList)TrackContainerFragment.this.mSongList).getNautilusId();
          this.mSavedAlbumMetajamId = str;
        }
        long l = TrackContainerFragment.this.mSongList.getAlbumId(localFragmentActivity);
        this.mSavedAlbumId = l;
      }

      public void taskCompleted()
      {
        TrackContainerFragment localTrackContainerFragment1 = TrackContainerFragment.this;
        long l1 = this.mSavedAlbumId;
        long l2 = TrackContainerFragment.access$202(localTrackContainerFragment1, l1);
        TrackContainerFragment localTrackContainerFragment2 = TrackContainerFragment.this;
        String str1 = this.mSavedAlbumMetajamId;
        String str2 = TrackContainerFragment.access$302(localTrackContainerFragment2, str1);
      }
    });
  }

  public static TrackContainerFragment newInstance(MediaList paramMediaList, Document paramDocument)
  {
    TrackContainerFragment localTrackContainerFragment = new TrackContainerFragment();
    String[] arrayOfString = TrackListAdapter.PROJECTION;
    localTrackContainerFragment.init(paramMediaList, paramDocument, arrayOfString, false);
    localTrackContainerFragment.saveMediaListAsArguments();
    return localTrackContainerFragment;
  }

  public long getAlbumId()
  {
    return this.mAlbumId;
  }

  public String getAlbumMetajamId()
  {
    return this.mAlbumMetajamId;
  }

  protected void init(MediaList paramMediaList, Document paramDocument, String[] paramArrayOfString, boolean paramBoolean)
  {
    super.init(paramMediaList, paramArrayOfString, paramBoolean);
    this.mContainerDocument = paramDocument;
  }

  protected void initEmptyScreen()
  {
  }

  MediaListCursorAdapter newAsyncCursorAdapter(Cursor paramCursor)
  {
    MediaList localMediaList = getMediaList();
    TrackListAdapter localTrackListAdapter = new TrackListAdapter(this, true, localMediaList, paramCursor);
    this.mAdapter = localTrackListAdapter;
    this.mHeader.setCursor(paramCursor);
    return this.mAdapter;
  }

  MediaListCursorAdapter newLoaderCursorAdapter()
  {
    MediaList localMediaList = getMediaList();
    TrackListAdapter localTrackListAdapter = new TrackListAdapter(this, true, localMediaList);
    this.mAdapter = localTrackListAdapter;
    return this.mAdapter;
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    this.mView = localView;
    SongList localSongList1 = (SongList)getMediaList();
    this.mSongList = localSongList1;
    ContainerHeaderView localContainerHeaderView1 = (ContainerHeaderView)paramLayoutInflater.inflate(2130968608, null);
    this.mHeader = localContainerHeaderView1;
    this.mHeader.setClickable(false);
    this.mHeader.setFragment(this);
    ContainerHeaderView localContainerHeaderView2 = this.mHeader;
    SongList localSongList2 = this.mSongList;
    localContainerHeaderView2.setSongList(localSongList2);
    ContainerHeaderView localContainerHeaderView3 = this.mHeader;
    Document localDocument = this.mContainerDocument;
    localContainerHeaderView3.setContainerDocument(localDocument);
    ContainerHeaderView localContainerHeaderView4 = this.mHeader;
    MusicPreferences localMusicPreferences = getPreferences();
    localContainerHeaderView4.setMusicPreferences(localMusicPreferences);
    BaseTrackListView localBaseTrackListView1 = (BaseTrackListView)this.mView.findViewById(16908298);
    this.mTrackList = localBaseTrackListView1;
    BaseTrackListView localBaseTrackListView2 = this.mTrackList;
    ContainerHeaderView localContainerHeaderView5 = this.mHeader;
    localBaseTrackListView2.addHeaderView(localContainerHeaderView5, null, false);
    this.mTrackList.setHeaderDividersEnabled(false);
    BaseTrackListView localBaseTrackListView3 = this.mTrackList;
    AbsListView.OnScrollListener local1 = new AbsListView.OnScrollListener()
    {
      public void onScroll(AbsListView paramAnonymousAbsListView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
        TrackContainerFragment.this.mHeader.onScroll(paramAnonymousAbsListView, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
        View localView = paramAnonymousAbsListView.getChildAt(0);
        ContainerHeaderView localContainerHeaderView = TrackContainerFragment.this.mHeader;
        if ((localView == localContainerHeaderView) && (TrackContainerFragment.this.mHeader.isArtistArtShown()))
        {
          int i = -localView.getTop();
          int j = TrackContainerFragment.this.mHeader.getHeight();
          int k = TrackContainerFragment.this.mHeader.getAlbumArtHeight();
          float f = j - k;
          int m = (int)(i / f * 255.0F);
          if (m > 255)
            m = 255;
          if (m < 0)
            m = 0;
          TrackContainerFragment.this.getActionBarController().setActionBarAlpha(m);
          return;
        }
        TrackContainerFragment.this.getActionBarController().setActionBarAlpha(255);
      }

      public void onScrollStateChanged(AbsListView paramAnonymousAbsListView, int paramAnonymousInt)
      {
      }
    };
    localBaseTrackListView3.setOnScrollListener(local1);
    this.mTrackList.setFastScrollEnabled(false);
    extractIds();
    return this.mView;
  }

  public void onDestroyView()
  {
    this.mHeader.onDestroyView();
    super.onDestroyView();
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    if (this.mAdapter != null);
    for (int i = this.mAdapter.getCount(); ; i = 0)
    {
      super.onLoadFinished(paramLoader, paramCursor);
      this.mHeader.setCursor(paramCursor);
      if (i == 0)
        return;
      if (this.mAdapter.getCount() != 0)
        return;
      FragmentActivity localFragmentActivity = getActivity();
      if (localFragmentActivity == null)
        return;
      localFragmentActivity.finish();
      return;
    }
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
    super.onLoaderReset(paramLoader);
    this.mHeader.setCursor(null);
  }

  public void onStart()
  {
    super.onStart();
    this.mHeader.onStart();
  }

  public void onStop()
  {
    super.onStop();
    this.mHeader.onStop();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.TrackContainerFragment
 * JD-Core Version:    0.6.2
 */