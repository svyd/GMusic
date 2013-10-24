package com.google.android.music.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.medialist.SongList;

public abstract class BaseTrackListFragment extends MediaListFragment
{
  private TrackListAdapter getTrackListAdapter()
  {
    return (TrackListAdapter)getListAdapter();
  }

  protected BaseTrackListView getTrackListView()
  {
    return (BaseTrackListView)getListView();
  }

  MediaListCursorAdapter newAsyncCursorAdapter(Cursor paramCursor)
  {
    MediaList localMediaList = getMediaList();
    return new TrackListAdapter(this, false, localMediaList, paramCursor);
  }

  MediaListCursorAdapter newLoaderCursorAdapter()
  {
    MediaList localMediaList = getMediaList();
    return new TrackListAdapter(this, false, localMediaList);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    BaseTrackListView localBaseTrackListView = getTrackListView();
    SongList localSongList = (SongList)getMediaList();
    localBaseTrackListView.setSongList(localSongList);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(2130968691, paramViewGroup, false);
  }

  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    if ((paramListView instanceof BaseTrackListView))
    {
      BaseTrackListView localBaseTrackListView = (BaseTrackListView)paramListView;
      ListView localListView = paramListView;
      View localView = paramView;
      int i = paramInt;
      long l = paramLong;
      localBaseTrackListView.handleItemClick(localListView, localView, i, l);
      return;
    }
    int j = Log.w("BaseTrackListFragment", "Unknown listview type");
  }

  public void onPause()
  {
    super.onPause();
    getTrackListAdapter().disablePlaybackIndicator();
  }

  public void onResume()
  {
    getTrackListAdapter().enablePlaybackIndicator();
    super.onResume();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.BaseTrackListFragment
 * JD-Core Version:    0.6.2
 */