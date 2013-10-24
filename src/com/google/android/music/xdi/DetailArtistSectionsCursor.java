package com.google.android.music.xdi;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.music.store.MusicContent.Artists;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;

class DetailArtistSectionsCursor extends MatrixCursor
{
  private static final String[] PROJECTION_ARTISTS = arrayOfString;
  private final Context mContext;
  private final ProjectionMap mProjectionMap;

  static
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "artist";
  }

  DetailArtistSectionsCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    addRowForArtist(paramString);
  }

  private void addRowForArtist(String paramString)
  {
    Context localContext = this.mContext;
    Uri localUri1 = MusicContent.Artists.CONTENT_URI.buildUpon().appendPath(paramString).build();
    String[] arrayOfString1 = PROJECTION_ARTISTS;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri1, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor == null)
      return;
    try
    {
      if (localCursor.moveToNext())
      {
        String str2 = localCursor.getString(1);
        Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
        ProjectionMap localProjectionMap1 = this.mProjectionMap;
        Integer localInteger1 = Integer.valueOf(1);
        localProjectionMap1.writeValueToArray(arrayOfObject, "_id", localInteger1);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "name", "");
        this.mProjectionMap.writeValueToArray(arrayOfObject, "display_header", str2);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "display_name", null);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "display_subname", null);
        ProjectionMap localProjectionMap2 = this.mProjectionMap;
        Integer localInteger2 = Integer.valueOf(1);
        localProjectionMap2.writeValueToArray(arrayOfObject, "section_type", localInteger2);
        ProjectionMap localProjectionMap3 = this.mProjectionMap;
        Uri localUri2 = XdiContentProvider.BASE_URI;
        String str3 = "/details/artists/" + paramString + "/albums";
        String str4 = Uri.withAppendedPath(localUri2, str3).toString();
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
 * Qualified Name:     com.google.android.music.xdi.DetailArtistSectionsCursor
 * JD-Core Version:    0.6.2
 */