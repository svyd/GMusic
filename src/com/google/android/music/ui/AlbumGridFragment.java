package com.google.android.music.ui;

import android.database.Cursor;
import com.google.android.music.medialist.AlbumList;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;

public class AlbumGridFragment extends MediaListGridFragment
{
  protected PlayCardClusterMetadata.CardMetadata mTileMetadata;

  public AlbumGridFragment()
  {
    PlayCardClusterMetadata.CardMetadata localCardMetadata = PlayCardClusterMetadata.CARD_SMALL;
    this.mTileMetadata = localCardMetadata;
  }

  public static AlbumGridFragment newInstance(AlbumList paramAlbumList)
  {
    AlbumGridFragment localAlbumGridFragment = new AlbumGridFragment();
    localAlbumGridFragment.init(paramAlbumList);
    localAlbumGridFragment.saveMediaListAsArguments();
    return localAlbumGridFragment;
  }

  protected void init(AlbumList paramAlbumList)
  {
    String[] arrayOfString = AlbumsAdapter.PROJECTION;
    init(paramAlbumList, arrayOfString, false);
  }

  MediaListCursorAdapter newAsyncCursorAdapter(Cursor paramCursor)
  {
    PlayCardView.ContextMenuDelegate localContextMenuDelegate = this.mCardsContextMenuDelegate;
    return new AlbumsAdapter(this, paramCursor, localContextMenuDelegate);
  }

  MediaListCursorAdapter newLoaderCursorAdapter()
  {
    PlayCardView.ContextMenuDelegate localContextMenuDelegate = this.mCardsContextMenuDelegate;
    return new AlbumsAdapter(this, localContextMenuDelegate);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.AlbumGridFragment
 * JD-Core Version:    0.6.2
 */