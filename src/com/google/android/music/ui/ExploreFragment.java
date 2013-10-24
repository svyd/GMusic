package com.google.android.music.ui;

import android.os.Bundle;
import com.google.android.music.preferences.MusicPreferences;
import java.util.ArrayList;

public class ExploreFragment extends SubFragmentsPagerFragment
{
  public ExploreFragment()
  {
    if (getPreferences().isTabletMusicExperience());
    for (float f = 0.5F; ; f = 1.0F)
    {
      Bundle localBundle = GenresExploreFragment.buildArguments(0);
      ArrayList localArrayList = new ArrayList();
      FragmentTabInfo localFragmentTabInfo1 = new FragmentTabInfo("genres", 2131230858, GenresExploreFragment.class, localBundle, f);
      boolean bool1 = localArrayList.add(localFragmentTabInfo1);
      FragmentTabInfo localFragmentTabInfo2 = new FragmentTabInfo("top_charts", 2131230857, TopChartsExploreFragment.class, localBundle);
      boolean bool2 = localArrayList.add(localFragmentTabInfo2);
      FragmentTabInfo localFragmentTabInfo3 = new FragmentTabInfo("recommended", 2131230855, RecommendedExploreFragment.class);
      boolean bool3 = localArrayList.add(localFragmentTabInfo3);
      FragmentTabInfo localFragmentTabInfo4 = new FragmentTabInfo("new_releases", 2131230856, NewReleasesExploreFragment.class, localBundle);
      boolean bool4 = localArrayList.add(localFragmentTabInfo4);
      init(localArrayList, "top_charts");
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.ExploreFragment
 * JD-Core Version:    0.6.2
 */