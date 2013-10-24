package com.google.android.music.store;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gsf.Gservices;
import com.google.android.music.cloudclient.AlbumJson;
import com.google.android.music.cloudclient.ExploreEntityGroupJson;
import com.google.android.music.cloudclient.ExploreEntityJson;
import com.google.android.music.cloudclient.MusicCloud;
import com.google.android.music.cloudclient.MusicCloudImpl;
import com.google.android.music.cloudclient.MusicGenreJson;
import com.google.android.music.cloudclient.MusicGenresResponseJson;
import com.google.android.music.cloudclient.TabJson;
import com.google.android.music.sync.api.MusicUrl.ExploreTabType;
import com.google.android.music.sync.google.model.SyncablePlaylist;
import com.google.android.music.sync.google.model.Track;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ExploreContentProviderHelper
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.CONTENT_PROVIDER);

  public static MusicGenresResponseJson getGenres(Context paramContext, MusicCloud paramMusicCloud, String paramString)
  {
    NautilusContentCache localNautilusContentCache = NautilusContentCache.getInstance(paramContext);
    MusicGenresResponseJson localMusicGenresResponseJson1 = localNautilusContentCache.getMusicGenresResponse(paramString);
    if (localMusicGenresResponseJson1 == null);
    try
    {
      localMusicGenresResponseJson1 = paramMusicCloud.getGenres(paramString);
      if ((localMusicGenresResponseJson1 == null) || (localMusicGenresResponseJson1.mGenres == null) || (localMusicGenresResponseJson1.mGenres.isEmpty()) || (((MusicGenreJson)localMusicGenresResponseJson1.mGenres.get(0)).mId == null))
        int i = Log.w("ExploreContentProviderHelper", "Incomplete data");
      for (localMusicGenresResponseJson2 = null; ; localMusicGenresResponseJson2 = localMusicGenresResponseJson1)
      {
        return localMusicGenresResponseJson2;
        localNautilusContentCache.putMusicGenresResponse(paramString, localMusicGenresResponseJson1);
      }
    }
    catch (InterruptedException localInterruptedException)
    {
      while (true)
      {
        String str1 = localInterruptedException.getMessage();
        int j = Log.w("ExploreContentProviderHelper", str1, localInterruptedException);
        MusicGenresResponseJson localMusicGenresResponseJson2 = localMusicGenresResponseJson1;
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        String str2 = localIOException.getMessage();
        int k = Log.w("ExploreContentProviderHelper", str2, localIOException);
      }
    }
  }

  private static TabJson getTab(Context paramContext, MusicCloud paramMusicCloud, MusicUrl.ExploreTabType paramExploreTabType, String paramString)
  {
    int i = Gservices.getInt(paramContext.getContentResolver(), "music_explore_num_entities", 25);
    TabJson localTabJson = NautilusContentCache.getInstance(paramContext).getExploreResponse(paramExploreTabType, paramString);
    if (localTabJson == null);
    try
    {
      localTabJson = paramMusicCloud.getTab(paramExploreTabType, i, paramString);
      NautilusContentCache.getInstance(paramContext).putExploreResponse(paramExploreTabType, paramString, localTabJson);
      return localTabJson;
    }
    catch (InterruptedException localInterruptedException)
    {
      while (true)
      {
        String str1 = localInterruptedException.getMessage();
        int j = Log.w("ExploreContentProviderHelper", str1, localInterruptedException);
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        String str2 = localIOException.getMessage();
        int k = Log.w("ExploreContentProviderHelper", str2, localIOException);
      }
    }
  }

  private static MusicUrl.ExploreTabType getTabType(int paramInt)
  {
    MusicUrl.ExploreTabType localExploreTabType;
    switch (paramInt)
    {
    default:
      String str = "Invalid Uri type: " + paramInt;
      throw new IllegalArgumentException(str);
    case 1620:
    case 1621:
      localExploreTabType = MusicUrl.ExploreTabType.NEW_RELEASES;
    case 1610:
    case 1611:
    case 1600:
    case 1601:
    }
    while (true)
    {
      return localExploreTabType;
      localExploreTabType = MusicUrl.ExploreTabType.TOP_CHARTS;
      continue;
      localExploreTabType = MusicUrl.ExploreTabType.RECOMMENDED;
    }
  }

  private static void invalidateCache(Context paramContext, MusicUrl.ExploreTabType paramExploreTabType, String paramString)
  {
    NautilusContentCache.getInstance(paramContext).removeExploreResponse(paramExploreTabType, paramString);
  }

  public static Cursor query(Context paramContext, Uri paramUri, int paramInt, String[] paramArrayOfString)
  {
    Context localContext1 = paramContext;
    MusicCloudImpl localMusicCloudImpl = new MusicCloudImpl(localContext1);
    String[] arrayOfString1 = paramArrayOfString;
    MatrixCursor localMatrixCursor = new MatrixCursor(arrayOfString1);
    boolean bool1 = Gservices.getBoolean(paramContext.getContentResolver(), "music_enable_shared_playlists", true);
    String str1 = null;
    switch (paramInt)
    {
    default:
    case 1600:
    case 1610:
    case 1620:
    case 1601:
    case 1611:
    case 1621:
    case 1631:
    case 1630:
    }
    while (true)
    {
      return localMatrixCursor;
      MusicUrl.ExploreTabType localExploreTabType1 = getTabType(paramInt);
      Uri localUri1 = paramUri;
      String str2 = "genreid";
      str1 = localUri1.getQueryParameter(str2);
      Context localContext2 = paramContext;
      MusicUrl.ExploreTabType localExploreTabType2 = localExploreTabType1;
      Object localObject = getTab(localContext2, localMusicCloudImpl, localExploreTabType2, str1);
      if ((localObject == null) || (((TabJson)localObject).mGroups == null) || (((TabJson)localObject).mGroups.isEmpty()))
      {
        int i = Log.w("ExploreContentProviderHelper", "Incomplete data");
        Context localContext3 = paramContext;
        MusicUrl.ExploreTabType localExploreTabType3 = localExploreTabType1;
        invalidateCache(localContext3, localExploreTabType3, str1);
        localMatrixCursor = null;
      }
      else
      {
        LinkedList localLinkedList = new LinkedList();
        Iterator localIterator = ((TabJson)localObject).mGroups.iterator();
        while (localIterator.hasNext())
        {
          ExploreEntityGroupJson localExploreEntityGroupJson1 = (ExploreEntityGroupJson)localIterator.next();
          boolean bool2 = localLinkedList.add(localExploreEntityGroupJson1);
        }
        if (MusicContentProvider.hasCount(paramArrayOfString))
        {
          Object[] arrayOfObject1 = new Object[1];
          Integer localInteger1 = Integer.valueOf(localLinkedList.size());
          arrayOfObject1[0] = localInteger1;
          Object[] arrayOfObject2 = arrayOfObject1;
          localMatrixCursor.addRow(arrayOfObject2);
        }
        else
        {
          int j = 0;
          while (true)
          {
            int k = localLinkedList.size();
            if (j >= k)
              break;
            ExploreEntityGroupJson localExploreEntityGroupJson2 = (ExploreEntityGroupJson)localLinkedList.get(j);
            long l = j;
            localExploreEntityGroupJson2.mId = l;
            String[] arrayOfString2 = paramArrayOfString;
            Object[] arrayOfObject3 = ProjectionUtils.project(localExploreEntityGroupJson2, arrayOfString2);
            localMatrixCursor.addRow(arrayOfObject3);
            j += 1;
          }
          int m = (int)ContentUris.parseId(paramUri);
          if (m < 0)
          {
            int n = Log.w("ExploreContentProviderHelper", "id out of range");
            localMatrixCursor = null;
          }
          else
          {
            localExploreTabType1 = getTabType(paramInt);
            Uri localUri2 = paramUri;
            String str3 = "genreid";
            str1 = localUri2.getQueryParameter(str3);
            Context localContext4 = paramContext;
            MusicUrl.ExploreTabType localExploreTabType4 = localExploreTabType1;
            localObject = getTab(localContext4, localMusicCloudImpl, localExploreTabType4, str1);
            if ((localObject == null) || (((TabJson)localObject).mGroups == null) || (((TabJson)localObject).mGroups.isEmpty()))
            {
              int i1 = Log.w("ExploreContentProviderHelper", "Incomplete data");
              Context localContext5 = paramContext;
              MusicUrl.ExploreTabType localExploreTabType5 = localExploreTabType1;
              invalidateCache(localContext5, localExploreTabType5, str1);
              localMatrixCursor = null;
            }
            else
            {
              int i2 = ((TabJson)localObject).mGroups.size() + -1;
              if (m > i2)
              {
                int i3 = Log.w("ExploreContentProviderHelper", "id out of range");
                localMatrixCursor = null;
              }
              else
              {
                List localList1 = ((ExploreEntityGroupJson)((TabJson)localObject).mGroups.get(m)).mEntities;
                if (localList1 == null)
                {
                  String str4 = "Group has empty entities:" + m;
                  int i4 = Log.w("ExploreContentProviderHelper", str4);
                  Context localContext6 = paramContext;
                  MusicUrl.ExploreTabType localExploreTabType6 = localExploreTabType1;
                  invalidateCache(localContext6, localExploreTabType6, str1);
                  localMatrixCursor = null;
                }
                else if (MusicContentProvider.hasCount(paramArrayOfString))
                {
                  Object[] arrayOfObject4 = new Object[1];
                  Integer localInteger2 = Integer.valueOf(localList1.size());
                  arrayOfObject4[0] = localInteger2;
                  Object[] arrayOfObject5 = arrayOfObject4;
                  localMatrixCursor.addRow(arrayOfObject5);
                }
                else
                {
                  localIterator = localList1.iterator();
                  while (localIterator.hasNext())
                  {
                    ExploreEntityJson localExploreEntityJson = (ExploreEntityJson)localIterator.next();
                    if (localExploreEntityJson.mAlbum != null)
                    {
                      AlbumJson localAlbumJson = localExploreEntityJson.mAlbum;
                      String[] arrayOfString3 = paramArrayOfString;
                      Object[] arrayOfObject6 = ProjectionUtils.project(localAlbumJson, arrayOfString3);
                      localMatrixCursor.addRow(arrayOfObject6);
                    }
                    else if (localExploreEntityJson.mTrack != null)
                    {
                      Track localTrack = localExploreEntityJson.mTrack;
                      String[] arrayOfString4 = paramArrayOfString;
                      Object[] arrayOfObject7 = ProjectionUtils.project(localTrack, arrayOfString4);
                      localMatrixCursor.addRow(arrayOfObject7);
                    }
                    else if ((bool1) && (localExploreEntityJson.mPlaylist != null))
                    {
                      SyncablePlaylist localSyncablePlaylist = localExploreEntityJson.mPlaylist;
                      String[] arrayOfString5 = paramArrayOfString;
                      Object[] arrayOfObject8 = ProjectionUtils.project(localSyncablePlaylist, arrayOfString5);
                      localMatrixCursor.addRow(arrayOfObject8);
                    }
                  }
                  str1 = paramUri.getLastPathSegment();
                  Uri localUri3 = paramUri;
                  String str5 = "subgenreId";
                  String str6 = localUri3.getQueryParameter(str5);
                  localObject = getGenres(paramContext, localMusicCloudImpl, str1);
                  if (localObject == null)
                  {
                    localMatrixCursor = null;
                  }
                  else
                  {
                    List localList2 = ((MusicGenresResponseJson)localObject).mGenres;
                    if (MusicContentProvider.hasCount(paramArrayOfString))
                    {
                      Object[] arrayOfObject9 = new Object[1];
                      Integer localInteger3 = Integer.valueOf(localList2.size());
                      arrayOfObject9[0] = localInteger3;
                      Object[] arrayOfObject10 = arrayOfObject9;
                      localMatrixCursor.addRow(arrayOfObject10);
                    }
                    else
                    {
                      localIterator = localList2.iterator();
                      while (localIterator.hasNext())
                      {
                        MusicGenreJson localMusicGenreJson = (MusicGenreJson)localIterator.next();
                        if (!TextUtils.isEmpty(str6))
                        {
                          String str7 = localMusicGenreJson.mId;
                          if (!str6.equals(str7));
                        }
                        else
                        {
                          String[] arrayOfString6 = paramArrayOfString;
                          Object[] arrayOfObject11 = ProjectionUtils.project(localMusicGenreJson, arrayOfString6);
                          localMatrixCursor.addRow(arrayOfObject11);
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
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.ExploreContentProviderHelper
 * JD-Core Version:    0.6.2
 */