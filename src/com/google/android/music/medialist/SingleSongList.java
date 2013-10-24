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
import com.google.android.music.utils.MusicUtils;

public class SingleSongList extends SongList
{
  private long mAlbumId = 65535L;
  private String mAlbumName = null;
  private String mArtUrl;
  private long mArtistId = 65535L;
  private String mArtistName = null;
  private final long mAudioId;
  private final String mTrackName;
  private boolean mVariablesResolved = false;

  public SingleSongList(long paramLong, String paramString)
  {
    super(-1, false, true);
    this.mAudioId = paramLong;
    this.mTrackName = paramString;
    if (paramLong > 0L)
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("Invalid local music id:");
    long l = this.mAudioId;
    String str = l;
    throw new IllegalArgumentException(str);
  }

  /** @deprecated */
  // ERROR //
  private void resolveNames(Context paramContext)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 23	com/google/android/music/medialist/SingleSongList:mVariablesResolved	Z
    //   6: astore_2
    //   7: aload_2
    //   8: ifnull +6 -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: iconst_4
    //   15: istore_2
    //   16: iload_2
    //   17: anewarray 66	java/lang/String
    //   20: astore_3
    //   21: aload_3
    //   22: iconst_0
    //   23: ldc 68
    //   25: aastore
    //   26: aload_3
    //   27: iconst_1
    //   28: ldc 70
    //   30: aastore
    //   31: aload_3
    //   32: iconst_2
    //   33: ldc 72
    //   35: aastore
    //   36: aload_3
    //   37: iconst_3
    //   38: ldc 74
    //   40: aastore
    //   41: aload_0
    //   42: aload_1
    //   43: invokevirtual 78	com/google/android/music/medialist/SingleSongList:getContentUri	(Landroid/content/Context;)Landroid/net/Uri;
    //   46: astore 4
    //   48: aload_1
    //   49: aload 4
    //   51: aload_3
    //   52: aconst_null
    //   53: aconst_null
    //   54: aconst_null
    //   55: invokestatic 84	com/google/android/music/utils/MusicUtils:query	(Landroid/content/Context;Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   58: astore 5
    //   60: aload 5
    //   62: astore 6
    //   64: aload 6
    //   66: ifnull +77 -> 143
    //   69: aload 6
    //   71: invokeinterface 90 1 0
    //   76: ifeq +67 -> 143
    //   79: aload 6
    //   81: iconst_0
    //   82: invokeinterface 94 2 0
    //   87: lstore 7
    //   89: aload_0
    //   90: lload 7
    //   92: putfield 27	com/google/android/music/medialist/SingleSongList:mArtistId	J
    //   95: aload 6
    //   97: iconst_1
    //   98: invokeinterface 98 2 0
    //   103: astore 9
    //   105: aload_0
    //   106: aload 9
    //   108: putfield 29	com/google/android/music/medialist/SingleSongList:mArtistName	Ljava/lang/String;
    //   111: aload 6
    //   113: iconst_2
    //   114: invokeinterface 94 2 0
    //   119: lstore 10
    //   121: aload_0
    //   122: lload 10
    //   124: putfield 31	com/google/android/music/medialist/SingleSongList:mAlbumId	J
    //   127: aload 6
    //   129: iconst_3
    //   130: invokeinterface 98 2 0
    //   135: astore 12
    //   137: aload_0
    //   138: aload 12
    //   140: putfield 33	com/google/android/music/medialist/SingleSongList:mAlbumName	Ljava/lang/String;
    //   143: aload 6
    //   145: invokestatic 104	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   148: aload_0
    //   149: iconst_1
    //   150: putfield 23	com/google/android/music/medialist/SingleSongList:mVariablesResolved	Z
    //   153: goto -142 -> 11
    //   156: astore 13
    //   158: aload_0
    //   159: monitorexit
    //   160: aload 13
    //   162: athrow
    //   163: astore_2
    //   164: aload 6
    //   166: invokestatic 104	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   169: aload_2
    //   170: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   2	7	156	finally
    //   16	60	156	finally
    //   143	153	156	finally
    //   164	171	156	finally
    //   69	143	163	finally
  }

  public int appendToPlaylist(Context paramContext, long paramLong)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    long l = this.mAudioId;
    return MusicContent.Playlists.appendItemToPlayList(localContentResolver, paramLong, l);
  }

  public long getAlbumId(Context paramContext)
  {
    resolveNames(paramContext);
    return this.mAlbumId;
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[2];
    StringBuilder localStringBuilder = new StringBuilder().append("");
    long l = this.mAudioId;
    String str1 = l;
    arrayOfString[0] = str1;
    String str2 = this.mTrackName;
    arrayOfString[1] = str2;
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
      Uri localUri = getContentUri(paramContext);
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

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.XAudio.getAudioUri(this.mAudioId);
  }

  public long getId()
  {
    return this.mAudioId;
  }

  public Bitmap getImage(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, boolean paramBoolean)
  {
    resolveNames(paramContext);
    long l = Long.valueOf(this.mAlbumId).longValue();
    String str1 = this.mAlbumName;
    String str2 = this.mArtistName;
    Context localContext = paramContext;
    int i = paramInt1;
    int j = paramInt2;
    AlbumIdSink localAlbumIdSink = paramAlbumIdSink;
    boolean bool = paramBoolean;
    return AlbumArtUtils.getArtwork(localContext, l, i, j, true, str1, str2, localAlbumIdSink, bool);
  }

  public String getName(Context paramContext)
  {
    return this.mTrackName;
  }

  public String getSecondaryName(Context paramContext)
  {
    resolveNames(paramContext);
    return this.mArtistName;
  }

  public boolean supportsAppendToPlaylist()
  {
    return true;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.SingleSongList
 * JD-Core Version:    0.6.2
 */