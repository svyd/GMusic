package com.google.android.music.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.medialist.SharedWithMeSongList;

public class FollowPlaylistButton extends BaseActionButton
{
  public FollowPlaylistButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet, 2131231267, 2130837772);
  }

  public static void followPlaylist(Context paramContext, MediaList paramMediaList)
  {
    if (paramMediaList != null)
    {
      if ((paramMediaList instanceof SharedWithMeSongList))
      {
        ((SharedWithMeSongList)paramMediaList).followPlaylist(paramContext);
        String str = paramContext.getString(2131230909);
        Toast.makeText(paramContext, str, 0).show();
        return;
      }
      Log.wtf("ActionButton", "The MediaList was not SharedWithMeSongList");
      return;
    }
    Log.w("ActionButton", "MediaList is null");
  }

  protected void handleAction(Context paramContext, MediaList paramMediaList)
  {
    followPlaylist(paramContext, paramMediaList);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.FollowPlaylistButton
 * JD-Core Version:    0.6.2
 */