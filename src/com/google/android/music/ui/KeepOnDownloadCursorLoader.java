package com.google.android.music.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import com.google.android.music.store.MusicContent;

public class KeepOnDownloadCursorLoader extends CursorLoader
{
  public KeepOnDownloadCursorLoader(Context paramContext, String[] paramArrayOfString)
  {
    super(paramContext);
    setProjection(paramArrayOfString);
  }

  public Cursor loadInBackground()
  {
    Uri localUri = MusicContent.DOWNLOAD_QUEUE_URI;
    setUri(localUri);
    return super.loadInBackground();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.KeepOnDownloadCursorLoader
 * JD-Core Version:    0.6.2
 */