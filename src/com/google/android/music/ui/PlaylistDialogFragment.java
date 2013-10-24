package com.google.android.music.ui;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.SongList;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.ui.cardlib.utils.Lists;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlaylistDialogFragment extends DialogFragment
  implements LoaderManager.LoaderCallbacks<Cursor>
{
  private static final int[] SECTION_TITLES = { 2131230907, 2131231262, 2131231263 };
  private MultiAdaptersListAdapter mAdapter;
  private final boolean mExcludeFollowedPlaylists;
  private long mExcludedPlaylistId;
  private final int mItemLayoutId = 2130968602;
  private ListView mListView;
  private AdapterView.OnItemClickListener mOnItemClickListener;
  private boolean mShowNewPlaylist = true;
  private final int mTitleResourceId;

  public PlaylistDialogFragment(int paramInt, boolean paramBoolean)
  {
    this.mTitleResourceId = paramInt;
    this.mExcludeFollowedPlaylists = paramBoolean;
  }

  public static Bundle createArgs(SongList paramSongList, long paramLong)
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("songList", paramSongList);
    localBundle.putLong("excludePlaylist", paramLong);
    return localBundle;
  }

  private void startLoading()
  {
    LoaderManager localLoaderManager = getLoaderManager();
    int i = 1;
    if (i >= 3)
      return;
    if (localLoaderManager.getLoader(i) != null)
      Loader localLoader1 = localLoaderManager.restartLoader(i, null, this);
    while (true)
    {
      i += 1;
      break;
      Loader localLoader2 = localLoaderManager.initLoader(i, null, this);
    }
  }

  public Object getAdapterItem(int paramInt)
  {
    return this.mAdapter.getItem(paramInt);
  }

  public Dialog onCreateDialog(Bundle paramBundle)
  {
    View localView = getActivity().getLayoutInflater().inflate(2130968600, null);
    ListView localListView1 = (ListView)localView.findViewById(16908298);
    this.mListView = localListView1;
    ListView localListView2 = this.mListView;
    AdapterView.OnItemClickListener localOnItemClickListener = this.mOnItemClickListener;
    localListView2.setOnItemClickListener(localOnItemClickListener);
    Bundle localBundle = getArguments();
    if (localBundle != null)
    {
      long l = localBundle.getLong("excludePlaylist", 65535L);
      this.mExcludedPlaylistId = l;
    }
    MultiAdaptersListAdapter localMultiAdaptersListAdapter = new MultiAdaptersListAdapter(3);
    this.mAdapter = localMultiAdaptersListAdapter;
    int i = 0;
    while (i < 3)
    {
      this.mAdapter.addSection(null);
      i += 1;
    }
    FragmentActivity localFragmentActivity1 = getActivity();
    ArrayList localArrayList = new ArrayList();
    if (this.mShowNewPlaylist)
    {
      int j = SECTION_TITLES[0];
      String str = localFragmentActivity1.getString(j);
      PlayListInfo localPlayListInfo = new PlayListInfo(65535L, str);
      boolean bool = localArrayList.add(localPlayListInfo);
    }
    HeaderListAdapter localHeaderListAdapter = new HeaderListAdapter(localFragmentActivity1, null, 2130968602, localArrayList);
    this.mAdapter.setSection(0, localHeaderListAdapter);
    startLoading();
    FragmentActivity localFragmentActivity2 = getActivity();
    AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(localFragmentActivity2);
    Resources localResources = getResources();
    int k = this.mTitleResourceId;
    CharSequence localCharSequence1 = localResources.getText(k);
    AlertDialog.Builder localBuilder2 = localBuilder1.setTitle(localCharSequence1);
    AlertDialog.Builder localBuilder3 = localBuilder1.setPositiveButton(null, null);
    CharSequence localCharSequence2 = localResources.getText(17039360);
    AlertDialog.Builder localBuilder4 = localBuilder1.setNegativeButton(localCharSequence2, null);
    AlertDialog.Builder localBuilder5 = localBuilder1.setView(localView);
    return localBuilder1.create();
  }

  public Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    Object localObject = null;
    boolean bool;
    switch (paramInt)
    {
    default:
      String str1 = "onCreateLoader undexpected id: " + paramInt;
      Log.e("AddToPlaylistFragment", str1);
      return localObject;
    case 1:
      bool = this.mExcludeFollowedPlaylists;
    case 2:
    }
    for (Uri localUri = MusicContent.Playlists.getRecentPlaylistUri(5, bool); ; localUri = MusicContent.Playlists.getPlaylistsUri(this.mExcludeFollowedPlaylists))
    {
      FragmentActivity localFragmentActivity = getActivity();
      String[] arrayOfString1 = PlaylistClustersFragment.CURSOR_COLUMNS;
      String[] arrayOfString2 = null;
      String str2 = null;
      localObject = new CursorLoader(localFragmentActivity, localUri, arrayOfString1, null, arrayOfString2, str2);
      break;
    }
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    int i = paramLoader.getId();
    int j = paramCursor.getCount();
    ArrayList localArrayList = Lists.newArrayList(j);
    if (i == 1);
    for (int k = 4; ; k = j)
    {
      int m = 0;
      while ((paramCursor.moveToNext()) && (m < k))
      {
        long l1 = paramCursor.getLong(0);
        long l2 = this.mExcludedPlaylistId;
        if (l1 != l2)
        {
          String str1 = paramCursor.getString(1);
          PlayListInfo localPlayListInfo = new PlayListInfo(l1, str1);
          boolean bool = localArrayList.add(localPlayListInfo);
          m += 1;
        }
      }
    }
    FragmentActivity localFragmentActivity1 = getActivity();
    int n = SECTION_TITLES[i];
    String str2 = localFragmentActivity1.getString(n);
    FragmentActivity localFragmentActivity2 = getActivity();
    HeaderListAdapter localHeaderListAdapter = new HeaderListAdapter(localFragmentActivity2, str2, 2130968602, localArrayList);
    this.mAdapter.setSection(i, localHeaderListAdapter);
    if (this.mAdapter.hasAllAdapters())
    {
      ListView localListView = this.mListView;
      MultiAdaptersListAdapter localMultiAdaptersListAdapter = this.mAdapter;
      localListView.setAdapter(localMultiAdaptersListAdapter);
    }
    this.mAdapter.notifyDataSetChanged();
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
  }

  public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener)
  {
    this.mOnItemClickListener = paramOnItemClickListener;
  }

  public void setShowNewPlaylist(boolean paramBoolean)
  {
    this.mShowNewPlaylist = paramBoolean;
  }

  protected static class HeaderListAdapter extends ArrayAdapter<PlaylistDialogFragment.PlayListInfo>
    implements MultiAdaptersListAdapter.HeaderAdapter
  {
    private final String mHeader;
    private final int mLayoutId;

    HeaderListAdapter(Context paramContext, String paramString, int paramInt, List<PlaylistDialogFragment.PlayListInfo> paramList)
    {
      super(paramInt, paramList);
      this.mHeader = paramString;
      this.mLayoutId = paramInt;
    }

    public void addLayoutIds(Set<Integer> paramSet)
    {
      Integer localInteger = Integer.valueOf(this.mLayoutId);
      boolean bool = paramSet.add(localInteger);
    }

    public String getHeader()
    {
      return this.mHeader;
    }

    public int getItemLayoutId(int paramInt)
    {
      return this.mLayoutId;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      TextView localTextView = (TextView)super.getView(paramInt, paramView, paramViewGroup);
      String str = ((PlaylistDialogFragment.PlayListInfo)getItem(paramInt)).name;
      localTextView.setText(str);
      return localTextView;
    }
  }

  protected static class PlayListInfo
  {
    final long id;
    final String name;

    PlayListInfo(long paramLong, String paramString)
    {
      this.id = paramLong;
      this.name = paramString;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.PlaylistDialogFragment
 * JD-Core Version:    0.6.2
 */