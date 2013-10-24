package com.google.cast;

public abstract interface MediaRouteAdapter
{
  public abstract void onDeviceAvailable(CastDevice paramCastDevice, String paramString, MediaRouteStateChangeListener paramMediaRouteStateChangeListener);

  public abstract void onSetVolume(double paramDouble);

  public abstract void onUpdateVolume(double paramDouble);
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.MediaRouteAdapter
 * JD-Core Version:    0.6.2
 */