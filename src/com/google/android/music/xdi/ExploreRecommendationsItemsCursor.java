package com.google.android.music.xdi;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import com.google.android.music.store.MusicContent.Explore;
import com.google.android.music.utils.MusicUtils;

class ExploreRecommendationsItemsCursor extends XdiCursor
{
  private final int mGroupType;
  private final int mImageHeight;
  private final int mImageWidth;
  private final ProjectionMap mProjectionMap;

  ExploreRecommendationsItemsCursor(Context paramContext, String[] paramArrayOfString, long paramLong, int paramInt)
  {
    super(paramContext, paramArrayOfString, localCursor);
    this.mGroupType = paramInt;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    int i = XdiUtils.getDefaultItemWidthPx(paramContext);
    this.mImageWidth = i;
    int j = XdiUtils.getDefaultItemHeightPx(paramContext);
    this.mImageHeight = j;
  }

  private static Cursor getCursorForHeader(Context paramContext, long paramLong, int paramInt)
  {
    String[] arrayOfString1 = XdiUtils.getClusterProjection(paramInt);
    if (arrayOfString1 == null);
    Uri localUri;
    Context localContext;
    String[] arrayOfString2;
    String str;
    for (Object localObject = new MatrixCursor(null); ; localObject = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str))
    {
      return localObject;
      localUri = MusicContent.Explore.getRecommendedUri(paramLong);
      localContext = paramContext;
      arrayOfString2 = null;
      str = null;
    }
  }

  protected boolean extractDataForCurrentRow(Object[] paramArrayOfObject)
  {
    Context localContext = getContext();
    Cursor localCursor = getWrappedCursor();
    ProjectionMap localProjectionMap = this.mProjectionMap;
    int i = this.mGroupType;
    int j = this.mImageWidth;
    int k = this.mImageHeight;
    Object[] arrayOfObject = paramArrayOfObject;
    return XdiUtils.extractDataForGroupType(localContext, localCursor, localProjectionMap, arrayOfObject, i, j, k);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.ExploreRecommendationsItemsCursor
 * JD-Core Version:    0.6.2
 */