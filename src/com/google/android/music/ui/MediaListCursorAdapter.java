package com.google.android.music.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;

public abstract class MediaListCursorAdapter extends SimpleCursorAdapter
{
  private final MusicFragment mMusicFragment;
  private int mRequiredIdColumnIndex;

  protected MediaListCursorAdapter(MusicFragment paramMusicFragment, int paramInt)
  {
  }

  protected MediaListCursorAdapter(MusicFragment paramMusicFragment, int paramInt, Cursor paramCursor)
  {
  }

  private void setRequiredIdColumnIndex(Cursor paramCursor)
  {
    if (paramCursor == null)
      return;
    int i = paramCursor.getColumnIndexOrThrow("_id");
    this.mRequiredIdColumnIndex = i;
  }

  public void bindView(View paramView, Context paramContext, Cursor paramCursor)
  {
    if (hasRowData(paramCursor))
    {
      int i = this.mRequiredIdColumnIndex;
      long l = paramCursor.getLong(i);
      MediaListCursorAdapter localMediaListCursorAdapter = this;
      View localView = paramView;
      Context localContext = paramContext;
      Cursor localCursor = paramCursor;
      localMediaListCursorAdapter.bindViewToMediaListItem(localView, localContext, localCursor, l);
      return;
    }
    bindViewToLoadingItem(paramView, paramContext);
  }

  protected abstract void bindViewToLoadingItem(View paramView, Context paramContext);

  protected abstract void bindViewToMediaListItem(View paramView, Context paramContext, Cursor paramCursor, long paramLong);

  public void changeCursor(Cursor paramCursor)
  {
    FragmentActivity localFragmentActivity = this.mMusicFragment.getFragment().getActivity();
    if ((paramCursor != null) && (localFragmentActivity != null) && (localFragmentActivity.isFinishing()))
    {
      paramCursor.close();
      paramCursor = null;
    }
    super.changeCursor(paramCursor);
  }

  protected Context getContext()
  {
    return this.mMusicFragment.getFragment().getActivity();
  }

  protected MusicFragment getFragment()
  {
    return this.mMusicFragment;
  }

  protected boolean hasRowData(Cursor paramCursor)
  {
    boolean bool = false;
    if (paramCursor == null);
    while (true)
    {
      return bool;
      int i = this.mRequiredIdColumnIndex;
      if (!paramCursor.isNull(i))
        bool = true;
    }
  }

  public Cursor swapCursor(Cursor paramCursor)
  {
    setRequiredIdColumnIndex(paramCursor);
    return super.swapCursor(paramCursor);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.MediaListCursorAdapter
 * JD-Core Version:    0.6.2
 */