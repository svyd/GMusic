package com.google.android.music.ui;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.google.android.music.medialist.AllGenreList;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.utils.MusicUtils;

public class GenreGridFragment extends MediaListGridFragment
{
  static final String[] PROJECTION = arrayOfString;
  private PlayCardClusterMetadata.CardMetadata mTileMetadata;

  static
  {
    String[] arrayOfString = new String[3];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "name";
    arrayOfString[2] = "hasLocal";
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
    return new GenresAdapter(this, paramCursor, null);
  }

  MediaListCursorAdapter newLoaderCursorAdapter()
  {
    return new GenresAdapter(this, null);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    if (!isInitialized())
    {
      PlayCardClusterMetadata.CardMetadata localCardMetadata = PlayCardClusterMetadata.CARD_SMALL_2x1;
      this.mTileMetadata = localCardMetadata;
      AllGenreList localAllGenreList = new AllGenreList();
      String[] arrayOfString = PROJECTION;
      init(localAllGenreList, arrayOfString, false);
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

  final class GenresAdapter extends MediaListCardAdapter
  {
    private GenresAdapter(GenreGridFragment arg2)
    {
      super(i);
      DataSetObserver local1 = new DataSetObserver()
      {
        public void onChanged()
        {
          GenreGridFragment.this.initEmptyScreen();
        }
      };
      registerDataSetObserver(local1);
    }

    private GenresAdapter(GenreGridFragment paramCursor, Cursor arg3)
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
      String str = paramCursor.getString(i);
      if (paramCursor.getInt(2) != 0);
      while (true)
      {
        localDocument.setTitle(str);
        Document.Type localType = Document.Type.GENRE;
        localDocument.setType(localType);
        localDocument.setHasLocal(i);
        if (!(paramView instanceof PlayCardView))
          return;
        PlayCardView localPlayCardView = (PlayCardView)paramView;
        PlayCardView.ContextMenuDelegate localContextMenuDelegate = GenreGridFragment.this.mCardsContextMenuDelegate;
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
        float f = GenreGridFragment.this.mTileMetadata.getThumbnailAspectRatio();
        localPlayCardView.setThumbnailAspectRatio(f);
      }
      return localView;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.GenreGridFragment
 * JD-Core Version:    0.6.2
 */