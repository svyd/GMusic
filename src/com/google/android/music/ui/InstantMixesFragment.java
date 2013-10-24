package com.google.android.music.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.music.preferences.MusicPreferences;
import java.util.ArrayList;

public class InstantMixesFragment extends SubFragmentsPagerFragment
{
  public InstantMixesFragment()
  {
    ArrayList localArrayList = new ArrayList();
    FragmentTabInfo localFragmentTabInfo1 = new FragmentTabInfo("instant_mixes", 2131230865, MyRadioFragment.class);
    boolean bool1 = localArrayList.add(localFragmentTabInfo1);
    FragmentTabInfo localFragmentTabInfo2 = new FragmentTabInfo("recommended", 2131230864, RecommendedRadioFragment.class);
    boolean bool2 = localArrayList.add(localFragmentTabInfo2);
    init(localArrayList, "instant_mixes");
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
    paramMenuInflater.inflate(2131820547, paramMenu);
    MenuItem localMenuItem = paramMenu.findItem(2131296588).setTitle(2131231298);
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
 * Qualified Name:     com.google.android.music.ui.InstantMixesFragment
 * JD-Core Version:    0.6.2
 */