package com.google.android.music.ui;

import android.content.Context;
import android.util.AttributeSet;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.utils.MusicUtils;

public class ShuffleButton extends BaseActionButton
{
  public ShuffleButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet, 2131231274, 2130837731);
  }

  protected void handleAction(Context paramContext, MediaList paramMediaList)
  {
    if ((paramMediaList instanceof SongList))
    {
      MusicUtils.shuffleSongs((SongList)paramMediaList);
      return;
    }
    String str = "Invalid MediaList: " + paramMediaList;
    Log.w("ActionButton", str);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.ShuffleButton
 * JD-Core Version:    0.6.2
 */