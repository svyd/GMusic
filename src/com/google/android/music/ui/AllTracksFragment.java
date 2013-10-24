package com.google.android.music.ui;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import com.google.android.music.medialist.AllSongsList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.MusicUtils;

public class AllTracksFragment extends BaseTrackListFragment
{
  private MediaListCursorAdapter mAdapter;
  private View mShuffleRow;

  protected void initEmptyScreen()
  {
    super.initEmptyScreen();
    if (UIStateManager.getInstance().isDisplayingLocalContent())
    {
      setEmptyScreenText(2131231364);
      setEmptyScreenLearnMoreVisible(true);
      return;
    }
    setEmptyScreenText(2131231363);
    setEmptyScreenVisible(false);
  }

  MediaListCursorAdapter newAsyncCursorAdapter(Cursor paramCursor)
  {
    MediaListCursorAdapter localMediaListCursorAdapter = super.newAsyncCursorAdapter(paramCursor);
    this.mAdapter = localMediaListCursorAdapter;
    if (paramCursor != null)
    {
      DataSetObserver local1 = new DataSetObserver()
      {
        public void onChanged()
        {
          if (AllTracksFragment.this.mAdapter.getCount() <= 1)
            return;
          AllTracksFragment.this.mShuffleRow.setVisibility(0);
        }
      };
      paramCursor.registerDataSetObserver(local1);
    }
    return this.mAdapter;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    if (!isInitialized())
    {
      int i = getPreferences().getAllSongsSortOrder();
      AllSongsList localAllSongsList = new AllSongsList(i);
      String[] arrayOfString = TrackListAdapter.PROJECTION;
      init(localAllSongsList, arrayOfString, true);
    }
    BaseTrackListView localBaseTrackListView = getTrackListView();
    View localView1 = LayoutInflater.from(getActivity()).inflate(2130968678, localBaseTrackListView, false);
    this.mShuffleRow = localView1;
    this.mShuffleRow.setVisibility(8);
    View localView2 = this.mShuffleRow;
    localBaseTrackListView.addHeaderView(localView2, null, true);
    super.onActivityCreated(paramBundle);
    localBaseTrackListView.showTrackArtist(true);
  }

  public final void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    View localView = this.mShuffleRow;
    if (paramView == localView)
    {
      MusicUtils.shuffleAll();
      return;
    }
    super.onListItemClick(paramListView, paramView, paramInt, paramLong);
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    getListView().setFastScrollEnabled(true);
    if (Build.VERSION.SDK_INT < 11)
      return;
    getListView().setScrollBarStyle(16777216);
    getListView().setFastScrollAlwaysVisible(true);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.AllTracksFragment
 * JD-Core Version:    0.6.2
 */