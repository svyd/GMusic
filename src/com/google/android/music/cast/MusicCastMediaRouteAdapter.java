package com.google.android.music.cast;

import android.content.Context;
import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.music.log.Log;
import com.google.android.music.playback.IMusicCastMediaRouterCallback;
import com.google.android.music.playback.IMusicCastMediaRouterCallback.Stub;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.UIStateManager;
import com.google.android.music.utils.MusicUtils;
import com.google.cast.CastDevice;
import com.google.cast.MediaRouteAdapter;
import com.google.cast.MediaRouteStateChangeListener;

public class MusicCastMediaRouteAdapter
  implements MediaRouteAdapter
{
  private final Context mContext;
  private MediaRouteStateChangeListener mMediaRouteStateChangeListener;
  private IMusicCastMediaRouterCallback mMusicCastMediaRouterCallback;
  private final Runnable mRegisterCallbackRunnable;
  private String mRouteId;

  public MusicCastMediaRouteAdapter(Context paramContext)
  {
    IMusicCastMediaRouterCallback.Stub local1 = new IMusicCastMediaRouterCallback.Stub()
    {
      public void onRouteVolumeChanged(String paramAnonymousString, final double paramAnonymousDouble)
        throws RemoteException
      {
        if (!MusicCastMediaRouteAdapter.this.mRouteId.equals(paramAnonymousString))
        {
          String str = "Ignoring onRouteVolumeChanged for routeId=" + paramAnonymousString;
          Log.w("MCMediaRouteAdapter", str);
          return;
        }
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            if (MusicCastMediaRouteAdapter.this.mMediaRouteStateChangeListener == null)
              return;
            MediaRouteStateChangeListener localMediaRouteStateChangeListener = MusicCastMediaRouteAdapter.this.mMediaRouteStateChangeListener;
            double d = paramAnonymousDouble;
            localMediaRouteStateChangeListener.onVolumeChanged(d);
          }
        };
        Context localContext = MusicCastMediaRouteAdapter.this.mContext;
        MusicUtils.runOnUiThread(local1, localContext);
      }
    };
    this.mMusicCastMediaRouterCallback = local1;
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        MusicCastMediaRouteAdapter.this.registerCallback();
      }
    };
    this.mRegisterCallbackRunnable = local2;
    this.mContext = paramContext;
  }

  private boolean isLogVerbose()
  {
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this.mContext, this);
    try
    {
      boolean bool1 = localMusicPreferences.isLogFilesEnabled();
      boolean bool2 = bool1;
      return bool2;
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(this);
    }
  }

  private void registerCallback()
  {
    if (MusicUtils.sService != null)
      try
      {
        IMusicPlaybackService localIMusicPlaybackService = MusicUtils.sService;
        IMusicCastMediaRouterCallback localIMusicCastMediaRouterCallback = this.mMusicCastMediaRouterCallback;
        localIMusicPlaybackService.registerMusicCastMediaRouterCallback(localIMusicCastMediaRouterCallback);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        Log.w("MCMediaRouteAdapter", "Remote call registerMusicCastMediaRouterCallback failed", localRemoteException);
        return;
      }
    Log.w("MCMediaRouteAdapter", "Music service is null");
  }

  public void onDeviceAvailable(CastDevice paramCastDevice, String paramString, MediaRouteStateChangeListener paramMediaRouteStateChangeListener)
  {
    this.mRouteId = paramString;
    this.mMediaRouteStateChangeListener = paramMediaRouteStateChangeListener;
    if (isLogVerbose())
      Log.v("MCMediaRouteAdapter", "onDeviceAvailable");
    UIStateManager localUIStateManager = UIStateManager.getInstance(this.mContext);
    Runnable localRunnable = this.mRegisterCallbackRunnable;
    localUIStateManager.addRunOnPlaybackServiceConnected(localRunnable);
  }

  public void onSetVolume(double paramDouble)
  {
    if (isLogVerbose())
    {
      String str = "onSetVolume: volume=" + paramDouble;
      Log.v("MCMediaRouteAdapter", str);
    }
    if (this.mMediaRouteStateChangeListener != null)
      this.mMediaRouteStateChangeListener.onVolumeChanged(paramDouble);
    if (!TextUtils.isEmpty(this.mRouteId))
    {
      MusicUtils.setMediaRouteVolume(this.mRouteId, paramDouble);
      return;
    }
    Log.w("MCMediaRouteAdapter", "Empty route id");
  }

  public void onUpdateVolume(double paramDouble)
  {
    if (isLogVerbose())
    {
      String str = "onUpdateVolume: delta=" + paramDouble;
      Log.d("MCMediaRouteAdapter", str);
    }
    if (!TextUtils.isEmpty(this.mRouteId))
    {
      MusicUtils.updateMediaRouteVolume(this.mRouteId, paramDouble);
      return;
    }
    Log.w("MCMediaRouteAdapter", "Empty route id");
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cast.MusicCastMediaRouteAdapter
 * JD-Core Version:    0.6.2
 */