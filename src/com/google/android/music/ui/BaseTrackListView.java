package com.google.android.music.ui;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.music.medialist.MediaList.MediaCursor;
import com.google.android.music.medialist.PlayQueueSongList;
import com.google.android.music.medialist.PlaylistSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent.Playlists.Members;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.DropListener;
import com.mobeta.android.dslv.DragSortListView.RemoveListener;

public class BaseTrackListView extends DragSortListView
{
  private TrackListAdapter mAdapter;
  private boolean mEditable;
  private BroadcastReceiver mNowPlayingReceiver;
  private DragSortListView.DropListener mOnDrop;
  private DragSortListView.RemoveListener mOnRemove;
  private boolean mScrollToNowPlaying;
  private SongList mSongList;

  public BaseTrackListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        if (BaseTrackListView.this.mAdapter == null)
          return;
        BaseTrackListView.this.mAdapter.updatePlaybackState();
      }
    };
    this.mNowPlayingReceiver = local1;
    DragSortListView.DropListener local2 = new DragSortListView.DropListener()
    {
      public void drop(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        if (BaseTrackListView.this.mAdapter == null)
          return;
        Cursor localCursor = BaseTrackListView.this.mAdapter.getCursor();
        if (localCursor == null)
          return;
        BaseTrackListView.this.mAdapter.moveItemTemp(paramAnonymousInt1, paramAnonymousInt2);
        BaseTrackListView.this.invalidateViews();
        if ((localCursor instanceof MediaList.MediaCursor))
        {
          ((MediaList.MediaCursor)localCursor).moveItem(paramAnonymousInt1, paramAnonymousInt2);
          return;
        }
        Context localContext = BaseTrackListView.this.getContext();
        PlaylistSongList localPlaylistSongList = (PlaylistSongList)BaseTrackListView.this.mSongList;
        MusicUtils.movePlaylistItem(localContext, localCursor, localPlaylistSongList, paramAnonymousInt1, paramAnonymousInt2);
      }
    };
    this.mOnDrop = local2;
    DragSortListView.RemoveListener local3 = new DragSortListView.RemoveListener()
    {
      public void remove(int paramAnonymousInt)
      {
        if (BaseTrackListView.this.mAdapter == null)
          return;
        Cursor localCursor = BaseTrackListView.this.mAdapter.getCursor();
        if (localCursor == null)
          return;
        int i = localCursor.getColumnIndexOrThrow("_id");
        int j = localCursor.getColumnIndexOrThrow("title");
        if (!localCursor.moveToPosition(paramAnonymousInt))
        {
          StringBuilder localStringBuilder = new StringBuilder().append("Failed to move item. Invalid \"remove\" position: ").append(paramAnonymousInt).append(". Cursor size:");
          int k = localCursor.getCount();
          String str1 = k;
          int m = Log.e("BaseTrackListView", str1);
          return;
        }
        final long l = localCursor.getLong(i);
        String str2 = localCursor.getString(j);
        BaseTrackListView.this.mAdapter.removeItemTemp(paramAnonymousInt);
        BaseTrackListView.this.invalidateViews();
        MusicUtils.runAsync(new Runnable()
        {
          public void run()
          {
            long l1 = ((PlaylistSongList)BaseTrackListView.this.mSongList).getPlaylistId();
            long l2 = l;
            Uri localUri = MusicContent.Playlists.Members.getPlaylistItemUri(l1, l2);
            if (BaseTrackListView.this.getContext().getContentResolver().delete(localUri, null, null) != 0)
            {
              if ((BaseTrackListView.this.mSongList instanceof PlayQueueSongList));
              for (int i = 2131230911; ; i = 2131230910)
              {
                Context localContext = BaseTrackListView.this.getContext();
                Object[] arrayOfObject = new Object[1];
                String str1 = this.val$title;
                arrayOfObject[0] = str1;
                String str2 = localContext.getString(i, arrayOfObject);
                Toast.makeText(BaseTrackListView.this.getContext(), str2, 0).show();
                return;
              }
            }
            int j = Log.w("BaseTrackListView", "Could not remove item from playlist");
          }
        });
      }
    };
    this.mOnRemove = local3;
    setDragEnabled(false);
    setVerticalFadingEdgeEnabled(false);
    setFastScrollEnabled(true);
  }

  private void disableDragDrop()
  {
    setDragEnabled(false);
    this.mAdapter.setEditMode(false);
  }

  private void doScroll()
  {
    if (!this.mScrollToNowPlaying)
      return;
    if (MusicUtils.sService == null)
    {
      int i = Log.w("BaseTrackListView", "Playback service not ready.");
      return;
    }
    if (this.mAdapter == null)
      return;
    int j;
    try
    {
      j = MusicUtils.sService.getQueuePosition();
      int k = this.mAdapter.getCount();
      if (j > k)
      {
        String str1 = "Need to scroll to: " + j + " which is larger than the current list size: " + k;
        int m = Log.i("BaseTrackListView", str1);
        return;
      }
    }
    catch (RemoteException localRemoteException)
    {
      String str2 = localRemoteException.getMessage();
      int n = Log.w("BaseTrackListView", str2, localRemoteException);
      return;
    }
    int i1 = 0;
    setSelectionFromTop(j, i1);
    this.mScrollToNowPlaying = false;
  }

  private void enableDragDrop()
  {
    setDragEnabled(true);
    DragSortListView.DropListener localDropListener = this.mOnDrop;
    setDropListener(localDropListener);
    DragSortListView.RemoveListener localRemoveListener = this.mOnRemove;
    setRemoveListener(localRemoveListener);
    setMaxScrollSpeed(5.0F);
    Context localContext = getContext();
    CustomDSController localCustomDSController = new CustomDSController(this, localContext);
    setFloatViewManager(localCustomDSController);
    setOnTouchListener(localCustomDSController);
    this.mAdapter.setEditMode(true);
  }

  private TrackListAdapter findTrackListAdapter(ListAdapter paramListAdapter)
  {
    TrackListAdapter localTrackListAdapter;
    if ((paramListAdapter instanceof TrackListAdapter))
      localTrackListAdapter = (TrackListAdapter)paramListAdapter;
    while (true)
    {
      return localTrackListAdapter;
      if ((paramListAdapter instanceof HeaderViewListAdapter))
      {
        ListAdapter localListAdapter = ((HeaderViewListAdapter)paramListAdapter).getWrappedAdapter();
        localTrackListAdapter = findTrackListAdapter(localListAdapter);
      }
      else
      {
        localTrackListAdapter = null;
      }
    }
  }

  private Object getTag(View paramView)
  {
    Object localObject1 = paramView.getTag();
    int i;
    if ((localObject1 == null) && ((paramView instanceof ViewGroup)))
    {
      ViewGroup localViewGroup = (ViewGroup)paramView;
      i = 0;
      int j = localViewGroup.getChildCount();
      if (i < j)
      {
        View localView = localViewGroup.getChildAt(i);
        localObject1 = getTag(localView);
        if (localObject1 == null);
      }
    }
    for (Object localObject2 = localObject1; ; localObject2 = localObject1)
    {
      return localObject2;
      i += 1;
      break;
    }
  }

  void handleItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    int i = paramListView.getHeaderViewsCount();
    if (paramInt < i)
      return;
    if (this.mSongList == null)
    {
      int j = Log.w("BaseTrackListView", "The SongList is null.");
      return;
    }
    Object localObject = getTag(paramView);
    if ((localObject instanceof TrackListAdapter.ViewHolder))
    {
      if (((TrackListAdapter.ViewHolder)localObject).isAvailable)
      {
        SongList localSongList1 = this.mSongList;
        int k = paramInt - i;
        MusicUtils.playMediaList(localSongList1, k);
        return;
      }
      if (UIStateManager.getInstance().getPrefs().isStreamOnlyOnWifi())
      {
        Toast.makeText(getContext(), 2131231060, 1).show();
        return;
      }
      Toast.makeText(getContext(), 2131231061, 1).show();
      return;
    }
    int m = Log.w("BaseTrackListView", "The tag on the view wasn't a ViewHolder. The underlying adapter probably is not TrackListAdapter.");
    SongList localSongList2 = this.mSongList;
    int n = paramInt - i;
    MusicUtils.playMediaList(localSongList2, n);
  }

  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.android.music.metachanged");
    localIntentFilter.addAction("com.android.music.queuechanged");
    localIntentFilter.addAction("com.google.android.music.mix.playbackmodechanged");
    Context localContext = getContext();
    BroadcastReceiver localBroadcastReceiver = this.mNowPlayingReceiver;
    Intent localIntent = localContext.registerReceiver(localBroadcastReceiver, localIntentFilter);
    if (this.mAdapter == null)
      return;
    this.mAdapter.updatePlaybackState();
  }

  public void onDetachedFromWindow()
  {
    Context localContext = getContext();
    BroadcastReceiver localBroadcastReceiver = this.mNowPlayingReceiver;
    localContext.unregisterReceiver(localBroadcastReceiver);
    super.onDetachedFromWindow();
  }

  public void scrollToNowPlaying()
  {
    this.mScrollToNowPlaying = true;
    doScroll();
  }

  public void setAdapter(ListAdapter paramListAdapter)
  {
    super.setAdapter(paramListAdapter);
    if (paramListAdapter == null)
    {
      this.mAdapter = null;
      return;
    }
    TrackListAdapter localTrackListAdapter1 = findTrackListAdapter(paramListAdapter);
    this.mAdapter = localTrackListAdapter1;
    if (this.mAdapter == null)
      throw new IllegalArgumentException("The adapter must be a TrackListAdapter");
    TrackListAdapter localTrackListAdapter2 = this.mAdapter;
    OnContentChangedCallback local4 = new OnContentChangedCallback()
    {
      public void onContentChanged()
      {
        BaseTrackListView.this.doScroll();
      }
    };
    localTrackListAdapter2.setOnContentChangedCallback(local4);
  }

  public void setSongList(SongList paramSongList)
  {
    this.mSongList = paramSongList;
    boolean bool = this.mSongList.isEditable();
    this.mEditable = bool;
    if (this.mEditable)
      enableDragDrop();
    while (true)
    {
      TrackListAdapter localTrackListAdapter = this.mAdapter;
      SongList localSongList = this.mSongList;
      localTrackListAdapter.setSongList(localSongList);
      MusicUtils.runAsyncWithCallback(new AsyncRunner()
      {
        private boolean mShowTrackArtists;

        public void backgroundTask()
        {
          SongList localSongList = BaseTrackListView.this.mSongList;
          Context localContext = BaseTrackListView.this.getContext();
          boolean bool = localSongList.hasDifferentTrackArtists(localContext);
          this.mShowTrackArtists = bool;
        }

        public void taskCompleted()
        {
          BaseTrackListView localBaseTrackListView = BaseTrackListView.this;
          boolean bool = this.mShowTrackArtists;
          localBaseTrackListView.showTrackArtist(bool);
        }
      });
      return;
      disableDragDrop();
    }
  }

  public void showTrackArtist(boolean paramBoolean)
  {
    if (this.mAdapter == null)
      throw new IllegalStateException("The adapter wasn't set");
    this.mAdapter.showTrackArtist(paramBoolean);
  }

  private static class CustomDSController extends DragSortController
  {
    private int mGrabHandleWidth;

    public CustomDSController(DragSortListView paramDragSortListView, Context paramContext)
    {
      super();
      Resources localResources = paramContext.getResources();
      int i = localResources.getDimensionPixelSize(2131558411);
      this.mGrabHandleWidth = i;
      int j = localResources.getColor(2131427359);
      setBackgroundColor(j);
      setDragInitMode(1);
      setRemoveEnabled(true);
      setRemoveMode(1);
    }

    public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
    {
      if ((paramMotionEvent1 == null) || (paramMotionEvent2 == null))
        int i = Log.w("BaseTrackListView", "onScroll called with null MotionEvents!");
      for (boolean bool = false; ; bool = super.onScroll(paramMotionEvent1, paramMotionEvent2, paramFloat1, paramFloat2))
        return bool;
    }

    public int startDragPosition(MotionEvent paramMotionEvent)
    {
      int i = super.dragHandleHitPosition(paramMotionEvent);
      int j = (int)paramMotionEvent.getX();
      int k = this.mGrabHandleWidth;
      if (j < k);
      while (true)
      {
        return i;
        i = -1;
      }
    }
  }

  static abstract interface OnContentChangedCallback
  {
    public abstract void onContentChanged();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.BaseTrackListView
 * JD-Core Version:    0.6.2
 */