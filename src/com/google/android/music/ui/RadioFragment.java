package com.google.android.music.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.music.preferences.MusicPreferences;
import java.util.ArrayList;

public class RadioFragment extends SubFragmentsPagerFragment
{
  public RadioFragment()
  {
    MusicPreferences localMusicPreferences = getPreferences();
    Bundle localBundle = GenreRadioGenreListFragment.buildArguments(0);
    if (localMusicPreferences.isTabletMusicExperience());
    for (float f = 0.5F; ; f = 1.0F)
    {
      ArrayList localArrayList = new ArrayList();
      FragmentTabInfo localFragmentTabInfo1 = new FragmentTabInfo("genres", 2131230863, GenreRadioGenreListFragment.class, localBundle, f);
      boolean bool1 = localArrayList.add(localFragmentTabInfo1);
      FragmentTabInfo localFragmentTabInfo2 = new FragmentTabInfo("stations", 2131230862, MyRadioFragment.class);
      boolean bool2 = localArrayList.add(localFragmentTabInfo2);
      FragmentTabInfo localFragmentTabInfo3 = new FragmentTabInfo("recommended", 2131230861, RecommendedRadioFragment.class);
      boolean bool3 = localArrayList.add(localFragmentTabInfo3);
      init(localArrayList, "stations");
      return;
    }
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setHasOptionsMenu(true);
  }

  public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    if (!getPreferences().showStartRadioButtonsInActionBar())
      return;
    if (getBaseActivity().isSideDrawerOpen())
      return;
    paramMenuInflater.inflate(2131820547, paramMenu);
    if (UIStateManager.getInstance().getPrefs().isNautilusEnabled())
    {
      MenuItem localMenuItem1 = paramMenu.findItem(2131296588).setTitle(2131231296);
      return;
    }
    MenuItem localMenuItem2 = paramMenu.findItem(2131296588).setTitle(2131231298);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default:
    case 2131296588:
    }
    for (boolean bool = super.onOptionsItemSelected(paramMenuItem); ; bool = true)
    {
      return bool;
      SearchActivity.showCreateRadioSearch(getActivity());
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.RadioFragment
 * JD-Core Version:    0.6.2
 */