package com.google.android.music.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.music.medialist.AlbumList;
import com.google.android.music.medialist.ArtistAlbumList;
import com.google.android.music.medialist.NautilusArtistAlbumList;

public class ArtistPageActivity extends BaseActivity
{
  public static final Intent buildStartIntent(Context paramContext, AlbumList paramAlbumList)
  {
    Intent localIntent1 = new Intent(paramContext, ArtistPageActivity.class);
    Intent localIntent2 = localIntent1.putExtra("albumlist", paramAlbumList);
    return localIntent1;
  }

  public static final void showArtist(Context paramContext, long paramLong, String paramString, boolean paramBoolean)
  {
    ArtistAlbumList localArtistAlbumList = new ArtistAlbumList(paramLong, paramString, true);
    Intent localIntent = buildStartIntent(paramContext, localArtistAlbumList);
    paramContext.startActivity(localIntent);
  }

  public static final void showNautilusArtist(Context paramContext, String paramString1, String paramString2)
  {
    NautilusArtistAlbumList localNautilusArtistAlbumList = new NautilusArtistAlbumList(paramString1, paramString2);
    Intent localIntent = buildStartIntent(paramContext, localNautilusArtistAlbumList);
    paramContext.startActivity(localIntent);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getContent() != null)
      return;
    ArtistAlbumsGridFragment localArtistAlbumsGridFragment = ArtistAlbumsGridFragment.newInstance((AlbumList)getIntent().getParcelableExtra("albumlist"));
    replaceContent(localArtistAlbumsGridFragment, false);
  }

  protected boolean usesActionBarOverlay()
  {
    return true;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.ArtistPageActivity
 * JD-Core Version:    0.6.2
 */