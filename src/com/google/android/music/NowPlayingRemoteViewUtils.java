package com.google.android.music;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RemoteViews;
import com.google.android.music.playback.MusicPlaybackService;
import com.google.android.music.ui.AppNavigation;
import com.google.android.music.utils.AlbumArtUtils;
import com.google.android.music.utils.MusicUtils;

public class NowPlayingRemoteViewUtils
{
  public static Config buildConfig(Context paramContext, int paramInt)
  {
    boolean bool1 = true;
    Config localConfig = new Config();
    localConfig.setLayoutId(paramInt);
    try
    {
      View localView = LayoutInflater.from(paramContext).inflate(paramInt, null);
      int i = 1;
      label67: label91: label115: label244: int j;
      if (localView.findViewById(2131296516) != null)
      {
        boolean bool2 = true;
        localConfig.setElement(i, bool2);
        i = 32;
        if (localView.findViewById(2131296536) == null)
          break label308;
        bool2 = true;
        localConfig.setElement(i, bool2);
        i = 4;
        if (localView.findViewById(2131296488) == null)
          break label314;
        bool2 = true;
        localConfig.setElement(i, bool2);
        i = 2;
        if (localView.findViewById(2131296489) == null)
          break label320;
        bool2 = true;
        localConfig.setElement(i, bool2);
        i = 512;
        if (localView.findViewById(2131296537) == null)
          break label326;
        bool2 = true;
        label141: localConfig.setElement(i, bool2);
        i = 64;
        if (localView.findViewById(2131296442) == null)
          break label332;
        bool2 = true;
        label166: localConfig.setElement(i, bool2);
        i = 128;
        if (localView.findViewById(2131296400) == null)
          break label338;
        bool2 = true;
        label192: localConfig.setElement(i, bool2);
        i = 256;
        if (localView.findViewById(2131296401) == null)
          break label344;
        bool2 = true;
        label218: localConfig.setElement(i, bool2);
        i = 1024;
        if (localView.findViewById(2131296576) == null)
          break label350;
        bool2 = true;
        localConfig.setElement(i, bool2);
        i = 2048;
        if (localView.findViewById(2131296490) == null)
          break label356;
        bool2 = true;
        label270: localConfig.setElement(i, bool2);
        j = 4096;
        if (localView.findViewById(2131296509) == null)
          break label362;
      }
      while (true)
      {
        localConfig.setElement(j, bool1);
        return localConfig;
        j = 0;
        break;
        label308: j = 0;
        break label67;
        label314: j = 0;
        break label91;
        label320: j = 0;
        break label115;
        label326: j = 0;
        break label141;
        label332: j = 0;
        break label166;
        label338: j = 0;
        break label192;
        label344: j = 0;
        break label218;
        label350: j = 0;
        break label244;
        label356: j = 0;
        break label270;
        label362: bool1 = false;
      }
    }
    catch (Exception localException)
    {
      while (true)
        int k = Log.e("NowPlayingUtils", "Failed to inflate layout to extract config info", localException);
    }
  }

