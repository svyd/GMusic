package android.support.v7.app;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.Callback;
import android.support.v7.media.MediaRouter.ProviderInfo;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import java.lang.ref.WeakReference;

public class MediaRouteActionProvider extends ActionProvider
{
  private MediaRouteButton mButton;
  private final MediaRouterCallback mCallback;
  private MediaRouteDialogFactory mDialogFactory;
  private final MediaRouter mRouter;
  private MediaRouteSelector mSelector;

  public MediaRouteActionProvider(Context paramContext)
  {
    super(paramContext);
    MediaRouteSelector localMediaRouteSelector = MediaRouteSelector.EMPTY;
    this.mSelector = localMediaRouteSelector;
    MediaRouteDialogFactory localMediaRouteDialogFactory = MediaRouteDialogFactory.getDefault();
    this.mDialogFactory = localMediaRouteDialogFactory;
    MediaRouter localMediaRouter = MediaRouter.getInstance(paramContext);
    this.mRouter = localMediaRouter;
    MediaRouterCallback localMediaRouterCallback = new MediaRouterCallback(this);
    this.mCallback = localMediaRouterCallback;
  }

  private void refreshRoute()
  {
    refreshVisibility();
  }

  public MediaRouteDialogFactory getDialogFactory()
  {
    return this.mDialogFactory;
  }

  public MediaRouteButton getMediaRouteButton()
  {
    return this.mButton;
  }

  public MediaRouteSelector getRouteSelector()
  {
    return this.mSelector;
  }

  public boolean isVisible()
  {
    MediaRouter localMediaRouter = this.mRouter;
    MediaRouteSelector localMediaRouteSelector = this.mSelector;
    return localMediaRouter.isRouteAvailable(localMediaRouteSelector, 1);
  }

  public View onCreateActionView()
  {
    if (this.mButton != null)
      int i = Log.e("MediaRouteActionProvider", "onCreateActionView: this ActionProvider is already associated with a menu item. Don't reuse MediaRouteActionProvider instances! Abandoning the old menu item...");
    MediaRouteButton localMediaRouteButton1 = onCreateMediaRouteButton();
    this.mButton = localMediaRouteButton1;
    this.mButton.setCheatSheetEnabled(true);
    MediaRouteButton localMediaRouteButton2 = this.mButton;
    MediaRouteSelector localMediaRouteSelector = this.mSelector;
    localMediaRouteButton2.setRouteSelector(localMediaRouteSelector);
    MediaRouteButton localMediaRouteButton3 = this.mButton;
    MediaRouteDialogFactory localMediaRouteDialogFactory = this.mDialogFactory;
    localMediaRouteButton3.setDialogFactory(localMediaRouteDialogFactory);
    MediaRouteButton localMediaRouteButton4 = this.mButton;
    ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, -1);
    localMediaRouteButton4.setLayoutParams(localLayoutParams);
    return this.mButton;
  }

  public MediaRouteButton onCreateMediaRouteButton()
  {
    Context localContext = getContext();
    return new MediaRouteButton(localContext);
  }

  public boolean onPerformDefaultAction()
  {
    if (this.mButton != null);
    for (boolean bool = this.mButton.showDialog(); ; bool = false)
      return bool;
  }

  public boolean overridesItemVisibility()
  {
    return true;
  }

  public void setDialogFactory(MediaRouteDialogFactory paramMediaRouteDialogFactory)
  {
    if (paramMediaRouteDialogFactory == null)
      throw new IllegalArgumentException("factory must not be null");
    if (this.mDialogFactory == paramMediaRouteDialogFactory)
      return;
    this.mDialogFactory = paramMediaRouteDialogFactory;
    if (this.mButton == null)
      return;
    this.mButton.setDialogFactory(paramMediaRouteDialogFactory);
  }

  public void setRouteSelector(MediaRouteSelector paramMediaRouteSelector)
  {
    if (paramMediaRouteSelector == null)
      throw new IllegalArgumentException("selector must not be null");
    if (this.mSelector.equals(paramMediaRouteSelector))
      return;
    if (!this.mSelector.isEmpty())
    {
      MediaRouter localMediaRouter1 = this.mRouter;
      MediaRouterCallback localMediaRouterCallback1 = this.mCallback;
      localMediaRouter1.removeCallback(localMediaRouterCallback1);
    }
    if (!paramMediaRouteSelector.isEmpty())
    {
      MediaRouter localMediaRouter2 = this.mRouter;
      MediaRouterCallback localMediaRouterCallback2 = this.mCallback;
      localMediaRouter2.addCallback(paramMediaRouteSelector, localMediaRouterCallback2);
    }
    this.mSelector = paramMediaRouteSelector;
    refreshRoute();
    if (this.mButton == null)
      return;
    this.mButton.setRouteSelector(paramMediaRouteSelector);
  }

  private static final class MediaRouterCallback extends MediaRouter.Callback
  {
    private final WeakReference<MediaRouteActionProvider> mProviderWeak;

    public MediaRouterCallback(MediaRouteActionProvider paramMediaRouteActionProvider)
    {
      WeakReference localWeakReference = new WeakReference(paramMediaRouteActionProvider);
      this.mProviderWeak = localWeakReference;
    }

    private void refreshRoute(MediaRouter paramMediaRouter)
    {
      MediaRouteActionProvider localMediaRouteActionProvider = (MediaRouteActionProvider)this.mProviderWeak.get();
      if (localMediaRouteActionProvider != null)
      {
        localMediaRouteActionProvider.refreshRoute();
        return;
      }
      paramMediaRouter.removeCallback(this);
    }

    public void onProviderAdded(MediaRouter paramMediaRouter, MediaRouter.ProviderInfo paramProviderInfo)
    {
      refreshRoute(paramMediaRouter);
    }

    public void onProviderChanged(MediaRouter paramMediaRouter, MediaRouter.ProviderInfo paramProviderInfo)
    {
      refreshRoute(paramMediaRouter);
    }

    public void onProviderRemoved(MediaRouter paramMediaRouter, MediaRouter.ProviderInfo paramProviderInfo)
    {
      refreshRoute(paramMediaRouter);
    }

    public void onRouteAdded(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      refreshRoute(paramMediaRouter);
    }

    public void onRouteChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      refreshRoute(paramMediaRouter);
    }

    public void onRouteRemoved(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      refreshRoute(paramMediaRouter);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.MediaRouteActionProvider
 * JD-Core Version:    0.6.2
 */