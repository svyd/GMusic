package com.google.android.music.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;
import java.util.ArrayList;

public class GenresExploreTabFragment extends SubFragmentsPagerFragment
{
  private String mGenreId;
  private String mGenreName;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (getPreferences().isTabletMusicExperience());
    for (float f = 0.5F; ; f = 1.0F)
    {
      Bundle localBundle = getArguments();
      int i = localBundle.getInt("subgenreCount");
      String str1 = localBundle.getString("nautilusId");
      this.mGenreId = str1;
      String str2 = localBundle.getString("name");
      this.mGenreName = str2;
      ArrayList localArrayList = new ArrayList();
      if (i > 0)
      {
        FragmentTabInfo localFragmentTabInfo1 = new FragmentTabInfo("genres", 2131230859, GenresExploreFragment.class, localBundle, f);
        boolean bool1 = localArrayList.add(localFragmentTabInfo1);
      }
      FragmentTabInfo localFragmentTabInfo2 = new FragmentTabInfo("top_charts", 2131230857, TopChartsExploreFragment.class, localBundle);
      boolean bool2 = localArrayList.add(localFragmentTabInfo2);
      FragmentTabInfo localFragmentTabInfo3 = new FragmentTabInfo("new_releases", 2131230856, NewReleasesExploreFragment.class, localBundle);
      boolean bool3 = localArrayList.add(localFragmentTabInfo3);
      init(localArrayList, "top_charts");
      setHasOptionsMenu(true);
      return;
    }
  }

  public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    if (!UIStateManager.getInstance().getPrefs().showStartRadioButtonsInActionBar())
      return;
    if (getBaseActivity().isSideDrawerOpen())
      return;
    if (!UIStateManager.getInstance().getPrefs().isNautilusEnabled())
      return;
    paramMenuInflater.inflate(2131820545, paramMenu);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default:
    case 2131296582:
    }
    for (boolean bool = super.onOptionsItemSelected(paramMenuItem); ; bool = true)
    {
      return bool;
      final Context localContext = getActivity().getApplicationContext();
      final String str1 = this.mGenreName;
      final String str2 = this.mGenreId;
      MusicUtils.runAsyncWithCallback(new AsyncRunner()
      {
        String mArtUrls;

        public void backgroundTask()
        {
          Context localContext = localContext;
          String str1 = str2;
          String str2 = MusicUtils.getArtUrlsForGenreRadio(localContext, str1, null);
          this.mArtUrls = str2;
        }

        public void taskCompleted()
        {
          FragmentActivity localFragmentActivity = GenresExploreTabFragment.this.getActivity();
          if (localFragmentActivity == null)
            return;
          String str1 = str1;
          String str2 = str2;
          String str3 = this.mArtUrls;
          MusicUtils.playGenreRadio(localFragmentActivity, str1, str2, str3);
        }
      });
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.GenresExploreTabFragment
 * JD-Core Version:    0.6.2
 */