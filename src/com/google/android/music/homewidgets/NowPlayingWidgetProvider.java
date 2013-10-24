package com.google.android.music.homewidgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import com.android.music.MediaAppWidgetProvider;
import com.google.android.music.NowPlayingRemoteViewUtils;
import com.google.android.music.NowPlayingRemoteViewUtils.Config;
import com.google.android.music.playback.MusicPlaybackService;
import com.google.android.music.utils.MusicUtils;

public class NowPlayingWidgetProvider extends AppWidgetProvider
{
  private static NowPlayingRemoteViewUtils.Config getWidgetConfig(Context paramContext, int paramInt)
  {
    AppWidgetProviderInfo localAppWidgetProviderInfo = AppWidgetManager.getInstance(paramContext).getAppWidgetInfo(paramInt);
    String str1 = localAppWidgetProviderInfo.provider.getClassName();
    String str2 = SmallNowPlayingProvider.class.getName();
    NowPlayingRemoteViewUtils.Config localConfig;
    if (str1.equals(str2))
      localConfig = SmallNowPlayingProvider.getConfig(paramContext, localAppWidgetProviderInfo);
    while (true)
    {
      return localConfig;
      String str3 = LargeNowPlayingProvider.class.getName();
      if (str1.equals(str3))
      {
        localConfig = LargeNowPlayingProvider.getConfig(paramContext, localAppWidgetProviderInfo);
      }
      else
      {
        String str4 = MediaAppWidgetProvider.class.getName();
        if (str1.equals(str4))
        {
          localConfig = SmallNowPlayingProvider.getConfig(paramContext, localAppWidgetProviderInfo);
        }
        else
        {
          String str5 = "Unable to find widget provider: " + str1;
          int i = Log.w("NowPlayingWidget", str5);
          localConfig = null;
        }
      }
    }
  }

  public static void notifyChange(Context paramContext, Intent paramIntent, String paramString)
  {
    if ((!"com.android.music.metachanged".equals(paramString)) && (!"com.android.music.playstatechanged".equals(paramString)))
      return;
    AppWidgetManager localAppWidgetManager = AppWidgetManager.getInstance(paramContext);
    ComponentName localComponentName1 = new ComponentName(paramContext, MediaAppWidgetProvider.class);
    int[] arrayOfInt1 = localAppWidgetManager.getAppWidgetIds(localComponentName1);
    performUpdate(paramContext, paramIntent, arrayOfInt1);
    ComponentName localComponentName2 = new ComponentName(paramContext, SmallNowPlayingProvider.class);
    int[] arrayOfInt2 = localAppWidgetManager.getAppWidgetIds(localComponentName2);
    performUpdate(paramContext, paramIntent, arrayOfInt2);
    ComponentName localComponentName3 = new ComponentName(paramContext, LargeNowPlayingProvider.class);
    int[] arrayOfInt3 = localAppWidgetManager.getAppWidgetIds(localComponentName3);
    performUpdate(paramContext, paramIntent, arrayOfInt3);
  }

  /** @deprecated */
  static void performUpdate(Context paramContext, final Intent paramIntent, final int[] paramArrayOfInt)
  {
    try
    {
      MusicUtils.runAsync(new Runnable()
      {
        public void run()
        {
          AppWidgetManager localAppWidgetManager = AppWidgetManager.getInstance(NowPlayingWidgetProvider.this);
          int[] arrayOfInt = paramArrayOfInt;
          int i = arrayOfInt.length;
          int j = 0;
          while (true)
          {
            if (j >= i)
              return;
            int k = arrayOfInt[j];
            try
            {
              NowPlayingRemoteViewUtils.Config localConfig = NowPlayingWidgetProvider.getWidgetConfig(NowPlayingWidgetProvider.this, k);
              if (localConfig != null)
              {
                Context localContext = NowPlayingWidgetProvider.this;
                Intent localIntent = paramIntent;
                RemoteViews localRemoteViews = NowPlayingRemoteViewUtils.createViews(localContext, localIntent, localConfig);
                if (localRemoteViews != null)
                  localAppWidgetManager.updateAppWidget(k, localRemoteViews);
              }
              while (true)
              {
                j += 1;
                break;
                String str = "Failed to find config for widget: " + k;
                int m = Log.e("NowPlayingWidget", str);
              }
            }
            catch (Exception localException)
            {
              while (true)
                int n = Log.e("NowPlayingWidget", "Failed to create views for Music widget", localException);
            }
          }
        }
      });
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void onUpdate(Context paramContext, AppWidgetManager paramAppWidgetManager, int[] paramArrayOfInt)
  {
    Intent localIntent1 = new Intent(paramContext, UpdateService.class);
    Intent localIntent2 = localIntent1.putExtra("appWidgetIds", paramArrayOfInt);
    ComponentName localComponentName = paramContext.startService(localIntent1);
  }

  public static class UpdateService extends IntentService
  {
    public UpdateService()
    {
      super();
    }

    protected void onHandleIntent(Intent paramIntent)
    {
      int[] arrayOfInt = paramIntent.getIntArrayExtra("appWidgetIds");
      Intent localIntent1 = new Intent();
      Intent localIntent2 = MusicPlaybackService.populateExtrasFromSharedPreferences(this, localIntent1);
      NowPlayingWidgetProvider.performUpdate(this, localIntent1, arrayOfInt);
      Intent localIntent3 = new Intent("com.android.music.musicservicecommand");
      Intent localIntent4 = localIntent3.putExtra("command", "appwidgetupdate");
      Intent localIntent5 = localIntent3.putExtra("device", "any");
      Intent localIntent6 = localIntent3.putExtra("appWidgetIds", arrayOfInt);
      Intent localIntent7 = localIntent3.addFlags(1073741824);
      sendBroadcast(localIntent3);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.homewidgets.NowPlayingWidgetProvider
 * JD-Core Version:    0.6.2
 */