  public static RemoteViews createViews(Context paramContext, Intent paramIntent, Config paramConfig)
  {
    String str1 = paramContext.getPackageName();
    int i = paramConfig.getLayoutId();
    RemoteViews localRemoteViews = new RemoteViews(str1, i);
    Intent localIntent1 = new Intent("com.android.music.musicservicecommand.previous").setClass(paramContext, MusicPlaybackService.class);
    PendingIntent localPendingIntent1 = PendingIntent.getService(paramContext, 0, localIntent1, 0);
    localRemoteViews.setOnClickPendingIntent(2131296488, localPendingIntent1);
    Intent localIntent2 = new Intent("com.android.music.musicservicecommand.next").setClass(paramContext, MusicPlaybackService.class);
    PendingIntent localPendingIntent2 = PendingIntent.getService(paramContext, 0, localIntent2, 0);
    localRemoteViews.setOnClickPendingIntent(2131296489, localPendingIntent2);
    Intent localIntent3 = new Intent("com.android.music.musicservicecommand.veto").setClass(paramContext, MusicPlaybackService.class);
    PendingIntent localPendingIntent3 = PendingIntent.getService(paramContext, 0, localIntent3, 0);
    localRemoteViews.setOnClickPendingIntent(2131296538, localPendingIntent3);
    int k;
    PendingIntent localPendingIntent7;
    label281: String str2;
    String str3;
    String str4;
    int n;
    label360: String str7;
    String str8;
    label481: int i1;
    label570: label622: int i2;
    label642: int i3;
    label671: int m;
    int i4;
    Object localObject;
    int i5;
    if (paramConfig.hasPreviousButton())
    {
      int j = 2131296488;
      if (paramConfig.isPreviousVisible())
      {
        k = 0;
        localRemoteViews.setViewVisibility(j, k);
      }
    }
    else
    {
      Intent localIntent4 = new Intent("com.android.music.musicservicecommand.togglepause").setClass(paramContext, MusicPlaybackService.class);
      Intent localIntent5 = localIntent4.putExtra("removeNotification", true);
      Uri localUri = Uri.fromParts("googlemusic", "togglePause", "removeNotification");
      Intent localIntent6 = localIntent4.setData(localUri);
      PendingIntent localPendingIntent4 = PendingIntent.getService(paramContext, 0, localIntent4, 0);
      localRemoteViews.setOnClickPendingIntent(2131296516, localPendingIntent4);
      PendingIntent localPendingIntent5 = PendingIntent.getService(paramContext, 0, localIntent4, 0);
      localRemoteViews.setOnClickPendingIntent(2131296487, localPendingIntent5);
      PendingIntent localPendingIntent6 = PendingIntent.getService(paramContext, 0, localIntent4, 0);
      localRemoteViews.setOnClickPendingIntent(2131296575, localPendingIntent6);
      if (!paramIntent.getBooleanExtra("playing", false))
        break label779;
      localPendingIntent7 = AppNavigation.getIntentToOpenAppWithPlaybackScreen(paramContext);
      str2 = paramIntent.getStringExtra("error");
      str3 = paramIntent.getStringExtra("track");
      str4 = paramIntent.getStringExtra("artist");
      String str5 = paramIntent.getStringExtra("album");
      long l = paramIntent.getLongExtra("albumId", 65535L);
      boolean bool = paramIntent.getBooleanExtra("supportsRating", true);
      if ((!MusicUtils.isUnknown(str3)) || (!MusicUtils.isUnknown(str4)) || (!MusicUtils.isUnknown(str5)))
        break label788;
      n = 1;
      if (str2 == null)
        break label794;
      localRemoteViews.setViewVisibility(2131296580, 0);
      localRemoteViews.setViewVisibility(2131296400, 8);
      if (paramConfig.hasTrackName())
      {
        localRemoteViews.setViewVisibility(2131296442, 8);
        localRemoteViews.setViewVisibility(2131296579, 8);
      }
      if (paramConfig.hasArtistName())
        localRemoteViews.setViewVisibility(2131296400, 8);
      if (paramConfig.hasAlbumName())
        localRemoteViews.setViewVisibility(2131296401, 8);
      if (paramConfig.hasAlbumArtist())
        localRemoteViews.setViewVisibility(2131296537, 8);
      localRemoteViews.setTextViewText(2131296580, str2);
      localRemoteViews.setOnClickPendingIntent(2131296580, localPendingIntent7);
      str7 = str5;
      str8 = str4;
      if (paramConfig.hasRatings())
      {
        if (!paramConfig.isRatingsVisible())
          break label1247;
        if (!bool)
          break label1218;
        i1 = RatingSelector.convertRatingToThumbs(paramIntent.getIntExtra("rating", 0));
        if (i1 != 5)
          break label1102;
        localRemoteViews.setViewVisibility(2131296576, 8);
        localRemoteViews.setViewVisibility(2131296577, 0);
        localRemoteViews.setViewVisibility(2131296578, 8);
        Intent localIntent7 = getRatingIntent(paramContext, 1);
        PendingIntent localPendingIntent8 = PendingIntent.getService(paramContext, 0, localIntent7, 0);
        localRemoteViews.setOnClickPendingIntent(2131296577, localPendingIntent8);
      }
      if (paramConfig.hasPlayPauseButton())
      {
        if (!paramIntent.getBooleanExtra("playing", false))
          break label1277;
        localRemoteViews.setViewVisibility(2131296516, 8);
        localRemoteViews.setViewVisibility(2131296487, 0);
        localRemoteViews.setViewVisibility(2131296575, 8);
        localRemoteViews.setViewVisibility(2131296354, 8);
      }
      if (paramConfig.hasNextButton())
      {
        str5 = null;
        if (!paramConfig.isNextVisible())
          break label1362;
        i2 = 0;
        localRemoteViews.setViewVisibility(str5, i2);
      }
      if (paramConfig.hasPreviousButton())
      {
        str5 = null;
        if (!paramConfig.isPreviousVisible())
          break label1369;
        i3 = 0;
        localRemoteViews.setViewVisibility(str5, i3);
      }
      if ((paramConfig.hasAlbumArt()) && (paramConfig.isAlbumArtVisible()))
      {
        m = paramConfig.getAlbumArtWidth();
        n = paramConfig.getAlbumArtHeight();
        str2 = null;
        i4 = -1;
        if (!paramIntent.getBooleanExtra("albumArtFromService", false))
          break label1376;
        localObject = AlbumArtUtils.getArtwork(paramContext, l, m, n, true, str7, str8, null, true);
        i5 = i4;
        label746: if (localObject == null)
          break label1489;
        localRemoteViews.setImageViewBitmap(2131296536, (Bitmap)localObject);
        localRemoteViews.setOnClickPendingIntent(2131296536, localPendingIntent7);
      }
    }
    while (true)
    {
      return localRemoteViews;
      k = 8;
      break;
      label779: localPendingIntent7 = AppNavigation.getIntentToOpenApp(paramContext);
      break label281;
      label788: n = 0;
      break label360;
      label794: localRemoteViews.setViewVisibility(2131296580, 8);
      if (MusicUtils.isUnknown(str4))
        str4 = paramContext.getString(2131230890);
      String str6;
      if (MusicUtils.isUnknown(m))
        str6 = paramContext.getString(2131230891);
      if (paramConfig.hasTrackName())
      {
        if (n != 0)
        {
          localRemoteViews.setViewVisibility(2131296442, 8);
          localRemoteViews.setViewVisibility(2131296579, 8);
        }
      }
      else
      {
        label867: if (paramConfig.hasArtistName())
        {
          if (n == 0)
            break label978;
          localRemoteViews.setViewVisibility(2131296400, 8);
        }
        label888: if (paramConfig.hasAlbumName())
        {
          if (n == 0)
            break label1007;
          localRemoteViews.setViewVisibility(2131296401, 8);
        }
      }
      while (true)
        if (paramConfig.hasAlbumArtist())
        {
          if (n != 0)
          {
            localRemoteViews.setViewVisibility(2131296537, 8);
            str7 = str6;
            str8 = str4;
            break;
            localRemoteViews.setViewVisibility(2131296579, 0);
            localRemoteViews.setViewVisibility(2131296442, 0);
            localRemoteViews.setTextViewText(2131296442, str3);
            localRemoteViews.setOnClickPendingIntent(2131296442, localPendingIntent7);
            break label867;
            label978: localRemoteViews.setViewVisibility(2131296400, 0);
            localRemoteViews.setTextViewText(2131296400, str4);
            localRemoteViews.setOnClickPendingIntent(2131296400, localPendingIntent7);
            break label888;
            label1007: localRemoteViews.setViewVisibility(2131296401, 0);
            localRemoteViews.setTextViewText(2131296401, str6);
            localRemoteViews.setOnClickPendingIntent(2131296401, localPendingIntent7);
            continue;
          }
          localRemoteViews.setViewVisibility(2131296537, 0);
          Object[] arrayOfObject = new Object[2];
          arrayOfObject[0] = str4;
          arrayOfObject[1] = str6;
          String str9 = paramContext.getString(2131231430, arrayOfObject);
          localRemoteViews.setTextViewText(2131296537, str9);
          localRemoteViews.setOnClickPendingIntent(2131296537, localPendingIntent7);
        }
      str7 = str6;
      str8 = str4;
      break label481;
      label1102: if (i1 == 1)
      {
        localRemoteViews.setViewVisibility(2131296576, 8);
        localRemoteViews.setViewVisibility(2131296577, 8);
        localRemoteViews.setViewVisibility(2131296578, 0);
        Intent localIntent8 = getRatingIntent(paramContext, 0);
        PendingIntent localPendingIntent9 = PendingIntent.getService(paramContext, 0, localIntent8, 0);
        localRemoteViews.setOnClickPendingIntent(2131296578, localPendingIntent9);
        break label570;
      }
      localRemoteViews.setViewVisibility(2131296576, 0);
      localRemoteViews.setViewVisibility(2131296577, 8);
      localRemoteViews.setViewVisibility(2131296578, 8);
      Intent localIntent9 = getRatingIntent(paramContext, 5);
      PendingIntent localPendingIntent10 = PendingIntent.getService(paramContext, 0, localIntent9, 0);
      localRemoteViews.setOnClickPendingIntent(2131296576, localPendingIntent10);
      break label570;
      label1218: localRemoteViews.setViewVisibility(2131296576, 4);
      localRemoteViews.setViewVisibility(2131296577, 8);
      localRemoteViews.setViewVisibility(2131296578, 8);
      break label570;
      label1247: localRemoteViews.setViewVisibility(2131296576, 8);
      localRemoteViews.setViewVisibility(2131296577, 8);
      localRemoteViews.setViewVisibility(2131296578, 8);
      break label570;
      label1277: if ((n != 0) && (str2 == null))
      {
        localRemoteViews.setViewVisibility(2131296516, 0);
        localRemoteViews.setViewVisibility(2131296487, 8);
        localRemoteViews.setViewVisibility(2131296575, 8);
        localRemoteViews.setViewVisibility(2131296354, 0);
        break label622;
      }
      localRemoteViews.setViewVisibility(2131296516, 0);
      localRemoteViews.setViewVisibility(2131296487, 8);
      localRemoteViews.setViewVisibility(2131296575, 8);
      localRemoteViews.setViewVisibility(2131296354, 8);
      break label622;
      label1362: i2 = 8;
      break label642;
      label1369: i3 = 8;
      break label671;
      label1376: if (paramIntent.hasExtra("externalAlbumArtUrl"))
      {
        String str10 = paramIntent.getStringExtra("externalAlbumArtUrl");
        localObject = AlbumArtUtils.getExternalAlbumArtBitmap(paramContext, str10, str6, n, true, true, true);
        i5 = i4;
        break label746;
      }
      if (paramIntent.hasExtra("albumArtResourceId"))
      {
        i5 = paramIntent.getIntExtra("albumArtResourceId", -1);
        localObject = str2;
        break label746;
      }
      if (localObject >= 0L)
      {
        localObject = AlbumArtUtils.getArtwork(paramContext, ()localObject, str6, n, true, null, null, null, true);
        i5 = i4;
        break label746;
      }
      i5 = -1;
      localObject = str2;
      break label746;
      label1489: if (i5 > 0)
      {
        localRemoteViews.setImageViewResource(2131296536, i5);
        localRemoteViews.setOnClickPendingIntent(2131296536, localPendingIntent7);
      }
      else
      {
        int i6 = Log.w("NowPlayingUtils", "Failed to set album art for the remote views");
      }
    }
  }

