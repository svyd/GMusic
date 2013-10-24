package com.google.android.music.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.music.NautilusStatus;
import com.google.android.music.medialist.MyRadioList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.ui.cardlib.model.DocumentClickHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.ViewUtils;

public class MyRadioFragment extends MediaListGridFragment
{
  static final String[] PROJECTION = arrayOfString;
  private MyRadioFragmentAdapter mAdapter;
  private TextView mCreateRadioStationText;
  private ViewGroup mPlayTutorialHeader;
  private PlayCardClusterMetadata.CardMetadata mTileMetadata;

  static
  {
    String[] arrayOfString = new String[4];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "radio_name";
    arrayOfString[2] = "radio_description";
    arrayOfString[3] = "radio_art";
  }

  public MyRadioFragment()
  {
    super(localMyRadioList, arrayOfString, false);
    PlayCardClusterMetadata.CardMetadata localCardMetadata = PlayCardClusterMetadata.CARD_SMALL;
    this.mTileMetadata = localCardMetadata;
  }

  private void updateCardHeader()
  {
    ViewGroup localViewGroup = this.mPlayTutorialHeader;
    TutorialCardsFactory.setupRadioTutorial(this, localViewGroup);
    if ((this.mPlayTutorialHeader != null) && (this.mPlayTutorialHeader.getChildCount() > 0));
    for (boolean bool = true; ; bool = false)
    {
      setIsCardHeaderShowing(bool);
      return;
    }
  }

  private void updateCreateRadioStationButton()
  {
    if (this.mCreateRadioStationText == null)
      return;
    if (UIStateManager.getInstance().getPrefs().isNautilusEnabled())
    {
      this.mCreateRadioStationText.setText(2131231295);
      return;
    }
    this.mCreateRadioStationText.setText(2131231297);
  }

  protected void initEmptyScreen()
  {
    super.initEmptyScreen();
    if (UIStateManager.getInstance().getPrefs().isNautilusEnabled())
    {
      setEmptyScreenText(2131231369);
      return;
    }
    setEmptyScreenText(2131231370);
  }

  MediaListCursorAdapter newAsyncCursorAdapter(Cursor paramCursor)
  {
    MyRadioFragmentAdapter localMyRadioFragmentAdapter = new MyRadioFragmentAdapter(this, paramCursor, null);
    this.mAdapter = localMyRadioFragmentAdapter;
    return this.mAdapter;
  }

  MediaListCursorAdapter newLoaderCursorAdapter()
  {
    MyRadioFragmentAdapter localMyRadioFragmentAdapter = new MyRadioFragmentAdapter(this, null);
    this.mAdapter = localMyRadioFragmentAdapter;
    return this.mAdapter;
  }

  public void onDestroyView()
  {
    super.onDestroyView();
    this.mPlayTutorialHeader = null;
    this.mCreateRadioStationText = null;
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    super.onLoadFinished(paramLoader, paramCursor);
    if (this.mAdapter == null)
      return;
    this.mAdapter.setShowFakeEvenIfEmpty(true);
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
    super.onLoaderReset(paramLoader);
    if (this.mAdapter == null)
      return;
    this.mAdapter.setShowFakeEvenIfEmpty(false);
  }

  protected void onNewNautilusStatus(NautilusStatus paramNautilusStatus)
  {
    super.onNewNautilusStatus(paramNautilusStatus);
    if (this.mPlayTutorialHeader != null)
      updateCardHeader();
    if (getPreferences().showStartRadioButtonsInActionBar())
      return;
    if (this.mCreateRadioStationText == null)
      return;
    updateCreateRadioStationButton();
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    ListView localListView = getListView();
    localListView.setFastScrollEnabled(false);
    localListView.setHeaderDividersEnabled(false);
    if (!getPreferences().showStartRadioButtonsInActionBar())
    {
      ViewGroup localViewGroup1 = (ViewGroup)getActivity().getLayoutInflater().inflate(2130968625, localListView, false);
      localListView.addHeaderView(localViewGroup1, null, false);
      ViewGroup localViewGroup2 = (ViewGroup)localViewGroup1.findViewById(2131296408);
      TextView localTextView = (TextView)localViewGroup1.findViewById(2131296409);
      this.mCreateRadioStationText = localTextView;
      updateCreateRadioStationButton();
      OnCreateNewStationListener localOnCreateNewStationListener = new OnCreateNewStationListener();
      localViewGroup2.setOnClickListener(localOnCreateNewStationListener);
      if (MusicUtils.isLandscape(getActivity()))
        int i = ViewUtils.setWidthToShortestEdge(getActivity(), localViewGroup2);
    }
    FragmentActivity localFragmentActivity = getActivity();
    FrameLayout localFrameLayout = new FrameLayout(localFragmentActivity);
    this.mPlayTutorialHeader = localFrameLayout;
    ViewGroup localViewGroup3 = this.mPlayTutorialHeader;
    localListView.addHeaderView(localViewGroup3, null, false);
    updateCardHeader();
  }

