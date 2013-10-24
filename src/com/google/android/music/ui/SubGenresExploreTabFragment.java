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

public class SubGenresExploreTabFragment extends SubFragmentsPagerFragment
{
  private String mGenreId;
  private String mGenreName;
  private String mParentGenreId;

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Bundle localBundle = getArguments();
    String str1 = localBundle.getString("nautilusId");
    this.mGenreId = str1;
    String str2 = localBundle.getString("name");
    this.mGenreName = str2;
    String str3 = localBundle.getString("parentNautilusId");
    this.mParentGenreId = str3;
    ArrayList localArrayList = new ArrayList();
    FragmentTabInfo localFragmentTabInfo1 = new FragmentTabInfo("top_charts", 2131230857, TopChartsExploreFragment.class, localBundle);
    boolean bool1 = localArrayList.add(localFragmentTabInfo1);
    FragmentTabInfo localFragmentTabInfo2 = new FragmentTabInfo("new_releases", 2131230856, NewReleasesExploreFragment.class, localBundle);
    boolean bool2 = localArrayList.add(localFragmentTabInfo2);
    init(localArrayList, "top_charts");
    setHasOptionsMenu(true);
  }

  public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    if (!getPreferences().showStartRadioButtonsInActionBar())
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
      final String str1 = this.mGenreId;
      final String str2 = this.mGenreName;
      final String str3 = this.mParentGenreId;
      SubGenresExploreTabFragment localSubGenresExploreTabFragment = this;
      MusicUtils.runAsyncWithCallback(new AsyncRunner()
      {
        String mArtUrls;

        public void backgroundTask()
        {
          Context localContext = localContext;
          String str1 = str1;
          String str2 = str3;
          String str3 = MusicUtils.getArtUrlsForGenreRadio(localContext, str1, str2);
          this.mArtUrls = str3;
        }

        public void taskCompleted()
        {
          FragmentActivity localFragmentActivity = SubGenresExploreTabFragment.this.getActivity();
          if (localFragmentActivity == null)
            return;
          String str1 = str2;
          String str2 = str1;
          String str3 = this.mArtUrls;
          MusicUtils.playGenreRadio(localFragmentActivity, str1, str2, str3);
        }
      });
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.SubGenresExploreTabFragment
 * JD-Core Version:    0.6.2
 */