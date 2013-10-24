package com.google.android.music.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MusicOptionsMenuItem extends MusicMenuItem
{
  private MusicOptionsMenuItemView mItemView;

  public View getItemView(ViewGroup paramViewGroup)
  {
    if (this.mItemView == null)
    {
      MusicOptionsMenuItemView localMusicOptionsMenuItemView = (MusicOptionsMenuItemView)LayoutInflater.from(this.mMusicMenu.getContext()).inflate(2130968635, paramViewGroup, false);
      this.mItemView = localMusicOptionsMenuItemView;
      this.mItemView.initialize(this);
    }
    return this.mItemView;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.menu.MusicOptionsMenuItem
 * JD-Core Version:    0.6.2
 */