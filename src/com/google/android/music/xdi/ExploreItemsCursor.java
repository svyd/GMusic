package com.google.android.music.xdi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import com.google.android.music.log.Log;
import com.google.android.music.store.MusicContent.Explore;
import com.google.android.music.utils.MusicUtils;

class ExploreItemsCursor extends XdiCursor
{
  private final String mGenreId;
  private final long mHeaderId;

  ExploreItemsCursor(Context paramContext, String[] paramArrayOfString, long paramLong, String paramString)
  {
    super(paramContext, paramArrayOfString, localCursor);
    this.mHeaderId = paramLong;
    this.mGenreId = paramString;
  }

  private void extractDataForFeaturedContent(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    Context localContext = getContext();
    int i = localCursor.getPosition();
    long l = i;
    String str1 = localCursor.getString(1);
    String str2 = localCursor.getString(2);
    String str3 = "explore/featured";
    if (!TextUtils.isEmpty(this.mGenreId))
    {
      StringBuilder localStringBuilder = new StringBuilder().append("explore/featured").append("/");
      String str4 = this.mGenreId;
      str3 = str4;
    }
    Intent localIntent1 = XdiContract.getBrowseIntent(XdiContentProvider.BASE_URI.buildUpon().appendEncodedPath(str3).build(), i);
    String str5 = XdiUtils.getMetaUri(4L).toString();
    Intent localIntent2 = localIntent1.putExtra("meta_uri", str5);
    Long localLong = Long.valueOf(l);
    writeValueToArray(paramArrayOfObject, "_id", localLong);
    writeValueToArray(paramArrayOfObject, "display_name", str1);
    writeValueToArray(paramArrayOfObject, "display_description", str2);
    String str6 = XdiUtils.getDefaultArtUri(getContext());
    writeValueToArray(paramArrayOfObject, "image_uri", str6);
    writeValueToArray(paramArrayOfObject, "width", null);
    writeValueToArray(paramArrayOfObject, "height", null);
    String str7 = localIntent1.toUri(1);
    writeValueToArray(paramArrayOfObject, "intent_uri", str7);
  }

  private void extractDataForGenres(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    Context localContext = getContext();
    int i = localCursor.getPosition();
    long l = localCursor.getPosition();
    String str1 = localCursor.getString(1);
    String str2 = localCursor.getString(2);
    Intent localIntent1 = XdiContract.getBrowseIntent(XdiContentProvider.BASE_URI.buildUpon().appendEncodedPath("explore/genres").appendPath(str1).appendPath(str2).build(), i);
    String str3 = XdiUtils.getMetaUri(7L).toString();
    Intent localIntent2 = localIntent1.putExtra("meta_uri", str3);
    Long localLong = Long.valueOf(l);
    writeValueToArray(paramArrayOfObject, "_id", localLong);
    writeValueToArray(paramArrayOfObject, "display_name", str2);
    writeValueToArray(paramArrayOfObject, "display_description", null);
    String str4 = XdiUtils.getDefaultArtUri(getContext());
    writeValueToArray(paramArrayOfObject, "image_uri", str4);
    writeValueToArray(paramArrayOfObject, "width", null);
    writeValueToArray(paramArrayOfObject, "height", null);
    String str5 = localIntent1.toUri(1);
    writeValueToArray(paramArrayOfObject, "intent_uri", str5);
  }

  private void extractDataForNewReleases(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    Context localContext = getContext();
    int i = localCursor.getPosition();
    long l = i;
    String str1 = localCursor.getString(1);
    String str2 = localCursor.getString(2);
    String str3 = "explore/newreleases";
    if (!TextUtils.isEmpty(this.mGenreId))
    {
      StringBuilder localStringBuilder = new StringBuilder().append("explore/newreleases").append("/");
      String str4 = this.mGenreId;
      str3 = str4;
    }
    Intent localIntent1 = XdiContract.getBrowseIntent(XdiContentProvider.BASE_URI.buildUpon().appendEncodedPath(str3).build(), i);
    String str5 = XdiUtils.getMetaUri(6L).toString();
    Intent localIntent2 = localIntent1.putExtra("meta_uri", str5);
    Long localLong = Long.valueOf(l);
    writeValueToArray(paramArrayOfObject, "_id", localLong);
    writeValueToArray(paramArrayOfObject, "display_name", str1);
    writeValueToArray(paramArrayOfObject, "display_description", str2);
    String str6 = XdiUtils.getDefaultArtUri(getContext());
    writeValueToArray(paramArrayOfObject, "image_uri", str6);
    writeValueToArray(paramArrayOfObject, "width", null);
    writeValueToArray(paramArrayOfObject, "height", null);
    String str7 = localIntent1.toUri(1);
    writeValueToArray(paramArrayOfObject, "intent_uri", str7);
  }

