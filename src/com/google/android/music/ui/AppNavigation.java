package com.google.android.music.ui;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.content.IntentCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;
import com.google.android.music.DownloadQueueActivity;
import com.google.android.music.download.IntentConstants;
import com.google.android.music.medialist.SongList;
import com.google.android.music.preferences.MusicPreferences;

public class AppNavigation
{
  public static PendingIntent getHomeScreenPendingIntent(Context paramContext)
  {
    Intent localIntent = HomeActivity.createHomeScreenIntent(paramContext);
    return PendingIntent.getActivity(paramContext, 0, localIntent, 0);
  }

  public static PendingIntent getIntentToOpenApp(Context paramContext)
  {
    Intent localIntent1 = IntentCompat.makeMainActivity(new ComponentName(paramContext, HomeActivity.class));
    Intent localIntent2 = localIntent1.setFlags(268435456);
    return PendingIntent.getActivity(paramContext, 0, localIntent1, 0);
  }

  public static PendingIntent getIntentToOpenAppWithPlaybackScreen(Context paramContext)
  {
    Intent localIntent = NowPlayingLaunchDelegateActivity.getLaunchIntent(paramContext);
    return PendingIntent.getActivity(paramContext, 0, localIntent, 0);
  }

  public static PendingIntent getIntentToOpenDownloadQueueActivity(Context paramContext)
  {
    if (MusicPreferences.usingNewDownloadUI(paramContext));
    for (Intent localIntent1 = new Intent(paramContext, DownloadContainerActivity.class); ; localIntent1 = new Intent(paramContext, DownloadQueueActivity.class))
    {
      Intent localIntent2 = localIntent1.setFlags(67108864);
      return PendingIntent.getActivity(paramContext, 0, localIntent1, 0);
    }
  }

  public static Intent getShowSonglistIntent(Context paramContext, SongList paramSongList)
  {
    return TrackContainerActivity.buildStartIntent(paramContext, paramSongList, null);
  }

  public static void goHome(Context paramContext)
  {
    Intent localIntent1 = HomeActivity.createHomeScreenIntent(paramContext);
    Intent localIntent2 = localIntent1.addFlags(335544320);
    paramContext.startActivity(localIntent1);
  }

  public static void openHelpLink(Context paramContext, String paramString)
  {
    Intent localIntent1 = new Intent("android.intent.action.VIEW");
    Uri localUri = Uri.parse(paramString);
    Intent localIntent2 = localIntent1.setData(localUri);
    Intent localIntent3 = localIntent1.addFlags(268435456);
    Intent localIntent4 = localIntent1.addFlags(32768);
    Intent localIntent5 = localIntent1.addFlags(524288);
    if (paramContext.getPackageManager().resolveActivity(localIntent1, 0) == null)
    {
      String str = paramContext.getResources().getString(2131231376);
      Toast.makeText(paramContext, str, 0).show();
      return;
    }
    paramContext.startActivity(localIntent1);
  }

  public static void openNowPlayingDrawer(Context paramContext)
  {
    LocalBroadcastManager localLocalBroadcastManager = LocalBroadcastManager.getInstance(paramContext);
    Intent localIntent = new Intent("com.google.android.music.OPEN_DRAWER");
    boolean bool = localLocalBroadcastManager.sendBroadcast(localIntent);
  }

  public static void showHomeScreen(Context paramContext, HomeActivity.Screen paramScreen)
  {
    HomeActivity.Screen localScreen = HomeActivity.Screen.SHOP;
    if (paramScreen == localScreen)
    {
      Intent localIntent1 = IntentConstants.getMusicStoreIntent(UIStateManager.getInstance().getPrefs());
      paramContext.startActivity(localIntent1);
      return;
    }
    Intent localIntent2 = HomeActivity.createHomeScreenIntent(paramContext, paramScreen);
    BaseActivity.putExtraDrawerState(localIntent2, true, true);
    paramContext.startActivity(localIntent2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.AppNavigation
 * JD-Core Version:    0.6.2
 */