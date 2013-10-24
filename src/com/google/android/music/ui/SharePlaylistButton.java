package com.google.android.music.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Toast;
import com.google.android.music.medialist.MediaList;

public class SharePlaylistButton extends BaseActionButton
{
  public SharePlaylistButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet, 2131231275, 2130837753);
  }

  protected void handleAction(Context paramContext, MediaList paramMediaList)
  {
    Toast.makeText(getContext(), "Not implemented yet.", 1).show();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.SharePlaylistButton
 * JD-Core Version:    0.6.2
 */