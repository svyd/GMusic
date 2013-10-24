package com.google.android.music.store;

import android.content.Context;
import android.database.CrossProcessCursor;
import android.database.MatrixCursor;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.RequeriableCursorWrapper;

public class AutoPlaylistCursor extends RequeriableCursorWrapper
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.STORE);
  private final long[] mAutoPlaylistIds;
  private final Context mContext;

  public AutoPlaylistCursor(Context paramContext, String[] paramArrayOfString, long[] paramArrayOfLong)
  {
    super(localMatrixCursor);
    this.mContext = paramContext;
    this.mAutoPlaylistIds = paramArrayOfLong;
  }

  private static MatrixCursor buildCursor(Context paramContext, String[] paramArrayOfString, long[] paramArrayOfLong)
  {
    return new AutoPlaylistCursorFactory(paramContext, paramArrayOfLong).buildCursor(paramArrayOfString);
  }

  protected CrossProcessCursor getUpdatedCursor()
  {
    Context localContext = this.mContext;
    String[] arrayOfString = getColumnNames();
    long[] arrayOfLong = this.mAutoPlaylistIds;
    return buildCursor(localContext, arrayOfString, arrayOfLong);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.AutoPlaylistCursor
 * JD-Core Version:    0.6.2
 */