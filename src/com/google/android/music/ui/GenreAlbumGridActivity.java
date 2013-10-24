package com.google.android.music.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.music.medialist.AlbumList;
import com.google.android.music.medialist.GenreAlbumList;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;

public class GenreAlbumGridActivity extends BaseActivity
{
  private GenreAlbumList mGenreAlbumList;

  public static final Intent buildStartIntent(Context paramContext, AlbumList paramAlbumList)
  {
    Intent localIntent1 = new Intent(paramContext, GenreAlbumGridActivity.class);
    Intent localIntent2 = localIntent1.putExtra("genrealbumlist", paramAlbumList);
    return localIntent1;
  }

  public static final void showAlbumsOfGenre(Context paramContext, long paramLong, boolean paramBoolean)
  {
    GenreAlbumList localGenreAlbumList = new GenreAlbumList(paramLong, true);
    Intent localIntent = buildStartIntent(paramContext, localGenreAlbumList);
    paramContext.startActivity(localIntent);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    GenreAlbumList localGenreAlbumList = (GenreAlbumList)getIntent().getParcelableExtra("genrealbumlist");
    this.mGenreAlbumList = localGenreAlbumList;
    MusicUtils.runAsyncWithCallback(new AsyncRunner()
    {
      private String mGenreName;

      public void backgroundTask()
      {
        GenreAlbumList localGenreAlbumList = GenreAlbumGridActivity.this.mGenreAlbumList;
        GenreAlbumGridActivity localGenreAlbumGridActivity = GenreAlbumGridActivity.this;
        String str = localGenreAlbumList.getName(localGenreAlbumGridActivity);
        this.mGenreName = str;
      }

      public void taskCompleted()
      {
        GenreAlbumGridActivity localGenreAlbumGridActivity = GenreAlbumGridActivity.this;
        String str = this.mGenreName;
        localGenreAlbumGridActivity.setActionBarTitle(str);
      }
    });
    if (getContent() != null)
      return;
    GenreAlbumGridFragment localGenreAlbumGridFragment = GenreAlbumGridFragment.newInstance(this.mGenreAlbumList);
    replaceContent(localGenreAlbumGridFragment, false);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.GenreAlbumGridActivity
 * JD-Core Version:    0.6.2
 */