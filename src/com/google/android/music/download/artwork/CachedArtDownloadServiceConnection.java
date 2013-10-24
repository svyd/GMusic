package com.google.android.music.download.artwork;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class CachedArtDownloadServiceConnection extends ArtDownloadServiceConnection<String>
{
  private ArtChangedBroadcastReceiver mArtChangedBroadcastReceiver;

  private void createArtChangedBroadcastReceiver(Context paramContext)
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.google.android.music.ArtChanged");
    localIntentFilter.addCategory("android.intent.category.DEFAULT");
    ArtChangedBroadcastReceiver localArtChangedBroadcastReceiver1 = new ArtChangedBroadcastReceiver(null);
    this.mArtChangedBroadcastReceiver = localArtChangedBroadcastReceiver1;
    ArtChangedBroadcastReceiver localArtChangedBroadcastReceiver2 = this.mArtChangedBroadcastReceiver;
    Intent localIntent = paramContext.registerReceiver(localArtChangedBroadcastReceiver2, localIntentFilter);
  }

  protected int getArtQueryWatchedClientPriority()
  {
    return 20;
  }

  protected String getWatchedArtIdListKey()
  {
    return "remoteUrlList";
  }

  protected void handleArtChanged(String paramString)
  {
    super.handleArtChanged(paramString);
  }

  public void onCreate(Context paramContext)
  {
    super.onCreate(paramContext);
    createArtChangedBroadcastReceiver(paramContext);
  }

  private class ArtChangedBroadcastReceiver extends BroadcastReceiver
  {
    private ArtChangedBroadcastReceiver()
    {
    }

    public void onReceive(Context paramContext, Intent paramIntent)
    {
      String str = paramIntent.getStringExtra("remoteUrl");
      CachedArtDownloadServiceConnection.this.handleArtChanged(str);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.artwork.CachedArtDownloadServiceConnection
 * JD-Core Version:    0.6.2
 */