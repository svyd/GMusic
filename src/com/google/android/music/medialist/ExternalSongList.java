package com.google.android.music.medialist;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.store.MusicFile;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;

public abstract class ExternalSongList extends SongList
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.MEDIA_LIST);
  private int mFlags = 2147483647;

  public ExternalSongList(ContentIdentifier.Domain paramDomain)
  {
    this(paramDomain, true);
  }

  public ExternalSongList(ContentIdentifier.Domain paramDomain, boolean paramBoolean)
  {
    super(0, paramDomain, false, paramBoolean);
    if (paramDomain == null)
      throw new NullPointerException();
    ContentIdentifier.Domain localDomain = ContentIdentifier.Domain.DEFAULT;
    if (paramDomain == localDomain)
      throw new IllegalArgumentException();
    unsetFlag(1);
    unsetFlag(2);
    unsetFlag(4);
    unsetFlag(8);
    unsetFlag(16);
    unsetFlag(32);
    unsetFlag(64);
    unsetFlag(128);
    unsetFlag(256);
    unsetFlag(512);
  }

  public long[] addToStore(Context paramContext, boolean paramBoolean)
  {
    throw new UnsupportedOperationException();
  }

  public String getAlbumArtUrl(Context paramContext)
  {
    return null;
  }

  public Uri getContentUri(Context paramContext)
  {
    return null;
  }

  public int getFlags()
  {
    return this.mFlags;
  }

  public int getItemCount()
  {
    throw new UnsupportedOperationException();
  }

  public MusicFile getMusicFile(int paramInt, MusicFile paramMusicFile)
  {
    throw new UnsupportedOperationException();
  }

  public Cursor getSongCursor(Context paramContext, ContentIdentifier paramContentIdentifier, String[] paramArrayOfString)
  {
    return getCursor(paramContext, paramArrayOfString, null);
  }

  public boolean isAddToStoreSupported()
  {
    return false;
  }

  protected final void setFlag(int paramInt)
  {
    int i = this.mFlags | paramInt;
    this.mFlags = i;
  }

  protected final void unsetFlag(int paramInt)
  {
    int i = this.mFlags;
    int j = paramInt ^ 0xFFFFFFFF;
    int k = i & j;
    this.mFlags = k;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.ExternalSongList
 * JD-Core Version:    0.6.2
 */