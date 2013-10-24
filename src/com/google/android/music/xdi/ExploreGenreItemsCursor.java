package com.google.android.music.xdi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.music.store.MusicContent.Explore;
import com.google.android.music.utils.MusicUtils;

class ExploreGenreItemsCursor extends XdiCursor
{
  private final ProjectionMap mProjectionMap;

  ExploreGenreItemsCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    super(paramContext, paramArrayOfString, localCursor);
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
  }

  private static Cursor getCursorForHeader(Context paramContext, String paramString)
  {
    Uri localUri = MusicContent.Explore.getGenresUri(paramString);
    String[] arrayOfString1 = MusicProjections.PROJECTION_GENRES;
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str = null;
    return MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str);
  }

  protected boolean extractDataForCurrentRow(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    Context localContext = getContext();
    long l = localCursor.getLong(0);
    String str1 = localCursor.getString(1);
    String str2 = localCursor.getString(2);
    Uri localUri = XdiContentProvider.BASE_URI.buildUpon().appendEncodedPath("explore/genre").appendPath(str1).build();
    int i = localCursor.getPosition();
    Intent localIntent1 = XdiContract.getBrowseIntent(localUri, i);
    String str3 = XdiUtils.getMetaTitleUri(str2).toString();
    Intent localIntent2 = localIntent1.putExtra("meta_uri", str3);
    ProjectionMap localProjectionMap1 = this.mProjectionMap;
    Long localLong = Long.valueOf(l);
    localProjectionMap1.writeValueToArray(paramArrayOfObject, "_id", localLong);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "display_name", str2);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "display_description", null);
    ProjectionMap localProjectionMap2 = this.mProjectionMap;
    String str4 = XdiUtils.getDefaultArtUri(getContext());
    localProjectionMap2.writeValueToArray(paramArrayOfObject, "image_uri", str4);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "width", null);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "height", null);
    ProjectionMap localProjectionMap3 = this.mProjectionMap;
    String str5 = localIntent1.toUri(1);
    localProjectionMap3.writeValueToArray(paramArrayOfObject, "intent_uri", str5);
    return true;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.ExploreGenreItemsCursor
 * JD-Core Version:    0.6.2
 */