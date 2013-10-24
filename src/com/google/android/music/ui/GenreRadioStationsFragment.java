package com.google.android.music.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.utils.MusicUtils;

public class GenreRadioStationsFragment extends MediaListGridFragment
{
  static final String[] PROJECTION = arrayOfString;
  private GenreRadioStationsAdapter mAdapter;
  private String mParentArt;
  private String mParentGenre;
  private String mParentName;
  private PlayCardClusterMetadata.CardMetadata mTileMetadata;

  static
  {
    String[] arrayOfString = new String[4];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "genreServerId";
    arrayOfString[2] = "name";
    arrayOfString[3] = "genreArtUris";
  }

  public GenreRadioStationsFragment()
  {
    PlayCardClusterMetadata.CardMetadata localCardMetadata = PlayCardClusterMetadata.CARD_SMALL;
    this.mTileMetadata = localCardMetadata;
  }

  protected void initEmptyScreen()
  {
    super.initEmptyScreen();
    setEmptyImageView(0);
  }

  MediaListCursorAdapter newAsyncCursorAdapter(Cursor paramCursor)
  {
    GenreRadioStationsAdapter localGenreRadioStationsAdapter = new GenreRadioStationsAdapter(this, paramCursor);
    this.mAdapter = localGenreRadioStationsAdapter;
    return this.mAdapter;
  }

  MediaListCursorAdapter newLoaderCursorAdapter()
  {
    GenreRadioStationsAdapter localGenreRadioStationsAdapter = new GenreRadioStationsAdapter(this);
    this.mAdapter = localGenreRadioStationsAdapter;
    return this.mAdapter;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    Bundle localBundle = getArguments();
    if (localBundle == null)
      throw new IllegalArgumentException("Genre must be provided to show genre radio stations.");
    String str1 = localBundle.getString("nautilusId");
    this.mParentGenre = str1;
    String str2 = localBundle.getString("name");
    this.mParentName = str2;
    String str3 = localBundle.getString("artUrls");
    this.mParentArt = str3;
    if (!isInitialized())
    {
      String str4 = this.mParentGenre;
      GenreExploreList localGenreExploreList = new GenreExploreList(str4);
      String[] arrayOfString = PROJECTION;
      init(localGenreExploreList, arrayOfString, false);
    }
    super.onActivityCreated(paramBundle);
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    this.mAdapter.setShowFakeEvenIfEmpty(true);
    super.onLoadFinished(paramLoader, paramCursor);
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
    this.mAdapter.setShowFakeEvenIfEmpty(false);
    super.onLoaderReset(paramLoader);
  }

  private class GenreRadioStationsAdapter extends FakeNthCardMediaListCardAdapter
  {
    protected GenreRadioStationsAdapter(GenreRadioStationsFragment arg2)
    {
      super(i, j, 0);
    }

    protected GenreRadioStationsAdapter(GenreRadioStationsFragment paramCursor, Cursor arg3)
    {
    }

    protected void bindViewToLoadingItem(View paramView, Context paramContext)
    {
      ((TextView)paramView.findViewById(16908308)).setText("");
    }

    protected void bindViewToMediaListItem(View paramView, Context paramContext, Cursor paramCursor, long paramLong)
    {
      String str1 = paramCursor.getString(1);
      String str2 = paramCursor.getString(2);
      String str3 = paramCursor.getString(3);
      Document localDocument = MusicUtils.getDocument(paramView);
      Document.Type localType = Document.Type.GENRE;
      localDocument.setType(localType);
      localDocument.setGenreId(str1);
      localDocument.setTitle(str2);
      localDocument.setArtUrl(str3);
      localDocument.setIsEmulatedRadio(true);
      if (!(paramView instanceof PlayCardView))
        return;
      PlayCardView localPlayCardView = (PlayCardView)paramView;
      PlayCardView.ContextMenuDelegate localContextMenuDelegate = GenreRadioStationsFragment.this.mCardsContextMenuDelegate;
      localPlayCardView.bind(localDocument, localContextMenuDelegate);
      localPlayCardView.setThumbnailAspectRatio(1.0F);
    }

    public View getFakeView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Document localDocument = MusicUtils.getDocument(paramView);
      Document.Type localType = Document.Type.GENRE;
      localDocument.setType(localType);
      String str1 = GenreRadioStationsFragment.this.mParentGenre;
      localDocument.setGenreId(str1);
      GenreRadioStationsFragment localGenreRadioStationsFragment = GenreRadioStationsFragment.this;
      Object[] arrayOfObject = new Object[1];
      String str2 = GenreRadioStationsFragment.this.mParentName;
      arrayOfObject[0] = str2;
      String str3 = localGenreRadioStationsFragment.getString(2131231397, arrayOfObject);
      localDocument.setTitle(str3);
      localDocument.setIsEmulatedRadio(true);
      String str4 = GenreRadioStationsFragment.this.mParentArt;
      localDocument.setArtUrl(str4);
      if ((paramView instanceof PlayCardView))
      {
        PlayCardView localPlayCardView = (PlayCardView)paramView;
        PlayCardView.ContextMenuDelegate localContextMenuDelegate = GenreRadioStationsFragment.this.mCardsContextMenuDelegate;
        localPlayCardView.bind(localDocument, localContextMenuDelegate);
        float f = GenreRadioStationsFragment.this.mTileMetadata.getThumbnailAspectRatio();
        localPlayCardView.setThumbnailAspectRatio(f);
      }
      paramView.setOnClickListener(this);
      return paramView;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.GenreRadioStationsFragment
 * JD-Core Version:    0.6.2
 */