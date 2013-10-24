package com.google.android.music.medialist;

import android.content.Context;
import android.database.Cursor;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.utils.MusicUtils;

public class PlayQueueSongList extends PlaylistSongList
{
  public PlayQueueSongList(long paramLong)
  {
  }

  protected MediaList.MediaCursor createMediaCursor(Context paramContext, Cursor paramCursor)
  {
    long l = getPlaylistId();
    return new PlayQueueCursor(paramContext, paramCursor, l);
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[1];
    String str = Long.toString(getPlaylistId());
    arrayOfString[0] = str;
    return arrayOfString;
  }

  public boolean hasStablePrimaryIds()
  {
    return true;
  }

  private static class PlayQueueCursor extends PlaylistSongList.PlaylistCursor
  {
    public PlayQueueCursor(Context paramContext, Cursor paramCursor, long paramLong)
    {
      super(paramCursor, paramLong);
    }

    private void disableGroupPlay()
    {
      if (MusicUtils.sService == null)
        return;
      try
      {
        MusicUtils.sService.disableGroupPlay();
        return;
      }
      catch (RemoteException localRemoteException)
      {
        int i = Log.e("PlayQueueSongList", "disableGroupPlay() failed");
      }
    }

    public void moveItem(int paramInt1, int paramInt2)
    {
      disableGroupPlay();
      super.moveItem(paramInt1, paramInt2);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.PlayQueueSongList
 * JD-Core Version:    0.6.2
 */