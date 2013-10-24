package com.google.android.music.utils;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.Xfermode;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Build.VERSION;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import com.google.android.music.download.artwork.AlbumIdSink;
import com.google.android.music.download.artwork.ArtDownloadService;
import com.google.android.music.medialist.AutoPlaylist;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent.Playlists.Members;
import com.google.android.music.store.Store;
import com.google.android.music.utils.async.AsyncWorkers;
import com.google.common.base.Strings;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.WeakHashMap;

public class AlbumArtUtils
{
  private static final int[] BUCKET_SIZES = { 130, 256, 512, 1400 };
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.ALBUM_ART);
  private static Bitmap mBackground_DefaultArt;
  private static Bitmap mBackground_DefaultArtistArt;
  private static Bitmap mBackground_RadioArt;
  private static File mCacheDir;
  private static Bitmap mLuckyBadge;
  private static WeakHashMap<Bitmap, MissingArtEntry> mMissingAlbumArt;
  private static Bitmap mMixBadge;
  private static Bitmap mRadioBadge;
  private static final String[] sAlbumIdCols;
  private static final BitmapFactory.Options sBitmapOptionsCache = new BitmapFactory.Options();
  private static volatile MutableBitmapPool sBitmapPool;
  private static int sCacheBucketMediumSize;
  private static int sCacheBucketSmallSize;
  private static int sCacheMediumBucketMaxDimen;
  private static int sCacheSmallBucketMaxDimen;
  private static final Set<ArtCacheKey> sCachedBitmapLocks;
  private static HashMap<Point, Bitmap> sCachedRezinBitmap;
  private static final BitmapFactory.Options sExternalBitmapOptionsCache = new BitmapFactory.Options();
  private static boolean sHprofDumped;
  private static int sMaxAlbumArtSize;
  private static boolean sMaxAlbumArtSizeInitalized;
  private static final HashMap<String, LruCache<ArtCacheKey, Bitmap>> sSizeCache;
  private static final HashMap<String, CacheRequest> sSizeCacheRequests;
  private static int sTotalSizeCacheRequests;

  static
  {
    sCachedBitmapLocks = new HashSet();
    sSizeCache = new HashMap(2);
    sTotalSizeCacheRequests = 0;
    sSizeCacheRequests = new HashMap(4);
    sCachedRezinBitmap = new HashMap();
    mMissingAlbumArt = new WeakHashMap();
    sBitmapPool = null;
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "album_id";
    sAlbumIdCols = arrayOfString;
    sBitmapOptionsCache.inDither = false;
    sBitmapOptionsCache.inPurgeable = true;
    sBitmapOptionsCache.inInputShareable = true;
    sExternalBitmapOptionsCache.inDither = false;
    sExternalBitmapOptionsCache.inPurgeable = true;
    sExternalBitmapOptionsCache.inInputShareable = true;
  }

  private static int albumArtStyleToArtCacheEntryType(int paramInt)
  {
    int i = 9;
    int j = paramInt & 0xF;
    switch (j)
    {
    case 6:
    default:
      String str = "Unsupported faux albumart style: " + j;
      throw new IllegalArgumentException(str);
    case 0:
      i = 2;
    case 7:
    case 9:
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    case 8:
    }
    while (true)
    {
      return i;
      i = 3;
      continue;
      i = 4;
      continue;
      i = 5;
      continue;
      i = 6;
      continue;
      i = 7;
      continue;
      i = 10;
    }
  }

  private static String appendSizeToExternalUrl(String paramString, int paramInt1, int paramInt2)
  {
    int i = paramString.lastIndexOf('/');
    String str1 = paramString.substring(i);
    String str2 = paramString.substring(0, i);
    if ((str1 != null) && (str2 != null))
    {
      StringBuilder localStringBuilder = new StringBuilder().append(str2);
      Object[] arrayOfObject = new Object[2];
      Integer localInteger1 = Integer.valueOf(paramInt1);
      arrayOfObject[0] = localInteger1;
      Integer localInteger2 = Integer.valueOf(paramInt2);
      arrayOfObject[1] = localInteger2;
      String str3 = String.format("/w%d-h%d", arrayOfObject);
      paramString = str3 + str1;
    }
    return paramString;
  }

  private static boolean cacheable(long paramLong, String paramString1, String paramString2)
  {
    if ((paramLong != 0L) || ((isEmpty(paramString1)) && (isEmpty(paramString2))));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private static boolean cacheable(String paramString)
  {
    if (!TextUtils.isEmpty(paramString));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private static Bitmap copyAliasedBitmapHelper(int paramInt1, int paramInt2, Bitmap paramBitmap, boolean paramBoolean)
  {
    Object localObject = null;
    if (paramBitmap != null)
    {
      if (paramBoolean)
      {
        if (Math.abs(paramInt1 - paramInt2) < 5)
          paramBitmap = cropToSquare(paramBitmap);
      }
      else
      {
        MutableBitmapPool localMutableBitmapPool = sBitmapPool;
        Bitmap.Config localConfig = getPreferredConfig();
        Bitmap localBitmap = localMutableBitmapPool.createBitmap(paramInt1, paramInt2, localConfig);
        scaleToFit(paramBitmap, localBitmap);
        localObject = localBitmap;
      }
    }
    else
      return localObject;
    throw new IllegalArgumentException("Only square cropping is supported.");
  }

  private static void copyCachedMissingAlbumIds(AlbumIdSink paramAlbumIdSink, ArtCacheKey paramArtCacheKey)
  {
    if (paramAlbumIdSink == null)
      return;
    synchronized (mMissingAlbumArt)
    {
      MissingArtEntry localMissingArtEntry = (MissingArtEntry)mMissingAlbumArt.get(paramArtCacheKey);
      if (localMissingArtEntry != null)
      {
        Iterator localIterator = localMissingArtEntry.missingAlbumIds.iterator();
        if (localIterator.hasNext())
        {
          Long localLong = (Long)localIterator.next();
          paramAlbumIdSink.report(localLong);
        }
      }
    }
  }

  public static AlbumIdIteratorFactory createAlbumIdIteratorFactoryForContentUri(Context paramContext, final Uri paramUri)
  {
    if (paramUri == null);
    for (Object localObject = null; ; localObject = new AlbumIdIteratorFactory()
    {
      public AlbumArtUtils.AlbumIdIterator createIterator()
      {
        Context localContext = AlbumArtUtils.this;
        Uri localUri = paramUri;
        String[] arrayOfString1 = AlbumArtUtils.sAlbumIdCols;
        String[] arrayOfString2 = null;
        String str = null;
        Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str, false, true);
        return new AlbumArtUtils.CursorAlbumIdIterator(localCursor, 0);
      }
    })
      return localObject;
  }

  private static ArtCacheKey createArtCacheKey(int paramInt, long paramLong)
  {
    int i = albumArtStyleToArtCacheEntryType(paramInt);
    return new ArtCacheKey(i, paramLong);
  }

  public static Bitmap createBitmap(int paramInt1, int paramInt2, Bitmap.Config paramConfig)
  {
    return sBitmapPool.createBitmap(paramInt1, paramInt2, paramConfig);
  }

  private static Bitmap createFauxAlbumArt(Context paramContext, int paramInt1, boolean paramBoolean, long paramLong, int paramInt2, int paramInt3, String paramString1, String paramString2, AlbumIdIteratorFactory paramAlbumIdIteratorFactory, AlbumIdSink paramAlbumIdSink)
  {
    int i = paramInt2;
    int j = paramInt3;
    Bitmap localBitmap1;
    int m;
    int n;
    if ((i != j) && ((paramInt2 < 128) || (paramInt3 < 128)))
    {
      Context localContext = paramContext;
      int k = paramInt1;
      boolean bool = paramBoolean;
      long l = paramLong;
      String str1 = paramString1;
      String str2 = paramString2;
      AlbumIdIteratorFactory localAlbumIdIteratorFactory = paramAlbumIdIteratorFactory;
      AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
      localBitmap1 = createFauxAlbumArt2(localContext, k, bool, l, 128, 128, str1, str2, localAlbumIdIteratorFactory, localAlbumIdSink);
      m = paramInt2;
      n = paramInt3;
    }
    for (Bitmap localBitmap2 = scaleToSize(localBitmap1, m, n); ; localBitmap2 = createFauxAlbumArt2(paramContext, paramInt1, paramBoolean, paramLong, paramInt2, paramInt3, paramString1, paramString2, paramAlbumIdIteratorFactory, paramAlbumIdSink))
      return localBitmap2;
  }

  private static Bitmap createFauxAlbumArt2(Context paramContext, int paramInt1, boolean paramBoolean, long paramLong, int paramInt2, int paramInt3, String paramString1, String paramString2, AlbumIdIteratorFactory paramAlbumIdIteratorFactory, AlbumIdSink paramAlbumIdSink)
  {
    MutableBitmapPool localMutableBitmapPool = sBitmapPool;
    Bitmap.Config localConfig = Bitmap.Config.RGB_565;
    int i = paramInt2;
    int j = paramInt3;
    Bitmap localBitmap = localMutableBitmapPool.createBitmap(i, j, localConfig);
    Canvas localCanvas = new Canvas(localBitmap);
    Context localContext = paramContext;
    int k = paramInt1;
    boolean bool = paramBoolean;
    long l = paramLong;
    int m = paramInt2;
    int n = paramInt3;
    String str1 = paramString1;
    String str2 = paramString2;
    AlbumIdIteratorFactory localAlbumIdIteratorFactory = paramAlbumIdIteratorFactory;
    AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
    drawFauxAlbumArt(localCanvas, localContext, k, bool, l, m, n, str1, str2, localAlbumIdIteratorFactory, localAlbumIdSink);
    return localBitmap;
  }

  private static Bitmap cropToSquare(Bitmap paramBitmap)
  {
    if (paramBitmap == null)
      paramBitmap = null;
    int i;
    int j;
    do
    {
      return paramBitmap;
      i = paramBitmap.getWidth();
      j = paramBitmap.getHeight();
    }
    while (i == j);
    int k = paramBitmap.getWidth();
    int m = paramBitmap.getHeight();
    int i2;
    int i3;
    int i4;
    if (k > m)
    {
      int n = paramBitmap.getWidth() / 2;
      int i1 = paramBitmap.getHeight() / 2;
      i2 = n - i1;
      i3 = paramBitmap.getHeight();
      i4 = paramBitmap.getHeight();
    }
    int i7;
    int i8;
    int i9;
    for (Bitmap localBitmap = Bitmap.createBitmap(paramBitmap, i2, 0, i3, i4); ; localBitmap = Bitmap.createBitmap(paramBitmap, 0, i7, i8, i9))
    {
      paramBitmap = localBitmap;
      break;
      int i5 = paramBitmap.getHeight() / 2;
      int i6 = paramBitmap.getWidth() / 2;
      i7 = i5 - i6;
      i8 = paramBitmap.getWidth();
      i9 = paramBitmap.getWidth();
    }
  }

  public static void drawFauxAlbumArt(Canvas paramCanvas, Context paramContext, int paramInt1, boolean paramBoolean, long paramLong, int paramInt2, int paramInt3, String paramString1, String paramString2, AlbumIdIteratorFactory paramAlbumIdIteratorFactory, AlbumIdSink paramAlbumIdSink)
  {
    int i = paramInt1 & 0xF;
    switch (i)
    {
    case 6:
    default:
      if (i != 8)
        break;
    case 1:
    case 2:
    case 4:
    case 5:
    case 3:
    case 0:
    case 7:
    case 8:
    }
    for (boolean bool1 = true; ; bool1 = false)
    {
      Canvas localCanvas1 = paramCanvas;
      Context localContext1 = paramContext;
      int j = paramInt1;
      boolean bool2 = paramBoolean;
      long l1 = paramLong;
      int k = paramInt2;
      int m = paramInt3;
      String str1 = paramString1;
      String str2 = paramString2;
      drawModernFauxAlbumArt(localCanvas1, localContext1, j, bool2, l1, k, m, str1, str2, bool1);
      return;
      Canvas localCanvas2 = paramCanvas;
      Context localContext2 = paramContext;
      int n = paramInt1;
      long l2 = paramLong;
      int i1 = paramInt2;
      int i2 = paramInt3;
      AlbumIdIteratorFactory localAlbumIdIteratorFactory1 = paramAlbumIdIteratorFactory;
      AlbumIdSink localAlbumIdSink1 = paramAlbumIdSink;
      if (!drawFauxPlaylistArt(localCanvas2, localContext2, n, l2, i1, i2, localAlbumIdIteratorFactory1, localAlbumIdSink1))
        break;
      return;
      Canvas localCanvas3 = paramCanvas;
      Context localContext3 = paramContext;
      int i3 = paramInt1;
      long l3 = paramLong;
      int i4 = paramInt2;
      int i5 = paramInt3;
      AlbumIdIteratorFactory localAlbumIdIteratorFactory2 = paramAlbumIdIteratorFactory;
      AlbumIdSink localAlbumIdSink2 = paramAlbumIdSink;
      if (!drawFauxPlaylistSuggestedMixArt(localCanvas3, localContext3, i3, l3, i4, i5, localAlbumIdIteratorFactory2, localAlbumIdSink2))
        break;
      return;
      if (paramAlbumIdSink == null)
        break;
      Long localLong = Long.valueOf(paramLong);
      paramAlbumIdSink.report(localLong);
      break;
      Canvas localCanvas4 = paramCanvas;
      Context localContext4 = paramContext;
      int i6 = paramInt1;
      long l4 = paramLong;
      int i7 = paramInt2;
      int i8 = paramInt3;
      AlbumIdIteratorFactory localAlbumIdIteratorFactory3 = paramAlbumIdIteratorFactory;
      AlbumIdSink localAlbumIdSink3 = paramAlbumIdSink;
      if (drawFauxStackedLandscapeArt(localCanvas4, localContext4, i6, l4, i7, i8, localAlbumIdIteratorFactory3, localAlbumIdSink3))
        return;
      Canvas localCanvas5 = paramCanvas;
      Context localContext5 = paramContext;
      int i9 = paramInt1;
      long l5 = paramLong;
      int i10 = paramInt2;
      int i11 = paramInt3;
      AlbumIdIteratorFactory localAlbumIdIteratorFactory4 = paramAlbumIdIteratorFactory;
      AlbumIdSink localAlbumIdSink4 = paramAlbumIdSink;
      if (!drawFauxStackedLandscapeArt(localCanvas5, localContext5, i9, l5, i10, i11, localAlbumIdIteratorFactory4, localAlbumIdSink4))
        break;
      return;
    }
  }

  public static boolean drawFauxPlaylistArt(Canvas paramCanvas, Context paramContext, int paramInt1, int paramInt2, int paramInt3, List<Bitmap> paramList)
  {
    try
    {
      arrayOfInt = new int[4];
      Object localObject1 = null;
      while (true)
      {
        int j = arrayOfInt.length;
        if (localObject1 >= j)
          break;
        arrayOfInt[localObject1] = localObject1;
        int i = localObject1 + 1;
      }
      k = paramList.size();
      if (k == 0);
      while (true)
      {
        m = 2;
        int n = 2;
        i1 = paramContext.getResources().getDimensionPixelSize(2131558447);
        int i2 = paramInt2 / m - i1;
        i3 = paramInt3 / n - i1;
        paramCanvas.drawARGB(255, 255, 255, 255);
        Paint localPaint1 = new Paint();
        localPaint1.setFilterBitmap(true);
        Rect localRect1 = new Rect();
        RectF localRectF1 = new RectF();
        i4 = 0;
        i5 = 0;
        if (i4 >= n)
          break label529;
        i6 = 0;
        int i13;
        for (int i7 = 0; ; i7 = i13)
        {
          if (i6 >= m)
            break label500;
          if (k != 0)
            break;
          localBitmap2 = getBackgroundBitmapForStyle(paramContext, paramInt1);
          int i8 = localBitmap2.getWidth();
          int i9 = localBitmap2.getHeight();
          Rect localRect2 = localRect1;
          int i10 = 0;
          int i11 = 0;
          localRect2.set(i10, i11, i8, i9);
          float f3 = i7;
          float f4 = i5;
          if (i6 >= 1)
            break label485;
          f5 = i7 + i2;
          if (i4 >= 1)
            break label492;
          float f1 = i5 + i3;
          RectF localRectF2 = localRectF1;
          float f6 = f3;
          float f7 = f4;
          localRectF2.set(f6, f7, f5, f1);
          Canvas localCanvas = paramCanvas;
          Rect localRect3 = localRect1;
          RectF localRectF3 = localRectF1;
          Paint localPaint2 = localPaint1;
          localCanvas.drawBitmap(localBitmap2, localRect3, localRectF3, localPaint2);
          int i12 = i1 * 2 + i2;
          i13 = i7 + i12;
          i6 += 1;
        }
        if (k != 1)
          break;
        arrayOfInt[3] = 0;
        arrayOfInt[2] = 0;
        arrayOfInt[1] = 0;
      }
    }
    finally
    {
      while (true)
      {
        int[] arrayOfInt;
        int k;
        int m;
        int i1;
        int i3;
        int i4;
        int i5;
        int i6;
        Bitmap localBitmap2;
        float f5;
        Iterator localIterator1 = paramList.iterator();
        while (localIterator1.hasNext())
        {
          Bitmap localBitmap3 = (Bitmap)localIterator1.next();
          sBitmapPool.recycleBitmap(localBitmap3);
        }
        Bitmap localBitmap1;
        if (k == 2)
        {
          localBitmap1 = null;
          f5 = 0.0F;
          arrayOfInt[localBitmap1] = f5;
          arrayOfInt[2] = 1;
          arrayOfInt[1] = 1;
        }
        else if (k == 3)
        {
          arrayOfInt[3] = 0;
          continue;
          int i14 = i4 * m + i6;
          int i15 = arrayOfInt[i14];
          localBitmap1 = (Bitmap)paramList.get(i15);
          localBitmap2 = localBitmap1;
          continue;
          label485: f5 = paramInt2;
          continue;
          label492: float f2 = paramInt3;
          continue;
          label500: int i16 = i1 * 2 + i3;
          int i17 = i5 + i16;
          i4 += 1;
          i5 = i17;
        }
      }
      label529: Iterator localIterator2 = paramList.iterator();
      while (localIterator2.hasNext())
      {
        Bitmap localBitmap4 = (Bitmap)localIterator2.next();
        sBitmapPool.recycleBitmap(localBitmap4);
      }
    }
    return true;
  }

  private static boolean drawFauxPlaylistArt(Canvas paramCanvas, Context paramContext, int paramInt1, long paramLong, int paramInt2, int paramInt3, AlbumIdIteratorFactory paramAlbumIdIteratorFactory, AlbumIdSink paramAlbumIdSink)
  {
    Context localContext1 = paramContext;
    int i = paramInt1;
    long l = paramLong;
    int j = paramInt2;
    int k = paramInt3;
    AlbumIdIteratorFactory localAlbumIdIteratorFactory = paramAlbumIdIteratorFactory;
    AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
    List localList = getUniqueArt(localContext1, i, l, 4, j, k, localAlbumIdIteratorFactory, localAlbumIdSink);
    Canvas localCanvas = paramCanvas;
    Context localContext2 = paramContext;
    int m = paramInt1;
    int n = paramInt2;
    int i1 = paramInt3;
    return drawFauxPlaylistArt(localCanvas, localContext2, m, n, i1, localList);
  }

  private static boolean drawFauxPlaylistSuggestedMixArt(Canvas paramCanvas, Context paramContext, int paramInt1, long paramLong, int paramInt2, int paramInt3, AlbumIdIteratorFactory paramAlbumIdIteratorFactory, AlbumIdSink paramAlbumIdSink)
  {
    boolean bool1 = drawFauxPlaylistArt(paramCanvas, paramContext, paramInt1, paramLong, paramInt2, paramInt3, paramAlbumIdIteratorFactory, paramAlbumIdSink);
    boolean bool2 = drawFauxRadioArtOverlay(paramContext, paramCanvas, paramInt2, paramInt3);
    return true;
  }

  public static boolean drawFauxRadioArtOverlay(Context paramContext, Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    Canvas localCanvas = new Canvas(paramBitmap);
    return drawFauxRadioArtOverlay(paramContext, localCanvas, paramInt1, paramInt2);
  }

  private static boolean drawFauxRadioArtOverlay(Context paramContext, Canvas paramCanvas, int paramInt1, int paramInt2)
  {
    Paint localPaint = new Paint();
    localPaint.setFilterBitmap(true);
    if (mBackground_RadioArt == null)
      mBackground_RadioArt = getBitmap(paramContext, 2130837606);
    Bitmap localBitmap1 = mBackground_RadioArt;
    Rect localRect1 = new Rect(0, 0, paramInt1, paramInt2);
    paramCanvas.drawBitmap(localBitmap1, null, localRect1, localPaint);
    Object localObject1 = new Object();
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, localObject1);
    while (true)
    {
      try
      {
        boolean bool1 = localMusicPreferences.isNautilusEnabled();
        boolean bool2 = bool1;
        MusicPreferences.releaseMusicPreferences(localObject1);
        if (bool2)
        {
          if (mRadioBadge == null)
            mRadioBadge = getBitmap(paramContext, 2130837612);
          localBitmap2 = mRadioBadge;
          int i = localBitmap2.getHeight();
          int j = localBitmap2.getWidth();
          int k = (paramInt2 - i) / 2;
          int m = (paramInt1 - j) / 2;
          int n = j + m;
          int i1 = i + k;
          Rect localRect2 = new Rect(m, k, n, i1);
          paramCanvas.drawBitmap(localBitmap2, null, localRect2, localPaint);
          return true;
        }
      }
      finally
      {
        MusicPreferences.releaseMusicPreferences(localObject1);
      }
      if (mMixBadge == null)
        mMixBadge = getBitmap(paramContext, 2130837611);
      Bitmap localBitmap2 = mMixBadge;
    }
  }

  private static boolean drawFauxStackedLandscapeArt(Canvas paramCanvas, Context paramContext, int paramInt1, long paramLong, int paramInt2, int paramInt3, AlbumIdIteratorFactory paramAlbumIdIteratorFactory, AlbumIdSink paramAlbumIdSink)
  {
    int i = Math.max(paramInt2, paramInt3);
    Context localContext = paramContext;
    int j = paramInt1;
    long l = paramLong;
    int k = i;
    AlbumIdIteratorFactory localAlbumIdIteratorFactory = paramAlbumIdIteratorFactory;
    AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
    Object localObject1 = getUniqueArt(localContext, j, l, 5, i, k, localAlbumIdIteratorFactory, localAlbumIdSink);
    while (true)
    {
      try
      {
        int m = ((List)localObject1).size();
        int n = m;
        Iterator localIterator1;
        if (n < 1)
        {
          localIterator1 = null;
          localObject1 = ((List)localObject1).iterator();
          if (((Iterator)localObject1).hasNext())
          {
            Bitmap localBitmap1 = (Bitmap)((Iterator)localObject1).next();
            sBitmapPool.recycleBitmap(localBitmap1);
            continue;
          }
          localIterator2 = localIterator1;
          return localIterator2;
        }
        Paint localPaint = new Paint();
        localPaint.setFilterBitmap(true);
        Rect localRect = new Rect();
        RectF localRectF = new RectF();
        if (n == 1)
        {
          Bitmap localBitmap2 = (Bitmap)((List)localObject1).get(0);
          int i1 = localBitmap2.getWidth();
          int i2 = localBitmap2.getHeight() / 2;
          localRect.set(0, 0, i1, i2);
          float f1 = paramInt2;
          float f2 = paramInt3;
          localRectF.set(0.0F, 0.0F, f1, f2);
          paramCanvas.drawBitmap(localBitmap2, localRect, localRectF, localPaint);
          localIterator1 = ((List)localObject1).iterator();
          if (localIterator1.hasNext())
          {
            Bitmap localBitmap3 = (Bitmap)localIterator1.next();
            sBitmapPool.recycleBitmap(localBitmap3);
            continue;
          }
        }
        else
        {
          int i3 = null;
          Bitmap localBitmap5 = getBitmap(paramContext, i3);
          int i4 = paramContext.getResources().getDimensionPixelSize(2131558458);
          int i5 = paramInt2 - paramInt3;
          int i6 = n + -1;
          int i7 = i5 / i6;
          int i8 = 0;
          Bitmap localBitmap4;
          int i9;
          int i10;
          if (i8 < n)
          {
            localBitmap4 = (Bitmap)((List)localObject1).get(i8);
            i9 = localBitmap4.getWidth();
            i10 = localBitmap4.getHeight();
            if (i8 != 0)
              continue;
            localRect.set(0, 0, i9, i10);
            float f3 = paramInt3;
            float f4 = paramInt3;
            localRectF.set(0.0F, 0.0F, f3, f4);
            paramCanvas.drawBitmap(localBitmap4, localRect, localRectF, localPaint);
            i8 += 1;
            continue;
          }
          continue;
          int i11 = n + -1;
          int i12 = i9 / i11;
          int i13 = i9 - i12;
          localRect.set(i13, 0, i9, i10);
          int i14 = i7 * i8 + paramInt3;
          int i15 = i14 - i7;
          float f5 = i15;
          float f6 = i14;
          float f7 = paramInt3;
          localRectF.set(f5, 0.0F, f6, f7);
          paramCanvas.drawBitmap(localBitmap4, localRect, localRectF, localPaint);
          float f8 = i15;
          float f9 = i15 + i4;
          float f10 = paramInt3;
          localRectF.set(f8, 0.0F, f9, f10);
          paramCanvas.drawBitmap(localBitmap5, null, localRectF, localPaint);
          continue;
        }
      }
      finally
      {
        Iterator localIterator3 = ((List)localObject1).iterator();
        if (localIterator3.hasNext())
        {
          Bitmap localBitmap6 = (Bitmap)localIterator3.next();
          sBitmapPool.recycleBitmap(localBitmap6);
          continue;
        }
      }
      Iterator localIterator2 = null;
    }
  }

  public static void drawImFeelingLuckyRadioArtOverlay(Context paramContext, Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    Canvas localCanvas = new Canvas(paramBitmap);
    drawImFeelingLuckyRadioArtOverlay(paramContext, localCanvas, paramInt1, paramInt2);
  }

  private static void drawImFeelingLuckyRadioArtOverlay(Context paramContext, Canvas paramCanvas, int paramInt1, int paramInt2)
  {
    Paint localPaint = new Paint();
    localPaint.setFilterBitmap(true);
    if (mLuckyBadge == null)
      mLuckyBadge = getBitmap(paramContext, 2130837610);
    int i = mLuckyBadge.getWidth();
    int j = mLuckyBadge.getHeight();
    int k = (paramInt2 - j) / 2;
    int m = (paramInt1 - i) / 2;
    int n = i + m;
    int i1 = j + k;
    Rect localRect = new Rect(m, k, n, i1);
    Bitmap localBitmap = mLuckyBadge;
    paramCanvas.drawBitmap(localBitmap, null, localRect, localPaint);
  }

  private static void drawModernFauxAlbumArt(Canvas paramCanvas, Context paramContext, int paramInt1, boolean paramBoolean1, long paramLong, int paramInt2, int paramInt3, String paramString1, String paramString2, boolean paramBoolean2)
  {
    long l = paramLong;
    Random localRandom = new Random(l);
    int i;
    int j;
    if ((paramInt1 & 0x10) == 16)
    {
      i = 1;
      j = paramInt1 & 0xF;
      if ((paramBoolean1) && (i == 0))
      {
        if (j != 1)
          break label314;
        paramString2 = paramContext.getString(2131230958);
      }
    }
    while (true)
    {
      if (paramBoolean2)
      {
        paramInt3 = Math.max(paramInt2, paramInt3);
        paramInt2 = paramInt3;
      }
      Paint localPaint = new Paint();
      Bitmap localBitmap = getBackgroundBitmapForStyle(paramContext, j);
      localPaint.setFilterBitmap(true);
      int k = paramInt2;
      int m = paramInt3;
      Rect localRect = new Rect(0, 0, k, m);
      paramCanvas.drawBitmap(localBitmap, null, localRect, localPaint);
      if (!paramBoolean1)
        return;
      int n = paramInt2 * 20 / 336;
      int i1 = paramInt2 * 296 / 336;
      if (paramString1 != null)
      {
        int i2 = paramInt3 * 28 / 336;
        int i3 = paramInt2 * 30 / 336;
        String str1 = paramString1.toUpperCase();
        Canvas localCanvas1 = paramCanvas;
        int i4 = n;
        renderFauxLabel(localCanvas1, i1, n, i4, i2, false, str1, -13421773, i3, false, 0, 0, false, 0, 0, 0.0F);
      }
      if (paramString2 == null)
        return;
      int i5 = paramInt3 * 55 / 336;
      int i6 = paramInt3 * 22 / 336;
      int i7 = paramInt2 * 20 / 336;
      Canvas localCanvas2 = paramCanvas;
      String str2 = paramString2;
      renderFauxLabel(localCanvas2, i1, n, i5, i6, false, str2, -8947849, i7, false, 0, 0, false, 0, 0, 0.0F);
      return;
      i = 0;
      break;
      label314: if (j == 4)
        paramString2 = paramContext.getString(2131230961);
      else if (j == 2)
        paramString2 = paramContext.getString(2131230959);
      else if (j == 3)
        paramString1 = paramContext.getString(2131230960);
      else if (j == 5)
        paramString2 = paramContext.getString(2131230957);
    }
  }

  private static long estimatedArtFileSize(int paramInt)
  {
    long l1 = paramInt;
    long l2 = 102400L * l1;
    long l3 = paramInt;
    return l2 * l3 / 360000L;
  }

  private static int findClosest(int[] paramArrayOfInt, int paramInt)
  {
    int i = 0;
    int j = Math.abs(paramArrayOfInt[0] - paramInt);
    int k = 1;
    while (true)
    {
      int m = paramArrayOfInt.length;
      if (k >= m)
        break;
      int n = Math.abs(paramArrayOfInt[k] - paramInt);
      if (n < j)
      {
        j = n;
        i = k;
      }
      k += 1;
    }
    return i;
  }

  public static Bitmap getArtwork(Context paramContext, long paramLong, int paramInt1, int paramInt2, boolean paramBoolean1, String paramString1, String paramString2, AlbumIdSink paramAlbumIdSink, boolean paramBoolean2)
  {
    Context localContext = paramContext;
    long l = paramLong;
    int i = paramInt1;
    int j = paramInt2;
    boolean bool1 = paramBoolean1;
    String str1 = paramString1;
    String str2 = paramString2;
    AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
    boolean bool2 = paramBoolean2;
    return getArtwork(localContext, l, i, j, bool1, str1, str2, localAlbumIdSink, true, bool2);
  }

  private static Bitmap getArtwork(Context paramContext, long paramLong, int paramInt1, int paramInt2, boolean paramBoolean1, String paramString1, String paramString2, AlbumIdSink paramAlbumIdSink, boolean paramBoolean2, boolean paramBoolean3)
  {
    MusicUtils.checkMainThread(paramContext, "Getting album art on main thread");
    Bitmap localBitmap;
    if (paramLong < 0L)
      if (paramBoolean1)
      {
        Context localContext1 = paramContext;
        long l1 = paramLong;
        int i = paramInt1;
        int j = paramInt2;
        String str1 = paramString1;
        String str2 = paramString2;
        AlbumIdSink localAlbumIdSink1 = paramAlbumIdSink;
        boolean bool1 = paramBoolean3;
        localBitmap = getDefaultArtwork(localContext1, true, l1, i, j, str1, str2, localAlbumIdSink1, bool1);
      }
    while (true)
    {
      return localBitmap;
      localBitmap = null;
      continue;
      Context localContext2 = paramContext;
      long l2 = paramLong;
      int k = paramInt1;
      int m = paramInt2;
      boolean bool2 = paramBoolean2;
      localBitmap = resolveArtwork(localContext2, l2, k, m, bool2);
      if (localBitmap == null)
        if (paramBoolean1)
        {
          Context localContext3 = paramContext;
          long l3 = paramLong;
          int n = paramInt1;
          int i1 = paramInt2;
          String str3 = paramString1;
          String str4 = paramString2;
          AlbumIdSink localAlbumIdSink2 = paramAlbumIdSink;
          boolean bool3 = paramBoolean3;
          localBitmap = getDefaultArtwork(localContext3, true, l3, n, i1, str3, str4, localAlbumIdSink2, bool3);
        }
        else
        {
          localBitmap = null;
        }
    }
  }

  public static Bitmap getArtworkFromUrl(Context paramContext, String paramString, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5)
  {
    StringBuilder localStringBuilder;
    String str1;
    if (paramBoolean2)
      if (!paramString.contains("="))
      {
        localStringBuilder = new StringBuilder().append(paramString).append('=');
        str1 = paramContext.getResources().getString(2131231238);
      }
    for (paramString = str1; ; paramString = appendSizeToExternalUrl(paramString, paramInt1, paramInt2))
    {
      Context localContext = paramContext;
      String str2 = paramString;
      int i = paramInt1;
      int j = paramInt2;
      boolean bool1 = paramBoolean3;
      boolean bool2 = paramBoolean4;
      boolean bool3 = paramBoolean5;
      return getExternalAlbumArtBitmap(localContext, str2, i, j, bool1, bool2, bool3);
    }
  }

  private static Bitmap getBackgroundBitmapForStyle(Context paramContext, int paramInt)
  {
    switch (paramInt)
    {
    case 6:
    default:
      throw new IllegalArgumentException("unknown style");
    case 7:
      if (mBackground_DefaultArtistArt == null)
        mBackground_DefaultArtistArt = getBitmap(paramContext, 2130837598);
      break;
    case 0:
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
    case 8:
    case 9:
    }
    for (Bitmap localBitmap = mBackground_DefaultArtistArt; ; localBitmap = mBackground_DefaultArt)
    {
      return localBitmap;
      if (mBackground_DefaultArt == null)
        mBackground_DefaultArt = getBitmap(paramContext, 2130837597);
    }
  }

  private static Bitmap getBitmap(Context paramContext, int paramInt)
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    Bitmap.Config localConfig = Bitmap.Config.RGB_565;
    localOptions.inPreferredConfig = localConfig;
    return BitmapFactory.decodeStream(paramContext.getResources().openRawResource(paramInt), null, localOptions);
  }

  private static Bitmap getBitmapFromCache(LruCache<ArtCacheKey, Bitmap> paramLruCache, ArtCacheKey paramArtCacheKey)
  {
    Bitmap localBitmap = null;
    if (paramLruCache != null);
    try
    {
      localBitmap = (Bitmap)paramLruCache.get(paramArtCacheKey);
      if ((localBitmap != null) && (localBitmap.isRecycled()))
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Found a recycled bitmap for artwork: ");
        String str1 = paramArtCacheKey.toString();
        String str2 = str1;
        int i = Log.w("AlbumArtUtils", str2);
        paramLruCache.remove(paramArtCacheKey);
        localBitmap = null;
      }
      return localBitmap;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private static Bitmap getBitmapFromCacheOrAcquireLock(LruCache<ArtCacheKey, Bitmap> paramLruCache, ArtCacheKey paramArtCacheKey)
  {
    Bitmap localBitmap;
    if (paramLruCache == null)
      localBitmap = null;
    while (true)
    {
      return localBitmap;
      synchronized (sCachedBitmapLocks)
      {
        while (true)
        {
          boolean bool1 = sCachedBitmapLocks.contains(paramArtCacheKey);
          if (!bool1)
            break;
          try
          {
            sCachedBitmapLocks.wait();
          }
          catch (InterruptedException localInterruptedException)
          {
          }
        }
        localBitmap = getBitmapFromCache(paramLruCache, paramArtCacheKey);
        if (localBitmap == null)
          boolean bool2 = sCachedBitmapLocks.add(paramArtCacheKey);
      }
    }
  }

  public static Bitmap getBitmapFromDisk(Context paramContext, String paramString)
  {
    Context localContext = paramContext;
    String str = paramString;
    int i = 0;
    boolean bool = false;
    return resolveArtworkRaw(localContext, str, 0, i, null, bool);
  }

  private static LruCache<ArtCacheKey, Bitmap> getCache(Context paramContext, int paramInt1, int paramInt2, String paramString)
  {
    LruCache localLruCache = null;
    if (!"LARGE".equals(paramString));
    synchronized (sSizeCache)
    {
      localLruCache = (LruCache)sSizeCache.get(paramString);
      if (localLruCache == null)
      {
        if (!paramString.equals("SMALL"))
          break label86;
        i = sCacheBucketSmallSize;
        localLruCache = new LruCache(i);
      }
      label86: 
      while (!paramString.equals("MEDIUM"))
      {
        int i;
        if (localLruCache != null)
          Object localObject1 = sSizeCache.put(paramString, localLruCache);
        return localLruCache;
      }
      int j = sCacheBucketMediumSize;
      localLruCache = new LruCache(j);
    }
  }

  public static Bitmap getCachedBitmap(Context paramContext, long paramLong, String paramString1, int paramInt1, int paramInt2, String paramString2, String paramString3, AlbumIdSink paramAlbumIdSink, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    Bitmap localBitmap1;
    if ((paramLong < 0L) && (paramString1 == null))
      localBitmap1 = null;
    while (true)
    {
      return localBitmap1;
      long l1 = paramLong;
      String str1 = paramString2;
      String str2 = paramString3;
      if (!cacheable(l1, str1, str2))
      {
        Context localContext1 = paramContext;
        long l2 = paramLong;
        String str3 = paramString1;
        int i = paramInt1;
        int j = paramInt2;
        String str4 = paramString2;
        String str5 = paramString3;
        AlbumIdSink localAlbumIdSink1 = paramAlbumIdSink;
        boolean bool1 = paramBoolean2;
        boolean bool2 = paramBoolean3;
        localBitmap1 = resolveArtworkImp(localContext1, l2, str3, i, j, str4, str5, localAlbumIdSink1, bool1, bool2);
      }
      else
      {
        localBitmap1 = null;
        Context localContext2 = paramContext;
        int k = paramInt1;
        int m = paramInt2;
        String str6 = getSizeKey(localContext2, k, m);
        Context localContext3 = paramContext;
        int n = paramInt1;
        int i1 = paramInt2;
        String str7 = str6;
        LruCache localLruCache = getCache(localContext3, n, i1, str7);
        if (localLruCache != null)
        {
          Context localContext4 = paramContext;
          long l3 = paramLong;
          String str8 = paramString1;
          int i2 = paramInt1;
          int i3 = paramInt2;
          String str9 = paramString2;
          String str10 = paramString3;
          AlbumIdSink localAlbumIdSink2 = paramAlbumIdSink;
          boolean bool3 = paramBoolean1;
          boolean bool4 = paramBoolean2;
          localBitmap1 = getCachedBitmapImpl(localContext4, l3, str8, i2, i3, str9, str10, localAlbumIdSink2, bool3, bool4, true, str6, localLruCache);
          if (!paramBoolean3)
          {
            int i4 = paramInt1;
            int i5 = paramInt2;
            Bitmap localBitmap2 = localBitmap1;
            localBitmap1 = copyAliasedBitmapHelper(i4, i5, localBitmap2, false);
          }
        }
        else if (paramBoolean1)
        {
          Context localContext5 = paramContext;
          long l4 = paramLong;
          String str11 = paramString1;
          int i6 = paramInt1;
          int i7 = paramInt2;
          String str12 = paramString2;
          String str13 = paramString3;
          AlbumIdSink localAlbumIdSink3 = paramAlbumIdSink;
          boolean bool5 = paramBoolean2;
          boolean bool6 = paramBoolean3;
          localBitmap1 = resolveArtworkImp(localContext5, l4, str11, i6, i7, str12, str13, localAlbumIdSink3, bool5, bool6);
        }
      }
    }
  }

  private static Bitmap getCachedBitmapImpl(Context paramContext, long paramLong, String paramString1, int paramInt1, int paramInt2, String paramString2, String paramString3, AlbumIdSink paramAlbumIdSink, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, String paramString4, LruCache<ArtCacheKey, Bitmap> paramLruCache)
  {
    long l1 = paramLong;
    ArtCacheKey localArtCacheKey = new ArtCacheKey(1, l1);
    Bitmap localBitmap;
    if (paramBoolean1)
    {
      localBitmap = getBitmapFromCacheOrAcquireLock(paramLruCache, localArtCacheKey);
      if (localBitmap != null)
        bool1 = true;
    }
    while (true)
    {
      Object localObject1 = null;
      if (localBitmap == null);
      try
      {
        AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
        AlbumIdTap localAlbumIdTap = new AlbumIdTap(localAlbumIdSink);
        Context localContext = paramContext;
        long l2 = paramLong;
        String str1 = paramString1;
        int i = paramInt1;
        int j = paramInt2;
        String str2 = paramString2;
        String str3 = paramString3;
        boolean bool2 = paramBoolean2;
        boolean bool3 = paramBoolean3;
        localBitmap = resolveArtworkImp(localContext, l2, str1, i, j, str2, str3, localAlbumIdTap, bool2, bool3);
        Set localSet1 = localAlbumIdTap.extractIds();
        Set localSet2 = localSet1;
        LruCache<ArtCacheKey, Bitmap> localLruCache1 = paramLruCache;
        Set localSet3;
        return localBitmap;
        bool1 = false;
      }
      finally
      {
        LruCache<ArtCacheKey, Bitmap> localLruCache2 = paramLruCache;
        Object localObject3 = localObject1;
        putBitmapInCacheAndReleaseLock(localLruCache2, localBitmap, localObject3, localArtCacheKey);
      }
    }
    if (localBitmap != null);
    for (boolean bool1 = true; ; bool1 = false)
      break;
  }

  public static Bitmap getCachedFauxAlbumArt(Context paramContext, int paramInt1, long paramLong, int paramInt2, int paramInt3, AlbumIdSink paramAlbumIdSink, boolean paramBoolean)
  {
    int i = paramInt1 & 0xF;
    Bitmap localBitmap = null;
    ArtCacheKey localArtCacheKey = createArtCacheKey(i, paramLong);
    String str = getSizeKey(paramContext, paramInt2, paramInt3);
    LruCache localLruCache = getCache(paramContext, paramInt2, paramInt3, str);
    if (localLruCache != null)
    {
      localBitmap = getBitmapFromCache(localLruCache, localArtCacheKey);
      if (localBitmap != null)
        copyCachedMissingAlbumIds(paramAlbumIdSink, localArtCacheKey);
      if (!paramBoolean)
        localBitmap = copyAliasedBitmapHelper(paramInt2, paramInt3, localBitmap, false);
    }
    return localBitmap;
  }

  public static Bitmap getCachedMultiImageComposite(Context paramContext, String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    Bitmap localBitmap1;
    if (TextUtils.isEmpty(paramString))
    {
      localBitmap1 = null;
      return localBitmap1;
    }
    String str = getSizeKey(paramContext, paramInt1, paramInt2);
    LruCache localLruCache = getCache(paramContext, paramInt1, paramInt2, str);
    if (paramBoolean);
    for (int i = 19; ; i = 9)
    {
      long l = Store.generateId(paramString);
      ArtCacheKey localArtCacheKey = new ArtCacheKey(i, l);
      Bitmap localBitmap2 = getBitmapFromCache(localLruCache, localArtCacheKey);
      localBitmap1 = copyAliasedBitmapHelper(paramInt1, paramInt2, localBitmap2, true);
      break;
    }
  }

  private static int getCapHeight(TextPaint paramTextPaint)
  {
    Rect localRect = new Rect();
    paramTextPaint.getTextBounds("I", 0, 1, localRect);
    return -localRect.top;
  }

  private static Bitmap.Config getConfigOrDefault(Bitmap paramBitmap, Bitmap.Config paramConfig)
  {
    Bitmap.Config localConfig = paramBitmap.getConfig();
    if (localConfig == null)
      localConfig = paramConfig;
    return localConfig;
  }

  public static Bitmap getDefaultArtwork(Context paramContext, boolean paramBoolean1, long paramLong, int paramInt1, int paramInt2, String paramString1, String paramString2, AlbumIdSink paramAlbumIdSink, boolean paramBoolean2)
  {
    Context localContext = paramContext;
    boolean bool1 = paramBoolean1;
    long l = paramLong;
    int i = paramInt1;
    int j = paramInt2;
    String str1 = paramString1;
    String str2 = paramString2;
    AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
    boolean bool2 = paramBoolean2;
    return getFauxAlbumArt(localContext, 0, bool1, l, i, j, str1, str2, null, localAlbumIdSink, bool2);
  }

  public static Bitmap getExternalAlbumArtBitmap(Context paramContext, String paramString, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    if (LOGV)
    {
      String str1 = "getExternalAlbumArtBitmap: albumArtUrl=" + paramString + " w=" + paramInt1 + " h=" + paramInt2;
      int i = Log.d("AlbumArtUtils", str1);
    }
    if (paramString == null);
    long l;
    Context localContext;
    String str3;
    int k;
    int m;
    boolean bool1;
    boolean bool2;
    boolean bool3;
    for (Bitmap localBitmap = null; ; localBitmap = getCachedBitmap(localContext, l, str3, k, m, null, null, null, bool1, bool2, bool3))
    {
      return localBitmap;
      l = makeDefaultArtId(paramString);
      if (LOGV)
      {
        String str2 = "getExternalAlbumArtBitmap: album_id=" + l;
        int j = Log.d("AlbumArtUtils", str2);
      }
      localContext = paramContext;
      str3 = paramString;
      k = paramInt1;
      m = paramInt2;
      bool1 = paramBoolean1;
      bool2 = paramBoolean2;
      bool3 = paramBoolean3;
    }
  }

  public static Bitmap getFauxAlbumArt(Context paramContext, int paramInt1, boolean paramBoolean1, long paramLong, int paramInt2, int paramInt3, String paramString1, String paramString2, AlbumIdIteratorFactory paramAlbumIdIteratorFactory, AlbumIdSink paramAlbumIdSink, boolean paramBoolean2)
  {
    Bitmap localBitmap1 = null;
    int i = paramInt1 & 0xF;
    if (i == 0)
    {
      long l1 = paramLong;
      String str1 = paramString1;
      String str2 = paramString2;
      if (!cacheable(l1, str1, str2));
    }
    else
    {
      int j = i;
      long l2 = paramLong;
      ArtCacheKey localArtCacheKey = createArtCacheKey(j, l2);
      Context localContext1 = paramContext;
      int k = paramInt2;
      int m = paramInt3;
      String str3 = getSizeKey(localContext1, k, m);
      Context localContext2 = paramContext;
      int n = paramInt2;
      int i1 = paramInt3;
      String str4 = str3;
      LruCache localLruCache = getCache(localContext2, n, i1, str4);
      if (localLruCache != null)
      {
        Context localContext3 = paramContext;
        int i2 = i;
        boolean bool = paramBoolean1;
        long l3 = paramLong;
        int i3 = paramInt2;
        int i4 = paramInt3;
        String str5 = paramString1;
        String str6 = paramString2;
        AlbumIdIteratorFactory localAlbumIdIteratorFactory = paramAlbumIdIteratorFactory;
        AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
        localBitmap1 = getFauxAlbumArtImpl(localContext3, i2, bool, l3, i3, i4, str5, str6, localAlbumIdIteratorFactory, localAlbumIdSink, localArtCacheKey, str3, localLruCache);
        if (!paramBoolean2)
        {
          int i5 = paramInt2;
          int i6 = paramInt3;
          Bitmap localBitmap2 = localBitmap1;
          localBitmap1 = copyAliasedBitmapHelper(i5, i6, localBitmap2, false);
        }
      }
    }
    if (localBitmap1 == null)
      localBitmap1 = createFauxAlbumArt(paramContext, i, paramBoolean1, paramLong, paramInt2, paramInt3, paramString1, paramString2, paramAlbumIdIteratorFactory, paramAlbumIdSink);
    return localBitmap1;
  }

  private static Bitmap getFauxAlbumArtImpl(Context paramContext, int paramInt1, boolean paramBoolean, long paramLong, int paramInt2, int paramInt3, String paramString1, String paramString2, AlbumIdIteratorFactory paramAlbumIdIteratorFactory, AlbumIdSink paramAlbumIdSink, ArtCacheKey paramArtCacheKey, String paramString3, LruCache<ArtCacheKey, Bitmap> paramLruCache)
  {
    LruCache<ArtCacheKey, Bitmap> localLruCache1 = paramLruCache;
    ArtCacheKey localArtCacheKey1 = paramArtCacheKey;
    Bitmap localBitmap = getBitmapFromCacheOrAcquireLock(localLruCache1, localArtCacheKey1);
    boolean bool1;
    if (localBitmap != null)
      bool1 = true;
    while (true)
    {
      Set localSet1 = null;
      if (localBitmap == null);
      try
      {
        AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
        AlbumIdTap localAlbumIdTap = new AlbumIdTap(localAlbumIdSink);
        Context localContext = paramContext;
        int i = paramInt1;
        boolean bool2 = paramBoolean;
        long l = paramLong;
        int j = paramInt2;
        int k = paramInt3;
        String str1 = paramString1;
        String str2 = paramString2;
        AlbumIdIteratorFactory localAlbumIdIteratorFactory = paramAlbumIdIteratorFactory;
        localBitmap = createFauxAlbumArt(localContext, i, bool2, l, j, k, str1, str2, localAlbumIdIteratorFactory, localAlbumIdTap);
        Set localSet2 = localAlbumIdTap.extractIds();
        Set localSet3 = localSet2;
        LruCache<ArtCacheKey, Bitmap> localLruCache2 = paramLruCache;
        ArtCacheKey localArtCacheKey2 = paramArtCacheKey;
        return localBitmap;
        bool1 = false;
      }
      finally
      {
        LruCache<ArtCacheKey, Bitmap> localLruCache3 = paramLruCache;
        ArtCacheKey localArtCacheKey3 = paramArtCacheKey;
        putBitmapInCacheAndReleaseLock(localLruCache3, localBitmap, localSet1, localArtCacheKey3);
      }
    }
  }

  public static Bitmap getLockScreenArtwork(Context paramContext, long paramLong, int paramInt1, int paramInt2, boolean paramBoolean1, String paramString1, String paramString2, boolean paramBoolean2)
  {
    if ((paramInt1 > 1024) || (paramInt2 > 1024))
    {
      paramInt1 = 1024;
      paramInt2 = 1024;
    }
    Context localContext = paramContext;
    long l = paramLong;
    int i = paramInt1;
    int j = paramInt2;
    boolean bool1 = paramBoolean1;
    String str1 = paramString1;
    String str2 = paramString2;
    boolean bool2 = paramBoolean2;
    return getArtwork(localContext, l, i, j, bool1, str1, str2, null, false, bool2);
  }

  public static int getMaxAlbumArtSize(Context paramContext)
  {
    if (!sMaxAlbumArtSizeInitalized)
      initializeMaxAlbumArtSize(paramContext);
    return sMaxAlbumArtSize;
  }

  public static Bitmap getMultiImageComposite(Context paramContext, String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    Context localContext1 = paramContext;
    int i = paramInt1;
    int j = paramInt2;
    String str1 = getSizeKey(localContext1, i, j);
    Context localContext2 = paramContext;
    int k = paramInt1;
    int m = paramInt2;
    String str2 = str1;
    LruCache localLruCache = getCache(localContext2, k, m, str2);
    int n;
    ArtCacheKey localArtCacheKey;
    Bitmap localBitmap1;
    boolean bool1;
    if (paramBoolean)
    {
      n = 19;
      long l = Store.generateId(paramString);
      int i1 = n;
      localArtCacheKey = new ArtCacheKey(i1, l);
      localBitmap1 = getBitmapFromCacheOrAcquireLock(localLruCache, localArtCacheKey);
      if (localBitmap1 == null)
        break label202;
      bool1 = true;
    }
    label202: 
    while (true)
      label95: if (localBitmap1 == null)
      {
        String[] arrayOfString = MusicUtils.decodeStringArray(paramString);
        int i2 = arrayOfString.length;
        ArrayList localArrayList = new ArrayList(i2);
        int i3 = 0;
        while (true)
          if (i3 < i2)
          {
            String str3 = arrayOfString[i3];
            int i4 = paramInt1 / 2;
            int i5 = paramInt2 / 2;
            Context localContext3 = paramContext;
            boolean bool2 = false;
            Bitmap localBitmap2 = getRealNonAlbumArt(localContext3, str3, i4, i5, bool2);
            if (localBitmap2 != null)
              boolean bool3 = localArrayList.add(localBitmap2);
            i3 += 1;
            continue;
            n = 9;
            break;
            bool1 = false;
            break label95;
          }
        Bitmap.Config localConfig = getPreferredConfig();
        int i6 = paramInt1;
        int i7 = paramInt2;
        localBitmap1 = createBitmap(i6, i7, localConfig);
        Bitmap localBitmap3 = localBitmap1;
        Canvas localCanvas = new Canvas(localBitmap3);
        Context localContext4 = paramContext;
        int i8 = paramInt1;
        int i9 = paramInt2;
        boolean bool4 = drawFauxPlaylistArt(localCanvas, localContext4, 9, i8, i9, localArrayList);
        if (paramBoolean)
        {
          Context localContext5 = paramContext;
          Bitmap localBitmap4 = localBitmap1;
          int i10 = paramInt1;
          int i11 = paramInt2;
          boolean bool5 = drawFauxRadioArtOverlay(localContext5, localBitmap4, i10, i11);
        }
        Bitmap localBitmap5 = localBitmap1;
        putBitmapInCacheAndReleaseLock(localLruCache, localBitmap5, null, localArtCacheKey);
      }
    if (localLruCache != null)
      trackCacheUsage(str1, bool1);
    if (localBitmap1 == null);
    for (Bitmap localBitmap6 = null; ; localBitmap6 = localBitmap1)
    {
      return localBitmap6;
      if (localLruCache != null)
      {
        int i12 = paramInt1;
        int i13 = paramInt2;
        Bitmap localBitmap7 = localBitmap1;
        localBitmap1 = copyAliasedBitmapHelper(i12, i13, localBitmap7, true);
      }
    }
  }

  private static Cursor getPlaylistMembersCursor(Context paramContext, int paramInt, long paramLong)
  {
    Cursor localCursor = null;
    int i = paramInt & 0xF;
    Uri localUri = null;
    boolean bool = true;
    switch (i)
    {
    default:
    case 1:
    case 2:
    case 3:
    case 4:
    }
    while (true)
    {
      if (localUri != null)
      {
        String[] arrayOfString1 = sAlbumIdCols;
        Context localContext = paramContext;
        String[] arrayOfString2 = null;
        String str = null;
        localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str, false, bool);
      }
      return localCursor;
      bool = true;
      localUri = MusicContent.Playlists.Members.getPlaylistItemsUri(paramLong);
      continue;
      bool = false;
      MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, paramContext);
      localUri = AutoPlaylist.getAutoPlaylist(paramLong, false, localMusicPreferences).getContentUri(paramContext);
    }
  }

  public static Bitmap.Config getPreferredConfig()
  {
    return sBitmapOptionsCache.inPreferredConfig;
  }

  public static Bitmap getRealNonAlbumArt(Context paramContext, String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    Object localObject = null;
    if ((paramInt1 <= 0) || (paramInt2 <= 0) || (TextUtils.isEmpty(paramString)))
      throw new IllegalArgumentException("must specify target height and width and url");
    ArtCacheKey localArtCacheKey;
    String str1;
    LruCache localLruCache;
    Bitmap localBitmap;
    boolean bool1;
    if (cacheable(paramString))
    {
      long l = Store.generateId(paramString);
      localArtCacheKey = new ArtCacheKey(20, l);
      str1 = getSizeKey(paramContext, paramInt1, paramInt2);
      localLruCache = getCache(paramContext, paramInt1, paramInt2, str1);
      localBitmap = getBitmapFromCacheOrAcquireLock(localLruCache, localArtCacheKey);
      if (localBitmap == null)
        break label169;
      bool1 = true;
      label90: if (localBitmap == null)
      {
        Context localContext = paramContext;
        String str2 = paramString;
        int i = paramInt1;
        int j = paramInt2;
        boolean bool2 = paramBoolean;
        localBitmap = resolveArtworkRaw(localContext, str2, i, j, null, bool2);
        putBitmapInCacheAndReleaseLock(localLruCache, localBitmap, null, localArtCacheKey);
      }
      if (localLruCache != null)
        trackCacheUsage(str1, bool1);
      if (localBitmap != null)
        break label175;
    }
    while (true)
    {
      return localObject;
      localLruCache = null;
      localArtCacheKey = null;
      str1 = null;
      break;
      label169: bool1 = false;
      break label90;
      label175: if (localLruCache != null)
        localBitmap = copyAliasedBitmapHelper(paramInt1, paramInt2, localBitmap, paramBoolean);
      localObject = localBitmap;
    }
  }

  public static Bitmap getRealNonAlbumArtCopy(Context paramContext, String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    Bitmap localBitmap1 = null;
    if (TextUtils.isEmpty(paramString));
    while (true)
    {
      return localBitmap1;
      if (cacheable(paramString))
      {
        long l = Store.generateId(paramString);
        ArtCacheKey localArtCacheKey = new ArtCacheKey(20, l);
        String str = getSizeKey(paramContext, paramInt1, paramInt2);
        Bitmap localBitmap2 = getBitmapFromCache(getCache(paramContext, paramInt1, paramInt2, str), localArtCacheKey);
        localBitmap1 = copyAliasedBitmapHelper(paramInt1, paramInt2, localBitmap2, paramBoolean);
      }
    }
  }

  private static String getSizeKey(Context paramContext, int paramInt1, int paramInt2)
  {
    MusicPreferences localMusicPreferences;
    int i;
    int j;
    String str;
    if (sCacheSmallBucketMaxDimen == 0)
    {
      sCacheSmallBucketMaxDimen = paramContext.getResources().getDimensionPixelSize(2131558483);
      sCacheMediumBucketMaxDimen = paramContext.getResources().getDimensionPixelSize(2131558484);
      Object localObject = new Object();
      localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, localObject);
      i = 3;
      if (localMusicPreferences.isLargeScreen())
      {
        i = 4;
        sCacheBucketMediumSize = (int)(paramContext.getResources().getInteger(2131492869) * i * 2.5D);
        sCacheBucketSmallSize = 30;
        MusicPreferences.releaseMusicPreferences(localObject);
      }
    }
    else
    {
      j = Math.max(paramInt1, paramInt2);
      int k = sCacheSmallBucketMaxDimen;
      if (j > k)
        break label133;
      str = "SMALL";
    }
    while (true)
    {
      return str;
      if (!localMusicPreferences.isXLargeScreen())
        break;
      i = 5;
      break;
      label133: int m = sCacheMediumBucketMaxDimen;
      if (j <= m)
        str = "MEDIUM";
      else
        str = "LARGE";
    }
  }

  /** @deprecated */
  private static File getStaticFauxArtCacheDir(Context paramContext)
  {
    try
    {
      if (mCacheDir == null)
      {
        StringBuilder localStringBuilder1 = new StringBuilder();
        File localFile1 = paramContext.getCacheDir();
        StringBuilder localStringBuilder2 = localStringBuilder1.append(localFile1);
        String str1 = File.separator;
        String str2 = str1;
        mCacheDir = new File(str2, "faux_artwork");
        if (!mCacheDir.exists())
          boolean bool1 = mCacheDir.mkdirs();
      }
      try
      {
        File localFile2 = mCacheDir;
        boolean bool2 = new File(localFile2, ".nomedia").createNewFile();
        File localFile3 = mCacheDir;
        return localFile3;
      }
      catch (IOException localIOException)
      {
        while (true)
        {
          String str3 = localIOException.getMessage();
          int i = Log.e("AlbumArtUtils", str3, localIOException);
        }
      }
    }
    finally
    {
    }
  }

  // ERROR //
  public static File getStaticFauxArtFile(Context paramContext, int paramInt1, long paramLong, String paramString1, String paramString2, int paramInt2, int paramInt3)
  {
    // Byte code:
    //   0: new 160	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 161	java/lang/StringBuilder:<init>	()V
    //   7: ldc_w 822
    //   10: invokevirtual 167	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   13: astore 8
    //   15: iload_1
    //   16: lload_2
    //   17: invokestatic 826	com/google/android/music/utils/AlbumArtUtils:getStaticFauxArtKey	(IJ)Ljava/lang/String;
    //   20: astore 9
    //   22: aload 8
    //   24: aload 9
    //   26: invokevirtual 167	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   29: invokevirtual 174	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   32: astore 10
    //   34: aload_0
    //   35: invokestatic 828	com/google/android/music/utils/AlbumArtUtils:getStaticFauxArtCacheDir	(Landroid/content/Context;)Ljava/io/File;
    //   38: astore 11
    //   40: new 786	java/io/File
    //   43: dup
    //   44: aload 11
    //   46: aload 10
    //   48: invokespecial 806	java/io/File:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   51: astore 12
    //   53: aload 12
    //   55: invokevirtual 798	java/io/File:exists	()Z
    //   58: ifne +112 -> 170
    //   61: iload 6
    //   63: bipush 255
    //   65: if_icmpeq +10 -> 75
    //   68: iload 7
    //   70: bipush 255
    //   72: if_icmpne +101 -> 173
    //   75: aload_0
    //   76: invokestatic 830	com/google/android/music/utils/AlbumArtUtils:getMaxAlbumArtSize	(Landroid/content/Context;)I
    //   79: istore 13
    //   81: iload_1
    //   82: bipush 16
    //   84: ior
    //   85: istore 14
    //   87: aload_0
    //   88: astore 15
    //   90: lload_2
    //   91: lstore 16
    //   93: iload 13
    //   95: istore 18
    //   97: aload 4
    //   99: astore 19
    //   101: aload 5
    //   103: astore 20
    //   105: aload 15
    //   107: iload 14
    //   109: iconst_1
    //   110: lload 16
    //   112: iload 13
    //   114: iload 18
    //   116: aload 19
    //   118: aload 20
    //   120: aconst_null
    //   121: aconst_null
    //   122: iconst_1
    //   123: invokestatic 674	com/google/android/music/utils/AlbumArtUtils:getFauxAlbumArt	(Landroid/content/Context;IZJIILjava/lang/String;Ljava/lang/String;Lcom/google/android/music/utils/AlbumArtUtils$AlbumIdIteratorFactory;Lcom/google/android/music/download/artwork/AlbumIdSink;Z)Landroid/graphics/Bitmap;
    //   126: astore 21
    //   128: aconst_null
    //   129: astore 22
    //   131: new 832	java/io/FileOutputStream
    //   134: dup
    //   135: aload 12
    //   137: invokespecial 835	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   140: astore 23
    //   142: getstatic 841	android/graphics/Bitmap$CompressFormat:PNG	Landroid/graphics/Bitmap$CompressFormat;
    //   145: astore 24
    //   147: aload 21
    //   149: aload 24
    //   151: bipush 100
    //   153: aload 23
    //   155: invokevirtual 845	android/graphics/Bitmap:compress	(Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
    //   158: istore 25
    //   160: aload 23
    //   162: ifnull +8 -> 170
    //   165: aload 23
    //   167: invokevirtual 848	java/io/FileOutputStream:close	()V
    //   170: aload 12
    //   172: areturn
    //   173: iload 6
    //   175: iload 7
    //   177: invokestatic 440	java/lang/Math:max	(II)I
    //   180: istore 13
    //   182: goto -101 -> 81
    //   185: astore 26
    //   187: aload 22
    //   189: astore 23
    //   191: aload 23
    //   193: ifnull -23 -> 170
    //   196: aload 23
    //   198: invokevirtual 848	java/io/FileOutputStream:close	()V
    //   201: goto -31 -> 170
    //   204: astore 27
    //   206: goto -36 -> 170
    //   209: astore 28
    //   211: aload 29
    //   213: ifnull +8 -> 221
    //   216: aload 29
    //   218: invokevirtual 848	java/io/FileOutputStream:close	()V
    //   221: aload 28
    //   223: athrow
    //   224: astore 30
    //   226: goto -56 -> 170
    //   229: astore 31
    //   231: goto -10 -> 221
    //   234: astore 32
    //   236: aload 23
    //   238: astore 29
    //   240: aload 32
    //   242: astore 28
    //   244: goto -33 -> 211
    //   247: astore 33
    //   249: goto -58 -> 191
    //
    // Exception table:
    //   from	to	target	type
    //   131	142	185	java/io/FileNotFoundException
    //   196	201	204	java/io/IOException
    //   131	142	209	finally
    //   165	170	224	java/io/IOException
    //   216	221	229	java/io/IOException
    //   142	160	234	finally
    //   142	160	247	java/io/FileNotFoundException
  }

  private static String getStaticFauxArtKey(int paramInt, long paramLong)
  {
    return "" + paramInt;
  }

  public static ParcelFileDescriptor getStaticFauxArtPipe(Context paramContext, int paramInt1, long paramLong, String paramString1, String paramString2, int paramInt2, int paramInt3)
  {
    int i;
    Bitmap localBitmap;
    ParcelFileDescriptor localParcelFileDescriptor1;
    if ((paramInt2 == -1) || (paramInt3 == -1))
    {
      i = getMaxAlbumArtSize(paramContext);
      int j = paramInt1 | 0x10;
      Context localContext = paramContext;
      long l = paramLong;
      int k = i;
      String str1 = paramString1;
      String str2 = paramString2;
      localBitmap = getFauxAlbumArt(localContext, j, true, l, i, k, str1, str2, null, null, true);
      if (localBitmap != null)
        break label101;
      int m = Log.e("AlbumArtUtils", "Could not create bitmap");
      localParcelFileDescriptor1 = null;
    }
    while (true)
    {
      return localParcelFileDescriptor1;
      i = Math.max(paramInt2, paramInt3);
      break;
      try
      {
        label101: ParcelFileDescriptor[] arrayOfParcelFileDescriptor = ParcelFileDescriptor.createPipe();
        ParcelFileDescriptor localParcelFileDescriptor2 = arrayOfParcelFileDescriptor[1];
        final ParcelFileDescriptor.AutoCloseOutputStream localAutoCloseOutputStream = new ParcelFileDescriptor.AutoCloseOutputStream(localParcelFileDescriptor2);
        LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
        Runnable local2 = new Runnable()
        {
          public void run()
          {
            try
            {
              Bitmap localBitmap = AlbumArtUtils.this;
              Bitmap.CompressFormat localCompressFormat = Bitmap.CompressFormat.PNG;
              OutputStream localOutputStream = localAutoCloseOutputStream;
              if (!localBitmap.compress(localCompressFormat, 100, localOutputStream))
                int i = Log.e("AlbumArtUtils", "Could not compress bitmap.");
              try
              {
                localAutoCloseOutputStream.close();
                return;
              }
              catch (IOException localIOException1)
              {
                int j = Log.e("AlbumArtUtils", "couldn't close", localIOException1);
                return;
              }
            }
            finally
            {
            }
            try
            {
              localAutoCloseOutputStream.close();
              throw localObject;
            }
            catch (IOException localIOException2)
            {
              while (true)
                int k = Log.e("AlbumArtUtils", "couldn't close", localIOException2);
            }
          }
        };
        AsyncWorkers.runAsync(localLoggableHandler, local2);
        if ((arrayOfParcelFileDescriptor != null) && (arrayOfParcelFileDescriptor.length > 0))
          localParcelFileDescriptor1 = arrayOfParcelFileDescriptor[0];
      }
      catch (IOException localIOException)
      {
        while (true)
          int n = Log.e("AlbumArtUtils", "Could not create pipe", localIOException);
        localParcelFileDescriptor1 = null;
      }
    }
  }

  private static List<Bitmap> getUniqueArt(Context paramContext, int paramInt1, long paramLong, int paramInt2, int paramInt3, int paramInt4, AlbumIdIteratorFactory paramAlbumIdIteratorFactory, AlbumIdSink paramAlbumIdSink)
  {
    HashSet localHashSet = new HashSet(paramInt2);
    ArrayList localArrayList = new ArrayList();
    Bitmap localBitmap = null;
    if (paramAlbumIdIteratorFactory != null)
      AlbumIdIterator localAlbumIdIterator = paramAlbumIdIteratorFactory.createIterator();
    Cursor localCursor;
    if (0 == 0)
    {
      localCursor = getPlaylistMembersCursor(paramContext, paramInt1, paramLong);
      if (localCursor == null);
    }
    for (CursorAlbumIdIterator localCursorAlbumIdIterator = new CursorAlbumIdIterator(localCursor, 0); ; localCursorAlbumIdIterator = null)
    {
      if (localCursorAlbumIdIterator != null);
      while (true)
      {
        try
        {
          Long localLong;
          if (localCursorAlbumIdIterator.hasNext())
          {
            localLong = Long.valueOf(localCursorAlbumIdIterator.next());
            if (localHashSet.contains(localLong))
              continue;
            boolean bool1 = localHashSet.add(localLong);
            int i = localArrayList.size();
            float f1 = playlistArtScaleFactor(paramInt1, i);
            int j = (int)Math.ceil(paramInt3 * f1);
            float f2 = paramInt4;
            int k = (int)Math.ceil(f1 * f2);
            long l = localLong.longValue();
            localBitmap = resolveArtwork(paramContext, l, j, k, false);
            if (localBitmap == null)
              continue;
            boolean bool2 = localArrayList.add(localBitmap);
            int m = localArrayList.size();
            if (m < paramInt2)
              continue;
          }
          return localArrayList;
          if (paramAlbumIdSink == null)
            continue;
          paramAlbumIdSink.report(localLong);
          continue;
        }
        finally
        {
          localCursorAlbumIdIterator.close();
        }
        String str = "Could not create cursor for children of " + paramLong;
        int n = Log.i("AlbumArtUtils", str);
      }
    }
  }

  public static void handleAlbumArtChanged(Long paramLong)
  {
    if (paramLong == null)
      return;
    synchronized (sSizeCache)
    {
      synchronized (mMissingAlbumArt)
      {
        Iterator localIterator = mMissingAlbumArt.values().iterator();
        while (localIterator.hasNext())
        {
          MissingArtEntry localMissingArtEntry = (MissingArtEntry)localIterator.next();
          if (localMissingArtEntry.missingAlbumIds.contains(paramLong))
            removeCachedBitmap(localMissingArtEntry.key);
        }
      }
    }
  }

  public static void handleSyncComplete()
  {
    synchronized (sSizeCache)
    {
      Iterator localIterator = sSizeCache.entrySet().iterator();
      while (localIterator.hasNext())
        synchronized ((Map.Entry)localIterator.next())
        {
          ((LruCache)???.getValue()).clear();
        }
    }
  }

  /** @deprecated */
  public static void initBitmapPoolForMainProcess()
  {
    try
    {
      long l1 = Runtime.getRuntime().maxMemory();
      double d1 = l1;
      long l2 = ()(0.02D * d1);
      int i = (int)Math.min(7840000L, l2);
      double d2 = l1;
      long l3 = ()(0.038D * d2);
      int j = (int)Math.min(7840000L, l3);
      sBitmapPool = new MutableBitmapPool(4, i, j);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  public static void initBitmapPoolForUIProcess()
  {
    try
    {
      long l1 = Runtime.getRuntime().maxMemory();
      double d1 = l1;
      long l2 = ()(0.08D * d1);
      int i = (int)Math.min(31360000L, l2);
      double d2 = l1;
      long l3 = ()(0.038D * d2);
      int j = (int)Math.min(7840000L, l3);
      sBitmapPool = new MutableBitmapPool(15, i, j);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private static void initializeMaxAlbumArtSize(Context paramContext)
  {
    Display localDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
    int i = localDisplay.getWidth();
    int j = localDisplay.getHeight();
    int k = Math.min(i, j);
    int m = findClosest(BUCKET_SIZES, k);
    long l1 = ArtDownloadService.getCacheLimit(paramContext);
    long l3;
    if (m > 0)
    {
      long l2 = estimatedArtFileSize(BUCKET_SIZES[m]);
      if (l1 / l2 < 300L);
    }
    else
    {
      l3 = ()(Runtime.getRuntime().maxMemory() * 0.04D);
    }
    while (true)
    {
      if (m > 0)
      {
        int n = BUCKET_SIZES[m];
        Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
        if (MutableBitmapPool.estimatedSize(n, n, localConfig) > l3);
      }
      else
      {
        sMaxAlbumArtSize = BUCKET_SIZES[m];
        sMaxAlbumArtSizeInitalized = true;
        return;
        m += -1;
        break;
      }
      m += -1;
    }
  }

  private static boolean isEmpty(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private static Bitmap loadBitmap(Context paramContext, FileDescriptor paramFileDescriptor, int paramInt1, int paramInt2, int[] paramArrayOfInt, boolean paramBoolean)
  {
    int i = 1;
    try
    {
      BitmapFactory.Options localOptions = new BitmapFactory.Options();
      Bitmap.Config localConfig1 = sBitmapOptionsCache.inPreferredConfig;
      localOptions.inPreferredConfig = localConfig1;
      localOptions.inJustDecodeBounds = true;
      localOptions.inSampleSize = 1;
      Bitmap localBitmap1 = BitmapFactory.decodeFileDescriptor(paramFileDescriptor, null, localOptions);
      if ((localOptions.outWidth <= 0) || (localOptions.outHeight <= 0))
        localObject = null;
      while (true)
      {
        return localObject;
        if (paramArrayOfInt != null)
        {
          int j = localOptions.outWidth;
          paramArrayOfInt[0] = j;
          int k = localOptions.outHeight;
          paramArrayOfInt[1] = k;
        }
        int m = localOptions.outWidth >> 1;
        int n = localOptions.outHeight >> 1;
        while (true)
        {
          int i1 = paramInt1;
          if (m <= i1)
            break;
          int i2 = paramInt2;
          if (n <= i2)
            break;
          i <<= 1;
          m >>= 1;
          n >>= 1;
        }
        Bitmap localBitmap2 = null;
        if (Build.VERSION.SDK_INT >= 11)
        {
          MutableBitmapPool localMutableBitmapPool1 = sBitmapPool;
          int i3 = localOptions.outWidth;
          int i4 = localOptions.outHeight;
          Bitmap.Config localConfig2 = getPreferredConfig();
          if (localMutableBitmapPool1.willCacheBitmap(i3, i4, localConfig2))
          {
            i = 1;
            MutableBitmapPool localMutableBitmapPool2 = sBitmapPool;
            int i5 = localOptions.outWidth;
            int i6 = localOptions.outHeight;
            Bitmap.Config localConfig3 = getPreferredConfig();
            localBitmap2 = localMutableBitmapPool2.createBitmap(i5, i6, localConfig3);
            PostFroyoUtils.BitmapFactoryOptionsCompat.setInBitmap(localOptions, localBitmap2);
          }
        }
        localOptions.inSampleSize = i;
        localOptions.inJustDecodeBounds = false;
        localObject = null;
        Rect localRect = null;
        try
        {
          localBitmap3 = BitmapFactory.decodeFileDescriptor(paramFileDescriptor, localRect, localOptions);
          localObject = localBitmap3;
          if (localObject == null)
            localObject = null;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          while (true)
            if (localBitmap2 != null)
            {
              localRect = null;
              PostFroyoUtils.BitmapFactoryOptionsCompat.setInBitmap(localOptions, localRect);
              localObject = BitmapFactory.decodeFileDescriptor(paramFileDescriptor, null, localOptions);
            }
          if ((paramBoolean) && (((Bitmap)localObject).getHeight() != 0))
          {
            float f1 = ((Bitmap)localObject).getWidth();
            float f2 = ((Bitmap)localObject).getHeight();
            float f3 = f1 / f2;
            paramInt1 = (int)(paramInt2 * f3);
          }
          int i7 = paramInt1;
          int i8 = paramInt2;
          Bitmap localBitmap3 = scaleToSize((Bitmap)localObject, i7, i8);
          localObject = localBitmap3;
        }
      }
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      while (true)
      {
        int i9 = paramInt1;
        int i10 = paramInt2;
        reportAndRethrow(localOutOfMemoryError, i9, i10);
        Object localObject = null;
      }
    }
  }

  public static long makeDefaultArtId(String paramString)
  {
    long l = Store.generateId(paramString);
    if (l == 0L)
      l = 65535L;
    if (l > 0L)
      l = -l;
    return l;
  }

  private static float playlistArtScaleFactor(int paramInt1, int paramInt2)
  {
    float f = 0.3333333F;
    switch (paramInt1)
    {
    default:
      if (paramInt2 < 2)
        f = 1.0F;
      break;
    case 3:
    }
    while (true)
    {
      return f;
      if (paramInt2 == 0)
      {
        f = 0.6666667F;
        continue;
        if (paramInt2 < 4)
          f = 0.5F;
      }
    }
  }

  public static int playlistTypeToArtStyle(int paramInt)
  {
    int i = 1;
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("Unknown playlist type");
    case 1:
      i = 2;
    case 0:
    case 71:
    case 50:
    case 100:
    case 10:
    case 70:
    }
    while (true)
    {
      return i;
      i = 3;
      continue;
      i = 4;
      continue;
      i = 4;
      continue;
      i = 9;
    }
  }

  private static void putBitmapInCache(LruCache<ArtCacheKey, Bitmap> paramLruCache, Bitmap paramBitmap, ArtCacheKey paramArtCacheKey)
  {
    throwIfRecycled(paramBitmap);
    if (paramLruCache == null)
      return;
    try
    {
      Object localObject1 = paramLruCache.put(paramArtCacheKey, paramBitmap);
      return;
    }
    finally
    {
      localObject2 = finally;
      throw localObject2;
    }
  }

  // ERROR //
  private static void putBitmapInCacheAndReleaseLock(LruCache<ArtCacheKey, Bitmap> paramLruCache, Bitmap paramBitmap, Set<Long> paramSet, ArtCacheKey paramArtCacheKey)
  {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull +4 -> 5
    //   4: return
    //   5: getstatic 110	com/google/android/music/utils/AlbumArtUtils:sCachedBitmapLocks	Ljava/util/Set;
    //   8: astore 4
    //   10: aload 4
    //   12: monitorenter
    //   13: aload_1
    //   14: ifnull +50 -> 64
    //   17: aload_0
    //   18: aload_1
    //   19: aload_3
    //   20: invokestatic 1067	com/google/android/music/utils/AlbumArtUtils:putBitmapInCache	(Lcom/google/android/music/utils/LruCache;Landroid/graphics/Bitmap;Lcom/google/android/music/utils/AlbumArtUtils$ArtCacheKey;)V
    //   23: aload_2
    //   24: ifnull +40 -> 64
    //   27: getstatic 129	com/google/android/music/utils/AlbumArtUtils:mMissingAlbumArt	Ljava/util/WeakHashMap;
    //   30: astore 5
    //   32: aload 5
    //   34: monitorenter
    //   35: getstatic 129	com/google/android/music/utils/AlbumArtUtils:mMissingAlbumArt	Ljava/util/WeakHashMap;
    //   38: astore 6
    //   40: new 33	com/google/android/music/utils/AlbumArtUtils$MissingArtEntry
    //   43: dup
    //   44: aload_3
    //   45: aload_2
    //   46: invokespecial 1070	com/google/android/music/utils/AlbumArtUtils$MissingArtEntry:<init>	(Lcom/google/android/music/utils/AlbumArtUtils$ArtCacheKey;Ljava/util/Set;)V
    //   49: astore 7
    //   51: aload 6
    //   53: aload_1
    //   54: aload 7
    //   56: invokevirtual 1071	java/util/WeakHashMap:put	(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   59: astore 8
    //   61: aload 5
    //   63: monitorexit
    //   64: getstatic 110	com/google/android/music/utils/AlbumArtUtils:sCachedBitmapLocks	Ljava/util/Set;
    //   67: aload_3
    //   68: invokeinterface 1073 2 0
    //   73: istore 9
    //   75: getstatic 110	com/google/android/music/utils/AlbumArtUtils:sCachedBitmapLocks	Ljava/util/Set;
    //   78: invokevirtual 1076	java/lang/Object:notifyAll	()V
    //   81: aload 4
    //   83: monitorexit
    //   84: return
    //   85: astore 10
    //   87: aload 4
    //   89: monitorexit
    //   90: aload 10
    //   92: athrow
    //   93: astore 11
    //   95: aload 5
    //   97: monitorexit
    //   98: aload 11
    //   100: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   17	35	85	finally
    //   64	85	85	finally
    //   98	101	85	finally
    //   35	64	93	finally
    //   95	98	93	finally
  }

  public static void recycleBitmap(Bitmap paramBitmap)
  {
    if (paramBitmap == null)
      return;
    sBitmapPool.recycleBitmap(paramBitmap);
  }

  private static void removeCachedBitmap(ArtCacheKey paramArtCacheKey)
  {
    synchronized (sSizeCache)
    {
      Iterator localIterator = sSizeCache.entrySet().iterator();
      while (localIterator.hasNext())
        synchronized ((Map.Entry)localIterator.next())
        {
          ((LruCache)???.getValue()).remove(paramArtCacheKey);
        }
    }
  }

  private static void renderFauxLabel(Canvas paramCanvas, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, String paramString, int paramInt5, int paramInt6, boolean paramBoolean2, int paramInt7, int paramInt8, boolean paramBoolean3, int paramInt9, int paramInt10, float paramFloat)
  {
    if (paramString == null)
      return;
    if (paramString.length() == 0)
      return;
    TextPaint localTextPaint = new TextPaint(129);
    float f1 = paramInt4;
    localTextPaint.setTextSize(f1);
    if (paramBoolean1)
    {
      Typeface localTypeface1 = Typeface.DEFAULT_BOLD;
      Typeface localTypeface2 = localTextPaint.setTypeface(localTypeface1);
    }
    Rect localRect = new Rect();
    int i = paramString.length();
    String str1 = paramString;
    localTextPaint.getTextBounds(str1, 0, i, localRect);
    int j = getCapHeight(localTextPaint);
    if (paramBoolean3)
    {
      int k = (paramInt9 - j) / 2;
      paramInt3 += k;
    }
    int m = paramInt3 + j;
    int n = localRect.right;
    int i1 = paramInt8;
    if (n >= i1)
      paramBoolean2 = false;
    int i2 = paramInt1;
    if (n > i2);
    for (int i3 = 1; ; i3 = 0)
    {
      int i4 = paramInt5;
      localTextPaint.setColor(i4);
      if (paramFloat > 0.0F)
      {
        float f2 = 0.707F * paramFloat;
        float f3 = paramFloat;
        int i5 = paramInt10;
        localTextPaint.setShadowLayer(f3, f2, f2, i5);
      }
      if (i3 == 0)
        break;
      int i6 = (int)Math.ceil(paramFloat) + 2;
      int i7 = (int)Math.ceil(paramFloat) + 2;
      int i8 = localRect.left + paramInt2 - 2;
      int i9 = localRect.top + m - 2;
      int i10 = localRect.right + paramInt2 + i6;
      int i11 = localRect.bottom + m + i7;
      int i12 = i10 - i8;
      int i13 = i11 - i9;
      MutableBitmapPool localMutableBitmapPool1 = sBitmapPool;
      Bitmap.Config localConfig1 = Bitmap.Config.ARGB_8888;
      Bitmap localBitmap1 = localMutableBitmapPool1.createBitmap(i12, i13, localConfig1);
      Canvas localCanvas1 = new Canvas(localBitmap1);
      float f4 = paramInt2 - i8;
      float f5 = m - i9;
      String str2 = paramString;
      localCanvas1.drawText(str2, f4, f5, localTextPaint);
      int i14 = paramInt2 + paramInt1 - paramInt6 - i8;
      int i15 = i14 + paramInt6;
      MutableBitmapPool localMutableBitmapPool2 = sBitmapPool;
      Bitmap.Config localConfig2 = Bitmap.Config.ARGB_8888;
      Bitmap localBitmap2 = localMutableBitmapPool2.createBitmap(i12, i13, localConfig2);
      Canvas localCanvas2 = new Canvas(localBitmap2);
      Paint localPaint1 = new Paint();
      float f6 = i14;
      float f7 = i15;
      Shader.TileMode localTileMode = Shader.TileMode.CLAMP;
      LinearGradient localLinearGradient = new LinearGradient(f6, 0.0F, f7, 0.0F, -1, 0, localTileMode);
      Shader localShader = localPaint1.setShader(localLinearGradient);
      localCanvas2.drawPaint(localPaint1);
      Paint localPaint2 = new Paint();
      PorterDuff.Mode localMode1 = PorterDuff.Mode.MULTIPLY;
      PorterDuffXfermode localPorterDuffXfermode1 = new PorterDuffXfermode(localMode1);
      Xfermode localXfermode1 = localPaint2.setXfermode(localPorterDuffXfermode1);
      localCanvas1.drawBitmap(localBitmap2, 0.0F, 0.0F, localPaint2);
      Paint localPaint3 = new Paint();
      PorterDuff.Mode localMode2 = PorterDuff.Mode.SRC_OVER;
      PorterDuffXfermode localPorterDuffXfermode2 = new PorterDuffXfermode(localMode2);
      Xfermode localXfermode2 = localPaint3.setXfermode(localPorterDuffXfermode2);
      float f8 = i8;
      float f9 = i9;
      paramCanvas.drawBitmap(localBitmap1, f8, f9, localPaint3);
      recycleBitmap(localBitmap1);
      recycleBitmap(localBitmap2);
      return;
    }
    if (paramBoolean2)
    {
      int i16 = localRect.right;
      paramInt2 = paramInt7 - i16;
    }
    float f10 = paramInt2;
    float f11 = m;
    Canvas localCanvas3 = paramCanvas;
    String str3 = paramString;
    localCanvas3.drawText(str3, f10, f11, localTextPaint);
  }

  /** @deprecated */
  // ERROR //
  public static void report(OutOfMemoryError paramOutOfMemoryError)
  {
    // Byte code:
    //   0: ldc 2
    //   2: monitorenter
    //   3: getstatic 1160	com/google/android/music/utils/AlbumArtUtils:sHprofDumped	Z
    //   6: ifne +82 -> 88
    //   9: iconst_1
    //   10: putstatic 1160	com/google/android/music/utils/AlbumArtUtils:sHprofDumped	Z
    //   13: new 160	java/lang/StringBuilder
    //   16: dup
    //   17: invokespecial 161	java/lang/StringBuilder:<init>	()V
    //   20: astore_1
    //   21: invokestatic 1165	android/os/Environment:getExternalStorageDirectory	()Ljava/io/File;
    //   24: astore_2
    //   25: aload_1
    //   26: aload_2
    //   27: invokevirtual 784	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   30: ldc_w 1167
    //   33: invokevirtual 167	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   36: invokevirtual 174	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   39: astore_3
    //   40: aload_3
    //   41: invokestatic 1172	android/os/Debug:dumpHprofData	(Ljava/lang/String;)V
    //   44: new 160	java/lang/StringBuilder
    //   47: dup
    //   48: invokespecial 161	java/lang/StringBuilder:<init>	()V
    //   51: ldc_w 1174
    //   54: invokevirtual 167	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   57: aload_3
    //   58: invokevirtual 167	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   61: ldc_w 1176
    //   64: invokevirtual 167	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: ldc_w 1178
    //   70: invokevirtual 167	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   73: invokevirtual 174	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   76: astore 4
    //   78: ldc_w 552
    //   81: aload 4
    //   83: invokestatic 856	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   86: istore 5
    //   88: ldc 2
    //   90: monitorexit
    //   91: return
    //   92: astore 6
    //   94: ldc 2
    //   96: monitorexit
    //   97: aload 6
    //   99: athrow
    //   100: astore 7
    //   102: goto -14 -> 88
    //
    // Exception table:
    //   from	to	target	type
    //   3	13	92	finally
    //   13	88	92	finally
    //   13	88	100	java/lang/Throwable
  }

  public static void reportAndRethrow(OutOfMemoryError paramOutOfMemoryError, int paramInt1, int paramInt2)
  {
    String str = "Out of memory allocating a (" + paramInt1 + ", " + paramInt2 + ") sized texture.";
    int i = Log.e("AlbumArtUtils", str);
    report(paramOutOfMemoryError);
    throw paramOutOfMemoryError;
  }

  private static Bitmap resizeHelper(Bitmap paramBitmap, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    if ((!paramBoolean) && (i <= paramInt1) && (j <= paramInt2));
    while (true)
    {
      return paramBitmap;
      paramBitmap = scaleToSize(paramBitmap, paramInt1, paramInt2);
    }
  }

  private static Bitmap resolveArtwork(Context paramContext, long paramLong, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    int[] arrayOfInt = new int[2];
    Context localContext = paramContext;
    long l = paramLong;
    int i = paramInt1;
    int j = paramInt2;
    Object localObject = resolveArtworkRaw(localContext, l, i, j, arrayOfInt);
    if (localObject != null);
    try
    {
      Bitmap localBitmap = resizeHelper((Bitmap)localObject, paramInt1, paramInt2, paramBoolean);
      localObject = localBitmap;
      return localObject;
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      while (true)
        reportAndRethrow(localOutOfMemoryError, paramInt1, paramInt2);
    }
  }

  // ERROR //
  private static Bitmap resolveArtworkFromUrl(String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    // Byte code:
    //   0: getstatic 96	com/google/android/music/utils/AlbumArtUtils:LOGV	Z
    //   3: ifeq +43 -> 46
    //   6: new 160	java/lang/StringBuilder
    //   9: dup
    //   10: invokespecial 161	java/lang/StringBuilder:<init>	()V
    //   13: ldc_w 1197
    //   16: invokevirtual 167	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   19: astore 4
    //   21: aload_0
    //   22: astore 5
    //   24: aload 4
    //   26: aload 5
    //   28: invokevirtual 167	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   31: invokevirtual 174	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   34: astore 6
    //   36: ldc_w 552
    //   39: aload 6
    //   41: invokestatic 683	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   44: istore 7
    //   46: iload_1
    //   47: ifle +7 -> 54
    //   50: iload_2
    //   51: ifgt +14 -> 65
    //   54: new 176	java/lang/IllegalArgumentException
    //   57: dup
    //   58: ldc_w 1199
    //   61: invokespecial 179	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   64: athrow
    //   65: aload_0
    //   66: ifnonnull +9 -> 75
    //   69: aconst_null
    //   70: astore 8
    //   72: aload 8
    //   74: areturn
    //   75: aconst_null
    //   76: astore 9
    //   78: aload_0
    //   79: astore 10
    //   81: new 1201	java/net/URL
    //   84: dup
    //   85: aload 10
    //   87: invokespecial 1202	java/net/URL:<init>	(Ljava/lang/String;)V
    //   90: invokevirtual 1206	java/net/URL:openConnection	()Ljava/net/URLConnection;
    //   93: astore 11
    //   95: aload 11
    //   97: invokevirtual 1211	java/net/URLConnection:connect	()V
    //   100: aload 11
    //   102: invokevirtual 1215	java/net/URLConnection:getInputStream	()Ljava/io/InputStream;
    //   105: astore 12
    //   107: new 1217	java/io/BufferedInputStream
    //   110: dup
    //   111: aload 12
    //   113: sipush 10240
    //   116: invokespecial 1220	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;I)V
    //   119: astore 13
    //   121: aload 11
    //   123: invokevirtual 1223	java/net/URLConnection:getContentLength	()I
    //   126: istore 14
    //   128: new 18	com/google/android/music/utils/AlbumArtUtils$ExtractableByteArrayOutputStream
    //   131: astore 15
    //   133: iload 14
    //   135: bipush 255
    //   137: if_icmpne +8 -> 145
    //   140: sipush 10240
    //   143: istore 14
    //   145: aload 15
    //   147: iload 14
    //   149: invokespecial 1224	com/google/android/music/utils/AlbumArtUtils$ExtractableByteArrayOutputStream:<init>	(I)V
    //   152: sipush 10240
    //   155: newarray byte
    //   157: astore 16
    //   159: aload 12
    //   161: aload 16
    //   163: invokevirtual 1230	java/io/InputStream:read	([B)I
    //   166: istore 17
    //   168: iload 17
    //   170: bipush 255
    //   172: if_icmpeq +55 -> 227
    //   175: aload 15
    //   177: aload 16
    //   179: iconst_0
    //   180: iload 17
    //   182: invokevirtual 1234	com/google/android/music/utils/AlbumArtUtils$ExtractableByteArrayOutputStream:write	([BII)V
    //   185: goto -26 -> 159
    //   188: astore 18
    //   190: aload 13
    //   192: astore 9
    //   194: ldc_w 1236
    //   197: astore 19
    //   199: ldc_w 552
    //   202: aload 19
    //   204: aload 18
    //   206: invokestatic 1238	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   209: istore 20
    //   211: aconst_null
    //   212: astore 8
    //   214: aload 9
    //   216: invokestatic 1244	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   219: aload 12
    //   221: invokestatic 1244	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   224: goto -152 -> 72
    //   227: aload 15
    //   229: invokevirtual 1248	com/google/android/music/utils/AlbumArtUtils$ExtractableByteArrayOutputStream:toInputStream	()Ljava/io/ByteArrayInputStream;
    //   232: astore 21
    //   234: aload 21
    //   236: astore 22
    //   238: aload 13
    //   240: invokestatic 1244	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   243: aload 12
    //   245: invokestatic 1244	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   248: getstatic 105	com/google/android/music/utils/AlbumArtUtils:sExternalBitmapOptionsCache	Landroid/graphics/BitmapFactory$Options;
    //   251: astore 23
    //   253: iconst_1
    //   254: istore 24
    //   256: aload 23
    //   258: iload 24
    //   260: putfield 1021	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
    //   263: getstatic 105	com/google/android/music/utils/AlbumArtUtils:sExternalBitmapOptionsCache	Landroid/graphics/BitmapFactory$Options;
    //   266: astore 25
    //   268: aload 22
    //   270: aconst_null
    //   271: aload 25
    //   273: invokestatic 539	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   276: astore 26
    //   278: getstatic 105	com/google/android/music/utils/AlbumArtUtils:sExternalBitmapOptionsCache	Landroid/graphics/BitmapFactory$Options;
    //   281: astore 27
    //   283: iconst_0
    //   284: istore 28
    //   286: aload 27
    //   288: iload 28
    //   290: putfield 1021	android/graphics/BitmapFactory$Options:inJustDecodeBounds	Z
    //   293: aload 22
    //   295: invokevirtual 1253	java/io/ByteArrayInputStream:reset	()V
    //   298: getstatic 105	com/google/android/music/utils/AlbumArtUtils:sExternalBitmapOptionsCache	Landroid/graphics/BitmapFactory$Options;
    //   301: astore 29
    //   303: getstatic 105	com/google/android/music/utils/AlbumArtUtils:sExternalBitmapOptionsCache	Landroid/graphics/BitmapFactory$Options;
    //   306: getfield 1031	android/graphics/BitmapFactory$Options:outWidth	I
    //   309: iload_1
    //   310: idiv
    //   311: istore 30
    //   313: getstatic 105	com/google/android/music/utils/AlbumArtUtils:sExternalBitmapOptionsCache	Landroid/graphics/BitmapFactory$Options;
    //   316: getfield 1034	android/graphics/BitmapFactory$Options:outHeight	I
    //   319: iload_2
    //   320: idiv
    //   321: istore 31
    //   323: iload 30
    //   325: iload 31
    //   327: invokestatic 990	java/lang/Math:min	(II)I
    //   330: istore 32
    //   332: iconst_1
    //   333: iload 32
    //   335: invokestatic 440	java/lang/Math:max	(II)I
    //   338: istore 33
    //   340: aload 29
    //   342: iload 33
    //   344: putfield 1024	android/graphics/BitmapFactory$Options:inSampleSize	I
    //   347: getstatic 105	com/google/android/music/utils/AlbumArtUtils:sExternalBitmapOptionsCache	Landroid/graphics/BitmapFactory$Options;
    //   350: astore 34
    //   352: aload 22
    //   354: aconst_null
    //   355: aload 34
    //   357: invokestatic 539	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   360: astore 8
    //   362: aload 22
    //   364: invokevirtual 1253	java/io/ByteArrayInputStream:reset	()V
    //   367: getstatic 96	com/google/android/music/utils/AlbumArtUtils:LOGV	Z
    //   370: ifeq +36 -> 406
    //   373: new 160	java/lang/StringBuilder
    //   376: dup
    //   377: invokespecial 161	java/lang/StringBuilder:<init>	()V
    //   380: ldc_w 1255
    //   383: invokevirtual 167	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   386: aload 8
    //   388: invokevirtual 784	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   391: invokevirtual 174	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   394: astore 35
    //   396: ldc_w 552
    //   399: aload 35
    //   401: invokestatic 683	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   404: istore 36
    //   406: aload 8
    //   408: ifnull -336 -> 72
    //   411: iload_1
    //   412: istore 37
    //   414: iload_2
    //   415: istore 38
    //   417: iload_3
    //   418: istore 39
    //   420: aload 8
    //   422: iload 37
    //   424: iload 38
    //   426: iload 39
    //   428: invokestatic 1193	com/google/android/music/utils/AlbumArtUtils:resizeHelper	(Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;
    //   431: astore 8
    //   433: goto -361 -> 72
    //   436: astore 40
    //   438: aload 41
    //   440: invokestatic 1244	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   443: aload 12
    //   445: invokestatic 1244	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   448: aload 40
    //   450: athrow
    //   451: astore 40
    //   453: aload 13
    //   455: astore 41
    //   457: goto -19 -> 438
    //   460: astore 18
    //   462: goto -268 -> 194
    //
    // Exception table:
    //   from	to	target	type
    //   121	185	188	java/io/IOException
    //   227	234	188	java/io/IOException
    //   78	121	436	finally
    //   194	211	436	finally
    //   121	185	451	finally
    //   227	234	451	finally
    //   78	121	460	java/io/IOException
  }

  private static Bitmap resolveArtworkImp(Context paramContext, long paramLong, String paramString1, int paramInt1, int paramInt2, String paramString2, String paramString3, AlbumIdSink paramAlbumIdSink, boolean paramBoolean1, boolean paramBoolean2)
  {
    Bitmap localBitmap = null;
    if (paramLong < 0L)
    {
      String str1 = paramString1;
      int i = paramInt1;
      int j = paramInt2;
      localBitmap = resolveArtworkFromUrl(str1, i, j, true);
    }
    while (true)
    {
      if ((localBitmap == null) && (paramBoolean1))
      {
        Context localContext1 = paramContext;
        long l1 = paramLong;
        int k = paramInt1;
        int m = paramInt2;
        String str2 = paramString2;
        String str3 = paramString3;
        AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
        boolean bool = paramBoolean2;
        localBitmap = getDefaultArtwork(localContext1, true, l1, k, m, str2, str3, localAlbumIdSink, bool);
      }
      return localBitmap;
      if (paramLong > 0L)
      {
        Context localContext2 = paramContext;
        long l2 = paramLong;
        int n = paramInt1;
        int i1 = paramInt2;
        localBitmap = resolveArtwork(localContext2, l2, n, i1, true);
      }
    }
  }

  // ERROR //
  private static Bitmap resolveArtworkRaw(Context paramContext, long paramLong, int paramInt1, int paramInt2, int[] paramArrayOfInt)
  {
    // Byte code:
    //   0: iload_3
    //   1: ifle +8 -> 9
    //   4: iload 4
    //   6: ifgt +14 -> 20
    //   9: new 176	java/lang/IllegalArgumentException
    //   12: dup
    //   13: ldc_w 1259
    //   16: invokespecial 179	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   19: athrow
    //   20: aload_0
    //   21: lload_1
    //   22: iload_3
    //   23: iload 4
    //   25: invokestatic 1265	com/google/android/music/store/MusicContent$AlbumArt:openFileDescriptor	(Landroid/content/Context;JII)Landroid/os/ParcelFileDescriptor;
    //   28: astore 6
    //   30: aload 6
    //   32: ifnull +57 -> 89
    //   35: aload 6
    //   37: invokevirtual 1269	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   40: astore 7
    //   42: aload_0
    //   43: astore 8
    //   45: iload_3
    //   46: istore 9
    //   48: iload 4
    //   50: istore 10
    //   52: aload 5
    //   54: astore 11
    //   56: aload 8
    //   58: aload 7
    //   60: iload 9
    //   62: iload 10
    //   64: aload 11
    //   66: iconst_0
    //   67: invokestatic 1271	com/google/android/music/utils/AlbumArtUtils:loadBitmap	(Landroid/content/Context;Ljava/io/FileDescriptor;II[IZ)Landroid/graphics/Bitmap;
    //   70: astore 12
    //   72: aload 12
    //   74: astore 13
    //   76: aload 6
    //   78: ifnull +8 -> 86
    //   81: aload 6
    //   83: invokevirtual 1272	android/os/ParcelFileDescriptor:close	()V
    //   86: aload 13
    //   88: areturn
    //   89: aload 6
    //   91: ifnull +8 -> 99
    //   94: aload 6
    //   96: invokevirtual 1272	android/os/ParcelFileDescriptor:close	()V
    //   99: aconst_null
    //   100: astore 13
    //   102: goto -16 -> 86
    //   105: astore 14
    //   107: aload 6
    //   109: ifnull -10 -> 99
    //   112: aload 6
    //   114: invokevirtual 1272	android/os/ParcelFileDescriptor:close	()V
    //   117: goto -18 -> 99
    //   120: astore 15
    //   122: goto -23 -> 99
    //   125: astore 16
    //   127: aload 6
    //   129: ifnull +8 -> 137
    //   132: aload 6
    //   134: invokevirtual 1272	android/os/ParcelFileDescriptor:close	()V
    //   137: aload 16
    //   139: athrow
    //   140: astore 17
    //   142: goto -56 -> 86
    //   145: astore 18
    //   147: goto -48 -> 99
    //   150: astore 19
    //   152: goto -15 -> 137
    //
    // Exception table:
    //   from	to	target	type
    //   20	72	105	java/io/FileNotFoundException
    //   112	117	120	java/io/IOException
    //   20	72	125	finally
    //   81	86	140	java/io/IOException
    //   94	99	145	java/io/IOException
    //   132	137	150	java/io/IOException
  }

  // ERROR //
  private static Bitmap resolveArtworkRaw(Context paramContext, String paramString, int paramInt1, int paramInt2, int[] paramArrayOfInt, boolean paramBoolean)
  {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: iload_2
    //   3: iload_3
    //   4: invokestatic 1277	com/google/android/music/store/MusicContent$CachedArt:openFileDescriptor	(Landroid/content/Context;Ljava/lang/String;II)Landroid/os/ParcelFileDescriptor;
    //   7: astore 6
    //   9: aload 6
    //   11: ifnull +101 -> 112
    //   14: iload_2
    //   15: ifle +65 -> 80
    //   18: iload_3
    //   19: ifle +61 -> 80
    //   22: aload 6
    //   24: invokevirtual 1269	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   27: astore 7
    //   29: aload_0
    //   30: astore 8
    //   32: iload_2
    //   33: istore 9
    //   35: iload_3
    //   36: istore 10
    //   38: aload 4
    //   40: astore 11
    //   42: iload 5
    //   44: istore 12
    //   46: aload 8
    //   48: aload 7
    //   50: iload 9
    //   52: iload 10
    //   54: aload 11
    //   56: iload 12
    //   58: invokestatic 1271	com/google/android/music/utils/AlbumArtUtils:loadBitmap	(Landroid/content/Context;Ljava/io/FileDescriptor;II[IZ)Landroid/graphics/Bitmap;
    //   61: astore 13
    //   63: aload 13
    //   65: astore 14
    //   67: aload 6
    //   69: ifnull +8 -> 77
    //   72: aload 6
    //   74: invokevirtual 1272	android/os/ParcelFileDescriptor:close	()V
    //   77: aload 14
    //   79: areturn
    //   80: aload 6
    //   82: invokevirtual 1269	android/os/ParcelFileDescriptor:getFileDescriptor	()Ljava/io/FileDescriptor;
    //   85: invokestatic 1280	android/graphics/BitmapFactory:decodeFileDescriptor	(Ljava/io/FileDescriptor;)Landroid/graphics/Bitmap;
    //   88: astore 13
    //   90: aload 13
    //   92: astore 14
    //   94: aload 6
    //   96: ifnull -19 -> 77
    //   99: aload 6
    //   101: invokevirtual 1272	android/os/ParcelFileDescriptor:close	()V
    //   104: goto -27 -> 77
    //   107: astore 15
    //   109: goto -32 -> 77
    //   112: aload 6
    //   114: ifnull +8 -> 122
    //   117: aload 6
    //   119: invokevirtual 1272	android/os/ParcelFileDescriptor:close	()V
    //   122: aconst_null
    //   123: astore 14
    //   125: goto -48 -> 77
    //   128: astore 16
    //   130: aload 6
    //   132: ifnull -10 -> 122
    //   135: aload 6
    //   137: invokevirtual 1272	android/os/ParcelFileDescriptor:close	()V
    //   140: goto -18 -> 122
    //   143: astore 17
    //   145: goto -23 -> 122
    //   148: astore 18
    //   150: aload 6
    //   152: ifnull +8 -> 160
    //   155: aload 6
    //   157: invokevirtual 1272	android/os/ParcelFileDescriptor:close	()V
    //   160: aload 18
    //   162: athrow
    //   163: astore 19
    //   165: goto -88 -> 77
    //   168: astore 20
    //   170: goto -48 -> 122
    //   173: astore 21
    //   175: goto -15 -> 160
    //
    // Exception table:
    //   from	to	target	type
    //   99	104	107	java/io/IOException
    //   0	63	128	java/io/FileNotFoundException
    //   80	90	128	java/io/FileNotFoundException
    //   135	140	143	java/io/IOException
    //   0	63	148	finally
    //   80	90	148	finally
    //   72	77	163	java/io/IOException
    //   117	122	168	java/io/IOException
    //   155	160	173	java/io/IOException
  }

  private static Bitmap scaleStep(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    MutableBitmapPool localMutableBitmapPool = sBitmapPool;
    Bitmap.Config localConfig1 = getPreferredConfig();
    Bitmap.Config localConfig2 = getConfigOrDefault(paramBitmap, localConfig1);
    Bitmap localBitmap = localMutableBitmapPool.createBitmap(paramInt1, paramInt2, localConfig2);
    scaleToFit(paramBitmap, localBitmap);
    sBitmapPool.recycleBitmap(paramBitmap);
    return localBitmap;
  }

  private static void scaleToFit(Bitmap paramBitmap1, Bitmap paramBitmap2)
  {
    Canvas localCanvas = new Canvas(paramBitmap2);
    Paint localPaint = new Paint();
    localPaint.setFilterBitmap(true);
    int i = paramBitmap1.getWidth();
    int j = paramBitmap1.getHeight();
    Rect localRect = new Rect(0, 0, i, j);
    float f1 = paramBitmap2.getWidth();
    float f2 = paramBitmap2.getHeight();
    RectF localRectF = new RectF(0.0F, 0.0F, f1, f2);
    localCanvas.drawBitmap(paramBitmap1, localRect, localRectF, localPaint);
  }

  public static Bitmap scaleToSize(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    if ((paramInt1 > 0) && ((paramBitmap.getWidth() == paramInt1) || (paramBitmap.getHeight() != paramInt2)))
    {
      while (true)
      {
        int i = paramBitmap.getWidth();
        int j = paramInt1 * 2;
        if (i <= j)
          break;
        int k = paramBitmap.getWidth() / 2;
        int m = paramBitmap.getHeight() / 2;
        paramBitmap = scaleStep(paramBitmap, k, m);
      }
      if ((paramBitmap.getWidth() == paramInt1) || (paramBitmap.getHeight() != paramInt2))
        paramBitmap = scaleStep(paramBitmap, paramInt1, paramInt2);
    }
    return paramBitmap;
  }

  public static void setPreferredConfig(Bitmap.Config paramConfig)
  {
    sBitmapOptionsCache.inPreferredConfig = paramConfig;
    sExternalBitmapOptionsCache.inPreferredConfig = paramConfig;
  }

  public static String stripDimensionFromImageUrl(String paramString)
  {
    try
    {
      Uri localUri1 = Uri.parse(paramString);
      Uri localUri2 = localUri1;
      String str1 = localUri2.getPath();
      if (Strings.isNullOrEmpty(str1));
      while (true)
      {
        label19: return paramString;
        int i = str1.lastIndexOf('=');
        if (i == -1)
          int j = str1.lastIndexOf("%3D");
        if (i != -1)
        {
          String str2 = str1.substring(0, i);
          paramString = localUri2.buildUpon().path(str2).build().toString();
        }
      }
    }
    catch (Exception localException)
    {
      break label19;
    }
  }

  private static void throwIfRecycled(Bitmap paramBitmap)
  {
    if (paramBitmap == null)
      return;
    if (!paramBitmap.isRecycled())
      return;
    String str1 = "Bitmap " + paramBitmap + " is recycled.";
    int i = Log.e("AlbumArtUtils", str1);
    String str2 = paramBitmap + " isRecycled";
    throw new IllegalArgumentException(str2);
  }

  private static void trackCacheUsage(String paramString, boolean paramBoolean)
  {
    synchronized (sSizeCache)
    {
      sTotalSizeCacheRequests += 1;
      CacheRequest localCacheRequest = (CacheRequest)sSizeCacheRequests.get(paramString);
      if (localCacheRequest == null)
      {
        localCacheRequest = new CacheRequest(null);
        Object localObject1 = sSizeCacheRequests.put(paramString, localCacheRequest);
      }
      int i = CacheRequest.access$608(localCacheRequest);
      if (paramBoolean)
        int j = CacheRequest.access$708(localCacheRequest);
      return;
    }
  }

  private static class CacheRequest
  {
    private int cacheHits = 0;
    private int cacheRequests = 0;
  }

  private static class ArtCacheKey
  {
    private final long mId;
    private final int mType;

    public ArtCacheKey(int paramInt, long paramLong)
    {
      this.mType = paramInt;
      this.mId = paramLong;
    }

    public boolean equals(Object paramObject)
    {
      boolean bool = false;
      if ((paramObject instanceof ArtCacheKey))
      {
        ArtCacheKey localArtCacheKey = (ArtCacheKey)paramObject;
        long l1 = localArtCacheKey.mId;
        long l2 = this.mId;
        if (l1 == l2)
        {
          int i = localArtCacheKey.mType;
          int j = this.mType;
          if (i != j)
            bool = true;
        }
      }
      return bool;
    }

    public int hashCode()
    {
      long l1 = this.mId;
      long l2 = this.mId >> 32;
      int i = (int)(l1 ^ l2);
      int j = this.mType;
      return i ^ j;
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("ArtCacheKey: type=");
      int i = this.mType;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(i).append(" id=");
      long l = this.mId;
      return l;
    }
  }

  private static class ExtractableByteArrayOutputStream extends ByteArrayOutputStream
  {
    public ExtractableByteArrayOutputStream(int paramInt)
    {
      super();
    }

    public ByteArrayInputStream toInputStream()
    {
      byte[] arrayOfByte = this.buf;
      int i = this.count;
      return new ByteArrayInputStream(arrayOfByte, 0, i);
    }
  }

  public static abstract interface AlbumIdIteratorFactory
  {
    public abstract AlbumArtUtils.AlbumIdIterator createIterator();
  }

  private static class CursorAlbumIdIterator
    implements AlbumArtUtils.AlbumIdIterator
  {
    private final int mCount;
    private Cursor mCursor;
    private final int mIdIndex;
    private int mPosition = 0;

    public CursorAlbumIdIterator(Cursor paramCursor, int paramInt)
    {
      this.mCursor = paramCursor;
      int i = this.mCursor.getCount();
      this.mCount = i;
      this.mIdIndex = paramInt;
    }

    public void close()
    {
      Store.safeClose(this.mCursor);
    }

    public boolean hasNext()
    {
      int i = this.mPosition;
      int j = this.mCount;
      if (i < j);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public long next()
    {
      int i = this.mPosition;
      int j = this.mCount;
      if (i >= j)
        throw new NoSuchElementException();
      Cursor localCursor1 = this.mCursor;
      int k = this.mPosition;
      int m = k + 1;
      this.mPosition = m;
      if (!localCursor1.moveToPosition(k))
        throw new NoSuchElementException();
      Cursor localCursor2 = this.mCursor;
      int n = this.mIdIndex;
      return localCursor2.getLong(n);
    }
  }

  public static abstract interface AlbumIdIterator
  {
    public abstract void close();

    public abstract boolean hasNext();

    public abstract long next();
  }

  private static class MutableBitmapPool
  {
    int mCapacityBytes;
    int mCapacityItems;
    private Entry mHead;
    int mLargestItemBytes;
    private Map<Key, LinkedList<Entry>> mMap;
    int mSizeBytes;
    int mSizeItems;
    private Entry mTail;

    public MutableBitmapPool(int paramInt1, int paramInt2, int paramInt3)
    {
      this.mCapacityItems = paramInt1;
      this.mCapacityBytes = paramInt2;
      int i = Math.min(this.mCapacityBytes, paramInt3);
      this.mLargestItemBytes = i;
      HashMap localHashMap = new HashMap(paramInt1);
      this.mMap = localHashMap;
    }

    private void addToList(Entry paramEntry)
    {
      int i = this.mSizeItems + 1;
      this.mSizeItems = i;
      int j = this.mSizeBytes;
      int k = paramEntry.getSizeBytes();
      int m = j + k;
      this.mSizeBytes = m;
      if (this.mHead == null)
      {
        this.mHead = paramEntry;
        this.mTail = paramEntry;
        return;
      }
      Entry localEntry1 = Entry.access$202(this.mHead, paramEntry);
      Entry localEntry2 = this.mHead;
      Entry localEntry3 = Entry.access$302(paramEntry, localEntry2);
      this.mHead = paramEntry;
    }

    private static int bytesPerPixel(Bitmap.Config paramConfig)
    {
      int i = 2;
      if (paramConfig == null)
      {
        int j = Log.wtf("AlbumArtUtils", "Null bitmap config, returning something huge to prevent caching");
        i = 1024;
      }
      while (true)
      {
        return i;
        int[] arrayOfInt = AlbumArtUtils.3.$SwitchMap$android$graphics$Bitmap$Config;
        int k = paramConfig.ordinal();
        switch (arrayOfInt[k])
        {
        case 2:
        case 4:
        default:
          i = 4;
          break;
        case 1:
          i = 1;
          break;
        case 3:
          i = 4;
        }
      }
    }

    public static int estimatedSize(int paramInt1, int paramInt2, Bitmap.Config paramConfig)
    {
      int i = paramInt1 + 3 & 0xFFFFFFFC;
      int j = bytesPerPixel(paramConfig);
      return i * j * paramInt2;
    }

    private static int getBitmapSizeBytes(Bitmap paramBitmap)
    {
      int i = paramBitmap.getRowBytes();
      int j = paramBitmap.getHeight();
      return i * j;
    }

    private Bitmap removeBitmap(Key paramKey)
    {
      LinkedList localLinkedList = (LinkedList)this.mMap.get(paramKey);
      Entry localEntry;
      if (localLinkedList != null)
      {
        localEntry = (Entry)localLinkedList.removeFirst();
        removeFromList(localEntry);
        if (localLinkedList.size() == 0)
          Object localObject = this.mMap.remove(paramKey);
        AlbumArtUtils.throwIfRecycled(localEntry.mBitmap);
      }
      for (Bitmap localBitmap = localEntry.mBitmap; ; localBitmap = null)
        return localBitmap;
    }

    private void removeFromList(Entry paramEntry)
    {
      int i = this.mSizeItems + -1;
      this.mSizeItems = i;
      int j = this.mSizeBytes;
      int k = paramEntry.getSizeBytes();
      int m = j - k;
      this.mSizeBytes = m;
      Entry localEntry1 = paramEntry.mPrevious;
      Entry localEntry2 = paramEntry.mNext;
      if (this.mHead == paramEntry)
        this.mHead = localEntry2;
      if (this.mTail == paramEntry)
        this.mTail = localEntry1;
      if (localEntry1 != null)
        Entry localEntry3 = Entry.access$302(localEntry1, localEntry2);
      if (localEntry2 == null)
        return;
      Entry localEntry4 = Entry.access$202(localEntry2, localEntry1);
    }

    /** @deprecated */
    public Bitmap createBitmap(int paramInt1, int paramInt2, Bitmap.Config paramConfig)
    {
      try
      {
        Key localKey = new Key(paramInt1, paramInt2, paramConfig);
        Object localObject1 = removeBitmap(localKey);
        if (localObject1 == null)
        {
          int i = localKey.mWidth;
          int j = localKey.mHeight;
          Bitmap.Config localConfig = localKey.mConfig;
          Bitmap localBitmap = Bitmap.createBitmap(i, j, localConfig);
          localObject1 = localBitmap;
        }
        while (true)
        {
          return localObject1;
          Canvas localCanvas = new Canvas((Bitmap)localObject1);
          PorterDuff.Mode localMode = PorterDuff.Mode.CLEAR;
          localCanvas.drawColor(0, localMode);
        }
      }
      finally
      {
      }
    }

    /** @deprecated */
    public void recycleBitmap(Bitmap paramBitmap)
    {
      while (true)
      {
        Key localKey1;
        Key localKey2;
        try
        {
          AlbumArtUtils.throwIfRecycled(paramBitmap);
          Bitmap.Config localConfig = paramBitmap.getConfig();
          int i = getBitmapSizeBytes(paramBitmap);
          if ((localConfig != null) && (paramBitmap.isMutable()))
          {
            int j = this.mLargestItemBytes;
            if (i <= j);
          }
          else
          {
            paramBitmap.recycle();
            return;
          }
          int k = paramBitmap.getWidth();
          int m = paramBitmap.getHeight();
          localKey1 = new Key(k, m, localConfig);
          int n = this.mCapacityItems;
          int i1 = this.mSizeItems;
          if (n != i1)
          {
            int i2 = this.mSizeBytes + i;
            int i3 = this.mCapacityBytes;
            if (i2 <= i3)
              break label166;
          }
          localKey2 = this.mTail.mKey;
          if (localKey2.equals(localKey1))
          {
            paramBitmap.recycle();
            continue;
          }
        }
        finally
        {
        }
        Bitmap localBitmap = removeBitmap(localKey2);
        if (localBitmap != null)
        {
          localBitmap.recycle();
          continue;
          label166: Entry localEntry = new Entry(localKey1, paramBitmap);
          addToList(localEntry);
          LinkedList localLinkedList = (LinkedList)this.mMap.get(localKey1);
          if (localLinkedList == null)
          {
            localLinkedList = new LinkedList();
            Object localObject2 = this.mMap.put(localKey1, localLinkedList);
          }
          boolean bool = localLinkedList.add(localEntry);
        }
      }
    }

    public boolean willCacheBitmap(int paramInt1, int paramInt2, Bitmap.Config paramConfig)
    {
      int i = estimatedSize(paramInt1, paramInt2, paramConfig);
      int j = this.mLargestItemBytes;
      if (i <= j);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    private static class Entry
    {
      public Bitmap mBitmap;
      public AlbumArtUtils.MutableBitmapPool.Key mKey;
      private Entry mNext;
      private Entry mPrevious;

      public Entry(AlbumArtUtils.MutableBitmapPool.Key paramKey, Bitmap paramBitmap)
      {
        this.mKey = paramKey;
        this.mBitmap = paramBitmap;
      }

      public int getSizeBytes()
      {
        return AlbumArtUtils.MutableBitmapPool.getBitmapSizeBytes(this.mBitmap);
      }

      public String toString()
      {
        StringBuilder localStringBuilder1 = new StringBuilder();
        String str = this.mKey.toString();
        StringBuilder localStringBuilder2 = localStringBuilder1.append(str).append(" ");
        int i = getSizeBytes();
        return i;
      }
    }

    private static class Key
    {
      Bitmap.Config mConfig;
      int mHeight;
      int mWidth;

      Key(int paramInt1, int paramInt2, Bitmap.Config paramConfig)
      {
        this.mWidth = paramInt1;
        this.mHeight = paramInt2;
        if (paramConfig == null)
          throw new IllegalArgumentException("config must be non-null");
        this.mConfig = paramConfig;
      }

      public boolean equals(Object paramObject)
      {
        boolean bool = false;
        if ((paramObject instanceof Key))
        {
          Key localKey = (Key)paramObject;
          int i = this.mWidth;
          int j = localKey.mWidth;
          if (i != j)
          {
            int k = this.mHeight;
            int m = localKey.mHeight;
            if (k != m)
            {
              Bitmap.Config localConfig1 = this.mConfig;
              Bitmap.Config localConfig2 = localKey.mConfig;
              if (localConfig1.equals(localConfig2))
                bool = true;
            }
          }
        }
        return bool;
      }

      public int hashCode()
      {
        int i = this.mWidth;
        int j = this.mHeight * 11;
        int k = i ^ j;
        int m = this.mConfig.hashCode();
        return k ^ m;
      }

      public String toString()
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("(");
        int i = this.mWidth;
        StringBuilder localStringBuilder2 = localStringBuilder1.append(i).append(", ");
        int j = this.mHeight;
        StringBuilder localStringBuilder3 = localStringBuilder2.append(j).append(", ");
        Bitmap.Config localConfig = this.mConfig;
        return localConfig + ")";
      }
    }
  }

  private static class MissingArtEntry
  {
    public final AlbumArtUtils.ArtCacheKey key;
    public final Set<Long> missingAlbumIds;

    public MissingArtEntry(AlbumArtUtils.ArtCacheKey paramArtCacheKey, Set<Long> paramSet)
    {
      this.key = paramArtCacheKey;
      this.missingAlbumIds = paramSet;
    }
  }

  private static class AlbumIdTap
    implements AlbumIdSink
  {
    private Set<Long> mIds;
    private AlbumIdSink mTarget;

    AlbumIdTap(AlbumIdSink paramAlbumIdSink)
    {
      this.mTarget = paramAlbumIdSink;
    }

    public Set<Long> extractIds()
    {
      Set localSet = this.mIds;
      this.mIds = null;
      return localSet;
    }

    public void report(Long paramLong)
    {
      if (this.mTarget != null)
        this.mTarget.report(paramLong);
      if (this.mIds == null)
      {
        HashSet localHashSet = new HashSet();
        this.mIds = localHashSet;
      }
      boolean bool = this.mIds.add(paramLong);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.AlbumArtUtils
 * JD-Core Version:    0.6.2
 */