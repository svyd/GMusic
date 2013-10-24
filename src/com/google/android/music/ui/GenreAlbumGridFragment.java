package com.google.android.music.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.google.android.music.medialist.GenreAlbumList;
import com.google.android.music.medialist.GenreSongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;

public class GenreAlbumGridFragment extends AlbumGridFragment
{
  private AllSongsView mAllSongs;
  private long mGenreId;
  private String mGenreName;

  public static GenreAlbumGridFragment newInstance(GenreAlbumList paramGenreAlbumList)
  {
    GenreAlbumGridFragment localGenreAlbumGridFragment = new GenreAlbumGridFragment();
    localGenreAlbumGridFragment.init(paramGenreAlbumList);
    localGenreAlbumGridFragment.saveMediaListAsArguments();
    return localGenreAlbumGridFragment;
  }

  MediaListCursorAdapter newAsyncCursorAdapter(Cursor paramCursor)
  {
    PlayCardView.ContextMenuDelegate localContextMenuDelegate = this.mCardsContextMenuDelegate;
    return new GenreAlbumsAdapter(this, paramCursor, localContextMenuDelegate);
  }

  MediaListCursorAdapter newLoaderCursorAdapter()
  {
    PlayCardView.ContextMenuDelegate localContextMenuDelegate = this.mCardsContextMenuDelegate;
    return new GenreAlbumsAdapter(this, localContextMenuDelegate);
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    if ((paramCursor.getCount() > 1) && (!getPreferences().isTabletMusicExperience()))
      this.mAllSongs.show();
    super.onLoadFinished(paramLoader, paramCursor);
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    LayoutInflater localLayoutInflater = getActivity().getLayoutInflater();
    ListView localListView = getListView();
    localListView.setHeaderDividersEnabled(false);
    localListView.setFooterDividersEnabled(false);
    AllSongsView localAllSongsView1 = (AllSongsView)localLayoutInflater.inflate(2130968603, localListView, false);
    this.mAllSongs = localAllSongsView1;
    this.mAllSongs.hide();
    AllSongsView localAllSongsView2 = this.mAllSongs;
    localListView.addHeaderView(localAllSongsView2, null, false);
    final GenreAlbumList localGenreAlbumList = (GenreAlbumList)getMediaList();
    long l = localGenreAlbumList.getGenreId();
    this.mGenreId = l;
    MusicUtils.runAsyncWithCallback(new AsyncRunner()
    {
      private final Context mSavedContext;
      private String mSavedGenreName;

      public void backgroundTask()
      {
        GenreAlbumList localGenreAlbumList = localGenreAlbumList;
        Context localContext = this.mSavedContext;
        String str = localGenreAlbumList.getName(localContext);
        this.mSavedGenreName = str;
      }

      public void taskCompleted()
      {
        GenreAlbumGridFragment localGenreAlbumGridFragment = GenreAlbumGridFragment.this;
        String str1 = this.mSavedGenreName;
        String str2 = GenreAlbumGridFragment.access$002(localGenreAlbumGridFragment, str1);
        long l = GenreAlbumGridFragment.this.mGenreId;
        String str3 = GenreAlbumGridFragment.this.mGenreName;
        GenreSongList localGenreSongList = new GenreSongList(l, str3, -1);
        GenreAlbumGridFragment.this.mAllSongs.setSongList(localGenreSongList);
        GenreAlbumGridFragment.this.mAllSongs.invalidate();
      }
    });
  }

  final class GenreAlbumsAdapter extends AlbumsAdapter
  {
    protected GenreAlbumsAdapter(AlbumGridFragment paramCursor, Cursor paramContextMenuDelegate, PlayCardView.ContextMenuDelegate arg4)
    {
      super(paramContextMenuDelegate, localContextMenuDelegate);
      init();
    }

    protected GenreAlbumsAdapter(AlbumGridFragment paramContextMenuDelegate, PlayCardView.ContextMenuDelegate arg3)
    {
      super(localContextMenuDelegate);
      init();
    }

    public View getFakeView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Document localDocument = MusicUtils.getDocument(paramView);
      Document.Type localType = Document.Type.ALL_SONGS_GENRE;
      localDocument.setType(localType);
      long l = GenreAlbumGridFragment.this.mGenreId;
      localDocument.setId(l);
      String str1 = GenreAlbumGridFragment.this.mGenreName;
      localDocument.setTitle(str1);
      String str2 = getContext().getString(2131230751);
      localDocument.setSubTitle(str2);
      PlayCardView localPlayCardView = (PlayCardView)paramView;
      PlayCardView.ContextMenuDelegate localContextMenuDelegate = this.mCardsContextMenuDelegate;
      localPlayCardView.bind(localDocument, localContextMenuDelegate);
      localPlayCardView.setTag(localDocument);
      paramView.setOnClickListener(this);
      return localPlayCardView;
    }

    protected void init()
    {
      super.init();
      if (!GenreAlbumGridFragment.this.getPreferences().isTabletMusicExperience())
        return;
      setDisableFake(false);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.GenreAlbumGridFragment
 * JD-Core Version:    0.6.2
 */