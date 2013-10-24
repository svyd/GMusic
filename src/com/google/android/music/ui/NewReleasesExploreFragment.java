package com.google.android.music.ui;

import android.os.Bundle;
import com.google.android.music.medialist.ExploreNewReleasesAlbumList;

public class NewReleasesExploreFragment extends AlbumGridFragment
{
  private String getGenreId()
  {
    Bundle localBundle = getArguments();
    if (localBundle == null)
      throw new IllegalArgumentException("Either genre level, genre ID or genre name is required.");
    return localBundle.getString("nautilusId");
  }

  protected void initEmptyScreen()
  {
    super.initEmptyScreen();
    setEmptyImageView(2130837707);
    setEmptyScreenText(2131231365);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    String str = getGenreId();
    ExploreNewReleasesAlbumList localExploreNewReleasesAlbumList = new ExploreNewReleasesAlbumList(str);
    init(localExploreNewReleasesAlbumList);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.NewReleasesExploreFragment
 * JD-Core Version:    0.6.2
 */