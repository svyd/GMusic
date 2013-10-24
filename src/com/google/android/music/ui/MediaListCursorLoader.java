package com.google.android.music.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import com.google.android.music.medialist.MediaList;

public class MediaListCursorLoader extends CursorLoader
{
  private final MediaList mMediaList;

  public MediaListCursorLoader(Context paramContext, MediaList paramMediaList, String[] paramArrayOfString)
  {
    super(paramContext);
    this.mMediaList = paramMediaList;
    setProjection(paramArrayOfString);
  }

  public Cursor loadInBackground()
  {
    MediaList localMediaList = this.mMediaList;
    Context localContext = getContext();
    Uri localUri = localMediaList.getFullContentUri(localContext);
    if (localUri == null)
      throw new IllegalStateException("MediaList must be content uri based");
    setUri(localUri);
    return super.loadInBackground();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.MediaListCursorLoader
 * JD-Core Version:    0.6.2
 */