package android.support.v7.app;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.Callback;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.support.v7.mediarouter.R.attr;
import android.support.v7.mediarouter.R.id;
import android.support.v7.mediarouter.R.layout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MediaRouteControllerDialog extends Dialog
{
  private final MediaRouterCallback mCallback;
  private View mControlView;
  private boolean mCreated;
  private Drawable mCurrentIconDrawable;
  private Button mDisconnectButton;
  private Drawable mMediaRouteConnectingDrawable;
  private Drawable mMediaRouteOnDrawable;
  private final MediaRouter.RouteInfo mRoute;
  private final MediaRouter mRouter;
  private boolean mVolumeControlEnabled = true;
  private LinearLayout mVolumeLayout;
  private SeekBar mVolumeSlider;
  private boolean mVolumeSliderTouched;

  public MediaRouteControllerDialog(Context paramContext)
  {
    this(paramContext, 0);
  }

  public MediaRouteControllerDialog(Context paramContext, int paramInt)
  {
    super(localContext, paramInt);
    MediaRouter localMediaRouter = MediaRouter.getInstance(getContext());
    this.mRouter = localMediaRouter;
    MediaRouterCallback localMediaRouterCallback = new MediaRouterCallback(null);
    this.mCallback = localMediaRouterCallback;
    MediaRouter.RouteInfo localRouteInfo = this.mRouter.getSelectedRoute();
    this.mRoute = localRouteInfo;
  }

  private Drawable getIconDrawable()
  {
    if (this.mRoute.isConnecting())
      if (this.mMediaRouteConnectingDrawable == null)
      {
        Context localContext1 = getContext();
        int i = R.attr.mediaRouteConnectingDrawable;
        Drawable localDrawable1 = MediaRouterThemeHelper.getThemeDrawable(localContext1, i);
        this.mMediaRouteConnectingDrawable = localDrawable1;
      }
    for (Drawable localDrawable2 = this.mMediaRouteConnectingDrawable; ; localDrawable2 = this.mMediaRouteOnDrawable)
    {
      return localDrawable2;
      if (this.mMediaRouteOnDrawable == null)
      {
        Context localContext2 = getContext();
        int j = R.attr.mediaRouteOnDrawable;
        Drawable localDrawable3 = MediaRouterThemeHelper.getThemeDrawable(localContext2, j);
        this.mMediaRouteOnDrawable = localDrawable3;
      }
    }
  }

  private boolean isVolumeControlAvailable()
  {
    boolean bool = true;
    if ((this.mVolumeControlEnabled) && (this.mRoute.getVolumeHandling() == 1));
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  private boolean update()
  {
    boolean bool1 = true;
    if ((!this.mRoute.isSelected()) || (this.mRoute.isDefault()))
    {
      dismiss();
      bool1 = false;
    }
    while (true)
    {
      return bool1;
      String str = this.mRoute.getName();
      setTitle(str);
      updateVolume();
      Drawable localDrawable1 = getIconDrawable();
      Drawable localDrawable2 = this.mCurrentIconDrawable;
      if (localDrawable1 != localDrawable2)
      {
        this.mCurrentIconDrawable = localDrawable1;
        boolean bool2 = localDrawable1.setVisible(false, true);
        getWindow().setFeatureDrawable(3, localDrawable1);
      }
    }
  }

  private void updateVolume()
  {
    if (this.mVolumeSliderTouched)
      return;
    if (isVolumeControlAvailable())
    {
      this.mVolumeLayout.setVisibility(0);
      SeekBar localSeekBar1 = this.mVolumeSlider;
      int i = this.mRoute.getVolumeMax();
      localSeekBar1.setMax(i);
      SeekBar localSeekBar2 = this.mVolumeSlider;
      int j = this.mRoute.getVolume();
      localSeekBar2.setProgress(j);
      return;
    }
    this.mVolumeLayout.setVisibility(8);
  }

  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    MediaRouter localMediaRouter = this.mRouter;
    MediaRouteSelector localMediaRouteSelector = MediaRouteSelector.EMPTY;
    MediaRouterCallback localMediaRouterCallback = this.mCallback;
    localMediaRouter.addCallback(localMediaRouteSelector, localMediaRouterCallback, 2);
    boolean bool = update();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    boolean bool = getWindow().requestFeature(3);
    int i = R.layout.mr_media_route_controller_dialog;
    setContentView(i);
    int j = R.id.media_route_volume_layout;
    LinearLayout localLinearLayout = (LinearLayout)findViewById(j);
    this.mVolumeLayout = localLinearLayout;
    int k = R.id.media_route_volume_slider;
    SeekBar localSeekBar1 = (SeekBar)findViewById(k);
    this.mVolumeSlider = localSeekBar1;
    SeekBar localSeekBar2 = this.mVolumeSlider;
    SeekBar.OnSeekBarChangeListener local1 = new SeekBar.OnSeekBarChangeListener()
    {
      private final Runnable mStopTrackingTouch;

      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        if (!paramAnonymousBoolean)
          return;
        MediaRouteControllerDialog.this.mRoute.requestSetVolume(paramAnonymousInt);
      }

      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
        if (MediaRouteControllerDialog.this.mVolumeSliderTouched)
        {
          SeekBar localSeekBar = MediaRouteControllerDialog.this.mVolumeSlider;
          Runnable localRunnable = this.mStopTrackingTouch;
          boolean bool1 = localSeekBar.removeCallbacks(localRunnable);
          return;
        }
        boolean bool2 = MediaRouteControllerDialog.access$102(MediaRouteControllerDialog.this, true);
      }

      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
        SeekBar localSeekBar = MediaRouteControllerDialog.this.mVolumeSlider;
        Runnable localRunnable = this.mStopTrackingTouch;
        boolean bool = localSeekBar.postDelayed(localRunnable, 250L);
      }
    };
    localSeekBar2.setOnSeekBarChangeListener(local1);
    int m = R.id.media_route_disconnect_button;
    Button localButton1 = (Button)findViewById(m);
    this.mDisconnectButton = localButton1;
    Button localButton2 = this.mDisconnectButton;
    View.OnClickListener local2 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (MediaRouteControllerDialog.this.mRoute.isSelected())
          MediaRouteControllerDialog.this.mRouter.getDefaultRoute().select();
        MediaRouteControllerDialog.this.dismiss();
      }
    };
    localButton2.setOnClickListener(local2);
    this.mCreated = true;
    if (!update())
      return;
    View localView1 = onCreateMediaControlView(paramBundle);
    this.mControlView = localView1;
    int n = R.id.media_route_control_frame;
    FrameLayout localFrameLayout = (FrameLayout)findViewById(n);
    if (this.mControlView != null)
    {
      View localView2 = this.mControlView;
      localFrameLayout.addView(localView2);
      localFrameLayout.setVisibility(0);
      return;
    }
    localFrameLayout.setVisibility(8);
  }

  public View onCreateMediaControlView(Bundle paramBundle)
  {
    return null;
  }

  public void onDetachedFromWindow()
  {
    MediaRouter localMediaRouter = this.mRouter;
    MediaRouterCallback localMediaRouterCallback = this.mCallback;
    localMediaRouter.removeCallback(localMediaRouterCallback);
    super.onDetachedFromWindow();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = true;
    int i;
    if ((paramInt == 25) || (paramInt == 24))
    {
      MediaRouter.RouteInfo localRouteInfo = this.mRoute;
      if (paramInt == 25)
      {
        i = -1;
        localRouteInfo.requestUpdateVolume(i);
      }
    }
    while (true)
    {
      return bool;
      i = 1;
      break;
      bool = super.onKeyDown(paramInt, paramKeyEvent);
    }
  }

  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt == 25) || (paramInt == 24));
    for (boolean bool = true; ; bool = super.onKeyUp(paramInt, paramKeyEvent))
      return bool;
  }

  private final class MediaRouterCallback extends MediaRouter.Callback
  {
    private MediaRouterCallback()
    {
    }

    public void onRouteChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      boolean bool = MediaRouteControllerDialog.this.update();
    }

    public void onRouteUnselected(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      boolean bool = MediaRouteControllerDialog.this.update();
    }

    public void onRouteVolumeChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouter.RouteInfo localRouteInfo = MediaRouteControllerDialog.this.mRoute;
      if (paramRouteInfo != localRouteInfo)
        return;
      MediaRouteControllerDialog.this.updateVolume();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.MediaRouteControllerDialog
 * JD-Core Version:    0.6.2
 */