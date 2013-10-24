package android.support.v7.media;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

public abstract class MediaRouteProvider
{
  private Callback mCallback;
  private final Context mContext;
  private MediaRouteProviderDescriptor mDescriptor;
  private MediaRouteDiscoveryRequest mDiscoveryRequest;
  private final ProviderHandler mHandler;
  private final ProviderMetadata mMetadata;
  private boolean mPendingDescriptorChange;
  private boolean mPendingDiscoveryRequestChange;

  public MediaRouteProvider(Context paramContext)
  {
    this(paramContext, null);
  }

  MediaRouteProvider(Context paramContext, ProviderMetadata paramProviderMetadata)
  {
    ProviderHandler localProviderHandler = new ProviderHandler(null);
    this.mHandler = localProviderHandler;
    if (paramContext == null)
      throw new IllegalArgumentException("context must not be null");
    this.mContext = paramContext;
    if (paramProviderMetadata == null)
    {
      String str = paramContext.getPackageName();
      ProviderMetadata localProviderMetadata = new ProviderMetadata(str);
      this.mMetadata = localProviderMetadata;
      return;
    }
    this.mMetadata = paramProviderMetadata;
  }

  private void deliverDescriptorChanged()
  {
    this.mPendingDescriptorChange = false;
    if (this.mCallback == null)
      return;
    Callback localCallback = this.mCallback;
    MediaRouteProviderDescriptor localMediaRouteProviderDescriptor = this.mDescriptor;
    localCallback.onDescriptorChanged(this, localMediaRouteProviderDescriptor);
  }

  private void deliverDiscoveryRequestChanged()
  {
    this.mPendingDiscoveryRequestChange = false;
    MediaRouteDiscoveryRequest localMediaRouteDiscoveryRequest = this.mDiscoveryRequest;
    onDiscoveryRequestChanged(localMediaRouteDiscoveryRequest);
  }

  public final Context getContext()
  {
    return this.mContext;
  }

  public final MediaRouteProviderDescriptor getDescriptor()
  {
    return this.mDescriptor;
  }

  public final MediaRouteDiscoveryRequest getDiscoveryRequest()
  {
    return this.mDiscoveryRequest;
  }

  public final Handler getHandler()
  {
    return this.mHandler;
  }

  public final ProviderMetadata getMetadata()
  {
    return this.mMetadata;
  }

  public RouteController onCreateRouteController(String paramString)
  {
    return null;
  }

  public void onDiscoveryRequestChanged(MediaRouteDiscoveryRequest paramMediaRouteDiscoveryRequest)
  {
  }

  public final void setCallback(Callback paramCallback)
  {
    MediaRouter.checkCallingThread();
    this.mCallback = paramCallback;
  }

  public final void setDescriptor(MediaRouteProviderDescriptor paramMediaRouteProviderDescriptor)
  {
    MediaRouter.checkCallingThread();
    if (this.mDescriptor == paramMediaRouteProviderDescriptor)
      return;
    this.mDescriptor = paramMediaRouteProviderDescriptor;
    if (this.mPendingDescriptorChange)
      return;
    this.mPendingDescriptorChange = true;
    boolean bool = this.mHandler.sendEmptyMessage(1);
  }

  public final void setDiscoveryRequest(MediaRouteDiscoveryRequest paramMediaRouteDiscoveryRequest)
  {
    MediaRouter.checkCallingThread();
    if (this.mDiscoveryRequest == paramMediaRouteDiscoveryRequest)
      return;
    if ((this.mDiscoveryRequest != null) && (this.mDiscoveryRequest.equals(paramMediaRouteDiscoveryRequest)))
      return;
    this.mDiscoveryRequest = paramMediaRouteDiscoveryRequest;
    if (this.mPendingDiscoveryRequestChange)
      return;
    this.mPendingDiscoveryRequestChange = true;
    boolean bool = this.mHandler.sendEmptyMessage(2);
  }

  private final class ProviderHandler extends Handler
  {
    private ProviderHandler()
    {
    }

    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
        MediaRouteProvider.this.deliverDescriptorChanged();
        return;
      case 2:
      }
      MediaRouteProvider.this.deliverDiscoveryRequestChanged();
    }
  }

  public static abstract class Callback
  {
    public void onDescriptorChanged(MediaRouteProvider paramMediaRouteProvider, MediaRouteProviderDescriptor paramMediaRouteProviderDescriptor)
    {
    }
  }

  public static abstract class RouteController
  {
    public boolean onControlRequest(Intent paramIntent, MediaRouter.ControlRequestCallback paramControlRequestCallback)
    {
      return false;
    }

    public void onRelease()
    {
    }

    public void onSelect()
    {
    }

    public void onSetVolume(int paramInt)
    {
    }

    public void onUnselect()
    {
    }

    public void onUpdateVolume(int paramInt)
    {
    }
  }

  public static final class ProviderMetadata
  {
    private final String mPackageName;

    public ProviderMetadata(String paramString)
    {
      if (TextUtils.isEmpty(paramString))
        throw new IllegalArgumentException("packageName must not be null or empty");
      this.mPackageName = paramString;
    }

    public String getPackageName()
    {
      return this.mPackageName;
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder().append("ProviderMetadata{ packageName=");
      String str = this.mPackageName;
      return str + " }";
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.media.MediaRouteProvider
 * JD-Core Version:    0.6.2
 */