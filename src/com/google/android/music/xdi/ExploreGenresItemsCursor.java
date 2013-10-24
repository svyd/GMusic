package com.google.android.music.xdi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.music.store.MusicContent.Explore;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;

class ExploreGenresItemsCursor extends MatrixCursor
{
  private final Context mContext;
  private final String mGenreId;
  private final String mGenreName;
  private final ProjectionMap mProjectionMap;

  ExploreGenresItemsCursor(Context paramContext, String[] paramArrayOfString, String paramString1, String paramString2)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    this.mGenreId = paramString1;
    this.mGenreName = paramString2;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    Object[] arrayOfObject = new Object[this.mProjectionMap.size()];
    addSubgenres(arrayOfObject);
    addFeatured(arrayOfObject);
    addNewReleases(arrayOfObject);
  }

  void addFeatured(Object[] paramArrayOfObject)
  {
    Context localContext = this.mContext;
    Uri localUri = MusicContent.Explore.getTopChartGroupsUri(this.mGenreId);
    String[] arrayOfString1 = MusicProjections.PROJECTION_ENTITY_GROUPS;
    String[] arrayOfString2 = null;
    String str = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str);
    if (localCursor == null)
      return;
    try
    {
      addItems(localCursor, paramArrayOfObject);
      return;
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  void addItems(Cursor paramCursor, Object[] paramArrayOfObject)
  {
    while (true)
    {
      if (!paramCursor.moveToNext())
        return;
      int i = getCount();
      long l = paramCursor.getLong(0);
      int j = paramCursor.getInt(4);
      String str1 = paramCursor.getString(1);
      String str2 = paramCursor.getString(2);
      Uri.Builder localBuilder = XdiContentProvider.BASE_URI.buildUpon().appendEncodedPath("explore/genre");
      String str3 = this.mGenreId;
      Intent localIntent1 = XdiContract.getBrowseIntent(localBuilder.appendPath(str3).build(), i);
      String str4 = XdiUtils.getMetaTitleUri(this.mGenreName).toString();
      Intent localIntent2 = localIntent1.putExtra("meta_uri", str4);
      ProjectionMap localProjectionMap1 = this.mProjectionMap;
      Integer localInteger = Integer.valueOf(i);
      localProjectionMap1.writeValueToArray(paramArrayOfObject, "_id", localInteger);
      this.mProjectionMap.writeValueToArray(paramArrayOfObject, "display_name", str1);
      this.mProjectionMap.writeValueToArray(paramArrayOfObject, "display_description", str2);
      ProjectionMap localProjectionMap2 = this.mProjectionMap;
      String str5 = XdiUtils.getDefaultArtUri(this.mContext);
      localProjectionMap2.writeValueToArray(paramArrayOfObject, "image_uri", str5);
      this.mProjectionMap.writeValueToArray(paramArrayOfObject, "width", null);
      this.mProjectionMap.writeValueToArray(paramArrayOfObject, "height", null);
      ProjectionMap localProjectionMap3 = this.mProjectionMap;
      String str6 = localIntent1.toUri(1);
      localProjectionMap3.writeValueToArray(paramArrayOfObject, "intent_uri", str6);
      addRow(paramArrayOfObject);
      int k = i + 1;
    }
  }

  void addNewReleases(Object[] paramArrayOfObject)
  {
    Context localContext = this.mContext;
    Uri localUri = MusicContent.Explore.getNewReleaseGroupsUri(this.mGenreId);
    String[] arrayOfString1 = MusicProjections.PROJECTION_ENTITY_GROUPS;
    String[] arrayOfString2 = null;
    String str = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str);
    if (localCursor == null)
      return;
    try
    {
      addItems(localCursor, paramArrayOfObject);
      return;
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  void addSubgenres(Object[] paramArrayOfObject)
  {
    Context localContext = this.mContext;
    String str1 = this.mGenreId;
    if (!XdiUtils.hasSubgenres(localContext, str1))
      return;
    int i = getCount();
    String str2 = this.mContext.getString(2131230859);
    Uri.Builder localBuilder = XdiContentProvider.BASE_URI.buildUpon().appendEncodedPath("explore/genre");
    String str3 = this.mGenreId;
    Intent localIntent1 = XdiContract.getBrowseIntent(localBuilder.appendPath(str3).build(), i);
    String str4 = XdiUtils.getMetaTitleUri(this.mGenreName).toString();
    Intent localIntent2 = localIntent1.putExtra("meta_uri", str4);
    ProjectionMap localProjectionMap1 = this.mProjectionMap;
    Integer localInteger = Integer.valueOf(i);
    localProjectionMap1.writeValueToArray(paramArrayOfObject, "_id", localInteger);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "display_name", str2);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "display_description", null);
    ProjectionMap localProjectionMap2 = this.mProjectionMap;
    String str5 = XdiUtils.getDefaultArtUri(this.mContext);
    localProjectionMap2.writeValueToArray(paramArrayOfObject, "image_uri", str5);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "width", null);
    this.mProjectionMap.writeValueToArray(paramArrayOfObject, "height", null);
    ProjectionMap localProjectionMap3 = this.mProjectionMap;
    String str6 = localIntent1.toUri(1);
    localProjectionMap3.writeValueToArray(paramArrayOfObject, "intent_uri", str6);
    addRow(paramArrayOfObject);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.ExploreGenresItemsCursor
 * JD-Core Version:    0.6.2
 */