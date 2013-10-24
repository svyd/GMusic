package com.google.android.music.xdi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import com.google.android.music.log.Log;
import com.google.android.music.store.MusicContent.AlbumArt;
import com.google.android.music.store.MusicContent.Albums;
import com.google.android.music.store.MusicContent.Artists;
import com.google.android.music.store.MusicContent.Genres;
import com.google.android.music.utils.MusicUtils;

class BrowseMyMusicHeaderCursor extends XdiCursor
{
  private static final String[] PROJECTION_ALBUMS = arrayOfString3;
  private static final String[] PROJECTION_ARTISTS;
  private static final String[] PROJECTION_GENRES;
  private final int mArtistItemHeight;
  private final int mArtistItemWidth;
  private final long mHeaderId;
  private final int mImageHeight;
  private final int mImageWidth;

  static
  {
    String[] arrayOfString1 = new String[2];
    arrayOfString1[0] = "_id";
    arrayOfString1[1] = "name";
    PROJECTION_GENRES = arrayOfString1;
    String[] arrayOfString2 = new String[3];
    arrayOfString2[0] = "_id";
    arrayOfString2[1] = "artist";
    arrayOfString2[2] = "artworkUrl";
    PROJECTION_ARTISTS = arrayOfString2;
    String[] arrayOfString3 = new String[3];
    arrayOfString3[0] = "_id";
    arrayOfString3[1] = "album_name";
    arrayOfString3[2] = "album_artist";
  }

  BrowseMyMusicHeaderCursor(Context paramContext, String[] paramArrayOfString, long paramLong)
  {
    super(paramContext, paramArrayOfString, localCursor);
    this.mHeaderId = paramLong;
    int i = XdiUtils.getDefaultItemWidthPx(paramContext);
    this.mImageWidth = i;
    int j = XdiUtils.getDefaultItemHeightPx(paramContext);
    this.mImageHeight = j;
    int k = XdiUtils.getDefaultArtistItemWidthDp(paramContext);
    this.mArtistItemWidth = k;
    int m = XdiUtils.getDefaultArtistItemHeightDp(paramContext);
    this.mArtistItemHeight = m;
  }

  private boolean extractDataForAlbums(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    Context localContext = getContext();
    long l = localCursor.getLong(0);
    String str1 = localCursor.getString(1);
    String str2 = localCursor.getString(2);
    int i = this.mImageWidth;
    int j = this.mImageHeight;
    Uri localUri = MusicContent.AlbumArt.getAlbumArtUri(l, true, i, j);
    Uri.Builder localBuilder = XdiContentProvider.BASE_URI.buildUpon().appendEncodedPath("details/albums");
    String str3 = String.valueOf(l);
    Intent localIntent = XdiContract.getDetailsIntent(localBuilder.appendPath(str3).build());
    Long localLong = Long.valueOf(l);
    writeValueToArray(paramArrayOfObject, "_id", localLong);
    Integer localInteger = Integer.valueOf(3);
    writeValueToArray(paramArrayOfObject, "parent_id", localInteger);
    writeValueToArray(paramArrayOfObject, "display_name", str1);
    writeValueToArray(paramArrayOfObject, "display_description", str2);
    writeValueToArray(paramArrayOfObject, "image_uri", localUri);
    writeValueToArray(paramArrayOfObject, "width", null);
    writeValueToArray(paramArrayOfObject, "height", null);
    String str4 = localIntent.toUri(1);
    writeValueToArray(paramArrayOfObject, "intent_uri", str4);
    return true;
  }

  private boolean extractDataForArtists(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    Context localContext = getContext();
    long l = localCursor.getLong(0);
    String str1 = localCursor.getString(1);
    String str2 = localCursor.getString(2);
    if (TextUtils.isEmpty(str2))
      str2 = XdiUtils.getDefaultArtistArtUri(localContext);
    int i = MusicContent.Artists.getAlbumsByArtistCount(localContext, l, false);
    Uri.Builder localBuilder = XdiContentProvider.BASE_URI.buildUpon().appendEncodedPath("details/artists");
    String str3 = String.valueOf(l);
    Intent localIntent = XdiContract.getDetailsIntent(localBuilder.appendPath(str3).build());
    Long localLong = Long.valueOf(l);
    writeValueToArray(paramArrayOfObject, "_id", localLong);
    Integer localInteger1 = Integer.valueOf(2);
    writeValueToArray(paramArrayOfObject, "parent_id", localInteger1);
    writeValueToArray(paramArrayOfObject, "display_name", str1);
    Resources localResources = localContext.getResources();
    Object[] arrayOfObject = new Object[1];
    Integer localInteger2 = Integer.valueOf(i);
    arrayOfObject[0] = localInteger2;
    String str4 = localResources.getQuantityString(2131623938, i, arrayOfObject);
    writeValueToArray(paramArrayOfObject, "display_description", str4);
    writeValueToArray(paramArrayOfObject, "image_uri", str2);
    Integer localInteger3 = Integer.valueOf(this.mArtistItemWidth);
    writeValueToArray(paramArrayOfObject, "width", localInteger3);
    Integer localInteger4 = Integer.valueOf(this.mArtistItemHeight);
    writeValueToArray(paramArrayOfObject, "height", localInteger4);
    String str5 = localIntent.toUri(1);
    writeValueToArray(paramArrayOfObject, "intent_uri", str5);
    return true;
  }

