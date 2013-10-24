package com.google.android.music.ui;

import android.net.Uri;
import com.google.android.music.store.MusicContent.Explore;

public class RecommendedExploreFragment extends ExploreClusterListFragment
{
  public RecommendedExploreFragment()
  {
    super(localUri);
  }

  protected Uri getGroupQueryUri(long paramLong)
  {
    return MusicContent.Explore.getRecommendedUri(paramLong);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.RecommendedExploreFragment
 * JD-Core Version:    0.6.2
 */