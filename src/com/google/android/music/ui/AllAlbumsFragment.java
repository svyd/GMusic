package com.google.android.music.ui;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.google.android.music.medialist.AlbumList;
import com.google.android.music.medialist.AllAlbumsList;

public class AllAlbumsFragment extends AlbumGridFragment
{
  protected void init(AlbumList paramAlbumList)
  {
    String[] arrayOfString = AlbumsAdapter.PROJECTION;
    init(paramAlbumList, arrayOfString, true);
  }

  protected void initEmptyScreen()
  {
    super.initEmptyScreen();
    if (UIStateManager.getInstance().isDisplayingLocalContent())
    {
      setEmptyScreenText(2131231364);
      setEmptyScreenLearnMoreVisible(true);
      return;
    }
    setEmptyScreenText(2131231363);
    setEmptyScreenLearnMoreVisible(false);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    if (!isInitialized())
    {
      AllAlbumsList localAllAlbumsList = new AllAlbumsList();
      init(localAllAlbumsList);
    }
    super.onActivityCreated(paramBundle);
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    getListView().setFastScrollEnabled(true);
    if (Build.VERSION.SDK_INT < 11)
      return;
    getListView().setScrollBarStyle(16777216);
    getListView().setFastScrollAlwaysVisible(true);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.AllAlbumsFragment
 * JD-Core Version:    0.6.2
 */