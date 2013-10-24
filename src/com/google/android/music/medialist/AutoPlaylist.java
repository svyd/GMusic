package com.google.android.music.medialist;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.music.download.artwork.AlbumIdSink;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.IStoreService;
import com.google.android.music.store.MusicContent.AutoPlaylists;
import com.google.android.music.store.MusicContent.AutoPlaylists.Members;
import com.google.android.music.store.Store;
import com.google.android.music.utils.AlbumArtUtils;
import com.google.android.music.utils.AlbumArtUtils.AlbumIdIteratorFactory;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LabelUtils;
import com.google.android.music.utils.MusicUtils;

public abstract class AutoPlaylist extends SongList
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.MEDIA_LIST);
  private final long mId;

  protected AutoPlaylist(int paramInt, long paramLong)
  {
    super(paramInt, true, false);
    this.mId = paramLong;
  }

  protected AutoPlaylist(int paramInt, long paramLong, boolean paramBoolean)
  {
    super(paramInt, true, paramBoolean);
    this.mId = paramLong;
  }

  protected AutoPlaylist(int paramInt, long paramLong, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramInt, paramBoolean2, paramBoolean1);
    this.mId = paramLong;
  }

  private boolean containsItems(Context paramContext, String[] paramArrayOfString)
  {
    Uri localUri = MusicContent.AutoPlaylists.getAutoPlaylistUri(this.mId);
    Context localContext = paramContext;
    String[] arrayOfString1 = paramArrayOfString;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor != null);
    try
    {
      if (!localCursor.moveToFirst())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown playlist id: ");
        long l = this.mId;
        String str2 = l;
        int i = Log.e("AutoPlaylist", str2);
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

  public static final AutoPlaylist getAutoPlaylist(long paramLong, boolean paramBoolean, MusicPreferences paramMusicPreferences)
  {
    int i = -1;
    Object localObject1;
    Object localObject2;
    if (paramLong == 65535L)
    {
      localObject1 = new com/google/android/music/medialist/RecentlyAdddedSongList;
      if (paramBoolean)
        i = paramMusicPreferences.getRecentlyAddedSongsSortOrder();
      ((RecentlyAdddedSongList)localObject1).<init>(i);
      localObject2 = localObject1;
    }
    while (true)
    {
      return localObject2;
      if (paramLong == 65534L)
      {
        localObject1 = new com/google/android/music/medialist/AllSongsList;
        if (paramBoolean)
          i = paramMusicPreferences.getAllSongsSortOrder();
        ((AllSongsList)localObject1).<init>(i);
        localObject2 = localObject1;
      }
      else if (paramLong == 65533L)
      {
        localObject1 = new com/google/android/music/medialist/StoreSongList;
        if (paramBoolean)
          i = paramMusicPreferences.getStoreSongsSortOrder();
        boolean bool = paramMusicPreferences.getStoreAvailable();
        ((StoreSongList)localObject1).<init>(i, bool);
        localObject2 = localObject1;
      }
      else
      {
        if (paramLong != 65532L)
          break;
        localObject1 = new com/google/android/music/medialist/ThumbsUpList;
        if (paramBoolean)
          i = paramMusicPreferences.getThumbsUpSongsSortOrder();
        ((ThumbsUpList)localObject1).<init>(i);
        localObject2 = localObject1;
      }
    }
    String str = "Unexpected auto-playlist id:" + paramLong;
    throw new IllegalArgumentException(str);
  }

  public boolean containsRemoteItems(Context paramContext)
  {
    if (LOGV)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("containsRemoteItems: mId=");
      long l = this.mId;
      String str = l;
      int i = Log.d("AutoPlaylist", str);
    }
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "hasRemote";
    return containsItems(paramContext, arrayOfString);
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[1];
    String str = Integer.toString(getSortOrder());
    arrayOfString[0] = str;
    return arrayOfString;
  }

  public Uri getContentUri(Context paramContext)
  {
    long l = this.mId;
    String str = getSortParam();
    return MusicContent.AutoPlaylists.Members.getAutoPlaylistItemsUri(l, str);
  }

  public int getDownloadedSongCount(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "keeponDownloadedSongCount";
    Uri localUri = MusicContent.AutoPlaylists.getAutoPlaylistUri(this.mId);
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor != null);
    try
    {
      if (!localCursor.moveToFirst())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown auto playlist id: ");
        long l = this.mId;
        String str2 = l;
        int i = Log.e("AutoPlaylist", str2);
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

  public long getId()
  {
    return this.mId;
  }

  public Bitmap getImage(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, boolean paramBoolean)
  {
    long l = this.mId;
    String str = getListingName(paramContext);
    Uri localUri = getContentUri(paramContext);
    AlbumArtUtils.AlbumIdIteratorFactory localAlbumIdIteratorFactory = AlbumArtUtils.createAlbumIdIteratorFactoryForContentUri(paramContext, localUri);
    Context localContext = paramContext;
    int i = paramInt1;
    int j = paramInt2;
    AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
    boolean bool = paramBoolean;
    return AlbumArtUtils.getFauxAlbumArt(localContext, 4, true, l, i, j, str, null, localAlbumIdIteratorFactory, localAlbumIdSink, bool);
  }

  public int getKeepOnSongCount(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "keeponSongCount";
    Uri localUri = MusicContent.AutoPlaylists.getAutoPlaylistUri(this.mId);
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor != null);
    try
    {
      if (!localCursor.moveToFirst())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown auto playlist id: ");
        long l = this.mId;
        String str2 = l;
        int i = Log.e("AutoPlaylist", str2);
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

  public String getListingName(Context paramContext)
  {
    int i = getListingNameResourceId();
    return paramContext.getString(i);
  }

  protected abstract int getListingNameResourceId();

  public String getName(Context paramContext)
  {
    int i = getTitleResourceId();
    String str = paramContext.getString(i);
    return LabelUtils.getPlaylistPrimaryLabel(paramContext, str, 100);
  }

  public String getSecondaryName(Context paramContext)
  {
    Object localObject1 = new Object();
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, localObject1);
    try
    {
      String str1 = LabelUtils.getPlaylistSecondaryLabel(paramContext, localMusicPreferences, 100);
      String str2 = str1;
      return str2;
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(localObject1);
    }
  }

  protected abstract int getTitleResourceId();

  public boolean hasArtistArt()
  {
    return true;
  }

  public boolean isAllLocal(Context paramContext)
  {
    if (LOGV)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("containsLocalItems: mId=");
      long l = this.mId;
      String str = l;
      int i = Log.d("AutoPlaylist", str);
    }
    return false;
  }

  public boolean isSelectedForOfflineCaching(Context paramContext, IStoreService paramIStoreService)
  {
    try
    {
      long l1 = this.mId;
      boolean bool1 = paramIStoreService.isAutoPlaylistSelectedAsKeepOn(l1);
      bool2 = bool1;
      return bool2;
    }
    catch (RemoteException localRemoteException)
    {
      while (true)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Error trying to get offline status for playlist: ");
        long l2 = this.mId;
        String str = l2;
        int i = Log.w("AutoPlaylist", str, localRemoteException);
        boolean bool2 = false;
      }
    }
  }

  public void refreshMetaData(Context paramContext)
  {
  }

  public boolean supportsOfflineCaching()
  {
    if (LOGV)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("supportsOfflineCaching: mId=");
      long l = this.mId;
      String str = l;
      int i = Log.d("AutoPlaylist", str);
    }
    return true;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.AutoPlaylist
 * JD-Core Version:    0.6.2
 */