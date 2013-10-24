package com.google.android.music.xdi;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import com.google.android.music.store.MusicContent.Albums;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;

class DetailAlbumSectionsCursor extends MatrixCursor
{
  private static final String[] PROJECTION_ALBUMS = arrayOfString;
  private final Context mContext;
  private final ProjectionMap mProjectionMap;

  static
  {
    String[] arrayOfString = new String[4];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "album_name";
    arrayOfString[2] = "album_artist";
    arrayOfString[3] = "album_artist_id";
  }

  DetailAlbumSectionsCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    addRowForAlbum(paramString);
  }

  private void addRowForAlbum(String paramString)
  {
    if (MusicUtils.isNautilusId(paramString));
    Cursor localCursor;
    for (Uri localUri1 = MusicContent.Albums.getNautilusAlbumsUri(paramString); ; localUri1 = MusicContent.Albums.getAlbumsUri(Long.valueOf(paramString).longValue()))
    {
      Context localContext = this.mContext;
      String[] arrayOfString1 = PROJECTION_ALBUMS;
      String[] arrayOfString2 = null;
      String str1 = null;
      localCursor = MusicUtils.query(localContext, localUri1, arrayOfString1, null, arrayOfString2, str1);
      if (localCursor != null)
        break;
      return;
    }
    try
    {
      if (localCursor.moveToNext())
      {
        String str2 = localCursor.getString(1);
        String str3 = localCursor.getString(2);
        Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
        this.mProjectionMap.writeValueToArray(arrayOfObject, "_id", paramString);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "name", "");
        this.mProjectionMap.writeValueToArray(arrayOfObject, "display_header", str2);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "display_name", null);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "display_subname", str3);
        ProjectionMap localProjectionMap1 = this.mProjectionMap;
        Integer localInteger = Integer.valueOf(1);
        localProjectionMap1.writeValueToArray(arrayOfObject, "section_type", localInteger);
        ProjectionMap localProjectionMap2 = this.mProjectionMap;
        Uri localUri2 = XdiContentProvider.BASE_URI;
        String str4 = "/details/albums/" + paramString + "/tracks";
        String str5 = Uri.withAppendedPath(localUri2, str4).toString();
        localProjectionMap2.writeValueToArray(arrayOfObject, "content_uri", str5);
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
 * Qualified Name:     com.google.android.music.xdi.DetailAlbumSectionsCursor
 * JD-Core Version:    0.6.2
 */