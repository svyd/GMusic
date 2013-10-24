package com.google.android.music.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.google.android.music.NautilusStatus;
import com.google.android.music.medialist.AllArtistsList;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.utils.MusicUtils;

public class ArtistGridFragment extends MediaListGridFragment
{
  static final String[] PROJECTION = arrayOfString;
  private ViewGroup mPlayTutorialHeader;
  private PlayCardClusterMetadata.CardMetadata mTileMetadata;

  static
  {
    String[] arrayOfString = new String[5];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "artist";
    arrayOfString[2] = "hasLocal";
    arrayOfString[3] = "artworkUrl";
    arrayOfString[4] = "ArtistMetajamId";
  }

  private void updateCardHeader()
  {
    ViewGroup localViewGroup = this.mPlayTutorialHeader;
    TutorialCardsFactory.setupMyLibraryTutorial(this, localViewGroup);
    if ((this.mPlayTutorialHeader != null) && (this.mPlayTutorialHeader.getChildCount() > 0));
    for (boolean bool = true; ; bool = false)
    {
      setIsCardHeaderShowing(bool);
      return;
    }
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

  MediaListCursorAdapter newAsyncCursorAdapter(Cursor paramCursor)
  {
    return new ArtistsAdapter(this, paramCursor, null);
  }

  MediaListCursorAdapter newLoaderCursorAdapter()
  {
    return new ArtistsAdapter(this, null);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    if (!isInitialized())
    {
      PlayCardClusterMetadata.CardMetadata localCardMetadata = PlayCardClusterMetadata.CARD_SMALL_2x1;
      this.mTileMetadata = localCardMetadata;
      AllArtistsList localAllArtistsList = new AllArtistsList();
      String[] arrayOfString = PROJECTION;
      init(localAllArtistsList, arrayOfString, true);
    }
    super.onActivityCreated(paramBundle);
  }

  public void onDestroyView()
  {
    super.onDestroyView();
    this.mPlayTutorialHeader = null;
  }

  protected void onNewNautilusStatus(NautilusStatus paramNautilusStatus)
  {
    super.onNewNautilusStatus(paramNautilusStatus);
    if (this.mPlayTutorialHeader == null)
      return;
    updateCardHeader();
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    ListView localListView = getListView();
    localListView.setFastScrollEnabled(true);
    FragmentActivity localFragmentActivity = getActivity();
    FrameLayout localFrameLayout = new FrameLayout(localFragmentActivity);
    this.mPlayTutorialHeader = localFrameLayout;
    ViewGroup localViewGroup = this.mPlayTutorialHeader;
    localListView.addHeaderView(localViewGroup, null, false);
    updateCardHeader();
    if (Build.VERSION.SDK_INT < 11)
      return;
    localListView.setScrollBarStyle(16777216);
    localListView.setFastScrollAlwaysVisible(true);
  }

  final class ArtistsAdapter extends MediaListCardAdapter
  {
    private ArtistsAdapter(ArtistGridFragment arg2)
    {
      super(i);
    }

    private ArtistsAdapter(ArtistGridFragment paramCursor, Cursor arg3)
    {
      super(i, localCursor);
    }

    protected void bindViewToLoadingItem(View paramView, Context paramContext)
    {
      if (!(paramView instanceof PlayCardView))
        return;
      ((PlayCardView)paramView).bindLoading();
    }

    protected void bindViewToMediaListItem(View paramView, Context paramContext, Cursor paramCursor, long paramLong)
    {
      int i = 1;
      Document localDocument = MusicUtils.getDocument(paramView);
      long l = paramCursor.getLong(0);
      String str1 = paramCursor.getString(i);
      if (paramCursor.getInt(2) != 0);
      while (true)
      {
        localDocument.setId(l);
        localDocument.setArtistId(l);
        localDocument.setTitle(str1);
        localDocument.setArtistName(str1);
        Document.Type localType = Document.Type.ARTIST;
        localDocument.setType(localType);
        localDocument.setHasLocal(i);
        if (!paramCursor.isNull(4))
        {
          String str2 = paramCursor.getString(4);
          localDocument.setArtistMetajamId(str2);
        }
        if (!paramCursor.isNull(3))
        {
          String str3 = paramCursor.getString(3);
          localDocument.setArtUrl(str3);
        }
        if (!(paramView instanceof PlayCardView))
          return;
        PlayCardView localPlayCardView = (PlayCardView)paramView;
        PlayCardView.ContextMenuDelegate localContextMenuDelegate = ArtistGridFragment.this.mCardsContextMenuDelegate;
        localPlayCardView.bind(localDocument, localContextMenuDelegate);
        return;
        int j = 0;
      }
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView = super.getView(paramInt, paramView, paramViewGroup);
      if ((localView instanceof PlayCardView))
      {
        PlayCardView localPlayCardView = (PlayCardView)localView;
        float f = ArtistGridFragment.this.mTileMetadata.getThumbnailAspectRatio();
        localPlayCardView.setThumbnailAspectRatio(f);
      }
      return localView;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.ArtistGridFragment
 * JD-Core Version:    0.6.2
 */