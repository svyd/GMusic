package com.google.android.music.ui;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.utils.ViewUtils;

public class QueueTrackListAdapter extends TrackListAdapter
{
  private static float mFadeAmount;
  private boolean mEnabled;

  public QueueTrackListAdapter(MusicFragment paramMusicFragment, boolean paramBoolean, MediaList paramMediaList)
  {
    super(paramMusicFragment, paramBoolean, paramMediaList);
    mFadeAmount = getContext().getResources().getInteger(2131492875) / 100.0F;
    this.mEnabled = true;
    setViewResource(2130968645);
  }

  public QueueTrackListAdapter(MusicFragment paramMusicFragment, boolean paramBoolean, MediaList paramMediaList, Cursor paramCursor)
  {
    super(paramMusicFragment, paramBoolean, paramMediaList, paramCursor);
    mFadeAmount = getContext().getResources().getInteger(2131492875) / 100.0F;
    this.mEnabled = true;
    setViewResource(2130968645);
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = super.getView(paramInt, paramView, paramViewGroup);
    if (!this.mEnabled)
    {
      float f = mFadeAmount;
      ViewUtils.setAlpha(localView, f);
    }
    return localView;
  }

  public void setEnabled(boolean paramBoolean)
  {
    boolean bool = this.mEnabled;
    if (paramBoolean != bool)
      return;
    this.mEnabled = paramBoolean;
    notifyDataSetChanged();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.QueueTrackListAdapter
 * JD-Core Version:    0.6.2
 */