  private void extractDataForRecommendations(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    Context localContext = getContext();
    int i = localCursor.getPosition();
    long l = i;
    String str1 = localCursor.getString(1);
    String str2 = localCursor.getString(2);
    Intent localIntent1 = XdiContract.getBrowseIntent(XdiContentProvider.BASE_URI.buildUpon().appendEncodedPath("explore/recommendations").build(), i);
    String str3 = XdiUtils.getMetaUri(5L).toString();
    Intent localIntent2 = localIntent1.putExtra("meta_uri", str3);
    Long localLong = Long.valueOf(l);
    writeValueToArray(paramArrayOfObject, "_id", localLong);
    writeValueToArray(paramArrayOfObject, "display_name", str1);
    writeValueToArray(paramArrayOfObject, "display_description", str2);
    String str4 = XdiUtils.getDefaultArtUri(getContext());
    writeValueToArray(paramArrayOfObject, "image_uri", str4);
    writeValueToArray(paramArrayOfObject, "width", null);
    writeValueToArray(paramArrayOfObject, "height", null);
    String str5 = localIntent1.toUri(1);
    writeValueToArray(paramArrayOfObject, "intent_uri", str5);
  }

  private static Cursor getCursorForHeader(Context paramContext, long paramLong, String paramString)
  {
    Object localObject;
    switch ((int)paramLong)
    {
    default:
      String str1 = "BrowseExploreHeaderCursor: Unexpected header id: " + paramLong;
      Log.wtf("MusicXdi", str1);
      localObject = new MatrixCursor(null);
    case 1:
    case 2:
    case 3:
    case 4:
    }
    while (true)
    {
      return localObject;
      Uri localUri1 = MusicContent.Explore.getTopChartGroupsUri(paramString);
      String[] arrayOfString1 = MusicProjections.PROJECTION_ENTITY_GROUPS;
      Context localContext1 = paramContext;
      String[] arrayOfString2 = null;
      String str2 = null;
      localObject = MusicUtils.query(localContext1, localUri1, arrayOfString1, null, arrayOfString2, str2);
      continue;
      Uri localUri2 = MusicContent.Explore.getRecommendedGroupsUri();
      String[] arrayOfString3 = MusicProjections.PROJECTION_ENTITY_GROUPS;
      Context localContext2 = paramContext;
      String[] arrayOfString4 = null;
      String str3 = null;
      localObject = MusicUtils.query(localContext2, localUri2, arrayOfString3, null, arrayOfString4, str3);
      continue;
      Uri localUri3 = MusicContent.Explore.getNewReleaseGroupsUri(paramString);
      String[] arrayOfString5 = MusicProjections.PROJECTION_ENTITY_GROUPS;
      Context localContext3 = paramContext;
      String[] arrayOfString6 = null;
      String str4 = null;
      localObject = MusicUtils.query(localContext3, localUri3, arrayOfString5, null, arrayOfString6, str4);
      continue;
      Uri localUri4 = MusicContent.Explore.getGenresUri(paramString);
      String[] arrayOfString7 = MusicProjections.PROJECTION_GENRES;
      Context localContext4 = paramContext;
      String[] arrayOfString8 = null;
      String str5 = null;
      localObject = MusicUtils.query(localContext4, localUri4, arrayOfString7, null, arrayOfString8, str5);
    }
  }

  protected boolean extractDataForCurrentRow(Object[] paramArrayOfObject)
  {
    boolean bool;
    switch ((int)this.mHeaderId)
    {
    default:
      StringBuilder localStringBuilder = new StringBuilder().append("BrowseExploreHeaderCursor: Unexpected header id: ");
      long l = this.mHeaderId;
      String str = l;
      Log.wtf("MusicXdi", str);
      bool = false;
      return bool;
    case 1:
      extractDataForFeaturedContent(paramArrayOfObject);
    case 2:
    case 3:
    case 4:
    }
    while (true)
    {
      bool = true;
      break;
      extractDataForRecommendations(paramArrayOfObject);
      continue;
      extractDataForNewReleases(paramArrayOfObject);
      continue;
      extractDataForGenres(paramArrayOfObject);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.ExploreItemsCursor
 * JD-Core Version:    0.6.2
 */