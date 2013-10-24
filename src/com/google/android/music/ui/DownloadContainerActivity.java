package com.google.android.music.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import com.google.android.music.download.keepon.KeeponSchedulingService;
import com.google.android.music.download.keepon.KeeponSchedulingServiceConnection;
import com.google.android.music.eventlog.MusicEventLogger;

@Deprecated
public class DownloadContainerActivity extends BaseActivity
{
  private final KeeponSchedulingServiceConnection mKeeponSchedulingServiceConnection;
  private MusicEventLogger mTracker;

  public DownloadContainerActivity()
  {
    KeeponSchedulingServiceConnection localKeeponSchedulingServiceConnection = new KeeponSchedulingServiceConnection();
    this.mKeeponSchedulingServiceConnection = localKeeponSchedulingServiceConnection;
  }

  public String getPageUrlForTracking()
  {
    return "downloadQueue";
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    String str = getResources().getString(2131231391);
    setActionBarTitle(str);
    if (getContent() == null)
    {
      DownloadContainerFragment localDownloadContainerFragment = new DownloadContainerFragment();
      replaceContent(localDownloadContainerFragment, false);
    }
    KeeponSchedulingServiceConnection localKeeponSchedulingServiceConnection = this.mKeeponSchedulingServiceConnection;
    Intent localIntent = new Intent(this, KeeponSchedulingService.class);
    boolean bool = localKeeponSchedulingServiceConnection.bindService(this, localIntent, 1);
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this);
    this.mTracker = localMusicEventLogger;
  }

  protected void onDestroy()
  {
    this.mKeeponSchedulingServiceConnection.unbindService(this);
    super.onDestroy();
  }

  protected void onResume()
  {
    super.onResume();
    MusicEventLogger localMusicEventLogger = this.mTracker;
    String str = getPageUrlForTracking();
    Object[] arrayOfObject = new Object[0];
    localMusicEventLogger.trackScreenView(this, str, arrayOfObject);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.DownloadContainerActivity
 * JD-Core Version:    0.6.2
 */