package com.google.android.music.ui;

import java.util.ArrayList;

public class MyLibraryFragment extends SubFragmentsPagerFragment
{
  public MyLibraryFragment()
  {
    ArrayList localArrayList = new ArrayList();
    FragmentTabInfo localFragmentTabInfo1 = new FragmentTabInfo("genres", 2131230850, GenreGridFragment.class);
    boolean bool1 = localArrayList.add(localFragmentTabInfo1);
    FragmentTabInfo localFragmentTabInfo2 = new FragmentTabInfo("artists", 2131230848, ArtistGridFragment.class);
    boolean bool2 = localArrayList.add(localFragmentTabInfo2);
    FragmentTabInfo localFragmentTabInfo3 = new FragmentTabInfo("albums", 2131230849, AllAlbumsFragment.class);
    boolean bool3 = localArrayList.add(localFragmentTabInfo3);
    FragmentTabInfo localFragmentTabInfo4 = new FragmentTabInfo("songs", 2131230851, AllTracksFragment.class);
    boolean bool4 = localArrayList.add(localFragmentTabInfo4);
    init(localArrayList, "artists");
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.MyLibraryFragment
 * JD-Core Version:    0.6.2
 */