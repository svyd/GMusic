package com.google.android.music.medialist;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.download.artwork.AlbumIdSink;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.store.MusicContent.SharedWithMePlaylist.Members;
import com.google.android.music.utils.LabelUtils;

public class SharedWithMeSongList extends ExternalSongList
{
  private final String mArtUrl;
  private final String mDescription;
  private final long mId;
  private final String mName;
  private final String mOwnerName;
  private final String mOwnerProfilePhotoUrl;
  private final String mShareToken;

  public SharedWithMeSongList(long paramLong, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    super(localDomain, false);
    setFlag(1);
    setFlag(2);
    this.mId = paramLong;
    this.mShareToken = paramString1;
    this.mName = paramString2;
    this.mDescription = paramString3;
    this.mOwnerName = paramString4;
    this.mArtUrl = paramString5;
    this.mOwnerProfilePhotoUrl = paramString6;
  }

  public long[] addToStore(Context paramContext, boolean paramBoolean)
  {
    Uri localUri = getContentUri(paramContext);
    return MusicContent.addExternalSongsToStore(paramContext, localUri, paramBoolean);
  }

  public int appendToPlaylist(Context paramContext, long paramLong)
  {
    boolean bool = false;
    long[] arrayOfLong = addToStore(paramContext, bool);
    int i;
    if ((arrayOfLong != null) && (arrayOfLong.length > 0))
      i = new SelectedSongList("", arrayOfLong).appendToPlaylist(paramContext, paramLong);
    return i;
  }

  public boolean containsRemoteItems(Context paramContext)
  {
    return true;
  }

  public void followPlaylist(Context paramContext)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    String str1 = this.mName;
    String str2 = this.mDescription;
    String str3 = this.mOwnerName;
    String str4 = this.mShareToken;
    String str5 = this.mArtUrl;
    Uri localUri = MusicContent.Playlists.followSharedPlaylist(localContentResolver, str1, str2, str3, str4, str5);
  }

  public String getAlbumArtUrl(Context paramContext)
  {
    return this.mArtUrl;
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[7];
    StringBuilder localStringBuilder = new StringBuilder().append("");
    long l = this.mId;
    String str1 = l;
    arrayOfString[0] = str1;
    String str2 = this.mShareToken;
    arrayOfString[1] = str2;
    String str3 = this.mName;
    arrayOfString[2] = str3;
    String str4 = this.mDescription;
    arrayOfString[3] = str4;
    String str5 = this.mOwnerName;
    arrayOfString[4] = str5;
    String str6 = this.mArtUrl;
    arrayOfString[5] = str6;
    String str7 = this.mOwnerProfilePhotoUrl;
    arrayOfString[6] = str7;
    return arrayOfString;
  }

  public String getArtUrl()
  {
    return this.mArtUrl;
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.SharedWithMePlaylist.Members.getUri(this.mShareToken);
  }

  public Bitmap getImage(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, boolean paramBoolean)
  {
    return null;
  }

  public String getName(Context paramContext)
  {
    return this.mName;
  }

  public String getOwnerProfilePhotoUrl()
  {
    return this.mOwnerProfilePhotoUrl;
  }

  public String getSecondaryName(Context paramContext)
  {
    Object localObject1;
    if (!TextUtils.isEmpty(this.mOwnerName))
      localObject1 = this.mOwnerName;
    while (true)
    {
      return localObject1;
      Object localObject2 = new Object();
      MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, localObject2);
      int i = 70;
      try
      {
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

  public boolean hasArtistArt()
  {
    return true;
  }

  public boolean isAddToStoreSupported()
  {
    return true;
  }

  public boolean supportsAppendToPlaylist()
  {
    return true;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.SharedWithMeSongList
 * JD-Core Version:    0.6.2
 */