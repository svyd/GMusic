package com.google.android.music.ui;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.medialist.MediaList.MediaCursor;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.DocumentMenuHandler.DocumentContextMenuDelegate;
import com.google.android.music.ui.cardlib.utils.Utils;

public abstract class MediaListGridFragment extends GridFragment
  implements LoaderManager.LoaderCallbacks<Cursor>
{
  private boolean mActivityCreated;
  private MediaListCursorAdapter mAdapter;
  private boolean mAsync;
  protected final PlayCardView.ContextMenuDelegate mCardsContextMenuDelegate;
  private boolean mClearedFromOnStop;
  private DataSetObserver mDataSetObserver;
  private boolean mIsDataSetObserverRegistered = false;
  private MediaList mMediaList;
  private String[] mProjection;
  private boolean mShowBigCard;

  protected MediaListGridFragment()
  {
    DataSetObserver local1 = new DataSetObserver()
    {
      public void onChanged()
      {
        MediaListGridFragment.this.initEmptyScreen();
        MediaListGridFragment localMediaListGridFragment = MediaListGridFragment.this;
        if (MediaListGridFragment.this.getListAdapter().getCount() == 0);
        for (boolean bool = true; ; bool = false)
        {
          localMediaListGridFragment.setEmptyScreenVisible(bool);
          return;
        }
      }
    };
    this.mDataSetObserver = local1;
    this.mActivityCreated = false;
    this.mClearedFromOnStop = false;
    DocumentMenuHandler.DocumentContextMenuDelegate localDocumentContextMenuDelegate = new DocumentMenuHandler.DocumentContextMenuDelegate(this);
    this.mCardsContextMenuDelegate = localDocumentContextMenuDelegate;
  }

  protected MediaListGridFragment(MediaList paramMediaList, String[] paramArrayOfString, boolean paramBoolean)
  {
    DataSetObserver local1 = new DataSetObserver()
    {
      public void onChanged()
      {
        MediaListGridFragment.this.initEmptyScreen();
        MediaListGridFragment localMediaListGridFragment = MediaListGridFragment.this;
        if (MediaListGridFragment.this.getListAdapter().getCount() == 0);
        for (boolean bool = true; ; bool = false)
        {
          localMediaListGridFragment.setEmptyScreenVisible(bool);
          return;
        }
      }
    };
    this.mDataSetObserver = local1;
    this.mActivityCreated = false;
    this.mClearedFromOnStop = false;
    DocumentMenuHandler.DocumentContextMenuDelegate localDocumentContextMenuDelegate = new DocumentMenuHandler.DocumentContextMenuDelegate(this);
    this.mCardsContextMenuDelegate = localDocumentContextMenuDelegate;
    init(paramMediaList, paramArrayOfString, paramBoolean);
  }

  private ListAdapter getGridAdapter(MediaListCursorAdapter paramMediaListCursorAdapter)
  {
    int i = getScreenColumns();
    int j;
    FragmentActivity localFragmentActivity1;
    if (this.mShowBigCard)
    {
      j = getActivity().getResources().getInteger(2131492872);
      localFragmentActivity1 = getActivity();
    }
    FragmentActivity localFragmentActivity2;
    for (Object localObject = new GridAdapterWrapperWithHeader(localFragmentActivity1, paramMediaListCursorAdapter, i, j); ; localObject = new GridAdapterWrapper(localFragmentActivity2, paramMediaListCursorAdapter, i))
    {
      return localObject;
      localFragmentActivity2 = getActivity();
    }
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
            boolean bool3 = localBundle.getBoolean("bigcard");
            this.mShowBigCard = bool3;
            bool1 = true;
          }
        }
      }
    }
  }

  private void initializeGridView()
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
      ListAdapter localListAdapter1 = getGridAdapter(localMediaListCursorAdapter2);
      setListAdapter(localListAdapter1);
    }
    while (true)
    {
      ListAdapter localListAdapter2 = getListAdapter();
      if (localListAdapter2 == null)
        return;
      if (this.mIsDataSetObserverRegistered)
        return;
      this.mIsDataSetObserverRegistered = true;
      DataSetObserver localDataSetObserver = this.mDataSetObserver;
      localListAdapter2.registerDataSetObserver(localDataSetObserver);
      return;
      MediaListCursorAdapter localMediaListCursorAdapter3 = newLoaderCursorAdapter();
      this.mAdapter = localMediaListCursorAdapter3;
      MediaListCursorAdapter localMediaListCursorAdapter4 = this.mAdapter;
      ListAdapter localListAdapter3 = getGridAdapter(localMediaListCursorAdapter4);
      setListAdapter(localListAdapter3);
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
    init(paramMediaList, paramArrayOfString, paramBoolean, false);
  }

  protected void init(MediaList paramMediaList, String[] paramArrayOfString, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.mMediaList != null)
      throw new IllegalStateException("Already initialized");
    this.mMediaList = paramMediaList;
    this.mProjection = paramArrayOfString;
    this.mAsync = paramBoolean1;
    this.mShowBigCard = paramBoolean2;
    if (!this.mActivityCreated)
      return;
    initializeGridView();
  }

  protected boolean isClearedFromOnStop()
  {
    return this.mClearedFromOnStop;
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
      initializeGridView();
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
    FragmentActivity localFragmentActivity = getActivity();
    MediaList localMediaList = this.mMediaList;
    String[] arrayOfString = this.mProjection;
    return new MediaListCursorLoader(localFragmentActivity, localMediaList, arrayOfString);
  }

  public void onDestroyView()
  {
    if (this.mIsDataSetObserverRegistered)
    {
      this.mIsDataSetObserverRegistered = false;
      ListAdapter localListAdapter = getListAdapter();
      DataSetObserver localDataSetObserver = this.mDataSetObserver;
      localListAdapter.unregisterDataSetObserver(localDataSetObserver);
    }
    if ((this.mAsync) && (this.mAdapter != null))
      this.mAdapter.changeCursor(null);
    getListView().setAdapter(null);
    super.onDestroyView();
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    if (this.mAsync)
      throw new IllegalStateException("Attempt to use loader with async cursor");
    Cursor localCursor = this.mAdapter.swapCursor(paramCursor);
    if (this.mAdapter.getCount() == 0);
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

  public void onStart()
  {
    super.onStart();
    if (!this.mClearedFromOnStop)
      return;
    this.mClearedFromOnStop = false;
    getListView().invalidateViews();
  }

  public void onStop()
  {
    super.onStop();
    this.mClearedFromOnStop = true;
    Utils.clearPlayCardThumbnails(getListView());
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    ListView localListView = getListView();
    int i = getActivity().getResources().getDimensionPixelSize(2131558445);
    localListView.setPadding(i, i, i, i);
    localListView.setClipToPadding(false);
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
    boolean bool1 = this.mAsync;
    localBundle.putBoolean("async", bool1);
    boolean bool2 = this.mShowBigCard;
    localBundle.putBoolean("bigcard", bool2);
    setArguments(localBundle);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.MediaListGridFragment
 * JD-Core Version:    0.6.2
 */