package com.google.android.music.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.google.android.music.medialist.RecommendedRadioList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.utils.MusicUtils;

public class RecommendedRadioFragment extends MediaListGridFragment
{
  static final String[] PROJECTION = arrayOfString;
  private PlayCardClusterMetadata.CardMetadata mTileMetadata;

  static
  {
    String[] arrayOfString = new String[4];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "playlist_name";
    arrayOfString[2] = "playlist_type";
    arrayOfString[3] = "hasLocal";
  }

  public RecommendedRadioFragment()
  {
    super(localRecommendedRadioList, arrayOfString, false);
    PlayCardClusterMetadata.CardMetadata localCardMetadata = PlayCardClusterMetadata.CARD_SMALL;
    this.mTileMetadata = localCardMetadata;
  }

  protected void initEmptyScreen()
  {
    super.initEmptyScreen();
    if (UIStateManager.getInstance().getPrefs().isNautilusEnabled())
    {
      setEmptyScreenText(2131231367);
      return;
    }
    setEmptyScreenText(2131231368);
  }

  MediaListCursorAdapter newAsyncCursorAdapter(Cursor paramCursor)
  {
    return new RecommendedRadioAdapter(this, paramCursor, null);
  }

  MediaListCursorAdapter newLoaderCursorAdapter()
  {
    return new RecommendedRadioAdapter(this, null);
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    getListView().setFastScrollEnabled(false);
  }

  final class RecommendedRadioAdapter extends MediaListCardAdapter
  {
    private RecommendedRadioAdapter(RecommendedRadioFragment arg2)
    {
      super(i);
    }

    private RecommendedRadioAdapter(RecommendedRadioFragment paramCursor, Cursor arg3)
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
      localDocument.setId(l);
      Document.Type localType = Document.Type.PLAYLIST;
      localDocument.setType(localType);
      String str = paramCursor.getString(i);
      localDocument.setTitle(str);
      localDocument.setPlaylistName(str);
      int k = paramCursor.getInt(2);
      localDocument.setPlaylistType(k);
      if (paramCursor.getInt(3) != 0);
      while (true)
      {
        localDocument.setHasLocal(i);
        if (!(paramView instanceof PlayCardView))
          return;
        PlayCardView localPlayCardView = (PlayCardView)paramView;
        PlayCardView.ContextMenuDelegate localContextMenuDelegate = RecommendedRadioFragment.this.mCardsContextMenuDelegate;
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
        float f = RecommendedRadioFragment.this.mTileMetadata.getThumbnailAspectRatio();
        localPlayCardView.setThumbnailAspectRatio(f);
      }
      return localView;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.RecommendedRadioFragment
 * JD-Core Version:    0.6.2
 */