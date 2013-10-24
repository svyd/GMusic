package com.google.android.music.homewidgets;

import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.res.Resources;
import com.google.android.music.NowPlayingRemoteViewUtils;
import com.google.android.music.NowPlayingRemoteViewUtils.Config;

public class LargeNowPlayingProvider extends NowPlayingWidgetProvider
{
  private static NowPlayingRemoteViewUtils.Config sConfig = null;

  public static NowPlayingRemoteViewUtils.Config getConfig(Context paramContext, AppWidgetProviderInfo paramAppWidgetProviderInfo)
  {
    if (sConfig == null)
    {
      int i = paramContext.getResources().getDimensionPixelSize(2131558417);
      int j = paramAppWidgetProviderInfo.initialLayout;
      sConfig = NowPlayingRemoteViewUtils.buildConfig(paramContext, j);
      sConfig.setAlbumArtHeight(i);
      sConfig.setAlbumArtWidth(i);
    }
    return sConfig;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.homewidgets.LargeNowPlayingProvider
 * JD-Core Version:    0.6.2
 */