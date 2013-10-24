package com.google.android.music.medialist;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.music.download.artwork.AlbumIdSink;
import com.google.android.music.store.IStoreService;
import com.google.android.music.store.MusicContent;
import com.google.android.music.store.MusicContent.Albums;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.store.Store;
import com.google.android.music.utils.AlbumArtUtils;
import com.google.android.music.utils.MusicUtils;

public class AlbumSongList extends SongList
{
  private final long mAlbumId;
  private String mAlbumMetajamId;
  private String mAlbumName;
  private int mAlbumYear;
  private String mArtUrl;
  private long mArtistId;
  private String mArtistName;
  private String mArtistSort;

  public AlbumSongList(long paramLong1, long paramLong2, String paramString1, String paramString2, String paramString3, boolean paramBoolean)
  {
    super(0, paramBoolean, false);
    if (paramLong1 < 0L)
    {
      String str = "Invalid album id: " + paramLong1;
      throw new IllegalArgumentException(str);
    }
    this.mAlbumId = paramLong1;
    this.mArtistId = paramLong2;
    this.mAlbumName = paramString1;
    this.mArtistName = paramString2;
    this.mArtistSort = paramString3;
  }

  public AlbumSongList(long paramLong, String paramString1, String paramString2, boolean paramBoolean)
  {
  }

  public AlbumSongList(long paramLong, boolean paramBoolean)
  {
  }

