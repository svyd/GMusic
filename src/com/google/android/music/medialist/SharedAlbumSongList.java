package com.google.android.music.medialist;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.Log;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.util.ArrayList;
import java.util.Iterator;

public class SharedAlbumSongList extends SharedSongList
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.MEDIA_LIST);
  private static final boolean LOG_PROJ = DebugUtils.isLoggable(DebugUtils.MusicTag.CONTENT_PROVIDER);
  private String mAlbumArtUrl;
  private SongDataList mSongs;
  private String mStoreUrl;

  public SharedAlbumSongList(String paramString1, String paramString2, SongDataList paramSongDataList)
  {
    super(localDomain);
    this.mAlbumArtUrl = paramString1;
    this.mStoreUrl = paramString2;
    this.mSongs = paramSongDataList;
  }

  public Cursor createSyncCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    Object localObject = null;
    if (this.mSongs == null);
    String[] arrayOfString;
    int k;
    int m;
    do
    {
      do
        return localObject;
      while (paramArrayOfString == null);
      arrayOfString = new String[paramArrayOfString.length];
      if (LOG_PROJ)
        logProjection(paramArrayOfString);
      int i = 0;
      while (true)
      {
        int j = paramArrayOfString.length;
        if (i >= j)
          break;
        String str = paramArrayOfString[i];
        arrayOfString[i] = str;
        i += 1;
      }
      localObject = new MatrixCursor(arrayOfString);
      k = 0;
      m = this.mSongs.mList.size();
    }
    while (k >= m);
    long l = k;
    SongData localSongData = (SongData)this.mSongs.mList.get(k);
    ArrayList localArrayList = createRow(l, arrayOfString, localSongData);
    if (localArrayList != null)
      ((MatrixCursor)localObject).addRow(localArrayList);
    while (true)
    {
      k += 1;
      break;
      int n = Log.w("SharedAlbumSongList", "Failed to create row");
    }
  }

  public String getAlbumArtUrl(Context paramContext)
  {
    return this.mAlbumArtUrl;
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[3];
    String str1 = this.mAlbumArtUrl;
    arrayOfString[0] = str1;
    String str2 = this.mStoreUrl;
    arrayOfString[1] = str2;
    String str3 = SongDataList.toJson(this.mSongs);
    arrayOfString[2] = str3;
    return arrayOfString;
  }

  public String getName(Context paramContext)
  {
    if ((this.mSongs == null) || (this.mSongs.mList == null) || (this.mSongs.mList.size() == 0));
    for (String str = null; ; str = ((SongData)this.mSongs.mList.get(0)).mAlbum)
      return str;
  }

  public String getSecondaryName(Context paramContext)
  {
    if ((this.mSongs == null) || (this.mSongs.mList == null) || (this.mSongs.mList.size() == 0));
    for (String str = null; ; str = ((SongData)this.mSongs.mList.get(0)).mArtist)
      return str;
  }

  public Cursor getSongCursor(Context paramContext, ContentIdentifier paramContentIdentifier, String[] paramArrayOfString)
  {
    Object localObject = null;
    if (paramContentIdentifier == null);
    while (true)
    {
      return localObject;
      if (this.mSongs != null)
      {
        String[] arrayOfString = new String[paramArrayOfString.length];
        if (LOG_PROJ)
          logProjection(paramArrayOfString);
        int i = 0;
        while (true)
        {
          int j = paramArrayOfString.length;
          if (i >= j)
            break;
          String str = paramArrayOfString[i];
          arrayOfString[i] = str;
          i += 1;
        }
        ArrayList localArrayList1 = this.mSongs.mList;
        int k = (int)paramContentIdentifier.getId();
        SongData localSongData = (SongData)localArrayList1.get(k);
        if (localSongData != null)
        {
          long l = paramContentIdentifier.getId();
          ArrayList localArrayList2 = createRow(l, paramArrayOfString, localSongData);
          if (localArrayList2 != null)
          {
            MatrixCursor localMatrixCursor = new MatrixCursor(arrayOfString);
            localMatrixCursor.addRow(localArrayList2);
            localObject = createMediaCursor(paramContext, localMatrixCursor);
          }
        }
      }
    }
  }

  public String getStoreUrl()
  {
    return this.mStoreUrl;
  }

  public boolean hasDifferentTrackArtists(Context paramContext)
  {
    Object localObject = null;
    Iterator localIterator = this.mSongs.mList.iterator();
    String str;
    if (localIterator.hasNext())
    {
      str = ((SongData)localIterator.next()).mArtist;
      if ((localObject == null) || (localObject.equals(str)));
    }
    for (boolean bool = true; ; bool = false)
    {
      return bool;
      localObject = str;
      break;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.SharedAlbumSongList
 * JD-Core Version:    0.6.2
 */