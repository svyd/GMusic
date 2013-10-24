package com.google.android.music.cast;

import android.content.Context;
import android.net.Uri;
import com.google.android.gsf.Gservices;
import com.google.cast.CastContext;
import com.google.cast.Logger;
import com.google.cast.MediaRouteHelper;
import com.google.cast.MimeData;

public class CastMediaProviderFactory
{
  private CastContext mCastContext;

  public CastMediaProviderFactory(Context paramContext, boolean paramBoolean)
  {
    CastContext localCastContext = new CastContext(paramContext);
    this.mCastContext = localCastContext;
    if (!paramBoolean)
      return;
    Logger.setDebugEnabledByDefault(true);
  }

  public void destroy()
  {
    if (this.mCastContext == null)
      return;
    this.mCastContext.dispose();
    this.mCastContext = null;
  }

  public void registerMediaRouteProvider()
  {
    if (this.mCastContext == null)
      throw new IllegalStateException("CastMediaProviderFactory is not initialized");
    String str = Gservices.getString(this.mCastContext.getApplicationContext().getContentResolver(), "music_cast_app_name", "GoogleMusic");
    MimeData localMimeData = MimeData.createUrlData(Uri.parse("http://www.google.com/"));
    boolean bool = MediaRouteHelper.registerMediaRouteProvider(this.mCastContext, str, localMimeData, 3, true);
  }

  public void registerMinimalMediaRouteProvider()
  {
    if (this.mCastContext == null)
      throw new IllegalStateException("CastMediaProviderFactory is not initialized");
    CastContext localCastContext = this.mCastContext;
    Context localContext = this.mCastContext.getApplicationContext();
    MusicCastMediaRouteAdapter localMusicCastMediaRouteAdapter = new MusicCastMediaRouteAdapter(localContext);
    boolean bool = MediaRouteHelper.registerMinimalMediaRouteProvider(localCastContext, localMusicCastMediaRouteAdapter);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cast.CastMediaProviderFactory
 * JD-Core Version:    0.6.2
 */