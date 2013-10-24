package com.google.android.music.ui;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.medialist.MediaList.MediaCursor;

public abstract class MediaListFragment extends BaseListFragment
  implements LoaderManager.LoaderCallbacks<Cursor>
{
  private boolean mActivityCreated = false;
  private MediaListCursorAdapter mAdapter;
  private boolean mAsync;
  private DataSetObserver mDataSetObserver;
  private boolean mIsDataSetObserverRegistered = false;
  private MediaList mMediaList;
  private String[] mProjection;

  protected MediaListFragment()
  {
    DataSetObserver local1 = new DataSetObserver()
    {
      public void onChanged()
      {
        MediaListFragment.this.initEmptyScreen();
        MediaListFragment localMediaListFragment = MediaListFragment.this;
        if (MediaListFragment.this.mAdapter.getCount() == 0);
        for (boolean bool = true; ; bool = false)
        {
          localMediaListFragment.setEmptyScreenVisible(bool);
          return;
        }
      }
    };
    this.mDataSetObserver = local1;
  }

  private boolean initFromArgs()
  {
    boolean bool1 = false;
    Bundle localBundle = getArguments();
    if (localBundle == null);
    while (true)
    {
      return bool1;
      String str = localBundle.getString("list");
      if (!TextUtils.isEmpty(str))
      {
        MediaList localMediaList = MediaList.thaw(str);
        if (localMediaList != null)
        {
          String[] arrayOfString = localBundle.getStringArray("proj");
          if ((arrayOfString != null) && (arrayOfString.length != 0))
          {
            this.mMediaList = localMediaList;
            this.mProjection = arrayOfString;
            boolean bool2 = localBundle.getBoolean("async");
            this.mAsync = bool2;
            bool1 = true;
          }
        }
      }
    }
  }

  private void initializeListView()
  {
    FragmentActivity localFragmentActivity = getActivity();
    if (this.mAsync)
    {
      MediaList localMediaList = this.mMediaList;
      String[] arrayOfString = this.mProjection;
      MediaList.MediaCursor localMediaCursor = localMediaList.getCursor(localFragmentActivity, arrayOfString, null);
      MediaListCursorAdapter localMediaListCursorAdapter1 = newAsyncCursorAdapter(localMediaCursor);
      this.mAdapter = localMediaListCursorAdapter1;
      MediaListCursorAdapter localMediaListCursorAdapter2 = this.mAdapter;
      setListAdapter(localMediaListCursorAdapter2);
    }
    while (true)
    {
      if (this.mAdapter == null)
        return;
      if (this.mIsDataSetObserverRegistered)
        return;
      MediaListCursorAdapter localMediaListCursorAdapter3 = this.mAdapter;
      DataSetObserver localDataSetObserver = this.mDataSetObserver;
      localMediaListCursorAdapter3.registerDataSetObserver(localDataSetObserver);
      this.mIsDataSetObserverRegistered = true;
      return;
      MediaListCursorAdapter localMediaListCursorAdapter4 = newLoaderCursorAdapter();
      this.mAdapter = localMediaListCursorAdapter4;
      MediaListCursorAdapter localMediaListCursorAdapter5 = this.mAdapter;
      setListAdapter(localMediaListCursorAdapter5);
      Loader localLoader = getLoaderManager().initLoader(0, null, this);
    }
  }

  public MediaList getFragmentMediaList()
  {
    return this.mMediaList;
  }

  protected MediaList getMediaList()
  {
    return this.mMediaList;
  }

  protected void init(MediaList paramMediaList, String[] paramArrayOfString, boolean paramBoolean)
  {
    if (this.mMediaList != null)
      throw new IllegalStateException("Already initialized");
    this.mMediaList = paramMediaList;
    this.mProjection = paramArrayOfString;
    this.mAsync = paramBoolean;
    if (!this.mActivityCreated)
      return;
    initializeListView();
  }

  protected boolean isInitialized()
  {
    if (this.mMediaList != null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  abstract MediaListCursorAdapter newAsyncCursorAdapter(Cursor paramCursor);

  abstract MediaListCursorAdapter newLoaderCursorAdapter();

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (isInitialized())
      initializeListView();
    this.mActivityCreated = true;
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (isInitialized())
      return;
    boolean bool = initFromArgs();
  }

  public final Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    if (this.mAsync)
      throw new IllegalStateException("Attempt to use loader with async cursor");
    MediaList localMediaList1 = this.mMediaList;
    FragmentActivity localFragmentActivity1 = getActivity();
    FragmentActivity localFragmentActivity2;
    MediaList localMediaList2;
    String[] arrayOfString1;
    if (localMediaList1.getFullContentUri(localFragmentActivity1) == null)
    {
      localFragmentActivity2 = getActivity();
      localMediaList2 = this.mMediaList;
      arrayOfString1 = this.mProjection;
    }
    FragmentActivity localFragmentActivity3;
    MediaList localMediaList3;
    String[] arrayOfString2;
    for (Object localObject = new NonUriMediaListCursorLoader(localFragmentActivity2, localMediaList2, arrayOfString1); ; localObject = new MediaListCursorLoader(localFragmentActivity3, localMediaList3, arrayOfString2))
    {
      return localObject;
      localFragmentActivity3 = getActivity();
      localMediaList3 = this.mMediaList;
      arrayOfString2 = this.mProjection;
    }
  }

  public void onDestroy()
  {
    if (this.mIsDataSetObserverRegistered)
    {
      this.mIsDataSetObserverRegistered = false;
      MediaListCursorAdapter localMediaListCursorAdapter = this.mAdapter;
      DataSetObserver localDataSetObserver = this.mDataSetObserver;
      localMediaListCursorAdapter.unregisterDataSetObserver(localDataSetObserver);
    }
    if ((this.mAsync) && (this.mAdapter != null))
      this.mAdapter.changeCursor(null);
    super.onDestroy();
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    if (this.mAsync)
      throw new IllegalStateException("Attempt to use loader with async cursor");
    Cursor localCursor = this.mAdapter.swapCursor(paramCursor);
    if ((paramCursor == null) || (paramCursor.getCount() == 0));
    for (boolean bool = true; ; bool = false)
    {
      setEmptyScreenVisible(bool);
      return;
    }
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
    if (this.mAsync)
      throw new IllegalStateException("Attempt to use loader with async cursor");
    Cursor localCursor = this.mAdapter.swapCursor(null);
  }

  protected void saveMediaListAsArguments()
  {
    if (this.mMediaList == null)
      return;
    Bundle localBundle = getArguments();
    if (localBundle == null)
      localBundle = new Bundle();
    String str = this.mMediaList.freeze();
    localBundle.putString("list", str);
    String[] arrayOfString = this.mProjection;
    localBundle.putStringArray("proj", arrayOfString);
    boolean bool = this.mAsync;
    localBundle.putBoolean("async", bool);
    setArguments(localBundle);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.MediaListFragment
 * JD-Core Version:    0.6.2
 */