  private void getNames(Context paramContext)
  {
    if ((this.mAlbumName != null) && (this.mArtistName != null) && (this.mArtistSort != null))
      return;
    String[] arrayOfString1 = new String[5];
    arrayOfString1[0] = "album_name";
    arrayOfString1[1] = "album_artist";
    arrayOfString1[2] = "album_artist_id";
    arrayOfString1[3] = "album_artist_sort";
    arrayOfString1[4] = "album_year";
    Uri localUri = MusicContent.Albums.getAlbumsUri(this.mAlbumId);
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor != null)
    {
      boolean bool = localCursor.moveToFirst();
      if (!localCursor.isAfterLast())
      {
        String str2 = localCursor.getString(0);
        this.mAlbumName = str2;
        String str3 = localCursor.getString(1);
        this.mArtistName = str3;
        long l = localCursor.getLong(2);
        this.mArtistId = l;
        String str4 = localCursor.getString(3);
        this.mArtistSort = str4;
        int i = localCursor.getInt(4);
        this.mAlbumYear = i;
      }
      localCursor.close();
    }
    if (MusicUtils.isUnknown(this.mAlbumName))
    {
      String str5 = paramContext.getString(2131230891);
      this.mAlbumName = str5;
    }
    if (!MusicUtils.isUnknown(this.mArtistName))
      return;
    String str6 = paramContext.getString(2131230890);
    this.mArtistName = str6;
  }

  public int appendToPlaylist(Context paramContext, long paramLong)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    long l = this.mAlbumId;
    return MusicContent.Playlists.appendAlbumToPlayList(localContentResolver, paramLong, l);
  }

  public boolean containsRemoteItems(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "hasRemote";
    Uri localUri = MusicContent.Albums.getAlbumsUri(this.mAlbumId);
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor != null);
    try
    {
      if (!localCursor.moveToFirst())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown album id: ");
        long l = this.mAlbumId;
        String str2 = l;
        int i = Log.e("AlbumSongList", str2);
        Store.safeClose(localCursor);
        bool = false;
        return bool;
      }
      int j = 0;
      int k = localCursor.getInt(j);
      if (k == 1);
      for (boolean bool = true; ; bool = false)
      {
        Store.safeClose(localCursor);
        break;
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  public long getAlbumId(Context paramContext)
  {
    try
    {
      long l1 = Long.valueOf(this.mAlbumId).longValue();
      l2 = l1;
      return l2;
    }
    catch (Exception localException)
    {
      while (true)
        long l2 = 65535L;
    }
  }

  public String getAlbumMetajamId(Context paramContext)
  {
    String[] arrayOfString;
    if (this.mAlbumMetajamId == null)
    {
      arrayOfString = new String[1];
      arrayOfString[0] = "StoreAlbumId";
    }
    try
    {
      Uri localUri = MusicContent.Albums.getAlbumsUri(this.mAlbumId);
      localCursor = MusicUtils.query(paramContext, localUri, arrayOfString, null, null, null);
      String str;
      if ((localCursor != null) && (localCursor.moveToFirst()) && (!localCursor.isNull(0)))
        str = localCursor.getString(0);
      for (this.mAlbumMetajamId = str; ; this.mAlbumMetajamId = "")
        return this.mAlbumMetajamId;
    }
    finally
    {
      Cursor localCursor;
      Store.safeClose(localCursor);
    }
  }

  public int getAlbumYear(Context paramContext)
  {
    getNames(paramContext);
    return this.mAlbumYear;
  }

  public String[] getArgs()
  {
    String[] arrayOfString;
    if ((this.mAlbumName != null) && (this.mArtistName != null))
    {
      arrayOfString = new String[4];
      String str1 = Long.toString(this.mAlbumId);
      arrayOfString[0] = str1;
      String str2 = this.mAlbumName;
      arrayOfString[1] = str2;
      String str3 = this.mArtistName;
      arrayOfString[2] = str3;
      String str4 = Boolean.toString(this.mShouldFilter);
      arrayOfString[3] = str4;
    }
    while (true)
    {
      return arrayOfString;
      arrayOfString = new String[2];
      String str5 = Long.toString(this.mAlbumId);
      arrayOfString[0] = str5;
      String str6 = Boolean.toString(this.mShouldFilter);
      arrayOfString[1] = str6;
    }
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
      Uri localUri = MusicContent.Albums.getAlbumsUri(this.mAlbumId);
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

  public long getArtistId(Context paramContext)
  {
    getNames(paramContext);
    return this.mArtistId;
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.Albums.getAudioInAlbumUri(this.mAlbumId);
  }

  public int getDownloadedSongCount(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "keeponDownloadedSongCount";
    Uri localUri = MusicContent.Albums.getAlbumsUri(this.mAlbumId);
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor != null);
    try
    {
      if (!localCursor.moveToFirst())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown album id: ");
        long l = this.mAlbumId;
        String str2 = l;
        int i = Log.e("AlbumSongList", str2);
      }
      int m;
      for (int j = -1; ; j = m)
      {
        return j;
        int k = 0;
        m = localCursor.getInt(k);
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  public Bitmap getImage(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, boolean paramBoolean)
  {
    try
    {
      long l = Long.valueOf(this.mAlbumId).longValue();
      String str1 = getName(paramContext);
      String str2 = getSecondaryName(paramContext);
      Context localContext = paramContext;
      int i = paramInt1;
      int j = paramInt2;
      AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
      boolean bool = paramBoolean;
      Bitmap localBitmap1 = AlbumArtUtils.getArtwork(localContext, l, i, j, true, str1, str2, localAlbumIdSink, bool);
      localBitmap2 = localBitmap1;
      return localBitmap2;
    }
    catch (Exception localException)
    {
      while (true)
        Bitmap localBitmap2 = null;
    }
  }

  public int getKeepOnSongCount(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "keeponSongCount";
    Uri localUri = MusicContent.Albums.getAlbumsUri(this.mAlbumId);
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor != null);
    try
    {
      if (!localCursor.moveToFirst())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown album id: ");
        long l = this.mAlbumId;
        String str2 = l;
        int i = Log.e("AlbumSongList", str2);
      }
      int m;
      for (int j = -1; ; j = m)
      {
        return j;
        int k = 0;
        m = localCursor.getInt(k);
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  public String getName(Context paramContext)
  {
    getNames(paramContext);
    return this.mAlbumName;
  }

  public String getSecondaryName(Context paramContext)
  {
    getNames(paramContext);
    return this.mArtistName;
  }

  public boolean hasArtistArt()
  {
    return true;
  }

  public boolean hasDifferentTrackArtists(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "HasDifferentTrackArtists";
    Uri localUri = MusicContent.Albums.getAlbumsUri(this.mAlbumId);
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor != null);
    try
    {
      if (!localCursor.moveToFirst())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown album id: ");
        long l = this.mAlbumId;
        String str2 = l;
        int i = Log.e("AlbumSongList", str2);
        Store.safeClose(localCursor);
        bool = false;
        return bool;
      }
      int j = 0;
      int k = localCursor.getInt(j);
      if (k == 1);
      for (boolean bool = true; ; bool = false)
      {
        Store.safeClose(localCursor);
        break;
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  public boolean hasPersistentNautilus(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "hasPersistNautilus";
    Uri localUri = MusicContent.Albums.getAlbumsUri(this.mAlbumId);
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor != null);
    try
    {
      if (!localCursor.moveToFirst())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown album id: ");
        long l = this.mAlbumId;
        String str2 = l;
        int i = Log.e("AlbumSongList", str2);
        Store.safeClose(localCursor);
        bool = false;
        return bool;
      }
      int j = 0;
      int k = localCursor.getInt(j);
      if (k != 0);
      for (boolean bool = true; ; bool = false)
      {
        Store.safeClose(localCursor);
        break;
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  public boolean isAllLocal(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "isAllLocal";
    Uri localUri = MusicContent.Albums.getAlbumsUri(this.mAlbumId);
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor != null);
    try
    {
      if (!localCursor.moveToFirst())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown album id: ");
        long l = this.mAlbumId;
        String str2 = l;
        int i = Log.e("AlbumSongList", str2);
        Store.safeClose(localCursor);
        bool = false;
        return bool;
      }
      int j = 0;
      int k = localCursor.getInt(j);
      if (k != 0);
      for (boolean bool = true; ; bool = false)
      {
        Store.safeClose(localCursor);
        break;
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  public boolean isSelectedForOfflineCaching(Context paramContext, IStoreService paramIStoreService)
  {
    try
    {
      long l1 = this.mAlbumId;
      boolean bool1 = paramIStoreService.isAlbumSelectedAsKeepOn(l1, true);
      bool2 = bool1;
      return bool2;
    }
    catch (RemoteException localRemoteException)
    {
      while (true)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Could not get keep on status for album id: ");
        long l2 = this.mAlbumId;
        String str = l2;
        int i = Log.w("AlbumSongList", str);
        boolean bool2 = false;
      }
    }
  }

  public void refreshMetaData(Context paramContext)
  {
    this.mAlbumName = null;
    getNames(paramContext);
  }

  public void removeFromMyLibrary(Context paramContext)
  {
    long l = this.mAlbumId;
    MusicContent.deletePersistentNautilusContentFromAlbum(paramContext, l);
  }

  public boolean supportsAppendToPlaylist()
  {
    return true;
  }

  public boolean supportsOfflineCaching()
  {
    return true;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.AlbumSongList
 * JD-Core Version:    0.6.2
 */