  public static Intent getRatingIntent(Context paramContext, int paramInt)
  {
    Intent localIntent1 = new Intent("com.android.music.musicservicecommand.rating").setClass(paramContext, MusicPlaybackService.class);
    Intent localIntent2 = localIntent1.putExtra("rating", paramInt);
    StringBuilder localStringBuilder = new StringBuilder().append("rating://");
    String str = Integer.toString(paramInt);
    Uri localUri = Uri.parse(str);
    Intent localIntent3 = localIntent1.setData(localUri);
    return localIntent1;
  }

  public static class Config
  {
    private int mAlbumArtHeight = 60;
    private int mAlbumArtWidth = 60;
    private int mElements = 0;
    private int mLayoutId;
    private int mVisibleElements = 2147483647;

    public void addElements(int paramInt)
    {
      int i = this.mElements | paramInt;
      this.mElements = i;
    }

    public int getAlbumArtHeight()
    {
      return this.mAlbumArtHeight;
    }

    public int getAlbumArtWidth()
    {
      return this.mAlbumArtWidth;
    }

    public int getLayoutId()
    {
      return this.mLayoutId;
    }

    public boolean hasAlbumArt()
    {
      return hasElement(32);
    }

    public boolean hasAlbumArtist()
    {
      return hasElement(512);
    }

