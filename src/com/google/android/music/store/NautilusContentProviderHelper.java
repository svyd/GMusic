package com.google.android.music.store;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.music.cloudclient.AlbumJson;
import com.google.android.music.cloudclient.ArtistJson;
import com.google.android.music.cloudclient.MusicCloud;
import com.google.android.music.cloudclient.MusicCloudImpl;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.sync.google.model.Track;
import com.google.android.music.utils.DbUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.http.client.HttpResponseException;

public class NautilusContentProviderHelper
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.CONTENT_PROVIDER);

  public static long generateLocalId(TagNormalizer paramTagNormalizer, AlbumJson paramAlbumJson)
  {
    String str1 = paramAlbumJson.mAlbumArtist;
    String str2 = getCanonicalString(paramTagNormalizer, str1);
    String str3 = paramAlbumJson.mName;
    String str4 = getCanonicalString(paramTagNormalizer, str3);
    long l;
    if ((str2 == null) || (str4 == null))
      l = 65535L;
    while (true)
    {
      return l;
      StringBuffer localStringBuffer1 = new StringBuffer(256);
      StringBuffer localStringBuffer2 = localStringBuffer1.append(str4).append('\037').append(str2);
      String str5 = localStringBuffer1.toString();
      l = Store.generateId(str5);
      if (LOGV)
      {
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = str5;
        Long localLong = Long.valueOf(l);
        arrayOfObject[1] = localLong;
        String str6 = String.format("Album: key=%s, id=%d", arrayOfObject);
        int i = Log.d("NautilusContentProvider", str6);
      }
    }
  }

  public static long generateLocalId(TagNormalizer paramTagNormalizer, ArtistJson paramArtistJson)
  {
    String str1 = paramArtistJson.mName;
    String str2 = getCanonicalString(paramTagNormalizer, str1);
    long l;
    if (str2 == null)
      l = 65535L;
    while (true)
    {
      return l;
      l = Store.generateId(str2);
      if (LOGV)
      {
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = str2;
        Long localLong = Long.valueOf(l);
        arrayOfObject[1] = localLong;
        String str3 = String.format("Artist: key=%s, id=%d", arrayOfObject);
        int i = Log.d("NautilusContentProvider", str3);
      }
    }
  }

  public static long generateLocalId(TagNormalizer paramTagNormalizer, Track paramTrack)
  {
    String str1 = paramTrack.mAlbumArtist;
    String str2 = getCanonicalString(paramTagNormalizer, str1);
    String str3 = paramTrack.mArtist;
    String str4 = getCanonicalString(paramTagNormalizer, str3);
    String str5 = paramTrack.mAlbum;
    String str6 = getCanonicalString(paramTagNormalizer, str5);
    String str7 = paramTrack.mTitle;
    String str8 = getCanonicalString(paramTagNormalizer, str7);
    long l;
    if ((str2 == null) || (str4 == null) || (str6 == null) || (str8 == null))
      l = 65535L;
    while (true)
    {
      return l;
      StringBuffer localStringBuffer1 = new StringBuffer(256);
      StringBuffer localStringBuffer2 = localStringBuffer1.append(str6).append('\037').append(str2).append('\037').append(str8);
      StringBuffer localStringBuffer3 = localStringBuffer1.append('\037');
      int i = paramTrack.mDiscNumber;
      StringBuffer localStringBuffer4 = localStringBuffer3.append(i).append('\037');
      int j = paramTrack.mTrackNumber;
      StringBuffer localStringBuffer5 = localStringBuffer4.append(j);
      if (!str4.equals(str2))
        StringBuffer localStringBuffer6 = localStringBuffer1.append('\037').append(str4);
      String str9 = localStringBuffer1.toString();
      l = Store.generateId(str9);
      if (LOGV)
      {
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = str9;
        Long localLong = Long.valueOf(l);
        arrayOfObject[1] = localLong;
        String str10 = String.format("Track: key=%s, id=%d", arrayOfObject);
        int k = Log.d("NautilusContentProvider", str10);
      }
    }
  }

  private static AlbumJson getAlbum(Context paramContext, String paramString)
  {
    NautilusContentCache localNautilusContentCache = NautilusContentCache.getInstance(paramContext);
    AlbumJson localAlbumJson = localNautilusContentCache.getAlbum(paramString);
    MusicCloud localMusicCloud;
    if (localAlbumJson == null)
      localMusicCloud = getCloudClient(paramContext);
    try
    {
      localAlbumJson = localMusicCloud.getNautilusAlbum(paramString);
      localNautilusContentCache.putAlbum(localAlbumJson);
      return localAlbumJson;
    }
    catch (HttpResponseException localHttpResponseException)
    {
      while (true)
        if (localHttpResponseException.getStatusCode() == 404)
        {
          String str1 = "Album not found by id:" + paramString + ". Clearing the Recent table.";
          int i = Log.i("NautilusContentProvider", str1);
          boolean bool = RecentItemsManager.deleteNautilusAlbum(paramContext, paramString);
        }
        else
        {
          String str2 = localHttpResponseException.getMessage();
          int j = Log.w("NautilusContentProvider", str2, localHttpResponseException);
        }
    }
    catch (InterruptedException localInterruptedException)
    {
      while (true)
      {
        String str3 = localInterruptedException.getMessage();
        int k = Log.w("NautilusContentProvider", str3, localInterruptedException);
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        String str4 = localIOException.getMessage();
        int m = Log.w("NautilusContentProvider", str4, localIOException);
      }
    }
  }

  private static ArtistJson getArtist(Context paramContext, String paramString)
  {
    NautilusContentCache localNautilusContentCache = NautilusContentCache.getInstance(paramContext);
    ArtistJson localArtistJson = localNautilusContentCache.getArtist(paramString);
    MusicCloud localMusicCloud;
    int i;
    int j;
    boolean bool;
    if (localArtistJson == null)
    {
      localMusicCloud = getCloudClient(paramContext);
      i = 20;
      j = 20;
      bool = true;
    }
    try
    {
      localArtistJson = localMusicCloud.getNautilusArtist(paramString, i, j, bool);
      localNautilusContentCache.putArtist(localArtistJson);
      return localArtistJson;
    }
    catch (InterruptedException localInterruptedException)
    {
      while (true)
      {
        String str1 = localInterruptedException.getMessage();
        int k = Log.w("NautilusContentProvider", str1, localInterruptedException);
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        String str2 = localIOException.getMessage();
        int m = Log.w("NautilusContentProvider", str2, localIOException);
      }
    }
  }

  private static String getArtistArtUrl(Context paramContext, String paramString)
  {
    NautilusContentCache localNautilusContentCache = NautilusContentCache.getInstance(paramContext);
    String str1 = localNautilusContentCache.getArtistArtUrl(paramString);
    if (!TextUtils.isEmpty(str1));
    while (true)
    {
      return str1;
      ArtistJson localArtistJson = localNautilusContentCache.getArtist(paramString);
      if (localArtistJson != null)
      {
        str1 = localArtistJson.mArtistArtRef;
        continue;
      }
      MusicCloud localMusicCloud = getCloudClient(paramContext);
      int i = 0;
      int j = 0;
      boolean bool = false;
      try
      {
        localArtistJson = localMusicCloud.getNautilusArtist(paramString, i, j, bool);
        if (localArtistJson != null)
        {
          String str2 = localArtistJson.mArtistArtRef;
          localNautilusContentCache.putArtistArtUrl(paramString, str2);
          str1 = localArtistJson.mArtistArtRef;
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        String str3 = localInterruptedException.getMessage();
        int k = Log.w("NautilusContentProvider", str3, localInterruptedException);
        str1 = null;
      }
      catch (IOException localIOException)
      {
        while (true)
        {
          String str4 = localIOException.getMessage();
          int m = Log.w("NautilusContentProvider", str4, localIOException);
        }
      }
    }
  }

  private static String getCanonicalString(TagNormalizer paramTagNormalizer, String paramString)
  {
    if (paramString != null);
    for (String str = paramTagNormalizer.normalize(paramString); ; str = null)
    {
      if ((TextUtils.isEmpty(str)) && (paramString != null))
        str = paramString;
      return str;
    }
  }

  private static MusicCloud getCloudClient(Context paramContext)
  {
    return new MusicCloudImpl(paramContext);
  }

  private static Track getTrack(Context paramContext, String paramString)
  {
    NautilusContentCache localNautilusContentCache = NautilusContentCache.getInstance(paramContext);
    Track localTrack = localNautilusContentCache.getTrack(paramString);
    MusicCloud localMusicCloud;
    if (localTrack == null)
      localMusicCloud = getCloudClient(paramContext);
    try
    {
      localTrack = localMusicCloud.getNautilusTrack(paramString);
      localNautilusContentCache.putTrack(localTrack);
      return localTrack;
    }
    catch (InterruptedException localInterruptedException)
    {
      while (true)
      {
        String str1 = localInterruptedException.getMessage();
        int i = Log.w("NautilusContentProvider", str1, localInterruptedException);
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        String str2 = localIOException.getMessage();
        int j = Log.w("NautilusContentProvider", str2, localIOException);
      }
    }
  }

  public static Uri insert(Context paramContext, Store paramStore, int paramInt, Uri paramUri, String paramString)
  {
    boolean bool1 = Boolean.parseBoolean(paramUri.getQueryParameter("addToLibrary"));
    Uri localUri1 = null;
    switch (paramInt)
    {
    case 304:
    case 305:
    default:
    case 301:
    case 306:
    case 302:
    case 303:
    }
    while (true)
    {
      if ((localUri1 != null) && (bool1))
      {
        ContentResolver localContentResolver = paramContext.getContentResolver();
        Uri localUri2 = MusicContent.CONTENT_URI;
        localContentResolver.notifyChange(localUri2, null);
        RecentItemsManager.updateRecentItemsAsync(paramContext);
      }
      Uri localUri3 = localUri1;
      ArtistJson localArtistJson;
      while (true)
      {
        return localUri3;
        Context localContext1 = paramContext;
        String str1 = paramString;
        Track localTrack1 = getTrack(localContext1, str1);
        if (localTrack1 == null)
        {
          StringBuilder localStringBuilder1 = new StringBuilder().append("Couldn't get the track for id: ");
          String str2 = paramString;
          String str3 = str2;
          int i = Log.w("NautilusContentProvider", str3);
          localUri3 = null;
        }
        else
        {
          Track[] arrayOfTrack = new Track[1];
          arrayOfTrack[0] = localTrack1;
          ArrayList localArrayList1 = Lists.newArrayList(arrayOfTrack);
          Context localContext2 = paramContext;
          Store localStore1 = paramStore;
          localUri1 = insertTracks(localContext2, localStore1, localArrayList1, bool1);
          break;
          LinkedList localLinkedList = new LinkedList();
          String[] arrayOfString = paramString.split(",");
          int j = arrayOfString.length;
          Object localObject = null;
          if (localObject < j)
          {
            String str4 = arrayOfString[localObject];
            Track localTrack2 = getTrack(paramContext, str4);
            if (localTrack2 == null)
            {
              String str5 = "Couldn't get the track for id: " + str4;
              int m = Log.w("NautilusContentProvider", str5);
            }
            while (true)
            {
              int k = localObject + 1;
              break;
              boolean bool2 = localLinkedList.add(localTrack2);
            }
          }
          ArrayList localArrayList2 = Lists.newArrayList(localLinkedList);
          Context localContext3 = paramContext;
          Store localStore2 = paramStore;
          localUri1 = insertTracks(localContext3, localStore2, localArrayList2, bool1);
          break;
          Context localContext4 = paramContext;
          String str6 = paramString;
          AlbumJson localAlbumJson1 = getAlbum(localContext4, str6);
          if (localAlbumJson1 == null)
          {
            StringBuilder localStringBuilder2 = new StringBuilder().append("Couldn't get the album for id: ");
            String str7 = paramString;
            String str8 = str7;
            int n = Log.w("NautilusContentProvider", str8);
            localUri3 = null;
          }
          else if ((localAlbumJson1.mTracks == null) || (localAlbumJson1.mTracks.size() == 0))
          {
            StringBuilder localStringBuilder3 = new StringBuilder().append("The album doesn't contain any tracks: ");
            String str9 = paramString;
            String str10 = str9;
            int i1 = Log.w("NautilusContentProvider", str10);
            localUri3 = null;
          }
          else
          {
            List localList1 = localAlbumJson1.mTracks;
            Context localContext5 = paramContext;
            Store localStore3 = paramStore;
            localUri1 = insertTracks(localContext5, localStore3, localList1, bool1);
            break;
            Context localContext6 = paramContext;
            String str11 = paramString;
            localArtistJson = getArtist(localContext6, str11);
            if (localArtistJson == null)
            {
              StringBuilder localStringBuilder4 = new StringBuilder().append("Couldn't get the artist for id: ");
              String str12 = paramString;
              String str13 = str12;
              int i2 = Log.w("NautilusContentProvider", str13);
              localUri3 = null;
            }
            else
            {
              if ((localArtistJson.mAlbums != null) && (localArtistJson.mAlbums.size() != 0))
                break label639;
              StringBuilder localStringBuilder5 = new StringBuilder().append("The artist doesn't contain any albums: ");
              String str14 = paramString;
              String str15 = str14;
              int i3 = Log.w("NautilusContentProvider", str15);
              localUri3 = null;
            }
          }
        }
      }
      label639: ArrayList localArrayList3 = Lists.newArrayList();
      Iterator localIterator = localArtistJson.mAlbums.iterator();
      while (localIterator.hasNext())
      {
        AlbumJson localAlbumJson2 = (AlbumJson)localIterator.next();
        List localList2 = localAlbumJson2.mTracks;
        if (localList2 == null)
        {
          String str16 = localAlbumJson2.mAlbumId;
          localAlbumJson2 = getAlbum(paramContext, str16);
          if (localAlbumJson2 != null)
            localList2 = localAlbumJson2.mTracks;
        }
        if (localList2 != null)
          boolean bool3 = localArrayList3.addAll(localList2);
      }
      Context localContext7 = paramContext;
      Store localStore4 = paramStore;
      localUri1 = insertTracks(localContext7, localStore4, localArrayList3, bool1);
    }
  }

  static Uri insertTracks(Context paramContext, Store paramStore, List<Track> paramList, boolean paramBoolean)
  {
    Uri localUri = null;
    Object localObject1 = new Object();
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, localObject1);
    while (true)
    {
      Account localAccount2;
      try
      {
        Account localAccount1 = localMusicPreferences.getStreamingAccount();
        localAccount2 = localAccount1;
        MusicPreferences.releaseMusicPreferences(localObject1);
        if (localAccount2 == null)
          return localUri;
      }
      finally
      {
        MusicPreferences.releaseMusicPreferences(localObject1);
      }
      int i = Store.computeAccountHash(localAccount2);
      List localList = paramStore.tryToInsertOrUpdateExternalSongs(i, paramList, paramBoolean);
      if ((localList != null) && (localList.size() > 0))
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Successfully inserted ");
        int j = localList.size();
        String str = j + " tracks.";
        int k = Log.i("NautilusContentProvider", str);
        localUri = MusicContent.XAudio.getSelectedAudioUri(localList);
      }
    }
  }

  public static Cursor merge(Cursor paramCursor1, Cursor paramCursor2, String paramString)
  {
    HashMap localHashMap = new HashMap();
    int i = paramCursor2.getColumnIndexOrThrow(paramString);
    while (paramCursor2.moveToNext())
      if (!paramCursor2.isNull(i))
      {
        String str1 = paramCursor2.getString(i);
        Integer localInteger1 = Integer.valueOf(paramCursor2.getPosition());
        Object localObject = localHashMap.put(str1, localInteger1);
      }
    String[] arrayOfString = paramCursor1.getColumnNames();
    MatrixCursor localMatrixCursor = new MatrixCursor(arrayOfString);
    while (paramCursor1.moveToNext())
    {
      if (!paramCursor1.isNull(i));
      Integer localInteger2;
      for (String str2 = paramCursor1.getString(i); ; str2 = null)
      {
        localInteger2 = (Integer)localHashMap.get(str2);
        if (localInteger2 == null)
          break label204;
        int j = localInteger2.intValue();
        if (!paramCursor2.moveToPosition(j))
          break label169;
        DbUtils.addRowToMatrixCursor(localMatrixCursor, paramCursor2);
        break;
      }
      label169: String str3 = "Failed to move the source cursor to position: " + localInteger2;
      int k = Log.wtf("NautilusContentProvider", str3);
      continue;
      label204: DbUtils.addRowToMatrixCursor(localMatrixCursor, paramCursor1);
    }
    return localMatrixCursor;
  }

  public static Cursor merge(Cursor[] paramArrayOfCursor)
  {
    int i = 1;
    Object localObject;
    if ((paramArrayOfCursor == null) || (paramArrayOfCursor.length == 0))
      localObject = null;
    while (true)
    {
      return localObject;
      if (paramArrayOfCursor.length == 1)
      {
        localObject = paramArrayOfCursor[0];
      }
      else
      {
        String[] arrayOfString = paramArrayOfCursor[0].getColumnNames();
        Cursor[] arrayOfCursor;
        int k;
        int m;
        if (MusicContentProvider.hasCount(arrayOfString))
        {
          int j = 0;
          arrayOfCursor = paramArrayOfCursor;
          k = arrayOfCursor.length;
          m = 0;
          while (m < k)
          {
            Cursor localCursor1 = arrayOfCursor[m];
            if (localCursor1.moveToFirst())
            {
              long l1 = j;
              long l2 = localCursor1.getLong(0);
              j = (int)(l1 + l2);
            }
            m += 1;
          }
          localObject = new MatrixCursor(arrayOfString);
          Object[] arrayOfObject1 = new Object[1];
          Integer localInteger1 = Integer.valueOf(j);
          arrayOfObject1[0] = localInteger1;
          ((MatrixCursor)localObject).addRow(arrayOfObject1);
        }
        else if (ProjectionUtils.isHasDifferentArtistProjection(arrayOfString))
        {
          int n = 0;
          arrayOfCursor = paramArrayOfCursor;
          k = arrayOfCursor.length;
          m = 0;
          while (m < k)
          {
            Cursor localCursor2 = arrayOfCursor[m];
            if (localCursor2.moveToFirst())
            {
              int i1 = localCursor2.getInt(0);
              n += i1;
            }
            int i2 = m + 1;
          }
          MatrixCursor localMatrixCursor = new MatrixCursor(arrayOfString);
          Object[] arrayOfObject2 = new Object[1];
          if (n == 0)
            i = 0;
          Integer localInteger2 = Integer.valueOf(i);
          arrayOfObject2[0] = localInteger2;
          localMatrixCursor.addRow(arrayOfObject2);
          localObject = localMatrixCursor;
        }
        else
        {
          localObject = new CustomMergeCursor(paramArrayOfCursor);
        }
      }
    }
  }

  public static NautilusQueryResult query(ThreadPoolExecutor paramThreadPoolExecutor, Context paramContext, int paramInt, Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    Context localContext = paramContext;
    int i = paramInt;
    Uri localUri = paramUri;
    String str1 = paramString;
    String[] arrayOfString = paramArrayOfString;
    NautilusQueryTask localNautilusQueryTask = new NautilusQueryTask(localContext, i, localUri, str1, arrayOfString);
    Future localFuture = paramThreadPoolExecutor.submit(localNautilusQueryTask);
    try
    {
      TimeUnit localTimeUnit = TimeUnit.SECONDS;
      NautilusQueryResult localNautilusQueryResult = (NautilusQueryResult)localFuture.get(30L, localTimeUnit);
      if (localNautilusQueryResult == null)
      {
        MatrixCursor localMatrixCursor = new MatrixCursor(paramArrayOfString);
        LinkedList localLinkedList = new LinkedList();
        localNautilusQueryResult = new NautilusQueryResult(localMatrixCursor, 65535L, localLinkedList);
      }
      return localNautilusQueryResult;
    }
    catch (Exception localException)
    {
      while (true)
      {
        String str2 = localException.getMessage();
        int j = Log.w("NautilusContentProvider", str2, localException);
      }
    }
  }

  private static class NautilusQueryTask
    implements Callable<NautilusContentProviderHelper.NautilusQueryResult>
  {
    private Context mContext;
    private String mNautilusId;
    private String[] mProjection;
    private Uri mUri;
    private int mUriType;

    public NautilusQueryTask(Context paramContext, int paramInt, Uri paramUri, String paramString, String[] paramArrayOfString)
    {
      this.mContext = paramContext;
      this.mUriType = paramInt;
      this.mUri = paramUri;
      this.mNautilusId = paramString;
      this.mProjection = paramArrayOfString;
    }

    public NautilusContentProviderHelper.NautilusQueryResult call()
      throws Exception
    {
      TagNormalizer localTagNormalizer = new TagNormalizer();
      String[] arrayOfString1 = this.mProjection;
      MatrixCursor localMatrixCursor = new MatrixCursor(arrayOfString1);
      long l = 65535L;
      LinkedList localLinkedList = new LinkedList();
      switch (this.mUriType)
      {
      default:
      case 301:
      case 401:
      case 302:
      case 501:
      case 303:
      case 502:
      case 504:
      case 503:
      case 505:
      }
      while (true)
      {
        if (localMatrixCursor != null)
        {
          ContentResolver localContentResolver1 = this.mContext.getContentResolver();
          Uri localUri1 = this.mUri;
          ContentResolver localContentResolver2 = localContentResolver1;
          Uri localUri2 = localUri1;
          localMatrixCursor.setNotificationUri(localContentResolver2, localUri2);
          int i = localMatrixCursor.getCount();
        }
        NautilusContentProviderHelper.NautilusQueryResult localNautilusQueryResult = new com/google/android/music/store/NautilusContentProviderHelper$NautilusQueryResult;
        localNautilusQueryResult.<init>(localMatrixCursor, l, localLinkedList);
        return localNautilusQueryResult;
        Context localContext1 = this.mContext;
        String str1 = this.mNautilusId;
        Track localTrack1 = NautilusContentProviderHelper.getTrack(localContext1, str1);
        String[] arrayOfString2 = this.mProjection;
        Track localTrack2 = localTrack1;
        String[] arrayOfString3 = arrayOfString2;
        Object[] arrayOfObject1 = ProjectionUtils.project(localTrack2, arrayOfString3);
        localMatrixCursor.addRow(arrayOfObject1);
        Track localTrack3 = localTrack1;
        l = NautilusContentProviderHelper.generateLocalId(localTagNormalizer, localTrack3);
        continue;
        Context localContext2 = this.mContext;
        String str2 = this.mNautilusId;
        AlbumJson localAlbumJson1 = NautilusContentProviderHelper.getAlbum(localContext2, str2);
        int j;
        String str3;
        Iterator localIterator1;
        label297: Track localTrack4;
        int m;
        if (ProjectionUtils.isHasDifferentArtistProjection(this.mProjection))
        {
          j = 0;
          str3 = null;
          localIterator1 = j.mTracks.iterator();
          if (localIterator1.hasNext())
          {
            localTrack4 = (Track)localIterator1.next();
            if (str3 != null)
            {
              String str4 = localTrack4.mArtist;
              if (!str3.equals(str4))
                j = 1;
            }
          }
          else
          {
            Object[] arrayOfObject2 = new Object[1];
            int k = 0;
            if (j == 0)
              break label406;
            m = 1;
            label361: Integer localInteger1 = Integer.valueOf(m);
            arrayOfObject2[k] = localInteger1;
            Object[] arrayOfObject3 = arrayOfObject2;
            localMatrixCursor.addRow(arrayOfObject3);
          }
        }
        while (true)
        {
          l = NautilusContentProviderHelper.generateLocalId(localTagNormalizer, j);
          break;
          str3 = localTrack4.mArtist;
          break label297;
          label406: m = 0;
          break label361;
          String[] arrayOfString4 = this.mProjection;
          Object[] arrayOfObject4 = ProjectionUtils.project(j, arrayOfString4);
          localMatrixCursor.addRow(arrayOfObject4);
        }
        Context localContext3 = this.mContext;
        String str5 = this.mNautilusId;
        AlbumJson localAlbumJson2 = NautilusContentProviderHelper.getAlbum(localContext3, str5);
        if ((localAlbumJson2 != null) && (localAlbumJson2.mTracks != null))
        {
          if (MusicContentProvider.hasCount(this.mProjection))
          {
            Object[] arrayOfObject5 = new Object[1];
            Integer localInteger2 = Integer.valueOf(localAlbumJson2.mTracks.size());
            arrayOfObject5[0] = localInteger2;
            Object[] arrayOfObject6 = arrayOfObject5;
            localMatrixCursor.addRow(arrayOfObject6);
            localIterator1 = localAlbumJson2.mTracks.iterator();
            while (localIterator1.hasNext())
            {
              Track localTrack5 = (Track)localIterator1.next();
              Long localLong1 = Long.valueOf(NautilusContentProviderHelper.generateLocalId(localTagNormalizer, localTrack5));
              boolean bool1 = localLinkedList.add(localLong1);
            }
          }
          localIterator1 = localAlbumJson2.mTracks.iterator();
          while (localIterator1.hasNext())
          {
            Track localTrack6 = (Track)localIterator1.next();
            String[] arrayOfString5 = this.mProjection;
            Track localTrack7 = localTrack6;
            String[] arrayOfString6 = arrayOfString5;
            Object[] arrayOfObject7 = ProjectionUtils.project(localTrack7, arrayOfString6);
            localMatrixCursor.addRow(arrayOfObject7);
            Track localTrack8 = localTrack6;
            Long localLong2 = Long.valueOf(NautilusContentProviderHelper.generateLocalId(localTagNormalizer, localTrack8));
            boolean bool2 = localLinkedList.add(localLong2);
          }
          l = NautilusContentProviderHelper.generateLocalId(localTagNormalizer, localAlbumJson2);
          continue;
          Context localContext4 = this.mContext;
          String str6 = this.mNautilusId;
          ArtistJson localArtistJson1 = NautilusContentProviderHelper.getArtist(localContext4, str6);
          String[] arrayOfString7 = this.mProjection;
          Object[] arrayOfObject8 = ProjectionUtils.project(localArtistJson1, arrayOfString7);
          localMatrixCursor.addRow(arrayOfObject8);
          l = NautilusContentProviderHelper.generateLocalId(localTagNormalizer, localArtistJson1);
          continue;
          Context localContext5 = this.mContext;
          String str7 = this.mNautilusId;
          ArtistJson localArtistJson2 = NautilusContentProviderHelper.getArtist(localContext5, str7);
          if ((localArtistJson2 != null) && (localArtistJson2.mAlbums != null))
            if (MusicContentProvider.hasCount(this.mProjection))
            {
              int n = 0;
              localIterator1 = localArtistJson2.mAlbums.iterator();
              while (localIterator1.hasNext())
              {
                AlbumJson localAlbumJson3 = (AlbumJson)localIterator1.next();
                List localList1 = localAlbumJson3.mTracks;
                if (localList1 == null)
                {
                  Context localContext6 = this.mContext;
                  String str8 = localAlbumJson3.mAlbumId;
                  localAlbumJson3 = NautilusContentProviderHelper.getAlbum(localContext6, str8);
                  if (localAlbumJson3 != null)
                    localList1 = localAlbumJson3.mTracks;
                }
                if (localList1 != null)
                {
                  int i1 = localList1.size();
                  n += i1;
                }
              }
              Object[] arrayOfObject9 = new Object[1];
              Integer localInteger3 = Integer.valueOf(n);
              arrayOfObject9[0] = localInteger3;
              Object[] arrayOfObject10 = arrayOfObject9;
              localMatrixCursor.addRow(arrayOfObject10);
              break label935;
              label912: l = NautilusContentProviderHelper.generateLocalId(localTagNormalizer, localArtistJson2);
            }
            else
            {
              localIterator1 = localArtistJson2.mAlbums.iterator();
              while (true)
              {
                label935: if (!localIterator1.hasNext())
                  break label912;
                AlbumJson localAlbumJson4 = (AlbumJson)localIterator1.next();
                List localList2 = localAlbumJson4.mTracks;
                if (localList2 == null)
                {
                  Context localContext7 = this.mContext;
                  String str9 = localAlbumJson4.mAlbumId;
                  localAlbumJson4 = NautilusContentProviderHelper.getAlbum(localContext7, str9);
                  if (localAlbumJson4 != null)
                    localList2 = localAlbumJson4.mTracks;
                }
                if (localList2 == null)
                  break;
                Iterator localIterator2 = localList2.iterator();
                while (localIterator2.hasNext())
                {
                  Track localTrack9 = (Track)localIterator2.next();
                  String[] arrayOfString8 = this.mProjection;
                  Track localTrack10 = localTrack9;
                  String[] arrayOfString9 = arrayOfString8;
                  Object[] arrayOfObject11 = ProjectionUtils.project(localTrack10, arrayOfString9);
                  localMatrixCursor.addRow(arrayOfObject11);
                  Track localTrack11 = localTrack9;
                  Long localLong3 = Long.valueOf(NautilusContentProviderHelper.generateLocalId(localTagNormalizer, localTrack11));
                  boolean bool3 = localLinkedList.add(localLong3);
                }
              }
              Context localContext8 = this.mContext;
              String str10 = this.mNautilusId;
              localArtistJson2 = NautilusContentProviderHelper.getArtist(localContext8, str10);
              if ((localArtistJson2 != null) && (localArtistJson2.mAlbums != null))
              {
                if (MusicContentProvider.hasCount(this.mProjection))
                {
                  Object[] arrayOfObject12 = new Object[1];
                  Integer localInteger4 = Integer.valueOf(localArtistJson2.mAlbums.size());
                  arrayOfObject12[0] = localInteger4;
                  Object[] arrayOfObject13 = arrayOfObject12;
                  localMatrixCursor.addRow(arrayOfObject13);
                  localIterator1 = localArtistJson2.mAlbums.iterator();
                  while (localIterator1.hasNext())
                  {
                    AlbumJson localAlbumJson5 = (AlbumJson)localIterator1.next();
                    Long localLong4 = Long.valueOf(NautilusContentProviderHelper.generateLocalId(localTagNormalizer, localAlbumJson5));
                    boolean bool4 = localLinkedList.add(localLong4);
                  }
                }
                localIterator1 = localArtistJson2.mAlbums.iterator();
                while (localIterator1.hasNext())
                {
                  AlbumJson localAlbumJson6 = (AlbumJson)localIterator1.next();
                  String[] arrayOfString10 = this.mProjection;
                  Object[] arrayOfObject14 = ProjectionUtils.project(localAlbumJson6, arrayOfString10);
                  localMatrixCursor.addRow(arrayOfObject14);
                  Long localLong5 = Long.valueOf(NautilusContentProviderHelper.generateLocalId(localTagNormalizer, localAlbumJson6));
                  boolean bool5 = localLinkedList.add(localLong5);
                }
                l = NautilusContentProviderHelper.generateLocalId(localTagNormalizer, localArtistJson2);
                continue;
                Context localContext9 = this.mContext;
                String str11 = this.mNautilusId;
                localArtistJson2 = NautilusContentProviderHelper.getArtist(localContext9, str11);
                if ((localArtistJson2 != null) && (localArtistJson2.mRelatedArtists != null))
                {
                  if (MusicContentProvider.hasCount(this.mProjection))
                  {
                    Object[] arrayOfObject15 = new Object[1];
                    Integer localInteger5 = Integer.valueOf(localArtistJson2.mRelatedArtists.size());
                    arrayOfObject15[0] = localInteger5;
                    Object[] arrayOfObject16 = arrayOfObject15;
                    localMatrixCursor.addRow(arrayOfObject16);
                  }
                  while (true)
                  {
                    l = NautilusContentProviderHelper.generateLocalId(localTagNormalizer, localArtistJson2);
                    break;
                    Iterator localIterator3 = localArtistJson2.mRelatedArtists.iterator();
                    while (localIterator3.hasNext())
                    {
                      ArtistJson localArtistJson3 = (ArtistJson)localIterator3.next();
                      String[] arrayOfString11 = this.mProjection;
                      Object[] arrayOfObject17 = ProjectionUtils.project(localArtistJson3, arrayOfString11);
                      localMatrixCursor.addRow(arrayOfObject17);
                    }
                  }
                  Context localContext10 = this.mContext;
                  String str12 = this.mNautilusId;
                  localArtistJson2 = NautilusContentProviderHelper.getArtist(localContext10, str12);
                  if ((localArtistJson2 != null) && (localArtistJson2.mTopTracks != null))
                  {
                    if (MusicContentProvider.hasCount(this.mProjection))
                    {
                      Object[] arrayOfObject18 = new Object[1];
                      Integer localInteger6 = Integer.valueOf(localArtistJson2.mTopTracks.size());
                      arrayOfObject18[0] = localInteger6;
                      Object[] arrayOfObject19 = arrayOfObject18;
                      localMatrixCursor.addRow(arrayOfObject19);
                    }
                    while (true)
                    {
                      l = NautilusContentProviderHelper.generateLocalId(localTagNormalizer, localArtistJson2);
                      break;
                      Iterator localIterator4 = localArtistJson2.mTopTracks.iterator();
                      while (localIterator4.hasNext())
                      {
                        Track localTrack12 = (Track)localIterator4.next();
                        String[] arrayOfString12 = this.mProjection;
                        Track localTrack13 = localTrack12;
                        String[] arrayOfString13 = arrayOfString12;
                        Object[] arrayOfObject20 = ProjectionUtils.project(localTrack13, arrayOfString13);
                        localMatrixCursor.addRow(arrayOfObject20);
                      }
                    }
                    int i2 = this.mProjection.length;
                    int i3 = 1;
                    if (i2 > i3)
                    {
                      int i4 = Log.e("NautilusContentProvider", "Only one column is supported for querying artist art url.");
                    }
                    else
                    {
                      Context localContext11 = this.mContext;
                      String str13 = this.mNautilusId;
                      String str14 = NautilusContentProviderHelper.getArtistArtUrl(localContext11, str13);
                      if (!TextUtils.isEmpty(str14))
                      {
                        Object[] arrayOfObject21 = new Object[1];
                        arrayOfObject21[0] = str14;
                        Object[] arrayOfObject22 = arrayOfObject21;
                        localMatrixCursor.addRow(arrayOfObject22);
                      }
                    }
                  }
                }
              }
            }
        }
      }
    }
  }

  public static class NautilusQueryResult
  {
    private final Cursor mCursor;
    private final List<Long> mExcludeIds;
    private final long mLocalId;

    public NautilusQueryResult(Cursor paramCursor, long paramLong, List<Long> paramList)
    {
      this.mCursor = paramCursor;
      this.mLocalId = paramLong;
      this.mExcludeIds = paramList;
    }

    public Cursor getCursor()
    {
      return this.mCursor;
    }

    public String getExcludeClause(String paramString)
    {
      List localList = this.mExcludeIds;
      return DbUtils.getNotInClause(paramString, localList);
    }

    public long getLocalId()
    {
      return this.mLocalId;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.NautilusContentProviderHelper
 * JD-Core Version:    0.6.2
 */