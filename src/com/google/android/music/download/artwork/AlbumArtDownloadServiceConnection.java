package com.google.android.music.download.artwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.net.Uri;
import android.util.Log;
import com.google.android.music.store.MusicContent.Albums;
import com.google.android.music.utils.AlbumArtUtils;

public class AlbumArtDownloadServiceConnection extends ArtDownloadServiceConnection<Long>
{
  private AlbumArtChangedBroadcastReceiver mAlbumArtChangedBroadcastReceiver;

  private void createArtChangedBroadcastReceiver(Context paramContext)
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.google.android.music.AlbumArtChanged");
    localIntentFilter.addCategory("android.intent.category.DEFAULT");
    Uri localUri = MusicContent.Albums.getAlbumsUri(0L);
    String str1 = localUri.getAuthority();
    localIntentFilter.addDataAuthority(str1, null);
    String str2 = localUri.getScheme();
    localIntentFilter.addDataScheme(str2);
    try
    {
      localIntentFilter.addDataType("vnd.android.cursor.item/vnd.google.music.album");
      AlbumArtChangedBroadcastReceiver localAlbumArtChangedBroadcastReceiver1 = new AlbumArtChangedBroadcastReceiver(null);
      this.mAlbumArtChangedBroadcastReceiver = localAlbumArtChangedBroadcastReceiver1;
      AlbumArtChangedBroadcastReceiver localAlbumArtChangedBroadcastReceiver2 = this.mAlbumArtChangedBroadcastReceiver;
      Intent localIntent = paramContext.registerReceiver(localAlbumArtChangedBroadcastReceiver2, localIntentFilter);
      return;
    }
    catch (IntentFilter.MalformedMimeTypeException localMalformedMimeTypeException)
    {
      while (true)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Error setting data type on filter: ");
        String str3 = localMalformedMimeTypeException.getMessage();
        String str4 = str3;
        int i = Log.e("AlbumArtDownload", str4);
      }
    }
  }

  protected int getArtQueryWatchedClientPriority()
  {
    return 10;
  }

  protected String getWatchedArtIdListKey()
  {
    return "albumIdList";
  }

  protected void handleArtChanged(Long paramLong)
  {
    AlbumArtUtils.handleAlbumArtChanged(paramLong);
    super.handleArtChanged(paramLong);
  }

  public void onCreate(Context paramContext)
  {
    super.onCreate(paramContext);
    createArtChangedBroadcastReceiver(paramContext);
  }

  protected void syncComplete()
  {
    AlbumArtUtils.handleSyncComplete();
    super.syncComplete();
  }

  private class AlbumArtChangedBroadcastReceiver extends BroadcastReceiver
  {
    private AlbumArtChangedBroadcastReceiver()
    {
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      long l = paramIntent.getLongExtra("albumId", 65535L);
      AlbumArtDownloadServiceConnection localAlbumArtDownloadServiceConnection = AlbumArtDownloadServiceConnection.this;
      Long localLong = Long.valueOf(l);
      localAlbumArtDownloadServiceConnection.handleArtChanged(localLong);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.artwork.AlbumArtDownloadServiceConnection
 * JD-Core Version:    0.6.2
 */