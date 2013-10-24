package com.google.android.music.ui;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.music.NautilusStatus;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent.Explore;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.ViewUtils;

public class TopChartsExploreFragment extends ExploreClusterListFragment
  implements View.OnClickListener
{
  private ViewGroup mListHeaderView;

  private String getGenreId()
  {
    Bundle localBundle = getArguments();
    if (localBundle == null)
      throw new IllegalArgumentException("Either genre level, genre ID or genre name is required.");
    return localBundle.getString("nautilusId");
  }

  private void updateCardHeader()
  {
    boolean bool = false;
    if (!TextUtils.isEmpty(getGenreId()))
    {
      MusicPreferences localMusicPreferences = getPreferences();
      if (!localMusicPreferences.isNautilusEnabled())
        return;
      if (localMusicPreferences.showStartRadioButtonsInActionBar())
        return;
      LayoutInflater localLayoutInflater = getLayoutInflater(null);
      ViewGroup localViewGroup1 = this.mListHeaderView;
      View localView = localLayoutInflater.inflate(2130968625, localViewGroup1, false);
      ViewGroup localViewGroup2 = (ViewGroup)localView.findViewById(2131296408);
      TextView localTextView = (TextView)localView.findViewById(2131296409);
      localTextView.setText(2131230860);
      localTextView.setCompoundDrawablesWithIntrinsicBounds(2130837730, 0, 0, 0);
      int i = getResources().getDimensionPixelSize(2131558445);
      localTextView.setCompoundDrawablePadding(i);
      localViewGroup2.setOnClickListener(this);
      if (MusicUtils.isLandscape(getActivity()))
        int j = ViewUtils.setWidthToShortestEdge(getActivity(), localViewGroup2);
      this.mListHeaderView.addView(localView);
      return;
    }
    ViewGroup localViewGroup3 = this.mListHeaderView;
    TutorialCardsFactory.setupExploreTutorial(this, localViewGroup3);
    if ((this.mListHeaderView != null) && (this.mListHeaderView.getChildCount() > 0))
      bool = true;
    setIsCardHeaderShowing(bool);
  }

  protected Uri getGroupQueryUri(long paramLong)
  {
    String str = getGenreId();
    return MusicContent.Explore.getTopChartsUri(paramLong, str);
  }

  public void onClick(View paramView)
  {
    final String str1 = getArguments().getString("name");
    final String str2 = getGenreId();
    final String str3 = getArguments().getString("parentNautilusId");
    MusicUtils.runAsync(new Runnable()
    {
      public void run()
      {
        FragmentActivity localFragmentActivity1 = TopChartsExploreFragment.this.getActivity();
        String str1 = str2;
        String str2 = str3;
        String str3 = MusicUtils.getArtUrlsForGenreRadio(localFragmentActivity1, str1, str2);
        FragmentActivity localFragmentActivity2 = TopChartsExploreFragment.this.getActivity();
        String str4 = str1;
        String str5 = str2;
        MusicUtils.playGenreRadio(localFragmentActivity2, str4, str5, str3);
      }
    });
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Uri localUri = MusicContent.Explore.getTopChartGroupsUri(getGenreId());
    init(localUri);
  }

  public void onDestroyView()
  {
    super.onDestroyView();
    this.mListHeaderView = null;
  }

  protected void onNewNautilusStatus(NautilusStatus paramNautilusStatus)
  {
    super.onNewNautilusStatus(paramNautilusStatus);
    if (this.mListHeaderView == null)
      return;
    updateCardHeader();
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    ListView localListView = getListView();
    FragmentActivity localFragmentActivity = getActivity();
    FrameLayout localFrameLayout = new FrameLayout(localFragmentActivity);
    this.mListHeaderView = localFrameLayout;
    ViewGroup localViewGroup = this.mListHeaderView;
    localListView.addHeaderView(localViewGroup, null, false);
    updateCardHeader();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.TopChartsExploreFragment
 * JD-Core Version:    0.6.2
 */