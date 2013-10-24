package com.google.android.music.medialist;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.music.download.artwork.AlbumIdSink;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.IStoreService;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.store.MusicContent.Playlists.Members;
import com.google.android.music.store.Store;
import com.google.android.music.utils.AlbumArtUtils;
import com.google.android.music.utils.LabelUtils;
import com.google.android.music.utils.MusicUtils;

public class PlaylistSongList extends SongList
{
  private final String mArtUrl;
  private final String mDescription;
  private final long mId;
  private String mName;
  private final String mOwnerName;
  private final String mOwnerProfilePhotoUrl;
  private final int mPlaylistType;
  private final String mShareToken;

  public PlaylistSongList(long paramLong, String paramString1, int paramInt, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, boolean paramBoolean)
  {
    super(0, paramBoolean, true);
    this.mId = paramLong;
    this.mName = paramString1;
    this.mPlaylistType = paramInt;
    this.mDescription = paramString2;
    this.mOwnerName = paramString3;
    this.mShareToken = paramString4;
    this.mArtUrl = paramString5;
    this.mOwnerProfilePhotoUrl = paramString6;
    if (paramLong <= 0L)
    {
      String str = "Invalid playlist id:" + paramLong;
      throw new IllegalArgumentException(str);
    }
    if (supportsShuffle())
      return;
    clearFlag(4);
  }

  private final boolean supportsShuffle()
  {
    switch (this.mPlaylistType)
    {
    default:
    case 0:
    case 1:
    case 100:
    }
    for (boolean bool = false; ; bool = true)
      return bool;
  }

