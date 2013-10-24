package com.google.android.music.medialist;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import com.google.android.music.download.artwork.AlbumIdSink;
import com.google.android.music.store.MusicContent.Genres;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.store.Store;
import com.google.android.music.utils.AlbumArtUtils;
import com.google.android.music.utils.AlbumArtUtils.AlbumIdIteratorFactory;
import com.google.android.music.utils.MusicUtils;

public class GenreSongList extends SongList
{
  long mId;
  String mName;

  public GenreSongList(long paramLong, String paramString, int paramInt)
  {
    super(paramInt, true, false);
    if (paramLong < 0L)
    {
      String str = "Invalid genre id: " + paramLong;
      throw new IllegalArgumentException(str);
    }
    this.mId = paramLong;
    this.mName = paramString;
  }

  public int appendToPlaylist(Context paramContext, long paramLong)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    long l = this.mId;
    return MusicContent.Playlists.appendGenreToPlayList(localContentResolver, paramLong, l);
  }

  public boolean containsRemoteItems(Context paramContext)
  {
    boolean bool = false;
    String[] arrayOfString1 = new String[1];
    arrayOfString1[bool] = "hasRemote";
    Uri localUri = MusicContent.Genres.getGenreUri(Long.valueOf(this.mId));
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    try
    {
      if (!localCursor.moveToFirst())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown Genre id: ");
        long l = this.mId;
        String str2 = l;
        int i = Log.e("GenreSongList", str2);
        return bool;
      }
      int j = 0;
      int k = localCursor.getInt(j);
      if (k == 1);
      for (j = 1; ; j = 0)
      {
        Store.safeClose(localCursor);
        bool = j;
        break;
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[3];
    StringBuilder localStringBuilder = new StringBuilder().append("");
    long l = this.mId;
    String str1 = l;
    arrayOfString[0] = str1;
    String str2 = this.mName;
    arrayOfString[1] = str2;
    String str3 = Integer.toString(getSortOrder());
    arrayOfString[2] = str3;
    return arrayOfString;
  }

  public Uri getContentUri(Context paramContext)
  {
    String str = getSortParam();
    return MusicContent.Genres.getGenreMembersUri(Long.valueOf(this.mId).longValue(), str);
  }

  public long getGenreId()
  {
    return this.mId;
  }

  public Bitmap getImage(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, boolean paramBoolean)
  {
    try
    {
      long l = this.mId;
      String str = this.mName;
      Uri localUri = MusicContent.Genres.getAlbumsOfGenreUri(this.mId);
      AlbumArtUtils.AlbumIdIteratorFactory localAlbumIdIteratorFactory = AlbumArtUtils.createAlbumIdIteratorFactoryForContentUri(paramContext, localUri);
      Context localContext = paramContext;
      int i = paramInt1;
      int j = paramInt2;
      AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
      boolean bool = paramBoolean;
      Bitmap localBitmap1 = AlbumArtUtils.getFauxAlbumArt(localContext, 5, false, l, i, j, str, null, localAlbumIdIteratorFactory, localAlbumIdSink, bool);
      localBitmap2 = localBitmap1;
      return localBitmap2;
    }
    catch (Exception localException)
    {
      while (true)
        Bitmap localBitmap2 = null;
    }
  }

  public String getName(Context paramContext)
  {
    return this.mName;
  }

  public String getSecondaryName(Context paramContext)
  {
    return paramContext.getResources().getString(2131230751);
  }

  public void refreshMetaData(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "name";
    Uri localUri = MusicContent.Genres.getGenreUri(Long.valueOf(this.mId));
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor == null)
      return;
    boolean bool = localCursor.moveToFirst();
    if (!localCursor.isAfterLast())
    {
      String str2 = localCursor.getString(0);
      this.mName = str2;
    }
    localCursor.close();
  }

  public boolean supportsAppendToPlaylist()
  {
    return true;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.GenreSongList
 * JD-Core Version:    0.6.2
 */