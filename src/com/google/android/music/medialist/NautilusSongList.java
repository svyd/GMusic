package com.google.android.music.medialist;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.store.MusicContent;
import com.google.android.music.store.ProjectionUtils;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;

public abstract class NautilusSongList extends ExternalSongList
  implements NautilusMediaList
{
  public NautilusSongList()
  {
    super(localDomain, false);
    setFlag(1);
    setFlag(2);
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

  public boolean isAddToStoreSupported()
  {
    return true;
  }

  public boolean isAllInLibrary(Context paramContext)
  {
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "_id";
    Uri localUri = getFullContentUri(paramContext);
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str);
    if (localCursor != null);
    while (true)
    {
      boolean bool2;
      try
      {
        if (localCursor.moveToNext())
        {
          boolean bool1 = ProjectionUtils.isFauxNautilusId(localCursor.getLong(0));
          if (bool1)
          {
            Store.safeClose(localCursor);
            return bool2;
          }
        }
        else
        {
          Store.safeClose(localCursor);
          bool2 = true;
        }
      }
      finally
      {
        Store.safeClose(localCursor);
      }
    }
  }

  public boolean supportsAppendToPlaylist()
  {
    return true;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.NautilusSongList
 * JD-Core Version:    0.6.2
 */