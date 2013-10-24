package com.google.android.music.ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.Toast;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.ExternalSongList;
import com.google.android.music.medialist.MediaList;

public class AddToLibraryButton extends BaseActionButton
{
  public AddToLibraryButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet, 2131231268, 2130837689);
  }

  public static void addToLibrary(Context paramContext, MediaList paramMediaList)
  {
    if (paramMediaList != null)
    {
      if ((paramMediaList instanceof ExternalSongList))
      {
        long[] arrayOfLong = ((ExternalSongList)paramMediaList).addToStore(paramContext, true);
        if ((arrayOfLong == null) || (arrayOfLong.length == 0))
        {
          String str1 = paramContext.getString(2131230914);
          Toast.makeText(paramContext, str1, 0).show();
          return;
        }
        int i = arrayOfLong.length;
        Resources localResources = paramContext.getResources();
        Object[] arrayOfObject = new Object[1];
        Integer localInteger = Integer.valueOf(i);
        arrayOfObject[0] = localInteger;
        String str2 = localResources.getQuantityString(2131623943, i, arrayOfObject);
        Toast.makeText(paramContext, str2, 0).show();
        return;
      }
      Log.wtf("ActionButton", "The MediaList was not ExternalSongList");
      return;
    }
    Log.w("ActionButton", "MediaList is null");
  }

  protected void handleAction(Context paramContext, MediaList paramMediaList)
  {
    addToLibrary(paramContext, paramMediaList);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.AddToLibraryButton
 * JD-Core Version:    0.6.2
 */