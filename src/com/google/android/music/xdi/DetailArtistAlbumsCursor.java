package com.google.android.music.xdi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.music.log.Log;
import com.google.android.music.store.MusicContent.AlbumArt;
import com.google.android.music.store.MusicContent.Albums;
import com.google.android.music.store.MusicContent.Artists;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;

class DetailArtistAlbumsCursor extends MatrixCursor
{
  private static final String[] PROJECTION_ARTIST_ALBUMS = arrayOfString;
  private final Context mContext;
  private final int mImageHeight;
  private final int mImageWidth;
  private final ProjectionMap mProjectionMap;

  static
  {
    String[] arrayOfString = new String[7];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "album_name";
    arrayOfString[2] = "album_art";
    arrayOfString[3] = "album_artist";
    arrayOfString[4] = "album_artist_id";
    arrayOfString[5] = "StoreAlbumId";
    arrayOfString[6] = "ArtistMetajamId";
  }

  DetailArtistAlbumsCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    int i = XdiUtils.getDefaultItemWidthPx(paramContext);
    this.mImageWidth = i;
    int j = XdiUtils.getDefaultItemHeightPx(paramContext);
    this.mImageHeight = j;
    addRowsForAlbums(paramString);
  }

  private void addRowsForAlbums(String paramString)
  {
    Object[] arrayOfObject1 = new Object[this.mProjectionMap.size()];
    boolean bool = MusicUtils.isNautilusId(paramString);
    Context localContext1;
    Uri localUri1;
    String[] arrayOfString1;
    if (bool)
    {
      localContext1 = this.mContext;
      localUri1 = MusicContent.Artists.getAlbumsByNautilusArtistsUri(paramString);
      arrayOfString1 = PROJECTION_ARTIST_ALBUMS;
    }
    Context localContext2;
    Uri localUri2;
    String[] arrayOfString2;
    for (Cursor localCursor = MusicUtils.query(localContext1, localUri1, arrayOfString1, null, null, null); localCursor == null; localCursor = MusicUtils.query(localContext2, localUri2, arrayOfString2, null, null, null))
    {
      return;
      long l1 = Long.valueOf(paramString).longValue();
      localContext2 = this.mContext;
      localUri2 = MusicContent.Artists.getAlbumsByArtistsUri(l1);
      arrayOfString2 = PROJECTION_ARTIST_ALBUMS;
    }
    long l2;
    String str1;
    String str2;
    Object localObject1;
    try
    {
      while (true)
      {
        if (!localCursor.moveToNext())
          break label734;
        l2 = localCursor.getLong(0);
        str1 = localCursor.getString(1);
        str2 = localCursor.getString(3);
        long l3 = localCursor.getLong(4);
        localObject1 = localCursor.getString(5);
        String str3 = localCursor.getString(6);
        if (!bool)
          break label659;
        if (!TextUtils.isEmpty((CharSequence)localObject1))
          break;
        Log.wtf("MusicXdi", "Invalid album metajam ID for nautilus artist.");
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
    int i = null;
    String str5 = localCursor.getString(i);
    if (TextUtils.isEmpty(str5))
      str5 = XdiUtils.getDefaultAlbumArtUri(this.mContext);
    int j = MusicContent.Albums.getAudioInNautilusAlbumCount(this.mContext, (String)localObject1, false);
    label247: Object localObject3 = null;
    Intent localIntent = new android/content/Intent;
    String str4 = "com.google.android.xdi.action.DETAIL";
    Uri localUri3 = XdiContentProvider.BASE_URI;
    StringBuilder localStringBuilder = new StringBuilder().append("details/albums/");
    if (bool);
    while (true)
    {
      String str6 = localObject1;
      Uri localUri4 = Uri.withAppendedPath(localUri3, str6);
      localIntent.<init>(str4, localUri4);
      ProjectionMap localProjectionMap1 = this.mProjectionMap;
      Integer localInteger1 = Integer.valueOf(localCursor.getPosition());
      Object[] arrayOfObject2 = arrayOfObject1;
      localProjectionMap1.writeValueToArray(arrayOfObject2, "_id", localInteger1);
      ProjectionMap localProjectionMap2 = this.mProjectionMap;
      Object[] arrayOfObject3 = arrayOfObject1;
      localProjectionMap2.writeValueToArray(arrayOfObject3, "display_name", str1);
      ProjectionMap localProjectionMap3 = this.mProjectionMap;
      Object[] arrayOfObject4 = arrayOfObject1;
      Object localObject4 = localObject3;
      localProjectionMap3.writeValueToArray(arrayOfObject4, "display_subname", localObject4);
      Resources localResources = this.mContext.getResources();
      Object[] arrayOfObject5 = new Object[1];
      Integer localInteger2 = Integer.valueOf(j);
      arrayOfObject5[0] = localInteger2;
      int k = j;
      String str7 = localResources.getQuantityString(2131623936, k, arrayOfObject5);
      ProjectionMap localProjectionMap4 = this.mProjectionMap;
      Object[] arrayOfObject6 = arrayOfObject1;
      String str8 = str7;
      localProjectionMap4.writeValueToArray(arrayOfObject6, "display_description", str8);
      ProjectionMap localProjectionMap5 = this.mProjectionMap;
      Object[] arrayOfObject7 = arrayOfObject1;
      localProjectionMap5.writeValueToArray(arrayOfObject7, "display_number", null);
      ProjectionMap localProjectionMap6 = this.mProjectionMap;
      Object[] arrayOfObject8 = arrayOfObject1;
      localProjectionMap6.writeValueToArray(arrayOfObject8, "image_uri", str5);
      ProjectionMap localProjectionMap7 = this.mProjectionMap;
      Integer localInteger3 = Integer.valueOf(0);
      Object[] arrayOfObject9 = arrayOfObject1;
      localProjectionMap7.writeValueToArray(arrayOfObject9, "item_display_type", localInteger3);
      ProjectionMap localProjectionMap8 = this.mProjectionMap;
      Object[] arrayOfObject10 = arrayOfObject1;
      localProjectionMap8.writeValueToArray(arrayOfObject10, "user_rating_count", null);
      ProjectionMap localProjectionMap9 = this.mProjectionMap;
      Integer localInteger4 = Integer.valueOf(-1);
      Object[] arrayOfObject11 = arrayOfObject1;
      localProjectionMap9.writeValueToArray(arrayOfObject11, "user_rating", localInteger4);
      ProjectionMap localProjectionMap10 = this.mProjectionMap;
      String str9 = localIntent.toUri(1);
      Object[] arrayOfObject12 = arrayOfObject1;
      localProjectionMap10.writeValueToArray(arrayOfObject12, "action_uri", str9);
      ProjectionMap localProjectionMap11 = this.mProjectionMap;
      Object[] arrayOfObject13 = arrayOfObject1;
      localProjectionMap11.writeValueToArray(arrayOfObject13, "music_albumArtist", str2);
      ProjectionMap localProjectionMap12 = this.mProjectionMap;
      Integer localInteger5 = Integer.valueOf(j);
      Object[] arrayOfObject14 = arrayOfObject1;
      localProjectionMap12.writeValueToArray(arrayOfObject14, "music_trackCount", localInteger5);
      DetailArtistAlbumsCursor localDetailArtistAlbumsCursor = this;
      Object[] arrayOfObject15 = arrayOfObject1;
      localDetailArtistAlbumsCursor.addRow(arrayOfObject15);
      break;
      label659: if (l2 <= 65535L)
      {
        Log.wtf("MusicXdi", "Invalid album ID for non-nautilus artist.");
        break;
      }
      int m = this.mImageWidth;
      int n = this.mImageHeight;
      str5 = MusicContent.AlbumArt.getAlbumArtUri(l2, true, m, n).toString();
      j = MusicContent.Albums.getAudioInAlbumCount(this.mContext, l2, false);
      break label247;
      Long localLong = Long.valueOf(l2);
      localObject1 = localLong;
    }
    label734: Store.safeClose(localCursor);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.DetailArtistAlbumsCursor
 * JD-Core Version:    0.6.2
 */