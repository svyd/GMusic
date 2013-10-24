package com.google.android.music.xdi;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;

class DetailPlaylistSectionsCursor extends MatrixCursor
{
  private static final String[] PROJECTION_PLAYLISTS = arrayOfString;
  private final Context mContext;
  private final ProjectionMap mProjectionMap;

  static
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "playlist_name";
  }

  DetailPlaylistSectionsCursor(Context paramContext, String[] paramArrayOfString, long paramLong)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    addRowForAlbum(paramLong);
  }

  private void addRowForAlbum(long paramLong)
  {
    Context localContext = this.mContext;
    Uri.Builder localBuilder = MusicContent.Playlists.CONTENT_URI.buildUpon();
    String str1 = Long.toString(paramLong);
    Uri localUri = localBuilder.appendPath(str1).build();
    String[] arrayOfString1 = PROJECTION_PLAYLISTS;
    String[] arrayOfString2 = null;
    String str2 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str2);
    if (localCursor == null)
      return;
    try
    {
      if (localCursor.moveToNext())
      {
        String str3 = localCursor.getString(1);
        Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
        ProjectionMap localProjectionMap1 = this.mProjectionMap;
        Long localLong = Long.valueOf(paramLong);
        localProjectionMap1.writeValueToArray(arrayOfObject, "_id", localLong);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "name", "");
        this.mProjectionMap.writeValueToArray(arrayOfObject, "display_header", str3);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "display_name", null);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "display_subname", null);
        ProjectionMap localProjectionMap2 = this.mProjectionMap;
        Integer localInteger = Integer.valueOf(1);
        localProjectionMap2.writeValueToArray(arrayOfObject, "section_type", localInteger);
        ProjectionMap localProjectionMap3 = this.mProjectionMap;
        String str4 = "content://com.google.android.music.xdi/details/playlists/" + paramLong + "/tracks";
        localProjectionMap3.writeValueToArray(arrayOfObject, "content_uri", str4);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "action_uri", null);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "user_rating_custom", null);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "user_rating", null);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "user_rating_count", null);
        addRow(arrayOfObject);
      }
      return;
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.DetailPlaylistSectionsCursor
 * JD-Core Version:    0.6.2
 */