  private boolean extractDataForGenres(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    long l = localCursor.getLong(0);
    String str1 = localCursor.getString(1);
    int i = MusicContent.Genres.getAlbumsOfGenreCount(getContext(), l);
    Uri localUri = XdiContentProvider.BASE_URI.buildUpon().appendPath("mygenres").build();
    int j = localCursor.getPosition();
    Intent localIntent1 = XdiContract.getBrowseIntent(localUri, j);
    String str2 = XdiUtils.getMetaUri(2L).toString();
    Intent localIntent2 = localIntent1.putExtra("meta_uri", str2);
    Long localLong = Long.valueOf(l);
    writeValueToArray(paramArrayOfObject, "_id", localLong);
    Integer localInteger1 = Integer.valueOf(1);
    writeValueToArray(paramArrayOfObject, "parent_id", localInteger1);
    writeValueToArray(paramArrayOfObject, "display_name", str1);
    Resources localResources = getContext().getResources();
    Object[] arrayOfObject = new Object[1];
    Integer localInteger2 = Integer.valueOf(i);
    arrayOfObject[0] = localInteger2;
    String str3 = localResources.getQuantityString(2131623938, i, arrayOfObject);
    writeValueToArray(paramArrayOfObject, "display_description", str3);
    String str4 = XdiUtils.getDefaultGenreArtUri(getContext());
    writeValueToArray(paramArrayOfObject, "image_uri", str4);
    writeValueToArray(paramArrayOfObject, "width", null);
    writeValueToArray(paramArrayOfObject, "height", null);
    String str5 = localIntent1.toUri(1);
    writeValueToArray(paramArrayOfObject, "intent_uri", str5);
    return true;
  }

  private static Cursor getCursorForHeader(Context paramContext, long paramLong)
  {
    Object localObject;
    switch ((int)paramLong)
    {
    default:
      String str1 = "Unexpected header id: " + paramLong;
      Log.wtf("MusicXdi", str1);
      localObject = new MatrixCursor(null);
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      return localObject;
      Uri localUri1 = MusicContent.Genres.CONTENT_URI;
      String[] arrayOfString1 = PROJECTION_GENRES;
      Context localContext1 = paramContext;
      String[] arrayOfString2 = null;
      String str2 = null;
      localObject = MusicUtils.query(localContext1, localUri1, arrayOfString1, null, arrayOfString2, str2);
      continue;
      Uri localUri2 = MusicContent.Artists.CONTENT_URI;
      String[] arrayOfString3 = PROJECTION_ARTISTS;
      Context localContext2 = paramContext;
      String[] arrayOfString4 = null;
      String str3 = null;
      localObject = MusicUtils.query(localContext2, localUri2, arrayOfString3, null, arrayOfString4, str3);
      continue;
      Uri localUri3 = MusicContent.Albums.CONTENT_URI;
      String[] arrayOfString5 = PROJECTION_ALBUMS;
      Context localContext3 = paramContext;
      String[] arrayOfString6 = null;
      String str4 = null;
      localObject = MusicUtils.query(localContext3, localUri3, arrayOfString5, null, arrayOfString6, str4);
    }
  }

  protected boolean extractDataForCurrentRow(Object[] paramArrayOfObject)
  {
    boolean bool1;
    switch ((int)this.mHeaderId)
    {
    default:
      StringBuilder localStringBuilder = new StringBuilder().append("Unexpected header id: ");
      long l = this.mHeaderId;
      String str = l;
      Log.wtf("MusicXdi", str);
      bool1 = false;
      return bool1;
    case 1:
      boolean bool2 = extractDataForGenres(paramArrayOfObject);
    case 2:
    case 3:
    }
    while (true)
    {
      bool1 = true;
      break;
      boolean bool3 = extractDataForArtists(paramArrayOfObject);
      continue;
      boolean bool4 = extractDataForAlbums(paramArrayOfObject);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.BrowseMyMusicHeaderCursor
 * JD-Core Version:    0.6.2
 */