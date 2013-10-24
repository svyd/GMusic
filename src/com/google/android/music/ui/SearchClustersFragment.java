package com.google.android.music.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.DocumentMenuHandler.DocumentContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.SearchDocumentBuilder;

public abstract class SearchClustersFragment extends BaseClusterListFragment
{
  protected final PlayCardView.ContextMenuDelegate mCardsContextMenuDelegate;
  protected boolean mPlayFirstSong;
  protected String mSearchString;

  public SearchClustersFragment()
  {
    DocumentMenuHandler.DocumentContextMenuDelegate localDocumentContextMenuDelegate = new DocumentMenuHandler.DocumentContextMenuDelegate(this);
    this.mCardsContextMenuDelegate = localDocumentContextMenuDelegate;
  }

  public SearchClustersFragment(String paramString)
  {
    DocumentMenuHandler.DocumentContextMenuDelegate localDocumentContextMenuDelegate = new DocumentMenuHandler.DocumentContextMenuDelegate(this);
    this.mCardsContextMenuDelegate = localDocumentContextMenuDelegate;
    init(paramString);
  }

  protected PlayCardView.ContextMenuDelegate getCardsContextMenuDelegate()
  {
    return this.mCardsContextMenuDelegate;
  }

  protected String[] getClusterProjection(int paramInt)
  {
    return SearchDocumentBuilder.CURSOR_COLUMNS;
  }

  protected void init(String paramString)
  {
    if (this.mSearchString != null)
      throw new IllegalStateException("Already initialized");
    this.mSearchString = paramString;
  }

  protected void initEmptyScreen()
  {
    super.initEmptyScreen();
    setupSearchEmptyScreen();
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    boolean bool = false;
    if (paramBundle != null)
    {
      String str = paramBundle.getString("com.google.android.music.ui.searchclusterfragment.searchstring");
      this.mSearchString = str;
      bool = true;
    }
    if (this.mSearchString == null)
      throw new IllegalStateException("Search string is not initialized");
    startLoading(bool);
    ListView localListView = getListView();
    AbsListView.OnScrollListener local1 = new AbsListView.OnScrollListener()
    {
      public void onScroll(AbsListView paramAnonymousAbsListView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
      }

      public void onScrollStateChanged(AbsListView paramAnonymousAbsListView, int paramAnonymousInt)
      {
        InputMethodManager localInputMethodManager = (InputMethodManager)SearchClustersFragment.this.getActivity().getApplicationContext().getSystemService("input_method");
        IBinder localIBinder = SearchClustersFragment.this.getView().getWindowToken();
        boolean bool = localInputMethodManager.hideSoftInputFromWindow(localIBinder, 0);
      }
    };
    localListView.setOnScrollListener(local1);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (this.mSearchString == null)
      return;
    String str = this.mSearchString;
    paramBundle.putString("com.google.android.music.ui.searchclusterfragment.searchstring", str);
  }

  public void setSearchString(String paramString, boolean paramBoolean)
  {
    this.mSearchString = paramString;
    this.mPlayFirstSong = paramBoolean;
    startLoading();
    setupSearchEmptyScreen();
  }

  protected void setupSearchEmptyScreen()
  {
    int i;
    if ((this.mSearchString == null) || (this.mSearchString.length() < 2))
    {
      i = 1;
      if (i == 0)
        break label45;
      setEmptyImageView(2130837710);
      clearEmptyScreenText();
    }
    while (true)
    {
      setEmptyScreenPadding(true);
      return;
      i = 0;
      break;
      label45: setEmptyImageView(2130837709);
      setEmptyScreenText(2131231371);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.SearchClustersFragment
 * JD-Core Version:    0.6.2
 */