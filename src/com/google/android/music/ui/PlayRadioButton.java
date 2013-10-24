package com.google.android.music.ui;

import android.content.Context;
import android.util.AttributeSet;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.utils.MusicUtils;

public class PlayRadioButton extends BaseActionButton
{
  public PlayRadioButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, bool);
  }

  private PlayRadioButton(Context paramContext, AttributeSet paramAttributeSet, boolean paramBoolean)
  {
  }

  protected void handleAction(Context paramContext, MediaList paramMediaList)
  {
    SongList localSongList = (SongList)paramMediaList;
    MusicUtils.playRadio(paramContext, localSongList);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.PlayRadioButton
 * JD-Core Version:    0.6.2
 */