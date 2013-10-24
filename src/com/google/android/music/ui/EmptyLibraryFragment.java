package com.google.android.music.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.music.NautilusStatus;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.tutorial.OtherWaysToPlayListAdapter;
import com.google.android.music.tutorial.OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry;
import com.google.android.music.utils.TypefaceUtil;
import java.util.ArrayList;

public class EmptyLibraryFragment extends Fragment
{
  private ListView mList;
  private TextView mTitle;

  private void initListView()
  {
    OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry localOtherWaysToPlayListEntry1 = new OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry(2130837758, 2131231007, 2131231022);
    OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry localOtherWaysToPlayListEntry2 = new OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry(2130837759, 2131231009, 2131231020);
    OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry localOtherWaysToPlayListEntry3 = new OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry(2130837767, 2131231008, 2131231021);
    OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry localOtherWaysToPlayListEntry4 = new OtherWaysToPlayListAdapter.OtherWaysToPlayListEntry(2130837769, 2131231010, 2131231020);
    ArrayList localArrayList = new ArrayList();
    MusicPreferences localMusicPreferences = UIStateManager.getInstance().getPrefs();
    if (!localMusicPreferences.hasStreamingAccount())
    {
      boolean bool1 = localArrayList.add(localOtherWaysToPlayListEntry4);
      this.mTitle.setText(2131231373);
    }
    while (true)
    {
      FragmentActivity localFragmentActivity = getActivity();
      OtherWaysToPlayListAdapter localOtherWaysToPlayListAdapter = new OtherWaysToPlayListAdapter(localFragmentActivity, localMusicPreferences, 2130968641, localArrayList);
      this.mList.setAdapter(localOtherWaysToPlayListAdapter);
      return;
      if (localMusicPreferences.getNautilusStatus().isFreeTrialAvailable())
        boolean bool2 = localArrayList.add(localOtherWaysToPlayListEntry1);
      boolean bool3 = localArrayList.add(localOtherWaysToPlayListEntry2);
      boolean bool4 = localArrayList.add(localOtherWaysToPlayListEntry3);
      boolean bool5 = localArrayList.add(localOtherWaysToPlayListEntry4);
      this.mTitle.setText(2131231372);
    }
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    View localView = getView();
    if (localView == null)
      throw new IllegalStateException("Content view not yet created");
    ListView localListView = (ListView)localView.findViewById(2131296435);
    this.mList = localListView;
    TextView localTextView = (TextView)localView.findViewById(2131296434);
    this.mTitle = localTextView;
    TypefaceUtil.setTypeface(this.mTitle, 2);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(2130968640, paramViewGroup, false);
  }

  public void onStart()
  {
    super.onStart();
    initListView();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.EmptyLibraryFragment
 * JD-Core Version:    0.6.2
 */