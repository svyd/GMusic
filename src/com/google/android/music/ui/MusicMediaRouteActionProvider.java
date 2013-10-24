package com.google.android.music.ui;

import android.content.Context;
import android.support.v7.app.MediaRouteActionProvider;
import android.view.ContextThemeWrapper;

public class MusicMediaRouteActionProvider extends MediaRouteActionProvider
{
  public MusicMediaRouteActionProvider(Context paramContext)
  {
    super(localContext);
  }

  private static Context getThemedContext(Context paramContext)
  {
    return new ContextThemeWrapper(paramContext, 2131755089);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.MusicMediaRouteActionProvider
 * JD-Core Version:    0.6.2
 */