package com.google.android.music.xdi;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.music.store.MusicContent.Artists;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;

class DetailArtistCursor extends MatrixCursor
{
  private static final String[] PROJECTION_ARTISTS = arrayOfString;
  private final Context mContext;
  private final ProjectionMap mProjectionMap;

  static
  {
    String[] arrayOfString = new String[3];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "artist";
    arrayOfString[2] = "artworkUrl";
  }

  DetailArtistCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    addRowForArtist(paramString);
  }

  private void addRowForArtist(String paramString)
  {
    Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
    Context localContext1;
    Uri localUri1;
    String[] arrayOfString1;
    String[] arrayOfString2;
    String str1;
    if (MusicUtils.isNautilusId(paramString))
    {
      localContext1 = this.mContext;
      localUri1 = MusicContent.Artists.getNautilusArtistsUri(paramString);
      arrayOfString1 = PROJECTION_ARTISTS;
      arrayOfString2 = null;
      str1 = null;
    }
    Context localContext2;
    Uri localUri2;
    String[] arrayOfString3;
    String[] arrayOfString4;
    String str2;
    for (Cursor localCursor = MusicUtils.query(localContext1, localUri1, arrayOfString1, null, arrayOfString2, str1); localCursor == null; localCursor = MusicUtils.query(localContext2, localUri2, arrayOfString3, null, arrayOfString4, str2))
    {
      return;
      localContext2 = this.mContext;
      localUri2 = MusicContent.Artists.getArtistsUri(Long.valueOf(paramString).longValue());
      arrayOfString3 = PROJECTION_ARTISTS;
      arrayOfString4 = null;
      str2 = null;
    }
    try
    {
      if (localCursor.moveToNext())
      {
        String str3 = localCursor.getString(1);
        String str4 = localCursor.getString(2);
        if (TextUtils.isEmpty(str4))
          str4 = XdiUtils.getDefaultArtistArtUri(this.mContext);
        ProjectionMap localProjectionMap1 = this.mProjectionMap;
        Integer localInteger1 = Integer.valueOf(localCursor.getPosition());
        localProjectionMap1.writeValueToArray(arrayOfObject, "_id", localInteger1);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "foreground_image_uri", str4);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "background_image_uri", str4);
        ProjectionMap localProjectionMap2 = this.mProjectionMap;
        String str5 = XdiUtils.getDefaultBadgeUri(this.mContext);
        localProjectionMap2.writeValueToArray(arrayOfObject, "badge_uri", str5);
        ProjectionMap localProjectionMap3 = this.mProjectionMap;
        Integer localInteger2 = Integer.valueOf(this.mContext.getResources().getColor(2131427398));
        localProjectionMap3.writeValueToArray(arrayOfObject, "color_hint", localInteger2);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "text_color_hint", null);
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
 * Qualified Name:     com.google.android.music.xdi.DetailArtistCursor
 * JD-Core Version:    0.6.2
 */