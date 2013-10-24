package com.google.android.music.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouteSelector.Builder;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.Callback;
import android.support.v7.media.MediaRouter.ProviderInfo;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Toast;
import com.google.android.gsf.Gservices;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.RouteChecker;
import com.google.cast.MediaRouteHelper;
import java.util.Iterator;
import java.util.List;

public class MediaRouteManager
{
  private static final boolean LOGV = Log.isLoggable("MusicCast", 2);
  private final Context mContext;
  private Handler mMediaRestoreHandler;
  private Runnable mMediaRestoreRunnable;
  private MediaRouteButton mMediaRouteButton;
  private boolean mMediaRouteButtonVisible;
  private String mMediaRouteIdToRestore;
  private BroadcastReceiver mMediaRouteInvalidatedReceiver;
  private MediaRouteSelector mMediaRouteSelector;
  private MediaRouter.RouteInfo mMediaRouteToRestore;
  private MediaRouter mMediaRouter;
  private MediaRouterCallback mMediaRouterCallback;
  private final RouteChecker mRouteChecker;

  public MediaRouteManager(Context paramContext)
  {
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        UIStateManager localUIStateManager = UIStateManager.getInstance(paramAnonymousContext);
        Runnable localRunnable = MediaRouteManager.this.mMediaRestoreRunnable;
        localUIStateManager.addRunOnPlaybackServiceConnected(localRunnable);
      }
    };
    this.mMediaRouteInvalidatedReceiver = local1;
    this.mContext = paramContext;
    Context localContext = this.mContext;
    RouteChecker localRouteChecker = new RouteChecker(localContext);
    this.mRouteChecker = localRouteChecker;
  }

  private String getCastAppName()
  {
    return Gservices.getString(this.mContext.getContentResolver(), "music_cast_app_name", "GoogleMusic");
  }

  private void refreshVisibility()
  {
    if (this.mMediaRouteButton == null)
      return;
    int i;
    if ((this.mMediaRouteButtonVisible) && (this.mMediaRouter != null))
    {
      i = 1;
      if (this.mMediaRouter != null)
      {
        if (i == 0)
          break label78;
        MediaRouter localMediaRouter = this.mMediaRouter;
        MediaRouteSelector localMediaRouteSelector = this.mMediaRouteSelector;
        if (!localMediaRouter.isRouteAvailable(localMediaRouteSelector, 1))
          break label78;
        i = 1;
      }
      label56: if (i == 0)
        break label83;
    }
    label78: label83: for (int j = 0; ; j = 8)
    {
      this.mMediaRouteButton.setVisibility(j);
      return;
      i = 0;
      break;
      i = 0;
      break label56;
    }
  }

  private void resetMediaRouteRestoreState()
  {
    this.mMediaRouteIdToRestore = null;
    this.mMediaRouteToRestore = null;
    if (this.mMediaRestoreHandler == null)
      return;
    this.mMediaRestoreHandler.removeCallbacksAndMessages(null);
    this.mMediaRestoreHandler = null;
  }

  private void restoreMediaRoute()
  {
    resetMediaRouteRestoreState();
    MediaRouter.RouteInfo localRouteInfo1 = this.mMediaRouter.getSelectedRoute();
    if (MusicUtils.sService != null);
    while (true)
    {
      try
      {
        String str1 = MusicUtils.sService.getSelectedMediaRouteId();
        this.mMediaRouteIdToRestore = str1;
        if (LOGV)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("Attempting to restore route: ");
          String str2 = this.mMediaRouteIdToRestore;
          String str3 = str2;
          int i = Log.d("MediaRouteManager", str3);
        }
        if ((TextUtils.isEmpty(this.mMediaRouteIdToRestore)) && (localRouteInfo1.getPlaybackType() != 0))
        {
          MediaRouter.RouteInfo localRouteInfo2 = this.mMediaRouter.getDefaultRoute();
          this.mMediaRouteToRestore = localRouteInfo2;
          if ((this.mMediaRouteToRestore != null) || (this.mMediaRouteIdToRestore != null))
          {
            Handler localHandler1 = new Handler();
            this.mMediaRestoreHandler = localHandler1;
            Handler localHandler2 = this.mMediaRestoreHandler;
            Runnable local3 = new Runnable()
            {
              public void run()
              {
                if ((MediaRouteManager.this.mMediaRouteToRestore == null) && (MediaRouteManager.this.mMediaRouteIdToRestore == null))
                  return;
                int i = Log.w("MediaRouteManager", "Timeout reached when restoring selected route");
                MediaRouteManager.this.resetMediaRouteRestoreState();
                MediaRouter.RouteInfo localRouteInfo1 = MediaRouteManager.this.mMediaRouter.getSelectedRoute();
                if (localRouteInfo1 != null)
                {
                  MediaRouteManager.this.setMediaRoute(localRouteInfo1);
                  return;
                }
                MediaRouter localMediaRouter = MediaRouteManager.this.mMediaRouter;
                MediaRouter.RouteInfo localRouteInfo2 = MediaRouteManager.this.mMediaRouter.getDefaultRoute();
                localMediaRouter.selectRoute(localRouteInfo2);
              }
            };
            boolean bool = localHandler2.postDelayed(local3, 5000L);
          }
          if (LOGV)
            int j = Log.d("MediaRouteManager", "Registering media route callback");
          MediaRouter localMediaRouter1 = this.mMediaRouter;
          MediaRouteSelector localMediaRouteSelector = this.mMediaRouteSelector;
          MediaRouterCallback localMediaRouterCallback = this.mMediaRouterCallback;
          localMediaRouter1.addCallback(localMediaRouteSelector, localMediaRouterCallback, 4);
          if (this.mMediaRouteToRestore == null)
            return;
          MediaRouter localMediaRouter2 = this.mMediaRouter;
          MediaRouter.RouteInfo localRouteInfo3 = this.mMediaRouteToRestore;
          localMediaRouter2.selectRoute(localRouteInfo3);
          return;
        }
      }
      catch (RemoteException localRemoteException)
      {
        int k = Log.e("MediaRouteManager", "Faied to communicate with playback service", localRemoteException);
        continue;
        if ((localRouteInfo1 != null) && (this.mMediaRouteIdToRestore != null))
        {
          String str4 = this.mMediaRouteIdToRestore;
          String str5 = localRouteInfo1.getId();
          if (str4.equals(str5))
          {
            resetMediaRouteRestoreState();
            continue;
          }
        }
        Iterator localIterator = this.mMediaRouter.getRoutes().iterator();
        if (localIterator.hasNext())
        {
          MediaRouter.RouteInfo localRouteInfo4 = (MediaRouter.RouteInfo)localIterator.next();
          if (this.mMediaRouteIdToRestore == null)
            continue;
          String str6 = this.mMediaRouteIdToRestore;
          String str7 = localRouteInfo4.getId();
          if (!str6.equals(str7))
            continue;
          int m = Log.i("MediaRouteManager", "Found selected route. Restoring.");
          this.mMediaRouteToRestore = localRouteInfo4;
          this.mMediaRouteIdToRestore = null;
        }
        if ((this.mMediaRouteToRestore != null) || (!LOGV))
          continue;
        int n = Log.d("MediaRouteManager", "Selected route is not found yet");
        continue;
      }
      int i1 = Log.w("MediaRouteManager", "onStart -- service not yet bound.");
    }
  }

  private void setMediaRoute(MediaRouter.RouteInfo paramRouteInfo)
  {
    try
    {
      if (MusicUtils.sService == null)
        break label88;
      switch (paramRouteInfo.getPlaybackType())
      {
      default:
        return;
      case 0:
        IMusicPlaybackService localIMusicPlaybackService1 = MusicUtils.sService;
        String str1 = paramRouteInfo.getId();
        localIMusicPlaybackService1.setMediaRoute(true, str1);
        return;
      case 1:
      }
    }
    catch (RemoteException localRemoteException)
    {
      int i = Log.w("MediaRouteManager", "Unable to switch route.", localRemoteException);
      return;
    }
    IMusicPlaybackService localIMusicPlaybackService2 = MusicUtils.sService;
    String str2 = paramRouteInfo.getId();
    localIMusicPlaybackService2.setMediaRoute(false, str2);
    return;
    label88: int j = Log.w("MediaRouteManager", "Unable to switch routes -- service not yet bound.");
  }

  public void bindMediaRouteButton(MediaRouteButton paramMediaRouteButton)
  {
    this.mMediaRouteButton = paramMediaRouteButton;
    MediaRouteSelector localMediaRouteSelector = this.mMediaRouteSelector;
    paramMediaRouteButton.setRouteSelector(localMediaRouteSelector);
    refreshVisibility();
  }

  public void onCreate()
  {
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    MediaRouter localMediaRouter = MediaRouter.getInstance(this.mContext);
    this.mMediaRouter = localMediaRouter;
    MediaRouteSelector.Builder localBuilder = new MediaRouteSelector.Builder().addControlCategory("android.media.intent.category.LIVE_AUDIO").addControlCategory("android.media.intent.category.REMOTE_PLAYBACK").addControlCategory("com.google.cast.CATEGORY_CAST");
    StringBuilder localStringBuilder = new StringBuilder().append("com.google.cast.CATEGORY_CAST_APP_NAME:");
    String str1 = getCastAppName();
    String str2 = str1;
    MediaRouteSelector localMediaRouteSelector = localBuilder.addControlCategory(str2).build();
    this.mMediaRouteSelector = localMediaRouteSelector;
    MediaRouterCallback localMediaRouterCallback = new MediaRouterCallback(null);
    this.mMediaRouterCallback = localMediaRouterCallback;
  }

  public void onCreateOptionsMenu(Menu paramMenu)
  {
    if (this.mMediaRouter == null)
      return;
    MediaRouteActionProvider localMediaRouteActionProvider = (MediaRouteActionProvider)MenuItemCompat.getActionProvider(paramMenu.findItem(2131296452));
    MediaRouteSelector localMediaRouteSelector = this.mMediaRouteSelector;
    localMediaRouteActionProvider.setRouteSelector(localMediaRouteSelector);
  }

  public void onDestroy()
  {
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = true;
    switch (paramInt)
    {
    default:
      bool = false;
    case 24:
    case 25:
    }
    while (true)
    {
      return bool;
      MediaRouter.RouteInfo localRouteInfo;
      if (this.mMediaRouter != null)
      {
        localRouteInfo = this.mMediaRouter.getSelectedRoute();
        if (localRouteInfo.getPlaybackType() == 1)
          localRouteInfo.requestUpdateVolume(1);
      }
      else
      {
        if (this.mMediaRouter == null)
          break;
        localRouteInfo = this.mMediaRouter.getSelectedRoute();
        if (localRouteInfo.getPlaybackType() != 1)
          break;
        localRouteInfo.requestUpdateVolume(-1);
      }
    }
  }

  public void onPause()
  {
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  public void onRestart()
  {
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  public void onResume()
  {
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  public void onStart()
  {
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    if (this.mMediaRouter == null)
      return;
    if (this.mMediaRestoreRunnable == null)
    {
      Runnable local2 = new Runnable()
      {
        public void run()
        {
          MediaRouteManager.this.restoreMediaRoute();
        }
      };
      this.mMediaRestoreRunnable = local2;
    }
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.android.music.mediarouteinvalidated");
    Context localContext = this.mContext;
    BroadcastReceiver localBroadcastReceiver = this.mMediaRouteInvalidatedReceiver;
    Intent localIntent = localContext.registerReceiver(localBroadcastReceiver, localIntentFilter);
    UIStateManager localUIStateManager = UIStateManager.getInstance(this.mContext);
    Runnable localRunnable = this.mMediaRestoreRunnable;
    localUIStateManager.addRunOnPlaybackServiceConnected(localRunnable);
  }

  public void onStop()
  {
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    if (this.mMediaRouter != null)
    {
      MediaRouter localMediaRouter = this.mMediaRouter;
      MediaRouterCallback localMediaRouterCallback = this.mMediaRouterCallback;
      localMediaRouter.removeCallback(localMediaRouterCallback);
      if (LOGV)
        int i = Log.d("MediaRouteManager", "Unregistered media route callback");
      Context localContext = this.mContext;
      BroadcastReceiver localBroadcastReceiver = this.mMediaRouteInvalidatedReceiver;
      localContext.unregisterReceiver(localBroadcastReceiver);
    }
    if (this.mMediaRestoreRunnable == null)
      return;
    UIStateManager localUIStateManager = UIStateManager.getInstance(this.mContext);
    Runnable localRunnable = this.mMediaRestoreRunnable;
    localUIStateManager.removeRunOnPlaybackServiceConnected(localRunnable);
    this.mMediaRestoreRunnable = null;
  }

  public void setMediaRouteButtonVisibility(boolean paramBoolean)
  {
    this.mMediaRouteButtonVisible = paramBoolean;
    refreshVisibility();
  }

  private class MediaRouterCallback extends MediaRouter.Callback
  {
    private MediaRouterCallback()
    {
    }

    public void onProviderAdded(MediaRouter paramMediaRouter, MediaRouter.ProviderInfo paramProviderInfo)
    {
      MediaRouteManager.this.refreshVisibility();
    }

    public void onProviderChanged(MediaRouter paramMediaRouter, MediaRouter.ProviderInfo paramProviderInfo)
    {
      MediaRouteManager.this.refreshVisibility();
    }

    public void onProviderRemoved(MediaRouter paramMediaRouter, MediaRouter.ProviderInfo paramProviderInfo)
    {
      MediaRouteManager.this.refreshVisibility();
    }

    public void onRouteAdded(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      if (MediaRouteManager.LOGV)
      {
        String str1 = "Route added " + paramRouteInfo;
        int i = Log.v("MediaRouteManager", str1);
      }
      MediaRouteManager.this.refreshVisibility();
      if (MediaRouteManager.this.mMediaRouteIdToRestore == null)
        return;
      if (MusicUtils.sService != null)
        try
        {
          MediaRouteManager localMediaRouteManager = MediaRouteManager.this;
          String str2 = MusicUtils.sService.getSelectedMediaRouteId();
          String str3 = MediaRouteManager.access$202(localMediaRouteManager, str2);
          if (MediaRouteManager.this.mMediaRouteIdToRestore == null)
            return;
          String str4 = MediaRouteManager.this.mMediaRouteIdToRestore;
          String str5 = paramRouteInfo.getId();
          if (!str4.equals(str5))
            return;
          MediaRouter.RouteInfo localRouteInfo1 = MediaRouteManager.access$302(MediaRouteManager.this, paramRouteInfo);
          String str6 = MediaRouteManager.access$202(MediaRouteManager.this, null);
          if (MediaRouteManager.LOGV)
            int j = Log.v("MediaRouteManager", "Route being restored is added");
          MediaRouter localMediaRouter = MediaRouteManager.this.mMediaRouter;
          MediaRouter.RouteInfo localRouteInfo2 = MediaRouteManager.this.mMediaRouteToRestore;
          localMediaRouter.selectRoute(localRouteInfo2);
          return;
        }
        catch (RemoteException localRemoteException)
        {
          while (true)
            int k = Log.w("MediaRouteManager", "Unable to get currently selected route.");
        }
      int m = Log.w("MediaRouteManager", "onRouteAdded -- service not yet bound.");
    }

    public void onRouteChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteManager.this.refreshVisibility();
    }

    public void onRouteRemoved(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteManager.this.refreshVisibility();
    }

    public void onRouteSelected(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      String str1 = "Route selected " + paramRouteInfo;
      int i = Log.i("MediaRouteManager", str1);
      MediaRouteManager.this.refreshVisibility();
      String str2 = paramRouteInfo.getId();
      MediaRouteSelector.Builder localBuilder = new MediaRouteSelector.Builder().addControlCategory("com.google.cast.CATEGORY_CAST");
      StringBuilder localStringBuilder1 = new StringBuilder().append("com.google.cast.CATEGORY_CAST_APP_NAME:");
      String str3 = MediaRouteManager.this.getCastAppName();
      String str4 = str3;
      MediaRouteSelector localMediaRouteSelector = localBuilder.addControlCategory(str4).build();
      if (paramRouteInfo.matchesSelector(localMediaRouteSelector))
        boolean bool = MediaRouteHelper.requestCastDeviceForRoute(paramRouteInfo);
      if (MediaRouteManager.this.mMediaRouteIdToRestore != null)
      {
        String str5 = MediaRouteManager.this.mMediaRouteIdToRestore;
        if (str2.equals(str5))
        {
          StringBuilder localStringBuilder2 = new StringBuilder().append("Route ");
          String str6 = MediaRouteManager.this.mMediaRouteIdToRestore;
          String str7 = str6 + " is restored";
          int j = Log.i("MediaRouteManager", str7);
          MediaRouteManager.this.resetMediaRouteRestoreState();
        }
      }
      else if (MediaRouteManager.this.mMediaRouteToRestore != null)
      {
        String str8 = MediaRouteManager.this.mMediaRouteToRestore.getId();
        if (!str2.equals(str8))
          break label528;
        StringBuilder localStringBuilder3 = new StringBuilder().append("Route ");
        MediaRouter.RouteInfo localRouteInfo1 = MediaRouteManager.this.mMediaRouteToRestore;
        String str9 = localRouteInfo1 + " is restored";
        int k = Log.i("MediaRouteManager", str9);
        MediaRouteManager.this.resetMediaRouteRestoreState();
      }
      while (true)
      {
        if (!MediaRouteManager.this.mRouteChecker.isAcceptableRoute(paramRouteInfo))
        {
          String str10 = "Rejecting unacceptable route <" + paramRouteInfo + ">.";
          int m = Log.w("MediaRouteManager", str10);
          String str11 = MediaRouteManager.this.mContext.getString(2131231375);
          Object[] arrayOfObject = new Object[1];
          String str12 = paramRouteInfo.getName();
          arrayOfObject[0] = str12;
          String str13 = String.format(str11, arrayOfObject);
          Toast.makeText(MediaRouteManager.this.mContext, str13, 0).show();
          paramRouteInfo = MediaRouteManager.this.mMediaRouter.getDefaultRoute();
          MediaRouteManager.this.mMediaRouter.selectRoute(paramRouteInfo);
        }
        MediaRouteManager.this.setMediaRoute(paramRouteInfo);
        return;
        if (!paramRouteInfo.isDefault())
        {
          String str14 = "Non-default route selected " + paramRouteInfo;
          int n = Log.i("MediaRouteManager", str14);
          MediaRouteManager.this.resetMediaRouteRestoreState();
          break;
        }
        StringBuilder localStringBuilder4 = new StringBuilder().append("Ignoring selected route <").append(paramRouteInfo).append(">. Restoring ");
        String str15 = MediaRouteManager.this.mMediaRouteIdToRestore;
        String str16 = str15;
        int i1 = Log.w("MediaRouteManager", str16);
        return;
        label528: if (paramRouteInfo.isDefault())
          break label575;
        String str17 = "Non-default route selected " + paramRouteInfo;
        int i2 = Log.i("MediaRouteManager", str17);
        MediaRouteManager.this.resetMediaRouteRestoreState();
      }
      label575: StringBuilder localStringBuilder5 = new StringBuilder().append("Ignoring selected route <").append(paramRouteInfo).append(">. Restoring ");
      MediaRouter.RouteInfo localRouteInfo2 = MediaRouteManager.this.mMediaRouteToRestore;
      String str18 = localRouteInfo2;
      int i3 = Log.w("MediaRouteManager", str18);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.MediaRouteManager
 * JD-Core Version:    0.6.2
 */