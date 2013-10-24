package com.google.android.music.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.android.music.download.artwork.ArtDownloadService;
import com.google.android.music.download.cache.CacheService;
import com.google.android.music.download.keepon.KeeponSchedulingService;
import com.google.android.music.download.ringtone.RingtoneSchedulingService;
import com.google.android.music.log.Log;
import com.google.android.music.store.KeepOnUpdater.SyncListener;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;

public class MusicCommunicator extends BroadcastReceiver
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);

  private static void startServiceOrFail(Context paramContext, Intent paramIntent)
  {
    if (paramContext.startService(paramIntent) != null)
      return;
    String str = "Could not start service: " + paramIntent;
    throw new IllegalArgumentException(str);
  }

  public void onReceive(Context paramContext, Intent paramIntent)
  {
    boolean bool = true;
    String str1 = paramIntent.getAction();
    if ("com.google.android.music.accountchanged".equals(str1));
    while (true)
    {
      if (!bool)
        return;
      Intent localIntent1 = new Intent(paramContext, CacheService.class);
      Intent localIntent2 = localIntent1.setAction("com.google.android.music.download.cache.CacheService.CLEAR_ORPHANED");
      startServiceOrFail(paramContext, localIntent1);
      return;
      if ("com.google.android.music.NEW_SHOULDKEEPON".equals(str1))
      {
        bool = paramIntent.getBooleanExtra("deleteCachedFiles", true);
        Intent localIntent3 = new Intent(paramContext, KeeponSchedulingService.class);
        Intent localIntent4 = localIntent3.setAction("com.google.android.music.download.keepon.KeeponSchedulingService.START_DOWNLOAD");
        Intent localIntent5 = localIntent3.putExtras(paramIntent);
        startServiceOrFail(paramContext, localIntent3);
      }
      else if ("com.google.android.music.RINGTONE_REQUEST_START".equals(str1))
      {
        Intent localIntent6 = new Intent(paramContext, RingtoneSchedulingService.class);
        Intent localIntent7 = localIntent6.setAction("com.google.android.music.download.ringtone.RingtoneSchedulingService.START_DOWNLOAD");
        startServiceOrFail(paramContext, localIntent6);
        bool = false;
      }
      else if (!"com.google.android.music.CLEAN_ORPHANED_FILES".equals(str1))
      {
        if ("com.google.android.music.SYNC_COMPLETE".equals(str1))
        {
          Intent localIntent8 = new Intent(paramContext, KeepOnUpdater.SyncListener.class);
          Intent localIntent9 = localIntent8.setAction(str1);
          startServiceOrFail(paramContext, localIntent8);
          Intent localIntent10 = new Intent(paramContext, ArtDownloadService.class);
          Intent localIntent11 = localIntent10.setAction(str1);
          startServiceOrFail(paramContext, localIntent10);
          bool = false;
        }
        else
        {
          StringBuilder localStringBuilder = new StringBuilder().append("Received unknown broadcast: ");
          String str2 = paramIntent.getAction();
          String str3 = str2;
          Log.e("MusicCommunicator", str3);
          bool = false;
        }
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.MusicCommunicator
 * JD-Core Version:    0.6.2
 */