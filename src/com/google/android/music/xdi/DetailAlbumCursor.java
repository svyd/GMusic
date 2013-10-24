package com.google.android.music.xdi;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.music.store.MusicContent.AlbumArt;
import com.google.android.music.store.MusicContent.Albums;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;

class DetailAlbumCursor extends MatrixCursor
{
  private static final String[] PROJECTION_ALBUMS = arrayOfString;
  private final Context mContext;
  private final int mImageHeight;
  private final int mImageWidth;
  private final ProjectionMap mProjectionMap;

  static
  {
    String[] arrayOfString = new String[3];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "album_name";
    arrayOfString[2] = "album_art";
  }

  DetailAlbumCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    int i = XdiUtils.getDefaultItemWidthPx(paramContext);
    this.mImageWidth = i;
    int j = XdiUtils.getDefaultItemHeightPx(paramContext);
    this.mImageHeight = j;
    addRowForAlbum(paramString);
  }

  private void addRowForAlbum(String paramString)
  {
    Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
    boolean bool = MusicUtils.isNautilusId(paramString);
    Context localContext1;
    Uri localUri1;
    String[] arrayOfString1;
    String[] arrayOfString2;
    String str1;
    if (bool)
    {
      localContext1 = this.mContext;
      localUri1 = MusicContent.Albums.getNautilusAlbumsUri(paramString);
      arrayOfString1 = PROJECTION_ALBUMS;
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
      localUri2 = MusicContent.Albums.getAlbumsUri(Long.valueOf(paramString).longValue());
      arrayOfString3 = PROJECTION_ALBUMS;
      arrayOfString4 = null;
      str2 = null;
    }
    try
    {
      if (localCursor.moveToNext())
      {
        String str3 = localCursor.getString(1);
        if (!bool)
          break label279;
        localObject1 = localCursor.getString(2);
        if (!TextUtils.isEmpty((CharSequence)localObject1));
      }
      label279: String str5;
      for (Object localObject1 = XdiUtils.getDefaultAlbumArtUri(this.mContext); ; localObject1 = str5)
      {
        this.mProjectionMap.writeValueToArray(arrayOfObject, "_id", paramString);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "foreground_image_uri", localObject1);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "background_image_uri", localObject1);
        ProjectionMap localProjectionMap1 = this.mProjectionMap;
        String str4 = XdiUtils.getDefaultBadgeUri(this.mContext);
        localProjectionMap1.writeValueToArray(arrayOfObject, "badge_uri", str4);
        ProjectionMap localProjectionMap2 = this.mProjectionMap;
        Integer localInteger = Integer.valueOf(this.mContext.getResources().getColor(2131427398));
        localProjectionMap2.writeValueToArray(arrayOfObject, "color_hint", localInteger);
        this.mProjectionMap.writeValueToArray(arrayOfObject, "text_color_hint", null);
        addRow(arrayOfObject);
        return;
        long l = Long.valueOf(paramString).longValue();
        int i = this.mImageWidth;
        int j = this.mImageHeight;
        str5 = MusicContent.AlbumArt.getAlbumArtUri(l, true, i, j).toString();
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.DetailAlbumCursor
 * JD-Core Version:    0.6.2
 */