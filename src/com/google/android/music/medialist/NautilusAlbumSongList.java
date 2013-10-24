package com.google.android.music.medialist;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.music.store.IStoreService;
import com.google.android.music.store.MusicContent.Albums;
import com.google.android.music.store.Store;
import com.google.android.music.store.TagNormalizer;
import com.google.android.music.utils.MusicUtils;

public class NautilusAlbumSongList extends NautilusSongList
{
  private String mAlbumArtUrl;
  private String mAlbumName;
  private int mAlbumYear;
  private String mArtistName;
  private long mLocalAlbumId = 65535L;
  private final String mNautilusAlbumId;

  public NautilusAlbumSongList(String paramString)
  {
    this.mNautilusAlbumId = paramString;
  }

  private void getNames(Context paramContext)
  {
    if ((this.mAlbumName != null) && (this.mArtistName != null))
      return;
    String[] arrayOfString1 = new String[4];
    arrayOfString1[0] = "album_name";
    arrayOfString1[1] = "album_artist";
    arrayOfString1[2] = "album_art";
    arrayOfString1[3] = "album_year";
    Uri localUri = MusicContent.Albums.getNautilusAlbumsUri(this.mNautilusAlbumId);
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor != null)
    {
      if (localCursor.moveToFirst())
      {
        String str2 = localCursor.getString(0);
        this.mAlbumName = str2;
        String str3 = localCursor.getString(1);
        this.mArtistName = str3;
        String str4 = localCursor.getString(2);
        this.mAlbumArtUrl = str4;
        int i = localCursor.getInt(3);
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

  public String getAlbumArtUrl(Context paramContext)
  {
    getNames(paramContext);
    return this.mAlbumArtUrl;
  }

  public String getAlbumArtist(Context paramContext)
  {
    getNames(paramContext);
    return this.mArtistName;
  }

  public long getAlbumId(Context paramContext)
  {
    String str1;
    Object localObject1;
    Cursor localCursor;
    if (this.mLocalAlbumId == 65535L)
    {
      String[] arrayOfString1 = new String[2];
      arrayOfString1[0] = "album";
      arrayOfString1[1] = "AlbumArtist";
      str1 = null;
      localObject1 = null;
      Uri localUri = getContentUri(paramContext);
      Context localContext = paramContext;
      String[] arrayOfString2 = null;
      String str2 = null;
      localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str2);
      if (localCursor == null);
    }
    while (true)
    {
      try
      {
        if ((localCursor.moveToFirst()) && (!localCursor.isNull(0)) && (!localCursor.isNull(1)))
        {
          str1 = localCursor.getString(0);
          String str3 = localCursor.getString(1);
          localObject1 = str3;
        }
        Store.safeClose(localCursor);
        if ((TextUtils.isEmpty(localObject1)) || (TextUtils.isEmpty(str1)))
        {
          l1 = 65535L;
          return l1;
        }
      }
      finally
      {
        Store.safeClose(localCursor);
      }
      TagNormalizer localTagNormalizer = new TagNormalizer();
      String str4 = localTagNormalizer.normalize(str1);
      String str5 = localTagNormalizer.normalize(localObject1);
      long l2 = Store.generateId(str4 + '\037' + str5);
      this.mLocalAlbumId = l2;
      long l1 = this.mLocalAlbumId;
    }
  }

  public int getAlbumYear(Context paramContext)
  {
    getNames(paramContext);
    return this.mAlbumYear;
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[1];
    String str = this.mNautilusAlbumId;
    arrayOfString[0] = str;
    return arrayOfString;
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.Albums.getAudioInNautilusAlbumUri(this.mNautilusAlbumId);
  }

  public int getDownloadedSongCount(Context paramContext)
  {
    int i = 0;
    long l = getAlbumId(paramContext);
    if (l == 65535L);
    while (true)
    {
      return i;
      String[] arrayOfString1 = new String[1];
      arrayOfString1[0] = "keeponDownloadedSongCount";
      Uri localUri = MusicContent.Albums.getAlbumsUri(l);
      Context localContext = paramContext;
      String[] arrayOfString2 = null;
      String str1 = null;
      Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
      if (localCursor != null);
      try
      {
        if (!localCursor.moveToFirst())
        {
          String str2 = "Unknown album id: " + l;
          int j = Log.e("NautilusSongList", str2);
        }
        int m;
        for (i = -1; ; i = m)
        {
          Store.safeClose(localCursor);
          break;
          int k = 0;
          m = localCursor.getInt(k);
        }
      }
      finally
      {
        Store.safeClose(localCursor);
      }
    }
  }

  public int getKeepOnSongCount(Context paramContext)
  {
    int i = 0;
    long l = getAlbumId(paramContext);
    if (l == 65535L);
    while (true)
    {
      return i;
      String[] arrayOfString1 = new String[1];
      arrayOfString1[0] = "keeponSongCount";
      Uri localUri = MusicContent.Albums.getAlbumsUri(l);
      Context localContext = paramContext;
      String[] arrayOfString2 = null;
      String str1 = null;
      Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
      if (localCursor != null);
      try
      {
        if (!localCursor.moveToFirst())
        {
          String str2 = "Unknown album id: " + l;
          int j = Log.e("NautilusSongList", str2);
        }
        int m;
        for (i = -1; ; i = m)
        {
          Store.safeClose(localCursor);
          break;
          int k = 0;
          m = localCursor.getInt(k);
        }
      }
      finally
      {
        Store.safeClose(localCursor);
      }
    }
  }

  public String getName(Context paramContext)
  {
    getNames(paramContext);
    return this.mAlbumName;
  }

  public String getNautilusId()
  {
    return this.mNautilusAlbumId;
  }

  public String getSecondaryName(Context paramContext)
  {
    return getAlbumArtist(paramContext);
  }

  public boolean hasArtistArt()
  {
    return true;
  }

  public boolean hasDifferentTrackArtists(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "HasDifferentTrackArtists";
    Uri localUri = MusicContent.Albums.getNautilusAlbumsUri(this.mNautilusAlbumId);
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
        String str2 = this.mNautilusAlbumId;
        String str3 = str2;
        int i = Log.e("NautilusSongList", str3);
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

  public boolean isSelectedForOfflineCaching(Context paramContext, IStoreService paramIStoreService)
  {
    boolean bool1 = false;
    long l = getAlbumId(paramContext);
    if (l == 65535L);
    while (true)
    {
      return bool1;
      boolean bool2 = true;
      try
      {
        boolean bool3 = paramIStoreService.isAlbumSelectedAsKeepOn(l, bool2);
        bool1 = bool3;
      }
      catch (RemoteException localRemoteException)
      {
        String str = "Could not get keep on status for album id: " + l;
        int i = Log.w("NautilusSongList", str);
      }
    }
  }

  public boolean supportsOfflineCaching()
  {
    return true;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.NautilusAlbumSongList
 * JD-Core Version:    0.6.2
 */