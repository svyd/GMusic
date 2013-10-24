package com.google.android.music.ui;

import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.utils.ViewUtils;

public abstract class BaseClusterListFragment extends BaseListFragment
  implements LoaderManager.LoaderCallbacks<Cursor>, ClusterListAdapter.ClusterBuilder
{
  private ClusterListAdapter mAdapter;
  private long mLoadedStateMask = 0L;

  public final Cluster buildCluster(int paramInt, Cursor paramCursor)
  {
    return createCluster(paramInt, paramCursor);
  }

  protected abstract Cluster createCluster(int paramInt, Cursor paramCursor);

  protected abstract PlayCardView.ContextMenuDelegate getCardsContextMenuDelegate();

  protected abstract Uri getClusterContentUri(int paramInt);

  protected abstract String[] getClusterProjection(int paramInt);

  protected abstract int getNumberOfClusters();

  protected int getScreenColumns()
  {
    Resources localResources = getResources();
    MusicPreferences localMusicPreferences = getPreferences();
    return ViewUtils.getScreenColumnCount(localResources, localMusicPreferences);
  }

  public Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    Uri localUri = getClusterContentUri(paramInt);
    String[] arrayOfString1 = getClusterProjection(paramInt);
    FragmentActivity localFragmentActivity = getActivity();
    String[] arrayOfString2 = null;
    String str = null;
    return new CursorLoader(localFragmentActivity, localUri, arrayOfString1, null, arrayOfString2, str);
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    boolean bool = true;
    this.mAdapter.swapCursor(paramLoader, paramCursor);
    setListShown(bool);
    if ((paramCursor == null) || (paramCursor.getCount() == 0))
    {
      long l1 = this.mLoadedStateMask;
      int i = paramLoader.getId();
      long l2 = 1 << i ^ 0xFFFFFFFF;
      long l3 = l1 & l2;
      this.mLoadedStateMask = l3;
      if (this.mLoadedStateMask != 0L)
        break label116;
    }
    while (true)
    {
      setEmptyScreenVisible(bool);
      return;
      long l4 = this.mLoadedStateMask;
      int j = paramLoader.getId();
      long l5 = 1 << j;
      long l6 = l4 | l5;
      this.mLoadedStateMask = l6;
      break;
      label116: bool = false;
    }
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
    this.mAdapter.nullCursor(paramLoader);
    this.mLoadedStateMask = 0L;
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    ListView localListView = getListView();
    int i = getActivity().getResources().getDimensionPixelSize(2131558445);
    localListView.setPadding(i, i, i, i);
    localListView.setClipToPadding(false);
  }

  protected void startLoading()
  {
    startLoading(false);
  }

  protected void startLoading(boolean paramBoolean)
  {
    int i = getNumberOfClusters();
    FragmentActivity localFragmentActivity = getActivity();
    PlayCardView.ContextMenuDelegate localContextMenuDelegate = getCardsContextMenuDelegate();
    ClusterListAdapter localClusterListAdapter1 = new ClusterListAdapter(localFragmentActivity, this, localContextMenuDelegate, i);
    this.mAdapter = localClusterListAdapter1;
    ClusterListAdapter localClusterListAdapter2 = this.mAdapter;
    setListAdapter(localClusterListAdapter2);
    setListShown(false);
    getListView().setDivider(null);
    long l1 = 0L;
    if (i > 0)
    {
      LoaderManager localLoaderManager = getLoaderManager();
      int j = 0;
      if (j < i)
      {
        if ((localLoaderManager.getLoader(j) != null) && (!paramBoolean))
          Loader localLoader1 = localLoaderManager.restartLoader(j, null, this);
        while (true)
        {
          long l2 = 1 << j;
          l1 |= l2;
          j += 1;
          break;
          Loader localLoader2 = localLoaderManager.initLoader(j, null, this);
        }
      }
    }
    else
    {
      setListShown(true);
      setEmptyScreenVisible(true);
    }
    this.mLoadedStateMask = l1;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.BaseClusterListFragment
 * JD-Core Version:    0.6.2
 */