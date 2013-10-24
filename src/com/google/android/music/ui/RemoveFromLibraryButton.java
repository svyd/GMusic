package com.google.android.music.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import com.google.android.music.medialist.AlbumSongList;
import com.google.android.music.medialist.MediaList;

public class RemoveFromLibraryButton extends BaseActionButton
{
  public RemoveFromLibraryButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet, 2131231269, 2130837689);
  }

  public static void removeFromMyLibrary(Context paramContext, MediaList paramMediaList)
  {
    if (paramMediaList == null)
    {
      int i = Log.wtf("ActionButton", "MediaList is null");
      return;
    }
    if (!(paramMediaList instanceof AlbumSongList))
    {
      int j = Log.wtf("ActionButton", "Can't remove from a non-album song list");
      return;
    }
    ((AlbumSongList)paramMediaList).removeFromMyLibrary(paramContext);
  }

  protected void handleAction(Context paramContext, MediaList paramMediaList)
  {
    removeFromMyLibrary(paramContext, paramMediaList);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.RemoveFromLibraryButton
 * JD-Core Version:    0.6.2
 */