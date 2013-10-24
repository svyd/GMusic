package com.google.android.music.homewidgets;

import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.res.Resources;
import com.google.android.gsf.Gservices;
import com.google.android.music.NowPlayingRemoteViewUtils;
import com.google.android.music.NowPlayingRemoteViewUtils.Config;

public class SmallNowPlayingProvider extends NowPlayingWidgetProvider
{
  private static NowPlayingRemoteViewUtils.Config sConfig = null;

  public static NowPlayingRemoteViewUtils.Config getConfig(Context paramContext, AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    if (sConfig == null)
    {
      int i = paramContext.getResources().getDimensionPixelSize(2131558416);
      int j = paramAppWidgetProviderInfo.initialLayout;
      sConfig = NowPlayingRemoteViewUtils.buildConfig(paramContext, j);
      sConfig.setAlbumArtHeight(i);
      sConfig.setAlbumArtWidth(i);
      if (!Gservices.getBoolean(paramContext.getApplicationContext().getContentResolver(), "music_use_star_ratings", false))
        break label72;
      sConfig.hideElements(1024);
    }
    while (true)
    {
      return sConfig;
      label72: sConfig.showElements(1024);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.homewidgets.SmallNowPlayingProvider
 * JD-Core Version:    0.6.2
 */