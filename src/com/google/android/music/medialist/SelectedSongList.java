package com.google.android.music.medialist;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import com.google.android.music.download.artwork.AlbumIdSink;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.store.MusicContent.XAudio;
import com.google.android.music.store.Store;
import com.google.android.music.utils.AlbumArtUtils;
import com.google.android.music.utils.AlbumArtUtils.AlbumIdIterator;
import com.google.android.music.utils.AlbumArtUtils.AlbumIdIteratorFactory;
import com.google.android.music.utils.MusicUtils;
import java.util.NoSuchElementException;

public class SelectedSongList extends SongList
{
  private static final String[] sSongListCols = arrayOfString;
  private final long[] mMusicIds;
  private final String mName;

  static
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "album_id";
  }

  public SelectedSongList(String paramString, long[] paramArrayOfLong)
  {
    super(-1, false, true);
    this.mMusicIds = paramArrayOfLong;
    this.mName = paramString;
  }

  public int appendToPlaylist(Context paramContext, long paramLong)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    long[] arrayOfLong = this.mMusicIds;
    return MusicContent.Playlists.appendItemsToPlayList(localContentResolver, paramLong, arrayOfLong);
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[2];
    String str1 = this.mName;
    arrayOfString[0] = str1;
    String str2 = encodeArg(this.mMusicIds);
    arrayOfString[1] = str2;
    return arrayOfString;
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.XAudio.getSelectedAudioUri(this.mMusicIds);
  }

  public Bitmap getImage(final Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, boolean paramBoolean)
  {
    if (this.mMusicIds.length > 0);
    for (long l = this.mMusicIds[1]; ; l = 1L)
    {
      String str = this.mName;
      AlbumArtUtils.AlbumIdIteratorFactory local1 = new AlbumArtUtils.AlbumIdIteratorFactory()
      {
        public AlbumArtUtils.AlbumIdIterator createIterator()
        {
          SelectedSongList localSelectedSongList = SelectedSongList.this;
          Context localContext = paramContext;
          return new SelectedSongList.SelectedSongListAlbumIdIterator(localSelectedSongList, localContext);
        }
      };
      Context localContext = paramContext;
      int i = paramInt1;
      int j = paramInt2;
      AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
      boolean bool = paramBoolean;
      return AlbumArtUtils.getFauxAlbumArt(localContext, 2, true, l, i, j, str, null, local1, localAlbumIdSink, bool);
    }
  }

  public String getName(Context paramContext)
  {
    return this.mName;
  }

  public String getSecondaryName(Context paramContext)
  {
    return "";
  }

  public boolean supportsAppendToPlaylist()
  {
    return true;
  }

  private class SelectedSongListAlbumIdIterator
    implements AlbumArtUtils.AlbumIdIterator
  {
    private int mCount;
    private Cursor mCursor;
    private int mPosition;

    public SelectedSongListAlbumIdIterator(Context arg2)
    {
      Context localContext1;
      Uri localUri = SelectedSongList.this.getContentUri(localContext1);
      String[] arrayOfString1 = SelectedSongList.sSongListCols;
      Context localContext2 = localContext1;
      String[] arrayOfString2 = null;
      String str = null;
      Cursor localCursor = MusicUtils.query(localContext2, localUri, arrayOfString1, null, arrayOfString2, str);
      this.mCursor = localCursor;
      int i = this.mCursor.getCount();
      this.mCount = i;
    }

    public void close()
    {
      Store.safeClose(this.mCursor);
    }

    public boolean hasNext()
    {
      int i = this.mPosition;
      int j = this.mCount;
      if (i < j);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public long next()
    {
      int i = this.mPosition;
      int j = this.mCount;
      if (i >= j)
        throw new NoSuchElementException();
      Cursor localCursor = this.mCursor;
      int k = this.mPosition;
      int m = k + 1;
      this.mPosition = m;
      if (!localCursor.moveToPosition(k))
        throw new NoSuchElementException();
      return this.mCursor.getLong(0);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.SelectedSongList
 * JD-Core Version:    0.6.2
 */