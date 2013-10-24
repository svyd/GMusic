package android.support.v7.media;

import android.content.Context;
import android.os.Build.VERSION;
import java.lang.ref.WeakReference;

abstract class RemoteControlClientCompat
{
  protected final Context mContext;
  protected final Object mRcc;
  protected VolumeCallback mVolumeCallback;

  protected RemoteControlClientCompat(Context paramContext, Object paramObject)
  {
    this.mContext = paramContext;
    this.mRcc = paramObject;
  }

  public static RemoteControlClientCompat obtain(Context paramContext, Object paramObject)
  {
    if (Build.VERSION.SDK_INT >= 16);
    for (Object localObject = new JellybeanImpl(paramContext, paramObject); ; localObject = new LegacyImpl(paramContext, paramObject))
      return localObject;
  }

  public Object getRemoteControlClient()
  {
    return this.mRcc;
  }

  public void setPlaybackInfo(PlaybackInfo paramPlaybackInfo)
  {
  }

  public void setVolumeCallback(VolumeCallback paramVolumeCallback)
  {
    this.mVolumeCallback = paramVolumeCallback;
  }

  static class JellybeanImpl extends RemoteControlClientCompat
  {
    private boolean mRegistered;
    private final Object mRouterObj;
    private final Object mUserRouteCategoryObj;
    private final Object mUserRouteObj;

    public JellybeanImpl(Context paramContext, Object paramObject)
    {
      super(paramObject);
      Object localObject1 = MediaRouterJellybean.getMediaRouter(paramContext);
      this.mRouterObj = localObject1;
      Object localObject2 = MediaRouterJellybean.createRouteCategory(this.mRouterObj, "", false);
      this.mUserRouteCategoryObj = localObject2;
      Object localObject3 = this.mRouterObj;
      Object localObject4 = this.mUserRouteCategoryObj;
      Object localObject5 = MediaRouterJellybean.createUserRoute(localObject3, localObject4);
      this.mUserRouteObj = localObject5;
    }

    public void setPlaybackInfo(RemoteControlClientCompat.PlaybackInfo paramPlaybackInfo)
    {
      Object localObject1 = this.mUserRouteObj;
      int i = paramPlaybackInfo.volume;
      MediaRouterJellybean.UserRouteInfo.setVolume(localObject1, i);
      Object localObject2 = this.mUserRouteObj;
      int j = paramPlaybackInfo.volumeMax;
      MediaRouterJellybean.UserRouteInfo.setVolumeMax(localObject2, j);
      Object localObject3 = this.mUserRouteObj;
      int k = paramPlaybackInfo.volumeHandling;
      MediaRouterJellybean.UserRouteInfo.setVolumeHandling(localObject3, k);
      Object localObject4 = this.mUserRouteObj;
      int m = paramPlaybackInfo.playbackStream;
      MediaRouterJellybean.UserRouteInfo.setPlaybackStream(localObject4, m);
      Object localObject5 = this.mUserRouteObj;
      int n = paramPlaybackInfo.playbackType;
      MediaRouterJellybean.UserRouteInfo.setPlaybackType(localObject5, n);
      if (this.mRegistered)
        return;
      this.mRegistered = true;
      Object localObject6 = this.mUserRouteObj;
      Object localObject7 = MediaRouterJellybean.createVolumeCallback(new VolumeCallbackWrapper(this));
      MediaRouterJellybean.UserRouteInfo.setVolumeCallback(localObject6, localObject7);
      Object localObject8 = this.mUserRouteObj;
      Object localObject9 = this.mRcc;
      MediaRouterJellybean.UserRouteInfo.setRemoteControlClient(localObject8, localObject9);
    }

    private static final class VolumeCallbackWrapper
      implements MediaRouterJellybean.VolumeCallback
    {
      private final WeakReference<RemoteControlClientCompat.JellybeanImpl> mImplWeak;

      public VolumeCallbackWrapper(RemoteControlClientCompat.JellybeanImpl paramJellybeanImpl)
      {
        WeakReference localWeakReference = new WeakReference(paramJellybeanImpl);
        this.mImplWeak = localWeakReference;
      }

      public void onVolumeSetRequest(Object paramObject, int paramInt)
      {
        RemoteControlClientCompat.JellybeanImpl localJellybeanImpl = (RemoteControlClientCompat.JellybeanImpl)this.mImplWeak.get();
        if (localJellybeanImpl == null)
          return;
        if (localJellybeanImpl.mVolumeCallback == null)
          return;
        localJellybeanImpl.mVolumeCallback.onVolumeSetRequest(paramInt);
      }

      public void onVolumeUpdateRequest(Object paramObject, int paramInt)
      {
        RemoteControlClientCompat.JellybeanImpl localJellybeanImpl = (RemoteControlClientCompat.JellybeanImpl)this.mImplWeak.get();
        if (localJellybeanImpl == null)
          return;
        if (localJellybeanImpl.mVolumeCallback == null)
          return;
        localJellybeanImpl.mVolumeCallback.onVolumeUpdateRequest(paramInt);
      }
    }
  }

  static class LegacyImpl extends RemoteControlClientCompat
  {
    public LegacyImpl(Context paramContext, Object paramObject)
    {
      super(paramObject);
    }
  }

  public static abstract interface VolumeCallback
  {
    public abstract void onVolumeSetRequest(int paramInt);

    public abstract void onVolumeUpdateRequest(int paramInt);
  }

  public static final class PlaybackInfo
  {
    public int playbackStream = 3;
    public int playbackType = 1;
    public int volume;
    public int volumeHandling = 0;
    public int volumeMax;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.media.RemoteControlClientCompat
 * JD-Core Version:    0.6.2
 */