package com.google.android.music.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import com.google.android.music.medialist.MediaList;

public class NonUriMediaListCursorLoader extends CursorLoader
{
  private final Context mContext;
  private final MediaList mMediaList;

  public NonUriMediaListCursorLoader(Context paramContext, MediaList paramMediaList, String[] paramArrayOfString)
  {
    super(paramContext);
    this.mMediaList = paramMediaList;
    this.mContext = paramContext;
    setProjection(paramArrayOfString);
  }

  public Cursor loadInBackground()
  {
    MediaList localMediaList1 = this.mMediaList;
    Context localContext1 = getContext();
    if (localMediaList1.getFullContentUri(localContext1) != null)
      throw new IllegalStateException("MediaList not be content uri based");
    MediaList localMediaList2 = this.mMediaList;
    Context localContext2 = this.mContext;
    String[] arrayOfString = getProjection();
    return localMediaList2.getSyncMediaCursor(localContext2, arrayOfString, null);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.NonUriMediaListCursorLoader
 * JD-Core Version:    0.6.2
 */