    public boolean hasAlbumName()
    {
      return hasElement(256);
    }

    public boolean hasArtistName()
    {
      return hasElement(128);
    }

    public boolean hasElement(int paramInt)
    {
      if ((this.mElements & paramInt) != 0);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean hasNextButton()
    {
      return hasElement(2);
    }

    public boolean hasPlayPauseButton()
    {
      return hasElement(1);
    }

    public boolean hasPreviousButton()
    {
      return hasElement(4);
    }

    public boolean hasRatings()
    {
      return hasElement(1024);
    }

    public boolean hasTrackName()
    {
      return hasElement(64);
    }

    public void hideElements(int paramInt)
    {
      int i = this.mVisibleElements;
      int j = paramInt ^ 0xFFFFFFFF;
      int k = i & j;
      this.mVisibleElements = k;
    }

    public boolean isAlbumArtVisible()
    {
      return isElementVisible(32);
    }

    public boolean isElementVisible(int paramInt)
    {
      if ((this.mVisibleElements & paramInt) != 0);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean isNextVisible()
    {
      return isElementVisible(2);
    }

    public boolean isPreviousVisible()
    {
      return isElementVisible(4);
    }

    public boolean isRatingsVisible()
    {
      return isElementVisible(1024);
    }

    public void removeElements(int paramInt)
    {
      int i = this.mElements;
      int j = paramInt ^ 0xFFFFFFFF;
      int k = i & j;
      this.mElements = k;
    }

    public void setAlbumArtHeight(int paramInt)
    {
      this.mAlbumArtHeight = paramInt;
    }

    public void setAlbumArtWidth(int paramInt)
    {
      this.mAlbumArtWidth = paramInt;
    }

    public void setElement(int paramInt, boolean paramBoolean)
    {
      if (paramBoolean)
      {
        addElements(paramInt);
        return;
      }
      removeElements(paramInt);
    }

    public void setLayoutId(int paramInt)
    {
      this.mLayoutId = paramInt;
    }

    public void showElements(int paramInt)
    {
      int i = this.mVisibleElements | paramInt;
      this.mVisibleElements = i;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.NowPlayingRemoteViewUtils
 * JD-Core Version:    0.6.2
 */