  final class OnCreateNewStationListener
    implements View.OnClickListener
  {
    OnCreateNewStationListener()
    {
    }

    public void onClick(View paramView)
    {
      SearchActivity.showCreateRadioSearch(MyRadioFragment.this.getActivity());
    }
  }

  final class MyRadioFragmentAdapter extends FakeNthCardMediaListCardAdapter
  {
    private MyRadioFragmentAdapter(MyRadioFragment arg2)
    {
      super(i, j, 0);
    }

    private MyRadioFragmentAdapter(MyRadioFragment paramCursor, Cursor arg3)
    {
    }

    protected void bindViewToLoadingItem(View paramView, Context paramContext)
    {
      if (!(paramView instanceof PlayCardView))
        return;
      ((PlayCardView)paramView).bindLoading();
    }

    protected void bindViewToMediaListItem(View paramView, Context paramContext, Cursor paramCursor, long paramLong)
    {
      Document localDocument = MusicUtils.getDocument(paramView);
      long l = paramCursor.getLong(0);
      String str1 = paramCursor.getString(1);
      String str2 = paramCursor.getString(2);
      String str3 = paramCursor.getString(3);
      Document.Type localType = Document.Type.RADIO;
      localDocument.setType(localType);
      localDocument.setId(l);
      localDocument.setTitle(str1);
      localDocument.setDescription(str2);
      localDocument.setArtUrl(str3);
      if (!(paramView instanceof PlayCardView))
        return;
      PlayCardView localPlayCardView = (PlayCardView)paramView;
      PlayCardView.ContextMenuDelegate localContextMenuDelegate = MyRadioFragment.this.mCardsContextMenuDelegate;
      localPlayCardView.bind(localDocument, localContextMenuDelegate);
    }

    public View getFakeView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Document localDocument = Document.getImFeelingLuckyDocument(this.mContext, false, false, false, false);
      PlayCardView localPlayCardView = (PlayCardView)paramView;
      PlayCardView.ContextMenuDelegate localContextMenuDelegate = MyRadioFragment.this.mCardsContextMenuDelegate;
      localPlayCardView.bind(localDocument, localContextMenuDelegate);
      localPlayCardView.setTag(localDocument);
      Context localContext = this.mContext;
      DocumentClickHandler localDocumentClickHandler = new DocumentClickHandler(localContext, localDocument);
      localPlayCardView.setOnClickListener(localDocumentClickHandler);
      return localPlayCardView;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView = super.getView(paramInt, paramView, paramViewGroup);
      if ((localView instanceof PlayCardView))
      {
        PlayCardView localPlayCardView = (PlayCardView)localView;
        float f = MyRadioFragment.this.mTileMetadata.getThumbnailAspectRatio();
        localPlayCardView.setThumbnailAspectRatio(f);
      }
      return localView;
    }

    public void setShowFakeEvenIfEmpty(boolean paramBoolean)
    {
      super.setShowFakeEvenIfEmpty(paramBoolean);
      MyRadioFragment localMyRadioFragment = MyRadioFragment.this;
      if (getCount() == 0);
      for (boolean bool = true; ; bool = false)
      {
        localMyRadioFragment.setEmptyScreenVisible(bool);
        return;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.MyRadioFragment
 * JD-Core Version:    0.6.2
 */