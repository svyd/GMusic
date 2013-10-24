package com.google.android.music.medialist;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.Log;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.util.ArrayList;

public class SharedSingleSongList extends SharedSongList
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.MEDIA_LIST);
  private static final boolean LOG_PROJ = DebugUtils.isLoggable(DebugUtils.MusicTag.CONTENT_PROVIDER);
  private String mAlbumArtUrl;
  private SongData mSong;
  private String mStoreUrl;

  public SharedSingleSongList(String paramString1, String paramString2, SongData paramSongData)
  {
    super(localDomain);
    this.mAlbumArtUrl = paramString1;
    this.mStoreUrl = paramString2;
    this.mSong = paramSongData;
  }

  public Cursor createSyncCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    Object localObject = null;
    String[] arrayOfString = new String[paramArrayOfString.length];
    if (this.mSong == null);
    while (true)
    {
      return localObject;
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
      SongData localSongData = this.mSong;
      ArrayList localArrayList = createRow(0L, arrayOfString, localSongData);
      if (localArrayList == null)
      {
        int k = Log.e("SharedSingleSongList", "Failed to creat row for the cursor");
      }
      else
      {
        localObject = new MatrixCursor(arrayOfString);
        ((MatrixCursor)localObject).addRow(localArrayList);
      }
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
    String str3 = SongData.toJson(this.mSong);
    arrayOfString[2] = str3;
    return arrayOfString;
  }

  public String getName(Context paramContext)
  {
    if (this.mSong == null);
    for (String str = null; ; str = this.mSong.mAlbum)
      return str;
  }

  public String getSecondaryName(Context paramContext)
  {
    if (this.mSong == null);
    for (String str = null; ; str = this.mSong.mArtist)
      return str;
  }

  public String getStoreUrl()
  {
    return this.mStoreUrl;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.SharedSingleSongList
 * JD-Core Version:    0.6.2
 */