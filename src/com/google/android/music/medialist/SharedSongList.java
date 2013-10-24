package com.google.android.music.medialist;

import android.util.Log;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.util.ArrayList;

public abstract class SharedSongList extends ExternalSongList
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.MEDIA_LIST);
  private int mFlags = 2147483647;

  public SharedSongList(ContentIdentifier.Domain paramDomain)
  {
    super(paramDomain);
  }

  protected ArrayList<Object> createRow(long paramLong, String[] paramArrayOfString, SongData paramSongData)
  {
    int i = paramArrayOfString.length;
    ArrayList localArrayList = new ArrayList(i);
    int j = 0;
    int k = paramArrayOfString.length;
    if (j < k)
    {
      String str1 = paramArrayOfString[j];
      if ((str1.equals("_id")) || (str1.equals("audio_id")))
      {
        Long localLong1 = Long.valueOf(paramLong);
        boolean bool1 = localArrayList.add(localLong1);
      }
      while (true)
      {
        j += 1;
        break;
        if (str1.equals("artist"))
        {
          String str2 = paramSongData.mArtist;
          boolean bool2 = localArrayList.add(str2);
        }
        else if (str1.equals("album"))
        {
          String str3 = paramSongData.mAlbum;
          boolean bool3 = localArrayList.add(str3);
        }
        else if (str1.equals("title"))
        {
          String str4 = paramSongData.mTitle;
          boolean bool4 = localArrayList.add(str4);
        }
        else if (str1.equals("album_id"))
        {
          Long localLong2 = Long.valueOf(paramSongData.mAlbumId);
          boolean bool5 = localArrayList.add(localLong2);
        }
        else if (str1.equals("AlbumArtistId"))
        {
          Long localLong3 = Long.valueOf(paramSongData.mAlbumArtistId);
          boolean bool6 = localArrayList.add(localLong3);
        }
        else if (str1.equals("AlbumArtist"))
        {
          String str5 = paramSongData.mAlbumArtist;
          boolean bool7 = localArrayList.add(str5);
        }
        else if (str1.equals("artistSort"))
        {
          String str6 = paramSongData.mArtistSort;
          boolean bool8 = localArrayList.add(str6);
        }
        else if (str1.equals("is_podcast"))
        {
          boolean bool9 = localArrayList.add("0");
        }
        else if (str1.equals("bookmark"))
        {
          boolean bool10 = localArrayList.add("0");
        }
        else if (str1.equals("duration"))
        {
          Long localLong4 = Long.valueOf(paramSongData.mDuration);
          boolean bool11 = localArrayList.add(localLong4);
        }
        else if (str1.equals("hasRemote"))
        {
          Integer localInteger1 = Integer.valueOf(paramSongData.mHasRemote);
          boolean bool12 = localArrayList.add(localInteger1);
        }
        else if (str1.equals("hasLocal"))
        {
          Integer localInteger2 = Integer.valueOf(paramSongData.mHasLocal);
          boolean bool13 = localArrayList.add(localInteger2);
        }
        else if (str1.equals("Rating"))
        {
          Integer localInteger3 = Integer.valueOf(paramSongData.mRating);
          boolean bool14 = localArrayList.add(localInteger3);
        }
        else if (str1.equals("SourceId"))
        {
          String str7 = paramSongData.mSourceId;
          boolean bool15 = localArrayList.add(str7);
        }
        else if (str1.equals("year"))
        {
          boolean bool16 = localArrayList.add("0");
        }
        else if (str1.equals("Genre"))
        {
          boolean bool17 = localArrayList.add("");
        }
        else if (str1.equals("StoreId"))
        {
          boolean bool18 = localArrayList.add(null);
        }
        else if (str1.equals("SongId"))
        {
          boolean bool19 = localArrayList.add("0");
        }
        else if (str1.equals("SourceAccount"))
        {
          boolean bool20 = localArrayList.add("0");
        }
        else if (str1.equals("Domain"))
        {
          Integer localInteger4 = Integer.valueOf(getDomain().ordinal());
          boolean bool21 = localArrayList.add(localInteger4);
        }
        else if (str1.equals("domainParam"))
        {
          String str8 = paramSongData.mDomainParam;
          boolean bool22 = localArrayList.add(str8);
        }
        else if (str1.equals("Size"))
        {
          boolean bool23 = localArrayList.add("0");
        }
        else if (str1.equals("Nid"))
        {
          boolean bool24 = localArrayList.add(null);
        }
        else if (str1.equals("StoreAlbumId"))
        {
          boolean bool25 = localArrayList.add(null);
        }
        else if (str1.equals("ArtistMetajamId"))
        {
          boolean bool26 = localArrayList.add(null);
        }
        else if (str1.equals("artist_id"))
        {
          boolean bool27 = localArrayList.add(null);
        }
        else if (str1.equals("ArtistArtLocation"))
        {
          boolean bool28 = localArrayList.add(null);
        }
        else
        {
          String str9 = "Nulling out projection: " + str1;
          int m = Log.w("SharedSongList", str9);
          boolean bool29 = localArrayList.add(null);
        }
      }
    }
    return localArrayList;
  }

  protected void logProjection(String[] paramArrayOfString)
  {
    if (paramArrayOfString == null)
      return;
    int i = 0;
    while (true)
    {
      int j = paramArrayOfString.length;
      if (i >= j)
        return;
      StringBuilder localStringBuilder = new StringBuilder().append("proj[").append(i).append("]: ");
      String str1 = paramArrayOfString[i];
      String str2 = str1;
      int k = Log.d("SharedSongList", str2);
      i += 1;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.SharedSongList
 * JD-Core Version:    0.6.2
 */