  public int appendToPlaylist(Context paramContext, long paramLong)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    long l = this.mId;
    return MusicContent.Playlists.appendPlaylistToPlayList(localContentResolver, paramLong, l);
  }

  public boolean containsRemoteItems(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "hasRemote";
    Uri localUri = MusicContent.Playlists.getPlaylistUri(this.mId);
    Context localContext = paramContext;
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
        int i = Log.e("PlaylistSongList", str2);
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

  protected MediaList.MediaCursor createMediaCursor(Context paramContext, Cursor paramCursor)
  {
    long l = this.mId;
    return new PlaylistCursor(paramContext, paramCursor, l);
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[9];
    StringBuilder localStringBuilder1 = new StringBuilder().append("");
    long l = this.mId;
    String str1 = l;
    arrayOfString[0] = str1;
    String str2 = this.mName;
    arrayOfString[1] = str2;
    StringBuilder localStringBuilder2 = new StringBuilder().append("");
    int i = this.mPlaylistType;
    String str3 = i;
    arrayOfString[2] = str3;
    String str4 = this.mDescription;
    arrayOfString[3] = str4;
    String str5 = this.mOwnerName;
    arrayOfString[4] = str5;
    String str6 = this.mShareToken;
    arrayOfString[5] = str6;
    String str7 = this.mArtUrl;
    arrayOfString[6] = str7;
    String str8 = this.mOwnerProfilePhotoUrl;
    arrayOfString[7] = str8;
    String str9 = Boolean.toString(this.mShouldFilter);
    arrayOfString[8] = str9;
    return arrayOfString;
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.Playlists.Members.getPlaylistItemsUri(Long.valueOf(this.mId).longValue());
  }

  public int getDownloadedSongCount(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "keeponDownloadedSongCount";
    Uri localUri = MusicContent.Playlists.getPlaylistUri(this.mId);
    Context localContext = paramContext;
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
        int i = Log.e("PlaylistSongList", str2);
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
    int i = AlbumArtUtils.playlistTypeToArtStyle(this.mPlaylistType);
    long l = this.mId;
    String str = this.mName;
    Context localContext = paramContext;
    int j = paramInt1;
    int k = paramInt2;
    AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
    boolean bool = paramBoolean;
    return AlbumArtUtils.getFauxAlbumArt(localContext, i, false, l, j, k, str, null, null, localAlbumIdSink, bool);
  }

  public int getKeepOnSongCount(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "keeponSongCount";
    Uri localUri = MusicContent.Playlists.getPlaylistUri(this.mId);
    Context localContext = paramContext;
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
        int i = Log.e("PlaylistSongList", str2);
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
    if (this.mName == null)
      refreshMetaData(paramContext);
    String str = this.mName;
    int i = this.mPlaylistType;
    return LabelUtils.getPlaylistPrimaryLabel(paramContext, str, i);
  }

  public String getOwnerProfilePhotoUrl()
  {
    return this.mOwnerProfilePhotoUrl;
  }

  public long getPlaylistId()
  {
    return this.mId;
  }

  public String getPlaylistName()
  {
    return this.mName;
  }

  public int getPlaylistType()
  {
    return this.mPlaylistType;
  }

  public String getSecondaryName(Context paramContext)
  {
    Object localObject1;
    if ((this.mPlaylistType == 71) && (!TextUtils.isEmpty(this.mOwnerName)))
      localObject1 = this.mOwnerName;
    while (true)
    {
      return localObject1;
      Object localObject2 = new Object();
      MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, localObject2);
      try
      {
        int i = this.mPlaylistType;
        String str = LabelUtils.getPlaylistSecondaryLabel(paramContext, localMusicPreferences, i);
        localObject1 = str;
        MusicPreferences.releaseMusicPreferences(localObject2);
      }
      finally
      {
        MusicPreferences.releaseMusicPreferences(localObject2);
      }
    }
  }

  public String getShareToken()
  {
    return this.mShareToken;
  }

  public int getSuggestedPositionSearchRadius()
  {
    return 1000;
  }

  public boolean hasArtistArt()
  {
    return true;
  }

  public boolean hasStablePrimaryIds()
  {
    return false;
  }

  public boolean hasUniqueAudioId()
  {
    return false;
  }

  public boolean isAllLocal(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "isAllLocal";
    Uri localUri = MusicContent.Playlists.getPlaylistUri(this.mId);
    Context localContext = paramContext;
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
        int i = Log.e("PlaylistSongList", str2);
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

  public boolean isEditable()
  {
    if ((this.mPlaylistType != 50) && (this.mPlaylistType != 71) && (this.mPlaylistType != 70));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isSelectedForOfflineCaching(Context paramContext, IStoreService paramIStoreService)
  {
    try
    {
      long l1 = this.mId;
      boolean bool1 = paramIStoreService.isPlaylistSelectedAsKeepOn(l1);
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
        int i = Log.w("PlaylistSongList", str, localRemoteException);
        boolean bool2 = false;
      }
    }
  }

  public void refreshMetaData(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "playlist_name";
    Uri localUri = MusicContent.Playlists.getPlaylistUri(this.mId);
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
    switch (this.mPlaylistType)
    {
    default:
    case 0:
    case 1:
    case 10:
    case 71:
    }
    for (boolean bool = false; ; bool = true)
      return bool;
  }

  public boolean supportsOfflineCaching()
  {
    switch (this.mPlaylistType)
    {
    default:
    case 0:
    case 1:
    case 100:
    }
    for (boolean bool = false; ; bool = true)
      return bool;
  }

  protected static class PlaylistCursor extends MediaList.MediaCursor
  {
    Cursor mCursor;
    long mId;
    ContentResolver mResolver;

    public PlaylistCursor(Context paramContext, Cursor paramCursor, long paramLong)
    {
      super();
      this.mId = paramLong;
      this.mCursor = paramCursor;
      ContentResolver localContentResolver = paramContext.getContentResolver();
      this.mResolver = localContentResolver;
    }

    public void moveItem(int paramInt1, int paramInt2)
    {
      if (paramInt1 != paramInt2)
        return;
      int i = this.mCursor.getColumnIndexOrThrow("_id");
      if (!this.mCursor.moveToPosition(paramInt1))
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("Failed to move item. Invalid \"from\" position: ").append(paramInt1).append(". Cursor size:");
        int j = this.mCursor.getCount();
        String str1 = j;
        int k = Log.e("PlaylistSongList", str1);
        return;
      }
      long l1 = this.mCursor.getLong(i);
      long l2 = 0L;
      if (paramInt2 > 0)
      {
        if (paramInt1 > paramInt2)
          paramInt2 += -1;
        if (!this.mCursor.moveToPosition(paramInt2))
        {
          StringBuilder localStringBuilder2 = new StringBuilder().append("Failed to move item. Invalid \"to\" position: ").append(paramInt2).append(". Cursor size:");
          int m = this.mCursor.getCount();
          String str2 = m;
          int n = Log.e("PlaylistSongList", str2);
          return;
        }
        l2 = this.mCursor.getLong(i);
      }
      ContentResolver localContentResolver = this.mResolver;
      long l3 = this.mId;
      MusicContent.Playlists.movePlaylistItem(localContentResolver, l3, l1, l2);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.PlaylistSongList
 * JD-Core Version:    0.6.2
 */