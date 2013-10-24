package com.google.android.music.medialist;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.music.download.artwork.AlbumIdSink;
import com.google.android.music.store.MusicContent.Artists;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.store.Store;
import com.google.android.music.utils.AlbumArtUtils;
import com.google.android.music.utils.AlbumArtUtils.AlbumIdIteratorFactory;
import com.google.android.music.utils.MusicUtils;

public class ArtistSongList extends SongList
{
  private String mArtUrl;
  private String mArtistMetajamId;
  private long mId;
  private String mName;

  public ArtistSongList(long paramLong, String paramString, int paramInt, boolean paramBoolean)
  {
    super(paramInt, paramBoolean, false);
    if (paramLong < 0L)
    {
      String str = "Invalid artist id: " + paramLong;
      throw new IllegalArgumentException(str);
    }
    this.mId = paramLong;
    this.mName = paramString;
  }

  public int appendToPlaylist(Context paramContext, long paramLong)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    long l = this.mId;
    return MusicContent.Playlists.appendArtistToPlayList(localContentResolver, paramLong, l);
  }

  public boolean containsRemoteItems(Context paramContext)
  {
    boolean bool = false;
    String[] arrayOfString1 = new String[1];
    arrayOfString1[bool] = "hasRemote";
    Uri localUri = MusicContent.Artists.getArtistsUri(this.mId);
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    try
    {
      if (!localCursor.moveToFirst())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown artist id: ");
        long l = this.mId;
        String str2 = l;
        int i = Log.e("ArtistSongList", str2);
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
    String[] arrayOfString = new String[4];
    StringBuilder localStringBuilder = new StringBuilder().append("");
    long l = this.mId;
    String str1 = l;
    arrayOfString[0] = str1;
    String str2 = this.mName;
    arrayOfString[1] = str2;
    String str3 = Integer.toString(getSortOrder());
    arrayOfString[2] = str3;
    String str4 = Boolean.toString(this.mShouldFilter);
    arrayOfString[3] = str4;
    return arrayOfString;
  }

  public String getArtUrl(Context paramContext)
  {
    String[] arrayOfString;
    if (this.mArtUrl == null)
    {
      arrayOfString = new String[1];
      arrayOfString[0] = "artworkUrl";
    }
    try
    {
      Uri localUri = MusicContent.Artists.getArtistsUri(this.mId);
      localCursor = MusicUtils.query(paramContext, localUri, arrayOfString, null, null, null);
      String str;
      if ((localCursor != null) && (localCursor.moveToFirst()) && (!localCursor.isNull(0)))
        str = localCursor.getString(0);
      for (this.mArtUrl = str; ; this.mArtUrl = "")
        return this.mArtUrl;
    }
    finally
    {
      Cursor localCursor;
      Store.safeClose(localCursor);
    }
  }

  public long getArtistId()
  {
    return this.mId;
  }

  public String getArtistMetajamId(Context paramContext)
  {
    String[] arrayOfString;
    if (this.mArtistMetajamId == null)
    {
      arrayOfString = new String[1];
      arrayOfString[0] = "ArtistMetajamId";
    }
    try
    {
      Uri localUri = MusicContent.Artists.getArtistsUri(this.mId);
      localCursor = MusicUtils.query(paramContext, localUri, arrayOfString, null, null, null);
      String str;
      if ((localCursor != null) && (localCursor.moveToFirst()) && (!localCursor.isNull(0)))
        str = localCursor.getString(0);
      for (this.mArtistMetajamId = str; ; this.mArtistMetajamId = "")
        return this.mArtistMetajamId;
    }
    finally
    {
      Cursor localCursor;
      Store.safeClose(localCursor);
    }
  }

  public Uri getContentUri(Context paramContext)
  {
    String str = getSortParam();
    return MusicContent.Artists.getAudioByArtistUri(this.mId, str);
  }

  public Bitmap getImage(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, boolean paramBoolean)
  {
    long l = this.mName.hashCode();
    String str = this.mName;
    Uri localUri = MusicContent.Artists.getAlbumsByArtistsUri(this.mId);
    AlbumArtUtils.AlbumIdIteratorFactory localAlbumIdIteratorFactory = AlbumArtUtils.createAlbumIdIteratorFactoryForContentUri(paramContext, localUri);
    Context localContext = paramContext;
    int i = paramInt1;
    int j = paramInt2;
    AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
    boolean bool = paramBoolean;
    return AlbumArtUtils.getFauxAlbumArt(localContext, 5, false, l, i, j, str, null, localAlbumIdIteratorFactory, localAlbumIdSink, bool);
  }

  public String getName(Context paramContext)
  {
    return this.mName;
  }

  public String getSecondaryName(Context paramContext)
  {
    return paramContext.getResources().getString(2131230751);
  }

  public boolean hasDifferentTrackArtists(Context paramContext)
  {
    return false;
  }

  public void refreshMetaData(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "artist";
    Uri localUri = MusicContent.Artists.getArtistsUri(this.mId);
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
      if (!TextUtils.isEmpty(str2))
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
 * Qualified Name:     com.google.android.music.medialist.ArtistSongList
 * JD-Core Version:    0.6.2
 */