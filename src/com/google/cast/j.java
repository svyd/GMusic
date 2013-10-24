package com.google.cast;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.media.MediaRouter.ControlRequestCallback;

final class j extends b
{
  private static final Logger b = new Logger("MinimalCastMediaRouteProvider");
  private MediaRouteAdapter c;

  public j(CastContext paramCastContext, MediaRouteAdapter paramMediaRouteAdapter)
  {
    super(paramCastContext);
    if (paramMediaRouteAdapter == null)
      throw new IllegalArgumentException("adapter cannot be null");
    this.c = paramMediaRouteAdapter;
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addCategory("com.google.cast.CATEGORY_CAST");
    localIntentFilter.addAction("com.google.cast.ACTION_GET_DEVICE");
    a(localIntentFilter);
  }

  protected b.a a(CastDevice paramCastDevice, String paramString)
  {
    MediaRouteAdapter localMediaRouteAdapter = this.c;
    return new a(paramCastDevice, paramString, localMediaRouteAdapter);
  }

  private final class a extends b.a
    implements MediaRouteStateChangeListener
  {
    boolean d;
    private MediaRouteAdapter f;

    public a(CastDevice paramString, String paramMediaRouteAdapter, MediaRouteAdapter arg4)
    {
      super(paramString, paramMediaRouteAdapter);
      Object localObject;
      this.f = localObject;
      this.d = false;
    }

    public boolean onControlRequest(Intent paramIntent, MediaRouter.ControlRequestCallback paramControlRequestCallback)
    {
      boolean bool = true;
      Logger localLogger = j.b();
      Object[] arrayOfObject = new Object[bool];
      arrayOfObject[0] = paramIntent;
      localLogger.d("Received control request %s", arrayOfObject);
      String str1 = paramIntent.getAction();
      if ("com.google.cast.ACTION_GET_DEVICE".equals(str1))
      {
        String str2 = paramIntent.getStringExtra("com.google.cast.EXTRA_ROUTE_ID");
        MediaRouteAdapter localMediaRouteAdapter = this.f;
        CastDevice localCastDevice = this.a;
        localMediaRouteAdapter.onDeviceAvailable(localCastDevice, str2, this);
      }
      while (true)
      {
        return bool;
        bool = false;
      }
    }

    public void onRelease()
    {
      this.f = null;
      super.onRelease();
    }

    public void onSelect()
    {
      this.d = true;
    }

    public void onSetVolume(int paramInt)
    {
      Logger localLogger = j.b();
      Object[] arrayOfObject = new Object[1];
      Integer localInteger = Integer.valueOf(paramInt);
      arrayOfObject[0] = localInteger;
      localLogger.d("onSetVolume: %d", arrayOfObject);
      if (!this.d)
        return;
      MediaRouteAdapter localMediaRouteAdapter = this.f;
      double d1 = paramInt / 20.0D;
      localMediaRouteAdapter.onSetVolume(d1);
    }

    public void onUnselect()
    {
      this.d = false;
    }

    public void onUpdateVolume(int paramInt)
    {
      Logger localLogger = j.b();
      Object[] arrayOfObject = new Object[1];
      Integer localInteger = Integer.valueOf(paramInt);
      arrayOfObject[0] = localInteger;
      localLogger.d("onUpdateVolume: %d", arrayOfObject);
      if (!this.d)
        return;
      MediaRouteAdapter localMediaRouteAdapter = this.f;
      double d1 = paramInt / 20.0D;
      localMediaRouteAdapter.onUpdateVolume(d1);
    }

    public void onVolumeChanged(double paramDouble)
    {
      if (!this.d)
        return;
      a(paramDouble);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.j
 * JD-Core Version:    0.6.2
 */